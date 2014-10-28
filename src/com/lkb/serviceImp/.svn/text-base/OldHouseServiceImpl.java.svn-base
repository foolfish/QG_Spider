package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.OldHouse;
import com.lkb.daoImp.OldHouseDaoImp;
import com.lkb.service.IOldHouseService;



@Service
@Transactional
public class OldHouseServiceImpl implements IOldHouseService {

	@Resource
	private OldHouseDaoImp oldHouseModel;
	
	@Override
	public OldHouse findById(int id) {
		
		OldHouse OldHouse = oldHouseModel.findById(id);
		return OldHouse; 
	}

	@Override
	public  void saveOldHouse(OldHouse oldHouse) {	
		try {
			oldHouseModel.saveOldHouse(oldHouse);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteOldHouse(int id) {	
		try {
			oldHouseModel.deleteOldHouse(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(OldHouse oldHouse){
		
		oldHouseModel.update(oldHouse);
	}
	@Override
	public List<Map> getAllCity(){		
		return oldHouseModel.getAllCity();
	}
}
