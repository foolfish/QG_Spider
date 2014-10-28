package com.lkb.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lkb.util.DateUtils;

/**
 * @author liangtao
 * @date 2014-9-9
 */
public class MobileMessage implements Serializable {
	private static final long serialVersionUID = -5251722932190741077L;
	private String id;
	
	private Date sentTime;//发送时间
	private String tradeWay; //通信方式，"发送、接收"
	private String sentAddr; //通信地点
	private String recevierPhone; //对方号码 	
	private BigDecimal allPay; //总费用
	private String phone;//手机号
	private Date createTs;//插入时间
	
	
	
	public String getTradeWay() {
		return tradeWay;
	}

	public void setTradeWay(String tradeWay) {
		this.tradeWay = tradeWay;
	}

	public String getId() {
		return id;
	}

	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getSentAddr() {
		return sentAddr;
	}

	public void setSentAddr(String sentAddr) {
		this.sentAddr = sentAddr;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
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
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		toStringBuilder.append("sentTime", DateUtils.formatDate(sentTime, "yyyy-MM-dd HH:mm:ss"));
		return toStringBuilder.toString();
	}
}
