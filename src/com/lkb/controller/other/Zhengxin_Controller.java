package com.lkb.controller.other;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ErrorMsg;
import com.lkb.controller.BaseInfo_Controller;
import com.lkb.service.IParseService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.service.IZhengxinDetailService;
import com.lkb.service.IZhengxinSummaryService;
import com.lkb.thirdUtil.XueXin;
import com.lkb.thirdUtil.Zhengxin;
import com.lkb.util.thread.loginParse.Control;

@Controller
@RequestMapping(value="/zhengxin")
public class Zhengxin_Controller extends BaseInfo_Controller{
	@Resource
	private IUserService userService;
	@Resource
	private IZhengxinSummaryService zxsummaryService;
	@Resource
	private IZhengxinDetailService zxDetailService;
	@Resource
	private IWarningService warningService;
	@Resource
	private IParseService parseService;
	
	private static Logger logger = Logger.getLogger(Zhengxin_Controller.class);
	@RequestMapping(value="/verifyLogin")//,method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> shanghai_shebao_vertifyLogin(HttpServletRequest req,
			HttpServletResponse resp, Model model,Login login) {
		String currentUser = getCurrentUser(req);
		if(StringUtils.isEmpty(login.getLoginName())){
			Map<String,Object> mp = new LinkedHashMap<String, Object>();
			mp.put("errorcode", ErrorMsg.loginNameEorror);
			return mp;
		}else if(StringUtils.isEmpty(login.getPassword())){
			Map<String,Object> mp = new LinkedHashMap<String, Object>();
			mp.put("errorcode", ErrorMsg.passwordEorror);
			return mp;
		}else if(StringUtils.isEmpty(login.getSpassword())){
			Map<String,Object> mp = new LinkedHashMap<String, Object>();
			mp.put("errorcode", ErrorMsg.spassEorror);
			return mp;
		}
		if(login!=null&&login.getAuthcode()!=null&&!login.getAuthcode().equals("")){}
		else{
			Map<String,Object> map = new LinkedHashMap<String, Object>();
			map.put("errorcode",ErrorMsg.authcodetEorror);
			return map;
		}
		Map<String, Object> map = login(new Zhengxin(login,currentUser));
		try{
			int status = Integer.parseInt(map.get(CommonConstant.status).toString());
	 		if(status==1){
	 			String content = map.get("content").toString();//跟登陆对应
				Control con = new Control(login.getLoginName(), content,
						userService, zxsummaryService,zxDetailService, warningService,currentUser,parseService);
				con.setType(502);
				Thread t = new Thread(con);
				t.setName(login.getLoginName()+"征信_采集中");
				t.start();
			}
			map.remove("content");//禁止返回该字符串
		}catch(Exception e){}
		return map;
	}
	

	@RequestMapping(value = "/init")//, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongZxFirst(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String putong_zx_userName = request.getParameter("loginName");
		return putongFirst(new Zhengxin(new Login(putong_zx_userName,null),getCurrentUser(request)));
	}
	@RequestMapping(value = "/isLogin")
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request) {
		String putong_jd_userName = request.getParameter("loginName");
		return isLogin(new Zhengxin(new Login(putong_jd_userName,null),getCurrentUser(request)));
	}
//	@RequestMapping(value = "/getZxAuthImg")
//	public @ResponseBody Map getAuthImg(HttpServletRequest request,HttpServletResponse resp, Model model) {
//		String putong_zx_userName = request.getParameter("loginName");
//		String currentUser = request.getSession().getAttribute("currentUser").toString();		
//		Zhengxin zhengxin = new Zhengxin(new Login(putong_zx_userName,null),warningService,currentUser);
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("url", zhengxin.getAuthcode());		
//		zhengxin.close();
//		return map;
//	}
	
	
	}
	
