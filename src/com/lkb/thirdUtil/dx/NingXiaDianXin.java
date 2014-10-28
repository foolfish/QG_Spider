package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;

public class NingXiaDianXin extends AbstractDianXinCrawler {
	
	public NingXiaDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util) {
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
	public NingXiaDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		spider.getSite().setCharset("utf-8");
	}
	public NingXiaDianXin() {
        areaName = "宁夏";
		customField1 = "3";
		customField2 = "30";
		//http://www.189.cn/dqmh/frontLink.do?method=linkTo&toStUrl=http://nx.189.cn/fee/0/
		toStUrl = "&toStUrl=http://nx.189.cn/fee/0/";
		shopId = "10009";
	}
	
	@Override
    public void checkVerifyCode(final String userName) {   
		saveVerifyCode("ningxia", userName);
    }
	public void sendSmsPasswordForRequireCallLogService() {
		String xml = "<buffalo-call><method>getSelectedFeeProdNum</method><string>0951</string><string>"+phoneNo+"</string><string>2</string></buffalo-call>";
		postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService", "http://nx.189.cn/fee/4/",  new String[]{null, xml},null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				//String text = ContextUtil.getContent(context);
				
			}
		});
		xml = "<buffalo-call><method>qryMeals</method><string>" + phoneNo + "</string></buffalo-call>";
		postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService", "http://nx.189.cn/fee/4/", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					//String text = ContextUtil.getContent(context); 
					
				} catch (Exception e) {
					logger.error("sendSmsPasswordForRequireCallLogService:" + ContextUtil.getContent(context), e);
				}
			}
		});
		xml = "<buffalo-call><checkIsBillSMSShow>qryMeals</method></buffalo-call>";
		postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService", "http://nx.189.cn/fee/4/", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					//String text = ContextUtil.getContent(context); 
					
				} catch (Exception e) {
					logger.error("sendSmsPasswordForRequireCallLogService:" + ContextUtil.getContent(context), e);
				}
			}
		});
		xml = "<buffalo-call><method>sendDXYzmForBill</method></buffalo-call>";
		postUrl("http://nx.189.cn/bfapp/buffalo/CtSubmitService", "http://nx.189.cn/fee/4/",  new String[]{null, xml},null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					String text = ContextUtil.getContent(context); 
					if (text != null && text.indexOf("base.Success") >= 0) {
						setStatus(STAT_SUC);
					} else {
						setStatus(STAT_STOPPED_FAIL);
                                data.put("errMsg", "发送短信失败，请重试 !");
					}
				} catch (Exception e) {
					logger.error("sendSmsPasswordForRequireCallLogService:" + ContextUtil.getContent(context), e);
				}
			}
		});
		
	}
	
	private void verifySmsCode() {
		String xml = "<buffalo-call><method>validBillSMS</method><string>" + phoneNo + "</string><string>" + authCode + "</string></buffalo-call>";
		postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService", "http://nx.189.cn/fee/4/", new String[]{null, xml}, null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					String text = ContextUtil.getContent(context); 
					if (text != null && text.indexOf("base.Success") >= 0) {
						setStatus(STAT_SUC);
						notifyStatus();
						requestService();
					}
				} catch (Exception e) {
					logger.error("verifySmsCode:" + ContextUtil.getContent(context), e);
				}
			}
		});
		
	}

	@Override
    protected void onCompleteLogin(SimpleObject context) {
		Document doc = ContextUtil.getDocumentOfContent(context); 
		Elements form = doc.select("#fycxlogin_form");
		ContextUtil.setCookieValue(context, "tj_qh", true, new String[] {form.select("areaCode").val(), "nx.189.cn", "/"});
		ContextUtil.setCookieValue(context, "tj_hm", true, new String[] {form.select("accNbr").val(), "nx.189.cn", "/"});
		ContextUtil.setCookieValue(context, "tj_mmlx", true, new String[] {form.select("pwdType").val(), "nx.189.cn", "/"});
		ContextUtil.setCookieValue(context, "tj_hmlx", true, new String[] {form.select("accountType").val(), "nx.189.cn", "/"});
		
		checkLogin();
//		parseBalanceInfo();
//		//DebugUtil.printCookieData(ContextUtil.getCookieStore(context), "nx.189.cn");
//		//DebugUtil.findMissing(ContextUtil.getCookieStore(context), "userId=201|163225231; .ybtj.189.cn=6F25792D01E2F0A7D051E3F7DED985C1; cityCode=nx; isLogin=logined; JSESSIONID=CX6kTlWQCKNsMPLy1zZsGWzLDX4J2w2KJKj8krsGZHQFQvGQPJlJ!-192648054; tj_qh=0951; tj_hm=18095107544; tj_mmlx=01; tj_hmlx=2000004");
//		sendSmsPasswordForRequireCallLogService();
//		setStatus(STAT_LOGIN_SUC);
		//requestService();
	}
	
	@Override
    public void requestAllService() {
		verifySmsCode();
		//requestService();
	}
	private void requestService() {
		parseBalanceInfo();
		getYue();
		requestMonthBillService();	
        requestCurrentMonthBillService();
		Date d = new Date();
		for(int i = 6; i >=0; i--) {
			final Date cd = DateUtils.add(d, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");
			//if (i > 0) {
				requestLogService(1, 1, cd, dstr);
			//}
		}
		
		
	}	
	
	
	private void getYue() {
		String xml = "<buffalo-call><method>get_Balance_Of_Prod</method></buffalo-call>";
		postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService", "http://nx.189.cn/fee/0/",  new String[]{null, xml},null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				Elements rect = doc.select("string");
				String s1 = rect.get(0).text().trim();
				
				String s2 = s1.substring(0, s1.length()-1);
				BigDecimal b1= new BigDecimal(s2);
				addPhoneRemain(b1);
			}
		});
		
	}
	private void checkLogin() {
		getUrl("http://nx.189.cn/resource/dqmh/cms/index/MyAccountLogin.jsp", "http://nx.189.cn/fee/0/", null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context); 
				if(!doc.toString().contains("您好，请登录")){
					parseBalanceInfo();
					sendSmsPasswordForRequireCallLogService();
					setStatus(STAT_LOGIN_SUC);
				} else {
					setStatus(STAT_STOPPED_FAIL);
					data.put("errMsg", "登陆失败，请重试 !");
				}
			}
		});
	}
	
	private void parseBalanceInfo() {
		String xml = "<buffalo-call><method>getCustAndContInfo</method><string>1</string></buffalo-call>";
		postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService", "http://nx.189.cn/mng/custInfo/",  new String[]{null, xml},null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
					Elements rect = doc.select("map map");
					int len = rect.size();
					for(int i = 0; i < len; i++) {
						try {
							Element e = rect.get(i);
							Elements es = e.getAllElements();
							int len1 = es.size();
							boolean isCustType = false;
							boolean isCertType = false;
							boolean isContactInfo = false;
							for(int j = 0; j < len1; j++) {
								final String text = es.get(j).text();
								if (text.indexOf("crm2.intf.groupbean.custbo.CUST_TYPE") >= 0) {
									isCustType = true;
									isCertType = false;
									isContactInfo = false;
								} else if (text.indexOf("crm2.intf.groupbean.custbo.CUST_CONTACT_INFO") >= 0) {
									isContactInfo = true;
									isCertType = false;
									isCustType = false;
								} else if (text.indexOf("crm2.intf.groupbean.custbo.CUST_CERT_TYPE") >= 0) {
									isCertType = true;
									isCustType = false;
									isContactInfo = false;
								}
								String t1 = null;
								if (isCustType && "_CUST_NAME".equalsIgnoreCase(text)) {
									t1 = es.get(j + 1).text();
									user.setRealName(t1);
								} else if (isCustType && "_CUST_ADDRESS".equalsIgnoreCase(text)) {
									t1 = es.get(j + 1).text();
									user.setAddr(t1);
								} else if (isCustType && "_CREATE_DATE".equalsIgnoreCase(text)) {
									t1 = es.get(j + 1).text();
									user.setRegisterDate(DateUtils.StringToDate(t1, "yyyyMMdd"));
								} else if (isCertType && "_CERT_NUMBER".equalsIgnoreCase(text)) {
									t1 = es.get(j + 1).text();
									user.setIdcard(t1);
								}
							}

						} catch (Exception e) {
							logger.error("tel:" + rect.get(i).html(), e);
						}
					}
				} catch (Exception e) {
					logger.error("tel:" + ContextUtil.getContent(context), e);
				}
			}
		});
		
        // 使用父类的方法 postUrl
		                                                                                                                                                                        /*
         * xml =
         * "<buffalo-call><method>getSelectedFeeProdNum</method><string>0951</string><string>"
         * +phoneNo+"</string><string>2</string></buffalo-call>";
         * postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService",
         * "http://nx.189.cn/fee/0/", new String[]{null, xml},null, new
         * AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
         * 
         * @Override public void afterRequest(SimpleObject context) { //String
         * text = ContextUtil.getDocumentOfContent(context).toString();
         * System.out.println(text); RegexPaserUtil rp1 = new RegexPaserUtil(
         * "<buffalo-reply><string>", "元</string></buffalo-reply>", text,
         * RegexPaserUtil.TEXTEGEXANDNRT); String yuE = rp1.getText(); } });
         */
//		xml = "<buffalo-call><method>get_Balance_Of_Prod</method></buffalo-call>";
//		postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService", "http://nx.189.cn/fee/0/",  new String[]{null, xml},null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
//			@Override
//			public void afterRequest(SimpleObject context) {
//				Document doc = ContextUtil.getDocumentOfContent(context); 
//				Elements rect = doc.select("string");
//				String s1 = rect.get(0).text().trim();
//				
//				String s2 = s1.substring(0, s1.length()-1);
//				BigDecimal b1= new BigDecimal(s2);
//				addPhoneRemain(b1);
//			}
//		});
		
	}	

    private void requestCurrentMonthBillService(){
        String xml = "<buffalo-call><method>getShiShiHuaFeiByAccnbr</method><string>1</string></buffalo-call>";
        postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService",
                "http://nx.189.cn/fee/3/",
                new String[] { null, xml },null,new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2) {
                    @Override
                    public void afterRequest(SimpleObject context) {
                        try {
                            Document doc = ContextUtil
                                    .getDocumentOfContent(context);
                            logger.info(doc.toString());
                            DianXinTel tel = new DianXinTel();
                            Elements rect = doc.select("map string");
                            try {
                                for(int i=0; i<rect.size(); i++){
                                    if ("SMSG".equalsIgnoreCase(rect.get(i).text())) {
                                        String t1 = rect.get(i + 1).text();
                                        logger.info(t1);
                                        BigDecimal b1 = new BigDecimal(t1.length() == 0 ? "0" : t1);
                                        tel.setcAllPay(b1);
                                    }
                            }
                            }catch(Exception e){
                                logger.error("tel:" + rect.get(0).html(), e);
                            }
                            rect = doc.select("map list map");
                            
                            tel.setTeleno(phoneNo);
                            int len = rect.size();
                            for (int i = 0; i < len; i++) {
                                try {
                                    Element e = rect.get(i);
                                    Elements es = e.getAllElements();
                                    int len1 = es.size();
                                    for (int j = 0; j < len1; j++) {
                                        final String text = es.get(j).text();
                                        if ("CYCLE_END_DATE"
                                                .equalsIgnoreCase(text)) {
                                            tel.setcTime(DateUtils
                                                    .StringToDate(es.get(j + 1).text().substring(0, 6),
                                                            "yyyyMM"));
                                        } 
                                    }


                                } catch (Exception e) {
                                    logger.error("tel:" + rect.get(i).html(), e);
                                }
                            }
                            // System.out.println(tel.getcTime());
                            // System.out.println(tel.getTeleno());
                            // System.out.println(tel.getcAllPay());

                            addMonthBill(tel);
                        } catch (Exception e) {
                            logger.error(
                                    "tel:" + ContextUtil.getContent(context), e);
                        }
                    }
                });
    }

	private void requestMonthBillService() {
		String xml = "<buffalo-call><method>qry_LiShiZhangDansOf6</method></buffalo-call>";
		postUrl("http://nx.189.cn/bfapp/buffalo/CtQryService", "http://nx.189.cn/fee/3/",  new String[]{null, xml},null, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_2){
			@Override
			public void afterRequest(SimpleObject context) {
				try {
					Document doc = ContextUtil.getDocumentOfContent(context); 
                            // logger.info(doc.toString());
					Elements rect = doc.select("list map");
					int len = rect.size();
					for(int i = 0; i < len; i++) {
						try {
							Element e = rect.get(i);
							Elements es = e.getAllElements();
							DianXinTel tel = new DianXinTel();
							tel.setTeleno(phoneNo);
							int len1 = es.size();
							for(int j = 0; j < len1; j++) {
								final String text = es.get(j).text();
								if ("month".equalsIgnoreCase(text)) {
									tel.setcTime(DateUtils.StringToDate(es.get(j + 1).text(), "yyyyMM"));
								} else if ("charge".equalsIgnoreCase(text)) {
									String t1 = es.get(j + 1).text();
									BigDecimal b1 = new BigDecimal(t1.length() == 0 ? "0" : t1);
									tel.setcAllPay(b1);
								}
							}
							addMonthBill(tel);

						} catch (Exception e) {
							logger.error("tel:" + rect.get(i).html(), e);
						}
					}
				} catch (Exception e) {
					logger.error("tel:" + ContextUtil.getContent(context), e);
				}
			}
		});
	}
	private void requestLogService(final int page, final int t, final Date d, final String dstr) {
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyyMMdd");
		String ed = DateUtils.formatDate(ds[1], "yyyyMMdd");		
		getUrl("http://nx.189.cn/reportXLS?methodCode=qry_sj_yuyinfeiqingdan&startTime=" + bd + "&endTime=" + ed, "http://nx.189.cn/fee/4/", new String[] {"gbk"}, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveCallLog(context, d, dstr);
			}
		});
		getUrl("http://nx.189.cn/reportXLS?methodCode=qry_sj_cxclxd&startTime=" + bd + "&endTime=" + ed, "http://nx.189.cn/fee/4/", new String[] {"gbk"}, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveSmsLog(context, d, dstr);
			}
		});
		getUrl("http://nx.189.cn/reportXLS?methodCode=qry_sj_llxd&startTime=" + bd + "&endTime=" + ed, "http://nx.189.cn/fee/4/", new String[] {"gbk"}, new AbstractProcessorObserver(util, WaringConstaint.NingXiaDX_5) {
			@Override
			public void afterRequest(SimpleObject context) {
				saveOnlineFlow(context, d, dstr);
			}
		});
	}
	private void saveCallLog(SimpleObject context, Date d, String dstr) {
		String year = DateUtils.formatDate(d, "yyyy-");
        // 下载帐单
		String rt = ContextUtil.getContent(context);
		if (rt == null) {
			return;
		}
		int index = rt.indexOf("\r\n");
		String[] rows = null;
		if (index >= 0) {
			rows = rt.split("\r\n");
		} else {
			index = rt.indexOf("\n");
			if (index >= 0) {
				rows = rt.split("\n");
			} else {
				rows = rt.split("\r");
			}
		}
		if (rows != null) {
			if (rows.length <= 2) {
				return;
			}
			for(String row : rows) {
				if (!StringUtil.isBlank(row)) {
					try {
						String[] cols = row.split(",");
						if (cols != null) {			
                            if (cols[0].indexOf("序号") >= 0
                                    || cols[0].indexOf("合计") >= 0) {
								continue;
							}
							int len = cols.length;
							DianXinDetail dxDetail = new DianXinDetail();
							dxDetail.setPhone(phoneNo);
							addDetail(dxDetail);
							for(int j = 1; j < len; j++) {
								String text = cols[j];
								if (text != null && !StringUtil.isBlank(text)) {
									text = text.replaceAll("\"", "");
									if (j == 1) {
										dxDetail.setcTime(DateUtils.StringToDate(year + text, "yyyy-MM-dd HH:mm:ss"));
									} else if (j == 3) {
										dxDetail.setRecevierPhone(text);
									} else if (j == 4) {
										dxDetail.setCallWay(text);
									} else if (j == 5) {
										dxDetail.setTradeAddr(text);
									} else if (j == 6) {
										dxDetail.setTradeTime(TimeUtil.timetoint(text));
									} else if (j == 7) {
										dxDetail.setAllPay(new BigDecimal(text));
									}
								}
							}
						}

					} catch (Exception e) {
						logger.error(row, e);
					}

				}
			}
		}
		

	}
	private void saveSmsLog(SimpleObject context, Date d, String dstr) {
		String year = DateUtils.formatDate(d, "yyyy-");
		String rt = ContextUtil.getContent(context);
		if (rt == null) {
			return;
		}
		int index = rt.indexOf("\r\n");
		String[] rows = null;
		if (index >= 0) {
			rows = rt.split("\r\n");
		} else {
			index = rt.indexOf("\n");
			if (index >= 0) {
				rows = rt.split("\n");
			} else {
				rows = rt.split("\r");
			}
		}
		if (rows != null) {
			if (rows.length <= 2) {
				return;
			}
			for(String row : rows) {
				if (!StringUtil.isBlank(row)) {
					try {
						String[] cols = row.split(",");
						if (cols != null) {			
                            if (cols[0].indexOf("序号") >= 0
                                    || cols[0].indexOf("合计") >= 0) {
								continue;
							}
							//int len = cols.length;
							try {
								TelcomMessage obj = new TelcomMessage();
								obj.setPhone(phoneNo);
								int j = 0;
								for(String col : cols) {
									String text = cols[j];
									if (text != null && !StringUtil.isBlank(text)) {
										text = text.replaceAll("\"", "");
										if (j == 2) {
											obj.setRecevierPhone(text.replace("'", ""));
										} else if (j == 3) {
											obj.setSentTime(DateUtils.StringToDate(year + text, "yyyy-MM-dd HH:mm:ss"));
										} else if (j == 4) {
											obj.setAllPay(Doubles.tryParse(text));
										} else if (j == 1) {
											obj.setBusinessType(text);
										}
										j++;
									}
								}
								
								if (obj != null) {
									addMessage(obj);
								}
							} catch (Exception e) {
								logger.error(row, e);
							}
						}
						
					} catch (Exception e) {
						logger.error(row, e);
					}
					
				}
			}
		}
		
		
	}
	private void saveOnlineFlow(SimpleObject context, Date d, String dstr) {
		String year = DateUtils.formatDate(d, "yyyy-");
		Date[] ds = DateUtils.getPeriodByType(d, DateUtils.PERIOD_TYPE_MONTH);
		String bd = DateUtils.formatDate(ds[0], "yyyyMMdd");
		String ed = DateUtils.formatDate(ds[1], "yyyyMMdd");
		String rt = ContextUtil.getContent(context);
		if (rt == null) {
			return;
		}
		int index = rt.indexOf("\r\n");
		String[] rows = null;
		if (index >= 0) {
			rows = rt.split("\r\n");
		} else {
			index = rt.indexOf("\n");
			if (index >= 0) {
				rows = rt.split("\n");
			} else {
				rows = rt.split("\r");
			}
		}
		if (rows != null) {
			if (rows.length <= 2) {
				return;
			}
			for(String row : rows) {
				if (!StringUtil.isBlank(row)) {
					try {
						String[] cols = row.replaceAll("\"", "").split(",");
						if (cols != null) {			
							try {
                                if (cols[0].indexOf("序号") >= 0) {
									continue;
                                } else if (cols[0].indexOf("合计") >= 0) {
									DianXinFlow obj = new DianXinFlow();
                                    String yee = cols[6].replaceAll("元", "");
                                    int allTime = TimeUtil
                                            .timetoint_HH_mm_ss(cols[2]
                                                    .replaceAll("小时", ":")
                                                    .replaceAll("时", ":")
                                                    .replaceAll("分", ":")
                                                    .replaceAll("秒", ""));
									Double allFlow = com.lkb.util.StringUtil.flowFormat(cols[3]);
									BigDecimal yees = new BigDecimal(0);
									BigDecimal allTimes = new BigDecimal(0);
									BigDecimal allFlows = new BigDecimal(0);
									try {
										yees = new BigDecimal(yee);
										allTimes = new BigDecimal(allTime);
										allFlows = new BigDecimal(allFlow.toString());
									} catch (Exception e) {
									}
									obj.setPhone(phoneNo);
									obj.setAllFlow(allFlows);//3
									obj.setAllPay(yees);//6
									obj.setAllTime(allTimes);//2
									obj.setDependCycle(bd+"-"+ed);
									obj.setQueryMonth(ds[0]);
									flowList.add(obj);
								}else if(cols.length==7){
									DianXinFlowDetail dianXinFlowDetail = new DianXinFlowDetail();
									String beginTime = cols[1];
                                    int tradeTime = TimeUtil
                                            .timetoint_HH_mm_ss(cols[2]
                                                    .replaceAll("小时", ":")
                                                    .replaceAll("时", "")
                                                    .replaceAll("分", ":")
                                                    .replaceAll("秒", ""));
									Double flow = com.lkb.util.StringUtil.flowFormat(cols[3]);
									String netType = cols[4];
									String location = cols[5];
									String fee = cols[6];
									Date beginTimeDate = null;
									BigDecimal feeDecimal = new BigDecimal(0);
									BigDecimal flowDecimal = new BigDecimal(0);
									int iscm = 0;
									try {
										beginTimeDate = DateUtils.StringToDate(beginTime,"yyyy-MM-dd HH:mm:ss");
										feeDecimal = new BigDecimal(fee);
										flowDecimal = new BigDecimal(flow.toString());
										if (DateUtils.formatDate(new Date(), "yyyyMM").equals(DateUtils.formatDate(d, "yyyyMM"))) {
											iscm = 1;
										};
									} catch (Exception e) {
										logger.error("error",e);
									}
									dianXinFlowDetail.setBeginTime(beginTimeDate);//1
									dianXinFlowDetail.setFee(feeDecimal);//6
									dianXinFlowDetail.setFlow(flowDecimal);//3
									dianXinFlowDetail.setIscm(iscm);
									dianXinFlowDetail.setLocation(location);//5
									dianXinFlowDetail.setNetType(netType);//4
									dianXinFlowDetail.setTradeTime(tradeTime);//2
									dianXinFlowDetail.setPhone(phoneNo);//2
									if (dianXinFlowDetail != null) {
										flowDetailList.add(dianXinFlowDetail);
									}
								}
							} catch (Exception e) {
								logger.error(row, e);
							}
						}
					} catch (Exception e) {
						logger.error(row, e);
					}
				}
			}
		}
		
		
	}
	
	public static void main(String[] args) throws Exception {
		/*for(int i=0; i< 10; i++) {
			System.out.println(i + "=" + (int) (Math.random() * 1000 % 10));
		}
		if (true) {
			return;
		}*/
        /*
         * String xml =
         * "<buffalo-reply><map><type>cn.ffcs.ct10000.pojo.crm2.CustAContInfo</type><string>cust</string><map><type>crm2.intf.groupbean.custbo.CUST_TYPE</type><string>_CUST_ID</string><null></null><string>_CUST_NUMBER</string><string>2951579983790000</string><string>_GROUP_CUST_SEQ</string><null></null><string>_CUST_NAME</string><string>赵玉龙</string><string>_CUST_ADDRESS</string><null></null><string>_CUST_TYPE</string><string>公众客户</string><string>_CUST_SUB_TYPE</string><null></null><string>_CUST_BRAND</string><null></null><string>_COMMON_REGION_ID</string><null></null><string>_LAN_ID</string><null></null><string>_CUST_AREA_GRADE</string><null></null><string>_SERVICE_LEVEL</string><null></null><string>_INDUSTY_CODE</string><null></null><string>_CREDIT_LEVEL</string><null></null><string>_INDUSTRY_CD</string><null></null><string>_CUST_PWD</string><null></null><string>_STATUS_CD</string><null></null><string>_STATUS_DATE</string><null></null><string>_ENTER_DATE</string><null></null><string>_CREATE_DATE</string><string>20140804</string><string>_ATTRList</string><list><type>java.util.Vector</type><length>0</length></list><string>_CUST_CERTList</string><ref>2</ref><string>_CUST_CONTACT_INFOList</string><ref>2</ref><string>_CUST_RELList</string><ref>2</ref><string>_ENGLISH_NAME</string><null></null><string>_MODIFY_DATE</string><null></null><string>_ACTION_TYPE</string><null></null><string>_AREA_CODE</string><null></null></map><string>contact</string><map><type>crm2.intf.groupbean.custbo.CUST_CONTACT_INFO</type><string>_CONTACT_ID</string><null></null><string>_CUST_ID</string><null></null><string>_HEAD_FLAG</string><null></null><string>_CONTACT_TYPE</string><null></null><string>_CONTACT_NAME</string><null></null><string>_CONTACT_GENDER</string><null></null><string>_CONTACT_ADDRESS</string><null></null><string>_CONTACT_EMPLOYER</string><null></null><string>_HOME_PHONE</string><null></null><string>_OFFICE_PHONE</string><null></null><string>_MOBILE_PHONE</string><null></null><string>_CONTACT_DESC</string><null></null><string>_e_MAIL</string><null></null><string>_POSTCODE</string><null></null><string>_POST_ADDRESS</string><null></null><string>_FAX</string><null></null><string>_STATUS_CD</string><null></null><string>_STATUS_DATE</string><null></null><string>_CREATE_DATE</string><null></null><string>_ATTRList</string><ref>2</ref></map><string>cert</string><map><type>crm2.intf.groupbean.custbo.CUST_CERT_TYPE</type><string>_CUST_CERTI_ID</string><null></null><string>_CUST_ID</string><null></null><string>_CERT_TYPE</string><string>身份证</string><string>_CERT_NUMBER</string><string>140321********0310</string><string>_CERT_ADDRESS</string><null></null><string>_CERT_ORG</string><null></null><string>_EFF_DATE</string><null></null><string>_EXP_DATE</string><null></null><string>_CREATE_DATE</string><null></null><string>_ATTRList</string><ref>2</ref><string>_MODIFY_DATE</string><null></null><string>_ACTION_TYPE</string><null></null></map></map></buffalo-reply>"
         * ; Document doc = Jsoup.parse(xml); Elements rect =
         * doc.select("map map"); int len = rect.size(); for(int i = 0; i < len;
         * i++) { try { Element e = rect.get(i); Elements es =
         * e.getAllElements(); int len1 = es.size(); boolean isCustType = false;
         * boolean isCertType = false; boolean isContactInfo = false; for(int j
         * = 0; j < len1; j++) { final String text = es.get(j).text(); if
         * (text.indexOf("crm2.intf.groupbean.custbo.CUST_TYPE") >= 0) {
         * isCustType = true; } else if
         * (text.indexOf("crm2.intf.groupbean.custbo.CUST_CONTACT_INFO") >= 0) {
         * isContactInfo = true; } else if
         * (text.indexOf("crm2.intf.groupbean.custbo.CUST_CERT_TYPE") >= 0) {
         * isCertType = true; } String t1 = null; if (isCustType &&
         * "_CUST_NAME".equalsIgnoreCase(text)) { t1 = es.get(j + 1).text(); }
         * else if (isCustType && "_CUST_ADDRESS".equalsIgnoreCase(text)) { t1 =
         * es.get(j + 1).text(); } else if (isCustType &&
         * "_CREATE_DATE".equalsIgnoreCase(text)) { t1 = es.get(j + 1).text(); }
         * else if (isCertType && "_CERT_NUMBER".equalsIgnoreCase(text)) { t1 =
         * es.get(j + 1).text(); } if (t1 != null) { System.out.println(t1); } }
         * 
         * } catch (Exception e) { System.out.println("tel:" +
         * rect.get(i).html()); logger.error("error",e); } }
         * 
         * if (true) { return; }
         */
		String phoneNo = "18095107544";
		String password = "602998";
		
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		NingXiaDianXin dx = new NingXiaDianXin(spider, null, phoneNo, password, "2345", null);
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		dx.printData();
		

	}
}
