package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.OTCity;
import com.lkb.dao.IOTCityDao;


@Service
public class OTCityDaoImp {

	@Resource
	private IOTCityDao otcDao;
	
	public OTCity get(Integer id){
		return otcDao.get(id);
	}
	/**
	 *返回所有 
	 * @return
	 */
	public List<OTCity> getAll(){
		return otcDao.getAll();
	}
	/***
	 * 根据otCity的type取得结果集,默认返回所有
	 * @param type
	 * @return all;
	 */
	public List<OTCity> getObjByType(Integer type){
		return otcDao.getObjByType(type);
	}
	
	public void save(OTCity otCity){
		otcDao.save(otCity);
	}
	
	public List<OTCity> getObjByName(Map map){
		return otcDao.getObjByName(map);
	}
	
}
