package com.lkb.controller.dx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.dx.GSDianxin;


@Controller
@RequestMapping(value = "/dx/gs")
public class GSDianXin_Controller extends Mobile_Controller{
	
	private static Logger logger = Logger.getLogger(GSDianXin_Controller.class);

	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new GSDianxin(login,getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new GSDianxin(login, getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.info("甘肃电信开始验证用户名!");
		return login(new GSDianxin(login, getCurrentUser(request)) , login);
	}
	
	@RequestMapping(value = "/first", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new GSDianxin(login, getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/image")
	public @ResponseBody Map getAuthImg(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		return getAuthImg(new GSDianxin(login,getCurrentUser(request)));
	}
	
	
/*	@RequestMapping(value = "/putong_gs_dianxin_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putong_gs_dianxin_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String authCode = request.getParameter("authCode");
		int  flag  =0;
		Map<String, String> map = new HashMap<String, String>();
		try {
			
			Map tjmap =DxConstant.gs_dxMap.get(currentUser);
			DefaultHttpClient client = (DefaultHttpClient)tjmap.get(ConstantHC.k_client);	
		
			GSDianxin service = new GSDianxin();
			flag  = service.loginHome(client,phone, password, authCode,currentUser,warningService);
			if(flag==1){
				Control con = new Control(phone, userService,  warningService,  currentUser,dianXinTelService,dianXinDetailService);
				con.setType(208);
				Thread t = new Thread(con);
				t.setName(phone+"甘肃电信_采集中");
				t.start();
				logger.info(phone+"甘肃电信验证成功,后台正在采集...");
			}
			map.put("flag", String.valueOf(flag));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("flag",  String.valueOf(flag));
		}
		return map;
	}
	

	
	
	
	
	@RequestMapping(value="/putong_gs_dianxin_GetAuth",method=RequestMethod.POST)
	public  @ResponseBody Map<String, String> putong_gs_dianxin_GetAuth(HttpServletRequest req,
			HttpServletResponse resp, Model model) {
		String currentUser = req.getSession().getAttribute("currentUser").toString();
		Map tjmap =DxConstant.gs_dxMap.get(currentUser);
		DefaultHttpClient client = (DefaultHttpClient)tjmap.get(ConstantHC.k_client);	
		Map<String,String> map=new HashMap<String,String>();
		GSDianxin service = new GSDianxin();
		
		String fileName=service.getAuthcode(currentUser);
		String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
		fileName=imgPath2+fileName;
		map.put("url", fileName);
		return map;
	}*/
	
	
	
}
