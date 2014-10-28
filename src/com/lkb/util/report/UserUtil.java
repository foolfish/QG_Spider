package com.lkb.util.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.service.IUserService;

public class UserUtil {

	/*
	 * 是否实名认证
	 * */
	public String isRalName(String currentUser,String realname,IUserService userService){
		String isrealName = "否";
		Map canMap = new HashMap(1);
		canMap.put("parentId", currentUser);
		canMap.put("realname",realname);
		List<User> list = userService.getUserByParentIdRealName(canMap);
		if(list!=null &&list.size()>0){
			isrealName = "是";
		}
		return isrealName;
	}
	
	
	/*
	 * 常用手机号
	 * */
	public String getPhone(String currentUser,IUserService userService){
		String phone = "";
		Map canMap = new HashMap(1);
		canMap.put("parentId", currentUser);
		List<Map> list = userService.getPhones(canMap);
		for(int i=0;i<list.size();i++){
			if(list.get(i)!=null){
				Map map = list.get(i);
				String elePhone = (String)map.get("phone");
				phone+= elePhone+"  ";
			}
		}
		
		return phone;
	}
	/*
	 * 查找工作地
	 * */
	public String getWorkLocation(User user,IUserService userService){
		String location = "";

		String shebao = user.getShebaolocation();
		if(shebao!=null && shebao.contains("shanghai_shebao")){
			location = "上海";
		}else if(shebao!=null && shebao.contains("shenzhen_shebao")){
			location = "深圳";
		}else{
			location = "其他";
		}		
		return location;
	}
	
	/*
	 * 获得用户
	 * 学历：		研究生、普通本专科-----学信网
		职业：		IT从业者	---社保
		工作地：	北京朝阳区	-----社保
		职工性质：	IT		----社保
		婚姻状况：	已婚/未婚/离婚/再婚	---征信报告
	 * */
	
	public Map getSomeInfo(String currentUser,IUserService userService){
		Map map = new HashMap();
		Map canMap = new HashMap(1);
		canMap.put("parentId", currentUser);
		List<User> list = userService.getUserByParentId(canMap);
		for(int i=0;i<list.size();i++){
			User user = list.get(i);
			String usersource = user.getUsersource();		
			if(usersource.contains("XUEXIN")){
				map.put("xueli", user.getEduRecord());
				map.put("eduSchool", user.getEduSchool());
			}else if(usersource.contains("SHEBAO")){
				map.put("zhiye", user.getJobTitle());
				map.put("pUnit", user.getpUnit());
				map.put("worknature", user.getJobTitle());
			}else if(usersource.contains("ZHENGXIN")){
				map.put("marry", user.getMerry());
			}
		}
		
		
		return map;
	}
	
}
