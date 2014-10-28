package com.lkb.controller.ds;


import java.util.Map;

import javax.annotation.Resource;

import com.lkb.bean.client.Login;
import com.lkb.controller.BaseInfo_Controller;
import com.lkb.service.IOrderItemService;
import com.lkb.service.IOrderService;
import com.lkb.service.IPayInfoService;
import com.lkb.service.IYuEBaoService;
import com.lkb.thirdUtil.base.BaseInfo;
import com.lkb.thirdUtil.base.BaseInfoBuisness;

/** 电商的超类
 * @author fastw
 * @date	2014-8-10 上午11:31:02
 */
public class DS_Controller extends BaseInfo_Controller{
	@Resource
	public IOrderService orderService;
	@Resource
	public IOrderItemService orderItemService;
	@Resource
	public IPayInfoService payInfoService;
	@Resource
	public IYuEBaoService yuebaoService;
	
	public void setService(BaseInfo base){
		BaseInfoBuisness baseinfo = (BaseInfoBuisness) base;
		baseinfo.setPayInfoService(payInfoService).setYuebaoService(yuebaoService);
		baseinfo.setOrderItemService(orderItemService).setOrderService(orderService);
	}
	
	/**
	 * 验证手机动态口令
	 * @return
	 */
	public Map<String, Object> checkPhoneDynamicsCode(BaseInfo baseinfo) {
		return super.checkPhoneDynamicsCode(baseinfo);
	}
	
	/**
	 * 发送手机动态口令
	 * @return
	 */
	public Map<String, Object> sendPhoneDynamicsCode(BaseInfo baseinfo) {		
		return super.sendPhoneDynamicsCode(baseinfo);
	}
	/**
	 * @param baseinfo
	 * @param login 此处login可为null;
	 * @return
	 */
	public Map<String, Object> login(BaseInfo baseinfo,Login login) {		
		return super.login(baseinfo, login);
	}
	public Map<String, Object> login(BaseInfo baseinfo) {
		return super.login(baseinfo, null);
	}
	public  Map<String, Object> putongFirst(BaseInfo baseinfo) {
		return super.putongFirst(baseinfo);
	}
}
