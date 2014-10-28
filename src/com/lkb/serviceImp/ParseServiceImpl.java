package com.lkb.serviceImp;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.Parse;
import com.lkb.daoImp.ParseDaoImp;
import com.lkb.service.IParseService;

@Service
@Transactional
public class ParseServiceImpl implements IParseService {

	@Resource
	private ParseDaoImp parseModel;

	@Override
	public Parse get(Long id) {
		return parseModel.get(id);
	}


	@Override
	public void save(Parse parse) {
		parseModel.save(parse);		
	}

	@Override
	public void update(Parse parse) {
		parseModel.update(parse);
	}
	
	@Override
	public List<Parse> getParseBySome(Map map){
		return parseModel.getParseBySome(map);
	}
	
	@Override
	public Map getStatus(Map map){
		return parseModel.getStatus(map);
	}
	
}
