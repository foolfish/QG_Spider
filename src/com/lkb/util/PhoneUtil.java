package com.lkb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IPhoneNumService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.thirdUtil.MyComparator;
import com.lkb.thirdUtil.dx.SHDianXin;
import com.lkb.util.report.UnicomUtil;
import com.lkb.util.report.YidongUtils;
public class PhoneUtil {

	public List getPhoneList(String currentUser, IUserService userService,
			IDianXinDetailService dianxinDetailService,
			IMobileDetailService mobileDetailService,
			IUnicomDetailService unicomDetailService) {
		// 手机联系人
		List<Map> listAll = new ArrayList<Map>(); // 电信、联通、移动合并后的查询数据
		Map canMap = new HashMap(1);
		// 联通
		canMap.put("parentId", currentUser);
		canMap.put("usersource", Constant.LIANTONG);
		List<User> contactlistlt = userService.getUserByParentIdSource(canMap);
		for (int i = 0; i < contactlistlt.size(); i++) {
			String phone = contactlistlt.get(i).getPhone();
			UnicomUtil lt = new UnicomUtil();
			List<Map> list = lt.getLTContacts(phone, unicomDetailService);
			listAll.addAll(list);
		}
		// 电信
		canMap.put("parentId", currentUser);
		canMap.put("usersource", Constant.DIANXIN);
		List<User> contactlistdx = userService.getUserByParentIdSource(canMap);

		for (int i = 0; i < contactlistdx.size(); i++) {
			String phone = contactlistdx.get(i).getPhone();
			SHDianXin dx = new SHDianXin();
			List<Map> list = dx.getLTContacts(phone, dianxinDetailService);
			listAll.addAll(list);
		}
		// 移动
		canMap.put("parentId", currentUser);
		canMap.put("usersource", Constant.YIDONG);
		List<User> contactlistyd = userService.getUserByParentIdSource(canMap);
		for (int i = 0; i < contactlistyd.size(); i++) {
			String phone = contactlistyd.get(i).getPhone();
			YidongUtils yd = new YidongUtils();
			List<Map> list = yd.getLTContacts(phone, mobileDetailService);
			listAll.addAll(list);
		}
		
		for (int i = 0; i < listAll.size(); i++) {
			System.out.println(listAll.get(i).toString());
			
		}
		MyComparator c= new MyComparator();
		Collections.sort(listAll,  c);

		return listAll;
	}
	public List getPhoneBillList(String currentUser, IUserService userService,
			IDianXinDetailService dianxinDetailService,
			IMobileTelService mobileTelService,IUnicomTelService unicomTelService,IDianXinTelService dianxinTelService,
			IUnicomDetailService unicomDetailService,IPhoneNumService phoneNumService,IMobileDetailService mobileDetailService) {
		// 手机联系人
		List<Map> listAll = new ArrayList<Map>(); // 电信、联通、移动合并后的查询数据
		Map canMap = new HashMap(1);
		
		// 移动
		canMap.put("parentId", currentUser);
		canMap.put("usersource", Constant.YIDONG);
		List<User> contactlistyd = userService.getUserByParentIdSource(canMap);
		for (int i = 0; i < contactlistyd.size(); i++) {
			String phone = contactlistyd.get(i).getPhone();
			YidongUtils yd = new YidongUtils();
			List<Map> list = yd.getYDBill(phone,  mobileTelService, phoneNumService,mobileDetailService,userService);
			listAll.addAll(list);

		}
		// 联通
		canMap.put("parentId", currentUser);
		canMap.put("usersource", Constant.LIANTONG);
		List<User> contactlistlt = userService.getUserByParentIdSource(canMap);
		for (int i = 0; i < contactlistlt.size(); i++) {
			String phone = contactlistlt.get(i).getPhone();
			UnicomUtil lt = new UnicomUtil();
			List<Map> list =lt.getLTBill(phone, unicomTelService, phoneNumService, unicomDetailService,userService);
			listAll.addAll(list);
		}
		// 电信
		canMap.put("parentId", currentUser);
		canMap.put("usersource", Constant.DIANXIN);
		List<User> contactlistdx = userService.getUserByParentIdSource(canMap);

		for (int i = 0; i < contactlistdx.size(); i++) {
			String phone = contactlistdx.get(i).getPhone();
			SHDianXin dx = new SHDianXin();
			List<Map> list=dx.getDXBill(phone, dianxinTelService, phoneNumService, dianxinDetailService,userService);
			listAll.addAll(list);
		}
		

		return listAll;
	}
}
