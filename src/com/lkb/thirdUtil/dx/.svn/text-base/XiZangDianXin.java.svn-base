package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
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
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

public class XiZangDianXin extends AbstractDianXinCrawler {

	public XiZangDianXin(Spider spider, User user, String phoneNo,
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
		spider.getSite().setCharset("utf-8");
	}

	public XiZangDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;
		this.util = util;
		// spider.getSite().setCharset("gbk");
	}

	public XiZangDianXin() {
		areaName = "西藏";
		customField1 = "3";
		customField2 = "26";
		// http://www.189.cn/dqmh/login/loginJT.jsp?UserUrlto=/dqmh/frontLink.do?method=linkTo&shopId=10031&toStUrl=http://xj.189.cn/service/account/index.jsp
		// http://www.189.cn/dqmh/frontLink.do?method=linkTo&shopId=10031&toStUrl=http://xj.189.cn:80/common/login.jsp
		toStUrl = "&toStUrl=http://xz.189.cn/service/account/init.action";
		shopId = "10026";
	}

	// https://uam.ct10000.com/ct10000uam/validateImg.jsp
	public void checkVerifyCode(final String userName) {
		saveVerifyCode("xizang", userName);
		// 1.生成一个request
		// 2.请求完成后的解析
		// 3.加入到Spider的执行队列
	}

	/*
	 * public void sendSmsPasswordForRequireCallLogService() {
	 * postUrl("http://he.189.cn/queryCheckSecondPwdAction.action",
	 * "	http://he.189.cn/group/bill/bill_billlist.do", new String[][]
	 * {{"inventoryVo.accNbr", phoneNo}, {"inventoryVo.productId", "8"}}, new
	 * AbstractProcessorObserver(util, WaringConstaint.XZDX_4){
	 * 
	 * @Override public void afterRequest(SimpleObject context) { try { String
	 * text = ContextUtil.getContent(context); if (text != null &&
	 * text.indexOf("请输入验证码") >= 0) { setStatus(STAT_SUC); } } catch (Exception
	 * e) { e.printStackTrace(); } } });
	 * 
	 * }
	 */
	/*
	 * public void verifySmsCode() { Date d = new Date(); String dstr =
	 * DateUtils.formatDate(d, "yyyyMM"); String xml =
	 * "<buffalo-call><method>queryDetailBill</method><map><type>java.util.HashMap</type><string>PRODNUM</string><string>MVWCdY0vDvFeiJ7Pmdzsqg==</string><string>CITYCODE</string><string>0898</string><string>QRYDATE</string><string>"
	 * + dstr +
	 * "</string><string>TYPE</string><string>8</string><string>PRODUCTID</string><string>50</string><string>CODE</string><string>631627</string><string>USERID</string><string>10158069</string></map></buffalo-call>"
	 * ; postUrl("http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service",
	 * "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new
	 * String[]{null, xml}, null, new AbstractProcessorObserver(util,
	 * WaringConstaint.LNYD_7){
	 * 
	 * @Override public void afterRequest(SimpleObject context) { try { Document
	 * doc = ContextUtil.getDocumentOfContent(context); Elements rect =
	 * doc.select("string"); String ac = rect.get(rect.size() - 1).text();
	 * requestAllService(); } catch (Exception e) { e.printStackTrace(); } } });
	 * 
	 * }
	 */
	// https://uam.ct10000.com/ct10000uam-gate/SSOFromUAM?ReturnURL=687474703A2F2F686E2E3138392E636E3A38302F686E73656C66736572766963652F75616D6C6F67696E2F75616D2D6C6F67696E2175616D4C6F67696E5265742E616374696F6E3F7255726C3D2F686E73656C66736572766963652F62696C6C71756572792F62696C6C2D71756572792173686F77546162732E616374696F6E3F746162496E6465783D33&ProvinceId=19

	protected void onCompleteLogin(SimpleObject context) {
		// sendSmsPasswordForRequireCallLogService();
		// logger.info(ContextUtil.getRequest(context).getUrl());
		// parseBalanceInfo();
		if(ContextUtil.getContent(context).contains("可用余额")){
			setStatus(STAT_LOGIN_SUC);
			notifyStatus();
			getUuid();
			requestService();
		}else{
			setStatus(STAT_STOPPED_FAIL);
			data.put("errMsg", "登录失败，请重试！");
			notifyStatus();
			return;
		}
	}

	public void requestAllService() {
		requestService();
	}

	private void requestService() {
		parseBalanceInfo();
		requestMonthBillService();
		Date d = new Date();
		for (int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			requestCallLogService(cd, 1);
			requestMessageLogService(cd, 1);
			requestFlow(cd, 1);
		}
	}

	private void parseBalanceInfo() {
		getUrl("http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo",
				"http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo",
				null, new AbstractProcessorObserver(util,
						WaringConstaint.XZDX_1) {
					// 2.请求完成后的解析
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil
								.getDocumentOfContent(context);
						// //System.out.println(doc);
						try {
							Elements trs = doc.select("form#personalInfo")
									.select("table").select("tr");
							if (trs != null && !trs.toString().equals("")) {
								user.setRealName(trs.select(
										"input[name=realName]").val());// 客户名称
								user.setIdcard(trs.select(
										"input[name=certificateNumber]").val());// 证件号码
								user.setEmail(trs.select("input[name=email]")
										.val());// 邮箱
								// String username =
								// trs.select("td").get(1).text();
								user.setUserName(phoneNo);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		getUrl("http://xz.189.cn/service/account/init.action", null,
				new AbstractProcessorObserver(util, WaringConstaint.XZDX_2) {
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil
								.getDocumentOfContent(context);
						try {
							String accountId = doc.select(
									"input[name=accountId]").val();
							if (accountId != null && !accountId.equals("")) {
								// System.out.println(accountId);
								data.put("accountId", accountId);
								getUrl("http://xz.189.cn/service/bill/feeQuery!getRestoInfo.action?accountId="
										+ accountId
										+ "&type=welcomePage&_="
										+ System.currentTimeMillis(),
										"http://xz.189.cn/service/account/init.action",
										new AbstractProcessorObserver(util,
												WaringConstaint.XZDX_2) {
											@Override
											public void afterRequest(
													SimpleObject context) {
												JSONObject doc = ContextUtil
														.getJsonOfContent(context);
												try {
													String trs = doc.get(
															"balance")
															.toString();
													if (trs != null
															&& !trs.equals("")) {
														// System.out.println(trs);
														BigDecimal balance = new BigDecimal(
																trs);
														user.setPhoneRemain(balance);
													}
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										});
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		// 获取rsa加密
		
		/* getUrl("http://xz.189.cn/service/bill/resto.action?rnd="+Math.random(), "http://xz.189.cn/service/bill/fee.action?type=resto", null, new AbstractProcessorObserver(util, WaringConstaint.XZDX_2){
		  
		 @Override public void afterRequest(SimpleObject context) { String
		  text = ContextUtil.getContent(context); if (text == null) { return; }
		 String n = StringUtil.subStr("function bodyRSA()", "}", text); n =
		  n.trim(); int f =
		  NumberUtils.toInt(StringUtil.subStr("setMaxDigits(", ");", n).trim(),
		  130); String[] stra = StringUtil.subStr("return new RSAKeyPair(",
		  ");", n).replaceAll("\"", "").trim().split(","); //pwd, digit, f, s
		  String epass = executeJsFunc("rsa/an_hui_dx_rsa.js", "exeEncry",
		  password, f, stra[0], stra[1], stra[2]); if (epass == null) { return;
		  }
		  getUrl("http://xz.189.cn/service/bill/feeQuery!getRestoInfo.action?uuid="
		  +epass+"&_="+System.currentTimeMillis(),
		  "http://xz.189.cn/service/bill/fee.action?type=resto", null, new
		  AbstractProcessorObserver(util, WaringConstaint.XZDX_2){
		  
		  @Override public void afterRequest(SimpleObject context) { JSONObject
		  doc = ContextUtil.getJsonOfContent(context); try { String trs =
		  doc.get("balance").toString(); if (trs!=null&&!trs.equals("")) {
		  //System.out.println(trs); } } catch (Exception e) {
		  e.printStackTrace(); } } }); } });*/
		 

	}

	private void requestMonthBillService() {
		getUrl("http://xz.189.cn/service/bill/initQueryBill.action?rnd="
				+ Math.random(),
				"http://xz.189.cn/service/bill/fee.action?type=bill", null,
				new AbstractProcessorObserver(util, WaringConstaint.XZDX_3) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							Document doc = ContextUtil
									.getDocumentOfContent(context);
							// System.out.println(doc);
							if (doc == null || doc.toString().equals("")) {
								return;
							}
							Elements trs = doc.select("tbody");
							if (trs == null || trs.toString().equals("")) {
								return;
							}
							for (int i = 0; i < trs.size(); i++) {
								Elements tds = trs.get(i).select("td");
								DianXinTel tel = new DianXinTel();
								tel.setcTime(DateUtils.StringToDate(tds.get(0)
										.text(), "yyyyMM"));
								String pay = tds.get(1).text().trim();
								BigDecimal b1 = new BigDecimal(
										pay.length() == 0 ? "0" : pay);
								tel.setcAllPay(b1);
								tel.setTeleno(phoneNo);
								telList.add(tel);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}
	
	private void requestFlow(final Date d, final int page){
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		final String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
		final String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		
		String url = "http://xz.189.cn/service/bill/queryDetail.action";
		String referer = "http://xz.189.cn/service/bill/fee.action?type=allDetails";
		String[][] pairs = {{"currentPage", String.valueOf(page)}, {"pageSize", "10"}, {"effDate", bd}, {"expDate", ed}, {"uuid", entity.getString("xizang_rsa_uuid")}, {"operListId", "undefined"}, {"qry_type", "4"}};
		
		postUrl(url, referer, pairs, new AbstractProcessorObserver(util, WaringConstaint.XZDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				try {

					if(ContextUtil.getContent(context) != null){
						Document doc = ContextUtil.getDocumentOfContent(context);
						Elements tables = doc.getElementsByTag("table");
						if(tables.size()>0){
							/*
							 * 流量账单
							 */

								Element flowTable = tables.get(0);
								String flowText = flowTable.text();
								String allFlow = StringUtil.subStr("总流量：", "总时长", flowText).trim();
								String allTime = StringUtil.subStr("总时长：", "总费用", flowText).trim();
								String allPay = StringUtil.subStr("总费用：", "(元)", flowText).trim();
								
								DianXinFlow dxf = new DianXinFlow();
								dxf.setAllFlow(new BigDecimal(StringUtil.flowFormat(allFlow)));
								dxf.setAllTime(new BigDecimal(StringUtil.flowTimeFormat(allTime)));
								dxf.setAllPay(new BigDecimal(allPay));
								dxf.setDependCycle(bd +"-"+ ed);
								dxf.setQueryMonth(DateUtils.StringToDate(bd, "yyyy-MM-dd"));
								dxf.setPhone(phoneNo);
								
								flowList.add(dxf);
								/*
								 * 流量详单
								 */
								Element flowDetailTable = tables.get(1);
								Elements tbodies = flowDetailTable.getElementsByTag("tbody");
								if(tbodies.size()>0){
									Elements trs = tbodies.get(0).getElementsByTag("tr");
									for (Element tr : trs) {
										DianXinFlowDetail dxfd = new DianXinFlowDetail();
										Elements tds = tr.getElementsByTag("td");
										if(tds.size()>0){
											for(int i=0; i<tds.size(); i++){
												String text = tds.get(i).text().trim();
												switch (i) {
												case 0:
													break;
												case 1:
													dxfd.setBeginTime(DateUtils.StringToDate(text, "yyyy-MM-dd hh:mm:ss"));
													break;
												case 2:
													dxfd.setTradeTime(StringUtil.flowTimeFormat(text).longValue());
													break;
												case 3:
													dxfd.setFlow(new BigDecimal(StringUtil.flowFormat(text)));
													break;
												case 4:
													dxfd.setNetType(text);
													break;
												case 5:
													dxfd.setLocation(text);
													break;
												case 6:
													dxfd.setBusiness(text);
													break;
												case 7:
													dxfd.setFee(new BigDecimal(text));
													break;
												}
											}
											dxfd.setPhone(phoneNo);
										}
										flowDetailList.add(dxfd);
									}
									getFlowDetailNextPage(d, bd, ed, page+1);
								}
						}
					}
				} catch (Exception e) {
					logger.error("西藏电信流量抓取出错！", e);
				}
			}
		});
	}
	
	/**
	* <p>Title: getFlowDetailNextPage</p>
	* <p>Description: 流量详单翻页</p>
	* @author Jerry Sun
	* @param d
	* @param bd
	* @param ed
	* @param page
	*/
	private void getFlowDetailNextPage(final Date d, final String bd, final String ed, final int page){
		String url = "http://xz.189.cn/service/bill/queryDetail.action";
		String referer = "http://xz.189.cn/service/bill/fee.action?type=allDetails";
		String[][] pairs = {{"currentPage", String.valueOf(page)}, {"pageSize", "10"}, {"effDate", bd}, {"expDate", ed}, {"uuid", entity.getString("xizang_rsa_uuid")}, {"operListId", "undefined"}};
		
		postUrl(url, referer, pairs, new AbstractProcessorObserver(util, WaringConstaint.XZDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				try {

					if(ContextUtil.getContent(context) != null){
						Document doc = ContextUtil.getDocumentOfContent(context);
						Elements tables = doc.getElementsByTag("table");
						if(tables.size()>0){
								Element flowDetailTable = tables.get(1);
								Elements tbodies = flowDetailTable.getElementsByTag("tbody");
								if(tbodies.size()>0){
									Elements trs = tbodies.get(0).getElementsByTag("tr");
									for (Element tr : trs) {
										Elements tds = tr.getElementsByTag("td");
										if(tds.size()>0){
											DianXinFlowDetail dxfd = new DianXinFlowDetail();
											for(int i=0; i<tds.size(); i++){
												String text = tds.get(i).text().trim();
												switch (i) {
												case 0:
													break;
												case 1:
													dxfd.setBeginTime(DateUtils.StringToDate(text, "yyyy-MM-dd hh:mm:ss"));
													break;
												case 2:
													dxfd.setTradeTime(StringUtil.flowTimeFormat(text).longValue());
													break;
												case 3:
													dxfd.setFlow(new BigDecimal(StringUtil.flowFormat(text)));
													break;
												case 4:
													dxfd.setNetType(text);
													break;
												case 5:
													dxfd.setLocation(text);
													break;
												case 6:
													dxfd.setBusiness(text);
													break;
												case 7:
													dxfd.setFee(new BigDecimal(text));
													break;
												}
											}
											dxfd.setPhone(phoneNo);
											
											flowDetailList.add(dxfd);
										}
								}
									getFlowDetailNextPage(d, bd, ed, page+1);
							}
						}
					}
				} catch (Exception e) {
					logger.error("西藏电信流量抓取翻页出错！", e);
				}
			}
		});
	}
	
	/**
	* <p>Title: getUuid</p>
	* <p>Description: 通过rsa加密算出西藏电信查询参数中的uuid的值</p>
	* @author Jerry Sun
	*/
	private void getUuid(){
		getUrl("http://xz.189.cn/service/bill/resto.action?rnd="+Math.random(), "http://xz.189.cn/service/bill/fee.action?type=resto", null, new AbstractProcessorObserver(util, WaringConstaint.XZDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					/*
					 * 获取rsaKey
					 */
					final String text = ContextUtil.getContent(context);
					 if (text == null)
						 return;
					 if(text.contains("location.replace")){
						 String url = StringUtil.subStr("location.replace('", "');</script>", text);
						 getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.XZDX_1) {
							 @Override
							public void afterRequest(SimpleObject context) {
								 String content = ContextUtil.getContent(context);
								 String n = StringUtil.subStr("function bodyRSA()", "}", content);
								 n = n.trim();
								 int f = NumberUtils.toInt(StringUtil.subStr("setMaxDigits(", ");", n).trim(), 130);
								 String[] stra = StringUtil.subStr("return new RSAKeyPair(", ");", n).replaceAll("\"", "").trim().split(","); //pwd, digit, f, s
								 String epass = executeJsFunc("rsa/an_hui_dx_rsa.js", "exeEncry", "serviceNbr=" + phoneNo, f, stra[0], stra[1], stra[2]);
								 if (epass == null) 
									 return;

								 entity.put("xizang_rsa_uuid", epass);
							}
						});
					 }else{
						 String n = StringUtil.subStr("function bodyRSA()", "}", text);
						 n = n.trim();
						 int f = NumberUtils.toInt(StringUtil.subStr("setMaxDigits(", ");", n).trim(), 130);
						 String[] stra = StringUtil.subStr("return new RSAKeyPair(", ");", n).replaceAll("\"", "").trim().split(","); //pwd, digit, f, s
						 String epass = executeJsFunc("rsa/an_hui_dx_rsa.js", "exeEncry", "serviceNbr=" + phoneNo, f, stra[0], stra[1], stra[2]);
						 if (epass == null) 
							 return;

						 entity.put("xizang_rsa_uuid", epass);
					 }
				} catch (Exception e) {
					logger.error("西藏电信uuid获取出错！", e);
				}
			}
		});  
	}

	private void requestMessageLogService(final Date d, final int page) {
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyyMMdd");
		String ed = DateUtils.formatDate(ds[1], "yyyyMMdd");
		// 默认查询用户的10000条短消息记录
		// 9.26证明10000条返回数据无意义
		String url = "http://xz.189.cn/service/bill/xz/feeDetailrecordList.action";
		String[][] pairs_msg = { { "currentPage", Integer.toString(page) },
				{ "pageSize", "10000" }, { "effDate", bd }, { "expDate", ed },
				{ "serviceNbr", phoneNo }, { "operListID", "2" },// 短信
				{ "isPrepay", "1" }, { "pOffrType", "481" } };
		postUrl(url, "http://xz.189.cn/service/bill/fee.action?type=ticket",
				pairs_msg, new AbstractProcessorObserver(util,
						WaringConstaint.XZDX_4) {
					@Override
					public void afterRequest(SimpleObject context) {
						String text = ContextUtil.getContent(context);
						if (text != null
								&& !text.contains("尊敬的客户，对不起，未查询到相关记录")) {
							saveSmsLog(context, d, page);
						}
					}
				});
	}

	private void requestCallLogService(final Date d, final int page) {
		// http://xz.189.cn/service/bill/xz/feeDetailrecordList.action
		// currentPage 1
		// pageSize 10
		// effDate 2014-09-01
		// expDate 2014-09-17
		// serviceNbr 18108910674
		// operListID 1
		// isPrepay 1
		// pOffrType 481
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyyMMdd");
		String ed = DateUtils.formatDate(ds[1], "yyyyMMdd");
		String[][] pairs_detail = { { "currentPage", Integer.toString(page) },
				{ "pageSize", "10000" }, { "effDate", bd }, { "expDate", ed },
				{ "serviceNbr", phoneNo }, { "operListID", "1" },// 详单
				{ "isPrepay", "1" }, { "pOffrType", "481" } };
		String url = "http://xz.189.cn/service/bill/xz/feeDetailrecordList.action";
		// 默认查询用户的10000条通话详单
		// 9.26证明10000条无意义，仍然返回10条，需要翻页
		postUrl(url, "http://xz.189.cn/service/bill/fee.action?type=ticket",
				pairs_detail, new AbstractProcessorObserver(util,
						WaringConstaint.XZDX_4) {
					@Override
					public void afterRequest(SimpleObject context) {
						String text = ContextUtil.getContent(context);
						if (text != null
								&& !text.contains("很抱歉，查询失败！尊敬的客户，对不起，未查询到相关记录")) {
							saveCallLog(context, d, page);
						}
					}
				});

	}

	private void saveCallLog(SimpleObject context, Date d, int page) {
		boolean isEnd = false;
		try {
			Document doc = ContextUtil.getDocumentOfContent(context);
			// System.out.println(doc);
			Elements trs = doc.select("tbody").select("tr");
			if (trs == null || trs.toString().equals("")) {
				return;
			}
			for (int i = 0; i < trs.size(); i++) {
				Elements tds = trs.get(i).select("td");
				if (tds.toString().contains("合计") || tds.toString().contains("小计")) {
					isEnd = true;
					break;
				}
				if (tds.size() > 7) {
					String serialNumber = tds.get(0).text()
							.replaceAll("\\s*", "");// 序号
					String thlx = StringEscapeUtils.unescapeHtml3(tds.get(1)
							.text().replaceAll("\\s*", ""));// 通信方式(主叫被叫)
					String tradeType = StringEscapeUtils.unescapeHtml3(tds
							.get(2).text().replaceAll("\\s*", ""));// 通信类型
					String thwz = StringEscapeUtils.unescapeHtml3(tds.get(3)
							.text().replaceAll("\\s*", ""));// 通信地点
					String dfhm = tds.get(4).text().replaceAll("\\s*", "");// 对方号码
					String thsj = tds.get(5).text();// 起始时间
					// 2014-09-16 20:38:32
					thsj = thsj.substring(0, 10) + " "
							+ thsj.substring(thsj.length() - 8, thsj.length());
					// System.out.println(thsj);
					String thsc = tds.get(6).text().replaceAll("\\s*", "");// 通话时长（时分秒）
					String zfy = tds.get(7).text().replaceAll("\\s*", "");// 费用（元）
					DianXinDetail dxDetail = new DianXinDetail();
					dxDetail.setPhone(phoneNo);
					dxDetail.setTradeType(tradeType);
					dxDetail.setRecevierPhone(dfhm);
					dxDetail.setTradeAddr(thwz);
					dxDetail.setcTime(DateUtils.StringToDate(thsj,
							"yyyy-MM-dd HH:mm:ss"));
					// 将HH:mm:ss格式时间转化为秒
					int secend = DateUtils.transform(thsc);
					dxDetail.setTradeTime(secend);
					dxDetail.setCallWay(thlx);
					// dxDetail.setBasePay(new BigDecimal(basePay));
					// dxDetail.setLongPay(new BigDecimal(longPay));
					dxDetail.setAllPay(new BigDecimal(zfy));
					// dxDetail.setOnlinePay(new BigDecimal(text));
					// dxDetail.setTradeWay(text);
					addDetail(dxDetail);
					//detailList.add(dxDetail);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isEnd)
			requestCallLogService(d, page + 1);
	}

	private void saveSmsLog(SimpleObject context, Date d, int page) {
		boolean isEnd = false;
		try {
			Document doc = ContextUtil.getDocumentOfContent(context);
			// System.out.println(doc);
			Elements trs = doc.select("tbody").select("tr");
			if (trs == null || trs.toString().equals("")) {
				return;
			}
			for (int i = 0; i < trs.size(); i++) {
				Elements tds = trs.get(i).select("td");
				if (tds.toString().contains("合计") || tds.toString().contains("小计")) {
					isEnd = true;
					break;
				}
				if (tds.size() > 5) {
					TelcomMessage obj = new TelcomMessage();
					obj.setPhone(phoneNo);
					obj.setBusinessType(tds.get(1).text());// 业务类型
					// obj.setBusinessType(tds.get(2).text());//收发类型
					obj.setRecevierPhone(tds.get(3).text()
							.replaceAll("\\s*", ""));// 对方号码
					String s = tds.get(4).text();
					String time = s.substring(0, 10) + " "
							+ s.substring(s.length() - 8, s.length());
					// System.out.println(time);
					obj.setSentTime(DateUtils.StringToDate(time,
							"yyyy-MM-dd HH:mm:ss"));// 发送时间
					obj.setAllPay(Doubles.tryParse(tds.get(5).text()
							.replaceAll("\\s*", "")));// 总费用
					//System.out.println(obj.toString());
					addMessage(obj);
					//messageList.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isEnd)
			requestMessageLogService(d, page + 1);
	}

	public static void main(String[] args) throws Exception {
		String phoneNo = "18108910674";
		String password = "281039";
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		XiZangDianXin dx = new XiZangDianXin(spider, null, phoneNo, password,
				"2345", null);
		dx.setTest(true);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		/*
		 * dx.printData(); dx.parseBalanceInfo(); spider.start();
		 */
		/*
		 * dx.setAuthCode(CUtil.inputYanzhengma()); dx.requestAllService();
		 * spider.start();
		 */
		// dx.printData();
		spider.start();
		
	}
}
