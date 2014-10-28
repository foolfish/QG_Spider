package com.lkb.daoImp;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.lkb.bean.ICBCCheckBillItem;
import com.lkb.dao.ICBCCheckBillItemDao;





@Service
public class ICBCCheckBillItemDaoImpl implements ICBCCheckBillItemDao  {

	@Resource
	private ICBCCheckBillItemDao checkBillItemDao;
	

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#findById(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillItemDao#findById(java.lang.String)
	 */
	public ICBCCheckBillItem findById(String id){
		return checkBillItemDao.findById(id);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#saveICBCCheckBill(com.lkb.bean.ICBCCheckBill)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillItemDao#saveICBCCheckBillItem(com.lkb.bean.ICBCCheckBillItem)
	 */
	public void saveICBCCheckBillItem(ICBCCheckBillItem icbcTransferferBillItem){
		checkBillItemDao.saveICBCCheckBillItem(icbcTransferferBillItem);
	}
	
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICMBTransferBillDao#deleteCMBTransferBill(java.lang.String)
	 */

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#deleteICBCCheckBill(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillItemDao#deleteICBCCheckBillItem(java.lang.String)
	 */
	public void deleteICBCCheckBillItem(String id){
		checkBillItemDao.deleteICBCCheckBillItem(id);
	}
	
	

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#update(com.lkb.bean.ICBCCheckBill)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillItemDao#update(com.lkb.bean.ICBCCheckBillItem)
	 */
	public void  update(ICBCCheckBillItem icbcTransferferBillItem ){
		checkBillItemDao.update(icbcTransferferBillItem);
	}
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#getBillByBaseUserIdDate(java.util.Map)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillItemDao#getBillItemByCheckBillIdDate(java.util.Map)
	 */
	public List<ICBCCheckBillItem> getBillItemByCheckBillIdDate(Map map){
		return checkBillItemDao.getBillItemByCheckBillIdDate(map);
	}
}



