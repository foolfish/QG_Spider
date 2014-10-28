package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.common.primitives.Doubles;
import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

public class XinJiangDianXin extends AbstractDianXinCrawler {
	
	public XinJiangDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
		spider.getSite().setCharset("gbk");
	}
	public XinJiangDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		spider.getSite().setCharset("gbk");
	}
	public XinJiangDianXin() {
		areaName = "新疆";
		customField1 = "3";
		customField2 = "31";
		//http://www.189.cn/dqmh/login/loginJT.jsp?UserUrlto=/dqmh/frontLink.do?method=linkTo&shopId=10031&toStUrl=http://xj.189.cn/service/account/index.jsp
		//http://www.189.cn/dqmh/frontLink.do?method=linkTo&shopId=10031&toStUrl=http://xj.189.cn:80/common/login.jsp
		toStUrl = "&toStUrl=http://xj.189.cn:80/common/login.jsp";
		shopId = "10031";
	}
	//https://uam.ct10000.com/ct10000uam/validateImg.jsp
	public void checkVerifyCode(final String userName) {   
		saveVerifyCode("xinjiang", userName);
		//1.生成一个request
		//2.请求完成后的解析
		//3.加入到Spider的执行队列		
    }
	public void sendSmsPasswordForRequireCallLogService() {
		postUrl("http://he.189.cn/queryCheckSecondPwdAction.action", "	http://he.189.cn/group/bill/bill_billlist.do", new String[][] {{"inventoryVo.accNbr", phoneNo}, {"inventoryVo.productId", "8"}}, new AbstractProcessorObserver(util, WaringConstaint.XJDX_4){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					String text = ContextUtil.getContent(context); 
					if (text != null && text.indexOf("请输入验证码") >= 0) {
						setStatus(STAT_SUC);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	/*
	public void verifySmsCode() {
		Date d = new Date();
		String dstr = DateUtils.formatDate(d, "yyyyMM");
		String xml = "<buffalo-call><method>queryDetailBill</method><map><type>java.util.HashMap</type><string>PRODNUM</string><string>MVWCdY0vDvFeiJ7Pmdzsqg==</string><string>CITYCODE</string><string>0898</string><string>QRYDATE</string><string>" + dstr + "</string><string>TYPE</string><string>8</string><string>PRODUCTID</string><string>50</string><string>CODE</string><string>631627</string><string>USERID</string><string>10158069</string></map></buffalo-call>";
		postUrl("http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service", "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=yecx", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.LNYD_7){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
					Elements rect = doc.select("string");
					String ac = rect.get(rect.size() - 1).text();
					requestAllService();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}*/
	//https://uam.ct10000.com/ct10000uam-gate/SSOFromUAM?ReturnURL=687474703A2F2F686E2E3138392E636E3A38302F686E73656C66736572766963652F75616D6C6F67696E2F75616D2D6C6F67696E2175616D4C6F67696E5265742E616374696F6E3F7255726C3D2F686E73656C66736572766963652F62696C6C71756572792F62696C6C2D71756572792173686F77546162732E616374696F6E3F746162496E6465783D33&ProvinceId=19

	protected void onCompleteLogin(SimpleObject context) {
		//sendSmsPasswordForRequireCallLogService();
		//logger.info(ContextUtil.getRequest(context).getUrl());
		if(ContextUtil.getContent(context).contains("IS_LOGINED =\"true\"")){
			setStatus(STAT_LOGIN_SUC);
			notifyStatus();
			requestService();
		}else{
			setStatus(STAT_STOPPED_FAIL);
			data.put("errMsg", "登录失败，请重试！");
			notifyStatus();
			return;
		}
		//parseBalanceInfo();
	}
	
	public void requestAllService() {
		requestService();
	}
	private void requestService() {
		parseBalanceInfo();
		requestMonthBillService();	
		requestCurrentMonthBillService();
		Date d = new Date();
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			requestCallLogService(cd,dstr);
		}
	}	
	
	private void parseBalanceInfo() {
		//String text = CUtil.get("http://xj.189.cn/service/bill/ye2.jsp");
		getUrl("http://xj.189.cn/service/manage/wdzl.jsp", null, new Object[] {"gbk"}, new AbstractProcessorObserver(util, WaringConstaint.XJDX_1){
			//2.请求完成后的解析
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				try {
					Elements trs = doc.select("table[class=nb]").select("tr");
					if (trs!=null&&!trs.toString().equals("")) {
						
						for(int i=0;i<trs.size();i++)
						{
							if(trs.get(i).toString().contains("客户名称"))
							{
								user.setRealName(trs.get(i).select("td").get(1).text().replace("&nbsp;", "").trim());//客户名称
							}
							else if(trs.get(i).toString().contains("证件号码"))
							{
								user.setIdcard(trs.get(i).select("td").get(1).text().replace("&nbsp;", "").trim());//证件号码
							}
							else if(trs.get(i).toString().contains("通信地址"))
							{
								user.setAddr(trs.get(i).select("td").get(1).select("input").first().val());//通信地址
							}
							else if(trs.get(i).toString().contains("E-mail"))
							{
								user.setEmail(trs.get(i).select("td").get(1).select("input").first().val());
							}
						}
						user.setUserName(phoneNo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//使用父类的方法 postUrl
		getUrl("http://xj.189.cn/service/bill/ye2.jsp", "http://xj.189.cn/service/bill/ye.jsp", null, new AbstractProcessorObserver(util, WaringConstaint.XJDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				//System.out.println(ContextUtil.getContent(context));
				Document doc = ContextUtil.getDocumentOfContent(context); 
				try {
					Elements div = doc.select("div.kbox");
					if (div!=null&&!div.toString().equals("")) {
					String n = StringUtil.subStr("存款余额", "元", div.text());
					BigDecimal b1= new BigDecimal(n);
					//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+b1);
					addPhoneRemain(b1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}	
	
	private void requestMonthBillService() {
		postUrl("http://xj.189.cn/service/bill/zd.jsp", "http://xj.189.cn/service/bill/zd.jsp", null, new AbstractProcessorObserver(util, WaringConstaint.XJDX_3){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
				if (doc == null||doc.toString().equals("")) {
					return;
				}
				Elements trs = doc.select("table").get(1).select("tr");
				if (trs==null||trs.toString().equals("")) {
					return;
				}
				for(int i = 2 ; i<trs.size();i++){
					Elements tds = trs.get(i).select("td");
					DianXinTel tel = new DianXinTel();
					tel.setcTime(DateUtils.StringToDate(tds.get(0).text(), "yyyyMM"));
					String pay = tds.get(2).text().replaceAll("元", "").replaceAll("\\s*", "").trim();
					BigDecimal b1 = new BigDecimal(pay.length() == 0 ? "0" : pay);
					tel.setcAllPay(b1);
					tel.setTeleno(phoneNo);
					telList.add(tel);
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	private void requestCurrentMonthBillService() {
		postUrl("http://xj.189.cn/service/bill/sshf2.jsp", "http://xj.189.cn/service/bill/zd.jsp", null, new AbstractProcessorObserver(util, WaringConstaint.XJDX_3){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
				if (doc == null||doc.toString().equals("")) {
					return;
				}
					String text = doc.toString();
					DianXinTel tel = new DianXinTel();
					List<String> cmon = DateUtils.getMonthForm(1, "yyyyMM");
					tel.setcTime(DateUtils.StringToDate(cmon.get(0), "yyyyMM"));
					RegexPaserUtil rp = new RegexPaserUtil( "您的本月实时费用总额为", "元.查询" ,text, RegexPaserUtil.TEXTEGEXANDNRT);
					String pay = rp.getText();
					BigDecimal b1 = new BigDecimal(pay.length() == 0 ? "0" : pay);
					tel.setcAllPay(b1);
					tel.setTeleno(phoneNo);
					telList.add(tel);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	private void requestCallLogService(final Date d, final String dstr) {
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyyMMdd");
		String ed = DateUtils.formatDate(ds[1], "yyyyMMdd");
		String detail_url = bd+"-"+ed+"&number=0000-"+phoneNo+"-mob&callType=&listingType=0";
		data.put("detail_url", detail_url);
		//http://xj.189.cn/EcssTuxedo235.do?curPage=1&searchDate=http://xj.189.cn/EcssTuxedo235.do?curPage=1&pageSize=10000&searchDate=20140701-20140731&number=0994-18199272917-mob&callType=&listingType=0
		//String url = "http://xj.189.cn/EcssTuxedo235.do?curPage=1&searchDate="+detail_url;
		String url_detail = "http://xj.189.cn/EcssTuxedo235.do?curPage=1&pageSize=10000&searchDate="+detail_url;
		//默认查询用户的10000条通话详单
		postUrl(url_detail, null, null, new AbstractProcessorObserver(util, WaringConstaint.XJDX_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				if (text != null && !text.contains("很抱歉，查询失败！尊敬的客户，对不起，未查询到相关记录")) {
					saveCallLog(context);
				}
				/*String text = ContextUtil.getContent(context); //很抱歉，查询失败！尊敬的客户，对不起，未查询到相关记录！
				if (text != null && !text.contains("很抱歉，查询失败！尊敬的客户，对不起，未查询到相关记录")) {
					//StringUtil.subStr(stext, etext, text)
					RegexPaserUtil rp1 = new RegexPaserUtil("记录总数:","条",context.toString(),RegexPaserUtil.TEXTEGEXANDNRT);
					String count = rp1.getText().toString();
					int page = Integer.parseInt(count);
					String detail_url = data.getString("detail_url").toString();
					String url = "http://xj.189.cn/EcssTuxedo235.do?curPage=1&pageSize="+page+"&searchDate="+detail_url;
					postUrl(url, null, null, new AbstractProcessorObserver(util, WaringConstaint.XJDX_4) {
						@Override
						public void afterRequest(SimpleObject context) {
							saveCallLog(context);
						}
					});
				}*/
			}
		});
		//默认查询用户的10000条短消息记录
		String url_msg = "http://xj.189.cn/EcssTuxedo235.do?curPage=1&pageSize=10000&searchDate="+bd+"-"+ed+"&number=0000-"+phoneNo+"-mob&callType=&listingType=6";
		postUrl(url_msg, null, null, new AbstractProcessorObserver(util, WaringConstaint.XJDX_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				if (text != null && !text.contains("尊敬的客户，对不起，未查询到相关记录")) {
					saveSmsLog(context);
				}
			}
		});
		
		
		//默认查询用户的10000条记
		String url_flow = "http://xj.189.cn/EcssTuxedo235.do?curPage=1&pageSize=10000&searchDate="+bd+"-"+ed+"&number=0000-"+phoneNo+"-mob&callType=&listingType=7";
		postUrl(url_flow, null, null, new AbstractProcessorObserver(util, WaringConstaint.XJDX_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				String text = ContextUtil.getContent(context);
				//System.out.println(text);
				
				if (text != null && !text.contains("尊敬的客户，对不起，未查询到相关记录")) {
					saveFlow_List(context);
					saveFlow_Bill(context);
				}
			}
		});
	}
	private void saveCallLog(SimpleObject context) {
		try {
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements trs = doc.select("div.kbox").select("table.fyb").select("tr");
			if (trs==null||trs.toString().equals("")) {
				return;
			}
			for (int i = 5; i < trs.size()-2; i++) {
				Elements tds = trs.get(i).select("td");
				if(tds.size()>10){
					String thsj = tds.get(0).select("div").text();//起始时间
					String thsc = tds.get(1).select("div").text().replaceAll("\\s*", "");//通信时长(秒)
					String thlx = StringEscapeUtils.unescapeHtml3(tds.get(2).select("div").text().replaceAll("\\s*", ""));//通信方式
					String dfhm = tds.get(3).select("div").text().replaceAll("\\s*", "");//对方号码
					String thwz = StringEscapeUtils.unescapeHtml3(tds.get(4).select("div").text().replaceAll("\\s*", ""));//通信地点
					String tradeType = StringEscapeUtils.unescapeHtml3(tds.get(5).select("div").text().replaceAll("\\s*", ""));//通信类型
					String basePay = tds.get(6).select("div").text().replaceAll("\\s*", ""); //基本费用
					String longPay = tds.get(7).select("div").text().replaceAll("\\s*", ""); //长途费用
					String zfy = tds.get(10).text().replaceAll("元" ,"").replaceAll("\\s*", "");//实收通信费
			    	DianXinDetail dxDetail = new DianXinDetail();
					dxDetail.setPhone(phoneNo);
					dxDetail.setTradeType(tradeType);
					dxDetail.setRecevierPhone(dfhm);
					dxDetail.setTradeAddr(thwz);					
					dxDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
					dxDetail.setTradeTime(Integer.parseInt(thsc));
					dxDetail.setCallWay(thlx);
					dxDetail.setBasePay(new BigDecimal(basePay));
					dxDetail.setLongPay(new BigDecimal(longPay));
					dxDetail.setAllPay(new BigDecimal(zfy));
					//dxDetail.setOnlinePay(new BigDecimal(text));
					//dxDetail.setTradeWay(text);					
					detailList.add(dxDetail);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void saveSmsLog(SimpleObject context) {
		try {
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements trs = doc.select("div.kbox").select("table.fyb").select("tr");
			if (trs==null||trs.toString().equals("")) {
				return;
			}
			for (int i = 4; i < trs.size()-3; i++) {
				Elements tds = trs.get(i).select("td");
				if(tds.size()>4){
					TelcomMessage obj = new TelcomMessage();
					obj.setPhone(phoneNo);
					obj.setBusinessType(tds.get(0).select("div").text());//业务类型：点对点
					obj.setRecevierPhone(tds.get(1).select("div").text().replaceAll("\\s*", ""));//对方号码
					obj.setSentTime(DateUtils.StringToDate(tds.get(2).select("div").text(), "yyyy-MM-dd HH:mm:ss"));//发送时间
					obj.setAllPay(Doubles.tryParse(tds.get(4).select("div").text().replaceAll("\\s*", "")));//总费用
					messageList.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//流量账单
	private void saveFlow_Bill(SimpleObject context) {
		try {
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements trs = doc.select("div.kbox").select("table.fyb").select("tr");
			
			if (trs==null||trs.toString().equals("")) {
				return;
			}
			
				Elements tds = trs.get(1).select("td");

					try {
						DianXinFlow obj = new DianXinFlow();
						String queryMonth1 = trs.get(3).select("td:eq(0)").text().replace("起止日期：", "").substring(0,7).replace(".", "-");//2014.10.01-2014.10.31
						Date  queryMonth = DateUtils.StringToDate(queryMonth1,"yyyy-MM");
						String dependCycle = trs.get(3).select("td:eq(0)").text().replace("起止日期：", "");
						String  allFlow1 = trs.get(4).select("td:eq(0)").toString();
						String allFlow2 =  new RegexPaserUtil("convertFlow\\(\\\\\"", "\\\\\"\\)",
								allFlow1, RegexPaserUtil.TEXTEGEXANDNRT).getText();
						BigDecimal allFlow = new BigDecimal(allFlow2);
						String  allTime1 = trs.get(4).select("td:eq(1)").toString();
						String allTime2 =  new RegexPaserUtil("convertTime\\(\\\\\"", "\\\\\"\\)",
								allTime1, RegexPaserUtil.TEXTEGEXANDNRT).getText();
						BigDecimal allTime = new BigDecimal(allTime2);
						String allPay1 = trs.get(5).select("td:eq(0)").text().replace("总费用：", "").replace("元", "");
						BigDecimal allPay = new BigDecimal(allPay1);
						
						obj.setPhone(phoneNo);
						obj.setAllFlow(allFlow);
						obj.setAllPay(allPay);
						obj.setAllTime(allTime);
						obj.setDependCycle(dependCycle);
						obj.setQueryMonth(queryMonth);
						flowList.add(obj);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//流量详单
	private void saveFlow_List(SimpleObject context) {
		try {
			Document doc = ContextUtil.getDocumentOfContent(context); 
			Elements trs = doc.select("div.kbox").select("table.fyb").select("tr");
			
			if (trs==null||trs.toString().equals("")) {
				return;
			}
			for (int i = 8; i < trs.size()-3; i++) {
				Elements tds = trs.get(i).select("td");
				String text1 = tds.toString();
				if(tds.size()>6){

					try {
						DianXinFlowDetail obj = new DianXinFlowDetail();
						String time = tds.get(1).select("div").text().replace("/", "-");
						Date beginTime = DateUtils.StringToDate(time, "yyyy-MM-dd HH:mm:ss");
						String tradeTime1 =  new RegexPaserUtil("convertTime\\(\\\\\"", "\\\\\"\\)",
								text1, RegexPaserUtil.TEXTEGEXANDNRT).getText();
						long tradeTime = new Long(tradeTime1);
						String flow1 = new RegexPaserUtil("convertFlow\\(\\\\\"", "\\\\\"\\)",
								text1, RegexPaserUtil.TEXTEGEXANDNRT).getText();
						BigDecimal flow = new BigDecimal(flow1);
						String netType = tds.get(4).text();
						String location = tds.get(5).text();
						BigDecimal fee = new BigDecimal(tds.get(7).text());
						
						obj.setPhone(phoneNo);
						obj.setBeginTime(beginTime);
						obj.setFlow(flow);
						obj.setTradeTime(tradeTime);
						obj.setFee(fee);
						obj.setNetType(netType);
						obj.setLocation(location);
						flowDetailList.add(obj);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		/*for(int i=0; i< 10; i++) {
			System.out.println(i + "=" + (int) (Math.random() * 1000 % 10));
		}
		if (true) {
			return;
		}*/
		String phoneNo = "18199272917";
		String password = "474802";
		
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		XinJiangDianXin dx = new XinJiangDianXin(spider, null, phoneNo, password, "2345", null);
		dx.setTest(true);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		dx.printData();
		/*dx.parseBalanceInfo();
		spider.start();*/
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		//dx.printData();
		

	}
}
