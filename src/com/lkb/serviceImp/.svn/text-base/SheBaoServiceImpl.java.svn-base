package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.SheBao;
import com.lkb.daoImp.SheBaoDaoImp;
import com.lkb.service.ISheBaoService;

@Service
@Transactional
public class SheBaoServiceImpl implements ISheBaoService {

	@Resource
	private SheBaoDaoImp SheBaoModel;
	
	@Override
	public SheBao findById(String SheBaoId) {
		
		SheBao SheBao = SheBaoModel.findById(SheBaoId);
		return SheBao;
	}

	@Override
	public  void saveSheBao(SheBao SheBao) {	
		try {
			SheBaoModel.saveSheBao(SheBao);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteSheBao(String SheBaoId) {	
		try {
			SheBaoModel.deleteSheBao(SheBaoId);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public List<SheBao> getSheBaoByBaseUseridsource(Map map){
		
		List<SheBao> list = SheBaoModel.getSheBaoByBaseUseridsource(map);		
		
		return list;
		
	}
	
	@Override
	public void  update(SheBao SheBao){
		SheBaoModel.update(SheBao);
	}
	
	@Override
	public List<Map> getAmountCount(Map map){
		return SheBaoModel.getAmountCount(map);
	}
	@Override
	public List<Map> getSheBaoForReport1(Map map) {
		return SheBaoModel.getSheBaoForReport1(map);
	}

	@Override
	public List<Map> getSheBaoForReport2(Map map) {
		return SheBaoModel.getSheBaoForReport2(map);
	}
	@Override
	public  List<Map> getEveryAmount(Map map){
		return SheBaoModel.getEveryAmount(map);
	}
	@Override
	public List<Map> getCount(Map map){
		return SheBaoModel.getCount(map);
	}
	@Override
	public List<Map> getRecentCompany(Map map){
		return SheBaoModel.getRecentCompany(map);
	}
	@Override
	public List<Map> getRecentPayFeedBase(Map map){
		return SheBaoModel.getRecentPayFeedBase(map);
	}

}
