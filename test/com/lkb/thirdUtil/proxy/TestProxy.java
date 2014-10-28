/**
 * 
 */
package com.lkb.thirdUtil.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.HttpHost;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lkb.bean.SimpleObject;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.thirdUtil.dx.AbstractDianXinCrawler;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;

/**
 * @author think
 * @date 2014-9-4
 */
public class TestProxy extends AbstractDianXinCrawler {
	//219.143.103.242  来自：北京市 电信
	public void test(Spider spider) {
		this.spider = spider;
		HttpHost item = null;
		try {
			item = new HttpHost(InetAddress.getByName("139.217.4.210"), 31288);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		getUrl("http://www.ip.cn/", null, null, new AbstractProcessorObserver(util, WaringConstaint.LNYD_7){
			@Override
			public void afterRequest(SimpleObject context) {
				parseIP(context, 1);
			}
		});
		
		getUrl("http://www.ip.cn/", null, new Object[] {null, null, item} , new AbstractProcessorObserver(util, WaringConstaint.LNYD_7){
			@Override
			public void afterRequest(SimpleObject context) {
				parseIP(context, 2);
			}
		});
		
	}
	public void parseIP(SimpleObject context, int i) {
		Document doc = ContextUtil.getDocumentOfContent(context); 
		Elements es1 = doc.select("div#result");
		data.put("ip" + i, es1.select("code").text());
		data.put("address" + i, StringUtil.subStr("来自：", "GeoIP:", es1.text()));
	}
	public static void main(String[] args) throws Exception {
		String phoneNo = "18033723291";
		String password = "199034";

		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		TestProxy dx = new TestProxy();
		dx.test(spider);
		spider.start();
		//dx.logger.error("aaaaaaaaa");
		/*dx.setAuthCode(CUtil.inputYanzhengma());
			dx.requestAllService();
			spider.start();*/
		dx.printData();


	}
	@Override
	protected void onCompleteLogin(SimpleObject context) {
		
	}

}
