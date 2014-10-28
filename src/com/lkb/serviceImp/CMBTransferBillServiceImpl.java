package com.lkb.serviceImp;




import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.CMBTransferBill;

import com.lkb.daoImp.CMBTransferBillDaoImpl;
import com.lkb.service.ICMBTransferBillService;




@Service
@Transactional
public class CMBTransferBillServiceImpl implements ICMBTransferBillService {

	@Resource
	private CMBTransferBillDaoImpl cmbTransferBillModel;
	

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#findById(java.lang.String)
	 */
	@Override
	public CMBTransferBill findById(String id) {
		
		CMBTransferBill tansferbill = cmbTransferBillModel.findById(id);
		return tansferbill; 
	}


	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#saveCMBTransferBill(com.lkb.bean.CMBTransferBill)
	 */
	@Override
	public  void saveCMBTransferBill(CMBTransferBill transferbill) {	
		try {
			cmbTransferBillModel.saveCMBTransferBill(transferbill)	;
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#deleteCMBTransferBill(java.lang.String)
	 */
	@Override
	public void deleteCMBTransferBill(String id) {	
		try {
			cmbTransferBillModel.deleteCMBTransferBill(id);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#update(com.lkb.bean.CMBTransferBill)
	 */
	@Override
	public void  update(CMBTransferBill transferbill){
		
		cmbTransferBillModel.update(transferbill);
	}
	public List<CMBTransferBill> getBillByBaseUserIdDate(Map map){
		return cmbTransferBillModel.getBillByBaseUserIdDate(map);
	}
	

}
