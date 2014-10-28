package com.lkb.controller;

import java.util.Map;

import javax.annotation.Resource;

import com.lkb.bean.client.Login;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinFlowDetailService;
import com.lkb.service.IDianXinFlowService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileMessageService;
import com.lkb.service.IMobileOnlineBillService;
import com.lkb.service.IMobileOnlineListService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.ITelcomMessageService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomFlowBillService;
import com.lkb.service.IUnicomFlowService;
import com.lkb.service.IUnicomMessageService;
import com.lkb.service.IUnicomTelService;
import com.lkb.thirdUtil.base.BaseInfo;
import com.lkb.thirdUtil.base.BaseInfoMobile;

/**
 * 手机超类
 * 
 * @author fastw
 * @date 2014-8-10 上午11:31:02
 */
public class Mobile_Controller extends BaseInfo_Controller {
	@Resource
	protected IDianXinTelService dianXinTelService;
	@Resource
	protected IDianXinDetailService dianXinDetailService;
	@Resource
	protected IMobileTelService mobileTelService;
	@Resource
	protected IMobileDetailService mobileDetailService;

	@Resource
	public IUnicomTelService unicomTelService;
	@Resource
	public IUnicomDetailService unicomDetailService;

	@Resource
	protected IUnicomFlowBillService unicomFlowBillService;
	@Resource
	public IUnicomMessageService unicomMessageService;
	@Resource
	protected IMobileMessageService mobileMessageService;
	@Resource
	public ITelcomMessageService telcomMessageService;
	@Resource
	public IUnicomFlowService unicomFlowService;
	@Resource
	public IDianXinFlowService dianXinFlowService;
	@Resource
	public IDianXinFlowDetailService dianXinFlowDetailService;
	@Resource
	public IMobileOnlineBillService mobileOnlineBillService;
	@Resource
	public IMobileOnlineListService mobileOnlineListService;

	public void setService(BaseInfo base) {
		BaseInfoMobile baseinfo = (BaseInfoMobile) base;
		baseinfo.setDianXinDetailService(dianXinDetailService)
				.setDianXinTelService(dianXinTelService);
		baseinfo.setMobileDetailService(mobileDetailService)
				.setMobileTelService(mobileTelService);
		baseinfo.setUnicomDetailService(unicomDetailService)
				.setUnicomTelService(unicomTelService)
				.setMobileMessageService(mobileMessageService);
		baseinfo.setTelcomMessageService(telcomMessageService);
		baseinfo.setMobileMessageService(mobileMessageService);
		baseinfo.setUnicomMessageService(unicomMessageService);
		baseinfo.setUnicomFlowService(unicomFlowService);
		baseinfo.setUnicomFlowBillService(unicomFlowBillService);
		baseinfo.setDianXinFlowService(dianXinFlowService);
		baseinfo.setDianXinFlowDetailService(dianXinFlowDetailService);
		baseinfo.setMobileOnlineBillService(mobileOnlineBillService);
		baseinfo.setMobileOnlineListService(mobileOnlineListService);
	}

	/**
	 * 验证手机动态口令
	 * 
	 * @return
	 */
	public Map<String, Object> checkPhoneDynamicsCode(BaseInfo baseinfo) {
		return super.checkPhoneDynamicsCode(baseinfo);
	}

	/**
	 * 发送手机动态口令
	 * 
	 * @return
	 */
	public Map<String, Object> sendPhoneDynamicsCode(BaseInfo baseinfo) {
		return super.sendPhoneDynamicsCode(baseinfo);
	}

	/**
	 * @param baseinfo
	 * @param login
	 *            此处login可为null;
	 * @return
	 */
	public Map<String, Object> login(BaseInfo baseinfo, Login login) {
		return super.login(baseinfo, login);
	}

	public Map<String, Object> login(BaseInfo baseinfo) {
		return super.login(baseinfo, null);
	}

	public Map<String, Object> putongFirst(BaseInfo baseinfo) {
		return super.putongFirst(baseinfo);
	}
}
