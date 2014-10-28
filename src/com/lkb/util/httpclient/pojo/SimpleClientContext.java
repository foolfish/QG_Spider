package com.lkb.util.httpclient.pojo;

import java.io.Serializable;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;

import com.lkb.thirdUtil.base.BasicAbstract;
import com.lkb.util.redis.Redis;

public class SimpleClientContext implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6490727698900156743L;

	private String key;
	
	private HttpClientContext context;
	
	private SimpleData simpleData;
	
	public SimpleClientContext(String key) {
		super();
		this.key = key;
		this.simpleData = getInstanceSimpleData(key);
	}
	
	
	public void setContext(HttpClientContext context) {
		this.context = context;
		CookieStore cs = (CookieStore) simpleData.get(BasicAbstract.KEY_COOKIESTORE);
		if(cs!=null){
			context.setCookieStore(cs);
		}
	}


	/**初始化 SimpleData对象
	 * @return
	 */
	private synchronized SimpleData getInstanceSimpleData(String key){
		simpleData = (SimpleData) Redis.getObj(key);
		if(simpleData==null){
			simpleData = new SimpleData();
		}
		return simpleData;
	}
	
	public SimpleData getSimpleData() {
		return simpleData;
	}
	/**移除*/
	public void del(){
		Redis.del(key);
	}
	/**更新上下文信息包括redis*/
	public void update(){
		update(15);
	}
	
	/**
	 * @param min 默认多少分钟
	 */
	public  void update(int min){
		if(min<0){
			min = 0;
		}
		simpleData.put(BasicAbstract.KEY_COOKIESTORE, getCookieStore());
		Redis.setEx(key, simpleData,Redis.getMinute(min));
	}
	/**
	 * 设置过期时间
	 * @param min
	 */
	public void exists(int min){
		if(simpleData.isEmpty()){
			Redis.del(key);
		}else{
			Redis.setEx(key, simpleData,Redis.getMinute(min));
		}
	}
	/**redis缓存结果
	 * 清除cookie
	 * 清空内存中的对象结果
	 */
	public void reset(){
		if(simpleData!=null){
			simpleData.clear();
		}
		if(getCookieStore()!=null){
			getCookieStore().clear();
		}
		del();
	}
	
	public CookieStore getCookieStore(){
		if(context.getCookieStore()==null){
			context.setCookieStore(new BasicCookieStore());
		}
		return context.getCookieStore();
	}
	
	/**设置验证码 
	 */
	public void setImgUrl(String url){
		if(url!=null&&url.length()>10){
			simpleData.put(BasicAbstract.KEY_IMGURL,url);
		}
	}
	public void removeImgUrl(){
		simpleData.remove(BasicAbstract.KEY_IMGURL);
	}
	/**原网站 验证码 url 
	 * @return
	 */
	public String getImgUrl(){
		Object s = simpleData.get(BasicAbstract.KEY_IMGURL);
		if(s!=null){
			return s.toString();
		}
		return null;
	}
	public static final String KEY_IS_INIT = "SimpleClientContext.isInit";
	/**
	 * true 已经初始化 false 未初始化
	 * @return
	 */
	public boolean isInit(){
		 Object obj = simpleData.get(KEY_IS_INIT);
		 if(obj==null)
			 return false;
		 else
			 return true;
	}
	public void setInit(){
		simpleData.put(KEY_IS_INIT, "a");
	}
	public void removeInit(){
		simpleData.remove(KEY_IS_INIT);
	}
}
