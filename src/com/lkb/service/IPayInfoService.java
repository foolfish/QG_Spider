package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.Order;
import com.lkb.bean.PayInfo;

public interface IPayInfoService {
	
	PayInfo findById(String id);
	
	void savePayInfo(PayInfo payInfo);
	
	void deletePayInfo(String id);
	
	/*
	 * 根据支付编号和来源确定支付信息
	 * */
	List<PayInfo> getPayInfoByTradeNoSource(Map map);
	
	/*
	 * 根据用户登录名和来源确定支付信息
	 * */
	List<PayInfo> getPayInfoByBaseUserIdSource(Map map);
	
	void  update(PayInfo payInfo);
	
	List<Map> getAmountCountLt(Map map);


	List<Map> getAmountCountGt(Map map);
	
	 List<Map>  getFirstDay(Map map);
	
	/*
	 * 最近一次支付时间
	 * */
	 List<Map>  getRecentDay(Map map);
	/*
	 * 代付金额和次数
	 * */
	 List<Map> getamountcount(Map map);
	/*
	 * 支付的最大金额
	 * */
	 List<Map> getLargeAmount(Map map);
	/*
	 * 支付次数
	 * */
	 List<Map>  getAllSum(Map map);

	 List<Map> getEveryAmount(Map map);

	
	 public List getMaxOrderAssignTime(PayInfo pay);
	 
	 void insertbatch(List vctList);
	
}
