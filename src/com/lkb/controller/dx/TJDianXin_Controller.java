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
import com.lkb.thirdUtil.dx.TJDianxin;

@Controller
@RequestMapping(value = "/dx/tj")
public class TJDianXin_Controller extends Mobile_Controller{
	private static Logger logger = Logger.getLogger(TJDianXin_Controller.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.debug("天津电信开始验证用户名!");
		return login(new TJDianxin(login,getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new TJDianxin(login,getCurrentUser(request)));
	}
	@RequestMapping(value = "/image")
	public @ResponseBody Map getAuthImg(HttpServletRequest request,Login login) {
		return getAuthImg(new TJDianxin(login,getCurrentUser(request)));
	}
		
//	@RequestMapping(value = "/startSpider", method = RequestMethod.POST)
//	public void startSpider(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
//		if(login.getAuthcode()!=null&&login.getLoginName()!=null){
//			TJDianxin gxd = new TJDianxin(login,getCurrentUser(request));
//			gxd.setDianXinDetailService(dianXinDetailService).setDianXinTelService(dianXinTelService).setUserService(userService);
//			startSpider(gxd);
//		}
//		
//	}
}
