package com.lkb.location;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.AnHuiDianXin_Controller;
import com.lkb.controller.dx.BJDianXin_Controller;
import com.lkb.controller.yd.AHYIDONG_Controller;
import com.lkb.controller.yd.BJYIDONG_Controller;

/*安徽*/
public class AH_Base extends AbstractBase {
	
	private static Logger logger = Logger.getLogger(AH_Base.class);
	public void goWhere(String ptype, String currentUser, Model model) {
		if (ptype.contains("移动")) {
			model.addAttribute("putong_sh_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		} else if (ptype.contains("电信")) {
		} else {
		}
	}

	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			AHYIDONG_Controller controller = (AHYIDONG_Controller) getBean(AHYIDONG_Controller.class);
			Map<String, Object> map = controller.putongFirst(fd.getRequest(),
					controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.ahyidong);
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			AnHuiDianXin_Controller controller = (AnHuiDianXin_Controller) getBean(AnHuiDianXin_Controller.class);
			Map map = controller.getAuth(false, fd);
			r.setStatus(1);
			final String url = (String) map.get("url");
			r.setImgUrl(url);
			r.setSuccess(true);
			r.setResult(true);
		}
		return r;
	}

	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			AHYIDONG_Controller controller = (AHYIDONG_Controller) getBean(AHYIDONG_Controller.class);
			Map<String, Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			AnHuiDianXin_Controller controller = (AnHuiDianXin_Controller) getBean(AnHuiDianXin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}

	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			AHYIDONG_Controller controller = (AHYIDONG_Controller) getBean(AHYIDONG_Controller.class);
			Map<String, Object> map = controller.sendPhoneDynamicsCode(
					fd.getRequest(), controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {

		} else if (isDianXin(phoneNum)) {

		}
		return r;
	}

	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			AHYIDONG_Controller controller = (AHYIDONG_Controller) getBean(AHYIDONG_Controller.class);
			Map<String, Object> map = controller.checkDynamicsCode(
					fd.getRequest(), controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {

		} else if (isDianXin(phoneNum)) {

		}
		return r;
	}
}
