package com.lkb.daoImp;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.lkb.bean.YqMa;
import com.lkb.dao.IYqMaDao;


@Service
public class YqMaDaoImp {

	@Resource
	private IYqMaDao yqMaDao;
	
	public YqMa get(String random){
		return yqMaDao.get(random);
	}

	public List<YqMa> getAll(){
		return yqMaDao.getAll();
	}

	public void save(YqMa yqMa){
		yqMaDao.save( yqMa);
	}

	public void  update(YqMa yqMa){
		yqMaDao.update( yqMa);
	}
	

}
