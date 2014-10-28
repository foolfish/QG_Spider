package com.lkb.thirdUtil.base;


import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.lkb.bean.client.Login;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IOrderItemService;
import com.lkb.service.IOrderService;
import com.lkb.service.IParseService;
import com.lkb.service.IPayInfoService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.service.IYuEBaoService;
import com.lkb.util.httpclient.ClientConnectionPool;
import com.lkb.util.httpclient.HttpRequest;
import com.lkb.util.redis.RedisMap;
import com.lkb.warning.WarningUtil;

/**
 * @author fastw
 * @date	2014-9-7 上午12:02:59
 */
public class BaseService {
	protected IUserService userService;
	protected IWarningService warningService;
	protected IParseService parseService;
	
	protected Map<String,Object> redismap =  null;
	protected Login login = null;
	protected String currentUser;
	/**登陆名称,有login.getloginName获取*/
	public String loginName;
	
	protected String islogin = "t_islogin";
	/**手机验证是否通过*/
	protected String smsPass = "t_smsPass";
	protected Map<String,Object> map = new LinkedHashMap<String,Object>();
	/**正常执行的上下文*/
	public HttpClientContext context = null;
	public HttpRequest cutil ;
	/**针对登陆状态
	 * 非web交互慎用,尽量重定义
	 */
	protected int status = 0; //标示是否正常执行完毕
	/**针对登陆错误或正确的信息内容返回
	 * 非web交互慎用,尽量重定义
	 * **/
	protected String errorMsg = null;
	/**
	 * session+登陆名+类别唯一标示,此值为唯一标示
	 */
	protected String constantNum ;
	
	protected static String isInit = "BaseService_isInit";
	
	/**
	 *是不是none 默认none可设置为"" 
	 */
	public String isnone = "";
	
	/**
	 * true 已经初始化 false 未初始化
	 * @return
	 */
	public boolean isInit(){
		 Object obj = redismap.get(isInit);
		 if(obj==null)
			 return false;
		 else
			 return true;
	}
	public void setInit(){
		redismap.put(isInit, "a");
	}
	public BaseService setUserService(IUserService userService) {
		this.userService = userService;
		return this;
	}
	
	
	public BaseService setWarningService(IWarningService warningService) {
		this.warningService = warningService;
		return this;
	}
	public BaseService setParseService(IParseService parseService) {
		this.parseService = parseService;
		return this;
	}

	public String getRediskey(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.currentUser).append(this.constantNum);
		try {
			sb.append(URLEncoder.encode(this.loginName,"utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			sb.append(this.loginName);
		}	
		return sb.toString();
	}
	public boolean islogin(){
		//只有成功时才设值
		if(redismap.get(islogin)!=null){
			return true;	
		}else{
			return false;
		}
	}
	public  boolean isSmsPass(){
		if(redismap.get(smsPass)!=null){
			return true;
		}else{
			return false;
		}
	}
	/**
	 *设置手机验证码登陆成功 
	 */
	public void setSmsPass(){
		redismap.put(smsPass,"1");
	}
	/**
	 *告诉redis登陆成功了
	 */
	public void loginsuccess(){
		status = 1;
		errorMsg = "ok";
		redismap.put(islogin,true);
		removeImgUrl();
		redismap.remove(isInit);//移除第一条
	}
	
	
	public void close(){
		RedisMap.setCookieStore(redismap, context.getCookieStore());
//		client.close();
		RedisMap.writeRedis(redismap, getRediskey());
	}
	public void del(){
		RedisMap.delRedisMap(getRediskey());
	}
	/**
	 * 几分钟后删除
	 * @param min
	 */
	public void del(int min){
		RedisMap.writeRedis(redismap, getRediskey(),min);
	}
	public void clearRedis(){
		context.getCookieStore().clear();
		redismap.clear();
	}
	/**添加cookie
	 * @param key  
	 * @param value
	 */
	public void addCookie(String key,String value){
		context.getCookieStore().addCookie(new BasicClientCookie(key,value));
	}
	public void addCookie(String key,String value,String domain){
		BasicClientCookie bs = new BasicClientCookie(key,value);
		bs.setDomain(domain);
		context.getCookieStore().addCookie(bs);
	}
	public String getCookiesString(){
		List<Cookie> list = context.getCookieStore().getCookies();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).getName()).append(":").append(list.get(i).getValue()).append(";");
		}
		return sb.toString();
	}
	/**189.cn
	 * 根据网站获取网站host
	 * @param domain
	 * @return
	 */
	public String getJsessionId(String domain){
		return getCookieValue("JSESSIONID", domain);
	}
	public String getJsessionId(String key,String domain){
		return getCookieValue(key, domain);
	}
	
	/**设置验证码 
	 * @param url
	 */
	public void setImgUrl(String url){
		if(url!=null&&url.length()>10){
			redismap.put("k_imgurl",url);
		}
		
	}
	public void removeImgUrl(){
		redismap.remove("k_imgurl");
	}
	/**原网站 验证码 url 
	 * @return
	 */
	public String getImgUrl(){
		Object s = redismap.get("k_imgurl");
		if(s!=null){
			return s.toString();
		}
		return this.isnone;
	}
	/**如果找不到键对应的值,返回空
	 * @return
	 */
	private String getCookieValue(String cookieKey,String domain){
		List<Cookie> list = context.getCookieStore().getCookies();
		String value = null;
		for (int i = 0; i < list.size(); i++) {
			value = list.get(i).getName();
			if(cookieKey!=null&&cookieKey.equals(value)){
				if(list.get(i).getDomain().contains(domain)){
					value = list.get(i).getValue();
					break;
				}

			}
		}
		return value;
	}
	public Login getLogin(){
		return this.login;
	}
}
