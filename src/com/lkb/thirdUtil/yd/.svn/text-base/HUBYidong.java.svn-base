package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
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
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.robot.util.ContextUtil;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

/**
 * @author jzr
 * 
 */
public class HUBYidong extends BaseInfoMobile {
	
	String au;
	String pa;
	static String au1 = "4655A";
	static String pa1;
	public HUBYidong(Login login) {
		super(login);
	}

	public HUBYidong(Login login, IWarningService warningService,
			String currentUser) {
		super(login, warningService, ConstantNum.comm_hub_yidong, currentUser);
	}

	public HUBYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_hub_yidong, currentUser);
	}

	/**
	 * @return 初始化,并获取验证码
	 */
	public void init() {
		if (!isInit()) {
			String url = "https://hb.ac.10086.cn/login";
			CHeader header = new CHeader("http://www.hb.10086.cn/");
			String html = cutil.get(url, header);
			if (html != null) {
				Document doc = Jsoup.parse(html);
				Elements form = doc.select("form#fm");
				String guestIP = form.select("#guestIP").attr("value");
				redismap.put("guestIP", guestIP);
				// cutil.get(firstUrl, client);
				url = "https://hb.ac.10086.cn/SSO/loginbox";
				cutil.get(url);
				setImgUrl("https://hb.ac.10086.cn/SSO/img?codeType=0&rand="
						+ System.currentTimeMillis());
				setInit();
			}
		}
	}

	// public Map<String, Object> getImg(){
	// //https://hb.ac.10086.cn/SSO/img?codeType=0&rand=
	// Map<String,Object> map = new HashMap<String,Object>();
	// setImgUrl("https://hb.ac.10086.cn/SSO/img?codeType=0&rand="+System.currentTimeMillis());
	// String imgUrl = getAuthcode();
	// map.put("url",imgUrl);
	// return map;
	// }
	public Map<String, Object> login() {
		try {
			String text = login1();
			// System.out.println(text);
			if (text != null && text.contains("SAMLart")) {// 验证第一步登陆成功
				text = login2(text);
				// System.out.println(text);
				String url = "http://www.hb.10086.cn/my/index.action";
				// CHeader header = new CHeader("");
				text = cutil.get(url);
				// System.out.println(text);
				url = "http://www.hb.10086.cn/service/shoppingCart!cartNum.action?source=my";
				text = cutil.post(url, null);
				// System.out.println(text);
				url = "http://www.hb.10086.cn/my/billdetails/queryInvoice.action";
				text = cutil.get(url);
				// System.out.println(text);
				// url =
				// "http://www.hb.10086.cn/service/selfService/hotSellAdvice!queryHotSellAdviceList.action?telNumber="+login.getLoginName();
				// text = cutil.post(url, client, null);
				// url =
				// "http://www.hb.10086.cn/service/ad/getAdvertisementAction!init.action";
				// text = cutil.post(url, client, null);
				// System.out.println(text);
				// 解析用户基本信息
				text = login3();
				// System.out.println(text);
				// sso验证
				text = login4();
				// System.out.println(text);
				loginsuccess();
				redismap.put("password", login.getPassword());
				addTask_1(this);
				sendPhoneDynamicsCode();
			} else {
				RegexPaserUtil rp = new RegexPaserUtil("errorMsg=\"", "\"",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				String errorMeg = rp.getText();
				// System.out.println(errorMeg);
				errorMsg = errorMeg;
			}
		} catch (Exception e) {
			writeLogByLogin(e);
		}
		return map;
	}

	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		int status = 0;
		String errorMsg = "";
		// http://www.hb.10086.cn/my/account/smsRandomPass!sendSmsCheckCode.action?menuid=myDetailBill
		String url = "http://www.hb.10086.cn/my/account/smsRandomPass!sendSmsCheckCode.action?menuid=myDetailBill";
		CHeader h = new CHeader(
				"http://www.hb.10086.cn/my/billdetails/queryInvoice.action");
		Map<String, String> param = null;
		String text = cutil.post(url, h, param);
		// System.out.println(text);
		if (text.contains("\"result\":\"1\"")) {
			status = 1;
			errorMsg = "短信验证码发送成功！";
		} else {
			status = 0;
			errorMsg = "短信验证码发送失败！";
		}

		map.put("status", status);
		map.put("errorMsg", errorMsg);
		return map;
	}

	public Map<String, Object> checkPhoneDynamicsCode() {
		errorMsg = "短信验证码错误，请重新获取取短信验证码！";
		List<String> ms = DateUtils.getMonths(6, "yyyyMM");
		// http://www.hb.10086.cn/service/fee/qryNewDetailList.action?postion=outer
		String DetailTelUrl = "http://www.hb.10086.cn/service/fee/qryNewDetailList.action?postion=outer";
		// http://www.hb.10086.cn/my/billdetails/queryInvoice.action
		CHeader h = new CHeader(
				"http://www.hb.10086.cn/my/billdetails/queryInvoice.action");
		Map<String, String> param = new HashMap<String, String>();
		param.put("detailBean.selecttype", "0");
		param.put("detailBean.flag", "GSM");
		param.put("menuid", "myDetailBill");
		param.put("groupId", "tabs3");
		String password = login.getPassword();
		if (login.getPassword() == null || login.getPassword().equals("")) {
			password = redismap.get("password").toString();
		}
		param.put("detailBean.password", password);
		param.put("detailBean.chkey", login.getPhoneCode());
		String s = ms.get(0);
		param.put("detailBean.billcycle", s);
		String text = cutil.post(DetailTelUrl, h, param);
		// System.out.println(text );
		if (text.contains("中国移动通信客户详单")) {
			status = 1;
			errorMsg = "短信验证成功！";
			addTask_2(this);
		}
		map.put("status", status);
		map.put("errorMsg", errorMsg);
		return map;
	}

	public Map<String, Object> getTestData() {
		// userKey,telKey,detailKey,messKey

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userKey", saveYuE());
		map.put("telKey", saveMonthTel());
		map.put("detailKey", saveDetailTel(login.getAuthcode()));
		map.put("messKey", null);
		return map;
	}

	/**
	 * 0,采集全部 1.采集手机验证 2.采集手机已经验证
	 * 
	 * @param type
	 */
	public void startSpider() {
		try {
			parseBegin(Constant.YIDONG);
			switch (login.getType()) {
			case 1:
				saveYuE(); // 个人信息
				saveMonthTel();// 历史账单
				break;
			case 2:
				saveMessage(login.getAuthcode()); // 短信
				saveDetailTel(login.getAuthcode()); // 通话记录
				saveFlow(login.getAuthcode());//流量详单
				break;
			case 3:
				saveYuE(); // 个人信息
				saveMonthTel(); // 历史账单
				saveMessage(login.getAuthcode()); // 查询短信记录
				saveDetailTel(login.getAuthcode()); // 通话记录
				saveFlow(login.getAuthcode());//流量详单
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	private String login1() {
		String url = "https://hb.ac.10086.cn/SSO/loginbox";
		// CHeader header = new CHeader("https://hb.ac.10086.cn/login");
		Map<String, String> map = new HashMap<String, String>();
		map.put("accountType", "0");
		map.put("username", login.getLoginName());
		
		map.put("passwordType", "1");
		map.put("password", login.getPassword());

		pa1 = login.getPassword();
		redismap.put(pa, login.getPassword());
		
		map.put("smsRandomCode", "");
		map.put("validateCode", login.getAuthcode());

		au1 = login.getAuthcode();
		redismap.put(au, login.getAuthcode());
		
		map.put("action", "/SSO/loginbox");//
		map.put("style", "mymobile");
		map.put("service", "my");
		map.put("continue", "");
		map.put("submitMode", "login");
		// String string = redismap.get("guestIP").toString();
		map.put("guestIP", redismap.get("guestIP").toString());
		CHeader h = new CHeader(CHeaderUtil.Accept_,
				"http://www.hb.10086.cn/my/billdetails/queryInvoice.action",
				CHeaderUtil.Content_Type__urlencoded, "www.hb.10086.cn");
		// h.setHost("hb.ac.10086.cn");
		h.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
		h.setAccept_Encoding("gzip, deflate");
		String text = cutil.post(url, h, map);
		return text;
	}

	private String login2(String text) {
		// RelayState=&SAMLart=a6d8028d8d0a4bc2a37f8998fbc3c441&PasswordType=1&errorMsg=
		Document doc = Jsoup.parse(text);
		String actionUrl = doc.select("form[id=sso]").attr("action");
		String elayState = doc.select("input[name=elayState]").attr("value");
		String SAMLart = doc.select("input[name=SAMLart]").attr("value");
		String PasswordType = doc.select("input[name=PasswordType]").attr(
				"value");
		String errorMsg = doc.select("input[name=errorMsg]").attr("value");
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("elayState", elayState);
		map2.put("SAMLart", SAMLart);
		map2.put("PasswordType", PasswordType);
		map2.put("errorMsg", errorMsg);
		text = cutil.post(actionUrl, map2);
		return text;
	}

	private String login3() {
		String url = "http://www.hb.10086.cn/my/account/basicInfoAction.action";
		String text = cutil.get(url);
		Document userdoc = Jsoup.parse(text);
		Elements elements = userdoc.select("div[class=acc_chax]").first()
				.select("table").first().select("tr");
		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			Elements elements2 = element.select("th");
			for (int j = 0; j < elements2.size(); j++) {
				Element element2 = elements2.get(j);
				String value = element.select("td").get(j).text();
				String key = element2.text();
				if (key.contains("客户姓名")) {
					redismap.put("realName", value);
					// System.out.println("客户姓名："+value);
				} else if (key.contains("用户性别")) {
					// System.out.println("用户性别："+value);
				} else if (key.contains("证件号码")) {
					// System.out.println("证件号码："+value);
					redismap.put("idCard", value);
				} else if (key.contains("入网时间")) {
					// System.out.println("入网时间："+value);
				}
			}
		}
		return text;
	}

	private String login4() {
		String url = "http://www.hb.10086.cn/service/autoLogin.action?auto=true";
		CHeader h = new CHeader("http://www.hb.10086.cn/my/index.action");
		String text = cutil.get(url, h);
		Map<String, String> param = new HashMap<String, String>();
		if (text != null) {
			Document doc = Jsoup.parse(text);
			url = "https://hb.ac.10086.cn/SSO/post";
			Elements el = doc.select("input[name=SAMLRequest]");
			if (el != null) {
				String SAMLRequest = el.val();
				String RelayState = doc.select("input[name=RelayState]").val();
				param = new HashMap<String, String>();
				param.put("SAMLRequest", SAMLRequest);
				param.put("RelayState", RelayState);
				h = new CHeader(
						"http://www.hb.10086.cn/service/autoLogin.action?auto=true");
				text = cutil.post(url, h, param);
				if (text != null) {
					url = "http://www.hb.10086.cn/service/postLogin.action?timeStamp="
							+ System.currentTimeMillis();
					doc = Jsoup.parse(text);
					el = doc.select("input[name=SAMLart]");
					if (el != null) {
						String SAMLart = el.val();
						String errorMsg = doc.select("input[name=errorMsg]")
								.val();
						String PasswordType = doc.select(
								"input[name=PasswordType]").val();
						RelayState = doc.select("input[name=RelayState]").val();
						param = new HashMap<String, String>();
						param.put("errorMsg", errorMsg);
						param.put("PasswordType", PasswordType);
						param.put("SAMLart", SAMLart);
						param.put("RelayState", RelayState);// ch.setCookie("CmWebNumSn=14791405282,sn; WT_FPC=id=21a9cbe5ed1115cb7521407396124467:lv=1407564547486:ss=1407564547481; flagcity=0000;CmProvid=ha;CmWebtokenid=15981945805,ha;JSESSIONID=0000tTORQr-1aFOoNZrD0imAB4c:153mmogvm;cmtokenid=df6aa040ea284863baef8575a8295098@ha.ac.10086.cn;cmtokenidHeNan=df6aa040ea284863baef8575a8295098@ha.ac.10086.cn;passtype=2;")
						text = cutil.post(url, param);
					}
				}
			}
		}
		return text;
	}

	private boolean saveYuE() {
		try {
			CHeader header = new CHeader(
					"http://www.hb.10086.cn/my/account/basicInfoAction.action");
			String text = cutil.get(
					"http://www.hb.10086.cn/my/account/basicInfoAction.action",
					header);
			Document doc = Jsoup.parse(text);
			Elements tds = doc.select("tbody").get(0).select("td");
			String PUK_NUM = tds.get(1).text();
			String shebeiwangluo = tds.get(4).text();
			String yonghudengji = tds.get(5).text();
			String idcard = tds.get(7).text();
			Date registerDate = DateUtils.StringToDate(tds.get(9).text(),
					"yyyy-mm-dd");
			String ruwangdidian = tds.get(10).text();
			/*
			 * header = new
			 * CHeader("http://www.hb.10086.cn/my/account/basicInfoAction.action"
			 * ); text = cutil.post(
			 * "http://www.hb.10086.cn/my/account/custInfoMaintain.action?SMSRandomCode="
			 * +login.getPhoneCode(), header, new HashMap<String, String>());
			 * doc = new Document(text); Elements trs =
			 * doc.select("tbody").get(0).select("tr"); String lianxiren =
			 * trs.select("input[name=custInfo.linkman]").val(); String
			 * jiatingdianhua =
			 * trs.select("input[name=custInfo.hometel]").val(); String address
			 * = trs.select("input[name=custInfo.address]").val(); String
			 * bangongdianhua =
			 * trs.select("input[name=custInfo.officetel]").val(); String email
			 * = doc.select("input[name=custInfo.email]").val();
			 */

			String loginName = login.getLoginName();
			Map<String, String> parmap = new HashMap<String, String>(3);
			parmap.put("parentId", currentUser);
			parmap.put("loginName", loginName);
			parmap.put("usersource", Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(parmap);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(redismap.get("realName").toString());
				user.setIdcard(redismap.get("idCard").toString());
				user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setEmail("");
				user.setRegisterDate(registerDate);
				user.setIdcard(idcard);
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(redismap.get("realName").toString());
				user.setIdcard(redismap.get("idCard").toString());
				user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setEmail("");
				user.setRegisterDate(registerDate);
				user.setIdcard(idcard);
				userService.saveUser(user);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUBYD_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

		return false;
	}

	private BigDecimal getYuE() {
		// http://www.hb.10086.cn/my/balance/queryBalance.action
		// http://www.hb.10086.cn/my/index.action
		String url = "http://www.hb.10086.cn/my/balance/queryBalance.action";
		CHeader header = new CHeader("http://www.hb.10086.cn/my/index.action");
		Map<String, String> map = null;
		String text = cutil.post(url, header, map);
		// System.out.println(text);
		JSONObject json;
		String balance = "0";
		try {
			json = new JSONObject(text);
			balance = json.get("left").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		BigDecimal yuE = new BigDecimal(balance);
		return yuE;
	}

	private boolean saveMonthTel() {
		String url = "http://www.hb.10086.cn/service/fee/queryNewInvoice!commitServiceNew.action?postion=outer";
		Map<String, String> param = new HashMap<String, String>();
		param.put("menuid", "myBill");
		param.put("groupId", "tabs3");
		CHeader header = new CHeader(
				"http://www.hb.10086.cn/service/fee/queryNewInvoice!commitServiceNew.action?postion=outer");
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			for (String s : ms) {
				String qryMonthType = "history";
				if (DateUtils.isEqual(s)) {// 判断是否是当前月
					qryMonthType = "current";
				}
				param.put("qryMonthType", qryMonthType);
				param.put("theMonth", s);

				String text = cutil.post(url, header, param);
				// System.out.println(text);
				parseMonthTel(text, s);
				// System.out.println("23");
			}
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUBYD_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}

		return true;

	}

	private boolean saveDetailTel(String mgAuthCode) {
		/*
		 * String url =
		 * "http://www.hb.10086.cn/my/billdetails/queryInvoice.action"; CHeader
		 * h = new CHeader("http://www.hb.10086.cn/my/index.action");
		 * //h.setCookie("WEBTRENDS_ID=106.38.205.86-1408333570.826253"); String
		 * text = cutil.get(url, client, h); System.out.println(text);
		 * 
		 * url =
		 * "http://www.hb.10086.cn/service/open/mmobile/businessCode.jsp?menuid=myDetailBill"
		 * ; h = new CHeader(
		 * "http://www.hb.10086.cn/service/fee/qryNewDetailList.action?postion=outer"
		 * ); text = cutil.get(url, client, h); System.out.println(text);
		 */
		String DetailTelUrl = "http://www.hb.10086.cn/service/fee/qryNewDetailList.action?postion=outer";
		try {
			int x = 0;
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader(
					"http://www.hb.10086.cn/my/billdetails/queryInvoice.action");
			// h.setCookie("CmProvid=hb; cmtokenid=b14c96d1d9004ea89557f7ceae7b2174@hb.ac.10086.cn; CmWebtokenid=13971146032,hb; WT_FPC=id=20ba2761a518349b8441408420559115:lv=1408440451243:ss=1408440275796; WEBTRENDS_ID=10.32.228.217-1408422058.718721; R_my_menu_list_13971146032=15; n62fO8j8LA=MDAwM2IyZDcwYzAwMDAwMDAwMDQwcjguQRMxNDA4MzgyODMw; JSESSIONIDMY=4g3QTzYLfTyPDHMmYQ7mPC5NvwqqlpmzFwB7RgXGsYfcLFWyJQgF!-1551480241; hhhzdrBwTU=MDAwM2IyZDcwYzAwMDAwMDAwMDMwawNXcGQxNDA4MzgyODM2; JSESSIONIDSHOPPING=q888TzYTcSGD1hwXdDyVGnwJDTzgf4z7tdCTttBLL32pBtgBS2pg!2035465558");
			Map<String, String> param = new HashMap<String, String>();
			// detailBean.billcycle=201407
			// &detailBean.selecttype=0
			// &detailBean.flag=GSM
			// &menuid=myDetailBill
			// &groupId=tabs3
			// &detailBean.password=418840
			// &detailBean.chkey=992127
			param.put("detailBean.selecttype", "0");
			param.put("detailBean.flag", "GSM");
			param.put("menuid", "myDetailBill");
			param.put("groupId", "tabs3");
			param.put("detailBean.password", pa1);
			param.put("detailBean.chkey", login.getPhoneCode());
			for (String s : ms) {
				param.put("detailBean.billcycle", s);
				String text = cutil.post(DetailTelUrl, h, param);
				Document doc6 = Jsoup.parse(text);
				System.out.println(doc6.toString());
				parseDetailTel(doc6, x);
				// System.out.println("1");
				x++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUBYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}
		return true;
	}

	private boolean saveMessage(String mgAuthCode) {
		/*
		 * String url =
		 * "http://www.hb.10086.cn/my/billdetails/queryInvoice.action"; CHeader
		 * h = new CHeader("http://www.hb.10086.cn/my/index.action");
		 * //h.setCookie("WEBTRENDS_ID=106.38.205.86-1408333570.826253"); String
		 * text = cutil.get(url, client, h); System.out.println(text);
		 * 
		 * url =
		 * "http://www.hb.10086.cn/service/open/mmobile/businessCode.jsp?menuid=myDetailBill"
		 * ; h = new CHeader(
		 * "http://www.hb.10086.cn/service/fee/qryNewDetailList.action?postion=outer"
		 * ); text = cutil.get(url, client, h); System.out.println(text);
		 */
		String DetailTelUrl = "http://www.hb.10086.cn/service/fee/qryNewDetailList.action?postion=outer";
		try {
			int x = 0;
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader(
					"http://www.hb.10086.cn/my/billdetails/queryInvoice.action");
			// h.setCookie("CmProvid=hb; cmtokenid=b14c96d1d9004ea89557f7ceae7b2174@hb.ac.10086.cn; CmWebtokenid=13971146032,hb; WT_FPC=id=20ba2761a518349b8441408420559115:lv=1408440451243:ss=1408440275796; WEBTRENDS_ID=10.32.228.217-1408422058.718721; R_my_menu_list_13971146032=15; n62fO8j8LA=MDAwM2IyZDcwYzAwMDAwMDAwMDQwcjguQRMxNDA4MzgyODMw; JSESSIONIDMY=4g3QTzYLfTyPDHMmYQ7mPC5NvwqqlpmzFwB7RgXGsYfcLFWyJQgF!-1551480241; hhhzdrBwTU=MDAwM2IyZDcwYzAwMDAwMDAwMDMwawNXcGQxNDA4MzgyODM2; JSESSIONIDSHOPPING=q888TzYTcSGD1hwXdDyVGnwJDTzgf4z7tdCTttBLL32pBtgBS2pg!2035465558");
			Map<String, String> param = new HashMap<String, String>();
			// detailBean.billcycle=201407
			// &detailBean.selecttype=0
			// &detailBean.flag=SMS
			// &menuid=myDetailBill
			// &groupId=tabs3
			// &detailBean.password=418840
			// &detailBean.chkey=992127

			param.put("detailBean.selecttype", "0");
			param.put("detailBean.flag", "SMS");
			param.put("menuid", "myDetailBill");
			param.put("groupId", "tabs3");			
			param.put("detailBean.password", pa1);
			param.put("detailBean.chkey", login.getPhoneCode());
			
			for (String s : ms) {
				param.put("detailBean.billcycle", s);
				String text = cutil.post(DetailTelUrl, h, param);
				Document doc6 = Jsoup.parse(text);
				System.out.println(doc6.toString());
				parseMessage(doc6, x);
				// System.out.println("1");
				x++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUBYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}
		return true;
	}

	
	private void parseMessage(Document doc6, int x){
		if (doc6.toString().contains("短/彩信详单")) {
			Elements tables = doc6.select("table[id=table6]");
			if(tables.size() >=1 ){
				Elements trs = tables.get(0).select("tr");
				for (int i = 1; i < trs.size(); i++) {
					if(trs.size() >= 1){
						Elements tds = trs.get(i).select("td");
						if (tds.size() == 8) {
							String sendTime = tds.get(0).text().trim();// 起始时间
							String sendAddr = StringEscapeUtils.unescapeHtml3(
									tds.get(1).text().replaceAll("\\s*", "")).trim();// 通信地点
							String receivePhone = StringEscapeUtils.unescapeHtml3(
									tds.get(2).text().replaceAll("\\s*", "")).trim();//接受电话
							String tradeType = tds.get(3).text().replaceAll("\\s*", "")
									.trim();// 方式
							String allPay = tds.get(7).text().replaceAll("&nbsp;", "").trim();// 通信时长
							allPay = allPay.substring(1,allPay.length()-1).trim();
							Map map2 = new HashMap();
//							System.out.println(allPay);
							map2.put("phone", login.getLoginName());
							map2.put("sentTime", DateUtils.StringToDate(sendTime,
									"  yyyy-MM-dd HH:mm:ss"));
							List list = mobileMessageService.getByPhone(map2);
							if (list == null || list.size() == 0) {
								MobileMessage mMessage = new MobileMessage();
								mMessage.setAllPay(new BigDecimal(allPay));
								mMessage.setCreateTs(new Date());
								UUID uuid = UUID.randomUUID();
								mMessage.setId(uuid.toString());
								mMessage.setPhone(login.getLoginName());
								mMessage.setRecevierPhone(receivePhone);
								mMessage.setSentAddr(sendAddr);
								mMessage.setSentTime(DateUtils.StringToDate(sendTime,
										"  yyyy-MM-dd HH:mm:ss"));
								mMessage.setTradeWay(tradeType);
								mobileMessageService.save(mMessage);
							}
						}
					}
				}
			}
		}
		
	}
	//流量
	private boolean saveFlow(String mgAuthCode) {
		String DetailUrl = "http://www.hb.10086.cn/service/fee/qryNewDetailList.action?postion=outer";
		try {
			int x = 0;
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader("http://www.hb.10086.cn/my/billdetails/queryInvoice.action");
			Map<String, String> param = new HashMap<String, String>();
//			postion	outer
//			detailBean.billcycle	201410
//			detailBean.chkey	007377   / 
//			detailBean.flag	GPRSWLAN  /
//			detailBean.password	921231  /
//			detailBean.selecttype	0  /
//			groupId	tabs3  /
//			menuid	myDetailBill  /
			param.put("detailBean.selecttype", "0");
			param.put("detailBean.flag", "GPRSWLAN");
			param.put("menuid", "myDetailBill");
			param.put("groupId", "tabs3");			
			param.put("detailBean.password", pa1);
			param.put("detailBean.chkey", login.getPhoneCode());
			
			MobileOnlineList bean_List = null;
			MobileOnlineBill bean_bill = null;
			try {
				bean_List = mobileOnlineListService.getMaxTime(login.getLoginName());
				bean_bill = mobileOnlineBillService.getMaxTime(login.getLoginName());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for (String s : ms) {
				param.put("detailBean.billcycle", s);
				//201410
				String year = s.substring(0,4);
				String mon = s.substring(4,6);
				String date = year+"-"+mon;
				String text = cutil.post(DetailUrl, h, param);
				//System.out.println(text);
				try {
					Document doc6 = Jsoup.parse(text);
					parseFlowDetail(doc6,bean_List, x);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Document doc7 = Jsoup.parse(text);
					onlineBill_parse(doc7,bean_bill,date,x);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				x++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUBYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}
		return true;
	}
	//流量详单解析       套餐 通信费取不到  
	private void parseFlowDetail(Document doc6,MobileOnlineList bean_List, int x){
		try {
			if(doc6.toString().contains("上网详单")){
				Element table =doc6.select("table[id=GPRS_Item]").first();
						Elements trs = table.select("tr");
						//System.out.println(trs.size());
						for(int j = 2 ; j<trs.size()-1;j++){
							Elements tds = trs.get(j).select("td");

							Date cTime = null;
							String tradeAddr = null;
							String onlineType = null;
							long onlineTime = 0;
							long totalFlow = 0;
							String cheapService = null;
							BigDecimal communicationFees = new BigDecimal(0);
							try {
								String cTime1 = tds.get(0).text().replace("  ","");
								cTime = DateUtils.StringToDate(cTime1, "yyyy-MM-dd HH:mm:ss");
								tradeAddr = tds.get(1).text();
								onlineType = tds.get(2).text();
								String onlineTime1 = tds.get(3).text().replace("  ", "");
								onlineTime = StringUtil.flowTimeFormat(onlineTime1);
								String totalFlow1 = tds.get(4).text().replace("(", "").replace(")", "").replace("  ", "");
								totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
//								cheapService = tds.get(5).text();
//								String communicationFees1 = tds.get(6).text();// 通信费
//								communicationFees = new BigDecimal(communicationFees1);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								log.error("parseFlowDetail", e1);
							}
							
						 try {
								MobileOnlineList onlineList = new MobileOnlineList();
								UUID uuid = UUID.randomUUID();
								onlineList.setId(uuid.toString());
								onlineList.setcTime(cTime);
								if(bean_List!=null){
									 if(bean_List.getcTime().getTime()<onlineList.getcTime().getTime()){
										onlineList.setcTime(cTime);
										onlineList.setOnlineTime(onlineTime);
										onlineList.setOnlineType(onlineType);
										onlineList.setTotalFlow(totalFlow);
										onlineList.setTradeAddr(tradeAddr);
										onlineList.setPhone(login.getLoginName());  
//										onlineList.setCheapService(cheapService);
//										onlineList.setCommunicationFees(communicationFees);
										mobileOnlineListService.save(onlineList);
									 }
								 }else {
									 	onlineList.setcTime(cTime);
										onlineList.setOnlineTime(onlineTime);
										onlineList.setOnlineType(onlineType);
										onlineList.setTotalFlow(totalFlow);
										onlineList.setTradeAddr(tradeAddr);
										onlineList.setPhone(login.getLoginName());    	
//										onlineList.setCheapService(cheapService);
//										onlineList.setCommunicationFees(communicationFees);
										mobileOnlineListService.save(onlineList);
								 }
								
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//流量账单解析     通信费取不到
	public boolean  onlineBill_parse(Document doc6,MobileOnlineBill bean_Bill,String startDate,int x){
		boolean b = true;
		try {
			if(doc6.toString().contains("上网详单")){
				Element table =doc6.select("table[id=GPRS_Count]").first();
							
							Date monthly = null;
							long totalFlow = 0;
							long freeFlow = 0;
							long chargeFlow = 0;
							BigDecimal communicationFees = new BigDecimal(0);
							try {
								monthly = DateUtils.StringToDate(startDate,"yyyy-MM");
								String chargeFlow1 = table.select("tr:eq(0)>td:eq(1)").text().replace("收费流量","").replace("(", "").replace(")", "").replace("   ", "").trim();
								chargeFlow = Math.round(StringUtil.flowFormat(chargeFlow1));
								String freeFlow1 = table.select("tr:eq(0)>td:eq(2)").text().replace("免费流量","").replace("(", "").replace(")", "").replace("   ", "").trim();
								freeFlow = Math.round(StringUtil.flowFormat(freeFlow1));
								String totalFlow1 = table.select("tr:eq(0)>td:eq(3)").text().replace("总流量","").replace("(", "").replace(")", "").replace("   ", "").trim();
								totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
//								RegexPaserUtil rp = new RegexPaserUtil("优惠后通信费(实收):								"," 元								<br>",text,RegexPaserUtil.TEXTEGEXANDNRT);
//								String trafficCharges = "0";
//								if(rp.getText()!=null) {
//									trafficCharges = rp.getText();// 通信费
//								}
//								try {
//									communicationFees = new BigDecimal(trafficCharges);
//								} catch (Exception e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								log.error("parseFlowBill", e);
							}
							
							Map<String,Object> map = new LinkedHashMap<String, Object>();
							map.put("phone", login.getLoginName());
							map.put("monthly", monthly);
						    List<Map> list = mobileOnlineBillService.getMobileOnlineBill(map);
					   	    if(list==null || list.size()==0){
							MobileOnlineBill onlineBill = new MobileOnlineBill();
							UUID uuid = UUID.randomUUID();
							onlineBill.setId(uuid.toString());
				        	if(bean_Bill!=null){
								 if(bean_Bill.getMonthly().getTime()>=onlineBill.getMonthly().getTime()){
									return false;
								 }
							 }
				        	onlineBill.setMonthly(monthly);
				        	onlineBill.setChargeFlow(chargeFlow);
				        	onlineBill.setFreeFlow(freeFlow);
				        	onlineBill.setTotalFlow(totalFlow);
				        	try {
								onlineBill.setTrafficCharges(communicationFees);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	onlineBill.setPhone(login.getLoginName());  
				        	if(x==0) {
				        		onlineBill.setIscm(1);
				        	}else {
				        		onlineBill.setIscm(0);
				        	}
							mobileOnlineBillService.save(onlineBill);
					   	}
					}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	private void parseMonthTel(String text, String month) {
		if (text.contains("费用信息")) {
			// 获取本期末话费账户余额
			/*
			 * RegexPaserUtil cAllBalance_rp = new RegexPaserUtil( "话费账户余额：",
			 * "<br />", text, RegexPaserUtil.TEXTEGEXANDNRT); String rp =
			 * cAllBalance_rp.getText(); String cAllBalance_string =
			 * cAllBalance_rp.getText().replaceAll("</label>",
			 * "").replaceAll("<label>", "").trim() ; BigDecimal cAllBalance =
			 * new BigDecimal(0); ; try { cAllBalance = new
			 * BigDecimal(cAllBalance_string); } catch (Exception e1) {
			 * e1.printStackTrace(); }
			 */
			RegexPaserUtil rp1 = new RegexPaserUtil(
					"<div class=\"fyxx\" style=\"width: 740px\">", "</table>",
					text, RegexPaserUtil.TEXTEGEXANDNRT);
			String base_table = rp1.getText();
			if (base_table == null || base_table.equals("")) {
				RegexPaserUtil rp2 = new RegexPaserUtil("<div class=\"fyxx\">",
						"</table>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				base_table = rp2.getText();
				if (base_table == null || base_table.equals("")) {
					return;
				}
			}
			String tableString = base_table + "</table>";
			Document doc6 = Jsoup.parse(tableString);
			// System.out.println(doc6);
			Elements tables = doc6.select("table");
			Elements trs = tables.get(0).select("tr");
			BigDecimal tcgdf = new BigDecimal(0); // 套餐及固定费
			BigDecimal yytxf = new BigDecimal(0); // 语音通信费
			BigDecimal tcwdxf = new BigDecimal(0); // 短彩信费
			BigDecimal tcwswf = new BigDecimal(0); // 上网费
			BigDecimal zyzzywfy = new BigDecimal(0); // 自有增值业务费用
			BigDecimal dsfywfy = new BigDecimal(0); // 代收费业务费用
			BigDecimal qtfy = new BigDecimal(0); // 其他费用
			BigDecimal jmf = new BigDecimal(0); // 减免费
			BigDecimal hj = new BigDecimal(0);
			for (int i = 0; i < trs.size(); i++) {
				Element tr = trs.get(i);
				String trStr = tr.text();
				if (trStr.contains("套餐及固定费")) {
					String tcgdfs = trStr.replaceAll("套餐及固定费", "").replaceAll(
							"\\s*", "");
					try {
						if (tcgdfs != null) {
							tcgdf = new BigDecimal(tcgdfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("话音通信费")) {
					String yytxfs = trStr.replaceAll("话音通信费", "")
							.replace("•", "").replaceAll("套餐外费用", "")
							.replaceAll("\\s*", "");
					try {
						if (yytxfs != null) {
							yytxf = new BigDecimal(yytxfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (trStr.contains("短彩信费")) {
					String tcwdxfs = trStr.replaceAll("短彩信费", "")
							.replace("•", "").replaceAll("\\s*", "");
					try {
						if (tcwdxfs != null) {
							tcwdxf = new BigDecimal(tcwdxfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("上网费")) {
					String tcwswfs = trStr.replaceAll("上网费", "")
							.replace("•", "").replaceAll("\\s*", "");
					try {
						if (tcwswfs != null) {
							tcwswf = new BigDecimal(tcwswfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("自有增值业务费用")) {
					String zyzzywfys = trStr.replaceAll("自有增值业务费用", "")
							.replaceAll("\\s*", "");
					try {
						if (zyzzywfys != null) {
							zyzzywfy = new BigDecimal(zyzzywfys);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("代收费业务费用")) {
					String dsfywfys = trStr.replaceAll("代收费业务费用", "")
							.replaceAll("\\s*", "");
					try {
						if (dsfywfys != null) {
							dsfywfy = new BigDecimal(dsfywfys);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("其他费用")) {
					String qtfys = trStr.replaceAll("其他费用", "").replaceAll(
							"\\s*", "");
					try {
						if (qtfys != null) {
							qtfy = new BigDecimal(qtfys);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("减免费")) {
					String jmfs = trStr.replaceAll("减免费", "").replaceAll(
							"\\s*", "");
					try {
						if (jmfs != null) {
							jmf = new BigDecimal(jmfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("合计")) {
					String hjs = trStr.replaceAll("合计", "").replaceAll("\\s*",
							"");
					try {
						if (hjs != null) {
							hj = new BigDecimal(hjs);
						}
					} catch (Exception e) {
						// System.out.println("网页文本提取出错！");
						e.printStackTrace();
					}
				}
			}
			Map map2 = new HashMap();
			map2.put("phone", login.getLoginName());
			map2.put("cTime", DateUtils.StringToDate(month, "yyyyMM"));
			List list = mobileTelService.getMobileTelBybc(map2);
			if (list == null || list.size() == 0) {
				MobileTel mobieTel = new MobileTel();
				UUID uuid = UUID.randomUUID();
				mobieTel.setId(uuid.toString());
				// mobieTel.setBaseUserId(currentUser);
				mobieTel.setcTime(DateUtils.StringToDate(month, "yyyyMM"));
				mobieTel.setcName(redismap.get("realName").toString());
				mobieTel.setTeleno(login.getLoginName());
				// mobieTel.setBrand(brand);
				mobieTel.setTcgdf(tcgdf);
				// mobieTel.setLdxsf(ldxsf);
				// mobieTel.setMgtjhyf(mgtjhyf);
				String year = month.substring(0, 4);
				String mouth = month.substring(4, 6);
				mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)))
						+ "至"
						+ TimeUtil.getLastDayOfMonth(Integer.parseInt(year),
								Integer.parseInt(DateUtils
										.formatDateMouth(mouth))));
				mobieTel.setTcwdxf(tcwdxf);
				mobieTel.setcAllPay(hj);
				mobieTel.setTcwyytxf(yytxf);
				// mobieTel.setcAllBalance(cAllBalance);
				String cd = new SimpleDateFormat("yyyyMM").format(new Date());
				if (cd.equals(month)) {
					mobieTel.setIscm(1);
				}
				mobileTelService.saveMobileTel(mobieTel);
			}
		}
	}

	private void parseDetailTel(Document doc5, int x) {
		if (doc5.toString().contains("通话详单")) {
			Elements tables = doc5.select("table[id=table6]");
			if (tables.size() >= 1) {
				Elements trs = tables.get(0).select("tr");
				for (int i = 1; i < trs.size(); i++) {
					if (trs.size() >= 1) {
						Elements tds = trs.get(i).select("td");
						if (tds.size() > 7) {

							String thsj = tds.get(0).text();// 起始时间
							String thwz = StringEscapeUtils.unescapeHtml3(
									tds.get(1).text().replaceAll("\\s*", ""))
									.trim();// 通信地点
							String thlx = StringEscapeUtils.unescapeHtml3(
									tds.get(2).text().replaceAll("\\s*", ""))
									.trim();// 通信方式
							String dfhm = tds.get(3).text()
									.replaceAll("\\s*", "").trim();// 对方号码
							String thsc = tds.get(4).text()
									.replaceAll("\\s*", "").trim();// 通信时长
							String ctlx = StringEscapeUtils.unescapeHtml3(
									tds.get(5).text().replaceAll("\\s*", ""))
									.trim();// 通信类型
							// String mylx =
							// StringEscapeUtils.unescapeHtml3(tds.get(6).text().replaceAll("\\s*",
							// ""));//套餐优惠
							String zfy = tds.get(7).text().replaceAll("元", "")
									.replaceAll("\\s*", "").trim();// 通信费
							Map map2 = new HashMap();
							zfy = zfy.trim().substring(1, zfy.length());
							map2.put("phone", login.getLoginName());
							map2.put("cTime", DateUtils.StringToDate(thsj,
									"  yyyy-MM-dd HH:mm:ss"));
							List list = mobileDetailService
									.getMobileDetailBypt(map2);
							if (list == null || list.size() == 0) {
								MobileDetail mDetail = new MobileDetail();
								UUID uuid = UUID.randomUUID();
								mDetail.setId(uuid.toString());
								mDetail.setcTime(DateUtils.StringToDate(thsj,
										"  yyyy-MM-dd HH:mm:ss"));
								mDetail.setTradeAddr(thwz);

								if (thlx.contains("主叫")) {
									mDetail.setTradeWay("主叫");
								} else if (thlx.contains("被叫")) {
									mDetail.setTradeWay("被叫");
								}
								int times = 0;
								try {
									times = Integer
											.parseInt(removeChinese(thsc.substring(1, thsc.length())));
								} catch (Exception e) {

								}
								mDetail.setRecevierPhone(dfhm);
								mDetail.setTradeTime(times);
								mDetail.setTradeType(ctlx);
								mDetail.setTaocan("");
								mDetail.setOnlinePay(new BigDecimal(zfy));
								mDetail.setPhone(login.getLoginName());
								if (x == 0) {
									mDetail.setIscm(1);
								}
								mobileDetailService.saveMobileDetail(mDetail);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 移除html报文数据中的汉字
	 * 
	 * @param codeValue
	 * @return
	 */
	private String removeChinese(String result) {
		if (result.getBytes().length < 2) {
			result = "0";
		} else if (result.getBytes().length != result.length()) {
			result = result.substring(0, result.getBytes().length - 2);
		}
		return result;
	}

	public static void main(String[] args) {
		Login login = new Login();
		login.setLoginName("15871374122");
		login.setPassword("921231");
		HUBYidong hb = new HUBYidong(login);
		hb.index();
		// hb.getImg();
		Scanner in = new Scanner(System.in);
		System.out.println("请输入验证码:");
		String auth = in.nextLine();
		login.setAuthcode(auth);
		hb.login();
		// hb.getYuE();
		hb.sendPhoneDynamicsCode();
		System.out.println("请输入动态码：");
		String authcode = in.nextLine();

		// hb.getYuE();
		// hb.saveMonthTel();
		hb.saveMessage(authcode);
		hb.saveDetailTel(authcode);
		hb.saveFlow(authcode);//流量详单

		hb.close();
	}

}
