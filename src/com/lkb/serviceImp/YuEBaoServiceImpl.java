package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.PayInfo;
import com.lkb.bean.YuEBao;
import com.lkb.constant.Constant;
import com.lkb.daoImp.YuEBaoDaoImp;
import com.lkb.service.IYuEBaoService;

@Service
@Transactional
public class YuEBaoServiceImpl implements IYuEBaoService {

	@Resource
	private YuEBaoDaoImp yuEBaoModel;
	
	@Override
	public YuEBao findById(String yuEBaoId) {
		
		YuEBao yuEBao = yuEBaoModel.findById(yuEBaoId);
		return yuEBao;
	}

	@Override
	public  void saveYuEBao(YuEBao yuEBao) {	
		try {
			yuEBaoModel.saveYuEBao(yuEBao);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteYuEBao(String yuEBaoId) {	
		try {
			yuEBaoModel.deleteYuEBao(yuEBaoId);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public List<YuEBao> getYuEBaoByBaseUseridsource(Map map){
		
		List<YuEBao> list = yuEBaoModel.getYuEBaoByBaseUseridsource(map);		
		
		return list;
		
	}
	/**返回(余额宝+邮箱账单) report页面*/
	public Map<String,String> getYuEBaoPersonalStatistical(Map<String,String> canMap){
		 Map<String,String> map= yuEBaoModel.getYuEBaoPersonalStatistical(canMap);		
		 return map;
	}
	
	
	@Override
	public void  update(YuEBao yuEBao){
		yuEBaoModel.update(yuEBao);
	}
	
	@Override
	public List<Map> getEveryAmount(Map map){
		return yuEBaoModel.getEveryAmount(map);
	}

	@Override
	public List<YuEBao> getYuEBaoByAlipay(String alipayName){
		return yuEBaoModel.getYuEBaoByAlipay(alipayName);
	}
	
	public YuEBao getMaxYuEBaoTime(YuEBao bao){
		 return yuEBaoModel.getMaxYuEBaoTime(bao);
	 }
	
	public List getMaxYuEBaoAssignTime(YuEBao bao){
		return yuEBaoModel.getMaxYuEBaoAssignTime(bao);
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
						yuEBaoModel.insertbatch(list);
					}	
				}				
			}else{
				yuEBaoModel.insertbatch(vctList);
			}	
		}
	}
}
