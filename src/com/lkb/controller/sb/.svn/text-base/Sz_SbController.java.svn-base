//package com.lkb.controller.sb;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.lkb.bean.client.Login;
//import com.lkb.constant.ErrorMsg;
//import com.lkb.controller.BaseInfo_Controller;
//import com.lkb.service.IParseService;
//import com.lkb.service.ISheBaoService;
//import com.lkb.service.IUserService;
//import com.lkb.service.IWarningService;
//import com.lkb.thirdUtil.shebao.guangdong.ShenZhenShebao;
//import com.lkb.util.thread.loginParse.Control;
//
///*
// * 深圳社保
// */
//@Controller
//@RequestMapping(value = "/shebao/shenzhen")
//public class Sz_SbController extends BaseInfo_Controller{
//	@Resource
//	private IUserService userService;
//	@Resource
//	private ISheBaoService shebaoService;
//	@Resource
//	private IWarningService warningService;
//	@Resource
//	private IParseService parseService;
//	private static Logger logger = Logger.getLogger(Sz_SbController.class);
//	
//	
//	@RequestMapping(value="/vertifyLogin")//,method=RequestMethod.POST)
//	public @ResponseBody Map<String, Object> vertifyLogin(HttpServletRequest req,
//			HttpServletResponse resp, Model model,Login login) {	
//		if(login!=null&&login.getAuthcode()!=null&&!login.getAuthcode().equals("")){}
//		else{
//			Map<String,Object> map = new LinkedHashMap<String, Object>();
//			map.put("errorcode",ErrorMsg.authcodetEorror);
//			return map;
//		}
//		Map<String,Object> map = login(new ShenZhenShebao(login, getCurrentUser(req)));
//		try{
//			Boolean flag = (Boolean)map.get("state");		
//	 		if(flag){		
//				Control con = new Control( login.getLoginName(),  login.getPassword(),
//						userService, shebaoService,warningService,parseService,getCurrentUser(req));
//				con.setType(402);
//				Thread t = new Thread(con);
//				t.setName(login.getLoginName()+"深圳社保_采集中");
//				t.start();
//				logger.info(login.getLoginName()+"深圳社保验证成功,后台正在采集...");
//			}
//		}catch(Exception e){
//		}
//		return map;
//	}
//	
//
//	
//	@RequestMapping(value = "/first")//, method = RequestMethod.POST)
//	public @ResponseBody Map<String, Object> first(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
//		return putongFirst(new ShenZhenShebao(login,getCurrentUser(request)));
//	}
//	
////	@RequestMapping(value = "/image")
////	public @ResponseBody Map image(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
////		return getAuthImg(new ShenZhenShebao(login,warningService, getCurrentUser(request)));
////	}
//	
//}
