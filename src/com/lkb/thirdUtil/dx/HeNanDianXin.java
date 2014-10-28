package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.util.HttpUtil;
import com.lkb.warning.WarningUtil;

public class HeNanDianXin extends AbstractDianXinCrawler {

	public HeNanDianXin(Spider spider, User user, String phoneNo,
			String password, String authCode, WarningUtil util) {
		this();
		this.spider = spider;
		this.password = password;
		this.phoneNo = phoneNo;
		if (null == user) {
			this.user = new User();
			this.user.setPhone(phoneNo);
		} else {
			this.user = user;
		}
		this.authCode = authCode;
		this.util = util;
		spider.getSite().setCharset("UTF-8");
	}

	public HeNanDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;
		this.util = util;
		spider.getSite().setCharset("utf-8");
	}

	public HeNanDianXin() {
		areaName = "河南";
		customField1 = "3";
		customField2 = "17";
		toStUrl = "&toStUrl=http://ha.189.cn/service/bill/";
		shopId = "10017";
	}
	
	
/*	public void goLoginReq() {     
		goLoginReq(null, null);
	}
	private void goLoginReq(final String prefix, final String phone) {        	
		getUrl("http://ha.189.cn/service/service_login.jsp?retUrl=%2FindexV5.jsp","http://ha.189.cn/indexV5.jsp", new AbstractProcessorObserver(util, WaringConstaint.HEHDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				parseLogin1(context, prefix, phone);
			}
		});
	}

	private void parseLogin1(SimpleObject context, final String prefix, final String phone){
		if (prefix != null) {
			saveVerifyImage(context, prefix, phone);
		} else {
			//com.lkb.debug.DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements forms = doc.select("form#LOGON_FORM");
			if(forms.size()>0){
				Element form = doc.select("form#LOGON_FORM").get(0);
				//logon_action=&retUrl=http%3A%2F%2Fwww.189.cn%2Fha&LOGIN_TYPE=21&logon_name_mini=&logon_name_tel=&logon_passwd=199278&RAND_TYPE=001&REQ_TYPE=CAP01005&LOGIN_TYPE_DATA=2122&AREA_CODE=
				//&logon_name=15378784068&PASSWD_TYPE=21&MINI_PASSWD_TYPE=26&TEL_PASSWD_TYPE=2A&passwdUserNum=&passwdCustNum=&passwdCustCodeNum=&passwdRegisterNum=&passwdCdmaNum=&passwdCdmaUserNum=199278
				//&passwdMiniNum=&passwdMiniUserNum=&passwdTelNum=&passwdTelUserNum=&logon_valid=63xt&cookieContent=http%3A%2F%2Fwww.189.cn%2Fha
				String[][] pairs = {{"logon_action", valOfElement(form, "#logon_action")}, {"retUrl", valOfElement(form, "#retUrl")}, {"LOGIN_TYPE", "21"}, {"logon_passwd", password}, {"RAND_TYPE", "001"}, 
						{"logon_name", phoneNo}, {"REQ_TYPE", "CAP01005"}, {"LOGIN_TYPE_DATA", "2122"}, {"logon_valid", authCode}, {"PASSWD_TYPE", "26"}, {"TEL_PASSWD_TYPE", "2A"}, {"passwdCdmaUserNum", password}, 
						{"cookieContent", valOfElement(form, "#retUrl")}};

				postUrl("http://ha.189.cn/UAMSSO/callservice_loginuam.jsp","http://ha.189.cn/service/service_login.jsp?retUrl=http://www.189.cn/ha", pairs, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_3) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLogin2(context);
					}
				});
			}else{
				//window.location.href="http://www.189.cn/dqmh/frontLink.do?method=linkTo&shopId=10017&toStUrl="+'/indexV5.jsp';
				String url = StringUtil.subStr("window.location.href=\"", "';", ContextUtil.getContent(context)).replace("\"+'", "");
				getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
					@Override
					public void afterRequest(SimpleObject context) {
						System.out.println(context);
					}
				});
			}
		}
		
	}
	private void parseLogin2(SimpleObject context){
		String text = ContextUtil.getContent(context);
		String errmsg = StringUtil.subStr("pop({text:\"", "\"});", text);
		if (StringUtils.isBlank(errmsg)) {
			Document doc = ContextUtil.getDocumentOfContent(context); 
			String txtSSORequestXML = doc.select("#txtSSORequestXML").val();
			
			if (StringUtils.isBlank(txtSSORequestXML)) {
				setStatus(STAT_STOPPED_FAIL);
				data.put("errMsg", "登录失败，请重试！");
				notifyStatus();
			} else {
				String txtDirectURL = doc.select("#txtDirectURL").val();
				String txtRedirectURL = doc.select("#txtRedirectURL").val();
				String txtUATicket = doc.select("#txtUATicket").val();
				String[][] pairs = {{"SSORequestXML", txtSSORequestXML}, {"RedirectURL", txtRedirectURL}, {"UATicket", txtUATicket}, {"USER_FLAG", "001"}};
				postUrl(txtDirectURL + txtSSORequestXML,"http://ha.189.cn/service/service_login.jsp?retUrl=http://www.189.cn/ha", pairs, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_3) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLogin3(context);
					}
				});
			}
			
		} else {
			data.put("errMsg", errmsg);
			setStatus(STAT_STOPPED_FAIL);
			notifyStatus();
			logger.info(text == null ? "No Content" : text.trim());
		}
	}
	private void parseLogin3(SimpleObject context){
		Request req1 = ContextUtil.getRequest(context);
		Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
		if (HttpUtil.isMovedStatusCode(scode)) {
			HttpResponse resp = ContextUtil.getResponse(context);
			Header h1 = resp.getFirstHeader("Location");
			String nexturl = h1.getValue();
			if (nexturl == null) {
				logger.error("Error : No Redirect URL");    		
			} else {
				getUrl(nexturl, null, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_3) {
					@Override
					public void afterRequest(SimpleObject context) {
						parseLogin3(context);
					}
				});				
			}
		} else {
			parseLogin4(context);
		}
	}
	private void parseLogin4(SimpleObject context){
		String text = ContextUtil.getContent(context);
		String flag = StringUtil.subStr("var flag = \"", "\";", text);
		if (StringUtils.isBlank(flag) || NumberUtils.toInt(flag, -1) < 0) {
			String errmsg = StringUtil.subStr("var msg = \"", "\";", text);
			data.put("errMsg", errmsg);
			setStatus(STAT_STOPPED_FAIL);
		} else {
			setStatus(STAT_SUC);
			notifyStatus();
			onCompleteLogin(context);
		}
	}*/
	public void checkVerifyCode(final String phone) {   
		String prefix = "henan";
		saveVerifyCode(prefix, phone);
//		goLoginReq(prefix, phone);
	}
	private void saveVerifyImage(SimpleObject context, final String prefix, final String phone) {
		//保存验证码图片
		data.put("checkVerifyCode","1");
		String picName =  prefix + "_dx_code_" + phone + "_" + (int) (Math.random() * 1000) + "5tw";
		String url = "http://ha.189.cn/public/v4/common/control/page/imageC.jsp?date=" + new Date().toGMTString();
		try {
			String imgName = saveFile(url.replaceAll(" ", "%20"), "http://ha.189.cn/service/service_login.jsp?retUrl=%2FindexV5.jsp", null, picName, true);
			data.put("imgName", imgName);
			//com.lkb.debug.DebugUtil.printCookieData(ContextUtil.getCookieStore(context), null);
		} catch (Exception e) {
			notifyStatus();
		}
	}
	protected void onCompleteLogin(SimpleObject context) {
//		logger.info(ContextUtil.getContent(context).trim());
		getAreaCode();
		getProdType();
		sendSmsPasswordForRequireCallLogService();
	}
	public void sendSmsPasswordForRequireCallLogService() {
		//&PROD_PWD=&=1&BEGIN_DATE=&END_DATE=&ACCT_DATE=201409
		//&=1&=&=1&=4&=15378784068&=&PASSWORD=
		String[][] pairs = {{"ACC_NBR", phoneNo}, {"PRODTYPE", entity.getString("prodType")}, {"RAND_TYPE", "002"}, {"PROD_TYPE", entity.getString("prodType")}, {"REFRESH_FLAG", "1"}, {"FIND_TYPE", "1"}, {"QRY_FLAG", "1"}
		, {"MOBILE_NAME", phoneNo}, {"OPER_TYPE", "CR1"}, {"ValueType", "4"}, {"SERV_NO", "FSE-2-2"}};
		postUrl("http://ha.189.cn/service/bill/getRand.jsp", "http://ha.189.cn/service/bill/", pairs, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				try {
					if (text.indexOf("<flag>0</flag>") >= 0) {
						setStatus(STAT_SUC);
						data.put("errMsg", "短信验证码发送成功");
					} else {
						data.put("errMsg", "短信验证码发送失败,请重试！");
					}
					notifyStatus();
				} catch (Exception e) {
					logger.error("sendSms:" + text == null ? "No Content" : text.trim(), e);
				}
				
			}
		});
		
	}
	public void requestAllService(String smsCode) {

		parseBalanceInfo();
		requestService(1, null);
		requestService(2, smsCode);
		getUserInfo();
		getFlow(); //流量账单和详单
	}
	private void requestService(int t, String smsCode) { 
		Date d = new Date();
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			if (t == 1) {
				if (i > 0) {
					requestMonthBillService(cd, dstr);	//每月账单
				}
			} else {
				requestCallLogService(smsCode, 1, cd, dstr);//长途通话记录
				requestCallLogService(smsCode, 2, cd, dstr);//市话通话记录
				requestSmsLogService(smsCode, 1, cd, dstr);
			}
		}
	}



	private void requestMonthBillService(final Date d, String dstr) {
		String[][] pairs = {{"ACC_NBR", phoneNo}, {"SERV_NO", "FSE-2-3"}, {"REFRESH_FLAG", "1"}, {"BillingCycle", dstr}, {"operateType", "1"}, {"operateType", "1"}};
		postUrl("http://ha.189.cn/service/bill/fycx/inzd.jsp", "http://ha.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", null, pairs, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				// to do save  monthBill
				try {
					DianXinTel tel = new DianXinTel();
					tel.setcTime(d);
					tel.setTeleno(phoneNo);
									
					String n = StringUtil.subStr("该用户总费用为：", "元", text).replaceAll("\r", "").replaceAll("\n", "").trim();
					BigDecimal b1= new BigDecimal(n.length() == 0 ? "0" : n);
					tel.setcAllPay(b1);
					addMonthBill(tel);
				} catch (Exception e) {
					logger.error("month bill:" + text == null ? "No Content" : text.trim(), e);
				}
			}
		});

	}

	private void requestCallLogService(final String code, final int t, final Date d, final String dstr){
		//=15378784068&=&BEGIN_DATE=&END_DATE=&=4&=1&=1&=on&=1&=201406&=201406
		//=&RAND_TYPE=002&BureauCode=0371&ACC_NBR=15378784068&PROD_TYPE=713040726755&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&ACCT_DATE=201409&FIND_TYPE=1
		//&SERV_NO=FSE-2-2&QRY_FLAG=1&ValueType=4&MOBILE_NAME=15378784068&OPER_TYPE=CR1&PASSWORD=055954
		String[][] pairs = {{"ACC_NBR", phoneNo}, {"RAND_TYPE", "002"}, {"OPER_TYPE", "CR1"}, {"PASSWORD", code}, {"PROD_TYPE", entity.getString("prodType")}, {"ValueType", "4"}, {"REFRESH_FLAG", "1"}, {"FIND_TYPE", "" + t}, {"radioQryType", "on"}, {"QRY_FLAG", "1"}, 
				{"ACCT_DATE", dstr}, {"ACCT_DATE_1", dstr}, {"PRODTYPE", entity.getString("prodType")}, {"QRY_FLAG", "1"}, {"MOBILE_NAME", phoneNo}, {"SERV_NO", "FSE-2-2"}};
		postUrl("http://ha.189.cn/service/bill/fycx/inxxall.jsp", "http://ha.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", null, pairs, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				if (text == null || text.indexOf("短信验证码检查") >= 0) {
					spider.stop();
					setStatus(STAT_STOPPED_FAIL);
					data.put("errMsg", "您输入的查询验证码错误或过期，请重新核对或再次获取！");
					notifyStatus();
					return;
				} else {
					setStatus(STAT_SUC);
					notifyStatus();
				}
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements es = doc.select("#listQry");
				if (StringUtils.isBlank(user.getRealName())) {
					String html = es.text();
					String un = StringUtil.subStr("用户：", "号码：", html);
					user.setRealName(un == null ? "" : un.trim());
				}
				Elements es1 = es.select("tbody tr");
				if(es1 != null && es1.size() > 0) {
					for(int j=0;j<es1.size();j++){
						try {
							Elements tds = es1.get(j).select("td");
							DianXinDetail dxDetail = new DianXinDetail();
							UUID uuid = UUID.randomUUID();
							dxDetail.setId(uuid.toString());
							dxDetail.setcTime(DateUtils.StringToDate(tds.get(2).text().trim(), "yyyyMMddHHmmss")); 
							dxDetail.setTradeTime(NumberUtils.toInt(tds.get(4).text().trim(), 0));
							final String callway = tds.get(5).text().trim();
							dxDetail.setCallWay(callway);
							dxDetail.setRecevierPhone(tds.get(callway.indexOf("主") >= 0 ? 1 : 0).text().trim());
							dxDetail.setOtherPay(new BigDecimal(tds.get(7).text().trim()));
							dxDetail.setAllPay(new BigDecimal(tds.get(6).text().trim()));
							dxDetail.setPhone(phoneNo);
							addDetail(dxDetail);
							
						} catch (Exception e) {
							logger.error("callLog:" + es1.get(j).html(), e);
						}
					}
					/*if(!content.contains("下一页")){
							break;
						}*/
				}
			}
		});

	}
	private void requestSmsLogService(final String code, final int t, final Date d, final String dstr){
		//ACC_NBR=15378784068&PROD_TYPE=&BEGIN_DATE=&END_DATE=&ValueType=4&REFRESH_FLAG=1&FIND_TYPE=5
		//&radioQryType=on&QRY_FLAG=1&ACCT_DATE=201409&ACCT_DATE_1=201407
		String[][] pairs = {{"ACC_NBR", phoneNo}, {"OPER_TYPE", "CR1"}, {"PASSWORD", code}, {"PROD_TYPE", entity.getString("prodType")}, {"ValueType", "4"}, {"REFRESH_FLAG", "1"}, {"FIND_TYPE", "5"}, {"radioQryType", "on"}, {"QRY_FLAG", "1"}, {"ACCT_DATE", dstr}, {"ACCT_DATE_1", dstr}};
		postUrl("http://ha.189.cn/service/bill/fycx/inxxall.jsp", "http://ha.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", null, pairs, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				if (text == null || text.indexOf("短信验证码检查") >= 0) {
					spider.stop();
					setStatus(STAT_STOPPED_FAIL);
					data.put("errMsg", "您输入的查询验证码错误或过期，请重新核对或再次获取！");
					notifyStatus();
					return;
				}
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements es = doc.select("#listQry");
				Elements es1 = es.select("tbody tr");
				if(es1 != null && es1.size() > 0) {
					for(int j=0;j<es1.size();j++){
						try {
							Elements tds = es1.get(j).select("td");
							TelcomMessage obj = new TelcomMessage();
							obj.setPhone(phoneNo);
							final String btype = tds.get(3).text().trim();
							UUID uuid = UUID.randomUUID();
							obj.setId(uuid.toString());
							obj.setRecevierPhone(tds.get(btype.indexOf("发送") >= 0 ? 1 : 0).text().trim());
							obj.setSentTime(DateUtils.StringToDate(tds.get(2).text().trim(), "yyyyMMddHHmmss"));
							obj.setAllPay(NumberUtils.toDouble(tds.get(4).text().trim(), 0));
							
							obj.setBusinessType(btype);
							
							obj.setPhone(phoneNo);
							addMessage(obj);
							
						} catch (Exception e) {
							logger.error("messageLog:" + es1.get(j).html(), e);
						}
					}
					/*if(!content.contains("下一页")){
							break;
						}*/
				}
			}
		});
		
	}


	private void parseBalanceInfo(){
		getUrl("http://ha.189.cn/service/bill/fycx/ye.jsp?SERV_NO=FSE-2-1&_=" + System.currentTimeMillis() + "&ACC_NBR=" + phoneNo + "&PROD_TYPE="+entity.getString("prodType")+"&ACCTNBR97=", "http://ha.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", new AbstractProcessorObserver(util, WaringConstaint.HEHDX_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				try {
					String n = StringUtil.subStr("账户可用余额为", "元", text);
					BigDecimal b1= new BigDecimal(n.trim());
					addPhoneRemain(b1);
				} catch (Exception e) {
					logger.error("balance:" + text == null ? "No Content" : text.trim(), e);
				}
			}
		});
	}
	
	/**
	* <p>Title: getUserInfo</p>
	* <p>Description: 用户信息抓取（证件号码、客户地址、email）</p>
	* @author Jerry Sun
	*/
	private void getUserInfo(){
		getUrl("http://ha.189.cn/service/manage/my_selfinfo.jsp", "http://ha.189.cn/service/manage/index.jsp", new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(ContextUtil.getContent(context) != null){
					Document doc = ContextUtil.getDocumentOfContent(context);
					Element table = doc.select("[class=table]").get(0);
					Elements tds = table.getElementsByTag("td");
					if(tds.size() > 0){
						for(int i=0; i<=tds.size(); i++){
							if(tds.get(i).text().indexOf("证件号码") != -1)
								user.setIdcard(tds.get(i+1).text());
							if(tds.get(i).text().indexOf("客户住址") != -1)
								user.setAddr(tds.get(i+1).text());
							if(tds.get(i).text().indexOf("Email") != -1)
								user.setEmail(tds.get(i+1).text());
						}
					}
				}
			}
		});
	}
	
	/**
	* <p>Title: getFlow</p>
	* <p>Description: 抓取流量记录</p>
	* @author Jerry Sun
	*/
	private void getFlow(){
		List<String> months = DateUtils.getMonths(6, "yyyyMM");
		for(int i=months.size()-1;i>=0;i--){
			final String month = months.get(i);
			String url = "http://ha.189.cn/service/listQuery/fycx/inxxall.jsp";
			String[][] pairs = {{"PRODTYPE", entity.getString("prodType")}, {"ACC_NBR", phoneNo}, {"BEGIN_DATE", ""}, {"END_DATE", ""}, {"ValueType", "4"},
					{"REFRESH_FLAG", "1"}, {"FIND_TYPE", "4"}, {"radioQryType", "on"}, {"QRY_FLAG", "1"}, {"ACCT_DATE", month}, {"ACCT_DATE_1", month}};
			postUrl(url, "http://ha.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", pairs, new AbstractProcessorObserver(util, WaringConstaint.HEHDX_6) {
				@Override
				public void afterRequest(SimpleObject context) {
					if(context != null){
						/*
						 * 抓取流量账单
						 */
						Elements listQries = ContextUtil.getDocumentOfContent(context).select("#listQry");
						if(listQries.size()>0){
							Element listQry = listQries.get(0);
							Elements theads = listQry.getElementsByTag("thead");
							if(theads.size()>0){
								DianXinFlow dxFlow = new DianXinFlow();
								String flow = theads.get(0).text();
//								String userName = StringUtil.subStr("客户名称：", "客户号码", flow);
								String dependCycle = StringUtil.subStr("起止日期：", "查询日期", flow).trim();
//								String queryDate = StringUtil.subStr("查询日期：", "总流量", flow);
								String allFlow_temp = StringUtil.subStr("总流量：", "总时长", flow).trim();	//628MB 377KB 
								String allTime_temp = StringUtil.subStr("总时长：", "总费用", flow).trim();
								String allPay = StringUtil.subStr("总费用：", "(元)", flow).trim();
								
								dxFlow.setPhone(phoneNo);
								dxFlow.setDependCycle(dependCycle);
								dxFlow.setAllFlow(new BigDecimal(StringUtil.flowFormat(allFlow_temp)));
								dxFlow.setAllPay(new BigDecimal(allPay));
								dxFlow.setAllTime(new BigDecimal(StringUtil.flowTimeFormat(allTime_temp)));
								dxFlow.setQueryMonth(DateUtils.StringToDate(month, "yyyyMM"));
								flowList.add(dxFlow);
							}
						}
						
						/*
						 * 抓取流量详单
						 */
						Elements tbodies = ContextUtil.getDocumentOfContent(context).getElementsByTag("tbody");
						if(tbodies.size()>0){
							Elements trs = tbodies.get(1).getElementsByTag("tr");
							if(trs.size()>0){
								for (Element tr : trs) {
									DianXinFlowDetail dxFd = new DianXinFlowDetail();
									dxFd.setPhone(phoneNo);
									Elements tds = tr.getElementsByTag("td");
									if(tds.size()>0){
										for (int i=0;i<tds.size();i++) {
											String temp = tds.get(i).text().trim();
											switch (i) {
											case 0:
											case 1:
												dxFd.setBeginTime(DateUtils.StringToDate(temp, "yyyy-MM-dd hh:mm:ss"));
												break;
											case 2:
												dxFd.setTradeTime(StringUtil.flowTimeFormat(temp));
												break;
											case 3:
												dxFd.setFlow(new BigDecimal(StringUtil.flowFormat(temp)));
												break;
											case 4:
												dxFd.setNetType(temp);
												break;
											case 5:
												dxFd.setLocation(temp);
												break;
											case 6:
												dxFd.setBusiness(temp);
												break;
											case 7:
												dxFd.setFee(new BigDecimal(temp));
												break;
											}
										}
									}
									flowDetailList.add(dxFd);
								}
							}
						}
					}else{
						logger.error("河南电信流量查询出错!");
					}
				}
			});
		}
	}
	
	/**
	* <p>Title: getAreaCode</p>
	* <p>Description: 获取区域编码</p>
	* @author Jerry Sun
	*/
	private void getAreaCode(){
		getUrl("http://ha.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", "http://www.189.cn/ha/", new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					String IDX_AREA_CODE = ContextUtil.getDocumentOfContent(context).select("#IDX_AREA_CODE").val();
					entity.put("areaCode", IDX_AREA_CODE);
				}else{
					logger.error("获取区域编码出错！");
				}
			}
		});
	}
	
	/**
	* <p>Title: getProdType</p>
	* <p>Description: 获取prodType</p>
	* @author Jerry Sun
	*/
	private void getProdType(){
		String[][] pairs = {{"SERV_NAME", "详单查询"}};
		postUrl("http://ha.189.cn/service/bill/fycx/fyjg.jsp?DFLAG=3&SERV_NO=FSE-2-2", "http://ha.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", pairs , new AbstractProcessorObserver(util, WaringConstaint.HEHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					String gotoUrl = StringUtil.subStr("gotoUrl('", "');", ContextUtil.getContent(context));
					if(gotoUrl != null){
						String[] splits = gotoUrl.split("','");
						if(splits.length>0){
							String prodType = splits[1];
							entity.put("prodType", prodType);
						}
					}
				}else{
					logger.error("获取prodeType出错！");
				}
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		String phoneNo = "15378784068";
		String password = "199278";
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		HeNanDianXin dx = new HeNanDianXin(spider, null, phoneNo, password, null, null);
		dx.setTest(true);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		dx.printData();
		
	}
}
