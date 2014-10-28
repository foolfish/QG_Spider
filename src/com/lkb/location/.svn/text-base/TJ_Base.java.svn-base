package com.lkb.location;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.TJDianXin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.GDYIDONG_Controller;
import com.lkb.controller.yd.TJYIDONG_Controller;
public class TJ_Base extends AbstractBase {
	
	private static Logger logger = Logger.getLogger(TJ_Base.class);
	public void goWhere(String ptype,String currentUser,Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_tj_yidong_url", "none");
		
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		}else if (ptype.contains("电信")) {
			model.addAttribute("putong_tj_dianxin_url", "none");
		} else {
			
		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			TJYIDONG_Controller controller = (TJYIDONG_Controller) getBean(TJYIDONG_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.tjyidong);
			 setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.liantong);
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			TJDianXin_Controller controller = (TJDianXin_Controller)getBean(TJDianXin_Controller.class);
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.tjdianxin);
			setResultOld(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			TJYIDONG_Controller controller = (TJYIDONG_Controller) getBean(TJYIDONG_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			TJDianXin_Controller controller = (TJDianXin_Controller)getBean(TJDianXin_Controller.class);
			Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		}
		return r;
	}
}
