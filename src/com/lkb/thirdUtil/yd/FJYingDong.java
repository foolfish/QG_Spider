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
import java.util.LinkedList;
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
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;

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

public class FJYingDong  extends BaseInfoMobile {


	public String index = "https://fj.ac.10086.cn/login";

	// 验证码图片路径
	public static String imgurl = "https://fj.ac.10086.cn/common/image.jsp?r_";



	public static void main(String[] args) throws Exception {
		FJYingDong fj = new FJYingDong(new Login("15859075047","888666"), null);
		fj.index();
		fj.inputCode(imgurl);
		fj.login();
	}
	

	public FJYingDong(Login login,String currentUser) {
		super(login,ConstantNum.comm_fj_yidong,currentUser);
	}
	
	
	
	
//	public Map<String,Object> index(){
//		
//		map.put("url",getAuthcode());
//		return map;
//	}
	public void init(){
		if(!isInit()){
			String text = cutil.get(index);
			if(text!=null){
				 setImgUrl(imgurl);
				 Document doc = Jsoup.parse(text);
				 String spid = doc.select("input[name=spid]").first().val();
				 if(spid!=null){
					 map.put("spid",spid);
					 setInit();
				 }	 
			}
			redismap.put("jsmap", map);//根据实际需要存放
		}
	}
	
	
	public BigDecimal getYue(){
		BigDecimal phoneremain = new BigDecimal("0");
		try {
			CHeader h= new CHeader(CHeaderUtil.Accept_,index,null,"www.fj.10086.cn",true);
			String text =cutil.get("http://www.fj.10086.cn/my/fee/query/queryCallFeeSs.do", h);
			Document doc = Jsoup.parse(text);
			Elements yuetable= doc.select("table[class=table_box]");
			if(yuetable.size()>0){
				Element table1 = yuetable.get(0);
				String tableString = table1.text();
				if(tableString.contains("账户总余额")){
					String jphoneremain= table1.select("tr").get(1).select("td").get(1).text();
					if(jphoneremain!=null){
						 phoneremain = new BigDecimal(jphoneremain);
					}
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
				CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.fj.10086.cn",null,"www.fj.10086.cn",true);
				String myinfo = cutil.get("http://www.fj.10086.cn/my/user/getUserInfo.do", h);
				
				if(myinfo.contains("个人资料查询")){
					parseBegin(Constant.YIDONG);
					Document doc = Jsoup.parse(myinfo);
					Elements uls = doc.select("ul[class=password_serve]");
					Elements lis = uls.select("li");
					if(lis.size()>10){
						String xm1= lis.get(2).text();
						String xm = lis.get(2).text().replaceAll("<span class=\"name1 fwb\">姓名：</span>", "").replaceAll("姓名：", "").trim();
						String hsbz = lis.get(3).text().replaceAll("<span class=\"name1 fwb\">证件类型：</span>", "").replaceAll("证件类型：", "").trim();
						String idcard = lis.get(4).text().replaceAll("<span class=\"name1 fwb\"><span class=\"red\">*</span>证件号码：</span>", "").replaceAll("\\*证件号码：", "").trim();
						String txdz = lis.get(5).text().replaceAll("<span class=\"name1 fwb\"><span class=\"red\">*</span>通信地址：</span>", "").replaceAll("\\*通信地址：", "").trim();
						String bjhm = lis.get(6).text().replaceAll("<span class=\"name1 fwb\">本机号码：</span>", "").replaceAll("本机号码：", "").trim();
						String lxdh = lis.get(7).text().replaceAll("<span class=\"name1 fwb\"><span class=\"red\">*</span>联系电话：</span>", "").replaceAll("\\*联系电话：", "").trim();
						String mail = lis.get(8).text().replaceAll("<span class=\"name1 fwb\">mail地址：</span>", "").replaceAll("mail地址：", "").trim();
						String czhm = lis.get(9).text().replaceAll("<span class=\"name1 fwb\">传真号码：</span>", "").replaceAll("传真号码：", "").trim();
						String yzbm = lis.get(10).text().replaceAll("<span class=\"name1 fwb\"><span class=\"red\">*</span>邮政编码：</span>", "").replaceAll("\\*邮政编码：", "").trim();
						Map<String, String> map = new HashMap<String, String>();
						map.put("parentId", currentUser);
						map.put("usersource", Constant.YIDONG);
						map.put("loginName", login.getLoginName());
						List<User> list = userService.getUserByParentIdSource(map);
						if (list != null && list.size() > 0) {
							User user = list.get(0);
							user.setLoginName(login.getLoginName());
							user.setLoginPassword("");
							user.setUserName(xm);
							user.setRealName(xm);
							user.setIdcard(idcard);
							user.setAddr(txdz);
							user.setUsersource(Constant.YIDONG);
							user.setParentId(currentUser);
							user.setModifyDate(new Date());
							user.setPhone(login.getLoginName());
							user.setFixphone(lxdh);
							user.setPhoneRemain(getYue());
							user.setEmail(mail);
							userService.update(user);
						} else {
							User user = new User();
							UUID uuid = UUID.randomUUID();
							user.setId(uuid.toString());
							user.setLoginName(login.getLoginName());
							user.setLoginPassword("");
							user.setUserName(xm);
							user.setRealName(xm);
							user.setIdcard(idcard);
							user.setAddr(txdz);
							user.setUsersource(Constant.YIDONG);
							user.setParentId(currentUser);
							user.setModifyDate(new Date());
							user.setPhone(login.getLoginName());
							user.setFixphone(lxdh);
							user.setPhoneRemain(getYue());
							user.setEmail(mail);
							userService.saveUser(user);
						}
					}
				}
			} catch (Exception e) {
				errorMsg=e.getMessage();
				sendWarningCallHistory(errorMsg);
			}finally{
				parseEnd(Constant.YIDONG);
			}
	}
	
	/**
	 * <p>为了拿实时话费的数值方便写的
	 * @author Pat.Liu
	 * */
	private BigDecimal getBigDecimal(String str){
		str=str.trim();
		if(str==null || str.length()<1)
			return new BigDecimal("0");
		else if(str.contains("￥"))
			str=str.replaceAll("￥", "");
		else if(str.contains("元"))
			str=str.replaceAll("元", "");
		try{
			str=str.trim();
			return new BigDecimal(str);
		}catch(Exception e){
			log.error("FJYidong this month tel error",e);
		}
		return new BigDecimal("0");
	}
	
	private void getThisMonthTel(){
		try {
			int update=0;
			MobileTel newBill=new MobileTel();
			CHeader h= new CHeader(CHeaderUtil.Accept_,index,null,"www.fj.10086.cn",true);
			String text =cutil.get("http://www.fj.10086.cn/my/fee/query/queryCallFee.do", h);
			Document doc = Jsoup.parse(text);
			Elements telTable= doc.select("table[class=table_box]");
			if(telTable!=null && telTable.size()>0){
				Element table=telTable.get(0);
				if(table.hasText()){
					newBill.setTeleno(login.getLoginName());
					//Date d=new Date();
					List<String> d=DateUtils.getMonths(1, "yyyy-MM");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					newBill.setcTime(sdf.parse(d.get(0)+"-01 00:00:00"));
					
					Map map0=new HashMap();
					map0.put("cTime", newBill.getcTime());
					map0.put("phone",login.getLoginName());
					List list=mobileTelService.getMobileTelBybc(map0);
					if(list==null || list.size()==0){
						newBill.setId(UUID.randomUUID().toString());
					}
					else{
						update=1;
						MobileTel oldBill=(MobileTel) list.get(0);
						newBill.setId(oldBill.getId());
					}
					
					Elements trs=table.getElementsByTag("tr");
					for(int i=0;i<trs.size();i++){
						Elements tds=trs.get(i).getElementsByTag("td");
						if(tds!=null && tds.size()==2){
							if(i==0 && tds.text().contains("费用名称"))
								continue;
							if(tds.text().contains("套餐及固定费"))
								newBill.setTcgdf(getBigDecimal(tds.get(1).text()));
							else if(tds.text().contains("套餐外短彩信费"))
								newBill.setTcwdxf(getBigDecimal(tds.get(1).text()));
							else if(tds.text().contains("合计"))
								newBill.setcAllPay(getBigDecimal(tds.get(1).text()));
						}
					}
					
					if(update==0)
						mobileTelService.saveMobileTel(newBill);
					else
						mobileTelService.update(newBill);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("FJYidong this month tel error:",e);
			e.printStackTrace();
		}
	}
	
	public void getTelDetailHtml() {
	
		try {
			boolean b = true;
			int num = 0;
			List<String> ms = DateUtils.getMonthsNotInclude(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.fj.10086.cn",null,"www.fj.10086.cn",true);
			parseBegin( Constant.YIDONG);
			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				parm.put("friendTel",new Date().toString());
				parm.put("query_month",startDate);
				parm.put("search",new Date().toString());
				String text = cutil.post("http://www.fj.10086.cn/my/fee/query/queryServiceFee.do",h,parm);
				//String text = cutil.get("http://www.fj.10086.cn/my/fee/query/queryNewServiceDetail.do?start_month_xdcs="+startDate+"", h);
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
			
			getThisMonthTel();
			
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
			if(text!=null &&text.contains("费用信息")){
				BigDecimal total=new BigDecimal(0);
				BigDecimal tcgdf=new BigDecimal(0);
				BigDecimal tcwyyf=new BigDecimal(0);
				BigDecimal tcwswf=new BigDecimal(0);
				BigDecimal tcwdxf=new BigDecimal(0);
				BigDecimal ywzxf=new BigDecimal(0);
				BigDecimal qtfy=new BigDecimal(0);
				String dependCycle="";
				//text=text.replaceAll("\\s*", "");
				Document doc6 = Jsoup.parse(text);
				Elements tables = doc6.select("table[class=table_box left]");
				if(tables.size()>0){
					Elements lis =	tables.select("li");
					for (Element li : lis) {
						String lihtml = li.html().replaceAll("\\s*", "");
						if(lihtml.contains("套餐及固定费")){
							RegexPaserUtil rp = new RegexPaserUtil("<spanclass=\"right\">","元</span>套餐及固定费",lihtml,RegexPaserUtil.TEXTEGEXANDNRT);
							String tcgdfs = rp.getText();
							if(tcgdfs!=null){
								tcgdf=new BigDecimal(tcgdfs);
							}
						}else if(lihtml.contains("套餐外语音通信费")){
							RegexPaserUtil rp = new RegexPaserUtil("<spanclass=\"right\">","元</span>套餐外语音通信费",lihtml,RegexPaserUtil.TEXTEGEXANDNRT);
							String tcwyyfs = rp.getText();
							if(tcwyyfs!=null){
								tcwyyf=new BigDecimal(tcwyyfs);
							}
						}else if(lihtml.contains("套餐外上网费")){
							RegexPaserUtil rp = new RegexPaserUtil("<spanclass=\"right\">","元</span>套餐外上网费",lihtml,RegexPaserUtil.TEXTEGEXANDNRT);
							String tcwswfs = rp.getText();
							if(tcwswfs!=null){
								tcwswf=new BigDecimal(tcwswfs);
							}
						}else if(lihtml.contains("套餐外短彩信费")){
							RegexPaserUtil rp = new RegexPaserUtil("<spanclass=\"right\">","元</span>套餐外短彩信费",lihtml,RegexPaserUtil.TEXTEGEXANDNRT);
							String tcwdxfs = rp.getText();
							if(tcwdxfs!=null){
								tcwdxf=new BigDecimal(tcwdxfs);
							}
						}else if(lihtml.contains("增值业务费")){
							RegexPaserUtil rp = new RegexPaserUtil("<spanclass=\"right\">","元</span>增值业务费",lihtml,RegexPaserUtil.TEXTEGEXANDNRT);
							String ywzxfs = rp.getText();
							if(ywzxfs!=null){
								ywzxf=new BigDecimal(ywzxfs);
							}
						}else if(lihtml.contains("合计")){
							RegexPaserUtil rp = new RegexPaserUtil("<spanclass=\"right\">","元</span>合计",lihtml,RegexPaserUtil.TEXTEGEXANDNRT);
							String hjs = rp.getText();
							if(hjs!=null){
								total=new BigDecimal(hjs);
							}
						}
					}
					Map map2 = new HashMap();
					 map2.put("phone", login.getLoginName());
					 map2.put("cTime", DateUtils.StringToDate(startDate, "yyyyMM"));
					 List list = mobileTelService.getMobileTelBybc(map2);
					if(list==null || list.size()==0){
						MobileTel mobieTel = new MobileTel();
						UUID uuid = UUID.randomUUID();
						mobieTel.setId(uuid.toString());
//						mobieTel.setBaseUserId(currentUser);
						mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
						// mobieTel.setcName(cName);
						mobieTel.setTeleno(login.getLoginName());
						// mobieTel.setBrand(brand);
						mobieTel.setTcwyytxf(tcwyyf);
						mobieTel.setTcgdf(tcgdf);
						mobieTel.setTcwdxf(tcwdxf);
						mobieTel.setZzywf(ywzxf);
						mobieTel.setDependCycle(dependCycle);
						mobieTel.setcAllPay(total);
						
						mobileTelService.saveMobileTel(mobieTel);
					}
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
	 * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
	 * type 市话/长途/港澳台/漫游
	 */
	public boolean  callHistory_parse(String text,MobileDetail bean,String startDate){
		boolean b = true;
		try {
			System.out.println(text);	
			if(true){
				Document doc  = Jsoup.parse(text);
				 Elements tables = doc.select("table[class=table_xd]");
				 if(tables.size()>0){
					 Element table1 =  tables.get(0);
					 Elements trs  = table1.select("tr");
					 for (int j = 0; j < trs.size(); j++) {
						 Elements tds =trs.get(j).select("td");
						 if(tds.size()>7){
							 String thsj= tds.get(0).text().trim().replaceAll(" ", "");
							 String txdd= tds.get(1).text().trim().replaceAll(" ", "");
							 String txfs= tds.get(2).text().trim().replaceAll(" ", "");
							 String dfhm= tds.get(3).text().trim().replaceAll(" ", "");
							 String thsc= tds.get(4).text().trim().replaceAll(" ", "");
							 String tx= tds.get(5).text().trim().replaceAll(" ", "");
							 if(tx.contains("主叫")){
								 tx = "主叫";
							 }else{
								 tx = "被叫"; 
							 }
							 String tcyh= tds.get(6).text().trim().replaceAll(" ", "");
							 String sjsf= tds.get(7).text().trim().replaceAll(" ", "");
							 Map map2 = new HashMap();
							 map2.put("phone", login.getLoginName());
							 map2.put("cTime", DateUtils.StringToDate(startDate.substring(0, 4)+"-"+thsj, "yyyy-MM-dd HH:mm:ss"));
							 List list = mobileDetailService.getMobileDetailBypt(map2);
							if(list==null || list.size()==0){
								MobileDetail mDetail = new MobileDetail();
								UUID uuid = UUID.randomUUID();
								mDetail.setId(uuid.toString());
					        	mDetail.setcTime(DateUtils.StringToDate(startDate.substring(0, 4)+"-"+thsj, "yyyy-MM-dd HH:mm:ss"));
					        	if(bean!=null){
									 if(bean.getcTime().getTime()>=mDetail.getcTime().getTime()){
										return false;
									 }
								 }
					        	mDetail.setTradeAddr(txdd);
					        	mDetail.setTradeWay(tx);
					        	mDetail.setRecevierPhone(dfhm);
					        	mDetail.setTradeTime(Integer.parseInt(thsc));
					        	mDetail.setTradeType(txfs);
					        	mDetail.setTaocan(tcyh);
					        	mDetail.setOnlinePay(new BigDecimal(sjsf));
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
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.fj.10086.cn",null,"www.fj.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);
			for (String startDate : ms) {
				
				String text = cutil.get("http://www.fj.10086.cn/my/fee/query/queryNewServiceDetail.do?rom=0.8769777291537182&start_month_xdcs="+startDate+"&search=search_ajax&friendTel=&class_id=2", h);
				
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
	
	
	/**文本解析
	 * true正常解析
	 * false 如果msg不等于空出现异常,如果等于空,数据库包含解析数据中止本次循环进入下次
	 * type created by qian
	 */
	public boolean  message_parse(String text,MobileMessage bean,String startDate){
		boolean b = true;
		try {
			if(text.contains("通信地点")&&text.contains("对方号码")){
				Document doc  = Jsoup.parse(text);
				
				 Elements tables = doc.select("table[class=table_xd]");
				 if(tables.size()>0){
					 for(int i = 0; i < tables.size(); i++){
						 Element table1 =  tables.get(i);
						 Elements trs  = table1.select("tr");
						 for (int j = 0; j < trs.size(); j++) {
							 Elements tds =trs.get(j).select("td");
							 if(tds.size()>7){
								 
								 String beginTime = tds.get(0).text().trim().replaceAll(" ", "");
								 String sendAddr = tds.get(1).text().trim().replaceAll(" ", "");
								 String sendType = tds.get(2).text().trim().replaceAll(" ", "");
								 String receivePhone = tds.get(3).text().trim().replaceAll(" ", "");
								 String allPay = tds.get(7).text().trim().replaceAll(" ", "");
								 
								 MobileMessage mMessage = new MobileMessage();
								 UUID uuid = UUID.randomUUID();
								 mMessage.setId(uuid.toString());
								 mMessage.setAllPay(new BigDecimal(allPay));
								 mMessage.setCreateTs(new Date());
								 mMessage.setPhone(login.getLoginName());
								 mMessage.setRecevierPhone(receivePhone);
								 mMessage.setSentAddr(sendAddr);
								 mMessage.setSentTime(DateUtils.StringToDate(startDate.substring(0, 4)+"-"+beginTime, "yyyy-MM-dd HH:mm:ss"));
								 mMessage.setTradeWay("发送");
								 
								 if(bean!=null){
									 if(bean.getSentTime().getTime()>=mMessage.getSentTime().getTime()){
										return false;
									 }
								 }
								 
								 mobileMessageService.save(mMessage);
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

	
	public void getMessage(){
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.fj.10086.cn",null,"www.fj.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileMessage bean = new MobileMessage();
			bean.setPhone(login.getLoginName());
			bean = mobileMessageService.getMaxSentTime(bean.getPhone());
			for (String startDate : ms) {

				String text = cutil.get("http://www.fj.10086.cn/my/fee/query/queryNewServiceDetail.do?rom=0.8769777291537182&start_month_xdcs="+startDate+"&search=search_ajax&friendTel=&class_id=4", h);
				
				b= message_parse(text,bean,startDate);
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
	
	public void getLiuliang()
	{
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.fj.10086.cn",null,"www.fj.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileOnlineBill bean_bill = mobileOnlineBillService.getMaxTime(login.getLoginName());
			MobileOnlineList bean_List = mobileOnlineListService.getMaxTime(login.getLoginName());
			for (String startDate : ms) {

				//http://www.fj.10086.cn/service/fee/query/queryNewServiceDetail.do?start_month_xdcs=201409
				//search=search_ajax&friendTel=Tue Oct 14 11:58:17 UTC+0800 2014&class_id=8
				String text = cutil.get("http://www.fj.10086.cn/my/fee/query/queryNewServiceDetail.do?rom=0.8769777291537182&start_month_xdcs="+startDate+"&search=search_ajax&friendTel=&class_id=8", h);
				b= parseFlowBill(text, startDate, bean_bill);
				b= liuliang_parse(text,startDate, bean_List);
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
	
	private boolean parseFlowBill(String text, String months, MobileOnlineBill bean_bill) {
	try {
		Document doc = Jsoup.parse(text);
		System.out.println(doc.toString());
		if (text != null) {
			Date monthly = DateUtils.StringToDate(months, "yyyyMM");
			
			Elements tds  = doc.select("table").get(0).select("tr").get(4).select("td");
			String freeFlow = tds.get(2).text().substring(0, tds.get(2).text().length()-2).replaceAll("\\(KB\\)", "KB").replaceAll("\\(M\\)", "MB").replaceAll("\\(MB\\)", "MB");
			String chargeFlow = tds.get(3).text().substring(0, tds.get(3).text().length()-2).replaceAll("\\(KB\\)", "KB").replaceAll("\\(M\\)", "MB").replaceAll("\\(MB\\)", "MB");
			Element span  = doc.select("span[class=right]").get(0);
			String totalFees = span.text().replaceAll("元", "").trim();
			if("".equals(freeFlow.trim())){
				freeFlow = "0";
			}
			if("".equals(chargeFlow.trim())){
				chargeFlow = "0";
			}
			if("".equals(totalFees)){
				totalFees = "0";
			}
			
			long free = Math.round(StringUtil.flowFormat(freeFlow));
			long charrge = Math.round(StringUtil.flowFormat(chargeFlow));
			long totalFlow = free + charrge;
			
			
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("phone", login.getLoginName());
			map.put("monthly", monthly);
			List<Map> list = mobileOnlineBillService
					.getMobileOnlineBill(map);
			if (list == null || list.size() == 0) {
				MobileOnlineBill onlineBill = new MobileOnlineBill();
				UUID uuid = UUID.randomUUID();
				onlineBill.setId(uuid.toString());
				onlineBill.setPhone(login.getLoginName());
				onlineBill.setIscm(0);
				onlineBill.setMonthly(monthly);
				onlineBill.setTotalFlow(totalFlow);
				onlineBill.setFreeFlow(free);
				onlineBill.setChargeFlow(charrge);
				onlineBill.setTrafficCharges(new BigDecimal(totalFees));
				if (bean_bill != null && bean_bill.getMonthly() != null) {
					if (bean_bill.getMonthly().getTime() < onlineBill
							.getMonthly().getTime()) {
						mobileOnlineBillService.save(onlineBill);
					} else if(bean_bill.getMonthly().getTime() == onlineBill
							.getMonthly().getTime()){
						mobileOnlineBillService.update(onlineBill);
					}
				} else {
					mobileOnlineBillService.save(onlineBill);
				}
			}
		}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 return false;
		}
		return true;
	}
	
	public boolean  liuliang_parse(String text,String startDate, MobileOnlineList bean_List)
	{
		boolean b = true;
		try {
			List<MobileOnlineList> mobileOnlineList = new LinkedList<MobileOnlineList>();
			if(text!=null&&text.contains("流量")){
				Document doc  = Jsoup.parse(text);
				
				 Elements tables = doc.select("table[class=table_xd]");
				if (tables.size() > 0) {
					for (int i = 0; i < tables.size(); i++) {
						Element table1 = tables.get(i);
						Elements trs = table1.select("tr");
						for (int j = 0; j < trs.size(); j++) {
							Elements tds = trs.get(j).select("td");
							if (tds.size() == 7) {
								String starttime = tds.get(0).text()
										.replace("&nbsp;", "");// 加上日期
								String allTotalFee = tds.get(6).text().trim()
										.replace("&nbsp;", "");
								String area = tds.get(1).text().trim()
										.replace("&nbsp;", "");
								String duration = tds.get(3).text().trim()
										.replace("&nbsp;", "");
								String accessPoint = tds.get(2).text().trim()
										.replace("&nbsp;", "");
								String data = tds.get(4).text().trim()
										.replace("&nbsp;", "");
								String cheapService = tds.get(5).text().trim()
										.replace("&nbsp;", "");

								if (data.contains("\\(M\\)")) {
									String[] str = data.split("\\(M\\)");
									int mb = Integer.parseInt(str[0]);
									int kb = 0;
									if (str[1] != null
											&& str[1].contains("\\(KB\\)")) {
										kb = Integer.parseInt(str[1].replace(
												"\\(KB\\)", ""));
									}

									data = "" + mb * 1024 + kb;

								} else if (data.contains("\\(KB\\)")) {
									data = data.replace("\\(KB\\)", "");
								}
								// 去除字符串前面的？
								starttime = startDate.substring(0, 4) + "-"
										+ starttime.substring(1);
								allTotalFee = allTotalFee.substring(1);
								area = area.substring(1);
								duration = duration.substring(1);
								accessPoint = accessPoint.substring(1);
								data = data.substring(1);
								cheapService = cheapService.substring(1);

								long onlinetime = 0;
								BigDecimal communicationFees = new BigDecimal(
										"0.0");
								;
								long totalFlow = 0;
								Date times = null;

								try {
									times = DateUtils.StringToDate(starttime,
											"yyyy-MM-dd HH:mm:ss");
									if (!duration.equals("")) {
										onlinetime = Long.parseLong(duration
												.trim());
									}
									if (!allTotalFee.equals("")) {
										communicationFees = new BigDecimal(
												allTotalFee.trim());
									}
									if (!data.equals("")) {
										Double du = Double.parseDouble(data);
										totalFlow = du.longValue();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

								MobileOnlineList datalist = new MobileOnlineList();
								UUID uuid = UUID.randomUUID();
								datalist.setId(uuid.toString());
								datalist.setOnlineTime(onlinetime);
								datalist.setPhone(login.getLoginName());
								datalist.setOnlineType(accessPoint);
								datalist.setTotalFlow(totalFlow);
								datalist.setcTime(times);
								datalist.setTradeAddr(area);
								datalist.setCommunicationFees(communicationFees);
								datalist.setCheapService(cheapService);

								if (bean_List != null
										&& bean_List.getcTime() != null) {
									if (bean_List.getcTime().getTime() < datalist
											.getcTime().getTime()) {
										mobileOnlineList.add(datalist);
									} else if (bean_List.getcTime().getTime() == datalist
											.getcTime().getTime()) {
										break;
									} else {
										break;
									}
								} else {
									mobileOnlineList.add(datalist);
								}

							}
						}
					}
				}
			
			}
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
			break;
		case 2:
			callHistory(); //历史账单	
			getMessage();//短信历史
			getMyInfo(); //个人信息
			getLiuliang();//流量
			break;
		case 3:
			getTelDetailHtml();//通话记录
			callHistory(); //历史账单
			getMyInfo(); //个人信息
			getMessage();
			getLiuliang();//流量
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
			}else if(result!=null&& result.contains("验证码错误")){
				errorMsg="验证码错误.";
			}else if(result!=null&& result.contains("密码错误")){
				errorMsg="密码错误.";
			}else if(result!=null&& result.contains("密码或验证码错误")){
				errorMsg="密码或验证码错误.";
			}
		
			if(status==1){
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
			Map<String, String> dxMap = new LinkedMap();
				dxMap.put("Password-type", "");
				dxMap.put("RelayState", "type=type=B;backurl=http://www.fj.10086.cn/my/;nl=3;loginFrom=http://www.fj.10086.cn");
				dxMap.put("backurl", "https://fj.ac.10086.cn/4login/backPage.jsp");
				dxMap.put("button", "登 录");
				dxMap.put("errorurl", "https://fj.ac.10086.cn/4login/errorPage.jsp");
				dxMap.put("mobileNum", login.getLoginName());
				dxMap.put("servicePassword", password(login.getPassword()));
				dxMap.put("smsValidCode", "");
				dxMap.put("spid", jsmap.get("spid").toString());
				dxMap.put("type", "B");
				dxMap.put("validCode", login.getAuthcode());
				CHeader c = new CHeader(CHeaderUtil.Accept_,index,null,"fj.ac.10086.cn",true);
			
				String  text = cutil.post("https://fj.ac.10086.cn/Login", c, dxMap);
				if(text!=null){
					if(text.contains("code=2003")){
						return "验证码错误.";
					}else if(text.contains("code=4001")){
						return "密码错误.";
					}else if(!text.contains("errorPage.jsp")){
						return login2(text);
					}
					
				}	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
	}
	
	public String login2(String pText){
		CHeader c = new CHeader(CHeaderUtil.Accept_,index,CHeaderUtil.Content_Type__urlencoded,"fj.ac.10086.cn",true);
		CHeader c1 = new CHeader(CHeaderUtil.Accept_,"https://fj.ac.10086.cn/Login",CHeaderUtil.Content_Type__urlencoded,"fj.ac.10086.cn",true);
		
		Map<String, String> dxMap1 = new LinkedMap();
		Document doc = Jsoup.parse(pText);
		String SAMLart = doc.select("input[name=SAMLart]").first().attr("value");
		String RelayState = doc.select("input[name=RelayState]").first().attr("value");
		String displayPic = doc.select("input[name=displayPic]").first().attr("value");
		dxMap1.put("SAMLart", SAMLart);
		dxMap1.put("RelayState", RelayState);
		dxMap1.put("displayPic", displayPic);
		//String callContent1 = postHtml(httpclient,"https://fj.ac.10086.cn/4login/backPage.jsp", dxMap1,"fj.ac.10086.cn", "https://fj.ac.10086.cn/Login");
		String text = cutil.post("https://fj.ac.10086.cn/4login/backPage.jsp",c1,dxMap1);
		if(text!=null){
			return login3(text);
		}
		return null;
	}
	
	public String login3(String pText){
		CHeader c = new CHeader(CHeaderUtil.Accept_,"http://www.fj.10086.cn","","www.fj.10086.cn",true);
		Document doc1 = Jsoup.parse(pText);
		String[] dc =doc1.select("body").first().attr("onLoad").replace("parent.callBackurlAll('", "").replace("');", "").split("','");
		String SAMLart1 = dc[0];
		String RelayState1 = dc[1];
		Map<String, String> dxMap3 = new LinkedMap();
		dxMap3.put("SAMLart", SAMLart1);
		dxMap3.put("RelayState",RelayState1);
		String text = cutil.get("http://www.fj.10086.cn/my/?SAMLart="+SAMLart1+"&RelayState=type=B;backurl=http://www.fj.10086.cn/my/;nl=3;loginFrom=http://www.fj.10086.cn/", c);
	//	System.out.println("========="+text);
		if(text.contains("<meta name=\"WT.mobile\" content=\""+login.getLoginName()+"\"/>")){
			
			return "1";
		}
		return null;
	}
	

	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
		
			Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
			Map<String, String> parMap = new LinkedMap();
			parMap.put("Password", "");
			parMap.put("Password-type", "");
			parMap.put("RelayState", "%");
			parMap.put("agentcode", "");
			parMap.put("backurl", "http://www.fj.10086.cn:80/my/ssoAssert.jsp?typesso=C&CALLBACK_URL=http://www.fj.10086.cn:80/my/user/getUserInfo.do");
			parMap.put("do_login_type", "");
			parMap.put("errorurl", "http://www.fj.10086.cn:80/my/login/send.jsp");
			parMap.put("mobileNum", login.getLoginName());
			parMap.put("n1", "1");
			parMap.put("ocs_url", "");
			parMap.put("s02", "false");
			parMap.put("s02", "false");
			parMap.put("servicePassword", "");
			parMap.put("smsValidCode", login.getPhoneCode());
			parMap.put("smscode", login.getPhoneCode());
			parMap.put("smscode1", login.getPhoneCode());
			parMap.put("sp_id", "");
			parMap.put("spid", jsmap.get("spid").toString());
			parMap.put("sso", "0");
			parMap.put("type", "A");
			parMap.put("validCode", "0000");
			CHeader c = new CHeader(CHeaderUtil.Accept_,"http://www.fj.10086.cn/my/index.jsp?id_type=YANZHENGMA",CHeaderUtil.Content_Type__urlencoded,"fj.ac.10086.cn",true);
			String  text = cutil.post("https://fj.ac.10086.cn/Login",c,parMap);
			if(text!=null){
				if(!text.contains("/my/login/send.jsp")){
					Document doc = Jsoup.parse(text);
					String CALLBACK_URL = doc.select("input[name=CALLBACK_URL]").first().attr("value");
					String RelayState4 = doc.select("input[name=RelayState]").first().attr("value");
					String SAMLart4 = doc.select("input[name=SAMLart]").first().attr("value");
					String displayPic4 = doc.select("input[name=displayPic]").first().attr("value");
					String typesso4 = doc.select("input[name=typesso]").first().attr("value");
					parMap= new LinkedMap();
					parMap.put("CALLBACK_URL", CALLBACK_URL);
					parMap.put("RelayState",RelayState4);
					parMap.put("SAMLart", SAMLart4);
					parMap.put("displayPic",displayPic4);
					parMap.put("typesso",typesso4);
					c = new CHeader(CHeaderUtil.Accept_,index,null,"www.fj.10086.cn",true);
					text = cutil.post("http://www.fj.10086.cn/my/ssoAssert.jsp",c,parMap);
					status = 1;	
				}else{
					errorMsg = "您输入的查询验证码错误或过期，请重新核对或再次获取！";
				}
			}else{
				errorMsg = "验证失败,请重试!";
			}
			map.put(CommonConstant.status, status);
			map.put(CommonConstant.errorMsg, errorMsg);
			if(status==1){
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
		int status = 0;
		CHeader c = new CHeader(CHeaderUtil.Accept_,"",null,"fj.ac.10086.cn",true);
		CHeader c1 = new CHeader(CHeaderUtil.Accept_,"http://www.fj.10086.cn/my/index.jsp?id_type=YANZHENGMA",null,"www.fj.10086.cn",true);
		try {
			cutil.setHandleRedirect(false);
			String text = cutil.get("https://fj.ac.10086.cn/SMSCodeSend?mobileNum="+login.getLoginName()+"&validCode=0000&errorurl=http://www.fj.10086.cn:80/my/login/send.jsp",c);
			cutil.setHandleRedirect(true);
			text =cutil.get(text,c1);
			if(text.contains("if(\"0000\" == \"0000\")")){
				errorMsg="发送成功";
				 status = 1;
			}else if(text.contains("if(\"2006\" == \"0000\")")){
				errorMsg="发送条数过多，请稍后再试 。";
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

	public void excute() {

	}
	

	
	public String password(String pwd) throws Exception{
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		String rsaPath = InfoUtil.getInstance().getInfo("road","tomcatWebappPath")+"/js/yd/fjyd_pwd.js";
		 File f = new File(rsaPath);
		 FileInputStream fip = new FileInputStream(f);
		// 构建FileInputStream对象
		 InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
			// 执行指定脚本
		engine.eval(reader);
		if(engine instanceof Invocable) {
			Invocable invoke = (Invocable)engine;
			String s = invoke.invokeFunction("enString",pwd).toString();
			reader.close();
			return s;
		}
		return null;
	}

	
	

}
