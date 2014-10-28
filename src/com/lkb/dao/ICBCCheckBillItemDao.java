package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.ICBCCheckBillItem;

public interface ICBCCheckBillItemDao {

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#findById(java.lang.String)
	 */
	ICBCCheckBillItem findById(String id);

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#saveICBCCheckBill(com.lkb.bean.ICBCCheckBill)
	 */
	void saveICBCCheckBillItem(ICBCCheckBillItem icbcTransferferBillItem);

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#deleteICBCCheckBill(java.lang.String)
	 */
	void deleteICBCCheckBillItem(String id);

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#update(com.lkb.bean.ICBCCheckBill)
	 */
	void update(ICBCCheckBillItem icbcTransferferBillItem);

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#getBillByBaseUserIdDate(java.util.Map)
	 */
	List<ICBCCheckBillItem> getBillItemByCheckBillIdDate(Map map);

}