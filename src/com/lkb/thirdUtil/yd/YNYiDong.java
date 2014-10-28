package com.lkb.thirdUtil.yd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;

import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

public class YNYiDong  extends BaseInfoMobile {


	public String index = "https://yn.ac.10086.cn/login/";

	// 验证码图片路径
	public static String imgurl = "https://yn.ac.10086.cn/WebAttIon/sso/randomNumber.jsp?";



	public static void main(String[] args) throws Exception {}
	

	public YNYiDong(Login login,String currentUser) {
		super(login,ConstantNum.comm_yn_yidong,currentUser);
	}
	
	
	
	

	public void init(){
		if(!isInit()){
			String text = cutil.get(index);
			if(text!=null){
				 setImgUrl(imgurl);
			}
			redismap.put("jsmap", map);//根据实际需要存放
		}
	}
	
	
	public BigDecimal getYue(){
		BigDecimal phoneremain = new BigDecimal("0");
		try {
			CHeader h= new CHeader(CHeaderUtil.Accept_,null,null,"www.yn.10086.cn",true);
			String text =cutil.get("http://www.yn.10086.cn/service/app?service=page/feeservice.BalanceQueryEC&listener=initPage", h);
		
			if(text.contains("当前余额")){
				text=text.replaceAll("\\s*", "");
				RegexPaserUtil rp = new RegexPaserUtil("当前余额:</td><tdwidth=\"63%\"class=\"fs_16\"><spanclass=\"c_red\"><b>","</b>",text,RegexPaserUtil.TEXTEGEXANDNRT);
	    		String yue = rp.getText();
	    		if(yue!=null){
	    			phoneremain = new BigDecimal(yue);
	    		}	
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return phoneremain;
	}


	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
			try {
				CHeader h= new CHeader(CHeaderUtil.Accept_,null,null,"www.yn.10086.cn",true);
				String myinfo = cutil.get("http://www.yn.10086.cn/service/app?service=page/personalinfo.CustInfoQueryModEC&listener=initPage&subSysCode=E003&eparchyCode=0871&LOGIN_LOG_ID=null", h);
				if(myinfo.contains("个人资料查询")){
					parseBegin(Constant.YIDONG);
					String text = myinfo.replaceAll("\\s*", "");
					
					RegexPaserUtil rp = new RegexPaserUtil("真实姓名</td><tdwidth=\"63%\">","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		String xm = StringEscapeUtils.unescapeHtml3(rp.getText());
		    		rp = new RegexPaserUtil("证件号码</td><tdwidth=\"63%\">","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		String idcard = StringEscapeUtils.unescapeHtml3(rp.getText());
		    		rp = new RegexPaserUtil("通信地址</td><tdwidth=\"63%\">","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		String txdd = StringEscapeUtils.unescapeHtml3(rp.getText());
		    		rp = new RegexPaserUtil("EMAIL邮箱</td><tdwidth=\"63%\">","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		String email = StringEscapeUtils.unescapeHtml3(rp.getText());
		    		Map<String, String> map = new HashMap<String, String>();
						map.put("parentId", currentUser);
						map.put("usersource", Constant.YIDONG);
						map.put("loginName", login.getLoginName());
						List<User> list = userService.getUserByParentIdSource(map);
						if (list != null && list.size() > 0) {
							User user = list.get(0);
							user.setLoginName(login.getLoginName());
							user.setLoginPassword("");
							user.setUserName(xm);
							user.setRealName(xm);
							user.setIdcard(idcard);
							user.setAddr(txdd);
							user.setUsersource(Constant.YIDONG);
							user.setParentId(currentUser);
							user.setModifyDate(new Date());
							user.setPhone(login.getLoginName());
							
							user.setPhoneRemain(getYue());
							user.setEmail(email);
							userService.update(user);
						} else {
							User user = new User();
							UUID uuid = UUID.randomUUID();
							user.setId(uuid.toString());
							user.setLoginName(login.getLoginName());
							user.setLoginPassword("");
							user.setUserName(xm);
							user.setRealName(xm);
							user.setIdcard(idcard);
							user.setAddr(txdd);
							user.setUsersource(Constant.YIDONG);
							user.setParentId(currentUser);
							user.setModifyDate(new Date());
							user.setPhone(login.getLoginName());
						
							user.setPhoneRemain(getYue());
							user.setEmail(email);
							userService.saveUser(user);
						}
					}
				
			} catch (Exception e) {
				errorMsg=e.getMessage();
				sendWarningCallHistory(errorMsg);
			}finally{
				parseEnd(Constant.YIDONG);
			}
	}
	
	public void getTelDetailHtml() {
	
		try {
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,null,null,"www.yn.10086.cn",true);
			parseBegin( Constant.YIDONG);
			int i = 1;
			for (String startDate : ms) {
				String text = cutil.get("http://www.yn.10086.cn/service/app?service=page/feeservice.GroupBillQueryEC&listener=formSubmit&&MONTH="+i+"&subSysCode=E003&eparchyCode=0871&LOGIN_LOG_ID=null", h);
				b=getTelDetailHtml_parse(text, startDate);
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
		
	
		
	}
	

	
	/**
	 * 查询话费记录
	 */
	public boolean getTelDetailHtml_parse(String text,String startDate) {
		boolean b = true;
		try {
			if(text!=null &&text.contains("费用信息")){
				BigDecimal total=new BigDecimal(0);
				BigDecimal tcgdf=new BigDecimal(0);
				BigDecimal tcwyyf=new BigDecimal(0);
				BigDecimal tcwswf=new BigDecimal(0);
				BigDecimal tcwdxf=new BigDecimal(0);
				BigDecimal ywzxf=new BigDecimal(0);
				BigDecimal qtfy=new BigDecimal(0);
				
				text=text.replaceAll("\\s*", "");
				RegexPaserUtil rp = new RegexPaserUtil("本期消费（元）：</b></td><td><b>￥","</b>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String hj = rp.getText();
				if(hj!=null){
					total=new BigDecimal(hj);
				}
				rp = new RegexPaserUtil("套餐及固定费用</td><td>","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String tcgdfs = rp.getText();
				if(tcgdfs!=null){
					tcgdf=new BigDecimal(tcgdfs);
				}
				rp = new RegexPaserUtil("短彩信费</td><td>","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String tcwdxfs = rp.getText();
				if(tcwdxfs!=null){
					tcwdxf=new BigDecimal(tcwdxfs);
				}
				rp = new RegexPaserUtil("自有增值业务</td><td>","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String ywzxfs = rp.getText();
				if(ywzxfs!=null){
					ywzxf=new BigDecimal(ywzxfs);
				}
				Map map2 = new HashMap();
				 map2.put("phone", login.getLoginName());
				 map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
				 List list = mobileTelService.getMobileTelBybc(map2);
				if(list==null || list.size()==0){
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
//					mobieTel.setBaseUserId(currentUser);
					mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
					// mobieTel.setcName(cName);
					mobieTel.setTeleno(login.getLoginName());
					// mobieTel.setBrand(brand);
					mobieTel.setTcwyytxf(tcwyyf);
					mobieTel.setTcgdf(tcgdf);
					mobieTel.setTcwdxf(tcwdxf);
					mobieTel.setZzywf(ywzxf);
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
				
					mobieTel.setcAllPay(total);
					mobileTelService.saveMobileTel(mobieTel);
				}
			} else {
				Map map2 = new HashMap();
				 map2.put("phone", login.getLoginName());
				 map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
				 List list = mobileTelService.getMobileTelBybc(map2);
				if(list==null || list.size()==0){
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
					mobieTel.setTeleno(login.getLoginName());
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
				
					mobieTel.setcAllPay(new BigDecimal(0));
					mobileTelService.saveMobileTel(mobieTel);
				}
			}
			
			
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
	
		return b;
	}

	/**文本解析
	 * true正常解析
	 * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
	 * type 市话/长途/港澳台/漫游
	 */
	public boolean  callHistory_parse(String text,MobileDetail bean,String startDate){
		boolean b = true;
		try {
			if(text.contains("通信地点")&&text.contains("对方号码")){
				Document doc  = Jsoup.parse(text);
				 Elements tables = doc.select("table[class=tab1]");
				 if(tables.size()>0){
					 Element table1 =  tables.get(0);
					 Elements trs  = table1.select("tr");
					 for (int j = 1; j < trs.size(); j++) {
						 Elements tds =trs.get(j).select("td");
						 if(tds.size()>7){
							 String thsj= tds.get(0).text().trim().replaceAll(" ", "");
							 String txdd= tds.get(1).text().trim().replaceAll(" ", "");
							 String txfs= tds.get(2).text().trim().replaceAll(" ", "");
							 String dfhm= tds.get(3).text().trim().replaceAll(" ", "");
							 String thsc= tds.get(4).text().trim().replaceAll(" ", "");
							 String txlx= tds.get(5).text().trim().replaceAll(" ", "");
							 if(txlx.contains("主叫")){
								 txlx = "主叫";
							 }else{
								 txlx = "被叫"; 
							 }
							 String sjsf= tds.get(6).text().trim().replaceAll(" ", "");
							// String sjsf= tds.get(7).text().trim().replaceAll(" ", "");
							 Map map2 = new HashMap();
							 map2.put("phone", login.getLoginName());
							 map2.put("cTime", DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
							 List list = mobileDetailService.getMobileDetailBypt(map2);
							if(list==null || list.size()==0){
								MobileDetail mDetail = new MobileDetail();
								UUID uuid = UUID.randomUUID();
								mDetail.setId(uuid.toString());
					        	mDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
					        	if(bean!=null){
									 if(bean.getcTime().getTime()>=mDetail.getcTime().getTime()){
										return false;
									 }
								 }
					        	mDetail.setTradeAddr(txdd);
					        	mDetail.setTradeWay(txlx);
					        	mDetail.setRecevierPhone(dfhm);
					        	int times = 0;
								try{
									TimeUtil tunit = new TimeUtil();
									times = tunit.timetoint(thsc);
								}catch(Exception e){
									
								}		
					        	mDetail.setTradeTime(times);
					        	mDetail.setTradeType(txfs);
					        //	mDetail.setTaocan(tcyh);
					        	mDetail.setOnlinePay(new BigDecimal(sjsf));
					        	mDetail.setPhone(login.getLoginName());    	
								mobileDetailService.saveMobileDetail(mDetail);
						}
						 
					 }
				  }
			   }
			
			}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	/**
	 * 查询通话记录
	 */
	public void callHistory(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.yn.10086.cn",null,"www.yn.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);
			for (String startDate : ms) {
				
				 
				Map<String, String> parMap = new LinkedMap();
				parMap.put("$FormConditional", "F");
				parMap.put("AMOUNT_HOME", "");
				parMap.put("BILL_TYPE2", "1");
				parMap.put("Form0", "user_status,menuId,brandCode,SERIALNUMBER_HOME,AMOUNT_HOME,$FormConditional,pageName,pageName2,isLogin,loginType,isNewNetCustomer,isNewNetFirst,isNeedSSOVertify,bquery");
				parMap.put("MONTH", startDate);
				parMap.put("SERIALNUMBER_HOME", "");
				parMap.put("SHOW_TYPE", "0");
				parMap.put("bquery", "");
				parMap.put("brandCode", "2");
				parMap.put("isLogin", "Strue");
				parMap.put("isNeedSSOVertify", "false");
				parMap.put("isNewNetCustomer", "false");
				parMap.put("isNewNetFirst", "1");
				parMap.put("loginType", "S1");
				parMap.put("menuId", "11282243");
				parMap.put("pageName", "Sfeeservice.VoiceQueryNewEC");
				parMap.put("pageName2", "Sfeeservice.VoiceQueryNewEC");
				parMap.put("service", "direct/1/feeservice.VoiceQueryNewEC/$Form");
				parMap.put("sp", "S0");
				parMap.put("textfield", "");
				parMap.put("textfield", "");
				parMap.put("user_status", "0");
				String text = cutil.post("http://www.yn.10086.cn/service/app?eparchyCode=0871&wade_randomcode=0.8211373662887548", h,parMap);
				b= callHistory_parse(text,bean,startDate);
				if(!b){
					//异常信息
					if(errorMsg!=null){
						
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
			
		
	}

	
	/**
	 * 查询通话记录
	 */
	public void messageHistory(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.yn.10086.cn",null,"www.yn.10086.cn",true);
			boolean b = false;
			int num = 0;
		
			MobileMessage bean = mobileMessageService.getMaxSentTime(login.getLoginName());
			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				parm.put("$FormConditional", "F");
				parm.put("AMOUNT_HOME", "");
				parm.put("BILL_TYPE2", "2");
				parm.put("Form0", "user_status,menuId,brandCode,SERIALNUMBER_HOME,AMOUNT_HOME,$FormConditional,pageName,pageName2,isLogin,loginType,isNewNetCustomer,isNewNetFirst,isNeedSSOVertify,bquery");
				parm.put("MONTH",startDate);
				parm.put("SERIALNUMBER_HOME", "");
				parm.put("SHOW_TYPE", "0");
				parm.put("bquery", "");
				parm.put("brandCode", "2");
				parm.put("isLogin", "Strue");
				parm.put("isNeedSSOVertify", "false");
				parm.put("isNewNetCustomer", "false");
				parm.put("isNewNetFirst", "1");
				parm.put("loginType", "S1");
				parm.put("menuId", "11282243");
				parm.put("pageName", "Sfeeservice.VoiceQueryNewEC");
				parm.put("pageName2", "Sfeeservice.VoiceQueryNewEC");
				parm.put("service", "direct/1/feeservice.VoiceQueryNewEC/$Form");
				parm.put("sp", "S0");
				parm.put("textfield", "");
				parm.put("textfield", "");
				parm.put("user_status", "0");
				String text = cutil.post("http://www.yn.10086.cn/service/app?eparchyCode=0871&wade_randomcode=0.9222471712606621", h, parm);
				//System.out.println(text);
				b= messageHistory_parse(text,bean,startDate);
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
			
		
	}

	
	public boolean  messageHistory_parse(String text,MobileMessage bean,String startDate){
		boolean b = true;
		try {
			if(text.contains("起始时间")&&text.contains("通信地点")){
				Document doc  = Jsoup.parse(text);
				 Elements tables = doc.select("table[class=tab1]");
				 if(tables.size()>0){
					 Element table1 =  tables.get(0);
					 Elements trs  = table1.select("tr");
					 for (int j = 1; j < trs.size(); j++) {
						 Elements tds =trs.get(j).select("td");
						 if(tds.size()>7){
							 String thsj= tds.get(0).text().trim().replaceAll(" ", "");
							 String txdd= tds.get(1).text().trim().replaceAll(" ", "");
							 String dfhm= tds.get(2).text().trim().replaceAll(" ", "");
							 String txfs= tds.get(3).text().trim().replaceAll(" ", "");
							 String xxlx= tds.get(4).text().trim().replaceAll(" ", "");
							 String ywmc= tds.get(5).text().trim().replaceAll(" ", "");
							 String tc= tds.get(6).text().trim().replaceAll(" ", "");
							 String txf= tds.get(7).text().trim().replaceAll(" ", "");
							MobileMessage mDetail = new MobileMessage();
							UUID uuid = UUID.randomUUID();
							mDetail.setId(uuid.toString());
				        	mDetail.setSentTime(DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
				        	if(bean!=null){
								 if(bean.getSentTime().getTime()>=mDetail.getSentTime().getTime()){
									return false;
								 }
							 }
				        	mDetail.setCreateTs(new Date());
				        	mDetail.setAllPay(new BigDecimal(txf));
				        	mDetail.setRecevierPhone(dfhm);
				        	mDetail.setSentAddr(txdd);
				        	mDetail.setTradeWay(txfs);
				        	mDetail.setPhone(login.getLoginName());    	
							mobileMessageService.save(mDetail);
					
					}
				}
			}
		 }
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	
	
	
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
//		parseBegin(Constant.YIDONG);
//		try{
			messageHistory();
			callHistory(); //历史账单	
			getTelDetailHtml();//通话记录
			getMyInfo(); //个人信息
			gatherFlow();//流量详单和账单
//		}finally{
//			parseBegin(Constant.YIDONG);
//		}
//		
	}
	

	// 首页登录
	public Map<String,Object> login() {
		try{
			String result = login1();
			if(result!=null&& result.equals("1")){
				loginsuccess();
			}else if(result!=null){
				errorMsg=result;
			}else{
				errorMsg="登录异常，请刷新页面重试.";
			}
			if(islogin()){
				addTask(this);
	    	}
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;
	}
	
	
	public String login1(){
		try {
				Map<String, String> dxMap = new LinkedMap();
				dxMap.put("ErrorUrl", "yn.ac.10086.cn/login/");
				dxMap.put("FieldID", "1");
				dxMap.put("ReturnURL", "my/web/index/Login_doin.do");
				dxMap.put("changepsw", "sjp");
				dxMap.put("mobileNum", login.getLoginName());
				dxMap.put("pwdtext", "请输入您的6位服务密码");
				dxMap.put("randCode", login.getAuthcode());
				dxMap.put("randompasswd", "");
				dxMap.put("remenbernum", "0");
				dxMap.put("servicePWD", login.getPassword());
				CHeader c = new CHeader(CHeaderUtil.Accept_,index,null,"yn.ac.10086.cn",true);
				String  text = cutil.post("https://yn.ac.10086.cn/WebAttIon/servlet/LoginServiceServlet", c, dxMap);
				if(text!=null){
					if(text.contains("Result=2")){
						return "验证码错误.";
					}else if(text.contains("Result=1")){
						return "密码错误.";
					}else if(text.contains("Result=3")){
						return "您输入的号码非云南省归属.";
					}else if(text.contains("Result=0")){
						text = cutil.get("https://yn.ac.10086.cn/WebAttIon/sso/MyResult.jsp?SSOPAGE=my/web/index/Login_doin.do&Result=0",c);
						addCookie("CmWebtokenid", login.getLoginName()+",yn", "10086.cn");
						RegexPaserUtil rp = new RegexPaserUtil("attritd=\" \\+ \"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
						String attritd = rp.getText();
						CHeader c1 = new CHeader(CHeaderUtil.Accept_,null,null,"www.yn.10086.cn",true);
						text = cutil.get("http://www.yn.10086.cn/my/web/index/Login_doin.do?attritd="+attritd,c1);
						if(text!=null && text.contains("登录成功")){
							text = cutil.get("http://www.yn.10086.cn/my/",c1);
							return login2();
						}
					}
					
				}	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
	}
	
	
	
	public String login2() {
	
		CHeader c = new CHeader(CHeaderUtil.Accept_,"http://www.yn.10086.cn/service/app?service=page/BusiOperation&listener=initPage&menuCode=50102",null,"www.yn.10086.cn",true);
		try {
			String text = cutil.get("http://www.yn.10086.cn/service/app?service=page/feeservice.VoiceQueryNewEC&listener=initPage&radioId=radio1&subSysCode=E003&eparchyCode=0871&LOGIN_LOG_ID=null",c);
			if(text!=null){
				text = StringEscapeUtils.unescapeHtml3(text);
				if(text.contains("短信验证码为：")){
					RegexPaserUtil rp = new RegexPaserUtil("短信验证码为：","，",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String sendCode = rp.getText();
					 Document doc = Jsoup.parse(text);
					 String EPARCHY_CODE = doc.select("input[name=EPARCHY_CODE]").first().val();
					 String BRAND_NO = doc.select("input[name=BRAND_NO]").first().val();
					 String IN_CHANNEL_CODE = doc.select("input[name=IN_CHANNEL_CODE]").first().val();
					// String LOGIN_LOG_ID = doc.select("input[name=LOGIN_LOG_ID]").first().val();
					 String PROVINCE_CODE = doc.select("input[name=PROVINCE_CODE]").first().val();
					 String ROUTE_EPARCHY_CODE = doc.select("input[name=ROUTE_EPARCHY_CODE]").first().val();
					 String SEND_CONTENT = doc.select("input[name=SEND_CONTENT]").first().val();
					 
					 Map<String, String> parMap = new LinkedMap();
					parMap.put("$FormConditional", "F");
					parMap.put("AMOUNT_HOME", "");
					parMap.put("BRAND_NO", BRAND_NO);
					parMap.put("EPARCHY_CODE", EPARCHY_CODE);
					parMap.put("Form0", "user_status,menuId,brandCode,SERIALNUMBER_HOME,AMOUNT_HOME,$FormConditional,pageName,pageName2,isLogin,loginType,isNewNetCustomer,isNewNetFirst,isNeedSSOVertify,bforgot,regetPwd");
					parMap.put("IN_CHANNEL_CODE", IN_CHANNEL_CODE);
					parMap.put("LOGIN_LOG_ID", "null");
					parMap.put("PROVINCE_CODE", PROVINCE_CODE);
					parMap.put("ROUTE_EPARCHY_CODE", ROUTE_EPARCHY_CODE);
					parMap.put("SEND_CONTENT", SEND_CONTENT);
					parMap.put("SEND_TYPE_CODE", "1");
					parMap.put("SERIALNUMBER_HOME", "");
					parMap.put("SERIAL_NUMBER", login.getLoginName());
					parMap.put("SMS_NUMBER", sendCode);
					parMap.put("TRADE_CITY_CODE", "A0AM");
					parMap.put("TRADE_EPARCHY_CODE", EPARCHY_CODE);
					parMap.put("X_TRANS_CODE", "ITF_ICS_INSERTIICSSMS");
					parMap.put("X_TRANS_TYPE", "WS");
					parMap.put("bforgot", "");
					parMap.put("brandCode", "");
					parMap.put("eparchyCode", EPARCHY_CODE);
					parMap.put("isLogin", "Strue");
					parMap.put("isNeedSSOVertify", "false");
					parMap.put("isNewNetCustomer", "false");
					parMap.put("isNewNetFirst", "1");
					parMap.put("loginType", "S1");
					parMap.put("menuId", "");
					parMap.put("mpageName", "feeservice.VoiceQueryNewEC");
					parMap.put("pageName", "Spersonalinfo.CheckedSmsEC");
					parMap.put("pageName2", "Spersonalinfo.CheckedSmsEC");
					parMap.put("radioId", "radio1");
					parMap.put("service", "direct/1/personalinfo.CheckedSmsEC/$Form");
					parMap.put("sp", "S0");
					parMap.put("subSysCode", "E003");
					parMap.put("user_status", "0");
					parMap.put("validateCodeType", "VOICE_QUERY");
					c = new CHeader(CHeaderUtil.Accept_,"http://www.yn.10086.cn/service/app?service=page/feeservice.VoiceQueryNewEC&listener=initPage&radioId=radio1&subSysCode=E003&eparchyCode=0871&LOGIN_LOG_ID=null",CHeaderUtil.Content_Type__urlencoded,"www.yn.10086.cn",true);
					text = cutil.post("http://www.yn.10086.cn/service/app?eparchyCode=0871&wade_randomcode=0.6986544225504868",c,parMap);
					if(text!=null){
						text = StringEscapeUtils.unescapeHtml3(text);
						doc = Jsoup.parse(text);
						//System.out.println(text);
						 String MONTH = doc.select("input[name=MONTH]").first().val();
						 if(MONTH!=null){
							 return "1";
						 }
						
					}
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 查询流量记录
	 */
	public void gatherFlow(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyy-MM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.yn.10086.cn",null,"www.yn.10086.cn",true);
			boolean b = false;
			int num = 0;
		
			MobileOnlineList bean_List = null;
			MobileOnlineBill bean_bill = null;

			bean_List = mobileOnlineListService
					.getMaxTime(login.getLoginName());
			bean_bill = mobileOnlineBillService
					.getMaxTime(login.getLoginName());

			for (String startDate : ms) {
				Map<String, String> parMap = new LinkedMap();
				parMap.put("$FormConditional", "F");
				parMap.put("AMOUNT_HOME", "");
				parMap.put("BILL_TYPE2", "3");
				parMap.put("Form0", "user_status,menuId,brandCode,SERIALNUMBER_HOME,AMOUNT_HOME,$FormConditional,pageName,pageName2,isLogin,loginType,isNewNetCustomer,isNewNetFirst,isNeedSSOVertify,bquery");
				parMap.put("MONTH", startDate.replaceAll("-", ""));
				parMap.put("SERIALNUMBER_HOME", "");
				parMap.put("SHOW_TYPE", "0");
				parMap.put("bquery", "");
				parMap.put("brandCode", "2");
				parMap.put("isLogin", "Strue");
				parMap.put("isNeedSSOVertify", "false");
				parMap.put("isNewNetCustomer", "false");
				parMap.put("isNewNetFirst", "1");
				parMap.put("loginType", "S1");
				parMap.put("menuId", "11282243");
				parMap.put("pageName", "Sfeeservice.VoiceQueryNewEC");
				parMap.put("pageName2", "Sfeeservice.VoiceQueryNewEC");
				parMap.put("service", "direct/1/feeservice.VoiceQueryNewEC/$Form");
				parMap.put("sp", "S0");
				parMap.put("textfield", "");
				parMap.put("textfield", "");
				parMap.put("user_status", "0");
				String text = cutil
						.post("http://www.yn.10086.cn/service/app?eparchyCode=0871&wade_randomcode=0.12126356133147909",
								h, parMap);
				
				
				Document doc6 = Jsoup.parse(text);
				
				b = onlineList_parse(doc6.toString(), bean_List, startDate);
				b = onlineBill_parse(doc6.toString(), bean_bill, startDate);
				
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningFlow(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}
	

	public boolean  onlineList_parse(String text,MobileOnlineList bean_List,String startDate){
		boolean b = true;
		try {
			if(!text.contains("未找到相应业务信息")){
				Document doc6 = Jsoup.parse(text);
				Element table =doc6.select("table[id=table]").get(0);
						Elements trs = table.select("tr");
						for(int j = 1 ; j<trs.size();j++){
							Elements tds = trs.get(j).select("td");

							Date cTime = null;
							String tradeAddr = null;
							String onlineType = null;
							long onlineTime = 0;
							long totalFlow = 0;
							String cheapService = null;
							BigDecimal communicationFees = new BigDecimal(0);
							try {
								String cTime1 = tds.get(0).text().trim();
								cTime = DateUtils.StringToDate(cTime1, "yyyy-MM-dd HH:mm:ss");
								tradeAddr = tds.get(1).text().trim();
								onlineType = tds.get(3).text();
								String onlineTime1 = tds.get(4).text().trim();
								onlineTime = StringUtil.flowTimeFormat(onlineTime1);
								
								String totalFlow1 = tds.get(5).text().trim();
								String [] flow= totalFlow1.replaceAll(";", "").split(":");
								double kb = Double.parseDouble(flow[1].replace("B", "")) / 1024;
								totalFlow = Math.round(kb);
								
								cheapService = tds.get(7).text().trim();
								String communicationFees1 = tds.get(6).text().trim();// 通信费
								if("无".equals(communicationFees1)){
									communicationFees = new BigDecimal(0);
								} else if(communicationFees1.contains("元")){
									communicationFees = new BigDecimal(communicationFees1.replaceAll("元", ""));
								} else {
									communicationFees = new BigDecimal(communicationFees1);
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						 try {
								MobileOnlineList onlineList = new MobileOnlineList();
								UUID uuid = UUID.randomUUID();
								onlineList.setId(uuid.toString());
								onlineList.setcTime(cTime);
								if(bean_List!=null){
									 if(bean_List.getcTime().getTime()<=onlineList.getcTime().getTime()){
										onlineList.setcTime(cTime);
										onlineList.setCheapService(cheapService);
										onlineList.setCommunicationFees(communicationFees);
										onlineList.setOnlineTime(onlineTime);
										onlineList.setOnlineType(onlineType);
										onlineList.setTotalFlow(totalFlow);
										onlineList.setTradeAddr(tradeAddr);
										onlineList.setPhone(login.getLoginName());    	
										mobileOnlineListService.save(onlineList);
									 }
								 }else {
									 	onlineList.setcTime(cTime);
										onlineList.setCheapService(cheapService);
										onlineList.setCommunicationFees(communicationFees);
										onlineList.setOnlineTime(onlineTime);
										onlineList.setOnlineType(onlineType);
										onlineList.setTotalFlow(totalFlow);
										onlineList.setTradeAddr(tradeAddr);
										onlineList.setPhone(login.getLoginName());    	
										mobileOnlineListService.save(onlineList);
								 }
								
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		 }
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	public boolean  onlineBill_parse(String text,MobileOnlineBill bean_Bill,String startDate){
		boolean b = true;
		try {
			if (!text.contains("未找到相应业务信息")) {
				Document doc6 = Jsoup.parse(text);

				Date monthly = null;
				BigDecimal totalFlow = new BigDecimal(0);
				long freeFlow = 0;
				long chargeFlow = 0;
				BigDecimal communicationFees = new BigDecimal(0);
				monthly = DateUtils.StringToDate(startDate, "yyyy-MM");

				Element div = doc6.select("div[class=tab_gray txt-c]").get(0);
				String totalFlow1 = div.select("ul:eq(0)>li:eq(1)").text()
						.trim();
				totalFlow = new BigDecimal(totalFlow1).multiply(BigDecimal
						.valueOf(1024));

				Element table = doc6.select("table[class=tab2]").get(0);
				String trafficCharges = table.select("tbody>tr:eq(2)").get(0).select("td").get(1)
						.text().trim();// 通信费
				communicationFees = new BigDecimal(trafficCharges);

				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("phone", login.getLoginName());
				map.put("monthly", monthly);
				List<Map> list = mobileOnlineBillService
						.getMobileOnlineBill(map);
				if (list == null || list.size() == 0) {
					MobileOnlineBill onlineBill = new MobileOnlineBill();
					UUID uuid = UUID.randomUUID();
					onlineBill.setId(uuid.toString());
					if (bean_Bill != null) {
						if (bean_Bill.getMonthly().getTime() >= onlineBill
								.getMonthly().getTime()) {
							return false;
						}
					}
					onlineBill.setMonthly(monthly);
					onlineBill.setChargeFlow(chargeFlow);
					onlineBill.setFreeFlow(freeFlow);
					onlineBill.setTotalFlow(totalFlow.longValue());
					onlineBill.setTrafficCharges(communicationFees);
					onlineBill.setPhone(login.getLoginName());
					onlineBill.setIscm(0);
					mobileOnlineBillService.save(onlineBill);
				}
			} else {
				Date monthly = DateUtils.StringToDate(startDate, "yyyy-MM");
				
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("phone", login.getLoginName());
				map.put("monthly", monthly);
				List<Map> list = mobileOnlineBillService
						.getMobileOnlineBill(map);
				if (list == null || list.size() == 0) {
					MobileOnlineBill onlineBill = new MobileOnlineBill();
					UUID uuid = UUID.randomUUID();
					onlineBill.setId(uuid.toString());
					onlineBill.setMonthly(monthly);
					if (bean_Bill != null) {
						if (bean_Bill.getMonthly().getTime() >= onlineBill
								.getMonthly().getTime()) {
							return false;
						}
					}
					onlineBill.setTotalFlow((long) 0);
					onlineBill.setTrafficCharges(new BigDecimal(0.00));
					onlineBill.setPhone(login.getLoginName());
					onlineBill.setIscm(0);
					mobileOnlineBillService.save(onlineBill);
				}
			}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}

}
