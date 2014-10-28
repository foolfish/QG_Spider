/**
 * 
 */
package com.lkb.controller.telcom;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.lkb.bean.Parse;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.robot.RedisSpiderListener;
import com.lkb.robot.util.RobotUtil;
import com.lkb.service.IParseService;
import com.lkb.service.ITelcomMessageService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.AbstractCrawler;
import com.lkb.thirdUtil.StatusTracker;
import com.lkb.thirdUtil.dx.AbstractDianXinCrawler;
import com.lkb.util.SessionUtil;

/**
 * @author think
 * @date 2014-8-16
 */
public class AbstractTelcomController {
	@Resource
	protected IUserService userService;
	@Resource
	protected IParseService parseService;
	@Resource
	protected IWarningService warningService;
	protected Logger logger = Logger.getLogger(getClass());
	protected void setResult(AbstractCrawler dx, Map map) {
		int stat = dx.getStatus();
		int flag = 0;
		if (StatusTracker.STAT_STOPPED_FAIL == stat) {
			flag = -1;
		} else if (isSuccess(dx)) {
			flag = 1;	
		}
		map.put("flag", String.valueOf(flag));
		if (dx.getData().get("errMsg") != null) {
			map.put("errMsg", dx.getData().getString("errMsg"));
		}
	}
	protected String getBusinessKey() {
		return SessionUtil.getText(SessionUtil.CURRENT_BUSINESS_KEY);
	}
	protected boolean isSuccessLogin() {
		String businessKey = getBusinessKey();
		Map map = RobotUtil.getCacheMap(null, businessKey);
		if (map != null) {
			/*Boolean b = (Boolean) map.get(RedisSpiderListener.KEY_CACHE_IS_LOGIN);
			if (b != null && b) {
				return true;
			}*/
		}
		return false;
	}
	protected boolean isSuccess(StatusTracker dx) {
		return dx.isSuccess();
	}
	protected User getUser(final String currentUser, String phone, String source) {
		//因为会真正抓取用户的数据，需要获取是否存在user对象
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("parentId", currentUser);
		map1.put("loginName", phone);			
		map1.put("usersource", source);
		List<User> list = userService.getUserByParentIdSource(map1);
		final User user;
		if (list != null && list.size() > 0) {
			user = list.get(0);
		} else {
			user = new User();
			user.setPhone(phone);
			user.setLoginName(phone);
			user.setParentId(currentUser);
			user.setUsersource(source);
		}
		return user;
	}
	
	protected void saveUser(User user, User u, String currentUser) {
		logger.info("saveData user info ....");
		try {
			if (user == null || user.getId() == null) {
				user = u;
				UUID uuid = UUID.randomUUID();	
				user.setId(uuid.toString());
				user.setModifyDate(new Date());
				user.setParentId(currentUser);
				userService.saveUser(user);
			} else {
				user.setModifyDate(new Date());
				userService.update(user);
			}
		} catch (Exception e) {
			logger.error("saveUser Error", e);
		}
		
	}	
	protected void saveOrUpdateParse(final User user, final String currentUser, int status) {
		try {
			Map<String,String> map = new HashMap<String,String>();
			map.put("userId", currentUser);
			map.put("loginName", user.getLoginName());
			map.put("usersource", user.getUsersource());
			List<Parse> list = parseService.getParseBySome(map);
			Date date = new Date();
			if(list!=null && list.size()>0){
				Parse parse = list.get(0);
				parse.setStatus(status);
				parse.setModifyTime(date);
				parseService.update(parse);			
			} else {
				Parse parse = new Parse();
				parse.setStatus(status);
				parse.setModifyTime(date);
				parse.setUserId(currentUser);
				parse.setLoginName(user.getLoginName());
				parse.setUsersource(user.getUsersource());
				parseService.save(parse);
			}
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
}
