package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileDetail;

public interface IMobileDetailDao {
	
	MobileDetail findById(String id);
	
	void saveMobileDetail(MobileDetail mobileTel);
	
	void deleteMobileDetail(String id);
	
	void  update(MobileDetail mobileDetail);
	
	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getMobileDetailBypt(Map map);
	List<Map> getMobileDetailForReport(Map map);
	List<Map> getMobileDetailForReport2(Map map);
	
	MobileDetail getMaxTime(String phone);
	
	List<Map> getMobileDetail(String phone);
	void insertbatch(List<MobileDetail> vctList);
}
