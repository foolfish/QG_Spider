package com.lkb.location;

import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;

import com.lkb.controller.dx.SDDianXin_Controller;

import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.SDYIDONG_Controller;

/*山东*/
public class SD_Base extends AbstractBase {

	private static Logger logger = Logger.getLogger(SD_Base.class);

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_sd_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");

		} else if (ptype.contains("电信")) {
			
			model.addAttribute("putong_sd_dianxin_url", "none");
		} else {

		}
	}
	
	
	public Result showImg2(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SDYIDONG_Controller controller = (SDYIDONG_Controller) getBean(SDYIDONG_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.getSecImgUrl(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
		
		}
		return r;
	}
	
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SDYIDONG_Controller controller = (SDYIDONG_Controller) getBean(SDYIDONG_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.sdyidong);
			 setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.liantong);
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			SDDianXin_Controller controller = (SDDianXin_Controller) getBean(SDDianXin_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.sddianxin);
			 setResultOld(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SDYIDONG_Controller controller = (SDYIDONG_Controller) getBean(SDYIDONG_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			//流程结束，还有下一步
			//System.out.println(map.get("status")+"  &&  "+map.get("state"));
			if((Integer)map.get("status")==1){
				r.setImgUrl("getImg");	
			}
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			SDDianXin_Controller controller = (SDDianXin_Controller) getBean(SDDianXin_Controller.class);
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
			SDYIDONG_Controller controller = (SDYIDONG_Controller) getBean(SDYIDONG_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			SDDianXin_Controller controller = (SDDianXin_Controller) getBean(SDDianXin_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SDYIDONG_Controller controller = (SDYIDONG_Controller) getBean(SDYIDONG_Controller.class);
			 Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			SDDianXin_Controller controller = (SDDianXin_Controller) getBean(SDDianXin_Controller.class);
			 Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		}
		return r;
	}
}
