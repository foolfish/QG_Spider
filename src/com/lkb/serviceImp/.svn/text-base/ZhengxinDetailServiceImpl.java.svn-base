package com.lkb.serviceImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.ZhengxinDetail;
import com.lkb.daoImp.ZhengxinDetailDaoImpl;
import com.lkb.service.IZhengxinDetailService;

@Service
@Transactional
public class ZhengxinDetailServiceImpl implements IZhengxinDetailService  {

	@Resource
	private ZhengxinDetailDaoImpl zxDetailModel;
	

	@Override
	public ZhengxinDetail findById(String id) {
		
		 return zxDetailModel.findById(id);
		
	}

	@Override
	public  void saveZhengxinDetail(ZhengxinDetail detail) {	
		try {
			zxDetailModel.saveZhengxinDetail(detail);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public void deleteZhengxinDetail(String id) {	
		try {
			zxDetailModel.deleteZhengxinDetail(id);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	

	@Override
	public void  update(ZhengxinDetail detail){		
		zxDetailModel.update(detail);
	}

	@Override
	public List<ZhengxinDetail> getZhengxinDetail(Map map){
		return zxDetailModel.getZhengxinDetail(map);
	}
	@Override
	public List<ZhengxinDetail> getZhengxinByLoginName(String loginName){
		return zxDetailModel.getZhengxinByLoginName(loginName);
	}

}
