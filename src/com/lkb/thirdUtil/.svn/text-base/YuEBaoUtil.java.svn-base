package com.lkb.thirdUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.User;
import com.lkb.bean.YuEBao;
import com.lkb.constant.Constant;
import com.lkb.service.IUserService;
import com.lkb.service.IYuEBaoService;
import com.lkb.util.InfoUtil;

public class YuEBaoUtil {
	private static Logger logger = Logger.getLogger(YuEBaoUtil.class);
	/*
	 * 保存用户订单信息
	 */
	public void saveLog(String content, IUserService userService,IYuEBaoService yuebaoService,String alipayName) {	
		//打开余额宝
			
				String ft =  InfoUtil.getInstance().getInfo("ds/yuebao",
     					"ft");
				String ft2 =  InfoUtil.getInstance().getInfo("ds/yuebao",
     					"ft2");

				Document doc = Jsoup.parse(content);
				Element div =  doc.select(ft).get(0);
				String text1 =  div.text().replace("元", "");
				Element div2 =  doc.select(ft2).get(0);
				String text2 =  div2.text().replace("元", "");
				System.out.println(text1);
				System.out.println(text2);
				BigDecimal yBalance = new BigDecimal("0");
				BigDecimal yincome = new BigDecimal("0");
				try{
					yBalance = new BigDecimal(text1);
					yincome = new BigDecimal(text2);
				}catch(Exception e ){
					logger.info("第43行捕获异常：",e);		
				}
				Date modifyDate = new Date();
				Map<String, String> map = new HashMap<String, String>(2);
				map.put("loginName", alipayName);
				map.put("usersource", Constant.ZHIFUBAO);
				List<User> list = userService.getUserByUserNamesource(map);
				if (list != null && list.size() > 0) {
					User user = list.get(0);
					user.setLoginName(alipayName);
					user.setUsersource(Constant.ZHIFUBAO);
					user.setUsersource2(Constant.ZHIFUBAO);
					user.setModifyDate(modifyDate);
					user.setyBalance(yBalance);
					user.setYincome(yincome);
					userService.update(user);
				} else {
					try {
						User user = new User();
						UUID uuid = UUID.randomUUID();
						user.setId(uuid.toString());
						user.setLoginName(alipayName);
						user.setUsersource(Constant.ZHIFUBAO);
						user.setUsersource2(Constant.ZHIFUBAO);
						user.setModifyDate(modifyDate);
						user.setyBalance(yBalance);
						user.setYincome(yincome);
						userService.saveUser(user);
					} catch (Exception e) {
						logger.info("第70行捕获异常：",e);		
						e.printStackTrace();

					}
				}
							
		}
	
	
	/*
	 * 开始解析余额宝数据
	 * */
	public  void listLog(String content,IYuEBaoService yuebaoService,String alipayName){
		
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
					logger.info("第116行捕获异常："+e1.getMessage());
					e1.printStackTrace();
				}
				
				Map map = new HashMap(2);
				map.put("alipayName", alipayName);
				map.put("cTime",cTime);
				List<YuEBao> list = yuebaoService.getYuEBaoByBaseUseridsource(map);
				if(list!=null && list.size()>0){
					break;
				}else{
					YuEBao yuebao = new YuEBao();
					UUID uuid = UUID.randomUUID();
					yuebao.setId(uuid.toString());
					yuebao.setAlipayName(alipayName);
					yuebao.setAmount(new BigDecimal(amount));
					yuebao.setAmountType(amountType);
					yuebao.setcTime(cTime);
					yuebaoService.saveYuEBao(yuebao);
				}
			}
			
		}
					
					

	}
	

}
