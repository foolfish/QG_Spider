//package com.lkb.util.taobao.alipay;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import com.lkb.constant.TaoBaoConstant;
//import com.lkb.service.IPayInfoService;
//import com.lkb.service.IUserService;
//import com.lkb.thirdUtil.Alipay;
//import com.lkb.util.RegexPaserUtil;
//import com.lkb.util.httpclient.CHeaderUtil;
//import com.lkb.util.httpclient.ParseResponse;
//import com.lkb.util.httpclient.entity.CHeader;
//import com.lkb.util.redis.RedisClient;
//
//public class InfoPage {
//	/**支付宝的个人信息*/
//	public boolean openInfoPage(Map map,IUserService userService,String loginName,String currentUser){
//		boolean b = true;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,"https://auth.alipay.com/login/loginResultDispatch.htm",null,"my.alipay.com",false);
//		//-----------项目运行中注释改行-----/
//		//h.setCookie(Test.alipay);
//		HttpGet get = CHeaderUtil.getHttpGet(TaoBaoConstant.alipay_info, h);
//		HttpResponse response = null;
//		String text="";
//		try {
//			 DefaultHttpClient httpClient = (DefaultHttpClient)map.get(TaoBaoConstant.k_client);
//			response = httpClient.execute(get);
//			text = new ParseResponse().parse(response);
//			System.out.println("---------------------------支付宝个人信息页-----------------------------");
//			System.out.println("支付宝页面内容:"+text);
//			
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			Alipay alipay = new Alipay();
//			alipay.saveUserInfo(text, userService, loginName, currentUser);
//		}
//		return b;
//	}
//	/**交易记录之一年内*/
//	public boolean openAlipayJiaoyi(Map map,IPayInfoService payInfoService,String currentUser){
//		/**支付宝交易记录一年内**/
//		StringBuffer sburl = new StringBuffer();
//		sburl.append("https://consumeprod.alipay.com/record/standard.htm?_input_charset=utf-8&dateRange=oneYear&tradeType=all&status=all&fundFlow=all&beginTime=00%3A00&dateType=createDate&endTime=24%3A00");
//		sburl.append("&beginDate=").append(TimeUtil.getStartTime());
//		sburl.append("&endDate=").append(TimeUtil.getEndTime());
//		sburl.append("&pageNum=");
//		boolean b = true;
//		Integer page = 1;
//		String url = openAlipayJiaoyiInfo(sburl.toString(),page,map, payInfoService, currentUser);
//		try{
//			page = Integer.parseInt(url.substring(url.length()-1));
//		}catch(Exception e ){
//			System.out.println("解析url错误");
//			return false;
//		}
//		for (int i = 2; i <= page; i++) {
//			System.out.println("-----------当前第"+i+"页-------------");
//			openAlipayJiaoyiInfo(sburl.toString(),i,map, payInfoService, currentUser);
//		}
//		
//		
//		
//		
//		return b;
//	}
//	/**交易记录*/
//	public String openAlipayJiaoyiInfo(String url,Integer page,Map map,IPayInfoService payInfoService,String currentUser){
//		CHeader h = new CHeader(CHeaderUtil.Accept_,"https://auth.alipay.com/login/loginResultDispatch.htm",null,"consumeprod.alipay.com",false);
//		//-----------项目运行中注释改行-----/
//		//h.setCookie(Test.alipay);
//		HttpGet get = CHeaderUtil.getHttpGet(url+page, h);
//		HttpResponse response = null;
//		RegexPaserUtil rp = null;
//		String str = null;
//		try {
//			 DefaultHttpClient httpClient = RedisClient.getClient(map);
//			response = httpClient.execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------支付宝交易信息页-----------------------------");
//			//System.out.println("支付宝交易信息页:"+text);
//			if(text!=null){
//				Alipay alipay = new Alipay();
//				String alipayName = map.get(TaoBaoConstant.alipayName).toString();
//				alipay.saveUserOrderByHtmlparser( text,
//						 payInfoService,  currentUser,alipayName);
//				
//				if(page.intValue()==1){
//					rp = new RegexPaserUtil("<a class=\"page-end\" href=\"","\">尾页",text,RegexPaserUtil.zel_all_chars);
//			    	str = rp.getText();
//			    	System.out.println("总页数:"+str);
//				}
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return str;
//	}
//	/**交易记录之一年之前*/
//	public boolean openAlipayJiaoyiYear(Map map,IPayInfoService payInfoService, String currentUser){
//		/**支付宝交易记录一年之前**/
//		String url = "https://lab.alipay.com/consume/record/historyIndexNew.htm";
//		boolean b = true;
//		String page = "1";
//		String criticalDate = TimeUtil.getCriticalDate();
//		String endTime = TimeUtil.getOneYearBeforeTime();
//		boolean flag = true;
//		int i = 0;
//		Map<Integer,String> smap = openAlipayJiaoyiInfoYearGet(url,map);
//		while(flag){
//			smap = openAlipayJiaoyiInfoYearPost(url,page,map,"2005-01-01",criticalDate,endTime,smap, payInfoService,  currentUser);
//			if(smap!=null&&smap.get(1)!=null){
//				page = smap.get(1).toString();
//				//System.out.println(page);
//			}else{
//				flag = false;
//			}
//			i++;
//			//防止死循环
//			if(i>100){
//				flag = false;
//			}
//		}
//		
//		return b;
//	}
//	public Map<Integer,String> openAlipayJiaoyiInfoYearGet(String url,Map map){
//		CHeader h = new CHeader(CHeaderUtil.Accept_,"https://lab.alipay.com/consume/record/historyIndexNew.htm",CHeaderUtil.Content_Type__urlencoded,"lab.alipay.com",false);
//		//-----------项目运行中注释改行-----/
//		//h.setCookie(Test.alipay);
//		DefaultHttpClient httpClient = (DefaultHttpClient)map.get(TaoBaoConstant.k_client);
//		HttpGet get = CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//		RegexPaserUtil rp = null;
//		String str = null;
//		Map<Integer,String> smap  = new HashMap<Integer,String>();
//	    try {
//	    	response = httpClient.execute(get);
//			String text = new ParseResponse().parse(response);
//			System.out.println("---------------------------支付宝一年之前交易信息页-----------------------------");
//			System.out.println("支付宝一年之前:"+text);
//		
////			rp = new RegexPaserUtil("<a class=\"page-next form-element\" href=\"#\" rel=\"currentPageNo\" rel-value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
////	    	str = rp.getText();
////	    	System.out.println("下一页:"+str);
//	    	rp = new RegexPaserUtil("<input type=\"hidden\" name=\"_form_token\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//	    	smap.put(1, str);
//	    	smap.put(2, rp.getText());
//	    	
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return smap;
//	}
//	/**交易记录一年之前*/
//	public Map<Integer,String> openAlipayJiaoyiInfoYearPost(String url,String page,Map map,String beginTime,String criticalDate,String endTime,Map<Integer,String> smap,IPayInfoService payInfoService, String currentUser){
//		if(smap.get(2)==null){
//			return null;
//		}
//		CHeader h = new CHeader(CHeaderUtil.Accept_,"https://lab.alipay.com/consume/record/historyIndexNew.htm",CHeaderUtil.Content_Type__urlencoded,"lab.alipay.com",false);
//		//-----------项目运行中注释改行-----/
//		//h.setCookie(Test.alipay);
//		smap.put(1,null);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("_form_token",smap.get(2).toString());    		
//		param.put("customerType","2");
//		param.put("keyword","0_");	
//		param.put("beginTime",beginTime);	
//		param.put("endTime",endTime);	
//		param.put("criticalDate",criticalDate);	
//		param.put("currentPageNo",page);	
//		
//		DefaultHttpClient httpClient = RedisClient.getClient(map);
//		HttpPost post = CHeaderUtil.getPost(url, h,param);
//		HttpResponse response = null;
//		RegexPaserUtil rp = null;
//		String str = null;
//	    try {
//	    	response = httpClient.execute(post);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------支付宝一年之前交易信息页-----------------------------");
//			System.out.println(":"+text);
//			
//			String alipayName = map.get(TaoBaoConstant.alipayName).toString();
//			Alipay alipay = new Alipay();
//			alipay.anLyzerAlipayOneYear(text, payInfoService,  currentUser,alipayName);
//			
//			rp = new RegexPaserUtil("<a class=\"page-next form-element\" href=\"#\" rel=\"currentPageNo\" rel-value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//	    	str = rp.getText();
//	    	System.out.println("下一页:"+str);
//	    	rp = new RegexPaserUtil("<input type=\"hidden\" name=\"_form_token\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//	    	smap.put(1, str);
//	    	smap.put(2, rp.getText());
//	    	
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return smap;
//	}
//	
//	
//	
//	
//	
//
//}
