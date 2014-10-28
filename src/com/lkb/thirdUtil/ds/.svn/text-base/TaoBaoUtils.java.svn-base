//package com.lkb.thirdUtil.ds;
//
//import java.io.File;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONObject;
//
//import com.lkb.bean.client.Login;
//import com.lkb.constant.Constant;
//import com.lkb.constant.ConstantNum;
//import com.lkb.constant.TaoBaoConstant;
//import com.lkb.service.IOrderItemService;
//import com.lkb.service.IOrderService;
//import com.lkb.service.IParseService;
//import com.lkb.service.IPayInfoService;
//import com.lkb.service.IUserService;
//import com.lkb.service.IYuEBaoService;
//import com.lkb.thirdUtil.base.BaseInfoBuisness;
//import com.lkb.thirdUtil.PicUpload;
//import com.lkb.util.InfoUtil;
//import com.lkb.util.RegexPaserUtil;
//import com.lkb.util.httpclient.CHeaderUtil;
//import com.lkb.util.httpclient.CUtil;
//import com.lkb.util.httpclient.ParseResponse;
//import com.lkb.util.httpclient.entity.CHeader;
//import com.lkb.util.httpclient.entity.Result;
//import com.lkb.util.redis.RedisClient;
//import com.lkb.util.taobao.util.LoginPage;
//import com.lkb.util.taobao.util.OpenOtherPage;
//
//public class TaoBaoUtils  extends BaseInfoBuisness{
////	private static String orderListURL = "http://trade.taobao.com/trade/itemlist/listBoughtItems.htm?pageNum=";
////	private static String userInfoURL = "http://i.taobao.com/user/baseInfoSet.htm";
////	private static String loginUrl = "https://login.taobao.com/member/login.jhtml";
//	private static int coun_num = 5;
//	
//	/**多次
//	 * @return
//	 */
//	public  boolean  indexs(Map<String,Object> redismap,String currentUser,String loginName){
//		boolean b = false;
//		int m = 0;
//		//0代表第一步.1代表第二部
//		for (int i = 0; i < 2; i++) {
//			m = RedisClient.getBuzhou(redismap);
//			if(m>i){
//				i = m;
//				break;
//			}
//			for (int j = 0; j < coun_num; j++) {
//				switch (m) {
//				case 0:
//					b = index(redismap);
//					break;
//				case 1:
//					b = checkUser(loginName, currentUser,redismap);
//					break;
//				default:
//					b = true;
//					break;
//				}
//				if(b){
//					break;
//				}
//			}
//			if(!b){break;}
//		}
//		
//		return b;
//	}
//	public  boolean index(Map<String,Object> redismap){
//		String url = TaoBaoConstant.loginPage;
//		boolean b = false;
//		CHeader h = new CHeader(CHeaderUtil.Accept_,null,null);
//		HttpGet get= CHeaderUtil.getHttpGet(url, h);
//		HttpResponse response = null;
//	    try {
//	    	DefaultHttpClient httpclient = RedisClient.getClient(redismap);
//	    	response = httpclient.execute(get);
//	    	String text = ParseResponse.parse(response);
//	    	System.out.println("---------------------------第一步-----------------------------");
//	    	System.out.println(text);
//	    	RegexPaserUtil rp = new RegexPaserUtil("id=\"J_StandardCode_m\" src=\"https://s.tbcdn.cn/apps/login/static/img/blank.gif\" data-src=\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//			String str = rp.getText();
//			if(str!=null){
//				str = str.replace("&amp;","&");
//				RedisClient.setYZMUrl(redismap, str);
//				b = true;
//				RedisClient.setBuzhou(redismap, 1);
//			}
//			System.out.println("验证码地址:"+str);			
//		}  catch (Exception e) {
//			e.printStackTrace();
//		}
//		return b;
//	}
//	
//	/**第二步返回true,需要输入验证码*/ 
//	public  boolean checkUser(String TPL_username,String currentUser,Map<String,Object> redismap){
//		String url =TaoBaoConstant.loginPage_1;
//		boolean b = false;
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,"https://login.taobao.com/",CHeaderUtil.Content_Type__urlencoded,
//				"login.taobao.com",true);
//		Map<String,String> param = new HashMap<String,String>();
//		 try {
//			param.put("ua", "");
////			param.put("username",URLEncoder.encode(TPL_username,"utf-8"));
//			param.put("username",TPL_username);
//			HttpPost post = CHeaderUtil.getPost(url, h,param);
//			HttpResponse response = null;
//			String img = "none";
//	   
//	    	DefaultHttpClient httpclient = RedisClient.getClient(redismap);
//	    	response = httpclient.execute(post);
//	    	String text = ParseResponse.parse(response);
//	    	System.out.println("---------------------------第二步-----------------------------");
//	    	System.out.println(text);
//	    	if(text!=null){
//		    	if(text.contains("true") && text.contains("url")){
//		    		JSONObject json;
//					json = new JSONObject(text);
//					String authurl = json.get("url").toString();
//					
//					if(authurl!=null && authurl.length()>1){
//						RedisClient.setTaobaoUrl(redismap, authurl);
//						String img2 = getAuthcode(redismap,currentUser,authurl);
//						RedisClient.setYZMUrl(redismap, img2);
//					}
//					
//	//	    		 
//		    	}
//		    	b = true;
//		    	
//		    	RedisClient.setBuzhou(redismap, 2);
//	    	}
//		}  catch (Exception e) {
//			e.printStackTrace();			
//		}
//		return b;
//	}
//	
//	
//	/*
//	 * 验证是否登陆成功
//	 * */
//	public Map  vertifyLogin(Map<String,Object> redismap,String currentUser,Login login){
//		Boolean	flag = indexs(redismap,currentUser,login.getLoginName());
//		String phone = "";
//		Map mp = new HashMap();
//		Result result = new Result();
//		boolean b = false;
//		//必须执行到第二部
//		if(RedisClient.getBuzhou(redismap)==2){
//			LoginPage loginPage = new LoginPage();
//		
//			if(login.getAuthcode()!=null && login.getAuthcode().length()>0){
//				RedisClient.setYZMValue(redismap, login.getAuthcode());		
//				result = loginPage.loginJsonOfVcode(TaoBaoConstant.loginPage, redismap,login);
//				if(result.getCode()!=Result.A_Error){
//					flag = true;
//				}
////				map.put(StartLogin.k_flag_num,"1");
//			}else{
//				result = loginPage.loginJson(TaoBaoConstant.loginPage, redismap,login);	
//				if(result.getCode()!=Result.A_Error){
//					flag = true;
//				}
////				if(flag == false){
////					 img = getAuthcode(   currentUser,picName);
////				}
//			}
//			
//			if(result.getText()!=null && result.getText().contains("ccurl")){
//				String text = result.getText();			
//				RegexPaserUtil rp = new RegexPaserUtil("ccurl\":\"","\"",text,RegexPaserUtil.TEXTEGEXANDNRT);
//				String ccur = rp.getText();								
//				RedisClient.setTaobaoUrl(redismap, ccur);
//				String url = getAuthcode(redismap,  currentUser,ccur);
//				RedisClient.setYZMUrl(redismap, url);
//				mp.put("img",url);
//			}
//			
//			int i = 0;
//			
//			if(flag&&result.getText()==null){
//				//第四步
//				String loginPage_3 = "https://passport.alipay.com/mini_apply_st.js?site=0&callback=vstCallback102&token="+ redismap.get(TaoBaoConstant.k_token).toString();
//				flag = LoginPage.passport(loginPage_3, redismap);
//				if(flag){
//					//第五步
//					loginPage_3 = "https://login.taobao.com/member/vst.htm?st="+ redismap.get(TaoBaoConstant.k_st).toString()+"&params=style%3Ddefault%26sub%3D%26TPL_username%3D"+"zhaoyulong0626"+"%26loginsite%3D0%26from_encoding%3D%26not_duplite_str%3D%26guf%3D%26full_redirect%3D%26isIgnore%3D%26need_sign%3D%26sign%3D%26from%3Dtb%26TPL_redirect_url%3D%26css_style%3D%26allp%3D&_ksTS="+new Date().getTime()+"_115&callback=jsonp116";
//					i = LoginPage.login(loginPage_3, redismap);
//					if(i==2){
//						if(redismap.get("duanxinMap")!=null){
//							Map duanxinMap = (Map)redismap.get("duanxinMap");
//							phone = duanxinMap.get("name").toString();
//						}
//						b = false;
//					}else if(i==0){
//						//==1是正常的
//						flag = false;
//						b = true;
//					}else{
//						b = true;
//					}
//				}
//				
//			}else{
//				flag = false;
//			}
//		}else{
//			result.setText("初始化失败!");
//		}
//		mp.put("flag", flag);
//		mp.put("msg", result.getTexts()!=null?result.getTexts():"");
//		mp.put("phone", phone);
//		
//		mp.put("state", b);
//		if(flag&&b){
//			RedisClient.setBuzhou(redismap, ConstantNum.client_buzhou_value);//登陆成功
//		}
//		
//		return mp;
//	}
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
//	
//	
//	
//	/**第五三步,获取手机验证码**/
//	public static boolean phoneCheckCode(Map map){
//		boolean b = true;
//		Map duanxinMap = (Map)map.get("duanxinMap");
//		String url 	= duanxinMap.get("url").toString();
//		String code 	= duanxinMap.get("code").toString();
//		String type 	= duanxinMap.get("type").toString();
//		CHeader h = new CHeader(CHeaderUtil.Accept_json,null,CHeaderUtil.Content_Type__urlencoded,"aq.taobao.com",true);
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("checkType", type);    		param.put("target",code);
//		param.put("safePhoneNum", "");    		param.put("checkCode","");
//		HttpPost get = CHeaderUtil.getPost("http://aq.taobao.com/durex/sendcode"+url, h,param);
//		HttpResponse response = null;
//		try {
//			response =  RedisClient.getClient(map).execute(get);
//			String text = ParseResponse.parse(response);
//			System.out.println("---------------------------第五二步-----------------------------");
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
//	
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
//			response =  RedisClient.getClient(map).execute(get);
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
//			response =  RedisClient.getClient(map).execute(get);
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
//		HttpGet get = CHeaderUtil.getHttpGet("https://login.taobao.com/member/login_by_safe.htm?allp=&sub=false&guf=&c_is_scure=&from=tbTop&type=1&style=default&minipara=&css_style=&tpl_redirect_url=http%3A%2F%2Fwww.taobao.com%2F&popid=&callback=jsonp127&is_ignore=&trust_alipay=&full_redirect=&user_num_id=2120391613&need_sign=%3F_duplite_str%3D&from_encoding=?_duplite_str=&sign=&ll=", h);
//		HttpResponse response = null;
//		try {
//			response =  RedisClient.getClient(map).execute(get);
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
//	/**
//	 * 执行登陆的一些操作
//	 * */
//	public Boolean login(Map map, String currentUser, String userName,
//			IParseService parseService, IUserService userService,
//			IOrderService orderService, IOrderItemService orderItemService,
//			IPayInfoService payInfoService, IYuEBaoService yuebaoService) {
//		Boolean b = false;
//		String userId = currentUser;
//		String loginName = userName;
//		String userSource = Constant.TAOBAO;
//		try {
//			parseBegin(parseService, userId, loginName, userSource);
//
//			// 第六步
//			b = LoginPage.iTaobao(currentUser, RedisClient.getUrl(map), map,
//					userService, orderService, orderItemService, userName);// 下边直接跳到支付宝
//
//			if (RedisClient.getUrl(map) != null) {
//				OpenOtherPage openOtherPage = new OpenOtherPage();
//				// 第七步 打开支付宝
//				b = openOtherPage.getAlipay(RedisClient.getUrl(map), map,
//						userService, userName, currentUser, payInfoService);
//				String k_flag_num = null;
//				// if(map.get(TaoBaoConstant.k_flag_num)!=null){
//				// k_flag_num = map.get(TaoBaoConstant.k_flag_num).toString();
//				// }else{
//				// return null;
//				// }
//				if (k_flag_num != null && k_flag_num.equals("2")) {
//					System.out.print("流程结束,未开通余额宝");
//				} else {
//					b = openOtherPage
//							.getYuEBao(map, userService, yuebaoService);
//					System.out.print("执行完毕");
//				}
//			}
//
//			// 暂且存储,如果确认结果100%插入,就销毁
//			// RedisClient.setRedisMap(map,ConstantNum.getClientMapKey(userName,
//			// ConstantNum.ds_taobao),10);
//			RedisClient.getClient(map).close(); // 关闭
//			RedisClient.delRedisMap(ConstantNum.getClientMapKey(currentUser
//					+ userName, ConstantNum.ds_taobao)); // 移除
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			parseEnd(parseService, userId, loginName, userSource);
//		}
//		return b;
//	}
//	
//
//	
////    // 获取验证码
//	public String getAuthcode(Map redisMap,String currentUser,String url) {
//		DefaultHttpClient httpclient =  RedisClient.getClient(redisMap);
//		int random = (int)(Math.random()*1000);
//		String picName = currentUser+random+"_taobao.png";
//		java.text.DateFormat format2 = new java.text.SimpleDateFormat(
//				"yyyyMMdd");
//		String dataPath = format2.format(new Date());
//		
//		
//		String authcodePath = InfoUtil.getInstance().getInfo("road","authcodePath");
//		String path1 = authcodePath+ "/"+dataPath;
//		File file2 = new File(path1);
//		if (!file2.exists() && !file2.isDirectory()) {
//			file2.mkdir();
//		}
//		String destfilename = path1 + "/"+ picName;
//		
//		CUtil.downimgCode(url,new CHeader(), client,destfilename);
//		String imgPath = InfoUtil.getInstance().getInfo("road",
//				"imgPath");	
//		String fileName =    imgPath+ dataPath + "/" + picName;
//		String img = fileName;
//		PicUpload picUpload = new PicUpload();
//		String localFilePre = InfoUtil.getInstance().getInfo("server/server",
//				"localFilePre");	
//		picUpload.upload(localFilePre+fileName);
//		String imgDomain = InfoUtil.getInstance().getInfo("road",
//				"imgDomain");	
//		return imgDomain+img;
//	}
//
//	
//	
//
//
//}
