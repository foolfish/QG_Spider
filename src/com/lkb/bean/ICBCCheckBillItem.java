package com.lkb.bean;

import java.math.BigDecimal;
import java.util.Date;

public class ICBCCheckBillItem {
	private String id;
	private String checkBillId;           //对账单ID
	private String cardNo;               //卡号后四位
	private Date tradeDate;              //交易日期
	private Date tradeKeepDate;          //记账日
	private String tradeType;           //交易类型
	private String tradePlace;         //交易商户或城市
	private BigDecimal tradeAmount;   //交易金额
	private String tradeCurrency;  //交易币种
	private String tradeMode;             //交易方式 ：转入或转出
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCheckBillId() {
		return checkBillId;
	}
	public void setCheckBillId(String checkBillId) {
		this.checkBillId = checkBillId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public Date getTradeKeepDate() {
		return tradeKeepDate;
	}
	public void setTradeKeepDate(Date tradeKeepDate) {
		this.tradeKeepDate = tradeKeepDate;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getTradePlace() {
		return tradePlace;
	}
	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
	}
	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public String getTradeCurrency() {
		return tradeCurrency;
	}
	public void setTradeCurrency(String tradeCurrency) {
		this.tradeCurrency = tradeCurrency;
	}
	public String getTradeMode() {
		return tradeMode;
	}
	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}
	
	
	
}
