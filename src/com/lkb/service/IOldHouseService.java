package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.OldHouse;

public interface IOldHouseService {
	OldHouse findById(int id);
	
	void saveOldHouse(OldHouse oldHouse);
	
	void deleteOldHouse(int id);
	
	void  update(OldHouse oldHouse);
	
	List<Map> getAllCity();
}
