package com.lkb.thirdUtil.yd;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
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
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;

public class XJYidong extends BaseInfoMobile{
	public static String authUrl = "http://www.xj.10086.cn/servlet/checkcode";
//	public static String authUrlVerifyUrl = "http://www.xj.10086.cn/app?service=ajaxDirect/1/IcsLogin/IcsLogin/javascript/undefined&pagename=IcsLogin&eventname=checkCode&&CHECK_CODE=";
	public static String authUrlVerifyUrl = "https://xj.ac.10086.cn/cas2/loginSamlPortal?type=portal";
//	public static String loginUrl = "https://xj.ac.10086.cn/cas2/loginSaml";
	public static String loginPageUrl="http://www.xj.10086.cn/app?service=page/IcsLogin&listener=initPage";
	public static String pagesUrl="http://www.xj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQueryNew/feequery.VoiceQueryNew/javascript/101&pagename=feequery.VoiceQueryNew&eventname=queryBusi&pagination_iPage=";
	public static String smsCodeCheckUrl="http://www.xj.10086.cn/app";
	
	public String yueKey  = "yue.xjyidong";
	
	public static void main(String[] args) {
		Login login = new Login("15894722376","636040");
		XJYidong xj = new XJYidong(login);
		xj.index();
		//nm.inputCode(nm.getImgUrl());
		//登陆
		Map<String,Object> map = xj.login();
		//xj。sendPhoneDynamicsCode();
		
		//System.out.println("请输入手机口令：");
		xj.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
		xj.checkPhoneDynamicsCode();
		xj.close();

		
	}
	public XJYidong(Login login) {
		super(login);
	}
	public XJYidong(Login login,String currentUser) {
		super(login, ConstantNum.comm_xj_yidong,currentUser);
		userSource = Constant.YIDONG;
	}
	public void init(){
		if(!isInit()){
			try{
				String text = cutil.get(loginPageUrl);
				//System.out.println(text);
				Document doc = Jsoup.parse(text);
				Map<String, String> param = new HashMap<String,String>();
				param.put("Form0", doc.select("input[id=Form0]").val());
				param.put("failedUrl", doc.select("input[name=failedUrl]").val());
				param.put("fromLogin", doc.select("input[name=fromLogin]").val());
				param.put("noService", doc.select("input[name=noService]").val());
				param.put("numId", doc.select("input[id=numId]").val());
				param.put("passwordType", doc.select("input[id=passwordType]").val());
				// param.put("service", doc.select("input[name=service]").get(1).val());
				//话费记录
				param.put("service","https://www.xj.10086.cn/my/");
				
				param.put("serviceright", doc.select("input[id=serviceright]").val());
				param.put("sid", doc.select("input[id=sid]").val());
				param.put("sp", doc.select("input[name=sp]").val());
				param.put("systemCode", doc.select("input[name=systemCode]").val());
				param.put("userType", doc.select("input[name=userType]").val());
				redismap.put("jsmap", param);
			}catch(Exception e){
			}
		}
	}
	public Map<String,Object> login(){
		try{
			String text = null;
			Map<String, String> param = (Map<String, String>) redismap.get("jsmap");
			param.put("CHECK_CODE", login.getAuthcode());
			param.put("SERIAL_NUMBER", login.getLoginName());
			param.put("USER_PASSWD", login.getPassword());
//			String authVertifyUrl = authUrlVerifyUrl + map.get("CHECK_CODE")
//					+ "&partids=undefined&ajaxSubmitType=post&ajax_randomcode="
//					+ Math.random();
			text = cutil.post(authUrlVerifyUrl, param);
			
			//param.put("service",
		   //"https://www.xj.10086.cn/app?service=ajaxDirect/1/MobileLogin/MobileLogin/javascript/remain&pagename=MobileLogin&eventname=initDiscntRemain&partids=remain&ajaxSubmitType=post&ajax_randomcode=0.9319386717279601");
			if(text.equals("https://www.xj.10086.cn/my/")){
				text = cutil.post(text,param);
				//System.out.println(text);
				text = cutil.get("https://www.xj.10086.cn/app?service=ajaxDirect/1/MobileLogin/MobileLogin/javascript/remain&pagename=MobileLogin&eventname=initDiscntRemain&partids=remain&ajaxSubmitType=post&ajax_randomcode=0.9319386717279601");
				text = StringEscapeUtils.unescapeXml(text);
				//System.out.println(text);
				RegexPaserUtil rp = new RegexPaserUtil("余额：","元",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String yue = rp.getText();
				if(yue!=null){
					redismap.put(yueKey, yue);
					loginsuccess();
					sendPhoneDynamicsCode();
					addTask_1(this);
				}
			}else if(text.contains("login?SailingSSO_error_code=-8&errorTime=")&&text.contains("&SERIAL_NUMBER="+login.getLoginName())){
				errorMsg = "您输入的账号或密码错误!";
			}else if(text.contains("SailingSSO_error_code=-10&errorTime=null")&&text.contains("&SERIAL_NUMBER="+login.getLoginName())){
				errorMsg = "账号因使用错误密码登陆超过3次已锁定,将于次日凌晨自动解锁";
			}
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;
	}
	
	public Map<String,Object> sendPhoneDynamicsCode(){
		Map<String,Object> map = new HashMap<String, Object>();
		int status = 0;
		String errorMsg = null;
		String text=cutil.get("http://www.xj.10086.cn/service/fee/personalinfo/CheckedSms/");
		if(text.contains("请输入您收到的动态短信验证码")){
			Document doc=Jsoup.parse(text);
			Map<String,String> smap=new HashMap<String,String>();
			smap.put("service", doc.select("input[id=service]").val());
			smap.put("sp", doc.select("input[id=sp]").val());
			smap.put("Form0", doc.select("input[id=Form0]").val());
			smap.put("checkButton", doc.select("input[id=checkButton]").val());
			smap.put("$FormConditional", doc.select("input[id=$FormConditional]").val());
			smap.put("$FormConditional$0", doc.select("input[id=$FormConditional$0]").val());
			redismap.put("dongtaiparams", smap);
			errorMsg = "发送成功!";
			status = 1;
		}else{
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.errorMsg, errorMsg);
		map.put(CommonConstant.status, status);
		return map;
	}
	// 随机短信登录
		public Map<String,Object> checkPhoneDynamicsCode() {
			Map<String,String> param=(Map<String, String>) redismap.get("dongtaiparams");
			param.put("SMS_NUMBER", login.getPhoneCode());
			String text = cutil.post(smsCodeCheckUrl, param);
			if(!text.contains("业务办理失败")){
				status = 1;
				errorMsg = "验证成功!";
			}else{
				errorMsg = "验证失败!";
			}
			map.put(CommonConstant.errorMsg, errorMsg);
			map.put(CommonConstant.status, status);
			if(status==1){
	        	addTask_2(this);
	    	}
			return map;
		}
		/**
		 * 月度账单
		 */
		public List<MobileTel> spiderMonthlyBill() {
			List<MobileTel> list = new ArrayList<MobileTel>();
			boolean isUpdate = false;
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			
			String text4 = cutil.get("http://www.xj.10086.cn/service/fee/feequery/BillQueryNew/");
			//System.out.println(text4);
			Document doc = Jsoup.parse(text4);
			//System.out.println(text4);
			Map<String, String> param = new HashMap<String, String>();
			param.put("service", doc.select("input[id=service]").val());
			param.put("sp", doc.select("input[id=sp]").val());
			param.put("Form0", doc.select("input[id=Form0]").val());
			param.put("$FormConditional",
					doc.select("input[id=$FormConditional]").val());
			param.put("$FormConditional$0",
					doc.select("input[id=$FormConditional$0]").val());
			param.put("SMSOUTNEW", doc.select("input[id=SMSOUTNEW]").val());
			param.put("$Submit", doc.select("input[id=$Submit]").val());
			param.put("com.ailk.ech.framework.html.TOKEN",
					doc.select("input[id=com.ailk.ech.framework.html.TOKEN]")
							.val());
			Set<Date> set = getMonthlyBillMaxNumTel();
			//System.out.println(set);
			Date  dtime = null;
			if(set!=null&&set.size()>0){
				dtime = set.iterator().next();
			}
			Date d = null;
			for (String startDate : ms) {
				param.put("MONTH", startDate);
				param.put("detailMonth", startDate);
				d = DateUtils.StringToDate(startDate, "yyyyMM");
				if(dtime!=null){
					//检测是否数据库有相等月份,如果包含,是否数据库最大月份在里边,主要是更新数据库中最大月份的话费清单,其他跳过
					if(set.contains(d)){
						if(d.getTime()!=dtime.getTime()){
							continue;
						}else{
							isUpdate = true;
						}
					}
				}
				try{
					String text = cutil.post("http://www.xj.10086.cn/app?random=" + Math.random(), param);
					Document doc1 = Jsoup.parse(text);
					param.put("com.ailk.ech.framework.html.TOKEN",
							doc1.select("input[id=com.ailk.ech.framework.html.TOKEN]")
									.val());
					if (doc1.select("form[name=Form0]").select("table").size() > 1) {
						Element table = doc1.select("form[name=Form0]")
								.select("table").get(1);
						Elements trs = table.select("tr");
						BigDecimal tcgdf = new BigDecimal("0.0");
						BigDecimal cAllPay = new BigDecimal("0.0");
						BigDecimal tcwyytxf = new BigDecimal("0.0");
						BigDecimal tcwdxf = new BigDecimal("0.0");
						for (int j = 1; j < trs.size(); j++) {
							if (trs.get(j).select("td").get(0).text()
									.contains("套餐及固定费用")) {
								String tcgdfStr = trs.get(j).select("b").get(1)
										.text();
								tcgdf = new BigDecimal(tcgdfStr);
							}

							else if (trs.get(j).select("td").get(0).text()
									.contains("费用合计")) {
								String cAllPayStr = trs.get(j).select("b").get(1)
										.text();
								cAllPay = new BigDecimal(cAllPayStr);
							} else if (trs.get(j).select("td").get(0).text()
									.contains("语音通话费")) {
								String tcwyytxfStr = trs.get(j).select("b").get(1)
										.text();
								tcwyytxf = new BigDecimal(tcwyytxfStr);
							} else if (trs.get(j).select("td").get(0).text()
									.contains("短信/彩信费")) {
								String tcwdxfStr = trs.get(j).select("b").get(1)
										.text();
								tcwdxf = new BigDecimal(tcwdxfStr);
							}
						}
						if(isUpdate){
							updateTel(d, cAllPay);
							isUpdate = false;
						}else{
							MobileTel mobileTel = new MobileTel();
							
							mobileTel.setcTime(d);
							mobileTel.setcAllPay(cAllPay);
							mobileTel.setTcgdf(tcgdf);
							mobileTel.setTcwdxf(tcwdxf);
							mobileTel.setTcwyytxf(tcwyytxf);
							list.add(mobileTel);
						}
					} else {
						//System.out.println("尊敬的神州行客户：您目前没有您当前账单未生成，请选择查询其他月份账单，欢迎使用网上营业厅自助服务。");
						continue;
					}
				} catch (Exception e) {
					writeLogByZhangdan(e);
				}
			}
			return list;
		}

		/**
		 * 查询通话记录
		 */
	public List spiderCallHistory(){	
		List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
		List<MobileDetail> list = new ArrayList<MobileDetail>();
		Date date = getMaxTime();
		boolean isCountine = true;
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("service", "direct/1/feequery.VoiceQueryNew/FormA");
		param.put("sp", "S0");
		param.put("Form0", "$Submit");
		// param.put("MONTH", month);
		param.put("BILL_TYPE_NEW", "101");
		param.put("DIS_TYPE", "00");
		param.put("CALL_TYPE", "00");
		param.put("CHART_TYPE", "01");
		param.put("$Submit", "查询");
		DateFormat df = new SimpleDateFormat("yyyyMM");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date cTime = null;
		Elements els = null;
		for (String startDate : ms) {
			param.put("MONTH", startDate);
			String text = cutil.post("http://www.xj.10086.cn/app", param);
			//System.out.println(text);
			Document doc = Jsoup.parse(text);
			//System.out.println(doc);
			els =  doc.select("form[name=Form1]");
			if (els!=null&&els.size()>0) {
				Elements trs = doc.select("table[class=listTable1 hover]")
						.get(1).select("tbody").select("tr");

				for (int j = 0; j < trs.size(); j++) {
					Elements tds = trs.get(j).select("td");
					String cTimeStr = tds.get(0).text();
					try {
						cTime = df2.parse(cTimeStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					 if(date!=null){
					    	if(date.getTime()>=cTime.getTime()){
					    		isCountine = false;//终止循环
					    		break;
					    	}
					   }
					String tradeAddr = tds.get(1).text();
					String tradeWay = tds.get(2).text();
					if(tradeWay.contains("主叫")){
						tradeWay = "主叫";
					}else{
						tradeWay = "被叫";
					}
					String recevierPhone = tds.get(3).text();
					String tradeTimeStr=tds.get(4).text();
					int tradeTime = TimeUtil.timetoint(tradeTimeStr);
					String tradeType = tds.get(5).text();
					BigDecimal onlinePay = new BigDecimal(tds.get(6).text())
							.add(new BigDecimal(tds.get(7).text()));
					MobileDetail mobileDetail = new MobileDetail();
					mobileDetail.setcTime(cTime);
					mobileDetail.setOnlinePay(onlinePay);
					mobileDetail.setRecevierPhone(recevierPhone);
					mobileDetail.setTradeAddr(tradeAddr);
					mobileDetail.setTradeTime(tradeTime);
					mobileDetail.setTradeType(tradeType);
					mobileDetail.setTradeWay(tradeWay);
					list.add(mobileDetail);
				}
					RegexPaserUtil rp = new RegexPaserUtil("第1/ ","页",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String s = rp.getText();
					int size = 0;
					if(s!=null){
						size=Integer.parseInt(s.trim());
					}
					for(int r=1;r<size;r++){
						int page=r+1;
						String pagesUrll=pagesUrl+page+"&partids=101&ajaxSubmitType=get&ajax_randomcode="+Math.random();
						String text1=cutil.get(pagesUrll);
						Document doc1=Jsoup.parse(text1);
						String info=doc1.select("part[id=101]").text();
						Document doc2=Jsoup.parse(info);
						Elements trss = doc2.select("table[class=listTable1 hover]")
								.get(0).select("tbody").select("tr");

						for (int j = 0; j < trss.size(); j++) {
							Elements tds = trss.get(j).select("td");
							String cTimeStr = tds.get(0).text();
							try {
								cTime = df2.parse(cTimeStr);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							String tradeAddr = tds.get(1).text();
							String tradeWay = tds.get(2).text();
							if(tradeWay.contains("主叫")){
								tradeWay = "主叫";
							}else{
								tradeWay = "被叫";
							}
							String recevierPhone = tds.get(3).text();
//							int tradeTime = Integer.parseInt(tds.get(4).text().replace("秒", ""));
							int tradeTime = TimeUtil.timetoint(tds.get(4).text());
							String tradeType = tds.get(5).text();
							BigDecimal onlinePay = new BigDecimal(tds.get(6).text())
									.add(new BigDecimal(tds.get(7).text()));
						//	Date cTime = df.parse(df.format(date1));
						
							MobileDetail mobileDetail = new MobileDetail();
							mobileDetail.setcTime(cTime);
							mobileDetail.setOnlinePay(onlinePay);
							mobileDetail.setRecevierPhone(recevierPhone);
							mobileDetail.setTradeAddr(tradeAddr);
							mobileDetail.setTradeTime(tradeTime);
							mobileDetail.setTradeType(tradeType);
							mobileDetail.setTradeWay(tradeWay);
							list.add(mobileDetail);
							
						}
					}
				if(!isCountine){
					break;
				}
			}
			else{
				//System.out.println("尊敬的神州行客户：您目前没有您当前账单未生成，请选择查询其他月份账单，欢迎使用网上营业厅自助服务。");
				continue;
			}
		}
		return list;
		
	}
	
	public User spiderInfo() {
		try {
			String text4 = cutil.get("http://www.xj.10086.cn/app?service=page/operation.CustInfoMod&listener=initPage");
			Document doc = Jsoup.parse(text4);
			Element table = doc.select("form[id=CustInfoModForm]")
					.select("table").first();
			Elements trs = table.select("tr");
			String realName = trs.get(0).select("input[id=$TextField]").val();
			String address = trs.get(2).select("input[id=POST_ADDRESS]").val();
			String postCode = trs.get(2).select("input[id=POST_CODE]").val();
			String email = trs.get(2).select("input[id=EMAIL]").val();
			String text3 = cutil.get("http://www.xj.10086.cn/service/fee/svcquery/FeesetQuery/");
			//System.out.println(text3);
			Document doc1 = Jsoup.parse(text3);
			String packageyd = doc1.select("form[name=Form0]").select("table")
					.get(0).select("b").text();
			String text2 =  cutil.get("http://www.xj.10086.cn/service/fee/feequery/BalanceQuery/");
			Document doc2 = Jsoup.parse(text2);
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("service", doc2.select("input[id=service]").val());
			map1.put("sp", doc2.select("input[id=sp]").val());
			map1.put("Form0", doc2.select("input[id=Form0]").val());
			map1.put("$FormConditional",
					doc2.select("input[id=$FormConditional]").val());
			map1.put("$FormConditional$0",
					doc2.select("input[id=$FormConditional$0]").val());
			map1.put("BLANCE", doc2.select("input[id=BLANCE]").val());
			map1.put("operHipInfo", doc2.select("input[id=operHipInfo]").val());
			map1.put("com.ailk.ech.framework.html.TOKEN",
					doc2.select("input[id=com.ailk.ech.framework.html.TOKEN]")
							.val());
			String balance = redismap.get(yueKey).toString();
			BigDecimal yBalance = new BigDecimal(balance);
			User user = new User();
			user.setAddr(address);
			user.setRealName(realName);
			user.setEmail(email);
			user.setModifyDate(new Date());
			user.setPackageName(packageyd);
			user.setPhoneRemain(yBalance);
			return user;
		}catch(Exception e){
			writeLogByInfo(e);
		}
		return null;
	}
	@Override
	public void startSpider() {
		parseBegin();
		try{
			int type = login.getType();
			switch (type) {
			case 1:
				getMonthlyBill();//账单详情
				
				getUser(); //个人信息
				
//				getCallHistory(); //历史账单	
				break;
			case 2:
				getCallHistory(); //历史账单	
				getSmsLog();//短信记录
				getOnline();//流量记录
				break;
			case 3:
				getMonthlyBill();//账单详情
				getUser(); //个人信息
				getCallHistory(); //历史账单
				getSmsLog();//短信记录
				break;
			default:
				break;
			}
		}finally{
			parseEnd();
		}
	}
	
	//爬取短信记录
	public void getSmsLog()
	{
		List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
		List<MobileDetail> list = new ArrayList<MobileDetail>();
//		MobileDetail bean = (MobileDetail) getMaxTime();
//		boolean isCountine = true;
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("service", "direct/1/feequery.VoiceQueryNew/FormA");
		param.put("sp", "S0");
		param.put("Form0", "$Submit");
		// param.put("MONTH", month);
		param.put("BILL_TYPE_NEW", "102");
		param.put("DIS_TYPE", "00");
		param.put("CALL_TYPE", "00");
		param.put("CHART_TYPE", "01");
		param.put("$Submit", "查询");
		for (String startDate : ms) {
			int pageNo=1;
			int pageCount=1;
			
			if(pageNo==1)
			{
				param.put("MONTH", startDate);
				String text=null;
				text = cutil.post("http://www.xj.10086.cn/app", param);
				RegexPaserUtil rp = new RegexPaserUtil("第1/ ","页",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String s = rp.getText();
				if(s!=null&&s!="")
				{
					pageCount=Integer.parseInt(s.trim());
				}
				//先把之前的记录加到数据库中
				if(pageCount>1)
				{
					for(pageNo=pageCount;pageNo>=2;pageNo--)
					{
						param.put("MONTH", startDate);
						String text2=null;
						String pagesUrll=pagesUrl+pageNo+"&partids=102&ajaxSubmitType=get&ajax_randomcode="+Math.random();
						text2=cutil.get(pagesUrll);
						text2 = text2.replace("&lt;", "<");
						text2 = text2.replace("&gt;", ">");
						parseSmsLog(text2);
					}
				}
				parseSmsLog(text);
			}
		}
	}
	
	public void parseSmsLog(String text)
	{
		Date lastTime = null;
		try{
			lastTime = mobileMessageService.getMaxSentTime(login.getLoginName()).getSentTime();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(text!=null&&text.contains("listTable1 hover"))
		{
			text = StringEscapeUtils.unescapeHtml4(text);
			Document doc = Jsoup.parse(text);
			//解析
			Elements tables = doc.select("table[class=listTable1 hover]");
			Element table = null;
			for(int i=0;i<tables.size();i++)
			{
				if(tables.get(i).toString().contains("thead")&&tables.get(i).toString().contains("tbody"))
				{
					table = tables.get(i);
				}
			}
			Element tbody = table.select("tbody").first();
			Elements trs = tbody.select("tr");
			for(int i=0;i<trs.size();i++)
			{
				
				Elements tds = trs.get(i).select("td");
				if(tds.size()==6)
				{
					 String sentTime= tds.get(0).text().trim();
					 String tradeWay= tds.get(2).text().trim();
					 String recevierPhone= tds.get(1).text().trim();
					 String sentAddr= "";
					 String allPay= tds.get(5).text().trim();	
					 String phone = login.getLoginName();//本机号码
					
					 MobileMessage message = new MobileMessage();
					 message.setAllPay(new BigDecimal(allPay));
					 message.setRecevierPhone(recevierPhone);
					 message.setSentAddr(sentAddr);
					 Date times=null;					 
					try{
						times=DateUtils.StringToDate( sentTime, "yyyy-MM-dd HH:mm:ss");							
						if(lastTime!=null&&times!=null){
							if(times.getTime()<=lastTime.getTime()){
								continue;
							}	
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					 message.setSentTime(times);
					 message.setTradeWay(tradeWay);
					 message.setPhone(phone);
					 message.setCreateTs(new Date());
					 UUID uuid = UUID.randomUUID();
					 message.setId(uuid.toString());
					 mobileMessageService.save(message);
				}
			}
			
		}
	}
	
	    //流量记录
		public void getOnline()
		{
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			List<MobileDetail> list = new ArrayList<MobileDetail>();
			Map<String, String> param = new HashMap<String, String>();

			param.put("service", "direct/1/feequery.VoiceQueryNew/FormA");
			param.put("sp", "S0");
			param.put("Form0", "$Submit");
			// param.put("MONTH", month);
			param.put("BILL_TYPE_NEW", "103");
			param.put("DIS_TYPE", "00");
			param.put("CALL_TYPE", "00");
			param.put("CHART_TYPE", "01");
			param.put("$Submit", "查询");
			
			MobileOnlineList bean_List = null;
			 if(mobileOnlineListService .getMaxTime(login.getLoginName())!= null) {
			   bean_List = mobileOnlineListService.getMaxTime(login .getLoginName());
			 }
			MobileOnlineBill bean_Bill = null;
			if(mobileOnlineBillService .getMaxTime(login.getLoginName())!= null) {
			   bean_Bill = mobileOnlineBillService.getMaxTime(login .getLoginName());
			 }

			for (int j = ms.size()-1; j >= 0; j--) {
				String startDate = ms.get(j);
				int pageNo=1;
				int pageCount=1;
				
				if(pageNo==1)
				{
					param.put("MONTH", startDate);
					String text=null;
					text = cutil.post("http://www.xj.10086.cn/app", param);
					//System.out.println(text);
					RegexPaserUtil rp = new RegexPaserUtil("第1/ ","页",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String s = rp.getText();
					if(s!=null&&s!="")
					{
						pageCount=Integer.parseInt(s.trim());
					}
					//先把之前的记录加到数据库中
					if(pageCount>1)
					{
						for(pageNo=pageCount;pageNo>=2;pageNo--)
						{
							param.put("MONTH", startDate);
							String text2=null;
							String pagesUrll=pagesUrl+pageNo+"&partids=102&ajaxSubmitType=get&ajax_randomcode="+Math.random();
							text2=cutil.get(pagesUrll);
							text2 = text2.replace("&lt;", "<");
							text2 = text2.replace("&gt;", ">");
							parseOnlineList(text2,bean_List);
						}
					}
					try {
						parseOnlineList(text,bean_List);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						parseOnlineBill(startDate,text,j,bean_Bill);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		public void parseOnlineList(String text,MobileOnlineList bean_List)
		{
			if(text.contains("起始时间")){
				Document doc6 = Jsoup.parse(text);
				Element table =doc6.select("table[class=listTable1 hover]").get(1);
						Elements trs = table.select("table>tbody>tr");
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
								cTime = DateUtils.StringToDate(cTime1, "yyyy-MM-dd HH:mm:ss");
								tradeAddr = tds.get(1).text();
								onlineType = tds.get(2).text();
								try {
									String onlineTime1 = tds.get(3).text();
									onlineTime = StringUtil.flowTimeFormat(onlineTime1);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								try {
									String totalFlow1 = tds.get(4).text();
									totalFlow = new Long(totalFlow1);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								try {
									String communicationFees1 = tds.get(5).text();// 通信费
									if(communicationFees1!=""&&communicationFees1!=null&&communicationFees1.length()>0){
									communicationFees = new BigDecimal(communicationFees1);
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
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
									 if(bean_List.getcTime().getTime() < onlineList.getcTime().getTime()){
										
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
		}
		
		public void parseOnlineBill(String startDate,String text,int iscm1,MobileOnlineBill bean_Bill)
		{
			if(text.contains("上网详单")){
				Document doc6 = Jsoup.parse(text);
				Element table =doc6.select("table[class=listTable1 hover]").get(0);

							Date monthly = null;
							long totalFlow = 0;
							long freeFlow = 0;
							long chargeFlow = 0;
							int iscm = 0;
							BigDecimal communicationFees = new BigDecimal(0);
							String year = startDate.substring(0,4);
							String mon = startDate.substring(4,6);
							try {
								monthly = DateUtils.StringToDate(year+"-"+mon,"yyyy-MM");
								String totalFlow1 = table.select("tr:eq(4)>td:eq(1)").text().replace("总流量：","").trim();
								totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(iscm1==0) {
								iscm = 1;
							}
							
							MobileOnlineBill onlineBill = new MobileOnlineBill();
							UUID uuid = UUID.randomUUID();
							onlineBill.setId(uuid.toString());
				        	if(bean_Bill!=null){
								 if(bean_Bill.getMonthly().getTime()>=onlineBill.getMonthly().getTime()){
								 }else {
								 	onlineBill.setMonthly(monthly);
						        	onlineBill.setChargeFlow(chargeFlow);
						        	onlineBill.setFreeFlow(freeFlow);
						        	onlineBill.setTotalFlow(totalFlow);
						        	try {
										onlineBill.setTrafficCharges(communicationFees);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
						        	onlineBill.setPhone(login.getLoginName());    
						        	onlineBill.setIscm(iscm);
									mobileOnlineBillService.save(onlineBill);
								 }
							}else {
				        	onlineBill.setMonthly(monthly);
				        	onlineBill.setChargeFlow(chargeFlow);
				        	onlineBill.setFreeFlow(freeFlow);
				        	onlineBill.setTotalFlow(totalFlow);
				        	try {
								onlineBill.setTrafficCharges(communicationFees);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	onlineBill.setPhone(login.getLoginName());    
				        	onlineBill.setIscm(iscm);
							mobileOnlineBillService.save(onlineBill);
						  }
					   	
					}
		}
			
}
