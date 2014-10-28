package com.lkb.location;

import java.util.Map;

import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.controller.dx.ShanXiDianXin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.ShanXiYiDong_Controller;
/*陕西*/
public class SX_Base extends AbstractBase {

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_sx_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");

		} else if (ptype.contains("电信")) {
			model.addAttribute("putong_sx_dianxin_url", "none");
		} else {

		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			ShanXiYiDong_Controller controller = (ShanXiYiDong_Controller) getBean(ShanXiYiDong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map map = controller.getAuth(false, fd);
			setImgResult(r, map);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			ShanXiDianXin_Controller controller = (ShanXiDianXin_Controller) getBean(ShanXiDianXin_Controller.class);
			Map map = controller.getAuth(false, fd);
			setImgResult(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			r = setImgCodeResult(fd, r);
			if (r.getErrorcode() == 0) {
				ShanXiYiDong_Controller controller = (ShanXiYiDong_Controller) getBean(ShanXiYiDong_Controller.class);
				Map map = controller.putong_vertifyLogin(false, fd);
				setResult(r, map);
				//未完成流程，走下一步，短信码
				r.setResult(false);
			}
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			r = setImgCodeResult(fd, r);
			if (r.getErrorcode() == 0) {
				ShanXiDianXin_Controller controller = (ShanXiDianXin_Controller) getBean(ShanXiDianXin_Controller.class);
				Map map = controller.putong_vertifyLogin(false, fd);
				setResult(r, map);
				r.setResult(false);
			}
		}
		return r;
	}
	
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			ShanXiYiDong_Controller controller = (ShanXiYiDong_Controller) getBean(ShanXiYiDong_Controller.class);
			Map map = controller.getSms(false, fd);
			setResult(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			ShanXiDianXin_Controller controller = (ShanXiDianXin_Controller) getBean(ShanXiDianXin_Controller.class);
			Map map = controller.getSms(false, fd);
			setResult(r, map);
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			ShanXiYiDong_Controller controller = (ShanXiYiDong_Controller) getBean(ShanXiYiDong_Controller.class);
			Map map = controller.dongtai_vertifyLogin(false, fd);
			setResult(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			ShanXiDianXin_Controller controller = (ShanXiDianXin_Controller) getBean(ShanXiDianXin_Controller.class);
			Map map = controller.dongtai_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}
}
