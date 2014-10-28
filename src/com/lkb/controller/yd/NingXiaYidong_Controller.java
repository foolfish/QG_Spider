package com.lkb.controller.yd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.controller.base.PhoneCommonController;
import com.lkb.thirdUtil.yd.NingXiaYidong;


@Controller

public class NingXiaYidong_Controller extends PhoneCommonController{//Mobile_Controller{
	
	private static Logger logger = Logger.getLogger(NingXiaYidong_Controller.class);

	
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.debug("宁夏移动验证用户名!");
		return login(new NingXiaYidong(login,getCurrentUser(request)) );
	}
	
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new NingXiaYidong(login,getCurrentUser(request)));
	}
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new NingXiaYidong(login,getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new NingXiaYidong(login,getCurrentUser(request)));
	}

	
}
