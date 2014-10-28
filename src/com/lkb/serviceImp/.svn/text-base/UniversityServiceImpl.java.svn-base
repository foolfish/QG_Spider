package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.University;
import com.lkb.daoImp.UniversityDaoImp;
import com.lkb.service.IUniversityService;

@Service
@Transactional
public class UniversityServiceImpl implements IUniversityService {

	@Resource
	private UniversityDaoImp universityDaoModel;
	
	/**返回所有**/
	public List<University> getAll(){
		return universityDaoModel.getAll();
	}
	/**返回当前**/
	public University get(Integer id){
		return universityDaoModel.get(id);
	}
	/***
	 * @param type=true 查询所有285院校
	 * @return all;
	 */
	public List<University> getObjByType(Boolean type){
		return universityDaoModel.getObjByType(type);
	}
	
	public void save(University university){
		universityDaoModel.save(university);
	}
	
	public int getObjByName(Map map){
		return universityDaoModel.getObjByName(map);
	}
	
	
}
