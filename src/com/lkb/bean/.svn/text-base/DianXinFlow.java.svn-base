package com.lkb.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lkb.util.DateUtils;


/**
 * 创建时间：2014-10-8 上午9:59:07
 * 项目名称：LKB
 * @author Jerry Sun
 * @version 1.0
 * @since JDK 1.6
 * 文件名称：DianXinFlow.java
 * 类说明：电信流量账单
 */
public class DianXinFlow implements Serializable {
	/** serialVersionUID*/
	private static final long serialVersionUID = 4818117212238505705L;
	/** id*/
	private String id;
	/** phone 手机号*/
	private String phone;
	/** dependCycle 起止日期（计费周期）*/
	private String dependCycle;
	/** queryMonth 查询月份*/
	private Date queryMonth;
	/** allFlow 总流量(KB)*/
	private BigDecimal allFlow;
	/** allTime 总时长*/
	private BigDecimal allTime;
	/** allPay 总费用*/
	private BigDecimal allPay;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDependCycle() {
		return dependCycle;
	}
	public void setDependCycle(String dependCycle) {
		this.dependCycle = dependCycle;
	}

	public BigDecimal getAllFlow() {
		return allFlow;
	}
	public void setAllFlow(BigDecimal allFlow) {
		this.allFlow = allFlow;
	}
	public BigDecimal getAllTime() {
		return allTime;
	}
	public void setAllTime(BigDecimal allTime) {
		this.allTime = allTime;
	}
	public BigDecimal getAllPay() {
		return allPay;
	}
	public void setAllPay(BigDecimal allPay) {
		this.allPay = allPay;
	}
	
	public Date getQueryMonth() {
		return queryMonth;
	}
	public void setQueryMonth(Date queryMonth) {
		this.queryMonth = queryMonth;
	}
	@Override
	public String toString(){
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		toStringBuilder.append("queryMonth", DateUtils.formatDate(queryMonth, "yyyy-MM"));
		return toStringBuilder.toString();
	}
	
	
}
