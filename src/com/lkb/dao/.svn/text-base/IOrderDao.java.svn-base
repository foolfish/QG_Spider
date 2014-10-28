package com.lkb.dao;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.lkb.bean.Order;


public interface IOrderDao {
	
	Order findById(String id);
	
	void saveOrder(Order order);
	
	void deleteOrder(String id);
	
	List<Order> getOrderByOrderIdsource(Map map);
	
	void  update(Order order);
	
	/*
	 * 根据登录名和来源取得订单列表
	 * */
	List<Order> getOrderByLoginNamesource(Map map);
	
	List<Order> getOrderByOrderIdSourcePid(Map map);
	
	
	/*
	 * 根据接收人姓名得到住址
	 * */
	List<Map> getAddrByReceiver(Map map);
	
	/*
	 * 得到当前用户的订单的价格总和，消费次数
	 * */
	List<Map> getAmountCount(Map map);
	
	/*
	 * 根据姓名得到交易记录
	 * */
	List<Map> getTransactionByName(Map map);

	/*
	 * 根据地址得到交易记录
	 * */
	List<Map> getTransactionByAddr(Map map);
	
	/*
	 * 得到某一用户所有的订单数
	 * */
	int getPerAll(Map map);

	/*
	 * 得到某一用户和真实姓名一样的订单数
	 * */
	int getPerPart(Map map);
	
	/*
	 * 得到消费金融，笔数和最大金额
	 * */
	List<Map> getSomeInfo(Map map);
	
	/*
	 * 得到注册日期
	 * */
	List<Map> getFirstDay(Map map);
	/*
	 * 得到不同来源订单的第一天
	 * */
	List<Map>  getFirstDays(Map map);
	
	/*
	 * 得到不同来源订单的最近一天
	 * */
	List<Map>  getRencentDays(Map map);

	/*
	 * 得到所有订单最大值，最小值，总和
	 * */
	List<Map> getAllMaxMin(Map map);
	
	/*
	 * 得到一年内订单的总和
	 * */
	List<Map> getPerMaxMin(Map map);
	
	/*
	 * 得到每个月的消费金额
	 * */
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
