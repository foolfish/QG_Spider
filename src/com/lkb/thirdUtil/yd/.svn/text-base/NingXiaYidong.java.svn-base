package com.lkb.thirdUtil.yd;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.ethz.ssh2.crypto.Base64;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.constant.Constant;
import com.lkb.thirdUtil.AbstractCrawler;
import com.lkb.thirdUtil.base.BasicCommonMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.entity.CHeader;


/**
 * 宁夏移动
 * @date 2014-9-12 
 */
public class NingXiaYidong extends BasicCommonMobile{//BaseInfoMobile {
	private static final Logger log = Logger.getLogger(NingXiaYidong.class);
	public static String imgurl = "https://ha.ac.10086.cn/checkImage";
	public static String loginMapKey = "NingXiaYidong.loginMap.Key";
	public static String pageInitKey = "NingXiaYidong.pageInit.Key";
	public static String yueKey = "NingXiaYidong.yue.Key";
	public static String phoneCodeKey = "NingXiaYidong.phoneCode.Key";
	
	public String commonParam = "/fee/query/queryDetailBill.do";

	public NingXiaYidong(Login login,String currentUser) {
		super(login, currentUser);
		this.userSource = Constant.YIDONG;
		this.constantNum = "NINGXIA_YIDONG";
	}
	
	public static void main(String[] args) throws Exception {
		NingXiaYidong nx = new NingXiaYidong(new Login("15009579295","809713"),null);
//		NingXiaYidong nx = new NingXiaYidong(new Login("15009579296","809713"),null);
//		nx.index();
//		nx.logins();
		if(nx.islogin()){
//			nx.gatherMonthlyBill();
//			nx.sendPhoneDynamicsCode(new ResOutput());
//			nx.getLogin().setPhoneCode(new Scanner(System.in).nextLine());
//			nx.checkPhoneDynamicsCode();
//			
//			nx.gatherCallHistory();
//			nx.gatherMessage();
			nx.gatherFlowList();
			
		}
		
//		
//		User user = nx.getUser();
//		nx.spiderCurrentMonth();
//		nx.gatherMonthlyBill();
		
	}
	
	public void init(){
		if(!context.isInit()){
			String text = cutil.get("https://nx.ac.10086.cn/login");
				if(text!=null){
//					System.out.println(text);
					Document doc =Jsoup.parse(text);
					Element el = doc.getElementById("oldLogin");
					Map<String,String> map = new HashMap<String,String>();
					map.put("spid",el.select("input[name=spid]").val());
					map.put("backurl",el.select("input[name=backurl]").val());
					map.put("errorurl",el.select("input[name=errorurl]").val());
					map.put("loginType",el.select("input[name=loginType]").val());
					map.put("RelayState",el.select("input[name=RelayState]").val());
					map.put("validCode","0000");
					map.put("website_pwd_email","");
					map.put("ssoPwd","");
					map.put("emailPwd","");
					map.put("smsValidCode","");
					//处理A、B、C不同类型的登录显示相应的登录信息（主要是登录模式）
					//A短信验证码,C,服务密码+短信验证码
					map.put("type","B");
					redismap.put(loginMapKey, map);
					RegexPaserUtil rp = new RegexPaserUtil("DES.init\\(\"", "\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
					redismap.put(pageInitKey, rp.getText());
					context.setInit();
				}
		}
	}
	public void login() {
			Map<String, String> param = (Map<String, String>) redismap.get(loginMapKey);
			param.put("mobileNum",login.getLoginName());
			param.put("userIdTemp",login.getLoginName());
//			
//			String password = AbstractCrawler.executeJsFunc("rsa/he_nan_yd_rsa.js", "encryptionStr",redismap.get(pageInitKey).toString(),login.getPassword());
			String password = AbstractCrawler.executeJsFunc("yd/ningxia_yd_des.js", "encryptionStr",redismap.get(pageInitKey).toString(),login.getPassword());
			if(StringUtils.isNotBlank(password)){
				param.put("servicePassword1",login.getPassword());
				param.put("servicePassword",password);
				String text = cutil.post("https://nx.ac.10086.cn/Login", param);
//				System.out.println(text);
				Document doc = null;
				String RelayState = param.get("RelayState");
				if(text!=null&&text.length()>150){
					doc = Jsoup.parse(text);
					param = new HashMap<String, String>();
					param.put("SAMLart", doc.select("input[name=SAMLart]").val());
					param.put("isEncodePassword", doc.select("input[name=isEncodePassword]").val());
					param.put("displayPic", doc.select("input[name=displayPic]").val());
					param.put("RelayState", doc.select("input[name=RelayState]").val());
					param.put("displayPics", doc.select("input[name=displayPics]").val());
					String uri = doc.select("form[name=postartifact]").attr("action");
					text = cutil.post("https://nx.ac.10086.cn/"+uri,param);
					//System.out.println(text);
					RegexPaserUtil rp = new RegexPaserUtil("parent.callBackurl\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
					text = cutil.get("http://www.nx.10086.cn/service/index.jsp?SAMLart="+rp.getText()+"&RelayState="+RelayState);
					text = cutil.get("http://www.nx.10086.cn/service/right.jsp?94.262327191368");
					rp = new RegexPaserUtil("class=\"tabbord\">\\￥","</th>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String yue  =  rp.getText();
					if(yue!=null&&yue.length()>0){
						redismap.put(yueKey,yue);
						loginsuccess();
						addTask(1);
						sendPhoneDynamicsCode();
						//System.out.println("登陆成功!");
					}
				}else{
					text = cutil.get(text);
					System.out.println(text);
					RegexPaserUtil rp = new RegexPaserUtil("parent.form_Error\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
					output.setErrorMsg(rp.getText());
				}
			}
	}
	public void sendPhoneDynamicsCode(ResOutput res){
		String url = "http://www.nx.10086.cn/service/systems/sendSmsCode.do";
		Map<String,String> param = new LinkedHashMap<String,String>();
		try {
			param.put("u", new String(Base64.encode(commonParam.getBytes("utf-8"))));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String text = cutil.post(url, param);
		RegexPaserUtil rp = new RegexPaserUtil("CDATA\\[","\\]",text,RegexPaserUtil.TEXTEGEXANDNRT);
		if(text.contains("短信验证码发送成功")){
			res.setErrorMsg(rp.getText());
			res.setStatus(1);
		}else{
			res.setErrorMsg("发送失败,请重试!");
		}
	}

	public void  checkPhoneDynamicsCode(ResOutput res){
		String url = "http://www.nx.10086.cn/service/systems/vaildateSmsCode.do	";
		Map<String, String> param = new HashMap<String, String>();
		
		try {
			param.put("u", new String(Base64.encode(commonParam.getBytes("utf-8"))));
			param.put("v", new String(Base64.encode(login.getPhoneCode().getBytes("utf-8"))));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String text = cutil.post(url,param);
		if (text.contains("验证成功")) {
			res.setStatus(1);
			res.setErrorMsg("短信验证码验证成功！");
			redismap.put(phoneCodeKey, login.getPhoneCode());
			addTask(2);
		}else {
			res.setErrorMsg("您输入的短信验证码不正确,请核实后重新输入！");
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
				saveTel();
				break;
			case 2:
				saveCallHistory();
				saveMessage();
				saveFlowList();
				saveFlowBill();
				break;
//			case 3:
//				getUser(); //个人信息
//				saveTel();
//				saveCallHistory(); //通话记录	
//				saveMessage();
//				//查询短信记录
//				break;
			default:
				break;
			}
	}
	
	
	public BigDecimal getYue(){
		try{
			return new BigDecimal(redismap.get(yueKey).toString());
		}catch(Exception e){
			log.error(this.loginName+":话费解析异常");
		}
		return new BigDecimal(0.00);
	}
	public String getBasicPackage(){
		String text = cutil.get("https://www.nx.10086.cn/service/business/package/current.do");
		String s = "";
		try{
			Document doc = Jsoup.parse(text);
			s = doc.getElementById("currentMsg").select("td").get(0).text();
		}catch(Exception e){
		}
		return s;
	}

	public User gatherUser() {
			String myinfo = cutil.get("https://www.nx.10086.cn/service/infos/queryDetail.do");
			Document doc3 = Jsoup.parse(myinfo);
			Element table =doc3.getElementById("userinfo");
			if(table!=null){
				table = table.select("tbody").get(0);
				User user = new User();
				user.setRealName(table.select("td").get(1).text());
				user.setUserName(user.getRealName());
				user.setAddr(table.select("td").get(3).text());
			
				String isIdCard = table.select("td").get(11).text();
				if(isIdCard!=null&&isIdCard.contains("身份证")){
					user.setIdcard(table.select("td").get(13).text());
				}
				try{
					user.setRegisterDate(DateUtils.StringToDate(table.select("td").get(15).text().trim(), "yyyy-MM-dd HH:mm:ss"));
				}catch(Exception e){
					log.warn(this.loginName+"时间格式错误",e);
				}
				user.setCardstatus(table.select("td").get(17).text());
				user.setPhoneRemain(getYue());
				user.setPackageName(getBasicPackage());
				return user;
			}
		return null;
	}
	
	/**
	 * 月度账单
	 */
	public LinkedList<MonthlyBillMark>  gatherMonthlyBill() {
//		LinkedList<SpeakBillPojo> linkedList = getMonthlyBillAccess("http://www.nx.10086.cn/service/fee/query/queryMonthBill.do","start_month");
		 LinkedList<MonthlyBillMark> listMark = getSpiderMonthsMark(true, "yyyyMM",6, 0);
				 
		 for (int i = 0; i < listMark.size(); i++) {
			 //当月的可用信息比较少,所以暂且不采集了
			 if(i==0){
				 listMark.get(i).setText(cutil.get(allMonthUrl));
			 }else{
				 //http://www.nx.10086.cn/service/fee/query/queryMonthBill.do 当月账单
				 listMark.get(i).setText(cutil.get(allMonthUrl+"?start_month="+listMark.get(i).getMonth())); 
			 }
			
		}
		RegexPaserUtil rp = null;
		String xi = null;
		String text = null;
		MonthlyBillMark pojo = null;
		for (int i = 0; i < listMark.size(); i++) {
			pojo = listMark.get(i);
			text = pojo.getText();
			if(text!=null){
				try{
					MobileTel mobileTel= new MobileTel();
					String name = new RegexPaserUtil("客户名称", "</tr>",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
					String dependCycle = null;
					if(name!=null){
						name = name.replaceAll("<.*?>", "").trim();
						mobileTel.setcName(name);
						dependCycle = new RegexPaserUtil("计费周期", "</tr>",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
						if(dependCycle!=null){
							dependCycle = dependCycle.replaceAll("<.*?>", "").trim();
							mobileTel.setDependCycle(dependCycle);
						}
					}
//					}else{
//						log4j.writeLogByZhangdan(new Exception("查询不到本月账单,月份:"+pojo.getMonth()));
//					}
//					System.out.println(text);
					if(i==0){
						 String allPay = new RegexPaserUtil("消费总额", "(\\)|</tr>)",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
						 if(allPay!=null){
							 allPay = allPay.replaceAll("<.*?>|\\(", "").trim();
							 mobileTel.setcAllPay(new BigDecimal(allPay));
						 }
						 pojo.setObj(mobileTel);
						pojo.setText(null);
					}else{
						rp = new RegexPaserUtil("本期消费</strong></td>                                  <td>", "</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
						xi = rp.getText();
						if(xi!=null){
							mobileTel.setcAllPay(new BigDecimal(xi.trim()));
							rp = new RegexPaserUtil("增值业务费</strong></td>	                                <td>", "</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
							xi = rp.getText();
							if(xi!=null){
								mobileTel.setZzywf(new BigDecimal(xi.trim()));
							}
							rp = new RegexPaserUtil("其它费</strong></td>	                                <td>", "</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
							xi = rp.getText();
							if(xi!=null){
								mobileTel.setTcgdf(new BigDecimal(xi.trim()));
							}
							rp = new RegexPaserUtil("套餐外语音通信费</strong></td>	                                <td>", "</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
							xi = rp.getText();
							if(xi!=null){
								mobileTel.setTcwyytxf(new BigDecimal(xi.trim()));
							}
							rp = new RegexPaserUtil("个人代付</strong></td>	                                <td>", "</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
							xi = rp.getText();
							if(xi!=null){
								mobileTel.setTryf(new BigDecimal(xi.trim()));
							}
							pojo.setObj(mobileTel);
							pojo.setText(null);
						}else{
							log4j.writeLogByZhangdan(new Exception("查询不到该月的账单,月份:"+pojo.getMonth()));
						}
					}
				} catch (Exception e) {
					log4j.writeLogByZhangdan(e);
				}
			}
		
		}
		return listMark;
	}
	
	/**
	 * 查询通话记录
	 */
	public LinkedList<MobileDetail> gatherCallHistory(){
		List<String> ms = DateUtils.getMonths(6,"yyyyMM");
		Date bigTime = getMaxCallTime();
		LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,allDetailUrl+"?cdrType=2&start_month=" );
		LinkedList<MobileDetail>  listData = new LinkedList<MobileDetail>();
		String text = null;
		Document doc = null;
		Elements els = null;
		MobileDetail md = null;
		for (int i = linkedList.size()-1; i >=0 ; i--) {
			text = linkedList.get(i);
			 try{
				 if(text!=null){
					 doc = Jsoup.parse(text);
//					 System.out.println(doc);
					 if(text.contains("主叫")||text.contains("被叫")){
						 els = doc.select("table[class=my_table1 ser_table mb10 mt10]").get(2).select("tr");
						 for (int j = 1; j < els.size(); j++) {
							 md = new MobileDetail();
							md.setcTime(DateUtils.StringToDate( els.get(j).select("td").get(1).text(), "yyyy-MM-dd HH:mm:ss"));
							if(isContinue(bigTime, md.getcTime())){
								continue;
							}
							md.setTradeAddr(els.get(j).select("td").get(2).text());
							md.setTradeWay(els.get(j).select("td").get(3).text());
							md.setRecevierPhone(els.get(j).select("td").get(4).text());
							try{
								TimeUtil tunit = new TimeUtil();
								md.setTradeTime(tunit.timetoint(els.get(j).select("td").get(5).text()));	
							}catch(Exception e){
							}
							md.setTradeType(els.get(j).select("td").get(6).text());
							md.setTaocan(els.get(j).select("td").get(7).text());
							try{
								md.setOnlinePay(new BigDecimal(els.get(j).select("td").get(8).text().replace("元","")));
							}catch(Exception e){}
							md.setId(UUID.randomUUID().toString());
							md.setPhone(this.loginName);
							listData.add(md);
						 }
					 }
				 }
			} catch (Exception e) {
				log4j.writeLogByHistory(e);
			}
		}
		return listData;
	}
	public LinkedList<String> gatherSpiderStrings(List<String> ms,Date bigTime,String url){
		LinkedList<String> list  = new LinkedList<String>();
		Date d = null;
		 CHeader h = new CHeader();
//		 h.setCookie("WT_FPC=id=2a6f2d5ec730561fe981413181071815:lv=1413181182772:ss=1413181071815; CmProvid=sh; CmWebtokenid=\"13681633227,sh\"; CmActokenid=7d46baa2b2cf4875922eb7c06d2c355d; WEBTRENDS_ID=40.40.40.214-1777055584.30402221; yRJfzS9NAZ=MDAwM2IyYzg3OTAwMDAwMDAwMzcwfUVpfQUxNDEzMTg3MDA0; jsessionid_141_p3=8284e60f514f51d49dab96510b69; oodZQ7MJgF=MDAwM2IyYzg3OTAwMDAwMDAwMzgwDhQqEAsxNDEzMTg3MDU1; __g_u=40918406203631_1_0.2_1_5_1413613086995_0; __g_c=c%3A40918406203631%7Cd%3A1%7Ca%3A1%7Cb%3A2%7Ce%3A0.2%7Cf%3A1%7Ch%3A0; Q_fiveBalance_new3|1089008159=true");
		for (int i = 0; i < ms.size(); i++) {
			if(i!=0){
				//如果当前最大时间比上一次采集的月份还要大,就终止,因为可以理解为后边已经采集了
				d = DateUtils.StringToDate(ms.get(i-1)+"01", "yyyyMMdd");
				if(bigTime!=null&&bigTime.getTime()>=d.getTime()){
					break;
				}
			}
			list.add(cutil.get(url+ms.get(i),h));
		}
		//集合线程装载的结果为从大到小,解析时转换
		return list;
	}
/**
 * 查询短信记录
 */
public LinkedList<MobileMessage> gatherMessage(){
	List<String> ms = DateUtils.getMonths(6,"yyyyMM");
	Date bigTime = getMaxMessageTime();
	LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,allDetailUrl+"?cdrType=3&start_month=" );
	LinkedList<MobileMessage>  listData = new LinkedList<MobileMessage>();
	String text = null;
	Document doc = null;
	Elements els = null;
	MobileMessage md = null;
	for (int i = linkedList.size()-1; i >=0 ; i--) {
		text = linkedList.get(i);
		try{
			if(text!=null){
				doc = Jsoup.parse(text);
//				System.out.println(doc);
				if(text.contains("发送")||text.contains("接收")){
					els = doc.select("table[class=my_table1 ser_table mb10 mt10]").get(2).select("tr");
					for (int j = 1; j < els.size(); j++) {
						md = new MobileMessage();
//						System.out.println( els.get(j));
						md.setSentTime(DateUtils.StringToDate( els.get(j).select("td").get(1).text(), "yyyy-MM-dd HH:mm:ss"));
						if(isContinue(bigTime, md.getSentTime())){
							continue;
						}
						md.setSentAddr(els.get(j).select("td").get(2).text());
						md.setRecevierPhone(els.get(j).select("td").get(4).text());
						md.setTradeWay(els.get(j).select("td").get(5).text());
						md.setAllPay(new BigDecimal(els.get(j).select("td").get(8).text().replace("元", "")));
						md.setId(UUID.randomUUID().toString());
						md.setPhone(this.loginName);
						md.setCreateTs(new Date());
						listData.add(md);
					}
				}
			}
		} catch (Exception e) {
			log4j.writeLogByMessage(e);
		}
	}
	return listData;
}
private static String allMonthUrl = "http://www.nx.10086.cn/service/fee/query/queryMonthBill.do";
private static String allDetailUrl = "http://www.nx.10086.cn/service/fee/query/queryDetailBill.do";

private LinkedList<MonthlyBillMark> billMark = null;
@Override
public LinkedList<MobileOnlineList> gatherFlowList() throws Exception {
//	List<String> ms = DateUtils.getMonths(6,"yyyyMM");
	Date bigTime = getMaxFlowTime();
//	LinkedList<String> linkedList = gatherSpiderStrings(ms,bigTime,"http://www.nx.10086.cn/service/fee/query/queryDetailBill.do?cdrType=4&start_month=" );
	
	billMark = getSpiderMonthsMark(1);
	 for (int i = 0; i < billMark.size(); i++) {
		 if(i==0){
			 billMark.get(i).setText(cutil.get(allDetailUrl+"?cdrType=4"));
		 }else{
			 //http://www.nx.10086.cn/service/fee/query/queryMonthBill.do 当月账单
			 billMark.get(i).setText(cutil.get(allDetailUrl+"?cdrType=4&start_month="+ billMark.get(i).getMonth())); 
		 }
	}
	MonthlyBillMark pojo = null;
	String text = null;
	LinkedList<MobileOnlineList>  listData = new LinkedList<MobileOnlineList>();
	Document doc = null;
	Elements els = null;
	MobileOnlineList md = null;
	for (int i = billMark.size()-1; i >=0; i--) {
		pojo = billMark.get(i);
		text = pojo.getText();
		try{
			if(text!=null){
				doc = Jsoup.parse(text);
				els = doc.select("table[class=my_table1 ser_table mb10 mt10]").get(1).select("tr");
				
				MobileOnlineBill bill = new MobileOnlineBill();
				try{
					if(els.toString().contains("总流量")){
						bill.setChargeFlow(Math.round(StringUtil.flowFormat(els.select("td").get(1).text().replace("收费流量", ""))));
						bill.setFreeFlow(Math.round(StringUtil.flowFormat(els.select("td").get(2).text().replace("免费流量",""))));
						bill.setTotalFlow(Math.round(StringUtil.flowFormat(els.select("td").get(3).text().replace("-","").replace("总流量",""))));
						bill.setTrafficCharges(new BigDecimal(doc.select("tfoot").get(0).select("th").get(2).text().replace("元", "")));
						pojo.setObj(bill);
						
						els = doc.select("table[class=my_table1 ser_table mb10 mt10]").get(2).select("tr");
						if(els.toString().contains("元")){
							for (int j = 1; j < els.size()-1; j++) {
								md = new MobileOnlineList();
								try{
								md.setcTime(DateUtils.StringToDate( els.get(j).select("td").get(1).text(), "yyyy-MM-dd HH:mm:ss"));
								if(isContinue(bigTime, md.getcTime())){
									continue;
								}
								md.setTradeAddr(els.get(j).select("td").get(2).text());
								md.setOnlineType(els.get(j).select("td").get(3).text());
								md.setOnlineTime(StringUtil.flowTimeFormat(els.get(j).select("td").get(4).text()));
								md.setTotalFlow(Math.round(StringUtil.flowFormat(els.get(j).select("td").get(5).text())));
								md.setCheapService(els.get(j).select("td").get(6).text());
								md.setCommunicationFees(new BigDecimal(els.get(j).select("td").get(8).text().replace("元","")));
								md.setId(UUID.randomUUID().toString());
								md.setPhone(this.loginName);
								listData.add(md);
								}catch(Exception e){
									log4j.writeLogByFlowList(e);
									
								}
							}
						}
						
					}
				}catch(Exception e){
					log4j.writeLogByFlowBill(e);
				}
			}
		} catch (Exception e) {
			log4j.writeLogByFlowList(e);
		}
	}
	return listData;
}

@Override
public LinkedList<MonthlyBillMark> gatherFlowBill() {
	return billMark;
}
}
