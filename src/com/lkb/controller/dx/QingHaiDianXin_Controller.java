package com.lkb.controller.dx;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.User;
import com.lkb.bean.req.FormData;
import com.lkb.constant.Constant;
import com.lkb.controller.telcom.AbstractDianXinController;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.util.RobotUtil;
import com.lkb.thirdUtil.dx.JiangXiDianXin;
import com.lkb.thirdUtil.dx.QingHaiDianXin;
import com.lkb.warning.WarningUtil;


/**
* <p>Title: QingHaiDianXin_Controller</p>
* <p>Description: 青海电信Controller</p>
* <p>Company: QuantGroup</p> 
* @author Jerry Sun
* @date 2014-9-15
*/
@Controller
public class QingHaiDianXin_Controller extends AbstractDianXinController {
	private static final String TYPE = "QingHaiDianXin_Controller";
	
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
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				final User user = getUser(currentUser, phone, Constant.DIANXIN);

				final QingHaiDianXin dx = new QingHaiDianXin(spider, user, phone, fwpassword, authCode, util);
				//登录，并抓到内容（客户名称、余额、帐单、通话记录），可以跳过短信验证
				dx.goLoginReq();
				SpiderManager.getInstance().startSpider(spider, saveSpiderListener(dx, user, currentUser, 3), dx);	
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
		//1.做一些初始化 TYPE：类型值，保证唯一，比如JiangXiDianXin_Controller,简单可以直接用类名  userName：手机号
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				//2.创建spider，用于管理request的执行
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);

				//3.创建业务流程处理类
				final JiangXiDianXin dx = new JiangXiDianXin(spider, util);
				//4.检查是否需要验证码，如要，需返回url，以便页面访问
				dx.checkVerifyCode(phone);
				//5.启动spider，开始按顺序执行request
				SpiderManager.getInstance().startSpider(spider, null, dx);	
				//6.等待状态的返回，因为spider可能要执行比较长的时间，页面需要知道执行的情况，比如登录成功后，可以返回告知用户，但spider还在抓取其它内容（客户名称、余额等）
				dx.waitStatus();
				//7.返回页面的内容，业务流程处理类有status和data字段可以用来返回数据，但不能用于集群
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
