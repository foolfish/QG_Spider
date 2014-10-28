package com.lkb.service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinTel;

public interface IDianXinTelService {
	DianXinTel findById(String id);
	
	void saveDianXinTel(DianXinTel dianXinTel);
	
	void deleteDianXinTel(String id);
	
	
	void  update(DianXinTel dianXinTel);
	List<Map> getDianXinTelBybc(Map map);
	List<Map> getFormatCtime(String phone);
	List<Map> getDianXinTelForReport1(Map map);
	List<Map> getEveryAmount(Map map);
	
	public DianXinTel getMaxTime(DianXinTel dianXinTel);
	/***
	 * 获取最大时间
	 * 参数  teleno 电话号
	 * 参数 num  最近几个月
	 * @param map
	 * @return
	 */
	public List getMaxNumTel(Map<String,Object> map);
	void insertbatch(List<DianXinTel> list);
	

}
