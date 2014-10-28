package com.lkb.util.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.util.HttpUtil;
import com.lkb.util.proxyip.entity.TaobaoIp;

/**
 * get/post 执行类
 * @author fastw
 * @date	2014-8-13 下午3:18:21
 */
public class HttpRequest {
	public static final Logger log = Logger.getLogger(HttpRequest.class);
	public static CloseableHttpClient client =  ClientConnectionPool.getInstance();
	private HttpClientContext context;
	private String User_Agent;
	
	/***
	 * 设置请求GET方式
	 */
	public static final Integer socketTimeout = 20000;//20秒
	public static final Integer connectTimeout = 20000;//20秒
	public static final String isproxy = InfoUtil.getInstance().getInfo("roadThread","SendRequestPojo.isproxy");
	public static final String proxy = "true"; 
	private Builder builder = null;
	private HttpHost host = null;

	public HttpRequest( HttpClientContext context) {
		super();
		this.context = context;
		builder = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout); //httpClient 请求参数
		context.setRequestConfig(builder.build());//设置请求和传输超时时间
	}
	
	public RequestConfig getRequestConfig(){
		if(isproxy.equals(proxy)){
			if(host!=null){
				//TODO 切换ip
			}
			//else 已经切换ip了
		}
		return  builder.build();
	}
	public void setUniqueProxy(HttpHost proxy){
		this.host = proxy;
		builder.setProxy(proxy);
	}
	private CHeader h;
	private Map<String,String> param;
	private String url;
	private boolean isParse = true;//是否解析
	
	
	private String getUser_Agent(){
		if(User_Agent==null){
			this.User_Agent = CHeaderUtil.getUser_Agent();
		}
		return this.User_Agent;
	}
	/**
	 * 针对httpclient4.3
	 * @param url 不允许为空
	 * @param h 允许为空
	 * @return
	 */
	public String get(){
		return get(url);
	}
	public String get(String url,CHeader h){
		URI uri = null;
		HttpGet get = null;
		try {
			URL	u = new URL(url);
			uri = new URI(u.getProtocol(), u.getHost(), u.getPath(), u.getQuery(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(uri!=null){
			get = new HttpGet(uri);
		}else{
			get = new HttpGet(url);
		}
		get.setConfig(getRequestConfig());
		get.setHeader("User-Agent", getUser_Agent());
		this.h = h;
		CHeaderUtil.setParam(get, h,null);
		return execute(get);
	}
	/**
	 * 支付宝块,必须url
	 * @return
	 */
	public String getURL(String url,CHeader h){
		HttpGet get  = new HttpGet(url);
		get.setConfig(getRequestConfig());
		get.setHeader("User-Agent", getUser_Agent());
		this.h = h;
		CHeaderUtil.setParam(get, h,null);
		return execute(get);
	}
	public String get(String url){
		return get(url, h);
	}
	
	/**
	 * @param url	post的url地址
	 * @param header 请求头bean 存放请求头信息
	 * @param map 请求参数的键值关系
	 * @return
	 */
	public String post(String url,CHeader header,Map<String,String> map){
		// 开始登陆
		URI uri = null;
		HttpPost post = null;
		try {
			URL	u = new URL(url);
			uri = new URI(u.getProtocol(), u.getHost(), u.getPath(), u.getQuery(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(uri!=null){
			post = new HttpPost(uri);
		}else{
			post = new HttpPost(url);
		}
		
		post.setConfig(getRequestConfig());
		post.setHeader("User-Agent", getUser_Agent());
		this.h = header;
		CHeaderUtil.setParam(post, header,map);
		return execute(post);
		
	}
	public String postURL(String url,CHeader header,Map<String,String> map){
		// 开始登陆
		HttpPost post  = new HttpPost(url);
		post.setConfig(getRequestConfig());
		post.setHeader("User-Agent", getUser_Agent());
		this.h = header;
		CHeaderUtil.setParam(post, header,map);
		return execute(post);
		
	}
	/**
	 * @param url
	 * @param c
	 * @param xmls XML/json格式的post提交 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String post(String url,CHeader c,String xmls) throws UnsupportedEncodingException{
		if(xmls!=null&&c!=null){
			HttpPost post = new HttpPost(url);
			StringEntity s = new StringEntity(xmls);
			if(c.getRespCharset()!=null){
				s.setContentEncoding("UTF-8");
			}else{
				s.setContentEncoding("GBK");
			}
			if(c.getContent_Type()!=null){
				s.setContentType(c.getContent_Type());
			}else{
				s.setContentType("application/json");
			}
			post.setEntity(s);
			return execute(post);
		}else{
			return null;
		}
		
		
	}
	/**调用方式
	 * obj.setUrl(..).setParam(..).setCheader(..).post();
	 * 除了url和post不能为空以外;其他可为空
	 * @return
	 */
	public String post(){
		return post(url,h,param);
	}
	public String post(String url,Map<String,String> param){
		return post(url,null,param);
	}
	
	public HttpRequest isParse(boolean isParse) {
		this.isParse = isParse;
		return this;
	}
	
	public HttpRequest setUrl(String url) {
		this.url = url;
		return this;
	}
	public HttpRequest setCHeader(CHeader h) {
		this.h = h;
		return this;
	}
	
	/**map 参数
	 * @param param
	 */
	public HttpRequest setParam(Map<String, String> param) {
		this.param = param;
		return this;
	}
	/**设置是否重定向,true重定向,false不重定向
	 * @param isAuto
	 */
	public void setHandleRedirect(boolean isAuto){
		builder.setRedirectsEnabled(isAuto);
	}
	public String execute(HttpUriRequest request){
		int num = 0;
		boolean b = true;
		int i= 0;//响应头
		String text = null;
		while(b){ //默认重发三次
			try {
				HttpResponse  response = this.client.execute(request,this.context);
				i = response.getStatusLine().getStatusCode(); 
				if(i==200){
					if(isParse){
						if(h!=null&&h.getRespCharset()!=null){
							text = ParseResponse.parse(response,h.getRespCharset());
						}else{
							text = ParseResponse.parse(response);	
						}
					}else{
						text = "ok";
					}
				}else if(i==302||i==301){
					text = ParseResponse.getLocation(response);
				}else{
					b = false;
				}
			}catch (IOException e) {
				if(e instanceof NoHttpResponseException){
					b = false;
					request.abort();
				}else{
					log.error("URL:"+url, e);
					e.printStackTrace(); //回头修改成具体异常那些需要重发
				}
				
			}
			if(text!=null&&b){
				b = false;
				request.abort();
			}else{
				num++;
				if(num>4){
					request.abort();
					b = false;
				}
			}
			
		}
		return  text;
	}
	
	
	/**
	 * @param url 验证码地址
	 * @param client
	 * @param path 输出路径
	 * @return
	 */
	public boolean  downimgCode(String url,String path){
		String referer = HttpUtil.getHomePage(url);
		return downimgCode(url,new CHeader(referer),path);
	}
	/**
	 * @param url 验证码地址
	 * @param path  输出路径
	 * @return 正确错误
	 */
	public  boolean  downimgCode(String url,CHeader h,String path){
		boolean b = false;
		// 输出的文件流  
		OutputStream os = null;
		InputStream is = null;
		try {
			HttpGet	get = new HttpGet(url);
			get.setConfig(getRequestConfig());
			get.setHeader("User-Agent", getUser_Agent());
			CHeaderUtil.setParam(get, h,null);
			HttpResponse  response = client.execute(get,this.context);
			// 输入流
			is = response.getEntity().getContent();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流  
			os = new FileOutputStream(path);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			b = true;
			// 完毕，关闭所有链接
		}  catch (Exception e) {
			e.printStackTrace();
			log.error("URL:"+url, e);
		}finally{
			try {
				if(os!=null){
					os.close();
				}
				if(is!=null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return b;
	}
	public  byte[]  downimgCode(String url){
		String referer = HttpUtil.getHomePage(url);
		return downimgCode(url, new CHeader(referer));
	}
	public  byte[]  downimgCode(String url,CHeader h){
		byte[] in_b = null;
		// 输出的文件流  
		OutputStream os = null;
		InputStream is = null;
		try {
			HttpGet	get = new HttpGet(url);
			get.setConfig(getRequestConfig());
			get.setHeader("User-Agent", getUser_Agent());
			CHeaderUtil.setParam(get, h,null);
			HttpResponse  response = client.execute(get,this.context);
			// 输入流
			is = response.getEntity().getContent();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				swapStream.write(bs, 0, len);
			}
			in_b = swapStream.toByteArray();
			// 完毕，关闭所有链接
		}  catch (Exception e) {
			e.printStackTrace();
			log.error("URL:"+url, e);
		}finally{
			try {
				if(os!=null){
					os.close();
				}
				if(is!=null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return in_b;
	}
}
