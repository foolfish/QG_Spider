package com.lkb.controller.dx;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.User;
import com.lkb.bean.req.FormData;
import com.lkb.constant.Constant;
import com.lkb.controller.telcom.AbstractDianXinController;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.util.CookieStoreUtil;
import com.lkb.robot.util.RobotUtil;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.util.InfoUtil;
import com.lkb.warning.WarningUtil;


@Controller
public class ShangHaiDianxin_Controller extends AbstractDianXinController {	
	private static final String TYPE = "ShangHaiDianxin_Controller";
	//fifth
	@RequestMapping(value = "/putong_sh_dx_reqService", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> reqService(HttpServletRequest req, HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return reqService(true, fd);
	}
	public Map<String, String> reqService(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		String authCode = fd.getAuthCode();
		String fwpassword = fd.getPassword();
		String phone = fd.getPhoneNo();
		String duanxin = StringUtils.isBlank(fd.getSmsCode()) ? fd.getRequest().getParameter("dxpass") : fd.getSmsCode();
		String tupian = StringUtils.isBlank(fd.getAuthCode()) ? fd.getRequest().getParameter("dxauth") : fd.getAuthCode();
		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				final User user = getUser(currentUser, phone, Constant.DIANXIN);
				final ShangHaiDianXin dx = new ShangHaiDianXin(spider, user, phone, fwpassword, authCode, util);
				dx.requestAllService(duanxin, tupian);
				SpiderManager.getInstance().startSpider(spider, saveSpiderListener(dx, user, currentUser), dx);	
				dx.waitStatus();
				setResult(dx, map);			

			} catch (Exception e) {
				logger.error("error", e);
			} finally {
				RobotUtil.stop();
			}
		} else {
			flag = 999;
		}
		if (map.get("flag") == null) {
			map.put("flag", String.valueOf(flag));
		}
		map.put("flag1", "true");	
		return map;
	}
	//fourth
	@RequestMapping(value = "/putong_sh_dx_sms", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> sendSms(HttpServletRequest req, HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return sendSms(true, fd);
	}
	public Map<String, String> sendSms(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		String authCode = fd.getAuthCode();
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(authCode)) {
			map.put("flag", "0");
			map.put("errMsg", "请输入图片验证码！");
		} else {
			String fwpassword = fd.getPassword();
			String phone = fd.getPhoneNo();
			String authValue = StringUtils.isBlank(fd.getAuthCode()) ? fd.getRequest().getParameter("authValue") : fd.getAuthCode();
			
			int  flag = 0;
			if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
				try {
					WarningUtil util = new WarningUtil();
					util.setContext(warningService, currentUser);
					Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
					final User user = getUser(currentUser, phone, Constant.DIANXIN);
					final ShangHaiDianXin dx = new ShangHaiDianXin(spider, user, phone, fwpassword, authCode, util);
					dx.sendSmsPasswordForRequireCallLogService(authValue);
					SpiderManager.getInstance().startSpider(spider, null, dx);	
					dx.waitStatus();
					setResult(dx, map);			

				} catch (Exception e) {
					logger.error("error", e);
				} finally {
					RobotUtil.stop();
				}
			} else {
				flag = 999;
			}
			if (map.get("flag") == null) {
				map.put("flag", String.valueOf(flag));
			}
			map.put("flag1", "true");	
		}
		return map;
	}
	//third
	@RequestMapping(value = "/putong_sh_dx_smsimg", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> showImgWhenSendSms(HttpServletRequest req, HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return showImgWhenSendSms(true, fd);
	}
	public Map<String, String> showImgWhenSendSms(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		String phone = fd.getPhoneNo();
		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				final ShangHaiDianXin dx = new ShangHaiDianXin(spider, null, phone, null, null, util);
				dx.showImgWhenSendSMS(phone);
				SpiderManager.getInstance().startSpider(spider, null, dx);	
				dx.waitStatus();
				if ("1".equals(dx.getData().getString("checkVerifyCode"))) {
					//String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
					map.put("url", dx.getData().getString("imgName"));
					//map.put("url", dx.getData().getString("imgName"));
				} else {
					map.put("url", "none");
				}	
				
			} catch (Exception e) {
				logger.error("error", e);
			} finally {
				RobotUtil.stop();
			}
		} else {
			flag = 999;
		}
		if (map.get("flag") == null) {
			map.put("flag", String.valueOf(flag));
		}
		map.put("flag1", "true");	
		return map;
	}
	//second
	@RequestMapping(value = "/putong_sh_dx_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putong_vertifyLogin(HttpServletRequest req, HttpServletResponse resp, Model model) {
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
			/*if (isSuccessLogin()) {
				
			}*/
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				//final User user = getUser(currentUser, phone);
				//CookieStore cs = CookieStoreUtil.putContextToCookieStore(null, 1);
				//DebugUtil.printCookieData(cs, null);
				final ShangHaiDianXin dx = new ShangHaiDianXin(spider, null, phone, fwpassword, authCode, util);
				dx.goLoginReq();
				SpiderManager.getInstance().startSpider(spider, null, dx);	
				dx.waitStatus();
				setResult(dx, map);			

			} catch (Exception e) {
				logger.error("error", e);
			} finally {
				RobotUtil.stop();
			}
		} else {
			flag = 999;
		}
		if (map.get("flag") == null) {
			map.put("flag", String.valueOf(flag));
		}
		map.put("flag1", "true");	
		return map;
	}
	//first
	@RequestMapping(value="/putong_sh_dx_GetAuth",method=RequestMethod.POST)
	public @ResponseBody Map<String, String> getAuth(HttpServletRequest req, HttpServletResponse resp, Model model) {
		String userName = req.getParameter("userName");
		FormData fd = FormData.build(req, resp, model);
		fd.setPhoneNo(userName);
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

				final ShangHaiDianXin dx = new ShangHaiDianXin(spider, util);
				dx.checkVerifyCode(phone);
				SpiderManager.getInstance().startSpider(spider, null, dx);	
				dx.waitStatus();
				if ("1".equals(dx.getData().getString("checkVerifyCode"))) {
					//String imgPath2 = InfoUtil.getInstance().getInfo("road", "imgPath");
					map.put("url", dx.getData().getString("imgName"));
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
