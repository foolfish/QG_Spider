package com.lkb.thirdUtil.yd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.test.jsdx.RegexPaserUtil;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.entity.SpeakBillPojo;

/**
 * @author fastw
 * @date 2014-8-26 下午9:30:26
 */
public class HEBYidong extends BaseInfoMobile {

	// 登陆验证码
	public static String imgurl = "https://he.ac.10086.cn//common/image.jsp";
	// 查询个人信息验证码
	public static String imgurl1 = "http://www.he.10086.cn/my/image.action";

	public String spidKey = "hebeiyidong_spid";
	public String RelayStateKey = "hebeiyidong_RelayState";
	public String yue_key = "hebeiyidong_yue";
	public String tempKey = "hebeiyidong_tempkey";
	public String name_key = "hebeiyidong_name";
	public LinkedList<MonthlyBillMark> flowBarks;
	public  List<String> flowBillHtmls;
	public HEBYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_heb_yidong, currentUser);
		this.userSource = Constant.YIDONG;
	}

	// /**
	// * @return 初始化,并获取验证码
	// */
	// public Map<String,Object> index(){
	// init();
	// map.put("url", getAuthcode());
	// return map;
	// }
	public void init() {
		if (!isInit()) {
			String text = cutil.get("https://he.ac.10086.cn/login");
			try {
				if (text != null) {
					if (text.contains("location.replace('http://www.he.10086.cn/my?SAMLart=")) {
						RegexPaserUtil rp = new RegexPaserUtil(
								"location.replace\\('", "'", text,
								RegexPaserUtil.TEXTEGEXANDNRT);
						text = cutil.get(rp.getText());
						yue();

					} else {
						Document doc = Jsoup.parse(text);
						// //System.out.println(doc);
						String sp = doc.select("input[name=spid]").get(0).val();
						String RelayS = doc.select("input[name=RelayState]")
								.get(0).val();
						redismap.put(spidKey, sp);
						redismap.put(RelayStateKey, RelayS);
						setImgUrl(imgurl);
						setInit();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, Object> login() {
		try {
			String spid = redismap.get(spidKey).toString();
			String RelayState = redismap.get(RelayStateKey).toString();
			Map<String, String> param = new HashMap<String, String>();
			param.put("type", "B");
			param.put("backurl", "/4login/backPage.jsp");
			param.put("spid", spid);
			param.put("RelayState", RelayState);
			param.put("mobileNum", login.getLoginName());
			param.put("nickName", "");
			param.put("email", "");
			param.put("servicePassword", login.getPassword());
			param.put("webPassword", "");
			param.put("emailPwd", "");
			param.put("ssoPwd", "");
			param.put("website_pwd_email", "");
			param.put("smsValidCode", "");
			param.put("validCode", login.getAuthcode());
			String text = cutil.post("https://he.ac.10086.cn/Login", param);
			// code=4001密码错误2003验证码错误
			// https://he.ac.10086.cn/2login/errorPage.jsp?code=2003&displayPic=1
			if (text != null) {
				if (text.length() < 200) {
					if (text.contains("errorPage.jsp?code=2003&displayPic=1")) {
						errorMsg = "验证码错误";
					} else if (text
							.contains("errorPage.jsp?code=4001&authThreshold=")) {
						errorMsg = "您输入的密码不正确";
					} else if (text
							.contains("errorPage.jsp?code=6000&displayPic=")) {
						errorMsg = "系统正在升级请稍后再试!";
					} else {
						errorMsg = "确认您填的信息是否正确!";
					}
				} else {
					login_1(text);
				}
			}
		} catch (Exception e) {
			writeLogByLogin(e);
		}
		// 注: 启动的采集时在login1中
		return map;
	}

	private void login_1(String text) {
		try {
			text = cutil.get("http://www.he.10086.cn/my/");
			// System.out.println(text);
			try {
				Document doc = Jsoup.parse(text);
				String SAMLRequest = doc.select("input[name=SAMLRequest]")
						.get(0).val();
				String RelayState = doc.select("input[name=RelayState]").get(0)
						.val();
				Map<String, String> param = new HashMap<String, String>();
				param.put("SAMLRequest", SAMLRequest);
				param.put("RelayState", RelayState);
				text = cutil.post("https://he.ac.10086.cn/POST", param);

				doc = Jsoup.parse(text);
				String SAMLart = doc.select("input[name=SAMLart]").get(0).val();
				String displayPic = doc.select("input[name=displayPic]").get(0)
						.val();
				RelayState = doc.select("input[name=RelayState]").get(0).val();
				param = new HashMap<String, String>();
				param.put("SAMLart", SAMLart);
				param.put("displayPic", displayPic);
				param.put("RelayState", RelayState);
				text = cutil.post("http://www.he.10086.cn/my/", param);

			} catch (Exception e1) {
			}
			yue();
		} catch (Exception e) {
			writeLogByLogin(e);
		}
	}

	private void yue() {
		// 查询余额
		String text = cutil
				.get("http://www.he.10086.cn/my/account/basicInfoHeb!qryBaseInfo.action");
		if (text != null && text.contains("accountfee\":\"")) {
			JSONObject json = null;
			try {
				json = new JSONObject(text);
				json = new JSONObject(json.getString("baseinfo"));
				redismap.put(yue_key,
						json.getString("accountfee").replace("元", ""));
				loginsuccess();
				sendPhoneDynamicsCode();
				addTask_1(this);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, Object> getSecImgUrl() {
		setImgUrl(imgurl1);// 更换验证码地址二次验证
		map.put("url", getAuthcode());
		return map;
	}

	/**
	 * 生成账单信息
	 * */
	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errorMsg = null;
		int status = 0;
		try {
			String text = null;
			String url = "http://www.he.10086.cn/service/fee/qryDetailBill.action";
			Map<String, String> param = new HashMap<String, String>();
			if (redismap.get(tempKey) == null) {
				text = cutil.get(url);
				if (text.contains("https://he.ac.10086.cn/POST")) {
					Document doc = Jsoup.parse(text);
					String SAMLRequest = doc.select("input[name=SAMLRequest]")
							.get(0).val();
					String RelayState = doc.select("input[name=RelayState]")
							.get(0).val();
					param.put("SAMLRequest", SAMLRequest);
					param.put("RelayState", RelayState);
					text = cutil.post("https://he.ac.10086.cn/POST", param);
					doc = Jsoup.parse(text);
					String SAMLart = doc.select("input[name=SAMLart]").get(0)
							.val();
					String displayPic = doc.select("input[name=displayPic]")
							.get(0).val();
					RelayState = doc.select("input[name=RelayState]").get(0)
							.val();
					param = new HashMap<String, String>();
					param.put("SAMLart", SAMLart);
					param.put("displayPic", displayPic);
					param.put("RelayState", RelayState);
					text = cutil.post(url, param);
					redismap.put(tempKey, "0");
				}
			}
			CHeader c = new CHeader(url);
			text = cutil
					.post("http://www.he.10086.cn/service/fee/fee/qryDetailBill!sendRandomCode.action",
							c, param);
			if (text != null && text.contains("success")) {
				errorMsg = "动态密码已发送,校验号码有效期仅为10分钟 ";
				status = 1;
			} else {
				errorMsg = "发送异常,请重试!";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (errorMsg == null) {
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		return map;
	}

	/**
	 * 手机验证用户信息
	 * */
	public Map<String, Object> sendPhoneUserInfo() {
		try {
			String text = cutil
					.get("http://www.he.10086.cn/my/authorize!sendRandomCode.action?phoneNumber=undefined");
			if (text != null && text.contains("success")) {
				errorMsg = "动态密码已发送,校验号码有效期仅为10分钟 ";
				status = 1;
			} else {
				errorMsg = "发送异常,请重试!";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (errorMsg == null) {
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		return map;
	}

	// 随机短信登录
	public Map<String, Object> checkPhoneDynamicsCode() {
		String path = "http://www.he.10086.cn/service/fee/qryDetailBill!checkSmsCode.action";
		Map<String, String> param = new HashMap<String, String>();
		param.put("smsrandom", login.getPhoneCode());
		String text = cutil.post(path, param);

		if (text != null) {
			// {"result":"smsValidateFail"} 失败
			if (text.contains("{\"desc\":\"\"}")) {
				status = 1;
			} else {
				errorMsg = "验证失败,请确认填写是否正确!";
			}
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		if (status == 1) {
			addTask_2(this);
		}
		return map;
	}

	// 随机短信登录
	public Map<String, Object> checkPhoneUserInfo() {
		String path = "http://www.he.10086.cn/my/authorize!validateRandomCode.action";
		Map<String, String> param = new HashMap<String, String>();
		param.put("smsRandomCode", login.getPhoneCode());
		param.put("validateCode", login.getAuthcode());
		String text = cutil.post(path, param);
		if (text != null) {
			// {"result":"smsValidateFail"} 失败
			if (text.contains("smsValidateSucc")) {
				status = 1;
			} else {
				errorMsg = "验证失败,请确认填写是否正确!";
			}
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		if (status == 1) {
			login.setType(4);
			addTask(this);
		}
		return map;
	}

	/**
	 * 不需要验证口令,不正规
	 */
	public void getMyInfo1() {
		try {
			// 个人信息
			String text = cutil.get("http://www.he.10086.cn/my/account/");// gbk
			// System.out.println(text);
			String name = new RegexPaserUtil("dhusername\">", "</span>", text,
					RegexPaserUtil.TEXTEGEXANDNRT).getText();
			String yue = redismap.get(yue_key).toString();
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("parentId", currentUser);
			map.put("usersource", Constant.YIDONG);
			map.put("loginName", login.getLoginName());
			List<User> list = userService.getUserByParentIdSource(map);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(name);
				try {
					user.setPhoneRemain(new BigDecimal(yue));
				} catch (Exception e1) {
					user.setPhoneRemain(new BigDecimal(0.00));
				}
				user.setEmail("");
				user.setRealName(name);
				// user.setIdcard("");
				user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setUsersource2(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setPackageName("");
				userService.update(user);
			} else {

				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(login.getLoginName());
				// user.setLoginPassword("");
				user.setUserName(name);
				try {
					user.setPhoneRemain(new BigDecimal(yue));
				} catch (Exception e2) {
					user.setPhoneRemain(new BigDecimal(0.00));
				}
				user.setEmail("");
				user.setRealName(name);
				// user.setIdcard("");
				// user.setAddr(addr);
				user.setUsersource(Constant.YIDONG);
				user.setUsersource2(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				// user.setPackageName(pplx);
				userService.saveUser(user);

			}
		} catch (Exception e) {
			writeLogByInfo(e);
		}

	}

	/**
	 * 查询个人信息 需要验证口令 正轨
	 */
	public void getMyInfo() {
		try {
			// 个人信息
			String text = cutil
					.get("http://www.he.10086.cn/my/individualInfoServiceAction!init.action?menuid=individualInformation&pageId="
							+ Math.random());// gbk
			// System.out.println(text);
			String name = new RegexPaserUtil(
					"<td align=\"right\">证件号码:</td>                                <td align=\"left\">",
					"<", text, RegexPaserUtil.TEXTEGEXANDNRT).getText();

			// 证件号码:
			String zjhm = new RegexPaserUtil("dhusername\">", "</span>", text,
					RegexPaserUtil.TEXTEGEXANDNRT).getText();
			String yue = redismap.get(yue_key).toString();

			Map<String, String> map = new HashMap<String, String>(2);
			map.put("parentId", currentUser);
			map.put("usersource", Constant.YIDONG);
			map.put("loginName", login.getLoginName());
			List<User> list = userService.getUserByParentIdSource(map);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(name);
				try {
					user.setPhoneRemain(new BigDecimal(yue));
				} catch (Exception e1) {
					user.setPhoneRemain(new BigDecimal(0.00));
				}
				user.setEmail("");
				user.setRealName(name);
				user.setIdcard(zjhm);
				user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setUsersource2(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setPackageName("");
				userService.update(user);
			} else {

				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(name);
				try {
					user.setPhoneRemain(new BigDecimal(yue));
				} catch (Exception e2) {
					user.setPhoneRemain(new BigDecimal(0.00));
				}

				user.setEmail("");
				user.setRealName(name);
				user.setIdcard(zjhm);
				// user.setAddr(addr);
				user.setUsersource(Constant.YIDONG);
				user.setUsersource2(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				// user.setPackageName(pplx);
				userService.saveUser(user);

			}
		} catch (Exception e) {
			writeLogByInfo(e);
		}

	}

	/**
	 * 0,采集全部 1.采集手机验证 2.采集手机已经验证
	 * 
	 * @param type
	 */
	public void startSpider() {
		try {
			int type = login.getType();
			// System.out.println(type);
			parseBegin(Constant.YIDONG);
			switch (type) {
			case 1:
				getMyInfo1();//
				saveTel();
				break;
			case 2:
				callHistory(); // 历史账单 第二步
				getMessage();
				saveFlowBill();
				saveFlowList();
				break;
			case 3:
				getMyInfo(); // 个人信息
				saveTel();
				callHistory(); // 历史账单
				getMessage();
				saveFlowBill();
				saveFlowList();
				break;
			case 4:
				getMyInfo(); // 第三步
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	public LinkedList<SpeakBillPojo> gatherMonthlyBill() {

		String url = "http://www.he.10086.cn/service/fee/qryMyBill.action?groupId=tabGroupBill";
		Map<String, String> param = new HashMap<String, String>();
		try {
			if (redismap.get(tempKey) == null) {
				String text = cutil.get(url);
				if (text.contains("https://he.ac.10086.cn/POST")) {
					Document doc = Jsoup.parse(text);
					String SAMLRequest = doc.select("input[name=SAMLRequest]")
							.get(0).val();
					String RelayState = doc.select("input[name=RelayState]")
							.get(0).val();
					param.put("SAMLRequest", SAMLRequest);
					param.put("RelayState", RelayState);
					text = cutil.post("https://he.ac.10086.cn/POST", param);
					doc = Jsoup.parse(text);
					String SAMLart = doc.select("input[name=SAMLart]").get(0)
							.val();
					String displayPic = doc.select("input[name=displayPic]")
							.get(0).val();
					RelayState = doc.select("input[name=RelayState]").get(0)
							.val();
					param = new HashMap<String, String>();
					param.put("SAMLart", SAMLart);
					param.put("displayPic", displayPic);
					param.put("RelayState", RelayState);
					text = cutil.post(url, param);
					redismap.put(tempKey, "0");
				}
			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
		}
		// String url =
		// "http://www.he.10086.cn/service/fee/qryMyBill.action?groupId=tabGroupBill";//根列表
		param = new HashMap<String, String>();
		param.put("type", "B");
		param.put("backurl", "");
		param.put("errorurl",
				"http%3A%2F%2Fwww.he.10086.cn%2Fservice%2FerrorPage.jsp");
		param.put("spid", (String) redismap.get(spidKey));
		param.put("RelayState", "");
		param.put("validCode", "");
		param.put("loginType", "1");
		param.put("mobileNum", "请输入河北移动手机号码");
		param.put("wireless", "区号(4位)+固定号码(7位或8位)");
		param.put("email", "请输入Email邮箱地址");
		param.put("servicePassword", "");
		param.put("smsValidCode", "");
		param.put("emailPwd", "");
		param.put("validCodeInput", "点击获取");
		// url =
		// "http://www.he.10086.cn/service/fee/qryMyBill!qryBillAllInfo.action?cycle=";
		url = "http://www.he.10086.cn/service/fee/qryMyBill!qryBillAllInfo.action";
		LinkedList<SpeakBillPojo> linkedList = getMonthlyBillAccess(url,
				"cycle", param, true, "yyyyMM");
		for (int i = 0; i < linkedList.size(); i++) {
			getTelDetailHtml_parse(linkedList.get(i));
		}
		return linkedList;
	}

	private void getTelDetailHtml_parse(SpeakBillPojo pojo) {
		try {
			if (pojo.getText() != null) {
				Document doc = Jsoup.parse(pojo.getText());
				Elements els = doc.select("div[class=margin_auto width_700]");
				// //System.out.println(els);
				if (els != null && els.size() != 0) {
					els = els.select("tbody");
					Element el = null;
					MobileTel tel = new MobileTel();
					el = els.get(0);
					// //System.out.println(el);
					tel.setcName(el.select("td").get(1).text());
					tel.setTeleno(el.select("td").get(3).text());
					tel.setBrand(el.select("td").get(5).text());
					tel.setDependCycle(el.select("td").get(7).text());
					el = els.get(1);
					tel.setcAllBalance(new BigDecimal(el.select("td").get(2)
							.text().trim()));
					tel.setcAllPay(new BigDecimal(el.select("td").get(8).text()
							.trim()));
					el = els.get(2);
					tel.setTcgdf(new BigDecimal(el.select("td").get(1).text()
							.trim()));
					tel.setTcwyytxf(new BigDecimal(el.select("td").get(5)
							.text().trim()));
					tel.setTcwdxf(new BigDecimal(el.select("td").get(7).text()
							.trim()));
					tel.setcTime(DateUtils.StringToDate(pojo.getMonth(),
							"yyyyMM"));
					pojo.setMobileTel(tel);
				}
			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
		}
	}

	private boolean getTelDetailHtml_parse(Map<String, String> param,
			String url, Date d, Date nowDate, MobileTel mt) {
		boolean b = true;
		String text = cutil
				.post(url,
						new CHeader(
								"http://www.he.10086.cn/service/fee/qryMyBill.action?groupId=tabGroupBill"),
						param);
		try {
			Document doc = Jsoup.parse(text);
			Elements els = doc.select("div[class=margin_auto width_700]");
			// //System.out.println(els);
			if (els != null && els.size() != 0) {
				els = els.select("tbody");
				Element el = null;
				MobileTel tel = new MobileTel();
				if (nowDate.getMonth() == d.getMonth()) {
					Map map2 = new HashMap();
					map2.put("phone", login.getLoginName());
					map2.put("cTime", d);
					List list = mobileTelService.getMobileTelBybc(map2);
					if (list != null && list.size() != 0) {
						tel = (MobileTel) list.get(0);
					} else {
						tel = new MobileTel();
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
					}
					tel.setIscm(1);
				} else {
					// 判断和修改最大月
					// 判断和修改最大月
					if (mt != null && mt.getcTime().getMonth() == d.getMonth()) {
						tel = mt;
					} else {
						tel = new MobileTel();
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
					}
					tel.setIscm(0);
				}

				// tel.setBaseUserId(currentUser);
				el = els.get(0);
				// //System.out.println(el);
				tel.setcName(el.select("td").get(1).text());
				tel.setTeleno(el.select("td").get(3).text());
				tel.setBrand(el.select("td").get(5).text());
				tel.setDependCycle(el.select("td").get(7).text());
				el = els.get(1);
				tel.setcAllBalance(new BigDecimal(el.select("td").get(2).text()
						.trim()));
				tel.setcAllPay(new BigDecimal(el.select("td").get(8).text()
						.trim()));
				el = els.get(2);
				tel.setTcgdf(new BigDecimal(el.select("td").get(1).text()
						.trim()));
				tel.setTcwyytxf(new BigDecimal(el.select("td").get(5).text()
						.trim()));
				tel.setTcwdxf(new BigDecimal(el.select("td").get(7).text()
						.trim()));
				tel.setcTime(d);
				mobileTelService.saveMobileTel(tel);
			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
			b = false;
		}
		return b;
	}

	public void callHistory() {
		try {
			String url = "http://www.he.10086.cn/service/fee/qryDetailBill!qryNewBill.action?smsrandom=";
			Map<String, String> params = new HashMap<String, String>();
			params.put("selectTaken", "");
			params.put("regionstate", "1");
			params.put("onlinetime", "201204");
			params.put("menuid", "");
			params.put("fieldErrFlag", "");
			params.put("selectncode", "");
			params.put("ncodestatus", "");
			params.put("operatype", "");
			params.put("groupId", "");
			params.put("qryscope", "0");
			params.put("queryType", "NGQryCallBill");
			params.put("qryType", "10");
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			MobileDetail md = new MobileDetail();
			md.setPhone(login.getLoginName());
			md = mobileDetailService.getMaxTime(md);
			boolean b = true;
			String text = null;
			for (int m = 0; m < ms.size(); m++) {
				String starDate = ms.get(m);
				params.put("theMonth", starDate);
				text = cutil.post(url, params);
				b = callHistory_parse(text, md, starDate);
				if (!b) {
					break;
				}
			}
		} catch (Exception e) {
			writeLogByHistory(e);
		}
	}

	private boolean callHistory_parse(String text, MobileDetail md,
			String starDate) {
		boolean b = true;
		try {
			// //System.out.println(text);
			Document doc = Jsoup.parse(text);
			Elements els = doc.getElementById("bill_page").select("tr");
			String riqi = null;
			String qssj = null;

			for (int i = 2; i < els.size(); i++) {
				Elements tds = els.get(i).select("td");
				if (tds.size() >= 8) {
					MobileDetail mDetail = new MobileDetail();
					UUID uuid = UUID.randomUUID();
					mDetail.setId(uuid.toString());
					riqi = tds.get(0).text();// 日期
					qssj = tds.get(1).text();// 起始时间
					mDetail.setcTime(DateUtils.StringToDate(
							starDate.substring(0, 4) + "-" + riqi + " " + qssj,
							"yyyy-MM-dd HH:mm:ss"));
					if (md != null && md.getcTime() != null) {
						if (mDetail.getcTime().getTime() <= md.getcTime()
								.getTime()) {
							b = false;
							break;
						}
					}
					// 通信地点
					String txdd = tds.get(2).text();
					// 通信方式
					String txfs = tds.get(3).text();
					// 对方号码
					String dfhm = tds.get(4).text();
					// 通信时长
					String txsc = tds.get(5).text();
					// 通信类型
					String txlx = tds.get(6).text();
					// 实收通信费
					// String tcyh= tds.get(7).text();
					String txf = "";
					if (tds.size() == 9) {
						txf = tds.get(8).text().replace("元", "");
					}
					int times = 0;
					try {
						TimeUtil tunit = new TimeUtil();
						times = tunit.timetoint(txsc);
					} catch (Exception e) {

					}
					mDetail.setTradeAddr(txdd);
					mDetail.setTradeWay(txfs);
					mDetail.setRecevierPhone(dfhm);
					mDetail.setTradeTime(times);
					mDetail.setTradeType(txlx);
					mDetail.setOnlinePay(new BigDecimal(txf));
					mDetail.setPhone(login.getLoginName());
					mDetail.setIscm(0);
					mobileDetailService.saveMobileDetail(mDetail);
				}
			}
		} catch (Exception e) {
		}
		return b;
	}

	// created by qian 9/12/2014
	public void getMessage() {
		try {
			String url = "http://www.he.10086.cn/service/fee/qryDetailBill!qryNewBill.action?smsrandom=";
			Map<String, String> params = new HashMap<String, String>();
			params.put("selectTaken", "");
			params.put("regionstate", "1");
			params.put("onlinetime", "201204");
			params.put("menuid", "");
			params.put("fieldErrFlag", "");
			params.put("selectncode", "");
			params.put("ncodestatus", "");
			params.put("operatype", "");
			params.put("groupId", "");
			params.put("qryscope", "0");
			params.put("queryType", "NGQrySMSBill");
			params.put("qryType", "10");

			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			MobileMessage md = new MobileMessage();
			md.setPhone(login.getLoginName());
			md = mobileMessageService.getMaxSentTime(login.getLoginName());
			boolean b = true;
			String text = null;
			for (int m = 0; m < ms.size(); m++) {
				String starDate = ms.get(m);
				params.put("theMonth", starDate);
				text = cutil.post(url, params);
				b = message_parse(text, md, starDate);
				if (!b) {
					break;
				}
			}
		} catch (Exception e) {
			writeLogByHistory(e);
		}
	}

	private boolean message_parse(String text, MobileMessage md, String starDate) {
		boolean b = true;
		try {
			Document doc = Jsoup.parse(text);

			Elements els = doc.getElementById("bill_page").select("tr");
			String date = null;
			String time = null;

			for (int i = 2; i < els.size(); i++) {
				Elements tds = els.get(i).select("td");
				if (tds.size() >= 8) {
					MobileMessage mMessage = new MobileMessage();
					UUID uuid = UUID.randomUUID();
					mMessage.setId(uuid.toString());
					date = tds.get(0).text();// 日期
					time = tds.get(1).text();// 起始时间
					mMessage.setSentTime(DateUtils.StringToDate(
							starDate.substring(0, 4) + "-" + date + " " + time,
							"yyyy-MM-dd HH:mm:ss"));
					if (md != null && md.getSentTime() != null) {
						if (mMessage.getSentTime().getTime() <= md
								.getSentTime().getTime()) {
							b = false;
							break;
						}
					}
					// 通信地点
					String txdd = tds.get(2).text();
					// 对方号码
					String dfhm = tds.get(3).text();
					// 通信方式
					String txfs = tds.get(4).text();
					// 信息类型
					String txsc = tds.get(5).text();

					mMessage.setAllPay(new BigDecimal(0.0));
					mMessage.setTradeWay(txfs);
					mMessage.setCreateTs(new Date());
					mMessage.setPhone(login.getLoginName());
					mMessage.setRecevierPhone(dfhm);
					mMessage.setSentAddr(txdd);
					mobileMessageService.save(mMessage);
				}
			}
		} catch (Exception e) {
		}
		return b;
	}

	/**
	 * 在第一次登录过程中返回的错误代码
	 * 
	 * @param codeValue
	 * @return
	 */
	public String getErrorText(String codeValue) {
		if (codeValue != null) {
			if (codeValue.contains("CMSSO_1_0500")) {
				return "系统忙，请稍候再试！";
			} else if (codeValue.contains("PP_9_0101")) {
				return "温馨提示：您输入的手机号码格式不正确，请重新输入!";
			} else if (codeValue.contains("PP_1_2015")) {
				return "温馨提示：验证码不能为空!";
			} else if (codeValue.contains("PP_1_1002")) {
				return "手机号码非本地号码,请切换至号码归属地";
			} else if (codeValue.contains("PP_1_2017")
					|| codeValue.contains("PP_1_2016")) {
				return "验证码错误,请重新输入";
			} else if (codeValue.contains("PP_1_2018")
					|| codeValue.contains("PP_1_2019")
					|| codeValue.contains("PP_1_2020")) {
				return "温馨提示：请输入正确的短信随机密码!";
			} else if (codeValue.contains("PP_1_0802")) {
				return "温馨提示：您尚未注册，请注册后使用。";
			} else if (codeValue.contains("PP_1_0820")) {
				return "对不起，密码输入错误三次，账号锁定!";
			} else if (codeValue.contains("PP_1_0803")) {
				return "您输入的密码和账户名不匹配，请重新输入";
			} else if (codeValue.contains("PP_1_0805")
					|| codeValue.contains("PP_1_0806")) {
				return "您一直使用随机密码方式登录但从未注册，请您注册后使用。";
			} else if (codeValue.contains("PP_0_1017")) {
				return "对不起，您的手机号状态为停机！";
			} else if (codeValue.contains("PP_0_1018")) {
				return "对不起，您的手机号状态为注销！";
			} else if (codeValue.contains("PP_1_0807")) {
				return "温馨提示：您已经登录本网站，请稍后再试！";
			}
		}
		return null;
	}

	/**
	 * 湖北移动抓取流量账单和详单信息
	 */
	public List<String> gatherFlowbillAndList() {
		List<String> list = new LinkedList<String>();
		Date date = getMaxFlowTime();
		CHeader header = new CHeader();
		String messageUrl = "http://www.he.10086.cn/service/fee/qryDetailBill!qryNewBill.action";
		List<String> ms = DateUtils.getMonths(6, "yyyyMM");

		Map<String, String> param = new HashMap<String, String>();
		param.put("r", new Date().toString());
		param.put("smsrandom", "");
		param.put("fieldErrFlag", "");
		param.put("groupId", "");
		param.put("menuid", "qryDetailBill");
		param.put("onlinetime", "201204");
		param.put("ncodestatus", "");

		param.put("qryscope", "0");
		param.put("qryType", "10");
		param.put("queryType", "NGQryNetBill");
		param.put("regionstate", "1");
		param.put("selectTaken", "");
		param.put("selectncode", "");
		int size = ms.size();
		for (int i = size - 1; i >= 0; i--) {
			param.put("theMonth", ms.get(i));
			String text = cutil.post(messageUrl, header, param);
			list.add(text);
		}
		return list;
	}

	@Override
	public LinkedList<? extends Object> gatherFlowList() {
		
		LinkedList<MobileOnlineList> list = new LinkedList<MobileOnlineList>();
		Date bigTime = getMaxFlowTime();
		Calendar calendar=Calendar.getInstance();
		int cyear=calendar.get(Calendar.YEAR);
		int cmonth=calendar.get(Calendar.MONTH);
		for (int i=0;i<flowBillHtmls.size();i++) {
			String html=this.flowBillHtmls.get(i);
			try {
				if (html != null && !html.contains("错误提示页面")) {
					Document doc = Jsoup.parse(html);
					Element billElement = doc.select("table").get(10);
					if (billElement.text().contains("合计")) {
						Elements bills = billElement.select("tr");
						bills.remove(bills.size() - 1);
						bills.remove(0);
						bills.remove(0);
						for(int j=0;j<bills.size();j++) {
							Elements items = bills.get(j).select("td");
							if (items.size() > 0) {
								// 起始时间
								String cDateStr=items.get(0).text().trim();
								String cTimeStr=items.get(1).text().trim();
								String[] monthAndDay=cDateStr.split("-");
								String day=monthAndDay[1].trim();
								String monthStr=monthAndDay[0].trim();
								int month=Integer.parseInt(monthStr);
								
								Date cTime ;
								if(month<=cmonth){
								 cTime=DateUtils.StringToDate(cyear+""+monthStr+day+" "+cTimeStr,
										"yyyyMMdd HH:mm:ss");
								}else{
									cTime=DateUtils.StringToDate((cyear-1)+monthStr+day+" "+cTimeStr,
											"yyyyMMdd HH:mm:ss");
								}
								// 通信地点
							    String tradeAddr=items.get(2).text().trim();
								// 上网方式(CMNET/CMWAP)
							    String onlineType=items.get(3).text().trim();
								// 时长
								String onlineTimeStr=items.get(5).text().trim();
								if (onlineTimeStr== null || "".equals(onlineTimeStr)) {
									onlineTimeStr = "0";
								}
								Long onlineTime =StringUtil.flowTimeFormat(onlineTimeStr);
								// 总流量
								String totalFlowStr=items.get(6).text().trim();
								Long totalFlow =Math.round(StringUtil
										.flowFormat(totalFlowStr));
								// 套餐优惠
								String cheapService=items.get(7).text().trim();
								// 通信费
								String chargeStr=items.get(8).text().trim();
								chargeStr = chargeStr.substring(0,
										chargeStr.length() - 1);
								BigDecimal charge = BigDecimal.valueOf(Double
										.parseDouble(chargeStr));
								
								
								
								MobileOnlineList mobileOnlineList = new MobileOnlineList();
								// 成员变量赋值
								UUID uuid = UUID.randomUUID();
								mobileOnlineList.setId(uuid.toString());
								mobileOnlineList.setPhone(login.getLoginName());
								
								mobileOnlineList.setcTime(cTime);
								if(bigTime!=null&&bigTime.getTime()>=mobileOnlineList.getcTime().getTime()){
									continue;
								}
								mobileOnlineList.setTradeAddr(tradeAddr);
								mobileOnlineList.setOnlineType(onlineType);
								mobileOnlineList.setOnlineTime(onlineTime);
								mobileOnlineList.setTotalFlow(totalFlow);
								mobileOnlineList.setCheapService(cheapService);
								mobileOnlineList.setCommunicationFees(charge);
								list.add(mobileOnlineList);
							}
						}

					}
				}
			} catch (Exception e) {
				writeLogByFlowBill(e);
			}
		}
		return list;
	}

	@Override
	public LinkedList<MonthlyBillMark> gatherFlowBill() {
		// TODO Auto-generated method stub
		this.flowBarks = getSpiderMonthsMark(true, "yyyyMM", 6, 1);
		Date date = getMaxFlowTime();
		CHeader header = new CHeader();
		String messageUrl = "http://www.he.10086.cn/service/fee/qryDetailBill!qryNewBill.action";
		Map<String, String> param = new HashMap<String, String>();
		param.put("r", new Date().toString());
		param.put("smsrandom", "");
		param.put("fieldErrFlag", "");
		param.put("groupId", "");
		param.put("menuid", "qryDetailBill");
		param.put("onlinetime", "201204");
		param.put("ncodestatus", "");

		param.put("qryscope", "0");
		param.put("qryType", "10");
		param.put("queryType", "NGQryNetBill");
		param.put("regionstate", "1");
		param.put("selectTaken", "");
		param.put("selectncode", "");
		int size = flowBarks.size();
		for (int i = size - 1; i >= 0; i--) {
			param.put("theMonth", flowBarks.get(i).getMonth());
			String text = cutil.post(messageUrl, header, param);
			flowBarks.get(i).setText(text);
		}
		flowBillHtmls=new LinkedList<String>();
		for (int i = 0; i <= size-1; i++) {
		flowBillHtmls.add(flowBarks.get(i).getText());	
		}
		gatherFlowBill_parse();
		MobileOnlineBill mobile = this.flowBarks.get(0).getMobileOnlineBill();
		if (mobile != null) {
			mobile.setIscm(1);
		}
		return this.flowBarks;
	}

	public void gatherFlowBill_parse() {

		for (MonthlyBillMark mark : flowBarks) {
			String html = mark.getText();
			try {
				if (html != null) {
					Document doc = Jsoup.parse(html);
					Element billElement = doc.select("table").get(9);

					List<Element> billList = billElement.select("tr").get(0)
							.select("span");
					// 收费流量
					String chargeFlowStr = billList.get(1).text().substring(4)
							.trim();
					long chargeFlow = Math.round(StringUtil
							.flowFormat(chargeFlowStr));
					// 总流量
					String totalFlowStr = billList.get(3).text().substring(3)
							.trim();
					long totalFlow = Math.round(StringUtil
							.flowFormat(totalFlowStr));
					String freeFlowStr1 = billElement.select("tr").get(1)
							.select("td").get(2).text().substring(12);
					String freeFlowStr2 = billElement.select("tr").get(2)
							.select("td").get(2).text().substring(12);

					long freeFlow1 = Math.round(StringUtil
							.flowFormat(freeFlowStr1));
					long freeFlow2 = Math.round(StringUtil
							.flowFormat(freeFlowStr2));
					// 免费流量
					long freeFlow = freeFlow1 + freeFlow2;
					BigDecimal charge;
					if (doc.select("table").get(10).text().contains("合计")) {
						String chargeStr = doc.select("table").get(10)
								.select("tr").last().select("span").first()
								.text().trim();
						chargeStr = chargeStr.substring(0,
								chargeStr.length() - 1);
						// 通信费用
						charge = BigDecimal.valueOf(Double
								.parseDouble(chargeStr));
					} else {
						charge = BigDecimal.valueOf(0);
					}

					MobileOnlineBill mOnlineBill = new MobileOnlineBill();
					mOnlineBill.setPhone(login.getLoginName());
					mOnlineBill.setMonthly(DateUtils.StringToDate(
							mark.getMonth(), "yyyyMM"));
					mOnlineBill.setTotalFlow(totalFlow);
					mOnlineBill.setChargeFlow(chargeFlow);
					mOnlineBill.setFreeFlow(freeFlow);
					mOnlineBill.setTrafficCharges(charge);
					mark.setObj(mOnlineBill);
				}
			} catch (Exception e) {
				writeLogByFlowBill(e);
			}
		}
	}

	public static void parseTest() throws IOException {
		InputStream in = new FileInputStream("D:/html.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				"gbk"));
		String phtml;
		StringBuffer buffer = new StringBuffer();
		while ((phtml = reader.readLine()) != null) {
			buffer.append(phtml);
		}
		String html = buffer.toString();
		Document doc = Jsoup.parse(html);
		Element billElement = doc.select("table").get(10);
		if (billElement.text().contains("合计")) {
			Elements bills = billElement.select("tr");
			bills.remove(bills.size() - 1);
			bills.remove(0);
			bills.remove(0);
			while (bills.hasText()) {
				Elements items = bills.remove(0).select("td");
				if (items.size() > 0) {
					// 起始时间
					String cDateStr=items.get(0).text().trim();
					String cTimeStr=items.get(1).text().trim();
					String[] monthAndDay=cDateStr.split("-");
					String day=monthAndDay[1];
					
//					if(month>)
//					DateUtils.StringToDate(S + "-" + ctime,
//							"yyyy-MM-dd HH:mm:ss"));
					// 通信地点
				    String tradeAddr=items.get(2).text().trim();
					// 上网方式(CMNET/CMWAP)
				    String onlineType=items.get(3).text().trim();
					// 时长
					String onlineTimeStr=items.get(5).text().trim();
					if (onlineTimeStr== null || "".equals(onlineTimeStr)) {
						onlineTimeStr = "0";
					}
					Long onlineTimetime =StringUtil.flowTimeFormat(onlineTimeStr);
					// 总流量
					String totalFlowStr=items.get(6).text().trim();
					Long totalFlow =Math.round(StringUtil
							.flowFormat(totalFlowStr));
					// 套餐优惠
					String cheapService=items.get(7).text().trim();
					// 通信费
					String chargeStr=items.get(8).text().trim();
					chargeStr = chargeStr.substring(0,
							chargeStr.length() - 1);
					BigDecimal charge = BigDecimal.valueOf(Double
							.parseDouble(chargeStr));
					System.out.println(day+cTimeStr);
					System.out.println(tradeAddr);
					System.out.println(onlineTimetime);
					System.out.println(totalFlow);
					System.out.println(cheapService);
					System.out.println(charge);
			        
				}
			}

		}

	}

	public static void main(String[] args) throws IOException {
		/*
		 * Login login = new Login("15931685323", "123110"); // Login login =
		 * new Login("15931685322","123110"); HEBYidong hn = new
		 * HEBYidong(login, null); // 初始化 hn.index(); hn.inputCode(imgurl); //
		 * 登陆 Map<String, Object> map = hn.login(); hn.close();
		 */
		// hn.getTelDetailHtml();
		// hn.sendPhoneDynamicsCode();
		// hn.getLogin().setPhoneCode(new Scanner(//System.in).nextLine());
		// hn.checkPhoneDynamicsCode();
		//

		// hn.sendPhoneUserInfo();
		// hn.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
		// hn.inputCode(imgurl1);
		// hn.checkPhoneUserInfo();
		// hn.getMyInfo();
		HEBYidong.parseTest();
	}

}
