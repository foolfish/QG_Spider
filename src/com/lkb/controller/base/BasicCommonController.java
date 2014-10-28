package com.lkb.controller.base;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.lkb.bean.client.ResOutput;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ErrorMsg;
import com.lkb.service.ILostContentService;
import com.lkb.service.IParseService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BasicCommonAbstract;
import com.lkb.thirdUtil.base.pojo.ActionStatusLabel;
import com.lkb.thirdUtil.base.pojo.ServicePojo;
import com.lkb.util.redis.Redis;

/** 模拟登陆的超类
 * @author fastw
 * @date	2014-8-10 上午11:31:02
 */
public abstract class BasicCommonController{
	@Resource
	protected IUserService userService;
	@Resource
	protected IWarningService warningService;
	@Resource
	protected IParseService parseService;
	
	@Resource
	protected ILostContentService loseRequestUrlService;
	
	public ServicePojo pojo;
	
	/**并且初始化 父类变量中的request ,和session
	 * @return session中currentUser
	 */
	public String getCurrentUser(HttpServletRequest request){
		String currentUser =  request.getParameter("userId");
		if(currentUser!=null&&!currentUser.trim().equals("")){
			return currentUser;
		}else{
			return request.getSession().getAttribute("currentUser").toString();
		}
	}
	
//	/**
//	 * @param basic
//	 * @param type 代表第几步 分为 为 1,初始化,2,登陆3,发送手机口令,4,验证手机口令,5,获取2次验证码,6,二次验证,...
//	 * @return
//	 */
//	public abstract ResOutput  getRequstBasicByResOutput(BasicCommonAbstract<ResOutput> basic,int type) ;
	/**20秒内禁止重复点击
	 * @param basic
	  * @param type 代表第几步 分为 为 1,初始化,2,登陆3,发送手机口令,4,验证手机口令,5,获取2次验证码,6,二次验证,...
	 * @param service
	 * @return
	 */
	public ResOutput getBasicByResOutput(BasicCommonAbstract<ResOutput> basic,int type,ServicePojo service){
		ResOutput res = null;
		if(type!=1){
			String key = basic.getRediskey()+"##"+type;
			//从redis取值判断,用于以下判断
			ActionStatusLabel obj = (ActionStatusLabel)Redis.getObj(key);
			 //如果对象为空 进入下边直接初始化,发送请求
			if(obj==null){
				Redis.setEx(key,new ActionStatusLabel(),Redis.getMinute(1));
				basic.setServicePojo(service);
				res = this.getRequstBasicByResOutput(basic, type);
				Redis.setEx(key,new ActionStatusLabel().setRes(res),5);//5秒有效期
			}else{
				//判断这个对象是否为空
				if(obj.getRes()!=null){
					res = obj.getRes();
				}else{
					//90秒等待结果
					for (int i = 0; i < 60; i++) {
						obj = (ActionStatusLabel)Redis.getObj(key);
						if(obj.getRes()==null){
							sleep(1);
						}else{
							res = obj.getRes();
							break;
						}
					}
					if(res==null){
						Redis.setEx(key,new ActionStatusLabel(),Redis.getMinute(1));
						basic.setServicePojo(service);
						res = getRequstBasicByResOutput(basic, type);
						Redis.setEx(key,new ActionStatusLabel().setRes(res),5);//5秒有效期
					}
				}
			}
		}else{
			basic.setServicePojo(service);
			res = getRequstBasicByResOutput(basic, type);
		}
		return res;
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
				res = basic.sendPhoneDynamicsCode();
			else
				res = getResOutput(0,"您的账号授权已经过期,请重新授权!");
			break;
		case 4:
			if(basic.islogin())
				res = basic.checkPhoneDynamicsCode();
			else
				res = getResOutput(0,"您的账号授权已经过期,请重新授权!");
			break;
		case 5:
			if(basic.islogin())
				res = basic.getSeccondRequest();
			else
				res = getResOutput(0,"您的账号授权已经过期,请重新授权!");
			break;
		case 6:
			if(basic.islogin())
				res = basic.checkDynamicsCode2();
			else
				res = getResOutput(0,"您的账号授权已经过期,请重新授权!");
			break;
		default:
			break;
		}
	return res;
}
	
	public ResOutput getResOutput(int status,String errorMsg){
		ResOutput res = new ResOutput();
		res.setStatus(status);
		res.setErrorMsg(errorMsg);
		return res;
	}
	
	/**
	 * @param pojo
	 * @return
	 */
	public ResOutput login(BasicCommonAbstract<ResOutput> pojo) {	
		return getBasicByResOutput(pojo, 2, this.getServices());
	}
	public  ResOutput putongFirst(BasicCommonAbstract<ResOutput> pojo) {
		return getBasicByResOutput(pojo, 1, this.getServices());
	}
	
	
	
	public Map<String,Object> isLogin(BasicCommonAbstract<ResOutput> basic) {
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		int code = ErrorMsg.normal;
		if(StringUtils.isNotBlank(basic.getLogin().getLoginName())){
			code = ErrorMsg.loginNameEorror;
		}
		 map.put("errorcode", code); 
		 if(code==ErrorMsg.normal){
			if(basic.islogin()){
				map.put(CommonConstant.status,1);//登陆成功
			}else{
				map.put(CommonConstant.status,0);//未登陆
			}
		 }
		return map;
	}
	/**
	 * 返回service 基础类
	 */
	public abstract ServicePojo getServices();
	public void sleep(int second){
		try {
			if(second<=0){
				second=1;
			}
			Thread.sleep(second*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
