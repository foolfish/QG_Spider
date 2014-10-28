package com.lkb.location;

import java.util.Map;

import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.CommonConstant;
import com.lkb.controller.dx.XiZangDianXin_Controller;
import com.lkb.controller.yd.XZYIDONG_Controller;
/*西藏*/
public class XZ_Base  extends AbstractBase{

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_xz_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		} else if (ptype.contains("电信")) {
			
		} else {

		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			XZYIDONG_Controller controller = (XZYIDONG_Controller) getBean(XZYIDONG_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map map = controller.getAuth(false, fd);
			map.put(CommonConstant.status, 1);
			setResultOld(r, map);
			
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			XiZangDianXin_Controller controller = (XiZangDianXin_Controller) getBean(XiZangDianXin_Controller.class);
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
			XZYIDONG_Controller controller = (XZYIDONG_Controller) getBean(XZYIDONG_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
			//未完成流程，走下一步，短信码
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			XiZangDianXin_Controller controller = (XiZangDianXin_Controller) getBean(XiZangDianXin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			XZYIDONG_Controller controller = (XZYIDONG_Controller) getBean(XZYIDONG_Controller.class);
			Map map = controller.getSms(false, fd);
			setResult(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			XZYIDONG_Controller controller = (XZYIDONG_Controller) getBean(XZYIDONG_Controller.class);
			Map map = controller.dongtai_vertifyLogin(false, fd);
			setResult(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			
		}
		return r;
	}
}
