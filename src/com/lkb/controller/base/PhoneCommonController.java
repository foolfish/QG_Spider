package com.lkb.controller.base;



import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.bean.req.FormData;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ErrorMsg;
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
import com.lkb.thirdUtil.base.BasicCommonAbstract;
import com.lkb.thirdUtil.base.pojo.ActionStatusLabel;
import com.lkb.thirdUtil.base.pojo.ServicePojo;
import com.lkb.util.redis.Redis;

/** 手机超类
 * @author fastw
 * @param <T>
 * @date	2014-8-10 上午11:31:02
 */
public class PhoneCommonController extends BasicCommonController{
	public static final String init_key = "PhoneCommonController.init";
	public static final String init_key_value = "PhoneCommonController.init.value";
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
	public IUnicomMessageService unicomMessageService;
	@Resource
	protected IMobileMessageService mobileMessageService;
	@Resource
	public ITelcomMessageService telcomMessageService;
	//---------------------------------------------流量
	@Resource
	public IUnicomFlowService unicomFlowService;
	@Resource
	protected IUnicomFlowBillService unicomFlowBillService;
	@Resource
	public IDianXinFlowService dianXinFlowService;
	@Resource
	public IDianXinFlowDetailService dianXinFlowDetailService;
	@Resource
	protected IMobileOnlineBillService mobileOnlineBillService;
	@Resource
	protected IMobileOnlineListService mobileOnlineListService;
	
	public ServicePojo getServices(){
		if(pojo==null){
			pojo = new ServicePojo();
			pojo.setDianXinDetailService(dianXinDetailService).setDianXinTelService(dianXinTelService);
			pojo.setMobileDetailService(mobileDetailService).setMobileTelService(mobileTelService);
			pojo.setUnicomDetailService(unicomDetailService).setUnicomTelService(unicomTelService).setMobileMessageService(mobileMessageService);
			pojo.setTelcomMessageService(telcomMessageService).setMobileMessageService(mobileMessageService).setUnicomMessageService(unicomMessageService);
			pojo.setUserService(userService).setParseService(parseService).setWarningService(warningService);
			//----------流量,流量账单
			pojo.setUnicomFlowBillService(unicomFlowBillService).setUnicomDetailService(unicomDetailService);
			pojo.setMobileOnlineBillService(mobileOnlineBillService).setMobileOnlineListService(mobileOnlineListService);
			pojo.setDianXinFlowDetailService(dianXinFlowDetailService).setDianXinFlowService(dianXinFlowService);
			
		}
		return pojo;
	}
	public ResOutput checkDynamicsCode2(BasicCommonAbstract<ResOutput>  pojo){
		return getBasicByResOutput(pojo, 6, this.getServices());
	}
	public ResOutput getSeccondRequest(BasicCommonAbstract<ResOutput>  pojo){
		return getBasicByResOutput(pojo, 5, this.getServices());
	}
	/**
	 * 验证手机动态口令
	 * @return
	 */
	public ResOutput checkPhoneDynamicsCode(BasicCommonAbstract<ResOutput>  pojo) {
		return getBasicByResOutput(pojo, 4, this.getServices());
	}
	
	/**
	 * 发送手机动态口令
	 * @return
	 */
	public ResOutput sendPhoneDynamicsCode(BasicCommonAbstract<ResOutput> pojo) {
		return getBasicByResOutput(pojo, 3, this.getServices());
	}
	
	
	/**
	 * 针对手机号
	 * @param fd
	 * @return
	 */
	public Login getLogin(FormData fd){
		Login login = new Login();
		login.setLoginName(fd.getUserName()!=null?fd.getUserName():fd.getPhoneNo());
		login.setPassword(fd.getPassword());
		login.setPhoneCode(fd.getSmsCode());
		login.setAuthcode(fd.getAuthCode());
		login.setCurrentUser(fd.getUserId());
		return login;
	}
//	private Map<String,Object> ResOutputToMap(ResOutput output){
//		Map<String,Object> map = new LinkedHashMap<String,Object>();
//		map.put(CommonConstant.status, output.getStatus());
//		map.put(CommonConstant.errorMsg, output.getErrorMsg());
//		map.put("url", output.getUrl());
//		map.put("errorcode", output.getErrorcode());
//		return map;
//	}
	
}
