package com.lkb.location;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.NeiMengGuDianXin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.NMGYIDONG_Controller;
/*内蒙古*/
public class NMG_Base  extends AbstractBase{
	
	private static Logger logger = Logger.getLogger(NMG_Base.class);
	public void goWhere(String ptype,String currentUser,Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_nmg_yidong_url", "none");			
		} else if  (ptype.contains("联通")) {
			
			model.addAttribute("putong_sh_liantong_url", "none");
		}else if (ptype.contains("电信")) {
			
			model.addAttribute("putong_ln_dianxin_url", "none");
		} else {
			
		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			NMGYIDONG_Controller controller = (NMGYIDONG_Controller) getBean(NMGYIDONG_Controller.class);
			Map<String, Object> map = controller.putongFirst(fd.getRequest(),
					controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.nmgyidong);
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.liantong);
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			NeiMengGuDianXin_Controller controller = (NeiMengGuDianXin_Controller) getBean(NeiMengGuDianXin_Controller.class);
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.bjdianxin);
			 setResultOld(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			NMGYIDONG_Controller controller = (NMGYIDONG_Controller) getBean(NMGYIDONG_Controller.class);
			Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			NeiMengGuDianXin_Controller controller = (NeiMengGuDianXin_Controller) getBean(NeiMengGuDianXin_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
			//流程结束，还有下一步
			r.setResult(false);
		}
		return r;
	}
	
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			NMGYIDONG_Controller controller = (NMGYIDONG_Controller) getBean(NMGYIDONG_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			NeiMengGuDianXin_Controller controller = (NeiMengGuDianXin_Controller) getBean(NeiMengGuDianXin_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		}
		return r;
	}
	

	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			NMGYIDONG_Controller controller = (NMGYIDONG_Controller) getBean(NMGYIDONG_Controller.class);
			Map<String, Object> map = controller.checkDynamicsCode(
			fd.getRequest(), controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {

		} else if (isDianXin(phoneNum)) {
			NeiMengGuDianXin_Controller controller = (NeiMengGuDianXin_Controller) getBean(NeiMengGuDianXin_Controller.class);
			 Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		}
		return r;
	}
}
