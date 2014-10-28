package com.lkb.service;


import java.util.List;
import java.util.Map;

import com.lkb.bean.Warning;


public interface IWarningService {
	
	Warning findById(String id);
	
	void saveWarning(Warning warning);
	
	void deleteWarning(String id);
	
	List<Warning> getWarningByType(Map map);
	
	void  update(Warning warning);

	List<Warning> getWarningByFlag(Map map);

}
