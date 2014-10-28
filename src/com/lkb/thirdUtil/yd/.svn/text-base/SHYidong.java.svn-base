package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.test.jsdx.RegexPaserUtil;
import com.lkb.thirdUtil.base.BasicCommonMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.entity.CHeader;


public class SHYidong extends BasicCommonMobile{//BaseInfoMobile{
	private static Logger logger = Logger.getLogger(SHYidong.class);
	private static String sendUrl = "https://sh.ac.10086.cn/loginex?iscb=1&act=1&telno=";
	private static String preloginUrl = "https://sh.ac.10086.cn/login";
	// private static String sendUrl2 =
	// "https://sh.ac.10086.cn/loginex?iscb=1&act=1&telno=";
	private static String loginUrl = "https://sh.ac.10086.cn/loginex?act=2&ctype=1";
	private static String confirmUrl = "http://www.sh.10086.cn/sh/wsyyt/busi.json?sid=WF000022&uid=";

	// 个人信息
	private static String identifyUrl = "http://www.sh.10086.cn/sh/wsyyt/action?act=myarea.getinfoManage";

	// 当前这个月的话费信息
	private static String urlCurrentMonth =   "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getFiveBillAll&showType=0&firstPage=y&uniqueKey=15&uniqueName=查询前5个月账单";
	// 之前五个月的话费信息 需要在增加： &dateTime=2013年12月&r=1396837684338
	private static String urlLastMonth = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=FiveBillAllNewAjax&isPriceTaxSeparate=null&showType=0";

	// 当月话费详单
	private static String currentMonthDetail = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getOneBillDetailAjax";
	
	// 当月短信详单
	private static String currentMessageDetail = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getOneBillDetailAjax";
	
	// 某月话费详单
	private static String otherMonthDetail = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getFiveBillDetailAjax";
	
	
	// 查看话费详单的登陆URL
	private static String loginUrl2 = "https://sh.ac.10086.cn/loginex?iscb=1&act=2&authLevel=1&validcode=";
	// 登陆完后
	private static String loginOverPost = "http://www.sh.10086.cn/sh/wsyyt/busi.json?sid=WF000022";

	//验证码URL
	public static String authUrl = "https://sh.ac.10086.cn/validationCode?rnd=";
	
	private MobileOnlineBill thisMonthOnlineBill=new MobileOnlineBill();
	
	public static void main(String[] args) throws Exception {
		SHYidong sh = new SHYidong(new Login("13681633227","198645"),null);
//		sh.index();
//		sh.inputCode(authUrl);
//		sh.logins();
//		if(sh.islogin()){
//			System.out.println(sh.testUserToString());
//			LinkedList<MonthlyBillMark> mark = sh.gatherMonthlyBill();
//			for (int i = 0; i < mark.size(); i++) {
//				System.out.println(mark.get(i).getMobileTel());
//			}
//			sh.sendPhoneDynamicsCode(new ResOutput());
//			sh.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
//			sh.checkPhoneDynamicsCode();
//			System.out.println(sh.getCookie().getCookiesString());
//			LinkedList<MobileMessage> mess = sh.gatherMessage();
//			for (int i = 0; i < mess.size(); i++) {
//				System.out.println(mess.get(i));
//			}
//			LinkedList<MobileDetail> list = sh.gatherCallHistory();
//			for (int i = 0; i < list.size(); i++) {
//				System.out.println(list.get(i));
//			}
//			LinkedList<MobileOnlineList> list1 = sh.gatherFlowList();
//			for (int i = 0; i < list1.size(); i++) {
//				System.out.println(list1.get(i));
//			}
			LinkedList<MonthlyBillMark> mark = sh.gatherFlowBill();
			for (int i = 0; i < mark.size(); i++) {
				System.out.println(mark.get(i).getMobileOnlineBill());
			}
//		}
		
		sh.sendPhoneDynamicsCode();
//

	}
	
	public SHYidong(Login login,String currentUser){
		super(login,currentUser);
		this.userSource = Constant.YIDONG;
		this.constantNum = ConstantNum.comm_sh_yidong;
	}
	
	
	public  void startSpider(int type){
		switch (type) {
		case 1:
			saveTel();//账单详情
			saveUser(); //个人信息
			break;
		case 2:
			saveCallHistory(); //历史账单	
			break;
		case 3:
			saveMessage();//短息记录
			break;
		case 4:
			saveFlowList();//流量详单
			saveFlowBill();//流量账单，必须放在流量详单后面！！因为当月的流量账单要详单累加
			break;
		default:
			break;
		}
	}
	
	
	public void init(){
		if(!context.isInit()){
			 if(cutil.get(preloginUrl)!=null){;
			 context.setImgUrl(authUrl);
			 context.setInit();
			 }
		}
	}
	
//	public void callHistory() {
//		alyzer();
//		alyzer3();
//	}

	// 随机短信登录
	public void checkPhoneDynamicsCode(ResOutput res) throws JSONException {
		String url2 = loginUrl2 + "&telno=" + login.getLoginName() + "&password=" + login.getPhoneCode();
		String text1 = cutil.get(url2);
		text1 = text1.replace("ssoLoginCallback(", "").replace(");", "");
		String uid = "";
		JSONObject json = new JSONObject(text1);
		uid = json.get("uid").toString();
		if (uid != null && uid.trim().length() > 0) {
			Map<String, String> map21 = new HashMap<String, String>();
			map21.put("uid", uid);
			text1 = cutil.post(loginOverPost, map21);
			if(text1!=null){
				res.setStatus(1);
				res.setErrorMsg("验证成功!");
			}else{
				res.setErrorMsg("验证错误!");	
			}
		}else{
			res.setErrorMsg("验证错误!");
		}
		if(res.getStatus()==1){
        	addTask(2);
        	addTask(3);
        	addTask(4);
    	}
	}


	

		private Boolean isDoule(String str) {
			Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
			Boolean flag = pattern.matcher(str.replace("-", "")).matches();
			return flag;
		}

		private MobileTel getProjectPay(Document doc, MobileTel mobileTel) {
			String tr = InfoUtil.getInstance().getInfo("yd/sh",
					"tr");
			String td = InfoUtil.getInstance().getInfo("yd/sh",
					"td");
			Elements lines = doc.select("table[class=cost_detail]")
					.select(tr);
			Elements trs2 = doc.select("table[class=fy_information_ul]")
					.first().select(tr);
			Elements trs3 = doc.select("div[class=detail-panel]").first()
					.select("div[class=detail-table]").first().select("table")
					.first().select(tr);

			lines.addAll(trs2);
			lines.addAll(trs3);
			//System.out.println(lines.html());
			for (int j = 0; j < lines.size(); j++) {
				Elements tds = lines.get(j).select(td);
				Elements ths = lines.get(j).select("th");
				if (ths != null && ths.size() <= 3 && ths.size() >= 2) {
					String key = lines.get(j).select("th").get(0).text();
					String value = lines.get(j).select("th").get(1).text()
							.replaceAll("￥", "");
					if (isDoule(value)) {
						if (key.contains("套餐及固定费")) {
							mobileTel.setTcgdf(new BigDecimal(value));
						} else if (key.contains("套餐外短彩信费")) {
							mobileTel.setTcwdxf(new BigDecimal(value));
						} else if (key.contains("套餐外语音通信费")) {
							mobileTel.setTcwyytxf(new BigDecimal(value));
						} else if (key.contains("他人（单位）代付")) {
							mobileTel.setTryf(new BigDecimal(value));
						} else if (key.contains("IP国内长途定向优")) {
							mobileTel.setIpgnctdx(new BigDecimal(value));
						} else if (key.contains("30天国内短期漫游优惠包功能费")) {
							mobileTel.setSstgndq(new BigDecimal(value));
						} else if (key.contains("动感地带网聊套餐费")) {
							mobileTel.setDgddwl(new BigDecimal(value));
						} else if (key.contains("集团优惠套餐月基本费")) {
							mobileTel.setJtyhtc(new BigDecimal(value));
						}
					}
				}

				if (tds != null && tds.size() <= 3 && tds.size() >= 2) {
					String key = lines.get(j).select(td).get(0).text();
					String value = lines.get(j).select(td).get(1).text()
							.replaceAll("￥", "").replace("MB", "");
					if (isDoule(value)) {
						if (key.contains("套餐及固定费")) {
							mobileTel.setTcgdf(new BigDecimal(value));
						} else if (key.contains("套餐外短彩信费")) {
							mobileTel.setTcwdxf(new BigDecimal(value));
						} else if (key.contains("套餐外语音通信费")) {
							mobileTel.setTcwyytxf(new BigDecimal(value));
						} else if (key.contains("他人（单位）代付")) {
							mobileTel.setTryf(new BigDecimal(value));
						} else if (key.contains("IP国内长途定向优")) {
							mobileTel.setIpgnctdx(new BigDecimal(value));
						} else if (key.contains("30天国内短期漫游优惠包功能费")) {
							mobileTel.setSstgndq(new BigDecimal(value));
						} else if (key.contains("动感地带网聊套餐费")) {
							mobileTel.setDgddwl(new BigDecimal(value));
						} else if (key.contains("集团优惠套餐月基本费")) {
							mobileTel.setJtyhtc(new BigDecimal(value));
						} else if (key.contains("IP本地通话费")) {
							mobileTel.setIpbdth(new BigDecimal(value));
						} else if (key.contains("IP长途通话费")) {
							mobileTel.setIpctth(new BigDecimal(value));
						} else if (key.contains("长途通话费")) {
							mobileTel.setCtth(new BigDecimal(value));
						} else if (key.contains("本地通话费")) {
							mobileTel.setBdth(new BigDecimal(value));
						} else if (key.contains("漫游通话费")) {
							mobileTel.setMyth(new BigDecimal(value));
						} else if (key.contains("国内短信通信费")) {
							mobileTel.setGndxtx(new BigDecimal(value));
						} else if (key.contains("移动数据3元流量加油包月基本费")) {
							mobileTel.setYdsj3y(new BigDecimal(value));
						} else if (key.contains("数据流量(MB)")) {
							mobileTel.setYdsjllqb(new BigDecimal(value));
							String value2 = lines.get(j).select(td).get(2)
									.text().replaceAll("￥", "")
									.replace("MB", "");
							mobileTel.setYdsjllsj(new BigDecimal(value2));
						}
					}
				}
			}
			return mobileTel;
		}

	

	
	/*
	 * 验证是否登陆成功
	 */
	public void login() throws JSONException {
		String url = loginUrl + "&telno=" + login.getLoginName() + "&password=" + login.getPassword()+ "&authLevel=2";
		if(login.getAuthcode()!=null&&login.getAuthcode().trim().length()>2){
			url=url+"&validcode="+login.getAuthcode();
		}else{
			url=url+"&validcode=";
		}
		String data = cutil.post(url,null);
		JSONObject json = new JSONObject(data);
		if (json.get("result").toString().contains("0")) {
			String[] s = data.split("uid\":\"");
			String[] s1 = s[1].split("\"");
			String uid = s1[0];
			String url1 = confirmUrl + uid;
			data = cutil.post(url1,null);
			if(data.contains("result\":\"0")){
				loginsuccess();
			}else{	
				data = json.get("error").toString();
				json = new JSONObject(data);
				output.setErrorMsg(json.get("message").toString());
			}
		}else{
			output.setErrorMsg(json.get("message").toString());
		}
		if(islogin()){
			addTask(1);
			sendPhoneDynamicsCode();
		}
	}

	/**
	 * 生成短信
	 * */
	public void sendPhoneDynamicsCode(ResOutput res) {
		if(cutil.get(sendUrl+login.getLoginName())!=null){
			res.setStatus(1);
			res.setErrorMsg("发送成功!");
		}else{
			res.setErrorMsg("发送失败!");
		}
	}

	@Override
	public User gatherUser() throws JSONException {
		String content = cutil.get(identifyUrl);
		if(content!=null){
			content = "{"+new RegexPaserUtil(",\"value\":\\{", "\\}",content,RegexPaserUtil.TEXTEGEXANDNRT).getText()+"}";
		}
		JSONObject result = new JSONObject(content);
		User user = null;
		String userName = result.getString("name");
		String idcard = "";
		if ("身份证".equals(result.getString("zjType"))) {
			idcard = result.getString("zjNum");
		}
		String addr = result.getString("address");
		String email = result.getString("email");
		//真实姓名--------------------------
		String realName = cutil.get("http://www.sh.10086.cn/sh/wsyyt/action?act=my.getUserName");
		realName = new RegexPaserUtil("loginName\":\"", "\"",realName,RegexPaserUtil.TEXTEGEXANDNRT).getText();
		//账户余额--------------------------
		String html = cutil.get("http://www.sh.10086.cn/sh/wsyyt/action?act=my.getaccountinfo");
		String yue = new RegexPaserUtil("usable_fee\":\"", "\"",html,RegexPaserUtil.TEXTEGEXANDNRT).getText();
		String taocan = new RegexPaserUtil("plan_name\":\"", "\"",html,RegexPaserUtil.TEXTEGEXANDNRT).getText();
		user = new User();
		user.setLoginName(login.getLoginName());
		user.setUserName(realName);

		user.setEmail(email);
		user.setRealName(realName);
		user.setIdcard(idcard);
		user.setAddr(addr);
		user.setPackageName(taocan);//套餐类型
		try{
			user.setPhoneRemain(new BigDecimal(yue));
		}catch(Exception e){
			log4j.warn("话费解析异常#",e);
		}
		return user;
	}

	@Override
	public LinkedList<MonthlyBillMark> gatherMonthlyBill() {
		 LinkedList<MonthlyBillMark> listMark = getSpiderMonthsMark(true, "yyyy年MM月", 7	, 0);
		 //--------单线程采集,不采用多线程了--------
		 String url = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=FiveBillAllNewAjax";
		 Map<String,String> map = new HashMap<String, String>();
		 map.put("tab", "tab2_15");
		 map.put("isPriceTaxSeparate", "null");
		 map.put("showType", "0");
		 map.put("r", System.currentTimeMillis()+"");
		for (int i = 0; i < listMark.size(); i++) {
			if(i==0){
				listMark.get(i).setText(cutil.get("http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getFiveBillAll&showType=0&firstPage=y&uniqueKey=15"));
			}else{
				map.put("dateTime",listMark.get(i).getMonth());
				listMark.get(i).setText(cutil.post(url,map));
			}
		}
		//------------------解析-------------------
		
		for (int i = 0; i < listMark.size(); i++) {
			gatherMonthlyBill_parse(listMark.get(i),i);
		}
		return listMark;
	}
	private String getString(String begin,String end,String text){
		String str = new RegexPaserUtil(begin,end,text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
		if(str!=null){
			str = str.replaceAll("<.*?>", "").replaceAll("元|￥","").trim();
		}
//		System.out.println(str);
		return str;
	}
	// 账单
	public void gatherMonthlyBill_parse(MonthlyBillMark mark,int type) {
		String content = mark.getText();
		if(content!=null){
			BigDecimal cAllPay = null;
			BigDecimal cAllBalance = null;
			String text  = null;
			if(type==0){
				text = new RegexPaserUtil("class=\"user_information my_user_info\"","费用信息",content,RegexPaserUtil.TEXTEGEXANDNRT).getText();
			}else{
				text = new RegexPaserUtil("class='user_information my_user_info'","费用信息",content,RegexPaserUtil.TEXTEGEXANDNRT).getText();
			}
			
			Date cDate = mark.getFormatDate();
//			System.out.println(text);
			try{
				String  cName = getString("客户姓名：","</tr>", text);;
				String	brand = getString("星&nbsp;&nbsp;&nbsp;&nbsp;级：","</tr>",text);
				String	dependCycle = getString("计费周期：","</tr>",text);
				try{
					cAllPay = new BigDecimal(getString("本期总费用：","</tr>",text));
				}catch(Exception e){
					log4j.warn("话费账单总费用", e);
					log4j.writeLogByFlowBill(e);
				}
				try{
					cAllBalance = new BigDecimal(getString("话费账户余额：","</tr>",text));
				}catch(Exception e){
					log4j.warn("话费账单余额", e);
					log4j.writeLogByFlowBill(e);
				}
			
				MobileTel mt = new MobileTel();
				MobileTel mobieTel = getProjectPay(Jsoup.parse(content), mt);
				UUID uuid = UUID.randomUUID();
				mobieTel.setId(uuid.toString());
				mobieTel.setcTime(cDate);
				mobieTel.setcName(cName);
				mobieTel.setTeleno(this.loginName);
				mobieTel.setBrand(brand);
				mobieTel.setDependCycle(dependCycle);
				mobieTel.setcAllPay(cAllPay);
				mobieTel.setcAllBalance(cAllBalance);
				mark.setObj(mobieTel);
			}catch(Exception e){
				log4j.writeLogByFlowBill(e);
			}
		}
	}

	
	
	@Override
	public LinkedList<MobileDetail> gatherCallHistory() throws Exception {
		List<String> ms = DateUtils.getMonths(7,"yyyy-MM");
		Date bigTime = getMaxCallTime();
		List<String> spiderMonth = new ArrayList<String>();
 		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,"NEW_GSM",spiderMonth );
		LinkedList  listData = null;
		LinkedList list =  new LinkedList();
		String string = null;
		 String year = null;
		if(linkedList!=null){
			//从最小的月份开始解析
			boolean bigMonth = false;
			for (int j = linkedList.size()-1; j>=0;j--) {
			  string  = linkedList.get(j);
			  year = spiderMonth.get(j).substring(0, 4);
			  if(j==0){
				  bigMonth = true;
			  }
			  listData = gatherCallHistory_parse(string,bigTime,ms.get(j),year,bigMonth);
			  list.addAll(listData);
			  System.out.println(getCookie().getCookiesString());
			  String url = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getFlowBillAjax&beginDate=201409&endDate=201409";
//				System.out.println(cutil.get(url));
			}
		}
		return list;
	}
	/**数据从小到大正序存放
	 * @param text
	 * @param bigDate
	 * @param starDate
	 * @return
	 * @throws JSONException 
	 */
	public LinkedList<MobileDetail> gatherCallHistory_parse(String text1,Date bigDate,String starDate,String year,boolean bigMonth) throws JSONException{
		LinkedList<MobileDetail> listData = new LinkedList<MobileDetail>();
		if(text1!=null){
			String text = null;
			if(bigMonth){
				text = StringUtil.subStr("value = [];value =\"", "\";members", text1);
			}else{
				text = StringUtil.subStr("value = [];value = ", ";members", text1);
			}
			if(text!=null){
				JSONArray jsonArray;
				JSONArray json;
				Date d = null;
				//System.out.println(text);
				try{
				jsonArray = new JSONArray(text);
				for (int i = 0; i < jsonArray.length(); i++) {
					json = jsonArray.getJSONArray(i);
					MobileDetail mDetail = new MobileDetail();
					String ctime = year+"-"+json.getString(1);
					d = DateUtils.StringToDate(ctime,"yyyy-MM-dd HH:mm:ss");
					if(bigDate!=null&&bigDate.getTime()>=d.getTime()){
						continue;
					}
					String tradeAddr =  json.getString(2);
					String tradeWay = json.getString(3);
					String recevierPhone =  json.getString(4);
					String tradeTime = json.getString(5);
					int times = 0;
					try{
						TimeUtil tunit = new TimeUtil();
						times = tunit.timetoint(tradeTime);
					}catch(Exception e){
						log4j.warn("通话历史记录时间转换#",e);
					}
					String tradeType = json.getString(6);
					String taocan =  json.getString(7);
					BigDecimal onlinePay = new BigDecimal(json.getString(8));
					UUID uuid = UUID.randomUUID();
					mDetail.setId(uuid.toString());
					mDetail.setcTime(d);
					mDetail.setTradeAddr(tradeAddr);
					mDetail.setTradeWay(tradeWay);
					mDetail.setRecevierPhone(recevierPhone);
					mDetail.setTradeTime(times);
					mDetail.setTradeType(tradeType);
					mDetail.setTaocan(taocan);
					mDetail.setOnlinePay(onlinePay);
					mDetail.setPhone(login.getLoginName());
					listData.add(mDetail);
				}
				}catch (JSONException e) {
					log4j.writeLogByHistory(e);
				}
			}
		}
		return listData;
	}
	/**
	 * @param ms  月份
	 * @param bigTime  最大时间
	 * @return  结果集字符串 按照从小的到大的顺序  如 2014 4.1-2014 5.1
	 * @throws Exception
	 */
	public LinkedList<String> gatherSpiderStrings(List<String> ms,Date bigTime,String billType,List<String> spiderMonth) throws Exception{
		LinkedList<String> list  = new LinkedList<String>();
		Date d = null;
		Map<String,String> pararm = null;
		String url = null;
		 CHeader h = new CHeader();
//		 h.setCookie("WT_FPC=id=2a6f2d5ec730561fe981413181071815:lv=1413181182772:ss=1413181071815; CmProvid=sh; CmWebtokenid=\"13681633227,sh\"; CmActokenid=7d46baa2b2cf4875922eb7c06d2c355d; WEBTRENDS_ID=40.40.40.214-1777055584.30402221; yRJfzS9NAZ=MDAwM2IyYzg3OTAwMDAwMDAwMzcwfUVpfQUxNDEzMTg3MDA0; jsessionid_141_p3=8284e60f514f51d49dab96510b69; oodZQ7MJgF=MDAwM2IyYzg3OTAwMDAwMDAwMzgwDhQqEAsxNDEzMTg3MDU1; __g_u=40918406203631_1_0.2_1_5_1413613086995_0; __g_c=c%3A40918406203631%7Cd%3A1%7Ca%3A1%7Cb%3A2%7Ce%3A0.2%7Cf%3A1%7Ch%3A0; Q_fiveBalance_new3|1089008159=true");
		for (int i = 0; i < ms.size(); i++) {
			pararm = new HashMap<String, String>();
			pararm.put("billType", billType);
			pararm.put("startDate", ms.get(i)+"-01");
			pararm.put("searchStr", "-1");
			pararm.put("index", "0");
			pararm.put("r", String.valueOf(System.currentTimeMillis()));
			pararm.put("isCardNo", "0");
			pararm.put("gprsType", "");	
			if(i!=0){
				//如果当前最大时间比上一次采集的月份还要大,就终止,因为可以理解为后边已经采集了
				d = DateUtils.StringToDate(ms.get(i-1)+"-01", "yyyy-MM-dd");
				if(bigTime!=null&&bigTime.getTime()>=d.getTime()){
					break;
				}
				pararm.put("endDate", DateUtils.lastDayOfMonth(ms.get(i), "yyyy-MM", "yyyy-MM-dd"));
				pararm.put("filterfield", "输入对方号码：");
				pararm.put("filterfield", "");
				pararm.put("filterValue", "");
				url = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getFiveBillDetailAjax";
			}else{
				pararm.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
				pararm.put("jingque", "");
				url = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getOneBillDetailAjax";
			}
			spiderMonth.add(ms.get(i));
			list.add(cutil.post(url,h, pararm));
		}
		//集合线程装载的结果为从大到小,解析时转换
		return list;
	}

	@Override
	public LinkedList<MobileMessage> gatherMessage() throws Exception {
		//System.out.println(getCookie().getCookiesString());
		List<String> ms = DateUtils.getMonths(7,"yyyy-MM");
		Date bigTime = getMaxMessageTime();
		List<String> spiderMonth = new ArrayList<String>();
		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,"NEW_SMS",spiderMonth);
		LinkedList  listData = null;
		LinkedList list =  new LinkedList();
		String string = null;
		String year = null;
		if(linkedList!=null){
			//从最小的月份开始解析
			boolean bigMonth = false;
			for (int j = linkedList.size()-1; j>=0;j--) {
			  string  = linkedList.get(j);
			  year = spiderMonth.get(j).substring(0, 4);
			  if(j==0){
				  bigMonth = true;
			  }
			  listData = gatherMessHistory_parse(string,bigTime,ms.get(j),year,bigMonth);
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
	 * @throws JSONException 
	 */
	public LinkedList<MobileMessage> gatherMessHistory_parse(String text1,Date bigDate,String starDate,String year,boolean bigMonth){
		LinkedList<MobileMessage> listData = new LinkedList<MobileMessage>();
		if(text1!=null){
			String text = null;
			if(bigMonth){
				text = StringUtil.subStr("var value = [];value =\"", "\";members", text1);
			}else{
				text = StringUtil.subStr("value = [];value = ", ";members", text1);
			}
			if(text!=null){
				JSONArray jsonArray;
				JSONArray json = null;
				Date d = null;
//				System.out.println(text);
				try {
					jsonArray = new JSONArray(text);
					for (int i = 0; i < jsonArray.length(); i++) {
						json = jsonArray.getJSONArray(i);
						MobileMessage mMessage = new MobileMessage();
						String ctime = year+"-"+json.getString(1);
						d = DateUtils.StringToDate(ctime,"yyyy-MM-dd HH:mm:ss");
						if(bigDate!=null&&bigDate.getTime()>=d.getTime()){
							continue;
						}
						String tradeAddr =  json.getString(2);
						String receivePhone = json.getString(3);
						String tradeWay = json.getString(4);
						String allPay = json.getString(8);
						
						UUID uuid = UUID.randomUUID();
						mMessage.setAllPay(new BigDecimal(allPay));
						mMessage.setCreateTs(new Date());
						mMessage.setPhone(login.getLoginName());
						mMessage.setRecevierPhone(receivePhone);
						mMessage.setSentAddr(tradeAddr);
						mMessage.setSentTime(d);
						mMessage.setTradeWay(tradeWay);
						mMessage.setId(uuid.toString());
						listData.add(mMessage);
					}
				} catch (JSONException e) {
					log4j.writeLogByMessage(e);
				}
			}
		}
		return listData;
	}
	@Override
	public LinkedList<MobileOnlineList> gatherFlowList() throws Exception {
		thisMonthOnlineBill.setTotalFlow(new Long(0));
		thisMonthOnlineBill.setTrafficCharges(new BigDecimal(0));
		List<String> ms = DateUtils.getMonths(7,"yyyy-MM");
		Date bigTime = getMaxFlowTime();
		List<String> spiderMonth = new ArrayList<String>();
		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,"NEW_GPRS",spiderMonth);
		LinkedList  listData = null;
		LinkedList list =  new LinkedList();
		String string = null;
		String year= null;
		if(linkedList!=null){
			//从最小的月份开始解析
			boolean bigMonth = false;
			for (int j = linkedList.size()-1; j>=0;j--) {
			  string  = linkedList.get(j);
			  year = spiderMonth.get(j).substring(0, 4);
			  if(j==0){
				  bigMonth = true;
			  }
			  listData = gatherFlowHistory_parse(string,bigTime,ms.get(j),year,bigMonth);
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
	public LinkedList<MobileOnlineList> gatherFlowHistory_parse(String text1,Date bigDate,String starDate,String year,boolean bigMonth){
		LinkedList<MobileOnlineList> listData = new LinkedList<MobileOnlineList>();
		Date today=new Date();
		Date monthDay=null;
		if(text1!=null){
			String text = null;
			if(bigMonth){
				text = StringUtil.subStr("var value = [];value =\"", "\";members", text1);
			}else{
				text = StringUtil.subStr("value = [];value = ", ";members", text1);
			}
			if(text!=null){
				JSONArray jsonArray;
				JSONArray json = null;
				Date d = null;
//				System.out.println(text);
				try {
					jsonArray = new JSONArray(text);
					Long all_flow=new Long(0);
					Double all_fee=new Double(0);
					for (int i = 0; i < jsonArray.length(); i++) {
						json = jsonArray.getJSONArray(i);
						d = DateUtils.StringToDate(year+"-"+json.getString(1),"yyyy-MM-dd HH:mm:ss");
						if(bigDate!=null&&bigDate.getTime()>=d.getTime()){
							continue;
						}
						
						MobileOnlineList mbOnline = new MobileOnlineList();
						UUID uuid = UUID.randomUUID();
						mbOnline.setId(uuid.toString());
						mbOnline.setPhone(login.getLoginName());
	
//						Calendar cal = Calendar.getInstance();
//						int currentyear = cal.get(Calendar.YEAR);
						mbOnline.setcTime(d);
						monthDay=d;
						mbOnline.setTradeAddr((String) json.get(2));
						mbOnline.setOnlineType((String) json.get(3));
	
						Long time = converSeconds((String) json.get(4));
						mbOnline.setOnlineTime(time);
						
						Long flow=addFlow((String) json.get(5));
						all_flow+=flow;//为当月流量账单做准备
						mbOnline.setTotalFlow(flow);
						mbOnline.setCheapService((String) json.get(6));
						Double fee=Double
								.parseDouble((String) json.get(7));
						all_fee+=fee;
						mbOnline.setCommunicationFees(BigDecimal.valueOf(fee));
						listData.add(mbOnline);
					}
					System.out.println(all_flow+","+all_fee+",");
					if(monthDay!=null && monthDay.getMonth()==today.getMonth()){
						thisMonthOnlineBill.setTotalFlow(all_flow);
						thisMonthOnlineBill.setTrafficCharges(new BigDecimal(all_fee.toString()));
					}
				}catch(Exception e){
					log4j.writeLogByFlowList(e);
				}

			}

		}
		return listData;
	}
	@Override
	public LinkedList<MonthlyBillMark> gatherFlowBill() {
		//当月的流量账单特殊对待！
		 LinkedList<MonthlyBillMark> listMark = getSpiderMonthsMark(true, "yyyyMM", 7, 1);
		 List<String> monthList=DateUtils.getMonths(1, "yyyyMM");

		 //--------单线程采集,不采用多线程了--------
		for (int i = 0; i < listMark.size(); i++) {
			//当月分开对待
			if(listMark.get(i).getMonth()==monthList.get(0))
				continue;
			listMark.get(i).setText(cutil.get("http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getFlowBillAjax&beginDate="
					+ listMark.get(i).getMonth() + "&endDate=" + listMark.get(i).getMonth()));
		}
		//------------------解析-------------------
		for (int i = 0; i < listMark.size(); i++) {
			//System.out.println(listMark.get(i).getMonth()+","+monthList.get(0));
			if(i==0){
				thisMonthOnlineBill.setPhone(login.getLoginName());
				thisMonthOnlineBill.setMonthly(DateUtils.StringToDate(listMark.get(i).getMonth(),"yyyyMM"));
				thisMonthOnlineBill.setIscm(1);
				listMark.get(i).setObj(thisMonthOnlineBill);
			}
			else
				gatherFlowBill_parse(listMark.get(i));
		}
		return listMark;
	}
	public void gatherFlowBill_parse(MonthlyBillMark mark){
			String html = mark.getText();
			String total = null;
			Long totalFlow = null;
			String fees = null;
			String outFees = "0";
			Double out_fees=new Double(0);
			try {
				if(html!=null){
					//我就怕会有个国外流量!
					fees = StringUtil.subStr("total_Item_fee\":\"", "元", html);	
					try{
					JSONObject json1=new JSONArray(html).getJSONObject(0);
					JSONArray json2=json1.getJSONArray("flowBillHisList");
					for(int ss=0;ss<json2.length();ss++){
						//System.out.println(json2.getJSONObject(ss).getString("bill_month"));
						if(json2.getJSONObject(ss).getString("bill_month").equals(mark.getMonth()))
							outFees=json2.getJSONObject(ss).getString("flow_fee_out");
					}
					}catch(Exception e){
						outFees="0";
					}
					String startText = "item_count\":\"";
					int sindex = html.lastIndexOf(startText);
					if(sindex!=-1){
						int eindex = html.indexOf("\",", sindex);
						if(eindex!=-1){
							total = html.substring(sindex+startText.length(),eindex);
							if(StringUtils.isBlank(total)){
								total = "0";
							}else{
								totalFlow = Math.round(StringUtil.flowFormat(total));
							}
						}
					}
					//item_fee":"
				out_fees=Double.parseDouble(outFees)/100;
				}
				MobileOnlineBill mOnlineBill = new MobileOnlineBill();
				mOnlineBill.setPhone(login.getLoginName());
				mOnlineBill.setMonthly(DateUtils.StringToDate(mark.getMonth(),"yyyyMM"));
				//mOnlineBill.setTrafficCharges(BigDecimal.valueOf(Double
				//		.parseDouble(fees)));
				mOnlineBill.setTrafficCharges(new BigDecimal(out_fees.toString()));
				mOnlineBill.setTotalFlow(totalFlow);
				mark.setObj(mOnlineBill);
			} catch (Exception e) {
				//当月的会空指针！
				e.printStackTrace();
			}
	}
	
	private Long addFlow(String flow){
		Long l0 = new Long(0);
		Long l1 = new Long(0);
		Long l2 = new Long(0);

		if(flow == null || "".equals(flow)){
			return new Long(0);
		}
		if(flow.contains("GB")){
			String [] aa = flow.split("GB");
			l0 = Long.parseLong(aa[0]) * 1024 * 1024;
				if(flow.contains("MB")){
					String [] bb = aa[1].split("MB");
					l1 = Long.parseLong(bb[0]) * 1024;
					if(flow.contains("KB")){
						String [] cc = bb[1].split("KB");
						l2 = Long.parseLong(cc[0]);
					}
				}
		} else if(flow.contains("MB")){
			String [] bb = flow.split("MB");
			l1 = Long.parseLong(bb[0]) * 1024;
			if(flow.contains("KB")){
				String [] cc = bb[1].split("KB");
				l2 = Long.parseLong(cc[0]);
			}
		} else {
			String [] cc = flow.split("KB");
			l2 = Long.parseLong(cc[0]);
		}
		return l0+l1+l2;
	}
	/**
	 * 时分秒转换
	 * @param string
	 * @return Long
	 */
	private Long converSeconds(String string) {
		Long aa = new Long(0);
		String time[] = string.split(":");
		for(int i = 0 ; i < time.length; i++){
			if(i == 0){
				aa = Long.parseLong(time[0]) * 60 * 60;
			} else if (i == 1){
				aa = aa + Long.parseLong(time[1]) * 60;
				
			} else {
				aa = aa + Long.parseLong(time[2]);
			}
			
		}
		
		return aa;
	}

}





///**
// * 手机上网账单 
// * @return int
// */
//public int callOnlineBill() {
//	
//	List<MobileOnlineBill> billList = new ArrayList<MobileOnlineBill>();
//	
//	try {
//		List<String> ms = getLastMonth(6, "yyyyMM");
//		LinkedList<String> list = new LinkedList<String>();
//		String url = null;
//		for (String string : ms) {
//			url = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getFlowBillAjax&beginDate="
//					+ string + "&endDate=" + string;
//			list.add(cutil.post(url, null));
//		}
//
//		for (int i = 0; i < list.size(); i++) {
//			String html = list.get(i);
//			String total = null;
//			Long totalFlow = null;
//			String fees = null;
//			try {
//				JSONArray jsonArray = new JSONArray(html);
//				
//				for (int j = 0; j < jsonArray.length(); j++) {
//					JSONObject arrays = jsonArray.getJSONObject(j);
//
//					fees = arrays.getString("total_Item_fee");
//					if (fees == null || "".equals(fees)) {
//						fees = "0";
//					} else if (fees.contains("元")) {
//						String aa[] = fees.split("元");
//						fees = aa[0];
//					}
//
//					JSONArray results = arrays
//							.getJSONArray("flowBillDtlList");
//
//					for (int k = 0; k < results.length(); k++) {
//
//						if (k == 4) {
//							JSONObject result = results.getJSONObject(k);
//							// 71.62M
//							total = result.getString("item_count");
//
//							if (total == null || "".equals(total)) {
//								totalFlow = new Long(0);
//							} else if (total.contains("M")) {
//								String aa[] = total.split("M");
//								totalFlow = (long)(Double.parseDouble(aa[0]) * 1024);
//							} else {
//								totalFlow = Long.parseLong(total);
//							}
//						}
//					}
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			MobileOnlineBill mOnlineBill = new MobileOnlineBill();
//			UUID uuid = UUID.randomUUID();
//			mOnlineBill.setId(uuid.toString());
//			mOnlineBill.setPhone(login.getLoginName());
//			String time = ms.get(i).substring(0, 4) + "-" + ms.get(i).substring(4, 6);
//			mOnlineBill.setMonthly(StringToDate(time + "-01 00:00:00",
//					"yyyy-MM-dd HH:mm:ss"));
//			mOnlineBill.setTrafficCharges(BigDecimal.valueOf(Double
//					.parseDouble(fees)));
//			mOnlineBill.setTotalFlow(totalFlow);
//			billList.add(mOnlineBill);
//		}
//		mobileOnlineBillService.insertbatch(billList);
//		return 1;
//	} catch (Exception e) {
//		e.printStackTrace();
//		//TODO
//		//log
//	}
//	return 0;
//}
//

	/*
	 * 保存用户基本信息
	 * //{"error":{"code":0,"hint":"","message":""},"value":{"zjType":"身份证"
	 * ,"telNo":"13681633227","isGroupMailMem":"0"(/),"name":"窦*",
	 * //"telStatus":
	 * "开机"（未保存）,"address":"安徽省合肥市包河区屯溪路１９３号０６级机汽学院","email":"","payStatus"
	 * :"正常"（未保存）,"planType":"1"（未保存）,"zjNum":"1403**********0013",
	 * //"postcode":(
	 * "200000"（未保存）,"contactTel":"13681633227","puk":"04570094"(/),
	 * "printtype":"不投递"(/)}}
	 */
//	public void getMyInfo() {
//		try{
//		Boolean flag = false;
//		
//		CHeader cHeader = new CHeader();
//		cHeader.setAccept("application/json, text/javascript, */*; q=0.01");
//		cHeader.setX_requested_with(true);
//		cHeader.setReferer("http://www.sh.10086.cn/sh/wsyyt/busi/64_85.jsp");
//		cHeader.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
//		cHeader.setHost("www.sh.10086.cn");
//		
//		String responseBody = cutil.get(identifyUrl, cHeader);
//	
//
//		
//		String content = responseBody;
//		content = content.replace("\"value\":{", "\"value\":[{").replace("}}",
//				"}]}");
//		JSONObject json;
//		try {
//			json = new JSONObject(content);
//			JSONArray results = json.getJSONArray("value");
//			for (int i = 0; i < results.length(); i++) {
//				JSONObject result = results.getJSONObject(i);
////				System.out.println(result.getString("zjType") + " "
////						+ result.getString("telNo"));
//
//				String userName = result.getString("name");
//				String idcard = "";
//				if ("身份证".equals(result.getString("zjType"))) {
//					idcard = result.getString("zjNum");
//				}
//				String addr = result.getString("address");
//				String email = result.getString("email");
//				String realName = result.getString("name");
//
//				Date modifyDate = new Date();
//				BaseUser bu = new BaseUser();
//				Map baseMap = new HashMap();
//				baseMap.put("addr", addr);
//
//				baseMap.put("modifyDate", modifyDate);
////				bu.saveUserInfo(userService, baseMap, currentUser);
//
//				Map<String, String> map = new HashMap<String, String>(3);
//				map.put("parentId", currentUser);
//				map.put("loginName", login.getLoginName());
//				map.put("usersource", Constant.YIDONG);
//				List<User> list = service.getUserService().getUserByParentIdSource(map);
//				if (list != null && list.size() > 0) {
//					User user = list.get(0);
//					user.setLoginName(login.getLoginName());
//					user.setUserName(userName);
//
//					user.setEmail(email);
//					user.setRealName(realName);
//					user.setIdcard(idcard);
//					user.setAddr(addr);
//					user.setUsersource(Constant.YIDONG);
//					user.setUsersource2(Constant.YIDONG);
//					user.setParentId(currentUser);
//					user.setModifyDate(modifyDate);
//					service.getUserService().update(user);
//				} else {
//					try {
//						User user = new User();
//						UUID uuid = UUID.randomUUID();
//						user.setId(uuid.toString());
//						user.setLoginName(login.getLoginName());
//						user.setUserName(userName);
//
//						user.setEmail(email);
//						user.setRealName(realName);
//						user.setIdcard(idcard);
//						user.setAddr(addr);
//						user.setUsersource(Constant.YIDONG);
//						user.setUsersource2(Constant.YIDONG);
//						user.setModifyDate(modifyDate);
//						user.setParentId(currentUser);
//						service.getUserService().saveUser(user);
//
//					} catch (Exception e) {
//						logger.info("第900行捕获异常：",e);		
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (JSONException e) {
//			logger.info("第906行捕获异常：",e);		
//			e.printStackTrace();
//		}
//		}catch(Exception e){
//			//报警
//			logger.info("第933行捕获异常：",e);		
//			String warnType = WaringConstaint.YD_1;
//			WarningUtil wutil = new WarningUtil();
//			wutil.warning(service.getWarningService(), currentUser, warnType);
//		}
//	}
//}
//public static void fromJson() {
//
//	String a = "{\"error\":{\"code\":0,\"hint\":\"\",\"message\":\"\"},\"value\":{\"zjType\":\"身份证\",\"telNo\":\"13681633227\",\"isGroupMailMem\":\"0\",\"name\":\"窦*\","
//			+ "\"telStatus\":\"开机\",\"address\":\"安徽省合肥市包河区屯溪路１９３号０６级机汽学院\",\"email\":\"\",\"payStatus\":\"正常\",\"planType\":\"1\",\"zjNum\":\"1403**********0013\","
//			+ "\"postcode\":\"200000\",\"contactTel\":\"13681633227\",\"puk\":\"04570094\",\"printtype\":\"不投递\"}}";
//	a = a.replace("\"value\":{", "\"value\":[{").replace("}}", "}]}");
//	JSONObject json;
//	try {
//		json = new JSONObject(a);
//		JSONArray results = json.getJSONArray("value");
//		for (int i = 0; i < results.length(); i++) {
//			JSONObject result = results.getJSONObject(i);
//			System.out.println(result.getString("zjType") + " "
//					+ result.getString("telNo"));
//		}
//	} catch (JSONException e) {
//		logger.info("第976行捕获异常：",e);		
//		e.printStackTrace();
//
//	}
//
//}
//CHeader cHeader = new CHeader();
//cHeader.setAccept("application/json, text/javascript, */*; q=0.01");
//cHeader.setX_requested_with(true);
//cHeader.setReferer("http://www.sh.10086.cn/sh/wsyyt/busi/64_85.jsp");
//cHeader.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0)");
//cHeader.setHost("www.sh.10086.cn");
//
//String responseBody = "";
//HttpResponse response3 = CUtil.getHttpGet(identifyUrl, cHeader, httpclient);
//if(response3!=null){
//	responseBody = ParseResponse.parse(response3);
//}
//
//
//String content = responseBody;
//System.out.println(content);
//
//
//// 当月
//String url6 = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=getFiveBillAll&showType=0&firstPage=y&uniqueKey=15&uniqueName=查询前5个月账单";
//
//String url7 = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=FiveBillAllNewAjax&dateTime=2013年12月&isPriceTaxSeparate=null&showType=0&r=1396837684338";
//String url8 = "http://www.sh.10086.cn/sh/wsyyt/busi/historySearch.do?method=FiveBillAllNewAjax&dateTime=2014年02月&isPriceTaxSeparate=null&showType=0&r=1396837974684";

// {"error":{"code":0,"hint":"","message":""},"value":{"zjType":"身份证","telNo":"13681633227","isGroupMailMem":"0"(/),"name":"窦*",
// "telStatus":"开机","address":"安徽省合肥市包河区屯溪路１９３号０６级机汽学院","email":"","payStatus":"正常","planType":"1","zjNum":"1403**********0013",
// "postcode":"200000","contactTel":"13681633227","puk":"04570094"(/),"printtype":"不投递"(/)}}

// System.out.println(yidong.getText(httpclient, url6));
//String sendUrl2 = sendUrl + "13681633227&t="
//		+ System.currentTimeMillis();
//// 发送短信息
//System.out.println(yidong.getText(httpclient, sendUrl2));
//Scanner in = new Scanner(System.in);
//System.out.print("短信息为：");
//String authcode = in.nextLine();
//String url2 = loginUrl2 + "&telno=13681633227&password=" + authcode
//		+ "&t=" + System.currentTimeMillis();
//String text1 = yidong.getText(httpclient, url2);
//System.out.println(text1);
//text1 = text1.replace("ssoLoginCallback(", "").replace(");", "");
//JSONObject json;
//String uid = "";
//try {
//	json = new JSONObject(text1);
//	uid = json.get("uid").toString();
//	System.out.println(uid);
//} catch (Exception e) {
//	logger.info("第158行捕获异常：",e);		
//	e.printStackTrace();
//}
//
//Map<String, String> map21 = new HashMap<String, String>();
//map21.put("uid", uid);
//String text21 = yidong.getDetail(httpclient, loginOverPost, map21);
//System.out.println(text21);

// 当月话费
//Map<String, String> map2 = new HashMap<String, String>();
//map2.put("billType", "NEW_GSM");
//map2.put("startDate", "2014-04-01");
//map2.put("endDate", "2014-04-30");
//map2.put("jingque", "");
//map2.put("searchStr", "-1");
//map2.put("index", "0");
//map2.put("r", String.valueOf(System.currentTimeMillis()));
//map2.put("isCardNo", "0");
//map2.put("gprsType", "");
//String text2 = yidong.getDetail(httpclient, currentMonthDetail, map2);
//System.out.println(text2);
//
//// 历史话费
//Map<String, String> map3 = new HashMap<String, String>();
//
//map3.put("dateTime", "2014年05月");
////map3.put("tab", "tab2_15");
//map3.put("isPriceTaxSeparate", "null");
//map3.put("showType", "0");
//map3.put("r", String.valueOf(System.currentTimeMillis()));
////dateTime=2014年05月&tab=tab2_15&isPriceTaxSeparate=null&showType=0&r=1406633363333
//		
//String text3 = yidong.getDetail(httpclient, otherMonthDetail, map3);
//System.out.println(text3);
//public SHYidong(Login login,String currentUser){
//	super(login,ConstantNum.comm_sh_yidong,currentUser);
//}
/*
 * 保存话费信息
 */
//public void getTelDetailHtml() {
//	
//	List<String> objectTmp = new ArrayList<String>();
//	objectTmp.add(urlCurrentMonth);
//	java.text.DateFormat format2 = new java.text.SimpleDateFormat(
//			"yyyy年MM月");
//	Calendar c = Calendar.getInstance();
//	for (int i = 0; i < 5; i++) {
//		c.add(Calendar.MONTH, -1);
//		Date date = c.getTime();
//		String date2 = format2.format(date);
//		long random = System.currentTimeMillis();
//		objectTmp.add(urlLastMonth + "&dateTime=" + date2 + "&r=" + random);
//	}
//
//
//	try {
//	
//		for (int i = 0; i < objectTmp.size(); i++) {
//			String url = objectTmp.get(i);
//			get( url, currentUser, service.getMobileTelService());
//		}
//
//	}catch(Exception e){
//		//报警
//		logger.info("第546行捕获异常：",e);		
//		String warnType = WaringConstaint.YD_2;
//		WarningUtil wutil = new WarningUtil();
//		wutil.warning(service.getWarningService(), currentUser, warnType);
//	}
//	
//}
//
//
//
//	// 订单
//	public void get(String redirectLocation,String currentUser,IMobileTelService mobileTelService) {
//		
//			String content = cutil.get(redirectLocation);
//			Date cDate = null;
//			String cName = "";
//			String teleno = "";
//			String brand = "";
//			String dependCycle = "";
//
//			BigDecimal cAllPay = new BigDecimal("0");
//			BigDecimal cAllBalance = new BigDecimal("0");
//			String td = InfoUtil.getInstance().getInfo("yd/sh",
//					"td");
//			String tr = InfoUtil.getInstance().getInfo("yd/sh",
//					"tr");
//			String base_information_ul = InfoUtil.getInstance().getInfo("yd/sh",
//					"base_information_ul");
//			String fy_information_ul = InfoUtil.getInstance().getInfo("yd/sh",
//					"fy_information_ul");
//			Document doc = null;
//			if (redirectLocation.contains("&dateTime=")) {
//				if (content.contains("\"message\":\"")) {
//					String[] str = content.split("\"message\":\"");
//					if (str.length > 1
//							&& str[1].contains("\",\"resultName")) {
//						String[] str1 = str[1].split("\",\"resultName");
//						String html = str1[0];
//						// 开始查找页面元素并且写入数据库
//						doc = Jsoup.parse(html);
//
//						String baseUserId = currentUser;
//
//						String[] temp = redirectLocation
//								.split("&dateTime=");
//						String date = temp[1].split("&r")[0];
//						DateFormat df = new SimpleDateFormat("yyyy年MM月");
//
//						try {
//							cDate = df.parse(date);
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							logger.info("第586行捕获异常：",e);		
//							e.printStackTrace();
//						}
//						Elements elements = doc
//								.select(base_information_ul)
//								.first().select(tr);
//						cName = elements.get(0).select(td).get(1).text();
//						teleno = elements.get(1).select(td).get(1).text();
//						brand = elements.get(2).select(td).get(1).text();
//						dependCycle = elements.get(3).select(td).get(1)
//								.text();
//						
//						try{
//							String amount1 = doc
//									.select("table[class=fy_information_ul]")
//									.first().select(tr).first().select(td)
//									.get(1).text().replace("￥", "")
//									.replace("元", "");
//							String amount2 = "";
//							try{
//							amount2 = doc
//									.select("table[class=fy_information_ul]")
//									.first().select(tr).get(2).select(td)
//									.get(1).text().replace("￥", "")
//									.replace("元", "");
//							}catch(Exception e){
//								amount2 = "0";
//							}
//							if (amount1 != null && amount1.trim().length() > 0) {
//								cAllPay = new BigDecimal(amount1);
//							}
//						
//							if (amount2 != null && amount2.trim().length() > 0) {
//								cAllBalance = new BigDecimal(amount2);
//							}
//						}catch(Exception e){
//							logger.info("第618行捕获异常：",e);		
//							e.printStackTrace();
//						}
//		
//					}
//				}
//			}
//
//			else {
//				doc = Jsoup.parse(content);
//				DateFormat df = new SimpleDateFormat("yyyy年MM月");
//				cDate = new Date();
//				try {
//					cDate = df.parse(df.format(cDate));
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					logger.info("第634行捕获异常：",e);		
//					e.printStackTrace();
//				}
//
//				Elements elements = doc
//						.select(base_information_ul).first()
//						.select(tr);
//				cName = elements.get(0).select(td).get(1).text();
//				teleno = elements.get(1).select(td).get(1).text();
//				brand = elements.get(2).select(td).get(1).text();
//				dependCycle = elements.get(3).select(td).get(1).text();
//
//				try{
//					String amount1 = doc
//						.select("table[class=fy_information_ul]").first()
//						.select(tr).first().select(td).get(1).text()
//						.replace("￥", "").replace("元", "");
//					if (amount1 != null && amount1.trim().length() > 0) {
//						cAllPay = new BigDecimal(amount1);
//					}
//				
//					String amount2 = doc
//							.select(fy_information_ul).first()
//							.select(tr).get(2).select(td).get(1).text()
//							.replace("￥", "").replace("元", "");
//					if (amount2 != null && amount2.trim().length() > 0) {
//						cAllBalance = new BigDecimal(amount2);
//					}
//				}catch(Exception e){
//					logger.info("第663行捕获异常：",e);		
//					e.printStackTrace();
//				}
//				
//
//				
//			}
//
//			String baseUserId = currentUser;
//			Map map = new HashMap();
//			map.put("phone", teleno);
//			map.put("cTime", cDate);
//			List<MobileTel> mobileTelList = (List) mobileTelService
//					.getMobileTelBybc(map);
//			if (mobileTelList != null && mobileTelList.size() > 0) {
//				MobileTel mb = mobileTelList.get(0);
//				if (mb.getIscm() == 1) {
//					MobileTel mobieTel = getProjectPay(doc, mb);
//					mobieTel.setcTime(cDate);
//					mobieTel.setcName(cName);
//					mobieTel.setTeleno(teleno);
//					mobieTel.setBrand(brand);
//					mobieTel.setDependCycle(dependCycle);
//					mobieTel.setcAllPay(cAllPay);
//					mobieTel.setcAllBalance(cAllBalance);
//					mobieTel.setIscm(1);
//					mobileTelService.update(mobieTel);
//				}
//			} else {
//				MobileTel mt = new MobileTel();
//				MobileTel mobieTel = getProjectPay(doc, mt);
//				UUID uuid = UUID.randomUUID();
//				mobieTel.setId(uuid.toString());
////				mobieTel.setBaseUserId(baseUserId);
//				mobieTel.setcTime(cDate);
//				mobieTel.setcName(cName);
//				mobieTel.setTeleno(teleno);
//				mobieTel.setBrand(brand);
//				mobieTel.setDependCycle(dependCycle);
//				mobieTel.setcAllPay(cAllPay);
//				mobieTel.setcAllBalance(cAllBalance);
//				mobieTel.setIscm(1);
//				mobileTelService.saveMobileTel(mobieTel);
//
//			}
//
//	}

//// 当月通话记录
//public void alyzer() {
//	java.text.DateFormat format2 = new java.text.SimpleDateFormat(
//			"yyyy-MM-dd");
//	Calendar c = Calendar.getInstance();
//	Date nowDate = c.getTime();
//	c.set(Calendar.DAY_OF_MONTH, 1);
//	Date firstDates = c.getTime();
//	String firstDate2 = format2.format(firstDates);
//	String lastDate2 = format2.format(nowDate);
//	System.out.println(firstDate2 + "---" + lastDate2);
//	String cyear = firstDate2.substring(0, 4);
//	// 当月通话记录
//	Map<String, String> map2 = new HashMap<String, String>();
//	map2.put("billType", "NEW_GSM");
//	map2.put("startDate", firstDate2);
//	map2.put("endDate", lastDate2);
//	map2.put("jingque", "");
//	map2.put("searchStr", "-1");
//	map2.put("index", "0");
//	map2.put("r", String.valueOf(System.currentTimeMillis()));
//	map2.put("isCardNo", "0");
//	map2.put("gprsType", "");
//	String str = cutil.post(currentMonthDetail, map2);
//	System.out.println(str);
//
//	String[] strs = str.split("value =\"\\u005B");
//	String[] strs2 = strs[1].split("]\";members"); 
//	
//	String str2 = strs2[0];
//	String[] strs3 = str2.split("],");
//	java.text.DateFormat format3 = new java.text.SimpleDateFormat(
//			"yyyy-MM-dd HH:mm:ss");
//	for (int i = 0; i < strs3.length; i++) {
//		String nstr = strs3[i].replace("]", "");
//		String[] nstrs = nstr.split("',");
//		if (nstrs != null && nstrs.length > 8) {
//			String day = cyear + "-" + nstrs[1].replace("'", "");
//			Date cTime = null;
//			try {
//				cTime = format3.parse(day);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				logger.info("第250行捕获异常：",e);		
//				e.printStackTrace();
//			}
//			Map map = new HashMap();
//			map.put("phone", login.getLoginName());
//			map.put("cTime", cTime);
//			List<MobileDetail> mobileTelList = (List) service.getMobileDetailService()
//					.getMobileDetailBypt(map);
//			if (mobileTelList != null && mobileTelList.size() > 0) {
//
//			} else {
//				MobileDetail mDetail = new MobileDetail();
//				String tradeAddr = nstrs[2].replace("'", "");
//				String tradeWay = nstrs[3].replace("'", "");
//				String recevierPhone = nstrs[4].replace("'", "");
//				String tradeTime = nstrs[5].replace("'", "");
//				
//				int times = 0;
//				try{
//					TimeUtil tunit = new TimeUtil();
//					times = tunit.timetoint(tradeTime);
//				}catch(Exception e){
//					
//				}
//				String tradeType = nstrs[6].replace("'", "");
//				String taocan = nstrs[7].replace("'", "");
//				BigDecimal onlinePay = new BigDecimal(nstrs[8].replace("'",
//						""));
//				UUID uuid = UUID.randomUUID();
//				mDetail.setId(uuid.toString());
//				mDetail.setcTime(cTime);
//				mDetail.setTradeAddr(tradeAddr);
//				mDetail.setTradeWay(tradeWay);
//				mDetail.setRecevierPhone(recevierPhone);
//				mDetail.setTradeTime(times);
//				mDetail.setTradeType(tradeType);
//				mDetail.setTaocan(taocan);
//				mDetail.setOnlinePay(onlinePay);
//				mDetail.setPhone(login.getLoginName());
//				mDetail.setIscm(1);
//				service.getMobileDetailService().saveMobileDetail(mDetail);
//
//			}
//		}
//	}
//
//}
//
//// 历史通话记录
//public void alyzer3() {
//	java.text.DateFormat format2 = new java.text.SimpleDateFormat(
//			"yyyy-MM-dd");
//	Calendar c = Calendar.getInstance();
//	MobileDetail md = new MobileDetail();
//	md.setPhone(login.getLoginName());
//	md = service.getMobileDetailService().getMaxTime(md);
//	for (int i = 0; i < 5; i++) {
//		// c.add(Calendar.MONTH, -1);
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		c.add(Calendar.DAY_OF_MONTH, -1);
//		Date lastDate = c.getTime();
//		String lastDate1 = format2.format(lastDate);
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		Date firstDate = c.getTime();
//		String firstDate1 = format2.format(firstDate);
//		//System.out.println(firstDate1 + "---" + lastDate1);
//		// 历史话费
//		Map<String, String> map3 = new HashMap<String, String>();
//
//		map3.put("billType", "NEW_GSM");
//		map3.put("startDate", firstDate1);
//		map3.put("endDate", lastDate1);
//		map3.put("filterfield", "输入对方号码：");
//		map3.put("filterfield", "");
//		map3.put("filterValue", "");
//		map3.put("searchStr", "-1");
//		map3.put("index", "0");
//		map3.put("r", String.valueOf(System.currentTimeMillis()));
//		map3.put("isCardNo", "0");
//		map3.put("gprsType", "");
//		String text3 = cutil.post( otherMonthDetail, map3);
//		//System.out.println(text3);
//
//	
//		String[] strs = text3.split(";value = \\u005B");
//		String[] strs2 = strs[1].split("];members");
//		
//		String str2 = strs2[0];
//		String[] strs3 = str2.split("],");
//		String cyear = lastDate1.substring(0, 4);
//		java.text.DateFormat format3 = new java.text.SimpleDateFormat(
//				"yyyy-MM-dd HH:mm:ss");
//
//		for (int j = 0; j < strs3.length; j++) {
//			String nstr = strs3[j].replace("]", "");
//			//System.out.println(j + "-----" + nstr);
//			String[] nstrs = nstr.split("',");
//			String day = cyear + "-" + nstrs[1].replace("'", "");
//			Date cTime = null;
//			try {
//				cTime = format3.parse(day);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				logger.info("第343行捕获异常：",e);		
//				e.printStackTrace();
//			}
//
//			if (nstrs != null && nstrs.length > 8) {
//				Map map = new HashMap();
//				map.put("phone",login.getLoginName());
//				map.put("cTime", cTime);
//				List<MobileDetail> mobileTelList = (List) service.getMobileDetailService()
//						.getMobileDetailBypt(map);
//				MobileDetail mDetail = null;
//				int iscm = 0;
//				String tradeAddr = nstrs[2].replace("'", "");
//				String tradeWay = nstrs[3].replace("'", "");
//				String recevierPhone = nstrs[4].replace("'", "");
//				String tradeTime = nstrs[5].replace("'", "");
//				int times = 0;
//				try{
//					TimeUtil tunit = new TimeUtil();
//					times = tunit.timetoint(tradeTime);
//				}catch(Exception e){
//					
//				}
//				String tradeType = nstrs[6].replace("'", "");
//				String taocan = nstrs[7].replace("'", "");
//				BigDecimal onlinePay = new BigDecimal(nstrs[8].replace("'",
//						""));
//
//				if (mobileTelList != null && mobileTelList.size() > 0) {
//					mDetail = mobileTelList.get(0);
//					iscm = mDetail.getIscm();
//					if (iscm == 0) {
//						continue;
//					} else {
//						mDetail.setcTime(cTime);
//						mDetail.setTradeAddr(tradeAddr);
//						mDetail.setTradeWay(tradeWay);
//						mDetail.setRecevierPhone(recevierPhone);
//						mDetail.setTradeTime(times);
//						mDetail.setTradeType(tradeType);
//						mDetail.setTaocan(taocan);
//						mDetail.setOnlinePay(onlinePay);
//						mDetail.setPhone(login.getLoginName());
//						mDetail.setIscm(0);
//						service.getMobileDetailService().update(mDetail);
//					}
//				} else {
//					mDetail = new MobileDetail();
//					UUID uuid = UUID.randomUUID();
//					mDetail.setId(uuid.toString());
//					mDetail.setcTime(cTime);
//					mDetail.setTradeAddr(tradeAddr);
//					mDetail.setTradeWay(tradeWay);
//					mDetail.setRecevierPhone(recevierPhone);
//					mDetail.setTradeTime(times);
//					mDetail.setTradeType(tradeType);
//					mDetail.setTaocan(taocan);
//					mDetail.setOnlinePay(onlinePay);
//					mDetail.setPhone(login.getLoginName());
//					mDetail.setIscm(0);
//					service.getMobileDetailService().saveMobileDetail(mDetail);
//				}
//
//			}
//		}
//	}
//
//}
//
//public void messageHistory() {
//	currentMessage();//当月短信记录
//	beforeMessage();//历史短信记录
//}
//public void currentMessage(){
//	java.text.DateFormat format2 = new java.text.SimpleDateFormat(
//			"yyyy-MM-dd");
//	Calendar c = Calendar.getInstance();
//	Date nowDate = c.getTime();
//	c.set(Calendar.DAY_OF_MONTH, 1);
//	Date firstDates = c.getTime();
//	String firstDate2 = format2.format(firstDates);
//	String lastDate2 = format2.format(nowDate);
//	System.out.println(firstDate2 + "---" + lastDate2);
//	String cyear = firstDate2.substring(0, 4);
//	// 当月通话记录
//	
//	Map<String, String> map2 = new HashMap<String, String>();
//	map2.put("billType", "NEW_SMS");
//	map2.put("startDate", firstDate2);
//	map2.put("endDate", lastDate2);
//	map2.put("jingque", "");
//	map2.put("searchStr", "-1");
//	map2.put("index", "0");
//	map2.put("r", String.valueOf(System.currentTimeMillis()));
//	map2.put("isCardNo", "0");
//	map2.put("gprsType", "");
//	String str = cutil.post(currentMessageDetail, map2);
//	
//	System.out.println("######:"+str);
//
//	String[] strs = str.split(";value =");
//	String[] strs2 = strs[1].split(";members");
//	String strsContent = strs2[0].replace("[[", "[");
//	strsContent = strsContent.replace("]]", "]");
//	
//	String[] strs3 = strsContent.split("],");
//	java.text.DateFormat format3 = new java.text.SimpleDateFormat(
//			"yyyy-MM-dd HH:mm:ss");
//	
//	for (int j = 0; j < strs3.length; j++) {
//		String nstr = strs3[j].replace("]", "");
//		nstr = nstr.replace("\"", "");
//		//System.out.println(j + "-----" + nstr);
//		String[] nstrs = nstr.split("',");
//		String day = cyear + "-" + nstrs[1].replace("'", "");
//		Date sentTime = null;
//		try {
//			sentTime = format3.parse(day);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			logger.info("第343行捕获异常：",e);		
//			e.printStackTrace();
//		}
//
//		if (nstrs != null && nstrs.length > 8) {
//			Map map = new HashMap();
//			map.put("phone",login.getLoginName());
//			map.put("sentTime", sentTime);
//			List<MobileMessage> mobileMessageList = (List) service.getMobileMessageService().getByPhone(map);
//			MobileMessage mMessage = new MobileMessage();
//
//			String tradeAddr = nstrs[2].replace("'", "");
//			String receivePhone = nstrs[3].replace("'", "");
//			String tradeWay = nstrs[4].replace("'", "");
//			String allPay = nstrs[8].replace("'", "");
//
//			UUID uuid = UUID.randomUUID();
//
//			if (mobileMessageList != null && mobileMessageList.size() > 0) {
//			} else {
//				mMessage.setAllPay(new BigDecimal(allPay));
//				mMessage.setCreateTs(new Date());
//				mMessage.setPhone(login.getLoginName());
//				mMessage.setRecevierPhone(receivePhone);
//				mMessage.setSentAddr(tradeAddr);
//				mMessage.setSentTime(DateUtils.StringToDate(day, "yyyy-MM-dd HH:mm:ss"));
//				mMessage.setTradeWay(tradeWay);
//				mMessage.setId(uuid.toString());
//
//				service.getMobileMessageService().save(mMessage);
//			}
//
//		}
//	}
//}
//	
//	
//public void beforeMessage(){
//	java.text.DateFormat format2 = new java.text.SimpleDateFormat(
//			"yyyy-MM-dd");
//	Calendar c = Calendar.getInstance();
//	MobileMessage md = new MobileMessage();
//	md.setPhone(login.getLoginName());
//	md = service.getMobileMessageService().getMaxSentTime(login.getLoginName());
//	for (int i = 0; i < 5; i++) {
//		// c.add(Calendar.MONTH, -1);
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		c.add(Calendar.DAY_OF_MONTH, -1);
//		Date lastDate = c.getTime();
//		String lastDate1 = format2.format(lastDate);
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		Date firstDate = c.getTime();
//		String firstDate1 = format2.format(firstDate);
//		//System.out.println(firstDate1 + "---" + lastDate1);
//		// 历史话费
//		Map<String, String> map3 = new HashMap<String, String>();
//
//		map3.put("billType", "NEW_SMS");
//		map3.put("startDate", firstDate1);
//		map3.put("endDate", lastDate1);
//		map3.put("filterfield", "输入对方号码：");
//		map3.put("filterfield", "");
//		map3.put("filterValue", "");
//		map3.put("searchStr", "-1");
//		map3.put("index", "0");
//		map3.put("r", String.valueOf(System.currentTimeMillis()));
//		map3.put("isCardNo", "0");
//		map3.put("gprsType", "");
//		String text3 = cutil.post( otherMonthDetail, map3);
//		
//		System.out.println("######:"+text3);
//
//		String cyear = lastDate1.substring(0, 4);
//		String[] strs = text3.split(";value =");
//		String[] strs2 = strs[1].split(";members");
//		String strsContent = strs2[0].replace("[[", "[");
//		strsContent = strsContent.replace("]]", "]");
//		
//		String[] strs3 = strsContent.split("],");
//		java.text.DateFormat format3 = new java.text.SimpleDateFormat(
//				"yyyy-MM-dd HH:mm:ss");
//		for (int j = 0; j < strs3.length; j++) {
//			String nstr = strs3[j].replace("]", "");
//			nstr = nstr.replace("\"", "");
//			//System.out.println(j + "-----" + nstr);
//			String[] nstrs = nstr.split("',");
//			String day = cyear + "-" + nstrs[1].replace("'", "");
//			Date sentTime = null;
//			try {
//				sentTime = format3.parse(day);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				logger.info("第343行捕获异常：",e);		
//				e.printStackTrace();
//			}
//
//			if (nstrs != null && nstrs.length > 8) {
//				Map map = new HashMap();
//				map.put("phone",login.getLoginName());
//				map.put("sentTime", sentTime);
//				List<MobileMessage> mobileMessageList = (List) service.getMobileMessageService().getByPhone(map);
//				MobileMessage mMessage = new MobileMessage();
//				String tradeAddr = nstrs[2].replace("'", "");
//				String receivePhone = nstrs[3].replace("'", "");
//				String tradeWay = nstrs[4].replace("'", "");
//				String allPay = nstrs[8].replace("'", "");
//				System.out.println(day+"#"+tradeWay+"#"+tradeAddr+"#"+receivePhone);
//				UUID uuid = UUID.randomUUID();
//
//				if (mobileMessageList != null && mobileMessageList.size() > 0) {
//				} else {
//					mMessage.setAllPay(new BigDecimal(allPay));
//					mMessage.setCreateTs(new Date());
//					mMessage.setPhone(login.getLoginName());
//					mMessage.setRecevierPhone(receivePhone);
//					mMessage.setSentAddr(tradeAddr);
//					mMessage.setSentTime(DateUtils.StringToDate(day, "yyyy-MM-dd HH:mm:ss"));
//					mMessage.setTradeWay(tradeWay);
//					mMessage.setId(uuid.toString());
//
//					service.getMobileMessageService().save(mMessage);
//				}
//
//			}
//		}
//	}
//}
