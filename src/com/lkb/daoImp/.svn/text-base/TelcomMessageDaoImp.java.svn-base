package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.TelcomMessage;
import com.lkb.dao.ITelcomMessageDao;

@Service
public class TelcomMessageDaoImp {

	@Resource
	private ITelcomMessageDao dao;
	
	public TelcomMessage findById(String userId){
		return dao.findById(userId);
	}
	
	
	public void save(TelcomMessage obj){
		dao.save(obj);
	}
	
	public void delete(String id){
		dao.delete(id);
	}
	
	public void  update(TelcomMessage obj){
		dao.update(obj);
	}
	
	public List<Map> getByPhone(Map map){
		return dao.getByPhone(map);
	}
	public List<Map> getListByPhone(String phone){
		return dao.getListByPhone(phone);
	}
	public List<Map> getTelcomMessageForReport(Map map) {
		return dao.getTelcomMessageForReport(map);
	}
	public List<Map> getTelcomMessageForReport2(Map map) {
		return dao.getTelcomMessageForReport2(map);
	}
	
	public TelcomMessage getMaxSentTime(String phone) {
		return dao.getMaxSentTime(phone);
	}
	public List<Map> getList(String phone) {
		return dao.getList(phone);
	}
	
	public void insertbatch(List<TelcomMessage> vctList) {
		 dao.insertbatch(vctList);
	}
	
}
