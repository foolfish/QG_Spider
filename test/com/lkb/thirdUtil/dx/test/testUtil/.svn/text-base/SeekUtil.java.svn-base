package com.lkb.thirdUtil.dx.test.testUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.TelcomMessage;

public class SeekUtil {
	
	public static <T>void sort(T[] a,Comparator<T> c){
		Arrays.sort(a, 0, a.length, c);
	}
	
	public static <T>int binarySearch(T[] a,T key,Comparator<T> c){
		return Arrays.binarySearch(a, key, c);
	}
	/*public static <T> T[] toArray(Collection<DianXinTel> list){
		int length=list.size();
		T[] arr=Array.newInstance(,length)
		return null;
	}*/
	public static DianXinTel[] toTelArray(Collection<DianXinTel> list){
		int length=list.size();
		int i=0;
		DianXinTel[] telarr =(DianXinTel[]) Array.newInstance(DianXinTel.class, length);
		for( DianXinTel tel:list){
			telarr[i++]=tel;
		}
		return telarr;
	}
	public static DianXinDetail[] toTelArray(Collection<DianXinDetail> list){
		int length=list.size();
		int i=0;
		DianXinDetail[] telarr =(DianXinDetail[]) Array.newInstance(DianXinDetail.class, length);
		for( DianXinDetail tel:list){
			telarr[i++]=tel;
		}
		return telarr;
	}
	
	public static TelcomMessage[] toTelArray(Collection<TelcomMessage> list){
		int length=list.size();
		int i=0;
		TelcomMessage[] telarr =(TelcomMessage[]) Array.newInstance(TelcomMessage.class, length);
		for( TelcomMessage tel:list){
			telarr[i++]=tel;
		}
		return telarr;
	}
}
