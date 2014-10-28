package com.lkb.thirdUtil.yd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
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
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;

import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

public class GXYiDong  extends BaseInfoMobile {


	public String index = "https://gx.ac.10086.cn/login";

	// 验证码图片路径
	public static String imgurl = "https://gx.ac.10086.cn/common/image.jsp?l=";




	public GXYiDong(Login login,String currentUser) {
		super(login,ConstantNum.comm_gx_yidong,currentUser);
	}
	
	public static void main(String[] args) {
		
	}
	
	
	
//	public Map<String,Object> index(){
//		
//		map.put("url",getAuthcode());
//		return map;
//	}
	public void init(){
		if(!isInit()){
			String text = cutil.get(index);
			if(text!=null && text.contains("spid")){
				 setImgUrl(imgurl);
				 Document doc = Jsoup.parse(text);
				 String spid = doc.select("input[name=spid]").first().val();
				 if(spid!=null){
					 map.put("spid",spid);
					 redismap.put("jsmap", map);//根据实际需要存放
					 setInit();
				 }	 
			}
		}
	}
	
	public BigDecimal getYue(){
		BigDecimal phoneremain= new BigDecimal("0.00");
		CHeader  h = new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
		Map<String, String> parm = new LinkedMap();
		parm.put("_buttonId", "null");
		parm.put("_menuId", "30102");
		parm.put("_tmpDate", new Date().toGMTString());
		parm.put("_zoneId", "busimain");
		String text =  cutil.post("http://service.gx.10086.cn/ncs/queryaccbalance/AccBalanceQueryAction/initBusi.menu",h,parm);
		try {
				if(text!=null&&text.contains("可用余额")){
					text=text.replaceAll("\\s*", "");
					RegexPaserUtil rp = new RegexPaserUtil("可用余额：","ahref",text,RegexPaserUtil.TEXTEGEXANDNRT);
					text = rp.getText();
					rp = new RegexPaserUtil("￥","<",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String yue = rp.getText();
					if(yue!=null){
						phoneremain = new BigDecimal(yue);
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		return phoneremain;
	}



	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
			try {
				CHeader h= new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/operate/CustiChange/CustiChange.jsp",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);

					Map<String, String> parm = new LinkedMap();
					parm.put("_buttonId", "null");
					parm.put("_menuId", "33333008");
					parm.put("_tmpDate", new Date().toGMTString());
					parm.put("_zoneId", "busimain");
					String text = cutil.post("http://service.gx.10086.cn/ncs/acceptcustifchange/AcceptCustiChangeAction/initBusi.menu", h, parm);
					if(text!=null && text.contains("客户名称")){
						Document doc = Jsoup.parse(text);
						String linker = doc.select("input[id=linker]").first().attr("value");
						String link_phone = doc.select("input[id=link_phone]").first().attr("value");
						String email = doc.select("input[id=email]").first().attr("value");
						String post_code = doc.select("input[id=post_code]").first().attr("value");
						String post_address = doc.select("input[id=post_address]").first().attr("value");
						Map<String, String> map = new HashMap<String, String>();
						map.put("parentId", currentUser);
						map.put("usersource", Constant.YIDONG);
						map.put("loginName", login.getLoginName());
						List<User> list = userService.getUserByParentIdSource(map);
						if (list != null && list.size() > 0) {
							User user = list.get(0);
							user.setLoginName(login.getLoginName());
							user.setLoginPassword("");
							user.setUserName(linker);
							user.setRealName(linker);
							user.setIdcard("");
							user.setAddr(post_address);
							user.setUsersource(Constant.YIDONG);
							user.setParentId(currentUser);
							user.setModifyDate(new Date());
							user.setPhone(login.getLoginName());
							user.setFixphone(link_phone);
							user.setPhoneRemain(getYue());
							user.setEmail(email);
							userService.update(user);
						} else {
							User user = new User();
							UUID uuid = UUID.randomUUID();
							user.setId(uuid.toString());
							user.setLoginName(login.getLoginName());
							user.setLoginPassword("");
							user.setUserName(linker);
							user.setRealName(linker);
							user.setIdcard("");
							user.setAddr(post_address);
							user.setUsersource(Constant.YIDONG);
							user.setParentId(currentUser);
							user.setModifyDate(new Date());
							user.setPhone(login.getLoginName());
							user.setFixphone(link_phone);
							user.setPhoneRemain(getYue());
							user.setEmail(email);
							userService.saveUser(user);
						}
				
						
					}
						
						
					
						
			} catch (Exception e) {
				errorMsg=e.getMessage();
				sendWarningCallHistory(errorMsg);
			}finally{
				parseEnd(Constant.YIDONG);
			}
	}
	
	public void getTelDetailHtml() {
	
		try {
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/fee/yzdcx/index.jsp",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
			h.setRespCharset("UTF-8");
			parseBegin( Constant.YIDONG);
			
			Map<String, String> parm = new LinkedMap();
			parm.put("_buttonId", "null");
			parm.put("_menuId", "30101");
			parm.put("_tmpDate", new Date().toGMTString());
			parm.put("_zoneId", "busimain");
			String text = cutil.post("http://service.gx.10086.cn/ncs/querymonthbill/QueryMonthBillNewAction/initBusi.menu", h, parm);
			//System.out.println(text);
			for (String startDate : ms) {
				 parm = new LinkedMap();
				parm.put("_buttonId", "query_bill");
				parm.put("_menuId", "30101");
				parm.put("_tmpDate", new Date().toGMTString());
				parm.put("_zoneId", "billreszone");
				parm.put("billMonth", startDate);
				parm.put("bill_month", startDate);
				parm.put("queryType", "0");
				parm.put("query_type", "0");
				
				 text = cutil.post("http://service.gx.10086.cn/ncs/querymonthbill/QueryMonthBillNewAction/queryBusi.menu", h, parm);
				
				b=getTelDetailHtml_parse(text, startDate);
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
		
	
		
	}
	

	
	/**
	 * 查询话费记录
	 */
	public boolean getTelDetailHtml_parse(String text,String startDate) {
		boolean b = true;
		try {
			if(text!=null &&text.contains("套餐及固定费用")){
			
				BigDecimal tcgdf=new BigDecimal(0);
				BigDecimal yytxf=new BigDecimal(0);
				BigDecimal swf=new BigDecimal(0);
				BigDecimal dxf=new BigDecimal(0);
				BigDecimal ywzxf=new BigDecimal(0);
				BigDecimal zzywf=new BigDecimal(0);
				BigDecimal dsywf=new BigDecimal(0);
			
				BigDecimal qt=new BigDecimal(0);
				BigDecimal hj=new BigDecimal(0);
				String dependCycle="";
				text=text.replaceAll("\\s*", "");
				RegexPaserUtil rp = new RegexPaserUtil("套餐及固定费用</td><tdclass=\"single_row\">￥","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String tcjgdfs = rp.getText();
				if(tcjgdfs!=null){
					tcgdf= new BigDecimal(tcjgdfs);
				}
				rp = new RegexPaserUtil("语音通信费</td><tdclass=\"double_row\">￥","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String yytxfs = rp.getText();
				if(yytxfs!=null){
					yytxf= new BigDecimal(yytxfs);
				}
				rp = new RegexPaserUtil("上网费</td><tdclass=\"single_row\">￥","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String swfs = rp.getText();
				if(swfs!=null){
					swf=new BigDecimal(swfs);
				}
				rp = new RegexPaserUtil("短（彩）信费</td><tdclass=\"double_row\">￥","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String dxfs = rp.getText();
				if(dxfs!=null){
					dxf=new BigDecimal(dxfs);
				}
				rp = new RegexPaserUtil("增值业务费</td><tdclass=\"single_row\">￥","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String zzywfs = rp.getText();
				if(zzywfs!=null){
					zzywf=new BigDecimal(zzywfs);
				}
				rp = new RegexPaserUtil("代收费业务费用</td><tdclass=\"double_row\">￥","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String dsywfs = rp.getText();
				if(dsywfs!=null){
					dsywf=new BigDecimal(dsywfs);
				}
				rp = new RegexPaserUtil("其他费用</td><tdclass=\"single_row\">￥","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String qtfs = rp.getText();
				if(qtfs!=null){
					qt=new BigDecimal(qtfs);
				}
				rp = new RegexPaserUtil("本期话费总额</td><tdclass=\"double_row\">￥","</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String hjs = rp.getText();
				
				if(hjs!=null){
					hj=new BigDecimal(hjs);
				}
				
				 Map map2 = new HashMap();
				 map2.put("phone", login.getLoginName());
				 map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
				 List list = mobileTelService.getMobileTelBybc(map2);
				if(list==null || list.size()==0){
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
//					mobieTel.setBaseUserId(currentUser);
					mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
					mobieTel.setYdsjllqb(swf);
					mobieTel.setTeleno(login.getLoginName());
					mobieTel.setTcwyytxf(yytxf);
					mobieTel.setTcgdf(tcgdf);
					mobieTel.setTcwdxf(dxf);
					mobieTel.setZzywf(ywzxf);
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
					mobieTel.setcAllPay(hj);
					mobieTel.setZzywf(zzywf);
					mobileTelService.saveMobileTel(mobieTel);
				}
			
				
				
			}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
	
		return b;
	}

	/**文本解析
	 * true正常解析
	 * false 如果errorMsg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
	 * type 市话/长途/港澳台/漫游
	 */
	public boolean  callHistory_parse(String text,MobileDetail bean,String startDate){
		boolean b = true;
		try {
			if(text.contains("主叫")||text.contains("被叫")){
				
				Document doc6 = Jsoup.parse(text);
				Elements tables =doc6.select("table[class=table_detail]");
				for(int i=0;i<tables.size();i++){  
					Element  table = tables.get(i);
					String tableString = table.text();
					if(tableString.contains("起始时间")){
						Elements trs = table.select("tr");
						for(int j = 1 ; j<trs.size();j++){
							Elements tds = trs.get(j).select("td");
							String thsj = tds.get(0).text();
							String txdd = tds.get(1).text();
							String txfs = tds.get(2).text();
							String dfhm = tds.get(3).text();
							String thsc = tds.get(4).text();
							String txlx = tds.get(5).text();
							String tcyh = tds.get(6).text();
							String fy = tds.get(7).text();
							
							 Map map2 = new HashMap();
							 map2.put("phone", login.getLoginName());
							 map2.put("cTime", DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
							 List list = mobileDetailService.getMobileDetailBypt(map2);
						   	if(list==null || list.size()==0){
								MobileDetail mDetail = new MobileDetail();
								UUID uuid = UUID.randomUUID();
								mDetail.setId(uuid.toString());
					        	mDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
					        	if(bean!=null){
									 if(bean.getcTime().getTime()>=mDetail.getcTime().getTime()){
										return false;
									 }
								 }
					        	int times = 0;
								try{
									TimeUtil tunit = new TimeUtil();
									times = tunit.timetoint(thsc);
								}catch(Exception e){
									
								}		
					        	mDetail.setTradeAddr(txdd);
					        	mDetail.setTradeWay(txfs);
					        	mDetail.setRecevierPhone(dfhm);
					        	mDetail.setTradeTime(times);
					        	mDetail.setTradeType(txlx);
					        	mDetail.setTaocan(tcyh);
					        	mDetail.setOnlinePay(new BigDecimal(fy));
					        	mDetail.setPhone(login.getLoginName());    	
								mobileDetailService.saveMobileDetail(mDetail);
						}
							
						}
					}
				}
			}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	/**
	 * 查询通话记录
	 */
	public void callHistory(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyy-MM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/fee/ng/querydetailinfo.jsp",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);
			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				parm.put("_buttonId", "null");
				parm.put("_menuId", "20113506");
				parm.put("_tmpDate", new Date().toGMTString());
				parm.put("_zoneId", "detail");
				parm.put("end_date", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
				parm.put("iPage", "1");
				parm.put("monthQuery", startDate);
				parm.put("newBill", "1");
				parm.put("queryType", "0");
				parm.put("query_mode", "202");
				parm.put("showAll", "0");
				parm.put("showDetail", "1");
				parm.put("start_date", DateUtils.formatDate(new Date(), "yyyy-MM")+"-01");
				String text = cutil.post("http://service.gx.10086.cn/ncs/querydetailinfo/QueryDetailInfoAction/queryBusi.menu", h, parm);
				b= callHistory_parse(text,bean,startDate);
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
			
		
	}
	
	
	/**
	 * 查询通话记录
	 */
	public void messageHistory(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyy-MM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/fee/ng/querydetailinfo.jsp",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
			boolean b = false;
			int num = 0;
		
			MobileMessage bean = mobileMessageService.getMaxSentTime(login.getLoginName());
			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				parm.put("_buttonId", "null");
				parm.put("_menuId", "20113506");
				parm.put("_tmpDate", new Date().toGMTString());
				parm.put("_zoneId", "detail");
				parm.put("end_date", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
				parm.put("iPage", "1");
				parm.put("monthQuery", startDate);
				parm.put("newBill", "1");
				parm.put("queryType", "0");
				parm.put("query_mode", "204");
				parm.put("showAll", "0");
				parm.put("showDetail", "1");
				parm.put("start_date", DateUtils.formatDate(new Date(), "yyyy-MM")+"-01");
				String text = cutil.post("http://service.gx.10086.cn/ncs/querydetailinfo/QueryDetailInfoAction/queryBusi.menu", h, parm);
				b= messageHistory_parse(text,bean,startDate);
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
			
			
		
	}
	
	/**
	 * 查询流量记录
	 */
	public void online(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyy-MM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/fee/ng/querydetailinfo.jsp",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
			boolean b = false;
			int num = 0;
			int iscm1 = 5;
			MobileOnlineList bean_List = null;
			 if(mobileOnlineListService .getMaxTime(login.getLoginName())!= null) {
			bean_List = mobileOnlineListService.getMaxTime(login .getLoginName());
			                     }
			MobileOnlineBill bean_Bill = null;
			if(mobileOnlineBillService .getMaxTime(login.getLoginName())!= null) {
			 bean_Bill = mobileOnlineBillService.getMaxTime(login .getLoginName());
			                     }

			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				parm.put("_buttonId", "null");
				parm.put("_menuId", "20113506");
				parm.put("_tmpDate", new Date().toGMTString());
				parm.put("_zoneId", "detail");
				parm.put("end_date", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
				parm.put("iPage", "1");
				parm.put("monthQuery", startDate);
				parm.put("newBill", "1");
				parm.put("queryType", "0");
				parm.put("query_mode", "203");
				parm.put("showAll", "0");
				parm.put("showDetail", "1");
				parm.put("start_date", DateUtils.formatDate(new Date(), "yyyy-MM")+"-01");
				String text = cutil.post("http://service.gx.10086.cn/ncs/querydetailinfo/QueryDetailInfoAction/queryBusi.menu", h, parm);
				try {
					b= onlineList_parse(text,bean_List,startDate);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					b= onlineBill_parse(text,bean_Bill,startDate,iscm1);
					iscm1++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!b){
					//异常信息
					if(errorMsg!=null){
						num ++;
						//超过五次,发送错误信息,
						if(num>5){
							//发送错误信息通知
							sendWarningCallHistory(errorMsg);
						}
						//错误
						errorMsg = null;
					}else{
						break;//数据库中已有数据
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}
	
	public boolean  onlineList_parse(String text,MobileOnlineList bean_List,String startDate){
		boolean b = true;
		try {
			if(text.contains("上网详单")){
				Document doc6 = Jsoup.parse(text);
				Element table =doc6.select("table[class=table_detail]").get(1);
						Elements trs = table.select("tr");
						//System.out.println(trs.size());
						for(int j = 1 ; j<trs.size()-2;j++){
							Elements tds = trs.get(j).select("td");

							Date cTime = null;
							String tradeAddr = null;
							String onlineType = null;
							long onlineTime = 0;
							long totalFlow = 0;
							String cheapService = null;
							BigDecimal communicationFees = new BigDecimal(0);
							try {
								String cTime1 = tds.get(0).text();
								cTime = DateUtils.StringToDate(cTime1, "yyyy-MM-dd HH:mm:ss");
								tradeAddr = tds.get(1).text();
								onlineType = tds.get(2).text();
								String onlineTime1 = tds.get(3).text();
								
								onlineTime = StringUtil.flowTimeFormat(onlineTime1);
								String totalFlow1 = tds.get(4).text();
								totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
								cheapService = tds.get(5).text();
								String communicationFees1 = tds.get(6).text();// 通信费
								communicationFees = new BigDecimal(communicationFees1);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						 try {
								MobileOnlineList onlineList = new MobileOnlineList();
								UUID uuid = UUID.randomUUID();
								onlineList.setId(uuid.toString());
								onlineList.setcTime(cTime);
								if(bean_List!=null){
									 if(bean_List.getcTime().getTime()<=onlineList.getcTime().getTime()){
										onlineList.setcTime(cTime);
										onlineList.setCheapService(cheapService);
										onlineList.setCommunicationFees(communicationFees);
										onlineList.setOnlineTime(onlineTime);
										onlineList.setOnlineType(onlineType);
										onlineList.setTotalFlow(totalFlow);
										onlineList.setTradeAddr(tradeAddr);
										onlineList.setPhone(login.getLoginName());    	
										mobileOnlineListService.save(onlineList);
									 }
								 }else {
									 	onlineList.setcTime(cTime);
										onlineList.setCheapService(cheapService);
										onlineList.setCommunicationFees(communicationFees);
										onlineList.setOnlineTime(onlineTime);
										onlineList.setOnlineType(onlineType);
										onlineList.setTotalFlow(totalFlow);
										onlineList.setTradeAddr(tradeAddr);
										onlineList.setPhone(login.getLoginName());    	
										mobileOnlineListService.save(onlineList);
								 }
								
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		 }
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	public boolean  onlineBill_parse(String text,MobileOnlineBill bean_Bill,String startDate,int iscm1){
		boolean b = true;
		try {
			if(text.contains("上网详单")){
				Document doc6 = Jsoup.parse(text);
				Element table =doc6.select("table[class=table_detail]").get(0);

							Date monthly = null;
							long totalFlow = 0;
							long freeFlow = 0;
							long chargeFlow = 0;
							BigDecimal trafficCharges = new BigDecimal(0);
							int iscm = 0;
							try {
								monthly = DateUtils.StringToDate(startDate,"yyyy-MM");
								String totalFlow1 = table.select("tbody>tr:eq(1)>td:eq(3)").text().replace("总流量","").trim();
								totalFlow = Math.round(StringUtil.flowFormat(totalFlow1));
								String freeFlow1 = table.select("tbody>tr:eq(1)>td:eq(2)").text().replace("免费流量","").trim();
								freeFlow = Math.round(StringUtil.flowFormat(freeFlow1));
								String chargeFlow1 = table.select("tbody>tr:eq(1)>td:eq(1)").text().replace("收费流量","").trim();
								chargeFlow = Math.round(StringUtil.flowFormat(chargeFlow1));
								
								RegexPaserUtil rp = new RegexPaserUtil("优惠后通信费(实收):								"," 元								<br>",text,RegexPaserUtil.TEXTEGEXANDNRT);
								String trafficChargesStr = "0";
								if(rp.getText()!=null) {
									trafficChargesStr = rp.getText();// 通信费
								}
								try {
									trafficCharges = new BigDecimal(trafficChargesStr);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								log.error("error",e);
							}
							
							Map<String,Object> map = new LinkedHashMap<String, Object>();
							map.put("phone", login.getLoginName());
							map.put("monthly", monthly);
						    List<Map> list = mobileOnlineBillService.getMobileOnlineBillByphone(map);
					   	    if(list==null || list.size()==0){
							MobileOnlineBill onlineBill = new MobileOnlineBill();
							UUID uuid = UUID.randomUUID();
							onlineBill.setId(uuid.toString());
				        	if(bean_Bill!=null){
								 if(bean_Bill.getMonthly().getTime()>=onlineBill.getMonthly().getTime()){
									return false;
								 }
							 }
				        	onlineBill.setMonthly(monthly);
				        	onlineBill.setChargeFlow(chargeFlow);
				        	onlineBill.setFreeFlow(freeFlow);
				        	onlineBill.setTotalFlow(totalFlow);
				        	try {
								onlineBill.setTrafficCharges(trafficCharges);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	onlineBill.setPhone(login.getLoginName());    
				        	if(iscm1==0) {
				        		iscm = 1;
				        	}
				        	onlineBill.setIscm(iscm);
							mobileOnlineBillService.save(onlineBill);
					   	}
					}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	

	

	
	public boolean  messageHistory_parse(String text,MobileMessage bean,String startDate){
		boolean b = true;
		try {
			if(text.contains("短信")){
				Document doc6 = Jsoup.parse(text);
				Elements tables =doc6.select("table[class=table_detail]");
				for(int i=0;i<tables.size();i++){  
					Element  table = tables.get(i);
					String tableString = table.text();
					if(tableString.contains("起始时间")){
						Elements trs = table.select("tr");
						for(int j = 1 ; j<trs.size();j++){
							Elements tds = trs.get(j).select("td");
							String thsj = tds.get(0).text();
							String txdd = tds.get(1).text();
							String dfhm = tds.get(2).text();
							String txfs = tds.get(3).text();
							String xxlx = tds.get(4).text();
							String ywmc = tds.get(5).text();
							String tc = tds.get(6).text();
							String fy = tds.get(7).text();
							
						 Map map2 = new HashMap();
						 map2.put("phone", login.getLoginName());
						 map2.put("sentTime", DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
						 List list = mobileMessageService.getByPhone(map2);
					   	if(list==null || list.size()==0){
							MobileMessage mDetail = new MobileMessage();
							UUID uuid = UUID.randomUUID();
							mDetail.setId(uuid.toString());
				        	mDetail.setSentTime(DateUtils.StringToDate(thsj, "yyyy-MM-dd HH:mm:ss"));
				        	if(bean!=null){
								 if(bean.getSentTime().getTime()>=mDetail.getSentTime().getTime()){
									return false;
								 }
							 }
				        	mDetail.setCreateTs(new Date());
				        	mDetail.setAllPay(new BigDecimal(fy));
				        	mDetail.setRecevierPhone(dfhm);
				        	mDetail.setSentAddr(txdd);
				        	mDetail.setTradeWay(txfs);
				        	mDetail.setPhone(login.getLoginName());    	
							mobileMessageService.save(mDetail);
					   	}
					}
				}
			}
		 }
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	
	
	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
		int type = login.getType();
		switch (type) {
		case 1:
			getTelDetailHtml();//通话记录
			getMyInfo(); //个人信息
			break;
		case 2:
			
			callHistory(); //历史账单	
			messageHistory(); //历史短信记录
			online(); //流量记录
			break;
		case 3:
			getTelDetailHtml();//通话记录
			getMyInfo(); //个人信息
			callHistory(); //历史账单	
			messageHistory(); //历史短信记录
			online(); //流量记录
			break;
		default:
			break;
		}
	}
	

	// 首页登录
	public Map<String,Object> login() {
		try{
		String result = login1();
		if(result!=null&& result.equals("1")){
			loginsuccess();
		}else if(result!=null){
			errorMsg=result;
		}
		if(islogin()){
			sendPhoneDynamicsCode();
			addTask_1(this);
    	}
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;
	}
	
	
	public String login1(){
		try {
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			Map<String, String> parm = new LinkedMap();
			parm.put("RelayState","type=A;backurl=http://www.gx.10086.cn/my/;nl=3;loginFrom=http://www.gx.10086.cn/my/");
			parm.put("backurl", "https://gx.ac.10086.cn/4logingx/backPage.jsp");
			parm.put("errorurl", "https://gx.ac.10086.cn/4logingx/errorPage.jsp");
			parm.put("isValidateCode","1");
			parm.put("mobileNum", login.getLoginName());
			parm.put("servicePassword", login.getPassword());
			parm.put("smsValidCode", "");
			parm.put("spid",jsmap.get("spid").toString() );
			parm.put("type", "B");
			parm.put("validCode", login.getAuthcode());
		
			CHeader h = new CHeader(CHeaderUtil.Accept_,index,CHeaderUtil.Content_Type__urlencoded,"www.gx.10086.cn",true);
			String text =cutil.post("https://gx.ac.10086.cn/Login", h, parm);
			if(text!=null){
				if(text.contains("https://gx.ac.10086.cn/4logingx/backPage.jsp")){
					Document doc = Jsoup.parse(text);
					String SAMLart = doc.select("input[name=SAMLart]").first().attr("value");
					String RelayState = doc.select("input[name=RelayState]").first().attr("value");
					String displayPic = doc.select("input[name=displayPic]").first().attr("value");
					parm = new LinkedMap();
					parm.put("SAMLart", SAMLart);
					parm.put("RelayState", RelayState);
					parm.put("displayPic", displayPic);
					text =cutil.post("https://gx.ac.10086.cn/4logingx/backPage.jsp", h, parm);
					if(text!=null){
						RegexPaserUtil rp = new RegexPaserUtil("parent.Login.callAssert\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
						String SAMLart1 = rp.getText();
						parm = new LinkedMap();
						parm.put("RelayState","type=A;backurl=http://www.gx.10086.cn/my/;nl=3;loginFrom=http://www.gx.10086.cn/my/");
						parm.put("SAMLart", SAMLart1);
						parm.put("myaction", "http://www.gx.10086.cn/my/");
						parm.put("netaction", "http://www.gx.10086.cn/padhallclient/netclient/customer/businessDealing");
						h = new CHeader(CHeaderUtil.Accept_,"http://www.gx.10086.cn/my/",CHeaderUtil.Content_Type__urlencoded,"www.gx.10086.cn",true);
						text =cutil.post("http://www.gx.10086.cn/my/", h, parm);
						if(text!=null && text.contains("http://www.gx.10086.cn/my/")){
							text= cutil.get("http://www.gx.10086.cn/my/",h);
							if(text!=null&&text.contains(login.getLoginName())){
								return login2();
							}
						}	
					}
				}else if(text.contains("code=2003")){
					return "验证码错误.";
				}else if(text.contains("code=4001")){
					return "密码错误.";
					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
	}
	
	public String login2(){
		CHeader h = new CHeader(CHeaderUtil.Accept_,"",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
		String text= cutil.get("http://service.gx.10086.cn/fee/yzdcx/",h);
		if(text!=null){
			Document doc = Jsoup.parse(text);
			String SAMLRequest = doc.select("input[name=SAMLRequest]").first().attr("value");
			String RelayState = doc.select("input[name=RelayState]").first().attr("value");
			LinkedMap parm = new LinkedMap();
			parm.put("SAMLRequest", SAMLRequest);
			parm.put("RelayState", RelayState);
			CHeader h1 = new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/fee/yzdcx/",CHeaderUtil.Content_Type__urlencoded,"gx.ac.10086.cn",true);
			text =cutil.post("https://gx.ac.10086.cn/POST", h1, parm);
			if(text!=null&&text.contains("RelayState")){
				 doc = Jsoup.parse(text);
				 String RelayState1 = doc.select("input[name=RelayState]").first().attr("value");
				 String SAMLart= doc.select("input[name=SAMLart]").first().attr("value");
				 String displayPic= doc.select("input[name=displayPic]").first().attr("value");
				 parm.put("RelayState1", RelayState1);
				 parm.put("SAMLart", SAMLart);
				 parm.put("displayPic", displayPic);
				 text =cutil.post("http://service.gx.10086.cn/fee/yzdcx/", h, parm);
				 if(text!=null && text.equals("http://service.gx.10086.cn/fee/yzdcx/")){
					 cutil.get("http://service.gx.10086.cn/fee/yzdcx/",h);
					 return "1";
				 }
			}
		}
		return null;
	}
	

	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
			Map<String, String> parMap = new LinkedMap();
			parMap.put("_buttonId", "other_sign_btn");
			parMap.put("_menuId", "20113506");
			parMap.put("_tmpDate", new Date().toGMTString());
			parMap.put("_zoneId", "_sign_errzone");
			parMap.put("code", login.getPhoneCode());
			CHeader c = new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/fee/ng/querydetailinfo.jsp",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
			String  text = cutil.post("http://service.gx.10086.cn/ncs/querydetailinfo/QueryDetailInfoAction/checkSecondPsw.menu",c,parMap);
			if(text!=null){
				if(text.contains("校验码验证成功")){
					errorMsg= "登陆成功!";
					status=1;
				}else{
					errorMsg = "您输入的查询验证码错误或过期，请重新核对或再次获取！";
				}
			}else{
				errorMsg = "验证失败,请重试!";
			}
			map.put(CommonConstant.status, status);
			map.put(CommonConstant.errorMsg, errorMsg);
			if(status == 1){
	        	addTask_2(this);
	    	}
			return map;	
	}

	/**
	 * 生成短信
	 * */
	public Map<String,Object> sendPhoneDynamicsCode() {
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		CHeader c = new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/fee/zhyecx/",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
		try {
			sendInit();
			Map<String, String> parMap = new LinkedMap();
			parMap.put("_buttonId", "null");
			parMap.put("_menuId", "20113506");
			parMap.put("_tmpDate", new Date().toGMTString());
			parMap.put("_zoneId", "_sign_errzone");
			String text = cutil.post("http://service.gx.10086.cn/ncs/querydetailinfo/QueryDetailInfoAction/sendSecondPsw.menu",c,parMap);
			if(text!=null && text.contains("验证码已发送至您的手机")){
				errorMsg="验证码已发送至您的手机";
				status = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(errorMsg==null){
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}

	
	
	
	public void sendInit(){
		CHeader c = new CHeader(CHeaderUtil.Accept_,"http://service.gx.10086.cn/fee/zhyecx/",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
		CHeader c1 = new CHeader(CHeaderUtil.Accept_json,"http://service.gx.10086.cn/fee/zhyecx/",CHeaderUtil.Content_Type__urlencoded,"service.gx.10086.cn",true);
		String text =cutil.get("http://service.gx.10086.cn/fee/ng/querydetailinfo.jsp", c);
		Map<String, String> parm = new LinkedMap();
		 parm.put("_buttonId", "null");
		 parm.put("_menuId", "30102");
		 parm.put("_tmpDate", new Date().toGMTString());
		 parm.put("ajaxType", "json");
		 text =cutil.post("http://service.gx.10086.cn/ncs/queryaccbalance/AccBalanceQueryAction/destroyBusi.menu", c1, parm);
		 parm = new LinkedMap();
		 parm.put("_buttonId", "null");
		 parm.put("_menuId", "20113506");
		 parm.put("_tmpDate", new Date().toGMTString());
		 parm.put("_zoneId", "busimain");
		 text =cutil.post("http://service.gx.10086.cn/ncs/querydetailinfo/QueryDetailInfoAction/initBusi.menu", c, parm);
	}
	

	
	

	
	

}
