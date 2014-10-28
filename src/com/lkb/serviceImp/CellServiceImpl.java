package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.Cells;
import com.lkb.daoImp.CellDaoImp;
import com.lkb.service.ICellService;

@Service
@Transactional
public class CellServiceImpl implements ICellService {

	@Resource
	private CellDaoImp cellModel;
	
	@Override
	public Cells findById(String cellId) {
		
		Cells cell = cellModel.findById(cellId);
		return cell;
	}

	@Override
	public  void saveCell(Cells cell) {	
		try {
			cellModel.saveCell(cell);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteCell(String cellId) {	
		try {
			cellModel.deleteCell(cellId);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	
	@Override
	public void  update(Cells cell){
		cellModel.update(cell);
	}


	@Override
	public List<Map> getlable() {
		// TODO Auto-generated method stub
		return cellModel.getlable();
	}

	@Override
	public List<Map> getAge() {
		// TODO Auto-generated method stub
		return cellModel.getAge();
	}

	@Override
	public List<Map> getIncome() {
		// TODO Auto-generated method stub
		return cellModel.getIncome();
	}

	@Override
	public List<Map> getBaby() {
		// TODO Auto-generated method stub
		return cellModel.getBaby();
	}

	@Override
	public List<Map> getXq() {
		// TODO Auto-generated method stub
		return cellModel.getXq();
	}

	@Override
	public List<Map> getDomain() {
		// TODO Auto-generated method stub
		return cellModel.getDomain();
	}

	@Override
	public List<Map> getKeyword() {
		// TODO Auto-generated method stub
		return cellModel.getKeyword();
	}
	
	@Override
	public List<Map> getSex() {
		// TODO Auto-generated method stub
		return cellModel.getSex();
	}

	@Override
	public List<Cells> getCells() {
		// TODO Auto-generated method stub
		return cellModel.getCells();
	}
	
	@Override
	public Cells findByBaby(String baby){
		return cellModel.findByBaby(baby);
	}

}
