package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.UnicomDetail;

public interface IUnicomDetailDao {
	
	UnicomDetail findById(String id);
	
	void saveUnicomDetail(UnicomDetail mobileTel);
	
	void deleteUnicomDetail(String id);
	
	void  update(UnicomDetail unicomDetail);
	
	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getUnicomDetailBypt(Map map);
	List<Map> getUnicomDetailForReport(Map map);
	List<Map> getUnicomDetailForReport2(Map map);
	List<Map> getUnicomDetail(String phone);
	/*
	 *批量插入
	 * */
	UnicomDetail getMaxTime(String phone);
	void insertbatch(List<UnicomDetail> list);
}
