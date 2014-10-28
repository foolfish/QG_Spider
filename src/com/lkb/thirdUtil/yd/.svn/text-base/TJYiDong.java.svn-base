package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
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
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.robot.util.ContextUtil;
import com.lkb.thirdUtil.base.BasicCommonMobile;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;


public class TJYiDong  extends BasicCommonMobile{//BaseInfoMobile{


	
	public String index = "https://tj.ac.10086.cn/login/";
	// 验证码图片路径
	public static String imgurl = "https://tj.ac.10086.cn/CheckCodeImage?id=";
	
	public static void main(String[] args) throws Exception {
		TJYiDong tj = new TJYiDong(new Login("18222781153","621217"), null);
		tj.index();
		tj.inputCode(imgurl);
		tj.logins();
		if(tj.islogin()){
//			tj.test();
			tj.gatherMonthlyBill();
		}
//		tj.messageHistory();
//	  
//		System.out.println(URLDecoder.decode("EFFICACY_CODE1=jnba&Form1=EFFICACY_CODE1%2CVERIFY_CODE%2Cbforgot&SMS_NUMBER=331980&VERIFY_CODE=jNBa&bforgot=%E7%A1%AE++%E8%AE%A4&mpageName=feequery.VoiceQuery&service=direct%2F1%2Ffeequery.VoiceQuery%2F%24Form&sp=S1&Form1=&MONTH=201409&BILL_TYPE=1&CALL_TYPE=+&ROAM_TYPE=+&LONG_TYPE=+&pagination_inputPage_95617202=&pagination_linkType=&pagination_iPage="));
	}
	
	public void test(){
		List<String> ms = DateUtils.getMonths(6, "yyyyMM");
		for (String startDate : ms) {
//			String url  = "http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.4430221446";
//			Map<String,String> map = new HashMap<String, String>();
//			map.put("EFFICACY_CODE1", "jnba");
//			map.put("Form1","EFFICACY_CODE1,VERIFY_CODE,bforgot");
//			map.put("SMS_NUMBER", "331980");
//			map.put("VERIFY_CODE", "jNBa");
//			map.put("bforgot", "确  认");
//			map.put("mpageName", "feequery.VoiceQuery");
//			map.put("service", "direct/1/feequery.VoiceQuery/$Form&sp=S1");
//			map.put("MONTH", startDate);
//			map.put("BILL_TYPE", "1");
//			map.put("CALL_TYPE", " ");
//			map.put("ROAM_TYPE", " ");
//			map.put("LONG_TYPE", " ");
//			map.put("pagination_inputPage_95617202", "");
//			map.put("pagination_linkType", "");
//			map.put("pagination_iPage", "");
//			String text = cutil.post(url, map);
			String text = cutil.get("http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.7537115738820983&EFFICACY_CODE1=61xy&Form1=EFFICACY_CODE1%2CVERIFY_CODE%2Cbforgot&SMS_NUMBER=355102&VERIFY_CODE=61XY&bforgot=%E7%A1%AE++%E8%AE%A4&mpageName=feequery.VoiceQuery&service=direct%2F1%2Ffeequery.VoiceQuery%2F%24Form&sp=S1&Form1=&MONTH="+startDate+"&BILL_TYPE=0&CALL_TYPE=+&ROAM_TYPE=+&LONG_TYPE=+");
			System.out.println(StringEscapeUtils.unescapeHtml3(text));
			String pageStr = new RegexPaserUtil("class=\"PageLeft\".*?第1/","页").reset(text).getText();
			System.out.println(pageStr);
		}
	}
	public TJYiDong(Login login,String currentUser) {
		super(login,currentUser);
		this.userSource = Constant.YIDONG;
		this.constantNum = ConstantNum.comm_tj_yidong;
	}
	
	public void init(){
		if(!context.isInit()){
			String text = cutil.get(index);
			if(text!=null){
				 context.setImgUrl(imgurl);
				 context.setInit();
			}
		}
	}
	
	// 首页登录
	public void login() {
		String result = login1();
		if(result!=null&& result.equals("1")){
			loginsuccess();
		}else{
			output.setErrorMsg("密码或验证码错误");
		}
		if(islogin()){
        	addTask(1);
    	}
	}
	
	
	
	public  String login1(){
		String url = "https://tj.ac.10086.cn/login/loginHandlerV2.jsp";
		CHeader h = new CHeader(CHeaderUtil.Accept_,"	https://tj.ac.10086.cn/login/",CHeaderUtil.Content_Type__urlencoded,"tj.ac.10086.cn",true);
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("RelayState", "MyHome"); 
		param.put("checkCode",login.getAuthcode()); 
		param.put("issuer", "http://service.tj.10086.cn"); 
		param.put("loginType", "phoneNo"); 
		param.put("mp", login.getLoginName()); 
		param.put("password", login.getPassword()); 
		param.put("passwordType", "service"); 
		param.put("smsPwd", ""); 
		param.put("type1", "index"); 
		
		String text =cutil.post(url, h, param);
		if(text!=null&& text.contains("http://service.tj.10086.cn/artifactServletRev")){
	    	RegexPaserUtil rp = new RegexPaserUtil("parent.location.href='","&BrowserVersion",text,RegexPaserUtil.TEXTEGEXANDNRT);
	    	String url1 = rp.getText()+"&BrowserVersion=&BrowserVersion=Mozilla/5.0%20(Windows%20NT%206.1;%20WOW64;%20Trident/7.0;%20rv:11.0)%20like%20Gecko&ip=";
	    		//System.out.println(url1);
	    	return login2(url1);
	    }
	    	
	    
	    return null;
	}
	
	
	public String login2(String location){
		CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"service.tj.10086.cn",true);
		String text =cutil.get(location, h);
//		String text =cutil.get(location);
		if(text!=null&& text.contains("账户余额")){
			return "1";
		}
		 return null;
	}
	
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(int type){
			saveUser();
			saveTel();
			saveCallHistory();
			saveMessage();
			flowHistory();//流量详单
			
	}

	
	
	
	
	
	/**
	 * 查询个人信息
	 */
	public User gatherUser() {
			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"service.tj.10086.cn",true);
			String text =  cutil.get("http://service.tj.10086.cn/app?service=page/personalinfo.CustInfoMod&listener=initPage", h);
			if(text.contains("客户姓名")){
				Document doc6 = Jsoup.parse(text);
				String xm = StringEscapeUtils.unescapeHtml3(doc6.select("input[id=CUST_NAME]").first().attr("value"));
				String SERIAL_NUMBER = doc6.select("input[name=SERIAL_NUMBER]").first().attr("value");
				RegexPaserUtil rp = new RegexPaserUtil("<dt>通信地址：</dt>","</dl>",text,RegexPaserUtil.TEXTEGEXANDNRT);
	    		String txdz = rp.getText();
	    		String txdz1="";
	    		if(txdz!=null){
	    			RegexPaserUtil rp1 = new RegexPaserUtil("<dd>","</dd>",txdz,RegexPaserUtil.TEXTEGEXANDNRT);
	    			txdz1 = rp1.getText();
	    			if(txdz1!=null){
	    				txdz1=StringEscapeUtils.unescapeHtml3(txdz1);
	    			}
	    		}
				User user = new User();
				user.setLoginName(login.getLoginName());
				user.setLoginPassword("");
				user.setUserName(xm);
				user.setRealName(xm);
				user.setIdcard("");
				user.setAddr(txdz1);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(login.getLoginName());
				user.setFixphone("");
				user.setPhoneRemain(getYue());
				user.setEmail("");
				return user;
			}
			return null;
	}
	
	
	
	
	
	public BigDecimal getYue(){
		BigDecimal phoneremain= new BigDecimal("0.00");
		CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"service.tj.10086.cn",true);
		String text =  cutil.get("http://service.tj.10086.cn/app?service=page/feequery.BalanceQuery&listener=queryBalance", h);
		try {
				if(text!=null&&text.contains("您的当前余额为")){
					
					RegexPaserUtil rp = new RegexPaserUtil("您的当前余额为","</strong>元。",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		String yue = rp.getText();
					
					if(yue!=null){
						RegexPaserUtil rp1 = new RegexPaserUtil("<b>","</b>",yue,RegexPaserUtil.TEXTEGEXANDNRT);
			    		String yue1 = rp1.getText();
			    		if(yue1!=null){
			    			phoneremain = new BigDecimal(yue1.replaceAll("\\s*", ""));
			    		}	
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return phoneremain;
	}

	
	
	
	//Created by Dongyu.Zhang
	/**文本解析
	 * true正常解析
	 * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
	 * type 市话/长途/港澳台/漫游
	 */
	public boolean flowHistory_parse(String text, MobileOnlineList flow, MobileOnlineBill bill, String startDate, List<MobileOnlineList> list){
		boolean b = true;
		Date d = null;
		/*int flowNum = 0;
		try{
			flowNum = bill.getTotalFlow().intValue();
		}catch(Exception e){
			
		}*/
		
		try{
			if(text.contains("GPRS清单查询结果")){
				RegexPaserUtil rp1 = new RegexPaserUtil("<table width=\"90%\" cellspacing=\"0\" border=\"0\" id=\"table\"	class=\"listTable1 hover\">","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String tableString = "<table>"+rp1.getText()+"</table>";
				Document doc6 = Jsoup.parse(tableString);
				Elements tables = doc6.select("table");
				Elements trs = tables.get(0).select("tr");
				for(int i = 1 ; i<trs.size();i++){
					Elements tds = trs.get(i).select("td");
					if(tds.size()>5){
						String kssj = tds.get(0).text();
						String wlbs = tds.get(1).text();//CMNET
						String sc = tds.get(2).text();//秒
						String thdd = tds.get(3).text();//北京市
						String szwl = tds.get(4).text();//GSM网络
						String zll = tds.get(5).text();//总流量   单位是B需要/1024
						long totalFlow = new Long(zll)/1024;
						
						d = DateUtils.StringToDate(kssj, "yyyy-MM-dd HH:mm:ss");
			        	if(flow!=null&&d.getTime()<=flow.getcTime().getTime()){
							b = false;
							break;
						}
			        	MobileOnlineList fFlow = new MobileOnlineList();
			        	fFlow.setcTime(d);
			        	UUID uuid = UUID.randomUUID();
						fFlow.setId(uuid.toString());
			        	fFlow.setOnlineType(wlbs);
			        	fFlow.setTradeAddr(thdd);
			        	fFlow.setOnlineTime(Long.parseLong(sc));
			        	fFlow.setTotalFlow(totalFlow);
			        	fFlow.setPhone(login.getLoginName());
			        	list.add(fFlow);
					}
				
				}
				//bill.setTotalFlow((long)flowNum);
				
				} 
		}catch (Exception e) {
			 b = false;
		}
		return b;
	}
	
	/**
	 * 月度账单
	 */
	public static String allMonthUrl = "http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.BillQuery/feequery.BillQuery/javascript/billQueryResultPart&pagename=feequery.BillQuery&eventname=formSubmit&partids=billQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.5440150887553135&service=direct%2F1%2Ffeequery.BillQuery%2F%24Form&sp=S1&Form1=%24RadioGroup&MONTH=";
	public LinkedList<MonthlyBillMark>  gatherMonthlyBill() {
//		LinkedList<SpeakBillPojo> linkedList = getMonthlyBillAccess("http://www.nx.10086.cn/service/fee/query/queryMonthBill.do","start_month");
		 LinkedList<MonthlyBillMark> listMark = getSpiderMonthsMark(0);
		 for (int m = 0; m < listMark.size(); m++) {
			 listMark.get(m).setText(cutil.get(allMonthUrl+(m+1))); 
		 }
		 String text = null;
		 MonthlyBillMark pojo = null;
		 for (int m = 0; m< listMark.size(); m++) {
				pojo = listMark.get(m);
				text = pojo.getText();
				if(text!=null&&text.contains("账目名称")){
					try{
						RegexPaserUtil rp1 = new RegexPaserUtil("<table  class=\"listTable1 hover tableWidth95\">","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
						String tableString = "<table>"+rp1.getText()+"</table>";
						Document doc6 = Jsoup.parse(tableString);
						Elements tables =doc6.select("table");
						Elements trs = tables.get(0).select("tr");
						BigDecimal tcgdf=new BigDecimal(0);	
						BigDecimal yytxf=new BigDecimal(0);	
						BigDecimal hj=new BigDecimal(0);	
						for(int i = 1 ; i<trs.size();i++){
							Element tr = trs.get(i);
							String trStr =tr.text();
							if(trStr.contains("套餐及固定费")){
								//RegexPaserUtil rp = new RegexPaserUtil("<td><strong><b>","</b></strong></td>",trStr,RegexPaserUtil.TEXTEGEXANDNRT);
								String tcgdfs = trStr.replaceAll("套餐及固定费", "").replaceAll("\\s*", "");
								if(tcgdfs!=null){
									tcgdf= new BigDecimal(tcgdfs);
								}
							}else if(trStr.contains("语音通信费")){
								
								String yytxfs = trStr.replaceAll("语音通信费", "").replaceAll("\\s*", "");
								if(yytxfs!=null){
									yytxf= new BigDecimal(yytxfs);
								}
							}else if(trStr.contains("合计")){
								
								String hjs = trStr.replaceAll("合计<元>", "").replaceAll("\\s*", "");
							
								if(hjs!=null){
									hj= new BigDecimal(hjs);
								}
							}	
						}
						MobileTel mobieTel = new MobileTel();
						mobieTel.setcTime(pojo.getFormatDate());
						mobieTel.setcName("");
						mobieTel.setTeleno(login.getLoginName());
						mobieTel.setTcgdf(tcgdf);
						String year = pojo.getMonth().substring(0, 4);
						String mouth = pojo.getMonth().substring(4, 6);
						mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
						mobieTel.setcAllPay(hj);
						mobieTel.setTcwyytxf(yytxf);
						pojo.setObj(mobieTel);
						pojo.setText(null);
					}catch(Exception e){
						log4j.writeLogByZhangdan(e);
					}
				}
			}
		return listMark;
	}
	
	
	//Created by Dongyu.Zhang
	/**
	 * 查询流量详单和账单
	 */
	public void flowHistory(){
		List<MobileOnlineList> list = new ArrayList<MobileOnlineList>();
		List<MobileOnlineBill> billList = new ArrayList<MobileOnlineBill>();
		try{
			//parseBegin(Constant.YIDONG);
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			//CHeader h = new CHeader(CHeaderUtil.Accept_json,null,null);
			CHeader h = new CHeader(CHeaderUtil.Accept_json,"",CHeaderUtil.Content_Type__urlencoded,"service.tj.10086.cn",true);
			boolean b = false;
			int num=0;
			MobileOnlineList flow = new MobileOnlineList();
			flow.setPhone(login.getLoginName());
			try{
				flow = service.getMobileOnlineListService().getMaxTime(flow.getPhone());
			}catch(Exception e){
				flow = null;
			}
			//--------必须先执行这个get请求，再重新Post才能有第二页，就像那个点一下“第二页”按钮，再点“查询一样”--------
			//cutil.get("http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/queryDetailSMSList&pagename=feequery.VoiceQuery&eventname=queryBusi&pagination_iPage=2&partids=queryDetailSMSList&ajaxSubmitType=get&ajax_randomcode=0.445224101962715378",new CHeader("http://service.tj.10086.cn/app?service=page/feequery.VoiceQuery&listener=initPage"));
			
					     //http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.7491153585988239
			String url  = "http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.4430221446";
			Map<String,String> map = new HashMap<String, String>();
//			map.put("EFFICACY_CODE1", "jnba");
			map.put("Form1","EFFICACY_CODE1,VERIFY_CODE,bforgot");
//			map.put("SMS_NUMBER", "331980");
			map.put("VERIFY_CODE", "jNBa");
			map.put("bforgot", "确  认");
			map.put("mpageName", "feequery.VoiceQuery");
			map.put("service", "direct/1/feequery.VoiceQuery/$Form");
			map.put("sp", "S1");
			map.put("BILL_TYPE", "2");
			map.put("CALL_TYPE", " ");
			map.put("ROAM_TYPE", " ");
			map.put("LONG_TYPE", " ");
			
			int m = 0;
			for (String startDate : ms) {
				m++;
				int page = 1;
				map.put("MONTH", startDate);
				int pageNum = 1;
				String text = cutil.post(url,h, map);
				log.info(text);
				MobileOnlineBill bill = new MobileOnlineBill();
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
					Date monthly = sdf.parse(startDate);
					if(m == 1){
						bill.setIscm(1);
					}else{
						bill.setIscm(0);
					}
					bill.setMonthly(monthly);
					bill.setPhone(login.getLoginName());
					UUID uuid2 = UUID.randomUUID();
		        	bill.setId(uuid2.toString());
		        	
		        					   //http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.7491153585988239
		        	String flowFeeUrl = "http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.BillQuery/feequery.BillQuery/javascript/billQueryResultPart&pagename=feequery.BillQuery&eventname=formSubmit&partids=billQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.8771754020052684&service=direct%2F1%2Ffeequery.BillQuery%2F%24Form&sp=S1&Form1=%24RadioGroup&MONTH="+m;
		        	String flowFeeText = cutil.post(flowFeeUrl, null);
		        	RegexPaserUtil flowFeeRP = new RegexPaserUtil("费用合计：<b>", "<", text, RegexPaserUtil.TEXTEGEXANDNRT);
		        	BigDecimal bd = new BigDecimal(0);
		        	long totalFlow = 0;
		        	try{
		        		if (flowFeeRP!=null) {
		        			String str=flowFeeRP.getText();
		        			if(str!=null){
		        				String flowFeeStr = str.trim();
		        				bd = new BigDecimal(flowFeeStr);
		        			}
		        		}
			        	totalFlow = StringUtil.flowFormat(StringUtil.subStr("总流量合计：<b>", "</b>", text)).longValue();
		        	}catch(Exception e){
		        		log.error("error", e);
		        	}
		        	bill.setTrafficCharges(bd);
		        	bill.setTotalFlow(totalFlow);
		        	
				}catch(Exception e){
					log.error("error", e);
				}
				try{
					int itemIndexStart = text.indexOf("第");
					int itemIndexEnd = text.indexOf("页",itemIndexStart);
					String subPage = text.substring(itemIndexStart + 1, itemIndexEnd).trim();
					int numIndexStart = subPage.indexOf("/");
					String pageNumStr = subPage.substring(numIndexStart+1).trim();
					pageNum = Integer.parseInt(pageNumStr);
				}catch(Exception e){
					
				}
				b = flowHistory_parse(text, flow, bill, startDate, list);
				if(pageNum > 1){
					for(int i = 1; i < pageNum; i++){
						page++;
						cutil.get("http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/queryDetailSMSList&pagename=feequery.VoiceQuery&eventname=queryBusi&pagination_iPage="+page+"&partids=queryDetailSMSList&ajaxSubmitType=get&ajax_randomcode=0.445224101962715378",new CHeader("http://service.tj.10086.cn/app?service=page/feequery.VoiceQuery&listener=initPage"));
						String url2  = "http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.4430221446";
						Map<String,String> map2 = new HashMap<String, String>();
						map2.put("Form1","EFFICACY_CODE1,VERIFY_CODE,bforgot");
						map2.put("VERIFY_CODE", "jNBa");
						map2.put("bforgot", "确  认");
						map2.put("mpageName", "feequery.VoiceQuery");
						map2.put("service", "direct/1/feequery.VoiceQuery/$Form");
						map2.put("sp", "S1");
						map2.put("MONTH", startDate);
						map2.put("BILL_TYPE", "2");
						map2.put("CALL_TYPE", " ");
						map2.put("ROAM_TYPE", " ");
						map2.put("LONG_TYPE", " ");
						map2.put("pagination_iPage", "" + page);
						String text2 = cutil.post(url2,h, map2);
						
						
						b = flowHistory_parse(text2, flow, bill, startDate, list);
						
					}
				}
				
				if(!b){
//					//异常信息
//					if(errorMsg!=null){
//						num ++;
//						//超过五次,发送错误信息,
//						if(num>5){
//							//发送错误信息通知
//							sendWarningCallHistory(errorMsg);
//						}
//						//错误
//						errorMsg = null;
//					}else{
						break;//数据库中已有数据
//					}
				}
				billList.add(bill);
			}
			service.getMobileOnlineBillService().insertbatch(billList);
			service.getMobileOnlineListService().insertbatch(list);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private LinkedList<MobileDetail> detailList = null;
	private LinkedList<MobileMessage> messList = null;
	public LinkedList<MobileMessage> gatherMessage(){
		List<String> ms = DateUtils.getMonths(6,"yyyyMM");
		Date bigTime = getMaxMessageTime();
		messList = new LinkedList<MobileMessage>();
		gatherSpiderStrings(ms,bigTime,1);
		return messList;
	}
	
	public boolean gatherMessageHistory_parse(String text,Date bigTime){
		boolean b = false;
		if(text!=null){
			text = StringEscapeUtils.unescapeHtml3(text);
			if(text.contains("短信清单查询结果")){
				RegexPaserUtil rp1 = new RegexPaserUtil("<table width=\"90%\" cellspacing=\"0\" border=\"0\" id=\"table\" style=\"border:1px solid #c6d5e1; margin:0 auto 15px;width:96%;\" class=\"listTable1 hover\">","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				
				String tableString = "<table>"+rp1.getText()+"</table>";
				Document doc6 = Jsoup.parse(tableString);
				Elements tables = doc6.select("table");
				Elements trs = tables.get(0).select("tr");
				for(int i = 1 ; i<trs.size();i++){
					Elements tds = trs.get(i).select("td");
					try{
						if(tds.size()>3){
							String dfhm = tds.get(0).text();
							String sfsj = tds.get(1).text();
							String jsfs = StringEscapeUtils.unescapeHtml3(tds.get(2).text());
							String zfy = tds.get(3).text().replaceAll("元" ,"");
							Date now = new Date();
							MobileMessage mMessage = new MobileMessage();
							UUID uuid = UUID.randomUUID();
							mMessage.setId(uuid.toString());
							Date d = DateUtils.StringToDate(sfsj, "yyyy-MM-dd HH:mm:ss");
				        	mMessage.setSentTime(d);
				        	if(isContinue(bigTime, d)){
								continue;
							}
				        	if(jsfs.contains("发送")){
				        		mMessage.setTradeWay("发送");
				        	}else if(jsfs.contains("接收")){
				        		mMessage.setTradeWay("接收");
				        	}
				        	mMessage.setCreateTs(now);
				        	mMessage.setRecevierPhone(dfhm);
				        	mMessage.setAllPay(new BigDecimal(Double.parseDouble(zfy)));
				        	mMessage.setPhone(login.getLoginName());
							messList.add(mMessage);
							b = true;
						}
					}catch(Exception e){
						log4j.writeLogByMessage(e);
					}
				}
			}
			
		}
		return b;
	}
	/**
	 * 查询通话记录
	 */
	public LinkedList<MobileDetail> gatherCallHistory(){
		List<String> ms = DateUtils.getMonths(6,"yyyyMM");
		Date bigTime = getMaxCallTime();
		detailList = new LinkedList<MobileDetail>();
		gatherSpiderStrings(ms,bigTime,0);
		return detailList;
	}
	public boolean  gatherCallHistory_parse(String text,Date bigTime){
		boolean b = false;
		if(text!=null){
			text = StringEscapeUtils.unescapeHtml3(text);
			if(text.contains("通话时间")){
				RegexPaserUtil rp1 = new RegexPaserUtil("<table width=\"90%\" cellspacing=\"0\" border=\"0\" id=\"table\" class=\"listTable1 hover\">","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String tableString = "<table>"+rp1.getText()+"</table>";
				Document doc6 = Jsoup.parse(tableString);
				Elements tables =doc6.select("table");
				Elements trs = tables.get(0).select("tr");
				BigDecimal tcgdf=new BigDecimal(0);	
				BigDecimal yytxf=new BigDecimal(0);	
				BigDecimal hj=new BigDecimal(0);	
				for(int i = 1 ; i<trs.size();i++){
					try{
					Elements tds = trs.get(i).select("td");
					if(tds.size()>8){
						String dfhm = tds.get(0).text().replaceAll("\\s*", "");
						String thsj = tds.get(1).text();
						String thlx = StringEscapeUtils.unescapeHtml3(tds.get(2).text().replaceAll("\\s*", ""));
						String thsc = tds.get(3).text().replaceAll("\\s*", "");
						String zfy = tds.get(4).text().replaceAll("元" ,"").replaceAll("\\s*", "");
						String ctlx = StringEscapeUtils.unescapeHtml3(tds.get(5).text().replaceAll("\\s*", ""));
						String thwz = StringEscapeUtils.unescapeHtml3(tds.get(6).text().replaceAll("\\s*", ""));
						String mylx = StringEscapeUtils.unescapeHtml3(tds.get(7).text().replaceAll("\\s*", ""));
						String wl = StringEscapeUtils.unescapeHtml3(tds.get(8).text().replaceAll("\\s*", ""));
						MobileDetail mDetail = new MobileDetail();
						UUID uuid = UUID.randomUUID();
						mDetail.setId(uuid.toString());
			        	mDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
			        	if(isContinue(bigTime, mDetail.getcTime())){
							continue;
						}
			        	mDetail.setTradeAddr(thwz);
			        	
			        	if(thlx.contains("主叫")){
			        		mDetail.setTradeWay("主叫");
			        	}else if(thlx.contains("被叫")){
			        		mDetail.setTradeWay("被叫");
			        	}
			        	int times = 0;
						try{
							TimeUtil tunit = new TimeUtil();
							times = tunit.timetoint(thsc);
						}catch(Exception e){
							
						}		
			        	mDetail.setRecevierPhone(dfhm);
			        	mDetail.setTradeTime(times);
			        	mDetail.setTradeType(ctlx);
			        	mDetail.setTaocan("");
			        	mDetail.setOnlinePay(new BigDecimal(zfy));
			        	mDetail.setPhone(login.getLoginName());
			        	detailList.add(mDetail);
			        	b = true;
					}
					}catch(Exception e){
						log4j.writeLogByHistory(e);
					}
						
				}
			}
		
		}
		return b;
	}
	
	
	
	public void gatherSpiderStrings(List<String> ms,Date bigTime,int type){
		Date d = null;
		String text = null;
		Integer page = 0;
		for (int i = 0; i < ms.size(); i++) {
			if(i!=0){
				//如果当前最大时间比上一次采集的月份还要大,就终止,因为可以理解为后边已经采集了
				d = DateUtils.StringToDate(ms.get(i-1)+"01", "yyyyMMdd");
				if(bigTime!=null&&bigTime.getTime()>=d.getTime()){
					break;
				}
			}
			if(type==0){
				text = cutil.get("http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.7537115738820983&EFFICACY_CODE1=61xy&Form1=EFFICACY_CODE1%2CVERIFY_CODE%2Cbforgot&SMS_NUMBER=355102&VERIFY_CODE=61XY&bforgot=%E7%A1%AE++%E8%AE%A4&mpageName=feequery.VoiceQuery&service=direct%2F1%2Ffeequery.VoiceQuery%2F%24Form&sp=S1&Form1=&MONTH="+ms.get(i)+"&BILL_TYPE=0&CALL_TYPE=+&ROAM_TYPE=+&LONG_TYPE=+");
				boolean b = gatherCallHistory_parse(text, bigTime);
				if(b){
					String pageStr = new RegexPaserUtil("class=\"PageLeft\".*?第1/","页").reset(text).getText();
					if(pageStr!=null){
						try{
							page = Integer.parseInt(pageStr.trim());
						}catch(Exception e){
							log4j.warn("详单页数解析出错#", e);
						}
						if(page!=null&&page>1){
							for (int j = 2; j <= page; j++) {
								text = cutil.get("http://service.tj.10086.cn//app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/queryDetailVoiceList&pagename=feequery.VoiceQuery&eventname=queryBusi&pagination_iPage="+j+"&partids=queryDetailVoiceList&ajaxSubmitType=get&ajax_randomcode=0.533&EFFICACY_CODE1=61xy&Form1=EFFICACY_CODE1%2CVERIFY_CODE%2Cbforgot&SMS_NUMBER=355102&VERIFY_CODE=61XY&bforgot=%E7%A1%AE++%E8%AE%A4&mpageName=feequery.VoiceQuery&service=direct%2F1%2Ffeequery.VoiceQuery%2F%24Form&sp=S1&Form1=&MONTH="+ms.get(i)+"&BILL_TYPE=0&CALL_TYPE=+&ROAM_TYPE=+&LONG_TYPE=+");	
								gatherCallHistory_parse(text, bigTime);
							}
						}
					}
				}
			}else if(type==1){
				//System.out.println("");
				String url  = "http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.2344430221446";
				Map<String,String> map = new HashMap<String, String>();
				map.put("Form1","EFFICACY_CODE1,VERIFY_CODE,bforgot");
//				map.put("SMS_NUMBER", "331980");
				map.put("VERIFY_CODE", "jNBa");
				map.put("bforgot", "确  认");
				map.put("mpageName", "feequery.VoiceQuery");
				map.put("service", "direct/1/feequery.VoiceQuery/$Form");
				map.put("sp", "S1");
				map.put("BILL_TYPE", "1");
				map.put("CALL_TYPE", " ");
				map.put("ROAM_TYPE", " ");
				map.put("LONG_TYPE", " ");
				map.put("MONTH",ms.get(i));
				
				text = cutil.post(url,new CHeader("http://service.tj.10086.cn/app#serCon"),map);
				boolean b = gatherMessageHistory_parse(text, bigTime);
				if(b){
					String pageStr = new RegexPaserUtil("class=\"PageLeft\".*?第1/","页").reset(text).getText();
					if(pageStr!=null){
						try{
							page = Integer.parseInt(pageStr.trim());
						}catch(Exception e){
							log4j.warn("详单页数解析出错#", e);
						}
						if(page!=null&&page>1){
							for (int j = 2; j <= page; j++) {
								if(page>0){
									map.put("pagination_iPage", "" + j);
								}
								text = cutil.post(url, new CHeader("http://service.tj.10086.cn/app#serCon"),map);	
								gatherMessageHistory_parse(text, bigTime);
							}
						}
					}
				}
				
			}else{
				
			}
		}
	}

	@Override
	public LinkedList<? extends Object> gatherFlowList() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<MonthlyBillMark> gatherFlowBill() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
///**
// * 查询通话记录
// */
//public void callHistory(){
//	try {
//		parseBegin( Constant.YIDONG);
//		List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,"",CHeaderUtil.Content_Type__urlencoded,"service.tj.10086.cn",true);
//		boolean b = false;
//		int num = 0;
//		MobileDetail bean = new MobileDetail(login.getLoginName());
//		bean = mobileDetailService.getMaxTime(bean);
//		for (String startDate : ms) {
//			String text =cutil.get("http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.7537115738820983&EFFICACY_CODE1=61xy&Form1=EFFICACY_CODE1%2CVERIFY_CODE%2Cbforgot&SMS_NUMBER=355102&VERIFY_CODE=61XY&bforgot=%E7%A1%AE++%E8%AE%A4&mpageName=feequery.VoiceQuery&service=direct%2F1%2Ffeequery.VoiceQuery%2F%24Form&sp=S1&Form1=&MONTH="+startDate+"&BILL_TYPE=0&CALL_TYPE=+&ROAM_TYPE=+&LONG_TYPE=+",h);
//			b= callHistory_parse(text,bean,startDate);
//			if(!b){
//				//异常信息
//				if(errorMsg!=null){
//					num ++;
//					//超过五次,发送错误信息,
//					if(num>5){
//						//发送错误信息通知
//						sendWarningCallHistory(errorMsg);
//					}
//					//错误
//					errorMsg = null;
//				}else{
//					break;//数据库中已有数据
//				}
//			}
//		}
//	} catch (Exception e) {
//		e.printStackTrace();
//	}finally{
//		parseEnd( Constant.YIDONG);
//	}
//}
///**
// * 查询个人信息
// */
//public void getMyInfo() {
//		try {
//			CHeader  h = new CHeader(CHeaderUtil.Accept_other,"",null,"service.tj.10086.cn",true);
//			String text =  cutil.get("http://service.tj.10086.cn/app?service=page/personalinfo.CustInfoMod&listener=initPage", h);
//			if(text.contains("客户姓名")){
//					parseBegin(Constant.YIDONG);
//					Document doc6 = Jsoup.parse(text);
//					String xm = StringEscapeUtils.unescapeHtml3(doc6.select("input[id=CUST_NAME]").first().attr("value"));
//					String SERIAL_NUMBER = doc6.select("input[name=SERIAL_NUMBER]").first().attr("value");
//					RegexPaserUtil rp = new RegexPaserUtil("<dt>通信地址：</dt>","</dl>",text,RegexPaserUtil.TEXTEGEXANDNRT);
//		    		String txdz = rp.getText();
//		    		String txdz1="";
//		    		if(txdz!=null){
//		    			RegexPaserUtil rp1 = new RegexPaserUtil("<dd>","</dd>",txdz,RegexPaserUtil.TEXTEGEXANDNRT);
//		    			txdz1 = rp1.getText();
//		    			if(txdz1!=null){
//		    				txdz1=StringEscapeUtils.unescapeHtml3(txdz1);
//		    			}
//		    		}
//		    		Map<String, String> parmap = new HashMap<String, String>(3);
//					parmap.put("parentId", currentUser);
//					parmap.put("loginName", login.getLoginName());
//					parmap.put("usersource", Constant.YIDONG);
//					
//					List<User> list = userService.getUserByParentIdSource(parmap);
//					if (list != null && list.size() > 0) {
//						User user = list.get(0);
//						user.setLoginName( login.getLoginName());
//						user.setLoginPassword("");
//						user.setUserName(xm);
//						user.setRealName(xm);
//						user.setIdcard("");
//						user.setAddr(txdz1);
//						user.setUsersource(Constant.YIDONG);
//						user.setParentId(currentUser);
//						user.setModifyDate(new Date());
//						user.setPhone(login.getLoginName());
//						user.setFixphone("");
//						user.setPhoneRemain(getYue());
//						user.setEmail("");
//						userService.update(user);
//					} else {
//						User user = new User();
//						UUID uuid = UUID.randomUUID();
//						user.setId(uuid.toString());
//
//						user.setLoginName(login.getLoginName());
//						user.setLoginPassword("");
//						user.setUserName(xm);
//
//						user.setRealName(xm);
//						user.setIdcard("");
//						user.setAddr(txdz1);
//						user.setUsersource(Constant.YIDONG);
//						user.setParentId(currentUser);
//						user.setModifyDate(new Date());
//						user.setPhone(login.getLoginName());
//						user.setFixphone("");
//						user.setPhoneRemain(getYue());
//						user.setEmail("");
//						userService.saveUser(user);
//					}
//				}
//			
//		} catch (Exception e) {
//			errorMsg=e.getMessage();
//			sendWarningCallHistory(errorMsg);
//		}finally{
//			parseEnd( Constant.YIDONG);
//		}
//}
//public void getTelDetailHtml() {
//	
//	try {
//		boolean b = true;
//		int num = 0;
//		List<String> ms = DateUtils.getMonths(6,"yyyyMM");
//		CHeader  h = new CHeader(CHeaderUtil.Accept_,"",null,"service.tj.10086.cn",true);
//		int mm = 1;
//		for (String startDate : ms) {
//			String text =  cutil.get("http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.BillQuery/feequery.BillQuery/javascript/billQueryResultPart&pagename=feequery.BillQuery&eventname=formSubmit&partids=billQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.5440150887553135&service=direct%2F1%2Ffeequery.BillQuery%2F%24Form&sp=S1&Form1=%24RadioGroup&MONTH="+mm, h);
//			b=getTelDetailHtml_parse(text, startDate);
//			if(!b){
//				//异常信息
//				if(errorMsg!=null){
//					num ++;
//					//超过五次,发送错误信息,
//					if(num>5){
//						//发送错误信息通知
//						sendWarningCallHistory(errorMsg);
//					}
//					//错误
//					errorMsg = null;
//				}else{
//					break;//数据库中已有数据
//				}
//			}
//			mm++;
//		}
//	} catch (Exception e) {
//		e.printStackTrace();
//	}finally{
//	}
//		
//	
//
//	
//}
///**
// * 查询话费记录
// */
//public boolean getTelDetailHtml_parse(String text,String startDate) {
//	boolean b = true;
//	try {
//		if(text.contains("账目名称")){
//			RegexPaserUtil rp1 = new RegexPaserUtil("<table  class=\"listTable1 hover tableWidth95\">","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			String tableString = "<table>"+rp1.getText()+"</table>";
//			Document doc6 = Jsoup.parse(tableString);
//			Elements tables =doc6.select("table");
//			Elements trs = tables.get(0).select("tr");
//			BigDecimal tcgdf=new BigDecimal(0);	
//			BigDecimal yytxf=new BigDecimal(0);	
//			BigDecimal hj=new BigDecimal(0);	
//			for(int i = 1 ; i<trs.size();i++){
//				Element tr = trs.get(i);
//				String trStr =tr.text();
//				if(trStr.contains("套餐及固定费")){
//					//RegexPaserUtil rp = new RegexPaserUtil("<td><strong><b>","</b></strong></td>",trStr,RegexPaserUtil.TEXTEGEXANDNRT);
//					String tcgdfs = trStr.replaceAll("套餐及固定费", "").replaceAll("\\s*", "");
//					if(tcgdfs!=null){
//						tcgdf= new BigDecimal(tcgdfs);
//					}
//				}else if(trStr.contains("语音通信费")){
//					
//					String yytxfs = trStr.replaceAll("语音通信费", "").replaceAll("\\s*", "");
//					if(yytxfs!=null){
//						yytxf= new BigDecimal(yytxfs);
//					}
//				}else if(trStr.contains("合计")){
//					
//					String hjs = trStr.replaceAll("合计<元>", "").replaceAll("\\s*", "");
//				
//					if(hjs!=null){
//						hj= new BigDecimal(hjs);
//					}
//				}	
//			}
//			 Map map2 = new HashMap();
//			 map2.put("phone", login.getLoginName());
//			 map2.put("cTime",DateUtils.StringToDate(startDate, "yyyyMM"));
//			 List list = mobileTelService.getMobileTelBybc(map2);
//			if(list==null || list.size()==0){
//				MobileTel mobieTel = new MobileTel();
//				UUID uuid = UUID.randomUUID();
//				mobieTel.setId(uuid.toString());
////				mobieTel.setBaseUserId(currentUser);
//				mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
//				mobieTel.setcName("");
//				mobieTel.setTeleno(login.getLoginName());
//				mobieTel.setTcgdf(tcgdf);
//				String year = startDate.substring(0, 4);
//				String mouth = startDate.substring(4, 6);
//				mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
//				mobieTel.setcAllPay(hj);
//				mobieTel.setTcwyytxf(yytxf);
//			
//				mobileTelService.saveMobileTel(mobieTel);
//			}
//		}
//	} catch (Exception e) {
//		 errorMsg = e.getMessage();
//		 b = false;
//	}
//
//	return b;
//}
//
///**文本解析
// * true正常解析
// * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
// * type 市话/长途/港澳台/漫游
// */
//public boolean  callHistory_parse(String text,MobileDetail bean,String startDate){
//	boolean b = true;
//	try {
//		if(text.contains("语音清单查询结果")){
//			RegexPaserUtil rp1 = new RegexPaserUtil("<table width=\"90%\" cellspacing=\"0\" border=\"0\" id=\"table\" class=\"listTable1 hover\">","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			String tableString = "<table>"+rp1.getText()+"</table>";
//			Document doc6 = Jsoup.parse(tableString);
//			Elements tables =doc6.select("table");
//			Elements trs = tables.get(0).select("tr");
//			BigDecimal tcgdf=new BigDecimal(0);	
//			BigDecimal yytxf=new BigDecimal(0);	
//			BigDecimal hj=new BigDecimal(0);	
//			for(int i = 1 ; i<trs.size();i++){
//				Elements tds = trs.get(i).select("td");
//				if(tds.size()>8){
//					String dfhm = tds.get(0).text().replaceAll("\\s*", "");
//					String thsj = tds.get(1).text();
//					String thlx = StringEscapeUtils.unescapeHtml3(tds.get(2).text().replaceAll("\\s*", ""));
//					String thsc = tds.get(3).text().replaceAll("\\s*", "");
//					String zfy = tds.get(4).text().replaceAll("元" ,"").replaceAll("\\s*", "");
//					String ctlx = StringEscapeUtils.unescapeHtml3(tds.get(5).text().replaceAll("\\s*", ""));
//					String thwz = StringEscapeUtils.unescapeHtml3(tds.get(6).text().replaceAll("\\s*", ""));
//					String mylx = StringEscapeUtils.unescapeHtml3(tds.get(7).text().replaceAll("\\s*", ""));
//					String wl = StringEscapeUtils.unescapeHtml3(tds.get(8).text().replaceAll("\\s*", ""));
//					 Map map2 = new HashMap();
//					 map2.put("phone", login.getLoginName());
//					 map2.put("cTime", DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
//					 List list = mobileDetailService.getMobileDetailBypt(map2);
//					if(list==null || list.size()==0){
//						MobileDetail mDetail = new MobileDetail();
//						UUID uuid = UUID.randomUUID();
//						mDetail.setId(uuid.toString());
//			        	mDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
//			        	mDetail.setTradeAddr(thwz);
//			        	
//			        	if(thlx.contains("主叫")){
//			        		mDetail.setTradeWay("主叫");
//			        	}else if(thlx.contains("被叫")){
//			        		mDetail.setTradeWay("被叫");
//			        	}
//			        	int times = 0;
//						try{
//							TimeUtil tunit = new TimeUtil();
//							times = tunit.timetoint(thsc);
//						}catch(Exception e){
//							
//						}		
//			        	mDetail.setRecevierPhone(dfhm);
//			        	mDetail.setTradeTime(times);
//			        	mDetail.setTradeType(ctlx);
//			        	mDetail.setTaocan("");
//			        	mDetail.setOnlinePay(new BigDecimal(zfy));
//			        	mDetail.setPhone(login.getLoginName());
//			        	mobileDetailService.saveMobileDetail(mDetail);
//				}
//					
//				}
//			
//			}
//		}
//		
//		
//	} catch (Exception e) {
//		 errorMsg = e.getMessage();
//		 b = false;
//	}
//	return b;
//}
//Created by Dongyu.Zhang
///**文本解析
// * true正常解析
// * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
// * type 市话/长途/港澳台/漫游
// */
//public boolean messageHistory_parse(String text, MobileMessage message, String startDate){
//	boolean b = true;
//	Date d = null;
//	try{
//		if(text.contains("短信清单查询结果")){
//			RegexPaserUtil rp1 = new RegexPaserUtil("<table width=\"90%\" cellspacing=\"0\" border=\"0\" id=\"table\" style=\"border:1px solid #c6d5e1; margin:0 auto 15px;width:96%;\" class=\"listTable1 hover\">","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			
//			String tableString = "<table>"+rp1.getText()+"</table>";
//			Document doc6 = Jsoup.parse(tableString);
//			Elements tables = doc6.select("table");
//			Elements trs = tables.get(0).select("tr");
//			for(int i = 1 ; i<trs.size();i++){
//				Elements tds = trs.get(i).select("td");
//				if(tds.size()>3){
//					String dfhm = tds.get(0).text();
//					String sfsj = tds.get(1).text();
//					String jsfs = StringEscapeUtils.unescapeHtml3(tds.get(2).text());
//					String zfy = tds.get(3).text().replaceAll("元" ,"");
//					Date now = new Date();
//					MobileMessage mMessage = new MobileMessage();
//					UUID uuid = UUID.randomUUID();
//					mMessage.setId(uuid.toString());
//					d = DateUtils.StringToDate(sfsj, "yyyy-MM-dd HH:mm:ss");
//		        	mMessage.setSentTime(d);
//		        	if(message!=null&&d.getTime()<=message.getSentTime().getTime()){
//						b = false;
//						break;
//					}
//		        	if(jsfs.contains("发送")){
//		        		mMessage.setTradeWay("发送");
//		        	}else if(jsfs.contains("接收")){
//		        		mMessage.setTradeWay("接收");
//		        	}
//		        	mMessage.setCreateTs(now);
//		        	mMessage.setRecevierPhone(dfhm);
//		        	mMessage.setAllPay(new BigDecimal(Double.parseDouble(zfy)));
//		        	mMessage.setPhone(login.getLoginName());
//		        	service.getMobileMessageService().save(mMessage);
//					
//				}
//			
//			}
//				
//			} 
//	}catch (Exception e) {
//		 b = false;
//	}
//	return b;
//}

////Created by Dongyu.Zhang
///**
// * 查询短信记录
// */
//public void messageHistory(){
//	
//	try{
//		//parseBegin(Constant.YIDONG);
//		List<String> ms = DateUtils.getMonths(6, "yyyyMM");
//		//CHeader h = new CHeader(CHeaderUtil.Accept_json,null,null);
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,"",CHeaderUtil.Content_Type__urlencoded,"service.tj.10086.cn",true);
//		boolean b = false;
//		int num=0;
//		MobileMessage message = new MobileMessage();
//		message.setPhone(login.getLoginName());
//		try{
//			message = service.getMobileMessageService().getMaxSentTime(message.getPhone());
//		}catch(Exception e){
//			message = null;
//		}
//		//--------必须先执行这个get请求，再重新Post才能有第二页，就像那个点一下“第二页”按钮，再点“查询一样”--------
//		//cutil.get("http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/queryDetailSMSList&pagename=feequery.VoiceQuery&eventname=queryBusi&pagination_iPage=2&partids=queryDetailSMSList&ajaxSubmitType=get&ajax_randomcode=0.445224101962715378",new CHeader("http://service.tj.10086.cn/app?service=page/feequery.VoiceQuery&listener=initPage"));
//		
//		
//		String url  = "http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.4430221446";
//		Map<String,String> map = new HashMap<String, String>();
////		map.put("EFFICACY_CODE1", "jnba");
//		map.put("Form1","EFFICACY_CODE1,VERIFY_CODE,bforgot");
////		map.put("SMS_NUMBER", "331980");
//		map.put("VERIFY_CODE", "jNBa");
//		map.put("bforgot", "确  认");
//		map.put("mpageName", "feequery.VoiceQuery");
//		map.put("service", "direct/1/feequery.VoiceQuery/$Form");
//		map.put("sp", "S1");
//		map.put("BILL_TYPE", "1");
//		map.put("CALL_TYPE", " ");
//		map.put("ROAM_TYPE", " ");
//		map.put("LONG_TYPE", " ");
//		
//		
//		for (String startDate : ms) {
//			int page = 1;
//			map.put("MONTH", startDate);
//			//map.put("pagination_inputPage_5095475", "");
//			//map.put("pagination_inputPage_95617202", "");
////			map.put("pagination_linkType", "5");
//			//map.put("pagination_iPage");
//			int pageNum = 1;
//			String text = cutil.post(url,h, map);
////			try{
//				int itemIndexStart = text.indexOf("第");
//				int itemIndexEnd = text.indexOf("页",itemIndexStart);
//				String subPage = text.substring(itemIndexStart + 1, itemIndexEnd).trim();
//				int numIndexStart = subPage.indexOf("/");
//				String pageNumStr = subPage.substring(numIndexStart+1).trim();
//				pageNum = Integer.parseInt(pageNumStr);
//			}catch(Exception e){
//				
//			}
//			b= messageHistory_parse(text, message, startDate);
//			if(pageNum > 1){
//				for(int i = 1; i < pageNum; i++){
//					page++;
//					cutil.get("http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/queryDetailSMSList&pagename=feequery.VoiceQuery&eventname=queryBusi&pagination_iPage="+page+"&partids=queryDetailSMSList&ajaxSubmitType=get&ajax_randomcode=0.445224101962715378",new CHeader("http://service.tj.10086.cn/app?service=page/feequery.VoiceQuery&listener=initPage"));
//					String url2  = "http://service.tj.10086.cn/app?service=ajaxDirect/1/feequery.VoiceQuery/feequery.VoiceQuery/javascript/voiceQueryResultPart&pagename=feequery.VoiceQuery&eventname=queryAll&partids=voiceQueryResultPart&ajaxSubmitType=post&ajax_randomcode=0.4430221446";
//					Map<String,String> map2 = new HashMap<String, String>();
//					map2.put("Form1","EFFICACY_CODE1,VERIFY_CODE,bforgot");
//					map2.put("VERIFY_CODE", "jNBa");
//					map2.put("bforgot", "确  认");
//					map2.put("mpageName", "feequery.VoiceQuery");
//					map2.put("service", "direct/1/feequery.VoiceQuery/$Form");
//					map2.put("sp", "S1");
//					map2.put("MONTH", startDate);
//					map2.put("BILL_TYPE", "1");
//					map2.put("CALL_TYPE", " ");
//					map2.put("ROAM_TYPE", " ");
//					map2.put("LONG_TYPE", " ");
//					map.put("pagination_iPage", "" + page);
//					String text2 = cutil.post(url2,h, map);
//					b= messageHistory_parse(text2, message, startDate);
//					
//				}
//			}
//			if(!b){
//				break;//数据库中已有数据
//			}
//		}
//		
//	}catch (Exception e) {
//		e.printStackTrace();
//	}
//}