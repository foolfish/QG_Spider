package com.lkb.daoImp;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.Order;
import com.lkb.dao.IOrderDao;



@Service
public class OrderDaoImp {

	@Resource
	private IOrderDao orderDao;
	
	public Order findById(String id){
		return orderDao.findById(id);
	}
	
	
	public void saveOrder(Order order){
		orderDao.saveOrder(order);
	}
	
	public void deleteOrder(String id){
		orderDao.deleteOrder(id);
	}
	
	public List<Order> getOrderByOrderIdsource(Map map){
		return orderDao.getOrderByOrderIdsource(map);
	}
	
	public void  update(Order order){
		
		orderDao.update(order);
	}
	
	
	public  List<Order> getOrderByLoginNamesource(Map map){
		return orderDao.getOrderByLoginNamesource(map);
	}
	
	
	public List<Order> getOrderByOrderIdSourcePid(Map map){
		return orderDao.getOrderByOrderIdSourcePid(map);
	}
	
	public List<Map> getAddrByReceiver(Map map){
		return orderDao.getAddrByReceiver(map);
	}
	
	public List<Map> getAmountCount(Map map){
		return orderDao.getAmountCount(map);
	}
	
	public List<Map> getTransactionByName(Map map){
		return orderDao.getTransactionByName(map);
	}

	/*
	 * 根据地址得到交易记录
	 * */
	public List<Map> getTransactionByAddr(Map map){
		return orderDao.getTransactionByAddr(map);
	}
	
	public int getPerAll(Map map){
		return orderDao.getPerAll(map);
	}

	/*
	 * 得到某一用户和真实姓名一样的订单数
	 * */
	public int getPerPart(Map map){
		return orderDao.getPerPart(map);
	}
	
	
	public List<Map> getSomeInfo(Map map){
		return orderDao.getSomeInfo(map);
	}
	
	/*
	 * 得到注册日期
	 * */
	public List<Map> getFirstDay(Map map){
		return orderDao.getFirstDay(map);
	}
	
	public List<Map>  getFirstDays(Map map){
		return orderDao.getFirstDays(map);
	}
	
	
	/*
	 * 得到不同来源订单的最近一天
	 * */
	public List<Map>  getRencentDays(Map map){
		return orderDao.getRencentDays(map);
	}
	

	/*
	 * 得到所有订单最大值，最小值，总和
	 * */
	public List<Map> getAllMaxMin(Map map){
		return orderDao.getAllMaxMin(map);
	}
	
	
	/*
	 * 得到一年内订单的总和
	 * */
	public List<Map> getPerMaxMin(Map map){
		return orderDao.getPerMaxMin(map);
	}
	
	
	public List<Map> getEveryAmount(Map map){
		return orderDao.getEveryAmount(map);
	}
	
	/*
	 * 根据用户名和订单来源获取最近一条记录的时间
	 * */
	public List<Map> getRecentDay(Map map){
		return orderDao.getRecentDay(map);
	}
	
	public List<Map> getOrderAllByLoginNamesource(Map map){
		return orderDao.getOrderAllByLoginNamesource(map);
	}
	public Order  getMaxOrderTime(Order order){
		return orderDao.getMaxOrderTime(order);
	}
	public List getMaxOrderAssignTime(Order order){
		return orderDao.getMaxOrderAssignTime(order);
	}
	public void insertbatch(List<Order> vctList){		
		orderDao.insertbatch(vctList);
	}
}
