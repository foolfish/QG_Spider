package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinDetail;

public interface IDianXinDetailService {
	DianXinDetail findById(String id);
	
	void saveDianXinDetail(DianXinDetail dianxinDetail);
	
	void deleteDianXinDetail(String id);
	
	void  update(DianXinDetail dianxinDetail);
	
	List<Map> getDianXinDetailBypt(Map map);
	List<Map> getDianXinDetailForReport(Map map);
	List<Map> getDianXinDetailForReport2(Map map);
	
	 DianXinDetail getMaxTime(DianXinDetail dianxinDetail) ;
	 List<Map> getDianXinDetail(String phone);
	 void insertbatch(List<DianXinDetail> list);
}
