package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.NewHouse;

public interface INewHouseDao {
	
	NewHouse findById(int id);
	
	void saveNewHouse(NewHouse newHouse);
	
	void deleteNewHouse(int id);
	
	void  update(NewHouse newHouse);
	
	List<Map> getAllCity();
}
