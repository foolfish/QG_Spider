package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
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
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtilTool;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;

/**
 * 山西电信
 * 
 * @author Pat.Liu description:目前测试可以跳过短信验证
 */

public class SHXDianXin extends AbstractDianXinCrawler {

	public SHXDianXin(Spider spider, User user, String phoneNo,
			String password, String authCode, WarningUtil util) {
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
		// spider.getSite().setCharset("utf-8");
	}

	public SHXDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;
		this.util = util;
		// spider.getSite().setCharset("utf-8");
	}

	public SHXDianXin() {
		toStUrl = "&toStUrl=http://sx.189.cn/service/bill/balanceQuery.action";
		shopId = "10007";
		areaName = "山西";
		customField1 = "3";
		customField2 = "06";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCompleteLogin(SimpleObject context) {
		// testLogin();
		String text = ContextUtil.getContent(context);
		// System.out.println("\n\nComlpete\n\n");
		// System.out.println("text:\n" + text);
		getUrl("http://sx.189.cn/service/manage/modifyUserInfo.action", null,
				new AbstractProcessorObserver(util, WaringConstaint.SHXDX_1) {
					public void afterRequest(SimpleObject context) {
						String str = ContextUtil.getContent(context);
						try {
							Document doc = Jsoup.parse(ContextUtil
									.getContent(context));
							Elements eles = doc.getElementsByTag("p");
							if (eles != null) {
								setStatus(STAT_LOGIN_SUC);
								notifyStatus();
								// 个人信息
								parseInfo(doc);
								// 抓取账单
								requestBalance();
								// 抓取详单
								requestService();
							}
							// System.out.println(str);
							else {
								setStatus(STAT_STOPPED_FAIL);
								notifyStatus();
								data.put("errMsg", "登录失败, 请重试！");
							}
						} catch (Exception e) {
							e.printStackTrace();
							setStatus(STAT_STOPPED_FAIL);
							notifyStatus();
							data.put("errMsg", "登录失败, 请重试！");
						}
					}
				});

		// if (text != null
		// && text.indexOf("service/uiss_mobileLogin.do?method=login") >= 0) {
		// setStatus(STAT_LOGIN_SUC);
		// //去拿那个UATicket验证
		// //
		// getUrl("http://sx.189.cn/service/uiss_mobileLogin.do?method=login",
		// // null, null);
		// } else {
		// setStatus(STAT_STOPPED_FAIL);
		// data.put("errMsg", "登录失败, 请重试！");
		// }

	}

	// 抓取账单，由于一下子可以把六个月的全拿出来（json），所以哇哈哈
	protected void requestBalance() {
		getUrl("http://sx.189.cn/service/bill/pie.action?timestamp=%"
				+ new Date().getTime(),
				"http://sx.189.cn/ui/local/nresources/flash/open-flash-chart-new.swf",
				new AbstractProcessorObserver(util, WaringConstaint.SHXDX_5) {
					public void afterRequest(SimpleObject context) {
						saveBalance(context);
					}
				});

		String[][] par = { { "requestFlag", "asynchronism" } };
		// 当月话费存储到账单（就一个话费总额）
		postUrl("http://sx.189.cn/service/bill/realtimeFeeQuery.action",
				"http://sx.189.cn/service/bill/billQuery.action", par,
				new AbstractProcessorObserver(util, WaringConstaint.SHXDX_3) {
					public void afterRequest(SimpleObject context) {
						try {
							DianXinTel dx = new DianXinTel();
							dx.setTeleno(phoneNo);
							JSONObject json = ContextUtil
									.getJsonOfContent(context);
							Document doc = ContextUtil
									.getDocumentOfContent(context);
							String fee = json.get("realtimeFee").toString();
							String date = DateUtilTool.getFirstDay();
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							dx.setcTime(sdf.parse(date));
							dx.setcAllPay(new BigDecimal(fee));
							telList.add(dx);
						} catch (Exception e) {
							// user.setyBalance(new BigDecimal(0.00));
							e.printStackTrace();
						}
					}
				});
	}

	protected void saveBalance(SimpleObject context) {
		if (context == null)
			return;
		JSONObject js = ContextUtil.getJsonOfContent(context);
		try {
			JSONArray result0 = js.getJSONArray("consumeInfoList");
			// 按月循环
			for (int i = 0; i < result0.length(); i++) {
				DianXinTel dx = new DianXinTel();
				dx.setTeleno(phoneNo);
				JSONObject result1 = result0.getJSONObject(i);
				String mon = result1.getString("dealMon");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				dx.setcTime(sdf.parse(mon + "01"));// 月份
				JSONArray result2 = result1.getJSONArray("consumeInfoSet");
				double total = 0;
				for (int j = 0; j < result2.length(); j++) {
					JSONObject result3 = result2.getJSONObject(j);
					int consumeType = Integer.parseInt(result3
							.getString("consumeType"));
					switch (consumeType) {
					case 101:
						dx.setZtcjbf(new BigDecimal(result3
								.getString("consumeFee")));
						total += Double.parseDouble(result3
								.getString("consumeFee"));
						break;// 基本费
					case 102:
						dx.setBdthf(new BigDecimal(result3
								.getString("consumeFee")));
						total += Double.parseDouble(result3
								.getString("consumeFee"));
						break;// 本地通话费
					case 103:
						dx.setMythf(new BigDecimal(result3
								.getString("consumeFee")));
						total += Double.parseDouble(result3
								.getString("consumeFee"));
						break;// 漫游通话费
					case 112:
						dx.setLdxsf(new BigDecimal(result3
								.getString("consumeFee")));
						break;// 来电显示非
					default:
						total += Double.parseDouble(result3
								.getString("consumeFee"));
					}
				}
				dx.setcAllPay(new BigDecimal(total));
				telList.add(dx);
			}
		} catch (Exception e) {
			return;
		}
	}

	protected void requestService() {
		String[][] pairs = new String[][] { { "billDetailType", "2003" },
				{ "billDetailValidate", "true" }, { "billPage", "" },
				{ "currentPhoneNum", phoneNo }, { "endTime", "" },
				{ "startTime", "" } };

		// 先判断一下最大日期
		Date d = new Date();
		List<String> months = DateUtils.getMonths(7, "yyyy-MM");
		for (int i = 0; i < 7; i++) {// 月份循环
			String startTime, endTime;
			endTime = DateUtils.lastDayOfMonth(months.get(i));
			startTime = months.get(i) + "-01";
			requestCallLogService(pairs, "2003", startTime, endTime, 1);
			requestMessLogService(pairs, "2004", startTime, endTime, 1);
			requestFlowLogService(pairs, "2005", startTime, endTime, 1);
		}
	}

	private void requestMessLogService(final String[][] pairs,
			final String type, final String startTime, final String endTime,
			final int i) {
		pairs[2][1] = Integer.toString(i);
		pairs[0][1] = type;
		pairs[4][1] = endTime;
		pairs[5][1] = startTime;
		postUrl("http://sx.189.cn/service/bill/queryDetailBill.action",
				"http://sx.189.cn/service/bill/queryDetailBill.action", pairs,
				new AbstractProcessorObserver(util, WaringConstaint.SHXDX_5) {
					public void afterRequest(SimpleObject context) {
						saveMessLog(context, pairs, type, startTime, endTime, i);
					}
				});
	}
	
	private void requestFlowLogService(final String[][] pairs,
			final String type, final String startTime, final String endTime,
			final int i) {
		pairs[2][1] = Integer.toString(i);
		pairs[0][1] = type;
		pairs[4][1] = endTime;
		pairs[5][1] = startTime;
		postUrl("http://sx.189.cn/service/bill/queryDetailBill.action",
				"http://sx.189.cn/service/bill/queryDetailBill.action", pairs,
				new AbstractProcessorObserver(util, WaringConstaint.SHXDX_7) {
					public void afterRequest(SimpleObject context) {
						saveFlowLog(context, pairs, type, startTime, endTime, i);
					}
				});
	}
	
	protected void saveFlowLog(SimpleObject context, String[][] pairs,
			String type, String startTime, String endTime, int i) {
		if (context == null)
			return;
		JSONObject js = ContextUtil.getJsonOfContent(context);
		try {
			JSONObject result0 = js.getJSONObject("resultBillDetail");
			int totalPage = Integer.parseInt(result0.getString("totalPage"));
			if (totalPage == 0)
				return;
			UUID uuid = UUID.randomUUID();
			
			if (i == 1) {
				String starttime = js.getString("startTime");
				String endtime = js.getString("endTime");
				String nowtime = js.getString("nowTime");
				Date queryMonth = DateUtils.StringToDate(nowtime
						.substring(0, 7).replaceAll("年", "")
						.replaceAll("月", ""), "yyyyMM");

				DianXinFlow dxFlow = new DianXinFlow();
				dxFlow.setId(uuid.toString());
				dxFlow.setPhone(phoneNo);
				dxFlow.setDependCycle(starttime + "--" + endtime);
				dxFlow.setQueryMonth(queryMonth);

				JSONArray resultbill = result0.getJSONArray("items");
				if (resultbill.length() == 0) {
					dxFlow.setAllTime(new BigDecimal("0"));
					dxFlow.setAllPay(new BigDecimal("0.00"));
					dxFlow.setAllFlow(new BigDecimal("0"));
				} else {
					JSONObject item1 = resultbill.getJSONObject(0);
					dxFlow.setAllTime(new BigDecimal(StringUtil
							.flowTimeFormat(item1.getString("totalDuration"))));
					dxFlow.setAllPay(new BigDecimal(item1.getString("totalFee")
							.replaceAll("（元）", "").trim()));
					dxFlow.setAllFlow(new BigDecimal(StringUtil
							.flowFormat(item1.getString("totalFlow"))));
				}
				addFlowBill(dxFlow);
			}

			JSONArray result3 = result0.getJSONArray("items");
			if (result3.length() == 0)
				return;
			for (int j = 0; j < result3.length(); j++) {
				//流量详单
				JSONObject item = result3.getJSONObject(j);
				
				
				DianXinFlowDetail dxFd = new DianXinFlowDetail();
				uuid = UUID.randomUUID();
				dxFd.setId(uuid.toString());
				dxFd.setPhone(phoneNo);
				
				//2014-10-08 13:01:57	3G（EVDO）	无线宽带	省际漫游	--	182KB	0.00（元）
				dxFd.setBeginTime(DateUtils.StringToDate(item.getString("netDatesx").trim(),"yyyy-MM-dd HH:mm:ss"));// 开始时间
				dxFd.setNetType(item.getString("netType").trim());// 通讯类型
				dxFd.setBusiness(item.getString("businessType").trim());//使用业务
				dxFd.setLocation(item.getString("roamType").trim());// 通信地点
				// 上网时长
				String time = item.getString("durationsx").trim();
				if(("--").equals(time)){
					dxFd.setTradeTime(0);
				}else {
					dxFd.setTradeTime(TimeUtil.timetoint(time));
				}
				dxFd.setFlow(new BigDecimal(StringUtil.flowFormat(item.getString("flowUni"))));// 总流量
				dxFd.setFee(new BigDecimal(item.getString("feeFlow").replaceAll("（元）", "").trim()));//费用（元）

				addFlowDetail(dxFd);
			}
			if (totalPage > i)
				requestFlowLogService(pairs, type, startTime, endTime, i + 1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return;
		}

	}
	

	protected void saveMessLog(SimpleObject context, String[][] pairs,
			String type, String startTime, String endTime, int i) {
		if (context == null)
			return;
		JSONObject js = ContextUtil.getJsonOfContent(context);
		try {
			JSONObject result0 = js.getJSONObject("resultBillDetail");
			int totalPage = Integer.parseInt(result0.getString("totalPage"));
			if (totalPage == 0)
				return;
			JSONArray result1 = result0.getJSONArray("items");
			if (result1.length() == 0)
				return;
			for (int j = 0; j < result1.length(); j++) {
				JSONObject item = result1.getJSONObject(j);
				TelcomMessage dxMessage = new TelcomMessage();
				dxMessage.setPhone(phoneNo);
				/**
				 * 短信目前还没记录！而且造出来查询不到！ 数据格式应该和通话很像，已经把item拿出来了
				 * 直接item.get("").toString() 然后set进去 最后messageList.add
				 * **/
				dxMessage.setAllPay(Math.abs(Double.parseDouble(item
						.getString("messageFee"))));
				dxMessage.setRecevierPhone(item.getString("calledAccNbr"));
				if (item.getString("transceiverType").contains("收")
						|| item.getString("transceiverType").contains("接"))
					dxMessage.setBusinessType("接收");
				else
					dxMessage.setBusinessType("发送");
				// dxMessage.setBusinessType(item.getString("transceiverType"));
				String date1 = item.getString("sendDate").replaceAll("T", " ");
				dxMessage.setSentTime(DateUtils.StringToDate(date1,
						"yyyy-MM-dd HH:mm:ss"));

				// dxDetail.setAllPay(new BigD
				// ecimal(item.get("addFee").toString()));
				addMessage(dxMessage);
			}
			if (totalPage > i)
				requestMessLogService(pairs, type, startTime, endTime, i + 1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return;
		}

	}

	/**
	 * <p>
	 * Description:带参数pairs，抓取某月的第i页信息
	 * </p>
	 * **/
	public void requestCallLogService(final String[][] pairs,
			final String type, final String startTime, final String endTime,
			final int i) {
		pairs[2][1] = Integer.toString(i);
		pairs[0][1] = type;
		pairs[4][1] = endTime;
		pairs[5][1] = startTime;
		postUrl("http://sx.189.cn/service/bill/queryDetailBill.action",
				"http://sx.189.cn/service/bill/queryDetailBill.action", pairs,
				new AbstractProcessorObserver(util, WaringConstaint.SHXDX_4) {
					public void afterRequest(SimpleObject context) {
						saveCallLog(context, pairs, type, startTime, endTime, i);
					}
				});
	}

	public void saveCallLog(SimpleObject context, String[][] pairs,
			String type, String startTime, String endTime, int i) {
		if (context == null)
			return;
		JSONObject js = ContextUtil.getJsonOfContent(context);
		try {
			JSONObject result0 = js.getJSONObject("resultBillDetail");
			int totalPage = Integer.parseInt(result0.getString("totalPage"));
			if (totalPage == 0)
				return;
			JSONArray result1 = result0.getJSONArray("items");
			if (result1.length() == 0)
				return;
			for (int j = 0; j < result1.length(); j++) {
				JSONObject item = result1.getJSONObject(j);
				DianXinDetail dxDetail = new DianXinDetail();
				dxDetail.setPhone(phoneNo);
				dxDetail.setcTime(DateUtils.StringToDate(
						item.getString("callDate").toString(),
						"yyyy-MM-dd HH:mm:ss"));
				dxDetail.setAllPay(new BigDecimal(item.get("favFee").toString()));
				dxDetail.setTradeType(item.get("teleType").toString());
				dxDetail.setTradeTime(TimeUtil.timetoint(item
						.getString("durationsx")));
				dxDetail.setCallWay(item.getString("callType").toString());
				dxDetail.setRecevierPhone(item.getString("calledAccNbr")
						.toString());
				dxDetail.setTradeAddr(item.getString("call_address").toString());
				// System.out.println(dxDetail.getRecevierPhone());
				addDetail(dxDetail);
				// detailList.add(dxDetail);
			}
			if (totalPage > i)
				requestCallLogService(pairs, type, startTime, endTime, i + 1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return;
		}

	}

	private String getSpanString(Element e, String tr) {
		try {
			return e.getElementsContainingOwnText(tr).first()
					.nextElementSibling().child(0).text();
		} catch (Exception ex) {
			logger.error("null user info!", ex);
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * <p>
	 * Title: parseInfo
	 * </p>
	 * <p>
	 * Description: 抓取用户余额，并将其保存至user实体中。该方法应放入抓取用户信息的方法体内部，使用同一个user实体
	 * </p>
	 * 
	 * @author Pat.Liu
	 */
	protected void parseInfo(Document doc) {
		// TODO Auto-generated method stub
		Element table = doc.getElementById("form1");
		String str = null;
		if (this.getSpanString(table, "客户名称") == null) {
			// System.out.println("null");
			return;
		}
		user.setRealName(this.getSpanString(table, "客户名称"));
		user.setLoginName(phoneNo);
		user.setIdcard(this.getSpanString(table, "证件号码"));
		str = this.getSpanString(table, "通信地址");
		if (!str.isEmpty())
			user.setAddr(str);
		str = this.getSpanString(table, "邮政编码");
		if (!str.isEmpty())
			user.setAddr(str);
		str = this.getSpanString(table, "性别");
		if (!str.isEmpty())
			user.setSex(str);
		str = this.getSpanString(table, "住宅电话");
		if (!str.isEmpty())
			user.setFixphone(str);
		str = this.getSpanString(table, "mail");
		if (!str.isEmpty())
			user.setEmail(str);
		user.setModifyDate(new Date());
		this.getYue();
		// user
	}

	private void getYue() {
		// TODO Auto-generated method stub
		getUrl("http://sx.189.cn/service/bill/balanceQuery.action", null,
				new AbstractProcessorObserver(util, WaringConstaint.SHXDX_2) {
					public void afterRequest(SimpleObject context) {
						try {
							Document doc = Jsoup.parse(ContextUtil
									.getContent(context));
							Element yue = doc.getElementById("balanceFeeSpan");
							if (!yue.text().isEmpty()) {
								// user.setyBalance(new BigDecimal(yue.text()));
								addPhoneRemain(new BigDecimal(yue.text()));
							} else {
								// user.setyBalance(new BigDecimal(0.00));
								addPhoneRemain(new BigDecimal(0.00));
							}
						} catch (Exception e) {
							// user.setyBalance(new BigDecimal(0.00));
							addPhoneRemain(new BigDecimal(0.00));
							e.printStackTrace();
						}
					}
				});
	}

	public void showImgWhenSendSMS(String phone) {
		data.put("checkVerifyCode", "1");
		String picName = "shx_dx_sms_" + phone + "_"
				+ (int) (Math.random() * 1000) + "3dw";
		try {
			String imgName = saveFile(
					"http://sx.189.cn/authImg?type=2&" + Math.random(),
					"http://sx.189.cn/service/bill/toDetailBill.action", null,
					picName, true);
			data.put("imgName", imgName);
		} catch (Exception e) {
			notifyStatus();
		}
	}

	public void checkVerifyCode(final String userName) {
		saveVerifyCode("shx", userName);
		// goLoginReq(prefix, phone);
		// 1.生成一个request
		// 2.请求完成后的解析
		// 3.加入到Spider的执行队列
	}

	public void sendSmsPasswordForRequireCallLogService(String authValue) {
		String[][] pairs = new String[][] { { "randCode", authValue } };
		String[][] headers = genHeaders();

		postUrl("http://sx.189.cn/service/bill/sendRandomcode.action",
				"http://sx.189.cn/service/bill/toDetailBill.action", null,
				pairs, new AbstractProcessorObserver(util,
						WaringConstaint.SHXDX_5) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							// 仅供测试
							String text = ContextUtil.getContent(context);
							JSONObject json = ContextUtil
									.getJsonOfContent(context);
							if (json != null && !json.isNull("tip")) {
								if (json.getString("tip").indexOf("发送成功") >= 0) {
									setStatus(STAT_SUC);
									notifyStatus();
								} else {
									data.put("errMsg", json.getString("tip"));
									setStatus(STAT_STOPPED_FAIL);
									notifyStatus();
								}
							} else {
								data.put("errMsg", "图片验证码输入错误,动态验证码发送失败,请重新输入");
								setStatus(STAT_STOPPED_FAIL);
								notifyStatus();
							}
						} catch (Exception e) {
							logger.error(
									"sendSmsPasswordForRequireCallLogService",
									e);
							setStatus(STAT_STOPPED_FAIL);
						}
					}
				});
	}

	private String[][] genHeaders() {
		return new String[][] {
				{ "Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" },
				{ "Accept-Encoding", "gzip, deflate" },
				{ "Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3" },
				{ "Cache-Control", "no-cache" },
				{ "Connection", "keep-alive" },
				{ "Content-Type", "text/plain; charset=UTF-8" },
				{ "Pragma", "no-cache" },
				// {"Host","sh.189.cn"},
				// {"Referer","http://sh.189.cn/service/redirect.do?service=detailQuery&tmp="},
				{ "User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0" } };
	}

	public void requestAllService(String duanxin, String tupian) {
		String[][] pairs = new String[][] { { "sRandomCode", duanxin },
				{ "randCode", tupian }, { "requestFlag", "synchronization" },
				{ "cardId", "" } };
		postUrl("http://sx.189.cn/service/bill/validateRandomcode.action",
				"http://sx.189.cn/service/bill/toDetailBill.action", null,
				pairs, null, new AbstractProcessorObserver(util,
						WaringConstaint.SHXDX_5) {
					@Override
					public void afterRequest(SimpleObject context) {
						try {
							boolean suc = false;
							System.out.println(context);
							JSONObject json = ContextUtil
									.getJsonOfContent(context);
							// 如果返回的结果不为JSON，换成HTML或者XML的话采用！
							// if (json == null) {
							// Document doc = ContextUtil
							// .getDocumentOfContent(context);
							// if (doc != null) {
							// Elements es = doc
							// .select("div.resultSuccessInfo");
							// if (es.size() > 0) {
							// data.put("errMsg", es.text());
							// setStatus(STAT_STOPPED_FAIL);
							// return;
							// }
							// }
							// }
							// 验证成功
							if (json != null
									&& !json.isNull("billDetailValidate")) {
								if (json.getString("billDetailValidate")
										.contains("true")) {
									setStatus(STAT_SUC);
									suc = true;
									notifyStatus();
									requestService();
								}
							}
							if (!suc) {
								if (json != null
										&& !json.isNull("billDetailValidate")) {
									data.put("errMsg", json.getString("tip"));
								} else {
									data.put("errMsg", "验证码输入不正确!");
								}
								setStatus(STAT_STOPPED_FAIL);
								notifyStatus();
							}
						} catch (Exception e) {
							logger.info(com.lkb.robot.util.ContextUtil
									.getContent(context));
							logger.error(
									"sendSmsPasswordForRequireCallLogService",
									e);
						}
					}
				});

	}

}
