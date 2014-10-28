package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.lkb.robot.Request;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.util.HttpUtil;
import com.lkb.warning.WarningUtil;


/**
* <p>Title: JLDianxin</p>
* <p>Description: 吉林电信</p>
* <p>Company: QuantGroup</p> 
* @author Jerry Sun
* @date 2014-9-23
*/
public class JLDianxin extends AbstractDianXinCrawler {
	public JLDianxin(Spider spider, User user, String phoneNo, String password,
			String authCode, WarningUtil util) {
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

	public JLDianxin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;
		this.util = util;
		// spider.getSite().setCharset("utf-8");
	}

	public JLDianxin() {
		areaName = "吉林";
		customField1 = "3";
		customField2 = "09";


		toStUrl = "&toStUrl=http://jl.189.cn/service/bill/balanceQuery.action";
		shopId = "10030";
	}

	/*
	 * （非 Javadoc） <p>Title: onCompleteLogin</p> <p>Description:
	 * 完成登陆后进行抓取动作的各方法</p>
	 * 
	 * @param context
	 * 
	 * @see
	 * com.lkb.thirdUtil.dx.AbstractDianXinCrawler#onCompleteLogin(com.lkb.bean
	 * .SimpleObject)
	 */
	@Override
	protected void onCompleteLogin(SimpleObject context) {
		setStatus(STAT_LOGIN_SUC);
		requestAllService();
	}

	/* （非 Javadoc）
	* <p>Title: requestAllService</p>
	* <p>Description: 所有抓取请求（登陆成功后调用）</p>
	* @author Jerry Sun
	* @see com.lkb.thirdUtil.AbstractCrawler#requestAllService()
	*/
	@Override
	public void requestAllService() {
		requestUserInfo();
		requestBillService();
		requestBillDetailService();

	}



	/**
	* <p>Title: requestUserInfo</p>
	* <p>Description: 用户个人信息和余额抓取服务</p>
	* @author Jerry Sun
	*/
	private void requestUserInfo() {
		getUserInfo();
		getBillBalance();
	}

	/**
	* <p>Title: requestBillService</p>
	* <p>Description: 账单明细抓取服务</p>
	* @author Jerry Sun
	*/
	private void requestBillDetailService() {
		getBillDetailPage();
		List<String> months = DateUtils.getMonths(6, "yyyy-MM-dd");
		for(int i=months.size()-1;i>=0;i--){
			String startTime = DateUtils.firstDayOfMonth(months.get(i));
			String endTime = DateUtils.lastDayOfMonth(months.get(i));
			getBillDetailInfo(startTime, endTime, "0", "", "5"); // 短信详单
			getBillDetailInfo(startTime, endTime, "0", "", "0"); // 漫游详单
			getBillDetailInfo(startTime, endTime, "0", "", "1"); // 长途详单
			getBillDetailInfo(startTime, endTime, "0", "", "2"); // 市话详单
			getBillDetailInfo(startTime, endTime, "0", "", "4"); // 流量账单和详单
		}
	}
	
	/**
	* <p>Title: requestBillService</p>
	* <p>Description: 账单抓取服务</p>
	* @author Jerry Sun
	*/
	private void requestBillService(){
		getThisMonthBill();
		getBillPage("http://jl.189.cn/service/bill/toBillQuery.action", "http://www.189.cn/jl/");
		List<String> months = DateUtils.getMonthsNotInclude(6, "yyyyMM");
		for(int i=months.size()-1;i>=0;i--){
			getBillInfo(months.get(i));
		}
	}
	
	/**
	* <p>Title: getThisMonthBill</p>
	* <p>Description: 抓取当月账单</p>
	* @author Jerry Sun
	*/
	private void getThisMonthBill(){
		getUrl("http://jl.189.cn/service/bill/realtimeFeeQuery.action", "http://jl.189.cn/service/bill/realtimeFeeQuery.action", new AbstractProcessorObserver(util, WaringConstaint.JLDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context!=null){
					if(ContextUtil.getContent(context).contains("账单明细")){
						String text = ContextUtil.getDocumentOfContent(context).text().trim();
						String allPay = StringUtil.subStr("总额为", "（单位", text).trim();
						String ztcjbf = StringUtil.subStr("月基本费", "上网及数据通信费 ", text).trim();
						
						DianXinTel dxt = new DianXinTel();
						dxt.setcAllPay(new BigDecimal(allPay));
						dxt.setcName(user.getRealName());
						dxt.setcTime(DateUtils.StringToDate(DateUtils.formatDate(new Date(), "yyyyMM"), "yyyyMM"));
						String date = DateUtils.formatDate(new Date(), "yyyyMM");
						dxt.setDependCycle(date.substring(0, 4) + "-" + date.substring(4, 6) + "-01-" + DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
						dxt.setTeleno(phoneNo);
						dxt.setZtcjbf(new BigDecimal(ztcjbf));
						
						telList.add(dxt);
					}
				}
			}
		});
	}

	/*
	 * （非 Javadoc） <p>Title: checkVerifyCode</p> <p>Description:
	 * 检查是否需要验证码，根据检查结果选择需要执行的方法</p>
	 * 
	 * @param userName
	 * 
	 * @see com.lkb.thirdUtil.AbstractCrawler#checkVerifyCode(java.lang.String)
	 */
	public void checkVerifyCode(final String userName) {
		saveVerifyCode("jilin", userName);
	}

	/**
	 * <p>
	 * Title: getUserInfo
	 * </p>
	 * <p>
	 * Description: 抓取用户个人基本信息
	 * </p>
	 * 
	 * @author Jerry Sun
	 */
	private void getUserInfo() {
		String url = "http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo&rand="+ System.currentTimeMillis();
		getUrl(url, url, new AbstractProcessorObserver(util,WaringConstaint.JLDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				String userName = StringUtil.subStr("var head_userName = '","'+'';", content);
				Document doc = ContextUtil.getDocumentOfContent(context);
				// 获取个人基本信息
				Element personalInfo = doc.select("#personalInfo").get(0);
				// Elements tds = personalInfo.getElementsByTag("td");
				// String nickName =
				// personalInfo.select("[name=nickName]").attr("value"); //昵称
				// String phoneNumber =
				// personalInfo.select("[name=phoneNumber]").attr("value");
				// //联系电话
				// String email =
				// personalInfo.select("[name=email]").attr("value"); //email
				// String realName =
				// personalInfo.select("[name=realName]").attr("value"); //真实姓名
				String certificateNumber = personalInfo.select(
						"[name=certificateNumber]").attr("value"); // 身份证号
				// String address =
				// personalInfo.select("[name=address]").attr("value"); //联系地址
				/*
				 * for(int i=0; i<tds.size(); i++){ String e =
				 * tds.get(i).toString(); if(e.indexOf("用&nbsp;户&nbsp;名") !=
				 * -1){ String span = tds.get(i+1).toString(); String loginName
				 * = StringUtil.subStr("<span>", "</span>", span); //用户名 } }
				 */
				// 获取更多个人信息
				Element moreInfo = doc.select("#moreInfo").get(0);
				String brithday = moreInfo.select("[name=brithday]").attr(
						"value"); // 生日
				user.setRealName(userName);
				entity.put("realName", userName);
				user.setIdcard(certificateNumber);
				user.setBirthday(DateUtils.StringToDate(brithday, "yyyy-MM-dd"));
			}
		});
	}

	/**
	 * <p>
	 * Title: getBillBalance
	 * </p>
	 * <p>
	 * Description: 抓取账户余额
	 * </p>
	 * 
	 * @author Jerry Sun
	 */
	private void getBillBalance() {
		getUrl("http://jl.189.cn/service/bill/balanceQuery.action", "http://www.189.cn/jl/", new AbstractProcessorObserver(util,WaringConstaint.JLDX_2) {
					@Override
					public void afterRequest(SimpleObject context) {
						String content = ContextUtil.getContent(context);
						if (content == null)
							return;
//						if (content.indexOf("location.replace") != -1) {
//							String url = StringUtil.subStr("location.replace('", "');", content);
//							getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.JLDX_2) {
//								@Override
//								public void afterRequest(SimpleObject context) {
//									String content = ContextUtil.getContent(context);
//									if (content == null)
//										return;
//									// 您的余额为<span class="font-ore">13</span>元
//									if (content.indexOf("您的余额为") != -1) {
//										String yue = StringUtil.subStr("您的余额为<span class=\"font-ore\">","</span>元", content);
//										if (!"".equals(yue) && yue != null)
//											user.setPhoneRemain(new BigDecimal(yue));
//									}
//								}
//							});
//						}
						if (content.indexOf("您的余额为") != -1) {
						String yue = StringUtil.subStr("您的余额为<span class=\"font-ore\">","</span>元", content);
						if (!"".equals(yue) && yue != null)
							user.setPhoneRemain(new BigDecimal(yue));
					}
					}
				});
	}
	
	/**
	* <p>Title: getBillPage</p>
	* <p>Description: 进入账单查询页（初始化账单查询cookie）</p>
	* @author Jerry Sun
	* @param url	http://jl.189.cn/service/bill/toBillQuery.action
	* @param referer	http://www.189.cn/jl/
	*/
	private void getBillPage(String url, String referer){
		getUrl(url, referer, new AbstractProcessorObserver(util, WaringConstaint.JLDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if (content == null)
					return;
				Integer scode = (Integer) ContextUtil.getRequest(context).getExtra(Request.STATUS_CODE);
				if (HttpUtil.isMovedStatusCode(scode)){
					Document doc = ContextUtil.getDocumentOfContent(context);
					Elements as = doc.getElementsByTag("a");
					if(as.size() < 0)
						return;
					Element a = as.get(0);
					String href = a.attr("href");
					if(!href.trim().isEmpty()){
						getBillPage(href, null);
					}
				}
			}
		});
	}
	
	/**
	* <p>Title: getBillInfo</p>
	* <p>Description: 抓取账单信息</p>
	* @author Jerry Sun
	* @param billingCycle	例如201408	范围：前六月（不包含当月）
	*/
	private void getBillInfo(final String billingCycle){
		String[][] pairs = {{"billingCycle", billingCycle}};
		postUrl("http://jl.189.cn/service/bill/queryBillInfo.action", null, pairs, new AbstractProcessorObserver(util, WaringConstaint.JLDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				Request req1 = ContextUtil.getRequest(context);
				Integer scode = (Integer) req1.getExtra(Request.STATUS_CODE);
				if (HttpUtil.isMovedStatusCode(scode)) {
					HttpResponse resp = ContextUtil.getResponse(context);
					Header h1 = resp.getFirstHeader("Location");
					String nexturl = h1.getValue();
					if (nexturl == null) {
						logger.error("Error : No Redirect URL");    		
					} else {
						getUrl(nexturl, null, new AbstractProcessorObserver(util, WaringConstaint.JLDX_3) {
							public void afterRequest(SimpleObject context) {
								String url = StringUtil.subStr("location.replace('", "');</script>", ContextUtil.getContent(context));
								if(url != null){
									getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.JLDX_3) {});
								}
							};
						});
					}
				}else{
					if(ContextUtil.getContent(context).contains("没有查询到账单数据"))
						return;
					String itemList = StringUtil.subStr("acctItems\":[{", "}],\"areaCode", ContextUtil.getContent(context));
					String[] splits = itemList.split("\\},\\{");
					if(splits.length>0){
						DianXinTel dxt = new DianXinTel();
						dxt.setcTime(DateUtils.StringToDate(billingCycle, "yyyyMM"));
						dxt.setcName(entity.getString("realName"));
						dxt.setTeleno(phoneNo);
						String date = DateUtils.formatDate(new Date(), "yyyyMM");
						dxt.setDependCycle(date.substring(0, 4) + "-" + date.substring(4, 6) + "-01-" + DateUtils.lastDayOfMonth(date, "yyyyMM", "yyyy-MM-dd"));
						for (String str : splits) {
							String fee = StringUtil.subStr("acctItemFee\":\"", "\",\"acctItemID", str);
							String feeType = StringUtil.subStr("acctItemName\":\"", "\",\"billingID", str);
							
							if("月基本费".equals(feeType))
								dxt.setZtcjbf(new BigDecimal(fee));
							
							String billFee = StringUtil.subStr("billFee\":\"", "\",\"channelID", ContextUtil.getContent(context));
							dxt.setcAllPay(new BigDecimal(billFee));
						}
						telList.add(dxt);
					}
				}
				
			}
		});
	}

	/**
	* <p>Title: getBillDetailPage</p>
	* <p>Description: 进入详单查询页，初始化详单查询功能的cookie</p>
	* @author Jerry Sun
	*/
	private void getBillDetailPage() {
		getUrl("http://jl.189.cn/service/bill/toDetailBill.action", "http://www.189.cn/jl/", new AbstractProcessorObserver(util,WaringConstaint.JLDX_5) {
					@Override
					public void afterRequest(SimpleObject context) {
						String content = ContextUtil.getContent(context);
						if (content == null)
							return;
						// <script
						// type='text/javascript'>location.replace('http://123.173.127.44/UAWeb/servlet/SSOFromCTUAM?UATicket=35001ST--388193-Q9WgdM2uujX6OD4WDRKD-ct10000uam');</script>

						if (content.indexOf("location.replace") != -1) {
							String url = StringUtil.subStr("replace('","');</script>", content);
							if ("".equals(url) || url == null)
								return;
							getUrl(url, null, new AbstractProcessorObserver(util, WaringConstaint.JLDX_5) {
								@Override
								public void afterRequest(SimpleObject context) {
									String content = ContextUtil.getContent(context);
									if (content == null)
										return;
									getUrl("http://jl.189.cn/authImg", null, new AbstractProcessorObserver(util,WaringConstaint.JLDX_5) {
												@Override
												public void afterRequest(
														SimpleObject context) {
												}
											});
								}
							});
						}
					}
				});

	}

	/**
	* <p>Title: getBillDetailInfo</p>
	* <p>Description: 抓取详单数据</p>
	* @author Jerry Sun
	* @param startTime	起始时间 yyyy-MM-dd
	* @param endTime	结束时间 格式同startTime
	* @param currentPage 当前页码（首次查询为0）
	* @param contactID	首次查询为空（之后每次查询由服务器返回）
	* @param billDetailType 详单类型 5：短信	0：漫游	1：长途	2：市话  4:流量
	*/
	private void getBillDetailInfo(final String startTime,
			final String endTime, final String currentPage, String contactID,
			final String billDetailType) {
		String[][] pairs = { { "billDetailValidate", "true" },
				{ "billDetailType", billDetailType },
				{ "startTime", startTime }, { "endTime", endTime },
				{ "pagingInfo.currentPage", currentPage },
				{ "contactID", contactID } };
		postUrl("http://jl.189.cn/service/bill/billDetailQuery.action",
				"http://jl.189.cn/service/bill/toDetailBill.action", pairs,
				new AbstractProcessorObserver(util, WaringConstaint.JLDX_5) {
					@Override
					public void afterRequest(SimpleObject context) {
						String content = ContextUtil.getContent(context);
						if (content != null) {
							JSONObject jobj = ContextUtil.getJsonOfContent(context);
							try {
								entity.put("contactID",jobj.getString("contactID"));
							} catch (JSONException e1) {
								logger.error("error",e1);
							}
							if (content.indexOf("没有查询到详单数据") == -1) {
								try {
									JSONObject jsonObject = jobj.getJSONObject("pagingInfo");
									if (jsonObject == null)
										return;

									entity.put("totalPage",jsonObject.getInt("totalPage"));
									entity.put("currentPage",jsonObject.getInt("currentPage"));

									JSONArray jsonArray = jobj.getJSONArray("items");
									if (jsonArray.length() > 0) {
										switch (Integer.parseInt(billDetailType)) {
										case 5: // 短信详单
											for (int i = 0; i < jsonArray.length(); i++) {
												TelcomMessage telMsg = new TelcomMessage();
												telMsg.setPhone(phoneNo);

												JSONObject job = (JSONObject) jsonArray.get(i);
												String calledAccNbr = job.getString("calledAccNbr");
												String beginTime = job.getString("beginTime");
												String fee = job.getString("fee");

												telMsg.setRecevierPhone(calledAccNbr);
												telMsg.setSentTime(DateUtils.StringToDate(beginTime.replace("T"," "),"yyyy-MM-dd HH:mm:ss"));
												telMsg.setAllPay(Double.parseDouble(fee));
												if (telMsg != null)
													messageList.add(telMsg);
											}
											break;
										case 0: // 漫游详单
											for (int i = 0; i < jsonArray.length(); i++) {
												DianXinDetail dxDetail = new DianXinDetail();
												dxDetail.setPhone(phoneNo);

												JSONObject job = (JSONObject) jsonArray.get(i);
												String callType = job.getString("callType");
												String calledAccNbr = job.getString("calledAccNbr");
												String beginTime = job.getString("beginTime");
												String duration = job.getString("duration"); // 时长
												String fee = job.getString("fee"); // 分
												String roamType = job.getString("roamType").equals("null") ? "漫游": job.getString("roamType"); // 分

												dxDetail.setTradeType(roamType);
												dxDetail.setcTime(DateUtils.StringToDate(beginTime.replace("T"," "),"yyyy-MM-dd HH:mm:ss"));
												dxDetail.setTradeTime(Integer.parseInt(duration));
												dxDetail.setCallWay(callType);
												dxDetail.setRecevierPhone(calledAccNbr);
												dxDetail.setAllPay(new BigDecimal(Double.parseDouble(fee) / 100));

												if (dxDetail != null)
													detailList.add(dxDetail);
											}
											break;
										case 1: // 长途
											for (int i = 0; i < jsonArray.length(); i++) {
												DianXinDetail dxDetail = new DianXinDetail();
												dxDetail.setPhone(phoneNo);

												JSONObject job = (JSONObject) jsonArray.get(i);
												String callType = job.getString("callType");
												String calledAccNbr = job.getString("calledAccNbr");
												String beginTime = job.getString("beginTime");
												String duration = job.getString("duration"); // 时长
												String fee = job.getString("fee"); // 分
												String roamType = job.getString("roamType").equals(null) ? "长途": job.getString("roamType"); // 分

												dxDetail.setTradeType(roamType);
												dxDetail.setcTime(DateUtils.StringToDate(beginTime.replace("T"," "),"yyyy-MM-dd HH:mm:ss"));
												dxDetail.setTradeTime(Integer.parseInt(duration));
												dxDetail.setCallWay(callType);
												dxDetail.setRecevierPhone(calledAccNbr);
												dxDetail.setAllPay(new BigDecimal(Double.parseDouble(fee) / 100));

												if (dxDetail != null)
													detailList.add(dxDetail);
											}
											break;
										case 2: // 市话
											for (int i = 0; i < jsonArray.length(); i++) {
												DianXinDetail dxDetail = new DianXinDetail();
												dxDetail.setPhone(phoneNo);

												JSONObject job = (JSONObject) jsonArray.get(i);
												String callType = job.getString("callType");
												String calledAccNbr = job.getString("calledAccNbr");
												String beginTime = job.getString("beginTime");
												String duration = job.getString("duration"); // 时长
												String fee = job.getString("fee"); // 分
												String roamType = job.getString("roamType").equals(null) ? "市话": job.getString("roamType"); // 分

												dxDetail.setTradeType(roamType);
												dxDetail.setcTime(DateUtils.StringToDate(beginTime.replace("T"," "),"yyyy-MM-dd HH:mm:ss"));
												dxDetail.setTradeTime(Integer.parseInt(duration));
												dxDetail.setCallWay(callType);
												dxDetail.setRecevierPhone(calledAccNbr);
												dxDetail.setAllPay(new BigDecimal(Double.parseDouble(fee) / 100));
												if (dxDetail != null)
													detailList.add(dxDetail);
											}
											break;
										case 4: // 流量
											/*
											 * 流量详单
											 */
											for (int i = 0; i < jsonArray.length(); i++) {
												
												DianXinFlowDetail dxfd = new DianXinFlowDetail();
												dxfd.setPhone(phoneNo);

												JSONObject job = (JSONObject) jsonArray.get(i);
//												String callType = job.getString("callType");
//												String calledAccNbr = job.getString("calledAccNbr");
												String beginTime = job.getString("beginTime");
												String duration = job.getString("duration"); // 时长
												String flow = job.getString("flow"); // 流量
												if(flow.equals("")){
													flow = "0";
												}
												String netType = job.getString("netType"); 
												String city = job.getString("city"); 
												String business = job.getString("business"); 
												String fee = job.getString("fee"); 
												

												dxfd.setBeginTime(DateUtils.StringToDate(beginTime.replace("T", " "), "yyyy-MM-dd hh:mm:ss"));
												dxfd.setTradeTime(StringUtil.flowTimeFormat(duration));
												dxfd.setFlow(new BigDecimal(flow));
												dxfd.setNetType(netType);
												dxfd.setLocation(city);
												dxfd.setBusiness(business);
												dxfd.setFee(new BigDecimal(fee));
												
												if (dxfd != null)
													flowDetailList.add(dxfd);
											}
											
											/*
											 * 流量账单
											 */
											if(Integer.parseInt(currentPage) == 0){
												DianXinFlow dxFlow = new DianXinFlow();
												dxFlow.setPhone(phoneNo);
												dxFlow.setDependCycle(startTime + "-" + endTime);
												dxFlow.setQueryMonth(DateUtils.StringToDate(startTime, "yyyy-MM"));
												dxFlow.setAllFlow(new BigDecimal(StringUtil.subStr("totalList\":\"", "\",\"", content)));
												dxFlow.setAllTime(new BigDecimal(StringUtil.subStr("totalTime\":\"", "\",\"", content)));
												dxFlow.setAllPay(new BigDecimal(Double.parseDouble(StringUtil.subStr("totalFee\":\"", "\",\"", content))/100));
												
												if(dxFlow != null)
													flowList.add(dxFlow);
											}
											break;
										}

									}
									/*
									 * 如果当前查询出的数据页数比总页数小，继续翻页
									 */
									if (entity.getInteger("currentPage") <= entity.getInteger("totalPage")) {
										getBillDetailInfo(startTime,endTime,String.valueOf(entity.getInteger("currentPage") + 1),entity.getString("contactID"),
												billDetailType);
//										entity.put("currentPage", String.valueOf(entity.getInteger("currentPage") + 1));
									}
								} catch (JSONException e) {
									logger.error("Json转换出错", e);
								}
							}

						}
						/*
						 * 查询日期按月叠加抓取，直到加到当月为止
						 */
//						String startTime2 = DateUtils.nextMonthFirstDay(startTime);
//						String endTime2 = DateUtils.nextMonthLastDay(endTime);
//						if (DateUtils.monthIsEqual(startTime2, DateUtils.formatDate(Calendar.getInstance().getTime(),"yyyy-MM-dd")))
//							getBillDetailInfo(startTime2, endTime2, "0",entity.getString("contactID"),billDetailType);
					}
				});
	}


	/**
	 * <p>
	 * Title: main
	 * </p>
	 * <p>
	 * Description: 账号登陆并抓取数据的本地测试方法
	 * </p>
	 * 
	 * @author Jerry Sun
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String phoneNo = "13341578170";
		String password = "281039";

		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		JLDianxin dx = new JLDianxin(spider, null, phoneNo, password, "2345",
				null);
		dx.setTest(true);

		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.getData().clear();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		dx.printData();
		
		dx.requestUserInfo();
		
		spider.start();
		dx.getBillPage("http://jl.189.cn/service/bill/toBillQuery.action", "http://www.189.cn/jl/");
		spider.start();
		dx.getBillInfo("201409");
		spider.start();

		// dx.getDetailBill();
		// DebugUtil.addToCookieStore("jl.189.cn",
		// "aactgsh111220=13341578170; s_pers=%20s_fid%3D24A172B0F1340D0F-15DF17A05FA73892%7C1474594972340%3B; svid=d932c0efeae4801ec54596921beb5a3e; userId=201|20140000000003601830; cityCode=jl; SHOPID_COOKIEID=10030; pgv_pvid=1175471162; isLogin=logined; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3D%3B; loginStatus=logined; .ybtj.189.cn=160DE6E7152FFA862B64FA60A81BCC88; s_cc=true; pgv_pvid=5621047580; JSESSIONID=66402F0D7A50F9453702492416DAC1A7-n4; hasshown=1; __qc__k=");
		// dx.requestBillService();
		// spider.start();
		/*
		 * dx.sendSms(CUtil.inputYanzhengma()); spider.start();
		 */
		// dx.postSmsDetail("0", "");
		// dx.saveFileTest("http://jl.189.cn/authImg?" + Math.random(), null,
		// null, "123123", true);
		// spider.start();
		// dx.sendSms(CUtil.inputYanzhengma());
		// dx.getDetailBill();
		// DebugUtil.addToCookieStore("gz.189.cn",
		// "JSESSIONID=V4yWJZMfn01qWYrLGyQMpnxFwYvpgXmkS3whnYL2T1cmLDQ1v0l2!-1455552722");
		// dx.getUserInfo();
		// dx.getBillBalance();
		// DebugUtil.addToCookieStore("www.189.cn",
		// "JSESSIONID=p3zcJZHD68jyC02NQJCFspGryRLQ1J5TDvsSqRQChJcTpnFMqrFL!-1455552722; loginStatus=logined;  userId=201|20140000000004537810; cityCode=gz;isLogin=logined; .ybtj.189.cn=1B0FBA7090364AD0827ACDD8D2745F94");
		// dx.getBillBalance();
		// dx.getUserInfo();
		// SERV_TYPE=FSE-2&SERV_NO=FSE-2-2&ACC_NBR=18198347389&AREA_CODE=0852&PROD_NO=2339&ACCTNBR97=
		// DebugUtil.addToCookieStore(".189.cn",
		// "s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-gz%253D%252526pid%25253D%2525252Fgz%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fwww.189.cn%2525252Fdqmh%2525252FfrontLink.do%2525253Fmethod%2525253DlinkTo%25252526shopId%2525253D10024%25252526toStUrl%2525253Dhttp%2525253A%2525252F%2525252Fgz.189.cn%2525252Fservice%2525252Fbill%252526ot%25253DA%252526oi%25253D320%3B; loginStatus=logined; s_cc=true; s_pers=%20s_fid%3D24A172B0F1340D0F-15DF17A05FA73892%7C1474183498086%3B; svid=d932c0efeae4801ec54596921beb5a3e; userId=201|20140000000004537810; cityCode=gz; SHOPID_COOKIEID=10024; isLogin=logined; .ybtj.189.cn=1B0FBA7090364AD0827ACDD8D2745F94; JSESSIONID=2RL0JhLKWFrZ8symY2plbc9Ls9l38ymTxs64RynWMB8WSy6SP1nL!-1455552722");
		// dx.getBillDetail("18198347389", "0852", "2339", "");

		// DebugUtil.addToCookieStore(".189.cn",
		// "s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-jl%253D%252526pid%25253D%2525252Fjl%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fjl.189.cn%2525252Fservice%2525252Fbill%2525252FtoDetailBill.action%252526ot%25253DA%252526oi%25253D286%3B; loginStatus=logined; JSESSIONID=CB51338C306418912619CA30633C2ED9-n12; __qc_wId=768; __qc__k=; hasshown=1; s_cc=true; s_pers=%20s_fid%3D24A172B0F1340D0F-15DF17A05FA73892%7C1474528579651%3B; svid=d932c0efeae4801ec54596921beb5a3e; userId=201|20140000000003601830; cityCode=jl; SHOPID_COOKIEID=10030; pgv_pvid=1175471162; isLogin=logined; .ybtj.189.cn=160DE6E7152FFA862B64FA60A81BCC88; JSESSIONID_jl=JjhpJfMX5g4d4Lcrm42krWm2K0RWJCXZ7n0Nvd7j1pLnhLmQywhd!391544918");
		// dx.testPostDetail();
		// spider.start();
	}
}
