package com.lkb.thirdUtil.dx;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.primitives.Doubles;
import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

public class NeiMengGuDianXin extends BaseInfoMobile {
	protected static Logger logger = Logger.getLogger(NeiMengGuDianXin.class);
	public String index = "https://uam.ct10000.com/ct10000uam/login?service=http://www.189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=219.143.103.242,%20220.181.46.166,%20127.0.0.1";
	// 验证码图片路径
	public static String imgurl = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?";
	public NeiMengGuDianXin(Login login,String currentUser) {
		super(login,ConstantNum.comm_nmg_dianxin,currentUser);
	}
	public void init(){
		try{
			if(!isInit()){
//				cutil.get("http://www.189.cn/nm/");
				String text = cutil.get(index);
				if(text!=null){
					Map<String,Object> jsmap = new HashMap<String, Object>();
					jsmap.put("indexText", text);
					String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
					Map<String,String> param = new LinkedHashMap<String,String>();
					param.put("username", login.getLoginName()); 
				    text = cutil.post(url,param);
					String s[] = text.split("\\|");
					jsmap.put("shengfen_id", s[0]) ;
					jsmap.put("shengfen_name",s[1]) ;
				    redismap.put("jsmap", jsmap);
					setImgUrl(imgurl);
					setInit();
				}
				
			}
		}catch(Exception e){
			logger.error("error",e);
		}
		
	}
	// 首页登录
	public Map<String,Object> login() {
		login1();
		String result = ""+status;
		if(result!=null&& result.equals("1")){
			loginsuccess();
			addTask_1(this);
			sendPhoneDynamicsCode();
		}
		return map;
	}

	/**第一次post*/
	public void login1(){
		Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String text = jsmap.get("indexText").toString();
		if(text.length()<300){
			//此处是特殊情况处理,主要是redis有记录,或者cookie有记录,会有出现此处连接的地方,需要注意
		    //System.out.println(text);
		    if(text.contains("location.replace(")){
		    	status = 0;
			}
		}else{
			Document doc = Jsoup.parse(text);
			String empoent = doc.select("input[id=forbidpass]").first().val();
			String forbidaccounts = doc.select("input[id=forbidaccounts]").first().val();
			String authtype = doc.select("input[name=authtype]").first().val();
			String customFileld02 = jsmap.get("shengfen_id").toString();
			String areaname = jsmap.get("shengfen_name").toString();
			String customFileld01 = "3";
			String lt = doc.select("input[name=lt]").first().val();
			String _eventId = doc.select("input[name=_eventId]").first().val();
			String open_no = "c2000004";
			String action = doc.select("form[id=c2000004]").first().attr("action");
			action = URLDecoder.decode(action);
			String url = "https://uam.ct10000.com"+action;
			Map<String,String> param = new LinkedHashMap<String,String>();
			param.put("_eventId", _eventId); 
			param.put("areaname",areaname); 
			param.put("authtype",authtype); 
			param.put("c2000004RmbMe", "on"); 
			param.put("customFileld01", customFileld01); 
			param.put("customFileld02", customFileld02); 
			param.put("forbidaccounts", "null"); 
			param.put("forbidpass", null); 
			param.put("lt", lt); 
			param.put("open_no",open_no); 
			param.put("password", login.getPassword()); 
			param.put("randomId", login.getAuthcode()); 
			param.put("username", this.loginName); 
			//https://uam.ct10000.com/ct10000uam/login?service=http://www.189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=219.143.103.242,%20220.181.46.179,%20127.0.0.1
			text = cutil.postURL("https://uam.ct10000.com/ct10000uam/login?service=http://www.189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=219.143.103.242,%20220.181.46.179,%20127.0.0.1", null, param);
			if(text!=null){
				if(text.length()<300){
					if(text.contains("https://uam.ct10000.com")){
						login2(text, param);
					}
				}else{
					doc = Jsoup.parse(text);
					Element el = doc.getElementById("status2");
					if(el!=null){
						errorMsg = el.text();
					}
				}
				
			}
		 }
	}
	
	
	public void login2(String url,Map<String,String> param){
		String  text = cutil.post(url, param);
		if(text!=null && text.contains("location.replace")){
			login3(text);
		}else{
			errorMsg="网络异常，请重新登录!";
		}
	}
	
	/**第三次post*/
	public  void login3(String text){
		RegexPaserUtil rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String url = rp.getText();
		text = cutil.getURL(url,null);
		if(text!=null){
			CHeader h = new CHeader("http://www.189.cn/nm/");
			text = cutil.get("http://nm.189.cn/selfservice/bill/hf",h);
			rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
			text = rp.getText();
			text = cutil.getURL(text,h);
			//获取手机号所在地区编号
			text = cutil.get("http://www.189.cn/dqmh/cms/index/login.jsp?rand="+System.currentTimeMillis(), new CHeader("http://www.189.cn/nm/"));
			log.error(text.toString());
			String areaCode = StringUtil.subStr("$(\"#activity_latnId\",window.parent.document).val('","')",text);
			redismap.put("areaCode", areaCode);
			try {
				text="{\"number\":\""+this.loginName+"\",\"intLoginType\":\"4\",\"areaCode\":\""+areaCode+"\",\"isBusinessCustType\":\"N\",\"identifyType\":\"B\",\"userLoginType\":\"4\",\"password\":\"\",\"randomPass\":\"\",\"noCheck\":\"N\",\"isSSOLogin\":\"Y\",\"sRand\":\"SSOLogin\"}";
				text = cutil.post("http://nm.189.cn/selfservice/service/userLogin",new CHeader("http://nm.189.cn/selfservice/bill/hf"),text);
				text="{\"qryAccNbrType\":\"\"}";
				text = cutil.post("http://nm.189.cn/selfservice/cust/queryAllProductInfo",new CHeader("http://nm.189.cn/selfservice/bill/hf"),text);
				text = "{\"accNbr\":\""+this.loginName+"\",\"accNbrType\":\"4\",\"areaCode\":\""+areaCode+"\",\"prodSpecId\":\"378\",\"smsCode\":\"\",\"prodSpecName\":\"??\"}";
				BigDecimal balance = new BigDecimal(0);
				text = cutil.post("http://nm.189.cn/selfservice/bill/hfQuery",new CHeader("http://nm.189.cn/selfservice/bill/hf"),text);
				if(text.contains("POR-0000")){
					JSONObject json =  new JSONObject(text);
					JSONObject json1 = json.getJSONObject("balance");
					String json2 = json1.get("balance").toString();
					balance = new BigDecimal(json2.toString());
					redismap.put("balance", balance);
					loginsuccess();
				}
				
			} catch (Exception e) {
				logger.error("error",e);
			}
		}
	}
	
	public Map<String, Object> sendPhoneDynamicsCode() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String json = "{}";
		String url = "http://nm.189.cn/selfservice/bill/xdQuerySMSCheckIf";
		CHeader h = new CHeader("http://nm.189.cn/selfservice/bill/xd");
		String text = "";
		try {
			text = cutil.post(url, h, json);
			JSONObject jsonObject = new JSONObject(text);
			if (jsonObject != null && jsonObject.has("flag") && jsonObject.getInt("flag") == 1) {
				if (jsonObject.getInt("flag") == 1) {
					status = 1;
					errorMsg = "您已经短信验证成功！";
				} 
			} else {
				json = "{\"phone\":\"" + this.loginName + "\"}";
				text = cutil.post("http://nm.189.cn/selfservice/bill/xdQuerySMS", h, json);
				JSONObject jobj = new JSONObject(text);
				//if flag=2 is 您好，验证码在1小时内只能发送5次
				if (jobj != null && jobj.has("flag") && jobj.getInt("flag") == 0) {
					if (jobj.getInt("flag") == 0) {
						status = 1;
						errorMsg= "短信发送成功";
					} else if (jobj.getInt("flag") == 2) {
						errorMsg="验证码在1小时内只能发送5次";
					}
				} else {
					errorMsg="短信发送失败,请重试！";
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		map.put("status", status);
		map.put("errorMsg", errorMsg);
		return map;
	}
	public Map<String, Object> checkPhoneDynamicsCode() {
		errorMsg = "短信验证码错误，请重新获取取短信验证码！";
		try {
			String json = "{\"code\":\"" + login.getPhoneCode() + "\"}";
			CHeader header = new CHeader("http://nm.189.cn/selfservice/bill/xd");
			String text = cutil.post("http://nm.189.cn/selfservice/bill/xdQuerySMSCheck", header , json);
			JSONObject jobj = new JSONObject(text);
			if (jobj != null && jobj.has("flag") && jobj.getInt("flag") == 1) {
				status = 1;
				errorMsg = "短信验证成功！";
				addTask_2(this);
			} 
		} catch (Exception e) {
			log.error(e);
		}
		map.put("status", status);
		map.put("errorMsg", errorMsg);
		return map;
	}
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
		try{
			parseBegin(Constant.DIANXIN);
			switch (login.getType()) {
			case 1:
				saveYuE(); //个人信息
				break;
			case 2:
				allRequest();
				break;
			case 3:
				saveYuE(); //个人信息
				allRequest();
				break;
			default:
				break;
			}
		}finally{
			parseEnd(Constant.DIANXIN);
		}
	}
	
	public void allRequest() {
		Date lastMsgTime = null;
		Date lastCallTime = null;
		try{
			DianXinDetail detail = new DianXinDetail(login.getLoginName());
			if(dianXinDetailService.getMaxTime(detail)!=null) {
				lastCallTime = dianXinDetailService.getMaxTime(detail).getcTime();	
			}
			
			if(telcomMessageService.getMaxSentTime(login.getLoginName())!=null)
			lastMsgTime = telcomMessageService.getMaxSentTime(login.getLoginName()).getSentTime();
			
		}
		catch(Exception e)
		{
			logger.error("error",e);
		}
		
		Date lastTelTime = null;
		DianXinTel dianXinTel = new DianXinTel(login.getLoginName());
		if(dianXinTelService.getMaxTime(dianXinTel)!=null) {
			lastTelTime = dianXinTelService.getMaxTime(dianXinTel).getcTime();
		}
		Date d1 = new Date();
		for(int i = 6; i >=0; i--) {
			final Date cd = DateUtils.add(d1, Calendar.MONTH, -1 * i);
			String dstr = DateUtils.formatDate(cd, "yyyyMM");//201404  05  06  ..  10   一共7个月
			/**
			 * 注意账单    假设本月为10月
			 * 请求9月账单一次就返回4-9 6个月的数据
			 * 10月份账单需要单独请求
			 * 所以只调用一次
			 */
			if(i==0) {
				requestMonthBillService(dstr,lastTelTime);
			}
			if(i==1) {
				requestOtherMonthBillService(dstr,lastTelTime);
			}
			requestLogMsgService(1, 1, cd, dstr,lastCallTime,lastMsgTime);
			requestFlow(1, 1, cd, dstr,i);
		}
	}

	private User saveYuE() {
		User user_return = new User();		
		try {
			String loginName = login.getLoginName();
			Map<String, String> parmap = new HashMap<String, String>(3);
			parmap.put("parentId", currentUser);
			parmap.put("loginName", loginName);
			parmap.put("usersource", Constant.DIANXIN);
			List<User> list = userService.getUserByParentIdSource(parmap);
			String userInfo = cutil.get("http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo&rand="+System.currentTimeMillis(),new CHeader("http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo&rand="+System.currentTimeMillis()));
			String idCard="";
			String email="";
			String addr="";
			//String registerDate="";
			String realName="";
			if(userInfo.contains("身份证号码")&&userInfo.contains("真实姓名"))
			{
				Document doc = Jsoup.parse(userInfo);
				Element table = doc.select("form#personalInfo").get(0);
				if(table!=null){
					idCard = table.select("input[name=certificateNumber]").val();
					email = table.select("input[name=email]").val();
					addr = table.select("textarea[name=address]").val();
					//registerDate = table.select("input[name=email]").get(1).text();
					realName = table.select("input[name=realName]").val();
				}
			}
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(realName);
				user.setIdcard(idCard);
				user.setAddr(addr);
				user.setUsersource(Constant.DIANXIN);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				BigDecimal balance = new BigDecimal(0);
				try {
					balance= new BigDecimal(redismap.get("balance").toString());
				} catch (Exception e) {
					log.error(e);
				}
				user.setPhoneRemain(balance);
				user.setEmail(email);
				userService.update(user);
				user_return = user;
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(realName);
				user.setIdcard(idCard);
				user.setAddr(addr);
				user.setUsersource(Constant.DIANXIN);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				BigDecimal balance = new BigDecimal(0);
				try {
					balance= new BigDecimal(redismap.get("balance").toString());
				} catch (Exception e) {
					log.error(e);
				}
				user.setPhoneRemain(balance);
				user.setEmail(email);
				userService.saveUser(user);
				user_return = user;
			}
			return user_return;
		} catch (Exception e) {
			logger.error("error",e);
			String warnType = WaringConstaint.NMGDX_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return null;
	}
	/**
	 * @param dstr   
	 * @param mon  用于表示当前月份
	 * 当前月份的账单请求和历史账单的url不同
	 */
	private void requestMonthBillService( final String dstr,Date lastTelTime) {
		
		try {
			    String text = "";
				String json = "{\"accNbr\":\"" + this.loginName + "\",\"accNbrType\":\"4\",\"areaCode\":\""+redismap.get("areaCode")+"\",\"prodSpecId\":\"378\",\"smsCode\":\"\",\"prodSpecName\":\"??\"}";
				CHeader header = new CHeader("http://nm.189.cn/selfservice/bill/khzd");
				text = cutil.post("http://nm.189.cn/selfservice/bill/dyzdQuery", header, json);
				
				RegexPaserUtil rp = new RegexPaserUtil( "billingCycleDesc\":\"", "\"" ,text, RegexPaserUtil.TEXTEGEXANDNRT);
				String dependCycle = rp.getText().replace("-","至").replace("/", "-");
				rp = new RegexPaserUtil( "charge", "套餐费" ,text, RegexPaserUtil.TEXTEGEXANDNRT);
				String ztcjbfStr = rp.getText();
				rp = new RegexPaserUtil( "\":\"", "\",\"billingCycleDesc" ,ztcjbfStr, RegexPaserUtil.TEXTEGEXANDNRT);
				ztcjbfStr = rp.getText();
				BigDecimal ztcjbf = new BigDecimal(ztcjbfStr);
				rp = new RegexPaserUtil( "total\":\"", "\"" ,text, RegexPaserUtil.TEXTEGEXANDNRT);
				String cAllPayStr = rp.getText();
				BigDecimal cAllPay = new BigDecimal(cAllPayStr);
				
				DianXinTel tel = new DianXinTel();
				UUID uuid = UUID.randomUUID();
				tel.setId(uuid.toString());
				tel.setTeleno(this.loginName);
				tel.setcTime(DateUtils.StringToDate(dstr, "yyyyMM"));
				if(lastTelTime!=null){
					if(lastTelTime.getTime()==tel.getcTime().getTime()){
						tel.setcAllPay(cAllPay);
						tel.setDependCycle(dependCycle);
						tel.setZtcjbf(ztcjbf);
						dianXinTelService.update(tel);
					}else if(lastTelTime.getTime()<=tel.getcTime().getTime()) {
						tel.setcAllPay(cAllPay);
						tel.setDependCycle(dependCycle);
						tel.setZtcjbf(ztcjbf);
						dianXinTelService.saveDianXinTel(tel);
					}	
				}else {
				tel.setcAllPay(cAllPay);
				tel.setDependCycle(dependCycle);
				tel.setZtcjbf(ztcjbf);
				dianXinTelService.saveDianXinTel(tel);
				}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error(e1);
		}
		
		
		
						
	}
	private void requestOtherMonthBillService(String dstr,Date lastTelTime) {
		try {
			String json = "{\"accNbr\":\"" + this.loginName + "\",\"accNbrType\":\"4\",\"areaCode\":\""+redismap.get("areaCode")+"\",\"prodSpecId\":\"378\",\"smsCode\":\"\",\"prodSpecName\":\"??\",\"billingCycle\":\"" + dstr + "\"}";
			CHeader header = new CHeader("http://nm.189.cn/selfservice/bill/khzd");
			String text = cutil.post("http://nm.189.cn/selfservice/bill/khzdQuery", header, json);
			if(text.contains("resultSet")) {
			JSONObject jobj = new JSONObject(text);
			JSONObject jobj1 = jobj.getJSONObject("resultSet");//aaaaaa
			String html = jobj1.getString("userHtml");
			html = jobj1.getString("sixMonthHtml");
			Document doc = Jsoup.parse(html);
			Elements es = doc.select("set");
			for(int i = 0; i < es.size(); i++) {
				
				Element e = es.get(i);
				String date = e.attr("name");
				String year = date.substring(0, 4);
				String mouth = date.substring(4, 6);
				DianXinTel tel = new DianXinTel();
				UUID uuid = UUID.randomUUID();
				tel.setId(uuid.toString());
				tel.setcTime(DateUtils.StringToDate(date, "yyyyMM"));
				if(lastTelTime!=null){
					if(lastTelTime.getTime()>=tel.getcTime().getTime()){
						continue;
					}	
				}
				tel.setTeleno(this.loginName);
				tel.setcAllPay(new BigDecimal(e.attr("value")));
				tel.setDependCycle(TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(formatDateMouth(mouth)))
						+ "至"
						+ TimeUtil.getLastDayOfMonth(Integer
								.parseInt(year), Integer
								.parseInt(formatDateMouth(mouth))));
				dianXinTelService.saveDianXinTel(tel);
			}
			}//if(text)
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void requestLogMsgService(final int page, final int t, final Date d, final String dstr,Date lastCallTime,Date lastMsgTime){
		CHeader header = new CHeader("http://nm.189.cn/selfservice/bill/xd");
		try {
			String json_detail = "{\"accNbr\":\"" + this.loginName + "\",\"pageRecords\":10000,\"pageNo\":1,\"qtype\":\"0\",\"areaCode\":\""+redismap.get("areaCode")+"\",\"accNbrType\":\"4\",\"prodSpecId\":\"378\",\"smsCode\":\"\",\"billingCycle\":\"" + dstr + "\"}";
			String detailText = cutil.post("http://nm.189.cn/selfservice/bill/xdQuery", header, json_detail);
			saveCallLog(detailText, page, d, dstr,lastCallTime);
		} catch (UnsupportedEncodingException e) {
			log.error("NeiMengGu.requestLogService,erroe!");
		}
		try {
			String json_msg =    "{\"accNbr\":\"" + this.loginName + "\",\"pageRecords\":10000,\"pageNo\":1,\"qtype\":\"1\",\"areaCode\":\""+redismap.get("areaCode")+"\",\"accNbrType\":\"4\",\"prodSpecId\":\"378\",\"smsCode\":\"\",\"billingCycle\":\"" + dstr + "\"}";
			String msglText = cutil.post("http://nm.189.cn/selfservice/bill/xdQuery", header, json_msg);
			saveSmsLog(msglText, page, d, dstr,lastMsgTime);
		} catch (UnsupportedEncodingException e) {
			log.error("NeiMengGu.requestMsgService,erroe!");
		}
		
	}
	
	//流量详单
	private void requestFlow(final int page, final int t, final Date d, final String dstr,int iscm1){
		CHeader header = new CHeader("http://nm.189.cn/selfservice/bill/xd");
		try {

			Date lasListtTime = null;
			Date lastBillTime = null;
			try{
				DianXinFlowDetail detail = new DianXinFlowDetail(login.getLoginName());
				if(dianXinFlowDetailService.getMaxTime(detail)!=null) {
					lasListtTime = dianXinFlowDetailService.getMaxTime(detail).getBeginTime();	
				}
			}
			catch(Exception e)
			{
				logger.error("error",e);
			}	
			 try {
				 if(dianXinFlowService.getMaxFlowTime(login.getLoginName())!= null)
				 lastBillTime = dianXinFlowService.getMaxFlowTime(login.getLoginName()).getQueryMonth();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.error("error",e1);
			}
			String json_detail = "{\"accNbr\":\"" + this.loginName + "\",\"pageRecords\":10000,\"pageNo\":1,\"qtype\":\"2\",\"areaCode\":\""+redismap.get("areaCode")+"\",\"accNbrType\":\"4\",\"prodSpecId\":\"378\",\"smsCode\":\"\",\"billingCycle\":\"" + dstr + "\"}";
			String detailText = cutil.post("http://nm.189.cn/selfservice/bill/xdQuery", header, json_detail);
			
			flow_parse(detailText,lasListtTime,lastBillTime, page, d, dstr,iscm1);
		} catch (UnsupportedEncodingException e) {
			log.error("NeiMengGu.requestLogService,erroe!");
		}
	
	}
	/**流量解析
	 * 无法获取账单
	 * 网站上就是加起来的
	 */
	private void flow_parse(String detailText,Date lastListTime, Date lastBillTime,final int page, final Date d, final String dstr,int iscmon) {
		if (detailText == null ||detailText.contains("无查询结果")) {
			return;
		}
		
		String dependCycle = null;
		Date queryMonth = null;
		BigDecimal	allFlow = new BigDecimal(0);
		BigDecimal	allPay = new BigDecimal(0);
		BigDecimal allTime = new BigDecimal(0);
		
		try {
			JSONObject jobj = new JSONObject(detailText);
			if (jobj.toString().contains("items")) {
			JSONArray arr = jobj.getJSONArray("items");
			int len = arr.length();
			for(int i=0; i < len; i++) {
					JSONObject jobj1 = arr.getJSONObject(i);
					BigDecimal flow = new BigDecimal(0);
					String netType = null;
					String location = null;
					BigDecimal fee = new BigDecimal(0);
					Date beginTime = null;
					long tradeTime = 0;
					int iscm = 0;
					try {
						String beginTime1 = jobj1.getString("startTime");
						flow = new BigDecimal(StringUtil.flowFormat(jobj1.getString("mysurfing")));
						netType = "undefined";
						location = jobj1.getString("billingVisitAreaCode");
						fee = new BigDecimal(jobj1.getString("fee"));
						tradeTime =Math.round(StringUtil.flowTimeFormat(jobj1.getString("duration_new")));
						if(iscmon==6) {
							iscm = 1;
						}
						/*
						 * 开始计算账单信息
						 * 需要加法计算
						 */
						allFlow = allFlow.add(flow);
						allPay = allPay.add(fee);
						allTime = allTime.add(new BigDecimal(tradeTime));
						queryMonth = DateUtils.StringToDate(beginTime1.substring(0, 7), "yyyy-MM");
						dependCycle =  jobj.get("begin_Time").toString()+"至"+jobj.get("end_Time").toString();
						//计算结束
						
						try{
							beginTime= DateUtils.StringToDate(beginTime1, "yyyy-MM-dd HH:mm:ss");
							if(lastListTime!=null&&beginTime!=null){
								if(beginTime.getTime()<=lastListTime.getTime()){
									continue;
								}	
							}	
						}catch (Exception e1) {
							log.error("detail" + jobj1, e1);
						}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							logger.error("error",e);
						}
					 
						DianXinFlowDetail obj = new DianXinFlowDetail();
						obj.setPhone(login.getLoginName());
						UUID uuid = UUID.randomUUID();
						obj.setId(uuid.toString());
						obj.setFlow(flow);
						obj.setNetType(netType);
						obj.setLocation(location);
						obj.setTradeTime(tradeTime);
						obj.setIscm(iscm);
						obj.setFee(fee);
						obj.setBeginTime(beginTime);
						dianXinFlowDetailService.saveDianXinFlowDetail(obj);
			
				}//for
			
			/*
			 * 开始保存流量账单
			 */
			try {
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
						obj.setAllTime(allTime);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("error",e);
					}
					dianXinFlowService.saveDianXinFlow(obj);
				}else {
					
					if(queryMonth.getTime()==lastBillTime.getTime()){
						DianXinFlow obj = new DianXinFlow();
						obj.setPhone(login.getLoginName());
						UUID uuid = UUID.randomUUID();
						obj.setId(uuid.toString());
						try {
							obj.setDependCycle(dependCycle);
							obj.setAllFlow(allFlow);
							obj.setAllPay(allPay);
							obj.setQueryMonth(queryMonth);
							obj.setAllTime(allTime);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("error",e);
						}
						dianXinFlowService.update(obj);
					}else if(queryMonth.getTime()>lastBillTime.getTime()){
						DianXinFlow obj = new DianXinFlow();
						obj.setPhone(login.getLoginName());
						UUID uuid = UUID.randomUUID();
						obj.setId(uuid.toString());
						try {
							obj.setDependCycle(dependCycle);
							obj.setAllFlow(allFlow);
							obj.setAllPay(allPay);
							obj.setQueryMonth(queryMonth);
							obj.setAllTime(allTime);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("error",e);
						}
						dianXinFlowService.saveDianXinFlow(obj);
					} 
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
			/**
			 * 流量账单结束
			 */
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("error",e);
		}
	}
	
	
	private void saveCallLog(String detailText, final int page, final Date d, final String dstr,Date lastCallTime) {
		if (detailText == null) {
			return;
		}
		try {
			JSONObject jobj = new JSONObject(detailText);
			if (jobj.toString().contains("items")) {
			JSONArray arr = jobj.getJSONArray("items");
			int len = arr.length();
			for(int i=0; i < len; i++) {
				JSONObject jobj1 = arr.getJSONObject(i);
				try {
					DianXinDetail dxDetail = new DianXinDetail();
					UUID uuid = UUID.randomUUID();
					dxDetail.setId(uuid.toString());
					dxDetail.setcTime(DateUtils.StringToDate(jobj1.getString("converseDate") + " " + jobj1.getString("converseTime"), "yyyy-MM-dd HH:mm:ss"));
					if(lastCallTime!=null) {
						if(lastCallTime.getTime()<dxDetail.getcTime().getTime()) {
							dxDetail.setPhone(this.loginName);
							dxDetail.setCallWay(jobj1.getString("callType"));
							dxDetail.setRecevierPhone(jobj1.getString("callingNbr"));
							dxDetail.setTradeTime(new TimeUtil().timetoint(jobj1.getString("converseDuration")));
							dxDetail.setTradeAddr(jobj1.getString("converseAddr"));
							dxDetail.setAllPay(new BigDecimal(jobj1.getString("fee")));
							dxDetail.setTradeType(jobj1.getString("converseType"));
							dianXinDetailService.saveDianXinDetail(dxDetail);
						}
					}else {
					dxDetail.setPhone(this.loginName);
					dxDetail.setCallWay(jobj1.getString("callType"));
					dxDetail.setRecevierPhone(jobj1.getString("callingNbr"));
					dxDetail.setTradeTime(new TimeUtil().timetoint(jobj1.getString("converseDuration")));
					dxDetail.setTradeAddr(jobj1.getString("converseAddr"));
					dxDetail.setAllPay(new BigDecimal(jobj1.getString("fee")));
					dxDetail.setTradeType(jobj1.getString("converseType"));
					dianXinDetailService.saveDianXinDetail(dxDetail);
					}
				} catch (Exception e1) {
					log.error("detail" + jobj1, e1);
				}
			}
			}
		} catch (Exception e) {
			log.error("detail" + detailText, e);
		}
	}
	private void saveSmsLog(String msgtext, int page, Date d, String dstr,Date lastMsgTime) {
		if (msgtext == null) {
			return;
		}
		try {
			JSONObject jobj = new JSONObject(msgtext);
			if (jobj.toString().contains("items")) {
			JSONArray arr = jobj.getJSONArray("items");
			int len = arr.length();
			for(int i=0; i < len; i++) {
				JSONObject jobj1 = arr.getJSONObject(i);
					try {
						
						
						Date sentTime=null;
						try{
							sentTime= DateUtils.StringToDate(jobj1.getString("sendDate") + " " + jobj1.getString("sendTime"), "yyyy-MM-dd HH:mm:ss");
							if(lastMsgTime!=null&&sentTime!=null){
								if(sentTime.getTime()<=lastMsgTime.getTime()){
									continue;
								}	
							}	
						}
						catch(Exception e)
						{
							logger.error("error",e);
						}
						TelcomMessage obj = new TelcomMessage();
						UUID uuid = UUID.randomUUID();
						obj.setId(uuid.toString());
						obj.setPhone(this.loginName);
						obj.setRecevierPhone(jobj1.getString("callingNbr"));
						obj.setSentTime(DateUtils.StringToDate(jobj1.getString("sendDate") + " " + jobj1.getString("sendTime"), "yyyy-MM-dd HH:mm:ss"));
						obj.setAllPay(Doubles.tryParse(jobj1.getString("sendFee")));
						String bussinessType = jobj1.getString("rcvSendType") + jobj1.getString("bussType");
						if(bussinessType.contains("国内发")){
							obj.setBusinessType("发送");
						}else{
							obj.setBusinessType("接收");
						}
						
						if (obj != null) {
							telcomMessageService.save(obj);
						}
				} catch (Exception e1) {
					log.error("detail" + jobj1);
				}
			}	
			}
		} catch (Exception e) {
			log.error("detail" + msgtext);
		}

	}
	
	private String formatDateMouth(String m) {
		if (m != null && m.length() == 2) {
			String fix1 = m.substring(0, 1);
			String fix2 = m.substring(1, 2);
			if (fix1.equals("0")) {
				return fix2;
			}
			return m;
		}
		return null;
	}
	public static void main(String[] args) {
		NeiMengGuDianXin n = new NeiMengGuDianXin(new Login("13314863621","145306"),null);
		n.index();
		n.inputCode(imgurl);
		n.login();
	}
	
}