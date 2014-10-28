package com.lkb.thirdUtil.dx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.constant.DxConstant;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
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
import com.lkb.util.httpclient.CMapUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.constant.ConstantHC;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

import freemarker.template.utility.DateUtil;

public class SCDianXin extends BaseInfoMobile {
	protected static Logger logger = Logger.getLogger(JSDianxin.class);
	public String index = "http://sc.189.cn/login/login.jsp";

	// 验证码图片路径
	public static String imgurl = "http://sc.189.cn/kaptcha.jpg?";

	public static void main(String[] args) throws Exception {
	}

	public SCDianXin(Login login, String currentUser) {
		super(login, ConstantNum.comm_sc_dianxin, currentUser);
	}

	// public Map<String,Object> index(){
	// map.put("url",getAuthcode());
	// return map;
	// }

	public void init() {
		if (!isInit()) {
			String text = cutil.get(index);
			if (text != null) {
				setImgUrl(imgurl);
				setInit();
			}
			redismap.put("jsmap", map);// 根据实际需要存放
		}
	}

	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
		try {
			parseBegin(Constant.DIANXIN);
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("parentId", currentUser);
			map1.put("usersource", Constant.DIANXIN);
			map1.put("loginName", login.getLoginName());
			List<User> list = userService.getUserByParentIdSource(map1);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());
				user.setRealName(login.getLoginName());
				user.setIdcard("");
				// user.setAddr(lxdz);
				user.setUsersource(Constant.DIANXIN);
				user.setEmail("");
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());
				user.setRealName(login.getLoginName());
				user.setIdcard("");
				// user.setAddr(lxdz);
				user.setUsersource(Constant.DIANXIN);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				user.setEmail("");
				userService.saveUser(user);
			}

		} catch (Exception e) {
			logger.error("error",e);
			errorMsg = e.getMessage();
			sendWarningCallHistory(errorMsg);
		} finally {
			parseEnd(Constant.DIANXIN);
		}
	}

	public void getTelDetailHtml() {

		try {
			parseBegin(Constant.DIANXIN);
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader(CHeaderUtil.Accept_, "",
					CHeaderUtil.Content_Type__urlencoded, "sc.189.cn", true);

			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");

			for (String startDate : ms) {

				Map<String, String> param = new HashMap<String, String>();
				param.put("account", login.getLoginName());
				param.put("accountType", "50");
				param.put("citycode", jsmap.get("CITYCODE").toString());
				param.put("month", startDate);

				String text = cutil.post(
						"http://sc.189.cn/service/bill/billdetail.jsp", h,
						param);

				b = getTelDetailHtml_parse(text, startDate);
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

	/**
	 * 查询话费记录
	 */
	public boolean getTelDetailHtml_parse(String text, String startDate) {
		boolean b = true;
		try {
			if (text.contains("帐单周期")) {
				BigDecimal tcfd = new BigDecimal(0);
				BigDecimal mybjthf = new BigDecimal(0);
				BigDecimal mythf = new BigDecimal(0);
				BigDecimal total = new BigDecimal(0);
				RegexPaserUtil rp = new RegexPaserUtil(
						"<span class=\"item\">套餐费</span><span class=\"sum\">",
						"</span></dd>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String tcf = rp.getText();
				if (tcf != null) {
					tcfd = new BigDecimal(tcf.replaceAll("\\s*", ""));
					// total=total.add(tcfd);
				}
				RegexPaserUtil rp1 = new RegexPaserUtil(
						"<span class=\"item\"> 国内漫游被叫通话费</span><span class=\"sum\">",
						"</span></dd>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String mybjthfs = rp1.getText();
				if (mybjthfs != null) {
					mybjthf = new BigDecimal(mybjthfs.replaceAll("\\s*", ""));
					// total=total.add(mybjthf);
					mythf = mythf.add(mybjthf);
				}
				RegexPaserUtil rp2 = new RegexPaserUtil(
						"<span class=\"item\"> 国内漫游国内通话费</span><span class=\"sum\">",
						"</span></dd>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String mythfs = rp2.getText();
				if (mythfs != null) {
					BigDecimal ms1 = new BigDecimal(mythfs.replaceAll("\\s*",
							""));
					mythf = mythf.add(ms1);
					// total=total.add(ms1);
				}
				RegexPaserUtil rp3 = new RegexPaserUtil("本期费用合计：", "元", text,
						RegexPaserUtil.TEXTEGEXANDNRT);
				String hj = rp3.getText();
				if (hj != null) {
					BigDecimal hjs = new BigDecimal(hj.replaceAll("\\s*", ""));
					total = hjs;
				}
				Map map2 = new HashMap();
				map2.put("teleno", login.getLoginName());
				map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
				List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
				if (list2 != null && list2.size() > 0) {
					DianXinTel tel = (DianXinTel) list2.get(0);
					tel.setcAllPay(total);
					dianXinTelService.update(tel);
				} else {
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					String fday = TimeUtil.getFirstDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)));
					String eday = TimeUtil.getLastDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)));
					DianXinTel tel = new DianXinTel();
					UUID uuid = UUID.randomUUID();
					tel.setId(uuid.toString());
					// tel.setBaseUserId(currentUser);
					tel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
					tel.setTeleno(login.getLoginName());
					tel.setcAllPay(total);
					tel.setDependCycle(fday + "至" + eday);
					tel.setcName(login.getLoginName());
					tel.setZtcjbf(tcfd);
					tel.setMythf(mythf);
					dianXinTelService.saveDianXinTel(tel);
				}
			}

		} catch (Exception e) {
			logger.error("error",e);
			errorMsg = e.getMessage();
			b = false;
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
			if (text.contains("通话地点") && text.contains("通话类型")) {
				Document doc = Jsoup.parse(text);
				Elements tables = doc.select("table[id=_XD_query_table]");
				for (int i = 0; i < tables.size(); i++) {
					Element table1 = tables.get(i);
					String tableString = table1.text();
					if (tableString.contains("通话地点")
							&& tableString.contains("通话类型")) {
						Elements trs = table1.select("tr");
						if (trs.size() > 1) {
							Elements tdss = trs.get(1).select("td");
							if (tdss.size() > 10) {
								for (int j = 1; j < trs.size(); j++) {
									Elements tds = trs.get(j).select("td");
									String qssj = tds.get(0).text();
									String thsc = tds.get(1).html();
									RegexPaserUtil rp1 = new RegexPaserUtil(
											"<span style=\"display:none\">",
											"</span>", thsc,
											RegexPaserUtil.TEXTEGEXANDNRT);
									thsc = rp1.getText();

									String hjlx = tds.get(2).text();
									String dfhm = tds.get(3).text();
									String thdd = tds.get(4).text();
									String thlx = tds.get(5).text();
									String jbhmy = tds.get(6).text();
									String ctf = tds.get(7).text();
									String qtf = tds.get(8).text();
									String yh = tds.get(9).text();
									String zfy = tds.get(10).text();

									Map map2 = new HashMap();
									map2.put("phone", login.getLoginName());
									map2.put("cTime", DateUtils.StringToDate(
											qssj, "yyyy-MM-dd HH:mm:ss"));
									List list = dianXinDetailService
											.getDianXinDetailBypt(map2);
									if (list == null || list.size() == 0) {
										DianXinDetail dxDetail = new DianXinDetail();
										if (thlx.contains("本地")) {
											dxDetail.setTradeType("本地");
										} else {
											dxDetail.setTradeType("漫游");
										}

										/*
										 * int times = 0; try{ TimeUtil tunit =
										 * new TimeUtil(); times =
										 * tunit.timetoint(thsc);
										 * }catch(Exception e){
										 * 
										 * }
										 */

										UUID uuid = UUID.randomUUID();
										dxDetail.setId(uuid.toString());
										dxDetail.setTradeTime(Integer
												.parseInt(thsc));
										dxDetail.setcTime(DateUtils
												.StringToDate(qssj,
														"yyyy-MM-dd HH:mm:ss"));
										if (bean != null) {
											if (bean.getcTime().getTime() >= dxDetail
													.getcTime().getTime()) {
												return false;
											}
										}

										dxDetail.setTradeAddr(thdd);
										dxDetail.setCallWay(hjlx);
										dxDetail.setRecevierPhone(StringUtil
												.filterChinese(dfhm));
										dxDetail.setBasePay(new BigDecimal(
												jbhmy));
										dxDetail.setLongPay(new BigDecimal(ctf));
										dxDetail.setAllPay(new BigDecimal(zfy));
										dxDetail.setOtherPay(new BigDecimal(qtf));
										dxDetail.setPhone(login.getLoginName());

										dianXinDetailService
												.saveDianXinDetail(dxDetail);
									}
								}
							}
						}

					}
				}

			}

		} catch (Exception e) {
			logger.error("error",e);
			errorMsg = e.getMessage();
			b = false;
		}
		return b;
	}

	public boolean messageHistory_parse(String text, TelcomMessage bean,
			String startDate) {
		boolean b = true;
		try {
			if (text.contains("短信发送") && text.contains("通信类型")) {
				Document doc = Jsoup.parse(text);
				Elements tables = doc.select("table[id=_XD_query_table]");
				for (int i = 0; i < tables.size(); i++) {
					Element table1 = tables.get(i);
					String tableString = table1.text();
					if (tableString.contains("短信发送")
							&& tableString.contains("通信类型")) {
						Elements trs = table1.select("tr");
						if (trs.size() > 1) {
							Elements tdss = trs.get(1).select("td");
								for (int j = 1; j < trs.size(); j++) {
									Elements tds = trs.get(j).select("td");
									String type=tds.get(0).text();
									String phone=tds.get(1).text();
									String time=tds.get(2).text();
									String pay=tds.get(3).text();
									TelcomMessage dxDetail = new TelcomMessage();

									UUID uuid = UUID.randomUUID();
									dxDetail.setId(uuid.toString());
									dxDetail.setSentTime(DateUtils.StringToDate(
											time, "yyyy-MM-dd HH:mm:ss"));
									
//									if (bean != null) {
//										if (bean.getcTime().getTime() >= dxDetail
//												.getcTime().getTime()) {
//											return false;
//										}
//									}
									if(type.contains("接收"))
										dxDetail.setBusinessType("接收");
									else
										dxDetail.setBusinessType("发送");
									dxDetail.setAllPay(Math.abs(Double.parseDouble(pay)));
									dxDetail.setRecevierPhone(phone);
									dxDetail.setPhone(login.getLoginName());
									if(bean==null)
										telcomMessageService.save(dxDetail);
									else if(bean.getSentTime().before(dxDetail.getSentTime()))
										telcomMessageService.save(dxDetail);
								}
							}
					}

				}

			}

		} catch (Exception e) {
			logger.error("error",e);
			errorMsg = e.getMessage();
			b = false;
		}
		return b;
	}

	/**
	 * 查询通话记录
	 */
	public void callHistory() {
		try {
			parseBegin(Constant.DIANXIN);
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader(CHeaderUtil.Accept_, "",
					CHeaderUtil.Content_Type__urlencoded, "sc.189.cn", true);
			boolean b = false;
			int num = 0;

			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			DianXinDetail bean = new DianXinDetail(login.getLoginName());
			bean = dianXinDetailService.getMaxTime(bean);
			for (String startDate : ms) {
				String text = cutil
						.get("http://sc.189.cn/service/bill/resultBill.jsp?PAGENO=1&PRODNO="
								+ login.getLoginName()
								+ "~50&smsrand=&PASSWORD=&RANDOMENBR=&ck_month="
								+ startDate
								+ "&PRODTYPE=50&timeType=1&QTYPE=1&CITYCODE=0128&sessionid="
								+ jsmap.get("sessionid").toString()
								+ "&SHOWTICKETPWD=&IFCHANGENUM=0&startDayvalue=1&endDayvalue=31",
								h);
				// System.out.println("==="+text);
				if (text != null) {
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
				// 还未做message增量
				// DianXinMessage bean_mes=DianXinMessage.getMaxSentTime();
				TelcomMessage bean_mes=telcomMessageService.getMaxSentTime(login.getLoginName());
				String text1 = cutil
						.get("http://sc.189.cn/service/bill/resultBill.jsp?PAGENO=1&PRODNO="
								+ login.getLoginName()
								+ "~50&smsrand=&PASSWORD=&RANDOMENBR=&ck_month="
								+ startDate
								+ "&PRODTYPE=50&timeType=1&QTYPE=2&CITYCODE=0128&sessionid="
								+ jsmap.get("sessionid").toString()
								+ "&SHOWTICKETPWD=&IFCHANGENUM=0&startDayvalue=1&endDayvalue=31",
								h);
				// System.out.println("==="+text);
				if (text1 != null) {
					// null应该传bean_mes max
					b = messageHistory_parse(text1, bean_mes, startDate);
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
		int type = login.getType();
		switch (type) {
		case 1:
			getTelDetailHtml();// 账单记录
			getMyInfo(); // 个人信息
			break;
		case 2:

			callHistory(); // 历史账单
			gatherFlow();
			break;
		case 3:
			getTelDetailHtml();// 账单记录
			getMyInfo(); // 个人信息
			callHistory(); // 历史账单
			gatherFlow();
			break;
		default:
			break;
		}
	}

	// 首页登录
	public Map<String, Object> login() {
		try {
			String result = login1();
			if (result != null && result.equals("1")) {
				loginsuccess();
			} else if (result != null) {
				errorMsg = result;
			} else {
				errorMsg = "密码或验证码错误.";
			}

			if (status == 1) {
				sendPhoneDynamicsCode();
				addTask_1(this);
			}
		} catch (Exception e) {
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

	// 随机短信登录
	public Map<String, Object> checkPhoneDynamicsCode() {

		try {

			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			String sessionid = jsmap.get("sessionid").toString();
			if (sessionid != null) {
				Map<String, String> param = new HashMap<String, String>();
				param.put("CITYCODE", jsmap.get("CITYCODE").toString());
				param.put("IdCardInput", "");
				param.put("IdCardNo", "");
				param.put("PASSWORD", "");
				param.put("PRODNO", login.getLoginName() + "~50");
				param.put("PRODTYPE", "50");
				param.put("RANDOMENBR", "");
				param.put("SHOWTICKETPWD", "");
				param.put("ck_month", DateUtils.getMonths(2, "yyyyMM").get(1)
						.toString());
				param.put("endDayvalue", "31");
				param.put("fwpassword", "");
				param.put("sessionid", sessionid);
				param.put("smsrand", login.getPhoneCode());
				param.put("startDayvalue", "1");
				param.put("timeType", "0");
				CHeader h = new CHeader(CHeaderUtil.Accept_,
						"http://sc.189.cn/service/bill/querynew.jsp",
						CHeaderUtil.Content_Type__urlencoded, "sc.189.cn", true);
				String text = cutil
						.post("http://sc.189.cn/service/bill/resultBill.jsp?PAGENO=1",
								h, param);
				if (text.contains("通话详单")) {
					status = 1;
				} else {
					errorMsg = "您输入的查询验证码错误或过期，请重新核对或再次获取！";
				}

			}
		} catch (Exception e) {
			logger.error("error",e);
		}

		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		if (status == 1) {
			addTask_2(this);
		}
		return map;
	}

	/**
	 * 生成短信
	 * */
	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errorMsg = null;
		int status = 0;
		try {

			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			String xmls = "<buffalo-call>";
			xmls += "<method>getPhsSmsCode</method>";
			xmls += "<map>";
			xmls += "<type>java.util.HashMap</type>";
			xmls += "<string>PHONENUM</string>";
			xmls += "<string>" + login.getLoginName() + "</string>";
			xmls += "<string>PRODUCTID</string>";
			xmls += "<string>50</string>";
			xmls += "<string>CITYCODE</string>";
			xmls += "<string>" + jsmap.get("CITYCODE").toString() + "</string>";
			xmls += "</map>";
			xmls += "</buffalo-call>";
			CHeader h = new CHeader(CHeaderUtil.Accept_, null,
					CHeaderUtil.x_requested_with, "sc.189.cn", true);
			String text1 = cutil.post(
					"http://sc.189.cn/BUFFALO/buffalo/commonOrder", h, xmls);
			if (text1.contains("发送短信随机码成功")) {
				errorMsg = "发送成功";
				status = 1;
			} else if (text1.contains("短信发送过于频繁")) {
				errorMsg = "短信发送过于频繁";
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
		if (errorMsg == null) {
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		return map;
	}

	public BigDecimal getYue() {

		BigDecimal yue = new BigDecimal("0");
		CHeader h = new CHeader(CHeaderUtil.Accept_, "",
				CHeaderUtil.Content_Type__urlencoded, "sc.189.cn", false);
		Map<String, String> param = new LinkedHashMap<String, String>();

		String text = cutil.post(
				"http://sc.189.cn/service/account/getBalance.jsp", h, param);
		if (text != null && text.contains("ye")) {
			try {
				JSONObject json = new JSONObject(text);
				String ye = json.getString("ye");
				return new BigDecimal(ye);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("error",e);
			}
		}

		return yue;

	}

	public String login1() {

		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "sc.189.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("code", login.getAuthcode());
		String text = cutil.post("http://sc.189.cn/vip/login/validate.jsp", h,
				param);
		if (text != null && text.contains("S")) {
			return login2();
		} else {
			return "验证码错误.";
		}
	}

	public String login2() {
		String url = "http://sc.189.cn/common/login/login.jsp";
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "sc.189.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("ACCOUNT_T_L", login.getLoginName());
		param.put("CITYNAME", "0592");
		param.put("INVALIDATE_T", login.getAuthcode());
		param.put("LANDING_S", "50");
		param.put("LOGINFIRSTFLAG", "n");
		param.put("PASSWORD_P", login.getPassword());
		param.put("USER_LOGIN_SELECT_CHANGE_CITYCODE", "");
		param.put("accessPointer", "1");
		param.put("autologin", "2");
		param.put("citycode", "0592");
		param.put("ifReadChck", "on");
		param.put("pasd", "");
		param.put("randMark", "S");
		param.put("refererpath", "");
		param.put("selCheckWay", "19");
		param.put("submit.x", "21");
		param.put("submit.y", "6");

		String text = cutil.post(url, h, param);
		if (text != null && text.contains("http://uam.sc.189.cn/scLogin")) {
			Document doc = Jsoup.parse(text);
			String xml = doc.select("input[id=SSORequestXML]").first().val();
			param = new LinkedHashMap<String, String>();
			param.put("SSORequestXML", xml);
			CHeader h1 = new CHeader(CHeaderUtil.Accept_, null, null,
					"uam.sc.189.cn");
			text = cutil.post("http://uam.sc.189.cn/scLogin", h1, param);
			if (text != null && text.contains("LoginSSO?UATicket=-5")) {
				return "服务密码输入错误.";
			}
			if (text != null && text.contains("LoginSSO?UATicket=")) {
				text = cutil.get(text, h);
				if (text != null && text.contains("/service/account/index.jsp")) {
					String reqUrl = "http://sc.189.cn/service/account/index.jsp";
					text = cutil.get(reqUrl, h);
					if (text != null) {
						text = login3();
						if (text != null
								&& text.contains("\"ISLOGIN\":\"true\"")) {
							try {
								JSONObject json = new JSONObject(text);
								String CITYCODE = json.getString("CITYCODE");
								map.put("CITYCODE", CITYCODE);

								redismap.put("jsmap", map);// 根据实际需要存放
								getSessionId();
								return "1";
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								logger.error("error",e);
							}
						}

					}

				}
			}
		}
		return null;
	}

	public String login3() {

		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null, "sc.189.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		String text = cutil.post("http://sc.189.cn/common/ajax.jsp", h, param);
		if (text != null) {
			return text;
		}
		return null;
	}
	

	public boolean callFlow_parse(String text, String month) {
		boolean b = true;
		try {
			Document doc = Jsoup.parse(text);
			List<DianXinFlow> flowBillList = new ArrayList<DianXinFlow>();
			if (text.contains("您所查询月份中未产生相关通讯费用记录")) {
				// 流量账单
				Element table0 = doc.select("table[class=show_table2]").get(0);
				Elements trs0 = table0.select("tr");
				String dependCycle = trs0.get(2).select("td").get(0).text()
						.replaceAll("起止日期：", "");
				Date queryMonth = DateUtils.StringToDate(month, "yyyyMM");
				DianXinFlow dxFlow = new DianXinFlow();
				UUID uuid = UUID.randomUUID();
				dxFlow.setId(uuid.toString());
				dxFlow.setPhone(login.getLoginName());
				dxFlow.setDependCycle(dependCycle);
				dxFlow.setAllFlow(new BigDecimal(0));
				dxFlow.setAllPay(new BigDecimal(0.00));
				dxFlow.setAllTime(new BigDecimal(0));
				dxFlow.setQueryMonth(queryMonth);
				flowBillList.add(dxFlow);
			} else {
				// 流量账单
				Element table0 = doc.select("table[class=show_table2]").get(0);
				Elements trs0 = table0.select("tr");

				String dependCycle = trs0.get(2).select("td").get(0).text()
						.replaceAll("起止日期：", "");
				Date queryMonth = DateUtils.StringToDate(month, "yyyyMM");
				String allFlow = trs0.get(3).select("td").get(0).text()
						.replaceAll("总流量：", "");
				int allTime = TimeUtil.timetoint_HH_mm_ss(trs0.get(3)
						.select("td").get(1).text().replaceAll("总时长：", "")
						.replaceAll("小时", ":").replaceAll("分", ":")
						.replaceAll("秒", ""));
				String allPay = trs0.get(4).select("td").get(0).text()
						.replaceAll("总费用：", "").replaceAll("\\(元\\)", "")
						.replaceAll("（元）", "").trim();
				if("".equals(allPay)){
					allPay = "0";
				}
				DianXinFlow dxFlow = new DianXinFlow();
				UUID uuid = UUID.randomUUID();
				dxFlow.setId(uuid.toString());
				dxFlow.setPhone(login.getLoginName());
				dxFlow.setDependCycle(dependCycle);
				dxFlow.setAllFlow(new BigDecimal(StringUtil.flowFormat(allFlow)));
				dxFlow.setAllPay(new BigDecimal(allPay));
				dxFlow.setAllTime(new BigDecimal(allTime));
				dxFlow.setQueryMonth(queryMonth);
				flowBillList.add(dxFlow);

				// 流量详单
				Element table1 = doc.select("table[id=_XD_query_table]").get(0);
				Elements trs1 = table1.select("tr");
				List<DianXinFlowDetail> flowDetailList = new ArrayList<DianXinFlowDetail>();
				if (trs1.size() >= 2) {
					for (int i = 1; i < trs1.size(); i++) {
						DianXinFlowDetail dxFd = new DianXinFlowDetail();
						Elements tds1 = trs1.get(i).select("td");

						uuid = UUID.randomUUID();
						dxFd.setId(uuid.toString());
						dxFd.setPhone(login.getLoginName());
						Date time = DateUtils.StringToDate(tds1.get(1).text()
								.trim(), "yyyy-MM-dd HH:mm:ss");
						dxFd.setBeginTime(time);// 开始时间
						dxFd.setTradeTime(TimeUtil.timetoint(tds1.get(2).text()
								.trim()));// 上网时长
						dxFd.setFlow(new BigDecimal(tds1.get(3).text().trim()));// 总流量
						dxFd.setNetType(tds1.get(4).text().trim());// 通讯类型
						dxFd.setLocation(tds1.get(5).text().trim());// 通信地点
						dxFd.setBusiness(tds1.get(6).text().trim());// 使用业务
						dxFd.setFee(new BigDecimal(tds1.get(7).text().trim()));// 费用（元）
						flowDetailList.add(dxFd);
					}
					saveDianXinFlowDetail(flowDetailList);
				}
			}
			saveDianXinFlowBill(flowBillList);
		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
		}
		return b;
	}
	
	
	
	
	/**
	 * 查询流量账单
	 */
	public void gatherFlow() {
		try {
			
			parseBegin(Constant.DIANXIN);
			List<String> ms = DateUtils.getMonths(7, "yyyyMM");
			CHeader h = new CHeader(CHeaderUtil.Accept_, "",
					CHeaderUtil.Content_Type__urlencoded, "sc.189.cn", true);
			boolean b = false;
			int num = 0;
			Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			for (int i = ms.size() - 1; i >= 0; i--) {
				// http://sc.189.cn/service/bill/resultBill.jsp?PAGENO=1
				// PRODNO=15390037740%7E50&smsrand=&PASSWORD=&RANDOMENBR=&ck_month=201410&PRODTYPE=50&timeType=1&QTYPE=4&CITYCODE=0128&sessionid=hJGdJ7JNpGcCvFWr6dFPpyZDTTp28JM2qsCf87nlnkKPn1cnZSBy%21-1514317084%211413171661384&SHOWTICKETPWD=&IFCHANGENUM=0&startDayvalue=1&endDayvalue=30
				// http://sc.189.cn/service/bill/resultBill.jsp?PAGENO=1&PRODNO=15390037740~50&ck_month=201409&PRODTYPE=50&timeType=1&QTYPE=4&CITYCODE=0128&IFCHANGENUM=0&startDayvalue=1&endDayvalue=30
				Map<String, String> param = new HashMap<String, String>();
				param.put("PRODNO", login.getLoginName() + "~50");
				param.put("smsrand", "");
				param.put("PASSWORD", "");
				param.put("RANDOMENBR", "");
				param.put("ck_month", ms.get(i));
				param.put("PRODTYPE", "50");
				param.put("timeType", "1");
				param.put("QTYPE", "4");
				param.put("CITYCODE", jsmap.get("CITYCODE").toString());
				param.put("sessionid", jsmap.get("sessionid").toString());
				param.put("SHOWTICKETPWD", "");
				param.put("IFCHANGENUM", "0");
				param.put("startDayvalue", "1");
				param.put("endDayvalue",
						DateUtils.lastDayOfMonth(ms.get(i), "yyyyMM", "dd"));
				String text = cutil
						.post("http://sc.189.cn/service/bill/resultBill.jsp?PAGENO=1",
								h, param);
				Document doc6 = Jsoup.parse(text);
				//System.out.println(doc6.toString());
				if (doc6.toString() != null) {
					b = callFlow_parse(doc6.toString(), ms.get(i));
					if (!b) {
						// 异常信息
						if (errorMsg != null) {
							num++;
							// 超过五次,发送错误信息,123
							if (num > 5) {
								// 发送错误信息通知
								sendWarningFlow(errorMsg);
							}
							// 错误
							errorMsg = null;
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parseEnd(Constant.DIANXIN);
		}
	}

}
