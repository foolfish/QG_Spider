package com.lkb.daoImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.Cells;
import com.lkb.dao.ICellDao;


@Service
public class CellDaoImp {

	@Resource
	private ICellDao cellDao;
	
	public Cells findById(String cellId){
		return cellDao.findById(cellId);
	}
	
	public void saveCell(Cells cell){
		cellDao.saveCell(cell);
	}
	
	public void deleteCell(String cellId){
		cellDao.deleteCell(cellId);
	}
	
	public List<Cells> getCellByType(Map map){
		return cellDao.getCellByType(map);
	}
		
	public void  update(Cells cell){
		cellDao.update(cell);
	}
	public List<Map> getlable(){
		return cellDao.getlable();
	}
	public List<Map> getAge(){
		return cellDao.getAge();
	}	
	public List<Map> getIncome(){
		return cellDao.getIncome();
	}	
	public List<Map> getBaby(){
		return cellDao.getBaby();
	}	
	public List<Map> getXq(){
		return cellDao.getXq();
	}	
	public List<Map> getDomain(){
		return cellDao.getDomain();
	}	
	public List<Map> getKeyword(){
		return cellDao.getKeyword();
	}
	public List<Map> getSex(){
		return cellDao.getSex();
	}
	public List<Cells> getCells(){
		return cellDao.getCells();
	}

	public Cells findByBaby(String baby){
		return cellDao.findByBaby(baby);
	}
	
}
