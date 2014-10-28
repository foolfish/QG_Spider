package com.lkb.test.fjdx;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import com.lkb.util.RegexPaserUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.constant.ConstantHC;
import com.lkb.util.httpclient.entity.CHeader;
import common.Logger;

/**
 * 注:江苏电信登陆注意事项</br>
 * 1.登陆地址:http://js.189.cn/</br>
 * 2.第一次post http://js.189.cn/self_service/validateLogin.action 提交解析SSORequestXML等参数</br>
 * 3.第二次post  https://uam.telecomjs.com/sso/JsLoginIn https方式 提交后重定向返回location</br>
 * 4.第三get location地址 解析返回内容进入第四步 </br>
 * 5.第四次post 解析上边内容 post提交 js.189.cn/bussiness/page/new/sso.html 带有解析后参数
 * 6.第五次get访问个人主页,好像没什么重要信息
 * @author fastw
 *
 */
public class Login {
	public static Logger log = Logger.getLogger(Login.class);
	
	public static void shengfen_id(Map map){
		String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"uam.ct10000.com");
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("username", ConstantHC.getUsername(map)); 
		HttpResponse response = CUtil.getPost(url, h,  ConstantHC.getClient(map), param);
		if(response!=null){
			String text=  ParseResponse.parse(response,"utf-8");
			if(text!=null){
				String s[] = text.split("\\|");
				map.put("shengfen_id", s[0]) ;
				map.put("shengfen_name",s[1]) ;
			}
		}
	}
	
	/**打开登陆页
	 * ip 本地ip
	 * */
	public static String index(Map map){
		boolean b = true;
		
		String reqUrl = "https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp="+map.get("ip").toString()+",172.16.10.20";
		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"uam.ct10000.com");
		HttpResponse response = CUtil.getHttpGet(reqUrl, h, ConstantHC.getClient(map));
		if(response!=null){
			return ParseResponse.parse(response,"utf-8");
		}
		return null;
	}
	/**第一次post*/
	public static String login(Map<String,Object> map,String text){
		Document doc = Jsoup.parse(text);
		String empoent = doc.select("input[id=forbidpass]").first().val();
		String forbidaccounts = doc.select("input[id=forbidaccounts]").first().val();
		String authtype = doc.select("input[name=authtype]").first().val();
		String customFileld02 = map.get("shengfen_id").toString();
		String areaname = map.get("shengfen_name").toString();
		String customFileld01 = "3";
		String lt = doc.select("input[name=lt]").first().val();
		String _eventId = doc.select("input[name=_eventId]").first().val();
		String open_no = "c2000004";
	
		String action = doc.select("form[id=c2000004]").first().attr("action");
		action = URLDecoder.decode(action);
		DefaultHttpClient client = ConstantHC.getClient(map);
		String url = "https://uam.ct10000.com"+action;
		CHeader h = new CHeader(CHeaderUtil.Accept_,"https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp="+"114.249.55.149",CHeaderUtil.Content_Type__urlencoded,"uam.ct10000.com",false);
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("empoent", empoent); 
		param.put("forbidaccounts",forbidaccounts); 
		param.put("authtype",authtype); 
		param.put("customFileld02", customFileld02); 
		param.put("areaname", areaname); 
		param.put("customFileld01", customFileld01); 
		param.put("lt", lt); 
		param.put("_eventId", _eventId); 
		param.put("username", ConstantHC.getUsername(map)); 
		param.put("password", ConstantHC.getPass(map)); 
		param.put("randomId", ConstantHC.getVcode(map)); 
		param.put("open_no", open_no); 
		System.out.println("验证码"+ConstantHC.getVcode(map));
//		h.setCookie(ConstantHC.getCookie(map));
		CUtil.setHandleRedirect(client, false);
		HttpResponse response = CUtil.getPost(url, h, client, param);
		CUtil.setHandleRedirect(client,true);
		
	    if(response!=null){
//	    	text = ParseResponse.parse(response,"utf-8");
//	    	System.out.println(text);
	    	String location = response.getFirstHeader("Location").getValue();
	    	ParseResponse.closeResponse(response);
			System.out.println("cookie:"+CUtil.getCookie(ConstantHC.getClient(map)));
			login2(map, location, param,h);
	    		
	    }
	    return null;
	}
	
	/**第二次post*/
	public static String login2(Map<String,Object> map ,String location,Map<String,String> param,CHeader h){
		DefaultHttpClient client = 	ConstantHC.getClient(map);
			HttpResponse response = CUtil.getPost(location, h, client, param);
		    if(response!=null){
		    	String text = ParseResponse.parse(response,"utf-8");
				if(text!=null){
					System.out.println(text);
					login3(map, text);
				}
		}
		 return null;
	}
	
	/**第三次post*/
	public static String login3(Map<String,Object> map,String text){
		RegexPaserUtil rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String url = rp.getText();
		System.out.println(url);
		
		CHeader  h = new CHeader(CHeaderUtil.Accept_,null,null,"189.cn");
		HttpResponse response = CUtil.getHttpGet(url, h, ConstantHC.getClient(map));
		if(response!=null){
			 text = ParseResponse.parse(response,"utf-8");
			if(text!=null){
				System.out.println("---------------第三次post----------------------");
				System.out.println(text);
				//余额查询页面 
				 text = login4(map, "http://fj.189.cn/service/bill/realtime.jsp","fj.189.cn");
				 text = login4(map, text,"fj.189.cn");
				 text = login4(map, text,"198.cn");
				 text = login4(map, text,"uam.ct10000.com");
				 text = login4(map, text,"uam.ct10000.com");
				text = login5(map, text); //返回200地址
				text =  login6(map, text);
				if(text!=null&&text.equals("true")){
					h = new CHeader(CHeaderUtil.Accept_,null,null,"fj.189.cn");
					response = CUtil.getHttpGet("http://fj.189.cn/service/bill/realtime.jsp", h, ConstantHC.getClient(map));
					if(response!=null){
						text = ParseResponse.parse(response);
						System.out.println("-----------------最后------------------");
						System.out.println(text);
					}
				}
				 
			}
		}
		return null;
	}

	/**第四次post*/
	public static String login4(Map<String,Object> map,String location,String host){
			CHeader  h = new CHeader(CHeaderUtil.Accept_,null,null,host);
			CUtil.setHandleRedirect(ConstantHC.getClient(map), false);
			HttpResponse response = CUtil.getHttpGet(location, h, ConstantHC.getClient(map));
			if(response!=null){
				location = response.getFirstHeader("Location").getValue();
				ParseResponse.closeResponse(response);
				return location;
			}
		return null;
	}
	
	/**第三次post*/
	public static String login5(Map<String,Object> map,String text){
		System.out.println("--------------"+text);
		CHeader  h = new CHeader(CHeaderUtil.Accept_,null,null,"uam.ct10000.com");
		HttpResponse response = CUtil.getHttpGet(text, h, ConstantHC.getClient(map));
		if(response!=null){
			text = ParseResponse.parse(response);
		}
		RegexPaserUtil rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String url = rp.getText();
		text = login4(map, url,"uam.fj.ct10000.com");
		System.out.println("------------------我执行到这了---------------");
		return text;
	}
	/**第三次post*/
	public static String login6(Map<String,Object> map,String text){
		CHeader  h = new CHeader(CHeaderUtil.Accept_,null,null,"fj.189.cn");
		HttpResponse response = CUtil.getHttpGet(text, h, ConstantHC.getClient(map));
		if(response!=null){
			text = ParseResponse.parse(response);
			if(text!=null){
				RegexPaserUtil rp = new RegexPaserUtil("parent.location.href ='","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String url = rp.getText();
				url = login4(map, "http://fj.189.cn"+url,"fj.189.cn");
				url = login4(map, url,"fj.189.cn");
				h = new CHeader(CHeaderUtil.Accept_,null,null,"fj.189.cn");
				response = CUtil.getHttpGet(url, h, ConstantHC.getClient(map));
				if(response!=null){
					text = ParseResponse.parse(response);
					return "true";
				}
			}
		}
		return text;
	}
	
	/**
	 * 加载验证码
	 *根据实际项目中情况修改
	 */
	public static String uploadVCode(Map<String,Object> map,String path){
		String url="https://uam.ct10000.com/ct10000uam/validateImg.jsp?"+new Date().getTime();
		boolean b = true;
		CHeader h = new CHeader("image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5",null,"uam.ct10000.com");
		CUtil.downimgCode(url, h, ConstantHC.getClient( map), path);
		//输入及 返回验证码
		return CUtil.inputYanzhengma();
	}
		
}
