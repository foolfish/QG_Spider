package com.lkb.daoImp;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.lkb.bean.Order;
import com.lkb.bean.PayInfo;
import com.lkb.dao.IPayInfoDao;


@Service
public class PayInfoDaoImp {

	@Resource
	private IPayInfoDao payInfoDao;
	
	public PayInfo findById(String id){
		return payInfoDao.findById(id);
	}
	
	public void savePayInfo(PayInfo payInfo){
		 payInfoDao.savePayInfo(payInfo);
	}
	
	public void deletePayInfo(String id){
		payInfoDao.deletePayInfo(id);
	}
	
	/*
	 * 根据支付编号和来源确定支付信息
	 * */
	public List<PayInfo> getPayInfoByTradeNoSource(Map map){
		return payInfoDao.getPayInfoByTradeNoSource(map);
	}
	
	/*
	 * 根据用户登录名和来源确定支付信息
	 * */
	public List<PayInfo> getPayInfoByBaseUserIdSource(Map map){
		return payInfoDao.getPayInfoByBaseUserIdSource(map);
	}
	
	public void  update(PayInfo payInfo){
		payInfoDao.update(payInfo);
	}
	
	
	public List<Map> getAmountCountLt(Map map){
		return payInfoDao.getAmountCountLt(map);
	}


	public List<Map> getAmountCountGt(Map map){
		return payInfoDao.getAmountCountGt(map);
	}
	
	public List<Map>  getFirstDay(Map map){
		return payInfoDao.getFirstDay(map);
	}
	
	/*
	 * 最近一次支付时间
	 * */
	public List<Map>  getRecentDay(Map map){
		return payInfoDao.getRecentDay(map);
	}
	/*
	 * 代付金额和次数
	 * */
	public List<Map> getamountcount(Map map){
		return payInfoDao.getamountcount(map);
	}

	/*
	 * 支付的最大金额
	 * */
	public List<Map> getLargeAmount(Map map){
		return payInfoDao.getLargeAmount(map);
	}

	/*
	 * 支付次数
	 * */
	public List<Map>  getAllSum(Map map){
		return payInfoDao.getAllSum(map);
	}
	
	public List<Map> getEveryAmount(Map map){
		return payInfoDao.getEveryAmount(map);
	}
	
	public List getMaxOrderAssignTime(PayInfo order){
		return payInfoDao.getMaxOrderAssignTime(order);
	}
	public void insertbatch(List vctList){		
		payInfoDao.insertbatch(vctList);
	}
}
