package com.lkb.thirdUtil.shebao;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lkb.bean.SheBao;
import com.lkb.bean.User;
import com.lkb.bean.client.SocialInsuranceInput;
import com.lkb.bean.client.SocialInsuranceOut;
import com.lkb.constant.Constant;
import com.lkb.thirdUtil.BaseUser;
import com.lkb.thirdUtil.base.BaseInfoSocialInsurance;
import com.lkb.util.WaringConstaint;
import com.lkb.warning.WarningUtil;

/*
 * 上海社保
 * */
public class ShShebao extends BaseInfoSocialInsurance{
	private static String loginURL = "http://www.12333sh.gov.cn/grxx/login.jsw";
	private static String orderList = "http://www.12333sh.gov.cn/grxx/jfxx.jsw";
	private static Logger logger = Logger.getLogger(ShShebao.class);

	public static void main(String[] args) throws Exception {
	}
	public ShShebao() {
		super();
	}
	
	@Override
	public void getInputPrarms() {
		//设置返回登陆参数,
		sio.addParams(SocialInsuranceInput.param_id_card);
		sio.setRegisterUrl("http://www.12333sh.gov.cn/wsbs/wsbg/2007czzn/200803/t20080311_1047029.shtml");
	}
	/*
	  * 模拟登陆
	  * */
	 public String login1() {
			Map<String,String> param = new HashMap<String,String>();
			param.put("idcard", sii.getIdCard());
			param.put("password", sii.getPassword());
			return cutil.post(loginURL, param);
	}
	 
	 /*
		 * 验证用户名密码是否正确
		 * */
		public void login_sio(){
			String content =login1();
			if (content!=null && content.contains("近期应缴情况")) {
				loginsuccess();
				addTask(this);
			}else if(content==null){
				errorMsg = "第三方网站异常，请稍后 再试！";
			}else{
				errorMsg = "用户名或密码错误";
			}
		}
		
		
		@Override
		public void startSpider() {
			try {
				String content = cutil.get(orderList);
				parseBegin(Constant.SHSHEBAO);
				saveUserInfo(content);
				saveShebaoInfo(content);
			} catch (ParserException e) {
				e.printStackTrace();
			}finally{
				parseEnd(Constant.SHSHEBAO);
			}
		}
//		/*
//		 * 开始抓取会员信息
//		 * */
//		public void parseBegin1(){
////			String content = getText(httpclient,orderList);
//			String content = cutil.get(orderList);
//			String userId = currentUser;
//			String loginName = this.loginName;
//			String userSource = Constant.SHSHEBAO;
//			try {
//				parseBegin(parseService, userId, loginName, userSource);
//				saveUserInfo(content);
//				saveShebaoInfo(content,loginName);
//			} catch (ParserException e) {
//				e.printStackTrace();
//			}finally{
//				parseEnd(parseService, userId, loginName, userSource);
//			}
//		}	
		
		/*
		 * 通过jsoup 抓取会员基本信息
		 */
		public Boolean saveUserInfo( String content) throws ParserException {
			
			Boolean flag = false;
			try{
			Document doc = Jsoup.parse(content);
			
			Elements tables = doc.select("table[width=100%]");

			String realName = tables.get(2).select("td[class=p1]").first().text().replace("姓名：", "").trim();
			String idcard = this.loginName;
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("loginName", this.loginName);
			map.put("usersource", Constant.SHSHEBAO);
			
			
			Date modifyDate = new Date();
			
			BaseUser bu = new BaseUser();
//			Map baseMap = new HashMap();
//			baseMap.put("idcard", idcard);
//			baseMap.put("realName", realName);
//			baseMap.put("modifyDate", modifyDate);
//			bu.saveUserInfo(userService, baseMap, currentUser);	
			
			List<User> list = userService.getUserByUserNamesource(map);
			if (list != null && list.size() > 0) {
				User user = list.get(0);
				user.setRealName(realName);
				user.setIdcard(idcard);
				user.setLoginName(this.loginName);
//				user.setLoginPassword(loginPassword);
				user.setUsersource(Constant.SHSHEBAO);
				user.setUsersource2(Constant.SHSHEBAO);
				user.setParentId(currentUser);
				user.setModifyDate(modifyDate);
				userService.update(user);
			} else {
				try {
					User user = new User();
					UUID uuid = UUID.randomUUID();
					user.setId(uuid.toString());
					user.setRealName(realName);
					user.setIdcard(idcard);
					user.setLoginName(this.loginName);
//					user.setLoginPassword(loginPassword);
					user.setUsersource(Constant.SHSHEBAO);
					user.setUsersource2(Constant.SHSHEBAO);
					user.setParentId(currentUser);
					user.setModifyDate(modifyDate);
					userService.saveUser(user);
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
			}
			catch(Exception e){
				//报警
				logger.info("第193行捕获异常：",e);		
				String warnType = WaringConstaint.SHSB_1;
				WarningUtil wutil = new WarningUtil();
				wutil.warning(warningService, currentUser, warnType);
			}
			return flag;
		}
		
		
		/*
		 * 通过jsoup 抓取社保信息
		 */
		public Boolean saveShebaoInfo(String content) throws ParserException {
			Boolean flag = false;
			
			Document doc = Jsoup.parse(content);
			try{
			Elements tables = doc.select("table[width=100%]");

			Elements elements = tables.get(3).select("tr[bgcolor=#EFEFEF]");
			for(int i=0;i<elements.size();i++){
				Elements elementTds = elements.get(i).select("td");
				String payTime = elementTds.get(0).text();
				DateFormat format = new SimpleDateFormat("yyyy年MM月");         
				Date date = null;    
				try {    
				   date = format.parse(payTime);  // Thu Jan 18 00:00:00 CST 2007    
				} catch (ParseException e) {    
				   e.printStackTrace();   
				} 
				
				
				Map<String, Object> map = new HashMap<String, Object>(3);
				map.put("baseUserId", currentUser);
				map.put("source", Constant.SHSHEBAO);	
				map.put("payTime", date);
				List<SheBao> list = shebaoService.getSheBaoByBaseUseridsource(map);
				if(list!=null && list.size()>0){
					//对写入的历史数据不再写入
//					SheBao sheBao = list.get(0);
//					sheBao.setPayBase(payBase);
//					sheBao.setBaseUserId(currentUser);
//					sheBao.setPayFeedPerson(payFeedPerson);
//					sheBao.setPayMedPerson(payMedPerson);
//					sheBao.setPayUmemplyPerson(payUmemplyPerson);
//					shebaoService.update(sheBao);
				}else{
					BigDecimal payBase = new BigDecimal(elementTds.get(1).text().trim());
					BigDecimal payFeedPerson = new BigDecimal(elementTds.get(2).text());
					BigDecimal payMedPerson = new BigDecimal(elementTds.get(3).text());
					BigDecimal payUmemplyPerson = new BigDecimal(elementTds.get(4).text());
					
					SheBao sheBao = new SheBao();
					UUID uuid = UUID.randomUUID();
					sheBao.setId(uuid.toString());					
					sheBao.setLoginName(this.loginName);				
					sheBao.setPayFeedBase(payBase);
					sheBao.setPayMedBase(payBase);
					sheBao.setPayUmemplyBase(payBase);
					sheBao.setPayInjuryBase(payBase);
					sheBao.setPayFeedPerson(payFeedPerson);
					sheBao.setPayMedPerson(payMedPerson);
					sheBao.setPayTime(date);
					sheBao.setPayUmemplyPerson(payUmemplyPerson);
					sheBao.setSource(Constant.SHSHEBAO);
					shebaoService.saveSheBao(sheBao);
				}
			}
			}catch(Exception e){
				//报警
				logger.info("第265行捕获异常：",e);		
				String warnType = WaringConstaint.SHSB_2;
				WarningUtil wutil = new WarningUtil();
				wutil.warning(warningService, currentUser, warnType);
			}
				
			return flag;
		}
	
	
}
