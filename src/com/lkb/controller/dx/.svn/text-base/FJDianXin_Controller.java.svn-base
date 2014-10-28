package com.lkb.controller.dx;

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
import com.lkb.thirdUtil.dx.FJDianxin;


@Controller
@RequestMapping(value = "/dx/fj")
public class FJDianXin_Controller extends Mobile_Controller{
	
	private static Logger logger = Logger.getLogger(FJDianXin_Controller.class);

	
//	@RequestMapping(value = "/startSpider", method = RequestMethod.POST)
//	public void startSpider(HttpServletRequest request,Login login) {
//		if(login.getAuthcode()!=null&&login.getLoginName()!=null){
//			startSpider(new FJDianxin(login,getCurrentUser(request)));
//		}
//		
//	}
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new FJDianxin(login,getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new FJDianxin(login, getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.info("福建电信开始验证用户名!");
		return login(new FJDianxin(login, getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new FJDianxin(login, getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/image")
	public @ResponseBody Map getAuthImg(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		return getAuthImg(new FJDianxin(login,getCurrentUser(request)));
	}

	
	
	
}
