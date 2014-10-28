package com.lkb.location;

import java.util.Map;

import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.HaiNanDianXin_Controller;
import com.lkb.controller.yd.HAINYIDONG_Controller;

/*海南*/
public class HaiN_Base extends AbstractBase {

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_hainan_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");

		} else if (ptype.contains("电信")) {
			model.addAttribute("putong_hainan_dianxin_url", "none");
		} else {

		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HAINYIDONG_Controller controller = (HAINYIDONG_Controller) getBean(HAINYIDONG_Controller.class);
			Map<String, Object> map = controller.putongFirst(fd.getRequest(),
					controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.hainayidong);
			setResultOld(r, map);
			r.setResult(true);
			
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HaiNanDianXin_Controller controller = (HaiNanDianXin_Controller) getBean(HaiNanDianXin_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map map = controller.getAuth(false, fd);
			setImgResult(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HAINYIDONG_Controller controller = (HAINYIDONG_Controller) getBean(HAINYIDONG_Controller.class);
			Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			r.setResult(false);
			
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HaiNanDianXin_Controller controller = (HaiNanDianXin_Controller) getBean(HaiNanDianXin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HAINYIDONG_Controller controller = (HAINYIDONG_Controller) getBean(HAINYIDONG_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HAINYIDONG_Controller controller = (HAINYIDONG_Controller) getBean(HAINYIDONG_Controller.class);
			Map<String, Object> map = controller.checkDynamicsCode(
					fd.getRequest(), controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {

		} else if (isDianXin(phoneNum)) {

		}
		return r;
	}
}
