package com.lkb.dao;

import java.util.List;
import java.util.Map;


import com.lkb.bean.OTCity;




public interface IOTCityDao {
	
	OTCity get(Integer id);
	/**
	 *返回所有 
	 * @return
	 */
	List<OTCity> getAll();
	/***
	 * 根据otCity的type取得结果集,默认返回所有
	 * @param type
	 * @return all;
	 */
	List<OTCity> getObjByType(Integer type);
	
	void save(OTCity otCity);
	
//	void delete(Integer id);
	
//	void  update(OTCity otCity);
	
	List<OTCity>  getObjByName(Map map);
	
	
}
