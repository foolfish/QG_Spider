package com.lkb.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileTel;

public interface IMobileTelService {
	MobileTel findById(String id);
	
	void saveMobileTel(MobileTel mobileTel);
	
	void deleteMobileTel(String id);
	
	void  update(MobileTel mobileTel);
	
	List<Map> getMobileTelBybc(Map map);
	List<Map> getFormatCtime(String phone);
	List<Map> getMobileTelForReport1(Map map);
	List<Map> getEveryAmount(Map map);
	public MobileTel getMaxTime(MobileTel mobileTel);
	/***
	 * 获取最大时间
	 * 参数  teleno 电话号
	 * 参数 num  最近几个月
	 * @param map
	 * @return
	 */
	public List getMaxNumTel(Map<String,Object> map);
	void insertbatch(List<MobileTel> list);
}
