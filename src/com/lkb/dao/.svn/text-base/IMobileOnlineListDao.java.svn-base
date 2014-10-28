package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileOnlineList;

public interface IMobileOnlineListDao {

	MobileOnlineList findById(String id);
	
	void save(MobileOnlineList obj);
	
	void delete(String id);
	
	void update(MobileOnlineList obj);
	
	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getMobileOnlineListBypt(Map map);
	List<Map> getMobileOnlineListForReport(String phone);
	List<Map> getMobileOnlineListForReport2(Map map);
	
	MobileOnlineList getMaxTime(String phone);
	
	List<Map> getMobileOnlineList(String phone);
	
	
	/*
	 *批量插入
	 * */
	void insertbatch(List<MobileOnlineList> list);
	
}
