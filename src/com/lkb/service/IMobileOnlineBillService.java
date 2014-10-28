package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileOnlineBill;

public interface IMobileOnlineBillService {

	MobileOnlineBill findById(String id);

	void save(MobileOnlineBill obj);

	void delete(String id);

	void update(MobileOnlineBill obj);

	List<Map> getMobileOnlineBillByphone(Map map);
	List<Map> getMobileOnlineBill(Map map);
	List<Map> getMobileOnlineBillForReport(String phone);
	
	MobileOnlineBill getMaxTime(String phone);
	List getMaxNumTel(Map map);

	/*
	 * 批量插入
	 */
	void insertbatch(List<MobileOnlineBill> list);
}
