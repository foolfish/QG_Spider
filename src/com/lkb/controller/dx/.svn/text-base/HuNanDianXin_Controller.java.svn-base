package com.lkb.controller.dx;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.Cells;
import com.lkb.bean.User;
import com.lkb.bean.req.FormData;
import com.lkb.constant.Constant;
import com.lkb.controller.telcom.AbstractDianXinController;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.util.RobotUtil;
import com.lkb.service.ICellService;
import com.lkb.thirdUtil.dx.HuNanDianXin;
import com.lkb.warning.WarningUtil;

/**
 * @author think
 * @date 2014-8-16
 */
@Controller
public class HuNanDianXin_Controller extends AbstractDianXinController {

	private static final String TYPE = "HuNanDianXin_Controller";
	@Resource
	protected ICellService cellService;
	
	@RequestMapping(value = "/hr/req")
	public @ResponseBody Map<String, String> saveReq2(HttpServletRequest req, HttpServletResponse resp, Model model) {
		Map<String, String> map = new HashMap<String, String>();
		Cells cells = new Cells();
		String name = req.getParameter("name");
		String phone = req.getParameter("new1");
		String school = req.getParameter("new2");
		String subject = req.getParameter("new3");
		if(phone!=null&&phone.trim().length()>0){
			if(cellService.findByBaby(phone)==null){
				//cells.set
				UUID uuid = UUID.randomUUID();	
				cells.setPhone(uuid.toString());
				cells.setcTime(new Date());
				cells.setLable(name);
				cells.setBaby(phone);
				cells.setXq(school);
				cells.setIncome(subject);
				cellService.saveCell(cells);
			}
		}		
		//logger.info(name + phone + school + subject);
		return map;
	}
	@RequestMapping(value = "/putong_hunan_dianxin_GetSms", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putong_hunan_dianxin_GetSms(HttpServletRequest req, HttpServletResponse resp, Model model) {
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

				final HuNanDianXin dx = new HuNanDianXin(spider, null, phone, null, null, util);
				dx.sendSmsPasswordForRequireCallLogService();
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
	@RequestMapping(value = "/dongtai_hunan_dianxin_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> dongtai_hunan_dianxin_vertifyLogin(HttpServletRequest req, HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(req, resp, model);
		return dongtai_vertifyLogin(true, fd);
	}
	public Map<String, String> dongtai_vertifyLogin(boolean init, FormData fd) { 
		final String currentUser = fd.getUserId();
		String authCode = fd.getSmsCode();
		String phone = fd.getPhoneNo();

		Map<String, String> map = new HashMap<String, String>();
		int  flag = 0;
		if (!init || RobotUtil.init(fd.getRequest(), TYPE, phone)) {
			try {
				WarningUtil util = new WarningUtil();
				util.setContext(warningService, currentUser);
				Spider spider = SpiderManager.getInstance().createSpider(currentUser, TYPE);
				final User user = getUser(currentUser, phone, Constant.DIANXIN);
				final HuNanDianXin dx = new HuNanDianXin(spider, user, phone, null, authCode, util);
				dx.requestAllService();
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
	@RequestMapping(value = "/putong_hunan_dianxin_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> putong_hunan_dianxin_vertifyLogin(HttpServletRequest req, HttpServletResponse resp, Model model) {
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

				final HuNanDianXin dx = new HuNanDianXin(spider, null, phone, fwpassword, authCode, util);
				dx.goLoginReq();
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
		return map;
	}


	@RequestMapping(value="/putong_hunan_dianxin_GetAuth",method=RequestMethod.POST)
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
				final HuNanDianXin dx = new HuNanDianXin(spider, util);
				dx.checkVerifyCode(phone);
				SpiderManager.getInstance().startSpider(spider, null, dx);	
				dx.waitStatus();
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
