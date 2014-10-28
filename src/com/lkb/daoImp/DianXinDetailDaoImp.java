package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.UnicomTel;
import com.lkb.dao.IDianXinDetailDao;



@Service
public class DianXinDetailDaoImp {

	@Resource
	private IDianXinDetailDao dianxinDetailDao;
	
	public DianXinDetail findById(String id){
		return dianxinDetailDao.findById(id);
	}
	
	
	public void saveDianXinDetail(DianXinDetail dianxinDetail){
		dianxinDetailDao.saveDianXinDetail(dianxinDetail);
	}
	
	public void deleteDianXinDetail(String id){
		dianxinDetailDao.deleteDianXinDetail(id);
	}
	
	
	public void  update(DianXinDetail dianxinDetail){
		
		dianxinDetailDao.update(dianxinDetail);
	}

	public List<Map> getDianXinDetailBypt(Map map){		
		return dianxinDetailDao.getDianXinDetailBypt(map);
	}
	public List<Map> getDianXinDetailForReport(Map map){		
		return dianxinDetailDao.getDianXinDetailForReport(map);
	}
	public List<Map> getDianXinDetailForReport2(Map map){		
		return dianxinDetailDao.getDianXinDetailForReport2(map);
	}
	public DianXinDetail getMaxCallTime(String phone) {
		return  dianxinDetailDao.getMaxCallTime(phone);
	}
	
	public List<Map> getDianXinDetail(String phone){		
		return dianxinDetailDao.getDianXinDetail(phone);
	}
	
	/*
	 *批量插入
	 * */
	public void insertbatch(List<DianXinDetail> list){		
		 dianxinDetailDao.insertbatch(list);
	}
}
