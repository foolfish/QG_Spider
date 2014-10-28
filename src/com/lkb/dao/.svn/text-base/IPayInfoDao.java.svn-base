package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.Order;
import com.lkb.bean.PayInfo;


public interface IPayInfoDao {
	
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
	
	/*
	 * 小于0的交易完成的金额和次数
	 * */
	List<Map> getAmountCountLt(Map map);

	/*
	 * 大于0的交易完成的金额和次数
	 * */
	List<Map> getAmountCountGt(Map map);
	
	/*
	 * 注册时间
	 * */
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
	
	
	/*
	 * 获得当月的消费总额
	 * */
	List<Map> getEveryAmount(Map map);
	
	 public List getMaxOrderAssignTime(PayInfo order);
	 void insertbatch(List vctList);
}
