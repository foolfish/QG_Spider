package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlow;
import com.lkb.constant.Constant;
import com.lkb.daoImp.DianXinFlowDaoImp;
import com.lkb.service.IDianXinFlowService;



@Service
@Transactional
public class DianXinFlowServiceImpl implements IDianXinFlowService {

	@Resource
	private DianXinFlowDaoImp dianXinFlowModel;
	
	@Override
	public DianXinFlow findById(String id) {
		
		DianXinFlow DianXinFlow = dianXinFlowModel.findById(id);
		return DianXinFlow; 
	}

	@Override
	public  void saveDianXinFlow(DianXinFlow dianXinFlow) {	
		try {
			dianXinFlowModel.saveDianXinFlow(dianXinFlow);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteDianXinFlow(String id) {	
		try {
			dianXinFlowModel.deleteDianXinFlow(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(DianXinFlow dianXinFlow){
		
		dianXinFlowModel.update(dianXinFlow);
	}
	
	@Override
	public List<Map> getDianXinFlowBybc(Map map){		
		return dianXinFlowModel.getDianXinFlowBybc(map);
	}
	@Override
	public List<Map> getFormatQueryTime(String phone){		
		return dianXinFlowModel.getFormatQueryTime(phone);
	}
/*	@Override
	public List<Map> getEveryAmount(Map map){		
		return dianXinFlowModel.getEveryAmount(map);
	}
	
	@Override
	public List<Map> getDianXinFlowForReport1(Map map) {
		return dianXinFlowModel.getDianXinFlowForReport1(map);
	}*/
	@Override
	public DianXinFlow getMaxFlowTime(String phone){
		return dianXinFlowModel.getMaxAccountTime(phone);
	}
	@Override
	public List getMaxNumTel(Map<String,Object> map){
		return dianXinFlowModel.getMaxNumTel(map);
	}
	@Override
	public void insertbatch(List<DianXinFlow> vctList){
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
						dianXinFlowModel.insertbatch(list);
					}	
				}				
			}else{
				dianXinFlowModel.insertbatch(vctList);
			}	
		}
		
	}

	@Override
	public List<Map> getDianXinFlowForReport(String phone) {
		return dianXinFlowModel.getDianXinFlowForReport(phone);
	}
}
