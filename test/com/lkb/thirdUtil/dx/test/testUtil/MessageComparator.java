package com.lkb.thirdUtil.dx.test.testUtil;
import java.util.Comparator;


import com.lkb.bean.TelcomMessage;
import com.lkb.util.DateUtils;


public class MessageComparator implements Comparator<TelcomMessage>{

	@Override
	public int compare(TelcomMessage arg0, TelcomMessage arg1) {
		// TODO Auto-generated method stub
		
		return DateUtils.formatDate(arg0.getSentTime(), "yyyy-MM-dd HH:mm:ss").compareTo(DateUtils.formatDate(arg1.getSentTime(), "yyyy-MM-dd HH:mm:ss"));
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