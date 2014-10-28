package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.Warning;
import com.lkb.daoImp.WarningDaoImp;
import com.lkb.service.IWarningService;

@Service
@Transactional
public class WarningServiceImpl implements IWarningService {

	@Resource
	private WarningDaoImp warningModel;
	
	@Override
	public Warning findById(String warningId) {
		
		Warning warning = warningModel.findById(warningId);
		return warning;
	}

	@Override
	public  void saveWarning(Warning warning) {	
		try {
			warningModel.saveWarning(warning);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteWarning(String warningId) {	
		try {
			warningModel.deleteWarning(warningId);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public List<Warning> getWarningByType(Map map){
		
		List<Warning> list = warningModel.getWarningByType(map);		
		
		return list;
		
	}
	@Override
	public List<Warning> getWarningByFlag(Map map){
		
		List<Warning> list = warningModel.getWarningByFlag(map);		
		
		return list;
		
	}
	
	@Override
	public void  update(Warning warning){
		warningModel.update(warning);
	}
	


}
