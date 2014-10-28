package com.lkb.serviceImp;




import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.ICBCCheckBill;
import com.lkb.bean.ICBCCheckBillItem;
import com.lkb.dao.ICBCCheckBillDao;
import com.lkb.dao.ICBCCheckBillItemDao;
import com.lkb.daoImp.ICBCCheckBillItemDaoImpl;
import com.lkb.service.ICBCCheckBillItemService;
import com.lkb.service.ICBCCheckBillService;





@Service
@Transactional
public class ICBCCheckBillItemServiceImpl implements ICBCCheckBillItemService {

	@Resource
	private ICBCCheckBillItemDaoImpl icbcCheckBillItemModel;
	

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#findById(java.lang.String)
	 */

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#findById(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillItemService#findById(java.lang.String)
	 */
	public ICBCCheckBillItem findById(String id) {
		
		 return icbcCheckBillItemModel.findById(id);
		
	}


	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICMBTransferBillService#saveCMBTransferBill(com.lkb.bean.CMBTransferBill)
	 */
	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#saveICBCCheckBill(com.lkb.bean.ICBCCheckBill)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillItemService#saveICBCCheckBillItem(com.lkb.bean.ICBCCheckBillItem)
	 */
	public  void saveICBCCheckBillItem(ICBCCheckBillItem checkbillitem) {	
		try {
			icbcCheckBillItemModel.saveICBCCheckBillItem(checkbillitem)	;
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
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillItemService#deleteICBCCheckBillItem(java.lang.String)
	 */
	public void deleteICBCCheckBillItem(String id) {	
		try {
			icbcCheckBillItemModel.deleteICBCCheckBillItem(id);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillItemService#update(com.lkb.bean.ICBCCheckBillItem)
	 */
	public void  update(ICBCCheckBillItem checkferbillitem){
		
		icbcCheckBillItemModel.update(checkferbillitem);
	}

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillItemService#getBillItemByCheckBillIdDate(java.util.Map)
	 */
	public List<ICBCCheckBillItem> getBillItemByCheckBillIdDate(Map map){
		return icbcCheckBillItemModel.getBillItemByCheckBillIdDate(map);
	}
	

}
