package com.lkb.location;

import java.util.Map;

import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.HuNanDianXin_Controller;
import com.lkb.controller.yd.HuNanYIDONG_Controller;
/*湖南*/
public class HuN_Base extends AbstractBase {

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {

		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");

		} else if (ptype.contains("电信")) {
			model.addAttribute("putong_hunan_dianxin_url", "none");
		} else {

		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HuNanYIDONG_Controller controller = (HuNanYIDONG_Controller)getBean(HuNanYIDONG_Controller.class);
			Map<String, Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.hunyidong);
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			HuNanDianXin_Controller controller = (HuNanDianXin_Controller) getBean(HuNanDianXin_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,String> map = controller.getAuth(false, fd);
			setImgResult(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HuNanYIDONG_Controller controller = (HuNanYIDONG_Controller)getBean(HuNanYIDONG_Controller.class);
			Map<String, Object> map = controller.putong_vertifyLogin(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			HuNanDianXin_Controller controller = (HuNanDianXin_Controller) getBean(HuNanDianXin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
			//未完成流程，走下一步，短信码
			r.setResult(false);
		}
		return r;
	}
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HuNanDianXin_Controller controller = (HuNanDianXin_Controller) getBean(HuNanDianXin_Controller.class);
			Map map = controller.getSms(false, fd);
			setResult(r, map);
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			HuNanDianXin_Controller controller = (HuNanDianXin_Controller) getBean(HuNanDianXin_Controller.class);
			Map map = controller.dongtai_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}
}
