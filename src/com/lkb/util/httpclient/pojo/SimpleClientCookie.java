package com.lkb.util.httpclient.pojo;

import java.io.Serializable;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

/**cookie 操作的基本方法
 * @author fastw
 * @date	2014-9-27 下午5:50:11
 */
public class SimpleClientCookie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6490727698900156743L;

	
	
	private HttpClientContext context;
	
	public SimpleClientCookie(HttpClientContext context) {
		super();
		this.context = context;
	}
	/**添加cookie
	 * @param key  
	 * @param value
	 */
	public void addCookie(String key,String value){
		getCookieStore().addCookie(new BasicClientCookie(key,value));
	}
	public void addCookie(String key,String value,String domain){
		BasicClientCookie bs = new BasicClientCookie(key,value);
		bs.setDomain(domain);
		getCookieStore().addCookie(bs);
	}
	
	public String getCookiesString(){
		List<Cookie> list = getCookieStore().getCookies();
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
		return getCookie("JSESSIONID", domain);
	}
	public String getCookie(String key,String domain){
		return getCookieValue(key, domain);
	}
	
	public CookieStore getCookieStore(){
		if(context.getCookieStore()==null){
			context.setCookieStore(new BasicCookieStore());
		}
		return context.getCookieStore();
	}
	
	
	/**如果找不到键对应的值,返回空
	 * @return
	 */
	private String getCookieValue(String cookieKey,String domain){
		List<Cookie> list = getCookieStore().getCookies();
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
	

}
