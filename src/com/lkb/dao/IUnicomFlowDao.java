package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.UnicomFlow;

public interface IUnicomFlowDao {
	
	UnicomFlow findById(String id);
	
	void save(UnicomFlow obj);
	
	void delete(String id);
	
	void  update(UnicomFlow obj);
	
	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getByPhone(Map map);
	List<Map> getListByPhone(String phone);
	List<Map> getUnicomFlowForReport(String phone);
	List<Map> getUnicomFlowForReport2(Map map);
	
	UnicomFlow getMaxStartTime(String phone);
	List<Map> getList(String phone);
	
	/*
	 *批量插入
	 * */
	void insertbatch(List<UnicomFlow> list);
}
