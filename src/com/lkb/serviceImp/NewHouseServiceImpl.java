package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.NewHouse;
import com.lkb.daoImp.NewHouseDaoImp;
import com.lkb.service.INewHouseService;



@Service
@Transactional
public class NewHouseServiceImpl implements INewHouseService {

	@Resource
	private NewHouseDaoImp newHouseModel;
	
	@Override
	public NewHouse findById(int id) {
		
		NewHouse NewHouse = newHouseModel.findById(id);
		return NewHouse; 
	}

	@Override
	public  void saveNewHouse(NewHouse newHouse) {	
		try {
			newHouseModel.saveNewHouse(newHouse);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteNewHouse(int id) {	
		try {
			newHouseModel.deleteNewHouse(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(NewHouse newHouse){
		
		newHouseModel.update(newHouse);
	}
	
	@Override
	public List<Map>  getAllCity(){		
		return newHouseModel.getAllCity();
	}
	
}
