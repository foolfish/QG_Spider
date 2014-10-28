package com.lkb.thirdUtil.yd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
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
import com.lkb.bean.SimpleObject;
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
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
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
public class HuNanYidong extends BaseInfoMobile {
	private List<String> flowBillHtmls;

	public HuNanYidong(Login login) {
		super(login);
		this.userSource = Constant.YIDONG;
	}

	public HuNanYidong(Login login, IWarningService warningService,
			String currentUser) {
		super(login, warningService, ConstantNum.comm_hun_yidong, currentUser);
		this.userSource = Constant.YIDONG;
	}

	public HuNanYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_hun_yidong, currentUser);
		this.userSource = Constant.YIDONG;
	}

	/**
	 * @return 初始化,并获取验证码
	 */
	public void init() {
		// https://www.hn.10086.cn/login.jsp
		String text = cutil.post(
				"https://www.hn.10086.cn/ajax/getAttachCodeKey.jsp", null);
		JSONObject json;
		try {
			json = new JSONObject(text);
			String serailNo = json.get("serailNo").toString();
			map.put("serailNo", serailNo);
			setImgUrl("https://www.hn.10086.cn/attachCode?serailNo=" + serailNo
					+ "&" + System.currentTimeMillis());
			setInit();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public Map<String, Object> login() {
		String text = login1();
		if (text != null && text.contains("index.jsp?recommendGiftPop=true")) {// 验证第一步登陆成功
			text = login2();
			if (text.contains("当前余额")) {
				RegexPaserUtil rp = new RegexPaserUtil(
						"当前余额：<span class=\"f_red fWeight_b fSize_16\">",
						"</span>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String balance = rp.getText();
				map.put("balance", balance);
				// login3();
				loginsuccess();
				addTask(this);
			}
			// addTask_1(this);
			// sendPhoneDynamicsCode();
		} else {
			CHeader header = new CHeader(
					"https://www.hn.10086.cn/login/dologin.jsp");
			text = cutil
					.get("https://www.hn.10086.cn/login.jsp?mode=1", header);
			// system.out.println(text);
			RegexPaserUtil rp = new RegexPaserUtil("showTip", ";", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String errorMeg = rp.getText();
			// //system.out.println(errorMeg);
			errorMsg = errorMeg;
		}
		return map;
	}

	public Map<String, Object> getTestData() {
		// userKey,telKey,detailKey,messKey

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userKey", saveYuE());
		map.put("telKey", saveMonthTel());
		map.put("detailKey", saveDetailTel());
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
				saveDetailTel(); // 通话记录
				break;
			case 3:
				saveYuE(); // 个人信息
				saveMonthTel(); // 历史账单
				saveDetailTel(); // 通话记录
				saveMessage();// 查询短信记录
				saveFlowBill();// 流量账单
				saveFlowList();// 流量详单
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	private String login1() {
		// 获取部分登录需要的参数
		String preUrl = "https://www.hn.10086.cn/login.jsp";
		getIndexParam(preUrl, null, "loginForm");
		String url = "https://www.hn.10086.cn/login/dologin.jsp";
		CHeader header = new CHeader("https://www.hn.10086.cn/login.jsp");
		// mobileNum=18373914055
		// &servicePWD=168168
		// &randomNum=
		// &attachCode=9865
		// &url=%2Fmy%2Faccount%2Findex.jsp
		// &continue=%2Fmy%2Faccount%2Findex.jsp
		// &mode=1
		// &loginTokenId=92f9823a858dd9975e6c9d9e1fd15e6e1ff1e20477cf7f7d
		// &serailNo=20140910101332324608
		// &needAttachCode=1
		// &struts.token.name=token
		// &token=HOSSIYHR3FPI6TODNDKDN50OBV64XJPM
		// &mac=
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobileNum", login.getLoginName());
		map.put("servicePWD", login.getPassword());
		map.put("randomNum", "");
		map.put("attachCode", login.getAuthcode());
		map.put("url", "/my/account/index.jsp");
		map.put("continue", "/my/account/index.jsp");
		map.put("mode", "1");
		map.put("loginTokenId", this.map.get("loginTokenId").toString());//
		map.put("serailNo", this.map.get("serailNo").toString());
		map.put("needAttachCode", "1");
		map.put("struts.token.name", "token");
		map.put("token", this.map.get("token").toString());//
		map.put("mac", "");
		String text = cutil.post(url, header, map);
		// system.out.println(text);
		return text;
	}

	private String login2() {
		CHeader cHeader = new CHeader(
				"https://www.hn.10086.cn/login/dologin.jsp");
		String text = cutil
				.get("https://www.hn.10086.cn/my/account/index.jsp?recommendGiftPop=true",
						cHeader);
		// system.out.println(text);
		return text;
	}

	/*
	 * private String login3(){ CHeader cHeader = new
	 * CHeader("https://www.hn.10086.cn/my/account/index.jsp?recommendGiftPop=true"
	 * ); String text =
	 * cutil.get("https://www.hn.10086.cn/ajax/fee/lifePayInfo.jsp?itemType=e",
	 * cHeader); //system.out.println(text); Map<String,String> map = new
	 * HashMap<String,String>(); map.put("area", "0739"); text = cutil.post(
	 * "https://www.hn.10086.cn/Shopping/mall/phone/getRecommentMobileNum.action"
	 * , cHeader, map); //system.out.println(text); return text; }
	 */
	private boolean saveYuE() {
		try {
			String loginName = login.getLoginName();
			Map<String, String> parmap = new HashMap<String, String>(3);
			parmap.put("parentId", currentUser);
			parmap.put("loginName", loginName);
			parmap.put("usersource", Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(parmap);
			// https://www.hn.10086.cn/service/customerService/changeuserinfo.jsp
			String text = cutil
					.get("https://www.hn.10086.cn/service/customerService/changeuserinfo.jsp");
			// system.out.println(text);
			RegexPaserUtil rp = new RegexPaserUtil("resultObject\":",
					",\"resultRecords", text, RegexPaserUtil.TEXTEGEXANDNRT);
			String userdata = rp.getText();
			JSONObject json;
			String idcard = "";
			String realName = "";
			String email = "";
			String OPENDATE = "";
			String HOMEADDRESS = "";
			/*
			 * "PASSPORTADDRESS":"中国重庆市长寿区112","SERVID":"3914031924828582",
			 * "LINKMANLINK"
			 * :"","VIPNUM":"","WORKADDRESS":"中国重庆市长寿区112","MANAGER_MOBILENUM"
			 * :""
			 * ,"SIMCARDNO":"898602A7182383977128","USRTYPE":"现费用户","LINKHOMEPHONE"
			 * :"","ACCOUNTID":"3914031983839140","PASSPORTTYPE":"本地身份证",
			 * "HOMEADDRESS"
			 * :"","OPENMODE":"6","VIP_TYPE_CODE":"","BOOKDESTROYTIME"
			 * :"","QUALITYTYPE"
			 * :"动感地带","INDEPARTNAME":"大祥区管理","TRANSDATA7":"","PRODUCTID"
			 * :"39188100"
			 * ,"ASSTATUSCODE":"39188100","BANK":"","SPECIALFACTOR":"-1"
			 * ,"ERROR_RETURN_CODE"
			 * :"","RESULT_INFO":"调用成功","ERROR_RETURN_INFO":""
			 * ,"BUSI_RETURN_INFO"
			 * :"ok","OPENSTAFFNAME":"ESYS9002","JOBTYPE":"","SERIALNUMBER"
			 * :"18373914055"
			 * ,"PREPAY_TAG":"1","SERVICEMODE":"","USRSTATE":"开通","USRPID"
			 * :"510225********041*"
			 * ,"TRANSDATA4":"0","PAYNAME":"刘正良","USRNAME":"刘*良"
			 * ,"TR_PAYMODECODE"
			 * :"0","HOMEPOSTCODE":"168168","RESULT_CODE":"0","OPENDATE"
			 * :"2014-03-19 16:21:19"
			 * ,"CARDQUALITY":"动感地带","TRANSDATA3":"0739","OPENDEPARTNAME"
			 * :"肖芳志","BUSI_RETURN_CODE"
			 * :"0","AGENT":"邵府街代理店","TRANSDATA5":"","OPDATE"
			 * :"","TRANSDATA8":"",
			 * "BANKACCOUNTNO":"","USRSTATECODESET":"0","LINKMAN"
			 * :"*","PAYMODE":"现金"
			 * ,"WORKNAME":"","ERROR_TYPE":"","MAXMONEY":"0","BIRTHDAY"
			 * :"1963-05-28 00:00:00"
			 * ,"PASSPORTTYPECODE":"0","TRANSDATA9":"邵阳","USRTYPECODE"
			 * :"0","EMAILADDRESS"
			 * :"","TRANSDATA6":"","TRANSDATA1":"N","CALLTIME"
			 * :"","CITYNAME":"城区营销中心"
			 * ,"FAXNO":"","QUALITYTYPECODE":"G010","USRPIDENDDATE"
			 * :"","TRANSDATA2":"N","SCORE":"39","SEXNAME":"男","CITYCODE":"E391"
			 */
			try {
				json = new JSONObject(userdata);
				idcard = json.get("USRPID").toString();
				realName = json.get("PAYNAME").toString();
				map.put("realName", realName);
				email = json.get("EMAILADDRESS").toString();
				OPENDATE = json.get("OPENDATE").toString();
				HOMEADDRESS = json.get("HOMEADDRESS").toString();

			} catch (JSONException e) {
				e.printStackTrace();
			}
			BigDecimal balance = new BigDecimal(0);
			try {
				balance = new BigDecimal(map.get("balance").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(realName);
				user.setIdcard(idcard);
				user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(balance);
				user.setEmail(email);
				user.setRegisterDate(DateUtils.StringToDate(OPENDATE,
						"yyyy-MM-dd HH:mm:ss"));
				user.setAddr(HOMEADDRESS);
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				// String s = redismap.get("realName").toString();
				// //system.out.println(s);
				user.setRealName(realName);
				user.setIdcard(idcard);
				user.setAddr("");
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(balance);
				user.setEmail(email);
				user.setRegisterDate(DateUtils.StringToDate(OPENDATE,
						"yyyy-MM-dd HH:mm:ss"));
				user.setAddr(HOMEADDRESS);
				userService.saveUser(user);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUNYD_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

		return false;
	}

	private boolean saveMonthTel() {
		String preUrl = "https://www.hn.10086.cn/service/fee/monthBill.jsp";
		getIndexParam(preUrl, null, "serviceForm");

		/*
		 * String url_pre =
		 * "https://www.hn.10086.cn/service/fee/monthBillResult.jsp";
		 * Map<String, String> param_pre = new HashMap<String, String>();
		 * param_pre.put("busiId", "monthBill11"); param_pre.put("operation",
		 * "query"); param_pre.put("token", map.get("token").toString());
		 * param_pre.put("Submit", ""); param_pre.put("struts.token.name",
		 * "token"); CHeader header_pre = new
		 * CHeader("https://www.hn.10086.cn/service/fee/monthBill.jsp"); String
		 * url_pre2 = "https://www.hn.10086.cn/jspLog/JSServer/pageDate.jsp?";
		 * Map<String, String> param_pre2 = new HashMap<String, String>();
		 */
		CHeader header = new CHeader(
				"https://www.hn.10086.cn/service/fee/monthBillResult.jsp");
		String url = "https://www.hn.10086.cn/ajax/billservice/monthBillResult.jsp";
		Map<String, String> param = new HashMap<String, String>();
		param.put("busiId", "monthBill11");
		param.put("operation", "query");
		param.put("token", map.get("token").toString());
		param.put("zqFlag", "null");
		param.put("r", "Fri Sep 12 10:24:51 UTC 0800 2014");
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			for (String s : ms) {
				/* param_pre.put("startDate", s); */
				param.put("startDate", s);
				/*
				 * String text = cutil.post(url_pre,header_pre, param_pre); text
				 * = cutil.post(url_pre2,header, param_pre2);
				 */
				String text = cutil.post(url, header, param);
				if (text == null) {
					continue;
				}
				// system.out.println(text);
				parseMonthTel(text, s);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUNYD_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}

		return true;

	}

	private boolean saveDetailTel() {

		CHeader header = new CHeader(
				"https://www.hn.10086.cn/service/fee/detailBillInfo.jsp");
		String DetailTelUrl = "https://www.hn.10086.cn/ajax/billservice/detailBillInfo.jsp";
		try {
			int x = 0;
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			Map<String, String> param = new HashMap<String, String>();
			param.put("busiId", "detailBill11");
			param.put("detailBillPwd", "undefined");
			param.put("detailType", "1");
			param.put("endDate", "01");
			param.put("operation", "query");
			param.put("r", "Wed Sep 10 2014 20:31:20 GMT 0800");
			param.put("startDate", "01");
			param.put("token", map.get("token").toString());
			for (String s : ms) {
				param.put("month", s);
				/*
				 * String text =cutil.post(url_pre,header_pre,param_pre); text =
				 * cutil.post(url_pre2,header,param_pre2);
				 */
				String text = cutil.post(DetailTelUrl, header, param);
				// system.out.println(text);
				parseDetailTel(text, x);
				x++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUNYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}
		return true;
	}

	public void saveMessage() {
		CHeader header = new CHeader(
				"https://www.hn.10086.cn/service/fee/detailBillInfo.jsp");
		String messageUrl = "https://www.hn.10086.cn/ajax/billservice/detailBillInfo.jsp";
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");

			Map<String, String> param = new HashMap<String, String>();
			param.put("busiId", "detailBill11");
			param.put("detailBillPwd", "undefined");
			param.put("detailType", "2");
			param.put("operation", "query");
			param.put("r", "Wed Sep 10 2014 20:31:20 GMT 0800");
			param.put("startDate", "01");
			param.put("token", map.get("token").toString());
			int size = ms.size();
			for (int i = size - 1; i >= 0; i--) {
				int endData = DateUtils.getDayOfMonth(DateUtils.StringToDate(
						ms.get(i), "yyyyMM"));
				param.put("month", ms.get(i));
				param.put("endDate", "" + endData);
				String text = cutil.post(messageUrl, header, param);
				parseMessage(text);
			}

		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HUNYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			// return false;
		}
		// return true;
	}

	private void parseMonthTel(String text, String month) {
		if (text.contains("费用项目")) {
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
					"<table width=\"271\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">",
					"</table>", text, RegexPaserUtil.TEXTEGEXANDNRT);
			String base_table = rp1.getText();
			if (base_table == null || base_table.equals("")) {
				return;
			}
			String tableString = "<table>" + base_table + "</table>";
			Document doc6 = Jsoup.parse(tableString);
			// //system.out.println(doc6);
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
			for (int i = 2; i < trs.size(); i++) {
				Element tr = trs.get(i);
				int a = tr.select("td").size();
				String trStr = tr.select("td").get(a > 1 ? 1 : 0).text()
						.toString();

				if (trStr == null || trStr.equals("")) {
					trStr = tr.select("td").get(0).toString();
				}
				if (trStr.contains("套餐及固定费")) {
					String tcgdfs = tr.select("td").get(2).text()
							.replace("￥", "").replaceAll("\\s*", "");
					try {
						if (tcgdfs != null) {
							tcgdf = new BigDecimal(tcgdfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("语音通信费")) {
					String yytxfs = tr.select("td").get(2).text()
							.replace("￥", "").replaceAll("\\s*", "");
					try {
						if (yytxfs != null) {
							yytxf = new BigDecimal(yytxfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (trStr.contains("短信")) {
					String tcwdxfs = tr.select("td").get(2).text()
							.replace("￥", "").replaceAll("\\s*", "");
					try {
						if (tcwdxfs != null) {
							tcwdxf = new BigDecimal(tcwdxfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("上网费")) {
					String tcwswfs = tr.select("td").get(2).text()
							.replace("￥", "").replaceAll("\\s*", "");
					try {
						if (tcwswfs != null) {
							tcwswf = new BigDecimal(tcwswfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("增值业务费")) {
					String zyzzywfys = tr.select("td").get(2).text()
							.replace("￥", "").replaceAll("\\s*", "");
					try {
						if (zyzzywfys != null) {
							zyzzywfy = new BigDecimal(zyzzywfys);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("代收费业务费")) {
					String dsfywfys = tr.select("td").get(2).text()
							.replace("￥", "").replaceAll("\\s*", "");
					try {
						if (dsfywfys != null) {
							dsfywfy = new BigDecimal(dsfywfys);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("其他费用")) {
					String qtfys = tr.select("td").get(2).text()
							.replace("￥", "").replaceAll("\\s*", "");
					try {
						if (qtfys != null) {
							qtfy = new BigDecimal(qtfys);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("优惠及减免")) {
					String jmfs = tr.select("td").get(2).text()
							.replace("￥", "").replaceAll("\\s*", "");
					try {
						if (jmfs != null) {
							jmf = new BigDecimal(jmfs);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (trStr.contains("本期实时话费")||trStr.contains("本期话费")) {
					RegexPaserUtil count = new RegexPaserUtil("￥", "元", trStr,
							RegexPaserUtil.TEXTEGEXANDNRT);
					String hjs = count.getText();
					try {
						if (hjs != null) {
							hj = new BigDecimal(hjs);
						}
					} catch (Exception e) {
						// //system.out.println("网页文本提取出错！");
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
				mobieTel.setcName(map.get("realName").toString());
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

	private void parseDetailTel(String text, int x) {
		if (text.contains("客户名称")) {
			Document doc6 = Jsoup.parse(text);
			Elements tables = doc6.select("tbody");
			Elements trs = tables.get(3).select("tr");
			String dateYYYMMDD = "";
			for (int i = 0; i < trs.size(); i++) {
				Elements tds = trs.get(i).select("td");

				if (tds.size() > 7) {
					String thsj = dateYYYMMDD + " " + tds.get(0).text();// 起始时间
					String thwz = StringEscapeUtils.unescapeHtml3(
							tds.get(1).text().replaceAll("\\s*", "")).trim();// 通信地点
					String thlx = StringEscapeUtils.unescapeHtml3(
							tds.get(2).text().replaceAll("\\s*", "")).trim();// 通信方式
					String dfhm = tds.get(3).text().replaceAll("\\s*", "")
							.trim();// 对方号码
					String thsc = tds.get(4).text().replaceAll("\\s*", "")
							.trim();// 通信时长
					String ctlx = StringEscapeUtils.unescapeHtml3(
							tds.get(5).text().replaceAll("\\s*", "")).trim();// 通信类型
					// String mylx =
					// StringEscapeUtils.unescapeHtml3(tds.get(6).text().replaceAll("\\s*",
					// ""));//套餐优惠
					String zfy = tds.get(7).text().replaceAll("元", "")
							.replaceAll("\\s*", "").trim();// 通信费
					Map map2 = new HashMap();
					zfy = zfy.trim().substring(1, zfy.length());
					map2.put("phone", login.getLoginName());
					map2.put("cTime",
							DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
					List list = mobileDetailService.getMobileDetailBypt(map2);
					if (list == null || list.size() == 0) {
						MobileDetail mDetail = new MobileDetail();
						UUID uuid = UUID.randomUUID();
						mDetail.setId(uuid.toString());
						mDetail.setcTime(DateUtils.StringToDate(thsj,
								"yyyy-MM-dd HH:mm:ss"));
						mDetail.setTradeAddr(thwz);

						if (thlx.contains("主叫")) {
							mDetail.setTradeWay("主叫");
						} else if (thlx.contains("被叫")) {
							mDetail.setTradeWay("被叫");
						}
						int times = 0;
						try {
							times = Integer.parseInt(removeChinese(thsc.trim()
									.substring(1, zfy.length())));
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
				} else {
					dateYYYMMDD = tds.get(0).text();
				}

			}
		}
	}

	private boolean parseMessage(String text) {
		// 温馨提示：由于一条短信最多70个字，当您发送超过70个字的短信时，系统会自动分成多条计费。查询短信详单时，会体现为几条相同的短信发送记录。
		boolean goNext = true;
		if (text.contains("客户名称")) {
			Document doc6 = Jsoup.parse(text);
			Elements tables = doc6.select("tbody");
			Elements trs = tables.get(2).select("tr");
			String dateYYYMMDD = "";
			List<MobileMessage> messageList = new ArrayList<MobileMessage>();
			for (int i = 0; i < trs.size() - 1; i++) {
				Elements tds = trs.get(i).select("td");
				if (tds.size() > 6) {
					// 起始时间 对方号码 通信方式 信息类型 业务名称 套餐 通信费
					String thsj = dateYYYMMDD + " " + tds.get(0).text();// 起始时间
					String dfhm = tds.get(1).text().replaceAll("\\s*", "")
							.trim();// 对方号码
					String thlx = StringEscapeUtils.unescapeHtml3(
							tds.get(2).text().replaceAll("\\s*", "")).trim();// 通信方式
					String ctlx = StringEscapeUtils.unescapeHtml3(
							tds.get(3).text().replaceAll("\\s*", "")).trim();// 信息类型
					String thwz = StringEscapeUtils.unescapeHtml3(
							tds.get(4).text().replaceAll("\\s*", "")).trim();// 业务名称
					String mylx = StringEscapeUtils.unescapeHtml3(tds.get(5)
							.text().replaceAll("\\s*", ""));// 套餐
					String zfy = tds.get(6).text().replaceAll("\\s*", "")
							.trim();// 通信费

					MobileMessage mobileMessage = new MobileMessage();
					UUID uuid = UUID.randomUUID();
					mobileMessage.setId(uuid.toString());
					mobileMessage.setSentTime(DateUtils.StringToDate(thsj,
							"yyyy-MM-dd HH:mm:ss"));
					mobileMessage.setTradeWay(thlx);
					// mobileMessage.setSentAddr(sentAddr);
					mobileMessage.setRecevierPhone(dfhm);
					mobileMessage.setAllPay(new BigDecimal(zfy));
					mobileMessage.setPhone(login.getLoginName());
					mobileMessage
							.setCreateTs(DateUtils.StringToDate(
									new Date().toLocaleString(),
									"yyyy-MM-dd HH:mm:ss"));
					messageList.add(mobileMessage);
				} else {
					dateYYYMMDD = tds.get(0).text();
				}
			}
			goNext = saveMobileMessage(messageList);
		}
		return goNext;
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

	private void getIndexParam(String url, CHeader header, String formName) {
		String html = "";
		if (header != null) {
			html = cutil.get(url, header);
		} else {
			html = cutil.get(url);
		}
		Document doc = Jsoup.parse(html);
		Elements form = doc.select("form#" + formName);
		String loginTokenId = form.select("[name=loginTokenId]").attr("value");
		String token = form.select("[name=token]").attr("value");
		map.put("loginTokenId", loginTokenId);
		map.put("token", token);
	}

	@Override
	public LinkedList<MonthlyBillMark> gatherFlowBill() {
		LinkedList<MonthlyBillMark> flowBarks = getSpiderMonthsMark(true,
				"yyyyMM", 6, 1);
		Date date = getMaxFlowTime();
		CHeader header = new CHeader(
				"https://www.hn.10086.cn/service/fee/detailBillInfo.jsp");
		String messageUrl = "https://www.hn.10086.cn/ajax/billservice/detailBillInfo.jsp";
		Map<String, String> param = new HashMap<String, String>();
		param.put("busiId", "detailBill11");
		param.put("detailBillPwd", "undefined");
		param.put("detailType", "3");
		param.put("operation", "query");
		param.put("r", new Date().toString());
		param.put("startDate", "01");
		param.put("token", map.get("token").toString());
		int size = flowBarks.size();
		for (int i = size - 1; i >= 0; i--) {
			int endData = DateUtils.getDayOfMonth(DateUtils.StringToDate(
					flowBarks.get(i).getMonth(), "yyyyMM"));
			param.put("month", flowBarks.get(i).getMonth());
			param.put("endDate", "" + endData);
			String text = cutil.post(messageUrl, header, param);
			flowBarks.get(i).setText(text);
		}

		flowBillHtmls = new LinkedList<String>();
		for (int i = 0; i <= size - 1; i++) {
			flowBillHtmls.add(flowBarks.get(i).getText());
		}

		gatherFlowBill_parse(flowBarks);
		MobileOnlineBill mobile = flowBarks.get(0).getMobileOnlineBill();
		if (mobile != null) {
			mobile.setIscm(1);
		}
		return flowBarks;
	}

	public void gatherFlowBill_parse(LinkedList<MonthlyBillMark> flowBarks) {
		for (MonthlyBillMark mark : flowBarks) {
			String html = mark.getText();
			try {
				if (html != null) {
					Document doc = Jsoup.parse(html);
					Element billElement = doc.select("table").get(3);
					Elements bills = billElement.select("tr").get(0)
							.select("td");
					String chargeFlowStr = bills.get(1).text().trim();
					String freeFlowStr = bills.get(2).text().trim();
					String totalFlowStr = bills.get(3).text().trim();
					Long chargeFlow = new Long(0);
					if (chargeFlowStr.length() > 6) {
						chargeFlow = new Long(Math.round(Double
								.parseDouble(chargeFlowStr.substring(4,
										chargeFlowStr.indexOf("(")))));
					}
					Long freeFlow = new Long(0);
					if (freeFlowStr.length() > 6) {
						freeFlow = new Long(Math.round(Double
								.parseDouble(freeFlowStr.substring(4,
										freeFlowStr.indexOf("(")))));
					}
					Long totalFlow = new Long(0);
					if (freeFlowStr.length() > 7) {
						totalFlow = new Long(Math.round(Double
								.parseDouble(totalFlowStr.substring(3,
										totalFlowStr.indexOf("(")))));
					}
					MobileOnlineBill mOnlineBill = new MobileOnlineBill();
					mOnlineBill.setPhone(login.getLoginName());
					mOnlineBill.setMonthly(DateUtils.StringToDate(
							mark.getMonth(), "yyyyMM"));
					mOnlineBill.setTotalFlow(totalFlow);
					mOnlineBill.setChargeFlow(chargeFlow);
					mOnlineBill.setFreeFlow(freeFlow);
					// mOnlineBill.setTrafficCharges(charge);
					mark.setObj(mOnlineBill);
				}
			} catch (Exception e) {
				writeLogByFlowBill(e);
			}
		}

	}

	@Override
	public LinkedList<MobileOnlineList> gatherFlowList() {
		LinkedList<MobileOnlineList> list = new LinkedList();
		Date date = getMaxFlowTime();
		int size = this.flowBillHtmls.size();
		for (int i = size - 1; i >= 0; i--) {
			String text = this.flowBillHtmls.get(i);

			if (text != null) {
				list.addAll(gatherFlowList_parse(text, date));
			}

		}
		return list;
	}

	public LinkedList<MobileOnlineList> gatherFlowList_parse(String html,
			Date bigTime) {
		LinkedList<MobileOnlineList> listData = new LinkedList();
		String date = "2014-01-01";
		if (html == null || !html.contains("上网详单(包括GPRS和WLAN)")) {
			return listData;
		}
		Document doc = Jsoup.parse(html);
		Element table = doc.select("table").get(4);
		List<Element> trs = table.select("tr");
		trs = trs.subList(1, trs.size() - 4);
		// 第几个月

		while (trs.size() != 0) {
			Element tr = trs.remove(0);
			// 获取第一个tr
			Elements tds = tr.select("td");
			String td1 = tds.remove(0).text();
			// 判断第一个tr是日期还是时间
			if (td1.indexOf("-") != -1) {
				// 是日期行更改日期
				date = td1.substring(0, 10);
			} else if (td1.indexOf(":") != -1) {
				// 是数据行
				// 实例化pojo对象
				MobileOnlineList mobileOnlineList = new MobileOnlineList();
				// 成员变量赋值
				UUID uuid = UUID.randomUUID();
				mobileOnlineList.setId(uuid.toString());
				mobileOnlineList.setPhone(login.getLoginName());

				mobileOnlineList.setcTime(DateUtils.StringToDate(date + " "
						+ td1.substring(0, 8), "yyyy-MM-dd HH:mm:ss"));
				if (bigTime != null
						&& bigTime.getTime() >= mobileOnlineList.getcTime()
								.getTime()) {
					continue;
				}
				mobileOnlineList.setTradeAddr(tds.remove(0).text().trim()
						.replaceAll(" ", ""));
				mobileOnlineList.setOnlineType(tds.remove(0).text().trim()
						.replaceAll(" ", ""));
				String onlineTime = tds.remove(0).text().trim()
						.replaceAll(" ", "");
				if (onlineTime == null || "".equals(onlineTime)) {
					onlineTime = "0";
				}
				Long time = StringUtil.flowTimeFormat(onlineTime);

				mobileOnlineList.setOnlineTime(time);
				String totalFlow = tds.remove(0).text().trim()
						.replaceAll(" ", "");
				mobileOnlineList.setTotalFlow(new Long(Math.round(Double
						.parseDouble(totalFlow))));
				String cheapService = tds.remove(0).text().trim()
						.replaceAll(" ", "");
				mobileOnlineList.setCheapService(cheapService);
				listData.add(mobileOnlineList);
				// mobileOnlineListService.save(mobileOnlineList);
			}
		}
		return listData;
	}

	/*
	 * public static void parseTest() throws IOException { InputStream in = new
	 * FileInputStream("D:/html.txt"); BufferedReader reader = new
	 * BufferedReader(new InputStreamReader(in, "gbk")); String phtml;
	 * StringBuffer buffer = new StringBuffer(); while ((phtml =
	 * reader.readLine()) != null) { buffer.append(phtml); } String html =
	 * buffer.toString(); Document doc = Jsoup.parse(html); Element billElement
	 * = doc.select("table").get(3); System.out.println(billElement); Elements
	 * bills = billElement.select("tr").get(0).select("td"); String
	 * chargeFlowStr = bills.get(1).text().trim(); String freeFlowStr =
	 * bills.get(2).text().trim(); String totalFlowStr =
	 * bills.get(3).text().trim(); Long chargeFlow = new Long(0); if
	 * (chargeFlowStr.length() > 6) { // chargeFlow =
	 * Long.parseLong(chargeFlowStr.substring(4, //
	 * chargeFlowStr.indexOf("."))); chargeFlow =new
	 * Long(Math.round(Double.parseDouble(chargeFlowStr.substring(4,
	 * chargeFlowStr.indexOf("("))))); } System.out.println(chargeFlow); Long
	 * freeFlow=new Long(0); if (freeFlowStr.length() > 6) { // freeFlow =
	 * Long.parseLong(freeFlowStr.substring(4, // freeFlowStr.indexOf(".")));
	 * freeFlow = new
	 * Long(Math.round(Double.parseDouble(freeFlowStr.substring(4,
	 * freeFlowStr.indexOf("."))))); } System.out.println(freeFlow); Long
	 * totalFlow = new Long(0); if (freeFlowStr.length() > 7) { // totalFlow =
	 * Long.parseLong(totalFlowStr.substring(3, // totalFlowStr.indexOf(".")));
	 * totalFlow = new
	 * Long(Math.round(Double.parseDouble(totalFlowStr.substring(3,
	 * totalFlowStr.indexOf("."))))); } System.out.println(totalFlow); }
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * Login login = new Login(); login.setLoginName("18373914055");
		 * login.setPassword("168168"); HuNanYidong hb = new HuNanYidong(login);
		 * // hb.index(); // hb.getImg(); hb.index();
		 * hb.inputCode(hb.getImgUrl()); hb.login(); hb.saveMessage();
		 * hb.gatherFlowList(); hb.close(); // hb.gatherFlowBill(); String s =
		 * "dsfdsfdsfdsf"; new RegexPaserUtil("ds",
		 * "sf",s,RegexPaserUtil.TEXTEGEXANDNRT).getText();
		 * 
		 * 
		 * InputStream in = new FileInputStream("D:/html.txt"); BufferedReader
		 * reader = new BufferedReader(new InputStreamReader(in, "gbk")); String
		 * phtml; StringBuffer buffer = new StringBuffer(); while ((phtml =
		 * reader.readLine()) != null) { buffer.append(phtml); } String html =
		 * buffer.toString(); System.out.println(html);
		 */
		// List<MobileOnlineList> list = hb.gatherFlowList_parse(html,null);
		// for (MobileOnlineList mobileOnlineList : list) {
		// System.out.println(mobileOnlineList.getPhone() + " "
		// + mobileOnlineList.getOnlineType() + " "
		// + mobileOnlineList.getTradeAddr() + " "
		// + mobileOnlineList.getTotalFlow() + " "
		// + mobileOnlineList.getCheapService());
		//
		// }

		// String html="";
		// RegexPaserUtil rp =new RegexPaserUtil(beginRegex,
		// endRegex,html,RegexPaserUtil.TEXTTEGEX);
		// hb.getYuE();
		// if(hb.islogin()){
		// hb.sendPhoneDynamicsCode();
		// hb.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
		// hb.checkPhoneDynamicsCode();//短信口令
		//
		// }

		// //system.out.println("请输入动态码：");
		// String authcode = in.nextLine();

		// hb.getYuE();
		// hb.saveMonthTel();
		// hb.saveDetailTel(authcode);
		// parseTest();
	}

}
