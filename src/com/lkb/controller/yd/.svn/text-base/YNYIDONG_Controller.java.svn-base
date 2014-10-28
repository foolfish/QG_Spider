package com.lkb.controller.yd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.yd.FJYingDong;
import com.lkb.thirdUtil.yd.YNYiDong;


@Controller
@RequestMapping(value = "/yd/yn")
public class YNYIDONG_Controller  extends Mobile_Controller{

	private static Logger logger = Logger.getLogger(YNYIDONG_Controller.class);

	
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new YNYiDong(login,getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new YNYiDong(login,getCurrentUser(request)));
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.info("云南移动开始验证用户名!");
		return login(new YNYiDong(login,getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new YNYiDong(login,getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/image")
	public @ResponseBody Map getAuthImg(HttpServletRequest request,Login login) {
		return getAuthImg(new YNYiDong(login,getCurrentUser(request)));
	}
	
	
	
	
}
