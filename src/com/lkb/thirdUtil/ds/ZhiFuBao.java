package com.lkb.thirdUtil.ds;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.LoseContent;
import com.lkb.bean.PayInfo;
import com.lkb.bean.User;
import com.lkb.bean.YuEBao;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.thirdUtil.base.BasicCommonDs;
import com.lkb.util.DateUtils;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.pojo.SimpleClientContext;
import com.lkb.util.httpclient.pojo.SimpleData;
import com.lkb.util.httpclient.response.ExecuteRequest;
import com.lkb.util.httpclient.util.CommonUtils;
import com.lkb.util.taobao.alipay.TimeUtil;

public class ZhiFuBao extends BasicCommonDs{
	public static String alipay_url_1 = "https://auth.alipay.com/login/trust_login.do";
	public static String alipay_url_3 = "https://my.alipay.com/portal/i.htm?src=yy_content_jygl";
	public static String yuebao_url_1 = "https://financeprod.alipay.com/fund/index.htm";
	public static String alipyName = "Alipay.alipyName";
	public static String alipayIndexParam = "Alipay.alipayIndexParam";

	public ZhiFuBao(Login login, String currentUser) {
		super(login, currentUser);
		this.userSource = Constant.ZHIFUBAO;
		this.constantNum = ConstantNum.ds_zhifubao;
	}
	public ZhiFuBao(Login login) {
		super(login);
		this.userSource = Constant.ZHIFUBAO;
		this.constantNum = ConstantNum.ds_zhifubao;
	}

	@Override
	public User gatherUser() throws Exception {
		String content = cutil.get("https://my.alipay.com/portal/account/index.htm", new CHeader("https://auth.alipay.com/login/loginResultDispatch.htm"));
			// 安全等级
			String secretLevel = "";
			// 身份认证
			String identifyId = "";
			// 身份证有效期限
			String identifyTime = "";
			// 职业
			String major = "";
			// 注册时间：
			String registerDateStr = "";
			// 是否实名认证
			String isRealName = "";
			// 是否会员保障
			String isProtected = "";
			// 手机号
			String phone = "";
			// 是否绑定手机号
			String isPhone = "";
			Date registerDate = null;
			// 姓名
			String name = "";
			// 淘宝账户名
			String taobaoName = "";
			if (content != null && content.trim().length() > 0) {
				Document doc = Jsoup.parse(content);
				Element element = doc.select("ul[class=account-info-det]").get(0);
				Elements lis = element.select("li");
				// 姓名
				name = lis.get(0).select("span").get(1).text();
				RegexPaserUtil rp = new RegexPaserUtil("sign_account=","&amp;",element.html(),RegexPaserUtil.TEXTEGEXANDNRT);
				String s = rp.getText();
				if(s!=null){
						try {
							s = URLDecoder.decode(s,"utf8");
							loginName = s.split("\\^")[1];
							System.out.println(loginName);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
				}

				// 安全等级
				secretLevel = lis.get(2).select("a").get(0).text();

				// 淘宝账户名
				taobaoName = lis.get(3).select("a").get(0).text();
				if (taobaoName.contains("您还未开通淘宝服务")) {
					taobaoName = "";
				}

				Elements lis2 = doc
						.select("ul[class=account-information fn-clear]").first()
						.select("li").select("div[class=item-name]");
				Elements lis3 = doc
						.select("ul[class=account-information fn-clear]").first()
						.select("li").select("div[class=item-text]");

				for (int i = 0; i < lis2.size(); i++) {
					String question = lis2.get(i).text();
					if (question.contains("身份验证")) {
						identifyId = lis3.get(i).text();
						if (identifyId.contains("通过验证后")) {
							identifyId = "";
						} else {
							identifyId = identifyId.replace("身份证号码", "");
						}
					} else if (question.contains("身份证有效期限")) {
						identifyTime = lis3.get(i).text();
					} else if (question.contains("职业")) {
						major = lis3.get(i).text();
						if (major.contains("未设置 ")) {
							major = "";
						}
					} else if (question.contains("注册时间")) {
						registerDateStr = lis3.get(i).text();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
						try {
							registerDate = sdf.parse(registerDateStr);
						} catch (ParseException e) {
							log4j.warn("支付宝时间格式化错误#", e);
						}
					}
				}

				Element element1 = doc.select("ul[id=J_used_pro]").get(0);
				Elements lis4 = element1.select("li");
				// 是否实名认证
				isRealName = lis4.get(0).select("div[class=account-status-det]")
						.select("p").get(0).select("span").get(0).text();

				// 是否会员保障
				isProtected = lis4.get(1).select("div[class=account-status-det]")
						.select("p").get(0).select("span").get(0).text();

				// 是否绑定手机号
				isPhone = lis4.get(2).select("div[class=account-status-det]")
						.select("p").get(0).select("span").get(0).text();

				if (isPhone.contains("已绑定")) {
					phone = lis4.get(2).select("span[class=mobile-number]").get(0)
							.text().replace("(", "").replace(")", "").trim();
				}

			}
		User user = null;
		if(StringUtils.isNotBlank(loginName)){
			user = new User();
			user.setLoginName(loginName);
			user.setPhone(phone);
			user.setRealName(name);
			user.setSecretLevel(secretLevel);
			user.setTaobaoName(taobaoName);
			user.setIdcard(identifyId);
			user.setIdentifyTime(identifyTime);
			user.setMajor(major);
			user.setRegisterDate(registerDate);
			user.setIsRealName(isRealName);
			user.setIsProtected(isProtected);
			user.setIsPhone(isPhone);
		}
		return user;
	}

	@Override
	public void login() throws Exception {}

	@Override
	public void init() {}

	@Override
	public void startSpider(int type) {
		switch (type) {
		case 1:
			int i = isHasYueEbao();
			if(i==2){
				addTask(2);//开启线程三采集
			}
			saveUser();//必须先执行
			//初始化基本参数
			payList = new LinkedHashSet<PayInfo>();
			listUpdatePay = new LinkedList<PayInfo>();
			saveAlipayOneYear(); //新的
			break;
		case 2:
			saveYuEBao();
			break;
		default:
			break;
		}
		
	}
	/**淘宝跳转过来的结果,把淘宝的结果set过来
	 * @param context
	 */
	public void setContextAndClient(SimpleClientContext context,ExecuteRequest cutil,SimpleData redismap){
		this.context = context;
		this.cutil = cutil;
		this.redismap = redismap;
	}
	public  int isHasYueEbao(){
		int i = 0;
	
		String text = null;
		CHeader h = new CHeader("http://i.taobao.com/my_taobao.htm");
		//为防止登陆不上去
		for (int j = 0; j < 3; j++) {
			try{
				String url = "https://lab.alipay.com/user/i.htm";
	//			String param =  redismap.getString(alipayIndexParam);
	//			if(param!=null){
	//			param = param.replace("&amp;", "&");
	//			text = cutil.get(url+param,new CHeader("http://i.taobao.com/my_taobao.htm"));
	//			url = alipay_url_1+param+"&goto=https://lab.alipay.com/user/i.htm";
	//			}else{
					text = cutil.get(url,new CHeader("http://i.taobao.com/my_taobao.htm"));
					url = alipay_url_1+"?goto=https://lab.alipay.com/user/i.htm";
	//			}
				text = cutil.get(url);
				cutil.setHandleRedirect(false);
				text = cutil.get("https://login.taobao.com:443/member/login.jhtml?tpl_redirect_url=https://auth.alipay.com:443/login/trustLoginResultDispatch.htm?redirectType=&sign_from=3000&goto=https://lab.alipay.com/user/i.htm?src=yy_content_jygl&from_alipay=1",new CHeader(url));
				try{
				  text = cutil.get(URLDecoder.decode(text,"utf-8")+"&goto=https://lab.alipay.com/user/i.htm?src=yy_content_jygl&sign_from=3000",new CHeader(url));
//				  if(!text.contains(".alipay.com:443/error.htm?exceptionCode=")){
//					  break;
//				  }
				}catch(Exception e){
				}
				
				h = new CHeader("https://auth.alipay.com/login/certCheck.htm");
				cutil.setHandleRedirect(true);
				text = cutil.get(alipay_url_3,h);
				Document doc = Jsoup.parse(text);
				String alipayName = doc.select("a[id=J-userInfo-account-userEmail]").first().attr("title");
				redismap.put(alipyName, alipayName);
				System.out.println("---------------------------支付宝打开页面内容-----------------------------");
				if(!text.contains("app-yuebao-manage-myalipay-v1")){
					log4j.info("--------该用户未开通余额宝---------");
				   i = 1;
				   break;
				}else{
				   i =2;
				   break;
				}
			}catch(Exception e){
				e.printStackTrace();
				if(i==2){
					log4j.writeLogOrder(text, e, "OPENYUEBAO-FAIL");	
				}
			}
		
		}
		return i;
	}
	private Set<PayInfo> payList = null;
	private LinkedList<PayInfo> listUpdatePay = null;
	public void gatherAlipayTradeOneYear_parse(String content){
		if(content!=null){
			Document doc = Jsoup.parse(content);

			for (int i = 1; i < 21; i++) {
				Element element = doc.select("tr[id=J-item-" + i + "]").first();
				if (element == null) {
					break;
				}
				// 交易号码或者流水号码
				String tradeNo = element.select("a[id=J-tradeNo-" + i + "]")
						.attr("title").trim();
				System.out.println(tradeNo);

				Map map = new HashMap(2);
				map.put("tradeNo", tradeNo);
				map.put("source", Constant.ZHIFUBAO);
				List<PayInfo> list1 = service.getPayInfoService().getPayInfoByTradeNoSource(map);
				if (list1 != null && list1.size() > 0) {

				} else {
					// 2014.03.18
					String time_d = element.select("p[class=time-d]").first()
							.text();
					// 09:44
					String time_d_m = element.select("p[class=time-h ft-gray]")
							.first().text();
					time_d = time_d.replaceAll("\\.", "-");
					String payTimeStr = time_d + " " + time_d_m;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date payTime = null;
					try {
						payTime = sdf.parse(payTimeStr);
					} catch (ParseException e1) {
						log4j.info("数据解析异常#：" + e1.getMessage());
						e1.printStackTrace();
					}
					// 交易类型
					String tradeType = element.select("p[class=consume-title]")
							.first().text();
					// 交易账号类型：流水号 或者 交易号
					String tradeNoType = element
							.select("div[id=J-tradeNo-container-" + i + "]")
							.first().text().trim().substring(0, 3);

					String receiverName = element.select("td[class=other]").first()
							.select("p[class=name]").text().trim();

					String amount = element.select("td[class=amount]").text()
							.trim();
					BigDecimal payAmount = new BigDecimal(amount);
					String status = element.select("td[class=status]").text()
							.trim();

					try {
						PayInfo payInfo = new PayInfo();
						UUID uuid = UUID.randomUUID();
						payInfo.setId(uuid.toString());
						payInfo.setPayTime(payTime);
						
						payInfo.setTradeNo(tradeNo);
						payInfo.setTradeNoType(tradeNoType);
						payInfo.setTradeType(tradeType);
						payInfo.setReceiverName(receiverName);
						payInfo.setAmount(payAmount);
						payInfo.setStatus(status);
						payInfo.setSource(Constant.ZHIFUBAO);
//						payInfo.setAlipayName(alipayName);
						payInfo.setAlipayName(this.loginName);
						 int num = isContinue(payInfo);
							if(num==1){
								continue;
							}else if(num==2){
								isStop = true;
								break;
							}
//							gatherURLSaveAndParse(cutil.get(orderDetailHref), order,orderItem,productElements);
							if(num==3){
								//update
								listUpdatePay.add(payInfo);
							}else{
								 payList.add(payInfo);
							}
//						service.getPayInfoService().savePayInfo(payInfo);
					} catch (Exception e) {
						log4j.writeLogOrder("保存失败#", e, ORDER);
						spiderOrderState = false;
					}
				}

			}
		}
		
	}
	public PayInfo bigPay;
	public Map<String,String> getMaxOrderAssignTime(Date time){
		if(isTest()){
			return null;
		}
		PayInfo pay = new PayInfo();
		pay.setSource(this.userSource);
		pay.setPayTime(time);
		pay.setAlipayName(this.loginName);
		Map<String, String> map = null;
		try{
			List<Object> list =  service.getPayInfoService().getMaxOrderAssignTime(pay);
			String key = null;
			String value = null;
			map = new LinkedHashMap<String, String>();
			if(CommonUtils.isNotEmpty(list)){
				int size = list.size();
				for (int i = 0; i < size; i++) {
					value = StringUtil.subStr("=", ",",list.get(i).toString());
					key = StringUtil.subStr("payTime=", "}",list.get(i).toString());
					if(i==(size-1)){
						bigPay = new PayInfo();
						bigPay.setPayTime(DateUtils.StringToDate(key, "yyyy-MM-dd HH:mm:ss"));
						bigPay.setStatus(value);
					}
					map.put(DateUtils.StringToDate(key, "yyyy-MM-dd HH:mm:ss").getTime()+"", value);
				}
				System.out.println(list);
			}
		}catch(Exception e){
			log4j.error(e);
		}
		return map;
	}
	/**1.continue字段 2.break字段,3update修改,0继续执行
	 * 操作之前一定理清楚插入和查询的先后顺序
	 * @param order
	 * @return
	 */
	public int isContinue(PayInfo pay){
		int num = 0;
		if(pay!=null&&pay.getPayTime()!=null){
			//如果lose为空说明没有错误日志
			if(lose==null){
				if(orderMap==null)
					orderMap = getMaxOrderAssignTime(getNewDate(-4));
			}else{
				//如果上次采集失败,那么此次需要重新采集 先获取所有的关键id存入hash
				if(orderMap == null){
					Date d = null;
					Date d1 = getNewDate(-4);
					d = d1.getTime()>=lose.getCode()?new Date(lose.getCode()):d1;//取最小的时间
					orderMap = getMaxOrderAssignTime(d);
				}
			}
			num = getContinueCode(pay);
		}
		
		return num;
		
	}
	/**1继续跳过本次循环,2终止执行,3update修改,0继续执行
	 * @param order
	 * @return
	 */
	public int getContinueCode(PayInfo order){
		Long orderid = order.getPayTime().getTime();
		int m = 0;
		//判断是否需要加入正常的保存队列,存在在抛出
		if(orderMap.containsKey(orderid+"")){
			if(order.getStatus()!=null){
				if(orderMap.get(orderid+"").equals(order.getStatus())){
					m = 1;	//如果value相等说明账单没有改变,继续执行
				}else{
					m = 3;	//执行update操作
				}
			}
		}else{
			//检查错误的标示码 0正常,说明上一次采集任务为正常的,因为京东默认为倒序所以到此步的时候可以break;
			if(bigPay!=null&&bigPay.getPayTime().getTime()>=orderid){
				m = 2;
			}
		}
		return m;
	}
	public void saveAlipayOneYear(){
		
		try{
			List lo= getMaxLoseTime(orderType);
			if(CommonUtils.isNotEmpty(lo)){
				lose = (LoseContent) lo.get(0);
			}
			//------------更新最新结果-------------
//			this.gatherHistoryLoseURL();
			gatherAlipayTradeOneYearOld();//老 的
			//采集最新的结果
			gatherAlipayTradeOneYear();//新的
			
			if(!isTest()){
				List<PayInfo> listp = new ArrayList<PayInfo>();
				listp.addAll(payList);
				service.getPayInfoService().insertbatch(listp);
				//更新修改订单
				if(CommonUtils.isNotEmpty(listUpdatePay)){
					for (PayInfo pay : listUpdatePay) {
						PayInfo pay1 = getPayInfo(pay.getTradeNo());
						if(pay1!=null){
							pay.setId(pay1.getId());
							service.getPayInfoService().update(pay1);
						}
					}
				}
				long code = 0;
				if(bigPay!=null){
					code = bigPay.getPayTime().getTime();//初始值	
				}
				saveOrDeleteLostOne(orderType,code);
				saveLoseList(loseList);
			}
		}catch(Exception e){
			log4j.writeLogOrder("",e,"BAOCUN");
		}
	}
	public PayInfo getPayInfo(String tradeNo){
		Map map = new HashMap();
		map.put("ordersource",this.userSource);
		map.put("tradeNo", tradeNo);
		List<PayInfo> list = service.getPayInfoService().getPayInfoByTradeNoSource(map);
		if(CommonUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	/**交易记录之一年内*/
	public boolean gatherAlipayTradeOneYear(){
		
		/**支付宝交易记录一年内**/
		StringBuffer sburl = new StringBuffer();
		sburl.append("https://consumeprod.alipay.com/record/standard.htm?_input_charset=utf-8&dateRange=oneYear&tradeType=all&status=all&fundFlow=all&beginTime=00%3A00&dateType=createDate&endTime=24%3A00");
		sburl.append("&beginDate=").append(TimeUtil.getStartTime());
		sburl.append("&endDate=").append(TimeUtil.getEndTime());
		sburl.append("&pageNum=");
		boolean b = true;
		Integer page = 1;
//		String url = openAlipayJiaoyiInfo(sburl.toString(),page);
		String text = getHtml(sburl.toString(), page);
		gatherAlipayTradeOneYear_parse(text);//查询年份的第一份
		if(!isStop){
			try{
				if(text!=null){
					page = getPage(text);
					for (int i = 2; i <= page; i++) {
						gatherAlipayTradeOneYear_parse(getHtml(sburl.toString(), i));
						if(isStop){
							break;
						}
					}
				}
			}catch(Exception e){
				log4j.writeLogOrder(text, e,ORDER);
				spiderOrderState = false;
			}
		}
		

		return b;
	}

	/**交易记录之一年之前
	 * @throws Exception */
	public void gatherAlipayTradeOneYearOld(){
		/**支付宝交易记录一年之前**/
		String text = null;
		String url = "https://lab.alipay.com/consume/record/historyIndexNew.htm";
		String page = "1";
		String criticalDate = TimeUtil.getCriticalDate();
		String endTime = TimeUtil.getOneYearBeforeTime();
		try{
			text = cutil.get(url,new CHeader("https://lab.alipay.com/consume/record/historyIndexNew.htm"));
			String _form_token =new RegexPaserUtil("name=\"_form_token\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
			if(_form_token!=null){
				Map<String,String> param = new HashMap<String,String>();
				param.put("customerType","2");
				param.put("keyword","0_");	
				param.put("beginTime","2004-01-01");	
				param.put("endTime",endTime);	
				param.put("criticalDate",criticalDate);	
				CHeader h = new CHeader("https://lab.alipay.com/consume/record/historyIndexNew.htm");
				String alipayName = redismap.getString(alipyName);
				//超过500次请求终止
				for (int i = 0; i < 500; i++) {
					if(_form_token!=null){
						param.put("_form_token",_form_token);
						//page 为空,未解析到下一页  终止循环
						if(page==null)
							break;
						param.put("currentPageNo",page);
						text = cutil.post(url, h,param);
						if(text!=null){
							page = new RegexPaserUtil("<a class=\"page-next form-element\" href=\"#\" rel=\"currentPageNo\" rel-value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
							_form_token = new RegexPaserUtil(" name=\"_form_token\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
						}else
							throw new Exception(text);
						 gatherAlipayTradeOneYearOld_parse(text, alipayName);
						 if(isStop){
							 break;
						 }
					}else
						throw new Exception("支付宝历史账单_form_token字段取值为空!---"+text);
				}
			}
		}catch(Exception e){
			log4j.writeLogOrder(text, e,ORDER);
			spiderOrderState = false;
		}
	}
	public void gatherAlipayTradeOneYearOld_parse(String content,String alipayName){
		Document doc = Jsoup.parse(content);
		Elements elements = doc.select("table[id=tradeRecordsIndex]").first()
				.select("tbody").select("tr");

		java.text.DateFormat format3 = new java.text.SimpleDateFormat(
				"yyyy.MM.dd HH:mm");

		for (int i = 0; i < elements.size(); i++) {
			try{
				Element element = elements.get(i);
				String times = element.select("td[class=time2]").first().text();
				Date payTime = null;
				try {
					payTime = format3.parse(times);
				} catch (ParseException e) {
					log4j.warn("支付宝历史账单记录时间格式化错误#", e);
				}

				String tradeType = element.select("li[class=name fn-claer]")
						.first().text();// 付款-转账
				String tradeNo = element.select("li[class=J-bizNo]").first().text();// No
				String receiverName = element.select("td[class=other]").first()
						.text();// receriverName
				String amount = element.select("td[class=amount]").first().text();// amount
				BigDecimal payAmount = new BigDecimal(amount);
				String status = element.select("td[class=status]").first().text();// status

				System.out.println(payTime + "&&" + tradeType + "&&" + tradeNo
					+ "&&" + receiverName + "&&" + payAmount + "&&" + status);
				String tradeNoType = "交易号";
				PayInfo payInfo = new PayInfo();
				UUID uuid = UUID.randomUUID();
				payInfo.setId(uuid.toString());
				payInfo.setPayTime(payTime);

				payInfo.setTradeNo(tradeNo);
				payInfo.setTradeNoType(tradeNoType);
				payInfo.setTradeType(tradeType);
				payInfo.setReceiverName(receiverName);
				payInfo.setAmount(payAmount);
				payInfo.setStatus(status);
				payInfo.setSource(Constant.ZHIFUBAO);
				payInfo.setAlipayName(alipayName);
			    int num = isContinue(payInfo);
				if(num==1){
					continue;
				}else if(num==2){
					isStop = true;
					break;
				}
				if(num==3){
					//update
					listUpdatePay.add(payInfo);
				}else{
					 payList.add(payInfo);
				}
			} catch (Exception e) {
				log4j.writeLogOrder(content, e, ORDER);
				spiderOrderState = false;
			}
		}
				
	}

	/** 解析支付宝一年前的 */

	private String getHtml(String url,int page){
		String text = cutil.get(url+page);
		if(text==null){
			spiderOrderState = false;
		}
		return text;
	}

	private Integer getPage(String html) throws Exception{
		int page = -1;
		try{
			String url = new RegexPaserUtil("<a class=\"page-end\" href=\"","\">尾页",html,RegexPaserUtil.zel_all_chars).getText();
			if(url!=null){
				if(url.contains("pageNum=")){
					page = Integer.parseInt(url.split("pageNum=")[1].trim());
				}else{
					page = Integer.parseInt(url.substring(url.length()-1));
				}
			}else {
				if(page>150){
					page=150;
				}
			}
		}catch(Exception e){
			throw new Exception(html,e);
		}
		return page;
	}
	@Override
	public void gatherHistoryLoseURL() { }

	@Override
	public void gatherOrder() {
		// TODO Auto-generated method stub
	}
//---------------------------------------------------------------------余额宝---------------------------
	/**打开余额宝*/
	public  boolean  getYuEBao(){
		boolean b =false;
		String text = cutil.get(yuebao_url_1);
		if(text!=null){
			if(text.contains("进入我的余额宝")){
				log4j.info("--------该用户未开通余额宝-------");
			}else{
				b = true;
			}
		}
		return b;
	}
	public String getYueBaoHtml(int page,String _form_token){
		String url = "https://financeprod.alipay.com/fund/asset.htm";
		String text = null;
		Map<String,String> param = new HashMap<String,String>();
		param.put("direction", "income");
		param.put("startDate", "2013-05-01");
		param.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
		CHeader h = new CHeader("https://financeprod.alipay.com/fund/asset.htm");
		if(_form_token!=null){
			param.put("_form_token",_form_token);
			param.put("pageNum",page+"");	
			text = cutil.post(url, h,param);
		}
		return text;
	}
	public int getTotalPageYuebao(String text){
		int page=-1;
		System.out.println(text);
    	String text1 = new RegexPaserUtil("totalPage:",",",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
    	try{
	    	if(text1!=null){
	    		page =  Integer.parseInt(text1.trim());
	    	}
    	}catch(Exception e){
    	}
    	return page;
	}
	/**余额宝收益信息
	  */
	public void openShouyi(){
		int page = 1;
		String text = null;
		try{
			String _form_token = getToken(cutil.get("https://financeprod.alipay.com/fund/asset.htm"));
			if(_form_token!=null){
				text = getYueBaoHtml(page, _form_token);
				if(text!=null){
					_form_token = getToken(text);
					page = 	getTotalPageYuebao(text);
					saveYueBaoUser(text);				
					listLog(text);
					if(isStopYuEBao){
						return ;
					}
					for (int i = 2; i <= page; i++) {
						text = getYueBaoHtml(i, _form_token);
						_form_token = getToken(text);
						listLog( text);
						if(isStopYuEBao){
							break;
						}
					}
				}
			}
		}catch(Exception e){
			log4j.writeLogOrder(text, e,"YUEBAO-SHOUYI");
			spiderYuEBaoState = false;
		}
	}
	
	/**模拟浏览器打开收益页用于获取token值
	 * @throws Exception */
	public String getToken(String text) throws Exception{
		try{
			String str = new RegexPaserUtil("\"J-calendar-form\">","id=\"J_submit_time",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
			return new RegexPaserUtil("_form_token\" value=\"","\"",str,RegexPaserUtil.TEXTEGEXANDNRT).getText();
		}catch(Exception e){
			throw new Exception(text);
		}
	}
	private YuEBao bigyuebao; 
	/**order表更新状态的对象*/
	public LoseContent loseYuebao;
	private Set<YuEBao> yuebaoList = null;
	private Set<String> setYueTime = null;
	private boolean spiderYuEBaoState = true;
	private boolean isStopYuEBao = false;
	public void saveYuEBao(){
		try{
			if(getYuEBao()){
				//此处orderItemType 标示余额宝
				List lo= getMaxLoseTime(orderItemType);
				if(CommonUtils.isNotEmpty(lo)){
					loseYuebao = (LoseContent) lo.get(0);
				}
				bigyuebao = getBigYuEBao();
				yuebaoList = new LinkedHashSet<YuEBao>();
				openShouyi();
				if(!isTest()){
					List<YuEBao> listp = new ArrayList<YuEBao>();
					listp.addAll(yuebaoList);
					service.getYuebaoService().insertbatch(listp);
					long code = 0;
					if(bigyuebao!=null){
						code = bigyuebao.getcTime().getTime();//初始值	
					}
					saveOrDeleteLostYuebaoState(orderItemType,code);
				}
			}
		}catch(Exception e){
			log4j.writeLogOrder("",e,"YUEBAO-BAOCUN");
		}
	}
	/**保存或者删除标示状态 
	 * @param type 标示值
	 * 修改值,可以是时间或者订单
	 */
	public void saveOrDeleteLostYuebaoState(int type,long code){
		if(!isTest()){
			//只会执行一次
			if(!spiderYuEBaoState){
				if(loseYuebao==null){
					loseYuebao = new LoseContent();
					loseYuebao.setId(UUID.randomUUID().toString());
					loseYuebao.setUserSource(this.userSource);
					loseYuebao.setLoginName(this.loginName);
					loseYuebao.setUpdateTime(new Date());
					loseYuebao.setType(type);
					loseYuebao.setErrorCode(1);
//					if(bigOrder!=null){
//						loseYuebao.setCode(bigOrder.getBuyTime().getTime());//初始值	
//					}else{
						loseYuebao.setCode(code);//初始值	
//					}
					service.getLoseContentService().save(loseYuebao);
				}
			}else{
				if(loseYuebao!=null){
					service.getLoseContentService().delete(loseYuebao);	
				}
			}
		}
		
	}
	public YuEBao getBigYuEBao(){
		if(!isTest()){
			YuEBao bao =new YuEBao();
			bao.setAlipayName(this.loginName);
			return service.getYuebaoService().getMaxYuEBaoTime(bao);
		}
		return null;
	}
	public int isContinueYuebao(YuEBao pay){
		int num = 0;
		if(pay!=null&&pay.getcTime()!=null){
			//如果lose为空说明没有错误日志
			if(loseYuebao!=null){
				//如果上次采集失败,那么此次需要重新采集 先获取所有的关键id存入hash
				if(setYueTime == null){
					setYueTime = getMaxYuEBaoAssignTime();
				}
				if(setYueTime.contains(DateUtils.formatDate(pay.getcTime(),"yyyy-MM-dd"))){
					num = 1;
				}
			}else{
				if(bigyuebao!=null&&bigyuebao.getcTime().getTime()>pay.getcTime().getTime()){
					num=2;
				}
			}
		}
		return num;
		
	}
	public Set<String> getMaxYuEBaoAssignTime(){
		YuEBao bao = new YuEBao();
		bao.setAlipayName(this.loginName);
		List<String> list = service.getYuebaoService().getMaxYuEBaoAssignTime(bao);
		if(CommonUtils.isNotEmpty(list)){
			setYueTime = new HashSet<String>();
			for (String string : list) {
				setYueTime.add(string.substring(0,string.indexOf(" ")));
			}
		}
 		return setYueTime;
	}
	/**余额宝转入信息,  没调用
	 * @throws Exception */
	@Deprecated
	private void openZhuanchu() throws Exception{
		String url = "https://financeprod.alipay.com/fund/asset.htm";
		String token = getToken(url+"?direction=in");
 		Map<String,String> smap = new HashMap<String, String>();
 		smap.put("_form_token", token);
 		smap.put("direction", "in");
 		smap.put("startDate", "2013-05-01");
 		smap.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
 		Integer page = 1;
// 		page = openPost(url, page,smap);
 		System.out.println("--------余额宝转出信息------------");
 		int result = 0 ;
 		for (int i = 2; i <= page; i++) {
// 			result = openPost(url, i, smap);
 			System.out.println("--------余额宝转出信息------------");
 			if(result==-1){
 				System.out.println("程序出错以后捕捉");
 				break;
 			}
		}
	}
	/**余额宝转出信息 没调用
	 * @throws Exception */
	@Deprecated
	private  void openZhuanru() throws Exception{
		String url = "https://financeprod.alipay.com/fund/asset.htm";
		boolean b = true;
		String token = getToken(url+"?direction=out");
		Map<String,String> smap = new HashMap<String, String>();
		smap.put("_form_token", token);
		smap.put("direction", "out");
		smap.put("startDate", "2013-05-01");
		smap.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
		Integer page = 1;
//		page = openPost(url, page,smap);
		System.out.println("--------余额宝转出信息------------");
		int result = 0 ;
		for (int i = 2; i <= page; i++) {
//			result = openPost(url, i, smap);
			System.out.println("--------余额宝转出信息------------");
			if(result==-1){
				System.out.println("程序出错以后捕捉");
				break;
			}
		}
	}

	/*
	 * 更新用户信息实际是支付宝的
	 */
	public void saveYueBaoUser(String content) {
		User user = null;
		if(content!=null && content.trim().length()>10){
			String ft =  InfoUtil.getInstance().getInfo("ds/yuebao","ft");
			String ft2 =  InfoUtil.getInstance().getInfo("ds/yuebao","ft2");
			Document doc = Jsoup.parse(content);
			Element div =  doc.select(ft).get(0);
			String text1 =  div.text().replace("元", "");
			Element div2 =  doc.select(ft2).get(0);
			String text2 =  div2.text().replace("元", "");
			BigDecimal yBalance = new BigDecimal("0");
			BigDecimal yincome = new BigDecimal("0");
			yBalance = new BigDecimal(text1);
			yincome = new BigDecimal(text2);
			user = new User();
			user.setLoginName(this.loginName);
			user.setyBalance(yBalance);
			user.setYincome(yincome);
			saveUser(user);
		}
	}
	
	
	/*
	 * 开始解析余额宝数据
	 * */
	public  void listLog(String content){
			String ui =  InfoUtil.getInstance().getInfo("ds/yuebao",
						"ui");
			String tr =  InfoUtil.getInstance().getInfo("ds/yuebao",
					"tr");
			String tbody =  InfoUtil.getInstance().getInfo("ds/yuebao",
					"tbody");
			String td =  InfoUtil.getInstance().getInfo("ds/yuebao",
					"td");
			Document doc = Jsoup.parse(content);
			Elements tables =  doc.select(ui);
			Elements trs = tables.get(0).select(tbody).select(tr);
			for(int i=0;i<trs.size();i++){
				try{
					Elements tds = trs.get(i).select(td);
					if(tds!=null && tds.size()>2){
						String date = tds.get(0).text();
						String amount = tds.get(1).text().replace("元", "").trim();//5.64 元
						String amountType =  tds.get(2).text().trim();//收益	
						System.out.println(date+amount+amountType);
						SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");				
						Date cTime=null;
						try {
							cTime = sdf.parse(date);
						} catch (ParseException e1) {	
							log4j.warn("时间格式解析错误#",e1);		
							e1.printStackTrace();
						}
						
						YuEBao yuebao = new YuEBao();
						UUID uuid = UUID.randomUUID();
						yuebao.setId(uuid.toString());
						yuebao.setAlipayName(this.loginName);
						yuebao.setAmount(new BigDecimal(amount));
						yuebao.setAmountType(amountType);
						yuebao.setcTime(cTime);
						int num = isContinueYuebao(yuebao);
						if(num==1){
							continue;
						}else if(num==2){
							isStopYuEBao = true;
							break;
						}
						yuebaoList.add(yuebao);
						}
					}catch(Exception e){
						log4j.writeLogOrder(content, e, "YUEBAO-SHOUYI");
						spiderYuEBaoState = false;
					}
				}
		}
		
	}					
///**余额宝收益信息*/
//public Integer openPost(String url,Integer page,Map<String,String> smap){
//	try{
//		String alipayName = redismap.getString(alipyName);
//		CHeader h = new CHeader("https://financeprod.alipay.com/fund/asset.htm");
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("_form_token",smap.get("_form_token").toString());    		
//		param.put("direction",smap.get("direction").toString());
//		param.put("startDate",smap.get("startDate").toString());	
//		param.put("endDate",smap.get("endDate").toString());	
//		param.put("pageNum",page+"");	
//		
//		String text = cutil.post(url, h,param);
//		System.out.println("---------------------------余额宝第"+page+"页-----------------------------");
////		YuEBaoUtil yuEBaoUtil = new YuEBaoUtil();
//		if(text!=null && text.trim().length()>10){				
////			yuEBaoUtil.listLog( text, service.getYuebaoService(), alipayName);
//			listLog( text, service.getYuebaoService(), alipayName);
//		}
//
//		RegexPaserUtil	rp = new RegexPaserUtil("\"J-calendar-form\">","id=\"J_submit_time",text,RegexPaserUtil.TEXTEGEXANDNRT);
//		String str = rp.getText();
//		rp = new RegexPaserUtil("_form_token\" value=\"","\"",str,RegexPaserUtil.TEXTEGEXANDNRT);
//		str = rp.getText();
//		smap.put("_form_token", str);
//		if(page==1){
//			if(text!=null && text.trim().length()>10){		
////				yuEBaoUtil.saveLog(text, service.getUserService(), service.getYuebaoService(), alipayName);				
//				saveLog(text, service.getUserService(), service.getYuebaoService(), alipayName);				
//			}
//			rp = new RegexPaserUtil("<span class=\"ui-page-bold\">第 1 /","</span>",text,RegexPaserUtil.TEXTEGEXANDNRT);
//	    	str = rp.getText();
//	    	if(str!=null){
//	    		return Integer.parseInt(str.trim());
//	    	}
//		}
//	}catch(Exception e){
//		e.printStackTrace();
//	}
//    return page;
//}
//public void anLyzerAlipayOneYear(String content,String alipayName) {
//Document doc = Jsoup.parse(content);
//Elements elements = doc.select("table[id=tradeRecordsIndex]").first()
//		.select("tbody").select("tr");
//
//java.text.DateFormat format3 = new java.text.SimpleDateFormat(
//		"yyyy.MM.dd HH:mm");
//
//for (int i = 0; i < elements.size(); i++) {
//	Element element = elements.get(i);
//	String times = element.select("td[class=time2]").first().text();
//	Date payTime = null;
//	try {
//		payTime = format3.parse(times);
//	} catch (ParseException e) {
//		log4j.warn("时间格式化错误#", e);
//	}
//
//	String tradeType = element.select("li[class=name fn-claer]")
//			.first().text();// 付款-转账
//	String tradeNo = element.select("li[class=J-bizNo]").first().text();// No
//	String receiverName = element.select("td[class=other]").first()
//			.text();// receriverName
//	String amount = element.select("td[class=amount]").first().text();// amount
//	BigDecimal payAmount = new BigDecimal(amount);
//	String status = element.select("td[class=status]").first().text();// status
//
//	System.out.println(payTime + "&&" + tradeType + "&&" + tradeNo
//			+ "&&" + receiverName + "&&" + payAmount + "&&" + status);
//
//	Map map = new HashMap(2);
//	map.put("tradeNo", tradeNo);
//	map.put("source", Constant.ZHIFUBAO);
//	List<PayInfo> list1 = service.getPayInfoService().getPayInfoByTradeNoSource(map);
//	if (list1 != null && list1.size() > 0) {
//
//	} else {
//		try {
//			String tradeNoType = "交易号";
//			PayInfo payInfo = new PayInfo();
//			UUID uuid = UUID.randomUUID();
//			payInfo.setId(uuid.toString());
//			payInfo.setPayTime(payTime);
//
//			payInfo.setTradeNo(tradeNo);
//			payInfo.setTradeNoType(tradeNoType);
//			payInfo.setTradeType(tradeType);
//			payInfo.setReceiverName(receiverName);
//			payInfo.setAmount(payAmount);
//			payInfo.setStatus(status);
//			payInfo.setSource(Constant.ZHIFUBAO);
//			payInfo.setAlipayName(alipayName);
//			service.getPayInfoService().savePayInfo(payInfo);
//		} catch (Exception e) {
//			log4j.writeLogOrder("支付宝交易记录", e,"PAYINFO");
////			e.printStackTrace();
//
//		}
//	}
//
//}
//}




///**支付宝一年之前交易信息页*/
//public Map<Integer,String> openAlipayJiaoyiInfoYearGet(String url){
//	String text = cutil.get(url,new CHeader("https://lab.alipay.com/consume/record/historyIndexNew.htm"));
////	System.out.println(text);
//	RegexPaserUtil rp = new RegexPaserUtil("<input type=\"hidden\" name=\"_form_token\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//	Map<Integer,String> smap  = new HashMap<Integer,String>();
//	smap.put(2, rp.getText());
//	return smap;
//}
///**交易记录一年之前*/
//public Map<Integer,String> openAlipayJiaoyiInfoYearPost(String url,String page,String beginTime,String criticalDate,String endTime,Map<Integer,String> smap){
//	if(smap.get(2)==null){
//		return null;
//	}
//	CHeader h = new CHeader("https://lab.alipay.com/consume/record/historyIndexNew.htm");
//	smap.put(1,null);
//	Map<String,String> param = new HashMap<String,String>();
//	param.put("_form_token",smap.get(2).toString());    		
//	param.put("customerType","2");
//	param.put("keyword","0_");	
//	param.put("beginTime",beginTime);	
//	param.put("endTime",endTime);	
//	param.put("criticalDate",criticalDate);	
//	param.put("currentPageNo",page);	
//	
//	
//   String text = cutil.post(url, h,param);
//	System.out.println("---------------------------支付宝一年之前交易信息页-----------------------------");
//	String alipayName = redismap.getString(alipyName);
////	Alipay alipay = new Alipay();
////	alipay.anLyzerAlipayOneYear(text,service.getPayInfoService(),  currentUser,alipayName);
//	anLyzerAlipayOneYear(text, alipayName);
//	RegexPaserUtil	rp = new RegexPaserUtil("<a class=\"page-next form-element\" href=\"#\" rel=\"currentPageNo\" rel-value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//    String str = rp.getText();
//	System.out.println("下一页:"+str);
//	rp = new RegexPaserUtil("<input type=\"hidden\" name=\"_form_token\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//	smap.put(1, str);
//	smap.put(2, rp.getText());
//    	
//	return smap;
//}



//try{
//if(url.contains("pageNum=")){
//	page = Integer.parseInt(url.split("pageNum=")[1]);
//}else{
//	page = Integer.parseInt(url.substring(url.length()-1));
//}
//
//}catch(Exception e ){
////System.out.println("解析url错误");
//return false;
//}
//for (int i = 2; i <= page; i++) {
//System.out.println("-----------当前第"+i+"页-------------");
//try {
//	Thread.sleep((long) (100*Math.random()));
//} catch (InterruptedException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//openAlipayJiaoyiInfo(sburl.toString(),i);
//}
///**---------------------------支付宝交易信息页-----------------------------交易记录*/
//public String openAlipayJiaoyiInfo(String url,Integer page){
//	String text = cutil.get(url+page, new CHeader("https://auth.alipay.com/login/loginResultDispatch.htm"));
//	String str = null;
//	if(text!=null){
//		String alipayName = redismap.get(alipyName).toString();
//		try {
//			saveUserOrderByHtmlparser(text, service.getPayInfoService(),  currentUser,alipayName);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		if(page.intValue()==1){
//			RegexPaserUtil	rp = new RegexPaserUtil("<a class=\"page-end\" href=\"","\">尾页",text,RegexPaserUtil.zel_all_chars);
//	    	str = rp.getText();
//	    	System.out.println("总页数:"+str);
//		}
//	}
//	return str;
//}
///*
// * 保存用户订单信息
// */
//public void saveUserOrderByHtmlparser(String content,
//		IPayInfoService payInfoService, String currentUser,
//		String alipayName) throws InterruptedException {
//
//	Document doc = Jsoup.parse(content);
//
//	for (int i = 1; i < 21; i++) {
//		Element element = doc.select("tr[id=J-item-" + i + "]").first();
//		if (element == null) {
//			break;
//		}
//
//		// 交易号码或者流水号码
//		String tradeNo = element.select("a[id=J-tradeNo-" + i + "]")
//				.attr("title").trim();
//		System.out.println(tradeNo);
//
//		Map map = new HashMap(2);
//		map.put("tradeNo", tradeNo);
//		map.put("source", Constant.ZHIFUBAO);
//		List<PayInfo> list1 = payInfoService.getPayInfoByTradeNoSource(map);
//		if (list1 != null && list1.size() > 0) {
//
//		} else {
//			// 2014.03.18
//			String time_d = element.select("p[class=time-d]").first()
//					.text();
//			// 09:44
//			String time_d_m = element.select("p[class=time-h ft-gray]")
//					.first().text();
//			time_d = time_d.replaceAll("\\.", "-");
//			String payTimeStr = time_d + " " + time_d_m;
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//			Date payTime = null;
//			try {
//				payTime = sdf.parse(payTimeStr);
//			} catch (ParseException e1) {
//				// TODO Auto-generated catch block
//				log4j.info("数据解析异常#：" + e1.getMessage());
//				e1.printStackTrace();
//			}
//			// 交易类型
//			String tradeType = element.select("p[class=consume-title]")
//					.first().text();
//			// 交易账号类型：流水号 或者 交易号
//			String tradeNoType = element
//					.select("div[id=J-tradeNo-container-" + i + "]")
//					.first().text().trim().substring(0, 3);
//
//			String receiverName = element.select("td[class=other]").first()
//					.select("p[class=name]").text().trim();
//
//			String amount = element.select("td[class=amount]").text()
//					.trim();
//			BigDecimal payAmount = new BigDecimal(amount);
//			String status = element.select("td[class=status]").text()
//					.trim();
//
//			try {
//				PayInfo payInfo = new PayInfo();
//				UUID uuid = UUID.randomUUID();
//				payInfo.setId(uuid.toString());
//				payInfo.setPayTime(payTime);
//
//				payInfo.setTradeNo(tradeNo);
//				payInfo.setTradeNoType(tradeNoType);
//				payInfo.setTradeType(tradeType);
//				payInfo.setReceiverName(receiverName);
//				payInfo.setAmount(payAmount);
//				payInfo.setStatus(status);
//				payInfo.setSource(Constant.ZHIFUBAO);
//				payInfo.setAlipayName(alipayName);
//				payInfoService.savePayInfo(payInfo);
//			} catch (Exception e) {
//				log4j.writeLogOrder("保存失败#", e, "ALIPAY");
//				e.printStackTrace();
//			}
//		}
//
//	}
//}

///**支付宝的个人信息*/
//public void openInfoPage(){
//	CHeader h = new CHeader("https://auth.alipay.com/login/loginResultDispatch.htm");
//	String text = cutil.get("https://my.alipay.com/portal/account/index.htm", h);
//	if(text!=null){
//		new Alipay().saveUserInfo(text, service.getUserService(), login.getLoginName(), currentUser);	
//	}
//}
//BaseUser bu = new BaseUser();
			// Map baseMap = new HashMap();
			// baseMap.put("phone", phone);
			// baseMap.put("modifyDate", modifyDate);
			// bu.saveUserInfo(userService, baseMap, currentUser);
//
//			Map<String, String> map = new HashMap<String, String>(2);
//			map.put("loginName", loginName);
//			map.put("usersource", Constant.ZHIFUBAO);
//			List<User> list = userService.getUserByUserNamesource(map);
//			if (list != null && list.size() > 0) {
//				User user = list.get(0);
//				user.setLoginName(loginName);
//
//				user.setPhone(phone);
//				user.setRealName(name);
//				user.setSecretLevel(secretLevel);
//				user.setTaobaoName(taobaoName);
//				user.setIdcard(identifyId);
//				user.setIdentifyTime(identifyTime);
//				user.setMajor(major);
//				user.setRegisterDate(registerDate);
//				user.setIsRealName(isRealName);
//				user.setIsProtected(isProtected);
//				user.setIsPhone(isPhone);
//				user.setUsersource(Constant.ZHIFUBAO);
//				user.setUsersource2(Constant.ZHIFUBAO);
//				user.setModifyDate(modifyDate);
//				user.setParentId(currentUser);
//				userService.update(user);
//			} else {