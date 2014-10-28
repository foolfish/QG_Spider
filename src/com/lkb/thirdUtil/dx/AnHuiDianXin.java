package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONException;
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
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

public class AnHuiDianXin extends AbstractDianXinCrawler {
	
	public AnHuiDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
		//spider.getSite().setCharset("utf-8");
	}
	public AnHuiDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		//spider.getSite().setCharset("utf-8");
	}
	public AnHuiDianXin() {
		areaName = "安徽";
		customField1 = "3";
		customField2 = "13";
		ssoUrl = "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10013&toStUrl=http://ah.189.cn/service/bill/fee.action?type=resto";
		shopId = "10013";
	}
	
	public void checkVerifyCode(final String phone) {   
		String prefix = "anhui";
		//保存验证码图片
		data.put("checkVerifyCode","1");
		//DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
		String picName =  prefix + "_dx_code_" + phone + "_" + (int) (Math.random() * 1000) + "5tw";
		try {
			String imgName = saveFile("http://ah.189.cn/sso/VImage.servlet?random=" + Math.random(), "http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.action", null, picName, true);
			data.put("imgName", imgName);
		} catch (Exception e) {
			logger.error("error",e);
			notifyStatus();
		}
    }
	public void goLoginReq() {     
		
		getUrl("http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.action", "http://189.cn/dqmh/cms/index/login_jx.jsp?shopId=10013&rand=" + System.currentTimeMillis(), new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginPage1(context);
			}
		});
	}
	private void parseLoginPage1(SimpleObject context) {
		String text = ContextUtil.getContent(context);
		if (text == null) {
			return;
		}
		String n = StringUtil.subStr("function bodyRSA()", "}", text);
		n = n.trim();
		int f = NumberUtils.toInt(StringUtil.subStr("setMaxDigits(", ");", n).trim(), 130);
		String[] stra = StringUtil.subStr("return new RSAKeyPair(", ");", n).replaceAll("\"", "").trim().split(",");
		//pwd, digit, f, s
		String epass = executeJsFunc("rsa/an_hui_dx_rsa.js", "exeEncry", password, f, stra[0], stra[1], stra[2]);
		String ephone = executeJsFunc("rsa/an_hui_dx_rsa.js", "exeEncry", "serviceNbr="+phoneNo, f, stra[0], stra[1], stra[2]);
		data.put("ephone", ephone);
		if (epass == null) {
			return;
		}
		//&loginType=4&accountType=10&latnId=551&loginName=18134537231&passType=0
		//&passWord=6c95aa06be848fe4ade941d9baccfdc723522505b100a82295331b5b9059bbabf58b47d9e689f910486e6d4fefd7ba2f16b3e2444d992356cb2eaa8f550c8de2&validCode=7898&csrftoken=null
		String[][] pairs = {{"ssoAuth", "0"}, {"returnUrl", "%2Fservice%2Faccount%2Finit.action&sysId=1003"}, {"loginType", "4"}, {"accountType", "10"}, {"latnId", "551"}, 
				{"loginName", phoneNo}, {"passType", "0"}, {"passWord", epass}, {"validCode", authCode}, {"csrftoken", "null"}};
		postUrl("http://ah.189.cn/sso/LoginServlet", "http://ah.189.cn/sso/login?returnUrl=%2Fservice%2Faccount%2Finit.action", pairs, new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginPage2(context);
			}
		});
	}
	private void parseLoginPage2(SimpleObject context) {
		Document doc = ContextUtil.getDocumentOfContent(context); 

		Elements form = doc.select("form#uamForm");
		String url = form.attr("action");
		String[][] pairs = {{"SSORequestXML", form.select("#SSORequestXML").val().trim()}};
		postUrl(url, "http://ah.189.cn/sso/LoginServlet", pairs, new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				ssoLogin1(context);
			}
		});
	}
	public void sendSmsPasswordForRequireCallLogService() {
		getUrl("http://hn.189.cn/hnselfservice/billquery/bill-query!queryBillList.action?tabIndex=2&patitype=&valicode=&accNbr="  + phoneNo + "&chargeType=10&_=" + System.currentTimeMillis(), "http://hn.189.cn/hnselfservice/billquery/bill-query!showTabs.action", new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
					if (doc.select("#blqYearMonth").size() > 0) {
						setStatus(STAT_SUC);
						data.put("success", true);
					}
					notifyStatus();
				} catch (Exception e) {
					logger.error("error",e);
				}
				
			}
		});
		
	}

	protected void onCompleteLogin(SimpleObject context) {
		setStatus(STAT_SUC);
		//sendSmsPasswordForRequireCallLogService();
		requestService();
		//parseBalanceInfo();
	}
	
	public void requestAllService() {
		requestService();
	}
	private void requestService() {
		
		Date d = new Date();
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyy-MM");
			if (i > 0) {
				//requestMonthBillService(d, dstr);	
			}
			requestCallLogService(1, 1, cd, dstr);
			Map<String, String> dxMap = new HashMap<String, String>();
			if (i != 0) {
				requestFlowLogService(false, dstr, 1, dxMap);
			} else {
				requestFlowLogService(true, dstr, 1, dxMap);
			}
		}
		requestMonthBillService();
		parseBalanceInfo();
		
		//requestCallLogService(context, number, queryDate);
	}	
	
	private void parseBalanceInfo() {
		getUrl("http://ah.189.cn/service/manage/showCustInfo.action", null, new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements es1 = doc.select("ul.mydata");
				if (es1.size() < 1) {
					return;
				}
				Element info = es1.get(0);
				String userName = info.select("li").get(0).text().replaceAll("客户名称：", "").trim();
				Element detail = es1.get(1);
				Elements es2 = detail.select("li");
				user.setBirthday(DateUtils.StringToDate(es2.get(0).text().replaceAll("出生日期：", ""), "yyyy-MM"));
				user.setEmail(es2.get(7).text().replaceAll("Email：", ""));
				user.setAddr(es2.get(8).text().replaceAll("地 址：", ""));
				user.setRealName(userName);
			}
		});
		getUrl("http://ah.189.cn/service/account/feeBalance.action?rnd=" + Math.random(), "http://ah.189.cn/service/account/init.action", new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					JSONObject obj = ContextUtil.getJsonOfContent(context);
					if (obj != null && obj.has("data")) {
						JSONObject obj1 = obj.getJSONObject("data");
						JSONObject obj2 = obj1.getJSONObject("resultData");
						double d1 = obj2.getDouble("total_balance");
						double d2 = obj2.getDouble("history_fee");
						double d3 = obj2.getDouble("real_fee");
						BigDecimal b1= new BigDecimal(StringUtil.formatNumber(d1 - d2 - d3));
						addPhoneRemain(b1);
						
						DianXinTel tel = new DianXinTel();
						tel.setTeleno(phoneNo);
						getTelList().add(tel);
						String date = DateUtils.formatDate(new Date(), "yyyyMM");
						tel.setcTime(DateUtils.StringToDate(date, "yyyyMM"));
						tel.setDependCycle(date.substring(0, 4) + "/"
								+ date.substring(4, 6) + "/01-"
								+ DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
						tel.setcAllPay(new BigDecimal(d3));
					}
				} catch (JSONException e1) {
					logger.error("error",e1);
				}
			}
		});
	}	
	
	private void requestMonthBillService() {
		//String dstr1 = DateUtils.formatDate(d, "yyyy-MM");
		getUrl("http://ah.189.cn/service/bill/initQueryBill.action?rnd=" + Math.random(), null, new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 			
				Elements table = doc.select("table.billtab");
				Elements trs = table.select("tr");
				int len = trs.size();
				for(int i = 1; i < len; i++) {
					Elements tds = trs.get(i).select("td");
					DianXinTel obj = new DianXinTel();
					obj.setTeleno(phoneNo);
					getTelList().add(obj);
					String date = tds.get(0).text().trim();
					obj.setcTime(DateUtils.StringToDate(date, "yyyyMM"));
					obj.setDependCycle(date.substring(0, 4) + "/" + date.substring(4, 6) + "/01-" + DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy/MM/dd"));
					String n = tds.get(1).text();
					BigDecimal b1 = new BigDecimal(n.length() == 0 ? "0" : n);
					obj.setcAllPay(b1);
				}
			}
		});
	}
	private void requestCallLogService(final int page, final int t, final Date d, final String dstr) {	
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
		String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		getUrl("http://ah.189.cn/service/bill/exportTxt.action?effDate=" + bd + "&expDate=" + ed + "&serviceNbr=" + phoneNo + "&operListID=2&prdType=481&fileName=/ias/ias/optias/tmp/detail/5512014090114392125089400007438&=" + phoneNo + "&mobilecallType=2", 
				null, new String[] {"utf-8"}, new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, page, d, dstr);
			}
		});
		getUrl("http://ah.189.cn/service/bill/exportTxt.action?effDate=" + bd + "&expDate=" + ed + "&serviceNbr=" + phoneNo + "&operListID=4&prdType=481&fileName=/ias/ias/optias/tmp/detail/5512014090114392125089400007438&=" + phoneNo, 
				null, new String[] {"utf-8"}, new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveSmsLog(context, page, d, dstr);
			}
		});
		
	}
	private void saveCallLog(SimpleObject context, final int page, final Date d, final String dstr) {
		String rt = ContextUtil.getContent(context);
		if (StringUtils.isBlank(rt)) {
			return;
		}
		try {
			int index = rt.indexOf("\r\n");
			String[] rows = null;
			if (index >= 0) {
				rows = rt.split("\r\n");
			} else {
				index = rt.indexOf("\n");
				if (index >= 0) {
					rows = rt.split("\n");
				} else {
					rows = rt.split("\r");
				}
			}
			if (rows != null) {
				int i = 0;
				for(String row : rows) {
					if (!StringUtils.isBlank(row)) {
						while(row.indexOf("  ") >= 0) {
							row = row.replaceAll("  ", " ");
						}
						try {
							String[] cols = row.split(" ");
							boolean hasContent = false;
							if (cols != null && cols.length > 1 && cols[0].indexOf("-") < 0) {						
								for(String col : cols) {
									if (col != null && !StringUtils.isBlank(col)) {
										hasContent = true;
										break;
									}
									//startIndex ++;
								}
							}

							if (hasContent) {
								i++;
								if (i >= 2) {
									try {
										DianXinDetail obj = new DianXinDetail();
										obj.setPhone(phoneNo);
										int j = 0;
										String day = null;
										for(String col : cols) {
											if (col != null && !StringUtils.isBlank(col)) {
												if (j == 2) {
													obj.setTradeType(col);
												} else if (j == 3 || j == 4) {
													if (col.indexOf(phoneNo) < 0) {
														obj.setRecevierPhone(col);
													}
												} else if (j == 5) {
													day = col;
												} else if (j == 6) {
													obj.setcTime(DateUtils.StringToDate(day + " " + col, "yyyy-MM-dd HH:mm:ss"));
												} else if (j == 7) {
													obj.setTradeTime(NumberUtils.toInt(col));
												} else if (j == 8) {
													obj.setAllPay(new BigDecimal(col));
												} else if (j == 1) {
													obj.setCallWay(col);
												}
												j++;
											}
										}

										if (obj != null) {
											detailList.add(obj);
										}
									} catch (Exception e) {
										logger.error(row, e);
									}
								}
							}
						} catch (Exception e) {
							logger.error(row, e);
						}

					}
				}
			}

		} catch (Exception e) {
			logger.error(rt, e);
		}
	}
	private void saveSmsLog(SimpleObject context, final int page, final Date d, final String dstr) {
		String rt = ContextUtil.getContent(context);
		if (StringUtils.isBlank(rt)) {
			return;
		}
		try {
			int index = rt.indexOf("\r\n");
			String[] rows = null;
			if (index >= 0) {
				rows = rt.split("\r\n");
			} else {
				index = rt.indexOf("\n");
				if (index >= 0) {
					rows = rt.split("\n");
				} else {
					rows = rt.split("\r");
				}
			}
			if (rows != null) {
				int i = 0;
				for(String row : rows) {
					if (!StringUtils.isBlank(row)) {
						while(row.indexOf("  ") >= 0) {
							row = row.replaceAll("  ", " ");
						}
						try {
							String[] cols = row.split(" ");
							boolean hasContent = false;
							if (cols != null && cols.length > 1 && cols[0].indexOf("-") < 0) {						
								for(String col : cols) {
									if (col != null && !StringUtils.isBlank(col)) {
										hasContent = true;
										break;
									}
									//startIndex ++;
								}
							}
							
							if (hasContent) {
								i++;
								if (i >= 2) {
									try {
										TelcomMessage obj = new TelcomMessage();
										obj.setPhone(phoneNo);
										int j = 0;
										String day = null;
										for(String col : cols) {
											if (col != null && !StringUtils.isBlank(col)) {
												if (j == 2) {
													obj.setRecevierPhone(col);
												} else if (j == 3) {
													day = col;
												} else if (j == 4) {
													obj.setSentTime(DateUtils.StringToDate(day + " " + col, "yyyy-MM-dd HH:mm:ss"));
												} else if (j == 5) {
													obj.setAllPay(Doubles.tryParse(col));
												} else if (j == 1) {
													obj.setBusinessType(col);
												}
												j++;
											}
										}
										
										if (obj != null) {
											messageList.add(obj);
										}
									} catch (Exception e) {
										logger.error(row, e);
									}
								}
							}
						} catch (Exception e) {
							logger.error(row, e);
						}
						
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(rt, e);
		}
	}
	
	
	private void requestFlowLogService(final boolean current, final String date, final int i, Map<String, String> dxMap) { 


		//http://ah.189.cn/service/bill/queryDetail.action
		//currentPage=1&pageSize=10&effDate=2014-10-01&expDate=2014-10-15&uuid=0064fcbef21cb47ee175b67ca1a0c86e3688e342941134905e13a4ea348b5faaa363dbbf19b1c2f1a58734165687bfeec400acd8ac90f9c77e3446164aad974d&operListId=6
		String startTime = date + "-01"; 
		String endTime = "";
		if(current){
			endTime = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
		}else{
			endTime = DateUtils.lastDayOfMonth(date, "yyyy-MM", "yyyy-MM-dd");
		}
		
		if(i == 1){
			
			String uuid =  data.getString("ephone"); 

			String[][] pairs_flow = {{"currentPage", String.valueOf(i)},{"pageSize","10"}, {"effDate", startTime}, {"expDate", endTime}, {"uuid", uuid}, {"operListId", "6"}};
			//TODO
			postUrl("http://ah.189.cn/service/bill/queryDetail.action","http://ah.189.cn/service/bill/fee.action?type=allDetails", new String[] {"utf-8"}, pairs_flow,new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
			saveFlowLog(current, context, i, date);
			}
			});
			
			
			
		} else {
			 
			dxMap.put("effDate", startTime);
			dxMap.put("expDate", endTime);

			String pairs_flow[][] = new String[dxMap.keySet().size()][2];
			Object[] pair = dxMap.keySet().toArray();
			for (int j = 0; j < pair.length; j++) {
				pairs_flow[j][0] = (String) pair[j];
				pairs_flow[j][1] = dxMap.get(pair[j]);
			}
//			Object[] param = {"utf-8"};
			postUrl("http://ah.189.cn/service/bill/queryDetail.action","http://ah.189.cn/service/bill/fee.action?type=allDetails", pairs_flow,new AbstractProcessorObserver(util, WaringConstaint.ANHUIDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
			saveFlowLog(current, context, i, date);
			}
			});
		}

		}
	
	
	
	protected void saveFlowLog(boolean current, SimpleObject context, int i, String date) {
		if (context == null)
			return;
		System.out.println(context);
		Document doc = ContextUtil.getDocumentOfContent(context);
		System.out.println(doc.toString());
		if (doc.toString().contains("没有符合条件的记录")) {
			if(i == 1){
				DianXinFlow dxFlow = new DianXinFlow();
				UUID uuid = UUID.randomUUID();
				dxFlow.setId(uuid.toString());
				dxFlow.setPhone(phoneNo);

				// 起止日期:2014-10-01——2014-10-31
				String cycle = date + "-01" + DateUtils.lastDayOfMonth(date, "yyyy-MM", "yyyy-MM-dd");
				// 查询日期
				Date queryMonth = DateUtils.StringToDate(date, "yyyyMM");
				dxFlow.setDependCycle(cycle);
				dxFlow.setQueryMonth(queryMonth);

				dxFlow.setAllFlow(new BigDecimal(0));
				dxFlow.setAllTime(new BigDecimal(0));
				dxFlow.setAllPay(new BigDecimal(0.00));
				flowList.add(dxFlow);
			}
			return;
		}
		Element table = doc.select("table").get(0);
		Elements trs = table.select("tr");
		UUID uuid;
		if (i == 1) {
			DianXinFlow dxFlow = new DianXinFlow();
			uuid = UUID.randomUUID();
			dxFlow.setId(uuid.toString());
			dxFlow.setPhone(phoneNo);

			// 起止日期:2014-10-012014-10-31
			String cycle = trs.get(1).select("td").get(0).text()
					.replaceAll("起止日期： ", "").trim();
			// 查询日期:2014年10月13日
			Date queryMonth = DateUtils.StringToDate(date, "yyyyMM");
			dxFlow.setDependCycle(cycle);
			dxFlow.setQueryMonth(queryMonth);
			// 总流量:	85.49MB
			String totalFlow = trs.get(2).select("td").get(0).text().replaceAll("总流量：", "").trim();
			// 总时长:0小时0分0秒
			String allTime = trs.get(2).select("td").get(1).text().replaceAll("总时长：", "").trim();
			// 总费用:0.0元
			String allPay = trs.get(3).select("td").get(0).text().replaceAll("总费用：", "").replaceAll("元", "").trim();
			dxFlow.setAllFlow(new BigDecimal(StringUtil.flowFormat(totalFlow)));
			dxFlow.setAllTime(new BigDecimal(StringUtil.flowTimeFormat(allTime)));
			dxFlow.setAllPay(new BigDecimal(allPay));
			flowList.add(dxFlow);
		}
		
		Elements trs1 = doc.select("table").get(1).select("tr");
		for (int j = 1; j < trs1.size(); j++) {
			Elements tds1 = trs1.get(j).select("td");
			DianXinFlowDetail dxFd = new DianXinFlowDetail();
			uuid = UUID.randomUUID();
			dxFd.setId(uuid.toString());
			dxFd.setPhone(phoneNo);

			dxFd.setLocation(tds1.get(5).text().trim());// 通信地点
			dxFd.setBeginTime(DateUtils.StringToDate(tds1.get(1).text().trim(),
					"yyyy-MM-dd HH:mm:ss"));// 开始时间
			// 上网时长
			dxFd.setTradeTime(StringUtil.flowTimeFormat(tds1.get(2).text().trim()));
			String flow_temp = tds1.get(3).text().replaceAll(" ", "").trim();
			double flow = flowFormat(flow_temp);
			dxFd.setFlow(new BigDecimal(flow));// 总流量
			
			
			
			dxFd.setNetType(tds1.get(4).text().trim());// 通讯类型
			dxFd.setBusiness("");// 使用业务
			dxFd.setFee(new BigDecimal(tds1.get(6).text().trim()));// 费用（元）
			flowDetailList.add(dxFd);
		}	
		
		String fileName = doc.select("input[id=fileName]").val();
		
//		String name = "%25E5%2591%25A8%25E7%2581%258F(1000010543541)";
//		String totalPays = "20.19%25E5%2585%2583";
		String name = trs.get(0).select("td").get(0).text().replaceAll("客户名称：", "").trim();
		String ceDate = trs.get(1).select("td").get(0).text().replaceAll("起止日期： ", "").trim();
		String seDate = trs.get(1).select("td").get(1).text().replaceAll("查询日期：", "").trim();
		String totalFlow = trs.get(2).select("td").get(0).text().replaceAll("总流量：", "").trim();
		String totalPays = trs.get(3).select("td").get(0).text().replaceAll("总费用：", "").trim();
		
		Map<String, String> dxMap = new HashMap<String, String>();
		dxMap.put("operListId", "6");
		dxMap.put("currentPage", String.valueOf(i+1));
		dxMap.put("pageSize","10");  
		dxMap.put("uuid", data.getString("ephone")); 
		dxMap.put("fileName", fileName);
		dxMap.put("queryTitleDetail.custName", name);
		dxMap.put("queryTitleDetail.accNbr", phoneNo);
		dxMap.put("queryTitleDetail.cycleRange", ceDate);
		dxMap.put("queryTitleDetail.qryDate", seDate);
		dxMap.put("queryTitleDetail.total_KB", totalFlow);
		dxMap.put("queryTitleDetail.totalCharge", totalPays);
		dxMap.put("queryTitleDetail.fileName", fileName);
		dxMap.put("queryTitleDetail.isPrePay", "2100");
		
		
		requestFlowLogService(current, date, i + 1, dxMap);

	}
	
	private double flowFormat(String flow_temp) {
		double gb2Kb=0,mb2Kb=0,kb=0,b=0;
		if(flow_temp.contains("GB") || flow_temp.contains("MB") || flow_temp.contains("KB") || flow_temp.contains("B")){
			if(flow_temp.contains("GB")){
				gb2Kb = Double.parseDouble(flow_temp.split("GB")[0].trim())*1024*1024*1024;
			}
			if(flow_temp.contains("MB")){
				if(flow_temp.contains("GB"))
					mb2Kb = Double.parseDouble(flow_temp.split("GB")[1].split("MB")[0].trim())*1024;
				else
					mb2Kb = Double.parseDouble(flow_temp.split("MB")[0].trim())*1024*1024;
			}
			if(flow_temp.contains("KB")){
				if(flow_temp.contains("MB")){
					kb = Double.parseDouble(StringUtil.subStr("MB", "KB", flow_temp).trim());
				}else if(flow_temp.contains("GB")){
					kb = Double.parseDouble(StringUtil.subStr("GB", "KB", flow_temp).trim());
				}else{
					kb = Double.parseDouble(flow_temp.split("KB")[0].trim())*1024;
				}
			}
			if(flow_temp.contains("B")){
				if(flow_temp.contains("KB")){
					b = Double.parseDouble(flow_temp.substring(flow_temp.indexOf("KB") + 2, flow_temp.length()-1).trim());
				}else if(flow_temp.contains("MB")){
					b = Double.parseDouble(flow_temp.substring(flow_temp.indexOf("MB") + 2, flow_temp.length()-1).trim());
				}else if(flow_temp.contains("GB")){
					b = Double.parseDouble(flow_temp.substring(flow_temp.indexOf("GB") + 2, flow_temp.length()-1).trim());
				}else {
					b = Double.parseDouble(flow_temp.split("B")[0].trim());
				}
			}
		}
		
		double flow = (gb2Kb + mb2Kb + kb + b) / 1024;
		double totalFlow = Math.round(flow);
		return totalFlow;
	}
	public static void main(String[] args) throws Exception {
		String phoneNo = "18134537231";
		String password = "062888";
		
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		AnHuiDianXin dx = new AnHuiDianXin(spider, null, phoneNo, password, "2345", null);
		dx.setTest(true);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.getData().clear();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		dx.printData();
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		//dx.printData();
		if (true) {
			return;
		}

	}
}
