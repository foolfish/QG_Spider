package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
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

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
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
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.AbstractCrawler;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

/**
 * 河南移动
 * 
 * @author fastw
 * @date 2014-8-2 下午3:01:55
 */
public class HENYidong extends BaseInfoMobile {
	private static final Logger log = Logger.getLogger(HENYidong.class);
	public String imgUrl = "https://ha.ac.10086.cn/checkImage";
	public String MonthTelUrl = "https://service.ha.10086.cn/service/self/tel-bill.action?menuCode=1026";
	public String CUST_NAME = "";
	
	/**
	 * 本地测试
	 * 
	 * @param login
	 * @param currentUser
	 */
	public HENYidong(Login login) {
		super(login);
	}

	public HENYidong(Login login, IWarningService warningService,
			String currentUser) {
		super(login, warningService, ConstantNum.comm_hen_yidong, currentUser);
	}
	public HENYidong(Login login,String currentUser) {
		super(login,  ConstantNum.comm_hen_yidong, currentUser);
	}
	@Override
    public void init(){
		setInit();
	}
	@Override
    public Map<String, Object> login() {
		Map<String, String> param;
		try{
		    param = new HashMap<String, String>();
		    param.put("Referer", "http://service.ha.10086.cn/service/self/customer-info-uphold.action?menuCode=1140");
			param.put("Host", "service.ha.10086.cn");
			
		    String text = login_0(param);
			//System.out.println(text);
			text = login_parseErrorText(text);//此处判断是否出现验证码比较特殊
			if(text==null){
				text = login_0(param); 
				text = login_parseErrorText(text);
			}
			if(errorMsg==null&&text!=null){
				login_1(text, param);
//				text = cutil.post("http://service.ha.10086.cn/service/index.action", param);
				//获取个人信息
				text = cutil.get("http://service.ha.10086.cn/service/self/customer-info-uphold.action?menuCode=1140");
				if(text==null){
					text = cutil.get("http://service.ha.10086.cn/service/self/customer-info-uphold.action?menuCode=1140");	
				}
				RegexPaserUtil rp = new RegexPaserUtil("尊敬的<span ","客户",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String text1 = rp.getText();
				String loginName = login.getLoginName();
				//System.out.println(text);
				if(text1!=null&&text1.contains(login.getLoginName())){
					loginsuccess();
					addTask_1(this);
					sendPhoneDynamicsCode();
				}
			}
			
		}catch(Exception e){
			writeLogByLogin(e);
		}
//		map.put(CommonConstant.status,status);
//		map.put(CommonConstant.errorMsg,errorMsg!=null?errorMsg:"提交错误,请重试,或您尝试刷新页面!");
		return map;
	}
	@Override
    public Map<String, Object> sendPhoneDynamicsCode(){
		 Map<String,Object> map = new LinkedHashMap<String,Object>();
			int status = 0;
			String errorMsg = "" ;
		String url = "https://service.ha.10086.cn/service/self/tel-bill!detail.action?menuCode=1026";
		CHeader h1 = new CHeader("https://service.ha.10086.cn/service/self/tel-bill.action");
		h1.setAccept_Language("zh-CN");
		String text = CUtil.getMethodGet(url, client, h1);
		//String url = "https://service.ha.10086.cn/verify!XdcxCodeStatus.action";
		url = "https://service.ha.10086.cn/verify!XdcxSecondAuthCode.action";
		//https://service.ha.10086.cn/verify!XdcxSecondAuthCode.action
		text = CUtil.getMethodPost(url, client, null);
		//System.out.println(text);
		if (text.contains("succ")) {
			status = 1;
			errorMsg = "短信验证码发送成功！";
		}else {
			
			errorMsg="30秒内只能获取一次验证码，不要频繁获取!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}

	@Override
    public Map<String, Object> checkPhoneDynamicsCode(){
		//String url = "https://service.ha.10086.cn/verify!XdcxCodeStatus.action";
		String url = "https://service.ha.10086.cn/verify!XdcxSecondAuth.action";
		CHeader cHeader= new CHeader("https://service.ha.10086.cn/service/self/tel-bill!detail.action?menuCode=1026");
		//https://service.ha.10086.cn/verify!XdcxSecondAuthCode.action
		Map<String, String> mp = new HashMap<String, String>();
		mp.put("verifyCode", login.getPhoneCode());
		String text = CUtil.getMethodPost(url, client,cHeader, mp);
		//System.out.println(text);
		Map<String, Object> map = new HashMap<String, Object>();
		if (text.contains("succ")) {
			status = 1;
			errorMsg = "短信验证码登陆成功！";
			addTask_2(this);
		}else {
			status = 0;
			errorMsg="您输入的短信验证码不正确,请核实后重新输入！";
		}
		
		map.put("status",status);
		map.put("msg", errorMsg);
		return map;
	}

	public void saveTelData() {
		boolean isUserInfo = saveYuE();
		boolean isTelDetail = saveMonthTel();
		boolean isPhoneDetail = saveDetailTel();
	}
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	@Override
    public  void startSpider(){
		try{
			parseBegin(Constant.YIDONG);
			switch (login.getType()) {
			case 1:
				saveYuE(); //个人信息
				saveMonthTel();//历史账单
				break;
			case 2:
				saveDetailTel(); //通话记录
				saveMessageHistory(); //保存短信
				saveLiuLiang();//流量   详单  账单
				break;
			case 3:
				saveYuE(); //个人信息
				saveMonthTel(); //历史账单	
				saveDetailTel(); //通话记录	
				saveMessageHistory();//查询短信记录
				saveLiuLiang();//流量
				break;
			default:
				break;
			}
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}
	private String login_0(Map<String, String> param){
		String url  = "https://ha.ac.10086.cn/ssosms.jsp"; //初始化参数,地址是的0.8kb的请求内容,只为获取session
		cutil.get(url);
		String encyrpty_phone = "";
		String encyrpty_password = "";
		try {
		url = "https://ha.ac.10086.cn/rsaKey";
		String text = cutil.get(url);
		JSONObject json = new JSONObject(text);
		String e =json.get("e").toString();
		String n =json.get("n").toString();
		String maxdigits = json.get("maxdigits").toString();
		String epass1 = AbstractCrawler.executeJsFunc("rsa/he_nan_yd_rsa.js", "getAll", login.getLoginName(), login.getPassword(),e,n,maxdigits);
//		String epass1 = AbstractCrawler.executeJsFunc("rsa/he_nan_yd_rsa.js", "getAll","11","22","10001","82ee7a9323f2233f385dfcf6845526eaa92ddd0310f3af80d6265c48cf83e2c581cad4d3ed9b756b0fb3ab871467cd01a8f3830b6b32de90f4878bc0f8fd6749","67");
		//System.out.println(epass1+"");
		json = new JSONObject(epass1);
		encyrpty_phone=json.get("phone").toString();
		encyrpty_password=json.get("pass").toString();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		param.put("IDToken1", encyrpty_phone);
		param.put("IDToken2", "2");
		param.put("IDToken3", encyrpty_password);
		param.put("IDToken4", "请输入验证码");
		param.put("SPID", "https://ha.ac.10086.cn/login");
		param.put("typeForPower", "null");
		return cutil.post("https://ha.ac.10086.cn/login",param);
	}
	
	
	private String login_1(String text,Map<String, String> param ){
	
		//http://service.ha.10086.cn/samlredirect.jsp?RelayState=http://service.ha.10086.cn/service/mobile/my-consume.action
	    String url = cutil.post(text,param);
		//System.out.println(url);//如果为空不做处理此处特殊情况
		text = cutil.get("http://service.ha.10086.cn/samlredirect.jsp?RelayState=http://service.ha.10086.cn/service/mobile/my-consume.action");
//		text = cutil.get("http://service.ha.10086.cn/samlredirect.jsp");
		if(text!=null){
			Document doc = Jsoup.parse(text);
			url = "https://ha.ac.10086.cn/SSOPOST";
			Elements el = doc.select("input[name=SAMLRequest]");
			if(el!=null){
				String SAMLRequest = el.val();
				String RelayState = doc.select("input[name=RelayState]").val();
				param = new HashMap<String, String>();
				param.put("SAMLRequest", SAMLRequest);
				param.put("RelayState", RelayState);
				text = cutil.post(url, param);
				if(text!=null){
					url = "https://service.ha.10086.cn/acauthen.jsp";
					doc = Jsoup.parse(text);
					el = doc.select("input[name=SAMLart]");
					if(el!=null){
						String SAMLart = el.val();
						param = new HashMap<String,String>();
						param.put("SAMLart", SAMLart);
						param.put("RelayState", RelayState);//ch.setCookie("CmWebNumSn=14791405282,sn; WT_FPC=id=21a9cbe5ed1115cb7521407396124467:lv=1407564547486:ss=1407564547481; flagcity=0000;CmProvid=ha;CmWebtokenid=15981945805,ha;JSESSIONID=0000tTORQr-1aFOoNZrD0imAB4c:153mmogvm;cmtokenid=df6aa040ea284863baef8575a8295098@ha.ac.10086.cn;cmtokenidHeNan=df6aa040ea284863baef8575a8295098@ha.ac.10086.cn;passtype=2;")
						text = cutil.post(url, param);
					}
				}
			}
		}
		return text;
	}

	private boolean saveMonthTel() {
		Map<String, String> param = new HashMap<String, String>();
		try {
			List<String> ms = getMonth(6, "yyyyMM");
			int mm = 1;
			for (String s : ms) {
				String starDate = s;
				param.put("QMonth", s.toString().trim());
				String text = cutil.post(MonthTelUrl,  param);

				if (text.contains("消费(元)：")) {
					RegexPaserUtil rp1 = new RegexPaserUtil(
							"<table width=\"100%\" cellspacing=\"1\" cellpadding=\"0\" class=\"table_bg\" id=\"biaoqian1\" >",
							"</table>", text, RegexPaserUtil.TEXTEGEXANDNRT);
					String tableString = "<table>" + rp1.getText() + "</table>";
					Document doc6 = Jsoup.parse(tableString);
					//System.out.println(doc6);
					Elements tables = doc6.select("table");
					if (tables==null||tables.equals("")) {
						continue;
					}
					Elements trs = tables.get(0).select("tr");
					BigDecimal tcgdf = new BigDecimal(0); // 套餐及固定费
					BigDecimal yytxf = new BigDecimal(0); // 语音通信费
					BigDecimal tcwdxf = new BigDecimal(0); // 套餐外短彩信费
					BigDecimal hj = new BigDecimal(0);
					// trs.size()-2是因为倒数第二个tr中也包含‘合计费用’的信息，出现异常，-2不会对最终结果造成影响，同时少去的对错误字段的遍历，防止异常出现
					for (int i = 1; i < trs.size() - 2; i++) {
						Element tr = trs.get(i);
						String trStr = tr.text();
						if (trStr.contains("套餐及固定费")) {
							// RegexPaserUtil rp = new
							// RegexPaserUtil("<td><strong><b>","</b></strong></td>",trStr,RegexPaserUtil.TEXTEGEXANDNRT);
							String tcgdfs = trStr.replaceAll("套餐及固定费", "")
									.replaceAll("\\s*", "");
							try {
								if (tcgdfs != null) {
									tcgdf = new BigDecimal(tcgdfs);
								}
							} catch (Exception e) {
								//System.out.println("网页文本提取出错！套餐及固定费");
								e.printStackTrace();
							}
						} else if (trStr.contains("话音通信费")) {
							String yytxfs = trStr.replaceAll("话音通信费", "")
									.replaceAll("套餐外费用", "")
									.replaceAll("\\s*", "");
							try {
								if (yytxfs != null) {
									yytxf = new BigDecimal(yytxfs);
								}
							} catch (Exception e) {
								//System.out.println("网页文本提取出错！语音通信费");
								e.printStackTrace();
							}

						} else if (trStr.contains("短彩信费")) {
							String tcwdxfs = trStr.replaceAll("短彩信费", "")
									.replaceAll("\\s*", "");
							try {
								if (tcwdxfs != null) {
									tcwdxf = new BigDecimal(tcwdxfs);
								}
							} catch (Exception e) {
								//System.out.println("网页文本提取出错！短彩信费");
								e.printStackTrace();
							}
						} else if (trStr.contains("合计费用")) {

							String hjs = trStr.replaceAll("合计费用", "")
									.replaceAll("\\s*", "");

							try {
								if (hjs != null) {
									hj = new BigDecimal(hjs);
								}
							} catch (Exception e) {
								//System.out.println("网页文本提取出错！");
								e.printStackTrace();
							}
						}
					}
					Map map2 = new HashMap();
					map2.put("phone", login.getLoginName());
					map2.put("cTime", StringToDate(s, "yyyyMM"));
					List list = mobileTelService.getMobileTelBybc(map2);
					if (list == null || list.size() == 0) {
						MobileTel mobieTel = new MobileTel();
						UUID uuid = UUID.randomUUID();
						mobieTel.setId(uuid.toString());
//						mobieTel.setBaseUserId(currentUser);
						mobieTel.setcTime(StringToDate(s, "yyyyMM"));
						mobieTel.setcName(CUST_NAME);
						mobieTel.setTeleno(login.getLoginName());
						// mobieTel.setBrand(brand);
						mobieTel.setTcgdf(tcgdf);
						// mobieTel.setLdxsf(ldxsf);
						// mobieTel.setMgtjhyf(mgtjhyf);
						String year = s.substring(0, 4);
						String mouth = s.substring(4, 6);
						mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
								Integer.parseInt(year),
								Integer.parseInt(formatDateMouth(mouth)))
								+ "至"
								+ TimeUtil.getLastDayOfMonth(Integer
										.parseInt(year), Integer
										.parseInt(formatDateMouth(mouth))));
						mobieTel.setTcwdxf(tcwdxf);
						mobieTel.setcAllPay(hj);
						mobieTel.setTcwyytxf(yytxf);
						String cd = formatDate(new Date());
						if (cd.equals(starDate)) {
							mobieTel.setIscm(1);
						}
						mobileTelService.saveMobileTel(mobieTel);
					}
				}

				mm++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HENYD_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}

		return true;

	}

	private boolean saveDetailTel() {
		String DetailTelUrl = "https://service.ha.10086.cn/service/self/tel-bill-detail!call.action?type=call&StartDate=";
		try {
			List<String> ms =  getMonth(6,"yyyyMM");
			int x = 0;
			CHeader h = new CHeader("https://service.ha.10086.cn/service/self/tel-bill-detail.action?menuCode=1032");
			h.setAccept_Language("zh-CN");
			h.setCookie("TelBillQuery=yes");
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int days = 0;
			for (String s : ms) {
				days = getDaysOfMonth(s);
				if(month < 10){
					if(s.equals(year+"0"+month)){
						days = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					}
				}else{
					if(s.equals(year+""+month)){
						days = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					}
				}
				String StartDate=s+"01";
				String EndDate=s+days;
				String text = CUtil.getMethodPost(DetailTelUrl+StartDate+"&EndDate="+EndDate+"&FilteredMobileNo=",client,h,null);
				//System.out.println(text);
				if(text.contains("语音详单") && !text.contains("对不起,查询信息不存在!")){
					RegexPaserUtil rp1 = new RegexPaserUtil("<table id=\"sort_table_call\" cellspacing=\"1\" cellpadding=\"0\" border=\"0\" width=\"1004\" class=\"table_bg gray-ft\">","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String tableString = "<table>"+rp1.getText()+"</table>";
					Document doc6 = Jsoup.parse(tableString);
					Elements tables =doc6.select("table");
					Elements trs = tables.get(0).select("tr");
//					BigDecimal tcgdf=new BigDecimal(0);	
//					BigDecimal yytxf=new BigDecimal(0);	
//					BigDecimal hj=new BigDecimal(0);	
					String data_day="";
					for(int i = 1 ; i<trs.size();i++){
						Elements tds = trs.get(i).select("td");
						if(tds.size()>8){
							String thsj = data_day+" "+tds.get(0).text();//起始时间
							String thwz = StringEscapeUtils.unescapeHtml3(tds.get(1).text().replaceAll("\\s*", ""));//通信地点
							String thlx = StringEscapeUtils.unescapeHtml3(tds.get(2).text().replaceAll("\\s*", ""));//通信方式
							String dfhm = tds.get(3).text().replaceAll("\\s*", "");//对方号码
							String thsc = tds.get(4).text().replaceAll("\\s*", "");//通信时长
							String ctlx = StringEscapeUtils.unescapeHtml3(tds.get(5).text().replaceAll("\\s*", ""));//通信类型
//							String mylx = StringEscapeUtils.unescapeHtml3(tds.get(6).text().replaceAll("\\s*", ""));//套餐优惠
//							String wl = StringEscapeUtils.unescapeHtml3(tds.get(7).text().replaceAll("\\s*", ""));//免费资源
							String zfy = tds.get(8).text().replaceAll("元" ,"").replaceAll("\\s*", "");//实收通信费
							
							 Map map2 = new HashMap();
							 map2.put("phone", login.getLoginName());
							 map2.put("cTime", StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
							 List list = mobileDetailService.getMobileDetailBypt(map2);
							if(list==null || list.size()==0){
								MobileDetail mDetail = new MobileDetail();
								UUID uuid = UUID.randomUUID();
								mDetail.setId(uuid.toString());
					        	mDetail.setcTime(StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
					        	mDetail.setTradeAddr(thwz);
					        	
					        	if(thlx.contains("主叫")){
					        		mDetail.setTradeWay("主叫");
					        	}else if(thlx.contains("被叫")){
					        		mDetail.setTradeWay("被叫");
					        	}
					        	int times = 0;
								try{
									TimeUtil tunit = new TimeUtil();
									
									times = tunit.timetoint(removeChinese(thsc));
								}catch(Exception e){
									
								}		
					        	mDetail.setRecevierPhone(dfhm);
					        	mDetail.setTradeTime(times);
					        	mDetail.setTradeType(ctlx);
					        	mDetail.setTaocan("");
					        	mDetail.setOnlinePay(new BigDecimal(zfy));
					        	mDetail.setPhone(login.getLoginName());
					        	if(x==0){
					        		mDetail.setIscm(1);
					        	}
								mobileDetailService.saveMobileDetail(mDetail);
						}
							
						}else {
							data_day = tds.get(0).text();
							//System.out.println(data_day);
									
						}
					
					}
				}
				x++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HENYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}
		return true;
	}
	
	
	private boolean saveMessageHistory() {
		String DetailTelUrl = "https://service.ha.10086.cn/service/self/tel-bill-detail!smsAndmms.action?type=smsAndmms&StartDate=";
		try {
			List<String> ms =  getMonth(6,"yyyyMM");
			CHeader h = new CHeader("https://service.ha.10086.cn/service/self/tel-bill-detail.action?menuCode=1032");
			h.setAccept_Language("zh-CN");
			h.setCookie("TelBillQuery=yes");
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int days = 0;
			
			MobileMessage bean = new MobileMessage();
			bean.setPhone(login.getLoginName());
			bean = mobileMessageService.getMaxSentTime(bean.getPhone());
			for (String s : ms) {
				days = getDaysOfMonth(s);
				if(month < 10){
					if(s.equals(year+"0"+month)){
						days = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					}
				}else{
					if(s.equals(year+""+month)){
						days = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					}
				}
				String StartDate=s+"01";
				String EndDate=s+days;
				String text = CUtil.getMethodPost(DetailTelUrl+StartDate+"&EndDate="+EndDate+"&FilteredMobileNo=",client,h,null);			
				
				if(text.contains("短彩信详单") && !text.contains("对不起,查询信息不存在!")){
					Document doc  = Jsoup.parse(text);
					Elements tables = doc.select("table");
					
					Element table = tables.get(1);
					
					Elements trs = table.select("tr");
					String date = null;
					
					for(int i = 1; i < trs.size(); i++) {
						Elements tds = trs.get(i).select("td");
						if(tds.size() == 1){
							date = tds.get(0).text();
						} 
						else if(tds.size() == 9){
							String time = tds.get(0).text();
							String sendAddr = tds.get(1).text();
							String receivePhone = tds.get(2).text();
							String type = tds.get(3).text();
							String allPay = tds.get(8).text();
														
//							Map map2 = new HashMap();
//							map2.put("phone", login.getLoginName());
//							map2.put("sentTime", StringToDate(date+" "+time, "yyyy-MM-dd HH:mm:ss"));
//							List list = mobileMessageService.getByPhone(map2);
							//MobileMessage mobileMessage = mobileMessageService.getMaxSentTime(login.getLoginName());

//							if(list == null || list.size() == 0){
								MobileMessage mMessage = new MobileMessage();
								UUID uuid = UUID.randomUUID();
								mMessage.setId(uuid.toString());
								mMessage.setCreateTs(new Date());
								mMessage.setAllPay(new BigDecimal(allPay));
								mMessage.setPhone(login.getLoginName());
								mMessage.setRecevierPhone(receivePhone);
								mMessage.setSentAddr(sendAddr);
								mMessage.setSentTime(DateUtils.StringToDate(date+" "+time, "yyyy-MM-dd HH:mm:ss"));
								mMessage.setTradeWay("发送");
								if(bean!=null){
									 if(bean.getSentTime().getTime()>=mMessage.getSentTime().getTime()){
										return false;
									 }
								 }
								mobileMessageService.save(mMessage);
//							} 
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HENYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}
		return true;
	}
	
	
	private boolean saveLiuLiang()
	{
		String DetailTelUrl = "https://service.ha.10086.cn/service/self/tel-bill-detail!flow.action?type=flow&StartDate=";
		try {
			List<String> ms =  getMonth(6,"yyyyMM");
			CHeader h = new CHeader("https://service.ha.10086.cn/service/self/tel-bill-detail.action?menuCode=1032");
			h.setAccept_Language("zh-CN");
			h.setCookie("TelBillQuery=yes");
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int days = 0;
			//流量账单param
			Date monthly = null;
			long totalFlow_b = 0;
			long freeFlow = 0;
			long chargeFlow = 0;
			int iscm = 0;
			BigDecimal trafficCharges = new BigDecimal(0);
			MobileOnlineBill bean_bill = null;
			try {
				bean_bill = mobileOnlineBillService.getMaxTime(login.getLoginName());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int iscm1 = 5;
			//流量账单end
			for (String s : ms) {
				days = getDaysOfMonth(s);
				if(month < 10){
					if(s.equals(year+"0"+month)){
						days = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					}
				}else{
					if(s.equals(year+""+month)){
						days = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					}
				}
				String StartDate=s+"01";
				String EndDate=s+days;
				
				String text = CUtil.getMethodPost(DetailTelUrl+StartDate+"&EndDate="+EndDate+"&FilteredMobileNo=",client,h,null);			
				List<MobileOnlineList> mobileOnlineList = new LinkedList<MobileOnlineList>();
				if(text.contains("上网类") && !text.contains("对不起,查询信息不存在!")){
					Document doc  = Jsoup.parse(text);
					
					//流量账单start
					Elements table_b = doc.select("table[class=table_bg gray-ft al-left]");
					String trs_b = table_b.select("tbody>tr:eq(0)").text();
					RegexPaserUtil rp = new RegexPaserUtil( "收费流量：", "\\(" ,trs_b, RegexPaserUtil.TEXTEGEXANDNRT);
					chargeFlow = Math.round(StringUtil.flowFormat(rp.getText()));
					rp = new RegexPaserUtil( "免费流量：", "\\(" ,trs_b, RegexPaserUtil.TEXTEGEXANDNRT);
					freeFlow = Math.round(StringUtil.flowFormat(rp.getText()));
					rp = new RegexPaserUtil( "总流量：", "\\(" ,trs_b, RegexPaserUtil.TEXTEGEXANDNRT);
					totalFlow_b = Math.round(StringUtil.flowFormat(rp.getText()));
					monthly = DateUtils.StringToDate(s, "yyyyMM");
					//流量账单end
					
					Elements table = doc.select("table[class=table_bg gray-ft]");
					Element body = table.select("tbody").get(0);
					Elements trs = body.select("tr");
					String fixdate = "";					
					for(int i = 0; i < trs.size()-1; i++) {
						Elements tds = trs.get(i).select("td");
						if(tds.size() == 1){
							fixdate = tds.get(0).text();
						} 
						else if(tds.size() == 8){
							String starttime = fixdate+" "+tds.get(0).text().trim();
							String allTotalFee = tds.get(7).text().trim();
							String area = tds.get(1).text().trim();
							String duration = tds.get(3).text();
							String accessPoint = tds.get(2).text().trim();
							String data = tds.get(4).text().trim();
							String cheapService = tds.get(5).text().trim();
							
							long onlinetime=0;
							BigDecimal communicationFees=new BigDecimal("0.0");
							long totalFlow=0;
							Date times=null;
							times=DateUtils.StringToDate( starttime, "yyyy-MM-dd HH:mm:ss");
							try{
								if(!duration.equals(""))
								{
									onlinetime = Long.parseLong(duration.trim());
								}
								if(!allTotalFee.equals(""))
								{
									communicationFees = new BigDecimal(allTotalFee.trim());
									trafficCharges = communicationFees;
								}
								if(!data.equals(""))
								{
								    double tFlow = StringUtil.flowFormat(data);
									totalFlow = (long) tFlow;	
								}
							}
							catch (Exception e) {
								e.printStackTrace();
							}
								MobileOnlineList datalist = new MobileOnlineList();
								UUID uuid = UUID.randomUUID();
								datalist.setId(uuid.toString());
								datalist.setOnlineTime(onlinetime);
								datalist.setPhone(login.getLoginName());
								datalist.setOnlineType(accessPoint);
								datalist.setTotalFlow(totalFlow);
								datalist.setTradeAddr(area);
								datalist.setcTime(times);
								datalist.setCommunicationFees(communicationFees);
								datalist.setCheapService(cheapService);
								if(datalist!=null)
								{
									mobileOnlineList.add(datalist);	
								}
						}
					}//for()
					
					//保存流量账单
					MobileOnlineBill onlineBill = new MobileOnlineBill();
					UUID uuid = UUID.randomUUID();
					onlineBill.setId(uuid.toString());
		        	if(bean_bill!=null){
						 if(bean_bill.getMonthly().getTime()>=onlineBill.getMonthly().getTime()){
						 }else {
						    onlineBill.setMonthly(monthly);
				        	onlineBill.setChargeFlow(chargeFlow);
				        	onlineBill.setFreeFlow(freeFlow);
				        	onlineBill.setTotalFlow(totalFlow_b);
							onlineBill.setTrafficCharges(trafficCharges);
				        	onlineBill.setPhone(login.getLoginName());    
				        	if(iscm1==5) {
				        		onlineBill.setIscm(1);
				        	}else {
				        		onlineBill.setIscm(0);
				        	}
							mobileOnlineBillService.save(onlineBill);
						 }
					}else {
		        	onlineBill.setMonthly(monthly);
		        	onlineBill.setChargeFlow(chargeFlow);
		        	onlineBill.setFreeFlow(freeFlow);
		        	onlineBill.setTotalFlow(totalFlow_b);
					onlineBill.setTrafficCharges(trafficCharges);
		        	onlineBill.setPhone(login.getLoginName());    
		        	if(iscm1==5) {
		        		onlineBill.setIscm(1);
		        	}else {
		        		onlineBill.setIscm(0);
		        	}
		        	onlineBill.setIscm(iscm);
					mobileOnlineBillService.save(onlineBill);
				  }
				}//if(text)
				
				try
				{
					if(mobileOnlineList!=null&&mobileOnlineList.size()>0)
					{
						mobileOnlineListService.insertbatch(mobileOnlineList);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				iscm1 --;//判断是否当月
			}//for()6个月
			
			
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HENYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
			return false;
		}
		return true;
	}

	
	
	private boolean saveYuE() {
		try {
			String loginName = login.getLoginName();
			Map<String, String> parmap = new HashMap<String, String>(3);
			parmap.put("parentId", currentUser);
			parmap.put("loginName", loginName);
			parmap.put("usersource", Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(parmap);
			
			CHeader h = new CHeader();
			h.setAccept(CHeaderUtil.Accept_);
			h.setAccept_Language("zh-CN");
			h.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/7.0)");
			// 解压报文出错！
			// h.setAccept_Encoding("gzip, deflate");
			h.setHost("service.ha.10086.cn");
			h.setConnection("Keep-Alive");
			h.setCache_Control("no-cache");//h.setCookie("CmWebNumSn=14791405282,sn; WT_FPC=id=21a9cbe5ed1115cb7521407396124467:lv=1407564547486:ss=1407564547481; flagcity=0000;CmProvid=ha;CmWebtokenid=15981945805,ha;JSESSIONID=0000tTORQr-1aFOoNZrD0imAB4c:153mmogvm;cmtokenid=df6aa040ea284863baef8575a8295098@ha.ac.10086.cn;cmtokenidHeNan=df6aa040ea284863baef8575a8295098@ha.ac.10086.cn;passtype=2;")
			String text = cutil.get("http://service.ha.10086.cn/service/self/customer-info-uphold.action?menuCode=1140",h);
			
			Document doc = Jsoup.parse(text);
			
			Elements els = doc.select("div[class=customer_info]");
			Elements dls = els.select("dl");
			Element dl = dls.get(1);
			String addr = dl.select("dd").get(1).text();
			String idcard = dl.select("dd").get(2).text();
			
			if (list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(loginName);
				user.setIdcard(idcard);
				user.setAddr(addr);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setEmail("");
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());

				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);

				user.setRealName(loginName);
				user.setIdcard(idcard);
				user.setAddr(addr);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setEmail("");
				userService.saveUser(user);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HENYD_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

		return false;
	}

	private List<String> getMonth(int num, String format) {
		List<String> objectTmp = new ArrayList<String>();
		java.text.DateFormat format2 = new java.text.SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		for (int i = 0; i < num; i++) {
			c.add(Calendar.MONTH, -1);
			Date date = c.getTime();
			String date2 = format2.format(date);
			//System.out.println(date2);
			objectTmp.add(date2);
		}
		return objectTmp;
	}
	private int getDaysOfMonth(String s){
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMM"); //如果写成年月日的形式的话，要写小d，如："yyyy/MM/dd"
		try {
		rightNow.setTime(simpleDate.parse(s)); //要计算你想要的月份，改变这里即可
		} catch (ParseException e) {
		e.printStackTrace();
		}
		int days = rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}
	private Date StringToDate(String dateStr, String formatStr) {
		DateFormat dd = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = dd.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
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

	private String formatDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(d);
	}

	private BigDecimal getYuE() {
		BigDecimal phoneremain = new BigDecimal("0.00");
		CHeader h = new CHeader();
		h.setAccept(CHeaderUtil.Accept_);
		h.setAccept_Language("zh-CN");
		h.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/7.0)");
		// 解压报文出错！
		// h.setAccept_Encoding("gzip, deflate");
		h.setHost("service.ha.10086.cn");
		h.setConnection("Keep-Alive");
		h.setCache_Control("no-cache");//h.setCookie("CmWebNumSn=14791405282,sn; WT_FPC=id=21a9cbe5ed1115cb7521407396124467:lv=1407564547486:ss=1407564547481; flagcity=0000;CmProvid=ha;CmWebtokenid=15981945805,ha;JSESSIONID=0000tTORQr-1aFOoNZrD0imAB4c:153mmogvm;cmtokenid=df6aa040ea284863baef8575a8295098@ha.ac.10086.cn;cmtokenidHeNan=df6aa040ea284863baef8575a8295098@ha.ac.10086.cn;passtype=2;")
		String text = cutil.get("http://service.ha.10086.cn/service/self/new-query-total.action?menuCode=1025",h);
		//System.out.println(text);
		try {
			if (text != null && text.contains("话费余额")) {
				RegexPaserUtil rp = new RegexPaserUtil(
						"可用余额：</em><span class=\"em_right\"><span class=\"f18 cyel fB\">",
						"</span>元", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String yue = rp.getText();
				if (yue != null) {
					//有非数字时出错如 欠4.2元
					if (yue.contains("欠")) {
						yue=yue.replaceAll("欠", "").trim();
					}
					phoneremain = new BigDecimal(yue.replaceAll("\\s*", ""));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phoneremain;
	}

	private String login_parseErrorText(String text ) {
		if(text.length()>300){
			RegexPaserUtil rp = new RegexPaserUtil(
					" id=\"YZM_Fm\" style=\"margin-bottom: 10px", "\"", text,
					RegexPaserUtil.TEXTEGEXANDNRT);
			String text1 = rp.getText();
			if (text1 != null && !text1.contains("display: block;")) {
				// 删除redis
				clearRedis();// 自动清空redismap数据; 此处的解决方案
				text = null;//重新执行一次登陆
			} else {
				rp = new RegexPaserUtil(
						" function checkMessage()(.*?)var errorCode = ", ";", text,
						RegexPaserUtil.TEXTEGEXANDNRT);
				text = rp.getText();
				errorMsg =getErrorText(text);
			}
		}
		return text;
	}

	/**
	 * 在第一次登录过程中返回的错误代码
	 * 
	 * @param codeValue
	 * @return
	 */
	private String getErrorText(String codeValue) {
		if (codeValue != null) {
			codeValue = codeValue.trim();
			if (codeValue.contains("4001")) {
				return "用户名或动态密码不正确，请重新输入。";
			} else if (codeValue.contains("8001")) {
				return "尊敬的客户，您已成功登录，不能重复登录。";
			} else if (codeValue.contains("5003")) {
				return "手机号码登录权限已被锁，24小时后将自动解锁。";
			} else if (codeValue.contains("-75")) {
				return "验证码错误，请您检查后重新输入！";
			} else if (codeValue.contains("5002")) {
				return "很抱歉，外省号码不能登录河南移动!";
			} else if (codeValue.contains("9002")) {
				return "很抱歉，非移动号码不能登录河南移动！";
			} else {
				return "系统繁忙，请重试";
			}
		}
		return null;
	}
	/**
	 * 移除html报文数据中的汉字
	 * 
	 * @param codeValue
	 * @return
	 */
	private String removeChinese(String result){
		if (result.getBytes().length<2) {
			result = "0";
		}else if (result.getBytes().length!=result.length()) {
			result = result.substring(0, result.getBytes().length-2);
		}
		return result;
	}
	public void testForDetail(){
			/*String url = "https://service.ha.10086.cn/service/self/tel-bill!detail.action?menuCode=1026";
			CHeader h1 = new CHeader("https://service.ha.10086.cn/service/self/tel-bill.action");
			String text = CUtil.getMethodGet(url, client, h1);
			System.out.println(text);*/
			String DetailTelUrl = "https://service.ha.10086.cn/service/self/tel-bill-detail!call.action?type=call&StartDate=";
			CHeader h2 = new CHeader("https://service.ha.10086.cn/service/self/tel-bill-detail.action?menuCode=1032");
			h2.setCookie("TelBillQuery=yes");
			//System.out.println("11");
			String s ="201407";
			int days= getDaysOfMonth(s);
			String StartDate=s+"01";
			String EndDate=s+days;
			String text = CUtil.getMethodPost(DetailTelUrl+StartDate+"&EndDate="+EndDate+"&FilteredMobileNo=",client,h2,null);
			//System.out.println(text);
	}
	public static void main(String[] args) {
//		Login login = new Login("15981945805", "899999");
//		HENYidong hn = new HENYidong(login);
//		// 登陆
//		hn.login(); 
//		//hn.sendMsg();
//		System.out.print("验证码为：");
//		Scanner in = new Scanner(System.in);
//		
//		String authcode = in.nextLine();
//		//hn.msgLogin();
//		hn.testForDetail();
//		hn.close();
//		Login login = new Login("15981945805", "899999");
//		HENYidong hn = new HENYidong(login);
//		List<String> ms =  hn.getMonth(6,"yyyyMM");
//		for (int i = 0; i < ms.size(); i++) {
//			System.out.println(ms.get(i));;
//		}
	}

}
