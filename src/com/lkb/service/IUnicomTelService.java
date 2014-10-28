package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.UnicomDetail;
import com.lkb.bean.UnicomTel;


public interface IUnicomTelService {
	UnicomTel findById(String id);
	
	void saveUnicomTel(UnicomTel unicomTel);
	
	void deleteUnicomTel(String id);
	
	void  update(UnicomTel unicomTel);
	
	List<UnicomTel> getUnicomTelBybc(Map map);
	List<Map> getUnicomTelForReport1(Map map);
	List<Map> getEveryAmount(Map map);
	List<Map> getFormatCtime(String phone);
	void insertbatch(List<UnicomTel> list);
}
