package com.lkb.serviceImp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lkb.bean.MobileOnlineList;
import com.lkb.constant.Constant;
import com.lkb.daoImp.MobileOnlineListDaoImp;
import com.lkb.service.IMobileOnlineListService;

@Service
@Transactional
public class MobileOnlineListServiceImpl implements IMobileOnlineListService {

	@Resource
	private MobileOnlineListDaoImp mobileOnlineListModel;

	@Override
	public MobileOnlineList findById(String id) {
		return mobileOnlineListModel.findById(id);
	}

	@Override
	public void save(MobileOnlineList obj) {
		try {
			mobileOnlineListModel.save(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String id) {
		try {
			mobileOnlineListModel.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(MobileOnlineList obj) {
		mobileOnlineListModel.update(obj);

	}

	@Override
	public List<Map> getMobileOnlineListBypt(Map map) {
		return mobileOnlineListModel.getMobileOnlineListBypt(map);
	}

	@Override
	public List<Map> getMobileOnlineListForReport(String phone) {
		return mobileOnlineListModel.getMobileOnlineListForReport(phone);
	}

	@Override
	public List<Map> getMobileOnlineListForReport2(Map map) {
		return mobileOnlineListModel.getMobileOnlineListForReport2(map);
	}

	@Override
	public MobileOnlineList getMaxTime(String phone) {
		return mobileOnlineListModel.getMaxTime(phone);
	}

	@Override
	public List<Map> getMobileOnlineList(String phone) {
		return mobileOnlineListModel.getMobileOnlineList(phone);
	}

	@Override
	public void insertbatch(List<MobileOnlineList> list) {
		int batchAmount = Constant.batchAmount;
		int vsize = list.size();
		if (list != null && vsize > 0) {
			int amount = vsize / batchAmount;
			if (amount >= 1) {
				for (int j = 0; j < amount + 1; j++) {
					int size = (j + 1) * batchAmount;
					if (size > vsize) {
						size = vsize;
					}
					List vslist = list.subList(j * batchAmount, size);
					if (vslist != null && vslist.size() > 0) {
						mobileOnlineListModel.insertbatch(vslist);
					}
				}

			} else {
				mobileOnlineListModel.insertbatch(list);
			}
		}
	}

}
