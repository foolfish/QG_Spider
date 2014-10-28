package com.lkb.controller.yd;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.Login;
import com.lkb.controller.Mobile_Controller;
import com.lkb.thirdUtil.yd.HUBYidong;
import com.lkb.thirdUtil.yd.HuNanYidong;
/**
 * 湖南移动
 * @author jzr
 *
 */
@Controller
public class HuNanYIDONG_Controller extends Mobile_Controller{
	@RequestMapping(value = "/hun_yidong_sendCard", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getmsg(HttpServletRequest request,Login login) {
		return sendPhoneDynamicsCode(new HuNanYidong(login,getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/dongtai_hun_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> dongtai_vertifyLogin(HttpServletRequest request,Login login) {
		return checkPhoneDynamicsCode(new HuNanYidong(login,getCurrentUser(request)));
	}
	
	@RequestMapping(value = "/fuwu_hun_yidong_vertifyLogin", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> putong_vertifyLogin(HttpServletRequest request,Login login) {
		return login(new HuNanYidong(login,getCurrentUser(request)));
	}

	@RequestMapping(value = "/putonghunydFirst", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> putongFirst(HttpServletRequest request,Login login) {
		return putongFirst(new HuNanYidong(login,getCurrentUser(request)));
	}
}
