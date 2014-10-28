/**
 * 
 */
package com.lkb.thirdUtil.dx.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.core.IsEqual;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.thirdUtil.dx.test.testUtil.DetailListComparator;
import com.lkb.thirdUtil.dx.test.testUtil.MessageComparator;
import com.lkb.thirdUtil.dx.test.testUtil.SeekUtil;
import com.lkb.thirdUtil.dx.test.testUtil.TelListComparator;
import com.lkb.thirdUtil.telcom.test.TelcomTest;
import com.lkb.util.DateUtils;

/**
 * @author think
 * @date 2014-9-2
 */
public abstract class DianXinTest extends TelcomTest {
	
	//初始化电话和密码
		public void setNoandPwd(String phoneNo,String password){
			this.phoneNo=phoneNo;
			this.password=password;
		}
		
		public void assuranceData(User u, Collection<DianXinTel> telList,Collection<DianXinDetail> detailList,Collection<TelcomMessage> mesList){
			//比较客户信息
			compUserInfo(u);
			//比较短信信息
			compMessage(mesList);
			//帐单比较
			if(telListInfo!=null && telListInfo.length>0){
				compTellList(telList);
			}
			//详单比较
			if(detailInfo!=null && detailInfo.length>0){
				compDetailList(detailList);
			}
		}
		public void compMessage(Collection<TelcomMessage> mesList){
			if (mesInfo == null ) {
				return;
			}
			TelcomMessage me1=new TelcomMessage();
			int j=mesInfo.length;
			int k=mesList.size();
			
			try{
				//System.out.println("k="+k);
				assertTrue("短信记录没有查到",k!=0);
				
			}
			catch(Throwable e){
				collector.addError(e);
				return;
			}
			TelcomMessage[] mesArr=SeekUtil.toTelArray(mesList);
			
			SeekUtil.sort(mesArr, new MessageComparator());
			
			String[] s=null;
			for(int i=0;i<j;i++){
				s=mesInfo[i].split(",");
				
				System.out.println("mes i="+i);
				
				Date d=DateUtils.StringToDate(s[0], detailFormat);
				me1.setSentTime(d);
				int h=SeekUtil.binarySearch(mesArr,me1, new MessageComparator());
				System.out.println("me1.d"+me1.getSentTime().toLocaleString()+"h="+h);
				
				Date d1=DateUtils.StringToDate("2014-08-15 12:25:20", detailFormat);
				me1.setSentTime(d1);
				int h1=SeekUtil.binarySearch(mesArr,me1, new MessageComparator());
				
				
				System.out.println("me2.d"+me1.getSentTime().toLocaleString()+"h1="+h1);
				//System.out.println("短信"+h);
				try{
					//assertNotNull("短信时间为"+s+"的记录没有查询到",me1);k=-1时
					//System.out.println("短信时间"+me1.getSentTime().toLocaleString());
					assertTrue("短信时间为"+s[0]+"的记录没有查询到",h>=0);
					//collector.checkThat(me1, IsNull.nullValue());	
				}
				catch(Throwable e){
					collector.addError(e);
					
					continue;
				}
				int len = s.length;
				System.out.println("h="+h);
				TelcomMessage me2=mesArr[h];
				
				if(len >= 2 && !StringUtils.isBlank(s[1])){
					collector.checkThat(me2.getRecevierPhone(), new IsEqual<String>(s[1].trim()));
					collector.addError(new Throwable("短信时间为"+s[0]+"的记录"+"对方短信号码有误"));  
					//collector.checkThat("对方短信号码有误", new IsEqual<String>(s[1]).equalTo(me1.getRecevierPhone()));
				}
				if(len >= 3 && !StringUtils.isBlank(s[2])){
					//collector.checkThat("短信费用有误", new IsEqual<String>(s[2]).equalTo(transBd(NumberUtils.toDouble(me1.getAllPay()))));
					collector.checkThat(formatNumber(me2.getAllPay()), new IsEqual<String>(formatNumber(NumberUtils.toDouble(s[2].trim()))));
					collector.addError(new Throwable("短信时间为"+s[0]+"的记录"+"短信费用有误"));  
				}
				if(len >= 4 && !StringUtils.isBlank(s[3])){
					//collector.checkThat("手机号有误", new IsEqual<String>(s[3]).equalTo(me1.getPhone()));
					collector.checkThat(me2.getPhone(), new IsEqual<String>(s[3].trim()));
					collector.addError(new Throwable("短信时间为"+s[0]+"的记录"+"手机号码有误"));  
				}
			}
		}
		
		//比较账单信息
		//@Test
		public void compTellList(Collection<DianXinTel> telList){
			if (telListInfo == null ) {
				return;
			}
			DianXinTel te1=new DianXinTel();
			int j=telListInfo.length;
			int k=telList.size();
			
				try{
					//System.out.println("k="+k);
					assertTrue("账单记录没有查到",k!=0);
					
				}
				catch(Throwable e){
					collector.addError(e);
					return;
				}
			
				DianXinTel[] telArr=SeekUtil.toTelArray(telList);
				SeekUtil.sort(telArr, new TelListComparator());
			String[] s=null;
			for(int i=0;i<j;i++){
				s=telListInfo[i].split(",");
				
				Date d=DateUtils.StringToDate(s[0], monthFormat);
				te1.setcTime(d);
				int h=SeekUtil.binarySearch(telArr,te1, new TelListComparator());
				System.out.println(h+"账单");;
				try{
					//assertNotNull("短信时间为"+s+"的记录没有查询到",me1);k=-1时
					assertTrue("账单时间为"+telListInfo[i]+"的记录没有查询到",h>=0);
					//collector.checkThat(me1, IsNull.nullValue());	
				}
				catch(Throwable e){
					collector.addError(e);
					continue;
				}
				int len = s.length;
				DianXinTel te2=telArr[h];
				
				//cAllPay本项合计ztcjbf; //主套餐基本费 bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
				if(len >= 2 && !StringUtils.isBlank(s[1])){
					collector.checkThat(formatNumber(te2.getcAllPay()), new IsEqual<String>(formatNumber(NumberUtils.toDouble(s[1].trim()))));
					collector.addError(new Throwable("账单时间为"+s[0]+"的记录"+"本项合计有误"));  
					//collector.checkThat("本项合计", new IsEqual<String>(transBd(NumberUtils.toDouble(s[5]))).equalTo(transBd(te1.getMythf())));
				}
				if(len >= 3 && !StringUtils.isBlank(s[2])){
					collector.checkThat(formatNumber(te2.getZtcjbf()), new IsEqual<String>(formatNumber(NumberUtils.toDouble(s[2].trim()))));
					collector.addError(new Throwable("账单时间为"+s[0]+"的记录"+"主套餐费用有误"));  
					//collector.checkThat("主套餐费用有误", new IsEqual<String>(transBd(NumberUtils.toDouble(s[2]))).equalTo(transBd(te1.getZtcjbf())));
				}
				if(len >= 4 && !StringUtils.isBlank(s[3])){
					collector.checkThat(formatNumber(te2.getBdthf()), new IsEqual<String>(formatNumber(NumberUtils.toDouble(s[3].trim()))));
					collector.addError(new Throwable("账单时间为"+s[0]+"的记录"+"本地通话费有误"));  
					//collector.checkThat("本地通话费有误", new IsEqual<String>(transBd(NumberUtils.toDouble(s[3]))).equalTo(transBd(te1.getBdthf())));
				}
			
				if(len >= 5 && !StringUtils.isBlank(s[4])){
					collector.checkThat(formatNumber(te2.getLdxsf()), new IsEqual<String>(formatNumber(NumberUtils.toDouble(s[4].trim()))));
					collector.addError(new Throwable("账单时间为"+s[0]+"的记录"+"来电显示费有误"));  
					//collector.checkThat("来电显示费有误", new IsEqual<String>(transBd(NumberUtils.toDouble(s[4]))).equalTo(transBd(te1.getLdxsf())));
	
				}
				if(len >= 6 && !StringUtils.isBlank(s[5])){
					collector.checkThat(formatNumber(te2.getMythf()), new IsEqual<String>(formatNumber(NumberUtils.toDouble(s[5].trim()))));
					collector.addError(new Throwable("账单时间为"+s[0]+"的记录"+"漫游通话费有误"));  
					//collector.checkThat("漫游通话有误", new IsEqual<String>(transBd(NumberUtils.toDouble(s[5]))).equalTo(transBd(te1.getMythf())));
				}
			}
			
		}
		//比较详单信息
		//@Test
		public void compDetailList(Collection<DianXinDetail> detailList){
			if (detailInfo == null) {
				return;
			}
			//detailList=dx.getDetailList();
			DianXinDetail de1=new DianXinDetail();
			DianXinDetail[] detailArr=SeekUtil.toTelArray(detailList);
			SeekUtil.sort(detailArr, new DetailListComparator());
			int j=detailInfo.length;
			int k=detailList.size();
			try{
				assertTrue("语音详单没有查到",k!=0);
				
			}
			catch(Throwable e){
				collector.addError(e);
				return;
			}
			String[] s=null;
			for(int i=0;i<j;i++){
				s=detailInfo[i].split(",");
				
				Date d=DateUtils.StringToDate(s[0], detailFormat);
				de1.setcTime(d);
				int h=SeekUtil.binarySearch(detailArr,de1, new DetailListComparator());
				System.out.println("语音详单"+h);
				try{
					assertTrue("通话时间为"+detailInfo[i]+"的记录没有查询到",h>=0);				
				}
				catch(Throwable e){
					collector.addError(e);
					continue;
				}
				int len = s.length;
				DianXinDetail de2=detailArr[h];
				
				if(len >= 2 && !StringUtils.isBlank(s[1])){
					collector.checkThat(de2.getRecevierPhone(), new IsEqual<String>(s[1].trim()));
					collector.addError(new Throwable("通话时间为"+s[0]+"的记录"+"呼叫号码有误"));  
					//collector.checkThat("呼叫号码错误", new IsEqual<String>(s[1]))).equalTo(de1.getRecevierPhone()));	
				}
				if(len >= 3 && !StringUtils.isBlank(s[2])){
					collector.checkThat(formatNumber(de2.getAllPay()), new IsEqual<String>(formatNumber(NumberUtils.toDouble(s[2].trim()))));
					collector.addError(new Throwable("通话时间为"+s[0]+"的记录"+"通话费用有误"));  
					//collector.checkThat("通话费用错误", new IsEqual<String>(transBd(NumberUtils.toDouble(s[2]))).equalTo(transBd(de1.getAllPay())));	
				}
				if(len >= 4 && !StringUtils.isBlank(s[3])){
					collector.checkThat(de2.getTradeTime()+"",new IsEqual<String>(s[3].trim()));
					collector.addError(new Throwable("通话时间为"+s[0]+"的记录"+"通话时长有误"));  
					//collector.checkThat("通话时长错误", new IsEqual<String>(s[3]).equalTo(new Integer(de1.getTradeTime()).toString()));		
				}
			}
			
		}
	     
}
