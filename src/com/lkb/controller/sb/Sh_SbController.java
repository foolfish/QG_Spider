//package com.lkb.controller.sb;
//
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
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.lkb.bean.client.Login;
//import com.lkb.controller.BaseInfo_Controller;
//import com.lkb.service.IParseService;
//import com.lkb.service.ISheBaoService;
//import com.lkb.service.IUserService;
//import com.lkb.service.IWarningService;
//import com.lkb.thirdUtil.shebao.ShShebao;
//import com.lkb.util.thread.loginParse.Control;
//
///*
// * 上海社保
// * */
//@Controller
//public class Sh_SbController  extends BaseInfo_Controller{
//	@Resource
//	private IUserService userService;
//	@Resource
//	private ISheBaoService shebaoService;
//	@Resource
//	private IWarningService warningService;
//	@Resource
//	private IParseService parseService;
//	private static Logger logger = Logger.getLogger(Sh_SbController.class);
//	@RequestMapping(value="shanghai_shebao_vertifyLogin")//,method=RequestMethod.POST)
//	public @ResponseBody Map<String, Object> shanghai_shebao_vertifyLogin(HttpServletRequest req,
//			HttpServletResponse resp, Model model) {
//		System.out.println("上海社保开始登陆！");
//		String userName = req.getParameter("loginName");
//		String password = req.getParameter("password");
//		Login login1 =	new Login(userName,password);
//		Map<String,Object> map = login(new ShShebao(login1,warningService, getCurrentUser(req)));
//		try{
//			Boolean flag = (Boolean)map.get("state");
//			if(flag){
//				Control con = new Control( userName,  password,
//						userService, shebaoService,warningService,parseService,getCurrentUser(req));
//				con.setType(401);
//				Thread t = new Thread(con);
//				t.setName(userName+"上海社保_采集中");
//				t.start();
//				logger.info(userName+"上海社保验证成功,后台正在采集...");
//			}
//		}catch(Exception e){}
//		return map;
//	}
//	
//	
//	
//}
