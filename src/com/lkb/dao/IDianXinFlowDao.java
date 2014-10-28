package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlow;

/**
* <p>Title: IDianXinFlowDao</p>
* <p>Description: 电信流量账单dao</p>
* <p>Company: QuantGroup</p> 
* @author Jerry Sun
* @date 2014-10-8
*/
public interface IDianXinFlowDao {
	
	DianXinFlow findById(String id);
	
	void saveDianXinFlow(DianXinFlow dianXinFlow);
	
	void deleteDianXinFlow(String id);
	
	void update(DianXinFlow dianXinFlow);
	
	
	
	/*
	 * 根据手机号和时间来确定一条记录
	 * */
	List<Map> getDianXinFlowBybc(Map map);
	List<Map> getFormatQueryTime(String phone);
	List<Map> getDianXinFlowForReport(String phone);
	
	/*List<Map> getEveryAmount(Map map);
	List<Map> getDianXinFlowForReport1(Map map);*/
	
	public DianXinFlow getMaxAccountTime(String phone);
	public List getMaxNumTel(Map<String,Object> map);
	/*
	 *批量插入
	 * */
	void insertbatch(List<DianXinFlow> list);
	
}
