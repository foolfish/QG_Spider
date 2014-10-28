package com.lkb.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;

public interface IDianXinFlowService {
	DianXinFlow findById(String id);
	
	void saveDianXinFlow(DianXinFlow dianXinFlow);
	
	void deleteDianXinFlow(String id);
	
	
	void  update(DianXinFlow dianXinFlow);
	List<Map> getDianXinFlowBybc(Map map);
	List<Map> getFormatQueryTime(String phone);
/*	List<Map> getDianXinFlowForReport1(Map map);
	List<Map> getEveryAmount(Map map);*/
	List<Map> getDianXinFlowForReport(String phone);
	public DianXinFlow getMaxFlowTime(String phone);
	/***
	 * 获取最大时间
	 * 参数  teleno 电话号
	 * 参数 num  最近几个月
	 * @param map
	 * @return
	 */
	public List getMaxNumTel(Map<String,Object> map);
	void insertbatch(List<DianXinFlow> list);
	

}
