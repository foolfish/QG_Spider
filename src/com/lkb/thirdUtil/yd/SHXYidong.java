//山西
package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.ibatis.type.FloatTypeHandler;
import org.apache.log4j.Logger;
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
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfo;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class SHXYidong extends BaseInfoMobile {

	public String index = "https://sx.ac.10086.cn/login";
	// 验证码图片路径
	public static String imgurl = "https://sx.ac.10086.cn/common/image.jsp?l=";

	public String imgurl2 = "http://service.sx.10086.cn/checkimage.shtml?";

	public String RelayState;

	private String beginDate = null;

	public static void main(String[] args) throws Exception {

		// String str =
		// "<input name=\"1\" value=\"123\"/><input name=\"2\" value=\"321\"/>";
		// Document doc = Jsoup.parse(str);
		// Element e = doc.select("input[name=1]").first();
		// String s = e.attr("value");
		// System.out.println(s);
	}

	public SHXYidong(Login login, IWarningService warningService,
			String currentUser) {
		super(login, warningService, ConstantNum.comm_shx_yidong, currentUser);
	}

	public SHXYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_shx_yidong, currentUser);
	}

	public SHXYidong(Login login) {
		super(login);
	}

	// public Map<String,Object> index(){
	// map.put("url",getAuthcode());
	// return map;
	// }
	public void init() {
		if (!isInit()) {
			setImgUrl(imgurl);
			setInit();
		}
	}

	// 首页登录
	public Map<String, Object> login() {

		try {
			String result = login1();
			if (result == null) {
				errorMsg = "登陆过程中出现不可处理异常,请重新提交!";
			} else if (result.equals("1")) {
				// map.put("state",true);
				loginsuccess();
			} else {
				errorMsg = "密码或验证码错误,请重新输入!！";
			}
		} catch (Exception e) {
			// ErrorMsg = "登陆过程中出现不可处理异常,请重新提交!";
			writeLogByLogin(e);
		}
		// map.put("url",getAuthcode());
		if (status == 1) {
			sendPhoneDynamicsCode();
			addTask(this);
		}
		return map;
	}

	public String login1() {
		String html = cutil.get("https://sx.ac.10086.cn/login");
		// System.out.println("HTML==========" + html);
		Document doc = Jsoup.parse(html);
		String spid = doc.select("input[name=spid]").first().attr("value");
		String RelayState = doc.select("input[name=RelayState]").first()
				.attr("value");
		String url = "https://sx.ac.10086.cn/Login";
		CHeader h = new CHeader();
		h.setReferer("https://sx.ac.10086.cn/login");
		h.setAccept(CHeaderUtil.Accept_);
		h.setHost("sx.ac.10086.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();

		// CHeader h0 = new CHeader();
		// h0.setReferer("http://sx.10086.cn");
		// h0.setAccept(CHeaderUtil.Accept_);
		// h0.setHost("sx.ac.10086.cn");

		// System.out.println("spid:" + spid);
		// System.out.println("RelayState:" + RelayState);

		param.put("mobileNum_temp", login.getLoginName());
		param.put("RelayState", RelayState);
		param.put("backurl", "https://sx.ac.10086.cn/4login/backPage.jsp");
		param.put("display", "");
		param.put("errorurl", "https://sx.ac.10086.cn/4login/errorPage.jsp");
		param.put("isValidateCode", "");
		param.put("mobileNum", login.getLoginName());
		param.put("remPwd1", "");
		param.put("servicePassword", login.getPassword());
		// 随便写，不写就把中文的乱码发上去，填满6位即可
		param.put("smsValidCode", "123456");
		param.put("spid", spid);
		param.put("type", "B");
		param.put("validCode", login.getAuthcode());
		param.put("webPassword", "");

		String text = cutil.post(url, h, param);

		if (text != null) {
			// System.out.println("post_1:" + text);
			if (text.contains("https://sx.ac.10086.cn/4login/backPage.jsp")) {
				return login2(text);
			} else
				return "2";

		}

		return null;
	}

	public String login2(String text) {
		String url = "https://sx.ac.10086.cn/4login/backPage.jsp";
		CHeader h = new CHeader();
		h.setReferer("https://sx.ac.10086.cn/Login");
		h.setAccept(CHeaderUtil.Accept_);
		h.setHost("service.sx.10086.cn");
		Document doc = Jsoup.parse(text);
		String SAMLart = doc.select("input[name=SAMLart]").first()
				.attr("value");
		String RelayState0;
		RelayState0 = doc.select("input[name=RelayState]").first()
				.attr("value");
		// System.out.println("SAMLart:" + SAMLart);
		// System.out.println("RelayState:"+RelayState0);
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("SAMLart", SAMLart);
		param.put("RelayState", RelayState0);
		param.put("displayPic", "1");
		String text1 = cutil.post(url, h, param);
		// System.out.println("post_2:" + text1);
		return login3(SAMLart);
	}

	public String login3(String SAMLart) {
		String url = "http://service.sx.10086.cn/my/";
		CHeader h = new CHeader();
		h.setAccept(CHeaderUtil.Accept_);
		h.setHost("service.sx.10086.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("RelayState", this.RelayState);
		param.put("SAMLart", SAMLart);
		// 保存cookie
		// super.addCookie(key, value);
		String text2 = cutil.post(url, h, param);
		// System.out.println("last_post:" + text2);
		Map<String, String> param1 = new LinkedHashMap<String, String>();
		Document doc = Jsoup.parse(text2);
		Elements eles = doc.select("input");
		Iterator<Element> i = eles.iterator();
		while (i.hasNext()) {
			Element ele = i.next();
			param1.put(ele.attr("name"), ele.attr("value"));
			// System.out.println(ele.attr("name")+"  "+ele.attr("value"));
		}
		CHeader h1 = new CHeader();
		h1.setAccept(CHeaderUtil.Accept_);
		h1.setHost("service.sx.10086.cn");
		h1.setReferer("http://service.sx.10086.cn/my/");
		cutil.post("http://service.sx.10086.cn/login/toLoginSso.action", h1,
				param1);
		String html = cutil.get("http://service.sx.10086.cn/my/xd.html");
		// cutil
		// System.out.println("html:" + html);
		return "1";
		// return login4();
	}

	public void startSpider() {
		int type = login.getType();
		try {
			parseBegin(Constant.YIDONG);
			switch (type) {
			case 1:
				getTelDetailHtml();// 账单记录
				getMyInfo(); // 个人信息+余额
				break;
			case 2:
				getFlow();	//流量账单和详单
				callHistory(); // 通话账单
				messageHistory();// 短信详单
				break;
			case 3:
				getTelDetailHtml();// 账单记录
				getMyInfo(); // 个人信息+余额
				callHistory(); // 通话详单
				messageHistory();// 短信详单
				getFlow();	//流量账单和详单
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	// }finally{
	// parseEnd(Constant.YIDONG);
	// }
	// }

	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
		try {
			String info = cutil.get("http://service.sx.10086.cn/my/grzl.html");
			Document doc = Jsoup.parse(info);
			Element form = doc.getElementById("thisform");
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
				user.setRealName(form.select("input[id=contact_name]").first()
						.attr("value"));
				user.setIdcard(form.select("input[id=id_iccid]").first()
						.attr("value"));
				String addr = form.select("input[id=contact_address]").first()
						.attr("value");
				if (!addr.contains("开户地址"))
					user.setAddr(addr);
				else
					user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				String email = form.select("input[id=contact_email]").first()
						.attr("value");
				if (!email.contains("mail@10086.com"))
					user.setEmail(email);
				else
					user.setEmail("");
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());

				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName("");
				user.setRealName(form.select("input[id=contact_name]").first()
						.attr("value"));
				user.setIdcard(form.select("input[id=id_iccid]").first()
						.attr("value"));
				String addr = form.select("input[id=contact_address]").first()
						.attr("value");
				if (!addr.contains("开户地址"))
					user.setAddr(addr);
				else
					user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				String email = form.select("input[id=contact_email]").first()
						.attr("value");
				if (!email.contains("mail@10086.com"))
					user.setEmail(email);
				else
					user.setEmail("");
				userService.saveUser(user);
			}
		} catch (Exception e) {
			errorMsg = e.getMessage();
			sendWarningCallHistory(errorMsg);
		}
	}

	public BigDecimal getYue() {
		// TODO Auto-generated method stub
		BigDecimal phoneremain;
		CHeader h = new CHeader(CHeaderUtil.Accept_other,
				"http://service.sx.10086.cn/my/hfcx.html", null,
				"service.sx.10086.cn");
		Map<String, String> parmap = new HashMap<String, String>();
		parmap.put("requestFlag", "asynchronism");
		String text = cutil.post(
				"http://service.sx.10086.cn/enhance/fee/currentFeeInfo.action",
				h, parmap);
		// System.out.println("JSON:" + text);
		try {
			JSONObject data = new JSONObject(text);
			String num = data.getString("prepay_fee_df");
			phoneremain = new BigDecimal(num);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			phoneremain = new BigDecimal("0.00");
			e.printStackTrace();
		}
		return phoneremain;
	}

	private String getBeginDate() {
		CHeader h2 = new CHeader();
		h2.setAccept(CHeaderUtil.Accept_);
		h2.setReferer("http://service.sx.10086.cn/my/xd-2.html");
		h2.setHost("service.sx.10086.cn");
		h2.setX_requested_with(true);
		Map<String, String> param2 = new LinkedHashMap<String, String>();
		param2.put("requestFlag", "asynchronism");
		cutil.post(
				"http://service.sx.10086.cn/enhance/fee/squeryFeeInfo.action",
				h2, param2);
		String text = cutil
				.post("http://service.sx.10086.cn/enhance/points/queryMonthScore.action",
						h2, param2);
		String beginDate = "", endDate = "";
		try {
			// System.out.println("get  " + text);
			JSONObject date0 = new JSONObject(text).getJSONObject("dateInfo");
			beginDate = date0.getJSONArray("beginDate").getString(0);
			endDate = date0.getJSONArray("endDate").getString(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// eg.20140901
		return beginDate;
	}

	// 返回20140901,i为前几个月，0时为本月
	private String getEndMonth(String year, String month, int i) {
		int year1 = Integer.parseInt(year);
		int month1 = Integer.parseInt(month);
		if (month1 <= i) {
			year1 -= 1;
			month1 = 12 + month1 - i;
		} else
			month1 -= i;
		// 年份总不会减到3位数吧
		if (month1 < 10)
			return Integer.toString(year1) + "0" + Integer.toString(month1)
					+ "01";
		else
			return Integer.toString(year1) + Integer.toString(month1) + "01";

	}

	// 拿到账单记录-搞定
	public void getTelDetailHtml() {
		// TODO Auto-generated method stub
		CHeader h = new CHeader(CHeaderUtil.Accept_other,
				"http://service.sx.10086.cn/my/zd.html", null,
				"service.sx.10086.cn");
		String detailHtml = cutil.get("http://service.sx.10086.cn/my/zd.html");
		String month = null;
		try {
			Document a = Jsoup.parse(detailHtml);
			Element e0 = a.getElementById("formName");
			// 201409
			month = e0.child(0).child(0).attr("href").substring(22, 28);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (month == null)
			return;
		Map<String, String> parmap = new HashMap<String, String>();
		boolean isEnd = false;
		Map<String, String> parmap1 = new HashMap<String, String>(3);
		for (int i = 0; i < 6; i++) {
			if (isEnd)
				break;

			String month1 = this.getEndMonth(month.substring(0, 4),
					month.substring(4, 6), i).substring(0, 6);
			String zd_month = cutil
					.post("http://service.sx.10086.cn/enhance/fee/queryMonthBill/qryAccount!toNewPage.action?startMonth="
							+ month1, h, parmap);
			if (zd_month == null)
				break;
			try {
				Document a = Jsoup.parse(zd_month);
				Element e0 = a.getElementsByClass("fare_info").first();
				if (e0 == null)
					break;
				int isUpdate = 0;
				MobileTel tel = new MobileTel();
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("phone", login.getLoginName());
				map2.put("cTime",
						month1.substring(0, 4) + "-" + month1.substring(4, 6)
								+ "-01 00:00:00");
				List list = mobileTelService.getMobileTelBybc(map2);
				if (list != null && list.size() != 0) {
					tel = (MobileTel) list.get(0);
					isUpdate = 1;
				} else {
					tel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					tel.setId(uuid.toString());
				}
				if (i == 0)
					tel.setIscm(1);
				else
					tel.setIscm(0);
				// tel.setBaseUserId(currentUser);
				tel.setTeleno(login.getLoginName());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				tel.setcTime(sdf.parse(month1 + "01"));
				Element e1 = e0.getElementsContainingOwnText("套餐及固定费用").first();
				tel.setTcgdf(new BigDecimal(e1.parent().nextElementSibling()
						.child(0).text()));
				Element e2 = e0.getElementsContainingOwnText("语音通信费").first();
				tel.setTcwyytxf(new BigDecimal(e2.nextElementSibling().text()));
				// Element e3=e0.getElementsContainingOwnText("上网费").first();
				// System.out.println(e3.nextElementSibling().text());
				Element e4 = e0.getElementsContainingOwnText("短彩信费").first();
				tel.setTcwdxf(new BigDecimal(e4.nextElementSibling().text()));
				Element e5 = e0.getElementsContainingOwnText("自有增值业务费").first();
				tel.setZzywf(new BigDecimal(e5.parent().nextElementSibling()
						.child(0).text()));
				// Element
				// e6=e0.getElementsContainingOwnText("代收费业务费用").first();
				// System.out.println(e6.parent().nextElementSibling().child(0).text());
				// Element e7=e0.getElementsContainingOwnText("其它").first();
				// System.out.println(e7.parent().nextElementSibling().text());
				Element e8 = e0.getElementsContainingOwnText("小计").first();
				tel.setcAllPay(new BigDecimal(e8.parent().nextElementSibling()
						.child(0).text()));
				Element e9 = e0.getElementsContainingOwnText("合计").first();
				tel.setcAllBalance(new BigDecimal(e9.parent()
						.nextElementSibling().child(0).text()));
				if (isUpdate == 1)
					mobileTelService.update(tel);
				else
					mobileTelService.saveMobileTel(tel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 拿到短信记录
	public void messageHistory() {
		// TODO Auto-generated method stub
		if (this.beginDate == null)
			return;
		// else
		// System.out.println(this.beginDate);
		CHeader h = new CHeader();
		h.setAccept(CHeaderUtil.Accept_);
		h.setHost("service.sx.10086.cn");
		h.setReferer("http://service.sx.10086.cn/my/xd-2.html");
		h.setX_requested_with(true);

		// 时间戳标记
		int isEnd = 0;
		// 六个月！
		MobileMessage md = new MobileMessage();
		md = mobileMessageService.getMaxSentTime(login.getLoginName());

		List<MobileMessage> messageList = new ArrayList<MobileMessage>();

		for (int ti = 0; ti < 6; ti++) {
			if (isEnd == 1)
				break;
			String endMonth = this.getEndMonth(beginDate.substring(0, 4),
					beginDate.substring(4, 6), ti);
			Map<String, String> parmap = new HashMap<String, String>();
			// parmap.clear();
			parmap.put("beginMonth",
					endMonth.substring(0, 4) + "-" + endMonth.substring(4, 6)
							+ "-" + endMonth.substring(6, 8));// 2014-09-01
			parmap.put("detailType", "4");
			parmap.put("endMonth", endMonth);
			// System.out.println("endMonth "+endMonth);
			parmap.put("zaType", "1");
			String detailMonth = cutil
					.post("http://service.sx.10086.cn/enhance/fee/queryDetail/queryDetail!queryLocalDetail22.action",
							h, parmap);
			// System.out.println("result  " + detailMonth);

			if (detailMonth == null){
				mobileMessageService.insertbatch(messageList);
				return;
				}
			if (detailMonth.compareTo("http://service.sx.10086.cn/my/xd.html") == 0){
				mobileMessageService.insertbatch(messageList);
				return;
				}

			/** Jsoup处理form表单并存储！ */
			Document smsDoc = Jsoup.parse(detailMonth);
			Elements e0 = smsDoc.getElementsByClass("table-form-b1");
			Iterator<Element> i = e0.iterator();
			while (i.hasNext()) {
				Element e1 = i.next();
				Iterator<Element> j = e1.getElementsByTag("tr").iterator();
				while (j.hasNext()) {
					Element e2 = j.next();
					// 短信记录刚好7个<td>
					if (e2.childNodeSize() != 7)
						continue;
					Iterator<Element> k = e2.getElementsByTag("td").iterator();
					// 0 09-03 16:30:39
					// 1 13323826252
					// 2 发送
					// 3 点对点短信 || 梦网彩信
					// 4 短信 || 彩信
					// 5
					// 6 0.10元
					int id = 0;// 记录第几个
					boolean isNew = true;
					MobileMessage mDetail = new MobileMessage();
					UUID uuid = UUID.randomUUID();
					mDetail.setId(uuid.toString());
					while (k.hasNext() && isNew) {
						Element e3 = k.next();
						// 需要用e3.text()
						switch (id) {
						case 0:
							mDetail.setSentTime(DateUtils.StringToDate(
									endMonth.substring(0, 4) + "-" + e3.text(),
									"yyyy-MM-dd HH:mm:ss"));
							// System.out.print(e3.text() + " ");
							if (md != null && md.getSentTime() != null) {
								if (mDetail.getSentTime().getTime() <= md
										.getSentTime().getTime()) {
									isNew = false;
									isEnd = 1;// 不再去翻下一页
									break;
								}
							}
							// mDetail.setcTime(DateUtils.StringToDate(endMonth.substring(0,4)+"-"+e3.text(),
							// "yyyy-MM-dd HH:mm:ss"));
							// System.out.print(e3.text() + " ");
							break;
						case 1:
							mDetail.setRecevierPhone(e3.text());
							// System.out.print(e3.text() + " ");
							break;
						case 2:
							mDetail.setTradeWay(e3.text());
							// System.out.print(e3.text() + " ");
							break;
						case 6:
							mDetail.setAllPay(new BigDecimal(e3.text()
									.replaceAll("元", "")));
							mDetail.setPhone(login.getLoginName());
							messageList.add(mDetail);
							// mobileMessageService.save(mDetail);
							break;
						}
						id++;
						// System.out.println(e3.outerHtml() + " ");
					}
					// System.out.println();
				}
			}

		}
		mobileMessageService.insertbatch(messageList);
	}

	// detailType的value：2为语音详单，4为短信详单，0为全部详单，此为拿到通话记录
	public void callHistory() {
		if (this.beginDate == null)
			return;
		CHeader h = new CHeader();
		h.setAccept(CHeaderUtil.Accept_);
		h.setHost("service.sx.10086.cn");
		h.setReferer("http://service.sx.10086.cn/my/xd-2.html");
		h.setX_requested_with(true);

		// 时间戳标记
		int isEnd = 0;
		// 六个月！
		MobileDetail md = new MobileDetail();
		md.setPhone(login.getLoginName());
		md = mobileDetailService.getMaxTime(md);

		List<MobileDetail> detailList = new ArrayList<MobileDetail>();

		for (int ti = 0; ti < 6; ti++) {
			if (isEnd == 1)
				break;
			String endMonth = this.getEndMonth(beginDate.substring(0, 4),
					beginDate.substring(4, 6), ti);
			Map<String, String> parmap = new HashMap<String, String>();
			// parmap.clear();
			parmap.put("beginMonth",
					endMonth.substring(0, 4) + "-" + endMonth.substring(4, 6)
							+ "-" + endMonth.substring(6, 8));// 2014-09-01
			parmap.put("detailType", "2");
			parmap.put("endMonth", endMonth);
			// System.out.println("endMonth "+endMonth);
			parmap.put("zaType", "1");
			String detailMonth = cutil
					.post("http://service.sx.10086.cn/enhance/fee/queryDetail/queryDetail!queryLocalDetail22.action",
							h, parmap);
			// System.out.println("result  " + detailMonth);
			if (detailMonth == null){
				mobileDetailService.insertbatch(detailList);
				return;
				}
			if (detailMonth.compareTo("http://service.sx.10086.cn/my/xd.html") == 0){
				mobileDetailService.insertbatch(detailList);
				return;			
			}
			/** Jsoup处理form表单并存储！ */
			Document smsDoc = Jsoup.parse(detailMonth);
			Elements e0 = smsDoc.getElementsByClass("table-form-b1");//一个form是一页！
			Iterator<Element> i = e0.iterator();
			while (i.hasNext()) {
				Element e1 = i.next();
				Iterator<Element> j = e1.getElementsByTag("tr").iterator();
				while (j.hasNext()) {
					Element e2 = j.next();
					// 电话记录刚好8个<td>
					if (e2.childNodeSize() != 8)
						continue;
					Iterator<Element> k = e2.getElementsByTag("td").iterator();
					// 0 09-10 14:53:47
					// 1 北京
					// 2 主叫
					// 3 15600369306
					// 4 9秒
					// 5 国内漫游
					// 6
					// 7 0.60
					int id = 0;// 记录第几个
					boolean isNew = true;
					MobileDetail mDetail = new MobileDetail();
					UUID uuid = UUID.randomUUID();
					mDetail.setId(uuid.toString());
					while (k.hasNext() && isNew) {
						Element e3 = k.next();
						// 需要用e3.text()
						switch (id) {
						case 0:
							mDetail.setcTime(DateUtils.StringToDate(
									endMonth.substring(0, 4) + "-" + e3.text(),
									"yyyy-MM-dd HH:mm:ss"));
							// System.out.print(e3.text() + " ");
							if (md != null
									&& !md.getcTime()
											.before(mDetail.getcTime())) {
								isNew = false;
								break;
							}
							break;
						case 1:
							mDetail.setTradeAddr(e3.text());
							// System.out.print(e3.text() + " ");
							break;
						case 2:
							mDetail.setTradeWay(e3.text());
							// System.out.print(e3.text() + " ");
							break;
						case 3:
							mDetail.setRecevierPhone(e3.text());
							// System.out.print(e3.text() + " ");
							break;
						case 4:
							int time = 0;
							String str = e3.text().replaceAll("小", "");
							int e_h = str.indexOf("时");
							if (e_h > -1) {
								time += Integer.parseInt(str.substring(0, e_h)) * 3600;
								str = str.substring(e_h + 1);
							}
							e_h = str.indexOf("分");
							if (e_h > -1) {
								time += Integer.parseInt(str.substring(0, e_h)) * 60;
								str = str.substring(e_h + 1);
							}
							time += Integer.parseInt(str.replaceAll("秒", ""));
							mDetail.setTradeTime(time);
							// System.out.print(e3.text() + " ");
							break;
						case 5:
							mDetail.setTradeType(e3.text());
							// ystem.out.print(e3.text() + " ");
							break;
						case 7:
							mDetail.setOnlinePay(new BigDecimal(e3.text()));
							mDetail.setPhone(login.getLoginName());
							if (ti == 0)
								mDetail.setIscm(1);
							else
								mDetail.setIscm(0);
							if (isNew)
								detailList.add(mDetail);
							// mobileDetailService.saveMobileDetail(mDetail);
							// System.out.println(e3.text() + " ");
							break;
						}
						id++;
						// System.out.println(e3.outerHtml() + " ");
					}
					// System.out.println();
				}
			}

		}
		mobileDetailService.insertbatch(detailList);
	}

	public Map<String, Object> getSecImgUrl() {
		// TODO Auto-generated method stub
		setImgUrl(imgurl2);// 更换验证码地址二次验证
		map.put("url", getAuthcode());
		map.put(CommonConstant.status, 1);
		return map;
	}

	// 完成
	public Map<String, Object> checkPhoneDynamicsCode() {
		String msg = null;
		CHeader h = new CHeader();
		h.setRespCharset("utf-8");
		h.setReferer("http://service.sx.10086.cn/my/xd.html");
		h.setX_requested_with(true);
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("randomPwd", login.getPhoneCode());
		String text = cutil
				.post("http://service.sx.10086.cn/enhance/operate/pwdModify/randomPwdCheck.action",
						h, param);
		try {
			JSONObject retMsg = new JSONObject(text);
			msg = retMsg.getString("retMsg");
			// System.out.println(msg);
			// System.out.println("retMsg   " + msg);
			if (!msg.contains("ok")) {
				map.put(CommonConstant.errorMsg, msg);
				return map;
			}
			if (msg.contains("ok")) {
				CHeader h1 = new CHeader();
				h1.setAccept(CHeaderUtil.Accept_);
				h1.setReferer("http://service.sx.10086.cn/my/xd.html");
				Map<String, String> param1 = new LinkedHashMap<String, String>();
				param1.put("extraPassword", login.getAuthcode());
				// System.out.println("extraPassword"+login.getAuthcode());
				param1.put("nexturl", "/my/xd-2.html");
				param1.put("servicepassWord", login.getPhoneCode());
				// System.out.println("servicepassWord"+login.getPhoneCode());
				String text1 = cutil.post(
						"http://service.sx.10086.cn/my/xd-2.html", h1, param1);

				/** 感觉下面这俩post没啥用，多发俩没啥坏处 */
				this.beginDate = this.getBeginDate();
				map.put(CommonConstant.status, 1);
				status = 1;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (status == 1) {
			addTask_2(this);
		}
		return map;
	}

	// 二次验证，验证码正确的情况下才能发送短信！
	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = null;
		CHeader h0 = new CHeader();
		h0.setAccept(CHeaderUtil.Accept_);
		h0.setHost("service.sx.10086.cn");
		h0.setReferer("http://service.sx.10086.cn/my/xd.html");
		h0.setX_requested_with(true);
		Map<String, String> param0 = new LinkedHashMap<String, String>();
		param0.put("seccodeverify", login.getAuthcode());
		// System.out.println("seccodeverify:" + login.getAuthcode());
		// text0为大部分为null的JSON
		String text0 = cutil
				.post("http://service.sx.10086.cn/enhance/operate/pwdModify/checkRandCode.action",
						h0, param0);
		// System.out.println("second:" + text0);
		if (text0 == null) {
			errorMsg = "发送失败";
			return map;
		}
		try {
			// System.out.println(text0);
			JSONObject secCodeReturn = new JSONObject(text0);
			// System.out.println(secCodeReturn.getString("retCode"));
			boolean secCodeIsTrue = (secCodeReturn.getString("retCode")
					.contains("0"));
			// System.out.println(secCodeIsTrue+" && "+secCodeReturn.getString("retCode"));
			if (!secCodeIsTrue) {
				map.put(CommonConstant.errorMsg, "验证码错误，请重新输入！");
				return map;
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			map.put(CommonConstant.errorMsg, "验证码出现问题，请刷新重试！");
			e1.printStackTrace();
			return map;
		}
		// 验证码正确的话继续走
		try {
			String url = "http://service.sx.10086.cn/enhance/operate/pwdModify/sendRandomPwd.action";
			CHeader h = new CHeader();
			h.setAccept(CHeaderUtil.Accept_);
			h.setHost("service.sx.10086.cn");
			h.setReferer("http://service.sx.10086.cn/my/xd.html");
			h.setX_requested_with(true);
			Map<String, String> param = new LinkedHashMap<String, String>();
			String text = cutil.post(url, h, param);
			// System.out.println("sendMessage："+text);
			try {
				JSONObject sendMessageReturn = new JSONObject(text);
				JSONObject json2 = sendMessageReturn
						.getJSONObject("SRandPassAdmOut");
				String returnMsg = json2.getString("return_msg");
				if (returnMsg.contains("ok")) {
					msg = "您的短信随机码已经发送，请注意查收！";
					status = 1;
				}
				if (returnMsg.contains("请输入手机号")) {
					msg = "您的cookie已过期，请重新输入！";
				}

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (text.contains("请输入手机号")) {
				msg = "您的cookie已过期，请重新输入！";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (msg == null) {
			msg = "发送失败!(60秒后再尝试)";
		}
		if (status == 1)
			map.put(CommonConstant.status, 1);
		else
			map.put(CommonConstant.status, 0);
		map.put(CommonConstant.errorMsg, msg);
		return map;
	}
	
	/**
	* <p>Title: getFlow</p>
	* <p>Description: 流量抓取</p>
	* @author Jerry Sun
	*/
	private void getFlow(){
		List<String> monthies = DateUtils.getMonth(6);
		for (String month : monthies) {
			String firstDayOfMonth = DateUtils.firstDayOfMonth(month.replace("/", "-"));
			String lastDayOfMonth = DateUtils.lastDayOfMonth(month, "yyyy/MM", "yyyyMMdd");
			
			String url = "http://service.sx.10086.cn/enhance/fee/queryDetail/queryDetail!queryLocalDetail22.action";
			Map<String, String> param = new LinkedMap();
			param.put("beginMonth", firstDayOfMonth);
			param.put("endMonth", lastDayOfMonth);
			param.put("detailType", "3");
			param.put("zqType", "2");
			
			String text = cutil.post(url, param);
			if(text.contains("无记录"))
				continue;
			
			/*
			 * 流量详单
			 */
			Document parse = Jsoup.parse(text);
			Elements tables = parse.select(".table-form-b1");
			if(tables.size()>0){
				for (Element table : tables) {
					if(table.text().trim().contains("起始时间")){
						Elements trs = table.getElementsByTag("tr");
						if(trs.size()>0){
							List<MobileOnlineList> list = new ArrayList<MobileOnlineList>();
							MobileOnlineList bean = new MobileOnlineList();
							if(mobileOnlineListService.getMaxTime(login.getLoginName())!=null) {
								bean = mobileOnlineListService.getMaxTime(login.getLoginName());
							}
							for (Element tr : trs) {
								Elements tds = tr.getElementsByTag("td");
								MobileOnlineList mol = new MobileOnlineList();
								if(tds.size()>0 && tds.size() == 7){
									for(int i=0; i<tds.size(); i++){
										String temp = tds.get(i).text().trim();
										switch (i) {
										case 0:
											mol.setcTime(DateUtils.StringToDate(Calendar.getInstance().getTime().getYear()+"-"+temp, "yyyy-MM-dd hh:mm:ss"));
											break;
										case 1:
											mol.setTradeAddr(temp);
											break;
										case 2:
											mol.setOnlineType(temp);
											break;
										case 3:
											mol.setOnlineTime(StringUtil.flowTimeFormat(temp));
											break;
										case 4:
											mol.setTotalFlow(StringUtil.flowFormat(temp).longValue());
											break;
										case 5:
											mol.setCheapService(temp);
											break;
										case 6:
											mol.setCommunicationFees(new BigDecimal(temp));
											break;
										}
									}
									mol.setPhone(login.getLoginName());
									//一行结束表示一条数据结束，存储
									if(bean.getcTime()!=null) {
										if (bean.getcTime().getTime()<mol.getcTime().getTime()) {
											UUID uuid = UUID.randomUUID();
											mol.setId(uuid.toString());
											list.add(mol);
									 }
									}else {
										UUID uuid = UUID.randomUUID();
										mol.setId(uuid.toString());
										list.add(mol);
									}
									mobileOnlineListService.insertbatch(list);
								}
							}
						}
					}
				}
			}
			/*
			 * 流量账单
			 */
			MobileOnlineBill mob = new MobileOnlineBill();
			String str = parse.text().trim();
			String allFlow = StringUtil.subStr("GPRS总流量", "(", str);
			String allPay = StringUtil.subStr("合计：", "元", str).trim();
			mob.setPhone(login.getLoginName());
			mob.setMonthly(DateUtils.StringToDate(month, "yyyy/MM"));
			mob.setTotalFlow(StringUtil.flowFormat(allFlow).longValue());
			try {
				if(allPay.length()>0)
				mob.setTrafficCharges(new BigDecimal(allPay));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String today = DateUtils.getToday("yyyy-MM-dd");
			if(DateUtils.isSameMonth(today, firstDayOfMonth))
				mob.setIscm(1);
			else
				mob.setIscm(0);
			
			MobileOnlineBill mob_bean = mobileOnlineBillService.getMaxTime(loginName);
			if(mob_bean!=null){
				if (mob_bean.getMonthly().getTime() < mob.getMonthly().getTime()) {
					
				}else if(mob_bean.getMonthly().getTime() == mob.getMonthly().getTime()){
					mobileOnlineBillService.update(mob);
				}
			}else {
				UUID uuid = UUID.randomUUID();
				mob.setId(uuid.toString());
				mobileOnlineBillService.save(mob);
			}
		}
	}
}
