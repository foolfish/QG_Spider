package com.lkb.location;

import java.util.Map;

import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.controller.dx.HeiLongJiangDianxin_Controller;
import com.lkb.controller.yd.HLJYIDONG_Controller;
/*黑龙江*/
public class HLJ_Base extends AbstractBase {

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_hlj_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		} else if (ptype.contains("电信")) {
			model.addAttribute("putong_hlj_dianxin_url", "none");
		} else {

		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HLJYIDONG_Controller controller = (HLJYIDONG_Controller )getBean(HLJYIDONG_Controller.class);
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.hljyidong);
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			HeiLongJiangDianxin_Controller controller = (HeiLongJiangDianxin_Controller) getBean(HeiLongJiangDianxin_Controller.class);
			Map map = controller.getAuth(false, fd);
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
			HLJYIDONG_Controller controller = (HLJYIDONG_Controller )getBean(HLJYIDONG_Controller.class);
			Map<String,Object> map = controller.putong_vertifyLogin(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			HeiLongJiangDianxin_Controller controller = (HeiLongJiangDianxin_Controller) getBean(HeiLongJiangDianxin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}
	
	/* （非 Javadoc）
	* <p>Title: sendSMS</p>
	* <p>Description: 发送手机验证码</p>
	* @author Jerry Sun
	* @param phoneNum
	* @param fd
	* @return
	* @see com.lkb.location.AbstractBase#sendSMS(com.lkb.bean.PhoneNum, com.lkb.bean.req.FormData)
	*/
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HLJYIDONG_Controller controller = (HLJYIDONG_Controller) getBean(HLJYIDONG_Controller.class);
			Map<String,Object> map = controller.sendMessage(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			
		}
		return r;
	}
	/* （非 Javadoc）
	* <p>Title: requireService</p>
	* <p>Description: 手机验证码校验</p>
	* @author Jerry Sun
	* @param phoneNum
	* @param fd
	* @return
	* @see com.lkb.location.AbstractBase#requireService(com.lkb.bean.PhoneNum, com.lkb.bean.req.FormData)
	*/
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			HLJYIDONG_Controller controller = (HLJYIDONG_Controller) getBean(HLJYIDONG_Controller.class);
			 Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			
		}
		return r;
	}
}
