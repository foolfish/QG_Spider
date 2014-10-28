package com.lkb.thirdUtil.dx;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;
import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class GSDianxin extends BaseInfoMobile {
	protected static Logger logger = Logger.getLogger(GSDianxin.class);
	// 验证码图片路径
	public static String imgurl = "http://gs.189.cn/web/validateCode.action?rand=";
	private LinkedList<MonthlyBillMark> flowBarks;
	private Date bigTime = getMaxFlowTime();
	public GSDianxin(Login login, String currentUser) {
		super(login, ConstantNum.comm_gs_dianxin, currentUser);
		userSource = Constant.DIANXIN;
	}

	public void init() {
		// 就一步操作,所以不需要判断是否初始化
		setImgUrl(imgurl);
	}

	/**
	 * 3,采集全部 1.采集手机验证 2.采集手机已经验证
	 * 
	 * @param type
	 */
	public void startSpider() {
		parseBegin();
		try {
			int type = login.getType();
			switch (type) {
			case 1:
				getMonthlyBill();// 账单详情

				getUser(); // 个人信息

				getCallHistory(); // 历史账单
				saveMessage(); // 短信账单
				saveFlowList();
				saveFlowBill();
				break;
			case 2:
				break;
			case 3:
				getMonthlyBill();// 账单详情
				getUser(); // 个人信息
				getCallHistory(); // 历史账单
				saveMessage(); // 短信账单

				break;
			default:
				break;
			}
		} finally {
			parseEnd();
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
			}
			if (status == 1) {
				addTask_1(this);
			}
		} catch (Exception e) {
			writeLogByLogin(e);
			logger.error("error", e);
		}
		return map;
	}

	public String login1() {
		String reqUrl = "http://gs.189.cn/web/jsonlogin/loginBlockAction.action?randomnumber="
				+ login.getAuthcode()
				+ "&logintype=4&forwordpage=&isInterceptor=0&isRemPsw=0&isShowRandom=truec&number="
				+ login.getLoginName()
				+ "&city=9999&pwdtype=1&password="
				+ login.getPassword() + "&srand=" + login.getAuthcode();
		CHeader h = new CHeader(CHeaderUtil.Accept_json, null, null,
				"gs.189.cn");
		String text = cutil.get(reqUrl, h);
		if (text != null) {
			if (text.contains("returnCode\\\":\\\"4\\\"")) {
				text = cutil.get(
						"http://gs.189.cn/web/self/accountHomeIndex.action", h);
				if (text != null) {
					return "1";
				}
			} else if (text.contains("系统繁忙，请稍后再试")) {
				return "验证码错误";
			} else if (text.contains("账户或密码错误")) {
				return "账户或密码错误";
			}
		}
		return null;
	}

	public User spiderInfo() {
		try {
			// CHeader h = new
			// CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"gs.189.cn",true);
			String myinfo = cutil
					.get("http://gs.189.cn/web/self/showMyProfile.action?randomnumber1=20711");
			// System.out.println(myinfo);
			Document doc3 = Jsoup.parse(myinfo);
			Elements tables = doc3.select("table");
			if (tables.size() > 0) {
				Element table = tables.get(0);
				String xm = table.select("tr").get(0).select("td").get(0)
						.text();
				String lxdh = table.select("tr").get(0).select("td").get(1)
						.text();
				String sfzh = table.select("tr").get(1).select("td").get(1)
						.text();
				table = tables.get(1);
				String lxr = table.select("tr").get(0).select("td").get(0)
						.text();
				String email = table.select("tr").get(1).select("td").get(0)
						.text();
				User user = new User();
				user.setUserName(xm);
				user.setRealName(xm);
				user.setIdcard(sfzh);
				// user.setAddr(lxdz);
				user.setEmail(email);
				user.setFixphone(lxdh);
				user.setPhoneRemain(getYue());
				String text = cutil
						.get("http://gs.189.cn/web/self/showMyPackage.action?productGroup=4:"
								+ login.getLoginName());
				// System.out.println(text);
				doc3 = Jsoup.parse(text);
				text = doc3.select("table").get(0).select("strong").get(0)
						.text();
				user.setPackageName(text);
				return user;
			}
		} catch (Exception e) {
			writeLogByInfo(e);
			logger.error("error", e);
		}
		return null;
	}

	/**
	 * 月度账单
	 */
	public List<DianXinTel> spiderMonthlyBill() {
		List<DianXinTel> list = new ArrayList<DianXinTel>();
		boolean isUpdate = false;
		List<String> ms = DateUtils.getMonths(6, "yyyy-MM");
		Map<String, String> param = new HashMap<String, String>();
		param.put("productInfo", "4:" + login.getLoginName());
		Set<Date> set = getMonthlyBillMaxNumTel();
		System.out.println(set);
		Date dtime = null;
		if (set != null && set.size() > 0) {
			dtime = set.iterator().next();
		}
		Date d = null;
		for (String startDate : ms) {
			param.put("detailMonth", startDate);
			d = DateUtils.StringToDate(startDate, "yyyy-MM");
			if (dtime != null) {
				// 检测是否数据库有相等月份,如果包含,是否数据库最大月份在里边,主要是更新数据库中最大月份的话费清单,其他跳过
				if (set.contains(d)) {
					if (d.getTime() != dtime.getTime()) {
						continue;
					} else {
						isUpdate = true;
					}
				}
			}
			String text = cutil
					.post("http://gs.189.cn/web/fee/getBillPayDetail.action?t=1407745400349",
							param);
			try {
				if (text != null && text.contains("费用项目")
						&& text.contains("本期费用合计")) {
					BigDecimal myf = new BigDecimal("0.00");
					BigDecimal tcf = new BigDecimal("0.00");
					BigDecimal total = new BigDecimal(0);
					Document doc = Jsoup.parse(text);
					Elements tables = doc.select("table[ class=bill_list]");
					Element table = tables.get(0);
					Elements trs = table.select("tr");
					for (int i = 0; i < trs.size(); i++) {
						Element tr = trs.get(i);
						String trString = tr.text();
						if (trString.contains("套餐功能费")) {
							String tcfs = tr.select("td").get(1).text()
									.replaceAll("\\s*", "").replace(" ", "");
							if (tcfs != null) {
								tcf = new BigDecimal(tcfs);
							}
						} else if (trString.contains("国内漫游通话费")) {
							String myfs = tr.select("td").get(1).text()
									.replaceAll("\\s*", "").replace(" ", "");
							if (myfs != null) {
								myf = new BigDecimal(myfs);
							}
						}
					}
					RegexPaserUtil rp = new RegexPaserUtil("本期费用合计：<b>",
							"</b>", text, RegexPaserUtil.TEXTEGEXANDNRT);
					String hj = rp.getText().replaceAll("\\s*", "");
					total = new BigDecimal(hj);

					if (isUpdate) {
						updateTel(d, total);
						isUpdate = false;
					} else {
						DianXinTel tel = new DianXinTel();
						tel.setcTime(d);
						tel.setcAllPay(total);
						String year = startDate.substring(0, 4);
						String mouth = startDate.substring(5, 7);
						String fday = TimeUtil.getFirstDayOfMonth(Integer
								.parseInt(year), Integer.parseInt(DateUtils
								.formatDateMouth(mouth)));
						String eday = TimeUtil.getLastDayOfMonth(Integer
								.parseInt(year), Integer.parseInt(DateUtils
								.formatDateMouth(mouth)));
						tel.setDependCycle(fday + "至" + eday);
						tel.setcName("");
						tel.setZtcjbf(tcf);
						tel.setMythf(myf);
						list.add(tel);
					}
				}
			} catch (Exception e) {
				writeLogByZhangdan(e);
				logger.error("error", e);
			}
		}
		return list;
	}

	/**
	 * 查询通话记录
	 */
	public List spiderCallHistory() {

		List<String> ms = DateUtils.getMonths(6, "yyyyMM");
		List<DianXinDetail> list = new ArrayList<DianXinDetail>();
		Date date = getMaxTime();
		boolean isCountine = true;
		for (String startDate : ms) {
			try {
				String text = cutil
						.get("http://gs.189.cn/web/json/searchDetailedFee.action?timestamp="
								+ new Date().getTime()
								+ "&productGroup=4:"
								+ login.getLoginName()
								+ "&orderDetailType=6&queryMonth=" + startDate);
				JSONObject json = new JSONObject(text);
				text = json.getString("jsonResult");
				if (text != null && !text.equals("null")) {
					JSONObject json1 = new JSONObject(text);
					JSONArray jr = json1.getJSONArray("trList");
					Date cTime = null;
					for (int i = 0; i < jr.length(); i++) {
						JSONObject jsonobject = jr.getJSONObject(i);
						String thsj = jsonobject.getString("val0");
						String hjfs = jsonobject.getString("val1");
						String dfhm = jsonobject.getString("val2");
						String thsc = jsonobject.getString("val3");
						String je = jsonobject.getString("val4");
						String thdd = jsonobject.getString("val5");
						String thlx = jsonobject.getString("val6");
						cTime = DateUtils.StringToDate(thsj,
								"yyyy-MM-dd HH:mm:ss");
						if (date != null) {
							if (date.getTime() >= cTime.getTime()) {
								isCountine = false;// 终止循环
								break;
							}
						}
						DianXinDetail dxDetail = new DianXinDetail();
						dxDetail.setcTime(cTime);

						int times = 0;
						try {
							TimeUtil tunit = new TimeUtil();
							times = tunit.timetoint(thsc);
						} catch (Exception e) {
							logger.error("error", e);
						}
						dxDetail.setTradeTime(times);
						dxDetail.setTradeAddr(thdd);
						if (thlx.contains("漫游")) {
							dxDetail.setTradeType("漫游");
						} else {
							dxDetail.setTradeType("本地");
						}
						if (hjfs.contains("主叫")) {
							dxDetail.setCallWay("主叫");
						} else {
							dxDetail.setCallWay("被叫");
						}
						dxDetail.setRecevierPhone(dfhm);
						// dxDetail.setBasePay(new BigDecimal(jbthf));
						// dxDetail.setLongPay(new BigDecimal(ctthf));
						dxDetail.setAllPay(new BigDecimal(je));
						list.add(dxDetail);
					}
				}
				if (!isCountine) {
					break;
				}
			} catch (Exception e) {
				logger.error("error", e);
			}
		}
		return list;
	}

	/**
	 * 查询通话记录
	 */
	public void saveMessage() {

		List<String> ms = DateUtils.getMonths(6, "yyyyMM");
		List<TelcomMessage> list = new ArrayList<TelcomMessage>();
		TelcomMessage bean = new TelcomMessage();
		bean.setPhone(login.getLoginName());
		bean = telcomMessageService.getMaxSentTime(bean.getPhone());
		boolean isCountine = true;
		for (String startDate : ms) {
			try {
				String text = cutil
						.get("http://gs.189.cn/web/json/searchDetailedFee.action?timestamp="
								+ new Date().getTime()
								+ "&productGroup=4:"
								+ login.getLoginName()
								+ "&orderDetailType=8&queryMonth=" + startDate);
				JSONObject json = new JSONObject(text);
				text = json.getString("jsonResult");

				if (text != null && !text.equals("null")) {
					JSONObject json1 = new JSONObject(text);
					JSONArray jr = json1.getJSONArray("trList");
					Date cTime = null;
					for (int i = 0; i < jr.length(); i++) {
						JSONObject jsonobject = jr.getJSONObject(i);

						String type = jsonobject.getString("val1");
						String receivePhone = jsonobject.getString("val2");
						String sendTime = jsonobject.getString("val0");
						String allPay = jsonobject.getString("val3");

						cTime = DateUtils.StringToDate(sendTime,
								"yyyy-MM-dd HH:mm:ss");
						if (bean != null && bean.getSentTime() != null) {
							if (bean.getSentTime().getTime() >= cTime.getTime()) {
								isCountine = false;// 终止循环
								break;
							}
						}
						TelcomMessage mMessage = new TelcomMessage();
						mMessage.setCreateTs(new Date());
						if (type.contains("发送")) {
							type = "发送";
						} else {
							type = "接受";
						}
						mMessage.setBusinessType("发送");
						mMessage.setRecevierPhone(receivePhone);
						mMessage.setPhone(login.getLoginName());
						mMessage.setAllPay(new Double(allPay));
						mMessage.setSentTime(DateUtils.StringToDate(sendTime,
								"yyyy-MM-dd HH:mm:ss"));
						UUID uuid = UUID.randomUUID();
						mMessage.setId(uuid.toString());

						telcomMessageService.save(mMessage);
					}
				}
				if (!isCountine) {
					break;
				}
			} catch (Exception e) {
				logger.error("error", e);
			}
		}
	}

	public BigDecimal getYue() {
		BigDecimal yue = new BigDecimal("0");
		CHeader h = new CHeader(CHeaderUtil.Accept_, "",
				CHeaderUtil.Content_Type__urlencoded, "gs.189.cn", false);
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("checkedProductName",
				"%E6%89%8B%E6%9C%BA:" + login.getLoginName());
		param.put("productInfo", "4:" + login.getLoginName());
		String text = cutil.post(
				"http://gs.189.cn/web/fee/getBalanceFeeInfo.action", h, param);

		if (text != null) {
			if (text.contains("专用余额")) {
				RegexPaserUtil rp = new RegexPaserUtil("专用余额:<span>￥",
						"</span>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String y = rp.getText().replaceAll("\\s*", "");
				try {
					return new BigDecimal(y);
				} catch (Exception e) {
					logger.error("error", e);
				}
			}
		}
		return yue;
	}

	/**
	 * 查询流量
	 */
	/*
	 * public void saveFlow(){
	 * 
	 * List<String> ms = DateUtils.getMonths(6,"yyyyMM"); List<TelcomMessage>
	 * list = new ArrayList<TelcomMessage>(); TelcomMessage bean = new
	 * TelcomMessage(); bean.setPhone(login.getLoginName()); bean =
	 * telcomMessageService.getMaxSentTime(bean.getPhone()); boolean isCountine
	 * = true; for (String startDate : ms) { try { String text =
	 * cutil.get("http://gs.189.cn/web/json/searchDetailedFee.action?timestamp="
	 * +new Date().getTime()+"&productGroup=4:"+login.getLoginName()+
	 * "&orderDetailType=7&queryMonth="+startDate); System.out.println(text);
	 * JSONObject json=new JSONObject(text); text =
	 * json.getString("jsonResult");
	 * 
	 * if(text!=null&&!text.equals("null")){ JSONObject json1=new
	 * JSONObject(text); JSONArray jr = json1.getJSONArray("trList"); Date cTime
	 * = null; for(int i=0;i<jr.length();i++){ JSONObject jsonobject =
	 * jr.getJSONObject(i);
	 * 
	 * String type = jsonobject.getString("val1"); String receivePhone =
	 * jsonobject.getString("val2"); String sendTime =
	 * jsonobject.getString("val0"); String allPay =
	 * jsonobject.getString("val3");
	 * 
	 * 
	 * cTime = DateUtils.StringToDate(sendTime, "yyyy-MM-dd HH:mm:ss");
	 * if(bean!=null&&bean.getSentTime()!=null){
	 * if(bean.getSentTime().getTime()>=cTime.getTime()){ isCountine =
	 * false;//终止循环 break; } } TelcomMessage mMessage = new TelcomMessage();
	 * mMessage.setCreateTs(new Date()); if(type.contains("发送")){ type = "发送";
	 * }else{ type = "接受"; } mMessage.setBusinessType("发送");
	 * mMessage.setRecevierPhone(receivePhone);
	 * mMessage.setPhone(login.getLoginName()); mMessage.setAllPay(new
	 * Double(allPay)); mMessage.setSentTime(DateUtils.StringToDate(sendTime,
	 * "yyyy-MM-dd HH:mm:ss")); UUID uuid = UUID.randomUUID();
	 * mMessage.setId(uuid.toString());
	 * 
	 * telcomMessageService.save(mMessage); } } if(!isCountine){ break; } }
	 * catch (Exception e) { logger.error("error",e); } } }
	 */
	@Override
	public LinkedList<DianXinFlowDetail> gatherFlowList() throws Exception {

		this.flowBarks = getSpiderMonthsMark(true, "yyyyMM", 6, 1);
		System.out.println(flowBarks.size());
		LinkedList<DianXinFlowDetail> list = new LinkedList<DianXinFlowDetail>();
		List<String> ms = DateUtils.getMonths(6, "yyyyMM");
		List<String> texts = new LinkedList<String>();
		int size = ms.size();
		for (int i = size - 1; i >= 0; i--) {
			try {
				String text = cutil
						.get("http://gs.189.cn/web/json/searchDetailedFee.action?timestamp="
								+ new Date().getTime()
								+ "&productGroup=4:"
								+ login.getLoginName()
								+ "&orderDetailType=7&queryMonth=" + ms.get(i));

				this.flowBarks.get(i).setText(text);
				if (i == 0) {
					list.addAll(gatherFlowList_parse(text, true));
				} else {
					list.addAll(gatherFlowList_parse(text, false));
				}
			} catch (Exception e) {
				logger.error("error", e);
			}
		}
		return list;
	}

	public LinkedList<DianXinFlowDetail> gatherFlowList_parse(String jsontext,
			boolean beCur) {
		LinkedList<DianXinFlowDetail> list = new LinkedList<DianXinFlowDetail>();
		if (jsontext != null) {
			JSONObject json;

			try {
				json = new JSONObject(jsontext);

				String text = json.getString("jsonResult");

				if (text.contains("trList")) {
					json = new JSONObject(text);
					JSONArray jr = json.getJSONArray("trList");
					for (int i = 0; i < jr.length(); i++) {
						JSONObject jsonobject = jr.getJSONObject(i);

						String beginTimeStr = jsonobject.getString("val0")
								.trim();
						String v1 = jsonobject.getString("val1").trim();
						String tradeTimeStr = jsonobject.getString("val2")
								.trim();
						String flowStr = jsonobject.getString("val3").trim();
						String feeStr = jsonobject.getString("val4").trim();
						String netTypeStr = jsonobject.getString("val5").trim();
						String locationStr = jsonobject.getString("val6")
								.trim();
						String businessStr = jsonobject.getString("val7")
								.trim();

						DianXinFlowDetail dianxinFlowDetail = new DianXinFlowDetail();
						dianxinFlowDetail.setPhone(login.getLoginName());
						dianxinFlowDetail.setId(UUID.randomUUID().toString());
						dianxinFlowDetail.setBeginTime(DateUtils.StringToDate(
								beginTimeStr, "yyyy-MM-dd HH:mm:ss"));
						if (bigTime != null
								&& bigTime.getTime() >= dianxinFlowDetail
										.getBeginTime().getTime()) {
							continue;
						}
						dianxinFlowDetail.setTradeTime(StringUtil
								.flowTimeFormat(tradeTimeStr));
						dianxinFlowDetail.setBusiness(businessStr);
						dianxinFlowDetail.setFlow(BigDecimal.valueOf(StringUtil
								.flowFormat(flowStr)));
						dianxinFlowDetail.setNetType(netTypeStr);
						dianxinFlowDetail.setFee(BigDecimal.valueOf(Double
								.parseDouble(feeStr)));
						dianxinFlowDetail.setLocation(locationStr);
						if (beCur) {
							dianxinFlowDetail.setIscm(1);
						} else {
							dianxinFlowDetail.setIscm(0);
						}
						list.add(dianxinFlowDetail);

					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				writeLogByFlowList(e);
			}
		}
		return list;
	}

	@Override
	public LinkedList<MonthlyBillMark> gatherFlowBill() {
		// TODO Auto-generated method stub
		int size = this.flowBarks.size();
		for (int i = size - 1; i >= 0; i--) {
			String text = this.flowBarks.get(i).getText();

			try {
				if (text != null && text.contains("totalInfo")) {
					JSONObject json = new JSONObject(text);
					text = json.getString("jsonResult");
					json = new JSONObject(text);
					text = "{" + json.getString("totalInfo") + "}";
					json = new JSONObject(text);
					String timeStr = json.getString("时长");
					String flowStr = json.getString("流量");
					String payStr = json.getString("金额(元)");

					DianXinFlow dianxinFlow = new DianXinFlow();
					dianxinFlow.setPhone(login.getLoginName());
					dianxinFlow.setId(UUID.randomUUID().toString());
					dianxinFlow.setAllTime(BigDecimal.valueOf(StringUtil
							.flowTimeFormat(timeStr)));
					dianxinFlow.setAllFlow(BigDecimal.valueOf(Math
							.round(StringUtil.flowFormat(flowStr))));
					dianxinFlow.setAllPay(BigDecimal.valueOf(Double
							.parseDouble(payStr)));
					dianxinFlow.setQueryMonth(DateUtils.StringToDate(flowBarks
							.get(i).getMonth(), "yyyyMM"));
					
					String dependCycle;
					String month = this.flowBarks.get(i).getMonth();
					if(i == 0){
						dependCycle = month.substring(0, 4) + "." + month.substring(4, 6) + ".01-"  + DateUtils.formatDate(new Date(), "yyyy.MM.dd");
					} else {
						dependCycle = month.substring(0, 4) + "." + month.substring(4, 6) + ".01-"  + month.substring(0, 4) + "." + month.substring(4, 6) + "." + DateUtils.getDaysOfMonth(month);
						
					}
					dianxinFlow.setDependCycle(dependCycle);
					
					
					if (bigTime != null
							&& bigTime.getTime() >= dianxinFlow.getQueryMonth()
									.getTime()) {
						continue;
					}
					
					this.flowBarks.get(i).setObj(dianxinFlow);
				}else{
					DianXinFlow dianxinFlow=new DianXinFlow();
					dianxinFlow.setPhone(login.getLoginName());
					dianxinFlow.setId(UUID.randomUUID().toString());
					dianxinFlow.setQueryMonth(DateUtils.StringToDate(
								flowBarks.get(i).getMonth(), "yyyyMM"));
					dianxinFlow.setAllFlow(new BigDecimal(0));
					dianxinFlow.setAllPay(new BigDecimal(0));
					dianxinFlow.setAllTime(new BigDecimal(0));
					String dependCycle;
					String month = this.flowBarks.get(i).getMonth();
					if(i == 0){
						dependCycle = month.substring(0, 4) + "." + month.substring(4, 6) + ".01-"  + DateUtils.formatDate(new Date(), "yyyy.MM.dd");
					} else {
						dependCycle = month.substring(0, 4) + "." + month.substring(4, 6) + ".01-"  + month.substring(0, 4) + "." + month.substring(4, 6) + "." + DateUtils.getDaysOfMonth(month);
						
					}
					dianxinFlow.setDependCycle(dependCycle);
					
					if (bigTime != null
							&& bigTime.getTime() >= dianxinFlow
									.getQueryMonth().getTime()) {
						continue;
					}
				   
					this.flowBarks.get(i).setObj(dianxinFlow);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				writeLogByFlowBill(e);
			}

		}
		return this.flowBarks;
	}

	/*public static void parseTest() throws IOException, JSONException {
		InputStream in = new FileInputStream("D:/html.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"gbk"));
		String phtml;
		StringBuffer buffer = new StringBuffer();
		while ((phtml = reader.readLine()) != null) {
			buffer.append(phtml);
		}
		String text = buffer.toString();
		JSONObject json = new JSONObject(text);
		text = json.getString("jsonResult");
		json = new JSONObject(text);
		text = "{" + json.getString("totalInfo") + "}";
		json = new JSONObject(text);
		String timeStr = json.getString("时长");
		String flowStr = json.getString("流量");
		String payStr = json.getString("金额(元)");

		DianXinFlow dianxinFlow = new DianXinFlow();
		dianxinFlow.setPhone("110");
		dianxinFlow.setId(UUID.randomUUID().toString());
		dianxinFlow.setAllTime(BigDecimal.valueOf(StringUtil
				.flowTimeFormat(timeStr)));
		dianxinFlow.setAllFlow(BigDecimal.valueOf(Math.round(StringUtil
				.flowFormat(flowStr))));
		dianxinFlow.setAllPay(BigDecimal.valueOf(Double.parseDouble(payStr)));

		// text = json.getString("jsonResult");
		// json = new JSONObject(text);
		// JSONArray jr = json.getJSONArray("thList");
		// for (int i = 0; i < jr.length(); i++) {
		// JSONObject jsonobject = jr.getJSONObject(i);
		//
		// String v0 = jsonobject.getString("val0");
		// String v1 = jsonobject.getString("val1");
		// String v2 = jsonobject.getString("val2");
		// String v3 = jsonobject.getString("val3");
		// String v4 = jsonobject.getString("val4");
		// String v5 = jsonobject.getString("val5");
		// String v6 = jsonobject.getString("val6");
		// String v7 = jsonobject.getString("val7");
		// System.out.println(v0);
		// System.out.println(v1);
		// System.out.println(v2);
		// System.out.println(v3);
		// System.out.println(v4);
		// System.out.println(v5);
		// System.out.println(v6);
		// System.out.println(v7);
		// }
		// jr = json.getJSONArray("trList");
		// for (int i = 0; i < jr.length(); i++) {
		// JSONObject jsonobject = jr.getJSONObject(i);
		//
		// String beginTimeStr = jsonobject.getString("val0").trim();
		// String v1 = jsonobject.getString("val1").trim();
		// String tradeTimeStr = jsonobject.getString("val2").trim();
		// String flowStr = jsonobject.getString("val3").trim();
		// String feeStr = jsonobject.getString("val4").trim();
		// String netTypeStr = jsonobject.getString("val5").trim();
		// String locationStr = jsonobject.getString("val6").trim();
		// String businessStr = jsonobject.getString("val7").trim();
		//
		// DianXinFlowDetail dianxinFlowDetail = new DianXinFlowDetail();
		// dianxinFlowDetail.setBeginTime(DateUtils.StringToDate(beginTimeStr,
		// "yyyy-MM-dd HH:mm:ss"));
		// dianxinFlowDetail.setTradeTime(StringUtil
		// .flowTimeFormat(tradeTimeStr));
		// dianxinFlowDetail.setBusiness(businessStr);
		// dianxinFlowDetail.setFlow(BigDecimal.valueOf(StringUtil
		// .flowFormat(flowStr)));
		// dianxinFlowDetail.setNetType(netTypeStr);
		// dianxinFlowDetail.setFee(BigDecimal.valueOf(Double
		// .parseDouble(feeStr)));
		// }

	}*/

	public static void main(String[] args) throws IOException, JSONException {
		// GSDianxin gs = new GSDianxin(new Login("18119381302","199034"),
		// null);
		// gs.index();
		// gs.inputCode(imgurl);
		// gs.login();
		// if(gs.status==1){
		// gs.close();
		// User user = gs.getUser();
		// System.out.println(user);
		// 部分参数父类设置了
		// List<DianXinTel> list = gs.spiderMonthlyBill();//gs.getMonthlyBill();
		// for (int i = 0; i < list.size(); i++) {
		// DianXinTel dianXinTel = list.get(i);
		// System.out.println(dianXinTel.getcTime()+"-----"+i);
		// }
		// List<DianXinDetail> list1 =
		// gs.spiderCallHistory();//gs.getCallHistory()
		// for (int i = 0; i < list1.size(); i++) {
		// DianXinDetail dianXinTel = list1.get(i);
		// System.out.println(dianXinTel.getcTime()+"-----"+i);
		// }
		// }
		//GSDianxin.parseTest();
	}
}
