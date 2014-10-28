package com.lkb.thirdUtil.base;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Args;

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
import com.lkb.bean.UnicomMessage;
import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.constant.Constant;
import com.lkb.thirdUtil.base.pojo.MonthlyBillMark;
import com.lkb.util.DateUtils;
import com.lkb.util.httpclient.util.CommonUtils;


public abstract class BasicCommonMobile extends BasicCommonMobileControl<ResOutput>{
	public static final String yue_key = "BasicCommonMobile.yue.key";
	public static final String name_key = "BasicCommonMobile.name.key";
	
	 /**
     * 采集个人信息和账单信息
     * @param base
     */
    public void addTask_1(){
    	addTask(1);
	}
	
	public BasicCommonMobile(Login login, String currentUser) {
		super(login, currentUser);
	}
	public BasicCommonMobile(Login login) {
		super(login);
	}
	/**
	 * 保存个人信息,以后不再执行getUserInfo方法
	 * 子类方法必须重新getInfo方法
	 * @return
	 */
	public abstract User gatherUser() throws Exception;
	/**
	 * 保存用户
	 */
	public User saveUser(){
		User user = null;
		try{
			user = this.gatherUser();
			context.update();
		}catch(Exception e){
			log4j.writeLogByMyInfo(e);
		}
		if(user!=null){
			User user1 = null;
			boolean b = false;
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("parentId", this.currentUser);
			map1.put("usersource", userSource);
			map1.put("loginName", this.loginName);
			List<User> list = null;
			try{
			 list = service.getUserService().getUserByParentIdSource(map1);
			}catch(Exception e){
			} 
			if (list != null && list.size() > 0) {
				user1 = list.get(0);
				b = true;
			}else{
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
			if(StringUtils.isEmpty(user1.getEmail())){
				user1.setEmail("");	
			}else{
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
	private boolean saveUser(User user,boolean b){
		if(!isTest()){
			try{
				if(b){
					 service.getUserService().update(user);
				}else{
					 service.getUserService().saveUser(user);
				}
			}catch(Exception e){
				log4j.writeLogByMyInfo(e);
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 默认查到12个月
	 * 获取几个月的最大时间
	 * @param type 0电话月份账单,1流量账单
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
							list =  service.getMobileTelService().getMaxNumTel(map);	
						}else{
							map.put("phone", this.loginName);
							list =  service.getMobileOnlineBillService().getMaxNumTel(map);	
						}
					}else if(this.userSource.equals(Constant.DIANXIN)){
						if(type==0){
							list =  service.getDianXinTelService().getMaxNumTel(map);
						}else{
							map.put("phone", this.loginName);
							list =  service.getDianXinFlowService().getMaxNumTel(map);
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
				log4j.writeLogByZhangdan(e);
			}
		}
		return null;
	}
	
	/**
	 * @param type  默认语音账单 1流量账单
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
	 * 话费账单
	 * @return
	 */
	public abstract LinkedList<MonthlyBillMark> gatherMonthlyBill();
	/**
	 *保存手机账单信息 
	 */
	public void saveTel(){
		Args.notNull(this.userSource, "userSource");
		try{
			LinkedList<MonthlyBillMark> simpleList = this.gatherMonthlyBill();
			if(this.userSource.equals(Constant.YIDONG)){
				MobileTel tel = null;
				for (MonthlyBillMark bill : simpleList) {
					tel = bill.getMobileTel();
					if(tel!=null){
						tel.setTeleno(this.loginName);
						if(tel.getcTime()==null){
							tel.setcTime(bill.getFormatDate());
						}
						if(bill.isSpiderCurrentMonth()){
							MonthlyBillMark billPojo = findTelData(tel.getcTime());
							tel.setId(billPojo.getMobileTel().getId());
							 service.getMobileTelService().update(tel);
						}else{
							UUID uuid = UUID.randomUUID();
							tel.setId(uuid.toString());
							service.getMobileTelService().saveMobileTel(tel);
						}
					}
				}
			}else if(this.userSource.equals(Constant.DIANXIN)){
				DianXinTel tel = null;
				for (MonthlyBillMark bill : simpleList) {
					tel = bill.getDianxinTel();
					if(tel!=null){
						tel.setTeleno(this.loginName);
						if(tel.getcTime()==null){
							tel.setcTime(bill.getFormatDate());
						}
						if(bill.isSpiderCurrentMonth()){
							MonthlyBillMark billPojo = findTelData(tel.getcTime());
							tel.setId(billPojo.getDianxinTel().getId());
							service.getDianXinTelService().update(tel);
						}else{
							UUID uuid = UUID.randomUUID();
							tel.setId(uuid.toString());
							service.getDianXinTelService().saveDianXinTel(tel);
						}
					}
				}
			}else{
				
			}
		}catch(Exception e){
			log4j.error("账单记录", "ZZJL", e);
			log4j.writeLogByZhangdan(e);
		}
		
	}
	/**
	 * 针对saveTel方法
	 * @param ctime
	 * @throws Exception 
	 */
	private MonthlyBillMark findTelData(Date ctime) throws Exception{
		MonthlyBillMark pojo = new MonthlyBillMark();
		 Map<String,Object> map2 = new HashMap<String,Object>();
		map2.put("cTime", ctime);	
		List<Map> list2 = null;
		if(this.userSource.equals(Constant.YIDONG)){
			map2.put("phone", this.loginName);
			list2 = service.getMobileTelService().getMobileTelBybc(map2);
		}else if(this.userSource.equals(Constant.DIANXIN)){
			map2.put("teleno", this.loginName);
			list2 = service.getDianXinTelService().getDianXinTelBybc(map2);
		}else{
			
		}
		pojo.setObj(list2.get(0));
		return pojo;
	}

	
	public void saveMessage(){
		try{
			LinkedList simpleList = this.gatherMessage();
			if(CommonUtils.isNotEmpty(simpleList)){
				if(this.userSource.equals(Constant.YIDONG)){
					service.getMobileMessageService().insertbatch(simpleList);
				}else if(this.userSource.equals(Constant.DIANXIN)){
					service.getTelcomMessageService().insertbatch(simpleList);
				}else{
					
				}
			}
		}catch(Exception e){
			log4j.writeLogByMessage(e);
		}
	}
	/**
	 *数据库默认保存从小到大,防止保存入库 ,所以入库原则保证插入队列的顺序
	 */
	public abstract LinkedList<? extends Object> gatherCallHistory() throws Exception;
	public abstract LinkedList<? extends Object> gatherMessage() throws Exception;
	/**
	 *数据库默认保存从小到大,防止保存入库 ,所以入库原则保证插入队列的顺序
	 */
	public void saveCallHistory(){
		if(!isTest()){
			try{
				LinkedList simpleList = this.gatherCallHistory();
//				System.out.println(simpleList.size());
				if(CommonUtils.isNotEmpty(simpleList)){
					if(this.userSource.equals(Constant.YIDONG)){
						service.getMobileDetailService().insertbatch(simpleList);
					}else if(this.userSource.equals(Constant.DIANXIN)){
						service.getDianXinDetailService().insertbatch(simpleList);
					}else{
						
					}
				}
			}catch(Exception e){
				log4j.writeLogByHistory(e);
			}
		}
		
	}

	private MobileDetail getMaxTimeMobileDetail(){
		MobileDetail tel = new MobileDetail();
		tel.setPhone(this.loginName);
		return service.getMobileDetailService().getMaxTime(tel);
	}
	private DianXinDetail getMaxTimeDianxinDetail(){
		return service.getDianXinDetailService().getMaxTime(new DianXinDetail(this.loginName));
	}
	private UnicomDetail getMaxTimeUnicomDetail(){
		return service.getUnicomDetailService().getMaxTime(new UnicomDetail(this.loginName));
	}
	private MobileMessage getMaxTimeMobileMessage(){
		return service.getMobileMessageService().getMaxSentTime(this.loginName);
	}
	private TelcomMessage getMaxTimeTelcomMessage(){
		return service.getTelcomMessageService().getMaxSentTime(this.loginName);
	}
	private UnicomMessage getMaxTimeUnicomMessage(){
		return service.getUnicomMessageService().getMaxSentTime(this.loginName);
	}
	private MobileOnlineList getMaxTimeMobileOnlineList(){
		return service.getMobileOnlineListService().getMaxTime(this.loginName);
	}
	private DianXinFlowDetail getMaxTimeDianXinFlowDetail(){
		return service.getDianXinFlowDetailService().getMaxTime(new DianXinFlowDetail(this.loginName));
	}
	private UnicomFlow getMaxTimeUnicomFlow(){
		return  service.getUnicomFlowService().getMaxStartTime(this.loginName);
	}

	public boolean isContinue(Date bigTime,Date time){
		boolean b = false;
		if(bigTime!=null&&bigTime.getTime()>=time.getTime()){
			b = true;
		}
		return b;
	}
	
	
	
	/**
	 * 获取通话记录的最大时间
	 * @return
	 */
	public Date getMaxCallTime(){
		Args.notNull(this.userSource, "userSource");
		if(!isTest()){
			try{
				if(this.userSource.equals(Constant.YIDONG)){
					if( getMaxTimeMobileDetail()!=null){
						return  getMaxTimeMobileDetail().getcTime();
					}
				}else if(this.userSource.equals(Constant.DIANXIN)){
					if(getMaxTimeDianxinDetail()!=null){
						return getMaxTimeDianxinDetail().getcTime();
					}
				}else{
					if(getMaxTimeUnicomDetail()!=null)
						return getMaxTimeUnicomDetail().getcTime();
				}
			}catch(Exception e){
				log4j.writeLogByHistory(e);
			}
		}
		return null;
	}
	/**
	 * 短信的最大时间
	 * @return
	 */
	public Date getMaxMessageTime(){
		Args.notNull(this.userSource, "userSource");
		if(!isTest()){
			try{
				if(this.userSource.equals(Constant.YIDONG)){
					if(getMaxTimeMobileMessage()!=null)
						return getMaxTimeMobileMessage().getSentTime();
				}else if(this.userSource.equals(Constant.DIANXIN)){
					if(getMaxTimeTelcomMessage()!=null)
						return getMaxTimeTelcomMessage().getSentTime();
				}else{
					if(getMaxTimeUnicomMessage()!=null)
						return getMaxTimeUnicomMessage().getSentTime();
				}
			}catch(Exception e){
				log4j.writeLogByMessage(e);
			}
		}
		return null;
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
					if( getMaxTimeMobileOnlineList()!=null)
						return  getMaxTimeMobileOnlineList().getcTime();
				}else if(this.userSource.equals(Constant.DIANXIN)){
					if( getMaxTimeDianXinFlowDetail()!=null){
						return getMaxTimeDianXinFlowDetail().getBeginTime();
					}
				}else{
					if(getMaxTimeUnicomFlow()!=null)
						return getMaxTimeUnicomFlow().getStartTime();
				}
			}catch(Exception e){
				log4j.writeLogByHistory(e);
			}
		}
		return null;
	}
	/**
	 * 采集流量详单
	 * @return
	 * @throws Exception
	 */
	public abstract LinkedList<? extends Object> gatherFlowList() throws Exception;
	
	/**
	 *数据库默认保存从小到大,防止保存入库 ,所以入库原则保证插入队列的顺序
	 */
	public void saveFlowList(){
		if(!isTest()){
			try{
				LinkedList simpleList = this.gatherFlowList();
//				System.out.println(simpleList.size());
				if(CommonUtils.isNotEmpty(simpleList)){
					if(this.userSource.equals(Constant.YIDONG)){
						service.getMobileOnlineListService().insertbatch(simpleList);
					}else if(this.userSource.equals(Constant.DIANXIN)){
						service.getDianXinFlowDetailService().insertbatch(simpleList);
					}else{
						service.getUnicomFlowService().insertbatch(simpleList);
					}
				}
			}catch(Exception e){
				log4j.writeLogByFlowList(e);
			}
		}
		
	}
	/**
	 * 流量账单
	 * @return
	 */
	public abstract LinkedList<MonthlyBillMark> gatherFlowBill();
	/**
	 * 保存流量账单
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
							 service.getMobileOnlineBillService().update(tel);
						}else{
							UUID uuid = UUID.randomUUID();
							tel.setId(uuid.toString());
							 service.getMobileOnlineBillService().save(tel);
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
							service.getDianXinFlowService().update(tel);
						}else{
							UUID uuid = UUID.randomUUID();
							tel.setId(uuid.toString());
							service.getDianXinFlowService().saveDianXinFlow(tel);
						}
					}
				}
			}else{
				System.out.println("没有做实现--------------------------------------报错了");
				throw new Exception("我没做实现");
			}
		}catch(Exception e){
			log4j.writeLogByFlowBill(e);
		}
	}
	/**
	 * 针对saveFlowBill方法
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
			list2 = service.getMobileOnlineBillService().getMobileOnlineBill(map2);
		}else if(this.userSource.equals(Constant.DIANXIN)){
			map2.put("queryMonth", ctime);
			map2.put("phone", this.loginName);
			list2 = service.getDianXinFlowService().getDianXinFlowBybc(map2);
		}else{
			map2.put("cTime", ctime);
			map2.put("teleno", this.loginName);
			list2 = service.getUnicomFlowBillService().getUnicomFlowBillBybc(map2);
		}
		pojo.setObj(list2.get(0));
		return pojo;
	}
	public String testUserToString(){
		try {
			User user = this.gatherUser();
			return "User [userName=" + this.loginName + ",  buyLevel=" + user.getBuyLevel() +", phone=" + this.loginName
					+ ", email=" + user.getEmail() + ", realName=" + user.getRealName() + ", idcard="
					+ user.getId() + ", addr=" + user.getAddr() + ", sex=" + user.getSex() + ", usersource="
					+ this.userSource +", loginName=" + loginName + ", modifyDate=" + new Date()  + ", packageName=" + user.getPackageName() + ", cardNo="
					+ user.getCardNo() +", yBalance=" + user.getyBalance()+ ", phoneRemain=" + user.getPhoneRemain() + "]";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
	}
}
