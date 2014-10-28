package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.ICBCCheckBillItem;

public interface ICBCCheckBillItemService {

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#findById(java.lang.String)
	 */
	ICBCCheckBillItem findById(String id);

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#saveICBCCheckBill(com.lkb.bean.ICBCCheckBill)
	 */
	void saveICBCCheckBillItem(ICBCCheckBillItem checkbillitem);

	/* (non-Javadoc)
	 * @see com.lkb.serviceImp.ICBCCheckBillService#deleteICBCCheckBill(java.lang.String)
	 */
	void deleteICBCCheckBillItem(String id);

	void update(ICBCCheckBillItem checkferbillitem);

	List<ICBCCheckBillItem> getBillItemByCheckBillIdDate(Map map);

}