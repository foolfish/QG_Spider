package com.lkb.thirdUtil.shebao.guangdong;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.SheBao;
import com.lkb.bean.User;
import com.lkb.bean.client.SocialInsuranceInput;
import com.lkb.bean.client.SocialInsuranceOut;
import com.lkb.constant.Constant;
import com.lkb.service.ISheBaoService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.BaseUser;
import com.lkb.thirdUtil.base.BaseInfoSocialInsurance;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;

/*
 * 深圳社保
 * */
public class ShenZhenShebao  extends BaseInfoSocialInsurance{

	public static final DateFormat format = new SimpleDateFormat(
			"yy_MM_dd_HH_mm_ss");
	private static String loginUrl = "https://e.szsi.gov.cn/siservice/#";
	private static String authUrl = "https://e.szsi.gov.cn/siservice/PImages?pid=";

	private static String userInfoURL = "https://e.szsi.gov.cn/siservice/serviceListAction.do?id=5&pid=1398339493218";
	private static String sbLoginURL = "https://e.szsi.gov.cn/siservice/LoginAction.do";
	private static String pIndexUrl = "https://e.szsi.gov.cn/siservice/pIndex.jsp";

	// 用户基本信息
	private static String userInfo = "https://e.szsi.gov.cn/siservice/transUrl.jsp?url=serviceListAction.do?id=5";
	private static String userInfoDetail = "https://e.szsi.gov.cn/siservice/serviceListAction.do?id=5&pid=";

	// 基本信息补充
	private static String userInfoSupply = "https://e.szsi.gov.cn/siservice/transUrl.jsp?url=person/personUpdate.jsp";
	private static String userInfoSupplyDetail = "https://e.szsi.gov.cn/siservice/person/personUpdate.jsp?pid=";

	// 养老保险
	private static String EnInsurance = "https://e.szsi.gov.cn/siservice/transUrl.jsp?url=serviceListAction.do?id=1";
	private static String EnInsuranceDetail = "https://e.szsi.gov.cn/siservice/serviceListAction.do?id=1&pid=";

	// 医疗保险
	private static String MeInsurance = "https://e.szsi.gov.cn/siservice/transUrl.jsp?url=serviceListAction.do?id=2";
	private static String MeInsuranceDetail = "https://e.szsi.gov.cn/siservice/serviceListAction.do?id=2&pid=";

	// 工伤保险
	private static String InInsurance = "https://e.szsi.gov.cn/siservice/transUrl.jsp?url=serviceListAction.do?id=4";
	private static String InInsuranceDetail = "https://e.szsi.gov.cn/siservice/serviceListAction.do?id=4&pid=";

	// 失业保险
	private static String UnInsurance = "https://e.szsi.gov.cn/siservice/transUrl.jsp?url=serviceListAction.do?id=7";
	private static String UnInsuranceDetail = "https://e.szsi.gov.cn/siservice/serviceListAction.do?id=7&pid=";

	private static Logger logger = Logger.getLogger(ShenZhenShebao.class);

	public static void main(String[] args) throws Exception {
	}
	public ShenZhenShebao() {
		super();
	}
	@Override
	public void getInputPrarms() {
		 sio.addParams(SocialInsuranceInput.param_id_card);
		 sio.setRegisterUrl("https://e.szsi.gov.cn/siservice/person/protocol.jsp?pid=1411572490734");
	}
	public void init() {
		if(!isInit()){
			try {
				Map<String, String> map = new HashMap<String, String>();
				String text = cutil.get(loginUrl);
				Document doc = Jsoup.parse(text);
				
				String Method = doc.select("input[name=Method]").first()
						.attr("value");
				map.put("Method", Method);
				String pid = doc.select("input[name=pid]").first().attr("value");
				map.put("pid", pid);
			
				redismap.put("szsbMap", map);//根据实际需要存放
				String authcodeURL2 = authUrl + pid;
				setImgUrl(authcodeURL2);
				setInit();
			} catch (Exception e) {
			}
		}
	}


	public void login_sio() {
		try{
			Map szsbMap = (Map)redismap.get("szsbMap");
			if(szsbMap!=null){
				String content = login(szsbMap);
				if(content.contains("/siservice/LoginAction.do")){
					RegexPaserUtil rp = new RegexPaserUtil("<script language='JavaScript'>alert\\('", "'\\)</script>",content,RegexPaserUtil.TEXTEGEXANDNRT);			
					errorMsg = rp.getText(); 
					if(errorMsg.contains("ok")){
						loginsuccess();
						addTask(this);
					}
				}else{
					loginsuccess();
					addTask(this);
				}
			}else{
				errorMsg = "系统出错，请刷新后重试！";
			}
		}catch(Exception e){
			writeLogByLogin(e);
		}
	}
	/*
	 * 验证用户是否登陆
	 */
	public String login(Map map) {
		Map param = new HashMap();
		param.put("pid", map.get("pid"));
		param.put("Method", map.get("Method"));
		param.put("AAC002", loginName);
		String b64 = DatatypeConverter.printBase64Binary(sii.getPassword().getBytes());
		param.put("CAC222", b64);
		param.put("PSINPUT", sii.getAuthcode());
		String content = cutil.post(sbLoginURL, param);
		return content;
	}
	
	/*
	 * 工伤保险
	 */
	public void parseInInsurance(ISheBaoService shebaoService,
			String currentUser, String content, IWarningService warningService,String loginName) {
		try {
			Document doc = Jsoup.parse(content);
			String[] strs = doc.select("body").first().select("p").first()
					.html().replace("&nbsp;", "").split("<br />");
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 1; i < strs.length; i++) {
				String text = strs[i];
				String[] strs2 = text.split(" ");
				String key = strs2[0];
				String value = strs2[1];
				map.put(key, value);
			}
			Elements elements = doc.select("tr[id=TR0]");
			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				Elements elements2 = element.select("td");
				String key = elements2.get(0).text();
				String payCompany = map.get(key);
				String payTime = elements2.get(1).text().trim();
				String payBase = elements2.get(2).text();
				String payInjuryCom = elements2.get(3).text();

				BigDecimal bpayBase = new BigDecimal(payBase);
				BigDecimal bpayInjuryCom = new BigDecimal(payInjuryCom);

				DateFormat format = new SimpleDateFormat("yyyy - MM");
				Date date = null;
				try {
					date = format.parse(payTime); // Thu Jan 18 00:00:00 CST
													// 2007
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Map<String, Object> sbmap = new HashMap<String, Object>(3);
				sbmap.put("baseUserId", currentUser);
				sbmap.put("source", Constant.SZSHEBAO);
				sbmap.put("payTime", date);
				List<SheBao> list = shebaoService
						.getSheBaoByBaseUseridsource(sbmap);

				if (list != null && list.size() > 0) {
					SheBao sheBao = list.get(0);
					sheBao.setPayInjuryBase(bpayBase);
					sheBao.setLoginName(loginName);
					sheBao.setPayInjuryCom(bpayInjuryCom);
					sheBao.setPayTime(date);
					sheBao.setPayCompany(payCompany);
					sheBao.setSource(Constant.SZSHEBAO);
					shebaoService.update(sheBao);
				} else {
					try {
						SheBao sheBao = new SheBao();
						UUID uuid = UUID.randomUUID();
						sheBao.setId(uuid.toString());
						sheBao.setPayInjuryBase(bpayBase);
						sheBao.setLoginName(loginName);
						sheBao.setPayInjuryCom(bpayInjuryCom);
						sheBao.setPayTime(date);
						sheBao.setPayCompany(payCompany);
						sheBao.setSource(Constant.SZSHEBAO);
						shebaoService.saveSheBao(sheBao);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		} catch (Exception e) {
			// 报警
			logger.info("第260行捕获异常：" ,e);		
			String warnType = WaringConstaint.SZSB_7;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}

	/*
	 * 失业保险
	 */
	public void parseUnInsurance(ISheBaoService shebaoService,
			String currentUser, String content, IWarningService warningService,String loginName) {
		try {
			Document doc = Jsoup.parse(content);
			String[] strs = doc.select("body").first().select("p").first()
					.html().replace("&nbsp;", "").split("<br />");
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 1; i < strs.length; i++) {
				String text = strs[i];
				String[] strs2 = text.split(" ");
				String key = strs2[0];
				String value = strs2[1];
				map.put(key, value);
			}
			Elements elements = doc.select("tr[id=TR0]");
			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				Elements elements2 = element.select("td");
				String key = elements2.get(0).text();
				String payCompany = map.get(key);
				String payTime = elements2.get(1).text().trim();
				String payBase = elements2.get(2).text();
				String payUmemplyCom = elements2.get(3).text();

				BigDecimal bpayBase = new BigDecimal(payBase);
				BigDecimal bpayUmemplyCom = new BigDecimal(payUmemplyCom);

				DateFormat format = new SimpleDateFormat("yyyy - MM");
				Date date = null;
				try {
					date = format.parse(payTime); // Thu Jan 18 00:00:00 CST
													// 2007
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Map<String, Object> sbmap = new HashMap<String, Object>(3);
				sbmap.put("baseUserId", currentUser);
				sbmap.put("source", Constant.SZSHEBAO);
				sbmap.put("payTime", date);
				List<SheBao> list = shebaoService
						.getSheBaoByBaseUseridsource(sbmap);

				if (list != null && list.size() > 0) {
					SheBao sheBao = list.get(0);
					sheBao.setPayUmemplyBase(bpayBase);
					sheBao.setLoginName(loginName);
					sheBao.setPayUmemplyCom(bpayUmemplyCom);
					sheBao.setPayTime(date);
					sheBao.setPayCompany(payCompany);
					sheBao.setSource(Constant.SZSHEBAO);
					shebaoService.update(sheBao);
				} else {
					try {
						SheBao sheBao = new SheBao();
						UUID uuid = UUID.randomUUID();
						sheBao.setId(uuid.toString());
						sheBao.setPayUmemplyBase(bpayBase);
						sheBao.setLoginName(loginName);
						sheBao.setPayUmemplyCom(bpayUmemplyCom);
						sheBao.setPayTime(date);
						sheBao.setPayCompany(payCompany);
						sheBao.setSource(Constant.SZSHEBAO);
						shebaoService.saveSheBao(sheBao);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		} catch (Exception e) {
			// 报警
			logger.info("第337行捕获异常：" ,e);		
			String warnType = WaringConstaint.SZSB_8;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}

	/*
	 * 医疗保险
	 */
	public void parseMeInsurance(ISheBaoService shebaoService,
			String currentUser, String content, IWarningService warningService,String loginName) {
		try {
			Document doc = Jsoup.parse(content);
			String[] strs = doc.select("body").first().select("p").first()
					.html().replace("&nbsp;", "").split("<br />");
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 1; i < strs.length; i++) {
				String text = strs[i];
				String[] strs2 = text.split(" ");
				String key = strs2[0];
				String value = strs2[1];
				map.put(key, value);
			}
			Elements elements = doc.select("tr[id=TR0]");
			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				Elements elements2 = element.select("td");
				String key = elements2.get(0).text();
				String payCompany = map.get(key);
				String payTime = elements2.get(1).text().trim();
				String payBase = elements2.get(2).text();
				String payMedPerson = elements2.get(3).text();
				String payMedCom = elements2.get(4).text();
				String payMedAll = elements2.get(5).text();
				BigDecimal bpayBase = new BigDecimal(payBase);
				BigDecimal bpayMedPerson = new BigDecimal(payMedPerson);
				BigDecimal bpayMedCom = new BigDecimal(payMedCom);
				BigDecimal bpayMedAll = new BigDecimal(payMedAll);
				// System.out.println(elements.get(i).text());

				DateFormat format = new SimpleDateFormat("yyyy - MM");
				Date date = null;
				try {
					date = format.parse(payTime); // Thu Jan 18 00:00:00 CST
													// 2007
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Map<String, Object> sbmap = new HashMap<String, Object>(3);
				sbmap.put("baseUserId", currentUser);
				sbmap.put("source", Constant.SZSHEBAO);
				sbmap.put("payTime", date);
				List<SheBao> list = shebaoService
						.getSheBaoByBaseUseridsource(sbmap);

				if (list != null && list.size() > 0) {
					SheBao sheBao = list.get(0);
					sheBao.setPayMedBase(bpayBase);
					sheBao.setLoginName(loginName);
					sheBao.setPayMedAll(bpayMedAll);
					sheBao.setPayMedCom(bpayMedCom);
					sheBao.setPayMedPerson(bpayMedPerson);
					sheBao.setPayTime(date);
					sheBao.setPayCompany(payCompany);
					sheBao.setSource(Constant.SZSHEBAO);
					shebaoService.update(sheBao);
				} else {
					try {
						SheBao sheBao = new SheBao();
						UUID uuid = UUID.randomUUID();
						sheBao.setId(uuid.toString());
						sheBao.setPayMedBase(bpayBase);
						sheBao.setLoginName(loginName);
						sheBao.setPayMedAll(bpayMedAll);
						sheBao.setPayMedCom(bpayMedCom);
						sheBao.setPayMedPerson(bpayMedPerson);
						sheBao.setPayTime(date);
						sheBao.setPayCompany(payCompany);
						sheBao.setSource(Constant.SZSHEBAO);
						shebaoService.saveSheBao(sheBao);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		} catch (Exception e) {
			// 报警
			logger.info("第407行捕获异常：",e);		
			String warnType = WaringConstaint.SZSB_6;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}

	/*
	 * 养老保险
	 */
	public void parseEnInsurance(ISheBaoService shebaoService,
			String currentUser, String content, IWarningService warningService,String loginName) {
		try {
			Document doc = Jsoup.parse(content);
			String[] strs = doc.select("body").first().select("p").first()
					.html().replace("&nbsp;", "").split("<br />");
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 1; i < strs.length; i++) {
				String text = strs[i];
				String[] strs2 = text.split(" ");
				String key = strs2[0];
				String value = strs2[1];
				map.put(key, value);
			}
			Elements elements = doc.select("tr[id=TR0]");
			for (int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				Elements elements2 = element.select("td");
				String key = elements2.get(0).text();
				String payCompany = map.get(key);
				String payTime = elements2.get(1).text().trim();
				String payBase = elements2.get(2).text();
				String payFeedPerson = elements2.get(3).text();
				String payFeedCom = elements2.get(4).text();
				String payFeedAll = elements2.get(5).text();
				BigDecimal bpayBase = new BigDecimal(payBase);
				BigDecimal bpayFeedPerson = new BigDecimal(payFeedPerson);
				BigDecimal bpayFeedCom = new BigDecimal(payFeedCom);
				BigDecimal bpayFeedAll = new BigDecimal(payFeedAll);

				DateFormat format = new SimpleDateFormat("yyyy - MM");
				Date date = null;
				try {
					date = format.parse(payTime); // Thu Jan 18 00:00:00 CST
													// 2007
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Map<String, Object> sbmap = new HashMap<String, Object>(3);
				sbmap.put("baseUserId", currentUser);
				sbmap.put("source", Constant.SZSHEBAO);
				sbmap.put("payTime", date);
				List<SheBao> list = shebaoService
						.getSheBaoByBaseUseridsource(sbmap);

				if (list != null && list.size() > 0) {
					SheBao sheBao = list.get(0);
					sheBao.setPayFeedBase(bpayBase);
					sheBao.setLoginName(loginName);
					sheBao.setPayFeedAll(bpayFeedAll);
					sheBao.setPayFeedCom(bpayFeedCom);
					sheBao.setPayFeedPerson(bpayFeedPerson);
					sheBao.setPayTime(date);
					sheBao.setPayCompany(payCompany);
					sheBao.setSource(Constant.SZSHEBAO);
					shebaoService.update(sheBao);
				} else {
					try {
						SheBao sheBao = new SheBao();
						UUID uuid = UUID.randomUUID();
						sheBao.setId(uuid.toString());
						sheBao.setLoginName(loginName);
						sheBao.setPayFeedBase(bpayBase);
						sheBao.setPayFeedAll(bpayFeedAll);
						sheBao.setPayFeedCom(bpayFeedCom);
						sheBao.setPayFeedPerson(bpayFeedPerson);
						sheBao.setPayTime(date);
						sheBao.setPayCompany(payCompany);
						sheBao.setSource(Constant.SZSHEBAO);
						shebaoService.saveSheBao(sheBao);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		} catch (Exception e) {
			// 报警
			logger.info("第488行捕获异常：" ,e);		
			String warnType = WaringConstaint.SZSB_5;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}

	/*
	 * 抓取用户其他信息；如手机号
	 */
	public void parseUserInfoSupply(IUserService userService,
			String currentUser, String loginName, 
			String content, IWarningService warningService) {
		Document doc = null;
		String phone = "";
		try {
			doc = Jsoup.parse(content);
			phone = doc.select("input[name=cae0f0]").attr("value");
		} catch (Exception e) {
			// 报警
			logger.info("第499行捕获异常：",e);		
			String warnType = WaringConstaint.SZSB_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		Date modifyDate = new Date();

		Map<String, String> map = new HashMap<String, String>(3);
		map.put("parentId", currentUser);
		map.put("loginName", loginName);
		map.put("usersource", Constant.SZSHEBAO);
		List<User> list = userService.getUserByParentIdSource(map);
		if (list != null && list.size() > 0) {
			User user = list.get(0);
			user.setPhone(phone);
			user.setLoginName(loginName);
			user.setUserName(loginName);
			user.setUsersource(Constant.SZSHEBAO);
			user.setUsersource2(Constant.SZSHEBAO);
			user.setParentId(currentUser);
			user.setModifyDate(modifyDate);
			userService.update(user);
		} else {
			try {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(loginName);
				user.setUserName(loginName);
				user.setUsersource(Constant.SZSHEBAO);
				user.setUsersource2(Constant.SZSHEBAO);
				user.setParentId(currentUser);
				user.setPhone(phone);
				user.setModifyDate(modifyDate);
				userService.saveUser(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



	/*
	 * 抓取用户基本信息
	 */
	public void parseUserInfo(IUserService userService, String currentUser,
			String loginName,  String content,
			IWarningService warningService) {
		try {
			Document doc = Jsoup.parse(content);

			Elements elements = doc.select("input[name=lefts]");
			Elements elements2 = doc.select("input[name=rights]");

			String realName = elements.get(0).attr("value");
			String idcard = elements.get(2).attr("value");
			String sex = elements.get(3).attr("value");
			String ssNo = elements2.get(0).attr("value");
			String workerNature = elements.get(6).attr("value");
			String jobTitle = elements2.get(3).attr("value");
			String pUnit = elements.get(7).attr("value");
			String sspComNo = elements.get(1).attr("value");
			String hcatagory = elements.get(5).attr("value");
			String cardstatus = elements2.get(1).attr("value");
			String carePerson = elements2.get(2).attr("value");
			String paySalary = elements.get(8).attr("value");
			String payStatus = elements.get(12).attr("value");
			String birthday = elements.get(4).attr("value");
			birthday = birthday.trim();

			DateFormat format = new SimpleDateFormat("yyyy - MM");
			Date date = null;
			try {
				date = format.parse(birthday); // Thu Jan 18 00:00:00 CST 2007
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Date modifyDate = new Date();
//			BaseUser bu = new BaseUser();
//			Map baseMap = new HashMap();
//			baseMap.put("sex", sex);
//			baseMap.put("idcard", idcard);
//			baseMap.put("modifyDate", modifyDate);
//			bu.saveUserInfo(userService, baseMap, currentUser);

			Map<String, String> map = new HashMap<String, String>(3);
			map.put("parentId", currentUser);
			map.put("loginName", loginName);
			map.put("usersource", Constant.SZSHEBAO);
			List<User> list = userService.getUserByParentIdSource(map);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setWorkerNature(workerNature);
				user.setJobTitle(jobTitle);
				user.setpUnit(pUnit);
				user.setSspComNo(sspComNo);
				user.setHcatagory(hcatagory);
				user.setCardstatus(cardstatus);
				user.setCarePerson(carePerson);
				user.setPaySalary(paySalary);
				user.setPayStatus(payStatus);
				user.setLoginName(loginName);
				user.setUserName(loginName);
				user.setRealName(realName);
				user.setSex(sex);
				user.setBirthday(date);
				user.setIdcard(idcard);
				user.setSsNo(ssNo);
				user.setUsersource(Constant.SZSHEBAO);
				user.setUsersource2(Constant.SZSHEBAO);
				user.setParentId(currentUser);
				user.setModifyDate(modifyDate);
				userService.update(user);
			} else {
				try {
					User user = new User();
					UUID uuid = UUID.randomUUID();
					user.setId(uuid.toString());
					user.setWorkerNature(workerNature);
					user.setJobTitle(jobTitle);
					user.setpUnit(pUnit);
					user.setSspComNo(sspComNo);
					user.setHcatagory(hcatagory);
					user.setCardstatus(cardstatus);
					user.setCarePerson(carePerson);
					user.setPaySalary(paySalary);
					user.setPayStatus(payStatus);
					user.setLoginName(loginName);
					user.setUserName(loginName);
					user.setRealName(realName);
					user.setSex(sex);
					user.setIdcard(idcard);
					user.setBirthday(date);
					user.setSsNo(ssNo);
					user.setUsersource(Constant.SZSHEBAO);
					user.setUsersource2(Constant.SZSHEBAO);
					user.setParentId(currentUser);
					user.setModifyDate(modifyDate);
					userService.saveUser(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// 报警
			logger.info("第644行捕获异常：" ,e);		
			String warnType = WaringConstaint.SZSB_3;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}

	
	@Override
	public void startSpider() {
		try{
			String[] urls = { userInfo, userInfoSupply, EnInsurance, MeInsurance,
					InInsurance, UnInsurance };
			String[] urlDetails = { userInfoDetail, userInfoSupplyDetail,
					EnInsuranceDetail, MeInsuranceDetail, InInsuranceDetail,
					UnInsuranceDetail };
			for (int i = 0; i < urls.length; i++) {
				String url = urls[i];
				String urlDetail = urlDetails[i];
				String content = cutil.get(url);
				// System.out.print(content);
				String[] strs = content.split("pid=");
				String[] strs2 = strs[1].split("\"");
				String pid2 = strs2[0];

				String content2 = cutil.get(urlDetail + pid2);
				if (i == 0) {
					parseUserInfo(userService, currentUser, loginName,
							 content2, warningService);
				} else if (i == 1) {
					parseUserInfoSupply(userService, currentUser, loginName,
							 content2, warningService);
				} else if (i == 2) {
					parseEnInsurance(shebaoService, currentUser, content2,
							warningService,loginName);
				} else if (i == 3) {
					parseMeInsurance(shebaoService, currentUser, content2,
							warningService,loginName);
				} else if (i == 4) {
					parseInInsurance(shebaoService, currentUser, content2,
							warningService,loginName);
				} else if (i == 5) {
					parseUnInsurance(shebaoService, currentUser, content2,
							warningService,loginName);
				}

			}
		}catch(Exception e){
			logger.warn("异常：",e);		
		}finally{
			parseEnd(Constant.SZSHEBAO);
		}
	}
}
//	/*
//	 * 开始抓取会员信息
//	 */
//	public void parseBegin1() {
//		
//		String userId = currentUser;
//		String loginName = loginName;
//		String userSource = Constant.SZSHEBAO;
//		try{
//			parseBegin(parseService, userId, loginName, userSource);
//		String[] urls = { userInfo, userInfoSupply, EnInsurance, MeInsurance,
//				InInsurance, UnInsurance };
//		String[] urlDetails = { userInfoDetail, userInfoSupplyDetail,
//				EnInsuranceDetail, MeInsuranceDetail, InInsuranceDetail,
//				UnInsuranceDetail };
//		for (int i = 0; i < urls.length; i++) {
//			String url = urls[i];
//			String urlDetail = urlDetails[i];
//			String content = cutil.get(url);
//			// System.out.print(content);
//			String[] strs = content.split("pid=");
//			String[] strs2 = strs[1].split("\"");
//			String pid2 = strs2[0];
//
//			String content2 = cutil.get(urlDetail + pid2);
//			if (i == 0) {
//				parseUserInfo(userService, currentUser, loginName,
//						 content2, warningService);
//			} else if (i == 1) {
//				parseUserInfoSupply(userService, currentUser, loginName,
//						 content2, warningService);
//			} else if (i == 2) {
//				parseEnInsurance(shebaoService, currentUser, content2,
//						warningService,loginName);
//			} else if (i == 3) {
//				parseMeInsurance(shebaoService, currentUser, content2,
//						warningService,loginName);
//			} else if (i == 4) {
//				parseInInsurance(shebaoService, currentUser, content2,
//						warningService,loginName);
//			} else if (i == 5) {
//				parseUnInsurance(shebaoService, currentUser, content2,
//						warningService,loginName);
//			}
//
//		}
//		}catch(Exception e){
//			logger.info("异常：" ,e);		
//		}finally{
//			parseEnd(parseService, userId, loginName, userSource);
//		}
//
//	}
//}