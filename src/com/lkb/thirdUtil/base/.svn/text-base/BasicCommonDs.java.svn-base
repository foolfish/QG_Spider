package com.lkb.thirdUtil.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


import com.lkb.bean.LoseContent;
import com.lkb.bean.Order;
import com.lkb.bean.OrderItem;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.util.StringUtil;
import com.lkb.util.httpclient.util.CommonUtils;


public abstract class BasicCommonDs extends BasicCommonMobileControl<ResOutput>{
	public BasicCommonDs(Login login, String currentUser) {
		super(login, currentUser);
	}
	public BasicCommonDs(Login login) {
		super(login);
	}
	public LinkedList<Order> listOrder;
	public LinkedList<Order> listUpdateOrder;
	public LinkedList<OrderItem> itemList = null;
	public Order bigOrder;
	public Set<LoseContent> loseList = null;
	public boolean spiderOrderState = true;
	/**order表更新状态的对象*/
	public LoseContent lose;
	/**是否停止采集账单*/
	public boolean isStop = false;
	public static final int orderType = 1;
	public static final int orderItemType = 2;
	public static final String ORDER = "ORDER";
	public static final String ORDERITEM = "ORDERITEM";
	
	
	public Map<String,String> orderMap = null;
	/**1.continue字段 2.break字段,3update修改,0继续执行
	 * 操作之前一定理清楚插入和查询的先后顺序
	 * @param order
	 * @return
	 */
	public int isContinue(Order order){
		int num = 0;
		if(order!=null&&order.getOrderId()!=null){
			//如果lose为空说明没有错误日志
			if(lose==null){
				if(orderMap==null)
					orderMap = getMaxOrderAssignTime(getNewDate(-4));
			}else{
				//如果上次采集失败,那么此次需要重新采集 先获取所有的关键id存入hash
				if(orderMap == null){
					Date d = null;
					Date d1 = getNewDate(-4);
					d = d1.getTime()>=lose.getCode()?new Date(lose.getCode()):d1;//取最小的时间
					orderMap = getMaxOrderAssignTime(d);
				}
			}
			num = getContinueCode(order);
		}
		return num;
		
	}
	/**1继续跳过本次循环,2终止执行,3update修改,0继续执行
	 * @param order
	 * @return
	 */
	public int getContinueCode(Order order){
		Long orderid = Long.parseLong(order.getOrderId());
		int m = 0;
		//判断是否需要加入正常的保存队列,存在在抛出
		if(orderMap.containsKey(order.getOrderId())){
			if(order.getOrderstatus()!=null){
				if(orderMap.get(order.getOrderId()).equals(order.getOrderstatus())){
					m = 1;	//如果value相等说明账单没有改变,继续执行
				}else{
					m = 3;	//执行update操作
				}
			}
		}else{
			//检查错误的标示码 0正常,说明上一次采集任务为正常的,因为京东默认为倒序所以到此步的时候可以break;
			if(bigOrder!=null&&Long.parseLong(bigOrder.getOrderId())>=orderid){
				m = 2;
			}
		}
		return m;
	}

//	public boolean isLastSpiderState(){
//		boolean b = false;
//		if(lose!=null){
//			//检查错误的标示码 0正常,说明上一次采集任务为正常的,因为京东默认为倒序所以到此步的时候可以break;
//			if(lose.getErrorCode()==0){
//				b = true;
//			}
//		}
//		return b;
//	}
	
	/**type=2 针对orderItem
	 * 如果该错误地址5次更新都错误,则需要剔除
	 * @param url
	 * @param count  该url被执行的次数,其他的话忽略
	 * @param type	  标识字段
	 */
	public void createLoseContent(String url,int count,int type){
		LoseContent lose = new LoseContent();
		lose.setUrl(url);
		lose.setUserSource(this.userSource);
		lose.setLoginName(this.loginName);
		lose.setId(UUID.randomUUID().toString());
		lose.setType(type);
		lose.setUpdateTime(new Date());
		lose.setCount(count);
		if(count<5){
			loseList.add(lose);			
		}

	}
	public Order getOrder(String orderId){
		Map map = new HashMap();
		map.put("ordersource",this.userSource);
		map.put("orderId", orderId);
		List<Order> list = service.getOrderService().getOrderByOrderIdsource(map);
		if(CommonUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	/**比如4  就是查询四月前到现在的所有order
	 * @param beforeMonth n月之前
	 * @return
	 */
	public Date getNewDate(int month){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, month); //默认四个月前的数据
		return cal.getTime();
	}
	
	public Map<String,String> getMaxOrderAssignTime(Date time){
		if(isTest()){
			return null;
		}
		Order order = new Order();
		order.setLoginName(this.loginName);
		order.setOrdersource(this.userSource);
		order.setBuyTime(time);
		Map<String, String> map = null;
		try{
			List<Object> list =  service.getOrderService().getMaxOrderAssignTime(order);
			String key = null;
			String value = null;
			map = new LinkedHashMap<String, String>();
			if(CommonUtils.isNotEmpty(list)){
				int size = list.size();
				for (int i = 0; i < size; i++) {
					value = StringUtil.subStr("=", ",",list.get(i).toString());
					key = StringUtil.subStr("orderid=", "}",list.get(i).toString());
					if(i==(size-1)){
						bigOrder = new Order();
						bigOrder.setOrderId(key);
						bigOrder.setOrderstatus(value);
					}
					map.put(key, value);
				}
//				System.out.println(list);
			}
		}catch(Exception e){
			log4j.error(e);
		}
		return map;
	}
	public List getMaxLoseTime(int type){
		if(isTest()){
			return null;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("loginName", this.loginName);
		map.put("type",type);
		map.put("userSource",this.userSource);
		try{
			if(!isTest()){
				List list = service.getLoseContentService().getAll(map);
				if(CommonUtils.isNotEmpty(list)){
					return list;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 删除错误记录
	 */
	public void deleteType(int type){
		LoseContent lose = new LoseContent();
		lose.setUserSource(this.userSource);
		lose.setLoginName(this.loginName);
		lose.setType(type);
		service.getLoseContentService().delete(lose);
	}
	public abstract void gatherHistoryLoseURL();
	public abstract void gatherOrder();
	
	public void saveAll(){
		listOrder = new LinkedList<Order>();
		itemList = new LinkedList<OrderItem>();
		loseList =  new HashSet<LoseContent>();
		listUpdateOrder = new LinkedList<Order>();
		try{
			List lo= getMaxLoseTime(orderType);
			if(CommonUtils.isNotEmpty(lo)){
				lose = (LoseContent) lo.get(0);
			}
			//------------更新最新结果-------------
			this.gatherHistoryLoseURL();
			//采集最新的结果
			this.gatherOrder();
			
			if(!isTest()){
				
				service.getOrderService().insertbatch(listOrder);
				for (OrderItem orderItem : itemList) {
					service.getOrderItemService().saveOrderItem(orderItem);
				}
				//更新修改订单
				if(CommonUtils.isNotEmpty(listUpdateOrder)){
					for (Order order : listUpdateOrder) {
						Order order1 = getOrder(order.getOrderId());
						if(order1!=null){
							order.setId(order1.getId());
							service.getOrderService().update(order1);
						}
					}
				}
				long code = 0;
				if(bigOrder!=null){
					code = Long.parseLong(bigOrder.getOrderId());
//					code = bigOrder.getBuyTime().getTime();//初始值	
				}
				saveOrDeleteLostOne(orderType,code);
				saveLoseList(loseList);
			
			}
		}catch(Exception e){
			log4j.writeLogOrder("",e,"BAOCUN");
		}
	}
	/**保存或者删除标示状态 
	 * @param type 标示值
	 * 修改值,可以是时间或者订单
	 */
	public void saveOrDeleteLostOne(int type,long code){
		if(!isTest()){
			//只会执行一次
			if(!spiderOrderState){
				if(lose==null){
					lose = new LoseContent();
					lose.setId(UUID.randomUUID().toString());
					lose.setUserSource(this.userSource);
					lose.setLoginName(this.loginName);
					lose.setUpdateTime(new Date());
					lose.setType(type);
					lose.setErrorCode(1);
//					if(bigOrder!=null){
//						lose.setCode(bigOrder.getBuyTime().getTime());//初始值	
//					}else{
						lose.setCode(code);//初始值	
//					}
					service.getLoseContentService().save(lose);
				}
			}else{
				if(lose!=null){
					service.getLoseContentService().delete(lose);	
				}
			}
		}
		
	}
	/**批量保存记录
	 * @param coll
	 */
	public void saveLoseList(Collection coll){
		if(!isTest()){
			if(CommonUtils.isNotEmpty(coll)){
				List<LoseContent> list = null;
				if(coll instanceof List){
					list = (List<LoseContent>) coll;
				}else{
					list = new ArrayList<LoseContent>();
					list.addAll(coll);
				}
				service.getLoseContentService().insertbatch(list);
			}
		}
	}
}
