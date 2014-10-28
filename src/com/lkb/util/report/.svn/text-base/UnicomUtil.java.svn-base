package com.lkb.util.report;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IPhoneNumService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.util.DateUtils;

public class UnicomUtil {
	//报告获取联系人信息
	public List<Map> getLTContacts(String phone,IUnicomDetailService unicomDetailService){
		Map<String,String> map=new HashMap();
		map.put("phone", phone);
		List<Map> list1=new ArrayList<Map>();
		List list=unicomDetailService.getUnicomDetailForReport(map);
		for(int i=0;i<list.size();i++){
			Map maptemp=new HashMap();
			Map<Object,Object> map1=(Map<Object,Object>)list.get(i);
			String phone1=map1.get("recevierPhone").toString();
			if(i+1<list.size()){
				Map<Object,Object> map2=(Map<Object,Object>)list.get(i+1);
				String phone2=map2.get("recevierPhone").toString();
				if(phone1.equals(phone2)){
					maptemp.put("phone", phone1);
					maptemp.put("place", map1.get("tradeAddr"));
					String type1=map1.get("callWay").toString();
					String type2=map2.get("callWay").toString();
					String zhujiao="";
					String beijiao="";
					if("主叫".equals(type1)){
						zhujiao=map1.get("num").toString();
						beijiao=map2.get("num").toString();
					}
					else{
						zhujiao=map2.get("num").toString();
						beijiao=map1.get("num").toString();
					}
					maptemp.put("zhujiao", zhujiao);
					maptemp.put("beijiao", beijiao);
					Integer totalint=Integer.parseInt(zhujiao)+Integer.parseInt(beijiao);
					maptemp.put("total",totalint.toString());
					String Times1=map1.get("tradetimes").toString();
					String Times2=map2.get("tradetimes").toString();
					if(Times1.contains(".")){
						Times1=Times1.replace(".0", "");
					}
					if(Times2.contains(".")){
						Times2=Times1.replace(".0", "");
					}
					Integer totalTimesint=Integer.parseInt(Times1)+Integer.parseInt(Times2);
					maptemp.put("totaltimes",totalTimesint.toString());
					list1.add(maptemp);
					i++;
				}
				else{
					maptemp.put("phone", phone1);
					maptemp.put("place", map1.get("tradeAddr").toString());
					String type1=map1.get("callWay").toString();
					
					String zhujiao="";
					String beijiao="";
					if("主叫".equals(type1)){
						
						zhujiao=map1.get("num").toString();
						beijiao="0";
					
					}
					else{
						
						beijiao=map1.get("num").toString();
						zhujiao="0";
					}
					maptemp.put("zhujiao", zhujiao);
					maptemp.put("beijiao", beijiao);
					maptemp.put("total",map1.get("num"));
				
					maptemp.put("totaltimes",map1.get("tradetimes").toString());
					list1.add(maptemp);
				}
			}
			else{
				maptemp.put("phone", phone1);
				maptemp.put("place", map1.get("tradeAddr").toString());
				String type1=map1.get("callWay").toString();
				
				String zhujiao="";
				String beijiao="";
				if("主叫".equals(type1)){
					
					zhujiao=map1.get("num").toString();
					beijiao="0";
				
				}
				else{
					
					beijiao=map1.get("num").toString();
					zhujiao="0";
				}
				maptemp.put("zhujiao", zhujiao);
				maptemp.put("beijiao", beijiao);
				maptemp.put("total",map1.get("num"));
				maptemp.put("totaltimes",map1.get("tradetimes").toString());
				list1.add(maptemp);
			}
	
		}
		return list1;
	}
	
	public List<Map> getLTBill(String phone,
			IUnicomTelService unicomTelService,
			IPhoneNumService phoneNumService,
			IUnicomDetailService unicomDetailService,IUserService userService) {
		Map map = new HashMap();
		map.put("teleno", phone);
		List<Map> list = unicomTelService.getUnicomTelForReport1(map);
		if (list.get(0) != null) {
			String avg = list.get(0).get("avg").toString();
			avg = avg.substring(0, avg.indexOf("."));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String earlest = list.get(0).get("earlest").toString()
					.substring(0, 10);
			Date date = null;
			try {
				date = df.parse(earlest);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			DateUtils du = new DateUtils();
			int days = 0;
			try {
				days = du.dayDist(earlest);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Map userMap = new HashMap();
			userMap.put("loginName", phone);
			userMap.put("usersource", Constant.LIANTONG);
			List<User> users = userService.getUserByUserNamesource(userMap);
			BigDecimal cAllBalance = new BigDecimal("0");
			if(users!=null && users.size()>0){
				User user = users.get(0);
				cAllBalance = user.getPhoneRemain();
			}
			
			Map map1 = new HashMap();
			map1.put("avg", avg);
			map1.put("days", days);
			map1.put("teleno", phone);
			map1.put("cAllBalance", cAllBalance);
			String phoneId = phone.substring(0, 7);
			PhoneNum phoneNum = phoneNumService.findById(phoneId);
			map1.put("local", phoneNum.getProvince());
			List<Map> list1 = unicomDetailService
					.getUnicomDetailForReport2(map);
			String lateststr ="";
			if(list1!=null&&list1.size()>0&&list1.get(0)!=null&&list1.get(0).get("latest")!=null){
				lateststr = list1.get(0).get("latest").toString();
				lateststr = lateststr.replaceAll("-", ".").substring(0, 10);
			}
			
			map1.put("latest", lateststr);
			list.clear();
			list.add(map1);
		} else {
			list.clear();
		}
		return list;
	}
}
