package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.ZhengxinSummary;

public interface IZhengxinSummaryService {

	ZhengxinSummary findById(String id);

	void saveZhengxinSummary(ZhengxinSummary summary);

	void deleteZhengxinSummary(String id);

	void update(ZhengxinSummary summary);

	List<ZhengxinSummary> getZhengxinSummary(Map map);
	
	 List<ZhengxinSummary> getZhengxinSummaryByLoginName(String loginName);

}