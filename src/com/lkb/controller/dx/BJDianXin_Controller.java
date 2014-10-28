package com.lkb.controller.dx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.base.PhoneCommonController;
import com.lkb.thirdUtil.dx.BJDianxin;


@Controller
@RequestMapping(value = "/dx/bj")
public class BJDianXin_Controller extends PhoneCommonController{//Mobile_Controller{
	/**
	 * 通话记录动态口令
	 */
//	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new BJDianxin(login,getCurrentUser(request)));
	}
	public @ResponseBody Map<String,Object> login(HttpServletRequest request,Login login) {
		return login(new BJDianxin(login,getCurrentUser(request)));
	}
	
	public @ResponseBody Map<String,Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new BJDianxin(login,getCurrentUser(request)));
	}
	/**
	 * 校验手机通话记录动态口令
	 */
	public @ResponseBody Map<String,Object> checkDynamicsCode(HttpServletRequest request, Login login) {
		return checkPhoneDynamicsCode(new BJDianxin(login,getCurrentUser(request)));
	}
	
}
