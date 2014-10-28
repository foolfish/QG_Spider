package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileTel;
import com.lkb.bean.UnicomDetail;

public interface IUnicomDetailService {
	UnicomDetail findById(String id);
	
	void saveUnicomDetail(UnicomDetail unicomDetail);
	
	void deleteUnicomDetail(String id);
	
	void  update(UnicomDetail unicomDetail);
	
	public UnicomDetail getMaxTime(UnicomDetail unicomDetail);
	List<Map> getUnicomDetailBypt(Map map);
	List<Map> getUnicomDetailForReport(Map map);
	List<Map> getUnicomDetailForReport2(Map map);
	List<Map> getUnicomDetail(String phone);
	void insertbatch(List<UnicomDetail> list);
}
