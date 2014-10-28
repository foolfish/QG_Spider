//package com.lkb.util.taobao.util;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.client.HttpClient;
//import org.apache.http.impl.client.AbstractHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.log4j.Logger;
//
//import com.lkb.service.IOrderItemService;
//import com.lkb.service.IOrderService;
//import com.lkb.service.IUserService;
//import com.lkb.util.httpclient.CUtil;
//import com.lkb.util.taobao.yuebao.YuEBaoInfo;
//
//
//public class StartLogin {
//	public static String k_client = "client";
//	public static String k_yanzhengma = "yanzhengma";
//	public static String k_token = "token";
//	/**返回值中提取st值*/
//	public static String k_st = "st";
//	public static String k_url = "k_url";
//	/**错误信息*/
//	public static String k_error = "k_error";
//	/**验证码*/
//	public static String k_vcode = "k_vcode";
//	/**标示符,如果 k_flag_num==1跳过第三部直接进入第四部*/
//	public static String k_flag_num = "k_flag_num";
//	/**支付宝地址*/
//	public static String k_alipay_url = "k_alipay_url";
//	
//	public static String loginPage = "https://login.taobao.com/member/login.jhtml";
//	/**验证账户*/
//	public static String loginPage_1 = "https://login.taobao.com/member/request_nick_check.do?_input_charset=utf-8";
//	
//	
//	private static Logger log = Logger.getLogger(LoginPage.class);
//
//	
//	
//	public static void main(String[] args) {
//		Map map = new HashMap();
//		//初始化client
//		DefaultHttpClient client = CUtil.init();
//		map.put(k_client, client);
//		//第一步
//		boolean b  = LoginPage.index(loginPage, map);
//		//第二步
//		b = LoginPage.checkUser(loginPage_1, map,LoginPage.TPL_usernames,LoginPage.TPL_passwords);
//		if(b){
//			//第三步
//			if(getFlag_num(map)!=null&&getFlag_num(map).toString().equals("1")){ }
//			else{
////				b = LoginPage.loginJson(loginPage, map,LoginPage.TPL_usernames,LoginPage.TPL_passwords);
//			}
//			if(b){
//				//第四步
//				String loginPage_3 = "https://passport.alipay.com/mini_apply_st.js?site=0&callback=vstCallback102&token="+getToken(map);
//				b = LoginPage.passport(loginPage_3, map);
//				
//				//第五步
//				loginPage_3 = "https://login.taobao.com/member/vst.htm?st="+getST(map)+"&params=style%3Ddefault%26sub%3D%26TPL_username%3D"+LoginPage.TPL_usernames+"%26loginsite%3D0%26from_encoding%3D%26not_duplite_str%3D%26guf%3D%26full_redirect%3D%26isIgnore%3D%26need_sign%3D%26sign%3D%26from%3Dtb%26TPL_redirect_url%3D%26css_style%3D%26allp%3D&_ksTS="+new Date().getTime()+"_115&callback=jsonp116";
////				b = LoginPage.login(loginPage_3, map);
//				
//				//第六步
////				b = LoginPage.iTaobao(getURL(map), map,null,null,null,null,null);
//				
//				OpenOtherPage openOtherPage = new OpenOtherPage();
//				//第七步 打开支付宝
//				b = openOtherPage.getAlipay(getAlipayURL(map), map, null, LoginPage.TPL_usernames, null,null);
//				if(getFlag_num(map)!=null&&getFlag_num(map).toString().equals("2")){
//					System.out.print("流程结束,未开通余额宝");
//				}
//				else{
//					b = openOtherPage.getYuEBao(map,null,null);
//					
//					System.out.print("执行完毕");
//				}
//				
//			}else{
//				System.out.println("第三步错误:"+getErrorInfo(map));
//			}
//			
//		}else{
//			System.out.println("第二步错误:"+getErrorInfo(map));
//		}
////		 List<org.apache.http.cookie.Cookie> cookies =  client.getCookieStore().getCookies();
////		 for (int i = 0; i < cookies.size(); i++) {
////			System.out.println(cookies.get(i).getName()+":"+cookies.get(i).getValue());
////		}
//		CUtil.colse(client);
//		map.remove(k_client);
//	}
//	/**
//	 * @param map 初始化的httpclint存放到map中后取值
//	 * @return
//	 */
//	public static HttpClient getClient(Map map){
//		return (HttpClient) map.get(k_client);
//	}
//	/**验证码地址**/
//	public static String getYanzhengmaURL(Map map){
//		return  map.get(k_yanzhengma).toString();
//	}
//	/**token**/
//	public static String getToken(Map map){
//		return  map.get(k_token).toString();
//	}
//	/**st值**/
//	public static String getST(Map map){
//		return  map.get(k_st).toString();
//	}
//	/**url**/
//	public static String getURL(Map map){
//		return  map.get(k_url).toString();
//	}
//	/**错误信息**/
//	public static String getErrorInfo(Map map){
//		return  map.get(k_error).toString();
//	}
//	/**验证码**/
//	public static String getVcode(Map map){
//		return  map.get(k_vcode).toString();
//	}
//	/**支付宝跳转地址**/
//	public static String getAlipayURL(Map map){
//		return  map.get(k_alipay_url).toString();
//	}
//	/**标示 1代表跳过第三步 json提交**/
//	public static String getFlag_num(Map map){
//		if(map.get(k_flag_num)!=null){
//			return  map.get(k_flag_num).toString();
//		}else{
//			return null;
//		}
//		
//	}
//	/***
//	 * 秒单位
//	 * @param time
//	 */
//	public static void sleep(int time){
//		try {
//			Thread.sleep(time*1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//}
