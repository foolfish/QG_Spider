package com.lkb.thirdUtil.yd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.thirdUtil.base.BasicCommonMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.FileUtil;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class GDYiDong extends BasicCommonMobile{//BaseInfoMobile{
	protected static Logger log = Logger.getLogger(GDYiDong.class);

	public String index = "https://gd.ac.10086.cn/ucs/login/signup.jsps";
	// 验证码图片路径
	public static String imgurl = "https://gd.ac.10086.cn/ucs/captcha/image/reade.jsps?sds=";
	
	public static String messageKey  = "GDYIDONG.MESSAGE.KEY";
	public static String flowKey  = "GDYIDONG.FLOW.KEY";

	
	public static void main(String[] args) throws Exception {
		Login login = new Login("13682668174","22116697");
//		Login login = new Login("18320406972","63759291");
//		Login login = new Login("18319241279","87788842");//已经登录
//		Login login = new Login("13763379639","210057");
		GDYiDong gd = new GDYiDong(login, null);
		gd.index();
		gd.inputCode(imgurl);
		ResOutput put = gd.logins();
		if(gd.islogin()){
		   gd.sendPhoneDynamicsCode(new ResOutput());
		   System.out.println("输入口令:");
		   gd.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
		   gd.checkPhoneDynamicsCode();
//		   gd.test();
		}else{
			System.out.println(put.getErrorMsg());
		}
			
////		gd.sendLoginSms();
////		login.setPassword(new Scanner(System.in).nextLine());
			gd.listHtml = new ArrayList<String>();
			gd.listHtml.add(FileUtil.readFile("d:/t.txt"));
			gd.initSpiderParse(gd.listHtml.get(0), "201409");
		
		
	}
	public void test(){
	//此地址位之前接口遗留下来地址,具体我也说不清楚	
		String text = cutil.get("http://gd.10086.cn/commodity/servicio/nostandardserv/realtimeListSearch/ajaxRealQuery.jsps?&month=201409&monthListType=0");
		System.out.println(text);
		
	}
	
	public GDYiDong(Login login,String currentUser) {
		super(login,currentUser);
		this.userSource  = Constant.YIDONG;
		this.constantNum = ConstantNum.comm_gd_yidong;
	}
	@Deprecated
	public void sendLoginSms(){
		Map<String,String> param = new LinkedHashMap<String, String>();
		param.put("mobile",login.getLoginName());
		param.put("dt", "23");
		System.out.println(cutil.post("https://gd.ac.10086.cn/ucs/captcha/dpwd/send.jsps", param));
		
	}
	public void init(){
		if(!context.isInit()){
			CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"gd.ac.10086.cn");
			h.setRespCharset("UTF-8");
			String text = cutil.get(index,h);
//			System.out.println(text);
			if(text!=null){
				context.setImgUrl(imgurl);
				 RegexPaserUtil rp = new RegexPaserUtil("\"e\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
				 String e = rp.getText();
				 if(e!=null){
					 rp = new RegexPaserUtil("maxdigits\":",",",text,RegexPaserUtil.TEXTEGEXANDNRT);
					 String maxdigits = rp.getText();	
					 rp = new RegexPaserUtil("\"n\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
					 String n = rp.getText();
					 Map<String,String> map = new HashMap<String,String>();
					 map.put("maxdigits", maxdigits);
					 map.put("e", e);
					 map.put("n", n);	
					 redismap.put("jsmap", map);//根据实际需要存放
					 context.setInit();
				 }
			}
		}
	}
	
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(int type){
		switch (type) {
		case 1:
			saveUser(); //个人信息
			break;
		case 2:
			initSpiderParam();
			saveCallHistory(); //通话记录
			saveMessage();//此部短信依赖于上一步saveCallHistory拿到的所有数据
			saveFlowBill();
			saveFlowList();
			saveTel();
			break;
		default:
			break;
		}
	}
	
	
	public void initSpiderParam(){
		onlineList = new LinkedList<MobileOnlineList>();
		detailList = new LinkedList<MobileDetail>();
		messageList = new LinkedList<MobileMessage>();
		monthlyList = getSpiderMonthsMark(1);
	    detailTime = getMaxCallTime();
		messageTime = getMaxMessageTime();
		onlineTime = getMaxFlowTime();
		listHtml = new ArrayList<String>();
		telList = new LinkedList<MobileTel>();
		billList = new LinkedList<MobileOnlineBill>();
		ms = DateUtils.getMonths(6,"yyyyMM");
		for (String string : ms) {
			//此地址位之前广东接口升级中遗留下来地址,具体我也说不清楚@fastw
			listHtml.add(cutil.get("http://gd.10086.cn/commodity/servicio/nostandardserv/realtimeListSearch/ajaxRealQuery.jsps?&month="+string+"&monthListType=0"));
		}
		for (int i = listHtml.size()-1; i >=0; i--) {
			if(listHtml.get(i)!=null&&!listHtml.get(i).contains("今天历史清单查询次数超过6次")){
//					 gatherHistory_parse(string,totalFowList,totalFeesList);
				initSpiderParse(listHtml.get(i), ms.get(i));
			}
		}
	}
    public void initSpiderParse(String text,String month){
    	String text1 = null;
		JSONObject jsonObj = null;
		try{
//			RegexPaserUtil rq= new RegexPaserUtil("dataMap.put\\(\\\\\"\\w*\\\\\",","\\);");
			RegexPaserUtil rq= new RegexPaserUtil("dataMap.put\\(\\\\","\\);");
			rq.reset(text);
			BigDecimal decimal = null;
			BigDecimal total = new BigDecimal(0);
			MobileTel tel = new MobileTel();
			String data = null;
			String key = null;
			while(rq.hasNext()){
				text1 = rq.getNextText();
				text1 = text1.replace("\\","");
				int startIndex = text1.indexOf(",");
				key = text1.substring(0,startIndex);
				data = text1.substring(startIndex+1,text1.length());
				jsonObj = new JSONObject(data);
				decimal =  new BigDecimal(jsonObj.getString("total"));
				total = total.add(decimal);
				if(key.contains("call")){
					 gatherCallHistory_parse(jsonObj.getJSONArray("data"),month);
					 tel.setTcwyytxf(decimal);
				}else if(key.contains("msg")){
					gatherMessageHistory_parse(jsonObj.getJSONArray("data"),month);
					tel.setTcwdxf(decimal);
				}else if(key.contains("net")){
					gatherFlowList_parse(jsonObj.getJSONArray("data"),month);
				}else if(key.contains("package")){
					tel.setTcgdf(decimal);
				}
//				System.out.println(text1);
			}
			tel.setcAllPay(total);
			tel.setcName(redismap.getString(name_key));
			tel.setcTime(DateUtils.StringToDate(month, "yyyyMM"));
			telList.add(tel);
		 }catch(Exception e){
			log4j.error(text, "ROOT", e);
		 }
    	
    }
	public LinkedList<MobileDetail> gatherCallHistory(){
		return detailList;
	}
	

	private LinkedList<MobileOnlineList> onlineList = null;
	private LinkedList<MobileMessage> messageList = null;
	private LinkedList<MobileDetail> detailList = null;
	/**电话账单和流量账单通用**/
	private LinkedList<MonthlyBillMark> monthlyList = null;
	private LinkedList<MobileTel> telList = null;
	private LinkedList<MobileOnlineBill> billList = null;
	
	private Date detailTime;
	private Date messageTime;
	private Date onlineTime;
	private List<String> listHtml = null;
	private List<String> ms = null;
	
	public void gatherMessageHistory_parse(JSONArray jsonArray,String month) throws JSONException{
		JSONArray array = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			try{
					array = jsonArray.getJSONArray(i);
					//防止出现["","","",""]形式
					if(array.get(0).toString().length()>1){
					Date time = DateUtils.StringToDate(month.substring(0,4)+"-"+array.get(0), "yyyy-MM-dd HH:mm:ss");
					 if(isContinue(messageTime,time)){
						 return;
					 }
				 	MobileMessage  mMessage = new MobileMessage();
				 	mMessage.setSentAddr(array.get(1).toString());
				 	mMessage.setRecevierPhone(array.get(2).toString());
					mMessage.setTradeWay(array.get(3).toString().contains("发")?"发送":"接收");//网页默认是发送
					mMessage.setSentTime(time);
					mMessage.setPhone(login.getLoginName());
					try{
						mMessage.setAllPay(new BigDecimal(array.getString(7)));
					}catch(Exception e){
						log4j.warn(jsonArray.toString(), e);
					}
					UUID uuid = UUID.randomUUID();
					mMessage.setId(uuid.toString());
					mMessage.setCreateTs(new Date());
					messageList.add(mMessage);
				}
			}catch(Exception e){
				log4j.writeLogByMessage(e);
			}
		}
	}
	public void gatherFlowList_parse(JSONArray jsonArray,String month) throws JSONException{
		MobileOnlineBill mob = new MobileOnlineBill();
		mob.setMonthly(DateUtils.StringToDate(month, "yyyyMM"));
		//数据总流量
		Long totalFlow = 0l;
		//通信费
	   BigDecimal trafficCharges = new BigDecimal(0);
		JSONArray array = null;
		boolean status = true;
		for (int i = 0; i < jsonArray.length(); i++) {
			try{
				array = jsonArray.getJSONArray(i);
				//防止出现["","","",""]形式
				if(array.get(0).toString().length()>1){
					Date time = DateUtils.StringToDate(month.substring(0,4)+"-"+array.get(0), "yyyy-MM-dd HH:mm:ss");
					 if(isContinue(onlineTime,time)){
						 return;
					 }
					 MobileOnlineList mbOnline = new MobileOnlineList();
					UUID uuid = UUID.randomUUID();
					mbOnline.setId(uuid.toString());
					mbOnline.setPhone(login.getLoginName());
					mbOnline.setcTime(time);
		
					mbOnline.setTradeAddr(array.get(1).toString());
					mbOnline.setcTime(time);
					mbOnline.setOnlineType(array.get(2).toString());
					mbOnline.setOnlineTime(StringUtil.flowTimeFormat(array.get(3).toString()));
					mbOnline.setTotalFlow(Math.round(StringUtil.flowFormat(array.get(4).toString())));
					totalFlow+=mbOnline.getTotalFlow();
					mbOnline.setCheapService(array.get(6).toString());
					try{
						mbOnline.setCommunicationFees(new BigDecimal(array.get(7).toString()));
					}catch(Exception e){
						log4j.warn(jsonArray.toString(), e);
						status = false;
					}
					trafficCharges = trafficCharges.add(mbOnline.getCommunicationFees());
					onlineList.add(mbOnline);
	//				totalFowList.add(mbOnline.getTotalFlow());
	//				totalFeesList.add(mbOnline.getCommunicationFees());
				}
			}catch(Exception e){
				log4j.writeLogByMessage(e);
				status = false;
			}
		}
		mob.setTotalFlow(totalFlow);
		mob.setTrafficCharges(trafficCharges);
		mob.setPhone(this.loginName);
		//出错后不保存
		if(status){
			billList.add(mob);	
		}
	}
	public void gatherCallHistory_parse(JSONArray jsonArray,String month) throws JSONException{
		JSONArray array = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			try{
				array = jsonArray.getJSONArray(i);
				//防止出现["","","",""]形式
				if(array.get(0).toString().length()>1){
					Date time = DateUtils.StringToDate(month.substring(0,4)+"-"+array.get(0), "yyyy-MM-dd HH:mm:ss");
					 if(isContinue(detailTime,time)){
						 return;
					 }
					 MobileDetail  md = new MobileDetail();
					 md.setTradeAddr(array.get(1).toString());//深圳
					 md.setTradeWay(array.get(2).toString());
					md.setRecevierPhone(array.get(3).toString());
			    	md.setcTime(time);
			    	md.setTradeTime(TimeUtil.timetoint_HH_mm_ss(array.get(4).toString()));
			    	md.setTradeType(array.get(5).toString());
			    	md.setTaocan(array.get(6).toString());
			    	try{
			    		md.setOnlinePay(new BigDecimal(array.getString(8)));
			    	}catch(Exception e){
			    		log4j.warn(jsonArray.toString(), e);
			    	}
			    	md.setPhone( login.getLoginName());
			    	UUID uuid = UUID.randomUUID();
					md.setId(uuid.toString());
					detailList.add(md);
				}
				
			}catch(Exception e){
				log4j.writeLogByHistory(e);
			}
		}
	}
	
	
	
	public BigDecimal getYue(){
		BigDecimal phoneremain = new BigDecimal("0");
		try {
			String zd = query("ACCOUNTS_BALANCE_SEARCH");
			 if(zd!=null&&zd.contains("当前账户余额")){
					String[] s = zd.split("amount=convertPointToYuan\\(");
					if(s.length>1){
						String[] ss =s[1].split("\\+");
						phoneremain = new BigDecimal(ss[0]).divide(new BigDecimal("100"));
					}	
			}
		} catch (Exception e) {
			log4j.writeLogByMyInfo(e);
		}
		return phoneremain;
	}
	public String getBasicPackage(){
		String result = "";
//		System.out.println("");
		try {
			String zd = query("PACKAGEMANAGEMENT");
			if(zd!=null&&zd.contains("套餐剩余分钟数")){
				Document doc = Jsoup.parse(zd);
				Elements els = doc.select("tbody");
				result = els.get(0).select("td").get(0).text();
				if(result==null||result.length()<3){
					result = els.get(1).select("td").get(0).text();
					if(result==null||result.length()<=3){
						result = els.get(2).select("td").get(0).text();	
					}
				}
			}
		} catch (Exception e) {
			log4j.writeLogByMyInfo(e);
			result = "";
		}
		return result;
	}

	// 首页登录
	public void  login() {
		boolean b = login1();
//			login_testSms();
		if(b){
			loginsuccess();
			sendPhoneDynamicsCode();
			addTask(1);
    	}
	}
	@Deprecated
	private void login_testSms(){
		CHeader h = new CHeader(CHeaderUtil.Accept_other,"https://gd.ac.10086.cn/ucs/login/signup.jsps",CHeaderUtil.Content_Type__urlencoded,"gd.ac.10086.cn",true);
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("loginType", "1"); 
		param.put("mobile", login.getLoginName()); 
		param.put("password", password(login.getPassword())); 
		param.put("imagCaptcha",  login.getAuthcode()); 
		param.put("cookieMobile", ""); 
		param.put("bizagreeable", "on"); 
		param.put("exp", ""); 
		param.put("cid", ""); 
		param.put("area", ""); 
		param.put("resource", ""); 
		param.put("channel", "0"); 
		param.put("reqType", "0"); 
		param.put("backURL", ""); 
		String text = cutil.post("https://gd.ac.10086.cn/ucs/login/register.jsps", h, param);
		if(getJSESSIONID()!=null){
			if(getECOPPJSESSIONID()!=null){
				login4();		
			}
		}
	}
	
	// 随机短信登录
	public void checkPhoneDynamicsCode(ResOutput output) {
//		addTask(2);
		String path = "https://gd.ac.10086.cn/ucs/second/authen.jsps";
		// 返回结果
		//{"content":"http://gd.10086.cn/my/REALTIME_LIST_SEARCH.shtml?dt=1413820800000","type":"ucs.server.location.url"}
		Map<String,String> param = new LinkedHashMap<String,String>();
		String pass =password(login.getPhoneCode());
		//---------------------------------------//
		param.put("dpwd", pass); 
		param.put("type", "2"); 
//		param.put("cid", "10002"); 
		param.put("cid", "10003"); 
		param.put("channel", "0"); 
		param.put("reqType", "0"); 
		param.put("backURL", "http%3A%2F%2Fgd.10086.cn%2Fmy%2FREALTIME_LIST_SEARCH.shtml"); 
		String text = cutil.post(path, param);
		 if(text!=null){
//			if(!text.contains("telExpenseMonthListInquire.jsp")){
//				param.put("dpwd", pass); 
//				text = cutil.post(path, param);
//			}
			RegexPaserUtil rp = new RegexPaserUtil("content\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			String url = rp.getText();
			if(url!=null&&url.contains("my/REALTIME_LIST_SEARCH.shtml")){
				output.setErrorMsg("验证成功!");
				output.setStatus(1);
			}else{
				text = new RegexPaserUtil("<span class=\"errors\">", "</span>",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
				if(text==null){
					output.setErrorMsg("验证失败,请重试!");
				}else{
					output.setErrorMsg(text);
				}
			}	
		}else{
			output.setErrorMsg("验证失败,请重试!");
		}
		if(output.getStatus()==1){
        	addTask(2);
    	}
	}
	


	
	//得到NGWEBJSESSIONID 的id
	public  String getNGWEBJSESSIONID(String url){
		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"gd.10086.cn");
		return cutil.get(url, h);
	}
	
	
	
	/**
	 * 生成短信
	 * */
	public void sendPhoneDynamicsCode(ResOutput output) {
		CHeader h = new CHeader(CHeaderUtil.Accept_other,null,CHeaderUtil.Content_Type__urlencoded,"gd.ac.10086.cn",true);
		try {
			Map<String,String> param = new LinkedHashMap<String,String>();
			param.put("dt", "23"); 
			param.put("mobile", login.getLoginName()); 
			String text = cutil.post("https://gd.ac.10086.cn/ucs/captcha/dpwd/send.jsps", h, param);
			 if(text!=null && text.contains("动态密码已发送")){
				 output.setErrorMsg("动态密码已发送");
				 output.setStatus(1);
			 } 
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isBlank(output.getErrorMsg())){
			output.setErrorMsg("发送失败!");
		}
	}
	

	public  boolean login1(){
		CHeader h = new CHeader(CHeaderUtil.Accept_other,"https://gd.ac.10086.cn/ucs/login/signup.jsps",CHeaderUtil.Content_Type__urlencoded,"gd.ac.10086.cn",true);
		h.setUser_Agent(CHeaderUtil.User_Agent.get(0));
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("loginType", "2"); 
		param.put("mobile", login.getLoginName()); 
		param.put("password", password(login.getPassword())); 
		param.put("imagCaptcha",  login.getAuthcode()); 
		param.put("cookieMobile", ""); 
		param.put("bizagreeable", "on"); 
		param.put("exp", ""); 
		param.put("cid", ""); 
		param.put("area", ""); 
		param.put("resource", ""); 
		param.put("channel", "0"); 
		param.put("reqType", "0"); 
		param.put("backURL", ""); 
		String text = cutil.post("https://gd.ac.10086.cn/ucs/login/register.jsps", h, param);
		if(text!=null){
    		if(text.contains("验证码错误")){
    			output.setErrorMsg("验证码错误");
    		}else if(text.contains("密码错误，请重新输入")){
    			output.setErrorMsg("密码错误");
    		}else if(text.contains("您当天验证密码错误次数已达上限，请明天再试")){
    			output.setErrorMsg("您当天验证密码错误次数已达上限，请明天再试");
    		}else if(text.contains("\"content\":\"http://gd.10086.cn/\"")){
    			if(getJSESSIONID()!=null){
    				if(getECOPPJSESSIONID()!=null){
    					boolean b = false;
    					for (int i = 0; i < 3; i++) {
    						b  = login4();
    						if(b){
    							break;
    						}
						}
    					return b;
    				}
    			}
    		}else if(text.contains("\"content\":\"http://gd.10086.cn/commodity/callback/login?cid=")){
    			
    		}
	    }
		return false;
	}
	
	public  boolean login4(){
		boolean b = false;
		CHeader h = new CHeader("http://gd.10086.cn/my/myService/myBasicInfo.shtml");
		h.setUser_Agent(CHeaderUtil.User_Agent.get(0));
		String text = jibenxinxi();
//		System.out.println(text);
		if(text!=null&&text.contains("我的个人中心")){
			b = true;
		}else{
			if(text.substring(0,5).contains("http")){
	    		text = cutil.getURL(text,h);
	    	}
			Map<String,String> param = new LinkedHashMap<String,String>();
			if(text!=null){
				Document doc = Jsoup.parse(text);
				Element el = doc.getElementById("signup");
				param.put("backURL", el.select("input[name=backURL]").val()); 
				param.put("reqType", el.select("input[name=reqType]").val()); 
				param.put("channel",el.select("input[name=channel]").val()); 
				param.put("cid", el.select("input[name=cid]").val()); 
				param.put("area",el.select("input[name=area]").val()); 
				param.put("resource", el.select("input[name=resource]").val()); 
				param.put("loginType", el.select("input[name=loginType]").val()); 
				param.put("optional", el.select("input[name=optional]").val()); 
				param.put("exp", el.select("input[name=exp]").val()); 
			}
			text = cutil.post("https://gd.ac.10086.cn/ucs/login/signup.jsps",h, param);
		    if(text!=null){
		    	try {
		    		String text1 = StringUtil.subStr("certificate=", "&backURL",text);
		    		text = text.replace(text1, "").replace("&certificate=", "");
		    		param = new HashMap<String, String>(1);
		    		//涉及到特殊字符+号需要特别注意
		    		param.put("certificate", URLDecoder.decode(text1,"utf-8"));//get方式容易出错,默认get方式
		    		text = cutil.post(URLDecoder.decode(URLDecoder.decode(text,"utf-8"),"utf-8"), param);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
//		    	System.out.println(text);
		    }
		}
	    return b;
	}

	/**得到JSESSIONID 的id*/
	public  String getJSESSIONID(){
		CHeader h = new CHeader(CHeaderUtil.Accept_other,"http://gd.10086.cn/",null,"gd.10086.cn",true);
		Map<String,String> param = new LinkedHashMap<String,String>();
		return cutil.post("http://gd.10086.cn/common/include/public/isOnline.jsp?_="+new Date().getTime(), h, param);
	
	}
	//得到getECOPPJSESSIONID 的id
	public  String getECOPPJSESSIONID(){
		CHeader h = new CHeader(CHeaderUtil.Accept_other,"http://gd.10086.cn/",null,"gd.10086.cn",true);
	    return cutil.get("http://gd.10086.cn/commodity/commons/isonline.jsp?_="+new Date().getTime(), h);
	}
	
	/**我的余额 ,套餐*/
	public  String query(String servCode){
		//可判断当前账户余额
		CHeader  h = new CHeader(CHeaderUtil.Accept_other,"http://gd.10086.cn/my/ACCOUNTS_BALANCE_SEARCH.shtml",CHeaderUtil.Content_Type__urlencoded+";charset-UTF-8","gd.10086.cn",true);
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("servCode", servCode); 
		param.put("operaType", "QUERY"); 
		String date = DateUtils.formatDate(new Date(), "yyyyMMdd");
		param.put("Payment_startDate",date+"000000"); 
		param.put("Payment_endDate", date+"235959"); 
		return cutil.post("http://gd.10086.cn/commodity/servicio/servicioForwarding/query.jsps", h, param);
	}
	/**我的基本信息*/
	public  String  jibenxinxi(){
		CHeader  h = new CHeader("http://gd.10086.cn/my/myService/myBasicInfo.shtml");
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("servCode", "MY_BASICINFO"); 
		param.put("operaType", "QUERY"); 
		return cutil.post("http://gd.10086.cn/commodity/servicio/servicioForwarding/query.jsps", h, param);
	}
	public  String password(String pass) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("js");   
		String rsaPath = InfoUtil.getInstance().getInfo("road","tomcatWebappPath")+"/js/yd/gd_pwd.js";
		Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		 File f = new File(rsaPath);
		 FileInputStream fip = null;
		try {
			fip = new FileInputStream(f);
		
		// 构建FileInputStream对象
			InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
			// 执行指定脚本
			engine.eval(reader);
			if(engine instanceof Invocable) {
				Invocable invoke = (Invocable)engine;
				//调用merge方法，并传入两个参数
				// c = merge(2, 3);
				int m =Integer.parseInt(jsmap.get("maxdigits").toString());
				Object c = (Object)invoke.invokeFunction("enform",pass,m,jsmap.get("e").toString(),jsmap.get("n").toString());
			//	Object c = (Object)invoke.invokeFunction("sss",pass,Integer.parseInt(jsmap.get("maxdigits").toString()),jsmap.get("e").toString(),jsmap.get("n").toString());
			//	System.out.println("c = " + c);// 525AF936132AEDA4A4B9A3C29788B968
				reader.close();
				return c.toString();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}


	@Override
	public User gatherUser() {
		CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"gd.10086.cn",true);
		String text =  cutil.get("http://gd.10086.cn/commodity/servicio/servicioForwarding/query.jsps?servCode=MY_BASICINFO&operaType=QUERY", h);
		Document doc6 = Jsoup.parse(text);
//		System.out.println(text);
		Elements tables= doc6.select("table");
//			String sjh= tables.select("tr").get(1).select("td").get(0).text();
		String xm= tables.select("tr").get(1).select("td").get(1).text();
		String sfzh= tables.select("tr").get(1).select("td").get(2).text();
		User user = new User();
		user.setUserName(xm);
		user.setRealName(xm);
		redismap.put(name_key, xm);
		user.setIdcard(sfzh);
		user.setAddr("");
		user.setFixphone("");
		user.setPhoneRemain(getYue());
		user.setEmail("");
		user.setPackageName(getBasicPackage());
		return user;
	}


	@Override
	public LinkedList<MonthlyBillMark> gatherMonthlyBill() {
		monthlyList = getSpiderMonthsMark(0);
		String date = null;
		for (int i = 0; i < monthlyList.size(); i++) {
			for (MobileTel tel : telList) {
				date = DateUtils.formatDate(tel.getcTime(), "yyyyMM");
				if(monthlyList.get(i).getMonth().equalsIgnoreCase(date)){
					monthlyList.get(i).setObj(tel);
					break;
				}
			}
		}
		return monthlyList;
	}


	@Override
	public LinkedList<MobileMessage> gatherMessage() throws Exception {
		return messageList;
	}
	@Override
	public LinkedList<MobileOnlineList> gatherFlowList() throws Exception {
		return onlineList;
	}
	@Override
	public LinkedList<MonthlyBillMark> gatherFlowBill() {
		monthlyList = getSpiderMonthsMark(1);
		String date = null;
		for (int i = 0; i < monthlyList.size(); i++) {
			for (MobileOnlineBill tel : billList) {
				date = DateUtils.formatDate(tel.getMonthly(), "yyyyMM");
				if(monthlyList.get(i).getMonth().equalsIgnoreCase(date)){
					monthlyList.get(i).setObj(tel);
					break;
				}
			}
		}
		return monthlyList;
	}
//	@Deprecated
//	public boolean smsLogin(){
//		String url = "http://gd.10086.cn/ngcrm/hall/ucsLogin";
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("backURL", "http%3A%2F%2Fgd.10086.cn%2Fngcrm%2Fhall%2Frevision%2Fpersonal%2FserviceInquire%2FtelExpenseMonthListInquire.jsp");
//		param.put("exp", "");
//		param.put("isReRequest", "false");
//		param.put("loginType", "3");
//		param.put("optional", "false");
//		param.put("saTypes", "");
//		url = cutil.post(url, param);
//		RegexPaserUtil rp = new RegexPaserUtil("content\":\"","\"",url,RegexPaserUtil.TEXTEGEXANDNRT);
//		String url1 = rp.getText();
//		if(url1!=null){
//			url = url1;
//		}
//		String text = cutil.getURL(url,null);
//		url = "https://gd.ac.10086.cn/ucs/login/signup.jsps";
//		 param = new HashMap<String, String>();
//		param.put("area", "/ngcrm");
//		param.put("backURL", "http%3A%2F%2Fgd.10086.cn%2Fngcrm%2Fhall%2Frevision%2Fpersonal%2FserviceInquire%2FtelExpenseMonthListInquire.jsp");
//		param.put("channel", "0");
//		param.put("cid", "10002");
//		param.put("exp", "");
//		param.put("loginType", "3");
//		param.put("optional", "off");
//		param.put("reqType", "0");
//		param.put("resource", "/ngcrm/hall/ucsLogin");
//		text = cutil.postURL(url,null, param);
//		 rp = new RegexPaserUtil("content\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//		url = rp.getText();
//		if(url!=null){
//			text = cutil.getURL(url,null);
//		}
//		return true;
//	}
//	
	
//	public  boolean phoneCodeLogin1(){
//		String url = "https://gd.ac.10086.cn/ucs/login/signup.jsps";
//		CHeader  h = new CHeader("https://gd.ac.10086.cn/ucs/login/loading.jsps?reqType=0&channel=0&cid=10002&area=%2Fngcrm&resource=%2Fngcrm%2Fhall%2FucsLogin&loginType=3&optional=false&exp=&backURL=http%253A%252F%252Fgd.10086.cn%252Fngcrm%252Fhall%252Frevision%252Fpersonal%252FserviceInquire%252FtelExpenseMonthListInquire.jsp");
//		Map<String,String> param = new LinkedHashMap<String,String>();
////		h.setCookie(ConstantHC.getCookie(map));
//		param.put("backURL","http%3A%2F%2Fgd.10086.cn%2Fngcrm%2Fhall%2Frevision%2Fpersonal%2FserviceInquire%2FtelExpenseMonthListInquire.jsp");
//		param.put("reqType","0");
//		param.put("channel","0");
//		param.put("cid","10002");
//		param.put("area","/ngcrm");
//		param.put("resource","/ngcrm/hall/ucsLogin");
//		param.put("loginType","3");
//		param.put("optional","off");
//		param.put("exp","");
//		String text = cutil.post(url, h, param) ;
//		if(text!=null){
//			RegexPaserUtil rp = new RegexPaserUtil("content\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			url = rp.getText();
//			if(get_a_h_b_c( url)!=null){
//				return true;	
//			}
//		}
//		return false;
//	}
//	
//	//得到NGWEBJSESSIONID 的id
//	public String get_a_h_b_c(String url){
//		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"gd.10086.cn");
//		return cutil.get(url, h);
//		
//	}
//	
//	public  boolean orderinfo(){
//		String url = "http://gd.10086.cn/ngcrm/hall/service/TelExpenseMonthListInquire.action";
//		CHeader  h = new CHeader(CHeaderUtil.Accept_,"http://gd.10086.cn/ngcrm/hall/revision/personal/serviceInquire/telExpenseMonthListInquire.jsp",CHeaderUtil.Content_Type__urlencoded+"; charset=GBK","gd.10086.cn",true);
//		Map<String,String> param = new LinkedHashMap<String,String>();
//	//	h.setCookie(ConstantHC.getCookie(map));
//		param.put("isReRequest","false"); 
//		String text = cutil.post(url, h, param);
//		if(text!=null){
//			return true;
//		}
//		return false;
//	}
//	
}
//public  boolean login2(){
//CHeader h = new CHeader(CHeaderUtil.Accept_other,"https://gd.ac.10086.cn/ucs/login/loading.jsps?reqType=0&channel=0&cid=10003&area=/commodity&resource=/commodity/servicio/servicioForwarding/query.jsps&loginType=2&optional=true&exp=&backURL=http://gd.10086.cn/my/myService/myBasicInfo.shtml",CHeaderUtil.Content_Type__urlencoded,"gd.ac.10086.cn",true);
//Map<String,String> param = new LinkedHashMap<String,String>();
//param.put("backURL", "http://gd.10086.cn/my/myService/myBasicInfo.shtml"); 
//param.put("reqType", "0"); 
//param.put("channel", "0"); 
//param.put("cid", "10003"); 
//param.put("area", "/commodity"); 
//param.put("resource", "/commodity/servicio/servicioForwarding/query.jsps"); 
//param.put("loginType", "2"); 
//param.put("optional", "on"); 
//param.put("exp", ""); 
//String text = cutil.post("https://gd.ac.10086.cn/ucs/login/signup.jsps", h, param);
////System.out.println(text);
//if(text!=null){
//	RegexPaserUtil rp = new RegexPaserUtil("content\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//	String url = rp.getText();
//	try {
//		url = URLDecoder.decode(url,"utf-8");
//	} catch (UnsupportedEncodingException e) {
//		e.printStackTrace();
//	}
//	text = cutil.get(url);
//	CHeader c = new CHeader();
//	c.setRespCharset("utf-8");
//	text = cutil.get("http://gd.10086.cn/my/myService/myBasicInfo.shtml",c);
////	System.out.println(text);
//	
//	if(text!=null){
//		//http://gd.10086.cn/commodity/servicio/servicioForwarding/query.jsps 话费信息
//		text= jibenxinxi();
////    	text= query("ACCOUNTS_BALANCE_SEARCH");
//		if(text.contains("我的个人中心")){
//			return true;
//		}else{
////    		return login22();
//			return login23(url);
//		}
//	}
//}
//return false;
//}
//public boolean login22(){
//String url = "https://gd.ac.10086.cn/ucs/login/signup.jsps";
//Map<String,String> map = new HashMap<String, String>();
//map.put("area", "/commodity");
//map.put("backURL", "http://gd.10086.cn/my/REALTIME_LIST_SEARCH.shtml");
//map.put("channel", "0");
//map.put("cid", "10003");
//map.put("cid", "");
//map.put("loginType", "3");
//map.put("optional", "off");
//map.put("reqType", "0");
//map.put("resource", "/commodity/servicio/servicioForwarding/query.jsps");
//String text = cutil.post(url, map);
//RegexPaserUtil rp = new RegexPaserUtil("content\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//url = rp.getText();
//if(url!=null){
////	try {
////		url = URLDecoder.decode(url,"utf-8");
////	} catch (UnsupportedEncodingException e) {
////	}
//	text = cutil.getURL(url,null);
//	if(text!=null){
//		text= jibenxinxi();
////		text= query("ACCOUNTS_BALANCE_SEARCH");
//		return text.contains("我的个人中心");
//	}
//}
//return false;
//}
