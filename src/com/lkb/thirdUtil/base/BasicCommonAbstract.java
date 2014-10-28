package com.lkb.thirdUtil.base;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Args;
import org.apache.log4j.Logger;

import com.lkb.bean.Parse;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.thirdUtil.base.pojo.Log4jPojo;
import com.lkb.thirdUtil.base.pojo.ServicePojo;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.pojo.SimpleClientContext;
import com.lkb.util.httpclient.pojo.SimpleClientCookie;
import com.lkb.util.httpclient.pojo.SimpleData;
import com.lkb.util.httpclient.response.ExecuteRequest;

public abstract class BasicCommonAbstract<T> extends BasicAbstract<T>{
	protected static Logger log = Logger.getLogger(BasicCommonAbstract.class);
	/**是否登陆key*/
	public static final String KEY_ISLOGIN = "BasicCommonAbstract.isLogin";
		
	public String loginName;
	public String currentUser;
	public String userSource;
	
	
	public ServicePojo service;
	
	public SimpleData redismap;
	public Login login;
	public ExecuteRequest cutil;
	/**操作cookie*/
	private SimpleClientCookie cookie;
	/**日志输出**/
	protected Log4jPojo log4j;
	/**类名称键*/
	public String constantNum;
	/**redis缓存键*/
	private String redisKey;
	/**操作基本方法*/
	public SimpleClientContext context;
	
	public ResOutput output;
	
	
	
	
	/**本地初始化的请求方式测试
	 * @param login
	 */
	public BasicCommonAbstract(Login login){
		this.login = login;
		this.loginName = login.getLoginName();
		this.currentUser = login.getCurrentUser();
		context = new SimpleClientContext(this.getRediskey());
		redismap = context.getSimpleData();
		if(isTest()){
			initialize();
		}
	}
	
	/**web初始化的请求方式**/
	public BasicCommonAbstract(Login login,String currentUser){
		this.login = login;
		this.loginName = login.getLoginName();
		if(currentUser!=null){
			this.currentUser = currentUser;
		}else{
			this.currentUser = login.getCurrentUser();
		}
		this.constantNum = this.getClass().getSimpleName();
		context = new SimpleClientContext(this.getRediskey());
		redismap = context.getSimpleData();
		//service赋值的时候进行初始化发送请求类
		if(isTest()){
			initialize();
		}
	}
	
	private void initialize(){
		log4j = new Log4jPojo(loginName,constantNum, currentUser);
		if(service!=null){
			log4j.setWarningService(service.getWarningService());
		}
		cutil = new ExecuteRequest();
		context.setContext(cutil.getContext());
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
	
	
	
	public SimpleClientContext getContext() {
		return context;
	}
	
	public SimpleClientCookie getCookie() {
		if(cookie==null){
			cookie = cutil.getCookie();
		}
		return cookie;
	}
	
	public SimpleData getRedismap() {
		return redismap;
	}
	public String getRediskey(){
		if(redisKey==null){
			StringBuffer sb = new StringBuffer();
			sb.append(this.currentUser).append(this.getClass().getSimpleName());
			try {
				sb.append(URLEncoder.encode(this.loginName,"utf-8"));
			} catch (Exception e) {
				sb.append(this.loginName);
			}	
			redisKey =  sb.toString();
		}
		return redisKey;
	}
	
	/**设置service时初始化请求对象
	 * @param servicePojo
	 * @return
	 */
	public BasicCommonAbstract<T> setServicePojo(ServicePojo service) {
		this.service = service;
		this.initialize();
		return this;
	}
	
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		Args.notNull(login, "login Object");
		this.login = login;
	}
	
	
	public abstract void init();
	public abstract T index();
	
	public abstract T logins();
	
	
	public T sendPhoneDynamicsCode(){return null;};
	
	
	public T checkPhoneDynamicsCode(){return null;};
	
	public  T getSeccondRequest() {return null;};
	public T checkDynamicsCode2() {return null;};
	
	public abstract User saveUser();
	public abstract void startSpider(int type);
	
	/**测试方法*/
	public void inputCode(String imgurl){
		if(StringUtils.isNotBlank(imgurl)){
			cutil.downimgCode(imgurl,null,"d:/a.png");
			login.setAuthcode(CUtil.inputYanzhengma());
		}
	}
	/**
	 *告诉redis登陆成功了
	 */
	public void loginsuccess(){
		if(output!=null){
			output.setStatus(1);
			output.setErrorMsg("ok");
		}
		redismap.put(KEY_ISLOGIN,1);
		context.removeImgUrl();
		context.removeInit();
	}	
	/**只有成功时才设值*/
	public boolean islogin(){
		if(redismap.get(KEY_ISLOGIN)!=null)
			return true;	
		else
			return false;
	}
	/**
	 * 清楚redis对象和内存中存在cookieStore对象
	 */
	public void reset(){
		context.reset();
	}
	   public void parseBegin(){
	    	try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("userId", this.currentUser);
			map.put("loginName", this.loginName);
			map.put("usersource", this.userSource);
			List<Parse> list = service.getParseService().getParseBySome(map);
			Date date = new Date();
			if(list!=null && list.size()>0){
				Parse parse = list.get(0);
				parse.setStatus(1);
				parse.setModifyTime(date);
				service.getParseService().update(parse);			
			}else{
				Parse parse = new Parse();
				parse.setStatus(1);
				parse.setModifyTime(date);
				parse.setUserId(this.currentUser);
				parse.setLoginName(this.loginName);
				parse.setUsersource(this.userSource);
				service.getParseService().save(parse);
			}
	    	}catch(Exception e){
	    		log.error("关闭采集状态异常#", e);
	    	}
		}
		
		public void parseEnd(){
			try{
				Map<String,String> map = new HashMap<String,String>();
				map.put("userId", this.currentUser);
				map.put("loginName",  this.loginName);
				map.put("usersource", this.userSource);
				List<Parse> list = service.getParseService().getParseBySome(map);
				Date date = new Date();
				if(list!=null && list.size()>0){
					Parse parse = list.get(0);
					parse.setStatus(0);
					parse.setModifyTime(date);
					service.getParseService().update(parse);			
				}else{
					Parse parse = new Parse();
					parse.setStatus(0);
					parse.setModifyTime(date);
					parse.setUserId(this.currentUser);
					parse.setLoginName(this.loginName);
					parse.setUsersource(this.userSource);
					service.getParseService().save(parse);
				}
			}catch(Exception e){
				log.error("关闭采集状态异常#", e);
			}
		}
}
