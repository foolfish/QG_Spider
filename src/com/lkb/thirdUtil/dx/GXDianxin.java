package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.entity.CHeader;

/**广西电信
 * @author fastw
 * @date   2014-8-6 下午3:01:55
 */
public class GXDianxin extends BaseInfoMobile{
	private static final Logger logger = Logger.getLogger(GXDianxin.class);
	public String index = "http://gx.189.cn/service/account/";
	public static String imgurl = "http://gx.189.cn/public/image.jsp";
	public String yueKey = "yue.gxdianxin";
	public String nameKey  = "name.gxdianxin";
	/**本地测试
	 * @param login
	 * @param currentUser
	 */
	public GXDianxin(Login login) {
		super(login);
		userSource = Constant.DIANXIN;
	}
	
	public GXDianxin(Login login,String currentUser) {
		super( login, ConstantNum.comm_gx_dianxi,currentUser);
		userSource = Constant.DIANXIN;
	}

	public void init(){
		if(!isInit()){
			String text = cutil.get(index);
			if(text!=null){
				try{
				Map<String,String> map = new HashMap<String, String>();
				RegexPaserUtil rp = new RegexPaserUtil("UForm.action='","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String action = rp.getText();
				 map.put("action", action);
				 rp = new RegexPaserUtil("<tr id=\"TR_RAND",">",text,RegexPaserUtil.TEXTEGEXANDNRT);
				 text = rp.getText();
				 if(text!=null&&!text.contains("display:none")){
					 setImgUrl(imgurl);
				 }
				 redismap.put("jsmap", map);//根据实际需要存放
				 //调用dwr时才使用一下参数,默认不调用,备用
				//if(action!=null)index_1(map,text);
				 setInit();
				}catch(Exception e){
					logger.error("error",e);
				}
				
			}
		}
	}
	/**调用dwr时才使用一下参数,默认不调用,备用
	 * @param map
	 * @param text
	 */
	private void index_1(Map<String,Object> map,String text){
		RegexPaserUtil rp = new RegexPaserUtil("custIp='","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String c0_e1 = rp.getText();
		map.put("c0-e1", "string:"+c0_e1);
		CHeader c = new CHeader(index);
		text = cutil.get("http://gx.189.cn/public/common/control/dwr/engine.js",c);
		 //System.out.println(text);
		 if(text!=null){
			 rp = new RegexPaserUtil("engine._origScriptSessionId = \"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			 text = rp.getText();
			 if(text!=null){
				 map.put("scriptSessionId", text+(Math.random()*900)+100);
			 }
		 }
	}
	public Map<String,Object> login() {
		try{
			//		String text = login_0();
			String	text = login_1();
			System.out.println(text);
			if(text!=null){
				//提交参数后的一系列操作,默认返回是msg的结果
				if(login_2(text)==null){
					//测试是否登陆成功 
					login_3();
				}else{
					if(errorMsg.equals("验证码错误")){
						//设置网站url地址
						setImgUrl(imgurl);
					}
				}
			}
			if(islogin()){
	        	addTask_1(this);
	        	sendPhoneDynamicsCode();
	    	}
		}catch(Exception e){
			logger.error("error",e);
		}
		return map;
	}
	/**暂且没用
	 * @return
	 */
	private String login_0(){
		String url = "http://gx.189.cn/dwr/call/plaincall/Service.excute.dwr";
		@SuppressWarnings("unchecked")
		Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		Map<String,String> param = new HashMap<String,String>();
		
		param.put("callCount", "1");
		param.put("page","/service/account/");
		param.put("httpSessionId",getJsessionId("189.cn"));
		param.put("scriptSessionId",(String)jsmap.get("scriptSessionId"));
		param.put("c0-scriptName", "Service");
		param.put("c0-methodName", "excute");
		param.put("c0-id", "0");
		param.put("c0-param0", "string:WB_IP_ACTION_QRY");
		param.put("c0-param1", "boolean:true");
		param.put("c0-e1", (String)jsmap.get("c0-e1"));
		param.put("c0-e2", "string:24");
		param.put("c0-e3", "string:2");
		param.put("c0-param2", "Object_Object:{CUST_IP:reference:c0-e1, TIME_RANGE:reference:c0-e2, LOG_TYPE:reference:c0-e3}");
		param.put("batchId", "0");
		CHeader c = new CHeader(CHeaderUtil.Accept_other,index,"text/plain",null,true);//设置referer;//设置referer
		return cutil.post(url,c,param);
	}
	private String login_1(){
		String url = "http://gx.189.cn/public/login.jsp";
		Map<String,String> param = new HashMap<String,String>();
		param.put("recomInfo", "");param.put("logon_action", "");
		param.put("OLDURL", "");param.put("LOGIN_NOTE_0", "");
		param.put("LOGIN_NOTE_2", "");param.put("logon_name_mini", "");
		param.put("logon_name_tel", "");param.put("AREA_CODE", "");
		param.put("passwdUserNum", "");param.put("passwdCustNum", "");
		param.put("passwdCustCodeNum", "");param.put("passwdRegisterNum", "");
		param.put("passwdCdmaNum", "");param.put("passwdMiniNum", "");
		param.put("passwdMiniUserNum", "");param.put("passwdTelNum", "");
		param.put("passwdTelUserNum", "");
		
		param.put("logon_name", login.getLoginName());
		param.put("logon_passwd", login.getPassword());
		param.put("cookieContent","/service/account/index.jsp");
		param.put("LOGIN_TYPE", "21");
		param.put("LOGIN_NOTE_A", "注册帐号");
		param.put("LOGIN_NOTE_20", "产品号码");
		param.put("LOGIN_NOTE_3", "宽带帐号");
		param.put("LOGIN_NOTE_22", "手机号码");
		param.put("LOGIN_NOTE_26", "小灵通");
		param.put("LOGIN_NOTE_2A", "固定电话");
		param.put("RAND_TYPE", "001");
		param.put("userFlag", "001");
		param.put("LOGIN_TYPE_DATA", "2122");
		param.put("password_type", "21");
		param.put("PASSWD_TYPE", "21");
		param.put("MINI_PASSWD_TYPE", "26");
		param.put("TEL_PASSWD_TYPE", "2A");
		param.put("passwdCdmaUserNum", login.getPassword());
		if(login.getAuthcode()!=null){
			param.put("logon_valid", login.getAuthcode());	
		}else{
			param.put("logon_valid", "点击获取");	
		}
		
		CHeader c = new CHeader(index);//设置referer
		return cutil.post(url,c,param);
	}
	private String login_2(String text){
		RegexPaserUtil rp = new RegexPaserUtil("<auth_ticket>","</auth_ticket>",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String auth_ticket = rp.getText();
		if(auth_ticket!=null&&auth_ticket.length()>5){
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			Map<String,String> param = new HashMap<String,String>();
			param.put("authTicket",auth_ticket);
			param.put("systemId","21101");
			param.put("redirectURL","http://gx.189.cn/main/public/quick4.jsp");
			String url = (String) jsmap.get("action");
			cutil.setHandleRedirect(false);
			if (url==null||url.equals("")) {
				errorMsg = "登录失败，请重试!"; 
				return errorMsg;
			}
			text = cutil.postURL(url,null,param);
		}else{
			rp = new RegexPaserUtil("<msg>","</msg>",text,RegexPaserUtil.TEXTEGEXANDNRT);
			errorMsg = rp.getText();
		}
		return errorMsg;
	}
	private void login_3(){
		String text = cutil.get("http://gx.189.cn/service/account/yeqry.jsp?ASK_TYPE=100");
		System.out.println(text);
		RegexPaserUtil rp = new RegexPaserUtil("余额为","元",text,RegexPaserUtil.TEXTEGEXANDNRT);
		text = rp.getText();
//		System.out.println(text);
		if(text!=null){
			text = text.replaceAll("<.*?>", "");
			if(text.length()<15){
				redismap.put(yueKey,text);
				loginsuccess();
			}
			
			
		}
	}
	public BigDecimal yue(){
		try{
			return new BigDecimal(redismap.get(yueKey).toString());
		}catch(Exception e){
			logger.error("error",e);
		}
		return new BigDecimal(0.00);
	}
	/**独立前台访问方法
	 * @return
	 */
	public Map<String,Object> sendPhoneDynamicsCode(){
		 Map<String,Object> map = new LinkedHashMap<String,Object>();
		int status = 0;
		String errorMsg = "" ;
		
		String key = "PhoneDynamicsCode";
		Map<String,String> param = (Map<String, String>) redismap.get(key);
		String text = null;
		if(param==null){
			String url = "http://gx.189.cn/service/bill/fycx/inxxall.jsp?ACC_NBR="+login.getLoginName();//自己拼的
		   text = cutil.get(url);
			if(text!=null){
				Document doc = Jsoup.parse(text);	
				String PRODTYPE = doc.getElementById("PRODTYPE").val();//2100297
				String RAND_TYPE = doc.getElementById("RAND_TYPE").val();//002
				String BureauCode = doc.getElementById("BureauCode").val();//1100
				String PROD_TYPE = doc.getElementById("PROD_TYPE").val();//2100297
				String REFRESH_FLAG = doc.getElementById("REFRESH_FLAG").val();//1
				param = new HashMap<String, String>();
				param.put("PRODTYPE", PRODTYPE);
				param.put("PROD_TYPE", PROD_TYPE);
				param.put("RAND_TYPE", RAND_TYPE);
				param.put("BureauCode", BureauCode);
				param.put("REFRESH_FLAG",REFRESH_FLAG);
				param.put("PASSWORD", "");
				param.put("PROD_PWD", "");
				param.put("SERV_NO", "");
				param.put("REFRESH_FLAG", "1");
				param.put("QRY_FLAG", "1");
				param.put("BEGIN_DATE", "");
				param.put("END_DATE", "");
				param.put("OPER_TYPE", "CR1");
				param.put("MOBILE_NAME", login.getLoginName());
				param.put("ACC_NBR", login.getLoginName());
				param.put("ACCT_DATE", DateUtils.formatDate(new Date(),"yyyyMM"));
				param.put("FIND_TYPE", "1032");
				
				redismap.put(key, param);
			}
		}
		if(text!=null){
			text = cutil.post("http://gx.189.cn/service/bill/getRand.jsp", param);
			if(text!=null){
				if(text.contains("<flag>0</flag>")){
					errorMsg = "发送成功!";
					status = 1;
				}else{
					//<msg>发送随机密码失败，请稍后操作！ (错误日志序号：188893145)</msg>
					RegexPaserUtil rp = new RegexPaserUtil("<msg>","</msg>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					errorMsg = rp.getText();
				}
				
			}
		}else{
			log.warn("广西电信获取通话记录发送动态手机口令失败");
		}
		if(errorMsg==null){
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
	/** 校验验证是否成功
	 * @return
	 */
	public Map<String,Object> checkPhoneDynamicsCode(){
		String url = "http://gx.189.cn/service/bill/fycx/inxxall.jsp";
		Map<String,String> param = (Map<String,String>) redismap.get("PhoneDynamicsCode");
		//默认当月市话清单
		param.put("PASSWORD",login.getPhoneCode());
		String text = cutil.post(url, param);
		if(text.contains("您输入的查询验证码错误")) {
			errorMsg = "您输入的查询验证码错误或过期，请重新核对或再次获取！";
		}else if (text.contains("客户名称")) {
			status = 1;	
		}else{
			errorMsg = "验证失败,请重试!";
		}
//		if(text!=null){
//			RegexPaserUtil rp = new RegexPaserUtil(" <td colspan=\"2\">", "<",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			String result = rp.getText();
//			if(result!=null){
//				errorMsg = "您输入的查询验证码错误或过期，请重新核对或再次获取！";
//			}else{
//				if(text.contains("对不起！您没有查询权限")){
//					errorMsg = "对不起！您没有查询权限,请登录!";
//				}else{
//					status = 1;	
//				}
//			}
//		}else{
//			errorMsg = "验证失败,请重试!";
//		}
		map.put(CommonConstant.status, status);
		map.put(CommonConstant.errorMsg, errorMsg);
		if(status==1){
        	addTask_2(this);
    	}
		return map;
	}
	/**通话记录/通话详单
	 * @return
	 */
	public List  spiderCallHistory(){
		List<DianXinDetail> list1 = new ArrayList<DianXinDetail>();
		
		String url = "http://gx.189.cn/service/bill/fycx/inxxall.jsp";
		Map<String,String> pa =  (Map<String,String>) redismap.get("PhoneDynamicsCode");
		Map<String,String> param = new HashMap<String,String>();
		param.put("ACC_NBR",login.getLoginName());
		param.put("PROD_TYPE",pa.get("PROD_TYPE").toString());
		param.put("BEGIN_DATE","");
		param.put("END_DATE","");
		param.put("REFRESH_FLAG",pa.get("REFRESH_FLAG").toString());
		param.put("radioQryType","on");
		param.put("QRY_FLAG",pa.get("QRY_FLAG").toString());
		param.put("ACCT_DATE_1",DateUtils.getBeforeMonth(new Date(), "yyyyMM", 6)); //可变化
		String text = null;
		//默认市话,长话,港澳台,漫游
		String[][] codes = {{"1030","市话"},{"1037","长话"},{"1038","港澳台"},{"1039","漫游"}};
//		String[][] codes = {{"1039","漫游"}};
		List<String> list = DateUtils.getMonths(6,"yyyyMM");
		
		DianXinDetail dianxin = new DianXinDetail(login.getLoginName());
		dianxin = dianXinDetailService.getMaxTime(dianxin);
		
		boolean b = true;
		for (int i = 0; i < codes.length; i++) {
			param.put("FIND_TYPE",codes[i][0]); //可变化
			for (int j = 0; j < list.size(); j++) {
				param.put("ACCT_DATE",list.get(j)); //可变化
				text = cutil.post(url, param);
				if(text!=null){
					if(text.indexOf("(错误日志序号：")!=-1){
						break;
					}else if(text.indexOf("无记录")!=-1){
						continue;
					}else{
						Document doc = Jsoup.parse(text);
						Element el = doc.getElementById("list_table");
						try{
							if(el!=null){
								 Elements trs = el.select("tr");
								 for (int k = 3; k < trs.size(); k++) {
									 DianXinDetail dxd = new DianXinDetail();
									Elements tds =trs.get(k).select("td");
									dxd.setCallWay(tds.get(1).text());//呼叫类型
									dxd.setTradeAddr(tds.get(2).text());//通信地点
									dxd.setRecevierPhone(tds.get(4).text()); //对方号码
									dxd.setcTime(DateUtils.StringToDate(tds.get(5).text(), "yyyy-MM-dd HH:mm:ss")); //通话开始时间
									dxd.setTradeTime(Integer.parseInt(tds.get(6).text())); //通话时长
									 if(dianxin!=null){
										 if(dianxin.getcTime().getTime()>=dxd.getcTime().getTime()){
											break;
										 }
									 }
									dxd.setTradeType(codes[i][1]);
									dxd.setAllPay(new BigDecimal(tds.get(8).text())); //总通话费用
									list1.add(dxd);
								 }
							}
						}catch(Exception e){
							logger.error("error",e);
						}
					}
				}else{
					writeLogByHistory(null);
				}
			}
		}
		return list1;
	}
	/**
	 * 通话记录
	 * @return 
	 */
	public List spiderMonthlyBill(){
		List<DianXinTel> list1 = new ArrayList<DianXinTel>();
		boolean isUpdate = false;
		List<String> list = DateUtils.getMonths(6,"yyyyMM");
		String url = "http://gx.189.cn//service/bill/fycx/cust_zd.jsp?ACC_NBR="+login.getLoginName();
		String u = null;
		String text = null;
		Document doc = null;
		Element el = null;
		RegexPaserUtil rp = null;

		DianXinTel maxTel = null;
		try {
			maxTel = new DianXinTel();
			maxTel.setTeleno(login.getLoginName());
			maxTel = dianXinTelService.getMaxTime(maxTel);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Date d = null;
		for (int  i = list.size()-1; i>=0; i--) {
			d = DateUtils.StringToDate(list.get(i), "yyyyMM");
			try{
				
				if(i==0){
					//当月
					//http://gx.189.cn/chaxun/ajax/hfcx.jsp
					//ACC_NBR=18934700051&PROD_TYPE=2100297&ACCT_NBR_97=104002706570440
					Map<String,String> param = new HashMap<String,String>();
					param.put("ACC_NBR",login.getLoginName());
					param.put("PROD_TYPE","2100297");
					param.put("ACCT_NBR_97","104002706570440");
					text = cutil.post("http://gx.189.cn/chaxun/ajax/hfcx.jsp", param);
					doc = Jsoup.parse(text);
					Element span = doc.select("span[class=fee]").get(0);
					String fee = span.text().trim();
					if(fee.equals("")){
						fee = "0";
					}
					
					DianXinTel tel = new DianXinTel();
					tel.setcTime(d);
					tel.setcAllPay(new BigDecimal(fee));
					tel.setDependCycle(list.get(i).substring(0, 4) + "年"
							+ list.get(i).substring(4, 6) + "月01日至"
							+ DateUtils.formatDate(new Date(), "yyyy年MM月dd日"));
					if (redismap.get(nameKey) != null) {
						tel.setcName(redismap.get(nameKey).toString());
					}
					list1.add(tel);
					
					
					
				} else {
					u = url + "&DATE=" + list.get(i);
					text = cutil.get(u);
					if (text != null && text.contains("错误日志序号：")) {
						continue;
					}
					if (text != null) {
						doc = Jsoup.parse(text);
						el = doc.select("table").first();
						Elements trs = el.select("tr");
						BigDecimal bigd = null;
						rp = new RegexPaserUtil("费用合计", "元", text,
								RegexPaserUtil.TEXTEGEXANDNRT);
						bigd = new BigDecimal(rp.getText());
						if (maxTel != null) {
							if (maxTel.getcTime().getTime() > d.getTime()) {
								break;
							} else if (maxTel.getcTime().getTime() == d
									.getTime()) {
								updateTel(d, bigd);
								break;
							}

						}
						DianXinTel tel = new DianXinTel();
						tel.setcTime(d);
						tel.setcAllPay(bigd);
						if (redismap.get(nameKey) != null) {
							tel.setcName(redismap.get(nameKey).toString());
						}

						tel.setDependCycle(list.get(i).substring(0, 4)
								+ "年"
								+ list.get(i).substring(4, 6)
								+ "月01日至"
								+ DateUtils.lastDayOfMonth(list.get(i),
										"yyyyMM", "yyyy年MM月dd日"));
						list1.add(tel);
					}
				}
			}catch(Exception e){
				logger.error("error",e);
			}
		}
		return list1;
	}
	public User spiderInfo(){
		String text = cutil.get("http://gx.189.cn/chaxun/?SERV_NO=FCX-2");
		User user = null;
		if(text!=null){
			Document doc = Jsoup.parse(text);
			String name = "";
			try{
				name = doc.getElementById("prod_list").select("tr").get(1).select("td").get(3).text();
				redismap.put(nameKey,name);
			}catch(Exception e){
				logger.error("error",e);
			}
			user = new User();
			user.setPhoneRemain(yue());
			user.setUserName(name);
			user.setRealName(name);
//			user.setPhoneRemain(new BigDecimal(0));
		}
	
		return user;
	}
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
		int type = login.getType();
		try{
			parseBegin();
			switch (type) {
			case 1:
				getUser(); //个人信息,获取名称
				getMonthlyBill();//通话记录
				
				break;
			case 2:
				getCallHistory(); //历史账单	
				getSmsLog();//短信记录
				getflow();//流量记录
				break;
			case 3:
				getUser(); //个人信息 ,获取名称
				getMonthlyBill();//通话记录,使用名称
				getCallHistory(); //历史账单	
				getSmsLog();//短信记录
				getflow();//流量记录
				break;
			default:
				break;
			}
		}finally{
			parseEnd();
		}
	}
	
	//获取短信记录
	public void getSmsLog()
	{
		String url = "http://gx.189.cn/service/bill/fycx/inxxall.jsp";
		Map<String,String> param = new HashMap<String,String>();
		param.put("ACC_NBR",login.getLoginName());
		param.put("PRODTYPE","2100297");
		param.put("PROD_TYPE","2100297");
		param.put("RAND_TYPE","002");
		param.put("BureauCode","1100");
		param.put("PROD_PWD","");
		param.put("BEGIN_DATE","");
		param.put("END_DATE","");
		param.put("REFRESH_FLAG","1");
		param.put("FIND_TYPE","1032");
		param.put("radioQryType","on");
		param.put("QRY_FLAG","1");
		param.put("SERV_NO","");
		param.put("MOBILE_NAME",login.getLoginName());
		param.put("OPER_TYPE","CR1");
		List<String> list = DateUtils.getMonths(6,"yyyyMM");
		Date lastTime = null;
		try{
			if(telcomMessageService.getMaxSentTime(login.getLoginName())!=null)
			lastTime = telcomMessageService.getMaxSentTime(login.getLoginName()).getSentTime();
		}
		catch(Exception e)
		{
			logger.error("error",e);
		}	
		for (int j = 0; j < list.size(); j++) {
			param.put("ACCT_DATE",list.get(j));
			String text = cutil.post(url, param);
			if(text!=null&&text.contains("记录数")){
				Document doc = Jsoup.parse(text);
				Element tbody = doc.select("table[class=table no_border_top]").select("tbody").first();
				Elements trs = tbody.select("tr");
				for(int i=2;i<trs.size()-1;i++)
				{
					Elements tds = trs.get(i).select("td");
					if(tds.size()==8)
					{
						String BusinessType=tds.get(2).text();
						String RecevierPhone=tds.get(4).text();
						String SentTime=tds.get(5).text();
						String AllPay=tds.get(7).text();
						
						Date sentTime=null;
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
						
						if(BusinessType.contains("发")) BusinessType = "发送";
						else if(BusinessType.contains("收")) BusinessType = "接收";
						
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
	//获取流量记录
		public void getflow()
		{
			String url = "http://gx.189.cn/service/bill/fycx/inxxall.jsp";
			Map<String,String> param = new HashMap<String,String>();

			param.put("ACC_NBR",login.getLoginName());
			param.put("BEGIN_DATE","");
			param.put("END_DATE","");
			param.put("PROD_PWD","");
			param.put("FIND_TYPE","1044");
			param.put("PROD_TYPE","2100297");
			param.put("QRY_FLAG","1");
			param.put("REFRESH_FLAG","1");
			param.put("SERV_NO","");
			param.put("radioQryType","on");
			List<String> list = DateUtils.getMonths(6,"yyyyMM");
			Date lastTime = null;
			Date lastBillTime = null;
			try{
				DianXinFlowDetail detail = new DianXinFlowDetail(login.getLoginName());
				if(dianXinFlowDetailService.getMaxTime(detail)!=null) {
					lastTime = dianXinFlowDetailService.getMaxTime(detail).getBeginTime();	
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
				logger.error("error",e1);
			}
			for (int j = list.size()-1; j >= 0; j--) {
				param.put("ACCT_DATE",list.get(j));
				param.put("ACCT_DATE_1",list.get(j));
				Date month = DateUtils.StringToDate(list.get(j), "yyyyMM");
				String text = cutil.post(url, param);
				System.out.println(text);
				if(text!=null&&text.contains("记录数")){
					//详单
					try {
						flowDetail_parse(text,lastTime, month,j);
					} catch (Exception e) {
						logger.error("error",e);
					}
					//账单
					try {
						flowBill_parse(text,lastBillTime, month);
					} catch (Exception e) {
						logger.error("error",e);
					}
			}
			
		}
	
		
	}
		//流量详单解析
		public void flowDetail_parse(String text,Date lastTime,Date month,int iscm1) {

			Document doc = Jsoup.parse(text);
			Element table = doc.select("table[id=list_table]").first();
			Elements trs = table.select("tr");
			for(int i=2;i<trs.size()-1;i++)
			{
				Elements tds = trs.get(i).select("td");
				if(tds.size()==6)
				{
					BigDecimal flow = new BigDecimal(0);
					String netType = null;
					String location = null;
					BigDecimal fee = new BigDecimal(0);
					int iscm = 0;
					Date beginTime = null;
					try {
						String beginTime1 = tds.get(1).text();
						flow = new BigDecimal(StringUtil.flowFormat(tds.get(2).text()));
						netType = tds.get(3).text();
						location = tds.get(4).text();
						fee = new BigDecimal(tds.get(5).text());
						iscm = 0;
						if(iscm1 == 0) {
							iscm = 1;
						}

						beginTime = null;
						try{
							beginTime= DateUtils.StringToDate(beginTime1, "yyyy-MM-dd HH:mm:ss");
							if(lastTime!=null&&beginTime!=null){
								if(beginTime.getTime()<=lastTime.getTime()){
									continue;
								}	
							}	
						}
						catch(Exception e)
						{
							logger.error("error",e);
						}
					} catch (Exception e) {
						logger.error("error",e);
					}
					
					try {
						DianXinFlowDetail obj = new DianXinFlowDetail();
						obj.setPhone(login.getLoginName());
						UUID uuid = UUID.randomUUID();
						obj.setId(uuid.toString());
						obj.setFlow(flow);
						obj.setNetType(netType);
						obj.setLocation(location);
						obj.setIscm(iscm);
						obj.setFee(fee);
						obj.setBeginTime(beginTime);
						obj.setIscm(iscm);
						dianXinFlowDetailService.saveDianXinFlowDetail(obj);
					} catch (Exception e) {
						logger.error("error",e);
					}
				}
			}
		}
		//流量账单解析
		public void flowBill_parse(String text,Date lastBillTime,Date month) {

			Document doc = Jsoup.parse(text);
			Element table = doc.select("table[class=table no_border_bottom]").first();
			Elements trs = table.select("tr");
			
			String dependCycle = null;
			Date queryMonth = null;
			BigDecimal	allFlow = new BigDecimal(0);
			BigDecimal	allPay = new BigDecimal(0);
			//BigDecimal allTime = new BigDecimal(0);
			try {
				//  总时长没有
				 dependCycle = trs.get(2).select("td").get(0).text().replace("起止日期：", "");
				 queryMonth = month;
				 allFlow = new BigDecimal(StringUtil.flowFormat(trs.get(3).select("td").get(0).text().replace("总流量：", "")));
				 allPay = new BigDecimal(trs.get(3).select("td").get(1).text().replace("总费用：", "").replace("元",""));
			} catch (Exception e1) {
				logger.error("error",e1);
			}
			try{
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
						logger.error("error",e);
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
							logger.error("error",e);
						}
						dianXinFlowService.saveDianXinFlow(obj);
					} 
				}
			
			}
			catch(Exception e)
			{
				logger.error("error",e);
			}
			
		}
				
	public static void main(String[] args) {
		//正确
		Login login = new Login("18934700051","270248");
//		Login login = new Login("18934700054","270248");
		GXDianxin hn = new GXDianxin(login);
		hn.index();
		Map<String,Object> map = hn.login();
		hn.close();
		hn.getUser();
		List list = hn.getMonthlyBill();
		list = hn.getCallHistory();
		System.out.println("---------");
	}

}
