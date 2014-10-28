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
import com.lkb.controller.dx.BJDianXin_Controller;
import com.lkb.controller.dx.QingHaiDianXin_Controller;
import com.lkb.controller.dx.QingHaiDianXin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.AHYIDONG_Controller;
import com.lkb.controller.yd.HAINYIDONG_Controller;
import com.lkb.controller.yd.QingHaiYidong_Controller;
import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.constant.ConstantHC;

/*青海*/
public class QH_Base extends AbstractBase {

	private static Logger logger = Logger.getLogger(QH_Base.class);

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			model.addAttribute("putong_qh_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");

		} else if (ptype.contains("电信")) {

		} else {

		}
	}

	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			QingHaiYidong_Controller controller = (QingHaiYidong_Controller) getBean(QingHaiYidong_Controller.class);
			Map<String, Object> map = controller.putongFirst(fd.getRequest(),
					controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.qhyidong);
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			// 在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String, Object> map = controller.putongFirst(fd.getRequest(),
					controller.getLogin(fd));
			r.setForgetPassUrl(ConstantForgetPassword.liantong);
			setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			QingHaiDianXin_Controller controller = (QingHaiDianXin_Controller) getBean(QingHaiDianXin_Controller.class);
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
			QingHaiYidong_Controller controller = (QingHaiYidong_Controller) getBean(QingHaiYidong_Controller.class);
			Map<String, Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
//			if((Integer)(map.get(CommonConstant.status))==1){
//				r.setImgUrl("getImg");	
//			}
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			Map<String, Object> map = controller.login(fd.getRequest(),
					controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			QingHaiDianXin_Controller controller = (QingHaiDianXin_Controller) getBean(QingHaiDianXin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}

	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			QingHaiYidong_Controller controller = (QingHaiYidong_Controller) getBean(QingHaiYidong_Controller.class);
			Map<String, Object> map = controller.sendPhoneDynamicsCode(
					fd.getRequest(), controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
		}
		return r;
	}

	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			QingHaiYidong_Controller controller = (QingHaiYidong_Controller) getBean(QingHaiYidong_Controller.class);
			Map<String, Object> map = controller.checkDynamicsCode(
					fd.getRequest(), controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
		}
		return r;
	}
}
