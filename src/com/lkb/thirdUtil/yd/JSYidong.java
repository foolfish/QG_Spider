package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.serviceImp.MobileOnlineListServiceImpl;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.entity.SpeakBillPojo;

/**
 * 江苏移动
 * 
 * @author Administrator 问题：登陆的时候第一次输入验证码，总会提示验证码输入有错（浏览器登陆）
 * 
 */
public class JSYidong extends BaseInfoMobile{
	/*
	 * reqUrl:queryBillDetail busiNum:QDCX queryMonth:201406 queryItem:1
	 * operType:3 smsNum:405831 confirmFlg:1
	 */
	// 获取关键参数的url（cookie中的参数可用作验证码获取地址的参数和登陆post的参数）
	private static String theKeyArgURL = "https://js.ac.10086.cn/jsauth/dzqd/addCookie";
	// 获取验证码url
	private String imgurl = "https://js.ac.10086.cn/jsauth/dzqd/zcyzm?t=new&ik=l_image_code&l_key=";
	private String ly_key = "jsyidong_ly_key";
	/**登陆时把个人信息保存了*/
	private String info = "jsyidong_info";
	// 登陆post请求
	private static String loginPostURL = "https://js.ac.10086.cn/jsauth/dzqd/popDoorPopLogonServletNewNew";
	// 发送动态密码,30秒可以再次发送，5分钟内动态验证码是有效的
	private static String sendDynamicPWDURL = "http://service.js.10086.cn/sms.do";
	// 获取通话详单,需要动态密码
	private static String mobileDetailURL = "http://service.js.10086.cn/actionDispatcher.do";
	/**
	 * mobile:15851996344 password:199131 loginType:wsyyt icode:6guu
	 * fromFlag:doorPage l_key:GUV2ID4SVCB9NMAQE5TL772MMPO7NK5R activepass:
	 * wttype:2
	 */

	public JSYidong(Login login,String currentUser){
		super(login,ConstantNum.comm_js_yidong,currentUser);
		userSource = Constant.YIDONG;
	}
	
	/**
	 * 保存每月通話記錄
	 * */
	public  void callHistory(){
		List<String> ms =  DateUtils.getMonthForm(6,"yyyyMM");
		MobileDetail md = new MobileDetail();
		md.setPhone(login.getLoginName());
		md = mobileDetailService.getMaxTime(md);
		boolean b = true;
		for(int m=0;m<ms.size();m++){
			String month = ms.get(m);
			String content = getEveryMonthPhone(month,login.getPhoneCode());
			if(content!=null&&content.contains("成功")){
				JSONObject json;
				try {
					json = new JSONObject(content);
					String resultObj = json.get("resultObj").toString();
					JSONObject resultjson = new JSONObject(resultObj);
					
					String qryResult = resultjson.get("qryResult").toString();
					JSONObject qryResultjson = new JSONObject(qryResult);
					
					String gsmBillDetail = qryResultjson.get("gsmBillDetail").toString();
					JSONArray arr=new JSONArray(gsmBillDetail);
					
					for(int i=0;i<arr.length();i++){
						String message = arr.get(i).toString();
						JSONObject messagejson = new JSONObject(message);
						String statusType = messagejson.getString("statusType");//主叫还是被叫
						if(statusType.contains("主叫")||statusType.contains("被叫")){
							String tradeAddr = messagejson.getString("visitArear");//北京
							String startTime = messagejson.getString("startTime");//20140625210233
							
							Date times=DateUtils.StringToDate( startTime, "yyyyMMddHHmmss");
							if(md!=null&&md.getcTime()!=null){
								if(times.getTime()<=md.getcTime().getTime()){
									b =false; 
									break;
								}	
							}
							String tradeWay = "被叫";
							if(statusType.contains("主叫")){
								tradeWay = "主叫";
							}
							String receverPhone = messagejson.getString("otherParty");//对方电话
							String callDuration = messagejson.getString("callDuration");//时间
							int tradeTime = Integer.parseInt(callDuration);
							String realCfee = messagejson.getString("realCfee");//
							BigDecimal onlinepay = new BigDecimal(realCfee);
							onlinepay = onlinepay.divide(new BigDecimal("100"),2, BigDecimal.ROUND_UNNECESSARY);
						
							MobileDetail mDetail = new MobileDetail();
							UUID uuid = UUID.randomUUID();
							mDetail.setId(uuid.toString());
							mDetail.setcTime(times);
							mDetail.setTradeAddr(tradeAddr);
							mDetail.setTradeWay(tradeWay);
							mDetail.setRecevierPhone(receverPhone);
							mDetail.setTradeTime(tradeTime);							
							mDetail.setOnlinePay(onlinepay);
							mDetail.setPhone(login.getLoginName());							
							mDetail.setIscm(0);
							mobileDetailService.saveMobileDetail(mDetail);
							}
						}
					if(!b){
						break;
					}
				} catch (Exception e) {
					writeLogByHistory(e);
				}
			}
			
		}
	}
	
	/*
	 * 查询每月通话记录
	 * */
	private  String getEveryMonthPhone(String month,String smsNum) {
		/**
		 * 获取通话详单
		 */
		String url = "http://service.js.10086.cn/actionDispatcher.do";
		Map<String,String> param = new HashMap<String,String>();
		// 准备post请求的参数,发送登陆请求
		param.put("confirmFlg", "1");
		param.put("operType", "3");
		param.put("queryItem", "1");
		param.put("queryMonth", month);
		param.put("reqUrl", "queryBillDetail");
		param.put("smsNum", smsNum);
		return cutil.post(url,new CHeader("http://service.js.10086.cn/index.jsp#QDCX"),param);
	}
	
//	public String getHistoryHuafei(String currentMonth) {
//		String url = "http://service.js.10086.cn/actionDispatcher.do";
//		CHeader h = new CHeader("http://service.js.10086.cn/#ZDCX");
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("reqUrl", "ZDCX");
//		param.put("busiNum", "ZDCX");
//		param.put("methodName", "getMobileRealTimeBill");
//		param.put("isFamily", "0");
//		param.put("userMobile", "");
//		param.put("userName", "");
//		param.put("beginDate", currentMonth);
//		return cutil.post(url,h,param);
//	}
	
	/*
	 * 获取每个月的话费消费金额
	 * */
	public Map<String,String> everyMonthPay(String content){
		Map<String,String> map = new HashMap<String,String>();
		String totalFee="0";
		JSONObject json;
		try {
			json = new JSONObject(content);
			String resultObj = json.get("resultObj").toString();
			JSONObject resultjson = new JSONObject(resultObj);
			
			String feeTime = resultjson.getString("feeTime");
			String billBal = resultjson.get("billRet").toString();
			JSONObject billBaljson = new JSONObject(billBal);
			totalFee = billBaljson.get("totalFee").toString();
			
			//System.out.println(feeTime+":"+totalFee);
			map.put("feeTime", feeTime);
			map.put("ducFee", totalFee.trim());
		} catch (JSONException e) {
			writeLogByZhangdan(e);
		}
		return map;
	
	}
	
	/**32位随机数*/
	public String generateMixed() {
		char c[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
				'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
				'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		String res = "";
		Random r = new Random();
		for ( int i = 0; i < 32; i++) {
			res += c[r.nextInt(32)];
		}
		return res;
	}
	public static void main(String[] args) {
		JSYidong js = new JSYidong(new Login("18361017664","888666"),null);
//		JSYidong js = new JSYidong(new Login("13913954116","121120"),null);
		js.index();
		js.inputCode(js.getImgUrl());
		js.login();
		js.close();
		if(js.islogin()){
			js.gatherMonthlyBill();
		}
		
	}
	
//	public Map<String,Object> index(){
//		map.put("url",getAuthcode());
//		return map;
//	}
	public void init(){
		if(!isInit()){
			if(cutil.post("https://js.ac.10086.cn/jsauth/dzqd/addCookie",null)!=null){
				 String s = generateMixed();
				 redismap.put(ly_key, s);
				 setImgUrl(imgurl+s);
				 setInit();
			}
		}
	}
	/**第一次post*/
	public  Map<String,Object> login(){
		try{
		Map<String,String> param = new HashMap<String,String>();
		param.put("activepass", "");
		param.put("fromFlag", "doorPage");
		param.put("icode", login.getAuthcode());
		String l_key = (String)redismap.get(ly_key);
		param.put("l_key", l_key);
		param.put("mobile", login.getLoginName());
		param.put("loginType", "wsyyt");
		param.put("password", login.getPassword());
		param.put("wttype", "2");
		CHeader c = new CHeader();
		c.setContent_Type("application/x-www-form-urlencoded;charset=UTF-8");
		c.setReferer("https://js.ac.10086.cn/jsauth/dzqd/mh/index.html");
		String text = cutil.post(loginPostURL,c, param);
		// 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断登录是否成功
		 // 返回-6表示验证码为空，-8表示验证码错误或者post参数不正确(参数为空也会),0000表示正常登陆
		// 验证是否登录成功的方法 {rcode:"0000"}
		if (text != null && text.endsWith("")) {
			String s = new RegexPaserUtil("code:\"", "\"",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
			if(s!=null) 	error_text(s);
			if(errorMsg==null){
//				cutil.get("http://service.js.10086.cn/");
				c = new CHeader("http://service.js.10086.cn/#home");
				text = cutil.get("http://service.js.10086.cn/pages/GRZLGL_GRZL.jsp",c);
				if(text!=null){
					RegexPaserUtil rp = new RegexPaserUtil("window.top.BmonPage.commonBusiCallBack\\(", ", 'GRZLGL_GRZL'",text,RegexPaserUtil.TEXTEGEXANDNRT);
					text = rp.getText();
					if(text!=null){
						if(text.contains("errorCode\":\"0000\"")){
//							//System.out.println(getCookiesString());
							addCookie("jscmSSOCookie", getJsessionId("cmtokenid","10086.cn"));
							redismap.put(info, text);
							//System.out.println(text);
							loginsuccess();	
						}
					}
				}
			}
		}
		if(status==1){
        	addTask(this);
//        	sendPhoneDynamicsCode();
    	}
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;
	}
	/**
	 * 生成短信
	 * */
	public Map<String,Object> sendPhoneDynamicsCode() {
//		checkPhoneDynamicsCode();
		
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		int status = 0;
		Map<String,String> param = new HashMap<String,String>();
		param.put("busiNum", "QDCX");
//		param.put("mobile", login.getLoginName());
		param.put("mobile","undefined");
		String text = cutil.post(sendDynamicPWDURL,new CHeader("http://service.js.10086.cn/index.jsp"),param);
		if(text!=null){
			String s = new RegexPaserUtil("resultCode\":\"", "\"",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
			//System.out.println(s);
			if(s!=null&&s.equals("0")) {
				status = 1;
				errorMsg = "发送成功,请注意查收!";
			}
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg==null?"发送失败!":errorMsg);
		return map;
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
				saveTel();
				getMyInfo(); //个人信息
				break;
			case 2:
				callHistory(); //历史账单	
				getSmsLog();//短信記錄
				getNetFlow();//流量记录
				break;
			case 3:
				saveTel();
				getMyInfo(); //个人信息
				callHistory(); //通话记录	
				getSmsLog();//短信記錄
				getNetFlow();//流量记录
				break;
			default:
				break;
			}
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}
	
	/**
	 * 抓取用户短信的方法
	 * **/
	public void getSmsLog()
	{
		List<String> ms =  DateUtils.getMonthForm(6,"yyyyMM");
		MobileDetail md = new MobileDetail();
		Date lastTime=null;
		try{
			MobileMessage MobileMessage =  mobileMessageService.getMaxSentTime(login.getLoginName());
			
			if(MobileMessage!=null){
				 lastTime = mobileMessageService.getMaxSentTime(login.getLoginName()).getSentTime();
			}
		   
		}
		catch(Exception e)
		{
			writeLogByMessage(e);
		}
		for(int m=0;m<ms.size();m++){
			String month = ms.get(m);
			String url = "http://service.js.10086.cn/actionDispatcher.do";
			Map<String,String> param = new HashMap<String,String>();
			// 准备post请求的参数,发送登陆请求
			param.put("confirmFlg", "1");
			param.put("operType", "3");
			param.put("queryItem", "6");
			param.put("queryMonth", month);
			param.put("reqUrl", "queryBillDetail");
			param.put("smsNum", login.getPhoneCode());
			String content = cutil.post(url,new CHeader("http://service.js.10086.cn/index.jsp#QDCX"),param);
			if(content!=null&&content.contains("成功")){
				JSONObject json;
				try 
				{
					json = new JSONObject(content);
					String resultObj = json.get("resultObj").toString();
					JSONObject resultjson = new JSONObject(resultObj);					
					String qryResult = resultjson.get("qryResult").toString();
					JSONObject qryResultjson = new JSONObject(qryResult);					
					String smsBillDetail = qryResultjson.get("smsBillDetail").toString();
					JSONArray arr=new JSONArray(smsBillDetail);				
					for(int i=0;i<arr.length();i++){
						String message = arr.get(i).toString();
						JSONObject messagejson = new JSONObject(message);
						String recevierPhone = messagejson.getString("otherParty");//對方電話號
						String startTime = messagejson.getString("startTime");//發送日期，原格式为20140901150451
						Date times=null;
						if(startTime.length()==14)
						{
							try{
								times=DateUtils.StringToDate( startTime, "yyyyMMddHHmmss");								
								if(lastTime!=null&&times!=null){
									if(times.getTime()<=lastTime.getTime()){
										continue;
									}	
								}
							}
							catch(Exception e)
							{
								log.error("URL:"+url, e);
								e.printStackTrace();
								continue;
							}
						}
						else continue;
						String allPay = messagejson.getString("totalFee");//費用
						String tradeWay = messagejson.getString("statusType").trim();//通信方式
						if(tradeWay.equals("短信发")) tradeWay = "发送";
						else tradeWay = "接收";
						String sentAddr = messagejson.getString("visitArear");//通信地点
						String phone = login.getLoginName();//本机号码						
						 MobileMessage sms = new MobileMessage();
						 sms.setAllPay(new BigDecimal(allPay));
						 sms.setRecevierPhone(recevierPhone);
						 sms.setSentAddr(sentAddr);
						 sms.setSentTime(times);
						 sms.setTradeWay(tradeWay);
						 sms.setPhone(phone);
						 sms.setCreateTs(new Date());
						 UUID uuid = UUID.randomUUID();
						 sms.setId(uuid.toString());
						 mobileMessageService.save(sms);
					}
				}
				catch (JSONException e) {
					writeLogByMessage(e);
				}				
			}
			
		}
	}
	
	/**
	 * 抓取用户流量记录
	 */
	public void getNetFlow()
	{
		Date lasttime=null;
		if(mobileOnlineListService.getMaxTime(login.getLoginName())!=null)
		{
			lasttime = mobileOnlineListService.getMaxTime(login.getLoginName()).getcTime();
		}
		
		MobileOnlineBill bean_bill = mobileOnlineBillService.getMaxTime(login.getLoginName());
		
		List<String> ms =  DateUtils.getMonthForm(6,"yyyyMM");
		for(int m=0;m<ms.size();m++){
			String month = ms.get(m);
			Map<String,String> param = new HashMap<String,String>();
			// 准备post请求的参数,发送登陆请求
			//reqUrl=queryBillDetail&busiNum=QDCX&queryMonth=201409&queryItem=8&operType=3
			String url = "http://service.js.10086.cn/actionDispatcher.do";
			param.put("confirmFlg", "1");
			param.put("operType", "3");
			param.put("queryItem", "8");
			param.put("queryMonth", month);
			param.put("reqUrl", "queryBillDetail");
			param.put("smsNum", login.getPhoneCode());
			String content = cutil.post(url,new CHeader("http://service.js.10086.cn/index.jsp#QDCX"),param);
//			System.out.println(content);
			if(content!=null&&content.contains("成功")){
				JSONObject json;
				try 
				{
					json = new JSONObject(content);
					String resultObj = json.get("resultObj").toString();
					JSONObject resultjson = new JSONObject(resultObj);					
					String qryResult = resultjson.get("qryResult").toString();
					JSONObject qryResultjson = new JSONObject(qryResult);
					String gprsBillDetail = qryResultjson.get("gprsBillDetail").toString();
					JSONArray arr=new JSONArray(gprsBillDetail);	
					List<MobileOnlineList> mobileOnlineList = new LinkedList<MobileOnlineList>();
					
					long totalFlows = 0;
					BigDecimal totalFees = new BigDecimal(0.0);
					
					for(int i=0;i<arr.length();i++){
						String dataflowlist = arr.get(i).toString();
						JSONObject ljson = new JSONObject(dataflowlist);
						String starttime = ljson.get("startTime").toString().trim();
						if(starttime.length()!=14){
							continue;
						}
						String allTotalFee = ljson.get("totalFee").toString().trim();
						String area = ljson.get("visitArear").toString();
						String duration = ljson.get("duration").toString();
						String accessPoint = ljson.get("cdrApnni").toString();
						String statusType = ljson.get("statusType").toString();
						String data = ljson.get("busyData").toString();
						String cheapService = ljson.get("msnc").toString();
						
						accessPoint = accessPoint.replace("手机", "").replace("IPV4", "").replace("IPV6", "").trim();
						
						long onlinetime=0;
						BigDecimal communicationFees=new BigDecimal("0.0");
						long totalFlow=0;
						Date times=null;
						try{
							onlinetime = Long.parseLong(duration.trim());
							communicationFees = new BigDecimal(allTotalFee.trim());
							communicationFees = communicationFees.divide(new BigDecimal(100));
							totalFlow = Long.parseLong(data);
							totalFlows = totalFlows + totalFlow;
							totalFees = totalFees.add(communicationFees);
							times = DateUtils.StringToDate(starttime,
									"yyyyMMddHHmmss");
							if (lasttime != null && times != null) {
								if (times.getTime() <= lasttime.getTime()) {
									continue;
								}
							}
						}
						catch (Exception e) {
							log.error("URL:"+url, e);
							writeLogByFlowBill(e);
						}	
						
						MobileOnlineList datalist = new MobileOnlineList();
						datalist.setcTime(times);
						UUID uuid = UUID.randomUUID();
						datalist.setId(uuid.toString());
						datalist.setOnlineTime(onlinetime);
						datalist.setPhone(login.getLoginName());
						datalist.setOnlineType(accessPoint);
						datalist.setTotalFlow(totalFlow);
						datalist.setTradeAddr(area);
						datalist.setCommunicationFees(communicationFees);
						datalist.setCheapService(cheapService);
						
						if(datalist!=null)
						{
							mobileOnlineList.add(datalist);	
						}
					}
					
					//账单
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					Date monthly = DateUtils.StringToDate(month, "yyyyMM");
					map.put("phone", login.getLoginName());
					map.put("monthly", monthly);
					List<Map> list = mobileOnlineBillService
							.getMobileOnlineBill(map);
					
					if (list == null || list.size() == 0) {
						MobileOnlineBill onlineBill = new MobileOnlineBill();
						UUID uuid = UUID.randomUUID();
						onlineBill.setId(uuid.toString());
						onlineBill.setTotalFlow(totalFlows);
						onlineBill.setTrafficCharges(totalFees);
						onlineBill.setPhone(login.getLoginName());
						onlineBill.setIscm(0);
						onlineBill.setMonthly(monthly);
						if (bean_bill != null && bean_bill.getMonthly() != null) {
							if (bean_bill.getMonthly().getTime() > onlineBill
									.getMonthly().getTime()) {
								continue;
							} else if(bean_bill.getMonthly().getTime() == onlineBill
									.getMonthly().getTime()){
								mobileOnlineBillService.update(onlineBill);
							} else {
								mobileOnlineBillService.save(onlineBill);
							}
						} else {
							mobileOnlineBillService.save(onlineBill);
						}
					}
					
					
					try
					{
						if(mobileOnlineList!=null&&mobileOnlineList.size()>0)
						{
							mobileOnlineListService.insertbatch(mobileOnlineList);
						}
					}
					catch (Exception e) {
						log.error("URL:"+url, e);
						writeLogByFlowList(e);
					}	
				}
				catch (JSONException e) {
					log.error("URL:"+url, e);
					writeLogByFlowList(e);
					continue;
				}	
			} else {
				
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				Date monthly = DateUtils.StringToDate(month, "yyyyMM");
				map.put("phone", login.getLoginName());
				map.put("monthly", monthly);
				List<Map> list = mobileOnlineBillService
						.getMobileOnlineBill(map);
				
				if (list == null || list.size() == 0) {
					MobileOnlineBill onlineBill = new MobileOnlineBill();
					UUID uuid = UUID.randomUUID();
					onlineBill.setId(uuid.toString());
					onlineBill.setTotalFlow((long) 0);
					onlineBill.setTrafficCharges(new BigDecimal(0.00));
					onlineBill.setPhone(login.getLoginName());
					onlineBill.setIscm(0);
					onlineBill.setMonthly(monthly);
					if (bean_bill != null && bean_bill.getMonthly() != null) {
						if (bean_bill.getMonthly().getTime() > onlineBill
								.getMonthly().getTime()) {
							continue;
						} else if(bean_bill.getMonthly().getTime() == onlineBill
								.getMonthly().getTime()){
							mobileOnlineBillService.update(onlineBill);
						} else {
							mobileOnlineBillService.save(onlineBill);
						}
					} else {
						mobileOnlineBillService.save(onlineBill);
					}
				}
				
			}
		}
		
	}
	
	/** 查询个人信息*/
	public boolean getMyInfo() {
		String url = "http://service.js.10086.cn/pages/ZHYEJYXQ.jsp?r="+Math.random();
		String content  = cutil.get(url);
		RegexPaserUtil rp = new RegexPaserUtil("\"balance\":\"","元\"",content,RegexPaserUtil.TEXTEGEXANDNRT);
		content = rp.getText();
		Double yue = null;
		if(content!=null){
			try{
				yue = Double.parseDouble(content.trim());
			}catch(Exception e1){
				yue = 0.0;
			}
		}
		String text = (String)redismap.get(info);
		//System.out.println(text);
		if(text!=null){
			try {
				JSONObject json = new JSONObject(text);
				Map<String, String> map = new HashMap<String, String>(3);
				map.put("parentId", currentUser);
				map.put("loginName", login.getLoginName());
				map.put("usersource", Constant.YIDONG);
				List<User> list = userService.getUserByParentIdSource(map);
				JSONObject ja = json.getJSONObject("resultObj");
				if (list != null && list.size() > 0) {
					User user = list.get(0);
					user.setUserName(ja.getString("userName"));
					user.setRealName(ja.getString("userName"));
					user.setAddr(ja.getString("relAddr"));
					user.setUsersource(Constant.YIDONG);
					user.setUsersource2(Constant.YIDONG);
					System.out.println(ja);
					user.setEmail((String)ja.get("relEmail"));
					user.setParentId(currentUser);
					user.setIdcard(ja.getString("icNo"));
					user.setModifyDate(new Date());
					Date d = DateUtils.StringToDate(ja.getString("appDate"), "yyyy-MM-dd HH:mm:ss");
					user.setRegisterDate(d);
					user.setPhoneRemain(new BigDecimal(yue));
					user.setPhone(login.getLoginName());
					user.setLoginName(login.getLoginName());
					userService.update(user);
				} else {
					User user = new User();
					UUID uuid = UUID.randomUUID();
					//System.out.println("");
					user.setId(uuid.toString());
					user.setUserName(ja.getString("userName"));
					user.setRealName(ja.getString("userName"));
					user.setAddr(ja.getString("relAddr"));
					user.setUsersource(Constant.YIDONG);
					user.setUsersource2(Constant.YIDONG);
					user.setEmail(ja.getString("relEmail"));
					user.setParentId(currentUser);
					user.setIdcard(ja.getString("icNo"));
					user.setModifyDate(new Date());
					Date d = DateUtils.StringToDate(ja.getString("appDate"), "yyyy-MM-dd HH:mm:ss");
					user.setRegisterDate(d);
					user.setPhoneRemain(new BigDecimal(yue));
					user.setPhone(login.getLoginName());
					user.setLoginName(login.getLoginName());
					userService.saveUser(user);

				}
			} catch (JSONException e) {
				log.error("URL:"+url, e);
				e.printStackTrace();
				writeLogByInfo(e);
			}
			
			
		}
		return true;
	}
	
	/**
	 *账单信息
	 */
	public LinkedList<SpeakBillPojo> gatherMonthlyBill() {
		Map<String,String> param = new HashMap<String,String>();
		param.put("reqUrl", "ZDCX");
		param.put("busiNum", "ZDCX");
		param.put("methodName", "getMobileRealTimeBill");
		param.put("isFamily", "0");
		param.put("userMobile", "");
		param.put("userName", "");
		LinkedList<SpeakBillPojo> linkedList = getMonthlyBillAccess("http://service.js.10086.cn/actionDispatcher.do","beginDate",param,false,"yyyyMM");
		String content = null;
		SpeakBillPojo pojo = null;
		for (int i = 0; i < linkedList.size(); i++) {
			pojo = linkedList.get(i);
			content = pojo.getText();
			if(content!=null){
				if(content.contains("每月1号至3号为移动话费出账期，不可以查询上月账单，请于3号以后查询")){
					continue;
				}
				Map<String,String> maps = everyMonthPay( content);
				String feeTime = maps.get("feeTime");
				String ducFee = maps.get("ducFee");
				BigDecimal btotalFee = new BigDecimal(Double.parseDouble(ducFee)/100);
				MobileTel tel = new MobileTel();
				tel.setDependCycle(feeTime);
				tel.setcAllPay(btotalFee);		
				pojo.setMobileTel(tel);
				pojo.setText(null);
			}
		}
		return linkedList;
	}

	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
		String month = DateUtils.getMonths(1, "yyyyMM").get(0);
		Map<String,String> param = new HashMap<String,String>();
		// 准备post请求的参数,发送登陆请求
		param.put("confirmFlg", "1");
		param.put("operType", "3");
		param.put("queryItem", "1");
		param.put("queryMonth", month);
		param.put("reqUrl", "queryBillDetail");
//		param.put("busiNum", "QDCX");
		if(login.getPhoneCode()!=null){
			param.put("smsNum", login.getPhoneCode());	
		}
		//System.out.println(getCookiesString());
		CHeader c = new CHeader("http://service.js.10086.cn/index.jsp#QDCX");
		String tongHuaJson = cutil.post("http://service.js.10086.cn/actionDispatcher.do",c,param);
		if(tongHuaJson.contains("errorMessage\":\"成功")){
			status = 1;
		}else{
			errorMsg = "口令验证失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		if(status==1){
//        	addTask_2(this);
    	}
		return map;
	}
	/**通过返回code返回错误信息
	 * @param text
	 */
	private void error_text(String text){
		if(text.equalsIgnoreCase("0000")){
		}else if(text.equalsIgnoreCase("-1")||text.equalsIgnoreCase("-2")){
			errorMsg = "登陆失败!";
		}else if(text.equalsIgnoreCase("-3")){
			errorMsg = "来源渠道非法或为空!";
		}else if(text.equalsIgnoreCase("-4")){
			errorMsg = "手机号码格式不正确";
		}else if(text.equalsIgnoreCase("-5")){
			errorMsg = "密码位数不正确";
		}else if(text.equalsIgnoreCase("-6")){
			errorMsg = "验证码位数不正确";
		}else if(text.equalsIgnoreCase("-7")){
			errorMsg = "验证码失效";
		}else if(text.equalsIgnoreCase("-8")){
			errorMsg = "验证码错误";
		}else if(text.equalsIgnoreCase("-9")){
			errorMsg = "用户信息为空";
		}else if(text.equalsIgnoreCase("-11")){
			errorMsg = "验证码为空";
		}else if(text.equalsIgnoreCase("-12")){
			errorMsg = "用户IP在登录黑名单中，不允许登录！";
		}else if(text.equalsIgnoreCase("-13")){
			errorMsg = "用户手机号码在登录黑名单中，不允许登录！";
		}else if(text.equalsIgnoreCase("-2203")){
			errorMsg = "您输入的号码非江苏省归属，请切换至手机号码归属省登录！";
		}else if(text.equalsIgnoreCase("-2230")){
			errorMsg = "对不起,密码长度错误，请输入6位有效密码！";
		}else if(text.equalsIgnoreCase("-2231")){
			errorMsg = "对不起,密码包含非法字符，请重新输入！";
		}else if(text.equalsIgnoreCase("-1020")||text.equalsIgnoreCase("-2232")){
			errorMsg = "对不起,登录密码错误，请重新输入！";
		}else if(text.equalsIgnoreCase("-2303")){
			errorMsg = "尊敬的客户，由于您已经连续3次输入错误的服务密码，您账号的使用将受限，请于24小时后再次登录。不便之处，敬请谅解！";
		}else if(text.equalsIgnoreCase("-1010")){
			errorMsg = "此用户不存在,请确认后重新输入！";
		}else if(text.equalsIgnoreCase("-1060")){
			errorMsg = "抱歉，您的号码暂时无法登录，请联系10086。";
		}else if(text.equalsIgnoreCase("1001")){
			errorMsg = "手机号码或密码错误！";
		}else if(text.equalsIgnoreCase("1002")){
			errorMsg = "系统繁忙，请稍候再试！";
		}else if(text.equalsIgnoreCase("1002")){
			errorMsg = "动态密码无效！";
		}else if(text.equalsIgnoreCase("1004")){
			errorMsg = "割接错误！";
		}else if(text.equalsIgnoreCase("1005")){
			errorMsg = "请输入正确的手机号码，号码不是11位！";
		}else if(text.equalsIgnoreCase("1006")){
			errorMsg = "号码中间有非法字符！";
		}else if(text.equalsIgnoreCase("1007")){
			errorMsg = "密码中间有空格！";
		}else if(text.equalsIgnoreCase("1008")){
			errorMsg = "用户是黑名单用户！";
		}else if(text.equalsIgnoreCase("1009")){
			errorMsg = "验证码为空！";
		}else if(text.equalsIgnoreCase("1100")){
			errorMsg = "验证码格式不正确！";
		}else if(text.equalsIgnoreCase("1101")){
			errorMsg = "手机号码和密码为空！";
		}else if(text.equalsIgnoreCase("1102")){
			errorMsg = "SSO服务器检查用户登录信息失败！";
		}else if(text.equalsIgnoreCase("1103")){
			errorMsg = "httpClient问题！";
		}else if(text.equalsIgnoreCase("1104")){
			errorMsg = "动态密码输入错误超过3次，请重新获取！";
		}else if(text.equalsIgnoreCase("2004")){
			errorMsg = "30s内无法再次发送动态验证码，请稍后再试";
		}else if(text.equalsIgnoreCase("2005")){
			errorMsg = "对不起，您获取动态验证码超30次，请明天再试；";
		}else if(text.equalsIgnoreCase("3000")){
			errorMsg = "成功！";
		}else if(text.equalsIgnoreCase("3001")){
			errorMsg = "动态密码只支持江苏省移动用户！";
		}else if(text.equalsIgnoreCase("3002")){
			errorMsg = "尊敬的用户您好！您目前的号码处于停机状态，暂不能登录！";
		}else if(text.equalsIgnoreCase("3003")){
			errorMsg = "动态密码下发失败，请稍后再试！";
		}else if(text.equalsIgnoreCase("3004")){
			errorMsg = "请正确输入中国移动手机号码！";
		}else if(text.equalsIgnoreCase("2003")){
			errorMsg = "手机号码不能为空";
		}else{
			errorMsg = "系统繁忙,请检查您输入的信息是否正确!";
		}
	}
	
}
