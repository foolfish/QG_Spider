package com.lkb.serviceImp;




import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.ICBCCheckBill;
import com.lkb.dao.ICBCCheckBillDao;
import com.lkb.daoImp.ICBCCheckBillDaoImpl;
import com.lkb.service.ICBCCheckBillService;





@Service
@Transactional
public class ICBCCheckBillServiceImpl implements ICBCCheckBillService {

	@Resource
	private ICBCCheckBillDaoImpl icbcCheckBillModel;
	

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#findById(java.lang.String)
	 */

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#findById(java.lang.String)
	 */
	public ICBCCheckBill findById(String id) {
		
		 return icbcCheckBillModel.findById(id);
		
	}


	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#saveCMBTransferBill(com.lkb.bean.CMBTransferBill)
	 */
	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#saveICBCCheckBill(com.lkb.bean.ICBCCheckBill)
	 */
	public  void saveICBCCheckBill(ICBCCheckBill checkbill) {	
		try {
			icbcCheckBillModel.saveICBCCheckBill(checkbill)	;
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#deleteCMBTransferBill(java.lang.String)
	 */
	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#deleteICBCCheckBill(java.lang.String)
	 */
	public void deleteICBCCheckBill(String id) {	
		try {
			icbcCheckBillModel.deleteICBCCheckBill(id);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#update(com.lkb.bean.CMBTransferBill)
	 */
	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#update(com.lkb.bean.ICBCCheckBill)
	 */
	public void  update(ICBCCheckBill checkferbill){
		
		icbcCheckBillModel.update(checkferbill);
	}
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#getBillByBaseUserIdDate(java.util.Map)
	 */
	public List<ICBCCheckBill> getBillByBaseUserIdDate(Map map){
		return icbcCheckBillModel.getBillByBaseUserIdDate(map);
	}
	

}
