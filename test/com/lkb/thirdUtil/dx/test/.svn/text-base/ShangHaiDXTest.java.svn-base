package com.lkb.thirdUtil.dx.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import com.lkb.bean.DianXinDetail;

import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.util.httpclient.CUtil;

public class ShangHaiDXTest {
	private String phoneNo = "18016252553";
	private String password = "026315";
	
	//name,balance,idcard,addresse,email
	private String[] personInfo = new String[]{"", "9", null, null, null};
	//ts,callNo,fee,duration,
	private String[] detail = new String[]{"2014-08-10,13569775,0.3,10", "2014-08-11,10000,0,30", null, null, null};
	
	String imgCode = null;
	Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
	ShangHaiDianXin dx = new ShangHaiDianXin(spider, null, phoneNo, password, "2345", null);
	public void initData(){
		
	}
	
	public void execute() {
		boolean isTestSms = false;
		
		dx.setTest(true);
		//Cookie	JSESSIONID=0000c1AdIjuPI6-dmKSZ_MUxYZb:157a35oke; chwlaz=1408446512797; chweblog_contents=1408446512811; chweblog_event=1408446512820; NSC_xu-222.68.185.229=ffffffffc3a01f1745525d5f4f58455e445a4a423660; userId=201|151669205; cityCode=sh; citrix_ns_id_.189.cn_%2F_wlf=TlNDX3h1LTIyMi42OC4xODUuMjI5?SFyPH7LPjeXzuvkfet/gspvs/akA&; _gscu_1708861450=08414613xm7uze18; s_pers=%20s_fid%3D313057A2881BEAB7-116456AD7C614BDE%7C1471680803210%3B; svid=57d164063536cf356b8e297ca4c33c07; SHOPID_COOKIEID=10003; isLogin=logined; _gscs_1708861450=085223651kvkvz70|pv:3; .ybtj.189.cn=590566E9B59ACF4CAED1A5D93A77C015; citrix_ns_id=+QoNCIMQqCGP4PhIFl9hF7QpHCIA000; _gscbrs_1708861450=1; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-sh%253D%252526pid%25253D%2525252Fsh%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fwww.189.cn%2525252Fdqmh%2525252FfrontLinkSkip.do%2525253Fmethod%2525253Dskip%25252526shopId%2525253D10003%25252526toStUrl%2525253Dhttp%2525253A%2525252F%2525252Fsh.189.cn%2525252Fservice%2525252Fac%252526ot%25253DA%252526oi%25253D309%3B; loginStatus=logined; s_cc=true; citrix_ns_id_.189.cn_%2F_wat=SlNFU1NJT05JRF9f?JkHZ1m+JPBYu0+RbkzIllXZM/DAA#qbg2R/Rp9UAoyjTa8a72ZkzKpoIA&
		if (!isTestSms) {
			dx.checkVerifyCode(phoneNo);
			spider.start();
			dx.printData();
			imgCode = CUtil.inputYanzhengma();
			dx.setAuthCode(imgCode);
			dx.goLoginReq();
			spider.start();
		} else {
			DebugUtil.addToCookieStore("sh.189.cn", "JSESSIONID=0000o2YEkDyP_3LYnuSFB8gAKL0:14hp09pa5; chwlaz=1408446512797; chweblog_contents=1408446512811; chweblog_event=1408446512820; NSC_xu-222.68.185.229=ffffffffc3a01f1845525d5f4f58455e445a4a423660; userId=201|151669205; cityCode=sh; citrix_ns_id_.189.cn_%2F_wlf=TlNDX3h1LTIyMi42OC4xODUuMjI5?sBn7JRm5ImeTI832903604H90UcA&; _gscu_1708861450=08414613xm7uze18; s_pers=%20s_fid%3D313057A2881BEAB7-116456AD7C614BDE%7C1471680803210%3B; svid=57d164063536cf356b8e297ca4c33c07; SHOPID_COOKIEID=10003; _gscs_1708861450=085223651kvkvz70|pv:6; .ybtj.189.cn=590566E9B59ACF4CAED1A5D93A77C015; citrix_ns_id=+QoNCIMQqCGP4PhIFl9hF7QpHCIA000; _gscbrs_1708861450=1; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-sh%253D%252526pid%25253D%2525252Fsh%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fwww.189.cn%2525252Fdqmh%2525252FfrontLinkSkip.do%2525253Fmethod%2525253Dskip%25252526shopId%2525253D10003%25252526toStUrl%2525253Dhttp%2525253A%2525252F%2525252Fsh.189.cn%2525252Fservice%2525252Fac%252526ot%25253DA%252526oi%25253D309%3B; loginStatus=logined; s_cc=true; citrix_ns_id_.189.cn_%2F_wat=SlNFU1NJT05JRF9f?PlEAlTjZQYCQA//QMXZjecARgqoA#qbg2R/Rp9UAoyjTa8a72ZkzKpoIA&");
		}
		if (isTestSms) {
			//dx.parseBalanceInfo();
		}
		//dx.getUser() 个人信息
		dx.getDetailList(); //详单
		//dx.getTelList(); //帐单
		
		if (isTestSms || dx.isSuccess()) {
			dx.getData().clear();
			dx.showImgWhenSendSMS(phoneNo);
			spider.start();			
			dx.printData();
			imgCode = CUtil.inputYanzhengma();
			dx.sendSmsPasswordForRequireCallLogService(imgCode);
			spider.start();		
			if (dx.isSuccess()) {
				String smsCode = CUtil.inputYanzhengma();
				dx.requestAllService(smsCode, imgCode);
				spider.start();
			}
		}
		dx.printData();		
		//u=dx.getUser();
		
		setUser1();
		//ass
		System.out.println(0);
		//assertEquals("姓名出错",u1.getRealName(),u.getRealName());
		//assertTrue("余额出错", u.getPhoneRemain().equals(new BigDecimal("9")));
		//assert();
		System.out.println(100);
		//详单比较
		int i=0;
		System.out.println("getDetailList个数"+dx.getDetailList().size());
		System.out.println("getTelList个数"+dx.getTelList().size());
		for(DianXinDetail de: dx.getDetailList()){
			System.out.println(i++);
			System.out.println(de.getcTime().toLocaleString());
		}
	}
	public void setUser1(){
		//u1.setRealName("张根硕");
		//u1.setPhoneRemain(new BigDecimal("0.3"));
		
	}
	   

}
