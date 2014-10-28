package com.lkb.bean.client;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;

import com.lkb.constant.ErrorMsg;

/**
 * 所有登陆的通用类
 * @author fastw
 * @date	2014-7-14 下午3:53:47
 */
/**
 * @author fastw
 * @date	2014-8-11 下午8:23:23
 */
/**
 * @author fastw
 * @date	2014-8-11 下午8:23:26
 */
public class Login implements Serializable{
	/**
	 */
	private static final long serialVersionUID = -2902462869057211101L;
	private String loginName; //登陆账号
	private String password;// 登陆密码
	private String authcode;// 验证码
	private String phoneCode;//手机口令
	private String spassword;//服务密码/征信查询码
	private String currentUser;
	private String cityCode;	//地区编码
	private String productId;	//产品ID
	/**整型识别参数,登陆标识*/
	private Integer type;
	
	public Login(){}
	public Login(String loginName, String password) {
		super();
		this.loginName = loginName;
		this.password = password;
	}
	
	/**服务密码
	 * @return
	 */
	public String getSpassword() {
		return spassword;
	}
	/**服务密码*/
	public void setSpassword(String spassword) {
		this.spassword = spassword;
	}
	/**登陆账号*/
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginNameEncoder(){
		try {
			return URLEncoder.encode(loginName,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**登陆密码*/
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordEncoder(){
		try {
			return URLEncoder.encode(password,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**验证码*/
	public String getAuthcode() {
		return authcode;
	}
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}
	/**手机验证码**/
	public String getPhoneCode() {
		return phoneCode;
	}
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**检测login对象的用户名和密码是否是正确传值
	 */
	@Deprecated
	public static int checkName(Login login){
		if(login!=null){
			if(login.getLoginName()!=null&&!"".equals(login.getLoginName().trim())){}
			else{return ErrorMsg.loginNameEorror;}
		}else{
			return ErrorMsg.loginNameEorror;
		}
		return ErrorMsg.normal;
	}
	/**检测login对象的用户名和密码是否是正确传值
	 */
	@Deprecated
	public static boolean checkLogin(Login login){
		if(login!=null){
			if(login.getLoginName()!=null&&!"".equals(login.getLoginName().trim())&&login.getPassword()!=null&&!"".equals(login.getPassword().trim())){
				return true;
			}
		}
		return false;
	}
	/**检测login对象的用户名和密码是否是正确传值
	 */
	@Deprecated
	public static int checkNamePass(Login login){
		if(login!=null){
			if(login.getLoginName()!=null&&!"".equals(login.getLoginName().trim())){}
			else{return ErrorMsg.loginNameEorror;}
			if(login.getPassword()!=null&&!"".equals(login.getPassword().trim())){}
			else{return ErrorMsg.passwordEorror;}
		}else{
			return ErrorMsg.loginNameEorror;
		}
		return ErrorMsg.normal;
	}

	/**检测login对象的用户名和密码是否是正确传值
	 */
	public static int checkNameSpass(Login login){
		if(login!=null){
			if(login.getLoginName()!=null&&!"".equals(login.getLoginName().trim())){}
			else{return ErrorMsg.loginNameEorror;}
			if(login.getSpassword()!=null&&!"".equals(login.getSpassword().trim())){}
			else{return ErrorMsg.phonepassEorror;}
		}else{
			return ErrorMsg.loginNameEorror;
		}
		return ErrorMsg.normal;
	}
	
	/**检测login对象的用户名和密码是否是正确传值
	 */
	@Deprecated
	public static int checkNamePhone(Login login){
		if(login!=null){
			if(login.getLoginName()!=null&&!"".equals(login.getLoginName().trim())){}
			else{return ErrorMsg.loginNameEorror;}
			if(login.getPhoneCode()!=null&&!"".equals(login.getPhoneCode().trim())){}
			else{return ErrorMsg.dynpasswordEorror;}
		}else{
			return ErrorMsg.loginNameEorror;
		}
		return ErrorMsg.normal;
	}
	
	public String getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	public Integer getType() {
		if(type==null){
			type = 0;
		}
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	

}
