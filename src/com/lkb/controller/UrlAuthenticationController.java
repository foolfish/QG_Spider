package com.lkb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.proxy.util.ProxyAuthentication;
import com.lkb.thirdUtil.PhoneNumUtil;

@Controller
@RequestMapping(value = "/urlAuth")
public class UrlAuthenticationController {

	@RequestMapping(value = "/auth")
	public @ResponseBody
	Map<String, Object> auth(
			@RequestParam(value = "ip", required = true) String ip,
			@RequestParam(value = "port", required = true) String port) {
		Map<String, Object> mp = new HashMap<String, Object>();
		ProxyAuthentication pa = new ProxyAuthentication();
		boolean isOk = pa.authenticate(ip, port);
		mp.put("是否可用", isOk);
		//if (isOk == true)
			mp.put("响应时间", pa.getResponseTime());
		return mp;
	}

}
