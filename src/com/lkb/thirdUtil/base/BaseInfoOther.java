package com.lkb.thirdUtil.base;

import org.apache.log4j.Logger;

import com.lkb.bean.client.Login;
import com.lkb.service.IWarningService;

/** 所有社保医保等的父类
 * @author fastw
 * @param <T>
 * @date	2014-8-3 下午9:13:42
 */
public abstract class BaseInfoOther extends BaseInfo{
	protected static Logger log = Logger.getLogger(BaseInfoOther.class);
	public BaseInfoOther(){}
	
	/**
	 * 本地测试
	 * @param login
	 */
	public BaseInfoOther(Login login){
		super(login);
	}
	public BaseInfoOther(Login login,IWarningService warningService,String ConstantNum,String currentUser){
		super(login,warningService,ConstantNum,currentUser);
	}
	public BaseInfoOther(Login login,String ConstantNum,String currentUser){
		super(login,ConstantNum,currentUser);
	}

	
}
