package com.lkb.thirdUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lkb.bean.User;
import com.lkb.service.IUserService;


/**
 * 信息完整度
 * */
public class Part {
	
	/*
	 * 进度条
	 * */
	public int jinduPart(){
		int part = 0;
		
		return part;
	}
	
	
	/*
	 * 
	 * 信息完整度
	 * jzr
	 * */
	
	public static int wanzhengPart(User user,IUserService userService){
		int all = 7;
		int part = 0;
//		Boolean phoneFlag = false;//手机号数据源
//		Boolean jingDongFlag = false;//京东数据源
//		Boolean taoBaoFlag = false;//淘宝数据源
//		Boolean xueXinFlag = false;//学信数据源
//		Boolean zhengXinFlag = false;//征信数据源
//		Boolean baseFlag = false;//个人信息数据源
//		Boolean shebaoFlag = false;//社保数据源
		Set set = new HashSet();
		Map map = new HashMap(3);
		map.put("parentId", user.getId());
		map.put("modifyDate", getQueryDate());
		map.put("loginName", user.getLoginName());
		List<User> sources = userService.getUserSourceByParentId(map);
		//用户基本信息
		//User user_base = (User)userService.checkLoginName(user.getLoginName()).get(0);
		
		User user_base = user;
		if (user_base.getRealName()!=null&&!user_base.getRealName().equals("")&&user_base.getIdcard()!=null&&!user_base.getIdcard().equals("")&&user_base.getEmail()!=null&&!user_base.equals("")) {
			//baseFlag= true;
			set.add("base");
		}
		for(int i=0;i<sources.size();i++){
			String source = sources.get(i).getUsersource(); 
			if(source.contains("DIANXIN") || source.contains("YIDONG") ||  source.contains("LIANTONG")){
//				phoneFlag = true;
				set.add("yunyingshang");
			}else if(source.contains("SHEBAO")){
//				shebaoFlag = true;
				set.add("shabao");
			}else if (source.contains("JD")) {
//				baseFlag= true;
				set.add("jd");
			}else if (source.contains("TAOBAO")) {
//				baseFlag= true;
				set.add("taobao");
			}else if (source.contains("XUEXIN")) {
//				baseFlag= true;
				set.add("xuexin");
			}else if (source.contains("ZHENGXIN")) {
//				baseFlag= true;
				set.add("zhengxin");
			}
		}
		int m  = set.size();
		part =  (m*100/all); 
		return part;
	}
	public static Map<String, String> getCheckedPart(User user,IUserService userService){
		Map<String, String> return_map = new HashMap<String, String>();
		String phoneFlag = "0";//手机号数据源
		String jingDongFlag = "0";//京东数据源
		String taoBaoFlag = "0";//淘宝数据源
		String xueXinFlag = "0";//学信数据源
		String zhengXinFlag = "0";//征信数据源
		String baseFlag = "0";//个人信息数据源
		String shebaoFlag = "0";//社保数据源
		Map map = new HashMap(3);
		map.put("parentId", user.getId());
		map.put("modifyDate", getQueryDate());
		map.put("loginName", user.getLoginName());
		List<User> sources = userService.getUserSourceByParentId(map);
		//用户基本信息
		User user_base = (User)userService.checkLoginName(user.getLoginName()).get(0);
		if (user_base.getRealName()!=null&&!user_base.getRealName().equals("")&&user_base.getIdcard()!=null&&!user_base.getIdcard().equals("")&&user_base.getEmail()!=null&&!user_base.equals("")) {
			baseFlag="1";
		}
		for(int i=0;i<sources.size();i++){
			String source = sources.get(i).getUsersource(); 
			if(source.contains("DIANXIN") || source.contains("YIDONG") ||  source.contains("LIANTONG")){
				phoneFlag = "1";
			}else if(source.contains("SHEBAO")){
				shebaoFlag = "1";
			}else if (source.contains("JD")) {
				jingDongFlag= "1";
			}else if (source.contains("TAOBAO")) {
				taoBaoFlag= "1";
			}else if (source.contains("XUEXIN")) {
				xueXinFlag= "1";
			}else if (source.contains("ZHENGXIN")) {
				zhengXinFlag= "1";
			}
		}
		return_map.put("phoneFlag", phoneFlag);
		return_map.put("shebaoFlag", shebaoFlag);
		return_map.put("jingDongFlag", jingDongFlag);
		return_map.put("taoBaoFlag", taoBaoFlag);
		return_map.put("xueXinFlag", xueXinFlag);
		return_map.put("zhengXinFlag", zhengXinFlag);
		return_map.put("baseFlag", baseFlag);
		return return_map;
	}
	private static Date getQueryDate(){
		Calendar c = Calendar.getInstance();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		//System.out.println(sdf.format(c.getTime()));
		return c.getTime();
	}
	public static void main(String[] args){
		int m=5;
		int all=7;
		int part = (m*100/all); 
		System.out.println(part);
	
	}

}
