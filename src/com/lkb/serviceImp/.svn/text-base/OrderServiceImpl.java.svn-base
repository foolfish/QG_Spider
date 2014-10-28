package com.lkb.serviceImp;


import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.Order;
import com.lkb.constant.Constant;
import com.lkb.daoImp.OrderDaoImp;
import com.lkb.service.IOrderService;



@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

	@Resource
	private OrderDaoImp orderModel;
	
	@Override
	public Order findById(String id) {
		
		Order Order = orderModel.findById(id);
		return Order; 
	}

	@Override
	public  void saveOrder(Order order) {	
		try {
			orderModel.saveOrder(order);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteOrder(String id) {	
		try {
			orderModel.deleteOrder(id);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public List<Order> getOrderByOrderIdsource(Map map){
		return orderModel.getOrderByOrderIdsource(map);
	}
	@Override
	public void  update(Order order){
		
		orderModel.update(order);
	}
	
	@Override
	public  List<Order> getOrderByLoginNamesource(Map map){
		return orderModel.getOrderByLoginNamesource(map);
	}
	
	@Override
	public List<Order> getOrderByOrderIdSourcePid(Map map){
		return orderModel.getOrderByOrderIdSourcePid(map);
	}
	
	@Override
	public List<Map> getAddrByReceiver(Map map){
		return orderModel.getAddrByReceiver(map);
	}
	
	@Override
	public List<Map> getAmountCount(Map map){
		return orderModel.getAmountCount(map);
	}
	@Override
	public List<Map> getTransactionByName(Map map){
		return orderModel.getTransactionByName(map);
	}

	@Override
	public List<Map> getTransactionByAddr(Map map){
		return orderModel.getTransactionByAddr(map);
	}
	@Override
	public int getPerAll(Map map){
		return orderModel.getPerAll(map);
	}

	@Override
	public int getPerPart(Map map){
		return orderModel.getPerPart(map);
	}
	
	@Override
	public List<Map> getSomeInfo(Map map){
		return orderModel.getSomeInfo(map);
	}
	
	/*
	 * 得到注册日期
	 * */
	@Override
	public List<Map> getFirstDay(Map map){
		return orderModel.getFirstDay(map);
	}
	
	@Override
	public List<Map>  getFirstDays(Map map){
		return orderModel.getFirstDays(map);
	}
	
	
	/*
	 * 得到不同来源订单的最近一天
	 * */
	@Override
	public List<Map>  getRencentDays(Map map){
		return orderModel.getRencentDays(map);
	}
	

	/*
	 * 得到所有订单最大值，最小值，总和
	 * */
	@Override
	public List<Map> getAllMaxMin(Map map){
		return orderModel.getAllMaxMin(map);
	}
	
	
	/*
	 * 得到一年内订单的总和
	 * */
	@Override
	public List<Map> getPerMaxMin(Map map){
		return orderModel.getPerMaxMin(map);
	}
	@Override
	public List<Map> getEveryAmount(Map map){
		return orderModel.getEveryAmount(map);
	}
	
	/*
	 * 得到最近的订单的时间
	 * */
	@Override
	public List<Map> getRecentDay(Map map){
		return orderModel.getRecentDay(map);
	}
	
	@Override
	public List<Map> getOrderAllByLoginNamesource(Map map){
		return orderModel.getOrderAllByLoginNamesource(map);
	}
	public Order getMaxOrderTime(Order order){
		 return orderModel.getMaxOrderTime(order);
	 }
	public List getMaxOrderAssignTime(Order order){
		return orderModel.getMaxOrderAssignTime(order);
	}
	public void insertbatch(List<Order> vctList){
		int batchAmount = Constant.batchAmount;
		int vsize =  vctList.size();
		if(vctList!=null&&vsize>0){
			int amount = vsize/batchAmount;
			if(amount>=1){
				for(int j=0;j<amount+1;j++){
					int size = (j+1)*batchAmount;
					if(size>vsize){
						size = vsize;
					}
					List list = vctList.subList(j*batchAmount, size);
					if(list!=null && list.size()>0){
						orderModel.insertbatch(list);
					}	
				}				
			}else{
				orderModel.insertbatch(vctList);
			}	
		}
		
	}
}
