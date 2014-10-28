package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
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
 * @author think
 * @date 2014-8-2
 */
public class LiaoNingDianxin extends AbstractDianXinCrawler {
	
	public LiaoNingDianxin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	}
	public LiaoNingDianxin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
	}
	public LiaoNingDianxin() {
		areaName = "辽宁";
		customField1 = "3";
		customField2 = "08";
		toStUrl = "&toStUrl=http://ln.189.cn/group/bill/bill_owed.action";
		shopId = "10005";
	}
	
	//https://uam.ct10000.com/ct10000uam/validateImg.jsp
	public void checkVerifyCode(final String userName) {    
		saveVerifyCode("ln", userName);
    	/*Request req = new Request(getOpenLoginPage());
    	req.putExtra("redirectsEnabled", false);
    	//req.setMethod("POST");
    	//req.putHeader("Referer", "http://ln.ac.10086.cn/login");
    	//req.putHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    	
    	req.addObjservers(new AbstractProcessorObserver(util, WaringConstaint.LNYD_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				data.put("checkVerifyCode","1");
				//data.put("sessionId", ContextUtil.getCookieValue(context, "JSESSIONID"));
				String picName =  "ln_dx_code_" + userName + "_6.jpg";
				try {
					String imgName = saveFile("https://uam.ct10000.com/ct10000uam/validateImg.jsp", "https://uam.ct10000.com/", null, picName, true);
					data.put("imgName", imgName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("error",e);
				}
				//notifyStatus();
			}
		});
    	spider.addRequest(req);*/
    	//printData();
    }
	//
	
	protected void onCompleteLogin(SimpleObject context) {
		requestService(context);
	}
	private void requestService(SimpleObject context) {
		getUserInfo();
		parseBalanceInfo(context);
		/*ContextUtil.getTask(context).addUrl(new AbstractProcessorObserver(util, WaringConstaint.LNDX_6) {
			@Override
			public void afterRequest(SimpleObject context) {
			}
		}, "http://www.ln.10086.cn/my/account/detailquery.xhtml");*/
		requestMonthBillService(context, null);
		Date d = new Date();
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			if (i > 0) {
				
			}
			requestCallLogService(context, cd, dstr);
			requestFlow(context, cd, dstr);
		}
		
		
		//requestCallLogService(context, number, queryDate);
	}
	
	
	private void parseBalanceInfo(SimpleObject context) {
		//http://ln.189.cn/getSessionInfo.action
		String[][] params = {{"Action", "post"}, {"Name", "lulu"}};
		//{"accNbr":"18040212031","TSR_RESULT":"0","TSR_CODE":"0","restFee":24.04,"TSR_MSG":"","queryDay":30,"userName":"周灏","queryDate":"2014年07月30日"}
		postUrl("http://ln.189.cn/chargeQuery/chargeQuery_queryBalanceInfo.action?productType=8&changeUserID="+phoneNo, "http://ln.189.cn/group/bill/bill_owed.action", params, new AbstractProcessorObserver(util, WaringConstaint.LNYD_7){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					JSONObject obj = ContextUtil.getJsonOfContent(context);
					String phoneNum = obj.getString("accNbr");
					String userName = obj.getString("userName");
					String balance = obj.getString("restFee");
					user.setRealName(userName);
					BigDecimal b1= new BigDecimal(NumberUtils.toDouble(balance, 0d));
					user.setPhoneRemain(b1);
					
					data.put("userName", userName);
					data.put("phoneNum", phoneNum);
					data.put("balance", balance);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					logger.error("error",e);
				}
			}
		});
	}	
	
	private void getUserInfo(){
		getUrl("http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo", "http://189.cn/ln/", new AbstractProcessorObserver(util, WaringConstaint.LNDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(ContextUtil.getContent(context) != null){
					String content = ContextUtil.getContent(context);
					String userName = StringUtil.subStr("var head_userName = '", "'+'';", content);
					Document doc = ContextUtil.getDocumentOfContent(context);
					//获取个人基本信息
					Element personalInfo = doc.select("#personalInfo").get(0);
//					Elements tds = personalInfo.getElementsByTag("td");
//					String nickName = personalInfo.select("[name=nickName]").attr("value");	//昵称
//					String phoneNumber = personalInfo.select("[name=phoneNumber]").attr("value");	//联系电话
//					String email = personalInfo.select("[name=email]").attr("value");	//email
//					String realName = personalInfo.select("[name=realName]").attr("value");		//真实姓名
					String certificateNumber = personalInfo.select("[name=certificateNumber]").attr("value");	//身份证号
//					String address = personalInfo.select("[name=address]").attr("value");	//联系地址
					/*for(int i=0; i<tds.size(); i++){
						String e = tds.get(i).toString();
						if(e.indexOf("用&nbsp;户&nbsp;名") != -1){
							String span = tds.get(i+1).toString();
							String loginName = StringUtil.subStr("<span>", "</span>", span);	//用户名
						}
					}*/
					//获取更多个人信息
					Element moreInfo = doc.select("#moreInfo").get(0);
					String brithday = moreInfo.select("[name=brithday]").attr("value");		//生日
					user.setRealName(userName);
					user.setIdcard(certificateNumber);
					user.setBirthday(DateUtils.StringToDate(brithday, "yyyy-MM-dd"));
				}
			}
		
		});
		getUserInfoForCt10000();

		
	}
	
	
	/**
	* <p>Title: getUserInfoForCt10000</p>
	* <p>Description: 从ct10000中获取个人信息</p>
	* @author Jerry Sun
	*/
	private void getUserInfoForCt10000() {  
		String[][] pairs = {{"logonPattern", "2"}, {"userType", "2000004"}, {"productId", phoneNo}, {"loginPwdType", "cellPassword"}, {"pwdType", "01"}, {"userPwd", password}, {"validateCode", ""}, {"serviceNumber", "可不填"}};
		postUrl("http://ln.ct10000.com/self_service/validateLogin.action", "http://ln.ct10000.com/self_service/", pairs, new AbstractProcessorObserver(util, WaringConstaint.LNDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String ssoRequestXml = "";
				try {
					ssoRequestXml = ContextUtil.getJsonOfContent(context).getString("SSORequestXML");
				} catch (JSONException e) {
					logger.error("error",e);
				}
				String[][] pairs = {{"SSORequestXML", ssoRequestXml}};
				postUrl("http://uam.ln.ct10000.com/ffcs-uam/login", "http://ln.ct10000.com/service/", pairs, new AbstractProcessorObserver(util, WaringConstaint.LNDX_1) {
					@Override
					public void afterRequest(SimpleObject context) {
						Integer scode = (Integer) ContextUtil.getRequest(context).getExtra(Request.STATUS_CODE);
						if(HttpUtil.isMovedStatusCode(scode)){
							HttpResponse resp = ContextUtil.getResponse(context);
							Header h1 = resp.getFirstHeader("Location");
							String nexturl = h1.getValue();
							if (nexturl == null) {
								logger.error("Error : No Redirect URL");    		
							} else {
								getUrl(nexturl, "http://ln.ct10000.com/service/", new AbstractProcessorObserver(util, WaringConstaint.LNDX_1) {
									public void afterRequest(SimpleObject context) {
										postUrl("http://ln.ct10000.com/getSessionInfo.action", "http://ln.ct10000.com/group/info/info_view.do", null, new AbstractProcessorObserver(util, WaringConstaint.LNDX_1) {
											public void afterRequest(SimpleObject context) {
												JSONObject json = ContextUtil.getJsonOfContent(context);
												try {
//													json.getString("userName");
													user.setIdcard(json.getString("indentCode"));
													user.setAddr(json.getString("userAddress"));
													user.setPackageName(json.getString("prodName"));
													user.setRegisterDate(DateUtils.StringToDate(json.getString("acceptDate"), "yyyy-MM-dd"));
												} catch (JSONException e) {
													logger.error("error",e);
												}
											};
										});
									};
								});
							}
						}
					}
				});
			}
		});
	}
	
	
	private void requestMonthBillService(SimpleObject context, final String dstr) {
		/*
		 * 当月账单
		 */
		String[][] params = {{"productType", "8"}, {"changeUserID", phoneNo}};
//		{"TSR_RESULT":"0","TSR_CODE":"0","TSR_MSG":"","restFee":"10.29","currNoFavour":"15.71","monthFeeDetailVO":[{"totalFee":"1.87","feeName":"1X/EVDO省际漫游流量费","feeDate":null,"realUnit":0,"discharge":0,"fee14":null,"fee13":null,"callTimes":0,"fee12":null,"fee11":null,"fee3":null,"fee2":null,"fee4":null,"payUnit":0,"fee1":null},{"totalFee":"19.00","feeName":"OCS基本月租费","feeDate":null,"realUnit":0,"discharge":0,"fee14":null,"fee13":null,"callTimes":0,"fee12":null,"fee11":null,"fee3":null,"fee2":null,"fee4":null,"payUnit":0,"fee1":null},{"totalFee":"0.39","feeName":"OCS省际漫游国内固话国内长途被叫费","feeDate":null,"realUnit":0,"discharge":0,"fee14":null,"fee13":null,"callTimes":0,"fee12":null,"fee11":null,"fee3":null,"fee2":null,"fee4":null,"payUnit":0,"fee1":null},{"totalFee":"0.39","feeName":"OCS省际漫游国内网内国内长途被叫费","feeDate":null,"realUnit":0,"discharge":0,"fee14":null,"fee13":null,"callTimes":0,"fee12":null,"fee11":null,"fee3":null,"fee2":null,"fee4":null,"payUnit":0,"fee1":null},{"totalFee":"-5.94","feeName":"OCS月租专款赠送费用","feeDate":null,"realUnit":0,"discharge":0,"fee14":null,"fee13":null,"callTimes":0,"fee12":null,"fee11":null,"fee3":null,"fee2":null,"fee4":null,"payUnit":0,"fee1":null},{"totalFee":"0.00","feeName":"网内及短信中心基本费","feeDate":null,"realUnit":0,"discharge":0,"fee14":null,"fee13":null,"callTimes":0,"fee12":null,"fee11":null,"fee3":null,"fee2":null,"fee4":null,"payUnit":0,"fee1":null}],"creditFee":"0"}
		postUrl("http://ln.189.cn/chargeQuery/chargeQuery_queryRealTimeCharges.action?productType=8&changeUserID="+phoneNo, "http://ln.189.cn/group/bill/bill_owed.action", params, new AbstractProcessorObserver(util, WaringConstaint.LNYD_7){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					JSONObject obj = ContextUtil.getJsonOfContent(context);
					String fee = obj.getString("currNoFavour");
					
					DianXinTel dxt = new DianXinTel();
					//当前月份
					dxt.setcTime(DateUtils.StringToDate(DateUtils.getToday("yyyy/MM"), "yyyy/MM"));
					dxt.setcName(user.getRealName());
					dxt.setTeleno(phoneNo);
					//计费周期
					dxt.setDependCycle(DateUtils.firstDayOfMonth(DateUtils.getToday("yyyy-MM"))+"-"+DateUtils.getToday("yyyy-MM-dd"));
					dxt.setcAllPay(new BigDecimal(!"".equals(fee)?fee:"0"));
					
					telList.add(dxt);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					logger.error("error",e);
				}
			}
		});
		/*
		 * 历史账单
		 */
		getUrl("http://ln.189.cn/chargeQuery/chargeQuery_queryBillForSixMonth.action?queryKind=3", null, new AbstractProcessorObserver(util, WaringConstaint.LNDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					JSONObject obj = ContextUtil.getJsonOfContent(context);
					//"2014年01月` 0.00~2014年02月` 0.00~2014年03月` 0.00~2014年04月` 0.00~2014年05月` 0.00~2014年06月` 0.00"
					String text = obj.getString("monthAccountStr");
					String[] arr1 = text.split("~");
					for(String s1 : arr1) {
						String[] arr2 = s1.split("`");
						BigDecimal b1= new BigDecimal(arr2[1].trim());
						DianXinTel tel = new DianXinTel();
						final String td = arr2[0].trim();
						tel.setcTime(DateUtils.StringToDate(td, "yyyy年MM月"));
						tel.setcAllPay(b1);
						data.put("Month" + td, td);
						data.put("Pay" + td, arr2[1].trim());
						tel.setTeleno(phoneNo);
						getTelList().add(tel);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					logger.error("error",e);
				}
			}
		});
	}
	
	private void requestCallLogService(SimpleObject context, Date d, final String dstr) {		
		//http://ln.189.cn/queryValidateSecondPwdAction.action
		//inventoryVo.accNbr=18040212031&inventoryVo.productId=2000004&inventoryVo.action=generate&inventoryVo.getFlag=isClose
		//http://ln.189.cn/queryCloseSecondValidatePwdAction.action
		//inventoryVo.accNbr=18040212031&inventoryVo.productId=2000004&inventoryVo.action=check&inventoryVo.inputRandomPwd=546867
		//Response TSR_RESULT 0
		//http://ln.189.cn/queryVoiceMsgAction.action?inventoryVo.accNbr=18040212031&inventoryVo.getFlag=3&inventoryVo.begDate=2014-07-01&inventoryVo.endDate=2014-07-30&inventoryVo.family=8&inventoryVo.accNbr97=&inventoryVo.productId=8&inventoryVo.acctName=18040212031&inventoryVo.feeDate=201407
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
		String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		getUrl("http://ln.189.cn/queryVoiceMsgAction.action?inventoryVo.accNbr=" + phoneNo+ "&inventoryVo.getFlag=3&inventoryVo.begDate=" + bd + "&inventoryVo.endDate=" + ed 
				+ "&inventoryVo.family=8&inventoryVo.accNbr97=&inventoryVo.productId=8&inventoryVo.acctName=" + phoneNo + "&inventoryVo.feeDate=" + dstr, 
				null, new AbstractProcessorObserver(util, WaringConstaint.LNDX_4) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, dstr);
			}
		});
//		inventoryVo.accNbr	18040212031
//		inventoryVo.accNbr97	
//		inventoryVo.acctName	18040212031
//		inventoryVo.begDate	2014-09-01
//		inventoryVo.endDate	2014-09-28
//		inventoryVo.family	8
//		inventoryVo.feeDate	201409
//		inventoryVo.getFlag	3
//		inventoryVo.productId	8
//		http://ln.189.cn/mobileInventoryAction.action
//TODO WO YAO 要改的地方		
		getUrl("http://ln.189.cn/mobileInventoryAction.action?inventoryVo.accNbr=" + phoneNo+ "&inventoryVo.getFlag=3&inventoryVo.begDate=" + bd + "&inventoryVo.endDate=" + ed 
				+ "&inventoryVo.family=8&inventoryVo.accNbr97=&inventoryVo.productId=8&inventoryVo.acctName=" + phoneNo + "&inventoryVo.feeDate=" + dstr, 
				null, new AbstractProcessorObserver(util, WaringConstaint.LNDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveMessage(context, dstr);
			}
		});
	}
	//与河北电信解析相同
	private void saveCallLog(SimpleObject context, final String dstr) {
		try {
			JSONObject obj = ContextUtil.getJsonOfContent(context);
			int count = obj.getInt("Count"); 
			if (count < 1) {return;}
			JSONArray arr = obj.getJSONArray("items");
			int len = arr.length();
			for(int i = 0; i < len; i++) {
				JSONObject obj1 = arr.getJSONObject(i);
				DianXinDetail dxDetail = new DianXinDetail();
				dxDetail.setPhone(phoneNo);

				dxDetail.setTradeType(obj1.getString("feeName"));
				dxDetail.setRecevierPhone(obj1.getString("counterNumber"));
				//dxDetail.setTradeAddr(obj1.getString("callType"));					
				dxDetail.setcTime(DateUtils.StringToDate(obj1.getString("callDate"), "yyyy-MM-dd HH:mm:ss"));
				dxDetail.setTradeTime(obj1.getInt("duration"));
				dxDetail.setCallWay(obj1.getString("callType"));
				dxDetail.setAllPay(new BigDecimal(obj1.getString("tollFee")));
				//dxDetail.setOnlinePay(new BigDecimal(text));
				//dxDetail.setTradeWay(text);					
				detailList.add(dxDetail);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("error",e);
		}

	}
	
	//created by qian 短信保存
	//TODO
	private void saveMessage(SimpleObject context, final String dstr) {
		try {
			JSONObject obj = ContextUtil.getJsonOfContent(context);
			
			int count = obj.getInt("Count"); 
			if (count < 1) {return;}
			JSONArray arr = obj.getJSONArray("items");
			int len = arr.length();
			for(int i = 0; i < len; i++) {
				JSONObject obj1 = arr.getJSONObject(i);
				TelcomMessage telcomMessage = new TelcomMessage();
				String businessType = obj1.getString("kind");
				String callPhone =  obj1.getString("callPhone");
				String beginTime = obj1.getString("beginDate");
				String fee = obj1.getString("fee");
				double fees = 0.0;
				try {
					fees = Double.parseDouble(fee);
				} catch (Exception e) {
					logger.error(e.toString());
				}
				telcomMessage.setPhone(phoneNo);
				telcomMessage.setBusinessType(businessType);
				telcomMessage.setCreateTs(new Date());
				telcomMessage.setRecevierPhone(callPhone);
				telcomMessage.setSentTime(DateUtils.StringToDate(beginTime, "yyyy-MM-dd hh:mm:ss"));
				telcomMessage.setAllPay(fees);
				UUID uuid = UUID.randomUUID();
				telcomMessage.setId(uuid.toString());
				//dxDetail.setOnlinePay(new BigDecimal(text));
				//dxDetail.setTradeWay(text);			
				
				
				messageList.add(telcomMessage);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("error",e);
		}

	}
	
	/**
	* <p>Title: requestFlow</p>
	* <p>Description: 流量抓取</p>
	* @author Jerry Sun
	* @param context
	* @param d
	* @param dstr
	*/
	private void requestFlow(SimpleObject context, Date d, final String dstr){
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		final String bd = DateUtils.formatDate(ds[0], "yyyy-MM-dd");
		final String ed = DateUtils.formatDate(ds[1], "yyyy-MM-dd");
		String[][] pairs = {{"inventoryVo.accNbr", phoneNo}, {"inventoryVo.getFlag", "3"}, {"inventoryVo.begDate", bd}, {"inventoryVo.endDate", ed},
				{"inventoryVo.family", "8"}, {"inventoryVo.accNbr97", ""}, {"inventoryVo.productId", "8"}, {"inventoryVo.acctName", phoneNo}, {"inventoryVo.feeDate", dstr}};
		postUrl("http://ln.189.cn/queryCdmaDataMsgListAction.action", "http://ln.189.cn/group/bill/bill_billlist.do", pairs , new AbstractProcessorObserver(util, WaringConstaint.LNDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context != null){
					JSONObject jsonOfContent = ContextUtil.getJsonOfContent(context);
					try {
						JSONObject jsonObject = jsonOfContent.getJSONObject("cdmaDataQueryResp");
						if(jsonObject.getInt("resultCode") == 0){
							/*
							 * 流量账单抓取
							 */
							DianXinFlow dxf = new DianXinFlow();
							String totalDischarge = jsonObject.getString("totalDischarge");
							String totalDuration = jsonObject.getString("totalDuration");
							String totalFee = jsonObject.getString("totalFee").replace("元", "");
							dxf.setAllFlow(new BigDecimal(StringUtil.flowFormat(totalDischarge)));
							dxf.setAllTime(new BigDecimal(StringUtil.flowTimeFormat(totalDuration)));
							dxf.setAllPay(new BigDecimal(totalFee));
							dxf.setDependCycle(bd + "-" + ed);
							dxf.setPhone(phoneNo);
							dxf.setQueryMonth(DateUtils.StringToDate(dstr, "yyyyMM"));
							flowList.add(dxf);
						}
					} catch (JSONException e) {
						logger.error("流量明细数据转换json出错!", e);
					}
					/*
					 * 流量详单抓取
					 */
					try {
						JSONObject jsonObject1 = jsonOfContent.getJSONObject("cdmaDataQueryResp");
						if(!jsonObject1.get("detailGroup").toString().equals("null")&&jsonObject1.get("detailGroup")!=null){
							JSONArray detailGroup = jsonObject1.getJSONArray("detailGroup");
							for(int i=0; i<detailGroup.length(); i++){
								JSONObject jso = (JSONObject) detailGroup.get(i);
								DianXinFlowDetail dxfd = new DianXinFlowDetail();
								String duration = jso.getString("duration");
								String fee = jso.getString("fee").replace("元", "");
								String beginDate = jso.getString("beginDate");
								String discharge = jso.getString("discharge");
								String netType = jso.getString("netType");
								String onlineCity = jso.getString("onlineCity");
								String useService = "".equals(jso.getString("useService"))?"其他":jso.getString("useService");
								dxfd.setTradeTime(StringUtil.flowTimeFormat(duration));
								dxfd.setFee(new BigDecimal(fee));
								dxfd.setBeginTime(DateUtils.StringToDate(beginDate, "yyyy-MM-dd hh:mm:ss"));
								dxfd.setFlow(new BigDecimal(StringUtil.flowFormat(discharge)));
								dxfd.setNetType(netType);
								dxfd.setLocation(onlineCity);
								dxfd.setBusiness(useService);
								dxfd.setPhone(phoneNo);
								flowDetailList.add(dxfd);
							}
						}
					} catch (Exception e) {
						logger.error(e.toString());
					}
				}
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		String phoneNo = "18040212031";
		String password = "880717";
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		LiaoNingDianxin dx = new LiaoNingDianxin(spider, null, phoneNo, password, "2345", null);
		
/*		dx.goLoginReqForCt10000(null, null);
		spider.start();*/
		
		dx.setTest(true);

		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.getData().clear();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		dx.printData();
//		DebugUtil.addToCookieStore(".189.cn", "aactgsh111220=18040212031; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3D%3B; loginStatus=logined; pgv_si=s9847168000; JSESSIONID=B1938F5AA2CAC445586F0B90B19E2D65-n11; __qc__k=; s_pers=%20s_fid%3D24A172B0F1340D0F-15DF17A05FA73892%7C1474624001442%3B; svid=d932c0efeae4801ec54596921beb5a3e; userId=201|161701688; cityCode=ln; SHOPID_COOKIEID=10005; pgv_pvid=1175471162; pgv_pvi=8961281024; isLogin=logined; .ybtj.189.cn=750979CC3C52830ECAE8DDC408DD0029; s_cc=true");
//		DebugUtil.addToCookieStore(".ct10000.com", "JSESSIONID=AA8D01F652CC07714367A8A48FA4FD21-an2");
		
//		dx.getUserInfoForCt10000();
/*		Date d = new Date();
		for(int i = 0; i < 7; i++) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			if (i > 0) {
				
			}
			dx.requestFlow(d, dstr);
			spider.start();
		}*/

	}
}
