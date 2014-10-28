package com.lkb.thirdUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.PayInfo;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IPayInfoService;
import com.lkb.service.IUserService;
import com.lkb.util.RegexPaserUtil;

public class Alipay {
	private static Logger logger = Logger.getLogger(Alipay.class);
//	private static String preorderListURL = "https://consumeprod.alipay.com/record/standard.htm?beginDate=2007.03.01&endDate=";
//	private static String afterorderListURL = "&dateRange=customDate&pageNum=";
//
//	private static String userInfoURL = "https://my.alipay.com/portal/account/index.htm";
//
//	private static String loginUrl = "https://auth.alipay.com/login/homeB.htm?redirectType=parent";

	public static void main(String[] args) throws Exception {

	}

	/*
	 * 保存用户订单信息
	 */
	public void saveUserOrderByHtmlparser(String content,
			IPayInfoService payInfoService, String currentUser,
			String alipayName) throws InterruptedException {

		Document doc = Jsoup.parse(content);

		for (int i = 1; i < 21; i++) {
			Element element = doc.select("tr[id=J-item-" + i + "]").first();
			if (element == null) {
				break;
			}

			// 交易号码或者流水号码
			String tradeNo = element.select("a[id=J-tradeNo-" + i + "]")
					.attr("title").trim();
			System.out.println(tradeNo);

			Map map = new HashMap(2);
			map.put("tradeNo", tradeNo);
			map.put("source", Constant.ZHIFUBAO);
			List<PayInfo> list1 = payInfoService.getPayInfoByTradeNoSource(map);
			if (list1 != null && list1.size() > 0) {

			} else {
				// 2014.03.18
				String time_d = element.select("p[class=time-d]").first()
						.text();
				// 09:44
				String time_d_m = element.select("p[class=time-h ft-gray]")
						.first().text();
				time_d = time_d.replaceAll("\\.", "-");
				String payTimeStr = time_d + " " + time_d_m;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date payTime = null;
				try {
					payTime = sdf.parse(payTimeStr);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					logger.info("第598行捕获异常：" + e1.getMessage());
					e1.printStackTrace();
				}
				// 交易类型
				String tradeType = element.select("p[class=consume-title]")
						.first().text();
				// 交易账号类型：流水号 或者 交易号
				String tradeNoType = element
						.select("div[id=J-tradeNo-container-" + i + "]")
						.first().text().trim().substring(0, 3);

				String receiverName = element.select("td[class=other]").first()
						.select("p[class=name]").text().trim();

				String amount = element.select("td[class=amount]").text()
						.trim();
				BigDecimal payAmount = new BigDecimal(amount);
				String status = element.select("td[class=status]").text()
						.trim();

				try {
					PayInfo payInfo = new PayInfo();
					UUID uuid = UUID.randomUUID();
					payInfo.setId(uuid.toString());
					payInfo.setPayTime(payTime);

					payInfo.setTradeNo(tradeNo);
					payInfo.setTradeNoType(tradeNoType);
					payInfo.setTradeType(tradeType);
					payInfo.setReceiverName(receiverName);
					payInfo.setAmount(payAmount);
					payInfo.setStatus(status);
					payInfo.setSource(Constant.ZHIFUBAO);
					payInfo.setAlipayName(alipayName);
					payInfoService.savePayInfo(payInfo);
				} catch (Exception e) {
					logger.info("第635行捕获异常：", e);
					e.printStackTrace();

				}
			}

		}
	}

	/*
	 * 保存用户基本信息
	 */
	public void saveUserInfo(String content, IUserService userService,
			String loginName, String currentUser) {
		// 安全等级
		String secretLevel = "";
		// 身份认证
		String identifyId = "";
		// 身份证有效期限
		String identifyTime = "";
		// 职业
		String major = "";
		// 注册时间：
		String registerDateStr = "";
		// 是否实名认证
		String isRealName = "";
		// 是否会员保障
		String isProtected = "";
		// 手机号
		String phone = "";
		// 是否绑定手机号
		String isPhone = "";
		Date registerDate = null;
		// 姓名
		String name = "";
		// 淘宝账户名
		String taobaoName = "";
		if (content != null && content.trim().length() > 0) {
			Document doc = Jsoup.parse(content);
			Element element = doc.select("ul[class=account-info-det]").get(0);
			Elements lis = element.select("li");
			// 姓名
			name = lis.get(0).select("span").get(1).text();
			RegexPaserUtil rp = new RegexPaserUtil("sign_account=","&amp;",element.html(),RegexPaserUtil.TEXTEGEXANDNRT);
			String s = rp.getText();
			if(s!=null){
					try {
						s = URLDecoder.decode(s,"utf8");
						loginName = s.split("\\^")[1];
						System.out.println(loginName);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
			}

			// 安全等级
			secretLevel = lis.get(2).select("a").get(0).text();

			// 淘宝账户名
			taobaoName = lis.get(3).select("a").get(0).text();
			if (taobaoName.contains("您还未开通淘宝服务")) {
				taobaoName = "";
			}

			Elements lis2 = doc
					.select("ul[class=account-information fn-clear]").first()
					.select("li").select("div[class=item-name]");
			Elements lis3 = doc
					.select("ul[class=account-information fn-clear]").first()
					.select("li").select("div[class=item-text]");

			for (int i = 0; i < lis2.size(); i++) {
				String question = lis2.get(i).text();
				if (question.contains("身份验证")) {
					identifyId = lis3.get(i).text();
					if (identifyId.contains("通过验证后")) {
						identifyId = "";
					} else {
						identifyId = identifyId.replace("身份证号码", "");
					}
				} else if (question.contains("身份证有效期限")) {
					identifyTime = lis3.get(i).text();
				} else if (question.contains("职业")) {
					major = lis3.get(i).text();
					if (major.contains("未设置 ")) {
						major = "";
					}
				} else if (question.contains("注册时间")) {
					registerDateStr = lis3.get(i).text();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
					try {
						registerDate = sdf.parse(registerDateStr);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						logger.info("第434行捕获异常：", e);
						e.printStackTrace();
					}
				}
			}

			Element element1 = doc.select("ul[id=J_used_pro]").get(0);
			Elements lis4 = element1.select("li");
			// 是否实名认证
			isRealName = lis4.get(0).select("div[class=account-status-det]")
					.select("p").get(0).select("span").get(0).text();

			// 是否会员保障
			isProtected = lis4.get(1).select("div[class=account-status-det]")
					.select("p").get(0).select("span").get(0).text();

			// 是否绑定手机号
			isPhone = lis4.get(2).select("div[class=account-status-det]")
					.select("p").get(0).select("span").get(0).text();

			if (isPhone.contains("已绑定")) {
				phone = lis4.get(2).select("span[class=mobile-number]").get(0)
						.text().replace("(", "").replace(")", "").trim();
			}

		}
		Date modifyDate = new Date();
		// BaseUser bu = new BaseUser();
		// Map baseMap = new HashMap();
		// baseMap.put("phone", phone);
		// baseMap.put("modifyDate", modifyDate);
		// bu.saveUserInfo(userService, baseMap, currentUser);

		Map<String, String> map = new HashMap<String, String>(2);
		map.put("loginName", loginName);
		map.put("usersource", Constant.ZHIFUBAO);
		List<User> list = userService.getUserByUserNamesource(map);
		if (list != null && list.size() > 0) {
			User user = list.get(0);
			user.setLoginName(loginName);

			user.setPhone(phone);
			user.setRealName(name);
			user.setSecretLevel(secretLevel);
			user.setTaobaoName(taobaoName);
			user.setIdcard(identifyId);
			user.setIdentifyTime(identifyTime);
			user.setMajor(major);
			user.setRegisterDate(registerDate);
			user.setIsRealName(isRealName);
			user.setIsProtected(isProtected);
			user.setIsPhone(isPhone);
			user.setUsersource(Constant.ZHIFUBAO);
			user.setUsersource2(Constant.ZHIFUBAO);
			user.setModifyDate(modifyDate);
			user.setParentId(currentUser);
			userService.update(user);
		} else {
			try {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(loginName);
				user.setPhone(phone);
				user.setRealName(name);
				user.setSecretLevel(secretLevel);
				user.setTaobaoName(taobaoName);
				user.setIdcard(identifyId);
				user.setIdentifyTime(identifyTime);
				user.setMajor(major);
				user.setRegisterDate(registerDate);
				user.setIsRealName(isRealName);
				user.setIsProtected(isProtected);
				user.setIsPhone(isPhone);
				user.setUsersource(Constant.ZHIFUBAO);
				user.setUsersource2(Constant.ZHIFUBAO);
				user.setParentId(currentUser);
				user.setModifyDate(modifyDate);
				userService.saveUser(user);
			} catch (Exception e) {
				logger.info("第518行捕获异常：", e);
				e.printStackTrace();

			}
		}

	}

	/** 解析支付宝一年前的 */
	public void anLyzerAlipayOneYear(String content,
			IPayInfoService payInfoService, String currentUser,
			String alipayName) {
		Document doc = Jsoup.parse(content);
		Elements elements = doc.select("table[id=tradeRecordsIndex]").first()
				.select("tbody").select("tr");

		java.text.DateFormat format3 = new java.text.SimpleDateFormat(
				"yyyy.MM.dd HH:mm");

		for (int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			String times = element.select("td[class=time2]").first().text();
			Date payTime = null;
			try {
				payTime = format3.parse(times);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.info("第250行捕获异常：", e);
				e.printStackTrace();
			}

			String tradeType = element.select("li[class=name fn-claer]")
					.first().text();// 付款-转账
			String tradeNo = element.select("li[class=J-bizNo]").first().text();// No
			String receiverName = element.select("td[class=other]").first()
					.text();// receriverName
			String amount = element.select("td[class=amount]").first().text();// amount
			BigDecimal payAmount = new BigDecimal(amount);
			String status = element.select("td[class=status]").first().text();// status

			System.out.println(payTime + "&&" + tradeType + "&&" + tradeNo
					+ "&&" + receiverName + "&&" + payAmount + "&&" + status);

			Map map = new HashMap(2);
			map.put("tradeNo", tradeNo);
			map.put("source", Constant.ZHIFUBAO);
			List<PayInfo> list1 = payInfoService.getPayInfoByTradeNoSource(map);
			if (list1 != null && list1.size() > 0) {

			} else {
				try {
					String tradeNoType = "交易号";
					PayInfo payInfo = new PayInfo();
					UUID uuid = UUID.randomUUID();
					payInfo.setId(uuid.toString());
					payInfo.setPayTime(payTime);

					payInfo.setTradeNo(tradeNo);
					payInfo.setTradeNoType(tradeNoType);
					payInfo.setTradeType(tradeType);
					payInfo.setReceiverName(receiverName);
					payInfo.setAmount(payAmount);
					payInfo.setStatus(status);
					payInfo.setSource(Constant.ZHIFUBAO);
					payInfo.setAlipayName(alipayName);
					payInfoService.savePayInfo(payInfo);
				} catch (Exception e) {
					logger.info("第635行捕获异常：", e);
					e.printStackTrace();

				}
			}

		}
	}

}
