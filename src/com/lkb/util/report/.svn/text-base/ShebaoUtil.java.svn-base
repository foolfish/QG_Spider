package com.lkb.util.report;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.service.ISheBaoService;
import com.lkb.util.DateUtils;

public class ShebaoUtil {
	public static void main(String[] args){
		 int shebaocount = 2;
		 int year=shebaocount/12;
		 int remainmonth = shebaocount%12;
		 String mm="";
		 if(year>0){
			 mm=year+"年"+remainmonth+"月";
		 }else{
			 mm=remainmonth+"月";
		 }
		 System.out.println(mm);
	}
	
	/*
	 * 获得每个月消毒的金额
	 * */
	public Map getEveryAmount(ISheBaoService shebaoService, List sb_loginNames){
		Map map11 = new HashMap();
		String a = "";
		String b = "";
		DateUtils dateUtils = new DateUtils();
		List<String> list2 =  dateUtils.getMonthForm(12,"yyyy-MM");
		Collections.reverse(list2);
		for(int i=0;i<list2.size();i++){
			BigDecimal mm= new BigDecimal("0");
			a += list2.get(i)+",";
			Map map1 = new HashMap(2);
			map1.put("list", sb_loginNames);
			map1.put("payTime", list2.get(i)+"%");
			List<Map>  list3 = shebaoService.getEveryAmount(map1);
			if(list3!=null){
				Map map = list3.get(0);
				if(map!=null && map.get("amount")!=null){
					BigDecimal amount= new BigDecimal(map.get("amount").toString());
					mm = amount;
										
				}
			}
			b += mm+",";
		}
		a =a.substring(0,a.length()-1);
		b =b.substring(0,b.length()-1);
		map11.put("shebaotime", a);
		map11.put("shebaovalues", b);
	
		return map11;
	}
	
	/*
	 * 得到用户工龄
	 * */
	public Map getWorkYear(ISheBaoService shebaoService, String currentUser){
		Map map = new HashMap();
		Map map1 = new HashMap(1);
		map1.put("baseUserId", currentUser);
		int shebaocount =0;
		List<Map> list1 = shebaoService.getCount(map1);
		if(list1!=null && list1.size()>0){
			Map map2 =list1.get(0);
			if(map2!=null && map2.get("shebaocount")!=null){
				 shebaocount = Integer.parseInt(map2.get("shebaocount").toString());				
				 if(shebaocount>0){
					 String allwork = getYearMonth(shebaocount);
					 map.put("allwork", allwork);
					 
					 List<Map> list2 = shebaoService.getRecentCompany(map1);
					 if(list2!=null && list2.size()>0){
						 Map map3 = list2.get(0);
						 if(map3!=null && map3.get("payCompany")!=null){
							 String payCompany =  map3.get("payCompany").toString();
							 Map map11 = new HashMap(2);
							 map11.put("baseUserId", currentUser);
							 map11.put("payCompany", payCompany);
							 List<Map> list3 = shebaoService.getCount(map11);
							 if(list3!=null && list3.size()>0){
								 Map map31 = list3.get(0);
								 if(map31!=null && map31.get("shebaocount")!=null){
									 int shebaocount2 = Integer.parseInt( map31.get("shebaocount").toString());
									String nowwork = getYearMonth(shebaocount2);
									 map.put("nowwork", nowwork);
								 	}
									 
								 }
						 }else{
							 map.put("nowwork", "-");
						 }
					 }
				 }
			}
		}
		if(shebaocount==0){
			map.put("allwork", "-");
			map.put("nowwork", "-");
		}
		
		return map;
	}
	
	/*
	 * 计算月对应的年月
	 * */
	public String  getYearMonth(int month){
		
		 int year=month/12;
		 int remainmonth = month%12;
		 String mm="";
		 if(year>0){
			 mm=year+"年"+remainmonth+"月";
		 }else{
			 mm=remainmonth+"月";
		 }
		 return mm;
	}
	
}
