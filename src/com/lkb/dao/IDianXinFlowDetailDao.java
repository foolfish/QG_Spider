package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.DianXinFlowDetail;

/**
* <p>Title: IDianXinFlowDetailDao</p>
* <p>Description: 电信流量详单dao</p>
* <p>Company: QuantGroup</p> 
* @author Jerry Sun
* @date 2014-10-8
*/
public interface IDianXinFlowDetailDao {
	
	/**
	* <p>Title: findById</p>
	* <p>Description: 通过id查询记录</p>
	* @author Jerry Sun
	* @param id
	* @return
	*/
	DianXinFlowDetail findById(String id);
	
	/**
	* <p>Title: saveDianXinFlowDetail</p>
	* <p>Description: 保存流量详单</p>
	* @author Jerry Sun
	* @param dianXinFlowDetail
	*/
	void saveDianXinFlowDetail(DianXinFlowDetail dianXinFlowDetail);
	
	/**
	* <p>Title: deleteDianXinFlowDetail</p>
	* <p>Description: 删除流量详单</p>
	* @author Jerry Sun
	* @param id
	*/
	void deleteDianXinFlowDetail(String id);
	
	/**
	* <p>Title: update</p>
	* <p>Description: 更新流量详单</p>
	* @author Jerry Sun
	* @param dianXinFlowDetail
	*/
	void update(DianXinFlowDetail dianXinFlowDetail);
	
	/*
	 * 根据手机号和时间判断是否一条记录
	 * */
	List<Map> getDianXinFlowDetailBypt(Map map);
//	List<Map> getDianXinFlowDetailForReport(Map map);
//	List<Map> getDianXinFlowDetailForReport2(Map map);
	
	/**
	* <p>Title: getMaxFlowTime</p>
	* <p>Description: 获取当前数据库中的最新记录</p>
	* @author Jerry Sun
	* @param phone
	* @return
	*/
	DianXinFlowDetail getMaxFlowTime(String phone);
	/**
	* <p>Title: getDianXinFlowDetail</p>
	* <p>Description: 批量获取某个phone的流量记录</p>
	* @author Jerry Sun
	* @param phone
	* @return
	*/
	List<Map> getDianXinFlowDetail(String phone);
	List<Map> getDianXinFlowDetailForReport(String phone);
	void insertbatch(List<DianXinFlowDetail> list);
}
