package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.IdentifyLocation;
import com.lkb.daoImp.IdentifyLocationDaoImp;
import com.lkb.service.IIdentifyLocationService;

@Service
@Transactional
public class IdentifyLocationServiceImpl implements IIdentifyLocationService {

	@Resource
	private IdentifyLocationDaoImp identifyLocationModel;
	
	@Override
	public IdentifyLocation findById(String identifyLocationId) {
		
		IdentifyLocation identifyLocation = identifyLocationModel.findById(identifyLocationId);
		return identifyLocation;
	}

	@Override
	public  void saveIdentifyLocation(IdentifyLocation identifyLocation) {	
		try {
			identifyLocationModel.saveIdentifyLocation(identifyLocation);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteIdentifyLocation(String identifyLocationId) {	
		try {
			identifyLocationModel.deleteIdentifyLocation(identifyLocationId);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	
	@Override
	public void  update(IdentifyLocation identifyLocation){
		identifyLocationModel.update(identifyLocation);
	}
	


}
