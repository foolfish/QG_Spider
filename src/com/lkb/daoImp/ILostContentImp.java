package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.LoseContent;
import com.lkb.dao.ILoseContentDao;



@Service
public class ILostContentImp {

	@Resource
	private ILoseContentDao loseContentDao;
	
	
	LoseContent findById(String id){
		return loseContentDao.findById(id);
	}
	
	void save(LoseContent loseUrl){
		loseContentDao.save(loseUrl);
	}
	
	void delete(LoseContent loseUrl){
		loseContentDao.delete(loseUrl);
	}
	
	void  update(LoseContent loseUrl){
		loseContentDao.update(loseUrl);
	}
	
	 List<Map> getAll(Map map){
		 return loseContentDao.getAll(map);
	 }
	 void insertbatch(List<LoseContent> list){
		 loseContentDao.insertbatch(list);
	 }
	
}
