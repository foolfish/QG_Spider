package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcifs.dcerpc.rpc.uuid_t;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.robot.Request;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.thirdUtil.StatusTracker;
import com.lkb.util.DateUtilTool;
import com.lkb.util.DateUtils;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;

public class GuangDongDianxin extends AbstractDianXinCrawler{
	public GuangDongDianxin(Spider spider, WarningUtil util) {
		this.spider = spider;
		this.util = util;
		// spider.getSite().setCharset("gbk");
	}
	public GuangDongDianxin(Spider spider, User user, String phoneNo, String password,String authCode, WarningUtil util) {
		this.spider = spider;
		this.phoneNo = phoneNo;
		this.password = password;
		this.authCode = authCode;
		if (user == null) {
			this.user = new User();
			this.user.setPhone(phoneNo);
		} else {
			this.user = user;
		}
		this.util = util;
	}
	public void getVerifyCode(final String userName) {
		String picName =  "guangdong_dx_code_" + userName + "_" + (int) (Math.random() * 1000) + "3ec";
		//saveVerifyCode("xizang", userName);
		String imgName = "none";
		try {
			imgName = saveFile("http://gd.189.cn/code", null, "gd.189.cn", picName, true);
		} catch (Exception e) {
			logger.error("error",e);
		}
		data.put("imgName2", imgName);
	}
	public void goLoginReq() {        	
		setUniqueHttpProxy();
    	Request req = new Request("http://gd.189.cn/dwr/exec/newLoginDwr.goLogin.dwr");
    	req.setMethod("POST");
    	req.putHeader("Referer", "http://gd.189.cn/common/newLogin/newLogin.htm?SSOArea=&SSOAccount=&SSOProType=&SSORetryTimes=2&SSOError=190017&SSOCustType=0&loginOldUri=/service/bill/bqsearch.jsp&SSOOldAccount=null&SSOProTypePre=2000004");
    	req.putHeader("Content-Type", "text/plain; charset=UTF-8");
    	int i = 15;
    	req.initNameValuePairs(i);
    	req.setNameValuePairs(--i, "xml", "true");
    	req.setNameValuePairs(--i, "c0-param9", "string:");
    	req.setNameValuePairs(--i, "c0-param8", "string:");
    	req.setNameValuePairs(--i, "c0-param7", "string:" + password);
    	req.setNameValuePairs(--i, "c0-param6", "string:00");
    	req.setNameValuePairs(--i, "c0-param5", "string:" + phoneNo);
    	req.setNameValuePairs(--i, "c0-param4", "string:2000004");
    	req.setNameValuePairs(--i, "c0-param3", "string:");
    	req.setNameValuePairs(--i, "c0-param2", "string:");
    	req.setNameValuePairs(--i, "c0-param1", "boolean:false");
    	req.setNameValuePairs(--i, "c0-param0", "boolean:false");
    	int random = (int)Math.floor(Math.random() * 10001);
    	String id = (random + "_" + new Date().getTime()).toString(); 
    	req.setNameValuePairs(--i, "c0-id", id);
    	req.setNameValuePairs(--i, "c0-methodName", "goLogin");
    	req.setNameValuePairs(--i, "c0-scriptName", "newLoginDwr");
    	req.setNameValuePairs(--i, "callCount", "1");
    	req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.GDDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginPasswordText(context);
			}
		});
    	spider.addRequest(req);
    	//printData();
    }
	
	private void parseLoginPasswordText(SimpleObject context) {    	
		String text = ContextUtil.getContent(context);
    	String nexturl = regex("var s2=\"(.*?)\";s0\\[1\\]", text, 1);
    	if (nexturl == null) {
    		logger.error("Error : No SSO URL", text);    		
    	} else {
    		//1.生成一个request
    		Request req = new Request(nexturl);
    		req.setMethod("POST");
    		req.putHeader("Referer", "http://gd.189.cn/common/newLogin/newLogin.htm?SSOArea=&SSOAccount=&SSOProType=&SSORetryTimes=2&SSOError=190017&SSOCustType=0&loginOldUri=/service/bill/bqsearch.jsp&SSOOldAccount=null&SSOProTypePre=2000004");
    		req.putHeader("Content-Type", "application/x-www-form-urlencoded");
    		int i = 15;
    		req.initNameValuePairs(i);
    		//smsCode=&loginCodeRand=
    		//req.setNameValuePairs(--i, "");
    		req.setNameValuePairs(--i, "password", password);
    		req.setNameValuePairs(--i, "mobilePassword", "custPassword");
    		req.setNameValuePairs(--i, "account", phoneNo);
    		req.setNameValuePairs(--i, "accountTypeSel", "2000004");
    		req.setNameValuePairs(--i, "areaSel", "020");
    		req.setNameValuePairs(--i, "isShowLoginRand", "N");
    		req.setNameValuePairs(--i, "from", "new");
    		req.setNameValuePairs(--i, "sysType", "2");
    		req.setNameValuePairs(--i, "SSORequestXML", regex("var s4=\"(.*?)\";s0\\[3\\]", text, 1));
    		req.setNameValuePairs(--i, "errorMsgType", "");
    		req.setNameValuePairs(--i, "IFdebug", "null");
    		req.setNameValuePairs(--i, "loginOldUri", "/service/bill/bqsearch.jsp");
    		req.setNameValuePairs(--i, "passwordType", "00");
    		req.setNameValuePairs(--i, "accountType", "2000004");
    		req.setNameValuePairs(--i, "area", "");
    		//2.请求完成后的解析
    		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.GDDX_2) {
    			@Override
    			public void afterRequest(SimpleObject context) {
    				parseSSOLogin(context);
    			}
			});
    		//3.加入到Spider的执行队列	
    		spider.addRequest(req);
    	}
	}
	private void parseSSOLogin(SimpleObject context) {
		Document doc = ContextUtil.getDocumentOfContent(context); 
		String nexturl = doc.select("form#redirectForm").attr("action");
		Request req = new Request(nexturl);
		req.putHeader("Referer", "http://uam.gd.ct10000.com/portal/SSOLoginForWT.do");
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.GDDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				isLogin(context);
			}
		});
		spider.addRequest(req);
		
		//requestIndexService(page, nexturl);
	}
	private void isLogin(SimpleObject context) {
		Date d = new Date();
		String dstr = DateUtils.formatDate(d, "yyyyMM");
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.GDDX_5) {
			public void afterRequest(SimpleObject context) {
				try {
					
					JSONObject json = ContextUtil.getJsonOfContent(context);
					String numberstr = json.getString("SOldNumber");
					data.put("numberstr", numberstr);
					String balance = json.getString("benjin");

					data.put("预存余额", balance);
					String islogin = json.getString("isLogin");
					data.put("isLogin", islogin);
					String keyong = json.getString("keyong");
					data.put("可用余额", keyong);
					BigDecimal b1= new BigDecimal(NumberUtils.toDouble(balance, 0d) + NumberUtils.toDouble(keyong, 0d));
					user.setPhoneRemain(b1);
					if ("Y".equalsIgnoreCase(islogin)) {
						requestService(context);
						setStatus(StatusTracker.STAT_LOGIN_SUC);
						notifyStatus();
					} else {
						setStatus(StatusTracker.STAT_STOPPED_FAIL);
						notifyStatus();
					}
					//resetDefaultProxy();
				} catch (JSONException e) {
					//logger.error(ContextUtil.getContent(context), e);
					setStatus(StatusTracker.STAT_STOPPED_FAIL);
					data.put("errMsg", "登录失败，请重试!");
					notifyStatus();
					logger.error("error",e);
				}

			}
		}, "http://gd.189.cn/query/serviceBillBalanceBalanceQueryAll.action?dataString="+dstr);
	}
	private void requestService(SimpleObject context) {
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.GDDX_8) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				String nexturl = doc.select("form#apply1Form").attr("action");
				Request req = new Request("http://gd.189.cn" + nexturl);
				req.setMethod("POST");
				req.putHeader("Referer", ContextUtil.getRequest(context).getUrl());
				Elements es = doc.select("#apply1Form input");
				req.initNameValuePairs(es.size());
				for(int i = es.size() - 1; i>=0; i--){ 
					req.setNameValuePairs(i, es.get(i).attr("name"), es.get(i).attr("value"));
				}
				req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.GDDX_9){
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil.getDocumentOfContent(context); 
						user.setRealName(doc.select("#rela_man_id").attr("value"));
						user.setAddr(doc.select("#post_addr_id").attr("value"));
						user.setEmail(doc.select("#email_id").attr("value"));
						user.setBirthday(DateUtils.StringToDate(doc.select("#bri_time_id").attr("value"), "yyyyMMdd"));
						user.setIdcard(doc.select("#id_num_id").attr("value"));
						user.setSex("1".equals(doc.select("#cust_sexs_id[checked]").attr("value")) ? "男" : "女");
						/*user.set
						doc.select("#bri_time_id").attr("value")*/
					}
				});
				spider.addRequest(req);
			}
		}, "http://gd.189.cn/transaction/operApply1.jsp?operCode=ChangeCustInfoNew");
		/*spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.GDDX_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				data.put("号码",  doc.select("div#haoma").select("span.dqhm").text());
				//page.putField("yexd", page.getHtml().xpath("//div[@id='yexd']"));    	
				Elements nb = doc.select("div.kbox").select("table.nb");
				data.put("余额", nb.text());
			}
		}, "http://gd.189.cn/service/Bill/Balance/BalanceQueryAll.do");*/
		//final Set<String> monthSet = new HashSet();
		//final String curMonth;
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.GDDX_7){
			@Override
			public void afterRequest(SimpleObject context){
				Document doc = ContextUtil.getDocumentOfContent(context);
				Elements es = doc.select("select#func_selectMonth5 option");
				Set<String> monthSet = new HashSet();
				for(int i = es.size() - 1; i>=0; i--){ 
					monthSet.add(es.get(i).attr("value"));
				}
				for(String str : monthSet) {
					requestDownloadMessageLog(context, data.getString("numberstr"), str);
				}
			}
		},  "http://gd.189.cn/service/bill/feiyong_xiangdan.jsp");
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.GDDX_7) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context);
				Elements es = doc.select("select#func_selectMonth5 option");
				Set<String> monthSet = new HashSet();
				for(int i = es.size() - 1; i>=0; i--){ 
					monthSet.add(es.get(i).attr("value"));
				}
				for(String str : monthSet) {
					requestDownloadCallLog(context, data.getString("numberstr"), str);
				}
			}
		}, "http://gd.189.cn/service/bill/feiyong_xiangdan.jsp");
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.GDDX_10) {
			@Override
			public void afterRequest(SimpleObject context) {
				requestMonthBillService(context);
			}
		}, "http://gd.189.cn/query/qryCustBill.action");
		requestBillNowQuery();
		requestOnlineFlowService();
	}
	
	/**
	* <p>Title: requestBillNowQuery</p>
	* <p>Description: 实时话费</p>
	* @author Jerry Sun
	*/
	private void requestBillNowQuery(){
		getUrl("http://gd.189.cn/query/serviceBillNowQuery.action", "http://gd.189.cn/query/realTimeFee.action?in_cmpid=ds-syleft-fycx-wdxf-hfcx", new AbstractProcessorObserver(util, WaringConstaint.GDDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					try {
						DianXinTel dxt = new DianXinTel();
						JSONObject jsonOfContent = ContextUtil.getJsonOfContent(context);
						dxt.setcTime(DateUtils.StringToDate(jsonOfContent.getJSONArray("row1").getString(0), "yyyyMMdd"));
						dxt.setcName(jsonOfContent.getString("custName"));
						dxt.setTeleno(jsonOfContent.getString("currentBusiNum"));
						dxt.setDependCycle(jsonOfContent.getJSONArray("row1").getString(1));
						dxt.setcAllPay(new BigDecimal(jsonOfContent.getJSONArray("row2").getString(3)));
						
						telList.add(dxt);
						
					} catch (Exception e) {
						logger.error("广东电信实时话费抓取出错!", e);
					}
				}
			}
		});
	}
	
	private void requestMonthBillService(SimpleObject context) {
		Document doc = ContextUtil.getDocumentOfContent(context);
		Request req = new Request("http://gd.189.cn/query/bill.action");
		req.setMethod("POST");
		req.putHeader("Referer", "	http://gd.189.cn/query/qryCustBill.action");
		//req.putHeader("Content-Type", "application/x-www-form-urlencoded");
		int i = 9;
		req.initNameValuePairs(i);
		//smsCode=&loginCodeRand=
		//req.setNameValuePairs(--i, "");
		req.setNameValuePairs(--i, "Date_Select_Type", doc.select("#Date_Select_Type").val());
		req.setNameValuePairs(--i, "billtype", doc.select("#billtype").val());
		req.setNameValuePairs(--i, "func_url", doc.select("#func_url").val());
		req.setNameValuePairs(--i, "listingType", doc.select("#listingType").val());
		final String number = doc.select("#number").val();
		req.setNameValuePairs(--i, "number", number);
		req.setNameValuePairs(--i, "zdType", doc.select("#zdType").val());
		req.setNameValuePairs(--i, "searchMode", doc.select("#searchMode").val());
		final String[] svals = new String[6];
		Elements es = null;
		for(int j=6; j >= 1; j--) {
			es = doc.select("#radio" + j);
			svals[j-1] = es.attr("svalue");
		}
		req.setNameValuePairs(--i, "searchDate", svals[0]);
		req.setNameValuePairs(--i, "qryMonth", es.val());
		req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.GDDX_11) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				String stext = "var consume =";
				int sindex = content.indexOf(stext);
				if (sindex >= 0) {
					int eindex = content.indexOf("];", sindex);
					String ctext = content.substring(sindex + stext.length(), eindex).replaceAll("\\]", "").replaceAll("\\[", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");
					String[] arr = ctext.split(",");
					int length = arr.length;
					String pno = StringUtil.isBlank(number) ? phoneNo : number;
					for(int x = length - 1; x >= 0; x--) {
						int len = length - x - 1;
						DianXinTel tel = new DianXinTel();
						tel.setcTime(DateUtils.StringToDate(svals[x], "yyyyMM"));
						tel.setcAllPay(new BigDecimal(arr[len]));
						data.put("Month" + x, svals[x]);
						data.put("Pay" + x, arr[len]);
						tel.setTeleno(pno);
						getTelList().add(tel);
					}
				}
			}
		});
		spider.addRequest(req);
	}
	private void requestDownloadMessageLog(SimpleObject context, String number, String queryDate){
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.GDDX_6) {
			@Override 
			public void afterRequest(SimpleObject context) {
				saveDownloadMessageLog(context);
			}
		}, "http://gd.189.cn/cloudbill/downLoadCLOUDBILLTXT.action?downtype=txt&number=" + number + "&querydate=" + queryDate + "&listingType=note&total_charge=&number200201=&pageSize=9999&startVisitDate=&endVisitDate=");
	}
	private void requestOnlineFlowService(){
		Date d = new Date();
		for (int i = 5; i >=0; i--) {
			String dstr = DateUtils.formatDate(DateUtils.add(d, Calendar.MONTH, -1 * i), "yyyyMM");
			getUrl("http://gd.189.cn/cloudbill/qryCLOUDBILL.action?kehuzuanqu=1&querydate="+dstr+"&startVisitDate=&endVisitDate=&listingType=data&number="+data.getString("numberstr")+"&SearchVerifyCode="+authCode+"&total_charge=0.00&pageSize=10000&curSize=0&sort_column_name=&sort_type=&number200201=", null, new AbstractProcessorObserver(util, WaringConstaint.GDDX_1) {
				@Override
				public void afterRequest(SimpleObject context) {
					Document doc = ContextUtil.getDocumentOfContent(context); 
					try {
						Elements table_bill = doc.select("table.tablesty");
						if (table_bill==null||table_bill.equals("")) {
							return;
						}else {
							Elements trs = table_bill.select("tr");
							String dependCycle = trs.get(2).select("td").get(0).text().replaceAll("起止日期：", "");
							
							Date queryMonth = DateUtils.StringToDate(dependCycle.substring(0, 7).replaceAll("\\.", ""), "yyyyMM");
							String allFlow = trs.get(3).select("td").get(0).text().replaceAll("总流量：", "");
							int allTime =  TimeUtil.timetoint_HH_mm_ss(trs.get(3).select("td").get(1).text().replaceAll("总时长：", "").replaceAll("小时", ":").replaceAll("分", ":").replaceAll("秒", ""));
							String allPay = trs.get(4).select("td").get(0).text().replaceAll("总费用：", "").replaceAll("（元）", "");
							BigDecimal allFlows=new BigDecimal(com.lkb.util.StringUtil.flowFormat(allFlow));
							BigDecimal allPays = new BigDecimal(allPay);
							DianXinFlow dianXinFlow = new DianXinFlow();
							dianXinFlow.setAllFlow(allFlows);
							dianXinFlow.setAllPay(allPays);
							dianXinFlow.setAllTime(new BigDecimal(allTime));
							dianXinFlow.setDependCycle(dependCycle);
							dianXinFlow.setPhone(phoneNo);
							dianXinFlow.setQueryMonth(queryMonth);
							flowList.add(dianXinFlow);
						}
					} catch (Exception e1) {
						logger.info(phoneNo+":"+data.get("dstr")+" FlowBillError!", e1);
						
					}
					try {
						Elements table = doc.select("table.fyb");
						if (table==null||table.equals("")) {
							return;
						}else {
							Elements trs = table.select("tr");
							for (int j = 2; j < trs.size(); j++) {
								Elements tds = trs.get(j).select("td");
								if (tds.size()==9) {
									String beginTime = tds.get(2).text().trim();// 开始时间
									int tradeTime = TimeUtil.timetoint_HH_mm_ss(tds.get(3).text().trim());// 上网时长
									int flow = Integer.parseInt(tds.get(4).text().trim());// 总流量
									String netType = tds.get(5).text().trim();// 通讯类型
									String location = tds.get(6).text().trim();// 上网地市
									String business = tds.get(7).text().trim().replaceAll("-", "");// 使用业务
									String fee = tds.get(8).text().trim().replaceAll("元", "");// 费用（元）
									Date beginTimeDate = null;
									BigDecimal feeDecimal = new BigDecimal(0);
									try {
										beginTimeDate = DateUtils.StringToDate(beginTime,
												"yyyy-MM-dd HH:mm:ss");
										feeDecimal = new BigDecimal(fee);
									} catch (Exception e) {
										logger.error("error",e);
									}
									DianXinFlowDetail obj = new DianXinFlowDetail();
									obj.setPhone(phoneNo);
									obj.setBeginTime(beginTimeDate);
									obj.setFee(feeDecimal);
									obj.setNetType(netType);
									obj.setTradeTime(tradeTime);
									BigDecimal flows = new BigDecimal(0);
									try {
										flows = new BigDecimal(flow);
									} catch (Exception e) {
										logger.error("error",e);
									}
									obj.setFlow(flows);
									obj.setLocation(location);
									obj.setBusiness(business);
									flowDetailList.add(obj);
								}
							}
						}
					} catch (Exception e) {
						logger.info(phoneNo+":"+data.get("dstr")+" FlowDetailError!",e);
					}
					
				}
			});
		}
	}
	private void requestDownloadCallLog(SimpleObject context, String number, String queryDate) {		
		spider.addUrl(new AbstractProcessorObserver(util, WaringConstaint.GDDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveDownloadCallLog(context);
			}
		}, "http://gd.189.cn/cloudbill/downLoadCLOUDBILLTXT.action?downtype=txt&number=" + number + "&querydate=" + queryDate + "&listingType=call&total_charge=&number200201=&pageSize=9999&startVisitDate=&endVisitDate=");
	}
	private void saveDownloadMessageLog(SimpleObject context){
		String rt = ContextUtil.getContent(context);
		//System.out.println(rt);
		if (!rt.contains("短信清单查询结果")) {
			return;
		}
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
		//System.out.println(rows);
		if (rows != null) {
			int i = 0;
			//String laststr = null;
			//boolean first = false;
			for(String row : rows) {
				if (!StringUtil.isBlank(row)) {
					try {
						String[] cols = row.split(" ");
						boolean hasContent = false;
						//int startIndex = 0;
						if (cols != null) {						
							for(String col : cols) {
								if (col != null && !StringUtil.isBlank(col)) {
									hasContent = true;
									break;
								}
								//startIndex ++;
							}
						}
						
						if (hasContent) {
							i++;
							if (i >= 4) {
								//StringBuilder sb = new StringBuilder();
								TelcomMessage tMessage = new TelcomMessage();
								tMessage.setPhone(phoneNo);
								int j = 1;
								String dstr = null;
								try {
									for(String col : cols) {
										if (col != null && !StringUtil.isBlank(col)) {
											j++;
											//sb.append(j++ + "=[" + col + "] ");
											if (j == 2) {
												tMessage.setPhone(col);
											} else if (j == 3) {
												tMessage.setRecevierPhone(col);
											} else if (j == 4) {
												dstr = col;
											} else if (j == 5) {
												tMessage.setSentTime(DateUtils.StringToDate(dstr + " " + col, "yyyy-MM-dd HH:mm:ss"));
											} else if (j == 6) {
												tMessage.setAllPay(Double.parseDouble(col));
											} 
											tMessage.setBusinessType("发送");
										}
									}
								} catch (Exception e) {
									logger.error("error",e);
								}
								//j = 9 end
								/*if (first && j < 8) {
									data.put("通话记录结束", laststr);								
									data.put("记录数", i);								
								}
								laststr = sb.toString();
								if (!first && j > 8) {
									data.put("通话记录开始", laststr);
									first = true;
								}*/
								if (j > 6) {
									Date now = new Date();
									tMessage.setCreateTs(now);
									messageList.add(tMessage);
								}
								//logger.info(i + " start: " + sb);
							}
							/*if (i == 2) {
								data.put("通话清单查询结果", cols[2]);
								//logger.info(i + cols[2]);
							}*/
							
							
						}
					} catch (Exception e) {
						logger.error(row, e);
					}
					
				}
			}
		}
	}
	private void saveDownloadCallLog(SimpleObject context) {
		//下载帐单
		String rt = ContextUtil.getContent(context);
		if (rt.indexOf("没有该号码的清单数据") >= 0) {
			return;
		}
		//page.putField("html", rt);
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
			//String laststr = null;
			//boolean first = false;
			for(String row : rows) {
				if (!StringUtil.isBlank(row)) {
					try {
						String[] cols = row.split(" ");
						boolean hasContent = false;
						//int startIndex = 0;
						if (cols != null) {						
							for(String col : cols) {
								if (col != null && !StringUtil.isBlank(col)) {
									hasContent = true;
									break;
								}
								//startIndex ++;
							}
						}
						
						if (hasContent) {
							i++;
							if (i >= 4) {
								//StringBuilder sb = new StringBuilder();
								DianXinDetail dxDetail = new DianXinDetail();
								dxDetail.setPhone(phoneNo);
								int j = 1;
								String dstr = null;
								try {
									for(String col : cols) {
										if (col != null && !StringUtil.isBlank(col)) {
											j++;
											//sb.append(j++ + "=[" + col + "] ");
											if (j == 2) {
												dxDetail.setTradeType(col);
											} else if (j == 3) {
												dxDetail.setRecevierPhone(col);
											} else if (j == 4) {
												dstr = col;
											} else if (j == 5) {
												dxDetail.setcTime(DateUtils.StringToDate(dstr + " " + col, "yyyy-MM-dd HH:mm:ss"));
											} else if (j == 6) {
												dxDetail.setTradeTime(new TimeUtil().timetoint(col));
											} else if (j == 7) {
												dxDetail.setAllPay(new BigDecimal(col));
											} else if (j == 8) {
												dxDetail.setCallWay(col);
											} else if (j == 9) {
												dxDetail.setTradeAddr(col);
											}
										}
									}
								} catch (Exception e) {
									logger.error("error",e);
								}
								//j = 9 end
								/*if (first && j < 8) {
									data.put("通话记录结束", laststr);								
									data.put("记录数", i);								
								}
								laststr = sb.toString();
								if (!first && j > 8) {
									data.put("通话记录开始", laststr);
									first = true;
								}*/
								if (j > 8) {
									
									detailList.add(dxDetail);
								}
								//logger.info(i + " start: " + sb);
							}
							/*if (i == 2) {
								data.put("通话清单查询结果", cols[2]);
								//logger.info(i + cols[2]);
							}*/
							
							
						}
					} catch (Exception e) {
						logger.error(row, e);
					}
					
				}
			}
		}
	}
	private static String regex(String regex, String text, int group) {
    	Matcher m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
           return m.group(group);
           //System.out.println(text1);
        }       
        return null;
    }
	public static void main(String[] args) throws Exception {
		String phoneNo = "18127819800";
		String password = "195307";
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		GuangDongDianxin dx = new GuangDongDianxin(spider, null, phoneNo, password,"", null);
		dx.goLoginReq();
		spider.start();
		//dx.requestService(context)
		dx.printData();
		if (true) {
			return;
		}

	}
	@Override
	protected void onCompleteLogin(SimpleObject context) {
		// TODO Auto-generated method stub
		
	}
}
