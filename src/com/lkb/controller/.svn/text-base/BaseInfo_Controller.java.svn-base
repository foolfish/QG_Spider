package com.lkb.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lkb.bean.client.Login;
import com.lkb.bean.req.FormData;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ErrorMsg;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IParseService;
import com.lkb.service.ISheBaoService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfo;

/** 模拟登陆的超类
 * @author fastw
 * @date	2014-8-10 上午11:31:02
 */
public abstract class BaseInfo_Controller {
	@Resource
	protected IUserService userService;
	@Resource
	protected IWarningService warningService;
	@Resource
	protected IParseService parseService;
	
	
	/**并且初始化 父类变量中的request ,和session
	 * @return session中currentUser
	 */
	public String getCurrentUser(HttpServletRequest request){
		String currentUser =  request.getParameter("userId");
		if(currentUser!=null&&!currentUser.trim().equals("")){
			return currentUser;
		}else{
			return request.getSession().getAttribute("currentUser").toString();
		}
	}
	
	/**
	 * 验证手机动态口令
	 * @return
	 */
	public Map<String, Object> checkPhoneDynamicsCode(BaseInfo baseinfo) {
		this.setServices(baseinfo);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		 int code = Login.checkNamePhone(baseinfo.getLogin());
		 if(code==ErrorMsg.normal){
			if(baseinfo.islogin()){
				map = baseinfo.checkPhoneDynamicsCode();
				baseinfo.close();
			}else{
				map.put(CommonConstant.status, 0);
				map.put(CommonConstant.errorMsg,"对不起您当前授权的账号已过期,请重新授权!");
			}
		 }	
		 map.put("errorcode", code);
		return map;
	}
	
	/**
	 * 发送手机动态口令
	 * @return
	 */
	public Map<String, Object> sendPhoneDynamicsCode(BaseInfo baseinfo) {		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		int code = Login.checkName(baseinfo.getLogin());
		if(code==ErrorMsg.normal){
			baseinfo.setWarningService(warningService);
			if(baseinfo.islogin()){
				map = baseinfo.sendPhoneDynamicsCode();
				baseinfo.close();
			}else{
				map.put(CommonConstant.status, 0);
				map.put(CommonConstant.errorMsg,"对不起您未登陆,请登陆后再试!");
			}
		}
		map.put("errorcode", code);
		return map;
	}
	/**
	 * @param baseinfo
	 * @param login 此处login可为null;
	 * @return
	 */
	public Map<String, Object> login(BaseInfo baseinfo,Login login) {		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		int code = Login.checkNamePass(baseinfo.getLogin());
		if(code==ErrorMsg.normal){
			this.setServices(baseinfo);
			if(baseinfo.islogin()){
				map.put(CommonConstant.status, 1);
				map.put("url","");
				map.put(CommonConstant.errorMsg, "ok");   //msg ok;
			}else{
				map = baseinfo.logins();
				baseinfo.close();
			}
		}
		map.put("errorcode", code);
		return map;
	}
	public Map<String, Object> login(BaseInfo baseinfo) {
		return login(baseinfo, null);
	}
	public  Map<String, Object> putongFirst(BaseInfo baseinfo) {
		 Map<String,Object> map = new LinkedHashMap<String,Object>();
		 int code = Login.checkName(baseinfo.getLogin());
		
		 if(code==ErrorMsg.normal){
			 this.setServices(baseinfo);		 
			 if(baseinfo.islogin()){
				 map.put("url","");
				 map.put("status", 1);
			}else{
				 map = baseinfo.index();
				 baseinfo.close();
			 }
		 }
		 map.put("errorcode", code);
		return map;
	}
	public  Map<String,Object> getAuthImg(BaseInfo baseinfo) {
		baseinfo.setWarningService(warningService);
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		String url = baseinfo.getAuthcode();
		map.put("url", url);
		baseinfo.close();
		return map;
	}
	public Map<String,Object> isLogin(BaseInfo baseinfo) {
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		 int code = Login.checkName(baseinfo.getLogin());
		 map.put("errorcode", code); 
		 if(code==ErrorMsg.normal){
			if(baseinfo.islogin()){
				map.put(CommonConstant.status,1);//登陆成功
			}else{
				map.put(CommonConstant.status,0);//未登陆
			}
		 }
		//baseinfo.close();
		return map;
	}
	public  Map<String,Object> getAuthImg(BaseInfo baseinfo,HttpServletResponse response) {
		setServices(baseinfo);
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		setResponse(response,baseinfo.getAuthcode1());
		baseinfo.close();
		return map;
	}
	/**
	 * 设置响应头
	 * @param b
	 * @param response
	 */
	private byte[] setResponse(HttpServletResponse response,byte[] b){
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		ServletOutputStream sopt = null;
		if(b!=null){
			try {
				sopt = response.getOutputStream();
				sopt.write(b);
				sopt.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(sopt!=null){
					try {
						sopt.close();   
					} catch (IOException e) {
						e.printStackTrace();
					}  
				}
			}
		}else{
			response.setContentType("image/jpeg");
		}
		return b;
	}
	
	/**
	 * 设置所有的
	 * @param baseinfo
	 */
	public void setServices(BaseInfo baseinfo){
		this.basicService(baseinfo);
		this.setService(baseinfo);
	}
	public void setService(BaseInfo baseinfo){}
	public void basicService(BaseInfo baseinfo){
		baseinfo.setUserService(userService).setParseService(parseService).setWarningService(warningService);
	}
	public Login getLogin(FormData fd){
		Login login = new Login();
		login.setLoginName(fd.getUserName()!=null?fd.getUserName():fd.getPhoneNo());
		login.setPassword(fd.getPassword());
		login.setPhoneCode(fd.getSmsCode());
		login.setAuthcode(fd.getAuthCode());
		login.setCurrentUser(fd.getUserId());
		//login.setSpassword(fd.getSmsCode());//后边随后添加
		return login;
	}
}
