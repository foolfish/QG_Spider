package com.lkb.controller.dx;

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
import com.lkb.controller.Mobile_Controller;
import com.lkb.service.IParseService;
import com.lkb.thirdUtil.dx.SDDianXin;


@Controller
@RequestMapping(value = "/dx/sd")
public class SDDianXin_Controller extends Mobile_Controller{
	private static Logger logger = Logger.getLogger(SDDianXin_Controller.class);

	
	@Resource
	private IParseService parseService;
	
	
//	@RequestMapping(value = "/startSpider", method = RequestMethod.POST)
//	public void startSpider(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
//		if(login.getAuthcode()!=null&&login.getLoginName()!=null){
//			SDDianXin gxd = new SDDianXin(login, getCurrentUser(request));
//			gxd.setDianXinDetailService(dianXinDetailService).setDianXinTelService(dianXinTelService).setUserService(userService).setParseService(parseService);
//			startSpider(gxd);
//		}
//		
//	}
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new SDDianXin(login, getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new SDDianXin(login, getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.info("山东电信开始验证用户名!");
		return login(new SDDianXin(login, getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new SDDianXin(login, getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/image")
	public @ResponseBody Map getAuthImg(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		return getAuthImg(new SDDianXin(login, getCurrentUser(request)));
	}
	
	
	
	
	
}
