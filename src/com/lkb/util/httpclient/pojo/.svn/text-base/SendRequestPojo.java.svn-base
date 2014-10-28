package com.lkb.util.httpclient.pojo;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.Args;

import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class SendRequestPojo {
	/**
	 * 设置请求POST方式
	 */
	public static final int POST = 0;
	/***
	 * 设置请求GET方式
	 */
	public static final int GET = 1;
	public static final Integer socketTimeout = 20000;//20秒
	public static final Integer connectTimeout = 20000;//20秒
	public static final String isproxy = InfoUtil.getInstance().getInfo("roadThread","SendRequestPojo.isproxy");
	public static final String proxy = "true"; 
	
	private String url;
	private Map<String,String> params;
	/**
	 * 请求方式,get or post
	 */
	private int method;
	
	private CHeader header;
	
	private boolean isUri = true;
	
	private String xmlsOrJsonEntity ;
	
	private Builder builder = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout); //httpClient 请求参数
	
	/**
	 * 是否禁用代理
	 */
	private boolean  forbid = true;
	private HttpRequestBase uriRequest;

	public SendRequestPojo(String url) {
		this.url = url;
		this.method = GET;
	}
	public SendRequestPojo(String url,CHeader h) {
		this.url = url;
		this.header = h;
		this.method = GET;
	}
	public SendRequestPojo(String url, CHeader header, boolean isUri) {
		this.url = url;
		this.method = GET;
		this.header = header;
		this.isUri = isUri;
	}
	public SendRequestPojo(String url, Map<String, String> params) {
		this.url = url;
		this.params = params;
		this.method = POST;
	}
	public SendRequestPojo(String url, Map<String, String> params,CHeader h) {
		this.url = url;
		this.params = params;
		this.header  = h;
		this.method = POST;
	}
	
	public SendRequestPojo(String url, Map<String, String> params, CHeader header, boolean isUri) {
		this.url = url;
		this.params = params;
		this.method = POST;
		this.header = header;
		this.isUri = isUri;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public int getMethod() {
		if(getXmlsOrJsonEntity()!=null){
			this.method = POST;
		}
		return method;
	}

	/**get请求or post请求 
	 * 格式: Request.GET/Request.POST
	 * @param method
	 */
	public void setMethod(int method) {
		this.method = method;
	}

	public CHeader getHeader() {
		if(header==null){
			header = new CHeader();
			header.setUser_Agent(CHeaderUtil.getUser_Agent());
		}
		return header;
	}

	public void setHeader(CHeader header) {
		this.header = header;
	}

	public boolean isUri() {
		return isUri;
	}

	public void setUri(boolean isUri) {
		this.isUri = isUri;
	}
	
	
	public String getXmlsOrJsonEntity() {
		return xmlsOrJsonEntity;
	}

	/**
	 * XML/json格式的post提交 	</br>
	 * 需要通过CHeader 设置请求头信息</br>
	 * HttpPost post = new HttpPost(url);</br>
	 *	StringEntity s = new StringEntity(xmls);</br>
	 *	if(c.getRespCharset()!=null){</br>
	 *		s.setContentEncoding("UTF-8");</br>
	 *	}else{</br>
	 *		s.setContentEncoding("GBK");</br>
	 *	}</br>
	 *	if(c.getContent_Type()!=null){</br>
	 *		s.setContentType(c.getContent_Type());</br>
	 *	}else{</br>
	 *		s.setContentType("application/json");</br>
	 *	}</br>
	 *	post.setEntity(s);</br>
	 * @param xmls
	 */
	public void setXmlsOrJsonEntity(String xmlsOrJsonEntity) {
		this.xmlsOrJsonEntity = xmlsOrJsonEntity;
	}
	/**
	 * 默认设置超时时间 
	 * 如果修改请求参数可直接使用该方法set对应的参数
	 * @return builder对象
	 */
	public Builder getBuilder() {
		if(forbid){
			Args.notEmpty(isproxy, "代理ip");
			if(proxy.equals(isproxy)){
//			 .setProxy(new HttpHost("myotherproxy", 8080))
				builder.setProxy(null);//回头改......
			}
		}
		return builder;
	}
//	/**
//	 * 默认设置超时时间 
//	 * 如果修改请求参数可直接使用该方法set对应的参数
//	 * @return builder对象
//	 */
//	public Builder setBuilder(Integer socketTimeout,Integer connectTimeout,String isproxy) {
//		return setBuilder(socketTimeout, connectTimeout, isproxy, true);
//	}
//		
//		/**
//		 * @param socketTimeout
//		 * @param connectTimeout  
//		 * @param isproxy  是否代理
//		 * @param redirectsEnabled 是否禁止重定向
//		 * @return
//		 */
//		public Builder setBuilder(Integer socketTimeout,Integer connectTimeout,String isproxy,boolean redirectsEnabled) {
//			if(builder==null){
//				builder = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout);
//				Args.notEmpty(isproxy, "代理ip");
//				if(proxy.equals(isproxy)){
////				 .setProxy(new HttpHost("myotherproxy", 8080))
//					builder.setProxy(null);//回头改......
//				}
//			}else{
//				if(socketTimeout!=null){
//					builder.setSocketTimeout(socketTimeout);
//				}
//				if(connectTimeout!=null){
//					builder.setConnectTimeout(connectTimeout);
//				}
//				if(proxy.equals(isproxy)){
//					builder.setProxy(null);//回头改......
//				}
//				builder.setRedirectsEnabled(redirectsEnabled);
//			}
//		return builder;
//	}
	/**禁用代理,当然是在代理使用的情况下
	 * @param b
	 */
	public Builder forbidProxy(){
		forbid = false;
		return builder;
	}
	
	/**禁止重定向
	 * @return
	 */
	public Builder forbidRedirect(){
		builder.setRedirectsEnabled(false);
		return builder;
	}
	
	/**默认毫秒
	 * @param socketTimeout
	 * @param connectTimeout
	 */
	public Builder setTimeOut(int socketTimeout,int connectTimeout){
		//setConnectionRequestTimeout(50)
		builder.setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout);
		return builder;
	}
	
	/**
	 * 返回request请求没有设置 RequestConfig
	 * @return
	 */
	public HttpRequestBase getHttpRequestBase(){
		if(uriRequest==null){
			URI uri = null;
			//判断是uri请求还是url请求
			if(isUri()){
				try {
					URL	u = new URL(this.url);
					uri = new URI(u.getProtocol(), u.getHost(), u.getPath(), u.getQuery(), null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(getMethod()==GET){
				if(uri!=null){
					uriRequest = new HttpGet(uri);
				}else{
					uriRequest = new HttpGet(this.url);
				}
			}else{
				if(uri!=null){
					uriRequest = new HttpPost(uri);
				}else{
					uriRequest = new HttpPost(this.url);
				}
			}
			//是否xml形式的post提交
			if(getXmlsOrJsonEntity()!=null){
				StringEntity s = null;
				try {
					s = new StringEntity(getXmlsOrJsonEntity());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if(getHeader().getRespCharset()!=null){
					s.setContentEncoding("UTF-8");
				}else{
					s.setContentEncoding("GBK");
				}
				if(getHeader().getContent_Type()!=null){
					s.setContentType(getHeader().getContent_Type());
				}else{
					s.setContentType("application/json");
				}
			}else{
				CHeaderUtil.setParam(uriRequest, getHeader(),this.params);
			}
		
		}
		return uriRequest;
	}
	

}
