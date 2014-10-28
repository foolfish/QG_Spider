package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.User;
import com.lkb.dao.IUserDao;

@Service
public class UserDaoImp {

	@Resource
	private IUserDao userDao;
	
	public User findById(String userId){
		return userDao.findById(userId);
	}
	
	
	public void saveUser(User user){
		userDao.saveUser(user);
	}
	
	public void deleteUser(String userId){
		userDao.deleteUser(userId);
	}
	
	public List<User> getUserByUserNamesource(Map map){
		return userDao.getUserByUserNamesource(map);
	}
	
	public List<User> getUserByCardNoAndSource(Map map){
		return userDao.getUserByCardNoAndSource(map);
	}
	
	public void  update(User user){
		userDao.update(user);
	}
	
	public List<User> getUserByPhone(Map map){
		return userDao.getUserByPhone(map);
	}
	
	public List<User> getUserByParentId(Map map){
		return userDao.getUserByParentId(map);
	}
	public List<User> getUserSourceByParentId(Map map){
		return userDao.getUserSourceByParentId(map);
	}
	
	public List<User> getUserByParentIdSource(Map map){
		return userDao.getUserByParentIdSource(map);
	}
	public List<User> getUserByParentIdSource2(Map map){
		return userDao.getUserByParentIdSource2(map);
	}
	
	public List<User> checkLoginName(String userName){
		return userDao.checkLoginName(userName);
	}
	public List<Map> getPhoneInfoForReport(Map map){
		return userDao.getPhoneInfoForReport(map);
	}
	
	
	public List<User> checkLogin(Map map){
		return userDao.checkLogin(map);
	}

	public List<User> checkParse(Map map){
		return userDao.checkParse(map);
	}
	
	public List<User> getUserByParentIdRealName(Map map){
		return userDao.getUserByParentIdRealName(map);
	}
	
	public List<Map> getPhones(Map map) {		
		return userDao.getPhones(map);
	}
	
	public int getUsercount(Map map){		
		return userDao.getUsercount(map);
	}
		
	public String getUserIdFromReportId(String markId) {		
		return userDao.getUserIdFromReportId(markId);
	}
	
}
