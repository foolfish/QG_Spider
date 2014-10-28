package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.TelcomMessage;
import com.lkb.dao.IMobileMessageDao;
import com.lkb.dao.ITelcomMessageDao;

@Service
public class MobileMessageDaoImp {

	@Resource
	private IMobileMessageDao dao;
	
	public MobileMessage findById(String userId){
		return dao.findById(userId);
	}
	
	
	public void save(MobileMessage obj){
		dao.save(obj);
	}
	
	public void delete(String id){
		dao.delete(id);
	}
	
	public void  update(MobileMessage obj){
		dao.update(obj);
	}
	
	public List<Map> getByPhone(Map map){
		return dao.getByPhone(map);
	}
	public List<Map> getListByPhone(String phone){
		return dao.getListByPhone(phone);
	}
	public List<Map> getMobileMessageForReport(Map map) {
		return dao.getMobileMessageForReport(map);
	}
	public List<Map> getMobileMessageForReport2(Map map) {
		return dao.getMobileMessageForReport2(map);
	}
	
	public MobileMessage getMaxSentTime(String phone) {
		return dao.getMaxSentTime(phone);
	}
	public List<Map> getList(String phone) {
		return dao.getList(phone);
	}
	
	public  void insertbatch(List<MobileMessage> list){
		dao.insertbatch(list);
	}

	
}
