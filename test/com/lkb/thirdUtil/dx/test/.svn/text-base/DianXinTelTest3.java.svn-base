package com.lkb.thirdUtil.dx.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.User;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.util.DateUtils;

public class DianXinTelTest3 {
	
	protected String phoneNo = null;
	private User u=null;
	private String password = null;
	protected String imgCode = null;
	//private Spider spider = null;
	private Collection detailList=null; 
	private Collection telList=null; 
	//private String[] personInfo=null;
	//private String[] detailInfo=null;
	//private String[] telListInfo=null;
	protected Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
	
	//初始化电话和密码
		public void setNoandPwd(String phoneNo,String password){
			this.phoneNo=phoneNo;
			this.password=password;
		}
		
		/*//运营商信息初始化(包括电话号和密码)
		public void setDxandNoandPwd(String phoneNo,String password){
			setNoandPwd(phoneNo,password);
			dx=new ShangHaiDianXin(spider, null, phoneNo, password, "2345", null);
		}*/
		//待验证信息初始化
	/*	public void initializeInfo(String[] personInfo,String[] detailInfo,String[] telListInfo){
			 
			
			this.personInfo=personInfo;
			this.detailInfo=detailInfo;
			this.telListInfo=telListInfo;
		}*/
		public void comp(String[] personInfo,String[] telListInfo,String[] detailInfo,User u,Collection<DianXinTel> telList,Collection<DianXinDetail> detailList){
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
		//比较客户信息
		public void compUserInfo(String[] personInfo,User u){
			
			if(personInfo[0]!=null && personInfo[0].length()>0){
			
				assertEquals("姓名出错",u.getRealName(),personInfo[0]);
				
			}
			if(personInfo[1]!=null && personInfo[1].length()>0){
				assertEquals("余额出错",transBd(u.getPhoneRemain()),transBd(new BigDecimal(personInfo[1])));
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
				if(te1==null){
					throw new RuntimeException("月份为"+s[0]+"的记录没有查询到");
				}
				if(s[1]!=null && s[1].length()>0){
					assertEquals("品牌有误", transBd(new BigDecimal(te1.getBrand())),s[1]);
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
