package com.lkb.thirdUtil.dx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
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
import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.constant.DxConstant;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.serviceImp.DianXinFlowDetailServiceImpl;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CMapUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.constant.ConstantHC;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

public class SDDianXin extends BaseInfoMobile {

	public String index = "http://sd.189.cn/selfservice/service/login";

	// 验证码图片路径
	public static String imgurl = "http://sd.189.cn/selfservice/validatecode/codeimg.jpg?";

	public static void main(String[] args) throws Exception {
	}

	public SDDianXin(Login login, String currentUser) {
		super(login, ConstantNum.comm_sd_dianxin, currentUser);
	}

	// public Map<String,Object> index(){
	//
	// map.put("url",getAuthcode());
	// return map;
	// }
	public void init() {
		if (!isInit()) {
			String text = cutil.get(index);
			if (text != null) {
				setImgUrl(imgurl);
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
			parseBegin(Constant.DIANXIN);
			Map<String, Object> jsmap = (Map<String, Object>) redismap
					.get("jsmap");
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("parentId", currentUser);
			map1.put("usersource", Constant.DIANXIN);
			map1.put("loginName", login.getLoginName());
			List<User> list = userService.getUserByParentIdSource(map1);

			CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
					"application/json; charset=UTF-8", "sd.189.cn");
			String text = cutil.post(
					"http://sd.189.cn/selfservice/cust/querymanage?100", h,
					"{\"accNbr\":\"" + login.getLoginName()
							+ "\",\"areaCode\":\""
							+ jsmap.get("areaCode").toString()
							+ "\",\"accNbrType\":\"4\"}");

			String idcard = "";
			String addr = "";
			String realname = "";
			if (text.contains("\"resultMsg\":\"查询成功\"")) {
				RegexPaserUtil rp = new RegexPaserUtil("\",\"name\":\"", "\",\"industryClassCd",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				realname = rp.getText();
				rp = new RegexPaserUtil("areaName\":\"", "\",\"upName",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				addr = rp.getText();
				rp = new RegexPaserUtil("indentNbr\":\"", "\",\"servEnsureId",
						text, RegexPaserUtil.TEXTEGEXANDNRT);
				idcard = rp.getText();

			}

			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());
				user.setRealName(realname);
				user.setIdcard(idcard);
				user.setAddr(addr);
				user.setUsersource(Constant.DIANXIN);
				user.setEmail("");
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(login.getLoginName());
				user.setRealName(realname);
				user.setIdcard(idcard);
				user.setAddr(addr);
				user.setUsersource(Constant.DIANXIN);
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
			parseEnd(Constant.DIANXIN);
		}
	}

	public void getTelDetailHtml() {

		try {
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
					"application/json; charset=UTF-8", "sd.189.cn");

			parseBegin(Constant.DIANXIN);
			for (String startDate : ms) {
				String text = cutil.post(
						"http://sd.189.cn/selfservice/bill/queryTwoBill",
						h,
						"{\"valueType\":\"1\",\"value\":\""
								+ login.getLoginName()
								+ "\",\"billingCycle\":\"" + startDate
								+ "\",\"areaCode\":\"" + login.getPhoneCode()
								+ "\",\"queryType\":\"5\",\"proType\":\"4\"}");
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
			parseEnd(Constant.DIANXIN);
		}

	}
	
	/**
	 * 查询话费记录
	 */
	public boolean getTelDetailHtml_parse(String text, String startDate) {
		boolean b = true;
		try {
			if (text.contains("\"resultMsg\":\"成功\"")) {
				JSONObject json = new JSONObject(text);
				String totals = json.getString("total");
				BigDecimal ldxsf = new BigDecimal(0);
				BigDecimal mythf = new BigDecimal(0);
				BigDecimal total = new BigDecimal(0);
				if (totals != null) {
					total = new BigDecimal(totals);
				}
				JSONArray array = json.getJSONArray("items");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonobject = array.getJSONObject(i);
					String name = jsonobject.getString("name");
					if (name != null && name.contains("来电显示费")) {
						String value = jsonobject.getString("value");
						ldxsf = new BigDecimal(value);
					} else if (name != null && name.contains("漫游通话费")) {
						String value = jsonobject.getString("value");
						mythf = new BigDecimal(value);
					}

				}
				Map map2 = new HashMap();
				map2.put("teleno", login.getLoginName());
				map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
				List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
				if (list2 != null && list2.size() > 0) {
					DianXinTel tel = (DianXinTel) list2.get(0);
					tel.setcAllPay(total);
					dianXinTelService.update(tel);
				} else {
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					String fday = TimeUtil.getFirstDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)));
					String eday = TimeUtil.getLastDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)));
					DianXinTel tel = new DianXinTel();
					UUID uuid = UUID.randomUUID();
					tel.setId(uuid.toString());
					// tel.setBaseUserId(currentUser);
					tel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
					tel.setTeleno(login.getLoginName());
					tel.setcAllPay(total);
					tel.setDependCycle(fday + "至" + eday);
					tel.setcName(login.getLoginName());
					tel.setLdxsf(ldxsf);
					tel.setMythf(mythf);
					dianXinTelService.saveDianXinTel(tel);
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
	public boolean callHistory_parse(String text, DianXinDetail bean,
			String startDate) {
		List<DianXinDetail> detailList = new ArrayList<DianXinDetail>();
		boolean b = true;
		try {
			if (text != null && text.contains("\"resultMsg\":\"成功\"")) {
				JSONObject json1 = new JSONObject(text);
				JSONArray array = json1.getJSONArray("items");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonobject = array.getJSONObject(i);
					String thdd = jsonobject.getString("position");
					String qssj = jsonobject.getString("startTime");
					String thsc = jsonobject.getString("duration");
					String fy = jsonobject.getString("charge");
					String dfhm = jsonobject.getString("callingNbr");
					String callWay = jsonobject.getString("callType");
					String thlb = jsonobject.getString("eventType");

					Map map2 = new HashMap();
					map2.put("phone", login.getLoginName());
					map2.put("cTime",
							DateUtils.StringToDate(qssj, "yyyy-MM-dd HH:mm:ss"));
					List list = dianXinDetailService.getDianXinDetailBypt(map2);
					if (list == null || list.size() == 0) {
						DianXinDetail dxDetail = new DianXinDetail();
						if (thlb.contains("本地")) {
							dxDetail.setTradeType("本地");
						} else {
							dxDetail.setTradeType("漫游");
						}
						UUID uuid = UUID.randomUUID();
						dxDetail.setId(uuid.toString());

						dxDetail.setcTime(DateUtils.StringToDate(qssj,
								"yyyy-MM-dd HH:mm:ss"));
						if (bean != null) {
							if (bean.getcTime().getTime() >= dxDetail
									.getcTime().getTime()) {
								return false;
							}
						}

						dxDetail.setTradeTime(Integer.parseInt(thsc));
						dxDetail.setTradeAddr(thdd);
						dxDetail.setCallWay(callWay);
						dxDetail.setRecevierPhone(dfhm);
						dxDetail.setAllPay(new BigDecimal(fy));
						dxDetail.setPhone(login.getLoginName());

						detailList.add(dxDetail);
						// dianXinDetailService.saveDianXinDetail(dxDetail);
					}
				}
			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
		}
		dianXinDetailService.insertbatch(detailList);
		return b;
	}

	/**
	 * 查询通话记录
	 */
	public void callHistory() {
		try {

			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
					"application/json; charset=UTF-8", "sd.189.cn");
			boolean b = false;
			int num = 0;

			parseBegin(Constant.DIANXIN);
			DianXinDetail bean = new DianXinDetail(login.getLoginName());
			bean = dianXinDetailService.getMaxTime(bean);
			TelcomMessage bean_mes = telcomMessageService.getMaxSentTime(login
					.getLoginName());
			DianXinFlow bean_flow = null;
			try {
				bean_flow = dianXinFlowService.getMaxFlowTime(login.getLoginName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DianXinFlowDetail bean_flowdetail = null;
			try {
				bean_flowdetail = dianXinFlowDetailService.getMaxTime(new DianXinFlowDetail(login.getLoginName()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String startDate : ms) {

				String text = cutil.post(
						"http://sd.189.cn/selfservice/bill/checkSms", h, "");

				if (text.contains("{\"flag\":\"1\"}")) {
					text = cutil
							.post("http://sd.189.cn/selfservice/bill/queryBillDetailNum",
									h, "{\"accNbr\":\"" + login.getLoginName()
											+ "\",\"billingCycle\":\""
											+ startDate
											+ "\",\"ticketType\":\"0\"}");

					if (text != null && text.contains("\"resultMsg\":\"成功\"")) {
						JSONObject json = new JSONObject(text);
						String records = json.getString("records");
						if (!"0".equals(records)) {
							text = cutil
									.post("http://sd.189.cn/selfservice/bill/queryBillDetail",
											h,
											"{\"accNbr\":\""
													+ login.getLoginName()
													+ "\",\"billingCycle\":\""
													+ startDate
													+ "\",\"pageRecords\":\""
													+ records
													+ "\",\"pageNo\":\"1\",\"qtype\":\"0\",\"totalPage\":\"1\",\"queryType\":\"6\"}");
							b = callHistory_parse(text, bean, startDate);
							if (!b) {
								// 异常信息
								if (errorMsg != null) {
									num++;
									// 超过五次,发送错误信息,123
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
				}

			}
			for (String startDate : ms) {
				String text1 = cutil.post(
						"http://sd.189.cn/selfservice/bill/checkSms", h, "");

				if (text1.contains("{\"flag\":\"1\"}")) {
					text1 = cutil
							.post("http://sd.189.cn/selfservice/bill/queryBillDetailNum",
									h, "{\"accNbr\":\"" + login.getLoginName()
											+ "\",\"billingCycle\":\""
											+ startDate
											+ "\",\"ticketType\":\"1\"}");

					if (text1 != null && text1.contains("\"resultMsg\":\"成功\"")) {
						JSONObject json1 = new JSONObject(text1);
						String records1 = json1.getString("records");
						if (!"0".equals(records1)) {
							text1 = cutil
									.post("http://sd.189.cn/selfservice/bill/queryBillDetail",
											h,
											"{\"accNbr\":\""
													+ login.getLoginName()
													+ "\",\"billingCycle\":\""
													+ startDate
													+ "\",\"pageRecords\":\""
													+ records1
													+ "\",\"pageNo\":\"1\",\"qtype\":\"1\",\"totalPage\":\"1\",\"queryType\":\"6\"}");
							b = messageHistory_parse(text1, bean_mes, startDate);
							if (!b) {
								// 异常信息
								if (errorMsg != null) {
									num++;
									// 超过五次,发送错误信息,123
									if (num > 5) {
										// 发送错误信息通知
										sendWarningMessageHistory(errorMsg);
										// sendWarningCallHistory(errorMsg);
									}
									// 错误
									errorMsg = null;
								} else {
									break;// 数据库中已有数据
								}
							}
						}
					}
				}
			}
			/*
			 * 流量详单与账单
			 * 在详单中解析账单
			 * @author li
			 */
			for (int k = ms.size()-1 ;k>=0;k--) {
				String startDate = (String)ms.get(k) ;
				String text = cutil.post(
						"http://sd.189.cn/selfservice/bill/checkSms", h, "");

				if (text.contains("{\"flag\":\"1\"}")) {
					text = cutil
							.post("http://sd.189.cn/selfservice/bill/queryBillDetailNum",
									h, "{\"accNbr\":\"" + login.getLoginName()
											+ "\",\"billingCycle\":\""
											+ startDate
											+ "\",\"ticketType\":\"3\"}");
					if (text != null && text.contains("\"resultMsg\":\"成功\"")) {
						JSONObject json = new JSONObject(text);
						String records = json.getString("records");
						if (!"0".equals(records)) {
							text = cutil
									.post("http://sd.189.cn/selfservice/bill/queryBillDetail",
											h,
											"{\"accNbr\":\""
													+ login.getLoginName()
													+ "\",\"billingCycle\":\""
													+ startDate
													+ "\",\"pageRecords\":\""
													+ records
													+ "\",\"pageNo\":\"1\",\"qtype\":\"3\",\"totalPage\":\"1\",\"queryType\":\"6\"}");
							try {
								/*
								 * k用来标示是否为本月
								 */
								b = flowdetail_parse(text,k);
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								b = flowbill_parse(text);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (!b) {
								// 异常信息
								if (errorMsg != null) {
									num++;
									// 超过五次,发送错误信息,123
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
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			parseEnd(Constant.DIANXIN);
		}
	}
	//流量详单
		private boolean flowdetail_parse(String text,int iscm1) {
			 
			
			List<DianXinFlowDetail> flowdetailList = new ArrayList<DianXinFlowDetail>();
			
			Date lastTime = null;
			try{
				DianXinFlowDetail detail = new DianXinFlowDetail(login.getLoginName());
				if(dianXinFlowDetailService.getMaxTime(detail)!=null) {
					lastTime = dianXinFlowDetailService.getMaxTime(detail).getBeginTime();	
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}	
		
			boolean b = true;
			try {
				if (text != null && text.contains("\"resultMsg\":\"成功\"")) {
					JSONObject json1 = new JSONObject(text);
					
					//流量详单
					try {
						JSONArray array = json1.getJSONArray("items");
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonobject = array.getJSONObject(i);
							long tradeTime = 0;
							BigDecimal flow = new BigDecimal(0);
							String netType = null;
							String location = null;
							String business = null;
							BigDecimal fee = null;
							Date nowDate = null;
							int iscm = 0;
							try {
								String beginTime = jsonobject.getString("startTime");
								tradeTime = new Long(jsonobject.getString("duration").replace("秒",""));
								flow = new BigDecimal(StringUtil.flowFormat(jsonobject.getString("flow")));
								netType = jsonobject.getString("netType");
								location = jsonobject.getString("position");
								business = jsonobject.getString("eventType");
								fee = new BigDecimal(jsonobject.getString("charge")); 
								nowDate = DateUtils.StringToDate(beginTime,
										"yyyy-MM-dd HH:mm:ss");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// 增量判断
								
							if(lastTime!=null&&nowDate!=null){
								if(nowDate.getTime()<=lastTime.getTime()){
									continue;
								}	
							}	
							
							DianXinFlowDetail dxFlowdetail = new DianXinFlowDetail();
							UUID uuid = UUID.randomUUID();
							dxFlowdetail.setId(uuid.toString());
							dxFlowdetail.setPhone(login.getLoginName());
							try {
								dxFlowdetail.setBeginTime(nowDate);
								dxFlowdetail.setTradeTime(tradeTime);
								dxFlowdetail.setFlow(flow);
								dxFlowdetail.setNetType(netType);
								dxFlowdetail.setBusiness(business);
								dxFlowdetail.setLocation(location);
								dxFlowdetail.setFee(fee);
								if(iscm1==0) {
									iscm = 1;
								}
								dxFlowdetail.setIscm(iscm);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							flowdetailList.add(dxFlowdetail);
							}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				errorMsg = e.getMessage();
				b = false;
			}
				dianXinFlowDetailService.insertbatch(flowdetailList);
		
			return b;
		}
	
	//流量账单
	private boolean flowbill_parse(String text) {
//		List<DianXinFlow> flowList = new ArrayList<DianXinFlow>();
		Date lastBillTime = null;
		 try {
			 if(dianXinFlowService.getMaxFlowTime(login.getLoginName())!= null)
			 lastBillTime = dianXinFlowService.getMaxFlowTime(login.getLoginName()).getQueryMonth();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		boolean b = true;
		try {
			if (text != null && text.contains("\"resultMsg\":\"成功\"")) {
				JSONObject json1 = new JSONObject(text);
				//流量账单
				try {
					String dependCycle = json1.getString("billCycle");
					String queryMonth1 = json1.getString("billCycle").substring(0, 7).replace(".", "-");
					Date queryMonth = DateUtils.StringToDate(queryMonth1,"yyyy-MM");
					BigDecimal allFlow = new BigDecimal(json1.getString("totalFlow"));
					allFlow = allFlow.multiply(BigDecimal.valueOf(1024));  //转换为kb单位
					BigDecimal allPay = new BigDecimal(json1.getString("totalFee"));
					//增量保存
					if(lastBillTime==null) {
						DianXinFlow obj = new DianXinFlow();
						obj.setPhone(login.getLoginName());
						UUID uuid = UUID.randomUUID();
						obj.setId(uuid.toString());
						try {
							obj.setDependCycle(dependCycle);
							obj.setAllFlow(allFlow);
							obj.setAllPay(allPay);
							obj.setQueryMonth(queryMonth);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dianXinFlowService.saveDianXinFlow(obj);
					}else {
						
						if(queryMonth.getTime()<=lastBillTime.getTime()){
							
						}else {
							DianXinFlow obj = new DianXinFlow();
							obj.setPhone(login.getLoginName());
							UUID uuid = UUID.randomUUID();
							obj.setId(uuid.toString());
							try {
								obj.setDependCycle(dependCycle);
								obj.setAllFlow(allFlow);
								obj.setAllPay(allPay);
								obj.setQueryMonth(queryMonth);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							dianXinFlowService.saveDianXinFlow(obj);
						} 
					}
					/*if (bean_flow != null && !bean_flow.getQueryMonth().before(queryMonth)) {
					}else {
						DianXinFlow dxflow = new DianXinFlow();
						UUID uuid1 = UUID.randomUUID();
						dxflow.setId(uuid1.toString());
						dxflow.setPhone(login.getLoginName());
						try {
							dxflow.setAllFlow(allFlow);
							dxflow.setAllPay(allPay);
							dxflow.setDependCycle(dependCycle);
							dxflow.setQueryMonth(queryMonth);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						flowList.add(dxflow);
					}*/
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorMsg = e.getMessage();
					b = false;
				}
				
			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
		}
//			dianXinFlowService.insertbatch(flowList);
		return b;
	}

	private boolean messageHistory_parse(String text, TelcomMessage bean_mes,
			String startDate) {
		List<TelcomMessage> messageList = new ArrayList<TelcomMessage>();
		boolean b = true;
		try {
			if (text != null && text.contains("\"resultMsg\":\"成功\"")) {
				JSONObject json1 = new JSONObject(text);
				JSONArray array = json1.getJSONArray("items");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonobject = array.getJSONObject(i);
					String qssj = jsonobject.getString("startTime");
					String fy = jsonobject.getString("charge");
					String dfhm = jsonobject.getString("callingNbr");
					String callWay = jsonobject.getString("callType");
					// String thlb = jsonobject.getString("eventType");

					Date nowDate = DateUtils.StringToDate(qssj,
							"yyyy-MM-dd HH:mm:ss");
					// 增量判断
					if (bean_mes != null
							&& !bean_mes.getSentTime().before(nowDate))
						continue;
					TelcomMessage dxMessage = new TelcomMessage();
					UUID uuid = UUID.randomUUID();
					dxMessage.setId(uuid.toString());
					dxMessage.setSentTime(nowDate);
					dxMessage.setBusinessType(callWay);
					dxMessage.setRecevierPhone(dfhm);
					dxMessage.setAllPay(Double.parseDouble(fy));
					dxMessage.setPhone(login.getLoginName());
					messageList.add(dxMessage);
				}
			}

		} catch (Exception e) {
			errorMsg = e.getMessage();
			b = false;
		}
		telcomMessageService.insertbatch(messageList);
		return b;
	}

	/**
	 * 0,采集全部 1.采集手机验证 2.采集手机已经验证
	 * 
	 * @param type
	 */
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

	// 随机短信登录
	public Map<String, Object> checkPhoneDynamicsCode() {

		CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
				"application/json; charset=UTF-8", "sd.189.cn");
		try {
			String text1 = cutil.post(
					"http://sd.189.cn/selfservice/bill/checkBillSmsRandom", h,
					"{\"code\":\"" + login.getPhoneCode()
							+ "\",\"accNbrorg\":\"" + login.getLoginName()
							+ "\"}");
			if (text1.contains("{\"flag\":\"1\"}")) {
				status = 1;
			} else {
				errorMsg = "您输入的查询验证码错误或过期，请重新核对或再次获取！";
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
				"application/json; charset=UTF-8", "sd.189.cn");

		try {
			String text = cutil.post(
					"http://sd.189.cn/selfservice/bill/sendBillSmsRandom", h,
					"{\"orgInfo\":\"" + login.getLoginName()
							+ "\",\"nbrType\":\"4\"}");
			if (text.contains("{\"flag\":\"0\"}")) {
				errorMsg = "发送成功";
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

	public void excute() {

	}

	public BigDecimal getYue() {
		BigDecimal yue = new BigDecimal("0");
		CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
				"application/json; charset=UTF-8", "sd.189.cn");
		Map<String, Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		try {
			String text = cutil.post(
					"http://sd.189.cn/selfservice/bill/queryBalance", h,
					"{\"accNbr\":\"" + login.getLoginName()
							+ "\",\"areaCode\":\""
							+ jsmap.get("areaCode").toString()
							+ "\",\"accNbrType\":\"4\"}");
			if (text.contains("\"resultMsg\":\"OK!\"")) {
				JSONObject json = new JSONObject(text);
				String ye = json.getString("balance");
				return new BigDecimal(ye);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return yue;
	}

	public String login1() {
		CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
				"application/json; charset=UTF-8", "sd.189.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		String text = cutil.post(
				"http://sd.189.cn/selfservice/service/loginAuthType", h, param);
		if (text != null) {
			return login2();
		}
		return null;
	}

	public String login2() {
		String url = "http://sd.189.cn/selfservice/service/userUamLogin";
		CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
				"application/json; charset=UTF-8", "sd.189.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		try {
			String text = cutil
					.post(url,
							h,
							"{\"loginUserTypeId\":\"1\",\"loginUserType\":\"4\",\"intLoginType\":\"4\",\"areaCode\":\"\",\"number\":\""
									+ login.getLoginName()
									+ "\",\"password\":\""
									+ login.getPassword()
									+ "\",\"sRand\":\""
									+ login.getAuthcode()
									+ "\",\"identifyType\":\"B\",\"formID\":\"cellphoneLoginForm\"}");
			if (text != null && text.contains("校验成功")) {
				JSONObject json = new JSONObject(text);
				String jurl = json.getString("url");
				if (jurl != null) {
					return login3(jurl);
				}

			} else if (text != null) {
				JSONObject json = new JSONObject(text);
				String msg = json.getString("msg");
				if (msg != null) {
					return msg;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String login3(String jurl) {
		CHeader h = new CHeader(CHeaderUtil.Accept_, null, null,
				"uam.sd.ct10000.com");
		Map<String, String> param = new LinkedHashMap<String, String>();
		param.put("SSORequestXML", jurl);
		String text = cutil.post("http://uam.sd.ct10000.com/LoginIn", h, param);
		if (text != null && text.contains("LoginAuth?UATicket=")) {
			CHeader h1 = new CHeader(CHeaderUtil.Accept_, null, null,
					"sd.189.cn");
			text = cutil.get(text, h1);
			if (text != null) {
				return login4();
			}
		}
		return null;
	}

	public String login4() {
		CHeader h = new CHeader(CHeaderUtil.Accept_json, null,
				"application/json; charset=UTF-8", "sd.189.cn");
		Map<String, String> param = new LinkedHashMap<String, String>();
		try {
			String text = cutil.post(
					"http://sd.189.cn/selfservice/cust/checkIsLogin", h, "{}");
			if (text != null && text.contains("\"resultMsg\":\"成功\"")) {

				JSONObject json = new JSONObject(text);
				map.put("areaCode", json.getString("areaCode").toString());
				map.put("userLoginType", json.getString("userLoginType")
						.toString());
				map.put("custName", json.getString("custName").toString());
				redismap.put("jsmap", map);// 根据实际需要存放
				return "1";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
