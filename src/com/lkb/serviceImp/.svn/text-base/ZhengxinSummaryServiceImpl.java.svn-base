package com.lkb.serviceImp;




import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.ZhengxinSummary;
import com.lkb.daoImp.ZhengxinSummaryDaoImpl;
import com.lkb.service.IZhengxinSummaryService;






@Service
@Transactional
public class ZhengxinSummaryServiceImpl implements IZhengxinSummaryService {

	@Resource
	private ZhengxinSummaryDaoImpl zxSummaryModel;
	

	
	@Override
	public ZhengxinSummary findById(String id) {
		
		 return zxSummaryModel.findById(id);
		
	}

	@Override
	public  void saveZhengxinSummary(ZhengxinSummary summary) {	
		try {
			zxSummaryModel.saveZhengxinSummary(summary);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteZhengxinSummary(String id) {	
		try {
			zxSummaryModel.deleteZhengxinSummary(id);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	
	@Override
	public void  update(ZhengxinSummary summary){
		
		zxSummaryModel.update(summary);
	}
	
	@Override
	public List<ZhengxinSummary> getZhengxinSummary(Map map){
		return zxSummaryModel.getZhengxinSummary(map);
	}
	@Override
	public List<ZhengxinSummary> getZhengxinSummaryByLoginName(String loginName){
		return zxSummaryModel.getZhengxinSummaryByLoginName(loginName);
	}
	

}
