package com.lkb.thirdUtil.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Args;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.UnicomDetail;
import com.lkb.bean.UnicomFlow;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.constant.Constant;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinFlowDetailService;
import com.lkb.service.IDianXinFlowService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileMessageService;
import com.lkb.service.IMobileOnlineBillService;
import com.lkb.service.IMobileOnlineListService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.ITelcomMessageService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomFlowBillService;
import com.lkb.service.IUnicomFlowService;
import com.lkb.service.IUnicomMessageService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.httpclient.entity.SpeakBillPojo;
import com.lkb.util.httpclient.util.CommonUtils;
import com.lkb.util.thread.Task;
import com.lkb.warning.WarningUtil;

/**
 * 所有登陆类的父类
 * 
 * @author fastw
 * @param <T>
 * @date 2014-8-3 下午9:13:42
 */
public abstract class BaseInfoMobile extends BaseInfo {
	protected static Logger log = Logger.getLogger(BaseInfoMobile.class);

	public BaseInfoMobile() {
	}

	protected IDianXinTelService dianXinTelService;
	protected IDianXinDetailService dianXinDetailService;
	protected IMobileTelService mobileTelService;
	protected IMobileDetailService mobileDetailService;
	protected IMobileOnlineBillService mobileOnlineBillService;
	protected IMobileOnlineListService mobileOnlineListService;
	public IUnicomTelService unicomTelService;
	public IUnicomDetailService unicomDetailService;
	public IUnicomMessageService unicomMessageService;
	protected IMobileMessageService mobileMessageService;
	public IUnicomFlowService unicomFlowService;
	protected IUnicomFlowBillService unicomFlowBillService;

	public ITelcomMessageService telcomMessageService;

	public IDianXinFlowService dianXinFlowService;
	public IDianXinFlowDetailService dianXinFlowDetailService;

	/**
	 * userSource 电信:Constant.DIANXIN 移动:Constant.YIDONG 联通:Constant.LIANTONG
	 */
	public String userSource = "";

	public BaseInfoMobile setUnicomFlowService(
			IUnicomFlowService unicomFlowService) {
		this.unicomFlowService = unicomFlowService;
		return this;
	}

	public BaseInfoMobile setUnicomFlowBillService(
			IUnicomFlowBillService unicomFlowBillService) {
		this.unicomFlowBillService = unicomFlowBillService;
		return this;
	}

	public BaseInfoMobile setMobileMessageService(
			IMobileMessageService mobileMessageService) {
		this.mobileMessageService = mobileMessageService;
		return this;
	}

	public BaseInfoMobile setUnicomTelService(IUnicomTelService unicomTelService) {
		this.unicomTelService = unicomTelService;
		return this;
	}

	public BaseInfoMobile setUnicomDetailService(
			IUnicomDetailService unicomDetailService) {
		this.unicomDetailService = unicomDetailService;
		return this;
	}

	public BaseInfoMobile setUnicomMessageService(
			IUnicomMessageService unicomMessageService) {
		this.unicomMessageService = unicomMessageService;
		return this;
	}

	public BaseInfoMobile setDianXinTelService(
			IDianXinTelService dianXinTelService) {
		this.dianXinTelService = dianXinTelService;
		return this;
	}

	public BaseInfoMobile setDianXinDetailService(
			IDianXinDetailService dianXinDetailService) {
		this.dianXinDetailService = dianXinDetailService;
		return this;
	}

	public BaseInfoMobile setMobileTelService(IMobileTelService mobileTelService) {
		this.mobileTelService = mobileTelService;
		return this;
	}

	public BaseInfoMobile setMobileDetailService(
			IMobileDetailService mobileDetailService) {
		this.mobileDetailService = mobileDetailService;
		return this;
	}
	
	public BaseInfoMobile setMobileOnlineBillService(
			IMobileOnlineBillService mobileOnlineBillService) {
		this.mobileOnlineBillService = mobileOnlineBillService;
		return this;
	}

	public BaseInfoMobile setMobileOnlineListService(
			IMobileOnlineListService mobileOnlineListService) {
		this.mobileOnlineListService = mobileOnlineListService;
		return this;
	}

	public BaseInfoMobile setTelcomMessageService(
			ITelcomMessageService telcomMessageService) {
		this.telcomMessageService = telcomMessageService;
		return this;
	}

	public BaseInfoMobile setDianXinFlowService(IDianXinFlowService dianXinFlowService) {
		this.dianXinFlowService = dianXinFlowService;
		return this;
	}
	
	public BaseInfoMobile setDianXinFlowDetailService(IDianXinFlowDetailService dianXinFlowDetailService) {
		this.dianXinFlowDetailService = dianXinFlowDetailService;
		return this;
	}

	/**
	 * 本地测试
	 * 
	 * @param login
	 */
	public BaseInfoMobile(Login login) {
		super(login);
	}

	public BaseInfoMobile(Login login, IWarningService warningService,
			String ConstantNum, String currentUser) {
		super(login, warningService, ConstantNum, currentUser);
	}

	public BaseInfoMobile(Login login, String ConstantNum, String currentUser) {
		super(login, ConstantNum, currentUser);
	}

	/**
	 * 采集个人信息和账单信息
	 * 
	 * @param base
	 */
	public void addTask_1(BaseInfo base) {
		base.getLogin().setType(1);
		Task.addTask(base);
	}

	/**
	 * 采集通话记录
	 * 
	 * @param base
	 */
	public void addTask_2(BaseInfo base) {
		base.getLogin().setType(2);
		Task.addTask(base);
	}

	/**
	 * 采集其他记录,以后可增加其他task
	 * 
	 * @param base
	 */
	public void addTask_4(BaseInfo base) {
		base.getLogin().setType(4);
		Task.addTask(base);
	}

	/**
	 * 个人信息错误 打印错误日志
	 * 
	 * @param e
	 */
	public void writeLogByInfo(Exception e) {
		log.error(constantNum + "_" + login.getLoginName() + "个人信息异常,错误信息:", e);
		try {
			new WarningUtil().warning(warningService, currentUser, constantNum
					+ "_GRXX");// 个人信息
		} catch (Exception e1) {
		}
	}

	/**
	 * 账单信息错误 打印错误日志
	 * 
	 * @param e
	 */
	public void writeLogByZhangdan(Exception e) {
		log.error(constantNum + "_" + login.getLoginName() + "账单记录异常,错误信息:", e);
		try {
			new WarningUtil().warning(warningService, currentUser, constantNum
					+ "_ZDJL");// 账单记录
		} catch (Exception e1) {
		}
	}

	/**
	 * 历史记录错误,打印错误日志
	 * 
	 * @param e
	 */
	public void writeLogByHistory(Exception e) {
		log.error(constantNum + "_" + login.getLoginName() + "通话记录异常,错误信息:", e);
		try {
			new WarningUtil().warning(warningService, currentUser, constantNum
					+ "_THJL");// 通话记录
		} catch (Exception e1) {
		}
	}

	/**
	 * 历史记录错误,打印错误日志
	 * 
	 * @param e
	 */
	public void writeLogByMessage(Exception e) {
		log.error(constantNum + "_" + login.getLoginName() + "短信记录记录异常,错误信息:",
				e);
		try {
			new WarningUtil().warning(warningService, currentUser, constantNum
					+ "_DXJL");// 通话记录
		} catch (Exception e1) {
		}
	}

	/**
	 * 发送通话记录错误警告
	 * 
	 * @param errormsg
	 */
	public void sendWarningCallHistory(String errormsg) {
		String s = "[" + constantNum + "]-" + login.getLoginName() + "通话记录:"
				+ errormsg;
		log.error(s);
		try {
			new WarningUtil().warning(warningService, currentUser, constantNum
					+ "_THJL");
		} catch (Exception e1) {
		}
	}
	
	/**
	 * 发送流量账单和详单错误警告
	 * 
	 * @param errormsg
	 */
	public void sendWarningFlow(String errormsg) {
		String s = "[" + constantNum + "]-" + login.getLoginName() + "流量账单和详单:"
				+ errormsg;
		log.error(s);
		try {
			new WarningUtil().warning(warningService, currentUser, constantNum
					+ "_THJL");
		} catch (Exception e1) {
		}
	}

	/**
	 * 发送短信错误警告
	 * 
	 * @param errormsg
	 */
	public void sendWarningMessageHistory(String errormsg) {
		String s = "[" + constantNum + "]-" + login.getLoginName() + "短信:"
				+ errormsg;
		log.error(s);
		try {
			new WarningUtil().warning(warningService, currentUser, s);
		} catch (Exception e1) {
		}
	}
	/**流量账单
     * @param e
     */
    public void writeLogByFlowBill(Exception e){
    	log.error(constantNum+"_"+loginName+"流量账单异常,错误信息:",e);
    	try{
    		new WarningUtil().warning(warningService,currentUser, constantNum+"_LLZD");//流量账单
    		
    	}catch(Exception e1){}
    }
    /**流量相当
     * @param e
     */
    public void writeLogByFlowList(Exception e){
    	log.error(constantNum+"_"+loginName+"流量详单异常,错误信息:",e);
    	try{
    		new WarningUtil().warning(warningService,currentUser, constantNum+"_LLXD");//流量详单
    	}catch(Exception e1){}
    }
	/**
	 * 保存个人信息,以后不再执行getUserInfo方法 子类方法必须重新getInfo方法
	 * 
	 * @return
	 */
	public User spiderInfo() {
		return null;
	}

	@Deprecated
	public List spiderMonthlyBill() {
		return null;
	}

	@Deprecated
	public List spiderCallHistory() {
		return null;
	}

	public User getUser() {
		User user = this.spiderInfo();
		if (user != null) {
			User user1 = null;
			boolean b = false;
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("parentId", this.currentUser);
			map1.put("usersource", userSource);
			map1.put("loginName", this.loginName);
			List<User> list = null;
			try {
				list = userService.getUserByParentIdSource(map1);
			} catch (Exception e) {
			}
			if (list != null && list.size() > 0) {
				user1 = list.get(0);
				b = true;
			} else {
				user1 = user;
				UUID uuid = UUID.randomUUID();
				user1.setId(uuid.toString());
			}
			user1.setUsersource(this.userSource);
			user1.setUsersource2(this.userSource);
			user1.setLoginName(this.loginName);
			user1.setParentId(this.currentUser);
			user1.setModifyDate(new Date());
			user1.setPhone(this.loginName);
			user1.setLoginPassword("");
			if (StringUtils.isEmpty(user1.getEmail())) {
				user1.setEmail("");
			} else {
				user1.setEmail(user1.getEmail());
			}
			user1.setUserName(user.getUserName());
			user1.setRealName(user.getRealName());
			user1.setIdcard(user.getIdcard());
			user1.setAddr(user.getAddr());
			user1.setEmail(user.getEmail());
			user1.setFixphone(user.getFixphone());
			user1.setPhoneRemain(user.getPhoneRemain());
			user1.setPackageName(user1.getPackageName());
			saveUser(user1, b);
			return user1;
		}
		return user;
	}

	private boolean saveUser(User user, boolean b) {
		try {
			if (b) {
				userService.update(user);
			} else {
				userService.saveUser(user);
			}
		} catch (Exception e) {
			writeLogByInfo(e);
			return false;
		}
		return true;
	}

	public void parseBegin() {
		super.parseBegin(this.userSource);
	}

	public void parseEnd() {
		super.parseBegin(this.userSource);
	}

	/**
	 * 获取几个月的最大时间
	 * 
	 * @param num
	 *            几个月的
	 * @return
	 */
	public Set<Date> getMonthlyBillMaxNumTel() {
		Args.notNull(this.userSource, "userSource");
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("teleno", this.loginName);
			List list = null;

			if (this.userSource.equals(Constant.YIDONG)) {
				list = mobileTelService.getMaxNumTel(map);
			} else if (this.userSource.equals(Constant.DIANXIN)) {
				list = dianXinTelService.getMaxNumTel(map);
			} else {

			}
			Set<Date> set = null;
			if (list != null && list.size() > 0) {
				set = new LinkedHashSet<Date>();
				set.addAll(list);
			}
			return set;
		} catch (Exception e) {
			writeLogByHistory(e);
		}
		return null;
	}

	/**
	 * @param isSpiderCurrent
	 *            是否采集当月信息默认不采集 默认false
	 * @param 默认格式yyyyMM
	 * @return LinkedList
	 */
	private LinkedList<SpeakBillPojo> getSpiderMonths(boolean isSpiderCurrent,
			String formatStr) {
		SpeakBillPojo ssa = null;
		LinkedList<SpeakBillPojo> list = new LinkedList<SpeakBillPojo>();
		if (formatStr == null) {
			formatStr = "yyyyMM";
		}
		List<String> ms = DateUtils.getMonths(6, formatStr);
		// 默认查到12个月
		Set<Date> set = getMonthlyBillMaxNumTel();
		Date dtime = null;
		// 取出12个月的最大时间 比如: 201407
		if (set != null && set.size() > 0) {
			dtime = set.iterator().next();
		}
		Date d = null;
		// 遍历一遍 ,,ms是当前要保存到数据库的6个月份
		for (String startDate : ms) {
			d = DateUtils.StringToDate(startDate, formatStr);

			if (dtime != null) {
				// 检测是否数据库有相等月份,如果包含,是否数据库最大月份在里边,主要是更新数据库中最大月份的话费清单,其他跳过
				//
				if (set.contains(d)) {
					if (d.getTime() == dtime.getTime()) {
						ssa = new SpeakBillPojo();
						ssa.setSpiderCurrentMonth(true);
						ssa.setMonth(startDate);
						ssa.setFormat(formatStr);
						list.add(ssa);
					}
				} else {
					ssa = new SpeakBillPojo();
					ssa.setMonth(startDate);
					ssa.setFormat(formatStr);
					list.add(ssa);
				}
			} else {
				break;
			}
		}
		if (list.size() != 0) {
			return list;
		} else {
			// 判断是否采集当月
			if (isSpiderCurrent) {
				ms.remove(0);
			}

			for (String date : ms) {
				ssa = new SpeakBillPojo(date);
				ssa.setFormat(formatStr);
				list.add(ssa);
			}
		}
		return list;
	}

	public LinkedList<SpeakBillPojo> getMonthlyBillAccess(String url, String key) {
		return getMonthlyBillAccess(url, key, null, false, null);
	}

	/**
	 * @param key
	 *            对应月份查询的key
	 * @param url
	 *            查询地址
	 * @param param
	 *            查询条件 post提交
	 * @param isSpiderCurrent
	 *            是否采集当月信息
	 * @param formatStr
	 *            格式化类型
	 * @param values
	 *            键值对应 对应键值 一般第一个参数为key第二个为value
	 * @return
	 */

	public LinkedList<SpeakBillPojo> getMonthlyBillAccess(String url,
			String key, Map<String, String> param, boolean isSpiderCurrent,
			String formatStr, String... values) {
		LinkedList<SpeakBillPojo> linkedList = getSpiderMonths(isSpiderCurrent,
				formatStr);
		return getText(url, key, param, linkedList, 0, values);
	}

	private LinkedList<SpeakBillPojo> getText(String url, String key,
			Map<String, String> param, LinkedList<SpeakBillPojo> linkedList,
			int begin, String... values) {
		SpeakBillPojo simple = null;
		try {
			String url1 = null;
			if (param != null && values != null) {
				for (int i = 0; i < values.length;) {
					param.put(values[i], values[i + 1]);
					i = i + 2;
				}
			} else {
				if (url.indexOf("?") == -1) {
					url1 = url + "?";
				} else {
					url1 = url + "&";
				}
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						url1 = url1 + values[i] + "=" + values[i + 1] + "&";
					}
				}
				url1 = url1 + key + "=";
			}
			int i = begin;
			for (i = 0; i < linkedList.size(); i++) {
				simple = linkedList.get(i);
				if (param != null) {
					param.put(key, simple.getMonth());
					simple.setText(cutil.post(url, param));
				} else {
					simple.setText(cutil.get(url1 + simple.getMonth()));
				}
			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
		}
		return linkedList;
	}

	public LinkedList<SpeakBillPojo> gatherMonthlyBill() {
		return null;
	}

	public void saveTel() {
		Args.notNull(this.userSource, "userSource");
		LinkedList<SpeakBillPojo> simpleList = this.gatherMonthlyBill();
		try {
			if (simpleList != null && simpleList.size() > 0) {
				if (this.userSource.equals(Constant.YIDONG)) {
					MobileTel tel = null;
					for (SpeakBillPojo simpleMonthlyBillPojo : simpleList) {
						tel = simpleMonthlyBillPojo.getMobileTel();
						if (tel != null) {
							tel.setTeleno(this.loginName);
							tel.setcTime(simpleMonthlyBillPojo.getFormatDate());
							if (simpleMonthlyBillPojo.isSpiderCurrentMonth()) {
								SpeakBillPojo billPojo = findTelData(tel
										.getcTime());
								tel.setId(billPojo.getMobileTel().getId());
								mobileTelService.update(tel);
							} else {
								UUID uuid = UUID.randomUUID();
								tel.setId(uuid.toString());
								mobileTelService.saveMobileTel(tel);
							}
						}

					}
				} else if (this.userSource.equals(Constant.DIANXIN)) {
					DianXinTel tel = null;
					for (SpeakBillPojo simpleMonthlyBillPojo : simpleList) {
						tel = simpleMonthlyBillPojo.getDianxinTel();
						if (tel != null) {
							tel.setTeleno(this.loginName);
							tel.setcTime(simpleMonthlyBillPojo.getFormatDate());
							if (simpleMonthlyBillPojo.isSpiderCurrentMonth()) {
								SpeakBillPojo billPojo = findTelData(tel
										.getcTime());
								tel.setId(billPojo.getDianxinTel().getId());
								dianXinTelService.update(tel);
							} else {
								UUID uuid = UUID.randomUUID();
								tel.setId(uuid.toString());
								dianXinTelService.saveDianXinTel(tel);
							}
						}
					}
				} else {

				}
			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
		}

	}

	private SpeakBillPojo findTelData(Date ctime) {
		SpeakBillPojo pojo = new SpeakBillPojo();
		try {
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("cTime", ctime);
			if (this.userSource.equals(Constant.YIDONG)) {
				map2.put("phone", this.loginName);
				List<Map> list2 = mobileTelService.getMobileTelBybc(map2);
				MobileTel tel = (MobileTel) list2.get(0);
				pojo.setMobileTel(tel);
			} else if (this.userSource.equals(Constant.DIANXIN)) {
				map2.put("teleno", this.loginName);
				List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
				DianXinTel tel = (DianXinTel) list2.get(0);
				pojo.setDianxinTel(tel);
			} else {

			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
		}
		return pojo;
	}

	/**
	 * @param 默认格式yyyyMM
	 * @param numMonth
	 *            几个月 默认6个月
	 * @return LinkedList 月份和最大时间
	 */
	public LinkedList<SpeakBillPojo> getSpiderCallHistoryMonths(
			String formatStr, int numMonth) {
		LinkedList<SpeakBillPojo> list = new LinkedList<SpeakBillPojo>();
		if (formatStr == null) {
			formatStr = "yyyyMM";
		}
		if (numMonth == 0) {
			numMonth = 6;
		}
		List<String> ms = DateUtils.getMonths(numMonth, formatStr);
		SpeakBillPojo pojo = null;
		Date d = getMaxTime();
		for (String string : ms) {
			pojo = new SpeakBillPojo();
			pojo.setMonth(string);
			pojo.setBigTime(d);
			list.add(pojo);
		}
		return list;
	}

	public LinkedList<SpeakBillPojo> gatherMessage() {
		return null;
	}

	/**
	 * @param 默认格式yyyyMM
	 * @param numMonth
	 *            几个月 默认6个月
	 * @return LinkedList 月份和最大时间
	 */
	public LinkedList<SpeakBillPojo> getSpiderMessageMonths(String formatStr,
			int numMonth) {
		LinkedList<SpeakBillPojo> list = new LinkedList<SpeakBillPojo>();
		if (formatStr == null) {
			formatStr = "yyyyMM";
		}
		if (numMonth == 0) {
			numMonth = 6;
		}
		List<String> ms = DateUtils.getMonths(numMonth, formatStr);
		SpeakBillPojo pojo = null;
		Date d = (Date) getMaxMessageTime();
		for (String string : ms) {
			pojo = new SpeakBillPojo();
			pojo.setMonth(string);
			pojo.setBigTime(d);
			list.add(pojo);
		}
		return list;
	}

	public void saveMessage() {
		LinkedList<SpeakBillPojo> simpleList = this.gatherMessage();
		try {
			SpeakBillPojo pojo = null;
			if (simpleList != null && simpleList.size() > 0) {
				if (this.userSource.equals(Constant.YIDONG)) {
					for (int i = simpleList.size() - 1; i >= 0; i--) {
						pojo = simpleList.get(i);
						if (pojo.getMobileMessList() != null
								&& pojo.getMobileMessList().size() != 0) {
							mobileMessageService.insertbatch(pojo
									.getMobileMessList());
						}
					}
				} else if (this.userSource.equals(Constant.DIANXIN)) {

				} else {

				}
			}
		} catch (Exception e) {
			writeLogByHistory(e);
		}
	}

	/**
	 * 数据库默认保存从小到大,防止保存入库 ,所以入库原则保证插入队列的顺序
	 */
	public LinkedList<SpeakBillPojo> gatherCallHistory() {
		return null;
	}

	/**
	 * 数据库默认保存从小到大,防止保存入库 ,所以入库原则保证插入队列的顺序
	 */
	public void saveCallHistory() {
		LinkedList<SpeakBillPojo> simpleList = this.gatherCallHistory();
		SpeakBillPojo pojo = null;
		try {
			if (simpleList != null && simpleList.size() > 0) {
				if (this.userSource.equals(Constant.YIDONG)) {
					for (int i = simpleList.size() - 1; i >= 0; i--) {
						pojo = simpleList.get(i);
						if (pojo.getMobileDetailList() != null
								&& pojo.getMobileDetailList().size() != 0) {
							mobileDetailService.insertbatch(pojo
									.getMobileDetailList());
						}
					}
				} else if (this.userSource.equals(Constant.DIANXIN)) {
					for (int i = simpleList.size() - 1; i >= 0; i--) {
						pojo = simpleList.get(i);
						if (pojo.getDianxinDetailList() != null
								&& pojo.getDianxinDetailList().size() != 0) {
							dianXinDetailService.insertbatch(pojo
									.getDianxinDetailList());
						}
					}
				} else {

				}
			}
		} catch (Exception e) {
			writeLogByHistory(e);
		}

	}

	@Deprecated
	public void updateTel(Date ctime, BigDecimal total) {
		try {
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("teleno", this.loginName);
			map2.put("cTime", ctime);
			if (this.userSource.equals(Constant.YIDONG)) {
				map2.put("phone", this.loginName);
				List<Map> list2 = mobileTelService.getMobileTelBybc(map2);
				MobileTel tel = (MobileTel) list2.get(0);
				tel.setcAllPay(total);
				mobileTelService.update(tel);
			} else if (this.userSource.equals(Constant.DIANXIN)) {
				List<Map> list2 = dianXinTelService.getDianXinTelBybc(map2);
				DianXinTel tel = (DianXinTel) list2.get(0);
				tel.setcAllPay(total);
				dianXinTelService.update(tel);
			} else {

			}

		} catch (Exception e) {
			writeLogByZhangdan(e);
		}
	}

	@Deprecated
	public List getMonthlyBill() {
		List list = this.spiderMonthlyBill();
		try {
			if (list != null && list.size() > 0) {
				if (this.userSource.equals(Constant.YIDONG)) {
					MobileTel tel = null;
					for (int i = 0; i < list.size(); i++) {
						tel = (MobileTel) list.get(i);
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
						tel.setTeleno(this.loginName);
						mobileTelService.saveMobileTel(tel);
					}
				} else if (this.userSource.equals(Constant.DIANXIN)) {
					DianXinTel tel = null;
					for (int i = 0; i < list.size(); i++) {
						tel = (DianXinTel) list.get(i);
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
						tel.setTeleno(this.loginName);
						dianXinTelService.saveDianXinTel(tel);
					}
				} else {

				}
			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
		}
		return list;
	}

	/**
	 * 获取通话记录的最大时间
	 * 
	 * @param num
	 *            几个月的
	 * @return
	 */
	public Date getMaxTime() {
		Args.notNull(this.userSource, "userSource");
		try {
			if (this.userSource.equals(Constant.YIDONG)) {
				MobileDetail tel = new MobileDetail();
				tel.setPhone(this.loginName);
				tel = mobileDetailService.getMaxTime(tel);
				if (tel != null) {
					return tel.getcTime();
				}
			} else if (this.userSource.equals(Constant.DIANXIN)) {
				DianXinDetail dx = dianXinDetailService
						.getMaxTime(new DianXinDetail(login.getLoginName()));
				if (dx != null) {
					return dx.getcTime();
				}
			} else {
				// 联通
				UnicomDetail uni = unicomDetailService
						.getMaxTime(new UnicomDetail(login.getLoginName()));
				if (uni != null)
					return uni.getcTime();
			}
		} catch (Exception e) {
			writeLogByHistory(e);
		}
		return null;
	}

	/**
	 * 获取最大时间
	 * 
	 * @param num
	 *            几个月的
	 * @return
	 */
	public Object getMaxMessageTime() {
		Args.notNull(this.userSource, "userSource");
		try {
			if (this.userSource.equals(Constant.YIDONG)) {
				return mobileMessageService.getMaxSentTime(this.loginName);
			} else if (this.userSource.equals(Constant.DIANXIN)) {
				// elcomMessage maxTimeMessage
				return telcomMessageService.getMaxSentTime(this.loginName);
			} else {
				// 联通
				return unicomMessageService.getMaxSentTime(this.loginName);
			}
		} catch (Exception e) {
			writeLogByHistory(e);
		}
		return null;
	}

	@Deprecated
	public List getCallHistory() {
		List list = this.spiderCallHistory();
		try {
			if (list != null && list.size() > 0) {
				if (this.userSource.equals(Constant.YIDONG)) {
					MobileDetail tel = null;
					for (int i = 0; i < list.size(); i++) {
						tel = (MobileDetail) list.get(i);
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
						tel.setPhone(this.loginName);
						mobileDetailService.saveMobileDetail(tel);
					}
				} else if (this.userSource.equals(Constant.DIANXIN)) {
					DianXinDetail tel = null;
					for (int i = 0; i < list.size(); i++) {
						tel = (DianXinDetail) list.get(i);
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
						tel.setPhone(this.loginName);
						dianXinDetailService.saveDianXinDetail(tel);
					}
				} else {

				}
			}
		} catch (Exception e) {
			writeLogByZhangdan(e);
		}
		return list;
	}

	public boolean saveDianxinMessage(List<TelcomMessage> messageList) {
		boolean goNextPage = true;
		List list1 = new ArrayList();
		if (messageList != null) {
			TelcomMessage maxTimeMessage = telcomMessageService
					.getMaxSentTime(this.loginName);
			for (int i = 0; i < messageList.size(); i++) {
				TelcomMessage currentMessage = messageList.get(i);
				if (maxTimeMessage != null
						&& currentMessage.getSentTime().getTime() <= maxTimeMessage
								.getSentTime().getTime()) {
					goNextPage = false;
					continue;
				}
				// telcomMessageService.save(currentMessage);
				list1.add(currentMessage);
			}
		}
		telcomMessageService.insertbatch(list1);
		return goNextPage;
	}

	public boolean saveMobileMessage(List<MobileMessage> messageList) {
		boolean goNextPage = true;
		List list1 = new ArrayList();
		if (messageList != null) {
			MobileMessage maxTimeMessage = mobileMessageService
					.getMaxSentTime(this.loginName);
			for (int i = 0; i < messageList.size(); i++) {
				MobileMessage currentMessage = messageList.get(i);
				if (maxTimeMessage != null
						&& currentMessage.getSentTime().getTime() <= maxTimeMessage
								.getSentTime().getTime()) {
					goNextPage = false;
					continue;
				}
				// mobileMessageService.save(currentMessage);
				UUID uuid = UUID.randomUUID();
				currentMessage.setId(uuid.toString());
				currentMessage.setCreateTs(new Date());
				list1.add(currentMessage);
			}
		}
		mobileMessageService.insertbatch(list1);
		return goNextPage;
	}

	public void saveMobileDetail(List<MobileDetail> detailList) {
		List <MobileDetail>list1 = new ArrayList<MobileDetail>();
		if (detailList != null) {
			MobileDetail k=new MobileDetail(this.loginName);
			MobileDetail maxTimeDetail = mobileDetailService
					.getMaxTime(k);
			for (int i = 0; i < detailList.size(); i++) {
				MobileDetail currentDetail = detailList.get(i);
				try{
					if (maxTimeDetail != null
							&& currentDetail.getcTime().before(maxTimeDetail.getcTime())) {
						continue;
					}
					// mobileMessageService.save(currentMessage);
					UUID uuid = UUID.randomUUID();
					currentDetail.setId(uuid.toString());
					//currentDetail.setCreateTs(new Date());
					list1.add(currentDetail);
				}
				catch(Exception e){
					log.error("mobileDetail"+currentDetail.toString(), e);
				}
			}
		}
		mobileDetailService.insertbatch(list1);
		return;
	}
	
	/**
	 * <p>存储流量月账单，只需要把所有月账单放入List，调用此函数即可
	 * <p>函数中已对UUID进行初始化，只需要保证月账单的monthly参数正确即可
	 * @author Pat.Liu
	 * @param List<MobileOnlineBill>
	 * 
	 * */
		public void saveMobileOnlineBill(List<MobileOnlineBill> mobileOnlineBills) {
			if (mobileOnlineBills == null || mobileOnlineBills.size() == 0)
				return;
			MobileOnlineBill maxTime = mobileOnlineBillService
					.getMaxTime(mobileOnlineBills.get(0).getPhone());

			for (int i = 0; i < mobileOnlineBills.size(); i++) {
				//是否当月的判断
				if(mobileOnlineBills.get(i).getMonthly().getMonth()==(new Date()).getMonth())
					mobileOnlineBills.get(i).setIscm(1);
				else 
					mobileOnlineBills.get(i).setIscm(0);
				if(maxTime==null){
					mobileOnlineBills.get(i).setId(UUID.randomUUID().toString());
					mobileOnlineBillService.save(mobileOnlineBills.get(i));	
				}
				else if (maxTime.getMonthly().equals(
						mobileOnlineBills.get(i).getMonthly())) {
					mobileOnlineBills.get(i).setId(maxTime.getId());
					mobileOnlineBillService.update(mobileOnlineBills.get(i));
				} else if (maxTime.getMonthly().before(
						mobileOnlineBills.get(i).getMonthly())) {
					if (mobileOnlineBills.get(i).getId() == null
							|| mobileOnlineBills.get(i).getId() == "")
						mobileOnlineBills.get(i)
								.setId(UUID.randomUUID().toString());
					mobileOnlineBillService.save(mobileOnlineBills.get(i));
				}
			}
		}
		
	/**
	 * <p>存储流量详单
	 * <p>已做增量式判断，对UUID进行了初始化
	 * <p>批量插入
	 * @author Pat.Liu
	 * @param List<MobileOnlineList>
	 * */
	public void saveMobileOnlineList(List<MobileOnlineList> mobileOnlineList){
		MobileOnlineList maxTime=mobileOnlineListService.getMaxTime(login.getLoginName());
		List<MobileOnlineList> newList=new ArrayList<MobileOnlineList>();
		Iterator<MobileOnlineList> i=mobileOnlineList.iterator();
		MobileOnlineList temp;
		while(i.hasNext()){
			try{
				temp=i.next();
				//ArrayList可以null！
				if(temp!=null && (maxTime==null || temp.getcTime().after(maxTime.getcTime()))){
					temp.setId(UUID.randomUUID().toString());
					newList.add(temp);
				}	
			}catch(Exception e){
				log.error("mobileOnlineList:"+i.toString(), e);
			}
		}
		mobileOnlineListService.insertbatch(newList);
	}
		
	
	public void saveUnicomMessage() {
		
	}
	public void saveDianXinFlowBill(List<DianXinFlow> dianXinFlows){
		boolean goNextPage = true;
		List list1 = new ArrayList();
		if (dianXinFlows != null) {
			DianXinFlow dianXinFlowBase = dianXinFlows.get(0);
			DianXinFlow maxDianXinFlow= dianXinFlowService.getMaxFlowTime(dianXinFlowBase.getPhone());
			for (int i = 0; i < dianXinFlows.size(); i++) {
				DianXinFlow currentFlow = dianXinFlows.get(i);
				if (maxDianXinFlow != null
						&& currentFlow.getQueryMonth().getTime() <= maxDianXinFlow.getQueryMonth().getTime()) {
					goNextPage = false;
					continue;
				}
				UUID uuid = UUID.randomUUID();
				currentFlow.setId(uuid.toString());
				list1.add(currentFlow);
			}
		}
		dianXinFlowService.insertbatch(list1);
	}
	public void saveDianXinFlowDetail(List<DianXinFlowDetail> dianXinFlowDetails){
		boolean goNextPage = true;
		List<DianXinFlowDetail> list1 = new ArrayList<DianXinFlowDetail>();
		if (dianXinFlowDetails != null) {
			DianXinFlowDetail dianXinFlowDetailBase = dianXinFlowDetails.get(0);
			DianXinFlowDetail maxDianXinFlowDetail= dianXinFlowDetailService.getMaxTime(dianXinFlowDetailBase);
			for (int i = 0; i < dianXinFlowDetails.size(); i++) {
				DianXinFlowDetail currentFlow = dianXinFlowDetails.get(i);
				if (maxDianXinFlowDetail != null
						&& currentFlow.getBeginTime().getTime() <= maxDianXinFlowDetail.getBeginTime().getTime()) {
					goNextPage = false;
					continue;
				}
				UUID uuid = UUID.randomUUID();
				currentFlow.setId(uuid.toString());
				list1.add(currentFlow);
			}
		}
		dianXinFlowDetailService.insertbatch(list1);
	}
	/**
	 * 将二位数组转为一个dwr框架post提交的参数格式
	 * @author JerrySun
	 * @param pairs
	 * @return key1=value1\r\nkey2=value2\r\n...
	 */
	public String joinPairsToPostBodyForDWR(String[][] pairs){
		StringBuffer sb = new StringBuffer(); 
		for(int i=0;i<pairs.length;i++){
			String key = "";
			String value = "";
			for(int j=0;j<2;j++){
				if(j==0)
					key = pairs[i][j];
				else if (j==1)
					value = pairs[i][j];
			}
			sb.append(key).append("=").append(value).append("\r\n");
		}
		return sb.toString();
	}
	/**
	 * 获取通话记录的最大时间
	 * @return
	 */
	public Date getMaxFlowTime(){
		Args.notNull(this.userSource, "userSource");
		if(!isTest()){
			try{
				if(this.userSource.equals(Constant.YIDONG)){
					MobileOnlineList tel =  mobileOnlineListService.getMaxTime(this.loginName);
					if(tel!=null){
						return tel.getcTime();
					}
				}else if(this.userSource.equals(Constant.DIANXIN)){
					DianXinFlowDetail dx =  dianXinFlowDetailService.getMaxTime(new DianXinFlowDetail(this.loginName));
					if(dx!=null){
						return dx.getBeginTime();
					}
				}else{
					//联通
					UnicomFlow uni= unicomFlowService.getMaxStartTime(this.loginName);
					if(uni!=null)
						return uni.getStartTime();
				}
			}catch(Exception e){
				writeLogByHistory(e);
			}
		}
		return null;
	}
	/**@fastw
	 * @param type
	 * @return
	 */
	public LinkedList<MonthlyBillMark>  getSpiderMonthsMark(int type){
		return  getSpiderMonthsMark(true,null,0,type);
	}
	/**
	 * @param isSpiderCurrent 是否采集当月信息 默认true
	 * @param formatStr  默认yyyyMM
	 * @param gatherMonth 默认采集6个月
	 * @param type 默认语音账单 1流量账单
	 * @return
	 */
	public LinkedList<MonthlyBillMark>  getSpiderMonthsMark(boolean isSpiderCurrent,String formatStr,int gatherMonth,int type){
		LinkedList<MonthlyBillMark> list = new LinkedList<MonthlyBillMark>();
		if(formatStr==null){
			formatStr = "yyyyMM";
		}
		if(gatherMonth==0){
			gatherMonth = 6;
		}
		List<String> ms = DateUtils.getMonths(gatherMonth,formatStr);
		//默认查到12个月
		Set<Date> set = getMonthlyBillMaxNumTel(type);
		//取出12个月的最大时间 比如: 201407
		if(CommonUtils.isNotEmpty(set)){
			Date dtime = set.iterator().next();
			Date d = null;
			//遍历一遍 ,,ms是当前要保存到数据库的6个月份
			for (String startDate : ms) {
				d = DateUtils.StringToDate(startDate, formatStr);
				//检测是否数据库有相等月份,如果包含,是否数据库最大月份在里边,主要是更新数据库中最大月份的话费清单,其他跳过
				if(set.contains(d)){
					if(d.getTime()==dtime.getTime()){
						MonthlyBillMark mbp = new MonthlyBillMark(startDate,formatStr);
						mbp.setSpiderCurrentMonth(true);
						list.add(mbp);//ssa.setFormat(formatStr);
					}
				}else{
					//formatStr 主要是作判断更新的时候使用
					list.add(new MonthlyBillMark(startDate,formatStr));
				}
			}
		}else{
			//判断是否采集当月
			if(!isSpiderCurrent){
				ms.remove(0); 
			}
			//采集月份
			for (String date : ms) {
				list.add(new MonthlyBillMark(date,formatStr));
			}
		}
		return list;
	}
	/**
	 * 默认查到12个月
	 * 获取几个月的最大时间
	 * @param type 0电话月份账单,1流量详单
	 * @return
	 */
	public Set<Date> getMonthlyBillMaxNumTel(int type){
		Args.notNull(this.userSource, "userSource");
		if(!isTest()){
			try{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("teleno", this.loginName);
				List<Date> list = null;
					if(this.userSource.equals(Constant.YIDONG)){
						if(type==0){
							list =  mobileTelService.getMaxNumTel(map);	
						}else{
							map.put("phone", this.loginName);
							list =  mobileOnlineBillService.getMaxNumTel(map);	
						}
					}else if(this.userSource.equals(Constant.DIANXIN)){
						if(type==0){
							list =  dianXinTelService .getMaxNumTel(map);
						}else{
							map.put("phone", this.loginName);
							list =  dianXinFlowService.getMaxNumTel(map);
						}
					}else{
						
					}
					Set<Date> set =  null;
					if(CommonUtils.isNotEmpty(list)){
						set = new LinkedHashSet<Date>();
						set.addAll(list);
					}
					return set;
			}catch(Exception e){
				writeLogByZhangdan(e);
			}
		}
		return null;
	}
	/**
	 * 采集流量详单@fastw
	 * @return
	 * @throws Exception
	 */
	public  LinkedList<? extends Object> gatherFlowList() throws Exception{
		return null;
	}
	
	/**
	 *数据库默认保存从小到大,防止保存入库 ,所以入库原则保证插入队列的顺序@fastw
	 */
	public void saveFlowList(){
		if(!isTest()){
			try{
				LinkedList simpleList = this.gatherFlowList();
//				System.out.println(simpleList.size());
				if(CommonUtils.isNotEmpty(simpleList)){
					if(this.userSource.equals(Constant.YIDONG)){
						mobileOnlineListService.insertbatch(simpleList);
					}else if(this.userSource.equals(Constant.DIANXIN)){
						dianXinFlowDetailService.insertbatch(simpleList);
					}else{
						unicomFlowService.insertbatch(simpleList);
					}
				}
			}catch(Exception e){
				writeLogByFlowList(e);
			}
		}
		
	}
	
	/**
	 * 流量账单
	 * @return
	 */
	public LinkedList<MonthlyBillMark> gatherFlowBill(){
		return null;
	}
	/**
	 * 保存流量账单@fastw
	 */
	public void saveFlowBill(){
		try{
			LinkedList<MonthlyBillMark> simpleList = this.gatherFlowBill();
			if(this.userSource.equals(Constant.YIDONG)){
				MobileOnlineBill tel = null;
				for (MonthlyBillMark bill : simpleList) {
					tel = bill.getMobileOnlineBill();
					if(tel!=null){
						tel.setPhone(this.loginName);
						if(tel.getMonthly()==null){
							tel.setMonthly(bill.getFormatDate());
						}
						if(bill.isSpiderCurrentMonth()){
							MonthlyBillMark billPojo = findOnlineBillData(tel.getMonthly());
							tel.setId(billPojo.getMobileOnlineBill().getId());
							 mobileOnlineBillService.update(tel);
						}else{
							UUID uuid = UUID.randomUUID();
							tel.setId(uuid.toString());
							 mobileOnlineBillService.save(tel);
						}
					}
				}
			}else if(this.userSource.equals(Constant.DIANXIN)){
				DianXinFlow tel = null;
				for (MonthlyBillMark bill : simpleList) {
					tel = bill.getDianXinFlow();
					if(tel!=null){
						tel.setPhone(this.loginName);
						if(tel.getQueryMonth()==null){
							tel.setQueryMonth(bill.getFormatDate());
						}
						if(bill.isSpiderCurrentMonth()){
							MonthlyBillMark billPojo = findOnlineBillData(tel.getQueryMonth());
							tel.setId(billPojo.getDianXinFlow().getId());
							dianXinFlowService.update(tel);
						}else{
							UUID uuid = UUID.randomUUID();
							tel.setId(uuid.toString());
							dianXinFlowService.saveDianXinFlow(tel);
						}
					}
				}
			}else{
				System.out.println("没有做实现--------------------------------------报错了");
				throw new Exception("我没做实现");
			}
		}catch(Exception e){
			writeLogByFlowBill(e);
		}
	}
	/**
	 * 针对saveFlowBill方法@fastw
	 * @param ctime
	 * @throws Exception 
	 */
	private MonthlyBillMark findOnlineBillData(Date ctime) throws Exception{
		MonthlyBillMark pojo = new MonthlyBillMark();
		 Map<String,Object> map2 = new HashMap<String,Object>();
		List list2 = null;
		if(this.userSource.equals(Constant.YIDONG)){
			map2.put("phone", this.loginName);
			map2.put("monthly", ctime);
			list2 = mobileOnlineBillService.getMobileOnlineBill(map2);
		}else if(this.userSource.equals(Constant.DIANXIN)){
			map2.put("queryMonth", ctime);
			map2.put("phone", this.loginName);
			list2 = dianXinFlowService.getDianXinFlowBybc(map2);
		}else{
			map2.put("cTime", ctime);
			map2.put("teleno", this.loginName);
			list2 = unicomFlowBillService.getUnicomFlowBillBybc(map2);
		}
		pojo.setObj(list2.get(0));
		return pojo;
	}
}
