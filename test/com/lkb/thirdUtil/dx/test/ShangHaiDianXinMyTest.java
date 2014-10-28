package com.lkb.thirdUtil.dx.test;
import com.lkb.bean.TelcomMessage;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.thirdUtil.dx.ShangHaiDianXin;
import com.lkb.util.DateUtils;
import com.lkb.util.httpclient.CUtil;
public class ShangHaiDianXinMyTest extends DianXinForSpiderTest{
	
	//ShangHaiDianXin dx=new ShangHaiDianXin(spider, null, "18016252553", "026315", "2345", null);
	//GanSuDianXin dx=new GanSuDianXin(spider, null, "18016252553", "026315", "2345", null);
		
		
		/*public void setBefore(){
			
			 String[] personInfo = new String[]{"张根硕", "0.3", null, null, null};
			
			//String[] detailInfo = new String[]{"2014/08/10-12:25,13569775,0.3,10", "2014/08/11-08:09,10000,0,30"};
			 String[] detailInfo = new String[]{null};yyyy-MM-dd HH:mm:ss
			
		
			//账单信息cTime,brand,ztcjbf; //主套餐基本费 bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
			 String[] telListInfo=new String[]{null};
			 
	
			initializeInfo(personInfo, detailInfo,telListInfo);
		}*/
	public void initData(){
		setNoandPwd("18016252553", "026315");
		personInfo = new String[]{"张根硕", "  0.38", "131124199101010225", null, null};
		mesInfo=new String[]{"2014-08-10 12:25:20  ,133 ,0.4,18016352553"};
		
		//String[] detailInfo = new String[]{"2014/08/10-12:25,13569775,0.3,10", "2014/08/11-08:09,10000,0,30"};
		 detailInfo = new String[]{"2014-08-10 12:25:20,13231222222,3.2,12"};
		
	
		telListInfo=new String[]{"201405,32,21,21,6,13"};
		
	}
	
	public void execute(){
		
	
			User u=new User();
			Collection<DianXinTel> telList=new ArrayList(); 
			Collection<DianXinDetail> detailList=new ArrayList();
			Collection<TelcomMessage> mesList=new ArrayList();
			//initial();
			u.setRealName("张根硕");
			u.setIdcard("131124199101010226");
			u.setPhoneRemain(new BigDecimal("0.28"));
			
			DianXinTel dxt=new DianXinTel();
			dxt.setcTime(generateDate(2014,04,3,0,0,0));
			//System.out.println(dxt.getcTime().toLocaleString());
		//dxt.setBrand("18");
			
			dxt.setcAllPay(new BigDecimal("30"));
			dxt.setZtcjbf(new BigDecimal("11"));
			dxt.setBdthf(new BigDecimal("24.0"));
			dxt.setLdxsf(new BigDecimal("5"));
			dxt.setMythf(new BigDecimal("18"));
			telList.add(dxt);
			
			DianXinDetail dxd=new DianXinDetail();
			dxd.setcTime(DateUtils.StringToDate("2014-08-10 12:25:20", detailFormat));
			System.out.println(dxd.getcTime().toLocaleString());
			//System.out.println(dxd.getcTime().toLocaleString());
			dxd.setRecevierPhone("13231222223");
			dxd.setAllPay(new BigDecimal("3.3"));
			dxd.setTradeTime(13);
			detailList.add(dxd);
			
			
			TelcomMessage tl=new TelcomMessage();
			tl.setSentTime(DateUtils.StringToDate("2014-08-10 12:25:20", detailFormat));
			
			//System.out.println(DateUtils.StringToDate("2014-08-10 12:25:20", detailFormat)tl.getSentTime().toLocaleString());
			tl.setRecevierPhone("133");
			tl.setAllPay(new Double("0.3"));
			tl.setPhone("18016252553");
			mesList.add(tl);
			
			//comp(personInfo,telListInfo,detailInfo,dx.getUser(),dx.getTelList(),dx.getDetailList());	
			//comp(u,telList,detailList);
			assuranceData(u,telList,detailList,mesList);	
			
		}
		
		public Date generateDate(int Y,int M,int D,int H,int m,int s){
			 Date date = new Date();
		        GregorianCalendar gc = new GregorianCalendar();
		        gc.set(Calendar.YEAR,Y);//设置年
		        gc.set(Calendar.MONTH, M);//这里0是1月..以此向后推
		        gc.set(Calendar.DAY_OF_MONTH, D);//设置天
		        gc.set(Calendar.HOUR_OF_DAY,H);//设置小时
		        gc.set(Calendar.MINUTE, m);//设置分
		        gc.set(Calendar.SECOND, s);//设置秒
		        //gc.set(Calendar.MILLISECOND,200);//设置毫秒
		        date = gc.getTime();
		        return date;
		}
			
			
		
		
}
