package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
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
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;
import com.mysql.jdbc.StringUtils;

public class HeBeiDianXin extends AbstractDianXinCrawler {
	
	public HeBeiDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	public HeBeiDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		spider.getSite().setCharset("utf-8");
	}
	public HeBeiDianXin() {
		areaName = "河北";
		customField1 = "3";
		customField2 = "05";
		toStUrl = "&toStUrl=http://he.189.cn/UAMSSO/login_uam_sso.jsp?ReturnURL=http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=yecx";
		//toStUrl = "&toStUrl=http://he.189.cn/self_service/selectAssertionInvoke.action?ReturnURL=http://he.189.cn/group/bill/bill_balance.do";
		shopId = "10006";
	}
	//https://uam.ct10000.com/ct10000uam/validateImg.jsp
	public void checkVerifyCode(final String userName) {   
		saveVerifyCode("hebei", userName);
		//1.生成一个request
		//2.请求完成后的解析
		//3.加入到Spider的执行队列		
    }
	public void sendSmsPasswordForRequireCallLogService() {

		// http://he.189.cn/service/bill/action/ifr_bill_detailslist_new.jsp
		// http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1
		//
		// AREA_CODE 188
		// NUM 18033723291
		// PROD_NO 35942
		// ServiceKind 8
		// USER_ID 472326252
		// USER_NAME 石家庄世泽房地产经纪有限公司
		// String[][] pairs = {{"AREA_CODE", entity.getString("AREA_CODE")},
		// {"NUM", phoneNo},
		// {"PROD_NO", entity.getString("PROD_NO")},
		// {"ServiceKind", "8"},
		// {"USER_ID", entity.getString("USER_ID")},
		// {"USER_NAME", entity.getString("USER_NAME")}};
		//
		// postUrl("http://he.189.cn/service/bill/action/ifr_bill_detailslist_new.jsp",
		// "http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", pairs,
		// new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_1){
		// @Override
		// public void afterRequest(SimpleObject context) {
		// try {
		// String text = ContextUtil.getContent(context);
		// if (text != null && text.indexOf("获取短信验证码") >= 0) {
		// setStatus(STAT_SUC);
		// data.put("errMsg", "短信发送成功");
		// } else {
		// data.put("errMsg", "短信发送失败,请重试！");
		// }
		// notifyStatus();
		// } catch (Exception e) {
		// logger.error("error",e);
		// }
		// }
		// });
			
		getUrl("http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", null, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					Document doc = ContextUtil.getDocumentOfContent(context);
					String html = doc.toString();
					RegexPaserUtil rp = new RegexPaserUtil("doQuery\\('","'\\);",html,RegexPaserUtil.TEXTEGEXANDNRT);
					String result = rp.getText();
					String query[] = result.split(",");
					
					String areaCode = query[1].replaceAll("'", "").trim();
					
					// http://he.189.cn/public/postValidCode.jsp
					// Referer: http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1
					// NUM=18033723291&AREA_CODE=188&LOGIN_TYPE=21&OPER_TYPE=CR0&RAND_TYPE=002
					String[][] pairs = {{ "NUM", phoneNo }, 
							             { "AREA_CODE", areaCode },
							             { "LOGIN_TYPE", "21" },
							             { "OPER_TYPE", "CR0" },
							             { "RAND_TYPE", "002" }};

					postUrl("http://he.189.cn/public/postValidCode.jsp",
							"http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1",
							pairs, new AbstractProcessorObserver(util,
									WaringConstaint.HEBEIDX_1) {
								@Override
								public void afterRequest(SimpleObject context) {
									try {
										String text = ContextUtil.getContent(context);
										if (text != null && text.indexOf("随机短信验证码已失效") >= 0) {
											data.put("errMsg", "短信发送失败,请重试！");
										} else {
											setStatus(STAT_SUC);
											data.put("errMsg", "短信发送成功");
										}
										notifyStatus();
									} catch (Exception e) {
										logger.error("error", e);
									}
								}
							});
				}else{
					logger.error("获取区域编码出错！");
				}
			}
		});	
	}
	/*
	public void verifySmsCode() {
		Date d = new Date();
		String dstr = DateUtils.formatDate(d, "yyyyMM");
		String xml = "<buffalo-call><method>queryDetailBill</method><map><type>java.util.HashMap</type><string>PRODNUM</string><string>MVWCdY0vDvFeiJ7Pmdzsqg==</string><string>CITYCODE</string><string>0898</string><string>QRYDATE</string><string>" + dstr + "</string><string>TYPE</string><string>8</string><string>PRODUCTID</string><string>50</string><string>CODE</string><string>631627</string><string>USERID</string><string>10158069</string></map></buffalo-call>";
		postUrl("http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_7){
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
		if(ContextUtil.getContent(context).contains(phoneNo)){
			setStatus(STAT_SUC);
			notifyStatus();
			
			sendSmsPasswordForRequireCallLogService();
			
			parseBalanceInfo();
			List<String> months = DateUtils.getMonths(7, "yyyyMM");
			for (int i = months.size() - 1; i >= 0; i--) {
				String date = months.get(i);
				if(i == 0){
					requestMonthBillService(date, true, i);	
				}else {
					requestMonthBillService(date, false, i);
				}
			}
		}else{
			setStatus(STAT_STOPPED_FAIL);
			data.put("errMsg", "登录失败，请重试！");
			notifyStatus();
			return;
		}
	}
	
	public void requestAllService() {
		setStatus(STAT_SUC);
		notifyStatus();
//		checkMessage();
		requestService();
	}
	private void requestService() {
		
		Date d = new Date();
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			requestCallLogService(dstr);
		}
		
		List<String> months = DateUtils.getMonths(7, "yyyyMM");
		for (int i = months.size() - 1; i >= 0; i--) {
			String date = months.get(i);
			requestFlowLogService(1, date);
			if(i == 0){
				requestMonthBillService(date, true, i);	
			}else {
				requestMonthBillService(date, false, i);
			}
		}
		
	}
	
	//重写了Flow的抓取和分析
//	private void requestFlowLogService(final int page, final String date) {
//
//		// http://he.189.cn/queryFlowCdmaDataMsgListAction.action
//		// inventoryVo.accNbr=18033723291&inventoryVo.getFlag=3&inventoryVo.begDate=2014-10-01&inventoryVo.endDate=2014-10-31&inventoryVo.family=8&inventoryVo.accNbr97=&inventoryVo.productId=8&inventoryVo.acctName=18033723291&inventoryVo.feeDate=201410
//		String startTime = date.substring(0, 4) + "-" + date.substring(4, 6)
//				+ "-01";
//		String endTime = DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy-MM-dd");
//
//		String[][] pairs_flow = { { "inventoryVo.accNbr", phoneNo },
//				{ "inventoryVo.getFlag", "3" },
//				{ "inventoryVo.begDate", startTime },
//				{ "inventoryVo.endDate", endTime },
//				{ "inventoryVo.family", "8" }, { "inventoryVo.accNbr97", "" },
//				{ "inventoryVo.productId", "8" },
//				{ "inventoryVo.acctName", phoneNo },
//				{ "inventoryVo.feeDate", date } };
//
//		postUrl("http://he.189.cn/queryFlowCdmaDataMsgListAction.action",
//				"http://he.189.cn/group/bill/bill_billlist.do", pairs_flow,
//				new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_7) {
//					@Override
//					public void afterRequest(SimpleObject context) {
//						saveFlowLog(context, date);
//					}
//				});
//
//	}
//	protected void saveFlowLog(SimpleObject context, String date) {
//		if (context == null)
//			return;
//		JSONObject result0 = ContextUtil.getJsonOfContent(context);
//		UUID uuid = UUID.randomUUID();
//		String startTime = date.substring(0, 4) + "-" + date.substring(4, 6) + "-01";
//		String endTime = DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy-MM-dd");
//		Date queryMonth = DateUtils.StringToDate(DateUtils.getToday("yyyyMM"), "yyyyMM");
//		try {
//			if (result0 == null) {
//				// 流量账单
//				DianXinFlow dxFlow = new DianXinFlow();
//				dxFlow.setId(uuid.toString());
//				dxFlow.setPhone(phoneNo);
//				dxFlow.setDependCycle(startTime + "--" + endTime);
//				dxFlow.setQueryMonth(queryMonth);
//				dxFlow.setAllFlow(new BigDecimal(0));
//				dxFlow.setAllTime(new BigDecimal(0));
//				dxFlow.setAllPay(new BigDecimal(0.00));
//				addFlowBill(dxFlow);
//
//			} else {
//				String totalFlow = result0.getString("totalflow");
//				String totalTime = result0.getString("totaltime");
//				String totalFees = result0.getString("totalmoney");
//				// 流量账单
//				DianXinFlow dxFlow = new DianXinFlow();
//				dxFlow.setId(uuid.toString());
//				dxFlow.setPhone(phoneNo);
//				dxFlow.setDependCycle(startTime + "--" + endTime);
//				dxFlow.setQueryMonth(queryMonth);
//				dxFlow.setAllFlow(new BigDecimal(totalFlow));
//				dxFlow.setAllTime(new BigDecimal(totalTime));
//				dxFlow.setAllPay(new BigDecimal(totalFees));
//				addFlowBill(dxFlow);
//
//				JSONArray result3 = result0.getJSONArray("items");
//				if (result3.length() == 0)
//					return;
//				for (int j = 0; j < result3.length(); j++) {
//					// 流量详单
//					JSONObject item = result3.getJSONObject(j);
//
//					DianXinFlowDetail dxFd = new DianXinFlowDetail();
//					uuid = UUID.randomUUID();
//					dxFd.setId(uuid.toString());
//					dxFd.setPhone(phoneNo);
//
//					// "sparam0":"2014-10-16 09:04:59"
//					dxFd.setBeginTime(DateUtils.StringToDate(
//							item.getString("sparam0").trim(),
//							"yyyy-MM-dd HH:mm:ss"));// 开始时间
//					// "sparam3":"3G"
//					dxFd.setNetType(item.getString("sparam3").trim());// 通讯类型
//					// "sparam5":""
//					dxFd.setBusiness(item.getString("sparam5").trim());// 使用业务
//					// "sparam4":"北京"
//					dxFd.setLocation(item.getString("sparam4").trim());// 通信地点
//					// "sparam1":"644"
//					String time = item.getString("sparam1").trim();// 上网时长
//					dxFd.setTradeTime(new Long(time));//
//					// "sparam2":"36"
//					dxFd.setFlow(new BigDecimal(item.getString("sparam2")));// 总流量
//					// fparam4":0,"fparam3":0,"fparam2":0
//					dxFd.setFee(new BigDecimal(item.getString("fparam4").trim()));// 费用
//
//					addFlowDetail(dxFd);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//			return;
//		}
//
//	}
	
	
	private void checkMessage() {

		getUrl("http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1",
				null, new AbstractProcessorObserver(util,WaringConstaint.HEHDX_1) {
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil.getDocumentOfContent(context);
						String html = doc.toString();
						RegexPaserUtil rp = new RegexPaserUtil("doQuery\\('",
								"'\\);", html, RegexPaserUtil.TEXTEGEXANDNRT);
						String result = rp.getText();
						String query[] = result.split(",");

						String areaCode = query[1].replaceAll("'", "").trim();

						// http://he.189.cn/public/pwValid.jsp
						// ACC_NBR 18033723291
						// AREA_CODE 188
						// LOGIN_TYPE 21
						// MOBILE_CODE 035965
						// MOBILE_CODE 035965
						// MOBILE_FLAG
						// MOBILE_LOGON_NAME
						// _FUNC_ID_ WB_VALID_RANDPWD

						String[][] pairs = { { "ACC_NBR", phoneNo },
								{ "AREA_CODE", areaCode },
								{ "LOGIN_TYPE", "21" },
								{ "MOBILE_CODE", authCode },
								{ "MOBILE_CODE", authCode },
								{ "MOBILE_FLAG", "" },
								{ "MOBILE_LOGON_NAME", },
								{ "_FUNC_ID_", "WB_VALID_RANDPWD" } };

						postUrl("http://he.189.cn/public/pwValid.jsp",
								"http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1",
								pairs, new AbstractProcessorObserver(util,
										WaringConstaint.HEBEIDX_1) {
									@Override
									public void afterRequest(
											SimpleObject context) {
										if (context == null)
											return;
										Document doc = ContextUtil
												.getDocumentOfContent(context);
										String xml = doc.toString();
										if (xml != null
												&& !xml.contains("随机短信验证码已失效")) {
											requestService();
										}

									}
								});
					}
				});

	}
	private void requestFlowLogService(final int page, final String date) {

		// http://he.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp
		String startTime = date.substring(0, 4) + "-" + date.substring(4, 6)
				+ "-01 00:00:00";
		String endTime = DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy-MM-dd") + " 23:59:59";

		String[][] pairs_flow = { 
				{ "ACCT_DATE", date },
				{ "ACCT_DATE_1", DateUtils.formatDate(new Date(), "yyyyMM") },
				{ "ACC_NBR", phoneNo },
				{ "BEGIN_DATE", startTime },
				{ "CITY_CODE", "188" },
				{ "END_DATE", endTime },
				{ "FEE_DATE", date },
				{ "QRY_FLAG", "1" }, 
				{ "QUERY_TYPE", "3" },
				{ "QUERY_TYPE_NAME", "移动上网详单" },
				{ "SERVICE_KIND", "8" },
				{ "openFlag", "1" },
				{ "radioQryType", "on" } 
				};

		postUrl("http://he.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp",
				"http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", pairs_flow,
				new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_7) {
					@Override
					public void afterRequest(SimpleObject context) {
						saveFlowLog(context, date);
					}
				});

	}
	
	
	protected void saveFlowLog(SimpleObject context, String date) {
		if (context == null)
			return;

		Document doc = ContextUtil.getDocumentOfContent(context);
//		System.out.println(doc.toString());

		try {
			UUID uuid = UUID.randomUUID();
			String startTime = date.substring(0, 4) + "-"
					+ date.substring(4, 6) + "-01";
			String endTime = DateUtils.lastDayOfMonth(date, "yyyyMM",
					"yyyy-MM-dd");
			Date queryMonth = DateUtils.StringToDate(
					DateUtils.getToday("yyyyMM"), "yyyyMM");

			if (doc.toString().contains("您所查询的条件内没有相应的记录")) {
				// 流量账单
				DianXinFlow dxFlow = new DianXinFlow();
				dxFlow.setId(uuid.toString());
				dxFlow.setPhone(phoneNo);
				dxFlow.setDependCycle(startTime + "--" + endTime);
				dxFlow.setQueryMonth(queryMonth);
				dxFlow.setAllFlow(new BigDecimal(0));
				dxFlow.setAllTime(new BigDecimal(0));
				dxFlow.setAllPay(new BigDecimal(0.00));
				addFlowBill(dxFlow);
			} else {

				Element table = doc.select("table[id=details_table]").get(0);

				Element tr1 = table.select("tr").get(0);
				String totalFlow = tr1.select("td").get(1).text();
				totalFlow = totalFlow.replaceAll("总流量：", "").trim();
				totalFlow = totalFlow.replaceAll("MB", "M").replaceAll("GB", "G").replaceAll("KB", "K");

				Element tr2 = table.select("tr").get(1);
				String totalTime = tr2.select("td").get(0).text();
				totalTime = totalTime.replaceAll("总时长：", "").trim();
				String totalFees = tr2.select("td").get(1).text();
				totalFees = totalFees.replaceAll("总金额：", "").trim();
				if (totalFees.equals("")) {
					totalFees = "0";
				}

				// 流量账单
				DianXinFlow dxFlow = new DianXinFlow();
				dxFlow.setId(uuid.toString());
				dxFlow.setPhone(phoneNo);
				dxFlow.setDependCycle(startTime + "--" + endTime);
				dxFlow.setQueryMonth(queryMonth);
				dxFlow.setAllFlow(new BigDecimal(StringUtil
						.flowFormat(totalFlow)));
				dxFlow.setAllTime(new BigDecimal(StringUtil
						.flowTimeFormat(totalTime)));
				dxFlow.setAllPay(new BigDecimal(totalFees));
				addFlowBill(dxFlow);

				// 流量详单
				Elements trs = table.select("tr");
				if (trs.size() > 3) {
					for (int i = 3; i < trs.size(); i++) {
						Elements tds = trs.get(i).select("td");
						if (tds.size() == 8) {
							DianXinFlowDetail dxFd = new DianXinFlowDetail();
							uuid = UUID.randomUUID();
							dxFd.setId(uuid.toString());
							dxFd.setPhone(phoneNo);
							// 开始时间
							String beginTime = tds.get(1).text().trim();
							if (!beginTime.equals("")) {
								// 2014-10-16 17:33:24
								dxFd.setBeginTime(DateUtils.StringToDate(
										beginTime, "yyyy-MM-dd HH:mm:ss"));
							}

							// 上网时长
							String time = tds.get(2).text().trim();
							// 总流量
							String flow = tds.get(3).text().trim();
							flow = flow.replaceAll("MB", "M").replaceAll("GB", "G").replaceAll("KB", "K");
							// 通讯类型
							String netType = tds.get(4).text().trim();
							// 通信地点
							String location = tds.get(5).text().trim();
							// 使用业务
							String business = tds.get(6).text().trim();
							// 费用
							String fee = tds.get(7).text().trim();
							if (fee.equals("")) {
								fee = "0";
							}

							dxFd.setTradeTime(StringUtil.flowTimeFormat(time));//
							dxFd.setFlow(new BigDecimal(StringUtil
									.flowFormat(flow)));// 总流量
							dxFd.setNetType(netType);// 通讯类型
							dxFd.setLocation(location);// 通信地点
							dxFd.setBusiness(business);// 使用业务
							dxFd.setFee(new BigDecimal(fee));// 费用
							addFlowDetail(dxFd);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("error", e);
			return;
		}

	}
	
	private void parseBalanceInfo() {
		//http://he.189.cn/service/manage/index.jsp
		getUrl("http://he.189.cn/service/manage/index.jsp", "http://www.189.cn/he/", null, new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_7){
			@Override
			public void afterRequest(SimpleObject context) {
				if(context == null)
					return;
				try {
					
					Document doc = ContextUtil.getDocumentOfContent(context);
					Element table = doc.select("div[class=reg_tab01]").get(0).select("table").get(0);
					
					user.setRealName(table.select("tr").get(0).select("td").get(0).text());
					user.setCardNo(table.select("tr").get(5).select("td").get(0).text());
					user.setCardType(table.select("tr").get(4).select("td").get(0).text());
					user.setAddr(doc.select("span[id=span_RelaAddress]").get(0).text());
					
				} catch (Exception e) {
					logger.error("error",e);
				}
			}
		});
		
		
	}
	
	
//	private void requestMonthBillService() {
//		postUrl("http://he.189.cn/chargeQuery/chargeQuery_queryMonthBill.action?months=6&queryKind=2&productType=8", "http://he.189.cn/group/bill/bill_detail.do", null, new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_7){
//			@Override
//			public void afterRequest(SimpleObject context) {
//				try {
//					JSONArray json = ContextUtil.getJsonArrayOfContent(context); 
//					if (json == null) {
//						return;
//					}
//					int len = json.length();
//					for(int i = 0; i < len; i++) {
//						JSONObject jobj = json.getJSONObject(i);
//						DianXinTel tel = new DianXinTel();
//						tel.setTeleno(phoneNo);
//						getTelList().add(tel);
//						tel.setcTime(DateUtils.StringToDate(jobj.getString("billMonthStr"), "yyyy年MM月"));
//						String n = jobj.getString("bill");
//						BigDecimal b1 = new BigDecimal(n.length() == 0 ? "0" : n);
//						tel.setcAllPay(b1);
//					}
//				} catch (Exception e) {
//					logger.error("error",e);
//				}
//			}
//		});
//		
//	}
	
	private void requestMonthBillService(final String date, boolean current,final int i) {
		
		if(current == true){
			//当月账单
			getUrl("http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1",
					null, new AbstractProcessorObserver(util,
							WaringConstaint.HEHDX_1) {
						@Override
						public void afterRequest(SimpleObject context) {
							Document doc = ContextUtil
									.getDocumentOfContent(context);
							String html = doc.toString();
							RegexPaserUtil rp = new RegexPaserUtil("doQuery\\('",
									"'\\);", html, RegexPaserUtil.TEXTEGEXANDNRT);
							String result = rp.getText();
							String query[] = result.split(",");

							String areaCode = query[1].replaceAll("'", "").trim();
							String prod_no = query[2].replaceAll("'", "").trim();
							String user_name = query[3].replaceAll("'", "").trim();
							String user_id = query[5].replaceAll("'", "").trim();

							// http://he.189.cn/service/bill/action/ifr_rt.jsp
							// AREA_CODE 188
							// NUM 18033723291
							// PROD_NO 35942
							// ServiceKind 8
							// USER_ID 472326252
							// USER_NAME 石家庄世泽房地产经纪有限公司

							String[][] pairs = {{ "AREA_CODE", areaCode },
												{ "NUM ", phoneNo },
												{ "PROD_NO", prod_no },
												{ "ServiceKind", "8" },
												{ "MOBILE_FLAG", "" },
												{ "USER_ID", user_id},
												{ "USER_NAME", user_name } };

							postUrl("http://he.189.cn/service/bill/action/ifr_rt.jsp",
									"http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=sshf",
									pairs, new AbstractProcessorObserver(util,
											WaringConstaint.HEBEIDX_1) {
										@Override
										public void afterRequest(
												SimpleObject context) {
											if (context == null)
												return;
											Document doc = ContextUtil
													.getDocumentOfContent(context);
											String fee = doc.select("span[class=sum]").text();
											fee = fee.replaceAll("元", "").trim();
											if(fee.equals("")){
												fee = "0";
											}
											
											DianXinTel tel = new DianXinTel();
											UUID uuid = UUID.randomUUID();
											tel.setId(uuid.toString());
											tel.setcTime(DateUtils.StringToDate(date, "yyyyMM"));
											tel.setTeleno(phoneNo);
											tel.setDependCycle(date.substring(0, 4) + "/" + date.substring(4, 6) + "/01-" + DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
											tel.setcAllPay(new BigDecimal(fee));
											addMonthBill(tel);
										}
									});
						}
					});
			
			
		} else {
			//历史账单
			// http://he.189.cn/service/bill/action/bill_month_list_detail.jsp
			// http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001
			// ACC_NBR 18033723291
			// SERVICE_KIND 8
			// feeDate 201409
			
			String [][] pairs = {{"ACC_NBR", phoneNo},{"SERVICE_KIND", "8"},{"feeDate", date}};

			postUrl("http://he.189.cn/service/bill/action/bill_month_list_detail.jsp", "http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", pairs, new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_2){
				@Override
				public void afterRequest(SimpleObject context) {
					if(context == null)
						return;
					try {
						
						Document doc = ContextUtil.getDocumentOfContent(context);
						String html = doc.toString();
						
						DianXinTel tel = new DianXinTel();
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
						tel.setcTime(DateUtils.StringToDate(date, "yyyyMM"));
						tel.setTeleno(phoneNo);
						tel.setDependCycle(date.substring(0, 4) + "/" + date.substring(4, 6) + "/01-" + DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy/MM/dd"));
						if(html.contains("本期费用合计")){
							RegexPaserUtil rp = new RegexPaserUtil("本期费用合计：","</div",html,RegexPaserUtil.TEXTEGEXANDNRT);
							String result = rp.getText().replaceAll("元", "").trim();
							if(result.equals("")){
								tel.setcAllPay(new BigDecimal(0));
							} else {
								tel.setcAllPay(new BigDecimal(result));
							}
						} else {
							tel.setcAllPay(new BigDecimal(0));
						}
						/*if(i == 0){
							Element table = doc.select("table").get(1);
							Element tr = table.select("tr").get(1);
							Element td = table.select("td").get(1);
							String fee = td.text().replaceAll("元", "").trim();
							if(fee.equals("")){
								fee = "0";
							}
							user.setPhoneRemain(new BigDecimal(fee));
						}*/
							
						addMonthBill(tel);
						
						
					} catch (Exception e) {
						logger.error("error",e);
					}
				}
			});
		}
	}
	
	private void requestCallLogService(final String date) {
		String startTime = date.substring(0, 4) + "-" + date.substring(4, 6)
				+ "-01 00:00:00";
		String endTime = DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy-MM-dd") + " 23:59:59";

		String[][] pairs_call = { 
				{ "ACCT_DATE", date },
				{ "ACCT_DATE_1", DateUtils.formatDate(new Date(), "yyyyMM") },
				{ "ACC_NBR", phoneNo },
				{ "BEGIN_DATE", startTime },
				{ "CITY_CODE", "188" },
				{ "END_DATE", endTime },
				{ "FEE_DATE", date },
				{ "QRY_FLAG", "1" }, 
				{ "QUERY_TYPE", "1" },
				{ "QUERY_TYPE_NAME", "移动语音详单" },
				{ "SERVICE_KIND", "8" },
				{ "openFlag", "1" },
				{ "radioQryType", "on" } 
				};

		postUrl("http://he.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp",
				"http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", pairs_call,
				new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_4) {
					@Override
					public void afterRequest(SimpleObject context) {
						saveCallLog(context, date);
					}
				});
		
		
		String[][] pairs_mess = { 
				{ "ACCT_DATE", date },
				{ "ACCT_DATE_1", DateUtils.formatDate(new Date(), "yyyyMM") },
				{ "ACC_NBR", phoneNo },
				{ "BEGIN_DATE", startTime },
				{ "CITY_CODE", "188" },
				{ "END_DATE", endTime },
				{ "FEE_DATE", date },
				{ "QRY_FLAG", "1" }, 
				{ "QUERY_TYPE", "2" },
				{ "QUERY_TYPE_NAME", "移动短信详单" },
				{ "SERVICE_KIND", "8" },
				{ "openFlag", "1" },
				{ "radioQryType", "on" } 
				};
		postUrl("http://he.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp",
				"http://he.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", pairs_mess,
				new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_5) {
					@Override
					public void afterRequest(SimpleObject context) {
						saveSmsLog(context, date);
					}
				});
	}
	
//	private void requestCallLogService(final int page, final int t, final Date d, final String dstr) {
//		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
//		String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
//		String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
//		String[][] pairs = new String[][] {{"inventoryVo.accNbr", phoneNo}, {"inventoryVo.getFlag", "3"}, {"inventoryVo.begDate", bd}, {"inventoryVo.endDate", ed}, {"inventoryVo.family", "8"}, 
//				{"inventoryVo.accNbr97", ""}, {"inventoryVo.productId", "8"}, {"inventoryVo.acctName", phoneNo}, {"inventoryVo.feeDate", dstr}};
//		postUrl("http://he.189.cn/queryVoiceMsgAction.action", "http://he.189.cn/group/bill/bill_billlist.do", pairs, new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_6) {
//			@Override
//			public void afterRequest(SimpleObject context) {
//				saveCallLog(context, dstr);
//			}
//		});
//		
//		//created by qian
//		//默认查询用户的10000条短消息记录
//		String[][] pairs2 = new String[][] {{"inventoryVo.accNbr", phoneNo}, {"inventoryVo.getFlag", "3"}, {"inventoryVo.begDate", bd}, {"inventoryVo.endDate", ed}, {"inventoryVo.family", "8"}, 
//				{"inventoryVo.accNbr97", ""}, {"inventoryVo.productId", "8"}, {"inventoryVo.acctName", phoneNo}, {"inventoryVo.feeDate", dstr}};
//		postUrl("http://he.189.cn/mobileInventoryAction.action", "http://he.189.cn/group/bill/bill_billlist.do", pairs2, new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_7) {
//			@Override
//			public void afterRequest(SimpleObject context) {
//				saveSmsLog(context, phoneNo);
//			}
//		});
//		//created by jiangzongren
//		/*String[][] pairs3 = new String[][] {{"inventoryVo.accNbr", phoneNo}, {"inventoryVo.getFlag", "3"}, {"inventoryVo.begDate", bd}, {"inventoryVo.endDate", ed}, {"inventoryVo.family", "8"}, 
//				{"inventoryVo.accNbr97", ""}, {"inventoryVo.productId", "8"}, {"inventoryVo.acctName", phoneNo}, {"inventoryVo.feeDate", dstr}};
//		postUrl("http://he.189.cn/mobileInventoryAction.action", "http://he.189.cn/group/bill/bill_billlist.do", pairs3, new AbstractProcessorObserver(util, WaringConstaint.HEBEIDX_7) {
//			@Override
//			public void afterRequest(SimpleObject context) {
//				saveOnlineFlow(context, phoneNo);
//			}
//		});*/
//					
//	}
	

//		private void saveSmsLog(SimpleObject context, String phoneNo) {
//			try {				
//				JSONObject obj = ContextUtil.getJsonOfContent(context);
//		
//				if (obj == null) {
//					return;
//				}
//				//int count = obj.getInt("Count"); 	
//				//if (count < 1) {return;}
//				if (obj.isNull("items")) {return;}
//				JSONArray arr = obj.getJSONArray("items");
//				if (arr == null || arr.length() < 1) {return;}
//				int len = arr.length();
//				for(int i = 0; i < len; i++) {
//					JSONObject obj1 = arr.getJSONObject(i);
//					TelcomMessage telcomMessage = new TelcomMessage();
//					UUID uuid = UUID.randomUUID();
//					telcomMessage.setId(uuid.toString());
//					telcomMessage.setAllPay(obj1.getDouble("monthFee"));
//					telcomMessage.setSentTime(DateUtils.StringToDate(obj1.getString("beginDate"), "yyyy-MM-dd HH:mm:ss"));
//					telcomMessage.setRecevierPhone(obj1.getString("callPhone"));
//					telcomMessage.setBusinessType(obj1.getString("spName"));				
//					telcomMessage.setPhone(phoneNo);
//					telcomMessage.setCreateTs(new Date());
//					
//					addMessage(telcomMessage);
////					messageList.add(telcomMessage);
//				}
//
//			} catch (JSONException e) {
//				logger.error("error",e);
//			}
//		}
	
	
	private void saveSmsLog(SimpleObject context, String date) {
		if (context == null)
			return;
		try {
			Document doc = ContextUtil.getDocumentOfContent(context);
			String html = doc.toString();
			if(!html.contains("没有相应的记录")){
				Element table = doc.select("table[id=details_table]").get(0);
				Elements trs = table.select("tr");
				if(trs.size() > 1){
					for(int i = 1; i < trs.size(); i++){
						Elements tds = trs.get(i).select("td");
						if(tds.size() > 8){
							
							TelcomMessage telcomMessage = new TelcomMessage();
							UUID uuid = UUID.randomUUID();
							telcomMessage.setId(uuid.toString());
							
							telcomMessage.setAllPay(Double.parseDouble(tds.get(8).text().trim()));
							telcomMessage.setSentTime(DateUtils.StringToDate(tds.get(3).text().trim(), "yyyy-MM-dd HH:mm:ss"));
							telcomMessage.setRecevierPhone(tds.get(1).text().trim());
							telcomMessage.setBusinessType(tds.get(2).text().trim());				
							telcomMessage.setPhone(phoneNo);
							telcomMessage.setCreateTs(new Date());
							
							addMessage(telcomMessage);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("error",e);
		}

	}
	
	private void saveCallLog(SimpleObject context, final String dstr) {
		if (context == null)
			return;
		try {
			Document doc = ContextUtil.getDocumentOfContent(context);
			String html = doc.toString();
			if(!html.contains("没有相应的记录")){
				Element table = doc.select("table[id=details_table]").get(0);
				Elements trs = table.select("tr");
				if(trs.size() > 1){
					for(int i = 1; i < trs.size(); i++){
						Elements tds = trs.get(i).select("td");
						if(tds.size() > 10){
							
							DianXinDetail dxDetail = new DianXinDetail();
							UUID uuid = UUID.randomUUID();
							dxDetail.setId(uuid.toString());
							dxDetail.setPhone(phoneNo);

							dxDetail.setRecevierPhone(tds.get(1).text().trim());
							dxDetail.setCallWay(tds.get(2).text().trim());
							dxDetail.setcTime(DateUtils.StringToDate(tds.get(3).text().trim(), "yyyy-MM-dd HH:mm:ss"));
							dxDetail.setTradeTime(Integer.parseInt(tds.get(4).text().trim()));
							dxDetail.setBasePay(new BigDecimal(tds.get(5).text().trim()));
							dxDetail.setLongPay(new BigDecimal(tds.get(6).text().trim()));
							dxDetail.setOtherPay(new BigDecimal(tds.get(8).text().trim()));
							dxDetail.setAllPay(new BigDecimal(tds.get(10).text().trim()));
		
							addDetail(dxDetail);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("error",e);
		}

	}

	public static void main(String[] args) throws Exception {
		/*for(int i=0; i< 10; i++) {
			System.out.println(i + "=" + (int) (Math.random() * 1000 % 10));
		}
		if (true) {
			return;
		}*/
		String phoneNo = "18033723291";
		String password = "199034";
		
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		HeBeiDianXin dx = new HeBeiDianXin(spider, null, phoneNo, password, "2345", null);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		dx.printData();
		

	}
}
