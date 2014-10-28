package com.lkb.service;


import java.util.List;
import java.util.Map;

import com.lkb.bean.ApplyEmail;


public interface IApplyEmailService {
	
	ApplyEmail get(Long id);
	/**
	 *返回所有 
	 * @return
	 */
	List<ApplyEmail> getAll();

	
	void save(ApplyEmail applyEmail);
	
	void update(ApplyEmail applyEmail);
	
	List<ApplyEmail> getApplyEmailEt(Map map);
}
