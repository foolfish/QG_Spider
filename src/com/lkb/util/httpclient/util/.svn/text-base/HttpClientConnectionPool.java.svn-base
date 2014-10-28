package com.lkb.util.httpclient.util;


import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

	 
/**client池类
 * @author fastw
 * @date   2014-9-14 上午11:10:24
 */
public class HttpClientConnectionPool { 
		/**将最大连接数增加到200**/
		private  final Integer maxTotal = 1000;
		/**defaultMaxPerRoute*/
		private final Integer defaultMaxPerRoute = 200;
		/**如果内部失败就重发!,最多重发4次**/
		private  final int resendTimes = 4;
		
	    private static CloseableHttpClient client = null;
	    private HttpClientBuilder builder = null;
	    static{
	    	client = getCloseableClient();
	    }
	    public static CloseableHttpClient getInstance(){
	    	return client;
	    }
		
	    private static CloseableHttpClient getCloseableClient(){
	    	HttpClientConnectionPool clientPool = new HttpClientConnectionPool();
	    	clientPool.setExecutionCount().setClientConnectionPool().setKeepAliveDuration();
	    	return clientPool.getBuilder().build();
	    }
	    public  HttpClientBuilder getBuilder(){
	    	if(builder==null){
	    		builder = HttpClientBuilder.create();
	    	}
	    	return builder;
	    }
	   
	    /**
	     * 默认请求次数
	     * @param builder
	     */
	    private  HttpClientConnectionPool setExecutionCount(){
	    	HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

	            public boolean retryRequest(
	                    IOException exception,
	                    int executionCount,
	                    HttpContext context) {
	                if (executionCount >= resendTimes) {
	                    // 如果已经重试了5次，就放弃
	                    return false;
	                }
	                if (exception instanceof InterruptedIOException) {
	                    // 超时
	                    return false;
	                }
	                if (exception instanceof UnknownHostException) {
	                    // 目标服务器不可达
	                    return false;
	                }
	                if (exception instanceof ConnectTimeoutException) {
	                    // 连接被拒绝
	                    return false;
	                }
	                if (exception instanceof SSLException) {
	                    // ssl握手异常
	                    return false;
	                }
	                HttpClientContext clientContext = HttpClientContext.adapt(context);
	                HttpRequest request = clientContext.getRequest();
	                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
	                if (idempotent) {
	                    // 如果请求是幂等的，就再次尝试
	                    return true;
	                }
	                return false;
	            }

	        };  
	        getBuilder().setRetryHandler(myRetryHandler);
	        return this;
	    }
	    /**设置线程池
	     * @param builder
	     */
	    private  HttpClientConnectionPool setClientConnectionPool(){
	    	X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager 
	    		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
	    		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
	    		public X509Certificate[] getAcceptedIssuers() { return null; }
				public void checkClientTrusted(X509Certificate[] arg0, String arg1, Socket arg2) throws CertificateException {}
				public void checkClientTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] arg0, String arg1, Socket arg2) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2) throws CertificateException {} 
	    	}; 
	    	 
	    	SSLContext ctx = null;
	    	 Registry<ConnectionSocketFactory> r = null;
			try {
//      		TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext 
				ctx = SSLContext.getInstance("TLS");
      		//使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用 
      		ctx.init(null, new TrustManager[]{xtm}, new SecureRandom()); 
      		SSLContext.setDefault(ctx);
	    	 SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx);
	    	r = RegistryBuilder.<ConnectionSocketFactory>create()
	    	            .register("http", PlainConnectionSocketFactory.INSTANCE)
	    	            .register("https", sslsf)
	    	            .build();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} 
	    	PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
	    	 SocketConfig socketConfig  = SocketConfig.custom().setTcpNoDelay(true).build();
            cm.setDefaultSocketConfig(socketConfig);
            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
                    .setMaxLineLength(2000).build();
            ConnectionConfig connectionConfig     = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints).build();
            cm.setDefaultConnectionConfig(connectionConfig);
    	    // 将最大连接数增加到200
    	    cm.setMaxTotal(maxTotal);
    	    // 将每个路由基础的连接增加到20
    	    cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
    	    getBuilder().setConnectionManager(cm);
    	    return this;
	    }
	    
	    private  HttpClientConnectionPool setKeepAliveDuration(){
	    	 ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {
	    	        @Override
	    	        public long getKeepAliveDuration(
	    	            HttpResponse response,
	    	            HttpContext context) {
	    	                long keepAlive = super.getKeepAliveDuration(response, context);
	    	                if (keepAlive == -1) {
	    	                    //如果服务器没有设置keep-alive这个参数，我们就把它设置成5秒
	    	                    keepAlive = 5000;
	    	                }
	    	                return keepAlive;
	    	        }

	    	    };
	    	    //定制我们自己的httpclient
	    	  
	    	    getBuilder().setKeepAliveStrategy(keepAliveStrat);
	    	    return this;
	    	            
	    }
	    /**关闭连接*/
	    public static void colse(CloseableHttpClient client){
	    	try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	  
		
}
