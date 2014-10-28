package com.lkb.daoImp;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lkb.bean.CMBTransferBill;

import com.lkb.dao.ICMBTransferBillDao;




@Service
public class CMBTransferBillDaoImpl {

	@Resource
	private ICMBTransferBillDao transferBillDao;
	
	
	public void setTransferBillDao(ICMBTransferBillDao transferBillDao) {
		this.transferBillDao = transferBillDao;
	}



	public CMBTransferBill findById(String id){
		return transferBillDao.findById(id);
	}
	
	
	
	public void saveCMBTransferBill(CMBTransferBill cmbTransferBill){
		transferBillDao.saveCMBTransferBill(cmbTransferBill);
	}
	
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICMBTransferBillDao#deleteCMBTransferBill(java.lang.String)
	 */

	public void deleteCMBTransferBill(String id){
		transferBillDao.deleteCMBTransferBill(id);
	}
	
	

	public void  update(CMBTransferBill cmbTransferBill ){
		transferBillDao.update(cmbTransferBill);
	}
	public List<CMBTransferBill> getBillByBaseUserIdDate(Map map){
		return transferBillDao.getBillByBaseUserIdDate(map);
	}
}



