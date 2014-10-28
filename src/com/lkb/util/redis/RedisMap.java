package com.lkb.util.redis;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.CookieStore;

import com.lkb.constant.ConstantNum;
import com.lkb.util.httpclient.pojo.SimpleData;

public class RedisMap {
	
	public static Map<String,Object> getInstance(String key){
		Map<String,Object> map = (Map<String, Object>) Redis.getObj(key);
		if(map==null){
			map = new HashMap<String,Object>();
			//如果不用十分钟过期
			Redis.setEx(key,map,Redis.getMinute(10));
		}
		return map;
	}
	public static SimpleData readRedis(String key){
		SimpleData simple = (SimpleData) Redis.getObj(key);
		if(simple==null){
			simple = new SimpleData();
		}
		return simple;
	}
	public static CookieStore getCookieStore(SimpleData simple,String key){
		return (CookieStore) simple.get(key);
	}
	public static void setCookieStore(Map<String,Object> redismap,CookieStore cs){
		if(cs!=null&&cs.getCookies()!=null&&cs.getCookies().size()!=0){
			redismap.put(ConstantNum.client, cs);	
		}
	}
	public static CookieStore getCookieStore(Map<String,Object> redismap){
		return (CookieStore) redismap.get(ConstantNum.client);
	}
	
	/**数据写入redis ,如果redismap的size==0,那么将删除redis
	 * @param redismap
	 * @param key
	 */
	public static void writeRedis(Map<String,Object> redismap,String key){
		writeRedis(redismap, key, 15);
	}
	/**
	 * 数据写入redis ,如果redismap的size==0,那么将删除redis
	 * @param seconds 默认多少分钟
	 */
	public static void writeRedis(Map<String,Object> redismap,String key,int seconds){
		if(redismap.isEmpty()){
			Redis.del(key);
		}else{
			Redis.setEx(key, redismap,Redis.getMinute(seconds));
		}
	}
	/**移除*/
	public static void delRedisMap(String key){
		Redis.del(key);
	}
	
}
