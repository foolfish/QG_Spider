package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.MobileDetail;

public interface IMobileDetailService {
	MobileDetail findById(String id);
	
	void saveMobileDetail(MobileDetail mobileDetail);
	
	void deleteMobileDetail(String id);
	
	void  update(MobileDetail mobileDetail);
	
	List<Map> getMobileDetailBypt(Map map);
	List<Map> getMobileDetailForReport(Map map);
	List<Map> getMobileDetailForReport2(Map map);
	
	public MobileDetail getMaxTime(MobileDetail mobileDetail) ;

	List<Map> getMobileDetail(String phone);
	
	void insertbatch(List<MobileDetail> vctList);
}
