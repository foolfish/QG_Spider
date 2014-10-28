package com.lkb.controller.ds;


import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ErrorMsg;
import com.lkb.controller.base.DsCommonController;
import com.lkb.thirdUtil.TaoBao;




@Controller
@RequestMapping(value = "/ds/taobao")
public class TAOBAO_Controller extends DsCommonController{
	
	private static Logger logger = Logger.getLogger(TAOBAO_Controller.class);


	@RequestMapping(value = "/init")
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new TaoBao(login,getCurrentUser(request)));
	}
	@RequestMapping(value = "/verifyLogin")
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		return  login(new TaoBao(login,getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/sendMsg")
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		Map<String,Object> smap = new LinkedHashMap<String, Object>();
		int errorcode = Login.checkName(login);
		if(errorcode==ErrorMsg.normal){
			TaoBao tb = new TaoBao(login,getCurrentUser(request));
			Map<String,Object> map = tb.sendPhoneDynamicsCode();
			smap.put(CommonConstant.status, map.get(CommonConstant.status));
		}
		smap.put("errorcode", errorcode);
		
		return smap;
	}
	@RequestMapping(value = "/verify_sendMsg")
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request, Login login) {
		Map<String,Object> smap = new LinkedHashMap<String, Object>();
		int errorcode = Login.checkNamePhone(login);
		if(errorcode==ErrorMsg.normal){
			TaoBao tb = new TaoBao(login,getCurrentUser(request));
			Map<String,Object> map = tb.checkPhoneDynamicsCode();
			
			smap.put(CommonConstant.status, map.get(CommonConstant.status));
		}
		smap.put("errorcode", errorcode);
		
		return smap;
	
	}
	
}
