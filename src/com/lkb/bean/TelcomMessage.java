package com.lkb.bean;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.lkb.util.DateUtils;

/**
 * @author think
 * @date 2014-9-1
 */
public class TelcomMessage implements Serializable {
	private static final long serialVersionUID = -5251722932190741077L;
	private String id;
	private String businessType; //业务类型：点对点,发送，接收
	private Date sentTime;//发送时间
	private String recevierPhone; //对方号码 	
	private Double allPay; //总费用
	private String phone;//手机号
	private Date createTs;//插入时间
	
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

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
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

	public Double getAllPay() {
		return allPay;
	}

	public void setAllPay(Double allPay) {
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
