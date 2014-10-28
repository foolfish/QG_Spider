package com.lkb.thirdUtil.dx.test;
//package com.lkb.thirdUtil.dx.test;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.lkb.bean.DianXinTel;
import com.lkb.bean.User;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.thirdUtil.dx.test.testUtil.SeekUtil;
import com.lkb.thirdUtil.dx.test.testUtil.TelListComparator;

public class Mytest2 {
	DianXinTel[] telarr=new DianXinTel[3];
	@Test
	public void test(){
		//dx=new ShangHaiDianXin(spider, null, "18016252553", "026315", "2345", null);
		int i=0;
		DianXinTel tel1=new DianXinTel();
		tel1.setcTime(generateDate(2014,6,0,0,0,0));
		DianXinTel tel2=new DianXinTel();
		tel2.setcTime(generateDate(2014,5,2,12,24,2));
		DianXinTel tel3=new DianXinTel();
		tel3.setcTime(generateDate(2013,5,0,0,0,0));
		ArrayList<DianXinTel> telList=new ArrayList<DianXinTel>();
		telList.add(tel1);
		telList.add(tel2);
		telList.add(tel3);
		/*telarr=(DianXinTel[])telList.toArray();
		System.out.println("telarr"+telarr.length);*/
		for(DianXinTel te:telList){
			System.out.println("i="+i);
			telarr[i++]=te;
			
		}
		/*DianXinTel[] arr=new DianXinTel[2];
		System.out.println(3);
		DianXinTel[] telarr
		if(telList.toArray() instanceof DianXinTel[]){
			System.out.println(1);
			arr=(DianXinTel[])telList.toArray();
		}
		System.out.println(arr.length);*/
		SeekUtil.sort(telarr, new TelListComparator());
		System.out.println(5);
		int b=SeekUtil.binarySearch(telarr, tel2, new TelListComparator());
		System.out.println(b+"..."+12);
	}
	public Date generateDate(int Y,int M,int D,int H,int m,int s){
		 Date date = new Date();
	        GregorianCalendar gc = new GregorianCalendar();
	        gc.set(Calendar.YEAR,Y);//设置年
	        gc.set(Calendar.MONTH, M);//这里0是1月..以此向后推
	        gc.set(Calendar.DAY_OF_MONTH, D);//设置天
	        gc.set(Calendar.HOUR_OF_DAY,H);//设置小时
	        gc.set(Calendar.MINUTE, m);//设置分
	        gc.set(Calendar.SECOND, s);//设置秒
	        //gc.set(Calendar.MILLISECOND,200);//设置毫秒
	        date = gc.getTime();
	        return date;
	}
}
