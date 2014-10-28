package com.lkb.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ITEyeTest {

	private static String urlpre = "http://www.iteye.com/search?query=爬虫&type=blog&page=";
	/**
	 * @param args
	 */
	static Set<String>  set = new HashSet();
	
	public static void main(String[] args) {
		CloseableHttpClient httpclient = null;
		HttpHost proxy = new HttpHost("61.164.73.19",82);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();		

        //HttpGet httpget = new HttpGet("http://passport.jd.com/uc/login");
        //ResponseHandler<String> responseHandler = new BasicResponseHandler();
		ITEyeTest iTEyeTest = new ITEyeTest();
       // String response = iTEyeTest.getText(httpclient,paUrl);
       // System.out.println(response);
		List<String> list = iTEyeTest.getURLlist(httpclient);
		iTEyeTest.anlyse( list, httpclient);
		
        //指定排序器   
        TreeMap<Integer, String> treeMap2 = new TreeMap<Integer, String>(new Comparator<Integer>(){
			@Override
			public int compare(Integer arg0, Integer arg1) {
				// TODO Auto-generated method stub
				return 0;
			}      
        });   
       

		System.out.println("**********************");
		Iterator it = set.iterator();
		int count=0;
		CloseableHttpClient httpclient2 = null;
		HttpHost proxy2 = new HttpHost("61.156.235.172",9999);
        DefaultProxyRoutePlanner routePlanner2 = new DefaultProxyRoutePlanner(proxy2);
        httpclient2 = HttpClients.custom().setRoutePlanner(routePlanner2).build();	
        
		while(it.hasNext()){		
 			String url = it.next().toString();
 			System.out.println(url);
			String content = iTEyeTest.getText(httpclient2,url);
			Document doc = Jsoup.parse(content);
			Element element = doc.select("div[id=blog_actions]").first();
			if(element!=null){
				Elements elements = element.select("li");
				if(elements!=null && elements.size()>1){
					String times = elements.get(0).text().replace("浏览: ", "").replace("次", "").trim();
					System.out.println("times="+times);
					int scanTimes = Integer.parseInt(times);
					treeMap2.put(scanTimes, url);
					count++;
				}
				
			}
			
		}
		
		
		Iterator it2 = treeMap2.keySet().iterator();
		while (it.hasNext()) {
			int key = Integer.parseInt(it.next().toString());
			String url = treeMap2.get(key);
			System.out.println(key+"****"+url);
		}
		
	}
	
	 static class GetThread extends Thread{
	        
		    private final HttpClient httpClient;
	        private final HttpGet httpget;
	        private final ResponseHandler<String> responseHandler;

	        public GetThread(HttpClient httpClient, HttpGet httpget) {
	            this.httpClient = httpClient;
	            this.responseHandler =  new BasicResponseHandler();
	            this.httpget = httpget;

	        }
	        @Override
	        public void run(){
	            get();
	        }
	        
	    	public String getText(HttpClient httpClient,String redirectLocation) {
	    		HttpGet httpget = new HttpGet(redirectLocation);
	    		ResponseHandler<String> responseHandler = new BasicResponseHandler();
	    		String responseBody = "";
	    		try {
	    			responseBody = httpClient.execute(httpget, responseHandler);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    			responseBody = null;
	    		} finally {
	    			httpget.abort();
	    		
	    		}
	    		return responseBody;
	    	}
	    	
	        public void get() {	        	
	        	 try {
	        		// System.out.println(httpget.getURI()+"***&&&");
					String content = httpClient.execute(this.httpget, this.responseHandler);
					Document doc = Jsoup.parse(content);    
         			Elements  orderElements = doc.select("div[class=topic clearfix]");
         			for(int i = 0;i<orderElements.size();i++){
         				Element element = orderElements.get(i);
         				String href = 	element.select("h4").first().select("a").first().attr("href");
         				set.add(href);
         				//System.out.println(href);
         			}
					
					
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	
	        }
	 }
	        
	
	public void anlyse(List<String> objectTmp,CloseableHttpClient httpclient){
		Object[] urisToGet =  objectTmp.toArray(); 
		
		//一种异步的写法，不要删除，留着以后使用
		//ExecutorService exe = Executors.newFixedThreadPool(20);
//        for (int i = 0; i < urisToGet.length; i++) {
//            HttpGet httpget = new HttpGet(urisToGet[i].toString());
//            exe.execute( new GetThread(httpclient, httpget, userService, orderService, currentUser));
//        }
		 try {
        GetThread[] threads = new GetThread[urisToGet.length];   
        int length = urisToGet.length;
        for (int i = 0; i < length; i++) {   
            HttpGet httpget = new HttpGet(urisToGet[i].toString());   
            threads[i] = new GetThread(httpclient, httpget);   
        }   
        
        // start the threads   
        for (int j = 0; j < length; j++) {   
            threads[j].start();   
        }   

        // join the threads   
        for (int j = 0; j < length; j++) {                  
				threads[j].join();				 
        	}   

		 } catch (InterruptedException e) {
			 e.printStackTrace();
	}  finally {   
    }  
	}
	
	public List<String> getURLlist(CloseableHttpClient httpclient){
		  List<String> objectTmp = new ArrayList<String>();
			int tempsize = 5;
			int time =1;
			while(1==1){
				String url = urlpre+time*tempsize;
				String content = getText(httpclient, url);
				if(!content.contains("很遗憾，没有找到您想要的，换个关键词或者类型再试试？")){
					for(int i=1;i<=5;i++){
						int cpage = (time-1)*5+i;  
						objectTmp.add(urlpre+cpage);
					}
					time++;
				}else{
					for(int i=1;i<=4;i++){
						int cpage = (time-1)*5+i;
						objectTmp.add(urlpre+cpage);
					}
					break;}
			}
			return objectTmp;
	}
	
	/*
	 * 得到网页的内容
	 * */
	private String getText(CloseableHttpClient httpclient,String redirectLocation) {
		HttpGet httpget = new HttpGet(redirectLocation);
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
