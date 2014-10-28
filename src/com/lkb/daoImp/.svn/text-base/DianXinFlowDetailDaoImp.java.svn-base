package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.DianXinFlowDetail;
import com.lkb.dao.IDianXinFlowDetailDao;



@Service
public class DianXinFlowDetailDaoImp {

	@Resource
	private IDianXinFlowDetailDao dianXinFlowDetailDao;
	
	public DianXinFlowDetail findById(String id){
		return dianXinFlowDetailDao.findById(id);
	}
	
	
	public void saveDianXinFlowDetail(DianXinFlowDetail dianXinFlowDetail){
		dianXinFlowDetailDao.saveDianXinFlowDetail(dianXinFlowDetail);
	}
	
	public void deleteDianXinFlowDetail(String id){
		dianXinFlowDetailDao.deleteDianXinFlowDetail(id);
	}
	
	
	public void update(DianXinFlowDetail dianXinFlowDetail){
		dianXinFlowDetailDao.update(dianXinFlowDetail);
	}

	public List<Map> getDianXinFlowDetailBypt(Map map){		
		return dianXinFlowDetailDao.getDianXinFlowDetailBypt(map);
	}
/*	public List<Map> getDianXinFlowDetailForReport(Map map){		
		return dianXinFlowDetailDao.getDianXinFlowDetailForReport(map);
	}
	public List<Map> getDianXinFlowDetailForReport2(Map map){		
		return dianXinFlowDetailDao.getDianXinFlowDetailForReport2(map);
	}*/
	public DianXinFlowDetail getMaxFlowTime(String phone) {
		return  dianXinFlowDetailDao.getMaxFlowTime(phone);
	}
	
	public List<Map> getDianXinFlowDetail(String phone){		
		return dianXinFlowDetailDao.getDianXinFlowDetail(phone);
	}
	public List<Map> getDianXinFlowDetailForReport(String phone){		
		return dianXinFlowDetailDao.getDianXinFlowDetailForReport(phone);
	}
	/*
	 *批量插入
	 * */
	public void insertbatch(List<DianXinFlowDetail> list){		
		dianXinFlowDetailDao.insertbatch(list);
	}
}
