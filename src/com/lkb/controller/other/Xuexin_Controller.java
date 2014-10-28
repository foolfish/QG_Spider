package com.lkb.controller.other;


import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.BaseInfo_Controller;
import com.lkb.service.IUserService;
import com.lkb.thirdUtil.XueXin;



@Controller
@RequestMapping(value = "/xuexin")
public class Xuexin_Controller extends BaseInfo_Controller{
	@Resource
	private IUserService userService;
	private static Logger logger = Logger.getLogger(Xuexin_Controller.class);

	
	@RequestMapping(value = "/verifyLogin")//, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putong_jd_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		return login(new XueXin(login,getCurrentUser(request)));
	}
	
	
	@RequestMapping(value = "/init")//, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongXxFirst(HttpServletRequest request,HttpServletResponse response) {
		String putong_xx_userName = request.getParameter("loginName");
		return putongFirst(new XueXin(new Login(putong_xx_userName,null),getCurrentUser(request)));
	}
	@RequestMapping(value = "/isLogin")
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request) {
		String putong_jd_userName = request.getParameter("loginName");
		return isLogin(new XueXin(new Login(putong_jd_userName,null),getCurrentUser(request)));
	}
//	@RequestMapping(value = "/putongXxImage")
//	public @ResponseBody Map<String,Object> getAuthImg(HttpServletRequest request,HttpServletResponse response,Login login) {
//		return getAuthImg(new BJYidong(login,getCurrentUser(request)),response);
//	}
	
	
}
