package com.lkb.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.User;
import com.lkb.bean.YqMa;
import com.lkb.constant.Constant;
import com.lkb.service.IPhoneNumService;
import com.lkb.service.IUserService;
import com.lkb.service.IYqMaService;
import com.lkb.thirdUtil.LoginUtil;
import com.lkb.util.*;
@Controller
public class LoginRegisterController {
	@Resource
	private IUserService userService;
	
	@Resource
	private IPhoneNumService phoneNumService;
	@Resource
	private IPhoneNumService iPhoneNumService;
	
	private static Logger logger = Logger.getLogger(LoginRegisterController.class);
	@Resource
	private IYqMaService yqMaService;

	
	/*
	 * 退出
	 * */
	@RequestMapping(value = "/lg/loginout", method = RequestMethod.GET)
	public String loginout(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("loginout=***");
		String uid = (String) req.getSession().getAttribute("currentUser");
		logger.info("uid="+uid);
		if(uid!=null){
			req.getSession().removeAttribute("currentUser");
			return "/login/login";
		}else{
			return "/login/login";
		}
		
	}
	
	@RequestMapping(value = "/lg/login", method = RequestMethod.GET)
	public String login(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("login=***");
		String uid = (String) req.getSession().getAttribute("currentUser");
		logger.info("uid="+uid);
		if(uid!=null){
//			LoginUtil lutil = new LoginUtil();
//			lutil.entrance(uid, userService,phoneNumService, model);
			return "redirect:/lg/arc_all.html";
		}else{
			return "/login/login";
		}
		
	}
	
	@RequestMapping(value = "/lg/register", method = RequestMethod.GET)
	public String register(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("register=***");
		return "/login/register";
	}
	@RequestMapping(value = "/lg/resetpwdsuc", method = RequestMethod.GET)
	public String resetpwdsuc(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("resetpwdsuc=***");
		String userId = (String) req.getSession().getAttribute("currentUser");
		if (userId == null) {
			return "redirect:/lg/login.html";
		}
		return "/login/resetpwdsuc";
	}
	@RequestMapping(value = "/lg/resetpwd", method = RequestMethod.GET)
	public String resetpwd(HttpServletRequest req, HttpServletResponse resp,Model model) {
		logger.info("resetpwd=***");
		String userId = (String) req.getSession().getAttribute("currentUser");
		if (userId == null) {
			return "redirect:/lg/login.html";
		}
		return "/login/resetpwd";
	}
	@RequestMapping(value = "/lg/backpwd_msgcheck", method = RequestMethod.GET)
	public String backpwd_msgcheck(HttpServletRequest req, HttpServletResponse resp,Model model) {
		logger.info("backpwd_msgcheck=***");
		return "/login/backpwd_msgcheck";
	}
	@RequestMapping(value = "/lg/backpwd_newpwd", method = RequestMethod.GET)
	public String backpwd_newpwd(HttpServletRequest req, HttpServletResponse resp,Model model) {
		logger.info("backpwd_newpwd=***");
		return "/login/backpwd_newpwd";
	}
	@RequestMapping(value = "/lg/backpwd_suc", method = RequestMethod.GET)
	public String backpwd_suc(HttpServletRequest req, HttpServletResponse resp,Model model) {
		logger.info("backpwd_suc=***");
		return "/login/backpwd_suc";
	}
	@RequestMapping(value = "/lg/backpwd", method = RequestMethod.GET)
	public String backpwd(HttpServletRequest req, HttpServletResponse resp,Model model) {
		logger.info("backpwd=***");
		return "/login/backpwd";
	}
	@RequestMapping(value = "/lg/arc_all", method = RequestMethod.GET)
	public String arc_all(HttpServletRequest req, HttpServletResponse resp,Model model) {
		logger.info("arc_all=***");
		String userId = (String) req.getSession().getAttribute("currentUser");
		if (userId == null) {
			return "redirect:/lg/login.html";
		}
		if (userId != null) {
			User user = userService.findById(userId);		
			model.addAttribute("user", user);
			
		}
		return "/login/arc_all";
	}
	@RequestMapping(value = "/lg/putLoginNameSession", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> putLoginNameSession(HttpServletRequest req, HttpServletResponse resp,Model model) {
		logger.info("putLoginNameSession=***");
		String loginName = req.getParameter("loginName");
		String flag= "0";
		if (loginName != null &&!loginName.equals("")) {
			flag = "1";
			req.getSession().setAttribute("loginName", loginName);
		}
		
		Map<String, String> map2 = new HashMap<String, String>(2);
		map2.put("loginName", loginName);
		map2.put("usersource", "base");
		List<User> users = userService.getUserByUserNamesource(map2);
		if(users!=null && users.size()>0){
			User user = users.get(0);
			String phone = user.getPhone();
			req.getSession().setAttribute("resetPhone", phone);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("flag", flag);
		return map;
	}
	@RequestMapping(value = "/lg/sendSms")
	public @ResponseBody Map<String, String> sendSms(HttpServletRequest req, HttpServletResponse resp,Model model) {
		Map<String, String> map = new HashMap<String, String>();
		logger.info("sendSms=***");
		String flag = "0";
		String loginName = req.getParameter("loginName");
		if (loginName==null) {
			loginName = (String) req.getSession().getAttribute("resetPhone");
			
		}
		if (loginName != null) {
			if(loginName!=null && loginName.length()>0){
				UserLoginSendSms userLoginSendSms = new UserLoginSendSms(iPhoneNumService);
				String smsNumber = userLoginSendSms.sendSms(loginName);
				//String smsNumber = "111111";
				if (!smsNumber.equals("send_message_fail")) {
					String key = loginName+"msm";
					req.getSession().setAttribute(key, smsNumber);
					flag = "1";
				}
			}
		}
		map.put("sendSms_flag", flag);
		return map;
	}
	/**
	 * 
	 * 
	 * */
	@RequestMapping(value = "/lg/loginForm", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> loginForm(HttpServletRequest req, HttpServletResponse resp,
				Model model)  {
		logger.info("loginForm=***");
		String flag = "0";
		String cu = (String) req.getSession().getAttribute("currentUser");
		Map<String, String> map2 = new HashMap<String, String>();
		if (cu != null) {
			flag = "1";
		} else {
			String userName = req.getParameter("userName");
			String password = req.getParameter("password");
			password = MD.MD5(password.toLowerCase()+ConstantUtil.PWD_KEY);
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("loginName", userName);
			map.put("loginPassword", password);
			List<User> users = userService.checkLogin(map);
			if(users!=null && users.size()>0){
				flag = "1";
				User user = users.get(0);
//				String islinux = InfoUtil.getInstance().getInfo("road","islinux");
//				if(islinux.equals("true")){
//					Date nowDate = new Date();
//					Date registerDate = user.getRegisterDate();
//					long diff = nowDate.getTime() - registerDate.getTime();
//					if(diff-1000 * 60 * 60 * 24>0){
//						flag = "3";
//					}
//				}
				req.getSession().setAttribute("currentUser", user.getId());
				req.getSession().setAttribute("loginName", user.getLoginName());			
				/*String realName = user.getRealName();
				if(realName!=null && realName.trim().length()>0){
					flag = "2";
				}*/
			}	
		}
		map2.put("flag", flag);
		return map2;
	}
	@RequestMapping(value = "/lg/checkPassword", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> checkPassword(HttpServletRequest req, HttpServletResponse resp,
			Model model)  {
		logger.info("loginForm=***");
		String flag = "0";
		String userName = (String) req.getSession().getAttribute("loginName");
		Map<String, String> map2 = new HashMap<String, String>();
		if (userName != null) {
			String password = req.getParameter("password");
			password = MD.MD5(password.toLowerCase()+ConstantUtil.PWD_KEY);
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("loginName", userName);
			map.put("loginPassword", password);
			List<User> users = userService.checkLogin(map);
			if(users!=null && users.size()>0){
				flag = "1";
			}	
		}
		map2.put("flag", flag);
		return map2;
	}
	
	@RequestMapping(value = "/lg/registerForm", method = RequestMethod.POST)
	public  String registerForm(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("registerForm=***");
		String userName = (String)req.getParameter("username");
		if(userName!=null){
			//String telephoneNum = req.getParameter("telephoneNum");
			String password = (String)req.getParameter("password");
			password = MD.MD5(password.toLowerCase()+ConstantUtil.PWD_KEY);
			//String authcode = req.getParameter("checkNum").toLowerCase();
			//String code = (String)req.getSession().getAttribute("rand");
			String yqma = (String)req.getParameter("code");
			/*if(code !=null && code.length()>0){
				code = code.toLowerCase();
			}*/
			//if(authcode.equals(code)){
				List<User> users = userService.checkLoginName(userName);
				String uid = "";
				//防止刷新
				if(users!=null && users.size()>0){
					uid = users.get(0).getId();
				}else{
					User user = new User();
					Date modifyDate = new Date();
					user.setPhone(userName);
					UUID uuid = UUID.randomUUID();
					uid = uuid.toString();
					user.setId(uid);
					user.setLoginName(userName);
					user.setLoginPassword(password);
					user.setRegisterDate(modifyDate);
					user.setModifyDate(modifyDate);
					user.setUsersource(Constant.BASE);
					user.setUsersource2(Constant.BASE);
					java.text.DateFormat format2 = new java.text.SimpleDateFormat(
							"yyyyMMdd");
					String date2 = format2.format(modifyDate);
					String markId = date2 + System.nanoTime();
					user.setMarkId(markId);
				
					userService.saveUser(user);
					//System.out.println(user.getUsersource2());
					
					YqMa yqMa = yqMaService.get(yqma);
					if(yqMa!=null){
						yqMa.setUserId(uid);
						yqMaService.update(yqMa);
					}
					req.getSession().setAttribute("loginName", user.getLoginName());
					//删除session中的短信验证码
					String key = user.getLoginName()+"msm";
					req.getSession().removeAttribute(key);
				}
				
				if(req.getSession().getAttribute("currentUser")!=null){
					req.getSession().removeAttribute("currentUser");
				}
				req.getSession().setAttribute("currentUser", uid);
				
			//}
				return "redirect:/lg/arc_all.html";
		}else{
			return "/lg/login";
		}
	
	}
	
	/*
	 * 检查登录名是否已经使用，已经使用返回1；未使用返回0
	 * */
	@RequestMapping(value = "/lg/checkLoginName", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> checkLoginName(HttpServletRequest req,HttpServletResponse resp, Model model) {
		Map<String, String> map = new HashMap<String, String>();
		logger.info("checkLoginName=***");
		String userName = req.getParameter("userName");
		List<User> users = userService.checkLoginName(userName);
		String flag = "0";
		if(users!=null && users.size()>0){
			 flag = "1";
		}
		map.put("loginflag", flag);
		return map;
	}
	
	/*
	 * 检查验证码是否正确，正确返回1；不正确返回0
	 * */
	@RequestMapping(value = "/lg/checkAuthCode")
	public @ResponseBody Map<String, String> checkAuthCode(HttpServletRequest req,HttpServletResponse resp, Model model) {
		logger.info("checkAuthCode=***");
		Map<String, String> map = new HashMap<String, String>();
		String authcode = req.getParameter("authcode").toLowerCase();
		String code = (String)req.getSession().getAttribute("rand");
		logger.info("authcode="+authcode);
		logger.info("sessioncode="+code);		
		if(code !=null && code.length()>0){
			code = code.toLowerCase();
		}
		String flag = "0";
		if(authcode.equals(code)){
			 flag = "1";
		}
		map.put("authflag", flag);
		return map;
	}	
	
	@RequestMapping(value = "/lg/checkSms")
	public @ResponseBody Map<String, String> checkSms(HttpServletRequest req, HttpServletResponse resp,Model model) {
		logger.info("checkSms=***");
		Map<String, String> map = new HashMap<String, String>();
		String flag = "0";
		
		String loginName = req.getParameter("loginName");
		if (loginName==null) {
			loginName = (String)req.getSession().getAttribute("loginName");
		}
		
		if (loginName != null) {
			String msgNumber = req.getParameter("msgNumber");
			//logger.info("msmNumber="+msmNumber);
			if(loginName!=null && loginName.length()>0){
				String key = loginName+"msm";
				String redisSmsNumber = (String)req.getSession().getAttribute(key);
				if (redisSmsNumber==null) {
					flag = "3";
				}
				if(msgNumber.equals(redisSmsNumber)){
					flag = "1";
				}else{
					flag = "2";
				}
			}
		}
		map.put("checkSms_flag", flag);
		return map;
	}

	@RequestMapping(value = "test")
	public String test(ModelMap map){
		
		return "test";
	}

	@RequestMapping(value = "/lg/resetpwdForm", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> resetpwdForm(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("resetpwdForm=***");
		String flag = "0";
		String cu = (String) req.getSession().getAttribute("currentUser");
		Map<String, String> map2 = new HashMap<String, String>();
		if (cu != null) {
			String userName = (String)req.getSession().getAttribute("loginName");
			String password = req.getParameter("password");
			String new_password = req.getParameter("new_password");
			password = MD.MD5(password.toLowerCase()+ConstantUtil.PWD_KEY);
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("loginName", userName);
			map.put("loginPassword", password);
			List<User> users = userService.checkLogin(map);
			if(users!=null && users.size()>0){
				User user = users.get(0);
				String new_md5_password = MD.MD5(new_password.toLowerCase()+ConstantUtil.PWD_KEY);
				user.setLoginPassword(new_md5_password);
				userService.update(user);
				//req.getSession().removeAttribute("currentUser");
				flag = "1";
			}	
		}
		map2.put("flag", flag);
		return map2;
	}

	@RequestMapping(value = "/lg/backpwdForm", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> backpwdForm(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("resetpwdForm=***");
		String flag = "0";
		String loginName = req.getParameter("loginName");
		if (loginName==null) {
			loginName = (String)req.getSession().getAttribute("loginName");
		}
		Map<String, String> map2 = new HashMap<String, String>();
		if (loginName != null) {
			String new_password = req.getParameter("new_password");
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("loginName", loginName);
			map.put("usersource", "base");
			List<User> users = userService.getUserByUserNamesource(map);
			if(users!=null && users.size()>0){
				User user = users.get(0);
				String new_md5_password = MD.MD5(new_password.toLowerCase()+ConstantUtil.PWD_KEY);
				user.setLoginPassword(new_md5_password);
				userService.update(user);
				req.getSession().removeAttribute("loginName");
				flag = "1";
			}	
		}
		map2.put("flag", flag);
		return map2;
	}
}
