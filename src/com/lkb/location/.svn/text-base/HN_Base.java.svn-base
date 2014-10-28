package com.lkb.location;

import java.util.Map;

import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.HeNanDianXin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.HENYIDONG_Controller;
/*河南*/
public class HN_Base extends AbstractBase {

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_hen_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		} else if (ptype.contains("电信")) {

		} else {

		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HENYIDONG_Controller controller = (HENYIDONG_Controller)getBean(HENYIDONG_Controller.class);
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.henyidong);
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.liantong);
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			HeNanDianXin_Controller controller = (HeNanDianXin_Controller) getBean(HeNanDianXin_Controller.class);
			Map map = controller.getAuth(false, fd);
			final String url = (String) map.get("url");
			r.setStatus(1);
			r.setImgUrl(url);
			r.setSuccess(true);
			r.setResult(true);
			r.setForgetPassUrl(ConstantForgetPassword.HeNanDianXin);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HENYIDONG_Controller controller = (HENYIDONG_Controller )getBean(HENYIDONG_Controller.class);
			Map<String,Object> map = controller.putong_vertifyLogin(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			//流程结束，还有下一步
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			HeNanDianXin_Controller controller = (HeNanDianXin_Controller) getBean(HeNanDianXin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
			r.setResult(false);
		}
		return r;
	}
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HENYIDONG_Controller controller = (HENYIDONG_Controller)getBean(HENYIDONG_Controller.class);
			Map<String,Object> map = controller.getmsg(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HeNanDianXin_Controller controller = (HeNanDianXin_Controller) getBean(HeNanDianXin_Controller.class);
			Map map = controller.sendSms(false, fd);
			setResult(r, map);
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HENYIDONG_Controller controller = (HENYIDONG_Controller )getBean(HENYIDONG_Controller.class);
			Map<String,Object> map = controller.dongtai_vertifyLogin(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HeNanDianXin_Controller controller = (HeNanDianXin_Controller) getBean(HeNanDianXin_Controller.class);
			Map map = controller.reqService(false, fd);
			setResult(r, map);
		}
		return r;
	}
}
