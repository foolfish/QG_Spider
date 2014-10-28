package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.UnicomTel;
import com.lkb.dao.IMobileDetailDao;



@Service
public class MobileDetailDaoImp {

	@Resource
	private IMobileDetailDao mobileDetailDao;
	
	public MobileDetail findById(String id){
		return mobileDetailDao.findById(id);
	}
	
	
	public void saveMobileDetail(MobileDetail mobileDetail){
		mobileDetailDao.saveMobileDetail(mobileDetail);
	}
	
	public void deleteMobileDetail(String id){
		mobileDetailDao.deleteMobileDetail(id);
	}
	
	
	public void  update(MobileDetail mobileDetail){
		
		mobileDetailDao.update(mobileDetail);
	}

	public List<Map> getMobileDetailBypt(Map map){		
		return mobileDetailDao.getMobileDetailBypt(map);
	}
	public List<Map> getMobileDetailForReport(Map map){		
		return mobileDetailDao.getMobileDetailForReport(map);
	}
	public List<Map> getMobileDetailForReport2(Map map){		
		return mobileDetailDao.getMobileDetailForReport2(map);
	}
	
	public MobileDetail getMaxTime(String  phone){
		return mobileDetailDao.getMaxTime(phone);
	}
	
	public List<Map> getMobileDetail(String phone){		
		return mobileDetailDao.getMobileDetail(phone);
	}
	
	
	public void insertbatch(List<MobileDetail> vctList){		
		 mobileDetailDao.insertbatch(vctList);
	}
	
	
}
