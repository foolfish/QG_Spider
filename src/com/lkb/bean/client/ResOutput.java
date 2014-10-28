package com.lkb.bean.client;

import java.util.LinkedHashMap;

/**
 * 输出类
 */
public class ResOutput extends LinkedHashMap<String, Object>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8985996533721387025L;
	/**错误值*/
	private static final String errorcode_key = "errorcode";
	/**验证码地址*/
	private static final  String url_key = "url";
	/**1正常,0 错误**/
	private static final  String status_key = "status";
	/**错误信息*/
	private static final  String errorMsg_key = "errorMsg";
	public ResOutput(){
		setErrorcode(0);
		setStatus(0);
		setUrl("");
	}
	

	public int getErrorcode() {
		return (Integer)get(errorcode_key);
	}
	public void setErrorcode(int errorcode) {
		put(errorcode_key, errorcode);
	}
	/**验证码地址
	 * @return
	 */
	public String getUrl() {
		String url = (String)get(url_key);
		if(url==null){
			url = "";
		}
		return url;
	}
	public void setUrl(String url) {
		if(url==null){
			url = "";
		}
		put(url_key, url);
	}
	public int getStatus() {
		return (Integer)get(status_key);
	}
	/**1正常,0 错误**/
	public void setStatus(int status) {
		put(status_key, status);
	}
	/**如果为空返回""字符串
	 * @return
	 */
	public String getErrorMsg() {
		String errorMsg = (String)get(errorMsg_key);
		if(errorMsg==null||"none".equals(errorMsg))
			errorMsg = "";
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		put(errorMsg_key, errorMsg);
	}
	

}
