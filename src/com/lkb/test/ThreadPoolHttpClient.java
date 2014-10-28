package com.lkb.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class ThreadPoolHttpClient {
   
    // 线程池的容量
    private static final int POOL_SIZE = 20;
    Object[] urls=null;
    public ThreadPoolHttpClient(Object[] urls){
        this.urls=urls;
    }
    
    public static void main(String[] args) throws Exception{
    	List<String> objectTmp = new ArrayList<String>();
    	objectTmp.add("http://baidu.com");
    	objectTmp.add("http://qq.com");
    	Object[] urls =  objectTmp.toArray();
	    
    	ThreadPoolHttpClient t = new ThreadPoolHttpClient(urls);
    	t.test();
    	 
    }
    public void test() throws Exception {
    	ExecutorService exe = Executors.newFixedThreadPool(POOL_SIZE);
        HttpParams params =new BasicHttpParams();
        /* 从连接池中取连接的超时时间 */ 
        ConnManagerParams.setTimeout(params, 1000);
        /* 连接超时 */ 
        HttpConnectionParams.setConnectionTimeout(params, 2000); 
        /* 请求超时 */
        HttpConnectionParams.setSoTimeout(params, 4000);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        //ClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        PoolingClientConnectionManager cm=new PoolingClientConnectionManager(schemeRegistry);
        cm.setMaxTotal(10);
        HttpClient httpClient = new DefaultHttpClient(cm,params);

        // URIs to perform GETs on
        final Object[] urisToGet = urls;
       
        for (int i = 0; i < urisToGet.length; i++) {
            HttpGet httpget = new HttpGet(urisToGet[i].toString());
            exe.execute( new GetThread(httpClient, httpget));
        }
        //httpClient.getConnectionManager().shutdown();
      
      
       
        System.out.println("Done");
        exe.shutdown();
    }
    static class GetThread extends Thread{
        
        private final HttpClient httpClient;
        private final ResponseHandler<String> responseHandler;
        private final HttpGet httpget;
        
        public GetThread(HttpClient httpClient, HttpGet httpget) {
            this.httpClient = httpClient;
            this.responseHandler =  new BasicResponseHandler();
            this.httpget = httpget;
        }
        @Override
        public void run(){
            get();
        }
        
        public void get() {
            try {
                String response = this.httpClient.execute(this.httpget, this.responseHandler);
              
                System.out.println(response);
                   
                
            } catch (Exception ex) {
                this.httpget.abort();
            }finally{
                httpget.releaseConnection();
            }
        }
    }
}