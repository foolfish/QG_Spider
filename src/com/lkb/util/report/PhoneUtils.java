package com.lkb.util.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lkb.bean.User;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinFlowDetailService;
import com.lkb.service.IDianXinFlowService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileMessageService;
import com.lkb.service.IMobileOnlineBillService;
import com.lkb.service.IMobileOnlineListService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.ITelcomMessageService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomFlowBillService;
import com.lkb.service.IUnicomFlowService;
import com.lkb.service.IUnicomMessageService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.util.DateUtils;

public class PhoneUtils {
	public Map getEveryAmount(IDianXinTelService dianxinService,
			IUnicomTelService unicomTelService,
			IMobileTelService mobileTelService, List ydloginNames,
			List ltloginNames, List dxloginNames) {
		Map map11 = new HashMap();
		String a = "";
		String b = "";
		DateUtils dateUtils = new DateUtils();
		List<String> list2 = dateUtils.getMonthForm(12, "yyyy-MM");
		Collections.reverse(list2);
		for (int i = 0; i < list2.size(); i++) {
			BigDecimal mm = new BigDecimal("0");
			a += list2.get(i) + ",";

			if (ydloginNames != null && ydloginNames.size() > 0) {
				Map map1 = new HashMap(2);
				map1.put("list", ydloginNames);
				map1.put("cTime", list2.get(i) + "%");
				List<Map> list3 = mobileTelService.getEveryAmount(map1);
				if (list3 != null) {
					Map map = list3.get(0);
					if (map != null && map.get("amount") != null) {
						BigDecimal amount = new BigDecimal(map.get("amount")
								.toString());
						mm = mm.add(amount);

					}
				}
			}

			if (ltloginNames != null && ltloginNames.size() > 0) {
				Map map1 = new HashMap(2);
				map1.put("list", ltloginNames);
				map1.put("cTime", list2.get(i) + "%");
				List<Map> list32 = unicomTelService.getEveryAmount(map1);
				if (list32 != null) {
					Map map = list32.get(0);
					if (map != null && map.get("amount") != null) {
						BigDecimal amount = new BigDecimal(map.get("amount")
								.toString());
						mm = mm.add(amount);

					}
				}
			}

			if (dxloginNames != null && dxloginNames.size() > 0) {
				Map map1 = new HashMap(2);
				map1.put("list", dxloginNames);
				map1.put("cTime", list2.get(i) + "%");
				List<Map> list33 = dianxinService.getEveryAmount(map1);
				if (list33 != null) {
					Map map = list33.get(0);
					if (map != null && map.get("amount") != null) {
						BigDecimal amount = new BigDecimal(map.get("amount")
								.toString());
						mm = mm.add(amount);
					}
				}
			}

			b += mm + ",";
		}
		a = a.substring(0, a.length() - 1);
		b = b.substring(0, b.length() - 1);
		map11.put("phonetime", a);
		map11.put("phonevalues", b);

		return map11;
	}
	
	public List getDetail(IDianXinDetailService dianxinDetailService,ITelcomMessageService telcomMessageService,IDianXinTelService dianXinTelService,IDianXinFlowService dianXinFlowService,IDianXinFlowDetailService dianXinFlowDetailService,
			IUnicomDetailService unicomDetailService,IUnicomMessageService unicomMessageService,IUnicomTelService unicomTelService,IUnicomFlowBillService unicomFlowBillService,IUnicomFlowService unicomFlowService,
			IMobileDetailService mobileDetailService, IMobileMessageService mobileMessageService,IMobileTelService mobileTelService,IMobileOnlineBillService mobileOnlineBillService,IMobileOnlineListService mobileOnlineListService,
			IUserService userService,List ydloginNames,List ltloginNames, List dxloginNames,String parentId) {
		List listAll = new ArrayList();
		Map<String, String> pama = new HashMap<String, String>();
		pama.put("parentId", parentId);
		if(ydloginNames!=null && ydloginNames.size()>0){
			for(int i=0;i<ydloginNames.size();i++){
				String phone = ydloginNames.get(i).toString();
				List<Map> tel_list = mobileDetailService.getMobileDetail(phone);
				List<Map> message_list = mobileMessageService.getListByPhone(phone);
				List<Map> bill_list = mobileTelService.getFormatCtime(phone);
				List<Map> flow_bill = mobileOnlineBillService.getMobileOnlineBillForReport(phone);
				List<Map> flow_detail = mobileOnlineListService.getMobileOnlineListForReport(phone);
				pama.put("loginName", phone);
				List<Map> user_list = userService.getPhoneInfoForReport(pama);
				Map resultmap= user_list.get(0);
				Map mapA = new HashMap();
				mapA = checkNull(mapA,resultmap);
				mapA.put("phone", phone);
				mapA.put("source", "中国移动");
				mapA.put("telData", tel_list);
				mapA.put("messageData", message_list);
				mapA.put("billData", bill_list);
				mapA.put("flowBill", flow_bill);
				mapA.put("flowDetail", flow_detail);
				//mapA.put("user_data", user_list);
				listAll.add(mapA);
			}
		}
		if(ltloginNames!=null && ltloginNames.size()>0){
			for(int i=0;i<ltloginNames.size();i++){
				String phone = ltloginNames.get(i).toString();
				List<Map> tel_list = unicomDetailService.getUnicomDetail(phone);
				List<Map> message_list = unicomMessageService.getListByPhone(phone);
				List<Map> bill_list = unicomTelService.getFormatCtime(phone);
				List<Map> flow_bill = unicomFlowBillService.getUnicomFlowBillForReport(phone);
				List<Map> flow_detail = unicomFlowService.getUnicomFlowForReport(phone);
				pama.put("loginName", phone);
				List<Map> user_list = userService.getPhoneInfoForReport(pama);
				Map resultmap= user_list.get(0);
				Map mapA = new HashMap();
				mapA = checkNull(mapA,resultmap);
				mapA.put("phone", phone);
				mapA.put("source", "中国联通");
				mapA.put("telData", tel_list);
				mapA.put("messageData", message_list);
				mapA.put("billData", bill_list);
				mapA.put("flowBill", flow_bill);
				mapA.put("flowDetail", flow_detail);
				//mapA.put("user_data", user_list);
				listAll.add(mapA);
			}
		}
		if(dxloginNames!=null && dxloginNames.size()>0){
			for(int i=0;i<dxloginNames.size();i++){
				String phone = dxloginNames.get(i).toString();
				List<Map> tel_list = dianxinDetailService.getDianXinDetail(phone);
				List<Map> message_list = telcomMessageService.getListByPhone(phone);
				List<Map> bill_list = dianXinTelService.getFormatCtime(phone);
				List<Map> flow_bill = dianXinFlowService.getDianXinFlowForReport(phone);
				List<Map> flow_detail = dianXinFlowDetailService.getDianXinFlowDetailForReport(phone);
				pama.put("loginName", phone);
				List<Map> user_list = userService.getPhoneInfoForReport(pama);
				Map resultmap= user_list.get(0);
				Map mapA = new HashMap();
				mapA = checkNull(mapA,resultmap);
				mapA.put("phone", phone);
				mapA.put("source", "中国电信");
				mapA.put("telData", tel_list);
				mapA.put("messageData", message_list);
				mapA.put("billData", bill_list);
				mapA.put("flowBill", flow_bill);
				mapA.put("flowDetail", flow_detail);
				//mapA.put("user_data", user_list);
				listAll.add(mapA);
			}
		}
		return listAll;
	}
	private Map checkNull(Map mapA,Map resultmap){
		if(resultmap==null){
			mapA.put("idCard", "");	
			mapA.put("registerDate", "");	
			mapA.put("realName", "");
			mapA.put("addr", "");		
			mapA.put("email", "");
			mapA.put("phoneRemain", "");
			return mapA;
		}
		Set<String> set = resultmap.keySet();
		if(!set.contains("idCard")){
			mapA.put("idCard", "");
		}
		if(!set.contains("registerDate")){
			mapA.put("registerDate", "");
		}
		if(!set.contains("realName")){
			mapA.put("realName", "");
		}
		if(!set.contains("addr")){
			mapA.put("addr", "");
		}
		if(!set.contains("email")){
			mapA.put("email", "");
		}
		if(!set.contains("phoneRemain")){
			mapA.put("phoneRemain", "");
		}
		for (String string : set) {
			if(resultmap.get(string)==null){
				mapA.put(string, "");
			}else {
				mapA.put(string, resultmap.get(string));
			}
		}
		//System.out.println(resultmap);
		return mapA;
	}
}
