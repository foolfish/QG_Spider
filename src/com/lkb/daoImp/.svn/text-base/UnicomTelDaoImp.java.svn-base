package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import com.lkb.bean.UnicomTel;
import com.lkb.dao.IUnicomTelDao;



@Service
public class UnicomTelDaoImp {

	@Resource
	private IUnicomTelDao unicomTelDao;
	
	public UnicomTel findById(String id){
		return unicomTelDao.findById(id);
	}
	
	
	public void saveUnicomTel(UnicomTel mobileTel){
		unicomTelDao.saveUnicomTel(mobileTel);
	}
	
	public void deleteUnicomTel(String id){
		unicomTelDao.deleteUnicomTel(id);
	}
	
	
	public void  update(UnicomTel unicomTel){
		
		unicomTelDao.update(unicomTel);
	}

	/*
	 * 根据baseUserId和时间来确定一条记录
	 * */
	public List<UnicomTel> getUnicomTelBybc(Map map){		
		return unicomTelDao.getUnicomTelBybc(map);
	}
	public List<Map> getUnicomTelForReport1(Map map){		
		return unicomTelDao.getUnicomTelForReport1(map);
	}
	
	public List<Map> getEveryAmount(Map map){		
		return unicomTelDao.getEveryAmount(map);
	}
	public List<Map> getFormatCtime(String phone){		
		return unicomTelDao.getFormatCtime(phone);
	}

	public void insertbatch(List<UnicomTel> vctList){
		unicomTelDao.insertbatch(vctList);
	}

}
