package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.test.jsdx.RegexPaserUtil;
import com.lkb.thirdUtil.base.BasicCommonMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.pojo.SendRequestPojo;
import com.lkb.util.httpclient.util.CommonUtils;

/**
 * 北京电信比较特殊,暂且不需要填写验证码
 * @author fastw
 * @date	2014-8-20 上午9:36:13
 */
public class BJDianxin extends BasicCommonMobile{//BaseInfoMobile{
	
	public String qianfei = "BJDianxin.qianfei";
	

	// 验证码图片路径
	public static String imgurl = "http://www.189.cn/dqmh/createCheckCode.do?method=checkLoginCode&date=";
	public BJDianxin(Login login,String currentUser){
		super(login,currentUser);
		this.userSource = Constant.DIANXIN;
		this.constantNum = ConstantNum.comm_bj_dx;
	}

	
	public static void main(String[] args) throws Exception {
//		BJDianxin bj = new BJDianxin(new Login("13341188266","927672"),null);
//		BJDianxin bj =  new BJDianxin(new Login("18911895611","447781"),null);
//		bj.index();
////		bj.inputCode(bj.getContext().getImgUrl());
//		bj.logins();
//		if(bj.islogin()){
//			User user = bj.saveUser();
//			LinkedList<MonthlyBillMark>  list= bj.gatherMonthlyBill();
//			for (MonthlyBillMark m : list) {
//				System.out.println(m.getDianxinTel().getcTime()+"-----");
//			}
//		    System.out.println(user.getLoginName());
//			bj.sendPhoneDynamicsCode(new ResOutput());
//			System.out.println("请输入手机口令:");
//			bj.login.setPhoneCode(new Scanner(System.in).nextLine());
//			bj.checkPhoneDynamicsCode();
//			
//			LinkedList<MonthlyBillMark>  list = bj.gatherFlowBill();
//			for (MonthlyBillMark m : list) {
//				System.out.println(m.getDianXinFlow().getAllTime()+"-----");
//			}
//			
//			LinkedList<DianXinDetail> simpleList = bj.gatherCallHistory();
//			for (int i = 0; i < simpleList.size(); i++) {
//				//System.out.println(simpleList.get(i).getcTime());
//			}
			
//		}
		Date d=new Date();
		//Tue Oct 21 2014 09:33:17 GMT 0800
		DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println(format.format(d));
	}

	/* (non-Javadoc)
	 * @see com.lkb.thirdUtil.base.BaseInfo#init()
	 */
	public void init(){
		if(!context.isInit()){
			context.setImgUrl(imgurl);
//			 map.put("url",getAuthcode());
			 //如果需要输入验证码,就需要修改为上边一行代码
			 getAuthcode();//不能使用这步死循环
			 context.removeImgUrl();
			 context.setInit();
		 }
	}
	
	public  void login() throws  Exception{
		String text = null;
		Map<String, String> dxMap = new HashMap<String, String>();
		//北京电信居然可以输错验证码
//			text = cutil.post("http://www.189.cn/dqmh/createCheckCode.do?method=checkCheckCode&code="+login.getAuthcode()+"&number="+login.getLoginName()+"&date=1402663024208", dxMap);
//			if(text!=null&&text.length()>0){
//				msg = text;
//			}else{
			dxMap.put("uamType", "uamType");
			dxMap.put("method", "uamUnifiedLogin");
			dxMap.put("backUrl", "http://bj.189.cn/service/account/customerHome.action");
			dxMap.put("cityCode", "10001");
			dxMap.put("loginType", "201");
			dxMap.put("phoneNumber", login.getLoginName());
			dxMap.put("phonePassWord", login.getPassword());
			dxMap.put("phonePwdType", "01");
			dxMap.put("userType", "");
			dxMap.put("verificationCode", login.getAuthcode());
			text = cutil.post("http://www.189.cn/dqmh/Uam.do?method=uamUnifiedLogin&uamType=phone&loginType=201", dxMap);
			text = cutil.post(text, dxMap);
			text = cutil.post(text, dxMap);
//				System.out.println(text);
			RegexPaserUtil rp = new RegexPaserUtil("var errMsg = '","';",text,RegexPaserUtil.TEXTEGEXANDNRT);
			text = rp.getText();
			if(text!=null){
				output.setErrorMsg(text);
			}else{
				//该地址重定向会出问题,所以禁止重定向该地址
				cutil.setHandleRedirect(false);
				text = cutil.getResult(new SendRequestPojo("http://bj.189.cn/service/account/customerHome.action"));
//					cutil.setHandleRedirect(false);
//					text = cutil.get("http://bj.189.cn/service/account/customerHome.action");
				if(text!=null){
					text = cutil.get(text);
					//http://uam.bj.ct10000.com/http%3A%2F%2Fbj.189.cn%2Fservice%2Faccount%2FcustomerHome.action?UATicket=CC5B7098D928AE9997534238C99495F4D71C023CD0ACC696ABF00DA6BE32B8AE
					//上边返回结果比较特殊,原因是关闭重定向后,可能它自动拼接url导致
					text = URLDecoder.decode(text.replace("http://uam.bj.ct10000.com/", ""),"utf-8");
				
					text = cutil.get(text);
	//				System.out.println(text);
	//				//查询话费
	//				text = cutil.get("http://bj.189.cn/service/bill/oweQuery.action?requestFlag=asynchronism&p1QueryFlag=2");
	//				System.out.println("欠费"+text);
					text = cutil.get("http://bj.189.cn/service/bill/balanceQuery.action?requestFlag=asynchronism&p1QueryFlag=2",new CHeader("http://bj.189.cn/service/bill/billQuery.action"));
					JSONObject json = new JSONObject(text);
					JSONObject results =  json.getJSONObject("basicAccountBalance");
					String jphoneremain = results.getString("accountBalance");
					
					if(jphoneremain!=null){
						redismap.put(yue_key, jphoneremain);
						loginsuccess();
					}
				}
			}
    	if(islogin()){
        	addTask_1();
        	sendPhoneDynamicsCode();
    	}
	}
	/** 每個type 都是单独的一个线程
	 * @param type
	 */
	public  void startSpider(int type){
		switch (type) {
		case 1:
			saveUser(); //个人信息 必须在账单记录上边
			saveThisMonthTel();
			saveTel();
			break;
		case 2:
			saveCallHistory(); //通话记录	
			break;
		case 3:
			saveMessage();
			break;
		case 4:
			saveFlowBill();
			saveFlowList();
			break;
		default:
			break;
		}
	}
	private void saveThisMonthTel() {
		try{
			//json里面有本月账单详细的记录，
			Map<String,String> parmap=new HashMap<String,String>();
			parmap.put("p1QueryFlag", "1");
			parmap.put("requestFlag","asynchronism");
			//官网上是GMT时间，format格式也不太一致，但是估计那边拿到也是直接转换~能过
			parmap.put("shijian",new Date().toString());
			String text = cutil.post("http://bj.189.cn/service/bill/realtimeFeeQuery.action",new CHeader("http://bj.189.cn/service/bill/balanceQuery.action"),parmap);
			JSONObject json=new JSONObject(text);
			String realTimeFee=json.getString("realtimeFee");
			String nowTime=json.getString("nowTime");
			DianXinTel tel = new DianXinTel();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tel.setcTime(sdf.parse(nowTime.substring(0, 8)+"01 00:00:00"));
			tel.setTeleno(this.loginName);
			tel.setcAllPay(new BigDecimal(realTimeFee));
			Map map=new HashMap();
			map.put("cTime",tel.getcTime());
			map.put("teleno",this.loginName);
			List list=service.getDianXinTelService().getDianXinTelBybc(map);
			int update=0;
			if(list==null || list.size()==0){
				tel.setId(UUID.randomUUID().toString());
				service.getDianXinTelService().saveDianXinTel(tel);
			}
			else{
				DianXinTel oldBill=(DianXinTel) list.get(0);
				tel.setId(oldBill.getId());
				service.getDianXinTelService().update(tel);
			}
		}catch(Exception e){
			log.error("BeijingDianXin this month balanceFee error",e);
		}
	}


	public User gatherUser() {
		//话费余额中有欠费情况,如果余额是0元,就要判断是不是欠费情况
		double yueD = 0.0;
		double qianfei = 0.0;
		try{
			//http://bj.189.cn/service/bill/balanceQuery.action 话费地址
			yueD = Double.parseDouble(redismap.getString(yue_key));
			if(yueD<=0){
				//默认是post提交,欠费地址
				String text = cutil.get("http://bj.189.cn/service/bill/oweQuery.action?requestFlag=asynchronism&p1QueryFlag=2",new CHeader("http://bj.189.cn/service/bill/billQuery.action"));
				JSONObject json=new JSONObject(text);
				qianfei =  Double.parseDouble(json.getString("SOweTotal").trim());
				yueD = yueD-qianfei;
			}
		}catch(Exception e){
			yueD = 0.0;
			log4j.warn("话费解析出错#", e);
		}
		String myinfo =cutil.get("http://bj.189.cn/service/manage/modifyUserInfo.action?rand=0.20440118322304668");
		Document doc3 = Jsoup.parse(myinfo);
		Elements table = doc3.select("table[class=table-data-2 mgt-5]");
		if(table!=null){
			User user = new User();
			String cname = table.select("td").get(1).text();
			user.setRealName(cname);
			user.setUserName(cname);
			redismap.put(name_key, cname);
			user.setAddr(table.select("td").get(4).text());
			user.setEmail(table.select("td").get(6).text());
//				user.setRegisterDate(DateUtils.StringToDate(table.select("td").get(15).text().trim(), "yyyy-MM-dd HH:mm:ss"));
//				}catch(Exception e){
//					log.warn(this.loginName+"时间格式错误");
//				}
			user.setPhoneRemain(new BigDecimal(yueD));
			user.setPackageName(getBasicPackage());
			return user;
		}
		return null;
	}
	public String getBasicPackage(){
		String text = cutil.get("http://bj.189.cn/service/manage/custService.action");
		String s = "";
		try{
			RegexPaserUtil rp = new RegexPaserUtil("pricingPlanName\":\"", "\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			s = rp.getText();
		}catch(Exception e){
			log4j.warn("话费解析异常#", e);
		}
		return s;
	}

	@Override
	public LinkedList<MonthlyBillMark> gatherMonthlyBill() {
		LinkedList<MonthlyBillMark> simpleList = null; 
		//,账单不包含当月,所以不用修改当月状态
		Set<Date> set = getMonthlyBillMaxNumTel(0);
		String text = cutil.get("http://bj.189.cn/service/bill/billQuery.action");
//			System.out.println(text);
		if(text!=null){
			Document doc = Jsoup.parse(text);
			Elements els = doc.getElementById("userBill").children();
			Element el = null;
			String month = null;
			String consume = null;
			Date d = null;
			simpleList = new LinkedList<MonthlyBillMark>();
			for (int i = 0; i < els.size(); i++) {
				 el = els.get(i);
				 month = el.select("td").get(0).text().trim();
				d =  DateUtils.StringToDate(month, "yyyy 年 MM 月");
				if(CommonUtils.isNotEmpty(set)){
					if(set.contains(d)){
						continue;
					}
				}
				consume = el.select("td").get(1).text().trim().replace("元", "");
				DianXinTel tel = new DianXinTel();
//					tel.setBaseUserId(currentUser);
				tel.setcTime(d);
				tel.setTeleno(login.getLoginName());
				tel.setcAllPay(new BigDecimal(consume));
				tel.setDependCycle(month);
				tel.setcName(redismap.getString(name_key));
				simpleList.add(new MonthlyBillMark().setObj(tel));
			}
		}
		return simpleList;
	}
	/**
	 * 查询通话记录
	 * @throws Exception 
	 */
	public LinkedList<DianXinDetail> gatherCallHistory() throws Exception{
		List<String> ms = DateUtils.getMonths(7,"yyyy-MM");
		Date bigTime = getMaxCallTime();
		Map<String, String> dxMap2 = new HashMap<String, String>();
		dxMap2.put("billDetailType", "1");
		dxMap2.put("billDetailValidate", "true");
		dxMap2.put("downBillDetailType", "0");
		dxMap2.put("downEndTime", "");
		dxMap2.put("downStartTime", "");
		dxMap2.put("productSpecID", "");
		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,dxMap2);
		LinkedList<DianXinDetail>  listData = null;
		LinkedList<DianXinDetail>  list =  new LinkedList<DianXinDetail>();
		if(linkedList!=null){
			for (String string : linkedList) {
			  listData = gatherCallHistory_parse(string,bigTime);
			  list.addAll(listData);
			}
		}
		return list;
	}
	public LinkedList<DianXinDetail> gatherCallHistory_parse(String text,Date bigDate){
		String text1 = null;
		DianXinDetail dianxinDetail;
		 Elements els = null;
		 Elements tds = null ;
		 String hjlx = null;
		LinkedList<DianXinDetail> listData = new LinkedList<DianXinDetail>();
//		System.out.println(text);
		RegexPaserUtil rq = new RegexPaserUtil("费用小计</th></tr>","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
			text1 = rq.getText();
			if(text1!=null){
				text1 ="<table>"+text1+"</table>";
				Document doc  = Jsoup.parse(text1);
				els = doc.select("tr");
				 for(int j=0;j<els.size();j++){ 
					 try{
						dianxinDetail = new DianXinDetail();
				        tds = els.get(j).select("td");
						hjlx= tds.get(1).text();
						if(hjlx.contains("被叫")){
							dianxinDetail.setCallWay("被叫");
						}else if(hjlx.contains("主叫")){
							dianxinDetail.setCallWay("主叫");
						}
						dianxinDetail.setTradeType(tds.get(2).text());
						dianxinDetail.setTradeAddr(tds.get(3).text());
						dianxinDetail.setRecevierPhone(tds.get(4).text());
						dianxinDetail.setcTime(DateUtils.StringToDate(tds.get(5).text(), "yyyy-MM-dd HH:mm:ss"));
						if(bigDate!=null&&bigDate.getTime()>=dianxinDetail.getcTime().getTime()){
							continue;
						}
						dianxinDetail.setBasePay(new BigDecimal(tds.get(6).text()));
						dianxinDetail.setLongPay(new BigDecimal(tds.get(7).text()));
						dianxinDetail.setTradeTime(Integer.parseInt(tds.get(8).text().trim()));
						dianxinDetail.setAllPay(new BigDecimal(tds.get(9).text().trim()));
						dianxinDetail.setPhone( login.getLoginName());
			        	UUID uuid = UUID.randomUUID();
			        	dianxinDetail.setId(uuid.toString());
						listData.add(dianxinDetail);
					 }catch(Exception e){
						log4j.writeLogByHistory(e);
					 }
				 }
			}
		return listData;
		
	}
	
	
	
	/**
	 * @param ms  月份
	 * @param bigTime  最大时间
	 * @param dxMap2  参数
	 * @return  结果集字符串 按照从小的到大的顺序  如 2014 4.1-2014 5.1
	 * @throws Exception
	 */
	public LinkedList<String> gatherSpiderStrings(List<String> ms,Date bigTime,Map<String, String> dxMap2) throws Exception{
		String url = "http://bj.189.cn/service/bill/billDetailQuery.action";
		List<SendRequestPojo> listPojo  = new LinkedList<SendRequestPojo>();
		Date d = null;
		Map<String,String> pararm = null;
		for (int i = 0; i < ms.size(); i++) {
			if(i!=0){
				//如果当前最大时间比上一次采集的月份还要大,就终止,因为可以理解为后边已经采集了
				d = DateUtils.StringToDate(ms.get(i-1)+"-01", "yyyy-MM-dd");
				if(bigTime!=null&&bigTime.getTime()>=d.getTime()){
					break;
				}
			}
			pararm = new HashMap<String, String>(dxMap2);
			pararm.put("startTime",ms.get(i)+"-01" );
//			dxMap2.put("startTime", "2014-08-01");
			pararm.put("endTime",DateUtils.lastDayOfMonth(ms.get(i),"yyyy-MM","yyyy-MM-dd"));
			listPojo.add(new SendRequestPojo(url,pararm,new CHeader(url))); //添加任务
		}
		LinkedList<String> listTotal = cutil.getResult(listPojo);
//		for (String string : listTotal) {
//			System.out.println(string);
//		}
		LinkedList<String> listAll = new LinkedList<String>();
		int page = 0;
		for (int i = 0; i < listTotal.size(); i++) {
			String  text = listTotal.get(i);
			if(text!=null){
				listAll.addFirst(text);
				String pageStr = new RegexPaserUtil("class=\"float-right\">共", "页",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
				if(StringUtils.isNotBlank(pageStr)){
					try{
						page = Integer.parseInt(pageStr.trim());
						if(page>1){
							listPojo = new LinkedList<SendRequestPojo>();
							Map<String,String> pararm2 = new HashMap<String, String>(dxMap2);
							pararm2.put("startTime", ms.get(i)+"-01");
							pararm2.put("endTime", DateUtils.lastDayOfMonth(ms.get(i),"yyyy-MM","yyyy-MM-dd"));
							//多线程采集
							for (int j = 2; j <= page; j++) {
								listPojo.add(new SendRequestPojo(url+"?billPage="+j,pararm2,new CHeader(url))); //添加任务
							}
							listAll.addAll(1, cutil.getResult(listPojo));
						}
					}catch(Exception e){
						log4j.warn("北京电信采集异常#", e);
					}
				}
			}
			
		}
		return listAll;
	}
	
	// 随机短信登录
	public void checkPhoneDynamicsCode(ResOutput output) {
		Map<String, String> dxMap4 = new HashMap<String, String>();
		dxMap4.put("requestFlag", "asynchronism");
		dxMap4.put("sRandomCode", login.getPhoneCode());
		dxMap4.put("shijian", new Date().toString());
		String text = cutil.post("http://bj.189.cn/service/bill/billDetailQuery.action", dxMap4);
		//System.out.println(text);
		if(text.contains("\"tip\":\"随机短信码错误\"")){
			output.setErrorMsg("随机短信码错误");
		}else if(text.length()>300){
			if(!text.contains("http")){
				output.setStatus(1);
			}
		}else{
			output.setErrorMsg("验证失败!");
		}
		if(output.getStatus()==1){
        	addTask(2);//采集spiderStart 第二步
        	addTask(3);//采集spiderStart 第三步
        	addTask(4);//采集spiderStart 采集流量
    	}
     
	}
	/**
	 * 生成短信
	 * */
    public void sendPhoneDynamicsCode(ResOutput output){
		String text =  cutil.post("http://bj.189.cn/service/bill/validateRandomcode.action", null);
		if(text.contains("\"tip\":null")){
			output.setStatus(1);
			output.setErrorMsg("发送成功");
		}else if(text.contains("当日使用随机短信次数过多")){
			output.setErrorMsg("短信次数过多");
		}else{
			output.setErrorMsg("发送失败!");
		}
	}
	public  LinkedList<TelcomMessage> gatherMessage() throws Exception{
		List<String> ms =  DateUtils.getMonths(7,"yyyy-MM");
		Date bigTime = getMaxMessageTime();
		Map<String, String> dxMap2 = new HashMap<String, String>();
		dxMap2.put("billDetailType", "2");
		dxMap2.put("billDetailValidate", "true");
		dxMap2.put("downBillDetailType", "2");
		dxMap2.put("downEndTime", "");
		dxMap2.put("downStartTime", "");
		dxMap2.put("productSpecID", "");
		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,dxMap2);
		LinkedList<TelcomMessage>  listData = null;
		LinkedList<TelcomMessage>  list =  new LinkedList<TelcomMessage>();
		if(linkedList!=null){
			for (String string : linkedList) {
			  listData = gatherMessageHistory_parse(string,bigTime);
			  list.addAll(listData);
			}
		}
		return list;
	}
	
	 public LinkedList<TelcomMessage> gatherMessageHistory_parse(String callContent10,Date bigDate) throws Exception{
		LinkedList<TelcomMessage> listData = new LinkedList<TelcomMessage>();
			if(callContent10.contains("业务类型")&&callContent10.contains("收发类型")){
				Document doc  = Jsoup.parse(callContent10);
				 Elements tables = doc.select("table[class=table-data-3]");
				 for(int i = 1;i<tables.size();i++){   //是否i=0？
					 Element table1 =  tables.get(i);
					 String tableString = table1.text();
					 if(tableString.contains("业务类型")&&tableString.contains("收发类型")){
						 Elements trs = table1.select("tr");
						 for (int j = 1; j < trs.size(); j++) {
							Elements tds =trs.get(j).select("td");
							String ywlx= tds.get(1).text();
							String sflx= tds.get(2).text();
							String dfhm= tds.get(3).text();
							String fssj= tds.get(4).text();
							String fy= tds.get(5).text();
							TelcomMessage dxmessage = new TelcomMessage();
							UUID uuid = UUID.randomUUID();
							dxmessage.setId(uuid.toString());
							dxmessage.setCreateTs(new Date());
							dxmessage.setSentTime(DateUtils.StringToDate(fssj, "yyyy-MM-dd HH:mm:ss"));
							if(bigDate!=null&&bigDate.getTime()>=dxmessage.getSentTime().getTime()){
								continue;
							}
							dxmessage.setBusinessType(ywlx.indexOf("接收")!=-1?"接收":"发送");
							dxmessage.setRecevierPhone(dfhm);
							dxmessage.setAllPay(Double.valueOf(fy));
							dxmessage.setPhone(this.loginName);
							listData.add(dxmessage);
						}
					 }
				 }
			}
		
	 return listData;
   }


	@Override
	public LinkedList<? extends Object> gatherFlowList() throws Exception {
		
		//http://bj.189.cn/service/bill/billDetailQuery.action
		//billDetailValidate=true&productSpecID=&downBillDetailType=0&downStartTime=&downEndTime=&billDetailType=1&startTime=2014-10-01&endTime=2014-10-11
		List<String> ms = DateUtils.getMonths(7,"yyyy-MM");
		Date bigTime = getMaxFlowTime();
		Map<String, String> dxMap2 = new HashMap<String, String>();
		dxMap2.put("billDetailValidate", "true");
		dxMap2.put("productSpecID", "");
		dxMap2.put("downBillDetailType", "0");
		dxMap2.put("downStartTime", "");
		dxMap2.put("downEndTime", "");
		dxMap2.put("billDetailType", "3");
		
		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,dxMap2);
		LinkedList<DianXinFlowDetail>  listData = null;
		LinkedList<DianXinFlowDetail>  list =  new LinkedList<DianXinFlowDetail>();
		if(linkedList!=null){
			for (String string : linkedList) {
			  listData = gatherFlowList_parse(string,bigTime);
			  list.addAll(listData);
			}
		}
		return list;
	}
	
	public LinkedList<DianXinFlowDetail> gatherFlowList_parse(String text,
			Date bigDate) {
		Elements trs = null;
		Elements tds = null;
		LinkedList<DianXinFlowDetail> listData = new LinkedList<DianXinFlowDetail>();

		Document doc = Jsoup.parse(text);
		//System.out.println(doc.toString());
		if (doc.toString().contains("业务类型")) {
			
			Elements tables = doc.select("table");
			
			Element table1=null;
			for(int j=0;j<tables.size();j++)
				if(tables.get(j).text().contains("网络类型")){
					table1=tables.get(j);
					break;
				}
			if(table1==null)
				return listData;
			
			trs = table1.select("tr");
			//第0个是标头
			for (int i = 1; i < trs.size(); i++) {
				tds = trs.get(i).select("td");

				String beginTime = tds.get(1).text().trim();// 开始时间
				long tradeTime = StringUtil.flowTimeFormat(tds.get(2).text()
						.trim());// 上网时长
				Double flow = StringUtil.flowFormat(tds.get(3).text().trim());// 总流量

				String netType = tds.get(4).text().trim();// 通讯类型
				String location = tds.get(5).text().trim();// 通信地点
				String fee = tds.get(6).text().trim().replaceAll("元", "");// 费用（元）
				String business = tds.get(7).text().trim().replaceAll("-", "");// 使用业务
				Date beginTimeDate = null;
				BigDecimal feeDecimal = new BigDecimal(0);
				try {
					beginTimeDate = DateUtils.StringToDate(beginTime,
							"yyyy-MM-dd HH:mm:ss");
					feeDecimal = new BigDecimal(fee);
				} catch (Exception e) {
					log4j.warn("feeDecimal", e);
				}

				DianXinFlowDetail obj = new DianXinFlowDetail();

				UUID uuid = UUID.randomUUID();
				obj.setId(uuid.toString());
				obj.setPhone(login.getLoginName());
				obj.setBeginTime(beginTimeDate);
				if (bigDate != null
						&& bigDate.getTime() >= obj.getBeginTime().getTime()) {
					continue;
				}
				obj.setFee(feeDecimal);
				obj.setNetType(netType);
				obj.setTradeTime(tradeTime);
				BigDecimal flows = new BigDecimal(0);
				try {
					flows = new BigDecimal(flow);
				} catch (Exception e) {
					log4j.warn("folws解析异常", e);
				}
				obj.setFlow(flows);
				obj.setLocation(location);
				obj.setBusiness(business);
				listData.add(obj);
			}

		}
		return listData;
	}


	public  List<String> gatherOnlineStrings(List<String> ms, Map<String, String> dxMap){
		List<SendRequestPojo> pojolist = new ArrayList<SendRequestPojo>();
		
		return cutil.getResult(pojolist);
	}
	
	/**
	 * 存放结果
	 * @param ms
	 * @return
	 */
	public LinkedList<MonthlyBillMark> getLinkedList(Map<String, String> dxMap){
		LinkedList<MonthlyBillMark> listMark = getSpiderMonthsMark(true, "yyyy-MM",7,1);
		List<SendRequestPojo> pojolist = new ArrayList<SendRequestPojo>();
		for (int i = 0; i < listMark.size(); i++) {
			Map<String, String> dxMap2 = new HashMap<String, String>(dxMap);
			dxMap2.put("startTime",listMark.get(i).getMonth() +"-01" );
			dxMap2.put("endTime",DateUtils.lastDayOfMonth(listMark.get(i).getMonth(),"yyyy-MM","yyyy-MM-dd"));
			pojolist.add(new SendRequestPojo("http://bj.189.cn/service/bill/billDetailQuery.action", dxMap2));
		}
		List<String> list =  cutil.getResult(pojolist);
		for (int i = 0; i < list.size(); i++) {
			listMark.get(i).setText(list.get(i));
		}
		return listMark;
	}
	@Override
	public LinkedList<MonthlyBillMark> gatherFlowBill() {
		//http://bj.189.cn/service/bill/billDetailQuery.action
		//billDetailValidate=true&productSpecID=&downBillDetailType=0&downStartTime=&downEndTime=&billDetailType=1&startTime=2014-10-01&endTime=2014-10-11
		Map<String, String> dxMap2 = new HashMap<String, String>();
		dxMap2.put("billDetailValidate", "true");
		dxMap2.put("productSpecID", "");
		dxMap2.put("downBillDetailType", "0");
		dxMap2.put("downStartTime", "");
		dxMap2.put("downEndTime", "");
		dxMap2.put("billDetailType", "3");
		LinkedList<MonthlyBillMark> listMark = getLinkedList(dxMap2);
		MonthlyBillMark mark = null;
		String html = null;
		for (int i = 0; i < listMark.size(); i++) {
			mark = listMark.get(i);
			html = mark.getText();
			//System.out.println(Jsoup.parse(html));

			if (html != null) {
				Document doc = Jsoup.parse(html);
				if(doc.toString().contains("起止日期")){
					try{
						Elements tables = doc.select("table");
						Element table1=null;
						for(int j=0;j<tables.size();j++)
							if(tables.get(j).text().contains("总流量")){
								table1=tables.get(j);
								break;
							}
						if(table1==null)
							continue;
						Elements trs = table1.select("tr");
						String dependCycle = trs.get(2).select("td").get(0).text().replaceAll("起止日期：", "");
						String month = trs.get(2).select("td").get(1).text().trim();
//						Date queryMonth = DateUtils.StringToDate(month.substring(0, 7).replaceAll("年", "").replaceAll("月", ""), "yyyyMM");
						String allFlow = trs.get(3).select("td").get(0).text().replaceAll("总流量：", "");
						int allTime =  TimeUtil.timetoint_HH_mm_ss(trs.get(3).select("td").get(1).text().replaceAll("总时长：", "").replaceAll("小时", ":").replaceAll("分", ":").replaceAll("秒", ""));
						String allPay = trs.get(4).select("td").get(0).text().replaceAll("总费用：", "").replaceAll("\\(元\\)", "").trim();
						BigDecimal allFlows=new BigDecimal(com.lkb.util.StringUtil.flowFormat(allFlow));
						BigDecimal allPays = new BigDecimal(allPay);
						DianXinFlow dianXinFlow = new DianXinFlow();
						dianXinFlow.setAllFlow(allFlows);
						dianXinFlow.setAllPay(allPays);
						dianXinFlow.setAllTime(new BigDecimal(allTime));
						dianXinFlow.setDependCycle(dependCycle);
						dianXinFlow.setPhone(login.getLoginName());
						dianXinFlow.setQueryMonth(mark.getFormatDate());
						mark.setObj(dianXinFlow);
					}catch(Exception e){
						log4j.writeLogByFlowBill(e);
					}
				}
			}
		}

		return listMark;
	}
    
}
