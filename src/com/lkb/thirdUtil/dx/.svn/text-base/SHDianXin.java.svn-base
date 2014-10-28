package com.lkb.thirdUtil.dx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URI;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.PhoneNum;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.constant.DxConstant;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IPhoneNumService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.util.DateUtils;
import com.lkb.util.IPUtil;
import com.lkb.util.InfoUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;

/*
 * 上海电信
 * */
public class SHDianXin {
	private static Logger logger = Logger.getLogger(SHDianXin.class);
	private static String loginUrl2 = "https://uam.ct10000.com/ct10000uam/login?service=http://www.189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=";	                                   		  
	//验证码图片路径
	public static String catImgUrl = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?";
	//验证码图片路径
	private static String catImgUrl2 = "http://sh.189.cn/service/RandomNum_new2.jsp?";
	
	private static String getLocation = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
	
	private static String a = "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10003&toStUrl=http://sh.189.cn/service/account_manage_query.do?functionName=queryPayDetail";
	/*
	 * 需要注意：电信对返回的IP做了限制，所以到linux服务器上面时，需要测试，并重新修改
	 * 
	 * */
	public static void main(String[] args) throws Exception{
		
		// System.setProperty("javax.net.ssl.keyStore", "um-15918"); 
		 
		 /*KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
		 String dxs = InfoUtil.getInstance().getInfo("road",
					"dx");
		 String dxpassword = InfoUtil.getInstance().getInfo("road",
					"dxpassword");*/
		 
         //加载证书文件
         /*FileInputStream instream = new FileInputStream(new File(dxs));
         try {
             trustStore.load(instream, dxpassword.toCharArray());
         } finally {
             instream.close();
         }*/
         
         CookieStore cookieStore = new BasicCookieStore();
  		 HttpClientContext localContext = HttpClientContext.create();
         localContext.setCookieStore(cookieStore);

         
         /*SSLContext sslcontext = SSLContexts.custom()
                 .loadTrustMaterial(trustStore)
                 .build();*/
 
         /*SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                 SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);*/
         LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy(){
        	 
         };
         X509TrustManager xtm = new X509TrustManager(){   //创建TrustManager 
     		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
     		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
     		public X509Certificate[] getAcceptedIssuers() { return null; }
 			public void checkClientTrusted(X509Certificate[] arg0, String arg1, Socket arg2) throws CertificateException {}
 			public void checkClientTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2) throws CertificateException {}
 			public void checkServerTrusted(X509Certificate[] arg0, String arg1, Socket arg2) throws CertificateException {}
 			public void checkServerTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2) throws CertificateException {} 
     	}; 
     	//TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext 
		SSLContext ctx = SSLContext.getInstance("TLS"); 

		//使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用 
		ctx.init(null, new TrustManager[]{xtm}, new SecureRandom()); 
		SSLContext.setDefault(ctx);
		//创建SSLSocketFactory 
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);         
		
         CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).setRedirectStrategy(redirectStrategy)
                 .setSSLSocketFactory(sslSocketFactory)
                 .build();
       
		SHDianXin dx = new SHDianXin();
		
		//String userIp = "101.40.145.124";
		IPUtil iputil = new IPUtil();
		String userIp = iputil.getIP();
		Map<String,String> dxMap = new HashMap<String,String>();
		//查找数据
		dx.putDate( httpclient,dxMap, userIp );
		
		//获取验证码
		try {
			dx.downloadFile(httpclient,catImgUrl+Math.random(), "2014-08-20", "22.png", null, null);
		} catch (Exception e) {

			e.printStackTrace();
		}
		String phone = "18016252553";
		String password="026315";
		//根据手机号判断地区,并把数据写入map
		dx.putDate2( httpclient ,phone,dxMap);
		
		Scanner in = new Scanner(System.in);
		System.out.print("首页验证码为：");
	    String authcode  = in.nextLine();
		String type="3";
		String open_no = "c2000004";
		dxMap.put("username", phone);
		dxMap.put("password", password);
		dxMap.put("randomId", authcode);
		dxMap.put("customFileld01", type);
		dxMap.put("open_no", open_no);

		String returnUrl = dx.login( httpclient , loginUrl2+userIp, dxMap).replace("<script type='text/javascript'>location.replace('", "").replace("');</script>", "");
		
		String syr = dx.getText(httpclient,returnUrl);
		

		
		
		String syr5= dx.getText1( httpclient,a);
		syr5 = syr5.replace("<script type='text/javascript'>location.replace('", "").replace("');</script>", "");
	
		HttpClient httpclient1 = httpclient; //new DefaultHttpClient();  
		String syr6= dx.getText3( httpclient1,syr5);

		syr6 = syr6.replace("<script type='text/javascript'>location.replace('", "").replace("');</script>", "");
	
		String syr7= dx.getText3( httpclient1,syr6);

		String syr8= dx.getText3( httpclient1,"http://sh.189.cn/service/uiss_mobileLogin.do?method=login");

		String syr9= dx.getText3( httpclient1,"http://sh.189.cn/service/redirect.do?service=detailQuery&tmp=");
		System.out.println("iframe 页面==================="+syr9);	
		
			
		//下载图片验证码，并且输入，判断正确与否
			try {
				dx.downloadFile(httpclient1,catImgUrl2+Math.random(),  "2014-08-20", "2332.png", null, null);
			} catch (Exception e) {
	
				e.printStackTrace();
			}
			Map<String,String> dxMap1 = new LinkedMap();
			Scanner in1 = new Scanner(System.in);
			System.out.print("算数验证码为：");
		    String imgnum  = in1.nextLine();
		   
		    dxMap1.put("dynnum","");
		    dxMap1.put("imgnum",imgnum);
			dxMap1.put("requesttype","send");
			dxMap1.put("oper","cnetQueryCondition.do?actionCode=init");
			String ll = dx.login1(httpclient1,"http://sh.189.cn/service/dynamicValidate.do", dxMap1);
			if(ll.contains("图片验证码输入错误")){
				System.out.println("图片验证码输入错误,请重新输入！");
				
			}
			//发送手机验证码
			Map<String,String> dxMap2 = new LinkedMap();
			Scanner in2 = new Scanner(System.in);
			System.out.print("手机验证码为：");
		    String dynnum  = in2.nextLine();
		    dxMap2.put("dynnum",dynnum);
		    dxMap2.put("imgnum",imgnum);
		    dxMap2.put("requesttype","end");
		    dxMap2.put("oper","cnetQueryCondition.do?actionCode=init");
			String ll2 = dx.login1(httpclient1,"http://sh.189.cn/service/dynamicValidate.do", dxMap2);
			System.out.println(ll2);
			while (true) {
				Scanner in3 = new Scanner(System.in);
				System.out.print("输入要查询的年月(格式为 2014/06)：");
				String  dateInfo  = dateToString(in3.nextLine());
				if(dateInfo==null){
					System.out.print("时间输入错误.");
					continue;
				}
				String syr23 = dx.getText(httpclient1,"http://sh.189.cn/service/cnetQueryCondition.do?actionCode=query&currentPage=1&menuid=4&flag=历史&devNo="+phone+"&subNo=null&deviceNo="+phone+"&selectType=历史&detailType=SCP&queryDate="+dateInfo);
				System.out.println("语音详单查询 ==================="+syr23);	
			}
			
		
	}
	


	
	public String history(CloseableHttpClient httpclient, String url,Map map) {

		HttpPost httpost = new HttpPost(url);
		
		String returnString = "";
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		if(map!=null){
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = map.get(key).toString();
				nvps.add(new BasicNameValuePair(key, value));
			}
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(
					(List<? extends org.apache.http.NameValuePair>) nvps,
					HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpost);
			System.out.println(response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
				String result = EntityUtils.toString(response.getEntity());
				// 得到返回的字符串
				returnString = result;
				// 打印输出
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			httpost.abort();
		}
		return returnString;
	}
	//报告获取联系人信息
	public List<Map> getLTContacts(String phone,IDianXinDetailService dianxinDetailService){
		Map<String,String> map=new HashMap();
		map.put("phone", phone);
		List<Map> list1=new ArrayList<Map>();
		List list=dianxinDetailService.getDianXinDetailForReport(map);
		for(int i=0;i<list.size();i++){
			Map maptemp=new HashMap();
			Map<Object,Object> map1=(Map<Object,Object>)list.get(i);
			String phone1=map1.get("recevierPhone").toString();
			if(i+1<list.size()){
				Map<Object,Object> map2=(Map<Object,Object>)list.get(i+1);
				String phone2=map2.get("recevierPhone").toString();
				if(phone1.equals(phone2)){
					maptemp.put("phone", phone1);
					String tradeAddr = "无";
					if(map1.get("tradeAddr")!=null){
						tradeAddr = map1.get("tradeAddr").toString();
					}
					maptemp.put("place", tradeAddr);
					String type1=map1.get("callWay").toString();
					String type2=map2.get("callWay").toString();
					String zhujiao="";
					String beijiao="";
					if("主叫".equals(type1)){
						zhujiao=map1.get("num").toString();
						beijiao=map2.get("num").toString();
					}
					else{
						zhujiao=map2.get("num").toString();
						beijiao=map1.get("num").toString();
					}
					maptemp.put("zhujiao", zhujiao);
					maptemp.put("beijiao", beijiao);
					Integer totalint=Integer.parseInt(zhujiao)+Integer.parseInt(beijiao);
					maptemp.put("total",totalint.toString());
					String Times1=map1.get("tradetimes").toString();
					String Times2=map2.get("tradetimes").toString();
					if(Times1.contains(".")){
						Times1=Times1.replace(".0", "");
					}
					if(Times2.contains(".")){
						Times2=Times1.replace(".0", "");
					}
					Integer totalTimesint=Integer.parseInt(Times1)+Integer.parseInt(Times2);
					maptemp.put("totaltimes",totalTimesint.toString());
					list1.add(maptemp);
					i++;
				}
				else{
					maptemp.put("phone", phone1);
					String tradeAddr = "无";
					if(map1.get("tradeAddr")!=null){
						tradeAddr = map1.get("tradeAddr").toString();
					}
					 maptemp.put("place", tradeAddr);
					 String callWay = "主叫";
					 if(map1.get("callWay")!=null){
						 callWay = map1.get("callWay").toString();
					 }
					String type1= callWay;
					
					String zhujiao="";
					String beijiao="";
					if("主叫".equals(type1)){
						
						zhujiao=map1.get("num").toString();
						beijiao="0";
					
					}
					else{
						
						beijiao=map1.get("num").toString();
						zhujiao="0";
					}
					maptemp.put("zhujiao", zhujiao);
					maptemp.put("beijiao", beijiao);
					maptemp.put("total",map1.get("num").toString());
				
					maptemp.put("totaltimes",map1.get("tradetimes").toString());
					list1.add(maptemp);
				}
			}
			else{
				String tradeAddr="";
				if(map1.get("tradeAddr")!=null){
					tradeAddr=map1.get("tradeAddr").toString();
				}
				maptemp.put("phone", phone1);
				maptemp.put("place", tradeAddr);
				String type1 = "主叫";
				if(map1.get("callWay") != null){
					 type1=map1.get("callWay").toString();
				}
				
				String zhujiao="";
				String beijiao="";
				if("主叫".equals(type1)){
					
					zhujiao=map1.get("num").toString();
					beijiao="0";
				
				}
				else{
					
					beijiao=map1.get("num").toString();
					zhujiao="0";
				}
				maptemp.put("zhujiao", zhujiao);
				maptemp.put("beijiao", beijiao);
				maptemp.put("total",map1.get("num").toString());
				maptemp.put("totaltimes",map1.get("tradetimes").toString());
				list1.add(maptemp);
			}
	
		}
		return list1;
	}
	public List<Map> getDXBill(String phone,
			IDianXinTelService dianxinTelService,
			IPhoneNumService phoneNumService,
			IDianXinDetailService dianxinDetailService,IUserService userService) {
		Map map = new HashMap();
		map.put("teleno", phone);
		List<Map> list = dianxinTelService.getDianXinTelForReport1(map);
		if (list.get(0) != null) {
			String avg = list.get(0).get("avg").toString();
			avg = avg.substring(0, avg.indexOf("."));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String earlest = list.get(0).get("earlest").toString()
					.substring(0, 10);
			Date date = null;
			try {
				date = df.parse(earlest);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DateUtils du = new DateUtils();
			int days = 0;
			try {
				days = du.dayDist(earlest);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Map userMap = new HashMap();
			userMap.put("loginName", phone);
			userMap.put("usersource", Constant.DIANXIN);
			List<User> users = userService.getUserByUserNamesource(userMap);
			BigDecimal cAllBalance = new BigDecimal("0");
			if(users!=null && users.size()>0){
				User user = users.get(0);
				cAllBalance = user.getPhoneRemain();
			}
			
			Map map1 = new HashMap();
			map1.put("avg", avg);
			map1.put("days", days);
			map1.put("teleno", phone);
			map1.put("cAllBalance", cAllBalance);
			String phoneId = phone.substring(0, 7);
			PhoneNum phoneNum = phoneNumService.findById(phoneId);
			map1.put("local", phoneNum.getProvince());
			List<Map> list1 =dianxinDetailService.getDianXinDetailForReport2(map1);
			if(list1!=null && list1.size()>0&&list1.get(0)!= null){
				String lateststr = list1.get(0).get("latest").toString();
				lateststr = lateststr.replaceAll("-", ".").substring(0, 10);
				map1.put("latest", lateststr);
			}else{
				map1.put("latest", "--");
			}
			
			list.clear();
			list.add(map1);
		} else {
			list.clear();
		}
		return list;
	}
	public String login(CloseableHttpClient httpclient,String url,Map dxMap){
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		httpost.setHeader("Connection","keep-alive");
		httpost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		String content ="";
		if(dxMap!=null && dxMap.size()>0){
			Iterator it = dxMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = dxMap.get(key).toString();
				nvps.add(new BasicNameValuePair(key, value));
			}
		}
	
		try {
			httpost.setEntity(new UrlEncodedFormEntity(
					(List<? extends org.apache.http.NameValuePair>) nvps,
					HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpost);
			//System.out.println(response.getStatusLine().getStatusCode());
			content = EntityUtils.toString(response.getEntity());
			//System.out.println("---------------");
			//System.out.println(content);
		} catch (Exception e) {
	
			e.printStackTrace();
			
		} finally {
			httpost.abort();
		}
		return content;
	}
	
	
	public String login1(HttpClient httpclient,String url,Map dxMap){
		HttpPost httpost = new HttpPost(url);
		httpost.setHeader("Accept","ext/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpost.setHeader("Accept-Encoding","gzip, deflate");
		httpost.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		
		httpost.setHeader("Cache-Control","no-cache");
		httpost.setHeader("Connection","keep-alive");
		httpost.setHeader("Content-Type","text/plain; charset=UTF-8");
		httpost.setHeader("Host","sh.189.cn");
		httpost.setHeader("Pragma","no-cache");
		httpost.setHeader("Referer","http://sh.189.cn/service/redirect.do?service=detailQuery&tmp=");
		httpost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String content ="";
		if(dxMap!=null && dxMap.size()>0){
			Iterator it = dxMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = dxMap.get(key).toString();
				nvps.add(new BasicNameValuePair(key, value));
				
			}
		}
	
		try {
			httpost.setEntity(new UrlEncodedFormEntity(
					(List<? extends org.apache.http.NameValuePair>) nvps,
					HTTP.UTF_8));
			
			
			HttpResponse response = httpclient.execute(httpost);
			System.out.println(response.getStatusLine().getStatusCode());

			 content = EntityUtils.toString(response.getEntity());
			System.out.println("---------------");
			System.out.println(content);
		} catch (Exception e) {
	
			e.printStackTrace();
			
		} finally {
			httpost.abort();
		}
		return content;
	}
	
	/*
	 * 解析首页，获得拼装的参数
	 * */
	public  void putDate2(CloseableHttpClient httpclient ,String phone,Map<String,String> dxMap){
		//02|上海
		String content = getText( httpclient,getLocation+"?username="+phone);
		String[] contents = content.split("\\|");
		if(contents.length>1){
			String customFileld02 = contents[0];
			String areaname = contents[1];
			dxMap.put("customFileld02", customFileld02);
			dxMap.put("areaname", areaname);	
		}
	}
	
	
	/*
	 * 解析首页，获得拼装的参数
	 * */
	public  void putDate(CloseableHttpClient httpclient,Map<String,String> dxMap,String userIp ){

		String content = getText( httpclient,loginUrl2+userIp);
		Document doc = Jsoup.parse(content);
		Element element = doc.getElementById("c2000004");
		Elements eles =  element.getElementsByTag("input");
		for(int i=0;i<eles.size();i++){
			String name = eles.get(i).attr("name");		
			if(name.equals("forbidpass")||name.equals("forbidaccounts")||name.equals("authtype")||name.equals("lt")||name.equals("_eventId")){
				String value = eles.get(i).attr("value");
				dxMap.put(name, value);
			}
		}
		
		
		
	}
	
	
	/*
	 * 得到网页的内容
	 * */
	private String getText2(CloseableHttpClient httpclient,String redirectLocation) {
		//HttpGet httpget = new HttpGet(redirectLocation);
		HttpPost httpost = new HttpPost(redirectLocation);
		httpost.setHeader("Referer", "http://www.189.cn/sh/");    
	    
		List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		String returnUrl = "";
		try {
			//responseBody = httpclient.execute(httpost, responseHandler);
			httpost.setEntity(new UrlEncodedFormEntity(
					(List<? extends org.apache.http.NameValuePair>) nvps,
					HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpost);
			System.out.println(response.getStatusLine().getStatusCode());
			Header hader = response.getFirstHeader("Location");
			if(hader!=null){
				returnUrl = hader.getValue();
			}
			String content = EntityUtils.toString(response.getEntity());
			System.out.println("---------------");
			System.out.println(content);
			
		} catch (Exception e) {
		
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpost.abort();
		}
		return responseBody;
	}
	
	
	public String getLocation(CloseableHttpClient httpclient,String url,int times){
		HttpGet httpget=new HttpGet(url);
		if(times==1){
			httpget.setHeader("Referer","http://www.189.cn/sh/");
		}else if(times ==2){
			httpget.setHeader("Host","uam.ct10000.com");
			httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
			httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpget.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			httpget.setHeader("Accept-Encoding","gzip, deflate");
			httpget.setHeader("Connection","keep-alive");

		}	
		 HttpParams params = new BasicHttpParams();
		 params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
		 httpget.setParams(params);
		 String location="";
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			int statuscode = response.getStatusLine().getStatusCode();			
	       if(statuscode==302){
	          Header locationHeader = response.getFirstHeader("Location");
	          
	         if (locationHeader != null) {
	            location = locationHeader.getValue();
	            //System.out.println(location);
	          }
	       }else{
	    	   location =  EntityUtils.toString(response.getEntity());
	       }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
		
	}
	
	
	
	/*
	 * 得到网页的内容
	 * */
	public static String getText(HttpClient httpclient,String redirectLocation) {
		HttpGet httpget = new HttpGet(redirectLocation);
		HttpClientContext context = HttpClientContext.create();
		 
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";

		try {
			responseBody = httpclient.execute(httpget, responseHandler);	
			HttpHost target = context.getTargetHost();
	        List<URI> redirectLocations = context.getRedirectLocations();
	        URI location = URIUtils.resolve(httpget.getURI(), target, redirectLocations);
	        //System.out.println("Final HTTP location: " + location.toASCIIString());

		} catch (Exception e) {
		
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}
	
	/*
	 * 得到网页的内容
	 * */
	public String getText1(CloseableHttpClient httpclient,String redirectLocation) {
		HttpGet httpget = new HttpGet(redirectLocation);
		
		httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		httpget.setHeader("Host","www.189.cn");
		httpget.setHeader("Referer","www.189.cn");
		httpget.setHeader("Connection","Keep-Alive");
		httpget.setHeader("Accept-Language","zh-CN");
		httpget.setHeader("Accept-Encoding","gzip, deflate");
		httpget.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		httpget.setHeader("X-Requested-With","XMLHttpRequest");
		httpget.setHeader("Accept","*/*");
		httpget.setHeader("DNT","1");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = httpclient. execute(httpget, responseHandler);
		} catch (Exception e) {
			
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}
	
	/*
	 * 得到网页的内容
	 * */
	public String getText3(HttpClient httpclient,String redirectLocation) {
		HttpGet httpget = new HttpGet(redirectLocation);
		
		httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.setHeader("Accept-Encoding","gzip, deflate");
		httpget.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		httpget.setHeader("Connection","keep-alive");
		httpget.setHeader("Host","sh.189.cn");
		httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			//CloseableHttpClient httpclient1 = HttpClients.createDefault();
			responseBody = httpclient. execute(httpget, responseHandler);
		} catch (Exception e) {
			
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}
	
	
	    // 获取验证码
		public String getAuthcode(CloseableHttpClient httpclient,String authUrl,String currentUser,String picNameAfter) {

			int random = (int)(Math.random()*1000);
			String picName = currentUser + random+picNameAfter;
			java.text.DateFormat format2 = new java.text.SimpleDateFormat(
					"yyyyMMdd");
			String dataPath = format2.format(new Date());
			// 获取验证码
			try {
				downloadFile(httpclient, catImgUrl + Math.random(),dataPath,picName,"","");
			} catch (Exception e) {
	
				e.printStackTrace();
			}
			
			
			String fileName = dataPath+"/"+picName+"?random="+Math.random();
			return  fileName;
		}
		
	
	
	/*
	 * 下载验证码
	 * */
		public String downloadFile(HttpClient httpclient, String url,String dataPath, String picName,String hostUrl,String refererUrl)
			throws Exception {

		String authcodePath = InfoUtil.getInstance().getInfo("road",
				"authcodePath");
		String path1 = authcodePath+ "/"+dataPath;

		File file2 = new File(path1);
		if (!file2.exists() && !file2.isDirectory()) {
			file2.mkdir();
		}
		
		String destfilename = path1 + "/"+ picName;
		HttpGet httpget = new HttpGet(url);
	
		
		httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		httpget.setHeader("Host","sh.189.cn");
		httpget.setHeader("Referer","http://sh.189.cn/service/redirect.do?service=detailQuery&tmp=");
		httpget.setHeader("Connection","Keep-Alive");
		httpget.setHeader("Accept-Language","zh-CN");
		httpget.setHeader("Accept-Encoding","gzip, deflate");
		httpget.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		httpget.setHeader("X-Requested-With","XMLHttpRequest");
		
		httpget.setHeader("Accept","*/*");
		httpget.setHeader("DNT","1");
		

		File file = new File(destfilename);
		if (file.exists()) {
			file.delete();
		}

		HttpResponse response = httpclient.execute(httpget);
		
		if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){  
            //请求成功  
            //取得请求内容  
            HttpEntity entity = response.getEntity();  
            //显示内容  
            if (entity != null) {  
                //这里可以得到文件的类型 如image/jpg /zip /tiff 等等 但是发现并不是十分有效，有时明明后缀是.rar但是取到的是null，这点特别说明  
                //System.out.println(entity.getContentType());  
                //可以判断是否是文件数据流  
                //System.out.println(entity.isStreaming());  
                //设置本地保存的文件  
                FileOutputStream output = new FileOutputStream(destfilename);  
                //得到网络资源并写入文件  
                InputStream input = entity.getContent();  
                byte b[] = new byte[1024];  
                int j = 0;  
                while( (j = input.read(b))!=-1){  
                    output.write(b,0,j);  
                }  
                output.flush();  
                output.close();   
            }  
            if (entity != null) {  
                entity.consumeContent();  
            }  
        }  
		return destfilename;
	}
	
	private static  String dateToString(String date) {       
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM");     
		//String str = formatDate.format(date);       
		try {            
			Date time = formatDate.parse(date);  
			Calendar calendar = Calendar.getInstance();//日历对象  
			calendar.setTime(time);//设置当前日期
			calendar.add(Calendar.MONTH, +1);  
			String s = formatDate.format(calendar.getTime());//输出格式化的日期
			return s;  
		} catch (ParseException e) {         
			e.printStackTrace();       
		}       
		  return null;
	}
	public void saveUserInfoByHtmlparser(HttpClient httpclient,
			IUserService userService, String loginName, 
			String currentUser, IWarningService warningService) {
		String url="http://sh.189.cn/service/AccountManageAction.do?method=init#";
		String content = getText(httpclient,url);
		System.out.println(content);
		
	
		User user=new User();
		
		user.setPhone(loginName);
		user.setLoginName(loginName);
		user.setParentId(currentUser);
		user.setUsersource(Constant.DIANXIN);
		user.setUsersource2(Constant.DIANXIN);
		Map map=new HashMap();
		map.put("phone", loginName);
		List<User>list=userService.getUserByPhone(map);
		if(list!=null&&list.size()>0){
			userService.update(user);
		}
		else{
			UUID uuid = UUID.randomUUID();
			user.setId(uuid.toString());
			userService.saveUser(user);
		}
	}

	/*
	 * 解析页面电话详单并记录到数据库
	 * */
	public void parseList(DefaultHttpClient httpclient,String phone,String subNo,String currentUser,IUserService userService,IDianXinDetailService dianxinDetailService,IWarningService warningService){
		List<String> months = DateUtils.getMonth(6);
		saveUserInfoByHtmlparser(httpclient,userService,phone, currentUser, warningService);
		try{
			try{
				GetThread[] threads = new GetThread[months.size()];
				for (int i = 0; i < threads.length; i++) {
					threads[i] = new GetThread( httpclient,  dianxinDetailService
					, phone, subNo,months.get(i));
				}
				for (int j = 0; j < threads.length; j++) {
					threads[j].start();
				}
					// join the threads
				for (int j = 0; j < threads.length; j++) {
					threads[j].join();
				}
			}
			catch (InterruptedException e) {
				logger.info("第671行捕获异常：",e);		
				e.printStackTrace();
			} finally {
			
					httpclient.close();
					DxConstant.sh_dxcloseClientMap1.remove(currentUser);
					CloseableHttpClient dxhttp = DxConstant.sh_dxcloseClientMap.get(currentUser);
					dxhttp.close();
					DxConstant.sh_dxcloseClientMap.remove(currentUser);
				
			}
		}catch(Exception e){
		//报警
			logger.info("第546行捕获异常：",e);		
			String warnType = WaringConstaint.SHDX_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}
	
	static class GetThread extends Thread {

		private final DefaultHttpClient httpclient;
		private final IDianXinDetailService dianxinDetailService;
		private final String phone;
		private final String subNo;
		private final String month;

		public GetThread(DefaultHttpClient httpClient, IDianXinDetailService dianxinDetailService
				,String phone,String subNo,String month) {
			this.httpclient = httpClient;
			this.dianxinDetailService = dianxinDetailService;
			this.phone = phone;
			this.subNo = subNo;
			this.month=month;
		}

		@Override
		public void run() {
			get();
		}

		// 订单
		public void get() {
			java.text.DateFormat format3 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int page=1;page<30;page++){
				String url = "http://sh.189.cn/service/cnetQueryCondition.do?currentPage="+page+"&deviceNo="+phone+"&detailType=SCP&menuid=4&flag=历史&devNo="+phone+"&actionCode=query&selectType=历史详单" +
						"&queryDate="+month+"&subNo="+subNo;
				
		
				String content = getText(httpclient,url);
				Document doc = Jsoup.parse(content);
				String tableSort = InfoUtil.getInstance().getInfo("dx/sh",
						"tableSort");
				String tbody = InfoUtil.getInstance().getInfo("dx/sh",
						"tbody");
				String tr = InfoUtil.getInstance().getInfo("dx/sh",
						"tr");
				String td = InfoUtil.getInstance().getInfo("dx/sh",
						"td");
				Elements elements = doc.select(tableSort);
				if(elements!=null && elements.size()>0){
					Elements elements2 = elements.first().select(tbody).first().select(tr);
					for(int j=0;j<elements2.size();j++){
						Elements elements3 =elements2.get(j).select(td);
						
						 String tradeType= elements3.get(1).text(); //通话类型 	
						 String date =  elements3.get(2).text();
						 Date cTime = null;//通话开始时间
							try {
								cTime = format3.parse(date);
							} catch (ParseException e) {
						
								e.printStackTrace();
							}
					
						 String tradeTime = elements3.get(3).text(); //通信时长 
						 String callWay = elements3.get(4).text(); //呼叫类型
						 String recevierPhone =  elements3.get(5).text(); //对方号码 	
						 String tradeAddr =  elements3.get(6).text(); //通信地点 
						 BigDecimal basePay = new BigDecimal(elements3.get(7).text());//基本费用
						 BigDecimal longPay= new BigDecimal(elements3.get(8).text()); //长途费用
						 BigDecimal infoPay= new BigDecimal(elements3.get(9).text()); //信息费用
						 BigDecimal otherPay= new BigDecimal(elements3.get(10).text()); //其他费用
						 BigDecimal allPay= new BigDecimal(elements3.get(11).text()); //总费用
						 
						 Map map2 = new HashMap();
						 map2.put("phone", phone);
						 map2.put("cTime", cTime);
						 List list = dianxinDetailService.getDianXinDetailBypt(map2);
						 if(list!=null && list.size()>0){
							 
						 }else{
							int tradeTimeInt = Integer.parseInt(tradeTime);
							
							 
							 DianXinDetail dxDetail = new DianXinDetail();
							 UUID uuid = UUID.randomUUID();
							 dxDetail.setId(uuid.toString());
							 dxDetail.setcTime(cTime); 
							 dxDetail.setTradeTime(tradeTimeInt);
							 dxDetail.setTradeAddr(tradeAddr);
							 dxDetail.setTradeType(tradeType);
							 dxDetail.setCallWay(callWay);
							 dxDetail.setRecevierPhone(recevierPhone);
							 dxDetail.setBasePay(basePay);
							 dxDetail.setLongPay(longPay);
							 dxDetail.setInfoPay(infoPay);
							 dxDetail.setOtherPay(otherPay);
							 dxDetail.setAllPay(allPay);
							 dxDetail.setPhone(phone);
							 dianxinDetailService.saveDianXinDetail(dxDetail);		
							
						 } 
					}
					if(!content.contains("下一页")){
						break;
					}
			}else{
				break;
			}
			
					
			}

		}
	}
}
