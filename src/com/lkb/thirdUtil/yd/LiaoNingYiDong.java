package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONException;
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
import com.lkb.bean.SimpleObject;
import com.lkb.bean.User;
import com.lkb.robot.Request;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.robot.util.CookieStoreUtil;
import com.lkb.thirdUtil.StatusTracker;
import com.lkb.thirdUtil.dx.BJDianxin;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;
/**
 * @author think
 * @date 2014-8-16
 */
public class LiaoNingYiDong extends AbstractYiDongCrawler {	
	
	public LiaoNingYiDong(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	public LiaoNingYiDong(Spider spider, WarningUtil util) {
		this.spider = spider;		
		this.util = util;
	}	
	
	public void checkVerifyCode(final String userName) {        	
    	Request req = new Request("http://ln.ac.10086.cn/VerifyCodeFlagServlet?username=" + userName);
    	//req.setMethod("POST");
    	req.putHeader("Referer", "http://ln.ac.10086.cn/login");
    	req.putHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    	
    	req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				data.put("checkVerifyCode", text);
				if ("1".equalsIgnoreCase(text)) {
					getUrl("http://ln.ac.10086.cn/login", "http://ln.ac.10086.cn/login", new AbstractProcessorObserver(util, WaringConstaint.LNYD_1) {
						public void afterRequest(SimpleObject context) {
							String picName =  "ln_yd_code_" + userName + "_" + (int) (Math.random() * 1000) + "adb";
							try {
								String imgName = saveFile("http://ln.ac.10086.cn/createVerifyImageServlet?datetime=" + new Date().toString().replaceAll(" ", "%20"), "http://ln.ac.10086.cn/login", "ln.ac.10086.cn", picName, true);
								data.put("imgName", imgName);
							} catch (Exception e) {
								e.printStackTrace();
							}
							//data.put("cookies", CookieStoreUtil.putContextToCookieStore(null, 1));
						}
					});
					
				} else {			
					notifyStatus();
				}
				//data.put("sessionId", ContextUtil.getCookieValue(context, "JSESSIONID"));
				//notifyStatus();
			}
		});
    	spider.addRequest(req);
    	//printData();
    }
	public void sendSmsPasswordForRequireCallLogService(int t) {       
		/*spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.LNYD_6) {
			@Override
			public void afterRequest(SimpleObject context) {
			}
		}, "http://www.ln.10086.cn/my/account/detailquery.xhtml");*/
		if (t == 1) {
			Request req = new Request("http://www.ln.10086.cn/busicenter/detailquery/DetailQueryMenuAction/initBusi.menu");
			req.setMethod("POST");
			req.putHeader("Referer", "http://www.ln.10086.cn/my/account/detailquery.xhtml");
			/*req.putHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		req.putHeader("X-Requested-With", "XMLHttpRequest");
			 */
			req.initNameValuePairs(2);
			//_menuId=40399&commonSmsPwd=
			req.addNameValuePairs("_menuId", "40399");
			req.addNameValuePairs("divId", "main");
			req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_1) {
				@Override
				public void beforeRequest(SimpleObject context) {

				}
				@Override
				public void afterRequest(SimpleObject context) {
					//data.put("cookies", CookieStoreUtil.putContextToCookieStore(null, 1));
					//data.put("text", ContextUtil.getContent(context));
					setStatus(STAT_SUC);
					notifyStatus();
					//data.put("cookies", ContextUtil.getCookieStore(context));
				}
			});
			spider.addRequest(req);
		}
		if (t == 2) {
			Request req = new Request("http://www.ln.10086.cn/busicenter/detailquery/DetailQueryMenuAction/reSendSmsPassWd.menu");
			req.setMethod("POST");
			req.putHeader("Referer", "http://www.ln.10086.cn/my/account/detailquery.xhtml");
			req.putHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			//req.putHeader("X-Requested-With", "XMLHttpRequest");
			req.putHeader("Accept", "application/json, text/javascript, */*; q=0.01");

			req.initNameValuePairs(2);
			//_menuId=40399&commonSmsPwd=
			req.addNameValuePairs("_menuId", "40399");
			req.addNameValuePairs("commonSmsPwd", "");
			req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_1) {
				@Override
				public void beforeRequest(SimpleObject context) {
					data.put("cookies", CookieStoreUtil.putContextToCookieStore(null, 1));
				}
				@Override
				public void afterRequest(SimpleObject context) {
					JSONObject json = null;
					try {
						json = ContextUtil.getJsonOfContent(context);

						if (!json.isNull("sendResult") && "success".equalsIgnoreCase(json.getString("sendResult"))) {
							setStatus(STAT_SUC);
						} else {
							if (!json.isNull("errorMsg")) {
								data.put("errorMsg", json.getString("errorMsg"));
							}
						}
						notifyStatus();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					data.put("text", ContextUtil.getContent(context));
					//data.put("cookies", ContextUtil.getCookieStore(context));
				}
			});
			spider.addRequest(req);
		}
		//printData();
	}
	public void requestAllService(String smsCode) {   
		Request req = new Request("http://www.ln.10086.cn/busicenter/detailquery/DetailQueryMenuAction/checkSmsPassWd.menu");
		req.setMethod("POST");
		req.putHeader("Referer", "http://www.ln.10086.cn/my/account/detailquery.xhtml");
		req.putHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		req.initNameValuePairs(2);
		
		req.addNameValuePairs("_menuId", "40399");
		req.addNameValuePairs("commonSmsPwd", smsCode);
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_1) {
			public void preparedData(SimpleObject context) {
				if (ContextUtil.getError(context) != null) {
					notifyStatus();
				}
				super.preparedData(context);
			}
			@Override
			public void afterRequest(SimpleObject context) {
				JSONObject json = null;
				try {
					json = ContextUtil.getJsonOfContent(context);
					boolean isSuccess = false;
					if (!json.isNull("checkResult") && "success".equalsIgnoreCase(json.getString("checkResult"))) {
						isSuccess = true;
						setStatus(STAT_SUC);
					} else {
						if (!json.isNull("errorMsg")) {
							data.put("errorMsg", json.getString("errorMsg"));
						}
					}
					notifyStatus();
					if (isSuccess) {
						requestService(null, 2);
						requestService(context, 1);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					notifyStatus();
				}
				
			}
		});
		spider.addRequest(req);
		//printData();
	}
	public void goLoginReq() {        	
		Request req = new Request("http://ln.ac.10086.cn/login");
		//req.setMethod("POST");
		req.putHeader("Referer", "http://ln.ac.10086.cn/login");
		//Mozilla/5.0 (Windows NT 5.1; rv:28.0) Gecko/20100101 Firefox/28.0
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginPage(context);
			}
		});
		spider.addRequest(req);
		//printData();
	}
	private void parseLoginPage(SimpleObject context) {   
		final Request req = new Request("http://ln.ac.10086.cn/login");
		req.setMethod("POST");
		req.initNameValuePairs(12);
		
		Document doc = ContextUtil.getDocumentOfContent(context); 
		Elements form = doc.select("form#loginForm");
		req.addNameValuePairs("ai_param_loginIndex", form.select("#ai_param_loginIndex").attr("value"));
		req.addNameValuePairs("appId", form.select("#appId").attr("value"));
		req.addNameValuePairs("bigloginlabelselect1", "");
		req.addNameValuePairs("loginPass", password);
		req.addNameValuePairs("loginType", "1");
		req.addNameValuePairs("password", password);
		req.addNameValuePairs("rememNum", "checkbox");
		req.addNameValuePairs("rndPassword", "");
		req.addNameValuePairs("service", "");
		req.addNameValuePairs("userType", "1");
		req.addNameValuePairs("username", phoneNo);
		req.addNameValuePairs("verifyCode", authCode == null ? "" : authCode);
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_3) {			
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginText(context);
			}
		});
		/*postUrl("http://ln.ac.10086.cn/verifyCodeServlet?verifyCode=" + authCode, "http://ln.ac.10086.cn/login", null, new AbstractProcessorObserver(util, WaringConstaint.LNYD_3) {			
			@Override
			public void afterRequest(SimpleObject context) {
				final String text = ContextUtil.getContent(context);
				if ("0000".equalsIgnoreCase(text)) {
					spider.addRequest(req);
				} else {
					//9999
					data.put("errMsg", "输入验证码不正确");
					setStatus(STAT_STOPPED_FAIL);
					notifyStatus();
				}
			}
		});*/
		spider.addRequest(req);
	}
	private void parseLoginText(SimpleObject context) {    
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		if (scode == 200) {
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements form = doc.select("form#loginForm");
			if (!form.isEmpty()) {
				String text = ContextUtil.getContent(context);
				String tips = com.lkb.util.StringUtil.subStrAndRegex("window.onload = function", "loadUsername()", "showError\\('loginTips',\"(.*?)\"\\);", text, 1);
				data.put("errMsg", tips);
				setStatus(STAT_STOPPED_FAIL);
				notifyStatus();
				return;
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
				req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_4) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLoginStep1(context);
					}
				});
				spider.addRequest(req);
			}
			
		}
		
		
	}
	private void parseLoginStep1(SimpleObject context) {
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		if (scode == 302) {
			HttpResponse resp = ContextUtil.getResponse(context);
			Header h1 = resp.getFirstHeader("Location");
			String nexturl = h1.getValue();
			//login?service=
			if (nexturl == null) {
				logger.error("Error : No Redirect URL");    		
			} else {
				Request req = new Request(nexturl);
				req.putHeader("Referer", "http://ln.ac.10086.cn/login");
				req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_5) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLoginStep2(context);
					}
				});
				spider.addRequest(req);
			}
		} else {
			//logger.error("Error : status code=" + scode);
			parseLoginStep2(context);
		}
	}
	private void parseLoginStep2(SimpleObject context) {
		Document doc = ContextUtil.getDocumentOfContent(context); 
		Elements form = doc.select("form#login_form");
		if (form.size() > 0) {
			Request req = new Request(form.attr("action"));
			req.setMethod("POST");
			Elements es = form.select("input");
			req.initNameValuePairs(es.size());
			for(int i = es.size() - 1; i>=0; i--){ 
				req.setNameValuePairs(i, es.get(i).attr("name"), es.get(i).attr("value"));
			}
			req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_6){
				@Override
				public void afterRequest(SimpleObject context) {
					setStatus(StatusTracker.STAT_LOGIN_SUC);
					notifyStatus();
					sendSmsPasswordForRequireCallLogService(1);
				}
			});
			spider.addRequest(req);		
		} else {
			setStatus(StatusTracker.STAT_LOGIN_SUC);
			notifyStatus();
			sendSmsPasswordForRequireCallLogService(1);
		}
	}
	private void requestService(SimpleObject context, int t) {
		if (t == 1) {
			parseBalanceInfo(context);
		}
		Date d = new Date();
		for(int i = 0; i < 7; i++) {
			String dstr = DateUtils.formatDate(DateUtils.add(d, Calendar.MONTH, -1 * i), "yyyyMM");
			if (t == 1) {
				if (i > 0) {
					requestMonthBillService(context, dstr);
				}else{
					requestMonthBillService();
				}
			} else if (t == 2){
				requestCallLogService(dstr);
			}
		}
		
		
		//requestCallLogService(context, number, queryDate);
	}
	private void parseBalanceInfo(SimpleObject context) {
		postUrl("http://www.ln.10086.cn/busicenter/myinfo/MyInfoMenuAction/initBusi.menu?_menuId=1040101&_menuId=1040101", "http://www.ln.10086.cn/my/account/mydata.xhtml", new String[][] {{"divId", "main"}}, new AbstractProcessorObserver(util, WaringConstaint.LNYD_7){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements es = doc.select("ul.myinfo2");
				if (es.size() < 1) {
					return;
				}
				//data.put("text", ContextUtil.getContent(context));
				Element es1 = es.first();
				String t1 = es1.text().trim();
				String birthDay = StringUtil.subStr("出生日期：", "客户地址", t1);
				String addr = StringUtil.subStr("客户地址：", "邮政编码", t1);
				String idnum = StringUtil.subStr("证件号码：", "电子邮件", t1);
				String email = StringUtil.subStr("电子邮件：", "客户级别", t1);
				String userLevel = StringUtil.subStr("客户级别：", "信用度级别", t1);
				String registDate = t1.substring(t1.indexOf("入网时间：")+5);
				user.setBirthday(DateUtils.StringToDate(birthDay, "yyyy-MM-dd"));
				user.setAddr(addr);
				user.setEmail(email);
				user.setMemberLevel(userLevel);
				user.setRegisterDate(DateUtils.StringToDate(registDate, "yyyy-MM-dd"));
				user.setIdcard(idnum);
				data.put("idnum", idnum);
			}
		});
		Request req = new Request("http://www.ln.10086.cn/busicenter/myinfo/MyInfoMenuAction/initBusi.menu?_menuId=10401&_menuId=10401");
		//req.setMethod("POST");

		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_7){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements es = doc.select("div.myaccount1");
				if (es.size() < 1) {
					return;
				}
				//data.put("text", ContextUtil.getContent(context));
				Element es1 = es.select("span").first();
				String userName = es1.select("font").text().trim();
				String t1 = es1.text();
				String phoneNum = com.lkb.util.StringUtil.regex("（(\\d*)）", t1, 1);
				
				//网站改版，样式发生变化，什么数据都抓不到，不建议使用es.select("span:eq(15)")这种方式提取元素
				/*Elements es2 = es.select("span:eq(15)");//.get(11);
				Elements es3 = es.select("span:eq(17)");
				String balance = com.lkb.util.StringUtil.regex("[^\\d]*([\\d\\.]*)[^\\d]*", es2.text(), 1);
				String tfee = com.lkb.util.StringUtil.regex("[^\\d]*([\\d\\.]*)[^\\d]*", es3.text(), 1);*/
				user.setRealName(userName);
				
				String str = es.text().trim();
				String yue = StringUtil.subStr("余额：", "元", str);
				String realPay = StringUtil.subStr("实时话费：", "元", str);
				BigDecimal b1= new BigDecimal(yue);
				user.setPhoneRemain(b1);
				data.put("userName", userName);
				data.put("phoneNum", phoneNum);
				data.put("balance", yue);
				data.put("tfee", realPay);
			}
		});
		spider.addRequest(req);	
	}	
	
	
	private void requestMonthBillService(SimpleObject context, final String dstr) {
		//Document doc = ContextUtil.getDocumentOfContent(context);
		Request req = new Request("http://www.ln.10086.cn/busicenter/fee/monthbill/MonthBillMenuAction/initBusi.menu?_menuId=1050344&billMonth=" + dstr + "&flag=999");
		//req.setMethod("POST");
		req.putHeader("Referer", "http://www.ln.10086.cn/my/account/billquery.xhtml");		
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements es = doc.select("span.feesalllist2");
				if (es.size() < 1) {
					return;
				}
				BigDecimal b1= new BigDecimal(es.get(es.size() - 1).text());

				MobileTel tel = new MobileTel();
				tel.setcTime(DateUtils.StringToDate(dstr, "yyyyMM"));
				tel.setcAllPay(b1);
				data.put("Month" + dstr, dstr);
				data.put("Pay" + dstr, es.text());
				tel.setTeleno(phoneNo);
				getTelList().add(tel);

			}
		});
		spider.addRequest(req);
	}
	
	/**
	* <p>Title: requestMonthBillService</p>
	* <p>Description: 当月实时</p>
	* @author Jerry Sun
	*/
	private void requestMonthBillService(){
		String[][] pairs = {{"divId", "main"}};
		postUrl("http://www.ln.10086.cn/busicenter/fee/realtimefee/RealTimeFeeMenuAction/initBusi.menu?_menuId=1050309&_menuId=1050309", "http://www.ln.10086.cn/my/account/realtimefee.xhtml", pairs, new AbstractProcessorObserver(util, WaringConstaint.LNYD_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					MobileTel tel = new MobileTel();
					Document doc = ContextUtil.getDocumentOfContent(context);
					
					Elements trs = doc.getElementsByTag("tr");
					if(trs.size()>0){
						for (Element tr : trs) {
							if(tr.text().trim().contains("套餐及固定费")){
								Elements ths = tr.getElementsByTag("th");
								if(ths.size()>0){
									BigDecimal tcgdf = new BigDecimal(0);
									try {
										tcgdf= new BigDecimal(ths.get(1).text().trim().replace("￥", ""));
									} catch (Exception e) {
										logger.error("error", e);
									}
									tel.setTcgdf(tcgdf);
								}
							}
						}
					}
					tel.setcTime(DateUtils.StringToDate(DateUtils.firstDayOfMonth(DateUtils.getToday("yyyy-MM")), "yyyy-MM-dd"));
					tel.setcName(user.getRealName());
					tel.setTeleno(phoneNo);
					BigDecimal yue = new BigDecimal(0);
					BigDecimal allPays = new BigDecimal(0);
					String dependCycle="";
					try {
						yue = new BigDecimal(doc.select("h4").get(0).text().replace("话费余额：", "").replaceAll("元"	, ""));
						dependCycle = doc.select("h3").get(0).text().replace("[打印][打印设置]", "").replace("计费周期：", "");
						String allPay = doc.select("div.realtime3").get(0).select("p").get(0).text().replaceAll("实时话费：", "").replaceAll("元"	, "");
						allPays= new BigDecimal(allPay);
					} catch (Exception e) {
						logger.error("error", e);
					}
					tel.setDependCycle(dependCycle);
					tel.setcAllPay(allPays);
					tel.setcAllBalance(yue);
					
					getTelList().add(tel);
				}
			}
		});
	}
	
	private void requestCallLogService(final String dstr) {		
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.LNYD_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, dstr);
			}
		}, "http://www.ln.10086.cn/busicenter/detailquery/DetailQueryMenuAction/queryResult.menu?_menuId=40399&select=" + dstr + "&beginDate=&endDate=&detailType=202&_=" + System.currentTimeMillis());
	
		//TODO created by qian 
		String dstrChangType = dstr.substring(0, 4)+"-"+dstr.substring(4, 6);
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.LNYD_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveMessage(context, dstr);
			}
		}, "http://www.ln.10086.cn/busicenter/detailquery/DetailQueryMenuAction/queryResult.menu?_menuId=40399&select=" + dstr + "&beginDate="+DateUtils.firstDayOfMonth(dstrChangType).replaceAll("-", "")+"&endDate="+DateUtils.lastDayOfMonth(dstr, "yyyyMM", "yyyyMMdd")+"&detailType=204&_=" + System.currentTimeMillis());
			//http://www.ln.10086.cn/busicenter/detailquery/DetailQueryMenuAction/queryResult.menu?_menuId=40399&select=201409&beginDate=20140901&endDate=20140930&detailType=204&_=1413374848765
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.LNYD_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				getFlow(context, dstr);
			}
		}, "http://www.ln.10086.cn/busicenter/detailquery/DetailQueryMenuAction/queryResult.menu?_menuId=40399&select=" + dstr + "&beginDate="+DateUtils.firstDayOfMonth(dstrChangType).replaceAll("-", "")+"&endDate="+DateUtils.lastDayOfMonth(dstr, "yyyyMM", "yyyyMMdd")+"&detailType=203&_=" + System.currentTimeMillis());
		spider.getSite().setTimeOut(1000*20);//20秒
	}
	
//TODO created by qian
	private void saveMessage(SimpleObject context, final String dstr) {
		Document doc = ContextUtil.getDocumentOfContent(context); 
		
		Elements es = doc.select("table:eq(2)");
		if (es.size() == 0) {
			data.put("messageLog", ContextUtil.getContent(context));
			return;
		}
		Elements es1 = es.select("tr");
		String dstr1 = null;
		for(int i=1; i < es1.size(); i++) {
			try {
				Element e = es1.get(i);
				Elements es2 = e.select("th,td");
				if (es2.size() == 1) {
					dstr1 = es2.text();
				} else {
					es2 = e.select("td");
					if (es2.size() != 8) {
						throw new IllegalArgumentException();
					}
					MobileMessage mbMessage = new MobileMessage();
					UUID uuidId = UUID.randomUUID();
					String beginTime = es2.get(0).text();
					String recevierNumber = es2.get(2).text();
					String tradeAddr = es2.get(1).text();
					String tradeWay = es2.get(3).text();
					String tradeType = es2.get(4).text();
					String allPayStr = es2.get(7).text();
					BigDecimal allPay = new BigDecimal(allPayStr);
					
					mbMessage.setPhone(phoneNo);
					mbMessage.setCreateTs(new Date());
					mbMessage.setId(uuidId.toString());
					mbMessage.setRecevierPhone(recevierNumber);
					mbMessage.setSentAddr(tradeAddr);
					mbMessage.setSentTime(DateUtils.StringToDate(dstr1 + " " + beginTime, "yyyy-MM-dd HH:mm:ss"));
					mbMessage.setTradeWay(tradeType);
					mbMessage.setTradeWay(tradeWay);
					mbMessage.setAllPay(allPay);
					mobileList.add(mbMessage);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void saveCallLog(SimpleObject context, final String dstr) {
		Document doc = ContextUtil.getDocumentOfContent(context); 
		Elements es = doc.select("table:eq(2)");
		if (es.size() == 0) {
			data.put("callLog", ContextUtil.getContent(context));
			return;
		}
		Elements es1 = es.select("tr");
		String dstr1 = null;
		for(int i=1; i < es1.size(); i++) {
			try {
				Element e = es1.get(i);
				Elements es2 = e.select("th,td");
				if (es2.size() == 1) {
					dstr1 = es2.text();
				} else {
					es2 = e.select("td");
					if (es2.size() != 10) {
						throw new IllegalArgumentException();
					}
					MobileDetail dxDetail = new MobileDetail();
					dxDetail.setPhone(phoneNo);
					for(int j=0; j < es2.size(); j++) {
						String text = es2.get(j).text();
						if (j == 7) {
							dxDetail.setTradeType(text);
						} else if (j == 4) {
							dxDetail.setRecevierPhone(text);
						} else if (j == 1) {
							dxDetail.setTradeAddr(text);					
						} else if (j == 0) {
							dxDetail.setcTime(DateUtils.StringToDate(dstr1 + " " + text, "yyyy-MM-dd HH:mm:ss"));
						} else if (j == 6) {
							dxDetail.setTradeTime(new TimeUtil().timetoint(text));
						} else if (j == 9) {
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
	
	/**
	* <p>Title: getFlow</p>
	* <p>Description: 流量抓取</p>
	* @author Jerry Sun
	* @param dstr
	*/
	private void getFlow(SimpleObject context,final String dstr){

		if(ContextUtil.getContent(context).contains("drNetSummaryId")){
			Document doc = ContextUtil.getDocumentOfContent(context);
			/**
			 * 流量详单
			 */
			Elements drNetDetailedIds = doc.select("#drNetDetailedId");
			if(drNetDetailedIds.size()>0){
				Element drNetDetailedId = drNetDetailedIds.get(0);
				Element tbody = drNetDetailedId.getElementsByTag("tbody").get(0);
				Elements trs = tbody.getElementsByTag("tr");
				String beginTime = "";
				for (Element tr : trs) {
					if(tr.text().contains("上网方式") || "display:none".equals(tr.attr("style"))){
						continue;
					}else{
						MobileOnlineList mol = new MobileOnlineList();
						Elements tds = tr.getElementsByTag("td");
						String netType = "";
						if(tds.size()<1){
							beginTime = tr.text().trim();
						}else{
							for(int i=0;i<tds.size();i++){
								String temp = tds.get(i).text().trim();
								switch (i) {
								case 0:
									beginTime += " "+temp;
									break;
								case 1:
									String location = temp;
									mol.setTradeAddr(location);
									break;
								case 2:
									netType = temp;
									break;
								case 3:
									netType += temp;
									break;
								case 4:
									String time = temp;
									mol.setOnlineTime(StringUtil.flowTimeFormat(time));
									break;
								case 5:
									String flow = temp;
									mol.setTotalFlow(StringUtil.flowFormat(flow).longValue());
									break;
								case 6:
									String tcyh = temp;
									mol.setCheapService(tcyh);
									break;
								case 7:
									String fee = temp;
									mol.setCommunicationFees(new BigDecimal(fee));
									break;
								}
							}
							
							mol.setPhone(phoneNo);
							mol.setcTime(DateUtils.StringToDate(beginTime, "yyyy-MM-dd hh:mm:ss"));
							mol.setOnlineType(netType);

							flowDetailList.add(mol);
						}
					}
				}
			}
			
			/*
			 * 流量账单
			 */
			String content = ContextUtil.getContent(context);
			Document doc1 = Jsoup.parse("<html>"+content+"</html>");
			Elements trs = doc1.select("table[class=w950_detailtable2]");
			String allPay = trs.get(0).select("td").get(1).text().replaceAll("元", "");
			long allFlows = 0;
			BigDecimal allPays = new BigDecimal(0); 
			try {
				allFlows = StringUtil.flowFormat(StringUtil.subStr("总流量", "</", content).trim()).longValue();
				allPays = new BigDecimal(allPay);
			} catch (Exception e) {
				logger.error("error",e);
			}
			MobileOnlineBill mob = new MobileOnlineBill();
			mob.setPhone(phoneNo);
			mob.setMonthly(DateUtils.StringToDate(dstr, "yyyyMM"));
			//String allFlow = 
			mob.setTotalFlow(allFlows);
			mob.setTrafficCharges(allPays);
			
			flowList.add(mob);
		}
	
	}
		
	public static void main(String[] args) throws Exception {
		String phoneNo = "15042433089";
		String password = "595499";
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		LiaoNingYiDong dx = new LiaoNingYiDong(spider, null, phoneNo, password, null, null);
//		dx.initForTest();
		dx.goLoginReq();
		spider.start();
		//dx.sendSmsPasswordForRequireCallLogService(1);
		dx.parseBalanceInfo(null);
		spider.start();
		dx.printData();
		if (true) {
			return;
		}

	}
}
