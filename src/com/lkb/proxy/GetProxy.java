package com.lkb.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.SimpleObject;
import com.lkb.framework.ThreadPoolManager;
import com.lkb.proxy.util.ProxyAuthentication;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.proxy.ProxyManager;
import com.lkb.robot.proxy.ProxyManagerFactory;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.thirdUtil.AbstractCrawler;
import com.lkb.util.WaringConstaint;

/**
 * <p>
 * 获取代理
 * 
 * @author Pat.Liu
 * */
public class GetProxy extends AbstractCrawler {
	private int maxThreadNum = 200;
	// private ProxyAuthentication pro = new ProxyAuthentication();
	// private static final AtomicInteger threadNum = new AtomicInteger(0);
	private Collection<Future<Boolean>> futureList = Collections
			.synchronizedSet(new HashSet());
	private Collection<String> queue = new ConcurrentSkipListSet<String>();
	public static int sum = 0;
	public static int test_sum_http = 0;
	public static int test_sum_https = 0;
	public static int test_sum_https_http = 0;
	public static int sum_all = 0;
	private static final AtomicInteger threadNum = new AtomicInteger(0);

	public GetProxy() {
		spider = SpiderManager.getInstance().createSpider("test", "aaa");
		spider.getSite().setCycleRetryTimes(0);
		spider.getSite().setTimeOut(15000);
		spider.setDestroyWhenExit(false);
		spider.setUseProxy(false);
	}

	public void close() {
		spider.close();
	}

	/*
	 * private boolean httpsIsOk(String ip, String port) {
	 * 
	 * try { if (pro.authenticateHttps(ip, port) == true) { //
	 * System.out.println(pro.getResponseTime()); return true; } else return
	 * false; } catch (Exception e) { return false; } }
	 * 
	 * private boolean httpIsOk(String ip, String port) { // ProxyAuthentication
	 * pro = new ProxyAuthentication(); try { if (pro.authenticateHttp(ip, port)
	 * == true) { return true; } else return false; } catch (Exception e) {
	 * return false; } }
	 */

	/**
	 * <p>
	 * 过滤24小时内提取过的IP,这是唯一保证的选项~其他限制选项会随着代理抓取次数的增多（没抓够数量）依次减少~
	 * */
	/*
	 * private List<String> getHttpsIpBy56(int num, SimpleObject proxy) throws
	 * Exception { List<String> list = new ArrayList<String>(); TestPublicProxy
	 * a = new TestPublicProxy(); int canUseNumber = 0; String speed = "7000";
	 * String region =
	 * "北京,河北,上海,山东,山西,广东,浙江,江苏,河南,辽宁,黑龙江,吉林,安徽,湖北,湖南,海南,陕西,云南,福建,内蒙古,青海,宁夏,广西,云南,天津,重庆,四川,贵州"
	 * ; String anonymity = "3"; String url =
	 * "http://www.56pu.com/api?orderId=715525837412169&quantity=" + num 5 +
	 * "&line=all&regionEx=&beginWith=&ports=&vport=&scheme=1&duplicate=2&sarea="
	 * ; boolean isOver = false; ProxyManager pm =
	 * ProxyManagerFactory.getProxyManager(); while (!isOver) { String result =
	 * a.get(url + "&anonymity=" + anonymity + "&region=" + region + "&speed=" +
	 * speed); String[] ips = result.split("/n"); if (ips.length == 0 ||
	 * (ips.length == 1 && ips[0].contains("没有"))) { // 日志告知url不够，必须扩大选择范围 if
	 * (region.length() > 5) region = "";// 去掉地区限制！有可能抓到国外代理，不过还是有速度保障 else if
	 * (anonymity == "3") anonymity = "";// 去掉匿名限制！被禁用的可能性极高 else if
	 * (speed.length() > 1) speed = "";// 去掉速度限制！这基本就是只有https一个限制了~再不行就完蛋！ else
	 * { throw new Exception("https:This proxySite is over!"); //
	 * 这个网站已经完全不能用了！！！ } continue; } for (int i = 0; i < ips.length; i++) {
	 * String[] ss = ips[i].split(":"); if (ss.length < 2) { continue; } String
	 * ip = ss[0]; String port = ss[1];
	 *//**
	 * 在这里开多线程
	 * */
	/*
	 * String[] hp = new String[] { ip, port }; if (!pm.existsProxy(hp)) { if
	 * (this.httpsIsOk(ip, port)) { // list.add(ip + ":" + port);
	 * pm.addProxy(proxy, hp); } canUseNumber++; } else if (this.httpIsOk(ip,
	 * port)) { pm.addProxy(null, hp);
	 * 
	 * }
	 * 
	 * if (canUseNumber >= num) break; // System.out.println("false"); } } //
	 * this.addProxyManager(list); return list; }
	 */

	/**
	 * <p>
	 * 过滤24小时内提取过的IP,这是唯一保证的选项~其他限制选项会随着代理抓取次数的增多（没抓够数量）依次减少~
	 * 由于可用http协议较多，每次没取够，下次取的数量减少，只会限制时间选项
	 * */
	/*
	 * private List<String> getHttpIpBy56(int num, SimpleObject proxy) throws
	 * Exception { List<String> list = new ArrayList<String>(); List<String>
	 * listReturn = new ArrayList<String>(); TestPublicProxy a = new
	 * TestPublicProxy(); int canUseNumber = 0; String speed = "1000"; String
	 * url =
	 * "http://www.56pu.com/api?orderId=715525837412169&line=all&region=&regionEx=&beginWith=&ports=&vport=&anonymity=3&scheme=&duplicate=2&sarea="
	 * ; boolean isOver = false; ProxyManager pm =
	 * ProxyManagerFactory.getProxyManager(); while (!isOver) { String result =
	 * a.get(url + "&speed=" + speed + "&quantity=" + (num - canUseNumber) *
	 * 10); System.out.println(result); String[] ips = result.split("/n"); if
	 * (ips.length == 0 || (ips.length == 1 && ips[0].contains("没有"))) { //
	 * 日志告知url不够，必须扩大选择范围 if (Integer.parseInt(speed) < 3000) speed = "3000";//
	 * 增加速度限制！ else if (Integer.parseInt(speed) < 5000) speed = "5000"; else {
	 * throw new Exception("http:This proxySite is over!"); // 这个网站已经完全不能用了！！！ }
	 * continue; } for (int i = 0; i < ips.length; i++) {
	 * 
	 * String[] ss = ips[i].split(":"); if (ss.length < 2) { continue; } String
	 * ip = ss[0]; String port = ss[1];
	 *//*
		 * 在这里开多线程
		 *//*
			 * String[] hp = new String[] { ip, port }; if (!pm.existsProxy(hp))
			 * { if (this.httpIsOk(ip, port)) { pm.addProxy(proxy, hp); //
			 * list.add(ip + ":" + port); canUseNumber++; }
			 * System.out.println("true!" + ip + ":" + port); } if (canUseNumber
			 * >= num) break; // System.out.println("false"); } } //
			 * this.addProxyManager(list); return list; }
			 */

	/**
	 * <p>
	 * 从http://www.proxynova.com/proxy-server-list/country-cn/拿一遍代理，每次有30多个，
	 * 大概每5分钟就可以拿到完全不一样的30多个，爬取机制可以特别制定 没办法爬指定数量个ip的
	 * 记得在外层try-catch，然后log记录，然后可以记录网页是否改版！！！！ 根据测试，每次36个ip，大约可以拿到10,5,5
	 * 
	 * @param country
	 *            0为中国，1为美国，其他可以再加
	 * */
	private void getIpByProxynova(final SimpleObject proxy, int country)
			throws Exception {
		String url = "";
		if (country == 0)
			url = "http://www.proxynova.com/proxy-server-list/country-cn";
		else if (country == 1)
			url = "http://www.proxynova.com/proxy-server-list/country-us";
		getUrl(url, null, new AbstractProcessorObserver(util,
				WaringConstaint.PROXY_NOVA) {
			@Override
			public void afterRequest(SimpleObject context) {
				if (context == null)
					return;
				Document doc = ContextUtil.getDocumentOfContent(context);
				Element tbody = doc.getElementById("tbl_proxy_list")
						.getElementsByTag("tbody").first();
				Elements trs = tbody.getElementsByTag("tr");
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < trs.size(); i++) {
					Element tr = trs.get(i);
					if (tr.getElementsByTag("span") == null
							|| tr.getElementsByTag("td") == null
							|| tr.getElementsByTag("td").size() < 2)
						continue;
					Elements tds = tr.getElementsByTag("td");
					// 一次性！每次36个~3~5分钟可以拿到完全不同的
					list.add(tds.get(0).text() + ":" + tds.get(1).text());
				}
				sum_all += list.size();
				if (list.size() > 0)
					exec(list, proxy, true);
			}
		});
		return;
	}

	/**
	 * <p>
	 * 从http://letushide.com/filter/all,all,cn/
	 * list_of_free_CN_China_proxy_servers拿代理，中国的能拿100~128，
	 * 网站是每分钟验证，世界总共有超过3000个
	 * 一共有3000+，但是看不全！暂时设计只拿中国的或者全球https的，也拿不太全，不过能看到一大半。支持txt和json
	 * 、api导出，但是数量不如网站直接看的
	 * 
	 * @param page
	 *            第几页，传1进来
	 * @param isAllCountry
	 *            表示拿全球的https还是只拿中国的,0 为中国，1为世界
	 * */
	private void getIpByProxyLetushide(final SimpleObject proxy,
			final int page, final int isAllCountry) throws Exception {
		String url, refer = null;
		if (isAllCountry == 0) {
			if (page <= 1)
				url = "http://letushide.com/filter/all,all,cn/list_of_free_CN_China_proxy_servers";
			else {
				url = "http://letushide.com/filter/all,all,cn/" + page
						+ "/list_of_free_CN_China_proxy_servers";
				refer = "http://letushide.com/filter/all,all,cn/list_of_free_CN_China_proxy_servers";
			}
		} else {
			if (page <= 1)
				url = "http://letushide.com/filter/https/list_of_free_HTTPS_proxy_servers";
			else {
				url = "http://letushide.com/filter/https/" + page
						+ "list_of_free_HTTPS_proxy_servers";
				refer = "http://letushide.com/filter/https/list_of_free_HTTPS_proxy_servers";
			}
		}
		getUrl(url, refer, new AbstractProcessorObserver(util,
				WaringConstaint.PROXY_LETUSHIDE) {
			@Override
			public void afterRequest(SimpleObject context) {
				if (context == null)
					return;
				Document doc = ContextUtil.getDocumentOfContent(context);
				Element table = doc.getElementById("basic");
				Element tbody = table.getElementsByTag("tbody").first();
				Elements trs = tbody.getElementsByTag("tr");
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < trs.size(); i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					list.add(tds.get(1).text() + ":" + tds.get(2).text());
				}
				sum_all += list.size();
				if (list.size() > 0)
					exec(list, proxy, true);
				Element pageEle = doc.getElementById("page");
				// if(page==null) 不能翻页！让他报错去！
				int page_all = pageEle.children().size();
				if (page < page_all) {
					try {
						getIpByProxyLetushide(proxy, page + 1, isAllCountry);
					} catch (Exception e) {
						// TODO log记录,翻页之后的结构变了
						e.printStackTrace();
					}
				}
			}
		});
		return;
	}

	/**
	 * <p>
	 * 从http://spys.ru/free-proxy-list/CN/拿代理，但是能看到的代理port采用加密，本方法拿http://txt.
	 * proxyspy.net/proxy.txt的，提供了250个可以直接拿的，1小时更新一次差不多
	 * 根据测试，每次36个ip，大约可以拿到20,15,15
	 * */
	private void getIpByProxyspy(final SimpleObject proxy) throws Exception {
		getUrl("http://txt.proxyspy.net/proxy.txt", null,
				new AbstractProcessorObserver(util, WaringConstaint.PROXY_SPY) {
					@Override
					public void afterRequest(SimpleObject context) {
						if (context == null)
							return;
						Pattern p = Pattern.compile(
								"(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)",
								Pattern.CASE_INSENSITIVE);
						Matcher matcher = p.matcher(ContextUtil
								.getContent(context));
						List<String> list = new ArrayList<String>();
						while (matcher.find()) {
							String str = matcher.group();
							list.add(str);
						}
						sum_all += list.size();
						if (list.size() > 0)
							exec(list, proxy, true);
					}
				});
		return;
	}

	/**
	 * <p>
	 * 从http://proxy.com.ru/gaoni/list_1.html拿一遍，大概200~250个
	 * <p>
	 * 暂时不用！！
	 * 
	 * @param page
	 *            页数，传1进来即可
	 * */
	private void getIpByProxyRu(final SimpleObject proxy, final int page)
			throws Exception {
		String url = "http://proxy.com.ru/gaoni/list_" + page + ".html";
		getUrl(url, null, new AbstractProcessorObserver(util,
				WaringConstaint.PROXY_PROXYRU) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context);
				Elements tables = doc.getElementsByTag("table");
				for (int i = 0; i < tables.size(); i++) {
					if (tables.get(i).text().contains("编号")
							&& tables.get(i).text().contains("端口")) {
						// System.out.println("1");
						// System.out.println(doc);
					}
				}

			}
		});
	}

	/**
	 * <p>
	 * 从http://spys.ru/free-proxy-list/CN/拿代理,http与https分别拿不同的页面
	 * http最多能拿到200个,，https的不一定 大概200个验证最早的是在一天前，半实时验证
	 * <p>
	 * 
	 * @param anonymity
	 *            0为全部，1为高匿&普匿，3为普匿，4为高匿
	 * @param SSL
	 *            0为全部 ，1为SSL+
	 *            <p>
	 *            已解密
	 * */
	private void getIpBySpysRu(final SimpleObject proxy, String anonymity,
			String SSL) {
		String site = proxy.getString(ProxyManager.CONTEXT_SITE);
		// http的就不要加SSL+的参数限制了，容易抓不到200
		// xf1:匿名限制,0为ALL,4为HIA
		// xf2:SSL限制,0为ALL,1为SSL+，2为SSL-
		// xf4:端口限制,没啥用
		// xpp:3是200个~就用这个就行了，反正不能翻页
		String[][] pairs = { { "xf1", "" }, { "xf2", "" }, { "xf4", "0" },
				{ "xpp", "3" } };
		// if (site == null)
		// pairs[1][0] = "0";
		// else
		// //暂时全都ALL的
		pairs[0][1] = anonymity;
		pairs[1][1] = SSL;
		postUrl("http://spys.ru/free-proxy-list/CN/",
				"	http://spys.ru/free-proxy-list/CN/", pairs,
				new AbstractProcessorObserver(util,
						WaringConstaint.PROXY_SPYSRU) {
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil
								.getDocumentOfContent(context);
						String js = null;// 这样如果没获取到直接报错！
						Elements scripts = doc.getElementsByTag("script");
						for (int j = 0; j < scripts.size(); j++) {
							// System.out.println(scripts.get(j).html());
							if (scripts.get(j).html().contains("eval")) {
								js = scripts.get(j).html();
								break;
							}
						}
						Invocable inv = getInv(js);
						Elements trs = doc.getElementsByTag("tr");
						List<String> list = new ArrayList<String>();
						for (int i = 0; i < trs.size(); i++) {
							Element tr = trs.get(i);
							if (tr.text().contains("China")
									&& tr.hasAttr("class")) {// 因为只抓中国的200嘛，就暂时先这么判断吧
								String ip = tr.child(0).child(1).text();
								String[] encodings = tr.child(0).child(1)
										.child(0).html().split("\\+");
								// System.out.println(encodings[1]);
								String port = "";
								for (int k = 1; k < encodings.length; k++) {
									String str = encodings[k];
									str = str.replaceAll("\\)\\)", "\\)");
									// System.out.print(str+" ");
									try {
										Object re = inv.invokeFunction(
												"encode", str);
										// System.out.println(re.toString());
										Double d = Double.parseDouble(re
												.toString());
										port += d.intValue();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								// System.out.println(ip+":"+port);
								list.add(ip + ":" + port);
							}
						}
						// doc.get
						sum_all += list.size();
						if (list.size() > 0)
							exec(list, proxy, true);
					}
				});
	}

	/**
	 * <p>
	 * http://spys.ru/free-proxy-list/CN端口加密，下位解密方法
	 * 
	 * @param 当前页面的加密代码
	 * @return Invocable ScriptEngine的脚本接口，对同一页面无需重复解密，直接调用js函数即可
	 * @see:inv.invokeFunction("encode",encodingString)
	 * @throws Exception
	 * */
	private Invocable getInv(String js_eval) {
		try {
			if (js_eval.contains("eval"))
				js_eval = js_eval.replaceFirst("eval", "");
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("javascript");
			engine.eval("function e(str){return " + js_eval + "}");
			Invocable inv = (Invocable) engine;
			Object re = inv.invokeFunction("e", "");
			engine.eval("eval(\"" + re.toString() + "\");"
					+ "function encode(str){return eval(str)}");
			inv = (Invocable) engine;
			return inv;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 匿名性和协议选项要单独写 调用这个类，可以把所需的全部拿下来 匿名选项全都要 http选项的话就不做https验证
	 * 
	 * @throws Exception
	 * */
	private void getIpByCn379(SimpleObject proxy) throws Exception {
		int pmode = ProxyManagerFactory.getProxyMode(proxy);
		// System.out.println(pmode);
		// System.out.println(ProxyManager.PROXY_MODE_HTTP+" "+ProxyManager.PROXY_MODE_HTTPS+" "+ProxyManager.PROXY_MODE_HTTP_HTTPS);
		// getIpByCn379(proxy, "&anonymoustype=4", "", true);
		String[] anonyAll = { "&anonymoustype=4", "&anonymoustype=3",
				"&anonymoustype=2" };
		for (String anonyIndex : anonyAll) {
			// http的不做https验证，数量多而且通过不了
			getIpByCn379(proxy, anonyIndex, "&proxytype=0", false);
			getIpByCn379(proxy, anonyIndex, "&proxytype=1", true);
		}
	}

	/**
	 * 我凑，这个网站太NB了http://www.cn379.cn~真尼玛厉害
	 * http://www.cn379.cn的免费匿名代理ip接口获取，目测能拿到500个左右http&https的
	 * 
	 * @param anonymoustye
	 *            匿名选项（0全部，1透明，2普通匿名，3高级匿名，4超级匿名）
	 * @param httpType
	 *            proxytype=0&proxytype=1
	 * */
	private void getIpByCn379(final SimpleObject proxy, String anonymoustye,
			String httpType, final boolean needHttpsCheck) throws Exception {
		String url = "http://www.cn379.cn/ip.php?getnum=9999&isp=0"
				+ anonymoustye + "&start=&ports=&ipaddress=&area=1" + httpType
				+ "&api=71daili";
		getUrl(url, null, new AbstractProcessorObserver(util,
				WaringConstaint.PROXY_CN379) {
			@Override
			public void afterRequest(SimpleObject context) {
				if (context == null)
					return;
				// 正则写的可以用，有点慢
				// Pattern p = Pattern.compile(
				// "(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)",
				// Pattern.CASE_INSENSITIVE);
				// Matcher matcher = p.matcher(ContextUtil
				// .getContent(context));
				// List<String> list = new ArrayList<String>();
				// while (matcher.find()) {
				// String str = matcher.group();
				// list.add(str);
				// }
				Document doc = ContextUtil.getDocumentOfContent(context);
				Element div = doc.getElementsByTag("div").first();
				if (div == null || div.text().length() < 10)
					return;
				List<String> list = Arrays.asList(div.text().split(" "));
				sum_all += list.size();
				if (list.size() > 0)
					exec(list, proxy, needHttpsCheck);
			}
		});
	}

	/**
	 * 
	 * */
	private void exec(List<String> ips, final SimpleObject proxy,
			final boolean needHttpsCheck) {
		for (String ip1 : ips) {
			final String[] ip2 = ip1.split(":");
			if (ip2.length != 2 || ip2[0].length() < 7 || ip2[1].length() < 1)
				continue;
			ProxyManager pm = ProxyManagerFactory.getProxyManager();
			if (pm.existsProxy(ip2))
				continue;
			String x = ip2.toString();
			if (!queue.contains(x)) {
				queue.add(x);
				while (threadNum.get() > /* 10000 */maxThreadNum) {
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Future<Boolean> future = ThreadPoolManager.getThreadPool()
						.submit(new Callable<Boolean>() {
							@Override
							public Boolean call() throws Exception {
								threadNum.incrementAndGet();
								try {
									exec(ip2[0], ip2[1], proxy, needHttpsCheck);
								} catch (Exception e) {
									e.printStackTrace();
								}
								threadNum.decrementAndGet();
								return true;
							}
						});
				futureList.add(future);
			}
		}
	}

	private void exec(String ip, String port, SimpleObject proxy,
			boolean needHttpsCheck) {
		SimpleObject proxy1 = new SimpleObject();
		proxy1.put(ProxyManager.CONTEXT_IS_AUTHENTICATE, true);
		//proxy1.putAll(proxy);
		ProxyAuthentication pa = new ProxyAuthentication();
		// String x = ip + ":" + port;
		String[] hp = new String[] { ip, port };
		// 这么写好像会使得重复的代理可能一起加进去
		// ProxyManager pm = ProxyManagerFactory.getProxyManager();
		// if(pm.existsProxy(hp))
		// return;
		// System.out.println("------------" + x + "-------------");
		// String site = proxy.getString(ProxyManager.CONTEXT_SITE);
		int pmode = ProxyManagerFactory.getProxyMode(proxy);
		boolean pHttp = false, pHttps = false;
		long httpTime = 0, httpsTime = 0;
		pHttp = pa.authenticateHttp(ip, port);
		httpTime = pa.getResponseTime();
		// 因为有些网站支持参数选择，选项直接选http的就不要https验证了，99.9%都过不了
		if (needHttpsCheck) {
			pHttps = pa.authenticateHttps(ip, port);
			httpsTime = pa.getResponseTime();
		}
		ProxyManager pm = ProxyManagerFactory.getProxyManager();
		if (needHttpsCheck) {
			if (pHttp & pHttps) {
				if (httpTime + httpsTime < 20000) {
					if (!pm.existsProxy(hp)) {
						proxy1.put(ProxyManager.CONTEXT_PROXY_MODE,
								ProxyManager.PROXY_MODE_HTTP_HTTPS);
						pm.addProxy(proxy1, hp);
						sum++;
					}
					// test_sum_https_http++;
				}
			} else if (pHttps) {
				if (httpsTime < 10000) {
					if (!pm.existsProxy(hp)) {
						proxy1.put(ProxyManager.CONTEXT_PROXY_MODE,
								ProxyManager.PROXY_MODE_HTTPS);
						pm.addProxy(proxy1, hp);
						if (pmode == ProxyManager.PROXY_MODE_HTTPS)
							sum++;
					}
					// test_sum_https++;
				}
			} else if (pHttp) {
				if (httpTime < 10000) {
					if (!pm.existsProxy(hp)) {
						proxy1.put(ProxyManager.CONTEXT_PROXY_MODE,
								ProxyManager.PROXY_MODE_HTTP);
						pm.addProxy(proxy1, hp);
						if (pmode == ProxyManager.PROXY_MODE_HTTP)
							sum++;
					}
					// test_sum_http++;
				}
			} else {
				// log
			}
		}
		// needHttpsCheck==false的情况，只看http
		else {
			if (pHttp) {
				if (httpTime < 10000) {
					if (!pm.existsProxy(hp)) {
						proxy1.put(ProxyManager.CONTEXT_PROXY_MODE,
								ProxyManager.PROXY_MODE_HTTP);
						pm.addProxy(proxy1, hp);
						if (pmode == ProxyManager.PROXY_MODE_HTTP)
							sum++;
					}
					// test_sum_http++;
				}
			}
		}
	}

	/**
	 * <p>
	 * 获取一定数量的代理加入线程池 由于代理网站不可控，所以获取代理的让其数量尽可能多，不能保证数量，但刻意通过responseTime控制质量
	 * <p>
	 * 为保证获取的代理不被浪费，假如获取的代理不符合本次要求，但是符合其他的要求，也会带相应参数加入代理池，但返回值sum仅为符合要求的代理数
	 * <p>
	 * 本方法抓取的网站全部为实时更新（最长1小时，最快1分钟） 当返回值不够数量，请再次调用本方法。
	 * 
	 * @author Pat.Liu
	 * @return sum 添加进代理池符合要求的代理数量
	 * @param proxy
	 * @throws 对网站结构部分更新的异常进行反馈
	 *             （ps:AbstractCrawler里好像抓到异常了...如果没有请忽略...）
	 * */
	public int getIpsByIntervals(SimpleObject proxy) throws Exception {
		sum = 0;
		proxy.put(ProxyManager.CONTEXT_IS_AUTHENTICATE, true);
		// test_sum_http=0;
		// test_sum_https=0;
		// test_sum_https_http=0;
		getIpByProxyLetushide(proxy, 1, 0);
		getIpByProxynova(proxy, 0);
		spider.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Future<Boolean> future : futureList) {
			if (future.isDone()) {
				continue;
			}
			try {
				// Boolean isAcceptingRequests =
				future.get(10, TimeUnit.SECONDS);
				// this waits for 10 seconds, throwing TimeoutException if not
				// done
			} catch (Exception e) {
				future.cancel(true);
			}
		}
		close();
		return sum;
	}

	public int getIps(SimpleObject proxy) throws Exception {
		sum = 0;
		proxy.put(ProxyManager.CONTEXT_IS_AUTHENTICATE, true);
		// List<String> listReturn = new ArrayList<String>();
		// if (isHttps)
		// getHttpsIpBy56(sum, proxy);// 参数可以选择,不取上一次取过的
		// else
		// getHttpIpBy56(sum, proxy);

		// getIpByProxyspy(proxy);

		// getIpBySpysRu(proxy, "3", "0");
		// getIpBySpysRu(proxy, "4", "0");
		getIpByCn379(proxy);
		spider.start();

		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Future<Boolean> future : futureList) {
			if (future.isDone()) {
				continue;
			}
			try {
				// Boolean isAcceptingRequests =
				future.get(15, TimeUnit.SECONDS);
				// this waits for 10 seconds, throwing TimeoutException if not
				// done
			} catch (Exception e) {
				future.cancel(true);
			}
		}
		close();
		// while (threadNum.get() > 0) {
		// char c = (char) System.in.read();
		// if (c == '1')
		// break;
		// Thread.sleep(1000);
		// System.out.println(sum_all + "," + threadNum.get());
		// System.out.println("http:"+test_sum_http+" https:"+test_sum_https+" http&https:"+test_sum_https_http);
		// }
		// list.addAll(其他网站);
		// list.add(代理池中所有ip)
		return sum;
	}

	/**
	 * <p>
	 * 测试结果：共抓取450+个
	 * <p>
	 * 要求http时,40左右
	 * <p>
	 * 要求https时,35左右
	 * <p>
	 * 要求http&https时,30左右
	 * */
	public static void main(String[] args) {
		// System.out.println(GetProxy.getIpBy56(50));
		GetProxy a = new GetProxy();
		try {
			// a.getIps(10, false);
			SimpleObject proxy = new SimpleObject();
			proxy.put(ProxyManager.CONTEXT_PROXY_MODE,
					ProxyManager.PROXY_MODE_HTTP);
			a.getIps(proxy);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("/n/n/n/n/n/n/n出错了！！！！！/n/n/n/n/n/n/n");
			e.printStackTrace();
		}
	}

}
