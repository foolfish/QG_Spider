package com.lkb.location;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.BJDianXin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.BJYIDONG_Controller;
/*北京*/
public class BJ_Base extends AbstractBase {

	private static Logger logger = Logger.getLogger(BJ_Base.class);
	public void goWhere(String ptype,String currentUser,Model model) {

		if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		}else if (ptype.contains("电信")) {
			
			model.addAttribute("putong_bj_dianxin_url", "none");
		}else if (ptype.contains("移动")) {
			model.addAttribute("putong_bj_yidong_url", "none");
		}
			
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			BJYIDONG_Controller controller = (BJYIDONG_Controller) getBean(BJYIDONG_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.bjyidong);
			 r.setPassName("网站密码");//比较特殊
			 setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.liantong);
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			BJDianXin_Controller controller = (BJDianXin_Controller) getBean(BJDianXin_Controller.class);
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.bjdianxin);
			 setResultOld(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			BJYIDONG_Controller controller = (BJYIDONG_Controller) getBean(BJYIDONG_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			    setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			BJDianXin_Controller controller = (BJDianXin_Controller) getBean(BJDianXin_Controller.class);
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
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			BJDianXin_Controller controller = (BJDianXin_Controller) getBean(BJDianXin_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			BJDianXin_Controller controller = (BJDianXin_Controller) getBean(BJDianXin_Controller.class);
			 Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		}
		return r;
	}

}
