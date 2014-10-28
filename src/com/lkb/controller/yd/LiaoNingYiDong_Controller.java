package com.lkb.controller.yd;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.User;
import com.lkb.bean.req.FormData;
import com.lkb.constant.Constant;
import com.lkb.controller.telcom.AbstractYiDongController;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.util.RobotUtil;
import com.lkb.thirdUtil.yd.LiaoNingYiDong;
import com.lkb.warning.WarningUtil;

/**
 * @author think
 * @date 2014-8-16
 */
@Controller
public class LiaoNingYiDong_Controller extends AbstractYiDongController {

	private static final String TYPE = "LiaoNingYiDong_Controller";

	@RequestMapping(value = "/putong_ln_yidong_GetSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> getSms(HttpServletRequest req, HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return getSms(true, fd);
	}
	public Map<String, String> getSms(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		String phone = fd.getPhoneNo();

		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				final LiaoNingYiDong dx = new LiaoNingYiDong(spider, null, phone, null, null, util);
				dx.sendSmsPasswordForRequireCallLogService(2);
				SpiderManager.getInstance().startSpider(spider, null, dx);	
				dx.waitStatus();
				setResult(dx, map);
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
	@RequestMapping(value = "/dongtai_ln_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> dongtai_vertifyLogin(HttpServletRequest req, HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return dongtai_vertifyLogin(true, fd);
	}
	public Map<String, String> dongtai_vertifyLogin(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		String phone = fd.getPhoneNo();

		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				final User user = getUser(currentUser, phone, Constant.YIDONG);

				final LiaoNingYiDong dx = new LiaoNingYiDong(spider, user, phone, null, null, util);
				dx.requestAllService(fd.getSmsCode());
				SpiderManager.getInstance().startSpider(spider, saveSpiderListener(dx, user, currentUser), dx);	
				dx.waitStatus();
				setResult(dx, map);			
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
	@RequestMapping(value = "/putong_ln_yidong_vertifyLogin", method = RequestMethod.POST)
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
		if (false && StringUtils.isBlank(authCode)) {
			map.put("flag", "0");
			map.put("errMsg", "请输入图片验证码！");
		} else {
			int  flag = 0;
			if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
				try {
					WarningUtil util = new WarningUtil();
					util.setContext(warningService, currentUser);
					Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
					final LiaoNingYiDong dx = new LiaoNingYiDong(spider, null, phone, fwpassword, authCode, util);
					dx.goLoginReq();
					//dx.sendSmsPasswordForRequireCallLogService();
					SpiderManager.getInstance().startSpider(spider, null, dx);	
					dx.waitStatus();

					setResult(dx, map);
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
		}
		return map;
	}

	@RequestMapping(value="/putong_ln_yidong_GetAuth",method=RequestMethod.POST)
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
				//spider.getSite().setCharset("gbk");

				final LiaoNingYiDong dx = new LiaoNingYiDong(spider, util);

				dx.checkVerifyCode(phone);
				SpiderManager.getInstance().startSpider(spider, null, dx);	
				dx.waitStatus();

				if ("1".equals(dx.getData().getString("checkVerifyCode"))) {
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
