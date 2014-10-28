package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.lkb.bean.User;
import com.lkb.daoImp.UserDaoImp;
import com.lkb.service.IUserService;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

	@Resource
	private UserDaoImp userModel;
	@Override
	public List<User> getUserByCardNoAndSource(Map map) {
		return userModel.getUserByCardNoAndSource(map);
		
	}
	@Override
	public User findById(String userId) {
		
		User user = userModel.findById(userId);
		return user;
	}

	@Override
	public  void saveUser(User user) {	
		try {
			userModel.saveUser(user);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteUser(String userId) {	
		try {
			userModel.deleteUser(userId);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public List<User> getUserByUserNamesource(Map map){
		
		List<User> list = userModel.getUserByUserNamesource(map);		
		
		return list;
		
	}
	
	@Override
	public void  update(User user){
		userModel.update(user);
	}

	@Override
	public List<User> getUserByPhone(Map map) {
		List<User> list = userModel.getUserByPhone(map);				
		return list;
	}
	
	@Override	
	public List<User> getUserByParentId(Map map){
		List<User> list = userModel.getUserByParentId(map);		
		return list;
	}
	
	@Override	
	public List<User> getUserByParentIdSource(Map map){
		List<User> list = userModel.getUserByParentIdSource(map);		
		return list;
	}
	@Override	
	public List<User> getUserByParentIdSource2(Map map){
		List<User> list = userModel.getUserByParentIdSource2(map);		
		return list;
	}
	@Override	
	public List<User> checkLoginName(String userName){
		List<User> list = userModel.checkLoginName(userName);		
		return list;
	}
	@Override	
	public List<Map> getPhoneInfoForReport(Map map){
		List<Map> list = userModel.getPhoneInfoForReport(map);		
		return list;
	}

	@Override	
	public List<User> checkLogin(Map map){
		List<User> list = userModel.checkLogin(map);		
		return list;
	}
	
	@Override	
	public List<User> checkParse(Map map){
		List<User> list = userModel.checkParse(map);		
		return list;
	}
	

	@Override	
	public List<User> getUserByParentIdRealName(Map map){
		return userModel.getUserByParentIdRealName(map);
	}
	@Override
	public List<Map> getPhones(Map map) {
		return userModel.getPhones(map);
	}
	@Override
	public int getUsercount(Map map){		
		return userModel.getUsercount(map);
	}
	@Override
	public String getUserIdFromReportId(String markId){
		return userModel.getUserIdFromReportId(markId);	
	}
	@Override
	public List<User> getUserSourceByParentId(Map map) {
		List<User> list = userModel.getUserSourceByParentId(map);		
		return list;
	}
	
}
