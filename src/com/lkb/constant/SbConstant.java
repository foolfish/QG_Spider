package com.lkb.constant;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;

/*
 * 社保常规参数设置
 * */
public class SbConstant {
	//上海社保
	public static Map<String,CloseableHttpClient> shhttpClientMap = new HashMap<String,CloseableHttpClient>();	
	//深圳社保
	public static Map<String,CloseableHttpClient> szsbcloseClientMap = new HashMap<String,CloseableHttpClient>();	
	
}
