package com.lkb.util.httpclient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import com.lkb.util.httpclient.entity.CHeader;



/**
 * client 请求头模板
 * @author fastw
 *
 */
public class CHeaderUtil {
	/**application/x-ms-application, application/xaml+xml, application/x-ms-xbap格式*/
	public static String Accept_x_ms_application = "application/x-ms-application, application/xaml+xml, application/x-ms-xbap";
	/**application/json, text/javascript  json 格式*/
	public static String Accept_json = "application/json, text/javascript, */*; q=0.01";
	public static String Accept_js = "application/javascript, */*;q=0.8";

	/**标准的accept头*/
	public static String Accept_ = "text/html, application/xhtml+xml, */*";
	public static String Accept_other = "*/*";
	
	public static String Accept_Encoding = "gzip, deflate";
	public static String Accept_Language = "zh-CN";
	public static String Cache_Control = "no-cache";
	public static String Connection = "Keep-Alive";
//	public static String User_Agent = "Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch)";
	
	public static List<String> User_Agent = null;
	static{
		User_Agent = new ArrayList<String>();
		User_Agent.add("Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		User_Agent.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153");
		User_Agent.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11");
		User_Agent.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER");
		User_Agent.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)");
		User_Agent.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)");
		User_Agent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; 360SE)");
		User_Agent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
		User_Agent.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)");
		User_Agent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; 360SE)");
		User_Agent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
		User_Agent.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
		User_Agent.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
		User_Agent.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)");
		User_Agent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
		User_Agent.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
		User_Agent.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0");
		User_Agent.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; SE 2.X MetaSr 1.0)");
		User_Agent.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:16.0) Gecko/20121026 Firefox/16.0");
		User_Agent.add("Mozilla/5.0 (iPad; U; CPU OS 4_2_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5");
		User_Agent.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:2.0b13pre) Gecko/20110307 Firefox/4.0b13pre");
//		User_Agent.add("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:16.0) Gecko/20100101 Firefox/16.0");
//		User_Agent.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
		User_Agent.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
		User_Agent.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16");
		User_Agent.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
		User_Agent.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
//		User_Agent.add("Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10");
	}
	
	
	
	/**application/x-www-form-urlencoded形式*/
	public static String Content_Type__urlencoded = "application/x-www-form-urlencoded";
	public static String x_requested_with = "XMLHttpRequest";
	

	/**
	 * @param url	post的url地址
	 * @param header 请求头bean 存放请求头信息
	 * @param map 请求参数的键值关系
	 * @return
	 */
	public static HttpPost getPost(String url,CHeader header,Map<String,String> map){
		// 开始登陆
		HttpPost post = new HttpPost(url);
		if(header!=null){
			putHeader(header, post);	
		}else{
			post.setHeader("User-Agent",User_Agent.get(new Random().nextInt(User_Agent.size()-1)));//随机取值
		}
		if(map!=null){
			setMap(map,post);
		}
		return post;
	}
	public static void putHeader(CHeader h,HttpRequestBase base){
		// 网页头信息
		base.setHeader("Accept", h.getAccept());
		if(h.getReferer()!=null){
			base.setHeader("Referer",h.getReferer());
		}
		if(h.getAccept_Encoding()!=null){
			base.setHeader("Accept-Encoding", h.getAccept_Encoding());
		}
		if(h.getHost()!=null){
			base.setHeader("Host",h.getHost());
		}
		if(h.getX_requested_with()!=null&&h.getX_requested_with()){
			base.setHeader("x_requested_with",x_requested_with);
		}
		
		if(h.getCookie()!=null){
			base.setHeader("Cookie",h.getCookie());
		}
		
		
		if(h.getAccept_Language()!=null){
			base.setHeader("Accept-Language", h.getAccept_Language());
		}else{
			base.setHeader("Accept-Language", Accept_Language);
		}
		if( h.getCache_Control()!=null){
			base.setHeader("Cache-Control", h.getCache_Control());
		}else{
			base.setHeader("Cache-Control", Cache_Control);
		}
		if(h.getConnection()!=null){
			base.setHeader("Connection", h.getConnection());
		}else{
			base.setHeader("Connection", Connection);
		}
		if( h.getContent_Type()!=null){
			base.setHeader("Content-Type", h.getContent_Type());
		}else{
			base.setHeader("Content-Type",Content_Type__urlencoded);
		}
		if(h.getUser_Agent()!=null){
			base.setHeader("User-Agent",h.getUser_Agent());
		}else{
			base.setHeader("User-Agent",User_Agent.get(new Random().nextInt(User_Agent.size()-1)));//随机取值
		}
			
	}
	private static void setMap(Map<String,String> map,HttpPost base){
		if(map!=null){
		List<NameValuePair> formParams = new ArrayList<NameValuePair>(); //构建POST请求的表单参数 
	      for(Map.Entry<String,String> entry : map.entrySet()){ 
	          formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue())); 
	      } 
	      UrlEncodedFormEntity params = null;
			try {
				params = new UrlEncodedFormEntity(formParams, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 base.setEntity(params);
		}
	}
	/**
	 * @param url 不允许为空
	 * @param h 允许为空
	 * @return
	 */
	public static  HttpGet getHttpGet(String url,CHeader h){
		HttpGet get = new HttpGet(url);
		if(h==null){
			get.setHeader("User-Agent",User_Agent.get(new Random().nextInt(User_Agent.size()-1)));//随机取值
		}else{
			putHeader(h, get);	
		}
		return get;
	}
	
	
	/** 设置普通参数
	 * @param base
	 * @param header
	 */
	public static void setParam(HttpRequestBase base,CHeader header,Map<String,String> map){
		if(header!=null){
//			if(header.getConnectTimeout()!=0){
//				base.setConfig(RequestConfig.custom().setSocketTimeout(20*1000).setConnectTimeout(header.getConnectTimeout()*1000).build());
//			}
//			if(header.getTimeout()!=0){
//				base.setConfig(RequestConfig.custom().setSocketTimeout(header.getTimeout()*1000).setConnectTimeout(20*1000).build());
//			}
//			if(header.getTimeout()!=0&&header.getConnectTimeout()!=0){
//				base.setConfig(RequestConfig.custom().setSocketTimeout(header.getTimeout()*1000).setConnectTimeout(header.getConnectTimeout()*1000).build());
//			}
			putHeader(header, base);	
		}
		if(map!=null){
			setMap(map,(HttpPost) base);
		}
	}
	
	public static String getUser_Agent(){
		return User_Agent.get(new Random().nextInt(User_Agent.size()-1));
	}
	
}
