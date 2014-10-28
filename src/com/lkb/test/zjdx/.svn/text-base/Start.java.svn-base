package com.lkb.test.zjdx;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.constant.ConstantHC;
import com.lkb.util.httpclient.entity.CHeader;


public class Start {

	public static void main(String[] args) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		DefaultHttpClient client = CUtil.init(); //创建默认的httpClient实例
		map.put(ConstantHC.k_client,client);
		map.put(ConstantHC.k_username,"15355733291");
		map.put(ConstantHC.k_pass,"705353");
		
		//第一步
		boolean b = Login.index(map);
		if(b){
			//返回验证码
			String code = Login.uploadVCode(map,"d://yanzhengma.png");
			map.put(ConstantHC.k_vcode,code);
			//开始登陆
			String s = Login.login1(map);
			if(s!=null){
				if(s.contains("输入有错误")){
					System.out.println("验证码错误");
				}else if(s.contains("location.href = \"/service/\"")){
					System.out.println("s======="+s);
					System.out.println("登陆成功");
				
			
					
					CHeader h = new CHeader(CHeaderUtil.Accept_json,"http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page",CHeaderUtil.Content_Type__urlencoded,
							"zj.189.cn",true);
					
					/*	HttpResponse response = CUtil.getHttpGet("http://zj.189.cn/shouji/15355733291/bangzhu/ziliaoxg/",h, client);
					
					String text = ParseResponse.parse(response,"GBK");
					//System.out.println("===========callContent6=========="+text);
					
					HttpResponse response1 = CUtil.getHttpGet("http://zj.189.cn/shouji/15355733291/zhanghu/yue/",h, client);
					
					String text1 = ParseResponse.parse(response1,"GBK");
					System.out.println("===========text1=========="+text1);*/
					
					String xmls= "<buffalo-call>";
					xmls+="<method>SendVCodeByNbr</method>";
					xmls+="<string>15355733291</string>";   
					xmls+="</buffalo-call>";
					//StringEntity se = new StringEntity(xmls, "GBK"); 
					HttpResponse response1 = getPost( "http://zj.189.cn/bfapp/buffalo/VCodeOperation", h,client, xmls);
					String text1 = ParseResponse.parse(response1,"UTF-8");
					System.out.println("===========text1=========="+text1);
					
					
					
		
					
					
					
				}
				
			}else{
				System.out.println("登陆失败");
			}
			
		}
		
		
		
	}
	
	/***
	 * @param url  访问地址
	 * @param h		访问请求头
	 * @param client	
	 * @return 返回post方式 的响应
	 */
	public static HttpResponse getPost(String url,CHeader h,DefaultHttpClient client,String xmls){
		try {
			HttpPost post = new HttpPost(url);
			//CHeaderUtil.putHeader(h, post);
			StringEntity s = new StringEntity(xmls);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/xmls");
			post.setEntity(s);
			HttpResponse response = client.execute(post);
			
			// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求 
			if(response.getStatusLine().getStatusCode() == 200||response.getStatusLine().getStatusCode()==302){ 
				return response;
			}
		}  catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	// POST 公共方法
	public static String postHtmlBySe(HttpClient httpclient, String url, StringEntity  se,
			String hostUrl, String refererUrl) {
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpost.setHeader("Accept-Encoding", "gzip, deflate");
		httpost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		httpost.setHeader("Connection", "keep-alive");
		httpost.setHeader("Host", hostUrl);
		httpost.setHeader("Referer", refererUrl);
		httpost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		String content = "";
	
		try {
			httpost.setEntity(se);
			HttpResponse response = httpclient.execute(httpost);
			
			int i =response.getStatusLine().getStatusCode();
			 if(i==302){
		          Header locationHeader = response.getFirstHeader("Location");
		          if (locationHeader != null) {
		        	 content = locationHeader.getValue();
		            //System.out.println(content);
		          }
		       }else{
		    	   content =  EntityUtils.toString(response.getEntity(),"UTF-8");
		       }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpost.abort();
		}
		return content;
	}
	
	// POST 公共方法
	public String postHtml(HttpClient httpclient, String url, Map dxMap,
			String hostUrl, String refererUrl) {
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpost.setHeader("Accept-Encoding", "gzip, deflate");
		httpost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		httpost.setHeader("Connection", "keep-alive");
		httpost.setHeader("Host", hostUrl);
		httpost.setHeader("Referer", refererUrl);
		httpost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		String content = "";
		if (dxMap != null && dxMap.size() > 0) {
			Iterator it = dxMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = dxMap.get(key).toString();
				nvps.add(new BasicNameValuePair(key, value));
			}
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(
					(List<? extends org.apache.http.NameValuePair>) nvps,
					HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpost);
			
			int i =response.getStatusLine().getStatusCode();
			 if(i==302){
		          Header locationHeader = response.getFirstHeader("Location");
		          if (locationHeader != null) {
		        	 content = locationHeader.getValue();
		            
		          }
		       }else{
		    	   content =  EntityUtils.toString(response.getEntity(),"UTF-8");
		       }
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpost.abort();
		}
		return content;
	}
	
	

	// GET 公共方法
	private static String getHtml(HttpClient httpclient, String url1, String hostUrl,String refererUrl,String charsetName,boolean  isRedirect) {
		URL url = null;
		try {
			url = new URL(url1);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		URI uri = null;
		try {
			uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpClientContext context = HttpClientContext.create();  
		HttpGet httpget = new HttpGet(uri);
		httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.setHeader("Accept-Encoding", "gzip, deflate");
		httpget.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		httpget.setHeader("Connection", "Keep-Alive");
		if (hostUrl != null && hostUrl.length() > 0) {
			httpget.setHeader("Host", hostUrl);
		}
		if (refererUrl != null && refererUrl.length() > 0) {
			httpget.setHeader("Referer", refererUrl);
		}
		if (!isRedirect) {
			HttpParams params = new BasicHttpParams();
			 params.setParameter("http.protocol.handle-redirects", false);
			 httpget.setParams(params);
		}
		
		
		//httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
		HttpResponse response =  httpclient.execute(httpget);
		int i =response.getStatusLine().getStatusCode();
		 if(i==302){
	          Header locationHeader = response.getFirstHeader("Location");
	          if (locationHeader != null) {
	        	  responseBody = locationHeader.getValue();
	          }
	       }else{
	    	   responseBody = getContent(response,charsetName);
	    	   
	    	  // responseBody =   EntityUtils.toString(response.getEntity()); 
	    	 
	       }
	
		} catch (Exception e) {

			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}
	
	public static String getContent(HttpResponse response,String charsetName) {
		BufferedReader in = null;
		String page = "";
		try {
			if(charsetName==null || charsetName.length()<1){
				charsetName="UTF-8";
			}
		
			
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent(), charsetName));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			try {
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
			} catch (Exception e) {
				// TODO: handle exception
			//	 e.printStackTrace();
			} finally {
				page = sb.toString();
			}
		} catch (Exception e) {
			 e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return page;
	}
	

}
