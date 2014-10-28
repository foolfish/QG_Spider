package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.IdentifyLocation;
import com.lkb.dao.IIdentifyLocationDao;


@Service
public class IdentifyLocationDaoImp {

	@Resource
	private IIdentifyLocationDao identifyLocationDao;
	
	public IdentifyLocation findById(String identifyLocationId){
		return identifyLocationDao.findById(identifyLocationId);
	}
	
	public void saveIdentifyLocation(IdentifyLocation identifyLocation){
		identifyLocationDao.saveIdentifyLocation(identifyLocation);
	}
	
	public void deleteIdentifyLocation(String identifyLocationId){
		identifyLocationDao.deleteIdentifyLocation(identifyLocationId);
	}

	public void  update(IdentifyLocation identifyLocation){
		identifyLocationDao.update(identifyLocation);
	}
	

}
