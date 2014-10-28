/**
 * 
 */
package com.lkb.thirdUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.CookieStore;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkb.bean.SimpleObject;
import com.lkb.bean.SimpleValue;
import com.lkb.bean.User;
import com.lkb.context.ContextLifecycle;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Request;
import com.lkb.robot.Spider;
import com.lkb.robot.proxy.ProxyManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.request.ProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.robot.util.CookieStoreUtil;
import com.lkb.robot.util.RobotUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.JSUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;
//import com.lkb.debug.DebugUtil;


/**
 * @author think
 * @date 2014-7-30
 */
public abstract class AbstractCrawler extends StatusTracker {
	public static final int LOGIN_SUC = 1;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	//临时数据
	protected SimpleObject data = new SimpleObject();
	//和登录状态同时保存的数据
	protected SimpleObject entity = new SimpleObject();
	protected String phoneNo;
	protected String password;
	protected String authCode;
	protected String mobileCode;
	protected Spider spider;
	protected int login;
	protected User user;
	protected WarningUtil util;
	private boolean test;
	protected Date maxTelTs;
	protected Date maxDetailTs;
	protected Date maxMessageTs;
	protected Date maxFlowTs;
	protected Date maxFlowDetailTs;
	
	public void checkVerifyCode(final String phone) {

	}
	public void goLoginReq() {     

	}
	public void requestAllService() {

	}
	/*
	 protected void onStartLogin(SimpleObject context)
	 protected void onCompleteLogin(SimpleObject context)
	 protected abstract void isLogin(SimpleObject context)
	 */
	public boolean isTest() {
		return test;
	}
	public void setTest(boolean test) {
		this.test = test;
	}
	public SimpleObject getData() {
		return data;
	}
	public User getUser() {
		return user;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public int getLogin() {
		return login;
	}
	public String getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
	public void setLogin(int login) {
		this.login = login;
	}
	public void loginSuccess() {
		this.login = LOGIN_SUC;
	}
	public boolean isSuccessLogin() {
		return this.login == LOGIN_SUC;
	}
	protected void getUrl(String url, String referer, ProcessorObserver observer) {
		getUrl(url, referer, null, observer);
	}
	protected void getUrl(String url, String referer, Object[] param, ProcessorObserver observer) {
		getUrl(url, referer, param, null, observer);
	}
	protected void getUrl(String url, String referer, Object[] param, String[][] headers, ProcessorObserver observer) {
		if (url == null) {
			logger.error("Error : No URL");    		
		} else {
			Request req = new Request(url);
			//req.setMethod("POST");
			if (referer != null) {
				req.putHeader("Referer", referer);
			}
			if (observer != null) {
				req.addObjservers(observer);
			}

			setRequest(param, headers, req);
			spider.addRequest(req);
		}
	}
	protected void postUrl(String url, String referer, String[][] nameValuePairs, ProcessorObserver observer) {
		postUrl(url, referer, null, nameValuePairs, observer);
	}
	protected void postUrl(String url, String referer, Object[] param, String[][] nameValuePairs, ProcessorObserver observer) {
		postUrl(url, referer, param, nameValuePairs, null, observer);
	}
	protected void postUrl(String url, String referer, Object[] param, String[][] nameValuePairs, String[][] headers, ProcessorObserver observer) {
		if (url == null) {
			logger.error("Error : No URL");    		
		} else {
			Request req = new Request(url);
			req.setMethod("POST");
			if (referer != null) {
				req.putHeader("Referer", referer);
			}
			if (nameValuePairs != null) {
				req.setNameValuePairs(nameValuePairs); 
			}
			if (observer != null) {
				req.addObjservers(observer);
			}

			setRequest(param, headers, req);
			spider.addRequest(req);
		}
	}
	private void setRequest(Object[] param, String[][] headers, Request req) {
		if (param != null) {
			int len = param.length;
			if (len > 0 && param[0] != null) {
				req.setCharset(param[0].toString());
			}
			if (len > 1 && param[1] != null) {
				req.setPostXml(param[1].toString());
			}
			if (len > 2 && param[2] != null) {
				req.setUseProxy(true);
				req.putExtra(Request.PROXY, param[2]);
			}
			if (len > 3 && param[3] != null) {
				req.setPostContentType(param[3].toString());
			}
		}
		if (headers != null) {
			for(String[] header : headers) {
				req.putHeader(header[0], header[1]);
			}
		}
	}
	public String saveFile(String url, String referer, String host, String picName, final boolean notifyStatus) throws Exception {
		if (isTest()) {
			return saveFileTest(url, referer, host, picName, notifyStatus);
		}
		return saveFile2(url, referer, host, picName, notifyStatus);
	}
	private String saveFile2(String url, String referer, String host, String picName, final boolean notifyStatus) throws Exception {
		String authcodePath = InfoUtil.getInstance().getInfo("road", "server.full.path");
		final String suffix = (int) (Math.random() * 100000) + "-" + System.currentTimeMillis() + ""; //DateUtils.formatDate(new Date(), "yyyyMMdd");
		/*
		String path1 = suffix;

		File file2 = new File(path1);
		if (!file2.exists() && !file2.isDirectory()) {
			file2.mkdir();
		}
		*/
		//final String destfilename = path1 + "/"+ picName;
		Request req = new Request(url);
		//req.setMethod("POST");
		if (referer != null) {
			req.putHeader("Referer", referer);
		}
		if (host != null) {
			req.putHeader("Host", host);
		}
		req.putHeader("Accept", "image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
		//req.putHeader("Accept", "	text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		req.putHeader("Accept-Encoding", "gzip, deflate");
		req.putHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		req.putHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
		req.putExtra(Request.STREAM, "true");
		final long sts = System.currentTimeMillis();
		final String key = suffix + "-"+ picName;
		//final CountDownLatch latch = new CountDownLatch(1);
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.GDDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					InputStream is = ContextUtil.getInputStream(context);
					if (is != null) {
						//FileOutputStream output = new FileOutputStream(destfilename);
						//ByteArrayOutputStream output = new ByteArrayOutputStream();
						try {
							byte[] bs = IOUtils.toByteArray(is);
							Map<String,Object> map = new HashMap();
							//map = RobotUtil.getCacheMap(null, key);
							map.put("1", bs);
							RobotUtil.setCacheMap(null, key, map, 2 * 60);
							//IOUtils.copy(is, output);
							//Thread.sleep(100);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							IOUtils.closeQuietly(is);
							//IOUtils.closeQuietly(output);
						}
						logger.info("---save img ok time(s) :" + (System.currentTimeMillis() - sts) / 1000d);
						/*PicUpload picUpload = new PicUpload();
						picUpload.upload(destfilename);*/
					}

					//latch.countDown();
				} catch (Exception e) {
					logger.error("inputStream", e);
					e.printStackTrace();
				} finally {
					if (notifyStatus) {
						notifyStatus();
					}
				}
				logger.info("---save img end time(s) :" + (System.currentTimeMillis() - sts) / 1000d);
			}
		});
		spider.addRequest(req);
		//latch.await();

		return authcodePath + "/pt/service/img/" + key + InfoUtil.getInstance().getInfo("road", "server.img.auth.code.suffix");
	}
	public String saveFileTest(String url, String referer, String host, String picName, final boolean notifyStatus) throws Exception {
		String authcodePath = InfoUtil.getInstance().getInfo("road", "authcodePath");
		final String suffix = DateUtils.formatDate(new Date(), "MMdd");
		String path1 = authcodePath+suffix;

		File file2 = new File(path1);
		if (!file2.exists() && !file2.isDirectory()) {
			file2.mkdir();
		}
		final String destfilename = path1 + "/"+ picName + ".jpg";
		Request req = new Request(url);
		//req.setMethod("POST");
		if (referer != null) {
			req.putHeader("Referer", referer);
		}
		if (host != null) {
			req.putHeader("Host", host);
		}
		req.putHeader("Accept", "image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
		//req.putHeader("Accept", "	text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		req.putHeader("Accept-Encoding", "gzip, deflate");
		req.putHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		req.putHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
		req.putExtra(Request.STREAM, "true");
		final long sts = System.currentTimeMillis();
		//final CountDownLatch latch = new CountDownLatch(1);
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.GDDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				//com.lkb.debug.DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
				try {
					InputStream is = ContextUtil.getInputStream(context);
					if (is != null) {
						FileOutputStream output = new FileOutputStream(destfilename);
						//ByteArrayOutputStream output = new ByteArrayOutputStream();
						try {
							IOUtils.copy(is, output);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							IOUtils.closeQuietly(is);
							IOUtils.closeQuietly(output);
						}
						logger.info("---save img ok time(s) :" + (System.currentTimeMillis() - sts) / 1000d);
						/*PicUpload picUpload = new PicUpload();
						picUpload.upload(destfilename);*/
					}

					//latch.countDown();
				} catch (Exception e) {
					logger.error("inputStream", e);
					e.printStackTrace();
				} finally {
					if (notifyStatus) {
						notifyStatus();
					}
				}
				//logger.info("---save img end time(s) :" + (System.currentTimeMillis() - sts) / 1000d);
			}
		});
		spider.addRequest(req);
		//latch.await();
		data.put(destfilename, "imgName");
		return suffix + "/"+ picName;
	}
	public void printCookieData() {
		CookieStore cs = CookieStoreUtil.putContextToCookieStore(null, 1);
		DebugUtil.printCookieData(cs, null);
	}

	public void printData() {
		printSimpleValue(data);
	}
	public void printEntityData() {
		printSimpleValue(entity);
	}
	public void printSimpleValue(SimpleObject so) {
		Collection<Map.Entry<String, SimpleValue>> entryset = so.entrySet();
		for(Map.Entry<String, SimpleValue> entry : entryset) {
			logger.info(entry.getKey() + " : " + entry.getValue().toString());
		}
	}
	public void addPhoneRemain(BigDecimal b1) {
		if (user.getPhoneRemain()  != null) {
			user.getPhoneRemain().add(b1);
		} else {
			user.setPhoneRemain(b1);
		}
	}
	public String unescapeHtml(String text) {
		return StringEscapeUtils.unescapeHtml4(text);
	}
	//
	public static String executeJsFunc(String jsFile, String jsFuncName, Object... args) {
		String jsPath = InfoUtil.getInstance().getInfo("road","tomcatWebappPath")+"/js/" + jsFile;
		return JSUtil.executeJsFunc(jsFuncName, jsPath, args);		
	}
	/*public static String executeJsFuncWithEnv(String jsFile, String jsFuncName, Object... args) throws Exception {
		String jsPath = InfoUtil.getInstance().getInfo("road","tomcatWebappPath")+"/js/";
		Object s = JSUtil.executeJsFuncWithEnv(jsFuncName, jsPath + "envjs", jsPath + jsFile, args);
		return s == null ? null : s.toString();		
	}*/

	public Date getMaxTelTs() {
		return maxTelTs;
	}
	public void setMaxTelTs(Date maxTelTs) {
		this.maxTelTs = maxTelTs;
	}
	public Date getMaxDetailTs() {
		return maxDetailTs;
	}
	public void setMaxDetailTs(Date maxDetailTs) {
		this.maxDetailTs = maxDetailTs;
	}
	public Date getMaxMessageTs() {
		return maxMessageTs;
	}
	public void setMaxMessageTs(Date maxMessageTs) {
		this.maxMessageTs = maxMessageTs;
	}
	public Date getMaxFlowTs() {
		return maxFlowTs;
	}
	public void setMaxFlowTs(Date maxFlowTs) {
		this.maxFlowTs = maxFlowTs;
	}
	
	public Date getMaxFlowDetailTs() {
		return maxFlowDetailTs;
	}
	public void setMaxFlowDetailTs(Date maxFlowDetailTs) {
		this.maxFlowDetailTs = maxFlowDetailTs;
	}
	/**
	 * 将二位数组转为一个dwr框架post提交的参数格式
	 * @author JerrySun
	 * @param pairs
	 * @return key1=value1\r\nkey2=value2\r\n...
	 */
	protected String joinPairsToPostBodyForDWR(String[][] pairs){
		StringBuffer sb = new StringBuffer(); 
		for(int i=0;i<pairs.length;i++){
			String key = "";
			String value = "";
			for(int j=0;j<2;j++){
				if(j==0)
					key = pairs[i][j];
				else if (j==1)
					value = pairs[i][j];
			}
			sb.append(key).append("=").append(value).append("\r\n");
		}
		return sb.toString();
	}
	public static String valOfElement(Element e, String filter) {
		Elements es1 = e.select(filter);
		return es1.val();
	}

	
	/**
	* <p>Title: mapToArray</p>
	* <p>Description: 将map转为二维数组</p>
	* @author Jerry Sun
	* @param map
	* @return Object[][]
	*/
	protected Object[][] mapToArray(Map map){
		Object[][] array = new String[map.size()][2];  
		  
		Object[] keys = map.keySet().toArray();  
		Object[] values = map.values().toArray();  
		  
		for (int row = 0; row < array.length; row++) {  
		    array[row][0] = keys[row];  
		    array[row][1] = values[row];  
		}  
		return array;
	}

	public SimpleObject getEntity() {
		return entity;
	}
	protected void resetDefaultProxy() {
		spider.setProxyMethod(ProxyManager.PROXY_METHOD_MULTI);
		spider.setProxyMode(ProxyManager.PROXY_MODE_HTTP);
		spider.setProxyHolder(ProxyManager.PROXY_HOLDER_NONE);
	}
	protected void setUniqueHttpsProxy() {
		spider.setProxyMethod(ProxyManager.PROXY_METHOD_UNIQUE);
		spider.setProxyMode(ProxyManager.PROXY_MODE_HTTPS);
	}
	protected void setUniqueHttpProxy() {
		spider.setProxyMethod(ProxyManager.PROXY_METHOD_UNIQUE);
		spider.setProxyMode(ProxyManager.PROXY_MODE_HTTP);
	}
	protected void setUniqueHttpAndHttpsProxy() {
		spider.setProxyMethod(ProxyManager.PROXY_METHOD_UNIQUE);
		spider.setProxyMode(ProxyManager.PROXY_MODE_HTTP_HTTPS);
		spider.setProxyHolder(ProxyManager.PROXY_HOLDER_ONE);
	}
	public Spider getSpider() {
		return spider;
	}
	public void initForTest() throws Exception {
		spider.setDestroyWhenExit(false);
		ContextLifecycle cf = new ContextLifecycle();
		cf.initializing();
		setTest(true);
	}
}
