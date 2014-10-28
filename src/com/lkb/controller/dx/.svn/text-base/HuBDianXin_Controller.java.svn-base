package com.lkb.controller.dx;

import java.util.HashMap;
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
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantNum;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.dx.HuBDianxin;
import com.lkb.thirdUtil.yd.BJYidong;
import com.lkb.util.thread.loginParse.Control;

@Controller
public class HuBDianXin_Controller extends Mobile_Controller{
	
	private static Logger logger = Logger.getLogger(HuBDianXin_Controller.class);
	
	@RequestMapping(value = "/putong_hub_dianxin_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putong_vertifyLogin(HttpServletRequest request,Login login) {
			logger.debug("湖北电信开始验证用户名!");
			return login(new HuBDianxin(login,getCurrentUser(request)) );
	}
	
	@RequestMapping(value="/putong_hub_dianxin_GetAuth")
	public  @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new HuBDianxin(login,getCurrentUser(request)));
	}
	
}
