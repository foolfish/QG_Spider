package com.lkb.controller.temp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.User;
import com.lkb.bean.YqMa;
import com.lkb.service.IUserService;
import com.lkb.service.IYqMaService;
import com.lkb.util.YqRandom;
@Controller
public class YqMaController {
	
	@Resource
	private IUserService userService;
	@Resource
	private IYqMaService yqMaService;


	/*
	 * 生成随机值
	 * */
	@RequestMapping(value = "makeMa")
	public String makeMa(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		YqRandom yqRandom = new YqRandom();
		Set set = yqRandom.getSet(10000, 6);
		Iterator it = set.iterator();
		while(it.hasNext()){
			String random = it.next().toString();
			YqMa yqMa = new YqMa();
			yqMa.setRandom(random);
			yqMaService.save(yqMa);
		}
		return "";
	}
	
	/*
	 * 验证邀请码是否可用
	 * */
	@RequestMapping(value = "/lg/checkMa")
	public @ResponseBody Map<String, String> checkMa(HttpServletRequest req,HttpServletResponse resp, Model model) {
		Map<String, String> map = new HashMap<String, String>();
		String yqma = req.getParameter("yqma");
		YqMa yqMa = yqMaService.get(yqma);
		String result = "0";
		if(yqMa==null){
			result = "1";
		}else if(yqMa!=null && yqMa.getUserId()!=null){
			result = "2";
		}
		map.put("result", result);
		return map;
	}
	
}
