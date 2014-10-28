package com.lkb.thirdUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.constant.ConstantNum;
import com.lkb.constant.ConstantProperty;
import com.lkb.thirdUtil.base.BaseInfoOther;
import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.entity.CHeader;

public class XueXin extends BaseInfoOther{

	private static String loginUrl = "https://account.chsi.com.cn/passport/login";
	private static String authUrl = "https://account.chsi.com.cn/passport/captcha.image?id=";
	private static String userinfo = "https://account.chsi.com.cn/account/account!show";
	private static String userinfo2 = "http://my.chsi.com.cn/archive/xlarchive.action";
	private static String imgurl = "https://account.chsi.com.cn/passport/captcha.image";
	
	String spider_xunxin = "spider_xunxin";
	
	private static Logger logger = Logger.getLogger(XueXin.class);
	public static void main(String[] args) {
		Login login = new Login("140321198609110310","chaoren0626"); //填写账号密码
		XueXin xx = new XueXin(login,null);
		xx.index();
		xx.inputCode(xx.getImgUrl());
		xx.login();
		xx.close();
		User user =xx.myInfo();
		System.out.println(user.getLoginName());
	}

	public XueXin(Login login, String currentUser) {
		super(login, ConstantNum.other_xuexin, currentUser);
	}
// 父类有写
//	public Map<String,Object> index(){
//		init();
//		map.put("url",getAuthcode());
//		return map;
//	}
	public void init(){
		if(!isInit()){
			try{
				String lt1 =  InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
						"lt");
				String _eventId =  InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
						"_eventId");
				String submit1 =  InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
						"submit");
				String captcha =  InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
						"captcha");
				String text = cutil.get(loginUrl);
				Document doc = Jsoup.parse(text);
				String lt =doc.select(lt1).attr("value");	
				String eventId =  doc.select(_eventId).attr("value");
				String submit =  doc.select(submit1).attr("value");
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("lt", lt);
				map.put("eventId", eventId);
				map.put("submit", submit);
				if( doc.select(captcha)!=null &&  doc.select(captcha).size()>0){
					setImgUrl(imgurl);
				}
				redismap.put("jsmap", map);
				setInit();
			}catch(Exception e){
			}
		}
	}
	public Map<String,Object> login() {
		try{
			Map<String,String> jsmap = (Map<String, String>) redismap.get("jsmap");
			String eventId =  jsmap.get("eventId");
			String lt = jsmap.get("lt");
			String submit = jsmap.get("submit");
			
			Map<String,String> param = new HashMap<String,String>();
			param.put("_eventId", eventId);
			param.put("lt", lt);
			param.put("password", login.getPassword());
			param.put("username", login.getLoginName());
			param.put("submit", submit);
			param.put("captcha", login.getAuthcode());
			
			String text = cutil.post(loginUrl, param);
	//		System.out.println(text);
			if(text!=null){
				String content = cutil.get(userinfo);
				if (content.contains("欢迎")) {
					loginsuccess();
					redismap.put("spider_xunxin", content);
					addTask(this);
				}else{
					if(text.contains("captcha")){
						setImgUrl(imgurl);
					}
				}
			}	
		}catch(Exception e){
			writeLogByLogin(e);
		}
		return map;
	}

	@Override
	public void startSpider() {
		save(myInfo());
	}

	public User myInfo() {
		User user = null;
		try{
			String content = redismap.get(spider_xunxin).toString();
			String inputarea =  InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
						"inputarea");
			String xjxlTable =  InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
					"xjxlTable");
			String tr =  InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
					"tr");
			String td =  InfoUtil.getInstance().getInfo(ConstantProperty.other_xuexin,
					"td");
			if (content.contains("欢迎")) {
				System.out.println("登录成功");
				Document docx = Jsoup.parse(content);
				String acount = docx.select(inputarea).get(0).text();
				System.out.println("账号：" + acount);
				String pwd = docx.select(inputarea).get(1).text();
				System.out.println("密码：" + pwd);
				String name = docx.select(inputarea).get(2).text();
				System.out.println("姓名：" + name);
				String type = docx.select(inputarea).get(3).text();
				System.out.println("证件类型：" + type);
				String no = docx.select(inputarea).get(4).text();
				System.out.println("证件号码：" + no);
				String tele = docx.select(inputarea).get(5).text();
				System.out.println("账号：" + tele);
				
				CHeader cHeader = new CHeader();
				cHeader.setContent_Type("text/html;charset=utf-8");
				String responseBody = cutil.get(userinfo2,cHeader);
				Document docxx = Jsoup.parse(responseBody);
				Elements trs = docxx.select(xjxlTable).select(tr);
				String name1 = trs.get(0).select(td).get(0).text();
				System.out.println(name1);
				String sex = trs.get(1).select(td).get(0).text();
				System.out.println(sex);
				String birthdayStr = trs.get(1).select(td).get(1).text();
				DateFormat df=new SimpleDateFormat("yyyy年MM月dd日");
				String ruxueStr = trs.get(2).select(td).get(0).text();
			
				String biyeStr = trs.get(2).select(td).get(1).text();
				
				Date birthday =new Date();
				Date ruxue=new Date();
				Date biye=new Date();
				try {
					birthday = df.parse(birthdayStr);
					ruxue=df.parse(ruxueStr);
					biye=df.parse(biyeStr);
				} catch (ParseException e) {
				}
				user = getUser();
				if(user==null){
					String eduType = trs.get(3).select(td).get(0).text();
					System.out.println("学历类别：" + eduType);
					String eduRecord = trs.get(3).select(td).get(1).text();
					System.out.println("教育层次" + eduRecord);
					String eduSchool = trs.get(4).select(td).get(0).text();
					System.out.println("学校" + eduSchool);
					String eduPlace = trs.get(4).select(td).get(1).text();
					System.out.println("地点：" + eduPlace);
					String specialty = trs.get(5).select(td).get(0).text();
					System.out.println("专业：" + specialty);
					String eduForm = trs.get(5).select(td).get(1).text();
					System.out.println("学习形式：" + eduForm);
					String certificateId = trs.get(6).select(td).get(0).text();
					System.out.println("证书编号：" + certificateId);
					String eduConclusion = trs.get(6).select(td).get(1).text();
					System.out.println("毕业结论：" + eduConclusion);
				    user = new User();
					UUID uuid = UUID.randomUUID();
					user.setId(uuid.toString());
					user.setParentId(currentUser);
					user.setModifyDate(new Date());
					user.setUsersource(Constant.XUEXIN);
					user.setUsersource2(Constant.XUEXIN);
					user.setLoginName(login.getLoginName());
					user.setUserName(login.getLoginName());
	
					user.setRealName(name);
					user.setCardTye(type);
					user.setCardNo(no);
					user.setPhone(tele);
					user.setSex(sex);
					user.setBirthday(birthday);
					user.setEntranceDate(ruxue);
					user.setGraduateDate(biye);
					user.setEduType(eduType);
					user.setEduRecord(eduRecord);
					user.setPhone(acount);
					user.setEduSchool(eduSchool);
					user.setSchoolPlace(eduPlace);
					user.setSpecialty(specialty);
					user.setEduForm(eduForm);
					user.setCertificateId(certificateId);
					user.setEduConclusion(eduConclusion);
					return user;
				}
			}
		}catch(Exception e){
		}
		return user;
	}
	public User getUser(){
		try{
			Map<String,String> map = new HashMap<String, String>();
			map.put("parentId", currentUser);
			map.put("usersource", Constant.XUEXIN);
			map.put("loginName", login.getLoginName());
			List<User> list = userService.getUserByParentIdSource(map);
			if(list!=null&&list.size()!=0){
				return list.get(0);
			}
		}catch(Exception e){
			
		}
		return null;
	}
	public void save(User user){
		try{
			if(user!=null){
				userService.saveUser(user);	
			}
		}catch(Exception e){
		}
	}
}
