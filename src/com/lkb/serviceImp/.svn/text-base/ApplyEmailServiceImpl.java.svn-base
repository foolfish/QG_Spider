package com.lkb.serviceImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.ApplyEmail;
import com.lkb.daoImp.ApplyEmailDaoImp;
import com.lkb.service.IApplyEmailService;

@Service
@Transactional
public class ApplyEmailServiceImpl implements IApplyEmailService {

	@Resource
	private ApplyEmailDaoImp applyEmailModel;

	@Override
	public ApplyEmail get(Long id) {
		return applyEmailModel.get(id);
	}

	@Override
	public List<ApplyEmail> getAll() {
		return applyEmailModel.getAll();
	}

	@Override
	public void save(ApplyEmail applyEmail) {
		applyEmailModel.save(applyEmail);		
	}

	@Override
	public void update(ApplyEmail applyEmail) {
		applyEmailModel.update(applyEmail);
	}
	
	@Override
	public List<ApplyEmail> getApplyEmailEt(Map map){
		return applyEmailModel.getApplyEmailEt(map);
	}
	
}
