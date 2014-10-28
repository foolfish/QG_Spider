package com.lkb.test.fjdx;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;

import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.constant.ConstantHC;



public class Start {

	
	
	public static void main(String[] args) {
		test1();
	}
	public static void test1(){
		Map<String,Object> map = new HashMap<String,Object>();
		DefaultHttpClient client = CUtil.init(); //创建默认的httpClient实例
		map.put(ConstantHC.k_client,client);
		map.put(ConstantHC.k_username,"18005012942");
		map.put(ConstantHC.k_pass,"woshi123465");
		map.put("ip", "114.249.55.149");
		//第一步
		Login.shengfen_id(map);
		String text = Login.index(map);
		if(text!=null){
			//返回验证码
			String code = Login.uploadVCode(map,"d://yanzhengma.png");
			map.put(ConstantHC.k_vcode,code);
//			//开始登陆
			String s = Login.login(map,text);
//			if(s!=null){
//				 if(s.contains("验证码错误")){
//					System.out.println("验证码错误");
//				}else if(s.contains("location.href = \"/service/\"")){
//					System.out.println("登陆成功");
//				}
//				
//			}else{
//				System.out.println("登陆失败");
//			}
//			
		}
		
		// System.out.println("江苏电信:"+CUtil.getCookie(client));
		
	}
	
	
		
}
