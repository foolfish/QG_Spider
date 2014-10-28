package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.University;
import com.lkb.dao.IUniversityDao;


@Service
public class UniversityDaoImp {

	@Resource
	private IUniversityDao universityDao;
	
	/**返回所有**/
	public List<University> getAll(){
		return universityDao.getAll();
	}
	/**返回当前**/
	public University get(Integer id){
		return universityDao.get(id);
	}
	/***
	 * @param type=true 查询所有285院校
	 * @return all;
	 */
	public List<University> getObjByType(Boolean type){
		return universityDao.getObjByType(type);
	}
	
	public void save(University university){
		universityDao.save(university);
	}
	
	
	public int getObjByName(Map map){
		return universityDao.getObjByName(map);
	}
	
	
}
