package com.lkb.thirdUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IUserService;

public class XuexinUtil {
	
	public List getXuexinDetail(String currentUser,IUserService userService,List loginNames){
		List list = new ArrayList();
		for(int i=0;i<loginNames.size();i++){
			String loginName = loginNames.get(i).toString();	
			Map map2 = new HashMap();
			map2.put("parentId", currentUser);
			map2.put("usersource", Constant.XUEXIN);
			map2.put("loginName", loginName);
			List<User> users = userService.getUserByParentIdSource(map2);
			if(users!=null && users.size()>0){
				User user = users.get(0);
				String phone = user.getPhone();
				String realname = user.getRealName();
				String sex = user.getSex();
				String cardtype = user.getCardType();
				String cardno = user.getCardNo();
				Date entranceDate = user.getEntranceDate();
				Date graduateDate = user.getGraduateDate();
				String eduType = user.getEduType();
				String eduRecord = user.getEduRecord();
				String eduSchool = user.getEduSchool();
				String schoolPlace = user.getSchoolPlace();
				String specialty = user.getSpecialty();
				String eduForm = user.getEduForm();		
				String certificateid = user.getCertificateId();		
				String eduConclusion = user.getEduConclusion();		

				Map map = new HashMap();
				map.put("loginName", loginName);
				map.put("source", Constant.XUEXIN);
				map.put("phone", phone);
				map.put("realname", realname);
				map.put("sex", sex);
				map.put("cardtype", cardtype);
				map.put("cardno", cardno);
				map.put("entranceDate", entranceDate);
				map.put("graduateDate", graduateDate);
				map.put("eduType", eduType);
				map.put("eduRecord", eduRecord);
				map.put("eduSchool", eduSchool);
				map.put("schoolPlace", schoolPlace);
				map.put("specialty", specialty);
				map.put("eduForm", eduForm);
				map.put("certificateid", certificateid);
				map.put("eduConclusion", eduConclusion);
				list.add(map);
			}
		}
		return list;
	}
}
