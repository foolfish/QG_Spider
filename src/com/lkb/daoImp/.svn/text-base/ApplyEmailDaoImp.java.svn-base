package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.ApplyEmail;
import com.lkb.bean.OTCity;
import com.lkb.dao.IApplyEmailDao;
import com.lkb.dao.IOTCityDao;


@Service
public class ApplyEmailDaoImp {

	@Resource
	private IApplyEmailDao applyEmailDao;
	
	public ApplyEmail get(Long id){
		return applyEmailDao.get(id);
	}
	/**
	 *返回所有 
	 * @return
	 */
	public List<ApplyEmail> getAll(){
		return applyEmailDao.getAll();
	}

	
	public void save(ApplyEmail applyEmail){
		applyEmailDao.save(applyEmail);
	}
	
	public void update(ApplyEmail applyEmail){
		applyEmailDao.update(applyEmail);
	}
	
	public List<ApplyEmail> getApplyEmailEt(Map map){
		return applyEmailDao.getApplyEmailEt(map);
	}
}
