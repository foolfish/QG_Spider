package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.UnicomMessage;

public interface IUnicomMessageDao {
	
	UnicomMessage findById(String id);
	
	void save(UnicomMessage obj);
	
	void delete(String id);
	
	void  update(UnicomMessage obj);
	
	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getByPhone(Map map);
	List<Map> getListByPhone(String phone);
	List<Map> getUnicomMessageForReport(Map map);
	List<Map> getUnicomMessageForReport2(Map map);
	
	UnicomMessage getMaxSentTime(String phone);
	List<Map> getList(String phone);
	
	/*
	 *批量插入
	 * */
	void insertbatch(List<UnicomMessage> list);
}
