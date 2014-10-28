package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Evaluator.IsLastChild;

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
import com.lkb.test.jsdx.RegexPaserUtil;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.constant.ConstantHC;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

/**
 * 江苏电信
 * */
public class JSDianxin extends BaseInfoMobile{
	protected static Logger logger = Logger.getLogger(JSDianxin.class);

	public static  String imgurl="http://js.189.cn/rand.action";
	/**个人信息*/
	public String info = "jsdianxin_info";

	public JSDianxin(Login login,IWarningService warningService,String currentUser) {
		super( login, warningService,ConstantNum.comm_js_dx,currentUser);
		this.userSource = Constant.DIANXIN;
	}
	public static void main(String[] args) {
		JSDianxin js = new JSDianxin(new Login("18051774753","102593"), null,null);
//		JSDianxin js = new JSDianxin(new Login("18051774755","102593"), null,null);
//		js.index();
//		js.inputCode(imgurl);
//		js.login();
//		js.close();
		if(js.islogin()){
			js.gatherFlowBill();
			try {
				js.gatherFlowList();
			} catch (Exception e) {
				logger.error("error",e);
			}
		}
		
	}
//	public Map<String,Object> index(){
//		if(!isInit()){
//			 setImgUrl(imgurl);
//			 setInit();
//		}
//		map.put("url",getAuthcode());
//		return map;
//	}
 public void init(){
	 setImgUrl(imgurl);
 }


	/**第一次post*/
	public  Map<String,Object> login(){
		try{
			login_1();
			//是否需要二次验证
			boolean isSecLogin = false;
			if(errorMsg==null){
				//http://js.189.cn/service/account ,此处个人信息,可打开个人资料页
				String text = cutil.get("http://js.189.cn/getSessionInfo.action");
				if(text!=null&&text.contains("productId")&&text.contains(login.getLoginName())){
					int num =  isSecLogin();
					switch (num) {
					case 1:
						isSecLogin = true;
						loginsuccess();
						redismap.put(info, text);
						break;
					case 2:
						loginsuccess();
						redismap.put(info, text);
						break;
					default:
						break;
					}
				}
				//System.out.println(text);
			}
	    	map.put("isSecLogin", isSecLogin);
	    	//System.out.println(msg);
	    	if(status==1){
	    		if(isSecLogin){
	    			if(temp1()){
	    				addTask(this);
	    				map.put("isSecLogin", false);
	    			}else{
	    				addTask_1(this);
	        			sendPhoneDynamicsCode();
	    			}
	    		}else{
	    			addTask(this);
	    		}
	    	}
		}catch(Exception e){
			logger.error("error",e);
		}
    	return map;
	}
	public boolean  temp1(){
		List<String> ms =  DateUtils.getMonths(1,"yyyyMM");
		String starDate = ms.get(0)+"01";
		String endDate = ms.get(0)+"31";
		
		Map<String,String>	param = new HashMap<String,String>();
		param.put("inventoryVo.accNbr", login.getLoginName());
		param.put("inventoryVo.accNbr97","");
		param.put("inventoryVo.acctName", login.getLoginName());
		param.put("inventoryVo.begDate", starDate);
		param.put("inventoryVo.endDate", endDate);
		param.put("inventoryVo.family", "4");
		param.put("inventoryVo.getFlag","3");
		param.put("inventoryVo.productId", "4");
    	String text = cutil.post("http://js.189.cn/queryVoiceMsgAction.action",param);
    	if(text!=null&&text.contains("\"TUXEDO_MSG\":\"成功\"")){
    		System.out.println( "打开成功了.....");
    		return true;
    	}
		return false;
	}
	/**
	 * 1.需要手机验证,0.是错误,2不需要验证
	 * @return
	 */
	public int isSecLogin(){
		int num = 0;
		//检测是否需要二次验证
		Map<String,String> param = new HashMap<String,String>();
		param.put("inventoryVo.accNbr", login.getLoginName());
		String text1 = cutil.post("http://js.189.cn/querySecondValidateStatuAction.action",new CHeader("http://js.189.cn/service/bill?tabFlag=billing2"), param);
		if(text1!=null){
			if(text1.equals("0")){
				//需要二次验证
				num =1;
			}else{
				num = 2;
			}
		}
		return num;
	}
	/**第一次post*/
	private void login_1(){
		String url = "http://js.189.cn/self_service/validateLogin.action";
		CHeader h = new CHeader("http://js.189.cn/");
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("logonPattern", "1"); 
		param.put("isshow", "2"); 
		param.put("ssoURL", "http://js.189.cn/bussiness/page/new/sso.html?"+Math.random()); 
		param.put("userType", "2000004"); 
		param.put("productId", login.getLoginName()); 
		param.put("loginPwdType", "cellPassword"); 
		param.put("userPwd", login.getPassword()); 
		param.put("validateCode", login.getAuthcode()); 
		param.put("serviceNumber", "可不填"); 
//		h.setCookie(ConstantHC.getCookie(map));
		String text =  cutil.post(url, h, param);
		//输出cookie
    	if(text!=null){
    		if(text.contains("验证码错误")){
    			errorMsg = "您输入的验证码有误!";
    		}
    		login2(text);
    	}
	}
	/**第一次post, 老入口 废弃*/
	private String login1(){
		String url = "http://js.189.cn/self_service/validateLogin.action";
		CHeader h = new CHeader("http://js.189.cn/service");
		Map<String,String> param = new HashMap<String,String>();
		param.put("logonPattern", "1"); 
		param.put("userType", "2000004"); 
		param.put("validateCode", ConstantHC.getVcode(map)); 
		param.put("qqNum", ""); 
		param.put("productId", ConstantHC.getUsername(map)); 
		param.put("loginPwdType", "cellPassword"); 
		param.put("userPwd", ConstantHC.getPass(map)); 
		//System.out.println("验证码"+ConstantHC.getVcode(map));
		param.put("validate", ConstantHC.getVcode(map)); 
//		h.setCookie(ConstantHC.getCookie(map));
		String text =  cutil.post(url, h, param);
		//输出cookie
	    	//System.out.println(text);
    	if(text!=null){
    		if(text.contains("验证码错误")){
    			return text;
    		}
    		login2(text);
    	}
	    return null;
	}
	/**第二次post*/
	private  void login2(String text){
		Map<String,String> 	param = new HashMap<String,String>();
		RegexPaserUtil rp = new RegexPaserUtil("name=\"SSORequestXML\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String requestXml = rp.getText();
		if(requestXml!=null){
			param.put("SSORequestXML",requestXml);	
			rp = new RegexPaserUtil("paramUam\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("paramUam",rp.getText());
			rp = new RegexPaserUtil("ds\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("ds",rp.getText());
			rp = new RegexPaserUtil("systemCode\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("systemCode",rp.getText());
			
			CHeader h = new CHeader("http://js.189.cn/self_service/validateLogin.action");
			String location =cutil.post("https://uam.telecomjs.com/sso/JsLoginIn", h,param);
		    	if(location.contains("UATicket=-2")){
		    		errorMsg = "您输入的账号和密码不匹配!";
		    	}else{
		    		text = cutil.get(location);
					if(text!=null){
						login4(text);
					}
		    	}
		    }
	}

	/**第四次post*/
	private void login4(String text){
		Map<String,String> 	param = new HashMap<String,String>();
		RegexPaserUtil rp = new RegexPaserUtil("action=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String url = rp.getText();
		if(url!=null&&url.contains("js.189.cn/bussiness/page/new/sso.html")){
			rp = new RegexPaserUtil("id=\"sp\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("sp",rp.getText());
			rp = new RegexPaserUtil("id=\"ssoURL\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("ssoURL",rp.getText());
			rp = new RegexPaserUtil("redirectURL\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("redirectURL",rp.getText());
			rp = new RegexPaserUtil("id=\"isshow\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("isshow",rp.getText());
			rp = new RegexPaserUtil("d=\"msg\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("msg",rp.getText());
			rp = new RegexPaserUtil("result\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("result",rp.getText());
			rp = new RegexPaserUtil("loginDescription\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
			param.put("loginDescription",rp.getText());
			CHeader  h = new CHeader(CHeaderUtil.Accept_,null,null,"js.189.cn");
			text = cutil.post(url.trim(),param);
			//System.out.println(text);
		}
	}
			
	/**
	 * 生成短信
	 * */
	public Map<String,Object> sendPhoneDynamicsCode() {
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		int status = 0;
		Map<String,String>	param = new HashMap<String,String>();
		
		addCookie("Js_userId", "2000004%3B"+login.getLoginName(),"js.189.cn");
		addCookie("Js_isLogin", "yes","js.189.cn");
		
		CHeader  h = new CHeader("http://js.189.cn/service/bill?tabFlag=billing1");
		param.put("inventoryVo.accNbr", login.getLoginName());
		param.put("inventoryVo.productId","2000004");
		String text = cutil.post("http://js.189.cn/queryCheckSecondPwdAction.action",h,param);
		if(text!=null&&text.contains("系统已自动将二次验证码发送到您指定的号码上")){
			status = 1;
			errorMsg = "口令发送成功!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg==null?"发送失败,请重试!":errorMsg);
		return map;
	}
	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
		Map<String,String>	param = new HashMap<String,String>();
		param.put("inventoryVo.accNbr", login.getLoginName());
		param.put("inventoryVo.action","check");
		param.put("inventoryVo.inputRandomPwd",login.getPhoneCode());
		param.put("inventoryVo.productId", "2000004");
	
		String text = cutil.post("http://js.189.cn/queryValidateSecondPwdAction.action",param);
		try{
	    	if(text.contains("成功")){
	    		status = 1;
		    }
		}catch(Exception e){
			errorMsg = "您输入的口令不正确,请重新输入!";
			logger.error("error",e);
		}
		map.put(CommonConstant.status,status);
		map.put(errorMsg,errorMsg==null?"您输入的口令不正确,请重新输入!":errorMsg);
		if(status==1){
//        	addTask_2(this);
			addTask(this);
    	}
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
				getTelDetailHtml();//通话记录
				getMyInfo(); //个人信息
				break;
			case 2:
				callHistory(); //历史账单	
				getSmsLog();	//短信详单记录
				saveFlowBill();
				saveFlowList();
				break;
			case 3:
				getTelDetailHtml();//通话记录
				getMyInfo(); //个人信息
				callHistory(); //历史账单
				getSmsLog();	//短信详单记录
				saveFlowBill();
				saveFlowList();
				del();
				break;
			default:
				break;
			}
		}finally{
			parseEnd(Constant.DIANXIN);
		}
	}
	/**
	 * 查询个人信息
	 */
	public boolean getMyInfo() {
			//话费余额不要,无用
			BigDecimal phoneremain= new BigDecimal("0.00");
			try {
				String yue = cutil.get("http://js.189.cn/chargeQuery/chargeQuery_queryBalanceInfo.action?productType=4&changeUserID="+login.getLoginName());
				//System.out.println(yue);
				if(yue.contains("成功")){
					JSONObject json=new JSONObject(yue);
					//JSONObject results =  json.getJSONObject("blances");
					 JSONArray array = json.getJSONArray("blances");  
					 String blance = array.getJSONObject(0).getString("blance");  
					 BigDecimal b = 	new BigDecimal(blance);
					phoneremain=b.divide(new BigDecimal("100"));
					 
				}
			String zl = (String)redismap.get(info);
			if(zl.contains("\"userName\"")){
				JSONObject json=new JSONObject(zl);
				String xm = json.getString("userName");
				String indentCode = json.getString("indentCode");
				Map<String, String> map = new HashMap<String, String>();
				map.put("parentId", currentUser);
				map.put("usersource", Constant.DIANXIN);
				map.put("loginName", login.getLoginName());
				List<User> list = userService.getUserByParentIdSource(map);
				if (list != null && list.size() > 0) {
					User user = list.get(0);
					user.setLoginName(login.getLoginName());
					user.setLoginPassword("");
					user.setUserName(xm);
					user.setRealName(xm);
					user.setIdcard(indentCode);
					user.setAddr("");
					user.setUsersource(Constant.DIANXIN);
					user.setParentId(currentUser);
					user.setModifyDate(new Date());
					user.setPhone(login.getLoginName());
					user.setFixphone("");
					user.setPhoneRemain(phoneremain);
					user.setEmail("");
					userService.update(user);
				} else {
					User user = new User();
					UUID uuid = UUID.randomUUID();
					user.setId(uuid.toString());
					user.setLoginName(login.getLoginName());
					user.setLoginPassword("");
					user.setUserName(xm);
					user.setRealName(xm);
					user.setIdcard(indentCode);
					user.setAddr("");
					user.setUsersource(Constant.DIANXIN);
					user.setParentId(currentUser);
					user.setModifyDate(new Date());
					user.setPhone(login.getLoginName());
					user.setFixphone("");
					user.setPhoneRemain(phoneremain);
					user.setEmail("");
					userService.saveUser(user);
				}
				return true;	
			}
		} catch (Exception e) {
			logger.error("error",e);
			String warnType = WaringConstaint.JSDX_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

		return false;
	}
	
	/**
		查询获取短信记录*/
	public void getSmsLog()
	{
		
		try{
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			Date lastTime = null;
			try{
				lastTime = telcomMessageService.getMaxSentTime(login.getLoginName()).getSentTime();
			}
			catch(Exception e)
			{
				logger.error("error",e);
			}		
			for (String s : ms) {
				String starDate = s+"01";
				String endDate = s+"31";
				System.out.println("");
				Map<String,String>	param = new HashMap<String,String>();
				param.put("inventoryVo.accNbr", login.getLoginName());
				param.put("inventoryVo.accNbr97","");
				param.put("inventoryVo.acctName", login.getLoginName());
				param.put("inventoryVo.begDate", starDate);
				param.put("inventoryVo.endDate", endDate);
				param.put("inventoryVo.family", "4");
				param.put("inventoryVo.getFlag","3");
				param.put("inventoryVo.productId", "4");
				String text = cutil.post("http://js.189.cn/mobileInventoryAction.action",param);
				if(text!=null&&text.contains("\"TUXEDO_MSG\":\"成功\""))
				{
					JSONObject json=new JSONObject(text);
					//JSONObject results =  json.getJSONObject("blances");
					JSONArray array = json.getJSONArray("items");  
					for(int i=0;i<array.length();i++){  
						JSONObject jsonobject = array.getJSONObject(i);  
						String ticketChargeCh =  jsonobject.getString("ticketChargeCh");
						String startTimeNew =  jsonobject.getString("startTimeNew");
						String nbr =  jsonobject.getString("nbr");
						String ticketType =  jsonobject.getString("ticketType").trim();
						String ticketNumber =  jsonobject.getString("ticketNumber");
						String productName =  jsonobject.getString("productName");
						String startDateNew =  jsonobject.getString("startDateNew");
						String qssj= startDateNew+" "+startTimeNew;
						
						Date sentTime =null;
						try{
							sentTime= DateUtils.StringToDate(qssj, "yyyy-MM-dd HH:mm:ss");
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
						
						if(ticketType.equals("短信发"))
						{
							ticketType = "发送";
						}
						else if(ticketType.equals("短信收"))
						{
							ticketType = "接收";
						}
											
						TelcomMessage obj = new TelcomMessage();
						obj.setPhone(login.getLoginName());
						obj.setBusinessType(ticketType);//业务类型：点对点
						obj.setRecevierPhone(nbr);//对方号码
						obj.setSentTime(sentTime);//发送时间
						obj.setCreateTs(new Date());
						UUID uuid = UUID.randomUUID();
						obj.setId(uuid.toString());
						obj.setAllPay(Double.parseDouble(ticketChargeCh));//总费用
						telcomMessageService.save(obj);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("error",e);
		}
		
	}
	
	/**
	 * 查询通话记录
	 */
	public void callHistory(){
		try{
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			boolean b = true;
			DianXinDetail detail = new DianXinDetail();
			detail.setPhone(login.getLoginName());
			detail = dianXinDetailService.getMaxTime(detail);
			for (String s : ms) {
				String starDate = s+"01";
				String endDate = s+"31";
				System.out.println("");
				Map<String,String>	param = new HashMap<String,String>();
				param.put("inventoryVo.accNbr", login.getLoginName());
				param.put("inventoryVo.accNbr97","");
				param.put("inventoryVo.acctName", login.getLoginName());
				param.put("inventoryVo.begDate", starDate);
				param.put("inventoryVo.endDate", endDate);
				param.put("inventoryVo.family", "4");
				param.put("inventoryVo.getFlag","3");
				param.put("inventoryVo.productId", "4");
		    	String text = cutil.post("http://js.189.cn/queryVoiceMsgAction.action",param);
		    	
		    	if(text!=null&&text.contains("\"TUXEDO_MSG\":\"成功\"")){
		    	 JSONObject json=new JSONObject(text);
					//JSONObject results =  json.getJSONObject("blances");
				 JSONArray array = json.getJSONArray("items");  
				 for(int i=0;i<array.length();i++){  
					   JSONObject jsonobject = array.getJSONObject(i);  
					   String ticketChargeCh =  jsonobject.getString("ticketChargeCh");
					   String startTimeNew =  jsonobject.getString("startTimeNew");
					   String duartionCh =  jsonobject.getString("duartionCh");
					   String nbr =  jsonobject.getString("nbr");
					   String areaCode =  jsonobject.getString("areaCode");
					   String ticketType =  jsonobject.getString("ticketType");
					   String ticketNumber =  jsonobject.getString("ticketNumber");
					   String productName =  jsonobject.getString("productName");
					   String startDateNew =  jsonobject.getString("startDateNew");
					   String qssj= startDateNew+" "+startTimeNew;
				
						DianXinDetail dxDetail = new DianXinDetail();
						UUID uuid = UUID.randomUUID();
						dxDetail.setId(uuid.toString());
						dxDetail.setcTime(DateUtils.StringToDate(qssj, "yyyy-MM-dd HH:mm:ss"));
						if(detail!=null&&detail.getcTime().getTime()>=dxDetail.getcTime().getTime()){
							b = false;
							break;
						}
						int times = 0;
						try{
							times = TimeUtil.timetoint(duartionCh);
						}catch(Exception e){
							logger.error("error",e);
						}
						
							dxDetail.setTradeTime(times);
							dxDetail.setTradeAddr(areaCode);
							if(ticketType.contains("本地")){
								dxDetail.setTradeType("本地");
							}else{
								dxDetail.setTradeType("漫游");
							}
							
							if(ticketType.contains("主叫")){
								dxDetail.setCallWay("主叫");
							}else{
								dxDetail.setCallWay("被叫");
							}
							//dxDetail.setCallWay(hjlx);
							dxDetail.setRecevierPhone(nbr);
							//dxDetail.setBasePay(new BigDecimal(jbthf));
							//dxDetail.setLongPay(new BigDecimal(ctthf));
							dxDetail.setAllPay(new BigDecimal(ticketChargeCh));
							dxDetail.setPhone(login.getLoginName());
							dianXinDetailService.saveDianXinDetail(dxDetail);	
						}
					 if(!b){
						 break;
					 }

		    	}
		    	}	
			del(5);
		} catch (Exception e) {
			logger.error("error",e);
		}
	}
	
	/**
	 * 查询话费记录
	 */
	public boolean getTelDetailHtml() {
		try {
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			DianXinTel maxTel = new DianXinTel();
			maxTel.setTeleno(login.getLoginName());
			maxTel = dianXinTelService.getMaxTime(maxTel);
			Date d = null;
			for (String s : ms) {
				String starDate = s ;
				d = DateUtils.StringToDate(starDate, "yyyyMM");
				if(maxTel!=null&&maxTel.getcTime().getTime()>=d.getTime()){
					break;
				}
				String text = cutil.get("http://js.189.cn/chargeQuery/chargeQuery_queryCustBill.action?billingCycleId="+starDate+"&queryFlag=0&productId=2&accNbr="+login.getLoginName());
				if(text!=null){
					BigDecimal total=new BigDecimal(0);
					BigDecimal tcf=new BigDecimal("0.00");
					BigDecimal ldxs=new BigDecimal("0.00");
					if(text.contains("帐户名称")){
						JSONObject json=new JSONObject(text);
						JSONArray custBaseInfo = json.getJSONArray("custBaseInfo");  
						 JSONObject khmcObject = custBaseInfo.getJSONObject(0);  
						 String kymc = khmcObject.getString("itemCharge");
						 JSONObject jfzqObject = custBaseInfo.getJSONObject(1);  
						 String dependCycle = jfzqObject.getString("itemCharge");
						 
						 JSONArray custBillInfoList = json.getJSONArray("custBillInfoList");  
						 for (int i = 0; i < custBillInfoList.length(); i++) {
							 JSONObject custBillInfoObject = custBillInfoList.getJSONObject(i);  
							String itemName = custBillInfoObject.getString("itemName");
							if(itemName.contains("本项小计")){
								String itemCharge = custBillInfoObject.getString("itemCharge");
								total= new BigDecimal(itemCharge).divide(new BigDecimal("100"));
							}else if(itemName.contains("套餐费")){
								String itemCharge = custBillInfoObject.getString("itemCharge");
								tcf= new BigDecimal(itemCharge).divide(new BigDecimal("100"));
							}else if(itemName.contains("来电显示费")){
								String itemCharge = custBillInfoObject.getString("itemCharge");
								ldxs= new BigDecimal(itemCharge).divide(new BigDecimal("100"));
							}
						}
					 	DianXinTel tel = new DianXinTel();
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
//						tel.setBaseUserId(currentUser);
						tel.setcTime(DateUtils.StringToDate(s, "yyyyMM"));
						tel.setTeleno(login.getLoginName());
						tel.setcAllPay(total);
						tel.setLdxsf(ldxs);
						tel.setZtcjbf(tcf);
						tel.setDependCycle(dependCycle);
						tel.setcName(kymc);
						dianXinTelService.saveDianXinTel(tel);
					}
				}
			}
		} catch (Exception e) {
			logger.error("error",e);
			return false;
		}
	
		return true;
	}
	public LinkedList<MonthlyBillMark> gatherFlowBill(){
		 LinkedList<MonthlyBillMark> listMark = getSpiderMonthsMark(true, "yyyyMM", 7, 1);
		 //--------单线程采集,不采用多线程了--------
		 Map<String,String> map = new HashMap<String,String>();
		 map.put("inventoryVo.accNbr",this.loginName);
		 map.put("inventoryVo.family","4");
		 map.put("inventoryVo.accNbr97","");
		 map.put("inventoryVo.productId","4");
		 map.put("inventoryVo.acctName",this.loginName);
		for (int i = 0; i < listMark.size(); i++) {
			if(i==0){
				map.put("inventoryVo.getFlag","3");
			}else{
				map.put("inventoryVo.getFlag","1");
			}
			 map.put("inventoryVo.begDate",listMark.get(i).getMonth()+"01");
			 map.put("inventoryVo.endDate",DateUtils.lastDayOfMonth(listMark.get(i).getMonth(), "yyyyMM", "yyyyMMdd"));
			listMark.get(i).setText(cutil.post("http://js.189.cn/queryNewDataMsgListAction.action",map));
		}
		List<DianXinFlowDetail> list = null;
		//------------------解析-------------------
		if(flowDetailList==null){
			flowDetailList = new LinkedList<DianXinFlowDetail>();
		}
		Date bigTime = getMaxFlowTime();
		for (int i=listMark.size()-1;i>=0; i--) {
			list  = gatherFlowBill_parse(listMark.get(i),i,bigTime);
			if(list!=null){
				flowDetailList.addAll(list);
			}
		}
		return listMark;
	}
	private LinkedList<DianXinFlowDetail> flowDetailList = null;
	public List gatherFlowBill_parse(MonthlyBillMark mark,int index,Date bigTime){
		String html = mark.getText();
		//System.out.println(html);
		List list = null;
		try {
			if(html!=null&&html.contains("\"items\"")){
				list = new ArrayList();
				String jsonArrayStr = null;
				String QUERY_RANG = StringUtil.subStr("QUERY_RANG\": \"","\",", html);
				DianXinFlow flow = new DianXinFlow();
//				if(index==0){
				jsonArrayStr = StringUtil.subStr("items\":", "]", html)+"]";
//				}else{
//					String startIndexStr = "\"items\":";
//					String endIndexStr = ",{";
//					int startIndex = html.indexOf(startIndexStr)+startIndexStr.length();
//					int endIndex = html.lastIndexOf(endIndexStr);
//					jsonArrayStr = html.substring(startIndex, endIndex)+"]";
//				}
				flow.setPhone(this.loginName);
				flow.setDependCycle(QUERY_RANG);
				String  totalFlow = StringUtil.subStr("totalBytesCnt\":\"","\",", html);
				String totalTime = StringUtil.subStr("duartionCntCh\":\"","\",", html);
				String totalFee = StringUtil.subStr("changeCntch\":\"","(元", html);
				flow.setAllFlow(new BigDecimal(StringUtil.flowFormat(totalFlow)));
				flow.setAllPay(new BigDecimal(totalFee));
				flow.setAllTime(new BigDecimal(StringUtil.flowTimeFormat(totalTime)));
				//------------
				JSONArray array = new JSONArray(jsonArrayStr);
				Date startTime = null;
				//System.out.println(array.length());
				for(int i=0;i<array.length();i++){  
				   JSONObject jsonobject = array.getJSONObject(i);  
				   String feiyong =  jsonobject.getString("TICKET_CHARGE_CH");
				   String shichang =  jsonobject.getString("DURATION_CH");
				   String startTimeStr =  jsonobject.getString("START_TIME");
				   startTime = DateUtils.StringToDate(startTimeStr, "yyyy-MM-dd HH:mm");
				   if(bigTime!=null&&bigTime.getTime()>=startTime.getTime()){
					   continue;
				   }
				   String wangluoleixing =  jsonobject.getString("SERVICE_TYPE");
				   String tongxindidian =  jsonobject.getString("TICKET_TYPE");
				   String liuliang =  jsonobject.getString("BYTES_CNT");
				   String yewuleixing =  jsonobject.getString("TICKET_NUMBER");
				   
				   DianXinFlowDetail obj = new DianXinFlowDetail();
				   obj.setId(UUID.randomUUID().toString());
					obj.setPhone(this.loginName);
					obj.setBeginTime(startTime);
					obj.setFee(new BigDecimal(feiyong));
					obj.setNetType(wangluoleixing);
					obj.setTradeTime(StringUtil.flowTimeFormat(shichang));
					BigDecimal flows = new BigDecimal(0);
					try {
						flows = new BigDecimal(StringUtil.flowFormat(liuliang));
					} catch (Exception e) {
						logger.error("error",e);
					}
					obj.setFlow(flows);
					obj.setLocation(tongxindidian);
					obj.setBusiness(yewuleixing);
					list.add(obj);
				}
				mark.setObj(flow);
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
		return list;
}

	public  LinkedList<DianXinFlowDetail> gatherFlowList() throws Exception{
		return flowDetailList;
		
	}
}
