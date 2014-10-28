package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringEscapeUtils;
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

/**
 * 浙江移动 Description:
 * 
 * @author daixun
 * @version 1.0
 * @created 2014-6-22 下午02:34:41
 */
public class ZJYidong extends BaseInfoMobile {

	public String index = "https://zj.ac.10086.cn/login";
	// 验证码图片路径
	public static String imgurl = "https://zj.ac.10086.cn/ImgDisp";

	public ZJYidong(Login login, String currentUser) {
		super(login, ConstantNum.comm_zj_yidong, currentUser);
	}

	// public Map<String,Object> index(){
	// map.put("url",getAuthcode());
	// return map;
	// }
	public void init() {
		if (!isInit()) {
			String text = cutil.get(index);
			if (text != null) {
				setImgUrl(imgurl);
				setInit();
				redismap.put("jsmap", map);// 根据实际需要存放
			}
		}
	}

	/**
	 * 0,采集全部 1.采集手机验证 2.采集手机已经验证
	 * 
	 * @param type
	 */
	public void startSpider() {
		int type = login.getType();
		try {
			parseBegin(Constant.YIDONG);
			switch (type) {
			case 1:
				getTelDetailHtml();// 通话记录
				getMyInfo(); // 个人信息
				break;
			case 2:
				callHistory(); // 历史账单
				getSmsLog();// 短信
				getLiuliang();//流量
				break;
			case 3:
				getTelDetailHtml();// 通话记录
				getMyInfo(); // 个人信息
				callHistory(); // 历史账单
				getSmsLog();// 短信
				getLiuliang();//流量
				break;
			default:
				break;
			}
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	/**
	 * 查询及保存短信记录
	 */
	public void getSmsLog() {
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms = DateUtils.getMonths(6, "MM=yyyy");
			CHeader h = new CHeader(CHeaderUtil.Accept_,
					"http://service.zj.10086.cn",
					CHeaderUtil.Content_Type__urlencoded,
					"service.zj.10086.cn", true);
			boolean b = false;
			int num = 0;
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			Date lastTime = null;
			lastTime = null;
			try {
				if (mobileMessageService.getMaxSentTime(login.getLoginName()) != null)
					lastTime = mobileMessageService.getMaxSentTime(
							login.getLoginName()).getSentTime();
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (int i = ms.size()-1; i >= 0; i--) {
				String startDate = (String) ms.get(i);
				
				String text = "";
				if (i == 0) {
					Map<String, String> parms = new LinkedMap();
					parms.put("listtype", "2");
					parms.put("validateCode", login.getPhoneCode());
					text = cutil.post(
							"http://service.zj.10086.cn/yw/detailbill/queryDetailBill.do?bid="
									+ jsmap.get("xdId").toString(), h, parms);
				} else {
					Map<String, String> parms = new LinkedMap();
					parms.put("listtype", "2");
					parms.put("month", startDate);
					parms.put("validateCode", login.getPhoneCode());
					text = cutil.post(
							"http://service.zj.10086.cn/yw/detailbill/nowQueryDetailBill.do?bid="
									+ jsmap.get("xdId").toString(), h, parms);
				}
				text = StringEscapeUtils.unescapeHtml4(text);
				SmsLog_parse(text, startDate,lastTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	/**
	 * 
	 * */
	public void SmsLog_parse(String text, String startDate,Date lastTime) {
		if (text != null && text.contains("起始时间") && text.contains("对方号码")) {
			List<MobileMessage> messageList=new ArrayList<MobileMessage>();
			Document doc = Jsoup.parse(text);
			Elements trs = doc.select("tr[class=content2]");
			for (int j = 0; j < trs.size(); j++) {
				try {
					Elements tds = trs.get(j).select("td[class=talbecontent1]");
					if (tds.size() == 10) {
						String sentTime = tds.get(1).text()
								.replace("&nbsp;", "").trim();
						String tradeWay = tds.get(3).text().trim()
								.replace("&nbsp;", "").trim();
						String recevierPhone = tds.get(5).text().trim()
								.replace("&nbsp;", "").trim();
						String sentAddr = tds.get(6).text().trim()
								.replace("&nbsp;", "").trim();
						String allPay = tds.get(8).text().trim()
								.replace("&nbsp;", "").trim();


						// 解析的各个属性后面总会奇怪的带一个？字符，需要把它去掉，否则日期与费用无法转换
						sentTime = sentTime.substring(0, sentTime.length() - 1);
						allPay = allPay.substring(0, allPay.length() - 1);
						tradeWay = tradeWay.substring(0, tradeWay.length() - 1);
						recevierPhone = recevierPhone.substring(0,
								recevierPhone.length() - 1);
						sentAddr = sentAddr.substring(0, sentAddr.length() - 1);

						Date times = null;
						try {
							times = DateUtils.StringToDate(sentTime,
									"yyyy-MM-dd HH:mm:ss");
							if (lastTime != null && times != null) {
								if (times.getTime() <= lastTime.getTime()) {
									continue;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						MobileMessage message = new MobileMessage();
						try {
							message.setAllPay(new BigDecimal(allPay));
						} catch (Exception e) {
							e.printStackTrace();
						}
						message.setRecevierPhone(recevierPhone);
						message.setSentAddr(sentAddr);
						message.setSentTime(times);
						if (tradeWay.contains("收")||tradeWay.contains("接"))
						{
							tradeWay = "接收";
						}
						else
						{
							tradeWay = "发送";
						}
						message.setTradeWay(tradeWay);
						message.setPhone(login.getLoginName());
						message.setCreateTs(new Date());
						UUID uuid = UUID.randomUUID();
						message.setId(uuid.toString());
						//mobileMessageService.save(message);
						messageList.add(message);					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			saveMobileMessage(messageList);
		}
	}

	public void getLiuliang()
	{
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms = DateUtils.getMonths(6, "MM=yyyy");
			CHeader h = new CHeader(CHeaderUtil.Accept_,
					"http://service.zj.10086.cn",
					CHeaderUtil.Content_Type__urlencoded,
					"service.zj.10086.cn", true);
			boolean b = false;
			int num = 0;
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");

			Date lasttime=null;
			if(mobileOnlineListService.getMaxTime(login.getLoginName())!=null)
			{
				lasttime = mobileOnlineListService.getMaxTime(login.getLoginName()).getcTime();
			}
			
			MobileOnlineBill bean_bill = mobileOnlineBillService.getMaxTime(login.getLoginName());
			
			for (int i = ms.size()-1; i >= 0; i--) {
				String startDate = (String) ms.get(i);

				
				String text = "";
				if (i == 0) {
					Map<String, String> parms = new LinkedMap();
					parms.put("listtype", "8");
					parms.put("validateCode", login.getPhoneCode());
					text = cutil.post(
							"http://service.zj.10086.cn/yw/detailbill/queryDetailBill.do?bid="
									+ jsmap.get("xdId").toString(), h, parms);
				} else {
					Map<String, String> parms = new LinkedMap();
					parms.put("listtype", "8");
					parms.put("month", startDate);
					parms.put("validateCode", login.getPhoneCode());
					text = cutil.post(
							"http://service.zj.10086.cn/yw/detailbill/nowQueryDetailBill.do?bid="
									+ jsmap.get("xdId").toString(), h, parms);
				}
				text = StringEscapeUtils.unescapeHtml4(text);
				String month = startDate.substring(3, 7) + startDate.substring(0, 2);
				try {
					parseFlowBill(text, month, bean_bill,i);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Liuliang_parse(text, startDate,lasttime);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}
	
	private boolean parseFlowBill(String text, String months, MobileOnlineBill bean_bill,int iscm1) {
	try {
		if(text.contains("移动数据流量")){
			Document doc = Jsoup.parse(text);
			Element table = doc.select("table").get(4);
						/**
						 * freeFlow
						 * chargeFlow
						 * 没有
						 */
						Date monthly = null;
						long totalFlow = 0;
//						long freeFlow = 0;
//						long chargeFlow = 0;
						int iscm = 0;
						String year = months.substring(0,4);
						String mon = months.substring(4,6);
						BigDecimal trafficCharges = new BigDecimal(0);
						try {
							monthly = DateUtils.StringToDate(year+"-"+mon,"yyyy-MM");
							
							String totalFlowStr = table.select("tr:eq(1)>td:eq(1)").text().replace(" ", "").toUpperCase();
							totalFlow = Math.round(StringUtil.flowFormat(totalFlowStr));
							String totalFees = table.select("tr:eq(4)>td:eq(1)").text();
							trafficCharges = new BigDecimal(totalFees);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Map<String,Object> map = new LinkedHashMap<String, Object>();
						map.put("phone", login.getLoginName());
						map.put("monthly", monthly);
					    List<Map> list = mobileOnlineBillService.getMobileOnlineBillByphone(map);
				   	    if(list==null || list.size()==0){
						MobileOnlineBill onlineBill = new MobileOnlineBill();
						UUID uuid = UUID.randomUUID();
						onlineBill.setId(uuid.toString());
			        	if(bean_bill!=null){
							 if(bean_bill.getMonthly().getTime()>=onlineBill.getMonthly().getTime()){
								return false;
							 }
						 }
			        	onlineBill.setMonthly(monthly);
			        	onlineBill.setTotalFlow(totalFlow);
			        	try {
							onlineBill.setTrafficCharges(trafficCharges);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	onlineBill.setPhone(login.getLoginName());    
			        	if(iscm1==0) { //当前月
			        		iscm = 1;
			        	}
			        	onlineBill.setIscm(iscm);
						mobileOnlineBillService.save(onlineBill);
				   	}
				}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 return false;
		}
		return true;
	}
	
	
	
	public void Liuliang_parse(String text, String startDate,Date lasttime)
	{
		if (text != null && text.contains("移动数据流量或wlan详单")) {
			Document doc = Jsoup.parse(text);
			Elements trs = doc.select("tr[class=content2]");
			List<MobileOnlineList> mobileOnlineList = new LinkedList<MobileOnlineList>();
			for (int j = 0; j < trs.size(); j++) {
				try {
					Elements tds = trs.get(j).select("td[class=talbecontent1]");
					if (tds.size() == 10) {
											
						String starttime = tds.get(1).text().replace("&nbsp;", "").trim();
						String allTotalFee = tds.get(8).text().replace("&nbsp;", "").trim();
						String area = tds.get(5).text().replace("&nbsp;", "").trim();
						String duration = tds.get(7).text().replace("&nbsp;", "").trim();
						String accessPoint = tds.get(2).text().replace("&nbsp;", "").trim();
						String data = tds.get(6).text().replace("&nbsp;", "").trim();
						String cheapService = tds.get(3).text().replace("&nbsp;", "").trim();
						
						//去掉后面带的？尾巴
						starttime = starttime.substring(0, starttime.length()-1);
						allTotalFee = allTotalFee.substring(0, allTotalFee.length()-1);
						area = area.substring(0, area.length()-1);
						duration = duration.substring(0, duration.length()-1);
						accessPoint = accessPoint.substring(0, accessPoint.length()-1);
						data = data.substring(0, data.length()-1);
						cheapService = cheapService.substring(0, cheapService.length()-1);
						
						if(data.contains("mb"))
						{
						   String[] b = data.split(" ");
						   String mb = b[0];
						   String kb = b[1];
						   try{
							   mb = mb.replace("mb", "").trim();
							   kb = kb.replace("kb", "").trim();
							   
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
							data = data.replace("kb", "");
						}
						
						long onlinetime=0;
						BigDecimal communicationFees=new BigDecimal("0.0");;
						long totalFlow=0;
						Date times=null;
						
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
							times = DateUtils.StringToDate(starttime,
									"yyyy-MM-dd HH:mm:ss");
						}
						catch (Exception e) {
							e.printStackTrace();
						}	
						
						MobileOnlineList datalist = new MobileOnlineList();						
						UUID uuid = UUID.randomUUID();
						datalist.setId(uuid.toString());
						datalist.setOnlineTime(onlinetime);
						datalist.setPhone(login.getLoginName());
						datalist.setOnlineType(accessPoint);
						datalist.setTotalFlow(totalFlow);
						datalist.setcTime(times);
						datalist.setTradeAddr(area);
						datalist.setCommunicationFees(communicationFees);
						datalist.setCheapService(cheapService);
						
						if(datalist!=null)
						{
							mobileOnlineList.add(datalist);	
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			
			try
			{
				saveMobileOnlineList(mobileOnlineList);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询通话记录
	 */
	public void callHistory() {
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms = DateUtils.getMonths(6, "MM=yyyy");
			CHeader h = new CHeader(CHeaderUtil.Accept_,
					"http://service.zj.10086.cn",
					CHeaderUtil.Content_Type__urlencoded,
					"service.zj.10086.cn", true);
			boolean b = false;
			int num = 0;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");

			for (int i = ms.size()-1; i >= 0; i--) {
				String startDate = (String) ms.get(i);

				String text = "";
				if (i == 0) {
					text = cutil.get(
							"http://service.zj.10086.cn/yw/detailbill/queryDetailBill.do?bid="
									+ jsmap.get("dyxdId").toString()
									+ "&listtype=1", h);
				} else {
					Map<String, String> parms = new LinkedMap();
					parms.put("listtype", "1");
					parms.put("month", startDate);
					parms.put("validateCode", login.getPhoneCode());
					text = cutil.post(
							"http://service.zj.10086.cn/yw/detailbill/nowQueryDetailBill.do?bid="
									+ jsmap.get("xdId").toString(), h, parms);
				}
				b = callHistory_parse(text, bean, startDate);
				if (!b) {
					// 异常信息
					if (errorMsg != null) {
						num++;
						// 超过五次,发送错误信息,
						if (num > 5) {
							// 发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						// 错误
						errorMsg = null;
					} else {
						break;// 数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parseEnd(Constant.YIDONG);
		}

	}

	public boolean callHistory_parse(String text, MobileDetail bean,
			String startDate) {
		boolean b = true;
		try {
			if (text.contains("起始时间") && text.contains("通话时长")) {
				List<MobileDetail> detailList = new ArrayList<MobileDetail>();
				Document doc = Jsoup.parse(text);
				System.out.println(doc.toString());

				Elements trs = doc.select("tr[class=content2]");
				for (int j = 1; j < trs.size(); j++) {
					Elements tds = trs.get(j).select("td");
					if (tds.size() > 13) {
						String thsj = tds.get(1).text().trim()
								.replaceAll(" ", "");
						String thsc = tds.get(2).text().trim()
								.replaceAll(" ", "");
						String thzt = tds.get(3).text().trim()
								.replaceAll(" ", "");
						String thlx = tds.get(4).text().trim()
								.replaceAll(" ", "");
						String dfhm = tds.get(5).text().trim()
								.replaceAll(" ", "");
						String dfhmlx = tds.get(6).text().trim()
								.replaceAll(" ", "");
						String thdd = tds.get(7).text().trim()
								.replaceAll(" ", "");
						String ywmc = tds.get(8).text().trim()
								.replaceAll(" ", "");
						String myf = tds.get(9).text().trim()
								.replaceAll(" ", "");
						String ctf = tds.get(10).text().trim()
								.replaceAll(" ", "");
						String xxf = tds.get(11).text().trim()
								.replaceAll(" ", "");
						String fyxj = tds.get(12).text().trim()
								.replaceAll(" ", "");
						String yhx = tds.get(13).text().trim()
								.replaceAll(" ", "");

						if (thlx.contains("主叫")) {
							thlx = "主叫";
						} else {
							thlx = "被叫";
						}
						if (thzt.contains("漫游")) {
							thzt = "漫游";
						} else {
							thzt = "本地";
						}
						Map map2 = new HashMap();
						map2.put("phone", login.getLoginName());
						map2.put("cTime", DateUtils.StringToDate(thsj,
								"yyyy-MM-dd HH:mm:ss"));
						List list = mobileDetailService
								.getMobileDetailBypt(map2);
						if (list == null || list.size() == 0) {
							MobileDetail mDetail = new MobileDetail();
							UUID uuid = UUID.randomUUID();
							mDetail.setId(uuid.toString());
							mDetail.setcTime(DateUtils.StringToDate(thsj,
									"yyyy-MM-dd HH:mm:ss"));

							if (bean != null) {
								if (bean.getcTime().getTime() >= mDetail
										.getcTime().getTime()) {
									return false;
								}
							}
							mDetail.setTradeAddr(thdd);
							mDetail.setRecevierPhone(dfhm);
							mDetail.setTradeTime(TimeUtil.timetoint(thsc));
							mDetail.setTradeType(thzt);
							mDetail.setTradeWay(thlx);
							mDetail.setTaocan("");
							mDetail.setOnlinePay(new BigDecimal(fyxj));
							mDetail.setPhone(login.getLoginName());
							detailList.add(mDetail);
							// mobileDetailService.saveMobileDetail(mDetail);

						}

					}
				}
				saveMobileDetail(detailList);
			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
		}
		return b;
	}

	public BigDecimal getYue() {
		BigDecimal phoneremain = new BigDecimal("0");
		CHeader h = new CHeader(CHeaderUtil.Accept_,
				"http://service.zj.10086.cn",
				CHeaderUtil.Content_Type__urlencoded, "service.zj.10086.cn",
				true);
		try {
			String text = cutil
					.get("http://service.zj.10086.cn/yw/bill/queryBalance.do?bid=BD399F39E69048CFE044001635842131",
							h);
			if (text != null && text.contains("账户余额")) {
				RegexPaserUtil rp = new RegexPaserUtil(
						"您的充值账户实际可用余额为：<font class=\"redcolor\">", "</font>",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				String yues = rp.getText();
				if (yues != null) {
					phoneremain = new BigDecimal(yues.replaceAll("\\s*", ""));
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return phoneremain;
	}

	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {

		try {
			
			parseBegin(Constant.YIDONG);
			//http://service.zj.10086.cn/yw/info/queryCustomerInfo.do?bid=BD399F39E68B48CFE044001635842131
			CHeader h = new CHeader(CHeaderUtil.Accept_,
					"http://service.zj.10086.cn",
					CHeaderUtil.Content_Type__urlencoded,
					"service.zj.10086.cn", true);
			boolean b = false;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);
			Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");

			String text = cutil.get(
					"http://service.zj.10086.cn/yw/info/queryCustomerInfo.do?bid="
							+ jsmap.get("dyxdId").toString(), h);
			
			Document doc = Jsoup.parse(text);
			
			Elements tables = doc.select("table[class=yw_tablelisttx1 mt10]");
			String name = "";
			String address = "";
			if(tables != null && tables.size() > 0){
				Elements trs = tables.select("tr");
				if(trs.size() > 8){
					name = tables.select("tr").get(0).select("td").get(0).text();
					address = tables.select("tr").get(8).select("td").get(0).text();
				}
			}

			Map<String, String> map = new HashMap<String, String>(3);
			map.put("parentId", currentUser);
			map.put("loginName", login.getLoginName());
			map.put("usersource", Constant.YIDONG);

			List<User> list = userService.getUserByParentIdSource(map);

			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());
				user.setRealName(name);
				user.setIdcard("");
				user.setAddr(address);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				user.setEmail("");
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());

				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());

				user.setRealName(name);
				user.setIdcard("");
				user.setAddr(address);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				user.setEmail("");
				userService.saveUser(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = e.getMessage();
			sendWarningCallHistory(errorMsg);
		} finally {
			parseEnd(Constant.YIDONG);
		}

	}

	// 首页登录
	public Map<String, Object> login() {
		try {
			String result = login1();
			if (result != null) {
				if (result.equals("1")) {
					loginsuccess();
				} else {
					errorMsg = result;
				}
			}

			if (status == 1) {
				sendPhoneDynamicsCode();
				addTask_1(this);
			}
		} catch (Exception e) {
			writeLogByLogin(e);
		}
		return map;
	}

	// 随机短信登录
	public Map<String, Object> checkPhoneDynamicsCode() {
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String path = "http://service.zj.10086.cn/yw/detailbill/hisQueryDetailBill.do?bid="
				+ jsmap.get("xdId").toString();
		CHeader h = new CHeader(CHeaderUtil.Accept_, null,
				CHeaderUtil.Content_Type__urlencoded, "service.zj.10086.cn",
				true);
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("listtype", "1");
		param.put("validateCode", login.getPhoneCode());
		String text = cutil.post(path, h, param);
		if (text != null) {

			if (text.contains("查询时段选择")) {
				status = 1;
				jsmap.put("phoneCode", login.getPhoneCode());
			} else {
				errorMsg = "验证失败,请重试!";
			}
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		if (status == 1) {
			addTask_2(this);
		}
		return map;
	}

	/**
	 * 生成短信
	 * */
	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errorMsg = null;
		int status = 0;
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		CHeader h = new CHeader(CHeaderUtil.Accept_other,
				"http://service.zj.10086.cn",
				CHeaderUtil.Content_Type__urlencoded, "service.zj.10086.cn",
				true);
		try {
			String text = cutil.get(
					"http://service.zj.10086.cn/yw/detailbill/hisQueryDetailBill.do?bid="
							+ jsmap.get("xdId").toString() + "&listtype=1", h);
			Map<String, String> param = new LinkedHashMap<String, String>();
			text = cutil.post(
					"http://service.zj.10086.cn/yw/detailbill/sendValidateCode.do?bid="
							+ jsmap.get("xdId").toString(), h, param);
			if (text != null && text.contains("发送成功")) {
				errorMsg = "动态密码已发送,您在30分钟内都可以使用该密码查询您的详单。";
				status = 1;
			} else if (text != null && text.contains("时间间隔为1分钟")) {
				errorMsg = "动态密码已发送,您在30分钟内都可以使用该密码,每次获取短信验证码的时间间隔为1分钟.";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (errorMsg == null) {
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		return map;
	}

	public String login1() {

		try {
			CHeader h = new CHeader(CHeaderUtil.Accept_, "",
					CHeaderUtil.Content_Type__urlencoded, "zj.ac.10086.cn",
					true);
			CHeader h1 = new CHeader(CHeaderUtil.Accept_,
					"https://zj.ac.10086.cn/login",
					CHeaderUtil.Content_Type__urlencoded, "www.zj.10086.cn",
					false);
			CHeader h2 = new CHeader(CHeaderUtil.Accept_,
					"http://service.zj.10086.cn",
					CHeaderUtil.Content_Type__urlencoded,
					"service.zj.10086.cn", false);

			Map<String, String> param = new LinkedHashMap<String, String>();
			param.put("service", "my");
			param.put("continue", "/my/login/loginSuccess.do");
			param.put("failurl", "https://zj.ac.10086.cn/login");
			param.put("style", "1");
			param.put("pwdType", "2");
			param.put("SMSpwdType", "0");
			param.put("billId", login.getLoginName());
			param.put("passwd", login.getPassword());
			param.put("validCode", login.getAuthcode());
			String text = cutil.post("https://zj.ac.10086.cn/loginbox", h,
					param);
			if (text != null) {
				if (text.contains("https://zj.ac.10086.cn/login?AISSO_LOGIN=true")) {
					return URLDecoder.decode(text.split("msg=")[1], "GBK");
				} else {
					param = new LinkedHashMap<String, String>();
					Document doc = Jsoup.parse(text);
					Elements elements = doc.getElementsByAttributeValue("type",
							"hidden");
					for (Element ele : elements) {
						param.put(ele.attr("name"), ele.attr("value"));
					}
					param.put("submit", "提交查询");
					text = cutil.post("http://www.zj.10086.cn/my/sso", h1,
							param);
					Document doc1 = Jsoup.parse(text);
					param = new LinkedHashMap<String, String>();
					Elements elements1 = doc1.getElementsByAttributeValue(
							"type", "hidden");
					for (Element ele : elements1) {
						param.put(ele.attr("name"), ele.attr("value"));
					}
					text = cutil
							.post("http://www.zj.10086.cn/my/UnifiedLoginClientServlet",
									param);
					if (text.contains("/my/login/loginSuccess.do")) {
						text = cutil
								.get("http://www.zj.10086.cn/my/login/loginSuccess.do",
										h1);
						if (text != null && text.contains("my/index.jsp")) {
							text = cutil.get(
									"http://www.zj.10086.cn/my/index.jsp", h1);
							if (text != null && text.contains("/my/index.do")) {
								text = cutil
										.get("http://service.zj.10086.cn/busi/goYwQueryPage.do?busi_id=BC5CC0A69BBA0482E044001635842131",
												h2);
								Document indexDoc = Jsoup.parse(text);
								Element dyzdHref = indexDoc.select(
										"a[title=实时账单查询]").get(0);
								if (dyzdHref != null) {
									String dyzdString = dyzdHref
											.attr("href")
											.substring(
													dyzdHref.attr("href")
															.indexOf("bid=") + 4);
									((Map) redismap.get("jsmap")).put("dyzdId",
											dyzdString);
								}
								String zdHref = indexDoc
										.select("a[title=月账单查询]").get(0)
										.attr("href");
								if (zdHref != null) {
									((Map) redismap.get("jsmap")).put("zdId",
											zdHref.split("=")[1].split("&")[0]);
								}

								String dyxdHref = indexDoc
										.select("a[title=当月语音详单]").get(0)
										.attr("href");
								if (dyxdHref != null) {
									((Map) redismap.get("jsmap"))
											.put("dyxdId",
													dyxdHref.split("=")[1]
															.split("&")[0]);
								}
								String xdHref = indexDoc
										.select("a[title=历史语音详单]").get(0)
										.attr("href");
								if (xdHref != null) {
									String xdid = xdHref.split("=")[1];
									((Map) redismap.get("jsmap")).put("xdId",
											xdid.split("&")[0]);
								}
								return "1";
							}

						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) throws Exception {

		// try {
		// zjSpider();
		//
		// } catch (ClientProtocolException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		ZJYidong zj = new ZJYidong(new Login("15267700746", "888666"), null);
		zj.index();
		zj.inputCode(imgurl);
		zj.login();

	}

	/**
	 * <p>查询话费记录
	 * 已做更新处理
	 */
	public boolean getTelDetailHtml_parse(String text, String startDate) {
		boolean b = true;
		try {
			if (text.contains("套餐及固定费")) {
				BigDecimal hj = new BigDecimal(0);
				BigDecimal tcgdf = new BigDecimal(0);
				BigDecimal ldxsf = new BigDecimal(0);
				BigDecimal yythf = new BigDecimal(0);
				BigDecimal cxf = new BigDecimal(0);
				BigDecimal zzywf = new BigDecimal(0);
				BigDecimal yhf = new BigDecimal(0);
				text = text.replaceAll("\\s*", "");
				RegexPaserUtil rp = new RegexPaserUtil(
						"套餐及固定费</th><tdalign=\"center\">", "</td>", text,
						RegexPaserUtil.TEXTEGEXANDNRT);
				String tcgdfs = rp.getText();
				if (tcgdfs != null) {
					tcgdf = new BigDecimal(tcgdfs);
				}
				rp = new RegexPaserUtil("语音通信费</th><tdalign=\"center\">",
						"</td>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String yythfs = rp.getText();
				if (yythfs != null) {
					yythf = new BigDecimal(yythfs);
				}
				rp = new RegexPaserUtil("短彩信费</th><tdalign=\"center\">",
						"</td>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String cxfs = rp.getText();
				if (cxfs != null) {
					cxf = new BigDecimal(cxfs);
				}
				rp = new RegexPaserUtil("增值业务费</th><tdalign=\"center\">",
						"</td>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String zzywfs = rp.getText();
				if (zzywfs != null) {
					zzywf = new BigDecimal(zzywfs);
				}
				rp = new RegexPaserUtil("优惠费</th><tdalign=\"center\">",
						"</td>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String yhfs = rp.getText();
				if (yhfs != null) {
					yhf = new BigDecimal(yhfs);
				}
				rp = new RegexPaserUtil(
						"合计</strong></th><tdclass=\"fBlack\"align=\"center\">",
						"</td>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String hjs = rp.getText();
				if (hjs != null) {
					hj = new BigDecimal(hjs);
				}
				String year = startDate.split("=")[1];
				String mouth = startDate.split("=")[0];
				String fday = TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)));
				String eday = TimeUtil.getLastDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)));
				Map map2 = new HashMap();
				map2.put("phone", login.getLoginName());
				map2.put("cTime",
						DateUtils.StringToDate(year + mouth, "yyyyMM"));
				List list = mobileTelService.getMobileTelBybc(map2);
				if (list == null || list.size() == 0) {
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					// mobieTel.setBaseUserId(currentUser);
					mobieTel.setcTime(DateUtils.StringToDate(year + mouth,
							"yyyyMM"));
					// mobieTel.setcName(cName);
					mobieTel.setTeleno(login.getLoginName());
					// mobieTel.setBrand(brand);
					mobieTel.setTcgdf(tcgdf);
					mobieTel.setLdxsf(ldxsf);
					mobieTel.setTcwyytxf(yythf);
					mobieTel.setTcwdxf(cxf);
					mobieTel.setDependCycle(fday + "至" + eday);
					mobieTel.setZzywf(zzywf);
					mobieTel.setJtyhtc(yhf);
					mobieTel.setcAllPay(hj);
					//坑爹的命名！英语不会拼啊！！
					MobileTel mobileMax=mobileTelService.getMaxTime(mobieTel);
					if(mobileMax!=null && mobileMax.getcTime()==mobieTel.getcTime()){
						mobieTel.setId(mobileMax.getId());
						mobileTelService.update(mobieTel);
					}
					else
						mobileTelService.saveMobileTel(mobieTel);
				}
			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
		}

		return b;
	}

	/**
	 * 查询话费记录
	 */
	public void getTelDetailHtml() {

		try {
			parseBegin(Constant.YIDONG);
			boolean b = true;
			int num = 0;
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			List<String> ms = DateUtils.getMonths(6, "MM=yyyy");
			CHeader h = new CHeader(CHeaderUtil.Accept_,
					"http://service.zj.10086.cn/yw/bill/billDetailBefore.do?bid="
							+ jsmap.get("zdId").toString(),
					CHeaderUtil.Content_Type__urlencoded,
					"service.zj.10086.cn", true);

			for (String startDate : ms) {
				if (ms.get(0) == startDate) {
					String text = cutil.get(
							"http://service.zj.10086.cn/yw/bill/billDetail.do?bid="
									+ jsmap.get("dyzdId").toString(), h);
					b = getTelDetailHtml_parse(text, startDate);
					if (!b) {
						// 异常信息
						if (errorMsg != null) {
							num++;
							// 超过五次,发送错误信息,
							if (num > 5) {
								// 发送错误信息通知
								sendWarningCallHistory(errorMsg);
							}
							// 错误
							errorMsg = null;
						} else {
							break;// 数据库中已有数据
						}
					}
				} else {
					Map<String, String> parms = new LinkedMap();
					parms.put("listtype", "");
					parms.put("month", startDate);
					parms.put("validateCode", "");
					String text = cutil.post(
							"http://service.zj.10086.cn/yw/bill/billDetail.do?bid="
									+ jsmap.get("zdId").toString(), h, parms);
					b = getTelDetailHtml_parse(text, startDate);
					if (!b) {
						// 异常信息
						if (errorMsg != null) {
							num++;
							// 超过五次,发送错误信息,
							if (num > 5) {
								// 发送错误信息通知
								sendWarningCallHistory(errorMsg);
							}
							// 错误
							errorMsg = null;
						} else {
							break;// 数据库中已有数据
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parseEnd(Constant.YIDONG);
		}

	}

}
