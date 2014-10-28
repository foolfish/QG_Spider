package com.lkb.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lkb.util.DateUtils;
/**
 * 手机上网账单
 * 
 * @author yanlongma
 * @date 2014-10-8
 */
public class MobileOnlineBill implements Serializable{

	private static final long serialVersionUID = -7138461902156152656L;

	private String id;
	private String phone;
	//月份
	private Date monthly;
	//数据总流量
	private Long totalFlow;
	//免费数据流量
	private Long freeFlow;
	//收费数据流量
	private Long chargeFlow;
	//通信费
	private BigDecimal trafficCharges;
	//是否当月
	private int iscm;
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
	public Long getTotalFlow() {
		return totalFlow;
	}
	public void setTotalFlow(Long totalFlow) {
		this.totalFlow = totalFlow;
	}
	public Long getFreeFlow() {
		return freeFlow;
	}
	public void setFreeFlow(Long freeFlow) {
		this.freeFlow = freeFlow;
	}
	public Long getChargeFlow() {
		return chargeFlow;
	}
	public void setChargeFlow(Long chargeFlow) {
		this.chargeFlow = chargeFlow;
	}
	public BigDecimal getTrafficCharges() {
		return trafficCharges;
	}
	public void setTrafficCharges(BigDecimal trafficCharges) {
		this.trafficCharges = trafficCharges;
	}
	public Date getMonthly() {
		return monthly;
	}
	public void setMonthly(Date monthly) {
		this.monthly = monthly;
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
		toStringBuilder.append("monthly", DateUtils.formatDate(monthly, "yyyy-MM-dd HH:mm:ss"));
		return toStringBuilder.toString();
	}
}
