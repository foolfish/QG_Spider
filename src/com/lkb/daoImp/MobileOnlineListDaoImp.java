package com.lkb.daoImp;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.lkb.bean.MobileOnlineList;
import com.lkb.dao.IMobileOnlineListDao;

@Service
public class MobileOnlineListDaoImp implements IMobileOnlineListDao{


	@Resource
	private IMobileOnlineListDao mobileOnlineListDao;
	
	@Override
	public MobileOnlineList findById(String id) {
		return mobileOnlineListDao.findById(id);
	}

	@Override
	public void save(MobileOnlineList obj) {
		mobileOnlineListDao.save(obj);
		
	}

	@Override
	public void delete(String id) {
		mobileOnlineListDao.delete(id);
		
	}

	@Override
	public void update(MobileOnlineList obj) {
		mobileOnlineListDao.update(obj);
		
	}

	@Override
	public List<Map> getMobileOnlineListBypt(Map map) {
		return mobileOnlineListDao.getMobileOnlineListBypt(map);
	}

	@Override
	public List<Map> getMobileOnlineListForReport(String phone) {
		return mobileOnlineListDao.getMobileOnlineListForReport(phone);
	}

	@Override
	public List<Map> getMobileOnlineListForReport2(Map map) {
		return mobileOnlineListDao.getMobileOnlineListForReport2(map);
	}

	@Override
	public MobileOnlineList getMaxTime(String phone) {
		return mobileOnlineListDao.getMaxTime(phone);
	}

	@Override
	public List<Map> getMobileOnlineList(String phone) {
		return mobileOnlineListDao.getMobileOnlineList(phone);
	}

	@Override
	public void insertbatch(List<MobileOnlineList> list) {
		mobileOnlineListDao.insertbatch(list);
		
	}

}
