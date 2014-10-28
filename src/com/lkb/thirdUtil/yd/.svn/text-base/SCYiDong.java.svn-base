package com.lkb.thirdUtil.yd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.robot.util.ContextUtil;
import com.lkb.serviceImp.MobileOnlineListServiceImpl;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class SCYiDong extends BaseInfoMobile {

	public String index = "https://sc.ac.10086.cn/login/";

	// 验证码图片路径
	public static String imgurl = "https://sc.ac.10086.cn/service/image_login.jsp?t=";

	public static void main(String[] args) throws Exception {
	}

	public SCYiDong(Login login, String currentUser) {
		super(login, ConstantNum.comm_sc_yidong, currentUser);
	}

	// public Map<String,Object> index(){
	// map.put("url",getAuthcode());
	// return map;
	// }
	public void init() {
		if (!isInit()) {
			CHeader h = new CHeader(CHeaderUtil.Accept_, "", "",
					"sc.ac.10086.cn");
			CHeader h1 = new CHeader(CHeaderUtil.Accept_, "", "",
					"www.sc.10086.cn");
			String text = cutil.get(index, h);
			if (text != null) {
				setImgUrl(imgurl + new Date().getTime());
				RegexPaserUtil rp = new RegexPaserUtil("var keyStr =", ";",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				String keyStr = rp.getText().replaceAll("\\s*", "")
						.replaceAll("\\+\"", "").replaceAll("\"", "");
				map.put("keyStr", keyStr);
				rp = new RegexPaserUtil(
						"<input type=\"hidden\" name=\"sid\" value=\"", "\"",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				String sid = rp.getText();
				map.put("sid", sid);
				text = cutil.get("http://www.sc.10086.cn/ssoLogin.do", h1);
				setInit();
			}
			redismap.put("jsmap", map);// 根据实际需要存放
		}
	}

	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
		try {
			parseBegin(Constant.YIDONG);

			Map<String, String> parmap = new HashMap<String, String>(3);
			parmap.put("parentId", currentUser);
			parmap.put("loginName", login.getLoginName());
			parmap.put("usersource", Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(parmap);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());
				user.setRealName(login.getLoginName());
				user.setIdcard("");
				user.setAddr("");
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

				user.setRealName(login.getLoginName());
				user.setIdcard("");
				user.setAddr("");
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
			errorMsg = e.getMessage();
			sendWarningCallHistory(errorMsg);
		} finally {
			parseEnd(Constant.YIDONG);
		}
	}

	public void getTelDetailHtml() {

		try {
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader(CHeaderUtil.Accept_other, "", null,
					"www.sc.10086.cn", true);
			parseBegin(Constant.YIDONG);
			//获取当月账单
			CHeader header = new CHeader("http://www.sc.10086.cn/service/fee/index.jsp?type=2003");
			//http://www.sc.10086.cn/charges.do?reqRand=1413453548825&dispatch=index&typeMain=3&vsn=new
			String firstMonth = cutil.get("http://www.sc.10086.cn/charges.do?reqRand="+System.currentTimeMillis()+"&dispatch=index&typeMain=3&vsn=new", header);
			try {
				Document doc = Jsoup.parse(firstMonth);
				String callPay = doc.select("table").get(0).select("tr").get(1).select("td").get(1).text().toString().replaceAll("元", "").trim();
				BigDecimal callPays = new BigDecimal(0);
				try {
					callPays = new BigDecimal(callPay);
				} catch (Exception e) {
					log.error(e);
				}
				Map map2 = new HashMap();
				map2.put("phone", login.getLoginName());
				map2.put("cTime", DateUtils.StringToDate(DateUtils.formatDate(new Date(), "yyyyMM"), "yyyyMM"));
				List list = mobileTelService.getMobileTelBybc(map2);
				if (list == null || list.size() == 0) {
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					// mobieTel.setBaseUserId(currentUser);
					mobieTel.setcTime(DateUtils.StringToDate(DateUtils.formatDate(new Date(), "yyyyMM"),
							"yyyyMM"));
					mobieTel.setcName("");
					mobieTel.setTeleno(login.getLoginName());
					String year = DateUtils.formatDate(new Date(), "yyyyMM").substring(0, 4);
					String mouth = DateUtils.formatDate(new Date(), "yyyyMM").substring(4, 6);
					mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)))
							+ "至"
							+ TimeUtil.getLastDayOfMonth(
									Integer.parseInt(year), Integer
											.parseInt(DateUtils
													.formatDateMouth(mouth))));
					mobieTel.setcAllPay(callPays);
					
					mobileTelService.saveMobileTel(mobieTel);
				}else {
					updateTel(DateUtils.StringToDate(DateUtils.formatDate(new Date(), "yyyyMM"),
							"yyyyMM"), callPays);
				}
			} catch (Exception e) {
				log.error(DateUtils.formatDate(new Date(), "yyyyMM")+"SCYiDong_bill",e);
			}
			for (String startDate : ms) {

				Map<String, String> param = new HashMap<String, String>();
				param.put("stime", startDate);

				String text = cutil
						.post("http://www.sc.10086.cn/historybill.do?dispatch=getHistoryBill",
								h, param);
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parseEnd(Constant.YIDONG);
		}

	}

	/**
	 * 查询话费记录
	 */
	public boolean getTelDetailHtml_parse(String text, String startDate) {
		boolean b = true;
		
		try {
			if (text != null && text.contains("费用项目") && text.contains("合计")) {
				Document doc6 = Jsoup.parse(text);
				Elements tables = doc6.select("table");
				BigDecimal tcgdf = new BigDecimal(0);
				BigDecimal yytxf = new BigDecimal(0);
				BigDecimal swf = new BigDecimal(0);
				BigDecimal dxf = new BigDecimal(0);
				BigDecimal zzywf = new BigDecimal(0);
				BigDecimal hj = new BigDecimal(0);
				for (int i = 0; i < tables.size(); i++) {
					String tableString = tables.get(i).text();
					if (tableString.contains("费用信息")
							&& tableString.contains("费用项目")) {
						Elements trs = tables.get(i).select("tr");
						for (int j = 1; j < trs.size(); j++) {
							Element tr = trs.get(j);
							String trStr = tr.text();
							if (trStr.contains("套餐及固定费用")) {
								String tcfs = tr.select("td").get(1).text()
										.replaceAll("\\s*", "");
								if (tcfs != null) {
									tcgdf = new BigDecimal(tcfs);
								}
							} else if (trStr.contains("语音通信费")) {
								String yytxfs = tr.select("td").get(1).text()
										.replaceAll("\\s*", "");
								if (yytxfs != null) {
									yytxf = new BigDecimal(yytxfs);
								}
							} else if (trStr.contains("上网费")) {
								String swfs = tr.select("td").get(1).text()
										.replaceAll("\\s*", "");
								if (swfs != null) {
									swf = new BigDecimal(swfs);
								}
							} else if (trStr.contains("短彩信费")) {
								String dxfs = tr.select("td").get(1).text()
										.replaceAll("\\s*", "");
								if (dxfs != null) {
									dxf = new BigDecimal(dxfs);
								}
							} else if (trStr.contains("增值业务费用")) {
								String zzywfs = tr.select("td").get(1).text()
										.replaceAll("\\s*", "");
								if (zzywfs != null) {
									zzywf = new BigDecimal(zzywfs);
								}
							} else if (trStr.contains("合计")) {
								String hjs = tr.select("td").get(1).text()
										.replaceAll("\\s*", "");
								if (hjs != null) {
									hj = new BigDecimal(hjs);
								}
							}
						}
					}

				}
				Map map2 = new HashMap();
				map2.put("phone", login.getLoginName());
				map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
				List list = mobileTelService.getMobileTelBybc(map2);
				if (list == null || list.size() == 0) {
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					// mobieTel.setBaseUserId(currentUser);
					mobieTel.setcTime(DateUtils.StringToDate(startDate,
							"yyyyMM"));
					mobieTel.setcName("");
					mobieTel.setTeleno(login.getLoginName());

					mobieTel.setYdsjllqb(swf);
					mobieTel.setTcgdf(tcgdf);
					mobieTel.setTcwdxf(dxf);
					mobieTel.setZzywf(zzywf);
					// mobieTel.setLdxsf(ldxsf);
					// mobieTel.setMgtjhyf(mgtjhyf);
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)))
							+ "至"
							+ TimeUtil.getLastDayOfMonth(
									Integer.parseInt(year), Integer
											.parseInt(DateUtils
													.formatDateMouth(mouth))));
					mobieTel.setcAllPay(hj);
					mobieTel.setTcwyytxf(yytxf);
					
					mobileTelService.saveMobileTel(mobieTel);
				}else {
					updateTel(DateUtils.StringToDate(startDate,
							"yyyyMM"), hj);
				}

			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
		}

		return b;
	}

	/**
	 * 文本解析 true正常解析 false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次 type
	 * 市话/长途/港澳台/漫游
	 */
	public boolean callHistory_parse(String text, MobileDetail bean,
			String startDate) {
		List<MobileDetail> detailList=new ArrayList<MobileDetail>();
		boolean b = true;
		try {
			if (text != null && text.contains("通话起始时间")) {
				Document doc6 = Jsoup.parse(text);
				Elements tables = doc6.select("table");
				for (int i = 0; i < tables.size(); i++) {
					String tabString = tables.get(i).text();
					if (tabString.contains("通话起始时间")) {
						Elements trs = tables.get(i).select("tr");
						for (int j = 2; j < trs.size(); j++) {
							Elements tds = trs.get(j).select("td");
							if (tds.size() > 9) {
								String qssj = tds.get(0).text();
								String sc = tds.get(1).text()
										.replaceAll("\\s*", "");
								String thlx = tds.get(2).text()
										.replaceAll("\\s*", "");
								String dfhm = tds.get(3).text()
										.replaceAll("\\s*", "");
								String thdd = tds.get(4).text()
										.replaceAll("\\s*", "");
								String ctlx = tds.get(5).text()
										.replaceAll("\\s*", "");
								String jbthf = tds.get(6).text()
										.replaceAll("\\s*", "");
								String ctf = tds.get(7).text()
										.replaceAll("\\s*", "");
								String xxf = tds.get(8).text()
										.replaceAll("\\s*", "");
								String ttyh = tds.get(9).text()
										.replaceAll("\\s*", "");
								Map map2 = new HashMap();
								map2.put("phone", login.getLoginName());
								map2.put("cTime", DateUtils.StringToDate(qssj,
										"yyyy/MM/dd HH:mm:ss"));
								List list = mobileDetailService
										.getMobileDetailBypt(map2);
								if (list == null || list.size() == 0) {
									MobileDetail mDetail = new MobileDetail();
									UUID uuid = UUID.randomUUID();
									mDetail.setId(uuid.toString());
									mDetail.setcTime(DateUtils.StringToDate(
											qssj, "yyyy/MM/dd HH:mm:ss"));
									mDetail.setTradeAddr(thdd);
									if (thlx.contains("主叫")) {
										mDetail.setTradeWay("主叫");
									} else if (thlx.contains("被叫")) {
										mDetail.setTradeWay("被叫");
									}
									int times = 0;
									try {
										TimeUtil tunit = new TimeUtil();
										times = tunit.timetoint(sc);
									} catch (Exception e) {

									}
									mDetail.setRecevierPhone(dfhm);
									mDetail.setTradeTime(times);
									mDetail.setTradeType(ctlx);
									mDetail.setTaocan(ttyh);
									mDetail.setOnlinePay(new BigDecimal(jbthf));
									mDetail.setPhone(login.getLoginName());
									// 判断重复
									if (bean == null)
										detailList.add(mDetail);
//										mobileDetailService
//												.saveMobileDetail(mDetail);
									else if (mDetail.getcTime().after(
											bean.getcTime()))
										detailList.add(mDetail);
//										mobileDetailService
//												.saveMobileDetail(mDetail);
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
		mobileDetailService.insertbatch(detailList);
		return b;
	}

	/**
	 * 查询通话记录
	 */
	public void callHistory() {
		try {
			parseBegin(Constant.YIDONG);
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			String month = (String) jsmap.get("month");
			boolean b = false;
			int num = 0;
			String year = month.substring(0, 4);
			String mouth = month.substring(4, 6);
			String fday = "1";
			// String
			// eday=TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))).split("-")[2];
			String eday = (String) jsmap.get("eday");
			CHeader h = new CHeader(CHeaderUtil.Accept_other, null,
					CHeaderUtil.Content_Type__urlencoded, "www.sc.10086.cn",
					true);
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);

			MobileMessage bean_mes = mobileMessageService.getMaxSentTime(login
					.getLoginName());

			MobileOnlineList bean_onl = mobileOnlineListService.getMaxTime(login
					.getLoginName());
			String text = cutil.get(
					"http://www.sc.10086.cn/bill.do?dispatch=getBillByType&ctype=2&month_begin="
							+ month + "&date_begin=" + fday + "&date_end="
							+ eday + "&call_type=0&sec_pwd="
							+ login.getPhoneCode() + "", h);
			b = callHistory_parse(text, bean, month);
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
				}
			}
			b = messageHistory_parse(text, bean_mes, month);
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
				}
			}
			b = netFlowHistory_parse(text, bean_onl, month);
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
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parseEnd(Constant.YIDONG);
		}

	}
	
	public boolean netFlowHistory_parse(String text, MobileOnlineList bean_mes,
			String month){
			List<MobileOnlineList> mobileOnlineList = new ArrayList<MobileOnlineList>();
			boolean b = true;
			try {
				if (text != null && text.contains("上网详单")) {
					Document doc6 = Jsoup.parse(text);
					Elements tables = doc6.select("table");
					for (int i = 0; i < tables.size(); i++) {
						String tabString = tables.get(i).text();
						if (tabString.contains("GPRS")) {
							Elements trs = tables.get(i).select("tr");
							for (int j = 2; j < trs.size(); j++) {
								Elements tds = trs.get(j).select("td");
								if (tds.size() == 8) {
									String zhujiao = tds.get(0).text(); //主叫号码
									String time = tds.get(2).text();	//起始时间
									String liuliang = tds.get(3).text(); //流量数
									String type = tds.get(1).text(); //类型
									String addr = tds.get(5).text(); //地址
									String feiyong = tds.get(6).text(); //费用
									String taocan = tds.get(7).text(); //套餐优惠
						
									
									
									MobileOnlineList mOnline= new MobileOnlineList();
									UUID uuid = UUID.randomUUID();
									mOnline.setCheapService(taocan);
									mOnline.setCommunicationFees(new BigDecimal(feiyong));
									mOnline.setcTime(DateUtils.StringToDate(
											time, "yyyy/MM/dd HH:mm:ss"));
									mOnline.setId(uuid.toString());
									mOnline.setOnlineTime(new Long(0)); //网上没有办法获取
									mOnline.setOnlineType(type);
									mOnline.setPhone(zhujiao);
									mOnline.setTotalFlow(new Long(liuliang));
									mOnline.setTradeAddr(addr);
									
									// 判断重复
									if (bean_mes == null)
										mobileOnlineList.add(mOnline);
									// mobileMessageService.save(mDetail);
									else if (mOnline.getcTime().after(
											bean_mes.getcTime()))
										mobileOnlineList.add(mOnline);
									// mobileMessageService.save(mDetail);
								}
	
							}
	
						}
	
					}
	
				}
	
			} catch (Exception e) {
				errorMsg = e.getMessage();
				b = false;
			}
			mobileOnlineListService.insertbatch(mobileOnlineList);
			return b;
		
	}
	

		
	private boolean messageHistory_parse(String text, MobileMessage bean_mes,
			String month) {
		List<MobileMessage> messageList = new ArrayList<MobileMessage>();
		boolean b = true;
		try {
			if (text != null && text.contains("短信")) {
				Document doc6 = Jsoup.parse(text);
				Elements tables = doc6.select("table");
				for (int i = 0; i < tables.size(); i++) {
					String tabString = tables.get(i).text();
					if (tabString.contains("被叫")) {
						Elements trs = tables.get(i).select("tr");
						for (int j = 2; j < trs.size(); j++) {
							Elements tds = trs.get(j).select("td");
							if (tds.size() == 5) {
								String zhujiao = tds.get(0).text();
								String beijiao = tds.get(1).text();
								if (zhujiao.contains("主叫")
										|| beijiao.contains("被叫"))
									continue;
								String time = tds.get(2).text();
								String price = tds.get(3).text();
								String nothing = tds.get(4).text();

								MobileMessage mDetail = new MobileMessage();
								UUID uuid = UUID.randomUUID();
								mDetail.setId(uuid.toString());
								mDetail.setSentTime(DateUtils.StringToDate(
										time, "yyyy/MM/dd HH:mm:ss"));
								if (zhujiao.contains(login.getLoginName())) {
									mDetail.setTradeWay("发送");
									mDetail.setRecevierPhone(beijiao);
								} else {
									mDetail.setTradeWay("接收");
									mDetail.setRecevierPhone(zhujiao);
								}
								mDetail.setAllPay(new BigDecimal(price));
								mDetail.setPhone(login.getLoginName());
								// 判断重复
								if (bean_mes == null)
									messageList.add(mDetail);
								// mobileMessageService.save(mDetail);
								else if (mDetail.getSentTime().after(
										bean_mes.getSentTime()))
									messageList.add(mDetail);
								// mobileMessageService.save(mDetail);
							}

						}

					}

				}

			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
		}
		mobileMessageService.insertbatch(messageList);
		return b;
	}

	public BigDecimal getYue() {
		BigDecimal phoneremain = new BigDecimal("0.00");
		CHeader h = new CHeader(CHeaderUtil.Accept_other, "", null,
				"www.sc.10086.cn", true);
		String text = cutil
				.get("http://www.sc.10086.cn/charges.do?dispatch=index&typeMain=3&vsn=new",
						h);
		try {
			if (text != null && text.contains("当前话费余额")) {

				RegexPaserUtil rp = new RegexPaserUtil(
						"<td style=\"position:relative;\"><p><em>",
						"</em>元</p>", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String yue = rp.getText();

				if (yue != null) {
					phoneremain = new BigDecimal(yue.replaceAll("\\s*", ""));
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return phoneremain;
	}

	public String password() {
		ScriptEngineManager manager = new ScriptEngineManager();
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		ScriptEngine engine = manager.getEngineByExtension("js");
		String rsaPath = InfoUtil.getInstance().getInfo("road",
				"tomcatWebappPath")
				+ "/js/yd/sc_rsa.js";
		String keyStr = (String) jsmap.get("keyStr");

		File f = new File(rsaPath);
		FileInputStream fip = null;
		try {
			fip = new FileInputStream(f);

			// 构建FileInputStream对象
			InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
			// 执行指定脚本
			engine.eval(reader);
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				Object c = (Object) invoke.invokeFunction("encode64",
						login.getPassword(), keyStr);
				reader.close();
				return password1(c.toString());

			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public String password1(String pwd) {

		String reqUrl = "https://sc.ac.10086.cn/ssoLogin.do?dispatch=epwd&pwd="
				+ pwd;
		CHeader h = new CHeader(CHeaderUtil.Accept_,
				"https://sc.ac.10086.cn/login/", "", "sc.ac.10086.cn");
		String text = cutil.get(reqUrl, h);
		if (text != null) {
			return text;
		}
		return null;
	}

	public void startSpider() {
		int type = login.getType();
		switch (type) {
		case 1:
			getTelDetailHtml();// 通话记录
			getMyInfo(); // 个人信息
			break;
		case 2:
			
			callHistory(); // 历史账单
			break;
		case 3:
			getTelDetailHtml();// 通话记录
			getMyInfo(); // 个人信息
			
			callHistory(); // 历史账单
			break;
		default:
			break;
		}
	}

	// 首页登录
	public Map<String, Object> login() {
		try {
			String result = login1();
			if (result != null && result.equals("1")) {
				loginsuccess();
			} else if (result != null) {
				errorMsg = result;
			} else {
				errorMsg = "密码或验证码错误.";
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

	public String login1() {
		String pwd = password();
		if (pwd != null) {
			login.setPassword(pwd);
		}
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String url = "http://www.sc.10086.cn/ssoLogin.do";
		CHeader h = new CHeader(CHeaderUtil.Accept_, "",
				CHeaderUtil.Content_Type__urlencoded, "www.sc.10086.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("commend_bunch", "");
		param.put("dispatch", "ssoLogin");
		param.put("dtype", "0");
		param.put("fakecode", login.getAuthcode());
		param.put("loginValue", "SingerLogin");
		param.put("pho_nohd", "");

		param.put("phone_no", login.getLoginName());
		param.put("pswTypeNew", "1");
		param.put("queryEmail", "2");
		param.put("rememberMe", "on");
		param.put("sid", jsmap.get("sid").toString());
		param.put("type_nohd", "");
		param.put("user_passwd", login.getPassword());
		String text = cutil.post(url, h, param);
		if (text.equals("http://www.sc.10086.cn/my/")) {
			CHeader h1 = new CHeader(CHeaderUtil.Accept_, "", "",
					"www.sc.10086.cn");
			text = cutil.get("http://www.sc.10086.cn/my/", h1);
			if (text != null) {
				return "1";
			}
		} else {
			if (text.contains("验证码不正确")) {
				return "验证码不正确";
			} else if (text.contains("输入的密码有误")) {
				return "输入的密码有误";
			} else if (text.contains("请检查手机号或密码与上次登录是否一致")) {
				return "该用户已登录，请检查手机号或密码与上次登录是否一致";
			} else if (text.contains("登录操作过于频繁")) {
				return "由于您登录操作过于频繁，请稍后再试";
			}

		}
		return null;
	}

	/**
	 * 生成短信
	 * */
	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new HashMap<String, Object>();
		String errorMsg = null;
		int status = 0;
		try {
			int days = DateUtils.getDayOfMonth(new Date());
			List<String> ms = DateUtils.getMonths(2, "yyyyMM");
			String month = ms.get(0);
			String fday = "1";
			String eday = "";
			if (days < 25) {
				month = ms.get(1);
				String year = month.substring(0, 4);
				String mouth = month.substring(4, 6);
				eday = TimeUtil.getLastDayOfMonth(Integer.parseInt(year),
						Integer.parseInt(DateUtils.formatDateMouth(mouth)))
						.split("-")[2];
			} else {
				eday = String.valueOf(days);
			}

			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");

			CHeader h = new CHeader(CHeaderUtil.Accept_other, null,
					CHeaderUtil.Content_Type__urlencoded, "www.sc.10086.cn",
					true);
			String text = cutil
					.get("http://www.sc.10086.cn/bill.do?dispatch=getBillRandomPwd&ctype=2&month_begin="
							+ month
							+ "&date_begin="
							+ fday
							+ "&date_end="
							+ eday
							+ "&call_type=0&sec_pwd=%CA%E4%C8%EB%B6%CC%D0%C5%D1%E9%D6%A4%C2%EB",
							h);
			if (text.contains("")) {
				errorMsg = month + "月份短信已发送请查收。";
				jsmap.put("month", month);
				jsmap.put("eday", eday);
				status = 1;
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

	public Map<String, Object> checkPhoneDynamicsCode() {
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String month = (String) jsmap.get("month");
		String year = month.substring(0, 4);
		String mouth = month.substring(4, 6);
		String fday = "1";
		String eday = (String) jsmap.get("eday");
		CHeader h = new CHeader(CHeaderUtil.Accept_other, null,
				CHeaderUtil.Content_Type__urlencoded, "www.sc.10086.cn", true);
		String text = cutil
				.get("http://www.sc.10086.cn/bill.do?dispatch=checkBillByType&Action=get&ctype=2&month_begin="
						+ month
						+ "&date_begin="
						+ fday
						+ "&date_end="
						+ eday
						+ "&call_type=0&sec_pwd=" + login.getPhoneCode() + "",
						h);
		if (text.contains("-111111")) {
			errorMsg = "短信验证码错误";
		} else if (text.contains("000000")) {
			status = 1;
		} else {
			errorMsg = "短信验证码错误";
		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		if (status == 1) {
			addTask_2(this);
		}
		return map;
	}

}
