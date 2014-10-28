package com.lkb.daoImp;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import com.lkb.bean.ZhengxinDetail;

import com.lkb.dao.IZhengxinDetailDao;







@Service
public class ZhengxinDetailDaoImpl implements IZhengxinDetailDao {

	@Resource
	private IZhengxinDetailDao zxDetailDao;
	

	
	public ZhengxinDetail findById(String id){
		return zxDetailDao.findById(id);
	}
	
	
	
	public void saveZhengxinDetail(ZhengxinDetail zxDetail){
		zxDetailDao.saveZhengxinDetail(zxDetail);
	}
	
	
	public void deleteZhengxinDetail(String id){
		zxDetailDao.deleteZhengxinDetail(id);
	}
	

	public void  update(ZhengxinDetail zxDetail){
		zxDetailDao.update(zxDetail);
	}
		
	public List<ZhengxinDetail> getZhengxinDetail(Map map){
		return zxDetailDao.getZhengxinDetail(map);
	}
	
	public List<ZhengxinDetail> getZhengxinByLoginName(String loginName){
		return zxDetailDao.getZhengxinByLoginName(loginName);
	}
	
	
}



