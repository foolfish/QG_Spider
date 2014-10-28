package com.lkb.controller.yd;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.req.FormData;
import com.lkb.constant.Constant;
import com.lkb.controller.telcom.AbstractYiDongController;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.util.RobotUtil;
import com.lkb.thirdUtil.yd.XZYidong;
import com.lkb.warning.WarningUtil;


@Controller
public class XZYIDONG_Controller extends AbstractYiDongController {
	
	private static final String TYPE = "XZYIDONG_Controller";
	
	@RequestMapping(value = "/xz_yidong_sendCard", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> xz_yidong_sendCard(HttpServletRequest req,HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return getSms(true, fd);
	}
	public Map<String, String> getSms(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		String phone = fd.getPhoneNo();
	//	final String token = fd.getRequest().getSession().getAttribute("token").toString();
		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);

				final XZYidong yidong = new XZYidong(spider, null, phone, null, null, util);
				yidong.sendMsg();
				SpiderManager.getInstance().startSpider(spider, null, yidong);	
				yidong.waitStatus();
				setResult(yidong, map);
			} catch (Exception e) {
				logger.error("error", e);
			} finally {
				if (init) {
					RobotUtil.stop();
				}
			}
		} else {
			flag = 999;
		}

		if (map.get("flag") == null) {
			map.put("flag", String.valueOf(flag));
		}
		return map;
	}
	
	@RequestMapping(value = "/dongtai_xz_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> dongtai_xz_yidong_vertifyLogin(HttpServletRequest req, HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return dongtai_vertifyLogin(true, fd);
	}
	public Map<String, String> dongtai_vertifyLogin(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		//final String token = fd.getRequest().getSession().getAttribute("token").toString();
		String dtpassword = fd.getSmsCode();
		//String authCode = fd.getAuthCode();
		String phone = fd.getPhoneNo();

		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				final User user = getUser(currentUser, phone, Constant.YIDONG);

				final XZYidong yidong = new XZYidong(spider, user, phone, dtpassword, null, util);
				yidong.loginMsg();
				SpiderManager.getInstance().startSpider(spider, saveSpiderListener(yidong, user, currentUser), yidong);	
				yidong.waitStatus();
				String result = yidong.getData().getString("msg");
				map.put("errMsg", "登录失败.");	
				if(result!=null){
					if(result.equals("1")){
						map.put("flag", "1");
					}else{
						map.put("errMsg", result);	
					}
				}

			} catch (Exception e) {
				logger.error("error", e);
			} finally {
				if (init) {
					RobotUtil.stop();
				}
			}
		} else {
			flag = 999;
		}
		if (map.get("flag") == null) {
			map.put("flag", String.valueOf(flag));
		}
		return map;
	}
	
	
	@RequestMapping(value = "/fuwu_xz_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> fuwu_xz_yidong_vertifyLogin(HttpServletRequest req, HttpServletResponse resp, Model model,Login login) {
		FormData fd = FormData.build(req, resp, model);
		return putong_vertifyLogin(true, fd);
	}
	public Map<String, String> putong_vertifyLogin(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		String authCode = fd.getAuthCode();
		String fwpassword = fd.getPassword();
		String phone = fd.getPhoneNo();
		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				//mp.put("flag", "false");
				final XZYidong yidong = new XZYidong(spider, null, phone, fwpassword, authCode, util);
				yidong.vertifyLogin();
				SpiderManager.getInstance().startSpider(spider, null, yidong);	
				yidong.waitStatus();
				setResult(yidong, map);	
			} catch (Exception e) {
				logger.error("error", e);
			} finally {
				if (init) {
					RobotUtil.stop();
				}
			}
		} else {
			flag = 999;
		}
		map.put("flag1", "true");	
		if (map.get("flag") == null) {
			map.put("flag", String.valueOf(flag));
		}
		return map;
	}
	
	@RequestMapping(value="/putong_xz_yidong_GetAuth",method=RequestMethod.POST)
	public @ResponseBody Map<String, String> getAuth(HttpServletRequest req, HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return getAuth(true, fd);
	}
	public Map<String, String> getAuth(boolean init, FormData fd) {
		String currentUser = fd.getUserId();
		String phone = fd.getPhoneNo();
		WarningUtil util = new WarningUtil();
		util.setContext(warningService, currentUser);
		Map<String,String> map=new HashMap<String,String>();
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				final XZYidong yd = new XZYidong(spider, util);
				yd.checkVerifyCode(phone);
				SpiderManager.getInstance().startSpider(spider, null, yd);	
				yd.waitStatus();
				if ("1".equals(yd.getData().getString("checkVerifyCode"))) {
					map.put("url", yd.getData().getString("imgName"));
				} else {
					map.put("url", "none");
				}
			} catch (Exception e) {
				logger.error("error", e);
			} finally {
				if (init) {
					RobotUtil.stop();
				}
			}
		}
		return map;
	}
}
