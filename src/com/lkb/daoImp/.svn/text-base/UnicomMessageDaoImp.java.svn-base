package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.UnicomMessage;
import com.lkb.dao.IUnicomMessageDao;

@Service
public class UnicomMessageDaoImp {

	@Resource
	private IUnicomMessageDao dao;
	
	public UnicomMessage findById(String userId){
		return dao.findById(userId);
	}
	
	
	public void save(UnicomMessage obj){
		dao.save(obj);
	}
	
	public void delete(String id){
		dao.delete(id);
	}
	
	public void  update(UnicomMessage obj){
		dao.update(obj);
	}
	
	public List<Map> getByPhone(Map map){
		return dao.getByPhone(map);
	}
	public List<Map> getListByPhone(String phone){
		return dao.getListByPhone(phone);
	}
	public List<Map> getUnicomMessageForReport(Map map) {
		return dao.getUnicomMessageForReport(map);
	}
	public List<Map> getUnicomMessageForReport2(Map map) {
		return dao.getUnicomMessageForReport2(map);
	}
	
	public UnicomMessage getMaxSentTime(String phone) {
		return dao.getMaxSentTime(phone);
	}
	public List<Map> getList(String phone) {
		return dao.getList(phone);
	}
	
	public  void insertbatch(List<UnicomMessage> list){
		dao.insertbatch(list);
	}

	
}
