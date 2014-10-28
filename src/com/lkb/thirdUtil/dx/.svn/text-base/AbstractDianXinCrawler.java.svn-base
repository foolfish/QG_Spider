/**
 * 
 */
package com.lkb.thirdUtil.dx;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Request;
import com.lkb.robot.proxy.ProxyManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.thirdUtil.AbstractCrawler;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.util.HttpUtil;

/**
 * @author think
 * @date 2014-8-2
 */
public abstract class AbstractDianXinCrawler extends AbstractCrawler {
	/*if(theName == "集团"){
		shopId = "10000";
	}else if(theName == "北京"){
		shopId = "10001";
	}else if(theName == "天津"){
		shopId = "10002";
	}else if(theName == "辽宁"){
		shopId = "10005";
	}else if(theName == "河北"){
		shopId = "10006";
	}else if(theName == "山西"){
		shopId = "10007";
	}else if(theName == "内蒙古"){
		shopId = "10008";
	}else if(theName == "宁夏"){
		shopId = "10009";
	}else if(theName == "黑龙江"){
		shopId = "10010";*/
	protected String shopId = "10005";
	protected String areaName = "辽宁";
	protected String customField1 = "3";
	protected String customField2 = "08";
	protected String toStUrl = "&toStUrl=http://ln.189.cn/group/bill/bill_owed.action";
	protected String ssoUrl;
	protected Collection<DianXinDetail> detailList = new ArrayList();
	protected Collection<DianXinTel> telList = new ArrayList();
	protected Collection<TelcomMessage> messageList = new ArrayList();
	protected Collection<DianXinFlow> flowList = new ArrayList();
	protected Collection<DianXinFlowDetail> flowDetailList = new ArrayList();
	protected String UAM_CHAR_SET = "utf-8";
	protected void saveVerifyCode(final String prefix, final String phone) {   
		//String url = "https://uam.ct10000.com/ct10000uam/login"; //?service=http://www.hi.189.cn/dqmh/Uam.do?method=loginJTUamGet";
		goLoginReq(prefix, phone);
    }
	private void saveVerifyImage(SimpleObject context, final String prefix, final String phone) {
		data.put("checkVerifyCode","1");
		//DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
		String picName =  prefix + "_dx_code_" + phone + "_" + (int) (Math.random() * 1000) + "3ec";
		try {
			String imgName = saveFile("https://uam.ct10000.com/ct10000uam/validateImg.jsp?"+Math.random(), "https://uam.ct10000.com/", null, picName, true);
			//String imgName = "https://uam.ct10000.com/ct10000uam/validateImg.jsp;JSESSIONID=" + sid;
			data.put("imgName", imgName);
			/*logger.info("img!@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			DebugUtil.printCookieData(ContextUtil.getCookieStore(context), "uam.ct10000.com");
			logger.info("img!@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");*/
		} catch (Exception e) {
			notifyStatus();
		}
	}
	
	
	
	public void goLoginReq() {     
		goLoginReq(null, null);
	}

	private void goLoginReq(final String prefix, final String phone) {       
		setUniqueHttpAndHttpsProxy();		

		Request req = new Request(getOpenLoginPage());
		//重写向的url带有空格，需要转义，不允许自动重定向
		req.putExtra("redirectsEnabled", false);
		req.setCharset(UAM_CHAR_SET);
		req.putHeader("Referer", "http://www.189.cn/dqmh/login/loginJT.jsp");
		//Mozilla/5.0 (Windows NT 5.1; rv:28.0) Gecko/20100101 Firefox/28.0
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.ZGDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				/*logger.info("***********************************************************************");
				DebugUtil.printCookieData(ContextUtil.getCookieStore(context), "uam.ct10000.com");
				logger.info("***********************************************************************");
				*/
				parseLoginPage(context, prefix, phone);
				
			}
		});
		spider.addRequest(req);
	}
	protected String getOpenLoginPage() {
		return "http://www.189.cn/dqmh/Uam.do?method=loginUamSendJT&logintype=telephone&shopId=" + shopId + "&loginRequestURLMark=http://www.189.cn/dqmh/login/loginJT.jsp&date=" + System.currentTimeMillis();
	}


	protected void parseLoginPage(SimpleObject context, final String prefix, final String phone) {   
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		//DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
		if (HttpUtil.isMovedStatusCode(scode)) {
			HttpResponse resp = ContextUtil.getResponse(context);
			Header h1 = resp.getFirstHeader("Location");
			String nexturl = h1.getValue();
			if (nexturl == null) {
				logger.error("Error : No Redirect URL");    		
			} else {
				nexturl = fixedFullUrl(nexturl);
				Request req = new Request(nexturl.replaceAll(" ", "%20"));
				req.setCharset(UAM_CHAR_SET);
				req.putHeader("Referer", "http://www.189.cn/dqmh/login/loginJT.jsp");
				req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.ZGDX_2) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLoginPage(context, prefix, phone);
					}
				});
				spider.addRequest(req);
			}
		} else {
			if (prefix != null) {
				//com.lkb.debug.DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
				saveVerifyImage(context, prefix, phone);
			} else {
				parseLoginStep2(context);
			}
		}
	}
	protected String fixedFullUrl(String nexturl) {
		String prefix = "https://uam.ct10000.com";
		if (!nexturl.startsWith(prefix)) {
			nexturl = prefix + nexturl;
		}
		return nexturl;
	}
	protected void parseLoginStep2(SimpleObject context) {   
		String text = ContextUtil.getContent(context);
		if (text == null) {
			return;
		}
		String phone1 = phoneNo;
		String password1 = password;
		
		String n = StringUtil.subStr("strEnc(username,", ");", text).trim();
		if (!StringUtils.isBlank(n)) {
			String[] stra = n.trim().replaceAll("\'", "").split(",");
			//pwd, digit, f, s
			phone1 = executeJsFunc("des/tel_com_des.js", "strEnc", phoneNo, stra[0], stra[1], stra[2]);
			password1 = executeJsFunc("des/tel_com_des.js", "strEnc", password, stra[0], stra[1], stra[2]);
		}
		Document doc = ContextUtil.getDocumentOfContent(context); 
		
		Elements form = doc.select("form#c2000004");

		Request req = new Request(fixedFullUrl(form.attr("action")));
		req.setMethod("POST");
		req.initNameValuePairs(12);

		req.addNameValuePairs("lt", form.select("input[name=lt]").attr("value"));
		req.addNameValuePairs("_eventId", "submit");
		req.addNameValuePairs("forbidpass", "null");
		
		req.addNameValuePairs("areaname", areaName);
		req.addNameValuePairs("password", password1);
		req.addNameValuePairs("authtype", "c2000004");
		
		req.addNameValuePairs("customFileld01", customField1);
		
		req.addNameValuePairs("customFileld02", customField2);
		req.addNameValuePairs("forbidaccounts", "null");
		req.addNameValuePairs("open_no", "c2000004");
		req.addNameValuePairs("username", phone1);
		req.addNameValuePairs("randomId", authCode == null ? "" : authCode);
		req.setCharset(UAM_CHAR_SET);
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.ZGDX_3) {			
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginStep3(context);
			}
		});
		spider.addRequest(req);
	}
	protected void parseLoginStep3(SimpleObject context) { 
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		if (HttpUtil.isMovedStatusCode(scode)) {
			HttpResponse resp = ContextUtil.getResponse(context);
			Header h1 = resp.getFirstHeader("Location");
			String nexturl = h1.getValue();
			if (nexturl == null) {
				logger.error("Error : No Redirect URL");    		
			} else {
				Request req = new Request(nexturl);
				req.setCharset(UAM_CHAR_SET);
				req.putHeader("Referer", "http://uam.ct10000.com/ct10000uam/login");
				req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.ZGDX_4) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLoginStep3(context);
					}
				});
				spider.addRequest(req);
			}
		} else {
			parseLoginStep4(context);
		}
	}
	private void parseLoginStep4(SimpleObject context) {  
		Document doc = ContextUtil.getDocumentOfContent(context); 
		Elements e1 = doc.select("form#c2000004");
		if (e1.size() > 0) {
			data.put("errMsg", e1.select("td#status2").text());
			setStatus(STAT_STOPPED_FAIL);
			notifyStatus();
			return;
		}
		e1 = doc.select("form#login_form");
		if (e1.size() > 0) {
			data.put("errMsg", "登录失败，请重试!");
			setStatus(STAT_STOPPED_FAIL);
			notifyStatus();
			return;
		}
		String text = ContextUtil.getContent(context);

		String url = StringUtil.subStr("<script type='text/javascript'>location.replace('", "');</script>", text);
		if (StringUtils.isBlank(url.trim())) {
			
			if ("IBM HTTP Server".equalsIgnoreCase(doc.select("title").text())) {
				setStatus(STAT_LOGIN_SUC);
				//notifyStatus();
				ssoLogin(context);
			} else {
				data.put("fail", true);
				setStatus(STAT_STOPPED_FAIL);
				notifyStatus();
				logger.error("Login Fail.....");
			}
			
			return;
		}
		getUrl(url, null, new Object[] {UAM_CHAR_SET}, new AbstractProcessorObserver(util, WaringConstaint.ZGDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				setStatus(STAT_LOGIN_SUC);				
				ssoLogin(context);
			}
		});
	}
	private void ssoLogin(SimpleObject context) {
		if (ssoUrl == null) {
			ssoUrl = "http://www.189.cn/dqmh/frontLink.do?method=linkTo&shopId=" + shopId + toStUrl;
		}
		getUrl(ssoUrl, null, new Object[] {UAM_CHAR_SET}, new AbstractProcessorObserver(util, WaringConstaint.ZGDX_6) {			
			@Override
			public void afterRequest(SimpleObject context) {
				ssoLogin1(context);
			}
		});
	}
	protected void ssoLogin1(SimpleObject context) {
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		if (HttpUtil.isMovedStatusCode(scode)) {
			HttpResponse resp = ContextUtil.getResponse(context);
			Header h1 = resp.getFirstHeader("Location");
			String nexturl = h1.getValue();
			if (nexturl == null) {
				logger.error("Error : No Redirect URL");    		
			} else {
				Request req = new Request(nexturl);
				req.setCharset(UAM_CHAR_SET);
				req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.ZGDX_7) {
					@Override
					public void afterRequest(SimpleObject context) {
						ssoLogin1(context);
					}
				});
				spider.addRequest(req);
			}
		} else {
			endSSOLogin(context);
		}
	}
	private void endSSOLogin(SimpleObject context) {
		String text = ContextUtil.getContent(context);
		String url = null;
		if (text != null) {
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements es = doc.select("script");
			for(int i=0; i < es.size(); i++) {
				String html = es.get(i).html();
				if (html.indexOf("location.replace") >= 0) {
					url = StringUtil.subStr("location.replace(", ");", html);
					url = url.substring(1, url.length() - 1);
					break;
				}
			}
			/*text = text.replaceAll("<script type='text/javascript'>", "<script>").replaceAll("<script type='javascript'>", "<script>");
			url = StringUtil.subStr("<script>location.replace('", "');</script>", text);*/
		}
		if (!StringUtils.isBlank(url)) {
			getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.ZGDX_8) {
				/*@Override
				public void preparedData(SimpleObject context) {
					super.preparedData(context);
					if (ContextUtil.getError(context) != null) {
						completeLogin(context); 
					}
				}*/
				public void afterRequest(SimpleObject context) {
					ssoLogin1(context);
				}
			});
		} else {
			//notifyStatus();
			completeLogin(context); 
		}
	}
	private void ssoLogin2(SimpleObject context) {
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		if (HttpUtil.isMovedStatusCode(scode)) {
			HttpResponse resp = ContextUtil.getResponse(context);
			Header h1 = resp.getFirstHeader("Location");
			String nexturl = h1.getValue();
			if (nexturl == null) {
				logger.error("Error : No Redirect URL");    		
			} else {
				ssoLogin2(context);
			}
		} else {
			completeLogin(context); 
		}
	}
	private void completeLogin(SimpleObject context) {
		resetDefaultProxy();
		onCompleteLogin(context); 
	}
	protected abstract void onCompleteLogin(SimpleObject context);
	public Collection<DianXinDetail> getDetailList() {
		return detailList;
	}
	public Collection<DianXinTel> getTelList() {
		return telList;
	}
	
	public Collection<TelcomMessage> getMessageList() {
		return messageList;
	}
	public Collection<DianXinFlow> getFlowList() {
		return flowList;
	}
	public Collection<DianXinFlowDetail> getFlowDetailList() {
		return flowDetailList;
	}
	
	public void addMonthBill(DianXinTel tel) {
		if (tel != null) {
			telList.add(tel);
		}
	}
	public void addDetail(DianXinDetail tel) {
		if (tel != null) {
			detailList.add(tel);
		}
	}
	public void addMessage(TelcomMessage tel) {
		if (tel != null) {
			messageList.add(tel);
		}
	}
	public void addFlowBill(DianXinFlow flow) {
		if (flow != null) {
			flowList.add(flow);
		}
	}
	public void addFlowDetail(DianXinFlowDetail flowDetail) {
		if (flowDetail != null) {
			flowDetailList.add(flowDetail);
		}
	}
	
	/*public void checkVerifyCode(final String userName) {   
			
	}
	public void showImgWhenSendSMS(final String phone) {   
		
    }
	public void sendSmsPasswordForRequireCallLogService(String authValue) {
	
	}
	public void requestAllService(String duanxin, String tupian) {
		
	}*/
	
	/**
	* <p>Title: iterateGetUrl</p>
	* <p>Description: 迭代进行get请求（若请求中存在location.raplace重定向，则进行迭代，若不存在，则返回），
	* 需要重写handleContext方法，用于处理get请求最终返回页面</p>
	* @author Jerry Sun
	* @param url
	*/
	protected void iterateGetUrl(String url){
		getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.QHDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				String url = StringUtil.subStr("<script type='text/javascript'>location.replace('", "');</script>", content);
				if (StringUtils.isBlank(url.trim())){
					handleContext(content);
					return;
				}
				iterateGetUrl(url);
			}
		});
	}
	
	/**
	* <p>Title: handleContext</p>
	* <p>Description: 处理迭代get请求最终返回页面</p>
	* @author Jerry Sun
	* @param context
	*/
	protected void handleContext(String context) {
	}
	
}
