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
import com.mysql.jdbc.StringUtils;

public class GSYiDong  extends BaseInfoMobile {


	public String index = "https://gs.ac.10086.cn/login";

	// 验证码图片路径
	public static String imgurl = "https://gs.ac.10086.cn/zcyzm?t=new&ik=l_image_code";



	public static void main(String[] args) throws Exception {
		BigDecimal b = 	new BigDecimal("-123");
		System.out.println(b.divide(new BigDecimal("100")));
	}
	

	public GSYiDong(Login login,String currentUser) {
		super(login,ConstantNum.comm_gs_yidong,currentUser);
	}
	
	
	
	


	public void init(){
		if(!isInit()){
			String text = cutil.get(index);
			if(text!=null){
				 setImgUrl(imgurl);
				 setInit(); 
			}
			redismap.put("jsmap", map);//根据实际需要存放
		}
	}
	
	

	public BigDecimal getYue(){
		Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
		String balance = jsmap.get("balance").toString();
		if(balance!=null){
			BigDecimal b = 	new BigDecimal(balance);
			return b.divide(new BigDecimal("100"));
		}
		return new BigDecimal("0");
	}

	/**
	 * 查询个人信息
	 */
	public void getMyInfo() {
			try {
			
				
						Map<String, String> map = new HashMap<String, String>();
						map.put("parentId", currentUser);
						map.put("usersource", Constant.YIDONG);
						map.put("loginName", login.getLoginName());
						List<User> list = userService.getUserByParentIdSource(map);
						CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.gs.10086.cn/my/myAccount.html",CHeaderUtil.Content_Type__urlencoded,"www.gs.10086.cn",true);

						Map<String, String> parm = new LinkedMap();
						parm.put("jsonParam", "	[{\"dynamicURI\":\"/login\",\"dynamicParameter\":{\"method\":\"updateData\",\"reqHandle\":\"login\",\"busiNum\":\"updateLogin\"},\"dynamicDataNodeName\":\"loginNode1\"}]");
						String text = cutil.post("http://www.gs.10086.cn/gs_obsh_service/actionDispatcher.do", h, parm);
						JSONObject json=new JSONObject(text);
						
						JSONObject jb = json.getJSONObject("loginNode1");
						jb = jb.getJSONObject("resultObj");
						String userName = jb.getString("userName");
						
						if (list != null && list.size() > 0) {
							User user = list.get(0);
							user.setLoginName(login.getLoginName());
							user.setLoginPassword("");
							user.setUserName(userName);
							user.setRealName(userName);
							user.setIdcard("");
							user.setAddr("");
							user.setUsersource(Constant.YIDONG);
							user.setParentId(currentUser);
							user.setModifyDate(new Date());
							user.setPhone(login.getLoginName());
							user.setFixphone("");
							user.setPhoneRemain(getYue());
							user.setEmail("");
							userService.update(user);
						} else {
							User user = new User();
							UUID uuid = UUID.randomUUID();
							user.setId(uuid.toString());
							user.setLoginName(login.getLoginName());
							user.setLoginPassword("");
							user.setUserName(userName);
							user.setRealName(userName);
							user.setIdcard("");
							user.setAddr("");
							user.setUsersource(Constant.YIDONG);
							user.setParentId(currentUser);
							user.setModifyDate(new Date());
							user.setPhone(login.getLoginName());
							user.setFixphone("");
							user.setPhoneRemain(getYue());
							user.setEmail("");
							userService.saveUser(user);
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
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.gs.10086.cn/my/myAccount.html",CHeaderUtil.Content_Type__urlencoded,"www.gs.10086.cn",true);
			h.setRespCharset("UTF-8");
			parseBegin( Constant.YIDONG);
			
			int x = 0;
			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				parm.put("jsonParam", "[{\"dynamicParameter\":{\"method\":\"getBill\",\"busiNum\":\"ZDCX\",\"operType\":\"3\",\"reqHandle\":\"query\",\"queryDate\":\""+startDate+"\"},\"dynamicDataNodeName\":\"getBillNode\"}]");
				String text = cutil.post("http://www.gs.10086.cn/gs_obsh_service/actionDispatcher.do", h, parm);
				b=getTelDetailHtml_parse(text, startDate, x);
				x++;
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
	public boolean getTelDetailHtml_parse(String text,String startDate, int current) {
		boolean b = true;
		try {
			if(current == 0){
				BigDecimal hj=new BigDecimal(0);
				JSONObject json=new JSONObject(text);
				JSONObject jb = json.getJSONObject("getBillNode");
				jb = jb.getJSONObject("resultObj");
				jb = jb.getJSONObject("qRY200012Result");
				String totalFee = jb.getString("totalFee");
				 if(totalFee!=null){
					 if(totalFee.equals("")){
						 hj=new BigDecimal(0);
					 }else {
						 hj=new BigDecimal(totalFee);
					 }
				}
				 
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					mobieTel.setcTime(DateUtils.StringToDate(startDate, "yyyyMM"));
					mobieTel.setTeleno(login.getLoginName());
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth)))+"至"+TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(DateUtils.formatDateMouth(mouth))));
					mobieTel.setcAllPay(hj);
					mobileTelService.saveMobileTel(mobieTel);
			} else {
				if (text != null && text.contains("feeTypeName")) {

					BigDecimal tcgdf = new BigDecimal(0);
					BigDecimal tcwyyf = new BigDecimal(0);
					BigDecimal tcwswf = new BigDecimal(0);
					BigDecimal tcwdxf = new BigDecimal(0);
					BigDecimal ywzxf = new BigDecimal(0);
					BigDecimal zzywf = new BigDecimal(0);
					BigDecimal dsywf = new BigDecimal(0);
					BigDecimal yhtf = new BigDecimal(0);
					BigDecimal qt = new BigDecimal(0);
					BigDecimal hj = new BigDecimal(0);
					String dependCycle = "";

					JSONObject json = new JSONObject(text);
					JSONObject jb = json.getJSONObject("getBillNode");
					jb = jb.getJSONObject("resultObj");
					jb = jb.getJSONObject("qRY200011Result");
					String totalFee = jb.getString("totalFee");
					if (totalFee != null) {
						hj = new BigDecimal(totalFee);
					}
					JSONArray array = jb.getJSONArray("feeDetailList");
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonobject = array.getJSONObject(i);
						String feeTypeName = jsonobject
								.getString("feeTypeName");
						String totalCost = jsonobject.getString("totalCost");
						if ("套餐及固定费".equals(feeTypeName)) {
							tcgdf = new BigDecimal(totalCost);
						} else if ("套餐外语音通信费".equals(feeTypeName)) {
							tcwyyf = new BigDecimal(totalCost);
						} else if ("套餐外上网费".equals(feeTypeName)) {
							tcwswf = new BigDecimal(totalCost);
						} else if ("套餐外短彩信费".equals(feeTypeName)) {
							tcwdxf = new BigDecimal(totalCost);
						} else if ("增值业务费".equals(feeTypeName)) {
							zzywf = new BigDecimal(totalCost);
						} else if ("代收业务费".equals(feeTypeName)) {
							dsywf = new BigDecimal(totalCost);
						} else if ("优惠退费".equals(feeTypeName)) {
							yhtf = new BigDecimal(totalCost);
						} else if ("其他费用".equals(feeTypeName)) {
							qt = new BigDecimal(totalCost);
						}
					}
					Map map2 = new HashMap();
					map2.put("phone", login.getLoginName());
					map2.put("cTime",
							DateUtils.StringToDate(startDate, "yyyyMM"));
					List list = mobileTelService.getMobileTelBybc(map2);
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					mobieTel.setcTime(DateUtils.StringToDate(startDate,
							"yyyyMM"));
					mobieTel.setTeleno(login.getLoginName());
					mobieTel.setTcwyytxf(tcwyyf);
					mobieTel.setTcgdf(tcgdf);
					mobieTel.setTcwdxf(tcwdxf);
					mobieTel.setZzywf(ywzxf);
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)))
							+ "至"
							+ TimeUtil.getLastDayOfMonth(
									Integer.parseInt(year), Integer
											.parseInt(DateUtils
													.formatDateMouth(mouth))));
					mobieTel.setcAllPay(hj);
					mobieTel.setZzywf(zzywf);
					if (list == null || list.size() == 0) {
						mobileTelService.saveMobileTel(mobieTel);
					} else if (list.size() == 1) {
						mobileTelService.update(mobieTel);
					}
				} else {
					MobileTel mobieTel = new MobileTel();
					UUID uuid = UUID.randomUUID();
					mobieTel.setId(uuid.toString());
					mobieTel.setcTime(DateUtils.StringToDate(startDate,
							"yyyyMM"));
					mobieTel.setTeleno(login.getLoginName());
					String year = startDate.substring(0, 4);
					String mouth = startDate.substring(4, 6);
					mobieTel.setDependCycle(TimeUtil.getFirstDayOfMonth(
							Integer.parseInt(year),
							Integer.parseInt(DateUtils.formatDateMouth(mouth)))
							+ "至"
							+ TimeUtil.getLastDayOfMonth(
									Integer.parseInt(year), Integer
											.parseInt(DateUtils
													.formatDateMouth(mouth))));
					mobieTel.setcAllPay(new BigDecimal(0));
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
				
				JSONObject json=new JSONObject(text);
				JSONObject jb = json.getJSONObject("qdcx");  
				jb = jb.getJSONObject("resultObj");
				String detail = jb.getString("detail");
				
				JSONArray jsonDetail=new JSONArray(detail);
			
				for(int i=0;i<jsonDetail.length();i++){  
					String  jsonobject = jsonDetail.getString(i);
					String[] ss = jsonobject.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "").split(",");
						String thsj= ss[0];
					 String thdd= ss[1];
					 String txfs= ss[2];
					 String dfhm= ss[3];
					 String dfhm1= ss[4];
					 String thsc= ss[5];
					 String txlx= ss[6];
					 String tcyh= ss[7];
					 String fy= ss[8];
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
			        	int times = 0;
						try{
							TimeUtil tunit = new TimeUtil();
							times = tunit.timetoint(thsc);
						}catch(Exception e){
							
						}		
			        	mDetail.setTradeAddr(thdd);
			        	mDetail.setTradeWay(txfs);
			        	mDetail.setRecevierPhone(dfhm);
			        	mDetail.setTradeTime(times);
			        	mDetail.setTradeType(txfs);
			        	mDetail.setTaocan(tcyh);
			        	mDetail.setOnlinePay(new BigDecimal(fy));
			        	mDetail.setPhone(login.getLoginName());    	
						mobileDetailService.saveMobileDetail(mDetail);
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
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.gs.10086.cn",null,"www.gs.10086.cn",true);
			boolean b = false;
			int num = 0;
			MobileDetail bean = new MobileDetail(login.getLoginName());
			bean = mobileDetailService.getMaxTime(bean);
			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				parm.put("jsonParam", "[{\"dynamicParameter\":{\"method\":\"cxFeePage\",\"busiNum\":\"CX_YXDBBX\",\"operType\":\"3\",\"reqHandle\":\"query\",\"xdlx\":\"202\",\"cxrq\":\""+startDate+"\",\"ksrq\":\"01\",\"jsrq\":\"31\",\"dfhm\":\"\"},\"dynamicDataNodeName\":\"qdcx\"}]");
				String text = cutil.post("http://www.gs.10086.cn/gs_obsh_service/actionDispatcher.do", h, parm);
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
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.gs.10086.cn",null,"www.gs.10086.cn",true);
			boolean b = false;
			int num = 0;
		
			MobileMessage bean = mobileMessageService.getMaxSentTime(login.getLoginName());
			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				parm.put("jsonParam", "[{\"dynamicParameter\":{\"method\":\"cxFeePage\",\"busiNum\":\"CX_YXDBBX\",\"operType\":\"3\",\"reqHandle\":\"query\",\"xdlx\":\"204\",\"cxrq\":\""+startDate+"\",\"ksrq\":\"01\",\"jsrq\":\"31\",\"dfhm\":\"\"},\"dynamicDataNodeName\":\"qdcx\"}]");
				String text = cutil.post("http://www.gs.10086.cn/gs_obsh_service/actionDispatcher.do", h, parm);
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

	
	public boolean  messageHistory_parse(String text,MobileMessage bean,String startDate){
		boolean b = true;
		try {
			if(text.contains("短信")){
				
				JSONObject json=new JSONObject(text);
				JSONObject jb = json.getJSONObject("qdcx");  
				jb = jb.getJSONObject("resultObj");
				String detail = jb.getString("detail");
				
				JSONArray jsonDetail=new JSONArray(detail);
			
				for(int i=0;i<jsonDetail.length();i++){  
					String  jsonobject = jsonDetail.getString(i);
					String[] ss = jsonobject.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "").split(",");
					String sentTime= ss[0];
					String sentAddr= ss[1];
					String recevierPhone= ss[2];
					String tradeWay= ss[3];
					String xxlx= ss[4];
					String ywmc= ss[5];
					String tc= ss[6];
					String allPay= ss[7];
					 Map map2 = new HashMap();
					 map2.put("phone", login.getLoginName());
					 map2.put("sentTime", DateUtils.StringToDate(startDate.substring(0, 4)+"-"+sentTime, "yyyy-MM-dd HH:mm:ss"));
					 List list = mobileMessageService.getByPhone(map2);
				   	if(list==null || list.size()==0){
						MobileMessage mDetail = new MobileMessage();
						UUID uuid = UUID.randomUUID();
						mDetail.setId(uuid.toString());
			        	mDetail.setSentTime(DateUtils.StringToDate(startDate.substring(0, 4)+"-"+sentTime, "yyyy-MM-dd HH:mm:ss"));
			        	if(bean!=null){
							 if(bean.getSentTime().getTime()>=mDetail.getSentTime().getTime()){
								return false;
							 }
						 }
			        	mDetail.setCreateTs(new Date());
			        	mDetail.setAllPay(new BigDecimal(allPay));
			        	mDetail.setRecevierPhone(recevierPhone);
			        	mDetail.setSentAddr(sentAddr);
			        	mDetail.setTradeWay(tradeWay);
			        	mDetail.setPhone(login.getLoginName());    	
						mobileMessageService.save(mDetail);
				}
				}
				
			
			}
		} catch (Exception e) {
			 errorMsg = e.getMessage();
			 b = false;
		}
		return b;
	}
	
	public void liuliangHistory()
	{
		try {
			parseBegin(Constant.YIDONG);
			List<String> ms =  DateUtils.getMonths(6,"yyyyMM");
			CHeader h= new CHeader(CHeaderUtil.Accept_,"http://www.gs.10086.cn",null,"www.gs.10086.cn",true);
			boolean b = false;
			int num = 0;	
			
			for (String startDate : ms) {
				Map<String, String> parm = new LinkedMap();
				// [{"dynamicParameter":{"method":"cxFeePage","busiNum":"CX_YXDBBX","operType":"3","reqHandle":"query","xdlx":"203","cxrq":"201409","ksrq":"01","jsrq":"30","dfhm":""},"dynamicDataNodeName":"qdcx"}]
				// TODO: 马延龙
				// [{"dynamicParameter":{"method":"cxFeePage","busiNum":"CX_YXDBBX","operType":"3","reqHandle":"query","xdlx":"203","cxrq":"201409","ksrq":"01","jsrq":"31","dfhm":""},"dynamicDataNodeName":"qdcx"}]
				parm.put("jsonParam", "[{\"dynamicParameter\":{\"method\":\"cxFeePage\",\"busiNum\":\"CX_YXDBBX\",\"operType\":\"3\",\"reqHandle\":\"query\",\"xdlx\":\"203\",\"cxrq\":\""+startDate+"\",\"ksrq\":\"01\",\"jsrq\":\"31\",\"dfhm\":\"\"},\"dynamicDataNodeName\":\"qdcx\"}]");
				String text = cutil.post("http://www.gs.10086.cn/gs_obsh_service/actionDispatcher.do", h, parm);
				b= liuliangHistory_parse(text,startDate);
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
	
	public boolean liuliangHistory_parse(String text,String startDate)
	{
		boolean b = true;
		System.out.println(text);
		if(text != null && !text.equals("")){
		try{
			JSONObject json=new JSONObject(text);
			JSONObject jb = json.getJSONObject("qdcx");
			jb = jb.getJSONObject("resultObj");
			JSONArray jsonDetail=new JSONArray(jb.getString("detail"));
			List<MobileOnlineList> mobileOnlineList = new LinkedList<MobileOnlineList>();
			List<MobileOnlineBill> mobileBillList = new LinkedList<MobileOnlineBill>();
		
			if(jsonDetail !=null && jsonDetail.length()>0){
				

				String totalFlows = jb.getString("gprsTotalFlux").replaceAll("\\(KB\\)", "");
				String totalFee = jb.getString("wlanTotalFee").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("元", "");
				if(totalFlows.equals("")){
					totalFlows = "0";
				}
				Double flow = Double.parseDouble(totalFlows);
				if(totalFee.equals("")){
					totalFee = "0.00";
				}
				
				//账单
				MobileOnlineBill onlineBill = new MobileOnlineBill();
				onlineBill.setTotalFlow(Math.round(flow));
				onlineBill.setTrafficCharges(new BigDecimal(totalFee));
				onlineBill.setPhone(login.getLoginName());
				onlineBill.setIscm(0);
				onlineBill.setMonthly(DateUtils.StringToDate(startDate, "yyyyMM"));
				saveMobileOnlineBill(mobileBillList);
				
				//详单
				for(int i=0;i<jsonDetail.length();i++){  
					
					String  jsonobject = jsonDetail.getString(i);
					String[] ss = jsonobject.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "").split(",");
					
					String starttime = startDate.substring(0,4)+"-"+ss[0];
					String allTotalFee = ss[6].trim();
					if(allTotalFee.equals("")){
						allTotalFee = "0.0";
					}
					String area = ss[1];
					String duration = ss[3];
					String accessPoint = "2/3G";
					String data = ss[4];
					String cheapService = ss[5];
					
					long onlinetime=0;
					BigDecimal communicationFees=new BigDecimal("0.0");;
					long totalFlow=0;
					Date times=null;
					times=DateUtils.StringToDate( starttime, "yyyy-MM-dd HH:mm:ss");	
					try{
						onlinetime = StringUtil.flowTimeFormat(duration);
						communicationFees = new BigDecimal(allTotalFee);
						totalFlow = Math.round(StringUtil.flowFormat(data));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					MobileOnlineList datalist = new MobileOnlineList();
						datalist.setOnlineTime(onlinetime);
						datalist.setPhone(login.getLoginName());
						datalist.setOnlineType(accessPoint);
						datalist.setTotalFlow(totalFlow);
						datalist.setTradeAddr(area);
						datalist.setcTime(times);
						datalist.setCommunicationFees(communicationFees);
						datalist.setCheapService(cheapService);
						
						mobileOnlineList.add(datalist);	
				}
				
				try
				{
					if(mobileOnlineList!=null&&mobileOnlineList.size()>0)
					{
						saveMobileOnlineList(mobileOnlineList);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			} else {
					MobileOnlineBill onlineBill = new MobileOnlineBill();
					onlineBill.setTotalFlow((long) 0);
					onlineBill.setTrafficCharges(new BigDecimal(0.00));
					onlineBill.setPhone(login.getLoginName());
					onlineBill.setIscm(0);
					onlineBill.setMonthly(DateUtils.StringToDate(startDate, "yyyyMM"));
					saveMobileOnlineBill(mobileBillList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
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
			liuliangHistory();
			break;
		case 3:
			getTelDetailHtml();//通话记录
			getMyInfo(); //个人信息
			callHistory(); //历史账单	
			messageHistory(); //历史短信记录
			liuliangHistory();
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
			
			map.put(CommonConstant.status, status);
			map.put(CommonConstant.errorMsg, errorMsg);
			if(status==1){
				sendPhoneDynamicsCode();
				addTask_1(this);
	    	}else{
	    		map.put("url",getAuthcode());
	    	}
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;
	}
	
	
	public String login1(){
		try {
			
				Map<String, String> dxMap = new LinkedMap();
				dxMap.put("checkbox", "false");
				dxMap.put("fromFlag", "doorPage");
				dxMap.put("icode", login.getAuthcode());
				dxMap.put("isHasV", "false");
				dxMap.put("loginType", "myjsmcc");
				dxMap.put("mobile", login.getLoginName());
				dxMap.put("password", login.getPassword());
				
				CHeader c = new CHeader(CHeaderUtil.Accept_,index,null,"gs.ac.10086.cn",true);
			
				String  text = cutil.post("https://gs.ac.10086.cn/popDoorPopLogonNew", c, dxMap);
				//System.out.println(text);
				if(text!=null){
					if(text.contains("rcode:\"-7\"")){
						return "验证码错误.";
					}else if(text.contains("rcode:'-1020'")){
						return "密码错误.";
					}else if(text.contains("rcode:\"1000\"")){
						c = new CHeader(CHeaderUtil.Accept_,"http://www.gs.10086.cn",null,"www.gs.10086.cn",true);
						c.setRespCharset("UTF-8");
						 text = cutil.get("https://gs.ac.10086.cn/gsauth/ssoCookieJsonp?_1410226318678=", c);
						// System.out.println(text);
						 dxMap = new LinkedMap();
						 dxMap.put("jsonParam", "[{\"dynamicURI\":\"/userCurrentFee\",\"dynamicParameter\":{\"method\":\"queryBalance\",\"reqHandle\":\"query\",\"busiNum\":\"CX_DQYE\"},\"dynamicDataNodeName\":\"API_balInfo\"}]");
						 c = new CHeader(CHeaderUtil.Accept_,"http://www.gs.10086.cn/my/myAccount.html",CHeaderUtil.Content_Type__urlencoded,"www.gs.10086.cn",true);
						 c.setRespCharset("UTF-8");
						text = cutil.post("http://www.gs.10086.cn/gs_obsh_service/actionDispatcher.do", c, dxMap);
						if(text!=null&&text.contains(login.getLoginName())){
							Map<String,Object> jsmap = (Map<String, Object>) redismap.get("jsmap");
							RegexPaserUtil rp = new RegexPaserUtil("balance\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
							String balance = rp.getText();
							jsmap.put("balance", balance);
							return "1";
						}
						
					}
					
				}	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
	}
	
	
	

	// 随机短信登录
	public Map<String,Object> checkPhoneDynamicsCode() {
			Map<String, String> parMap = new LinkedMap();
			parMap.put("jsonParam", "	[{\"dynamicParameter\":{\"method\":\"InitFeePage\",\"busiNum\":\"CX_YXD\",\"operType\":\"3\",\"reqHandle\":\"query\",\"smsNum\":\""+login.getPhoneCode()+"\"},\"dynamicDataNodeName\":\"qdcxBusiness\",\"dynamicURI\":\"\",\"dynamicPriority\":1}]");
			CHeader c = new CHeader(CHeaderUtil.Accept_,"http://www.gs.10086.cn/service/control.html",CHeaderUtil.Content_Type__urlencoded,"www.gs.10086.cn",true);
			String  text = cutil.post("http://www.gs.10086.cn/gs_obsh_service/actionDispatcher.do",c,parMap);
			if(text!=null){
				if(text.contains("\"resultCode\":\"0\"")){
					errorMsg = "您输入的查询验证码错误或过期，请重新核对或再次获取！";
				}else if(text.contains("\"resultCode\":\"1\"")){
					errorMsg= "登陆成功!";
					status=1;
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
		int status = 0;
		CHeader c = new CHeader(CHeaderUtil.Accept_,"",null,"www.gs.10086.cn",true);
		
		try {
			Map<String, String> parMap = new LinkedMap();
			parMap.put("busiNum", "CX_YXD");
			String text = cutil.post("http://www.gs.10086.cn/gs_obsh_service/sms.do",c,parMap);
		
			if(text!=null && text.contains("\"resultCode\":\"1\"")){
				errorMsg="发送成功";
				status = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(errorMsg==null){
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg ,errorMsg);
		return map;
	}

	
	

	
	

	
	

}
