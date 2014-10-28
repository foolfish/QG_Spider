package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinTel;

public interface IDianXinTelDao {
	
	DianXinTel findById(String id);
	
	void saveDianXinTel(DianXinTel dianXinTel);
	
	void deleteDianXinTel(String id);
	
	void  update(DianXinTel dianXinTel);
	
	
	
	/*
	 * 根据手机号和时间来确定一条记录
	 * */
	List<Map> getDianXinTelBybc(Map map);
	List<Map> getFormatCtime(String phone);
	
	List<Map> getEveryAmount(Map map);
	List<Map> getDianXinTelForReport1(Map map);
	
	public DianXinTel getMaxAccountTime(DianXinTel dianXinTel);
	public List getMaxNumTel(Map<String,Object> map);
	/*
	 *批量插入
	 * */
	void insertbatch(List<DianXinTel> list);
	
}
