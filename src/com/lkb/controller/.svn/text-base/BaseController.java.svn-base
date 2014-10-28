package com.lkb.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.lkb.bean.PhoneNum;
import com.lkb.bean.User;
import com.lkb.constant.ConstantProperty;
import com.lkb.service.ICellService;
import com.lkb.service.IPhoneNumService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.LoginUtil;
import com.lkb.thirdUtil.Part;
import com.lkb.thirdUtil.Phone_Base;
import com.lkb.util.CleanRead;
import com.lkb.util.InfoUtil;

@Controller
public class BaseController {
	@Resource
	private IUserService userService;
	@Resource
	private IPhoneNumService phoneNumService;
	@Resource
	private IWarningService warningService;	
	@Resource
	private ICellService ics;
	private static Logger logger = Logger.getLogger(BaseController.class);
	String imgPath2 = InfoUtil.getInstance().getInfo(ConstantProperty.road, "imgPath");

	@RequestMapping(value = "baseinfo")
	public String baseinfo(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("baseinfo=***");
		String uid = (String) req.getSession().getAttribute("currentUser");
		if (uid != null && uid.trim().length() > 0) {
			LoginUtil lutil = new LoginUtil();		
			lutil.base(uid, userService, model);
			return "baseinfo";
		} else {
			return "redirect:/lg/login.html";
		}
	}

	@RequestMapping(value = "error", method = RequestMethod.GET)
	public String error(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		return "error";
	}

	@RequestMapping(value = "saveBaseInfo", method = RequestMethod.POST)
	public String saveBaseInfo(HttpServletRequest req,
			HttpServletResponse resp, Model model) {

		String realname = req.getParameter("realname");
		logger.info(realname + "进入系统"); 
		String email = req.getParameter("email");
		String idcard = req.getParameter("idcard");
		String qq = req.getParameter("qq");
		String shebaolocation = req.getParameter("shebao");
		String hourse = req.getParameter("hourse");
		String cars = req.getParameter("cars");
		int ihourse = 6;
		int icars = 5;
		if(hourse!=null && hourse.trim().length()>0){
			ihourse = Integer.parseInt(hourse);
		}
		if(cars!=null && cars.trim().length()>0){
			icars = Integer.parseInt(cars);
		}
		
		
		String uid = (String) req.getSession().getAttribute("currentUser");
		if (uid != null) {
			User user = userService.findById(uid);
			Date modifyDate = new Date();
			if (user != null) {
				uid = user.getId();
				user.setRealName(realname);
				user.setEmail(email);
				user.setIdcard(idcard);
				user.setQq(qq);
				user.setModifyDate(modifyDate);
				user.setShebaolocation(shebaolocation);
				user.setHourse(ihourse);
				user.setCars(icars);
				userService.update(user);
			}

			LoginUtil lutil = new LoginUtil();
			lutil.entrance(uid, userService, phoneNumService, model);
			return "redirect:/entrance.html";
		} else {
			return "redirect:/lg/login.html";
		}
	}

	@RequestMapping(value = "entrance", method = RequestMethod.GET)
	public String entrance(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		String uid = (String) req.getSession().getAttribute("currentUser");
		if (uid != null && uid.trim().length() > 0) {
			LoginUtil lutil = new LoginUtil();
			lutil.entrance(uid, userService, phoneNumService, model);
			return "entrance";
		} else {
			return "redirect:/lg/login.html";
		}

	}

	@RequestMapping(value = "entranceAll", method = RequestMethod.POST)
	public String entranceAll(HttpServletRequest req, HttpServletResponse resp,
			Model model) {

		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}

		String[] domains = req.getParameterValues("domainNames");
		String[] domains2 = req.getParameterValues("domainNames2");
		int n1 = 0;
		int n2 = 0; 
	
		
	
		
		List list = new ArrayList();
		List list2 = new ArrayList();
		if(domains==null && domains2==null){
			return "redirect:/entranceSucessful.html";
		}
		if(domains!=null){
			 n1 = domains.length;
			list = Arrays.asList(domains);
		}
		
		if(domains2!=null){
			n2 =  domains2.length;
			list2 = Arrays.asList(domains2);
		}
		
		int all = n1+n2;
		req.getSession().setAttribute("all", all); //进度条
		req.getSession().setAttribute("alls", 7); //信息完成度
		
		
		if (list.contains("putong_jd")) {
			req.getSession().setAttribute("jdvalue", "");
		}
		if (list2.contains("jd-select")) {
			String putong_jd2 = req.getParameter("jd-select");
			req.getSession().setAttribute("jdvalue", putong_jd2);
		}
		
		if (list.contains("putong_taobao")) {
			req.getSession().setAttribute("taobaovalue", "");
		}
		if (list2.contains("taobao-select")) {
			String putong_taobao2 = req.getParameter("taobao-select");
			req.getSession().setAttribute("taobaovalue", putong_taobao2);
		}
		
		if (list.contains("phone")) {
			String phone = req.getParameter("phone");
			req.getSession().setAttribute("phone", phone);
		}
		if (list2.contains("phone2")) {
			String phone = req.getParameter("phone2");
			req.getSession().setAttribute("phone", phone);
		}
		
		if (list.contains("xuexin")) {
			req.getSession().setAttribute("xuexin", "");
		}
		if (list2.contains("xuexin2")) {
			String xuexin2 = req.getParameter("xuexin2");
			req.getSession().setAttribute("xuexin", xuexin2);
		}
		
		if (list.contains("email")) {
			String email = req.getParameter("email");
			req.getSession().setAttribute("email", email);
		}
		if (list2.contains("email2")) {
			String email = req.getParameter("email2");
			req.getSession().setAttribute("email", email);
		}
		
		if (list.contains("shebao")) {
			req.getSession().setAttribute("shebao", "");
		}
		if (list2.contains("shebao2")) {
			String shebao2 = req.getParameter("shebao2");
			req.getSession().setAttribute("shebao", shebao2);
		}
		
		if (list.contains("zhengxin")) {		
			req.getSession().setAttribute("zhengxin", "");
		}
		if (list2.contains("zhengxin2")) {
			String zhengxin2 = req.getParameter("zhengxin2");
			req.getSession().setAttribute("zhengxin", zhengxin2);
		}
		
		
		req.getSession().setAttribute("domains", list);
		req.getSession().setAttribute("domains2", list2);
		return "redirect:/entranceDianshang.html";

	}

	@RequestMapping(value = "/entranceDianshang", method = RequestMethod.GET)
	public String entranceDianshang(HttpServletRequest req,
			HttpServletResponse resp, Model model) {

		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}

		List list = (List) req.getSession().getAttribute("domains");
		List list2 = (List) req.getSession().getAttribute("domains2");
		if (list.contains("putong_jd") || list.contains("putong_taobao")||list2.contains("jd-select") || list2.contains("taobao-select")) {
			if (list.contains("putong_jd") || list2.contains("jd-select") ) {
				String jdvalue =  req.getSession().getAttribute("jdvalue").toString();
				model.addAttribute("putong_jd_url", "none");
				model.addAttribute("jdvalue", jdvalue);
			}
			//跳转淘宝
			if(list.contains("putong_taobao") || list2.contains("taobao-select")) {
				model.addAttribute("putong_taobao_url", "none");
				String taobaovalue =  req.getSession().getAttribute("taobaovalue").toString();
				model.addAttribute("taobaovalue", taobaovalue);
			}
			User user = new User();
			user.setParentId(currentUser);
			part( model, user);
			return "/entrance/entranceDianshang";
		} else  {
			return "redirect:/entrancePhone.html";
		}


	}
	
	
	/*
	 * 计算完整度
	 * */
	public void part(Model model,User user){
		int part = Part.wanzhengPart(user, userService);
		model.addAttribute("part", part);
	}
	
	@RequestMapping(value = "/getPart", method = RequestMethod.GET)
	public  @ResponseBody String getPart(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		User user  = userService.findById(currentUser);
		int part = Part.wanzhengPart(user, userService);
		return part+"";		
	}
	
	@RequestMapping(value = "/getCheckedPart", method = RequestMethod.GET)
	public  @ResponseBody Map<String, String > getCheckedPart(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		User user  = userService.findById(currentUser);
		Map<String, String > return_type = Part.getCheckedPart(user, userService);
		return return_type;		
	}
	

	@RequestMapping(value = "/entrancePhone", method = RequestMethod.GET)
	public String entrancePhone(HttpServletRequest req,
			HttpServletResponse resp, Model model) {

		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}
		List list = (List) req.getSession().getAttribute("domains");
		List list2 = (List) req.getSession().getAttribute("domains2");
		String phone = "";
		if (list.contains("phone")||list2.contains("phone2")) {
			phone = req.getSession().getAttribute("phone").toString();
			model.addAttribute("phone", phone);
			String phoneId = phone.substring(0, 7);
			PhoneNum phoneNum = phoneNumService.findById(phoneId);
			Phone_Base phonebase = new Phone_Base();
			if(phoneNum==null){
				phoneNum=phonebase.getPhoneBelong(phone,phoneNumService);
			}
			model.addAttribute("phoneNum",phoneNum );
			//修改后
			Boolean flag =  phonebase.goWhere(phoneNum,  currentUser,model);
			//控制跳转	
			 if(flag){
				return "redirect:/entranceXuexin.html";
			 }else{
				 User user= new User();
				 user.setParentId(currentUser);
				 part( model, user);
				 return "/entrance/entrancePhone";
			 }	
	
		} else  {
			return "redirect:/entranceXuexin.html";
		} 
	}
	
	@RequestMapping(value = "/entranceXuexin", method = RequestMethod.GET)
	public String entranceXuexin(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}

		List list = (List) req.getSession().getAttribute("domains");
		List list2 = (List) req.getSession().getAttribute("domains2");
		if (list.contains("xuexin") || list2.contains("xuexin2")) {
			
				String xuexin = req.getSession().getAttribute("xuexin").toString();
				model.addAttribute("xuexin", xuexin);
				

				model.addAttribute("xuexin_url", "none");
				User user = new User();
				user.setParentId(currentUser);
				part( model, user);

			return "/entrance/entranceXuexin";
		} else{
			return "redirect:/entranceEmail.html";
		}

	}

	

	@RequestMapping(value = "/entranceEmail", method = RequestMethod.GET)
	public String entranceEmail(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}

		List list = (List) req.getSession().getAttribute("domains");
		List list2 = (List) req.getSession().getAttribute("domains2");
		String email = "";
		if (list.contains("email") || list2.contains("email2") ) {
			email = req.getSession().getAttribute("email").toString();
			String[] emails = email.split("@");
			String mailafter = emails[1];
			if (mailafter.contains("163.com") || mailafter.contains("126.com")
					|| mailafter.contains("yeah.net")
					|| mailafter.contains("sohu.com")
					|| mailafter.contains("139.com")
					|| mailafter.contains("21cn.com")) {
				model.addAttribute("mailafter", mailafter);
				model.addAttribute("email", email);
				User user = new User();
				user.setParentId(currentUser);
				part( model, user);

				return "/entrance/entranceEmail";
			} else {
				return "redirect:/entranceZhengxin.html";
			}
		} else {
			return "redirect:/entranceZhengxin.html";
		}

	}

	@RequestMapping(value = "/entranceZhengxin", method = RequestMethod.GET)
	public String entranceZhengxin(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}

		List list = (List) req.getSession().getAttribute("domains");
		List list2 = (List) req.getSession().getAttribute("domains2");
	
		if (list.contains("zhengxin") || list2.contains("zhengxin2")) {
				
			String zhengxins = req.getSession().getAttribute("zhengxin").toString();
			model.addAttribute("zhengxin", zhengxins);
			User user = new User();
			user.setParentId(currentUser);
			part( model, user);
			model.addAttribute("zhengxin_url", "none");
			part( model, user);

			return "/entrance/entranceZhengxin";
		} else {
			return "redirect:/entranceShebao.html";
		}

	}

	
	
	@RequestMapping(value = "/entranceShebao", method = RequestMethod.GET)
	public String entranceShebao(HttpServletRequest req,
			HttpServletResponse resp, Model model) {

		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}

		List list = (List) req.getSession().getAttribute("domains");
		List list2 = (List) req.getSession().getAttribute("domains2");
		if (list.contains("shebao") || list2.contains("shebao2")) {
			String shebao = req.getSession().getAttribute("shebao").toString();

			User user = userService.findById(currentUser);
			String shebaolocation = user.getShebaolocation();
		
			model.addAttribute("shebao", shebao);
			
			if (shebaolocation.contains("shanghai_shebao")) {
				model.addAttribute("shanghai_shebao_url", "none");
			} else if(shebaolocation.contains("shenzhen_shebao")){
				model.addAttribute("shenzhen_shebao_url", "none");
//				ShenZhenShebao szsb = new ShenZhenShebao();
//				Map maps = szsb.getParams(closehttpclient, currentUser,
//						warningService);
//				String path = maps.get("img").toString();
//				Map szsbMap = (Map) maps.get("szsbMap");
//				req.getSession().setAttribute("szsbMap", szsbMap);
//				String url = imgPath2 + path;
			
			}
			User user1 = new User();
			user1.setId(currentUser);
			part( model, user1);

			return "/entrance/entranceShebao";
		}  else {
			return "redirect:/entranceSucessful.html";
		}

	}

	

	@RequestMapping(value = "/entranceSucessful", method = RequestMethod.GET)
	public String entranceSucessful(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}
		User user = userService.findById(currentUser);
		String markId = user.getMarkId();
		model.addAttribute("markId", markId);
		return "/entrance/entranceSucessful";
	}


	
	
	@RequestMapping(value = "excel", method = RequestMethod.GET)
	public String excel(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		try{
//		JxlRead JxlRead = new JxlRead();
//		JxlRead.write(phoneNumService);
			CleanRead cr = new CleanRead();
			//cr.readintodb( ics);
//			SpiderManager.getInstance().startSpider(cr, phoneNumService, ics, "d:/1.xls");
//			SpiderManager.getInstance().startSpider(cr, phoneNumService, ics, "d:/1/hlj.xls");
//			SpiderManager.getInstance().startSpider(cr, phoneNumService, ics, "d:/1/js.xls");
//			SpiderManager.getInstance().startSpider(cr, phoneNumService, ics, "d:/1/sh.xls");
//			cr.readintodbs( ics, phoneNumService,"d:/1/fj.xls");
//			cr.readintodbs( ics, phoneNumService,"d:/1/hlj.xls");
//			cr.readintodbs( ics, phoneNumService,"d:/1/js.xls");
//			cr.readintodbs( ics, phoneNumService,"d:/1/sh.xls");
//			SaveLoticaon ss = new SaveLoticaon();
//			ss.saveLocation(identifyLocationService);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	
	@RequestMapping(value = "/bc/saveBaseInfos")
	public  @ResponseBody Map<String, String>  saveBaseInfos(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		
		String realname="";
		try {
			realname = new String(req.getParameter("realname").getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String email = req.getParameter("email");
		String idcard = req.getParameter("idcard");
		Map<String, String> map2 = new HashMap<String, String>();

		
		
		String uid = (String) req.getSession().getAttribute("currentUser");
		if (uid != null) {
			User user = userService.findById(uid);
			Date modifyDate = new Date();
			if (user != null) {
				uid = user.getId();
				user.setRealName(realname);
				user.setEmail(email);
				user.setIdcard(idcard);
				user.setModifyDate(modifyDate);
				userService.update(user);
			}

			map2.put("flag", "1");//更新成功	
		} else {
			map2.put("flag", "0");//用户名不存在
		}
		return map2;
	}
	
	
}
