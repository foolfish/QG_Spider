package com.lkb.util.httpclient.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.lkb.util.InfoUtil;



public class ExecutorServicePool {
	private static final Integer corePoolSize;
	public static final Integer maximumPoolSize;
	private static final Integer keepAliveTime;//5分钟
	private static final Integer corePoolSizeThread;
	private static final Integer maximumPoolSizeThread;
	private static ThreadPoolExecutor executorFuture;
	private static ThreadPoolExecutor executorThread;
	
	static{
		corePoolSize = Integer.parseInt(InfoUtil.getInstance().getInfo("roadThread","ExecutorServicePool.corePoolSize"));
		maximumPoolSize = Integer.parseInt(InfoUtil.getInstance().getInfo("roadThread","ExecutorServicePool.maximumPoolSize"));
		keepAliveTime = Integer.parseInt(InfoUtil.getInstance().getInfo("roadThread","ExecutorServicePool.keepAliveTime"));
		corePoolSizeThread = Integer.parseInt(InfoUtil.getInstance().getInfo("roadThread","ExecutorServicePool.corePoolSizeThread"));
		maximumPoolSizeThread = Integer.parseInt(InfoUtil.getInstance().getInfo("roadThread","ExecutorServicePool.maximumPoolSizeThread"));
		init();
	}
    public static ThreadPoolExecutor getInstance(){
    	return executorFuture;
    }
    public static ThreadPoolExecutor getInstanceThread(){
    	if(executorThread==null){
    		synchronized (ExecutorServicePool.class) {
    			if(executorThread == null){
    				BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
    				executorThread  = new XThreadPoolExecutor(corePoolSizeThread, maximumPoolSizeThread, keepAliveTime, TimeUnit.MINUTES,workQueue);
    			}
			}
    	}
    	return executorThread;
    }
  
	public static void  init(){
		/**带有执行优先级的队列*/
		BlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<Runnable>();//带有执行优先级的队列
		executorFuture  = new XThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES,workQueue);
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// 进行异步任务列表
		List<ComparableFutureTask<Integer>> futureTasks = new ArrayList<ComparableFutureTask<Integer>>();
		long start = System.currentTimeMillis();
		// 类似与run方法的实现 Callable是一个接口，在call中手写逻辑代码
		Callable<Integer> callable = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Integer res = new Random().nextInt(100);
				Thread.sleep(1000);
				System.out.println("任务执行:获取到结果 :" + res);
				return res;
			}
		};

		for (int i = 0; i < 10; i++) {
			// 创建一个异步任务
			ComparableFutureTask<Integer> futureTask = new ComparableFutureTask<Integer>(callable);
			futureTasks.add(futureTask);
			// 提交异步任务到线程池，让线程池管理任务 特爽把。
			// 由于是异步并行任务，所以这里并不会阻塞
			executorFuture.submit(futureTask);
		}
		System.out.println();
		Integer count = 0;
		for (ComparableFutureTask<Integer> futureTask : futureTasks) {
			// futureTask.get() 得到我们想要的结果
			// 该方法有一个重载get(long timeout, TimeUnit unit) 第一个参数为最大等待时间，第二个为时间的单位
			count += futureTask.get();
		}
		long end = System.currentTimeMillis();
		System.out.println("线程池的任务全部完成:结果为:" + count + "，main线程关闭，进行线程的清理");
		System.out.println("使用时间：" + (end - start) + "ms");
		// 清理线程池
		executorFuture.shutdown();
		System.out.println("完毕");
		System.out.println(executorFuture.getPoolSize());
	}  
}
