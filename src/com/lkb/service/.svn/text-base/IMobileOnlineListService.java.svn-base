package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileOnlineList;

public interface IMobileOnlineListService {

	MobileOnlineList findById(String id);
	
	void save(MobileOnlineList obj);
	
	void delete(String id);
	
	void update(MobileOnlineList obj);
	
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
