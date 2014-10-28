package com.lkb.controller.dx;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.constant.Constant;
import com.lkb.constant.DxConstant;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.dx.BJDianxin;
import com.lkb.thirdUtil.dx.SHDianXin;
import com.lkb.util.IPUtil;
import com.lkb.util.InfoUtil;
import com.lkb.util.thread.loginParse.Control;

@Controller
public class SHDianXin_Controller {
	@Resource
	private IUserService userService;
	@Resource
	private IDianXinTelService dianxinTelService;
	@Resource
	private IWarningService warningService;
	@Resource
	private IDianXinDetailService dianxinDetailService;
	private static String queryPayDetail = "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10003&toStUrl=http://sh.189.cn/service/account_manage_query.do?functionName=queryPayDetail";
	private static Logger logger = Logger.getLogger(SHDianXin_Controller.class);
	//验证码图片路径
	private static String catImgUrl = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?";
	//验证码图片路径
	private static String catImgUrl2 = "http://sh.189.cn/service/RandomNum_new2.jsp?";
	private static String loginUrl2 = "https://uam.ct10000.com/ct10000uam/login?service=http://www.189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=";	
	
	private static String dynamicValidate = "http://sh.189.cn/service/dynamicValidate.do";
	
	@RequestMapping(value = "/fuwu_sh_dianxin_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> fuwu_sh_dianxin_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String telno = request.getParameter("userName");
		String password = request.getParameter("password");
		String authcode = request.getParameter("authcode");
		CloseableHttpClient httpclient=DxConstant.sh_dxcloseClientMap.get(currentUser);
		Map dxMap = Constant.sh_dxMap.get(currentUser);
		Map<String, String> map = new HashMap<String, String>();
		SHDianXin dx = new SHDianXin();
		
		IPUtil iputil = new IPUtil();
		String userIp = iputil.getIP();
		//查找数据
		dx.putDate(httpclient,dxMap, userIp );
		String type="3";
		String open_no = "c2000004";
		dxMap.put("username", telno);
		dxMap.put("password", password);
		dxMap.put("randomId", authcode);
		dxMap.put("customFileld01", type);
		dxMap.put("open_no", open_no);
		

		//根据手机号判断地区,并把数据写入map
		dx.putDate2( httpclient ,telno,dxMap);
		Constant.sh_dxNoMap.put(currentUser, telno);
		
		String returnUrl = dx.login( httpclient , loginUrl2+userIp, dxMap).replace("<script type='text/javascript'>location.replace('", "").replace("');</script>", "");
		
		//System.out.println(returnUrl.length());
		Boolean flag=false;
		if(returnUrl.length()<500&&returnUrl.contains("http://www.189.cn/dqmh/Uam.do?method=loginJTUamGet&")){
			flag=true;
		}
		int  flags = 0;
		if(flag==true){

			String syr = dx.getText(httpclient,returnUrl);
			String syr5= dx.getText1( httpclient,queryPayDetail);
			syr5 = syr5.replace("<script type='text/javascript'>location.replace('", "").replace("');</script>", "");
		    
			DefaultHttpClient httpclient1 = new DefaultHttpClient(new PoolingClientConnectionManager());  
			DxConstant.sh_dxcloseClientMap1.put(currentUser, httpclient1);
			String syr6= dx.getText3( httpclient1,syr5);

			syr6 = syr6.replace("<script type='text/javascript'>location.replace('", "").replace("');</script>", "");
		
			String syr7= dx.getText3( httpclient1,syr6);

			String syr8= dx.getText3( httpclient1,"http://sh.189.cn/service/uiss_mobileLogin.do?method=login");

			String syr9= dx.getText3( httpclient1,"http://sh.189.cn/service/redirect.do?service=detailQuery&tmp=");
		
			flags = 1;
			
			try {				

//				int random = (int)(Math.random()*1000);
//				String picName2 = currentUser+random+"_putongdianxin2.png";
//				
//				java.text.DateFormat format2 = new java.text.SimpleDateFormat(
//						"yyyyMMdd");
//				String dataPath = format2.format(new Date());
//				
//				dx.downloadFile(httpclient1,catImgUrl2+Math.random(), dataPath,picName2,"","");
//				String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
//				picName2= imgPath2+dataPath+"/"+picName2;
//				map.put("url", picName2);
				
			}catch (Exception e) {
			}
		}
		else{
			
			
		}
		
		map.put("flag",  String.valueOf(flags));	
		
		return map;
	}
	
	@RequestMapping(value="/putong_sh_dianxin_GetAuth",method=RequestMethod.POST)
	public  @ResponseBody Map<String, String> putong_sh_dianxin_GetAuth(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = req.getSession().getAttribute("currentUser").toString();
		CloseableHttpClient httpclient = DxConstant.sh_dxcloseClientMap.get(currentUser);
		Map<String,String> map=new HashMap<String,String>();
		SHDianXin service = new SHDianXin();
		String fileName=service.getAuthcode(httpclient, SHDianXin.catImgUrl,currentUser,"_shdx.png");
		String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
		fileName=imgPath2+fileName;
		map.put("url", fileName);
		return map;
	}
	
	
	@RequestMapping(value = "/sh_dxSend", method = RequestMethod.POST)
	public @ResponseBody Map<String, String>  sh_dxSend(HttpServletRequest request,HttpServletResponse resp, Model model) {
	
		Map<String, String> map = new HashMap<String, String>();
		Map<String,String> dxMap1 = new LinkedMap();
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String authValue = request.getParameter("authValue");
		SHDianXin dx = new SHDianXin();
	    dxMap1.put("dynnum","");
		dxMap1.put("imgnum",authValue);
		dxMap1.put("requesttype","send");
		dxMap1.put("oper","cnetQueryCondition.do?actionCode=init");
		HttpClient httpclient1 = DxConstant.sh_dxcloseClientMap1.get(currentUser);  
		String ll = dx.login1(httpclient1,dynamicValidate, dxMap1);
		if(ll.contains("图片验证码输入错误")){
				System.out.println("图片验证码输入错误,请重新输入！");
				map.put("flag", "1");
		}else{
			//发送短信成功
			map.put("flag", "2");
		}
		return map;
	}
	
	
	@RequestMapping(value = "/sh_dianxinauth", method = RequestMethod.POST)
	public @ResponseBody Map<String, String>  sh_dianxinauth(HttpServletRequest request,HttpServletResponse resp, Model model) {
		Map<String, String> map = new HashMap<String, String>();
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String duanxin = request.getParameter("dxpass");
		String tupian = request.getParameter("dxauth");
		SHDianXin dx = new SHDianXin();
		Map<String,String> dxMap2 = new LinkedMap();
		DefaultHttpClient httpclient1 = DxConstant.sh_dxcloseClientMap1.get(currentUser);  
	    dxMap2.put("dynnum",duanxin);
	    dxMap2.put("imgnum",tupian);
	    dxMap2.put("requesttype","end");
	    dxMap2.put("oper","cnetQueryCondition.do?actionCode=init");
		String ll2 = dx.login1(httpclient1,dynamicValidate, dxMap2);
		System.out.println(ll2);
		Boolean flag=false;
		if(!ll2.contains("动态验证码输入错误!")){
			flag=true;
		}
		
		if(flag==true){
			map.put("flag", String.valueOf(flag));	
			
		 String phone = Constant.sh_dxNoMap.get(currentUser);
			Control con = new Control(phone, userService,  warningService,  currentUser,dianxinTelService,dianxinDetailService);
			con.setType(203);
			Thread t = new Thread(con);
			t.setName(phone+"上海电信_采集中");
			t.start();
			logger.info(phone+"上海电信验证成功,后台正在采集...");
		}
		else{
			try {
				map.put("flag", String.valueOf(flag));	
			}
				
			catch (Exception e) {
			}
		}
		return map;
	}
	
	
	@RequestMapping(value="/dongtai_sh_dianxin_GetAuth",method=RequestMethod.POST)
	public  @ResponseBody Map<String, String> dongtai_sh_dianxin_GetAuth(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		Map<String, String> map = new HashMap<String, String>();
		String currentUser = req.getSession().getAttribute("currentUser").toString();
		DefaultHttpClient httpclient1 = DxConstant.sh_dxcloseClientMap1.get(currentUser);  
		SHDianXin dx = new SHDianXin();
		
		int random = (int)(Math.random()*1000);
		String picName2 = currentUser+random+"_putongdianxin2.png";
		
		java.text.DateFormat format2 = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		String dataPath = format2.format(new Date());
		
		try {
			dx.downloadFile(httpclient1,catImgUrl2+Math.random(), dataPath,picName2,"","");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
		picName2= imgPath2+dataPath+"/"+picName2;
		map.put("url", picName2);
		
		
		return map;
	}
	
	
//	@RequestMapping(value = "/sh_dianxin_parseBegin", method = RequestMethod.POST)
//	public @ResponseBody Map<String, String> sh_dianxin_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
//		Map<String, String> map = new HashMap<String, String>(1);
//		String currentUser = request.getSession().getAttribute("currentUser").toString();
//		SHDianXin dx=new SHDianXin();
//		HttpClient httpclient=DxConstant.sh_dxcloseClientMap1.get(currentUser);
//		String phone= Constant.sh_dxNoMap.get(currentUser);
//		dx.parseList(httpclient, phone, "null", currentUser,userService,dianxinDetailService, warningService);
//		map.put("status", "finished");
//		return map;
//	}

	
//	@RequestMapping(value = "/sendCardDX", method = RequestMethod.POST)
//	public @ResponseBody String sendCardDX(HttpServletRequest request,HttpServletResponse resp, Model model) {
//		String currentUser = request.getSession().getAttribute("currentUser").toString();
//		//Map<String, String> map = new HashMap<String, String>();
//		String phone = request.getParameter("phone");
//
//		CloseableHttpClient httpclient = Constaint.httpClientMap.get(currentUser);
//
//		DianXin dianxin = new DianXin();
//		String json ="";
//				//dianxin.sendCard(httpclient,phone);		
//		return json;
//	}
//	
//	@RequestMapping(value = "/fuwu_dianxin_vertifyLogin", method = RequestMethod.POST)
//	public @ResponseBody Map<String, String> fuwu_dianxin_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
//		String currentUser = request.getSession().getAttribute("currentUser").toString();
//		String telno = request.getParameter("userName");
//		String password = request.getParameter("password");
//		String ptype = "2";
//		Map<String, String> map = new HashMap<String, String>();
//		CloseableHttpClient httpclient = Constaint.httpClientMap.get(currentUser);
//
//		Yidong yidong = new Yidong();
//		String json = yidong.vertifyLogin(httpclient, telno, password, ptype);
//		String flag = "false";
//		if(json.contains("\"result\":\"0\"")){
//			flag = "true";
//		}
//		map.put("json", json);	
//		map.put("flag", flag);	
//		return map;
//	}
//	
//	@RequestMapping(value = "/dongtai_dianxin_vertifyLogin", method = RequestMethod.POST)
//	public @ResponseBody Map<String, String> dongtai_dianxin_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
//		String currentUser = request.getSession().getAttribute("currentUser").toString();
//		String telno = request.getParameter("userName");
//		String password = request.getParameter("password");
//		String ptype = "1";
//		Map<String, String> map = new HashMap<String, String>();
//		CloseableHttpClient httpclient = Constaint.httpClientMap.get(currentUser);
//		httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");  
//		Yidong yidong = new Yidong();
//	    String json = yidong.vertifyLogin(httpclient, telno, password, ptype);
//		String flag = "false";
//		if(json.contains("\"result\":\"0\"")){
//			flag = "true";
//		}
//		map.put("json", json);	
//		map.put("flag", flag);	
//		return map;
//	}
//	
//	
//	/*
//	 * 收集用户数据；注意当ptype=2.也就是服务密码时，才要将密码记入数据库，否则不计入，因为动态密码没有意义
//	 * */
//	@RequestMapping(value = "/fuwu_dianxin_parseBegin", method = RequestMethod.POST)
//	public @ResponseBody Map<String, String> fuwu_dianxin_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
//		Map<String, String> map = new HashMap<String, String>(1);
//		String telno = request.getParameter("userName");
//		String password = request.getParameter("password");
//		String ptype = "2";
//		String currentUser = request.getSession().getAttribute("currentUser").toString();
//		CloseableHttpClient httpclient = Constaint.httpClientMap.get(currentUser);
////		Dianxin yidong = new Dianxin();
////		yidong.parseBegin( httpclient,telno,  password,
////				userService,mobileTelService,currentUser);
//		map.put("status", "finished");
//		return map;
//	}
//	
//	/*
//	 * 收集用户数据；注意当ptype=2.也就是服务密码时，才要将密码记入数据库，否则不计入，因为动态密码没有意义
//	 * */
//	@RequestMapping(value = "/dongtai_dianxin_parseBegin", method = RequestMethod.POST)
//	public @ResponseBody Map<String, String> dongtai_dianxin_parseBegin(HttpServletRequest request,HttpServletResponse resp, Model model) {
//		Map<String, String> map = new HashMap<String, String>(1);
//		String telno = request.getParameter("userName");
//		String password = request.getParameter("password");
//		String ptype = "1";
//		String currentUser = request.getSession().getAttribute("currentUser").toString();
//		CloseableHttpClient httpclient = Constaint.httpClientMap.get(currentUser);
////		Yidong yidong = new Yidong();
////		yidong.parseBegin( httpclient,telno,  password,
////				userService,mobileTelService,currentUser);
//		map.put("status", "finished");
//		return map;
//	}
	
	
	
}
