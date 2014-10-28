package com.lkb.util.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.OTCity;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IOTCityService;
import com.lkb.service.ISheBaoService;
import com.lkb.service.IUniversityService;
import com.lkb.service.IUserService;

public class ReportUtil {

	public static void main(String[] args) {
		String nowwork = "5年4月";
		String[] strs = nowwork.split("年");
		String str1 = strs[0];
		System.out.println(str1);

	}

	public Map caculate(Map identifyMap, Map somethingMap,
			ISheBaoService shebaoService, String currentUser,
			IUserService userService, IUniversityService universityService,
			Map workmap, IOTCityService oTCityService, Map shebaoMap,
			List<Map> paylist, List<Map> phoneBillList, List<Map> phoneList,List listIncome,Map jinMap,Map yuebaoMap ) {
		Map map = new HashMap();
		BigDecimal all = new BigDecimal("0");
		User baseUser = userService.findById(currentUser);
		
		// 最后分值
		
		if(identifyMap.get("age").toString()!=null && !"".equals(identifyMap.get("age").toString())){
			int age = Integer.parseInt(identifyMap.get("age").toString());
			BigDecimal ageDegree = dgreeAge(age);
			all = all.add(ageDegree);
		}
		String sex = identifyMap.get("sex").toString();
		
		BigDecimal sexDegree = dgreeSex(sex);
		
		all = all.add(sexDegree);
		// 学历
		BigDecimal xueliDegree = new BigDecimal("0");
		if (somethingMap.get("xueli") != null) {
			String xueli = somethingMap.get("xueli").toString();
			xueliDegree = dgreeXueli(xueli);
		} else {
			xueliDegree = new BigDecimal("0.18");
		}
		// 学校
		BigDecimal xuexiaoDegree = new BigDecimal("0");
		if (somethingMap.get("eduSchool") != null) {
			String eduSchool = somethingMap.get("eduSchool").toString();
			xuexiaoDegree = dgreeXuexiao(eduSchool, universityService);
		} else {
			xuexiaoDegree = new BigDecimal("0.06");
		}
		all = all.add(xuexiaoDegree);

		// 户籍
		String city = identifyMap.get("city").toString();
		if(city!=null && !"".equals(city)){
			String province = city.substring(0, 2);
			BigDecimal hujiDegree = dgreeHuJi(province, oTCityService);
			all = all.add(hujiDegree);
		}

		// 当前公司工龄
		//String nowwork = workmap.get("nowwork").toString();
		String nowwork = "5年6月";
		BigDecimal nowworkDegree = dgreeWorkAge(nowwork);
		all = all.add(nowworkDegree);

		// 婚姻
//		BigDecimal merryDegree = dgreeMerry(currentUser, userService);
//		all = all.add(merryDegree);
		all = all.add(new BigDecimal("0.03"));
		// 是否有子女（既要提供）
		all = all.add(new BigDecimal("0.02"));
		// 健康及隐患
		// 公共记录

		if(listIncome!=null && listIncome.size()>0){
			// 收入
			Map incomeMap = (Map)listIncome.get(0);
			if(incomeMap!=null && incomeMap.get("avg")!=null){
//				BigDecimal avg = new BigDecimal(incomeMap.get("avg").toString());
//				String shebaovalues= shebaoMap.get("shebaovalues").toString();
				
				BigDecimal avg = new BigDecimal("5000");
				String shebaovalues= "5500,5500,5500,5500,5500,5500,5500,5500,5500";
				BigDecimal ss = dgreeIncome( avg, shebaovalues);
				all = all.add(ss);
			}else{
				all = all.add(new BigDecimal("0.3"));
			}
		}
	
		
		
		// 支出 
		if(jinMap!=null && jinMap.get("everyMonth")!=null){
			BigDecimal everyMonth = new BigDecimal(jinMap.get("everyMonth").toString());
			BigDecimal ss = dgreePay(everyMonth); 
			all = all.add(ss);
		}else{
			all = all.add(new BigDecimal("0.09"));
		}
		
		
		// 储蓄	
		if(yuebaoMap!=null && yuebaoMap.get("monAmount")!=null){
			BigDecimal monAmount = new BigDecimal(yuebaoMap.get("monAmount").toString());
			BigDecimal ss = dgreeChuxu(monAmount.multiply(new BigDecimal("30"))); 
			all = all.add(ss);
		}else{
			all = all.add(new BigDecimal("0.12"));
		}
		
		
		// 贷款 无及缺失
		BigDecimal daikuanDegree = new BigDecimal("0.08");
		all = all.add(daikuanDegree);
		// 房产
		int hourse = baseUser.getHourse();
		if(hourse==6){
			BigDecimal fangchanDegree = new BigDecimal("0.15");
			all = all.add(fangchanDegree);
		}else{
			BigDecimal fangchanDegree = new BigDecimal("0.05");
			all = all.add(fangchanDegree);
		}
		
		// 车
		int cars = baseUser.getCars();
		if(cars==5){
			BigDecimal fangchanDegree = new BigDecimal("0.12");
			all = all.add(fangchanDegree);
		}else{
			BigDecimal fangchanDegree = new BigDecimal("0.04");
			all = all.add(fangchanDegree);
		}

		// 未结算未销户数的信用卡
		all = all.add(new BigDecimal("0.1"));
		// 发生过逾期数
		// 发生过90天逾期数
		// 为他人担保笔数

		// 总消费金额
		BigDecimal xiaofei = dgreeXiaofei(paylist);
		all = all.add(xiaofei);

		// 6个月以上交易金额
		BigDecimal jiaoyisix = dgreeJiaoyi(paylist);
		all = all.add(jiaoyisix);

		// 月均消费金额
		BigDecimal yuejun = dgreeYuejun(paylist);
		all = all.add(yuejun);

		// 手机归属地
		BigDecimal phoneLocationdegree = dgreePhoneLocation(phoneBillList,
				oTCityService);
		all = all.add(phoneLocationdegree);

		// 手机月均消费额计算评分
		BigDecimal yuejunPhone = dgreeEveryPhoneLocation(phoneBillList);
		all = all.add(yuejunPhone);

		// 使用时间
		BigDecimal userTimeDegree = dgreePhoneUseTime(phoneBillList);
		all = all.add(userTimeDegree);

		// 借款意向
		all = all.add(new BigDecimal("0.1"));
		// 理财意向
		all = all.add(new BigDecimal("0.06"));
		// 欺诈风险
		// 重要联系人
		BigDecimal importantDegree =  dgreePhoneList(phoneList,phoneBillList);
		all = all.add(importantDegree);
		

		String content = "";
		String passstatus = "";
		if (all.compareTo(new BigDecimal("1.5")) <= 0) {
			content = "建议通过 ";
			passstatus = "A";
		} else if (all.compareTo(new BigDecimal("2")) <= 0) {
			content = "严格审核 ";
			passstatus = "B";
		} else if (all.compareTo(new BigDecimal("3.5")) <= 0) {
			content = "建议不通过";
			passstatus = "C";
		} else if (all.compareTo(new BigDecimal("4.5")) <= 0) {
			content = "建议不通过";
			passstatus = "D";
		} else {
			content = "建议不通过";
			passstatus = "E";
		}
		BigDecimal applyMoney = new BigDecimal("0");
		if (passstatus.equals("A") || passstatus.equals("B")) {
			BigDecimal sixwan = new BigDecimal("60000");
			Map mp1 = new HashMap(1);
			mp1.put("baseUserId", currentUser);
			List<Map> list2 = shebaoService.getRecentPayFeedBase(mp1);
			if (list2 != null && list2.size() > 0) {
				Map map2 = list2.get(0);
				if (map2 != null && map2.get("payFeedBase") != null) {
					BigDecimal payFeedBase = new BigDecimal(map2.get(
							"payFeedBase").toString());
					BigDecimal fourBase = payFeedBase.multiply(new BigDecimal(
							"4"));

					if (fourBase.compareTo(sixwan) <= 0) {
						applyMoney = fourBase;
					} else {
						applyMoney = sixwan;
					}
				}
			}
			if (applyMoney.compareTo(new BigDecimal("0")) == 0) {
				BigDecimal oneseven = new BigDecimal("2.1");
				applyMoney = ((oneseven.subtract(all)).multiply(sixwan))
						.divide(oneseven, BigDecimal.ROUND_HALF_UP);
				// 60000*（1.7-当前分数）/1.7
			}

		}

		// 计算总评分
		BigDecimal fullscore = getScore(all);
		int ifullscore = fullscore.intValue();
		int level = caculateLevel(fullscore);
		int complete = caculateComplete(currentUser, userService);
		int iapplyMoney = 1000;
		if (applyMoney.compareTo(new BigDecimal("1000")) > 0) {
			iapplyMoney = (applyMoney.intValue() / 1000) * 1000;
		} else {
			iapplyMoney = 0;
		}

		map.put("content", content);
		map.put("applyMoney", iapplyMoney);
		map.put("passstatus", passstatus);
		map.put("fullscore", ifullscore);
		map.put("level", level);
		map.put("complete", complete);
		return map;
	}
	
	
	/*
	 * 支出
	 */
	public BigDecimal dgreeChuxu(BigDecimal avg) {
		BigDecimal bb = new BigDecimal("0");	
		if (avg.compareTo(new BigDecimal("300000")) > 0) {
			bb = new BigDecimal("0.00");
		} else if (avg.compareTo(new BigDecimal("50000")) > 0) {
			bb = new BigDecimal("0.03");
		}else if (avg.compareTo(new BigDecimal("10000")) > 0) {
			bb = new BigDecimal("0.06");
		}else if (avg.compareTo(new BigDecimal("10000")) <= 0) {
			bb = new BigDecimal("0.09");
		}

		return bb;
	}
	
	
	/*
	 * 支出
	 */
	public BigDecimal dgreePay(BigDecimal avg) {
		BigDecimal bb = new BigDecimal("0");	
		if (avg.compareTo(new BigDecimal("1000")) > 0) {
			bb = new BigDecimal("0.03");
		} else {
			bb = new BigDecimal("0.06");
		}

		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.09");
		}

		return bb;
	}
	
	
	
	/*
	 * 收入
	 */
	public BigDecimal dgreeIncome(BigDecimal avg,String shebaovalues) {
		BigDecimal bb = new BigDecimal("0");	
		if (avg.compareTo(new BigDecimal("10000")) > 0) {
			bb = new BigDecimal("0.06");
		} else if (avg.compareTo(new BigDecimal("8000")) > 0) {
			bb = new BigDecimal("0.12");
		} else if (avg.compareTo(new BigDecimal("5000")) > 0) {
			bb = new BigDecimal("0.18");
		} else if (avg.compareTo(new BigDecimal("3000")) > 0) {
			bb = new BigDecimal("0.24");
		}else {
			bb = new BigDecimal("0.30");
		}
		
				
		String[] strs = shebaovalues.split(",");
		for (int i = 0; i < 6; i++) {
			String ss = strs[i];
			BigDecimal bs = new BigDecimal(ss);
			if (bs.compareTo(new BigDecimal("0")) == 0) {
				bb = new BigDecimal("0.30");
			}
		}
		

		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.30");
		}

		return bb;
	}

	

	/*
	 * 重要联系人
	 */
	public BigDecimal dgreePhoneList(List<Map> phoneList,
			List<Map> phoneBillList) {
		BigDecimal bb = new BigDecimal("0");

		if (phoneList != null && phoneList.size() > 3) {
			int totaltimes = 0;
			for (int i = 0; i < phoneList.size(); i++) {
				Map map = phoneList.get(i);
				int totaltime = Integer.parseInt(map.get("totaltimes")
						.toString());
				totaltimes += totaltime;
			}

			Map map1 = phoneList.get(0);
			Map map2 = phoneList.get(1);
			Map map3 = phoneList.get(2);
			int total1 = Integer.parseInt(map1.get("total").toString());
			int total2 = Integer.parseInt(map2.get("total").toString());
			int total3 = Integer.parseInt(map3.get("total").toString());

//			int totaltimes1 = Integer.parseInt(map1.get("totaltimes")
//					.toString());
//			int totaltimes2 = Integer.parseInt(map2.get("totaltimes")
//					.toString());
//			int totaltimes3 = Integer.parseInt(map3.get("totaltimes")
//					.toString());
			int month = 6;
			if(phoneBillList!=null &&phoneBillList.size()>0){
				Map map = phoneBillList.get(0);
				int days = Integer.parseInt(map.get("days").toString());
				month = days / 30 + 1;
			}

			if ((total1 + total2 + total3) / month >= 10
					&& totaltimes / month > 600) {
				bb = new BigDecimal("0.1");
			} else if ((total1 + total2 + total3) > 10 && totaltimes > 600) {
				bb = new BigDecimal("0.2");
			}else if ((total1 + total2 + total3) > 10){
				bb = new BigDecimal("0.3");
			}else{
				bb = new BigDecimal("0.4");
			}
		}

		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.4");
		}

		return bb;
	}

	/*
	 * 根据手机使用时间计算评分
	 */
	public BigDecimal dgreePhoneUseTime(List<Map> phoneBillList) {
		BigDecimal bb = new BigDecimal("0");

		if (phoneBillList != null && phoneBillList.size() > 0) {
			for (int i = 0; i < phoneBillList.size(); i++) {
				Map map = phoneBillList.get(i);
				if (map != null && map.get("days") != null) {
					int days = Integer.parseInt(map.get("days").toString());
					if (days > 90) {
						bb = new BigDecimal("0.03");
					} else if (days > 60) {
						bb = new BigDecimal("0.06");
					} else if (days > 30) {
						bb = new BigDecimal("0.09");
					} else {
						bb = new BigDecimal("0.12");
					}

				}
			}
		}

		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.12");
		}

		return bb;
	}

	/*
	 * 手机月均消费计算评分
	 */
	public BigDecimal dgreeEveryPhoneLocation(List<Map> phoneBillList) {
		BigDecimal bb = new BigDecimal("0");

		if (phoneBillList != null && phoneBillList.size() > 0) {
			for (int i = 0; i < phoneBillList.size(); i++) {
				Map map = phoneBillList.get(i);
				if (map != null && map.get("avg") != null) {
					BigDecimal avg = new BigDecimal(map.get("avg").toString());

					if (avg.compareTo(new BigDecimal("299")) > 0) {
						bb = new BigDecimal("0.03");
					} else if (avg.compareTo(new BigDecimal("99")) > 0) {
						bb = new BigDecimal("0.06");
					} else if (avg.compareTo(new BigDecimal("49")) > 0) {
						bb = new BigDecimal("0.09");
					} else {
						bb = new BigDecimal("0.12");
					}

				}
			}
		}

		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.12");
		}

		return bb;
	}

	/*
	 * 手机归属地得分计算
	 */
	public BigDecimal dgreePhoneLocation(List<Map> phoneBillList,
			IOTCityService oTCityService) {
		BigDecimal bb = new BigDecimal("0");

		if (phoneBillList != null && phoneBillList.size() > 0) {
			for (int i = 0; i < phoneBillList.size(); i++) {
				Map map = phoneBillList.get(i);
				if (map != null && map.get("local") != null) {
					String local = map.get("local").toString();
					Map map2 = new HashMap(1);
					map2.put("name", "%" + local + "%");
					List<OTCity> list = oTCityService.getObjByName(map2);
					if (list != null && list.size() > 0) {
						OTCity otCity = list.get(0);
						if (otCity != null) {
							int type = otCity.getType();
							if (type == 1) {
								bb = new BigDecimal("0.02");
							} else {
								bb = new BigDecimal("0.04");
							}
						}
					}

				}
			}
		}

		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.06");
		}

		return bb;
	}

	/*
	 * 月均消费
	 */
	public BigDecimal dgreeYuejun(List<Map> paylist) {
		BigDecimal bb = new BigDecimal("0");
		BigDecimal pingjuns = new BigDecimal("0");
		if (paylist != null && paylist.size() > 0) {
			for (int i = 0; i < paylist.size(); i++) {
				Map map = paylist.get(i);
				if (map != null && map.get("pingjun") != null) {
					BigDecimal pingjun = new BigDecimal(map.get("pingjun")
							.toString());
					pingjuns = pingjuns.add(pingjun);

				}
			}
		}
		if (pingjuns.compareTo(new BigDecimal("1000")) > 0) {
			bb = new BigDecimal("0.03");
		} else if (pingjuns.compareTo(new BigDecimal("500")) > 0) {
			bb = new BigDecimal("0.06");
		} else if (pingjuns.compareTo(new BigDecimal("200")) > 0) {
			bb = new BigDecimal("0.09");
		} else {
			bb = new BigDecimal("0.12");
		}

		if (pingjuns.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.12");
		}

		return bb;
	}

	/*
	 * 6 个月以上消费金额
	 */
	public BigDecimal dgreeJiaoyi(List<Map> paylist) {
		BigDecimal bb = new BigDecimal("0");
		BigDecimal sixallmoneys = new BigDecimal("0");
		if (paylist != null && paylist.size() > 0) {
			for (int i = 0; i < paylist.size(); i++) {
				Map map = paylist.get(i);
				if (map != null && map.get("sixmoney") != null) {
					BigDecimal sixallmoney = new BigDecimal(map.get("sixmoney")
							.toString());
					sixallmoneys = sixallmoneys.add(sixallmoney);

				}
			}
		}
		if (sixallmoneys.compareTo(new BigDecimal("4000")) > 0) {
			bb = new BigDecimal("0.03");
		} else if (sixallmoneys.compareTo(new BigDecimal("1500")) > 0) {
			bb = new BigDecimal("0.06");
		} else if (sixallmoneys.compareTo(new BigDecimal("0")) > 0) {
			bb = new BigDecimal("0.09");
		} else {
			bb = new BigDecimal("0.12");
		}

		if (sixallmoneys.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.12");
		}

		return bb;
	}

	/*
	 * 根据消费信息计算分值
	 */
	public BigDecimal dgreeXiaofei(List<Map> paylist) {
		BigDecimal bb = new BigDecimal("0");
		BigDecimal allmoneys = new BigDecimal("0");
		BigDecimal sixallmoneys = new BigDecimal("0");
		if (paylist != null && paylist.size() > 0) {
			for (int i = 0; i < paylist.size(); i++) {
				Map map = paylist.get(i);
				if (map != null && map.get("allmoney") != null) {
					BigDecimal allmoney = new BigDecimal(map.get("allmoney")
							.toString());
					allmoneys = allmoneys.add(allmoney);

					BigDecimal sixallmoney = new BigDecimal(map.get("sixmoney")
							.toString());
					sixallmoneys = sixallmoneys.add(sixallmoney);

				}
			}
		}
		if (allmoneys.compareTo(new BigDecimal("8000")) > 0) {
			bb = new BigDecimal("0.03");
		} else if (allmoneys.compareTo(new BigDecimal("3000")) > 0) {
			bb = new BigDecimal("0.06");
		} else if (allmoneys.compareTo(new BigDecimal("0")) > 0) {
			bb = new BigDecimal("0.09");
		} else {
			bb = new BigDecimal("0.12");
		}

		if (sixallmoneys.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.12");
		}

		return bb;
	}

	/*
	 * 根据s收入计算分值
	 */
	public BigDecimal dgreeSheBao(String shebaovalues) {
		BigDecimal bb = new BigDecimal("0");
		String[] strs = shebaovalues.split(",");
		BigDecimal start = new BigDecimal(strs[0]);
		if (start.compareTo(new BigDecimal("10000")) > 0) {
			bb = new BigDecimal("0.06");
		} else if (start.compareTo(new BigDecimal("8000")) > 0) {
			bb = new BigDecimal("0.12");
		} else if (start.compareTo(new BigDecimal("5000")) > 0) {
			bb = new BigDecimal("0.18");
		} else if (start.compareTo(new BigDecimal("3000")) > 0) {
			bb = new BigDecimal("0.24");
		} else {
			bb = new BigDecimal("0.30");
		}
		for (int i = 0; i < 6; i++) {
			String ss = strs[i];
			BigDecimal bs = new BigDecimal(ss);
			if (bs.compareTo(new BigDecimal("0")) == 0) {
				bb = new BigDecimal("0.30");
			}
		}
		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.30");
		}
		return bb;
	}

	/*
	 * 根据户籍计算分值
	 */
	public BigDecimal dgreeHuJi(String province, IOTCityService oTCityService) {
		BigDecimal bb = new BigDecimal("0");
		Map map = new HashMap(1);
		map.put("name", "%" + province + "%");
		List<OTCity> list = oTCityService.getObjByName(map);
		if (list != null && list.size() > 0) {
			OTCity otCity = list.get(0);
			if (otCity != null) {
				int type = otCity.getType();
				if (type == 1) {
					bb = new BigDecimal("0.02");
				} else {
					bb = new BigDecimal("0.04");
				}
			}
		}

		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.06");
		}
		return bb;
	}

	/*
	 * 根据婚姻状况计算分值
	 */
	public BigDecimal dgreeMerry(String currentUser, IUserService userService) {
		BigDecimal bb = new BigDecimal("0");
		Map map = new HashMap(2);
		map.put("parentId", currentUser);
		map.put("usersource", Constant.ZHENGXIN);
		List<User> list = userService.getUserByParentIdSource(map);
		if (list != null && list.size() > 0) {
			User user = list.get(0);
			if (user != null) {
				String merry = user.getMerry();
				if (merry != null && merry.length() > 0) {
					if (merry.contains("已婚")) {
						bb = new BigDecimal("0.03");
					} else if (merry.contains("未婚")) {
						bb = new BigDecimal("0.09");
					} else if (merry.contains("离婚")) {
						bb = new BigDecimal("0.15");
					}
				}
			}
		}
		if (bb.compareTo(new BigDecimal("0")) == 0) {
			bb = new BigDecimal("0.12");
		}
		return bb;
	}

	/*
	 * 根据工龄计算分值
	 */
	public BigDecimal dgreeWorkAge(String nowwork) {
		BigDecimal bb = new BigDecimal("0");

		if (nowwork.contains("月")) {
			if (nowwork.contains("年")) {
				String[] strs = nowwork.split("年");
				String str1 = strs[0];
				int year = Integer.parseInt(str1);
				if (year > 5) {
					bb = new BigDecimal("0.03");
				} else if (year > 2 && year <= 5) {
					bb = new BigDecimal("0.06");
				} else {
					bb = new BigDecimal("0.09");
				}
			} else {
				nowwork = nowwork.replace("月", "");
				int inowwork = Integer.parseInt(nowwork);
				if (inowwork > 6) {
					bb = new BigDecimal("0.09");
				} else {
					bb = new BigDecimal("0.12");
				}
			}
		} else {
			bb = new BigDecimal("0.15");
		}
		return bb;
	}

	/*
	 * 根据学校计算分值
	 */
	public BigDecimal dgreeXuexiao(String eduSchool,
			IUniversityService universityService) {
		BigDecimal bb = new BigDecimal("0");
		Map map = new HashMap(1);
		map.put("name", "%" + eduSchool + "%");
		int count = universityService.getObjByName(map);
		if (count > 0) {
			bb = new BigDecimal("0.02");
		} else {
			bb = new BigDecimal("0.06");
		}
		return bb;
	}

	/*
	 * 计算信息完整度
	 */
	public int caculateComplete(String currentUser, IUserService userService) {

		int m = Constant.allSource;
		Map mapc = new HashMap(1);
		mapc.put("parentId", currentUser);
		int count = userService.getUsercount(mapc);
		int score = count * 100 / m;
		return score;
	}

	/*
	 * 计算信用水平 735分以上 98% 685分以上 90% 630分以上 77% 585分以上 47% 535分以上 27% 485分以上 15%
	 * 435分以上 7% 385分以上 2% 385分以下 0%
	 */
	public int caculateLevel(BigDecimal fullscore) {
		int level = 0;
		if (fullscore.compareTo(new BigDecimal("735")) > 0) {
			level = 98;
		} else if (fullscore.compareTo(new BigDecimal("685")) > 0) {
			level = 90;
		} else if (fullscore.compareTo(new BigDecimal("630")) > 0) {
			level = 77;
		} else if (fullscore.compareTo(new BigDecimal("585")) > 0) {
			level = 47;
		} else if (fullscore.compareTo(new BigDecimal("535")) > 0) {
			level = 27;
		} else if (fullscore.compareTo(new BigDecimal("485")) > 0) {
			level = 15;
		} else if (fullscore.compareTo(new BigDecimal("435")) > 0) {
			level = 7;
		} else if (fullscore.compareTo(new BigDecimal("385")) > 0) {
			level = 2;
		}
		return level;
	}

	/*
	 * 计算总评分 FS = 300 +（max(5-Score,0)）*(850-300)/5
	 */
	public BigDecimal getScore(BigDecimal all) {
		BigDecimal score = new BigDecimal("300");
		if (all.compareTo(new BigDecimal(5)) < 0) {
			BigDecimal five = new BigDecimal("5");
			score = ((five.subtract(all)).multiply(new BigDecimal("110")))
					.add(score);
		}

		return score;
	}

	/*
	 * 根据学历算出分值
	 */
	public BigDecimal dgreeXueli(String xueli) {
		BigDecimal degree = new BigDecimal("0");
		if (xueli.contains("本") || xueli.contains("专")) {
			degree = new BigDecimal("0.12");
		} else if (xueli.contains("硕")) {
			degree = new BigDecimal("0.06");
		} else if (xueli.contains("高中")) {
			degree = new BigDecimal("0.30");
		} else {
			degree = new BigDecimal("0.18");
		}
		return degree;
	}

	/*
	 * 返回年龄的分值
	 */
	public BigDecimal dgreeAge(int age) {
		BigDecimal degree = new BigDecimal("0");
		if (age <= 18 || age > 61) {
			degree = new BigDecimal("0.25");
		} else if ((age > 18 && age <= 21) || (age > 55 && age <= 60)) {
			degree = new BigDecimal("0.20");
		} else if ((age > 22 && age <= 25) || (age > 50 && age <= 55)) {
			degree = new BigDecimal("0.15");
		} else if ((age > 25 && age <= 34) || (age > 41 && age <= 50)) {
			degree = new BigDecimal("0.10");
		} else if ((age > 35 && age <= 40)) {
			degree = new BigDecimal("0.05");
		}
		return degree;
	}

	/*
	 * 根据性别算出分值
	 */

	public BigDecimal dgreeSex(String sex) {
		BigDecimal degree = new BigDecimal("0");
		if (sex.contains("女")) {
			degree = new BigDecimal("0.03");
		} else {
			degree = new BigDecimal("0.06");
		}
		return degree;
	}

}
