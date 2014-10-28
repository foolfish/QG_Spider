package com.lkb.util.report;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.Order;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IOrderService;
import com.lkb.service.IPayInfoService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.util.DateUtils;

/*
 * 订单相关的工具类
 * */
public class OrderUtil {

	public static void main(String[] args) {
		// int days =31;
		// BigDecimal money = new BigDecimal("3332.32");
		//
		// int month=days/30+1;
		// BigDecimal pingjun = money.divide(new BigDecimal("34"),BigDecimal.
		// ROUND_HALF_UP);
		// System.out.println(pingjun);
		String loginNames = "3331,";
		loginNames = loginNames.subSequence(0, loginNames.length() - 1)
				.toString();
		System.out.println(loginNames);
	}

	public List<Map> getPay(IUserService userService,
			IOrderService orderService, String currentUser) {
		List<Map> list = new ArrayList<Map>();
		Date nowDate = new Date();
		java.text.DateFormat format2 = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		String date = format2.format(nowDate);
		DateUtils dateUtils = new DateUtils();

		// String[] source = {Constant.JD,Constant.TAOBAO};
		Map canMap1 = new HashMap(1);
		canMap1.put("parentId", currentUser);
		List<User> list2 = userService.getUserByParentId(canMap1);
		for (int i = 0; i < list2.size(); i++) {
			User user = list2.get(i);
			String orderstatus = "";
			String domainName = "";
			String usersource = user.getUsersource();
			String loginName = user.getLoginName();
			if (usersource.contains(Constant.JD)) {
				domainName = "京东商城";
				orderstatus = "已完成";
			} else if (usersource.contains(Constant.TAOBAO)) {
				domainName = "淘宝";
				orderstatus = "交易成功";
			}
			if (usersource.contains(Constant.JD)
					|| usersource.contains(Constant.TAOBAO)) {

				Map canMap = new HashMap(4);
				canMap.put("loginName", loginName);
				canMap.put("ordersource", usersource);
				canMap.put("buyTime", date);
				canMap.put("orderstatus", orderstatus);
				List<Map> list1 = orderService.getSomeInfo(canMap);
				if (list1 != null && list1.size() > 0) {
					Map map1 = list1.get(0);
					if (map1 != null && map1.get("money") != null) {
						BigDecimal money = new BigDecimal(map1.get("money")
								.toString());
						int ordercount = Integer.parseInt(map1
								.get("ordercount").toString());
						BigDecimal maxmoney = new BigDecimal(map1.get(
								"maxmoney").toString());
						if (ordercount > 0) {
							Map mapxin = new HashMap();
							String date2 = dateUtils.getLMDay(6);
							Map canMaps = new HashMap(4);
							canMap.put("loginName", loginName);
							canMaps.put("ordersource", usersource);
							canMaps.put("buyTime", date2);
							canMaps.put("orderstatus", orderstatus);

							List<Map> list5 = orderService.getSomeInfo(canMaps);
							Map map2 = list5.get(0);
							BigDecimal money2 = new BigDecimal(map1
									.get("money").toString());
							int ordercount2 = Integer.parseInt(map1.get(
									"ordercount").toString());

							// 计算注册时间到今天的天数
							Map canMap2 = new HashMap(2);
							canMap2.put("loginName", loginName);
							canMap2.put("ordersource", usersource);
							List<Map> list3 = orderService.getFirstDay(canMap2);
							Map map3 = list3.get(0);
							Date date3 = (Date) map3.get("buyTime");
							int days = dateUtils.caculateDays(date3);

							// 计算每月平均消费金额
							int month = days / 30 + 1;
							BigDecimal pingjun = money.divide(new BigDecimal(
									month), BigDecimal.ROUND_HALF_UP);

							mapxin.put("domainName", domainName);
							mapxin.put("allmoney", money);
							mapxin.put("ordercount", ordercount);
							mapxin.put("maxmoney", maxmoney);
							mapxin.put("days", days);
							mapxin.put("sixmoney", money2);
							mapxin.put("sixcount", ordercount2);
							mapxin.put("pingjun", pingjun);
							list.add(mapxin);

						}

					}

				}

			}

		}

		return list;
	}

	// 支出(淘宝+京东)
	public Map getZhichu(IUserService userService, IOrderService orderService,
			List loginNames) {
		Map maps = new HashMap();

		java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyy.MM");

		java.text.DateFormat format3 = new java.text.SimpleDateFormat("yyyy-MM");
		DateUtils dateUtils = new DateUtils();

		if (loginNames != null && loginNames.size() > 0) {

		} else {
			maps.put("days", "无");
			maps.put("maxmoney", "0");
			maps.put("minmoney", "0");
			maps.put("everyMonth", "0");
			maps.put("recenteveryMonth", "0");
			return maps;
		}

		Map canMaps = new HashMap(1);
		canMaps.put("list", loginNames);
		List<Map> list1 = orderService.getFirstDays(canMaps);
		if (list1 != null && list1.size() > 0) {
			Map map1 = list1.get(0);
			if (map1 != null) {
				Date buyTime = (Date) map1.get("buyTime");
				String firstDay = format2.format(buyTime);
				String firstDay2 = format3.format(buyTime);

				int monthslenth = 0;
				// 前后距离的时间
				Map canMaps2 = new HashMap(1);
				canMaps2.put("list", loginNames);
				List<Map> list2 = orderService.getRencentDays(canMaps2);
				if (list2 != null) {
					Map map2 = list2.get(0);
					if (map2 != null) {
						Date buyTime2 = (Date) map2.get("buyTime");
						String recentDay = format2.format(buyTime2);
						String recentDay2 = format3.format(buyTime2);
						String[] months = dateUtils.getAllMonths(firstDay2,
								recentDay2);
						monthslenth = months.length;
						maps.put("days", firstDay + "-" + recentDay);
					}
				}

				List<Map> list3 = orderService.getAllMaxMin(canMaps);
				if (list3 != null) {
					Map map2 = list3.get(0);
					if (map2 != null) {
						BigDecimal maxmoney = new BigDecimal(map2.get(
								"maxmoney").toString());
						BigDecimal minmoney = new BigDecimal(map2.get(
								"minmoney").toString());
						BigDecimal allmoney = new BigDecimal(map2.get(
								"allmoney").toString());
						BigDecimal everyMonth = allmoney.divide(new BigDecimal(
								monthslenth), BigDecimal.ROUND_HALF_UP);

						maps.put("maxmoney", maxmoney);
						maps.put("minmoney", minmoney);
						maps.put("everyMonth", everyMonth);
					}
				}

				Map canMaps4 = new HashMap(2);
				String date2 = dateUtils.getLMDay(12);
				canMaps4.put("list", loginNames);
				canMaps4.put("buyTime", date2);
				List<Map> list4 = orderService.getPerMaxMin(canMaps4);
				if (list4 != null) {
					Map map2 = list4.get(0);
					if (map2 != null) {

						BigDecimal allmoney = new BigDecimal(map2.get(
								"allmoney").toString());
						BigDecimal recenteveryMonth = allmoney.divide(
								new BigDecimal(12), BigDecimal.ROUND_HALF_UP);
						maps.put("recenteveryMonth", recenteveryMonth);
					}
				} else {
					maps.put("recenteveryMonth", new BigDecimal("0"));
				}

			}

		}

		return maps;
	}

	/*
	 * 获得每个月消毒的金额
	 */
	public Map getEveryAmount(IOrderService orderService, List loginNames) {
		Map map11 = new HashMap();
		String a = "";
		String b = "";
		DateUtils dateUtils = new DateUtils();
		List<String> list2 = dateUtils.getMonthForm(12, "yyyy-MM");
		Collections.reverse(list2);
		for (int i = 0; i < list2.size(); i++) {
			BigDecimal mm = new BigDecimal("0");
			a += list2.get(i) + ",";
			Map map1 = new HashMap(1);
			map1.put("list", loginNames);
			map1.put("buyTime", list2.get(i) + "%");
			List<Map> list3 = orderService.getEveryAmount(map1);
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
		map11.put("ordertime", a);
		map11.put("ordervalues", b);

		return map11;
	}

	/*
	 * 获得每个月消费的金额
	 */
	public Map getEveryAmountFu(IOrderService orderService, List dsloginNames,
			List ydloginNames, List ltloginNames, List dxloginNames,
			IDianXinTelService dianxinService,
			IUnicomTelService unicomTelService,
			IMobileTelService mobileTelService) {
		Map map11 = new HashMap();
		String a = "";
		String b = "";
		DateUtils dateUtils = new DateUtils();
		List<String> list2 = dateUtils.getMonthForm(12, "yyyy-MM");
		Collections.reverse(list2);
		for (int i = 0; i < list2.size(); i++) {
			BigDecimal zero = new BigDecimal("0");
			BigDecimal mm = new BigDecimal("0");
			a += list2.get(i) + ",";
			if (dsloginNames != null && dsloginNames.size() > 0) {
				Map map1 = new HashMap(2);
				map1.put("list", dsloginNames);
				map1.put("buyTime", list2.get(i) + "%");
				List<Map> list3 = orderService.getEveryAmount(map1);
				if (list3 != null) {
					Map map = list3.get(0);
					if (map != null && map.get("amount") != null) {
						BigDecimal amount = new BigDecimal(map.get("amount")
								.toString());
						mm = amount;

					}
				}
			}
			if (ydloginNames != null && ydloginNames.size() > 0) {
				Map map111 = new HashMap(2);
				map111.put("list", ydloginNames);
				map111.put("cTime", list2.get(i) + "%");
				List<Map> list31 = mobileTelService.getEveryAmount(map111);
				if (list31 != null) {
					Map map = list31.get(0);
					if (map != null && map.get("amount") != null) {
						BigDecimal amount = new BigDecimal(map.get("amount")
								.toString());
						mm = mm.add(amount);

					}
				}
			}

			if (ltloginNames != null && ltloginNames.size() > 0) {
				Map map222 = new HashMap(2);
				map222.put("list", ltloginNames);
				map222.put("cTime", list2.get(i) + "%");
				List<Map> list32 = unicomTelService.getEveryAmount(map222);
				if (list32 != null) {
					Map map = list32.get(0);
					if (map != null && map.get("amount") != null) {
						BigDecimal amount = new BigDecimal(map.get("amount")
								.toString());
						mm = mm.add(amount);
					}
				}
			}

			if (dxloginNames != null && dxloginNames.size() > 0) {
				Map map333 = new HashMap(2);
				map333.put("list", dxloginNames);
				map333.put("cTime", list2.get(i) + "%");
				List<Map> list33 = dianxinService.getEveryAmount(map333);
				if (list33 != null) {
					Map map = list33.get(0);
					if (map != null && map.get("amount") != null) {
						BigDecimal amount = new BigDecimal(map.get("amount")
								.toString());
						mm = mm.add(amount);
					}
				}
			}
			mm = zero.subtract(mm);
			b += mm + ",";
		}
		a = a.substring(0, a.length() - 1);
		b = b.substring(0, b.length() - 1);
		map11.put("ordertime", a);
		map11.put("ordervalues", b);

		return map11;
	}
	
	
	/*
	 * 获取订单相关元数据
	 * 
	 * */
	public List<Map> getPay(IOrderService orderService, List dsloginNames,String usersource){
		List<Map> list = new ArrayList();
		for(int i=0;i<dsloginNames.size();i++){
			Map maps = new HashMap();
			Map map = new HashMap();
			String loginName = dsloginNames.get(i).toString();
			map.put("loginName", loginName);
			map.put("ordersource", usersource);
			String orderstatus = "已完成";	
			map.put("orderstatus", orderstatus);
			List<Map> orders = orderService.getOrderAllByLoginNamesource(map);
			
			maps.put("loginName", loginName);
			maps.put("source", usersource);
			maps.put("data", orders);
			list.add(maps);
		}
		return list;		
	}

}
