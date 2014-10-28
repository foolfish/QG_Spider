package com.lkb.location;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.yd.HEBYIDONG_Controller;
import com.lkb.controller.dx.HeBeiDianXin_Controller;
/*河北*/
public class HB_Base extends AbstractBase {

	private static Logger logger = Logger.getLogger(HB_Base.class);

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_heb_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");

		} else if (ptype.contains("电信")) {
			model.addAttribute("putong_hebei_dianxin_url", "none");
		} else {

		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HEBYIDONG_Controller controller = (HEBYIDONG_Controller)getBean(HEBYIDONG_Controller.class);
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),fd.getResponse(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.hebyidong);
			 setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HeBeiDianXin_Controller controller = (HeBeiDianXin_Controller) getBean(HeBeiDianXin_Controller.class);
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
			HEBYIDONG_Controller controller = (HEBYIDONG_Controller)getBean(HEBYIDONG_Controller.class);
			Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
			//流程结束，还有下一步
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			HeBeiDianXin_Controller controller = (HeBeiDianXin_Controller) getBean(HeBeiDianXin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
			//流程结束，还有下一步
			r.setResult(false);
		}
		return r;
	}
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HEBYIDONG_Controller controller = (HEBYIDONG_Controller)getBean(HEBYIDONG_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HeBeiDianXin_Controller controller = (HeBeiDianXin_Controller) getBean(HeBeiDianXin_Controller.class);
			Map map = controller.getSms(false, fd);
			setResult(r, map);
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HEBYIDONG_Controller controller = (HEBYIDONG_Controller)getBean(HEBYIDONG_Controller.class);
			Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HeBeiDianXin_Controller controller = (HeBeiDianXin_Controller) getBean(HeBeiDianXin_Controller.class);
			Map map = controller.dongtai_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}

	
	
	
}
