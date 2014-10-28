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
 * 文件名称：DianXinFlowDetail.java
 * 类说明：电信流量详单
 */
public class DianXinFlowDetail implements Serializable {
	/** serialVersionUID*/
	private static final long serialVersionUID = 4196068035758501327L;
	/** id*/
	private String id;
	/** phone 手机号*/
	private String phone;
	/** iscm 是否当月 是的话保存1，不是的话保存0*/
	private int iscm;
	/** beginTime 开始时间*/
	private Date beginTime;
	/** tradeTime 时长*/
	private long tradeTime;
	/** flow 流量*/
	private BigDecimal flow;
	/** netType 网络类型*/
	private String netType;
	/** location 上网地市*/
	private String location;
	/** business 使用业务*/
	private String business;
	/** fee 费用（元）*/
	private BigDecimal fee;
	
	public DianXinFlowDetail() {
		// TODO Auto-generated constructor stub
	}
	
	public DianXinFlowDetail(String phone) {
		super();
		this.phone = phone;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public long getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(long tradeTime) {
		this.tradeTime = tradeTime;
	}
	public BigDecimal getFlow() {
		return flow;
	}
	public void setFlow(BigDecimal flow) {
		this.flow = flow;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
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
		toStringBuilder.append("beginTime", DateUtils.formatDate(beginTime, "yyyy-MM-dd HH:mm:ss"));
		return toStringBuilder.toString();
	}
	
}
