package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.UnicomFlow;
import com.lkb.constant.Constant;
import com.lkb.dao.IUnicomFlowDao;
import com.lkb.service.IUnicomFlowService;

@Service
@Transactional
public class UnicomFlowServiceImpl implements IUnicomFlowService {

	@Resource
	private IUnicomFlowDao dao;

	@Override
	public UnicomFlow findById(String id) {
		return dao.findById(id);
	}

	@Override
	public void save(UnicomFlow obj) {
		dao.save(obj);
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public void update(UnicomFlow obj) {
		dao.update(obj);
	}

	@Override
	public List<Map> getByPhone(Map map) {
		return dao.getByPhone(map);
	}

	@Override
	public List<Map> getListByPhone(String phone) {
		return dao.getListByPhone(phone);
	}

	@Override
	public List<Map> getUnicomFlowForReport(String phone) {
		return dao.getUnicomFlowForReport(phone);
	}

	@Override
	public List<Map> getUnicomFlowForReport2(Map map) {
		return dao.getUnicomFlowForReport2(map);
	}

	@Override
	public UnicomFlow getMaxStartTime(String phone) {
		return dao.getMaxStartTime(phone);
	}

	@Override
	public List<Map> getList(String phone) {
		return dao.getList(phone);
	}

	@Override
	public void insertbatch(List<UnicomFlow> vctList) {
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
					List<UnicomFlow> list = vctList.subList(j*batchAmount, size);
					if(list!=null && list.size()>0){
						dao.insertbatch(list);
					}	
				}				
			}else{
				dao.insertbatch(vctList);
			}	
		}
	}
	
}
