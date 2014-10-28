package com.lkb.util.httpclient;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.lkb.util.httpclient.entity.CHeader;

	 
	/**
	 * @see =====================================================================================================
	 * @see 在开发HTTPS应用时，时常会遇到两种情况
	 * @see 1、要么测试服务器没有有效的SSL证书,客户端连接时就会抛异常
	 * @see    javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
	 * @see 2、要么测试服务器有SSL证书,但可能由于各种不知名的原因,它还是会抛一个堆烂码七糟的异常
	 * @see =====================================================================================================
	 * @see 由于我们这里使用的是HttpComponents-Client-4.1.2创建的连接，所以，我们就要告诉它使用一个不同的TrustManager
	 * @see TrustManager是一个用于检查给定的证书是否有效的类
	 * @see SSL使用的模式是X.509....对于该模式,Java有一个特定的TrustManager,称为X509TrustManager
	 * @see 所以我们自己创建一个X509TrustManager实例
	 * @see 而在X509TrustManager实例中，若证书无效，那么TrustManager在它的checkXXX()方法中将抛出CertificateException
	 * @see 既然我们要接受所有的证书,那么X509TrustManager里面的方法体中不抛出异常就行了
	 * @see 然后创建一个SSLContext并使用X509TrustManager实例来初始化之
	 * @see 接着通过SSLContext创建SSLSocketFactory，最后将SSLSocketFactory注册给HttpClient就可以了
	 * @see =====================================================================================================
	 * @create 6.21, 2014 11:11:52 PM
	 * @author fastw
	 */ 
	public class CUtil { 
		public static Logger log = Logger.getLogger(CUtil.class);
		
	    /**
	     * 初始化httpClint 并且是https形式
	     * 项目中直接返回httpclient对象
	     */ 
	    public static DefaultHttpClient init(){ 
	        X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager 
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
	            public X509Certificate[] getAcceptedIssuers() { return null; } 
	        }; 
	        try { 
	            //TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext 
	            SSLContext ctx = SSLContext.getInstance("TLS"); 
	            //使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用 
	            ctx.init(null, new TrustManager[]{xtm}, null); 
	            //创建SSLSocketFactory 
	            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx); 
	            //多线程
//	            ThreadSafeClientConnManager  connectionManager =  new ThreadSafeClientConnManager ();
//	            connectionManager.setMaxTotal(10);
//	            httpClient = new DefaultHttpClient(connectionManager); //创建默认的httpClient实例 
	            //通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上 
	            return setParam(socketFactory);
	        } catch (KeyManagementException e) { 
	            e.printStackTrace(); 
	        } catch (NoSuchAlgorithmException e) { 
	            e.printStackTrace(); 
	        } catch (ParseException e) { 
	            e.printStackTrace(); 
	        } 
			return createDefaultClient(); 
	    } 
	    /**
	     * @return 非安全连接
	     */
	    public static DefaultHttpClient createDefaultClient(){
	    	 return setParam(null);
	    }
	    private static DefaultHttpClient setParam(SSLSocketFactory sslSocketFactory){
	    	
	    	 HttpParams params = new BasicHttpParams();  
	         HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
	         HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);  
	         HttpProtocolParams.setUseExpectContinue(params, true);  
	         HttpConnectionParams.setConnectionTimeout(params, 20*1000);
	         HttpConnectionParams.setSoTimeout(params, 20*1000);
	         
	         SchemeRegistry schReg = new SchemeRegistry();  
	         schReg.register(new Scheme("http",PlainSocketFactory.getSocketFactory(),80));  
	         if(sslSocketFactory==null){
	        	  schReg.register(new Scheme("https",SSLSocketFactory.getSocketFactory(),443));  
	         }else{
	        	 schReg.register(new Scheme("https",sslSocketFactory,443));  
	         }
	       
	         ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
	    	 DefaultHttpClient httpClient = new DefaultHttpClient(conMgr,params);
//	    	  httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
//	            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
	            HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {  
	                public boolean retryRequest(  
	                        IOException exception,   
	                        int executionCount,  
	                        HttpContext context) {  
	                    if (executionCount >= 5) {  
	                        // Do not retry if over max retry count  
	                        return false;  
	                    }  
	                    if (exception instanceof InterruptedIOException) {  
	                        // Timeout  
	                        return false;  
	                    }  
	                    if (exception instanceof NoHttpResponseException) {   
	                    	// 如果服务器丢掉了连接，那么就重试   
	                    	return true;   
	                    }   
	                    if (exception instanceof UnknownHostException) {  
	                        // Unknown host  
	                        return false;  
	                    }  
//	                    if (exception instanceof ConnectException) {  
//	                        // Connection refused  
//	                        return false;  
//	                    }  
	                    if (exception instanceof SSLException) {  
	                        // SSL handshake exception  
	                        return false;  
	                    }  
	                    HttpRequest request = (HttpRequest) context.getAttribute(  
	                            ExecutionContext.HTTP_REQUEST);  
	                    boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);   
	                    if (idempotent) {  
	                        // Retry if the request is considered idempotent   
	                        return true;  
	                    }  
	                    return false;  
	                }  
	            };  
	            httpClient.setHttpRequestRetryHandler(myRetryHandler);
	            return httpClient;
	    }
	   
	  
	    /**关闭连接*/
	    public static void colse(HttpClient client){
	    	client.getConnectionManager().shutdown();
	    }
//	    private static DefaultHttpClient client = null;
	    
//	    static{
//	    	client = getHttpClient();
//	    }
//	    public static DefaultHttpClient getInstance(){
//	    	return client;
//	    }
	    /** 
	     * 适合多线程的HttpClient,用httpClient4.2.1实现 
	     * @return DefaultHttpClient 
	     */  
//	    public static DefaultHttpClient getHttpClient(){         
//	        // 设置组件参数, HTTP协议的版本 
//	        HttpParams params = new BasicHttpParams();   
//	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);   
//	        HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");   
//	        HttpProtocolParams.setUseExpectContinue(params, true);      
//	      
//	        X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager 
//	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
//	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
//	            public X509Certificate[] getAcceptedIssuers() { return null; } 
//	        }; 
//	        //TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext 
//            SSLContext ctx;
//            SSLSocketFactory socketFactory = null;
//			try {
//				ctx = SSLContext.getInstance("TLS");
//            //使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用 
//				ctx.init(null, new TrustManager[]{xtm}, null); 
//				 //创建SSLSocketFactory 
//	            socketFactory = new SSLSocketFactory(ctx); 
//	            //通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上 
//			}catch (KeyManagementException e) { 
//	            e.printStackTrace(); 
//	        } catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			} catch (ParseException e) { 
//	            e.printStackTrace(); 
//	        } 
//			
//	        //设置连接超时时间   
//	        int REQUEST_TIMEOUT = 30*1000;  //设置请求超时10秒钟   
//	        int SO_TIMEOUT = 30*1000;       //设置等待数据超时时间10秒钟   
//	        //HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);  
//	        //HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);  
//	        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);    
//	        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);   
//	        
//	        //设置访问协议   
//	        SchemeRegistry schreg = new SchemeRegistry();    
//	        schreg.register(new Scheme("http",80,PlainSocketFactory.getSocketFactory()));   
////	        schreg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));         
//	        schreg.register(new Scheme("https", 443, socketFactory));
//	          
//	        //多连接的线程安全的管理器   
//	        PoolingClientConnectionManager pccm = new PoolingClientConnectionManager(schreg); 
//	        pccm.setDefaultMaxPerRoute(100); //每个主机的最大并行链接数   
//	        pccm.setMaxTotal(300);          //客户端总并行链接最大数      
//	          
//	        DefaultHttpClient httpClient = new DefaultHttpClient(pccm, params); 
//	        return httpClient;  
//	    }  
//	    
	    
	    /** 适合做api授权时适用
	     *  用安卓内置的httpclient包的用法(ClientConnectionManager线程安全的连接管理类)：
	     * @return DefaultHttpClient 
	     */  
//	    public static synchronized DefaultHttpClient getHttpClient1()   {         
//	        if(client == null)  
//	        {                     
//	            // 设置组件参数, HTTP协议的版本,1.1/1.0/0.9   
//	            HttpParams params = new BasicHttpParams();   
//	            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);   
//	            HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");   
//	            HttpProtocolParams.setUseExpectContinue(params, true);      
//	      
//	            //设置连接超时时间   
//	            int REQUEST_TIMEOUT = 10 * 1000;    //设置请求超时10秒钟   
//	            int SO_TIMEOUT = 10 * 1000;         //设置等待数据超时时间10秒钟   
//	            HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);  
//	            HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);  
//	            ConnManagerParams.setTimeout(params, 1000); //从连接池中取连接的超时时间  
//	                
//	            //设置访问协议   
//	            SchemeRegistry schreg = new SchemeRegistry();    
//	            schreg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));   
//	            schreg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));       
//	                          
//	            // 使用线程安全的连接管理来创建HttpClient    
//	            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schreg);    
//	            client = new DefaultHttpClient(conMgr, params);   
//	        }  
//	        return httpClient;  
//	    }  
//	    
	    
	 // 设置是否重由httpclient自动管理跳转
		public static void setHandleRedirect(HttpClient client,
				boolean isAuto) {
			if (isAuto) 
				client.getParams().setParameter(
						ClientPNames.HANDLE_REDIRECTS, true);
			 else 
				client.getParams().setParameter(
						ClientPNames.HANDLE_REDIRECTS, false);
			
		}
		/** 移除代理
		 * @param client
		 */
		public static void removePROXY(HttpClient client){
			client.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
		}
		/***
		 * 不涉及到请求头
		 * @param url  访问地址
		 * @param client	
		 * @return 返回get方式 的响应 
		 */
		@Deprecated
		public static HttpResponse getHttpGet(String url,DefaultHttpClient client){
			return getHttpGet(url, new CHeader(), client);
		}
		
		
		/**
		 * 解析后字符串
		 * text==null 失败!
		 */
		public static String getMethodGet(String url,HttpClient client){
			return  getHttpGet(url, client,null,true);
		}
		
		/**
		 *解析后字符串
		 * text==null 失败!
		 * @param isParse 默认true解析页面
		 * @return
		 */
		public static String getMethodGet(String url,HttpClient client,boolean isParse){
			return  getHttpGet(url, client,null,isParse);
		}
		/***
		 * 解析后字符串
		 * text==null 失败!
		 */
		public static String getMethodGet(String url,HttpClient client,CHeader h){
			return  getHttpGet(url, client, h,true);
		}
		
		/**
		 * 涉及到请求头
		 * 默认解析后字符串
		 * text==null,失败,
		 * @param isParse 是否解析页面,默认解析
		 * @return
		 */
		public static String getHttpGet(String url,HttpClient client,CHeader h,boolean isParse){
			HttpGet	get = CHeaderUtil.getHttpGet(url, h);	
			return execute(get, client,isParse,h);
		}
		public static String execute(HttpUriRequest request,HttpClient client,boolean isParse,CHeader h){
			int i= 0;
			String text = null;
			try {
				HttpResponse  response = client.execute(request);
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
				}else if(i==302){
					text = ParseResponse.getLocation(response);
				}
			}catch (IOException e) {
				e.printStackTrace();
				log.error("URL:"+request.getURI(), e);
			}
			request.abort();
//			HttpResponse response = getHttpGet(url, new CHeader(), client);
//			if(client instanceof CloseableHttpClient){
			return  text;
		}
		/**
		 * 解析后字符串
		 * text==null 失败!
		 */
		public static String getMethodPost(String url,HttpClient client,Map<String,String> param){
			return  getPost(url,client, null,param,true);
		}
		/***
		 * 解析后字符串
		 * text==null 失败!
		 */
		public static String getMethodPost(String url,HttpClient client,Map<String,String> param,boolean isParse){
			return  getPost(url, client,null,param,isParse);
		}
		/***
		 * 解析后字符串
		 * text==null 失败!
		 */
		public static String getMethodPost(String url,HttpClient client,CHeader h,Map<String,String> param){
			return  getPost(url,  client,h,param,true);
		}
		
		/**
		 * 涉及到请求头
		 * 默认解析后字符串
		 * text==null,失败,
		 * @param isParse 是否解析页面,默认解析
		 * @return
		 */
		public static String getPost(String url,HttpClient client,CHeader h,Map<String,String> param,boolean isParse){
			HttpPost post = CHeaderUtil.getPost(url, h, param);
			return execute(post, client,isParse,h);
		}
		
		
		/***
		 * @param url  访问地址
		 * @param h		访问请求头
		 * @param client	
		 * @return 返回get方式 的响应
		 */
		@Deprecated
		public static HttpResponse getHttpGet(String url,CHeader h,DefaultHttpClient client){
			HttpGet	get = CHeaderUtil.getHttpGet(url, h);
			try {
				HttpResponse  response = client.execute(get);
				if(response.getStatusLine().getStatusCode() == 200||response.getStatusLine().getStatusCode()==302){ 
					return response;
				}else{
					get.abort();
				}
			}  catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		/***
		 * @param url  访问地址
		 * @param client	
		 * @return 返回post方式 的响应
		 */
		@Deprecated
		public static HttpResponse getPost(String url,DefaultHttpClient client,Map<String,String> param){
			return getPost(url, new CHeader(), client, param);
		}
		/***
		 * @param url  访问地址
		 * @param h		访问请求头
		 * @param client	
		 * @return 返回post方式 的响应
		 */
		@Deprecated
		public static HttpResponse getPost(String url,CHeader h,DefaultHttpClient client,Map<String,String> param){
			HttpPost post = CHeaderUtil.getPost(url, h, param);
			try {
				HttpResponse  response = client.execute(post);
				// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求 
				if(response.getStatusLine().getStatusCode() == 200||response.getStatusLine().getStatusCode()==302){ 
					return response;
				}else{
					post.abort();
				}
			}  catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/**
		 * @param url 验证码地址
		 * @param client
		 * @param path 输出路径
		 * @return
		 */
		@Deprecated
		public static boolean  downimgCode(String url,HttpClient client,String path){
			String referer = getHomePage(url);
			return downimgCode(url,new CHeader(referer), client,path);
		}
		/**
		 * @param url 根据url 获取网站主页
		 * @return
		 */
		public static String getHomePage(String url)
		{
		    Pattern p = Pattern.compile("(http://|https://)?([^/|?]*)",Pattern.CASE_INSENSITIVE);
		    Matcher m = p.matcher(url);
		    String s = "";
		    for (int i = 0; i < 2; i++) {
		    	m.find();
		    	s = s+ m.group();
			}
		    return s;
		}

		/**
		 * @param url 验证码地址
		 * @param h 	验证码头部
		 * @param client
		 * @param path  输出路径
		 * @return 正确错误
		 */
		@Deprecated
		public static boolean  downimgCode(String url,CHeader h,HttpClient client,String path){
			boolean b = false;
			// 输出的文件流  
			OutputStream os = null;
			InputStream is = null;
			try {
				HttpGet	get = CHeaderUtil.getHttpGet(url, h);
				HttpResponse  response = client.execute(get);
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
		public static String inputYanzhengma(){
			// 输出的文件流
			return inputYanzhengma("请输入验证码:");
		}
		public static String inputYanzhengma(String label){
			// 输出的文件流
			System.out.println(label);
			Scanner in = new Scanner(System.in);
			String name = in.nextLine();
			return name;
		}
		public static String getCookie(DefaultHttpClient client){
			 List<org.apache.http.cookie.Cookie> cookies =  client.getCookieStore().getCookies();
			 StringBuffer sb1 = new StringBuffer();
			 for (int i = 0; i < cookies.size(); i++) {
					 sb1.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue()).append(";");
//				 }
			}
			 return sb1.toString();
		}
		/**普通urlconnection
		 * @param url
		 * @return
		 * @throws Exception 
		 */
		public static URLConnection openURL(String url) throws Exception{
			HttpURLConnection httpurlconnection = null ;
			URL pageURL = null;
				try {
					pageURL = new URL(url);
					httpurlconnection = (HttpURLConnection) pageURL.openConnection();
					httpurlconnection.setConnectTimeout(40000);
					httpurlconnection.setReadTimeout(40000);
					httpurlconnection.setDoOutput(true);
					// Read from the connection. Default is true.
					httpurlconnection.setDoInput(true);
					httpurlconnection.connect(); 
				}catch(Exception e){
					throw new Exception(e);
				}
				return httpurlconnection;
		}
		
		
}
