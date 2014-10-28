package com.lkb.bean;

import java.math.BigDecimal;
import java.util.Date;
/*
 * 联通话费详单
 * */
public class UnicomDetail {
	private String id;
	private String businessType; //业务类型
	private Date cTime;//通话起始时间
	private int tradeTime; //通信时长 
	private String callType; //呼叫类型
	private String recevierPhone; //对方号码 		
	private String tradeAddr; //通信地点 
	private String tradeType; //通话类型
		
	private BigDecimal basePay; //基本或漫游通话费
	private BigDecimal ldPay; //长途通话费
	private BigDecimal otherPay; //其他费
	private BigDecimal reductionPay; //优惠减免
	private BigDecimal totalPay; //小计
	private String phone;//手机号
	private int iscm;//是否当月 是的话保存1，不是的话保存0
	
	public UnicomDetail(){};
	public UnicomDetail(String phone) {
		super();
		this.phone = phone;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public Date getcTime() {
		return cTime;
	}
	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getRecevierPhone() {
		return recevierPhone;
	}
	public void setRecevierPhone(String recevierPhone) {
		this.recevierPhone = recevierPhone;
	}
	public String getTradeAddr() {
		return tradeAddr;
	}
	public void setTradeAddr(String tradeAddr) {
		this.tradeAddr = tradeAddr;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public BigDecimal getBasePay() {
		return basePay;
	}
	public void setBasePay(BigDecimal basePay) {
		this.basePay = basePay;
	}
	public BigDecimal getLdPay() {
		return ldPay;
	}
	public void setLdPay(BigDecimal ldPay) {
		this.ldPay = ldPay;
	}
	public BigDecimal getOtherPay() {
		return otherPay;
	}
	public void setOtherPay(BigDecimal otherPay) {
		this.otherPay = otherPay;
	}
	public BigDecimal getTotalPay() {
		return totalPay;
	}
	public void setTotalPay(BigDecimal totalPay) {
		this.totalPay = totalPay;
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
	public BigDecimal getReductionPay() {
		return reductionPay;
	}
	public void setReductionPay(BigDecimal reductionPay) {
		this.reductionPay = reductionPay;
	}
	public int getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(int tradeTime) {
		this.tradeTime = tradeTime;
	}
	

	
}
