package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileMessage;
import com.lkb.bean.TelcomMessage;

public interface IMobileMessageDao {
	
	MobileMessage findById(String id);
	
	void save(MobileMessage obj);
	
	void delete(String id);
	
	void  update(MobileMessage obj);
	
	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getByPhone(Map map);
	List<Map> getListByPhone(String phone);
	List<Map> getMobileMessageForReport(Map map);
	List<Map> getMobileMessageForReport2(Map map);
	
	MobileMessage getMaxSentTime(String phone);
	List<Map> getList(String phone);
	
	/*
	 *批量插入
	 * */
	void insertbatch(List<MobileMessage> list);
}
