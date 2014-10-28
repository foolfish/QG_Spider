package com.lkb.controller.other;


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

import com.lkb.service.ICBCCheckBillItemService;
import com.lkb.service.ICBCCheckBillService;
import com.lkb.service.ICMBTransferBillService;
import com.lkb.service.IOrderItemService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.CommonMail;
import com.lkb.util.thread.loginParse.Control;




@Controller
public class CommonMail_Controller{
	@Resource
	private IUserService userService;
	
	@Resource
	private ICMBTransferBillService cmbService;
	@Resource
	private ICBCCheckBillService icbcCheckBillService;
	@Resource
	private ICBCCheckBillItemService icbcCheckBillItemService;
	
	@Resource
	private IOrderItemService orderItemService;
	@Resource
	private IWarningService warningService;
	
	private static Logger logger = Logger.getLogger(CommonMail_Controller.class);
	
	@RequestMapping(value = "/163_verifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_163_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_vertifyLogin(request,resp,model,"163");
	}
/*	@RequestMapping(value = "/163_parseBegin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_163_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_parseBegin(request,resp,model,"163");
	}*/
	@RequestMapping(value = "/126_verifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_126_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_vertifyLogin(request,resp,model,"126");
	}
/*	@RequestMapping(value = "/126_parseBegin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_126_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_parseBegin(request,resp,model,"126");
	}*/
	@RequestMapping(value = "/yeah_verifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_yeah_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_vertifyLogin(request,resp,model,"yeah");
	}
	/*@RequestMapping(value = "/yeah_parseBegin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_yeah_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_parseBegin(request,resp,model,"yeah");
	}*/
	@RequestMapping(value = "/139_verifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_139_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_vertifyLogin(request,resp,model,"139");
	}
/*	@RequestMapping(value = "/139_parseBegin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_139_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_parseBegin(request,resp,model,"139");
	}*/
	@RequestMapping(value = "/sohu_verifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_sohu_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_vertifyLogin(request,resp,model,"sohu");
	}
	/*@RequestMapping(value = "/sohu_parseBegin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_sohu_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_parseBegin(request,resp,model,"sohu");
	}*/
	@RequestMapping(value = "/21cn_verifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_21cn_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_vertifyLogin(request,resp,model,"21cn");
	}
	/*@RequestMapping(value = "/21cn_parseBegin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> common_21cn_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		return common_parseBegin(request,resp,model,"21cn");
	}*/
	public Map<String, String> common_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model,String commonMailBrand){
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		Map<String, String> map = new HashMap<String, String>();
		String mail_common_userName = request.getParameter("loginName");
		if(mail_common_userName!=null && mail_common_userName.trim().length()>0){
			String mail_common_password = request.getParameter("password");
			
			CommonMail cm=new CommonMail();
		
			if (cm.verifyLogin(mail_common_userName, mail_common_password,commonMailBrand)){
				map.put("flag", "true");
				
				Control con = new Control( mail_common_userName,  mail_common_password,commonMailBrand,
						userService,cmbService, icbcCheckBillService,icbcCheckBillItemService, warningService,currentUser);
				con.setType(601);
				Thread t = new Thread(con);
				t.setName(mail_common_userName+"邮件_采集中");
				t.start();
				logger.info(mail_common_userName+"邮件验证成功,后台正在采集...");
			}else{
				map.put("flag", "false");
				map.put("url", "none");
				
			}
		}
		
		
		
		
		return map;
	}
	
	
	
	
	/*public Map<String, String> common_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model,String commonMailBrand) {
		Map<String, String> map = new HashMap<String, String>(1);
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String currentUser = request.getSession().getAttribute("currentUser").toString();
	
	
		CommonMail cm=new CommonMail();
		try {
			cm.parseBegin( userName,  password,commonMailBrand,
					userService,cmbService, icbcCheckBillService,icbcCheckBillItemService, warningService,currentUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("第142行捕获异常：",e);		
			e.printStackTrace();
		}
		
		map.put("status", "finished");
		return map;
	}*/
	
	
	

	
	


	
}
