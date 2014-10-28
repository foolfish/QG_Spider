package com.lkb.util.httpclient.entity;

/**
 * httpClient请求头实体bean
 * @author fastw
 *
 */
public class CHeader {
	/**服务器接受类型*/
	private String Accept;
	/**引用那个地址过来*/
	private String Referer;
	/**服务器接受类型编码 gzip.default*/
	private String Accept_Encoding;
	/**服务器接受类型语言*/
	private String Accept_Language;
	/**是否有缓存*/
	private String Cache_Control;
	/**连接状态 keep-alive*/
	private String Connection;
	/**文本格式渲染格式*/
	private String Content_Type;
	private String Host;
	/**xml request*/
	private Boolean x_requested_with;
	//默认就行
	private String User_Agent;
	/**响应解析编码*/
	private String respCharset;
	
	/***
	 * cookie信息
	 */
	private String cookie;
	
	public CHeader(){}
	public CHeader(String accept,  String content_Type, String host) {
		Accept = accept;
		Content_Type = content_Type;
		Host = host;
	}
	public CHeader(String referer, Boolean x_requested_with) {
		this.Referer = referer;
		this.x_requested_with = x_requested_with;
	}
	/**
	 * @param referer 设置referer
	 */
	public CHeader(String referer) {
		this.Referer = referer;
	}
	public CHeader(String accept, String referer, String content_Type,
			String host) {
		super();
		Accept = accept;
		Referer = referer;
		Content_Type = content_Type;
		Host = host;
	}
	public CHeader(String accept, String referer, String content_Type,
			String host, Boolean x_requested_with) {
		super();
		Accept = accept;
		Referer = referer;
		Content_Type = content_Type;
		Host = host;
		this.x_requested_with = x_requested_with;
	}
	public String getAccept() {
		return Accept;
	}
	/**服务器接受类型*/
	public void setAccept(String accept) {
		Accept = accept;
	}
	public String getReferer() {
		return Referer;
	}
	/**引用那个地址过来*/
	public void setReferer(String referer) {
		Referer = referer;
	}
	public String getAccept_Encoding() {
		return Accept_Encoding;
	}
	/**服务器接受类型编码 gzip.default ,可为空*/
	public void setAccept_Encoding(String accept_Encoding) {
		Accept_Encoding = accept_Encoding;
	}
	public String getAccept_Language() {
		return Accept_Language;
	}
	/**服务器接受类型语言 可为空默认zh-cn*/
	public void setAccept_Language(String accept_Language) {
		Accept_Language = accept_Language;
	}
	public String getCache_Control() {
		return Cache_Control;
	}
	/**是否有缓存,可为空*/
	public void setCache_Control(String cache_Control) {
		Cache_Control = cache_Control;
	}
	public String getConnection() {
		return Connection;
	}
	/**连接状态 keep-alive,可为空*/
	public void setConnection(String connection) {
		Connection = connection;
	}
	public String getContent_Type() {
		return Content_Type;
	}
	/**文本格式渲染格式*/
	public void setContent_Type(String content_Type) {
		Content_Type = content_Type;
	}
	public String getHost() {
		return Host;
	}
	public void setHost(String host) {
		Host = host;
	}
	public Boolean getX_requested_with() {
		return x_requested_with;
	}
	/**xml request*/
	public void setX_requested_with(Boolean x_requested_with) {
		this.x_requested_with = x_requested_with;
	}
	public String getUser_Agent() {
		return User_Agent;
	}
	public void setUser_Agent(String user_Agent) {
		User_Agent = user_Agent;
	}
	
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	/**
	 * @return 编码
	 */
	public String getRespCharset() {
		return respCharset;
	}
	/**
	 * 设置响应编码
	 */
	public void setRespCharset(String respCharset) {
		this.respCharset = respCharset;
	}
	
	
}
