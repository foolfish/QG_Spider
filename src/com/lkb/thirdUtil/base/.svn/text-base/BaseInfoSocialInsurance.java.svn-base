package com.lkb.thirdUtil.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.log4j.Logger;

import com.lkb.bean.SheBao;
import com.lkb.bean.User;
import com.lkb.bean.client.SocialInsuranceInput;
import com.lkb.bean.client.SocialInsuranceOut;
import com.lkb.constant.Constant;
import com.lkb.constant.ErrorMsg;
import com.lkb.service.ISheBaoService;
import com.lkb.thirdUtil.BaseUser;
import com.lkb.util.httpclient.ClientConnectionPool;
import com.lkb.util.httpclient.HttpRequest;
import com.lkb.util.redis.RedisMap;
import com.lkb.util.thread.Task;

/**社保类
 * @author fastw
 * @date	2014-9-9 上午11:26:38
 */
public abstract class BaseInfoSocialInsurance extends BaseInfo{
	protected static Logger log = Logger.getLogger(BaseInfoSocialInsurance.class);
	
	public String userSource ;

	public SocialInsuranceInput sii;
	public SocialInsuranceOut sio =  new SocialInsuranceOut();
	
	public ISheBaoService shebaoService;
	
	
	public BaseInfo setShebaoService(ISheBaoService shebaoService) {
		this.shebaoService = shebaoService;
		return this;
	}
	public BaseInfoSocialInsurance(){}
	
	public SocialInsuranceInput getSii() {
		return sii;
	}
	public int setSii(SocialInsuranceInput sii) {
		return setSii(sii,false);
	}
	/**
	 * @param sii
	 * @param b  是否启动调用验证码
	 * @return
	 */
	public int setSii(SocialInsuranceInput sii,boolean b) {
		this.sii = sii;
		this.currentUser = sii.getCurrentUser();
		this.constantNum = this.getClass().getSimpleName();//类名
		if(sii.getIdCard()!=null){
			this.loginName = sii.getIdCard();
		}else if(sii.getTsin()!=null){
			this.loginName = sii.getTsin();
		} else if(sii.getName()!=null){
			this.loginName = sii.getName();
		}else{
			this.loginName = "";
		}
		redismap = RedisMap.getInstance(getRediskey());
		if(redismap.size()==0){
			redismap = RedisMap.getInstance(getRediskey_NotLoginName());//当loginName为空的情况下,万一包含验证码,此处需要测试,暂时没测
		}
		if(b){
			//判断验证码
			String s = getImgUrl();
			if(!s.equals(isnone)&&!s.equals("")){
				if(StringUtils.isEmpty(sii.getAuthcode())){
					return ErrorMsg.authcodetEorror;
				}
			}
		}
		context = HttpClientContext.create();
		context.setCookieStore(RedisMap.getCookieStore(redismap));
		
		cutil = new HttpRequest(context);
		return ErrorMsg.normal;
	}
	private String getRediskey_NotLoginName(){
		return this.currentUser+this.constantNum;
	}
	/**
	 * 本地测试
	 * @param login
	 */
	public BaseInfoSocialInsurance(SocialInsuranceInput sii){
		setSii(sii);
	}
	public abstract  void getInputPrarms();
	public int getRequestPrarms(SocialInsuranceInput sii){
		this.getInputPrarms();
		Set<String> set = sio.getInputParams();
		String string = null;
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
		     string = iterator.next();
		     if(string.equals(SocialInsuranceInput.param_id_card)){
		    	 if(StringUtils.isEmpty(sii.getIdCard())){
		    		 return ErrorMsg.idCardNotEorror;
		    	 }else if(sii.getIdCard().length()!=15&&sii.getIdCard().length()!=18){
		    		 System.out.println(sii.getIdCard().length());
		    		 return ErrorMsg.idcard1Eorror;
		    	 }
		     }else if(string.equals(SocialInsuranceInput.param_tsin)){
		    	 if(StringUtils.isEmpty(sii.getTsin())){
		    		 return ErrorMsg.tsinNotEorror;
		    	 }
		     }else if(string.equals(SocialInsuranceInput.param_name)){
		    	 if(StringUtils.isEmpty(sii.getName())){
		    		 return ErrorMsg.nameNotEorror;
		    	 }
		     }
			
		}
		if(StringUtils.isEmpty(sii.getPassword())){
			 return ErrorMsg.passwordEorror;
		}
		return ErrorMsg.normal;
	}
	
	
	
	
	public  SocialInsuranceOut index_sio(){
		this.getInputPrarms();
		this.init();
		status = 1;
		sio.setStatus(status);
		sio.setUrl(getAuthcode());
		return sio;
	}
	
	
	/**
	 * 登陆后输出 SocialInsuranceOut对象
	 */
	public abstract void login_sio();
	
	public  SocialInsuranceOut logins_sio(){
		this.getInputPrarms();
		this.init();//校验基本参数是否初始化
		this.login_sio();
		if(errorMsg==null){
			removeInit();
			errorMsg = "登陆出错，请重试！";
		}
		sio.setErrorMsg(errorMsg);
		if(status==1){
			sio.setUrl(isnone);
		}else{
			sio.setUrl(getAuthcode());
		}
		sio.setStatus(status);
		return sio;
	}
	 /**
     * 添加时需要设置login.getType 如果是1大部分值采集个人信息和账单信息,2通话记录.3采集所有
     * @param base
     */
    public void addTask(BaseInfoSocialInsurance base){
    	if(base.getSii().getType()==0){
    		base.getSii().setType(3);
    	}
    	Task.addTask(base);
	}
    public User findUser(){
		try{
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("loginName", this.loginName);
			map.put("usersource", Constant.SHSHEBAO);
			List<User> list = userService.getUserByParentIdSource(map);
			if(list!=null&&list.size()!=0){
				return list.get(0);
			}
		}catch(Exception e){
			log.warn("检测是否包含此个人信息",e);
		}
		return null;
    }
    public User findUser(Map<String,String> map){
		try{
			List<User> list = userService.getUserByParentIdSource(map);
			if(list!=null&&list.size()!=0){
				return list.get(0);
			}
		}catch(Exception e){
			log.warn("检测是否包含此个人信息",e);
		}
		return null;
    }
    
    
    public void setUserGeneralProp(User user){
    	user.setLoginName(this.loginName);
		user.setUsersource(Constant.SHSHEBAO);
		user.setUsersource2(Constant.SHSHEBAO);
		user.setParentId(currentUser);
		user.setModifyDate(new Date());
    }
    /***
     * 此处以user.getId判断,如果数据库没有数据不要设置uuid
     * @param user
     */
    public void saveUser(User user){
    	try{
    		if(user!=null){
    			if(user.getId()!=null){
    				userService.update(user);
    			}else{
    				UUID uuid = UUID.randomUUID();
					user.setId(uuid.toString());
    				userService.saveUser(user);
    			}
    			saveBaseUser(user);
    		}
    	}catch(Exception e){
    		log.error("user信息更新或插入失败#",e);
    	}
    }
    public void saveBaseUser(User user){
    	
    	BaseUser bu = new BaseUser();
		Map<String,Object> baseMap = new HashMap<String,Object>();
		baseMap.put("idcard", user.getIdcard());
		if(user.getRealName()!=null){
			baseMap.put("realName", user.getRealName());
		}else if(user.getSex()!=null){
			baseMap.put("sex", user.getSex());
		}
		
		baseMap.put("modifyDate", user.getModifyDate());
//		try{
//			bu.saveUserInfo(userService, baseMap, currentUser);
//		}catch(Exception e){
//			log.warn("base user 数据更新失败",e);
//		}
    }
    /**
     *临时解决方案 
     */
    public SheBao findShebao(Date date){
    	Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("baseUserId", currentUser);
		map.put("source", this.userSource);	
		map.put("payTime", date);
		try{
			List<SheBao> list = shebaoService.getSheBaoByBaseUseridsource(map);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			log.warn("base user 数据更新失败",e);
		}
		return null;
    }
    public void saveShebao(List<SheBao> list){
    	try{
    		if(list!=null&&list.size()>0){
    			for (SheBao sheBao : list) {
    				if(sheBao.getId()!=null){
    					shebaoService.update(sheBao);	
    				}else{
    					sheBao.setId(UUID.randomUUID().toString());
    					shebaoService.saveSheBao(sheBao);	
    				}
				}
    		}
    	}catch(Exception e){
    		log.error("社保信息更新或插入失败#",e);
    	}
    }
}
