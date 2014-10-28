package com.lkb.controller.ds;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.base.DsCommonController;
import com.lkb.thirdUtil.JD;



@Controller
@RequestMapping(value = "/jd")
public class JD_Controller extends DsCommonController{// DS_Controller{
	
	@RequestMapping(value = "/verifyLogin")
	public @ResponseBody Map<String, Object> putong_jd_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		return login(new JD(login,getCurrentUser(request)));
	}
	@RequestMapping(value = "/init")
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,HttpServletResponse response) {
		String putong_jd_userName = request.getParameter("loginName");
		return putongFirst(new JD(new Login(putong_jd_userName,null),getCurrentUser(request)));
	}
	@RequestMapping(value = "/isLogin")
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request) {
		String putong_jd_userName = request.getParameter("loginName");
		return isLogin(new JD(new Login(putong_jd_userName,null),getCurrentUser(request)));
	}
		
	
}
