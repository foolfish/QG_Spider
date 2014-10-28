package com.lkb.thirdUtil;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.Order;
import com.lkb.bean.OrderItem;
import com.lkb.bean.PayInfo;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IOrderItemService;
import com.lkb.service.IOrderService;
import com.lkb.thirdUtil.base.BasicCommonDs;
import com.lkb.thirdUtil.ds.ZhiFuBao;
import com.lkb.util.DateUtils;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.StringUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.taobao.alipay.TimeUtil;

public class TaoBao extends BasicCommonDs{//BaseInfoBuisness{
	private static Logger logger = Logger.getLogger(TaoBao.class);

	private static String orderListURL = "http://trade.taobao.com/trade/itemlist/listBoughtItems.htm?action=itemlist/QueryAction&event_submit_do_query=1&pageNum=";
	private static String userInfoURL = "http://i.taobao.com/user/baseInfoSet.htm";
	private static String loginUrl = "https://login.taobao.com/member/login.jhtml";
	private static String alipayUrl = "http://member1.taobao.com/member/fresh/account_management.htm";
	private static String yuebaoUrl = "https://financeprod.alipay.com/fund/asset.htm";
	private static String orderDetailURL = "http://trade.taobao.com/trade/detail/trade_item_detail.htm?bizOrderId=";
	
	
	public static String TPL_checkcode = "";
	public static String need_check_code = "";//跟上边对应
	public static String newlogin = "1";//跟上边对应
	public static String tid = "XOR_1_000000000000000000000000000000_63504554470A7C717375720C";
	public static String pstrong = "2";
	public static String poy = "XOR_1_000000000000000000000000000000_625A424A45137C6F7A7F047A62";//
	public static String sub = "";
	public static String callback="1";
	public static String umto="NaN";
	
	//参数
	public static String token = "taobao_token";
	public static String sk = "taobao_sk";
	public static String duanxinMap = "taobao_duanxinMap";
	public static String taobao_index = "taobao_taobao_index";
	
	
	public TaoBao(Login login,  String currentUser) {
//		super(login, ConstantNum.ds_taobao, currentUser);
		super(login,  currentUser);
		this.userSource = Constant.TAOBAO;
		this.constantNum = ConstantNum.ds_taobao;
	}
	
	public void init(){
		if(!context.isInit()){
			try{
				String text = cutil.get("https://login.taobao.com/member/login.jhtml");
				RegexPaserUtil rp = new RegexPaserUtil("id=\"J_StandardCode_m\" src=\"https://s.tbcdn.cn/apps/login/static/img/blank.gif\" data-src=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
				text = rp.getText();
				if(text!=null){
					text = text.replace("&amp;","&");
					context.setImgUrl(text);
				}
				Map<String,String> param = new HashMap<String,String>();
				param.put("ua", "");
//				param.put("username",URLEncoder.encode(TPL_username,"utf-8"));
				param.put("username",login.getLoginName());
				text = cutil.post("https://login.taobao.com/member/request_nick_check.do?_input_charset=utf-8", param);
				//{"needcode":false} 不需要验证码
				//{"needcode":true,"url":"https://pin.aliyun.com/get_img?sessionid=b2413d9419797faf31cad98d7cbde6f3&identity=taobao.login&type=150_40"}输入验证码
		    	if(text.contains("true") && text.contains("url")){
		    		JSONObject json = new JSONObject(text);
		    		context.setImgUrl(json.get("url").toString());
		    	}
		    	context.setInit();
			}catch(Exception e){}
		}
	}
	public void login(){
		output.put("phone", "");
		try{
			login_1();
			if(StringUtils.isBlank(output.getErrorMsg())){
				login_2();
			}
		}catch(Exception e){
			log4j.login(e);
		}
	}
	private void login_1() throws JSONException{
		CHeader h = new CHeader("https://login.taobao.com/member/login.jhtml");
		h.setContent_Type(CHeaderUtil.Content_Type__urlencoded+"; charset=UTF-8");
		Map<String,String> param = new HashMap<String,String>();
		param.put("ua", "");    		param.put("TPL_username",login.getLoginName());
		param.put("TPL_password",login.getPassword());		param.put("loginsite","0");
		param.put("TPL_redirect_url","");				param.put("from","tb");
		param.put("fc","default");			   	param.put("style","default");
		param.put("css_style","");				param.put("support","000001");
		param.put("CtrlVersion","1%2C0%2C0%2C7");	param.put("loginType","3");
		param.put("minititle","");					param.put("minipara","");
		param.put("llnick","");							param.put("sign","");
		param.put("need_sign",""); 					param.put("isIgnore","");
		param.put("full_redirect","");					param.put("popid","");
		param.put("guf","");					param.put("not_duplite_str","");
		param.put("need_user_id","");				param.put("gvfdcname","");
		param.put("gvfdcre","");				param.put("from_encoding","");
		param.put("allp","");						param.put("oslanguage","");
		param.put("sr","619*371");						param.put("osVer","");
		param.put("naviVer","ie%7C8"); 		
		
		
		
		param.put("newlogin",newlogin);
		param.put("tid",tid);								param.put("pstrong",pstrong);
		param.put("poy",poy);							param.put("sub",sub);
		param.put("callback",callback);					param.put("umto",umto);	
		if(login.getAuthcode()!=null && login.getAuthcode().length()>0){
			param.put("need_check_code","true");
			param.put("ChuJiYuTag", "ChuJiYuTag");
			param.put("TPL_checkcode",login.getAuthcode());
		}else{
			param.put("need_check_code",need_check_code);	
			param.put("TPL_checkcode","");
		}
		String text = cutil.post("https://login.taobao.com/member/login.jhtml", h,param);
	    //{"state":false,"message":"您输入的密码和账户名不匹配，请重新输入。","data":{"code":3501,"url":"","needrefresh":false}} 淘宝登陆
		//{"state":false,"message":"为了您的账户安全，请输入验证码。","data":{"code":3425,"url":"","needrefresh":false,"ccurl":"https://pin.aliyun.com/get_img?sessionid=8f3302f75418d09fae14f9376cc96559&identity=taobao.login&type=150_40"}}
		//{"state":false,"message":"验证码错误，请重新输入。","data":{"code":1000,"url":"","needrefresh":false,"ccurl":"https://pin.aliyun.com/get_img?sessionid=8f3302f75418d09fae14f9376cc96559&identity=taobao.login&type=150_40"}}
		JSONObject json = new JSONObject(text);
		String message = json.getString("message");
		if(message.equals("")){
			if(text.contains("token")){
				json = new JSONObject(json.getString("data"));
				message = (String)json.get("token");
				redismap.put(token,message);
			}
//			else{
//				errorMsg = "登陆失败,确认账号密码是否正确!";
//			}
		}else{
			if(text.contains("ccurl")){
				RegexPaserUtil rp = new RegexPaserUtil("ccurl\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
				String ccur = rp.getText();
				if(ccur!=null){
					context.setImgUrl(ccur);
				}
			}
			output.setErrorMsg(message);	
		}
	}
	
	private void login_2() throws Exception{
		String tok = (String) redismap.get(token);
		String url = "https://passport.alipay.com/mini_apply_st.js?site=0&callback=vstCallback102&token="+tok;
		CHeader h = new CHeader("https://login.taobao.com/member/login.jhtml");
		String text = cutil.get(url, h);
    	RegexPaserUtil rp = new RegexPaserUtil("\"st\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
    	tok = rp.getText();
		if(tok!=null){
			url = "https://login.taobao.com/member/vst.htm?st="+ tok+"&params=style%3Ddefault%26sub%3D%26TPL_username%3D"+login.getLoginName()+"%26loginsite%3D0%26from_encoding%3D%26not_duplite_str%3D%26guf%3D%26full_redirect%3D%26isIgnore%3D%26need_sign%3D%26sign%3D%26from%3Dtb%26TPL_redirect_url%3D%26css_style%3D%26allp%3D&_ksTS="+new Date().getTime()+"_115&callback=jsonp116";
			text = cutil.get(url, h);
		    rp = new RegexPaserUtil("\"url\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
	    	url = rp.getText();
	    	if(url!=null&&url.length()>10){
	    		System.out.println("最后的地址"+url);
	    		Map<String,String> param  = null;
				if(url.contains("login_unusual.htm?")){
		    		//手机验证
					param = login_21(url);
					if(param.get("phone")!=null){
						output.put("phone",param.get("phone"));
						context.removeImgUrl();
						output.setErrorMsg("手机验证登陆!");
					}
		    	}else{
		    		redismap.put(taobao_index, url);
		    		loginsuccess();
		    		addTask(1); 
		    	}
	    	}
		}
//			else{
//				errorMsg = "授权过程中部分参数获取失败,请重新提交!";
//				return ;
//			}
	}
	private Map<String,String> login_21(String url) throws Exception{
		Map<String,String> param = null;
		String text = cutil.get(url);
		RegexPaserUtil rp = new RegexPaserUtil("AQPop\\(\\{       url:'","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
		String text1 = rp.getText();
		text1 = URLDecoder.decode(text1,"utf-8");
		text = cutil.getURL(text1,new CHeader(url));
		text1 = Jsoup.parse(text).getElementById("J_DurexData").val();
		//System.out.println(text);
		rp = new RegexPaserUtil("optionText\":\\[",",\\{",text1,RegexPaserUtil.TEXTEGEXANDNRT);
		text = rp.getText();
		JSONObject json = new JSONObject(text);
		param = new LinkedHashMap<String,String>();
		rp = new RegexPaserUtil("\"param\":\"","\"",text1,RegexPaserUtil.TEXTEGEXANDNRT);
		param.put("url",rp.getText());
		if(param.get("url")!=null){
			param.put("checkType",json.getString("type"));//发送短信使用参数
			param.put("phone",json.getString("name"));
			param.put("target",json.getString("code"));//发送短信使用参数
			redismap.put(duanxinMap, param);
			Map<String,String> pa = new HashMap<String,String>();
			pa.put("checkType", "um");  
			text = cutil.postURL("http://aq.taobao.com/durex/checkcode?param="+param.get("url").toString(),null, pa);
		}
		//能正常执行到底就行
		return param;
	}
	
	 public void sendPhoneDynamicsCode(ResOutput output){
		 Map<String,String> dmap= (Map<String,String>)redismap.get(duanxinMap);
		 try{
			String url 	= dmap.get("url").toString();
			dmap.put("safePhoneNum", "");    		dmap.put("checkCode","");
			String text = cutil.postURL("http://aq.taobao.com/durex/sendcode?param="+url,null, dmap);
			if(text!=null){
				JSONObject json = new JSONObject(text);
				if(json.getBoolean("isSuccess")){
					output.setStatus(1);
				}
				output.setErrorMsg(json.getString("message"));
			}
		}  catch (Exception e) {
			output.setErrorMsg("短信发送失败!");
		}
		 if(StringUtils.isBlank(output.getErrorMsg())){
			 output.setErrorMsg("发送失败!");
		 }
//		 smap.put(CommonConstant.errorMsg,errorMsg==null?"短信发送失败!":errorMsg);
//		 return smap;
	 }
	// 随机短信登录
	public void checkPhoneDynamicsCode(ResOutput output) {
		try{
			Map<String,String> param = (Map)redismap.get(duanxinMap);
			param.put("safePhoneNum", login.getLoginName());    	
			param.put("checkCode",login.getPhoneCode());
			String url 	= param.get("url").toString();
			String text = cutil.postURL("http://aq.taobao.com/durex/checkcode?param="+url,null,param);
			if(!text.contains("true")){}
			else{
				//第七步
				text = cutil.getURL("http://login.taobao.com/member/login_mid.htm?type=success",null);
				if(text!=null){
					CHeader h = new CHeader("http://login.taobao.com/member/login_mid.htm?type=success");
					text = cutil.getURL("https://login.taobao.com/member/login_by_safe.htm?allp=&sub=false&guf=&c_is_scure=&from=tbTop&type=1&style=default&minipara=&css_style=&tpl_redirect_url=http%3A%2F%2Fwww.taobao.com%2F&popid=&callback=jsonp127&is_ignore=&trust_alipay=&full_redirect=&user_num_id=2120391613&need_sign=%3F_duplite_str%3D&from_encoding=?_duplite_str=&sign=&ll=",h);
					if(text!=null){
						//System.out.println(text);
						redismap.put(taobao_index, "http://i.taobao.com/my_taobao.htm");
						loginsuccess();
						addTask(1); 
//						output.setErrorMsg("验证成功!");
					}
				}
			}
		}  catch (Exception e) {
			output.setErrorMsg("短信口令验证失败!");
		}
		 if(output.getErrorMsg().equals("")){
			 output.setErrorMsg("验证失败!");
		 }
	}
	
	
	
	@Override
	public void startSpider(int type) {
			switch (type) {
			case 1:
				if(iTaobao()){
					context.update();//更新下cookie
					System.out.println("登陆成功");
					//此处login包含的淘宝的登陆账号,这样可以跳到淘宝后,去除cookie 然后采集用户信息并且修改loginName 切记
					ZhiFuBao  fu = 	new ZhiFuBao(login, currentUser);
					fu.setServicePojo(service);
					fu.setContextAndClient(context,cutil,redismap);
					fu.addTask(1);
				}
				saveUser();
				saveAll();//先保存当前
				break;
			default:
				break;
			}
			
	}
	public static void main(String[] args) {
//		TaoBao tb = new TaoBao(new Login("zhaoyulong0626","chaoren06262"),null);
		TaoBao tb = new TaoBao(new Login("18911895611","pangzuomei2008hi"),null);
//		TaoBao tb = new TaoBao(new Login("13323826252","yin521.."),null);
//		tb.index();
//		tb.inputCode(tb.getContext().getImgUrl());
//		ResOutput out = tb.logins();
		if(tb.islogin()){
			tb.iTaobao();	
//			tb.test();
//			tb.context.update();//更新下cookie
			ZhiFuBao  fu = 	new ZhiFuBao(tb.login, tb.currentUser);
			fu.setContextAndClient(tb.context,tb.cutil,tb.redismap);
			fu.isHasYueEbao();
			
		}else{
//			System.out.println(out.getErrorMsg());
		}
//		tb.getAlipay();
	}
	public void test(){
		String text = cutil.get(orderListURL);
		System.out.println(text);
		
	}
	
	/**是否**/
	public  boolean iTaobao(){
		boolean b = false;
		String url = (String) redismap.get(taobao_index);
		if(url==null||"".equals(url)){
			url = "http://i.taobao.com/my_taobao.htm";
		}
		String text = cutil.get(url);
		if(text!=null){
	    	String	str = new RegexPaserUtil("http://lab.alipay.com/user/i.htm\\?src=","\"",text,RegexPaserUtil.TEXTEGEXANDNRT).getText();
	    	if(str!=null){
	    		str = str.replace("http","https");
	    		str = "?src="+str;
	    		//System.out.println("下一步执行地址");
	    		redismap.put(ZhiFuBao.alipayIndexParam, str);
	    		b = true;
	    	}
		}
		return b;
	}
	
	/**
	 *  抓取会员基本信息
	 */
	public User gatherUser()  {
		User user = null;
		String content =  cutil.get(userInfoURL);
		if(content!=null){
			Document doc = Jsoup.parse(content);
			
			//昵称
			String petName = "";
			Element petNameElement = doc.select("input[id=J_uniqueName]").first();
			if(petNameElement!=null){
				petName = petNameElement.attr("value").toString();
			}
			
			//真实名字
			String realName = "";
			Element realNameElement = doc.select("input[id=J_realname]").first();
			if(realNameElement!=null){
				realName = realNameElement.attr("value").toString();
			}
			
			//性别
			String sex = "男";
			Element genderElement = doc.select("input[id=J_gender2]").first();
			if(genderElement!=null){
				String checked = genderElement.attr("checked").toString();
				if(checked!=null && !checked.trim().equals("")){
					sex = "女";
				}
			}
			
			//生日
			String year = "1900";
			Element yearElement = doc.select("select[id=J_Year]").select("option[selected=selected]").first();
			if(yearElement!=null){
				 if(yearElement.text().trim().length()>1){
					 year = yearElement.text();
					}
			}
			String month = "01";
			Element monthElement = doc.select("select[id=J_Month]").select("option[selected=selected]").first();
			if(monthElement!=null){
				if(monthElement.text().trim().length()>1){
					month = monthElement.text();
				}
			}
			String day = "01";
			Element dayElement = doc.select("select[id=J_Date]").select("option[selected=selected]").first();
			if(dayElement!=null){
				if(dayElement.text().trim().length()>1){
					day = dayElement.text();
				}
				
				
			}
			
			
			String birthday = year+"年"+month+"月"+day+"日";
			DateFormat format = new SimpleDateFormat("yyyy年MM月DD日");         
			Date date = null;    
			try {    
			   date = format.parse(birthday);  // Thu Jan 18 00:00:00 CST 2007    
			} catch (ParseException e) {    
			   log4j.warn("时间解析错误#", e);
			} 
			
			//居住地
			String redstar = "";
			if(doc.select("input[id=divisionCode]")!=null && doc.select("input[id=divisionCode]").size()>0){
				String divisionCodeElement = doc.select("input[id=divisionCode]").first().attr("value");		
				if(!divisionCodeElement.trim().equals("")){
					divisionCodeElement = divisionCodeElement.trim();		
					String area= doc.select("select[id=J_redstar_area]").get(0).select("option[value="+divisionCodeElement+"]").text();
					String cityElement = divisionCodeElement.substring(0, 3)+"00";
					String city =  doc.select("select[id=J_redstar_city]").select("option[value="+cityElement+"]").text();
					String provinceElement = divisionCodeElement.substring(0, 2)+"000";
					String province =  doc.select("select[id=J_redstar_province]").select("option[value="+provinceElement+"]").text();
					redstar = province+" "+city+" "+area;
				}
			}
		
			
			//家乡
			String live = "";
			if(doc.select("input[id=liveDivisionCode]")!=null && doc.select("input[id=liveDivisionCode]").size()>0){
				String liveDivisionCodeElement = doc.select("input[id=liveDivisionCode]").first().attr("value");
				if(!liveDivisionCodeElement.trim().equals("")){
					liveDivisionCodeElement = liveDivisionCodeElement.trim();		
					String area = doc.select("select[id=J_live_area]").select("option[value="+liveDivisionCodeElement+"]").text();
					String cityElement = liveDivisionCodeElement.substring(0, 3)+"00";
					String city =  doc.select("select[id=J_live_city]").select("option[value="+cityElement+"]").text();
					String provinceElement = liveDivisionCodeElement.substring(0, 2)+"000";
					String province =  doc.select("select[id=J_live_province]").select("option[value="+provinceElement+"]").text();
					live = province+" "+city+" "+area;
				}			
			}
		
			user = new User();
			user.setLoginName(login.getLoginName());
			user.setPetName(petName);
			user.setRealName(realName);
			user.setUsersource(Constant.TAOBAO);
			user.setBirthday(date);
			user.setRedstar(redstar);
			user.setLive(live);
			user.setSex(sex);
			user.setModifyDate(new Date());
			user.setParentId(currentUser);

		}
	
		return user;
	}
	
	
	
	@Override
	public void gatherHistoryLoseURL() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gatherOrder() {
		String text = null;
		try{
			text = getHtml(orderListURL, 1);
			gatherUserOrderSaveAndParse(text);
			int page = 1;
			if(!isStop){
				if(text!=null){
					page = getPage(text);
					for (int i = 2; i <= page; i++) {
						gatherUserOrderSaveAndParse(getHtml(orderListURL, i));
						if(isStop){
							break;
						}
					}
				}
			}
		}catch(Exception e){
			log4j.writeLogOrder(text, e, ORDER);
			spiderOrderState = false;
		}
	}
	private String getHtml(String url,int page){
		String text = cutil.get(url+page);
		if(text==null){
			spiderOrderState = false;
		}
		return text;
	}
	public  void gatherUserOrderSaveAndParse(String content){
		if(content!=null){
			if (content.contains("item active")) {
				Document doc = Jsoup.parse(content);
				Elements tbodyElements = doc.select("table[id=J_BoughtTable]")
						.first().select("tbody");
				for (int i = 0; i < tbodyElements.size(); i++) {
					try{
						Order order = new Order();
						Element element = tbodyElements.get(i);
						// 订单编号
						String orderId = element.attr("data-orderid");
						// 订单成交时间
						String orderTimeStr = element.select("tr[class=order-hd]")
								.first().select("span[class=dealtime]").first().text()
								.replace("成交时间：", "");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date orderTime = null;
						try {
							orderTime = sdf.parse(orderTimeStr);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log4j.warn("order时间转换错误#", e1);
						}
						//System.out.println(orderId);
						// 商家名称
						String merchant = "";
						if (element.select("a[class=shopname J_MakePoint]") != null
								&& element.select("a[class=shopname J_MakePoint]")
										.size() > 0) {
							merchant = element.select("a[class=shopname J_MakePoint]")
									.first().text();
						}
						order.setProductNames(merchant);
						Elements productElements = element.select("tr");
						// 总价
						BigDecimal allprice = new BigDecimal("0");
						
						if(element
								.select("em[class=real-price special-num]")!=null && element
										.select("em[class=real-price special-num]").size()>0){
							allprice = new BigDecimal(element
									.select("em[class=real-price special-num]").first()
									.text());
						}else if(element.select("strong[class=special-num]")!=null && element
						.select("strong[class=special-num]").size()>0){
							allprice = new BigDecimal(element.select("strong[class=special-num]").first().text());
						}
						// 订单状态
						String status = "已完成";
						if(element.select("a[class=J_MakePoint status]")!=null && element.select("a[class=J_MakePoint status]").size()>0){
							status = element.select("a[class=J_MakePoint status]")
							.first().text().trim();
						}
						if (status.equals("交易成功")) {
							status = "已完成";
						}
						order.setOrderstatus(status);
						
						// 订单详情链接
						String orderDetailHref = element
								.select("a[class=detail-link J_MakePoint]").first()
								.attr("href"); 
						 order.setId( UUID.randomUUID().toString());
						 order.setOrderId(orderId);
						 //order.setProductNames(productName);
						 order.setMoney(allprice);
						 order.setBuyTime(orderTime);
						 order.setOrderstatus(status);
						 order.setOrdersource(Constant.TAOBAO);
						 order.setLoginName(loginName);
						 OrderItem orderItem = new OrderItem();
						 orderItem.setMerchant(order.getProductNames());
						 orderItem.setOrderTId(orderId);
						 int num = isContinue(order);
							if(num==1){
								continue;
							}else if(num==2){
								isStop = true;
								break;
							}
							gatherURLSaveAndParse(cutil.get(orderDetailHref), order,orderItem,productElements);
							if(num==3){
								//update
								listUpdateOrder.add(order);
							}else{
								 listOrder.add(order);
								 itemList.add(orderItem);
							}
					 }catch(Exception e){
						 spiderOrderState = false;
						 log4j.writeLogOrder(content, e, ORDER);
					 }
				}
			}else{
				isStop = true;
			}
		}
	}
	/**解析账单详情 ,已完成订单
	 * @param html
	 * @param url
	 * @param order
	 * @param count 统计该地址被执行几次
	 * @throws Exception 
	 */
	public void gatherURLSaveAndParse(String orderDetail, Order order,OrderItem orderItem,Elements productElements) throws Exception{
		if(orderDetail!=null){
			Document docDetail = Jsoup.parse(orderDetail);
			String detail = "";
			if (docDetail.select("table[class=simple-list logistics-list]")
					 != null
					&& docDetail
							.select("table[class=simple-list logistics-list]")
							.size() > 0) {
				detail = docDetail
						.select("table[class=simple-list logistics-list]")
						.first().select("tr").get(2).select("td").get(1)
						.text();
			}

			if (detail.equals("")) {
				if (docDetail.select("div[class=addr_and_note]") != null
						&& docDetail.select("div[class=addr_and_note]")
								.size() > 0) {
					detail = docDetail.select("div[class=addr_and_note]")
							.first().select("dd").first().text();
				}
			}
			
			if (detail.equals("")) {
				if (docDetail.select("div[class=address-detail]") != null
						&& docDetail.select("div[class=address-detail]")
								.size() > 0) {
					detail = docDetail.select("div[class=address-detail]")
							.first().text();
				}
			}
			String[] attrs = detail.replace(" ", "").split("，");
			String receiver = "";
			String recevierTelephone = "";
			String recevierFixPhone = "";
			String recevierAddr = "";
			String recevierPost = "";
			String buyway = "在线支付";
			if (attrs.length > 4) {
				receiver = attrs[0];
				recevierTelephone = attrs[1];
				recevierFixPhone = attrs[2];
				recevierAddr = attrs[3];
				recevierPost = attrs[4];

			}

			 order.setReceiver(receiver);
			 order.setBuyway(buyway);
			 order.setRecevierAddr(recevierAddr);
			 //order.setRecevierEmail(recevierEmail);
			 order.setRecevierFixPhone(recevierFixPhone);
			 order.setRecevierTelephone(recevierTelephone);
			 order.setRecevierPost(recevierPost);
			

			if (productElements.size() > 2) {
				for (int j = 2; j < productElements.size(); j++) {
					Element product = productElements.get(j);
					if(product
							.select("a[class=J_MakePoint]")!=null && product
									.select("a[class=J_MakePoint]").size()>0){
						String productName = product
								.select("a[class=J_MakePoint]").first().text();

						String productNameHref = product
								.select("a[class=J_MakePoint]").first()
								.attr("href");
						String productId = "";
						String[] trades = productNameHref.split("item_id_num=");
						if (trades.length > 1) {
							productId = trades[1];
						}
						// 价格
						BigDecimal productPrice = new BigDecimal(product
								.select("i[class=special-num]").first().text());
						// 数量
						int num = Integer.parseInt(product
								.select("td[class=quantity]").first()
								.attr("title"));
						 orderItem.setId(UUID.randomUUID().toString());
						 orderItem.setOrderTId(orderItem.getOrderTId());
						 orderItem.setProductId(productId);
						 orderItem.setPrice(productPrice);
						 //orderItem.setProductType(productType);
						 orderItem.setNum(num);
						 
						 orderItem.setProductName(productName);
						 
						 }
					}
				}
		}else{
			throw new Exception("请求结果为空!");
		}
	}
	
	
	private Integer getPage(String html) throws Exception{
		int page = -1;
		try{
			String pageNum = new RegexPaserUtil("class=\"total\">共", "页",html,RegexPaserUtil.TEXTEGEXANDNRT).getText();
			if(pageNum!=null){
				page = Integer.parseInt(pageNum.trim());
				if(page>350){
					page=350;
				}
			}
		}catch(Exception e){
			log4j.writeLogOrder(html, new Exception("The query is less than the total page"), ORDER);
			throw new Exception(e);
		}
		return page;
	}

}

///*
// * 保存订单信息
// */
//public void saveOrder() {
//	String urls = orderListURL + 1;
//	String content1 = cutil.get(urls);
//	Document doc = Jsoup.parse(content1);
//	Elements eles = doc.select("li[class=item active]");
//
//	if (eles != null && eles.size() > 0) {
//		List<String> objectTmp = new ArrayList<String>();
//		int tempsize = 5;
//		int time = 1;
//		while (true) {
//			String url = orderListURL + time * tempsize;
//			String content =  cutil.get(url);
//			if (content.contains("item active")) {
//				for (int i = 1; i <= 5; i++) {
//					int cpage = (time - 1) * 5 + i;
//					objectTmp.add(orderListURL + cpage);
//				}
//				time++;
//			} else {
//				for (int i = 1; i <= 4; i++) {
//					int cpage = (time - 1) * 5 + i;
//					objectTmp.add(orderListURL + cpage);
//				}
//				break;
//			}
//		}
//		for (int i = 0; i < objectTmp.size(); i++) {
//			anlyzerOrder(objectTmp.get(i),service.getOrderService(),
//					 service.getOrderItemService(),currentUser,login.getLoginName());
//		}
//	}
//}


///**
// * 解析订单
// * */
//public void anlyzerOrder(String url,IOrderService orderService,
//		IOrderItemService orderItemService,String currentUser,String loginName) {
//	String content = "";
//
//	content = cutil.get(url);
//	if (content.contains("item active")) {
//		Document doc = Jsoup.parse(content);
//		Elements tbodyElements = doc.select("table[id=J_BoughtTable]")
//				.first().select("tbody");
//		for (int i = 0; i < tbodyElements.size(); i++) {
//			Element element = tbodyElements.get(i);
//			// 订单编号
//			String orderId = element.attr("data-orderid");
//			// 订单成交时间
//			String orderTimeStr = element.select("tr[class=order-hd]")
//					.first().select("span[class=dealtime]").first().text()
//					.replace("成交时间：", "");
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date orderTime = null;
//			try {
//				orderTime = sdf.parse(orderTimeStr);
//			} catch (ParseException e1) {
//				e1.printStackTrace();
//				log4j.warn("order时间转换错误#", e1);
//			}
//			//System.out.println(orderId);
//			// 商家名称
//			String merchant = "";
//			if (element.select("a[class=shopname J_MakePoint]") != null
//					&& element.select("a[class=shopname J_MakePoint]")
//							.size() > 0) {
//				merchant = element.select("a[class=shopname J_MakePoint]")
//						.first().text();
//			}
//			Elements productElements = element.select("tr");
//			// 总价
//			BigDecimal allprice = new BigDecimal("0");
//			
//			if(element
//					.select("em[class=real-price special-num]")!=null && element
//							.select("em[class=real-price special-num]").size()>0){
//				allprice = new BigDecimal(element
//						.select("em[class=real-price special-num]").first()
//						.text());
//			}else if(element.select("strong[class=special-num]")!=null && element
//			.select("strong[class=special-num]").size()>0){
//				allprice = new BigDecimal(element.select("strong[class=special-num]").first().text());
//			}
//					
//			// 订单状态
//			String status = "已完成";
//			if(element.select("a[class=J_MakePoint status]")!=null && element.select("a[class=J_MakePoint status]").size()>0){
//				status = element.select("a[class=J_MakePoint status]")
//				.first().text().trim();
//			}
//			if (status.equals("交易成功")) {
//				status = "已完成";
//			}
//			
//			// 订单详情链接
//			String orderDetailHref = element
//					.select("a[class=detail-link J_MakePoint]").first()
//					.attr("href"); 
//			String orderDetail =   cutil.get(orderDetailHref);
//
//			Document docDetail = Jsoup.parse(orderDetail);
////			System.out.println(orderDetailHref
////					+ "**************************");
//			String detail = "";
//			if (docDetail.select("table[class=simple-list logistics-list]")
//					 != null
//					&& docDetail
//							.select("table[class=simple-list logistics-list]")
//							.size() > 0) {
//				detail = docDetail
//						.select("table[class=simple-list logistics-list]")
//						.first().select("tr").get(2).select("td").get(1)
//						.text();
//			}
//
//			if (detail.equals("")) {
//				if (docDetail.select("div[class=addr_and_note]") != null
//						&& docDetail.select("div[class=addr_and_note]")
//								.size() > 0) {
//					detail = docDetail.select("div[class=addr_and_note]")
//							.first().select("dd").first().text();
//				}
//			}
//			
//			if (detail.equals("")) {
//				if (docDetail.select("div[class=address-detail]") != null
//						&& docDetail.select("div[class=address-detail]")
//								.size() > 0) {
//					detail = docDetail.select("div[class=address-detail]")
//							.first().text();
//				}
//			}
//			String[] attrs = detail.replace(" ", "").split("，");
//			String receiver = "";
//			String recevierTelephone = "";
//			String recevierFixPhone = "";
//			String recevierAddr = "";
//			String recevierPost = "";
//			String buyway = "在线支付";
//			if (attrs.length > 4) {
//				receiver = attrs[0];
//				recevierTelephone = attrs[1];
//				recevierFixPhone = attrs[2];
//				recevierAddr = attrs[3];
//				recevierPost = attrs[4];
//
//			}
//
//			 Map taomap = new HashMap(2);
//			 taomap.put("orderId", orderId);
//			 taomap.put("ordersource", Constant.TAOBAO);
//			 List<Order> list1 =
//			 orderService.getOrderByOrderIdsource(taomap);
//			 if(list1!=null && list1.size()>0){
//			
//			 }else{
//			 Order order = new Order();
//			 UUID uuid = UUID.randomUUID();
//			 order.setId(uuid.toString());
//			 order.setOrderId(orderId);
//			 //order.setProductNames(productName);
//			 order.setReceiver(receiver);
//			 order.setMoney(allprice);
//			 order.setBuyway(buyway);
//			 order.setBuyTime(orderTime);
//			
//			 order.setOrderstatus(status);
//			 order.setOrdersource(Constant.TAOBAO);
//			 order.setLoginName(loginName);
//			 order.setRecevierAddr(recevierAddr);
//			 //order.setRecevierEmail(recevierEmail);
//			 order.setRecevierFixPhone(recevierFixPhone);
//			 order.setRecevierTelephone(recevierTelephone);
//			 order.setRecevierPost(recevierPost);
//			
//			 orderService.saveOrder(order);
//			
//			
//
//			if (productElements.size() > 2) {
//				for (int j = 2; j < productElements.size(); j++) {
//					Element product = productElements.get(j);
//					if(product
//							.select("a[class=J_MakePoint]")!=null && product
//									.select("a[class=J_MakePoint]").size()>0){
//						String productName = product
//								.select("a[class=J_MakePoint]").first().text();
//
//						String productNameHref = product
//								.select("a[class=J_MakePoint]").first()
//								.attr("href");
//						String productId = "";
//						String[] trades = productNameHref.split("id=");
//						if (trades.length > 1) {
//							productId = trades[1];
//						}
//						// 价格
//						BigDecimal productPrice = new BigDecimal(product
//								.select("i[class=special-num]").first().text());
//						// 数量
//						int num = Integer.parseInt(product
//								.select("td[class=quantity]").first()
//								.attr("title"));
//						 OrderItem orderItem = new OrderItem();
//						 UUID uuid2 = UUID.randomUUID();
//						 orderItem.setId(uuid2.toString());
//						 orderItem.setOrderTId(uuid.toString());
//						 orderItem.setProductId(productId);
//						 orderItem.setPrice(productPrice);
//						 //orderItem.setProductType(productType);
//						 orderItem.setNum(num);
//						 orderItem.setMerchant(merchant);
//						 orderItem.setProductName(productName);
//						 orderItemService.saveOrderItem(orderItem);
//						 }
//					}
//					}
//					
//
//			}
//		}
//	}
//
//}



//public  int getAlipay(){
//int i = 0;
//try{
//	String key = "taobao_isAlipay_login";
//	Object obj = redismap.get(key);
//	String text = null;
//	CHeader h = new CHeader("http://i.taobao.com/my_taobao.htm");
//	//为防止登陆不上去
//	for (int j = 0; j < 3; j++) {
//		if(obj==null){
//			String url = "https://lab.alipay.com/user/i.htm";
//			String param = (String) redismap.get(alipay_index_param);
//			param = param.replace("&amp;", "&");
//			text = cutil.get(url+param,new CHeader("http://i.taobao.com/my_taobao.htm"));
//			url = alipay_url_1+param+"&goto=https://lab.alipay.com/user/i.htm";
//			text = cutil.get(url);
//			cutil.setHandleRedirect(false);
//			text = cutil.get("https://login.taobao.com:443/member/login.jhtml?tpl_redirect_url=https://auth.alipay.com:443/login/trustLoginResultDispatch.htm?redirectType=&sign_from=3000&goto=https://lab.alipay.com/user/i.htm?src=yy_content_jygl&from_alipay=1",new CHeader(url));
//			try{
////				System.out.println(text);
//			  text = cutil.get(URLDecoder.decode(text,"utf-8")+"&goto=https://lab.alipay.com/user/i.htm?src=yy_content_jygl&sign_from=3000",new CHeader(url));
//			  if(!text.contains("https://auth.alipay.com:443/error.htm?exceptionCode=")){
//				  redismap.put(key, "1");
//				  break;
//			  }
//			  //System.out.println(text);
//			}catch(Exception e1){
//			}
//		}else{
//			break;
//		}
//	}
//	
//	h = new CHeader("https://auth.alipay.com/login/certCheck.htm");
//		cutil.setHandleRedirect(true);
//	text = cutil.get(alipay_url_3,h);
//	//System.out.println(text+"--");
//	Document doc = Jsoup.parse(text);
//	String alipayName = doc.select("a[id=J-userInfo-account-userEmail]").first().attr("title");
//	redismap.put(alipyName, alipayName);
//	System.out.println("---------------------------支付宝打开页面内容-----------------------------");
//	  i = 1;
//	//System.out.println("支付宝页面内容:"+text);
//	if(!text.contains("app-yuebao-manage-myalipay-v1")){
//		System.out.println("--------该用户未开通余额宝---------");
//	}else{
//	   i =2;
//	}
//		
//}catch(Exception e){
//	e.printStackTrace();
//	log.warn(login.getLoginName()+"支付宝解析异常#",e);
//}
//
//return i;
//}


//public static String yuebao_url_1 = "https://financeprod.alipay.com/fund/index.htm";
//public static String alipyName = "taobao_alipyName";
///**打开余额宝*/
//public  boolean getYuEBao(){
//	boolean b = false;
//	String text = cutil.get(yuebao_url_1);
//	try {
//		if(text.contains("进入我的余额宝")){
//			System.out.println("--------该用户未开通余额宝-------");
//		}else{
////			parseBegin(Constant.YUEBAO);
//			openShouyi();
////			parseEnd(Constant.YUEBAO);
//		}
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	return b;
//}
///**余额宝收益信息
// * @throws Exception */
//public void openShouyi() throws Exception{
//	String url = "https://financeprod.alipay.com/fund/asset.htm";
//	String token = openGet(url);
//		Map<String,String> smap = new HashMap<String, String>();
//		smap.put("_form_token", token);
//		smap.put("direction", "income");
//		smap.put("startDate", "2013-05-01");
//		smap.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
//		Integer page = 1;
//		page = openPost(url, page,  smap);
//		int result = 0 ;
//		for (int i = 2; i <= page; i++) {
//			result = openPost(url, i,smap);
//			if(result==-1){
//				System.out.println("程序出错以后捕捉");
//				break;
//			}
//	}
//}
///**模拟浏览器打开收益页用于获取token值
// * @throws Exception */
//public String openGet(String url) throws Exception{
//	RegexPaserUtil rp = null;
//	try{
//		CHeader h = new CHeader("https://financeprod.alipay.com/fund/index.htm");
//		String text = cutil.get(url, h);
//		System.out.println("打开余额宝页面内容:"+text);
//		rp = new RegexPaserUtil("\"J-calendar-form\">","id=\"J_submit_time",text,RegexPaserUtil.TEXTEGEXANDNRT);
//		String str = rp.getText();
//		rp = new RegexPaserUtil("_form_token\" value=\"","\"",str,RegexPaserUtil.TEXTEGEXANDNRT);
//		return rp.getText();
//	}catch(Exception e){
//		throw new Exception();
//	}
//}
///**余额宝收益信息*/
//public Integer openPost(String url,Integer page,Map<String,String> smap){
//	try{
//		String alipayName = redismap.get(alipyName).toString();
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
//		YuEBaoUtil yuEBaoUtil = new YuEBaoUtil();
//		if(text!=null && text.trim().length()>10){				
//			yuEBaoUtil.listLog( text, service.getYuebaoService(), alipayName);
//		}
//
//		RegexPaserUtil	rp = new RegexPaserUtil("\"J-calendar-form\">","id=\"J_submit_time",text,RegexPaserUtil.TEXTEGEXANDNRT);
//		String str = rp.getText();
//		rp = new RegexPaserUtil("_form_token\" value=\"","\"",str,RegexPaserUtil.TEXTEGEXANDNRT);
//		str = rp.getText();
//		smap.put("_form_token", str);
//		if(page==1){
//			if(text!=null && text.trim().length()>10){		
//				yuEBaoUtil.saveLog(text, service.getUserService(), service.getYuebaoService(), alipayName);				
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
///**余额宝转出信息,  没调用
// * @throws Exception */
//private void openZhuanchu() throws Exception{
//	String url = "https://financeprod.alipay.com/fund/asset.htm";
//	String token = openGet(url+"?direction=in");
//		Map<String,String> smap = new HashMap<String, String>();
//		smap.put("_form_token", token);
//		smap.put("direction", "in");
//		smap.put("startDate", "2013-05-01");
//		smap.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
//		Integer page = 1;
//		page = openPost(url, page,smap);
//		System.out.println("--------余额宝转出信息------------");
//		int result = 0 ;
//		for (int i = 2; i <= page; i++) {
//			result = openPost(url, i, smap);
//			System.out.println("--------余额宝转出信息------------");
//			if(result==-1){
//				System.out.println("程序出错以后捕捉");
//				break;
//			}
//	}
//}
///**余额宝转出信息 没调用
// * @throws Exception */
//private  void openZhuanru() throws Exception{
//	String url = "https://financeprod.alipay.com/fund/asset.htm";
//	boolean b = true;
//	String token = openGet(url+"?direction=out");
//	Map<String,String> smap = new HashMap<String, String>();
//	smap.put("_form_token", token);
//	smap.put("direction", "out");
//	smap.put("startDate", "2013-05-01");
//	smap.put("endDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
//	Integer page = 1;
//	page = openPost(url, page,smap);
//	System.out.println("--------余额宝转出信息------------");
//	int result = 0 ;
//	for (int i = 2; i <= page; i++) {
//		result = openPost(url, i, smap);
//		System.out.println("--------余额宝转出信息------------");
//		if(result==-1){
//			System.out.println("程序出错以后捕捉");
//			break;
//		}
//	}
//}
//@Override
//public void startSpider(int type) {
//		switch (type) {
//		case 1:
//			boolean b = iTaobao();
////			close();
//			if(b){
//				TaoBao tb = new TaoBao(login, currentUser);
////				tb.setPayInfoService(payInfoService).setOrderItemService(orderItemService).setYuebaoService(yuebaoService);
////				tb.setUserService(userService).setWarningService(warningService).setParseService(parseService);
////				tb.getLogin().setType(2);//单独线程调用支付宝
////				addTask(tb);
//			}
//			startSpiderTaobao();
//			break;
//		case 2:
//			int i = getAlipay();
//			if(i>0){
////				close();
//				if(i>1){
////					TaoBao tb = new TaoBao(login, currentUser);
////					tb.setOrderItemService(orderItemService).setYuebaoService(yuebaoService).setParseService(parseService);
////					tb.setPayInfoService(payInfoService).setUserService(userService).setWarningService(warningService);
////					tb.getLogin().setType(4);//单独线程调用支付宝
////					addTask(tb);
//				}
//				
////				startSpiderZhifubao();
//			}
//			break;
//		case 3:
//			break;
//		case 4:
//			getYuEBao();
//			break;
//
//		default:
//			break;
//		}
//		
//}
//public static String alipay_url_1 = "https://auth.alipay.com/login/trust_login.do";
//public static String alipay_url_2 = "https://login.taobao.com/member/login.jhtml?tpl_redirect_url=https%3A%2F%2Fauth.alipay.com%3A443%2Flogin%2FtrustLoginResultDispatch.htm%3FredirectType%3D%26sign_from%3D3000%26goto%3Dhttps%253A%252F%252Flab.alipay.com%252Fuser%252Fi.htm%253Fsrc%253Dyy_content_jygl&from_alipay=1";
//public static String alipay_url_3 = "https://my.alipay.com/portal/i.htm?src=yy_content_jygl";
//public void startSpiderZhifubao(){
////parseBegin(Constant.ZHIFUBAO);
//openInfoPage();
//openAlipayJiaoyi();
//openAlipayJiaoyiYear();
////parseEnd(Constant.ZHIFUBAO);
//
//}
///**支付宝的个人信息*/
//public void openInfoPage(){
//CHeader h = new CHeader("https://auth.alipay.com/login/loginResultDispatch.htm");
//String text = cutil.get("https://my.alipay.com/portal/account/index.htm", h);
//if(text!=null){
//	new Alipay().saveUserInfo(text, service.getUserService(), login.getLoginName(), currentUser);	
//}
//}
///**交易记录之一年内*/
//public boolean openAlipayJiaoyi(){
///**支付宝交易记录一年内**/
//StringBuffer sburl = new StringBuffer();
//sburl.append("https://consumeprod.alipay.com/record/standard.htm?_input_charset=utf-8&dateRange=oneYear&tradeType=all&status=all&fundFlow=all&beginTime=00%3A00&dateType=createDate&endTime=24%3A00");
//sburl.append("&beginDate=").append(TimeUtil.getStartTime());
//sburl.append("&endDate=").append(TimeUtil.getEndTime());
//sburl.append("&pageNum=");
//boolean b = true;
//Integer page = 1;
//String url = openAlipayJiaoyiInfo(sburl.toString(),page);
//try{
//	if(url.contains("pageNum=")){
//		page = Integer.parseInt(url.split("pageNum=")[1]);
//	}else{
//		page = Integer.parseInt(url.substring(url.length()-1));
//	}
//	
//}catch(Exception e ){
//	//System.out.println("解析url错误");
//	return false;
//}
//for (int i = 2; i <= page; i++) {
//	System.out.println("-----------当前第"+i+"页-------------");
//	try {
//		Thread.sleep((long) (100*Math.random()));
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	openAlipayJiaoyiInfo(sburl.toString(),i);
//}
//return b;
//}
///**交易记录*/
//public String openAlipayJiaoyiInfo(String url,Integer page){
//CHeader h = new CHeader("https://auth.alipay.com/login/loginResultDispatch.htm");
//String text = cutil.get(url+page, h);
//System.out.println("---------------------------支付宝交易信息页-----------------------------");
//String str = null;
//if(text!=null){
//	Alipay alipay = new Alipay();
//	String alipayName = redismap.get(alipyName).toString();
//	try {
//		alipay.saveUserOrderByHtmlparser( text, service.getPayInfoService(),  currentUser,alipayName);
//	} catch (InterruptedException e) {
//		e.printStackTrace();
//	}
//	if(page.intValue()==1){
//		RegexPaserUtil	rp = new RegexPaserUtil("<a class=\"page-end\" href=\"","\">尾页",text,RegexPaserUtil.zel_all_chars);
//    	str = rp.getText();
//    	System.out.println("总页数:"+str);
//	}
//}
//return str;
//}
///**交易记录之一年之前*/
//public boolean openAlipayJiaoyiYear(){
///**支付宝交易记录一年之前**/
//String url = "https://lab.alipay.com/consume/record/historyIndexNew.htm";
//boolean b = true;
//String page = "1";
//String criticalDate = TimeUtil.getCriticalDate();
//String endTime = TimeUtil.getOneYearBeforeTime();
//boolean flag = true;
//int i = 0;
//Map<Integer,String> smap = openAlipayJiaoyiInfoYearGet(url);
//while(flag){
//	smap = openAlipayJiaoyiInfoYearPost(url,page,"2005-01-01",criticalDate,endTime,smap);
//	if(smap!=null&&smap.get(1)!=null){
//		page = smap.get(1).toString();
//	}else{
//		flag = false;
//	}
//	i++;
//	//防止死循环
//	if(i>100){
//		flag = false;
//	}
//}
//
//return b;
//}
///**支付宝一年之前交易信息页*/
//public Map<Integer,String> openAlipayJiaoyiInfoYearGet(String url){
//String text = cutil.get(url,new CHeader("https://lab.alipay.com/consume/record/historyIndexNew.htm"));
//System.out.println(text);
//RegexPaserUtil rp = new RegexPaserUtil("<input type=\"hidden\" name=\"_form_token\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//Map<Integer,String> smap  = new HashMap<Integer,String>();
//smap.put(2, rp.getText());
//return smap;
//}
///**交易记录一年之前*/
//public Map<Integer,String> openAlipayJiaoyiInfoYearPost(String url,String page,String beginTime,String criticalDate,String endTime,Map<Integer,String> smap){
//if(smap.get(2)==null){
//	return null;
//}
//CHeader h = new CHeader("https://lab.alipay.com/consume/record/historyIndexNew.htm");
//smap.put(1,null);
//Map<String,String> param = new HashMap<String,String>();
//param.put("_form_token",smap.get(2).toString());    		
//param.put("customerType","2");
//param.put("keyword","0_");	
//param.put("beginTime",beginTime);	
//param.put("endTime",endTime);	
//param.put("criticalDate",criticalDate);	
//param.put("currentPageNo",page);	
//
//
//String text = cutil.post(url, h,param);
//System.out.println("---------------------------支付宝一年之前交易信息页-----------------------------");
//String alipayName = redismap.get(alipyName).toString();
//Alipay alipay = new Alipay();
//alipay.anLyzerAlipayOneYear(text,service.getPayInfoService(),  currentUser,alipayName);
//	
//RegexPaserUtil	rp = new RegexPaserUtil("<a class=\"page-next form-element\" href=\"#\" rel=\"currentPageNo\" rel-value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//String str = rp.getText();
//System.out.println("下一页:"+str);
//rp = new RegexPaserUtil("<input type=\"hidden\" name=\"_form_token\" value=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//smap.put(1, str);
//smap.put(2, rp.getText());
//	
//return smap;
//}