package com.lkb.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lkb.util.DateUtils;

/**
 * 手机上网详单
 * 
 * @author yanlongma
 * @date 2014-10-8
 */
public class MobileOnlineList implements Serializable {

	private static final long serialVersionUID = -6169869990888875961L;

	private String id;
	private String phone;
	// 起始时间
	private Date cTime;
	// 通信地点
	private String tradeAddr;
	// 上网方式(CMNET/CMWAP)
	private String onlineType;
	// 时长
	private Long onlineTime;
	// 总流量
	private Long totalFlow;
	// 套餐优惠
	private String cheapService;
	// 通信费
	private BigDecimal communicationFees;

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		toStringBuilder.append("cTime", DateUtils.formatDate(cTime, "yyyy-MM-dd HH:mm:ss"));
		return toStringBuilder.toString();
	}
	
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

	public String getOnlineType() {
		return onlineType;
	}

	public void setOnlineType(String onlineType) {
		this.onlineType = onlineType;
	}

	public Long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Long getTotalFlow() {
		return totalFlow;
	}

	public void setTotalFlow(Long totalFlow) {
		this.totalFlow = totalFlow;
	}

	public String getCheapService() {
		return cheapService;
	}

	public void setCheapService(String cheapService) {
		this.cheapService = cheapService;
	}

	public BigDecimal getCommunicationFees() {
		return communicationFees;
	}

	public void setCommunicationFees(BigDecimal communicationFees) {
		this.communicationFees = communicationFees;
	}

}
