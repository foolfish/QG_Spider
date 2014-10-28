package com.lkb.thirdUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;
import com.lkb.bean.LoseContent;
import com.lkb.bean.Order;
import com.lkb.bean.OrderItem;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.constant.ConstantProperty;
import com.lkb.thirdUtil.base.BasicCommonDs;
import com.lkb.util.DateUtils;
import com.lkb.util.FileUtil;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.util.CommonUtils;

public class JD extends BasicCommonDs{//  BaseInfoBuisness{
	
	private static String orderListURL = "http://order.jd.com/center/list.action";
	private static String userInfoURL = "http://i.jd.com/user/info";
	private static String loginUrl = "https://passport.jd.com/uc/login";
//	private static String loginUrl = "https://passport.jd.com/new/login.aspx";

	private static String jdLoginURL = "https://passport.jd.com/uc/loginService";
	private static String authcodeURL = "https://authcode.jd.com/verify/image?a=1";
	

	public JD(Login login) {
		super(login);
	}

	public static void main(String[] args){
//		JD jd = new JD(new Login("zhaoyulong0626@126.com","test001"),null);
		JD jd = new JD(new Login("cleexiang@126.com","Lixiang880617"),null);
//		jd.index();
//		jd.inputCode(jd.getContext().getImgUrl());
//		jd.logins();
//		try {
//			if(jd.islogin()){
//				jd.gatherUser();
//				jd.gatherUserOrder();
//				jd.test();
//			}else
//			{
//				System.out.println("denglu shibai ");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String text = FileUtil.readFile("d://t.txt");
		try {
			jd.parseOrderOther(text);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void test(){
//		List<SendRequestPojo> list = new ArrayList<SendRequestPojo>();
//		list.add(new SendRequestPojo("http://www.jd.com"));
//		list.add(new SendRequestPojo("http://www.jd.com"));
//		list.add(new SendRequestPojo("http://www.jd.com"));
//		List<String > strs = cutil.getResult(list);
//		for (int i = 0; i < strs.size(); i++) {
//			String string  =  strs.get(i);
//			System.out.println("第"+i+"个"+string);
//		}
//		String text = cutil.get("http://order.jd.com/normal/item.action?orde" +
//				"rid=1705623825&PassKey=256F494CE0C81D0400DCFA1CFEFA3AAD");
//		System.out.println(text);
//		System.out.println(Jsoup.parse(text).getElementById("cano").text());
		
//		String text = cutil.get("http://order.jd.com/center/list.action?d=2&s=4096&t=");
		String text = FileUtil.readFile("D:/t.txt");
		listOrder = new LinkedList<Order>();
		itemList = new LinkedList<OrderItem>();
//		gatherUserOrderSaveAndParse(text);
	}
	
	public void init(){
		if(!context.isInit()){
			Map<String,String> jdMap = new HashMap<String,String>();
			String str = cutil.get(loginUrl);
			if(str!=null){
				Document doc=Jsoup.parse(str);
				String uuid= "";
				String uuidroad = InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
						"uuid");
				String randomroad = InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
						"randomroad");
				String machineNetroad = InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
						"machineNetroad");
				String machineCpuroad = InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
						"machineCpuroad");
				String machineDiskroad = InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
						"machineDiskroad");
				uuid=doc.select(uuidroad).first().attr("value");
				jdMap.put("uuid", uuid);
				String key=doc.select(randomroad).first().child(1).attr("name");
				String value=doc.select(randomroad).first().child(1).attr("value");
				
				String machineNet=doc.select(machineNetroad).attr("value");
				String machineCpu=doc.select(machineCpuroad).attr("value");
				String machineDisk=doc.select(machineDiskroad).attr("value");
				jdMap.put("machineNet", machineNet);
				jdMap.put("machineCpu", machineCpu);
				jdMap.put("machineDisk", machineDisk);
				jdMap.put("sk", key+","+value);
				redismap.put("jdMap", jdMap);
				if (!str.contains("item fore3  hide")) {
					String img = authcodeURL + "&acid=" + uuid + "&uid=" + uuid;
					context.setImgUrl(img);
				}
				context.setInit();
			}
		}
	}
	public JD(Login login, String currentUser) {
		super(login, currentUser);
		this.userSource = Constant.JD;
		this.constantNum = ConstantNum.ds_jd;
	}

	public void login() throws JSONException {
			@SuppressWarnings("unchecked")
			Map<String,String> jmap = ((Map<String,String>)redismap.get("jdMap"));
			String uuid = jmap.get("uuid").toString();
			String url = jdLoginURL+"?uuid="+uuid+"&ReturnUrl=http://www.jd.com/&&r="+Math.random();
			Map<String,String> param = new LinkedHashMap<String,String>();
			param.put("uuid",uuid);
			param.put("loginname", login.getLoginName());
			param.put("nloginpwd", login.getPassword());
			param.put("loginpwd", login.getPassword());
			param.put("&machineNet", "");
			param.put("machineCpu", "");
			param.put("machineDisk","");
			if (login.getAuthcode() != null && login.getAuthcode().trim().length() > 0) {
				param.put("authcode", login.getAuthcode());
			}
			String sk = jmap.get("sk").toString();
			String ss[] = sk.split(",");
			param.put(ss[0],ss[1]);
			CHeader h = new CHeader();
			h.setContent_Type(CHeaderUtil.Content_Type__urlencoded+"; charset=utf-8");
			String text = cutil.post(url, h, param);
			if(text!=null){
				text = text.substring(1,text.length()-1);
				JSONObject json = new JSONObject(text);
				if(text.contains("success")){
					h = new CHeader();
					h.setX_requested_with(true);
					text = cutil.get(userInfoURL, h);
					if(text!=null){
						if (text.contains("NotLogin")||text.contains("登录京东")) {
//							setloginfalse();
						}else{
							loginsuccess();
							addTask(1);
						}
					}
				}
				else if(text.contains("username")){
					output.setErrorMsg(json.getString("username"));
					if(output.getErrorMsg().contains("刷新")){
						output.setErrorMsg(null);
					}
				}else if(text.contains("pwd")){
					output.setErrorMsg(json.getString("pwd"));
				}else if(text.contains("emptyAuthcode")){
					output.setErrorMsg(json.getString("emptyAuthcode"));
					context.setImgUrl("https://authcode.jd.com/verify/image?a=1&acid="+uuid+"&uid="+uuid);
				}else{
					output.setErrorMsg("请填写验证码!");
					context.setImgUrl("https://authcode.jd.com/verify/image?a=1&acid="+uuid+"&uid="+uuid);
				}
				
//				System.out.println("错误信息:"+output.getErrorMsg());
			}
	}
	
	
	public void gatherOrder(){
		try{
		List<String> list = getInitRequestParam();
		//开始采集
		for (int m = 0; m <list.size(); m++) {
			gatherOrderList(list.get(m));
			if(isStop){
				break;
			}
		}
		}catch(Exception e){
			log4j.writeLogOrder("采集出错#", e, "ORDER");
			//最开始想不记录,但是考虑到如果上次是失败,这次要是不记录就会可能出现只更新四个月的,所以还是要设置成false;
			spiderOrderState = false;
		}
	}
	public void gatherOrderList(String param){
		int page =  1; 
		String url = getUrl(param);
		String text = getHtml(url, 1);
		try{
			gatherUserOrderSaveAndParse(text);//查询年份的第一份
			if(!isStop){
				if(text!=null){
					page = getPage(text);
					for (int i = 2; i <= page; i++) {
						gatherUserOrderSaveAndParse(getHtml(url, i));
						if(isStop){
							break;
						}
					}
				}
			}
		}catch(Exception e){
			log4j.writeLogOrder(text, e, "ORDER");
			spiderOrderState = false;
		}
		
	}
	private String getUrl(String param){
		return  orderListURL+"?s=4096&d="+param+"&page=";
	}
	private String getHtml(String url,int page){
		String text = cutil.get(url+page);
		if(text==null){
			spiderOrderState = false;
		}
		return text;
	}
	private Integer getPage(String html) throws Exception{
		int page = 1;
		try{
			String pageNum = new RegexPaserUtil("</span>.*<span class=\"text\">共", "页",html,RegexPaserUtil.TEXTEGEXANDNRT).getText();
			if(pageNum!=null){
				page = Integer.parseInt(pageNum.trim());
				if(page>150){
					page=150;
				}
			}
		}catch(Exception e){
			throw new Exception("分页解析失败,请注意!");
		}
		return page;
	}
	

	
	/**解析账单列表
	 * @param html
	 * @param str
	 * @param page
	 * @throws JSONException 
	 */
	public  void gatherUserOrderSaveAndParse(String html) throws JSONException{
		Order order = null;
		if(html!=null){
//			System.out.println(html);
			String text = null;
			RegexPaserUtil rp = new RegexPaserUtil("<tbody id=\"tb-\\d*","</tbody>");
			if(StringUtils.isNotBlank(html)){
				rp.reset(html);
				Map<String,Order> tempOrder = parseOrderOther(html);
				while(rp.hasNext()){
					try{
						//只抓取已完成
						text = rp.getNext();
//						System.out.println(i+"-----"+text);i++;
						order = new Order();
						order.setId(UUID.randomUUID().toString());
						order.setOrderId(StringUtil.subStr("orderid=", "&", text));
//						System.out.println(order.getOrderId());
						RegexPaserUtil rep = new RegexPaserUtil("<img title=\"","\"");
						rep.reset(text);
						String productNames = "";
						while(rep.hasNext()){
							productNames = productNames+rep.getNextText()+";";
						}
						order.setProductNames(productNames);
						String receiver =  "<"+new RegexPaserUtil("u-name\"","</div>",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
						if(receiver!=null){
							receiver = receiver.replaceAll("<.*?>|<null", "").trim();
						}
						order.setReceiver(receiver);
						
						RegexPaserUtil timerp = new RegexPaserUtil("class=\"ftx-03\">","</span>",text,RegexPaserUtil.TEXTEGEXANDNRT);
						if(timerp.hasNext()){
							try{
								BigDecimal  money = new BigDecimal(new RegexPaserUtil("￥","<",text,RegexPaserUtil.TEXTEGEXANDNRT).getText().trim());
								order.setMoney(money);
								order.setBuyway(new RegexPaserUtil("<br />","<br /",text,RegexPaserUtil.TEXTEGEXANDNRT).getText().trim());
							}catch(Exception e){
								order = tempOrder.get(order.getOrderId());
							}
							String buyTime = timerp.getNextText().replaceAll("<.*?>", "").replaceAll("\\s+", " ");
							order.setBuyTime(DateUtils.StringToDate(buyTime, "yyyy-MM-dd HH:mm:ss"));
							String url = null;
							if(StringUtils.isBlank(order.getOrderstatus())){
								String orderstatus = null;
								if(timerp.hasNext()){
									orderstatus = timerp.getNextText();
								}else{
									//针对特殊情况,比如等待付款
									orderstatus = new RegexPaserUtil("<strong class=\"ftx-04","</strong>",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
									if(orderstatus!=null&&orderstatus.contains(">")){
										orderstatus = "<"+orderstatus;
										orderstatus  = orderstatus.replaceAll("<.*?>", "").trim();
									}
								}
								//url地址
								RegexPaserUtil	rp1 = new RegexPaserUtil("http://order.jd.com/normal/item.action\\?orderid=","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
								url = rp1.getText();
								
								order.setOrderstatus(orderstatus);
							}
							
							order.setLoginName(this.loginName);
							order.setOrdersource(this.userSource);
							int num = isContinue(order);
							if(num==1){
								continue;
							}else if(num==2){
								isStop = true;
								break;
							}
							if(url!=null){
								gatherURLSaveAndParse(cutil.get("http://order.jd.com/normal/item.action?orderid="+url),"http://order.jd.com/normal/item.action?orderid="+url,order,0);
							}
							if(num==3){
								//update
								listUpdateOrder.add(order);
							}else{
								//从小到大的排序
								listOrder.add(order);
							}
						}
						
					}catch(Exception e){
						spiderOrderState = false;
						log4j.writeLogOrder(text, e,ORDER);
					}
				}
			}
		}
	}
	
	public Map<String,Order> parseOrderOther(String html) throws JSONException{
		Map<String,Order> map = null;
		Document doc = Jsoup.parse(html);
		String text = doc.getElementById("pop_sign").text();
		if(StringUtils.isNotBlank(text)){
			map = new HashMap<String, Order>();
			String url = "http://order.jd.com/lazy/getPopOrderInfo.action?callback=jsonp1414414935706&_="+System.currentTimeMillis()+"&jsonPara="+text;
			String result = cutil.get(url);
			if(StringUtils.isNotBlank(result)){
				result = result.replace("\\", "").replaceAll("<.*?>", "");
				result = new RegexPaserUtil("resData\\\":","\\}\\}\\)",result,RegexPaserUtil.TEXTEGEXANDNRT).getText();
				if(result!=null){
					RegexPaserUtil rp = new RegexPaserUtil("\\{\\\"contactName",",\\\"urlPairs",result,RegexPaserUtil.TEXTEGEXANDNRT);
					String text1 = null;
					JSONObject json = null;
					while (rp.hasNext()) {
						text1 = rp.getNext();
						text1 = text1.replace(",\"urlPairs", "")+"}";
						json = new JSONObject(text1);
						Order order = new Order();
						order.setId(UUID.randomUUID().toString());
						order.setLoginName(json.get("contactName").toString());
						order.setOrderId(json.getString("erpOrderId"));
						order.setOrderstatus(json.getString("orderStatus"));
						String productName = json.getString("productName");
						order.setReceiver(json.getString("contactName"));
						if(json.has("no")){
							productName += json.getString("no");
						}else if(json.has("mobile")) {
							productName += json.getString("mobile");
							order.setRecevierTelephone(json.getString("mobile"));
						}
						order.setProductNames(productName);
						order.setBuyway(json.getString("payType"));
						order.setMoney(new BigDecimal(json.getString("price")));
						
						map.put(order.getOrderId(), order);
						
					}
				}
			}
		}
		return map;
	}
	/**解析账单详情 ,已完成订单
	 * @param html
	 * @param url
	 * @param order
	 * @param count 统计该地址被执行几次
	 * @throws Exception 
	 */
	public void gatherURLSaveAndParse(String html, String url,Order order,int count) throws Exception{
		
		if(html!=null){
			String text = null;
			try{
				text = Jsoup.parse(html).getElementById("orderinfo").text();
			}catch(Exception e){
			}
			//-------------取消的账单
			if(text==null){
				Element el = Jsoup.parse(html).getElementById("main");
//					String name = StringUtil.subStr("人：", "地", text).trim();
				String text1 = el.toString();
				if(order!=null){
					String address = new RegexPaserUtil("地址","手机号码|固定电话|电子邮件", text1,RegexPaserUtil.TEXTEGEXANDNRT).getText();
					if(address!=null){
						order.setRecevierAddr(address.replaceAll("<.*?>", "").trim());
					}
					RegexPaserUtil rp1= new RegexPaserUtil("固定电话</td> ","手机|</td>",text1,RegexPaserUtil.TEXTEGEXANDNRT);
					String fix = rp1.getText();
					if(fix!=null){
						order.setRecevierFixPhone(fix.trim());				
					}
//						System.out.println(el);
					rp1 = new RegexPaserUtil("手机号码：","</td>",text1,RegexPaserUtil.TEXTEGEXANDNRT);	
					fix = rp1.getText();
					if(fix!=null){
						order.setRecevierTelephone(fix.replaceAll("<.*?>", "").trim());
					}
					rp1 = new RegexPaserUtil("电子邮件</td>","</td>",text1,RegexPaserUtil.TEXTEGEXANDNRT);	
					fix = rp1.getText();
					if(fix!=null){
						order.setRecevierEmail(fix.replaceAll("<.*?>", "").trim());
					}
				}
				try{
					String orderid = StringUtil.subStr("订单编号", "支付方式",el.text()).trim();
					Elements els = el.select("tbody").select("tr");
//					System.out.println(els);
					for (int i = 0; i < els.size(); i++) {
						Elements elss = els.get(i).select("td");
						if(elss.size()==6){
							OrderItem item = new OrderItem();
							item.setId(UUID.randomUUID().toString());
							item.setOrderTId(orderid);
							item.setProductId(elss.get(0).text());
							item.setProductName(elss.get(1).text());
							String price = elss.get(2).text();
							if(price.contains("￥")){
								item.setPrice(new BigDecimal(price.replace("￥","").trim()));
							}
							item.setNum(Integer.parseInt(elss.get(4).text().trim()));
							itemList.add(item);
						}else{
							break;
						}
					}
				}catch(Exception e){
					log4j.writeLogOrder(html,e,ORDERITEM);
					createLoseContent(url,count,orderItemType);
				}
			}else{
				//------------------以完成的文件
//				String name = StringUtil.subStr("人：", "地", text).trim();
				String address = new RegexPaserUtil("址：","手机号码|固定电话", text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
				String orderText =  new RegexPaserUtil("操作","还要买", text,RegexPaserUtil.TEXTEGEXANDNRT).getText()+"还要买";
				RegexPaserUtil rp = new RegexPaserUtil(" ","还要买");
				rp.reset(orderText);
				String orderidtext = null;
				if(order!=null){
					order.setRecevierAddr(address);
				}
				try{
				String orderid = new RegexPaserUtil("订单号：", "&nbsp",html,RegexPaserUtil.TEXTEGEXANDNRT).getText();
				while(rp.hasNext()){
					OrderItem item = new OrderItem();
					item.setOrderTId(orderid);
					item.setId(UUID.randomUUID().toString());
					orderidtext = rp.getNextText().trim();
					item.setProductId(orderidtext.substring(0,orderidtext.indexOf(" ")));
					int priceIndex = orderidtext.indexOf("￥");
					item.setProductName(orderidtext.substring(orderidtext.indexOf(" "),priceIndex).trim());
					orderidtext = StringUtil.subStr("￥", "申请返", orderidtext);
					String strs[] = orderidtext.split(" ");
					item.setPrice(new BigDecimal(strs[0].trim()));
					item.setNum(Integer.parseInt(strs[2].trim()));
					itemList.add(item);
				}
				}catch(Exception e){
					log4j.writeLogOrder(html,e,ORDERITEM);
					createLoseContent(url,count,orderItemType);
				}
			}
		
		}else{
			throw new Exception("请求结果为空!");
		}
	}
	
	
	public void gatherHistoryLoseURL(){
		List<LoseContent> list = null;
		try{
			list = getMaxLoseTime(orderItemType);
		}catch(Exception e){
			e.printStackTrace();
			log.error("连接数据库异常#");
		}
		String text = null;
		if(CommonUtils.isNotEmpty(list)){
			for (LoseContent lose : list) {
				text = cutil.get(lose.getUrl());
				try {
					if(lose.getCount()==null){
						lose.setCount(0);
					}
					gatherURLSaveAndParse(text, lose.getUrl(), null,(lose.getCount()+1));
				} catch (Exception e) {
					log4j.writeLogOrder(text,e,ORDERITEM);
					createLoseContent(lose.getUrl(),(lose.getCount()+1),orderItemType);
				}
			}
			deleteType(orderItemType);
		}
	}
	
	
		
	@Override
	public User gatherUser() throws Exception {
		//http://i.jd.com/user/userinfo/showBaseInfo.action
		String content= cutil.get(userInfoURL);
//		System.out.println(content);
		if(content!=null){
			Document doc = Jsoup.parse(content);
			String petName="";
			String sex="";
			String realName="";
			String addr="";
			try{
				petName=doc.select("input[id=nickName]").first().attr("value");
				if(doc.toString().contains("0==0")){
					sex="男";
				} else if(doc.toString().contains("0==1")){
					sex="女";
				}else{
					sex="保密";
				}
				realName=doc.select("input[id=realName]").attr("value");
				addr=doc.select("input[id=address]").attr("value");
			} catch(Exception e){
				log4j.writeLogByMyInfo(e);
			}
			
			User user = new User();
			user.setUserName(login.getLoginName());
			user.setPetName(petName);
			user.setRealName(realName);
			user.setAddr(addr);
			user.setSex(sex);
			return user;
		}
		return null;
	}

	public List<String> getInitRequestParam(){
		//----获取年份 查询年份
		List<String> list = new ArrayList<String>();
		String text = cutil.get("http://order.jd.com/center/list.action");
	    Document doc = Jsoup.parse(text);
	    Elements els =  doc.getElementById("submitDate").select("option");
	    RegexPaserUtil rp = new RegexPaserUtil("value=\"", "\"");
	    //解析查询的年份
	    for (int i = 1; i < els.size(); i++) {
	    	list.add(rp.reset(els.get(i).toString()).getText());
		}
	    return list;
	}
	@Override
	public void startSpider(int type) {
		switch (type) {
		case 1:
			saveUser();
			saveAll();
			break;

		default:
			break;
		}
		
	}
//	public Order getMaxOrderTime(){
//		if(isTest()){
//			return null;
//		}
//		Order order = new Order();
//		order.setLoginName(this.loginName);
//		order.setOrdersource(this.userSource);
//		return service.getOrderService().getMaxOrderTime(order);
//	}
	
	
}



///*
// * 查找当前用户的地址列表;
// * 当用户recevier是NUll时，再查找用户
// * */
//public List<Map> addrList1(String currentId,IUserService userService,IOrderService orderService,String receiver){
//	String realName = receiver;
//	String loginName = "";
//	if(receiver==null){
//		User user = userService.findById(currentId);
//		realName = user.getRealName();
//		loginName = user.getLoginName();
//		
//	}		
//	Map map = new HashMap(2);
//	map.put("receiver", realName);
//	map.put("loginName", loginName);		
//	List<Map> list = orderService.getAddrByReceiver(map);
//	return list;
//} 
///*
// * 保存用户订单信息
// */
//public void saveUserOrderByHtmlparser(IUserService userService,IOrderService orderService, IOrderItemService orderItemService) {
//		String urlpre= orderListURL+"?page=";
//		List<String> objectTmp = new ArrayList<String>();
//		int tempsize = 5;
//		int time =1;
//		int page = 1000; //最大值1000以内
//		CHeader h = new CHeader();
//		String content = null;
//		while(true){
//			String url = urlpre+time*tempsize;
//			content = cutil.get(url, h);
//			if(!content.contains("暂无订单，这就去挑选商品")){
//				for(int i=1;i<=5;i++){
//					int cpage = (time-1)*5+i;  
//					objectTmp.add(urlpre+cpage);
//				}
//				time++;
//			}else{
//				for(int i=1;i<=4;i++){
//					int cpage = (time-1)*5+i;
//					objectTmp.add(urlpre+cpage);
//				}
//				break;
//			}
//			if(page>1000){
//				System.out.println("程序有可能死循环,检查程序是否正确!--------,我是京东分页部分");
//				break;
//				
//			}
//			page++;
//		}
//	
//		
//		Map tmap = new HashMap();
//		tmap.put("loginName", login.getLoginName());
//		tmap.put("ordersource", Constant.JD);
//		List<Map> list2= orderService.getRecentDay(tmap);
//		Date rDay = null;
//		if(list2!=null && list2.size()>0 && list2.get(0).get("buyTime")!=null){
//			String buyTime = list2.get(0).get("buyTime").toString();		
//			rDay = DateUtils.StringToDate(buyTime,  "yyyy-MM-dd HH:mm:ss");
//			rDay = DateUtils.add(rDay, Calendar.DATE, ConstantProperty.days);
//		}
//		
//		
//		for(int i=0;i<objectTmp.size();i++){
//			String url = objectTmp.get(i);
//			String loginName = login.getLoginName();
//		
//			Boolean flag = get( url,loginName,userService, orderService,  orderItemService,rDay);
//			if(flag==true){
//				break;
//			}
//		}
//}
//    	
//        public Boolean get(String url,String loginName,IUserService userService,IOrderService orderService, IOrderItemService orderItemService,Date rDay) {
//        	
//        	
//        	Boolean jflag = false;
//            try {
//            	 String content = cutil.get(url);
//            	 if(content.contains("暂无订单，这就去挑选商品")){
//         			
//         		}else{
//         			String tbodyroad = InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"tbody");
//         			String aroad = InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"a");
//         			String tdroad = InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"td");	         			
//         			String idroad =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"id");
//         			String order02 =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"order02");
//         			String mc =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"mc");
//         			String tcol1 =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"tcol1");
//         			String href =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"href");
//         			
//         			String pD1 =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//        					"pD1");
//         			try{
//         			Document doc = Jsoup.parse(content);    
//         			Elements  orderElements = doc.select(order02).first().select(mc).first().select(tbodyroad);
//         			
//         	
//         			for (int i = 1; i < orderElements.size(); i++) {
//         				Element element = orderElements.get(i);
//         				String attrId = element.attr(idroad);
//         				if(!attrId.contains("parent-")){
//         					// 订单ID
//         					String orderId = element.select(tcol1).first().select(aroad).text();
//         					System.out.println(orderId);
//         					
//         			
//         						// 订单url
//         						
//         						String orderIdUrl = element.select(tcol1).first().select(aroad).attr(href);
//         						// 收货人
//         						String receiver = element.select(tdroad).get(2).text().trim();
//         						
//         			
//         						// 订单金额 收货方式
//         						String textnode5str = element.select(tdroad).get(3).text();
//         						BigDecimal money = new BigDecimal("0.00");
//         						String buyway = "";
//         						if(textnode5str!=null && textnode5str.trim().length()>0){
//         							String new5str = textnode5str.replace("\r", "").replace("\n", "").replace("\t", "").replace("￥", "").trim();
//         							String moneyStr = new5str.replace("在线支付", "").replace("货到付款", "").replace("上门自提", "").trim();
//         							String[] str5s = new5str.split(moneyStr);
//         							money = new BigDecimal(moneyStr.trim());
//         							buyway = str5s[1].trim();
//         						}
//
//         						// 下单时间
//         						
//         						SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//         						
//         						String buyTimestr =  element.select(tdroad).get(4).text();
//         						Date buyTime=null;
//         						try {
//         							buyTime = sdf.parse(buyTimestr);
//         						} catch (ParseException e1) {
//         							logger.info("第477行捕获异常："+e1.getMessage());
//         							e1.printStackTrace();
//         						}
//         						// 
//         						if(rDay!=null&&buyTime!=null){
//         							if(rDay.after(buyTime)){
//         								return true;
//         							}
//         						}
//         						
//         						// 订单状态
//         						String orderstatus = element.select(tdroad).get(5).text().trim().replace("跟踪", "");
//         						
//         						String orderdetailhtml = cutil.get(orderIdUrl);
//         						if(orderdetailhtml!=null && orderdetailhtml.trim().length()>100){
//         							try{
//         							Document document = Jsoup.parse(orderdetailhtml);
//	         						String recevierAddr = "";
//	         						String recevierFixPhone = "";
//	         						String recevierTelephone = "";
//	         						String recevierEmail = "";
//	         						if(orderstatus.trim().equals("已取消")){
//	         							if(document.select(pD1).size()>0){
//	         								 String elements = document.select(pD1).get(1).text().trim().replace("收货人信息", "");
//	         								String[] s111 = {};
//	         								 if(elements.contains("地址：")){
//	         									 s111 = elements.split("地址："); 
//	         								 }
////	         								String[] s112 = {};
////	         								 if( s111[1].contains("电子邮件：")){ 
////	         									  s112 = s111[1].split("电子邮件："); 
////	         								 }
//	         								String[] s113 = {};
//	         								if( s111[1].contains("固定电话：")){		         									
//	         									  s113 = s111[1].split("固定电话：");		         								 
//	         								 }
//	         							    String[] s114 = {};
//	         								if( s113[1].contains("手机号码：")){		         									
//		         								  s114 = s113[1].split("手机号码：");
//	         								 }
//	         								String[] s115 = {};
//	         								if( s114[1].contains("电子邮件：")){
//		         								 s115 = s114[1].split("电子邮件：");			         								 
//	         								 }
//	         								 
//	         								
//	         								
//	         								 recevierAddr = s113[0].trim();
//	         								 recevierFixPhone = s114[0].trim();
//	         								 recevierTelephone = s115[0].trim();
//	         								 if(s115.length>1){
//	         									recevierEmail = s115[1].trim();
//	         								 }
//	         								 
//	         								 
//	         								Map map = new HashMap(2);
//         									map.put("orderId", orderId);
//         									map.put("ordersource", Constant.JD);
//         									List<Order> list1 = orderService.getOrderByOrderIdsource(map);
//         									if(list1!=null&&list1.size()>0){
//         										
//         									}else{
//         										try {
//         											Order order = new Order();
//         											UUID uuid = UUID.randomUUID();
//         											order.setId(uuid.toString());
//         											order.setOrderId(orderId);
//         											//order.setProductNames(productName);
//         											order.setReceiver(receiver);
//         											order.setMoney(money);
//         											order.setBuyway(buyway);
//         											order.setBuyTime(buyTime);
//         											
//         											order.setOrderstatus(orderstatus);
//         											order.setOrdersource(Constant.JD);
//         											order.setLoginName(loginName);
//         										
//         											order.setRecevierAddr(recevierAddr.replaceAll(" ", ""));
//         										
//         											order.setRecevierEmail(recevierEmail);
//         											order.setRecevierFixPhone(recevierFixPhone);
//         											order.setRecevierTelephone(recevierTelephone);
//         											
//         											
//         											
//         											orderService.saveOrder(order);
//         											
//         									
//         											//保存子订单信息
//         											String marginb10 =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"marginb10");
//         											String tr =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"tr");
//         											String breadcrumb =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"breadcrumb");
//         											String strong =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"strong");
//         											 Elements proElements1  = document.select(marginb10).first().select(tr);
//        	         								 for(int j=1;j<proElements1.size();j++){
//        	         									 Element pElement = proElements1.get(j);
//        	         									 String productId = pElement.select(tdroad).get(0).text();
//        	         									 String productName = pElement.select(tdroad).get(1).text();
//        	         									 String productHref = pElement.select(tdroad).get(1).select(aroad).first().attr(href);
//        	         									 
//        	         									 String productType = "";
//        	         									 try{
//        	         										 Document dos = Jsoup.connect(productHref).get();
//        	         										 
//        	         										 if(dos.select(breadcrumb).first().select(strong).size()>0){
//        	         											 productType =  dos.select(breadcrumb).first().select(strong).first().text();
//        	         										 }else{
//        	         											String all =  dos.select(breadcrumb).first().text();
//        	         											String[] strs = all.split(">");
//        	         											productType = strs[0];
//        	         										 }
//        	         									 }catch(Exception e){
//        	         										logger.info("第566行捕获异常：",e);		
//        	         										//e.printStackTrace();
//        	         									 }
//        	         									  
//        	         									 String priceStr = pElement.select(tdroad).get(2).text().trim().replace("￥", "");
//        	         									 BigDecimal price = new BigDecimal(priceStr);
//        	         									 
//        	         									 int num = Integer.parseInt(pElement.select(tdroad).get(4).text());
//        	         									 
//        	         									OrderItem orderItem = new  OrderItem();
//        	         									UUID uuid2 = UUID.randomUUID();
//        	         									orderItem.setId(uuid2.toString());
//        	         									orderItem.setOrderTId(uuid.toString());
//        	         									orderItem.setProductId(productId);
//        	         									orderItem.setPrice(price);
//        	         									orderItem.setProductType(productType);
//        	         									orderItem.setNum(num);
//        	         									orderItem.setProductName(productName);
//        	         									orderItemService.saveOrderItem(orderItem);
//        	         								 }
//        	         								 
//         											
//         											
//         										} catch (Exception e) {
//         											logger.info("第590行捕获异常：",e);		
//         											e.printStackTrace();
//
//         										}
//         									}	
//         									
//	         								 
//	         								 
//	         								
//	         									
//	         							}	
//	         						}else{
//	         							Boolean flag = true;
//	         							Elements elements1 = null;
//	         							String orderinfo =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//						        					"orderinfo");
//	         							String dl =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//						        					"dl");
//	         							String li =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//						        					"li");
//	         							try{
//	         								 elements1 = document.select(orderinfo).first().select(mc);
//	         							}catch(Exception e){
//	         								 flag = false;;
//	         							}
//	         							 
//	         							if(flag)
//	         							if(elements1.size()>0){
//	         								 Elements elements = elements1.first().select(dl).first().select(li);
//	         								 recevierAddr = elements.get(1).text().trim().replace("地????址：", "").replace("地    址：", "").replace("地????址：", "").replace("地????址：", "").replace("地??????址：", "").trim();
//	         								 if(elements.size()>2 && elements.get(2).text().contains("固定电话：")){
//	         									recevierFixPhone = elements.get(2).text().replace("固定电话：", "").trim();
//	         								 }else if(elements.size()>2 && elements.get(2).text().contains("手机号码：")){
//	         									 recevierTelephone = elements.get(2).text().replace("手机号码：", "").trim();
//	         								 }
//	         								 if(elements.size()>3 && elements.get(3).text().contains("固定电话：")){
//		         									recevierFixPhone = elements.get(3).text().replace("固定电话：", "").trim();
//		         							 }else if(elements.size()>3  && elements.get(3).text().contains("手机号码：")){
//		         									 recevierTelephone = elements.get(3).text().replace("手机号码：", "").trim();
//		         						     }
//		         								 
//	         								if(elements.size()>4 && elements.get(4).text().contains("电子邮件：")){
//	         									 recevierEmail = elements.get(4).text().replace("电子邮件：", "").trim();
//	         							     }
//	         								
//	         								Map map = new HashMap(2);
//         									map.put("orderId", orderId);
//         									map.put("ordersource", Constant.JD);
//         									List<Order> list1 = orderService.getOrderByOrderIdsource(map);
//         									if(list1!=null&&list1.size()>0){
//         										
//         									}else{
//         										try {
//         											Order order = new Order();
//         											UUID uuid = UUID.randomUUID();
//         											order.setId(uuid.toString());
//         											order.setOrderId(orderId);
//         											//order.setProductNames(productName);
//         											order.setReceiver(receiver);
//         											order.setMoney(money);
//         											order.setBuyway(buyway);
//         											order.setBuyTime(buyTime);
//         											order.setOrderstatus(orderstatus);
//         											order.setOrdersource(Constant.JD);
//         											order.setLoginName(loginName);
//         											order.setRecevierAddr(recevierAddr.replaceAll(" ", ""));
//         											order.setRecevierEmail(recevierEmail);
//         											order.setRecevierFixPhone(recevierFixPhone);
//         											order.setRecevierTelephone(recevierTelephone);
//         											
//         											orderService.saveOrder(order);
//         											
//         										
//         											String list =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"list");
//         											String tr =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"tr");
//         											String a =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"a");
//         											String strong =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"strong");
//         											String breadcrumb =  InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//         						        					"breadcrumb");
//         											 Elements proElements1 = document.select(orderinfo).first().select(list).first().select(tr); 
//        	         								 for(int j=1;j<proElements1.size();j++){
//        	         									 Element pElement = proElements1.get(j);
//        	         									 int size = pElement.select(tdroad).size();
//        	         									 String productId = "";
//        	         									 String productName = "";
//        	         									 String productHref = "";
//        	         									 String productType = "";
//        	         									 BigDecimal price = new BigDecimal("0");
//        	         									 
//        	         									 int num = 1;
//        	         									 
//        	         									 if(size==6){
//        	         										  productId = pElement.select(tdroad).get(0).text();
//        	         										  productName = pElement.select(tdroad).get(1).text();
//        	         										  productHref = pElement.select(tdroad).get(1).select(a).first().attr(href);
//        	         										  productType = "";
//        	         										 if(productHref!=null && !productHref.trim().equals("")){
//        	         											 try{
//        	         												 Document dos = Jsoup.connect(productHref).get();
//        	         												 
//        	         												 if(dos.select(breadcrumb).first().select(strong).size()>0){
//        	         													 productType =  dos.select(breadcrumb).first().select(strong).first().text();
//        	         												 }else{
//        	         													String all =  dos.select(breadcrumb).first().text();
//        	         													String[] strs = all.split(">");
//        	         													productType = strs[0];
//        	         												 }
//        	         											 }catch(Exception e){
//        	         												 e.printStackTrace();
//        	         												logger.info("第677行捕获异常：",e);		
//        	         												
//        	         											 }
//        	         										 }
//        	         										
//        	         										 
//        	         										 String priceStr = pElement.select(tdroad).get(2).text().trim().replace("￥", "");
//        	         										 price = new BigDecimal(priceStr);
//        	         										 
//        	         										 num = Integer.parseInt(pElement.select(tdroad).get(4).text());
//        	         									 }else if(size==7){
//        	         										  productId = pElement.select(tdroad).get(0).text();
//        	         										  productName = pElement.select(tdroad).get(2).text();
//        	         										  productHref = pElement.select(tdroad).get(2).select(a).first().attr(href);
//        	         										  productType = "";
//        	         										 if(productHref!=null && !productHref.trim().equals("")){
//        	         											 Document dos = Jsoup.connect(productHref).get();
//        	         											 
//        	         											 if(dos.select(breadcrumb).first().select(strong).size()>0){
//        	         												 productType =  dos.select(breadcrumb).first().select(strong).first().text();
//        	         											 }else{
//        	         												String all =  dos.select(breadcrumb).first().text();
//        	         												String[] strs = all.split(">");
//        	         												productType = strs[0];
//        	         											 }
//        	         										 }
//        	         										
//        	         										 
//        	         										 String priceStr = pElement.select(tdroad).get(3).text().trim().replace("￥", "");
//        	         										 price = new BigDecimal(priceStr);
//        	         										 
//        	         										 num = Integer.parseInt(pElement.select(tdroad).get(5).text());
//        	         									 }
//        	         									 
//        	         									OrderItem orderItem = new  OrderItem();
//        	         									UUID uuid2 = UUID.randomUUID();
//        	         									orderItem.setId(uuid2.toString());
//        	         									orderItem.setOrderTId(uuid.toString());
//        	         									orderItem.setProductId(productId);
//        	         									orderItem.setPrice(price);
//        	         									orderItem.setProductType(productType);
//        	         									orderItem.setNum(num);
//        	         									orderItem.setProductName(productName);
//        	         									orderItemService.saveOrderItem(orderItem);
//        	         					     	         									 
//        	         								 
//        	         								 }
//         											
//         										} catch (Exception e) {
//         											logger.info("第727行捕获异常：",e);		
//         											e.printStackTrace();
//         										}
//         									}	
//	         							}	
//	         						}
//         						}
//         						catch(Exception e){
//         							logger.info("第817行捕获异常：",e);		
//         							String warnType = WaringConstaint.JD_4;
//         							WarningUtil wutil = new WarningUtil();
////         							wutil.warning(warningService, currentUser, warnType);
//         						}
//         						}
//         							
//         						
//         				}
//         				
//         				}
//
//         		
//         			}
//         			catch(Exception e){
//         				logger.info("第856行捕获异常：",e);		
//							String warnType = WaringConstaint.JD_3;
//							WarningUtil wutil = new WarningUtil();
////							wutil.warning(warningService, currentUser, warnType);
//         			}
//         		}
//            	 
//            	 
//               
//            } catch (Exception ex) {
//            	logger.info("第770行捕获异常："+ex.getMessage());
//            	ex.printStackTrace();
//               
//            }
//            
//            return jflag;
//        }
///*
// * 查找当前用户的地址列表;
// * 当用户recevier是NUll时，再查找用户
// * */
//public List<Map> addrList(String currentId,IUserService userService,IOrderService orderService,String receiver){
//	String realName = receiver;
//	String loginName = "";
//	if(receiver==null){
//		User user = userService.findById(currentId);
//		realName = user.getRealName();
//		loginName = user.getLoginName();
//				
//	}		
//	Map map = new HashMap(2);
//	map.put("receiver", realName);
//	map.put("loginName", loginName);		
//	List<Map> list = orderService.getAddrByReceiver(map);
//	return list;
//} 
//
//


///*
// * 开始抓取会员信息
// * */
//public void parseBegin(IUserService userService,IOrderService orderService, IOrderItemService orderItemService,IParseService parseService){
//	String userId = currentUser;
//	String loginName = login.getLoginName();
//	String userSource = Constant.JD;
//	try{
////		parseBegin(parseService, userId ,  loginName,  userSource);
//		String content= cutil.get(userInfoURL);
//		if(content!=null){
//			Document doc=null;
//			doc=Jsoup.parse(content);
////			savaUserInfoByJsoup(doc,userService);
//			saveUserOrderByHtmlparser( userService,orderService,  orderItemService);
//		}	
////		close();
//	}catch(Exception e){
//		
//	}finally{
////		parseEnd(parseService, userId ,  loginName,  userSource);
//	}
//}
//public Boolean savaUserInfoByJsoup(Document doc,IUserService userService){
//	
//	Boolean flag = false;
//	String petName="";
//	String sex="";
//	String realName="";
//	String addr="";
//	String  nickName= InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//			"nickName");
//	String  realName1= InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//			"realName");
//	String  address= InfoUtil.getInstance().getInfo(ConstantProperty.ds_jd,
//			"address");
//	try{
//	petName=doc.select(nickName).first().attr("value");
//	//String memberLevel=doc.select("a[href=http://usergrade.jd.com/user/grade]").text().trim().replace("我的级别 ", "");
//	//String buyLevelAWhole=doc.select("i[class^=rank-sh rank-sh0]").attr("class");
//	//String buyLevel=buyLevelAWhole.substring(buyLevelAWhole.length()-1,buyLevelAWhole.length());
//	//String memberType=doc.select("div[id=changeType]").text();
//	//String petName=doc.select("[id=petName]").attr("value");
//	
//	if(doc.toString().contains("0==0")){
//		sex="女";
//	}
//	else if(doc.toString().contains("0==1")){
//		sex="保密";
//	}
//	else{
//		sex="男";
//	}
////	String phone=doc.select("input[name=userInfo.usermob").attr("value");
////	String email=doc.select("div[class=item").get(6).select("strong").text();
//	realName=doc.select(realName1).attr("value");
////	String idcard=doc.select("div[class=item").get(8).select("input[id=ucid]").attr("value");
//	addr=doc.select(address).attr("value");
//	
//	}
//	catch(Exception e){
//		String warnType = WaringConstaint.JD_2;
//		WarningUtil wutil = new WarningUtil();
////		wutil.warning(warningService, currentUser, warnType);
//		logger.info("第294行捕获异常：",e);		
//		e.printStackTrace();
//	}
//	
//	Date modifyDate = new Date();
////	BaseUser bu = new BaseUser();
////	Map baseMap = new HashMap();
////	baseMap.put("addr", addr);
////	baseMap.put("sex", sex);
////	baseMap.put("modifyDate", modifyDate);
////	bu.saveUserInfo(userService, baseMap, currentUser);	
//	
//	Map<String, String> map = new HashMap<String, String>(2);
//	map.put("parentId", currentUser);
//	map.put("usersource", Constant.JD);
//	List<User> list = userService.getUserByParentIdSource(map);
//	if (list != null && list.size() > 0) {
//		User user = list.get(0);
//		user.setLoginName(login.getLoginName());
//		
//		user.setUserName(login.getLoginName());
////		user.setMemberLevel(memberLevel);
////		user.setBuyLevel(buyLevel);
////		user.setMemberType(memberType);
//		user.setPetName(petName);
////		user.setPhone(phone);
////		user.setEmail(email);
//		user.setRealName(realName);
////		user.setIdcard(idcard);
//		user.setAddr(addr);
//		user.setUsersource(Constant.JD);
//		user.setUsersource2(Constant.JD);
//		user.setSex(sex);
//		user.setParentId(currentUser);
//		user.setModifyDate(modifyDate);
//		userService.update(user);
//	} else {
//		try {
//			User user = new User();
//			UUID uuid = UUID.randomUUID();
//			user.setId(uuid.toString());
//			user.setLoginName(login.getLoginName());
//			//user.setLoginPassword(login.getPassword());
//			user.setUserName(login.getLoginName());
////			user.setMemberLevel(memberLevel);
////			user.setBuyLevel(buyLevel);
////			user.setMemberType(memberType);
//			user.setPetName(petName);
////			user.setPhone(phone);
////			user.setEmail(email);
//			user.setRealName(realName);
////			user.setIdcard(idcard);
//			user.setAddr(addr);
//			user.setUsersource(Constant.JD);
//			user.setUsersource2(Constant.JD);
//			user.setSex(sex);
//			user.setModifyDate(modifyDate);
//			user.setParentId(currentUser);
//			userService.saveUser(user);
//		} catch (Exception e) {
//			logger.info("第294行捕获异常：",e);		
//			e.printStackTrace();
//		}
//	}
//
//	return flag;
//	
//}
