//package com.lkb.thirdUtil.base.pojo;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import com.lkb.bean.client.ResOutput;
//
//
//public class SimpleBean{
//	private Map<String,Object> map = new HashMap<String,Object>();
//
//	public synchronized void put(String key,ResOutput res){
//		map.put(key+"_v", res);
//	}
//	/**
//	 * @param key  查询键
//	 * @param value  设置查询步骤如 value=1或2或3等
//	 */
//	public synchronized void put(String key,int value){
//		map.put(key, value);
//	}
//	/**
//	 * 唯一标示
//	 * @param value  主要标示第几步,做key唯一标示
//	 * @return
//	 */
//	public String getKey(String key,int value){
//		return key+"_"+value;
//	}
//	public ResOutput getResOutput(String key){
//		return (ResOutput)map.get(key+"_v");
//	}
//	public Object get(String key){
//		return map.get(key);
//	}
//
//	public synchronized void remove(String key){
//		map.remove(key);
//	}
//	
//	/**自增,统一的key
//	 * @param key
//	 */
//	public  void increment(String key){
//		int i = getSize(key);
//		setInteger(key,++i);
//	}
//	/**统一的key
//	 */
//	public synchronized int getSize(String key){
//		Integer i  = (Integer)map.get(key+"_count");
//		return i==null?0:i;
//	}
//	private void setInteger(String key,int num){
//		if(num<0){
//			num = 0;
//		}
//		map.put(key+"_count", num);
//	}
//	/**自减
//	 * @param key
//	 */
//	public  void decrement(String key){
//		int i = getSize(key);
//		setInteger(key,--i);
//	}
//	
//	
//}
