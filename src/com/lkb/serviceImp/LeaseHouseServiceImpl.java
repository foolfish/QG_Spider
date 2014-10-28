package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.LeaseHouse;
import com.lkb.daoImp.LeaseHouseDaoImp;
import com.lkb.service.ILeaseHouseService;



@Service
@Transactional
public class LeaseHouseServiceImpl implements ILeaseHouseService {

	@Resource
	private LeaseHouseDaoImp leaseHouseModel;
	
	@Override
	public LeaseHouse findById(int id) {
		
		LeaseHouse LeaseHouse = leaseHouseModel.findById(id);
		return LeaseHouse; 
	}

	@Override
	public  void saveLeaseHouse(LeaseHouse leaseHouse) {	
		try {
			leaseHouseModel.saveLeaseHouse(leaseHouse);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteLeaseHouse(int id) {	
		try {
			leaseHouseModel.deleteLeaseHouse(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(LeaseHouse leaseHouse){
		
		leaseHouseModel.update(leaseHouse);
	}
	
	@Override
	public   List<Map> getAllCity(){
		
		return leaseHouseModel.getAllCity();
	}
	
	
}
