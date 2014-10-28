package com.lkb.util.httpclient.thread;

import com.lkb.thirdUtil.base.BasicCommonAbstract;
import com.lkb.util.redis.Redis;

public class Task implements Runnable {
	
	private BasicCommonAbstract basic;
	private int type;
	public Task(){}
	public Task(BasicCommonAbstract basic,int type){
		this.basic = basic;
		this.type = type;
	}

	public BasicCommonAbstract getBasic() {
		return basic;
	}
	public void setBasic(BasicCommonAbstract basic) {
		this.basic = basic;
	}
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @param basic
	 * @param type 对应要执行方法
	 */
	public static void addTask(BasicCommonAbstract basic,int type){
		ExecutorServicePool.getInstanceThread().execute(new Task(basic,type));
	}

	public void run(){
		String key = getBasic().getRediskey()+"_run";
		synchronized (Task.class) {
			Integer num = (Integer) Redis.getObj(key);
			if(num==null){
				num = 0;
				getBasic().parseBegin();
			}
			Redis.setEx(key, ++num,Redis.getMinute(5));
		}
		try{
			getBasic().startSpider(getType());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			synchronized (Task.class) {
				Integer num = (Integer) Redis.getObj(key);
				if(num!=null){
				   num--;
				   if(num<=0){
					    getBasic().parseEnd();
						Redis.del(key);
					}else{
						Redis.setEx(key,num,Redis.getMinute(5));
					}
				}else{
					getBasic().parseEnd();
				}
			}
		}
		
	}
}
