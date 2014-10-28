package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.PhoneNum;
import com.lkb.dao.IPhoneNumDao;


@Service
public class PhoneNumDaoImp {

	@Resource
	private IPhoneNumDao phoneNumDao;
	
	public PhoneNum findById(String phoneNumId){
		return phoneNumDao.findById(phoneNumId);
	}
	
	public void savePhoneNum(PhoneNum phoneNum){
		phoneNumDao.savePhoneNum(phoneNum);
	}
	
	public void deletePhoneNum(String phoneNumId){
		phoneNumDao.deletePhoneNum(phoneNumId);
	}
	
	public List<PhoneNum> getPhoneNumByBaseUseridsource(Map map){
		return phoneNumDao.getPhoneNumByBaseUseridsource(map);
	}
		
	public void  update(PhoneNum phoneNum){
		phoneNumDao.update(phoneNum);
	}
	

}
