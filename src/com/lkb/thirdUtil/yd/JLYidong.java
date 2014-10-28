package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.test.jsdx.RegexPaserUtil;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

public class JLYidong extends BaseInfoMobile {
	private static String captchaPicUrl = "https://jl.ac.10086.cn/SSO/FuJaMaJlWww?"; // 吉林移动验证码
	private static String yueKey = "JLYIDONG.yue.key";

	public JLYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_jl_yidong, currentUser);
	}

	public JLYidong(Login login) {
		super(login);
	}

	public void init() {
		setImgUrl(captchaPicUrl);
	}

	public Map<String, Object> login() {

		try {
			String result = login1();
			if (result == null) {
				// errorMsg = "登陆过程中出现不可处理异常,请重新提交!";
			} else if (result.equals("1")) {
				String url = "http://www.jl.10086.cn/service/fee/queryIndex.do?serviceType=19";
				String text = cutil.get(url);
				if (text != null) {
					String yue = new RegexPaserUtil("balance\":\"", "\"", text,
							RegexPaserUtil.TEXTEGEXANDNRT).getText();
					if (yue != null) {
						loginsuccess();
						redismap.put("jlyidong.password", login.getPassword());
						redismap.put(yueKey, yue);
						sendPhoneDynamicsCode();
						//addTask_1(this); // 开启后短信验证不通过
					}
				}
			} else {
				errorMsg = result;
			}
		} catch (Exception e) {
			writeLogByLogin(e);
		}
		return map;
	}

	public Map<String, Object> checkPhoneDynamicsCode() {

		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("serviceType", "3300");
		param.put("dateType", "1");
		param.put("radio", DateUtils.getMonths(1, "yyyyMM").get(0));// 日期

		param.put("password10086", redismap.get("jlyidong.password").toString());
		// 第一次check怎么测试都不行~只能先无脑check一下了
		CHeader c = new CHeader();
		c.setReferer("http://www.jl.10086.cn/service/fee/QueryDetailList.jsp?serviceType=3300");
		param.put("smsCheckCode", login.getPhoneCode());
		String tongHua = cutil.post(
				"http://www.jl.10086.cn/service/fee/query.do", c, param);
		if (tongHua == null) {
			errorMsg = "出现未知的异常，请重试";
		}
		Document th = Jsoup.parse(tongHua);
		try {
			Element msg = th.getElementsByTag("p").first();
			String msgString = msg.text();
			if (!msgString.contains("不正确")) {
				redismap.put("jlyidong_xdText", tongHua);
				status = 1;
				errorMsg = "短信验证成功!";
			} else {
				errorMsg = msgString;
			}

		} catch (Exception e) {
			errorMsg = "短信检验失败,请重试!";
			e.printStackTrace();
		}

		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		if (status == 1) {
			addTask(this);
		}
		return map;
	}

	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String errorMsg = null;
		int status = 0;
		String sendDynamicPWDURL = "http://www.jl.10086.cn/service/operate/sendSmsCheckCode.do?randomStr="
				+ new Date().getTime() + "&type=query";
		String text = cutil
				.get(sendDynamicPWDURL,
						new CHeader(
								"http://www.jl.10086.cn/service/fee/QueryDetailList.jsp?serviceType=3301"));
		if (text != null) {
			Document doc = Jsoup.parse(text);
			if (doc != null) {
				try {
					String str = doc.getElementsByTag("p").first().text();
					// System.out.println("\n\n\n"+str);
					if (str.contains("短信验证码已下发至您的手机中，请注意查收")) {
						status = 1;
					}
					errorMsg = str;
				} catch (Exception e) {
					e.printStackTrace();
					errorMsg = "发送失败!";
				}
			}
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg == null ? "发送失败!" : errorMsg);
		return map;
	}

	public String login1() {
		String url = "https://jl.ac.10086.cn/SSO/LoginAuthenticate";

		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("fujama", login.getAuthcode());
		param.put("loginType", "1");
		param.put("userId", login.getLoginName());
		param.put("userPassword", login.getPassword());
		String text = cutil.post(url, param);
		if (text != null) {
			if (text.contains("SAMLart")) {
				return login2(text);
			} else {
				int i = text.indexOf("error");
				int err = Integer.parseInt(text.substring(i + 7, i + 8));
				if (err == 2)
					return "验证码错误！";
				else if (err == 3)
					return "短信随机码错误！";// 不会出现！
				else if (err == 4)
					return "账号或密码错误！";
				else if (err == 5)
					return "系统出错！";
				else if (err == 6)
					return "IP受限";
			}
		}
		return null;
	}

	public String login2(String text) {
		String url = "http://www.jl.10086.cn//SsoPost_10086.jsp";
		Document doc = Jsoup.parse(text);
		String SAMLart = doc.select("input[name=SAMLart]").first()
				.attr("value");
		String RelayState = doc.select("input[name=RelayState]").first()
				.attr("value");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("SAMLart", SAMLart);
		param.put("RelayState", RelayState);
		cutil.post(url, param);
		return "1";
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
				// messageHistory();// 短信详单
				break;
			case 3:
				getMyInfo(); // 个人信息+余额
				getTelDetailHtml();// 账单记录
				callHistory(); // 通话详单
				// messageHistory();// 短信详单
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	// 未测试翻页！看页面结构很可能不翻页！
	private void callHistory() {
		// TODO Auto-generated method stub
		// this.xdText;
		if (redismap.get("jlyidong_xdText") == null)
			return;

		// System.out.println(redismap.get("jlyidong_xdText"));
		try {
			Document a = Jsoup
					.parse(redismap.get("jlyidong_xdText").toString());
			Elements e = a.getElementsByTag("tr");
			if (e == null)
				return;
			Iterator<Element> i = e.iterator();
			if (!i.hasNext())
				return;
			i.next();// 第一个是目录！所以跳过
			while (i.hasNext()) {
				Element e1 = i.next();
				if (e1.childNodeSize() == 23) {
					Iterator<Element> j = e1.children().iterator();
					int k = 0;
					int isNew = 1;// 是否存储
					MobileDetail mDetail = new MobileDetail();
					// MobileDetailService
					mDetail.setPhone(login.getLoginName());
					// 读取当前最大时间
					MobileDetail maxTime = mobileDetailService
							.getMaxTime(mDetail);
					Date max = null;
					if (maxTime != null) {
						max = maxTime.getcTime();
					}
					UUID uuid = UUID.randomUUID();
					mDetail.setId(uuid.toString());

					// int nullData=0;
					while (j.hasNext() && isNew == 1) {
						Element e3 = j.next();
						switch (k) {
						case 0:
							Date today = (Date) redismap.get("jlyidong_today");
							if (today == null)
								today = new Date();
							mDetail.setcTime(DateUtils.StringToDate(
									(today.getYear() + 1900) + e3.text(),
									"yyyyMM月dd日HH:mm:ss"));
							if (max != null)
								if (mDetail.getcTime().before(max))
									isNew = 0;
							break;
						case 1:
							mDetail.setTradeWay(e3.text());
							break;
						case 2:
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
							break;
						case 3:
							mDetail.setRecevierPhone(e3.text());
							break;
						case 4:
							mDetail.setTradeAddr(e3.text());
							break;
						case 6:
							mDetail.setTradeType(e3.text());
							break;
						case 10:
							mDetail.setOnlinePay(new BigDecimal(e3.text()));
							mDetail.setIscm(1);// 只抓一个月，当然是当前月
							mobileDetailService.saveMobileDetail(mDetail);
							break;
						}
						k++;
						// System.out.println(e3.text());
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getEndMonth(int year1, int month1, int i) {
		if (month1 <= i) {
			year1 -= 1;
			month1 = 12 + month1 - i;
		} else
			month1 -= i;
		// 年份总不会减到3位数吧
		if (month1 < 10)
			return Integer.toString(year1) + "0" + Integer.toString(month1);
		else
			return Integer.toString(year1) + Integer.toString(month1);

	}

	private void getThisMonthTelDetail(){
		MobileTel tel = new MobileTel();
		Map map2 = new HashMap();
		map2.put("phone", login.getLoginName());
		
		Date today=new Date();
		String d = this.getEndMonth(today.getYear() + 1900,
				today.getMonth() + 1, 0);
		map2.put("cTime", d.substring(0, 4) + "-" + d.substring(4, 6)
				+ "-01 00:00:00");
		int isUpdate_thisMonth=0;
		List list = mobileTelService.getMobileTelBybc(map2);
		if (list != null && list.size() != 0) {
			tel = (MobileTel) list.get(0);
			isUpdate_thisMonth = 1;
		} else {
			tel = new MobileTel();
			UUID uuid = UUID.randomUUID();
			tel.setId(uuid.toString());
		}
		tel.setIscm(1);
		tel.setTeleno(login.getLoginName());
		CHeader h = new CHeader(CHeaderUtil.Accept_other,
				"	http://www.jl.10086.cn/my/account/index.jsp",
				null, "www.jl.10086.cn");
		Map<String, String> parmap = new HashMap<String, String>();
		Random ran=new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			tel.setcTime(sdf.parse(this.getEndMonth(today.getYear() + 1900,
					today.getMonth() + 1, 0) + "01"));
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			log.error("JLYidong Tel month error",e);
			//e.printStackTrace();
		}
		String text=cutil.post("http://www.jl.10086.cn/service/fee/queryIndex.do?serviceType=19&rd="+ran.nextDouble(),h,parmap);
		try{
			String total=new JSONArray(text).getJSONObject(1).getString("total");
			tel.setcAllPay(new BigDecimal(total));
			if(isUpdate_thisMonth==1)
				mobileTelService.update(tel);
			else
				mobileTelService.saveMobileTel(tel);
		}catch(Exception e){
			log.error("JLYidong this month tel error", e);
		}
	}
	
	// 新版账单，2014.10.13修改
	@SuppressWarnings({ "deprecation", "unchecked" })
	private void getTelDetailHtml() {
		// TODO Auto-generated method stub
		Date today = (Date) redismap.get("jlyidong_today");
		if (today == null)
			today = new Date();
		
		getThisMonthTelDetail();
		
		CHeader h = new CHeader(CHeaderUtil.Accept_other,
				"http://www.jl.10086.cn/service/fee/QueryCustomerBill.jsp",
				null, "www.jl.10086.cn");
		
		Map<String, String> parmap = new HashMap<String, String>();
		for (int i = 0; i < 6; i++) {
			ArrayList<Double> m = new ArrayList<Double>();
			for (int j = 0; j < 5; j++)//5项数据保存
				m.add(0.00);
			
			parmap.put("serviceType", "485");
			parmap.put("billsAccount", this.getEndMonth(today.getYear() + 1900,
					today.getMonth() + 1, i + 1));
			// i==0 当查询最新一个月的时候把缴费情况全抓下来
			// if (true) {
			// parmap.put("radio", "0");// 最新一个月选择未缴那一项，不知道要不
			// String detailHtml0 = cutil.post(
			// "http://www.jl.10086.cn/service/fee/query.do", h,
			// parmap);
			// if (detailHtml0 == null) {
			// //System.out.println("null");
			// return;
			// }
			// this.detailParseHtml(detailHtml0, m);
			// }
			// parmap.put("radio", "1");
			// String detailHtml = cutil.post(
			// "http://www.jl.10086.cn/service/fee/query.do", h, parmap);
			// if (detailHtml == null) {
			// //System.out.println("null");
			// return;
			// }
			String detailHtml0 = cutil.post(
					"http://www.jl.10086.cn/service/fee/CustomerBillMain.do",
					h, parmap);
			detailParseHtml(detailHtml0, m);

			int isUpdate = 0;
			MobileTel tel = new MobileTel();
			Map map2 = new HashMap();
			map2.put("phone", login.getLoginName());
			String d = this.getEndMonth(today.getYear() + 1900,
					today.getMonth() + 1, i+1);
			map2.put("cTime", d.substring(0, 4) + "-" + d.substring(4, 6)
					+ "-01 00:00:00");
			// mobileTelService.get
			// System.out.println(d.substring(0, 4) + "-" + d.substring(4, 6)
			// + "-01 00:00:00");
			List list = mobileTelService.getMobileTelBybc(map2);
			if (list != null && list.size() != 0) {
				tel = (MobileTel) list.get(0);
				isUpdate = 1;
			} else {
				tel = new MobileTel();
				UUID uuid = UUID.randomUUID();
				tel.setId(uuid.toString());
			}
			tel.setIscm(0);
			// tel.setBaseUserId(currentUser);
			tel.setTeleno(login.getLoginName());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			try {
				tel.setcTime(sdf.parse(this.getEndMonth(today.getYear() + 1900,
						today.getMonth() + 1, i+1) + "01"));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				log.error("JLYidong Tel month error",e);
				//e.printStackTrace();
			}
			tel.setTcgdf(new BigDecimal(m.get(0)));
			tel.setTcwyytxf(new BigDecimal(m.get(1)));
			tel.setTcwdxf(new BigDecimal(m.get(3)));
			tel.setcAllPay(new BigDecimal(m.get(4)));
			// System.out.println(tel.getTeleno()+"  "+tel.getcTime());
			if (isUpdate == 1)
				mobileTelService.update(tel);
			else
				mobileTelService.saveMobileTel(tel);
		}
	}


	private void detailParseHtml(String detailHtml, ArrayList<Double> m) {
		// TODO Auto-generated method stub
		Document a = Jsoup.parse(detailHtml);
		Element div = a.select("div[class=costbill billqqtbg]").first();
		Element table = div.child(0);
		Elements tds = table.getElementsByTag("td");
		for (int i = 0; i < tds.size(); i++) {
			if (tds.get(i).text().contains("套餐及固定费"))
				m.set(0, Double.parseDouble(tds.get(i + 1).text()));
			else if (tds.get(i).text().contains("语音通信费"))
				m.set(1, Double.parseDouble(tds.get(i + 1).text()));
			else if (tds.get(i).text().contains("上网费"))
				m.set(2, Double.parseDouble(tds.get(i + 1).text()));
			else if (tds.get(i).text().contains("短彩信费"))
				m.set(3, Double.parseDouble(tds.get(i + 1).text()));
			else if (tds.get(i).text().contains("合计"))
				m.set(4, Double.parseDouble(tds.get(i + 1).text()));
		}
		// Element e1 = a.getElementsContainingOwnText("套餐及固定费").first();
		// if (e1 != null) {
		// String s1 = e1.parent().nextElementSibling().nextElementSibling()
		// .nextElementSibling().child(0).text();
		// m.set(0, m.get(0) + Double.parseDouble(s1));
		// }
		// Element e2 = a.getElementsContainingOwnText("语音通信费").first();
		// if (e2 != null) {
		// String s2 = e2.parent().nextElementSibling().nextElementSibling()
		// .nextElementSibling().child(0).text();
		// m.set(1, m.get(1) + Double.parseDouble(s2));
		// }
		// Element e3 = a.getElementsContainingOwnText("短彩信费").first();
		// if (e3 != null) {
		// String s3 = e3.parent().nextElementSibling().nextElementSibling()
		// .nextElementSibling().child(0).text();
		// m.set(2, m.get(2) + Double.parseDouble(s3));
		// }
		// Element e4 = a.getElementsContainingOwnText("应收金额总计").first();
		// if (e4 != null) {
		// String s4 = e4.nextSibling().toString().replaceAll("元", "");
		// m.set(3, m.get(3) + Double.parseDouble(s4));
		// }
	}

	private void getMyInfo() {
		try {
			String url = "http://www.jl.10086.cn/service/fee/query.do?serviceType=403";
			// 在这里可以用Jsoup之类的工具对返回结果进行分析，以判断登录是否成功
			String postResult = cutil.get(url);
			Document doc = Jsoup.parse(postResult);
			Element table = doc.getElementsByClass("inquiry").first()
					.getElementsByTag("table").first();
			if (table == null)
				return;
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("parentId", currentUser);
			map.put("loginName", login.getLoginName());
			map.put("usersource", Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(map);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName("");
				Element e1 = table.getElementsContainingOwnText("客户名称").first();
				if (e1.nextElementSibling().text()!="")
					user.setRealName(e1.nextElementSibling().text());
				user.setIdcard("");
				Element e2 = table.getElementsContainingOwnText("客户地址").first();
				if (e2.nextElementSibling().text()!="")
					user.setAddr(e2.nextElementSibling().text());
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				Element e3 = table.getElementsContainingOwnText("出生日期").first();
				if(e3!=null && e3.nextElementSibling().text()!=null && e3.nextElementSibling().text().length()>0)
				user.setBirthday(new SimpleDateFormat("yyyyMMdd").parse(e3
						.nextElementSibling().text()));
				Element e4 = table.getElementsContainingOwnText("客户性别").first();
				user.setSex(e4.nextElementSibling().text());
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());

				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName("");
				Element e1 = table.getElementsContainingOwnText("客户名称").first();
				if (e1.nextElementSibling().text()!="")
					user.setRealName(e1.nextElementSibling().text());
				user.setIdcard("");
				Element e2 = table.getElementsContainingOwnText("客户地址").first();
				if (e2.nextElementSibling().text()!="")
					user.setAddr(e2.nextElementSibling().text());
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				Element e3 = table.getElementsContainingOwnText("出生日期").first();
				if(e3!=null && e3.nextElementSibling().text()!=null && e3.nextElementSibling().text().length()>0)
				user.setBirthday(new SimpleDateFormat("yyyyMMdd").parse(e3
						.nextElementSibling().text()));
				Element e4 = table.getElementsContainingOwnText("客户性别").first();
				user.setSex(e4.nextElementSibling().text());
				userService.saveUser(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.JLYD_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}

	private BigDecimal getYue() {
		// TODO Auto-generated method stub
		String result = cutil
				.get("http://www.jl.10086.cn/service/fee/query.do?serviceType=19");
		String yue = Jsoup.parse(result).getElementsByClass("c_mzh").first()
				.child(0).text();
		// System.out.println(yue);
		if (yue == null)
			return new BigDecimal(0.00);
		int year = yue.indexOf("年");
		String date = yue.substring(year - 4, year + 7);
		try {
			redismap.put("jlyidong_today",
					new SimpleDateFormat("yyyy年MM月dd日").parse(date));
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			redismap.put("jlyidong_today", new Date());
			// System.out.println(this.today.toString());
			e.printStackTrace();
		}
		int moneyend = yue.lastIndexOf("元");
		int moneyfir = yue.lastIndexOf(":");
		if (moneyfir == -1)
			moneyfir = yue.lastIndexOf("：");
		if (moneyfir >= moneyend)
			return new BigDecimal(0.00);
		return new BigDecimal(yue.substring(moneyfir + 1, moneyend));
	}

	public static void main(String[] args) {
		JLYidong jl = new JLYidong(new Login("18743075319", "346911"));
		jl.index();
		jl.inputCode(jl.getImgUrl());
		jl.login();
		if (jl.islogin()) {
			jl.close();
			// jl.sendPhoneDynamicsCode();
			// System.out.println("-----------shu ru ");
			// jl.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
			// jl.checkPhoneDynamicsCode();

		}
	}
}
