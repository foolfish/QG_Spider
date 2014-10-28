package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

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
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;


public class NMGYidong extends BaseInfoMobile{
	private static String index_param = "nmgyidong_index_param";	
	private static String Accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	private static String login_referer = "https://nm.ac.10086.cn/login";
	private static String xiangdanReferer = "http://www.nm.10086.cn/my/account/index.xhtml";
	private static String msgReferer = "http://www.nm.10086.cn/my/detailQuery.xhtml";
	private static final Logger log = Logger.getLogger(NMGYidong.class);
	public String CUST_NAME = "";
	public NMGYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_nmg_yidong, currentUser);
	}
	public NMGYidong(Login login, IWarningService warningService,
			String currentUser) {
		super(login, warningService, ConstantNum.comm_nmg_yidong, currentUser);
	}
	public NMGYidong(Login login) {
		super(login);
	}

	public static void main(String[] args) {
		Login login = new Login("13754023610","809713");
		//Login login = new Login("13754023611","123123");
		NMGYidong nm = new NMGYidong(login);
		//初始化
		nm.index();
		//nm.inputCode(nm.getImgUrl());
		//登陆
		Map<String,Object> map = nm.login();
//		System.out.println("请输入手机口令：");
//		nm.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
//		nm.checkPhoneDynamicsCode();
		//nm.getMyInfo();
		nm.getTelDetailHtml();
		//nm.checkPhoneDynamicsCode();
		//nm.getYue();
		nm.close();
	}
	
	public void init() {
		if (!isInit()) {
			try {
				String text = cutil.get("https://nm.ac.10086.cn/login");
				Map<String, String> param1 = new HashMap<String, String>();
				param1.put("username", login.getLoginName());
				String text2 = cutil.post("https://nm.ac.10086.cn/VerifyCodeFlagServlet?username="+login.getLoginName(),param1);
				//System.out.println("111111111111"+text);
				Document doc = Jsoup.parse(text);
				Map<String, String> param = new HashMap<String, String>();
				param.put("ai_param_loginIndex",  "8"); 
				param.put("appId",doc.select("input[name=appId]").val());
				param.put("bigloginlabelselect1", ""); 
				param.put("loginType","1"); 
				param.put("loginWay", "loginpage"); 
				param.put("rememNum", "checkbox"); 
				param.put("rndPassword", ""); 
				param.put("service", ""); 
				param.put("userType", "1"); 
				if(text2.equals("1")){//判断是否有验证码
					setImgUrl("https://nm.ac.10086.cn/createVerifyImageServlet");
				}else{
					param.put("verifyCode","");
				}
				redismap.put(index_param, param);
				setInit();
			} catch (Exception e) {
				e.printStackTrace();
				

			}
		}
	}
  
	public   Map<String, Object>  login() {
		
		try {
			CHeader h = new CHeader();
			h.setReferer(login_referer);
			String url = "https://nm.ac.10086.cn/login";
			Map<String,String> param = (Map<String,String>) redismap.get(index_param);
			param.put("loginPass", login.getPassword()); 
			param.put("password",login.getPassword()); 
			param.put("username",login.getLoginName());
			param.put("verifyCode",login.getAuthcode()); 
			
			String text = cutil.post(url,h, param);
			if(text!=null && text.equals(("http://www.nm.10086.cn/my"))){
				 text = cutil.get(text);
				 text = cutil.get("http://www.nm.10086.cn/my/account/index.xhtml");
				 text = cutil.get("https://nm.ac.10086.cn/login?ai_param_loginIndex=9&appId=9&service=http://www.nm.10086.cn/sso/iLoginFrameCas.jsp");
				 Document doc6 = Jsoup.parse(text);
				 String ticket =doc6.select("input[name=ticket]").val();
				 String host =doc6.select("input[name=host]").val();
				 param = new LinkedHashMap<String,String>();
				 param.put("ticket", ticket);
				 param.put("host", host);
				 text = cutil.post("http://www.nm.10086.cn/sso/iLoginFrameCas.jsp",h,param);
				 if(text.contains("my/account/index.xhtml")){
					 //System.out.println("登陆成功");
					 loginsuccess();
					 sendPhoneDynamicsCode();
				 }
			}else if(text.contains("您输入的密码错误")){
				errorMsg =   "您输入的密码错误，请重新输入！可发送“MMCZ+空格+入网证件号码”或“MMCZ+#+入网证件号码”到10086进行密码重置。如果您不是实名制客户，请先到营业厅登记再进行密码重置。";
			}else if(text.contains("您输入的图形验证码错误")){
				errorMsg =  "您输入的图形验证码错误，请重新输入！";
			}
	
		} catch (Exception e) {
			writeLogByLogin(e);
		}
		return map;
	}
	
	
	
	public  void startSpider(){
		try{
			parseBegin(Constant.YIDONG);
				getYue();
				getYue();//余额
				getMyInfo(); //个人信息
				getTelDetailHtml();//账单记录
				getMessage();//短信
				getFlow1();//流量
				callHistory(); //历史通话
				
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}
	
	//个人信息
		public  Map<String,Object> getMyInfo()  {
			try{
				Map<String,String> map = new LinkedHashMap<String, String>();
				map.put("divId","main");
				
			String text = cutil.post("http://www.nm.10086.cn/busicenter/myinfo/MyInfoMenuAction/initBusi.menu?_menuId=1040101&_menuId=1040101",map);
			//System.out.println(text);
			if(text.contains("个人信息")){
			parseBegin(Constant.YIDONG);
			Document doc = Jsoup.parse(text);		

			Element element = doc.select("ul[class=myinfo2]").get(0);		
			String realName = element.select("ul>li:eq(0)").text().substring(5);//真实姓名
			String userName = login.getLoginName();//用户名
			String memberLevel = element.select("ul>li:eq(8)").text();//信用等级
			String phone = login.getLoginName();//电话
			String idcard = element.select("ul>li:eq(5)").text().replace("证件号码：", "");//IDcard
			String addr = element.select("ul>li:eq(2)").text();//地址
			String Date = element.select("ul>li:eq(16)").text().substring(5);//地址
			map = new HashMap<String, String>();
			map.put("parentId", currentUser);
			map.put("usersource", Constant.YIDONG);
			map.put("loginName", login.getLoginName());
			List<User> list = userService.getUserByParentIdSource(map);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword(login.getSpassword());
				user.setUsersource(Constant.YIDONG);
				user.setUsersource2(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setMemberLevel(memberLevel);
				user.setIdcard(idcard);
				user.setAddr(addr);
				user.setUserName(userName);
				user.setRealName(realName);
				user.setPhone(phone);
				user.setRegisterDate(DateUtils.StringToDate(Date, "yyyy-MM-dd"));
				user.setPhoneRemain(getYue());
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(login.getLoginName());
				user.setUsersource(Constant.YIDONG);
				user.setUsersource2(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setMemberLevel(memberLevel);
				user.setIdcard(idcard);
				user.setAddr(addr);
				user.setUserName(userName);
				user.setRealName(realName);
				user.setPhone(phone);
				user.setRegisterDate(DateUtils.StringToDate(Date, "yyyy-MM-dd"));
				user.setPhoneRemain(getYue());
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
	
	
	//余额
	public BigDecimal getYue(){
		BigDecimal phoneremain= new BigDecimal("0.00");
		Map<String, String>   param = new LinkedHashMap<String,String>();
		param.put("divId", "main");
		CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.nm.10086.cn",true);
		String text = cutil.post("http://www.nm.10086.cn/busicenter/fee/realtimefeenm/RealTimeFeeNmMenuAction/initBusi.menu?_menuId=1050105&_menuId=1050105",h,param);
		if(text!=null && text.contains("话费余额")){
			try {
					RegexPaserUtil rp = new RegexPaserUtil("话费余额：<strong><span style=\"color:red\">","元</span>",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		String yue = rp.getText();
					if(yue!=null){
						phoneremain = new BigDecimal(yue.replaceAll("\\s*", ""));
					}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return phoneremain;
	}
	// 查询话费记录
	public Map<String, Object> getTelDetailHtml() {
		try {
			//当月账单记录
			List<String> cms = DateUtils.getMonths(1,"yyyyMM");
			String cmonthDate = cms.get(0);
			Date t1 =  DateUtils.StringToDate(cmonthDate, "yyyyMM");
			
			Map<String,String> 	cparam = new LinkedHashMap<String,String>();
			cparam.put("divId","myinfo");
			String cmonth = cutil.post("http://www.nm.10086.cn/busicenter/myinfo/MyInfoMenuAction/initBusi.menu?_menuId=10401",cparam);
			if(cmonth.contains("实时话费")) {
			Document doc1 = Jsoup.parse(cmonth);
	        Elements body = doc1.select("body");
	        cmonth = body.text();
	        RegexPaserUtil rp1 = new RegexPaserUtil("实时话费：","元 139邮箱", cmonth, RegexPaserUtil.TEXTEGEXANDNRT);
			//总费用
			String cmonthAllPayStr = rp1.getText();
			BigDecimal cmonthAllPay = new BigDecimal(cmonthAllPayStr);
			try {
				Map<String,Object> cmap = new LinkedHashMap<String, Object>();
				cmap.put("phone", login.getLoginName());
				cmap.put("cTime", t1);
				List<Map> clist = mobileTelService.getMobileTelBybc(cmap);
				if(clist==null || clist.size()==0){
				MobileTel cmobieTel = new MobileTel();
				UUID cuuid = UUID.randomUUID();
				cmobieTel.setId(cuuid.toString());
				cmobieTel.setcTime(t1);
				cmobieTel.setTeleno(login.getLoginName());
				cmobieTel.setcAllPay(cmonthAllPay);
				cmobieTel.setIscm(1);
				mobileTelService.saveMobileTel(cmobieTel);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			
			//其他月账单
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.nm.10086.cn",true);
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			Map<String,String> 	param = new LinkedHashMap<String,String>();
			param.put("divId", "main");
			String text = cutil.post("http://www.nm.10086.cn/busicenter/fee/monthbillnm/MonthBillNmMenuAction/initBusi.menu?_menuId=1050106&_menuId=1050106",h,param);
			param = new LinkedHashMap<String,String>();
			 for (int k = 1 ;k < ms.size();k++) {
				    String starDate = (String)ms.get(k) ;
					h.setRespCharset("GBK");
					param = new LinkedHashMap<String,String>();
					param.put("BillMonth", starDate);
//					if(k!=1){
//						String StrNum = Integer.toString(k);
//						param.put("StrNum",StrNum);
//					}
					param.put("_menuId","1050106");
					param.put("divId", "main");
					
					text = cutil.post("http://www.nm.10086.cn/busicenter/fee/monthbillnm/MonthBillNmMenuAction/queryBillInfo.menu",h,param);
//					http://www.nm.10086.cn/service/fee/monthbillNm/billInfoFrame.jsp?test=1&_menuId=1050106&queryMonth=201409
//					_menuId	1050106
//					queryMonth	201409
//					test	1
					if(!text.contains("对不起，您的该月账单没有生成")){
					text = cutil.get("http://www.nm.10086.cn/service/fee/monthbillNm/billInfoFrame.jsp?test=1&_menuId=1050106&queryMonth="+starDate);
					//System.out.println(text);
					
					try {
						if(text!=null){
							Document doc = Jsoup.parse(text);
							Element element01 = doc.select("ul[class=feesalllist]").first();
							//总费用
							String cAllPay = element01.select("li:eq(11)").text().replace("账户当前用户费用", "").trim();
							//套餐及固定费
							String tcgdf = element01.select("li:eq(1)").text().replace("套餐及固定费用", "").trim();
							//套餐外短彩信费
							String tcwdxf = element01.select("li:eq(5)").text().replace("短彩信费", "").trim();
							//套餐外语音通信费
							String tcwyytxf  = element01.select("li:eq(3)").text().replace("语音通信费", "").trim();
							
							SimpleDateFormat tempDate = new SimpleDateFormat("yyyy.MM");
							Date t2 =  DateUtils.StringToDate(starDate, "yyyyMM");
							Map<String,Object> map = new LinkedHashMap<String, Object>();
							map.put("phone", login.getLoginName());
							map.put("cTime", t2);
							List<Map> list = mobileTelService.getMobileTelBybc(map);
							if(list==null || list.size()==0){
								MobileTel mobieTel = new MobileTel();
								UUID uuid = UUID.randomUUID();
								mobieTel.setId(uuid.toString());
								mobieTel.setcTime(t2);
								mobieTel.setTeleno(login.getLoginName());
								
								try {
									if (cAllPay != null) {
										mobieTel.setcAllPay(new BigDecimal(cAllPay));
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									if (tcgdf != null) {
										mobieTel.setTcgdf(new BigDecimal(tcgdf));
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
									if (tcwyytxf != null) {
										mobieTel.setTcwyytxf(new BigDecimal(tcwyytxf));
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
									mobieTel.setIscm(0); //这六个月 都不是当月
								
								mobileTelService.saveMobileTel(mobieTel);
								}
							   }
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						 }
					   }//if(text)
					}//for 5个月
						
						} catch (Exception e) {
							e.printStackTrace();
					}
					return map;
				}
	

	 //查询通话记录
	public Map<String,Object> callHistory(){
		try {
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.nm.10086.cn",true);
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			MobileDetail detail = new MobileDetail();
			detail.setPhone(login.getLoginName());
			detail = mobileDetailService.getMaxTime(detail);
			boolean b = true;
			String text=null;
			Map<String,String> param = new LinkedHashMap<String,String>();
			for (int k = 0 ;k<ms.size();k++) {
				String starDate = (String)ms.get(k) ;
				h.setRespCharset("GBK");
				String year = starDate.substring(0, 4);
				String month = starDate.substring(4, 6);
				String fday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(month)));
				String eday  = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(month)));
				fday = fday.replace("-", "");
				eday = eday.replace("-", "");
				param.put("divId","main");
				text = cutil.post("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/initBusi.menu?_menuId=1050103&_menuId=1050103",h,param);
				param = new LinkedHashMap<String,String>();
				param.put("beginDate","");
				param.put("detailType", "202");
				param.put("endDate", "");
				param.put("select", starDate);
				text = cutil.post("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/indexShowDetailHead.menu?_menuId=1050103&detailType=202&select="+starDate+"&beginDate=&endDate=",h,param);
				text = cutil.get("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/queryResult.menu?_menuId=1050103&select="+starDate+"&beginDate="+fday+"&endDate="+eday+"&detailType=202&_=1410750708746",h);
					if(text!=null && text.contains("起始时间")&& text.contains("通信地点")){
						
						Document doc6 = Jsoup.parse(text);
						Elements tables =doc6.select("table");
						for (int i = 0; i < tables.size(); i++) {
							Element table = tables.get(i);
							String tableString = table.text();
							if(tableString.contains("起始时间")){
								Elements trs =  table.select("tr");
								String fixdate="";//记录日期
								for(int x = 1 ; x<trs.size();x++){
									Elements ths = trs.get(x).select("th");
									Elements tds = trs.get(x).select("td");
									if(ths.size()==1){
										fixdate=ths.get(0).text();
									}else if(tds.size()==8){
//										<th>起始时间</th>
//										<th>通信地点</th>
//										<th>通信方式</th>
//										<th>对方号码</th>
//										<th>通信时长</th>
//										<th>通信类型</th>
//										<th>套餐优惠</th>
//										<th>实收通信费（元）</th>
										String cTime = fixdate+" "+tds.get(0).text();
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
			}
		
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}
	
	

	 //查询短信记录
	public Map<String,Object> getMessage(){
		try {
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.nm.10086.cn",true);
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");

			MobileMessage mobilemessage = new MobileMessage();
			mobilemessage.setPhone(login.getLoginName());
			mobilemessage = mobileMessageService.getMaxSentTime(login.getLoginName());
			boolean b = true;
			String text=null;
			Map<String,String> param = new LinkedHashMap<String,String>();
			for (int k = 0 ;k<ms.size();k++) {
				String starDate = (String)ms.get(k) ;
				h.setRespCharset("GBK");
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				String eday  = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				fday = fday.replace("-", "");
				eday = eday.replace("-", "");
				param.put("divId","main");
				text = cutil.post("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/initBusi.menu?_menuId=1050103&_menuId=1050103",h,param);
				param = new LinkedHashMap<String,String>();
				param.put("beginDate","");
				param.put("detailType", "204");
				param.put("endDate", "");
				param.put("select", starDate);
				text = cutil.post("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/indexShowDetailHead.menu?_menuId=1050103&detailType=204&select="+starDate+"&beginDate=&endDate=",h,param);
				text = cutil.get("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/queryResult.menu?_menuId=1050103&select="+starDate+"&beginDate="+fday+"&endDate="+eday+"&detailType=204&_=1410751131164",h);
					if(text!=null && text.contains("彩信详单")){
						
						Document doc6 = Jsoup.parse(text);
						Elements tables =doc6.select("table");
						for (int i = 0; i < tables.size(); i++) {
							Element table = tables.get(i);
							String tableString = table.text();
							if(tableString.contains("起始时间")){
								Elements trs =  table.select("tr");
								String fixdate="";
								for(int x = 1 ; x<trs.size();x++){
									Elements ths = trs.get(x).select("th");
									Elements tds = trs.get(x).select("td");
									if(ths.size()==1){
										fixdate=ths.get(0).text();
									}else if(tds.size()==8){

//										<th>起始时间</th>
//										<th>通信地点</th>
//										<th>对方号码</th>
//										<th>通信方式</th>
//										<th id='infoTypeThId'>信息类型</th>
//										<th>业务名称</th>
//										<th>套餐优惠</th>
//										<th>通信费（元）</th>
										
										String sentTime =fixdate+" "+tds.get(0).text();//加上日期
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
			}
		
	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}

	public void getFlow1() {
		MobileOnlineList bean_List = null;
		 if(mobileOnlineListService .getMaxTime(login.getLoginName())!= null) {
		bean_List = mobileOnlineListService.getMaxTime(login .getLoginName());
		                     }
		MobileOnlineBill bean_Bill = null;
		if(mobileOnlineBillService .getMaxTime(login.getLoginName())!= null) {
		 bean_Bill = mobileOnlineBillService.getMaxTime(login .getLoginName());
		                     }
		getFlow(bean_List,bean_Bill);
	}
	
	public void saveFlowBill(String text,MobileOnlineBill bean_Bill,String starDate,int iscm1) {
		
		if(text!=null && text.contains("上网详单")){
//		取不到的数据			
//		免费数据流量
//		private Long freeFlow;
//		收费数据流量
//		private Long chargeFlow;
	
			Date monthly = null;
			long totalFlow_b = 0;
			BigDecimal trafficCharges = new BigDecimal(0);
			int iscm = 0;
			String year = starDate.substring(0, 4);
			String mouth = starDate.substring(4, 6);
			try {
				RegexPaserUtil folw = new RegexPaserUtil( "总流量", "<" ,text, RegexPaserUtil.TEXTEGEXANDNRT);
				monthly = DateUtils.StringToDate(year+"-"+mouth,"yyyy-MM");
				totalFlow_b = Math.round(StringUtil.flowFormat(folw.getText()));
				RegexPaserUtil rp1 = new RegexPaserUtil( "netFeeSumId'>", "元</strong>" ,text, RegexPaserUtil.TEXTEGEXANDNRT);
				String trafficChargesStr = rp1.getText();
				trafficCharges = new BigDecimal(trafficChargesStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * 流量账单保存
			 */
				MobileOnlineBill onlineBill = new MobileOnlineBill();
				UUID uuid = UUID.randomUUID();
				onlineBill.setId(uuid.toString());
				onlineBill.setMonthly(monthly);
				if(bean_Bill!=null){
					 if(bean_Bill.getMonthly().getTime()>=onlineBill.getMonthly().getTime()){
					 }else {
						onlineBill.setTotalFlow(totalFlow_b);
						onlineBill.setTrafficCharges(trafficCharges);
						onlineBill.setPhone(login.getLoginName());   
						if(iscm1==0) {
							iscm = 1;
						}
						onlineBill.setIscm(iscm);
						mobileOnlineBillService.save(onlineBill);
					 }
				 }else {
					onlineBill.setMonthly(monthly);
					onlineBill.setTotalFlow(totalFlow_b);
					onlineBill.setTrafficCharges(trafficCharges);
					onlineBill.setPhone(login.getLoginName());  
					if(iscm1==0) {
						iscm = 1;
					}
					onlineBill.setIscm(iscm);
					mobileOnlineBillService.save(onlineBill);
				 }
		 }
	}
	
	public void getFlow(MobileOnlineList bean_List,MobileOnlineBill bean_Bill)
	{
		try {
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.nm.10086.cn",true);
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			String text=null;
			Map<String,String> param = new LinkedHashMap<String,String>();
			for (int k = 0 ;k<ms.size();k++) {
				String starDate = (String)ms.get(k) ;
				h.setRespCharset("GBK");
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				String eday  = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				fday = fday.replace("-", "");
				eday = eday.replace("-", "");
				param.put("divId","main");
				//http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/indexShowDetailHead.menu?_menuId=1050103&detailType=203&select=201409&beginDate=&endDate=
				text = cutil.post("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/initBusi.menu?_menuId=1050103&_menuId=1050103",h,param);
				param = new LinkedHashMap<String,String>();
				param.put("beginDate","");
				param.put("detailType", "203");
				param.put("endDate", "");
				param.put("select", starDate);
				//http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/queryResult.menu?_menuId=1050103&select=201409&beginDate=20140901&endDate=20140930&detailType=203&_=1413250721725
				text = cutil.post("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/indexShowDetailHead.menu?_menuId=1050103&detailType=203&select="+starDate+"&beginDate=&endDate=",h,param);
				text = cutil.get("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/queryResult.menu?_menuId=1050103&select="+starDate+"&beginDate="+fday+"&endDate="+eday+"&detailType=203&_=1413250721725",h);
				List<MobileOnlineList> mobileOnlineList = new LinkedList<MobileOnlineList>();
				
				try {
					//保存流量账单
					saveFlowBill(text,bean_Bill,starDate,k);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					log.error("内蒙古流量账单", e1);
				}
				
				if(text!=null && text.contains("上网详单")){	
						Document doc6 = Jsoup.parse(text);
						Elements tables =doc6.select("table[class=w950_detailtable]");
						for (int i = 0; i < tables.size(); i++) {
							Element table = tables.get(i);
							String tableString = table.text();
							if(tableString.contains("起始时间")&&tableString.contains("上网方式")){
								Elements trs =  table.select("tr");
								String fixdate="";
								for(int x = 1 ; x<trs.size();x++){
									Elements ths = trs.get(x).select("th");
									Elements tds = trs.get(x).select("td");
									if(ths.size()==1){
										fixdate=ths.get(0).text();
									}else if(tds.size()==7){			
										String starttime =fixdate+" "+tds.get(0).text();//加上日期
										String allTotalFee = tds.get(6).text().trim();
										String area = tds.get(1).text().trim();
										String duration = tds.get(3).text().trim();
										String accessPoint = tds.get(2).text().trim();
										String data = tds.get(4).text().trim();
										String cheapService = tds.get(5).text().trim();
										
										if(duration.contains("时"))
										{
											String[] str;
											if(duration.contains("小时")) str = duration.split("小时");
											str = duration.split("时");
											int hh = 0;
											int m = 0;
											int s = 0;
											hh = Integer.parseInt(str[0])*3600;
											if(str[1]!=null&&str[1].contains("分"))
											{
												String[] str2 = str[1].split("分");					
												m = Integer.parseInt(str[0])*60;
												if(str2[1]!=null&&str2[1].contains("秒"))
												{
													String str3 = str2[1].replace("秒", "");
													s = Integer.parseInt(str3);
												}
												
											}
											else if(str[1]!=null&&str[1].contains("秒"))
											{
												s = Integer.parseInt(str[1].replace("秒", ""));
											}
											
											duration = ""+(hh+m+s);
										}
										else if(duration.contains("分"))
										{
											String[] str = duration.split("分");
											int m = Integer.parseInt(str[0])*60;
											int s=0;
											if(str[1]!=null&&str[1].contains("秒"))
											{
												s = Integer.parseInt(str[1].replace("秒", ""));
											}
											
											duration = ""+(m+s);
										}
										else
										{
											duration = duration.replace("秒", "");
										}
										
										if(data.contains("MB"))
										{
										   String[] bt = data.split(" ");
										   String mb = bt[0];
										   String kb = bt[1];
										   try{
											   mb = mb.replace("MB", "").trim();
											   kb = kb.replace("KB", "").trim();
											   
											   int all = Integer.parseInt(mb)*1024+ Integer.parseInt(kb);
											   data = all+"";
											   
										   }
										   catch(Exception e)
										   {
											   e.printStackTrace();
										   }
										}
										else
										{
											data = data.replace("KB", "");
										}
										
										long onlinetime=0;
										BigDecimal communicationFees=new BigDecimal("0.0");;
										long totalFlow=0;
										Date times=null;
										
										try{
											times = DateUtils.StringToDate(starttime,
													"yyyy-MM-dd HH:mm:ss");
											if(!duration.equals(""))
											{
												onlinetime = Long.parseLong(duration.trim());
											}
											if(!allTotalFee.equals(""))
											{
												communicationFees = new BigDecimal(allTotalFee.trim());
											}
											if(!data.equals(""))
											{
												totalFlow = Long.parseLong(data);
											}
										}
										catch (Exception e) {
											e.printStackTrace();
										}	
										MobileOnlineList datalist = new MobileOnlineList();						
										UUID uuid = UUID.randomUUID();
										datalist.setId(uuid.toString());
										datalist.setcTime(times);
										if(bean_List!=null){
											 if(bean_List.getcTime().getTime()<datalist.getcTime().getTime()){
												datalist.setOnlineTime(onlinetime);
												datalist.setPhone(login.getLoginName());
												datalist.setOnlineType(accessPoint);
												datalist.setTotalFlow(totalFlow);
												datalist.setTradeAddr(area);
												datalist.setCommunicationFees(communicationFees);
												datalist.setCheapService(cheapService);   	
												mobileOnlineListService.save(datalist);
											 }
										 }else {
											datalist.setOnlineTime(onlinetime);
											datalist.setPhone(login.getLoginName());
											datalist.setOnlineType(accessPoint);
											datalist.setTotalFlow(totalFlow);
											datalist.setTradeAddr(area);
											datalist.setCommunicationFees(communicationFees);
											datalist.setCheapService(cheapService);   	
											mobileOnlineListService.save(datalist);
										 }
										
										}
									}
									
								}
							}//for(table.size)
						}//if(text)	
			     }//for()6个月
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
    //	  生成短信
	public Map<String,Object> sendPhoneDynamicsCode() {
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		int status = 0;
		try {
				String text1 = cutil.get("http://www.nm.10086.cn/my/detailQuery.xhtml");
			    CHeader h = new CHeader(CHeaderUtil.Accept_other,"http://www.nm.10086.cn/my/detailQuery.xhtml",CHeaderUtil.Content_Type__urlencoded,"www.nm.10086.cn",true);
			    Map<String,String> param = new LinkedHashMap<String,String>();
				param.put("divId", "main");
			    String text = cutil.post("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/initBusi.menu?_menuId=1050103&_menuId=1050103",h,param);
				try {																														      
					if(text.contains("当日使用随机短信次数过多")){
						errorMsg = "当日使用随机短信次数过多";
					}else{
						status = 1;
						errorMsg = "发送成功";
					}
				} catch (Exception e) {
					errorMsg = "发送失败!";
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
	
	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
		try {
			    //System.out.println(getCookiesString());
			    CHeader h = new CHeader("http://www.nm.10086.cn/my/detailQuery.xhtml");
			    h.setAccept("application/json, text/javascript, */*; q=0.01");
				Map<String,String> param = new LinkedHashMap<String,String>();
				param.put("_menuId", "1050103");
				param.put("commonSmsPwd", login.getPhoneCode());
				param.put("operVerifyCode1","");
			    String text = cutil.post("http://www.nm.10086.cn/busicenter/detailquery/DetailQueryMenuAction/checkSmsPassWd.menu",h,param);
			    System.out.println(text);
			    if(text!=null){
			       //正确返回//{"forwardUrl":"/busicenter/detailquery/DetailQueryMenuAction/initBusi.menu?_menuId=1050103&_menuId=1050103","type":"2","errorMsg":"短信随机码正确","errorCode":"60028","checkResult":"success"}
			       //{"type":"2","errorMsg":"随机短信密码错误，请重新输入！","errorCode":"20007","checkResult":"error"}
			  	  if(text.contains("随机短信密码错误")){
					errorMsg = "随机短信密码错误，请重新输入";
				  }else if(text.contains("密码已失效")){
					errorMsg ="您的二次鉴权密码已失效，请重新发送";
				  }else if(text.contains("由于您打开的页面已经超时")) {
					  errorMsg = "尊敬的客户，您好！由于您打开的页面已经超时，请重新打开本页面进行业务办理。";
				  }else if(text.contains("短信随机码正确")){
					  status = 1;
				      addTask(this);
				  }
			    }
		} catch (Exception e) {

			e.printStackTrace();
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
	
	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public String formatDate(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
		return sdf.format(d);  
	}
	public static String formatDateMouth(String m ){
		if(m!=null&&m.length()==2){
			String fix1 = m.substring(0, 1);
			String fix2 = m.substring(1, 2);
			if(fix1.equals("0")){
				return fix2;
			}
			return m;
		}
		return null;
	}
}
