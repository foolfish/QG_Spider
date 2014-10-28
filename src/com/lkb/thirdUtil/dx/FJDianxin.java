package com.lkb.thirdUtil.dx;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.controller.ds.TAOBAO_Controller;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class FJDianxin extends BaseInfoMobile{
	
	private static Logger logger = Logger.getLogger(FJDianxin.class);
	

	

	public String index = "https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=114.249.55.149,172.16.10.20";


	// 验证码图片路径
	public static String imgurl = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?";







	public static void main(String[] args) throws Exception {
		FJDianxin tj = new FJDianxin(new Login("18005012942","woshi123465"),null);
		tj.index();
		tj.inputCode(imgurl);
		tj.login();
		tj.close();
		
	}
	

	
	public FJDianxin(Login login,String currentUser) {
		super(login,ConstantNum.comm_fj_dx,currentUser);
	}
	
//	public Map<String,Object> index(){
//		map.put("url",getAuthcode());
//		return map;
//	}
	public void init(){
		try{
			if(!isInit()){
				String text = cutil.get(index);
				if(text!=null){
					Map<String,Object> jsmap = new HashMap<String, Object>();
					jsmap.put("indexText", text);
					String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
					CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"uam.ct10000.com");
					Map<String,String> param = new LinkedHashMap<String,String>();
					param.put("username", login.getLoginName()); 
				    text = cutil.post(url, h, param);
					String s[] = text.split("\\|");
					jsmap.put("shengfen_id", s[0]) ;
					jsmap.put("shengfen_name",s[1]) ;
				    redismap.put("jsmap", jsmap);
					setImgUrl(imgurl);	
					setInit();
				}
				
			}
		}catch(Exception e){
			logger.error("error",e);
		}
		
	}
	public  void shengfen_id() throws Exception{
		try{
			Map<String,Object> jsmap = new HashMap<String, Object>();
			String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
			CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"uam.ct10000.com");
			Map<String,String> param = new LinkedHashMap<String,String>();
			param.put("username", login.getLoginName()); 
			String  text = cutil.post(url, h, param);
			String s[] = text.split("\\|");
			jsmap.put("shengfen_id", s[0]) ;
			jsmap.put("shengfen_name",s[1]) ;
			if(s[0]!=null){
				 redismap.put("jsmap", jsmap);
			}else{
				throw new Exception();
			}
		}catch(Exception e){
			logger.error("error",e);
		}
	}
		
		
		
		


		/**
		 * 查询个人信息
		 */
		public void getMyInfo() {
				try {

					
					parseBegin( Constant.DIANXIN);
					
					CHeader h = new CHeader(CHeaderUtil.Accept_json,"",CHeaderUtil.Content_Type__urlencoded,"189.cn",true);
					String myinfo = cutil.get("http://189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo",h);
					Document doc3 = Jsoup.parse(myinfo);
					
					 String lxdh = doc3.select("input[id=phoneNumber]").first().attr("value");
					 String lxdz = doc3.select("textarea[id=address]").first().attr("value");
					 String realname = doc3.select("input[name=realName]").first().attr("value");
					 String idCard = doc3.select("input[name=certificateNumber]").first().attr("value");
					 String email = doc3.select("input[id=email]").first().attr("value");
					 
					Map<String, String> map = new HashMap<String, String>();
					map.put("parentId", currentUser);
					map.put("usersource", Constant.DIANXIN);
					map.put("loginName", login.getLoginName());
					List<User> list = userService.getUserByParentIdSource(map);
					if (list != null && list.size() > 0) {
						User user = list.get(0);
						user.setLoginName( login.getLoginName());
						user.setLoginPassword("");
						user.setUserName(realname);
						user.setRealName(realname);
						user.setIdcard(idCard);
						user.setAddr(lxdz);
						user.setUsersource(Constant.DIANXIN);
						user.setEmail(email);
						user.setParentId(currentUser);
						user.setModifyDate(new Date());
						user.setPhone( login.getLoginName());
						user.setFixphone(lxdh);
						user.setPhoneRemain(getYue());
						userService.update(user);
					} else {
						User user = new User();
						UUID uuid = UUID.randomUUID();
						user.setId(uuid.toString());
						user.setLoginName(login.getLoginName());
						user.setLoginPassword("");
						user.setUserName(realname);
						user.setRealName(realname);
						user.setIdcard(idCard);
						user.setAddr(lxdz);
						user.setUsersource(Constant.DIANXIN);
						user.setParentId(currentUser);
						user.setModifyDate(new Date());
						user.setPhone(login.getLoginName());
						user.setFixphone(lxdh);
						user.setPhoneRemain(getYue());
						user.setEmail(email);
						userService.saveUser(user);
					}
				
				} catch (Exception e) {
					errorMsg=e.getMessage();
					sendWarningCallHistory(errorMsg);
					logger.error("error",e);
				}finally{
					parseEnd(Constant.DIANXIN);
				}
		}
		
		public void getTelDetailHtml() {
		
			try {
				parseBegin( Constant.DIANXIN);
				boolean b = true;
				int num = 0;
				List<String> ms = DateUtils.getMonths(7,"yyyyMM");
				CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"fj.189.cn",true);
				
				for (int i = ms.size()-1; i>=0; i--) {
					String startDate = ms.get(i);
					
					if(i==0){
						//http://fj.189.cn/BillAjaxServlet.do
						//method=realtime&PRODNO=18005012942&PRODTYPE=50
						Map<String, String> parm = new LinkedMap();
						parm.put("PRODNO", login.getLoginName());
						parm.put("PRODTYPE", "50");
						parm.put("method","realtime");
						String text = cutil.post( "http://fj.189.cn/BillAjaxServlet.do",h,parm);
						b = getCurrent_parse(text, startDate);

					} else {
						String text =cutil.get("http://fj.189.cn/service/bill/custbill.jsp?ck_month="+startDate+"&citycode=591&PRODNO="+login.getLoginName()+"&PRODTYPE=50", h);
						b=getTelDetailHtml_parse(text, startDate);
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
				logger.error("error",e);
			}finally{
				parseEnd(Constant.DIANXIN);
			}
				
			
		
			
		}
		

		
	private boolean getCurrent_parse(String text, String startDate) {
		boolean b = true;

		Map map2 = new HashMap();
		map2.put("teleno", login.getLoginName());
		map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
		List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
		String allPay = "";
		try {
			JSONObject json = new JSONObject(text);
			// "SUMMENOY":"5.94",
			allPay = json.getString("SUMMENOY");

		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
			logger.error("error", e);
		}

		if (allPay.equals("")) {
			allPay = "0";
		}

		if (list2 != null && list2.size() > 0) {
			DianXinTel tel = (DianXinTel) list2.get(0);
			tel.setcAllPay(new BigDecimal(allPay));
			tel.setDependCycle(startDate.substring(0, 4) + "/"
					+ startDate.substring(4, 6) + "/01-"
					+ DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
			dianXinTelService.update(tel);
		} else {
			DianXinTel tel = new DianXinTel();
			UUID uuid = UUID.randomUUID();
			tel.setId(uuid.toString());
			// tel.setBaseUserId(currentUser);
			tel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
			tel.setTeleno(login.getLoginName());
			tel.setcAllPay(new BigDecimal(allPay));
			tel.setDependCycle(startDate.substring(0, 4) + "/"
					+ startDate.substring(4, 6) + "/01-"
					+ DateUtils.formatDate(new Date(), "yyyy/MM/dd"));
			dianXinTelService.saveDianXinTel(tel);
		}

		return b;
	}



		/**
		 * 查询话费记录
		 */
		public boolean getTelDetailHtml_parse(String text,String startDate) {
			boolean b = true;
			try {
				if(text.contains("客户名称")&&text.contains("费用提示")){
					Document doc6 = Jsoup.parse(text);
					
					Elements jbxx = doc6.select("div[class=lszd_zl]");
					Elements jbxxli = jbxx.select("li");
					String khmc="";
					String dependCycle="";
					for (int i = 0; i < jbxxli.size(); i++) {
						Element li = jbxxli.get(i);
						if(li.toString().contains("客户名称")){
							khmc= li.text().replaceAll("<span class=\"fw_b c0\">客户名称：</span>", "").replaceAll("客户名称：", "");
						}else if(li.toString().contains("账单周期")){
							dependCycle= li.text().replaceAll("<span class=\"fw_b c0\">账单周期：</span>", "").replaceAll("账单周期：", "");
						}					
					}
					Elements divs = doc6.select("div[class=fyxx_bot_a]");
					BigDecimal myf=new BigDecimal("0.00");
					BigDecimal ldxs=new BigDecimal("0.00");
					 for(int i = 1;i<divs.size();i++){ 
						 Element div =  divs.get(i);
						String tk = div.select("span[class=leftk]").first().text();
						try {
							if(tk.contains("漫游通话费")){
								String myfs = div.select("span[class=fr]").select("span").first().text();
								if(myfs!=null){
									myf=new BigDecimal(myfs);
								}
							}
						} catch (Exception e) {
							logger.error("error",e);
						}
						
						try {
							if(tk.contains("来电显示")){
								String ldxss = div.select("span[class=fr]").select("span").first().text();
								if(ldxss!=null){
									ldxs=new BigDecimal(ldxss);
								}
							}
						} catch (Exception e) {
							logger.error("error",e);
						}
						
					 }	
					 
					 String html = doc6.toString();
						RegexPaserUtil rp = new RegexPaserUtil("本期费用合计：","元",html,RegexPaserUtil.TEXTEGEXANDNRT);
						String total = rp.getText().trim();
						if(total.equals("")){
							total = "0";
						}
						
					 
					 
					 Map map2 = new HashMap();
						map2.put("teleno", login.getLoginName());
						map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));	
						List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
						if(list2!=null && list2.size()>0){
							DianXinTel tel = (DianXinTel)list2.get(0);
							tel.setcAllPay(new BigDecimal(total));
							dianXinTelService.update(tel);
						}else{
							DianXinTel tel = new DianXinTel();
							UUID uuid = UUID.randomUUID();
							tel.setId(uuid.toString());
//							tel.setBaseUserId(currentUser);
							tel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
							tel.setTeleno(login.getLoginName());
							tel.setcAllPay(new BigDecimal(total));
							tel.setDependCycle(dependCycle);
							tel.setcName(khmc);
							tel.setLdxsf(ldxs);
							tel.setMythf(myf);
							dianXinTelService.saveDianXinTel(tel);
						}
				
					
				} else {
					 Map map2 = new HashMap();
						map2.put("teleno", login.getLoginName());
						map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));	
					List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
					if(list2.size()==0){
						DianXinTel tel = new DianXinTel();
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
//						tel.setBaseUserId(currentUser);
						tel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
						tel.setTeleno(login.getLoginName());
						tel.setcAllPay(new BigDecimal(0));
						tel.setDependCycle(startDate.substring(0, 4) + "/" + startDate.substring(4, 6) + "/01-" + DateUtils.lastDayOfMonth(startDate, "yyyyMM", "yyyy/MM/dd"));
						dianXinTelService.saveDianXinTel(tel);
					}
				}
			} catch (Exception e) {
				 errorMsg = e.getMessage();
				 b = false;
				 logger.error("error",e);
			}
		
			return b;
		}

		/**文本解析
		 * true正常解析
		 * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
		 * type 市话/长途/港澳台/漫游
		 */
		public boolean  callHistory_parse(String text,DianXinDetail bean,String startDate){
			boolean b = true;
			try {
				if(text.contains("呼叫类型")&&text.contains("对方号码")){
					Document doc  = Jsoup.parse(text);
					 Elements tables = doc.select("table");
					 for(int i = 1;i<tables.size();i++){ 
						 Element table1 =  tables.get(i);
						 String tableString = table1.text();
						 if(tableString.contains("呼叫类型")&&tableString.contains("对方号码")&&!tableString.contains("暂无您所查询的数据清单")){
							 Elements trs = table1.select("tr");
							 if(trs.size()>1){
								Elements tdss= trs.get(1).select("td");
								 if(tdss.size()>5){
									 for (int j = 1; j < trs.size(); j++) {
											Elements tds =trs.get(j).select("td");
											String thsj= tds.get(1).text();
											String thsc= tds.get(2).text();
											String hjlx= tds.get(3).text();
											String dfhm= tds.get(4).text();
											String fy= tds.get(5).text();
											Map map2 = new HashMap();
											 map2.put("phone", login.getLoginName());
											 map2.put("cTime", DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
											 List list = dianXinDetailService.getDianXinDetailBypt(map2);
											if(list==null || list.size()==0){
												DianXinDetail dxDetail = new DianXinDetail();
												UUID uuid = UUID.randomUUID();
												dxDetail.setId(uuid.toString());
												dxDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
												int times = 0;
												try{
													TimeUtil tunit = new TimeUtil();
													times = tunit.timetoint(thsc);
												}catch(Exception e){
													logger.error("error",e);
												}
												dxDetail.setTradeTime(times);
												//dxDetail.setTradeAddr(thdd);
												//dxDetail.setTradeType(hjlx);
												dxDetail.setCallWay(hjlx);
												dxDetail.setRecevierPhone(dfhm);
												//dxDetail.setBasePay(new BigDecimal(jbthf));
												//dxDetail.setLongPay(new BigDecimal(ctthf));
												dxDetail.setAllPay(new BigDecimal(fy));
												dxDetail.setPhone(login.getLoginName());
											
												dianXinDetailService.saveDianXinDetail(dxDetail);	
											}
										 } 
								 }
							 }
							 
						}
					 }
					
				}
			} catch (Exception e) {
				 errorMsg = e.getMessage();
				 b = false;
				 logger.error("error",e);
			}
			return b;
		}
		
		/**
		 * 查询通话记录
		 */
		public void callHistory(){
			try {
				parseBegin( Constant.DIANXIN);
				List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
				CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"fj.189.cn",true);
				boolean b = false;
				int num = 0;
				
				Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
				DianXinDetail bean = new DianXinDetail(login.getLoginName());
				bean = dianXinDetailService.getMaxTime(bean);
				for (String startDate : ms) {
					
				
					String text =cutil.get("http://fj.189.cn/service/bill/result/mobile/mobile_call_result.jsp?v_choosetype=0&MONTH="+startDate+"&PRODTYPE=50&PRODNO=MTgwMDUwMTI5NDI=&PAGENO=1&INTERPAGE=10000", h);
					if(text!=null){
						b= callHistory_parse(text,bean,startDate);
						if(!b){
							//异常信息
							if(errorMsg!=null){
								num ++;
										//超过五次,发送错误信息,123
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
				logger.error("error",e);
			}finally{
				parseEnd(Constant.DIANXIN);
			}
		}

		
		/** 0,采集全部
		 * 	1.采集手机验证
		 *  2.采集手机已经验证
		 * @param type
		 */
		public  void startSpider(){
			int type = login.getType();
			getCityCode();
			switch (type) {
			case 1:
				getTelDetailHtml();//账单详情
				getMyInfo(); //个人信息
				break;
			case 2:
				
				callHistory(); //历史账单
				getMessage();
				getFlow();	//流量
				break;
			case 3:
				getTelDetailHtml();//账单详情
				getMyInfo(); //个人信息
				callHistory(); //历史账单
				getMessage();
				getFlow();	//流量
				break;
			default:
				break;
			}
		}
		
		
		/** created by qian
		 * 查询短信记录
		 */
		public void getMessage(){
			try {
				parseBegin( Constant.DIANXIN);
				List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
				CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"fj.189.cn",true);
				boolean b = false;
				int num = 0;
				
				TelcomMessage bean = new TelcomMessage();
				bean.setPhone(login.getLoginName());
				bean = telcomMessageService.getMaxSentTime(bean.getPhone());
				for (String startDate : ms) {
					
					String text =cutil.get("http://fj.189.cn/service/bill/result/mobile/mobile_call_result.jsp?SDAY=1&v_choosetype=3&EDAY=30&MONTH="+startDate+"&PRODTYPE=50&PRODNO=MTgwMDUwMTI5NDI=&PAGENO=1&INTERPAGE=10000", h);
					/*System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
					System.out.println(text);*/
					
					if(text!=null){
						b= message_parse(text,bean,startDate);
						if(!b){
							//异常信息
							if(errorMsg!=null){
								num ++;
										//超过五次,发送错误信息,123
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
					
				}
			} catch (Exception e) {
				logger.error("error",e);
			}finally{
				parseEnd(Constant.DIANXIN);
			}
		}

		/*解析短信内容*/
		
		public boolean  message_parse(String text, TelcomMessage bean,String startDate){
			boolean b = true;
			try {
				if(text.contains("短信清单")&&!text.contains("暂无您所查询的数据清单")){
					Document doc  = Jsoup.parse(text);
					 Elements tables = doc.select("div[class=jtqd_table2]");
					 Elements table = tables.select("table");
					 Elements trs = table.select("tr");
					 int trsSize = trs.size();
					 if(trsSize > 1){
						 for( int i = 1; i < trsSize; i++){
							Elements tds = trs.get(i).select("td");
							String type = tds.get(1).text();
							String sendTime = tds.get(2).text();
							String sendPhone = tds.get(3).text();
							String recevierPhone = tds.get(4).text();
							String method = tds.get(5).text();
							String fee = tds.get(6).text();
							
							/*System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
							System.out.println(tds+"&"+type+"&"+sendTime+"&"+sendPhone+"&"+recevierPhone+"&"+method+"&"+fee);*/
							
							/*Map map2 = new HashMap();
							map2.put("phone", login.getLoginName());
							map2.put("cTime", DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
							List list = telcomMessageService.getByPhone(map2);*/
							
							TelcomMessage telMessage = new TelcomMessage();
							UUID uuid = UUID.randomUUID();
							telMessage.setId(uuid.toString());
							telMessage.setAllPay(new Double(fee));
							telMessage.setBusinessType(method);
							telMessage.setCreateTs(new Date());
							telMessage.setPhone(sendPhone);
							telMessage.setRecevierPhone(recevierPhone);
							telMessage.setSentTime(DateUtils.StringToDate(sendTime, "yyyy-MM-dd HH:mm:ss"));
							
						    if(bean!=null&&bean.getSentTime()!=null){
						    	if(bean.getSentTime().getTime()>=telMessage.getSentTime().getTime()){
						    		break;
						    	}
						    }

							
							telcomMessageService.save(telMessage);
						 }
					 }				
				}
			} catch (Exception e) {
				 errorMsg = e.getMessage();
				 b = false;
				 logger.error("error",e);
			}
			return b;
		}
		

		// 首页登录
		public Map<String,Object> login() {
			String result = login1();
			if(result!=null&& result.equals("1")){
				loginsuccess();
			}
			
			if(status==1){
				sendPhoneDynamicsCode();
				addTask_1(this);
	    	}
			return map;
		}
		
		
		public  String getSessionId(){
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			String reqUrl = "http://sc.189.cn/service/bill/querynew.jsp";
			CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"sc.189.cn");
			String text = cutil.get(reqUrl, h);
			String sessionid ="";
			if(text!=null && text.contains("sessionid")){
				Document doc1 = Jsoup.parse(text);
				sessionid = doc1.select("input[id=sessionid]").first().attr("value");
				jsmap.put("sessionid", sessionid);
				//redismap.put("jsmap", map);//根据实际需要存放
			}
			return null;
		}
		

		// 随机短信登录
		public Map<String,Object> checkPhoneDynamicsCode() {
			try {
				Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
				CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"fj.189.cn",false);
				Map<String, String> parm = new LinkedMap();
				parm.put("CITYCODE", "0591");
				parm.put("MONTH",DateUtils.formatDate(new Date(),"yyyyMM"));
				parm.put("PRODNO", login.getLoginName());
				parm.put("PRODTYPE", "50");
				parm.put("PURID", "0");
				parm.put("SELTYPE", "1");
				parm.put("randomPwd", login.getPhoneCode());
				parm.put("serPwd50",jsmap.get("loginPwd").toString());
				String text = cutil.post( "http://fj.189.cn/service/bill/trans.jsp",h,parm);
				if(text!=null &&text.contains("手机通话清单")){
					status = 1;	
				}else{
					errorMsg = "您的短信随机码不正确或已失效，请重新获取！您已输错1次，连续输错10次，将暂停查询详单24小时，请慎重输入。";
				}
			} catch (Exception e) {
				logger.error("error",e);
			}
			map.put(CommonConstant.status, status);
			map.put(CommonConstant.errorMsg, errorMsg);
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
			int status = 0;
			try {
			
				CHeader	h = new CHeader(CHeaderUtil.Accept_,null,CHeaderUtil.x_requested_with,"fj.189.cn",true);
				String xmls= "<buffalo-call><method>getCDMASmsCode</method><map><type>java.util.HashMap</type><string>PHONENUM</string>";
				xmls+="<string>"+login.getLoginName()+"</string><string>PRODUCTID</string><string>50</string><string>CITYCODE</string>";
				xmls+="<string>0591</string><string>I_ISLIMIT</string><string>1</string><string>QUERYTYPE</string><string>BILL</string></map></buffalo-call>";   
				String text = cutil.post("http://fj.189.cn/BUFFALO/buffalo/QueryAllAjax", h, xmls);
				
				if(text!=null && text.contains("短信随机密码已经发到您的手机")){
					errorMsg="尊敬的客户,您好!短信随机密码已经发到您的手机,请查收!未退出登录情况下30分钟内有效";
					status = 1;
				}else if(text!=null&&text.contains("短信发送过于频繁")){
					errorMsg="短信发送过于频繁";
				}
			} catch (Exception e) {
				logger.error("error",e);
			}
			if(errorMsg==null){
				errorMsg = "发送失败!";
			}
			map.put(CommonConstant.status,status);
			map.put(CommonConstant.errorMsg,errorMsg);
			return map;
		}

		
		public BigDecimal getYue(){
			BigDecimal phoneremain= new BigDecimal("0");
			try {
				CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"fj.189.cn",false);
				Map<String, String>  parm= new LinkedMap();
				parm.put("method", "payFee");
				String text = cutil.post("http://fj.189.cn/AjaxServlet.do", h, parm);
				if(text!=null){
					JSONObject json=new JSONObject(text);
					String jphoneremain = json.getString("PAYFEE");
					if(jphoneremain!=null){
						 phoneremain = new BigDecimal(jphoneremain);
					}
				}
			} catch (Exception e) {
				logger.error("error",e);
			}
		return phoneremain;
			
		}
		
	

	/**第一次post*/
	public String login1(){
		Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String text = jsmap.get("indexText").toString();
	
		 if(text.length()<300){
				//此处是特殊情况处理,主要是redis有记录,或者cookie有记录,会有出现此处连接的地方,需要注意
				  //System.out.println(text);
				  if(text.contains("location.replace(")){
					  return "1";
				  }
			 }else{
				  Document doc = Jsoup.parse(text);
					String empoent = doc.select("input[id=forbidpass]").first().val();
					String forbidaccounts = doc.select("input[id=forbidaccounts]").first().val();
					String authtype = doc.select("input[name=authtype]").first().val();
					String customFileld02 = jsmap.get("shengfen_id").toString();
					String areaname = jsmap.get("shengfen_name").toString();
					String customFileld01 = "3";
					String lt = doc.select("input[name=lt]").first().val();
					String _eventId = doc.select("input[name=_eventId]").first().val();
					String open_no = "c2000004";
					String action = doc.select("form[id=c2000004]").first().attr("action");
					action = URLDecoder.decode(action);
					
					String phone1 = null;
					String password1 = null;
					//-----------加密 ,天津
					try{
						String n = StringUtil.subStr("strEnc(username,", ");", text).trim();
						String[] stra = n.trim().replaceAll("\'", "").split(",");
						phone1 = AbstractDianXinCrawler.executeJsFunc("des/tel_com_des.js", "strEnc", login.getLoginName(), stra[0], stra[1], stra[2]);
						password1 =AbstractDianXinCrawler.executeJsFunc("des/tel_com_des.js", "strEnc", login.getPassword(), stra[0], stra[1], stra[2]);
					}catch(Exception e){
						phone1 = login.getLoginName();
						password1 =login.getPassword();
						logger.error("error",e);
					}
					String url = "https://uam.ct10000.com"+action;
					CHeader h = new CHeader(CHeaderUtil.Accept_,"https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=114.249.55.149",CHeaderUtil.Content_Type__urlencoded,"uam.ct10000.com",false);
					Map<String,String> param = new LinkedHashMap<String,String>();
					param.put("_eventId", _eventId); 
					param.put("areaname",areaname); 
					param.put("authtype",authtype); 
					param.put("c2000004RmbMe", "on"); 
					param.put("customFileld01", customFileld01); 
					param.put("customFileld02", customFileld02); 
					param.put("forbidaccounts", "null"); 
					param.put("forbidpass", null); 
					param.put("lt", lt); 
					param.put("open_no",open_no); 
					param.put("password", password1); 
					param.put("randomId", login.getAuthcode()); 
					param.put("username", phone1); 
					text = cutil.post(url, h, param);
					//System.out.println(text);
					if(text!=null){
						if(text.length()<300){
							if(text.contains("https://uam.ct10000.com")){
								return login2(text, param,h);
							}
						}else{
							doc = Jsoup.parse(text);
							Element el = doc.getElementById("status2");
							if(el!=null){
								errorMsg = el.text();
							}
						}
						
					}
			  }
		return null;
	}
	
	
	public  String login2(String url,Map<String,String> param,CHeader h){
	
		String  text = cutil.post(url, h, param);
		if(text!=null && text.contains("location.replace")){
			return login3(text);
		}
		 return null;
	}
	
	/**第三次post*/
	public  String login3(String text){
		RegexPaserUtil rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String url = rp.getText();
		CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"189.cn",false);
		CHeader h1 = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"fj.189.cn",false);
		text = cutil.get(url, h);
		if(text!=null){
			text = cutil.get("http://fj.189.cn/service/bill/realtime.jsp",h1);
			rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
			url = rp.getText();
			text = cutil.get(url);
			rp = new RegexPaserUtil("parent.location.href ='","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
			url = rp.getText();
			text =cutil.get("http://fj.189.cn"+url,h1);
	
			if(text!=null){
				text =cutil.get( "http://fj.189.cn/service/bill/realtime.jsp", h1); //200请求
				if(text!=null && text.contains(login.getLoginName())){
					Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
					jsmap.put("loginPwd", login.getPassword());
					return "1";
				}
			}
		}
		return null;
	}
	
	/**
	* <p>Title: getFlow</p>
	* <p>Description: 抓取流量</p>
	* @author Jerry Sun
	*/
	private void getFlow(){
		List<String> months = DateUtils.getMonths(6, "yyyyMM");
		for (int i=months.size()-1;i>=0;i--) {
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			Map<String, String> param = new LinkedMap();
			String url = "http://fj.189.cn/service/bill/trans.jsp";
			param.put("PRODNO", login.getLoginName());
			param.put("PRODTYPE", login.getProductId());
			param.put("SELTYPE", "2");
			param.put("MONTH", months.get(i));
			param.put("PURID", "0");
			param.put("CITYCODE", login.getCityCode());
			param.put("NUMCOUNT", "1");
			param.put("TEMPNUM", login.getLoginName());
			param.put("TEMPNUM", login.getProductId());
//			param.put("serPwd50", jsmap.get("loginPwd").toString());
//			param.put("randomPwd", login.getPhoneCode());
			String text = cutil.post(url , param);
			if(text != null){
				if(text.contains("暂无您所查询的数据清单"))
					continue;
				else
					parseFlowText(text, months.get(i));
			}else{
				logger.error("福建电信流量抓取出错!");
			}
		}
		
		
	}
	
	/**
	* <p>Title: parseFlowText</p>
	* <p>Description: 流量数据解析</p>
	* @author Jerry Sun
	* @param text
	*/
	private void parseFlowText(String text, String month){
		Document doc = Jsoup.parse(text);
		Elements es_table1 = doc.getElementsByClass("jtqd_table1");
		int pageCount = 0;
		if(es_table1.size()>0){
			DianXinFlow bean = new DianXinFlow();
			bean = dianXinFlowService.getMaxFlowTime(login.getLoginName());
			List<DianXinFlow> dxFlowList = new ArrayList<DianXinFlow>();
			for (Element table : es_table1) {
				DianXinFlow dxFlow = new DianXinFlow();
				String content = table.text();
				String phone = StringUtil.subStr("客户号码：", "起止日期", content).trim();
				String dependCycle = StringUtil.subStr("起止日期：", "查询日期", content).trim();
				String allFlow = StringUtil.subStr("总流量：", "总时长", content).trim();
				String allTime = StringUtil.subStr("总时长：", "总费用", content).trim();
				String allPay = StringUtil.subStr("总费用：", "（元）", content).trim();
				
				
				dxFlow.setPhone(phone);
				dxFlow.setDependCycle(dependCycle);
				dxFlow.setQueryMonth(DateUtils.StringToDate(month, "yyyyMM"));
				dxFlow.setAllFlow(new BigDecimal(StringUtil.flowFormat(allFlow)));
				dxFlow.setAllTime(new BigDecimal(StringUtil.flowTimeFormat(allTime)));
				dxFlow.setAllPay(new BigDecimal(allPay));
				
				
				if(dxFlow != null){
					if(bean!=null&&bean.getQueryMonth()!=null){
				    	if(bean.getQueryMonth().getTime()>dxFlow.getQueryMonth().getTime()){
				    		break;
				    	}else if(bean.getQueryMonth().getTime()==dxFlow.getQueryMonth().getTime()){
				    		dianXinFlowService.update(dxFlow);
				    	}else{
				    		UUID id = UUID.randomUUID();
				    		dxFlow.setId(id.toString());
				    		dianXinFlowService.saveDianXinFlow(dxFlow);
				    	}
				    }else{
				    	UUID id = UUID.randomUUID();
			    		dxFlow.setId(id.toString());
			    		dianXinFlowService.saveDianXinFlow(dxFlow);
				    }
					
				}
			}
//			dianXinFlowService.insertbatch(dxFlowList);
		}else{
			logger.error("福建电信流量账单解析出错");
		}
		
		Elements es_table2 = doc.getElementsByClass("jtqd_table2");
		if(es_table2.size()>0){
			parseFlowDetail(es_table2);
			pageCount = Integer.parseInt(StringUtil.subStr("</a>共", "页&nbsp", text));
			
			//下一页
			if(pageCount>1){
				Elements yeshus = doc.getElementsByClass("yeshu");
				String nextPage = "http://fj.189.cn";
				for (Element yeshu : yeshus) {
					Elements as = yeshu.getElementsByTag("a");
					for (Element a : as) {
						if(("下一页").equals(a.text())){
							nextPage = nextPage + a.attr("href");
							break;
						}
					}
					break;
				}
				for(int p=2;p<=pageCount;p++){
					String temp = cutil.get(nextPage);
					if(temp == null)
						return;
					Elements temp_element = Jsoup.parse(temp).getElementsByClass("jtqd_table2");
					if(temp_element.size()>0)
						parseFlowDetail(temp_element);
				}
				
			}
		}else{
			logger.error("福建电信流量详单解析出错");
		}
	}



	private void parseFlowDetail(Elements es_table2) {
		//解析流量详单
		for (Element table : es_table2) {
			Elements trs = table.getElementsByTag("tr");
			if(trs.size()>0){
				DianXinFlowDetail bean = new DianXinFlowDetail();
				bean.setPhone(login.getLoginName());
				bean = dianXinFlowDetailService.getMaxTime(bean);
				List<DianXinFlowDetail> list = new ArrayList<DianXinFlowDetail>();
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
				String today = DateUtils.getToday("yyyy-MM");
				for(int i=1;i<trs.size();i++){
					Elements tds = trs.get(i).getElementsByTag("td");
					if(tds.size()>0){
						DianXinFlowDetail dxfd = new DianXinFlowDetail();
						for(int j=1;j<tds.size();j++){
							String temp = tds.get(j).text();
							switch (j) {
							case 1:
								dxfd.setBeginTime(DateUtils.StringToDate(temp, "yyyy-MM-dd hh:mm:ss"));
								break;
							case 2:
								dxfd.setTradeTime(StringUtil.flowTimeFormat(temp));
								break;
							case 3:
								dxfd.setFlow(new BigDecimal(StringUtil.flowFormat(temp)));
								break;
							case 4:
								dxfd.setNetType(temp);
								break;
							case 5:
								dxfd.setLocation(temp);
								break;
							case 6:
								dxfd.setBusiness(temp);
								break;
							case 7:
								dxfd.setFee(new BigDecimal(temp));
								break;
							}
						}
						dxfd.setPhone(login.getLoginName());
						
						if(dxfd != null){
							if(bean!=null&&bean.getBeginTime()!=null){
						    	if(bean.getBeginTime().getTime()>dxfd.getBeginTime().getTime()){
						    		break;
						    	}else if(bean.getBeginTime().getTime()>=dxfd.getBeginTime().getTime()){
						    		break;
						    	}
						    }
						}
						String beginTime = sdf.format(dxfd.getBeginTime());
						if(DateUtils.isSameMonth(today, beginTime))
							dxfd.setIscm(1);
						dxfd.setId(UUID.randomUUID().toString());
						list.add(dxfd);
					}
				}
				dianXinFlowDetailService.insertbatch(list);
			}
		}
	}
	
	/**
	* <p>Title: getCityCode</p>
	* <p>Description: 获取地区编码</p>
	* @author Jerry Sun
	*/
	private void getCityCode(){
		Map<String, String> param = new LinkedMap();
		param.put("method", "getCityCodeAndIsLogin");
		CHeader header = new CHeader();
		String text = cutil.post("http://fj.189.cn/ajaxServlet/getCityCodeAndIsLogin", header, param);
		if(text != null){
			if(text.contains("ISLOGIN\":\"true")){
				String cityCode = StringUtil.subStr("CITYCODE\":\"", "\",", text);
				String productId = StringUtil.subStr("PRODUCTID\":\"", "\",", text);
				login.setCityCode(cityCode);
				login.setProductId(productId);
			}
		}
	}
	

}
