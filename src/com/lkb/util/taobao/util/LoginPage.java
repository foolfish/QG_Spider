//package com.lkb.util.taobao.util;
//
//import java.io.BufferedReader;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONObject;
//import org.springframework.data.redis.support.collections.RedisMap;
//
//import com.lkb.bean.client.Login;
//import com.lkb.constant.TaoBaoConstant;
//import com.lkb.service.IOrderItemService;
//import com.lkb.service.IOrderService;
//import com.lkb.service.IUserService;
//import com.lkb.thirdUtil.TaoBao;
//import com.lkb.util.RegexPaserUtil;
//import com.lkb.util.httpclient.CHeaderUtil;
//import com.lkb.util.httpclient.ParseResponse;
//import com.lkb.util.httpclient.entity.CHeader;
//import com.lkb.util.httpclient.entity.Result;
//import com.lkb.util.redis.Redis;
//import com.lkb.util.redis.RedisClient;
//
//
//
//
//public class LoginPage {
//	
//	/**登陆参数**/
////	public static String TPL_usernames = "wangxuefei0310";
////	public static String TPL_passwords = "wxf51521";
//	public static String TPL_usernames = "zhaoyulong0626";
//	public static String TPL_passwords = "chaoren06262";
////	public static String TPL_usernames = URLEncoder.encode("wy0203168"); //异地账号
////	public static String TPL_passwords = URLEncoder.encode("chaoren0626");
////	public static String TPL_usernames = URLEncoder.encode("chaoren062611");
////	public static String TPL_passwords = URLEncoder.encode("test001");
//	/**验证码*/
//	public static String TPL_checkcode = "";
////	public static String need_check_code = "yes";//跟上边对应
//	public static String need_check_code = "";//跟上边对应
////	public static String newlogin = "0";//跟上边对应
//	public static String newlogin = "1";//跟上边对应
//	public static String tid = "XOR_1_000000000000000000000000000000_63504554470A7C717375720C";
//	public static String pstrong = "2";
//	public static String poy = "XOR_1_000000000000000000000000000000_625A424A45137C6F7A7F047A62";//
////	public static String sub = "false";
//	public static String sub = "";
//	public static String callback="1";
//	public static String umto="NaN";
//	
//	
//	
//	public static void main(String[] args) {
//	}
//	/**第一步*/
//	public static boolean index(String url,Map map){
//		boolean b = true;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null);
//		HttpGet get= CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//	    try {
//	    	response = RedisClient.getClient(map).execute(get);
//	    	String text = ParseResponse.parse(response);
//	    	System.out.println("---------------------------第一步-----------------------------");
//	    	System.out.println(text);
//	    	RegexPaserUtil rp = new RegexPaserUtil("id=\"J_StandardCode_m\" src=\"https://s.tbcdn.cn/apps/login/static/img/blank.gif\" data-src=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			String str = rp.getText();
//			str = str.replace("&amp;","&");
//			map.put(StartLogin.k_yanzhengma, str);
//			System.out.println("验证码地址:"+str);
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return b;
//	}
//	/**第二步返回true,需要输入验证码*/ 
//	public static boolean checkUser(String url,Map map,String userName,String password){
//		boolean b = true;
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,"https://login.taobao.com/",CHeaderUtil.Content_Type__urlencoded,
//				"login.taobao.com",true);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("ua", "");
//		param.put("username",userName);
//		HttpPost post = CHeaderUtil.getPost(url, h,param);
//		HttpResponse response = null;
//	    try {
//	    	response = RedisClient.getClient(map).execute(post);
//	    	String text = new ParseResponse().parse(response);
//	    	System.out.println("---------------------------第二步-----------------------------");
//	    	System.out.println(text);
//	    	if(text.contains("true")){
////	    		b = false;
////	    		map.put(StartLogin.k_error, text);
//	    		System.out.println("....................特殊情况1...............");
//				b =false;
//				uploadVCode(StartLogin.getYanzhengmaURL(map), map);
////				b = loginJsonOfVcode(StartLogin.loginPage, map,userName,password);
////				map.put(StartLogin.k_flag_num,"1");
//	    	}
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return b;
//	}
//	/**第三步*/
//	public static Result loginJson(String url,Map map,Login login){
//		Result result = new Result();
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,"https://login.taobao.com/member/login.jhtml",CHeaderUtil.Content_Type__urlencoded+"; charset=UTF-8",
//				"login.taobao.com",true);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("ua", "");    		param.put("TPL_username",login.getLoginName());
//		param.put("TPL_password",login.getPassword());		param.put("loginsite","0");
//		param.put("TPL_redirect_url","");				param.put("from","tb");
//		param.put("fc","default");			   	param.put("style","default");
//		param.put("css_style","");				param.put("support","000001");
//		param.put("CtrlVersion","1%2C0%2C0%2C7");	param.put("loginType","3");
//		param.put("minititle","");					param.put("minipara","");
//		param.put("llnick","");							param.put("sign","");
//		param.put("need_sign",""); 					param.put("isIgnore","");
//		param.put("full_redirect","");					param.put("popid","");
//		param.put("guf","");					param.put("not_duplite_str","");
//		param.put("need_user_id","");				param.put("gvfdcname","");
//		param.put("gvfdcre","");				param.put("from_encoding","");
//		param.put("allp","");						param.put("oslanguage","");
//		param.put("sr","619*371");						param.put("osVer","windows%7C6.1");
//		param.put("naviVer","ie%7C8");
//		param.put("TPL_checkcode","");
//		
//		param.put("need_check_code",need_check_code);	param.put("newlogin",newlogin);
//		param.put("tid",tid);								param.put("pstrong",pstrong);
//		param.put("poy",poy);							param.put("sub",sub);
//		param.put("callback",callback);					param.put("umto",umto);		
//		
//		HttpPost post = CHeaderUtil.getPost(url, h,param);
//		HttpResponse response = null;
//	    try {
//	    	response = RedisClient.getClient(map).execute(post);
//	    	String text = ParseResponse.parse(response);
//	    	System.out.println("---------------------------第三步-----------------------------");
//	    	System.out.println(text);
//	    	//密码错误返回串{"state":false,"message":"您输入的密码和账户名不匹配，请重新输入。","data":{"code":3501,"url":"","needrefresh":false}}
//	    	if(text!=null){
//	    		if(text.contains("token")){
//		    		RegexPaserUtil rp = new RegexPaserUtil("token\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//					String str = rp.getText();
//					map.put(StartLogin.k_token, str);
//				}else if(text.contains("3501")&&text.contains("密码和账户名不匹配")){
//					result.setText(Result.B_MMError);
//				}else if(text.contains("该账户名不存在")){
//					result.setText(Result.B_AccountNot);
//				}else{
//					JSONObject json= new JSONObject(text);
//					if(text!=null && text.contains("ccurl"));
//					RegexPaserUtil rp = new RegexPaserUtil("ccurl\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//					String ccur = rp.getText();
//					RedisClient.setTaobaoUrl(map, ccur);
//					result.setText(Result.B_CodeInfo);
//					
////					result.setText(Result.B_CodeInfo);
//					//uploadVCode(StartLogin.getYanzhengmaURL(map), map);
//					//b = false;
//					//b = loginJsonOfVcode(StartLogin.loginPage, map,userName,password);
//				}
//	    	}else{
//	    		result.setCode(result.A_Error);
//	    	}
//	    	
//		}  catch (Exception e) {
//			e.printStackTrace();
//			result.setCode(result.A_Error);
//		}
//		return result;
//	}
//	/**第三步之验证码*/
//	public static Result loginJsonOfVcode(String url,Map<String,Object> map,Login login){
//		Result result = new Result();
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,"https://login.taobao.com/member/login.jhtml",CHeaderUtil.Content_Type__urlencoded+"; charset=UTF-8",
//				"login.taobao.com",true);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("ua", "");    		param.put("TPL_username",login.getLoginName());
//		param.put("TPL_password",login.getPassword());		param.put("loginsite","0");
//		param.put("TPL_redirect_url","");				param.put("from","tb");
//		param.put("fc","default");			   	param.put("style","default");
//		param.put("css_style","");				param.put("support","000001");
//		param.put("CtrlVersion","1%2C0%2C0%2C7");	param.put("loginType","3");
//		param.put("minititle","");					param.put("minipara","");
//		param.put("llnick","");							param.put("sign","");
//		param.put("need_sign",""); 					param.put("isIgnore","");
//		param.put("full_redirect","");					param.put("popid","");
//		param.put("guf","");					param.put("not_duplite_str","");
//		param.put("need_user_id","");				param.put("gvfdcname","");
//		param.put("gvfdcre","");				param.put("from_encoding","");
//		param.put("allp","");						param.put("oslanguage","");
//		param.put("sr","619*371");						param.put("osVer","");
//		param.put("naviVer","ie%7C8"); 			param.put("ChuJiYuTag", "ChuJiYuTag");
//		param.put("TPL_checkcode",login.getAuthcode());
//		param.put("need_check_code","true");	param.put("newlogin",newlogin);
//		param.put("tid",tid);								param.put("pstrong",pstrong);
//		param.put("poy",poy);							param.put("sub",sub);
//		param.put("callback",callback);					param.put("umto",umto);		
//		
//		HttpPost post = CHeaderUtil.getPost(url, h,param);
//		HttpResponse response = null;
//	    try {
//	    	response = RedisClient.getClient(map).execute(post);
//	    	String text = ParseResponse.parse(response);
//	    	System.out.println("---------------------------第三步2-----------------------------");
//	    	System.out.println(text);
//	    	RegexPaserUtil rp = new RegexPaserUtil("token\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			if(text!=null){
//				if(text.contains("验证码错误，请重新输入")){
//					result.setText(Result.B_CodeError);
//				}else if(text.contains("您输入的密码和账户名不匹配")){
//					result.setText(Result.B_MMError);
//				}else if(text.contains("该账户名不存在")){
//					result.setText(Result.B_AccountNot);
//				}else{
//					String str = rp.getText();
//					System.out.println("token值:"+str);
//					map.put(StartLogin.k_token, str);
//				}
//			}else{
//				result.setCode(Result.A_Error);
//			}
//			
//		
//			
//		}  catch (Exception e) {
//			e.printStackTrace();
//			result.setCode(Result.A_Error);
//		}
//		return result;
//	}
//	/**第四步**/
//	public static boolean passport(String url,Map map){
//		boolean b = false;
//		CHeader h = new CHeader(CHeaderUtil.Accept_js,"https://login.taobao.com/member/login.jhtml",CHeaderUtil.Content_Type__urlencoded,
//				"lpassport.alipay.com",false);
//	
//		HttpGet get = CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//	    try {
//	    	response = RedisClient.getClient(map).execute(get);
//	    	String text = ParseResponse.parse(response);
//	    	System.out.println("---------------------------第四步-----------------------------");
//	    	System.out.println(text);
//	    	RegexPaserUtil rp = new RegexPaserUtil("\"st\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			String str = rp.getText();
//			if(text!=null){
//				System.out.println("st值:"+str);
//				map.put(StartLogin.k_st, str);
//				b = true;
//			}
//		}  catch (Exception e) {
//			e.printStackTrace();
//		}
//		return b;
//	}
//    
//	/**第五步
//	 *0错误,1.正常,2.需要手机号确认 
//	 * 
//	 * **/
//	public static int login(String url,Map map){
//		int i = 0;
//		CHeader h = new CHeader(CHeaderUtil.Accept_x_ms_application,"https://login.taobao.com/member/login.jhtml",null,
//				"login.taobao.com",true);
//	
//		HttpGet get = CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//	    try {
//	    	response = RedisClient.getClient(map).execute(get);
//	    	String text = ParseResponse.parse(response);
//	    	System.out.println("---------------------------第五步-----------------------------");
//	    	//System.out.println(text);
//	    	
//	    	RegexPaserUtil rp = new RegexPaserUtil("\"url\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//	    	String	str = rp.getText();
//	    	if(str!=null){
//	    		System.out.println("最后的地址"+str);
//				RedisClient.setUrl(map, str);
//				if(str.contains("login_unusual.htm?")){
//		    		i=2;
//		    		//手机验证
//		    		if(RedisClient.getUrl(map)!=null){
//						LoginPage.phoneCheck(map);
//					}	    		
//		    	}else{
//		    		i=1;
//		    	}
//	    	}
//		}  catch (Exception e) {
//			e.printStackTrace();
//		}
//		return i;
//	}
//	
//	
//	/**第五一步**/
//	public static boolean phoneCheck(Map map){
//		boolean b = true;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"login.taobao.com");
//		HttpGet get = CHeaderUtil.getHttpGet(RedisClient.getUrl(map), h);
//		HttpResponse response = null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------第五一步-----------------------------");
//			System.out.println(text);
//			
//			RegexPaserUtil rp = new RegexPaserUtil("AQPop\\(\\{       url:'","'",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			String	str = rp.getText();
//
//			phoneCheck1(map, str);
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return b;
//	}
//	
//	/**第五二步,项目中前台使用**/
//	public static boolean phoneCheck1(Map map,String url){
//		boolean b = true;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"aq.taobao.com");
//		HttpGet get = CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//		String name = null;
//		String type = null;
//		String code=  null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			String text = new ParseResponse().parse(response);
//			System.out.println("---------------------------第五二步-----------------------------");
//			System.out.println(text);
//			RegexPaserUtil rp = new RegexPaserUtil("optionText\":\\[\\{\"type\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			type= rp.getText();
//			rp = new RegexPaserUtil("\"name\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			name = rp.getText();
//			rp = new RegexPaserUtil("\"code\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			code = rp.getText();
//			url = url.substring(url.indexOf("?"));
//			url = url.substring(0,url.indexOf("&redirecturl"));
//			//-------------把参数放入map中--------------------------
//			Map map2 = new HashMap();
//			System.out.println("手机号=="+name);
//			map2.put("url",url);
//			map2.put("code",code);
//			map2.put("type",type);
//			map2.put("name",name);
//			map.put("duanxinMap", map2);
//			//第三步
//			phoneCheck2(map);
//			
//			
////			//第四步:重复发短信接口
////			phoneCheckCode(map);
//		
//			
//			
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return b;
//	}
//	
//	
//	/*
//	 * 验证短信验证码是否正确，如果正确返回true;
//	 * */
//	public static Boolean phonecheck2(Map map,String checkCode){
//		
//		//第六步：此步说明验证通过，与否，返回true，说明成功，直接执行 七八步，否则说明不成功，重新发请求输入
//		Boolean flag2 = phonePost(map,  checkCode);
//		if(flag2){
//			//第七步
//			 phoneLoginId(map);
//			//第八步
//			 phoneLoginId2(map);
//		}else{
//			System.out.println("验证码输入错误");
//		}
//		return flag2;
//	}
//	
//	
//	/**第五二步,项目中前台使用**/
//	public static boolean phoneCheck2(Map map){
//		boolean b = true;
//		Map duanxinMap = (Map)map.get("duanxinMap");
//		String url 	= duanxinMap.get("url").toString();
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,null,CHeaderUtil.Content_Type__urlencoded,"aq.taobao.com",true);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("checkType", "um");  
//		HttpPost get = CHeaderUtil.getPost("http://aq.taobao.com/durex/checkcode"+url, h,param);
//		HttpResponse response = null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			String text = new ParseResponse().parse(response);
//			System.out.println(text);
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return b;
//	}
//
//	/**第五三步,获取手机验证码**/
//	public static boolean phoneCheckCode(Map map){
//		boolean b = true;
//		Map duanxinMap = (Map)map.get("duanxinMap");
//		String url 	= duanxinMap.get("url").toString();
//		String code 	= duanxinMap.get("code").toString();
//		String type 	= duanxinMap.get("type").toString();
//		String phone = "";
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,null,CHeaderUtil.Content_Type__urlencoded,"aq.taobao.com",true);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("checkType", type);    		param.put("target",code);
//		param.put("safePhoneNum", "");    		param.put("checkCode","");
//		HttpPost get = CHeaderUtil.getPost("http://aq.taobao.com/durex/sendcode"+url, h,param);
//		HttpResponse response = null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			String text = new ParseResponse().parse(response);
//			System.out.println("---------------------------第五二步-----------------------------");
//			if(text!=null && text.contains("\",\"code\"")){			
//				RegexPaserUtil rp =  new RegexPaserUtil("\"name\":\"","\",\"code",text,RegexPaserUtil.TEXTEGEXANDNRT);
//				phone = rp.getText();
//			}
//			System.out.println(text);//{"isSuccess":true,"message":"%E5%8F%91%E9%80%81%E5%A4%B1%E8%B4%A5"}
//			if(text!=null && text.contains("true")){
//				
//			}else{
//				b = false;//发送次数过多，
//			}
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return b;
//	}
////	public static String inputPhoneCode(){
////		System.out.println("请输入手机口令:");
////		Scanner in = new Scanner(System.in);
////		System.out.print("首页验证码为：");
////		String validCode = in.nextLine();
////	    return validCode;
////	}
//	/**第五六步,post提交**/
//	public static boolean phonePost(Map map,String checkCode){
//		boolean b = true;
//		
//		Map duanxinMap = (Map)map.get("duanxinMap");
//		String url 	= duanxinMap.get("url").toString();
//		String code 	= duanxinMap.get("code").toString();
//		String type 	= duanxinMap.get("type").toString();
//		String name 	= duanxinMap.get("name").toString();
//		
//		
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,null,CHeaderUtil.Content_Type__urlencoded,"aq.taobao.com",true);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("checkType", type);    		param.put("target",code);
//		param.put("safePhoneNum", name);    		param.put("checkCode",checkCode);
//		HttpPost get = CHeaderUtil.getPost("http://aq.taobao.com/durex/checkcode"+url, h,param);
//		HttpResponse response = null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------第五六步-----------------------------");
//			System.out.println(text);
//			if(!text.contains("true")){
//				b =false;
//			}
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return b;
//	}
//	/**第五七步,post提交**/
//	public static String phoneLoginId(Map map){
//		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"login.taobao.com");
//		HttpGet get = CHeaderUtil.getHttpGet("http://login.taobao.com/member/login_mid.htm?type=success", h);
//		HttpResponse response = null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------第五六步-----------------------------");
//			System.out.println(text);
//			return text;
//		}  catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	/**第五八步,post提交**/
//	public static String phoneLoginId2(Map map){
//		CHeader h = new CHeader(CHeaderUtil.Accept_,"http://login.taobao.com/member/login_mid.htm?type=success",null,"login.taobao.com");
//		HttpGet get = CHeaderUtil.getHttpGet("https://login.taobao.com/member/login_by_safe.htm?allp=&sub=false&guf=&c_is_scure=&from=tbTop&type=1&style=default&minipara=&css_style=&tpl_redirect_url=http%3A%2F%2Fwww.taobao.com%2F&popid=&callback=jsonp127&is_ignore=&trust_alipay=&full_redirect=&user_num_id=2120391613&need_sign=%3F_duplite_str%3D&from_encoding=¬_duplite_str=&sign=&ll=", h);
//		HttpResponse response = null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------第五六步-----------------------------");
//			System.out.println(text);
//			return text;
//		}  catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	
//	/**第六步**/
//	public static boolean iTaobao(String currentUser,String url,Map map,IUserService userService, IOrderService orderService,
//			IOrderItemService orderItemService,String loginName){
//		boolean b = true;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"i.taobao.com",true);
//		DefaultHttpClient httpClient = RedisClient.getClient(map);
//		HttpGet get = CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//		try {
//			response = httpClient.execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------第六步-----------------------------");
//			//System.out.println(text);
//			if(text!=null){
//				RegexPaserUtil rp = new RegexPaserUtil("<span class=\"my-alipaybtn\">	   <a  target=\"_blank\" href=\"http://lab.alipay.com/user/i.htm","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//		    	String	str = rp.getText();
//		    	if(str!=null){
//		    		str = str.replace("http","https");
//		    	}
//				//System.out.println("支付宝地址:"+str);
////				map.put(TaoBaoConstant.k_alipay_url, str);
//				RedisClient.setUrl(map, str);
////				TaoBao taobao = new TaoBao();
////				taobao.saveUserInfo( httpClient, userService,
////						 loginName,  currentUser); 
////				taobao.saveOrder(httpClient,  userService,  orderService,
////						 orderItemService,  currentUser,loginName);
//			}
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return b;
//	}
//	/**加载验证码*/
//	public static boolean uploadVCode(String url,Map map){
//		boolean b = true;
//		CHeader h = new CHeader(null,null,null);
//		HttpGet get = CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//		try {
//			response = RedisClient.getClient(map).execute(get);
//			// 输入流
//			InputStream is = response.getEntity().getContent();
//			// 1K的数据缓冲
//			byte[] bs = new byte[1024];
//			// 读取到的数据长度
//			int len;
//			// 输出的文件流
//			OutputStream os = new FileOutputStream("laozizhu.com.gif");
//			// 开始读取
//			while ((len = is.read(bs)) != -1) {
//				os.write(bs, 0, len);
//			}
//			// 完毕，关闭所有链接
//				os.close();
//				is.close();
//			System.out.println("---------------------------加载验证码-----------------------------");
//			
//		}  catch (Exception e) {
//			e.printStackTrace();
//			b = false;
//		}
//		return inputYanzhengma(map);
//	}
//		public static boolean inputYanzhengma(Map map){
//			boolean  b = true;
//			// 输出的文件流
//			System.out.println("请输入验证码:");
//			InputStreamReader isr=new InputStreamReader(System.in);
//		    BufferedReader br=new BufferedReader(isr);
//		    try{
//		           String name=br.readLine();
//		           map.put(StartLogin.k_vcode, name);
//		    }catch(IOException e){
//		        System.out.println("系统错误！");
//		        e.printStackTrace();
//		        b = false;
//		    }
//		    try{
//	            br.close();
//	            isr.close();
//	        }catch(IOException e){
//	            System.out.println("关闭流发生错误！");
//	            e.printStackTrace();
//	        }
//		    if(!b){
//		    	return b;
//		    }else{
//		    	return true;
//		    }
//		   
//		}
//		
//		/**
//		 * @param map
//		 * @param orderUrl  订单的url地址
//		 * @return
//		 */
//		public static boolean orderinfo(Map map,String orderUrl){
//			boolean b = true;
//			CHeader h = new CHeader(CHeaderUtil.Accept_,null,null,"trade.taobao.com",false);
//			//-----------项目运行中注释改行-----/
//			h.setCookie(Test.taobao);
//			HttpGet get = CHeaderUtil.getHttpGet(orderUrl, h);
//			HttpResponse response = null;
//			try {
//				 DefaultHttpClient httpClient = (DefaultHttpClient)map.get(TaoBaoConstant.k_client);
//				response = httpClient.execute(get);
//				String text = new ParseResponse().parse(response);
//				System.out.println("---------------------------支付宝个人信息页-----------------------------");
//				//System.out.println("支付宝页面内容:"+text);
//				
//			}catch(Exception e){
//				e.printStackTrace();
//				b = false;
//			}
//			return b;
//		}
//}
