package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.DateFormat;
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

import org.apache.commons.collections.map.LinkedMap;
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
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.AbstractCrawler;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class AHYidong extends BaseInfoMobile {
	private static String index_param = "ahyidong_index_param";	
	private static String yu_eReferer = "http://service.ah.10086.cn/pub/nineNavIndex.action?kind=200011522&f=200011535&ajax_url=/fee/currentFeeInfo.action?area=MH";
	private static String yuezhangdanReferer = "http://service.ah.10086.cn/pub/nineNavIndex.action?kind=200011522&f=200011536&ajax_url=/fee/queryMonthBillIndex.action?area=cd";	
    private static String xiangdanReferer = "http://service.ah.10086.cn/pub/nineNavIndex.action?kind=200011522&f=200011538&ajax_url=/fee/billDetailIndex.action?area=cd";
	public AHYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_ah_yidong, currentUser);
	}
	public AHYidong(Login login, IWarningService warningService,
			String currentUser) {
		super(login, warningService, ConstantNum.comm_ah_yidong, currentUser);
	}
	public AHYidong(Login login) {
		super(login);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Login login = new Login("18326020113", "284279");
//		Login login = new Login("18326020119", "284279");
		
		
		AHYidong hn = new AHYidong(login, null);
		hn.index();
		// 验证码
		hn.inputCode(hn.getImgUrl());
		// 登陆
		Map<String,Object> map = hn.login();
		System.out.println("请输入手机口令：");
		hn.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
		hn.checkPhoneDynamicsCode();
		
		
		
		
		hn.close();
		hn.getFlow();
		//hn.callHistory();
		//hn.getTelDetailHtml();
	}
	
	public void init() {
		if (!isInit()) {
			try {
				String text = cutil.get("https://ah.ac.10086.cn/login");
				Document doc = Jsoup.parse(text);
				Map<String, String> params = new HashMap<String, String>();
				params.put("spid", doc.select("input[name=spid]").val());
				params.put("RelayState", doc.select("input[name=RelayState]")
						.val());
				params.put("backurl", doc.select("input[name=backurl]").val());
				params.put("errorurl", doc.select("input[name=errorurl]").val());
				params.put("loginType", doc.select("input[name=loginType]")
						.val());
				params.put("formertype", doc.select("input[name=formertype]")
						.val());
				params.put("login_pwd_type",
						doc.select("input[name=login_pwd_type]").val());
				params.put("type", doc.select("input[name=type]").val());
				setImgUrl("https://ah.ac.10086.cn/common/image.jsp");
				getAuthcode();
				params.put("SAMLart", doc.select("input[name=SAMLart]").val());
				redismap.put(index_param, params);
				setInit();
			} catch (Exception e) {
				e.printStackTrace();

			}

		}
	}
  
	public Map<String, Object> login() {
	 try{

		Map<String, String> param = (Map<String, String>) redismap.get(index_param);
		CHeader h = new CHeader();
		h.setAccept(CHeaderUtil.Accept_json);
		h.setX_requested_with(true);
		param.put("mobileNum", login.getLoginName());
		param.put("servicePassword", login.getPassword());
		param.put("loginBackurl", "");
		// 判断是否需要获取验证码，需要则获取验证码,手动输入
		param.put("login_type_ah", "");
		param.put("servicePassword_1", "");
		param.put("smsValidCode", "");
		param.put("timestamp", System.currentTimeMillis() + "");
		param.put("validCode", login.getAuthcode());
		String text = cutil.post("https://ah.ac.10086.cn/Login",h, param);//第一
		//错误判断
		if(text.contains("errorPage.jsp")){
			errorMsg = getErrorText(text);	
		}else{
			Document doc = Jsoup.parse(text);
			param.put("RelayState", doc.select("input[name=RelayState]").val());
			param.put("displayPics", doc.select("input[name=displayPics]").val());
			param.put("displayPic", doc.select("input[name=displayPic]").val());
			param.put("SAMLart", doc.select("input[name=SAMLart]").val());
			param.put("isEncodePassword", doc.select("input[name=isEncodePassword]").val());
			h = new CHeader();
			h.setAccept(CHeaderUtil.Accept_);
			text = cutil.post("https://ah.ac.10086.cn/4login/backPage.jsp",h, param); // 第二
			
	
			String Elem_t = param.get("RelayState");
			RegexPaserUtil rp = new RegexPaserUtil("ex.jsp%3Ft%3D", ";nl=3;",
					Elem_t, RegexPaserUtil.TEXTEGEXANDNRT);
			
			String t = rp.getText();
	
			param = new LinkedHashMap<String, String>();
			doc = Jsoup.parse(text);
			param.put("SAMLart", doc.select("input[name=SAMLart]").val());
			param.put("RelayState", doc.select("input[name=RelayState]").val());
			text = cutil.post("http://service.ah.10086.cn/my/index.jsp", param); // 第三
			//System.out.println(text);
	
			rp = new RegexPaserUtil("rad%3D", "$", text,RegexPaserUtil.TEXTEGEXANDNRT);
			String rad = rp.getText();
			
			rp = new RegexPaserUtil("sso%3D", "%26rad", text,RegexPaserUtil.TEXTEGEXANDNRT);
			String sso = rp.getText();
	
			rp = new RegexPaserUtil("backurl=", "$", Elem_t,RegexPaserUtil.TEXTEGEXANDNRT);
			
			String login_backurl = rp.getText();
			
			text = cutil.get("http://service.ah.10086.cn/login/returnUrl.action?login_backurl="+login_backurl); // 第四   
			//System.out.println(text);
	
			text = cutil.get("http://service.ah.10086.cn/my/index.jsp?t=" + t
					+ "&sso=" + sso + "&rad=" + rad); // 第五步  
			//System.out.println(text);
			param = new LinkedHashMap<String, String>();
			param.put("requestFlag","asynchronism");
			//余额查询
			String text1 = cutil.post("http://service.ah.10086.cn/fee/currentFeeInfo.action?t=0.5540844045800362",param);
			rp = new RegexPaserUtil("totalLateFee\":\"", "\",\"totalUnchargedSum", text1,RegexPaserUtil.TEXTEGEXANDNRT);
			String phoneRemain = rp.getText();
			System.out.println(phoneRemain);
			redismap.put("phoneRemain",phoneRemain);
			if(text.contains("<em class=\"th\">号码:")){
				System.out.println("成功");
				loginsuccess();
				sendPhoneDynamicsCode();
			  }
			}
	 }catch(Exception e){
		 e.printStackTrace();
		 writeLogByLogin(e);	 
	 }
	 if(status == 1){
		 addTask_1(this);
	 }
	 return map;
	}
	//月账单差查询
	public Map<String, Object> getbill(){
		try {	
		Map<String,String> params = new LinkedHashMap<String, String>();
		CHeader h = new CHeader();
		List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
		int i = 1;
		for (String s : ms) {
		String starDate= s;
		String text = null;
		String tcwdxf = null;
		String tcwyytxf = null;
		String zzywf = null;
		String dependCycle = null;
		String cAllBalance = null;
		String cAllPay = null;
	    if(i==1) {//本月的请求
	    	 text = cutil.post("http://service.ah.10086.cn/fee/queryMonthBillIndex.action?area=cd&kind=200011522",h,params);
	    	 i++;
	    }else {//其他月份的请求
	    	 text = cutil.post("http://service.ah.10086.cn/fee/queryMonthBillIndex.action?beginDate="+starDate+"&kind=200011522",h,params);
	    }
	   
	    if(text.contains("我的账单")){
		Document doc = Jsoup.parse(text);
		Element element01 = doc.select("table").get(0);
		//计费周期
		
		try {
			dependCycle = element01.select("table>tbody>tr:eq(2)>td:eq(1)").text();
			element01 = doc.select("table").get(2);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//话费账户余额:
		
		try {
			cAllBalance = element01.select("table>tbody>tr:eq(1)>td:eq(1)").text();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//本期总费用 
		
		try {
			element01 = doc.select("table").get(5);
			cAllPay = element01.select("table>tbody>tr:eq(0)>td:eq(6)").text();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//增值业务费
		
		try {
			element01 = doc.select("table").get(4);
			zzywf = element01.select("table>tbody>tr:eq(3)>td:eq(1)").text();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//套餐外语音通信费
		
		try {
			element01 = doc.select("table").get(4);
			tcwyytxf = element01.select("table>tbody>tr:eq(1)>td:eq(1)").text();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//套餐外短彩信费
		
		try {
			element01 = doc.select("table").get(4);
			tcwdxf = element01.select("table>tbody>tr:eq(2)>td:eq(1)").text();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				

		Map map2 = new HashMap();
		map2.put("phone", login.getLoginName());
		map2.put("cTime", StringToDate(s, "yyyyMM"));
		List list = mobileTelService.getMobileTelBybc(map2);
		if(list==null || list.size()==0){
			MobileTel mobieTel = new MobileTel();
			UUID uuid = UUID.randomUUID();
			mobieTel.setId(uuid.toString());
			mobieTel.setcTime(DateUtils.StringToDate(s, "yyyyMM"));
			mobieTel.setTeleno(login.getLoginName());
			try {
				if (dependCycle != null) {
					mobieTel.setDependCycle(dependCycle);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (tcwyytxf != null) {
					mobieTel.setTcwyytxf(new BigDecimal(tcwyytxf));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (tcwdxf != null) {
					mobieTel.setTcwdxf(new BigDecimal(tcwdxf));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (cAllBalance != null) {
					mobieTel.setTcgdf(new BigDecimal(cAllBalance));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (cAllPay != null) {
					mobieTel.setTcwyytxf(new BigDecimal(cAllPay));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (cAllPay != null) {
					mobieTel.setcAllPay(new BigDecimal(cAllPay));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (zzywf != null) {
					mobieTel.setZzywf(new BigDecimal(zzywf));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mobieTel.setIscm(1);
			mobileTelService.saveMobileTel(mobieTel);
			}
		   } else {
				MobileTel mobieTel = new MobileTel();
				UUID uuid = UUID.randomUUID();
				mobieTel.setId(uuid.toString());
				mobieTel.setcTime(DateUtils.StringToDate(s, "yyyyMM"));
				mobieTel.setTeleno(login.getLoginName());
				mobieTel.setcAllPay(new BigDecimal(0));
				mobieTel.setIscm(1);
				String cycle = s.substring(0, 4) + "年"
							+ s.substring(4, 6) + "月01日 至 " + s.substring(0, 4)
							+ "年" + s.substring(4, 6) + "月"
							+ DateUtils.getDaysOfMonth(s) + "日";
				mobieTel.setDependCycle(cycle);
				mobileTelService.saveMobileTel(mobieTel);
		   }
		 }
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}

	//详单查询
	private Map<String, Object> callHistory(){
		try {
			
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			MobileDetail detail = new MobileDetail();
			detail.setPhone(login.getLoginName());
			detail = mobileDetailService.getMaxTime(detail);
			boolean b = true;
			int mon = 0;//标示是否是本月
			for (String s : ms) {
				String starDate= s+"01";
				String endDate= s+"31";
				for(int p = 1;p < 20; p++){		
					String page = Integer.toString(p);
				String callContent =  cutil.get("http://service.ah.10086.cn/fee/billDetailInfoPage.action?nowPage="+page+"&begin="+starDate+"&end="+endDate+"&detail_type=205");
				if(callContent==null){continue;}
				if(callContent != null){
					if(callContent.contains("查询时段")){
					 Document doc  = Jsoup.parse(callContent);
					 System.out.println(doc.toString());
					 Element table1 = doc.getElementById("cdrDetailDiv");

							 Elements trs = table1.select("tr[name=billTrName1]");
							 if(trs.size() > 0){
							 for (int j = 0; j < trs.size(); j++) {
								Elements tds =trs.get(j).select("td");
								String cTime = tds.get(0).text();
								String tradeAddr = tds.get(1).text();
								String tradeWay = tds.get(2).text();
								String recevierPhone = tds.get(3).text();
								String tradeTime1 = tds.get(4).text();//通话时间
								
								int tradeTime = TimeUtil.timetoint(tradeTime1);
								
								String tradeType = tds.get(5).text();
								String taocan = tds.get(6).text();
								String onlinePay1 = tds.get(7).text();
								BigDecimal onlinePay =new BigDecimal(onlinePay1);	
								
								MobileDetail mobileDetail = new MobileDetail();
								UUID uuid = UUID.randomUUID();
								mobileDetail.setId(uuid.toString());
								mobileDetail.setcTime(DateUtils.StringToDate(cTime, "yyyy-MM-dd HH:mm:ss"));
								
								if(detail!=null&&mobileDetail.getcTime().getTime()<=detail.getcTime().getTime()){
									b =false;
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
								if(mon==0) {
									mobileDetail.setIscm(0);
								}else {
									mobileDetail.setIscm(1);
								}
								
								mobileDetailService.saveMobileDetail(mobileDetail);
							 } 
							 }
				}
				if (callContent.contains("获取帐期起止时间出错")){
					errorMsg = "QryPCASRequest.pc-InsPCASRequest line 693: [获取帐期起止时间出错[1403]]"; 
				}
				if(!b){
					break;
				}
			  }
			}
			mon++;//标示本月
		  }
				
		} catch (Exception e) {
			writeLogByHistory(e);
		}
		return map;
	}
	
	//短信详单
	private Map<String, Object> getMessage(){
		try {
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			MobileMessage mobilemessage = new MobileMessage();
			mobilemessage.setPhone(login.getLoginName());
			mobilemessage = mobileMessageService.getMaxSentTime(login.getLoginName());
			Boolean b = true;
			for (String s : ms) {
				String starDate= s+"01";
				String endDate= s+"31";
				//System.out.println(starDate);
				//System.out.println(endDate);
				for(int p = 1; p < 20; p++){                      
					String page = Integer.toString(p);
				String message =  cutil.get("http://service.ah.10086.cn/fee/billDetailInfoPage.action?nowPage="+page+"&begin="+starDate+"&end="+endDate+"&detail_type=401");
				//System.out.println(message);
				if(message==null){continue;}   // http://service.ah.10086.cn/fee/billDetailInfoPage.action?nowPage=1&begin=20140801&end=20140831&detail_type=401
				if(message!=null){			//http://service.ah.10086.cn/fee/billDetailInfoPage.action?nowPage="+p+"&begin="+starDate+"&end="+endDate+"&detail_type=401
					//if(message.contains("查询时段")){
						Document doc  = Jsoup.parse(message);
						 Elements table1 = doc.select("table[id=cdrDetailDiv]");

								 Elements trs = table1.select("tr[name=billTrName1]");
								 for (int j = 0; j < trs.size(); j++) {
									Elements tds =trs.get(j).select("td");
									String sentTime = tds.get(0).text();
									String recevierPhone = tds.get(1).text();//对方号码
									String tradeWay = tds.get(2).text();//通信方式
									String allPay1 = tds.get(6).text();//费用
									BigDecimal allPay = new BigDecimal(allPay1);
									MobileMessage message1 = new MobileMessage();
									UUID uuid = UUID.randomUUID();
									message1.setId(uuid.toString());
									message1.setSentTime(DateUtils.StringToDate(sentTime, "yyyy-MM-dd HH:mm:ss"));
									
									if(mobilemessage!=null&&message1.getSentTime().getTime()<=mobilemessage.getSentTime().getTime()){
										b =false;
										break;
									}
									message1.setRecevierPhone(recevierPhone);
									message1.setTradeWay(tradeWay);
									message1.setAllPay(allPay);
									message1.setPhone(login.getLoginName());
									mobileMessageService.save(message1);
								 } 
						 }
							if (message.contains("获取帐期起止时间出错")){
								errorMsg = "QryPCASRequest.pc-InsPCASRequest line 693: [获取帐期起止时间出错[1403]]"; 
							}
					}

					if(!b){
						break;
					}
				}
			
		//}
		}catch (Exception e) {
			writeLogByHistory(e);
		}
		return map;
	}

	//个人信息
	public  Map<String,Object> getMyInfo()  {
		try{
		String text = cutil.get("http://service.ah.10086.cn/operate/user!check.action?f=200011570&kind=200011523&area=cd");
		//System.out.println(text);
		if(text.contains("个人资料变更")){
			parseBegin(Constant.YIDONG);
		Document doc = Jsoup.parse(text);		
		Element element = doc.select("table").get(0);		
		String cName = element.select("table>tbody>tr:eq(0)>td:eq(1)").text();
		String lianxiren = element.select("table>tbody>tr:eq(1)>td:eq(1)").text();
		String teleno = element.select("table>tbody>tr:eq(2)>td:eq(1)").text();
		String youzheng = element.select("table>tbody>tr:eq(3)>td:eq(1)").text();
		String dizhi = element.select("table>tbody>tr:eq(4)>td:eq(1)").text();
		String email = element.select("table>tbody>tr:eq(5)>td:eq(1)").text();
		String danwei = element.select("table>tbody>tr:eq(6)>td:eq(1)").text();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentId", currentUser);
		map.put("usersource", Constant.YIDONG);
		map.put("loginName", login.getLoginName());
		List<User> list = userService.getUserByParentIdSource(map);
		if (list != null && list.size() > 0) {
			User user = list.get(0);
			user.setLoginName(login.getLoginName());
			user.setLoginPassword("");
			user.setEmail(email);
			user.setIdcard("");
			user.setAddr(dizhi);
			user.setRealName(cName);
			user.setUsersource(Constant.YIDONG);
			user.setUsersource2(Constant.YIDONG);
			user.setParentId(currentUser);
			user.setModifyDate(new Date());
			user.setPhone(login.getLoginName());
			user.setPackageName("");
			user.setPhoneRemain(new BigDecimal(redismap.get("phoneRemain").toString()));//余额
			userService.update(user);
		} else {
			User user = new User();
			UUID uuid = UUID.randomUUID();
			user.setId(uuid.toString());
			
			user.setLoginName(login.getLoginName());
			user.setLoginPassword("");
			user.setEmail(email);
			user.setIdcard("");
			user.setAddr(dizhi);
			user.setRealName(cName);
			user.setUsersource(Constant.YIDONG);
			user.setUsersource2(Constant.YIDONG);
			user.setParentId(currentUser);
			user.setModifyDate(new Date());
			user.setPhone(login.getLoginName());
			user.setPackageName("");
			user.setPhoneRemain(new BigDecimal(redismap.get("phoneRemain").toString()));//余额
			userService.saveUser(user);
		}
		
		}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
		return map;
	}
	
	public   Date StringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	
	public  void startSpider(){
		try{
			parseBegin(Constant.YIDONG);
			switch (login.getType()) {
			case 1:
				getMyInfo(); //个人信息
				getbill();//账单记录
				break;
			case 2:
				getMessage();//短信信息
				callHistory(); //历史通话
				getFlow();	//流量
				break;
			case 3:
				getMyInfo(); //个人信息
				getbill();//账单记录
				getMessage();//短信
				callHistory(); //历史通话
				getFlow();	//流量
				
				break;
				
			}
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}
	

	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
		try {
			Map<String, String> param = new HashMap<String, String>();
			CHeader h = new CHeader();
			h.setRespCharset("utf-8");
			String q = "0085c6cd8ae8ed5c1f114f224d956da093256847af2d1881a10aab16e8bb8cf1549d24f9ab5e528d7d951ca42a359f3a3767a176e56734a8c3049f2427719aa9bfd60803118d38de10cff48ce0c0047ac50b1fd809725151334bbcb07ca2277f313d7e64fa7515c5bb7468f8649f86981e7382315f842efe6e8a3d4e56f03fcec5";
			String exponent = "010001" ;
			param.put("funcCode", "010012");
			param.put("smPass", AbstractCrawler.executeJsFunc("yd/ah_yd_des.js", "getSmPass",exponent,q,login.getPhoneCode()));
			String callContent9 = cutil.post("http://service.ah.10086.cn/pub/randomPwdCheck.action",h,param);
			System.out.println(callContent9);
			if(callContent9.contains("失败")){
				errorMsg = "您好，短信随机码校验失败，请重新获取！";
			}else if(callContent9.contains("失效")){
				errorMsg = "您好，短信随机码已经失效，请重新获取！";
			}else if(callContent9.contains("ok")){
				status = 1;
			}
		    } catch (Exception e) {
		    	errorMsg = "您好，短信随机码校验失败，请重新获取！";
		    }
			if(status==1){
	        	addTask_2(this);
	    	}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
	
	
	/**
	 * 生成手机短信
	 * */
    public Map<String,Object> sendPhoneDynamicsCode(){
    	
    	Map<String,String >param = new LinkedHashMap<String,String>();
		param.put("area","cd");
		param.put("kind","200011522");
		CHeader h = new CHeader();
		h.setReferer(xiangdanReferer);
		String text = cutil.post("http://service.ah.10086.cn/fee/billDetailIndex.action",h,param);
    	param = new LinkedHashMap<String, String>();
		param.put("opCode","5868");	
		String errorMsg = null;
		int status = 0;
		try {
			
			text =  cutil.post("http://service.ah.10086.cn/pub/sendRandomPwd.action",h,param);
			if(text.contains("{\"SRandPassAdmOut\":{")){
				status = 1;
			}else if(text.contains("当日使用随机短信次数过多")){
				errorMsg = "当日使用随机短信次数过多";
			}else if(text.contains("无法继续获取")){
				errorMsg = "您好，您获取随机密码次数已达到上限(10)次，无法继续获取。";
			}else{
				errorMsg = "发送失败!";
			}
		} catch (Exception e) {
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
    
    
	private void getFlow() {
		List<String> months = DateUtils.getMonths(6, "yyyyMM");
		for (int i = months.size() - 1; i >= 0; i--) {
			String date = months.get(i);
			
			//http://service.ah.10086.cn/fee/billDetailInfoPage.action?nowPage=1&begin=20141001&end=20141031&detail_type=301
			String startTime = date + "01"; 
			String endTime = DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyyMMdd");
			String text =  cutil.get("http://service.ah.10086.cn/fee/billDetailInfoPage.action?nowPage="+1+"&begin="+startTime+"&end="+endTime+"&detail_type=301");
			
			boolean b = false;
			b = parseFlowBill(text, months.get(i));
		}
	}
    
    
	private boolean parseFlowBill(String text, String months) {
	try {
		Document doc = Jsoup.parse(text);
		//System.out.println(doc.toString());
		if(!doc.toString().contains("通信地点")){
			Date monthly = DateUtils.StringToDate(months, "yyyyMM");
			
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("phone", login.getLoginName());
			map.put("monthly", monthly);
			List<Map> list = mobileOnlineBillService
					.getMobileOnlineBill(map);
			
			if (list == null || list.size() == 0) {
				MobileOnlineBill onlineBill = new MobileOnlineBill();
				UUID uuid = UUID.randomUUID();
				onlineBill.setId(uuid.toString());
				onlineBill.setTotalFlow((long) 0);
				onlineBill.setTrafficCharges(new BigDecimal(0.00));
				onlineBill.setPhone(login.getLoginName());
				onlineBill.setIscm(0);
				onlineBill.setMonthly(monthly);
				MobileOnlineBill bean_bill = mobileOnlineBillService
						.getMaxTime(login.getLoginName());
				if (bean_bill != null && bean_bill.getMonthly() != null) {
					if (bean_bill.getMonthly().getTime() > onlineBill
							.getMonthly().getTime()) {
						return true;
					} else if(bean_bill.getMonthly().getTime() == onlineBill
							.getMonthly().getTime()){
						mobileOnlineBillService.update(onlineBill);
					} else {
						mobileOnlineBillService.save(onlineBill);
					}
				} else {
					mobileOnlineBillService.save(onlineBill);
				}
			}
		} else {
			Date monthly = DateUtils.StringToDate(months, "yyyyMM");
			
			Element table = doc.select("table").get(1);
			String chargeFlow = table.select("tr").get(1).select("th").get(1).text().replaceAll("收费流量", "").replaceAll("\\(", "").replaceAll("\\)", "").trim();
			String freeFlow = table.select("tr").get(1).select("th").get(2).text().replaceAll("免费流量", "").replaceAll("\\(", "").replaceAll("\\)", "").trim();
			String totalFlow = table.select("tr").get(1).select("th").get(3).text().replaceAll("总流量", "").replaceAll("\\(", "").replaceAll("\\)", "").trim();

			Element table3 = doc.select("table").get(3);
			String totalFees = table3.select("tr").get(0).select("td").get(1).text().replace("元", "").trim();
			
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("phone", login.getLoginName());
			map.put("monthly", monthly);
			List<Map> list = mobileOnlineBillService
					.getMobileOnlineBill(map);
			if (list == null || list.size() == 0) {
				MobileOnlineBill onlineBill = new MobileOnlineBill();
				UUID uuid = UUID.randomUUID();
				onlineBill.setId(uuid.toString());
				onlineBill.setPhone(login.getLoginName());
				onlineBill.setIscm(0);
				onlineBill.setMonthly(monthly);
				onlineBill.setTotalFlow(Math.round(StringUtil.flowFormat(totalFlow)));
				onlineBill.setFreeFlow(Math.round(StringUtil.flowFormat(freeFlow)));
				onlineBill.setChargeFlow(Math.round(StringUtil.flowFormat(chargeFlow)));
				onlineBill.setTrafficCharges(new BigDecimal(totalFees));
				MobileOnlineBill bean_bill = mobileOnlineBillService
						.getMaxTime(login.getLoginName());
				if (bean_bill != null && bean_bill.getMonthly() != null) {
					if (bean_bill.getMonthly().getTime() < onlineBill
							.getMonthly().getTime()) {
						mobileOnlineBillService.save(onlineBill);
					} else if(bean_bill.getMonthly().getTime() == onlineBill
							.getMonthly().getTime()){
						mobileOnlineBillService.update(onlineBill);
					}
				} else {
					mobileOnlineBillService.save(onlineBill);
				}
			}
			
			System.out.println(doc.toString());
			if(!doc.toString().contains("您暂无此类消费记录")){
			parseFlowList(doc, months);

			int page = 2;
			while(true){
				String startTime = months + "01"; 
				String endTime = DateUtils.lastDayOfMonth(months, "yyyyMM", "yyyyMMdd");
				//http://service.ah.10086.cn/fee/billDetailInfoPageSyc.action?begin=20141001&end=20141031&nowPage=2&detail_type=301&_=1413511272561
				String  url = "http://service.ah.10086.cn/fee/billDetailInfoPageSyc.action?nowPage="+page+"&begin="+startTime+"&end="+endTime+"&detail_type=301&_="+System.currentTimeMillis();
				String flowHtml =  cutil.get(url);
				Document doc1 = Jsoup.parse(flowHtml);
				System.out.println(doc1.toString());
				if(page == 30){
					break;
				}
				if(!doc1.toString().contains("div") && doc1.toString().contains("body") && doc1.select("body").text().length() > 0){
					parseFlowListNext(doc1, months);
					page ++;
				} else {
					break;
				}
			}
			}
		}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 return false;
		}
		return true;
	}
	
	public void parseFlowListNext(Document doc, String startDate) throws Exception {
		String html = doc.select("body").text().trim();
		String []info = html.split(" ");
		int num = info.length / 10;
		
		for (int j = 0; j < num; j++) {
			
			String cTime1 = info[j*10 + 0] + " " + info[j*10 + 1];// 起始时间
			Date cTime = DateUtils.StringToDate(cTime1, "yyyy-MM-dd HH:mm:ss");
			String tradeAddr = info[j*10 + 2];// 通信地点
			String onlineType = info[j*10 + 3];// 上网方式
			// 总流量
			String totalFlow1 = info[j*10 + 4].replaceAll("\\(K\\)", "KB")
					.replaceAll("\\(M\\)", "MB").replaceAll("\\(G\\)", "GB").trim();
			long totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
			// 套餐优惠
			String cheapService = info[j*10 + 5] + " " + info[j*10 + 6];
			String communicationFees1 = info[j*10 + 7].trim();// 通信费
			if(communicationFees1.equals("")){
				communicationFees1 = "0";
			}
			BigDecimal communicationFees = new BigDecimal(communicationFees1);

			MobileOnlineList onlineList = new MobileOnlineList();
			UUID uuid = UUID.randomUUID();
			onlineList.setId(uuid.toString());
			onlineList.setPhone(login.getLoginName());
			onlineList.setcTime(cTime);
			onlineList.setCheapService(cheapService);
			onlineList.setCommunicationFees(communicationFees);
			onlineList.setOnlineType(onlineType);
			onlineList.setTotalFlow(totalFlow);
			onlineList.setTradeAddr(tradeAddr);
			MobileOnlineList bean_List = new MobileOnlineList();
			bean_List.setPhone(login.getLoginName());
			bean_List = mobileOnlineListService
					.getMaxTime(login.getLoginName());
			if (bean_List != null && bean_List.getcTime() != null) {
				if (bean_List.getcTime().getTime() < onlineList.getcTime()
						.getTime()) {
					mobileOnlineListService.save(onlineList);
				} else if (bean_List.getcTime().getTime() == onlineList
						.getcTime().getTime()) {
					break;
				} else {
					break;
				}
			} else {
				mobileOnlineListService.save(onlineList);
			}
		}
	}
	
	public void parseFlowList(Document doc, String startDate) throws Exception {
		Element table = doc.select("table").get(2);
		Elements trs = table.select("tr");
		for (int j = 1; j < trs.size(); j++) {
			Elements tds = trs.get(j).select("td");

			Date cTime = null;
			String tradeAddr = null;
			String onlineType = null;
			long totalFlow = 0;
			String cheapService = null;
			String cTime1 = tds.get(0).text().trim();// 起始时间
			cTime = DateUtils.StringToDate(cTime1, "yyyy-MM-dd HH:mm:ss");
			tradeAddr = tds.get(1).text().trim();// 通信地点
			onlineType = tds.get(2).text();// 上网方式
			long onlineTime = 0;
			// 总流量
			String totalFlow1 = tds.get(3).text().replaceAll("\\(K\\)", "KB")
					.replaceAll("\\(M\\)", "MB").replaceAll("\\(G\\)", "GB").trim();
			totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
			// 套餐优惠
			cheapService = tds.get(4).text().trim();
			String communicationFees1 = tds.get(5).text().trim();// 通信费
			BigDecimal communicationFees = new BigDecimal(communicationFees1);

			MobileOnlineList onlineList = new MobileOnlineList();
			UUID uuid = UUID.randomUUID();
			onlineList.setId(uuid.toString());
			onlineList.setPhone(login.getLoginName());
			onlineList.setcTime(cTime);
			onlineList.setCheapService(cheapService);
			onlineList.setCommunicationFees(communicationFees);
			onlineList.setOnlineTime(onlineTime);
			onlineList.setOnlineType(onlineType);
			onlineList.setTotalFlow(totalFlow);
			onlineList.setTradeAddr(tradeAddr);
			MobileOnlineList bean_List = new MobileOnlineList();
			bean_List.setPhone(login.getLoginName());
			bean_List = mobileOnlineListService
					.getMaxTime(login.getLoginName());
			if (bean_List != null && bean_List.getcTime() != null) {
				if (bean_List.getcTime().getTime() < onlineList.getcTime()
						.getTime()) {
					mobileOnlineListService.save(onlineList);
				} else if (bean_List.getcTime().getTime() == onlineList
						.getcTime().getTime()) {
					break;
				} else {
					break;
				}
			} else {
				mobileOnlineListService.save(onlineList);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**在第一次登录过程中返回的错误代码
	 * @param codeValue
	 * @return
	 */
	public   String getErrorText(String codeValue){
		if(codeValue!=null){
			if(codeValue.contains("2003")){
				return "您好，请输入正确的图形验证码。!";
			}else if(codeValue.contains("2009")){
				return "您好，您的图形验证码已失效，请重新获取";
			}else if(codeValue.contains("5002")){
				return "您好，系统未查询到您的手机号和用户名。。";
			}else if(codeValue.contains("4001")&&codeValue.contains("authCount=1")){
				return 	"您好，您今天已输错1次密码，还有4次机会，输错5次后账户将被锁定至次日0时";
			}else if(codeValue.contains("4001")&&codeValue.contains("authCount=2")){
				return 	"您好，您今天已输错2次密码，还有3次机会，输错5次后账户将被锁定至次日0时";
			}else if(codeValue.contains("4001")&&codeValue.contains("authCount=3")){
				return 	"您好，您今天已输错3次密码，还有2次机会，输错5次后账户将被锁定至次日0时";
			}else if(codeValue.contains("4001")&&codeValue.contains("authCount=4")){
				return 	"您好，您今天已输错4次密码，还有1次机会，输错5次后账户将被锁定至次日0时";
			}else if(codeValue.contains("4001")&&codeValue.contains("authCount=5")){
				return 	"您好，您今天已输错5次密码，还有0次机会，输错5次后账户将被锁定至次日0时";
			}else if(codeValue.contains("5001")){
				return "您好，您今天已输错5次密码，为了保障账号的安全，账号将被锁定至次日0时";
			}
		}
		return "请重新获取";
	}

}
