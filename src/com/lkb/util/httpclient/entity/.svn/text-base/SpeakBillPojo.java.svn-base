package com.lkb.util.httpclient.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.UnicomDetail;
import com.lkb.bean.UnicomMessage;
import com.lkb.bean.UnicomTel;
import com.lkb.util.DateUtils;




/**基础类
 * @author fastw
 * @date	2014-9-17 上午12:41:53
 */
public class SpeakBillPojo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3816007811111554709L;
	protected Logger logger = LoggerFactory.getLogger(SpeakBillPojo.class);
	private boolean isSpiderCurrentMonth;
	/**
	 * 存取状态
	 */
	private String text;
	/**
	 * 存取文本
	 */
	private LinkedList<String> listText;
	/**
	 * 采集月份
	 */
	private String month;
	
	private String format;
	
	private Date bigTime;
	
	private LinkedList<MobileDetail> mobileDetailList;
	
	private LinkedList<DianXinDetail> dianxinDetailList;
	
	private LinkedList<UnicomDetail> unicomDetailList;
	
	private LinkedList<UnicomMessage> unicomMessList;
	private LinkedList<TelcomMessage> telcomMessList;
	private LinkedList<MobileMessage> mobileMessList;
	
	
	
	private MobileTel mobileTel;
	
	private DianXinTel dianxinTel;
	
	private UnicomTel unicomTel;

	public SpeakBillPojo() {
	}
	public SpeakBillPojo(String month) {
		this.month = month;
	}

	/**账单参数
	 * @return
	 */
	public boolean isSpiderCurrentMonth() {
		return isSpiderCurrentMonth;
	}

	public void setSpiderCurrentMonth(boolean isSpiderCurrentMonth) {
		this.isSpiderCurrentMonth = isSpiderCurrentMonth;
	}

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
	public LinkedList<MobileDetail> getMobileDetailList() {
		return mobileDetailList;
	}
	public void setMobileDetailList(LinkedList<MobileDetail> mobileDetailList) {
		this.mobileDetailList = mobileDetailList;
	}
	public LinkedList<DianXinDetail> getDianxinDetailList() {
		return dianxinDetailList;
	}
	public void setDianxinDetailList(LinkedList<DianXinDetail> dianxinDetailList) {
		this.dianxinDetailList = dianxinDetailList;
	}
	public LinkedList<UnicomDetail> getUnicomDetailList() {
		return unicomDetailList;
	}
	public void setUnicomDetailList(LinkedList<UnicomDetail> unicomDetailList) {
		this.unicomDetailList = unicomDetailList;
	}
	public MobileTel getMobileTel() {
		return mobileTel;
	}
	public void setMobileTel(MobileTel mobileTel) {
		this.mobileTel = mobileTel;
	}
	public DianXinTel getDianxinTel() {
		return dianxinTel;
	}
	public void setDianxinTel(DianXinTel dianxinTel) {
		this.dianxinTel = dianxinTel;
	}
	public UnicomTel getUnicomTel() {
		return unicomTel;
	}
	public void setUnicomTel(UnicomTel unicomTel) {
		this.unicomTel = unicomTel;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public Date getFormatDate(){
		return DateUtils.StringToDate(getMonth(), getFormat());
	}
	/**
	 * 详单参数
	 * @return
	 */
	public Date getBigTime() {
		return bigTime;
	}
	public void setBigTime(Date bigTime) {
		this.bigTime = bigTime;
	}
	public LinkedList<UnicomMessage> getUnicomMessList() {
		return unicomMessList;
	}
	public void setUnicomMessList(LinkedList<UnicomMessage> unicomMessList) {
		this.unicomMessList = unicomMessList;
	}
	public LinkedList<TelcomMessage> getTelcomMessList() {
		return telcomMessList;
	}
	public void setTelcomMessList(LinkedList<TelcomMessage> telcomMessList) {
		this.telcomMessList = telcomMessList;
	}
	public LinkedList<MobileMessage> getMobileMessList() {
		return mobileMessList;
	}
	public void setMobileMessList(LinkedList<MobileMessage> mobileMessList) {
		this.mobileMessList = mobileMessList;
	}
	public LinkedList<String> getListText() {
		return listText;
	}
	public void setListText(LinkedList<String> listText) {
		this.listText = listText;
	}





}
