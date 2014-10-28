package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.DianXinFlowDetail;
import com.lkb.constant.Constant;
import com.lkb.daoImp.DianXinFlowDetailDaoImp;
import com.lkb.service.IDianXinFlowDetailService;



@Service
@Transactional
public class DianXinFlowDetailServiceImpl implements IDianXinFlowDetailService {

	@Resource
	private DianXinFlowDetailDaoImp dianXinFlowDetailModel;
	
	@Override
	public DianXinFlowDetail findById(String id) {
		
		DianXinFlowDetail DianXinFlowDetail = dianXinFlowDetailModel.findById(id);
		return DianXinFlowDetail; 
	}

	@Override
	public  void saveDianXinFlowDetail(DianXinFlowDetail dianXinFlowDetail) {	
		try {
			dianXinFlowDetailModel.saveDianXinFlowDetail(dianXinFlowDetail);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteDianXinFlowDetail(String id) {	
		try {
			dianXinFlowDetailModel.deleteDianXinFlowDetail(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(DianXinFlowDetail dianxinDetail){
		
		dianXinFlowDetailModel.update(dianxinDetail);
	}
	
	@Override
	public List<Map> getDianXinFlowDetailBypt(Map map){		
		return dianXinFlowDetailModel.getDianXinFlowDetailBypt(map);
	}

/*	@Override
	public List<Map> getDianXinFlowDetailForReport(Map map) {
		return dianXinFlowDetailModel.getDianXinFlowDetailForReport(map);
		
	}

	@Override
	public List<Map> getDianXinFlowDetailForReport2(Map map) {
		return dianXinFlowDetailModel.getDianXinFlowDetailForReport2(map);
	}*/
	@Override
	public DianXinFlowDetail getMaxTime(DianXinFlowDetail dianxinDetail) {
		return dianXinFlowDetailModel.getMaxFlowTime(dianxinDetail.getPhone());
	}
	@Override
	public List<Map> getDianXinFlowDetail(String phone){		
		return dianXinFlowDetailModel.getDianXinFlowDetail(phone);
	}
	@Override
	public List<Map> getDianXinFlowDetailForReport(String phone){		
		return dianXinFlowDetailModel.getDianXinFlowDetailForReport(phone);
	}
	
	@Override
	public void insertbatch(List<DianXinFlowDetail> vctList){
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
						dianXinFlowDetailModel.insertbatch(list);
					}	
				}				
			}else{
				dianXinFlowDetailModel.insertbatch(vctList);
			}	
		}
		
	}
	
}
