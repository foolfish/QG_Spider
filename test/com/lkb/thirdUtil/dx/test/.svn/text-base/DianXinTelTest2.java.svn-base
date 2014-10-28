package com.lkb.thirdUtil.dx.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.util.DateUtils;

public class DianXinTelTest2 {
	
	protected String phoneNo = null;
	
	private String password = null;
	protected String imgCode = null;
	//private Spider spider = null;
	/*protected User u=new User();
	protected Collection<DianXinTel> telList=new ArrayList(); 
	protected Collection<DianXinDetail> detailList=new ArrayList();*/
	
	protected String[] personInfo=null;
	protected String[] detailInfo=null;
	protected String[] telListInfo=null;
	protected Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
	@Rule
	public ErrorCollector collector= new ErrorCollector();
	
	//初始化电话和密码
		public void setNoandPwd(String phoneNo,String password){
			this.phoneNo=phoneNo;
			this.password=password;
		}
		
		
		public void comp(User u,Collection<DianXinTel> telList,Collection<DianXinDetail> detailList){
			//比较客户信息
			compUserInfo(personInfo,u);
			//帐单比较
			if(telListInfo!=null && telListInfo.length>0){
				compTellList(telListInfo,telList);
			}
			//详单比较
			if(detailInfo!=null && detailInfo.length>0){
				compDetailList(detailInfo,detailList);
			}
		}
		public void compMessage(String[] mesInfo,Collection<TelcomMessage> mesList){
			TelcomMessage me1=null;
			int j=mesInfo.length;
			String[] s=null;
			for(int i=0;i<j;i++){
				s=mesInfo[i].split(",");
				for(TelcomMessage tm: mesList){
					String st = DateUtils.formatDate(tm.getSentTime(), "yyyy/MM/dd-HH:mm");
					if(st.equals(s[0])){
						me1=tm;
						break;
					}
				}
				try{
					assertNotNull("短信时间为"+s[0]+"的记录没有查询到",me1);
					
				}
				catch(Throwable e){
					collector.addError(e);
					break;
				}
				if(!StringUtils.isBlank(s[1])){
					collector.checkThat("对方号码有误", new IsEqual<String>(s[1]).equalTo(me1.getRecevierPhone()));
				}
				if(!StringUtils.isBlank(s[2])){
					collector.checkThat("短信费用有误", new IsEqual<String>(s[2]).equalTo(transBd(new BigDecimal(me1.getAllPay()))));
				}
				if(!StringUtils.isBlank(s[3])){
					collector.checkThat("手机号有误", new IsEqual<String>(s[3]).equalTo(me1.getPhone()));
				}
			}
		}
		//比较客户信息
		//@Test
		public void compUserInfo(String[] personInfo,User u){
			System.out.println(u.getUserName());
			System.out.println(2);
			
			if(!StringUtils.isBlank(personInfo[0])){
				 //collector.checkThat(personInfo[0], u.getRealName());
				collector.checkThat("姓名出错", new IsEqual<String>(personInfo[0]).equalTo(u.getRealName()));
				/*try{
					assertEquals("姓名出错",personInfo[0],u.getRealName());
				}
				catch(Throwable e){
					collector.addError(e);
				}*/
				
			}
			if(!StringUtils.isBlank(personInfo[1])){
				collector.checkThat("余额出错", new IsEqual<String>(transBd(new BigDecimal(personInfo[1]))).equalTo(transBd(u.getPhoneRemain())));
				/*try{
					assertEquals("余额出错",transBd(new BigDecimal(personInfo[1])),transBd(u.getPhoneRemain()));
				}
				catch(Throwable e){
					collector.addError(e);
				}*/
			}
			if(!StringUtils.isBlank(personInfo[2])){
				collector.checkThat("身份证号出错", new IsEqual<String>(personInfo[2]).equalTo(u.getIdcard()));
				/*try{
					assertTrue("身份证号出错", u.getIdcard().equals(personInfo[2]));
				}
				catch(Throwable e){
					collector.addError(e);
				}*/
			}
			if(!StringUtils.isBlank(personInfo[3])){
				collector.checkThat("地址出错", new IsEqual<String>(personInfo[3]).equalTo(u.getAddr()));
				/*try{
					assertTrue("地址出错", u.getAddr().equals(personInfo[3]));
				}
				catch(Throwable e){
					collector.addError(e);
				}*/
				
			}
			if(!StringUtils.isBlank(personInfo[4])){
				collector.checkThat("邮箱出错", new IsEqual<String>(personInfo[4]).equalTo(u.getEmail()));
				/*try{
					assertTrue("邮箱出错", u.getEmail().equals(personInfo[4]));
				}
				catch(Throwable e){
					collector.addError(e);
				}	*/
			}
		}
		//比较账单信息
		//@Test
		public void compTellList(String[] telListInfo,Collection<DianXinTel> telList){
			
			DianXinTel te1=null;
			int j=telListInfo.length;
			String[] s=null;
			for(int i=0;i<j;i++){
				s=telListInfo[i].split(",");
				for(DianXinTel te: telList){
					String dstr = DateUtils.formatDate(te.getcTime(), "yyyy/MM");
					if(dstr.equals(s[0])){
						te1=te;
						break;
					}
				
				}
				try{
					assertNotNull("月份为"+s[0]+"的记录没有查询到",te1);
					
				}
				catch(Throwable e){
					collector.addError(e);
					break;
				}
				if(!StringUtils.isBlank(s[1])){
					collector.checkThat("品牌有误", new IsEqual<String>(s[1]).equalTo(te1.getBrand()));
					/*try{
						assertEquals("品牌有误",s[1],te1.getBrand());
					}
					catch(Throwable e){
						collector.addError(e);
					}	*/
				}
				//ztcjbf; //主套餐基本费 bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
			
				if(!StringUtils.isBlank(s[2])){
					collector.checkThat("主套餐费用有误", new IsEqual<String>(transBd(new BigDecimal(s[2]))).equalTo(transBd(te1.getZtcjbf())));
					/*try{
						assertEquals("主套餐费用有误",transBd(new BigDecimal(s[2])),transBd(te1.getZtcjbf()));
					}
					catch(Throwable e){
						collector.addError(e);
					}	*/
				}
				if(!StringUtils.isBlank(s[3])){
					collector.checkThat("本地通话费有误", new IsEqual<String>(transBd(new BigDecimal(s[3]))).equalTo(transBd(te1.getBdthf())));
					/*try{
						assertEquals("本地通话费",transBd(new BigDecimal(s[3])),transBd(te1.getBdthf()));
					}
					catch(Throwable e){
						collector.addError(e);
					}	*/	
				}
			
				if(!StringUtils.isBlank(s[4])){
					collector.checkThat("来电显示费有误", new IsEqual<String>(transBd(new BigDecimal(s[4]))).equalTo(transBd(te1.getLdxsf())));
					/*try{
						assertEquals("来电显示费",transBd(new BigDecimal(s[4])),transBd(te1.getLdxsf()));
					}
					catch(Throwable e){
						collector.addError(e);
					}*/
				}
				if(!StringUtils.isBlank(s[5])){
					collector.checkThat("漫游通话有误", new IsEqual<String>(transBd(new BigDecimal(s[5]))).equalTo(transBd(te1.getMythf())));
					/*try{
						assertEquals("漫游通话费",transBd(new BigDecimal(s[5])),transBd(te1.getMythf()));
					}
					catch(Throwable e){
						collector.addError(e);
					}*/	
				}
			}
			
		}
		//比较详单信息
		//@Test
		public void compDetailList(String[] detailInfo,Collection<DianXinDetail> detailList){
			//detailList=dx.getDetailList();
			DianXinDetail de1=null;
			int j=detailInfo.length;
			String[] s=null;
			for(int i=0;i<j;i++){
				s=detailInfo[i].split(",");
				for(DianXinDetail de: detailList){
					String dstr = DateUtils.formatDate(de.getcTime(), "yyyy/MM/dd-HH:mm");
					if(dstr.equals(s[0])){
						de1=de;
						break;
					}
				
				}
					try{
						assertNotNull("拨出时间为"+s[0]+"的通话记录没有查询到",de1);
					}
					catch(Throwable e){
						collector.addError(e);
						break;
					}
				/*if(de1==null){
					throw new RuntimeException("");
				}*/
				if(!StringUtils.isBlank(s[1])){
					collector.checkThat("呼叫号码错误", new IsEqual<String>(transBd(new BigDecimal(s[1]))).equalTo(de1.getRecevierPhone()));
					/*try{
						assertTrue("呼叫号码错误", de1.getRecevierPhone().equals(s[1]));
					}
					catch(Throwable e){
						collector.addError(e);
					}*/		
				}
				if(!StringUtils.isBlank(s[2])){
					try{
						assertEquals("通话费用有误", transBd(de1.getAllPay()),transBd(new BigDecimal(s[2])));
					}
					catch(Throwable e){
						collector.addError(e);
					}	
					
				}
				if(!StringUtils.isBlank(s[3])){
					try{
						assertTrue("通话时长有误", new Integer(de1.getTradeTime()).toString().equals(s[3]));
					}
					catch(Throwable e){
						collector.addError(e);
					}	
					
				}
			}
			
		}
	        //把BigDecimal型数转化成两位小数点的字符串
		public String transBd(BigDecimal bd){
			DecimalFormat   df   =new DecimalFormat("0.00"); 
	   		return df.format(bd);
		}
}

