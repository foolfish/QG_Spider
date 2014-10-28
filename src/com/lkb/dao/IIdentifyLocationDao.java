package com.lkb.dao;

import com.lkb.bean.IdentifyLocation;



public interface IIdentifyLocationDao {
	
	IdentifyLocation findById(String id);
	
	void saveIdentifyLocation(IdentifyLocation identifyLocation);
	
	void deleteIdentifyLocation(String id);
	

	void  update(IdentifyLocation identifyLocation);
	

}
