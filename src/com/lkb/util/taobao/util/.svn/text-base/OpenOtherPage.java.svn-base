//package com.lkb.util.taobao.util;
//
//import java.util.Map;
//
//import org.apache.http.Header;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import com.lkb.constant.TaoBaoConstant;
//import com.lkb.service.IPayInfoService;
//import com.lkb.service.IUserService;
//import com.lkb.service.IYuEBaoService;
//import com.lkb.util.httpclient.CHeaderUtil;
//import com.lkb.util.httpclient.CUtil;
//import com.lkb.util.httpclient.ParseResponse;
//import com.lkb.util.httpclient.entity.CHeader;
//import com.lkb.util.redis.RedisClient;
//import com.lkb.util.taobao.alipay.InfoPage;
//import com.lkb.util.taobao.yuebao.YuEBaoInfo;
//
//
//public class OpenOtherPage {
//	public static String alipay_url_1 = "https://auth.alipay.com/login/trust_login.do";
//	public static String alipay_url_2 = "https://login.taobao.com/member/login.jhtml?tpl_redirect_url=https%3A%2F%2Fauth.alipay.com%3A443%2Flogin%2FtrustLoginResultDispatch.htm%3FredirectType%3D%26sign_from%3D3000%26goto%3Dhttps%253A%252F%252Flab.alipay.com%252Fuser%252Fi.htm%253Fsrc%253Dyy_content_jygl&from_alipay=1";
//	public static String alipay_url_3 = "https://my.alipay.com/portal/i.htm?src=yy_content_jygl";
//	public static String yuebao_url_1 = "https://financeprod.alipay.com/fund/index.htm";
//	/***
//	 * 
//	 * @param url 值类型?src=yy_content_jygl&amp;sign_from=3000&amp;sign_account_no=20887020432209740156
//	 * @param map
//	 * @param userService
//	 * @param loginName
//	 * @param currentUser
//	 * @return
//	 */
//	public  boolean getAlipay(String url,Map map,IUserService userService,String loginName,String currentUser,IPayInfoService payInfoService){
//		url = url.replace("&amp;", "&");
//		url = alipay_url_1+url+"&goto=https://lab.alipay.com/user/i.htm";
//		boolean b = false;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,"http://i.taobao.com/my_taobao.htm",null,"lab.alipay.com",false);
//		HttpGet get = CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			response.getEntity().getContent().close();
//			//-------------------------------------------------------------------
//			 h = new CHeader(CHeaderUtil.Accept_,url,null,"login.taobao.com",false);
//			 get = CHeaderUtil.getHttpGet(alipay_url_2, h);
//			 DefaultHttpClient httpClient = RedisClient.getClient(map);
//				// 不让自动跳转,在这里主要是为了截取到跳转中的cookies
//			CUtil.setHandleRedirect(httpClient, false);
//			 response = httpClient.execute(get);
//			// 恢復成htmlclient自行管理請求轉向
//			CUtil.setHandleRedirect(httpClient, true);
//			Header header = response.getFirstHeader("Location");
//			//释放client链接,必须有,要不会有Invalid use of BasicClientConnManager: connection still allocated.异常
//			response.getEntity().getContent().close();
//			System.out.println("---------------------------支付宝地址-----------------------------");
//			if(header!=null){
//				String text = header.getValue();
//				System.out.println("支付宝最后登录地址:"+text);
//				h = new CHeader(CHeaderUtil.Accept_,url,null,"auth.alipay.com",false);
//				get = CHeaderUtil.getHttpGet(text, h);
//				response = RedisClient.getClient(map).execute(get);
//				response.getEntity().getContent().close();
//				
//				h = new CHeader(CHeaderUtil.Accept_,"https://auth.alipay.com/login/certCheck.htm",null,"my.alipay.com",false);
//				get = CHeaderUtil.getHttpGet(alipay_url_3, h);
//				response = RedisClient.getClient(map).execute(get);
//				text = ParseResponse.parse(response);
//				Document doc = Jsoup.parse(text);
//				String alipayName = doc.select("a[id=J-userInfo-account-userEmail]").first().attr("title");
//				map.put(TaoBaoConstant.alipayName,alipayName);
//				
//				
//				InfoPage infoPage = new InfoPage();
//				infoPage.openInfoPage(map, userService, alipayName, currentUser);
//				infoPage.openAlipayJiaoyi( map, payInfoService, currentUser);
//				infoPage.openAlipayJiaoyiYear( map, payInfoService,  currentUser);
//				
//				System.out.println("---------------------------支付宝打开页面内容-----------------------------");
//				//System.out.println("支付宝页面内容:"+text);
//				if(!text.contains("app-yuebao-manage-myalipay-v1")){
//					System.out.println("--------该用户未开通余额宝---------");
//					map.put(StartLogin.k_flag_num, "2");
//				}
//			}
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		
//		return b;
//	}
//	/**打开余额宝*/
//	public  boolean getYuEBao(Map map,IUserService userService,IYuEBaoService yuebaoService){
//		boolean b = false;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"financeprod.alipay.com",false);
//		HttpGet get = CHeaderUtil.getHttpGet(yuebao_url_1, h);
//		HttpResponse response = null;
//		try {
//			 DefaultHttpClient httpClient = RedisClient.getClient(map);
//			response = httpClient.execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------余额宝打开页面内容-----------------------------");
//			System.out.println("余额宝页面内容:"+text);
//			if(text.contains("进入我的余额宝")){
//				System.out.println("--------该用户未开通余额宝-------");
//			}else{
//				String alipayName = map.get(TaoBaoConstant.alipayName).toString();
//				YuEBaoInfo yuEBaoInfo = new YuEBaoInfo();
//				yuEBaoInfo.openShouyi(map, userService, yuebaoService, alipayName);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return b;
//	}
//	
//	
//	public void anlyzeYuEbao(){
//		
//	}
//}
