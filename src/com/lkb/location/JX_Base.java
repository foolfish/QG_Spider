package com.lkb.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
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
import com.lkb.constant.YdConstant;
import com.lkb.controller.dx.AnHuiDianXin_Controller;
import com.lkb.controller.dx.JiangXiDianXin_Controller;
import com.lkb.controller.dx.SCDianXin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.JXYIDONG_Controller;
import com.lkb.controller.yd.SCYIDONG_Controller;
import com.lkb.controller.yd.SDYIDONG_Controller;
import com.lkb.util.InfoUtil;
/*江西*/
public class JX_Base extends AbstractBase {
	
	private static Logger logger = Logger.getLogger(JX_Base.class);
	public void goWhere(String ptype,String currentUser,Model model) {

		if (ptype.contains("移动")) {
			
			model.addAttribute("putong_jx_yidong_url", "none");
		} else if (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		} else {
			
		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			
			JXYIDONG_Controller controller = (JXYIDONG_Controller) getBean(JXYIDONG_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.jxyidong);
			 setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.putongFirst(fd.getRequest(),controller.getLogin(fd));
			 r.setForgetPassUrl(ConstantForgetPassword.liantong);
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			JiangXiDianXin_Controller controller = (JiangXiDianXin_Controller) getBean(JiangXiDianXin_Controller.class);
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
			JXYIDONG_Controller controller = (JXYIDONG_Controller) getBean(JXYIDONG_Controller.class);
			Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
			//流程结束，还有下一步
			if((Integer)map.get("status")==1){
				r.setImgUrl("getImg");	
			}
			r.setResult(false);
		} else if (isLianTong(phoneNum)) {
			SHLianTong_Controller controller = (SHLianTong_Controller) getBean(SHLianTong_Controller.class);
			 Map<String,Object> map = controller.login(fd.getRequest(),controller.getLogin(fd));
			 setResultOld(r, map);
		} else if (isDianXin(phoneNum)) {
			JiangXiDianXin_Controller controller = (JiangXiDianXin_Controller) getBean(JiangXiDianXin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}
	
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			JXYIDONG_Controller controller = (JXYIDONG_Controller) getBean(JXYIDONG_Controller.class);
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
			JXYIDONG_Controller controller = (JXYIDONG_Controller) getBean(JXYIDONG_Controller.class);
			 Map<String,Object> map = controller.checkDynamicsCode(fd.getRequest(),controller.getLogin(fd));
		    setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
		
		}
		return r;
	}
	
	public Result showImg2(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			JXYIDONG_Controller controller = (JXYIDONG_Controller) getBean(JXYIDONG_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map<String,Object> map = controller.getSecImgUrl(fd.getRequest(),controller.getLogin(fd));
			setResultOld(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
		
		}
		return r;
	}
}
