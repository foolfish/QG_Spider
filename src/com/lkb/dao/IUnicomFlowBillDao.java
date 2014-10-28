package com.lkb.dao;


import java.util.List;
import java.util.Map;
import com.lkb.bean.UnicomFlowBill;

public interface IUnicomFlowBillDao {
	UnicomFlowBill findById(String id);
	
	void saveUnicomFlowBill(UnicomFlowBill unicomFlowBill);
	
	void deleteUnicomFlowBill(String id);
	
	void  update(UnicomFlowBill unicomFlowBill);
	List<UnicomFlowBill> getUnicomFlowBillBybc(Map map);
//	
	List<Map> getEveryAmount(Map map);
	List<Map> getFormatCtime(String phone);
	List<Map> getUnicomFlowBillForReport1(Map map);
	List<Map> getUnicomFlowBillForReport(String teleno);
	
	public UnicomFlowBill getMaxAccountTime(UnicomFlowBill unicomFlowBill);
	
	List getMaxNumTel(Map map);
	/*
	 *批量插入
	 * */
	void insertbatch(List<UnicomFlowBill> list);
}