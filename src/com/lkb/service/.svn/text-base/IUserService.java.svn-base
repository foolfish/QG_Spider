package com.lkb.service;


import java.util.List;
import java.util.Map;

import com.lkb.bean.User;

public interface IUserService {
	
	 User findById(String userId);

	void saveUser(User user);
	
	void deleteUser(String userId);
	
	List<User> getUserByUserNamesource(Map map);
	
	List<User> getUserByPhone(Map map);
	
	List<User> getUserByParentId(Map map);
	List<User> getUserSourceByParentId(Map map);//JIANG ADD
	List<User> getUserByCardNoAndSource(Map map);
	List<User> getUserByParentIdSource(Map map);
	List<User> getUserByParentIdSource2(Map map);
	
	void  update(User user);
	
	List<User> checkLoginName(String userName);
	List<Map> getPhoneInfoForReport(Map map);

	List<User> checkLogin(Map map);
	
	List<User> checkParse(Map map);
	
	List<User> getUserByParentIdRealName(Map map);
	
	List<Map> getPhones(Map map);
	
	int getUsercount(Map map);
	
	String getUserIdFromReportId(String markId);
	
}
