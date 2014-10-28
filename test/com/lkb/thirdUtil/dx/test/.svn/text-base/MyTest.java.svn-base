package com.lkb.thirdUtil.dx.test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.AssertionFailedError;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import com.lkb.bean.User;
import com.lkb.thirdUtil.dx.AbstractDianXinCrawler;
public  class  MyTest {
	//http://openapi.quantgroup.cn/LKBClient/openapi/phone/pt_ued_phone_img.json?appId=0001&timeunit=123123123123&token=b7dae5053b85e177f74eaa2914640a0a&userId=0001_01111&phone=18051774753&index=1
		String url1="http://210.73.218.136:8082/LKBClient/openapi/phone/pt_ued_phone_img.json?appId=0002&timeunit=123123123123&token=ec62bf0b1354cdfa8b8423563de9caa8&userId=0002_01116&phone=&index=1";
		String url2="http://210.73.218.136:8082/LKBClient/openapi/phone/pt_ued_phone_login.json?appId=0002&timeunit=123123123123&token=ec62bf0b1354cdfa8b8423563de9caa8&userId=0002_01116&phone=&fwpassword=&authCode=";
		String url4="http://210.73.218.136:8082/LKBClient/openapi/phone/pt_ued_phone_sms.json?appId=0002&timeunit=123123123123&token=ec62bf0b1354cdfa8b8423563de9caa8&userId=0002_01116&phone=&authCode=";

		String url5="http://210.73.218.136:8082/LKBClient/openapi/phone/pt_ued_phone_service.json?appId=0002&timeunit=123123123123&token=ec62bf0b1354cdfa8b8423563de9caa8&userId=0002_01116&phone=&authCode=&smsCode=";
		/*String authCode1="";
		String smsCode1="689363";*/
		String authCode1="9943";
		String smsCode1="910823";//江西移动，即将第四步
	
		//重庆  四川  黑龙江  陕西 辽宁
		String uniphones[]={"18581381327","18502884929","15504642968","18591791531","13125493550"};
		String dianphones[]={"13308370860","15390037740","13351266898","18161910153","18040212031"};
		String mobilephones[]={"18290425516","18782418840","18345067130","14791405282","15042433089"};
		String unipwd[]={"951925","888666","112233","123321","462742"};
		String dianpwd[]={"325360","215363","014814","462742","880717"};
		String mobpwd[]={"075720","381327","920115","686868","595499"};
		/*String phone1=uniphones[1];
		String password1=unipwd[1];*/
		/*String phone1=dianphones[3];
		String password1=dianpwd[3];*/
		/*String phone1=mobilephones[1];
		String password1=mobpwd[1];*/
			
	

		//安徽  云南  宁夏  吉林  山西
				String uniphones30[]={"15695517910","13097487367","13209607796","13244263097","13134633106"};
				String dianphones30[]={"18134537231","15398469671","18095107544","13341578170","18135159373"};
				String mobilephones30[]={"18326020113","18387122194","15009579295","18743075319","15735353850"};
				String unipwd30[]={"154523","045352","497593","123321","880717"};
				String dianpwd30[]={"062888","231102","602998","669459","034987"};
				String mobpwd30[]={"284279","100434","809713","346911","116880"};
				/*String phone1=mobilephones30[2];
				String password1=mobpwd30[2];*/
				/*String phone1=uniphones30[5];
				String password1=unipwd30[5];*/
				/*String phone1=dianphones30[4];
				String password1=dianpwd30[4];*/
				/*String phone1=mobilephones30[3];
				String password1=mobpwd30[3];*/
		//青海  西藏 内蒙古   贵州 广西 海南 贵州电信没有
		String uniphones3[]={"15597108906","15692605762","15540164740","15519568312","15678190725","13006003404"};
		String dianphones3[]={"18097445987","18108910674","13314863621","18198347389","18934700051","18189814687"};
		String mobilephones3[]={"13997436394","18308008648","13754023610","18702401460","15240682175","13687595621"};
		String unipwd3[]={"971089","926057","899675","440835","053262","681282"};
		String dianpwd3[]={"765422","317224","145306","888666","270248","418840"};
		String mobpwd3[]={"809713","030259","809713","809713","201308","418840"};
		/*String phone1=mobilephones3[2];
		String password1=mobpwd3[2];*/
		/*String phone1=uniphones3[1];
		String password1=unipwd3[1];*/
		String phone1=dianphones3[5];
		String password1=dianpwd3[5];
		/*String phone1=mobilephones3[3];
		String password1=mobpwd3[3];*/
		//西藏移动
				/*String phone1="18308008648";
				//String phone1="13466665458";//错误号码
				String password1="030259";
		//陕西电信
		String phone1="18161910153";
		//String phone1="13466665458";//错误号码
		String password1="462742";*/
		//String password1="198341";//错误密码
		/*//四川联通
		String phone1="18502884929";
		//String phone1="13466665458";//错误号码
		String password1="888666";
		//String password1="198341";//错误密码
*/
		//江西联通
		/*String phone1="13133609157";
		//String phone1="13466665458";//错误号码
		String password1="693712";*/
		//String password1="198341";//错误密码
		//江西移动
		/*String phone1="13627079996";
		//String phone1="13466665458";//错误号码
		String password1="822666";*/
		//江西电信
		/*String phone1="18160710531";
		//String phone1="18160710531";//错误号码
		String password1="568060";*/
		//String password1="568060";
		
		//新疆联通
		/*String phone1="15599816279";
		//String phone1="15599816279";//正确号码
		String password1="123321";*/
		//String password1="123321";
		//新疆电信
		/*String phone1="18199272917";
		//String phone1="13466665458";//错误号码
		String password1="474802";*/
		//新疆移动
		/*String phone1="15894722376";
		//String phone1="13466665458";//错误号码
		String password1="636040";*/
		
		//河南电信
		/*String phone1="15378784068";
		//String phone1="13466665458";//错误号码
		String password1="199278";*/
		//河南移动
		/*String phone1="15981945805";
		//String phone1="13466665458";//错误号码
		String password1="899999";*/
		//河南联通
		/*String phone1="15617681495";
		//String phone1="13466665458";//错误号码
		String password1="147258";*/
		
		
		//甘肃电信
		/*String phone1="18119381302";
		//String phone1="13466665458";//错误号码
		String password1="199034";*/
		//甘肃移动
		/*String phone1="15101254051";
		//String phone1="13466665458";//错误号码
		String password1="809713";*/
		//甘肃联通
		/*String phone1="13028710505";
		//String phone1="13466665458";//错误号码
		String password1="972352";*/
		
		//湖南电信
		/*String phone1="18142631055";
		//String phone1="13466665458";//错误号码
		String password1="739586";*/
		//湖南移动
		//String phone1="18373914055";
		/*String phone1="18373914055";//错误号码
		String password1="168168";*/
		//湖南联通
		/*String phone1="13272053039";
		//String phone1="13272053038";//错误号码
		String password1="496091";*/

		/*//河北电信
		String phone1="18033723291";
		//String phone1="13466665458";//错误号码
		String password1="199034";*/
		

		
		@Test
		public void test1(){
			accessURL(url1,phone1,null,null,null);
		}
		@Test
		public void test2(){
			accessURL(url2,phone1,password1,authCode1,null);
		}
		@Test
		public void test4(){
			accessURL(url4,phone1,password1,authCode1,null);
		}
		@Test
		public void test5(){
			accessURL(url5,phone1,password1,authCode1,smsCode1);
		}
		//访问url
		public void accessURL(String url,String phone,String password,String authCode,String smsCode){
			
			/*int phoneBeg=url.indexOf("phone");
			int phoneNo=phoneBeg+6;*/
			url=url.replace("phone=", "phone="+phone);
			if(url.indexOf("fwpassword=")>=0 && !StringUtils.isBlank(password)){
				url=url.replace("fwpassword=", "fwpassword="+password);
			}
			if(url.indexOf("authCode=")>=0 && !StringUtils.isBlank(authCode)){
				url=url.replace("authCode=", "authCode="+authCode);
			}
			if(url.indexOf("smsCode=")>=0 && !StringUtils.isBlank(smsCode)){
				url=url.replace("smsCode=", "smsCode="+smsCode);
			}
			URL urlR=null;
			
			try {  
				urlR = new URL(url);
			} catch (MalformedURLException e) {  
			    e.printStackTrace();  
			}  
			BufferedReader r=null;  
			try {  
			    URLConnection con=urlR.openConnection();  
			    r = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));  
			} catch (UnsupportedEncodingException e1) {  
			    e1.printStackTrace();  
			} catch (IOException e1) {  
			    e1.printStackTrace();  
			}  
			 StringBuffer sb=new StringBuffer(); 
			String str="";  
			do{  
			    try {  
			        str=r.readLine();  
			    } catch (IOException e) {  
			        e.printStackTrace();  
			    }  
			            //System.out.println(str);  
			    sb.append(str);  
			}while(str!=null);  
			  
			try {  
			    r.close();  
			} catch (IOException e) {  
			    e.printStackTrace();  
			}  
			System.out.println(sb.toString());
		/*	String stri=sb.toString();
			int indSta=stri.indexOf("errMsg");*/
			
		}
		
}
