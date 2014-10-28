package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.Warning;
import com.lkb.dao.IWarningDao;


@Service
public class WarningDaoImp {

	@Resource
	private IWarningDao warningDao;
	
	public Warning findById(String warningId){
		return warningDao.findById(warningId);
	}
	
	public void saveWarning(Warning warning){
		warningDao.saveWarning(warning);
	}
	
	public void deleteWarning(String warningId){
		warningDao.deleteWarning(warningId);
	}
	
	public List<Warning> getWarningByType(Map map){
		return warningDao.getWarningByType(map);
	}
	public List<Warning> getWarningByFlag(Map map){
		return warningDao.getWarningByFlag(map);
	}
		
	public void  update(Warning warning){
		warningDao.update(warning);
	}
	

}
