package com.lkb.thirdUtil.yd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.entity.CHeader;

/**
 * 江苏移动
 * 
 * @author Administrator 问题：登陆的时候第一次输入验证码，总会提示验证码输入有错（浏览器登陆）
 * 
 */
public class JSYDSpider {
	
	// 登陆post参数
	private static String mobile = "18361017664";
	private static String password = "888666";
	
	private static String loginType = "wsyyt";
	private static String icode = "";// 验证码
	private static String fromFlag = "doorPage";
	private static String l_key = "";// 关键参数，需要从cookie中获取
	private static String wt_dl123 = "";// l_key的来源
	private static String activepass = "";// 没用
	private static String wttype = "2";
	//private static HttpResponse httpResponse = null;
	// 详单查询post参数
	private static String reqUrl = "queryBillDetail";
	private static String busiNum = "QDCX";
	//private static String queryMonth = DateUtil.dateToStr6(new Date());// 当前月份201406
	private static String queryItem = "1";
	private static String operType = "3";
	
	private static String confirmFlg = "1";
	/*
	 * reqUrl:queryBillDetail busiNum:QDCX queryMonth:201406 queryItem:1
	 * operType:3 smsNum:405831 confirmFlg:1
	 */
	private static String hostA = "js.ac.10086.cn";
	private static String hostB = "service.js.10086.cn";
	// 登陆页面
	private static String loginPageURL = "https://js.ac.10086.cn/jsauth/dzqd/mh/index.html";
	// 获取关键参数的url（cookie中的参数可用作验证码获取地址的参数和登陆post的参数）
	private static String theKeyArgURL = "https://js.ac.10086.cn/jsauth/dzqd/addCookie";
	// 获取验证码url
	private static String captchaPicUrl = "https://js.ac.10086.cn/jsauth/dzqd/zcyzm?t=new&ik=l_image_code&l_key=";
	// 登陆post请求
	private static String loginPostURL = "https://js.ac.10086.cn/jsauth/dzqd/popDoorPopLogonServletNewNew";
	// 发送动态密码,30秒可以再次发送，5分钟内动态验证码是有效的
	private static String sendDynamicPWDURL = "http://service.js.10086.cn/sms.do";
	// 获取通话详单,需要动态密码
	private static String mobileDetailURL = "http://service.js.10086.cn/actionDispatcher.do";
	/**
	 * mobile:15851996344 password:199131 loginType:wsyyt icode:6guu
	 * fromFlag:doorPage l_key:GUV2ID4SVCB9NMAQE5TL772MMPO7NK5R activepass:
	 * wttype:2
	 */

	// 登录跳转页面
	private static String redirectLoginUrl = "https://bj.ac.10086.cn/ac/cmsso/redirect.jsp";

	public static void main(String[] args) {
		try {
			DefaultHttpClient httpClient = CUtil.init(); 
			jiangSuSpider(httpClient);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载验证码图片到本地
	 * 
	 * @param captchaPicUrl
	 * @param argsSourceUrl
	 * @return 获取豆瓣验证码时发送请求，需要传的参数id
	 * @throws ParseException
	 * @throws IOException
	 */
	private static String getCapthcaPic(String captchaPicUrl, String argsSourceUrl, HttpClient httpClient) throws ParseException, IOException {
		String destfilename = "D:\\bjydyz.png";
		String captchaId = "";

		HttpGet httpget = new HttpGet(captchaPicUrl);
		File file = new File(destfilename);
		if (file.exists()) {
			file.delete();
		}

		HttpResponse response = httpClient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		try {
			FileOutputStream fout = new FileOutputStream(file);
			int len = -1;
			byte[] tmp = new byte[1024];
			while ((len = in.read(tmp)) != -1) {
				fout.write(tmp, 0, len);
			}
			fout.close();
		} finally {
			in.close();
			httpget.releaseConnection();
		}
		return captchaId;
	}

	/**
	 * 执行get请求
	 * 
	 * @param url
	 * @param referer
	 * @param XRequestedWith
	 * @return
	 */
	private static String executeGet( DefaultHttpClient httpClient,String url, String referer, String XRequestedWith, String host) {
		HttpGet httpGet = new HttpGet(url);
		String location;
		String responseBody = null;
		if (XRequestedWith != null && "".equals(XRequestedWith)) {
			httpGet.setHeader("X-Requested-With", XRequestedWith);
		}
		if (referer != null && "".equals(referer)) {
			httpGet.setHeader("Referer", referer);
		}
		if (host != null && "".equals(host)) {
			httpGet.setHeader("Host", "js.ac.10086.cn");
		}
		// httpGet.setHeader("User-Agent",
		// "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");
		try {
			HttpResponse  httpResponse = httpClient.execute(httpGet);
			responseBody = EntityUtils.toString(httpResponse.getEntity(), "GBK");
			System.out.println(responseBody);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		return responseBody;
	}

	/**
	 * 
	 * @param url
	 * @param params
	 * @param referer
	 * @param contentType
	 * @param acceptEncoding
	 * @param xRequestedWith
	 * @param host
	 * @return
	 */
	private static String executePost( DefaultHttpClient httpClient,String url, List<NameValuePair> params, String referer, String contentType, String acceptEncoding, String xRequestedWith, String host) {
		HttpPost httpPost = new HttpPost(url);
		String responseBody = null;
		try {
			httpPost.setEntity(params == null ? null : new UrlEncodedFormEntity(params));
			// 当登陆后跳转，获取关键参数，后面获取话费等信息用到
			if (mobileDetailURL.equals(url)) {
				httpPost.addHeader("favTips", "1");
				httpPost.addHeader("KEY_IN_TIMES", "0");
				httpPost.addHeader("Origin", "http://service.js.10086.cn");
				httpPost.addHeader("city", "CZDQ");
				httpPost.addHeader("forwardPopSearch_" + mobile, String.valueOf(System.currentTimeMillis()));
				httpPost.addHeader("favTips", "1");
				List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					if (cookies.get(i).getName().equals("cmtokenid")) {
						httpPost.addHeader("jscmSSOCookie", cookies.get(i).getValue());
					}
				}
			}
			// 设置头部的参数
			// httpPost.addHeader("User-Agent",
			// "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
			httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");
			if (referer != null && !"".equals(referer)) {
				httpPost.addHeader("Referer", referer);
			}
			if (contentType != null && !"".equals(contentType)) {
				httpPost.addHeader("Content-Type", contentType);
			}
			if (acceptEncoding != null && !"".equals(acceptEncoding)) {
				httpPost.addHeader("Accept-Encoding", acceptEncoding);
			}
			// 设置这个表示是ajax，还是普通post提交
			if (xRequestedWith != null && !"".equals(xRequestedWith)) {
				httpPost.addHeader("X-Requested-With", xRequestedWith);
			}
			if (host != null && !"".equals(host)) {
				httpPost.addHeader("Host", host);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			// 当登陆后跳转，获取关键参数，后面获取话费等信息用到
			if (theKeyArgURL.equals(url)) {
				Header[] headers = httpResponse.getHeaders("Set-Cookie");
				for (int i = 0; i < headers.length; i++) {
					HeaderElement[] elements = headers[i].getElements();
					if (elements[0].getName().equals("wt_dl123")) {
						wt_dl123 = elements[0].getValue();
						l_key = wt_dl123;
					}
				}
			}

			responseBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			System.out.println(responseBody);
			//printCookies();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.abort();
		}
		return responseBody;
	}




	private static void jiangSuSpider( DefaultHttpClient httpClient) throws ClientProtocolException, IOException {

		// 登陆
		Boolean flag = loginJiangSuYD( httpClient);
		// 获取个人信息
		//String userInfo=executeGet("http://service.js.10086.cn/pages/GRZLGL_GRZL.jsp", "http://service.js.10086.cn/index.jsp", null, "service.js.10086.cn");
		// TODO 获取六个月话费记录
		// 获取当月通话详单
		if(flag){
			sendDynamicPwd(  httpClient);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in,
					"utf-8"));
			System.out.println("请输入手机收到的动态短信验证码...");
			String smsNum = br.readLine();
			
			
			DateUtils dateUtils = new DateUtils();
			List<String> ms =  dateUtils.getMonthForm(6,"yyyyMM");

			for(int i=0;i<ms.size();i++){
				getCurrentMonth(  httpClient,ms.get(i),smsNum);
			}
			
		}
		

	}
	/*
	 * 获取每个月的话费消费金额
	 * */
	public static Map<String,String> everyMonthPay(String content){
		Map<String,String> map = new HashMap<String,String>();
		String totalFee="0";
		JSONObject json;
		try {
			json = new JSONObject(content);
			String resultObj = json.get("resultObj").toString();
			JSONObject resultjson = new JSONObject(resultObj);
			
			String feeTime = resultjson.getString("feeTime");
			String billBal = resultjson.get("billRet").toString();
			JSONObject billBaljson = new JSONObject(billBal);
			totalFee = billBaljson.get("totalFee").toString();
			
			System.out.println(feeTime+":"+totalFee);
			map.put("feeTime", feeTime);
			map.put("ducFee", totalFee);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	
	}

	public static String yue(DefaultHttpClient httpclient){
		String url = "http://service.js.10086.cn/pages/ZHYEJYXQ.jsp?r="+Math.random();
		CHeader h = new CHeader(CHeaderUtil.Accept_,"http://service.js.10086.cn/",CHeaderUtil.Content_Type__urlencoded,
				"service.js.10086.cn",true);

		String content = "";
		HttpGet get = CHeaderUtil.getHttpGet(url, h);
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(get);
			content = new ParseResponse().parse(response,"utf-8");
			RegexPaserUtil rp = new RegexPaserUtil("\"balance\":\"","元\"",content,RegexPaserUtil.TEXTEGEXANDNRT);
			content = rp.getText().trim();
		}catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	/*
	 * 获取当前月话费信息 和 余额 以及用户基本信息
	 * */

	public static String getCHuafei(DefaultHttpClient httpclient ) {
		String tongHuaJson = "";
		String url = "http://service.js.10086.cn/actionDispatcher.do";
		DateUtils dateUtils = new DateUtils();
		Date date = new Date();
		String currentMonth = dateUtils.formatDate(date,"yyyyMM");	
		CHeader h = new CHeader(CHeaderUtil.Accept_other,"http://service.js.10086.cn/#ZDCX",CHeaderUtil.Content_Type__urlencoded,
				"service.js.10086.cn",true);
		Map<String,String> param = new HashMap<String,String>();
		param.put("reqUrl", "ZDCX");
		param.put("busiNum", "ZDCX");
		param.put("methodName", "getMobileRealTimeBill");
		param.put("isFamily", "0");
		param.put("userMobile", "");
		param.put("userName", "");
		param.put("beginDate", currentMonth);
		HttpPost post = CHeaderUtil.getPost(url, h,param);
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(post);
			tongHuaJson = new ParseResponse().parse(response,"utf-8");
			
		}catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
		return tongHuaJson;
	}
	
	public static String getHistoryHuafei(DefaultHttpClient httpclient,String currentMonth ) {
		String tongHuaJson = "";
		String url = "http://service.js.10086.cn/actionDispatcher.do";
		CHeader h = new CHeader(CHeaderUtil.Accept_other,"http://service.js.10086.cn/#ZDCX",CHeaderUtil.Content_Type__urlencoded,
				"service.js.10086.cn",true);
		Map<String,String> param = new HashMap<String,String>();
		param.put("reqUrl", "ZDCX");
		param.put("busiNum", "ZDCX");
		param.put("methodName", "getMobileRealTimeBill");
		param.put("isFamily", "0");
		param.put("userMobile", "");
		param.put("userName", "");
		param.put("beginDate", currentMonth);
		
		
		HttpPost post = CHeaderUtil.getPost(url, h,param);
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(post);
			tongHuaJson = new ParseResponse().parse(response,"utf-8");
			
		}catch (Exception  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
		return tongHuaJson;
	}
	
			
	
	public static void saveUserInfo(String content){
		JSONObject json;
		try {
			json = new JSONObject(content);
			String resultObj = json.get("resultObj").toString();
			JSONObject resultjson = new JSONObject(resultObj);
			
			String userInfoBean = resultjson.get("userInfoBean").toString();
			JSONObject userjson = new JSONObject(userInfoBean);
			String userName = userjson.get("userName").toString();
			String userApplyDate = userjson.get("userApplyDate").toString();
			String email = userjson.get("email").toString();
			String balance = userjson.get("balance").toString();
			
		
			String billRet = resultjson.get("billRet").toString();
			JSONObject billRetjson = new JSONObject(billRet);
			String totalFee = billRetjson.get("totalFee").toString();
			
			String mainprodName = billRetjson.get("mainprodName").toString();
			System.out.println(userName+balance+mainprodName+totalFee+userApplyDate+email);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private static void getCurrentMonth( DefaultHttpClient httpClient,String month,String smsNum) throws IOException, UnsupportedEncodingException {
		/**
		 * 获取通话详单
		 */
		
				
				HttpPost httppost = new HttpPost("http://service.js.10086.cn/actionDispatcher.do");
				List<NameValuePair> params;
				params = new ArrayList<NameValuePair>();
				// 准备post请求的参数,发送登陆请求
				params.add(new BasicNameValuePair("confirmFlg", "1"));
				params.add(new BasicNameValuePair("operType", "3"));
				params.add(new BasicNameValuePair("queryItem", "1"));
				params.add(new BasicNameValuePair("queryMonth", month));
				params.add(new BasicNameValuePair("reqUrl", "queryBillDetail"));
				params.add(new BasicNameValuePair("smsNum", smsNum));
				// 需要设置cookie 值
				httppost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
				httppost.addHeader("X-Requested-With", "XMLHttpRequest");
				httppost.setHeader("User-Agent",
						"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
				httppost.addHeader("Host", "service.js.10086.cn");
				httppost.addHeader("Referer",
						"http://service.js.10086.cn/index.jsp#QDCX");
			
				HttpResponse httpResponse = httpClient.execute(httppost);
				HttpEntity entity = httpResponse.getEntity();
				String tongHuaJson = EntityUtils.toString(entity, "UTF-8");
				System.out.println(tongHuaJson);
				httppost.releaseConnection();
	}

	


	/**
	 * 发送动态验证码
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static  void sendDynamicPwd(DefaultHttpClient httpClient){
		/**
		 * 发送动态验证码 busiNum:QDCX mobile:undefined
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("busiNum", "QDCX"));
		params.add(new BasicNameValuePair("mobile", mobile));
		executePost(  httpClient,sendDynamicPWDURL, params, "http://service.js.10086.cn/index.jsp", "application/x-www-form-urlencoded", null, "XMLHttpRequest", hostB);

	}

	/**
	 * 登陆方法
	 * 
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private static boolean loginJiangSuYD( DefaultHttpClient httpClient) throws IOException, UnsupportedEncodingException {
		boolean isLoginFlag = false;
		// 判断是否需要获取验证码，需要则获取验证码,手动输入
		executePost(  httpClient,theKeyArgURL, null, loginPageURL, "application/x-www-form-urlencoded;charset=UTF-8", null, "XMLHttpRequest", hostA);
		// 1,获取验证码
		getCapthcaPic(captchaPicUrl + l_key, loginPageURL, httpClient);

		// 需要手工输入下载验证码中显示的字母、数字
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
		System.out.println("请输入下载下来的验证码中...");
		icode = br.readLine();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		/**
		 * 准备参数
		 */
		/**
		 * mobile:15851996344 password:199131 loginType:wsyyt icode:6guu
		 * fromFlag:doorPage l_key:GUV2ID4SVCB9NMAQE5TL772MMPO7NK5R activepass:
		 * wttype:2
		 */
		params.add(new BasicNameValuePair("activepass", activepass));
		params.add(new BasicNameValuePair("fromFlag", fromFlag));
		params.add(new BasicNameValuePair("icode", icode));
		params.add(new BasicNameValuePair("l_key", l_key));
		params.add(new BasicNameValuePair("loginType", loginType));
		params.add(new BasicNameValuePair("mobile", mobile));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("wttype", wttype));

		// 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断登录是否成功
		String loginPostJson = executePost(  httpClient,loginPostURL, params, loginPageURL, "application/x-www-form-urlencoded;charset=UTF-8", null, "XMLHttpRequest", hostA);
		isLoginFlag = isLogin(loginPostJson);
		return isLoginFlag;
	}

	/**
	 * 判断是否登录成功
	 * 
	 * @param loginPostJson
	 *            登陆post请求返回的json数据
	 * @return
	 */
	private static boolean isLogin(String loginPostJson) {
		boolean isLoginFlag = false;
		/**
		 * 返回-6表示验证码为空，-8表示验证码错误或者post参数不正确(参数为空也会),0000表示正常登陆
		 */
		// 验证是否登录成功的方法
		// {rcode:"0000"}
		if (loginPostJson != null && loginPostJson.endsWith("")) {
			if (loginPostJson.split(":")[1].split("}")[0].endsWith("\"0000\"")) {
				System.out.println("登陆成功");
				isLoginFlag = true;
			} else {
				// TODO 返回错误代码
				System.out.println("登陆 失败");
			}
		}
		return isLoginFlag;
	}

	/**
	 * get请求的参数,特殊字符
	 * 
	 * @param urlString
	 * @return
	 */
	private static URI urlStringToURI(String urlString) {
		URL url = null;
		URI uri = null;
		try {
			url = new URL(urlString);
			uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return uri;
	}

}
