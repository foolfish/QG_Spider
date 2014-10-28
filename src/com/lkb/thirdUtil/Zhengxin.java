package com.lkb.thirdUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.User;
import com.lkb.bean.ZhengxinDetail;
import com.lkb.bean.ZhengxinSummary;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.service.IParseService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.service.IZhengxinDetailService;
import com.lkb.service.IZhengxinSummaryService;
import com.lkb.thirdUtil.base.BaseInfoOther;
import com.lkb.util.InfoUtil;
import com.lkb.util.WaringConstaint;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.warning.WarningUtil;

public class Zhengxin extends BaseInfoOther{
	private static Logger logger = Logger.getLogger(Zhengxin.class);
	public static final DateFormat format = new SimpleDateFormat(
			"yy_MM_dd_HH_mm_ss");
	private static String loginUrl = "https://ipcrs.pbccrc.org.cn/page/login/login.jsp";
	public static String authUrl = "https://ipcrs.pbccrc.org.cn/imgrc.do?";
	//private static String sbLoginURL = "https://ipcrs.pbccrc.org.cn/login.do";
	private static String zxurl  = "https://ipcrs.pbccrc.org.cn";
	
	private static String cxmLoginURL = "https://ipcrs.pbccrc.org.cn/simpleReportAction.do?method=welcome";
	private static String cxmLoginCommitURL = "https://ipcrs.pbccrc.org.cn/simpleReportAction.do?";

	public Zhengxin(Login login) {
		super(login);
	}
	public Zhengxin(Login login, String currentUser) {
		super(login, ConstantNum.other_zhengxin, currentUser);
	}

	
	
	/*
	 * 抓取用户基本信息
	 * */
	public void parseUserInfo(IUserService userService,String currentUser,String loginName,  String content,IWarningService warningService){
		try{
		String table =  InfoUtil.getInstance().getInfo("zx",
				"table");
		String td =  InfoUtil.getInstance().getInfo("zx",
				"td");
		Document doc = Jsoup.parse(content);
		
		Elements tables=doc.select(table);
		Element table1=tables.get(1);
		Elements tds=table1.select(td);
		String reportId=tds.get(0).text().replace("报告编号： ", "");
		Element table2=tables.get(2);
		Elements tds2=table2.select(td);
		String realName=tds2.get(0).text().replace("姓名：", "");
		String cardType=tds2.get(1).text().replace("证件类型：", "");
		String cardNo=tds2.get(2).text().replace("证件号码：", "");
		String merry=tds2.get(3).text();
		
		Date modifyDate = new Date();
//		BaseUser bu = new BaseUser();
//		Map baseMap = new HashMap();
//	
//		baseMap.put("modifyDate", modifyDate);
//		bu.saveUserInfo(userService, baseMap, currentUser);	
		
		Map<String, String> map = new HashMap<String, String>(2);
		map.put("parentId", currentUser);
//		map.put("loginName", loginName);
		map.put("usersource", Constant.ZHENGXIN);
		List<User> list = userService.getUserByParentIdSource(map);
		if (list != null && list.size() > 0) {
			User user = list.get(0);
			user.setLoginName(loginName);
			user.setUsersource(Constant.ZHENGXIN);
			user.setUsersource2(Constant.ZHENGXIN);
			user.setParentId(currentUser);
			user.setModifyDate(modifyDate);
			user.setRealName(realName);
			user.setCardTye(cardType);
			user.setCardNo(cardNo);
			user.setMerry(merry);
			userService.update(user);
		} else {
			try {
				User user = new User();
				UUID uuid = UUID.randomUUID();
				user.setLoginName(loginName);
				user.setId(uuid.toString());
				user.setRealName(realName);
				user.setCardTye(cardType);
				user.setCardNo(cardNo);
				user.setMerry(merry);
				user.setUsersource(Constant.ZHENGXIN);
				user.setUsersource2(Constant.ZHENGXIN);
				user.setParentId(currentUser);
				user.setModifyDate(modifyDate);
				userService.saveUser(user);
			} catch (Exception e) {
				logger.info(e.getStackTrace());
				e.printStackTrace();
			}
		}
		}
		catch(Exception e){
			//报警
			logger.info("第378行捕获异常：",e);		
			String warnType = WaringConstaint.ZX_2;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

	}
	public void parseZhengxinReportInfo(IZhengxinSummaryService zxSummaryService,IZhengxinDetailService zxDetailService,IWarningService warningService,String loginName, String content){
		Document doc = Jsoup.parse(content);
		String table =  InfoUtil.getInstance().getInfo("zx",
				"table");
		String tr =  InfoUtil.getInstance().getInfo("zx",
				"tr");
		String td =  InfoUtil.getInstance().getInfo("zx",
				"td");
		String h1 =  InfoUtil.getInstance().getInfo("zx",
				"h1");
		String li =  InfoUtil.getInstance().getInfo("zx",
				"li");
		try{
		Elements tables=doc.select(table);
		Element table1=tables.get(5);
		Elements trs=table1.select(tr);
		Elements tds=null;
		
		for(int i=1;i<trs.size();i++){
			tds=trs.get(i).select(td);
			String state=tds.get(0).text().trim().replaceAll(" ", "");
			int xiny=Integer.parseInt(tds.get(1).text());
			int zhuf=Integer.parseInt(tds.get(2).text());
			int qit=Integer.parseInt(tds.get(3).text());
	
			Map map=new HashMap();
			map.put("loginName", loginName);
			map.put("state", state);
			List<ZhengxinSummary> list=zxSummaryService.getZhengxinSummary(map);
			if(list!=null&&list.size()>0){
				ZhengxinSummary summary = list.get(0);
				zxSummaryService.update(summary);
			}
			else{
				ZhengxinSummary summary=new ZhengxinSummary();
				UUID uuid = UUID.randomUUID();
				summary.setId(uuid.toString());
				summary.setLoginName(loginName);
				summary.setState(state);
				summary.setXinyNum(xiny);
				summary.setZhufNum(zhuf);
				summary.setQitNum(qit);
				zxSummaryService.saveZhengxinSummary(summary);
			}
		}
		String type1=doc.select(h1).get(1).text();
		Element ol1=doc.select(h1).get(1).nextElementSibling();
		Elements lis1=ol1.select(li);
		for(int i=0;i<lis1.size();i++){
			String record=lis1.get(i).text();
			ZhengxinDetail zxDetail=new ZhengxinDetail();
			zxDetail.setcType(type1);
			zxDetail.setLoginName(loginName);
			zxDetail.setcRecord(record);
			Map map=new HashMap();
			map.put("cType",type1);
			map.put("loginName", loginName);
			map.put("cRecord", record);
			List<ZhengxinDetail> list=zxDetailService.getZhengxinDetail(map);
			if(list!=null&&list.size()>0){
				
			}
			else{
				UUID uuid = UUID.randomUUID();
				zxDetail.setId(uuid.toString());
				zxDetailService.saveZhengxinDetail(zxDetail);
			}
		}
//		String type2=doc.select(h1).get(2).text();
//		Element ol2=doc.select(h1).get(2).nextElementSibling();
//		Elements lis2=ol2.select(li);
//		for(int i=0;i<lis2.size();i++){
//			String record=lis2.get(i).text();
//			ZhengxinDetail zxDetail=new ZhengxinDetail();
//			zxDetail.setcType(type2);
//			zxDetail.setBaseUserId(currentUser);
//			zxDetail.setcRecord(record);
//			Map map=new HashMap();
//			map.put("cType",type2);
//			map.put("baseUserId", currentUser);
//			map.put("cRecord", record);
//			List<ZhengxinDetail> list=zxDetailService.getZhengxinDetail(map);
//			if(list!=null&&list.size()>0){
//			}
//			else{
//				UUID uuid = UUID.randomUUID();
//				zxDetail.setId(uuid.toString());
//				zxDetailService.saveZhengxinDetail(zxDetail);
//			}
//		}
		}
		catch(Exception e){
			//报警
			logger.info("第468行捕获异常：",e);		
			String warnType = WaringConstaint.ZX_3;
			WarningUtil wutil = new WarningUtil();
			wutil.warning(warningService, currentUser, warnType);
		}

	}
	

	/* (non-Javadoc)
	 * @see com.lkb.thirdUtil.base.BaseInfo#index()
	 */
//	public Map<String,Object> index(){
//		init();
//		map.put("url",getAuthcode());
//		return map;
//	}
	
	public void init() {
		if(!isInit()){
			CHeader h = new CHeader();
			String str = null;
			try{
				Map<String,String> map = new HashMap<String,String>();
				str = cutil.get(loginUrl, h);
				Document doc=Jsoup.parse(str);
				String actionUrl = zxurl+doc.select("form[name=loginForm]").first().attr("action");
				
		
				String page1 =  InfoUtil.getInstance().getInfo("zx",
							"page");	
				String method1 =  InfoUtil.getInstance().getInfo("zx",
						"method");	
				String date1 =  InfoUtil.getInstance().getInfo("zx",
						"date");	
				String page=doc.select(page1).first().attr("value");
				map.put("page", page);
				String method=doc.select(method1).first().attr("value");
				map.put("method", method);
				String date=doc.select(date1).first().attr("value");
				map.put("date", date);
				redismap.put("zxurl", actionUrl);
				redismap.put("zxMap", map);
				setImgUrl(authUrl);
				setInit();
		//		String jseesionId=ContextUtil.getCookieValue("JSESSIONID", null, client.getCookieStore());
		//		String authurl = +"&JSESSIONID="+jseesionId;
			}catch(Exception e){
			}
		
		}
	}
	/*
	 * 验证用户是否登陆
	 * */
	public Map<String,Object>  login() {
		errorMsg = "用户名，密码或验证码错误！";
		Map<String,String> param = new LinkedHashMap<String,String>();
		param.put("loginname", login.getLoginName());
		param.put("password", login.getPassword());
		param.put("_@IMGRC@_", login.getAuthcode());
		try {
			//page=1&method=login&date=1407640992979&loginname=anyfly&password=Wabc123&_@IMGRC@_=xhye59
			Map<String,String> zxmap =  (Map)redismap.get("zxMap");
			Iterator it = zxmap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = zxmap.get(key).toString();
				param.put(key, value);
			}
			Boolean flag1 = false;
			Boolean flag2=false;
		
			CHeader h = new CHeader();
			h.setContent_Type(CHeaderUtil.Content_Type__urlencoded);
			h.setReferer("https://ipcrs.pbccrc.org.cn/page/login/login.jsp");
			//h.setAccept_Encoding("gzip, deflate");
			h.setHost("ipcrs.pbccrc.org.cn");
			String actionUrl = redismap.get("zxurl").toString();
			String text = cutil.post(actionUrl, h, param);
			if(text!=null){				
				String btn =  InfoUtil.getInstance().getInfo("zx",
     					"btn");
				String TOKEN =  InfoUtil.getInstance().getInfo("zx",
     					"TOKEN");
				if(text.contains(btn)){
					Document doc=Jsoup.parse(text);
					try{
						if(text.contains("_error_field_")){
							errorMsg =  doc.select("span[id=_error_field_]").first().text().trim();
							if(errorMsg.trim().equals("")&&text.contains("_@MSG@_")){
								errorMsg =  doc.select("span[id=_@MSG@_]").first().text().trim();
							}
							
						}
					}
					catch(Exception e){
						errorMsg = "用户名，密码或验证码错误！";
					}
				}else{
					flag1 = true;
					String s = cutil.get(cxmLoginURL);
					Document doc=Jsoup.parse(s);	
					String method="view";
					
					String token=doc.select(TOKEN).first().val();
					String url=cxmLoginCommitURL+"method="+method+"&tradeCode="+login.getSpassword()+"&org.apache.struts.taglib.html.TOKEN="+token;
				
					
					CHeader h2 = new CHeader();
					String str =  cutil.get(url, h2);
					if(str.contains("您输入的查询码错误或已过期！")){
//						setloginfalse();
						errorMsg =  "您输入的查询码错误或已过期！";
					}
					else {
						map.put("content", str);						
						flag2=true;
					}
					 if(flag1&&flag2){
						 loginsuccess();		
					 }
				}
			}
		} catch (Exception e) {
			writeLogByLogin(e);
		}
	//公共返回值,父类中
		return map;
	}

	
	/*
	 * 开始抓取会员信息
	 * */
	public void parseBegin(String loginName,String content, 
			IUserService userService, IZhengxinSummaryService zhengxinSummaryService,IZhengxinDetailService zhengxinDetailService,IWarningService warningService,String currentUser,IParseService parseService){
			parseBegin(parseService, currentUser, loginName, Constant.ZHENGXIN);
			parseUserInfo(userService,currentUser, loginName, content, warningService);
			parseZhengxinReportInfo(zhengxinSummaryService,zhengxinDetailService, warningService,loginName,  content);
			parseEnd(parseService, currentUser, loginName, Constant.ZHENGXIN);
			//close();
		
	}

}