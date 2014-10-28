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
import com.lkb.controller.base.PhoneCommonController;
import com.lkb.thirdUtil.yd.TJYiDong;


@Controller
@RequestMapping(value = "/yd/tj")
public class TJYIDONG_Controller  extends PhoneCommonController{

	private static Logger logger = Logger.getLogger(TJYIDONG_Controller.class);

	
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		return checkPhoneDynamicsCode(new TJYiDong(login, getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		return sendPhoneDynamicsCode(new TJYiDong(login, getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.info("天津移动开始验证用户名!");
		return login(new TJYiDong(login, getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new TJYiDong(login, getCurrentUser(request)));
	}
	
//	@RequestMapping(value = "/image")
//	public @ResponseBody Map getAuthImg(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
//		return getAuthImg(new TJYiDong(login, getCurrentUser(request)));
//	}
	
	
}
