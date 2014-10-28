package com.lkb.thirdUtil.yd;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.BaseInfoMobile;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.TimeUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

/**黑龙江移动
 * @author fastw
 * @date   2014-8-2 下午3:01:55
 */
public class HLJYidong extends BaseInfoMobile{
	public String CUST_NAME = "";
	public HLJYidong(Login login) {
		super(login);
	}
	
	public HLJYidong( Login login,
			IWarningService warningService,String currentUser) {
		super( login, warningService,ConstantNum.comm_hlj_yidong,currentUser);
	}
	public HLJYidong(Login login,String currentUser) {
		super(login,  ConstantNum.comm_hlj_yidong, currentUser);
	}
	
	@Override
    public void init(){
		setImgUrl("https://hl.ac.10086.cn/SSO/img");
		setInit();
	}
	@Override
    public Map<String,Object> login() {
		try{
			String text = login_0();
			if(text!=null){
			    //System.out.println(text);
				//https://hl.ac.10086.cn/SSO/clearAndLogin
				if(text.contains("您已经在其它地点或浏览器登录过，确定将已登录过的用户退出并重新登录么")){
					text = login_1(text);
				}
				if(text!=null){
					RegexPaserUtil rq = new RegexPaserUtil("var errorMsg=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
					String result = rq.getText();
					if(result!=null&&result.length()>0){
						errorMsg = result;
					}else{
						text = login_2(text);
						if(text!=null){
							text = cutil.get("http://www.hl.10086.cn//jsp/module/my/load_account_info.jsp");
							if(text!=null&&text.contains(login.getLoginName())){
								//System.out.println("=");
								loginsuccess();
								sendPhoneDynamicsCode();
								addTask_1(this);
							}
						}
					}
				}
			}
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;
	}
	
	public void saveTelData() {
		
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
				saveYuE();
				saveMonthTel(); //历史账单
				saveMessage1();//短信记录
				saveDetailTel(); //通话记录	
				getUserInfo();
				saveFlow();//流量记录
				saveCurrentMonthTel();
				break;
			case 2:
				getUserInfo();
				break;
			case 3:
				getUserInfo();
				saveMonthTel(); //历史账单
				saveFlow();//流量记录
				saveMessage1();//短信记录
				saveDetailTel(); //通话记录	
				break;
			default:
				break;
			}
		}finally{
			parseEnd(Constant.YIDONG);
		}
	}
	private String login_0(){
		String url = "https://hl.ac.10086.cn/login";
		Map<String,String> param = new HashMap<String,String>();
		param.put("username", login.getLoginName());
		param.put("password", login.getPassword());
		param.put("validateCode",login.getAuthcode());
		param.put("service", "ecare");
		param.put("style", "portal");
		param.put("submitMode", "login");
		param.put("fromCode", "sso");
		param.put("rememberNum", "false");
		param.put("getReadtime", "30");
		param.put("passwordType", "2");
		param.put("loginbutton", "登 录");
		return cutil.post(url,param);
	}
	private String login_1(String text){
		Document doc = Jsoup.parse(text);
		Elements el = doc.select("input[name=uid]");
		if(el!=null){
			String url = "https://hl.ac.10086.cn/SSO/clearAndLogin";
			String uid = el.val();
			Map<String, String> param = new HashMap<String, String>();
			param.put("passwordType", "2");
			param.put("uid", uid);
			param.put("service", "ecare");
			param.put("style", "portal");
			text = cutil.post(url,param);
		}
		return text;
	}
	private String login_2(String text){
		Document doc = Jsoup.parse(text);
		Elements el = doc.select("input[name=SAMLart]");
		if(el!=null){
			String SAMLart = el.val();
			String RelayState = doc.select("input[name=RelayState]").val();
			String PasswordType = doc.select("input[name=PasswordType]").val();
			String url = "http://www.hl.10086.cn/sso/ssoresponse.jsp?timeStamp="+System.currentTimeMillis();
			Map<String,String>param = new HashMap<String,String>();
			param.put("RelayState", RelayState);
			param.put("SAMLart", SAMLart);
			param.put("PasswordType", PasswordType);
		    text = cutil.post(url,param);
		    if(text!=null){
		    	doc = Jsoup.parse(text);
		    	String artifact = doc.select("input[name=artifact]").val();
		    	param = new HashMap<String,String>();
				param.put("RelayState", RelayState);
				param.put("artifact", artifact);
				url = "http://www.hl.10086.cn/sso/ssouserinfo.do";
				text = cutil.post(url,param);
		    }
		}
		return text;
	}
	private boolean saveYuE() {
		try {
			String loginName = login.getLoginName();
			Map<String, String> parmap = new HashMap<String, String>(3);
			parmap.put("parentId", currentUser);
			parmap.put("loginName", loginName);
			parmap.put("usersource", Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(parmap);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(loginName);
				user.setIdcard("");
				user.setAddr("");
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
				user.setIdcard("");
				user.setAddr("");
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
			String warnType = WaringConstaint.TJYD_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

		return false;
	}
	private BigDecimal getYuE() {
		BigDecimal phoneremain = new BigDecimal("0.00");
		try {
			String text = cutil.get("http://www.hl.10086.cn//jsp/module/my/load_account_info.jsp");
			if(text!=null&&text.contains(login.getLoginName())){
				RegexPaserUtil rp1 = new RegexPaserUtil("<p>账户余额：<span style=\"color:#ff0000; font-weight:bold\">",
						"</span>元", text, RegexPaserUtil.TEXTEGEXANDNRT);
				String balance = rp1.getText();
				phoneremain = new BigDecimal(balance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HLJYD_1;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return phoneremain;
	}
	
	/* （非 Javadoc）
	* <p>Title: sendPhoneDynamicsCode</p>
	* <p>Description: 发送手机短信验证码</p>
	* @author Jerry Sun
	* @return
	* @see com.lkb.thirdUtil.base.BaseInfo#sendPhoneDynamicsCode()
	*/
	@Override
    public Map<String,Object> sendPhoneDynamicsCode(){
		Map<String, String> param = new HashMap<String, String>();
		param.put("plevel", "6");
		param.put("logFlag", "0");
		String url = "http://www.hl.10086.cn/fg/sendOperateRandom.do";
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		String errorMsg = null;
		int status = 0;
		try {
			String text =  cutil.post(url, param);
			if(text.contains("ok")){
				status = 1;
				errorMsg = "发送成功";
			}else{
				errorMsg = "发送失败!";
			}
		} catch (Exception e) {
			errorMsg = "发送失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		return map;
	}
	
	/* （非 Javadoc）
	* <p>Title: checkPhoneDynamicsCode</p>
	* <p>Description: 校验手机 验证码</p>
	* @author Jerry Sun
	* @return
	* @see com.lkb.thirdUtil.base.BaseInfo#checkPhoneDynamicsCode()
	*/
	@Override
    public Map<String,Object> checkPhoneDynamicsCode(){
		String url = "http://www.hl.10086.cn/fg/validateOperateRandom.do";
		Map<String, String> param = new HashMap<String, String>();
		param.put("random", login.getPhoneCode());
		if(cutil.post(url, param).indexOf("ok") != -1){
			status = 1;
		}else{
			errorMsg = "验证失败!";
		}
		map.put(CommonConstant.status,status);
		map.put(CommonConstant.errorMsg,errorMsg);
		if(status==1){
	        addTask_2(this);
	    }
		return map;
		
	}
	
	/**
	* <p>Title: getUserInfo</p>
	* <p>Description: 抓取用户基本资料</p>
	* @author Jerry Sun
	*/
	private void getUserInfo(){
//		sendSmsForUserInfo();
		String url = "http://www.hl.10086.cn/service/info/userinfo/getInfo.do";
		Map<String, String> param = new HashMap<String, String>();
		param.put("random", "");
		String result = cutil.post(url, param);
		
		
		if(result.indexOf("开户时间") != -1){
			Document doc = Jsoup.parse(result);
			Elements tables = doc.select("table[class=bor zf sele]");
			if (tables.size() < 1) {
				return;
			}
			Element table = tables.first();
			String t1 = table.text().trim();
			String registDate = StringUtil.subStr("开户时间", "用户归属", t1);
			String realName = StringUtil.subStr("机主名称", "用户地址", t1);
			String addr = StringUtil.subStr("用户地址", "证件名称", t1);
			String idCard = StringUtil.subStr("证件号码", "合同人", t1);
			
			String loginName = login.getLoginName();
			Map<String, String> parmap = new HashMap<String, String>(3);
			parmap.put("parentId", currentUser);
			parmap.put("loginName", loginName);
			parmap.put("usersource", Constant.YIDONG);
			List<User> list = userService.getUserByParentIdSource(parmap);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);
				user.setRealName(realName);
				user.setIdcard(idCard);
				user.setAddr(addr);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setEmail("");
				user.setRegisterDate(DateUtils.StringToDate(registDate, "yyyy年MM月dd日"));
				userService.update(user);
			} else {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setId(uuid.toString());

				user.setLoginName(loginName);
				user.setLoginPassword("");
				user.setUserName(loginName);

				user.setRealName(realName);
				user.setIdcard(idCard);
				user.setAddr(addr);
				user.setUsersource(Constant.YIDONG);
				user.setParentId(currentUser);
				user.setModifyDate(new Date());
				user.setPhone(loginName);
				user.setFixphone("");
				user.setPhoneRemain(getYuE());
				user.setEmail("");
				user.setRegisterDate(DateUtils.StringToDate(registDate, "yyyy年MM月dd日"));
				userService.saveUser(user);
			}
		}

	}
	private boolean saveCurrentMonthTel(){
	    try{
	        String text = cutil.get("http://www.hl.10086.cn//jsp/module/my/load_account_info.jsp");
	        
	        MobileTel mobieTel = new MobileTel();
            UUID uuid = UUID.randomUUID();
            mobieTel.setId(uuid.toString());

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(System.currentTimeMillis()));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 1);
            Date time = cal.getTime();
            mobieTel.setcTime(time);
            mobieTel.setcName(CUST_NAME);
            mobieTel.setTeleno(login.getLoginName());
            // mobieTel.setBrand(brand);
            int begin = text.indexOf("本月已使用话费：");
            if(begin != -1){
                int end = text.indexOf("元",begin);
                mobieTel.setTcgdf(new BigDecimal(text.substring(begin+8,end)));
            }
            // mobieTel.setLdxsf(ldxsf);
            // mobieTel.setMgtjhyf(mgtjhyf);
            Integer year = cal.get(Calendar.YEAR);
            Integer m = cal.get(Calendar.MONTH)+1;
            String mouth = m>9?m.toString():"0"+m.toString();
            cal = Calendar.getInstance();
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            mobieTel.setDependCycle(year.toString()+"-"+mouth+"-01"
                    + "至 "+year.toString()+"-"+mouth+"-"+day.toString());
            mobileTelService.saveMobileTel(mobieTel);
	    }catch (Exception e) {
            e.printStackTrace();
            String warnType = WaringConstaint.HLJYD_2;
            WarningUtil wutil = new WarningUtil();
            wutil.warning(warningService, currentUser, warnType);
        }
	    return false;
	}
	
	private boolean saveMonthTel() {
		boolean flag = false;
		Map<String, String> param = new HashMap<String, String>();
		try {
			List<String> ms = DateUtils.getMonths(7, "yyyyMM");
			for (String s : ms) {
				//不能查询当月的账单
				if (DateUtils.isEqual(s)) {
					continue;
				}
				String starDate = s;
				param.put("busiType", s.toString().trim());
				param.put("flag", "");
				String url = "http://www.hl.10086.cn/service/fee/f_phone/querymonth/queryBillList.do";
				String text = cutil.post(url, param);
				if (text.contains("费用信息")) {
					RegexPaserUtil rp1 = new RegexPaserUtil(
							"<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"gb_tab1\">",
							"</table>", text, RegexPaserUtil.TEXTEGEXANDNRT);
					String tableString = "<table>" + rp1.getText() + "</table>";
					Document doc6 = Jsoup.parse(tableString);
					//System.out.println(doc6);
					Elements tables = doc6.select("table");
					Elements trs = tables.get(0).select("tr");
					BigDecimal tcgdf = new BigDecimal(0); // 套餐及固定费
					BigDecimal yytxf = new BigDecimal(0); // 语音通信费
					BigDecimal tcwdxf = new BigDecimal(0); // 套餐外短彩信费
					BigDecimal hj = new BigDecimal(0);
					for (int i = 1; i < trs.size() ; i++) {
						Element tr = trs.get(i);
						String trStr = tr.text();
						if (trStr.contains("套餐及固定费")) {
							// RegexPaserUtil rp = new
							// RegexPaserUtil("<td><strong><b>","</b></strong></td>",trStr,RegexPaserUtil.TEXTEGEXANDNRT);
							String tcgdfs = trStr.replaceAll("套餐及固定费", "").replaceAll("\\s*", "");
							try {
								if (tcgdfs != null) {
									tcgdf = new BigDecimal(tcgdfs);
								}
							} catch (Exception e) {
								//System.out.println("网页文本提取出错！套餐及固定费");
								e.printStackTrace();
							}
						} else if (trStr.contains("●语音通信费")) {
							String yytxfs = trStr.replaceAll("●语音通信费", "").replaceAll("\\s*", "");
							try {
								if (yytxfs != null) {
									yytxf = new BigDecimal(yytxfs);
								}
							} catch (Exception e) {
								//System.out.println("网页文本提取出错！语音通信费");
								e.printStackTrace();
							}

						} else if (trStr.contains("合计")) {
							String hjs = trStr.replaceAll("合计", "").replaceAll("\\s*", "");
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
					Map<String , Object> map2 = new HashMap<String , Object>();
					map2.put("phone", login.getLoginName());
					map2.put("cTime", DateUtils.StringToDate(s, "yyyyMM"));
					List list = mobileTelService.getMobileTelBybc(map2);
					if (list == null || list.size() == 0) {
						MobileTel mobieTel = new MobileTel();
						UUID uuid = UUID.randomUUID();
						mobieTel.setId(uuid.toString());
//						mobieTel.setBaseUserId(currentUser);
						mobieTel.setcTime(DateUtils.StringToDate(s, "yyyyMM"));
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

			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HLJYD_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return flag;
	}
	
	//Created by Dongyu.Zhang
	private boolean saveFlow(){
		List<MobileOnlineList> list = new ArrayList<MobileOnlineList>();
		List<MobileOnlineBill> billList = new ArrayList<MobileOnlineBill>();
		boolean flag = false;
		String flowUrl = "http://www.hl.10086.cn/service/fee/f_phone/particular/pfDetListNew.do";
		try{
			List<String> ms = DateUtils.getMonths(6, "yyyyMM");
			CHeader h = new CHeader();
			h.setAccept(CHeaderUtil.Accept_);
			h.setAccept_Language("zh-CN");
			h.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/7.0)");
			// 解压报文出错！
			// h.setAccept_Encoding("gzip, deflate");
			h.setHost("www.hl.10086.cn");
			h.setReferer("http://www.hl.10086.cn/service/fee/f_phone/particular/pfDet_search.jsp");
			h.setConnection("Keep-Alive");
			h.setCache_Control("no-cache");
			Map<String, String> param = new HashMap<String, String>();
			param.put("fee", "0");
			param.put("sDataType", "1");
			param.put("sListType", "75");
			param.put("sMul", "0");
			param.put("forwardStr", "detailQuery");
			param.put("flag", "");
			int m = 0;
			MobileOnlineBill flowBill;
			for(String s : ms){
				
				Double flowFees = 0.0;
				m++;
				String year = s.substring(0,4);
	 			String month = s.substring(4);
	 			String date = year+"年"+month+"月";
	 			param.put("sDate", date);
	 			flowBill = new MobileOnlineBill();
	 			MobileOnlineList mFlow;
				String text = cutil.post(flowUrl, h, param);
				Document doc = Jsoup.parse(text);
				System.out.println(doc.toString());
				if(m == 1){
					flowBill.setIscm(1);
				}else{
					flowBill.setIscm(0);
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				Date monthly = sdf.parse(s);
				flowBill.setMonthly(monthly);
				flowBill.setPhone(login.getLoginName());
				Elements tables = doc.select("table");
				if(tables==null||tables.toString().equals("")){
		        	flowBill.setChargeFlow((long) 0);
		        	flowBill.setFreeFlow((long) 0);
		        	flowBill.setTotalFlow((long) 0);
		        	flowBill.setTrafficCharges(new BigDecimal(0.0));
		        	billList.add(flowBill);
		        	saveMobileOnlineBill(billList);
					continue ;
				}
				try{
					String flowCycle = tables.get(2).select("tr").get(1).select("td").get(1).text();
					String paidFlowStr = tables.get(3).select("tr").get(2).select("td").get(1).text();
					Double paidFlowNum = Double.parseDouble(paidFlowStr.substring(4, paidFlowStr.indexOf('(')));
					String freeFlowStr = tables.get(3).select("tr").get(2).select("td").get(2).text();
					Double freeFlowNum = Double.parseDouble(freeFlowStr.substring(4, freeFlowStr.indexOf('(')));
					String allFlowStr = tables.get(3).select("tr").get(2).select("td").get(3).text();
					Double allFlowNum = Double.parseDouble(allFlowStr.substring(3, allFlowStr.indexOf('(')));
		        	flowBill.setChargeFlow(paidFlowNum.longValue());
		        	flowBill.setFreeFlow(freeFlowNum.longValue());
		        	flowBill.setTotalFlow(allFlowNum.longValue());
				
					Elements trs = tables.get(4).select("tr");
					for(int i = 2; i < trs.size(); i++){
						Elements tds = trs.get(i).select("td");
						if(tds.size() >= 8){
							String qssj = tds.get(0).text();//起始时间
							String txdd = tds.get(1).text();//通信地点
							String swfs = tds.get(2).text();//上网方式
							String sc = tds.get(3).text();//时长
							String ll = tds.get(4).text();//流量
							String flowNumStr = ll.substring(0, ll.indexOf('('));
							String zxtc = tds.get(5).text();//执行套餐
							String txf = tds.get(6).text();//通信费
							String wllx = tds.get(7).text();//网络类型
							String cz = tds.get(8).text();//操作
							try{
								Long flowTime = StringUtil.flowTimeFormat(sc);
								Double flowNum = Double.parseDouble(flowNumStr);
								Double flowFee = Double.parseDouble(txf);
								flowFees += flowFee;
								mFlow = new MobileOnlineList();
								mFlow.setPhone(login.getLoginName());
								mFlow.setCommunicationFees(new BigDecimal(flowFee));
								mFlow.setCheapService(zxtc);
								mFlow.setcTime(DateUtils.StringToDate(qssj, "yyyy/MM/dd HH:mm:ss"));
								mFlow.setOnlineTime(flowTime);
								mFlow.setOnlineType(wllx);
								mFlow.setTotalFlow(flowNum.longValue());
								mFlow.setTradeAddr(txdd);
								list.add(mFlow);
							}catch(Exception e){
								log.error("error",e);
							}
						}
					}
					flowBill.setTrafficCharges(new BigDecimal(flowFees));
					billList.add(flowBill);
				}catch(Exception e){}
				finally{
//				    flowBill.setTrafficCharges(new BigDecimal(flowFees));
//                    billList.add(flowBill);
				}
			}
			try{
				saveMobileOnlineBill(billList);
				saveMobileOnlineList(list);
			}catch(Exception e){
				log.error("error",e);
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("error",e);
			String warnType = WaringConstaint.HLJYD_5;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return flag;
	}
	
	
	//Created by Dongyu.Zhang
	private boolean saveMessage1(){
		boolean flag = false;
		String DetailTelUrl = "http://www.hl.10086.cn/service/fee/f_phone/particular/pfDetListNew.do";
		try{
			List<String> ms = DateUtils.getMonths(6, "yyyyMM") ;
			CHeader h = new CHeader();
			h.setAccept(CHeaderUtil.Accept_);
			h.setAccept_Language("zh-CN");
			h.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/7.0)");
			// 解压报文出错！
			// h.setAccept_Encoding("gzip, deflate");
			h.setHost("service.hl.10086.cn");
			h.setConnection("Keep-Alive");
			h.setCache_Control("no-cache");
			Map<String, String> param = new HashMap<String, String>();
			param.put("fee", "0");
			param.put("sDataType", "1");
			param.put("sListType", "74");
			param.put("sMul", "0");
			param.put("forwardStr", "detailQuery");
			for(String s: ms){
				String year = s.substring(0,4);
	 			String month = s.substring(4);
	 			String date = year+"年"+month+"月";
	 			param.put("sDate", date);
				String text = cutil.post(DetailTelUrl, h, param);
				//System.out.println(text);
				RegexPaserUtil rp1 = new RegexPaserUtil("</b></td>    </tr>","<tr style=\"font-weight:bold;font-size:12px;\">    <td height=\"30\"  align=\"left\">合计：</td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String table = rp1.getText(); 
				String tableString = "<table>"+table+"</table>";
				Document doc6 = Jsoup.parse(tableString);
				//System.out.println(doc6);
				Elements tables =doc6.select("table");
				Elements trs = tables.get(0).select("tr");
				for(int i = 0 ; i<trs.size();i++){
					Elements tds = trs.get(i).select("td");
					if(tds.size()>5){
						String sfsj = tds.get(0).text();
						String dfhm = tds.get(1).text();
						String txfs = tds.get(2).text();
						String ywlx = tds.get(3).text();
						String txf = tds.get(5).text();
						Date sendTime = DateUtils.StringToDate(sfsj, "yyyy/MM/dd HH:mm:ss");
						Map map2 = new HashMap();
						 map2.put("phone", login.getLoginName());
						 map2.put("sentTime", sendTime);
						 List list = mobileMessageService.getByPhone(map2);
						if(list==null || list.size()==0){
							MobileMessage mMessage = new MobileMessage();
							UUID uuid = UUID.randomUUID();
							mMessage.setId(uuid.toString());
							mMessage.setSentTime(sendTime);
							if(txfs.contains("接收")){
								mMessage.setTradeWay("接收");
							}else{
								mMessage.setTradeWay("发送");
							}
				        	mMessage.setAllPay(new BigDecimal(Double.parseDouble(txf)));
				        	mMessage.setRecevierPhone(dfhm);
				        	mMessage.setPhone(login.getLoginName());
				        	Date now = new Date();
				        	mMessage.setCreateTs(now);
							mobileMessageService.save(mMessage);
					}
						
					}
				
				}
			}
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
			String warnType = WaringConstaint.HLJYD_5;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return flag;
	}
	private boolean saveDetailTel(){
		boolean flag = false;
		String DetailTelUrl = "http://www.hl.10086.cn/service/fee/f_phone/particular/pfDetListNew.do";
		try {
			List<String> ms = DateUtils.getMonths(6, "yyyyMM") ;
			int x = 0;
			CHeader h = new CHeader();
			h.setAccept(CHeaderUtil.Accept_);
			h.setAccept_Language("zh-CN");
			h.setUser_Agent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/7.0)");
			// 解压报文出错！
			// h.setAccept_Encoding("gzip, deflate");
			h.setHost("service.hl.10086.cn");
			h.setConnection("Keep-Alive");
			h.setCache_Control("no-cache");
			Map<String, String> param = new HashMap<String, String>();
			//fee=0&sDataType=1&flag=&sDate=2014%E5%B9%B407%E6%9C%88&sListType=72&sMul=0&forwardStr=detailQuery
			param.put("fee", "0");
			param.put("sDataType", "1");
			param.put("sListType", "72");
			param.put("sMul", "0");
			param.put("forwardStr", "detailQuery");
			for (String s : ms) {
				String year = s.substring(0,4);
	 			String month = s.substring(4);
	 			String date = year+"年"+month+"月";
	 			param.put("sDate", date);
				String text = cutil.post(DetailTelUrl, h, param);
				//System.out.println(text);
				if(text.contains("中国移动通信客户详单")){
					RegexPaserUtil rp1 = new RegexPaserUtil("</b></td>    </tr>","<tr style=\"font-size:12px;\">      <td height=\"24\" align=\"center\"><b>合计：</b></td>",text,RegexPaserUtil.TEXTEGEXANDNRT);
					//RegexPaserUtil rp2 = new RegexPaserUtil("</b></td>    </tr>",rp1.getText(),RegexPaserUtil.TEXTEGEXANDNRT);
					String table = rp1.getText(); 
					if(table==null||table.trim().equals("")){
						continue;
					}
					String tableString = "<table>"+table+"</table>";
					Document doc6 = Jsoup.parse(tableString);
					Elements tables =doc6.select("table");
					Elements trs = tables.get(0).select("tr");
					BigDecimal tcgdf=new BigDecimal(0);	
					BigDecimal yytxf=new BigDecimal(0);	
					BigDecimal hj=new BigDecimal(0);	
					for(int i = 1 ; i<trs.size();i++){
						Elements tds = trs.get(i).select("td");
						if(tds.size()>8){
							String thsj = tds.get(0).text();//起始时间
							String thwz = StringEscapeUtils.unescapeHtml3(tds.get(1).text().replaceAll("\\s*", ""));//通信地点
							String thlx = StringEscapeUtils.unescapeHtml3(tds.get(2).text().replaceAll("\\s*", ""));//通信方式
							String dfhm = tds.get(3).text().replaceAll("\\s*", "");//对方号码
							String thsc = tds.get(4).text().replaceAll("\\s*", "");//通信时长
							String ctlx = StringEscapeUtils.unescapeHtml3(tds.get(5).text().replaceAll("\\s*", ""));//通信类型
							String mylx = StringEscapeUtils.unescapeHtml3(tds.get(6).text().replaceAll("\\s*", ""));//执行套餐
							String zfy = tds.get(7).text().replaceAll("元" ,"").replaceAll("\\s*", "");//实收通信费
							String wl = StringEscapeUtils.unescapeHtml3(tds.get(8).text().replaceAll("\\s*", ""));//网络类型
							 Map map2 = new HashMap();
							 map2.put("phone", login.getLoginName());
							 map2.put("cTime", DateUtils.StringToDate(thsj, "yyyy/MM/dd HH:mm:ss"));
							 List list = mobileDetailService.getMobileDetailBypt(map2);
							if(list==null || list.size()==0){
								MobileDetail mDetail = new MobileDetail();
								UUID uuid = UUID.randomUUID();
								mDetail.setId(uuid.toString());
					        	mDetail.setcTime(DateUtils.StringToDate(thsj, "yyyy/MM/dd HH:mm:ss"));
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
					        	mDetail.setTaocan(mylx);
					        	mDetail.setOnlinePay(new BigDecimal(zfy));
					        	mDetail.setPhone(login.getLoginName());
					        	if(x==0){
					        		mDetail.setIscm(1);
					        	}
								mobileDetailService.saveMobileDetail(mDetail);
						}
							
						}
					
					}
				}
				x++;
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			String warnType = WaringConstaint.HLJYD_4;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}
		return flag;
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
	public static void main(String[] args) {
		Login login = new Login("18246463160","123123");
		HLJYidong hn = new HLJYidong(login);
		//初始化
		hn.index();
		hn.inputCode(hn.getImgUrl());
		//登陆
		Map<String,Object> map = hn.login();
		//获取当前余额
		//hn.getMonthTel("18246463160", "888888888888", null, null);
		//hn.sendMsg();
		hn.close();
	}

}
