package com.lkb.bean;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 
 * 联通短信
 * */
public class UnicomMessage {
	private String id;
	private Date sentTime;//发送时间
	private String tradeType; //业务类型
	private String recevierPhone; //对方号码 	
	private BigDecimal allPay; //费用
	private String phone;//手机号
	private Date createTs;//插入时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getSentTime() {
		return sentTime;
	}
	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getRecevierPhone() {
		return recevierPhone;
	}
	public void setRecevierPhone(String recevierPhone) {
		this.recevierPhone = recevierPhone;
	}
	public BigDecimal getAllPay() {
		return allPay;
	}
	public void setAllPay(BigDecimal allPay) {
		this.allPay = allPay;
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
