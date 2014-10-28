package com.lkb.serviceImp;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileTel;
import com.lkb.constant.Constant;
import com.lkb.daoImp.MobileTelDaoImp;
import com.lkb.service.IMobileTelService;



@Service
@Transactional
public class MobileTelServiceImpl implements IMobileTelService {

	@Resource
	private MobileTelDaoImp mobileTelModel;
	
	@Override
	public MobileTel findById(String id) {
		
		MobileTel MobileTel = mobileTelModel.findById(id);
		return MobileTel; 
	}

	@Override
	public  void saveMobileTel(MobileTel mobileTel) {	
		try {
			mobileTelModel.saveMobileTel(mobileTel);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteMobileTel(String id) {	
		try {
			mobileTelModel.deleteMobileTel(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(MobileTel mobileTel){
		
		mobileTelModel.update(mobileTel);
	}
	
	@Override
	public List<Map> getMobileTelBybc(Map map){		
		return mobileTelModel.getMobileTelBybc(map);
	}
	@Override
	public List<Map> getFormatCtime(String phone){		
		return mobileTelModel.getFormatCtime(phone);
	}

	@Override
	public List<Map> getMobileTelForReport1(Map map) {
		return mobileTelModel.getMobileTelForReport1(map);
	}
	@Override
	public List<Map> getEveryAmount(Map map){		
		return mobileTelModel.getEveryAmount(map);
	}
	public MobileTel getMaxTime(MobileTel mobileTel){
		return mobileTelModel.getMaxAccountTime(mobileTel);
	}
	public List getMaxNumTel(Map<String,Object> map){
		return mobileTelModel.getMaxNumTel(map);
	}
	@Override
	public void insertbatch(List<MobileTel> vctList){
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
						mobileTelModel.insertbatch(list);
					}	
				}				
			}else{
				mobileTelModel.insertbatch(vctList);
			}	
		}
		
	}
}
