package com.lkb.controller.dx;

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
import com.lkb.bean.req.FormData;
import com.lkb.constant.Constant;
import com.lkb.controller.telcom.AbstractDianXinController;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.util.RobotUtil;
import com.lkb.thirdUtil.dx.LiaoNingDianxin;
import com.lkb.warning.WarningUtil;

/**
 * @author think
 * @date 2014-8-16
 */
@Controller
public class LiaoNingDianxin_Controller extends AbstractDianXinController {	
	private static final String TYPE = "LiaoNingDianxin_Controller";
/*
	@RequestMapping(value = "/putong_ln_dianxin_GetSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putong_ln_dianxin_GetSms(HttpServletRequest request,HttpServletResponse resp, Model model) {
		final String currentUser = request.getSession().getAttribute("currentUser").toString();
		String phone = request.getParameter("phone");
		
		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		try {
			if (RobotUtil.init(request, TYPE, phone)) {
				try {
					WarningUtil util = new WarningUtil();
					util.setContext(warningService, currentUser);
					Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
					//spider.getSite().setCharset("gbk");
					Map<String, String> map1 = new HashMap<String, String>();
					map1.put("parentId", currentUser);
					map1.put("loginName", phone);			
					map1.put("usersource", Constant.DIANXIN);
					List<User> list = userService.getUserByParentIdSource(map1);
					final User user;
					if (list != null && list.size() > 0) {
						user = list.get(0);
					} else {
						user = new User();
						user.setPhone(phone);
						user.setLoginName(phone);
						user.setParentId(currentUser);
						user.setUsersource(Constant.dianxin);
					}
					final LNDianxin dx = new LNDianxin(spider, null, phone, null, null, util);
					//dx.sendSmsPasswordForRequireCallLogService();
					spider.addSpiderListener(new AbstractSpiderListener(Spider.buildListenerContext()) {
						public void onComplete(SimpleObject contenxt, Object obj) {
							int stat = dx.getStatus();
							if (StatusTracker.STAT_LOGIN_SUC == stat) {
								saveData(user, dx, currentUser);
								RobotUtil.stop(this.context);
							}
						}
					});
					SpiderManager.getInstance().startSpider(spider, null, dx);	
					dx.waitStatus();
					//dx.printData();
					//spider.start();
					if (dx.getData().getObject("success") != null) {
						flag=1;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {		
					RobotUtil.stop((SimpleObject)null);
				}
			} else {
				flag = 999;
			}
			map.put("flag", String.valueOf(flag));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("flag",  String.valueOf(flag));
		}
		return map;
	}
	@RequestMapping(value = "/dongtai_ln_dianxin_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> dongtai_ln_dianxin_vertifyLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		final String currentUser = request.getSession().getAttribute("currentUser").toString();
		String phone = request.getParameter("phone");
		String authCode = request.getParameter("smsCode");
		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		try {
			if (RobotUtil.init(request, TYPE, phone)) {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				//spider.getSite().setCharset("gbk");
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("parentId", currentUser);
				map1.put("loginName", phone);			
				map1.put("usersource", Constant.DIANXIN);
				List<User> list = userService.getUserByParentIdSource(map1);
				final User user;
				if (list != null && list.size() > 0) {
					user = list.get(0);
				} else {
					user = new User();
					user.setPhone(phone);
					user.setLoginName(phone);
					user.setParentId(currentUser);
					user.setUsersource(Constant.DIANXIN);
				}
				final LNDianxin dx = new LNDianxin(spider, user, phone, null, null, util);
				//dx.requestAllService(authCode);
				SpiderManager.getInstance().startSpider(spider, new AbstractSpiderListener(Spider.buildListenerContext()) {
					public void onComplete(SimpleObject contenxt, Object obj) {
						saveData(user, dx, currentUser, 1);
						saveData(null, dx, currentUser, 2);
						RobotUtil.stop(this.context);
						
					}
				}, dx);	
				dx.waitStatus();
				if (dx.getData().get("success") != null) {
					flag=1;
				}				
				
			} else {
				flag = 999;
			}
			map.put("flag", String.valueOf(flag));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			map.put("flag",  String.valueOf(flag));
		}
		return map;
	}*/
	@RequestMapping(value = "/putong_ln_dianxin_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putong_ln_dianxin_vertifyLogin(HttpServletRequest req, HttpServletResponse resp, Model model) {
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
				final User user = getUser(currentUser, phone, Constant.DIANXIN);
				final LiaoNingDianxin dx = new LiaoNingDianxin(spider, user, phone, fwpassword, authCode, util);
				dx.goLoginReq();
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

	@RequestMapping(value="/putong_ln_dianxin_GetAuth",method=RequestMethod.POST)
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
		//1.做一些初始化 TYPE：类型值，保证唯一，比如HaiNanDianXin_Controller,简单可以直接用类名  userName：手机号
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				//2.创建spider，用于管理request的执行
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);

				final LiaoNingDianxin dx = new LiaoNingDianxin(spider, util);
				dx.checkVerifyCode(phone);
				//5.启动spider，开始按顺序执行request
				SpiderManager.getInstance().startSpider(spider, null, dx);	
				//6.等待状态的返回，因为spider可能要执行比较长的时间，页面需要知道执行的情况，比如登录成功后，可以返回告知用户，但spider还在抓取其它内容（客户名称、余额等）
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
