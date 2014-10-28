package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.User;

public interface IUserDao {
	
	User findById(String id);
	
	void saveUser(User user);
	
	void deleteUser(String id);
	
	List<User> getUserByUserNamesource(Map map);
	
	List<User> getUserByPhone(Map map);
	
	List<User> getUserByParentId(Map map);
	List<User> getUserSourceByParentId(Map map);
	List<User> getUserByCardNoAndSource(Map map);
	List<User> getUserByParentIdSource(Map map);
	List<User> getUserByParentIdSource2(Map map);
	void  update(User user);
	
	List<User> checkLoginName(String userName);
	List<Map> getPhoneInfoForReport(Map map);
	
	List<User> checkLogin(Map map);
	
	/*
	 * 查找一个月内用户登陆过的网站源
	 * */
	List<User> checkParse(Map map);
	
	/*
	 * 得到user数据，根据父Id和真实姓名，判断是否验证
	 * */
	List<User> getUserByParentIdRealName(Map map);
	
	/*
	 * 获取常用手机号
	 * */
	List<Map> getPhones(Map map);
	
	/*
	 * 计算用户已经申请的来源
	 * */
	int getUsercount(Map map);
	
	String getUserIdFromReportId(String markId);
}
