package com.lkb.thirdUtil.base.pojo;

import java.util.Date;

import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileTel;
import com.lkb.bean.UnicomTel;
import com.lkb.util.DateUtils;



/**
 * 采集账单标示类
 * @author fastw
 * @date	2014-10-8 下午7:13:55
 */
public class MonthlyBillMark{
	
	public MonthlyBillMark(String month) {
		this.month = month;
	}
	public MonthlyBillMark(String month,String formatDate) {
		this.month = month;
		this.format = formatDate;
	}
	/**
	 * 存取状态
	 */
	private String text;
	/**
	 * 采集月份
	 */
	private String month;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	public MonthlyBillMark() {}
	/**
	 * 是否采集当月
	 */
	private boolean isSpiderCurrentMonth;
    private String format;
	
	public boolean isSpiderCurrentMonth() {
		return isSpiderCurrentMonth;
	}

	public void setSpiderCurrentMonth(boolean isSpiderCurrentMonth) {
		this.isSpiderCurrentMonth = isSpiderCurrentMonth;
	}

	
	
	public String getFormat() {
		return format;
	}
	/**
	 * 日期格式
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	public Date getFormatDate(){
		return DateUtils.StringToDate(getMonth(), getFormat());
	}
	
	private Object obj;

	public MonthlyBillMark setObj(Object obj) {
		this.obj = obj;
		return this;
	}
	public MobileTel getMobileTel() {
		return (MobileTel)obj;
	}
	public MobileOnlineBill getMobileOnlineBill() {
		return (MobileOnlineBill)obj;
	}
	public DianXinTel getDianxinTel() {
		return (DianXinTel)obj;
	}
	public DianXinFlow getDianXinFlow() {
		return (DianXinFlow)obj;
	}
	public UnicomTel getUnicomTel() {
		return (UnicomTel)obj;
	}
	
	
}
