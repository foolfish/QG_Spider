package com.lkb.controller.yd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.yd.JSYidong;

/**
 * 江苏移动
 * 
 */
@Controller
@RequestMapping(value = "/yd/js")
public class JSYIDONG_Controller extends Mobile_Controller{
	
	private static Logger logger = Logger.getLogger(JSYIDONG_Controller.class);
	
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new JSYidong(login,getCurrentUser(request)));
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.debug("浙江电信开始验证用户名!");
		return login(new JSYidong(login,getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new JSYidong(login,getCurrentUser(request)));
	}
	@RequestMapping(value = "/image")
	public @ResponseBody Map getAuthImg(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		return getAuthImg(new JSYidong(login,getCurrentUser(request)));
	}
		
	
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new JSYidong(login,getCurrentUser(request)));
	}
	
}
