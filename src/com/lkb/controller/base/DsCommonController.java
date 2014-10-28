package com.lkb.controller.base;



import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.bean.req.FormData;
import com.lkb.constant.CommonConstant;
import com.lkb.service.IOrderItemService;
import com.lkb.service.IOrderService;
import com.lkb.service.IPayInfoService;
import com.lkb.service.IYuEBaoService;
import com.lkb.thirdUtil.base.BasicCommonAbstract;
import com.lkb.thirdUtil.base.pojo.ServicePojo;

/** 电商类
 * @author fastw
 * @date	2014-8-10 上午11:31:02
 */
public class DsCommonController extends BasicCommonController{
	@Resource
	public IOrderService orderService;
	@Resource
	public IOrderItemService orderItemService;
	@Resource
	public IPayInfoService payInfoService;
	@Resource
	public IYuEBaoService yuebaoService;
	
	
	public ServicePojo getServices(){
		if(pojo==null){
			pojo = new ServicePojo();
			pojo.setOrderService(orderService).setOrderItemService(orderItemService);
			pojo.setPayInfoService(payInfoService).setYuebaoService(yuebaoService);
			pojo.setUserService(userService).setParseService(parseService).setWarningService(warningService);
			pojo.setLoseContentService(loseRequestUrlService);
		}
		return pojo;
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
	 * @param basic
	 * @param type 代表第几步 分为 为 1,初始化,2,登陆3,发送手机口令,4,验证手机口令,5,获取2次验证码,6,二次验证,...
	 * @return
	 */
	public  ResOutput  getRequstBasicByResOutput(BasicCommonAbstract<ResOutput> basic,int type) {
	 ResOutput res = null;
	 	switch (type) {
		case 1:
			res = basic.index();
			break;
		case 2:
			if(basic.islogin())
				res = getResOutput(1,"ok");
			else
				res = basic.logins();	
			break;
		case 3:
			if(basic.islogin())
				res = getResOutput(1,"您已经登录成功,不需要手机验证!");
			else
				res = basic.sendPhoneDynamicsCode();
			break;
		case 4:
			if(basic.islogin())
				res = getResOutput(1,"您的账号授权已经过期,请重新授权!");
			else
				res = basic.checkPhoneDynamicsCode();
			break;
		default:
			break;
		}
	return res;
}
	
}
