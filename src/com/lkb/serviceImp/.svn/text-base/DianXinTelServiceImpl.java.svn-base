package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.DianXinTel;
import com.lkb.constant.Constant;
import com.lkb.daoImp.DianXinTelDaoImp;
import com.lkb.service.IDianXinTelService;



@Service
@Transactional
public class DianXinTelServiceImpl implements IDianXinTelService {

	@Resource
	private DianXinTelDaoImp dianXinTelModel;
	
	@Override
	public DianXinTel findById(String id) {
		
		DianXinTel DianXinTel = dianXinTelModel.findById(id);
		return DianXinTel; 
	}

	@Override
	public  void saveDianXinTel(DianXinTel dianXinTel) {	
		try {
			dianXinTelModel.saveDianXinTel(dianXinTel);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteDianXinTel(String id) {	
		try {
			dianXinTelModel.deleteDianXinTel(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(DianXinTel dianXinTel){
		
		dianXinTelModel.update(dianXinTel);
	}
	
	@Override
	public List<Map> getDianXinTelBybc(Map map){		
		return dianXinTelModel.getDianXinTelBybc(map);
	}
	@Override
	public List<Map> getFormatCtime(String phone){		
		return dianXinTelModel.getFormatCtime(phone);
	}
	@Override
	public List<Map> getEveryAmount(Map map){		
		return dianXinTelModel.getEveryAmount(map);
	}
	
	@Override
	public List<Map> getDianXinTelForReport1(Map map) {
		return dianXinTelModel.getDianXinTelForReport1(map);
	}
	@Override
	public DianXinTel getMaxTime(DianXinTel dianXinTel){
		return dianXinTelModel.getMaxAccountTime(dianXinTel);
	}
	@Override
	public List getMaxNumTel(Map<String,Object> map){
		return dianXinTelModel.getMaxNumTel(map);
	}
	@Override
	public void insertbatch(List<DianXinTel> vctList){
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
						dianXinTelModel.insertbatch(list);
					}	
				}				
			}else{
				dianXinTelModel.insertbatch(vctList);
			}	
		}
		
	}
}
