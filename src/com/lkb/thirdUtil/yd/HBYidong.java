package com.lkb.thirdUtil.yd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.thirdUtil.PicUpload;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.entity.CHeader;

public class HBYidong {
	
	public static String firstUrl ="https://hb.ac.10086.cn/login";
	public static String indexUrl = "https://hb.ac.10086.cn/SSO/loginbox";
	public static String authUrl = "https://hb.ac.10086.cn/SSO/img?codeType=0&rand=";
	
	public static String loginUrl = "https://hb.ac.10086.cn/SSO/loginbox";
	public static String userinfoUrl = "http://www.hb.10086.cn/my/account/basicInfoAction.action";
	public static String payUrl1 = "http://www.hb.10086.cn/my/billdetails/queryInvoice.action";
	public static String payUrl = "http://www.hb.10086.cn/service/fee/queryNewInvoice!commitServiceNew.action?postion=outer";
	public static String login2 = "http://www.hb.10086.cn/service/shoppingCart!cartNum.action?source=my";
	
	public static void main(String[] args){
		DefaultHttpClient httpclient = CUtil.init();
		HBYidong hbYidong = new HBYidong();
		
		String text = hbYidong.getText(httpclient, firstUrl);
		text = hbYidong.getText(httpclient, indexUrl);
		String authUrls = authUrl + System.currentTimeMillis();
		String currentUser = "eewerew";
		try {
			String authImg = hbYidong.getAuthcode( httpclient, currentUser);
			System.out.println(authImg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Scanner in2 = new Scanner(System.in);
//		System.out.print("密码为：");
//		String password = in2.nextLine();
		
		Scanner in = new Scanner(System.in);
		System.out.print("验证码为：");
		String authcode = in.nextLine();
		String phone = "13971146032";
		String password = "418840";
		//检验密码是否符合规范
		Map map1 = hbYidong.checkPassword(password );
		Boolean flag1 = Boolean.valueOf(map1.get("flag").toString());
		if(flag1){
			//第一次登陆
			Map map = new HashMap();
			map.put("accountType", "0");
			map.put("username", phone);
			map.put("passwordType", "1");
			map.put("password", password);
			map.put("smsRandomCode", "");
			map.put("validateCode", authcode);
			map.put("action", "/SSO/loginbox");// 
			map.put("style", "mymobile");
			map.put("service", "my");
			map.put("continue", "");
			map.put("submitMode", "login");
			map.put("guestIP", "219.143.103.242");
			text = hbYidong.login( httpclient, loginUrl,  map);
			System.out.println(text);
			
			//
			if(text!=null&&text.contains("SAMLart")){//验证第一步登陆成功
				//跳转
				Document doc = Jsoup.parse(text);
				//RelayState=&SAMLart=a6d8028d8d0a4bc2a37f8998fbc3c441&PasswordType=1&errorMsg=
				String actionUrl = doc.select("form[id=sso]").attr("action");
				String elayState = doc.select("input[name=elayState]").attr("value");
				String SAMLart = doc.select("input[name=SAMLart]").attr("value");
				String PasswordType = doc.select("input[name=PasswordType]").attr("value");
				String errorMsg = doc.select("input[name=errorMsg]").attr("value");
				Map map2 = new HashMap();
				map2.put("elayState", elayState);
				map2.put("SAMLart", SAMLart);
				map2.put("PasswordType", PasswordType);
				map2.put("errorMsg", errorMsg);
				text =  hbYidong.login( httpclient,  actionUrl,  map2);
				System.out.println(text);
				
				Map mapnull = new HashMap();
				text = hbYidong.postText(httpclient, login2, mapnull);
				text =  hbYidong.getText(httpclient,payUrl1);
				
				//解析用户基本信息
				text =  hbYidong.getText(httpclient,userinfoUrl);
				System.out.println(text);
				Document userdoc = Jsoup.parse(text);
				Elements elements = userdoc.select("div[class=acc_chax]").first().select("table").first().select("tr");
				for(int i=0;i<elements.size();i++){
					Element element = elements.get(i);
					Elements elements2 = element.select("th");
					for(int j=0;j<elements2.size();j++){
						Element element2 = 	elements2.get(j);
						String value = element.select("td").get(j).text();
						String key = element2.text();
						if(key.contains("客户姓名")){
							System.out.println("客户姓名："+value);
						}else if(key.contains("用户性别")){
							System.out.println("用户性别："+value);
						}else if(key.contains("证件号码")){
							System.out.println("证件号码："+value);
						}else if(key.contains("入网时间")){
							System.out.println("入网时间："+value);
						}
					}
				}
				
				
				//开始解析当月账单:加上当前总共6个月
//				http://www.hb.10086.cn/service/fee/queryNewInvoice!commitServiceNew.action?postion=outer
//				当月qryMonthType=current&theMonth=201407&menuid=myBill&groupId=tabs3
//				qryMonthType=history&theMonth=201402&menuid=myBill&groupId=tabs3
//				qryMonthType=history&theMonth=201406&menuid=myBill&groupId=tabs3
				List<String> months = DateUtils.getMonths(6,"yyyyMM");
				for(int i=0;i<months.size();i++){
					Map cmap = new LinkedHashMap();
					String theMonth = months.get(i);
					String qryMonthType = "history";
					String menuid = "myBill";
					String groupId = "tabs3";
					if(DateUtils.isEqual( theMonth)){//判断是否是当前月
						qryMonthType = "current";
					}
					cmap.put("qryMonthType", qryMonthType);
					cmap.put("theMonth", theMonth);					
					cmap.put("menuid", menuid);
					cmap.put("groupId", groupId);
					
					text =  hbYidong.postText(httpclient,payUrl,cmap);
					System.out.println(text);
					Document paydoc = Jsoup.parse(text);
					
					Elements payelements = paydoc.select("div[class=fyxx]").first().select("tr");
					for(int j=0;j<payelements.size();j++){
						Element payelement = payelements.get(j);
						Elements payDetails = payelement.select("td");
						System.out.println(j+"--"+payDetails.get(0).text()+"---"+payDetails.get(2));
						
					}
				
					
				}

				 
			
				
				
				//第三步：发送动态验证码
				
				
			}else{//登陆出错
				//打印出错信息
				RegexPaserUtil rp = new RegexPaserUtil("errorMsg=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String errorMeg= rp.getText();
				System.out.println(errorMeg);
			}
			
			
			
			
			
		}
		
		
		
	}

	
	/*
	 * 获取验证码图片
	 * */
	public String getAuthcode(DefaultHttpClient httpclient,String currentUser){
		String authcodeURL2 = authUrl + currentUser;
		int random = (int)(Math.random()*1000);
		String picName = currentUser+random+"_hbyd.png";
		String authUrls = authUrl + System.currentTimeMillis();
		java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyyMMdd");
		String dataPath = format2.format(new Date());

		try {
			downloadFile( httpclient,  authUrls, dataPath,  picName, "hb.ac.10086.cn", "https://hb.ac.10086.cn/SSO/loginbox");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileName =  dataPath+"/"+picName;
		fileName +="?random="+Math.random();
		
		String img =  fileName;
		return img;
	}
	
	public String login(DefaultHttpClient httpclient, String url, Map map){
		 String text = postText( httpclient,  url,  map);
		 return text;
	}
	
	/*
	 * 检查验证码是否合规，必须是6位
	 * */
	public Map checkPassword(String password){
		Map map = new HashMap();
		Boolean flag = false;
		if(password!=null &&password.trim().length()==6){
			flag = true;
		}
		map.put("flag", flag);
		return map;		
	}
	
	public String postText(DefaultHttpClient httpclient, String url, Map map) {
		CHeader h = new CHeader(CHeaderUtil.Accept_,"http://www.hb.10086.cn/my/billdetails/queryInvoice.action",CHeaderUtil.Content_Type__urlencoded,"www.hb.10086.cn");
		//h.setHost("hb.ac.10086.cn"); 
		h.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
		h.setAccept_Encoding("gzip, deflate");
		HttpPost post = CHeaderUtil.getPost(url, h,map);
		HttpResponse response = null;
		String result = "";
		try {
			response = httpclient.execute(post);
			result = new ParseResponse().parse(response);
			System.out.println(result);
		}  catch (Exception e) {
			e.printStackTrace();		
		}
	
		return result;

	}

	
	/*
	 * 得到网页的内容
	 */
	public String getText(DefaultHttpClient httpclient,
			String redirectLocation) {
		CHeader cHeader = new CHeader();		
		String responseBody = "";
		HttpResponse response = CUtil.getHttpGet(redirectLocation, cHeader, httpclient);
		if(response!=null){
			responseBody = ParseResponse.parse(response,"GBK");
		}
		return responseBody;
	}
	
	/*
	 * 下载验证码
	 */
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
		httpget.setHeader("Accept", "image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5");
		httpget.setHeader("Accept-Encoding", "gzip, deflate");
		httpget.setHeader("Accept-Language",
				"zh-CN");
		
		if (hostUrl != null && hostUrl.length() > 0) {
			httpget.setHeader("Host", hostUrl);
		}
		
		if (refererUrl != null && refererUrl.length() > 0) {
			httpget.setHeader("Referer", refererUrl);
		}
		
		httpget
				.setHeader("User-Agent",
						"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");

		File file = new File(destfilename);
		if (file.exists()) {
			file.delete();
		}

		HttpResponse response = httpclient.execute(httpget);

		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
			// 请求成功
			// 取得请求内容
			HttpEntity entity = response.getEntity();
			// 显示内容
			if (entity != null) {
				// 这里可以得到文件的类型 如image/jpg /zip /tiff 等等
				// 但是发现并不是十分有效，有时明明后缀是.rar但是取到的是null，这点特别说明
				System.out.println(entity.getContentType());
				// 可以判断是否是文件数据流
				System.out.println(entity.isStreaming());
				// 设置本地保存的文件
				FileOutputStream output = new FileOutputStream(destfilename);
				// 得到网络资源并写入文件
				InputStream input = entity.getContent();
				byte b[] = new byte[1024];
				int j = 0;
				while ((j = input.read(b)) != -1) {
					output.write(b, 0, j);
				}
				output.flush();
				output.close();
			}
			if (entity != null) {
				entity.consumeContent();
			}
		}
		
		PicUpload fupload = new PicUpload();
		fupload.upload(file.getAbsolutePath());
		
		return picName;

	}

			
}
