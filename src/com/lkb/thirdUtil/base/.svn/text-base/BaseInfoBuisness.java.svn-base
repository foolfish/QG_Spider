package com.lkb.thirdUtil.base;


import org.apache.log4j.Logger;

import com.lkb.bean.client.Login;
import com.lkb.service.IOrderItemService;
import com.lkb.service.IOrderService;
import com.lkb.service.IPayInfoService;
import com.lkb.service.IWarningService;
import com.lkb.service.IYuEBaoService;

/** 所有电商类的父类
 * @author fastw
 * @date	2014-8-3 下午9:13:42
 */
public abstract class BaseInfoBuisness extends BaseInfo{
	protected static Logger log = Logger.getLogger(BaseInfoBuisness.class);
	public BaseInfoBuisness(){}
	public IOrderService orderService; 
	public IOrderItemService orderItemService;
	
	public IPayInfoService payInfoService;
	public IYuEBaoService yuebaoService;
	/**
	 * 本地测试
	 * @param login
	 */
	public BaseInfoBuisness(Login login){
		super(login);
	}
	public BaseInfoBuisness(Login login,IWarningService warningService,String ConstantNum,String currentUser){
		super(login,warningService,ConstantNum,currentUser);
	}
	public BaseInfoBuisness(Login login,String ConstantNum,String currentUser){
		super(login,ConstantNum,currentUser);
	}
	
	public BaseInfoBuisness setOrderService(IOrderService orderService) {
		this.orderService = orderService;
		return this;
	}
	public BaseInfoBuisness setOrderItemService(IOrderItemService orderItemService) {
		this.orderItemService = orderItemService;
		return this;
	}
	
	
	public BaseInfoBuisness setPayInfoService(IPayInfoService payInfoService) {
		this.payInfoService = payInfoService;
		return this;
	}
	public BaseInfoBuisness setYuebaoService(IYuEBaoService yuebaoService) {
		this.yuebaoService = yuebaoService;
		return this;
	}
	
	
}
