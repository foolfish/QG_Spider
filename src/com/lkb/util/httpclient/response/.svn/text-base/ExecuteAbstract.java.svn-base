package com.lkb.util.httpclient.response;

import java.util.concurrent.Callable;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.thread.ExecutorServicePool;
import com.lkb.util.httpclient.util.HttpClientConnectionPool;


/**
 * @author fastw
 * @date   2014-9-25 下午6:39:15
 */
public abstract class ExecuteAbstract {//,Comparable{
   public static final Logger log = Logger.getLogger(ExecuteRequest.class);
	
	public static final int resendTimes = Integer.parseInt(InfoUtil.getInstance().getInfo("roadThread","ExecuteAbstract.resendTimes"));//重发次数
	/**是否直接请求直接交付给线程池管理*/
	public static final String isThreadManagement = InfoUtil.getInstance().getInfo("roadThread","ExecuteAbstract.isThreadManagement");
	
	
	public static CloseableHttpClient client = HttpClientConnectionPool.getInstance();
	public HttpClientContext context;
	public ExecuteAbstract() {
		this.context = HttpClientContext.create();
	}
	/**	请求地址的数量超过最大线程数5倍时返回服务器繁忙!
	 * @return
	 */
	public static boolean isServerBusy(){
		if(!isThreadManagement.equals("false")){
			if(ExecutorServicePool.getInstance().getPoolSize()>(ExecutorServicePool.maximumPoolSize*5)){
				return true;
			}
		}
		return false;
	}
}
