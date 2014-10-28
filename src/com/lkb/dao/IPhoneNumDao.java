package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.PhoneNum;



public interface IPhoneNumDao {
	
	PhoneNum findById(String id);
	
	void savePhoneNum(PhoneNum yuEBao);
	
	void deletePhoneNum(String id);
	
	List<PhoneNum> getPhoneNumByBaseUseridsource(Map map);
	
	void  update(PhoneNum yuEBao);
	

}
