package com.lkb.thirdUtil.dx.test;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.util.httpclient.CUtil;
public class SHDXT extends DianXinForSpiderTest{
	
		
	ShangHaiDianXin dx=new ShangHaiDianXin(spider, null, "18016252553", "026315", "2345", null);
	
	//GanSuDianXin dx=new GanSuDianXin(spider, null, "18016252553", "026315", "2345", null);
		
		
		/*public void setBefore(){
			
			 String[] personInfo = new String[]{"张根硕", "0.3", null, null, null};
			
			//String[] detailInfo = new String[]{"2014/08/10-12:25,13569775,0.3,10", "2014/08/11-08:09,10000,0,30"};
			 String[] detailInfo = new String[]{null};
			
		
			//账单信息cTime,brand,ztcjbf; //主套餐基本费 bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
			 String[] telListInfo=new String[]{null};
			 
	
			initializeInfo(personInfo, detailInfo,telListInfo);
		}*/
	public void initial(){
		setNoandPwd("18016252553", "026315");
		personInfo = new String[]{"张根硕", "0.38", null, null, null};
		
		//String[] detailInfo = new String[]{"2014/08/10-12:25,13569775,0.3,10", "2014/08/11-08:09,10000,0,30"};
		 detailInfo = new String[]{"2014/08/10-12:25,13123456789,0.3,10"};
		
	
		telListInfo=new String[]{null};
		
	}
		@Before
		public void Before() {
			initial();
			//setNoandPwd("18016252553", "026315");
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
			User u=dx.getUser();
			Collection telList=dx.getTelList();
			Collection detailList=dx.getDetailList();
			
			/*//comp(personInfo,telListInfo,detailInfo,dx.getUser(),dx.getTelList(),dx.getDetailList());	
			//比较客户信息
			compUserInfo(personInfo,u);
			//帐单比较
			if(telListInfo!=null && telListInfo.length>0){
				compTellList(telListInfo,telList);
			}
			//详单比较
			if(detailInfo!=null && detailInfo.length>0){
				compDetailList(detailInfo,detailList);
			}*/
			/*u=dx.getUser();
			telList=dx.getTelList();
			detailList=dx.getDetailList();	*/
			
		}
		//@Test
		//比较客户信息
		public void test1(){
			//initial();
			if(personInfo!=null && personInfo.length>0){
				compUserInfo(dx.getUser());
				//compUserInfo(personInfo,u)
			}
		}
		//@Test
		public void test2(){
			//initial();
			if(detailInfo!=null && detailInfo.length>0){
				super.compDetailList(dx.getDetailList());
			}
		}
		@Test
		public void test3(){
			//initial();
			if(telListInfo!=null && telListInfo.length>0){
				System.out.println(telListInfo.length+"telListInfo.length");
				compTellList(dx.getTelList());
			}
		}
		@Override
		protected void initData() {
			// TODO Auto-generated method stub
			
		}
		@Override
		protected void execute() {
			// TODO Auto-generated method stub
			
		}
}

