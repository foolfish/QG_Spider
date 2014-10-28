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
import com.lkb.constant.ErrorMsg;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.yd.BJYidong;
import com.lkb.thirdUtil.yd.GDYiDong;
import com.lkb.thirdUtil.yd.SDYidong;
import com.lkb.thirdUtil.yd.SHXYidong;


/**
 * 山西移动
 * @author BX.Liu
 *
 */

@Controller
@RequestMapping(value = "/yd/shx")
public class SHXYIDONG_Controller  extends Mobile_Controller{

	private static Logger logger = Logger.getLogger(SHXYIDONG_Controller.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.debug("山西移动开始验证用户名!");
		return login(new SHXYidong(login,getCurrentUser(request)) );
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new SHXYidong(login,getCurrentUser(request)));
	}
//	@RequestMapping(value = "/image")
//	public @ResponseBody Map<String,Object> getAuthImg(HttpServletRequest request,HttpServletResponse response,Login login) {
////		return getAuthImg(new BJYidong(login,getCurrentUser(request)));
//		return getAuthImg(new SHXYidong(login,getCurrentUser(request)),response);
//	}
	
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new SHXYidong(login,getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new SHXYidong(login,getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/getSecImgUrl")
	public @ResponseBody Map<String,Object> getSecImgUrl(HttpServletRequest request,Login login) {
		SHXYidong shx = new SHXYidong(login,getCurrentUser(request));
		setService(shx);
		Map<String,Object> map = shx.getSecImgUrl();
		map.put("errorcode",ErrorMsg.normal);
		shx.close();
		return map;
	}
	
}

