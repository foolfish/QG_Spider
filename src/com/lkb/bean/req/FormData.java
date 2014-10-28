/**
 * 
 */
package com.lkb.bean.req;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

/**
 * @author think
 * @date 2014-8-15
 */
public class FormData {
	private String userId;//
	private String userName;//当前用户的名称（暂时未用）
	private String phoneNo;//授权账号用户名，如手机号 或者 京东账号
	private String password;
	private String authCode;
	private String smsCode;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Model model;
	
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	//private String 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	public static FormData build(HttpServletRequest request,HttpServletResponse response, Model model) {
		FormData fd = new FormData();
		fd.setModel(model);
		fd.setRequest(request);
		fd.setResponse(response);
		
		String userId = (String) request.getSession().getAttribute("currentUser");
		if (userId == null) {
			userId = request.getParameter("userId");
		}
		fd.setUserId(userId);
		String authCode = request.getParameter("authCode");
		if (authCode == null) {
			authCode = request.getParameter("authcode");
		}
		fd.setAuthCode(authCode);
		
		String fwpassword = request.getParameter("fwpassword");
		if (fwpassword == null) {
			fwpassword = request.getParameter("password");
		}
		fd.setPassword(fwpassword);
		
		String phone = request.getParameter("phone");
		if (phone == null) {
			phone = request.getParameter("userName");
		}
		if (phone == null) {
			phone = request.getParameter("loginName");
		}
		fd.setPhoneNo(phone);
		
		fd.setSmsCode(request.getParameter("smsCode"));
		return fd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
