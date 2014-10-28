package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

public class HeiLongJiangDianXin extends AbstractDianXinCrawler {
	
	public HeiLongJiangDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	public HeiLongJiangDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		spider.getSite().setCharset("utf-8");
	}
	public HeiLongJiangDianXin() {
		areaName = "黑龙江";
		customField1 = "3";
		customField2 = "10";
		toStUrl = "&toStUrl=http://hl.189.cn/service/zzfw.do?method=fycx";
		shopId = "10010";
		
	}
	
	//https://uam.ct10000.com/ct10000uam/validateImg.jsp
	public void checkVerifyCode(final String userName) {   
		saveVerifyCode("hlj", userName);
		/*String url = "https://uam.ct10000.com/ct10000uam/login?service=http://hlj.189.cn/dqmh/Uam.do?method=loginJTUamGet";
    	Request req = new Request(url);
    	//req.putExtra("redirectsEnabled", false);
    	//req.putHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    	
    	req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.HLJDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				data.put("checkVerifyCode","1");
				//data.put("aa" ,CookieStoreUtil.putContextToCookieStore(null, 1));
				//data.put("sessionId", ContextUtil.getCookieValue(context, "JSESSIONID"));
				//String sid = ContextUtil.getCookieValue(context, "JSESSIONID", "uam.ct10000.com");
				String picName =  "hlj_dx_code_" + userName + "_21.jpg";
				try {
					String imgName = saveFile("https://uam.ct10000.com/ct10000uam/validateImg.jsp", "https://uam.ct10000.com/", null, picName, true);
					//String imgName = "https://uam.ct10000.com/ct10000uam/validateImg.jsp;JSESSIONID=" + sid;
					data.put("imgName", imgName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("error",e);
				}
				//notifyStatus();
			}
		});
    	spider.addRequest(req);*/
    }
	/*public void sendSmsPasswordForRequireCallLogService() {       
		getUrl("http://sn.189.cn/service/bill/sendValidReq.action?listType=1&mobileNum=" + phoneNo, "http://sn.189.cn/service/bill/sendValidReq.action", new AbstractProcessorObserver(util, WaringConstaint.HLJDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					JSONObject obj = ContextUtil.getJsonOfContent(context);
					data.put("success", obj.getBoolean("success"));
					notifyStatus();
				} catch (JSONException e) {
					logger.error("error",e);
				}
				
			}
		});
		
	}*/
	protected void onCompleteLogin(SimpleObject context) {
		//sendSmsPasswordForRequireCallLogService();
		//printCookieData();
		//getUrl("https://uam.ct10000.com/ct10000uam-gate/SSOFromUAM?ReturnURL=&ProvinceId=" + customField2, null, null);
		
		if(ContextUtil.getContent(context).contains("uiss_loginmobile")){
			setStatus(STAT_SUC);
			notifyStatus();
			requestService();
		}else{
			setStatus(STAT_STOPPED_FAIL);
			data.put("errMsg", "登录失败，请重试！");
			notifyStatus();
			return;
		}
	}
	
	private void requestService() {
		/*ContextUtil.getTask(context).addUrl(new AbstractProcessorObserver(util, WaringConstaint.HLJDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
			}
		}, "http://www.ln.10086.cn/my/account/detailquery.xhtml");*/
		parseBalanceInfo();
		
		Date d = new Date();
		Calendar currentCalendar = Calendar.getInstance();
		Calendar testCalendar = Calendar.getInstance();
		currentCalendar.setTime(d);
		int currentMonth = currentCalendar.get(Calendar.MONTH);
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			Date monthly = cd;
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				monthly = sdf.parse(dstr);
			} catch (Exception e) {
				logger.error("error",e);
			}
			if (i > 0) {
				requestMonthBillService(cd, dstr);
			}
			testCalendar.setTime(cd);
			int testMonth = testCalendar.get(Calendar.MONTH);
			if(testMonth != currentMonth){
				requestFlowLogService(1, 4, monthly, dstr, false);
			}else{
				requestFlowLogServiceCurrent(1, 4, monthly, dstr, true);
			}
			requestMessageLogService(1, 5, cd, dstr);
			requestCallLogService(1, 0, cd, dstr);
			requestCallLogService(1, 1, cd, dstr);
			requestCallLogService(1, 2, cd, dstr);
		}
	}	
	
	
	private void parseBalanceInfo() {
		//http://ln.189.cn/getSessionInfo.action
		//String[][] params = {{"Action", "post"}, {"Name", "lulu"}};
		//{"accNbr":"18040212031","TSR_RESULT":"0","TSR_CODE":"0","restFee":24.04,"TSR_MSG":"","queryDay":30,"userName":"周灏","queryDate":"2014年07月30日"}
		
		getUrl("http://hl.189.cn/service/uiss_loginmobile.do", null, new String[]{"gbk"}, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_5){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				String userName = doc.select("span.zzfw_wdxx_boxspan").text().trim().split(" ")[0];
				user.setRealName(userName);
				final Elements es1 = doc.select("table.workbench_tale");//com.lkb.debug.DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
				if (es1.size() < 1) {
					return;
				}
				Element table = es1.get(0);
				//保存当月实时账单 jiangzongren  start   ContextInitializing
				BigDecimal currentMonthTel = new BigDecimal(0);
				try {
					currentMonthTel = new BigDecimal(table
							.select("tr").get(1).select("td").get(1).text()
							.replaceAll("元", "").trim());
				} catch (Exception e) {
					logger.error("获取当月实时花费     url=http://hl.189.cn/service/uiss_loginmobile.do");
				}
				DianXinTel tel = new DianXinTel();
				tel.setcTime(DateUtils.StringToDate(DateUtils.formatDate(new Date(), "yyyyMM"), "yyyyMM"));
				tel.setcAllPay(currentMonthTel);
				tel.setTeleno(phoneNo);
				telList.add(tel);
				//保存当月实时账单 jiangzongren  end
				
				Elements es = table.select("tr").get(0).select("td");
				BigDecimal b1= new BigDecimal(es.get(1).text().replaceAll("元", "").trim());
				addPhoneRemain(b1);
				
				Element zzxx_text2 = doc.select("[class=zzxx_text2]").get(0);
				Elements as = zzxx_text2.getElementsByTag("a");
				if(as.size() > 0){
					for (Element a : as) {
						String href = a.attr("href");
						if(a.text().equals("我的资料")){
							getUserInfo(href);
						}else if(a.text().equals("我的产品")){
							getUserPackege(href);
						}
					}
				}
			}
		});
	}
	
	/**
	* <p>Title: getUserInfo</p>
	* <p>Description: 抓取用户基本信息</p>
	* @author Jerry Sun
	* @param href
	*/
	private void getUserInfo(String href){
		String url = "http://hl.189.cn" + href;
		String[] param = new String[4];
		param[0] = "GBK";
		getUrl(url, "http://hl.189.cn/service/uiss_loginmobile.do", param, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				String[] param = new String[4];
				param[0] = "GBK";
				getUrl("http://hl.189.cn/service/crm_cust_info_show.do?funcName=custSupport&canAdd2Tool=canAdd2Tool", "http://hl.189.cn/service/show_crm_device_init_mwg.do?frameFlag=product", param, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_1) {
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil.getDocumentOfContent(context);
						Element form = doc.select("form[name=CrmCustInfoForm]").get(0);
						Elements tds = form.getElementsByTag("td");
						if(tds.size() > 0){
							user.setRealName(tds.get(1).text());
						}
//						String phone = form.select("input[name=crmCustInfoVO.contactPhone]").val();
						String addr = form.select("input[name=crmCustInfoVO.address]").val();
//						user.setPhone(phone);
						user.setAddr(addr);
					}
				});
			}
			
		});
	}
	
	/**
	* <p>Title: getUserPackege</p>
	* <p>Description: 抓取产品类型</p>
	* @author Jerry Sun
	* @param href
	*/
	private void getUserPackege(String href){
		String url = "http://hl.189.cn" + href;
		String[] param = new String[4];
		param[0] = "GBK";
		getUrl(url, "http://hl.189.cn/service/uiss_loginmobile.do", param, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				String[] param = new String[4];
				param[0] = "GBK";
				getUrl("http://hl.189.cn/service/show_crm_device_init.do?frameFlag=compages&canAdd2Tool=canAdd2Tool", "http://hl.189.cn/service/zzfw.do?method=khzlgl&id=19", param, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_1) {
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil.getDocumentOfContent(context);
						Elements trs = doc.getElementsByTag("tr");
						if(trs.size() > 0){
							Element tr = trs.get(1);
							Elements tds = tr.getElementsByTag("td");
							if(tds.size() > 0){
								String pacName = tds.get(1).text();
								user.setPackageName(tds.get(1).text());
							}
						}
					}
				});
			}
		});
	}
	
	private void requestMonthBillService(final Date d, final String dstr) {
		postUrl("http://hl.189.cn/service/billDateChoiceNew.do?method=doSearch&selectDate=" + dstr, "	http://hl.189.cn/service/billDateChoiceNew.do?method=doInit", new String[]{"gbk"}, null, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				//本期费用合计：   本期已付费用：
				String text = ContextUtil.getContent(context);
				if (text == null) {
					return;
				}
				String n = StringUtil.subStr("本期费用合计：", "本期已付费用：", text).replaceAll("</br>", "").trim();
				
				BigDecimal b1= new BigDecimal(n.length() == 0 ? "0" : n);
				DianXinTel tel = new DianXinTel();
				tel.setcTime(DateUtils.StringToDate(dstr, "yyyyMM"));
				tel.setcAllPay(b1);
				data.put("Month" + dstr, d);
				data.put("Pay" + dstr, n.trim());
				tel.setTeleno(phoneNo);
				telList.add(tel);
			}
		});
	}
	
	//Created by Dongyu.Zhang
	private void requestFlowLogService(final int page, final int t, final Date d, String dstr, final boolean isCurrentMonth){
		
		String bd = DateUtils.formatDate(d, "yyyyMM");
		
		//isMobile=0&seledType=4&queryType=1&pageSize=10&pageNo=1&flag=&accountNum=13351266898%3A2000004&callType=3&selectType=1&detailType=4&selectedDate=201409&detailType=4&method=queryCQDMain
		postUrl("http://hl.189.cn/service/cqd/queryFlowDetailList.do?isMobile=0&seledType=4&queryType=1&pageSize=10&pageNo=1&flag=&accountNum=" + phoneNo +":2000004&callType=3&selectType=1&detailType=4&selectedDate="+bd+"&detailType=4&method=queryCQDMain", null,new String[]{"gbk"}, null, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveFlowLog(context, page, d, isCurrentMonth);
			}
		});
	}
	
	//Created by Dongyu.Zhang
	private void requestFlowLogServiceCurrent(final int page, final int t, final Date d, String dstr, final boolean isCurrentMonth){
		
		String bd = DateUtils.formatDate(d, "yyyyMM");
		//http://hl.189.cn/service/cqd/queryDetailList.do
		//isMobile=0&seledType=4&queryType=2&pageSize=10&pageNo=1&flag=wx&accountNum=13351266898%3A2000004&callType=1&selectType=2&detailType=4&selectedDate=20149&detailType=4&method=queryCQDMain
		postUrl("http://hl.189.cn/service/cqd/queryDetailList.do?isMobile=0&seledType=4&queryType=2&pageSize=10&pageNo=1&flag=wx&accountNum=" + phoneNo +":2000004&callType=1&selectType=2&detailType=4&selectedDate="+bd+"&detailType=4&method=queryCQDMain", null,new String[]{"gbk"}, null, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveFlowLog(context, page, d, isCurrentMonth);
			}
		});
	}
	
	private void saveFlowLog(SimpleObject context, final int page, final Date d, final boolean isCurrentMonth){
		String text1 = ContextUtil.getContent(context);
		if(text1 == null || text1.equals("")){
			return;
		}
		try{
			Document doc = ContextUtil.getDocumentOfContent(context);
			//System.out.println(doc.toString());
			DianXinFlow flowBill = new DianXinFlow();
			flowBill.setPhone(phoneNo);
			UUID id = UUID.randomUUID();
			flowBill.setId(id.toString());
			if(isCurrentMonth){
				Elements tables = doc.select("table[id=tb1]");
				try{
					 SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
					 String endDay = formatter.format(new Date());
					 
					 Calendar c = Calendar.getInstance();
					 c.setTime(new Date());
					 c.set(Calendar.DAY_OF_MONTH, 1);
					 Date firstDay = c.getTime();
					 String startDay = formatter.format(firstDay);
					 
					 String currentScale = startDay + "-" + endDay;
					 
					 flowBill.setDependCycle(currentScale);
				 }catch(Exception e){
					 logger.error("error",e);
				 }
				if(tables==null||tables.toString().equals("")){
					flowBill.setQueryMonth(d);
					flowBill.setAllFlow(new BigDecimal(0));
					flowBill.setAllPay(new BigDecimal(0));
					flowBill.setAllTime(new BigDecimal(0));
					flowList.add(flowBill);
					
				} else {
					Elements trs = tables.select("tr");
					BigDecimal totalTime = new BigDecimal(0);
					BigDecimal totalFlow = new BigDecimal(0);
					BigDecimal totalFees = new BigDecimal(0);
					for(int i = 1; i < trs.size(); i++){
						Elements tds = trs.get(i).select("td");
						if (tds.size() > 1) {
							try {
								String kssj = trs.get(i).toString();// 开始时间
								RegexPaserUtil rp = new RegexPaserUtil(
										"new String\\(", "\\)", kssj,
										RegexPaserUtil.TEXTEGEXANDNRT);
								String startTime = rp.getText();
								String date = startTime.substring(0, 4) + "-"
										+ startTime.substring(4, 6) + "-"
										+ startTime.substring(6, 8) + " "
										+ startTime.substring(8, 10) + ":"
										+ startTime.substring(10, 12) + ":"
										+ startTime.substring(12, 14);
								Date time = DateUtils.StringToDate(date,
										"yyyy-MM-dd HH:mm:ss");
								String sc = tds.get(2).text()
										.replaceAll("秒", "");// 时长
								BigDecimal second = new BigDecimal(sc);
								totalTime = totalTime.add(second);

								String ll = tds.get(5).text();// 流量
								BigDecimal flow = new BigDecimal(ll);
								totalFlow = totalFlow.add(flow);

								String fy = tds.get(3).text().trim();// 费用
								BigDecimal fees = new BigDecimal(fy);
								fees = fees.divide(new BigDecimal(100));
								totalFees = totalFees.add(fees);

								DianXinFlowDetail dFlow = new DianXinFlowDetail();
								dFlow.setPhone(phoneNo);
								UUID uuid = UUID.randomUUID();
								dFlow.setId(uuid.toString());
								dFlow.setTradeTime(second.longValue());
								dFlow.setBeginTime(time);
								dFlow.setFee(fees);
								dFlow.setFlow(flow);
								dFlow.setIscm(1);
								flowDetailList.add(dFlow);
							} catch (Exception e) {
								logger.error("error", e);
							}
						}
					}
					
					flowBill.setQueryMonth(d);
					flowBill.setAllFlow(totalFlow);
					flowBill.setAllPay(totalFees);
					flowBill.setAllTime(totalTime);
					flowList.add(flowBill);
					
				}
				
			} else {
				Elements table = doc.select("table");
				Elements trs = table.select("tr");
				try {
					String flowScale = trs.get(2).select("td").get(0).text()
							.substring(5);
					String flowNumStr = trs.get(3).select("td").get(0).text()
							.substring(4).trim();
					Double flowNum = StringUtil.flowFormat(flowNumStr);
					String flowTimeStr = trs.get(3).select("td").get(1).text()
							.substring(4).trim();
					Long flowTime = StringUtil.flowTimeFormat(flowTimeStr);
					String flowFeeStr = trs.get(4).select("td").get(0).text();
					int endFlagIndex = flowFeeStr.indexOf('(');
					String flowFee = flowFeeStr.substring(4, endFlagIndex);
					flowBill.setDependCycle(flowScale);
					flowBill.setQueryMonth(d);
					flowBill.setAllFlow(new BigDecimal(flowNum));
					flowBill.setAllPay(new BigDecimal(flowFee));
					flowBill.setAllTime(new BigDecimal(flowTime));
					flowBill.setQueryMonth(d);
					flowList.add(flowBill);
				} catch (Exception e) {
					logger.error("error", e);
				}

				if (trs.size() > 7) {
					for (int i = 6; i < trs.size(); i++) {
						Elements tds = trs.get(i).select("td");
						if (tds.size() > 7) {
							String kssj = tds.get(1).text();// 开始时间
							String sc = tds.get(2).text();// 时长
							String ll = tds.get(3).text();// 流量
							String wllx = tds.get(4).text();// 网络类型
							String swds = tds.get(5).text();// 上网地市
							String syyw = tds.get(6).text();// 使用业务
							String fy = tds.get(7).text().trim();// 费用
							DianXinFlowDetail dFlow = new DianXinFlowDetail();
							dFlow.setPhone(phoneNo);
							UUID uuid = UUID.randomUUID();
							dFlow.setId(uuid.toString());
							Long tradeTime = StringUtil.flowTimeFormat(sc);
							dFlow.setTradeTime(tradeTime);
							dFlow.setBeginTime(DateUtils.StringToDate(kssj,
									"yyyy-MM-dd HH:mm:ss"));
							dFlow.setLocation(swds);
							dFlow.setFee(new BigDecimal(fy));
							dFlow.setBusiness(syyw);
							dFlow.setFlow(new BigDecimal(StringUtil
									.flowFormat(ll)));
								dFlow.setIscm(0);
							dFlow.setNetType(wllx);
							flowDetailList.add(dFlow);
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("flow" + text1, e);
		}
	}
	
	
	
	
	
	//Created by Dongyu.Zhang
	private void requestMessageLogService(final int page, final int t, final Date d, String dstr){
		
		String bd = DateUtils.formatDate(d, "yyyyMM");
		
		//isMobile=0&seledType=5&queryType=1&pageSize=10&pageNo=1&flag=&accountNum=13351266898%3A2000004&callType=3&selectType=1&detailType=5&selectedDate=20148&detailType=5&method=queryCQDMain
		//isMobile=0&seledType=5&queryType=2&pageSize=10&pageNo=1&flag=&accountNum=13351266898%3A2000004&callType=3&selectType=2&detailType=5&selectedDate=20148&detailType=5&method=queryCQDMain
		String[][] pairs = {{"accountNum", phoneNo + ":2000004"}, {"callType", "3"}, {"detailType", t + ""}, {"detailType", "-1"}, {"isMobile", "0"}, 
				{"method", "queryCQDMain"}, {"pageNo", "1"}, {"pageSize", "10"}, {"queryType", "1"}, {"selectedDate", bd}, {"selectType", "1"}, {"seledType", t + ""}};
		postUrl("http://hl.189.cn/service/cqd/queryDetailList.do", 
				"http://hl.189.cn/service/cqd/detailQueryCondition.do?method=query&accountNum="+ phoneNo +":2000004&selectedDate="+ bd +"&flag=", new String[]{"gbk"}, pairs, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveMessageLog(context, page, d);
			}
		});
	}
	private void saveMessageLog(SimpleObject context, final int page, final Date d){
		String text1 = ContextUtil.getContent(context);
		if(null == text1){
			return;
		}
		try{
			Document doc = ContextUtil.getDocumentOfContent(context); 			
			Elements table = doc.select("table#tb1");
			if (table.size() > 0) {				
				setStatus(STAT_SUC);
				notifyStatus();
				//utf-8
				Elements trs = table.select("tr");
				int len = trs.size();
				for(int i = 1; i < len; i++) {
					Elements tds = trs.get(i).select("td"); 
					if (tds.size() <= 2) {
						continue;
					}
					try {
						TelcomMessage tMessage = new TelcomMessage();
						tMessage.setPhone(tds.get(1).text());
						tMessage.setRecevierPhone(tds.get(2).text());
						String ts = StringUtil.subStr("var begtime = new String(", ");", trs.get(i).html());
						tMessage.setSentTime(DateUtils.StringToDate(ts, "yyyyMMddHHmmss"));
						tMessage.setAllPay(Double.parseDouble(tds.get(5).text()));
						tMessage.setBusinessType("点对点");
						Date now = new Date();
						tMessage.setCreateTs(now);
//						UUID uuid = UUID.randomUUID();
//						tMessage.setId(uuid.toString());					
						messageList.add(tMessage);
						
						
		}catch(Exception e){
			logger.error("message" + trs.get(i).html(), e);
		}
	}
			}
		}catch(Exception e){
			logger.error("message" + text1, e);
		}
	}
	private void requestCallLogService(final int page, final int t, final Date d, String dstr) {	
		//0 1 2
		String bd = DateUtils.formatDate(d, "yyyyM");
		
		//String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		//isMobile=0&seledType=1&queryType=1&pageSize=10&pageNo=1&flag=&accountNum=13351266898%3A2000004&callType=3&selectType=1&detailType=1&selectedDate=20147&detailType=-1&startDate=&endDate=&method=queryCQDMain

		String[][] pairs = {{"accountNum", phoneNo + ":2000004"}, {"callType", "3"}, {"detailType", t + ""}, {"detailType", "-1"}, {"isMobile", "0"}, 
				{"method", "queryCQDMain"}, {"pageNo", "1"}, {"pageSize", "10"}, {"queryType", "1"}, {"selectedDate", bd}, {"selectType", "1"}, {"seledType", t + ""}};
		postUrl("http://hl.189.cn/service/cqd/queryDetailList.do", 
				"http://hl.189.cn/service/cqd/detailQueryCondition.do?method=query&accountNum="+ phoneNo +":2000004&selectedDate="+ bd +"&flag=", new String[]{"gbk"}, pairs, new AbstractProcessorObserver(util, WaringConstaint.HLJDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, page, d);
			}
		});
	}
	private void saveCallLog(SimpleObject context, final int page, final Date d) {
		String text1 = ContextUtil.getContent(context);
		if (text1 == null) {
			return;
		}
		try {			
			Document doc = ContextUtil.getDocumentOfContent(context); 			
			Elements table = doc.select("table#tb1");
			if (table.size() > 0) {				
				setStatus(STAT_SUC);
				notifyStatus();
				//utf-8
				Elements trs = table.select("tr");
				int len = trs.size();
				for(int i = 1; i < len; i++) {
					Elements tds = trs.get(i).select("td"); 
					if (tds.size() <= 2) {
						continue;
					}
					try {
						DianXinDetail dxDetail = new DianXinDetail();
						dxDetail.setPhone(phoneNo);

						String text = tds.size() > 9 ? tds.get(9).text() : "";
						
						dxDetail.setTradeType(StringUtils.isBlank(text) && text.equals(tds.get(2).text()) ? "长途" : "本地");
						dxDetail.setRecevierPhone(tds.get(3).text());
						dxDetail.setTradeAddr(text);			
						String ts = StringUtil.subStr("var begtime = new String(", ");", trs.get(i).html());
						dxDetail.setcTime(DateUtils.StringToDate(ts, "yyyyMMddHHmmss"));
						dxDetail.setTradeTime(new TimeUtil().timetoint(tds.get(5).text()));
						dxDetail.setCallWay(tds.get(7).text());
						dxDetail.setAllPay(new BigDecimal(Long.parseLong(tds.get(6).text()) / 100d));
						//dxDetail.setBasePay(new BigDecimal(tds.get(6).text()));
						dxDetail.setLongPay(new BigDecimal(Long.parseLong(tds.get(8).text()) / 100d));
						dxDetail.setTradeAddr(text);
						//dxDetail.setOnlinePay(new BigDecimal(text));
						//dxDetail.setTradeWay(text);					
						detailList.add(dxDetail);
					} catch (Exception e) {
						logger.error("detail" + trs.get(i).html(), e);
					}
				}
				//requestCallLogService(page + 1, d);
			}

		} catch (Exception e) {
			logger.error("detail" + text1, e);
		}

	}

	public static void main(String[] args) throws Exception {
		String phoneNo = "13351266898";
		String password = "014814";
		
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		HeiLongJiangDianXin dx = new HeiLongJiangDianXin(spider, null, phoneNo, password, "2345", null);
		dx.setTest(true);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		dx.printData();
		dx.parseBalanceInfo();
		spider.start();
		if (true) {
			return;
		}

	}
}
