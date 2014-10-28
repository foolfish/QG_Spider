package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.constant.Constant;
import com.lkb.daoImp.MobileDetailDaoImp;
import com.lkb.service.IMobileDetailService;



@Service
@Transactional
public class MobileDetailServiceImpl implements IMobileDetailService {

	@Resource
	private MobileDetailDaoImp mobileDetailModel;
	
	@Override
	public MobileDetail findById(String id) {
		
		MobileDetail MobileDetail = mobileDetailModel.findById(id);
		return MobileDetail; 
	}

	@Override
	public  void saveMobileDetail(MobileDetail mobileDetail) {	
		try {
			mobileDetailModel.saveMobileDetail(mobileDetail);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteMobileDetail(String id) {	
		try {
			mobileDetailModel.deleteMobileDetail(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(MobileDetail mobileDetail){
		
		mobileDetailModel.update(mobileDetail);
	}
	
	@Override
	public List<Map> getMobileDetailBypt(Map map){		
		return mobileDetailModel.getMobileDetailBypt(map);
	}

	@Override
	public List<Map> getMobileDetailForReport(Map map) {
		return mobileDetailModel.getMobileDetailForReport(map);
	}

	@Override
	public List<Map> getMobileDetailForReport2(Map map) {
		return mobileDetailModel.getMobileDetailForReport2(map);
	}

	
	@Override
	public MobileDetail getMaxTime(MobileDetail mobileDetail) {
		
		return mobileDetailModel.getMaxTime(mobileDetail.getPhone());
	}
	@Override
	public List<Map> getMobileDetail(String phone){
		return mobileDetailModel.getMobileDetail(phone);
	}
	
	@Override
	public void insertbatch(List<MobileDetail> vctList){
		int batchAmount = Constant.batchAmount;
		int vsize =  vctList.size();
		if(vctList!=null&&vsize>0){
			int amount = vsize/batchAmount;
			if(amount>=1){
				for(int j=0;j<amount+1;j++){
					int size = (j+1)*batchAmount;
					if(size>vsize){
						size = vsize;
					}
					List list = vctList.subList(j*batchAmount, size);
					if(list!=null && list.size()>0){
						mobileDetailModel.insertbatch(list);
					}	
				}				
			}else{
				mobileDetailModel.insertbatch(vctList);
			}	
		}
		
	}
}
