//package com.lkb.util.redis;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.client.CookieStore;
//import org.apache.http.cookie.Cookie;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import com.lkb.constant.ConstantNum;
//import com.lkb.util.httpclient.CUtil;
//import com.lkb.util.proxyip.IPUtils;
//@Deprecated
//public class RedisClient {
//	
//	/**从redis中得到httpclient相关的值map
//	 * key 为编号加账号名称,保证键值唯一性
//	 * 包含DefaultHttpClient 但是不会存入redis中,方法结束client消失
//	 * @return
//	 */
//	public static Map<String,Object> getRedisMap(String key){
//		Map<String,Object> map = (Map<String, Object>) Redis.getObj(key);
//		if(map==null){
//			map = new HashMap<String,Object>();
//			//如果不用十分钟过期
//			Redis.setEx(key,map,Redis.getMinute(10));
//		}
//		if(getBuzhou(map)!=ConstantNum.client_buzhou_value){
//			map.put(ConstantNum.client, CUtil.init());
//			//初始化基本
//			initCookieStore(map);
//		}
//		initYZM(map);
//		return map;
//	}
//	public static Map<String,Object> init(String key){
//		Map<String,Object> map = (Map<String, Object>) Redis.getObj(key);
//		if(map==null){
//			map = new HashMap<String,Object>();
//			//如果不用十分钟过期
//			Redis.setEx(key,map,Redis.getMinute(10));
//		}
//		//初始化基本
//		initYZM(map);
//		return null;
//	}
//	/**只是在线程中使用
//	 * @param key
//	 * @return
//	 */
//	public static Map<String,Object> getRedisMapThread(String key){
//		Map<String,Object> map = (Map<String, Object>) Redis.getObj(key);
//		map.put(ConstantNum.client, CUtil.init());
//		initCookieStore(map);
//		return map;
//	}
//	public static DefaultHttpClient getClient(Map<String,Object> redismap){
//		return (DefaultHttpClient) redismap.get(ConstantNum.client);
//	}
//	public static CloseableHttpClient getCloseClient(Map<String,Object> redismap){
//		return (CloseableHttpClient) redismap.get(ConstantNum.client);
//	}
//	/**判断是否登录成功 true成功,false失败
//	 * @param redismap
//	 * @return
//	 */
//	public static boolean isLogin(Map<String,Object> redismap){
//		if(getClient(redismap)==null){
//			return true;
//		}
//		return false;
//	}
//	/**
//	 * @return 验证码地址
//	 */
//	public static String getYZM(Map<String,Object> redismap){
//		return redismap.get(ConstantNum.client_yanzmUrl).toString();
//	}
//	/**数据存入redis
//	 * 默认保存25分钟
//	 */
//	public static void setRedisMap(Map<String,Object> redismap,String key){
//		setRedisMap(redismap, key, 25);
//	}
//	/**数据存入redis
//	 * 默认保存分钟
//	 */
//	public static void setRedisMap(Map<String,Object> redismap,String key,int time){
//		redismap.put(ConstantNum.client_cookie, getClient(redismap).getCookieStore());
//		getClient(redismap).close();
//		redismap.remove(ConstantNum.client);
//		Redis.setEx(key, redismap,Redis.getMinute(time));
//	}
//	/**移除*/
//	public static void delRedisMap(String key){
//		Redis.del(key);
//	}
//	/**如果找不到键对应的值,返回空
//	 * @param redismap
//	 * @param cookieKey
//	 * @return
//	 */
//	public static String getCookieValue(Map<String,Object> redismap,String cookieKey){
//		CookieStore cs = (CookieStore) redismap.get(ConstantNum.client_cookie); 
//		List<Cookie> list = cs.getCookies();
//		String value = null;
//		for (int i = 0; i < list.size(); i++) {
//			value = list.get(i).getName();
//			if(cookieKey!=null&&cookieKey.equals(value)){
//				value = list.get(i).getValue();
//				break;
//			}else{
//				value = null;
//			}
//		}
//		return value;
//	}
//	/**如果找不到键对应的值,返回空,默认返回JSESSIONID的值
//	 * @param redismap
//	 * @param cookieKey
//	 * @return
//	 */
//	public static String getCookieValue(Map<String,Object> redismap){
//		return getCookieValue(redismap,"JSESSIONID");
//	}
//	/**
//	 * 切换代理ip 
//	 * @param redismap
//	 */
//	public static void setIp(Map<String,Object> redismap){
//		redismap.put(ConstantNum.client_taobaoip, IPUtils.setProxyId(getClient(redismap)));
//	}
//	/**存验证码值
//	 * @param redismap
//	 * @param value
//	 */
//	public static void setYZMValue(Map<String,Object> redismap,String value){
//		if(value!=null){
//			redismap.put(ConstantNum.client_yanzmValue,value);	
//		}
//	}
//	
//	/**
//	 * 取验证码值
//	 */
//	public static String getYZMValue(Map<String,Object> redismap){
//		return (String) redismap.get(ConstantNum.client_yanzmValue);
//	}
//	/**存验证码值 url
//	 */
//	public static void setYZMUrl(Map<String,Object> redismap,String value){
//		if(value!=null&&!"".equals(value)){
//			redismap.put(ConstantNum.client_yanzmUrl,value);	
//		}
//	}
//	
//	/**
//	 * 取淘宝验证码值
//	 */
//	public static String getTaobaoValue(Map<String,Object> redismap){
//		return (String) redismap.get(ConstantNum.client_taobaoUrl);
//	}
//	/**存淘宝验证码值 url
//	 */
//	public static void setTaobaoUrl(Map<String,Object> redismap,String value){
//		if(value!=null&&!"".equals(value)){
//			redismap.put(ConstantNum.client_taobaoUrl,value);	
//		}
//	}
//	
//	/**
//	 * 取验证码 url
//	 */
//	public static String getYZMUrl(Map<String,Object> redismap){
//		return (String) redismap.get(ConstantNum.client_yanzmUrl);
//	}
//	/**
//	 * 访问地址
//	 * @param redismap
//	 * @param url
//	 */
//	public static void setUrl(Map<String,Object> redismap,String url){
//		if(url!=null&&!"".equals(url)){
//			redismap.put(ConstantNum.client_url, url);
//		}
//	}
//	/**下一阶段访问地址
//	 * @param redismap
//	 * @return
//	 */
//	public static String getUrl(Map<String,Object> redismap){
//		return (String)redismap.get(ConstantNum.client_url);
//	}
//	/***
//	 * 得到步骤
//	 */
//	public static int getBuzhou(Map<String,Object> redismap){
//		Integer i = (Integer)redismap.get(ConstantNum.client_buzhou);
//		if(i==null){
//			i=0;
//		}
//		return i.intValue();
//	}
//	/**
//	 * 执行步骤
//	 * @param redismap
//	 * @param url
//	 */
//	public static void setBuzhou(Map<String,Object> redismap,Integer i){
//			redismap.put(ConstantNum.client_buzhou, i);
//	}
//	/**初始化验证码
//	 * @param redismap
//	 */
//	public static void initCookieStore(Map<String,Object> redismap){
//		CookieStore cs = (CookieStore) redismap.get(ConstantNum.client_cookie);
//		if(cs!=null){
//			getClient(redismap).setCookieStore(cs);
//		}
//	}
//	/**初始化验证码地址
//	 * 默认存放 none
//	 * @param redismap
//	 */
//	public static void initYZM(Map<String,Object> redismap){
//		if(redismap.get(ConstantNum.client_yanzmUrl)==null||"".equals(redismap.get(ConstantNum.client_yanzmUrl))){
//			redismap.put(ConstantNum.client_yanzmUrl, "none"); //默认url 为none	
//		}
//	}
//	
//	
//}
