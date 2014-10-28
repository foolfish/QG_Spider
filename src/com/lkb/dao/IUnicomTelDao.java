package com.lkb.dao;


import java.util.List;
import java.util.Map;

import com.lkb.bean.UnicomTel;

public interface IUnicomTelDao {
	UnicomTel findById(String id);
	
	void saveUnicomTel(UnicomTel unicomTel);
	
	void deleteUnicomTel(String id);
	
	void  update(UnicomTel unicomTel);
	List<UnicomTel> getUnicomTelBybc(Map map);
//	
	List<Map> getEveryAmount(Map map);
	List<Map> getFormatCtime(String phone);
	List<Map> getUnicomTelForReport1(Map map);
	/*
	 *批量插入
	 * */
	void insertbatch(List<UnicomTel> list);
}
