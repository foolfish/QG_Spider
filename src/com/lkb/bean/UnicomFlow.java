package com.lkb.bean;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 
 * 联通流量
 * */
public class UnicomFlow {
	private String id;
	private Date startTime;//起始时间
	private String tradeType; //业务类型
	private String tradeAddr; //通信地点
	private BigDecimal allFlow;//总流量
	private BigDecimal allPay; //费用
	private String phone;//手机号
	private Date createTs;//插入时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getTradeAddr() {
		return tradeAddr;
	}
	public void settradeAddr(String tradeAddr) {
		this.tradeAddr = tradeAddr;
	}
	public BigDecimal getAllPay() {
		return allPay;
	}
	public void setAllPay(BigDecimal allPay) {
		this.allPay = allPay;
	}
	public BigDecimal getAllFlow() {
		return allFlow;
	}
	public void setAllFlow(BigDecimal allFlow) {
		this.allFlow = allFlow;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}
	
	
	
}
