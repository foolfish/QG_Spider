package com.lkb.daoImp;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import com.lkb.bean.ZhengxinSummary;
import com.lkb.dao.IZhengxinSummaryDao;



@Service
public class ZhengxinSummaryDaoImpl implements IZhengxinSummaryDao {

	@Resource
	private IZhengxinSummaryDao zxSummaryDao;
	

	public ZhengxinSummary findById(String id){
		return zxSummaryDao.findById(id);
	}
	
	
	
	public void saveZhengxinSummary(ZhengxinSummary zxSummay){
		zxSummaryDao.saveZhengxinSummary(zxSummay);
	}
	
	
	public void deleteZhengxinSummary(String id){
		zxSummaryDao.deleteZhengxinSummary(id);
	}
	
	

	
	public void  update(ZhengxinSummary zxSummay){
		zxSummaryDao.update(zxSummay);
	}
	public List<ZhengxinSummary> getZhengxinSummary(Map map){
		return zxSummaryDao.getZhengxinSummary(map);
	}
	
	
	
	public List<ZhengxinSummary> getZhengxinSummaryByLoginName(String loginName){
		return zxSummaryDao.getZhengxinSummaryByLoginName(loginName);
	}
	
	
}



