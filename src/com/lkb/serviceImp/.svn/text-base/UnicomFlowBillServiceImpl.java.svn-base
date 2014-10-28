package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.lkb.bean.UnicomFlowBill;
import com.lkb.constant.Constant;
import com.lkb.daoImp.UnicomFlowBillDaoImp;
import com.lkb.service.IUnicomFlowBillService;



@Service
@Transactional
public class UnicomFlowBillServiceImpl implements IUnicomFlowBillService {

	@Resource
	private UnicomFlowBillDaoImp unicomFlowBillModel;
	
	@Override
	public UnicomFlowBill findById(String id) {
		
		UnicomFlowBill UnicomFlowBill = unicomFlowBillModel.findById(id);
		return UnicomFlowBill; 
	}

	@Override
	public  void saveUnicomFlowBill(UnicomFlowBill unicomFlowBill) {	
		try {
			unicomFlowBillModel.saveUnicomFlowBill(unicomFlowBill);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteUnicomFlowBill(String id) {	
		try {
			unicomFlowBillModel.deleteUnicomFlowBill(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(UnicomFlowBill unicomFlowBill){
		
		unicomFlowBillModel.update(unicomFlowBill);
	}
	
	@Override
	public List<UnicomFlowBill> getUnicomFlowBillBybc(Map map){		
		return unicomFlowBillModel.getUnicomFlowBillBybc(map);
	}

	@Override
	public List<Map> getEveryAmount(Map map){		
		return unicomFlowBillModel.getEveryAmount(map);
	}
	@Override
	public List<Map> getFormatCtime(String phone){		
		return unicomFlowBillModel.getFormatCtime(phone);
	}
	@Override
	public List<Map> getUnicomFlowBillForReport1(Map map){		
		return unicomFlowBillModel.getUnicomFlowBillForReport1(map);
	}
	@Override
	public void insertbatch(List<UnicomFlowBill> vctList){
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
						unicomFlowBillModel.insertbatch(list);
					}	
				}				
			}else{
				unicomFlowBillModel.insertbatch(vctList);
			}	
		}
		
	}

	@Override
	public UnicomFlowBill getMaxTime(UnicomFlowBill unicomFlowBill) {
		return unicomFlowBillModel.getMaxAccountTime(unicomFlowBill);
	}

	@Override
	public List getMaxNumTel(Map map) {
		return unicomFlowBillModel.getMaxNumTel(map);
	}

	@Override
	public List<Map> getUnicomFlowBillForReport(String teleno) {
		return unicomFlowBillModel.getUnicomFlowBillForReport(teleno);
	}
	
	
}
