package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileTel;

public interface IMobileTelDao {
	
	MobileTel findById(String id);
	
	void saveMobileTel(MobileTel mobileTel);
	
	void deleteMobileTel(String id);
	
	void  update(MobileTel mobileTel);
	
	
	
	/*
	 * 根据baseUserId和时间来确定一条记录
	 * */
	List<Map> getMobileTelBybc(Map map);
	List<Map> getFormatCtime(String phone);
	List<Map> getMobileTelForReport1(Map map);
	
	List<Map> getEveryAmount(Map map);
	public MobileTel getMaxAccountTime(MobileTel mobileTel);
	public List getMaxNumTel(Map<String,Object> map);
	/*
	 *批量插入
	 * */
	void insertbatch(List<MobileTel> list);
}
