package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.ZhengxinDetail;

public interface IZhengxinDetailService {

	ZhengxinDetail findById(String id);

	void saveZhengxinDetail(ZhengxinDetail detail);

	void deleteZhengxinDetail(String id);

	void update(ZhengxinDetail detail);

	List<ZhengxinDetail> getZhengxinDetail(Map map);
	
	List<ZhengxinDetail> getZhengxinByLoginName(String loginName);

}