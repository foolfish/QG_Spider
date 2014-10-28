package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.thirdUtil.base.BasicCommonMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.pojo.SendRequestPojo;

/**
 * 北京移动支持不输入服务密码也能查询话费清单
 * 测试地址:https://cmodsvr1.bj.chinamobile.com/PortalCMOD/detail
 * /detail.do?checkMonth
 * =2014.06&detailType=gsm&ssoSessionID=2c9d82fa477d6ea3014786c08ce40598
 * 
 * @修改时间 2014-7-30 下午6:14:11
 */
public class BJYidong extends BasicCommonMobile{//BaseInfoMobile {

	private static String backurl = "http://www.bj.10086.cn/my";
	private static String continueArg = "http://www.bj.10086.cn/my";
	private static String style = "BIZ_LOGINBOX";
	private static String target = "_parent";// hostId 4 _self
	private static String ssoLogin = "yes";
	private static String loginMode = "1";
	private static String loginMethod = "1";
	private static String isNeedCaptchaPicUrl = "https://bj.ac.10086.cn/ac/IsShowValidateRnum";
	private static String captchaPicUrl = "https://bj.ac.10086.cn/ac/ValidateNum";
	private static String loginSourceUrl = "https://bj.ac.10086.cn/ac/CmSsoLogin";
	// 登录跳转页面
	private static String redirectLoginUrl = "https://bj.ac.10086.cn/ac/cmsso/redirect.jsp";
//	public BJYidong(Login login, IWarningService warningService,
//			String currentUser) {
//		super(login, warningService, ConstantNum.comm_bj_yidong, currentUser);
//	}

	public BJYidong(Login login, String currentUser) {
		super(login, currentUser);
		this.userSource = Constant.YIDONG;
		this.constantNum = ConstantNum.comm_bj_yidong;
	}

	public static void main(String[] args) {
		Login login = new Login("18701683762", "test001");
		// Login login = new Login("18810334535","找玉红");
		BJYidong hn = new BJYidong(login,null);
		// 初始化
		hn.index();
		// 登陆
		hn.inputCode(captchaPicUrl);
//		Map<String, Object> map = hn.login();
		
		hn.getTelDetailHtml();
		String text = hn.cutil.get("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/detail/detail.do?checkMonth=2014.09&detailType=gprs&ssoSessionID="+hn.redismap.get("ssoSessionID").toString());
		System.out.println(text);
		
	}

	// /**
	// * @return 初始化,并获取验证码
	// */
	public void init() {
		context.setImgUrl(captchaPicUrl);
	}

	public void login() {
		getCookie().addCookie("login_mobile", login.getLoginName());
		getCookie().addCookie("c_mobile", login.getLoginName());
		Map<String, String> param = new HashMap<String, String>();
		CHeader h = new CHeader();
		h.setAccept(CHeaderUtil.Accept_json);
		h.setX_requested_with(true);
		param.put("rnum", login.getAuthcode());
		param.put("user", login.getLoginName());
		param.put("phone", login.getLoginName());
		param.put("service", "www.bj.10086.cn");
		param.put("loginMethod", "1");
		param.put("loginMode", "1");
		cutil.post("https://bj.ac.10086.cn/ac/ValidateRnum", h, param);

		// 判断是否需要获取验证码，需要则获取验证码,手动输入
		// HttpPost httppost = new HttpPost(loginSourceUrl);
		param.put("backurl", backurl);
		param.put("box", "");
		param.put("ckCookie", "on");
		param.put("continue", continueArg);
		param.put("loginMethod", loginMethod);
		param.put("loginMode", loginMode);
		param.put("loginName", login.getLoginName());
		param.put("password", login.getPassword());
		param.put("phone", login.getLoginName());
		param.put("rnum", login.getAuthcode());
		param.put("service", "www.bj.10086.cn");
		param.put("smsNum", "随机码");
		param.put("ssoLogin", ssoLogin);
		param.put("style", style);
		param.put("target", target);
		param.put("user", login.getLoginName());
		// CUtil.setHandleRedirect(client,false);
		String postResult = cutil.post(loginSourceUrl, new CHeader(loginSourceUrl), param);
		System.out.println(postResult);
		if (postResult != null) {
			RegexPaserUtil rp = new RegexPaserUtil("var \\$fcode = '", "'",
					postResult, RegexPaserUtil.TEXTEGEXANDNRT);
			output.setErrorMsg(getErrorText(rp.getText()));
		}
		if (output.getErrorMsg() != null && "温馨提示：您已经登录本网站，请稍后再试！".equals(output.getErrorMsg())) {
			param = new HashMap<String, String>();
			param.put("backurl", backurl);
			param.put("box", "");
			param.put("continue", continueArg);
			param.put("loginMethod", loginMethod);
			param.put("loginMode", loginMode);
			param.put("service", "www.bj.10086.cn");
			param.put("style", style);
			param.put("target", "_self");
			param.put("hostId", "4");
			postResult = cutil.post("https://bj.ac.10086.cn/ac/loginAgain",
					h, param);
			if (postResult != null) {
				output.setErrorMsg(null);
			}
		}
		if (output.getErrorMsg().equals("")) {
			// 第三步：打开会员页面以判断登录成功（未登录用户是打不开会员页面的）,这里演示发送豆油
			postResult = cutil.get(redirectLoginUrl);
			if (postResult != null) {
				String ssoSessionId = getCookie().getCookie("cmtokenid", "10086.cn");
				if (ssoSessionId != null) {
					redismap.put(
							"ssoSessionID",
							ssoSessionId.substring(0,
									ssoSessionId.indexOf("@")));
					redismap.put("phone", login.getLoginName());// 存储必要参数
					postResult = loginServicePass();
					if (postResult != null) {
						// System.out.println(postResult);//此处加上判断就ok了
						loginsuccess();
					}
				}
			}
		}
		if (islogin()) {
			addTask(1);
		}
	}

	/***
	 * 登陆服务密码
	 * 
	 * @return
	 */
	public String loginServicePass() {
		String ssoSessionId = redismap.get("ssoSessionID").toString();
		Map<String, String> param = new HashMap<String, String>();
		param.put("searchType", "NowDetail");
		param.put("detailType", "gsm");
		String s = DateUtils.formatDate(new Date(), "yyyy.MM");
		param.put("checkMonth", s);
		param.put("password", login.getSpassword());
		String text = cutil.post(
				"https://cmodsvr1.bj.chinamobile.com/PortalCMOD/LoginSecondCheck?ssoSessionID="
						+ ssoSessionId, param);
		return text;
	}

	/**
	 * 当月话费，入库保存
	 * 
	 * @param nextUrl
	 */
	private void currentMontHuaFei(String nextUrl) {
		String ssoSessionId = redismap.get("ssoSessionID").toString();
		try {
			String text = cutil
					.get("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/InnerInterFaceBusiness?searchType=NowBill&ssoSessionID=="
							+ ssoSessionId);
			// 登陆了一次，服务器那边的session会记录，一段时间内是允许登陆的
			CHeader c = new CHeader();
			c.setX_requested_with(true);
			String phonePage11 = cutil
					.get("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/bill/realBill.do?ssoSessionID="
							+ ssoSessionId);
			Document doc = Jsoup.parse(phonePage11);
			// System.out.println(phonePage11);
			Element element01 = doc.select("table").get(0);
			// 计费周期
			String cycle = element01.select("table>tbody>tr:eq(1)>td:eq(1)")
					.text();
			// 查询日期
			// String searchDate = element01.select(
			// "table>tbody>tr:eq(1)>td:eq(3)").text();
			// 固定费用
			String tcgdf = doc.select("table").get(1)
					.select("table>tbody>tr:eq(1)>td:eq(1)").text();

			// 语音费
			String tcwyytxf = doc.select("table").get(1)
					.select("table>tbody>tr:eq(1)>td:eq(3)").text();
			// 短信费r
			String tcwdxf = doc.select("table").get(1)
					.select("table>tbody>tr:eq(2)>td:eq(3)").text();
			// 代收
			String tryf = doc.select("table").get(1)
					.select("table>tbody>tr:eq(3)>td:eq(3)").text();

			String cAllPay = doc.select("span[class=lv_zi cu_zi]").get(0)
					.text();
			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy.MM");
			String datetime = tempDate.format(new java.util.Date());
			Date t2 = DateUtils.StringToDate(datetime, "yyyy.MM");
			Map map = new HashMap();
			map.put("phone", login.getLoginName());
			map.put("cTime", t2);
			List list = service.getMobileTelService().getMobileTelBybc(map);
			if (list != null && list.size() > 0) {
				MobileTel mobieTel = (MobileTel) list.get(0);
				if (tcgdf != null) {
					mobieTel.setTcgdf(new BigDecimal(tcgdf));
				}
				if (tcwyytxf != null) {
					mobieTel.setTcwyytxf(new BigDecimal(tcwyytxf));
				}
				if (tryf != null) {
					mobieTel.setTryf(new BigDecimal(tryf));
				}
				if (tcwdxf != null) {
					mobieTel.setTcwdxf(new BigDecimal(tcwdxf));
				}
				mobieTel.setcAllPay(new BigDecimal(cAllPay));
				service.getMobileTelService().update(mobieTel);
			} else {
				MobileTel mobieTel = new MobileTel();
				// MobileTel mobieTel = getProjectPay(doc, mt);
				UUID uuid = UUID.randomUUID();
				mobieTel.setId(uuid.toString());
				// mobieTel.setBaseUserId(currentUser);
				mobieTel.setcTime(t2);
				mobieTel.setTeleno(login.getLoginName());
				if (tcgdf != null) {
					mobieTel.setTcgdf(new BigDecimal(tcgdf));
				}
				if (tcwyytxf != null) {
					mobieTel.setTcwyytxf(new BigDecimal(tcwyytxf));
				}
				if (tryf != null) {
					mobieTel.setTryf(new BigDecimal(tryf));
				}
				if (tcwdxf != null) {
					mobieTel.setTcwdxf(new BigDecimal(tcwdxf));
				}
				mobieTel.setcAllPay(new BigDecimal(cAllPay));
				mobieTel.setDependCycle(cycle);
				mobieTel.setIscm(1);
				service.getMobileTelService().saveMobileTel(mobieTel);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 生成话费对象，进行保存入库
	 * 
	 * @param phonePage11
	 */
	private void generateHuaFei(String phonePage11, String month) {
		Gson gson = new Gson();
		LinkedTreeMap obj = gson.fromJson(phonePage11,
				new TypeToken<LinkedTreeMap>() {
				}.getType());
		obj.get("cummIntegral");
		LinkedTreeMap map = (LinkedTreeMap) obj.get("billMainAreaVo");
		map.get("mainList");
		List list = (List) map.get("mainList");
		LinkedTreeMap map1 = (LinkedTreeMap) (list.get(0));
		map1.get("BILL_ITEM_FEE");
		LinkedTreeMap map2 = (LinkedTreeMap) (list.get(1));
		map2.get("BILL_ITEM_FEE");
		LinkedTreeMap feeGatherMap = (LinkedTreeMap) obj.get("feeGatherVo");
		LinkedTreeMap feeMxVo = (LinkedTreeMap) obj.get("feeMxVo");
		LinkedTreeMap linkMap1 = null;
		String bendi = "0";
		String changtu = "0";
		if (feeMxVo.get("GSM_LIST")!=null) {
			List GSM_LIST = (List) feeMxVo.get("GSM_LIST");
			for (int i = 0; i < GSM_LIST.size(); i++) {
				linkMap1 = (LinkedTreeMap) GSM_LIST.get(i);
				if(linkMap1.get("BILL_ITEM_FEE")!=null&&linkMap1.get("BILL_ITEM_NAME").toString().contains("本地")){
					bendi = linkMap1.get("BILL_ITEM_FEE").toString();
				}
				if(linkMap1.get("BILL_ITEM_FEE")!=null&&linkMap1.get("BILL_ITEM_NAME").toString().contains("长途")){
					changtu = linkMap1.get("BILL_ITEM_FEE").toString();
				}
				
			}
			 
			List SMS_MMS_LIST = (List) feeMxVo.get("SMS_MMS_LIST");
		}
		
		// LinkedTreeMap map3 = (LinkedTreeMap) SMS_MMS_LIST.get(0);
		// LinkedTreeMap map4=(LinkedTreeMap) SMS_MMS_LIST.get(1);
		redismap.put(name_key, obj.get("CUST_NAME"));// 计费周期
		String dependCycle = (String) obj.get("CYCLE");// 计费周期
		String teleno = (String) obj.get("DEVNO");// 手机号
		BigDecimal cAllPay = BigDecimal.valueOf(Double
				.parseDouble((String) map1.get("BILL_ITEM_FEE"))); // 本期总费用
		// :
		BigDecimal cAllBalance = BigDecimal.valueOf(Double
				.parseDouble((String) map2.get("BILL_ITEM_FEE"))); // 话费账户余额:
		BigDecimal tcgdf = BigDecimal.valueOf(Double
				.parseDouble((String) feeGatherMap.get("FEE_BASE")));// 套餐及固定费
		BigDecimal tcwdxf = BigDecimal.valueOf(Double
				.parseDouble((String) feeGatherMap.get("FEE_SMS_MMS")));// 套餐外短彩信费
		BigDecimal tcwyytxf = BigDecimal.valueOf(Double
				.parseDouble((String) feeGatherMap.get("FEE_GSM")));// 套餐外语音通信费
		BigDecimal tryf = BigDecimal.valueOf(Double
				.parseDouble((String) feeGatherMap.get("FEE_DAISHOU")));// 他人（单位）代付
		BigDecimal ctth = BigDecimal.valueOf(Double
				.parseDouble(changtu));// (mine:国内长途费用)
		// 长途通话费（拨打内地）
		// ￥0.21
		BigDecimal bdth = BigDecimal.valueOf(Double
				.parseDouble(bendi));// 本地通话费

		Date t =  DateUtils.StringToDate(month, "yyyy.MM");
		Map map21 = new HashMap();
		map21.put("phone", redismap.get("phone").toString());
		map21.put("cTime", t);
		List list2 = service.getMobileTelService().getMobileTelBybc(map21);
		if (list2 != null && list2.size() > 0) {
			MobileTel mobieTel = (MobileTel) list2.get(0);
			mobieTel.setcAllPay(cAllPay);
			mobieTel.setcAllBalance(cAllBalance);
			mobieTel.setTcgdf(tcgdf);
			mobieTel.setTcwdxf(tcwdxf);
			mobieTel.setTcwyytxf(tcwyytxf);
			mobieTel.setTryf(tryf);
			mobieTel.setDependCycle(dependCycle);
			mobieTel.setCtth(ctth);
			mobieTel.setBdth(bdth);
			service.getMobileTelService().update(mobieTel);
		} else {
			MobileTel mobieTel = new MobileTel();
			UUID uuid = UUID.randomUUID();
			mobieTel.setId(uuid.toString());
			// mobieTel.setBaseUserId(currentUser);
			mobieTel.setcTime(t);
			mobieTel.setcAllPay(cAllPay);
			mobieTel.setcAllBalance(cAllBalance);
			mobieTel.setTcgdf(tcgdf);
			mobieTel.setTcwdxf(tcwdxf);
			mobieTel.setTcwyytxf(tcwyytxf);
			mobieTel.setTryf(tryf);
			mobieTel.setDependCycle(dependCycle);
			mobieTel.setCtth(ctth);
			mobieTel.setBdth(bdth);
			mobieTel.setTeleno(teleno);
			service.getMobileTelService().saveMobileTel(mobieTel);
		}

	}

	/**
	 * 获取每个月的话费
	 * 
	 * @param month
	 * @param nextUrl
	 * @return
	 */
	private String oneMonthHuaFei(String month, String nextUrl) {
		String phonePage11;
		String[] array01;
		String[] array02;
		String timer;
		String livel;
		String ssoSessionId = redismap.get("ssoSessionID").toString();
		cutil.get("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/Inner_Interface_Business.jsp?RelayState=InnerInterFaceCiisHisBill&ssoSessionID=="
				+ ssoSessionId);
		// 登陆了一次，服务器那边的session会记录，一段时间内是允许登陆的
		CHeader c = new CHeader();
		c.setX_requested_with(true);
		phonePage11 = cutil
				.get("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/bill/userbill.do?act=and&Month="
						+ month + "&ssoSessionID=" + ssoSessionId, c);
		// 获取关键参数：data:{timer:"26",ssoSessionID:"2c9d82fb460c43430146912237ee6132",Month:"2014.04",livel:""},
		array01 = phonePage11.split("timer:");
		array02 = phonePage11.split("livel:");
		timer = "";
		livel = "";
		if (array01 != null && array01.length > 1 && array02 != null) {
			timer = array01[1].split(",")[0];
			livel = array02[1].split("}")[0];
		}

		phonePage11 = cutil.get(
				"https://cmodsvr1.bj.chinamobile.com/PortalCMOD/bill/userbilland.do?livel="
						+ livel + "&timer=" + timer + "&Month=" + month
						+ "&livel=ssoSessionID==" + ssoSessionId, c);
		// System.out.println(phonePage11);// 获取话费记录成功!!
		generateHuaFei(phonePage11, month);
		return phonePage11;
	}
	/*
	 * 电话账单
	 */
	@Override
	public LinkedList<MonthlyBillMark> gatherMonthlyBill() {
		return null;
	}

	/**
	 * 1.第一个线程采集
	 * 2.第二个线程采集
	 * @param type
	 */
	public void startSpider(int type) {
		switch (type) {
		case 1:
			getTelDetailHtml();// 账单
			saveUser(); // 个人信息
			addTask(2);
			addTask(3);
			addTask(4);
			break;
		case 2:
			saveCallHistory(); // 历史账单
		case 3:
			saveMessage();// 短信记录
		case 4:
			saveFlowBill();
			saveFlowList();
			break;
		default:
			break;
		}
	}

	public int getTelDetailHtml() {

		String ssoSessionId = redismap.get("ssoSessionID").toString();
		try {

			String html11 = cutil
					.get("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/InnerInterFaceBusiness?searchType=HisBill&Month=2011.04&ssoSessionID="
							+ ssoSessionId);
			// String html11 = executeGet(redismap,
			// "https://cmodsvr1.bj.chinamobile.com/PortalCMOD/InnerInterFaceBusiness?searchType=HisBill&Month=2011.04&ssoSessionID="+ssoSessionId,
			// "https://cmodsvr1.bj.chinamobile.com/PortalCMOD/InnerInterFaceBusiness?searchType=HisBill&Month=2014.04&ssoSessionID="+
			// ssoSessionId);
			// 加载话费记录页面,实际上加载iframe，在这里访问iframe的引用地址会连续重定向两次
			Document doc = Jsoup.parse(html11);
			String nextUrl = doc.select("form").attr("action");
			String SAMLRequest = doc.select("input[name=SAMLRequest]").val();
			String RelayState = doc.select("input[name=RelayState]").val();

			Map<String, String> params = new HashMap<String, String>();
			params.put("SAMLRequest", SAMLRequest);
			params.put("RelayState", RelayState);
			// HttpResponse response = CUtil.getPost(nextUrl, params);
			String phonePage = cutil.post(nextUrl, params);
			doc = Jsoup.parse(phonePage);
			nextUrl = doc.select("form").attr("action");
			String SAMLart = doc.select("input[name=SAMLart]").val();
			RelayState = doc.select("input[name=RelayState]").val();

			params = new HashMap<String, String>();
			params.put("ssoSessionID", ssoSessionId);
			params.put("SAMLart", SAMLart);
			params.put("RelayState", RelayState);
			cutil.post(nextUrl, params);
			// 获取当月，实时查询。从每个月3号才提供该查询服务TODO 没判断
			currentMontHuaFei(nextUrl);
			// 获取历史记录，前5个月
			java.text.DateFormat format2 = new java.text.SimpleDateFormat(
					"yyyy.MM");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			for (int i = 0; i < 6; i++) {
				try {
					calendar.add(Calendar.MONTH, -1);
					Date date = calendar.getTime();
					String date2 = format2.format(date);
					oneMonthHuaFei(date2, nextUrl);
				} catch (Exception e) {
					log4j.writeLogByZhangdan(e);
				}
			}
			return 1;
		} catch (Exception e) {
			log4j.writeLogByZhangdan(e);
		}
		return 0;
	}
	@Override
	public LinkedList<MobileDetail> gatherCallHistory() throws Exception {
		List<String> ms = DateUtils.getMonths(7,"yyyy.MM");
		Date bigTime = getMaxCallTime();
		Map<String, String> params = new HashMap<String, String>();
		params.put("searchType", "HisDetail");
		params.put("detailType", "rc");
		params.put("password", login.getPassword());//----------如果有短信验证需要对password进行保存切记!
		
		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,params,"gsm");
		LinkedList<MobileDetail>  listData = null;
		LinkedList<MobileDetail>  list =  new LinkedList<MobileDetail>();
		String string = null;
		if(linkedList!=null){
			//从最小的月份开始解析
			for (int j = linkedList.size()-1; j>=0;j--) {
			  string  = linkedList.get(j);
			  listData = gatherCallHistory_parse(string,bigTime,ms.get(j));
			  list.addAll(listData);
			}
		}
		return list;
	}
	/**数据从小到大正序存放
	 * @param text
	 * @param bigDate
	 * @param starDate
	 * @return
	 */
	public LinkedList<MobileDetail> gatherCallHistory_parse(String text,Date bigDate,String starDate){
		LinkedList<MobileDetail> listData = new LinkedList<MobileDetail>();
		Document doc7 = Jsoup.parse(text);
		Elements tables = doc7.select("table[id=tableId]");
		if (tables != null && tables.size() > 0) {
			try{
			Elements trs = tables.get(0).select("tr");
				for (int j = 0; j < trs.size(); j++) {
				Elements tds = trs.get(j).select("td");
				if (tds.size() > 9) {
					String qssj = tds.get(0).text();
					String txdd = tds.get(1).text();
					String txfs = tds.get(2).text();
					String dfhm = tds.get(3).text();
					String txsc = tds.get(4).text();
					String txlx = tds.get(5).text();
					String ywmc = tds.get(6).text();
					String tcyh = tds.get(7).text();
					String txf = tds.get(8).text();
					MobileDetail mDetail = new MobileDetail();
					int times = 0;
					try {
						TimeUtil tunit = new TimeUtil();
						times = tunit.timetoint(txsc);
					} catch (Exception e) {
					}
					UUID uuid = UUID.randomUUID();
					mDetail.setId(uuid.toString());
					mDetail.setcTime( DateUtils.StringToDate(starDate.substring(0, 4) + "-" + qssj,
							"yyyy-MM-dd HH:mm:ss"));
					if(isContinue(bigDate,mDetail.getcTime())){
						continue;
					}
					mDetail.setTradeAddr(txdd);
					mDetail.setTradeWay(txfs);
					mDetail.setRecevierPhone(dfhm);
					mDetail.setTradeTime(times);
					mDetail.setTradeType(txlx);
					mDetail.setTaocan(tcyh);
					mDetail.setOnlinePay(new BigDecimal(txf));
					mDetail.setPhone(login.getLoginName());
					listData.add(mDetail);
				}
				}
			} catch (Exception e) {
				log4j.writeLogByHistory(e);
			}
		}
		return listData;
	}
	
	/**
	 * @param ms  月份
	 * @param bigTime  最大时间
	 * @param dxMap2  参数
	 * @return  结果集字符串 按照从小的到大的顺序  如 2014 4.1-2014 5.1
	 * @throws Exception
	 */
	public LinkedList<String> gatherSpiderStrings(List<String> ms,Date bigTime,Map<String, String> params,String detailType) throws Exception{
		String ssoSessionId = redismap.get("ssoSessionID").toString();
//		List<SendRequestPojo> listPojo  = new LinkedList<SendRequestPojo>();
		Date d = null;
		Map<String,String> pararm = null;
	//-------------------------多线程调用------------	
//		for (int i = 0; i < ms.size(); i++) {
//			if(i!=0){
//				//如果当前最大时间比上一次采集的月份还要大,就终止,因为可以理解为后边已经采集了
//				d = DateUtils.StringToDate(ms.get(i-1), "yyyy.MM");
//				if(bigTime!=null&&bigTime.getTime()>=d.getTime()){
//					break;
//				}
//			}
//			pararm = new HashMap<String, String>(params);
//			pararm.put("checkMonth", ms.get(i));
//			listPojo.add(new SendRequestPojo("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/detail/detail.do?checkMonth="
//					+ ms.get(i) + "&detailType="+detailType+"&ssoSessionID="+ ssoSessionId,pararm)); //添加任务
//		}
//		//集合线程装载的结果为从大到小,解析时转换
//		return cutil.getResult(listPojo);
		LinkedList<String> list = new LinkedList<String>();
		for (int i = 0; i < ms.size(); i++) {
			if(i!=0){
				//如果当前最大时间比上一次采集的月份还要大,就终止,因为可以理解为后边已经采集了
				d = DateUtils.StringToDate(ms.get(i-1), "yyyy.MM");
				if(bigTime!=null&&bigTime.getTime()>=d.getTime()){
					break;
				}
			}
			pararm = new HashMap<String, String>(params);
			pararm.put("checkMonth", ms.get(i));
			list.add(cutil.post("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/detail/detail.do?checkMonth="
					+ ms.get(i) + "&detailType="+detailType+"&ssoSessionID="+ ssoSessionId,pararm)); //添加任务
		}
		return list;
	}

	@Override
	public LinkedList<MobileMessage> gatherMessage() throws Exception {
		List<String> ms =  DateUtils.getMonths(7,"yyyy.MM");
		Date bigTime = getMaxMessageTime();
		Map<String, String> params = new HashMap<String, String>();
		params.put("searchType", "NowDetail");
		params.put("detailType", "sms");
		params.put("password", login.getPassword());
		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,params,"sms");
		LinkedList<MobileMessage>  listData = null;
		LinkedList<MobileMessage>  list =  new LinkedList<MobileMessage>();
		String string;
		if(linkedList!=null){
			//从最小的月份开始解析
			for (int j = linkedList.size()-1; j>=0;j--) {
			  string  = linkedList.get(j);
			  listData = gatherMessageHistory_parse(string,bigTime,ms.get(j));
			  list.addAll(listData);
			}
		}
		return list;
	}
	 public LinkedList<MobileMessage> gatherMessageHistory_parse(String text,Date bigDate,String starDate) {
			LinkedList<MobileMessage> listData = new LinkedList<MobileMessage>();
		    try{
		    	Document doc7 = Jsoup.parse(text);
				Elements tables = doc7.select("table[id=tableId]");
				if (tables != null && tables.size() > 0) {
					Elements trs = tables.get(0).select("tr");
					for (int j = 0; j < trs.size(); j++) {
						Elements tds = trs.get(j).select("td");
						if (tds.size() > 8) {
							String qssj = tds.get(0).text();
							String txdd = tds.get(1).text();
							String dfhm = tds.get(2).text();
							String txfs = tds.get(3).text();
							String xxlx = tds.get(4).text();
							String ywmc = tds.get(5).text();
							String tcyh = tds.get(6).text();
							String thf = tds.get(7).text();
							MobileMessage mbmessage = new MobileMessage();

							UUID uuid = UUID.randomUUID();
							mbmessage.setId(uuid.toString());
							mbmessage.setCreateTs(new Date());
							mbmessage.setSentTime( DateUtils.StringToDate(
									starDate.substring(0, 4) + "-" + qssj,
									"yyyy-MM-dd HH:mm:ss"));
							if(isContinue(bigDate,mbmessage.getSentTime())){
								continue;
							}

							mbmessage.setSentAddr(txdd);
							mbmessage.setTradeWay(txfs);
							mbmessage.setRecevierPhone(dfhm);
							mbmessage.setAllPay(new BigDecimal(thf));
							mbmessage.setPhone(login.getLoginName());
							listData.add(mbmessage);
						}
					}
				}
			}catch(Exception e){
				log4j.writeLogByMessage(e);
			}
		 return listData;
	   }

	
	public User gatherUser() {
		String myinfo = cutil
				.get("http://www.bj.10086.cn/www/servletfuwuhfnew");// gbk
		String yue = myinfo.split(" ")[0].split("err")[0];
		User user = new User();
		user.setLoginName(login.getLoginName());
		user.setUserName(redismap.getString(name_key));
		try {
			user.setPhoneRemain(new BigDecimal(yue));
		} catch (Exception e) {
			user.setPhoneRemain(new BigDecimal(0.0));
		}
		user.setEmail("");
		user.setRealName(redismap.getString(name_key));
		return user;
	
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
	
	

	
	private Long addFlow(String flow){
		Long l0 = new Long(0);
		Long l1 = new Long(0);
		Long l2 = new Long(0);

		if(flow == null || "".equals(flow)){
			return new Long(0);
		}
		if(flow.contains("G")){
			String [] aa = flow.split("G");
			l0 = Long.parseLong(aa[0]) * 1024 * 1024;
			if(l0 < 0){
				if(flow.contains("M")){
					String [] bb = aa[1].split("M");
					l1 = Long.parseLong("-" + bb[0]) * 1024;
					l2 = Long.parseLong("-" + bb[1]);
				} else {
					l2 = Long.parseLong("-" + aa[1]);
				}
			} else {
				if(flow.contains("M")){
					String [] bb = aa[1].split("M");
					l1 = Long.parseLong(bb[0]) * 1024;
					l2 = Long.parseLong(bb[1]);
				} else {
					l2 = Long.parseLong(aa[1]);
				}
				
			}

		}
		if(flow.contains("M")){
			String [] aa = flow.split("M");
			l1 = Long.parseLong(aa[0]) * 1024;
			l2 = Long.parseLong(aa[1]);
			if(l1 < 0){
				l2 = Long.parseLong("-" + aa[1]);
			}
		} else {
			l1 = Long.parseLong(flow);
			
		}
		return l0+l1+l2;
		
	}
    
	private List<String> list = null;
	public void gatherOnlineStrings(List<String> ms ){
		List<SendRequestPojo> pojolist = new ArrayList<SendRequestPojo>();
		for (String string : ms) {
			pojolist.add(new SendRequestPojo("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/detail/detail.do?checkMonth="+ string + "&detailType=gprs"));
		}
		list = cutil.getResult(pojolist);
	}
	@Override
	public LinkedList<MobileOnlineList> gatherFlowList() throws Exception {
		List<String> ms = DateUtils.getMonths(7, "yyyy-MM");
		if(list==null){
			gatherOnlineStrings(ms);
		}
		Date bigTime = getMaxFlowTime();
		LinkedList<MobileOnlineList> listData = new LinkedList<MobileOnlineList>();
		//从小到大遍历
		for (int i = list.size()-1; i>=0; i--) {
		  String html = list.get(i);
		  if(html!=null){
			  try{
			Document doc = Jsoup.parse(html);
			Element table1 = doc.select("table").get(1);
			Elements trs = table1.select("tbody").get(1).select("tr");
			for (int j = 0; j < trs.size(); j++) {
				Elements tds = trs.get(j).select("td");
				if (tds.size() >= 8) {
					String ctime = tds.get(0).text().trim()
							.replaceAll(" ", "");
					String tradeAddr = tds.get(1).text().trim()
							.replaceAll(" ", "");
					String onlineType = tds.get(2).text().trim()
							.replaceAll(" ", "");
					String onlineTime = tds.get(3).text().trim()
							.replaceAll(" ", "");
					if(onlineTime == null || "".equals(onlineTime)){
						onlineTime = "0";
					}
					
					Long time = new Long(0);
					String[] time1 = null ;
					String[] time2 = null;
					String[] time3;
//					if (onlineTime.contains("时")) {
//						time1 = onlineTime.split("时");
//						time = Long.parseLong(time1[0]) * 60 * 60;
//						if (onlineTime.contains("分")) {
//							time2 = time1[1].split("分");
//							time = time + Long.parseLong(time2[0]) * 60;
//							if (onlineTime.contains("秒")) {
//								time3 = time2[1].split("秒");
//								time = time + Long.parseLong(time3[0]);
//							} else {
//								time = time + Long.parseLong(time2[1]);
//							}
//						}
//					} else if(onlineTime.contains("分")){
//							time2 = onlineTime.split("分");
//							time = time + Long.parseLong(time2[0]) * 60;
//							if(onlineTime.contains("秒")){
//								time3 = time2[1].split("秒");
//								time = time + Long.parseLong(time3[0]);
//							} else {
//								time = time + Long.parseLong(time2[1]);
//							}
//					} else if(onlineTime.contains("秒")){
//							time3 = onlineTime.split("秒");
//							time = Long.parseLong(time3[0]);
//					} else {
//						time = Long.parseLong(onlineTime);
//					}
					time = StringUtil.flowTimeFormat(onlineTime);

					String totalFlow = tds.get(4).text().trim()
							.replaceAll(" ", "").replaceAll("秒", "");
					String cheapService = tds.get(5).text().trim()
							.replaceAll(" ", "");
					String communicationFees = tds.get(6).text().trim()
							.replaceAll(" ", "");
					MobileOnlineList mbOnline = new MobileOnlineList();
					UUID uuid = UUID.randomUUID();
					mbOnline.setId(uuid.toString());
					mbOnline.setPhone(login.getLoginName());

					Calendar cal = Calendar.getInstance();
					int currentyear = cal.get(Calendar.YEAR);
					mbOnline.setcTime( DateUtils.StringToDate(currentyear + "-" + ctime,
							"yyyy-MM-dd HH:mm:ss"));
					if(isContinue(bigTime,mbOnline.getcTime())){
						continue;
					}
					mbOnline.setTradeAddr(tradeAddr);
					mbOnline.setOnlineType(onlineType);
					mbOnline.setOnlineTime(time);
					mbOnline.setTotalFlow(Long.parseLong(totalFlow));
					mbOnline.setCheapService(cheapService);
					mbOnline.setCommunicationFees(BigDecimal.valueOf(Double
							.parseDouble(communicationFees)));
					listData.add(mbOnline);
				}
			}
			  }catch(Exception e){
				  log4j.writeLogByFlowList(e);
			  }
		  }
		}
		return listData;
	}
	/**
	 * 存放结果
	 * @param ms
	 * @return
	 */
	public LinkedList<MonthlyBillMark> getLinkedList(List<String> ms){
		LinkedList<MonthlyBillMark> listMark = getSpiderMonthsMark(true, "yyyy-MM",7,1);
		MonthlyBillMark mark = null;
		for (int i = 0; i < ms.size(); i++) {
			for (int j = 0; j < listMark.size(); j++) {
				mark = listMark.get(j); 
				if(mark.getMonth().equals(ms.get(i))){
					mark.setText(list.get(i));
					break;
				}
			}
		}
		return listMark;
	}
	@Override
	public LinkedList<MonthlyBillMark> gatherFlowBill() {
		List<String> ms = DateUtils.getMonths(7, "yyyy-MM");
		if(list==null){
			gatherOnlineStrings(ms);
		}
		LinkedList<MonthlyBillMark> listMark = getLinkedList(ms);
		MonthlyBillMark mark = null;
		String html = null;
		for (int i = 0; i < listMark.size(); i++) {
			mark = listMark.get(i);
			 html = mark.getText();
			 try{
			if(html!=null){
				Document doc = Jsoup.parse(html);
				Element table1 = doc.select("table").get(0);
				Elements tds = table1.select("tr").get(0).select("td");

				String total = null;
				String free = null;
				String charge = null;
				String fees = null;

				for (int j = 0; j < tds.size(); j++) {
					if (j == 3) {
						total = tds.get(0).select("span").get(0).text().trim().replaceAll(" ", "");
						free = tds.get(1).select("span").get(0).text().trim().replaceAll(" ", "");
						charge = tds.get(2).select("span").get(0).text().trim().replaceAll(" ", "");
						fees = tds.get(3).select("span").get(0).text().trim().replaceAll(" ", "");

						RegexPaserUtil rp = new RegexPaserUtil("数据总流量", "KB",
								total, RegexPaserUtil.TEXTEGEXANDNRT);
						total = rp.getText();
						rp = new RegexPaserUtil("免费数据流量", "KB", free,
								RegexPaserUtil.TEXTEGEXANDNRT);
						free = rp.getText();
						rp = new RegexPaserUtil("收费数据流量", "KB", charge,
								RegexPaserUtil.TEXTEGEXANDNRT);
						charge = rp.getText();
						if(charge!=null){
							charge = charge.replace("-", "");
						}
						rp = new RegexPaserUtil("数据流量费用", "元", fees,
								RegexPaserUtil.TEXTEGEXANDNRT);
						fees = rp.getText();

						MobileOnlineBill mOnlineBill = new MobileOnlineBill();
						mOnlineBill.setPhone(login.getLoginName());
						mOnlineBill.setTotalFlow(addFlow(total));
						mOnlineBill.setFreeFlow(addFlow(free));
						mOnlineBill.setChargeFlow(addFlow(charge));

						mOnlineBill.setTrafficCharges(BigDecimal.valueOf(Double
								.parseDouble(fees)));

						mOnlineBill.setMonthly( DateUtils.StringToDate(mark.getMonth()
								+ "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
						mark.setObj(mOnlineBill);
					}
				}
			}
			 }catch(Exception e){
				 log4j.writeLogByFlowBill(e);
			 }
		}
		return listMark;
	}
	
//	/**
//	 * 查询个人信息
//	 */
//	public int getMyInfo() {
//
//		try {
//			String tr = InfoUtil.getInstance().getInfo("yd/jx", "tr");
//			String td = InfoUtil.getInstance().getInfo("yd/jx", "td");
//			String table = InfoUtil.getInstance().getInfo("yd/jx", "table");
//
//			String myinfo = cutil
//					.get("http://www.bj.10086.cn/www/servletfuwuhfnew");// gbk
//			String yue = myinfo.split(" ")[0].split("err")[0];
//
//			Map<String, String> map = new HashMap<String, String>(2);
//			map.put("parentId", currentUser);
//			map.put("usersource", Constant.YIDONG);
//			map.put("loginName", login.getLoginName());
//			List<User> list = userService.getUserByParentIdSource(map);
//			if (list != null && list.size() > 0) {
//				User user = list.get(0);
//				user.setLoginName(login.getLoginName());
//				user.setLoginPassword("");
//				user.setUserName(CUST_NAME);
//				try {
//					user.setPhoneRemain(new BigDecimal(yue));
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//				user.setEmail("");
//				user.setRealName(CUST_NAME);
//				user.setIdcard("");
//				user.setAddr("");
//				user.setUsersource(Constant.YIDONG);
//				user.setUsersource2(Constant.YIDONG);
//				user.setParentId(currentUser);
//				user.setModifyDate(new Date());
//				user.setPhone(login.getLoginName());
//				user.setPackageName("");
//				userService.update(user);
//			} else {
//
//				User user = new User();
//				UUID uuid = UUID.randomUUID();
//				user.setId(uuid.toString());
//				user.setLoginName(login.getLoginName());
//				user.setLoginPassword("");
//				user.setUserName(CUST_NAME);
//				try {
//					user.setPhoneRemain(new BigDecimal(yue));
//				} catch (Exception e) {
//					user.setPhoneRemain(new BigDecimal(0.0));
//				}
//
//				user.setEmail("");
//				user.setRealName(CUST_NAME);
//				// user.setIdcard(idcard);
//				// user.setAddr(addr);
//				user.setUsersource(Constant.YIDONG);
//				user.setUsersource2(Constant.YIDONG);
//				user.setParentId(currentUser);
//				user.setModifyDate(new Date());
//				user.setPhone(login.getLoginName());
//				// user.setPackageName(pplx);
//				userService.saveUser(user);
//
//			}
//			return 1;
//		} catch (Exception e) {
//			e.printStackTrace();
//			writeLogByInfo(e);
//		}
//
//		return 0;
//	}
//	public int callHistory() {
//		List<MobileDetail> detailList = new ArrayList<MobileDetail>();
//		try {
//			String ssoSessionId = redismap.get("ssoSessionID").toString();
//
//			List<String> ms = DateUtils.getMonths(7, "yyyy.MM");
//			MobileDetail md = new MobileDetail();
//			md.setPhone(login.getLoginName());
//			md = service.getMobileDetailService().getMaxTime(md);
//			boolean b = true;
//			for (int m = 0; m < ms.size(); m++) {
//				String starDate = ms.get(m);
//				// 获取通话记录 TODO 1,没解析，数据在获取的页面上,2，没有获取更多月份信息
//				// searchType=HisDetail&detailType=rc&checkMonth=2014.05&password=42938127
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("searchType", "HisDetail");
//				params.put("detailType", "rc");
//				params.put("checkMonth", starDate);
//				params.put("password", login.getPassword());
//				String tonghua = cutil.post(
//						"https://cmodsvr1.bj.chinamobile.com/PortalCMOD/detail/detail.do?checkMonth="
//								+ starDate + "&detailType=gsm&ssoSessionID="
//								+ ssoSessionId, params);
//				Document doc7 = Jsoup.parse(tonghua);
//				Elements tables = doc7.select("table[id=tableId]");
//				if (tables != null && tables.size() > 0) {
//					Elements trs = tables.get(0).select("tr");
//					for (int j = 0; j < trs.size(); j++) {
//						Elements tds = trs.get(j).select("td");
//						if (tds.size() > 9) {
//							String qssj = tds.get(0).text();
//							String txdd = tds.get(1).text();
//							String txfs = tds.get(2).text();
//							String dfhm = tds.get(3).text();
//							String txsc = tds.get(4).text();
//							String txlx = tds.get(5).text();
//							String ywmc = tds.get(6).text();
//							String tcyh = tds.get(7).text();
//							String txf = tds.get(8).text();
//							MobileDetail mDetail = new MobileDetail();
//
//							int times = 0;
//							try {
//								TimeUtil tunit = new TimeUtil();
//								times = tunit.timetoint(txsc);
//							} catch (Exception e) {
//
//							}
//
//							UUID uuid = UUID.randomUUID();
//							mDetail.setId(uuid.toString());
//							mDetail.setcTime( DateUtils.StringToDate(
//									starDate.substring(0, 4) + "-" + qssj,
//									"yyyy-MM-dd HH:mm:ss"));
//							if (md != null && md.getcTime() != null) {
//								if (mDetail.getcTime().getTime() <= md
//										.getcTime().getTime()) {
//									b = false;
//									break;
//								}
//							}
//							mDetail.setTradeAddr(txdd);
//							mDetail.setTradeWay(txfs);
//							mDetail.setRecevierPhone(dfhm);
//							mDetail.setTradeTime(times);
//							mDetail.setTradeType(txlx);
//							mDetail.setTaocan(tcyh);
//							mDetail.setOnlinePay(new BigDecimal(txf));
//							mDetail.setPhone(login.getLoginName());
//
//							mDetail.setIscm(0);
//
//							detailList.add(mDetail);
//							// mobileDetailService.saveMobileDetail(mDetail);
//						}
//					}
//				}
//
//				if (!b) {
//					break;
//				}
//			}
//			service.getMobileDetailService().insertbatch(detailList);
//			return 1;
//		} catch (Exception e) {
//			log4j.writeLogByHistory(e);
//		}
//		service.getMobileDetailService().insertbatch(detailList);
//		return 0;
//	}
//	public int messageHistory() {
//		List<MobileMessage> messageList = new ArrayList<MobileMessage>();
//		try {
//			String ssoSessionId = redismap.get("ssoSessionID").toString();
//
//			List<String> ms = DateUtils.getMonths(7, "yyyy.MM");
//			MobileMessage md = new MobileMessage();
//			md.setPhone(login.getLoginName());
//			md = service.getMobileMessageService().getMaxSentTime(md.getPhone());
//			boolean b = true;
//
//			for (int m = 0; m < ms.size(); m++) {
//				String starDate = ms.get(m);
//				// 获取通话记录 TODO 1,没解析，数据在获取的页面上,2，没有获取更多月份信息
//				// searchType=HisDetail&detailType=rc&checkMonth=2014.05&password=42938127
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("searchType", "NowDetail");
//				params.put("detailType", "sms");
//				params.put("checkMonth", starDate);
//				params.put("password", login.getPassword());
//				String duanxin = cutil.post(
//						"https://cmodsvr1.bj.chinamobile.com/PortalCMOD/detail/detail.do?checkMonth="
//								+ starDate + "&detailType=sms&ssoSessionID="
//								+ ssoSessionId, params);
//				Document doc7 = Jsoup.parse(duanxin);
//				Elements tables = doc7.select("table[id=tableId]");
//				if (tables != null && tables.size() > 0) {
//					Elements trs = tables.get(0).select("tr");
//					for (int j = 0; j < trs.size(); j++) {
//						Elements tds = trs.get(j).select("td");
//						if (tds.size() > 8) {
//							String qssj = tds.get(0).text();
//							String txdd = tds.get(1).text();
//							String dfhm = tds.get(2).text();
//							String txfs = tds.get(3).text();
//							String xxlx = tds.get(4).text();
//							String ywmc = tds.get(5).text();
//							String tcyh = tds.get(6).text();
//							String thf = tds.get(7).text();
//							MobileMessage mbmessage = new MobileMessage();
//
//							// int times = 0;
//							// try{
//							// TimeUtil tunit = new TimeUtil();
//							// times = tunit.timetoint(txsc);
//							// }catch(Exception e){
//							//
//							// }
//							UUID uuid = UUID.randomUUID();
//							mbmessage.setId(uuid.toString());
//							mbmessage.setCreateTs(new Date());
//							mbmessage.setSentTime( DateUtils.StringToDate(
//									starDate.substring(0, 4) + "-" + qssj,
//									"yyyy-MM-dd HH:mm:ss"));
//							if (md != null
//									&& !md.getSentTime().before(
//											mbmessage.getSentTime())) {
//								b = false;
//								break;
//							}
//
//							mbmessage.setSentAddr(txdd);
//							mbmessage.setTradeWay(txfs);
//							mbmessage.setRecevierPhone(dfhm);
//							mbmessage.setAllPay(new BigDecimal(thf));
//							mbmessage.setPhone(login.getLoginName());
//							messageList.add(mbmessage);
//							// mobileMessageService.save(mbmessage);
//						}
//					}
//				}
//				if (!b) {
//					break;
//				}
//			}
//			service.getMobileMessageService().insertbatch(messageList);
//			return 1;
//		} catch (Exception e) {
//			log4j.writeLogByMessage(e);
//		}
//		service.getMobileMessageService().insertbatch(messageList);
//		return 0;
//	}
//	/**
//	 * 手机上网详单
//	 * @return int
//	 */
//	public int callOnlineList(LinkedList<String> linkList) {
//		List<MobileOnlineList> list = new ArrayList<MobileOnlineList>();
//
//		try {
//			for (String html : linkList) {
//
//				Document doc = Jsoup.parse(html);
//				Element table1 = doc.select("table").get(1);
//				Elements trs = table1.select("tbody").get(1).select("tr");
//				for (int j = 0; j < trs.size(); j++) {
//					Elements tds = trs.get(j).select("td");
//					if (tds.size() >= 8) {
//						String ctime = tds.get(0).text().trim()
//								.replaceAll(" ", "");
//						String tradeAddr = tds.get(1).text().trim()
//								.replaceAll(" ", "");
//						String onlineType = tds.get(2).text().trim()
//								.replaceAll(" ", "");
//						String onlineTime = tds.get(3).text().trim()
//								.replaceAll(" ", "");
//						if(onlineTime == null || "".equals(onlineTime)){
//							onlineTime = "0";
//						}
//						
//						Long time = new Long(0);
//						String[] time1 = null ;
//						String[] time2 = null;
//						String[] time3;
//						if (onlineTime.contains("时")) {
//							time1 = onlineTime.split("时");
//							time = Long.parseLong(time1[0]) * 60 * 60;
//							if (onlineTime.contains("分")) {
//								time2 = time1[1].split("分");
//								time = time + Long.parseLong(time2[0]) * 60;
//								if (onlineTime.contains("秒")) {
//									time3 = time2[1].split("秒");
//									time = time + Long.parseLong(time3[0]);
//								} else {
//									time = time + Long.parseLong(time2[1]);
//								}
//							}
//						} else if(onlineTime.contains("分")){
//								time2 = onlineTime.split("分");
//								time = time + Long.parseLong(time2[0]) * 60;
//								if(onlineTime.contains("秒")){
//									time3 = time2[1].split("秒");
//									time = time + Long.parseLong(time3[0]);
//								} else {
//									time = time + Long.parseLong(time2[1]);
//								}
//						} else if(onlineTime.contains("秒")){
//								time3 = onlineTime.split("秒");
//								time = Long.parseLong(time3[0]);
//						} else {
//							time = Long.parseLong(onlineTime);
//						}
//
//						String totalFlow = tds.get(4).text().trim()
//								.replaceAll(" ", "");
//						String cheapService = tds.get(5).text().trim()
//								.replaceAll(" ", "");
//						String communicationFees = tds.get(6).text().trim()
//								.replaceAll(" ", "");
//						MobileOnlineList mbOnline = new MobileOnlineList();
//						UUID uuid = UUID.randomUUID();
//						mbOnline.setId(uuid.toString());
//						mbOnline.setPhone(login.getLoginName());
//
//						Calendar cal = Calendar.getInstance();
//						int currentyear = cal.get(Calendar.YEAR);
//						mbOnline.setcTime( DateUtils.StringToDate(currentyear + "-" + ctime,
//								"yyyy-MM-dd HH:mm:ss"));
//
//						mbOnline.setTradeAddr(tradeAddr);
//						mbOnline.setOnlineType(onlineType);
//						mbOnline.setOnlineTime(time);
//						mbOnline.setTotalFlow(Long.parseLong(totalFlow));
//						mbOnline.setCheapService(cheapService);
//						mbOnline.setCommunicationFees(BigDecimal.valueOf(Double
//								.parseDouble(communicationFees)));
//						list.add(mbOnline);
//					}
//				}
//
//			}
//			service.mobileOnlineListService.insertbatch(list);
//			return 1;
//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO
//			// log
//		}
//		return 0;
//
//	}
//	/**
//	 * 手机上网账单 
//	 * @return int
//	 */
//	public int callOnlineBill() {
//		List<MobileOnlineBill> billList = new ArrayList<MobileOnlineBill>();
//		try {
//			List<String> ms = DateUtils.getMonths(7, "yyyy-MM");
//			LinkedList<String> list = new LinkedList<String>();
//			for (String string : ms) {
//				list.add(cutil
//						.get("https://cmodsvr1.bj.chinamobile.com/PortalCMOD/detail/detail.do?checkMonth="
//								+ string + "&detailType=gprs"));
//			}
//			
//			//调用手机上网详单
//			callOnlineList(list);
//
//			for (int i = 0; i < list.size(); i++) {
//
//				String html = list.get(i);
//
//				Document doc = Jsoup.parse(html);
//				Element table1 = doc.select("table").get(0);
//				Elements tds = table1.select("tr").get(0).select("td");
//
//				String total = null;
//				String free = null;
//				String charge = null;
//				String fees = null;
//
//				for (int j = 0; j < tds.size(); j++) {
//					if (j == 3) {
//						total = tds.get(0).select("span").get(0).text().trim().replaceAll(" ", "");
//						free = tds.get(1).select("span").get(0).text().trim().replaceAll(" ", "");
//						charge = tds.get(2).select("span").get(0).text().trim().replaceAll(" ", "");
//						fees = tds.get(3).select("span").get(0).text().trim().replaceAll(" ", "");
//
//						RegexPaserUtil rp = new RegexPaserUtil("数据总流量", "KB",
//								total, RegexPaserUtil.TEXTEGEXANDNRT);
//						total = rp.getText();
//						rp = new RegexPaserUtil("免费数据流量", "KB", free,
//								RegexPaserUtil.TEXTEGEXANDNRT);
//						free = rp.getText();
//						rp = new RegexPaserUtil("收费数据流量", "KB", charge,
//								RegexPaserUtil.TEXTEGEXANDNRT);
//						charge = rp.getText();
//						rp = new RegexPaserUtil("数据流量费用", "元", fees,
//								RegexPaserUtil.TEXTEGEXANDNRT);
//						fees = rp.getText();
//
//						MobileOnlineBill mOnlineBill = new MobileOnlineBill();
//						UUID uuid = UUID.randomUUID();
//						mOnlineBill.setId(uuid.toString());
//						mOnlineBill.setPhone(login.getLoginName());
//						mOnlineBill.setTotalFlow(addFlow(total));
//						mOnlineBill.setFreeFlow(addFlow(free));
//						mOnlineBill.setChargeFlow(addFlow(charge));
//
//						mOnlineBill.setTrafficCharges(BigDecimal.valueOf(Double
//								.parseDouble(fees)));
//
//						// TODO 当月状态的可以不用
//						// if (ms.get(6).equals(ms.get(i))) {
//						// mOnlineBill.setIscm(1);
//						// } else {
//						// mOnlineBill.setIscm(0);
//						// }
//						//mOnlineBill.setIscm(0);
//						
//						
//						mOnlineBill.setMonthly( DateUtils.StringToDate(ms.get(i)
//								+ "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
//						billList.add(mOnlineBill);
//					}
//				}
//
//			}
//			service.getMobileOnlineBillService().insertbatch(billList);
//			return 1;
//		} catch (Exception e) {
//			e.printStackTrace();
//			//TODO
//			//log
//		}
//		return 0;
//
//	}
	
}
