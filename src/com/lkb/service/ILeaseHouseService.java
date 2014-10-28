package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.LeaseHouse;

public interface ILeaseHouseService {
	LeaseHouse findById(int id);
	
	void saveLeaseHouse(LeaseHouse leaseHouse);
	
	void deleteLeaseHouse(int id);
	
	void  update(LeaseHouse leaseHouse);
	
	List<Map> getAllCity();
	
}
