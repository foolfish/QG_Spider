package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

/**
* <p>Title: QingHaiDianXin</p>
* <p>Description: 青海电信</p>
* <p>Company: QuantGroup</p> 
* @author Jerry Sun
* @date 2014-9-12
*/
/**
* <p>Title: QingHaiDianXin</p>
* <p>Description: </p>
* <p>Company: QuantGroup</p> 
* @author Jerry Sun
* @date 2014-9-15
*/
public class QingHaiDianXin extends AbstractDianXinCrawler {
	
	public QingHaiDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	
	public QingHaiDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		//spider.getSite().setCharset("utf-8");
	}
	
	public QingHaiDianXin() {
		areaName = "青海";
		customField1 = "1";
		customField2 = "29";
		
		toStUrl = "&toStUrl=http://qh.189.cn/service/bill/fee.action?type=resto";
		shopId = "10029";
	}

	/* （非 Javadoc）
	* <p>Title: onCompleteLogin</p>
	* <p>Description: 完成登陆后进行抓取动作的各方法</p>
	* @param context
	* @see com.lkb.thirdUtil.dx.AbstractDianXinCrawler#onCompleteLogin(com.lkb.bean.SimpleObject)
	*/
	@Override
	protected void onCompleteLogin(SimpleObject context) {
		setStatus(STAT_LOGIN_SUC);
		initQueryTicket();	//初始化查询cookie中authtoken的值
		sendValidReq("http://qh.189.cn/service/bill/sendValidReq.action", "http://qh.189.cn/service/bill/fee.action?type=ticket", "1");	//发送短信动态验证码
		requestUserInfoService();	//查询用户信息 + 账户余额
		requestBillInfo();	//查询近六个月的话费账单
		List<String> monthForm = DateUtils.getMonthsNotInclude(6, "yyyy-MM");
		for(int i = 5; i >= 0 ; i--){
			String date = monthForm.get(i);
			String effDate = date+"-01";
			String expDate = DateUtils.lastDayOfMonth(date);
			requestFeeDetailRecord(1, effDate, expDate, "1");	//长途话单
			requestFeeDetailRecord(1, effDate, expDate, "5");	//市话单
			requestFeeDetailRecordForMessage(1, effDate, expDate);	//短信记录
			
			DianXinFlow newBill=new DianXinFlow();
			newBill.setPhone(phoneNo);
			newBill.setId(UUID.randomUUID().toString());
			newBill.setQueryMonth(DateUtils.StringToDate(effDate + " 00:00:00",
					"yyyy-MM-dd HH:mm:ss"));
			newBill.setDependCycle(effDate+"~"+expDate);
			requestFeeDetailRecordForFlow(1,effDate,expDate,newBill,0.0,0.0,0);//流量详单
			flowList.add(newBill);
		}
	}
	
	/* （非 Javadoc）
	* <p>Title: checkVerifyCode</p>
	* <p>Description: 检查是否需要验证码，根据检查结果选择需要执行的方法</p>
	* @param userName
	* @see com.lkb.thirdUtil.AbstractCrawler#checkVerifyCode(java.lang.String)
	*/
	public void checkVerifyCode(final String userName) {   
		saveVerifyCode("qinghai", userName);
    }
	
	/**
	* <p>Title: sendValidReq</p>
	* <p>Description: 发送手机动态验证码</p>
	* @author Jerry Sun
	* @param postUrl	post提交地址
	* @param referer	referer地址
	* @param listType	类型
	*/
	private void sendValidReq(final String postUrl, final String referer, final String listType){
		String[][] pairs = {{"mobileNum", phoneNo}, {"listType", "1"}};
		String[][] headers = {{"X-Requested-With", "XMLHttpRequest"}, {"Accept", "application/json, text/javascript, */*; q=0.01"}, {"Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"}, {"Accept-Language", "zh-cn"}, {"Accept-Encoding", "gzip, deflate"},
				{"Host", "qh.189.cn"}, {"Pragma", "no-cache"}, {"Connection","Keep-alive"}, {"User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; KB974488; rv:11.0) like Gecko"}, {"Referer", "http://qh.189.cn/service/bill/fee.action?type=ticket"}};
		String[] param = new String[4];
		param[0] = "UTF-8";
		param[3] = "text/json";
		
		postUrl(postUrl, referer, null, pairs, headers, new AbstractProcessorObserver(util, WaringConstaint.QHDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				if(content.indexOf("随机码发送成功") == -1)
					logger.error("发送动态验证码失败");
			}
		});
	}
	
	/**
	* <p>Title: requestFeeDetailRecord</p>
	* <p>Description: 抓取通话详单记录</p>
	* @author Jerry Sun
	* @param cPage 查询页数
	* @param effDate	查询起始日期 格式如：2014-07-01
	* @param expDate	查询结束日期 注：不可跨月查询
	* @param operListID 查询类型（1 长途电话 2 市话）
	*/
	private void requestFeeDetailRecord(final int cPage, final String effDate, final String expDate, final String operListID){
		String[][] pairs = {{"currentPage", String.valueOf(cPage)}, {"pageSize", "10"}, {"effDate", effDate}, {"expDate", expDate}, {"serviceNbr", phoneNo}, {"operListID", operListID}, {"pOffrType", "4"}, {"sendSmsFlag", "true"}, {"num", "1"}};
		postUrl("http://qh.189.cn/service/bill/feeDetailrecordList.action", "http://qh.189.cn/service/bill/fee.action?type=ticket", pairs, new AbstractProcessorObserver(util, WaringConstaint.QHDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				int page = cPage;
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				if(content.indexOf("导出查询结果") != -1){
					Document doc = Jsoup.parse(content);
					Elements trs = doc.getElementsByTag("tr");
					if(trs.size() < 0 )
						return;
					for (int i = 1; i < trs.size(); i++) {
						DianXinDetail dxDetail = new DianXinDetail();
						Elements tds = trs.get(i).getElementsByTag("td");
						for (int j = 0; j < tds.size(); j++) {
							String text = tds.get(j).text();
							switch (j) {
							case 1:
								//主叫号码
								dxDetail.setPhone(text);
								break;
							case 3:
								//被叫号码
								dxDetail.setRecevierPhone(text);
								break;
							case 4:
								//通话开始时间
								dxDetail.setcTime(DateUtils.StringToDate(text, "yyyyMMdd HH:mm:ss"));
								break;
							case 5:
								//时长（秒）
								dxDetail.setTradeTime(Integer.parseInt(text));
								break;
							case 6:
								//通话类型
								if(operListID.equals("1")){
									dxDetail.setTradeType("长途");
								}else if(operListID.equals("5"))
									dxDetail.setTradeType("本地");
								break;
							case 7:
								//费用（分）
								DecimalFormat df = new DecimalFormat("#.00");
						        String text_temp = df.format(Double.parseDouble(text)/100);
								dxDetail.setAllPay(new BigDecimal(text_temp));
								break;
							case 8:
								//类型
								dxDetail.setCallWay(text);
								break;
							}
						}
						if(dxDetail != null)
							detailList.add(dxDetail);
					}
					requestFeeDetailRecord(page+1, effDate, expDate, operListID);
				}
			}
		});
	}
	
	/**
	* <p>Title: requestFeeDetailRecordForMessage</p>
	* <p>Description: 抓取短信记录</p>
	* @author Jerry Sun
	* @param cPage
	* @param effDate
	* @param expDate
	*/
	private void requestFeeDetailRecordForMessage(final int cPage, final String effDate, final String expDate){
		String[][] pairs = {{"currentPage", String.valueOf(cPage)}, {"pageSize", "10"}, {"effDate", effDate}, {"expDate", expDate}, {"serviceNbr", phoneNo}, {"operListID", "11"}, {"pOffrType", "4"}, {"sendSmsFlag", "true"}, {"num", "1"}};
		postUrl("http://qh.189.cn/service/bill/feeDetailrecordList.action", "http://qh.189.cn/service/bill/fee.action?type=ticket", pairs, new AbstractProcessorObserver(util, WaringConstaint.QHDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				int page = cPage;
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				if(content.indexOf("导出查询结果") != -1){
					Document doc = Jsoup.parse(content);
					Elements trs = doc.getElementsByTag("tr");
					if(trs.size() < 0 )
						return;
					for (int i = 1; i < trs.size(); i++) {
						TelcomMessage telMes = new TelcomMessage();
						Elements tds = trs.get(i).getElementsByTag("td");
						for (int j = 0; j < tds.size(); j++) {
							String text = tds.get(j).text();
							switch (j) {
							case 1:
								telMes.setPhone(phoneNo);
								break;
							case 3:
								//被叫号码
								telMes.setRecevierPhone(text);
								break;
							case 4:
								//通话开始时间
								telMes.setSentTime(DateUtils.StringToDate(text, "yyyyMMdd HH:mm:ss"));
								break;
							case 6:
								//业务类型
								telMes.setBusinessType(text);
								break;
							case 7:
								//费用（分）
								DecimalFormat df = new DecimalFormat("#.00");
						        String text_temp = df.format(Double.parseDouble(text)/100);
						        telMes.setAllPay(Double.parseDouble(text_temp));
								break;
							}
						}
						if(telMes != null)
							messageList.add(telMes);
					}
					requestFeeDetailRecordForMessage(page+1, effDate, expDate);
				}
			}
		});
	}
	
	/**
	* <p>Title: requestFeeDetailRecordForFlow</p>
	* <p>Description: 抓取流量记录</p>
	* @author Pat.Liu
	* @param cPage
	* @param effDate
	* @param expDate
	*/
	private void requestFeeDetailRecordForFlow(final int cPage, final String effDate, final String expDate,
			final DianXinFlow newBill,final double allFlow,final double allFee,final long allTime){
		String[][] pairs = {{"currentPage", String.valueOf(cPage)}, {"pageSize", "10"}, {"effDate", effDate}, {"expDate", expDate}, {"serviceNbr", phoneNo}, {"operListID", "2"}, {"pOffrType", "4"}, {"sendSmsFlag", "true"}, {"num", "1"}};
		postUrl("http://qh.189.cn/service/bill/feeDetailrecordList.action", "http://qh.189.cn/service/bill/fee.action?type=ticket", pairs, new AbstractProcessorObserver(util, WaringConstaint.QHDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				int page = cPage;
				double fee=0.0,flow=0.0;
				long time=0;
				String content = ContextUtil.getContent(context);
				if(content == null){
					newBill.setAllFlow(new BigDecimal(allFlow));
					newBill.setAllTime(new BigDecimal(allTime));
					newBill.setAllPay(new BigDecimal(allFee));
					if(page>1)
						flowList.add(newBill);
					return;
				}
				if(content.indexOf("导出查询结果") != -1){
					Document doc = Jsoup.parse(content);
					Elements trs = doc.getElementsByTag("tr");
					if(trs.size() < 0 ){
						newBill.setAllFlow(new BigDecimal(allFlow));
						newBill.setAllTime(new BigDecimal(allTime));
						newBill.setAllPay(new BigDecimal(allFee));
						if(page>1)
							flowList.add(newBill);
						return;
					}
					for (int i = 1; i < trs.size(); i++) {
						DianXinFlowDetail flowDetail = new DianXinFlowDetail();
						flowDetail.setId(UUID.randomUUID().toString());
						flowDetail.setPhone(phoneNo);
						Elements tds = trs.get(i).getElementsByTag("td");
						for (int j = 0; j < tds.size(); j++) {
							String text = tds.get(j).text();
							switch (j) {
							case 0:
								//开始时间
								flowDetail.setBeginTime(DateUtils.StringToDate(text, "yyyyMMdd HH:mm:ss"));
								break;
							case 1:
								//上网时长
								flowDetail.setTradeTime(Long.parseLong(text));
								time+=Long.parseLong(text);
								break;
							case 2:
								//流量
								flowDetail.setFlow(new BigDecimal(text));
								flow+=Double.parseDouble(text);
								break;
							case 3:
								//费用（分）
								DecimalFormat df = new DecimalFormat("#.00");
						        String text_temp = df.format(Double.parseDouble(text)/100);
						        flowDetail.setFee(new BigDecimal(text_temp));
						        fee+=Double.parseDouble(text)/100;
								break;
							case 4:
								//费用（分）
								flowDetail.setNetType(text);
								break;
							}
						}
						if(flowDetail != null)
							flowDetailList.add(flowDetail);
						
					}
					requestFeeDetailRecordForFlow(page+1, effDate, expDate,newBill,allFlow+flow,allFee+fee,allTime+time);
				}
			}
		});
	}
	
	/**
	* <p>Title: initQueryTicket</p>
	* <p>Description: 进入话费详单查询页面，在cookie中初始化authtoken的值（若该值不存在，无法发送手机动态验证码）</p>
	* @author Jerry Sun
	*/
	private void initQueryTicket(){
		getUrl("http://qh.189.cn/service/bill/initQueryTicket.action?rnd=" + Math.random(), "http://qh.189.cn/service/bill/fee.action?type=ticket", new AbstractProcessorObserver(util, WaringConstaint.QHDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				String url = StringUtil.subStr("<script type='text/javascript'>location.replace('", "');</script>", content);
				if (StringUtils.isBlank(url.trim())){
					logger.error("erro.....");
					return;
				}
				iterateGetUrl(url);
			}
		});
	}
	
	
	
	/**
	* <p>Title: requestBillBalance</p>
	* <p>Description: 抓取用户余额，并将其保存至user实体中。该方法应放入抓取用户信息的方法体内部，使用同一个user实体</p>
	* @author Jerry Sun
	*/
	private void requestBillBalance(){
		getUrl("http://qh.189.cn/service/bill/resto.action?rnd=" + Math.random(), "http://qh.189.cn/service/bill/fee.action?type=resto", new AbstractProcessorObserver(util, WaringConstaint.QHDX_1){
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content.indexOf("location.replace") != -1){
					String url = StringUtil.subStr("<script type='text/javascript'>location.replace('", "');</script>", content);
					if (StringUtils.isBlank(url.trim())){
						logger.error("青海电信余额查询失败.....");
						return;
					}
					getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.QHDX_1) {
						@Override
						public void afterRequest(SimpleObject context) {
							String content = ContextUtil.getContent(context);
							if(content.indexOf("您的可用余额为") < 0){
								logger.error("青海电信余额查询失败.....");
								return;
							}
							Document doc = Jsoup.parse(content);
							String yue = doc.select("[class=orange]").text();
							if(yue != "" && !"".equals(yue)){
								user.setPhoneRemain(new BigDecimal(yue));
							}
						}
					});
				}else{
					Document doc = Jsoup.parse(content);
					String yue = doc.select("[class=orange]").text();
					if(yue != "" && !"".equals(yue)){
						user.setPhoneRemain(new BigDecimal(yue));
					}
				}
			}
		});
	}
	
	/**
	* <p>Title: requestUserInfoService</p>
	* <p>Description: 进入用户信息查询页，初始化cookie中authtoken的值</p>
	* @author Jerry Sun
	*/
	private void requestUserInfoService(){
		getUrl("http://qh.189.cn/service/manage/showCustInfo.action", "http://qh.189.cn/service/manage/showCustInfo.action", new AbstractProcessorObserver(util, WaringConstaint.QHDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content.indexOf("location.replace") != -1){
					String url = StringUtil.subStr("<script type='text/javascript'>location.replace('", "');</script>", content);
					if (StringUtils.isBlank(url.trim())){
						return;
					}
					iterateGetUrl(url);
				}
			}
		});
		getUserInfo();
		
	}
	
	/**
	* <p>Title: getUserInfo</p>
	* <p>Description: 抓取用户个人基本信息</p>
	* @author Jerry Sun
	*/
	private void getUserInfo(){
		getUrl("http://qh.189.cn/ydtlogin.action?_=" + System.currentTimeMillis(), "http://qh.189.cn/service/manage/showCustInfo.action", new AbstractProcessorObserver(util, WaringConstaint.QHDX_1){
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content.indexOf("custInfo") == -1)
					return;
				try {
					JSONObject jso = new JSONObject(content);
					JSONObject custInfo = (JSONObject) jso.get("customerInfo");
					user.setLoginName(phoneNo);
					user.setUserName(phoneNo);
					user.setPhone(phoneNo);
					user.setEmail(custInfo.getString("email"));
					user.setRealName(custInfo.getString("custName"));
					user.setIdcard(custInfo.getString("cardNum"));
					user.setAddr(custInfo.getString("contactAddr"));
					user.setLoginPassword(custInfo.getString("passWord"));
					requestBillBalance();
				} catch (JSONException e) {
					logger.error("用户个人信息转换json出错", e);
				}
				
			}
		});
	}
	
	/**
	* <p>Title: requestBillInfo</p>
	* <p>Description: 进入账单查询页面，通过页面源码获取最近6个月内话费详单的查询链接</p>
	* @author Jerry Sun
	*/
	private void requestBillInfo(){
		/*
		 * 当月账单（实时话费）
		 */
		getUrl("http://qh.189.cn/service/bill/realTime.action?rnd="+Math.random(), "http://qh.189.cn/service/bill/fee.action?type=realTime", new AbstractProcessorObserver(util, WaringConstaint.QHDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					Document doc = ContextUtil.getDocumentOfContent(context);
					String str = doc.text().trim();
					String tempTime = StringUtil.subStr("截止至", "，", str);
					String allPay = StringUtil.subStr("实时话费为", "元", str);
					String realName = "";
					
					Elements tables = doc.select(".tab_1 m20");
					if(tables.size()>0){
						for (Element table : tables) {
							Elements trs = table.getElementsByTag("tr");
							if(trs.size()>0){
								Element tr = trs.get(1);
								Elements tds = tr.getElementsByTag("td");
								if(tds.size()>0){
									Element td = tds.get(0);
									realName = td.text().trim();
								}
							}
						}
					}
					
					DianXinTel dxt = new DianXinTel();
					dxt.setcAllPay(new BigDecimal(allPay));
					dxt.setcName(realName);
					dxt.setcTime(Calendar.getInstance().getTime());
					dxt.setDependCycle(DateUtils.firstDayOfMonth(tempTime)+"-"+tempTime.replace("年", "-").replace("月", "-").replace("日", ""));
					dxt.setTeleno(phoneNo);
					
					telList.add(dxt);
				}
			}
		});
		/*
		 * 最近6个月内账单
		 */
		getUrl("http://qh.189.cn/service/bill/initQueryBill.action?rnd=" + Math.random(), "http://qh.189.cn/service/bill/fee.action?type=bill", new AbstractProcessorObserver(util, WaringConstaint.QHDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content.indexOf("location.replace") != -1){
					String url = StringUtil.subStr("<script type='text/javascript'>location.replace('", "');</script>", content);
					if (StringUtils.isBlank(url.trim())){
						logger.error("青海电信话费查询失败.....");
						return;
					}
					getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.QHDX_1) {
						@Override
						public void afterRequest(SimpleObject context) {
							String content = ContextUtil.getContent(context);
							if(content != null){
								Document doc = Jsoup.parse(content);
								Element table = doc.select("[class=mt10 transact_tab]").get(0);
								Elements as = table.getElementsByTag("a");
								if(as.size() > 0){
									for (Element a : as) {
										if(a.toString().indexOf("queryBillDetailInfo") != -1){
											String attr = a.attr("onclick");
											attr = StringUtil.subStr("queryBillDetailInfo(", ")", attr);
											String[] splits = attr.split(", ");
											String month = splits[0];
											String acc_id = splits[1];
											requestBillInfoDetail(month, acc_id);
										}
									}
								}
							}
						}
					});
				}else{
					if(content != null){
						Document doc = Jsoup.parse(content);
						Element table = doc.select("[class=mt10 transact_tab]").get(0);
						Elements as = table.getElementsByTag("a");
						if(as.size() > 0){
							for (Element a : as) {
								if(a.toString().indexOf("queryBillDetailInfo") != -1){
									String attr = a.attr("onclick");
									attr = StringUtil.subStr("queryBillDetailInfo(", ")", attr);
									String[] splits = attr.split(", ");
									String month = splits[0];
									String acc_id = splits[1];
									requestBillInfoDetail(month, acc_id);
								}
							}
						}
					}
				}
			}
		});
	}
	
	/**
	* <p>Title: requestBillInfoDetail</p>
	* <p>Description: 抓取每个月的账单详情</p>
	* @author Jerry Sun
	* @param month
	* @param acc_id
	*/
	private void requestBillInfoDetail(final String month, final String acc_id){
		String[][] pairs = {{"month", month}, {"acc_id", acc_id}};
		postUrl("http://qh.189.cn/service/bill/queryBillInfoDetail.action", "http://qh.189.cn/jsp/bill/billInfoDetail.jsp", pairs, new AbstractProcessorObserver(util, WaringConstaint.QHDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				try {
					JSONObject jso = new JSONObject(content);
					String msg = (String) jso.get("msg");
					if(msg.indexOf("对不起") != -1 ){
						return;
					}
					String data = (String) jso.get("data");
					Document doc = Jsoup.parse(data);
					Elements tds = doc.getElementsByTag("td");
					if(tds.size() > 0){
						DianXinTel dxTel = new DianXinTel();
						dxTel.setTeleno(phoneNo);
						for(int i = 0; i < tds.size(); i++){
							String text = tds.get(i).text();
							if(text.indexOf("客户名称") != -1){
								text = text.substring(text.indexOf(":")+1);
								dxTel.setcName(text);
							}else if(text.indexOf("计费周期") != -1){
								text = text.substring(text.indexOf(":")+1);
								dxTel.setDependCycle(text);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
								Date cTime;
								try {
									cTime = sdf.parse(text.substring(0, 7).replace(".", "/"));
									dxTel.setcTime(cTime);
								} catch (ParseException e) {
									logger.error("cTime转换异常", e);
								}
							}else if(text.indexOf("套餐月基本费") != -1){
								String temp = tds.get(i).toString();
								if(temp.indexOf("费用项目") != -1){
									String subStr = StringUtil.subStr("套餐月基本费</b></td>", "</tr>", temp);
									subStr = StringUtil.subStr(">", "</td>", subStr);
									subStr = (!"".equals(subStr))?subStr:"0";
									dxTel.setZtcjbf(new BigDecimal(subStr));
								}
							}else if(text.indexOf("本项小计") != -1){
								text = tds.get(i+1).text();
								text = (!"".equals(text))?text:"0";
								dxTel.setcAllPay(new BigDecimal(text));
							}else if(text.indexOf("本期费用合计") != -1){
								text = text.substring(text.indexOf(":")+1);
							}
							/*
							 * 目前测试账号中暂未出现 本地通话费、来电显示费和漫游通话费
							 */
							dxTel.setBdthf(new BigDecimal(0));
							dxTel.setLdxsf(new BigDecimal(0));
							dxTel.setMythf(new BigDecimal(0));
							
						}
						if(dxTel != null)
							telList.add(dxTel);
					}
				} catch (JSONException e) {
					logger.error("Json转换异常", e);
				}
			}
		});
	}
	
	/**
	* <p>Title: main</p>
	* <p>Description: 账号登陆并抓取数据的本地测试方法</p>
	* @author Jerry Sun
	* @param args
	*/
	public static void main(String[] args) {
		String phoneNo = "18097445987";
		String password = "765422";
		
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		QingHaiDianXin dx = new QingHaiDianXin(spider, null, phoneNo, password, "2345", null);
		dx.setTest(true);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.getData().clear();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		dx.printData();
			
		dx.initQueryTicket();
		spider.start();
//		dx.saveVerifyImage(phoneNo, dx.getRandomCode(), "http://qh.189.cn/service/bill/fee.action?type=ticket");
////		
//		spider.start();
//		DebugUtil.addToCookieStore("qh.189.cn", "cityCode=qh; flag=2; SHOPID_COOKIEID=10029; s_pers=%20s_fid%3D69CA68D61E7CE1E0-0DFD08DC102E7FBE%7C1473904740397%3B; svid=fef4b97a79bdd4236f0d24c22784fedc; userId=201|159207746; dqmhIpCityInfos=%E5%8C%97%E4%BA%AC%E5%B8%82+%E7%94%B5%E4%BF%A1; _gscu_1758414200=0988788762jx3o19; pgv_pvid=6189458048; Hm_lvt_2e629dae6af3fccff6fc5bcc4e9e067e=1409899084; isLogin=logined; .ybtj.189.cn=94E8519437148DB417C7283D79F226B3; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-qh%253D%252526pid%25253D%2525252Fqh%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fqh.189.cn%2525252Fservice%2525252Fbill%2525252Ffee.action%2525253Ftype%2525253Dticket%252526ot%25253DA%252526oi%25253D311%3B; loginStatus=logined; s_cc=true; Hm_lvt_024e4958b87ba93ed27e4571805fbb5a=1410523276,1410576347,1410576361,1410746344; __utma=218313689.1302410054.1410495022.1410588496.1410594678.8; __utmz=218313689.1410495022.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); logintimes=0; lid=971; COM.TYDIC.SSO_AUTH_TOKEN=20622eb5-2aca-4a32-a03f-9f53f9aa1a53; JSESSIONID_PERSONWEB=Gxs5JWGQqVVFJmnlknhnFZjH42Pj9nL8rTWjhJrBKY8Tp0RdpTdZ!-216719225; Hm_lpvt_024e4958b87ba93ed27e4571805fbb5a=1410746344; rand=5234");
//		dx.checkRandomCode(CUtil.inputYanzhengma());
//		spider.start();	
//		DebugUtil.addToCookieStore("qh.189.cn", "COM.TYDIC.SSO_AUTH_TOKEN=673c4469-8644-4ddf-8f81-683266270ceb");
//		dx.sendValidReq("http://qh.189.cn/service/bill/sendValidReq.action", "http://qh.189.cn/service/bill/fee.action?type=ticket", "1");
		dx.requestUserInfoService();
		spider.start();
		
//		dx.requestFeeDetailRecord("3", "2014-07-01", "2014-07-31");
//		spider.start();
	}


}
