package com.lkb.daoImp;



import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import com.lkb.bean.ICBCCheckBill;

import com.lkb.dao.ICBCCheckBillDao;
import com.lkb.dao.ICMBTransferBillDao;




@Service
public class ICBCCheckBillDaoImpl implements ICBCCheckBillDao {

	@Resource
	private ICBCCheckBillDao checkBillDao;
	

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#findById(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#findById(java.lang.String)
	 */
	public ICBCCheckBill findById(String id){
		return checkBillDao.findById(id);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#saveICBCCheckBill(com.lkb.bean.ICBCCheckBill)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#saveICBCCheckBill(com.lkb.bean.ICBCCheckBill)
	 */
	public void saveICBCCheckBill(ICBCCheckBill icbcTransferferBill){
		checkBillDao.saveICBCCheckBill(icbcTransferferBill);
	}
	
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICMBTransferBillDao#deleteCMBTransferBill(java.lang.String)
	 */

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#deleteICBCCheckBill(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#deleteICBCCheckBill(java.lang.String)
	 */
	public void deleteICBCCheckBill(String id){
		checkBillDao.deleteICBCCheckBill(id);
	}
	
	

	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#update(com.lkb.bean.ICBCCheckBill)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#update(com.lkb.bean.ICBCCheckBill)
	 */
	public void  update(ICBCCheckBill icbcTransferferBill ){
		checkBillDao.update(icbcTransferferBill);
	}
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#getBillByBaseUserIdDate(java.util.Map)
	 */
	/* (non-Javadoc)
	 * @see com.lkb.daoImp.ICBCCheckBillDao#getBillByBaseUserIdDate(java.util.Map)
	 */
	public List<ICBCCheckBill> getBillByBaseUserIdDate(Map map){
		return checkBillDao.getBillByBaseUserIdDate(map);
	}
}



