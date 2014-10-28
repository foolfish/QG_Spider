package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
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
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

public class TJDianxin  extends BaseInfoMobile{
	public static Logger log = Logger.getLogger(TJDianxin.class);

	public String index_text = "tjdianxin_index_text";
	public String price = "tjdianxin_price";
	public String tj_realname = "tjdianxin_realname";
	// 验证码图片路径					 
	public static String imgurl  ="https://uam.ct10000.com/ct10000uam/validateImg.jsp";


	public TJDianxin(Login login,String currentUser) {
		super( login, ConstantNum.comm_tj_dx,currentUser);
	}

	/**
	 * 查询个人信息
	 */
	public boolean getMyInfo() {
		try {
			
			String myinfo = cutil.get("http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo");
			if(myinfo!=null){
				Document doc3 = Jsoup.parse(myinfo);
				String lxdh = doc3.select("input[id=phoneNumber]").first().attr("value");
				 String lxdz = doc3.select("textarea[id=address]").first().attr("value");
				 String realname = doc3.select("input[name=realName]").first().attr("value");
				 redismap.put(tj_realname,realname);
				 String idCard = doc3.select("input[name=certificateNumber]").first().attr("value");
				 String email = doc3.select("input[id=email]").first().attr("value");
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("parentId", currentUser);
				map1.put("usersource", Constant.DIANXIN);
				map1.put("loginName", login.getLoginName());
				List<User> list = userService.getUserByParentIdSource(map1);
				BigDecimal yue = new BigDecimal(redismap.get(price).toString());
				if (list != null && list.size() > 0) {
					User user = list.get(0);
					user.setLoginName(login.getLoginName());
					user.setLoginPassword("");
					user.setUserName(realname);
					user.setRealName(realname);
					user.setIdcard(idCard);
					user.setAddr(lxdz);
					user.setUsersource(Constant.DIANXIN);
					user.setEmail(email);
					user.setParentId(currentUser);
					user.setModifyDate(new Date());
					user.setPhone(login.getLoginName());
					user.setFixphone(lxdh);
					
					user.setPhoneRemain(yue);
					userService.update(user);
				} else {
					User user = new User();
					UUID uuid = UUID.randomUUID();
					user.setId(uuid.toString());
					user.setLoginName(login.getLoginName());
					user.setLoginPassword("");
					user.setUserName(realname);
					user.setRealName(realname);
					user.setIdcard(idCard);
					user.setAddr(lxdz);
					user.setUsersource(Constant.DIANXIN);
					user.setParentId(currentUser);
					user.setModifyDate(new Date());
					user.setPhone(login.getLoginName());
					user.setFixphone(lxdh);
					user.setPhoneRemain(yue);
					user.setEmail(email);
					userService.saveUser(user);
				}
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			writeLogByInfo(e);
		}

		return false;
	}

	public void getRealTimeFee(){
		//实时话费查询
		try{
			Map<String,String> parmap=new HashMap<String,String>();
			parmap.put("requestFlag","asynchronism");
			parmap.put("shijian", new Date().toString());
			String test=cutil.post("http://tj.189.cn/tj/service/bill/realtimeFeeQuery.action",parmap);
			JSONObject json=new JSONObject(test);
			String realTimeFee=json.getString("realtimeFee");
			String nowTime=json.getString("nowTime");
			DianXinTel tel = new DianXinTel();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String year = nowTime.substring(0, 4);
			String mouth = nowTime.substring(5, 7);
			String fday=TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
			String eday=TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
			tel.setcTime(sdf.parse(nowTime.substring(0, 8)+"01 00:00:00"));
			tel.setTeleno(this.loginName);
			tel.setcAllPay(new BigDecimal(realTimeFee));
			tel.setDependCycle(fday+"至"+eday);
			Map map=new HashMap();
			map.put("cTime", tel.getcTime());
			map.put("teleno", this.loginName);
			List list=dianXinTelService.getDianXinTelBybc(map);
			int update=0;
			if(list==null || list.size()==0){
				tel.setId(UUID.randomUUID().toString());
				dianXinTelService.saveDianXinTel(tel);
			}
			else{
				DianXinTel oldBill=(DianXinTel) list.get(0);
				tel.setId(oldBill.getId());
				dianXinTelService.update(tel);
			}
		}catch(Exception e){
			writeLogByZhangdan(e);
		}
	}
	
	public boolean getTelDetailHtml() {
		
		try {
			//可查六个月,当月的就不查了
			List<String> ms = DateUtils.getMonths(7,"yyyy年MM月");
			DianXinTel maxTel = new DianXinTel();
			maxTel.setTeleno(login.getLoginName());
			maxTel = dianXinTelService.getMaxTime(maxTel);
			Map<String, String> param = new HashMap<String, String>();
			String text = null;
			Date d = null;
			String realName = (String)redismap.get(tj_realname);
			for (int j = 1; j < ms.size(); j++) {
				String starDate = ms.get(j) ;
				param.put("billingCycle1", starDate);
				d = DateUtils.StringToDate(starDate, "yyyy年MM月");
				if(maxTel!=null&&maxTel.getcTime().getTime()>=d.getTime()){
					break;
				}
				text = cutil.post("http://tj.189.cn/tj/service/bill/queryBillInfo.action", param);
				if(text!=null && text.contains("acctItemFee")&& text.contains("billFee")){
					JSONObject jObj=new JSONObject(text);	
					//JSONObject billItemList = jObj.getJSONObject("billItemList");
					JSONArray billItemList = jObj.getJSONArray("billItemList");
					if(billItemList.length()>0){
						JSONObject jsonobject = billItemList.getJSONObject(0);  
						JSONArray acctItems = jsonobject.getJSONArray("acctItems");
						
						String billFee =  jsonobject.getString("billFee");
						BigDecimal myf=new BigDecimal("0.00");
						BigDecimal tcf=new BigDecimal("0");
						BigDecimal hj=new BigDecimal(billFee);
						for(int i=0;i<acctItems.length();i++){  
							JSONObject acctItemObj = acctItems.getJSONObject(i);  
							String acctItemName =  acctItemObj.getString("acctItemName");
							String acctItemFee =  acctItemObj.getString("acctItemFee");
							if(acctItemName!=null&& acctItemName.contains("套餐费")){
								tcf=new BigDecimal(acctItemFee);
							}else if(acctItemName!=null&& acctItemName.contains("国内漫游")){
								myf=new BigDecimal(acctItemFee);
							}	
						}
				 
						DianXinTel tel = new DianXinTel();
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
//						tel.setBaseUserId(currentUser);
						tel.setcTime(DateUtils.StringToDate(starDate, "yyyy年MM月"));
						tel.setTeleno(login.getLoginName());
						tel.setcAllPay(hj);
						
						String year = starDate.substring(0, 4);
						String mouth = starDate.substring(5, 7);
						String fday=TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
						String eday=TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
						
						tel.setDependCycle(fday+"至"+eday);
						tel.setcName(realName);
						tel.setZtcjbf(tcf);
						tel.setMythf(myf);
						dianXinTelService.saveDianXinTel(tel);
					}
					
				}
			
			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
			return false;
		}
	
		return true;
	}

	public String formatDateMouth(String m ){
		if(m!=null&&m.length()==2){
			String fix1 = m.substring(0, 1);
			String fix2 = m.substring(1, 2);
			if(fix1.equals("0")){
				return fix2;
			}
			return m;
		}
		return null;
	}
	
	
	//Created by Dongyu.Zhang
	/**
	 * 查询流量详单
	 */
	public boolean flowHistory(){
		List<DianXinFlowDetail> list = new ArrayList<DianXinFlowDetail>();
		List<DianXinFlow> list2 = new ArrayList<DianXinFlow>();
		String url = null;
		try{
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			DianXinFlowDetail flow = new DianXinFlowDetail();
			flow.setPhone(login.getLoginName());
			flow = dianXinFlowDetailService.getMaxTime(flow);
			String text = null;
			int num = 0;
			int count = 0;
			for(int i = 0; i < ms.size(); i++){
				
				DianXinFlow flowBill = new DianXinFlow();
				UUID uuid2 = UUID.randomUUID();
				flowBill.setId(uuid2.toString());
				flowBill.setPhone(login.getLoginName());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				Date monthly = sdf.parse(ms.get(i));
				flowBill.setQueryMonth(monthly);
				
				
				String starDate = ms.get(i) ;
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday=TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				String eday=TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				url = "http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=7&startTime="+fday+"&endTime="+eday+"&pageNo=1&pageSize=50&totalPage=0";
				text = cutil.get(url);
				if(null != text){
					if(text.contains("上网及数据通信详单")){
						num = flowHistory_parse(text, flow, true, list, i, flowBill);
						count = num;
						if(num == -2){
							break;
						}
						for (int j = 2; j <= count; j++) {
							url = "http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=7&startTime="+fday+"&endTime="+eday+"&pageNo="+j+"&pageSize=50&totalPage=0";
							text = cutil.get(url);
							if(text!=null&&text.contains("上网及数据通信详单")){
								num = flowHistory_parse(text, flow, false, list, i, flowBill);
								if(num==-2){
									break;
								}
							}
						}
					}
				}
				list2.add(flowBill);
			}
			dianXinFlowService.insertbatch(list2);
			dianXinFlowDetailService.insertbatch(list);
		}catch(Exception e){
			writeLogByHistory(e);
			return false;
		}
		return true;
	}
	
	//Created by Dongyu.Zhang
		/**
		 * 查询短信记录
		 */
		public boolean messageHistory(){
			String url = null;
			try{
				List<String> ms = DateUtils.getMonths(12, "yyyyMM");
				TelcomMessage message = new TelcomMessage();
				message.setPhone(login.getLoginName());
				message = telcomMessageService.getMaxSentTime(message.getPhone());
				String text = null;
				int num = 0;
				int count = 0;
				for (int i = 0; i < ms.size(); i++){
					String starDate = ms.get(i) ;
					String year = starDate.substring(0, 4);
					String mouth = starDate.substring(4, 6);
					String fday=TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
					String eday=TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
					url = "http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=2&startTime="+fday+"&endTime="+eday+"&pageNo=1&pageSize=50&totalPage=0";
					text = cutil.get(url);
					if(null != text){
						//http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=2&startTime=2014-09-01&endTime=2014-09-11&pageNo=1&pageSize=50&totalPage=0
						if(text.contains("发送号码")&&text.contains("接收号码")){
							num = messageHistory_parse(text, message, true);
							count = num;
							if(num == -2){
								break;
							}
							for (int j = 2; j <= count; j++) {
								url = "http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=2&startTime="+fday+"&endTime="+eday+"&pageNo="+j+"&pageSize=50&totalPage=0";
								text = cutil.get(url);
								if(text!=null&&text.contains("发送号码")&&text.contains("接收号码")){
									num = messageHistory_parse(text, message, false);
									if(num==-2){
										break;
									}
								}
							}
						}
					}
					
				}
			}catch(Exception e){
				writeLogByHistory(e);
				return false;
			}
			return true;
		}
	
		
	//Created by Dongyu.Zhang
	/**
	 * @param text	 响应的正文信息
	 * @param detail 数据库保存的最大时间
	 * @return -1错误,1正常,其他当前页,count=-2;数据已存在
	 */
	private int flowHistory_parse(String text, DianXinFlowDetail flow, boolean isPage1, List<DianXinFlowDetail> list, int month, DianXinFlow flowBill){
		int count = 1;
		Date d = null;
		Double flowNum = 0.0;
		Double flowTime = 0.0;
		Double flowFee = 0.0;
		try{
			flowNum = flowBill.getAllFlow().doubleValue();
		}catch(Exception e){}
		try{
			flowTime = flowBill.getAllTime().doubleValue();
		}catch(Exception e){}
		try{
			flowFee = flowBill.getAllPay().doubleValue();
		}catch(Exception e){}
		
		try{
			Document doc = Jsoup.parse(text);
			if(isPage1){
				 try{
					 Element table2 = doc.select("table[class=table-form-21]").get(0);
					 Elements trs2 = table2.select("tr");
					 String flowTimeScale = trs2.get(2).select("td").get(0).text().trim().substring(5);
					 if(month == 0){
						 try{
							 Date currentTime = new Date();
							 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							 String endDay = formatter.format(currentTime);
							 
							 Calendar c = Calendar.getInstance();
							 c.setTime(currentTime);
							 c.set(Calendar.DAY_OF_MONTH, 1);
							 Date firstDay = c.getTime();
							 String startDay = formatter.format(firstDay);
							 
							 String currentScale = startDay + "-" + endDay;
							 
							 flowBill.setDependCycle(currentScale);
						 }catch(Exception e){
							 flowBill.setDependCycle(flowTimeScale);
						 }
						 
					 }else{
						 flowBill.setDependCycle(flowTimeScale);
					 }
					 
					 
					 
					 count = Integer.parseInt(doc.getElementById("pages").text().replace("共", "").replace("页","").trim());
				 }catch(Exception e){
					 count = 1;
				 }
			 }
			 Element table1 = doc.select("table[class=table-form-21]").get(1);
			 Elements trs = table1.select("tr");
			 Elements tds = null;
			 for (int j = 1; j < trs.size(); j++) {
					tds = trs.get(j).select("td");
					String kssj = tds.get(0).text();
					//目前时长在网站上是空
					String sc = tds.get(1).text();
					String ll = tds.get(2).text();
					String wllx = tds.get(3).text();//3G
					String lllx = tds.get(4).text();//流量类型：省际
					//目前业务类型在网站上是空
					String ywlx = tds.get(5).text();
					String fy = tds.get(6).text();//都是0.00元
					d = DateUtils.StringToDate(kssj, "yyyy-MM-dd HH:mm:ss");
					if(flow!=null&&d.getTime()<=flow.getBeginTime().getTime()){
						count = -2;
						break;
					}
					DianXinFlowDetail dFlow = new DianXinFlowDetail();
					UUID uuid = UUID.randomUUID();
					dFlow.setId(uuid.toString());
					dFlow.setBeginTime(d);
					Double llDouble = StringUtil.flowFormat(ll);
					
					try{
						flowNum += llDouble;
						flowFee += Double.parseDouble(fy);
						flowTime += Double.parseDouble(sc);
					}catch(Exception e){}
					
					
					dFlow.setFlow(new BigDecimal(llDouble));
					dFlow.setPhone(login.getLoginName());
					dFlow.setNetType(wllx);
					if(month == 0){
						dFlow.setIscm(1);
					}else{
						dFlow.setIscm(0);
					}
					try{
						dFlow.setTradeTime(Long.parseLong(sc));
					}catch(Exception e){}
					dFlow.setBusiness(ywlx);
					try{
						dFlow.setFee(new BigDecimal(fy));
					}catch(Exception e){}
					list.add(dFlow);
				 }
			 flowBill.setAllFlow(new BigDecimal(flowNum));
			 flowBill.setAllTime(new BigDecimal(flowTime));
			 flowBill.setAllPay(new BigDecimal(flowFee));
			
		}catch(Exception e){
			 count = -1;
			 log.error("流量记录解析失败!"+login.getLoginName());
		 }
		 return count;
		
		
	}
		
		
	//Created by Dongyu.Zhang
	/**
	 * @param text	 响应的正文信息
	 * @param detail 数据库保存的最大时间
	 * @return -1错误,1正常,其他当前页,count=-2;数据已存在
	 */
	private int messageHistory_parse(String text, TelcomMessage message,boolean isPage1){
		int count = 1;
	     Date d = null;
		 
		// System.out.println(doc);
		 try{
			 Document doc  = Jsoup.parse(text);
			 if(isPage1){
				 try{
				   count = Integer.parseInt(doc.getElementById("pages").text().replace("共", "").replace("页","").trim());
				 }catch(Exception e){
					 count = 1;
				 }
			 }
			 Element table1 = doc.select("table[class=table-form-21]").get(0);
			 Elements trs = table1.select("tr");
			 Elements tds = null;
			 for (int j = 1; j < trs.size(); j++) {
				tds = trs.get(j).select("td");
				String fshm= tds.get(0).text();
				String jshm= tds.get(1).text();
				String fssj= tds.get(2).text();
				String fy= tds.get(3).text();
				String yhfy= tds.get(4).text();
				d = DateUtils.StringToDate(fssj, "yyyy-MM-dd HH:mm:ss");
				if(message!=null&&d.getTime()<=message.getSentTime().getTime()){
					count = -2;
					break;
				}
				TelcomMessage tMessage=new TelcomMessage();
				UUID uuid = UUID.randomUUID();
				tMessage.setId(uuid.toString());
				tMessage.setSentTime(d);
				tMessage.setPhone(fshm);
				tMessage.setRecevierPhone(jshm);
				tMessage.setAllPay(Double.parseDouble(fy));
				Date now = new Date(); 
				tMessage.setCreateTs(now);
				tMessage.setBusinessType("发送");
				telcomMessageService.save(tMessage);	
			 } 
		 }catch(Exception e){
			 count = -1;
			 log.error("短信记录解析失败!"+login.getLoginName());
		 }
		 return count;
	}
	
	/**
	 * 查询通话记录
	 */
	public boolean callHistory(){
		String url = null;
		try {
			List<String> ms =  DateUtils.getMonths(12,"yyyyMM");
			DianXinDetail detail = new DianXinDetail();
			detail.setPhone(login.getLoginName());
			detail = dianXinDetailService.getMaxTime(detail);
			String text = null;
			int num = 0;
			int count = 0;
			//天津电信默认从当月的前一个月开始查询
			for (int i = 0; i < ms.size(); i++) {
				String starDate = ms.get(i) ;
				String year = starDate.substring(0, 4);
				String mouth = starDate.substring(4, 6);
				String fday=TimeUtil.getFirstDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
				String eday=TimeUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(formatDateMouth(mouth)));
//				http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=1&startTime="+fday+"&endTime="+eday+"&pageNo=1&pageSize=10000&totalPage=0
				//以后可改成线程池
				url = "http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=1&startTime="+fday+"&endTime="+eday+"&pageNo=1&pageSize=50&totalPage=0";
				text = cutil.get(url);
				if(text!=null){
					if(text.contains("呼叫类型")&&text.contains("通话地点")){
						num = callHistory_parse(text,detail,true);
						count = num;//总页数
						if(num==-2){
							break;
						}
						for (int j = 2; j <= count; j++) {
							url = "http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=1&startTime="+fday+"&endTime="+eday+"&pageNo="+j+"&pageSize=50&totalPage=0";
							text = cutil.get(url);
							if(text!=null&&text.contains("呼叫类型")&&text.contains("通话地点")){
								num = callHistory_parse(text,detail,false);
								if(num==-2){
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			writeLogByHistory(e);
			return false;
		}
		return true;
	}
	/**
	 * @param text	 响应的正文信息
	 * @param detail 数据库保存的最大时间
	 * @return -1错误,1正常,其他当前页,count=-2;数据已存在
	 */
	private int callHistory_parse(String text,DianXinDetail detail,boolean isPage1){
		int count = 1;
	     Date d = null;
		 Document doc  = Jsoup.parse(text);
		// System.out.println(doc);
		 try{
			 if(isPage1){
				 try{
				   count = Integer.parseInt(doc.getElementById("pages").text().replace("共", "").replace("页","").trim());
				 }catch(Exception e){
					 count = 1;
				 }
			 }
			 Element table1 = doc.select("table[class=table-form-21]").get(0);
			 Elements trs = table1.select("tr");
			 Elements tds = null;
			 for (int j = 1; j < trs.size(); j++) {
				tds = trs.get(j).select("td");
				String zjhm= tds.get(0).text();
				String dfhm= tds.get(1).text();
				String hjlx= tds.get(2).text();
				String thdd= tds.get(3).text();
				String qssj= tds.get(4).text();
				String thsc= tds.get(5).text();
				String fy= tds.get(6).text();
				String yhfy= tds.get(6).text();
				d = DateUtils.StringToDate(qssj, "yyyy-MM-dd HH:mm:ss");
				if(detail!=null&&d.getTime()<=detail.getcTime().getTime()){
					count = -2;
					break;
				}
				DianXinDetail dxDetail = new DianXinDetail();
				UUID uuid = UUID.randomUUID();
				dxDetail.setId(uuid.toString());
				dxDetail.setcTime(d);
			
				dxDetail.setTradeTime(Integer.parseInt(thsc));
				dxDetail.setTradeAddr(thdd);
				//dxDetail.setTradeType(hjlx);
				dxDetail.setCallWay(hjlx);
				dxDetail.setRecevierPhone(dfhm);
				//dxDetail.setBasePay(new BigDecimal(jbthf));
				//dxDetail.setLongPay(new BigDecimal(ctthf));
				dxDetail.setAllPay(new BigDecimal(fy));
				dxDetail.setPhone(login.getLoginName());
				dxDetail.setIscm(0);
				dianXinDetailService.saveDianXinDetail(dxDetail);	
			 } 
		 }catch(Exception e){
			 count = -1;
			 log.error("通话记录解析失败!"+login.getLoginName());
		 }
		 return count;
	}

	public static void main(String[] args) {
		TJDianxin tj = new TJDianxin(new Login("15342164109","888666"),null);
//		TJDianxin tj = new TJDianxin(new Login("15342164108","888666"),null);
		tj.index();
		tj.inputCode(imgurl);
		tj.login();
		tj.close();
//		tj = new TJDianxin(new Login("15342164108","888666"),null,null);
////		tj.test();
//		tj.index();
//		tj.inputCode(imgurl);
//		tj.login();
//		tj.close();
	}
	public void test(){
		String text = cutil.get("http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=1&startTime=2014-07-01&endTime=2014-07-31&pageNo=1&pageSize=10000&totalPage=0");
		
	}
	
	
	
	
	
//	public Map<String,Object> index(){
//		map.put("url",getAuthcode());
//		return map;
//	}
	/* (non-Javadoc)
	 * @see com.lkb.thirdUtil.base.BaseInfo#init()
	 */
	public void init(){
		if(!isInit()){
			String reqUrl = "https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=172.16.10.20";
			String text = cutil.get(reqUrl);
			if(text!=null){
				redismap.put(index_text, text);
				setImgUrl(imgurl);
				setInit();
			}
		}
	}
	public  Map<String,Object> login(){
		//第一步
		try{
			String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
			Map<String,String> param = new HashMap<String,String>();
			param.put("username", login.getLoginName()); 
			String text=  cutil.post(url, param);
			if(text!=null){
				login_1(text);
			}
		}catch(Exception e){
			writeLogByLogin(e);
		}
//		map.put("state", state);
//    	map.put("msg", msg==null?"登陆失败,请确认账户密码是否正确!":msg);
//    	map.put("url", getAuthcode());
    	if(status==1){
        	addTask(this);
    	}
    	return map;
	}
	
	private  void login_1(String ss){
		try{
			String s[] = ss.split("\\|");
			String customFileld02 = s[0];
			String areaname = s[1];
			String text =(String)redismap.get(index_text);
		  if(text.length()<300){
			//此处是特殊情况处理,主要是redis有记录,或者cookie有记录,会有出现此处连接的地方,需要注意
			 // System.out.println(text);
			  if(text.contains("location.replace(")){
				    String pr = (String)redismap.get(price);
				    if(pr==null){
				    	 text = cutil.get("http://tj.189.cn/tj/service/bill/balanceQuery.action");
						Document doc = Jsoup.parse(text);
						pr = doc.getElementById("balanceFeeSpan").html();
				    }
					if(pr!=null){
						redismap.put(price,pr);
						loginsuccess();
					}
			  }
		  }else{
			  Document doc = Jsoup.parse(text);
				String empoent = doc.select("input[id=forbidpass]").first().val();
				String forbidaccounts = doc.select("input[id=forbidaccounts]").first().val();
				String authtype = doc.select("input[name=authtype]").first().val();
				
				
				String customFileld01 = "3";
				String lt = doc.select("input[name=lt]").first().val();
				String _eventId = doc.select("input[name=_eventId]").first().val();
				String open_no = "c2000004";
			
				String action = doc.select("form[id=c2000004]").first().attr("action");
				String phone1 = null;
				String password1 = null;
				//-----------加密
					//pwd, digit, f, s
				try{
					String n = StringUtil.subStr("strEnc(username,", ");", text).trim();
					String[] stra = n.trim().replaceAll("\'", "").split(",");
					phone1 = AbstractDianXinCrawler.executeJsFunc("des/tel_com_des.js", "strEnc", login.getLoginName(), stra[0], stra[1], stra[2]);
					password1 =AbstractDianXinCrawler.executeJsFunc("des/tel_com_des.js", "strEnc", login.getPassword(), stra[0], stra[1], stra[2]);
				}catch(Exception e){
					phone1 = login.getLoginName();
					password1 =login.getPassword();
				}
				action = URLDecoder.decode(action);
				
				String url = "https://uam.ct10000.com"+action;
				CHeader h = new CHeader("https://uam.ct10000.com/ct10000uam/login?service=http://189.cn/dqmh/Uam.do?method=loginJTUamGet&returnURL=1&register=register2.0&UserIp=114.249.55.149");
				Map<String,String> param = new LinkedHashMap<String,String>();
				param.put("empoent", empoent); 
				param.put("forbidaccounts",forbidaccounts); 
				param.put("authtype",authtype); 
				param.put("customFileld02", customFileld02); 
				param.put("areaname", areaname); 
				param.put("customFileld01", customFileld01); 
				param.put("lt", lt); 
				param.put("_eventId", _eventId); 
				param.put("username", phone1); 
				param.put("password", password1); 
				param.put("randomId", login.getAuthcode()); 
				param.put("open_no", open_no); 
				text = cutil.post(url,h, param);
		    	//System.out.println(text);
				
		    	if(text.contains("https://uam.ct10000.com:443/ct10000uam-gate/?SSORequestXMLRETURN=")){
		    		text = cutil.post(text,h, param);
		    		RegexPaserUtil rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
		    		url = rp.getText();
	    			//http://189.cn/dqmh/Uam.do?method=loginJTUamGet&UATicket=35nullST--423854-4K2FlITjex4PVnWulrrd-ct10000uam
	    			 url = url.replace("http://189.cn/", "http://www.189.cn/");
	    			 CHeader c = new CHeader("http://www.189.cn/dqmh/Uam.do?method=loginJTUamGet&UATicket=");
	    			 text = cutil.get(url,c);
		    		 login_2();
		    	}else if(text.contains("http://uam.ct10000.com/ct10000uam/login?")){
		    		
		    	} else{
		    		errorMsg = Jsoup.parse(text).getElementById("status2").text();
		    	}
		  }
		}catch(Exception e){
			writeLogByLogin(e);
		}
    		
	}
	

	private void login_2() throws Exception{
		RegexPaserUtil rp = null;
		try{
			String text = cutil.get("http://www.189.cn/dqmh/frontLink.do?method=linkTo&toStUrl=http://tj.189.cn/tj/service/bill/balanceQuery.action");
			rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
			String url = rp.getText();
			text = cutil.get(url);
			rp = new RegexPaserUtil("location.replace\\('","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
			url = rp.getText();
			text = cutil.get(url);
			Document doc = Jsoup.parse(text);
			String ssoCallBack_ajax = doc.select("input[name=ssoCallBack_ajax]").first().val();
			String UATicket = doc.select("input[name=UATicket]").first().val();
			String IMurl = doc.select("input[name=IMurl]").first().val();
			String userType = doc.select("input[name=userType]").first().val();
			String toStUrl = doc.select("input[name=toStUrl]").first().val();
			Map<String,String> param = new LinkedHashMap<String,String>();
			param.put("ssoCallBack_ajax", ssoCallBack_ajax); 
			param.put("UATicket",UATicket); 
			param.put("IMurl",IMurl); 
			param.put("userType", userType); 
			param.put("toStUrl", toStUrl); 
			text = cutil.post("http://tj.189.cn/tj/ninclude/ssoCallBack.jsp", param);
			//System.out.println(text);
			text = cutil.get("http://tj.189.cn/tj/service/bill/balanceQuery.action");
			doc = Jsoup.parse(text);
			String pr = doc.getElementById("balanceFeeSpan").html();
			if(pr!=null){
				redismap.put(price,pr);
				loginsuccess();
			}
		}catch(Exception e){
			throw new Exception();
		}
	}		

	/** 0,采集全部
	 * 	1.采集手机验证
	 *  2.采集手机已经验证
	 * @param type
	 */
	public  void startSpider(){
		try{
			parseBegin(Constant.DIANXIN);
				getMyInfo(); //个人信息
				getTelDetailHtml();//通话记录
				getRealTimeFee();//实时话费=当月账单！切记放在后面！不然前6个月账单都不存了！
				flowHistory();//流量详单
				callHistory(); //历史账单
				messageHistory();//短信账单
		}finally{
			parseEnd(Constant.DIANXIN);
		}
	}
	
	
}
