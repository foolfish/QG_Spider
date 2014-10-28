package com.lkb.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lkb.util.DateUtils;
/*
 * 电信话费详单
 * */
public class DianXinDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String tradeType; //通话类型 ：长途 本地	
	private Date cTime;//通话开始时间
	private int tradeTime; //通信时长 
	private String callWay; //呼叫类型:主叫 被叫
	private String recevierPhone; //对方号码 	
	private String tradeAddr; //通信地点 
	private BigDecimal basePay; //基本费用
	private BigDecimal longPay; //长途费用
	private BigDecimal infoPay; //信息费用
	private BigDecimal otherPay; //其他费用
	private BigDecimal allPay; //总费用
	
	private String phone;//手机号
	private int iscm;//是否当月 是的话保存1，不是的话保存0
	
	public DianXinDetail(){};
	public DianXinDetail(String phone) {
		super();
		this.phone = phone;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTradeType() {
		return tradeType;
	}
	/**通话类型 ：长途 本地
	 */
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public Date getcTime() {
		return cTime;
	}
	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}
	public int getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(int tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getCallWay() {
		return callWay;
	}
	public void setCallWay(String callWay) {
		this.callWay = callWay;
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
	public BigDecimal getBasePay() {
		return basePay;
	}
	public void setBasePay(BigDecimal basePay) {
		this.basePay = basePay;
	}
	public BigDecimal getLongPay() {
		return longPay;
	}
	public void setLongPay(BigDecimal longPay) {
		this.longPay = longPay;
	}
	public BigDecimal getInfoPay() {
		return infoPay;
	}
	public void setInfoPay(BigDecimal infoPay) {
		this.infoPay = infoPay;
	}
	public BigDecimal getOtherPay() {
		return otherPay;
	}
	public void setOtherPay(BigDecimal otherPay) {
		this.otherPay = otherPay;
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
	public int getIscm() {
		return iscm;
	}
	public void setIscm(int iscm) {
		this.iscm = iscm;
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		toStringBuilder.append("cTime", DateUtils.formatDate(cTime, "yyyy-MM-dd HH:mm:ss"));
		return toStringBuilder.toString();
	}
	
}
