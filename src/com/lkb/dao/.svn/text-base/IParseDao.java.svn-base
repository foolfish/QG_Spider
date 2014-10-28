package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.Parse;


public interface IParseDao {
	
	Parse get(Long id);

	/*
	 * 保存parse
	 * */
	void save(Parse parse);
	
	/*
	 * 更新parse
	 * */
	void  update(Parse parse);
	
	
	/*
	 * 根据userId,loginName,source确定一条记录
	 * */
	List<Parse> getParseBySome(Map map);

	/*
	 * 获取抓取状态
	 * */
	Map getStatus(Map map);
}
