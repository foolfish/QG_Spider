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
import com.lkb.thirdUtil.yd.HEBYidong;

/**
 */
@Controller
@RequestMapping(value = "/yd/heb")
public class HEBYIDONG_Controller extends Mobile_Controller{
	
	private static Logger logger = Logger.getLogger(HEBYIDONG_Controller.class);
	
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new HEBYidong(login,getCurrentUser(request)));
	}
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSmsUser", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSmsUser(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		HEBYidong hn = new HEBYidong(login,getCurrentUser(request));
		Map<String,Object> map = hn.sendPhoneUserInfo();
		hn.close();
		return map;
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.debug("河北移动开始验证用户名!");
		return login(new HEBYidong(login,getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,HttpServletResponse resp,Login login) {
		return putongFirst(new HEBYidong(login,getCurrentUser(request)));
	}
//	@RequestMapping(value = "/image")
//	public @ResponseBody Map<String,Object> getAuthImg(HttpServletRequest request,HttpServletResponse resp, Login login) {
//		return getAuthImg(new HEBYidong(login,getCurrentUser(request)),resp);
//	}
		
	@RequestMapping(value = "/getSecImgUrl")
	public @ResponseBody Map<String,Object> getSecImgUrl(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		HEBYidong hn = new HEBYidong(login,getCurrentUser(request));
		setService(hn);
		Map<String,Object> map = hn.getSecImgUrl();
	
		hn.close();
		return map;
	}
	@RequestMapping(value = "/checkSecDynamics")
	public @ResponseBody Map<String,Object> checkSecDynamics(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		HEBYidong hn = new HEBYidong(login,getCurrentUser(request));
		setService(hn);
		Map<String,Object> map = hn.checkPhoneUserInfo();
		hn.close();
		return map;
	}
	
	
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new HEBYidong(login,getCurrentUser(request)));
	}
	
}
