package com.lkb.bean.client;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 社保登陆类
 * @author fastw
 * @date	2014-9-4 下午4:53:53
 */
public class SocialInsuranceOut  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3069010868193827396L;
	/**错误值*/
	private int errorcode;
	/**验证码地址*/
	private String url;
	/**1正常,0 错误**/
	private int status;
	/**错误信息*/
	private String errorMsg;
	/**返回必填字段*/
	private Set<String> inputParams;
	/**返回社保对应的官方网站**/
//	private String officialWebsite;
	/**注册地址*/
	private String registerUrl;
	

	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public String getUrl() {
		if(url==null||"none".equals(url)){
			url = "";
		}
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatus() {
		return status;
	}
	/**1正常,0 错误**/
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg==null?"":errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public Set<String> getInputParams() {
		if(inputParams==null){
			inputParams = new LinkedHashSet<String>();
		}
		return inputParams;
	}
	public void setInputParams(Set<String> inputParams) {
		this.inputParams = inputParams;
	}
	public void addParams(String param) {
		getInputParams().add(param);
	}
	/**官方网站
	 * @return
	 */
//	public String getOfficialWebsite() {
//		return officialWebsite;
//	}
//	/**官方网站*/
//	public void setOfficialWebsite(String officialWebsite) {
//		this.officialWebsite = officialWebsite;
//	}
	/**注册地址*/
	public String getRegisterUrl() {
		return registerUrl;
	}
	/**注册地址*/
	public void setRegisterUrl(String registerUrl) {
		this.registerUrl = registerUrl;
	}
	
	
	
//	public String toJson(){
//		StringBuffer sb = new StringBuffer();
//		sb.append("{\"status\":").append(getStatus()).append(",");
//		sb.append("\"errorMsg\":").append(getErrorMsg()).append(",");
//		sb.append("\"url\":").append(getUrl()).append(",");
//		sb.append("\"inputParams\":").append(getInputParams()).append(",");
//		sb.append("\"errorCode\":").append(getErrorCode()).append("}");
//		return sb.toString();
//	}

}
