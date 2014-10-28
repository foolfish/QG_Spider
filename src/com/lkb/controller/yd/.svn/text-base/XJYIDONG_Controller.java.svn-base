package com.lkb.controller.yd;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.yd.XJYidong;
@Controller
public class XJYIDONG_Controller extends Mobile_Controller{
	
	/**
	 * 通话记录动态口令
	 */
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new XJYidong(login,getCurrentUser(request)));
	}
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		return login(new XJYidong(login,getCurrentUser(request)));
	}
	
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new XJYidong(login,getCurrentUser(request)));
	}
	/**
	 * 校验手机通话记录动态口令
	 */
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request, Login login) {
		return checkPhoneDynamicsCode(new XJYidong(login,getCurrentUser(request)));
	}
	
}
