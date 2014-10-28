package com.lkb.daoImp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileTel;
import com.lkb.dao.IMobileTelDao;



@Service
public class MobileTelDaoImp {

	@Resource
	private IMobileTelDao mobileTelDao;
	
	public MobileTel findById(String id){
		return mobileTelDao.findById(id);
	}
	
	
	public void saveMobileTel(MobileTel mobileTel){
		mobileTelDao.saveMobileTel(mobileTel);
	}
	
	public void deleteMobileTel(String id){
		mobileTelDao.deleteMobileTel(id);
	}
	
	
	public void  update(MobileTel mobileTel){
		
		mobileTelDao.update(mobileTel);
	}

	/*
	 * 根据baseUserId和时间来确定一条记录
	 * */
	public List<Map> getMobileTelBybc(Map map){		
		return mobileTelDao.getMobileTelBybc(map);
	}
	public List<Map> getFormatCtime(String phone){		
		return mobileTelDao.getFormatCtime(phone);
	}
	public List<Map> getMobileTelForReport1(Map map){		
		return mobileTelDao.getMobileTelForReport1(map);
	}
	
	public List<Map> getEveryAmount(Map map){		
		return mobileTelDao.getEveryAmount(map);
	}
	public MobileTel getMaxAccountTime(MobileTel mobileTel){
		return mobileTelDao.getMaxAccountTime(mobileTel);
	}
	public List getMaxNumTel(Map<String,Object> map){
		return mobileTelDao.getMaxNumTel(map);
	}
	
	public  void insertbatch(List<MobileTel> list){
		mobileTelDao.insertbatch(list);
	}
}
