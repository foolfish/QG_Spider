package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
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
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfo;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;


 /**
 * 湖北电信
 * @author jzr
 * 
 */
public class HuBDianxin extends BaseInfoMobile{
	public String CUST_NAME = "";
	private static final Logger logger = Logger.getLogger(HuBDianxin.class);
	public HuBDianxin(Login login) {
		super(login);
	}
	
//	public HuBDianxin( Login login,
//			IWarningService warningService,String currentUser) {
//		super( login, warningService,ConstantNum.comm_hub_dianxin,currentUser);
//	}
	public HuBDianxin(Login login,String currentUser) {
		super(login,  ConstantNum.comm_hub_dianxin, currentUser);
	}
//	public HuBDianxin(Login login, IWarningService warningService,
//			String currentUser, IUserService userService,
//			IDianXinTelService dianXinTelService,
//			IDianXinDetailService dianXinDetailService) {
//		super(login, warningService, ConstantNum.comm_hub_dianxin, currentUser);
//		this.userService = userService;
//		this.dianXinTelService = dianXinTelService;
//		this.dianXinDetailService = dianXinDetailService;
//	}
	
	
//	public Map<String,Object> index(){
//		cutil.get("http://hb.189.cn/login/");
//		Map<String,Object> map = new HashMap<String,Object>();
//		String random = ""+Math.random();
//		////system.out.println(random);
//		setImgUrl("http://hb.189.cn/checkcode?type=login&value="+random);
//		String imgUrl = getAuthcode();
//		map.put("url",imgUrl);
//		return map;
//	}
	
	/**
	 * @return 初始化,并获取验证码
	 */
	public void init(){
		if(!isInit()){
			if(cutil.get("http://hb.189.cn/login/")!=null){
				String random = ""+Math.random();
				setImgUrl("http://hb.189.cn/checkcode?type=login&value="+random);
				setInit();
			}
		}
	}
	public Map<String,Object> login() {
		try{
			String text = login_0();
			//system.out.println(text);
			if(!text.contains("LoginIn?SSORequestXML")){
				errorMsg = "验证码错误！";
				}else{
				text = login_1(text);
				//system.out.println(text);
				text = login_2(text);
				if(!text.contains(login.getLoginName())){
					//RegexPaserUtil rq = new RegexPaserUtil("var errMassges = \"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
					errorMsg = "服务密码错误!";
				}else{
					////system.out.println(text);
					text = login_3(text);
					////system.out.println(text);
					text = login_4(text);
					////system.out.println(text);
					RegexPaserUtil rp1 = new RegexPaserUtil("<span class=\"f_bold floatleft\" style=\"line-height:2\" >","用户，欢迎回来！",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String realName = rp1.getText();
					redismap.put("realName", realName);
					loginsuccess();
				}
			}
		}catch (Exception e) {
			logger.error("error",e);
			writeLogByLogin(e);
		}
		if(status==1){
        	addTask(this);
    	}
		return map;
	}
		
	public Map<String, Object> getTestData() {
		//userKey,telKey,detailKey,messKey
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userKey", saveYuE());
		map.put("telKey", getMonthTel());
		map.put("detailKey", getDetailTel());
		map.put("messKey", null);
		return map;
		/*
		//获取当前用户各月消费记录
		boolean isSaveUser = saveYuE();
		if (isSaveUser) {
			log.info(login.getLoginName() + "保存用户资料成功");
		} else {
			log.info(login.getLoginName() + "保存用户资料失败");
		}
		//获取当前用户各月消费记录
		boolean isTelDetail = getMonthTel();
		if (isTelDetail) {
			log.info(login.getLoginName() + "获取话费资料成功");
		} else {
			log.info(login.getLoginName() + "获取话费资料失败");
		}
		// 获取通话记录
		boolean isPhoneDetail = getDetailTel();
		if (isPhoneDetail) {
			log.info(login.getLoginName() + "获取通话记录成功");
		} else {
			log.info(login.getLoginName() + "获取通话记录失败");
		}*/
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
				getDetailTel();//通话记录
				getSmsLog();//查询短信记录
				break;
			case 2:
				getMonthTel(); //历史账单	
				break;
			case 3:
				saveYuE(); //个人信息
				getDetailTel();//通话记录
				getMonthTel(); //历史账单	
				getSmsLog();//查询短信记录
				getOnlineFlow();//查询流量记录
				break;
			default:
				break;
			}
		}finally{
			parseEnd(Constant.DIANXIN);
		}
	}
	
	//短信记录
	private void getSmsLog()
	{
		String DetailTelUrl = "http://hb.189.cn/feesquery_querylist.action";
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyyMM") ;
			Date lastTime = null;
			try{
				TelcomMessage telcomMessage = telcomMessageService.getMaxSentTime(login.getLoginName());
				if (telcomMessage!=null) {
					lastTime = telcomMessage.getSentTime();
				}
			}
			catch(Exception e)
			{
				logger.error("error",e);
			}	
			
			CHeader h = new CHeader("http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp");
			Map<String, String> param = new HashMap<String, String>();
			param.put("type", "7");
			param.put("prod_type", "1");
			param.put("pagecount", "100000");	//设置只显示一页，取前10万条
			for (String s : ms) {
	 			param.put("startMonth", s+"0000");
				String text = cutil.post(DetailTelUrl, h, param);
				//system.out.println(text);
					if (text.contains("每页显示数量")) {
						Document doc6 = Jsoup.parse(text);
						Elements tables =doc6.select("table");
						Elements trs = tables.get(0).select("tr");
						for(int i = 0 ; i<trs.size();i++){
							Elements tds = trs.get(i).select("td");
							if(tds.size()==5){
								String RecevierPhone = tds.get(1).text().trim();//对方号码
								String SentTime = tds.get(2).text().trim();//发送时间
								String BusinessType = "发送";//tds.get(3).text().trim();//费用类型
								String AllPay = tds.get(4).text().trim();//费用
								
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
		} catch (Exception e) {
			logger.error("error",e);
			String warnType = WaringConstaint.HUBDX_6;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
	}
	
 	private String login_0(){
		String url = "http://hb.189.cn/pages/login/hbloginvalidate.action";
		Map<String,String> param = new HashMap<String,String>();
		param.put("loginSwitch", "CDMA");
		param.put("logintype", "19");
		param.put("citycode","");
		param.put("account", login.getLoginName());
		param.put("password", login.getPassword());
		param.put("loginIp", "219.143.103.242");
		param.put("validatecode", login.getAuthcode());
		param.put("to_page", "URL0");
		param.put("back_page", "URL_BACKPAGE");
		param.put("accessPointer", "1");
		CHeader cHeader = new CHeader("http://hb.189.cn/pages/login/loginView2.jsp");
		return cutil.post(url,cHeader,param);
	}
	private String login_1(String text){
		String xml = text.substring("http://uam.hb.ct10000.com:8080/LoginIn?SSORequestXML=".length());
		String url = "http://uam.hb.ct10000.com:8080/LoginIn";
		Map<String, String> param = new HashMap<String, String>();
		param.put("SSORequestXML", xml);
		CHeader cHeader = new CHeader("http://hb.189.cn/pages/login/loginView2.jsp");
		text = cutil.postURL(url, cHeader, param);
		return text;
	}
	private String login_2(String text){
		text = cutil.get(text);
		return text;
	}
	private String login_3(String text){
		String url = "http://hb.189.cn/hbloginvalidate.action";
		text = cutil.post(url, null);
		return text;
	}
	private String login_4(String text){
		String url = "http://hb.189.cn/hbuserCenter.action";
		text = cutil.get(url);
		return text;
	}
	private List<DianXinTel> getMonthTel() {
		List<DianXinTel> telList = new ArrayList<DianXinTel>();
		String road = "http://hb.189.cn/pages/selfservice/feesquery/zhangdan.jsp";
		CHeader c = new CHeader("http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp");
		String result = cutil.get(road, c);
		//system.out.println(result);
		boolean flag = false;
		Map<String, String> param = new HashMap<String, String>();
		try {
			List<String> ms = DateUtils.getMonths(7, "yyyyMM");
			int x = 0;
			for (String s : ms) {
				Map<String , String> map2 = new HashMap<String , String>();
				map2.put("teleno", login.getLoginName());
				map2.put("cTime", s);
				List list = dianXinTelService.getDianXinTelBybc(map2);
				
				if(x == 0){
					getCurrentTel(list, x, s);
				}
				x++;
				
				param.put("billbeanos.accnbr", login.getLoginName());
				param.put("billbeanos.btime", s);
				param.put("billbeanos.citycode", getRealCityCode(redismap.get("citynum").toString()));
				param.put("billbeanos.paymode", "1");
				param.put("skipmethod.cityname", redismap.get("city").toString());
				String url = "http://hb.189.cn/pages/selfservice/feesquery/newBOSSQueryCustBill.action";
				CHeader cHeader =new CHeader("http://hb.189.cn/pages/selfservice/feesquery/zhangdan.jsp");
				//system.out.println("12");
				String text = cutil.post(url, cHeader, param);
				//system.out.println(text);
				if (text.contains("客户名称")) {
					RegexPaserUtil rp1 = new RegexPaserUtil(
							"<tr><td valign=\"top\"><table align=\"left\" width=\"400px\" border=\"0\">",
							"</table></td><td valign=\"top\"><table align=\"left\" width=\"400px\" border=\"0\">", text, RegexPaserUtil.TEXTEGEXANDNRT);
					RegexPaserUtil rp2 = new RegexPaserUtil(
							"</table></td><td valign=\"top\"><table align=\"left\" width=\"400px\" border=\"0\">",
							"</table>", text, RegexPaserUtil.TEXTEGEXANDNRT);
					String tableString = "<table>" + rp1.getText() + rp2.getText()+"</table>";
					Document doc6 = Jsoup.parse(tableString);
					////system.out.println(doc6);
					Elements tables = doc6.select("table");
					Elements trs = tables.get(0).select("tr");
					BigDecimal ztcjbf = new BigDecimal(0); // 套餐月基本费
					BigDecimal mythf = new BigDecimal(0); // 国内漫游
					BigDecimal hj = new BigDecimal(0);//合计
					for (int i = 1; i < trs.size() ; i++) {
						Element tr = trs.get(i);
						String trStr = tr.text();
						if (trStr.contains("套餐月基本费")) {
							String ztcjbfs = trStr.replaceAll("套餐月基本费", "").replaceAll("元", "").replaceAll("\\s*", "");
							try {
								if (ztcjbfs != null) {
									
									ztcjbf = new BigDecimal(removeChinese(ztcjbfs));
								}
							} catch (Exception e) {
								////system.out.println("网页文本提取出错！套餐及固定费");
								logger.error("error",e);
							}
						} else if (trStr.contains("国内漫游通话")) {
							String mythfs = trStr.replaceAll("国内漫游通话", "").replaceAll("元", "").replaceAll("\\s*", "");
							try {
								if (mythfs != null) {
									mythf = new BigDecimal(removeChinese(mythfs));
								}
							} catch (Exception e) {
								////system.out.println("网页文本提取出错！语音通信费");
								logger.error("error",e);
							}

						} else if (trStr.contains("本项小计")) {
							String hjs = trStr.replaceAll("本项小计", "").replaceAll("元", "").replaceAll("\\s*", "");
							try {
								if (hjs != null) {
									hj = new BigDecimal(removeChinese(hjs));
								}
							} catch (Exception e) {
								////system.out.println("网页文本提取出错！");
								logger.error("error",e);
							}
						}
					}
					if (list == null || list.size() == 0) {
						DianXinTel dx = new DianXinTel();
						UUID uuid = UUID.randomUUID();
						dx.setId(uuid.toString());
//						dx.setBaseUserId(currentUser);
						dx.setcTime(DateUtils.StringToDate(s, "yyyyMM"));
						dx.setcName(redismap.get("realName").toString());
						dx.setTeleno(login.getLoginName());
						String year = s.substring(0, 4);
						String mouth = s.substring(4, 6);
						dx.setDependCycle(TimeUtil.getFirstDayOfMonth(
								Integer.parseInt(year),
								Integer.parseInt(formatDateMouth(mouth)))
								+ "至"
								+ TimeUtil.getLastDayOfMonth(Integer
										.parseInt(year), Integer
										.parseInt(formatDateMouth(mouth))));
						dx.setcAllPay(hj);
						dx.setMythf(mythf);
						dx.setZtcjbf(ztcjbf);
						
						 dianXinTelService.saveDianXinTel(dx);
						 telList.add(dx);
					}
				}

			}
			flag = true;
		} catch (Exception e) {
			logger.error("error",e);
			String warnType = WaringConstaint.HUBDX_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return telList;
	}
	private void getCurrentTel(List list, int x, String s) {
		String url = "http://hb.189.cn/pages/selfservice/feesquery/feesyue.jsp";
		CHeader header = new CHeader("http://hb.189.cn/pages/selfservice/feesquery/feesyue.jsp");
		String html = cutil.get(url, header);
		if (html != null) {
			Document doc = Jsoup.parse(html);
			
			Elements tds = doc.select("td[id=new_real_Charge]");
			if (tds.size() == 1) {
				String pay = tds.get(0).text().trim();
				if (pay.equals("")) {
					pay = "0.00";
				}
				DianXinTel dx = new DianXinTel();
				UUID uuid = UUID.randomUUID();
				dx.setId(uuid.toString());
				dx.setcTime(DateUtils.StringToDate(s, "yyyyMM"));
				dx.setcName(redismap.get("realName").toString());
				dx.setTeleno(login.getLoginName());
				String year = s.substring(0, 4);
				String mouth = s.substring(4, 6);
				dx.setDependCycle(TimeUtil.getFirstDayOfMonth(
						Integer.parseInt(year),
						Integer.parseInt(formatDateMouth(mouth)))
						+ "至"
						+ TimeUtil.getLastDayOfMonth(Integer.parseInt(year),
								Integer.parseInt(formatDateMouth(mouth))));
				try {
					if (pay != null) {
						dx.setcAllPay(new BigDecimal(pay));
					}
				} catch (Exception e) {
					logger.error("error",e);
				}

				dianXinTelService.saveDianXinTel(dx);
			}
		}
		
	}

	private boolean getDetailTel(){
		boolean flag = false;
		String DetailTelUrl = "http://hb.189.cn/feesquery_querylist.action";
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyyMM") ;
			int x = 0;
			CHeader h = new CHeader("http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp");
			Map<String, String> param = new HashMap<String, String>();
			param.put("type", "5");
			/*param.put("prod_type", "1");
			param.put("pagecount", "25");
			param.put("forwardStr", "detailQuery");*/
			for (String s : ms) {
	 			param.put("startMonth", s+"0000");
				String text = cutil.post(DetailTelUrl, h, param);
				//system.out.println(text);
				if(text.contains("每页显示数量")){
					RegexPaserUtil rp1 = new RegexPaserUtil("<th>小计</th></tr>","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					//RegexPaserUtil rp2 = new RegexPaserUtil("</b></td>    </tr>",rp1.getText(),RegexPaserUtil.TEXTEGEXANDNRT);
					String table = rp1.getText(); 
					if(table==null||table.trim().equals("")){
						continue;
					}
					param.put("prod_type", "1");
					param.put("pagecount", "25");
					text = cutil.post(DetailTelUrl, h, param);
					if (text.contains("每页显示数量")) {
						RegexPaserUtil rp2 = new RegexPaserUtil("<th>小计</th></tr>","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
						//RegexPaserUtil rp2 = new RegexPaserUtil("</b></td>    </tr>",rp1.getText(),RegexPaserUtil.TEXTEGEXANDNRT);
						table = rp2.getText(); 
						if(table==null||table.trim().equals("")){
							continue;
						}
						String tableString = "<table>"+table+"</table>";
						Document doc6 = Jsoup.parse(tableString);
						Elements tables =doc6.select("table");
						Elements trs = tables.get(0).select("tr");
						for(int i = 0 ; i<trs.size();i++){
							Elements tds = trs.get(i).select("td");
							if(tds.size()>9){
								String thsj = tds.get(1).text();//起始时间
								String dfhm = tds.get(2).text().replaceAll("\\s*", "");//对方号码
								String thsc = tds.get(3).text().replaceAll("\\s*", "");//通信时长
								String thlx = StringEscapeUtils.unescapeHtml3(tds.get(4).text().replaceAll("\\s*", ""));//通信方式
								String thwz = StringEscapeUtils.unescapeHtml3(tds.get(6).text().replaceAll("\\s*", ""));//通信地点
								String zfy = tds.get(9).text().replaceAll("元" ,"").replaceAll("\\s*", "");//实收通信费
								 Map map2 = new HashMap();
								 map2.put("phone", login.getLoginName());
								 map2.put("cTime", DateUtils.StringToDate(thsj, "yyyy/MM/dd HH:mm:ss"));
								 List list = dianXinDetailService.getDianXinDetailBypt(map2);
								if(list==null || list.size()==0){
									DianXinDetail dxDetail = new DianXinDetail();
									UUID uuid = UUID.randomUUID();
									dxDetail.setId(uuid.toString());
									dxDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy/MM/dd HH:mm:ss"));
									dxDetail.setTradeAddr(thwz);
						        	
						        	if(thlx.contains("主叫")){
						        		dxDetail.setCallWay("主叫");
						        	}else if(thlx.contains("被叫")){
						        		dxDetail.setCallWay("被叫");
						        	}
									dxDetail.setRecevierPhone(dfhm);
									dxDetail.setTradeTime(Integer.parseInt(thsc));
									dxDetail.setAllPay(new BigDecimal(zfy));
									dxDetail.setPhone(login.getLoginName());
						        	if(x==0){
						        		dxDetail.setIscm(1);
						        	}
						        	dianXinDetailService.saveDianXinDetail(dxDetail);
								}
							}
						}
					}
				}
				//判断分页
				try {
					RegexPaserUtil rp1 = new RegexPaserUtil("共","页",text,RegexPaserUtil.TEXTEGEXANDNRT);
					int pageCount = Integer.parseInt(rp1.getText()); 
					if(pageCount>1){
						for (int i = 2; i < pageCount+1; i++) {
							parseDetailPage(i,x);
						}
					}
				} catch (Exception e) {
					logger.error("error",e);
					continue;
				}
				
				
				x++;
			}
			flag = true;
		} catch (Exception e) {
			logger.error("error",e);
			String warnType = WaringConstaint.HUBDX_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return flag;
	}
	
	private void parseDetailPage(int page,int x){
		//http://hb.189.cn/feesquery_pageQuery.action
		String url = "http://hb.189.cn/feesquery_pageQuery.action";
		CHeader c = new CHeader("http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp");
		Map<String, String> param = new HashMap<String, String>();
		param.put("page", ""+page);
		String text = cutil.post(url, c , param);
		if(text.contains("每页显示数量")){
			RegexPaserUtil rp1 = new RegexPaserUtil("<th>小计</th></tr>","</table>",text,RegexPaserUtil.TEXTEGEXANDNRT);
			//RegexPaserUtil rp2 = new RegexPaserUtil("</b></td>    </tr>",rp1.getText(),RegexPaserUtil.TEXTEGEXANDNRT);
			String table = rp1.getText(); 
			if(table==null||table.trim().equals("")){
				return ;
			}
			String tableString = "<table>"+table+"</table>";
			Document doc6 = Jsoup.parse(tableString);
			Elements tables =doc6.select("table");
			Elements trs = tables.get(0).select("tr");
			for(int i = 0 ; i<trs.size();i++){
				Elements tds = trs.get(i).select("td");
				if(tds.size()>9){
					String thsj = tds.get(1).text();//起始时间
					String dfhm = tds.get(2).text().replaceAll("\\s*", "");//对方号码
					String thsc = tds.get(3).text().replaceAll("\\s*", "");//通信时长
					String thlx = StringEscapeUtils.unescapeHtml3(tds.get(4).text().replaceAll("\\s*", ""));//通信方式
					String thwz = StringEscapeUtils.unescapeHtml3(tds.get(6).text().replaceAll("\\s*", ""));//通信地点
					String zfy = tds.get(9).text().replaceAll("元" ,"").replaceAll("\\s*", "");//实收通信费
					 Map map2 = new HashMap();
					 map2.put("phone", login.getLoginName());
					 map2.put("cTime", DateUtils.StringToDate(thsj, "yyyy/MM/dd HH:mm:ss"));
					 List list = dianXinDetailService.getDianXinDetailBypt(map2);
					if(list==null || list.size()==0){
						DianXinDetail dxDetail = new DianXinDetail();
						UUID uuid = UUID.randomUUID();
						dxDetail.setId(uuid.toString());
						dxDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy/MM/dd HH:mm:ss"));
						dxDetail.setTradeAddr(thwz);
			        	
			        	if(thlx.contains("主叫")){
			        		dxDetail.setCallWay("主叫");
			        	}else if(thlx.contains("被叫")){
			        		dxDetail.setCallWay("被叫");
			        	}
						dxDetail.setRecevierPhone(dfhm);
						dxDetail.setTradeTime(Integer.parseInt(thsc));
						dxDetail.setAllPay(new BigDecimal(zfy));
						dxDetail.setPhone(login.getLoginName());
			        	if(x==0){
			        		dxDetail.setIscm(1);
			        	}
			        	dianXinDetailService.saveDianXinDetail(dxDetail);
				}
					
				}
			
			}
		}
	}
	private boolean getOnlineFlow(){
		boolean flag = false;
		String DetailTelUrl = "http://hb.189.cn/feesquery_querylist.action";
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyyMM") ;
			int x = 0;
			CHeader h = new CHeader("http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp");
			Map<String, String> param = new HashMap<String, String>();
			param.put("type", "6");
			param.put("prod_type", "1");
			param.put("pagecount", "100000");	//设置只显示一页，取前10万条
			List<DianXinFlow> flowBillList = new ArrayList<DianXinFlow>();
			List<DianXinFlowDetail> flowDetailList = new ArrayList<DianXinFlowDetail>();
			for (int i = ms.size()-1; i >= 0; i--) {
	 			try {
					param.put("startMonth", ms.get(i)+"0000");
					String text = cutil.post(DetailTelUrl, h, param);
					//system.out.println(text);
					Document doc = Jsoup.parse(text);
					if(doc.toString().contains("每页显示数量")){
						//保存流量账单
						Elements spans = doc.select("span.f_org");
						if (!(spans==null||spans.equals(""))) {
							String dependCycle = spans.get(2).text().trim();
							Date queryMonth = DateUtils.StringToDate(dependCycle.substring(0, 7).replaceAll("\\.", ""), "yyyyMM");
							String allFlow = spans.get(4).text().trim();
							int allTime =  TimeUtil.timetoint_HH_mm_ss(spans.get(5).text().replaceAll("总时长：", "").replaceAll("小时", ":").replaceAll("分钟", ":").replaceAll("秒", ""));
							String allPay = spans.get(6).text().replaceAll("总费用：", "").replaceAll("（元）", "").trim();
							BigDecimal allFlows=new BigDecimal(com.lkb.util.StringUtil.flowFormat(allFlow));
							BigDecimal allPays = new BigDecimal(allPay);
							DianXinFlow dianXinFlow = new DianXinFlow();
							dianXinFlow.setAllFlow(allFlows);
							dianXinFlow.setAllPay(allPays);
							dianXinFlow.setAllTime(new BigDecimal(allTime));
							dianXinFlow.setDependCycle(dependCycle);
							dianXinFlow.setPhone(login.getLoginName());
							dianXinFlow.setQueryMonth(queryMonth);
							flowBillList.add(dianXinFlow);
						}
						//保存流量详单
						Element table = doc.select("table").get(0);
						if (table==null||table.equals("")) {
							continue;
						}
						Elements trs = table.select("tr");
						if (trs.size() > 1) {
							for (int j = 1; j < trs.size(); j++) {
								Elements tds = trs.get(j).select("td");
								if (tds.size() == 8) {
									String beginTime = tds.get(1).text().trim();// 开始时间
									int tradeTime = TimeUtil
											.timetoint_HH_mm_ss(tds.get(2)
													.text().trim()
													.replaceAll("小时", ":")
													.replaceAll("分钟", ":")
													.replaceAll("秒", ""));// 上网时长
									Double flow = com.lkb.util.StringUtil
											.flowFormat(tds.get(3).text()
													.trim());// 流量
									String netType = tds.get(4).text().trim();// 通讯类型
									String location = tds.get(5).text().trim();// 上网地市
									String business = tds.get(6).text().trim();// 使用业务
									String fee = tds.get(7).text().trim();// 费用（元）
									Date beginTimeDate = null;
									BigDecimal feeDecimal = new BigDecimal(0);
									try {
										beginTimeDate = DateUtils.StringToDate(
												beginTime,
												"yyyy/MM/dd HH:mm:ss");
										feeDecimal = new BigDecimal(fee);
									} catch (Exception e) {
										logger.error("error", e);
									}
									DianXinFlowDetail obj = new DianXinFlowDetail();
									obj.setPhone(login.getLoginName());
									obj.setBeginTime(beginTimeDate);
									obj.setFee(feeDecimal);
									obj.setNetType(netType);
									obj.setTradeTime(tradeTime);
									BigDecimal flows = new BigDecimal(0);
									try {
										flows = new BigDecimal(flow);
									} catch (Exception e) {
										logger.error("error", e);
									}
									obj.setFlow(flows);
									obj.setLocation(location);
									obj.setBusiness(business);
									flowDetailList.add(obj);
								}
							}
						}
					} else {
						DianXinFlow dianXinFlow = new DianXinFlow();
						dianXinFlow.setAllFlow(new BigDecimal(0));
						dianXinFlow.setAllPay(new BigDecimal(0));
						dianXinFlow.setAllTime(new BigDecimal(0));
						//2014.10.01 - 2014.10.31
						dianXinFlow.setDependCycle(ms.get(i).substring(0, 4)
								+ "." + ms.get(i).substring(4, 6) + ".01 - "
								+ ms.get(i).substring(0, 4) + "."
								+ ms.get(i).substring(4, 6) + "."
								+ DateUtils.getDaysOfMonth(ms.get(i)));
						dianXinFlow.setPhone(login.getLoginName());
						dianXinFlow.setQueryMonth(DateUtils.StringToDate(ms.get(i), "yyyyMM"));
						flowBillList.add(dianXinFlow);
						
					}
					x++;
				} catch (Exception e) {
					log.error(login.getLoginName()+" "+ms.get(i)+" FlowError!");
				}
			}
			saveDianXinFlowBill(flowBillList);
			saveDianXinFlowDetail(flowDetailList);
			flag = true;
		} catch (Exception e) {
			logger.error("error",e);
			String warnType = WaringConstaint.HUBDX_7;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return flag;
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
			
			String userInfo = cutil.get("http://hb.189.cn/pages/selfservice/custinfo/userinfo/userInfo.action");
			String idCard="";
			String email="";
			String addr="";
			String registerDate="";
			String qq="";
			if(userInfo.contains("客户名称：")&&userInfo.contains("证件类型："))
			{
				Document doc = Jsoup.parse(userInfo);
				Elements tables = doc.select("table");
				Element table=null;
				for(int i=0;i<tables.size();i++)
				{	if(tables.get(i).toString().contains("客户名称："))
					{
						table = tables.get(i);
					}
				}
				
				if(table!=null)
				{
					Elements trs = table.select("tr");
					for(int i=0;i<trs.size();i++)
					{
						if(trs.get(i).toString().contains("证件号码"))
						{
							idCard = trs.get(i).select("td").get(1).text();
						}
						else if(trs.get(i).toString().contains("电子邮件"))
						{
							email = trs.get(i).select("td").get(1).select("input").first().attributes().get("value");
						}
						else if(trs.get(i).toString().contains("通讯地址"))
						{
							addr = trs.get(i).select("td").get(1).select("input").first().attributes().get("value");
						}
						else if(trs.get(i).toString().contains("入网时间"))
						{
							registerDate = trs.get(i).select("td").get(1).text();
						}
						else if(trs.get(i).toString().contains("QQ"))
						{
							qq = trs.get(i).select("td").get(1).select("input").first().attributes().get("value");
						}
					}
				}
				
			}
			
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(redismap.get("realName").toString());
				//user.setRealName(loginName);
				user.setIdcard(idCard);
				user.setAddr(addr);
				user.setUsersource(Constant.DIANXIN);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setEmail(email);
				user.setQq(qq);
				user.setRegisterDate(DateUtils.StringToDate(registerDate, "yyyy-MM-dd HH:mm:ss"));
				userService.update(user);
				user_return = user;
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(redismap.get("realName").toString());
				user.setIdcard(idCard);
				user.setAddr(addr);
				user.setUsersource(Constant.DIANXIN);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setEmail(email);
				user.setQq(qq);
				user.setRegisterDate(DateUtils.StringToDate(registerDate, "yyyy-MM-dd HH:mm:ss"));
				userService.saveUser(user);
				user_return = user;
			}
			return user_return;
		} catch (Exception e) {
			logger.error("error",e);
			String warnType = WaringConstaint.HUBDX_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return null;
	}
	private BigDecimal getYuE() {
		BigDecimal phoneremain = new BigDecimal("0.00");
		try {
			//湖北电信查询当前余额时需要先从feesyue.jsp获取手机号码归属地和用户姓名
			String text = cutil.get("http://hb.189.cn/pages/selfservice/feesquery/feesyue.jsp");
			if(text!=null&&text.contains(login.getLoginName())){
				RegexPaserUtil rp1 = new RegexPaserUtil("var cityname=\"","\";",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String city = rp1.getText();
				RegexPaserUtil rp2 = new RegexPaserUtil("var username=\"","\";",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String user_name_Chinese = rp2.getText();
				RegexPaserUtil rp3 = new RegexPaserUtil("name=\"CITYCODE\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String citynum = rp3.getText();
				//查询账单的时候需要用到手机号码归属地编号
				redismap.put("citynum", citynum);
				redismap.put("city", city);
				//查询余额的具体实现
				String url = "http://hb.189.cn/queryFeesYue.action";
				Map<String,String> param = new HashMap<String,String>();
				param.put("cityname", city);
				param.put("username", user_name_Chinese);
				CHeader cHeader = new CHeader();
				cHeader.setReferer("http://hb.189.cn/pages/selfservice/feesquery/feesyue.jsp");
				text = cutil.post(url, cHeader, param);
				////system.out.println(text);
				//String s= "周灏,15347077828,17.30,17.30,0.00,0.00,2014年08月13日17时,17.30,0.00,0.00,7.30,10.00,0.00,0.00,17.30,0.00,0.00,0.00,0.00";
				String [] iString = text.split(",");
				String YuE = iString[iString.length-5];
				phoneremain = new BigDecimal(YuE);
			}
		} catch (Exception e) {
			logger.error("error",e);
			String warnType = WaringConstaint.HUBDX_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return phoneremain;
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
	private String getRealCityCode(String citycode){
		String realcity="";
		if(citycode.equals("0127")){
			realcity="027";
		}else if(citycode.equals("0719")){
			realcity="0719";
		}else if(citycode.equals("0703")){
			realcity="0728";
		}else if(citycode.equals("0701")){
			realcity="0719";
		}else if(citycode.equals("0702")){
			realcity="0728";
		}else if(citycode.equals("0728")){
			realcity="0728";
		}else{
			realcity=citycode;
		}
		return realcity;
	}
	private String removeChinese(String result){
		if (result.getBytes().length<2) {
			result = "0";
		}else if (result.getBytes().length!=result.length()) {
			result = result.substring(0, result.getBytes().length-2);
		}
		return result;
	}

	public static void main(String[] args) {
		Login login = new Login("15347077828","67901976");
		HuBDianxin hn = new HuBDianxin(login);
		//初始化
		hn.index();
		//登陆
		Scanner in = new Scanner(System.in);
		//system.out.print("验证码为：");
		String authcode = in.nextLine();
		login.setAuthcode(authcode);
		//Map<String,Object> map = hn.login1();
		//获取当前余额
		//hn.getMonthTel("18246463160", "888888888888", null, null);
		//hn.sendMsg();
		////system.out.print("短信验证码为：");
		//String msgAuthcode = in.nextLine();
		//hn.saveDetailTel();
		hn.close();
	}

}
