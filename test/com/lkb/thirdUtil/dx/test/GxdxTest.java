package com.lkb.thirdUtil.dx.test;

import java.util.Map;


import org.junit.Test;


import com.lkb.bean.client.Login;
import com.lkb.thirdUtil.dx.GXDianxin;

public class GxdxTest {
	@Test
	public void test(){
	//正确
	Login login = new Login("18934700051","270248");
	//Login login = new Login("18934700054","270248");
	System.out.println(1);
	GXDianxin hn = new GXDianxin(login);
	System.out.println(2);
//	if(!hn.islogin()){
//	//初始化
	hn.index();
	System.out.println(3);
//	//登陆
	Map<String,Object> map = hn.login();
	System.out.println(4);
		//hn.getPhoneDynamicsCode();//获取动态密码
////		hn.checkPhoneDynamicsCode();//校验动态密码
////		hn.callHistory();//通话记录
//		
//	}
//	login.setPhoneCode("830521");
//	GXDianxin hn = new GXDianxin(login);
//	if(hn.islogin()){
////		hn.getPhoneDynamicsCode();//获取动态密码
////		hn.checkPhoneDynamicsCode();//校验动态密码
//		hn.callHistory();//通话记录
//	hn.getTelDetailHtml();
//	}
//	hn.close();
	}
}
