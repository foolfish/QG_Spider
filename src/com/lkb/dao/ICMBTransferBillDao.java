package com.lkb.dao;

import java.util.List;
import java.util.Map;

import com.lkb.bean.CMBTransferBill;
import com.lkb.bean.User;

public interface ICMBTransferBillDao {

	public  CMBTransferBill findById(String id);

	public  void saveCMBTransferBill(CMBTransferBill cmbTransferBill);

	public  void deleteCMBTransferBill(String id);

	public  void update(CMBTransferBill cmbTransferBill);

	List<CMBTransferBill> getBillByBaseUserIdDate(Map map);
}