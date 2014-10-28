package com.lkb.thirdUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lkb.bean.User;
import com.lkb.service.IUserService;

public class BaseUser {
	private static Logger logger = Logger.getLogger(BaseUser.class);
	/**
	 * 更新主user信息
	 * @param userService
	 * @param map
	 * @param currentUserId
	 */
//	
//	public static void saveUserInfo(IUserService userService,Map map,String currentUserId){
//		User user = userService.findById(currentUserId);
//		if(user!=null){
//			for(Iterator keys = map.keySet().iterator(); keys.hasNext();) {
//				   String key = (String) keys.next();
//				   if(key.equals("birthday")){
//					   Date birthday= (Date)map.get(key);
//					   	String day = "1990年01月01日";
//						DateFormat format = new SimpleDateFormat("yyyy年MM月DD日");         
//						Date date = null;    
//						try {    
//						   date = format.parse(day);  // Thu Jan 18 00:00:00 CST 2007    
//						} catch (ParseException e) {    
//							logger.info("第30行捕获异常：",e);		
//						   e.printStackTrace();   
//						} 
//						if(!birthday.equals(date)){
//							user.setBirthday(birthday);
//						} 
//				   }
//				   else if(key.equals("email")){
//					   String value= (String)map.get(key);  
//					   if(value!=null&&!value.trim().equals("")){
//						   user.setEmail(value);
//					   }
//				   }
//				   else if(key.equals("idcard")){
//					   String value= (String)map.get(key);  
//					   if(map.get("realName")!=null){
//						   String realName = map.get("realName").toString();   
//						   if(user.getRealName().equals(realName)&&value!=null&&!value.trim().equals("")){
//							   user.setIdcard(value);
//						   }
//					   }
//					   
//				   }else if(key.equals("addr")){
//					   String value= (String)map.get(key);  
//					   if(value!=null&&!value.trim().equals("")){
//						   user.setAddr(value);
//					   }
//				   }else if(key.equals("sex")){
//					   String value= (String)map.get(key);  
//					   if(value!=null&&!value.trim().equals("")){
//						   user.setSex(value);
//					   }
//				   }else if(key.equals("taobaoName")){
//					   String value= (String)map.get(key);  
//					   if(value!=null&&!value.trim().equals("")){
//						   user.setTaobaoName(value);
//					   }
//				   }else if(key.equals("major")){
//					   String value= (String)map.get(key);  
//					   if(value!=null&&!value.trim().equals("")){
//						   user.setMajor(value);
//					   }
//				   }else if(key.equals("redstar")){
//					   String value= (String)map.get(key);  
//					   if(value!=null&&!value.trim().equals("")){
//						   user.setRedstar(value);
//					   }
//				   }else if(key.equals("live")){
//					   String value= (String)map.get(key);  
//					   if(value!=null&&!value.trim().equals("")){
//						   user.setLive(value);
//					   }
//				   }else if(key.equals("modifyDate")){
//					   Date value= (Date)map.get(key);  
//					   user.setModifyDate(value);				  
//				   }
//				   userService.update(user);
//					
//	
//				} 
//			
//		}
//	}
}
