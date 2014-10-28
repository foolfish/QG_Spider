package com.lkb.util.report;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IUserService;
import com.lkb.service.IYuEBaoService;
import com.lkb.util.DateUtils;

public class YueBaoUtil {

	/*
	 * 获得每个月消毒的金额
	 */
	public Map getEveryAmount(IYuEBaoService yuebaoService, String currentUser,
			IUserService userService) {

		Map map11 = new HashMap();
		Map canMap = new HashMap(2);
		canMap.put("parentId", currentUser);
		canMap.put("usersource", Constant.ZHIFUBAO);
		DateUtils dateUtils = new DateUtils();
		List<User> contactlistlt = userService.getUserByParentIdSource(canMap);
		if (contactlistlt != null && contactlistlt.size() != 0) {
			String alipayName = contactlistlt.get(0).getLoginName();

			String a = "";
			String b = "";
			
			List<String> list2 = dateUtils.getMonthForm(12, "yyyy-MM");
			Collections.reverse(list2);
			for (int i = 0; i < list2.size(); i++) {
				BigDecimal mm = new BigDecimal("0");
				a += list2.get(i) + ",";
				Map map1 = new HashMap(1);
				map1.put("alipayName", alipayName);
				map1.put("cTime", list2.get(i) + "%");
				List<Map> list3 = yuebaoService.getEveryAmount(map1);
				if (list3 != null) {
					Map map = list3.get(0);
					if (map != null && map.get("amount") != null) {
						BigDecimal amount = new BigDecimal(map.get("amount")
								.toString());
						mm = amount;

					}
				}
				b += mm + ",";
			}
			a = a.substring(0, a.length() - 1);
			b = b.substring(0, b.length() - 1);
			map11.put("yuebaotime", a);
			map11.put("yuebaovalues", b);
			
		}else{
			String a = "";
			String b = "";
			List<String> list2 = dateUtils.getMonthForm(12, "yyyy-MM");
			Collections.reverse(list2);
			for (int i = 0; i < list2.size(); i++) {
				BigDecimal mm = new BigDecimal("0");
				a += list2.get(i) + ",";	
				b += 0 + ",";
			}
			a = a.substring(0, a.length() - 1);
			b = b.substring(0, b.length() - 1);
			map11.put("yuebaotime", a);
			map11.put("yuebaovalues", b);
		}
		return map11;
		
	}

}
