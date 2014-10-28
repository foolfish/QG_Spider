package com.lkb.controller.yd;


import java.util.HashMap;
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
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantNum;
import com.lkb.controller.Mobile_Controller;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.dx.BJDianxin;
import com.lkb.thirdUtil.yd.HLJYidong;
import com.lkb.thirdUtil.yd.HUBYidong;
import com.lkb.util.thread.loginParse.Control;


/**
 * 黑龙江移动
 * @author jzr
 *
 */
@Controller
public class HLJYIDONG_Controller extends Mobile_Controller{
	@RequestMapping(value = "/fuwu_hlj_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putong_vertifyLogin(HttpServletRequest request,Login login) {
		return login(new HLJYidong(login,getCurrentUser(request)));
	}


	@RequestMapping(value = "/putong_hlj_yidong_GetAuth", method = RequestMethod.POST)
	public @ResponseBody  Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new HLJYidong(login,getCurrentUser(request)));
	}
	
	/**
	* <p>Title: sendMessage</p>
	* <p>Description: 发送手机验证码</p>
	* @author Jerry Sun
	* @param request
	* @param login
	* @return
	*/
	@RequestMapping(value = "/heilongjiang_YD_getSms", method = RequestMethod.POST)
	public @ResponseBody  Map<String, Object> sendMessage(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new HLJYidong(login,getCurrentUser(request)));
	}
	
	/**
	* <p>Title: checkDynamicsCode</p>
	* <p>Description: 验证码校验</p>
	* @author Jerry Sun
	* @param request
	* @param login
	* @return
	*/
	@RequestMapping(value = "/heilongjiang_YD_checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request, Login login) {
		return checkPhoneDynamicsCode(new HLJYidong(login,getCurrentUser(request)));
	}
	
	
	
	
}
