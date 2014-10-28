package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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

public class YunNanDianXin extends AbstractDianXinCrawler {
	
	public YunNanDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	public YunNanDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		//spider.getSite().setCharset("utf-8");
	}
	public YunNanDianXin() {
		areaName = "云南";
		customField1 = "3";
		customField2 = "25";
		//http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=yecx
		ssoUrl = "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10013&toStUrl=http://ah.189.cn/service/bill/fee.action?type=resto";
		shopId = "10001";
	}
	
	public void checkVerifyCode(final String phone) {   
		getUrl("http://yn.189.cn/", "", null);
		String prefix = "yunnan";
		//保存验证码图片
		data.put("checkVerifyCode","1");
		//DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
		String picName =  prefix + "_dx_code_" + phone + "_" + (int) (Math.random() * 1000) + "5tw";
		//http://yn.189.cn/public/v4/common/control/page/login_image.jsp?date=Sun Sep 21 18:36:27 UTC+0800 2014
		try {
			String imgName = saveFile("http://yn.189.cn/public/v4/common/control/page/login_image.jsp?date="+(new Date()).toString().replaceAll(" ", "%20").replaceAll("CST", "UTC+0800"), "http://yn.189.cn/login/login_pop.jsp", null, picName, true);
			data.put("imgName", imgName);
		} catch (Exception e) {
			notifyStatus();
		}
    }
	public void goLoginReq() {     
		getUrl("http://yn.189.cn/login/login_pop.jsp", "http://yn.189.cn/" , new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLoginPage1(context);
			}
		});
	}
	private void parseLoginPage1(SimpleObject context) {
		getUrl("http://yn.189.cn/public/queryinfo.jsp?_="+System.currentTimeMillis(), "http://yn.189.cn/", null);
		Document doc = ContextUtil.getDocumentOfContent(context); 
		if (doc == null) {
			return;
		}
		Elements form = doc.select("form#LOGON_FORM");
		//logon_action=&retUrl=%2Fservice%2Faccount%2Findex.jsp&LOGIN_TYPE=21&logon_name_mini=&logon_name_tel=&logon_passwd=231102&RAND_TYPE=001&LOGIN_TYPE_DATA=9&AREA_CODE=&logon_name=15398469671&PASSWD_TYPE=21&MINI_PASSWD_TYPE=26&TEL_PASSWD_TYPE=21&passwdUserNum=&passwdCustNum=&passwdCustCodeNum=&passwdRegisterNum=&passwdCdmaNum=&passwdCdmaUserNum=231102&passwdMiniNum=&passwdMiniUserNum=&passwdTelNum=&passwdTelUserNum=&logon_valid=d516
		String[][] pairs = new String[24][2];
		int i=0;
		pairs[i++] = new String[]{"logon_action", form.select("input[name=logon_action]").attr("value")};
		pairs[i++] = new String[]{"retUrl", form.select("input[name=retUrl]").attr("value")};
		pairs[i++] = new String[]{"LOGIN_TYPE", "21"};
		pairs[i++] = new String[]{"logon_name_mini", form.select("input[name=logon_name_mini]").attr("value")};
		pairs[i++] = new String[]{"logon_name_tel", form.select("input[name=logon_name_tel]").attr("value")};
		pairs[i++] = new String[]{"logon_passwd", password};
		pairs[i++] = new String[]{"RAND_TYPE", form.select("input[name=RAND_TYPE]").attr("value")};
		pairs[i++] = new String[]{"LOGIN_TYPE_DATA", form.select("input[name=LOGIN_TYPE_DATA]").attr("value")};
		pairs[i++] = new String[]{"AREA_CODE", ""};
		pairs[i++] = new String[]{"logon_name", phoneNo};
		pairs[i++] = new String[]{"PASSWD_TYPE", "21"};
		pairs[i++] = new String[]{"MINI_PASSWD_TYPE", "26"};
		pairs[i++] = new String[]{"TEL_PASSWD_TYPE", "21"};
		pairs[i++] = new String[]{"passwdUserNum", form.select("input[name=passwdUserNum]").attr("value")};
		pairs[i++] = new String[]{"passwdCustNum", form.select("input[name=passwdCustNum]").attr("value")};
		pairs[i++] = new String[]{"passwdCustCodeNum", form.select("input[name=passwdCustCodeNum]").attr("value")};
		pairs[i++] = new String[]{"passwdRegisterNum", form.select("input[name=passwdRegisterNum]").attr("value")};
		pairs[i++] = new String[]{"passwdCdmaNum", form.select("input[name=passwdCdmaNum]").attr("value")};
		pairs[i++] = new String[]{"passwdCdmaUserNum", password};
		pairs[i++] = new String[]{"passwdMiniNum", form.select("input[name=passwdMiniNum]").attr("value")};
		pairs[i++] = new String[]{"passwdMiniUserNum", form.select("input[name=passwdMiniUserNum]").attr("value")};
		pairs[i++] = new String[]{"passwdTelNum", form.select("input[name=passwdTelNum]").attr("value")};
		pairs[i++] = new String[]{"passwdTelUserNum", form.select("input[name=passwdTelUserNum]").attr("value")};
		pairs[i++] = new String[]{"logon_valid", authCode};
		postUrl("http://yn.189.cn/login/loginAction.jsp", "http://yn.189.cn/login/login_pop.jsp", pairs, new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				if (!doc.toString().contains("USER_NO_VAL")) {
					String msg=doc.select("span.error_info").text();
					data.put("errMsg", msg);
					setStatus(STAT_STOPPED_FAIL);
					notifyStatus();
				}else {
					getUrl("http://yn.189.cn/service/account/index.jsp", "http://yn.189.cn/login/login_pop.jsp", new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1){
						@Override
						public void afterRequest(SimpleObject context) {
							Document doc = ContextUtil.getDocumentOfContent(context);
							String area_code = StringUtil.subStr("area_code=\"", "\";", doc.toString());
							String user_no = StringUtil.subStr("user_no = '", "';", doc.toString());
							entity.put("area_code", area_code);
							entity.put("user_no", user_no);
						}
					});
						
					
//					String[][] pairs = {{"Mobile",phoneNo}};
//					postUrl("http://yn.189.cn/public/doAction.jsp", "http://yn.189.cn/service/account/index.jsp",pairs, null);
					/*postUrl("http://yn.189.cn/public/doAction.jsp", "http://yn.189.cn/service/account/index.jsp",pairs, new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1){
						@Override
						public void afterRequest(SimpleObject context) {
							Document doc = ContextUtil.getDocumentOfContent(context); 
							//M_ACTION=WB_EXPENDQUERY&M_FLAG=1&M_CHARTTYPE=1&&M_WIDTH=300&M_HEIGHT=300&AREA_CODE=0871&NUM=15398469671&BILL_CYCL_ID=201408&CUST_ID=713030900012
							String AREA_CODE = StringUtil.subStr("AREA_CODE=", "&", doc.toString());
							String CUST_ID = StringUtil.subStr("CUST_ID=", "", doc.toString());
							//String AREA_CODE = StringUtil.subStr("AREA_CODE=", "&", doc.toString());
						}
					});*/
					
					//getUrl("http://yn.189.cn/service/account/uamsso.jsp", "http://yn.189.cn/service/account/index.jsp", null);
					//getUrl("http://yn.189.cn/service/account/query_scores.jsp", "http://yn.189.cn/service/account/index.jsp", null);
					onCompleteLogin(context);
				}
				notifyStatus();
				
			}
		});
	}

	protected void onCompleteLogin(SimpleObject context) {
		setStatus(STAT_SUC);
		//sendSmsPasswordForRequireCallLogService();
		requestService();
	}
	
	public void requestAllService() {
		requestService();
	}
	private void requestService() {
		parseBalanceInfo();
		//账单和详单的查询放到了parseBalanceInfo的after回调方法
	}	
	
	private void parseBalanceInfo() {
		//http://yn.189.cn/service/manage/my_managepwd.jsp
		getUrl("http://yn.189.cn/service/manage/my_selfinfo.jsp", "http://yn.189.cn/service/manage/index.jsp?FLAG=4", new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements table = doc.select("table");
				if (table==null) {
					return;
				}
				Elements trs = table.select("tr");
				//System.out.println(true);
				//user.setBirthday(DateUtils.StringToDate(es2.get(0).text().replaceAll("出生日期：", ""), "yyyy-MM"));
				user.setRealName(trs.get(0).select("td").text());
				data.put("realName", trs.get(0).select("td").text());
				user.setMemberType(trs.get(1).select("td").text());
				user.setIdcard(trs.get(2).select("td").text());
				user.setAddr(trs.get(4).select("td").text());
				user.setEmail(trs.get(6).select("td").text());
				user.setRegisterDate(DateUtils.StringToDate(trs.get(7).select("td").text(), "yyyyMMdd"));
				data.put("area_code", entity.get("area_code"));
				String[][] pairs = {{"NUM",phoneNo},{"AREA_CODE",entity.get("area_code").toString()}};
				//查询用户当前余额
				postUrl("http://yn.189.cn/service/account/query_account_balance.jsp", "http://yn.189.cn/service/account/index.jsp",pairs, new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1){
					@Override
					public void afterRequest(SimpleObject context) {
						Document doc = ContextUtil.getDocumentOfContent(context);
						try {
							String balance = doc.select("body").text().trim();
							/*String[][] pairs_pre = {{"NUM",phoneNo},{"AREA_CODE","0871"},{"PROD_NO","4217"},{"USER_NAME"},{"ACCT_ID","413262837"},{"USER_ID","713030900012"}};
							postUrl("http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001",pairs_pre, new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1) {
								@Override
								public void afterRequest(SimpleObject context) {
								}
							});*/
							System.out.println(balance);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
				
				requestMyInfoService();

				
				//用户账单和详单查询
				List<String> ms =  DateUtils.getMonthForm(7,"yyyyMM");
				for (int i = 0;i<ms.size();i++) {
					
					if (i<6) {
						requestMonthBillService(ms.get(6-i));
						requestFlowLogService(ms.get(5-i), 1);
					}
					if(i==6){
						requestCurrentBillService(ms.get(0));
					}
					
					if (i>0) {
						requestCallLogService(ms.get(6-i));
					}
				}
			}

		});
		
		
//		getUrl("http://yn.189.cn/public/queryinfo.jsp?_="+System.currentTimeMillis(), "http://yn.189.cn/service/account/index.jsp", null);
//		getUrl("http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=yecx", null);
//		String[][] pairs1 = {{"Mobile",phoneNo}};
//		postUrl("http://yn.189.cn/public/doAction.jsp", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", pairs1,null);
//		String[][] pairs2 = {{"NUM",phoneNo},{"AREA_CODE","0871"},{"PROD_NO","4217"},{"USER_NAME",""},{"ACCT_ID","413262837"},{"USER_ID","713030900012"}};
//		postUrl("http://yn.189.cn/service/bill/action/ifr_bill_hislist.jsp", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", pairs2,null);
		//postUrl("http://yn.189.cn/public/shop_car_det.jsp", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", null,null);
		getUrl("http://yn.189.cn/public/queryinfo.jsp?_="+System.currentTimeMillis(), "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", null);
		
	}	
	
	

	private void requestMyInfoService() {
		String[][] pairs = {{"NUM",phoneNo},{"AREA_CODE",entity.get("area_code").toString()}};
		//查询用户当前余额 -7.26
		postUrl("http://yn.189.cn/service/account/query_account_balance.jsp", "http://yn.189.cn/service/account/",pairs, new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context);
				try {
					String balance = doc.select("body").text().trim();
					if(balance.equals("")){
						balance = "0";
					}
					user.setPhoneRemain(new BigDecimal(balance));
				} catch (Exception e1) {
					logger.error(doc.toString(), e1);
				}
			}
		});
		
	}
	
	private void requestCurrentBillService(final String date) {
		String[][] pairs = {{"NUM",phoneNo},{"AREA_CODE",entity.get("area_code").toString()}};
		//实时话费 13.42
		postUrl("http://yn.189.cn/service/account/query_rt.jsp", "http://yn.189.cn/service/account/",pairs, new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_3){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context);
				String balance = "";
				try {
					balance = doc.select("body").text().trim();
					if (balance.equals("")) {
						balance = "0";
					}
				} catch (Exception e1) {
					logger.error(doc.toString(), e1);
				}
				
				DianXinTel obj = new DianXinTel();
				obj.setcAllPay(new BigDecimal(balance));
				obj.setTeleno(phoneNo);
				obj.setcName(data.getString("realName"));
				obj.setcTime(DateUtils.StringToDate(date, "yyyyMM"));
				String dependeCycle = date.substring(0, 4) + "/" + date.substring(4, 6) + "/01-" + DateUtils.formatDate(new Date(), "yyyy/MM/dd");
				obj.setDependCycle(dependeCycle);
				telList.add(obj);
			}
		});
		
	}
	
	private void requestMonthBillService(final String dstr) {
		//TEMPLATE_ID=&BILLING_CYCLE=201408&USER_ID=713030900012&NUM=15398469671&AREA_CODE=0871
		String[][] pairs = {{"TEMPLATE_ID",""},{"BILLING_CYCLE",dstr},{"USER_ID",entity.get("user_no").toString()},{"NUM",phoneNo},{"AREA_CODE",entity.get("area_code").toString()}};
		postUrl("http://yn.189.cn/service/bill/action/ifr_bill_hislist_em.jsp", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001",pairs, new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 	
				//logger.info(doc.toString());
				DianXinTel obj = new DianXinTel();
				String allPay = StringUtil.subStr("本期消费：", "&nbsp;元", doc.toString());
				//String balance = StringUtil.subStr("AcctName=本项小计：, AcctFee=", "}]", doc.toString());
				/*String balance_add = "";
				String balance_end = "";
				BigDecimal account = new BigDecimal(0);
				if(balance==null||balance.equals("")){
					try {
						balance_end = StringUtil.subStr("本期末可用余额：", "&nbsp;元", doc.toString());
						BigDecimal decimal_end = new BigDecimal(balance_end);
						balance_add = StringUtil.subStr("本期可用存入：", "&nbsp;元", doc.toString());
						BigDecimal decimal_add = new BigDecimal(balance_add);
						balance = StringUtil.subStr("上期末可用余额：", "&nbsp;元", doc.toString());
						BigDecimal decimal = new BigDecimal(balance);
						account = decimal.add(decimal_add).subtract(decimal_end);
					} catch (Exception e) {
						logger.error(e.toString());
					}
				}else {
					try {
						account = new BigDecimal(balance);
					} catch (Exception e) {
						logger.error(e.toString());
					}
				}*/
				BigDecimal account = new BigDecimal(0);
				try {
					account = new BigDecimal(allPay);
				} catch (Exception e) {
					logger.info(e.toString());
				}
				obj.setcAllPay(account);
				obj.setcTime(DateUtils.StringToDate(dstr, "yyyyMM"));
				obj.setTeleno(phoneNo);
				String dependeCycle = dstr+"01-"+dstr+DateUtils.getDaysOfMonth(dstr);
				obj.setDependCycle(dependeCycle);
				obj.setcName(data.getString("realName"));
				telList.add(obj);
			}
		});
	}
	private void requestCallLogService(final String date) {	
	try {
		//http://yn.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp
		//Referer: http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1
		//NUM=15398469671&AREA_CODE=0871&CYCLE_BEGIN_DATE&CYCLE_END_DATE&BILLING_CYCLE=201407&QUERY_TYPE=10
//		String[][] pairs1 = {{"Mobile",phoneNo}};
//		postUrl("http://yn.189.cn/public/doAction.jsp", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", pairs1,null);
		getUrl("http://yn.189.cn/public/queryinfo.jsp?_="+System.currentTimeMillis(), "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", null);
		String[][] pairs_detail = {{"NUM",phoneNo},{"AREA_CODE",entity.get("area_code").toString()},{"CYCLE_BEGIN_DATE",""},{"CYCLE_END_DATE",""},{"BILLING_CYCLE",date},{"QUERY_TYPE","10"}};
		postUrl("http://yn.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp","http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1",pairs_detail, new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, date);
			}
		});
	} catch (Exception e) {
		e.printStackTrace();
	}
	try {
		//http://yn.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp
		//Referer: http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1
		//NUM	15398469671,AREA_CODE	0871,CYCLE_BEGIN_DATE,CYCLE_END_DATE,BILLING_CYCLE	201409,QUERY_TYPE	30
		String[][] pairs_message = {{"NUM",phoneNo},{"AREA_CODE",entity.get("area_code").toString()},{"CYCLE_BEGIN_DATE",""},{"CYCLE_END_DATE",""},{"BILLING_CYCLE",date},{"QUERY_TYPE","30"}};
		postUrl("http://yn.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp","http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", pairs_message,new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveSmsLog(context, date);
			}
		});
	} catch (Exception e) {
		e.printStackTrace();
	}
	
		
	}
	private void saveCallLog(SimpleObject context,final String dstr) {
		Document doc = ContextUtil.getDocumentOfContent(context);
		//logger.error(doc.toString());
		if (StringUtils.isBlank(doc.toString())) {
			return;
		}
		try {
			Elements table = doc.select("table#details_table");
			Elements trs = table.select("tr");
			for (int i = 0; i < trs.size(); i++) {
				Elements tds = trs.get(i).select("td");
				if (tds.size()>8) {
					DianXinDetail dxDetail = new DianXinDetail();
					UUID uuid = UUID.randomUUID();
					dxDetail.setId(uuid.toString());
					dxDetail.setTradeAddr(tds.get(1).text());//主叫位置
					dxDetail.setPhone(phoneNo);//主叫号码
					if(tds.get(2).text().trim().equals(phoneNo)){
						dxDetail.setCallWay("主叫");
						dxDetail.setRecevierPhone(tds.get(3).text().trim());//被叫号码
					}else {
						dxDetail.setCallWay("被叫");
						dxDetail.setRecevierPhone(tds.get(2).text().trim());//被叫号码
					}
					dxDetail.setcTime(DateUtils.StringToDate(tds.get(4).text().trim(), "yyyy-MM-dd-HH:mm:ss"));//通话开始时间
					int secend = DateUtils.transform(tds.get(5).text().trim());
					dxDetail.setTradeTime(secend);//通话时长
					BigDecimal b1= new BigDecimal(tds.get(6).text().trim());//基本/漫游费(元)
					BigDecimal b2= new BigDecimal(tds.get(7).text().trim());//长话费(元)
					dxDetail.setAllPay(b1.add(b2));
					dxDetail.setTradeType(tds.get(8).text().trim());
		        	if (dxDetail != null) {
						detailList.add(dxDetail);
					}
				}
			}
		} catch (Exception e) {
			logger.error(doc.toString(), e);
		}
	}
	private void saveSmsLog(SimpleObject context,  final String dstr) {
		Document doc = ContextUtil.getDocumentOfContent(context);
		if (StringUtils.isBlank(doc.toString())) {
			return;
		}
		try {
			Elements table = doc.select("table#details_table");
			Elements trs = table.select("tr");
			for (int i = 0; i < trs.size(); i++) {
				Elements tds = trs.get(i).select("td");
				if (tds.size()>5) {
					TelcomMessage obj = new TelcomMessage();
					obj.setPhone(phoneNo);
					obj.setRecevierPhone(tds.get(2).text());//对方号码
					obj.setSentTime(DateUtils.StringToDate(tds.get(3).text(), "yyyy-MM-dd-HH:mm:ss"));//发送时间
					obj.setAllPay(Doubles.tryParse(tds.get(4).text()));//总费用
					obj.setBusinessType(tds.get(5).text());//业务类型：发送
					if (obj!=null) {
						messageList.add(obj);
					}
				}
			}
		} catch (Exception e) {
			logger.error(doc.toString(), e);
		}
	}
	
	private void requestFlowLogService(final String date, final int i) { 


		// http://yn.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp?
		// AREA_CODE=0871&BILLING_CYCLE=201410&CYCLE_BEGIN_DATE=2014-10-01&&CYCLE_END_DATE=2014-10-31&NUM=15398469671&QUERY_TYPE=20
		String startTime = date.substring(0, 4) + "-" + date.substring(4, 6) + "-01"; 
		String endTime = DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy-MM-dd");

		String[][] pairs_flow = {{"pageno", String.valueOf(i)},{"AREA_CODE",entity.get("area_code").toString()}, {"BILLING_CYCLE", date}, {"CYCLE_BEGIN_DATE", startTime}, {"CYCLE_END_DATE", endTime}, {"NUM", phoneNo}, {"QUERY_TYPE", "20"}};
		//TODO
		postUrl("http://yn.189.cn/service/bill/action/ifr_bill_detailslist_em_new.jsp","http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=SHQD1", pairs_flow,new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_5) {
		@Override
		public void afterRequest(SimpleObject context) {
		saveFlowLog(context, i, date);
		}
		});
		}
	
	
	protected void saveFlowLog(SimpleObject context, int i, String date) {
		if (context == null)
			return;
		Document doc = ContextUtil.getDocumentOfContent(context);
		if (StringUtils.isBlank(doc.toString())
				|| doc.toString().contains("尊敬的客户，您所查询的条件内没有相应的记录")) {
			DianXinFlow dxFlow = new DianXinFlow();
			UUID uuid = UUID.randomUUID();
			dxFlow.setId(uuid.toString());
			dxFlow.setPhone(phoneNo);

			// 起止日期:2014-10-01——2014-10-31
			String cycle = date.substring(0, 4) + "-" + date.substring(4, 6)
					+ "-01" + "——"
					+ DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy-MM-dd");
			// 查询日期:2014年10月13日
			Date queryMonth = DateUtils.StringToDate(date, "yyyyMM");
			dxFlow.setDependCycle(cycle);
			dxFlow.setQueryMonth(queryMonth);

			dxFlow.setAllFlow(new BigDecimal(0));
			dxFlow.setAllTime(new BigDecimal(0));
			dxFlow.setAllPay(new BigDecimal(0.00));
			flowList.add(dxFlow);
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

			// 起止日期:2014-10-01——2014-10-31
			String cycle = trs.get(2).select("td").get(0).text()
					.replaceAll("起止日期:", "");
			dxFlow.setDependCycle(cycle);
			// 查询日期:2014年10月13日
			Date queryMonth = DateUtils.StringToDate(date, "yyyyMM");
			dxFlow.setQueryMonth(queryMonth);
			// 总流量:0.53MB
			String totalFlow = trs.get(3).select("td").get(0).text()
					.replaceAll("总流量:", "");
			// 总时长:0小时0分0秒
			String allTime = trs.get(3).select("td").get(1).text()
					.replaceAll("总时长:", "");
			// 总费用:0.0元
			String allPay = trs.get(4).select("td").get(0).text()
					.replaceAll("总费用:", "").replaceAll("元", "");
			dxFlow.setAllFlow(new BigDecimal(StringUtil.flowFormat(totalFlow)));
			dxFlow.setAllTime(new BigDecimal(StringUtil.flowTimeFormat(allTime)));
			dxFlow.setAllPay(new BigDecimal(allPay));
			flowList.add(dxFlow);
		}
		
		Elements trs1 = trs.get(5).select("table").get(0).select("tr");
		for (int j = 1; j < trs1.size() - 3; j++) {
			Elements tds1 = trs1.get(j).select("td");
			DianXinFlowDetail dxFd = new DianXinFlowDetail();
			uuid = UUID.randomUUID();
			dxFd.setId(uuid.toString());
			dxFd.setPhone(phoneNo);

			dxFd.setLocation(tds1.get(1).text());// 通信地点
			dxFd.setBeginTime(DateUtils.StringToDate(tds1.get(2).text(),
					"yyyy-MM-dd-HH:mm:ss"));// 开始时间
			// 上网时长
			dxFd.setTradeTime(TimeUtil.timetoint(tds1.get(3).text()));
			String flow = tds1.get(4).text();
			if(flow.equals("")){
				flow = "0";
			}
			dxFd.setFlow(new BigDecimal(flow));// 总流量
			dxFd.setNetType(tds1.get(5).text());// 通讯类型
			dxFd.setBusiness(tds1.get(6).text());// 使用业务
			dxFd.setFee(new BigDecimal(tds1.get(7).text()));// 费用（元）
			flowDetailList.add(dxFd);
		}

		if (doc.toString().contains("页次")) {
			RegexPaserUtil rp = new RegexPaserUtil("页次 &nbsp", "\\(",
					doc.toString(), RegexPaserUtil.TEXTEGEXANDNRT);
			String[] page = rp.getText().split("/");
			if (Integer.parseInt(page[1]) > i) {
				requestFlowLogService(date, i + 1);
			}

		}
	}
	
	private void test() {
		//String param = "TEMPLATE_ID=&BILLING_CYCLE=201406&USER_ID=713030900012&NUM=15398469671&AREA_CODE=0871";
		//getUrl("http://yn.189.cn/public/queryinfo.jsp?_=1411435427700", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", null);
		//NUM=15398469671&AREA_CODE=0871
		/*String[][] pairs1 = {{"NUM","15398469671"},{"AREA_CODE","0871"}};
		postUrl("http://yn.189.cn/service/bill/action/ifr_dict_use.jsp", "http://yn.189.cn/service/account/index.jsp",pairs1, null);
		postUrl("http://yn.189.cn/public/shop_car_det.jsp", "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001",null, null);*/
		
		String[][] pairs = {{"TEMPLATE_ID",""},{"BILLING_CYCLE","201406"},{"USER_ID",entity.get("user_no").toString()},{"NUM",phoneNo},{"AREA_CODE",entity.get("area_code").toString()}};
		postUrl("http://yn.189.cn/service/bill/action/ifr_bill_hislist_em.jsp" , "http://yn.189.cn/service/bill/feeQuery.jsp?SERV_NO=9A001", null, pairs, null,new AbstractProcessorObserver(util, WaringConstaint.YUNNANDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				if (text != null && text.indexOf("本期费用合计") >= 0) {
					logger.info("aaa");
				} else {
					logger.info(text);
				}
				//Document doc = ContextUtil.getDocumentOfContent(context); 
				/*String text = doc.select("div.con").text();
				if (StringUtils.isBlank(text) || text.length() < 3) {
					text = doc.select("div.loginRe_yewuLef").text();
				}
				logger.info(text);
				Elements es = doc.select("tr.title2_2013 td");
				if (es.size() == 0) {
					return;
				}
				logger.info(es.text());*/
				//DebugUtil.findMissing(ContextUtil.getCookieStore(context), "CmProvid=sn; cmtokenid=vj2eYJzMSeb3spP1AwJLvQpubltRN7eN@sn.ac.10086.cn; CmWebtokenid=14791405282,sn; CmWebNumSn=14791405282,sn; eparch_code=0912; flagcity=0000; city=0029; WT_FPC=id=2de2f6df637dde039641410766675596:lv=1410766735922:ss=1410766675596; SATURN_JSESSIONID=QQHbJWWVR31qGrvHMJXJQnXVp4G1y9dTqNdTTpmNnnJXBkYTnTdj!1932990289; VISIT_SESSION_ID=QQHbJWWVR31qGrvHMJXJQnXVp4G1y9dTqNdTTpmNnnJXBkYTnTdj!1932990289!1410766421653; VISIT_CALLER=u1O3PWIav2GDJ36NSnce1xYsr5Bjdzul; ICS_JSESSIONID=hz13JWWJQC0ChLHmXG4yDgnz8TvTsdLwzjvXFyhcpZ05MjGPr1Cp!-2070368112");
			}
		});
	}
	public static void main(String[] args) throws Exception {
		String phoneNo = "15398469671";
		String password = "231102";

		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		spider.getSite().setTimeOut(99999);
		YunNanDianXin dx = new YunNanDianXin(spider, null, phoneNo, password, "2345", null);
		dx.setTest(true);
		boolean isTestService =false;
		if (isTestService) {
			DebugUtil.addToCookieStore("yn.189.cn", "sto-id-20480=AENCKIMAHICD; Hm_lvt_ad041cdaa630664faeaf7ca3a6a45b89=1410258848,1410333066,1411717404; _pk_id.1.2ade=776784f4b021014b.1410258848.4.1411717440.1410333066.; _pk_ses.1.2ade=*; JSESSIONID=0001cC4sihZabThfpItyjBVgMb3:1DP3MTTO6M; Hm_lpvt_ad041cdaa630664faeaf7ca3a6a45b89=1411717440; userId=201|151669205; citrix_ns_id_.189.cn_%2F_wlf=TlNDX3h1LTIyMi42OC4xODUuMjI5?sBn7JRm5ImeTI832903604H90UcA&; _gscu_1708861450=08414613xm7uze18; s_pers=%20s_fid%3D1A242395ED79A581-3A26212DCAB10D67%7C1474875802586%3B; svid=57d164063536cf356b8e297ca4c33c07; SHOPID_COOKIEID=10003; _gscu_1758414200=09538089x3yxpv14; WT_FPC=id=2a8a08f14672bf11d2f1410332092134:lv=1410332422977:ss=1410332092134; dqmhIpCityInfos=%E5%8C%97%E4%BA%AC%E5%B8%82+%E7%94%B5%E4%BF%A1; Js_cityId=3; pgv_pvid=4789877535; pgv_pvi=1460595712; _gscs_1708861450=11717319ppsmxx93|pv:1; _gscbrs_1708861450=1; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%253D%252526pid%25253D%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fyn.189.cn%2525252Fact%2525252F%252526ot%25253DA%252526oi%25253D137%2526eship-189-sh%253D%252526pid%25253D%2525252F%252526pidt%25253D1%252526oid%25253Dfunctiononclick%25252528%25252529%2525257BredirectProvince%25252528%25252527yn%25252527%25252529%2525253B%2525257D%252526oidt%25253D2%252526ot%25253DA%252526oi%25253D111%3B; loginStatus=non-logined; cityCode=yn; s_cc=true");
			/*Date d = new Date();
			String dstr = DateUtils.formatDate(DateUtils.add(d, Calendar.MONTH, -1 * 1), "yyyyMM");*/
			dx.test();
			//dx.requestAllService();
			spider.start();
		} else{
			dx.checkVerifyCode(phoneNo);
			spider.start();
			dx.printData();
			dx.getData().clear();
			dx.setAuthCode(CUtil.inputYanzhengma());
			dx.goLoginReq();
			spider.start();
			dx.printData();
		}
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		//dx.printData();
		if (true) {
			return;
		}
		/*List<String> ms =  DateUtils.getMonthForm(7,"yyyyMM");
		for (int i = 0;i<ms.size();i++) {
			if (i<6) {
				System.out.println("m"+ms.get(6-i));
			}
			if (i>0) {
				System.out.println("d"+ms.get(6-i));
			}
		}*/
	}
}
