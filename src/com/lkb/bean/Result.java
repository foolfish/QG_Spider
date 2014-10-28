/**
 * 
 */
package com.lkb.bean;

/**
 * @author think
 * @date 2014-8-15
 */
public class Result {
	private int status; //状态码
	private String imgUrl = ""; //图片链接
	private boolean result; //是否完成一个流程，用于判断是否还有下一步，否表示还有下一步
	private boolean success; //响应是否成功，如不成功就不会判断status了
	private String errorMsg = ""; //错误信息
	private String text = "";
	private String title = "";
	private String body = "";
	private String value = "";
	private String forgetPassUrl = "";//忘记密码连接
	private String passName = "";//服务密码还是网站密码这种名字
	private int errorcode;
	
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		if(imgUrl==null || "none".equals(imgUrl)){
			this.imgUrl = "";
		}else{
			this.imgUrl = imgUrl;
		}
		
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errMsg) {
		if(errMsg==null || "ok".equals(errMsg)){
			this.errorMsg = "";
		}else{
			this.errorMsg = errMsg;
		}
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getForgetPassUrl() {
		return forgetPassUrl;
	}
	public void setForgetPassUrl(String forgetPassUrl) {
		this.forgetPassUrl = forgetPassUrl;
	}
	public String getPassName() {
		return passName;
	}
	public void setPassName(String passName) {
		if(passName!=null){
			this.passName = passName;
		}else{
			this.passName = "服务密码";
		}
		
	}
	
}
