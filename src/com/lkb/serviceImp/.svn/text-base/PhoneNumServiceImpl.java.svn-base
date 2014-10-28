package com.lkb.serviceImp;


import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lkb.bean.PhoneNum;
import com.lkb.daoImp.PhoneNumDaoImp;
import com.lkb.service.IPhoneNumService;

@Service
@Transactional
public class PhoneNumServiceImpl implements IPhoneNumService {

	@Resource
	private PhoneNumDaoImp phoneNumModel;
	
	@Override
	public PhoneNum findById(String phoneNumId) {
		
		PhoneNum phoneNum = phoneNumModel.findById(phoneNumId);
		return phoneNum;
	}

	@Override
	public  void savePhoneNum(PhoneNum phoneNum) {	
		try {
			phoneNumModel.savePhoneNum(phoneNum);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deletePhoneNum(String phoneNumId) {	
		try {
			phoneNumModel.deletePhoneNum(phoneNumId);		
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public List<PhoneNum> getPhoneNumByBaseUseridsource(Map map){
		
		List<PhoneNum> list = phoneNumModel.getPhoneNumByBaseUseridsource(map);		
		
		return list;
		
	}
	
	@Override
	public void  update(PhoneNum phoneNum){
		phoneNumModel.update(phoneNum);
	}
	


}
