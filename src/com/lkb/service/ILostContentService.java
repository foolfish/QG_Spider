package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.LoseContent;

public interface ILostContentService {
	LoseContent findById(String id);
	
	void save(LoseContent loseUrl);
	
	void delete(LoseContent loseUrl);
	
	void  update(LoseContent loseUrl);
	
	 List<Map> getAll(Map map);
	 void insertbatch(List<LoseContent> list);
	 
//	 void deletebatch(List<LoseContent> list);
	 
}
