package com.lkb.daoImp;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.DianXinTel;
import com.lkb.bean.UnicomTel;
import com.lkb.dao.IDianXinTelDao;



@Service
public class DianXinTelDaoImp {

	@Resource
	private IDianXinTelDao dianXinTelDao;
	
	public DianXinTel findById(String id){
		return dianXinTelDao.findById(id);
	}
	
	
	public void saveDianXinTel(DianXinTel dianXinTel){
		dianXinTelDao.saveDianXinTel(dianXinTel);
	}
	
	public void deleteDianXinTel(String id){
		dianXinTelDao.deleteDianXinTel(id);
	}
	
	
	public void  update(DianXinTel dianXinTel){
		
		dianXinTelDao.update(dianXinTel);
	}

	/*
	 * 根据baseUserId和时间来确定一条记录
	 * */
	public List<Map> getDianXinTelBybc(Map map){		
		return dianXinTelDao.getDianXinTelBybc(map);
	}
	public List<Map> getFormatCtime(String phone){		
		return dianXinTelDao.getFormatCtime(phone);
	}
	public List<Map> getDianXinTelForReport1(Map map){		
		return dianXinTelDao.getDianXinTelForReport1(map);
	}
	public List<Map> getEveryAmount(Map map){		
		return dianXinTelDao.getEveryAmount(map);
	}
	public DianXinTel getMaxAccountTime(DianXinTel dianXinTel){
		return dianXinTelDao.getMaxAccountTime(dianXinTel);
	}
	public List getMaxNumTel(Map<String,Object> map){
		return dianXinTelDao.getMaxNumTel(map);
	}
	/*
	 *批量插入
	 * */
	public void insertbatch(List<DianXinTel> list){		
		 dianXinTelDao.insertbatch(list);
	}
}
