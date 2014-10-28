package com.lkb.test.zjdx;


import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.constant.ConstantHC;
import com.lkb.util.httpclient.entity.CHeader;

/**
 * 注:浙江登陆注意事项</br>
 * 1.登陆地址:http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page</br>
 * 2.第一次post提交解析success对应的值,做下次参数提交</br>
 * 3.第二次post提交后解析extraInfo对应的值,做下次参数提交</br>
 * 4.第三post提交获取location值 重定向 </br>
 * 5.第四次post提交带有auth_token的location.href的url
 * 6.第五次get访问登陆成功
 * 
 * 两次post提交后第三次post提交会有一次302重定向问题,然后第四次post
 * 
 * @author lkb
 *
 */
public class Login {
	
	/**打开登陆页*/
	public static boolean index(Map map){
		boolean b = true;
		String reqUrl = "http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page";
		CHeader h = new CHeader();
		HttpResponse response = CUtil.getHttpGet(reqUrl, h, ConstantHC.getClient(map));
		if(response!=null){
			ParseResponse.closeResponse(response);
		}else{
			b = false;
		}
		return b;
	}
	/**第一次post*/
	public static String login1(Map<String,Object> map){
		DefaultHttpClient client = ConstantHC.getClient(map);
		//------------post1------------------------/
		String url = "http://zj.189.cn/wt_uac/accounttype!gettype.ajax";
		CHeader h = new CHeader(CHeaderUtil.Accept_json,"http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page",CHeaderUtil.Content_Type__urlencoded,
				"zj.189.cn",true);
		Map<String,String> param = new HashMap<String,String>();
		param.put("account",ConstantHC.getUsername(map));							param.put("area_id","");
		param.put("authBean.app_name","wt"); 					param.put("authBean.auth_name","uam_login_auth");
		param.put("authBean.call_back_url","");		
		param.put("authBean.channel","wt");
		//---------------可能会出问题------
		param.put("authBean.client_ip","116.238.166.71");		
		param.put("authBean.module","null");
		param.put("authSign","");			
		param.put("isSaveCookie","0");			
		param.put("nexturl","service/");
		param.put("password",ConstantHC.getPass(map));		
		param.put("phoneVerify","");
		param.put("pr_area","");			
		param.put("pr_type","");
		param.put("type","");
		param.put("validcode",ConstantHC.getVcode(map));
		param.put("verifySign","");
		
		HttpResponse response = CUtil.getPost(url, h, client, param);
	    if(response!=null){
	    	String text = ParseResponse.parse(response,"utf-8");
	    	if(text!=null){
	    		return login2(map, text);
	    	}
	    		
	    }
	    return null;
	}
	/**第二次post*/
	public static String login2(Map<String,Object> map ,String text){
		RegexPaserUtil rp = new RegexPaserUtil("success:",":",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String authSign = rp.getText();
		if(authSign!=null){
			CHeader h = new CHeader(CHeaderUtil.Accept_json,"http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page",CHeaderUtil.Content_Type__urlencoded,
					"zj.189.cn",true);
			Map<String,String> 	param = new HashMap<String,String>();
			param.put("account",ConstantHC.getUsername(map));							param.put("area_id","573");
			param.put("authBean.app_name","wt"); 					param.put("authBean.auth_name","uam_login_auth");
			param.put("authBean.call_back_url","");		
			param.put("authBean.channel","wt");
			//---------------可能会出问题
			param.put("authBean.client_ip","116.238.166.71");		
			param.put("authBean.module","null");
			param.put("authSign",authSign);			
			param.put("isSaveCookie","0");			
			param.put("nexturl","service/");
			param.put("password",ConstantHC.getPass(map));		
			param.put("phoneVerify","");
			param.put("pr_area","");			
			param.put("pr_type","");
			param.put("type","18");
			param.put("validcode",ConstantHC.getVcode(map));
			param.put("verifySign","");
			
		    HttpResponse response = CUtil.getPost("http://zj.189.cn/wt_uac/ajaxauth!login.ajax", h, ConstantHC.getClient(map), param);
		    if(response!=null){
		    	text = ParseResponse.parse(response,"gbk");
		    	if(text!=null){
		    		if(text.contains("输入有错误")){
		    			return text;
		    		}
		    		return login3(map, text);
		    	}
		    	
		    }
		}
		 return null;
	}
	/**第三次post*/
	public static String login3(Map<String,Object> map,String text){
		DefaultHttpClient client = 	ConstantHC.getClient(map);
		RegexPaserUtil rp = new RegexPaserUtil("extraInfo\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
		text = rp.getText();
		if(text!=null){
			CHeader	h = new CHeader(CHeaderUtil.Accept_,"http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page",CHeaderUtil.Content_Type__urlencoded,
					"uam.zj.ct10000.com",true);
			Map<String,String>	param = new HashMap<String,String>();
			param.put("SSORequestXML", text);	
			
			CUtil.setHandleRedirect(client, false);
			HttpResponse response = CUtil.getPost("http://uam.zj.ct10000.com/portal/LoginAuth", h, client, param);	
		    if(response!=null){
		    	CUtil.setHandleRedirect(client, true);
		    	String location = response.getFirstHeader("Location").getValue();
				 ParseResponse.closeResponse(response);
				 if(text!=null){
					return  login4(map, text,location); 
				 }
		    }
		}
		return null;
	
	}
	/**第四次post*/
	public static String login4(Map<String,Object> map,String text,String location){
		CHeader  h = new CHeader(CHeaderUtil.Accept_,"http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page",null,"zj.189.cn");
		Map<String,String>	param = new HashMap<String,String>();
		param.put("SSORequestXML", text);	
		HttpResponse response = CUtil.getPost(location, h, ConstantHC.getClient(map), param);
		if(response!=null){
			text = ParseResponse.parse(response,"utf-8");
			if(text!=null){
				return login5(map, text);
			}
		}
		
		return null;
	}
	/**第五次get*/
	public static String login5(Map<String,Object> map,String text){
		//location.href = "
		RegexPaserUtil rp = new RegexPaserUtil("location.href = \"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
		text = rp.getText();
		if(text!=null){
			CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"zj.189.cn");
			HttpResponse	response = CUtil.getHttpGet(text, h, ConstantHC.getClient(map));
			if(response!=null){
				text = new ParseResponse().parse(response,"gbk");
				//***********以后做检查**********/
				System.out.println(text);
				return text;
				
			}
		}
		return null;
	}
	/**
	 * 加载验证码
	 *根据实际项目中情况修改
	 */
	public static String uploadVCode(Map<String,Object> map,String path){
		String url="http://zj.189.cn/wt_uac/UserCCServlet?type=2&method=loginimage";
		boolean b = true;
		CHeader h = new CHeader(null,"http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page",null,"zj.189.cn");
		com.lkb.util.httpclient.CUtil.downimgCode(url, h, ConstantHC.getClient( map), path);
		//输入及 返回验证码
		return CUtil.inputYanzhengma();
	}
		
}
