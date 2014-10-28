package com.lkb.thirdUtil.dx.test;

import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.proxy.ProxyPool;
import com.lkb.thirdUtil.dx.AbstractDianXinCrawler;

public abstract class DianXinForSpiderTest extends DianXinTest {
	
	protected Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
	protected AbstractDianXinCrawler dx=null;
	public void test() {
		initData();
		execute();
		if (dx.isSuccess()) {
			assuranceData(user != null ? user : dx.getUser(), dx.getTelList(), dx.getDetailList(), dx.getMessageList());
		}
		//assuranceData(dx.getUser(), dx.getTelList(), dx.getDetailList(), dx.getMessageList());
	}
	protected void useProxy() {
		ProxyPool.initProxyPool();
		ProxyPool pp = ProxyPool.getProxyPool();
		pp.addProxy(new String[] {"139.217.4.210", "31288"});
		
		spider.setUseProxy(true);
	}
}
