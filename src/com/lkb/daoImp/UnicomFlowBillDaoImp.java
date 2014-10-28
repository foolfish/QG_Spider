package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.MobileTel;
import com.lkb.bean.UnicomFlowBill;
import com.lkb.dao.IUnicomFlowBillDao;


@Service
public class UnicomFlowBillDaoImp {

	@Resource
	private IUnicomFlowBillDao unicomFlowBillDao;
	
	public UnicomFlowBill findById(String id){
		return unicomFlowBillDao.findById(id);
	}
	
	
	public void saveUnicomFlowBill(UnicomFlowBill mobileFlowBill){
		unicomFlowBillDao.saveUnicomFlowBill(mobileFlowBill);
	}
	
	public void deleteUnicomFlowBill(String id){
		unicomFlowBillDao.deleteUnicomFlowBill(id);
	}
	
	public void  update(UnicomFlowBill unicomFlowBill){
		
		unicomFlowBillDao.update(unicomFlowBill);
	}

	/*
	 * 根据baseUserId和时间来确定一条记录
	 * */
	public List<UnicomFlowBill> getUnicomFlowBillBybc(Map map){		
		return unicomFlowBillDao.getUnicomFlowBillBybc(map);
	}
	public List<Map> getUnicomFlowBillForReport1(Map map){		
		return unicomFlowBillDao.getUnicomFlowBillForReport1(map);
	}
	public List<Map> getUnicomFlowBillForReport(String teleno){		
		return unicomFlowBillDao.getUnicomFlowBillForReport(teleno);
	}
	
	public List<Map> getEveryAmount(Map map){		
		return unicomFlowBillDao.getEveryAmount(map);
	}
	public List<Map> getFormatCtime(String phone){		
		return unicomFlowBillDao.getFormatCtime(phone);
	}
	public UnicomFlowBill getMaxAccountTime(UnicomFlowBill unicomFlowBill){
		return unicomFlowBillDao.getMaxAccountTime(unicomFlowBill);
	}
	
	public void insertbatch(List<UnicomFlowBill> vctList){
		unicomFlowBillDao.insertbatch(vctList);
	}


	public List getMaxNumTel(Map map) {
		return unicomFlowBillDao.getMaxNumTel(map);
	}

}