package com.lkb.serviceImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.MobileOnlineBill;
import com.lkb.constant.Constant;
import com.lkb.daoImp.MobileOnlineBillDaoImp;
import com.lkb.service.IMobileOnlineBillService;

@Service
@Transactional
public class MobileOnlineBillServiceImpl implements IMobileOnlineBillService{

	@Resource
	private MobileOnlineBillDaoImp mobileOnlineBillModel;
	
	@Override
	public MobileOnlineBill findById(String id) {
		return mobileOnlineBillModel.findById(id);
	}

	@Override
	public void save(MobileOnlineBill obj) {
		try {
			mobileOnlineBillModel.save(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String id) {
		try {
			mobileOnlineBillModel.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(MobileOnlineBill obj) {
		mobileOnlineBillModel.update(obj);
	}

	@Override
	public List<Map> getMobileOnlineBillByphone(Map map) {
		return mobileOnlineBillModel.getMobileOnlineBillByphone(map);
	}

	@Override
	public List<Map> getMobileOnlineBill(Map map) {
		return mobileOnlineBillModel.getMobileOnlineBill(map);
	}

	@Override
	public void insertbatch(List<MobileOnlineBill> list) {
		int batchAmount = Constant.batchAmount;
		int vsize = list.size();
		if(list != null && vsize > 0){
			int amount = vsize/batchAmount;
			if(amount >= 1){
				for(int j = 0 ; j<amount+1; j++){
					int size = (j+1) * batchAmount;
					if(size > vsize){
						size = vsize;
					}
					List vslist = list.subList(j*batchAmount, size);
					if(vslist!=null && vslist.size()>0){
						mobileOnlineBillModel.insertbatch(vslist);
				}
			}
			
		} else {
			mobileOnlineBillModel.insertbatch(list);
		}
		}
		
	}

	@Override
	public MobileOnlineBill getMaxTime(String phone) {
		return mobileOnlineBillModel.getMaxTime(phone);
	}

	@Override
	public List getMaxNumTel(Map map) {
		return mobileOnlineBillModel.getMaxNumTel(map);
	}

	@Override
	public List<Map> getMobileOnlineBillForReport(String phone) {
		return mobileOnlineBillModel.getMobileOnlineBillForReport(phone);
	}

}
