package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.NewHouse;

public interface INewHouseService {
	NewHouse findById(int id);
	
	void saveNewHouse(NewHouse newHouse);
	
	void deleteNewHouse(int id);
	
	void  update(NewHouse newHouse);
	
	List<Map>  getAllCity();
}
