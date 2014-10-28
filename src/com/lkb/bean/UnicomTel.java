package com.lkb.bean;

import java.math.BigDecimal;
import java.util.Date;

public class UnicomTel {
	private String id;
//	private String baseUserId;//baseUserId
	private Date cTime;//当前月份	
	private String cName;//客户姓名
	private String teleno;//手机号
	private String dependCycle;//计费周期
	
	private BigDecimal cAllPay; //总费用

	private BigDecimal jbyzf; // 基本月租费
	private BigDecimal bdthf; //本地通话费 
	private BigDecimal ctthf; //长途通话费 
	private BigDecimal mythf; //漫游通话费
	private BigDecimal dxtxf; //短信通信费 
	private BigDecimal zzywf; //增值业务费
	private BigDecimal dsf; //代收费(信息费)
	private BigDecimal tff; //特服费
	private BigDecimal qtf; //其他费用
	private int iscm ;//是的话保存1，不是的话保存0
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
	public BigDecimal getJbyzf() {
		return jbyzf;
	}
	public void setJbyzf(BigDecimal jbyzf) {
		this.jbyzf = jbyzf;
	}
	public BigDecimal getBdthf() {
		return bdthf;
	}
	public void setBdthf(BigDecimal bdthf) {
		this.bdthf = bdthf;
	}
	public BigDecimal getCtthf() {
		return ctthf;
	}
	public void setCtthf(BigDecimal ctthf) {
		this.ctthf = ctthf;
	}
	public BigDecimal getMythf() {
		return mythf;
	}
	public void setMythf(BigDecimal mythf) {
		this.mythf = mythf;
	}
	public BigDecimal getDxtxf() {
		return dxtxf;
	}
	public void setDxtxf(BigDecimal dxtxf) {
		this.dxtxf = dxtxf;
	}
	public BigDecimal getZzywf() {
		return zzywf;
	}
	public void setZzywf(BigDecimal zzywf) {
		this.zzywf = zzywf;
	}
	public BigDecimal getDsf() {
		return dsf;
	}
	public void setDsf(BigDecimal dsf) {
		this.dsf = dsf;
	}
	public BigDecimal getTff() {
		return tff;
	}
	public void setTff(BigDecimal tff) {
		this.tff = tff;
	}
	public BigDecimal getQtf() {
		return qtf;
	}
	public void setQtf(BigDecimal qtf) {
		this.qtf = qtf;
	}
	public int getIscm() {
		return iscm;
	}
	public void setIscm(int iscm) {
		this.iscm = iscm;
	}
	
	

}
