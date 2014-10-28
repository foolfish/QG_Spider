package com.lkb.controller.sb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.client.SocialInsuranceInput;
import com.lkb.bean.client.SocialInsuranceOut;
import com.lkb.bean.client.SocialInsuranceProperty;
import com.lkb.constant.ErrorMsg;
import com.lkb.thirdUtil.base.BaseInfoSocialInsurance;



/**
 * @author fastw
 * @date	2014-9-4 下午5:18:45
 */
@Controller
@RequestMapping(value = "/shebao")
public class OpenSocialInsurance_Controller extends SocialInsurance_Controller{
	
	@RequestMapping(value="/verifyLogin")//,method=RequestMethod.POST)
	public @ResponseBody SocialInsuranceOut vertifyLogin(HttpServletRequest req,
			HttpServletResponse resp,SocialInsuranceInput sii) {
		sii.setCurrentUser(getCurrentUser(req));
		SocialInsuranceOut sio = new SocialInsuranceOut();
		int errorCode = sii.checkProvinAndCity();
		if(errorCode == ErrorMsg.normal){
			BaseInfoSocialInsurance	baseinfo = SocialInsuranceProperty.getBaseInfoSocialInsurance(sii);
			if(baseinfo!=null){
				errorCode = baseinfo.getRequestPrarms(sii);
				if(errorCode == ErrorMsg.normal){
					errorCode = baseinfo.setSii(sii,true);//此步进行初始化操作,且判断验证码
					if(errorCode == ErrorMsg.normal){
						sio = vertifyLogin(baseinfo);
					}else{
						sio.setErrorcode(errorCode);
					}
				}else{
					sio.setErrorcode(errorCode);
				}
			}else{
				sio.setErrorcode(ErrorMsg.provinceOrCityEorror);
			}
		}else{
			sio.setErrorcode(errorCode);
		}
		return sio;
	}
	

	
	@RequestMapping(value = "/init")//, method = RequestMethod.POST)
	public @ResponseBody SocialInsuranceOut init(HttpServletRequest request,HttpServletResponse resp,SocialInsuranceInput sii) {
		SocialInsuranceOut sio = new SocialInsuranceOut();
		sii.setCurrentUser(getCurrentUser(request));
		int errorCode = sii.checkProvinAndCity();
		if(errorCode == ErrorMsg.normal){
			BaseInfoSocialInsurance	baseinfo = SocialInsuranceProperty.getBaseInfoSocialInsurance(sii);
			if(baseinfo!=null){
				baseinfo.setSii(sii);
				sio = init(baseinfo);
			}else{
				sio.setErrorcode(ErrorMsg.provinceOrCityEorror);
			}
		}else{
			sio.setErrorcode(errorCode);
		}
		return sio;
	}
	
	
	
}
