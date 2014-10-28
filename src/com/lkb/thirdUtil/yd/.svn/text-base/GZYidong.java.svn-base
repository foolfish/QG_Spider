package com.lkb.thirdUtil.yd;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
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
import java.util.Scanner;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.ethz.ssh2.crypto.Base64;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.Result;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
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
import com.sun.mail.handlers.text_html;

/**
 * 贵州移动
 * 
 * @author li
 * @date 2014-9-12
 */
public class GZYidong extends BaseInfoMobile {
	private static final Logger log = Logger.getLogger(GZYidong.class);
	private static String index_param = "gzyidong_index_param";
	public static String balance = null;// 保存余额
	public String imgUrl = "https://gz.ac.10086.cn/aicas/createVerifyImageServlet";
	public String CUST_NAME = "";
	public String csrfToken = "";
	public String csrfToken1 = "";

	/**
	 * 本地测试
	 * 
	 * @param login
	 * @param currentUser
	 */
	public GZYidong(Login login) {
		super(login);
	}

	public GZYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_gz_yidong, currentUser);
	}

	public GZYidong(Login login, IWarningService warningService,
			String currentUser) {
		super(login, warningService, ConstantNum.comm_gz_yidong, currentUser);
	}

	public static void main(String[] args) {
		Login login = new Login("18702401460", "809713");
		// Login login = new Login("18702401461", "809713");
		GZYidong hn = new GZYidong(login);
		hn.index();
		hn.inputCode(hn.getImgUrl());
		// 登陆
		hn.login();
		hn.getbill();
		// hn.sendPhoneDynamicsCode();
		// System.out.println("请输入手机口令：");
		// hn.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
		// hn.checkPhoneDynamicsCode();
		// hn.getMyInfo();
		// hn.callHistory();
		// hn.getMessage();
		hn.close();
	}

	public void init() {
		if (!isInit()) {
			try {
				String url = "https://gz.ac.10086.cn/login";
				CHeader h = new CHeader("http://www.gz.10086.cn/");
				String text = cutil.get(url, h);
				if (text != null) {
					Map<String, String> params = new LinkedHashMap<String, String>();
					Document doc = Jsoup.parse(text);
					Elements form = doc.select("form#login_form");
					String ENCRYPT_FLAG = form.select("[name=ENCRYPT_FLAG]")
							.attr("value");//
					String VERIFY_CODE_FLAG = form.select(
							"[name=VERIFY_CODE_FLAG]").attr("value");//
					String ai_param_loginIndex = form.select(
							"[name=ai_param_loginIndex]").attr("value");//
					String ai_param_loginTypes = form.select(
							"[name=ai_param_loginTypes]").attr("value");//
					String appId = form.select("[name=appId]").attr("value");//
					String filter_rule = form.select("[name=filter_rule]")
							.attr("value");
					String lt = "null";
					String service = "null";
					try {
						params.put("ENCRYPT_FLAG", ENCRYPT_FLAG);
						params.put("VERIFY_CODE_FLAG", VERIFY_CODE_FLAG);
						params.put("ai_param_loginIndex", ai_param_loginIndex);
						params.put("ai_param_loginTypes", ai_param_loginTypes);
						params.put("appId", appId);
						params.put("filter_rule", filter_rule);
						params.put("loginType", "1");
						params.put("lt", lt);
						params.put("service", service);
						setImgUrl(imgUrl);
						setInit();
						redismap.put(index_param, params);
					} catch (Exception e) {
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, Object> login() {
		try {
			Map<String, String> param1 = new LinkedHashMap<String, String>();
			param1.put("verifyCode", login.getAuthcode());
			String code = cutil.post(
					"https://gz.ac.10086.cn/aicas/verifyCodeServlet", param1);
			if (code.contains("9999")) {
				errorMsg = "请输入正确的验证码！";
				// System.out.println("请输入正确的验证码");
			} else if (code.contains("0000")) {

				String url = "https://gz.ac.10086.cn/aicas/login";
				Map<String, String> param = (Map<String, String>) redismap
						.get(index_param);
				param.put(
						"password",
						new String(Base64.encode(login.getPassword().getBytes(
								"utf-8"))));
				param.put("rndPassword", "");
				param.put("username", login.getLoginName());
				param.put("verifyCode", login.getAuthcode());
				CHeader h = new CHeader("https://gz.ac.10086.cn/login");
				h.setRespCharset("gbk");
				h.setAccept(CHeaderUtil.Accept_);
				String text = cutil.post(url, h, param); // 第一步
				if (text.contains("错误") || text.contains("锁定")) {
					errorMsg = getErrorText(text);
				}
				if (errorMsg == null && text != null) {
					Document doc = Jsoup.parse(text);
					Elements form = doc.select("form#login_form");
					String ticket = form.select("[name=ticket]").attr("value");//
					String host = form.select("[name=host]").attr("value");//
					param = new LinkedHashMap<String, String>();
					param.put("ticket", ticket);
					param.put("host", host);
					h = new CHeader("https://gz.ac.10086.cn/login");
					h.setRespCharset("GBK");
					text = cutil.post("http://www.gz.10086.cn/my", h, param);// http://www.gz.10086.cn/my/
					h.setRespCharset("utf-8");
					text = cutil.get("http://www.gz.10086.cn/my/", h);
					text = cutil
							.get("http://www.gz.10086.cn/service/app?service=page/other.HotBusi&listener=initPage");
					text = cutil.get(
							"http://www.gz.10086.cn/pmsV4-web/pms/check.do", h);
					h = new CHeader("http://www.gz.10086.cn/my/");
					param = new LinkedHashMap<String, String>();
					text = cutil
							.post("http://www.gz.10086.cn/pmsV4-web/pms/checkSession.do",
									h, param);
					text = cutil
							.get("http://www.gz.10086.cn/pmsV4-web/pms/Login2.do?url=http%3A%2F%2Fwww.gz.10086.cn%2Fmy%2F");// http://www.gz.10086.cn/my/
					text = cutil
							.get("https://gz.ac.10086.cn/login?service=http://www.gz.10086.cn/pmsV4-web/pms/showCasLogin2.do&ai_param_loginTypes=2,1,3&username=&ai_param_loginIndex=6");
					doc = Jsoup.parse(text);
					String ticket1 = doc.select("input[name=ticket]").attr(
							"value");//
					String host1 = doc.select("input[name=host]").attr("value");//
					param = new LinkedHashMap<String, String>();
					param.put("host", host1);
					param.put("ticket", ticket1);
					h = new CHeader();
					text = cutil
							.post("http://www.gz.10086.cn/pmsV4-web/pms/showCasLogin2.do",
									h, param);
					text = cutil.get("http://www.gz.10086.cn/my/");
					text = cutil
							.get("http://www.gz.10086.cn/service/app?service=page/other.HotBusi&listener=initPage");
					text = cutil
							.get("http://www.gz.10086.cn/pmsV4-web/pms/check.do");
					// System.out.println(text);
					param = new LinkedHashMap<String, String>();
					text = cutil
							.post("http://www.gz.10086.cn/pmsV4-web/pms/checkSession.do",
									param);// {msg:1,reason:'您已经成功登录！',phoneId:18702401460}

					h = new CHeader("http://www.gz.10086.cn/my/");
					param = new LinkedHashMap<String, String>();
					if (text.contains("成功")) {
						text = cutil
								.post("http://www.gz.10086.cn/pmsV4-web/pms/feeSimple.do",
										h, param);
						RegexPaserUtil rp = new RegexPaserUtil(
								"CommonFee\":\"", "\",\"msg", text,
								RegexPaserUtil.TEXTEGEXANDNRT);
						balance = rp.getText(); // 余额
						loginsuccess();
						addTask_1(this);
						sendPhoneDynamicsCode();
					}
				}
			}
		} catch (Exception e) {
			writeLogByLogin(e);
		}
		return map;
	}

	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		int status = 0;
		String errorMsg = null;
		String url = "http://www.gz.10086.cn/service/fee/xdcx.jsp";
		CHeader h = new CHeader("http://www.gz.10086.cn/my/");
		String text = cutil.get(url, h);
		text = cutil
				.get("https://gz.ac.10086.cn/aicas/loginStatus?jsoncallback=jQuery172010282128922524536_"
						+ System.currentTimeMillis()
						+ "&service=http://www.gz.10086.cn/service/cascallback/login&forceLogoutUrl=ics_23_40001&_="
						+ System.currentTimeMillis());
		RegexPaserUtil rp = new RegexPaserUtil("\"host\":\"", "\",\"tic", text,
				RegexPaserUtil.TEXTEGEXANDNRT);
		String host = rp.getText();
		rp = new RegexPaserUtil("ticket\":\"", "\"}]", text,
				RegexPaserUtil.TEXTEGEXANDNRT);
		String ticket = rp.getText();
		text = cutil
				.get("http://www.gz.10086.cn/service/cascallback/login?ticket="
						+ ticket + "&host=" + host);
		url = "http://www.gz.10086.cn/service/fee/xdcx.jsp";
		h = new CHeader("http://www.gz.10086.cn/my/");
		text = cutil.get(url, h); // 发送短信
		rp = new RegexPaserUtil("TOKEN : \"", "\"", text,
				RegexPaserUtil.TEXTEGEXANDNRT);
		csrfToken = rp.getText();
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("csrfToken", csrfToken);
		redismap.put("csrfToken", params);
		if (text.contains("频繁")) {
			errorMsg = "30秒内只能获取一次验证码，不要频繁获取!";
		} else {
			status = 1;
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		return map;

	}

	// 短信登陆
	public Map<String, Object> checkPhoneDynamicsCode() {

		Map<String, String> param = (Map<String, String>) redismap
				.get("csrfToken");

		Map<String, String> mp = new HashMap<String, String>();
		mp.put("ID", "4043");
		mp.put("ajaxSubmitType", "post");
		mp.put("ajax_randomcode", "0.9252873924494788");
		mp.put("autoType", "false");
		mp.put("csrfToken", param.get("csrfToken").toString());
		mp.put("eventname", "checkSMSValidation");
		mp.put("pagename", "AjaxSubmit");
		mp.put("partids", "");
		mp.put("service", "ajaxDirect/1/AjaxSubmit/AjaxSubmit/javascript/");
		mp.put("passwrod_smsValidation", login.getPhoneCode());
		CHeader h = new CHeader("http://www.gz.10086.cn/service/fee/xdcx.jsp");
		String url = "http://www.gz.10086.cn/service/app";
		String text = cutil.post(url, h, mp);
		// System.out.println(text); // check 结束
		if (text.contains("ID=4043") || text.contains("ID=4047")) {
			status = 1;
			addTask_2(this);
		} else if (text.contains("击获取获取验证码再")) {
			status = 0;
			errorMsg = "尊敬的客户，请点击获取获取验证码再重新操作。";
		} else {
			status = 0;
			errorMsg = "系统繁忙，重新输入！";
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
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
				getCurrentBill();//本月账单
				getbill();// 历史账单
				break;
			case 2:
				getMyInfo(); // 个人信息
				callHistory(); // 通话记录
				getMessage();// 短信记录
				getFlow();
				//
				break;
			case 3:
				getMyInfo(); // 个人信息
				getbill(); // 历史账单
				callHistory(); // 通话记录
				getMessage();// 查询短信记录
				getFlow();
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	/**
	 * 本月账单
	 */
	private void getCurrentBill() {
		 
			CHeader c = new CHeader("http://www.gz.10086.cn/my/");
			Map<String, String> param_bill = new LinkedHashMap<String, String>();
			String cbill = cutil.post(
					"http://www.gz.10086.cn/pmsV4-web/pms/userInfo.do", c,
					param_bill);
			//{"userInfo":{"UseAge":25,"TotalFee":"2839","BrandId":"261000000004","retcode":"0000","BrandIdDesc":"动感地带","phoneId":"18702401460","CustName":"燕*天"},"msg":1}
			RegexPaserUtil rp1 = new RegexPaserUtil("TotalFee\":\"", "\"",
					cbill, RegexPaserUtil.TEXTEGEXANDNRT);// "TotalFee":"2800","BrandId":"261000000004
			String allPayStr = rp1.getText();
			String allPayStr1 = allPayStr.substring(0,2)+"."+allPayStr.substring(2,4);
			BigDecimal cAllPay = new BigDecimal(allPayStr1); // 套餐及固定费

			List<String> ms_c = getMonth(1, "yyyy-MM");

			String starDate_c = ms_c.get(0).toString();
			String DependCycle = starDate_c + "-01  至今";
			Map map3 = new HashMap();
			map3.put("phone", login.getLoginName());
			map3.put("cTime",StringToDate(starDate_c.replace("-", ""), "yyyyMM"));
			List list1 = mobileTelService.getMobileTelBybc(map3);
			if (list1 == null || list1.size() == 0) {
				MobileTel mobieTel1 = new MobileTel();
				UUID uuid = UUID.randomUUID();
				mobieTel1.setId(uuid.toString());
				mobieTel1.setcTime(StringToDate(starDate_c.replace("-", ""),
						"yyyyMM"));
				mobieTel1.setcName(CUST_NAME);
				mobieTel1.setTeleno(login.getLoginName());
				mobieTel1.setcAllPay(cAllPay);
				//mobieTel1.setTcgdf(tcgdf_c);
				mobieTel1.setDependCycle(DependCycle);
				mobieTel1.setIscm(1);
				mobileTelService.saveMobileTel(mobieTel1);
			} else {
				MobileTel mobieTel1 = new MobileTel();
				UUID uuid = UUID.randomUUID();
				mobieTel1.setId(uuid.toString());
				mobieTel1.setcTime(StringToDate(starDate_c, "yyyyMM"));
				mobieTel1.setcName(CUST_NAME);
				mobieTel1.setTeleno(login.getLoginName());
				mobieTel1.setcAllPay(cAllPay);
				//mobieTel1.setTcgdf(tcgdf_c);
				mobieTel1.setDependCycle(DependCycle);
				mobieTel1.setIscm(1);
				mobileTelService.update(mobieTel1);
			}
	}
	// 月账单
	private boolean getbill() {

		try {
			CHeader h = new CHeader("http://www.gz.10086.cn/my/");
			String text = cutil.get(
					"http://www.gz.10086.cn/service/fee/yzdcx.jsp", h);
			RegexPaserUtil rp = new RegexPaserUtil("var forceLogoutUrl = \"",
					"\";", text, RegexPaserUtil.TEXTEGEXANDNRT);
			String forceLogoutUrl = rp.getText();
			text = cutil
					.get("https://gz.ac.10086.cn/aicas/loginStatus?jsoncallback=jQuery17207738676514626515_"
							+ System.currentTimeMillis()
							+ "&service=http://www.gz.10086.cn/service/cascallback/login&forceLogoutUrl="
							+ forceLogoutUrl
							+ "&_="
							+ System.currentTimeMillis());
			rp = new RegexPaserUtil("\"host\":\"", "\",\"tic", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String host = rp.getText();
			rp = new RegexPaserUtil("ticket\":\"", "\"}]", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String ticket = rp.getText();
			text = cutil
					.get("http://www.gz.10086.cn/service/cascallback/login?ticket="
							+ ticket + "&host=" + host);

			text = cutil.get("http://www.gz.10086.cn/service/fee/yzdcx.jsp", h);

			rp = new RegexPaserUtil("TOKEN : \"", "\"", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String csrfToken = rp.getText();

			rp = new RegexPaserUtil("\" CATALOG_ID=\"", "\" appPath=", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String ID = rp.getText();
			Map<String, String> param = new LinkedHashMap<String, String>();

			List<String> ms = getMonth(7, "yyyyMM");
			ms.remove(0);
			for (String s : ms) {
				String starDate = s;
				param.put("select_month", starDate.toString().trim());
				param.put("ID", ID);
				param.put("ajaxSubmitType", "post");
				param.put("ajax_randomcode", "0.21290666143605397");
				param.put("autoType", "false");
				param.put("csrfToken", csrfToken);
				param.put("eventname", "query");
				param.put("pagename", "fee.QueryMonthBill");
				param.put("partids", "");
				param.put("service",
						"ajaxDirect/1/fee.QueryMonthBill/fee.QueryMonthBill/javascript/");
				h = new CHeader("http://www.gz.10086.cn/service/fee/yzdcx.jsp");
				text = cutil.post("http://www.gz.10086.cn/service/app", h,
						param); // 账单查询
				// System.out.println(text);
				String dependCycle = null;
				BigDecimal tcgdf = new BigDecimal(0); // 套餐及固定费
				BigDecimal yytxf = new BigDecimal(0); // 语音通信费
				BigDecimal tcwdxf = new BigDecimal(0); // 套餐外短彩信费
				BigDecimal cAllPay = new BigDecimal(0);// 本期总费用
				if (text.contains("cus_fee_cycle")) {
					try {
						rp = new RegexPaserUtil("cycle\":\"", "\"", text,
								RegexPaserUtil.TEXTEGEXANDNRT);
						dependCycle = rp.getText();
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						rp = new RegexPaserUtil("cus_fee_sum\":\"", "\"", text,
								RegexPaserUtil.TEXTEGEXANDNRT);
						String tcgdfs = rp.getText();
						try {
							if (tcgdfs != null) {
								tcgdf = new BigDecimal(tcgdfs);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						rp = new RegexPaserUtil("信费\"\\},\\{\"fee\":\"", "\"",
								text, RegexPaserUtil.TEXTEGEXANDNRT);
						String yytxfs = rp.getText();
						try {
							if (yytxfs != null) {
								yytxf = new BigDecimal(yytxfs);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						rp = new RegexPaserUtil("短彩信费\"\\},\\{\"fee\":\"",
								"\"", text, RegexPaserUtil.TEXTEGEXANDNRT);
						String tcwdxfs = rp.getText();
						try {
							if (tcwdxfs != null) {
								tcwdxf = new BigDecimal(tcwdxfs);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					rp = new RegexPaserUtil("fee_total\":\"", "\"", text,
							RegexPaserUtil.TEXTEGEXANDNRT);
					String cAllPays = rp.getText();
					try {
						if (cAllPay != null) {
							cAllPay = new BigDecimal(cAllPays);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Map map2 = new HashMap();
				map2.put("phone", login.getLoginName());
				map2.put("cTime", StringToDate(s, "yyyyMM"));
				List list = mobileTelService.getMobileTelBybc(map2);
				if (list == null || list.size() == 0) {
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					mobieTel.setcTime(StringToDate(s, "yyyyMM"));
					mobieTel.setcName(CUST_NAME);
					mobieTel.setTeleno(login.getLoginName());
					mobieTel.setTcgdf(tcgdf);
					String year = s.substring(0, 4);
					String mouth = s.substring(4, 6);
					mobieTel.setDependCycle(dependCycle);

					mobieTel.setTcwdxf(tcwdxf);
					mobieTel.setcAllPay(cAllPay);
					mobieTel.setTcwyytxf(yytxf);
					String cd = formatDate(new Date());
					mobieTel.setIscm(0);
					mobileTelService.saveMobileTel(mobieTel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return true;
	}

	// 查询通话记录
	public Map<String, Object> callHistory() {
		try {

			CHeader h = new CHeader(
					"http://www.gz.10086.cn/service/fee/xdcx.jsp");
			String text = cutil
					.get("http://www.gz.10086.cn/service/app?service=page/fee.QueryDetailBill&listener=initPage&ID=4043",
							h);
			RegexPaserUtil rp = new RegexPaserUtil("TOKEN : \"", "\"", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String billcsrfToken = rp.getText();
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			MobileDetail detail = new MobileDetail();
			detail.setPhone(login.getLoginName());
			detail = mobileDetailService.getMaxTime(detail);
			boolean b = true;
			String tradeAddr = null;
			String tradeWay = null;
			String recevierPhone = null;
			int tradeTime = 0;
			String tradeType = null;
			BigDecimal onlinePay = null;
			String taocan = null;
			String cTime = null;
			int iscm = 0;
			int iscm1 = 5;

			Map<String, String> param = new LinkedHashMap<String, String>();
			for (int k = 0; k < ms.size(); k++) {
				String starDate = (String) ms.get(k);
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday = TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(formatDateMouth(mouth)));
				// 2014-09-01
				String eday = TimeUtil.getLastDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(formatDateMouth(mouth)));
				String mfday = fday.substring(0, 7);// 2014-06
				h = new CHeader(
						"http://www.gz.10086.cn/service/app?service=page/fee.QueryDetailBill&listener=initPage&ID=4043");

				// 分页
				// detailpwdQueryForm
				// from 2014-09-01
				// isPage 1
				// monthQuery 2014-08
				// queryMode queryMonth
				// queryType 1
				// searchResultInfo
				// to 2014-09-25

				param.put("detailpwdQueryForm", "");
				param.put("from", fday);
				param.put("isPage", "1");
				param.put("monthQuery", mfday);
				param.put("queryMode", "queryMonth");
				param.put("queryType", "1");
				param.put("searchResultInfo", "");
				param.put("to", eday);
				String url = "http://www.gz.10086.cn/service/app?service=ajaxDirect/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
						+ billcsrfToken
						+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode=0.9538554840262938&autoType=false";
				text = cutil.post(url, h, param);

				// 显示全部
				// detailpwdQueryForm
				// from 2014-09-01
				// isPage 0
				// monthQuery 2014-09
				// queryMode queryMonth
				// queryType 1
				// searchResultInfo
				// to 2014-09-25
				param.put("detailpwdQueryForm", "");
				param.put("from", fday);
				param.put("isPage", "0");
				param.put("monthQuery", mfday);
				param.put("queryMode", "queryMonth");
				param.put("queryType", "1");
				param.put("searchResultInfo", "");
				param.put("to", eday);
				url = "http://www.gz.10086.cn/service/app?service=ajaxDirect/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
						+ billcsrfToken
						+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode=0.9538554840262938&autoType=false";
				text = cutil.post(url, h, param);
				try {
					// System.out.println(text);
					rp = new RegexPaserUtil("dataList\":", "\\]\\]\\]", text,
							RegexPaserUtil.TEXTEGEXANDNRT);
					text = rp.getText() + "]";
					// System.out.println(text);
					if (text.length() > 30) {
						JSONArray json = null;
						json = new JSONArray(text);
						if(json.length()>=3) { 
						for (int i = 1; i < json.length() - 1; i++) {
						 text = json.getString(i);
						 if(text.length()>40) {
							try {
								rp = new RegexPaserUtil("\\]", "\",",
										text, RegexPaserUtil.TEXTEGEXANDNRT);
								tradeAddr = rp.getText();
								
							} catch (Exception e6) {
								// TODO Auto-generated catch block
								e6.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("CALLTYPE",
										"\",", text,
										RegexPaserUtil.TEXTEGEXANDNRT);
								String tradeWayStr = rp.getText();
								tradeWay = tradeWayStr.substring(tradeWayStr.length()-2,tradeWayStr.length());
								
							} catch (Exception e5) {
								// TODO Auto-generated catch block
								e5.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("PPNUMBER\":\"", "\",",
										text, RegexPaserUtil.TEXTEGEXANDNRT);
								recevierPhone = rp.getText();
							} catch (Exception e4) {
								// TODO Auto-generated catch block
								e4.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("DURATION\":\"", "\",",
										text, RegexPaserUtil.TEXTEGEXANDNRT);
								String tradeTime1 = rp.getText();
								tradeTime = StringUtil.flowTimeFormat(tradeTime1).intValue();
							} catch (Exception e3) {
								// TODO Auto-generated catch block
								e3.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil(
										"TOLLTYPE\":\"\\[104\\]", "\",", text,
										RegexPaserUtil.TEXTEGEXANDNRT);
								tradeType = rp.getText();
							} catch (Exception e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("CHARGE\":\"￥", "\",",
										text, RegexPaserUtil.TEXTEGEXANDNRT);
								String onlinePay1 = rp.getText();
								onlinePay = new BigDecimal(onlinePay1);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("STARTTIME\":\"",
										"\",", text,
										RegexPaserUtil.TEXTEGEXANDNRT);
								cTime = year + "-" + rp.getText();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						

							MobileDetail mobileDetail = new MobileDetail();
							UUID uuid = UUID.randomUUID();
							mobileDetail.setId(uuid.toString());
							mobileDetail.setcTime(DateUtils.StringToDate(cTime,
									"yyyy-MM-dd HH:mm:ss"));
							if (detail != null
									&& mobileDetail.getcTime().getTime() <= detail
											.getcTime().getTime()) {
								b = false;
								break;
							}
							mobileDetail.setTradeAddr(tradeAddr);
							mobileDetail.setTradeWay(tradeWay);
							mobileDetail.setRecevierPhone(recevierPhone);
							mobileDetail.setTradeTime(tradeTime);
							mobileDetail.setTradeType(tradeType);
							mobileDetail.setTaocan(taocan);
							mobileDetail.setOnlinePay(onlinePay);
							mobileDetail.setPhone(login.getLoginName());
							if(iscm1==0) {  
								iscm = 1;
							}
							mobileDetail.setIscm(iscm);
							mobileDetailService.saveMobileDetail(mobileDetail);
						}//if(text>40)
					   }//for
					  }
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <p>流量记录以及流量账单存储
	 * 已经针对每一条详单和账单写了try-catch，不会因为一条特殊信息使得全部错误
	 * 每一种错误进行了log.error()响应
	 * @author Pat.Liu
	 * */
	public Map<String, Object> getFlow() {
		try {
			CHeader h = new CHeader(
					"http://www.gz.10086.cn/service/fee/xdcx.jsp");
			String text = cutil
					.get("http://www.gz.10086.cn/service/app?service=page/fee.QueryDetailBill&listener=initPage&ID=4043",
							h);
			RegexPaserUtil rp = new RegexPaserUtil("TOKEN : \"", "\"", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String billcsrfToken = rp.getText();
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			List<MobileOnlineList> onlineList = new ArrayList<MobileOnlineList>();// saveMobileOnlineList()
			List<MobileOnlineBill> onlineBill = new ArrayList<MobileOnlineBill>();// saveMobileOnlineBill()

			boolean b = true;
			// 感觉没啥用

			Map<String, String> param = new LinkedHashMap<String, String>();
			for (int k = ms.size()-1; k >=0; k--) {
				String starDate = (String) ms.get(k);
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday = TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(formatDateMouth(mouth)));
				// 2014-09-01
				String eday = TimeUtil.getLastDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(formatDateMouth(mouth)));
				String mfday = fday.substring(0, 7);// 2014-06
				h = new CHeader(
						"http://www.gz.10086.cn/service/app?service=page/fee.QueryDetailBill&listener=initPage&ID=4043");

				// 分页
				// detailpwdQueryForm
				// from 2014-09-01
				// isPage 1
				// monthQuery 2014-08
				// queryMode queryMonth
				// queryType 2
				// searchResultInfo
				// to 2014-09-25

				param.put("detailpwdQueryForm", "");
				param.put("from", fday);
				param.put("isPage", "1");
				param.put("monthQuery", mfday);
				param.put("queryMode", "queryMonth");
				param.put("queryType", "2");
				param.put("searchResultInfo", "");
				param.put("to", eday);
				String url = "http://www.gz.10086.cn/service/app?service=ajaxDirect/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
						+ billcsrfToken
						+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode=0.9538554840262938&autoType=false";
				text = cutil.post(url, h, param);

				// 显示全部
				// detailpwdQueryForm
				// from 2014-09-01
				// isPage 0
				// monthQuery 2014-09
				// queryMode queryMonth
				// queryType 2
				// searchResultInfo
				// to 2014-09-25
				param.put("detailpwdQueryForm", "");
				param.put("from", fday);
				param.put("isPage", "0");// 全部显示
				param.put("monthQuery", mfday);
				param.put("queryMode", "queryMonth");
				param.put("queryType", "2");
				param.put("searchResultInfo", "");
				param.put("to", eday);
				url = "http://www.gz.10086.cn/service/app?service=ajaxDirect/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
						+ billcsrfToken
						+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode=0.9538554840262938&autoType=false";
				text = cutil.post(url, h, param);

				MobileOnlineBill newBill = new MobileOnlineBill();
				Double allCharge=0.0;
				
				try {
					// System.out.println(text);
					Document doc = Jsoup.parse(text);
					Element ele = doc.getElementById("dataset");
					JSONArray json0 = new JSONArray(ele.text());
					if (json0.length() > 1)
						log.error("GZYidong flow-list has changed!");
					if (json0.length() < 1)
						continue;
					JSONObject json1 = json0.getJSONObject(0);
					if(!json1.has("FreeFlow"))
						continue;
					try {
						// 月账单
						Double freeflow = StringUtil.flowFormat(json1
								.getString("FreeFlow"))
								+ StringUtil.flowFormat(json1
										.getString("CmwapFreeFlow"))
								+ StringUtil.flowFormat(json1
										.getString("CmnetFreeFlow"));
						Double chargeflow = StringUtil.flowFormat(json1
								.getString("ChargeFlow"))
								+ StringUtil.flowFormat(json1
										.getString("CmwapChargeFlow"))
								+ StringUtil.flowFormat(json1
										.getString("CmnetChargeFlow"));
						Double totalflow = StringUtil.flowFormat(json1
								.getString("TotalFlow"))
								+ StringUtil.flowFormat(json1
										.getString("CmwapTotalFlow"))
								+ StringUtil.flowFormat(json1
										.getString("CmnetTotalFlow"));
						newBill.setChargeFlow(chargeflow.longValue());
						newBill.setFreeFlow(freeflow.longValue());
						newBill.setTotalFlow(totalflow.longValue());
						newBill.setPhone(login.getLoginName());
						newBill.setMonthly(DateUtils.StringToDate(fday
								+ " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
					} catch (Exception e) {
						log.error("GZYidong flow month list error", e);
					}
					if(!json1.has("dataList"))
						continue;
					JSONArray json2 = json1.getJSONArray("dataList");
					for (int k1 = 0; k1 < json2.length(); k1++) {
						try {
							JSONObject jsonData = json2.getJSONObject(k1);
							if (jsonData.has("isDayData")
									&& jsonData.getBoolean("isDayData") == true) {
								MobileOnlineList newList = new MobileOnlineList();
								newList.setCheapService(jsonData
										.getString("VALIDRATEPRODID"));
								newList.setOnlineType(jsonData
										.getString("DRTYPE"));
								newList.setTradeAddr(jsonData
										.getString("HPLMN"));
								if (jsonData.getString("DATAFLOWTOTAL") != "") {
									Double flow = StringUtil
											.flowFormat(jsonData
													.getString("DATAFLOWTOTAL"));
									newList.setTotalFlow(flow.longValue());
								}
								if (jsonData.getString("CHARGE") != ""
										&& jsonData.getString("CHARGE")
												.length() > 2) {
									String fee = jsonData.getString("CHARGE")
											.replaceAll("￥", "");
									newList.setCommunicationFees(new BigDecimal(
											fee));
									if(fee=="")
										fee="0.00";
									allCharge+=Double.parseDouble(fee);
								}
								if (jsonData.getString("STARTTIME").length() > 10)
									newList.setcTime(DateUtils.StringToDate(
											mfday.substring(0, 5)
													+ jsonData
															.getString("STARTTIME"),
											"yyyy-MM-dd HH:mm:ss"));
								if (jsonData.getString("DURATION") != "") {
									int time = TimeUtil.timetoint(jsonData
											.getString("DURATION"));
									newList.setOnlineTime((long) time);
								}
								newList.setPhone(login.getLoginName());
								onlineList.add(newList);
							}
						} catch (Exception e) {
							log.error(
									"GZYidong flow dataList:"
											+ json2.getJSONObject(k1), e);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("GZYidong:flow-list error:", e);
					e.printStackTrace();
				}
				newBill.setTrafficCharges(new BigDecimal(allCharge));
				onlineBill.add(newBill);
				//月循环结束
			}
			// 存储流量详单和月账单
			saveMobileOnlineBill(onlineBill);
			saveMobileOnlineList(onlineList);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("GZYidong flow-post error", e);
			e.printStackTrace();
		}
		return map;
	}

	// 短信记录
	public Map<String, Object> getMessage() {
		try {
			CHeader h = new CHeader(
					"http://www.gz.10086.cn/service/fee/xdcx.jsp");
			String text = cutil
					.get("http://www.gz.10086.cn/service/app?service=page/fee.QueryDetailBill&listener=initPage&ID=4043",
							h);
			RegexPaserUtil rp = new RegexPaserUtil("TOKEN : \"", "\"", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String messagecsrfToken = rp.getText();
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			MobileMessage mobilemessage = new MobileMessage();
			mobilemessage.setPhone(login.getLoginName());
			mobilemessage = mobileMessageService.getMaxSentTime(login
					.getLoginName());
			boolean b = true;

			String sentTime = null;
			String sentAddr = null;
			String recevierPhone = null;
			String tradeWay = null;
			BigDecimal allPay = null;

			Map<String, String> param = new LinkedHashMap<String, String>();
			for (int k = 0; k < ms.size(); k++) {
				String starDate = (String) ms.get(k);
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday = TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(formatDateMouth(mouth)));
				// 2014-09-01
				String eday = TimeUtil.getLastDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(formatDateMouth(mouth)));
				String mfday = fday.substring(0, 7);// 2014-06
				String meday = eday.replace("-", "");
				h = new CHeader(
						"http://www.gz.10086.cn/service/app?service=page/fee.QueryDetailBill&listener=initPage&ID=4043");

				// 分页
				// detailpwdQueryForm
				// from 2014-09-01
				// isPage 1
				// monthQuery 2014-09
				// queryMode queryMonth
				// queryType 3
				// searchResultInfo
				// to 2014-09-25
				param.put("detailpwdQueryForm", "");
				param.put("from", fday);
				param.put("isPage", "1");
				param.put("monthQuery", mfday);
				param.put("queryMode", "queryMonth");
				param.put("queryType", "3");
				param.put("searchResultInfo", "");
				param.put("to", eday);
				text = cutil
						.post("http://www.gz.10086.cn/service/app?service=ajaxDirect/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
								+ messagecsrfToken
								+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode=0.3538754767644128&autoType=false",
								h, param);

				// 显示全部
				// detailpwdQueryForm
				// from 2014-09-01
				// isPage 0
				// monthQuery 2014-09
				// pagination_iPage
				// pagination_inputPage
				// pagination_linkType
				// queryMode queryMonth
				// queryType 3
				// searchResultInfo
				// to 2014-09-25
				param.put("detailpwdQueryForm", "");
				param.put("from", fday);
				param.put("isPage", "0");
				param.put("monthQuery", mfday);
				param.put("pagination_iPage", "");
				param.put("pagination_linkType", "");
				param.put("queryMode", "queryMonth");
				param.put("queryType", "3");
				param.put("searchResultInfo", "");
				param.put("to", eday);
				text = cutil
						.post("http://www.gz.10086.cn/service/app?service=ajaxDirect/1/fee.QueryDetailBill/fee.QueryDetailBill/javascript/refreshSearchResult&pagename=fee.QueryDetailBill&eventname=query&&ID=4043&csrfToken="
								+ messagecsrfToken
								+ "&partids=refreshSearchResult&ajaxSubmitType=post&ajax_randomcode=0.3538754767644128&autoType=false",
								h, param);
				try {
					// System.out.println(text);
					rp = new RegexPaserUtil("dataList\":", "\\]\\]\\]", text,
							RegexPaserUtil.TEXTEGEXANDNRT);
					text = rp.getText() + "]";
					if (text.length() > 30) {
						JSONArray json = null;
						json = new JSONArray(text);
						for (int i = 1; i < json.length() - 1; i++) {
							text = json.getString(i);

							try {
								rp = new RegexPaserUtil("STARTTIME\":\"",
										"\",", text,
										RegexPaserUtil.TEXTEGEXANDNRT);
								sentTime = year + "-" + rp.getText();
							} catch (Exception e4) {
								// TODO Auto-generated catch block
								e4.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("N\":\"\\[851\\]",
										"\",", text,
										RegexPaserUtil.TEXTEGEXANDNRT);
								sentAddr = rp.getText();
							} catch (Exception e3) {
								// TODO Auto-generated catch block
								e3.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("OPPNUMBER\":\"",
										"\",", text,
										RegexPaserUtil.TEXTEGEXANDNRT);
								recevierPhone = rp.getText();
							} catch (Exception e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("CALLTYPE\":\"\\[1\\]",
										"\",", text,
										RegexPaserUtil.TEXTEGEXANDNRT);
								tradeWay = rp.getText();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							try {
								rp = new RegexPaserUtil("CHARGE\":\"￥", "\",",
										text, RegexPaserUtil.TEXTEGEXANDNRT);
								String allPay1 = rp.getText();
								allPay = new BigDecimal(allPay1);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							MobileMessage message1 = new MobileMessage();
							UUID uuid = UUID.randomUUID();
							message1.setId(uuid.toString());
							message1.setSentTime(DateUtils.StringToDate(
									sentTime, "yyyy-MM-dd HH:mm:ss"));

							if (mobilemessage != null
									&& message1.getSentTime().getTime() <= mobilemessage
											.getSentTime().getTime()) {
								b = false;
								break;
							}
							message1.setSentAddr(sentAddr);
							message1.setRecevierPhone(recevierPhone);
							message1.setTradeWay(tradeWay);
							message1.setAllPay(allPay);
							message1.setPhone(login.getLoginName());
							mobileMessageService.save(message1);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}
//
	private Map<String, Object> getMyInfo() {
		try {
			CHeader h = new CHeader(
					"http://www.gz.10086.cn/service/operate/nav.jsp?service=page/personalbusiness.QueryUserInfo&listener=initPage&csrfToken="
							+ csrfToken + "&ID=4073");
			String text = cutil
					.get("http://www.gz.10086.cn/service/app?service=page/personalbusiness.QueryUserInfo&listener=initPage&ID=4073",
							h);
			StringEscapeUtils.escapeHtml4(text);
			String loginName = login.getLoginName();
			String realName = null;
			String addr = null;
			String memberType = null;// 会员类型
			String memberLevel = null;// 会员等级
			String sex = "";
			if (text != null) {
				Document doc = Jsoup.parse(text);
				Element element = doc.select("table").first();
				//System.out.println(element.toString());
				try {
					sex = element.select("tbody>tr:eq(1)>td:eq(3)").text();
					realName = element.select("tbody>tr:eq(1)>td:eq(1)").text();
					addr = element.select("tbody>tr:eq(6)>td:eq(3)").text();
					memberType = element.select("tbody>tr:eq(3)>td:eq(1)")
							.text();
					memberLevel = element.select("tbody>tr:eq(4)>td:eq(1)")
							.text();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Map<String, String> parmap = new HashMap<String, String>();
			parmap.put("parentId", currentUser);
			parmap.put("loginName", loginName);
			parmap.put("usersource", Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(parmap);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(realName);
				user.setAddr(addr);
				user.setSex(sex);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setMemberLevel(memberLevel);
				user.setMemberType(memberType);
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());

				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(realName);
				user.setAddr(addr);
				user.setSex(sex);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setMemberLevel(memberLevel);
				user.setMemberType(memberType);
				userService.saveUser(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	private BigDecimal getYuE() {
		BigDecimal phoneremain = new BigDecimal("0.00");
		try {
			phoneremain = new BigDecimal(balance);
			BigDecimal phoneremain1 = new BigDecimal("100");
			phoneremain = phoneremain.divide(phoneremain1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phoneremain;
	}

	private List<String> getMonth(int num, String format) {
		List<String> objectTmp = new ArrayList<String>();
		java.text.DateFormat format2 = new java.text.SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		for (int i = 0; i < num; i++) {
			c.add(Calendar.MONTH, -1);
			Date date = c.getTime();
			String date2 = format2.format(date);
			objectTmp.add(date2);
		}
		return objectTmp;
	}

	private int getDaysOfMonth(String s) {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMM"); // 如果写成年月日的形式的话，要写小d，如："yyyy/MM/dd"
		try {
			rightNow.setTime(simpleDate.parse(s)); // 要计算你想要的月份，改变这里即可
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	private Date StringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	private String formatDateMouth(String m) {
		if (m != null && m.length() == 2) {
			String fix1 = m.substring(0, 1);
			String fix2 = m.substring(1, 2);
			if (fix1.equals("0")) {
				return fix2;
			}
			return m;
		}
		return null;
	}

	private String formatDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(d);
	}

	/**
	 * 在第一次登录过程中返回的错误代码
	 * 
	 * @param codeValue
	 * @return
	 */
	private String getErrorText(String codeValue) {
		if (codeValue != null) {
			codeValue = codeValue.trim();
			if (codeValue.contains("密码被锁定")) {
				return "对不起，为保护您的信息安全，密码被锁定，请明天再试，或持身份证到营业厅办理立即解锁。";
			} else if (codeValue.contains("服务密码1次")) {
				return "服务密码错误，您今天已输入错误服务密码1次，还可以输入4次。";
			} else if (codeValue.contains("服务密码2次")) {
				return "服务密码错误，您今天已输入错误服务密码2次，还可以输入3次。";
			} else if (codeValue.contains("服务密码3次")) {
				return "服务密码错误，您今天已输入错误服务密码3次，还可以输入2次。";
			} else if (codeValue.contains("服务密码4次")) {
				return "服务密码错误，您今天已输入错误服务密码4次，还可以输入1次。";
			} else {
				return null;
			}

		}
		return null;
	}
}
