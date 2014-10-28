package com.lkb.thirdUtil.yd.test.testUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileTel;
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
	public static MobileTel[] toTelArray(Collection<MobileTel> list){
		int length=list.size();
		int i=0;
		MobileTel[] telarr =(MobileTel[]) Array.newInstance(MobileTel.class, length);
		for(MobileTel tel:list){
			telarr[i++]=tel;
		}
		return telarr;
	}
	public static MobileDetail[] toTelArray(Collection<MobileDetail> list){
		int length=list.size();
		int i=0;
		MobileDetail[] telarr =(MobileDetail[]) Array.newInstance(MobileDetail.class, length);
		for( MobileDetail tel:list){
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
