package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.dao.IMobileOnlineBillDao;

@Service
public class MobileOnlineBillDaoImp implements IMobileOnlineBillDao {

	@Resource
	private IMobileOnlineBillDao mobileOnlineBillDao;

	@Override
	public MobileOnlineBill findById(String id) {
		return mobileOnlineBillDao.findById(id);
	}

	@Override
	public void save(MobileOnlineBill obj) {
		mobileOnlineBillDao.save(obj);
	}

	@Override
	public void delete(String id) {
		mobileOnlineBillDao.delete(id);
	}

	@Override
	public void update(MobileOnlineBill obj) {
		mobileOnlineBillDao.update(obj);

	}

	@Override
	public List<Map> getMobileOnlineBillByphone(Map map) {
		return mobileOnlineBillDao.getMobileOnlineBillByphone(map);
	}

	@Override
	public List<Map> getMobileOnlineBill(Map map) {
		return mobileOnlineBillDao.getMobileOnlineBill(map);
	}

	@Override
	public void insertbatch(List<MobileOnlineBill> list) {
		mobileOnlineBillDao.insertbatch(list);

	}

	@Override
	public MobileOnlineBill getMaxTime(String phone) {
		return mobileOnlineBillDao.getMaxTime(phone);
	}

	@Override
	public List getMaxNumTel(Map map) {
		return mobileOnlineBillDao.getMaxNumTel(map);
	}

	@Override
	public List<Map> getMobileOnlineBillForReport(String phone) {
		return mobileOnlineBillDao.getMobileOnlineBillForReport(phone);
	}

}
