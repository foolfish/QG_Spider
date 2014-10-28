package com.lkb.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ConstantForgetPassword;
import com.lkb.constant.DxConstant;
import com.lkb.constant.LtConstant;
import com.lkb.controller.dx.CQDianXin_Controller;
import com.lkb.controller.dx.FJDianXin_Controller;
import com.lkb.controller.dx.TJDianXin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.CQYIDONG_Controller;
import com.lkb.controller.yd.FJYIDONG_Controller;
import com.lkb.controller.yd.ZJYIDONG_Controller;
import com.lkb.util.InfoUtil;
/*重庆*/
public class CQ_Base extends AbstractBase {

	private static Logger logger = Logger.getLogger(CQ_Base.class);
	public void goWhere(String ptype,String currentUser,Model model) {
		if (ptype.contains("移动")) {
			model.addAttribute("putong_cq_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");

		} else if (ptype.contains("电信")) {
			model.addAttribute("putong_cq_dianxin_url", "none");
		} 
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			CQYIDONG_Controller controller = (CQYIDONG_Controller) getBean(CQYIDONG_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.cqyidong);
			 setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.liantong);
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			CQDianXin_Controller controller = (CQDianXin_Controller)getBean(CQDianXin_Controller.class);
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.tjdianxin);
			 setResultOld(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			CQYIDONG_Controller controller = (CQYIDONG_Controller) getBean(CQYIDONG_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			//流程结束，还有下一步
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			CQDianXin_Controller controller = (CQDianXin_Controller)getBean(CQDianXin_Controller.class);
			Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		}
		return r;
	}
	
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			CQYIDONG_Controller controller = (CQYIDONG_Controller) getBean(CQYIDONG_Controller.class);
			Map<String,Object> map = controller.sendPhoneDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
		
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			CQYIDONG_Controller controller = (CQYIDONG_Controller) getBean(CQYIDONG_Controller.class);
			 Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
		
		}
		return r;
	}
}
