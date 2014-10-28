package com.lkb.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.ICellService;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IIdentifyLocationService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IOTCityService;
import com.lkb.service.IOrderService;
import com.lkb.service.IPayInfoService;
import com.lkb.service.IPhoneNumService;
import com.lkb.service.ISheBaoService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUniversityService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.service.IYuEBaoService;
import com.lkb.thirdUtil.FinanceInformation;
import com.lkb.util.IdentifyUtil;
import com.lkb.util.PhoneUtil;
import com.lkb.util.report.JsonUtils;
import com.lkb.util.report.OrderUtil;
import com.lkb.util.report.PayUtils;
import com.lkb.util.report.PhoneUtils;
import com.lkb.util.report.ReportUtil;
import com.lkb.util.report.ShebaoUtil;
import com.lkb.util.report.UserUtil;
import com.lkb.util.report.YueBaoUtil;
import com.lkb.util.taobao.YuEBaoUtil;
@Controller
public class ReportController {
	
	@Resource
	private IUserService userService;
	@Resource
	private IMobileTelService mobileTelService;
	@Resource
	private IYuEBaoService yuEBaoService;

	@Resource
	private IOrderService orderService;

	@Resource
	private IPayInfoService payInfoService;

	@Resource
	private ISheBaoService shebaoService;

	@Resource
	private IPhoneNumService phoneNumService;
	@Resource
	private IUnicomDetailService unicomDetailService;
	@Resource
	private IUnicomTelService unicomTelService;
	@Resource
	private IDianXinDetailService dianxinDetailService;
	@Resource
	private IDianXinTelService dianxinTelService;
	@Resource
	private IWarningService warningService;

	@Resource
	private IDianXinTelService dianXinTelService;
	@Resource
	private IMobileDetailService mobileDetailService;
	
	@Resource
	private IIdentifyLocationService identifyLocationService;
	
	@Resource
	private IUniversityService universityService;
	
	@Resource
	private IOTCityService oTCityService;
	
	@Resource
	private ICellService ics;
	
	
	/*
	 * 生成报告
	 */
	@RequestMapping(value = "/report")
	public String report(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
//		long startTime = System.currentTimeMillis();
		String currentUser = (String) req.getSession().getAttribute(
				"currentUser");
		if (currentUser == null) {
			return "redirect:/lg/login.html";
		}
		
		if (currentUser != null) {
			JsonUtils jsonUtils = new JsonUtils();
			User cuser = userService.findById(currentUser);
			model.addAttribute("user", cuser);
			Date nowDate = new Date();
			model.addAttribute("nowDate", nowDate);
			String realName = cuser.getRealName();

			// 获取电商用户名集合
			Map canMap = new HashMap(1);
			canMap.put("parentId", currentUser);
			List<User> list = userService.getUserByParentId(canMap);
			
			List dsloginNames = new ArrayList();
			List dsloginNamesJD = new ArrayList();
			List dsloginNamesTAOBAO = new ArrayList();
			List alipayNames = new ArrayList();
			List ydloginNames = new ArrayList();
			List ltloginNames = new ArrayList();
			List dxloginNames = new ArrayList();
			List sb_loginNames= new ArrayList();//社保
			List zxloginNames= new ArrayList();//征信
			List xxloginNames= new ArrayList();//学信
			
			List usersourceList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				User user = (User) list.get(i);
				String usersource = user.getUsersource();
				String loginName = user.getLoginName();
				
				Map usersourceMap = new HashMap();
				usersourceMap.put("userSource", usersource);
				usersourceMap.put("loginName", loginName);
				usersourceList.add(usersourceMap);
				if (usersource.contains(Constant.JD)) {
					dsloginNamesJD.add(loginName);
					dsloginNames.add(loginName);
				} else if ( usersource.contains(Constant.TAOBAO)) {
					dsloginNamesTAOBAO.add(loginName);
					dsloginNames.add(loginName);
				} else if (usersource.contains(Constant.ZHIFUBAO)) {
					alipayNames.add(loginName);
				} else if (usersource.contains(Constant.YIDONG)) {
					ydloginNames.add(loginName);
				} else if (usersource.contains(Constant.LIANTONG)) {
					ltloginNames.add(loginName);
				} else if (usersource.contains(Constant.DIANXIN)) {
					dxloginNames.add(loginName);
				} else if(usersource.contains(Constant.SHSHEBAO)){
					sb_loginNames.add(loginName);
				}else if(usersource.contains(Constant.SZSHEBAO)){
					sb_loginNames.add(loginName);
				}else if(usersource.contains(Constant.ZHENGXIN)){
					zxloginNames.add(loginName);
				}else if(usersource.contains(Constant.XUEXIN)){
					xxloginNames.add(loginName);
				}
			}
			

			// 是否实名认证
			UserUtil userUtil = new UserUtil();
			String isRealName = userUtil.isRalName(currentUser, realName,
					userService);
			model.addAttribute("isRealName", isRealName);

			// 常用手机号
			String phones = userUtil.getPhone(currentUser, userService);
			model.addAttribute("phones", phones);

			// 获得工龄
			ShebaoUtil shebaoUtil = new ShebaoUtil();
			Map workmap = shebaoUtil.getWorkYear(shebaoService, currentUser);
			model.addAttribute("workmap", workmap);

			// 身份证获得信息
			IdentifyUtil iutil = new IdentifyUtil();
			String identify = cuser.getIdcard();
			Map identifyMap = iutil.getIdentify(identify,
					identifyLocationService);
			model.addAttribute("identify", identifyMap);

			// 工作地
			String workLocation = userUtil.getWorkLocation(cuser, userService);
			model.addAttribute("workLocation", workLocation);

			// 获取一些基本信息
			Map somethingMap = userUtil.getSomeInfo(currentUser, userService);
			model.addAttribute("somethingMap", somethingMap);

			// 获取网购消费信息
			OrderUtil orderUtil = new OrderUtil();
			List<Map> paylist = orderUtil.getPay(userService, orderService,
					currentUser);
			model.addAttribute("paylist", paylist);

			// 获取支付宝消费信息
			PayUtils payutils = new PayUtils();
			Map payMap = payutils.getMap(payInfoService, alipayNames);
			model.addAttribute("payMap", payMap);

			// 金融信息：支出部分
			Map jinMap = orderUtil.getZhichu(userService, orderService,
					dsloginNames);
			model.addAttribute("jinMap", jinMap);

			// 获取通话最多的前10条记录
			PhoneUtil phoneUtil = new PhoneUtil();
			List<Map> phoneList = phoneUtil.getPhoneList(currentUser,
					userService, dianxinDetailService, mobileDetailService,
					unicomDetailService);
			List<Map> phoneList2 = new ArrayList<Map>();
			if (phoneList != null && phoneList.size() > 10) {
				phoneList2 = phoneList.subList(0, 10);
			} else {
				phoneList2 = phoneList;
			}
			model.addAttribute("phoneList", phoneList2);

			// 金融信息中的收入
			
			List listIncome = null;
			if(sb_loginNames!=null && sb_loginNames.size()>0){
				FinanceInformation finance = new FinanceInformation();
				 listIncome = finance.getIncomeInformation(sb_loginNames,
						shebaoService);
				model.addAttribute("incomeMap", listIncome.get(0));
			}

			// 余额宝+邮箱账单
			YuEBaoUtil yuebaoUtil = new YuEBaoUtil();
			// cuser.getLoginName() 查询的支付宝账户,可能有问题

			Map yuebaoMap = yuebaoUtil.getYuEBao(currentUser, yuEBaoService,
					userService);
			model.addAttribute("yuebaoMap", yuebaoMap);

			// 话费
			List<Map> phoneBillList = phoneUtil.getPhoneBillList(currentUser,
					userService, dianxinDetailService, mobileTelService,
					unicomTelService, dianXinTelService, unicomDetailService,
					phoneNumService, mobileDetailService);
			model.addAttribute("phoneBillList", phoneBillList);

			// 获得支付宝每个月的消费金额
			Map payMap2 = payutils.getEveryAmount(payInfoService, alipayNames);
			String paytimes = payMap2.get("time").toString();
			String payvalues = payMap2.get("values").toString();
			paytimes = jsonUtils.zuzhuangDate(paytimes);
			payvalues = jsonUtils.zuzhuangNum(payvalues);
			model.addAttribute("paytimes", paytimes);
			model.addAttribute("payvalues", payvalues);

			// 每个月的支出金额
			Map orderMap2 = orderUtil.getEveryAmountFu(orderService,
					dsloginNames, ydloginNames, ltloginNames, dxloginNames,
					dianXinTelService, unicomTelService, mobileTelService);
			String paytimes21 = orderMap2.get("ordertime").toString();
			String ordervalues21 = orderMap2.get("ordervalues").toString();
			paytimes21 = jsonUtils.zuzhuangDate(paytimes21);
			ordervalues21 = jsonUtils.zuzhuangNum(ordervalues21);
			model.addAttribute("paytimes21", paytimes21);
			model.addAttribute("ordervalues21", ordervalues21);

			// 获得网购每月消费总金额
			if(dsloginNames!=null&&dsloginNames.size()>0){
				Map orderMap = orderUtil.getEveryAmount(orderService, dsloginNames);
				String paytimes2 = orderMap.get("ordertime").toString();
				String ordervalues2 = orderMap.get("ordervalues").toString();
				paytimes2 = jsonUtils.zuzhuangDate(paytimes2);
				ordervalues2 = jsonUtils.zuzhuangNum(ordervalues2);
				model.addAttribute("paytimes2", paytimes2);
				model.addAttribute("ordervalues2", ordervalues2);

			}
		
			// 获得储蓄信息
			YueBaoUtil yueBaoUtil = new YueBaoUtil();
			Map yuMap = yueBaoUtil.getEveryAmount(yuEBaoService, currentUser,
					userService);
			String yuebaotime = yuMap.get("yuebaotime").toString();
			String yuebaovalues = yuMap.get("yuebaovalues").toString();
			yuebaotime = jsonUtils.zuzhuangDate(yuebaotime);
			yuebaovalues = jsonUtils.zuzhuangNum(yuebaovalues);
			model.addAttribute("yuebaotime", yuebaotime);
			model.addAttribute("yuebaovalues", yuebaovalues);

			// 收入每月信息
			Map shebaoMap = null;
			if(sb_loginNames!=null && sb_loginNames.size()>0){
				shebaoMap = shebaoUtil.getEveryAmount(shebaoService,
						sb_loginNames);
				String shebaotime = shebaoMap.get("shebaotime").toString();
				String shebaovalues = shebaoMap.get("shebaovalues").toString();
				shebaotime = jsonUtils.zuzhuangDate(shebaotime);
				shebaovalues = jsonUtils.zuzhuangNum(shebaovalues);
				model.addAttribute("shebaotime", shebaotime);
				model.addAttribute("shebaovalues", shebaovalues);
			}
		

			// 每月话费信息
			PhoneUtils phoneUtil2 = new PhoneUtils();
			Map phone2Map = phoneUtil2.getEveryAmount(dianXinTelService,
					unicomTelService, mobileTelService, ydloginNames,
					ltloginNames, dxloginNames);
			String phonetime = phone2Map.get("phonetime").toString();
			String phonevalues = phone2Map.get("phonevalues").toString();
			phonetime = jsonUtils.zuzhuangDate(phonetime);
			phonevalues = jsonUtils.zuzhuangNum(phonevalues);
			model.addAttribute("phonetime", phonetime);
			model.addAttribute("phonevalues", phonevalues);

			ReportUtil reportUtil = new ReportUtil();
			Map reportMap = reportUtil.caculate(identifyMap, somethingMap,
					shebaoService, currentUser, userService, universityService,
					workmap, oTCityService, shebaoMap, paylist, phoneBillList,
					phoneList, listIncome, jinMap, yuebaoMap);
			model.addAttribute("reportMap", reportMap);
		}
//		System.out.println("模型耗时：" + (System.currentTimeMillis() - startTime)/60 + "秒");
		return "report2";
	}

}
