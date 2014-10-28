package com.lkb.thirdUtil.base.pojo;

import org.apache.log4j.Logger;

import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BasicCommonMobileControl;
import com.lkb.warning.WarningUtil;

/**
 * @author fastw
 * @date	2014-9-27 下午7:25:03
 */
public class Log4jPojo {
	private String loginName;
	private String className;
	private IWarningService warningService;
	private String currentUser;
	

	public Log4jPojo(String loginName, String className,String currentUser) {
		super();
		this.loginName = loginName;
		this.className = className;
		this.currentUser = currentUser;
	}


	protected static Logger log = Logger.getLogger(BasicCommonMobileControl.class);
	
	 public void setWarningService(IWarningService warningService) {
		this.warningService = warningService;
	}
	public void warn(String msg,Exception e){
		log.warn(className+"_"+loginName+msg+"#",e);
	}
	public void info(String msg){
		log.info(className+"_"+loginName+"#"+msg);
	}
	 
	/** 登陆错误 */
    public void login(Exception e){
    	log.warn(className+"_"+loginName+"登陆异常#",e);
    }
    /** 登陆错误 */
    public void error(Exception e){
    	log.warn(className+"_"+loginName+"#",e);
    }
    
    /**
     * @param msg 例如:账单记录,通话记录,个人信息
     * @param mark ZZJL,THJL,GRXX
     * @param e 异常
     */
    public void error(String msg,String mark,Exception e){
    	log.error(className+"_"+loginName+msg+"#",e);
    	try{
    		new WarningUtil().warning(warningService,currentUser, className+"_"+mark);
    	}catch(Exception e1){}
    }
  
    /** 账单信息错误 打印错误日志
     * @param e
     */
    public void writeLogByMyInfo(Exception e){
    	log.error(className+"_"+loginName+"用户信息异常,错误信息:",e);
    	try{
    		new WarningUtil().warning(warningService,currentUser, className+"_GRXX");//个人信息
    	}catch(Exception e1){}
    }
    /** 账单信息错误 打印错误日志
     * @param e
     */
    public void writeLogByZhangdan(Exception e){
    	log.error(className+"_"+loginName+"账单记录异常,错误信息:",e);
    	try{
    		new WarningUtil().warning(warningService,currentUser, className+"_ZDJL");//账单记录
    	}catch(Exception e1){}
    }
    /** 历史记录错误,打印错误日志
     * @param e
     */
    public void writeLogByHistory(Exception e){
    	log.error(className+"_"+loginName+"通话记录异常,错误信息:",e);
    	try{
    	new WarningUtil().warning(warningService,currentUser, className+"_THJL");//通话记录
    	}catch(Exception e1){}
    }
    /** 历史记录短信错误,打印错误日志
     * @param e
     */
    public void writeLogByMessage(Exception e){
    	log.error(className+"_"+loginName+"短信记录异常,错误信息:",e);
    	try{
    		new WarningUtil().warning(warningService,currentUser, className+"_DXJL");//通话记录
    	}catch(Exception e1){}
    }
    /**流量账单
     * @param e
     */
    public void writeLogByFlowBill(Exception e){
    	log.error(className+"_"+loginName+"流量账单异常,错误信息:",e);
    	try{
    		new WarningUtil().warning(warningService,currentUser, className+"_LLZD");//流量账单
    		
    	}catch(Exception e1){}
    }
    /**流量相当
     * @param e
     */
    public void writeLogByFlowList(Exception e){
    	log.error(className+"_"+loginName+"流量详单异常,错误信息:",e);
    	try{
    		new WarningUtil().warning(warningService,currentUser, className+"_LLXD");//流量详单
    	}catch(Exception e1){}
    }
    /**订单列表
     * type ORDER/ORDERITEM
     * @param e
     */
    public void writeLogOrder(String text,Exception e,String type){
    	if(text!=null){
    		log.error(className+"_"+loginName+"_"+type+"异常,错误信息:"+text,e);
        	try{
        		new WarningUtil().warning(warningService,currentUser, className+"_"+type);//订单列表
        	}catch(Exception e1){}
    	}
    }

}
