package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinDetail;

public interface IDianXinDetailDao {
	
	DianXinDetail findById(String id);
	
	void saveDianXinDetail(DianXinDetail dianxinDetail);
	
	void deleteDianXinDetail(String id);
	
	void  update(DianXinDetail dianxinDetail);
	
	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getDianXinDetailBypt(Map map);
	List<Map> getDianXinDetailForReport(Map map);
	List<Map> getDianXinDetailForReport2(Map map);
	
	DianXinDetail getMaxCallTime(String phone);
	List<Map> getDianXinDetail(String phone);
	void insertbatch(List<DianXinDetail> list);
}
