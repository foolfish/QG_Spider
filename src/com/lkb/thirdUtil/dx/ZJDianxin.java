package com.lkb.thirdUtil.dx;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IWarningService;
import com.lkb.test.jsdx.RegexPaserUtil;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.thirdUtil.yd.GZYidong;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.entity.SpeakBillPojo;
import com.lkb.warning.WarningUtil;

/*
 * 
 * 浙江电信
 * */
public class ZJDianxin extends BaseInfoMobile{
	private static final Logger logger = Logger.getLogger(GZYidong.class);
	public  String imgurl = "https://uam.ct10000.com/ct10000uam/validateImg.jsp";
	public String CUST_NAME = "CUST_NAME";
	public String phone_code = "phone_code";
	public String index_text = "zjdianxin_index_text";
	public String price = "zjdianxin_price";
	public String tj_realname = "zjdianxin_realname";
	
	public ZJDianxin(Login login,String currentUser) {
		super( login,ConstantNum.comm_zj_dx,currentUser);
		this.userSource = Constant.DIANXIN;
	}
	

	
	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			logger.error("error",e);
		}
		return date;
	}

	

	public  List<String> getMonth(int num,String format) {
		List<String> objectTmp = new ArrayList<String>();
		java.text.DateFormat format2 = new java.text.SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		//电信出账日期
		if(1<=c.get(Calendar.DAY_OF_MONTH)&&c.get(Calendar.DAY_OF_MONTH)<=3){}
		else{
			c.add(Calendar.MONTH, 1);	
		}
		
		for (int i = 0; i < num; i++) {
			c.add(Calendar.MONTH, -1);
			Date date = c.getTime();
			String date2 = format2.format(date);
			// System.out.println(date2);
			objectTmp.add(date2);
		}
		return objectTmp;
	}

	public void init(){
		if(!isInit()){
			if(cutil.get("http://zj.189.cn/wt_uac/auth.html?app=wt&login_goto_url=service/&module=null&auth=uam_login_auth&template=uam_login_page")!=null){
				setImgUrl(imgurl);
				addCookie("SHOPID_COOKIEID", "10012");
				addCookie("cityCode", "zj");
				String reqUrl = "https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=172.16.10.20";
				String text = cutil.get(reqUrl);
				if(text!=null){
					redismap.put(index_text, text);
					setInit();
				}
			}
		}
	}
	
	/**第一次post*/
	public  Map<String,Object> login(){
		try{
			String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
			Map<String,String> param = new HashMap<String,String>();
			param.put("username", login.getLoginName()); 
			String text=  cutil.post(url, param);
			
			if(text!=null){
				login_1(text);
			}
		}catch(Exception e){
			writeLogByLogin(e);
		}
    	if(islogin()){
        	addTask(this);
    	}
    	return map;
	}
	private  void login_1(String ss){
		try{
			String s[] = ss.split("\\|");
			String customFileld02 = s[0];
			String areaname = s[1];
			String text =(String)redismap.get(index_text);
			  Document doc = Jsoup.parse(text);
				String empoent = doc.select("input[id=forbidpass]").first().val();
				String forbidaccounts = doc.select("input[id=forbidaccounts]").first().val();
				String authtype = doc.select("input[name=authtype]").first().val();
				
				
				String customFileld01 = "3";
				String lt = doc.select("input[name=lt]").first().val();
				String _eventId = doc.select("input[name=_eventId]").first().val();
				String open_no = "c2000004";
			
				String action = doc.select("form[id=c2000004]").first().attr("action");
				String phone1 = login.getLoginName();
				String password1 = login.getPassword();
				//-----------加密
					//pwd, digit, f, s
				/*try{
					String n = StringUtil.subStr("strEnc(username,", ");", text).trim();
					String[] stra = n.trim().replaceAll("\'", "").split(",");
					phone1 = AbstractDianXinCrawler.executeJsFunc("des/tel_com_des.js", "strEnc", login.getLoginName(), stra[0], stra[1], stra[2]);
					password1 =AbstractDianXinCrawler.executeJsFunc("des/tel_com_des.js", "strEnc", login.getPassword(), stra[0], stra[1], stra[2]);
				}catch(Exception e){
					phone1 = login.getLoginName();
					password1 =login.getPassword();
				}*/
				action = URLDecoder.decode(action);
				
				String url = "https://uam.ct10000.com"+action;
				CHeader h = new CHeader("https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=114.249.55.149");
				Map<String,String> param = new LinkedHashMap<String,String>();
				param.put("empoent", empoent); 
				param.put("forbidaccounts",forbidaccounts); 
				param.put("authtype",authtype); 
				param.put("customFileld02", customFileld02); 
				param.put("areaname", areaname); 
				param.put("customFileld01", customFileld01); 
				param.put("lt", lt); 
				param.put("_eventId", _eventId); 
				param.put("username", phone1); 
				param.put("password", password1); 
				param.put("randomId", login.getAuthcode()); 
				param.put("open_no", open_no); 
				text = cutil.post(url,h, param);
		    	System.out.println(text);
				
		    	if(text.contains("https://uam.ct10000.com:443/ct10000uam-gate/?SSORequestXMLRETURN=")){
		    		text = cutil.post(text,h, param);
		    		RegexPaserUtil rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		url = rp.getText();
	    			//http://189.cn/dqmh/Uam.do?method=loginJTUamGet&UATicket=35nullST--423854-4K2FlITjex4PVnWulrrd-ct10000uam
	    			 url = url.replace("http://189.cn/", "http://www.189.cn/");
	    			 CHeader c = new CHeader("http://www.189.cn/dqmh/Uam.do?method=loginJTUamGet&UATicket=");
	    			 text = cutil.get(url,c);
		    		 login_2();
		    	}else if(text.contains("http://uam.ct10000.com/ct10000uam/login?")){
		    		
		    	} else{
		    		errorMsg = Jsoup.parse(text).getElementById("status2").text();
		    	}
		}catch(Exception e){
			writeLogByLogin(e);
		}
    		
	}
	

	private void login_2(){
		RegexPaserUtil rp = null;
		String text = cutil.get("http://zj.189.cn/zjpr/index/iframe/memberPay_iframe.html");
		text = cutil.get("http://zj.189.cn/zjpr/index/iframe/chongzhi_iframe.html");
		text = cutil.get("https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=106.39.255.238,%20127.0.0.1");
		String url = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
		if(url!=null){
			text = cutil.get(url);
			System.out.println(text);
			if(text!=null){
				text = cutil.get("http://189.cn/dqmh/frontLink.do?method=linkTo&shopId=10012&toStUrl=zjpr/index/tiaozhuan/kongbaiyezhongxin.html");
				url = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
				text = cutil.get(url);
				text = cutil.get("http://zj.189.cn/zjpr/index/tiaozhuan/kongbaiyezhongxin.html");
				//System.out.println(text);
				text = cutil.get("http://zj.189.cn/zjpr/balancep/getBalancep.htm");
				String yue = StringUtil.subStr("您的帐户余额：", "元", text);
				if(StringUtils.isNotBlank(yue)){
					yue = yue.replaceAll("<.*?>", "").trim();
					if(yue.length()>0){
						redismap.put(price,yue);
						loginsuccess();
						sendPhoneDynamicsCode();
					}
				}
			}
		}
	}
	
	
	/**
	 * 生成短信
	 * */
	public Map<String,Object> sendPhoneDynamicsCode() {
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		int status  = 0;
		try {
			String xmls= "<buffalo-call>";
			xmls+="<method>SendVCodeByNbr</method>";
			xmls+="<string>"+login.getLoginName()+"</string>";   
			xmls+="</buffalo-call>";
//			HttpResponse response1 = getPost( "http://zj.189.cn/bfapp/buffalo/VCodeOperation",null,httpclient, xmls);
//			String text1 = ParseResponse.parse(response1,"UTF-8");
			CHeader c = new CHeader();
			c.setRespCharset("UTF-8");
			c.setContent_Type("application/xmls");
			String text = cutil.post("http://zj.189.cn/bfapp/buffalo/VCodeOperation", c, xmls);
			if(text.contains("成功")){
			    status = 1;
			    errorMsg = "发送成功,请注意查收!";
			}
		} catch (Exception e) {
			logger.error("error",e);
			errorMsg = "服务器繁忙,发送失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
		int type = login.getType();
		try{
			parseBegin(Constant.DIANXIN);
			switch (type) {
			case 1:
				getTelDetailHtml();//其他月份通话记录
				saveTel();//当月
				getMyInfo(); //个人信息
				break;
			case 2:
				callHistory(); //历史账单
				getSmsLog();//短信记录
				getOnlineFlow();//抓取流量
				break;
			case 3:
				saveTel();//通话记录
				getMyInfo(); //个人信息
				callHistory(); //历史账单	
				getSmsLog();//短信记录
				break;
			default:
				break;
			}
		}finally{
			parseEnd(Constant.DIANXIN);
		}
	}
	
	
	/**
	查询获取短信记录*/
	public void getSmsLog()
	{	
		try{
		List<String> ms =  getMonth(6,"yyyyMM");
		String phoneCode = (String)redismap.get(phone_code);
		Date lastTime = null;
		try{
			lastTime = telcomMessageService.getMaxSentTime(login.getLoginName()).getSentTime();
		}
		catch(Exception e)
		{
			logger.error("error",e);
		}		
		for (String s : ms) {
			String starDate = s ;
			Map<String,String>	param = new HashMap<String,String>();
			param.put("cdrCondition.areaid", "573");
			param.put("cdrCondition.cdrlevel","");
			param.put("cdrCondition.cdrmonth", starDate);
			param.put("cdrCondition.cdrtype", "21");
			param.put("cdrCondition.pagenum", "1");
			param.put("cdrCondition.pagesize", "10000");
			param.put("cdrCondition.product_servtype","18");
			param.put("cdrCondition.productid", "1-1IBCFCMC");
			param.put("cdrCondition.productnbr", login.getLoginName());
			param.put("cdrCondition.randpsw", phoneCode);
			param.put("cdrCondition.recievenbr","");
			String text = cutil.post("http://zj.189.cn/zjpr/cdr/getCdrDetail.htm",param);	
			
	    	if(text.contains("我的清单详情")){
				System.out.println(text);
		    	Document doc  = Jsoup.parse(text);	    		  
				 Elements tables = doc.select("table[class=cdrtable]");
				 for(int i = 0;i<tables.size();i++){ 
					 Element table1 =  tables.get(i);
					 String tableString = table1.text();
					 if(tableString.contains("对方号码")&&tableString.contains("业务类型")){
						 Elements trs = table1.select("tr");
						 if(trs.size()>2){
							 for (int j = 2; j < trs.size(); j++) {
								Elements tds =trs.get(j).select("td");
								if(tds.size()==8){
									String RecevierPhone= tds.get(1).text();
									String BusinessType= "发送";
									String SentTime= fdate(new RegexPaserUtil("<td align=\"center\" class=\"Pzone_details_dashed pzone_right_dashed\"><script>formateDate\\(\"","\"\\)</script></td>",tds.get(3).toString(),RegexPaserUtil.TEXTEGEXANDNRT).getText());
									String AllPay=new RegexPaserUtil("<td align=\"center\" class=\"Pzone_details_dashed pzone_right_dashed\"><script> getbF\\(\"","\"\\) </script>",tds.get(4).toString(),RegexPaserUtil.TEXTEGEXANDNRT).getText();						
									
									Date sentTime =null;
									try{
										sentTime= DateUtils.StringToDate(SentTime, "yyyy-MM-dd HH:mm:ss");
										if(lastTime!=null&&sentTime!=null){
											if(sentTime.getTime()<=lastTime.getTime()){
												continue;
											}	
										}	
									}
									catch(Exception e)
									{
										logger.error("error",e);
									}
									
									TelcomMessage obj = new TelcomMessage();
									obj.setPhone(login.getLoginName());
									UUID uuid = UUID.randomUUID();
									obj.setId(uuid.toString());
									obj.setBusinessType(BusinessType);//业务类型：点对点
									obj.setRecevierPhone(RecevierPhone);//对方号码
									obj.setSentTime(sentTime);//发送时间
									obj.setCreateTs(new Date());
									obj.setAllPay(Double.parseDouble(AllPay));//总费用
									
									telcomMessageService.save(obj);
								}
							 }
						 }
					 }
					}
		    	}
			
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			String warnType = WaringConstaint.ZJDX_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}
	/**
	查询获取短信记录*/
	public void getOnlineFlow()
	{	
		try{
			List<String> ms =  getMonth(6,"yyyyMM");
			String phoneCode = (String)redismap.get(phone_code);
			Date lastTime = null;
			try{
				if(dianXinFlowService.getMaxFlowTime(login.getLoginName())!=null)
				lastTime = dianXinFlowService.getMaxFlowTime(login.getLoginName()).getQueryMonth();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}		
			for (String s : ms) {
				String starDate = s ;
				String cd = formatDate(new Date());
				int iscm = 0;
				if (cd.equals(starDate)) {
					iscm = 1;
				}
				CHeader header = new CHeader("http://zj.189.cn/zjpr/cdr/getCdrDetailInput.htm");
				Map<String,String>	param = new HashMap<String,String>();
				param.put("cdrCondition.areaid", "573");//
				param.put("cdrCondition.cdrlevel","");//
				param.put("cdrCondition.cdrmonth", starDate);//
				param.put("cdrCondition.cdrtype", "41");//
				param.put("cdrCondition.pagenum", "1");//
				param.put("cdrCondition.pagesize", "10000");//
				param.put("cdrCondition.product_servtype","18");//
				param.put("cdrCondition.productid", "1-1IBCFCMC");//
				param.put("cdrCondition.productnbr", login.getLoginName());
				param.put("cdrCondition.randpsw", phoneCode);
				param.put("cdrCondition.recievenbr","");//
				String text = cutil.post("http://zj.189.cn/zjpr/cdr/getCdrDetail.htm",header,param);	
				System.out.println(text);
				if(text.contains("我的清单详情")){
					Document doc  = Jsoup.parse(text);	
					try {
						List<DianXinFlow> flowList = new ArrayList<DianXinFlow>();
						Element tableBill =  doc.select("table[class=accountTotalT]").get(0);
						String tableBillString = tableBill.text();
						if (tableBillString.contains("查询日期")) {
							Elements trs = tableBill.select("tr");
							BigDecimal allFlows = new BigDecimal(0);
							BigDecimal allPays = new BigDecimal(0);
							System.out.println("1");
							String allFlow = trs.get(2).select("td").get(1).text().replaceAll("总 流 量：", "").replaceAll("MB", "").trim();
							String allPay = trs.get(3).select("td").get(0).select("script").toString();
							try {
								allFlows = new BigDecimal(Double.parseDouble(allFlow)*1024);//计算流量
								String allPayString = StringUtil.subStr("<script>getbF(\"", "\")</script>", allPay);
								allPays = getbF(allPayString);//计算费用
							} catch (Exception e) {
								log.error("error",e);
							}
							
							int allTime = DateUtils.transform(trs.get(2).select("td").get(0).text().replaceAll("总 时 长：", "").replaceAll("小时", ":").replaceAll("分", ":").replaceAll("秒", ""));
							String dependCycle = starDate+"01-"+starDate+DateUtils.getDaysOfMonth(starDate);
							DianXinFlow dianXinFlow = new DianXinFlow();
							dianXinFlow.setAllFlow(allFlows);
							dianXinFlow.setAllPay(allPays);
							dianXinFlow.setAllTime(new BigDecimal(allTime));
							dianXinFlow.setDependCycle(dependCycle);
							dianXinFlow.setPhone(login.getLoginName());
							dianXinFlow.setQueryMonth(DateUtils.StringToDate(starDate,"yyyyMM"));
							flowList.add(dianXinFlow);
							saveDianXinFlowBill(flowList);
						}
					} catch (Exception e) {
						log.error(e);
					}
					
					Element tableDetail =  doc.select("table[class=cdrtable]").get(0);
					String tableString = tableDetail.text();
					if(tableString.contains("流量")&&tableString.contains("网络类型")){
						Elements trs = tableDetail.select("tr");
						List<DianXinFlowDetail> detailList = new ArrayList<DianXinFlowDetail>();
						if(trs.size()>2){
							for (int j = 2; j < trs.size(); j++) {
								Elements tds =trs.get(j).select("td");
								if(tds.size()==11){
									try {
										String beginTime = fdate(tds.get(1).select("script").toString());// 开始时间
										String tradeTime = tds.get(2).select("script").toString().replaceAll("<script>funcFmtTime\\(\"", "").replaceAll("\"\\)</script>", "").trim();// 上网时长
										int flow = Integer.parseInt(tds.get(3).text().trim());// 总流量
										String netType = tds.get(4).text().substring(0,4).trim();// 网络类型
										String countType = tds.get(5).text().trim();// 计费类型
										String location = tds.get(6).text().trim();// 通信地点
										String fee = tds.get(7).select("script").toString().replaceAll("<script> getbF\\(\"", "").replaceAll("\"\\) </script>", "").trim();// 费用（元）
										String derate  = tds.get(8).select("script").toString().replaceAll("<script> getbF\\(\"", "").replaceAll("\"\\) </script>", "").trim();// 减免
										String feeAll = tds.get(9).select("script").toString().replaceAll("<script> getbF\\(\"", "").replaceAll("\"\\) </script>", "").trim();// 费用小计
										String business = tds.get(10).text().trim();// 业务内容
										Date beginTimeDate = null;
										long  tradeTimes = 0;
										BigDecimal fees = new BigDecimal(0);
										BigDecimal flows = new BigDecimal(0);
										try {
											beginTimeDate = DateUtils.StringToDate(beginTime,
													"yyyy-MM-dd HH:mm:ss");
											tradeTimes = Long.parseLong(tradeTime);
											fees = getbF(feeAll);
											flows = new BigDecimal(flow);
										} catch (Exception e) {
											logger.error("error",e);
										}
										
										try {
											DianXinFlowDetail obj = new DianXinFlowDetail();
											obj.setPhone(login.getLoginName());
											UUID uuid = UUID.randomUUID();
											obj.setId(uuid.toString());
											obj.setBeginTime(beginTimeDate);
											
											if(lastTime!=null){
												if(beginTimeDate.getTime()<=lastTime.getTime()){
													
												}else {
													obj.setFee(fees);
													obj.setNetType(netType);
													obj.setTradeTime(tradeTimes);
													obj.setFlow(flows);
													obj.setLocation(location);
													obj.setBusiness(business);
													dianXinFlowDetailService.saveDianXinFlowDetail(obj);
												}
											}else {
												obj.setFee(fees);
												obj.setNetType(netType);
												obj.setTradeTime(tradeTimes);
												obj.setFlow(flows);
												obj.setLocation(location);
												obj.setBusiness(business);
												System.out.println("*********");
												dianXinFlowDetailService.saveDianXinFlowDetail(obj);
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									} catch (Exception e) {
										// TODO: handle exception
									}
									
								}
							}//for(trs.size)
							//System.out.println("*************************save");
							//saveDianXinFlowDetail(detailList);
						}
					}
					
				}
				
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			String warnType = WaringConstaint.ZJDX_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}
	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
		try {
			Map<String,String>	param = new HashMap<String,String>();
			param.put("cdrCondition.areaid", "573");
			param.put("cdrCondition.cdrlevel","");
			param.put("cdrCondition.cdrmonth", getMonth(1,"yyyyMM").get(0).toString());
			param.put("cdrCondition.cdrtype", "11");
			param.put("cdrCondition.pagenum", "1");
			param.put("cdrCondition.pagesize", "100");
			param.put("cdrCondition.product_servtype","18");
			param.put("cdrCondition.productid", "1-1IBCFCMC");
			param.put("cdrCondition.productnbr", login.getLoginName());
			param.put("cdrCondition.randpsw", login.getPhoneCode());
//			param.put("cdrCondition.recievenbr","");
			param.put("cdrCondition.recievenbr","移动电话");
			CHeader	h = new CHeader("http://zj.189.cn/shouji/"+login.getLoginName()+"/zhanghu/xiangdan/");
			String text = cutil.post("http://zj.189.cn/zjpr/cdr/getCdrDetail.htm", h,param);
			System.out.println(text);
			if(text.contains("我的清单详情")||text.contains("zjpr/common/show_error.html?ErrorNo=61010")){
				redismap.put(phone_code, login.getPhoneCode());
				status = 1;
	    	}else{
	    		errorMsg = "随机验证码输入有误！";
	    	}
		} catch (Exception e) {
//			sendWarning("随机短信登陆异常#"+e.getMessage());
			errorMsg = "随机口令验证失败,请重试!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		if(status==1){
        	addTask_2(this);
    	}
		return map;
	}
	
	
	
	/**
	 * 查询个人信息
	 */
	public boolean getMyInfo() {
		try {
//			CHeader h = new CHeader(CHeaderUtil.Accept_json,"",CHeaderUtil.Content_Type__urlencoded,"zj.189.cn",true);
			BigDecimal phoneremain= new BigDecimal("0.00");
			try {
				String yue = cutil.get("http://zj.189.cn/shouji/"+login.getLoginName()+"/zhanghu/yue/");
//				String yue = ParseResponse.parse(response1,"GBK");
				//System.out.println(yue);
				if(yue.contains("您的帐户余额")){
					Document doc = Jsoup.parse(yue);
			
					String yu =  doc.select("span[class=userzone_black_12]").first().text().replace("元", "");
					
					if(yu!=null){
						 phoneremain = new BigDecimal(yu);
					}
				}
			} catch (Exception e) {
				
			}
			String zl =cutil.get("http://zj.189.cn/shouji/"+login.getLoginName()+"/bangzhu/ziliaoxg");
//			System.out.println(zl);
//			String zl = ParseResponse.parse(response2,"GBK");
			if(zl.contains("资料修改")){
				Document doc1 = Jsoup.parse(zl);
				String dz = doc1.select("input[id=saddress]").first().attr("value");
				String dh = doc1.select("input[id=phone]").first().attr("value");
				String email = doc1.select("input[id=email]").first().attr("value");
				
				String wspan = doc1.select("span[id=v4_welcome_msg2]").first().val();
				//String xm = wspan.split("</script>")[1].split("<a")[0].replaceAll(" ", "");
				Map<String, String> map = new HashMap<String, String>();
				map.put("parentId", currentUser);
				map.put("usersource", Constant.DIANXIN);
				map.put("loginName", login.getLoginName());
				List<User> list = userService.getUserByParentIdSource(map);
				String name = (String)redismap.get(CUST_NAME);
				if (list != null && list.size() > 0) {
					User user = list.get(0);
					user.setLoginName(login.getLoginName());
					user.setLoginPassword("");
					user.setUserName(name);
					user.setRealName(name);
					user.setIdcard("");
					user.setAddr(dz);
					user.setUsersource(Constant.DIANXIN);
					user.setParentId(currentUser);
					user.setModifyDate(new Date());
					user.setPhone(login.getLoginName());
					user.setFixphone("");
					user.setPhoneRemain(phoneremain);
					user.setEmail(email);
					userService.update(user);
				} else {
					User user = new User();
					UUID uuid = UUID.randomUUID();
					user.setId(uuid.toString());
					user.setLoginName(login.getLoginName());
					user.setLoginPassword("");
					user.setUserName(name);
					user.setRealName(name);
					user.setIdcard("");
					user.setAddr(dz);
					user.setUsersource(Constant.DIANXIN);
					user.setParentId(currentUser);
					user.setModifyDate(new Date());
					user.setPhone(login.getLoginName());
					user.setFixphone("");
					user.setPhoneRemain(phoneremain);
					user.setEmail(email);
					userService.saveUser(user);
				}
				return true;	
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			String warnType = WaringConstaint.ZJDX_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

		return false;
	}
	
	private String formatDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(d);
	}
	public String fdate(String date){
		
		try {
			String xm = date.replaceAll("<script>formateDate\\(\"", "").replaceAll("\"\\)</script>", "").trim().replaceAll("T", " ");
			String t = xm.substring(0,xm.indexOf(".000+"));
			return t;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("error",e);
		}
		
		return  "";
	}
	public String funcFmtTime(String date){
		
		//String a = "<script>formateDate(\"2014-06-21T10:13:12.000+08:00\")</script>";
		String xm = date.replaceAll("<script>funcFmtTime\\(\"", "").replaceAll("\"\\)</script>", "").trim();
		return  xm;
	}
	public String empsub(String date){
		
		//String a = "<script>formateDate(\"2014-06-21T10:13:12.000+08:00\")</script>";
		String xm = date.replaceAll("<script>empsub\\(\"", "").replaceAll("\"\\)</script>", "").trim();
		return  xm;
	}
	public BigDecimal getbF(String date){
		try {
		
			if(date.toString().indexOf(",")!=-1)
			{
				date=date.replaceAll(",", "");
			}
			BigDecimal b = 	new BigDecimal(date);
			return b.divide(new BigDecimal("100"));
		} catch (Exception e) {
			logger.error("error",e);
		}	
		return  new BigDecimal("0.00");
	}
	
	
	
	
	
	/**
	 * 查询通话记录
	 */
	public boolean callHistory(){
		try {
			List<String> ms =  getMonth(6,"yyyyMM");
			String phoneCode = (String)redismap.get(phone_code);
			DianXinDetail detail = new DianXinDetail();
			detail.setPhone(login.getLoginName());
			detail = dianXinDetailService.getMaxTime(detail);
			boolean b = true;
			for (String s : ms) {
				String starDate = s ;
				Map<String,String>	param = new HashMap<String,String>();
				param.put("cdrCondition.areaid", "573");
				param.put("cdrCondition.cdrlevel","");
				param.put("cdrCondition.cdrmonth", starDate);
				param.put("cdrCondition.cdrtype", "11");
				param.put("cdrCondition.pagenum", "1");
				param.put("cdrCondition.pagesize", "10000");
				param.put("cdrCondition.product_servtype","18");
				param.put("cdrCondition.productid", "1-1IBCFCMC");
				param.put("cdrCondition.productnbr", login.getLoginName());
				param.put("cdrCondition.randpsw", phoneCode);
				param.put("cdrCondition.recievenbr","");
				String text = cutil.post("http://zj.189.cn/zjpr/cdr/getCdrDetail.htm",param);
		    	if(text.contains("我的清单详情")){
		    	  Document doc  = Jsoup.parse(text);
	    		  Elements accountTables = doc.select("table[class=accountTotalT]");
	    		  if(accountTables.size()>0){
	    			 Element at =  accountTables.get(0);
	    			String aTableString =  accountTables.get(0).text();
//			    			if(aTableString.contains("客户姓名")){
//			    				String name = (String)redismap.get(CUST_NAME);
//			    				if(name==null){
//			    					redismap.put(CUST_NAME,at.select("tr").get(0).select("td").get(0).text().replaceAll("客户姓名：", ""));		
//			    				}
//			    			}
		    		}
		    		  
				 Elements tables = doc.select("table[class=cdrtable]");
				 for(int i = 0;i<tables.size();i++){ 
					 Element table1 =  tables.get(i);
					 String tableString = table1.text();
					 if(tableString.contains("对方号码")&&tableString.contains("呼叫类型")){
						 Elements trs = table1.select("tr");
						 if(trs.size()>2){
							 for (int j = 2; j < trs.size(); j++) {
								Elements tds =trs.get(j).select("td");
								if(tds.size()>10){
									String dfhm= tds.get(1).text();
									String hjlx= tds.get(2).text();
									String qssj= fdate(new RegexPaserUtil("<td align=\"center\" class=\"Pzone_details_dashed pzone_right_dashed\"><script>formateDate\\(\"","\"\\)</script></td>",tds.get(3).toString(),RegexPaserUtil.TEXTEGEXANDNRT).getText());
									String thsc= funcFmtTime(new RegexPaserUtil("<td align=\"center\" class=\"Pzone_details_dashed pzone_right_dashed\"><script>funcFmtTime\\(\"","\"\\)</script></td>",tds.get(4).toString(),RegexPaserUtil.TEXTEGEXANDNRT).getText());
									String thd= tds.get(5).text();
									String thlx= empsub(new RegexPaserUtil("<td align=\"center\" class=\"Pzone_details_dashed pzone_right_dashed\"><script>empsub\\(\"","\"\\)</script></td>",tds.get(6).toString(),RegexPaserUtil.TEXTEGEXANDNRT).getText());
									String bdmy= tds.get(7).text();
									String ctf= tds.get(8).text();
									String yh= tds.get(9).text();
									BigDecimal xj=getbF(new RegexPaserUtil("<td align=\"center\" class=\"Pzone_details_dashed pzone_right_dashed\"><script> getbF\\(\"","\"\\) </script>",tds.get(10).toString(),RegexPaserUtil.TEXTEGEXANDNRT).getText());
									DianXinDetail dxDetail = new DianXinDetail();
									UUID uuid = UUID.randomUUID();
									dxDetail.setId(uuid.toString());
									dxDetail.setcTime(StringToDate(qssj, "yyyy-MM-dd HH:mm:ss"));
									if(detail!=null&&dxDetail.getcTime().getTime()<=detail.getcTime().getTime()){
										b =false;
										break;
									}
									dxDetail.setTradeTime(Integer.parseInt(thsc));
									dxDetail.setTradeAddr(thd);
									dxDetail.setTradeType(thlx);
									dxDetail.setCallWay(hjlx);
									dxDetail.setRecevierPhone(dfhm);
									//dxDetail.setBasePay(new BigDecimal(jbthf));
									//dxDetail.setLongPay(new BigDecimal(ctthf));
									dxDetail.setAllPay(xj);
									dxDetail.setPhone(login.getLoginName());
									dxDetail.setIscm(0);
									dianXinDetailService.saveDianXinDetail(dxDetail);	
									}
							 	}
						 	}
					 	}
					 }
					 if(!b){
						 break;
					 }
		    	}
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			String warnType = WaringConstaint.ZJDX_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 月度账单
	 */
	public LinkedList<SpeakBillPojo> gatherMonthlyBill() {
		LinkedList<SpeakBillPojo> linkedList = getMonthlyBillAccess("http://zj.189.cn/zjpr/bill/getBillDetail.htm?pr_billDomain.product_id=10037027&pr_billDomain.query_type=1&pr_billDomain.bill_type=0&flag=htzd","pr_billDomain.bill_month");
		RegexPaserUtil rp = null;
		String xi = null;
		String text = null;
		SpeakBillPojo pojo = null;
		for (int i = 0; i < linkedList.size(); i++) {
			pojo = linkedList.get(i);
			text = pojo.getText();
			System.out.println(text);
			if(text!=null){
				try{
					DianXinTel tel = new DianXinTel();
					if(pojo.getMonth().equals(DateUtils.formatDate(new Date(), "yyyyMM"))){
						rp = new RegexPaserUtil("当前消费：","元",text,RegexPaserUtil.TEXTEGEXANDNRT);
						String xf = rp.getText();
						if(xf!=null){
							tel.setcAllPay(new BigDecimal(xf));	
						}
						rp = new RegexPaserUtil("查询时间：","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
						String dc = rp.getText();
						if(dc!=null){
							tel.setDependCycle(dc);
						}
						tel.setcName((String)redismap.get(CUST_NAME));
						pojo.setDianxinTel(tel);
						pojo.setText(null);
					}else if(text!=null&&text.contains("中国电信股份有限公司")&&text.contains("客户帐单")){
							rp = new RegexPaserUtil("本期费用合计：","元",text,RegexPaserUtil.TEXTEGEXANDNRT);
							String  hj = rp.getText();
							if(hj!=null){
								tel.setcAllPay(new BigDecimal(hj));	
									
							}
							rp = new RegexPaserUtil("帐单周期：","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
							String dc = rp.getText();
							if(dc!=null){
								tel.setDependCycle(dc);
							}
							
							rp = new RegexPaserUtil("来电显示功能费</td><td class=iv_1_0_0_0>","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
							String ldxs = rp.getText();
							if(ldxs!=null){
								tel.setLdxsf(new BigDecimal(ldxs));
							}
							rp = new RegexPaserUtil("套餐月基本费</td><td class=iv_1_0_0_0>","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
							String jbf = rp.getText();
							if(jbf!=null){
								tel.setZtcjbf(new BigDecimal(jbf));
							}
						//	UUID uuid = UUID.randomUUID();
						//	tel.setId(uuid.toString());
//							tel.setBaseUserId(currentUser);
						//	tel.setcTime(d);
						//	tel.setTeleno(login.getLoginName());
							tel.setcName((String)redismap.get(CUST_NAME));
							pojo.setDianxinTel(tel);
							pojo.setText(null);
					}
				
				} catch (Exception e) {
					writeLogByZhangdan(e);
				}
			}
		
		}
		return linkedList;
	}
	
	
	public boolean getTelDetailHtml() {
		//尊敬的用户，查不到账单数据，请稍候！  查询不到数据时
			List<String> ms = getMonth(6,"yyyyMM");
			DianXinTel maxTel = new DianXinTel();
			maxTel.setTeleno(login.getLoginName());
			Date d = StringToDate(ms.get(0), "yyyyMM");
			maxTel.setcTime(d);
			maxTel = dianXinTelService.getMaxTime(maxTel);
			//当月没查,因为不是整个月的话费
			for (int i = 1; i < ms.size(); i++) {
				String starDate = ms.get(i) ;
				d = StringToDate(starDate, "yyyyMM");
				if(maxTel!=null&&maxTel.getcTime().getTime()>=d.getTime()){
					break;
				}
				try {
					String text  = cutil.get("http://zj.189.cn/zjpr/bill/getBillDetail.htm?pr_billDomain.bill_month="+starDate+"&pr_billDomain.product_id=10037027&pr_billDomain.query_type=1&pr_billDomain.bill_type=0&flag=htzd");
					 if(text!=null&&text.contains("中国电信股份有限公司")&&text.contains("客户帐单")){
						RegexPaserUtil rp = new RegexPaserUtil("本期费用合计：","元",text,RegexPaserUtil.TEXTEGEXANDNRT);
						text = rp.getText();
						DianXinTel tel = new DianXinTel();
						if(text!=null){
							tel.setcAllPay(new BigDecimal(text));	
							tel.setDependCycle(starDate);
						}
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
//						tel.setBaseUserId(currentUser);
						tel.setcTime(d);
						tel.setTeleno(login.getLoginName());
						tel.setcName((String)redismap.get(CUST_NAME));
						dianXinTelService.saveDianXinTel(tel);
					}
				} catch (Exception e) {
					writeLogByZhangdan(e);
				}
			}
	
		return true;
	}
	
		public static void main(String[] args) {
			//String xm = fdate1("<td align=\"center\" class=\"Pzone_details_dashed pzone_right_dashed\"><script> getbF(\"39\") </script></td>");
//			System.out.println(getbF("39"));
			ZJDianxin zj = new ZJDianxin(new Login("15355733291","705353"), null);
			zj.index();
			zj.inputCode(zj.getImgUrl());
			zj.login();
		}
	
	
}
