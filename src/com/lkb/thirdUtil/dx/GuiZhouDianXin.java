package com.lkb.thirdUtil.dx;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderManager;
import com.lkb.robot.request.AbstractProcessorObserver;
import com.lkb.robot.util.ContextUtil;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CUtil;
import com.lkb.warning.WarningUtil;


/**
* <p>Title: GuiZhouDianXin</p>
* <p>Description: 贵州电信</p>
* <p>Company: QuantGroup</p> 
* @author Jerry Sun
* @date 2014-9-16
*/
public class GuiZhouDianXin extends AbstractDianXinCrawler {
	Map<String, String> map = new HashMap<String, String>();
	
	public GuiZhouDianXin(Spider spider, User user, String phoneNo, String password, String authCode, WarningUtil util, String sms) {
		this();
		this.spider = spider;
		this.phoneNo = phoneNo;
		this.password = password;
		if (user == null) {
			this.user = new User();
			this.user.setPhone(phoneNo);
		} else {
			this.user = user;
		}
		this.util = util;
		this.authCode = authCode;
		this.mobileCode = sms;
		spider.getSite().setCharset("utf-8");
	}
	
	public GuiZhouDianXin(Spider spider, WarningUtil util) {
		this();
		this.spider = spider;		
		this.util = util;
		//spider.getSite().setCharset("utf-8");
	}
	
	public GuiZhouDianXin() {
		areaName = "贵州";
		customField1 = "1";
		customField2 = "24";
		
		toStUrl = "&toStUrl=http://gz.189.cn/SsoAgent?returnPage=yecx";
		shopId = "10024";
	}

	/* （非 Javadoc）
	* <p>Title: onCompleteLogin</p>
	* <p>Description: 完成登陆后进行抓取动作的各方法</p>
	* @param context
	* @see com.lkb.thirdUtil.dx.AbstractDianXinCrawler#onCompleteLogin(com.lkb.bean.SimpleObject)
	*/
	@Override
	protected void onCompleteLogin(SimpleObject context) {
		Document doc = ContextUtil.getDocumentOfContent(context);
		Elements userNos = doc.select("#USER_NO");
		Elements loginTypes = doc.select("#LOGIN_TYPE");
		if(userNos.size()>0 && loginTypes.size()>0){
			if(!"".equals(userNos.get(0).val()) && !"".equals(loginTypes.get(0).val())){
				sendMobileCode();
				getUserInfo();	//获取用户信息
				getBillBalance();
				setStatus(STAT_SUC);
				notifyStatus();
			}else{
				setStatus(STAT_STOPPED_FAIL);
				data.put("errMsg", "登录失败，请重试！");
				notifyStatus();
				return;
			}
		}else{
			setStatus(STAT_STOPPED_FAIL);
			data.put("errMsg", "登录失败，请重试！");
			notifyStatus();
			return;
		}
	}
	
	/**
	* <p>Title: sendMobileCode</p>
	* <p>Description: 发送手机动态验证码</p>
	* @author Jerry Sun
	*/
	public void sendMobileCode(){
		getUrl("http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", new AbstractProcessorObserver(util, WaringConstaint.GZDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				if(content.indexOf("gotoUrl('") != -1){
					String replaceContent = content.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "").replace("\t", "");
					String gotoUrlParam = StringUtil.subStr("gotoUrl('", ");", replaceContent);
					if (StringUtils.isBlank(gotoUrlParam.trim())){
						return;
					}
					String[] split = gotoUrlParam.split(",");
					if(split.length != 0){
						String str = "";
						for(int i=0; i<split.length; i++){
							if(i==0)
								str = split[i].substring(0, split[i].lastIndexOf("'"));
							else
								str = split[i].substring(1, split[i].lastIndexOf("'"));
							switch (i) {
							case 0:
								entity.put("phoneNum", str);
								break;
							case 1:
								entity.put("areaCode", str);
								break;
							case 2:
								entity.put("prodType", str);
								break;
							case 3:
								entity.put("accNbr97", str);
								break;
							}
						}

					}
				}
				
				String[][] pairs = {{"SERV_TYPE", "FSE-2"}, {"SERV_NO", "FSE-2-2"}, {"ACC_NBR", entity.getString("phoneNum")}, {"AREA_CODE", entity.getString("areaCode")}, {"PROD_NO", entity.getString("prodType")}, {"ACCTNBR97", entity.getString("accNbr97")}};
				String[][] headers = {{"X-Requested-With", "XMLHttpRequest"}, {"Content-Type", "application/x-www-form-urlencoded"}, {"Accept-Language", "zh-CN"}, {"Accept-Encoding", "gzip, deflate"}, {"Connection", "Keep-Alive"}, {"Host", "gz.189.cn"}, {"Pragma", "no-cache"}};
				postUrl("http://gz.189.cn/service/bill/fycx/detail_info.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", null, pairs, headers, new AbstractProcessorObserver(util, WaringConstaint.GZDX_3) {
					@Override
					public void afterRequest(SimpleObject context) {
						if(context == null)
							return;
						Document doc = ContextUtil.getDocumentOfContent(context);
						//2.获取12个参数值
						Map params = getParams(doc);
						getRandomPwd(params);
					}
				});
			}
		});
	}
	
	/**
	* <p>Title: checkMobileCode</p>
	* <p>Description: 校验验证码,根据验证码正确性决定下一步。如果正确，进行数据抓取；如果错误，停留在验证码页面</p>
	* @author Jerry Sun
	* @param mobileCode
	*/
	private void checkMobileCode(final String mobileCode){
		getUrl("http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", new AbstractProcessorObserver(util, WaringConstaint.GZDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				if(content.indexOf("gotoUrl('") != -1){
					String replaceContent = content.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "").replace("\t", "");
					String gotoUrlParam = StringUtil.subStr("gotoUrl('", ");", replaceContent);
					if (StringUtils.isBlank(gotoUrlParam.trim())){
						return;
					}
					String[] split = gotoUrlParam.split(",");
					if(split.length != 0){
						String str = "";
						for(int i=0; i<split.length; i++){
							if(i==0)
								str = split[i].substring(0, split[i].lastIndexOf("'"));
							else
								str = split[i].substring(1, split[i].lastIndexOf("'"));
							switch (i) {
							case 0:
								entity.put("phoneNum", str);
								break;
							case 1:
								entity.put("areaCode", str);
								break;
							case 2:
								entity.put("prodType", str);
								break;
							case 3:
								entity.put("accNbr97", str);
								break;
							}
						}

					}
				}
				
				String[][] pairs = {{"SERV_TYPE", "FSE-2"}, {"SERV_NO", "FSE-2-2"}, {"ACC_NBR", entity.getString("phoneNum")}, {"AREA_CODE", entity.getString("areaCode")}, {"PROD_NO", entity.getString("prodType")}, {"ACCTNBR97", entity.getString("accNbr97")}};
				String[][] headers = {{"X-Requested-With", "XMLHttpRequest"}, {"Content-Type", "application/x-www-form-urlencoded"}, {"Accept-Language", "zh-CN"}, {"Accept-Encoding", "gzip, deflate"}, {"Connection", "Keep-Alive"}, {"Host", "gz.189.cn"}, {"Pragma", "no-cache"}};
				postUrl("http://gz.189.cn/service/bill/fycx/detail_info.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", null, pairs, headers, new AbstractProcessorObserver(util, WaringConstaint.GZDX_3) {
					@Override
					public void afterRequest(SimpleObject context) {
						if(context == null)
							return;
						Document doc = ContextUtil.getDocumentOfContent(context);
						//2.获取12个参数值
						Map params = getParams(doc);
						checkSmsCode(params, mobileCode);
					}
				});
			}
		});
	}
	
	/**
	* <p>Title: requestAllService</p>
	* <p>Description: 所有请求服务（流程：1.校验手机动态码 2.根据成功判断下步流程 3.正确——抓取数据 错误——停留在当前页面，提示验证码错误）</p>
	* @author Jerry Sun
	* @param sms
	*/
	public void requestAllService(String sms) {
		checkMobileCode(sms);
	}
	
	/**
	* <p>Title: requestService</p>
	* <p>Description: 抓取服务请求</p>
	* @author Jerry Sun
	*/
	public void requestService() { //由于目前的测试数据中没有话费记录，所以暂时未开发此功能
		
		getBill();
	}
	
	
	/* （非 Javadoc）
	* <p>Title: checkVerifyCode</p>
	* <p>Description: 检查是否需要验证码，根据检查结果选择需要执行的方法</p>
	* @param userName
	* @see com.lkb.thirdUtil.AbstractCrawler#checkVerifyCode(java.lang.String)
	*/
	public void checkVerifyCode(final String userName) {   
		saveVerifyCode("guizhou", userName);
    }
	
	/**
	* <p>Title: getUserInfo</p>
	* <p>Description: 抓取用户个人基本信息</p>
	* @author Jerry Sun
	*/
	private void getUserInfo(){
		String url = "http://www.189.cn/dqmh/userCenter/userInfo.do?method=editUserInfo&rand=" + System.currentTimeMillis();
		getUrl(url, url, new AbstractProcessorObserver(util, WaringConstaint.GZDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				String userName = StringUtil.subStr("var head_userName = '", "'+'';", content);
				Document doc = ContextUtil.getDocumentOfContent(context);
				//获取个人基本信息
				Element personalInfo = doc.select("#personalInfo").get(0);
//				Elements tds = personalInfo.getElementsByTag("td");
//				String nickName = personalInfo.select("[name=nickName]").attr("value");	//昵称
//				String phoneNumber = personalInfo.select("[name=phoneNumber]").attr("value");	//联系电话
//				String email = personalInfo.select("[name=email]").attr("value");	//email
//				String realName = personalInfo.select("[name=realName]").attr("value");		//真实姓名
				String certificateNumber = personalInfo.select("[name=certificateNumber]").attr("value");	//身份证号
//				String address = personalInfo.select("[name=address]").attr("value");	//联系地址
				/*for(int i=0; i<tds.size(); i++){
					String e = tds.get(i).toString();
					if(e.indexOf("用&nbsp;户&nbsp;名") != -1){
						String span = tds.get(i+1).toString();
						String loginName = StringUtil.subStr("<span>", "</span>", span);	//用户名
					}
				}*/
				//获取更多个人信息
				Element moreInfo = doc.select("#moreInfo").get(0);
				String brithday = moreInfo.select("[name=brithday]").attr("value");		//生日
				user.setRealName(userName);
				user.setIdcard(certificateNumber);
				user.setBirthday(DateUtils.StringToDate(brithday, "yyyy-MM-dd"));
			}
		});
	}
	
	/**
	* <p>Title: getBillBalance</p>
	* <p>Description: 抓取账户余额</p>
	* @author Jerry Sun
	*/
	private void getBillBalance(){
		getUrl("http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", new AbstractProcessorObserver(util, WaringConstaint.GZDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				if(content.indexOf("gotoUrl('") != -1){
					String replaceContent = content.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "").replace("\t", "");
					String gotoUrlParam = StringUtil.subStr("gotoUrl('", ");", replaceContent);
					if (StringUtils.isBlank(gotoUrlParam.trim())){
						return;
					}
					String[] split = gotoUrlParam.split(",");
					if(split.length != 0){
						String str = "";
						for(int i=0; i<split.length; i++){
							if(i==0)
								str = split[i].substring(0, split[i].lastIndexOf("'"));
							else
								str = split[i].substring(1, split[i].lastIndexOf("'"));
							switch (i) {
							case 0:
								entity.put("phoneNum", str);
								break;
							case 1:
								entity.put("areaCode", str);
								break;
							case 2:
								entity.put("prodType", str);
								break;
							case 3:
								entity.put("accNbr97", str);
								break;
							}
						}

					}
				}
				getBillBalanceDetail(entity.getString("phoneNum"), entity.getString("areaCode"), entity.getString("prodType"), entity.getString("accNbr97"));
			}
		});
	}
	
	/**
	* <p>Title: getBillBalanceDetail</p>
	* <p>Description: 抓取余额明细</p>
	* @author Jerry Sun
	* @param phoneNum
	* @param areaCode
	* @param prodType
	* @param accNbr97
	*/
	private void getBillBalanceDetail(final String phoneNum, final String areaCode, final String prodType, final String accNbr97){
		String[][] pairs = {{"SERV_TYPE", "FSE-2"}, {"SERV_NO", "FSE-2-1"}, {"ACC_NBR", phoneNum}, {"AREA_CODE", areaCode}, {"PROD_NO", prodType}, {"ACCTNBR97", accNbr97}};
		postUrl("http://gz.189.cn/service/bill/fycx/ye.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", pairs, new AbstractProcessorObserver(util, WaringConstaint.GZDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				Document doc = ContextUtil.getDocumentOfContent(context);
				//<font style="font-weight:bold;color:red;">
				Elements fonts = doc.select("[style=font-weight:bold;color:red]");
				if(fonts.size()<=0)
					return;
				String yue = fonts.get(0).text();
				//实时话费！
				user.setPhoneRemain(new BigDecimal(yue));
				fonts = doc.select("[style=font-weight:bold;color:red;]");
				if(fonts.size()<=0)
					return;
				try{
					String realTimeFee = fonts.get(0).text();
					DianXinTel thisMonthBill=new DianXinTel();
					List<String> months = DateUtils.getMonths(1, "yyyyMM");
					thisMonthBill.setcAllPay(new BigDecimal(realTimeFee));
					thisMonthBill.setcTime(DateUtils.StringToDate(months.get(0), "yyyyMM"));
					thisMonthBill.setTeleno(phoneNo);
					telList.add(thisMonthBill);
				}catch(Exception e){
					logger.error("GuiZhouDianXin realTimeFee&yue error:",e);
				}
			}
		});
	}
	
	/**
	* <p>Title: getBill</p>
	* <p>Description: 抓取月通话账单</p>
	* @author Jerry Sun
	*/
	private void getBill(){
		getUrl("http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-1", new AbstractProcessorObserver(util, WaringConstaint.GZDX_1) {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				if(content == null)
					return;
				if(content.indexOf("gotoUrl('") != -1){
					String replaceContent = content.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "").replace("\t", "");
					String gotoUrlParam = StringUtil.subStr("gotoUrl('", ");", replaceContent);
					if (StringUtils.isBlank(gotoUrlParam.trim())){
						return;
					}
					String[] split = gotoUrlParam.split(",");
					if(split.length != 0){
						String str = "";
						for(int i=0; i<split.length; i++){
							if(i==0)
								str = split[i].substring(0, split[i].lastIndexOf("'"));
							else
								str = split[i].substring(1, split[i].lastIndexOf("'"));
							switch (i) {
							case 0:
								entity.put("phoneNum", str);
								break;
							case 1:
								entity.put("areaCode", str);
								break;
							case 2:
								entity.put("prodType", str);
								break;
							case 3:
								entity.put("accNbr97", str);
								break;
							}
						}

					}
				}
				getBillInfo();
				getBillDetail(entity.getString("phoneNum"), entity.getString("areaCode"), entity.getString("prodType"), entity.getString("accNbr97"));
			}
		});
	}
	
	/**
	* <p>Title: getBillInfo</p>
	* <p>Description: 抓取用户月账单</p>
	* @author Jerry Sun
	*/
	private void getBillInfo(){
		List<String> months = DateUtils.getMonthsNotInclude(6, "yyyyMM");
		for (String month : months) {
		String[][] pairs = {{"PreshForm", "1"}, {"BillingMonth", month}};
		final String month_temp = month;
		postUrl("http://gz.189.cn/service/bill/cust_bill/index.jsp", "http://gz.189.cn/service/bill/cust_bill/index.jsp", pairs, new AbstractProcessorObserver(util, WaringConstaint.GZDX_2) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(ContextUtil.getContent(context).contains("该月份无相关账单信息 "))
					return;
				Document doc = ContextUtil.getDocumentOfContent(context);
				String text = doc.text();
				DianXinTel dxTel = new DianXinTel();
				Date dqyf = DateUtils.StringToDate(month_temp, "yyyyMM");
				String jfzq = StringUtil.subStr("计费周期：", "打印日期", text).trim();
				String khxm = StringUtil.subStr("客户姓名：", "温馨提示", text).trim();
				String sjhm = StringUtil.subStr("用户号码： 手机：", "费用项目", text).trim();
				String bxxj = StringUtil.subStr("本项小计:", "使用量项目", text).trim()!=""?StringUtil.subStr("本项小计:", "使用量项目", text).trim():"0";
				String mythf = StringUtil.subStr("国内漫游通话费", "短信彩信费", text).trim()!=""?StringUtil.subStr("国内漫游通话费", "短信彩信费", text).trim():"0";
				
				dxTel.setcTime(dqyf);
				dxTel.setcName(khxm);
				dxTel.setTeleno(sjhm);
				dxTel.setDependCycle(jfzq);
				dxTel.setcAllPay(new BigDecimal(bxxj));
				dxTel.setMythf(new BigDecimal(mythf));
				
				if(dxTel != null)
					telList.add(dxTel);
			}
		});
		}
	}
	
	/**
	* <p>Title: getBillDetail</p>
	* <p>Description: 抓取月账单明细</p>
	* @author Jerry Sun
	* @param phoneNum
	* @param areaCode
	* @param prodType
	* @param accNbr97
	*/
	private void getBillDetail(final String phoneNum, final String areaCode, final String prodType, final String accNbr97){
		//1.请求话费详单查询页
		String[][] pairs = {{"SERV_TYPE", "FSE-2"}, {"SERV_NO", "FSE-2-2"}, {"ACC_NBR", phoneNum}, {"AREA_CODE", areaCode}, {"PROD_NO", prodType}, {"ACCTNBR97", accNbr97}};
		String[][] headers = {{"X-Requested-With", "XMLHttpRequest"}, {"Content-Type", "application/x-www-form-urlencoded"}, {"Accept-Language", "zh-CN"}, {"Accept-Encoding", "gzip, deflate"}, {"Connection", "Keep-Alive"}, {"Host", "gz.189.cn"}, {"Pragma", "no-cache"}};
		postUrl("http://gz.189.cn/service/bill/fycx/detail_info.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", null, pairs, headers, new AbstractProcessorObserver(util, WaringConstaint.GZDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context == null)
					return;
				Document doc = ContextUtil.getDocumentOfContent(context);
				//2.获取12个参数值
				Map params = getParams(doc);
				List<String> monthForm = DateUtils.getMonths(7, "yyyyMM");
				for(int i = 6; i >= 0 ; i--){
					String accMonth = monthForm.get(i);
					getTelBillDetailForMonth(params, accMonth, mobileCode);
					getFlowForMonth(params, accMonth, mobileCode);
					getSmsBillDetailForMonth(params, accMonth, mobileCode);
					
				};
			}
		});
	}
	
	/**
	* <p>Title: getParams</p>
	* <p>Description: 通过解析页面源码获取12个参数值（具体参数请查看源码）</p>
	* @author Jerry Sun
	* @param doc
	* @return
	*/
	private Map getParams(Document doc) {
		String area_code = doc.select("[name=AREA_CODE]").attr("value");
		String acc_nbr = doc.select("[name=ACC_NBR]").attr("value");
		String num = doc.select("[name=NUM]").attr("value");
		String prod_no = doc.select("[name=PROD_NO]").attr("value");
		String serv_kind = doc.select("[name=SERV_KIND]").attr("value");
		String serv_type = doc.select("[name=SERV_TYPE]").attr("value");
		String sub_fun_id = doc.select("[name=SUB_FUN_ID]").attr("value");
		String serv_no = doc.select("[name=SERV_NO]").attr("value");
		String acct_qry = doc.select("[name=ACCT_QRY]").attr("value");
		String mobile_logon_name = doc.select("[name=MOBILE_LOGON_NAME]").attr("value");
		String oper_type = doc.select("[name=OPER_TYPE]").attr("value");
		String rand_type = doc.select("[name=RAND_TYPE]").attr("value");
		String hidden = doc.select("[name=hidden]").attr("value");
		map.put("AREA_CODE", area_code);
		map.put("ACC_NBR", acc_nbr);
		map.put("NUM", num);
		map.put("PROD_NO", prod_no);
		map.put("SERV_KIND", serv_kind);
		map.put("SERV_TYPE", serv_type);
		map.put("SUB_FUN_ID", sub_fun_id);
		map.put("SERV_NO", serv_no);
		map.put("ACCT_QRY", acct_qry);
		map.put("MOBILE_LOGON_NAME", mobile_logon_name);
		map.put("OPER_TYPE", oper_type);
		map.put("RAND_TYPE", rand_type);
		map.put("hidden", hidden);
		return map;
	}
	
	/**
	* <p>Title: getRandomPwd</p>
	* <p>Description: 话费详单查询，获取手机验证码</p>
	* @author Jerry Sun
	* @param map
	*/
	private void getRandomPwd(Map map){
		map.put("MOBILE_LOGON_NAME", map.get("NUM"));
		String[][] pairs = (String[][]) mapToArray(map);
		String[][] headers = {{"X-Requested-With", "XMLHttpRequest"}, {"Content-Type", "application/x-www-form-urlencoded"}, {"Accept-Language", "zh-CN"}, {"Accept-Encoding", "gzip, deflate"}, {"Connection", "Keep-Alive"}, {"Host", "gz.189.cn"}, {"Pragma", "no-cache"}};
		postUrl("http://gz.189.cn/service/bill/fycx/getrandompassword.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", null, pairs, headers, new AbstractProcessorObserver(util, WaringConstaint.GZDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(context == null)
					return;
				String content = ContextUtil.getContent(context);
				if ("0".equals(StringUtil.subStr("<actionFlag>", "</actionFlag>", content))) {
					logger.info("动态验证码发送成功！");
					setStatus(STAT_SUC);
				} else {
					data.put("errMsg", "动态验证码发送失败");
					setStatus(STAT_STOPPED_FAIL);
				}
			}
		});
	}
	
	/**
	* <p>Title: getTelBillDetailForMonth</p>
	* <p>Description: 查询该月详细通话记录</p>
	* @author Jerry Sun
	* @param map
	* @param accMonth	格式例如：201409
	*/
	private void getTelBillDetailForMonth(Map<String, String> map, String accMonth, String mobileCode){
		map.put("ACCT_QRY", "1");
		map.put("QUERY_TYPE", "1");
		map.put("MOBILE_CODE", mobileCode);
		map.put("ACCTMONTH", accMonth);
		final String areaCode = map.get("AREA_CODE");
		String[][] pairs = (String[][]) mapToArray(map);
		postUrl("http://gz.189.cn/service/bill/fycx/detail.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", pairs, new AbstractProcessorObserver(util, WaringConstaint.GZDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(ContextUtil.getContent(context).indexOf("没有相应的记录") != -1)
					return;
				Elements tables = ContextUtil.getDocumentOfContent(context).select("[class=table]");
				if(tables.size() <= 0)
					return;
				Element e = null;
				for (Element table : tables) {
					if(table.toString().indexOf("详单类型") != -1)
						continue;
					e = table;
				}
				Elements trs = e.getElementsByTag("tr");
				if(trs.size() <= 0)
					return;
				for(int i=1; i<trs.size(); i++){
					if(i == trs.size()-1){
						if(trs.get(i).toString().indexOf("合计") != -1)
							return;
					}
					DianXinDetail dxDetail = new DianXinDetail();
					dxDetail.setPhone(phoneNo);
					Elements tds = trs.get(i).getElementsByTag("td");
					for(int j=0; j<tds.size(); j++){
						String text = tds.get(j).text();
						switch (j) {
						case 1:
							dxDetail.setCallWay(text);
							break;
						case 2:
							if(text.equals(areaCode))
								dxDetail.setTradeType("本地");
							else
								dxDetail.setTradeType("长途");
							break;
						case 3:
							dxDetail.setRecevierPhone(text);
							break;
						case 4:
							dxDetail.setcTime(DateUtils.StringToDate(text, "yyyy-MM-dd HH:mm:ss"));
							break;
						case 5:
							dxDetail.setTradeTime(Integer.parseInt(text));
							break;
						case 6:
							dxDetail.setBasePay(new BigDecimal(text));
							break;
						case 7:
							dxDetail.setLongPay(new BigDecimal(text));
							break;
						case 8:
							dxDetail.setOtherPay(new BigDecimal(text));
							break;
						case 9:
							dxDetail.setAllPay(new BigDecimal(text));
							break;
						}
					}
					if(dxDetail != null)
						detailList.add(dxDetail);
				}
			}
		});
	}
	
	/**
	* <p>Title: getSmsBillDetailForMonth</p>
	* <p>Description: 查询该月详细通话记录</p>
	* @author Jerry Sun
	* @param map
	* @param accMonth	格式例如：201409
	*/
	private void getSmsBillDetailForMonth(Map<String, String> map, String accMonth, String mobileCode){
		map.put("ACCT_QRY", "1");
		map.put("QUERY_TYPE", "2");
		map.put("MOBILE_CODE", mobileCode);
		map.put("ACCTMONTH", accMonth);
		String[][] pairs = (String[][]) mapToArray(map);
		postUrl("http://gz.189.cn/service/bill/fycx/detail.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", pairs, new AbstractProcessorObserver(util, WaringConstaint.GZDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(ContextUtil.getContent(context).indexOf("没有相应的记录") != -1)
					return;
				Elements tables = ContextUtil.getDocumentOfContent(context).select("[class=table]");
				if(tables.size() <= 0)
					return;
				Element e = null;
				for (Element table : tables) {
					if(table.toString().indexOf("详单类型") != -1)
						continue;
					e = table;
				}
				Elements trs = e.getElementsByTag("tr");
				if(trs.size() <= 0)
					return;
				for(int i=1; i<trs.size(); i++){
					if(i == trs.size()-1){
						if(trs.get(i).toString().indexOf("合计") != -1)
							return;
					}
					TelcomMessage telMes = new TelcomMessage();
					telMes.setPhone(phoneNo);
					Elements tds = trs.get(i).getElementsByTag("td");
					for(int j=0; j<tds.size(); j++){
						String text = tds.get(j).text();
						switch (j) {
						case 0:
							break;
						case 1:
							//有接收短信
							if(text==null)
								telMes.setBusinessType("发送");
							else if(text.contains("接") || text.contains("收"))
								telMes.setBusinessType("接收");
							else
								telMes.setBusinessType("发送");
							break;
						case 3:
							telMes.setRecevierPhone(text);
							break;
						case 4:
							telMes.setSentTime(DateUtils.StringToDate(text, "yyyy-MM-dd HH:mm:ss"));
							break;
						case 5:
							telMes.setAllPay(Double.parseDouble(text));
							break;
						}
					}
					if(telMes != null)
						messageList.add(telMes);
				}
			}
		});
	}
	/*
	 *流量记录
	 *@ 
	 */
	private void getFlowForMonth(Map<String, String> map, String accMonth, String mobileCode){
		map.put("ACCT_QRY", "1");
		map.put("QUERY_TYPE", "4");
		map.put("MOBILE_CODE", mobileCode);
		map.put("ACCTMONTH", accMonth);
		final String mon = accMonth;  //月份              匿名内部类调用局部变量
		String[][] pairs = (String[][]) mapToArray(map);
		postUrl("http://gz.189.cn/service/bill/fycx/detail.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", pairs, new AbstractProcessorObserver(util, WaringConstaint.GZDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(ContextUtil.getContent(context).indexOf("没有相应的记录") != -1)
					return;
				Elements tables = ContextUtil.getDocumentOfContent(context).select("[class=table]");
				if(tables.size() <= 0)
					return;
				Element e = null;
				for (Element table : tables) {
					if(table.toString().indexOf("详单类型") != -1)
						continue;
					e = table;
				}
				
				/*
				 * 流量账单需要从详单上加起来获得
				 * start
				 */
				String day = e.toString();
				RegexPaserUtil rp = new RegexPaserUtil( "起止日期： ", " <" ,day, RegexPaserUtil.TEXTEGEXANDNRT);
				/** dependCycle 起止日期（计费周期）*/
				 String dependCycle = rp.getText();
				/** queryMonth 查询月份*/
				 Date queryMonth = DateUtils.StringToDate(mon, "yyyyMM");
				/** allFlow 总流量(KB)*/
				 BigDecimal allFlow = new BigDecimal(0);
				/** allTime 总时长*/
				 BigDecimal allTime = new BigDecimal(0);
				/** allPay 总费用*/
				 BigDecimal allPay = new BigDecimal(0);
				
				
				Elements trs = e.getElementsByTag("tr");
				if(trs.size() <= 0)
					return;
				for(int i=4; i<trs.size(); i++){
//					if(i == trs.size()-1){
//						if(trs.get(i).toString().indexOf("合计") != -1)//有合计
//							return;
//					}
					DianXinFlowDetail flowDetail = new DianXinFlowDetail();
					flowDetail.setPhone(phoneNo);
					Elements tds = trs.get(i).getElementsByTag("td");
					for(int j=0; j<tds.size(); j++){

						String text = tds.get(j).text();
						switch (j) {
						case 0:
							break;
						case 1:
							try {
								Date beginTime= DateUtils.StringToDate(text, "yyyy-MM-dd HH:mm:ss");
								flowDetail.setBeginTime(beginTime);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							break;
						case 2:
							
						 	try {
								long tradeTime = Math.round(StringUtil.flowFormat(text.replace(".00", "")));
								allTime = allTime.add(new BigDecimal(tradeTime));
								flowDetail.setTradeTime(tradeTime);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							break;
						case 3:
							
							try {
								text = text.substring(0,text.length()-5)+"KB";
								BigDecimal flow = new BigDecimal(StringUtil.flowFormat(text));
								allFlow = allFlow.add(flow);//总流量
								flowDetail.setFlow(flow);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							break;
						case 4:
							try {
								String netType = text;
								flowDetail.setNetType(netType);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							break;
						case 7:
							try {
								BigDecimal fee = new BigDecimal(text);
								allPay = allPay.add(fee);
								flowDetail.setFee(fee);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							break;
						}
					}
					if(flowDetail != null)
						flowDetailList.add(flowDetail);
					
				}
				
				/*
				 * 开始保存账单信息
				 */
				DianXinFlow flowBill = new DianXinFlow();
				flowBill.setPhone(phoneNo);
				UUID uuid = UUID.randomUUID();
				flowBill.setId(uuid.toString());
					try {
						flowBill.setDependCycle(dependCycle);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						flowBill.setAllFlow(allFlow);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						flowBill.setAllPay(allPay);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						flowBill.setQueryMonth(queryMonth);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(flowBill != null) {
					flowList.add(flowBill);
				}
			}
		});
	}
	/**
	* <p>Title: checkSmsCode</p>
	* <p>Description: 校验手机动态验证码</p>
	* @author Jerry Sun
	* @param map
	* @param mobileCode
	*/
	private void checkSmsCode(Map<String, String> map, String mobileCode){
		map.put("ACCT_QRY", "1");
		map.put("QUERY_TYPE", "1");
		map.put("MOBILE_CODE", mobileCode);
		map.put("ACCTMONTH", "201409");
		String[][] pairs = (String[][]) mapToArray(map);
		postUrl("http://gz.189.cn/service/bill/fycx/detail.jsp", "http://gz.189.cn/service/bill/index.jsp?SERV_NO=FSE-2-2", pairs, new AbstractProcessorObserver(util, WaringConstaint.GZDX_3) {
			@Override
			public void afterRequest(SimpleObject context) {
				if(ContextUtil.getContent(context).indexOf("短信验证码错误") != -1 || ContextUtil.getContent(context).indexOf("您没有查询此号码的权限") != -1){
					data.put("errMsg", "动态验证码输入错误!");
					setStatus(STAT_STOPPED_FAIL);
				}else{
					setStatus(STAT_SUC);
					notifyStatus();
					requestService();
				}
			}
		});
	}
	
	/**
	* <p>Title: main</p>
	* <p>Description: 账号登陆并抓取数据的本地测试方法</p>
	* @author Jerry Sun
	* @param args
	*/
	public static void main(String[] args) {
		String phoneNo = "18198347389";
		String password = "888666";
		
		Spider spider = SpiderManager.getInstance().createSpider("test", "aaa");
		GuiZhouDianXin dx = new GuiZhouDianXin(spider, null, phoneNo, password, "2345", null, null);
		dx.setTest(true);
		
		dx.checkVerifyCode(phoneNo);
		spider.start();
		dx.printData();
		dx.getData().clear();
		dx.setAuthCode(CUtil.inputYanzhengma());
		dx.goLoginReq();
		spider.start();
		dx.printData();
		
		
//		DebugUtil.addToCookieStore("gz.189.cn", "JSESSIONID=V4yWJZMfn01qWYrLGyQMpnxFwYvpgXmkS3whnYL2T1cmLDQ1v0l2!-1455552722");
//		dx.getUserInfo();
//		DebugUtil.addToCookieStore("www.189.cn", "JSESSIONID=p3zcJZHD68jyC02NQJCFspGryRLQ1J5TDvsSqRQChJcTpnFMqrFL!-1455552722; loginStatus=logined;  userId=201|20140000000004537810; cityCode=gz;isLogin=logined; .ybtj.189.cn=1B0FBA7090364AD0827ACDD8D2745F94");
//		dx.getBillBalance();
//		dx.getUserInfo();
//		SERV_TYPE=FSE-2&SERV_NO=FSE-2-2&ACC_NBR=18198347389&AREA_CODE=0852&PROD_NO=2339&ACCTNBR97=
//		DebugUtil.addToCookieStore(".189.cn", "s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-gz%253D%252526pid%25253D%2525252Fgz%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fwww.189.cn%2525252Fdqmh%2525252FfrontLink.do%2525253Fmethod%2525253DlinkTo%25252526shopId%2525253D10024%25252526toStUrl%2525253Dhttp%2525253A%2525252F%2525252Fgz.189.cn%2525252Fservice%2525252Fbill%252526ot%25253DA%252526oi%25253D320%3B; loginStatus=logined; s_cc=true; s_pers=%20s_fid%3D24A172B0F1340D0F-15DF17A05FA73892%7C1474183498086%3B; svid=d932c0efeae4801ec54596921beb5a3e; userId=201|20140000000004537810; cityCode=gz; SHOPID_COOKIEID=10024; isLogin=logined; .ybtj.189.cn=1B0FBA7090364AD0827ACDD8D2745F94; JSESSIONID=2RL0JhLKWFrZ8symY2plbc9Ls9l38ymTxs64RynWMB8WSy6SP1nL!-1455552722");
//		dx.getBillDetail("18198347389", "0852", "2339", "");
		dx.getBillInfo();
		spider.start();
	}


}
