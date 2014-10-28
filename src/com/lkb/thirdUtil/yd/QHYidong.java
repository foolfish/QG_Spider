package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtilTool;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

/**
 * 青海移动 By BX。Liu 账单查询（其他也有可能）速度较慢，每次（1个月）查询平均需要4秒左右，易触发超时设定
 * **/
public class QHYidong extends BaseInfoMobile {

	public static String imgurl = "https://qh.ac.10086.cn/servlet/CreateImage";

	public QHYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_qh_yidong, currentUser);
	}

	public QHYidong(Login login) {
		super(login);
	}

	public void init() {
		if (!isInit()) {
			// 登陆主页获取session
			if (cutil.get("https://qh.ac.10086.cn/login") != null) {
				setInit();
			}
			setImgUrl(imgurl);
		}
	}

	// 首页登录
	public Map<String, Object> login() {

		try {
			String result = login1();
			if (result == null) {

			} else if (result.equals("1")) {
				// map.put(CommonConstant.status, 1);
				loginsuccess();
			} else {
				errorMsg = result;
			}
		} catch (Exception e) {
			// ErrorMsg = "登陆过程中出现不可处理异常,请重新提交!";
			writeLogByLogin(e);
		}
		// map.put("url",getAuthcode());
		if (status == 1) {
			sendPhoneDynamicsCode();
			addTask_1(this);
		}
		return map;
	}

	private String login1() {
		CHeader h = new CHeader();
		h.setReferer("https://qh.ac.10086.cn/newLogin");
		h.setAccept(CHeaderUtil.Accept_);
		h.setHost("qh.ac.10086.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("authType", "1");
		param.put("code", login.getAuthcode());
		param.put("from", "login");
		param.put("password", login.getPassword());
		param.put("passwordTxt", login.getPassword());
		param.put("passwordsms", "");
		param.put("userName", login.getLoginName());
		String postResult = cutil.post("https://qh.ac.10086.cn/newLogin", h,
				param);
		try {
			if (postResult
					.contains("http://qh.10086.cn/ics/app?service=page/my.MyCount&listener=initPage"))
				return login2();
			else {
				Document doc = Jsoup.parse(postResult);
				Element e0 = doc.getElementsContainingOwnText("错误").first();
				return e0.text();
			}
		} catch (Exception e) {
			return null;
		}
	}

	private String login2() {
		CHeader h = new CHeader();
		h.setAccept(CHeaderUtil.Accept_);
		h.setHost("qh.ac.10086.cn");
		cutil.get(
				"http://qh.10086.cn/ics/app?service=page/my.MyCount&listener=initPage",
				h);
		String result = cutil
				.get("http://qh.10086.cn/ics/soLogin?phone=null&jtcmname=&nextUrl=my.MyCount",
						h);
		Map<String, String> param = new LinkedHashMap<String, String>();
		try {
			Document doc = Jsoup.parse(result);
			Elements eles = doc.getElementsByTag("input");
			for (int i = 0; i < eles.size(); i++) {
				param.put(eles.get(i).attr("name"), eles.get(i).attr("value"));
			}
			h.setReferer("http://qh.10086.cn/ics/soLogin?phone=null&jtcmname=&nextUrl=my.MyCount");
			String result1 = cutil.post(
					"http://qh.ac.10086.cn/getLoginRequest", h, param);
			cutil.get(result1);
			// Document doc1=Jsoup.parse(result1);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Object> checkPhoneDynamicsCode() {
		CHeader h = new CHeader();
		h.setReferer("http://qh.10086.cn/ics/app?service=page/personalinfo.CheckedSms&listener=initPage&mpageName=feequery.VoiceQuery");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("mpageName", "feequery.VoiceQuery");
		param.put("service", "direct/1/personalinfo.CheckedSms/$Form");
		param.put("sp", "S1");
		param.put("Form1", "bforgot,regetPwd");
		param.put("SMS_NUMBER", login.getPhoneCode());
		param.put("bforgot", "");// 发送乱码啊！fiddler抓的是"ȷ  ��",firebug抓的是" ²é Ñ¯"
		String result = cutil.post("http://qh.10086.cn/ics/app", h, param);
		if (result.contains("SAMLRequest")) {
			map.put(CommonConstant.errorMsg, "cookie已过期，请重新登录");
			map.put(CommonConstant.status, 0);
		} else if (result.contains("m15")) {
			map.put(CommonConstant.errorMsg, "验证码输入错误");
			map.put(CommonConstant.status, 0);
		} else if (result.contains("show_content")) {
			map.put(CommonConstant.status, 1);
			addTask_2(this);
		}
		return map;
	}

	public Map<String, Object> sendPhoneDynamicsCode() {
		String send = cutil
				.get("http://qh.10086.cn/ics/app?service=page/personalinfo.CheckedSms&listener=initPage&mpageName=feequery.VoiceQuery");
		if (send.contains("SAMLRequest")) {
			map.put(CommonConstant.errorMsg, "cookie已过期，请重新登录");
			map.put(CommonConstant.status, 0);
		}
		Document doc = Jsoup.parse(send);
		Map<String, String> param0 = new LinkedHashMap<String, String>();
		map.put(CommonConstant.status, 1);
		// map.put(CommonConsta, value)
		return map;
	}

	public void startSpider() {
		int type = login.getType();
		try {
			parseBegin(Constant.YIDONG);
			switch (type) {
			case 1:
				getMyInfo(); // 个人信息+余额
				getTelDetailHtml();// 账单记录
				break;
			case 2:
				callHistory(); // 通话账单
				messageHistory();// 短信详单
				flowHistory();// 流量详单
				break;
			case 3:
				getMyInfo(); // 个人信息+余额
				getTelDetailHtml();// 账单记录
				callHistory(); // 通话详单
				messageHistory();// 短信详单
				flowHistory();// 流量详单
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	private void flowHistory() {
		CHeader h = new CHeader();
		h.setReferer("http://qh.10086.cn/ics/app");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("service", "direct/1/feequery.VoiceQuery/$Form");
		param.put("sp", "S1");
		param.put("Form1", "bquery");
		param.put("SHOW_TYPE", "t0t");
		param.put("BILL_TYPE", "t3t");// 短信是t2t
		String[] puts = { "CALL_TYPE", "ROAM_TYPE", "LONG_TYPE", "bquery" };
		for (int i = 0; i < puts.length; i++)
			param.put(puts[i], "");
		List<MobileOnlineList> detailList = new ArrayList<MobileOnlineList>();
		List<MobileOnlineBill> billList = new ArrayList<MobileOnlineBill>();
		// 找出当前详单最大时间
		MobileOnlineList max_detail = mobileOnlineListService.getMaxTime(login
				.getLoginName());

		try {
			// 月循环
			List<String> months = DateUtils.getMonths(6, "yyyyMM");
			for (int i = months.size() - 1; i >= 0; i--) {
				param.put("MONTH", months.get(i));
				// 青海移动访问速度较慢，靠前的月份最好干脆就不访问了
				boolean pageEnd = false;
				int page = 1;

				Double list_allflow = 0.0;
				double list_fee = 0.0;
				MobileOnlineBill newBill=new MobileOnlineBill();
				newBill.setPhone(login.getLoginName());
				// 翻页循环，翻到没有就不翻了
				while (!pageEnd) {
					String pageXml = "";
					if (page == 1)
						pageXml = cutil.post("http://qh.10086.cn/ics/app", h,
								param);
					else {
						pageXml = cutil
								.get("http://qh.10086.cn/ics/app?service=ajaxDirect/1/feequery.VoiceQueryResult/feequery.VoiceQueryResult/javascript/refushBusiVoiceResult&pagename=feequery.VoiceQueryResult&eventname=queryBusi&pagination_iPage="
										+ page
										+ "&partids=refushBusiwlanResult&ajaxSubmitType=get&ajax_randomcode="
										+ 0.8612810603034525);
					}
					Document doc = Jsoup.parse(pageXml);
					Element result = null;
					Element monthList = null;
					if (page==1 && doc.getElementById("refushBusiwlanResult") == null) {
						pageEnd = true;
						break;
					}
					if (page == 1) {
						// 流量月账单抓取
						monthList = doc.getElementById("internetNow");
						Element zongliuliang = monthList.getElementsContainingOwnText(
								"总流量").first();
						if (zongliuliang != null) {
							String allFlow = zongliuliang.nextElementSibling()
									.text();
							allFlow = allFlow.replaceAll("\\(", "");
							allFlow = allFlow.replaceAll("\\)", "");
							list_allflow = StringUtil.flowFormat(allFlow);
							newBill.setTotalFlow(list_allflow.longValue());
						}
						Element shoufeiliuliang = monthList.getElementsContainingOwnText(
								"收费流量").first();
						if (shoufeiliuliang != null) {
							String allFlow = shoufeiliuliang.nextElementSibling()
									.text();
							allFlow = allFlow.replaceAll("\\(", "");
							allFlow = allFlow.replaceAll("\\)", "");
							list_allflow = StringUtil.flowFormat(allFlow);
							newBill.setChargeFlow(list_allflow.longValue());
						}
						Element mianfeiliuliang = monthList.getElementsContainingOwnText(
								"免费流量").first();
						if (mianfeiliuliang != null) {
							String allFlow = mianfeiliuliang.nextElementSibling()
									.text();
							allFlow = allFlow.replaceAll("\\(", "");
							allFlow = allFlow.replaceAll("\\)", "");
							list_allflow = StringUtil.flowFormat(allFlow);
							newBill.setFreeFlow(list_allflow.longValue());
						}
						
						result = doc.getElementById("refushBusiwlanResult")
								.child(0);
						
					} else {
						pageXml = pageXml.replaceAll("&lt;", "<");
						pageXml = pageXml.replaceAll("&gt;", ">");
						doc = Jsoup.parse(pageXml);
						// String a1=StringUtil.convertWML(pageXml);
						if(doc.getElementById("refushBusiwlanResult")==null){
							pageEnd=true;
							break;
						}
						result = doc.getElementById("refushBusiwlanResult")
								.child(0);// 底下的table没有id
					}
					if (result == null || !result.hasText()) {
						pageEnd = true;// 短信这里在前搞了这个了~这里有备无患吧
						break;
					}
					Element result1 = result.getElementsByTag("tbody").first();
					Elements trs = result1.getElementsByTag("tr");
					if (trs.size() < 10)
						pageEnd = true;// 没有下一页，不要翻页了！
					for (int k = 0; k < trs.size(); k++) {
						MobileOnlineList detail = new MobileOnlineList();
						UUID uuid = UUID.randomUUID();
						detail.setId(uuid.toString());
						detail.setPhone(login.getLoginName());
						// 因为Date不带年份，可鞥出现去年的情况！
						int year, x = Integer.parseInt(months.get(i).substring(
								4, 6));
						int y = Integer.parseInt(months.get(0).substring(4, 6));
						if (x > y)
							year = new Date().getYear() + 1900 - 1;
						else
							year = new Date().getYear() + 1900;
						Date nowDate = DateUtils.StringToDate(year
								+ trs.get(k).child(0).text(),
								"yyyyMM-dd HH:mm:ss");
						if (max_detail != null
								&& !max_detail.getcTime().before(nowDate))
							break;
						detail.setcTime(nowDate);
						if (page == 1) {
							detail.setTradeAddr(trs.get(k).child(1).text());
							detail.setOnlineType(trs.get(k).child(2).text());
							Long time = (long) TimeUtil.timetoint(trs.get(k)
									.child(3).text());
							detail.setOnlineTime(time);
							String flow = trs.get(k).child(4).text();
							flow = flow.replaceAll("\\(", "");
							flow = flow.replaceAll("\\)", "");
							Double allflow = StringUtil.flowFormat(flow);
							// detail.setTradeWay(trs.get(k).child(3).text());
							detail.setTotalFlow(allflow.longValue());
							detail.setCheapService(trs.get(k).child(5).text());
							Double fee = Double.parseDouble(trs.get(k).child(6)
									.text());
							list_fee += fee;
							detail.setCommunicationFees(new BigDecimal(fee));
						} else {
							detail.setTradeAddr(StringUtil.convertWML(trs
									.get(k).child(1).text()));
							detail.setOnlineType(trs.get(k).child(2).text());
							Long time = (long) TimeUtil.timetoint(StringUtil
									.convertWML(trs.get(k).child(3).text()));
							detail.setOnlineTime(time);
							String flow = trs.get(k).child(4).text();
							flow = flow.replaceAll("\\(", "");
							flow = flow.replaceAll("\\)", "");
							Double allflow = StringUtil.flowFormat(flow);
							// detail.setTradeWay(trs.get(k).child(3).text());
							detail.setTotalFlow(allflow.longValue());
							detail.setCheapService(StringUtil.convertWML(trs
									.get(k).child(5).text()));
							Double fee = Double.parseDouble(trs.get(k).child(6)
									.text());
							list_fee += fee;
							detail.setCommunicationFees(new BigDecimal(fee));
						}
						detailList.add(detail);
					}
					page++;
				}
				// 月循环最后
				newBill.setTrafficCharges(new BigDecimal(list_fee));
				DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				newBill.setMonthly(df.parse(months.get(i)+"01 00:00:00"));
				billList.add(newBill);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.saveMobileOnlineBill(billList);
		mobileOnlineListService.insertbatch(detailList);
	}

	private void messageHistory() {
		CHeader h = new CHeader();
		h.setReferer("http://qh.10086.cn/ics/app");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("service", "direct/1/feequery.VoiceQuery/$Form");
		param.put("sp", "S1");
		param.put("Form1", "bquery");
		param.put("SHOW_TYPE", "t0t");
		param.put("BILL_TYPE", "t2t");// 短信是t2t
		String[] puts = { "CALL_TYPE", "ROAM_TYPE", "LONG_TYPE", "bquery" };
		for (int i = 0; i < puts.length; i++)
			param.put(puts[i], "");
		List<MobileMessage> detailList = new ArrayList<MobileMessage>();
		// 找出当前详单最大时间
		MobileMessage max_detail = mobileMessageService.getMaxSentTime(login
				.getLoginName());
		try {
			// 月循环
			List<String> months = DateUtils.getMonths(6, "yyyyMM");
			for (int i = months.size() - 1; i >= 0; i--) {
				param.put("MONTH", months.get(i));
				// 青海移动访问速度较慢，靠前的月份最好干脆就不访问了
				boolean pageEnd = false;
				int page = 1;

				// 翻页循环，翻到没有就不翻了
				while (!pageEnd) {
					String pageXml = "";
					if (page == 1)
						pageXml = cutil.post("http://qh.10086.cn/ics/app", h,
								param);
					else {
						pageXml = cutil
								.get("http://qh.10086.cn/ics/app?service=ajaxDirect/1/feequery.VoiceQueryResult/feequery.VoiceQueryResult/javascript/refushBusiVoiceResult&pagename=feequery.VoiceQueryResult&eventname=queryBusi&pagination_iPage="
										+ page
										+ "&partids=refushBusiVoiceResult&ajaxSubmitType=get&ajax_randomcode="
										+ 0.8612810603034525);
					}
					Document doc = Jsoup.parse(pageXml);
					Element result = null;
					if (page==1 && doc.getElementById("refushBusiDxResult") == null) {
						pageEnd = true;
						break;
					}
					if (page == 1)
						result = doc.getElementById("refushBusiDxResult")
								.child(0);
					else {
						pageXml = pageXml.replaceAll("&lt;", "<");
						pageXml = pageXml.replaceAll("&gt;", ">");
						doc = Jsoup.parse(pageXml);
						// String a1=StringUtil.convertWML(pageXml);
						if(doc.getElementById("refushBusiDxResult")==null){
							pageEnd=true;
							break;
						}
						result = doc.getElementById("refushBusiDxResult")
								.child(0);// 底下的table没有id
					}
					if (result == null || !result.hasText()) {
						pageEnd = true;// 短信这里在前搞了这个了~这里有备无患吧
						break;
					}
					Element result1 = result.getElementsByTag("tbody").first();
					Elements trs = result1.getElementsByTag("tr");
					if (trs.size() < 10)
						pageEnd = true;// 没有下一页，不要翻页了！
					for (int k = 0; k < trs.size(); k++) {
						MobileMessage detail = new MobileMessage();
						UUID uuid = UUID.randomUUID();
						detail.setId(uuid.toString());
						detail.setPhone(login.getLoginName());
						// 因为Date不带年份，可鞥出现去年的情况！
						int year, x = Integer.parseInt(months.get(i).substring(
								4, 6));
						int y = Integer.parseInt(months.get(0).substring(4, 6));
						if (x > y)
							year = new Date().getYear() + 1900 - 1;
						else
							year = new Date().getYear() + 1900;
						Date nowDate = DateUtils.StringToDate(year
								+ trs.get(k).child(0).text(),
								"yyyyMM-dd HH:mm:ss");
						if (max_detail != null
								&& !max_detail.getSentTime().before(nowDate))
							break;
						detail.setSentTime(nowDate);
						if (page == 1) {
							detail.setSentAddr(trs.get(k).child(1).text());
							detail.setRecevierPhone(trs.get(k).child(2).text());
							if (trs.get(k).child(3).text().contains("收"))
								detail.setTradeWay("接收");
							else
								detail.setTradeWay("发送");
							// detail.setTradeWay(trs.get(k).child(3).text());
							detail.setAllPay(new BigDecimal(trs.get(k).child(7)
									.text()));
						} else {
							detail.setSentAddr(StringUtil.convertWML(trs.get(k)
									.child(1).text()));
							detail.setRecevierPhone(trs.get(k).child(2).text());
							if (StringUtil.convertWML(
									trs.get(k).child(3).text()).contains("收"))
								detail.setTradeWay("接收");
							else
								detail.setTradeWay("发送");
							// detail.setTradeWay(StringUtil.convertWML(trs.get(k).child(3).text()));
							detail.setAllPay(new BigDecimal(trs.get(k).child(7)
									.text()));
						}
						detailList.add(detail);
					}
					page++;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mobileMessageService.insertbatch(detailList);
	}

	private void callHistory() {
		CHeader h = new CHeader();
		h.setReferer("http://qh.10086.cn/ics/app");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("service", "direct/1/feequery.VoiceQuery/$Form");
		param.put("sp", "S1");
		param.put("Form1", "bquery");
		param.put("SHOW_TYPE", "t0t");
		param.put("BILL_TYPE", "t1t");// 短信是t2t
		String[] puts = { "CALL_TYPE", "ROAM_TYPE", "LONG_TYPE", "bquery" };
		for (int i = 0; i < puts.length; i++)
			param.put(puts[i], "");
		List<MobileDetail> detailList = new ArrayList<MobileDetail>();
		// 找出当前详单最大时间
		MobileDetail max_detail = new MobileDetail(login.getLoginName());
		max_detail = mobileDetailService.getMaxTime(max_detail);
		try {
			// 月循环
			List<String> months = DateUtils.getMonths(6, "yyyyMM");
			for (int i = months.size() - 1; i >= 0; i--) {
				param.put("MONTH", months.get(i));
				// 青海移动访问速度较慢，靠前的月份最好干脆就不访问了
				boolean pageEnd = false;
				int page = 1;

				// 翻页循环，翻到没有就不翻了
				while (!pageEnd) {
					String pageXml = "";
					if (page == 1)
						pageXml = cutil.post("http://qh.10086.cn/ics/app", h,
								param);
					else {
						pageXml = cutil
								.get("http://qh.10086.cn/ics/app?service=ajaxDirect/1/feequery.VoiceQueryResult/feequery.VoiceQueryResult/javascript/refushBusiVoiceResult&pagename=feequery.VoiceQueryResult&eventname=queryBusi&pagination_iPage="
										+ page
										+ "&partids=refushBusiVoiceResult&ajaxSubmitType=get&ajax_randomcode="
										+ 0.8612810603034525);
					}
					Document doc = Jsoup.parse(pageXml);
					Element result = null;
					if (page == 1)
						result = doc.getElementById("table");
					else {
						pageXml = pageXml.replaceAll("&lt;", "<");
						pageXml = pageXml.replaceAll("&gt;", ">");
						doc = Jsoup.parse(pageXml);
						// String a1=StringUtil.convertWML(pageXml);
						result = doc.getElementById("table");
					}
					if (result == null || !result.hasText()) {
						pageEnd = true;// 好像break的话就没啥用了...以防万一吧
						break;
					}
					Element result1 = result.getElementsByTag("tbody").first();
					Elements trs = result1.getElementsByTag("tr");
					if (trs.size() < 10)
						pageEnd = true;// 没有下一页，不要翻页了！
					for (int k = 0; k < trs.size(); k++) {
						MobileDetail detail = new MobileDetail();
						UUID uuid = UUID.randomUUID();
						detail.setId(uuid.toString());
						detail.setPhone(login.getLoginName());
						// 因为Date不带年份，可鞥出现去年的情况！
						int year, x = Integer.parseInt(months.get(i).substring(
								4, 6));
						int y = Integer.parseInt(months.get(0).substring(4, 6));
						if (x > y)
							year = new Date().getYear() + 1900 - 1;
						else
							year = new Date().getYear() + 1900;
						Date nowDate = DateUtils.StringToDate(year
								+ trs.get(k).child(0).text(),
								"yyyyMM-dd HH:mm:ss");
						if (max_detail != null
								&& !max_detail.getcTime().before(nowDate))
							break;
						detail.setcTime(nowDate);
						if (page == 1) {
							detail.setTradeAddr(trs.get(k).child(1).text());
							detail.setTradeWay(trs.get(k).child(2).text());
							detail.setRecevierPhone(trs.get(k).child(3).text());
							detail.setTradeTime(TimeUtil.timetoint(trs.get(k)
									.child(4).text()));
							detail.setTradeType(trs.get(k).child(5).text());
							detail.setOnlinePay(new BigDecimal(trs.get(k)
									.child(7).text()));
						} else {
							detail.setTradeAddr(StringUtil.convertWML(trs
									.get(k).child(1).text()));
							detail.setTradeWay(StringUtil.convertWML(trs.get(k)
									.child(2).text()));
							detail.setRecevierPhone(trs.get(k).child(3).text());
							detail.setTradeTime(TimeUtil.timetoint(StringUtil
									.convertWML(trs.get(k).child(4).text())));
							detail.setTradeType(StringUtil.convertWML(trs
									.get(k).child(5).text()));
							detail.setOnlinePay(new BigDecimal(trs.get(k)
									.child(7).text()));
						}
						detailList.add(detail);
					}
					page++;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mobileDetailService.insertbatch(detailList);
	}

	private void getTelDetailHtml() {
		CHeader h = new CHeader();
		h.setReferer("http://qh.10086.cn/ics/app");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("service", "direct/1/feequery.BillQuery/$Form");
		param.put("sp", "S1");
		param.put("Form1", "$RadioGroup,$Submit");
		param.put("$Submit", "");// 发送乱码啊！fiddler抓的是“�� ѯ",firebug抓的是" ²é Ñ¯"
		String[] puts = { "PageName", "MAIL_SERVICE_ID", "MAIL_SERVICE_NAME",
				"MAIL_SMS_BILL", "PageName", "SERVICE_ID", "SMS_SERVICE_NAME",
				"SMS_BILL", "PageName", "MMS_SERVICE_ID", "MMS_SERVICE_NAME",
				"MMS_SMS_BILL" };
		for (int i = 0; i < puts.length; i++)
			param.put(puts[i], "");
		MobileTel tel0 = new MobileTel();
		tel0.setTeleno(login.getLoginName());
		MobileTel tel_max = mobileTelService.getMaxTime(tel0);
		List<MobileTel> telList = new ArrayList<MobileTel>();
		try {
			List<String> months = DateUtils.getMonths(6, "yyyyMM");
			for (int i = months.size() - 1; i >= 0; i--) {
				param.put("MONTH", months.get(i));
				int isUpdate = 0;
				MobileTel tel = new MobileTel();
				tel.setTeleno(login.getLoginName());
				tel.setcTime(DateUtilTool.toDate(months.get(i) + "01"));
				if (tel_max == null) {
					UUID uuid = UUID.randomUUID();
					tel.setId(uuid.toString());
				} else if (tel_max.getcTime().before(
						DateUtilTool.toDate(months.get(i) + "01"))) {
					UUID uuid = UUID.randomUUID();
					tel.setId(uuid.toString());
				}
				// 本月应该是更新的，但是batch里没有写
				else if (tel_max.getcTime().equals(
						DateUtilTool.toDate(months.get(i) + "01"))) {
					isUpdate = 1;
					tel = tel_max;
				} else
					continue;
				// 只更新最新的一个月账单！不然账单POST请求太慢了！
				String result = cutil.post("http://qh.10086.cn/ics/app", h,
						param);
				Document doc = Jsoup.parse(result);
				Elements eles = doc.getElementsByClass("listtable6");
				Element ele = eles.get(3);// 这个没办法，html乱七八糟，只能数数了~
				// System.out.println(ele);
				Elements tds = ele.getElementsByTag("td");
				double allpay = 0;
				for (int j = 0; j < tds.size(); j++) {
					if (tds.get(j).text().contains("套餐及固定费")) {
						tel.setTcgdf(new BigDecimal(tds.get(++j).text()));
						allpay += Double.parseDouble(tds.get(j).text());
					} else if (tds.get(j).text().contains("短彩信费")) {
						tel.setTcwdxf(new BigDecimal(tds.get(++j).text()));
						allpay += Double.parseDouble(tds.get(j).text());
					} else if (tds.get(j).text().contains("语音通信费")) {
						tel.setTcwyytxf(new BigDecimal(tds.get(++j).text()));
						allpay += Double.parseDouble(tds.get(j).text());
					} else if (tds.get(j).text().contains("其他费用")) {
						tel.setTcwyytxf(new BigDecimal(tds.get(++j).text()));
						allpay += Double.parseDouble(tds.get(j).text());
					}else if (tds.get(j).text().contains("上网费")) {
						allpay += Double.parseDouble(tds.get(j).text());
					}
				}
				tel.setcAllPay(new BigDecimal(allpay));
				if (isUpdate == 0)
					mobileTelService.saveMobileTel(tel);
				else
					mobileTelService.update(tel);
				// telList.add(tel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// mobileTelService.insertbatch(telList);
	}

	private void getMyInfo() {
		String info = cutil
				.get("http://qh.10086.cn/ics/app?service=page/personalinfo.CustInfoMod&listener=initPage");
		// 先把余额写了吧，几行很简单，一个get完事
		String yue_info = cutil
				.get("http://qh.10086.cn/ics/app?service=page/feequery.BalanceQuery&listener=queryOPer");
		Document doc_yue = Jsoup.parse(yue_info);
		Element yue_e = doc_yue.getElementsContainingOwnText("账户余额为").last();
		String yue = yue_e.nextElementSibling().text().replaceAll("元", "");

		Document doc = Jsoup.parse(info);
		String name = doc.getElementById("CUST_NAME").attr("value");
		String address = doc.getElementById("POST_ADDRESS").attr("value");
		String email = doc.getElementById("EMAIL").attr("value");
		String contact_phone = doc.getElementById("CONTACT_PHONE")
				.attr("value");
		// Element e0=doc.getElementsByTag("table").last();
		// System.out.println(e0);
		Map<String, String> parmap = new HashMap<String, String>(3);
		parmap.put("parentId", currentUser);
		parmap.put("loginName", login.getLoginName());
		parmap.put("usersource", Constant.YIDONG);
		List<User> list = userService.getUserByParentIdSource(parmap);
		if (list != null && list.size() > 0) {
			User user = list.get(0);
			user.setLoginName(login.getLoginName());
			user.setLoginPassword("");
			user.setUserName("");
			user.setRealName(name);
			user.setAddr(address);
			user.setUsersource(Constant.YIDONG);
			user.setParentId(currentUser);
			user.setModifyDate(new Date());
			user.setPhone(login.getLoginName());
			user.setPhoneRemain(new BigDecimal(yue));
			user.setEmail(email);
			userService.update(user);
		} else {
			User user = new User();
			UUID uuid = UUID.randomUUID();
			user.setId(uuid.toString());
			user.setLoginName(login.getLoginName());
			user.setLoginPassword("");
			user.setUserName("");
			user.setRealName(name);
			user.setAddr(address);
			user.setUsersource(Constant.YIDONG);
			user.setParentId(currentUser);
			user.setModifyDate(new Date());
			user.setPhone(login.getLoginName());
			user.setPhoneRemain(new BigDecimal(yue));
			user.setEmail(email);
			userService.saveUser(user);
		}

	}

	public static void main(String[] args) {
		QHYidong qh = new QHYidong(new Login("13997436394", "809713"));
		qh.index();
		qh.inputCode(imgurl);
		qh.login();
		if (qh.islogin()) {
			qh.close();
			// qh.sendPhoneDynamicsCode();
			qh.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
			qh.checkPhoneDynamicsCode();
			qh.callHistory();
		}
	}

}
