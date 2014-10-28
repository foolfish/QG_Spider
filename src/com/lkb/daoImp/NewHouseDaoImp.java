package com.lkb.daoImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.NewHouse;
import com.lkb.dao.INewHouseDao;



@Service
public class NewHouseDaoImp {

	@Resource
	private INewHouseDao newHouseDao;
	
	public NewHouse findById(int id){
		return newHouseDao.findById(id);
	}
		
	public void saveNewHouse(NewHouse newHouse){
		newHouseDao.saveNewHouse(newHouse);
	}
	
	public void deleteNewHouse(int id){
		newHouseDao.deleteNewHouse(id);
	}
	
	
	public void  update(NewHouse newHouse){
		
		newHouseDao.update(newHouse);
	}
	
	public List<Map>  getAllCity(){		
		return newHouseDao.getAllCity();
	}

	

}
