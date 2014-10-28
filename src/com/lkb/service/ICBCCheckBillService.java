package com.lkb.service;

import java.util.List;
import java.util.Map;

import com.lkb.bean.ICBCCheckBill;

public interface ICBCCheckBillService {

	ICBCCheckBill findById(String id);

	void saveICBCCheckBill(ICBCCheckBill checkbill);

	void deleteICBCCheckBill(String id);

	void update(ICBCCheckBill checkferbill);

	List<ICBCCheckBill> getBillByBaseUserIdDate(Map map);

}