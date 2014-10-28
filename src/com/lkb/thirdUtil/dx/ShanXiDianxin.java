package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import com.lkb.bean.client.Login;
import  com.lkb.thirdUtil.base.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.robot.Spider;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;

public class ShanXiDianxin extends AbstractDianXinCrawler {
	
	public ShanXiDianxin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
		this();
		this.spider = spider;
		this.phoneNo = phoneNo;
		this.password = password;
		if (user == null) {
			this.user = new User();
			this.user.setPhone(phoneNo);
		} else {
			this.user = user;
		}
		this.util = util;
		this.authCode = authCode;
		spider.getSite().setCharset("utf-8");
	}
	public ShanXiDianxin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		spider.getSite().setCharset("utf-8");
	}
	public ShanXiDianxin() {
		areaName = "陕西";
		customField1 = "3";
		customField2 = "27";
		toStUrl = "&toStUrl=http://sn.189.cn/service/bill/fee.action?type=resto";
		shopId = "10027";
		
	}
	
	public void checkVerifyCode(final String userName) {  
		//sendSmsPasswordForRequireCallLogService();
		saveVerifyCode("sx", userName);
		
    	/*Request req = new Request("https://uam.ct10000.com/ct10000uam/login?service=http://hlj.189.cn/dqmh/Uam.do?method=loginJTUamGet");
    	req.putExtra("redirectsEnabled", false);
    	//req.setMethod("POST"3);
    	//req.putHeader("Referer", "http://ln.ac.10086.cn/login");
    	//req.putHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    	
    	req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.SXDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				data.put("checkVerifyCode","1");
				//data.put("sessionId", ContextUtil.getCookieValue(context, "JSESSIONID"));
				String picName =  "sx_dx_code_" + userName + "_6.jpg";
				try {
					String imgName = saveFile("https://uam.ct10000.com/ct10000uam/validateImg.jsp", "https://uam.ct10000.com/", null, picName, true);
					data.put("imgName", imgName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//notifyStatus();
			}
		});
    	spider.addRequest(req);*/
    	//printData();
    }
	
	//Created by Dongyu.Zhang
	public void getUserInfo(){
		//因为没有陕西电信登录的问题常量，所以用山西了=。=   SHXDX_1
		getUrl("http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo", "http://www.189.cn/sn/",new AbstractProcessorObserver(util, WaringConstaint.SXDX_2){
			@Override
			public void afterRequest(SimpleObject context){
				String text = ContextUtil.getContent(context);
				if(text != null){
					//System.out.println(text);
					String email = StringUtil.subStr("<input name=\"email\" id=\"email\" type=\"hidden\" value=\"", "\">", text);
					user.setEmail(email);
					StringBuffer realName = new StringBuffer("");
					try {
						realName.append(StringUtil.subStr("<input name=\"realName\" class=\"width130\" type=\"text\" maxlength=\"20\" value=\"", "\">", text)); 
					} catch (Exception e) {
						logger.error("erroe", e);
					}
					user.setRealName(realName.toString());
					String idNumberStart = StringUtil.subStr("onfocus=\"removalPrompt('cardnumTd')\" maxlength=\"18\"","\" />" , text);
					String idCard = idNumberStart.trim().substring(7);
					user.setIdcard(idCard);
					if(!"".equals(idCard) && idCard != null){
						int idCardLen = idCard.length();
						switch (idCardLen) {
						case 15:
							//320521 72 08 07 024 
							String year_15 = "19" + idCard.substring(6, 8);
							String month_15 = idCard.substring(8, 10);
							String day_15 = idCard.substring(10, 12);
							user.setBirthday(DateUtils.StringToDate(year_15 + month_15 + day_15, "yyyyMMdd"));
							break;
						case 18:
							//140321 1986 09 11 0310
							String year_18 = idCard.substring(6, 10);
							String month_18 = idCard.substring(10, 12);
							String day_18 = idCard.substring(12, 14);
							user.setBirthday(DateUtils.StringToDate(year_18 + month_18 + day_18, "yyyyMMdd"));
							break;
						}
					}
					user.setUserName(phoneNo);
					
				}
			}
		});
	}
	
	
	
	
	public void sendSmsPasswordForRequireCallLogService() {       
		getUrl("http://sn.189.cn/service/bill/sendValidReq.action?listType=1&mobileNum=" + phoneNo, "http://sn.189.cn/service/bill/sendValidReq.action", new AbstractProcessorObserver(util, WaringConstaint.SXDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				setStatus(STAT_SUC);
				notifyStatus();
			}
		});
		
	}
	protected void onCompleteLogin(SimpleObject context) {
		//判断是否登陆成功...
		postUrl("http://sn.189.cn/service/account/findUserOttActivity.action", 
				"http://sn.189.cn/service/bill/feeDetailrecordList.action", null, new AbstractProcessorObserver(util, WaringConstaint.SXDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
//				String str=ContextUtil.getContent(context);
//				System.out.println(str);
			}
		});
		
		getUrl("http://sn.189.cn/service/bill/resto.action?rnd="+Math.random(), 
				"http://sn.189.cn/service/bill/fee.action", null, null);
		
		sendSmsPasswordForRequireCallLogService();
		//requestService(context);
	}
	public void requestAllService(String authCode) {
		requestService();
	}
	private void requestService() {
		/*ContextUtil.getTask(context).addUrl(new AbstractProcessorObserver(util, WaringConstaint.SXDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
			}
		}, "http://www.ln.10086.cn/my/account/detailquery.xhtml");*/
		setStatus(STAT_SUC);
		notifyStatus();
		getUserInfo();
		Date d = new Date();
		
		//不知道这两个请求有啥用
//		postUrl("http://sn.189.cn/service/account/findUserOttActivity.action", 
//				"http://sn.189.cn/service/bill/feeDetailrecordList.action", null, new AbstractProcessorObserver(util, WaringConstaint.SXDX_6) {
//			@Override
//			public void afterRequest(SimpleObject context) {
//				String str=ContextUtil.getContent(context);
//				System.out.println(str);	
//			}
//		});
//		getUrl("http://sn.189.cn/service/bill/initQueryTicket.action?rnd="+Math.random(), "http://sn.189.cn/service/bill/fee.action", null);
		
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			/*String dstr = DateUtils.formatDate(cd, "yyyyMM");
			if (i > 0) {
				
			}*/
			//requestMessageService(1,DateUtils.add(new Date(), Calendar.MONTH, -1 * i));
			requestCallLogService(1, cd);
		}
		for(int i = 0; i < 7; i++){
			final Date dd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			requestMessageLogService(1, dd);
			//requestOnlineFlowService(1, dd);
		}
		requestMonthBillService(d);
		parseBalanceInfo();
		
		//requestCallLogService(context, number, queryDate);
	}	
	
	private void parseBalanceInfo() {
		//http://ln.189.cn/getSessionInfo.action
		//String[][] params = {{"Action", "post"}, {"Name", "lulu"}};
		//{"accNbr":"18040212031","TSR_RESULT":"0","TSR_CODE":"0","restFee":24.04,"TSR_MSG":"","queryDay":30,"userName":"周灏","queryDate":"2014年07月30日"}
		postUrl("http://sn.189.cn/service/bill/resto.action?rnd=" + Math.random(), "http://sn.189.cn/service/bill/fee.action?type=resto", null, new AbstractProcessorObserver(util, WaringConstaint.SXDX_7){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context);
				
				Element table = doc.select("table.m20").get(0);
				Elements es = table.select("tr");
				for(int i = 1; i < es.size(); i++) {
					Elements es1 = es.get(i).select("td");
					if (es1.size() == 5) {
						String userName = es1.get(0).text();
						user.setRealName(userName);
						BigDecimal b1= new BigDecimal(es1.get(2).text().trim());
						addPhoneRemain(b1);
					} else if (es1.size() == 2) {
						BigDecimal b1= new BigDecimal(es1.get(1).text().trim());
						addPhoneRemain(b1);
					}
				}				
			}
		});
	}	
	
	private void requestMonthBillService(final Date d) {
		String dstr = DateUtils.formatDate(d, "yyyyMM");
		getUrl("http://sn.189.cn/service/bill/billDetail.action?billtype=5&month=" + dstr + "&areacode=290&accnbr=" + phoneNo + "&productid=41010300", "http://sn.189.cn/service/bill/fee.action?type=bill", new AbstractProcessorObserver(util, WaringConstaint.SXDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				
				Elements rect = doc.select("v|rect[title]");
				if (rect.size() == 0) {
					//还没出帐单
					Date cd = DateUtils.add(d, Calendar.MONTH, -1);
					try{
						requestMonthBillService(cd);
					}catch(Exception e){
						logger.error("ShanXiDianXin monthbill error:",e);
					}
				} else {
					//201401 267.0
					for(int i=0; i<rect.size(); i++) {
						try {
							String title = rect.get(i).attr("title");
							String[] arr2 = title.split(" ");
							BigDecimal b1= new BigDecimal(arr2[1].trim());
							DianXinTel tel = new DianXinTel();
							final String td = arr2[0].trim();
							tel.setcTime(DateUtils.StringToDate(td, "yyyyMM"));
							tel.setcAllPay(b1);
							data.put("Month" + td, td);
							data.put("Pay" + td, arr2[1].trim());
							tel.setTeleno(phoneNo);
							getTelList().add(tel);
						} catch (Exception e) {
							logger.error("month bill", e);
						}
					}
				}				
			}
		});
	}
	//Created by Dongyu.Zhang
	private void saveMessageLog(SimpleObject context, final int page, final Date d){
		Date dt = new Date();
		try {
			Document doc = ContextUtil.getDocumentOfContent(context); 			
			Elements div = doc.select("div#xs2");
			if (div.size() > 0) {
				String t1 = div.text();
				String dstr = "date=" + DateUtils.formatDate(d, "yyyy-MM-dd") + ":page=" + page;
				if (t1.indexOf("查询失败") >= 0) {
					logger.error(dstr, t1);
				} else if (t1.indexOf("无话单记录") >= 0) {

				} else {
					logger.error(dstr, t1);
				}
			} else {
				setStatus(STAT_SUC);
				notifyStatus();
				//utf-8
				Elements es = doc.select("table.transact_tab");
				Elements trs = es.select("tr");
				int len = trs.size();
				Date now = new Date();
				
				for(int i = 1; i < len; i++) {
					Elements tds = trs.get(i).select("td");
					TelcomMessage tMessage = new TelcomMessage();
					UUID uuid = UUID.randomUUID();
					tMessage.setId(uuid.toString());
					dt = DateUtils.StringToDate(tds.get(1).text(), "yyyy/MM/dd HH:mm:ss");
					tMessage.setPhone(phoneNo);
					String fsjs = tds.get(2).text();
					if(fsjs.contains("接收")){
						tMessage.setBusinessType("接收");
					}else{
						tMessage.setBusinessType("发送");
					}
					tMessage.setRecevierPhone(tds.get(0).text());
					tMessage.setSentTime(DateUtils.StringToDate(tds.get(1).text(), "yyyy/MM/dd HH:mm:ss"));
					tMessage.setAllPay(Double.parseDouble(tds.get(3).text()));
					tMessage.setCreateTs(now);				
					messageList.add(tMessage);
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void requestMessageLogService(final int page, final Date d){
		
		try{
			Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
			String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
			String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
			String[][] pairs1 = {{"currentPage", page + ""}, {"pageSize", "50"}, {"effDate", bd}, {"expDate", ed}, {"serviceNbr", phoneNo}, {"operListID", "12"}, {"isPrepay", "2"}, 
					{"pOffrType", "481"}, {"sendSmsFlag", "true"}, {"validCode", authCode}};
			postUrl("http://sn.189.cn/service/bill/feeDetailrecordList.action", 
					"http://sn.189.cn/service/bill/feeDetailrecordList.action", pairs1, new AbstractProcessorObserver(util, WaringConstaint.SXDX_6) {
				@Override
				public void afterRequest(SimpleObject context) {
					saveMessageLog(context, page, d);
				}
			});
		}catch(Exception e){
			
		}
		
	}
	private void requestOnlineFlowService(final int page, final Date d){
		
		try{
			Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
			String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
			String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
			String[][] pairs1 = {{"currentPage", page + ""}, {"pageSize", "15"}, {"effDate", bd}, {"expDate", ed}, {"serviceNbr", phoneNo}, {"operListID", "14"}, {"isPrepay", "2"}, 
					{"pOffrType", "481"}, {"sendSmsFlag", "true"}, {"validCode", authCode}};
			postUrl("http://sn.189.cn/service/bill/feeDetailrecordList.action", 
					"http://sn.189.cn/service/bill/feeDetailrecordList.action", pairs1, new AbstractProcessorObserver(util, WaringConstaint.SXDX_6) {
				@Override
				public void afterRequest(SimpleObject context) {
					saveOnlineFlow(context, page, d);
				}
			});
		}catch(Exception e){
			
		}
	}
	//Created by jiangzongren
	private void saveOnlineFlow(SimpleObject context, final int page, final Date d){
		Date dt = new Date();
		try {
			Document doc = ContextUtil.getDocumentOfContent(context); 			
			Elements table_detail = doc.select("table[name=mt10 transact_tab]");
			Elements table_bill = doc.select("table[name=tabsty]");
			if ((table_detail==null||table_detail.size()==0)&&(table_bill==null||table_bill.size()==0)) {
				return ;
			} else {
				try {
					Elements trs = table_bill.select("tr");
					String dependCycle = trs.get(1).select("td").get(0).text().replaceAll("起止日期：", "");
					Date queryMonth = DateUtils.StringToDate(dependCycle.substring(0, 7).replaceAll("\\.", ""), "yyyyMM");
					String allFlow = trs.get(3).select("td").get(0).text().replaceAll("总&nbsp;&nbsp;流&nbsp;量：", "");
					int allTime =  TimeUtil.timetoint_HH_mm_ss(trs.get(3).select("td").get(1).text().replaceAll("总&nbsp;&nbsp;时&nbsp;长：", "").replaceAll("小时", ":").replaceAll("分", ":").replaceAll("秒", ""));
					String allPay = trs.get(4).select("td").get(0).text().replaceAll("总&nbsp;&nbsp;费&nbsp;用：", "").replaceAll("(元)", "");
					BigDecimal allFlows=new BigDecimal(com.lkb.util.StringUtil.flowFormat(allFlow));
					BigDecimal allPays = new BigDecimal(allPay);
					DianXinFlow dianXinFlow = new DianXinFlow();
					dianXinFlow.setAllFlow(allFlows);
					dianXinFlow.setAllPay(allPays);
					dianXinFlow.setAllTime(new BigDecimal(allTime));
					dianXinFlow.setDependCycle(dependCycle);
					dianXinFlow.setPhone(phoneNo);
					dianXinFlow.setQueryMonth(queryMonth);
					flowList.add(dianXinFlow);
				} catch (Exception e) {
					logger.error(e.toString());
				}
				try {
					Elements trs = table_detail.get(0).select("tr");
					int len = trs.size();
					for(int i = 1; i < len; i++) {
						Elements tds = trs.get(i).select("td");
						if (tds.size()==9) {
							String beginTime = tds.get(1).text().trim();// 开始时间
							int tradeTime = TimeUtil.timetoint_HH_mm_ss(tds.get(2).text().trim().replaceAll("小时", ":").replaceAll("时", ":").replaceAll("分", ":").replaceAll("秒", ""));// 上网时长
							int flow = Integer.parseInt(tds.get(3).text().trim());// 总流量
							String netType = tds.get(4).text().trim();// 通讯类型
							String business = tds.get(5).text().trim();// 计费方式
							String location = tds.get(6).text().trim();// 上网地市
							String fee = tds.get(8).text().trim();// 费用（元）
							Date beginTimeDate = null;
							BigDecimal feeDecimal = new BigDecimal(0);
							try {
								beginTimeDate = DateUtils.StringToDate(beginTime,
										"yyyy/MM/dd HH:mm:ss");
								feeDecimal = new BigDecimal(fee);
							} catch (Exception e) {
								e.printStackTrace();
							}
							DianXinFlowDetail obj = new DianXinFlowDetail();
							obj.setPhone(phoneNo);
							obj.setBeginTime(beginTimeDate);
							obj.setFee(feeDecimal);
							obj.setNetType(netType);
							obj.setTradeTime(tradeTime);
							BigDecimal flows = new BigDecimal(0);
							try {
								flows = new BigDecimal(flow);
							} catch (Exception e) {
							}
							obj.setFlow(flows);
							obj.setLocation(location);
							obj.setBusiness(business);
							flowDetailList.add(obj);
						}
					}
				} catch (Exception e) {
					logger.error(e.toString());
				}
				
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void requestCallLogService(final int page, final Date d) {
		

		
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		//DateUtils.lastDayOfMonth("");
		String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
		String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		String[][] pairs = {{"currentPage", page + ""}, {"pageSize", "5000"}, {"effDate", bd}, {"expDate", ed}, {"serviceNbr", phoneNo}, {"operListID", "11"}, {"isPrepay", "2"}, 
				{"pOffrType", "481"}, {"sendSmsFlag", "true"}, {"validCode", authCode}};
		postUrl("http://sn.189.cn/service/bill/feeDetailrecordList.action", 
				"http://sn.189.cn/service/bill/feeDetailrecordList.action", pairs, new AbstractProcessorObserver(util, WaringConstaint.SXDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, page, d);
			}
		});
		
	}
	private void saveCallLog(SimpleObject context, final int page, final Date d) {
		try {
			Document doc = ContextUtil.getDocumentOfContent(context); 			
			Elements div = doc.select("div#xs2");
			if (div.size() > 0) {
				String t1 = div.text();
				String dstr = "date=" + DateUtils.formatDate(d, "yyyy-MM-dd") + ":page=" + page;
				if (t1.indexOf("查询失败") >= 0) {
					logger.error(dstr, t1);
				} else if (t1.indexOf("无话单记录") >= 0) {

				} else {
					logger.error(dstr, t1);
				}
			} else {
				setStatus(STAT_SUC);
				notifyStatus();
				//utf-8
				Elements es = doc.select("table.transact_tab");
				Elements trs = es.select("tr");
				int len = trs.size();
				for(int i = 1; i < len; i++) {
					try {
						Elements tds = trs.get(i).select("td");
						DianXinDetail dxDetail = new DianXinDetail();
						dxDetail.setPhone(phoneNo);

						dxDetail.setTradeType(tds.get(8).text());
						dxDetail.setRecevierPhone(tds.get(0).text());
						//dxDetail.setTradeAddr(obj1.getString("callType"));					
						dxDetail.setcTime(DateUtils.StringToDate(tds.get(2).text(), "yyyy/MM/dd HH:mm:ss"));
						dxDetail.setTradeTime(Integer.parseInt(tds.get(3).text()));
						dxDetail.setCallWay(tds.get(1).text());
						dxDetail.setAllPay(new BigDecimal(tds.get(13).text()));
						dxDetail.setBasePay(new BigDecimal(tds.get(8).text()));
						dxDetail.setLongPay(new BigDecimal(tds.get(9).text()));
						dxDetail.setTradeAddr(tds.get(4).text());
						//dxDetail.setOnlinePay(new BigDecimal(text));
						//dxDetail.setTradeWay(text);					
						detailList.add(dxDetail);
					} catch (Exception e) {
						logger.error("shanxidianxin call log", e);
					}
				}
				requestCallLogService(page + 1, d);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		String phoneNo = "18161910153";
		String password = "462742";
		String c = "<buffalo-reply><string>&lt;div class=&quot;zzfw_fycxh_sshf&quot;&gt;&lt;p&gt; 您的号码&lt;span&gt;18189814687&lt;/span&gt;，截止&lt;span&gt;2014年08月07日15时00分&lt;/span&gt;，账户余额&lt;span&gt;25.07&lt;/span&gt;元,其中专项使用余额 &lt;span&gt;0.00&lt;/span&gt;元.&lt;/p&gt; &lt;/div&gt;&lt;br&gt;&lt;/br&gt;</string></buffalo-reply>";
		Document doc = Jsoup.parse(c);
		
		Elements rect = doc.select("string");
		System.out.println("title=" + StringEscapeUtils.unescapeHtml4(rect.text()));
		/*Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		LNDianxin dx = new LNDianxin(spider, null, phoneNo, password, "2345", null);
		dx.checkVerifyCode("aaa");
		//dx.goLoginReq();
		spider.start();
		dx.printData();*/
		if (true) {
			return;
		}

	}
}
