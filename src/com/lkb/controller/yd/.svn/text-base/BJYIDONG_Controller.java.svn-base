package com.lkb.controller.yd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.controller.base.PhoneCommonController;
import com.lkb.thirdUtil.yd.BJYidong;


@Controller
@RequestMapping(value = "/yd/bj")
public class BJYIDONG_Controller extends PhoneCommonController{//Mobile_Controller{
	
	private static Logger logger = Logger.getLogger(BJYIDONG_Controller.class);

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.debug("北京移动开始验证用户名!");
		return login(new BJYidong(login,getCurrentUser(request)) );
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new BJYidong(login,getCurrentUser(request)));
	}
	
	
}
