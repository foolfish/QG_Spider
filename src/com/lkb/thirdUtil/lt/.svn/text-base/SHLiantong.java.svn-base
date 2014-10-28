package com.lkb.thirdUtil.lt;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.UnicomDetail;
import com.lkb.bean.UnicomFlow;
import com.lkb.bean.UnicomFlowBill;
import com.lkb.bean.UnicomMessage;
import com.lkb.bean.UnicomTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.dao.IUnicomDetailDao;
import com.lkb.dao.IUnicomMessageDao;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.serviceImp.UnicomDetailServiceImpl;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

public class SHLiantong extends BaseInfoMobile {
	private static Logger logger = Logger.getLogger(SHLiantong.class);
	private static String catImgUrl = "https://uac.10010.com/portal/Service/CreateImage?t=";
	private static String loginUrl = "https://uac.10010.com/portal/Service/MallLogin";
	private static String vertifyAuthUrl = "https://uac.10010.com/portal/Service/CheckNeedVerify?callback=";
	private static String yanAuthUrl = "https://uac.10010.com/portal/Service/CtaIdyChk?callback=";
	private static String balanceUrl = "http://iservice.10010.com/ehallService/static/querybalance/execute/Query_YHhead.processData/Query_YHhead_Data?_=";
	private static String historyUrl = "http://iservice.10010.com/ehallService/static/historyBiil/execute/YH102010002/QUERY_YH102010002.processData/QueryYH102010002_Data/";
	private static String sendUrl = "https://uac.10010.com/portal/Service/SendMSG?callback=";
	private static String userUrl = "http://iservice.10010.com/ehallService/static/myInfo/execute/YH102010028/myinfo.processData/myinfo_Data?_=";
	private static String phoneDetail = "http://iservice.10010.com/ehallService/static/callDetail/execute/YH102010006/Query_YH102010006.processData/QueryYH102010006_Data/true/1/100000000?_=";
	private static String phoneMessage = "http://iservice.10010.com/ehallService/static/SMSDetail/execute/YH102010007/Query_YH102010007.processData/QueryYH102010006_Data/true/1/100000000?_=";
	private static String phoneFlow = "http://iservice.10010.com/ehallService/static/phoneNetFlow/execute/YH102010014/_QUERY_YH102010014.processData/QueryYH102010014_Data/true/1/100000000?_=";
	private static String hUrlpre = "http://iservice.10010.com/ehallService/static/queryMonth/execute2/YHgetMonths/QUERY_paramSession.processData/QUERY_paramSession_Data/000100030001/";
	private static String hUrlpre1 = "http://iservice.10010.com/ehallService/static/queryMonth/execute2/YHgetMonths/QUERY_paramSession.processData/QUERY_paramSession_Data/000100030002/";
	private static String hUrlpre2 = "http://iservice.10010.com/ehallService/static/queryMonth/execute2/YHgetMonths/QUERY_paramSession.processData/QUERY_paramSession_Data/000100030004/";
	private static String hUrlafter = "/undefined/undefined/undefined?_=";
	private static String cleanUrl = "http://iservice.10010.com/ehallService/static/queryMonth/checkmapExtraParam/0001";
	private static String cleanUrl1 = "http://iservice.10010.com/ehallService/static/queryMonth/checkmapExtraParam/0002";
	private static String cleanUrl2 = "http://iservice.10010.com/ehallService/static/queryMonth/checkmapExtraParam/0004";
	private static String monthsUrl = "http://iservice.10010.com/ehallService/static/queryMonth/execute/YH10201000X/QUERY_YHgetMonths.processData/QueryYHgetMonths_Data/detailBill?_=";

	public SHLiantong(Login login) {
		super(login);
	}

	public SHLiantong(Login login, String currentUser) {
		super(login, ConstantNum.comm_sh_lt, currentUser);
	}

	// public Map<String,Object> index(){
	// init();
	// map.put("url",getAuthcode());
	// return map;
	// }

	public static void main(String[] args) {

	}

	public void init() {
		if (!isInit()) {
			long ctime = System.currentTimeMillis();
			String timeParam = "jQuery" + currentUser + "_" + ctime;
			String url = vertifyAuthUrl + timeParam + "&userName="
					+ login.getLoginName() + "&pwdType=01&_=" + ctime;
			try {
				String returnCode = cutil.get(url);
				String[] s1 = returnCode.split("\\(");
				String[] s2 = s1[1].split("\\)");
				JSONObject json = new JSONObject(s2[0]);
				String resultCode = json.getString("resultCode");
				if (!resultCode.equals("false")) {
					setImgUrl(catImgUrl);
				}
				setInit();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, Object> login() {
		long ctime = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		String timeParam = "jQuery" + currentUser + "_" + ctime;
		sb.append(loginUrl).append("?callback=").append(timeParam);
		sb.append("&redirectURL=http%3A%2F%2Fwww.10010.com&userName=").append(
				login.getLoginName());
		sb.append("&password=").append(login.getPassword().trim())
				.append("&pwdType=01");
		sb.append("&productType=01&");
		if (login.getAuthcode() != null && !"".equals(login.getAuthcode())) {
			String uacverifykey = "";
			try {
				uacverifykey = getJsessionId("uacverifykey", "uac.10010.com");
			} catch (Exception e) {
			}

			sb.append("verifyCode=" + login.getAuthcode() + "&uvc="
					+ uacverifykey + "&redirectType=01&rememberMe=1&_=" + ctime);
		} else {
			sb.append("rememberMe=1&_=" + ctime);
		}
		try {
			CHeader h = new CHeader(loginUrl);
			String content = cutil.get(sb.toString(), h);
			String[] s11 = content.split("\\(");
			String[] s21 = s11[1].split("\\)");
			JSONObject json2 = new JSONObject(s21[0]);
			String resultCode2 = json2.getString("resultCode");
			if (json2.has("msg")) {
				String error = json2.getString("msg");
				if (error.contains("登录密码出错已达上限")) {
					errorMsg = "登录密码出错已达上限,找回登录密码后登录，或3小时后重试。";
				} else if (error.contains("还有1次机会")) {
					errorMsg = "用户名或密码不正确，还有1次机会。";
				} else if (error.contains("还有2次机会")) {
					errorMsg = "用户名或密码不正确，还有2次机会。";
				} else if (error.contains("还有1次机会")) {
					errorMsg = "用户名或密码不正确，还有1次机会。";
				} else if (error.contains("用户名或密码不正确")) {
					errorMsg = "用户名或密码不正确!";
				} else {
					errorMsg = error;
				}
				// 用户名或密码不正确
				// 用户名或密码不正确，还有3次机会
				// 用户名或密码不正确，还有2次机会。
				// 用户名或密码不正确，还有1次机会
			}
			// System.out.println(resultCode2);
			if (resultCode2.equals("0000")) {
				loginsuccess();
				addTask(this);
			} else {
				String needvode = json2.getString("needvode");
				if (needvode.equals("0")) {
					setImgUrl(catImgUrl);
				}
			}
		} catch (Exception e) {
			writeLogByLogin(e);
		}
		return map;
	}

	/** 一个对象不能创建两个任务切记*,除非单独创建一个对象 */
	@Override
	public void startSpider() {
		int type = login.getType();
		try {
			parseBegin(Constant.LIANTONG);
			switch (type) {
			case 1:
				saveUserInfoByHtmlparser();
				saveUserOrderByHtmlparser();
				break;
			case 2:
				parsePhoneDetail();
				break;
			case 3:
				saveUserInfoByHtmlparser();
				saveUserOrderByHtmlparser();
				parsePhoneDetail();// 短信和通话详单一起存了
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.LIANTONG);
		}
	}

	// /*
	// * 开始抓取会员信息
	// */
	// public void parseBegin() {
	// saveUserInfoByHtmlparser();
	// saveUserOrderByHtmlparser();
	// parsePhoneDetail();
	// }

	private void parsePhoneDetail() {
		List<String> list = getMonths();
		UnicomDetail uni1 = new UnicomDetail(login.getLoginName());
		UnicomDetail max_uni1 = unicomDetailService.getMaxTime(uni1);
		UnicomMessage max_uni2 = null;
		max_uni2 = unicomMessageService.getMaxSentTime(login.getLoginName());
		UnicomFlowBill uni3 = new UnicomFlowBill();
		uni3.setTeleno(login.getLoginName());
		UnicomFlowBill max_uni3 = unicomFlowBillService.getMaxTime(uni3);
		UnicomFlow max_uni4 = unicomFlowService.getMaxStartTime(login
				.getLoginName());
		for (int i = list.size() - 1; i >= 0; i--) {
			String month = list.get(i);
			parsePhoneDetail(month, max_uni1);
			// 添加短信爬取功能
			parsePhoneMessage(month, max_uni2);
			// 添加流量详单爬取和流量月账单爬取（一起存）
			parsePhoneFlow(month, max_uni3, max_uni4);
		}

	}

	private List<String> getMonths() {

		String content = cutil.post(monthsUrl, null);
		// System.out.println(content);
		String[] ss1 = null;
		if (content.contains("data=")) {
			ss1 = content.split("data=");
		} else if (content.contains("Data=")) {
			ss1 = content.split("Data=");
		}
		int index = ss1[1].indexOf("}};");
		content = ss1[1].substring(0, index + 3);
		JSONObject json;
		List<String> list = new ArrayList<String>();
		try {
			json = new JSONObject(content);
			JSONObject json2 = new JSONObject(json.get("rspPublicArgs")
					.toString());
			JSONObject rspArgs = new JSONObject(json2.get("rspArgs").toString());
			String dateMonths_zh = rspArgs.get("dateMonths_zh").toString()
					.replace("]", "").replace("\"", "").replace("\\u005B", "")
					.replace("[", "");
			String[] dateMonths = dateMonths_zh.split(",");
			for (int i = 0; i < dateMonths.length; i++) {
				String date = dateMonths[i];
				if (date != null && date.trim().length() > 0) {
					date = date.replace("年", "").replace("月", "");
					list.add(date);
				}
			}
		} catch (Exception e) {
			logger.info(e.getStackTrace());
			e.printStackTrace();
		}
		return list;
	}

	private void saveUserOrderByHtmlparser() {
		// http://iservice.10010.com/ehallService/static/myInfo/execute/YH102010028/QUERY_YH102010028.processData/QueryYH102010028_Data?_=1410534937837
		// Referer: http://iservice.10010.com/index.html
		// _ 1410534937837
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			for (int i = 1; i < ms.size(); i++) {
				String date = ms.get(i);
				String url = historyUrl + date + "/undefined?_="
						+ System.currentTimeMillis() + "&menuid=000100020001";
				String content = cutil.post(url, null);
				// System.out.println(content);
				if (content == null || content.equals("")) {
					continue;
				}
				String[] ss1 = null;
				if (content.contains("data=")) {
					ss1 = content.split("data=");
				} else if (content.contains("Data=")) {
					ss1 = content.split("Data=");
				}
				int index = ss1[1].indexOf("}};");
				content = ss1[1].substring(0, index + 3);
				JSONObject json;
				try {
					json = new JSONObject(content);
					JSONObject json2 = new JSONObject(json.get("rspPublicArgs")
							.toString());
					String beginDate = json2.get("beginDate").toString();
					String endDate = json2.get("endDate").toString();
					String rspArgs = json2.get("rspArgs").toString();
					JSONObject json3 = new JSONObject(rspArgs);
					if (!json3.has("columnList")) {
						if (json3.has("exceptionContent")) {
							continue;
						} else {
							return;
						}

					}
					String columnList = json3.get("columnList").toString();
					String dependCycle = beginDate + "-" + endDate;
					columnList = columnList.replace("[", "").replace("]", "");
					String[] attrs = columnList.split("},");
					String sumFee = json3.get("payTotal").toString();
					// 天津,广西地区
					// if
					// (sumFee.equals("")&&(json3.get("proviceName").equals("天津")))
					// {
					if (sumFee.equals("")) {
						sumFee = "0.00";
					}
					BigDecimal cAllPay = new BigDecimal(sumFee);
					if (cAllPay.compareTo(new BigDecimal("0")) == 0) {
						sumFee = json3.get("sumFee").toString();
						String province = json3.get("proviceName").toString();
						// 天津,广西地区
						if (sumFee.equals("")
								&& (province.equals("天津")
										|| province.equals("广西")
										|| province.equals("西藏")
										|| province.equals("湖北")
										|| province.equals("河南")
										|| province.equals("吉林")
										|| province.equals("安徽") || province
											.equals("湖南"))) {
							// if (sumFee.equals("")) {
							sumFee = "0.00";
						}
						if (province.equals("西藏") || province.equals("天津")
								|| province.equals("广西")) {
							JSONArray jsonArray = json3.getJSONArray("list");
							for (int j = 0; j < jsonArray.length(); j++) {
								JSONObject jo = (JSONObject) jsonArray.get(j);
								String name = jo.get("name").toString();
								if (name.trim().contains("合计")
										|| name.trim().equals("小计（C）")) {
									sumFee = jo.get("fee").toString();
								}
							}
						}
						cAllPay = new BigDecimal(sumFee);
					}
					String customName = json3.get("customName").toString();
					String userNumber = json2.get("userNumber").toString();
					DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
					Date cDate = null;
					try {
						cDate = df.parse(beginDate + " 00:00:00");
					} catch (ParseException e) {
						logger.info(e.getStackTrace());
						e.printStackTrace();
					}
					Map map = new HashMap(2);
					// map.put("baseUserId", currentUser);
					map.put("cTime", cDate);
					map.put("teleno", login.getLoginName());
					List<UnicomTel> unicomTelList = unicomTelService
							.getUnicomTelBybc(map);
					if (unicomTelList != null && unicomTelList.size() > 0) {
						UnicomTel mb = unicomTelList.get(0);
						if (mb.getIscm() == 1) {
							UnicomTel ut = getProjectPay(mb, attrs);
							ut.setcTime(cDate);
							ut.setcName(customName);
							ut.setTeleno(userNumber);
							ut.setDependCycle(dependCycle);
							ut.setcAllPay(cAllPay);
							unicomTelService.update(ut);
						}
					} else {
						UnicomTel ut = new UnicomTel();
						ut = getProjectPay(ut, attrs);
						UUID uuid = UUID.randomUUID();
						ut.setId(uuid.toString());
						// ut.setBaseUserId(currentUser);
						ut.setcTime(cDate);
						ut.setcName(customName);
						ut.setTeleno(userNumber);
						ut.setDependCycle(dependCycle);
						ut.setcAllPay(cAllPay);
						unicomTelService.saveUnicomTel(ut);
					}

				} catch (Exception e) {
					logger.info(e.getStackTrace());
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			String warnType = WaringConstaint.LT_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			logger.info("第371行捕获异常：", e);
			e.printStackTrace();
		}

	}

	private UnicomTel getProjectPay(UnicomTel unicomTel, String[] attrs)
			throws JSONException {
		for (int i = 0; i < attrs.length; i++) {
			JSONObject json4;
			// 没有生效月份的月结会是""字符串，new JSONObject(attrs[i] + "}")方法会出现异常
			if (!attrs[i].equals("")) {
				json4 = new JSONObject(attrs[i] + "}");

				// System.out
				// .println(json4.get("name") + "=" + json4.get("value"));
				if (json4.get("name").toString().contains("基本月租")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setJbyzf(bvalue);
				} else if (json4.get("name").toString().contains("本地通话费")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setBdthf(bvalue);
				} else if (json4.get("name").toString().contains(" 长途通话费")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setCtthf(bvalue);
				} else if (json4.get("name").toString().contains("漫游通话费")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setMythf(bvalue);
				} else if (json4.get("name").toString().contains("短信通信费")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setDxtxf(bvalue);
				} else if (json4.get("name").toString().contains(" 增值业务费")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setZzywf(bvalue);
				} else if (json4.get("name").toString().contains(" 代收费(信息费)")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setDsf(bvalue);
				} else if (json4.get("name").toString().contains(" 特服费")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setTff(bvalue);
				} else if (json4.get("name").toString().contains(" 其他费用")) {
					String value = "0.0";
					if (json4.get("value").toString() != null
							&& json4.get("value").toString().trim().length() > 0) {
						value = json4.get("value").toString();
					}
					BigDecimal bvalue = new BigDecimal(value);
					unicomTel.setQtf(bvalue);
				}
			}
		}

		return unicomTel;
	}

	private String getBalance() {
		// 保存余额
		String balance = "0";
		String balanceUrll = balanceUrl + System.currentTimeMillis()
				+ "&menuid=000100010002";
		String balanceContent = cutil.post(balanceUrll, null);
		// System.out.println(balanceContent);
		try {
			if (balanceContent != null && balanceContent.length() > 0
					&& balanceContent.contains("YHhead_Data=")) {
				balanceContent = balanceContent.split("YHhead_Data=")[1];
				balanceContent = balanceContent.split(";")[0];
				// System.out.println(balanceContent);
				JSONObject json = new JSONObject(balanceContent);
				// 上海地区
				String subjson = json.toString().substring(2, 5);
				// System.out.println(subjson);
				if (subjson.equals("yue")) {
					json = json.getJSONObject("yue");
					JSONObject json2 = new JSONObject(json.get("rspPublicArgs")
							.toString());
					JSONObject json3 = new JSONObject(json2.get("rspArgs")
							.toString());
					String util_string = json3.get(
							"acctBalanceNewYFFBalanceInfo").toString();
					String sub_string = util_string.substring(1,
							util_string.length());
					JSONObject jsonObject4 = new JSONObject(sub_string);
					balance = jsonObject4.get("balance").toString();
				} else {
					JSONObject json2 = new JSONObject(json.get("rspPublicArgs")
							.toString());
					JSONObject json3 = new JSONObject(json2.get("rspArgs")
							.toString());
					String provicen = new JSONObject(json3.get("userInfoBean")
							.toString()).get("province_name").toString();
					// System.out.println(json3.toString());
					String exceptionBean = json3.get("exceptionBean")
							.toString();
					if (exceptionBean.equals("null")
							|| exceptionBean.equals("")) {
						// 工资参数不同省份 所在的名称不同主要是amount和balance两种，上海特别需要特殊处理。
						if (provicen.equals("黑龙江") || provicen.equals("辽宁")
								|| provicen.endsWith("河北")
								|| provicen.equals("河南")
								|| provicen.equals("北京")
								|| provicen.equals("山东")) {
							balance = json3.get("amount").toString();
						}
						// 天津
						/*
						 * if
						 * (provicen.equals("天津")||provicen.equals("重庆")||provicen
						 * .
						 * equals("湖北")||provicen.endsWith("陕西")||provicen.equals
						 * ("山西")
						 * ||provicen.equals("新疆")||provicen.equals("吉林")||
						 * provicen
						 * .equals("内蒙古")||provicen.equals("安徽")||provicen
						 * .equals("云南")
						 * ||provicen.equals("湖南")||provicen.equals
						 * ("广西")||provicen
						 * .equals("贵州")||provicen.equals("西藏")||
						 * provicen.equals("海南")
						 * ||provicen.equals("甘肃")||provicen
						 * .equals("宁夏")||provicen
						 * .equals("青海")||provicen.equals(
						 * "浙江")||provicen.equals("江苏")
						 * ||provicen.equals("江西")||provicen.equals("福建")) {
						 */
						else {
							JSONObject json4 = new JSONObject(json3.get(
									"balanceReportResult").toString());
							balance = json4.get("balance").toString();
						}
					} else {
						return "";
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balance;
	}

	private void saveUserInfoByHtmlparser() {
		Date modifyDate = new Date();
		Map<String, String> map = new HashMap<String, String>(3);
		map.put("parentId", currentUser);
		map.put("loginName", login.getLoginName());
		map.put("usersource", Constant.LIANTONG);
		List<User> list = userService.getUserByParentIdSource(map);
		User base_user = new User();
		try {
			String content = cutil.post(userUrl + System.currentTimeMillis(),
					null);
			// System.out.println(content);
			if (content != null && !content.equals("")) {
				// System.out.println(content);
				String s1 = "";
				try {
					if (content.contains("data=")) {
						s1 = "data=";
					} else if (content.contains("Data=")) {
						s1 = "Data=";
					}
					String[] ss1 = content.split(s1);
					int index = ss1[1].indexOf("}};");
					content = ss1[1].substring(0, index + 3);
					// {"rspPublicArgs":{"payType":null,"endDate":null,"netType":"02","beginDate":null,"provinceCode":null,"cityCode":null,"userNumber":"18502884929","bizInfo":["081_11022000001"],"rspCode":"00","rspDesc":"OK","queryDate":null,"templateId":null,"queryBeginEndDate":null,"rspArgs":{"packageName":"WCDMA(3G)-66元基本套餐A","exceptionMsg":null,"payType":"2","userMarked":"090","userMobile":"18502884929","mobileBean":{"usernumber":null,"brand":"沃","paytype":"多种付费类型","citycode":null,"productid":null,"custid":null,"acctid":null,"productname":null,"landlvl":"本地通话","custname":"胡文文","custlvl":"银卡会员","certnum":"5101**********0022","managername":"罗毓","managercontact":"18602821277","subscrbstat":"正常服务","opendate":"2013年02月03日","laststatdate":null,"certtype":"身份证","sendname":"胡文文","sendpost":"100000","sendemail":"","sendaddr":"金牛区土桥金周路999号","subscrbtype":"GSM普通","roamstat":"国内漫游","certaddr":null,"custsex":null,"subscrbid":null,"simcard":null,"vpnname":null,"creditvale":null,"sendflag":null,"sendcontent":null,"rspcode":null,"rspdesc":null,"rspts":null,"reqsign":null,"rspsign":null,"trxid":null,"resultMap":null,"busiorder":null,"errMessage":null,"exceptionContent":null},"fixBean":null,"lifeBean":null,"pukBean":{"pukcode":"45745455","pincode":"1234","simcard":null,"puk2code":null,"pin2code":null,"rspcode":null,"rspdesc":null,"rspts":null,"reqsign":null,"rspsign":null,"trxid":null,"resultMap":null,"busiorder":null,"errMessage":null,"exceptionContent":null},"checkFamilyNumber":false}}};
					// 客户名称 胡文文 用户级别 银卡会员
					// 证件类型 身份证 证件号码 5101**********0022
					// 客户经理 罗毓 联系方式 18602821277
					// 账单户名 胡文文 付费方式 多种付费类型
					// 寄送邮编 100000 E-mail --
					// 寄送地址 金牛区土桥金周路999号
					// 当前状态 正常服务 入网日期 2013年02月03日
					// 号码类型 GSM普通 所属品牌 沃
					// 通话级别 本地通话 漫游级别 国内漫游
					// 当前状态 入网日期
					// 计费类型 所属品牌
					// 通话级别 宽带编码
					// 用户生命周期
					// PIN码 1234 PUK码 45745455

					JSONObject json;
					json = new JSONObject(content);
					JSONObject json2 = new JSONObject(json.get("rspPublicArgs")
							.toString());
					JSONObject json3 = new JSONObject(json2.get("rspArgs")
							.toString());
					String packageName = json3.get("packageName").toString();// 沃3G-慧卡6元套餐+10元（OCS）
					JSONObject json4 = new JSONObject(json3.get("mobileBean")
							.toString());
					String sendname = json4.get("sendname").toString();// 张莉
					String opendate = json4.get("opendate").toString();// 入网日期2013年07月21日
					DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
					String realName = json4.get("custname").toString();// 真实姓名
					String idCard = json4.get("certnum").toString();// 身份证号
					String address = json4.get("sendaddr").toString();// 寄送地址
					String custlvl = json4.get("custlvl").toString();// 用户级别
					String brand = json4.get("brand").toString();// 所属
					String roamstat = json4.get("roamstat").toString();// 漫游级别
					String landlvl = json4.get("landlvl").toString();// 通话级别
					String subscrbtype = json4.get("subscrbtype").toString();// 号码类型
					String subscrbstat = json4.get("subscrbstat").toString();// 当前状态

					Date registerDate = null;
					base_user.setRealName(sendname);
					base_user.setPackageName(packageName);
					base_user.setIdcard(idCard);
					base_user.setRealName(realName);
					base_user.setMemberLevel(custlvl);
					try {
						registerDate = df.parse(opendate);
					} catch (ParseException e) {
						logger.info(e.getStackTrace());
						e.printStackTrace();
					}
					base_user.setRegisterDate(registerDate);
					base_user.setAddr(address);
				} catch (Exception e) {
					logger.info("Liantong user parser:\n"+e.getStackTrace());
					e.printStackTrace();
				}
			}
			UUID uuid = UUID.randomUUID();
			base_user.setId(uuid.toString());
			base_user.setLoginName(login.getLoginName());
			String balanceStr = getBalance();
			if (balanceStr != null && !balanceStr.equals("")) {
				BigDecimal balance = new BigDecimal(balanceStr);
				// System.out.println(balance);
				base_user.setPhoneRemain(balance);
			}
			base_user.setPhone(login.getLoginName());
			base_user.setUserName(login.getLoginName());

			base_user.setModifyDate(modifyDate);
			base_user.setParentId(currentUser);
			base_user.setUsersource(Constant.LIANTONG);

			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				/*
				 * if (login.getPassword() != null &&
				 * login.getPassword().trim().length() > 0) {
				 * user.setLoginPassword(login.getPassword()); }
				 */
				user.setUserName(login.getLoginName());
				user.setPhoneRemain(base_user.getPhoneRemain());
				user.setParentId(currentUser);
				user.setModifyDate(modifyDate);
				userService.update(user);
			} else {
				try {

					userService.saveUser(base_user);
				} catch (Exception e) {
					logger.error("抓取用户信息出错！", e);

				}
			}
		} catch (Exception e) {
			String warnType = WaringConstaint.LT_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			logger.error("第562行捕获异常：", e);
		}
	}

	/*
	 * yanAuthUrl 验证验证码是否输入正确
	 */
	// public Boolean checkAuthcode() {
	// Boolean flag = false;
	// long ctime = System.currentTimeMillis();
	// String timeParam = "jQuery" + currentUser + "_" + ctime;
	// String url2 = yanAuthUrl + "callback=" + timeParam + "&verifyCode="
	// + login.getAuthcode() + "&verifyType=1&_=" + ctime;
	// String returnCode2 = cutil.get(url2, client);
	// String[] s11 = returnCode2.split("\\(");
	// String[] s21 = s11[1].split("\\)");
	// JSONObject json2;
	// try {
	// json2 = new JSONObject(s21[0]);
	// String resultCode2 = json2.getString("resultCode");
	// if (resultCode2.equals("true")) {
	// flag = true;
	// }
	// } catch (JSONException e) {
	// logger.info(e.getStackTrace());
	// e.printStackTrace();
	// }
	// return flag;
	// }

	// /*
	// * 验证是否登陆成功
	// */
	// public Map<String,Object> login(Boolean needAuth) {
	// Map<String,Object> bmap = new HashMap<String,Object>();
	// Boolean flag = false;
	// Boolean isNeedAuth = false;
	// Boolean busyflag = false;
	// Boolean overload = true;
	// long ctime = System.currentTimeMillis();
	// String timeParam = "jQuery" + currentUser + "_" + ctime;
	// String url2 = loginUrl + "?callback=" + timeParam
	// + "&redirectURL=http%3A%2F%2Fwww.10010.com&userName=" +
	// login.getLoginName()
	// + "&password=" + login.getPassword().trim() + "&pwdType=01"
	// + "&productType=01&redirectType=01&rememberMe=1&_=" + ctime;
	// try {
	// if (needAuth == true) {
	// String uacverifykey = getJsessionId("uacverifykey", "uac.10010.com");
	// url2 = loginUrl + "?callback=" + timeParam
	// + "&redirectURL=http%3A%2F%2Fwww.10010.com&userName="
	// + login.getLoginName() + "&password=" + login.getPassword() +
	// "&pwdType=01"
	// + "&productType=01&" + "verifyCode=" + login.getAuthcode() + "&uvc="
	// + uacverifykey + "&redirectType=01&rememberMe=1&_=" + ctime;
	// }
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// return bmap;
	// }
	// CHeader h = new CHeader(loginUrl);
	//
	// String content = cutil.get(url2, h);
	// String[] s11 = content.split("\\(");
	// String[] s21 = s11[1].split("\\)");
	// JSONObject json2;
	// try {
	// json2 = new JSONObject(s21[0]);
	// String resultCode2 = json2.getString("resultCode");
	// if(json2.has("msg")){
	// msg = json2.getString("msg");
	// }else {
	// msg="";
	// }
	// /*if(msg.contains("<a")){
	// msg = msg.substring(0, msg.indexOf("<a"));
	// }*/
	// System.out.println(resultCode2);
	// if (resultCode2.equals("0000")) {
	// flag = true;
	//
	// } else if (resultCode2.equals("7099")) {
	// busyflag = true;
	//
	// }
	// else if (resultCode2.equals("7004")) {
	// overload = true;
	//
	// }else {
	// String needvode = json2.getString("needvode");
	// if (needvode.equals("0")) {
	// isNeedAuth = true;
	// }
	// }
	// } catch (JSONException e) {
	// logger.info(e.getStackTrace());
	// e.printStackTrace();
	// }
	// bmap.put("flag", flag);
	// bmap.put("isbusy", busyflag);
	// bmap.put("isNeedAuth", isNeedAuth);
	// bmap.put("overload", overload);
	// bmap.put("msg", msg);
	// return bmap;
	// }

	private void parsePhoneFlow(String month, UnicomFlowBill max_uni3,
			UnicomFlow max_uni4) {
		try{

			List<UnicomFlow> flowList = new ArrayList<UnicomFlow>();
			try {
				cutil.post(cleanUrl2, null);
				String hUrl = hUrlpre2 + month + hUrlafter
						+ System.currentTimeMillis() + "&menuid=000100030004";
				cutil.post(hUrl, null);
				String content3 = cutil.post(
						phoneFlow + System.currentTimeMillis(), null);
				if (content3 == null || content3.equals("")) {
					return;
				}
				
				List<Document> docs = new ArrayList<Document>();
				Document doc1 = Jsoup.parse(content3);
				
				//流量月账单（取每个月详单的showTop）
				Element doc2=doc1.getElementById("showTop");
				Elements tds=doc2.getElementsByTag("td");
				Date billMonth=(new SimpleDateFormat("yyyyMMdd").parse(month+"01"));
				UnicomFlowBill newBill;
				List<UnicomFlowBill> billList;
				Map<String,Object> map = new HashMap(2);
				map.put("cTime", billMonth);
				map.put("teleNo", login.getLoginName());
				billList=unicomFlowBillService.getUnicomFlowBillBybc(map);
				int isUpdate=0;
				if(billList==null || billList.size()==0)
					newBill=new UnicomFlowBill();
				else{
					newBill=billList.get(0);
					isUpdate=1;
				}
				newBill.setDependCycle(tds.get(2).text());
				newBill.setcTime(billMonth);
				if(tds.size()<6){
					newBill.setcAllFlow(new BigDecimal("0"));
					newBill.setcAllPay(new BigDecimal("0"));
				}
				else{
					String flow=tds.get(4).text().replaceAll("KB", "");
					flow=flow.replaceAll("kb", "");
					newBill.setcAllFlow(new BigDecimal(flow));
					newBill.setcAllPay(new BigDecimal(tds.get(5).text().replaceAll("元", "")));
				}
				newBill.setTeleno(login.getLoginName());
				if(isUpdate==1)
					unicomFlowBillService.update(newBill);
				else{
					newBill.setId(UUID.randomUUID().toString());
					unicomFlowBillService.saveUnicomFlowBill(newBill);
				}
				
				
				docs.add(doc1);
				String totalPage1 = InfoUtil.getInstance().getInfo("lt/sh",
						"totalPage1");
				// String callDetailHead = InfoUtil.getInstance().getInfo("lt/sh",
				// "base2Gtable");
				String callDetailHead = "table[id=phoneNetFlowDetailHead]";
				String th = InfoUtil.getInstance().getInfo("lt/sh", "th");
				String callDetailBody;
				try {
					callDetailBody = InfoUtil.getInstance().getInfo("lt/sh",
							"phoneNetFlowDetailBody");
				} catch (Exception e) {
					callDetailBody = "table[id=phoneNetFlowDetailBody]";
				}
				String tr = InfoUtil.getInstance().getInfo("lt/sh", "tr");
				String td = InfoUtil.getInstance().getInfo("lt/sh", "td");
				if (doc1.select(totalPage1) != null
						&& doc1.select(totalPage1).size() > 0) {
				} else {
					return;
				}
				// String totalPage = doc1.select(totalPage1).get(0).text().trim();
				Elements elementsHead = doc1.select(callDetailHead).first()
						.select(th);

				int isAllFlow = -1;
				int isTradeType = -1;
				int isStartTime = -1;
				int isAllPay = -1;
				int isTradeAddr = -1;
				
				for (int i = 0; i < elementsHead.size(); i++) {
					String content = elementsHead.get(i).text();
					if (content.contains("起始时间")) {
						isStartTime = i;
					} else if (content.contains("网络类型")) {
						isTradeType = i;
					} else if (content.contains("流量")) {
						isAllFlow = i;
					} else if (content.contains("费用")) {
						isAllPay = i;
					} else if (content.contains("地点")){
						isTradeAddr = i;
					}
				}
				java.text.DateFormat format3 = new java.text.SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				// post参数为1000000，翻页目前无意义！
				// int itotalPage = Integer.parseInt(totalPage);
				// List<String> list = new ArrayList<String>();
				// if (itotalPage > 1) {
				// for (int i = 2; i <= itotalPage; i++) {
				// String url =
				// "http://iservice.10010.com/ehallService/static/callDetail/execute/YH102010006/Query_YH102010006.processData/QueryYH102010006_Data/true/"
				// + i + "/100000000?_=" + System.currentTimeMillis();
				// list.add(url);
				// }
				// }
				//
				// for (int j = 0; j < list.size(); j++) {
				// String url = list.get(j);
				// String content = cutil.post(url, null);
				// // System.out.println(content3);
				// Document doc2 = Jsoup.parse(content);
				// docs.add(doc2);
				// }

				// 判断是否是当前这个月

				for (int m = 0; m < docs.size(); m++) {
					Document doc = docs.get(m);
					// System.out.print(m+"---");
					Elements elements = doc.select(callDetailBody).first()
							.select(tr);
					for (int i = 0; i < elements.size(); i++) {
						Element subElement = elements.get(i);
						Elements elements2 = subElement.select(td);
						// System.out.print(i+"--");
						String day = elements2.get(isStartTime).text();

						Date startTime = null;
						try {
							startTime = format3.parse(day);
						} catch (ParseException e) {
							logger.info(e.getStackTrace());
							e.printStackTrace();
						}
						// Map map = new HashMap();
						// map.put("phone", login.getLoginName());
						// map.put("cTime", cTime);

						// 增量式判断
						if (max_uni4 != null
								&& !max_uni4.getStartTime().before(startTime))
							continue;
						// save

						String tradeType = "";
						if (isTradeType != -1) {
							tradeType = elements2.get(isTradeType).text();
						}
						String tradeAddr = "";
						if (isTradeAddr != -1) {
							tradeAddr = elements2.get(isTradeAddr).text();
						}
						String allPay = "";
						if (isAllPay != -1) {
							allPay = elements2.get(isAllPay).text();
						}
						String allFlow="";
						if (isAllFlow != -1) {
							allFlow = elements2.get(isAllFlow).text();
						}
						UnicomFlow unicomFlow = new UnicomFlow();
						UUID uuid = UUID.randomUUID();
						unicomFlow.setId(uuid.toString());
						unicomFlow.settradeAddr(tradeAddr);
						unicomFlow.setStartTime(startTime);
						unicomFlow.setTradeType(tradeType);
						unicomFlow.setAllPay(new BigDecimal(allPay));
						unicomFlow.setAllFlow(new BigDecimal(allFlow));
						unicomFlow.setPhone(login.getLoginName());
						flowList.add(unicomFlow);
						// unicomMessageService.save(unicomMessage);
					}
				}
			} catch (Exception e) {
				logger.info(e);
			}
			unicomFlowService.insertbatch(flowList);
		}catch(Exception e){
			logger.error(e);
		}
	}

	private void parsePhoneMessage(String month, UnicomMessage max_uni) {
		List<UnicomMessage> messageList = new ArrayList<UnicomMessage>();
		try {
			cutil.post(cleanUrl1, null);
			String hUrl = hUrlpre1 + month + hUrlafter
					+ System.currentTimeMillis() + "&menuid=000100030002";
			cutil.post(hUrl, null);
			String content3 = cutil.post(
					phoneMessage + System.currentTimeMillis(), null);
			if (content3 == null || content3.equals("")) {
				return;
			}
			List<Document> docs = new ArrayList<Document>();
			Document doc1 = Jsoup.parse(content3);
			docs.add(doc1);
			String totalPage1 = InfoUtil.getInstance().getInfo("lt/sh",
					"totalPage1");
			// String callDetailHead = InfoUtil.getInstance().getInfo("lt/sh",
			// "base2Gtable");
			String callDetailHead = "table[id=base2Gtable]";
			String th = InfoUtil.getInstance().getInfo("lt/sh", "th");
			String callDetailBody;
			try {
				callDetailBody = InfoUtil.getInstance().getInfo("lt/sh",
						"detai2Gtable");
			} catch (Exception e) {
				callDetailBody = "table[id=detai2Gtable]";
			}
			String tr = InfoUtil.getInstance().getInfo("lt/sh", "tr");
			String td = InfoUtil.getInstance().getInfo("lt/sh", "td");
			if (doc1.select(totalPage1) != null
					&& doc1.select(totalPage1).size() > 0) {
			} else {
				return;
			}
			// String totalPage = doc1.select(totalPage1).get(0).text().trim();
			Elements elementsHead = doc1.select(callDetailHead).first()
					.select(th);

			int isSentTime = -1;
			int isTradeType = -1;
			int isRecevierPhone = -1;
			int isAllPay = -1;

			for (int i = 0; i < elementsHead.size(); i++) {
				String content = elementsHead.get(i).text();
				if (content.contains("使用起始时间") || content.contains("传送时间")) {
					isSentTime = i;
				} else if (content.contains("业务类型") || content.contains("传送方式")) {
					isTradeType = i;
				} else if (content.contains("对方号码")) {
					isRecevierPhone = i;
				} else if (content.contains("费用")) {
					isAllPay = i;
				}
			}
			java.text.DateFormat format3 = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			// post参数为1000000，翻页目前无意义！
			// int itotalPage = Integer.parseInt(totalPage);
			// List<String> list = new ArrayList<String>();
			// if (itotalPage > 1) {
			// for (int i = 2; i <= itotalPage; i++) {
			// String url =
			// "http://iservice.10010.com/ehallService/static/callDetail/execute/YH102010006/Query_YH102010006.processData/QueryYH102010006_Data/true/"
			// + i + "/100000000?_=" + System.currentTimeMillis();
			// list.add(url);
			// }
			// }
			//
			// for (int j = 0; j < list.size(); j++) {
			// String url = list.get(j);
			// String content = cutil.post(url, null);
			// // System.out.println(content3);
			// Document doc2 = Jsoup.parse(content);
			// docs.add(doc2);
			// }

			// 判断是否是当前这个月

			for (int m = 0; m < docs.size(); m++) {
				Document doc = docs.get(m);
				// System.out.print(m+"---");
				Elements elements = doc.select(callDetailBody).first()
						.select(tr);
				for (int i = 0; i < elements.size(); i++) {
					Element subElement = elements.get(i);
					Elements elements2 = subElement.select(td);
					// System.out.print(i+"--");
					String day = elements2.get(isSentTime).text();

					Date sentTime = null;
					try {
						sentTime = format3.parse(day);
					} catch (ParseException e) {
						logger.info(e.getStackTrace());
						e.printStackTrace();
					}
					// Map map = new HashMap();
					// map.put("phone", login.getLoginName());
					// map.put("cTime", cTime);

					// 增量式判断
					if (max_uni != null
							&& !max_uni.getSentTime().before(sentTime))
						continue;
					// save

					String tradeType = "";
					if (isTradeType != -1) {
						tradeType = elements2.get(isTradeType).text();
					}
					String receiverPhone = "";
					if (isRecevierPhone != -1) {
						receiverPhone = elements2.get(isRecevierPhone).text();
					}
					String allPay = "";
					if (isAllPay != -1) {
						allPay = elements2.get(isAllPay).text();
					}
					UnicomMessage unicomMessage = new UnicomMessage();
					UUID uuid = UUID.randomUUID();
					unicomMessage.setId(uuid.toString());
					unicomMessage.setRecevierPhone(receiverPhone);
					unicomMessage.setSentTime(sentTime);
					if (tradeType.contains("接收") || tradeType.contains("接受"))
						unicomMessage.setTradeType("接收");
					else
						unicomMessage.setTradeType("发送");
					unicomMessage.setAllPay(new BigDecimal(Math.abs(Double
							.parseDouble(allPay))));
					unicomMessage.setPhone(login.getLoginName());
					messageList.add(unicomMessage);
					// unicomMessageService.save(unicomMessage);
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
		unicomMessageService.insertbatch(messageList);
	}

	private void parsePhoneDetail(String month, UnicomDetail max_uni) {
		List<UnicomDetail> detailList = new ArrayList<UnicomDetail>();
		try {
			cutil.post(cleanUrl, null);
			Thread.sleep(300);
			String hUrl = hUrlpre + month + hUrlafter
					+ System.currentTimeMillis() + "&menuid=000100030001";
			cutil.post(hUrl, null);
			String content3 = cutil.post(
					phoneDetail + System.currentTimeMillis(), null);
			if (content3 == null || content3.equals("")) {
				return;
			}
			List<Document> docs = new ArrayList<Document>();
			Document doc1 = Jsoup.parse(content3);
			docs.add(doc1);
			String totalPage1 = InfoUtil.getInstance().getInfo("lt/sh",
					"totalPage1");
			String callDetailHead = InfoUtil.getInstance().getInfo("lt/sh",
					"callDetailHead");
			String th = InfoUtil.getInstance().getInfo("lt/sh", "th");
			String callDetailBody = InfoUtil.getInstance().getInfo("lt/sh",
					"callDetailBody");
			String tr = InfoUtil.getInstance().getInfo("lt/sh", "tr");
			String td = InfoUtil.getInstance().getInfo("lt/sh", "td");
			if (doc1.select(totalPage1) != null
					&& doc1.select(totalPage1).size() > 0) {
			} else {
				return;
			}
			// String totalPage = doc1.select(totalPage1).get(0).text().trim();
			Elements elementsHead = doc1.select(callDetailHead).first()
					.select(th);
			int icTime = -1;
			int ibusinessType = -1;
			int itradeTime = -1;
			int icallType = -1;
			int irecevierPhone = -1;
			int itradeAddr = -1;
			int itradeType = -1;
			int ibasePay = -1;
			int ildPay = -1;
			int iotherPay = -1;
			int itotalPay = -1;
			int ireductionPay = -1;
			for (int i = 0; i < elementsHead.size(); i++) {
				String content = elementsHead.get(i).text();
				if (content.contains("起始时间")) {
					icTime = i;
				} else if (content.contains("业务类型")) {
					ibusinessType = i;
				} else if (content.contains("通话时长")) {
					itradeTime = i;
				} else if (content.contains("呼叫类型")) {
					icallType = i;
				} else if (content.contains("对方号码")) {
					irecevierPhone = i;
				} else if (content.contains("通话地点")) {
					itradeAddr = i;
				} else if (content.contains("通话类型")) {
					itradeType = i;
				} else if (content.contains("基本或漫游")) {
					ibasePay = i;
				} else if (content.contains("长途")) {
					ildPay = i;
				} else if (content.contains("其他费")) {
					iotherPay = i;
				} else if (content.contains("优惠减免")) {
					ireductionPay = i;
				} else if (content.contains("小计")) {
					itotalPay = i;
				}
			}
			// int itotalPage = Integer.parseInt(totalPage);
			List<String> list = new ArrayList<String>();
			// if (itotalPage > 1) {
			// for (int i = 2; i <= itotalPage; i++) {
			// String url =
			// "http://iservice.10010.com/ehallService/static/callDetail/execute/YH102010006/Query_YH102010006.processData/QueryYH102010006_Data/true/"
			// + i + "/100?_=" + System.currentTimeMillis();
			// list.add(url);
			// }
			// }

			java.text.DateFormat format3 = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			for (int j = 0; j < list.size(); j++) {
				String url = list.get(j);
				String content = cutil.post(url, null);
				// System.out.println(content3);
				Document doc2 = Jsoup.parse(content);
				docs.add(doc2);
			}

			// 判断是否是当前这个月
			int iscm = 0;
			java.text.DateFormat format2 = new java.text.SimpleDateFormat(
					"yyyyMM");
			Date nowDate = new Date();
			String firstDate2 = format2.format(nowDate);
			if (firstDate2.equals(month)) {
				iscm = 1;
			}

			for (int m = 0; m < docs.size(); m++) {
				Document doc = docs.get(m);
				// System.out.print(m+"---");
				Elements elements = doc.select(callDetailBody).first()
						.select(tr);
				for (int i = 0; i < elements.size(); i++) {
					Element subElement = elements.get(i);
					Elements elements2 = subElement.select(td);
					// System.out.print(i+"--");
					String day = elements2.get(icTime).text();

					Date cTime = null;
					try {
						cTime = format3.parse(day);
					} catch (ParseException e) {
						logger.info(e.getStackTrace());
						e.printStackTrace();
					}
					Map map = new HashMap();
					map.put("phone", login.getLoginName());
					map.put("cTime", cTime);

					// 增量式判断

					if (max_uni != null && !max_uni.getcTime().before(cTime))
						continue;

					List<UnicomDetail> unicomDetailList = (List) unicomDetailService
							.getUnicomDetailBypt(map);
					if (unicomDetailList != null && unicomDetailList.size() > 0) {
						UnicomDetail unicomDetail = unicomDetailList.get(0);
						if (unicomDetail.getIscm() == 1) {
							// update iscm=0
							unicomDetail.setIscm(iscm);
							unicomDetailService.update(unicomDetail);

						} else {
							break;
						}
					} else {
						// save

						String businessType = "";
						if (ibusinessType != -1) {
							businessType = elements2.get(ibusinessType).text();
						}
						String tradeTime = "";
						if (itradeTime != -1) {
							tradeTime = elements2.get(itradeTime).text();
						}
						String callType = "";
						if (icallType != -1) {
							callType = elements2.get(icallType).text();
						}
						String recevierPhone = "";
						if (irecevierPhone != -1) {
							recevierPhone = elements2.get(irecevierPhone)
									.text();
						}
						String tradeAddr = "";
						if (itradeAddr != -1) {
							tradeAddr = elements2.get(itradeAddr).text();
						}
						String tradeType = "";
						if (itradeType != -1) {
							tradeType = elements2.get(itradeType).text();
						}
						BigDecimal basePay = new BigDecimal("0");
						if (ibasePay != -1) {
							if (!elements2.get(ibasePay).text().equals("")) {
								basePay = new BigDecimal(elements2
										.get(ibasePay).text());
							}
						}
						BigDecimal ldPay = new BigDecimal("0");
						if (ildPay != -1) {
							if (!elements2.get(ildPay).text().equals("")) {
								ldPay = new BigDecimal(elements2.get(ildPay)
										.text());
							}
						}
						BigDecimal otherPay = new BigDecimal("0");
						if (iotherPay != -1) {
							if (!elements2.get(iotherPay).text().equals("")) {
								otherPay = new BigDecimal(elements2.get(
										iotherPay).text());
							}
						}
						BigDecimal totalPay = new BigDecimal("0");
						if (itotalPay != -1) {
							if (!elements2.get(itotalPay).text().equals("")) {
								totalPay = new BigDecimal(elements2.get(
										itotalPay).text());
							}
						}
						BigDecimal reductionPay = new BigDecimal("0");
						if (ireductionPay != -1) {
							if (!elements2.get(ireductionPay).text().equals("")) {
								reductionPay = new BigDecimal(elements2.get(
										ireductionPay).text());
							}
						}
						int times = 0;
						try {
							TimeUtil tunit = new TimeUtil();
							times = tunit.timetoint(tradeTime);
						} catch (Exception e) {

						}
						UnicomDetail unicomDetail = new UnicomDetail();
						UUID uuid = UUID.randomUUID();
						unicomDetail.setId(uuid.toString());
						unicomDetail.setBusinessType(businessType);
						unicomDetail.setTradeTime(times);
						unicomDetail.setCallType(callType);
						unicomDetail.setRecevierPhone(recevierPhone);
						unicomDetail.setTradeAddr(tradeAddr);
						unicomDetail.setcTime(cTime);
						unicomDetail.setTradeType(tradeType);
						unicomDetail.setBasePay(basePay);
						unicomDetail.setIscm(iscm);
						unicomDetail.setLdPay(ldPay);
						unicomDetail.setOtherPay(otherPay);
						unicomDetail.setTotalPay(totalPay);
						unicomDetail.setPhone(login.getLoginName());
						unicomDetail.setReductionPay(reductionPay);
						detailList.add(unicomDetail);
						// unicomDetailService.saveUnicomDetail(unicomDetail);
					}
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
		unicomDetailService.insertbatch(detailList);
	}

	/*
	 * 发送动态验证码
	 */
	public String sendCard() {
		long ctime = System.currentTimeMillis();
		String redirectLocation = sendUrl + "jQuery" + currentUser + "_"
				+ ctime + "&mobile=" + login.getLoginName() + "&_=" + ctime;
		return cutil.get(redirectLocation);
	}

	// /*
	// * 检查是否需要验证码
	// */
	// public Boolean checkNeedAuthcode(String pwdType) {
	// Boolean flag = true;
	// long ctime = System.currentTimeMillis();
	// String timeParam = "jQuery" + currentUser + "_" + ctime;
	// String url = vertifyAuthUrl + timeParam + "&userName=" +
	// login.getLoginName()
	// + "&pwdType=01&_=" + ctime;
	// String returnCode = cutil.get(url, client);
	// if (returnCode == null) {
	// flag=false;
	// }
	// else{
	// String[] s1 = returnCode.split("\\(");
	// String[] s2 = s1[1].split("\\)");
	// JSONObject json;
	// try {
	// json = new JSONObject(s2[0]);
	// String resultCode = json.getString("resultCode");
	// if (resultCode.equals("false")) {
	// flag = false;
	// }
	// } catch (JSONException e) {
	// logger.info(e.getStackTrace());
	// e.printStackTrace();
	// }
	// }
	// //redismap.put("img_url", "none");
	// return flag;
	//
	// }

	// /*
	// * 返回验证码图片
	// */
	// public String getAuthcodeImg() {
	// setImgUrl(catImgUrl+currentUser);
	// String imgUrl = getAuthcode();
	// //imgUrl = imgUrl.substring(13,imgUrl.length());
	// return imgUrl;
	// }
	// public List<Map> getLTBill(String phone,
	// IUnicomTelService unicomTelService,
	// IPhoneNumService phoneNumService,
	// IUnicomDetailService unicomDetailService,IUserService userService) {
	// Map map = new HashMap();
	// map.put("teleno", phone);
	// List<Map> list = unicomTelService.getUnicomTelForReport1(map);
	// if (list.get(0) != null) {
	// String avg = list.get(0).get("avg").toString();
	// avg = avg.substring(0, avg.indexOf("."));
	// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	// String earlest = list.get(0).get("earlest").toString()
	// .substring(0, 10);
	// Date date = null;
	// try {
	// date = df.parse(earlest);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// DateUtils du = new DateUtils();
	// int days = 0;
	// try {
	// days = du.dayDist(earlest);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// Map userMap = new HashMap();
	// userMap.put("loginName", phone);
	// userMap.put("usersource", Constant.LIANTONG);
	// List<User> users = userService.getUserByUserNamesource(userMap);
	// BigDecimal cAllBalance = new BigDecimal("0");
	// if(users!=null && users.size()>0){
	// User user = users.get(0);
	// cAllBalance = user.getPhoneRemain();
	// }
	//
	// Map map1 = new HashMap();
	// map1.put("avg", avg);
	// map1.put("days", days);
	// map1.put("teleno", phone);
	// map1.put("cAllBalance", cAllBalance);
	// String phoneId = phone.substring(0, 7);
	// PhoneNum phoneNum = phoneNumService.findById(phoneId);
	// map1.put("local", phoneNum.getProvince());
	// List<Map> list1 = unicomDetailService
	// .getUnicomDetailForReport2(map);
	// String lateststr = list1.get(0).get("latest").toString();
	// lateststr = lateststr.replaceAll("-", ".").substring(0, 10);
	// map1.put("latest", lateststr);
	// list.clear();
	// list.add(map1);
	// } else {
	// list.clear();
	// }
	// return list;
	// }
	/*
	 * public Map getParam(Map<String, Object> redismap){ DefaultHttpClient
	 * httpclient = RedisClient.getClient(redismap); String lt1 =
	 * InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin, "lt");
	 * String _eventId =
	 * InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
	 * "_eventId"); String submit1 =
	 * InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin, "submit");
	 * String captcha =
	 * InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin, "captcha");
	 * String text = getText(httpclient, loginUrl); Document doc =
	 * Jsoup.parse(text); String lt =doc.select(lt1).attr("value"); String
	 * eventId = doc.select(_eventId).attr("value"); String submit =
	 * doc.select(submit1).attr("value"); Map<String,Object> map=new
	 * HashMap<String,Object>(); map.put("lt", lt); map.put("eventId", eventId);
	 * map.put("submit", submit); Boolean isAuth = false;
	 * 
	 * if( doc.select(captcha)!=null && doc.select(captcha).size()>0){ isAuth =
	 * true; } map.put("isAuth", isAuth);
	 * 
	 * return map; }
	 */
	/*
	 * 得到网页的内容
	 */
	// private String getText(DefaultHttpClient httpclient,
	// String redirectLocation) {
	// CHeader cHeader = new CHeader();
	// cHeader.setAccept("text/html, application/xhtml+xml, */*");
	// cHeader.setHost("uac.10010.com");
	// cHeader.setContent_Type("application/x-www-form-urlencoded");
	// String responseBody = "";
	// HttpResponse response = CUtil.getHttpGet(redirectLocation, cHeader,
	// httpclient);
	// if(response!=null){
	// responseBody = ParseResponse.parse(response,"utf-8");
	// }
	//
	// return responseBody;
	// }
	/*
	 * public String history(DefaultHttpClient httpclient, String url, Map map)
	 * { HttpPost httpost = new HttpPost(url); String returnString = "";
	 * List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>(); if
	 * (map != null) { Iterator it = map.keySet().iterator(); while
	 * (it.hasNext()) { String key = it.next().toString(); String value =
	 * map.get(key).toString(); nvps.add(new BasicNameValuePair(key, value)); }
	 * } try { httpost.setEntity(new UrlEncodedFormEntity( (List<? extends
	 * org.apache.http.NameValuePair>) nvps, HTTP.UTF_8)); HttpResponse response
	 * = httpclient.execute(httpost); if
	 * (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
	 * String result = EntityUtils.toString(response.getEntity()); // 得到返回的字符串
	 * returnString = result; // 打印输出 }
	 * 
	 * } catch (Exception e) { logger.info(e.getStackTrace());
	 * e.printStackTrace();
	 * 
	 * } finally { httpost.abort(); } return returnString; }
	 */

	// 报告获取联系人信息
	public List<Map> getLTContacts(String phone,
			IUnicomDetailService unicomDetailService) {
		Map<String, String> map = new HashMap();
		map.put("phone", phone);
		List<Map> list1 = new ArrayList<Map>();
		List list = unicomDetailService.getUnicomDetailForReport(map);
		for (int i = 0; i < list.size(); i++) {
			Map maptemp = new HashMap();
			Map<Object, Object> map1 = (Map<Object, Object>) list.get(i);
			String phone1 = map1.get("recevierPhone").toString();
			if (i + 1 < list.size()) {
				Map<Object, Object> map2 = (Map<Object, Object>) list
						.get(i + 1);
				String phone2 = map2.get("recevierPhone").toString();
				if (phone1.equals(phone2)) {
					maptemp.put("phone", phone1);
					maptemp.put("place", map1.get("tradeAddr"));
					String type1 = map1.get("callWay").toString();
					String type2 = map2.get("callWay").toString();
					String zhujiao = "";
					String beijiao = "";
					if ("主叫".equals(type1)) {
						zhujiao = map1.get("num").toString();
						beijiao = map2.get("num").toString();
					} else {
						zhujiao = map2.get("num").toString();
						beijiao = map1.get("num").toString();
					}
					maptemp.put("zhujiao", zhujiao);
					maptemp.put("beijiao", beijiao);
					Integer totalint = Integer.parseInt(zhujiao)
							+ Integer.parseInt(beijiao);
					maptemp.put("total", totalint.toString());
					String Times1 = map1.get("tradetimes").toString();
					String Times2 = map2.get("tradetimes").toString();
					if (Times1.contains(".")) {
						Times1 = Times1.replace(".0", "");
					}
					if (Times2.contains(".")) {
						Times2 = Times1.replace(".0", "");
					}
					Integer totalTimesint = Integer.parseInt(Times1)
							+ Integer.parseInt(Times2);
					maptemp.put("totaltimes", totalTimesint.toString());
					list1.add(maptemp);
					i++;
				} else {
					maptemp.put("phone", phone1);
					maptemp.put("place", map1.get("tradeAddr").toString());
					String type1 = map1.get("callWay").toString();

					String zhujiao = "";
					String beijiao = "";
					if ("主叫".equals(type1)) {

						zhujiao = map1.get("num").toString();
						beijiao = "0";

					} else {

						beijiao = map1.get("num").toString();
						zhujiao = "0";
					}
					maptemp.put("zhujiao", zhujiao);
					maptemp.put("beijiao", beijiao);
					maptemp.put("total", map1.get("num"));

					maptemp.put("totaltimes", map1.get("tradetimes").toString());
					list1.add(maptemp);
				}
			} else {
				maptemp.put("phone", phone1);
				maptemp.put("place", map1.get("tradeAddr").toString());
				String type1 = map1.get("callWay").toString();

				String zhujiao = "";
				String beijiao = "";
				if ("主叫".equals(type1)) {

					zhujiao = map1.get("num").toString();
					beijiao = "0";

				} else {

					beijiao = map1.get("num").toString();
					zhujiao = "0";
				}
				maptemp.put("zhujiao", zhujiao);
				maptemp.put("beijiao", beijiao);
				maptemp.put("total", map1.get("num"));
				maptemp.put("totaltimes", map1.get("tradetimes").toString());
				list1.add(maptemp);
			}

		}
		return list1;
	}
}
