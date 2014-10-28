package com.lkb.service;


import java.util.List;
import java.util.Map;

import com.lkb.bean.TelcomMessage;

public interface ITelcomMessageService {

	TelcomMessage findById(String id);

	void save(TelcomMessage obj);

	void delete(String id);

	void  update(TelcomMessage obj);

	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getByPhone(Map map);
	List<Map> getListByPhone(String phone);
	List<Map> getTelcomMessageForReport(Map map);
	List<Map> getTelcomMessageForReport2(Map map);

	TelcomMessage getMaxSentTime(String phone);
	List<Map> getList(String phone);

	void insertbatch(List<TelcomMessage> vctList);
	
}
