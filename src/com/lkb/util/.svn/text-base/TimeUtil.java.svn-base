package com.lkb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * 将时分秒转成int
 * */
public class TimeUtil {
	public static void main(String[] args){
		String splits = "0.00\"],[\"0.00";
		String[] ss =splits.split("\"\\],\\[\"");
		System.out.println(ss.length);
	}
	
	public  static int timetoint(String time){
		
		
		int s=0;
		time = time.replaceAll("钟", "").replaceAll("小时", "时");
		if(time.contains("秒")){
			if(time.contains("时")){
				s+=Integer.parseInt(time.substring(0,time.indexOf("时")))*3600;   
				//小时
			}
			if(time.contains("分")){
				s+=Integer.parseInt(time.substring(time.indexOf("时")+1,time.indexOf("分")))*60;   
				//分钟
			}
			//可能会出现1时5秒的情况，没有分！by Pat.Liu
			if(time.contains("秒")){
				s+=Integer.parseInt(time.substring(Math.max(time.indexOf("时")+1,time.indexOf("分")+1),time.indexOf("秒")));   
				//小时
			}
		}else{
			String split = ":";
			if (time.indexOf(split) < 0) {
				split = "'";
			}
			String[] strs = time.split(split);
			String hour = strs[0].replace(split, "");
			String min = strs[1].replace(split, "");
			String sencond = strs[2].replace(split, "");
			s+=Integer.parseInt(hour)*3600; 
			s+=Integer.parseInt(min)*60; 
			s+=Integer.parseInt(sencond); 
		}
	

		return s;
	}
	/**输入格式  0:0:10 //小时:分钟:秒
	 * 	     或者  0:0 //分钟:秒
	 *     或者 0 //秒
	 *通话时长
	 * 例如:广东移动 
	 * @return 通话时长的整型 (当时间格式错误时返回0)
	 */
	public  static int timetoint_HH_mm_ss(String time){
		try{
			String[] strs = time.split(":");
			if(strs.length==3){
				return Integer.parseInt(strs[0])*60*60+Integer.parseInt(strs[1])*60+Integer.parseInt(strs[2]);
			}else if (strs.length==2) {
				return Integer.parseInt(strs[0])*60+Integer.parseInt(strs[1]);
			}else if (strs.length==1) {
				return Integer.parseInt(strs[0]);
			}else {
				return 0;
			}
			
		}catch(Exception e){
			
		}
		return 0;
	}
	

	public static String getFirstDayOfMonth(int year, int month) {
		return getFirstDayOfMonth(year, month,"yyyy-MM-dd");
	}
	
	public static String getFirstDayOfMonth(int year, int month,String format) {
		Calendar cal = Calendar.getInstance();  
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH, month);  
		cal.set(Calendar.DAY_OF_MONTH, 1);  
		cal.add(Calendar.DAY_OF_MONTH, -1);  
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat(format).format(cal.getTime());
	}

	public static String getLastDayOfMonth(int year, int month,String format) {
		
		Calendar cal = Calendar.getInstance();  
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH, month);  
		cal.set(Calendar.DAY_OF_MONTH, 1);  
		cal.add(Calendar.DAY_OF_MONTH, -1);  
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	public static String getLastDayOfMonth(int year, int month) {
		return getLastDayOfMonth(year, month, "yyyy-MM-dd");
	}
	
}
