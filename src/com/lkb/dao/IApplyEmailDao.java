package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.ApplyEmail;


public interface IApplyEmailDao {
	
	ApplyEmail get(Long id);
	/**
	 *返回所有 
	 * @return
	 */
	List<ApplyEmail> getAll();

	/*
	 * 保存email
	 * */
	void save(ApplyEmail applyEmail);
	
	/*
	 * 更新email
	 * */
	void  update(ApplyEmail applyEmail);
	
	
	/*
	 * 根据email和type确定记录值
	 * */
	List<ApplyEmail> getApplyEmailEt(Map map);

}
