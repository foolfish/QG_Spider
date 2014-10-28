package com.lkb.serviceImp;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.OrderItem;
import com.lkb.daoImp.OrderItemDaoImp;
import com.lkb.service.IOrderItemService;



@Service
@Transactional
public class OrderItemServiceImpl implements IOrderItemService {

	@Resource
	private OrderItemDaoImp OrderItemModel;
	
	@Override
	public OrderItem findById(String id) {
		
		OrderItem OrderItem = OrderItemModel.findById(id);
		return OrderItem; 
	}

	@Override
	public  void saveOrderItem(OrderItem OrderItem) {	
		try {
			OrderItemModel.saveOrderItem(OrderItem);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteOrderItem(String id) {	
		try {
			OrderItemModel.deleteOrderItem(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void  update(OrderItem OrderItem){
		
		OrderItemModel.update(OrderItem);
	}
	

}
