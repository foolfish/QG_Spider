package com.lkb.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lkb.util.DateUtils;

public class DianXinTel implements Serializable{
	private static final long serialVersionUID = 7253512103685062097L;
	private String id;
//	private String baseUserId;//baseUserId
	private String brand;//品牌
	private Date cTime;//当前月份	
	private String cName;//客户姓名
	private String teleno;//手机号
	private String dependCycle;//计费周期
	private BigDecimal cAllPay; //本项小计
	private BigDecimal ztcjbf; //主套餐基本费	
	private BigDecimal bdthf; //本地通话费
	

	private BigDecimal ldxsf; //来电显示费
	private BigDecimal mythf; //漫游通话费
	
	
	public DianXinTel(){};
	public DianXinTel(String phone) {
		super();
		this.teleno = phone;
	}
	
	public BigDecimal getLdxsf() {
		return ldxsf;
	}
	public void setLdxsf(BigDecimal ldxsf) {
		this.ldxsf = ldxsf;
	}
	public BigDecimal getMythf() {
		return mythf;
	}
	public void setMythf(BigDecimal mythf) {
		this.mythf = mythf;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
//	public String getBaseUserId() {
//		return baseUserId;
//	}
//	public void setBaseUserId(String baseUserId) {
//		this.baseUserId = baseUserId;
//	}
	public Date getcTime() {
		return cTime;
	}
	/**当前月份*/
	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getTeleno() {
		return teleno;
	}
	public void setTeleno(String teleno) {
		this.teleno = teleno;
	}
	public String getDependCycle() {
		return dependCycle;
	}
	public void setDependCycle(String dependCycle) {
		this.dependCycle = dependCycle;
	}
	public BigDecimal getcAllPay() {
		return cAllPay;
	}
	public void setcAllPay(BigDecimal cAllPay) {
		this.cAllPay = cAllPay;
	}
	public BigDecimal getZtcjbf() {
		return ztcjbf;
	}
	public void setZtcjbf(BigDecimal ztcjbf) {
		this.ztcjbf = ztcjbf;
	}
	public BigDecimal getBdthf() {
		return bdthf;
	}
	public void setBdthf(BigDecimal bdthf) {
		this.bdthf = bdthf;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		toStringBuilder.append("Date", DateUtils.formatDate(cTime, "yyyy-MM"));
		return toStringBuilder.toString();
	}
	
}
