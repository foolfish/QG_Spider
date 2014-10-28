package com.lkb.service;


import java.util.List;
import java.util.Map;

import com.lkb.bean.Parse;


public interface IParseService {
	
	Parse get(Long id);

	
	void save(Parse parse);
	
	void update(Parse parse);
	
	List<Parse> getParseBySome(Map map);
	
	 Map getStatus(Map map);
	
}
