package com.lkb.daoImp;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.lkb.bean.Parse;
import com.lkb.dao.IParseDao;


@Service
public class ParseDaoImp {

	@Resource
	private IParseDao parseDao;
	
	public Parse get(Long id){
		return parseDao.get(id);
	}
	
	public void save(Parse parse){
		parseDao.save(parse);
	}
	
	public void update(Parse parse){
		parseDao.update(parse);
	}
	
	public List<Parse> getParseBySome(Map map){
		return parseDao.getParseBySome(map);
	}
	
	/*
	 * 获取抓取状态
	 * */
	public Map getStatus(Map map){
		return parseDao.getStatus(map);
	}
}
