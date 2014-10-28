package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.UnicomFlow;
import com.lkb.dao.IUnicomFlowDao;

@Service
public class UnicomFlowDaoImp {

	@Resource
	private IUnicomFlowDao dao;
	
	public UnicomFlow findById(String userId){
		return dao.findById(userId);
	}
	
	
	public void save(UnicomFlow obj){
		dao.save(obj);
	}
	
	public void delete(String id){
		dao.delete(id);
	}
	
	public void  update(UnicomFlow obj){
		dao.update(obj);
	}
	
	public List<Map> getByPhone(Map map){
		return dao.getByPhone(map);
	}
	public List<Map> getListByPhone(String phone){
		return dao.getListByPhone(phone);
	}
	public List<Map> getUnicomFlowForReport(String phone) {
		return dao.getUnicomFlowForReport(phone);
	}
	public List<Map> getUnicomFlowForReport2(Map map) {
		return dao.getUnicomFlowForReport2(map);
	}
	
	public UnicomFlow getMaxStartTime(String phone) {
		return dao.getMaxStartTime(phone);
	}
	public List<Map> getList(String phone) {
		return dao.getList(phone);
	}
	
	public  void insertbatch(List<UnicomFlow> list){
		dao.insertbatch(list);
	}

	
}
