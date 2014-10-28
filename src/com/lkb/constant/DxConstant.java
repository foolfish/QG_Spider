package com.lkb.constant;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/*
 * 中国电信常规参数设置
 * */
public class DxConstant {
	public static Map<String,CloseableHttpClient> sh_dxcloseClientMap = new HashMap<String,CloseableHttpClient>();
	public static Map<String,DefaultHttpClient> sh_dxcloseClientMap1 = new HashMap<String,DefaultHttpClient>();

	// 北京电信
	public static Map<String,CloseableHttpClient> bj_dxcloseClientMap = new HashMap<String,CloseableHttpClient>();
	// 重庆电信
	public static Map<String,CloseableHttpClient> cq_dxcloseClientMap = new HashMap<String,CloseableHttpClient>();
	// 福建电信
	public static Map<String,Map> fj_dxcloseClientMap = new HashMap<String,Map>();
	// 浙江电信
	public static Map<String,Map> zjMap = new HashMap<String,Map>();
	
	// 浙江电信
	public static Map<String,Map> jsMap = new HashMap<String,Map>();
	
	// 天津电信
	public static Map<String,Map> tj_dxMap = new HashMap<String,Map>();
	
	// 甘肃电信
	public static Map<String,Map> gs_dxMap = new HashMap<String,Map>();
	
	// 四川电信
	public static Map<String,Map> sc_dxMap = new HashMap<String,Map>();
	
	// 山东电信
	public static Map<String,Map> sd_dxMap = new HashMap<String,Map>();
	
}
