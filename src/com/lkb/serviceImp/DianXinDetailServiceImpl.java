package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.DianXinDetail;
import com.lkb.constant.Constant;
import com.lkb.daoImp.DianXinDetailDaoImp;
import com.lkb.service.IDianXinDetailService;



@Service
@Transactional
public class DianXinDetailServiceImpl implements IDianXinDetailService {

	@Resource
	private DianXinDetailDaoImp dianxinDetailModel;
	
	@Override
	public DianXinDetail findById(String id) {
		
		DianXinDetail DianXinDetail = dianxinDetailModel.findById(id);
		return DianXinDetail; 
	}

	@Override
	public  void saveDianXinDetail(DianXinDetail dianxinDetail) {	
		try {
			dianxinDetailModel.saveDianXinDetail(dianxinDetail);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteDianXinDetail(String id) {	
		try {
			dianxinDetailModel.deleteDianXinDetail(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(DianXinDetail dianxinDetail){
		
		dianxinDetailModel.update(dianxinDetail);
	}
	
	@Override
	public List<Map> getDianXinDetailBypt(Map map){		
		return dianxinDetailModel.getDianXinDetailBypt(map);
	}

	@Override
	public List<Map> getDianXinDetailForReport(Map map) {
		return dianxinDetailModel.getDianXinDetailForReport(map);
		
	}

	@Override
	public List<Map> getDianXinDetailForReport2(Map map) {
		return dianxinDetailModel.getDianXinDetailForReport2(map);
	}
	@Override
	public DianXinDetail getMaxTime(DianXinDetail dianxinDetail) {
		return dianxinDetailModel.getMaxCallTime(dianxinDetail.getPhone());
	}
	@Override
	public List<Map> getDianXinDetail(String phone){		
		return dianxinDetailModel.getDianXinDetail(phone);
	}
	
	@Override
	public void insertbatch(List<DianXinDetail> vctList){
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
						dianxinDetailModel.insertbatch(list);
					}	
				}				
			}else{
				dianxinDetailModel.insertbatch(vctList);
			}	
		}
		
	}
	
}
