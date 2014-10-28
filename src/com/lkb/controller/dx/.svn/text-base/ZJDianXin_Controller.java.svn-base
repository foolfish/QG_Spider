package com.lkb.controller.dx;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ErrorMsg;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.dx.ZJDianxin;


@Controller
@RequestMapping(value = "/dx/zj")
public class ZJDianXin_Controller extends Mobile_Controller{
	private static Logger logger = Logger.getLogger(ZJDianXin_Controller.class);
	
	
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new ZJDianxin(login, getCurrentUser(request)));
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.debug("浙江电信开始验证用户名!");
		if(login.getAuthcode()!=null&&!login.getAuthcode().equals("")){ }
		else{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("errorcode",ErrorMsg.authcodetEorror);
			map.put(CommonConstant.status,0);
			return map;
		}
		return login(new ZJDianxin(login, getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new ZJDianxin(login, getCurrentUser(request)));
	}
//	@RequestMapping(value = "/image")
//	public @ResponseBody Map getAuthImg(HttpServletRequest request,Login login) {
//		return getAuthImg(new ZJDianxin(login, getCurrentUser(request)));
//	}
		
//	@RequestMapping(value = "/startSpider", method = RequestMethod.POST)
//	public void startSpider(HttpServletRequest request,Login login) {
//		if(login.getAuthcode()!=null&&login.getLoginName()!=null){
//			ZJDianxin gxd = new ZJDianxin(login, getCurrentUser(request));
//			gxd.setDianXinDetailService(dianXinDetailService).setDianXinTelService(dianXinTelService).setUserService(userService);
//			startSpider(gxd);
//		}
//		
//	}
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new ZJDianxin(login, getCurrentUser(request)));
	}
	
	
	
}
