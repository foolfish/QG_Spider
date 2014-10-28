package com.lkb.util.thread;

import com.lkb.thirdUtil.base.BaseInfo;

public class Task {
	
//	 threadPool.waitFinish(); //等待所有任务执行完毕  
//     threadPool.closePool(); //关闭线程池  
	private BaseInfo baseInfo;//回头修改成其他对象
	private static ThreadPool threadPool = ThreadPool.getInstance();
	
	public Task(){}
	public Task(BaseInfo base){
		this.baseInfo = base;
	}
	 public BaseInfo getBaseinfo() {
		return baseInfo;
	}

	public void setBaseinfo(BaseInfo baseinfo) {
		baseInfo = baseinfo;
	}

	public static void addTask(BaseInfo base){
		threadPool.execute(new Task(base));
	}

	public void run(){
		getBaseinfo().startSpider();
	}
}
