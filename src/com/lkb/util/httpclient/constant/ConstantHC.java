package com.lkb.util.httpclient.constant;

import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;

public class ConstantHC {
	/***
	 * httpclient 的 key
	 */
	public static String k_client = "k_client"; 
	/***
	 * 公共验证码键
	 */
	public static String k_vcode = "k_vcode"; 
	/***
	 * 手机号/淘宝/京东等账户
	 */
	public static String k_username = "t_username"; 
	/**
	 * 手机号/淘宝/京东等密码
	 */
	public static String k_pass = "k_pass"; 
	
	/***
	 * 必要时设置cookie
	 */
	public static String k_cookie = "k_cookie"; 
	/**
	 * 防止到处都是强制转换,此处统一获取值
	 * @param map
	 * @return
	 */
	public static DefaultHttpClient getClient(Map<String,Object> map){
		return (DefaultHttpClient) map.get(k_client);
	}
	/**
	 * 获取验证码
	 * @param map
	 * @return
	 */
	public static String getVcode(Map<String,Object> map){
		if( map.get(k_vcode)!=null){
			return  map.get(k_vcode).toString();
		}
		return null;
	}
	/**
	 * 返回用户名
	 * @param map
	 * @return
	 */
	public static String getUsername(Map<String,Object> map){
		if( map.get(k_username)!=null){
			return  map.get(k_username).toString();
		}
		return null;
	}
	/**
	 * 返回密码
	 * @param map
	 * @return
	 */
	public static String getPass(Map<String,Object> map){
		if( map.get(k_pass)!=null){
			return  map.get(k_pass).toString();
		}
		return null;
	}
	public static String getCookie(Map<String,Object> map){
		if( map.get(k_cookie)!=null){
			return  map.get(k_cookie).toString();
		}
		return null;
	}
}
