package com.lkb.location;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.ShangHaiDianxin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.SHYIDONG_Controller;
/*上海*/
public class SH_Base extends AbstractBase {

	public void goWhere(String ptype,String currentUser,Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_sh_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		} else {			
			model.addAttribute("putong_sh_dianxin_url", "none");
		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHYIDONG_Controller controller = (SHYIDONG_Controller) getBean(SHYIDONG_Controller.class);
			Map<String,Object> map = controller.putongFirst(fd.getRequest(), controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.shyidong+fd.getPhoneNo());
			 setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.liantong);
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			ShangHaiDianxin_Controller controller = (ShangHaiDianxin_Controller) getBean(ShangHaiDianxin_Controller.class);
			Map map = controller.getAuth(false, fd);
			final String url = (String) map.get("url");
			r.setStatus(1);
			r.setImgUrl(url);
			r.setSuccess(true);
			r.setResult(true);
			r.setForgetPassUrl(ConstantForgetPassword.shdianxin);
		}
		return r;
	}
	public Result showImg2(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			ShangHaiDianxin_Controller controller = (ShangHaiDianxin_Controller) getBean(ShangHaiDianxin_Controller.class);
			Map map = controller.showImgWhenSendSms(false, fd);
			final String url = (String) map.get("url");
			r.setStatus(1);
			r.setImgUrl(url);
			r.setSuccess(true);
			r.setResult(true);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHYIDONG_Controller controller = (SHYIDONG_Controller) getBean(SHYIDONG_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			ShangHaiDianxin_Controller controller = (ShangHaiDianxin_Controller) getBean(ShangHaiDianxin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
			r.setResult(false);
			if (r.getStatus() == 1) {
				r.setImgUrl("getImg");
			}
		}
		return r;
	}
	
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHYIDONG_Controller controller = (SHYIDONG_Controller) getBean(SHYIDONG_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			ShangHaiDianxin_Controller controller = (ShangHaiDianxin_Controller) getBean(ShangHaiDianxin_Controller.class);
			Map map = controller.sendSms(false, fd);
			setResult(r, map);
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHYIDONG_Controller controller = (SHYIDONG_Controller) getBean(SHYIDONG_Controller.class);
			Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			ShangHaiDianxin_Controller controller = (ShangHaiDianxin_Controller) getBean(ShangHaiDianxin_Controller.class);
			Map map = controller.reqService(false, fd);
			setResult(r, map);
		}
		return r;
	}
}
