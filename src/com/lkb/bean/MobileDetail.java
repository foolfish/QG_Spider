package com.lkb.bean;

import java.math.BigDecimal;
import java.util.Date;
/*
 * 移动话费详单
 * */
public class MobileDetail {
	private String id;
	private Date cTime;//起始时间
	private String tradeAddr; //通信地点 
	private String tradeWay; //通信方式 
	private String recevierPhone; //对方号码 
	private int tradeTime; //通信时长 
	private String tradeType; //通信类型 
	private String taocan; //套餐优惠 
	private BigDecimal onlinePay; //实收通信费
	private String phone;//手机号
	private int iscm;//是否当月 是的话保存1，不是的话保存0
	
	public MobileDetail() {
		
	}
	
	public MobileDetail(String phone) {
		super();
		this.phone = phone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getcTime() {
		return cTime;
	}
	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}
	public String getTradeAddr() {
		return tradeAddr;
	}
	public void setTradeAddr(String tradeAddr) {
		this.tradeAddr = tradeAddr;
	}
	public String getTradeWay() {
		return tradeWay;
	}
	public void setTradeWay(String tradeWay) {
		this.tradeWay = tradeWay;
	}
	public String getRecevierPhone() {
		return recevierPhone;
	}
	public void setRecevierPhone(String recevierPhone) {
		this.recevierPhone = recevierPhone;
	}
	public int getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(int tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getTaocan() {
		return taocan;
	}
	public void setTaocan(String taocan) {
		this.taocan = taocan;
	}
	public BigDecimal getOnlinePay() {
		return onlinePay;
	}
	public void setOnlinePay(BigDecimal onlinePay) {
		this.onlinePay = onlinePay;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getIscm() {
		return iscm;
	}
	public void setIscm(int iscm) {
		this.iscm = iscm;
	}

	

	
}
