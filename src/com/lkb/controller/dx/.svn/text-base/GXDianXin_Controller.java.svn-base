package com.lkb.controller.dx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.dx.GXDianxin;


@Controller
@RequestMapping(value = "/dx/gx")
public class GXDianXin_Controller extends Mobile_Controller{
	/**
	 * 校验手机通话记录动态口令
	 */
//	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new GXDianxin(login, getCurrentUser(request)));
	}
	/**
	 * 通话记录动态口令
	 */
//	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new GXDianxin(login, getCurrentUser(request)));
	}
//	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		return login(new GXDianxin(login,getCurrentUser(request)) , login);
	}
	
//	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new GXDianxin(login, getCurrentUser(request)));
	}
		
	
}
