package com.lkb.service;


import java.util.List;
import java.util.Map;

import com.lkb.bean.SheBao;


public interface ISheBaoService {
	
	SheBao findById(String id);
	
	void saveSheBao(SheBao shebao);
	
	void deleteSheBao(String id);
	
	List<SheBao> getSheBaoByBaseUseridsource(Map map);
	
	void  update(SheBao shebao);
	
	List<Map> getAmountCount(Map map);
	List<Map> getSheBaoForReport1(Map map);
	List<Map> getSheBaoForReport2(Map map);
	
	List<Map> getEveryAmount(Map map);
	 
	List<Map> getCount(Map map);
			
	List<Map> getRecentCompany(Map map);
	
	 List<Map> getRecentPayFeedBase(Map map);
		
}
