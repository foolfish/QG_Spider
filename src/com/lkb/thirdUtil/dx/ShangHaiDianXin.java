package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.print.Doc;

import org.apache.http.client.CookieStore;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.proxy.ProxyManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.robot.util.CookieStoreUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

import freemarker.template.utility.DateUtil;

public class ShangHaiDianXin extends AbstractDianXinCrawler {

	public ShangHaiDianXin(Spider spider, User user, String phoneNo,
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

	public ShangHaiDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;
		this.util = util;
		// spider.getSite().setCharset("utf-8");
	}

	public ShangHaiDianXin() {
		areaName = "上海";
		customField1 = "3";
		customField2 = "02";
		toStUrl = "&toStUrl=http://sh.189.cn/service/account_manage_orderinfo.do?method=init";
		shopId = "10003";
	}

	public void showImgWhenSendSMS(final String phone) {
		data.put("checkVerifyCode", "1");
		String picName = "shanghai_dx_sms_" + phone + "_"
				+ (int) (Math.random() * 1000) + "3dw";
		try {
			String imgName = saveFile(
					"http://sh.189.cn/service/RandomNum_new2.jsp?"
							+ Math.random(),
					"http://sh.189.cn/service/redirect.do?service=detailQuery&tmp=",
					null, picName, true);
			data.put("imgName", imgName);
		} catch (Exception e) {
			notifyStatus();
		}
	}

	public void checkVerifyCode(final String userName) {
		saveVerifyCode("shanghai", userName);
		spider.setProxyHolder(ProxyManager.PROXY_HOLDER_ONE);
		// 1.生成一个request
		// 2.请求完成后的解析
		// 3.加入到Spider的执行队列
	}

	private String[][] genHeaders() {
		return new String[][] {
				{ "Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" },
				{ "Accept-Encoding", "gzip, deflate" },
				{ "Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3" },
				{ "Cache-Control", "no-cache" },
				{ "Connection", "keep-alive" },
				{ "Content-Type", "text/plain; charset=UTF-8" },
				{ "Pragma", "no-cache" },
				// {"Host","sh.189.cn"},
				// {"Referer","http://sh.189.cn/service/redirect.do?service=detailQuery&tmp="},
				{ "User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0" } };
	}

	public void sendSmsPasswordForRequireCallLogService(String authValue) {
		String[][] pairs = new String[][] { { "dynnum", "" },
				{ "imgnum", authValue }, { "requesttype", "send" },
				{ "oper", "cnetQueryCondition.do?actionCode=init" } };
		String[][] headers = genHeaders();
		postUrl("http://sh.189.cn/service/dynamicValidate.do",
				"http://sh.189.cn/service/redirect.do?service=detailQuery&tmp=0.00145258",
				new String[] { "gbk" }, pairs, headers,
				new AbstractProcessorObserver(util, WaringConstaint.SHDX_5) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {// chwlazq=1; chwlaz=1408419775471;
								// chweblog_itemq=1; chweblog_contentsq=1;
								// chweblog_contents=1408419775480;
								// chweblog_eventq=1;
								// chweblog_event=1408419775489;
								// _gscbrs_1708861450=1;
								// s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-sh%253D%252526pid%25253D%2525252Fsh%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fwww.189.cn%2525252Fdqmh%2525252FfrontLink.do%2525253Fmethod%2525253DlinkTo%25252526shopId%2525253D10003%25252526toStUrl%2525253Dhttp%2525253A%2525252F%2525252Fsh.189.cn%2525252Fservice%2525252Facco%252526ot%25253DA%252526oi%25253D290%3B;
								// loginStatus=logined; s_cc=true;
								// _gscu_1708861450=08414613xm7uze18;
								// s_pers=%20s_fid%3D313057A2881BEAB7-116456AD7C614BDE%7C1471578149690%3B;
								// svid=57d164063536cf356b8e297ca4c33c07;
								// _gscs_1708861450=t08418162gw68gv51|pv:30;
								// SHOPID_COOKIEID=10003;
								// DebugUtil.findMissing(ContextUtil.getCookieStore(context),
								// "JSESSIONID=0000GMoVKALFth58OMv9HqGnUI3:157a3ffd6; chwlazq=1; chwlaz=1408419775471; chweblog_itemq=1; chweblog_contentsq=1; chweblog_contents=1408419775480; chweblog_eventq=1; chweblog_event=1408419775489; NSC_xu-222.68.185.229=ffffffffc3a01f1945525d5f4f58455e445a4a423660; citrix_ns_id=22eu/e1YOHVEyyynnQOL3KL9Vz4A000; citrix_ns_id_.189.cn_%2F_wat=SlNFU1NJT05JRF9f?Kh20KFhZ8PrPUwbNSoV1/aSiUmsA#qbg2R/Rp9UAoyjTa8a72ZkzKpoIA&; _gscbrs_1708861450=1; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-sh%253D%252526pid%25253D%2525252Fsh%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fwww.189.cn%2525252Fdqmh%2525252FfrontLink.do%2525253Fmethod%2525253DlinkTo%25252526shopId%2525253D10003%25252526toStUrl%2525253Dhttp%2525253A%2525252F%2525252Fsh.189.cn%2525252Fservice%2525252Facco%252526ot%25253DA%252526oi%25253D290%3B; loginStatus=logined; s_cc=true; userId=201|151669205; cityCode=sh; citrix_ns_id_.189.cn_%2F_wlf=TlNDX3h1LTIyMi42OC4xODUuMjI5?NsNVGZ1ouCBNq/CMOEeCK+p6z4YA&; _gscu_1708861450=08414613xm7uze18; s_pers=%20s_fid%3D313057A2881BEAB7-116456AD7C614BDE%7C1471578149690%3B; svid=57d164063536cf356b8e297ca4c33c07; _gscs_1708861450=t08418162gw68gv51|pv:30; SHOPID_COOKIEID=10003; isLogin=logined; .ybtj.189.cn=590566E9B59ACF4CAED1A5D93A77C015");
								// DebugUtil.printCookieData(ContextUtil.getCookieStore(context),
								// "sh.189.cn");//{"go":"false","msg":"已发送动态验证码到手机,请查收!","url":""}
							JSONObject json = ContextUtil
									.getJsonOfContent(context);
							if (json != null && !json.isNull("msg")) {
								if (json.getString("msg").indexOf("发送动态验证码") >= 0) {
									setStatus(STAT_SUC);
								} else {
									data.put("errMsg", json.getString("msg"));
									setStatus(STAT_STOPPED_FAIL);
								}
							} else {
								data.put("errMsg", "图片验证码输入错误,动态验证码发送失败,请重新输入");
								setStatus(STAT_STOPPED_FAIL);
							}
						} catch (Exception e) {
							logger.error(
									"sendSmsPasswordForRequireCallLogService",
									e);
							setStatus(STAT_STOPPED_FAIL);
						}
					}
				});
	}

	protected void onCompleteLogin(SimpleObject context) {
		// <script
		// language="javascript">top.location="/service/uiss_mobileLogin.do?method=login
		// logger.info(ContextUtil.getContent(context));
		// com.lkb.debug.DebugUtil.printCookieData(ContextUtil.getCookieStore(context),
		// "sh.189.cn");
		String text = ContextUtil.getContent(context);
		if (text != null
				&& text.indexOf("service/uiss_mobileLogin.do?method=login") >= 0) {
			setStatus(STAT_LOGIN_SUC);
			getUrl("http://sh.189.cn/service/uiss_mobileLogin.do?method=login",
					null, null);
		} else {
			setStatus(STAT_STOPPED_FAIL);
			data.put("errMsg", "登录失败, 请重试！");
		}
	}

	public void requestAllService(String duanxin, String tupian) {
		String[][] pairs = new String[][] { { "dynnum", duanxin },
				{ "imgnum", tupian }, { "requesttype", "end" },
				{ "oper", "cnetQueryCondition.do?actionCode=init" } };
		String[][] headers = genHeaders();
		postUrl("http://sh.189.cn/service/dynamicValidate.do",
				"http://sh.189.cn/service/redirect.do?service=detailQuery&tmp=",
				null, pairs, headers, new AbstractProcessorObserver(util,
						WaringConstaint.SHDX_4) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							// {"go":"true","msg":"","url":"cnetQueryCondition.do?actionCode=init"}
							boolean suc = false;
							JSONObject json = ContextUtil
									.getJsonOfContent(context);
							if (json == null) {
								Document doc = ContextUtil
										.getDocumentOfContent(context);
								if (doc != null) {
									Elements es = doc
											.select("div.resultSuccessInfo");
									if (es.size() > 0) {
										data.put("errMsg", es.text());
										setStatus(STAT_STOPPED_FAIL);
										return;
									}
								}

							}
							if (json != null && !json.isNull("go")) {
								if (json.getString("go").equals("true")) {
									setStatus(STAT_SUC);
									suc = true;
									notifyStatus();
									requestService();
								}
							}
							if (!suc) {
								if (json != null && !json.isNull("msg")) {
									data.put("errMsg", json.getString("msg"));
								} else {
									data.put("errMsg", "动态验证码输入错误!");
								}
								setStatus(STAT_STOPPED_FAIL);
							}
						} catch (Exception e) {
							logger.info(com.lkb.robot.util.ContextUtil
									.getContent(context));
							logger.error(
									"sendSmsPasswordForRequireCallLogService",
									e);
						}
					}
				});

	}

	private void requestService() {
		Date d = new Date();
		for (int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyy/MM");
			// 当前月查询
			try{
			if (i == 0) {
				requestThisMonthService(
						1,
						0,
						DateUtils.formatDate(
								DateUtils.add(d, Calendar.MONTH, 0), "yyyy/MM")
								+ "/01", DateUtils.formatDate(
								DateUtils.add(d, Calendar.MONTH, 0),
								"yyyy/MM/dd"), 0);// t==0表示语音，1表示短信,2表示流量
				requestThisMonthService(
						1,
						1,
						DateUtils.formatDate(
								DateUtils.add(d, Calendar.MONTH, 0), "yyyy/MM")
								+ "/01", DateUtils.formatDate(
								DateUtils.add(d, Calendar.MONTH, 0),
								"yyyy/MM/dd"), 0);// t==0表示语音，1表示短信,2表示流量
				requestThisMonthService(
						1,
						2,
						DateUtils.formatDate(
								DateUtils.add(d, Calendar.MONTH, 0), "yyyy/MM")
								+ "/01", DateUtils.formatDate(
										DateUtils.add(d, Calendar.MONTH, 0),
										"yyyy/MM/dd"), 0);// t==0表示语音，1表示短信,2表示流量
			}}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//历史查询
		for (int i = 0; i < 6; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyy/MM");
			
			final Date cd1 = DateUtils.add(d, Calendar.MONTH, (-1 * i)-1);
			String date = DateUtils.formatDate(cd1, "yyyy/MM");
			
			requestOnlineFlowService(1, 1, cd, dstr, 1, date);
			requestCallLogService(1, 1, cd, dstr, 1);
			requestSmsLogService(1, 1, cd, dstr, 1);
		
		}
		

		parseBalanceInfo();
		//查不出来账单
		//requestMonthBillService();
	}

	// 当前月的详单查询除了第一页，后面的请求语音和短信都一样，互相穿插还会冲突~
	private void requestThisMonthService(final int page, final int t,
			final String beginDate, final String endDate, final int isHistory) {
		// t==0为当前月语音详单查询第一页post，t=1为短信,t=2为流量
		String[][] pairs = new String[][] { { "detailType", "" },
				{ "devNo", phoneNo }, { "deviceNo", phoneNo },
				 { "endDate", endDate },
				{ "startDate", beginDate }, { "flag", "实时" },
				{ "menuid", "4" }, { "selectType", "实时" }, { "subNo", "null" },{"dou","0.45"},{"actionCode","query"} };
		if (page == 1 && t == 0) {
			pairs[0][1] = "SCP";
			postUrl("http://sh.189.cn/service/cnetQueryCondition.do",
					"http://sh.189.cn/service/redirect.do",
					pairs,
					new AbstractProcessorObserver(util, WaringConstaint.SHDX_4) {
						public void afterRequest(SimpleObject context) {
							saveCallLog(context, page, t, new Date(), endDate,
									isHistory);
						}
					});
		} else if (page == 1 && t == 1) {
			pairs[0][1] = "SMSC";
			postUrl("http://sh.189.cn/service/cnetQueryCondition.do",
					"http://sh.189.cn/service/redirect.do",
					pairs,
					new AbstractProcessorObserver(util, WaringConstaint.SHDX_6) {
						public void afterRequest(SimpleObject context) {
							saveSmsLog(context, page, t, new Date(), endDate,
									isHistory);
						}
					});
		}else if (page == 1 && t == 2) {
			pairs[0][1] = "AAA";
			postUrl("http://sh.189.cn/service/cnetQueryCondition.do",
					"http://sh.189.cn/service/redirect.do",
					pairs,
					new AbstractProcessorObserver(util, WaringConstaint.SHDX_7) {
						public void afterRequest(SimpleObject context) {
							saveOnlineFlow(context, page, t, new Date(), endDate, isHistory, DateUtils.formatDate(new Date(), "yyyy/MM"));
						}
					});
		} else {
			getUrl("http://sh.189.cn/service/detailPage.do?currentPage=" + page
					+ "&menuid=4&version=2.0&deviceNo=" + phoneNo
					+ "&subNo=null", "", new AbstractProcessorObserver(util,
					WaringConstaint.SHDX_6) {
				@Override
				public void afterRequest(SimpleObject context) {
					if (t == 0)
						saveCallLog(context, page, t, new Date(), endDate,
								isHistory);
					else if (t==1) {
						saveSmsLog(context, page, t, new Date(), endDate,
								isHistory);
					} else if (t==2) {
						saveOnlineFlow(context, page, t, new Date(), endDate, isHistory, DateUtils.formatDate(new Date(), "yyyy/MM"));
					}
						
				}
			});
		}
		return;
	}

	private void parseBalanceInfo() {
		getUrl("http://sh.189.cn/service/selectBallance.do?method=doSelect&isRight=0&mySel=%CA%D6%BB%FA-"
				+ phoneNo + "&adslAccount=&accountPWD=&queryType=0&check=52",
				null, new AbstractProcessorObserver(util,
						WaringConstaint.SHDX_7) {
					@Override
					public void afterRequest(SimpleObject context) {
						String text = ContextUtil.getContent(context);
						if (text != null && text.indexOf("查询成功") >= 0) {
							try {
								Document doc = ContextUtil
										.getDocumentOfContent(context);
								Elements es = doc
										.select("tr[bgcolor=mistyrose]");
								if (es.size() <= 0) {
									return;
								}
								Element tr = es.get(es.size() - 1);
								Elements tds = tr.select("td");
								String balance = tds.get(tds.size() - 3).text();
								BigDecimal b1 = new BigDecimal(balance
										.replaceAll("元", ""));
								addPhoneRemain(b1);
							} catch (Exception e) {
								logger.error("balanceInfo", e);
							}
						} else {
							data.put("balanceInfo1", "查询不成功");
						}
					}
				});
		getUrl("http://sh.189.cn/service/wblogin_gk.do", null,
				new AbstractProcessorObserver(util, WaringConstaint.SHDX_7) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							Document doc = ContextUtil
									.getDocumentOfContent(context);
							Elements es = doc.select(".zzfw_wdxx_boxspan");
							if (es.size() <= 0) {
								return;
							}
							String[] t = es.text().split(" ");
							user.setRealName(t[0]);
						} catch (Exception e) {
							logger.error("balanceInfo", e);
						}
					}
				});
		getUrl("http://sh.189.cn/service/curAccount.do?method=userInfo&menuid=3&canAdd2Tool=canAdd2Tool",
				"http://sh.189.cn/service/AccountManageAction.do?method=init",
				null, new String[][] { { "Accept-Language", "zh-CN" } },
				new AbstractProcessorObserver(util, WaringConstaint.SHDX_7) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							Document doc = ContextUtil
									.getDocumentOfContent(context);
							Elements es = doc.select("table[style]");
							if (es.size() <= 0) {
								return;
							}
							final String text = es.text();
							String[] sd = StringUtil.subStr("入网日期", "套餐名称",
									text).split("：");
							user.setRegisterDate(DateUtils.StringToDate(
									sd[1].trim(), "yyyy-MM-dd"));
							String t = StringUtil.subStr("套餐名称：", "套餐开始日期：",
									text);
							user.setPackageName(t.trim());
						} catch (Exception e) {
							logger.error("balanceInfo", e);
						}
					}
				});
	}

	private void requestMonthBillService() {
		postUrl("http://he.189.cn/chargeQuery/chargeQuery_queryMonthBill.action?months=6&queryKind=2&productType=8",
				"http://he.189.cn/group/bill/bill_detail.do", null,
				new AbstractProcessorObserver(util, WaringConstaint.SHDX_7) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							JSONArray json = ContextUtil
									.getJsonArrayOfContent(context);
							if (json == null) {
								return;
							}
							int len = json.length();
							for (int i = 0; i < len; i++) {
								JSONObject jobj = json.getJSONObject(i);
								DianXinTel tel = new DianXinTel();
								tel.setTeleno(phoneNo);
								getTelList().add(tel);
								tel.setcTime(DateUtils.StringToDate(
										jobj.getString("billMonthStr"),
										"yyyy年MM月"));
								String n = jobj.getString("bill");
								BigDecimal b1 = new BigDecimal(
										n.length() == 0 ? "0" : n);
								tel.setcAllPay(b1);
							}
						} catch (Exception e) {
							logger.error("requestMonthBillService", e);
						}
					}
				});

	}
	private void requestOnlineFlowService(final int page, final int t,
			final Date d, final String dstr, final int isHistory, final String date) {
		getUrl("http://sh.189.cn/service/cnetQueryCondition.do?currentPage="
				+ page + "&deviceNo=" + phoneNo
				+ "&detailType=AAA&menuid=4&flag=历史&devNo=" + phoneNo
				+ "&actionCode=query&selectType=历史详单" + "&queryDate=" + dstr
				+ "&subNo=null",
				"http://he.189.cn/group/bill/bill_billlist.do", null,
				new AbstractProcessorObserver(util, WaringConstaint.SHDX_6) {
					@Override
					public void afterRequest(SimpleObject context) {
						saveOnlineFlow(context, page, t, d, dstr, isHistory, date);
					}
				});
	}
	private void saveOnlineFlow(SimpleObject context, final int page, final int t,
			final Date d, final String dstr, final int isHistory, final String date) {
		String text = ContextUtil.getContent(context);
		Document doc = ContextUtil.getDocumentOfContent(context);
		System.out.println(doc.toString());
		DianXinFlow dxFlow = new DianXinFlow();
		UUID uuid = UUID.randomUUID();
		dxFlow.setId(uuid.toString());
		dxFlow.setPhone(phoneNo);
		
		Date queryMonth = DateUtils.StringToDate(date, "yyyy/MM");
		// 查询日期
		dxFlow.setQueryMonth(queryMonth);
		String cycle = "";
		if(isHistory ==0){
			cycle = date + "/01-" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
		} else {
			cycle = date + "/01-" + DateUtils.lastDayOfMonth(date, "yyyy/MM", "yyyy/MM/dd");
		}
		dxFlow.setDependCycle(cycle);
		if (text.indexOf("没有查找到相关数据") >= 0) {
			if(page == 1){
			dxFlow.setAllFlow(new BigDecimal(0));
			dxFlow.setAllTime(new BigDecimal(0));
			dxFlow.setAllPay(new BigDecimal(0.00));
			flowList.add(dxFlow);
			}
		} else {
			if (page == 1) {
				Elements tables = doc.select("table");
				if (isHistory == 0) {
					Element tr1 = tables.get(1).select("tr").get(1);
					String allpay = tr1.select("td").get(3).text()
							.replaceAll("元", "").trim();
					if (allpay.equals("")) {
						allpay = "0";
					}
					String allTime = tr1.select("td").get(5).text().trim();
					String allFlow = tr1.select("td").get(7).text().trim();

					dxFlow.setAllFlow(new BigDecimal(allpay));
					dxFlow.setAllTime(new BigDecimal(StringUtil
							.flowTimeFormat(allTime)));
					dxFlow.setAllPay(new BigDecimal(StringUtil
							.flowFormat(allFlow)));
					flowList.add(dxFlow);
				} else {
					Element tr1 = tables.get(2).select("tr").get(1);
					String allpay = tr1.select("td").get(3).text()
							.replaceAll("元", "").trim();
					if (allpay.equals("")) {
						allpay = "0";
					}
					String allTime = tr1.select("td").get(5).text().trim();
					String allFlow = tr1.select("td").get(7).text().trim();

					dxFlow.setAllFlow(new BigDecimal(allpay));
					dxFlow.setAllTime(new BigDecimal(StringUtil
							.flowTimeFormat(allTime)));
					dxFlow.setAllPay(new BigDecimal(StringUtil
							.flowFormat(allFlow)));
					flowList.add(dxFlow);
				}
			}
		String tableSort = InfoUtil.getInstance().getInfo("dx/sh", "tableSort");
		String tbody = InfoUtil.getInstance().getInfo("dx/sh", "tbody");
		String tr = InfoUtil.getInstance().getInfo("dx/sh", "tr");
		String td = InfoUtil.getInstance().getInfo("dx/sh", "td");
		Elements elements = doc.select(tableSort);
		if (elements != null && elements.size() > 0) {
			Elements elements2 = elements.first().select(tbody).first()
					.select(tr);
			for (int j = 0; j < elements2.size(); j++) {
				try {
					Elements tds = elements2.get(j).select(td);
					if (tds.size() == 9&&isHistory==1) {
						String beginTime = tds.get(3).text().trim();// 开始时间
						String fee = tds.get(8).text().trim().replaceAll("元", "");// 费用（元）
						//String RecevierPhone = tds.get(2).text().trim();// 对方号码
						String netType = tds.get(1).text().trim();// 通讯类型
						//String time_flow = tds.get(4).text().trim();// 时长/流量(分钟/KB)
						int tradeTime = TimeUtil.timetoint_HH_mm_ss(tds.get(5).text().trim().replaceAll("小时", ":").replaceAll("分", ":").replaceAll("秒", ""));// 上网时长
						String flow = tds.get(6).text().trim().replaceAll("KB", "");// 总流量
						String business = tds.get(7).text().trim();// 漫游类型
						Date beginTimeDate = null;
						BigDecimal feeDecimal = new BigDecimal(0);
						BigDecimal flows = new BigDecimal(0);
						try {
							beginTimeDate = DateUtils.StringToDate(beginTime,
									"yyyy-MM-dd HH:mm:ss");
							feeDecimal = new BigDecimal(fee);
							flows  = new BigDecimal(flow);
						} catch (Exception e) {
							e.printStackTrace();
						}
						DianXinFlowDetail obj = new DianXinFlowDetail();
						obj.setPhone(phoneNo);
						obj.setBeginTime(beginTimeDate);
						obj.setFee(feeDecimal);
						obj.setNetType(netType);
						obj.setTradeTime(tradeTime);
						obj.setFlow(flows);
						obj.setBusiness(business);
						flowDetailList.add(obj);
					}else if (tds.size() == 8&&isHistory==0) {
						String beginTime = tds.get(1).text().trim();// 开始时间
						String fee = tds.get(2).text().trim().replaceAll("元", "");// 费用（元）
						//String RecevierPhone = tds.get(2).text().trim();// 对方号码
						String netType = tds.get(3).text().trim();// 通讯类型
						//String time_flow = tds.get(4).text().trim();// 时长/流量(分钟/KB)
						int tradeTime = TimeUtil.timetoint_HH_mm_ss(tds.get(5).text().trim().replaceAll("小时", ":").replaceAll("分", ":").replaceAll("秒", ""));// 上网时长
						int flow = Integer.parseInt(tds.get(6).text().trim().replaceAll("KB", ""));// 总流量
						String business = tds.get(7).text().trim();// 漫游类型
						Date beginTimeDate = null;
						BigDecimal feeDecimal = new BigDecimal(0);
						try {
							beginTimeDate = DateUtils.StringToDate(beginTime,
									"yyyy-MM-dd HH:mm:ss");
							feeDecimal = new BigDecimal(fee);
						} catch (Exception e) {
							e.printStackTrace();
						}
						DianXinFlowDetail obj = new DianXinFlowDetail();
						obj.setPhone(phoneNo);
						obj.setBeginTime(beginTimeDate);
						obj.setFee(feeDecimal);
						obj.setNetType(netType);
						obj.setTradeTime(tradeTime);
						obj.setFlow(new BigDecimal(flow));
						obj.setBusiness(business);
						flowDetailList.add(obj);
					}

				} catch (Exception e) {
					logger.error("saveSmsLog", e);
				}
			}
			if (text.contains("下一页")) {
				if (text.contains("下一页")) {
					if (isHistory == 1)
						requestOnlineFlowService(page + 1, 2, d, dstr, isHistory, date);
					else
						requestThisMonthService(page + 1, t, "", "", isHistory);
				}
			}
		}
		}

	}
	private void requestCallLogService(final int page, final int t,
			final Date d, final String dstr, final int isHistory) {
		getUrl("http://sh.189.cn/service/cnetQueryCondition.do?currentPage="
				+ page + "&deviceNo=" + phoneNo
				+ "&detailType=SCP&menuid=4&flag=历史&devNo=" + phoneNo
				+ "&actionCode=query&selectType=历史详单" + "&queryDate=" + dstr
				+ "&subNo=null",
				"http://he.189.cn/group/bill/bill_billlist.do", null,
				new AbstractProcessorObserver(util, WaringConstaint.SHDX_6) {
					@Override
					public void afterRequest(SimpleObject context) {
						saveCallLog(context, page, t, d, dstr, isHistory);
					}
				});
	}

	private void saveCallLog(SimpleObject context, final int page, final int t,
			final Date d, final String dstr, final int isHistory) {
		String text = ContextUtil.getContent(context);
		Document doc = ContextUtil.getDocumentOfContent(context);
		System.out.println(doc.toString());
		if (text.indexOf("没有查找到相关数据") >= 0) {
			return;
		}
		String tableSort = InfoUtil.getInstance().getInfo("dx/sh", "tableSort");
		String tbody = InfoUtil.getInstance().getInfo("dx/sh", "tbody");
		String tr = InfoUtil.getInstance().getInfo("dx/sh", "tr");
		String td = InfoUtil.getInstance().getInfo("dx/sh", "td");
		Elements elements = doc.select(tableSort);
		if (elements != null && elements.size() > 0) {
			Elements elements2 = elements.first().select(tbody).first()
					.select(tr);
			for (int j = 0; j < elements2.size(); j++) {
				try {
					Elements elements3 = elements2.get(j).select(td);

					String tradeType = elements3.get(1).text(); // 通话类型
					String date = elements3.get(2).text();
					Date cTime = DateUtils.StringToDate(date,
							"yyyy-MM-dd HH:mm:ss");// 通话开始时间
					String tradeTime = elements3.get(3).text(); // 通信时长
					String callWay = elements3.get(4).text(); // 呼叫类型
					String recevierPhone = elements3.get(5).text(); // 对方号码
					String tradeAddr = elements3.get(6).text(); // 通信地点
					BigDecimal basePay = new BigDecimal(elements3.get(7).text());// 基本费用
					BigDecimal longPay = new BigDecimal(elements3.get(8).text()); // 长途费用
					BigDecimal infoPay = new BigDecimal(elements3.get(9).text()); // 信息费用
					BigDecimal otherPay = new BigDecimal(elements3.get(10)
							.text()); // 其他费用
					BigDecimal allPay = new BigDecimal(elements3.get(11).text()); // 总费用

					int tradeTimeInt = new TimeUtil().timetoint(tradeTime);
					DianXinDetail dxDetail = new DianXinDetail();
					UUID uuid = UUID.randomUUID();
					dxDetail.setId(uuid.toString());
					dxDetail.setcTime(cTime);
					dxDetail.setTradeTime(tradeTimeInt);
					dxDetail.setTradeAddr(tradeAddr);
					dxDetail.setTradeType(tradeType);
					dxDetail.setCallWay(callWay);
					dxDetail.setRecevierPhone(recevierPhone);
					dxDetail.setBasePay(basePay);
					dxDetail.setLongPay(longPay);
					dxDetail.setInfoPay(infoPay);
					dxDetail.setOtherPay(otherPay);
					dxDetail.setAllPay(allPay);
					dxDetail.setPhone(phoneNo);
					detailList.add(dxDetail);
				} catch (Exception e) {
					logger.error("saveCallLog", e);
				}
			}
			/*
			 * if(!content.contains("下一页")){ break; }
			 */
			if (text.contains("下一页")) {
				if (isHistory == 1)
					requestCallLogService(page + 1, 1, d, dstr, isHistory);
				else
					requestThisMonthService(page + 1, t, "", "", isHistory);
			}
		}
	}

	private void requestSmsLogService(final int page, final int t,
			final Date d, final String dstr, final int isHistory) {
		getUrl("http://sh.189.cn/service/cnetQueryCondition.do?currentPage="
				+ page + "&deviceNo=" + phoneNo
				+ "&detailType=SMSC&menuid=4&flag=历史&devNo=" + phoneNo
				+ "&actionCode=query&selectType=历史详单" + "&queryDate=" + dstr
				+ "&subNo=null",
				"http://he.189.cn/group/bill/bill_billlist.do", null,
				new AbstractProcessorObserver(util, WaringConstaint.SHDX_6) {
					@Override
					public void afterRequest(SimpleObject context) {
						saveSmsLog(context, page, t, d, dstr, isHistory);
					}
				});
	}

	private void saveSmsLog(SimpleObject context, final int page, final int t,
			final Date d, final String dstr, final int isHistory) {
		String text = ContextUtil.getContent(context);
		Document doc = ContextUtil.getDocumentOfContent(context);
		System.out.println(doc.toString());
		if (text.indexOf("没有查找到相关数据") >= 0) {
			return;
		}
		String tableSort = InfoUtil.getInstance().getInfo("dx/sh", "tableSort");
		String tbody = InfoUtil.getInstance().getInfo("dx/sh", "tbody");
		String tr = InfoUtil.getInstance().getInfo("dx/sh", "tr");
		String td = InfoUtil.getInstance().getInfo("dx/sh", "td");
		Elements elements = doc.select(tableSort);
		if (elements != null && elements.size() > 0) {
			Elements elements2 = elements.first().select(tbody).first()
					.select(tr);
			for (int j = 0; j < elements2.size(); j++) {
				try {
					Elements tds = elements2.get(j).select(td);
					if (tds.size() == 5) {
						String RecevierPhone = tds.get(2).text().trim();// 对方号码
						String SentTime = tds.get(1).text().trim();// 发送时间
						String BusinessType = tds.get(3).text().trim();// 费用类型
						String AllPay = tds.get(4).text().trim();// 费用

						Date sentTime = null;
						try {
							sentTime = DateUtils.StringToDate(SentTime,
									"yyyy-MM-dd HH:mm:ss");
						} catch (Exception e) {
							e.printStackTrace();
						}
						TelcomMessage obj = new TelcomMessage();
						obj.setPhone(phoneNo);
						UUID uuid = UUID.randomUUID();
						obj.setId(uuid.toString());
						obj.setBusinessType(BusinessType);// 业务类型：点对点
						obj.setRecevierPhone(RecevierPhone);// 对方号码
						obj.setSentTime(sentTime);// 发送时间
						obj.setCreateTs(new Date());
						obj.setAllPay(Double.parseDouble(AllPay));// 总费用
						messageList.add(obj);
					}

				} catch (Exception e) {
					logger.error("saveSmsLog", e);
				}
			}
			if (text.contains("下一页")) {
				requestSmsLogService(page + 1, 1, d, dstr, isHistory);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		/*
		 * for(int i=0; i< 10; i++) { System.out.println(i + "=" + (int)
		 * (Math.random() * 1000 % 10)); } if (true) { return; }
		 */
		ShangHaiDianXin dx1 = new ShangHaiDianXin();
		dx1.requestService();

		boolean isTestSms = false;
		String phoneNo = "18016252553";
		String password = "026315";
		String imgCode = null;
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		ShangHaiDianXin dx = new ShangHaiDianXin(spider, null, phoneNo,
				password, "2345", null);
		dx.setTest(true);
		// DebugUtil.addToCookieStore("www.189.cn",
		// "JSESSIONID=F963B038D5489C15A308A64DBF9B89EC-n14;");
		// DebugUtil.addToCookieStore("uam.ct10000.com",
		// "JSESSIONID=0000NEi1uFD2wq0Uo1-ZYYb6e8_:18m6j7te1;");
		if (!isTestSms) {
			dx.checkVerifyCode(phoneNo);
			spider.start();
			dx.printData();
			imgCode = CUtil.inputYanzhengma();
			dx.setAuthCode(imgCode);
			dx.getData().clear();
			/*
			 * CookieStore cs = CookieStoreUtil.putContextToCookieStore(null,
			 * 1); DebugUtil.printCookieData(cs, "www.189.cn");
			 * DebugUtil.printCookieData(cs, "uam.ct10000.com");
			 */
			dx.goLoginReq();
			spider.start();
			dx.parseBalanceInfo();
			spider.start();
			// DebugUtil.findMissing(cs,
			// CookieStoreUtil.putContextToCookieStore(null, 1));
		} else {
			DebugUtil
					.addToCookieStore(
							"sh.189.cn",
							".ybtj.189.cn=590566E9B59ACF4CAED1A5D93A77C015;JSESSIONID=0000aBuIqoAkvVpCNMr0hbctakn:14horgrp7;JSESSIONID=000150uiRh29ZJYvZdV295TKBfy:-K00E9;NSC_xu-222.68.185.229=ffffffffc3a01f1645525d5f4f58455e445a4a423660;SSLJSESSION=0001SESSIONMANAGEMENTAFFINI:-GK5AF7;UAMTGC=TGT-137611-jNhbnSnCtbuL9IuJZg3hajXgqTygNY6gHblaN6Smn31BSFEriy-uam;citrix_ns_id=hDpt4EK0T/54cf3R2kdRUptXoqUA000;citrix_ns_id_.189.cn_%2F_wat=SlNFU1NJT05JRF9f?GJVU755YShTDws6Vq+cPIy2rUIsA#qbg2R/Rp9UAoyjTa8a72ZkzKpoIA&;citrix_ns_id_.189.cn_%2F_wlf=TlNDX3h1LTIyMi42OC4xODUuMjI5?yag49zJ2NzBf26sVRUcSiIKLM+kA&;cityCode=sh;isLogin=logined;userId=201|151669205;");
			dx.parseBalanceInfo();
			spider.start();

		}
		if (isTestSms) {
			// dx.parseBalanceInfo();
		}
		// dx.getUser() 个人信息
		// dx.getDetailList() 详单
		// dx.getTelList() 帐单
		if (isTestSms || dx.isSuccess()) {
			dx.showImgWhenSendSMS(phoneNo);
			spider.start();
			imgCode = CUtil.inputYanzhengma();
			dx.sendSmsPasswordForRequireCallLogService(imgCode);
			spider.start();
			if (dx.isSuccess()) {
				String smsCode = CUtil.inputYanzhengma();
				dx.requestAllService(smsCode, imgCode);
				spider.start();
			}
		}
		dx.printData();

	}
}
