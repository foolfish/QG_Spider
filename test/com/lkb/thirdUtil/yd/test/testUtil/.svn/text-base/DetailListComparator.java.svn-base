package com.lkb.thirdUtil.yd.test.testUtil;
import java.util.Comparator;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.MobileDetail;
import com.lkb.util.DateUtils;


public class DetailListComparator implements Comparator<MobileDetail>{

	@Override
	public int compare(MobileDetail o1, MobileDetail o2) {
		// TODO Auto-generated method stub
		return DateUtils.formatDate(o1.getcTime(), "yyyy-MM-dd HH:mm:ss").compareTo(DateUtils.formatDate(o2.getcTime(), "yyyy-MM-dd HH:mm:ss"));
	}
}
