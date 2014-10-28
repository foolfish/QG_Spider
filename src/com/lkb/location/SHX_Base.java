package com.lkb.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.constant.DxConstant;
import com.lkb.constant.LtConstant;
import com.lkb.constant.YdConstant;
import com.lkb.controller.dx.SDDianXin_Controller;
import com.lkb.controller.dx.SHXDianxin_Controller;
import com.lkb.controller.dx.ShangHaiDianxin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.SHXYIDONG_Controller;
import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.constant.ConstantHC;

/*山西*/
public class SHX_Base extends AbstractBase {

	private static Logger logger = Logger.getLogger(SHX_Base.class);

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_shx_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");

		} else if (ptype.contains("电信")) {
			model.addAttribute("putong_shx_dianxin_url", "none");
		} else {

		}
	}

	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHXYIDONG_Controller controller = (SHXYIDONG_Controller) getBean(SHXYIDONG_Controller.class);
			// 在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String, Object> map = controller.putongFirst(fd.getRequest(),
					controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.shxyidong);
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			// 在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String, Object> map = controller.putongFirst(fd.getRequest(),
					controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.liantong);
			setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			SHXDianxin_Controller controller = (SHXDianxin_Controller) getBean(SHXDianxin_Controller.class);
			Map map = controller.getAuth(false, fd);
			setImgResult(r, map);
			r.setForgetPassUrl(ConstantForgetPassword.shxdianxin);
		}
		return r;
	}

	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHXYIDONG_Controller controller = (SHXYIDONG_Controller) getBean(SHXYIDONG_Controller.class);
			Map<String, Object> map = controller.login(fd.getRequest(),
					controller.getLogin(fd));
			setResultOld(r, map);
			// System.out.println(map.get("status")+"  &&  "+map.get("state"));
			if ((Integer) (map.get(CommonConstant.status)) == 1) {
				r.setImgUrl("getImg");
			}
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			Map<String, Object> map = controller.login(fd.getRequest(),
					controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			r = setImgCodeResult(fd, r);
			if (r.getErrorcode() == 0) {
				SHXDianxin_Controller controller = (SHXDianxin_Controller) getBean(SHXDianxin_Controller.class);
				Map map = controller.putong_vertifyLogin(false, fd);
				r.setResult(true);
				setResult(r, map);
			}
			// r.setResult(false);
			/** 由于跳过了短信验证直接抓取详单，不执行短信后面几步 **/
			// if (r.getStatus() == 1) {
			// r.setImgUrl("getImg");
			// }
		}
		return r;
	}

	public Result showImg2(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHXYIDONG_Controller controller = (SHXYIDONG_Controller) getBean(SHXYIDONG_Controller.class);
			// 在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String, Object> map = controller.getSecImgUrl(fd.getRequest(),
					controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {

		} else if (isDianXin(phoneNum)) {
			// SHXDianxin_Controller controller = (SHXDianxin_Controller)
			// getBean(SHXDianxin_Controller.class);
			// Map map = controller.showImgWhenSendSms(false, fd);
			// final String url = (String) map.get("url");
			// r.setStatus(1);
			// r.setImgUrl(url);
			// r.setSuccess(true);
			// r.setResult(true);
		}
		return r;
	}

	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHXYIDONG_Controller controller = (SHXYIDONG_Controller) getBean(SHXYIDONG_Controller.class);
			Map<String, Object> map = controller.sendPhoneDynamicsCode(
					fd.getRequest(), controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {

		} else if (isDianXin(phoneNum)) {
			// SHXDianxin_Controller controller = (SHXDianxin_Controller)
			// getBean(SHXDianxin_Controller.class);
			// Map map = controller.sendSms(false, fd);
			// setResult(r, map);
		}
		return r;
	}

	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			SHXYIDONG_Controller controller = (SHXYIDONG_Controller) getBean(SHXYIDONG_Controller.class);
			Map<String, Object> map = controller.checkDynamicsCode(
					fd.getRequest(), controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {

		} else if (isDianXin(phoneNum)) {
			// SHXDianxin_Controller controller = (SHXDianxin_Controller)
			// getBean(SHXDianxin_Controller.class);
			// Map map = controller.reqService(false, fd);
			// setResult(r, map);
		}
		return r;
	}
}
