package com.lkb.util;

import java.io.IOException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class IPUtil {
	private static String ip138 = "http://20140507.ip138.com/ic.asp";
	private static String chinaz ="http://ip.chinaz.com/";
	private static String ip38s ="http://www.ip38.com/";
	
	public static void main(String[] args){
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		IPUtil ipUtil = new IPUtil();
		//String content = ipUtil.getIP138(httpclient,ip138);
		
		System.out.print(ipUtil.getIP());
		
		
		
	}
	


	
	
	public String getIP(){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String userIp = getIP138( httpclient,ip138);
		try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userIp;
		
	}
	
	public String getIP138s(CloseableHttpClient httpclient,String url ){
		String content = getText(httpclient, url);
		Document doc = Jsoup.parse(content);
		String mm = doc.select("h2").first().select("li").first().text();
		String[] ss=mm.split(" ");
		return ss[1];
	}
	
	

	
	public String getIP138(CloseableHttpClient httpclient,String url ){
		String ipcontent = getText(httpclient,url);
		Document doc = Jsoup.parse(ipcontent);
		String content = doc.select("body").first().select("center").first().text();
		String[] strs = content.split("\\u005B");
		String[] strs2 = strs[1].split("]");
		return strs2[0];
	}
	
	/*
	 * 得到网页的内容
	 * */
	private String getText(CloseableHttpClient httpclient,String redirectLocation) {
		HttpGet httpget = new HttpGet(redirectLocation);
		httpget.addHeader("Accept-Charset", "gb2312,utf-8");  
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
