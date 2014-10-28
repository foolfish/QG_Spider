package com.lkb.util.httpclient.response;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.Args;

import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.pojo.SendRequestPojo;
import com.lkb.util.httpclient.pojo.SimpleClientCookie;
import com.lkb.util.httpclient.thread.ComparableFutureTask;
import com.lkb.util.httpclient.thread.ExecutorServicePool;
import com.lkb.util.httpclient.thread.ThreadCallable;
import com.lkb.util.httpclient.util.CommonUtils;
import com.lkb.util.httpclient.util.HttpUtil;

/**
 * 请求执行类
 * @author fastw
 * @date	2014-8-13 下午3:18:21
 */
public class ExecuteRequest extends ExecuteAbstract{
	private SimpleClientCookie cookie;
	private boolean redirectsEnabled = true;
	
	public ExecuteRequest() {
		super();
	}
	
	
	public SimpleClientCookie getCookie() {
		if(cookie==null){
			this.cookie = new SimpleClientCookie(context);
		}
		return cookie;
	}
	public HttpClientContext getContext() {
		return context;
	}
	

//	public SendRequestPojo getRequestPojo() {
//		return requestPojo;
//	}

//
//	private ExecuteRequest setRequestPojo(SendRequestPojo requestPojo) {
//		Args.notNull(requestPojo, "SendRequestPojo Object");
//		context.setRequestConfig(requestPojo.getBuilder().build());
//		return this;
//	}

	public void setHandleRedirect(boolean isAuto){
		redirectsEnabled = isAuto;
	}
	/**禁止直接使用
	 * @param requestPojo 
	 * @return
	 */
	public String execute(SendRequestPojo requestPojo){
		if(redirectsEnabled){
			context.setRequestConfig(requestPojo.getBuilder().build());
		}else{
			context.setRequestConfig(requestPojo.getBuilder().setRedirectsEnabled(false).build());
		}
		int num = 0;
		boolean b = true;
		int i= 0;//响应头
		String text = null;
		log.debug("url:"+requestPojo.getUrl()+" 正在执行!");
		while(b){ //默认重发三次
			try {
				HttpResponse  response = client.execute(requestPojo.getHttpRequestBase(),this.context);
				i = response.getStatusLine().getStatusCode(); 
				if(i==200){
					if(requestPojo.getHeader()!=null&&requestPojo.getHeader().getRespCharset()!=null){
						text = ParseResponse.parse(response,requestPojo.getHeader().getRespCharset());
					}else{
						text = ParseResponse.parse(response);	
					}
				}else if(i==302||i==301){
					text = ParseResponse.getLocation(response);
				}else{
					b = false;
				}
			}catch (Exception e) {
				if(e instanceof NoHttpResponseException){
					b = false;
					requestPojo.getHttpRequestBase().abort();//中止循环
				}else if(e instanceof java.net.UnknownHostException){
					b = false;
					requestPojo.getHttpRequestBase().abort();//中止循环
				}else{
					if(requestPojo.getUrl()==null||"".equals(requestPojo.getUrl())){
						b = false;
						requestPojo.getHttpRequestBase().abort();//地址为空中止循环
					}else{
						log.error("URL:"+requestPojo.getUrl(),e);
//						e.printStackTrace(); //回头修改成具体异常那些需要重发	
					}
					
				}
			}
			if(text!=null&&b){
				b = false;
				requestPojo.getHttpRequestBase().abort();
			}else{
				num++;
				if(num>resendTimes){
					requestPojo.getHttpRequestBase().abort();
					b = false;
				}
			}
			
		}
		return  text;
	}
	
	
	
//	public  byte[]  downimgCode(){
//		String referer = HttpUtil.getHomePage(url);
//		return downimgCode(null);
//	}
	/**
	 *二进制返回
	 */
	public  byte[]  downimgCode(SendRequestPojo requestPojo){
		context.setRequestConfig(requestPojo.getBuilder().build());
		byte[] in_b = null;
		// 输出的文件流  
		OutputStream os = null;
		InputStream is = null;
		try {
			HttpResponse  response = client.execute(requestPojo.getHttpRequestBase(),this.context);
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
			log.error("URL:"+requestPojo.getUrl(), e);
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
	
	//----------------------------------兼容之前方法-------------------------------------------------
	public String get(String url,CHeader h){
		return getResult(new SendRequestPojo(url,h,true));
	}
	public String getResult(SendRequestPojo pojo){
		String result = null;
		if(StringUtils.isNotBlank(pojo.getUrl())){
			Args.notEmpty(isThreadManagement, "isThreadManagement");
			if(isThreadManagement.equals("false")){
				result =  this.execute(pojo);	
			}else{
				ComparableFutureTask<String> futureTask = new ComparableFutureTask<String>(new ThreadCallable(this,pojo));
				ExecutorServicePool.getInstance().submit(futureTask);
				try {
					result =  futureTask.get();
				} catch (Exception e) {
					log.error(pojo.getUrl()+"异常#", e);
				} 
			}
		}
		return result;
	}
	public String get(String url){
		return get(url,null);
	}
	public String getURL(String url){
		return getResult(new SendRequestPojo(url));
	}
	public String getURL(String url,CHeader h){
		return getResult(new SendRequestPojo(url,h));
	}
	public String post(String url,CHeader h,Map<String,String> param){
		return getResult(new SendRequestPojo(url,param,h,true));//为兼容之前的请求默认是true uri请求访问
	}
	public String post(String url,Map<String,String> param){
		return post(url,null,param);
	}
	public String postURL(String url,Map<String,String> param){
		return getResult(new SendRequestPojo(url,param));
	}
	public String postURL(String url,CHeader h,Map<String,String> param){
		return getResult(new SendRequestPojo(url,param,h));
	}
	public  byte[]  downimgCode(String url){
		String referer = HttpUtil.getHomePage(url);
		return downimgCode(url, new CHeader(referer));
	}
	public  byte[]  downimgCode(String url,CHeader h){
		return this.downimgCode(new SendRequestPojo(url,h));
	}
	public  boolean  downimgCode(String url,CHeader h,String path){
		// 输出的文件流  
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			os.write(downimgCode(new SendRequestPojo(url,h)));
			return true;
		}  catch (Exception e) {
		}finally{
			try {
				if(os!=null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}


	
	
	/**批量抓取
	 * @param list
	 * @return
	 */
	public LinkedList<String> getResult(List<SendRequestPojo> list){
		LinkedList<String> lstr = null;
		if(CommonUtils.isNotEmpty(list)){
			// 进行异步任务列表
		    List<ComparableFutureTask<String>> futureTasks = new ArrayList<ComparableFutureTask<String>>();
			for (int i = 0;i < list.size(); i++) {
				// 创建一个异步任务
				ComparableFutureTask<String> futureTask = new ComparableFutureTask<String>( new ThreadCallable(this,list.get(i)));
				futureTasks.add(futureTask);
				ExecutorServicePool.getInstance().submit(futureTask);
			}
			lstr = new LinkedList<String>();
			for (ComparableFutureTask<String> comparableFutureTask : futureTasks) {
				try {
					lstr.add(comparableFutureTask.get());
				} catch (Exception e) {
					log.error("httpclient多线程执行异常#", e);
				} 
			}
		}else{
			 lstr = new LinkedList<String>();
		}
		return lstr;
	}
//
//	@Override
//	public int compareTo(Object o) {
//		return 0;
//	}
	
}
