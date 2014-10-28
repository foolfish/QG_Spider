package com.lkb.test.sj;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.constant.ConstantHC;
import com.lkb.util.httpclient.entity.CHeader;



public class Start {

	
	
	public static void main(String[] args) {
		test1();
	}
	public static void test(){
		Map<String,Object> map = new HashMap<String,Object>();
		DefaultHttpClient client = CUtil.init(); //创建默认的httpClient实例
		map.put(ConstantHC.k_client,client);
		map.put(ConstantHC.k_username,"18051774753");
		map.put(ConstantHC.k_pass,"102593");
		map.put(ConstantHC.k_vcode,"yruy");
		map.put(ConstantHC.k_cookie,  "JSESSIONID=25D80D1AC6DC1EB10A8142F0CABAA840-an2; Hm_lvt_2fc39a7fd5c2fa8981bd3ccd9764c1d7=1403593484,1403597883,1403698065,1403706724; Hm_lpvt_2fc39a7fd5c2fa8981bd3ccd9764c1d7=1403709279; Hm_lvt_0dcb2e24b290e07390d62192b57b24bb=1403597883,1403676575,1403697824,1403698065; Hm_lpvt_0dcb2e24b290e07390d62192b57b24bb=1403709279; Hm_lvt_dde0eaa78e997cf66a7025b6ca8f3d6b=1403593485,1403597885,1403698066,1403706744; Hm_lpvt_dde0eaa78e997cf66a7025b6ca8f3d6b=1403709281; _qzja=1.271251857.1403709283188.1403709283188.1403709283189.1403709283188.1403709283189.0.0.0.1.1; _qzjb=1.1403709283188.1.0.0.0; _qzjc=1; _qzjto=1.1.0; Js_userId=undefined%3Bundefined; Js_isLogin=no; Js_cityId=undefined; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3D%3B; s_pers=%20s_fid%3D58FA05AC3DD438EF-033EE2FF1FBECB80%7C1466867700253%3B; _ga=GA1.2.716648490.1403709283; _jzqa=1.480524061146459100.1403709283.1403709283.1403709283.1; _jzqb=1.1.10.1403709283.1; _jzqc=1; _jzqckmp=1");
		Login.login1(map);
		
	}
	public static void test1(){
		Map<String,Object> map = new HashMap<String,Object>();
		DefaultHttpClient client = CUtil.init(); //创建默认的httpClient实例
		map.put(ConstantHC.k_client,client);
		map.put(ConstantHC.k_username,"18051774753");
		map.put(ConstantHC.k_pass,"102593");
		//第一步
		boolean b = Login.index(map);
		if(b){
			//返回验证码
			String code = Login.uploadVCode(map,"d://yanzhengma.png");
			map.put(ConstantHC.k_vcode,code);
			//开始登陆
//			String s = Login.login1(map);
			String s = Login.login_1(map);
			if(s!=null){
				 if(s.contains("验证码错误")){
					System.out.println("验证码错误");
				}else if(s.contains("true")){
					System.out.println("登陆成功");
					
					
					CHeader h = new CHeader(CHeaderUtil.Accept_json,"",CHeaderUtil.Content_Type__urlencoded,
							"js.189.cn",true);
					HttpResponse response = CUtil.getHttpGet("http://js.189.cn/getSessionInfo.action",h, client);
					
					String text = ParseResponse.parse(response,"utf-8");
					System.out.println("text================"+text);
				}
			}else{
				System.out.println("登陆失败");
			}
			
		}
		
		// System.out.println("江苏电信:"+CUtil.getCookie(client));
		
	}
	
	
		
}
