package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.SheBao;



public interface ISheBaoDao {
	
	SheBao findById(String id);
	
	void saveSheBao(SheBao shebao);
	
	void deleteSheBao(String id);
	
	List<SheBao> getSheBaoByBaseUseridsource(Map map);
	
	void  update(SheBao shebao);
	
	/*
	 * 得到社保的缴费数据
	 * */
	List<Map> getAmountCount(Map map);
	List<Map> getSheBaoForReport1(Map map);
	List<Map> getSheBaoForReport2(Map map);
	List<Map>  getEveryAmount(Map map);
	/*
	 * 得到缴费月数
	 * */
	List<Map> getCount(Map map);
	
	/*
	 * 得到最近一家公司名称
	 * */
	List<Map> getRecentCompany(Map map);
	
	List<Map> getRecentPayFeedBase(Map map);
}
