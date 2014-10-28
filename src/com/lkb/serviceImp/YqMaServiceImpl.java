package com.lkb.serviceImp;


import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.YqMa;
import com.lkb.daoImp.YqMaDaoImp;
import com.lkb.service.IYqMaService;

@Service
@Transactional
public class YqMaServiceImpl implements IYqMaService {

	@Resource
	private YqMaDaoImp yqMaModel;

	@Override
	public YqMa get(String random) {
		// TODO Auto-generated method stub
		return yqMaModel.get(random);
	}

	@Override
	public List<YqMa> getAll() {
		// TODO Auto-generated method stub
		return yqMaModel.getAll();
	}

	@Override
	public void save(YqMa yqMa) {
		yqMaModel.save(yqMa);
	}

	@Override
	public void update(YqMa yqMa) {
		yqMaModel.update(yqMa);
	}
}


