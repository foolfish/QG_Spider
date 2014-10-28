package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.OTCity;
import com.lkb.daoImp.OTCityDaoImp;
import com.lkb.service.IOTCityService;

@Service
@Transactional
public class OTCityServiceImpl implements IOTCityService {

	@Resource
	private OTCityDaoImp otcityModel;
	
	public OTCity get(Integer id){
		return otcityModel.get(id);
	}
	/**
	 *返回所有 
	 * @return
	 */
	public List<OTCity> getAll(){
		return otcityModel.getAll();
	}
	/***
	 * 根据otCity的type取得结果集,默认返回所有
	 * @param type
	 * @return all;
	 */
	public List<OTCity> getObjByType(Integer type){
		return otcityModel.getObjByType(type);
	}
	
	public void save(OTCity otCity){
		otcityModel.save(otCity);
	}
	
	public List<OTCity> getObjByName(Map map){
		return otcityModel.getObjByName(map);
	}
	
	

}
