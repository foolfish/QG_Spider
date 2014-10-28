package com.lkb.thirdUtil.dx.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;

import org.junit.Test;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.util.DateUtils;
import com.lkb.util.httpclient.CUtil;

public class TelTest {
	
	private String phoneNo = null;
	private String password = null;
	private String imgCode = null;
	private Spider spider = null;
	private ShangHaiDianXin dx = null;
	private Collection detailList=null; 
	private Collection telList=null; 
	private String[] personInfo=null;
	private String[] detailInfo=null;
	private String[] telListInfo=null;

	//信息初始化
	public void initialize(){
		//u=new User();
		phoneNo = "18016252553";
		password = "026315";
		imgCode = null;
		spider = SpiderManager.getInstance().createSpider("test", "aaa");
		 dx = new ShangHaiDianXin(spider, null, phoneNo, password, "2345", null);
		 
		 
		//name,balance,idcard,addresse,email
		 personInfo = new String[]{"", "9", null, null, null};
		//ts(yyyy/MM/dd-HH:mm）,RecevierPhone,fee,duratio
		detailInfo = new String[]{"2014/08/10-12:25,13569775,0.3,10", "2014/08/11-08:09,10000,0,30"};
		
		//账单信息cTime,brand,ztcjbf; //主套餐基本费 bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
		telListInfo=new String[]{"2014/07，上海电信，10，5，1，3",};
	}
	
	@Test
	public void test() {
		initialize();
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
		//dx.getDetailList(); //详单
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
		//比较客户信息
		//compUserInfo(u);
		//帐单比较
		if(telListInfo!=null && telListInfo.length>0){
			compTellList(telListInfo);
		}
		//详单比较
		/*if(detail!=null && detail.length>0){
			compDetailList(detailInfo);
		}*/
		
	}
	
	//比较客户信息，姓名和余额,身份证号和地址
	//@Test
	public void compUserInfo(User u){
		if(personInfo[0]!=null && personInfo[0].length()>0){
			assertEquals("姓名出错",u.getRealName(),personInfo[0]);
		}
		if(personInfo[1]!=null && personInfo[1].length()>0){
			//assertTrue("余额出错",transBd(u.getPhoneRemain()),transBd(new BigDecimal(personInfo[1])));
		}
		if(personInfo[2]!=null && personInfo[2].length()>0){
			assertTrue("身份证号出错", u.getIdcard().equals(personInfo[2]));
		}
		if(personInfo[3]!=null && personInfo[3].length()>0){
			assertTrue("地址出错", u.getAddr().equals(personInfo[3]));
		}
		if(personInfo[4]!=null && personInfo[4].length()>0){
			assertTrue("邮箱出错", u.getEmail().equals(personInfo[4]));
		}
	}

	//比较账单信息
	public void compTellList(String[] telListInfo){
		telList=dx.getTelList();
		DianXinTel te1=null;
		int j=telListInfo.length;
		String[] s=null;
		for(int i=0;i<j;i++){
			s=telListInfo[i].split(",");
			/*for(DianXinTel te: telList){
				String dstr = DateUtils.formatDate(de.getcTime(), "yyyy/MM");
				if(dstr.equals(s[0])){
					te1=te;
					break;
				}
			
			}
			if(te1==null){
				throw new RuntimeException("月份为"+s[0]+"的记录没有查询到");
			}*/
			if(s[1]!=null && s[1].length()>0){
				//assertEquals("品牌有误", transBd(te1.getBrand()),s[1]);
			}
			//ztcjbf; //主套餐基本费 bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
		
			if(s[2]!=null && s[2].length()>0){
				assertEquals("主套餐费用有误", transBd(te1.getZtcjbf()),transBd(new BigDecimal(s[2])));
			}
			if(s[3]!=null && s[3].length()>0){
				assertEquals("本地通话费", transBd(te1.getBdthf()),transBd(new BigDecimal(s[3])));
			}
		
			if(s[4]!=null && s[4].length()>0){
				assertEquals("来电显示费", transBd(te1.getLdxsf()),transBd(new BigDecimal(s[4])));
			}
			if(s[5]!=null && s[5].length()>0){
				assertEquals("漫游通话费", transBd(te1.getMythf()),transBd(new BigDecimal(s[5])));
			}
		}
		
	}
	//比较详单信息
	public void compDetailList(String[] detailInfo){
		detailList=dx.getDetailList();
		DianXinDetail de1=null;
		int j=detailInfo.length;
		String[] s=null;
		for(int i=0;i<j;i++){
			s=detailInfo[i].split(",");
			for(DianXinDetail de: dx.getDetailList()){
				String dstr = DateUtils.formatDate(de.getcTime(), "yyyy/MM/dd-HH:mm");
				if(dstr.equals(s[0])){
					de1=de;
					break;
				}
			
			}
			if(de1==null){
				throw new RuntimeException("拨出时间为"+s[0]+"的通话记录没有查询到");
			}
			if(s[1]!=null && s[1].length()>0){
				assertTrue("呼叫号码错误", de1.getRecevierPhone().equals(s[1]));
			}
			if(s[2]!=null && s[2].length()>0){
				assertEquals("通话费用有误", transBd(de1.getAllPay()),transBd(new BigDecimal(s[2])));
			}
			if(s[3]!=null && s[3].length()>0){
				assertTrue("通话时长有误", new Integer(de1.getTradeTime()).toString().equals(s[3]));
			}
		}
		
	}
        //把BigDecimal型数转化成两位小数点的字符串
	public String transBd(BigDecimal bd){
		DecimalFormat   df   =new DecimalFormat("0.00"); 
   		return df.format(bd);
	}
	
	

}
