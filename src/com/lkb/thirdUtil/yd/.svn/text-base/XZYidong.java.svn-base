package com.lkb.thirdUtil.yd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
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
import com.lkb.bean.SimpleObject;
import com.lkb.bean.User;

import com.lkb.robot.Request;
import com.lkb.robot.Spider;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.thirdUtil.StatusTracker;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;





public class XZYidong extends AbstractYiDongCrawler {
	public static String CUST_NAME = "";

	public XZYidong() {
		super();
	}

	public XZYidong(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;
		this.util = util;
		spider.getSite().setCharset("utf-8");
	}

	public XZYidong(Spider spider, User user, String phoneNo, String password,
			String authCode, WarningUtil util) {
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
	}

	public void vertifyLogin() {

		Request req = new Request("https://xz.ac.10086.cn/login");
		req.setMethod("POST");
		req.setCharset("GBK");
		req.initNameValuePairs(13);
		req.addNameValuePairs("ENCRYPT_FLAG", "0");
		req.addNameValuePairs("VERIFY_CODE_FLAG", "0");
		req.addNameValuePairs("ai_param_loginIndex", "0");
		req.addNameValuePairs("ai_param_loginTypes", "2,1,3");
		req.addNameValuePairs("appId", "1");
		req.addNameValuePairs("filter_rule", "");
		req.addNameValuePairs("loginType", "1");
		req.addNameValuePairs("lt", "null");
		req.addNameValuePairs("password", password(password));
		req.addNameValuePairs("rndPassword", "");
		req.addNameValuePairs("service", "null");
		req.addNameValuePairs("username", phoneNo);
		req.addNameValuePairs("verifyCode", authCode);
		req.addObjservers(new AbstractProcessorObserver(util,
				WaringConstaint.XZYD_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginPage(context);

			}
		});
		spider.addRequest(req);

	}

	protected void parseLoginPage(SimpleObject context) {
		String text = ContextUtil.getContent(context);
		// System.out.println("==============="+text);
		if (text != null && text.contains("/success.jsp")) {
			Document doc = Jsoup.parse(text);
			String ticket = doc.select("input[name=ticket]").val();
			String host = doc.select("input[name=host]").val();
			Request req = new Request("https://xz.ac.10086.cn/success.jsp");
			req.setMethod("POST");
			req.initNameValuePairs(2);
			req.addNameValuePairs("ticket", ticket);
			req.addNameValuePairs("host", host);
			req.addObjservers(new AbstractProcessorObserver(util,
					WaringConstaint.XZYD_3) {
				@Override
				public void afterRequest(SimpleObject context) {
					parseLoginPage1(context);
				}
			});
			spider.addRequest(req);
		} else {
			parseLoginErrorPage(context);
		}
	}

	protected void parseLoginPage1(SimpleObject context) {
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		if (scode == 302) {
			HttpResponse resp = ContextUtil.getResponse(context);
			Header h1 = resp.getFirstHeader("Location");
			String nexturl = h1.getValue();
			if (nexturl != null && nexturl.equals("http://www.xz.10086.cn/my/")) {
				Request req = new Request("http://www.xz.10086.cn/my/");
				req.setMethod("GET");
				req.addObjservers(new AbstractProcessorObserver(util,
						WaringConstaint.XZYD_3) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLoginPage2(context);
					}
				});
				spider.addRequest(req);
			}
		} else {
			parseLoginErrorPage(context);
		}
	}

	protected void parseLoginPage2(SimpleObject context) {
		Request req = new Request(
				"https://xz.ac.10086.cn/login?service=http%3A%2F%2Fxz.10086.cn%2Fservice%2Fdispatcher%2FMyMoveLogin&ai_param_loginIndex=4&appId=6");
		req.setMethod("GET");
		req.addObjservers(new AbstractProcessorObserver(util,
				WaringConstaint.XZYD_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginPage3(context);
			}
		});
		spider.addRequest(req);
	}

	protected void parseLoginPage3(SimpleObject context) {
		String text = ContextUtil.getContent(context);
		if (text != null
				&& text.contains("http://xz.10086.cn/service/dispatcher/MyMoveLogin")) {
			Request req = new Request(
					"http://xz.10086.cn/service/dispatcher/MyMoveLogin");
			req.setMethod("POST");
			Document doc = Jsoup.parse(text);
			String ticket = doc.select("input[name=ticket]").val();
			String host = doc.select("input[name=host]").val();
			req.initNameValuePairs(2);
			req.addNameValuePairs("ticket", ticket);
			req.addNameValuePairs("host", host);
			req.addObjservers(new AbstractProcessorObserver(util,
					WaringConstaint.XZYD_3) {
				@Override
				public void afterRequest(SimpleObject context) {
					String text = ContextUtil.getContent(context);
					if (text.contains(phoneNo)) {
						setStatus(StatusTracker.STAT_LOGIN_SUC);
						initToken();
					}
				}
			});
			spider.addRequest(req);
		}
	}

	public void initToken() {
		try {
			Request req = new Request(
					"http://xz.10086.cn/service/points/jfcx.jsp");
			req.setMethod("GET");
			req.addObjservers(new AbstractProcessorObserver(util,
					WaringConstaint.XZYD_5) {
				@Override
				public void afterRequest(SimpleObject context) {
					String text = ContextUtil.getContent(context);
					if (text != null) {
						try {
							RegexPaserUtil rp = new RegexPaserUtil(
									"TOKEN : \"", "\"", text,
									RegexPaserUtil.TEXTEGEXANDNRT);
							String tk = rp.getText();
							entity.put("token", tk);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							notifyStatus();
							sendMsg();
						}

					}

				}
			});
			spider.addRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void parseLoginErrorPage(SimpleObject context) {
		String text = ContextUtil.getContent(context);
		try {
			if (text != null) {
				RegexPaserUtil rp = new RegexPaserUtil(
						"<META name=\"WT.failType\" content=\"", "\">", text,
						RegexPaserUtil.TEXTEGEXANDNRT);
				String eInfo = rp.getText();
				if (eInfo != null) {
					data.put("msg", eInfo);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			notifyStatus();
		}
	}

	public static String password(String pass) {
		ScriptEngineManager manager = new ScriptEngineManager();

		ScriptEngine engine = manager.getEngineByExtension("js");
		String rsaPath = InfoUtil.getInstance().getInfo("road",
				"tomcatWebappPath")
				+ "/js/yd/xzyd_pwd.js";

		File f = new File(rsaPath);
		FileInputStream fip = null;
		try {
			fip = new FileInputStream(f);

			// 构建FileInputStream对象
			InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
			// 执行指定脚本
			engine.eval(reader);
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				// 调用merge方法，并传入两个参数
				// c = merge(2, 3);
				Object c = (Object) invoke.invokeFunction("enString", pass);
				// Object c = (Object)invoke.invokeFunction("sss",3);
				System.out.println("c = " + c.toString());// 525AF936132AEDA4A4B9A3C29788B968
				reader.close();
				return c.toString();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public void loginMsg1(String token) {

		Request req = new Request(
				"http://xz.10086.cn/service/app?service=ajaxService/1/AjaxSubmit/AjaxSubmit/javascript/&pagename=AjaxSubmit&eventname=checkSMSValidation&&ID=4047&csrfToken="
						+ token
						+ "&partids=&ajaxSubmitType=post&ajax_randomcode=0.7710339324482651&autoType=false");
		req.setMethod("POST");
		req.initNameValuePairs(1);
		req.addNameValuePairs("passwrod_smsValidation", password);
		req.addObjservers(new AbstractProcessorObserver(util,
				WaringConstaint.XZYD_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				if (text != null && text.contains("\"SECOND_CHECK_TAG\":\"3\"")) {
					//data.put("msg", "1");
					notifyStatus();
					onCompleteLogin(context);
				} else {
					if (text != null && text.contains("您输入的短信验证码有误")) {
						data.put("msg", "您输入的短信验证码有误");
					} else if (text != null && text.contains("请点击获取获取验证码再重新操作")) {
						data.put("msg", "请点击获取获取验证码再重新操作");

					} else {
						data.put("msg", "您输入的短信验证码有误");
					}
					notifyStatus();
				}

			}
		});
		spider.addRequest(req);

	}

	// 随机短信登录
	public void loginMsg() {
		try {
			Request req = new Request("http://xz.10086.cn/service/");
			req.setMethod("GET");
			req.addObjservers(new AbstractProcessorObserver(util,
					WaringConstaint.XZYD_5) {
				@Override
				public void afterRequest(SimpleObject context) {
					String token = entity.getString("token");
					loginMsg1(token);
				}
			});
			spider.addRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onCompleteLogin(SimpleObject context) {
		setStatus(STAT_LOGIN_SUC);
		callHistory();
		getSmsLog();
		getTelDetailHtml();
		getYue();
		getFlow(entity.getString("token"));
		// notifyStatus();
	}

	public void sendMsg1(String token) {
		Request req = new Request(
				"http://xz.10086.cn/service/app?service=ajaxService/1/fee.QueryMonthBill/fee.QueryMonthBill/javascript/&pagename=fee.QueryMonthBill&eventname=sendSMSValidation&&ID=4047&csrfToken="
						+ token
						+ "&partids=&ajaxSubmitType=post&ajax_randomcode=0.9133086927258078&autoType=false");
		req.setMethod("POST");
		req.initNameValuePairs(1);
		req.addNameValuePairs("passwrod_smsValidation", "");
		req.addObjservers(new AbstractProcessorObserver(util,
				WaringConstaint.XZYD_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					String text = ContextUtil.getContent(context);
					if (text != null) {
						if (text.contains("短信验证码已通过短信方式发送到您的手机")) {
							data.put("msg", "1");
						} else if (text.contains("在1分钟内不能重新发送短信")) {
							data.put("msg", "在1分钟内不能重新发送短信");
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					notifyStatus();
				}
			}
		});
		spider.addRequest(req);
	}

	public void sendMsg() {
		Request req = new Request("http://xz.10086.cn/service/");
		req.setMethod("GET");
		req.addObjservers(new AbstractProcessorObserver(util,
				WaringConstaint.XZYD_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				String token = entity.getString("token");
				sendMsg1(token);
			}
		});
		spider.addRequest(req);

	}

	/*
	 * 获取短信详单
	 */
	public void getSmsLog() {
		try {
			Request req = new Request(
					"http://xz.10086.cn/service/fee/xd9yf.jsp");
			req.setMethod("GET");
			req.addObjservers(new AbstractProcessorObserver(util,
					WaringConstaint.XZYD_5) {
				@Override
				public void afterRequest(SimpleObject context) {
					String text = ContextUtil.getContent(context);
					if (text != null) {
						try {
							RegexPaserUtil rp = new RegexPaserUtil(
									"TOKEN : \"", "\"", text,
									RegexPaserUtil.TEXTEGEXANDNRT);
							String tk = rp.getText();
							try {
								List<String> ms = DateUtils.getMonths(6,
										"yyyy-MM");
								for (int k = 0; k < ms.size(); k++) {
									final String startDate = (String) ms.get(k);
									Request req = new Request(
											"http://xz.10086.cn/service/app?service=ajaxService/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
													+ tk
													+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode=0.08528087751877167&autoType=false");

									req.setMethod("POST");
									req.initNameValuePairs(7);
									req.addNameValuePairs("detailpwdQueryForm",
											"");
									req.addNameValuePairs(
											"from",
											DateUtils.formatDate(new Date(),
													"yyyy-MM") + "01");
									req.addNameValuePairs("isPage", "0");
									req.addNameValuePairs("monthQuery",
											startDate);
									req.addNameValuePairs("queryMode",
											"queryMonth");
									req.addNameValuePairs("queryType", "3");
									req.addNameValuePairs("to", DateUtils
											.formatDate(new Date(),
													"yyyy-MM-dd"));
									req.addObjservers(new AbstractProcessorObserver(
											util, WaringConstaint.XZYD_2) {
										@Override
										public void afterRequest(
												SimpleObject context) {
											try {
												String text = ContextUtil
														.getContent(context);

												if (text != null
														&& text.contains("dataList")) {
													text = text.substring(1,
															text.length() - 1);
													JSONObject json = new JSONObject(
															text);
													JSONArray jsonDetail = json
															.getJSONArray("dataList");
													if (jsonDetail != null) {
														for (int i = 0; i < jsonDetail
																.length(); i++) {
															JSONObject jsonobject = jsonDetail
																	.getJSONObject(i);
															if (jsonobject
																	.length() == 9) {
																String sentTime = jsonobject
																		.getString("STARTTIME");
																String tradeWay = jsonobject
																		.getString("CALLTYPE");
																String recevierPhone = jsonobject
																		.getString("OPPNUMBER");
																String sentAddr = jsonobject
																		.getString("HPLMN");
																String allPay = jsonobject
																		.getString(
																				"CHARGE")
																		.replace(
																				"￥",
																				"")
																		.trim();
																String phone = phoneNo;// 本机号码

																MobileMessage message = new MobileMessage();
																message.setAllPay(new BigDecimal(
																		allPay));
																message.setRecevierPhone(recevierPhone);
																message.setSentAddr(sentAddr);
																Date times = null;
																try {
																	times = DateUtils
																			.StringToDate(
																					sentTime,
																					"yyyy-MM-dd HH:mm:ss");
																	// if(lastTime!=null&&times!=null){
																	// if(times.getTime()<=lastTime.getTime()){
																	// continue;
																	// }
																	// }
																} catch (Exception e) {
																	e.printStackTrace();
																}
																message.setSentTime(times);
																message.setTradeWay(tradeWay);
																message.setPhone(phone);
																message.setCreateTs(new Date());
																UUID uuid = UUID
																		.randomUUID();
																message.setId(uuid
																		.toString());

																getMessageList()
																		.add(message);
															}
														}
													}

												}

											} catch (Exception e) {
												// TODO: handle exception
												e.printStackTrace();
											}

										}
									});
									spider.addRequest(req);
								}
							} catch (Exception e) {
								e.printStackTrace();

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			});
			spider.addRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通话详单
	 * */
	public void callHistory() {
		try {
			Request req = new Request(
					"http://xz.10086.cn/service/fee/xd9yf.jsp");
			req.setMethod("GET");
			req.addObjservers(new AbstractProcessorObserver(util,
					WaringConstaint.XZYD_5) {
				@Override
				public void afterRequest(SimpleObject context) {
					String text = ContextUtil.getContent(context);
					if (text != null) {
						try {
							RegexPaserUtil rp = new RegexPaserUtil(
									"TOKEN : \"", "\"", text,
									RegexPaserUtil.TEXTEGEXANDNRT);
							String tk = rp.getText();
							callHistoryHtml(tk);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			});
			spider.addRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 详单
	 */
	public void callHistoryHtml(String tk) {
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyy-MM");
			for (int k = 0; k < ms.size(); k++) {
				final String startDate = (String) ms.get(k);
				Request req = new Request(
						"http://xz.10086.cn/service/app?service=ajaxService/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
								+ tk
								+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode=0.9770824573547267&autoType=false");

				req.setMethod("POST");
				req.initNameValuePairs(10);
				req.addNameValuePairs("detailpwdQueryForm", "");
				req.addNameValuePairs("from",
						DateUtils.formatDate(new Date(), "yyyy-MM") + "01");
				req.addNameValuePairs("isPage", "0");
				req.addNameValuePairs("monthQuery", startDate);
				req.addNameValuePairs("pagination_iPage", "");
				req.addNameValuePairs("pagination_inputPage", "");
				req.addNameValuePairs("pagination_linkType", "");
				req.addNameValuePairs("queryMode", "queryMonth");
				req.addNameValuePairs("queryType", "1");
				req.addNameValuePairs("to",
						DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
				req.addObjservers(new AbstractProcessorObserver(util,
						WaringConstaint.XZYD_2) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							String text = ContextUtil.getContent(context);
							if (text != null && text.contains("dataList")) {
								text = text.substring(1, text.length() - 1);
								JSONObject json = new JSONObject(text);
								JSONArray jsonDetail = json
										.getJSONArray("dataList");
								if (jsonDetail != null) {
									for (int i = 0; i < jsonDetail.length(); i++) {
										JSONObject jsonobject = jsonDetail
												.getJSONObject(i);
										if (jsonobject.length() > 16) {
											String thsj = jsonobject
													.getString("STARTTIME");
											String thdd = jsonobject
													.getString("HPLMN");
											String txfs = jsonobject
													.getString("CALLTYPE");
											String dfhm = jsonobject
													.getString("OPPNUMBER");
											String thsc = jsonobject
													.getString("DURATION");
											String txlx = jsonobject
													.getString("TOLLTYPE");
											String fy = jsonobject
													.getString("TOTAL_CHARGE");

											MobileDetail mDetail = new MobileDetail();
											mDetail.setcTime(DateUtils
													.StringToDate(thsj,
															"yyyy-MM-dd HH:mm:ss"));
											int times = 0;
											try {
												TimeUtil tunit = new TimeUtil();
												times = tunit.timetoint(thsc);
											} catch (Exception e) {

											}
											if (txfs.contains("主叫")) {
												txfs = "主叫";
											} else {
												txfs = "被叫";
											}
											if (txlx.contains("漫游")) {
												txlx = "漫游";
											} else {
												txlx = "本地";
											}
											mDetail.setTradeAddr(thdd);
											mDetail.setTradeWay(txfs);
											mDetail.setRecevierPhone(dfhm);
											mDetail.setTradeTime(times);
											mDetail.setTradeType(txlx);
											mDetail.setOnlinePay(new BigDecimal(
													fy));
											mDetail.setPhone(phoneNo);
											getDetailList().add(mDetail);
										}
									}
								}

							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				});
				spider.addRequest(req);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void getTelDetailHtml() {
		try {
			Request req = new Request(
					"http://xz.10086.cn/service/fee/xbyzdcx.jsp");
			req.setMethod("GET");
			req.addObjservers(new AbstractProcessorObserver(util,
					WaringConstaint.XZYD_5) {
				@Override
				public void afterRequest(SimpleObject context) {
					String text = ContextUtil.getContent(context);
					if (text != null) {
						try {
							RegexPaserUtil rp = new RegexPaserUtil(
									"TOKEN : \"", "\"", text,
									RegexPaserUtil.TEXTEGEXANDNRT);
							String tk = rp.getText();
							getTelDetailHtml(tk);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			});
			spider.addRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 账单
	 */
	public void getTelDetailHtml(String tk) {
		try {
			
			List<String> ms = DateUtils.getMonths(7, "yyyyMM");
			for (int k = 0; k < ms.size(); k++) {
				final String startDate = (String) ms.get(k);
				if(k == 0){
					//TODO  当月的的无法显示 话费
					getUrl("http://xz.10086.cn/service/fee/sshf.jsp", "http://xz.10086.cn/service/index.jsp", null);
					String token =	entity.getString("token");
					Request req = new Request("http://xz.10086.cn/service/app");
					req.setMethod("POST");
					req.putHeader("Referer", "http://xz.10086.cn/service/fee/sshf.jsp");
					req.initNameValuePairs(10);
					
					req.addNameValuePairs("$FormConditional", "F");
					req.addNameValuePairs("$Submit", "查询");
					req.addNameValuePairs("Form0", "$Submit,$FormConditional");
					req.addNameValuePairs("ID", "4040");
					req.addNameValuePairs("csrfToken", token);
					req.addNameValuePairs("flush_mode", "1");
					req.addNameValuePairs("flush_mode_param", "");
					req.addNameValuePairs("queryType", "0");
					req.addNameValuePairs("service", "direct/1/fee.QueryFee/$IcsForm");
					req.addNameValuePairs("sp", "S0");

					req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.XZYD_2) {
						@Override
						public void afterRequest(SimpleObject context) {
							String text1 = ContextUtil.getContent(context);
							if(text1 == null || text1.equals("")){
								return;
							}
							String pay = "";
							Document doc = ContextUtil.getDocumentOfContent(context);
							System.out.println(doc.toString());
							Elements tables = doc.select("table[class=table1]");
							if(tables !=null && tables.size() > 0){
								Element table = tables.get(0);
								Elements trs = table.select("tr");
								if(trs.size() > 1){
									Elements tds = trs.get(1).select("td");
									pay = tds.get(2).text().trim();
								}
								
							}
							if(pay.equals("")){
								pay = "0";
							}
							
							MobileTel mobieTel = new MobileTel();
							mobieTel.setcTime(DateUtils.StringToDate(
									startDate, "yyyyMM"));
							mobieTel.setTeleno(phoneNo);
							String year = startDate.substring(0, 4);
							String mouth = startDate.substring(4, 6);
							mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
									Integer.parseInt(year),
									Integer.parseInt(DateUtils
											.formatDateMouth(mouth)))
									+ "至"
									+ DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
							mobieTel.setcAllPay(new BigDecimal(pay));
							getTelList().add(mobieTel);
						}
					});
					spider.addRequest(req);
					
				} else {
				
				
				Request req = new Request(
						"http://xz.10086.cn/service/app?service=page/fee.QueryMonthBillNewInfo&listener=initPage&csrfToken="
								+ tk
								+ "&select_month="
								+ startDate
								+ "&ID=4047&csrfToken="
								+ tk
								+ "&flush_mode=1&flush_mode_param=");

				req.setMethod("GET");

				req.addObjservers(new AbstractProcessorObserver(util,
						WaringConstaint.XZYD_2) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							String text = ContextUtil.getContent(context);
							text = text.replaceAll("\\s*", "");
							text = StringEscapeUtils.unescapeHtml3(text);
							if (text != null && text.contains("费用信息")
									&& text.contains("费用项目")) {
								BigDecimal tcgdf = new BigDecimal(0);
								BigDecimal yytxf = new BigDecimal(0);
								BigDecimal zzywf = new BigDecimal(0);
								BigDecimal tcwfy = new BigDecimal(0);
								BigDecimal swf = new BigDecimal(0);
								BigDecimal dx = new BigDecimal(0);
								BigDecimal hj = new BigDecimal(0);

								if (text != null) {
									RegexPaserUtil rp = new RegexPaserUtil(
											"套餐及固定费用</strong></td><tdalign=\"center\">",
											"</td>", text,
											RegexPaserUtil.TEXTEGEXANDNRT);
									String tcgdfs = rp.getText();
									if (tcgdfs != null) {
										tcgdf = new BigDecimal(tcgdfs);
									}
									rp = new RegexPaserUtil(
											"语音通信费</span></td><tdalign=\"center\">",
											"</td>", text,
											RegexPaserUtil.TEXTEGEXANDNRT);
									String tcwyytxf = rp.getText();
									if (tcwyytxf != null) {
										yytxf = new BigDecimal(tcwyytxf);
									}
									rp = new RegexPaserUtil(
											"上网费</span></td><tdalign=\"center\">",
											"</td>", text,
											RegexPaserUtil.TEXTEGEXANDNRT);
									String tcwswfs = rp.getText();
									if (tcwswfs != null) {
										swf = new BigDecimal(tcwswfs);
									}
									rp = new RegexPaserUtil(
											"短彩信费</span></td><tdalign=\"center\">",
											"</td>", text,
											RegexPaserUtil.TEXTEGEXANDNRT);
									String tcwdxf = rp.getText();
									if (tcwdxf != null) {
										dx = new BigDecimal(tcwdxf);
									}
									rp = new RegexPaserUtil(
											"增值业务费用</strong></td><tdalign=\"center\">",
											"</td>", text,
											RegexPaserUtil.TEXTEGEXANDNRT);
									String zzywfs = rp.getText();
									if (tcgdfs != null) {
										zzywf = new BigDecimal(zzywfs);
									}
									rp = new RegexPaserUtil(
											"合计</strong></td><tdalign=\"center\">",
											"</td>", text,
											RegexPaserUtil.TEXTEGEXANDNRT);
									String hjs = rp.getText();
									if (hjs != null) {
										hj = new BigDecimal(hjs);
									}
									MobileTel mobieTel = new MobileTel();

									mobieTel.setcTime(DateUtils.StringToDate(
											startDate, "yyyyMM"));
									mobieTel.setcName("");
									mobieTel.setTeleno(phoneNo);
									mobieTel.setZzywf(zzywf);
									mobieTel.setYdsjllqb(swf);
									mobieTel.setGndxtx(dx);
									mobieTel.setTcgdf(tcgdf);
									mobieTel.setTcwyytxf(tcwfy);
									String year = startDate.substring(0, 4);
									String mouth = startDate.substring(4, 6);
									mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
											Integer.parseInt(year),
											Integer.parseInt(DateUtils
													.formatDateMouth(mouth)))
											+ "至"
											+ TimeUtil.getLastDayOfMonth(
													Integer.parseInt(year),
													Integer.parseInt(DateUtils
															.formatDateMouth(mouth))));
									mobieTel.setcAllPay(hj);
									mobieTel.setTcwyytxf(yytxf);
									getTelList().add(mobieTel);

								}

							} else {
								MobileTel mobieTel = new MobileTel();

								mobieTel.setcTime(DateUtils.StringToDate(
										startDate, "yyyyMM"));
								mobieTel.setcName("");
								mobieTel.setTeleno(phoneNo);
								String year = startDate.substring(0, 4);
								String mouth = startDate.substring(4, 6);
								mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
										Integer.parseInt(year),
										Integer.parseInt(DateUtils
												.formatDateMouth(mouth)))
										+ "至"
										+ TimeUtil.getLastDayOfMonth(
												Integer.parseInt(year),
												Integer.parseInt(DateUtils
														.formatDateMouth(mouth))));
								mobieTel.setcAllPay(new BigDecimal(0));
								getTelList().add(mobieTel);
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				});
				spider.addRequest(req);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void getYue1(String token) {
		Request req = new Request(
				"http://xz.10086.cn/service/app?service=page/fee.QueryBalanceFee&listener=initPage&csrfToken="
						+ token + "&ID=4041");
		req.setMethod("GET");
		req.addObjservers(new AbstractProcessorObserver(util,
				WaringConstaint.XZYD_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				BigDecimal phoneremain = new BigDecimal("0.00");
				if (text != null) {
					if (text.contains("通用话费余额")) {
						text = text.replaceAll("\\s*", "");
						RegexPaserUtil rp = new RegexPaserUtil(
								"<th>通用话费余额：</th><td>", "</td>", text,
								RegexPaserUtil.TEXTEGEXANDNRT);
						String yue = rp.getText().replaceAll("\\s*", "")
								.replaceAll("￥", "");
						if (yue != null) {
							phoneremain = new BigDecimal(yue);
						}
					}
				}
				addPhoneRemain(phoneremain);
			}
		});
		spider.addRequest(req);
	}

	public void getYue() {

		Request req = new Request("http://xz.10086.cn/service/");
		req.setMethod("GET");
		req.addObjservers(new AbstractProcessorObserver(util,
				WaringConstaint.XZYD_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				String token = entity.getString("token");
				getYue1(token);
			}
		});
		spider.addRequest(req);
	}

	public void checkVerifyCode(final String userName) {
		// 1.生成一个request
		String url = "https://xz.ac.10086.cn/login";
		Request req = new Request(url);

		req.addObjservers(new AbstractProcessorObserver(util,
				WaringConstaint.XZYD_3) {

			// 2.请求完成后的解析
			@Override
			public void afterRequest(SimpleObject context) {
				data.put("checkVerifyCode", "1");
				String picName = "xz_yd_code_" + userName + "_"
						+ (int) (Math.random() * 1000) + "ddd";
				try {
					String imgName = saveFile(
							"https://xz.ac.10086.cn/createVerifyImageServlet?datetime="
									+ new Date().getTime(), "xz.ac.10086.cn",
							null, picName, true);
					data.put("imgName", imgName);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		);

		// 3.加入到Spider的执行队列
		spider.addRequest(req);
	}

	/**
	 * <p>
	 * Title: getFlow
	 * </p>
	 * <p>
	 * Description: 抓取流量
	 * </p>
	 * 
	 * @author Jerry Sun
	 * @param token
	 */
	private void getFlow(final String token) {
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyy-MM");
			for (int k = 0; k < ms.size(); k++) {
				final String startDate = (String) ms.get(k);
				Request req = new Request(
						"http://xz.10086.cn/service/app?service=ajaxService/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
								+ token
								+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode="
								+ Math.random() + "&autoType=false");

				req.setMethod("POST");
				spider.getSite().setTimeOut(1000 * 30);
				req.initNameValuePairs(7);
				req.addNameValuePairs("detailpwdQueryForm", "");
				req.addNameValuePairs("isPage", "1");
				req.addNameValuePairs("queryMode", "queryDay");
				req.addNameValuePairs("queryType", "2");
				req.addNameValuePairs("monthQuery", startDate);
				req.addNameValuePairs("from",
						DateUtils.firstDayOfMonth(startDate));
				req.addNameValuePairs("to", DateUtils.lastDayOfMonth(startDate));
				req.addObjservers(new AbstractProcessorObserver(util,
						WaringConstaint.XZYD_2) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							String text = ContextUtil.getContent(context);
							if (text != null) {
								/*
								 * 流量详单
								 */
								JSONArray jsonArray = new JSONArray(text);
								JSONObject json = jsonArray.getJSONObject(0);
								String js = json.toString();
								if(js.contains("dataList") && json.getString("dataList").length() > 0){
								JSONArray dataList=new JSONArray(json.getString("dataList"));
								for (int i = 0; i < dataList.length(); i++) {
									JSONObject jso = dataList.getJSONObject(i);
									if(jso.length() == 8){
										MobileOnlineList mol = new MobileOnlineList();
										String location = jso.getString("HPLMN");
										String netType = jso.getString("DRTYPE");
										String flow = jso.getString("DATAFLOWTOTAL");
										String beginTime = jso.getString("STARTTIME");
										String fee = jso.getString("CHARGE").replace("￥", "");
										String duration = jso.getString("DURATION");
										String tcyh = jso.getString("VALIDRATEPRODID");

										mol.setPhone(phoneNo);
										mol.setcTime(DateUtils.StringToDate(beginTime,"yyyy-MM-dd hh:mm:ss"));
										mol.setTradeAddr(location);
										mol.setOnlineType(netType);
										mol.setOnlineTime(StringUtil
												.flowTimeFormat(duration));
										mol.setTotalFlow(StringUtil.flowFormat(flow).longValue());
										mol.setCheapService(tcyh);
										mol.setCommunicationFees(new BigDecimal(fee));

										flowDetailList.add(mol);
									}
									}

										int currentPage = Integer.parseInt(json.getString("currentPage"));
										int totalPage = Integer.parseInt(json.getString("totalPage"));

										if (currentPage < totalPage) {
											getFlowDetailNextPage(
													token,
													startDate,
													String.valueOf(currentPage + 1));
										}

										/*
										 * 流量账单
										 */
										double allPay = 0, allFlow = 0, allTime = 0;
										for (MobileOnlineList temp : flowDetailList) {
											if (temp != null) {
												allPay += temp
														.getCommunicationFees()
														.doubleValue();
												allFlow += temp.getTotalFlow();
												allTime += temp.getOnlineTime();
											}
										}

										MobileOnlineBill mob = new MobileOnlineBill();
										mob.setPhone(phoneNo);
										mob.setMonthly(DateUtils.StringToDate(
												startDate, "yyyy-MM"));
										mob.setTotalFlow((long) allFlow);
										mob.setTrafficCharges(new BigDecimal(allPay));

										flowList.add(mob);
									} else {
										MobileOnlineBill mob = new MobileOnlineBill();
										mob.setPhone(phoneNo);
										mob.setMonthly(DateUtils.StringToDate(
												startDate, "yyyy-MM"));
										mob.setTotalFlow((long) 0);
										mob.setTrafficCharges(new BigDecimal(0));

										flowList.add(mob);
									}
								}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				spider.addRequest(req);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Title: getFlowDetailNextPage
	 * </p>
	 * <p>
	 * Description: 流量详单下一页
	 * </p>
	 * 
	 * @author Jerry Sun
	 * @param token
	 * @param startDate
	 * @param page
	 */
	private void getFlowDetailNextPage(final String token,
			final String startDate, String page) {
		try {
			Request req = new Request(
					"http://xz.10086.cn/service/app?service=ajaxService/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
							+ token
							+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode="
							+ System.currentTimeMillis() + "&autoType=false");

			req.setMethod("POST");
			req.initNameValuePairs(10);
			req.addNameValuePairs("detailpwdQueryForm", "");
			req.addNameValuePairs("isPage", "1");
			req.addNameValuePairs("queryMode", "queryDay");
			req.addNameValuePairs("queryType", "2");
			req.addNameValuePairs("monthQuery", startDate);
			req.addNameValuePairs("from", DateUtils.firstDayOfMonth(startDate));
			req.addNameValuePairs("to", DateUtils.lastDayOfMonth(startDate));
			req.addNameValuePairs("pagination_inputPage", "");
			req.addNameValuePairs("pagination_linkType", "5");
			req.addNameValuePairs("pagination_iPage", page);
			req.addObjservers(new AbstractProcessorObserver(util,
					WaringConstaint.XZYD_2) {
				@Override
				public void afterRequest(SimpleObject context) {
					try {
						String text = ContextUtil.getContent(context);
						if (text != null) {
							/*
							 * 流量详单
							 */

							JSONArray jsonArray = new JSONArray(text);
							JSONObject json = jsonArray.getJSONObject(0);
							String js = json.toString();
							if(js.contains("dataList") && json.getString("dataList").length() > 0){
								JSONArray dataList = new JSONArray(json.getString("dataList"));
								for (int i = 0; i < dataList.length(); i++) {
									JSONObject jso = dataList.getJSONObject(i);
									if(jso.length() == 8){
									MobileOnlineList mol = new MobileOnlineList();
									String location = jso.getString("HPLMN");
									String netType = jso.getString("DRTYPE");
									String flow = jso
											.getString("DATAFLOWTOTAL");
									String beginTime = jso
											.getString("STARTTIME");
									String fee = jso.getString("CHARGE")
											.replace("￥", "");
									String duration = jso.getString("DURATION");
									String tcyh = jso
											.getString("VALIDRATEPRODID");

									mol.setPhone(phoneNo);
									mol.setcTime(DateUtils.StringToDate(
											beginTime, "yyyy-MM-dd hh:mm:ss"));
									mol.setTradeAddr(location);
									mol.setOnlineType(netType);
									mol.setOnlineTime(StringUtil
											.flowTimeFormat(duration));
									mol.setTotalFlow(StringUtil
											.flowFormat(flow).longValue());
									mol.setCheapService(tcyh);
									mol.setCommunicationFees(new BigDecimal(fee));

									flowDetailList.add(mol);
									}
								}

								int currentPage = Integer.parseInt(json
										.getString("currentPage"));
								int totalPage = Integer.parseInt(json
										.getString("totalPage"));

								if (currentPage < totalPage) {
									getFlowDetailNextPage(token, startDate,
											String.valueOf(currentPage + 1));
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
			spider.addRequest(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out
				.println(StringEscapeUtils
						.unescapeHtml3("&#22871;&#39184;&#21450;&#22266;&#23450;&#36153;"));
	}
}
