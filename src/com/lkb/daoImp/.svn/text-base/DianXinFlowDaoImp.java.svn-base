package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.DianXinFlow;
import com.lkb.dao.IDianXinFlowDao;
import com.lkb.dao.IDianXinFlowDao;



@Service
public class DianXinFlowDaoImp {

	@Resource
	private IDianXinFlowDao dianXinFlowDao;
	
	public DianXinFlow findById(String id){
		return dianXinFlowDao.findById(id);
	}
	
	
	public void saveDianXinFlow(DianXinFlow dianXinFlow){
		dianXinFlowDao.saveDianXinFlow(dianXinFlow);
	}
	
	public void deleteDianXinFlow(String id){
		dianXinFlowDao.deleteDianXinFlow(id);
	}
	
	
	public void  update(DianXinFlow dianXinFlow){
		
		dianXinFlowDao.update(dianXinFlow);
	}

	/*
	 * 根据baseUserId和时间来确定一条记录
	 * */
	public List<Map> getDianXinFlowBybc(Map map){		
		return dianXinFlowDao.getDianXinFlowBybc(map);
	}
	public List<Map> getDianXinFlowForReport(String phone){		
		return dianXinFlowDao.getDianXinFlowForReport(phone);
	}
	public List<Map> getFormatQueryTime(String phone){		
		return dianXinFlowDao.getFormatQueryTime(phone);
	}
/*	public List<Map> getDianXinFlowForReport1(Map map){		
		return dianXinFlowDao.getDianXinFlowForReport1(map);
	}
	public List<Map> getEveryAmount(Map map){		
		return dianXinFlowDao.getEveryAmount(map);
	}*/
	public DianXinFlow getMaxAccountTime(String phone){
		return dianXinFlowDao.getMaxAccountTime(phone);
	}
	public List getMaxNumTel(Map<String,Object> map){
		return dianXinFlowDao.getMaxNumTel(map);
	}
	/*
	 *批量插入
	 * */
	public void insertbatch(List<DianXinFlow> list){		
		 dianXinFlowDao.insertbatch(list);
	}
}
