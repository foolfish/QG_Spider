package com.lkb.util;

import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

public class ProxyUtil {

	private static String loginUrl = "http://passport.jd.com/uc/login";
	public static void main(String[] args) throws Exception {
		HttpHost proxy = new HttpHost("58.68.246.12", 18080);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
		HttpGet httpget = new HttpGet(loginUrl);
		httpget.setHeader("Host","passport.jd.com");
        System.out.println(getText( httpclient, httpget));
        
	}
	
	
	private static String getText(CloseableHttpClient httpclient,HttpGet httpget) {
		httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		httpget.setHeader("Referer","http://www.jd.com");
		httpget.setHeader("Connection","Keep-Alive");
		httpget.setHeader("Accept-Language","zh-CN");
		httpget.setHeader("Accept-Encoding","gzip, deflate");
		httpget.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		httpget.setHeader("X-Requested-With","XMLHttpRequest");
		httpget.setHeader("Accept","*/*");
		httpget.setHeader("DNT","1");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}
	
	

}