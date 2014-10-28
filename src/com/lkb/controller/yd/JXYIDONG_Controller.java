package com.lkb.controller.yd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.constant.ErrorMsg;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.yd.JXYidong;
import com.lkb.thirdUtil.yd.SDYidong;


@Controller
@RequestMapping(value = "/yd/jx")
public class JXYIDONG_Controller extends Mobile_Controller{
	
	
	private static Logger logger = Logger.getLogger(JXYIDONG_Controller.class);

	
	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new JXYidong(login,getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new JXYidong(login,getCurrentUser(request)));
	}

	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.info("江西移动开始验证用户名!");
		JXYidong jx =	new JXYidong(login,getCurrentUser(request));
		return login(jx, login);
	}
	
	
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new JXYidong(login,getCurrentUser(request)));
	}
	
	

	
	@RequestMapping(value = "/getSecImgUrl")
	public @ResponseBody Map<String,Object> getSecImgUrl(HttpServletRequest request,Login login) {
		JXYidong jx = new JXYidong(login,getCurrentUser(request));
		setService(jx);
		Map<String,Object> map = jx.getSecImgUrl();
		map.put("errorcode",ErrorMsg.normal);
		jx.close();
		return map;
	}
	
	
	
	
	/*@RequestMapping(value = "/putong_jx_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putong_jx_yidong_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String authCode = request.getParameter("authCode");
		Map<String, String> map = new HashMap<String, String>();
		try {
//			CloseableHttpClient httpclient = YdConstant.jx_ydcloseClientMap.get(currentUser);  
//			JXYidong yidong = new JXYidong();
//			String spid = yidong.getSpid(httpclient);
//			map.put("spid", spid);	
//			boolean  flag  = yidong.loginHome(httpclient, spid,phone, password, authCode,currentUser,warningService);
//			if(flag){
//				//yidong.sendMsg(httpclient, phone);
//				String fileName=yidong.getAuthcode(httpclient, JXYidong.catImgUrl2,currentUser+"_dy");
//				String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
//				fileName=imgPath2+fileName;
//				map.put("url", fileName);	
//			}
//			map.put("flag", String.valueOf(flag));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("flag", "false");
		}
		return map;
	}
	

	@RequestMapping(value = "/dongtai_jx_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> dongtai_jx_yidong_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String smsCode = request.getParameter("smsCode");
		String smsAuthCode = request.getParameter("smsAuthCode");
		String spid = request.getParameter("spid");
		String phone = request.getParameter("phone");
		Map<String, String> map = new HashMap<String, String>();
		CloseableHttpClient httpclient = YdConstant.jx_ydcloseClientMap.get(currentUser);
//		JXYidong yidong = new JXYidong();
//		boolean result = yidong.loginMsg(httpclient, phone, spid, smsAuthCode, smsCode,currentUser,warningService);
//		String reMsg="";
//		if(result){
//			Control con = new Control(phone, userService, warningService, currentUser, mobileDetailService, mobileTelService);
//			con.setType(301);
//			Thread t = new Thread(con);
//			t.setName(phone+"江西移动_采集中");
//			t.start();
//			logger.info(phone+"江西移动验证成功,后台正在采集...");
//			// 获取个人资料
//			boolean isMyInfo = yidong.getMyInfo(httpclient, currentUser, warningService, userService);
//			if(isMyInfo){
//				reMsg+="获取个人资料成功.";
//			}else{
//				reMsg+="获取个人资料失败.";
//			}
//			//获取通话记录
//			boolean isPhoneDetail = yidong.getPhoneDetailHtml(httpclient, mobileDetailService, phone,currentUser, warningService);
//			if(isPhoneDetail){
//				reMsg+="获取通话记录成功.";
//			}else{
//				reMsg+="获取通话记录失败.";
//			}
//			// 获取话费
//			boolean isTelDetail = yidong.getTelDetailHtml(httpclient, phone, currentUser, warningService, mobileTelService);
//			if(isTelDetail){
//				reMsg+="获取话费资料成功.";
//			}else{
//				reMsg+="获取话费资料失败.";
//			}
//			map.put("flag1",String.valueOf((isMyInfo&&isPhoneDetail&&isTelDetail)));	
//		}
//		map.put("flag",String.valueOf(result));	
//		map.put("reMsg",reMsg);	
		return map;
	}
	
	
	
	
	
	
	
	@RequestMapping(value="/putong_jx_yidong_GetDyAuth",method=RequestMethod.POST)
	public  @ResponseBody Map<String, String> putong_jx_yidong_GetDyAuth(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = req.getSession().getAttribute("currentUser").toString();
		CloseableHttpClient httpclient = YdConstant.jx_ydcloseClientMap.get(currentUser);
		Map<String,String> map=new HashMap<String,String>();
//		JXYidong yidong = new JXYidong();
//		String fileName=yidong.getAuthcode(httpclient, JXYidong.catImgUrl2,currentUser+"_dy");
//		String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
//		fileName=imgPath2+fileName;
//		map.put("url", fileName);
		return map;
	}
	
	@RequestMapping(value="/putong_jx_yidong_GetAuth",method=RequestMethod.POST)
	public  @ResponseBody Map<String, String> putong_jx_yidong_GetAuth(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = req.getSession().getAttribute("currentUser").toString();
		CloseableHttpClient httpclient = YdConstant.jx_ydcloseClientMap.get(currentUser);
		Map<String,String> map=new HashMap<String,String>();
//		JXYidong yidong = new JXYidong();
//		String fileName=yidong.getAuthcode(httpclient, JXYidong.catImgUrl,currentUser);
//		String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
//		fileName=imgPath2+fileName;
//		map.put("url", fileName);
		return map;
	}
	
	@RequestMapping(value="/putong_jx_yidong_GetSms",method=RequestMethod.POST)
	public  @ResponseBody Map<String, String> putong_jx_yidong_GetSms(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = req.getSession().getAttribute("currentUser").toString();
		String phone = req.getParameter("phone");
		CloseableHttpClient httpclient = YdConstant.jx_ydcloseClientMap.get(currentUser);
		Map<String,String> map=new HashMap<String,String>();
//		JXYidong yidong = new JXYidong();
//		String msgCode = yidong.sendMsg(httpclient, phone);
//		map.put("msgCode",msgCode);
		return map;
	}
	*/
}
