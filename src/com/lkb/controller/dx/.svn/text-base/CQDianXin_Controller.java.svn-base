package com.lkb.controller.dx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.dx.CQDianXin;


@Controller
@RequestMapping(value = "/dx/cq")
public class CQDianXin_Controller extends Mobile_Controller {
	private static Logger logger = Logger.getLogger(CQDianXin_Controller.class);


	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.info("重庆电信开始验证用户名!");
		return login(new CQDianXin(login,getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new CQDianXin(login,getCurrentUser(request)));
	}
	
//	@RequestMapping(value = "/image")
//	public @ResponseBody Map getAuthImg(HttpServletRequest request,Login login) {
//		return getAuthImg(new CQDianXin(login,getCurrentUser(request)));
//	}

	
	
	
}
