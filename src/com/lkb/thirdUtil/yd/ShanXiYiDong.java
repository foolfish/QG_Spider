//陝西
package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Request;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

public class ShanXiYiDong extends AbstractYiDongCrawler {	
	
	public ShanXiYiDong(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	}
	public ShanXiYiDong(Spider spider, WarningUtil util) {
		this.spider = spider;		
		this.util = util;
	}
	public void checkVerifyCode(int index, final String phone) {   
		String prefix = "shanxi";
		goLoginReq(index, prefix, phone);
	}
	private void saveVerifyImage(int index, final String prefix, final String phone) {
		//保存验证码图片
    	String picName =  "sx_yd_code_" + phone + "_" + (int) (Math.random() * 1000 % 30) + "dc3";
    	data.put("checkVerifyCode","1");
		try {
			String imgName = saveFile("https://sn.ac.10086.cn/servlet/CreateImage?" + index, "https://sn.ac.10086.cn/login", null, picName, true);
			data.put("imgName", imgName);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public void sendSmsPasswordForRequireCallLogService(int t) {        
		if (t == 1) {
			Request req = new Request("https://service.sn.10086.cn/app?service=page/feeService.VoiceQueryNew&listener=initPage");
			req.putHeader("Referer", "https://service.sn.10086.cn/app?service=page/feeService.VoiceQueryNew&listener=initPage");
			req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
				@Override
				public void afterRequest(SimpleObject context) {
					////system.out.println(ContextUtil.getDocumentOfContent(context));
					Document doc = ContextUtil.getDocumentOfContent(context); 
					//system.out.println(doc);
					Elements form = doc.select("form[name=FORM1]");
					setStatus(STAT_SUC);
					notifyStatus();
				}
			});
			spider.addRequest(req);
		} else if (t == 2) {
			getUrl("https://service.sn.10086.cn/app?service=ajaxDirect/1/personalinfo.CheckedSms/personalinfo.CheckedSms/javascript/null&pagename=personalinfo.CheckedSms&eventname=reget&partids=null&ajaxSubmitType=get&ajax_randomcode="+Math.random(), "https://service.sn.10086.cn/app?service=page/feeService.VoiceQueryNew&listener=initPage", new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
				@Override
				public void afterRequest(SimpleObject context) {
					////system.out.println(ContextUtil.getDocumentOfContent(context));
					setStatus(STAT_SUC);
					notifyStatus();
				}
			});
		}
	}
	public void requestAllService(String smsCode) {   
		Request req = new Request("https://service.sn.10086.cn/app");
		req.setMethod("POST");
		req.putHeader("Referer", "https://service.sn.10086.cn/app?service=page/feeService.VoiceQueryNew&listener=initPage");
		req.putHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		req.initNameValuePairs(8);
		req.addNameValuePairs("Form1", "bforgot");
		req.addNameValuePairs("bforgot", "%C8%B7++%C8%CF");
//		req.addNameValuePairs("smsLeftNum", "41");
		req.addNameValuePairs("smsLeftNum", "33");
		req.addNameValuePairs("loginFlag", "1");
		req.addNameValuePairs("sp", "S1");
		req.addNameValuePairs("mpageName", "feeService.VoiceQueryNew");
		req.addNameValuePairs("service", "direct/1/personalinfo.CheckedSms/$Form");
		req.addNameValuePairs("SMS_NUMBER", smsCode);
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				//service
				boolean isSuccess = false;
				Document doc = ContextUtil.getDocumentOfContent(context); 
//				System.out.println(doc.select("#service").attr("value"));
				if ("重新获取".equalsIgnoreCase(doc.select("#regetPwd").attr("value"))) {
//				if ("direct/1/personalinfo.CheckedSms/$Form".equalsIgnoreCase(doc.select("#service").attr("value"))) {
					data.put("errMsg", "验证码输入错误！短信密码已发送到手机，请注意查收，请输入短信密码");
					setStatus(STAT_STOPPED_FAIL);
				} else {
					setStatus(STAT_SUC);
					isSuccess = true;
				}
				notifyStatus();
				if (isSuccess) {
					beforeRequestService();
					requestService(null, 1);
					requestService(null, 2);
				}				
			}
		});
		spider.addRequest(req);
		//printData();
	}
	public void goLoginReq() {     
		goLoginReq(0, null, null);
	}
	private void goLoginReq(final int index, final String prefix, final String phone) {      
		Request req = new Request("https://sn.ac.10086.cn/login");
		//req.setMethod("POST");
		req.putHeader("Referer", "https://sn.ac.10086.cn/login");
		//Mozilla/5.0 (Windows NT 5.1; rv:28.0) Gecko/20100101 Firefox/28.0
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginPage(context, index, prefix, phone);
			}
		});
		spider.addRequest(req);
		//printData();
	}
	private void parseLoginPage(SimpleObject context, int index, final String prefix, final String phone) {   
		if (prefix != null) {
			saveVerifyImage(index, prefix, phone);//DebugUtil.printCookieData(ContextUtil.getCookieStore(context), "10086.cn")
		} else {
			Request req = new Request("https://sn.ac.10086.cn/loginAction");
			req.setMethod("POST");
			setRequest(req);
			req.putHeader("Referer", "https://sn.ac.10086.cn/login");
			req.initNameValuePairs(7);
			//userName=14791405282&password=686868&verifyCode=dkex&OrCookies=1&loginType=1&fromUrl=uiue%2Flogin_max.jsp&toUrl=http%3A%2F%2Fwww.sn.10086.cn%2Fmy%2Faccount%2F
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements form = doc.select("form#loginForm");
			req.addNameValuePairs("OrCookies", "0");
			req.addNameValuePairs("fromUrl", form.select("input[name=fromUrl]").val());
			req.addNameValuePairs("loginType", "1");
			req.addNameValuePairs("password", password);
			req.addNameValuePairs("toUrl", form.select("input[name=toUrl]").val());
			req.addNameValuePairs("userName", phoneNo);
			req.addNameValuePairs("verifyCode", authCode == null ? "" : authCode);
			req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_3) {			
				@Override
				public void afterRequest(SimpleObject context) {
					parseLoginText(context);
				}
			});
			spider.addRequest(req);
		}
	}
	private void parseLoginText(SimpleObject context) {    
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		if (scode == 200) {			
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements form = doc.select("form#loginForm");
			if (form.size() > 0) {
				Elements es = doc.select("div#message");
				String text = ContextUtil.getContent(context);
				String msg = null;
				if (es.size() > 0) {
					final Element e1 = es.get(0);
					if (text.indexOf("document.getElementById(\"message\").style.display") >= 0) {
						msg = e1.text();
					} else if (e1.attr("style").indexOf("display:none") < 0) {
						msg = e1.text();
					}
				}
				data.put("errMsg", msg);
				setStatus(STAT_STOPPED_FAIL);
				notifyStatus();
				
			} else {
				setStatus(STAT_LOGIN_SUC);
				
				
				sendSmsPasswordForRequireCallLogService(1);
				sendSmsPasswordForRequireCallLogService(1);
				notifyStatus();
				//requestService(null, 1);
			}
		} else {
			HttpResponse resp = ContextUtil.getResponse(context);
			Header h1 = resp.getFirstHeader("Location");
			String nexturl = h1.getValue();
			//iLoginFrameCas.jsp
			if (nexturl == null) {				
				logger.error("Error : No Redirect URL");    		
			} else {
				
				Request req = new Request(nexturl);
				req.putHeader("Referer", "http://ln.ac.10086.cn/login");
				req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_4) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLoginText(context);
					}
				});
				spider.addRequest(req);
			}
			
		}		
		
	}	
	
	private void beforeRequestService() {
		getUrl("http://service.sn.10086.cn/app?service=page/MyBusiness&listener=initPage", null, null);
		getUrl("http://service.sn.10086.cn/saturn/app?service=page/Home&listener=getCustInfo&%3fidsite=2&rec=1&url=http%253A%252F%252Fwww.sn.10086.cn%252Fmy%252Faccount%252F&res=1920x1080&col=24-bit&h=15&m=37&s=38&cookie=1&urlref=http%253A%252F%252Fwww.sn.10086.cn%252Fmy%252Faccount%252F&rand=0.5511654799969444&pdf=0&qt=0&realp=0&wma=0&dir=0&fla=1&java=1&gears=0&ag=0&action_name=", null, null);
		getUrl("http://service.sn.10086.cn/saturn/app?service=page/Home&listener=getCustInfo&serial_number=" + phoneNo + "&eparchy_code=0912&?idsite=1&rec=1&url=http%3A%2F%2Fservice.sn.10086.cn%2Fapp%3Fservice%3Dpage%2FfeeService.BillQueryNew%26listener%3DquerybyOtherMonth&res=1920x1080&col=24-bit&h=15&m=39&s=14&cookie=1&urlref=http%3A%2F%2Fservice.sn.10086.cn%2Fapp%3Fservice%3Dpage%2FfeeService.BillQueryNew%26listener%3DquerybyOtherMonth&rand=0.8918545423041831&pdf=0&qt=0&realp=0&wma=0&dir=0&fla=1&java=1&gears=0&ag=0&action_name=%2525E7%2525BD%252591%2525E4%2525B8%25258A%2525E8%252590%2525A5%2525E4%2525B8%25259A%2525E5%25258E%252585_%2525E4%2525B8%2525AD%2525E5%25259B%2525BD%2525E7%2525A7%2525BB%2525E5%25258A%2525A8%2525E9%252580%25259A%2525E4%2525BF%2525A1", "http://service.sn.10086.cn/app?service=page/feeService.BillQueryNew&listener=querybyOtherMonth", null);
	}
	private void requestService(SimpleObject context, int t) {
		
		if (t == 1) {
			parseBalanceInfo();
			beforeRequestMonthBillService();
		} else if (t == 2) {
			Date d = new Date();
			for(int i = 0; i < 7; i++) {
				String dstr = DateUtils.formatDate(DateUtils.add(d, Calendar.MONTH, -1 * i), "yyyyMM");
				if (t == 1) {
					if (i > 0) {
//						requestMonthBillService(context, dstr);
						String[][] pairs = new String[3][2];
						requestMonthBillService(pairs);
					}
				} else if (t == 2){
					requestCallLogService(d, dstr);
					requestMessageService(d,dstr);
					getFlow(d, dstr);
				}
			}
		}
		
		//requestCallLogService(context, number, queryDate);
	}
	private void parseBalanceInfo() {
		Request req = new Request("http://service.sn.10086.cn/app?service=page/feeService.BalanceQuery&listener=initPage");
		setRequest(req);
		//req.putHeader("Referer", "http://www.sn.10086.cn/my/account/");
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Element td = doc.select("table.listTable1 td").get(1);
				String b = td.text().replaceAll("&nbsp;", "").replaceAll("元", "").trim();
				b = b.replaceAll(" ", "").replaceAll(" ", "").replaceAll("\\?", "").trim();
				BigDecimal b1= new BigDecimal(b);
				user.setPhoneRemain(b1);
				//Elements div = doc.select("div.fl813");
				//data.put("aaaa", div.text());
				//logger.info("aaa", div.text());
				//parseLoginText(context);
			}
		});
		spider.addRequest(req);
		
		req = new Request("https://service.sn.10086.cn/app?service=page/personalinfo.CustInfoChg&listener=initPage");
		req.putHeader("Referer", "https://service.sn.10086.cn/app?service=page/personalinfo.CustInfoChg&listener=initPage");
		setRequest(req);
		
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements input = doc.select("#CUST_NAME");
				final String userName = input.val();
				//logger.info("aaa", userName);
				user.setRealName(userName);
				//parseLoginText(context);
			}
		});
		spider.addRequest(req);
	}
	private void setRequest(Request req) {
		req.putHeader("Accept",	"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		req.putHeader("Accept-Encoding", "gzip, deflate");
		req.putHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
	}	
	private void test() {
		String param = "service=direct%2F1%2FfeeService.BillQueryNew%2Fbillform&sp=S1&Form1=FLAGBY%2CFLAG%2C%24RadioGroup&FLAGBY=&FLAG=&selectMonth=201409&DATE_THISACCT=2014-10-01&DATE_LASTACCT=2014-09-01&piePic=&barPic=&MONTH=201409";
		getUrl("http://service.sn.10086.cn/app?service=page/feeService.BillQueryNew&listener=querybyOtherMonth&" + param, null, new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				String text = doc.select("div.con").text();
				if (StringUtils.isBlank(text) || text.length() < 3) {
					text = doc.select("div.loginRe_yewuLef").text();
				}
				logger.info(text);
				Elements es = doc.select("tr.title2_2013 td");
				if (es.size() == 0) {
					return;
				}
				logger.info(es.text());
				DebugUtil.findMissing(ContextUtil.getCookieStore(context), "CmProvid=sn; cmtokenid=vj2eYJzMSeb3spP1AwJLvQpubltRN7eN@sn.ac.10086.cn; CmWebtokenid=14791405282,sn; CmWebNumSn=14791405282,sn; eparch_code=0912; flagcity=0000; city=0029; WT_FPC=id=2de2f6df637dde039641410766675596:lv=1410766735922:ss=1410766675596; SATURN_JSESSIONID=QQHbJWWVR31qGrvHMJXJQnXVp4G1y9dTqNdTTpmNnnJXBkYTnTdj!1932990289; VISIT_SESSION_ID=QQHbJWWVR31qGrvHMJXJQnXVp4G1y9dTqNdTTpmNnnJXBkYTnTdj!1932990289!1410766421653; VISIT_CALLER=u1O3PWIav2GDJ36NSnce1xYsr5Bjdzul; ICS_JSESSIONID=hz13JWWJQC0ChLHmXG4yDgnz8TvTsdLwzjvXFyhcpZ05MjGPr1Cp!-2070368112");
			}
		});
	}
	private void beforeRequestMonthBillService() {
		final String url = "http://service.sn.10086.cn/app?service=page/feeService.BillQueryNew&listener=querybyOtherMonth";
		Request req = new Request(url);
		setRequest(req);
		req.putHeader("Referer", url);		
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				String[][] pairs = new String[11][2];
				int i = 0;
				Elements form = doc.select("form#billform");
				pairs[i++] = new String[]{"MONTH", ""};
				pairs[i++] = new String[]{"DATE_LASTACCT", form.select("input[name=DATE_LASTACCT]").attr("value")};
				pairs[i++] = new String[]{"DATE_THISACCT", form.select("input[name=DATE_THISACCT]").attr("value")};
				pairs[i++] = new String[]{"FLAG", form.select("input[name=FLAG]").attr("value")};
				pairs[i++] = new String[]{"FLAGBY", form.select("input[name=FLAGBY]").attr("value")};
				pairs[i++] = new String[]{"Form1", form.select("input[name=Form1]").attr("value")};
				pairs[i++] = new String[]{"service", form.select("input[name=service]").attr("value")};
				pairs[i++] = new String[]{"sp", form.select("input[name=sp]").attr("value")};				
				pairs[i++] = new String[]{"barPic", ""};
				pairs[i++] = new String[]{"piePic", ""};
				pairs[i++] = new String[]{"selectMonth", form.select("input[name=selectMonth]").attr("value")};		
				requestMonthBillService(pairs);
				//DebugUtil.findMissing(ContextUtil.getCookieStore(context), "CmProvid=sn; cmtokenid=vj2eYJzMSeb3spP1AwJLvQpubltRN7eN@sn.ac.10086.cn; CmWebtokenid=14791405282,sn; CmWebNumSn=14791405282,sn; eparch_code=0912; flagcity=0000; city=0029; WT_FPC=id=2de2f6df637dde039641410766675596:lv=1410766735922:ss=1410766675596; SATURN_JSESSIONID=QQHbJWWVR31qGrvHMJXJQnXVp4G1y9dTqNdTTpmNnnJXBkYTnTdj!1932990289; VISIT_SESSION_ID=QQHbJWWVR31qGrvHMJXJQnXVp4G1y9dTqNdTTpmNnnJXBkYTnTdj!1932990289!1410766421653; VISIT_CALLER=u1O3PWIav2GDJ36NSnce1xYsr5Bjdzul; ICS_JSESSIONID=hz13JWWJQC0ChLHmXG4yDgnz8TvTsdLwzjvXFyhcpZ05MjGPr1Cp!-2070368112");
			}
		});
		spider.addRequest(req);
	}
	private void requestMonthBillService(String[][] pairs) {
		//Document doc = ContextUtil.getDocumentOfContent(context);
		Date cd = new Date();
		for(int i = 1; i < 7; i++) {
			Date d = DateUtils.add(cd, Calendar.MONTH, -1 * i);
			final String dstr = DateUtils.formatDate(d, "yyyyMM");
			if (i > 0) {
				final String url = "http://service.sn.10086.cn/app?service=page/feeService.BillQueryNew&listener=querybyOtherMonth";
				Request req = new Request(url);
				pairs[0][1] = dstr;
				Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
				String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
				String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
				pairs[1][1] = bd;
				pairs[2][1] = ed;
				req.setNameValuePairs(pairs);		

				req.setMethod("POST");
				req.putHeader("Referer", url);		
				req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
					@Override
					public void afterRequest(SimpleObject context) {
						//logger.info(ContextUtil.getContent(context));
						Document doc = ContextUtil.getDocumentOfContent(context); 
						//System.out.println(doc);
						String text = doc.select("div.con").text();
						//System.out.println(text);
						if (StringUtils.isBlank(text) || text.length() < 3) {
							text = doc.select("div.loginRe_yewuLef").text();
							logger.info(text);
						}
						
						Elements es = doc.select("tr.title2_2013 td");
						if (es.size() == 0) {
							return;
						}
						BigDecimal b1= new BigDecimal(StringUtil.convertWML(es.get(es.size() - 1).text()).replaceAll("￥", "").trim());

						MobileTel tel = new MobileTel();
						tel.setcTime(DateUtils.StringToDate(dstr, "yyyyMM"));
						tel.setcAllPay(b1);
						data.put("Month" + dstr, dstr);
						data.put("Pay" + dstr, StringUtil.convertWML(es.text()));
						tel.setTeleno(phoneNo);
						getTelList().add(tel);

					}
				});
				spider.addRequest(req);
			}
		}

	}
	private void requestCallLogService(Date d, final String dstr) {
		
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		final String year = DateUtils.formatDate(ds[0], "yyyy-");
		String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
		String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		
		final String url = "http://service.sn.10086.cn/app?service=page/feeService.BillQueryNew&listener=querybyOtherMonth";
		//Request req = new Request("https://service.sn.10086.cn/app?service=direct/1/feeService.VoiceQueryNew/$Form&sp=S1&Form1=bquery&MONTH_DAY=2014-10-01&LAST_MONTH_DAY=2014-09-01&SERIAL_NUMBER=14791405282&CUST_NAME=绍兴中导电子科技有限公司&PRODUCT_NAME=车辆定位（全省）&MONTH=201407&SHOW_TYPE=1&BILL_TYPE=201&bquery=查询");
		Request req = new Request("https://service.sn.10086.cn/app");
		String[][] pairs = new String[12][2];
		int i = 0;
		pairs[i++] = new String[]{"BILL_TYPE", "201"};
		pairs[i++] = new String[]{"CUST_NAME", "aa"};
		pairs[i++] = new String[]{"Form1", "bquery"};
		pairs[i++] = new String[]{"LAST_MONTH_DAY", bd};
		pairs[i++] = new String[]{"MONTH_DAY", ed};
		pairs[i++] = new String[]{"PRODUCT_NAME", dstr};
		pairs[i++] = new String[]{"SERIAL_NUMBER", phoneNo};
		pairs[i++] = new String[]{"SHOW_TYPE", "1"};
		pairs[i++] = new String[]{"service", "direct/1/feeService.VoiceQueryNew/$Form"};
		pairs[i++] = new String[]{"sp", "S1"};
		pairs[i++] = new String[]{"MONTH", dstr};
		pairs[i++] = new String[]{"bquery", "查询"};
		req.setNameValuePairs(pairs);		
		//service=direct%2F1%2FfeeService.VoiceQueryNew%2F%24Form&sp=S1&Form1=bquery&MONTH_DAY=2014-10-01&LAST_MONTH_DAY=2014-09-01&SERIAL_NUMBER=14791405282
		//&CUST_NAME=%C9%DC%D0%CB%D6%D0%B5%BC%B5%E7%D7%D3%BF%C6%BC%BC%D3%D0%CF%DE%B9%AB%CB%BE&PRODUCT_NAME=%B3%B5%C1%BE%B6%A8%CE%BB%A3%A8%C8%AB%CA%A1%A3%A9&MONTH=201407&SHOW_TYPE=1
		//&BILL_TYPE=201&bquery=%B2%E9%D1%AF
		req.setMethod("post");
		req.putHeader("Referer", url);		
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
			/*@Override
			public void beforeRequest(SimpleObject context) {
				DebugUtil.findMissing(CookieStoreUtil.putContextToCookieStore(null, 1), "WT_FPC=id=27cc58e1c1f36ce9faa1407146940739:lv=1410771763765:ss=1410771435549; _gscu_1502255179=08773884xdfewd18; WEBTRENDS_ID=219.143.103.242-1408773957.983491; _gscu_1757501843=087739831n244298; CmWebNumSn=14791405282,sn; SATURN_JSESSIONID=QQvnJWpHpChLf6d5Xf2mn5Grpbxv3swW0l4HvtFtJPxmtJ43HpXZ!1932990289; VISIT_SESSION_ID=QQvnJWpHpChLf6d5Xf2mn5Grpbxv3swW0l4HvtFtJPxmtJ43HpXZ!1932990289!1410771239508; VISIT_CALLER=Fb51YCMblzDzm0RXNoMvPXc5q1HATm8E; ICS_JSESSIONID=1nd0JWyKlMcJ5XGQNy2FPcwt0jlSvwT04LSHtnhrwRhWRKR3z9qP!1474086964; eparch_code=0912; flagcity=0000; CmProvid=sn; cmtokenid=KuDkk8mVNkT4aN863OAlvrGshgm1voQz@sn.ac.10086.cn; CmWebtokenid=14791405282,sn; city=0029");
			}*/
			@Override
			public void afterRequest(SimpleObject context) {
				//logger.info(CookieStoreUtil.putContextToCookieStore(null, 1).toString());
				Document doc = ContextUtil.getDocumentOfContent(context); //logger.info(ContextUtil.getContent(context));
				 //StringEscapeUtils.unescapeHtml4(doc.toString());
				//System.out.println(StringEscapeUtils.unescapeHtml4(doc.toString()));
				Elements es = doc.select("#table");
				Elements es1 = es.select("tr");
				//String dstr1 = null;
				for(int i=1; i < es1.size(); i++) {
					try {
						Element e = es1.get(i);
						Elements es2 = e.select("th,td");
						if (es2.size() == 1) {
							//dstr1 = es2.text();
						} else {
							es2 = e.select("td");
							if (es2.size() != 7) {
								throw new IllegalArgumentException();
							}
							MobileDetail dxDetail = new MobileDetail();
							dxDetail.setPhone(phoneNo);
							for(int j=0; j < es2.size(); j++) {
								String text = StringUtil.convertWML(es2.get(j).text());
								if (j == 5) {
									dxDetail.setTradeType(text);
								} else if (j == 3) {
									dxDetail.setRecevierPhone(text);
								} else if (j == 1) {
									dxDetail.setTradeAddr(text);					
								} else if (j == 0) {
									dxDetail.setcTime(DateUtils.StringToDate(year + " " + text, "yyyy-MM-dd HH:mm:ss"));
								} else if (j == 4) {
									dxDetail.setTradeTime(new TimeUtil().timetoint(text));
								} else if (j == 6) {
									dxDetail.setOnlinePay(new BigDecimal(text));
								} else if (j == 2) {
									dxDetail.setTradeWay(text);					
								}
							}
							detailList.add(dxDetail);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		spider.addRequest(req);
	
	}
	
	private void requestMessageService(Date d, final String dstr) {
		
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		final String year = DateUtils.formatDate(ds[0], "yyyy-");
		String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
		String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		final String url = "http://service.sn.10086.cn/app?service=page/feeService.VoiceQueryNew&listener=querybyOtherMonth";
		//Request req = new Request("https://service.sn.10086.cn/app?service=direct/1/feeService.VoiceQueryNew/$Form&sp=S1&Form1=bquery&MONTH_DAY=2014-10-01&LAST_MONTH_DAY=2014-09-01&SERIAL_NUMBER=14791405282&CUST_NAME=绍兴中导电子科技有限公司&PRODUCT_NAME=车辆定位（全省）&MONTH=201407&SHOW_TYPE=1&BILL_TYPE=201&bquery=查询");
		Request req = new Request("https://service.sn.10086.cn/app");
		String[][] pairs = new String[12][2];
		int i = 0;
		pairs[i++] = new String[]{"BILL_TYPE", "202"};
		pairs[i++] = new String[]{"CUST_NAME", "aa"};
		pairs[i++] = new String[]{"Form1", "bquery"};
		pairs[i++] = new String[]{"LAST_MONTH_DAY", bd};
		pairs[i++] = new String[]{"MONTH_DAY", ed};
		pairs[i++] = new String[]{"PRODUCT_NAME", dstr};
		pairs[i++] = new String[]{"SERIAL_NUMBER", phoneNo};
		pairs[i++] = new String[]{"SHOW_TYPE", "1"};
		pairs[i++] = new String[]{"service", "direct/1/feeService.VoiceQueryNew/$Form"};
		pairs[i++] = new String[]{"sp", "S1"};
		pairs[i++] = new String[]{"MONTH", dstr};
		pairs[i++] = new String[]{"bquery", "查询"};
		req.setNameValuePairs(pairs);		
		//service=direct%2F1%2FfeeService.VoiceQueryNew%2F%24Form&sp=S1&Form1=bquery&MONTH_DAY=2014-10-01&LAST_MONTH_DAY=2014-09-01&SERIAL_NUMBER=14791405282
		//&CUST_NAME=%C9%DC%D0%CB%D6%D0%B5%BC%B5%E7%D7%D3%BF%C6%BC%BC%D3%D0%CF%DE%B9%AB%CB%BE&PRODUCT_NAME=%B3%B5%C1%BE%B6%A8%CE%BB%A3%A8%C8%AB%CA%A1%A3%A9&MONTH=201407&SHOW_TYPE=1
		//&BILL_TYPE=201&bquery=%B2%E9%D1%AF
		req.setMethod("post");
		req.putHeader("Referer", url);		
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
			/*@Override
			public void beforeRequest(SimpleObject context) {
				DebugUtil.findMissing(CookieStoreUtil.putContextToCookieStore(null, 1), "WT_FPC=id=27cc58e1c1f36ce9faa1407146940739:lv=1410771763765:ss=1410771435549; _gscu_1502255179=08773884xdfewd18; WEBTRENDS_ID=219.143.103.242-1408773957.983491; _gscu_1757501843=087739831n244298; CmWebNumSn=14791405282,sn; SATURN_JSESSIONID=QQvnJWpHpChLf6d5Xf2mn5Grpbxv3swW0l4HvtFtJPxmtJ43HpXZ!1932990289; VISIT_SESSION_ID=QQvnJWpHpChLf6d5Xf2mn5Grpbxv3swW0l4HvtFtJPxmtJ43HpXZ!1932990289!1410771239508; VISIT_CALLER=Fb51YCMblzDzm0RXNoMvPXc5q1HATm8E; ICS_JSESSIONID=1nd0JWyKlMcJ5XGQNy2FPcwt0jlSvwT04LSHtnhrwRhWRKR3z9qP!1474086964; eparch_code=0912; flagcity=0000; CmProvid=sn; cmtokenid=KuDkk8mVNkT4aN863OAlvrGshgm1voQz@sn.ac.10086.cn; CmWebtokenid=14791405282,sn; city=0029");
			}*/
			@Override
			public void afterRequest(SimpleObject context) {
				//logger.info(CookieStoreUtil.putContextToCookieStore(null, 1).toString());
				Document doc = ContextUtil.getDocumentOfContent(context); //logger.info(ContextUtil.getContent(context));
				//StringEscapeUtils.unescapeHtml4(doc.toString());
				//System.out.println(StringEscapeUtils.unescapeHtml4(doc.toString()));
				Elements es = doc.select("table");
				if(es.size()>1){
					if(es.get(1)==null||es.get(1).toString().equals("")){
						return;
					}
					Elements es1 = es.get(1).select("tr");
					//String dstr1 = null;
					for(int i=1; i < es1.size(); i++) {
						try {
							Element e = es1.get(i);
							Elements es2 = e.select("th,td");
							if (es2.size() == 1) {
								//dstr1 = es2.text();
							} else {
								es2 = e.select("td");
								if (es2.size() != 6) {
									throw new IllegalArgumentException();
								}
								
								String time = StringUtil.convertWML(es2.get(0).text());
								String receivePhone = es2.get(1).text();
								String type = StringUtil.convertWML(es2.get(3).text());
								String allPay = es2.get(5).text();
								
								MobileMessage mMessage = new MobileMessage();
								mMessage.setPhone(phoneNo);
								mMessage.setCreateTs(new Date());
								mMessage.setAllPay(new BigDecimal(allPay));
								UUID uuid = UUID.randomUUID();
								mMessage.setId(uuid.toString());
								mMessage.setSentAddr("陝西");
								mMessage.setRecevierPhone(receivePhone);
								mMessage.setSentTime(DateUtils.StringToDate(year + " " + time, "yyyy-MM-dd HH:mm:ss"));
								if(type.contains("接收")){
									mMessage.setTradeWay("接收");
								}else{
									mMessage.setTradeWay("发送");
								}
								mobileList.add(mMessage);
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		spider.addRequest(req);
	
	}
	
	private void getFlow(Date d, final String dstr){
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		final String year = DateUtils.formatDate(ds[0], "yyyy-");
		String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
		String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		final String url = "http://service.sn.10086.cn/app?service=page/feeService.VoiceQueryNew&listener=querybyOtherMonth";
		//Request req = new Request("https://service.sn.10086.cn/app?service=direct/1/feeService.VoiceQueryNew/$Form&sp=S1&Form1=bquery&MONTH_DAY=2014-10-01&LAST_MONTH_DAY=2014-09-01&SERIAL_NUMBER=14791405282&CUST_NAME=绍兴中导电子科技有限公司&PRODUCT_NAME=车辆定位（全省）&MONTH=201407&SHOW_TYPE=1&BILL_TYPE=201&bquery=查询");
		Request req = new Request("https://service.sn.10086.cn/app");
		String[][] pairs = new String[12][2];
		int i = 0;
		pairs[i++] = new String[]{"BILL_TYPE", "203"};
		pairs[i++] = new String[]{"CUST_NAME", "aa"};
		pairs[i++] = new String[]{"Form1", "bquery"};
		pairs[i++] = new String[]{"LAST_MONTH_DAY", bd};
		pairs[i++] = new String[]{"MONTH_DAY", ed};
		pairs[i++] = new String[]{"PRODUCT_NAME", dstr};
		pairs[i++] = new String[]{"SERIAL_NUMBER", phoneNo};
		pairs[i++] = new String[]{"SHOW_TYPE", "0"};
		pairs[i++] = new String[]{"service", "direct/1/feeService.VoiceQueryNew/$Form"};
		pairs[i++] = new String[]{"sp", "S1"};
		pairs[i++] = new String[]{"MONTH", dstr};
		pairs[i++] = new String[]{"bquery", "查询"};
		req.setNameValuePairs(pairs);		
		req.setMethod("post");
		req.putHeader("Referer", url);
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
			private String allFlow;

			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					/**
					 * 流量详单
					 */
					if(ContextUtil.getContent(context).contains("起始时间")){
						Elements tables = ContextUtil.getDocumentOfContent(context).select("#table");
						if(tables.size()>0){
							Element table = tables.get(0);
							Elements tbodies = table.getElementsByTag("tbody");
							if(tbodies.size()>0){
								Element tbody = tbodies.get(0);
								Elements trs = tbody.getElementsByTag("tr");
								if(trs.size()>0){
//									MobileOnlineList bean = new MobileOnlineList();
//									bean = mobileOnlineListService.getMaxTime(phoneNo);
									for (Element tr : trs) {
										MobileOnlineList mol = new MobileOnlineList();
										mol.setPhone(phoneNo);
										Elements tds = tr.getElementsByTag("td");
										if(tds.size()>0){
											for(int i=0;i<tds.size();i++){
												String temp = StringEscapeUtils.unescapeHtml4(tds.get(i).text());	//返回值是html实体字符，需转换为正常字符
												switch (i) {
												case 0:
													mol.setcTime(DateUtils.StringToDate(Calendar.getInstance().getTime().getYear()+"-"+temp, "yyyy-MM-dd hh:mm:ss"));
													break;
												case 1:
													mol.setTradeAddr(temp);
													break;
												case 2:
													mol.setOnlineType(temp);
													break;
												case 3:
													if(temp.contains("小于"))
														temp = temp.replace("小于", "");
													mol.setOnlineTime(StringUtil.flowTimeFormat(temp));
													break;
												case 4:
													mol.setTotalFlow(StringUtil.flowFormat(temp).longValue());
													break;
												}
											}
										}
										//一行结束表示一条数据结束，存储
										/*if (bean.getcTime()!=null&&bean.getcTime().getTime()<=mol.getcTime().getTime()) {
											continue ;
										}else {
											UUID uuid = UUID.randomUUID();
											mol.setId(uuid.toString());
											list.add(mol);
										}
										mobileOnlineListService.insertbatch(list);*/
										flowDetailList.add(mol);
									}
								}
							}
						}
						getFlowNextPage(1);
					}
					/*
					 * 流量账单
					 */
					if(ContextUtil.getContent(context).contains("总条数")){
						MobileOnlineBill mob = new MobileOnlineBill();
						mob.setPhone(phoneNo);
						mob.setMonthly(DateUtils.StringToDate(dstr, "yyyyMM"));
						String text = ContextUtil.getContent(context);
						String allFlow = StringUtil.subStr("总流量:<b>", "(", text); // 11 M 58.85 K
						allFlow = allFlow.replaceAll(" ", "");
						mob.setTotalFlow(StringUtil.flowFormat(allFlow).longValue());
						if(DateUtils.isSameMonth(DateUtils.getToday("yyyy-MM"), dstr.substring(0, 4)+"-"+dstr.substring(4, 6)))
							mob.setIscm(1);
						else
							mob.setIscm(0);
						
						/*MobileOnlineBill mob_bean = mobileOnlineBillService.getMaxTime(phoneNo);
						if (mob_bean.getMonthly()!=null&&mob_bean.getMonthly().getTime() < mob.getMonthly().getTime()) {
							
						}else if(mob_bean.getMonthly()!=null&&mob_bean.getMonthly().getTime() == mob.getMonthly().getTime()){
							mobileOnlineBillService.update(mob);
						}else {
							UUID uuid = UUID.randomUUID();
							mob.setId(uuid.toString());
							mobileOnlineBillService.save(mob);
						}*/
						flowList.add(mob);
					}
				}
			}
		});
		spider.addRequest(req);
	}
	
	private void getFlowNextPage(final int page){
//		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
//		final String year = DateUtils.formatDate(ds[0], "yyyy-");
//		String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
//		String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		final String url = "https://service.sn.10086.cn/app";
//		Request req = new Request("https://service.sn.10086.cn/app?service=ajaxDirect/1/feeService.VoiceQueryNewResult/feeService.VoiceQueryNewResult/javascript/refushBusiSearchResult&pagename=feeService.VoiceQueryNewResult&eventname=queryAll&pagination_iPage="+(page+1)+"&partids=refushBusiSearchResult&ajaxSubmitType=get&ajax_randomcode="+Math.random());
		Request req = new Request("https://service.sn.10086.cn/app");
		String[][] pairs = new String[12][2];
		int i = 0;
		pairs[i++] = new String[]{"service", "ajaxDirect/1/feeService.VoiceQueryNewResult/feeService.VoiceQueryNewResult/javascript/refushBusiSearchResult"};
		pairs[i++] = new String[]{"pagename", "feeService.VoiceQueryNewResult"};
		pairs[i++] = new String[]{"eventname", "queryAll"};
		pairs[i++] = new String[]{"pagination_iPage", String.valueOf(page+1)};
		pairs[i++] = new String[]{"partids", "refushBusiSearchResult"};
		pairs[i++] = new String[]{"ajaxSubmitType", "get"};
		pairs[i++] = new String[]{"ajax_randomcode", String.valueOf(Math.random())};
		req.setNameValuePairs(pairs);		
		req.setMethod("get");
		req.putHeader("Referer", url);
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXYD_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					/**
					 * 流量详单
					 */
					if(ContextUtil.getContent(context).contentEquals("客户信息")){
						String content = StringUtil.subStr("<part id=\"refushBusiSearchResult\">", "</part>", ContextUtil.getContent(context));
						Elements tables = Jsoup.parse(content).select("#table");
						if(tables.size()>0){
							Element table = tables.get(0);
							Elements tbodies = table.getElementsByTag("tbody");
							if(tbodies.size()>0){
								Element tbody = tbodies.get(0);
								Elements trs = tbody.getElementsByTag("tr");
								if(trs.size()>0){
									/*List<MobileOnlineList> list = new ArrayList();
									MobileOnlineList bean = new MobileOnlineList();
									bean = mobileOnlineListService.getMaxTime(phoneNo);*/
									for (Element tr : trs) {
										MobileOnlineList mol = new MobileOnlineList();
										mol.setPhone(phoneNo);
										Elements tds = tr.getElementsByTag("td");
										if(tds.size()>0){
											for(int i=0;i<tds.size();i++){
												String temp = StringEscapeUtils.unescapeHtml4(tds.get(i).text());	//返回值是html实体字符，需转换为正常字符
												switch (i) {
												case 0:
													mol.setcTime(DateUtils.StringToDate(Calendar.getInstance().getTime().getYear()+"-"+temp, "yyyy-MM-dd hh:mm:ss"));
													break;
												case 1:
													mol.setTradeAddr(temp);
													break;
												case 2:
													mol.setOnlineType(temp);
													break;
												case 3:
													if(temp.contains("小于"))
														temp = temp.replace("小于", "");
													mol.setOnlineTime(StringUtil.flowTimeFormat(temp));
													break;
												case 4:
													mol.setTotalFlow(StringUtil.flowFormat(temp).longValue());
													break;
												}
											}
										}
										//一行结束表示一条数据结束，存储
										/*if (bean.getcTime()!=null&&bean.getcTime().getTime()<=mol.getcTime().getTime()) {
											continue ;
										}else {
											UUID uuid = UUID.randomUUID();
											mol.setId(uuid.toString());
											list.add(mol);
										}
										mobileOnlineListService.insertbatch(list);*/
										flowDetailList.add(mol);
									}
								}
							}
						}
						getFlowNextPage(page+1);
					}
				}
			}
		});
		spider.addRequest(req);
	}
	
	
	public static void main(String[] args) throws Exception {
		String phoneNo = "14791405282";
		String password = "686868";
		//18220857806 920918
		////system.out.println(StringUtil.convertWML("https&#x3a;&#x2f;&#x2f;service.ha.10086.cn&#x2f;acauthen.jsp"));
		if (true) {
			//return;
		}
		/*FileInputStream fis = new FileInputStream("e:/soft/aa.txt");
		FileOutputStream fos = new FileOutputStream("e:/soft/bb.txt");
		String os = IOUtils.toString(fis, "utf-8");
		IOUtils.write(StringUtil.convertWML("https&#x3a;&#x2f;&#x2f;service.ha.10086.cn&#x2f;acauthen.jsp), fos, "utf-8");
		IOUtils.closeQuietly(fis);
		IOUtils.closeQuietly(fos);*/
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		ShanXiYiDong dx = new ShanXiYiDong(spider, null, phoneNo, password, null, null);
//		dx.initForTest();
		boolean isTestService = false;
		if (isTestService) {
			//DebugUtil.addToCookieStore("sn.10086.cn", "cmtokenid=CNe1ddtIKf0YI5ivy5JMBSPioiK0F49u@sn.ac.10086.cn; CmWebtokenid=14791405282,sn; eparch_code=0912; city=0029; CmProvid=sn; CmWebNumSn=14791405282,sn; WT_FPC=id=2de2f6df637dde039641410766675596:lv=1410945557480:ss=1410944564581; VISIT_CALLER=Wn2Cwccwh8e2u8cK8gnsEvFVrgYd1747; SATURN_JSESSIONID=ZmRbJZRJM5tswGPmzcGfNW131h9cnqNTZn8Pvt2kGDxVnfyphy3Q!1932990289; VISIT_SESSION_ID=ZmRbJZRJM5tswGPmzcGfNW131h9cnqNTZn8Pvt2kGDxVnfyphy3Q!1932990289!1410945321647; ICS_JSESSIONID=0nXvJZ8LR2QG00nwPxZnLzGRTpyVYdyssc7Jx88SCQdbLxCvVFdN!-338057719");
			/*Date d = new Date();
			String dstr = DateUtils.formatDate(DateUtils.add(d, Calendar.MONTH, -1 * 1), "yyyyMM");*/
			dx.test();
			dx.beforeRequestMonthBillService();
			spider.start();
		} else {
			dx.checkVerifyCode(1, phoneNo);
			spider.start();
			dx.printData();
			dx.getData().clear();
			dx.setAuthCode(CUtil.inputYanzhengma());
			dx.goLoginReq();
			//dx.sendSmsPasswordForRequireCallLogService(2);
			spider.start();
			
			if (dx.isSuccess()) {
				dx.parseBalanceInfo();
				//dx.beforeRequestService();
				//dx.requestService(null, 1);	
				
				/*dx.setAuthCode(CUtil.inputYanzhengma("短信码："));//dx.sendSmsPasswordForRequireCallLogService(1);
				dx.requestAllService(dx.getAuthCode());*/
				spider.start();
				dx.printData();
				dx.getData().clear();
			}
		}
		if (true) {
			return;
		}
	}
}
