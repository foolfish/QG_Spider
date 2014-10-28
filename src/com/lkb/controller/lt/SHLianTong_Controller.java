package com.lkb.controller.lt;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomFlowBillService;
import com.lkb.service.IUnicomFlowService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.lt.SHLiantong;

@Controller
public class SHLianTong_Controller extends Mobile_Controller{
	@Resource
	private IUserService userService;
	@Resource
	private IUnicomTelService unicomTelService;
	@Resource
	private IUnicomDetailService unicomDetailService;
	@Resource
	private IUnicomFlowService unicomFlowService;
	@Resource
	private IUnicomFlowBillService unicomFlowBillService;
	@Resource
	private IWarningService warningService;
	private static Logger logger = Logger.getLogger(SHLianTong_Controller.class);
	
	@RequestMapping(value = "/sh_checkNeedAuthCode")//, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new SHLiantong(login,getCurrentUser(request)));
	}
	@RequestMapping(value = "/fuwu_sh_liantong_vertifyLogin")//, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		return login(new SHLiantong(login,getCurrentUser(request)));
	}
//	@RequestMapping(value = "/sh_checkNeedAuthCode", method = RequestMethod.POST)
//	public @ResponseBody
//	Map<String, String> sh_checkNeedAuthCode(HttpServletRequest request,
//			HttpServletResponse resp, Model model,Login login) {
//		String currentUser = request.getSession().getAttribute("currentUser").toString();
//		String ptype = request.getParameter("ptype");
//		Map<String, String> map = new HashMap<String, String>();
//		String userName = login.getLoginName();
//		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
//		if (userName != null && userName.trim().length() > 0) {
//			Boolean flag = lt.checkNeedAuthcode(ptype);
//			if (!flag) {
//				map.put("flag", "true");
//				map.put("url", "none");
//			} else {
//				map.put("flag", "false");
//				String url = lt.getAuthcodeImg();
//				map.put("url", url);
//			}
//		}
//		lt.close();
//		return map;
//	}
	
//	public Map<String, String> getAuth(boolean init, FormData fd) {
//		final String currentUser = fd.getUserId();
//		String authCode = fd.getAuthCode();
//		String fwpassword = fd.getPassword();
//		String phone = fd.getPhoneNo();
//		
//		String ptype = fd.getRequest().getParameter("ptype");
//		Map<String, String> map = new HashMap<String, String>();
//		String userName = fd.getPhoneNo();
//		Login login = new Login();
//		login.setAuthcode(authCode);
//		login.setPassword(fwpassword);
//		login.setLoginName(phone);
//		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
//		if (userName != null && userName.trim().length() > 0) {
//			Boolean flag = lt.checkNeedAuthcode(ptype);
//			if (!flag) {
//				map.put("flag", "1");
//				map.put("url", "none");
//			} else {
//				map.put("flag", "0");
//				String url = lt.getAuthcodeImg();
//				map.put("url", url);
//			}
//		}
//		lt.close();
//		return map;
//	}
//	

//	@RequestMapping(value = "/sh_checkAuthCodelian", method = RequestMethod.POST)
//	public @ResponseBody
//	Map<String, String> sh_checkAuthCodelian(HttpServletRequest request,
//			HttpServletResponse resp, Model model,Login login) {
//		String currentUser = request.getSession().getAttribute("currentUser")
//				.toString();
//		String verifyCode = login.getAuthcode();
//		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
//		Map<String, String> map = new HashMap<String, String>();
//		if (verifyCode != null && verifyCode.trim().length() > 0) {
//			Boolean flag = lt.checkAuthcode();
//			if (!flag) {
//				map.put("flag", "true");
//				map.put("url", "none");
//			} else {
//				map.put("flag", "false");
//				String path = lt.getAuthcodeImg();
//				String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
//				String url = imgPath2 + path;
//				map.put("url", url);
//			}
//		}
//		lt.close();
//		return map;
//	}

//	@RequestMapping(value = "/sh_sendCardLian", method = RequestMethod.POST)
//	public @ResponseBody
//	String sh_sendCardLian(HttpServletRequest request, HttpServletResponse resp,
//			Model model,Login login) {
//		String currentUser = request.getSession().getAttribute("currentUser")
//				.toString();
//		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
//		String json = lt.sendCard();
//		lt.close();
//		return json;
//	}

//	@RequestMapping(value = "/fuwu_sh_liantong_vertifyLogin", method = RequestMethod.POST)
//	public @ResponseBody
//	Map<String, Object> fuwu_sh_liantong_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
//		String currentUser = request.getSession().getAttribute("currentUser")
//				.toString();
//		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
//		Map<String,Object> mp = new HashMap<String, Object>();
//		if(Login.checkLogin(login)){
//			Boolean needAuth = false;
//			if (login.getAuthcode() != null && login.getAuthcode().length() > 0) {
//				needAuth = true;
//			}
//			Map<String,Object> bmap = lt.login(needAuth);
//			Boolean state = (Boolean)bmap.get("flag");
//			Boolean isNeedAuth = (Boolean)bmap.get("isNeedAuth");
//			String msg = bmap.get("msg").toString();
//			String url = "";
//			if (state){
//				Control con = new Control(login, userService,unicomTelService, unicomDetailService, warningService,  currentUser);
//				con.setType(101);
//				Thread t = new Thread(con);
//				t.setName(login.getLoginName()+"联通_采集中");
//				t.start();
//				logger.info(login.getLoginName()+"联通验证成功,后台正在采集...");
//			}
//			if (isNeedAuth) {
//				url = lt.getAuthcodeImg();
//			}else {
//				url = "none";
//			}
//			mp.put("state", state);
//			mp.put("msg", msg);
//			mp.put("url", url);
//		}
//		lt.close();
//		return mp;
//	}
//	
//	public Map<String, Object> putong_vertifyLogin(boolean init, FormData fd) {
//		final String currentUser = fd.getUserId();
//		Login login = getLogin(fd);
//		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
//		Map<String,Object> mp = new HashMap<String, Object>();
//		if(Login.checkLogin(login)){
//			Boolean needAuth = false;
//			if (login.getAuthcode() != null && login.getAuthcode().length() > 0) {
//				needAuth = true;
//			}
//			Map<String,Object> bmap = lt.login(needAuth);
//			Boolean state = (Boolean)bmap.get("flag");
//			Boolean isNeedAuth = (Boolean)bmap.get("isNeedAuth");
//			String msg = bmap.get("msg").toString();
//			String url = "";
//			if (state){
//				Control con = new Control(login, userService,unicomTelService, unicomDetailService, warningService,  currentUser);
//				con.setType(101);
//				Thread t = new Thread(con);
//				t.setName(login.getLoginName()+"联通_采集中");
//				t.start();
//				logger.info(login.getLoginName()+"联通验证成功,后台正在采集...");
//			}
//			if (isNeedAuth) {
//				url = lt.getAuthcodeImg();
//			}else {
//				url = "none";
//			}
//			mp.put("state", state);
//			mp.put("msg", msg);
//			mp.put("url", url);
//		}
//		lt.close();
//		return mp;
//	}
//	
//	@RequestMapping(value = "/dongtai_sh_liantong_vertifyLogin", method = RequestMethod.POST)
//	public @ResponseBody
//	Map<String, String> dongtai_sh_liantong_vertifyLogin(
//			HttpServletRequest request, HttpServletResponse resp, Model model,Login login) {
//		String currentUser = request.getSession().getAttribute("currentUser")
//				.toString();
//		Map<String, String> map = new HashMap<String, String>();
//
//		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
//		Boolean needAuth = false;
//		String authcode = login.getAuthcode();
//		if (authcode != null && authcode.length() > 0) {
//			needAuth = true;
//		}
//		Map<String, Object> map2 = lt.login(needAuth);
//		Boolean isLogin = (Boolean)map2.get("isLogin");
//		Boolean isNeedAuth = (Boolean)map2.get("isNeedAuth");
//		if (isNeedAuth == true) {
//			String url = lt.getAuthcodeImg();
//			map.put("url", url);
//		} else {
//			map.put("url", "none");
//		}
//		map.put("flag", String.valueOf(isLogin));
//		lt.close();
//		return map;
//	}
//
//	@RequestMapping(value="fuwu_sh_liantong_authcode_img")
//	public  @ResponseBody Map<String, String> fuwu_sh_liantong_authcode_img(HttpServletRequest req,
//			HttpServletResponse resp, Model model,Login login) {
//		Map<String, String> map = new HashMap<String, String>(1);
//		String currentUser = req.getSession().getAttribute("currentUser").toString();
//		SHLiantong lt=new SHLiantong(login, warningService, currentUser);
//		String url = lt.getAuthcodeImg();
//		map.put("url", url);
//		lt.close();
//		return map;
//	}
	/*
	 * 服务密码保存
	 * */
	/*@RequestMapping(value = "/fuwu_sh_liantong_parseBegin", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, String> fuwu_sh_liantong_parseBegin(HttpServletRequest request,
			HttpServletResponse resp, Model model,Login login) {
		Map<String, String> map = new HashMap<String, String>(1);
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String telno = request.getParameter("userName");
		String key =  ConstantNum.getClientMapKey(currentUser+telno,ConstantNum.comm_sh_lt);
		Map<String, Object> redismap= RedisClient.getRedisMap(key);
		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
		lt.parseBegin( redismap,telno, 
				 userService,unicomTelService, unicomDetailService, currentUser, warningService);
		
		map.put("status", "finished");
		return map;
	}*/
	/*
	 * 动态密码不保存
	 * */
	/*@RequestMapping(value = "/dongtai_sh_liantong_parseBegin", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, String> dongtai_sh_liantong_parseBegin(HttpServletRequest request,
			HttpServletResponse resp, Model model,Login login) {
		Map<String, String> map = new HashMap<String, String>(1);
		String telno = request.getParameter("userName");
		String currentUser = request.getSession().getAttribute("currentUser")
				.toString();
		String key =  ConstantNum.getClientMapKey(currentUser+telno,ConstantNum.comm_sh_lt);
		Map<String, Object> redismap= RedisClient.getRedisMap(key);
		SHLiantong lt = new SHLiantong(login, warningService, currentUser);
		lt.parseBegin( redismap,telno, 
		 userService,unicomTelService, unicomDetailService, currentUser, warningService);
		map.put("status", "finished");
		return map;
	}*/
}
