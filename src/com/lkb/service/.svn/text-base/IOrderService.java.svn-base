package com.lkb.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.Order;

public interface IOrderService {

	Order findById(String orderId);

	void saveOrder(Order order);

	void deleteOrder(String orderId);

	List<Order> getOrderByOrderIdsource(Map map);

	void update(Order order);

	List<Order> getOrderByLoginNamesource(Map map);

	List<Order> getOrderByOrderIdSourcePid(Map map);

	List<Map> getAddrByReceiver(Map map);

	List<Map> getAmountCount(Map map);

	List<Map> getTransactionByName(Map map);

	/*
	 * 根据地址得到交易记录
	 */
	List<Map> getTransactionByAddr(Map map);

	int getPerAll(Map map);

	/*
	 * 得到某一用户和真实姓名一样的订单数
	 */
	int getPerPart(Map map);

	List<Map> getSomeInfo(Map map);

	/*
	 * 得到注册日期
	 */
	List<Map> getFirstDay(Map map);

	List<Map> getFirstDays(Map map);

	/*
	 * 得到不同来源订单的最近一天
	 */
	List<Map> getRencentDays(Map map);

	/*
	 * 得到所有订单最大值，最小值，总和
	 */
	List<Map> getAllMaxMin(Map map);

	/*
	 * 得到一年内订单的总和
	 */
	List<Map> getPerMaxMin(Map map);

	 List<Map> getEveryAmount(Map map);
	 
	 /*
		 * 得到最近的订单的时间
		 * */
	 List<Map> getRecentDay(Map map);
	 
	 List<Map> getOrderAllByLoginNamesource(Map map);
	
	 
	 Order getMaxOrderTime(Order order);
	 public List getMaxOrderAssignTime(Order order);
	 
	 void insertbatch(List<Order> vctList);
}
