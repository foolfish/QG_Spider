//package com.lkb.util.taobao.yuebao;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import com.lkb.constant.TaoBaoConstant;
//import com.lkb.service.IUserService;
//import com.lkb.service.IYuEBaoService;
//import com.lkb.thirdUtil.YuEBaoUtil;
//import com.lkb.util.DateUtils;
//import com.lkb.util.RegexPaserUtil;
//import com.lkb.util.httpclient.CHeaderUtil;
//import com.lkb.util.httpclient.ParseResponse;
//import com.lkb.util.httpclient.entity.CHeader;
//import com.lkb.util.redis.RedisClient;
//
//public class YuEBaoInfo {
//	/**余额宝收益信息*/
//	public boolean openShouyi(Map map,IUserService userService,IYuEBaoService yuebaoService,String alipayName){
//		String url = "https://financeprod.alipay.com/fund/asset.htm";
//		boolean b = true;
//		String token = openGet(url, map);
// 		Map<String,String> smap = new HashMap<String, String>();
// 		smap.put("_form_token", token);
// 		smap.put("direction", "income");
// 		smap.put("startDate", "2013-05-01");
// 		smap.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
// 		Integer page = 1;
// 		page = openPost(url, page, map, smap, userService, yuebaoService, alipayName);
// 		int result = 0 ;
// 		for (int i = 2; i <= page; i++) {
// 			result = openPost(url, i, map, smap, userService, yuebaoService, alipayName);
// 			if(result==-1){
// 				System.out.println("程序出错以后捕捉");
// 				break;
// 			}
//		}
//		return b;
//	}
//	/**模拟浏览器打开收益页用于获取token值*/
//	public String openGet(String url,Map map){
//		boolean b = true;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,"https://financeprod.alipay.com/fund/index.htm",null,"financeprod.alipay.com",false);
//		//-----------项目运行中注释改行-----/
//		//h.setCookie(Test.alipay);
//		HttpGet get = CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//		try {
//			 DefaultHttpClient httpClient = RedisClient.getClient(map);
//			response = httpClient.execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("打开余额宝页面内容:"+text);
////			RegexPaserUtil rp = new RegexPaserUtil("<a class=\"page-next form-element\" href=\"#\" rel=\"currentPageNo\" rel-value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			RegexPaserUtil rp = new RegexPaserUtil("\"J-calendar-form\">","id=\"J_submit_time",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			String str = rp.getText();
//			rp = new RegexPaserUtil("_form_token\" value=\"","\"",str,RegexPaserUtil.TEXTEGEXANDNRT);
//			str = rp.getText();
//			System.out.println("_token值"+str);
//			return str;
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return null;
//	}
//	/**余额宝收益信息*/
//	public Integer openPost(String url,Integer page,Map map,Map<String,String> smap,IUserService userService,IYuEBaoService yuebaoService,String alipayName ){
//		CHeader h = new CHeader(CHeaderUtil.Accept_,"https://financeprod.alipay.com/fund/asset.htm",CHeaderUtil.Content_Type__urlencoded,"financeprod.alipay.com",false);
//		//-----------项目运行中注释改行-----/
//		//h.setCookie(Test.alipay);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("_form_token",smap.get("_form_token").toString());    		
//		param.put("direction",smap.get("direction").toString());
//		param.put("startDate",smap.get("startDate").toString());	
//		param.put("endDate",smap.get("endDate").toString());	
//		param.put("pageNum",page+"");	
//		
//		DefaultHttpClient httpClient = RedisClient.getClient(map);
//		HttpPost post = CHeaderUtil.getPost(url, h,param);
//		HttpResponse response = null;
//		RegexPaserUtil rp = null;
//		String str = null;
//	    try {
//	    	response = httpClient.execute(post);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------余额宝第"+page+"页-----------------------------");
//			//System.out.println(text);
//			YuEBaoUtil yuEBaoUtil = new YuEBaoUtil();
//			if(text!=null && text.trim().length()>10){				
//				yuEBaoUtil.listLog( text, yuebaoService, alipayName);
//			}
//
//			rp = new RegexPaserUtil("\"J-calendar-form\">","id=\"J_submit_time",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			str = rp.getText();
//			rp = new RegexPaserUtil("_form_token\" value=\"","\"",str,RegexPaserUtil.TEXTEGEXANDNRT);
//			str = rp.getText();
//			smap.put("_form_token", str);
//			if(page==1){
//				if(text!=null && text.trim().length()>10){		
//					yuEBaoUtil.saveLog(text, userService, yuebaoService, alipayName);				
//				}
//				rp = new RegexPaserUtil("<span class=\"ui-page-bold\">第 1 /","</span>",text,RegexPaserUtil.TEXTEGEXANDNRT);
//		    	str = rp.getText();
//		    	if(str!=null){
//		    		return Integer.parseInt(str.trim());
//		    	}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			return -1;
//		}
//	    return page;
//}
//	/**余额宝转出信息*/
//	public boolean openZhuanchu(Map map,IUserService userService,IYuEBaoService yuebaoService,String alipayName){
//		String url = "https://financeprod.alipay.com/fund/asset.htm";
//		boolean b = true;
//		String token = openGet(url+"?direction=in", map);
// 		Map<String,String> smap = new HashMap<String, String>();
// 		smap.put("_form_token", token);
// 		smap.put("direction", "in");
// 		smap.put("startDate", "2013-05-01");
// 		smap.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
// 		Integer page = 1;
// 		page = openPost(url, page, map, smap, userService, yuebaoService, alipayName);
// 		System.out.println("--------余额宝转出信息------------");
// 		int result = 0 ;
// 		for (int i = 2; i <= page; i++) {
// 			result = openPost(url, i, map, smap, userService, yuebaoService, alipayName);
// 			System.out.println("--------余额宝转出信息------------");
// 			if(result==-1){
// 				System.out.println("程序出错以后捕捉");
// 				break;
// 			}
//		}
//		return b;
//	}
//	/**余额宝转出信息*/
//	public boolean openZhuanru(Map map,IUserService userService,IYuEBaoService yuebaoService,String alipayName){
//		String url = "https://financeprod.alipay.com/fund/asset.htm";
//		boolean b = true;
//		String token = openGet(url+"?direction=out", map);
//		Map<String,String> smap = new HashMap<String, String>();
//		smap.put("_form_token", token);
//		smap.put("direction", "out");
//		smap.put("startDate", "2013-05-01");
//		smap.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
//		Integer page = 1;
//		page = openPost(url, page, map, smap, userService, yuebaoService, alipayName);
//		System.out.println("--------余额宝转出信息------------");
//		int result = 0 ;
//		for (int i = 2; i <= page; i++) {
//			result = openPost(url, i, map, smap, userService, yuebaoService, alipayName);
//			System.out.println("--------余额宝转出信息------------");
//			if(result==-1){
//				System.out.println("程序出错以后捕捉");
//				break;
//			}
//		}
//		return b;
//	}
//	
//	
//}
