package com.lkb.controller.dx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.dx.JSDianxin;


@Controller
@RequestMapping(value = "/dx/js")
public class JSDianXin_Controller extends Mobile_Controller{
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new JSDianxin(login,warningService, getCurrentUser(request)));
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		return login(new JSDianxin(login,warningService, getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new JSDianxin(login,warningService, getCurrentUser(request)));
	}
//	@RequestMapping(value = "/image")
//	public @ResponseBody Map getAuthImg(HttpServletRequest request,Login login) {
//		return getAuthImg(new JSDianxin(login,warningService, getCurrentUser(request)));
//	}
		
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new JSDianxin(login,warningService, getCurrentUser(request)));
	}
	
}
