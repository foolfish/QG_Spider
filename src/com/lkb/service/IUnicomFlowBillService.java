package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileTel;
import com.lkb.bean.UnicomFlowBill;


public interface IUnicomFlowBillService {
	UnicomFlowBill findById(String id);
	
	void saveUnicomFlowBill(UnicomFlowBill unicomFlowBill);
	
	void deleteUnicomFlowBill(String id);
	
	void  update(UnicomFlowBill unicomFlowBill);
	
	List<UnicomFlowBill> getUnicomFlowBillBybc(Map map);
	List<Map> getUnicomFlowBillForReport1(Map map);
	List<Map> getUnicomFlowBillForReport(String teleno);
	List<Map> getEveryAmount(Map map);
	List<Map> getFormatCtime(String teleno);
	void insertbatch(List<UnicomFlowBill> list);
	List getMaxNumTel(Map map);
	
	public UnicomFlowBill getMaxTime(UnicomFlowBill unicomflowbill);
}