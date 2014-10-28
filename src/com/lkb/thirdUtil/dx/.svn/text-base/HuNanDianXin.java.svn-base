package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.primitives.Doubles;
import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
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

public class HuNanDianXin extends AbstractDianXinCrawler {

	public HuNanDianXin(Spider spider, User user, String phoneNo,
			String password, String authCode, WarningUtil util) {
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
		// spider.getSite().setCharset("utf-8");
	}

	public HuNanDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;
		this.util = util;
		// spider.getSite().setCharset("utf-8");
	}

	public HuNanDianXin() {
		areaName = "湖南";
		customField1 = "3";
		customField2 = "19";
		toStUrl = "&toStUrl=http://hn.189.cn:80/hnselfservice/uamlogin/uam-login!uamLoginRet.action?rUrl=/hnselfservice/billquery/bill-query!showTabs.action?tabIndex=3";
		shopId = "10019";

	}

	public void checkVerifyCode(final String userName) {
		saveVerifyCode("hunan", userName);
	}

	public void sendSmsPasswordForRequireCallLogService() {
		getEntity().put("smsPassword", 1);
		// http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tm=2028%E4%B8%8A%E5%8D%8810:22:07&tabIndex=2&queryMonth=2014-08&patitype=&valicode=130816&accNbr=18142631055&chargeType=10&_=1407378127231
		// http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tm=2028%E4%B8%8A%E5%8D%8810:23:06&tabIndex=2&queryMonth=2014-07&patitype=2&valicode=&accNbr=18142631055&chargeType=10&_=1407378186249
		getUrl("http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tabIndex=2&patitype=&valicode=&accNbr="
				+ phoneNo + "&chargeType=10&_=" + System.currentTimeMillis(),
				"http://hn.189.cn/hnselfservice/billquery/bill-query!showTabs.action",
				new AbstractProcessorObserver(util, WaringConstaint.HUNANDX_1) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							Document doc = ContextUtil
									.getDocumentOfContent(context);
							if (doc.select("#blqYearMonth").size() > 0) {
								setStatus(STAT_SUC);
								data.put("errMsg", "短信发送成功");
							} else {
								data.put("errMsg", "短信发送失败,请重试！");
							}
							notifyStatus();
						} catch (Exception e) {
							logger.error("error",e);
						}

					}
				});

	}

	// https://uam.ct10000.com/ct10000uam-gate/SSOFromUAM?ReturnURL=687474703A2F2F686E2E3138392E636E3A38302F686E73656C66736572766963652F75616D6C6F67696E2F75616D2D6C6F67696E2175616D4C6F67696E5265742E616374696F6E3F7255726C3D2F686E73656C66736572766963652F62696C6C71756572792F62696C6C2D71756572792173686F77546162732E616374696F6E3F746162496E6465783D33&ProvinceId=19

	protected void onCompleteLogin(SimpleObject context) {
		setStatus(STAT_SUC);
		notifyStatus();
		getEntity().put("complete", 1);
		sendSmsPasswordForRequireCallLogService();
		// requestService();
	}

	public void requestAllService() {
		getEntity().put("service", 1);
		requestService();
	}

	private void requestService() {
		// printEntityData();
		/*
		 * ContextUtil.getTask(context).addUrl(new
		 * AbstractProcessorObserver(util, WaringConstaint.HUNANDX_6) {
		 * 
		 * @Override public void afterRequest(SimpleObject context) { } },
		 * "http://www.ln.10086.cn/my/account/detailquery.xhtml");
		 */

		requestMyInfo();
		Date d = new Date();
		for (int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyy-MM");
			if (i > 0) {
				requestMonthBillService(cd, dstr);
			}
			// requestCallLogService(1, 0, cd, dstr);
			requestCallLogService(1, 1, cd, dstr);
			requestSmsLogService(1, 1, cd, dstr);
			requestFlowLogService(1, 1, cd, dstr);
			if (STAT_STOPPED_FAIL == getStatus()) {
				// break;
				return;
			}
			// requestCallLogService(1, 2, cd, dstr);
		}
		parseBalanceInfo();
		// requestCallLogService(context, number, queryDate);
	}

	private void parseBalanceInfo() {
		// String[][] params = {{"Action", "post"}, {"Name", "lulu"}};
		// {"accNbr":"18040212031","TSR_RESULT":"0","TSR_CODE":"0","restFee":24.04,"TSR_MSG":"","queryDay":30,"userName":"周灏","queryDate":"2014年07月30日"}
		getUrl("http://hn.189.cn/hnselfservice/billquery/bill-query!queryRealTimeTelCharge.action?tabIndex=0&accNbr="
				+ phoneNo + "&chargeType=10&_=" + System.currentTimeMillis(),
				null, new AbstractProcessorObserver(util,
						WaringConstaint.HUNANDX_7) {
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil
								.getDocumentOfContent(context);
						String userName = doc.select("div.selser_name li")
								.get(0).select("span").text();
						user.setRealName(userName);
						final Elements es1 = doc.select("div.selser_mn span");
						if (es1.size() < 1) {
							return;
						}
						setStatus(STAT_SUC);
						notifyStatus();
						BigDecimal b1 = new BigDecimal(es1.get(es1.size() - 1)
								.text().replaceAll("元", "").trim());
						addPhoneRemain(b1);
					}
				});
	}

	private void requestMyInfo() {
		getUrl("http://hn.189.cn/hnselfservice/customerinfomanager/customer-info!queryCustInfo.action",
				null, new AbstractProcessorObserver(util,
						WaringConstaint.HUNANDX_6) {
					@Override
					public void afterRequest(SimpleObject context) {
						String html = context.toString();
						String idCard = "";
						String name = "";
						if (html.contains("以下资料您在办理业务时留下的客户信息，请您仔细核对。")) {
							Document doc = Jsoup.parse(html);
							Element table = doc
									.select("table[class=sersel_table sersel_table_right]")
									.get(1);

							Elements trs = table.select("tr");
							for (int i = 0; i < trs.size(); i++) {
								if (trs.get(i).toString().contains("证件号码")) {
									idCard = trs.get(i).select("td").get(0)
											.text().replace("证件号码：", "").trim();
								} else if (trs.get(i).toString()
										.contains("客户姓名")) {
									name = trs.get(i).select("td").get(0)
											.text().replace("客户姓名：", "").trim();
								}
							}
						}
						// 存用户信息，应该掉哪个方法啊
						user.setLoginName(phoneNo);
						user.setLoginPassword("");
						user.setUserName(phoneNo);
						user.setRealName(name);
						// user.setRealName(loginName);
						user.setIdcard(idCard);
						user.setAddr("");
						user.setUsersource(Constant.DIANXIN);
						user.setModifyDate(new Date());
						user.setPhone(phoneNo);
						user.setFixphone("");
					}
				});
	}

	private void requestMonthBillService(final Date d, final String dstr) {
		// String dstr1 = DateUtils.formatDate(d, "yyyy-MM");
		getUrl("http://hn.189.cn/hnselfservice/billquery/bill-query!queryCustBillDetail.action?chargeType=10&queryMonth="
				+ dstr + "&productId=" + phoneNo, null,
				new AbstractProcessorObserver(util, WaringConstaint.HUNANDX_6) {
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil
								.getDocumentOfContent(context);
						Elements es = doc.select("#datatable table");

						DianXinTel tel = new DianXinTel();
						tel.setcTime(DateUtils.StringToDate(dstr, "yyyy-MM"));
						tel.setTeleno(phoneNo);
						getTelList().add(tel);
						if (es.size() <= 1) {
							tel.setcAllPay(new BigDecimal(0));
							return;
						}
						es = doc.select("#Table4").get(0).select("tr");
						Element e = es.get(es.size() - 1).select("td").get(0);
						String n = StringUtil
								.subStr("本期费用合计：", "本期已付费用：", e.text())
								.replaceAll("\r", "").replaceAll("\n", "")
								.trim();
						BigDecimal b1 = new BigDecimal(n.length() == 0 ? "0"
								: n);
						tel.setcAllPay(b1);
						data.put("Month" + dstr, d);
						data.put("Pay" + dstr, n.trim());
					}
				});
	}

	private void requestCallLogService(final int page, final int t,
			final Date d, final String dstr) {
		getUrl("http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tabIndex=2&queryMonth="
				+ dstr
				+ "&patitype=2&pageNo="
				+ page
				+ "&valicode="
				+ authCode
				+ "&accNbr="
				+ phoneNo
				+ "&chargeType=10&_="
				+ System.currentTimeMillis(), null,
				new AbstractProcessorObserver(util, WaringConstaint.HUNANDX_6) {
					@Override
					public void afterRequest(SimpleObject context) {
						saveCallLog(context, page, d, dstr);
					}
				});
	}

	private void saveCallLog(SimpleObject context, final int page,
			final Date d, final String dstr) {
		String text1 = ContextUtil.getContent(context);
		if (text1 == null) {
			return;
		}
		try {
			Document doc = ContextUtil.getDocumentOfContent(context);
			Elements table = doc.select("table.sersel_table");
			if (table.size() <= 0) {
				if (text1.indexOf("uamlogin/uam-login!login.action") >= 0) {
					setStatus(STAT_STOPPED_FAIL);
					data.put("errMsg", "需要重新登录！");
					notifyStatus();
					spider.stop();
				}
			} else {
				if (text1.indexOf("请输入正确的随机码") >= 0) {
					setStatus(STAT_STOPPED_FAIL);
					data.put("errMsg", "随机码错误，请重新输入！");
					notifyStatus();
					spider.stop();
					return;
				}
				setStatus(STAT_SUC);
				// notifyStatus();
				// table = doc.select("table.sersel_table");
				Elements trs = table.select("tr");
				int len = trs.size();
				String dstr1 = null;
				for (int i = 2; i < len; i++) {
					Element e = trs.get(i);
					Elements tds = e.select("th,td");
					if (tds.size() == 1) {
						dstr1 = tds.text();
					} else {
						try {
							DianXinDetail dxDetail = new DianXinDetail();
							dxDetail.setPhone(phoneNo);
							for (int j = 1; j < tds.size(); j++) {
								String text = tds.get(j).text();
								if (j == 1) {
									dxDetail.setcTime(DateUtils.StringToDate(
											dstr1 + " " + text,
											"yyyy-MM-dd HH:mm:ss"));
								} else if (j == 2) {
									dxDetail.setCallWay(text);
								} else if (j == 3) {
									dxDetail.setRecevierPhone(text);
								} else if (j == 4) {
									dxDetail.setTradeTime(new TimeUtil()
											.timetoint(text));
								} else if (j == 5) {
									dxDetail.setTradeAddr(text);
								} else if (j == 6) {
									dxDetail.setAllPay(new BigDecimal(text));
								} else if (j == 7) {
									dxDetail.setTradeType(text);
								}
							}
							detailList.add(dxDetail);
						} catch (Exception e1) {
							logger.error("detail" + e.html(), e1);
						}
					}

				}
				if (len > 3) {
					requestCallLogService(page + 1, 1, d, dstr);
				}
			}

		} catch (Exception e) {
			logger.error("detail" + text1, e);
		}

	}

	/**
	 * <p>
	 * 流量存储
	 * <p>
	 * author:Pat.Liu
	 * */
	private void requestFlowLogService(final int page, final int t,
			final Date d, final String dstr) {
		// 查询用户移动数据记录
		String url_msg = "http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tabIndex=2&queryMonth="
				+ dstr
				+ "&patitype=9&pageNo="
				+ page
				+ "&valicode="
				+ authCode
				+ "&accNbr="
				+ phoneNo
				+ "&chargeType=10&_="
				+ System.currentTimeMillis();
		postUrl(url_msg, null, null, new AbstractProcessorObserver(util,
				WaringConstaint.HUNANDX_7) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				if (text != null && !text.contains("尊敬的客户，对不起，未查询到相关记录")) {
					Pattern p = Pattern.compile("总页数：(.*?)当前页：");// 正则表达式，取=和|之间的字符串，不包括=和|
					Matcher m = p.matcher(text);
					m.find();
					try {
						int pageCount = Integer.parseInt(m.group(1).trim());
						if (pageCount >= 1) {
							saveFlowLog(context, pageCount, dstr);
						}
					} catch (Exception e) {
						logger.error("error",e);
						return;
					}
				}
			}
		});
	}

	private int getData(String str) {
		if (str.indexOf(":") > 0)
			return str.indexOf(":")+1;
		else if (str.indexOf("：") > 0)
			return str.indexOf("：")+1;
		else
			return 0;
	}

	private void saveFlowLog(SimpleObject context, final int pageCount,
			final String dstr) {
		for (int j = 1; j <= pageCount; j++) {
			// 查询用户移动数据记录
			String url_msg = "http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tabIndex=2&queryMonth="
					+ dstr
					+ "&patitype=9&pageNo="
					+ j
					+ "&valicode="
					+ authCode
					+ "&accNbr="
					+ phoneNo
					+ "&chargeType=10&_="
					+ System.currentTimeMillis();
			postUrl(url_msg, null, null, new AbstractProcessorObserver(util,
					WaringConstaint.HUNANDX_7) {
				@Override
				public void afterRequest(SimpleObject context) {
					String text = ContextUtil.getContent(context);
					if (text != null && !text.contains("尊敬的客户，对不起，未查询到相关记录")) {
						try {
							Document doc = ContextUtil
									.getDocumentOfContent(context);
							Elements trs = doc.select("table.sersel_table")
									.select("tr");
							if (trs == null || trs.toString().equals("")) {
								return;
							}
							DianXinFlow newBill = new DianXinFlow();
							newBill.setId(UUID.randomUUID().toString());
							newBill.setPhone(phoneNo);
							for (int i = 2; i < trs.size() - 1; i++) {// 前两个是空的<tr>没啥用~写0也无所谓
								Elements tds = trs.get(i).select("td");
								try {
									if (tds.size() == 2) {// 只有每月的第一页有size=2的
										// dstr为2014-10这种格式的
										if (tds.text().contains("日期")) {
											newBill.setDependCycle(tds
													.get(0)
													.text()
													.substring(
															getData(tds.get(0)
																	.text()))
													.trim());
										}
										if (tds.text().contains("流量")) {
											String flow = tds
													.get(0)
													.text()
													.substring(
															getData(tds.get(0)
																	.text()))
													.trim();
											newBill.setAllFlow(new BigDecimal(
													StringUtil.flowFormat(flow)));
											String time = tds
													.get(1)
													.text()
													.substring(
															getData(tds.get(1)
																	.text()))
													.trim();
											newBill.setAllTime(new BigDecimal(
													Integer.toString(TimeUtil
															.timetoint(time))));
										}
										if (tds.text().contains("费用")) {
											String fee = tds
													.get(0)
													.text()
													.substring(
															getData(tds.get(0)
																	.text()))
													.trim();
											newBill.setAllPay(new BigDecimal(
													fee));
											newBill.setQueryMonth(DateUtils
													.StringToDate(dstr + "-01",
															"yyyy-MM-dd"));
											flowList.add(newBill);
										}
									}
									if (tds.size() == 8) {// 流量详单是8
										String sentTime = tds.get(1).text()
												.trim();
										String tradeTime = tds.get(2).text()
												.trim();
										String allFlow = tds.get(3).text()
												.trim();
										String netType = tds.get(4).text()
												.trim();
										String location = tds.get(5).text()
												.trim();
										String allPay = tds.get(6).text()
												.trim();
										String business = tds.get(7).text()
												.trim();

										DianXinFlowDetail obj = new DianXinFlowDetail();
										obj.setPhone(phoneNo);
										obj.setBeginTime(DateUtils
												.StringToDate(sentTime,
														"yyyy-MM-dd HH:mm:ss"));// 时间
										obj.setTradeTime(Long
												.parseLong(tradeTime));// 时长
										obj.setFlow(new BigDecimal(StringUtil
												.flowFormat(allFlow)));
										obj.setNetType(netType);
										obj.setLocation(location);
										obj.setFee(new BigDecimal(allPay));
										obj.setBusiness(business);
										flowDetailList.add(obj);
									}
								} catch (Exception e) {
									logger.error("HuNanDianXin:" + tds, e);
								}
							}
						} catch (Exception e) {
							logger.error("HuNanDianXin:", e);
						}
					}
				}
			});
		}
	}

	private void requestSmsLogService(final int page, final int t,
			final Date d, final String dstr) {
		// 查询用户条短消息记录
		String url_msg = "http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tabIndex=2&queryMonth="
				+ dstr
				+ "&patitype=12&pageNo="
				+ page
				+ "&valicode="
				+ authCode
				+ "&accNbr="
				+ phoneNo
				+ "&chargeType=10&_="
				+ System.currentTimeMillis();
		postUrl(url_msg, null, null, new AbstractProcessorObserver(util,
				WaringConstaint.HUNANDX_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				if (text != null && !text.contains("尊敬的客户，对不起，未查询到相关记录")) {
					Pattern p = Pattern.compile("总页数：(.*?)当前页：");// 正则表达式，取=和|之间的字符串，不包括=和|
					Matcher m = p.matcher(text);
					m.find();
					try {
						int pageCount = Integer.parseInt(m.group(1).trim());
						if (pageCount >= 1) {
							saveSmsLog(context, pageCount, dstr);
						}
					} catch (Exception e) {
						logger.error("error",e);
						return;
					}
				}
			}
		});
	}

	private void saveSmsLog(SimpleObject context, final int pageCount,
			final String dstr) {
		for (int j = 1; j <= pageCount; j++) {
			// 查询用户条短消息记录
			String url_msg = "http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tabIndex=2&queryMonth="
					+ dstr
					+ "&patitype=12&pageNo="
					+ j
					+ "&valicode="
					+ authCode
					+ "&accNbr="
					+ phoneNo
					+ "&chargeType=10&_="
					+ System.currentTimeMillis();
			postUrl(url_msg, null, null, new AbstractProcessorObserver(util,
					WaringConstaint.HUNANDX_4) {
				@Override
				public void afterRequest(SimpleObject context) {
					String text = ContextUtil.getContent(context);
					if (text != null && !text.contains("尊敬的客户，对不起，未查询到相关记录")) {
						try {
							Document doc = ContextUtil
									.getDocumentOfContent(context);
							Elements trs = doc.select("table.sersel_table")
									.select("tr");
							if (trs == null || trs.toString().equals("")) {
								return;
							}
							String date = "";
							for (int i = 2; i < trs.size() - 1; i++) {
								Elements tds = trs.get(i).select("td");
								if (tds.size() == 1) {
									date = tds.get(0).text().trim();
								}
								if (tds.size() == 6) {

									String SentTime = date + " "
											+ tds.get(1).text().trim();
									String RecevierPhone = tds.get(3).text()
											.trim();
									String AllPay = tds.get(4).text().trim();
									String BusinessType = tds.get(5).text()
											.trim();

									TelcomMessage obj = new TelcomMessage();
									obj.setPhone(phoneNo);
									obj.setBusinessType(BusinessType);// 业务类型：点对点
									obj.setRecevierPhone(RecevierPhone);// 对方号码
									obj.setSentTime(DateUtils.StringToDate(
											SentTime, "yyyy-MM-dd HH:mm:ss"));// 发送时间
									obj.setAllPay(Doubles.tryParse(AllPay));// 总费用
									messageList.add(obj);
								}
							}
						} catch (Exception e) {
							logger.error("error",e);
						}
					}
				}
			});
		}
	}

	public static void main(String[] args) throws Exception {
		String phoneNo = "18142631055";
		String password = "740586";

		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		HuNanDianXin dx = new HuNanDianXin(spider, null, phoneNo, password,
				"2345", null);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();
		dx.printData();
		if (true) {
			return;
		}

	}
}
