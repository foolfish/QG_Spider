package com.lkb.thirdUtil.dx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.aspectj.weaver.tools.ISupportsMessageContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.UnicomFlowBill;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.constant.DxConstant;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class CQDianXin extends BaseInfoMobile {
	protected static Logger logger = Logger.getLogger(CQDianXin.class);
	public String index = "https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=114.249.55.149,172.16.10.20";

	// 验证码图片路径
	public static String imgurl = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?";

	public static void main(String[] args) throws Exception {
	}

	public CQDianXin(Login login, String currentUser) {
		super(login, ConstantNum.comm_cq_dx, currentUser);
	}

	public void init() {
		try {
			if (!isInit()) {
				String text = cutil.get(index);
				if (text != null) {
					Map<String, Object> jsmap = new HashMap<String, Object>();
					jsmap.put("indexText", text);
					String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
					CHeader h = new CHeader(CHeaderUtil.Accept_, null, null,
							"uam.ct10000.com");
					Map<String, String> param = new LinkedHashMap<String, String>();
					param.put("username", login.getLoginName());
					text = cutil.post(url, h, param);
					String s[] = text.split("\\|");
					jsmap.put("shengfen_id", s[0]);
					jsmap.put("shengfen_name", s[1]);
					redismap.put("jsmap", jsmap);
					setImgUrl(imgurl);
					setInit();
				}
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
	}

	public void shengfen_id() {
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null,
				"uam.ct10000.com");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("username", login.getLoginName());
		String text = cutil.post(url, h, param);
		if (text != null) {
			String s[] = text.split("\\|");
			jsmap.put("shengfen_id", s[0]);
			jsmap.put("shengfen_name", s[1]);
		}
	}

	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
		try {
			parseBegin(Constant.DIANXIN);
			initDwr();
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			Map<String, String> param = new LinkedHashMap<String, String>();
			param.put("callCount", "1");
			param.put("page", "/account/userInfo.htm");
			param.put("httpSessionId", jsmap.get("httpSessionId").toString());
			param.put("scriptSessionId", jsmap.get("scriptSessionId")
					.toString());
			param.put("c0-scriptName", "userInfoQueryDwr");
			param.put("c0-methodName", "getCustInfoNew");
			param.put("c0-id", "0");
			param.put("batchId", "3");
			CHeader h = new CHeader(CHeaderUtil.Accept_, null, null,
					"cq.189.cn");
			String text = cutil
					.post("http://cq.189.cn/account/dwr/call/plaincall/userInfoQueryDwr.getCustInfoNew.dwr",
							h, param);
			if (text != null) {

				if (text.contains("userName")) {
					RegexPaserUtil rp1 = new RegexPaserUtil("\\{", "\\}", text,
							RegexPaserUtil.TEXTEGEXANDNRT);
					String jsons = "{" + rp1.getText() + "}";
					JSONObject json = new JSONObject(jsons);
					String userName = json.getString("userName");
					String idCardNumber = json.getString("idCardNumber");
					String custAddr = json.getString("custAddr");
					String contactPhone = json.getString("contactPhone");

					param = new LinkedHashMap<String, String>();
					param.put("callCount", "1");
					param.put("page", "/account/userProd.htm");
					param.put("httpSessionId", jsmap.get("httpSessionId")
							.toString());
					param.put("scriptSessionId", jsmap.get("scriptSessionId")
							.toString());
					param.put("c0-scriptName", "acceptQueryDwr");
					param.put("c0-methodName", "getfavourInfo");
					param.put("c0-id", "0");
					param.put("c0-param0", "string:" + login.getLoginName());
					param.put("c0-param1", "string:208511296");
					param.put("batchId", "9");

					String pn = "";
					text = cutil
							.post("http://cq.189.cn/account/dwr/call/plaincall/acceptQueryDwr.getfavourInfo.dwr",
									h, param);
					if (text != null) {
						text = StringEscapeUtils.unescapeJava(text);
						rp1 = new RegexPaserUtil("s0.offerCompName=\"", "\"",
								text, RegexPaserUtil.TEXTEGEXANDNRT);
						pn = rp1.getText();
					}
					Map<String, String> parmap = new HashMap<String, String>(3);
					parmap.put("parentId", currentUser);
					parmap.put("loginName", login.getLoginName());
					parmap.put("usersource", Constant.DIANXIN);
					List<User> list = userService
							.getUserByParentIdSource(parmap);
					if (list != null && list.size() > 0) {
						User user = list.get(0);
						user.setLoginName(login.getLoginName());
						user.setLoginPassword("");
						user.setUserName(userName);
						user.setRealName(userName);
						user.setIdcard(idCardNumber);
						user.setAddr(custAddr);
						user.setUsersource(Constant.DIANXIN);
						user.setParentId(currentUser);
						user.setModifyDate(new Date());
						user.setPhone(login.getLoginName());
						user.setFixphone(contactPhone);
						user.setPhoneRemain(getYue());
						user.setEmail("");
						user.setPackageName(pn);
						userService.update(user);
					} else {
						User user = new User();
						UUID uuid = UUID.randomUUID();
						user.setId(uuid.toString());

						user.setLoginName(login.getLoginName());
						user.setLoginPassword("");
						user.setUserName(userName);

						user.setRealName(userName);
						user.setIdcard(idCardNumber);
						user.setAddr(custAddr);
						user.setUsersource(Constant.DIANXIN);
						user.setParentId(currentUser);
						user.setModifyDate(new Date());
						user.setPhone(login.getLoginName());
						user.setFixphone(contactPhone);
						user.setPhoneRemain(getYue());
						user.setEmail("");
						user.setPackageName(pn);
						userService.saveUser(user);
					}
				}
			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			sendWarningCallHistory(errorMsg);
			logger.error("error",e);
		} finally {
			parseEnd(Constant.DIANXIN);
		}
	}

	public void getTelDetailHtml() {

		try {
			parseBegin(Constant.DIANXIN);
			boolean b = true;
			int num = 0;
			bill();
			initDwr1();
			List<String> ms = DateUtils.getMonths(6, "yyyy-MM");
			CHeader h = new CHeader(CHeaderUtil.Accept_, null, null,
					"cq.189.cn");

			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			for (int x = 0; x < ms.size(); x++) {
				
				if (x == 0) {
					String startDate = ms.get(x);
					Map<String, String> param = new LinkedHashMap<String, String>();
					param = new LinkedHashMap<String, String>();
					param.put("callCount", "1");
					param.put("page", "page=/bill/bill.htm?id=2");
					param.put("httpSessionId", jsmap.get("httpSessionId")
							.toString());
					param.put("scriptSessionId", jsmap.get("scriptSessionId")
							.toString());
					param.put("c0-scriptName", "billDwr");
					param.put("c0-methodName", "getSSHF");
					param.put("c0-id", "0");
					param.put("c0-param0", "string:0");
					param.put("batchId", "0");
					String text = cutil
							.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.getSSHF.dwr",
									h, param);
					b = getTelCurrent_parse(text, startDate);
				} else {
				
				String startDate = ms.get(x);
				Map<String, String> param = new LinkedHashMap<String, String>();
				param.put("callCount", "1");
				param.put("page", "page=/bill/bill.htm?id=3");
				param.put("httpSessionId", jsmap.get("httpSessionId")
						.toString());
				param.put("scriptSessionId", jsmap.get("scriptSessionId")
						.toString());
				param.put("c0-scriptName", "billDwr");
				param.put("c0-methodName", "getBillDesc");
				param.put("c0-id", "0");
				param.put("c0-param0", "string:0");
				param.put("c0-param1", "string:" + startDate);
				param.put("c0-param2", "string:2");
				param.put("batchId", "3");

				String text = cutil
						.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.getBillDesc.dwr",
								h, param);
				if (text.contains("\\u8D26\\u76EE\\u603B\\u91D1\\u989D")) {
					param = new LinkedHashMap<String, String>();
					param.put("callCount", "1");
					param.put("page", "page=/bill/bill.htm?id=3");
					param.put("httpSessionId", jsmap.get("httpSessionId")
							.toString());
					param.put("scriptSessionId", jsmap.get("scriptSessionId")
							.toString());
					param.put("c0-scriptName", "billDwr");
					param.put("c0-methodName", "pageServAcctList");
					param.put("c0-id", "0");
					param.put("c0-param0", "Object_Object:{}");
					param.put("c0-param1", "Object_Object:{}");
					param.put("c0-param2", "Object_Object:{}");
					param.put("c0-param3", "number:1");
					param.put("c0-param4", "number:20");
					param.put("batchId", "4");
					text = cutil
							.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.pageServAcctList.dwr",
									h, param);
					b = getTelDetailHtml_parse(text, startDate);
				}
				}
				
				
				if (!b) {
					// 异常信息
					if (errorMsg != null) {
						num++;
						// 超过五次,发送错误信息,
						if (num > 5) {
							// 发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						// 错误
						errorMsg = null;
					} else {
						break;// 数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			logger.error("error",e);
		} finally {
			parseEnd(Constant.DIANXIN);
		}

	}

	private boolean getTelCurrent_parse(String text, String startDate) {
		boolean b = true;
		try {
			if (text.contains("font")) {
				RegexPaserUtil rp1 = new RegexPaserUtil(">", "<",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				String count = rp1.getText();
				if(count == null || count.equals("")){
					count = "0";
				}
				BigDecimal total = new BigDecimal(count);
				Map map2 = new HashMap();
				map2.put("teleno", login.getLoginName());
				map2.put("cTime", DateUtils.StringToDate(startDate, "yyyy-MM"));
				List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
				if (list2 != null && list2.size() > 0) {
					DianXinTel tel = (DianXinTel) list2.get(0);
					tel.setcAllPay(total);
					dianXinTelService.update(tel);
				} else {
					DianXinTel tel = new DianXinTel();
					UUID uuid = UUID.randomUUID();
					tel.setId(uuid.toString());
					tel.setcTime(DateUtils.StringToDate(startDate, "yyyy-MM"));
					tel.setTeleno(login.getLoginName());
					tel.setcAllPay(total);
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(5, 7);
					tel.setDependCycle(TimeUtil.getFirstDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)))
							+ "至"
							+ TimeUtil.getLastDayOfMonth(
									Integer.parseInt(year), Integer
											.parseInt(DateUtils
													.formatDateMouth(mouth))));
					dianXinTelService.saveDianXinTel(tel);
				}

			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
			logger.error("error",e);
		}

		return b;
	}

	/**
	 * 查询话费记录
	 */
	public boolean getTelDetailHtml_parse(String text, String startDate) {
		boolean b = true;
		try {
			if (text.contains("acctType=")) {
				RegexPaserUtil rp1 = new RegexPaserUtil("totalCount:", "}",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				int totalCount = Integer.parseInt(rp1.getText());
				BigDecimal total = new BigDecimal(0);
				BigDecimal tcf = new BigDecimal("0.00");
				// BigDecimal ldxs=new BigDecimal("0.00");
				for (int i = 1; i <= totalCount; i++) {
					rp1 = new RegexPaserUtil("s" + i + ".billAmount=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String billAmount = rp1.getText();
					rp1 = new RegexPaserUtil("s" + i + ".billItem=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String billItem = StringEscapeUtils.unescapeJava(rp1
							.getText());
					if (billItem.contains("套餐费")) {
						tcf = new BigDecimal(billAmount);
					} else if (billItem.contains("手机上网")) {
						// tcf=new BigDecimal(billAmount);
					} else if (billItem.contains("合计")) {
						total = new BigDecimal(billAmount);
					}
				}
				Map map2 = new HashMap();
				map2.put("teleno", login.getLoginName());
				map2.put("cTime", DateUtils.StringToDate(startDate, "yyyy-MM"));
				List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
				if (list2 != null && list2.size() > 0) {
					DianXinTel tel = (DianXinTel) list2.get(0);
					tel.setcAllPay(total);
					dianXinTelService.update(tel);
				} else {
					DianXinTel tel = new DianXinTel();
					UUID uuid = UUID.randomUUID();
					tel.setId(uuid.toString());
					// tel.setBaseUserId(currentUser);
					tel.setcTime(DateUtils.StringToDate(startDate, "yyyy-MM"));
					tel.setTeleno(login.getLoginName());
					tel.setcAllPay(total);
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(5, 7);
					tel.setDependCycle(TimeUtil.getFirstDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)))
							+ "至"
							+ TimeUtil.getLastDayOfMonth(
									Integer.parseInt(year), Integer
											.parseInt(DateUtils
													.formatDateMouth(mouth))));
					tel.setcName("");
					tel.setZtcjbf(tcf);
					// tel.setLdxsf(ldxs);
					// tel.setMythf(myf);
					dianXinTelService.saveDianXinTel(tel);
				}

			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
			logger.error("error",e);
		}

		return b;
	}

	/**
	 * 文本解析 true正常解析 false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次 type
	 * 市话/长途/港澳台/漫游
	 */
	public boolean callHistory_parse(String text, DianXinDetail bean,
			String startDate) {
		boolean b = true;
		try {
			if (text.contains("totalCount")) {
				RegexPaserUtil rp1 = new RegexPaserUtil("totalCount:", "}",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				int totalCount = Integer.parseInt(rp1.getText());
				text = StringEscapeUtils.unescapeJava(text);
				for (int i = 1; i <= totalCount; i++) {
					String fix = "s" + i;
					rp1 = new RegexPaserUtil(fix + "\\['使用地点'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String thdd = rp1.getText();
					rp1 = new RegexPaserUtil(fix + "\\['起始时间'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String qssj = rp1.getText();
					rp1 = new RegexPaserUtil(fix + "\\['通话类型'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String thlx = rp1.getText();
					rp1 = new RegexPaserUtil(fix + "\\['是否在套餐内'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String sftcl = rp1.getText();
					rp1 = new RegexPaserUtil(fix + "\\['通话时长（秒）'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String thsc = rp1.getText();
					rp1 = new RegexPaserUtil(fix + "\\['费用（元）'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String fy = rp1.getText();
					rp1 = new RegexPaserUtil(fix + "\\['对方号码'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String dfhm = rp1.getText();
					rp1 = new RegexPaserUtil(fix + "\\['呼叫类型'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String hjlx = rp1.getText();

					Map map2 = new HashMap();
					map2.put("phone", login.getLoginName());
					map2.put("cTime",
							DateUtils.StringToDate(qssj, "yyyy-MM-dd HH:mm:ss"));
					List list = dianXinDetailService.getDianXinDetailBypt(map2);
					if (list == null || list.size() == 0) {
						DianXinDetail dxDetail = new DianXinDetail();
						UUID uuid = UUID.randomUUID();
						dxDetail.setId(uuid.toString());
						dxDetail.setcTime(DateUtils.StringToDate(qssj,
								"yyyy-MM-dd HH:mm:ss"));

						dxDetail.setTradeTime(Integer.parseInt(thsc));
						dxDetail.setTradeAddr(thdd);
						// dxDetail.setTradeType(hjlx);
						if (thlx.contains("国内")) {
							dxDetail.setTradeType("本地");
						} else {
							dxDetail.setTradeType("漫游");
						}

						dxDetail.setCallWay(hjlx);
						dxDetail.setRecevierPhone(dfhm);

						dxDetail.setAllPay(new BigDecimal(fy));
						dxDetail.setPhone(login.getLoginName());

						dianXinDetailService.saveDianXinDetail(dxDetail);
					}

				}

			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
			logger.error("error",e);
		}
		return b;
	}

	/**
	 * 查询通话记录
	 */
	public void callHistory() {
		try {
			parseBegin(Constant.DIANXIN);
			List<String> ms = DateUtils.getMonths(6, "yyyy-MM");
			CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "189.cn");
			boolean b = false;
			int num = 0;
			bill();
			initDwr1();
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			DianXinDetail bean = new DianXinDetail(login.getLoginName());
			bean = dianXinDetailService.getMaxTime(bean);
			for (int i = ms.size() - 1; i >= 0; i--) {// 不倒序的话最大约先取，会只存一个月的！
				String s = ms.get(i);
				String startDate = s;
				String year = startDate.substring(0, 4);
				String mouth = startDate.substring(5, 7);
				String fday = TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)));
				String eday = TimeUtil.getLastDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)));
				Map<String, String> param = new LinkedHashMap<String, String>();
				param.put("callCount", "1");
				param.put("page", "/bill/bill.htm?id=4");
				param.put("httpSessionId", jsmap.get("httpSessionId")
						.toString());
				param.put("scriptSessionId", jsmap.get("scriptSessionId")
						.toString());
				param.put("c0-scriptName", "billDwr");
				param.put("c0-methodName", "selectListType");
				param.put("c0-id", "0");
				param.put("c0-param0", "string:0");
				param.put("c0-param1", "string:" + startDate);
				param.put("c0-param2", "string:" + fday);
				param.put("c0-param3", "string:" + eday);
				param.put("c0-param4", "string:300001");
				param.put("c0-param5", "string:01");
				param.put("batchId", "4");

				String text = cutil
						.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.selectListType.dwr",
								h, param);
				if (text != null) {
					if (text.contains("sumDesc")) {
						param = new LinkedHashMap<String, String>();
						param.put("callCount", "1");
						param.put("page", "/bill/bill.htm?id=4");
						param.put("httpSessionId", jsmap.get("httpSessionId")
								.toString());
						param.put("scriptSessionId",
								jsmap.get("scriptSessionId").toString());
						param.put("c0-scriptName", "billDwr");
						param.put("c0-methodName", "bsnPageHead");
						param.put("c0-id", "0");
						param.put("batchId", "5");
						text = cutil
								.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.bsnPageHead.dwr",
										h, param);
						if (text != null) {
							text = StringEscapeUtils.unescapeJava(text);
							// System.out.println(text);
							param = new LinkedHashMap<String, String>();
							param.put("callCount", "1");
							param.put("page", "/bill/bill.htm?id=4");
							param.put("httpSessionId",
									jsmap.get("httpSessionId").toString());
							param.put("scriptSessionId",
									jsmap.get("scriptSessionId").toString());
							param.put("c0-scriptName", "billDwr");
							param.put("c0-methodName", "bsnPage");
							param.put("c0-id", "0");
							param.put("c0-param0", "Object_Object:{}");
							param.put("c0-param1", "Object_Object:{}");
							param.put("c0-param2", "Object_Object:{}");
							param.put("c0-param3", "number:1");
							param.put("c0-param4", "number:10000");
							param.put("batchId", "6");
							text = cutil
									.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.bsnPage.dwr",
											h, param);
							b = callHistory_parse(text, bean, startDate);
							if (!b) {
								// 异常信息
								if (errorMsg != null) {
									num++;
									// 超过五次,发送错误信息,123
									if (num > 5) {
										// 发送错误信息通知
										sendWarningCallHistory(errorMsg);
									}
									// 错误
									errorMsg = null;
								} else {
									break;// 数据库中已有数据
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("error",e);
		} finally {
			parseEnd(Constant.DIANXIN);
		}
	}

	/**
	 * 0,采集全部 1.采集手机验证 2.采集手机已经验证
	 * 
	 * @param type
	 */
	public void startSpider() {
		getTelDetailHtml();// 通话记录
		getMyInfo(); // 个人信息
		callHistory(); // 历史账单
		getSmsLog();// 短信记录
		getFlowDetail();
	}

	/**
	 * 流量记录
	 */

	public void getFlowDetail() {
		try {
			parseBegin(Constant.DIANXIN);
			List<String> ms = DateUtils.getMonths(6, "yyyy-MM");
			CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "189.cn");
			boolean b = false;
			int num = 0;
			bill();
			initDwr1();
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			for (int i = ms.size() - 1; i >= 0; i--) {
				String s = ms.get(i);
				String startDate = s;
				String year = startDate.substring(0, 4);
				String mouth = startDate.substring(5, 7);
				String fday = TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)));
				String eday = TimeUtil.getLastDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)));
				Map<String, String> param = new LinkedHashMap<String, String>();
				param.put("callCount", "1");
				param.put("page", "/bill/bill.htm?id=4");
				param.put("httpSessionId", jsmap.get("httpSessionId")
						.toString());
				param.put("scriptSessionId", jsmap.get("scriptSessionId")
						.toString());
				param.put("c0-scriptName", "billDwr");
				param.put("c0-methodName", "selectListType");
				param.put("c0-id", "0");
				param.put("c0-param0", "string:0");
				param.put("c0-param1", "string:" + startDate);
				param.put("c0-param2", "string:" + fday);
				param.put("c0-param3", "string:" + eday);
				param.put("c0-param4", "string:300003");// 表示为流量，1为电话，2为短信
				param.put("c0-param5", "string:01");
				param.put("batchId", "9");// id各个详单不同

				String text = cutil
						.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.selectListType.dwr",
								h, param);
				if (text != null) {
					if (text.contains("sumDesc")) {
						param = new LinkedHashMap<String, String>();
						param.put("callCount", "1");
						param.put("page", "/bill/bill.htm?id=4");
						param.put("httpSessionId", jsmap.get("httpSessionId")
								.toString());
						param.put("scriptSessionId",
								jsmap.get("scriptSessionId").toString());
						param.put("c0-scriptName", "billDwr");
						param.put("c0-methodName", "bsnPageHead");
						param.put("c0-id", "0");
						param.put("batchId", "10");
						text = cutil
								.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.bsnPageHead.dwr",
										h, param);
						if (text != null) {
							text = StringEscapeUtils.unescapeJava(text);
							// System.out.println(text);
							param = new LinkedHashMap<String, String>();
							param.put("callCount", "1");
							param.put("page", "/bill/bill.htm?id=4");
							param.put("httpSessionId",
									jsmap.get("httpSessionId").toString());
							param.put("scriptSessionId",
									jsmap.get("scriptSessionId").toString());
							param.put("c0-scriptName", "billDwr");
							param.put("c0-methodName", "bsnPage");
							param.put("c0-id", "0");
							param.put("c0-param0", "Object_Object:{}");
							param.put("c0-param1", "Object_Object:{}");
							param.put("c0-param2", "Object_Object:{}");
							param.put("c0-param3", "number:1");
							param.put("c0-param4", "number:10000");
							param.put("batchId", "11");
							text = cutil
									.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.bsnPage.dwr",
											h, param);
							b = parse_FlowLog(text, fday, eday);
							if (!b) {
								// 异常信息
								if (errorMsg != null) {
									num++;
									// 超过五次,发送错误信息,123
									if (num > 5) {
										// 发送错误信息通知
										sendWarningFlow(errorMsg);
										//sendWarningCallHistory(errorMsg);
									}
									// 错误
									errorMsg = null;
								} else {
									break;// 数据库中已有数据
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("error",e);
		} finally {
			parseEnd(Constant.DIANXIN);
		}
	}

	/**
	 * 短信记录
	 */

	public void getSmsLog() {
		try {
			parseBegin(Constant.DIANXIN);
			List<String> ms = DateUtils.getMonths(6, "yyyy-MM");
			CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "189.cn");
			boolean b = false;
			int num = 0;
			bill();
			initDwr1();
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			for (int i = ms.size() - 1; i >= 0; i--) {
				String s = ms.get(i);
				String startDate = s;
				String year = startDate.substring(0, 4);
				String mouth = startDate.substring(5, 7);
				String fday = TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)));
				String eday = TimeUtil.getLastDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)));
				Map<String, String> param = new LinkedHashMap<String, String>();
				param.put("callCount", "1");
				param.put("page", "/bill/bill.htm?id=4");
				param.put("httpSessionId", jsmap.get("httpSessionId")
						.toString());
				param.put("scriptSessionId", jsmap.get("scriptSessionId")
						.toString());
				param.put("c0-scriptName", "billDwr");
				param.put("c0-methodName", "selectListType");
				param.put("c0-id", "0");
				param.put("c0-param0", "string:0");
				param.put("c0-param1", "string:" + startDate);
				param.put("c0-param2", "string:" + fday);
				param.put("c0-param3", "string:" + eday);
				param.put("c0-param4", "string:300002");
				param.put("c0-param5", "string:01");
				param.put("batchId", "7");

				String text = cutil
						.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.selectListType.dwr",
								h, param);
				if (text != null) {
					if (text.contains("sumDesc")) {
						param = new LinkedHashMap<String, String>();
						param.put("callCount", "1");
						param.put("page", "/bill/bill.htm?id=4");
						param.put("httpSessionId", jsmap.get("httpSessionId")
								.toString());
						param.put("scriptSessionId",
								jsmap.get("scriptSessionId").toString());
						param.put("c0-scriptName", "billDwr");
						param.put("c0-methodName", "bsnPageHead");
						param.put("c0-id", "0");
						param.put("batchId", "8");
						text = cutil
								.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.bsnPageHead.dwr",
										h, param);
						if (text != null) {
							text = StringEscapeUtils.unescapeJava(text);
							// System.out.println(text);
							param = new LinkedHashMap<String, String>();
							param.put("callCount", "1");
							param.put("page", "/bill/bill.htm?id=4");
							param.put("httpSessionId",
									jsmap.get("httpSessionId").toString());
							param.put("scriptSessionId",
									jsmap.get("scriptSessionId").toString());
							param.put("c0-scriptName", "billDwr");
							param.put("c0-methodName", "bsnPage");
							param.put("c0-id", "0");
							param.put("c0-param0", "Object_Object:{}");
							param.put("c0-param1", "Object_Object:{}");
							param.put("c0-param2", "Object_Object:{}");
							param.put("c0-param3", "number:1");
							// 利用c0-param4=number来让
							param.put("c0-param4", "number:10000");
							param.put("batchId", "9");
							text = cutil
									.post("http://cq.189.cn/bill/dwr/call/plaincall/billDwr.bsnPage.dwr",
											h, param);
							b = parse_SmsLog(text, startDate);
							if (!b) {
								// 异常信息
								if (errorMsg != null) {
									num++;
									// 超过五次,发送错误信息,123
									if (num > 5) {
										// 发送错误信息通知
										sendWarningMessageHistory(errorMsg);
										//sendWarningCallHistory(errorMsg);
									}
									// 错误
									errorMsg = null;
								} else {
									break;// 数据库中已有数据
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error("error",e);
		} finally {
			parseEnd(Constant.DIANXIN);
		}
	}

	public boolean parse_FlowLog(String text, String fday, String eday) {
		boolean b = true;
		List<DianXinFlowDetail> flowList = new ArrayList<DianXinFlowDetail>();
		long bill_allFlow=0,bill_allTime=0;
		Double bill_allPay=0.0;
		int isUpdate=0;
		DianXinFlow newBill=null;
		try {
			Map<String, Object> map = new HashMap(2);
			map.put("queryMonth", DateUtils.StringToDate(fday + " 00:00:00",
					"yyyy-MM-dd HH:mm:ss"));
			map.put("phone", login.getLoginName());
			List billList = dianXinFlowService.getDianXinFlowBybc(map);

			if (billList == null || billList.size() == 0){
				newBill = new DianXinFlow();
				newBill.setId(UUID.randomUUID().toString());
				newBill.setPhone(login.getLoginName());
			}
			else {
				newBill = (DianXinFlow) billList.get(0);
				bill_allFlow=Long.parseLong(newBill.getAllFlow().toString());
				bill_allTime=Long.parseLong(newBill.getAllTime().toString());
				bill_allPay=Double.parseDouble(newBill.getAllPay().toString());
				isUpdate = 1;
			}
			newBill.setDependCycle(fday+"~"+eday);
			newBill.setQueryMonth(DateUtils.StringToDate(fday + " 00:00:00",
					"yyyy-MM-dd HH:mm:ss"));
			
		} catch (Exception e) {
			logger.error("error",e);
			b = false;
		}

		try {
			Date lastTime = null;
			try {
				DianXinFlowDetail temp = new DianXinFlowDetail(
						login.getLoginName());
				if (dianXinFlowDetailService.getMaxTime(temp) != null)
					lastTime = dianXinFlowDetailService.getMaxTime(temp)
							.getBeginTime();
			} catch (Exception e) {
				logger.error("error",e);
			}
			if (text.contains("totalCount")) {
				RegexPaserUtil rp1 = new RegexPaserUtil("totalCount:", "}",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				int totalCount = Integer.parseInt(rp1.getText());
				text = StringEscapeUtils.unescapeJava(text);
				for (int i = 1; i <= totalCount; i++) {
					String fix = "s" + i;
					rp1 = new RegexPaserUtil(fix + "\\['业务类型'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String tradeway = rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['网络类型'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String netway = rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['通信地点'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String tradeAddr = rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['开始时间'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String beginTime = rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['费用（元）'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String AllPay = rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['流量（KB）'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String flowKb = rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['上网时长（秒）'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String time = rp1.getText().trim();

					Date sentTime = null;
					try {
						sentTime = DateUtils.StringToDate(beginTime,
								"yyyy-MM-dd HH:mm:ss");
						if (lastTime != null && sentTime != null) {
							if (sentTime.getTime() <= lastTime.getTime()) {
								continue;
							}
						}
					} catch (Exception e) {
						logger.error("error",e);
					}
					DianXinFlowDetail obj = new DianXinFlowDetail();
					obj.setPhone(login.getLoginName());
					UUID uuid = UUID.randomUUID();
					obj.setId(uuid.toString());
					if (sentTime.getMonth() == new Date().getMonth())
						obj.setIscm(1);
					else
						obj.setIscm(0);
					obj.setBusiness(tradeway);// 业务类型
					obj.setNetType(netway);// 网络类型
					obj.setLocation(tradeAddr);// 通信地点
					obj.setBeginTime(sentTime);//发送时间
					obj.setFee(new BigDecimal(AllPay));
					bill_allPay+=Double.parseDouble(AllPay);
					obj.setFlow(new BigDecimal(flowKb));
					bill_allFlow+=Long.parseLong(flowKb);
					obj.setTradeTime(Long.parseLong(time));
					bill_allTime+=Long.parseLong(time);

					flowList.add(obj);
				}

			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
			logger.error("error",e);
		}
		newBill.setAllFlow(new BigDecimal(bill_allFlow));
		newBill.setAllTime(new BigDecimal(bill_allTime));
		newBill.setAllPay(new BigDecimal(bill_allPay));
		if(isUpdate==0)
			dianXinFlowService.saveDianXinFlow(newBill);
		else
			dianXinFlowService.update(newBill);
		dianXinFlowDetailService.insertbatch(flowList);
		return b;
	}

	public boolean parse_SmsLog(String text, String startDate) {
		boolean b = true;
		try {
			Date lastTime = null;
			try {
				lastTime = telcomMessageService.getMaxSentTime(
						login.getLoginName()).getSentTime();
			} catch (Exception e) {
				logger.error("error",e);
			}
			if (text.contains("totalCount")) {
				RegexPaserUtil rp1 = new RegexPaserUtil("totalCount:", "}",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				int totalCount = Integer.parseInt(rp1.getText());
				text = StringEscapeUtils.unescapeJava(text);
				for (int i = 1; i <= totalCount; i++) {
					String fix = "s" + i;
					rp1 = new RegexPaserUtil(fix + "\\['业务类型'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String BusinessType = "发送";// rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['对方号码'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String RecevierPhone = rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['发送时间'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String SentTime = rp1.getText().trim();
					rp1 = new RegexPaserUtil(fix + "\\['费用（元）'\\]=\"", "\"",
							text, RegexPaserUtil.TEXTEGEXANDNRT);
					String AllPay = rp1.getText().trim();

					Date sentTime = null;
					try {
						sentTime = DateUtils.StringToDate(SentTime,
								"yyyy-MM-dd HH:mm:ss");
						if (lastTime != null && sentTime != null) {
							if (sentTime.getTime() <= lastTime.getTime()) {
								continue;
							}
						}
					} catch (Exception e) {
						logger.error("error",e);
					}
					TelcomMessage obj = new TelcomMessage();
					obj.setPhone(login.getLoginName());
					UUID uuid = UUID.randomUUID();
					obj.setId(uuid.toString());
					obj.setBusinessType(BusinessType);// 业务类型：点对点
					obj.setRecevierPhone(RecevierPhone);// 对方号码
					obj.setSentTime(sentTime);// 发送时间
					obj.setCreateTs(new Date());
					obj.setAllPay(Double.parseDouble(AllPay));// 总费用
					telcomMessageService.save(obj);
				}

			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
			logger.error("error",e);
		}
		return b;
	}

	// 首页登录
	public Map<String, Object> login() {
		try {
			String result = login1();
			if (result != null && result.equals("1")) {
				loginsuccess();
			}

			if (status == 1) {
				addTask(this);
			}
		} catch (Exception e) {
			writeLogByLogin(e);
			logger.error("error",e);
		}
		return map;

	}

	public String getSessionId() {
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String reqUrl = "http://sc.189.cn/service/bill/querynew.jsp";
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "sc.189.cn");
		String text = cutil.get(reqUrl, h);
		String sessionid = "";
		if (text != null && text.contains("sessionid")) {
			Document doc1 = Jsoup.parse(text);
			sessionid = doc1.select("input[id=sessionid]").first()
					.attr("value");
			jsmap.put("sessionid", sessionid);
			// redismap.put("jsmap", map);//根据实际需要存放
		}
		return null;
	}

	/** 第一次post */
	public String login1() {
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String text = jsmap.get("indexText").toString();
		if (text.length() < 300) {
			// 此处是特殊情况处理,主要是redis有记录,或者cookie有记录,会有出现此处连接的地方,需要注意
			// System.out.println(text);
			if (text.contains("location.replace(")) {
				return "1";
			}
		} else {
			Document doc = Jsoup.parse(text);
			String empoent = doc.select("input[id=forbidpass]").first().val();
			String forbidaccounts = doc.select("input[id=forbidaccounts]")
					.first().val();
			String authtype = doc.select("input[name=authtype]").first().val();
			String customFileld02 = jsmap.get("shengfen_id").toString();
			String areaname = jsmap.get("shengfen_name").toString();
			String customFileld01 = "1";
			String lt = doc.select("input[name=lt]").first().val();
			String _eventId = doc.select("input[name=_eventId]").first().val();
			String open_no = "c2000004";
			String action = doc.select("form[id=c2000004]").first()
					.attr("action");
			action = URLDecoder.decode(action);

			String phone1 = null;
			String password1 = null;
			try {

				String n = StringUtil.subStr("strEnc(username,", ");", text)
						.trim();
				String[] stra = n.trim().replaceAll("\'", "").split(",");
				phone1 = AbstractDianXinCrawler.executeJsFunc(
						"des/tel_com_des.js", "strEnc", login.getLoginName(),
						stra[0], stra[1], stra[2]);
				password1 = AbstractDianXinCrawler.executeJsFunc(
						"des/tel_com_des.js", "strEnc", login.getPassword(),
						stra[0], stra[1], stra[2]);
			} catch (Exception e) {
				phone1 = login.getLoginName();
				password1 = login.getPassword();
				logger.error("error",e);
			}

			String url = "https://uam.ct10000.com" + action;
			CHeader h = new CHeader(
					CHeaderUtil.Accept_,
					"https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=114.249.55.149",
					CHeaderUtil.Content_Type__urlencoded, "uam.ct10000.com",
					false);
			Map<String, String> param = new LinkedHashMap<String, String>();
			param.put("empoent", empoent);
			param.put("forbidaccounts", forbidaccounts);
			param.put("authtype", authtype);
			param.put("customFileld02", customFileld02);
			param.put("areaname", areaname);
			param.put("customFileld01", customFileld01);
			param.put("lt", lt);
			param.put("_eventId", _eventId);
			param.put("username", phone1);
			param.put("password", password1);
			param.put("randomId", login.getAuthcode());
			param.put("open_no", open_no);
			text = cutil.post(url, h, param);
			if (text != null) {
				if (text.length() < 300) {
					if (text.contains("https://uam.ct10000.com")) {
						return login2(text, param, h);
					}
				} else {
					doc = Jsoup.parse(text);
					Element el = doc.getElementById("status2");
					if (el != null) {
						errorMsg = el.text();

					}
				}
			}
		}
		return null;
	}

	public String login2(String url, Map<String, String> param, CHeader h) {

		String text = cutil.post(url, h, param);
		if (text != null && text.contains("location.replace")) {
			return login3(text);
		}
		return null;
	}

	/** 第三次post */
	public String login3(String text) {
		RegexPaserUtil rp = new RegexPaserUtil("location.replace\\('", "'",
				text, RegexPaserUtil.TEXTEGEXANDNRT);
		String url = rp.getText();
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "189.cn");
		CHeader h1 = new CHeader(CHeaderUtil.Accept_, null, null, "cq.189.cn");
		CHeader h2 = new CHeader(CHeaderUtil.Accept_, null, null, "www.189.cn");
		text = cutil.get(url, h);
		if (text != null) {
			text = cutil
					.get("http://www.189.cn/dqmh/cms/index/login.jsp?rand=1405350537319",
							h2);
			if (text != null) {
				text = cutil
						.get("http://www.189.cn/dqmh/frontLink.do?method=linkTo&shopId=10004&toStUrl=http://cq.189.cn/users/getTicket.htm?sendredirect=http://cq.189.cn/account/index.htm",
								h2);
				if (text != null) {
					rp = new RegexPaserUtil("location.replace\\('", "'", text,
							RegexPaserUtil.TEXTEGEXANDNRT);
					url = rp.getText();
					if (url != null) {
						rp = new RegexPaserUtil("http:\\/\\/", "\\/", url,
								RegexPaserUtil.TEXTEGEXANDNRT);
						String host = rp.getText();
						CHeader h3 = new CHeader(CHeaderUtil.Accept_, null,
								null, host);
						text = cutil.get(url, h3);
						if (text != null) {
							rp = new RegexPaserUtil("action=\"", "\"", text,
									RegexPaserUtil.TEXTEGEXANDNRT);
							url = rp.getText();
							if (url != null) {
								url = URLDecoder.decode(url);
								text = cutil.get(url, h1);
								if (text != null) {
									text = cutil
											.get("http://cq.189.cn/account/userInfo.htm",
													h1); // 200请求
									if (text != null) {
										return "1";
									}
								}

							}
						}

					}

				}

			}

		}
		return null;
	}

	public BigDecimal getYue() {
		bill();
		initDwr1();
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		BigDecimal phoneremain = new BigDecimal("0.00");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("callCount", "1");
		param.put("page", "page=/bill/bill.htm?id=1 ");
		param.put("httpSessionId", jsmap.get("httpSessionId").toString());
		param.put("scriptSessionId", jsmap.get("scriptSessionId").toString());
		param.put("c0-scriptName", "billDwr");
		param.put("c0-methodName", "getYE");
		param.put("c0-id", "0");
		param.put("c0-param0", "string:0");
		param.put("batchId", "0");
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "cq.189.cn");
		String text = cutil.post(
				"http://cq.189.cn/bill/dwr/call/plaincall/billDwr.getYE.dwr",
				h, param);
		if (text != null) {
			RegexPaserUtil rp = new RegexPaserUtil(">", "</font>", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String yue = rp.getText();
			if (yue != null) {
				phoneremain = new BigDecimal(yue.replaceAll("\\s*", ""));
			}
		}
		return phoneremain;
	}

	public void bill() {
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "189.cn");
		String text = cutil.get("http://cq.189.cn/bill/bill.htm?id=1", h);

	}

	public void initDwr1() {
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String url = "http://cq.189.cn/bill/dwr/engine.js";
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "cq.189.cn");
		String text = cutil.get(url, h);
		if (text != null) {
			RegexPaserUtil rp = new RegexPaserUtil(
					"dwr.engine._origScriptSessionId = \"", "\"", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String scriptSessionId = rp.getText();
			jsmap.put("scriptSessionId", scriptSessionId);
			rp = new RegexPaserUtil("dwr.engine._sessionCookieName = \"", "\"",
					text, RegexPaserUtil.TEXTEGEXANDNRT);
			String cName = rp.getText();
			CookieStore cookes = context.getCookieStore();
			List<Cookie> list = cookes.getCookies();

			String httpSessionId = "";
			for (Cookie cookie : list) {
				String cookieName = cookie.getName();
				// //System.out.println(cookieName+"==========");
				if (cName.equals(cookieName)) {
					httpSessionId = cookie.getValue();
					// System.out.println(httpSessionId);
					jsmap.put("httpSessionId", httpSessionId);
				}
			}
		}
	}

	public void initDwr() {
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String url = "http://cq.189.cn/account/dwr/engine.js";
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "cq.189.cn");

		String text = cutil.get(url, h);
		if (text != null) {

			RegexPaserUtil rp = new RegexPaserUtil(
					"dwr.engine._origScriptSessionId = \"", "\"", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String scriptSessionId = rp.getText();
			jsmap.put("scriptSessionId", scriptSessionId);
			rp = new RegexPaserUtil("dwr.engine._sessionCookieName = \"", "\"",
					text, RegexPaserUtil.TEXTEGEXANDNRT);
			String cName = rp.getText();
			CookieStore cookes = context.getCookieStore();
			List<Cookie> list = cookes.getCookies();

			String httpSessionId = "";
			for (Cookie cookie : list) {
				String cookieName = cookie.getName();

				if (cName.equals(cookieName)) {
					httpSessionId = cookie.getValue();

					jsmap.put("httpSessionId", httpSessionId);
				}
			}

		}
	}

}
