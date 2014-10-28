package com.lkb.util.taobao.alipay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	/**开始时间一年之内的*/
	public static String getStartTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		c.add(Calendar.YEAR, -1);
		c.add(Calendar.MONTH, +1);
		return sdf.format(c.getTime())+".1";
	}
	/**一年以后的criticalDate时间*/
	public static String getCriticalDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		c.add(Calendar.YEAR, -1);
		c.add(Calendar.MONTH, +1);
		return sdf.format(c.getTime())+"-01";
	}
	/***
	 * 一年之前的endTime时间 ,如果是以2013-7-1开始,那么这个时间就是2013-6-30
	 * @return
	 */
	public static String getOneYearBeforeTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		c.add(Calendar.YEAR, -1);
		c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
		return sdf.format(c.getTime());
	}
	
	/**开始时间*/
	public static String getEndTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(new Date());
	}
	public static void main(String[] args) {
		System.out.println(getStartTime());
		System.out.println(getEndTime());
		System.out.println(getOneYearBeforeTime());
	}

}
