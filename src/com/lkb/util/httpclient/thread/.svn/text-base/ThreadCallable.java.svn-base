package com.lkb.util.httpclient.thread;

import java.util.concurrent.Callable;

import com.lkb.util.httpclient.pojo.SendRequestPojo;
import com.lkb.util.httpclient.response.ExecuteRequest;

public class ThreadCallable implements Callable<String> {
	private ExecuteRequest request;
	private SendRequestPojo pojo;
	public ThreadCallable(ExecuteRequest request,SendRequestPojo pojo) {
		this.request = request;
		this.pojo = pojo;
	}
	
	@Override
	public String call() throws Exception {
		return request.execute(pojo);
	}
	

}
