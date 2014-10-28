package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.DianXinFlow;
import com.lkb.bean.LoseContent;
import com.lkb.constant.Constant;
import com.lkb.dao.ILoseContentDao;
import com.lkb.daoImp.DianXinFlowDaoImp;
import com.lkb.service.ILostContentService;



@Service
@Transactional
public class LostContentServiceImpl implements ILostContentService {
	
	@Resource
	private ILoseContentDao loseContentDao;
	
	
	public LoseContent findById(String id){
		return loseContentDao.findById(id);
	}
	
	public void save(LoseContent loseUrl){
		loseContentDao.save(loseUrl);
	}
	
	public void delete(LoseContent loseUrl){
		loseContentDao.delete(loseUrl);
	}
	
	public void  update(LoseContent loseUrl){
		loseContentDao.update(loseUrl);
	}
	
	 public List<Map> getAll(Map map){
		 return loseContentDao.getAll(map);
	 }
	 
	 
	@Override
	public void insertbatch(List<LoseContent> vctList){
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
						loseContentDao.insertbatch(list);
					}	
				}				
			}else{
				loseContentDao.insertbatch(vctList);
			}	
		}
		
	}
}
