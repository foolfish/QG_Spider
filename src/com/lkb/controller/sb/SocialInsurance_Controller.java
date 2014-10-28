package com.lkb.controller.sb;



import javax.annotation.Resource;

import com.lkb.bean.client.SocialInsuranceOut;
import com.lkb.controller.BaseInfo_Controller;
import com.lkb.service.ISheBaoService;
import com.lkb.thirdUtil.base.BaseInfo;
import com.lkb.thirdUtil.base.BaseInfoSocialInsurance;


/** 社保超类
 * @author fastw
 * @date	2014-9-3 下午5:18:26
 */
public class SocialInsurance_Controller extends BaseInfo_Controller{
	
	@Resource
	private ISheBaoService shebaoService;
	
	public void setService(BaseInfo base){
		BaseInfoSocialInsurance baseInfo = (BaseInfoSocialInsurance) base;
		baseInfo.setShebaoService(shebaoService);
	}

	public SocialInsuranceOut vertifyLogin(BaseInfoSocialInsurance baseinfo) {
		this.setServices(baseinfo);
		SocialInsuranceOut sio = baseinfo.logins_sio(); 
		baseinfo.close();
		return sio;
	}
	public  SocialInsuranceOut init(BaseInfoSocialInsurance baseinfo) {
		this.setServices(baseinfo);
		SocialInsuranceOut sio = baseinfo.index_sio();
		baseinfo.close();
		return sio;
	}
}
