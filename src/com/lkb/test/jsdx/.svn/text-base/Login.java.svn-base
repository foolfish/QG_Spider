package com.lkb.test.jsdx;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;


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
	/**打开登陆页*/
	public static boolean index(Map map){
		boolean b = true;
		String reqUrl = "http://js.189.cn/service";
		CHeader h = new CHeader();
		h.setCookie(ConstantHC.getCookie(map));
		HttpResponse response = CUtil.getHttpGet(reqUrl, h, ConstantHC.getClient(map));
		if(response!=null){
			ParseResponse.closeResponse(response);
			//输出cookie
			System.out.println("cookie:"+CUtil.getCookie(ConstantHC.getClient(map)));
		}else{
			b = false;
		}
		return b;
	}
	/**第一次post*/
	public static String login_1(Map<String,Object> map){
		DefaultHttpClient client = ConstantHC.getClient(map);
		String url = "http://js.189.cn/self_service/validateLogin.action";
		CHeader h = new CHeader(CHeaderUtil.Accept_,"http://js.189.cn/",CHeaderUtil.Content_Type__urlencoded,"js.189.cn",false);
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("logonPattern", "1"); 
		param.put("isshow", "2"); 
		param.put("ssoURL", "http://js.189.cn/bussiness/page/new/sso.html?"+Math.random()); 
		param.put("userType", "2000004"); 
		param.put("productId", ConstantHC.getUsername(map)); 
		param.put("loginPwdType", "cellPassword"); 
		param.put("userPwd", ConstantHC.getPass(map)); 
		param.put("validateCode", ConstantHC.getVcode(map)); 
		System.out.println("验证码"+ConstantHC.getVcode(map));
		param.put("serviceNumber", "可不填"); 
//		h.setCookie(ConstantHC.getCookie(map));
		HttpResponse response = CUtil.getPost(url, h, client, param);
		//输出cookie
		System.out.println("cookie:"+CUtil.getCookie(ConstantHC.getClient(map)));
	    if(response!=null){
	    	String text = ParseResponse.parse(response,"utf-8");
	    	if(text!=null){
	    		if(text.contains("验证码错误")){
	    			return text;
	    		}
	    		return login2(map, text);
	    	}
	    		
	    }
	    return null;
	}
	/**第一次post, 老入口 废弃*/
	public static String login1(Map<String,Object> map){
		DefaultHttpClient client = ConstantHC.getClient(map);
		String url = "http://js.189.cn/self_service/validateLogin.action";
		CHeader h = new CHeader(CHeaderUtil.Accept_,"http://js.189.cn/service",CHeaderUtil.Content_Type__urlencoded,"js.189.cn",false);
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("logonPattern", "1"); 
		param.put("userType", "2000004"); 
		param.put("validateCode", ConstantHC.getVcode(map)); 
		param.put("qqNum", ""); 
		param.put("productId", ConstantHC.getUsername(map)); 
		param.put("loginPwdType", "cellPassword"); 
		param.put("userPwd", ConstantHC.getPass(map)); 
		System.out.println("验证码"+ConstantHC.getVcode(map));
		param.put("validate", ConstantHC.getVcode(map)); 
//		h.setCookie(ConstantHC.getCookie(map));
		HttpResponse response = CUtil.getPost(url, h, client, param);
		//输出cookie
		System.out.println("cookie:"+CUtil.getCookie(ConstantHC.getClient(map)));
	    if(response!=null){
	    	String text = ParseResponse.parse(response,"utf-8");
	    	System.out.println(text);
	    	log.debug(text);
	    	if(text!=null){
	    		if(text.contains("验证码错误")){
	    			return text;
	    		}
	    		return login2(map, text);
	    	}
	    		
	    }
	    return null;
	}
	/**第二次post*/
	public static String login2(Map<String,Object> map ,String text){
		DefaultHttpClient client = 	ConstantHC.getClient(map);
		Map<String,String> 	param = new HashMap<String,String>();
		RegexPaserUtil rp = new RegexPaserUtil("name=\"SSORequestXML\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String requestXml = rp.getText();
		if(requestXml!=null){
			System.out.println("xml:"+requestXml);
			param.put("SSORequestXML",requestXml);	
			rp = new RegexPaserUtil("paramUam\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("paramUam",rp.getText());
			rp = new RegexPaserUtil("ds\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("ds",rp.getText());
			rp = new RegexPaserUtil("systemCode\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("systemCode",rp.getText());
			
			CHeader h = new CHeader(CHeaderUtil.Accept_,"http://js.189.cn/self_service/validateLogin.action",CHeaderUtil.Content_Type__urlencoded,
					"uam.telecomjs.com");
			CUtil.setHandleRedirect(client, false);
			h.setAccept_Encoding("gzip, deflate");
			h.setCookie(ConstantHC.getCookie(map));
			HttpResponse response = CUtil.getPost("https://uam.telecomjs.com/sso/JsLoginIn", h, client, param);
		    if(response!=null){
		    	String location = response.getFirstHeader("Location").getValue();
		    	System.out.println("location:-----"+location);
		    	CUtil.setHandleRedirect(client, true);
				 ParseResponse.closeResponse(response);
//				 return location;
		    	return login3(map, location ,param );
		    	
		    }
		}
		 return null;
	}
	
	/**第三次post*/
	public static String login3(Map<String,Object> map,String location,Map<String,String> 	param ){
		CHeader  h = new CHeader(CHeaderUtil.Accept_,null,null,"js.189.cn");
		HttpResponse response = CUtil.getHttpGet(location, h, ConstantHC.getClient(map));
		if(response!=null){
			String text = ParseResponse.parse(response,"utf-8");
			if(text!=null){
				System.out.println("---------------第三次post----------------------");
				System.out.println(text);
				return login4(map, text);
			}
		}
		
		return null;
	}

	/**第四次post*/
	public static String login4(Map<String,Object> map,String text){
		Map<String,String> 	param = new HashMap<String,String>();
		RegexPaserUtil rp = new RegexPaserUtil("action=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String url = rp.getText();
		if(url!=null&&url.contains("js.189.cn/bussiness/page/new/sso.html")){
			rp = new RegexPaserUtil("id=\"sp\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("sp",rp.getText());
			rp = new RegexPaserUtil("id=\"ssoURL\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("ssoURL",rp.getText());
			rp = new RegexPaserUtil("redirectURL\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("redirectURL",rp.getText());
			rp = new RegexPaserUtil("id=\"isshow\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("isshow",rp.getText());
			rp = new RegexPaserUtil("d=\"msg\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("msg",rp.getText());
			rp = new RegexPaserUtil("result\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("result",rp.getText());
			rp = new RegexPaserUtil("loginDescription\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("loginDescription",rp.getText());
			CHeader  h = new CHeader(CHeaderUtil.Accept_,null,null,"js.189.cn");
			HttpResponse response = CUtil.getPost(url.trim(), h, ConstantHC.getClient(map),param);
			if(response!=null){
				text = ParseResponse.parse(response,"utf-8");
				if(text!=null){
					System.out.println("---------------登陆成功返回页----------------------");
					System.out.println(text);
					return login5(map);
				}
			}
		}
		
		return null;
	}
	/***
	 * 打开个人主页
	 */
	//
	/**最后一步*/
	public static String login5(Map<String,Object> map ){
		CHeader  h = new CHeader(CHeaderUtil.Accept_,null,null,"js.189.cn");
		HttpResponse response = CUtil.getHttpGet("http://js.189.cn/service/account", h, ConstantHC.getClient(map));
		if(response!=null){
			String text = ParseResponse.parse(response,"utf-8");
			if(text!=null){
				System.out.println("---------------第三次post----------------------");
				System.out.println(text);
				return "true";
			}
		}
		
		return null;
	}
	/**
	 * 加载验证码
	 *根据实际项目中情况修改
	 */
	public static String uploadVCode(Map<String,Object> map,String path){
		String url="http://js.189.cn/rand.action?t="+new Date().getTime();
		boolean b = true;
		CHeader h = new CHeader("image/png, image/svg+xml, image/*;q=0.8, */*;q=0.5",null,"js.189.cn");
		CUtil.downimgCode(url, h, ConstantHC.getClient( map), path);
		//输入及 返回验证码
		return CUtil.inputYanzhengma();
	}
		
}
