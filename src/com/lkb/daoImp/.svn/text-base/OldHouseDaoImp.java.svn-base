package com.lkb.daoImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.OldHouse;
import com.lkb.dao.IOldHouseDao;



@Service
public class OldHouseDaoImp {

	@Resource
	private IOldHouseDao oldHouseDao;
	
	public OldHouse findById(int id){
		return oldHouseDao.findById(id);
	}
		
	public void saveOldHouse(OldHouse oldHouse){
		oldHouseDao.saveOldHouse(oldHouse);
	}
	
	public void deleteOldHouse(int id){
		oldHouseDao.deleteOldHouse(id);
	}
	
	
	public void  update(OldHouse oldHouse){
		
		oldHouseDao.update(oldHouse);
	}

	public List<Map> getAllCity(){
		
		return oldHouseDao.getAllCity();
	}
	

}
