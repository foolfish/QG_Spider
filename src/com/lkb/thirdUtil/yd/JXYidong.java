package com.lkb.thirdUtil.yd;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Logger;
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
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

import freemarker.template.utility.DateUtil;

public class JXYidong extends BaseInfoMobile {

	private static Logger logger = Logger.getLogger(JXYidong.class);
	
	
	public String index = "https://jx.ac.10086.cn/login";

	// 验证码图片路径
	public static String imgurl = "https://jx.ac.10086.cn/common/image.jsp?l=";
	
	
	public JXYidong(Login login,String currentUser) {
		super(login,ConstantNum.comm_jx_yidong,currentUser);
	}
	
	
	
	
	public Map<String,Object> getSecImgUrl(){
		setImgUrl(imgurl);//更换验证码地址二次验证
		map.put("url", getAuthcode());
		map.put(CommonConstant.status, 1);
		return map;
	}
	
//	public Map<String,Object> index(){
//		map.put("url",getAuthcode());
//		return map;
//	}
	public void init(){
		if(!isInit()){
			String text = cutil.get(index);
			if(text!=null && text.contains("spid")){
				 setImgUrl(imgurl);
				 Document doc = Jsoup.parse(text);
				 String spid = doc.select("input[name=spid]").first().val();
				 if(spid!=null){
					 map.put("spid",spid);
					 redismap.put("jsmap", map);//根据实际需要存放
					 setInit();
				 }	 
			}
		}
	}
	
	



	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
			try {
				Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
				CHeader h= new CHeader(CHeaderUtil.Accept_,null,null,"www.jx.10086.cn",true);
				String myinfo = cutil.get("http://www.jx.10086.cn/my/queryXX.do",h);
				if(myinfo!=null){
					Document doc3 = Jsoup.parse(myinfo);
					Elements tables3 = doc3.select("table");
					Elements table3 = tables3.get(0).select("tr");
					Elements tds0 = table3.get(0).select("td");
					String username = tds0.get(1).text();
					String idcard = tds0.get(3).text();
					Elements tds1 = table3.get(1).select("td");
					// String username= tds1.get(1).text();
					String addr = tds1.get(3).text();
					Elements tds2 = table3.get(2).select("td");
					String telhome = tds2.get(1).text();
					// String teloffice= tds2.get(3).text();
					Elements tds3 = table3.get(3).select("td");
					 String openDate= tds3.get(1).text();
					// String zffa= tds3.get(3).text();
					Elements tds4 = table3.get(4).select("td");
					//String rwsj = tds4.get(0).text();
					String pplx = tds4.get(1).text();
					// String xyd= tds3.get(3).text();
					Map<String, String> map = new HashMap<String, String>(3);
					map.put("parentId", currentUser);
					map.put("loginName", telhome);
					map.put("usersource", Constant.YIDONG);
					map.put("loginName", login.getLoginName());
					List<User> list = userService.getUserByParentIdSource(map);
					if (list != null && list.size() > 0) {
						User user = list.get(0);

						user.setLoginName(login.getLoginName());
						user.setLoginPassword("");

						user.setUserName(username);
						user.setRegisterDate(DateUtils.StringToDate(openDate,"yyyyMMdd"));
						user.setRealName(username);
						user.setIdcard(idcard);
						user.setAddr(addr);
						user.setUsersource(Constant.YIDONG);
						user.setUsersource2(Constant.YIDONG);
						user.setParentId(currentUser);
						user.setModifyDate(new Date());
						user.setPhone(telhome);
						user.setPackageName(pplx);
						user.setPhoneRemain(new BigDecimal(jsmap.get("yue").toString()));
						userService.update(user);
					} else {

						User user = new User();
						UUID uuid = UUID.randomUUID();
						user.setId(uuid.toString());

						user.setLoginName(login.getLoginName());
						user.setLoginPassword("");
						user.setRegisterDate(DateUtils.StringToDate(openDate,"yyyyMMdd"));

						user.setUserName(username);

						user.setRealName(username);
						user.setIdcard(idcard);
						user.setAddr(addr);
						user.setUsersource(Constant.YIDONG);
						user.setUsersource2(Constant.YIDONG);
						user.setModifyDate(new Date());
						user.setParentId(currentUser);
						user.setPhone(telhome);
						user.setPackageName(pplx);
						user.setPhoneRemain(new BigDecimal(jsmap.get("yue").toString()));
						userService.saveUser(user);

					}
				}
			} catch (Exception e) {
				errorMsg=e.getMessage();
				sendWarningCallHistory(errorMsg);
			}finally{
				parseEnd(Constant.YIDONG);
			}
	}
	
	
	private void getThisMonthTel(){
		try {
			int update=0;
			MobileTel newBill=new MobileTel();
			CHeader h= new CHeader(CHeaderUtil.Accept_,null,null,"service.jx.10086.cn",true);
			String text =cutil.get("http://service.jx.10086.cn/service/queryCurrentMonthBill.action?menuid=00890103&pageid=1133603423&requestStartTime=2014-10-23%2008:58:57.654", h);
		
			text = text.replaceAll("\\s*", "");
			if(text!=null && text.contains("套餐及固定费")){
				Map map0=new HashMap();
				newBill.setTeleno(login.getLoginName());
				List<String> d=DateUtils.getMonths(1, "yyyy-MM");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				newBill.setcTime(sdf.parse(d.get(0)+"-01 00:00:00"));
				
				map0.put("cTime", newBill.getcTime());
				map0.put("phone",login.getLoginName());
				List list=mobileTelService.getMobileTelBybc(map0);
				if(list==null || list.size()==0){
					newBill.setId(UUID.randomUUID().toString());
				}else{
					update=1;
					MobileTel oldBill=(MobileTel) list.get(0);
					newBill.setId(oldBill.getId());
				}
				RegexPaserUtil rp = new RegexPaserUtil("套餐及固定费</div></td><td><divalign=\"center\">", "</div>", text,RegexPaserUtil.TEXTEGEXANDNRT);
				String tcgdfs = rp.getText();
				if (tcgdfs != null) {
					newBill.setTcgdf(new BigDecimal(tcgdfs));
				}
				rp = new RegexPaserUtil("套餐外短(彩)信费</div></td><td><divalign=\"center\">", "</div>", text,RegexPaserUtil.TEXTEGEXANDNRT);
				String tcwdxfs = rp.getText();
				if (tcwdxfs != null) {
					newBill.setTcwdxf(new BigDecimal(tcwdxfs));
				}
				rp = new RegexPaserUtil("套餐外语音通信费</div></td><td><divalign=\"center\">", "</div>", text,RegexPaserUtil.TEXTEGEXANDNRT);
				String tcwyydxfs = rp.getText();
				if (tcwyydxfs != null) {
					newBill.setTcwyytxf(new BigDecimal(tcwyydxfs));
				}
				rp = new RegexPaserUtil("应收总金额</div></td><td><divalign=\"center\">", "</div>", text,RegexPaserUtil.TEXTEGEXANDNRT);
				String hjs = rp.getText();
				if (hjs != null) {
					newBill.setcAllPay(new BigDecimal(hjs));
				}
				
			
				newBill.setDependCycle(d.get(0)+"-01至"+DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
				if(update==0){
					mobileTelService.saveMobileTel(newBill);
				}else{
					mobileTelService.update(newBill);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("FJYidong this month tel error:",e);
			e.printStackTrace();
		}
	}
	
	
	public void getTelDetailHtml() {
	
		try {
			
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonthsNotInclude(5,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"",null,"service.jx.10086.cn",true);
			parseBegin( Constant.YIDONG);
			getThisMonthTel();
			for (String startDate : ms) {
				
				//String year = startDate.substring(0, 4);
				//String mouth = startDate.substring(4, 6);
			//	String sday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)),"yyyyMMdd");
			//	String eday = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)),"yyyyMMdd");
				String text =cutil.get("http://service.jx.10086.cn/service/queryWebPageInfo.action?requestStartTime=2014-09-26%2016:16:48.564&menuid=00890104&queryMonth="+startDate+"&s=0.9565388613438328", h);
			//	System.out.println(text);
				b=getTelDetailHtml_parse(text, startDate);
				
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
		
	
		
	}
	

	
	/**
	 * 查询话费记录
	 */
	public boolean getTelDetailHtml_parse(String text,String startDate) {
		boolean b = true;
		try {
			if(text.contains("套餐及固定费用")){
				Document doc6 = Jsoup.parse(text);
				Elements tables = doc6.select("table");
				if(tables.size()>0){
					//Elements table1 = tables.get(0).select("tr");
					//String dependCycle = table1.get(1).select("td").get(1).text();
					BigDecimal swf = new BigDecimal(0);
					BigDecimal yydxf = new BigDecimal(0);
					BigDecimal tcgdf = new BigDecimal(0);
					BigDecimal total = new BigDecimal(0);
					text=text.replaceAll("\\s*", "");
					
					RegexPaserUtil rp = new RegexPaserUtil("<strong>套餐及固定费用</strong></div><divstyle=\"float:none;padding-left:255px;\">￥","</div>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String tcgdfs = rp.getText();
					if(tcgdfs!=null){
						tcgdf=new BigDecimal(tcgdfs);
					}
					rp = new RegexPaserUtil("语音通信费</li></div><divstyle=\"float:none;padding-left:255px;\">￥","</div>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String yytxfs = rp.getText();
					if(yytxfs!=null){
						yydxf=new BigDecimal(yytxfs);
					}	
					rp = new RegexPaserUtil("上网费</li></div><divstyle=\"float:none;padding-left:255px;\">￥","</div>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String swfs = rp.getText();
					if(swfs!=null){
						swf=new BigDecimal(swfs);
					}
					rp = new RegexPaserUtil("合计</strong></div><divstyle=\"float:none;padding-left:255px;\">￥","</div>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String hjs = rp.getText();
					if(hjs!=null){
						total=new BigDecimal(hjs);
					}
					 Map map2 = new HashMap();
					 map2.put("phone", login.getLoginName());
					 map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
					 List list = mobileTelService.getMobileTelBybc(map2);
					 if(list==null || list.size()==0){
						 	MobileTel mobieTel = new MobileTel();
							UUID uuid = UUID.randomUUID();
							mobieTel.setId(uuid.toString());
							mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
							mobieTel.setTeleno(login.getLoginName());
							mobieTel.setTcgdf(tcgdf);
							mobieTel.setTcwyytxf(yydxf);
							String year = startDate.substring(0, 4);
							String mouth = startDate.substring(4, 6);
							mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
							mobieTel.setcAllPay(total);
							mobileTelService.saveMobileTel(mobieTel);
					 }else{
						 MobileTel mobieTel = (MobileTel)list.get(0);
						 if(mobieTel.getcAllPay().compareTo(total)!=0){
							 	mobieTel.setTcgdf(tcgdf);
								mobieTel.setTcwyytxf(yydxf);
								String year = startDate.substring(0, 4);
								String mouth = startDate.substring(4, 6);
								mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
								mobieTel.setcAllPay(total);
								mobileTelService.update(mobieTel);
						 }
					 }
				}
			}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
	
		return b;
	}
	
	/**文本解析
	 * true正常解析
	 * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
	 * created by qian
	 */
	public boolean  message_parse(String text,MobileMessage bean,String startDate){
		boolean b = true;
		try {
			 Document doc  = Jsoup.parse(text);
			 Elements tables = doc.select("table");
			 if(tables.size()>1){
				 Elements table7 = tables.get(0).select("tr");
				 for(int i = 1;i<table7.size();i++){ 
					 	Elements tds = table7.get(i).select("td");
					 	String tem="";
					 	// 起始时间
					 	String qssj = tds.get(0).text();
					 	// 通信地点
					 	String txdd = tds.get(1).text();
					 	// 对方号码
					 	String dfhm = tds.get(2).text();
					 	// 通信方式
					 	String txfs = tds.get(4).text();
					 	// 通信类型
						String txlx = tds.get(6).text();
						// 实收通信费(元)
						String sjtxf = tds.get(7).text();
						
						MobileMessage mMessage = new MobileMessage();
						UUID uuid = UUID.randomUUID();
						mMessage.setId(uuid.toString());
						mMessage.setAllPay(new BigDecimal(0.0));
						mMessage.setCreateTs(new Date());
						mMessage.setRecevierPhone(dfhm);
						mMessage.setPhone(login.getLoginName());
						mMessage.setSentAddr(txdd);
						mMessage.setSentTime(DateUtils.StringToDate(startDate.substring(0, 4)+"-"+qssj, "yyyy-MM-dd HH:mm:ss"));
						if(txlx.contains("接收"))
							mMessage.setTradeWay("接收");
						else
							mMessage.setTradeWay("发送");
						//mMessage.setTradeWay(txlx);
						
						if(bean != null && bean.getSentTime() != null){
							if(bean.getSentTime().getTime() >= mMessage.getSentTime().getTime()){
								break;
							}
						}
					
						mobileMessageService.save(mMessage);
				 }
			}
			
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	

	/**文本解析
	 * true正常解析
	 * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
	 * type 市话/长途/港澳台/漫游
	 */
	public boolean  callHistory_parse(String text,MobileDetail bean,String startDate){
		boolean b = true;
		try {
			 Document doc  = Jsoup.parse(text);
			 Elements tables = doc.select("table");
			 if(tables.size()>1){
				 Elements table7 = tables.get(1).select("tr");
				 for(int i = 1;i<table7.size();i++){ 
					 	Elements tds = table7.get(i).select("td");
					 	String tem="";
					 	// 起始时间
					 	String qssj = tds.get(0).text();
					 	// 通信地点
					 	String txdd = tds.get(1).text();
					 	// 通信方式
					 	String txfs = tds.get(2).text();
					 	// 对方号码
					 	String dfhm = tds.get(3).text();
					 	// 通信时长(秒)
					 	String dxsc = tds.get(4).text();
					 	// 通信类型
						String txlx = tds.get(5).text();
						// 套餐优惠优惠或减免(元)
						String tzyh = tds.get(6).text();
						
						// 实收通信费(元)
						String sjtxf = tds.get(7).text();
						
						
						Map map = new HashMap();
						map.put("phone", login.getLoginName());
						map.put("cTime", DateUtils.StringToDate(startDate.substring(0, 4)+"-"+qssj, "yyyy-MM-dd HH:mm:ss"));
						List<MobileDetail> mobileTelList = (List) mobileDetailService.getMobileDetailBypt(map);
						if(mobileTelList==null || mobileTelList.size()==0){
							int times = 0;
							try{
								TimeUtil tunit = new TimeUtil();
								times = tunit.timetoint(dxsc);
							}catch(Exception e){
								
							}
							
				        	MobileDetail mDetail = new MobileDetail();
				        	UUID uuid = UUID.randomUUID();
							mDetail.setId(uuid.toString());
				        	mDetail.setcTime(DateUtils.StringToDate(startDate.substring(0, 4)+"-"+qssj, "yyyy-MM-dd HH:mm:ss"));
				        	mDetail.setTradeAddr(txdd);
				        	mDetail.setTradeWay(txfs);
				        	mDetail.setRecevierPhone(dfhm);
				        	mDetail.setTradeTime(times);
				        	mDetail.setTradeType(txlx);
				        	mDetail.setTaocan(tzyh);
				        	mDetail.setOnlinePay(new BigDecimal(sjtxf));
				        	mDetail.setPhone(login.getLoginName());
				        	
							mobileDetailService.saveMobileDetail(mDetail);
						}
				 }
			}
			
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	/**
	 * 保存短信
	 */
	public void saveMessage(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"",null,"service.jx.10086.cn",true);
			boolean b = false;
			int num = 0;
						
			MobileMessage bean = new MobileMessage();
			bean.setPhone(login.getLoginName());
			bean = mobileMessageService.getMaxSentTime(bean.getPhone());
			for (String startDate : ms) {
			
				String year = startDate.substring(0, 4);
				String mouth = startDate.substring(4, 6);
				String sday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)),"yyyyMMdd");
				String eday = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)),"yyyyMMdd");
				String  text=cutil.get("http://service.jx.10086.cn/service/showBillDetail!billQueryCommit.action?&page=1&pageC=1000&otherorder=&billType=204&startDate="+sday+"&endDate="+eday+"&clientDate=&menuid=00890201&requestStartTime=",h);
				
				b= message_parse(text,bean,startDate);
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningMessageHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
			
		
	}

	
	
	/**
	 * 查询通话记录
	 */
	public void callHistory(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"",null,"service.jx.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);
			for (String startDate : ms) {
			
				String year = startDate.substring(0, 4);
				String mouth = startDate.substring(4, 6);
				String sday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)),"yyyyMMdd");
				String eday = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)),"yyyyMMdd");
				String  text=cutil.get("http://service.jx.10086.cn/service/showBillDetail!billQueryCommit.action?&page=1&pageC=1000&otherorder=&billType=202&startDate="+sday+"&endDate="+eday+"&clientDate=2014-06-05%2022:44:43&menuid=00890201&requestStartTime=2014-06-05%2022:39:36.636",h);
				b= callHistory_parse(text,bean,startDate);
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
			
		
	}

	
	
	
	
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
		int type = login.getType();
		switch (type) {
		case 1:
			
			break;
		case 2:
			getTelDetailHtml();//通话记录
			getMyInfo(); //个人信息
			callHistory(); //历史账单
			saveMessage(); //短信记录
			getFlowDetail(); //流量
			break;
		case 3:
			getTelDetailHtml();//通话记录
			getMyInfo(); //个人信息
			callHistory(); //历史账单
			saveMessage(); //短信记录
			getFlowDetail(); //流量
			break;
		default:
			break;
		}
	}
	

	// 首页登录
	public Map<String,Object> login() {
		try{
		String result = login1();
		if(result!=null&& result.equals("1")){
			loginsuccess();
		}else if(result!=null){
			errorMsg=result;
		
		}else{
			errorMsg="密码或验证码错误.";
		}
		
		if(status==1){
			sendPhoneDynamicsCode();
			addTask_1(this);
    	}
		}catch(Exception e){
			writeLogByLogin(e);
		}
		
		return map;
	}
	
	
	public String login1(){
		try {
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			Map<String, String> parm = new LinkedMap();
			parm.put("RelayState","type=A;backurl=http://www.jx.10086.cn/my/;nl=3;loginFrom=http://www.jx.10086.cn/;refer=");
			parm.put("backurl", "https://jx.ac.10086.cn/4login/backPage.jsp");
			parm.put("emailPwd", "");
			parm.put("errorurl","https://jx.ac.10086.cn/4login/errorPage.jsp");
			parm.put("mobileNum", login.getLoginName());
			parm.put("nickName", "");
			parm.put("servicePassword", login.getPassword());
			parm.put("smsValidCode", "");
			parm.put("spid",jsmap.get("spid").toString() );
			parm.put("ssoPwd", "");
			parm.put("type", "B");
			parm.put("validCode", login.getAuthcode());
			parm.put("website_pwd_email", "");
			CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"jx.ac.10086.cn",true);
			String text =cutil.post("https://jx.ac.10086.cn/Login", h, parm);
			if(text!=null){
				if(text.contains("code=2003")){
					return "验证码错误.";
				}else if(text.contains("code=4001")){
					return "密码错误.";
					
				}else if(text.contains("https://jx.ac.10086.cn/4login/backPage.jsp")){
					Document doc = Jsoup.parse(text);
					String SAMLart = doc.select("input[name=SAMLart]").first().attr("value");
					String RelayState = doc.select("input[name=RelayState]").first().attr("value");
					String displayPic = doc.select("input[name=displayPic]").first().attr("value");
					parm = new LinkedMap();
					parm.put("SAMLart", SAMLart);
					parm.put("RelayState", RelayState);
					parm.put("displayPic", displayPic);
					text =cutil.post("https://jx.ac.10086.cn/4login/backPage.jsp", h, parm);
					if(text!=null){
						doc = Jsoup.parse(text);
						String SAMLart1 = doc.select("body").first().attr("onLoad").replace("parent.callBackurl('", "").replace("');", "");
						parm = new LinkedMap();
						parm.put("SAMLart", SAMLart1);
						parm.put("RelayState","type=A;backurl=http://www.jx.10086.cn/my/;nl=3;loginFrom=http://www.jx.10086.cn/;refer=");
						h = new CHeader(CHeaderUtil.Accept_,"http://www.jx.10086.cn/my/",CHeaderUtil.Content_Type__urlencoded,"www.jx.10086.cn",true);
						text =cutil.post("http://www.jx.10086.cn/my/", h, parm);
						if(text!=null && text.contains("http://www.jx.10086.cn/my/")){
							text= cutil.get("http://www.jx.10086.cn/my/",h);
							if(text!=null&&text.contains("当前余额")){
								getYue(text);
								return "1";
							}
						}
						
						
					}
				}
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
	}
	

	public void getYue(String text){
		BigDecimal phoneremain = new BigDecimal("0");
		Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		try {
			text= text.replaceAll("\\s*", "");
			if(text.contains("当前余额")){
					RegexPaserUtil rp = new RegexPaserUtil("当前余额：</span><spanclass=\"my_yellow\">","元</span>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					text = rp.getText();
					if(text!=null){
						 phoneremain = new BigDecimal(text);
					}
				}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		jsmap.put("yue", phoneremain);
	}
	
	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
		Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		Map<String, String> parm = new LinkedMap();
		parm.put("backurl","http://service.jx.10086.cn/service/backAction.action?menuid=00890201");
		parm.put("brandFlag", "gotone");
		parm.put("errorurl","https://jx.ac.10086.cn/4login/errorPage.jsp");
		parm.put("checkErrFlag", "");
		parm.put("contextPath", "/service");
		parm.put("contractCheck", "agree");
		parm.put("ecare.confirm", "确定");
		parm.put("errorurl","http://service.jx.10086.cn/service/common/ssoPrompt.jsp");
		parm.put("fieldErrFlag", "");
		parm.put("fullid", "");
		parm.put("validCode", login.getAuthcode());
		parm.put("loginFlag", "false");
		parm.put("loginStatus", "A");
		parm.put("menuid", "00890201");
		parm.put("mobileNum", login.getLoginName());
		parm.put("operType", "");
		parm.put("requestStartTime", "");
		parm.put("showConfirmMessage", "noBtnConfirmMore");
		parm.put("sid", "05E7CC5B115556A554E1DABC8293C26E");
		parm.put("smsValidCode", login.getPhoneCode());
		parm.put("spid", jsmap.get("spid").toString());
		parm.put("ssoImageUrl","https://jx.ac.10086.cn/common/image.jsp");
		parm.put("ssoSmsUrl", "https://jx.ac.10086.cn/SMSCodeSend");
		parm.put("type", "A");
		CHeader h = new CHeader(CHeaderUtil.Accept_,"",null,"jx.ac.10086.cn",true);
		String text = cutil.post("https://jx.ac.10086.cn/Login", h, parm);
		if(text!=null){
			if(text.contains("code=2003")){
				errorMsg="图形验证码错误,请重新获取.";
			}else if(text.contains("code=2008")){
				errorMsg="短信验证码已经超时,请重新获取";
			}else if(text.contains("code=2004")){
				errorMsg="短信验证码错误,请重新获取.";
			}else if(text.contains("displayPic")){
				Document doc = Jsoup.parse(text);
				String displayPic2 = doc.select("input[name=displayPic]").first().attr("value");
				String RelayState2 = doc.select("input[name=RelayState]").first().attr("value");
				String SAMLart2 = doc.select("input[name=SAMLart]").first().attr("value");
				String menuid2 = doc.select("input[name=menuid]").first().attr("value");

				parm = new LinkedMap();
				parm.put("displayPic", displayPic2);
				parm.put("RelayState", RelayState2);
				parm.put("SAMLart", SAMLart2);
				parm.put("menuid", menuid2);
				h = new CHeader(CHeaderUtil.Accept_,"",null,"service.jx.10086.cn",true);
				text = cutil.post("http://service.jx.10086.cn/service/backAction.action",h,parm);
				
				if(text.contains("操作成功")){
					status = 1;	
					errorMsg= "登录成功";
				}
			}
		}
		if(errorMsg ==null){
			errorMsg = "验证失败,请重试!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		if(status==1){
        	addTask_2(this);
    	}
		return map;	
	
		
	}

	/**
	 * 生成短信
	 * */
	public Map<String,Object> sendPhoneDynamicsCode() {
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		int status= 0;
		CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"jx.ac.10086.cn",true);
		CHeader h1 = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"service.jx.10086.cn",true);
		try {
			cutil.setHandleRedirect(false);
			String text =cutil.get("https://jx.ac.10086.cn/SMSCodeSend?mobileNum="+login.getLoginName()+"&errorurl=http://service.jx.10086.cn/service/common/ssoPrompt.jsp",h);
			cutil.setHandleRedirect(true);
			text =cutil.get(text,h1);

			if(text!=null&&text.contains("随机验证码请求次数过多")){
				errorMsg="随机验证码请求次数过多";
			}else if(text!=null&&text.contains("短信验证码已发送到您的手机")){
				errorMsg="短信验证码已发送到您的手机";
				status = 1;
			}	
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(errorMsg==null){
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}


	private void getFlowDetail(){
		Map<String, String> map = new LinkedMap();
		Map<String, String> param = new LinkedMap();
		param.put("keyId", "getMonitorTime");
		String smallscrew = cutil.post("http://service.jx.10086.cn/service/smallscrew", param);
		if(smallscrew != null){
			try {
				JSONObject jso = new JSONObject(smallscrew);
				String val = jso.getString("val");
				if(val != null){
					map.put("requestStartTime", val);
					getFlowDetail_OtherOrder(map);
				}else{
					logger.error("江西移动流量查询requestStartTime为空");
				}
			} catch (JSONException e) {
				logger.error("江西移动流量查询requestStartTime转换json出错", e);
			}
		}else{
			logger.error("江西移动流量查询获取requestStartTime出错！");
		}
	}
	
	private void getFlowDetail_OtherOrder(Map map){
		List<String> months = DateUtils.getMonths(6, "yyyy-MM");
		String clientDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		for (String month : months) {
			String startDate = DateUtils.firstDayOfMonth(month).replace("-", "");
			String endDate = DateUtils.lastDayOfMonth(month).replace("-", "");
		
			//String sday = TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)),"yyyyMMdd");
			//String eday = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)),"yyyyMMdd");
			
			
			String url = "http://service.jx.10086.cn/service/showBillDetail!queryIndex.action?billType=203&startDate="+startDate+"&endDate="+endDate+"&clientDate="+clientDate+"&menuid=00890201&requestStartTime="+map.get("requestStartTime").toString();
			String text = cutil.get(url);
			if(text != null){
				String otherorder = StringUtil.subStr("var otherorder = \"", "\";", text);
				if(otherorder != null){
					getFlowDetail_begin(otherorder, startDate, endDate, clientDate, map.get("requestStartTime").toString());
				}else{
					logger.error("江西移动流量查询otherorder为空");
				}
			}else{
				logger.error("江西移动流量查询获取OtherOrder出错！");
			}
		}
	}
	
	private void getFlowDetail_begin(String otherorder, String startDate, String endDate, String clientDate, String requestStartTime){
		double pageCount = 0;
		MobileOnlineBill mob = new MobileOnlineBill();
		Long totalFlow=0L;
		double trafficCharges=0;
		String text = cutil.get("http://service.jx.10086.cn/service/showBillDetail!billQueryCommit.action?otherorder="+otherorder+"&billType=203&startDate="+startDate+"&endDate="+endDate+"&clientDate="+clientDate+"&menuid=00890201&requestStartTime="+requestStartTime);
		if(text != null && text.contains("详单内容页")){
			Document doc = Jsoup.parse(text);
			Elements table1 = doc.select("#table1");
			if(table1.size() > 0){
				for (Element table : table1) {
					Elements trs = table.getElementsByTag("tr");
					if(trs.size()>0){
						Element tr = trs.get(1);
						Elements tds = tr.getElementsByTag("td");
						if(tds.size()>0){
							pageCount = Integer.parseInt(tds.get(0).text());
						}
					}
				}
			}
			Elements tables = doc.select("#datatable");
			if(tables.size()>0){
				for (Element table : tables) {
					Elements trs = table.getElementsByTag("tr");
					List<MobileOnlineList> list = new ArrayList<MobileOnlineList>();
					MobileOnlineList bean = new MobileOnlineList();
					bean = mobileOnlineListService.getMaxTime(loginName);
					if(trs.size() > 0){
						for(int i=1; i<trs.size(); i++){
							MobileOnlineList mol = new MobileOnlineList();
							mol.setPhone(login.getLoginName());
							Elements tds = trs.get(i).getElementsByTag("td");
							if(tds.size() > 0){
								for (int j=0; j<tds.size(); j++) {
									String temp = tds.get(j).text();
									switch (j) {
									case 0:
										String beginTime = String.valueOf(startDate.substring(0, 4)) + "-" + temp;
										mol.setcTime(DateUtils.StringToDate(beginTime, "yyyy-MM-dd hh:mm:ss"));
										break;
									case 1:
										String location = temp;
										mol.setTradeAddr(location);
										break;
									case 2:
										String netType = temp;
										mol.setOnlineType(netType);
										break;
									case 3:
										String allTime = temp;
										mol.setOnlineTime(Long.parseLong(allTime));
										break;
									case 4:
										String allFlow = temp;
										mol.setTotalFlow(StringUtil.flowFormat(allFlow).longValue());
										break;
									case 5:
										String tcyh = temp;
										mol.setCheapService(tcyh);
										break;
									case 6:
//										String yhhjm = temp;
										break;
									case 7:
										String txf = temp;
										mol.setCommunicationFees(new BigDecimal(txf));
										break;
									}
								}
							}else{
								logger.error("江西移动流量详单查询datatable中未找到td");
							}
							//一行结束表示一条数据结束，存储
							if (bean!=null&&bean.getcTime()!=null&&bean.getcTime().getTime()<=mol.getcTime().getTime()) {
								continue ;
							}else {
								UUID uuid = UUID.randomUUID();
								mol.setId(uuid.toString());
								list.add(mol);
							}
							totalFlow += mol.getTotalFlow();
							trafficCharges += mol.getCommunicationFees().doubleValue();
							
						}
						//tr循环结束表示当前页结束
						mobileOnlineListService.insertbatch(list);
					}else{
						logger.error("江西移动流量详单查询datatable中未找到tr");
					}
				}
				if(Math.round(pageCount/20) > 1){
					String js_engine = cutil.get("http://service.jx.10086.cn/service/dwr/engine.js?_="+System.currentTimeMillis());
					if(js_engine != null){
						String scriptSessionId  = StringUtil.subStr("dwr.engine._origScriptSessionId = \"", "\";", js_engine);
						if(scriptSessionId != null){
							for(int i=2;i<=Math.round(pageCount/20);i++){
								String param0 = String.valueOf((i-1)*20);
								String param1 = String.valueOf(i*20);
								String[][] pairs = {{"callCount", "1"}, {"page", "/service/showBillDetail!queryIndex.action?billType=203&startDate="+startDate+"&endDate="+endDate+"&clientDate="+clientDate+"&menuid=00890201&requestStartTime="+requestStartTime},
										{"httpSessionId",getJsessionId(".jx.10086.cn")}, {"scriptSessionId", scriptSessionId+Math.round(Math.random()*100)}, {"c0-scriptName", "queryBill"}, {"c0-methodName", "getHtml"}, {"c0-id", "0"}, {"c0-param0", "number:"+param0},
										{"c0-param1", "number:"+param1}, {"c0-param2", "boolean:false"}, {"batchId", "0"}};
								String url = "http://service.jx.10086.cn/service/dwr/call/plaincall/queryBill.getHtml.dwr";
								CHeader header = new CHeader();
								header.setAccept("application/json, text/javascript, */*");
								header.setAccept_Encoding("gzip, deflate");
								header.setAccept_Language("zh-CN");
								try {
									String dwrText = cutil.post(url, header, joinPairsToPostBodyForDWR(pairs));
									if(dwrText != null){
										String new_dt = StringUtil.decodeUnicode(dwrText);
										String temp = StringUtil.subStr("dwr.engine._remoteHandleCallback('0','0',\"{\"list\":[[\"", "\"]]}\");", new_dt);
										if(temp != null){
											String[] splits = temp.split("\"\\],\\[\"");
											List<MobileOnlineList> list = new ArrayList<MobileOnlineList>();
											MobileOnlineList bean = new MobileOnlineList();
											bean = mobileOnlineListService.getMaxTime(loginName);
											for (String row : splits) {
												MobileOnlineList mol = new MobileOnlineList();
												mol.setPhone(login.getLoginName());
												String[] columns = row.split("\",\"");
												for(int j=0;columns.length ==8 && j<columns.length;j++){
													temp = columns[j];
													switch (j) {
													case 0:
														String beginTime = String.valueOf(startDate.substring(0, 4)) + "-" + temp;
														//System.out.println("==========="+beginTime);
														mol.setcTime(DateUtils.StringToDate(beginTime, "yyyy-MM-dd hh:mm:ss"));
														break;
													case 1:
														String location = temp;
														mol.setTradeAddr(location);
														break;
													case 2:
														String netType = temp;
														mol.setOnlineType(netType);
														break;
													case 3:
														String allTime = temp;
														mol.setOnlineTime(Long.parseLong(allTime));
														break;
													case 4:
														String allFlow = temp;
														mol.setTotalFlow(StringUtil.flowFormat(allFlow).longValue());
														break;
													case 5:
														String tcyh = temp;
														mol.setCheapService(tcyh);
														break;
													case 6:
//														String yhhjm = temp;
														break;
													case 7:
														String txf = temp;
														mol.setCommunicationFees(new BigDecimal(txf));
														break;
													}
												}
												if (bean!=null&&bean.getcTime()!=null&&mol.getcTime()!=null &&bean.getcTime().getTime()<=mol.getcTime().getTime()) {
													continue ;
												}else {
													UUID uuid = UUID.randomUUID();
													mol.setId(uuid.toString());
													list.add(mol);
												}
												if( mol.getTotalFlow()!=null){
													totalFlow += mol.getTotalFlow();
												}
												if(mol.getCommunicationFees()!=null){
													trafficCharges += mol.getCommunicationFees().doubleValue();
												}
											}
											mobileOnlineListService.insertbatch(list);
										}
									}
								} catch (UnsupportedEncodingException e) {
									logger.error("江西移动流量详单下一页查询出错", e);
								}
							}
						}
					}
				}
				
			}else{
				logger.info("江西移动流量详单查询未找到datatable");
			}
			
			mob.setPhone(loginName);
			mob.setMonthly(DateUtils.StringToDate(startDate, "yyyyMMdd"));
			mob.setTotalFlow(totalFlow);
			mob.setTrafficCharges(new BigDecimal(trafficCharges));
			String today = DateUtils.getToday("yyyyMMdd");
			if(DateUtils.isSameMonth(today, startDate,"yyyyMM"))
				mob.setIscm(1);
			else
				mob.setIscm(0);
			
			MobileOnlineBill mob_bean = mobileOnlineBillService.getMaxTime(loginName);
			if (mob_bean!=null && mob_bean.getMonthly()!=null&&mob.getMonthly()!=null&&mob_bean.getMonthly().getTime() < mob.getMonthly().getTime()) {
				
			}else if(mob_bean!=null&&mob_bean.getMonthly()!=null&& mob.getMonthly()!=null&&mob_bean.getMonthly().getTime() == mob.getMonthly().getTime()){
				mobileOnlineBillService.update(mob);
			}else {
				UUID uuid = UUID.randomUUID();
				mob.setId(uuid.toString());
				mobileOnlineBillService.save(mob);
			}
			
		}else{
			logger.info("江西移动流量详单数据未查询到");
		}
	}
	


}
