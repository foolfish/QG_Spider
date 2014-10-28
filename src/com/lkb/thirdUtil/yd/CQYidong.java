package com.lkb.thirdUtil.yd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

import freemarker.core._RegexBuiltins.replace_reBI;


public class CQYidong extends BaseInfoMobile{
	private static final Logger log = Logger.getLogger(CQYidong.class);
	
	
	public String index = "http://service.cq.10086.cn/app?service=page/newLogin.login";
	// 验证码图片路径
	public static String imgurl = "https://cq.ac.10086.cn/SSO/img?width=51&height=20&rand=";
	

	
	public CQYidong(Login login,String currentUser) {
		super(login, ConstantNum.comm_cq_yidong,currentUser);
	}
	
	
   public void init(){
	   if(!isInit()){
			String text = cutil.get(index);
			if(text!=null){
				 setImgUrl(imgurl+new Date().getTime());	 	
				 setInit();
				 redismap.put("jsmap", map);//根据实际需要存放
			}
		}
   }
	
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
		int type = login.getType();
		try{
			parseBegin(Constant.YIDONG);
		switch (type) {
		case 1:
			getTelDetailHtml();//账单记录
			getMyInfo(); //个人信息
			break;
		case 2:
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			
			MobileOnlineList bean_List = null;
			try {
				if(mobileOnlineListService.getMaxTime(login.getLoginName())!=null) {
					bean_List = mobileOnlineListService.getMaxTime(login.getLoginName());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MobileOnlineBill bean_Bill = null;
			try {
				if(mobileOnlineBillService.getMaxTime(login.getLoginName())!=null) {
					bean_Bill = mobileOnlineBillService.getMaxTime(login.getLoginName());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			int iscm1 = 5;
			for (String startDate:ms ) {   //月份按照从小到大
				callHistory(startDate, ms); //通话记录	
				messageHistory(startDate, ms);//短信记录
				online(startDate, ms,bean_List,bean_Bill,iscm1);//流量记录
				iscm1--;
			}
			break;
		case 3:
			getTelDetailHtml();//通话记录
			getMyInfo(); //个人信息
			List<String> ms2 =  DateUtils.getMonths(6,"yyyyMM");
			for (String startDate : ms2) {
				callHistory(startDate, ms2); //历史账单	
				messageHistory(startDate, ms2);//短信记录
			}
			break;
		default:
			break;
		}
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}


	
	

	
	public  static String password(String pass) {
		ScriptEngineManager manager = new ScriptEngineManager();

		ScriptEngine engine = manager.getEngineByExtension("js");   
		String rsaPath = InfoUtil.getInstance().getInfo("road","tomcatWebappPath")+"/js/yd/cqyd_pwd.js";

		 File f = new File(rsaPath);
		 FileInputStream fip = null;
		try {
			fip = new FileInputStream(f);
		
		// 构建FileInputStream对象
			InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
			// 执行指定脚本
			engine.eval(reader);
			if(engine instanceof Invocable) {
				Invocable invoke = (Invocable)engine;
				//调用merge方法，并传入两个参数
		
				Object c = (Object)invoke.invokeFunction("base64encode",pass);
	
				reader.close();
				return c.toString();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	
	public String login1() {
		CHeader h = new CHeader(CHeaderUtil.Accept_,"http://service.cq.10086.cn/app?service=page/newLogin.login",CHeaderUtil.Content_Type__urlencoded,"service.cq.10086.cn");
		
		
		String url = "http://service.cq.10086.cn/app?service=page/SSOLogin&listener=login";
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("EFFICACY_CODE",  login.getAuthcode()); 
		param.put("Form0", "blogin"); 
		param.put("SERIAL_NUMBER", login.getLoginName()); 
		param.put("USER_PASSSMS", ""); 
		param.put("USER_PASSWD", password(login.getPassword())); 
		param.put("USER_PASSWD_SELECT", "1"); 
		param.put("clogin", "on"); 
		param.put("service", "direct/1/newLogin.login/$Form"); 
		param.put("sp", "S0"); 
		
		
		String text = cutil.post(url, h, param);
		if(text.contains("action=\"https://cq.ac.10086.cn/SSO/loginbox\"")){
	    		Document doc = Jsoup.parse(text);
	    		String service = doc.select("input[name=service]").first().val();
	    		String failUrl = doc.select("input[name=failUrl]").first().val();
	    		String username = doc.select("input[name=username]").first().val();
	    		String password = doc.select("input[name=password]").first().val();
	    		String passwordType = doc.select("input[name=passwordType]").first().val();
	    		String validateCode = doc.select("input[name=validateCode]").first().val();
	    		String smsRandomCode = doc.select("input[name=smsRandomCode]").first().val();
	    	
	    		param = new LinkedHashMap<String,String>();
	    		param.put("service",  service); 
	    		param.put("failUrl", failUrl); 
	    		param.put("username", username); 
	    		param.put("password", password); 
	    		param.put("passwordType", passwordType); 
	    		param.put("validateCode", validateCode); 
	    		param.put("smsRandomCode", smsRandomCode); 
	    		text = cutil.post("https://cq.ac.10086.cn/SSO/loginbox", h, param);
	    		
	    	
	    		if(text!=null  && text.contains("SAMLart")){
	    			doc = Jsoup.parse(text);
	    			String PasswordType = doc.select("input[name=PasswordType]").first().val();
	    			String RelayState = doc.select("input[name=RelayState]").first().val();
		    		String SAMLart = doc.select("input[name=SAMLart]").first().val();
		    		
		    		String errorMsg = doc.select("input[name=errorMsg]").first().val();
		    		param = new LinkedHashMap<String,String>();
		    		param.put("PasswordType",  PasswordType); 
		    		param.put("RelayState", RelayState); 
		    		param.put("SAMLart", SAMLart); 
		    		param.put("errorMsg", errorMsg); 
		    		 text = cutil.post("http://service.cq.10086.cn/CHOQ/authentication/authentication_return.jsp?timeStamp="+new Date().getTime(), h, param);
		    	
		    		 if(text!=null && text.contains("SERIAL_NUMBER")){
		    			 	doc = Jsoup.parse(text);
			    			String SERIAL_NUMBER = doc.select("input[name=SERIAL_NUMBER]").first().val();
			    			String flag = doc.select("input[name=flag]").first().val();
				    		String errorInfo = doc.select("input[name=errorInfo]").first().val();
				    		String UID = doc.select("input[name=UID]").first().val();
				    		param = new LinkedHashMap<String,String>();
				    		param.put("SERIAL_NUMBER",  SERIAL_NUMBER); 
				    		param.put("flag", flag); 
				    		param.put("errorInfo", errorInfo); 
				    		param.put("UID", UID); 
				    		text = cutil.post("http://service.cq.10086.cn/app?service=page/Home&listener=getLoginInfo", h, param);
				    		//System.out.println(text);
				    		if(text.contains(login.getLoginName())){
				    			return "1";
				    		}else{
				    			text =  cutil.get(text, h);
					    		text =  cutil.get("http://service.cq.10086.cn/app", h);
					    		text =   cutil.get("http://service.cq.10086.cn/app?service=page/operation.myMobileIndex&listener=initPage", h);
					    		if(text!=null && text.contains("余额")){
					    			return "1";
					    		}	
				    		}
				    }
		    	}else if(text.contains("authentication_error.jsp")){
	    			doc = Jsoup.parse(text);
	    			String errInfo = doc.select("input[name=errInfo]").first().val();
	    			try {
						errInfo = URLDecoder.decode(errInfo, "gbk");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	    			return errInfo;
	    		}
	    
	    	}
	     return null;
	}
	
/*	public void test(){
		String result = cutil.getURL("http://service.cq.10086.cn/app?service=page/operation.PersonalInfo&listener=initPage", new CHeader("https://service.cq.10086.cn/app?service=page/operation.PersonalInfo&listener=initPage"));
		System.out.println(result);
		System.out.println(getCookiesString());
	}*/
	

	// 首页登录
	public  Map<String,Object>  login() {
		try{
		String result = login1();
		if(result!=null){
			if(result.equals("1")){
				loginsuccess();
			}else{
				errorMsg=result;
			}
		}
		
		if(status==1){
			sendPhoneDynamicsCode();
			addTask_1(this);
    	}
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;	
	}
	

	
	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {

		try {
			Map<String, String> parmap = new HashMap<String, String>(3);
			parmap.put("parentId", currentUser);
			parmap.put("loginName", login.getLoginName());
			parmap.put("usersource", Constant.YIDONG);
			parseBegin(Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(parmap);
			
			CHeader header = new CHeader("https://service.cq.10086.cn/app?service=page/operation.PersonalInfo&listener=initPage");
			header.setContent_Type("text/html;charset=GBK");
			String result = cutil.get("http://service.cq.10086.cn/app?service=page/operation.PersonalInfo&listener=initPage", header);
			String realName = "";
			String userPack = "";
			if(result != null && result.contains("客户姓名")){
				Document doc = Jsoup.parse(result);
				Elements es = doc.select("table.infoTable");
				if (es.size() <= 0) {
					return;
				}
				final String text = es.text();
				realName = StringUtil.subStr("客户姓名： ", "密码", text).trim();
				userPack = StringUtil.subStr("客户品牌：", "原密码", text).trim();
			}


			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());
				user.setRealName(realName);
				user.setIdcard("");
				user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				user.setEmail("");
				user.setPackageName(userPack);
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());

				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());

				user.setRealName(realName);
				user.setIdcard("");
				user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				user.setEmail(" ");
				user.setPackageName(userPack);
				userService.saveUser(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg=e.getMessage();
			sendWarningCallHistory(errorMsg);
		}finally{
			parseEnd(Constant.YIDONG);
		}

	}
	
	public BigDecimal getYue(){
		BigDecimal phoneremain= new BigDecimal("0.00");
		CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"service.cq.10086.cn",true);
		String text =  cutil.get("http://service.cq.10086.cn/app?service=page/operation.myMobileIndex&listener=initPage", h);
		if(text!=null && text.contains("余额")){
			try {
					RegexPaserUtil rp = new RegexPaserUtil(">账户余额：<strong class=\"font14 verdana roseo\">","</strong>",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		String yue = rp.getText();
					if(yue!=null){
						phoneremain = new BigDecimal(yue.replaceAll("\\s*", ""));
					}
					
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return phoneremain;
	}
	
	
	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			CHeader c = new CHeader(CHeaderUtil.Accept_,"",null,"service.cq.10086.cn",true);
			String text = cutil.get("http://service.cq.10086.cn/app?service=page/personalinfo.SecondCheck&listener=doCheckedSms&PageNumber="+login.getPhoneCode(),c);
			if(text!=null){
				if(text.contains("通话详单查询")){
					Document doc = Jsoup.parse(text);
					String token = doc.select("input[id=token]").first().val();
					//System.out.println(doc);
					jsmap.put("token", token);
					errorMsg = "验证成功！";
					status = 1;	
				}else if(text.contains("您输入的短信验证码校验失败")){
					errorMsg = "您输入的短信验证码校验失败，请重新操作。！";
				}else {
					errorMsg = "您输入的查询验证码错误或过期，请重新核对或再次获取！";
				}
			}else{
				errorMsg = "验证失败,请重试!";
			}
			map.put(CommonConstant.status, status);
			map.put(CommonConstant.errorMsg, errorMsg);
			if(status==1){
	        	addTask_2(this);
	    	}
			return map;	
	}

	/**
	 * 生成短信
	 * */
	public Map<String,Object> sendPhoneDynamicsCode() {
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		int status = 0;
		CHeader c = new CHeader(CHeaderUtil.Accept_,"",null,"service.cq.10086.cn",true);
	
		try {

			String text = cutil.get("http://service.cq.10086.cn/app?service=page/personalinfo.SecondCheck&listener=initPage&pageCodeValidate=myYD.OrderQuery",c);

			if(text.contains(login.getLoginName())){
				errorMsg="发送成功";
				status = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(errorMsg==null){
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}

	public void excute() {

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
				 Elements tables = doc.select("table[class=black font12 ywTable]");
				 if(tables.size()>0){
					 Element table1 =  tables.get(0);
					 Elements trs  = table1.select("tr");
					 for (int j = 0; j < trs.size(); j++) {
						 Elements tds =trs.get(j).select("td");
						 if(tds.size()>8){
							 String thsj= tds.get(0).text();
							 String txdd=StringEscapeUtils.unescapeHtml3(tds.get(1).text());
							 String txfs=StringEscapeUtils.unescapeHtml3( tds.get(2).text().trim());
							 String dfhm= StringEscapeUtils.unescapeHtml3(tds.get(3).text().trim());
							 String thsc=StringEscapeUtils.unescapeHtml3( tds.get(4).text().trim());
							 String txlx= StringEscapeUtils.unescapeHtml3(tds.get(5).text().trim());
							// String tcyh=StringEscapeUtils.unescapeHtml3(tds.get(5).text().trim().replaceAll(" ", "");
							 String thf= tds.get(7).text().trim();
							 String ctf= tds.get(8).text().trim();
							 
							 if(txfs.contains("主叫")){
								 txfs = "主叫";
							 }else{
								 txfs = "被叫"; 
							 }
							
							 Map map2 = new HashMap();
							 map2.put("phone", login.getLoginName());
							 map2.put("cTime", DateUtils.StringToDate(startDate.substring(0, 4)+"-"+thsj, "yyyy-MM-dd HH:mm:ss"));
							 List list = mobileDetailService.getMobileDetailBypt(map2);
							if(list==null || list.size()==0){
								MobileDetail mDetail = new MobileDetail();
								UUID uuid = UUID.randomUUID();
								mDetail.setId(uuid.toString());
					        	mDetail.setcTime(DateUtils.StringToDate(startDate.substring(0, 4)+"-"+thsj, "yyyy-MM-dd HH:mm:ss"));
					        	if(bean!=null){
									 if(bean.getcTime().getTime()>=mDetail.getcTime().getTime()){
										return false;
									 }
								 }
					        	
					        	int times = 0;
								try{
									TimeUtil tunit = new TimeUtil();
									times = tunit.timetoint(thsc);
								}catch(Exception e){
									
								}		
					        	mDetail.setTradeAddr(txdd);
					        	mDetail.setTradeWay(txfs);
					        	mDetail.setRecevierPhone(dfhm);
					        	mDetail.setTradeTime(times);
					        	mDetail.setTradeType(txlx);
					        	//mDetail.setTaocan(tcyh);
					        	mDetail.setOnlinePay(new BigDecimal(thf));
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
	public void callHistory(String startDate, List<String> ms){
		try {
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			parseBegin(Constant.YIDONG);
			CHeader h= new CHeader(CHeaderUtil.Accept_,"https://service.cq.10086.cn/app?service=page/myYD.OrderQuery&listener=initPage",null,"service.cq.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			Map<String,String> param = new IdentityHashMap<String,String>();
			bean = mobileDetailService.getMaxTime(bean);
				String token =	jsmap.get("token").toString();
				param = new IdentityHashMap<String,String>();
				param.put("$FormConditional", "F");
				
				param.put("ENDDATE", "");
				param.put("Form0", "bsubmit1,$FormConditional");
				param.put("QUERY_TYPE", "2");
				param.put("SELECT_MONTH", startDate);
				param.put(new String("SELECT_MONTHVAl"),ms.get(0));
				param.put(new String("SELECT_MONTHVAl"),ms.get(1));
				param.put(new String("SELECT_MONTHVAl"),ms.get(2));
				param.put(new String("SELECT_MONTHVAl"),ms.get(3));
				param.put(new String("SELECT_MONTHVAl"),ms.get(4));
				param.put(new String("SELECT_MONTHVAl"),ms.get(5));
				param.put("STARTDATE", "");
				param.put("infoType", "10");
				param.put("service", "direct/1/myYD.OrderQuery/Form1");
				param.put("sp", "S0");
				param.put("token", token);
				String text = cutil.post("http://service.cq.10086.cn/app?service=page/myYD.OrderQuery&listener=detailQuery", h,param);
				Document doc = Jsoup.parse(text);
				Elements tokens = doc.select("input[id=token]");
				if(tokens.size() > 0){
					token = tokens.get(0).attr("value");
					jsmap.put("token", token);
				}
				
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
//						break;//数据库中已有数据
					}
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
			
		
	}
	
	
	public boolean  messageHistory_parse(String text,MobileMessage bean,String startDate){
		boolean b = true;
		try {
//			if(text.contains("通信地点")&&text.contains("对方号码")){
			List<MobileMessage> messageList = new ArrayList<MobileMessage>();
			MobileMessage md = new MobileMessage();
			md.setPhone(login.getLoginName());
			md = mobileMessageService.getMaxSentTime(md.getPhone());
				Document doc  = Jsoup.parse(text);
				 Elements tables = doc.select("table[class=black font12 ywTable]");
				 if(tables.size()>0){
					 Element table1 =  tables.get(0);
					 Elements trs  = table1.select("tr");
					 for (int j = 1; j < trs.size(); j++) {
						 Elements tds =trs.get(j).select("td");
						 if(tds.size()>6){
							 	String qssj = tds.get(0).text();
								String txdd = tds.get(1).text();
								String dfhm = tds.get(2).text();
								String txfs = tds.get(3).text();
//								String xxlx = tds.get(4).text();
								String ywmc = tds.get(5).text();
//								String tcyh = tds.get(6).text();
								String fee = tds.get(6).text();
								MobileMessage mbmessage = new MobileMessage();

								mbmessage.setSentTime(DateUtils.StringToDate(Calendar.getInstance().get(Calendar.YEAR) + "-" + qssj, "yyyy-MM-dd HH:mm:ss"));

								mbmessage.setSentAddr(txdd);
								mbmessage.setTradeWay(txfs);
								mbmessage.setRecevierPhone(dfhm);
								mbmessage.setAllPay(new BigDecimal(fee));
								mbmessage.setPhone(login.getLoginName());
								messageList.add(mbmessage);
						 }
					 }
					 saveMobileMessage(messageList);
//					 mobileMessageService.insertbatch(messageList);
				  }
//			   }
			
			
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	/**
	 * 查询短信记录
	 */
	public void messageHistory(String startDate, List<String> ms){
		try {
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			parseBegin(Constant.YIDONG);
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://service.cq.10086.cn/app?service=page/myYD.OrderQuery&listener=initPage",null,"service.cq.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileMessage bean = new MobileMessage();
			bean.setPhone(login.getLoginName());
			Map<String,String> param = new IdentityHashMap<String,String>();
			bean = mobileMessageService.getMaxSentTime(bean.getPhone());
				String token =	jsmap.get("token").toString();
				//service=direct%2F1%2FmyYD.OrderQuery%2FForm1&sp=S0&Form0=bsubmit1%2C%24FormConditional&%24FormConditional=F&token=jrut&QUERY_TYPE=2&SELECT_MONTH=201409&SELECT_MONTHVAl=201409&SELECT_MONTHVAl=201408&SELECT_MONTHVAl=201407&SELECT_MONTHVAl=201406&SELECT_MONTHVAl=201405&SELECT_MONTHVAl=201404&STARTDATE=&ENDDATE=&infoType=12
				param = new IdentityHashMap<String,String>();
				
				param.put("service", "direct/1/myYD.OrderQuery/Form1");
				param.put("sp", "S0");
				param.put("Form0", "bsubmit1,$FormConditional");
				param.put("$FormConditional", "F");
				param.put("token", token);
				param.put("QUERY_TYPE", "2");
				param.put("SELECT_MONTH", startDate);
				param.put(new String("SELECT_MONTHVAl"),ms.get(0));
				param.put(new String("SELECT_MONTHVAl"),ms.get(1));
				param.put(new String("SELECT_MONTHVAl"),ms.get(2));
				param.put(new String("SELECT_MONTHVAl"),ms.get(3));
				param.put(new String("SELECT_MONTHVAl"),ms.get(4));
				param.put(new String("SELECT_MONTHVAl"),ms.get(5));
				param.put("STARTDATE", "");
				param.put("ENDDATE", "");
				param.put("infoType", "12");

				String text = cutil.post("http://service.cq.10086.cn/app?service=page/myYD.OrderQuery&listener=detailQuery",h,param);
				Document doc = Jsoup.parse(text);
				Elements tokens = doc.select("input[id=token]");
				if(tokens.size() > 0){
					token = tokens.get(0).attr("value");
					jsmap.put("token", token);
				}
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
//						break;//数据库中已有数据
					}
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
			
		
	}
	/**
	 * 查询流量
	 */
	public void online(String startDate, List<String> ms,MobileOnlineList bean_List,MobileOnlineBill bean_Bill,int iscm1){
		try {
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			parseBegin(Constant.YIDONG);
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://service.cq.10086.cn/app?service=page/myYD.OrderQuery&listener=initPage",null,"service.cq.10086.cn",true);
			boolean b = false;
			int num = 0;
			
			Map<String,String> param = new IdentityHashMap<String,String>();
			
				String token =	jsmap.get("token").toString();
				param = new IdentityHashMap<String,String>();
				param.put("$FormConditional", "T");
				param.put("$FormConditional$0", "F");
				param.put("$FormConditional$1", "F");
				param.put("$FormConditional$2", "F");
				param.put("$FormConditional$4", "F");
				param.put("$FormConditional$6", "T");
				param.put("$FormConditional$7", "F");
				param.put("$FormConditional$8", "F");
				param.put("$FormConditional$9", "F");
				param.put("ENDDATE","");
				param.put("Form0", "bsubmit1,$FormConditional,$FormConditional$0,$FormConditional$1,$FormConditional$2,$FormConditional$4,$FormConditional$6,$FormConditional$7,$FormConditional$8,$FormConditional$9");
				param.put("QUERY_TYPE", "2");
				param.put("SELECT_MONTH", startDate);
				param.put(new String("SELECT_MONTHVAl"),ms.get(0));
				param.put(new String("SELECT_MONTHVAl"),ms.get(1));
				param.put(new String("SELECT_MONTHVAl"),ms.get(2));
				param.put(new String("SELECT_MONTHVAl"),ms.get(3));
				param.put(new String("SELECT_MONTHVAl"),ms.get(4));
				param.put(new String("SELECT_MONTHVAl"),ms.get(5));
				param.put("STARTDATE", "");
				param.put("infoType", "11");
				param.put("service", "direct/1/myYD.OrderQuery/Form1");
				param.put("sp", "S0");
				param.put("token", token);

				String text = cutil.post("http://service.cq.10086.cn/app?service=page/myYD.OrderQuery&listener=detailQuery",h,param);
				System.out.println(text);
				Document doc = Jsoup.parse(text);
				Elements tokens = doc.select("input[id=token]");
				if(tokens.size() > 0){
					token = tokens.get(0).attr("value");
					jsmap.put("token", token);
				}
				try {
					b= onlineList_parse(text,bean_List,startDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					b= onlineBill_parse(text,bean_Bill,startDate,iscm1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
//						break;//数据库中已有数据
					}
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}
	
	/**
	 * 解析流量详单
	 */
	public boolean  onlineList_parse(String text,MobileOnlineList bean_List,String startDate){
		boolean b = true;
		try {
			String year = startDate.substring(0,4);
			if(text.contains("起始时间")&&!text.contains("用户的详单查询月份小于用户的开户月份")){
				Document doc6 = Jsoup.parse(text);
				Element table =doc6.select("table[class=black font12 ywTable]").get(1);
						Elements trs = table.select("tbody>tr");
						//System.out.println(trs.size());
						for(int j = 0 ; j<trs.size();j++){
							Elements tds = trs.get(j).select("td");

							Date cTime = null;
							String tradeAddr = null;
							String onlineType = null;
							long onlineTime = 0;
							long totalFlow = 0;
							String cheapService = null;
							BigDecimal communicationFees = new BigDecimal(0);
							try {
								String cTime1 = tds.get(0).text();
								cTime = DateUtils.StringToDate(year+"-"+cTime1, "yyyy-MM-dd HH:mm:ss");
								tradeAddr = tds.get(1).text();
								onlineType = tds.get(2).text();
								String onlineTime1 = tds.get(3).text();
								
								onlineTime = StringUtil.flowTimeFormat(onlineTime1);
								String totalFlow1 = tds.get(4).text();
								totalFlow = new Long(totalFlow1);
								String communicationFees1 = tds.get(6).text();// 通信费
								communicationFees = new BigDecimal(communicationFees1);
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
									 if(bean_List.getcTime().getTime()<onlineList.getcTime().getTime()){
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
	
	/**
	 * 解析流量账单
	 */
	public boolean  onlineBill_parse(String text,MobileOnlineBill bean_Bill,String startDate,int iscm1){
		boolean b = true;
		try {
			if(text.contains("起始时间")&&!text.contains("用户的详单查询月份小于用户的开户月份")){
				Document doc6 = Jsoup.parse(text);
				Element table =doc6.select("table[class=black font12 ywTable]").get(0);

							Date monthly = null;
							long totalFlow = 0;
							long freeFlow = 0;
							long chargeFlow = 0;
							BigDecimal communicationFees = new BigDecimal(0);
							int iscm = 0;
							try {
								monthly = DateUtils.StringToDate(startDate,"yyyyMM");
								String totalFlow1 = table.select("table>tr:eq(1)>td:eq(1)").text().replace("gprs流量：","").replace(".00","").trim();
								totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								String trafficCharges = table.select("tbody>tr:eq(1)>td:eq(2)").text().replace("gprs总费用：","").replace("元", "").trim();
								communicationFees = new BigDecimal(trafficCharges);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							MobileOnlineBill onlineBill = new MobileOnlineBill();
							UUID uuid = UUID.randomUUID();
							onlineBill.setId(uuid.toString());
							onlineBill.setMonthly(monthly);
				        	if(bean_Bill!=null){
								 if(bean_Bill.getMonthly().getTime()<onlineBill.getMonthly().getTime()){
									onlineBill.setChargeFlow(chargeFlow);
						        	onlineBill.setFreeFlow(freeFlow);
						        	onlineBill.setTotalFlow(totalFlow);
									onlineBill.setTrafficCharges(communicationFees);
						        	onlineBill.setPhone(login.getLoginName()); 
						        	if(iscm1==5) {
						        		iscm = 1;
						        	}
						        	onlineBill.setIscm(iscm);
									mobileOnlineBillService.save(onlineBill);
								 }
							 }
				        	onlineBill.setChargeFlow(chargeFlow);
				        	onlineBill.setFreeFlow(freeFlow);
				        	onlineBill.setTotalFlow(totalFlow);
							onlineBill.setTrafficCharges(communicationFees);
				        	onlineBill.setPhone(login.getLoginName()); 
				        	if(iscm1==5) {
				        		iscm = 1;
				        	}
				        	onlineBill.setIscm(iscm);
							mobileOnlineBillService.save(onlineBill);
					   	
					}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}

	private String replace(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 查询通话记录
	 */
	
	public void getTelDetailHtml() {
		
		try {
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,null,null,"service.cq.10086.cn",true);
			parseBegin(Constant.YIDONG);
			for (String startDate : ms) {
				
				String text = cutil.get("http://service.cq.10086.cn/app?service=page/operation.AJAXMyBill&listener=makePieChar&qtime=0.2950844530650206&month="+startDate,h);
			
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
			text = StringEscapeUtils.unescapeHtml3(text);
			if(text!=null &&text.contains("套餐及固定费")){
				BigDecimal total=new BigDecimal(0);
				BigDecimal tcgdf=new BigDecimal(0);
				BigDecimal tcwyyf=new BigDecimal(0);
				
				BigDecimal tcwdxf=new BigDecimal(0);
				
				
			
				JSONObject json=new JSONObject(text);
				JSONArray jsonDetail=json.getJSONArray("costDataset");
				
				for(int i=0;i<jsonDetail.length();i++){  
					JSONObject  jsonobject = jsonDetail.getJSONObject(i);
					String fname = jsonobject.getString("FEENAME");
					String fee = jsonobject.getString("FEE");
					if(fname.contains("套餐及固定费")){
						tcgdf= new BigDecimal(fee).divide(new BigDecimal("100"));
					}else if(fname.contains("套餐外语音通信费")){
						tcwyyf= new BigDecimal(fee).divide(new BigDecimal("100"));
					}else if(fname.contains("套餐外短彩信费")){
						tcwdxf= new BigDecimal(fee).divide(new BigDecimal("100"));
					}else if(fname.contains("消费合计")){
						total= new BigDecimal(fee).divide(new BigDecimal("100"));
					}
				}
				
				
					Map map2 = new HashMap();
					 map2.put("phone", login.getLoginName());
					 map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
					 List list = mobileTelService.getMobileTelBybc(map2);
					if(list==null || list.size()==0){
						MobileTel mobieTel = new MobileTel();
						UUID uuid = UUID.randomUUID();
						mobieTel.setId(uuid.toString());
//						mobieTel.setBaseUserId(currentUser);
						mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
						// mobieTel.setcName(cName);
						mobieTel.setTeleno(login.getLoginName());
						// mobieTel.setBrand(brand);
						mobieTel.setTcwyytxf(tcwyyf);
						mobieTel.setTcgdf(tcgdf);
						mobieTel.setTcwdxf(tcwdxf);
						String year = startDate.substring(0, 4);
						String mouth = startDate.substring(4, 6);
						mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
					
						mobieTel.setcAllPay(total);
						
						mobileTelService.saveMobileTel(mobieTel);
					}
				}
			
			
			
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
	
		return b;
	}
	public static void main(String[] args) {
		Login login = new Login();
		login.setLoginName("18302369208");
		login.setPassword("343179");
		CQYidong hb = new CQYidong(login,"11111111111");
		//hb.index();
		//hb.getImg();
		hb.index();
		hb.inputCode(hb.getImgUrl());
		hb.login();
		//hb.getYuE();
		//hb.sendPhoneDynamicsCode();
		////system.out.println("请输入动态码：");
		//String authcode = in.nextLine();
		
		//hb.getYuE();
		//hb.saveMonthTel();
		//hb.saveDetailTel(authcode);
		hb.getMyInfo();
		hb.close();
	}
}
