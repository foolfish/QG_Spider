package com.lkb.util.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.PayInfo;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IPayInfoService;
import com.lkb.service.IUserService;
import com.lkb.util.DateUtils;

/*
 * 支付宝工具类
 * */
public class PayUtils {
	public Map getMap(IPayInfoService payInfoService, List alipayNames){
		Map map = new HashMap();
		
/*		Map map11 = new HashMap();
		map11.put("parentId", currentUser);
		map11.put("usersource", Constant.ZHIFUBAO);
		List<User> users = userService.getUserByParentIdSource(map11);*/
		if(alipayNames!=null && alipayNames.size()>0){
			
		}else{
			map.put("domain", "支付宝");
			map.put("registerDate", "无");
			map.put("recentDay", "0");
			map.put("amount",  "0");
			map.put("paycount",  "0");
			map.put("hamount",  "0");
			map.put("allamount",  "0");
			map.put("sumcount",  "0");
			return map;
		}
		Map map1 = new HashMap(1);
		map1.put("list", alipayNames);
		BigDecimal zero = new BigDecimal("0");
		List<Map> list1 = payInfoService.getFirstDay(map1);
		if(list1!=null && list1.size()>0){
			Map map2 = list1.get(0);
			if(map2!=null && map2.get("payTime")!=null){
				map.put("domain", "支付宝");
				//注册时间
				Date firstDay = (Date)map2.get("payTime");				
				java.text.DateFormat format2 = new java.text.SimpleDateFormat(
						"yyyy.MM.dd");
				String registerDate = format2.format(firstDay);
				map.put("registerDate", registerDate);
				
				//最后使用时间
				Map map12 = new HashMap(1);
				map12.put("list", alipayNames);
				List<Map> list12 = payInfoService.getRecentDay(map12);
				if(list12!=null && list12.size()>0){
					Map map21 = list12.get(0);
					if(map21!=null && map21.get("payTime")!=null){
						Date recentDay = (Date)map21.get("payTime");				
						String recentDays = format2.format(recentDay);
						map.put("recentDay", recentDays);
					}
				}
				
				//代付金额和次数
				Map map13 = new HashMap(1);
				map13.put("list", alipayNames);
				List<Map> list13 = payInfoService.getamountcount(map13);
				if(list13!=null &&list13.size()>0){
					Map map21 = list13.get(0);
					if(map21!=null && map21.get("amount")!=null){
						BigDecimal amount = new BigDecimal( map21.get("amount").toString());	
						amount = zero.subtract(amount);
						int paycount =  Integer.parseInt(map21.get("paycount").toString());
						map.put("amount", amount);
						map.put("paycount", paycount);
					}else{
						map.put("amount", "0");
						map.put("paycount", "0");
					}
				}else{
					map.put("amount", "0");
					map.put("paycount", "0");
				}
				
				//代付最大金额
				Map map14 = new HashMap(1);
				map14.put("list", alipayNames);
				List<Map> list14 = payInfoService.getLargeAmount(map14);
				if(list14!=null &&list14.size()>0){
					Map map21 = list14.get(0);
					if(map21!=null && map21.get("amount")!=null){
						BigDecimal hamount = new BigDecimal( map21.get("amount").toString());		
						hamount = zero.subtract(hamount);
						map.put("hamount", hamount);
					}
				}
				
				//全部费用和次数
				
				Map map15 = new HashMap(1);
				map15.put("list", alipayNames);
				List<Map> list15 = payInfoService.getAllSum(map15);
				if(list15!=null &&list15.size()>0){
					Map map21 = list15.get(0);
					if(map21!=null && map21.get("amount")!=null){
						BigDecimal allamount = new BigDecimal( map21.get("amount").toString());	
						allamount = zero.subtract(allamount);
						int sumcount =  Integer.parseInt(map21.get("sumcount").toString());						
						map.put("allamount", allamount);
						map.put("sumcount", sumcount);
					}
				}
	
			}

		}
		return map;
	}
	
	/*
	 * 获得每个月支付宝的金额
	 * */
	public Map getEveryAmount(IPayInfoService payInfoService, List alipayNames){
		Map map11 = new HashMap();
		String a = "";
		String b = "";
		DateUtils dateUtils = new DateUtils();
		List<String> list2 =  dateUtils.getMonthForm(12,"yyyy-MM");
		Collections.reverse(list2);
		for(int i=0;i<list2.size();i++){
			BigDecimal mm= new BigDecimal("0");
			a += list2.get(i)+",";
			if(alipayNames!=null && alipayNames.size()>0){
				Map map1 = new HashMap(2);
				map1.put("list", alipayNames);
				map1.put("payTime", list2.get(i)+"%");
				List<Map>  list3 = payInfoService.getEveryAmount(map1);
				if(list3!=null){
					Map map = list3.get(0);
					if(map!=null && map.get("amount")!=null){
						BigDecimal amount= new BigDecimal(map.get("amount").toString());
						mm = amount;
											
					}
				}
				
			}	
			b += mm+",";
		}
		a =a.substring(0,a.length()-1);
		b =b.substring(0,b.length()-1);
		map11.put("time", a);
		map11.put("values", b);
	
		return map11;
	}
	
	
	/*
	 * 
	 * */
	public List getMap2(IPayInfoService payInfoService, List alipayNames){
		List list = new ArrayList();
		for(int i=0;i<alipayNames.size();i++){
			String alipayName = alipayNames.get(i).toString();
			Map map2 = new HashMap();
			map2.put("alipayName",alipayName);
			map2.put("source", Constant.ZHIFUBAO);
		
			List<PayInfo> pays = payInfoService.getPayInfoByBaseUserIdSource(map2);
			
			Map map3 = new HashMap();
			map3.put("alipayName", alipayName);
			map3.put("source", Constant.ZHIFUBAO);
			map3.put("data", pays);
			list.add(map3);
		}
		return list;
	}
	
}
