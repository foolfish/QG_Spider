package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.LinkedMap;
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

/**山东移动
 * @author fastw
 * @date   2014-8-2 下午3:01:55
 */
public class SDYidong extends BaseInfoMobile{
	
	public String index = "https://sd.ac.10086.cn/portal/mainLogon.do";
	// 验证码图片路径
	public static String imgurl = "https://sd.ac.10086.cn/portal/login/briefValidateCode.jsp";
	// 验证码图片路径
	public static String imgurl2 = "http://www.sd.10086.cn/eMobile/RandomCodeImage?pageId=0.2199086531560409";

	
	
	public SDYidong(Login login,String currentUser) {
		super(login,ConstantNum.comm_sd_yidong,currentUser);
	}


	public static void main(String[] args) {
		SDYidong sd = new SDYidong(new Login("13573142608","116880"), null);
		sd.index();
		sd.inputCode(imgurl);
		sd.login();
		sd.close();
	}

	
	 public void init(){
		if(!isInit()){
			String text = cutil.post(index,null);
			if(text!=null){
				 setImgUrl(imgurl);
				 setInit();	
			}
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
	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
			try {
				parseBegin(Constant.YIDONG);
				CHeader  h = new CHeader(CHeaderUtil.Accept_,"",null,"www.sd.10086.cn",true);
				
				String text = cutil.get("http://www.sd.10086.cn/eMobile/qryUserInfo_init.action?menuid=qryUserInfo&pageId=232938945" ,h);
				if(text!=null){
					Document doc = Jsoup.parse(text);
					String nickName = doc.select("input[id=nickName]").first().attr("value");
					String email = doc.select("input[id=email]").first().attr("value");
					RegexPaserUtil rp = new RegexPaserUtil("<div class=\"font_size_14 cut width_150\" title=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String xm = rp.getText();
					Map<String, String> parmap = new HashMap<String, String>(3);
					parmap.put("parentId", currentUser);
					parmap.put("loginName", login.getLoginName());
					parmap.put("usersource", Constant.YIDONG);
					List<User> list = userService.getUserByParentIdSource(parmap);
					if (list != null && list.size() > 0) {
						User user = list.get(0);
						user.setLoginName(login.getLoginName());
						user.setLoginPassword("");
						user.setUserName(xm);
						user.setRealName(xm);
						user.setIdcard("");
						user.setAddr("");
						user.setUsersource(Constant.YIDONG);
						user.setParentId(currentUser);
						user.setModifyDate(new Date());
						user.setPhone(login.getLoginName());
						user.setFixphone("");
						user.setPhoneRemain(getYue());
						user.setEmail(email);
						userService.update(user);
					} else {
						User user = new User();
						UUID uuid = UUID.randomUUID();
						user.setId(uuid.toString());

						user.setLoginName(login.getLoginName());
						user.setLoginPassword("");
						user.setUserName(xm);
						user.setRealName(xm);
						user.setIdcard("");
						user.setAddr("");
						user.setUsersource(Constant.YIDONG);
						user.setParentId(currentUser);
						user.setModifyDate(new Date());
						user.setPhone(login.getLoginName());
						user.setFixphone("");
						user.setPhoneRemain(getYue());
						user.setEmail(email);
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
	public void getCurrentTelDetailHtml() {
		List<String> ms = DateUtils.getMonths(1,"yyyyMM");
		String startDate = (String)ms.get(0);
		String year = startDate.substring(0,4);
		String mon = startDate.substring(4,6);
		String month = year+"-"+mon;
		String firstDay = month+"-01";
		String endDay = DateUtils.lastDayOfMonth(month);
		Map<String, String> param = new LinkedHashMap<String,String>();
		param.put("cycleStartDate", "");
		param.put("menuid", "queryFee");
		param.put("pageid","0.22124449273211844");
		param.put("queryMonth",startDate);
		String text = cutil.post("http://www.sd.10086.cn/eMobile/queryFee_qryFeeInfo.action",param);
		BigDecimal tcgdf = new BigDecimal(0);	
		BigDecimal cAllPay = new BigDecimal(0);
		if(text.contains("套餐及固定费")){
			 try {
				RegexPaserUtil rp = new RegexPaserUtil( "套餐及固定费\",\"","&nbsp" ,text, RegexPaserUtil.TEXTEGEXANDNRT);
				 String tcgdfStr = rp.getText();
				 tcgdf= new BigDecimal(tcgdfStr);
				 rp = new RegexPaserUtil( "本月费用合计\",\"","&nbsp" ,text, RegexPaserUtil.TEXTEGEXANDNRT);
				 String cAllPayStr = rp.getText();
				 cAllPay= new BigDecimal(cAllPayStr);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				log.error("current bill",e1);
			}
				
			 try {
				Map map2 = new HashMap();
				 map2.put("phone", login.getLoginName());
				 map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
				 List list = mobileTelService.getMobileTelBybc(map2);
				 if(list==null || list.size()==0){
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
					mobieTel.setcName("");
					mobieTel.setTeleno(login.getLoginName());
					mobieTel.setTcgdf(tcgdf);
					mobieTel.setDependCycle(firstDay+"至"+endDay);
					mobieTel.setcAllPay(cAllPay);
					mobieTel.setIscm(1);
					mobileTelService.saveMobileTel(mobieTel);
				 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void getTelDetailHtml() {
	
		try {
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.sd.10086.cn",true);
			parseBegin( Constant.YIDONG);
			int mm = 1;
			String text=null;
			String dqzq="";
			Map<String,String> param = new LinkedHashMap<String,String>();
			for (int k = 1 ;k<ms.size();k++) {
				String startDate = (String)ms.get(k) ;
				Map<String, String> parm = new LinkedMap();
				h.setRespCharset("utf-8");
				param = new LinkedHashMap<String,String>();
				param.put("contextPath", "/eMobile");
				param.put("customInfo.brandName", "");
				param.put("customInfo.custName","");
				param.put("customInfo.prodName", "");
				param.put("subsId", "");
				param.put("cycleStartDate", "");
				param.put("feeType", "");
				param.put("fieldErrFlag", "");
				param.put("menuid", "queryBill");
				param.put("month", startDate);
				param.put("retMonth","" );
				text = cutil.post("http://www.sd.10086.cn/eMobile/queryBill_custInfo.action?pageid=0.5859994330427745",h,param);
			//	System.out.println("==="+text);
				if(text!=null){
					Document doc6 = Jsoup.parse(text);
					String cycleStartDate = doc6.select("input[id=queryBill_cycleStartDate]").first().attr("value");
					String retMonth = doc6.select("input[id=retMonth]").first().attr("value");
					String subsId = doc6.select("input[id=queryBill_customInfo_subsId]").first().attr("value");
					String custName = doc6.select("input[id=queryBill_customInfo_custName]").first().attr("value");
					String brandName = doc6.select("input[id=queryBill_customInfo_brandName]").first().attr("value");
					String prodName = doc6.select("input[id=queryBill_customInfo_prodName]").first().attr("value");
					String cycle = doc6.select("input[id=queryBill_cycleMap_cycle_"+cycleStartDate+"]").first().attr("value");
					String cycleMap_startDate = doc6.select("input[id=queryBill_cycleMap_startDate_"+cycleStartDate+"]").first().attr("value");
					String cycleMap_endDate = doc6.select("input[id=queryBill_cycleMap_endDate_"+cycleStartDate+"]").first().attr("value");
					String cycleMap_acctId = doc6.select("input[id=queryBill_cycleMap_acctId_"+cycleStartDate+"]").first().attr("value");
					String cycleMap_unionacct = doc6.select("input[id=queryBill_cycleMap_unionacct_"+cycleStartDate+"]").first().attr("value");
					 param = new LinkedHashMap<String,String>();
					 param.put("contextPath", "/eMobile");
					 param.put("customInfo.brandName", brandName);
						param.put("customInfo.custName",custName);
						param.put("customInfo.prodName", prodName);
						param.put("customInfo.subsId", subsId);
						param.put("cycleMap.acctId_"+cycleStartDate, cycleMap_acctId);
						param.put("cycleMap.cycle_"+cycleStartDate, cycle);
						param.put("cycleMap.endDate_"+cycleStartDate, cycleMap_endDate);
						param.put("cycleMap.startDate_"+cycleStartDate, cycleMap_startDate);
						param.put("cycleMap.unionacct_"+cycleStartDate, cycleMap_unionacct);
						param.put("cycleStartDate", cycleStartDate);
						param.put("feeType", "");
						param.put("fieldErrFlag", "");
						param.put("menuid", "queryBill");
						param.put("month",startDate);
						param.put("retMonth", retMonth);
						
						text = cutil.post("http://www.sd.10086.cn/eMobile/queryBill_billInfo.action?pageid=0.22138219991894725",h,param);
						
						try {
							b=getTelDetailHtml_parse(text, startDate,cycleMap_startDate,cycleMap_endDate);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							log.error("bill",e);
						}
				}
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
	public boolean getTelDetailHtml_parse(String text,String startDate,String cycleMap_startDate,String cycleMap_endDate) {
		boolean b = true;
		try {
			BigDecimal tcgdf=new BigDecimal(0);	
			BigDecimal yytxf=new BigDecimal(0);	
			BigDecimal zzywf=new BigDecimal(0);	
			BigDecimal hj=new BigDecimal(0);
			String year = startDate.substring(0,4);
			String mon = startDate.substring(4,6);
			String month = year+"-"+mon;  //2014-10
			String firstDay = month+"-01";//2014-10-01
			String endDay = DateUtils.lastDayOfMonth(month);//2014-10-31
			if(!text.contains("您查询的账单未生成或不存在")){
				Document doc = Jsoup.parse(text);
				Elements tables =doc.select("table");
				for (int i = 0; i < tables.size(); i++) {
					Element table = tables.get(i);
					String tableString = table.text();
					if(tableString.contains("费用项目")){
						Elements tables2 = table.select("div[class=orderGoods_msg_table]").select("table");
						for (int y = 0; y < tables2.size(); y++) {
							Element table2 = tables2.get(y);
							String tableString2 = table2.text();
							if(tableString2.contains("费用信息")){
								Elements trs =  table2.select("tr");
								for(int x = 1 ; x<trs.size();x++){
									Element tr = trs.get(x);
									String trStr =tr.text();
									if(trStr.contains("套餐及固定费")){
										String tcgdfs =tr.select("td").get(1).text().replaceAll("\\s*", "").replace("  ","");
										if(tcgdfs!=null){
											tcgdf= new BigDecimal(tcgdfs);
										}
									}else if(trStr.contains("语音通信费")){
										
										String yytxfs = tr.select("td").get(1).text().replaceAll("\\s*", "").replace("  ","");
										if(yytxfs!=null){
											yytxf= new BigDecimal(yytxfs);
										}
									}else if(trStr.contains("增值业务费")){
										
										String zzywfs =tr.select("td").get(1).text().replaceAll("\\s*", "").replace("  ","");
										if(zzywfs!=null){
											zzywf= new BigDecimal(zzywfs);
										}
									}
									else if(trStr.contains("合计")){
										
										String hjs = tr.select("td").get(1).text().replaceAll("\\s*", "").replace("  ","");
										if(hjs!=null){
											hj= new BigDecimal(hjs);
										}
									 }	
								   }
							     }
						      }
					        }
				          }
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
					mobieTel.setcName("");
					mobieTel.setTeleno(login.getLoginName());
					mobieTel.setZzywf(zzywf);
					mobieTel.setTcgdf(tcgdf);
					mobieTel.setDependCycle(firstDay+"至"+endDay);
					mobieTel.setcAllPay(hj);
					mobieTel.setTcwyytxf(yytxf);
					mobileTelService.saveMobileTel(mobieTel);
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
			if(text!=null&& text.contains("通话详单")&& text.contains("通信地点")){
				Document doc6 = Jsoup.parse(text);
				Elements tables =doc6.select("table");
				for (int i = 0; i < tables.size(); i++) {
					
					Element table = tables.get(i);
					String tableString = table.text();
					if(tableString.contains("通话详单")&& tableString.contains("起始时间")){
						Elements trs = table.select("tr");
						String fixdate="";
						for(int j = 2 ; j<trs.size();j++){
							Elements tds = trs.get(j).select("td");
							if(tds.size()==1){
								fixdate=tds.get(0).text();
							}else if(tds.size()==9 && !tds.text().contains("合计")){
								String qssj = fixdate+" "+tds.get(0).text();
								String thdd = tds.get(1).text().replaceAll("\\s*", "");;
								String txfs = tds.get(2).text().replaceAll("\\s*", "");;
								String dfhm = tds.get(3).text().replaceAll("\\s*", "");;
								String thsc = tds.get(4).text().replaceAll("\\s*", "");;
								String txlx = tds.get(5).text().replaceAll("\\s*", "");;
								String tcyh = tds.get(6).text().replaceAll("\\s*", "");;
								String thf = tds.get(7).text().replaceAll("\\s*", "");;
								String ctf = tds.get(8).text().replaceAll("\\s*", "");;
								
								Map map2 = new HashMap();
								 map2.put("phone", login.getLoginName());
								 map2.put("cTime", DateUtils.StringToDate(qssj, "yyyy-MM-dd HH:mm:ss"));
								 List list = mobileDetailService.getMobileDetailBypt(map2);
								if(list==null || list.size()==0){
									MobileDetail mDetail = new MobileDetail();
									UUID uuid = UUID.randomUUID();
									mDetail.setId(uuid.toString());
						        	mDetail.setcTime(DateUtils.StringToDate(qssj, "yyyy-MM-dd HH:mm:ss"));
						        	mDetail.setTradeAddr(thdd);
						        	if(txfs.contains("主叫")){
						        		mDetail.setTradeWay("主叫");
						        	}else if(txfs.contains("被叫")){
						        		mDetail.setTradeWay("被叫");
						        	}
						        	int times = 0;
									try{
										TimeUtil tunit = new TimeUtil();
										times = tunit.timetoint(thsc);
									}catch(Exception e){
										
									}		
						        	mDetail.setRecevierPhone(dfhm);
						        	mDetail.setTradeTime(times);
						        	mDetail.setTradeType(txlx);
						        	mDetail.setTaocan(tcyh);
						        	mDetail.setOnlinePay(new BigDecimal(thf));
						        	mDetail.setPhone(login.getLoginName());
						        	mobileDetailService.saveMobileDetail(mDetail);
								}
								
								
							}
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
	
	/**
	 * 查询通话记录
	 */
	public void callHistory(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(7,"yyyyMM");
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.sd.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);
			for (int k = 1 ;k<ms.size();k++) {
				String startDate = (String)ms.get(k);
				h.setRespCharset("utf-8");
				Map<String,String> param = new LinkedHashMap<String,String>();
				String text = cutil.get("http://www.sd.10086.cn/eMobile/queryBillDetail_custInfo.action?month="+startDate+"&menuid=billdetails&pageid=0.1079183998311275" ,h);
				if(text!=null && text.contains("cyclelist")){
					RegexPaserUtil rp = new RegexPaserUtil("cyclelist\":\\[","\\]",text,RegexPaserUtil.TEXTEGEXANDNRT);
					text = rp.getText().trim();
					if(!text.contains("{")||!text.contains("}")) continue;
		    		JSONObject json=new JSONObject(text);
					String cycle = json.getString("cycle");
					String endDate = json.getString("endDate");
					String sdate = json.getString("startDate");
					param = new LinkedHashMap<String,String>();
					param.put("billcycle", sdate+"-"+endDate+"-"+cycle);
					param.put("checkedStyle", "on");
					param.put("contextPath","/eMobile");
					param.put("fieldErrFlag", "");
					param.put("menuid", "billdetails");
					param.put("month", startDate);
					param.put("queryType", "2");
					param.put("tempStartDate", "2014-07-01 00:00:00");
					text = cutil.post("http://www.sd.10086.cn/eMobile/queryBillDetail_detailBill.action?pageid=0.874315694355501&dateType=byMonth&startDate="+sdate+"&endDate="+endDate+"&month="+startDate+"&cycle="+cycle+"",h,param);
					
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
			getCurrentTelDetailHtml();
			getTelDetailHtml();//历史账单	
			getMyInfo(); //个人信息
			break;
		case 2:
			callHistory(); //通话记录
			getSmsLog();   //短信详单
			getLiuliang(); //流量
			break;
		case 3:
			getTelDetailHtml();//历史账单
			getMyInfo(); //个人信息
			callHistory(); //通话详单
			getSmsLog();
			getLiuliang();
			break;
		default:
			break;
		}
	}
	
	public void getSmsLog()
	{
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(7,"yyyyMM");
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.sd.10086.cn",true);
			boolean b = false;
			int num = 0;
			for (String m: ms) {
				String startDate = m.trim();
				h.setRespCharset("utf-8");
				Map<String,String> param = new LinkedHashMap<String,String>();
				String text = cutil.get("http://www.sd.10086.cn/eMobile/queryBillDetail_custInfo.action?month="+startDate+"&menuid=billdetails&pageid=0.7536554159847282" ,h);
				if(text!=null && text.contains("cyclelist")){
					RegexPaserUtil rp = new RegexPaserUtil("cyclelist\":\\[","\\]",text,RegexPaserUtil.TEXTEGEXANDNRT);
					text = rp.getText().trim();
					if(!text.contains("{")||!text.contains("}")) continue;
		    		JSONObject json=new JSONObject(text);
					String cycle = json.getString("cycle");
					String endDate = json.getString("endDate");
					String sdate = json.getString("startDate");
					param = new LinkedHashMap<String,String>();
					param.put("billcycle", sdate+"-"+endDate+"-"+cycle);
					param.put("checkedStyle", "on");
					param.put("contextPath","/eMobile");
					param.put("fieldErrFlag", "");
					param.put("menuid", "billdetails");
					param.put("month", startDate);
					param.put("queryType", "3");
					param.put("tempStartDate", "2014-07-01 00:00:00");
					text = cutil.post("http://www.sd.10086.cn/eMobile/queryBillDetail_detailBill.action?pageid=0.874315694355501&dateType=byMonth&startDate="+sdate+"&endDate="+endDate+"&month="+startDate+"&cycle="+cycle+"",h,param);
					
					b= SmsLog_parse(text,startDate);
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
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}

	boolean SmsLog_parse(String text,String startDate)
	{
		boolean b = true;
		try {
			Date lastTime = null;
			try{
				if(mobileMessageService.getMaxSentTime(login.getLoginName())!=null)
				lastTime = mobileMessageService.getMaxSentTime(login.getLoginName()).getSentTime();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			if(text!=null&& text.contains("通信方式")&& text.contains("信息类型")){
				Document doc6 = Jsoup.parse(text);
				Elements tables =doc6.select("table[class=box_out06]");
				for (int i = 0; i < tables.size(); i++) {
					
					Element table = tables.get(i);
					String tableString = table.text();
					if(tableString.contains("通信方式")&& tableString.contains("信息类型")){
						Elements trs = table.select("tr");
						String fixdate="";
						for(int j = 3 ; j<trs.size()-1;j++){
							Elements tds = trs.get(j).select("td");
							if(tds.size()==1){
								fixdate=tds.get(0).text().trim();
							}else if(tds.size()==8){								
									 String sentTime= fixdate+" "+tds.get(0).text().trim();
									 String tradeWay= tds.get(3).text().trim();
									 String recevierPhone= tds.get(2).text().trim();
									 String sentAddr= tds.get(1).text().trim();
									 String allPay= tds.get(7).text().trim();	
									 String phone = login.getLoginName();//本机号码
									
									 MobileMessage message = new MobileMessage();
									 message.setAllPay(new BigDecimal(allPay));
									 message.setRecevierPhone(recevierPhone);
									 message.setSentAddr(sentAddr);
									 Date times=null;					 
									try{
										times=DateUtils.StringToDate( sentTime, "yyyy-MM-dd HH:mm:ss");							
										if(lastTime!=null&&times!=null){
											if(times.getTime()<=lastTime.getTime()){
												continue;
											}	
										}
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									 message.setSentTime(times);
									 message.setTradeWay(tradeWay);
									 message.setPhone(phone);
									 message.setCreateTs(new Date());
									 UUID uuid = UUID.randomUUID();
									 message.setId(uuid.toString());
									 mobileMessageService.save(message);
								
							}
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
	
	public void getLiuliang()
	{
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.sd.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileOnlineBill bean_Bill = null;
			
			if(mobileOnlineBillService .getMaxTime(login.getLoginName())!= null) {
			   bean_Bill = mobileOnlineBillService.getMaxTime(login .getLoginName());
			 }
			MobileOnlineList bean_List = null;
			
			if(mobileOnlineListService .getMaxTime(login.getLoginName())!= null) {
			   bean_List = mobileOnlineListService.getMaxTime(login .getLoginName());
			 }


			for (int j = 0; j < ms.size();j++) {
				String startDate = ms.get(j);
				//String nextMon = ms.get(i);
				String year = startDate.substring(0,4);
				String mon = startDate.substring(4,6);
				String month  = year+"-"+mon;
				String lastDay = DateUtils.lastDayOfMonth(month);
				Map<String,String> param = new LinkedHashMap<String,String>();
//				String text = cutil.get("http://www.sd.10086.cn/eMobile/queryBillDetail_custInfo.action?month="+startDate+"&menuid=billdetails&pageid=0.7536554159847282" ,h);
//				if(text!=null && text.contains("cyclelist")){
//					RegexPaserUtil rp = new RegexPaserUtil("cyclelist\":\\[","\\]",text,RegexPaserUtil.TEXTEGEXANDNRT);
//					text = rp.getText().trim();
//					if(!text.contains("{")||!text.contains("}")) continue;
//		    		JSONObject json=new JSONObject(text);
//					String cycle = json.getString("cycle");
//					String endDate = json.getString("endDate");
//					String sdate = json.getString("startDate");
//					param = new LinkedHashMap<String,String>();
//					param.put("billcycle", sdate+"-"+endDate+"-"+cycle);
//					param.put("checkedStyle", "on");
//					param.put("contextPath","/eMobile");
//					param.put("fieldErrFlag", "");
//					param.put("menuid", "billdetails");
//					param.put("month", startDate);
//					param.put("queryType", "4");
//					param.put("tempStartDate", "2014-07-01 00:00:00");
					
//					cycle	undefined
//					dateType	byTime
//					endDate	2014-10-23 23:59:59
//					month	
//					pageid	0.8661733516455368
//					startDate	2014-10-01 00:00:00
					
//					billcycle	20140823-20140922-20140823
//					checkedStyle	on
//					contextPath	/eMobile
//					fieldErrFlag	
//					menuid	billdetails
//					month	201408
//					queryType	4
//					tempStartDate	2014-10-01 00:00:00
					//String endDate = nextMon+"22";
					String sdate = startDate+"23";
					param = new LinkedHashMap<String,String>();
					
					param.put("cycle", "undefined");
					param.put("dateType","byTime");
					param.put("endDate", lastDay+" 23:59:59");
					param.put("month", "");
					param.put("pageid", "0.25191775799982696");
					param.put("startDate", month+"-01 00:00:00");
					
					//param.put("billcycle", sdate+"-"+endDate+"-"+sdate);
					param.put("billcycle", "");
					param.put("checkedStyle", "on");
					param.put("contextPath","/eMobile");
					param.put("fieldErrFlag", "");
					param.put("menuid", "billdetails");
					param.put("month", startDate);
					param.put("queryType", "4");
					param.put("tempStartDate", year+"-"+mon+"-01 00:00:00");
					//http://www.sd.10086.cn/eMobile/queryBillDetail_detailBill.action?pageid=0.25191775799982696&dateType=byTime&startDate=2014-09-01%2000:00:00&endDate=2014-09-30%2023:59:59&month=&cycle=undefined
					String text = cutil.post("http://www.sd.10086.cn/eMobile/queryBillDetail_detailBill.action",param);
					//System.out.println(text);
					try {
						b = onlineBill_parse(text,bean_Bill,startDate,j);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						b= Liuliang_parse(text,startDate,bean_List);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
				}//if(b)
			 }//for
		  
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
}
	
	
	//流量账单
	public boolean  onlineBill_parse(String text,MobileOnlineBill bean_Bill,String startDate,int iscm1){
		boolean b = true;
		try {
			if(text.contains("上网详单")){
				Document doc6 = Jsoup.parse(text);
				Element table =doc6.select("table[class=box_out06]").get(0);

							Date monthly = null;
							long totalFlow = 0;
//							long freeFlow = 0;
//							long chargeFlow = 0;
							BigDecimal communicationFees = new BigDecimal(0);
							int iscm = 0;
							startDate = startDate.substring(0,4)+"-"+startDate.substring(4,6);
							try {
								monthly = DateUtils.StringToDate(startDate,"yyyy-MM");
								String totalFlowStr = table.select("tr:eq(3)>td:eq(3)").text().trim();
								String totalFeesStr = table.select("tr:eq(1)>td:eq(3)").text().replace("元", "");
								totalFlow = Math.round(StringUtil.flowFormat(totalFlowStr));
								communicationFees = new BigDecimal(totalFeesStr);
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
								 }else {
						        	onlineBill.setMonthly(monthly);
						        	onlineBill.setTotalFlow(totalFlow);
									onlineBill.setTrafficCharges(communicationFees);
						        	onlineBill.setPhone(login.getLoginName());   
						        	if(iscm1==0) {
						        		iscm = 1;
						        	}
						        	onlineBill.setIscm(iscm);
									mobileOnlineBillService.save(onlineBill);
								 }
							 }else {
						        	onlineBill.setMonthly(monthly);
						        	onlineBill.setTotalFlow(totalFlow);
									onlineBill.setTrafficCharges(communicationFees);
						        	onlineBill.setPhone(login.getLoginName());   
						        	if(iscm1==0) {
						        		iscm = 1;
						        	}
						        	onlineBill.setIscm(iscm);
									mobileOnlineBillService.save(onlineBill);
							 }
					}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	
	public boolean Liuliang_parse(String text,String startDate,MobileOnlineList bean_List)
	{
			boolean b = true;
			List<MobileOnlineList> mobileOnlineList = new LinkedList<MobileOnlineList>();
			if(text!=null&& text.contains("上网详单")&& text.contains("上网方式")){
				Document doc6 = Jsoup.parse(text);
				Elements tables =doc6.select("table[class=box_out06]");
				for (int i = 0; i < tables.size(); i++) {
					
					Element table = tables.get(i);
					String tableString = table.text();
					if(tableString.contains("上网详单")&& tableString.contains("上网方式")){
						Elements trs = table.select("tr");
						String fixdate="";
						for(int j = 6 ; j<trs.size();j++){
							try{
							Elements tds = trs.get(j).select("td");
							if(tds.size()==1){
								fixdate=tds.get(0).text().trim();
							}else if(tds.size()==7){								
								
								String starttime = fixdate+" "+tds.get(0).text().trim();
								String allTotalFee = tds.get(6).text().trim();
								String area = tds.get(1).text().trim();
								String duration = tds.get(3).text();
								String accessPoint = tds.get(2).text().trim();
								String data = tds.get(4).text().replace("KB", "").trim();
								String cheapService = tds.get(5).text().trim();	
								
								Date times=null;
								try
								{
									
									times=DateUtils.StringToDate( starttime, "yyyy-MM-dd HH:mm:ss");
								}
								catch (Exception e) {
									e.printStackTrace();
								}
								
								if(duration.contains("分"))
								{							
									duration = duration.substring(0,duration.indexOf("分"));
									
									try{
										duration = Integer.parseInt(duration)*60+"";
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
								else if(duration.contains("秒"))
								{
									duration = duration.replace("秒","");
								}
								
								if(data.contains("."))
								{
									double d = Double.parseDouble(data);
									int dd = (int)d;
									data = dd+"";
								}
								
								long onlinetime=0;
								BigDecimal communicationFees=new BigDecimal("0.0");
								long totalFlow=0;
								
								try{		
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
									
									 try {
											MobileOnlineList onlineList = new MobileOnlineList();
											UUID uuid = UUID.randomUUID();
											onlineList.setId(uuid.toString());
											onlineList.setcTime(times);
											if(bean_List!=null){
												 if(bean_List.getcTime().getTime()<onlineList.getcTime().getTime()){
													onlineList.setCheapService(cheapService);
													onlineList.setCommunicationFees(communicationFees);
													onlineList.setOnlineTime(onlinetime);
													onlineList.setOnlineType(accessPoint);
													onlineList.setTotalFlow(totalFlow);
													onlineList.setTradeAddr(area);
													onlineList.setPhone(login.getLoginName());    	
													mobileOnlineListService.save(onlineList);
												 }
											 }else {
													onlineList.setCheapService(cheapService);
													onlineList.setCommunicationFees(communicationFees);
													onlineList.setOnlineTime(onlinetime);
													onlineList.setOnlineType(accessPoint);
													onlineList.setTotalFlow(totalFlow);
													onlineList.setTradeAddr(area);
													onlineList.setPhone(login.getLoginName());    	
													mobileOnlineListService.save(onlineList);
											 }
											
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
							}
							
						}
						catch(Exception e)
						{
							e.printStackTrace();	
						}
					}
				}
			}
			
			try
			{
				if(mobileOnlineList!=null&&mobileOnlineList.size()>0)
				{
					mobileOnlineListService.insertbatch(mobileOnlineList);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			}	
		return b;
	}
	
	public Map<String,Object> getSecImgUrl(){
		setImgUrl(imgurl2);//更换验证码地址二次验证
		map.put("url", getAuthcode());
		map.put(CommonConstant.status, 1);
		return map;
	}


	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
		CHeader h = new CHeader();
		h.setRespCharset("utf-8");
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("confirmCode", login.getAuthcode());
		param.put("contextPath", "/eMobile");
		param.put("fieldErrFlag","");
		param.put("menuid", "billdetails");
		param.put("randomSms", login.getPhoneCode());
		String text = cutil.post("http://www.sd.10086.cn/eMobile/checkSmsPass_commit.action",h,param);
		 if(text!=null){
			// System.out.println(text);
			if(text.contains("短信随机码不正确")){
				errorMsg= "短信随机码不正确，请重新输入";
			  }else if(text.contains("您输入的图片验证码不正确")){
				  errorMsg= "您输入的图片验证码不正确，请重新输入！";
			  }else if(text.contains("短信随机码已过期")){
				  errorMsg= "短信随机码已过期";
			  }else if(text.contains("http://www.sd.10086.cn/eMobile/queryBillDetail.action")){
				  errorMsg= "登陆成功!";
				  status = 1;
			  }else{
				  errorMsg = "验证失败!";
			  }
		    
		}else{
			errorMsg = "验证失败,请重试!";
		}	
		if(status==1){
	        addTask_2(this);
	    }
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		
		return map;	
	}

	/**
	 * 生成短信
	 * */
	public Map<String,Object> sendPhoneDynamicsCode() {
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		int status = 0;
	
		
		try {
			  String text = cutil.get("http://www.sd.10086.cn/eMobile/sendSms.action?menuid=billdetails&pageid=0.8326240054416375");
			 if(text.contains("您的短信随机码已经发送")){
				 errorMsg	 ="您的短信随机码已经发送，请注意查收";
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

	public void excute() {
		
	}
	

	
	public String login1() {
		String url = "https://sd.ac.10086.cn/portal/servlet/LoginServlet";
		Map<String,String> param = new HashMap<String,String>();
		param.put("mobileNum", login.getLoginName());
		param.put("servicePWD", login.getPassword());
		param.put("randCode",login.getAuthcode());
		param.put("submitMode", "2");
		param.put("logonMode", "1");
		param.put("FieldID", "1");
		param.put("ReturnURL", "www.sd.10086.cn/eMobile/jsp/common/prior.jsp");
		param.put("ErrorUrl", "../mainLogon.do");
		param.put("entrance", "IndexBrief");
		CHeader c = new CHeader();
		c.setRespCharset("utf-8");
		String text = cutil.post(url,c,param);
		if(text!=null){
			if(text.equals("0")){
				url = "http://sd.10086.cn/eMobile/jsp/common/prior.jsp";
				text = cutil.post(url,param);
				url = "http://sd.10086.cn/portal/servlet/CookieServlet?FieldID=2";
				text = cutil.get(url);
				if(text!=null){
					RegexPaserUtil rq = new RegexPaserUtil("a='","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String Attritd = rq.getText();
					if(Attritd!=null){
						url = "http://sd.10086.cn/eMobile/loginSSO.action?Attritd="+Attritd;
						text =  cutil.get(url);
						if(text.contains(login.getLoginName())&&text.contains("努力加载中.")){
							return "1";
						}
					}
					
				}
			}else{
				return  text;
			}
		}
		
		return null;
	}
	
	
	
	
	public BigDecimal getYue(){
		BigDecimal phoneremain= new BigDecimal("0.00");
		CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"www.sd.10086.cn",true);
		String text =  cutil.get("http://www.sd.10086.cn/eMobile/qryBalance_result.action?menuid=qryBalance&pageid=0.22554175904017448",h);
		if(text!=null && text.contains("话费余额")){
			
			try {
					RegexPaserUtil rp = new RegexPaserUtil("话费余额\",\"","元",text,RegexPaserUtil.TEXTEGEXANDNRT);
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
	
	
}
