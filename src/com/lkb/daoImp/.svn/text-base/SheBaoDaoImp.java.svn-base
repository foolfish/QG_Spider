package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.SheBao;
import com.lkb.dao.ISheBaoDao;


@Service
public class SheBaoDaoImp {

	@Resource
	private ISheBaoDao shebaoDao;
	
	public SheBao findById(String SheBaoId){
		return shebaoDao.findById(SheBaoId);
	}
	
	public void saveSheBao(SheBao SheBao){
		shebaoDao.saveSheBao(SheBao);
	}
	
	public void deleteSheBao(String SheBaoId){
		shebaoDao.deleteSheBao(SheBaoId);
	}
	
	public List<SheBao> getSheBaoByBaseUseridsource(Map map){
		return shebaoDao.getSheBaoByBaseUseridsource(map);
	}
		
	public void  update(SheBao SheBao){
		shebaoDao.update(SheBao);
	}
	
	public List<Map> getAmountCount(Map map){
		return shebaoDao.getAmountCount(map);
	}
	public List<Map> getSheBaoForReport1(Map map){
		return shebaoDao.getSheBaoForReport1(map);
	}
	public List<Map> getSheBaoForReport2(Map map){
		return shebaoDao.getSheBaoForReport2(map);
	}
	
	public  List<Map> getEveryAmount(Map map){
		return shebaoDao.getEveryAmount(map);
	}
	
	public List<Map> getCount(Map map){
		return shebaoDao.getCount(map);
	}
	
	/*
	 * 得到最近一家公司名称
	 * */
	public List<Map> getRecentCompany(Map map){
		return shebaoDao.getRecentCompany(map);
	}
	
	public List<Map> getRecentPayFeedBase(Map map){
		return shebaoDao.getRecentPayFeedBase(map);
	}
}
