package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.MobileTel;
import com.lkb.bean.UnicomDetail;
import com.lkb.constant.Constant;
import com.lkb.daoImp.UnicomDetailDaoImp;
import com.lkb.service.IUnicomDetailService;



@Service
@Transactional
public class UnicomDetailServiceImpl implements IUnicomDetailService {

	@Resource
	private UnicomDetailDaoImp unicomDetailModel;
	
	@Override
	public UnicomDetail findById(String id) {
		
		UnicomDetail UnicomDetail = unicomDetailModel.findById(id);
		return UnicomDetail; 
	}

	@Override
	public  void saveUnicomDetail(UnicomDetail unicomDetail) {	
		try {
			unicomDetailModel.saveUnicomDetail(unicomDetail);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteUnicomDetail(String id) {	
		try {
			unicomDetailModel.deleteUnicomDetail(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(UnicomDetail unicomDetail){
		
		unicomDetailModel.update(unicomDetail);
	}
	
	@Override
	public List<Map> getUnicomDetailBypt(Map map){		
		return unicomDetailModel.getUnicomDetailBypt(map);
	}

	@Override
	public List<Map> getUnicomDetailForReport(Map map) {
		return unicomDetailModel.getUnicomDetailForReport(map);
	}
	@Override
	public List<Map> getUnicomDetailForReport2(Map map) {
		return unicomDetailModel.getUnicomDetailForReport2(map);
	}
	@Override
	public List<Map> getUnicomDetail(String phone){
		return unicomDetailModel.getUnicomDetail(phone);
	}
	
	@Override
	public void insertbatch(List<UnicomDetail> vctList){
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
						unicomDetailModel.insertbatch(list);
					}	
				}				
			}else{
				unicomDetailModel.insertbatch(vctList);
			}	
		}
		
	}

	@Override
	public UnicomDetail getMaxTime(UnicomDetail unicomDetail) {
		// TODO Auto-generated method stub
		return unicomDetailModel.getMaxTime(unicomDetail.getPhone());
	}
	
	
}
