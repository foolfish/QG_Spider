package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.UnicomDetail;
import com.lkb.dao.IUnicomDetailDao;



@Service
public class UnicomDetailDaoImp {

	@Resource
	private IUnicomDetailDao unicomDetailDao;
	
	public UnicomDetail findById(String id){
		return unicomDetailDao.findById(id);
	}
	
	
	public void saveUnicomDetail(UnicomDetail unicomDetail){
		unicomDetailDao.saveUnicomDetail(unicomDetail);
	}
	
	public void deleteUnicomDetail(String id){
		unicomDetailDao.deleteUnicomDetail(id);
	}
	
	
	public void  update(UnicomDetail unicomDetail){
		
		unicomDetailDao.update(unicomDetail);
	}

	public List<Map> getUnicomDetailBypt(Map map){		
		return unicomDetailDao.getUnicomDetailBypt(map);
	}
	public List<Map> getUnicomDetailForReport(Map map){		
		return unicomDetailDao.getUnicomDetailForReport(map);
	}
	public List<Map> getUnicomDetailForReport2(Map map){		
		return unicomDetailDao.getUnicomDetailForReport2(map);
	}
	public UnicomDetail getMaxTime(String phone){
		return unicomDetailDao.getMaxTime(phone);
	}
	public List<Map> getUnicomDetail(String phone){		
		return unicomDetailDao.getUnicomDetail(phone);
	}
	
	public void  insertbatch(List<UnicomDetail> list){
		 unicomDetailDao.insertbatch(list);
	}
}
