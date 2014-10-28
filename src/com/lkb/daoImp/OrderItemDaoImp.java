package com.lkb.daoImp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.OrderItem;
import com.lkb.dao.IOrderItemDao;



@Service
public class OrderItemDaoImp {

	@Resource
	private IOrderItemDao OrderItemDao;
	
	public OrderItem findById(String id){
		return OrderItemDao.findById(id);
	}
	
	
	public void saveOrderItem(OrderItem OrderItem){
		OrderItemDao.saveOrderItem(OrderItem);
	}
	
	public void deleteOrderItem(String id){
		OrderItemDao.deleteOrderItem(id);
	}
	
	
	public void  update(OrderItem OrderItem){
		
		OrderItemDao.update(OrderItem);
	}

	
	

}
