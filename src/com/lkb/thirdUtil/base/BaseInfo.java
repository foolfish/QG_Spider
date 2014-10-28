package com.lkb.thirdUtil.base;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.lkb.bean.Parse;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ConstantProperty;
import com.lkb.constant.ErrorMsg;
import com.lkb.service.IParseService;
import com.lkb.service.IWarningService;
import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.HttpRequest;
import com.lkb.util.proxyip.entity.TaobaoIp;
import com.lkb.util.redis.Redis;
import com.lkb.util.redis.RedisMap;
import com.lkb.util.thread.Task;
import com.lkb.warning.WarningUtil;

/** 所有登陆类的父类
 * @author fastw
 * @date	2014-8-3 下午9:13:42
 */
public abstract class BaseInfo extends BaseService{
	protected static Logger log = Logger.getLogger(BaseInfo.class);
	private final  static String isUniqueProxy = "BaseInfo.isUniqueProxy";
	public BaseInfo(){}
	public CloseableHttpClient client;
	/**
	 * 本地测试
	 * @param login
	 */
	public BaseInfo(Login login){
		this.login = login;
		this.loginName = login.getLoginName();
		this.constantNum = this.getClass().getSimpleName();//类名
		this.currentUser = login.getCurrentUser();
		redismap = RedisMap.getInstance(getRediskey());
		context = HttpClientContext.create();
		context.setCookieStore(RedisMap.getCookieStore(redismap));
		cutil = new HttpRequest(context);
		//临时性兼容
		client = cutil.client;
	}
	
	public BaseInfo(Login login,IWarningService warningService,String constantNum,String currentUser){
		if(Login.checkName(login)!=ErrorMsg.normal){
			return ;
		}
		this.login = login;
		this.loginName = login.getLoginName();
		this.warningService = warningService;
		this.constantNum = constantNum;
		//针对外部传入currentUser
		this.currentUser = login.getCurrentUser()==null?currentUser:login.getCurrentUser();
		redismap = RedisMap.getInstance(getRediskey());
		
		context = HttpClientContext.create();
		context.setCookieStore(RedisMap.getCookieStore(redismap));
		this.cutil = new HttpRequest(context);
		//临时性兼容
		client = cutil.client;
	}
	public BaseInfo(Login login,String constantNum,String currentUser){
		if(Login.checkName(login)!=ErrorMsg.normal){
			return ;
		}
		this.login = login;
		this.loginName = login.getLoginName();
		this.constantNum = constantNum;
		this.currentUser = login.getCurrentUser()==null?currentUser:login.getCurrentUser();
		redismap = RedisMap.getInstance(getRediskey());
		
		context = HttpClientContext.create();
		context.setCookieStore(RedisMap.getCookieStore(redismap));
		this.cutil = new HttpRequest(context);
		//临时性兼容
		client = cutil.client;
	}
	public BaseInfo(Login login,String currentUser){
		if(Login.checkName(login)!=ErrorMsg.normal){
			return ;
		}
		this.login = login;
		this.loginName = login.getLoginName();
		this.constantNum = this.getClass().getSimpleName();//类名
		this.currentUser = login.getCurrentUser()==null?currentUser:login.getCurrentUser();
		redismap = RedisMap.getInstance(getRediskey());
		
		context = HttpClientContext.create();
		context.setCookieStore(RedisMap.getCookieStore(redismap));
		this.cutil = new HttpRequest(context);
		//临时性兼容
		client = cutil.client;
	}
	
	public void setUniqueProxy(){
		HttpHost isProxy = (HttpHost)redismap.get(isUniqueProxy);
		if(isProxy==null){
//		    isProxy = new HttpHost("183.208.223.151", 8123, "http");  
		    isProxy = new HttpHost("139.217.5.155", 31288, "http");  
//		    isProxy = new HttpHost("112.124.105.36", 3218, "https");  
//		    isProxy = new HttpHost("118.186.242.222", 843, "http");  
			redismap.put(isUniqueProxy, isProxy);
		}
		cutil.setUniqueProxy(isProxy);
	}
	public boolean isUniqueProxy(){
		if(redismap.get(isUniqueProxy)!=null)
			return true;
		else
			return false;
	}
	
	/**本网站的验证码url*/
	public String getAuthcode(){
		String url = getImgUrl();
		if(!url.equals(this.isnone)&&!url.equals("")){
			String uuid = UUID.randomUUID().toString();
			StringBuffer sb = new StringBuffer();
			sb.append(InfoUtil.getInstance().getInfo(ConstantProperty.road,"server.full.path"));
			sb.append("/img_auth/").append(uuid);	
			sb.append(InfoUtil.getInstance().getInfo(ConstantProperty.road,"server.img.auth.code.suffix"));
			byte[] b = getAuthcode1();
			if(b!=null){
				Redis.setEx(uuid,b,2);	
			}
			return sb.toString();
					
		}
		return  url;
	}
	
	/**验证码字节数组
	 * @return
	 */
	public byte[] getAuthcode1(){
		String url = getImgUrl();
		if(!getImgUrl().equals(this.isnone)){
			return cutil.downimgCode(url);
		}
		return null;
	}
	/**测试方法*/
	public void inputCode(String imgurl){
		if(StringUtils.isNotBlank(imgurl)){
			cutil.downimgCode(imgurl,"d:/a.png");
			login.setAuthcode(CUtil.inputYanzhengma());
		}
	}
	public  Map<String,Object> index(){
		this.init();
		map.put("url",getAuthcode());
		map.put(CommonConstant.status,1);
		return map;
	}
	/**方法内不要执行验证码方法 getAuthcode1()
	 *只做session获取,不做参数存放,否则可能出现意象不到的问题 
	 **/
	public void init(){}
	public  Map<String,Object> login(){return null;}
	public  Map<String,Object> logins(){
		this.init();//校验基本参数是否初始化
		this.login();
		
		if(map.get(CommonConstant.errorMsg)==null){
			if(errorMsg==null){
				removeInit();
				errorMsg = "登陆出错，请重试！";
			}
			map.put(CommonConstant.errorMsg, errorMsg);	
		}
		if(map.get(CommonConstant.status)==null){
			map.put(CommonConstant.status, status);
		}
		if(map.get("url")==null){
			if(status==1){
				map.put("url",this.isnone);
			}else{
				map.put("url",getAuthcode());
			}
		}
		
		return map;
	}
	/**
	 *清空数据 所有的
	 */
	public void removeInit(){
		Integer num = 1;
		String key = "baseService_writeLogByLogin";
    	Object obj = redismap.get(key);
    	if(obj!=null){
    		int i = Integer.parseInt(obj.toString());
    		if(i<3){
    			num = ++i;
    		}else{
    			num = 1;
    			new WarningUtil().warning(warningService,currentUser, constantNum+"_DLSB");//登陆失败
    		}
    	}
		del();
		clearRedis();
		this.init();
		redismap.put(key, num);
	}
	public Map<String, Object> sendPhoneDynamicsCode() {return null;}
	public Map<String, Object> checkPhoneDynamicsCode() {return null;}
	/** 
	 *1.不采集通话记录2.单独采集通话记录,3采集所有
	 */
	public  void startSpider(){};
	
    /**
     * 添加时需要设置login.getType 如果是1大部分值采集个人信息和账单信息,2通话记录.3采集所有
     * @param base
     */
    public void addTask(BaseInfo base){
    	if(!isTest()){
    		if(base.getLogin().getType()==0){
        		base.getLogin().setType(3);
        	}
        	Task.addTask(base);	
    	}
	}
    /** 登陆错误 打印错误日志,设置错误状态
     * @param e
     */
    public void writeLogByLogin(Exception e){
    	log.warn(constantNum+"_"+this.loginName+"登陆异常#",e);
    }
	/**
	 * 针对老版本,主要是玉龙这块
	 */
	@Deprecated
	public void parseBegin(IParseService parseService,String userId,String loginName,String userSource){
		this.parseService = parseService;
		this.currentUser = userId;
		if(this.login==null){
			this.login = new Login(loginName,null);
		}else{
			this.login.setLoginName(loginName);
		}
		parseBegin(userSource);
	}
	/**
	 * 老版本慎用保证
	 * @param userSource
	 */
	public void parseBegin(String userSource){
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", this.currentUser);
		map.put("loginName", this.loginName);
		map.put("usersource", userSource);
		List<Parse> list = parseService.getParseBySome(map);
		Date date = new Date();
		if(list!=null && list.size()>0){
			Parse parse = list.get(0);
			parse.setStatus(1);
			parse.setModifyTime(date);
			parseService.update(parse);			
		}else{
			Parse parse = new Parse();
			parse.setStatus(1);
			parse.setModifyTime(date);
			parse.setUserId(this.currentUser);
			parse.setLoginName(this.loginName);
			parse.setUsersource(userSource);
			parseService.save(parse);
		}
	}
	/**
	 * 针对老版本,主要是玉龙这块
	 */
	@Deprecated
	public void parseEnd(IParseService parseService,String userId,String loginName,String userSource){
		this.parseService = parseService;
		this.currentUser = userId;
		if(this.login==null){
			this.login = new Login(loginName,null);
		}else{
			this.login.setLoginName(loginName);
		}
		parseEnd(userSource);
	}
	/**
	 * 老版本慎用保证
	 * @param userSource
	 */
	public void parseEnd(String userSource){
		Map<String,String> map = new HashMap<String,String>();
		map.put("userId", this.currentUser);
		map.put("loginName",  this.loginName);
		map.put("usersource", userSource);
		List<Parse> list = parseService.getParseBySome(map);
		Date date = new Date();
		if(list!=null && list.size()>0){
			Parse parse = list.get(0);
			parse.setStatus(0);
			parse.setModifyTime(date);
			parseService.update(parse);			
		}else{
			Parse parse = new Parse();
			parse.setStatus(0);
			parse.setModifyTime(date);
			parse.setUserId(this.currentUser);
			parse.setLoginName(this.loginName);
			parse.setUsersource(userSource);
			parseService.save(parse);
		}
	}
	/**是否是测试true为测试
	 * @return
	 */
	public boolean isTest() {
		if(this.currentUser==null){
			return true;
		}
		return false;
	}
}
