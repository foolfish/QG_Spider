package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.util.HtmlUtils;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.context.ContextLifecycle;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.proxy.Proxy;
import com.lkb.robot.proxy.ProxyManager;
import com.lkb.robot.proxy.ProxyManagerFactory;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

public class HaiNanDianXin extends AbstractDianXinCrawler {
	
	public HaiNanDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
		this();
		this.spider = spider;
		this.phoneNo = phoneNo;
		this.password = password;
		if (user == null) {
			this.user = new User();
			this.user.setPhone(phoneNo);
		} else {
			this.user = user;
		}
		this.util = util;
		this.authCode = authCode;
		spider.getSite().setCharset("utf-8");
		spider.getSite().setDomain("www.hi.189.cn");
	}
	public HaiNanDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		spider.getSite().setCharset("utf-8");
		spider.getSite().setDomain("www.hi.189.cn");
	}
	public HaiNanDianXin() {
		areaName = "海南";
		customField1 = "1";
		customField2 = "22";
		toStUrl = "&toStUrl=http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx";
		shopId = "10022";
		ssoUrl = "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10022&toStUrl=http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx";
	}
	
	//https://uam.ct10000.com/ct10000uam/validateImg.jsp
	public void checkVerifyCode(final String userName) {   
		spider.setProxyHolder(ProxyManager.PROXY_HOLDER_ONE);
		saveVerifyCode("hainan", userName);
    }
	/*public void sendSmsPasswordForRequireCallLogService() {
		String xml = "<buffalo-call><method>getSmsCode</method><map><type>java.util.HashMap</type><string>PHONENUM</string><string>" + phoneNo + "</string><string>PRODUCTID</string><string>50</string><string>RTYPE</string><string>QD</string></map></buffalo-call>";
		postUrl("http://www.hi.189.cn/BUFFALO/buffalo/CommonAjaxService", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.HNDX_7){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
					Elements rect = doc.select("string");
					String ac = rect.get(rect.size() - 1).text();
					requestAllService();
				} logger.error("error",e); (Exception e) {
					logger.error("error",e);
				}
			}
		});
		
	}
	public void verifySmsCode() {
		Date d = new Date();
		String dstr = DateUtils.formatDate(d, "yyyyMM");
		String xml = "<buffalo-call><method>queryDetailBill</method><map><type>java.util.HashMap</type><string>PRODNUM</string><string>MVWCdY0vDvFeiJ7Pmdzsqg==</string><string>CITYCODE</string><string>0898</string><string>QRYDATE</string><string>" + dstr + "</string><string>TYPE</string><string>8</string><string>PRODUCTID</string><string>50</string><string>CODE</string><string>631627</string><string>USERID</string><string>10158069</string></map></buffalo-call>";
		postUrl("http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.HNDX_7){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
					Elements rect = doc.select("string");
					String ac = rect.get(rect.size() - 1).text();
					requestAllService();
				} catch (Exception e) {
					logger.error("error",e);
				}
			}
		});
		
	}*/
	//https://uam.ct10000.com/ct10000uam-gate/SSOFromUAM?ReturnURL=687474703A2F2F686E2E3138392E636E3A38302F686E73656C66736572766963652F75616D6C6F67696E2F75616D2D6C6F67696E2175616D4C6F67696E5265742E616374696F6E3F7255726C3D2F686E73656C66736572766963652F62696C6C71756572792F62696C6C2D71756572792173686F77546162732E616374696F6E3F746162496E6465783D33&ProvinceId=19

	protected void onCompleteLogin(SimpleObject context) {
		//sendSmsPasswordForRequireCallLogService();
		//DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
		parseBalanceInfo();
	}
	
	public void requestAllService() {
		requestService();
	}
	private void requestService() {
		String xml = "<buffalo-call><method>queryBalance</method><map><type>java.util.HashMap</type><string>PRODNUM</string><string>MVWCdY0vDvFeiJ7Pmdzsqg==</string><string>CITYCODE</string><string>"+entity.getString("CITYCODE")+"</string><string>TYPE</string><string>1</string><string>PRODID</string><string>3042325593</string><string>USERTYPE</string><string>SHOUJI</string></map></buffalo-call>";
		postUrl("http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.HNDX_7){
			@Override
			public void afterRequest(SimpleObject context) {
				String content = HtmlUtils.htmlUnescape(ContextUtil.getContent(context)).replaceAll("<span>", "").replaceAll("</span>", "");
				String yue = StringUtil.subStr("账户余额", "元", content);
				if (!StringUtils.isBlank(yue)) {
					user.setPhoneRemain(new BigDecimal(yue));
				}
			}
		});
		
		getUserInfo();
		//parseBalanceInfo();
		Date d = new Date();
		Calendar currentCalendar = Calendar.getInstance();
		Calendar testCalendar = Calendar.getInstance();
		currentCalendar.setTime(d);
		int currentMonth = currentCalendar.get(Calendar.MONTH);
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			testCalendar.setTime(cd);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			int testMonth = testCalendar.get(Calendar.MONTH);
			Date monthly = cd;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				monthly = sdf.parse(dstr);
			} catch (ParseException e) {
				logger.error("error",e);
			}
			
			if(testMonth != currentMonth){
				requestFlowService(1, 1, monthly, dstr, false);
			}else{
				requestFlowService(1, 1, monthly, dstr, true);
			}
			requestMessageService(1, 1, cd, dstr);
			requestCallLogService(1, 1, cd, dstr);
		}
		
		requestMonthBillService();	
	}	
	
	/**
	* <p>Title: getUserInfo</p>
	* <p>Description: 抓取用户基本信息</p>
	* @author Jerry Sun
	*/
	private void getUserInfo(){
		String[] param = new String[4];
		param[0] = "GBK";
		getUrl("http://www.hi.189.cn/service/account/service_kh_xx.jsp", "http://www.hi.189.cn/service/account/service_kh_xx.jsp", param, new AbstractProcessorObserver(util, WaringConstaint.HNDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(ContextUtil.getContent(context) != null){
					//System.out.println(context);
					String idCard = StringUtil.subStr("var val = \"", "\";	//证件号码", ContextUtil.getContent(context));
					if(!"".equals(idCard) && idCard != null){
						int idCardLen = idCard.length();
						switch (idCardLen) {
						case 15:
							//320521 72 08 07 024 
							String year_15 = "19" + idCard.substring(6, 8);
							String month_15 = idCard.substring(8, 10);
							String day_15 = idCard.substring(10, 12);
							user.setBirthday(DateUtils.StringToDate(year_15 + month_15 + day_15, "yyyyMMdd"));
							break;
						case 18:
							//140321 1986 09 11 0310
							String year_18 = idCard.substring(6, 10);
							String month_18 = idCard.substring(10, 12);
							String day_18 = idCard.substring(12, 14);
							user.setBirthday(DateUtils.StringToDate(year_18 + month_18 + day_18, "yyyyMMdd"));
							break;
						}
						
					}
					Document doc = ContextUtil.getDocumentOfContent(context);
					String addr = doc.select("#relationAddress").attr("value");
					String mobileNum = doc.select("#mobileNum").attr("value");
					String relationTel = doc.select("#relationTel").attr("value");
					String QQ = doc.select("#QQ").attr("value");
					String eMail = doc.select("#eMail").attr("value");
					
					user.setIdcard(idCard);
					user.setAddr(addr);
					user.setPhone(phoneNo);
					user.setEmail(eMail);
					user.setQq(QQ);
					user.setUserName(phoneNo);
//					user.setRegisterDate(registerDate);	
				}
			}
		});
	}
	
	private void parseBalanceInfo() {
		//使用父类的方法 postUrl
		postUrl("http://www.hi.189.cn/ajaxServlet/getCityCodeAndIsLogin", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new String[][]{{"method", "getCityCodeAndIsLogin"}}, new AbstractProcessorObserver(util, WaringConstaint.HNDX_7){
			//2.请求完成后的解析
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					JSONObject json = ContextUtil.getJsonOfContent(context); 
					if (json != null && json.has("ISLOGIN") && json.getBoolean("ISLOGIN")) {
						//{"ISLOGIN":"true","CITYCODE":"0898","ACCOUNT":"18189814687","USERNAME":"赵玉龙","MSG":"下午好,赵玉龙"}
						String userName = json.getString("USERNAME");
						user.setRealName(userName);
						setStatus(STAT_LOGIN_SUC);
						entity.put("CITYCODE", json.getString("CITYCODE"));
						notifyStatus();
						requestService();
					} else {
						setStatus(STAT_STOPPED_FAIL);
						data.put("errMsg", "登录失败，请重试！");
						notifyStatus();
					}
				} catch (JSONException e) {
					logger.error("error",e);
				}
			}
		});
		
	}	
	
	private void requestMonthBillService() {
		/*
		 * 抓取实时话费（当月话费）
		 */
		String xml_rightNow = "<buffalo-call><method>getRealtimeCharges</method><map><type>java.util.HashMap</type><string>PRODNUM</string><string>MVWCdY0vDvFeiJ7Pmdzsqg==</string><string>CITYCODE</string><string>"+entity.getString("CITYCODE")+"</string><string>TYPE</string><string>1</string><string>PRODID</string><string>3042325593</string></map></buffalo-call>";
		postUrl("http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=sshfcx&rand="+System.currentTimeMillis(), new String[]{null, xml_rightNow}, null, new AbstractProcessorObserver(util, WaringConstaint.HNDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				try{
					if(context != null){
						String decodeUnicode = HtmlUtils.htmlUnescape(ContextUtil.getContent(context));
						Document doc = Jsoup.parse(decodeUnicode);
						String str = doc.text().trim();
						if(!"".equals(str)){
							String jzrq = StringUtil.subStr("截止日期：", ")", str).trim();
//							String txfwf = StringUtil.subStr("通信服务费", "元", str);
							String allPay = StringUtil.subStr("话费总额", "元", str).trim();
							
							DianXinTel dxt = new DianXinTel();
							dxt.setcAllPay(new BigDecimal(allPay));
							dxt.setcTime(DateUtils.StringToDate(DateUtils.firstDayOfMonth(DateUtils.getToday("yyyy-MM")), "yyyy-MM-dd"));
							dxt.setcName(user.getRealName());
							dxt.setTeleno(phoneNo);
							dxt.setDependCycle(DateUtils.getToday("yyyy-MM-dd")+"-"+jzrq.replace("年", "-").replace("月", "-").replace("日", ""));
							
							telList.add(dxt);
						}
						
					}
				}catch(Exception e){
					logger.error("海南电信实时话费抓取出错！", e);
				}
			}
		});
		/*
		 * 抓取账单记录
		 */
		String xml = "<buffalo-call><method>queryAccountBilltow</method><map><type>java.util.HashMap</type><string>PRODNUM</string><string>MVWCdY0vDvFeiJ7Pmdzsqg==</string><string>CITYCODE</string><string>0898</string><string>TYPE</string><string>2</string><string>PRODCODE</string><string>3042325593</string><string>PRODID</string><string>50</string></map></buffalo-call>";
		postUrl("http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.HNDX_7){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
					Elements rect = doc.select("string");
					doc = Jsoup.parse(StringEscapeUtils.unescapeHtml4(rect.text()));
					rect = doc.select("tr");
					int len = rect.size();
					for(int i = 1; i < len; i++) {
						Elements tds = rect.get(i).select("td");
						DianXinTel tel = new DianXinTel();
						tel.setTeleno(phoneNo);
						getTelList().add(tel);
						
						for(int j=0; j < tds.size(); j++) {
							String txt = tds.get(j).text();
							if (j == 0) {
								tel.setcTime(DateUtils.StringToDate(txt, "yyyy年MM月"));
							} else if (j == 1) {
								String n = "";
								if (txt.indexOf("没有") >= 0) {
									
								} else {
									n = txt.replaceAll("元", "").trim();
								}
								BigDecimal b1= new BigDecimal(n.length() == 0 ? "0" : n);
								tel.setcAllPay(b1);
							}
						}
					}
				} catch (Exception e) {
					logger.error("error",e);
				}
			}
		});
		
	}
	
	//Created by Dongyu.Zhang
	private void requestFlowService(final int page, final int t, final Date d, final String dstr, final boolean isCurrentMonth){
		getUrl("http://www.hi.189.cn/service/bill/exportDetailInfo.jsp?prodnum=MVWCdY0vDvFeiJ7Pmdzsqg%3D%3D&citycode=0898&qrydate=" + dstr + "&type=1&productid=50", null, new String[] {"gbk"}, new AbstractProcessorObserver(util, WaringConstaint.HNDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveFlowLog(context, page, d, dstr, isCurrentMonth);
			}
		});
	}
	private void saveFlowLog(SimpleObject context, final int page, final Date d, final String dstr, final boolean isCurrentMonth){
		try{
			
			Document doc = ContextUtil.getDocumentOfContent(context);
			//System.out.println(doc);
			Elements tables = doc.select("table");
			if(tables.size()<=1){
				
			}else{
				setStatus(STAT_SUC);
				notifyStatus();
				
				Elements trs1 = tables.get(0).select("tr");
				try{
					String flowScale = trs1.get(3).select("td").get(0).text().substring(5);
					String flowNumStr = trs1.get(4).select("td").get(0).text().substring(4);
					Double flowNum = StringUtil.flowFormat(flowNumStr);
					String flowTimeStr = trs1.get(4).select("td").get(1).text();
					int endFlagIndex1 = flowTimeStr.indexOf('（'); 
					String flowTime = flowTimeStr.substring(4, endFlagIndex1);
					String flowFeeStr = trs1.get(5).select("td").get(0).text();
					int endFlagIndex2 = flowFeeStr.indexOf('（');
					String flowFee = flowFeeStr.substring(4, endFlagIndex2);
					DianXinFlow flowBill = new DianXinFlow();
					flowBill.setDependCycle(flowScale);
					flowBill.setAllFlow(new BigDecimal(flowNum));
					flowBill.setAllPay(new BigDecimal(flowFee));
					flowBill.setAllTime(new BigDecimal(flowTime));
					flowBill.setPhone(phoneNo);
					UUID id = UUID.randomUUID();
					flowBill.setId(id.toString());
					flowBill.setQueryMonth(d);
					flowList.add(flowBill);
				}catch(Exception e){
					logger.error("error",e);
				}
				
				
				
				
				Elements trs2 = tables.get(1).select("tr");
				int len = trs2.size();
				//String dstr1 = null;
				for(int i = 1; i < len; i++) {
					Element e = trs2.get(i);
					Elements tds = e.select("td");
					if (tds.size() == 1) {
						//dstr1 = tds.text();
					} else {
						String kssj = tds.get(1).text();//开始时间
						String sc = tds.get(2).text();//时长(分钟)
						String ll = tds.get(3).text();//流量
						String wllx = tds.get(4).text();//网络类型：无线宽带（3G/1X）
						String txdd = tds.get(5).text();//通信地点：省际漫游
						String fy = tds.get(6).text();//费用（元）
						DianXinFlowDetail dFlow = new DianXinFlowDetail();
						dFlow.setPhone(phoneNo);
						UUID uuid = UUID.randomUUID();
						dFlow.setId(uuid.toString());
						Long tradeTime = StringUtil.flowTimeFormat(sc);
						dFlow.setTradeTime(tradeTime);
						dFlow.setBeginTime(DateUtils.StringToDate(kssj, "yyyy-MM-dd HH:mm:ss"));
						dFlow.setLocation(txdd);
						dFlow.setFee(new BigDecimal(fy));
						dFlow.setFlow(new BigDecimal(StringUtil.flowFormat(ll)));
						if(wllx.contains("3G")){
							dFlow.setNetType("3G");
						}else if(wllx.contains("4G")){
							dFlow.setNetType("4G");
						}else{
							dFlow.setNetType(wllx.substring(0, 4));
						}
						
						if(isCurrentMonth){
							dFlow.setIscm(1);
						}else{
							dFlow.setIscm(0);
						}
						flowDetailList.add(dFlow);
					}

				}
			}
		}catch(Exception e){
			logger.error("error",e);
		}
	}
	
	
	//Created by Dongyu.Zhang
	private void requestMessageService(final int page, final int t, final Date d, final String dstr){
		getUrl("http://www.hi.189.cn/service/bill/exportDetailInfo.jsp?prodnum=MVWCdY0vDvFeiJ7Pmdzsqg%3D%3D&citycode=0898&qrydate=" + dstr + "&type=6&productid=50", null, new String[] {"gbk"}, new AbstractProcessorObserver(util, WaringConstaint.HNDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveMessageLog(context, page, d, dstr);
			}
		});
	}
	private void saveMessageLog(SimpleObject context, final int page, final Date d, final String dstr){
		try{
			Document doc = ContextUtil.getDocumentOfContent(context);
			//System.out.println(doc);
			Elements trs = doc.select("tr");
			if(trs.size()<=0){
				
			}else{
				setStatus(STAT_SUC);
				notifyStatus();
				int len = trs.size();
				//String dstr1 = null;
				for(int i = 2; i < len; i++) {
					Element e = trs.get(i);
					Elements tds = e.select("th,td");
					if (tds.size() == 1) {
						//dstr1 = tds.text();
					} else {
						TelcomMessage tMessage = new TelcomMessage();
						tMessage.setPhone(phoneNo);
						tMessage.setBusinessType(tds.get(1).text().trim());
						tMessage.setRecevierPhone(tds.get(3).text().trim());
						tMessage.setSentTime(DateUtils.StringToDate(tds.get(4).text(), "yyyy-MM-dd HH:mm:ss"));
						tMessage.setAllPay(Double.parseDouble(tds.get(5).text()));
						Date now = new Date();
						tMessage.setCreateTs(now);
						UUID uuid = UUID.randomUUID();
						tMessage.setId(uuid.toString());
						messageList.add(tMessage);
					}

				}
			}
		}catch(Exception e){
			logger.error("error",e);
		}
	}
	private void requestCallLogService(final int page, final int t, final Date d, final String dstr) {	
		getUrl("http://www.hi.189.cn/service/bill/exportDetailInfo.jsp?prodnum=MVWCdY0vDvFeiJ7Pmdzsqg%3D%3D&citycode=0898&qrydate=" + dstr + "&type=8&productid=50", null, new String[] {"gbk"}, new AbstractProcessorObserver(util, WaringConstaint.HNDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, page, d, dstr);
			}
		});
	}
	private void saveCallLog(SimpleObject context, final int page, final Date d, final String dstr) {
		try {
			Document doc = ContextUtil.getDocumentOfContent(context); 			
			Elements trs = doc.select("tr");
			if (trs.size() <= 0) {		
				
			} else {
				setStatus(STAT_SUC);
				notifyStatus();
				int len = trs.size();
				//String dstr1 = null;
				for(int i = 2; i < len; i++) {
					Element e = trs.get(i);
					Elements tds = e.select("th,td");
					if (tds.size() == 1) {
						//dstr1 = tds.text();
					} else {
						DianXinDetail dxDetail = new DianXinDetail();
						dxDetail.setPhone(phoneNo);
						for(int j=1; j < tds.size(); j++) {
							String text = tds.get(j).text();
							if (j == 5) {
								dxDetail.setcTime(DateUtils.StringToDate(text, "yyyy-MM-dd HH:mm:ss"));
							} else if (j == 1) {
								dxDetail.setCallWay(text);
							} else if (j == 4) {
								dxDetail.setRecevierPhone(text);
							} else if (j == 6) {
								dxDetail.setTradeTime(Integer.parseInt(text));
							} else if (j == 3) {
								dxDetail.setTradeAddr(text);
							} else if (j == 7) {
								dxDetail.setAllPay(new BigDecimal(text));
							} else if (j == 2) {
								dxDetail.setTradeType(text);
							}
						}
						detailList.add(dxDetail);
					}

				}
				//requestCallLogService(page + 1, 1, d, dstr);
			}

		} catch (Exception e) {
			logger.error("error",e);
		}

	}
	
	/**
	* <p>Title: getCityCode</p>
	* <p>Description: 获取cityCode</p>
	* @author Jerry Sun
	*/
/*	private void getCityCode(){
		String[][] pairs = {{"method", "getCityCodeAndIsLogin"}};
		postUrl("http://www.hi.189.cn/ajaxServlet/getCityCodeAndIsLogin", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", pairs, new AbstractProcessorObserver(util, WaringConstaint.HNDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					if(context != null){
						if(ContextUtil.getContent(context).contains("ISLOGIN\":\"true")){
							JSONObject jso = ContextUtil.getJsonOfContent(context);
							entity.put("CITYCODE", jso.getString("CITYCODE"));
						}
					}
				} catch (Exception e) {
					logger.error("海南电信获取cityCode出错！", e);
				}
			}
		});
	}*/
		
	public static void main(String[] args) throws Exception {
		/*for(int i=0; i< 10; i++) {
			System.out.println(i + "=" + (int) (Math.random() * 1000 % 10));
		}
		if (true) {
			return;
		}*/
		
		//海南电信多次尝试容易被锁
		String phoneNo = "18189814687";
		String password = "418840";
		//DebugUtil.addToCookieStore("sh.189.cn",".ybtj.189.cn=");
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		SimpleObject proxyContext = new SimpleObject();
		proxyContext.put(ProxyManager.CONTEXT_PROXY_METHOD, ProxyManager.PROXY_METHOD_UNIQUE);
		proxyContext.put(ProxyManager.CONTEXT_PROXY_MODE, ProxyManager.PROXY_MODE_HTTP_HTTPS);
		//proxy.put(ProxyManager.CONTEXT_PROXY_);
		Proxy p = ProxyManagerFactory.getProxyManager().getProxy(proxyContext);
		spider.setHolderProxy(p);
		HaiNanDianXin dx = new HaiNanDianXin(spider, null, phoneNo, password, "2345", null);
//		dx.initForTest();
		
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.getData().clear();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		dx.printData();
		spider.close();	

	}
}
