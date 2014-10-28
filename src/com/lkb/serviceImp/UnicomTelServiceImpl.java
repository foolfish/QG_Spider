package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.lkb.bean.UnicomDetail;
import com.lkb.bean.UnicomTel;
import com.lkb.constant.Constant;
import com.lkb.daoImp.UnicomTelDaoImp;
import com.lkb.service.IUnicomTelService;



@Service
@Transactional
public class UnicomTelServiceImpl implements IUnicomTelService {

	@Resource
	private UnicomTelDaoImp unicomTelModel;
	
	@Override
	public UnicomTel findById(String id) {
		
		UnicomTel UnicomTel = unicomTelModel.findById(id);
		return UnicomTel; 
	}

	@Override
	public  void saveUnicomTel(UnicomTel unicomTel) {	
		try {
			unicomTelModel.saveUnicomTel(unicomTel);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteUnicomTel(String id) {	
		try {
			unicomTelModel.deleteUnicomTel(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(UnicomTel unicomTel){
		
		unicomTelModel.update(unicomTel);
	}
	
	@Override
	public List<UnicomTel> getUnicomTelBybc(Map map){		
		return unicomTelModel.getUnicomTelBybc(map);
	}

	@Override
	public List<Map> getEveryAmount(Map map){		
		return unicomTelModel.getEveryAmount(map);
	}
	@Override
	public List<Map> getFormatCtime(String phone){		
		return unicomTelModel.getFormatCtime(phone);
	}
	@Override
	public List<Map> getUnicomTelForReport1(Map map){		
		return unicomTelModel.getUnicomTelForReport1(map);
	}
	@Override
	public void insertbatch(List<UnicomTel> vctList){
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
						unicomTelModel.insertbatch(list);
					}	
				}				
			}else{
				unicomTelModel.insertbatch(vctList);
			}	
		}
		
	}
	
	
}
