package com.lkb.thirdUtil.dx.test.testUtil;
import java.util.Comparator;

import com.lkb.bean.DianXinTel;
import com.lkb.util.DateUtils;


public class TelListComparator implements Comparator<DianXinTel>{

	@Override
	public int compare(DianXinTel arg0, DianXinTel arg1) {
		// TODO Auto-generated method stub
		
		return DateUtils.formatDate(arg0.getcTime(), "yyyyMM").compareTo(DateUtils.formatDate(arg1.getcTime(), "yyyyMM"));
	}

	/*@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof DianXinTel){
			return this..equals((DianXinTel)obj);
		}
		return false;
	}*/

}
