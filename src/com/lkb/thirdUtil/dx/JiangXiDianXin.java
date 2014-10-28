package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
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
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

public class JiangXiDianXin extends AbstractDianXinCrawler {

	public JiangXiDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	public JiangXiDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		//spider.getSite().setCharset("utf-8");
	}
	public JiangXiDianXin() {
		areaName = "江西";
		customField1 = "3";
		customField2 = "15";
		
		toStUrl = "&toStUrl=http://jx.189.cn/SsoAgent?returnPage=yecx";
		shopId = "10015";
	}
	
	public void checkVerifyCode(final String userName) {   
		saveVerifyCode("jiangxi", userName);
    }
	public void sendSmsPasswordForRequireCallLogService() {
		postUrl("http://he.189.cn/queryCheckSecondPwdAction.action", "	http://he.189.cn/group/bill/bill_billlist.do", new String[][] {{"inventoryVo.accNbr", phoneNo}, {"inventoryVo.productId", "8"}}, new AbstractProcessorObserver(util, WaringConstaint.JXDX_5){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					String text = ContextUtil.getContent(context); 
					if (text != null && text.indexOf("请输入验证码") >= 0) {
						setStatus(STAT_SUC);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("error",e);
				}
			}
		});
		
	}
	/*
	public void verifySmsCode() {
		Date d = new Date();
		String dstr = DateUtils.formatDate(d, "yyyyMM");
		String xml = "<buffalo-call><method>queryDetailBill</method><map><type>java.util.HashMap</type><string>PRODNUM</string><string>MVWCdY0vDvFeiJ7Pmdzsqg==</string><string>CITYCODE</string><string>0898</string><string>QRYDATE</string><string>" + dstr + "</string><string>TYPE</string><string>8</string><string>PRODUCTID</string><string>50</string><string>CODE</string><string>631627</string><string>USERID</string><string>10158069</string></map></buffalo-call>";
		postUrl("http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.JXDX_7){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
					Elements rect = doc.select("string");
					String ac = rect.get(rect.size() - 1).text();
					requestAllService();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("error",e);
				}
			}
		});
		
	}*/
	//https://uam.ct10000.com/ct10000uam-gate/SSOFromUAM?ReturnURL=687474703A2F2F686E2E3138392E636E3A38302F686E73656C66736572766963652F75616D6C6F67696E2F75616D2D6C6F67696E2175616D4C6F67696E5265742E616374696F6E3F7255726C3D2F686E73656C66736572766963652F62696C6C71756572792F62696C6C2D71756572792173686F77546162732E616374696F6E3F746162496E6465783D33&ProvinceId=19

	protected void onCompleteLogin(SimpleObject context) {
		if(ContextUtil.getContent(context).contains("您的可用积分为")){
			setStatus(STAT_LOGIN_SUC);
			notifyStatus();
			initDwr();
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
		Date d = new Date();
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			requestCallLogService(1, 1, cd);	//抓取通话记录
			requestSmsLogService(1, 1, cd);		//抓取短信记录
		}
		requestUserInfo();	//抓取用户信息
		List<String> months = DateUtils.getMonthsNotInclude(6, "yyyyMM");	
		for (String month : months) {
			Date de = DateUtils.StringToDate(month, "yyyyMM");
			requestMonthBillService(month);		//抓取账单信息
			requestFlowService(1, 1, de);		//抓取流量
		}
	}	
	
	private void requestBillBalance(){
		String scriptSessionId = entity.getString("scriptSessionId");
		String[][] pairs = {{"callCount", "1"}, {"page", "/service/bill/customerbill/?bill=balance"}, {"httpSessionId", ""}, {"scriptSessionId", scriptSessionId}, {"c0-scriptName", "Service"}, 
				{"c0-methodName", "excute"}, {"c0-id", "0"}, {"c0-param0", "string:TWB_ACCEPT"}, {"c0-param1", "boolean:false"}, {"c0-e1", "string:GET_PRODUCT_INFO"},
				{"c0-e2", "string:ALL"},  {"c0-param2", "Object_Object:{method:reference:c0-e1, filter_product_id:reference:c0-e2}"},
				{"batchId", "0"}};
		String postBody = joinPairsToPostBodyForDWR(pairs);
		String[] param = new String[4];
		param[1] = postBody;
		param[3] = "text/plain";
		postUrl("http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr", "http://jx.189.cn/service/bill/customerbill/?bill=balance", param, null, new AbstractProcessorObserver( util, WaringConstaint.JXDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveBillBalance_1(context);
			}
		});
	}
	
	private void saveBillBalance_1(SimpleObject context){
		String text = ContextUtil.getContent(context);
		if(text == null)
			return;
		int index = text.indexOf("\r\n");
		String[] rows = null;
		if (index >= 0) {
			rows = text.split("\r\n");
		} else {
			index = text.indexOf("\n");
			if (index >= 0) {
				rows = text.split("\n");
			} else {
				rows = text.split("\r");
			}
		}
		if (rows != null) {
			if (rows.length <= 2) {
				return;
			}
			String[][] pairs = null;
			for(String row : rows) {
				if (!StringUtil.isBlank(row)) {
					try {
						String[] cols = row.split(";");
						if (cols != null) {			
							if (cols[0].indexOf("stime=") >= 0){
								int len = cols.length;
								pairs = new String[len][2] ;
								for(int j = 0; j < len; j++) {
									String col = cols[j];
									if (col != null && !StringUtil.isBlank(col)) {
										String key = "";
										if(col.indexOf("[\'") >= 0){
											key = col.substring(col.indexOf("[\'")+2, col.indexOf("\']"));
										}else if(col.indexOf(".") >= 0){
											key = col.substring(0, col.indexOf("="));
										}
										String value = col.substring(col.indexOf("\"")+1, col.lastIndexOf("\""));
										pairs[j][0] = key;
										pairs[j][1] = value;
									}
								}
							}
						}

					} catch (Exception e) {
						logger.error(row, e);
					}
				}
			}
			if(pairs == null)
				return;
			postUrl("http://jx.189.cn/service/bill/customerbill/balance_uiForyue.jsp", "http://jx.189.cn/service/bill/customerbill/index.jsp?bill=balance", pairs, new AbstractProcessorObserver( util, WaringConstaint.JXDX_5) {
				@Override
				public void afterRequest(SimpleObject context) {
					saveBillBalance_2(context);
				}
			});
		}
	}
	
	private void saveBillBalance_2(SimpleObject context){
		String text = ContextUtil.getContent(context).trim();
		if(text == null)
			return;
		String yue = com.lkb.util.StringUtil.subStr("您的账户现金余额", "元", text);
		yue = com.lkb.util.StringUtil.subStr("<font color=red>", "</font>", yue);
		if(yue.equals("")){
			yue = "0";
		}
		user.setPhoneRemain(new BigDecimal(yue));
		
		/*
		 * 抓取当月账单
		 */
		String str = ContextUtil.getDocumentOfContent(context).text().trim();
		String thisMonth = com.lkb.util.StringUtil.subStr("截止到", "，", str);
		String thisMonthBill = com.lkb.util.StringUtil.subStr("您本月已消费为", "元", str);
		if(thisMonthBill.equals("")){
			thisMonthBill = "0";
		}
		
		DianXinTel dxt = new DianXinTel();
		dxt.setTeleno(phoneNo);
		dxt.setcName(user.getRealName());
		String thisTime = thisMonth.substring(0,7).replaceAll("年", "/").replaceAll("月", "");
		dxt.setcTime(DateUtils.StringToDate(thisTime + "/01", "yyyy/MM/dd"));
		
		dxt.setDependCycle(thisTime + "/01"+"-"+DateUtils.formatDate(DateUtils.StringToDate(thisTime, "yyyy/MM"), "yyyy/MM/dd"));
		dxt.setcAllPay(new BigDecimal(thisMonthBill));
		
		telList.add(dxt);
	}
	
	private void requestMonthBillService(String month) {
		getUrl("http://jx.189.cn/service/bill/e_billing?month=" + month, "http://jx.189.cn/service/bill/customerbill/index.jsp?bill=balance", new AbstractProcessorObserver(util, WaringConstaint.JXDX_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveMonthBill(context);
			}
		});
	}
	
	private void saveMonthBill(SimpleObject context){
		String text = ContextUtil.getContent(context);
		if(text == null)
			return;
		DianXinTel dxTel = new DianXinTel();
		Document doc = Jsoup.parse(text);
		
		Elements baseInfos = doc.select("[style=COLOR: #0057a7]");	//抓取账单基本信息（客户名称、计费周期、打印时间）
		int i = 1;	//switch判断临时变量
		for (Element baseInfo : baseInfos) {
			String temp = baseInfo.text();
			String[] splits = temp.split("：");
			String info = splits[1];
			switch (i) {
				case 1:
					dxTel.setcName(info);
					i++;
					break;
				case 2:
					dxTel.setDependCycle(info);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
					Date cTime;
					try {
						cTime = sdf.parse(info.substring(0, 7));
						dxTel.setcTime(cTime);
					} catch (ParseException e) {
						logger.error("error",e);
					}
					i++;
					break;
			}
		}
		
		Elements tables = doc.select("[bgcolor=#d8d8d8]");	//抓取账单明细（套餐月基本费、天翼套餐费、来电显示月基本费、 省际漫游通话费等）
		if(tables.size() > 0){	
			Element table = tables.get(0);	//抓取账单明细（套餐月基本费、天翼套餐费、来电显示月基本费、 省际漫游通话费等）
			Elements tds = table.getElementsByTag("td");
			
			if(tds.size() > 0){
				String tcyjbf = "";	//套餐月基本费
				String tytcf = "";	//天翼套餐费
				String ldxsyjbf = "";	//来电显示月基本费
				String bdthf = "";	//本地通话费
				String sjmythf = "";	//省际漫游通话费
				String xj = "";	//本项小计
				String hj = "";	//本期费用合计
				
				for(int j=0;j<tds.size();j++){
					if(tds.get(j).text().indexOf("套餐月基本费") != -1){
						tcyjbf = tds.get(j+1).text();
					}else if(tds.get(j).text().indexOf("天翼套餐费") != -1){
						tytcf = tds.get(j+1).text();
					}else if(tds.get(j).text().indexOf("来电显示月基本费") != -1){
						ldxsyjbf = tds.get(j+1).text();
					}else if(tds.get(j).text().indexOf("省际漫游通话费") != -1){
						sjmythf = tds.get(j+1).text();
					}else if(tds.get(j).text().indexOf("本地通话费") != -1){
						bdthf = tds.get(j+1).text();
					}else if(tds.get(j).text().indexOf("本项小计") != -1){
						String td = tds.get(j+1).toString();
						xj = com.lkb.util.StringUtil.subStr("<strong>", "</strong>", td);
					}else if(tds.get(j).text().indexOf("本期费用合计") != -1){
						String td = tds.get(j).toString();
						hj = com.lkb.util.StringUtil.subStr("td_div_span2\">", "</span>", td);
					}
				}
				
				
				
				
				dxTel.setTeleno(phoneNo);
				xj = (!"".equals(xj))?xj:"0";
				tcyjbf = (!"".equals(tcyjbf))?tcyjbf:"0";
				bdthf = (!"".equals(bdthf))?bdthf:"0";
				ldxsyjbf = (!"".equals(ldxsyjbf))?ldxsyjbf:"0";
				sjmythf = (!"".equals(sjmythf))?sjmythf:"0";
	
				dxTel.setcAllPay(new BigDecimal(xj));
				dxTel.setZtcjbf(new BigDecimal(tcyjbf));
				dxTel.setLdxsf(new BigDecimal(ldxsyjbf));
				dxTel.setMythf(new BigDecimal(sjmythf));
				dxTel.setBdthf(new BigDecimal(bdthf));	//目前测试数据中无此项	2014/09/11
			}
			if(dxTel != null){
				telList.add(dxTel);
			}
		}
	}
	
	
	private void requestUserInfo(){
		getUrl("http://jx.189.cn/service/account/seeInfo.jsp", "http://jx.189.cn/service/bill/customerbill/index.jsp?bill=customerBill", new AbstractProcessorObserver(util, WaringConstaint.JXDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveUserInfo(context);
			}
		});
		requestBillBalance();	//抓取账户余额
	}
	
	
	private void saveUserInfo(SimpleObject context){
		String text = ContextUtil.getContent(context);
		if(text != null){
			Document doc = Jsoup.parse(text);
			String CUST_NAME = doc.select("#CUST_NAME").val();//获取客户名称
//			String CERT_TYPE = doc.select("#CERT_TYPE").val();//获取证件类型
			String CERT_NUM = doc.select("#CERT_NUM").val();//获取证件号码
//			String CUST_ADDR = doc.select("#CUST_ADDR").val();//获取客户地址
//			String CONTACT_NAME = doc.select("#CONTACT_NAME").val();//获取联系人姓名
			String MOBILE_PHONE = doc.select("#MOBILE_PHONE").val();//获取联系电话
			String EMAIL_ADDRESS = doc.select("#EMAIL_ADDRESS").val();//获取email
//			String POST_CODE = doc.select("#POST_CODE").val();//获取邮政编码
			String POST_ADDR = doc.select("#POST_ADDR").val();//获取通信地址
			
			user.setUserName(MOBILE_PHONE);
			user.setPhone(MOBILE_PHONE);
			user.setEmail(EMAIL_ADDRESS);
			user.setRealName(CUST_NAME);
			user.setIdcard(CERT_NUM);
			user.setAddr(POST_ADDR);
		}
	}
	
	
	private void requestSmsLogService(final int page, final int t, final Date d) {
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyyMM");	
		String scriptSessionId = entity.getString("scriptSessionId");
		String[][] pairs = {{"callCount", "1"}, {"page", "/service/bill/customerbill/?bill=balance"}, {"httpSessionId", ""}, {"scriptSessionId", scriptSessionId}, {"c0-scriptName", "Service"}, 
				{"c0-methodName", "excute"}, {"c0-id", "0"}, {"c0-param0", "string:TWB_GET_MONTH_DETAIL_BILL"}, {"c0-param1", "boolean:false"}, {"c0-e1", "string:myPage"},
				{"c0-e2", "string:myPage_table"}, {"c0-e3", "string:TWB_GET_MONTH_DETAIL_BILL"}, {"c0-e4", "boolean:false"}, {"c0-e5", "string:15"}, {"c0-e6", "string:1"}, {"c0-e7", "null:null"},
				{"c0-e8", "boolean:false"}, {"c0-e9", "null:null"}, {"c0-e10", "string:-1"}, {"c0-e11", "string:" + phoneNo}, {"c0-e12", "string:0"}, {"c0-e13", "string:" + bd}, {"c0-e14", "string:8"},
				{"c0-e15", "string:10"}, {"c0-e16", "string:1"}, {"c0-e17", "string:1"}, {"c0-e18", "string:yes"},  {"c0-param2", "Object_Object:{div_id:reference:c0-e1, table_id:reference:c0-e2, func_id:reference:c0-e3, is_sql:reference:c0-e4, page_size:reference:c0-e5, page_index:reference:c0-e6, exp_excel:reference:c0-e7, hide_pager:reference:c0-e8, class_name:reference:c0-e9, area_code:reference:c0-e10, acc_nbr:reference:c0-e11, service_type:reference:c0-e12, inYearMonth:reference:c0-e13, queryContent:reference:c0-e14, deviceType:reference:c0-e15, sortingOrder:reference:c0-e16, write_order:reference:c0-e17, need_check_session:reference:c0-e18}"},
				{"batchId", "18"}};
		String postBody = joinPairsToPostBodyForDWR(pairs);
		String[] param = new String[4];
		param[1] = postBody;
		param[3] = "text/plain";
		postUrl("http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr", "http://jx.189.cn/service/bill/customerbill/?bill=balance", param, null, new AbstractProcessorObserver( util, WaringConstaint.JXDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveSmsLog(context, d);
			}
		});
	}
	
	private void saveSmsLog(SimpleObject context, Date d) {
		//下载帐单
		String rt = ContextUtil.getContent(context);
		if (rt == null) {
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
		if (rows != null) {
			if (rows.length <= 2) {
				return;
			}
			for(String row : rows) {
				TelcomMessage telMessage = new TelcomMessage();
				if (!StringUtil.isBlank(row)) {
					try {
						row = new String(row.getBytes(),"UTF-8");
						String[] cols = row.split(";");
						if (cols != null) {			
							if (cols[0].indexOf(".fee=") >= 0){
								int len = cols.length;
								for(int j = 0; j < len; j++) {
									String text = cols[j];
									if (text != null && !StringUtil.isBlank(text)) {
										text = text.substring(text.indexOf("\"")+1, text.lastIndexOf("\""));
										text = com.lkb.util.StringUtil.convertUTF(text);
										if (j == 0) {
											telMessage.setAllPay(Double.parseDouble(text));
										} else if (j == 3) {
											telMessage.setSentTime(DateUtils.StringToDate(text, "yyyy/MM/dd HH:mm:ss"));
										} else if (j == 4) {
											telMessage.setRecevierPhone(text);
										}
									}
								}
								telMessage.setPhone(phoneNo);
								if(telMessage != null)
									messageList.add(telMessage);
							}
						}

					} catch (Exception e) {
						logger.error(row, e);
					}
				}
			}
		}
	}
	private void requestCallLogService(final int page, final int t, final Date d) {
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyyMM");	
		String scriptSessionId = entity.getString("scriptSessionId");
		String[][] pairs = {{"callCount", "1"}, {"page", "/service/bill/customerbill/?bill=balance"}, {"httpSessionId", ""}, {"scriptSessionId", scriptSessionId}, {"c0-scriptName", "Service"}, 
				{"c0-methodName", "excute"}, {"c0-id", "0"}, {"c0-param0", "string:TWB_GET_MONTH_DETAIL_BILL_NEW"}, {"c0-param1", "boolean:false"}, {"c0-e1", "string:myPage"},
				{"c0-e2", "string:myPage_table"}, {"c0-e3", "string:TWB_GET_MONTH_DETAIL_BILL_NEW"}, {"c0-e4", "boolean:false"}, {"c0-e5", "string:15"}, {"c0-e6", "string:" + page}, {"c0-e7", "null:null"},
				{"c0-e8", "boolean:false"}, {"c0-e9", "null:null"}, {"c0-e10", "string:-1"}, {"c0-e11", "string:" + phoneNo}, {"c0-e12", "string:0"}, {"c0-e13", "string:" + bd}, {"c0-e14", "string:7"},
				{"c0-e15", "string:10"}, {"c0-e16", "string:1"}, {"c0-e17", "string:1"}, {"c0-e18", "string:"}, {"c0-e19", "string:"}, {"c0-e20", "string:yes"}, {"c0-param2", "Object_Object:{div_id:reference:c0-e1, table_id:reference:c0-e2, func_id:reference:c0-e3, is_sql:reference:c0-e4, page_size:reference:c0-e5, page_index:reference:c0-e6, exp_excel:reference:c0-e7, hide_pager:reference:c0-e8, class_name:reference:c0-e9, area_code:reference:c0-e10, acc_nbr:reference:c0-e11, service_type:reference:c0-e12, inYearMonth:reference:c0-e13, queryContent:reference:c0-e14, deviceType:reference:c0-e15, sortingOrder:reference:c0-e16, write_order:reference:c0-e17, call_type:reference:c0-e18, search_date:reference:c0-e19, need_check_session:reference:c0-e20}"},
				{"batchId", "4"}};
		String postBody = joinPairsToPostBodyForDWR(pairs);
		String[] param = new String[4];
		param[1] = postBody;
		param[3] = "text/plain";
		postUrl("http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr", "http://jx.189.cn/service/bill/customerbill/?bill=balance", param, null, new AbstractProcessorObserver( util, WaringConstaint.JXDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, d);
			}
		});
	}
	private void saveCallLog(SimpleObject context, Date d) {
		//下载帐单
		String rt = ContextUtil.getContent(context);
		if (rt == null) {
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
		if (rows != null) {
			if (rows.length <= 2) {
				return;
			}
			for(String row : rows) {
				DianXinDetail dxDetail = new DianXinDetail();
				if (!StringUtil.isBlank(row)) {
					try {
						row = new String(row.getBytes(),"UTF-8");
						String[] cols = row.split(";");
						if (cols != null) {			
							if (cols[0].indexOf("times_int") >= 0){
								int len = cols.length;
								for(int j = 0; j < len; j++) {
									String text = cols[j];
									if (text != null && !StringUtil.isBlank(text)) {
										text = text.substring(text.indexOf("\"")+1, text.lastIndexOf("\""));
										text = com.lkb.util.StringUtil.convertUTF(text);
										if (j == 0) {
											dxDetail.setTradeTime(Integer.parseInt(text));
										} else if (j == 11) {
											dxDetail.setTradeType(text);
										} else if (j == 12) {
											dxDetail.setCallWay(text);
										} else if (j == 6) {
											dxDetail.setTradeAddr(text);
										} else if (j == 5) {
											dxDetail.setAllPay(new BigDecimal(text));
										} else if (j == 2) {
											dxDetail.setcTime(DateUtils.StringToDate(text, "yyyy/MM/dd HH:mm:ss"));
											if(text.substring(0, 7) == DateUtils.formatDate(new Date(), "yyyy/MM") || text.substring(0, 7).equals(DateUtils.formatDate(new Date(), "yyyy/MM"))){
												dxDetail.setIscm(1);
											}else{
												dxDetail.setIscm(0);
											}
										} else if (j == 10) {
											dxDetail.setRecevierPhone(text);
										} else if (j == 3) {
											dxDetail.setBasePay(new BigDecimal(text.length() == 0 ? "0" : text));
										} else if (j == 4) {
											dxDetail.setLongPay(new BigDecimal(text.length() == 0 ? "0" : text));
										} else if (j == 1) {
											dxDetail.setOtherPay(new BigDecimal(text.length() == 0 ? "0" : text));
										}
									}
								}
								dxDetail.setPhone(phoneNo);
								if(dxDetail != null)
									detailList.add(dxDetail);
							}
						}

					} catch (Exception e) {
						logger.error(row, e);
					}
				}
			}
		}
	}
	
	private void requestFlowService(final int page, final int t, final Date d) {
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyyMM");	
		String scriptSessionId = entity.getString("scriptSessionId");
		String[][] pairs = {{"callCount", "1"}, {"page", "/service/bill/customerbill/?bill=balance"}, {"httpSessionId", ""}, {"scriptSessionId", scriptSessionId}, {"c0-scriptName", "Service"}, 
				{"c0-methodName", "excute"}, {"c0-id", "0"}, {"c0-param0", "string:TWB_GET_MONTH_DETAIL_BILL"}, {"c0-param1", "boolean:false"}, {"c0-e1", "string:myPage"},
				{"c0-e2", "string:myPage_table"}, {"c0-e3", "string:TWB_GET_MONTH_DETAIL_BILL"}, {"c0-e4", "boolean:false"}, {"c0-e5", "string:15"}, {"c0-e6", "string:1"}, {"c0-e7", "null:null"},
				{"c0-e8", "boolean:false"}, {"c0-e9", "null:null"}, {"c0-e10", "string:-1"}, {"c0-e11", "string:" + phoneNo}, {"c0-e12", "string:0"}, {"c0-e13", "string:" + bd}, {"c0-e14", "string:0"},
				{"c0-e15", "string:10"}, {"c0-e16", "string:1"}, {"c0-e17", "string:1"}, {"c0-e18", "string:yes"},  {"c0-param2", "Object_Object:{div_id:reference:c0-e1, table_id:reference:c0-e2, func_id:reference:c0-e3, is_sql:reference:c0-e4, page_size:reference:c0-e5, page_index:reference:c0-e6, exp_excel:reference:c0-e7, hide_pager:reference:c0-e8, class_name:reference:c0-e9, area_code:reference:c0-e10, acc_nbr:reference:c0-e11, service_type:reference:c0-e12, inYearMonth:reference:c0-e13, queryContent:reference:c0-e14, deviceType:reference:c0-e15, sortingOrder:reference:c0-e16, write_order:reference:c0-e17, need_check_session:reference:c0-e18}"},
				{"batchId", "14"}};
		String postBody = joinPairsToPostBodyForDWR(pairs);
		String[] param = new String[4];
		param[1] = postBody;
		param[3] = "text/plain";
		postUrl("http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr", "http://jx.189.cn/service/bill/customerbill/?bill=balance", param, null, new AbstractProcessorObserver( util, WaringConstaint.JXDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveFlow(context, d);
			}
		});
	}
	
	private void saveFlow(SimpleObject context, Date d){
		String rt = ContextUtil.getContent(context);
		if (rt == null) {
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
		if (rows != null) {
			if (rows.length <= 2) {
				return;
			}
			for(String row : rows) {
				if (!StringUtil.isBlank(row)) {
					try {
						row = com.lkb.util.StringUtil.decodeUnicode(row);
						String[] cols = row.split(";");
						if (cols != null) {	
							/*
							 * 江西电信流量账单
							 */
							if (cols[0].contains("total_time")){
								DianXinFlow dxf = new DianXinFlow();
								int len = cols.length;
								for(int i=0;i<len;i++){
									String text = cols[i];
									if (text != null && !StringUtil.isBlank(text)) {
										text = text.substring(text.indexOf("\"")+1, text.lastIndexOf("\""));
										switch (i) {
										case 0:
											dxf.setAllTime(new BigDecimal(com.lkb.util.StringUtil.flowTimeFormat(text)));
											break;
										case 1:
											break;
										case 2:
											dxf.setAllPay(new BigDecimal(text));
											break;
										case 3:
											dxf.setAllFlow(new BigDecimal(com.lkb.util.StringUtil.flowFormat(text)));
											break;
										}
									}
								}
								dxf.setPhone(phoneNo);
								dxf.setQueryMonth(d);
								flowList.add(dxf);
							}
							/*
							 * 江西电信流量详单
							 */
							if(cols[0].contains("fee")){
								DianXinFlowDetail dxfd = new DianXinFlowDetail();
								int len = cols.length;
								for(int i=0;i<len;i++){
									String text = cols[i];
									if (text != null && !StringUtil.isBlank(text)) {
										text = text.substring(text.indexOf("\"")+1, text.lastIndexOf("\""));
										switch (i) {
										case 0:
											dxfd.setFee(new BigDecimal(text));
											break;
										case 1:
											dxfd.setTradeTime(com.lkb.util.StringUtil.flowTimeFormat(text));
											break;
										case 2:
											dxfd.setNetType(text);
											break;
										case 3:
											dxfd.setLocation(text);
											break;
										case 4:
											dxfd.setFlow(new BigDecimal(com.lkb.util.StringUtil.flowFormat(text)));
											break;
										case 5:
											dxfd.setBusiness(text);
											break;
										case 6:
											dxfd.setBeginTime(DateUtils.StringToDate(text, "yyyy-MM-dd hh:mm:ss"));
											break;
										}
									}
								}
								dxfd.setPhone(phoneNo);
								flowDetailList.add(dxfd);
							}
						}
					} catch (Exception e) {
						logger.error(row, e);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		/*for(int i=0; i< 10; i++) {
			System.out.println(i + "=" + (int) (Math.random() * 1000 % 10));
		}
		if (true) {
			return;
		}*/
		String phoneNo = "18160710531";
		String password = "568060";
		
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		JiangXiDianXin dx = new JiangXiDianXin(spider, null, phoneNo, password, "2345", null);
		dx.setTest(true);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.getData().clear();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		dx.printData();
		
		dx.requestFlowService(1, 1, Calendar.getInstance().getTime());
		spider.start();
		

	}

	
	private void initDwr(){
		/*
		 * get请求engine.js并不是每一次都能请求成功
		 */
		
		/*getUrl("http://jx.189.cn/public/common/control/dwr/engine.js", null, new AbstractProcessorObserver(util, WaringConstaint.GDDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				Request req1 = ContextUtil.getRequest(context);
				Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
				if(scode == 302){
					initDwr();
					i++;
					if(i==10){
						logger.error("Error : 请求dwr引擎文件失败");
						return;
					}
				}else if(scode == 200){
					String text = ContextUtil.getContent(context);
					if(text != null){
						String n = com.lkb.util.StringUtil.subStr("/** The original page id sent from the server ", "*//** The session cookie name *//*", text);
						n = n.trim();
						String origScriptSessionId = com.lkb.util.StringUtil.subStr("\"","\";",n).trim();
						Integer random = (int) Math.floor(Math.random() * 1000);
						String scriptSessionId = origScriptSessionId + random;
						data.put("scriptSessionId", scriptSessionId);
					}
				}
			}
		});*/
		
			String origScriptSessionId = "AA0CBE9FB90164F9E0E55CF74FCC9338";
			Integer random = (int) Math.floor(Math.random() * 1000);
			String scriptSessionId = origScriptSessionId + random;
			entity.put("scriptSessionId", scriptSessionId);
	}
	
	private String getDwrJSResult(String text, String keyStart, String keyEnd) {
		String temp = com.lkb.util.StringUtil.subStr(keyStart, keyEnd, text);
		return temp;
	}
}
