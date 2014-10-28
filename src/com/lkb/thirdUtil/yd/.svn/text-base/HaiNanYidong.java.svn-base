package com.lkb.thirdUtil.yd;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
 * 海南移动
 * 
 * @author li
 * @date 2014-9-12 
 */
public class HaiNanYidong extends BaseInfoMobile {
	private static final Logger log = Logger.getLogger(HaiNanYidong.class);
	private static String index_param = "hainanyidong_index_param";	
	public static String balance = null;//保存余额
	public String imgUrl = "https://hi.ac.10086.cn/sso3/common/image.jsp";
	public String MonthTelUrl = "http://www.hi.10086.cn/service/bill/billNewQuery.do";
	public String CUST_NAME = "";
	public static int displayPic = 0;
	String date = "";
	
	/**
	 * 本地测试
	 * 
	 * @param login
	 * @param currentUser
	 */
	public HaiNanYidong(Login login) {
		super(login);
	}

	public HaiNanYidong(Login login,String currentUser) {
		super(login,  ConstantNum.comm_hain_yidong, currentUser);
	}
	
	public HaiNanYidong(Login login, IWarningService warningService,
			String currentUser) {
		super(login, warningService, ConstantNum.comm_hain_yidong, currentUser);
	}
	
	public static void main(String[] args) {
		Login login = new Login("13687595621", "418840");
//		Login login = new Login("13687595626", "418840");
		HaiNanYidong hn = new HaiNanYidong(login);
		
		hn.index();
		hn.inputCode(hn.getImgUrl());
		// 登陆
		hn.login(); 
		hn.getbill();
		//hn.sendPhoneDynamicsCode();
//		System.out.println("请输入手机口令：");
//		hn.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
//		hn.checkPhoneDynamicsCode();
//		hn.getMyInfo();
//		hn.callHistory();
//		hn.getMessage();
		hn.close();
	}
	
	public void init(){
	if (!isInit()) {
		try {
		String url = "https://hi.ac.10086.cn/login";
		CHeader header = new CHeader("http://www.10086.cn/hi/");
		String text = cutil.get(url,header);
		if (text!=null) {
			Map<String,String> params = new LinkedHashMap<String, String>();
			Document doc = Jsoup.parse(text);
			Elements form = doc.select("form#oldLogin");
			String spid = form.select("[name=spid]").attr("value");//
			String RelayState = form.select("[name=RelayState]").attr("value");//
			String type = form.select("[name=type]").attr("value");//
			String backurl = form.select("[name=backurl]").attr("value");//
			String errorurl = form.select("[name=errorurl]").attr("value");//
			String Passwordtype = form.select("[name=Password-type]").attr("value");
			String mobileNum = null;
			params.put("spid", spid);
			params.put("RelayState", RelayState);
			params.put("type", type);
			params.put("backurl", backurl);
			params.put("errorurl", errorurl);
			params.put("Passwordtype", Passwordtype);
			params.put("mobileNum",mobileNum);
			if(displayPic==1) {
				setImgUrl(imgUrl);
			}
			redismap.put(index_param, params);
			
		}
		setInit();
		}catch (Exception e) {
			e.printStackTrace();
		}
	   }
	}
		
		
	public Map<String, Object> login() {
		Map<String, String> param = new HashMap<String, String>();
		try{
			String text = login_0(param);
			//System.out.println(text);
			if(text.contains("errorPage.jsp")) {
				errorMsg = getErrorText(text);
				displayPic = 1;  //标示出现验证码
			}
		
			if(errorMsg==null&&text!=null){
				text = login_1();
				if(text!=null){
					text = login_2(text);
					//System.out.println(text);
					RegexPaserUtil rp = new RegexPaserUtil("余额：","元",text,RegexPaserUtil.TEXTEGEXANDNRT);
					balance = rp.getText();//余额
					rp = new RegexPaserUtil("入网时间：","</sp",text,RegexPaserUtil.TEXTEGEXANDNRT);
					date = rp.getText();
					errorMsg = "ok";
					loginsuccess();
					//System.out.println(balance);
					addTask_1(this);
					sendPhoneDynamicsCode();
				}
			}
			
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;
	}
	
	public Map<String, Object> sendPhoneDynamicsCode(){
		 Map<String,String> param = new LinkedHashMap<String,String>();
			int status = 0;
			String errorMsg = "" ;
		String url = "http://www.hi.10086.cn/service/user/sendvaildateSmsCode.do";
		CHeader h = new CHeader("http://www.hi.10086.cn/service/login_yzmpasswd.jsp");
		h.setAccept_Language("zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		param.put("getsmscode","true");
		param.put("mobileno",login.getLoginName());
		String text = cutil.post(url,h,param);
		Map<String,Object> map = new HashMap<String,Object>();
		url = "http://www.hi.10086.cn/service/info/input_yzm.jsp";
		text = cutil.get(url,h);
		if (text.contains("频繁")) {
			errorMsg="30秒内只能获取一次验证码，不要频繁获取!";
		}else {
			status = 1;
			errorMsg = "随机短信码已发送，请查收！如果您在3分钟内未收到，请重新点击获取短信码";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
	//短信登陆
	public Map<String, Object> checkPhoneDynamicsCode(){
		String url = "http://www.hi.10086.cn/service/user/vaildateSms.do";
		CHeader h = new CHeader("http://www.hi.10086.cn/service/login_yzmpasswd.jsp");
		Map<String, String> param = new HashMap<String, String>();
		String text = cutil.post("http://www.hi.10086.cn/service/user/vaildateCode.do?vaildateCode="+login.getPhoneCode(),param);
		if (text.contains("true")) {
			Map<String, String> mp = new HashMap<String, String>();
			mp.put("INPASS","ture_aa");
			mp.put("INSMS","ture_aa");
			mp.put("agentcode","");
			mp.put("mobileno",login.getLoginName());
			mp.put("sso","0");
			mp.put("vaildateCode", login.getPhoneCode());
			String text1 = cutil.post(url,h,mp);
			status = 1;
			addTask_2(this);
		}else if(text.contains("false")) {
			status = 0;
			errorMsg="您输入的短信验证码不正确,请核实后重新输入！";
		}
		Map<String, String> mp = new HashMap<String, String>();
		mp.put("INPASS","ture_aa");
		mp.put("INSMS","ture_aa");
		mp.put("agentcode","");
		mp.put("mobileno",login.getLoginName());
		mp.put("sso","0");
		mp.put("vaildateCode", login.getPhoneCode());
		String text1 = cutil.post(url,h,mp);
		
//		if (text.contains("http://www.hi.10086.cn/service")) {
//			
//			status = 1;
//			addTask_2(this);
//		}else if(text.contains("false")) {
//			status = 0;
//			errorMsg="您输入的短信验证码不正确,请核实后重新输入！";
//		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
		try{
			parseBegin(Constant.YIDONG);
			switch (login.getType()) {
			case 1:
				getbill();//历史账单
				break;
			case 2:
				getMyInfo(); //个人信息
				callHistory(); //通话记录	
				getMessage();//短信记录
				getOnline();//流量记录
				break;
			case 3:
				getMyInfo(); //个人信息
				getbill(); //历史账单	
				callHistory(); //通话记录	
				getMessage();//查询短信记录
				getOnline();//流量记录
				break;
			default:
				break;
			}
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}

//		type	B
//		backurl	/sso3/4login/backPage.jsp
//		errorurl	/sso3/4login/errorPage.jsp
//		spid	8a481e862c08afe5012c0a9788590002
//		RelayState	type=B;backurl=http://www.hi.10086.cn/my/;nl=3;loginFrom=http://www.10086.cn/hi/
//		mobileNum	13687595621
//		Password-type	
//		nickName	
//		email	
//		servicePassword	418840
//		Password	
//		emailPwd	
//		ssoPwd	
//		website_pwd_email	
//		smsValidCode	
//		validCode	0000
	  private String login_0(Map<String, String> param){
		String url  = "https://hi.ac.10086.cn/sso3/Login";
		CHeader header = new CHeader("https://hi.ac.10086.cn/login");
		try{
			String text = cutil.get("https://hi.ac.10086.cn/sso3/checkUserInfoServlet?MSISDN="+login.getLoginName());
			if(text.equals("NO")) {
				errorMsg = "如您输入的号码非海南移动手机号码，请切换至手机归属地";
			}else {
				Map<String, String> param1 = (Map<String, String>) redismap.get(index_param);
				param.put("type", param1.get("type").toString());
				param.put("backurl", param1.get("backurl").toString());
				param.put("errorurl", param1.get("errorurl").toString());
				param.put("spid", param1.get("spid").toString());
				param.put("RelayState", param1.get("RelayState").toString());
				param.put("mobileNum", login.getLoginName());
				param.put("Password-type", param1.get("Passwordtype").toString());
				param.put("nickName", "");
				param.put("email", "");
				param.put("servicePassword", login.getPassword());
				param.put("Password", "");
				param.put("emailPwd", "");
				param.put("ssoPwd", "");
				param.put("website_pwd_email", "");
				param.put("smsValidCode", "");
				if(displayPic==1) {
					param.put("validCode",login.getAuthcode());
				}else {
					param.put("validCode","");
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return  cutil.post(url,header,param);
	}
	
	private String login_1(){
		String url = "http://www.hi.10086.cn/my/";
		String text = cutil.get(url);
		//System.out.println(text);
		Document doc = Jsoup.parse(text);
		String SAMLRequest = doc.select("input[name=SAMLRequest]").val();
		String RelayState = doc.select("input[name=RelayState]").val();
		if(SAMLRequest!=null){
			Map< String, String> param = new HashMap<String, String>();
			param.put("SAMLRequest", SAMLRequest);
			param.put("RelayState", RelayState);
			url = "https://hi.ac.10086.cn/sso3/POST";
			CHeader header = new CHeader("http://www.hi.10086.cn/service/mainQuery.do");
			text= cutil.post(url,header, param);
		}
		return text;
	}
	private String login_2(String text){
		Map< String, String> param = new HashMap<String, String>();
		if(text!=null){
		Document doc = Jsoup.parse(text);
		String SAMLart = doc.select("input[name=SAMLart]").val();
		String RelayState = doc.select("input[name=RelayState]").val();
		String displayPic = doc.select("input[name=displayPic]").val();
		if(SAMLart!=null){
			param = new HashMap<String, String>();
			param.put("SAMLart", SAMLart);
			param.put("RelayState", RelayState);
			param.put("displayPic", displayPic);
			String url = "http://www.hi.10086.cn/service/mainQuery.do";
			text = cutil.post(url, param);
			}
		}
		return text;
	}
	//月账单
	private boolean getbill() {
		Map<String, String> param = new HashMap<String, String>();
		try {
			List<String> ms = getMonth(6, "yyyyMM");
			int mm = 0;
			for (String s : ms) {
				String starDate = s;
				param.put("month", s.toString().trim());
				String text = cutil.post(MonthTelUrl,param);
				//System.out.println(text);
				if (text.contains("余额")) {
					Document doc = Jsoup.parse(text);
					Element table = doc.select("table").get(2);
					Elements trs = table.select("tr");
					BigDecimal tcgdf = new BigDecimal(0); // 套餐及固定费
					BigDecimal yytxf = new BigDecimal(0); // 语音通信费
					BigDecimal tcwdxf = new BigDecimal(0); // 套餐外短彩信费
					BigDecimal cAllPay = new BigDecimal(0);//本期总费用
					// trs.size()-2是因为倒数第二个tr中也包含‘合计费用’的信息，出现异常，-2不会对最终结果造成影响，同时少去的对错误字段的遍历，防止异常出现
					for (int i = 1; i < trs.size() - 2; i++) {
						Element tr = trs.get(i);
						String trStr = tr.text();
						if (trStr.contains("套餐及固定费")) {
							String tcgdfs = trStr.replace("11. 套餐及固定费 ￥","");
							try {
								if (tcgdfs != null) {
									tcgdf = new BigDecimal(tcgdfs);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (trStr.contains("语音通信费")) {
							String yytxfs = trStr.replace("22. 语音通信费 ￥", "");
							try {
								if (yytxfs != null) {
									yytxf = new BigDecimal(yytxfs);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else if (trStr.contains("短彩信费")) {
							String tcwdxfs = trStr.replace("33. 短彩信费 ￥", "");
							try {
								if (tcwdxfs != null) {
									tcwdxf = new BigDecimal(tcwdxfs);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (trStr.contains("本月话费")) {

							String cAllPays = trStr.replace("本月话费(￥", "").replace(")","");
							try {
								if (cAllPay != null) {
									cAllPay = new BigDecimal(cAllPays);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
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
						mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
								Integer.parseInt(year),
								Integer.parseInt(formatDateMouth(mouth)))
								+ "至"
								+ TimeUtil.getLastDayOfMonth(Integer
										.parseInt(year), Integer
										.parseInt(formatDateMouth(mouth))));
						mobieTel.setTcwdxf(tcwdxf);
						mobieTel.setcAllPay(cAllPay);
						mobieTel.setTcwyytxf(yytxf);
						String cd = formatDate(new Date());
						if (mm == 0) {
							mobieTel.setIscm(1);
						}else {
							mobieTel.setIscm(0);
						}
						mobileTelService.saveMobileTel(mobieTel);
					}
				}

				mm++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HENYD_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}

		return true;

	}
	 //查询通话记录
	public Map<String,Object> callHistory(){
		try {
			
			CHeader  h = new CHeader("http://www.hi.10086.cn/service/bill/beforeQueryNewDetails.do");
			h.setAccept_Language("zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			MobileDetail detail = new MobileDetail();
			detail.setPhone(login.getLoginName());
			detail = mobileDetailService.getMaxTime(detail);
			boolean b = true;
			String text = null;
			Map<String,String> param = new LinkedHashMap<String,String>();
			for (int k = 0 ;k<ms.size();k++) {
				String starDate = (String)ms.get(k) ;
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				String eday  = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				fday = fday.replace("-", "");
				eday = eday.replace("-", "");//20140631
				param = new LinkedHashMap<String,String>();
//				bill_type	3
//				bill_type_parent	1
//				call_type	
//				long_type	
//				month	201409
//				new_bill_type	101
//				roam_type	
//				status	0
				param.put("bill_type","3");
				param.put("bill_type_parent", "1");
				param.put("call_type", "");
				param.put("long_type", "");
				param.put("month",starDate);
				param.put("new_bill_type", "101");
				param.put("roam_type", "");
				param.put("status", "0");
				text = cutil.post("http://www.hi.10086.cn/service/bill/queryNewDetails.do",h,param);
				
				
					if(text!=null && text.contains("起始时间")){
						 Document doc = Jsoup.parse(text);
						 Elements trs1 =doc.select("tr[name=ttrr1],tr[name=ttrr]");
					     String day = "";
					     for(int i = 0; i < trs1.size(); i++) {
					    	 if(trs1.get(i).text().length()==10) {
					    		   day = trs1.get(i).text();
					    	   }
					    	 if(trs1.get(i).text().length()>20) {
					    		 Elements tds =  trs1.get(i).select("td");
							   {
//		                            <td>起始时间</span></td>
//		                            <td>通信地点</span></td>
//		                            <td>通信方式</span></td>
//		                            <td>对方号码</span></td>
//		                            <td>通信时长</span></td>
//		                            <td>通信类型</span></td>
//		                            <td>套餐优惠</span></td>
//		                            <td>实收通信费(元)</span></td>
									
									String cTime = day+" "+tds.get(0).text();
									String tradeAddr = tds.get(1).text();
									String tradeWay = tds.get(2).text();
									String recevierPhone = tds.get(3).text();
									String tradeTime1 = tds.get(4).text();//通话时间
									
									int tradeTime = TimeUtil.timetoint(tradeTime1);
									
									String tradeType = tds.get(5).text();
									String taocan = tds.get(6).text();
									String onlinePay1 = tds.get(7).text();
									BigDecimal onlinePay = new BigDecimal(onlinePay1);
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
									mobileDetail.setIscm(0);
									mobileDetailService.saveMobileDetail(mobileDetail);
							   }
							}
						}
					}
			}
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}
	
	//短信记录
	public Map<String,Object> getMessage(){
		try {
			CHeader  h = new CHeader("http://www.hi.10086.cn/service/bill/beforeQueryNewDetails.do");
			h.setAccept_Language("zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			MobileMessage mobilemessage = new MobileMessage();
			mobilemessage.setPhone(login.getLoginName());
			mobilemessage = mobileMessageService.getMaxSentTime(login.getLoginName());
			boolean b = true;
			String text=null;
			Map<String,String> param = new LinkedHashMap<String,String>();
			for (int k = 0 ;k<ms.size();k++) {
				String starDate = (String)ms.get(k) ;
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				String eday  = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				fday = fday.replace("-", "");
				eday = eday.replace("-", "");
				param = new LinkedHashMap<String,String>();
				
//				bill_type	3
//				bill_type_parent	1
//				call_type	
//				long_type	
//				month	201409
//				new_bill_type	102
//				roam_type	
//				status	0
				
				param.put("bill_type","3");
				param.put("bill_type_parent", "1");
				param.put("call_type", "");
				param.put("long_type", "");
				param.put("month",starDate);
				param.put("new_bill_type", "102");
				param.put("roam_type", "");
				param.put("status", "0");
				text = cutil.post("http://www.hi.10086.cn/service/bill/queryNewDetails.do",h,param);
				
				if(text!=null && text.contains("起始时间")){
					 Document doc = Jsoup.parse(text);
					 Elements trs1 =doc.select("tr[name=ttrr1],tr[name=ttrr]");
				     String day = "";
				     for(int i = 0; i < trs1.size(); i++) {
				    	 if(trs1.get(i).text().length()==10) {
				    		   day = trs1.get(i).text();
				    	   }
				    	 if(trs1.get(i).text().length()>20) {
				    		 Elements tds =  trs1.get(i).select("td");
						   {
//							<th>起始时间</th>
//							<th>通信地点</th>
//							<th>对方号码</th>
//							<th>通信方式</th>
//							<th>信息类型</th>
//							<th>业务名称</th>
//							<th>套餐优惠</th>
//							<th>通信费（元）</th>
							String sentTime = day+" "+tds.get(0).text();
							String sentAddr = tds.get(1).text();
							String recevierPhone = tds.get(2).text();
							String tradeWay = tds.get(3).text();
							String allPay1 = tds.get(7).text();
							BigDecimal allPay = new BigDecimal(allPay1);
							
							MobileMessage message1 = new MobileMessage();
							UUID uuid = UUID.randomUUID();
							message1.setId(uuid.toString());
							message1.setSentTime(DateUtils.StringToDate(sentTime, "yyyy-MM-dd HH:mm:ss"));
							
							if(mobilemessage!=null&&message1.getSentTime().getTime()<=mobilemessage.getSentTime().getTime()){
								b =false;
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
				}
			 }
		  } 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}
	
	//短信记录
		public Map<String,Object> getOnline(){
			try {
				CHeader  h = new CHeader("http://www.hi.10086.cn/service/bill/beforeQueryNewDetails.do");
				h.setAccept_Language("zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
				List<String> ms = DateUtils.getMonths(6,"yyyyMM");
				MobileOnlineList bean_List = null;
				MobileOnlineBill bean_Bill = null;
				try {
					bean_List = mobileOnlineListService.getMaxTime(login.getLoginName());
					bean_Bill = mobileOnlineBillService.getMaxTime(login.getLoginName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				boolean b = true;
				String text=null;
				Map<String,String> param = new LinkedHashMap<String,String>();
				for (int k = 0 ;k<ms.size();k++) {
					String starDate = (String)ms.get(k) ;
					String year = starDate.substring(0, 4);
					String mouth = starDate.substring(4, 6);
					String fday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
					String eday  = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
					fday = fday.replace("-", "");
					eday = eday.replace("-", "");
					param = new LinkedHashMap<String,String>();

					param.put("bill_type","3");
					param.put("bill_type_parent", "1");
					param.put("call_type", "");
					param.put("long_type", "");
					param.put("month",starDate);
					param.put("new_bill_type", "103");
					param.put("roam_type", "");
					param.put("status", "0");
					text = cutil.post("http://www.hi.10086.cn/service/bill/queryNewDetails.do",h,param);
					BigDecimal allFees = new BigDecimal(0);
					try {
						allFees = onlineList_parse(text,bean_List,starDate);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						onlineBill_parse(text,bean_Bill,starDate,allFees,k);
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
		//流量详单      onlineTime取不到
		public BigDecimal  onlineList_parse(String text,MobileOnlineList bean_List,String startDate){
			boolean b = true;
			BigDecimal allFees = new BigDecimal(0);//需要计算账单通信费  加法计算
			try {
				if(text.contains("上网详单")){
					Document doc6 = Jsoup.parse(text);
					Element table =doc6.select("table[id=tab1]").first();
							Elements trs = table.select("tr[name=ttrr],tr[name=ttrr1]");
							//System.out.println(trs.size());
							String  day = null;
							for(int j = 0 ; j<trs.size();j++){
							  if(trs.get(j).text().length()==10) { //获得日期
						    	 day = trs.get(j).text();
						    	}
							  if(trs.get(j).text().length()>20) {
								Elements tds = trs.get(j).select("td");

								Date cTime = null;
								String tradeAddr = null;
								String onlineType = null;
								long onlineTime = 0;
								long totalFlow = 0;
								String cheapService = null;
								BigDecimal communicationFees = new BigDecimal(0);//需要计算账单通信费  加法计算
								try {
									String cTime1 = day+" "+tds.get(0).text();
									cTime = DateUtils.StringToDate(cTime1, "yyyy-MM-dd HH:mm:ss");
									tradeAddr = tds.get(1).text();
									onlineType = tds.get(2).text();
									String onlineTime1 = tds.get(3).text();
									
//									onlineTime = StringUtil.flowTimeFormat(onlineTime1);
									String totalFlow1 = tds.get(4).text();
									totalFlow = new Long(totalFlow1)/1024;
									cheapService = tds.get(5).text();
									String communicationFees1 = tds.get(6).text();// 通信费
									communicationFees = new BigDecimal(communicationFees1);
									allFees = communicationFees.add(allFees) ;
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
							 try {
									MobileOnlineList onlineList = new MobileOnlineList();
									UUID uuid = UUID.randomUUID();
									onlineList.setId(uuid.toString());
									onlineList.setcTime(cTime);
									if(bean_List!=null){
										 if(bean_List.getcTime().getTime()<=onlineList.getcTime().getTime()){
											onlineList.setcTime(cTime);
											onlineList.setCheapService(cheapService);
											onlineList.setCommunicationFees(communicationFees);
//											onlineList.setOnlineTime(onlineTime);
											onlineList.setOnlineType(onlineType);
											onlineList.setTotalFlow(totalFlow);
											onlineList.setTradeAddr(tradeAddr);
											onlineList.setPhone(login.getLoginName());    	
											mobileOnlineListService.save(onlineList);
										 }
									 }else {
										 	onlineList.setcTime(cTime);
											onlineList.setCheapService(cheapService);
											onlineList.setCommunicationFees(communicationFees);
//											onlineList.setOnlineTime(onlineTime);
											onlineList.setOnlineType(onlineType);
											onlineList.setTotalFlow(totalFlow);
											onlineList.setTradeAddr(tradeAddr);
											onlineList.setPhone(login.getLoginName());    	
											mobileOnlineListService.save(onlineList);
									 }
									
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
			 }
			} catch (Exception e) {
				 errorMsg = e.getMessage();
				 b = false;
			}
			return allFees;
		}
		//解析流量账单
		public boolean  onlineBill_parse(String text,MobileOnlineBill bean_Bill,String startDate,BigDecimal allFees,int iscm1){
			boolean b = true;
			try {
				if(text.contains("上网详单")&&!text.contains("查询无记录")){
					Document doc6 = Jsoup.parse(text);
					Element table =doc6.select("table[id=tab1]").first();

								Date monthly = null;
								long totalFlow = 0;
								long freeFlow = 0;
								long chargeFlow = 0;
								BigDecimal communicationFees = new BigDecimal(0.0);
								int iscm = 0;
								try {
									String year = "";
									String mon = "";
							   		year = startDate.substring(0,4);
							   		mon = startDate.substring(4,6);
							   		startDate = year+"-"+mon;
									monthly = DateUtils.StringToDate(startDate,"yyyy-MM");
									String totalFlow1 = table.select("tr:eq(1)>td:eq(1)").text().replace("收费流量","").replace("(", "").replace(")", "").trim();
									totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
//									String freeFlow1 = table.select("tr:eq(1)>td:eq(2)").text().replace("免费流量","").trim();
//									freeFlow = Math.round(StringUtil.flowFormat(freeFlow1));
									String chargeFlow1 = table.select("tr:eq(1)>td:eq(1)").text().replace("收费流量","").replace("(", "").replace(")", "").trim();//收费流量1(M)389.77(K)
									chargeFlow = Math.round(StringUtil.flowFormat(chargeFlow1));
									communicationFees = allFees;
									if(iscm1==0) {
						        		iscm = 1;
						        	}
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
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
					        	onlineBill.setIscm(iscm);
								mobileOnlineBillService.save(onlineBill);
						   	
						}
			} catch (Exception e) {
				 errorMsg = e.getMessage();
				 b = false;
			}
			return b;
		}
		
		
	private boolean getMyInfo() {
		try {
			String loginName = login.getLoginName();
			CHeader h = new CHeader("http://www.hi.10086.cn/service/mainQuery.do");
			String text = cutil.get("http://www.hi.10086.cn/service/info/changeCustData_init.do",h);
			String realName = null;
			String idcard = null;
			String email = null;
			String addr = null;
			//System.out.println(text);
			if(text != null){
				Document doc = Jsoup.parse(text);
				Element element = doc.select("table").first();
				String realName1 = element.select("tbody").text();
				RegexPaserUtil rp = new RegexPaserUtil("真实姓名： ", "证件类型",realName1, RegexPaserUtil.TEXTEGEXANDNRT);
				 realName = rp.getText();
				rp = new RegexPaserUtil("证件号码： ", " 通信地址",realName1, RegexPaserUtil.TEXTEGEXANDNRT);
				 idcard = rp.getText();
				 email = element.select("input[name=EMAIL]").val();
				 addr = element.select("input[name=HOME_ADDRESS]").val();
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
				user.setIdcard(idcard);
				user.setAddr(addr);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setRegisterDate(DateUtils.StringToDate(date, "yyyy-MM-dd"));
				user.setPhoneRemain(getYuE());
				user.setEmail(email);
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());

				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(realName);
				user.setIdcard(idcard);
				user.setAddr(addr);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setRegisterDate(DateUtils.StringToDate(date, "yyyy-MM-dd"));
				user.setPhoneRemain(getYuE());
				user.setEmail(email);
				userService.saveUser(user);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HENYD_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

		return false;
	}

	private BigDecimal getYuE() {
		BigDecimal phoneremain = new BigDecimal("0.00");
		try {
			phoneremain = new BigDecimal(balance);
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
	private int getDaysOfMonth(String s){
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMM"); //如果写成年月日的形式的话，要写小d，如："yyyy/MM/dd"
		try {
		rightNow.setTime(simpleDate.parse(s)); //要计算你想要的月份，改变这里即可
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
			if (codeValue.contains("5001")) {
				return "尊敬的用户，您已经连续3次输入错误的服务密码,账号已被锁，24小时后自动解锁,或使用短信验证码进行登录。";
			}else if (codeValue.contains("authCount=1")) {
				return "尊敬的用户，您已经输入错误密码1次，当天累计输入3次错误密码，服务密码将会被锁！或使用短信验证码进行登录";
			}else if (codeValue.contains("authCount=2")) {
				return "尊敬的用户，您已经输入错误密码2次，当天累计输入3次错误密码，服务密码将会被锁！或使用短信验证码进行登录";
			}else if(codeValue.contains("2003")){
				return "尊敬的用户，请您输入正确的图形验证码后重试";
			}else {
				return null;
			}
			
		}
		return null;
	}



}
