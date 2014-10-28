package com.lkb.thirdUtil.dx.test.testUtil;
import java.util.Comparator;

import com.lkb.bean.DianXinDetail;
import com.lkb.util.DateUtils;


public class DetailListComparator implements Comparator<DianXinDetail>{

	@Override
	public int compare(DianXinDetail o1, DianXinDetail o2) {
		// TODO Auto-generated method stub
		return DateUtils.formatDate(o1.getcTime(), "yyyy-MM-dd HH:mm:ss").compareTo(DateUtils.formatDate(o2.getcTime(), "yyyy-MM-dd HH:mm:ss"));
	}
}
