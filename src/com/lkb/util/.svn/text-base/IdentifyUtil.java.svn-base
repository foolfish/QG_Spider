package com.lkb.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.lkb.bean.IdentifyLocation;
import com.lkb.service.IIdentifyLocationService;
/*
 * 返回身份证信息
 * */
public class IdentifyUtil {
	
	public static void main(String[] args){
		String d1 = "140321198609110310";
		String d2 = "140321870911031";	
		String isex = d1.substring(16, 17);		
		String isex2 = d2.substring(14, 15);
		int sex1 = Integer.parseInt(isex);
		int sex2 = Integer.parseInt(isex2);
		String sex = "男";
		if(sex1%2==0){
			sex = "女";
		}
		
		
		
		System.out.println(sex);
		//System.out.println(isex2);
		
		
		
//		System.out.println(d1.substring(0, 6));
//		String sbirthday = d1.substring(6, 14);
//		String birthday = sbirthday.substring(0,4)+"."+ sbirthday.substring(4,6)+"."+ sbirthday.substring(6,8);
//		System.out.println(birthday);
//		
//		System.out.println(d2.substring(0, 6));
//		System.out.println(d2.substring(6, 12));
//
//		String sbirthday2 = d2.substring(6, 12);
//		
//		String birthday2 = "19"+sbirthday2.substring(0,2)+"."+ sbirthday2.substring(2,4)+"."+ sbirthday2.substring(4,6);
//		System.out.println(birthday2);
		
		
		
	}
	
	public Map getIdentify(String identify,IIdentifyLocationService identifyLocationService){
		Map map = new HashMap();
		if(identify!=null&&identify.length()>14){
			Calendar cal = Calendar.getInstance();
			if(identify.length()==15){
				String quyu = identify.substring(0,6);
				IdentifyLocation identifyLocation = identifyLocationService.findById(quyu);
				String city = identifyLocation.getCity();
				map.put("city", city);
				
				String sbirthday2 = identify.substring(6, 12);			
				String birthday = "19"+sbirthday2.substring(0,2)+"."+ sbirthday2.substring(2,4)+"."+ sbirthday2.substring(4,6);
				map.put("birthday", birthday);
				
				String year2 = "19"+identify.substring(6, 8);
				int iyear2 = Integer.parseInt(year2);				
				int nowyear = cal.get(Calendar.YEAR);
				int age = nowyear-iyear2;
				map.put("age", age);
				
	
				String isex2 = identify.substring(14, 15);
				int sex2 = Integer.parseInt(isex2);
				String sex = "男";
				if(sex2%2==0){
					sex = "女";
				}
				map.put("sex", sex);
				
			}else{
				String quyu = identify.substring(0,6);
				IdentifyLocation identifyLocation = identifyLocationService.findById(quyu);
				if(identifyLocation!=null){
					String city = identifyLocation.getCity();
					map.put("city", city);
				}else{
					map.put("city", "其他地区");
				}
				
				
				String sbirthday = identify.substring(6, 14);
				String birthday = sbirthday.substring(0,4)+"."+ sbirthday.substring(4,6)+"."+ sbirthday.substring(6,8);
				map.put("birthday", birthday);
				
				String year = identify.substring(6, 10);
				int iyear = Integer.parseInt(year);				
				int nowyear = cal.get(Calendar.YEAR);
				int age = nowyear-iyear;
				map.put("age", age);
				
				String isex = identify.substring(16, 17);		
				int sex1 = Integer.parseInt(isex);
				String sex = "男";
				if(sex1%2==0){
					sex = "女";
				}
				map.put("sex", sex);
				
				
				
			}
		}else{
			map.put("city", "");
			map.put("birthday", "");
			map.put("age", "");
			map.put("sex", "");
			
		}
		return map;
		
		
		
	}
}
