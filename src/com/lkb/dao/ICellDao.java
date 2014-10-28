package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.Cells;



public interface ICellDao {
	
	Cells findById(String id);
	
	void saveCell(Cells cell);
	
	void deleteCell(String id);
	
	List<Cells> getCellByType(Map map);
	
	void  update(Cells cell);
	
	List<Map> getlable();
	List<Map> getAge();	
	List<Map> getSex();
	List<Map> getIncome();
	List<Map> getBaby();
	List<Map> getXq();
	List<Map> getDomain();
	List<Map> getKeyword();

	List<Cells> getCells();
	Cells findByBaby(String baby);
}
