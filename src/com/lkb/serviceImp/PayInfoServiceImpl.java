package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.Order;
import com.lkb.bean.PayInfo;
import com.lkb.constant.Constant;
import com.lkb.daoImp.PayInfoDaoImp;
import com.lkb.service.IPayInfoService;


@Service
@Transactional
public class PayInfoServiceImpl implements IPayInfoService {

	@Resource
	private PayInfoDaoImp payInfoModel;

	@Override
	public PayInfo findById(String id) {
		// TODO Auto-generated method stub
		return payInfoModel.findById(id);
	}

	@Override
	public void savePayInfo(PayInfo payInfo) {
		// TODO Auto-generated method stub
		payInfoModel.savePayInfo(payInfo);
	}

	@Override
	public void deletePayInfo(String id) {
		// TODO Auto-generated method stub
		payInfoModel.deletePayInfo(id);
	}

	@Override
	public List<PayInfo> getPayInfoByTradeNoSource(Map map) {
		// TODO Auto-generated method stub
		return payInfoModel.getPayInfoByTradeNoSource(map);
	}

	@Override
	public List<PayInfo> getPayInfoByBaseUserIdSource(Map map) {
		// TODO Auto-generated method stub
		return payInfoModel.getPayInfoByBaseUserIdSource(map);
	}

	@Override
	public void update(PayInfo payInfo) {
		// TODO Auto-generated method stub
		payInfoModel.update(payInfo);
	}
	
	@Override
	public List<Map> getAmountCountLt(Map map){
		// TODO Auto-generated method stub
		return payInfoModel.getAmountCountLt(map);
	}

	@Override
	public List<Map> getAmountCountGt(Map map){
		// TODO Auto-generated method stub
		return payInfoModel.getAmountCountGt(map);
	}
	
	@Override
	public List<Map>  getFirstDay(Map map){
		return payInfoModel.getFirstDay(map);
	}
	
	@Override
	public List<Map>  getRecentDay(Map map){
		return payInfoModel.getRecentDay(map);
	}
	@Override
	public List<Map> getamountcount(Map map){
		return payInfoModel.getamountcount(map);
	}

	@Override
	public List<Map> getLargeAmount(Map map){
		return payInfoModel.getLargeAmount(map);
	}

	@Override
	public List<Map>  getAllSum(Map map){
		return payInfoModel.getAllSum(map);
	}
	
	@Override
	public List<Map> getEveryAmount(Map map){
		return payInfoModel.getEveryAmount(map);
	}
	public List getMaxOrderAssignTime(PayInfo pay){
		return payInfoModel.getMaxOrderAssignTime(pay);
	}
	public void insertbatch(List vctList){
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
						payInfoModel.insertbatch(list);
					}	
				}				
			}else{
				payInfoModel.insertbatch(vctList);
			}	
		}
	}
}
