package com.lkb.daoImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.LeaseHouse;
import com.lkb.dao.ILeaseHouseDao;



@Service
public class LeaseHouseDaoImp {

	@Resource
	private ILeaseHouseDao leaseHouseDao;
	
	public LeaseHouse findById(int id){
		return leaseHouseDao.findById(id);
	}
		
	public void saveLeaseHouse(LeaseHouse leaseHouse){
		leaseHouseDao.saveLeaseHouse(leaseHouse);
	}
	
	public void deleteLeaseHouse(int id){
		leaseHouseDao.deleteLeaseHouse(id);
	}
	
	
	public void  update(LeaseHouse leaseHouse){
		
		leaseHouseDao.update(leaseHouse);
	}
	
	public List<Map> getAllCity(){
		return leaseHouseDao.getAllCity();
	}


}
