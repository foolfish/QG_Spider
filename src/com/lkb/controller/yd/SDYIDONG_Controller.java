package com.lkb.controller.yd;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.constant.ErrorMsg;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.yd.SDYidong;


@Controller
@RequestMapping(value = "/yd/sd")
public class SDYIDONG_Controller  extends Mobile_Controller{
	private static Logger logger = Logger.getLogger(SDYIDONG_Controller.class);


	/**
	 * 校验手机通话记录动态口令
	 */
	@RequestMapping(value = "/checkDynamics")
	public @ResponseBody Map<String, Object> checkDynamicsCode(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new SDYidong(login,getCurrentUser(request)));
	}
	
	/**
	 * 通话记录动态口令
	 */
	@RequestMapping(value = "/getSms")
	public @ResponseBody Map<String, Object> sendPhoneDynamicsCode(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new SDYidong(login,getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/login")
	public @ResponseBody Map<String, Object> login(HttpServletRequest request,Login login) {
		logger.info("山东移动开始验证用户名!");
		SDYidong sd =	new SDYidong(login,getCurrentUser(request));
		return login(sd, login);
	}
	
	@RequestMapping(value = "/first")
	public @ResponseBody Map<String, Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new SDYidong(login,getCurrentUser(request)));
	}
	
//	@RequestMapping(value = "/image")
//	public @ResponseBody Map getAuthImg(HttpServletRequest request,Login login) {
//		return getAuthImg(new SDYidong(login,getCurrentUser(request)));
//	}
//	
	@RequestMapping(value = "/getSecImgUrl")
	public @ResponseBody Map<String,Object> getSecImgUrl(HttpServletRequest request,Login login) {
		SDYidong sd = new SDYidong(login,getCurrentUser(request));
		setService(sd);
		Map<String,Object> map = sd.getSecImgUrl();
		map.put("errorcode",ErrorMsg.normal);
		sd.close();
		return map;
	}
	
	
	/*
	@RequestMapping(value = "/sd_yidong_sendCard", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> sd_yidong_sendCard(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String phone = request.getParameter("phone");
		
		Map<String, String> map = new HashMap<String, String>();
		String key =  ConstantNum.getClientMapKey(currentUser+phone,ConstantNum.comm_sd_yidong);
		Map<String, Object> redismap= RedisClient.getRedisMap(key);
		SDYidong yidong = new SDYidong();
		String json = yidong.sendMsg(RedisClient.getClient(redismap),phone);
		map.put("msgcode", json);
		RedisClient.setRedisMap(redismap, key);
		return map;
	}
	
	@RequestMapping(value = "/dongtai_sd_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> dongtai_sd_yidong_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String phone = request.getParameter("phone");		
		String dtpassword = request.getParameter("dtpassword");
		String smsAuthCode = request.getParameter("smsAuthCode");
		String key =  ConstantNum.getClientMapKey(currentUser+phone,ConstantNum.comm_sd_yidong);
		Map<String, Object> redismap= RedisClient.getRedisMap(key);
		Map map = new HashMap<String, String>();
		SDYidong yidong = new SDYidong();
		String result = yidong.loginMsg(RedisClient.getClient(redismap), phone, dtpassword,smsAuthCode,mobileDetailService,currentUser,warningService);
		//result="1";
		if(result.equals("1")){
			Control con = new Control(phone, userService, warningService, currentUser, mobileDetailService, mobileTelService);
			con.setType(311);
			Thread t = new Thread(con);
			t.setName(phone+"山东移动_采集中");
			t.start();
			logger.info(phone+"山东移动验证成功,后台正在采集...");
			
			map.put("flag", "1");	
		}else{
			map.put("message",result);
			map.put("flag", "-1");
		}
		RedisClient.setRedisMap(redismap, key);
		return map;
	}
	
	
	@RequestMapping(value = "/fuwu_sd_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> fuwu_sd_yidong_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model,Login login) {
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		SDYidong yidong = new SDYidong();
		Map mp = new HashMap();
		mp.put("flag", "false");
	
		if(Login.checkLogin(login)){
			String key =  ConstantNum.getClientMapKey(currentUser+login.getLoginName(),ConstantNum.comm_sd_yidong);
			Map<String, Object> redismap= RedisClient.getRedisMap(key);
			if(RedisClient.getClient(redismap)==null){
				mp.put("flag", "true");
			}else{				
				String flag = "false";
				String jsons = yidong.vertifyLogin(redismap, login);
				if(jsons!=null && jsons.equals("1")){
					flag = "true";
				}else{	
					String message = "网络异常，请稍后再试！";
					if(jsons!=null&&!jsons.equals("null")&&jsons.length()>0){
						message= jsons;
					}
				
					mp.put("message", message);	
					
				}
				mp.put("flag", flag);
				RedisClient.setRedisMap(redismap, key);
			}	
		}
		return mp;
	}
	
	
	@RequestMapping(value = "/putongsdydFirst", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putongsdydFirst(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String phone = request.getParameter("phone");
		String isReAuthcode = request.getParameter("isReAuthcode");
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String key =  ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_sd_yidong);
		Map<String, Object> redismap= RedisClient.getRedisMap(key);
		DefaultHttpClient client = RedisClient.getClient(redismap);
		Map<String, String> map2 = new HashMap<String, String>(1);
		if(!RedisClient.isLogin(redismap)){
			SDYidong yidong = new SDYidong();
			if(isReAuthcode!=null&&isReAuthcode.equals("1")){
			}else{
				yidong.index(redismap);
				//存放cookie
				RedisClient.setRedisMap(redismap, key);
			}
			redismap= RedisClient.getRedisMap(key);
			client = RedisClient.getClient(redismap);
			String fileName=yidong.getAuthcode(key,redismap);
			String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
			fileName=imgPath2+fileName;
			map2.put("url", fileName);
			//存放cookie
			RedisClient.setRedisMap(redismap, key);
		}
		
		return map2;
	}
	
	
	@RequestMapping(value = "/putongsdydSecond", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putongsdydSecond(HttpServletRequest request,HttpServletResponse resp, Model model) {
		String phone = request.getParameter("phone");
		String isReAuthcode = request.getParameter("isReAuthcode");
		String currentUser = request.getSession().getAttribute("currentUser").toString();
		String key =  ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_sd_yidong);
		Map<String, Object> redismap= RedisClient.getRedisMap(key);
		DefaultHttpClient client = RedisClient.getClient(redismap);
		Map<String, String> map2 = new HashMap<String, String>(1);
		if(!RedisClient.isLogin(redismap)){
			SDYidong yidong = new SDYidong();
		
			redismap= RedisClient.getRedisMap(key);
			client = RedisClient.getClient(redismap);
			String fileName=yidong.getSecondAuthcode(key,redismap);
			String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
			fileName=imgPath2+fileName;
			map2.put("url", fileName);
			//存放cookie
			RedisClient.setRedisMap(redismap, key);
		}
		
		return map2;
	}*/
	
	
}
