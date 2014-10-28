package com.lkb.controller.yd;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantNum;
import com.lkb.controller.Mobile_Controller;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.yd.BJYidong;
import com.lkb.thirdUtil.yd.FJYingDong;
import com.lkb.thirdUtil.yd.JLYidong;
import com.lkb.thirdUtil.yd.JSYidong;
import com.lkb.util.InfoUtil;

@Controller
@RequestMapping(value = "/yd/jl")
public class JLYIDONG_Controller extends Mobile_Controller {

	private static Logger logger = Logger.getLogger(JLYIDONG_Controller.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> login(HttpServletRequest request, Login login) {
		logger.debug("吉林移动开始验证用户名!");
		return login(new JLYidong(login, getCurrentUser(request)));
	}

	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> putongFirst(HttpServletRequest request, Login login) {
		return putongFirst(new JLYidong(login, getCurrentUser(request)));
	}

	@RequestMapping(value = "/image")
	public @ResponseBody
	Map<String, Object> getAuthImg(HttpServletRequest request,
			HttpServletResponse response, Login login) {
		// return getAuthImg(new BJYidong(login,getCurrentUser(request)));
		return getAuthImg(new JLYidong(login, getCurrentUser(request)),
				response);
	}

	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkDynamicsCode(HttpServletRequest request,
			Login login) {
		return checkPhoneDynamicsCode(new JLYidong(login,
				getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,
			Login login) {
		return sendPhoneDynamicsCode(new JLYidong(login,
				getCurrentUser(request)));
	}

}
