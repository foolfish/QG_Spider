package com.lkb.controller;

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

import com.lkb.service.IPhoneNumService;
import com.lkb.thirdUtil.PhoneNumUtil;

@Controller

public class PhoneNumController {

	@Resource
	private IPhoneNumService phoneNumService;
	private static Logger logger = Logger.getLogger(PhoneNumController.class);
	
	@RequestMapping(value="phoneNumBegin",method=RequestMethod.GET)
	public @ResponseBody Map<String, String> phoneNumBegin(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		Map map = new HashMap();
		PhoneNumUtil pUtil = new PhoneNumUtil();
		pUtil.saveLog(phoneNumService);
		return map;
	}
	
	
	
}
