package com.lkb.controller.telcom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.lkb.bean.DianXinDetail;
import com.lkb.bean.DianXinFlow;
import com.lkb.bean.DianXinFlowDetail;
import com.lkb.bean.DianXinTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.TelcomMessage;
import com.lkb.bean.User;
import com.lkb.robot.AbstractSpiderListener;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderListener;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinFlowDetailService;
import com.lkb.service.IDianXinFlowService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.ITelcomMessageService;
import com.lkb.thirdUtil.dx.AbstractDianXinCrawler;
import com.lkb.util.DateUtils;


public abstract class AbstractDianXinController extends AbstractTelcomController {
	@Resource
	protected IDianXinTelService dianXinTelService;
	@Resource
	protected IDianXinDetailService dianXinDetailService;
	@Resource
	protected ITelcomMessageService messageService;
	@Resource
	protected IDianXinFlowService dianXinFlowService;
	@Resource
	protected IDianXinFlowDetailService dianXinFlowDetailService;
	
	protected void queryMaxTs(String phoneNo, AbstractDianXinCrawler dx) {
		TelcomMessage telcomMessage =messageService.getMaxSentTime(phoneNo);
		if (telcomMessage!=null) {
			dx.setMaxMessageTs(telcomMessage.getSentTime());
		}else {
			dx.setMaxMessageTs(null);
		}
		DianXinTel dianXinTel = new DianXinTel();
		dianXinTel.setTeleno(phoneNo);
		DianXinTel maxDianXinTel =dianXinTelService.getMaxTime(dianXinTel);
		if (maxDianXinTel!=null) {
			dx.setMaxTelTs(maxDianXinTel.getcTime());
		}else {
			dx.setMaxTelTs(null);
		}
		DianXinDetail dianXinDetail = new DianXinDetail();
		dianXinDetail.setPhone(phoneNo);
		DianXinDetail maxDianXinDetail= dianXinDetailService.getMaxTime(dianXinDetail);
		if (maxDianXinDetail!=null) {
			dx.setMaxDetailTs(maxDianXinDetail.getcTime());
		}else {
			dx.setMaxDetailTs(null);
		}
		DianXinFlow maxDianXinFlow= dianXinFlowService.getMaxFlowTime(phoneNo);
		if (maxDianXinFlow!=null) {
			dx.setMaxFlowTs(maxDianXinFlow.getQueryMonth());
		}else {
			dx.setMaxFlowTs(null);
		}
		DianXinFlowDetail dianXinFlowDetail = new DianXinFlowDetail();
		dianXinFlowDetail.setPhone(phoneNo);
		DianXinFlowDetail maxDianXinFlowDetail = dianXinFlowDetailService.getMaxTime(dianXinFlowDetail);
		if (maxDianXinFlowDetail!=null) {
			dx.setMaxFlowDetailTs(maxDianXinFlowDetail.getBeginTime());
		}else {
			dx.setMaxFlowDetailTs(null);
		}
	}
	protected void saveMonthBill(String currentUser, AbstractDianXinCrawler dx) {
		try {
			Collection<DianXinTel> list = dx.getTelList();
			if (list == null) {
				return;
			}
			logger.info("saveData month bill.... size:" + list.size());
			for(DianXinTel tel : list) {
				 if (dx.getMaxTelTs()==null||tel.getcTime().getTime()>dx.getMaxTelTs().getTime()) {
						UUID uuid = UUID.randomUUID();
						tel.setId(uuid.toString());
//						tel.setBaseUserId(currentUser);	
						dianXinTelService.saveDianXinTel(tel);
				}else if (tel.getcTime().getTime()<dx.getMaxTelTs().getTime()) {
					continue;
				}else {
					dianXinTelService.update(tel);
				}
			}
		} catch (Exception e) {
			logger.error("saveMonthBill Error", e);
		}
		
	}


	protected void saveCallLogDetail( AbstractDianXinCrawler dx) {
		try {
			Collection<DianXinDetail> list = dx.getDetailList();
			if (list == null) {
				return;
			}
			logger.info("saveData detail.... size:" + list.size());
			for(DianXinDetail detail : list) {
				if(dx.getMaxDetailTs()!=null&&detail.getcTime().getTime()<=dx.getMaxDetailTs().getTime()){
					continue;
				}
				UUID uuid = UUID.randomUUID();
				detail.setId(uuid.toString());
				dianXinDetailService.saveDianXinDetail(detail);	
			}
		} catch (Exception e) {
			logger.error("saveCallLogDetail", e);
		}
		
	}
	protected void saveMessage( AbstractDianXinCrawler dx) {
		try {
			Collection<TelcomMessage> list= dx.getMessageList();
			if (list == null) {
				return;
			}
			logger.info("saveData message.... size:" + list.size());
			List list1 = new ArrayList();
			for(TelcomMessage obj : list) {
				if (dx.getMaxMessageTs()!=null&&obj.getSentTime().getTime()<=dx.getMaxMessageTs().getTime()) {
					continue ;
				}else {
					UUID uuid = UUID.randomUUID();
					obj.setId(uuid.toString());
					obj.setCreateTs(new Date());
					if(obj.getBusinessType() == null || "".equals(obj.getBusinessType()) || obj.getBusinessType().indexOf("发送")!=-1)
						obj.setBusinessType("发送");
					else
						obj.setBusinessType("接收");
					
					//messageService.save(obj);	
					list1.add(obj);
					
				}
			}	
			messageService.insertbatch(list1);
		} catch (Exception e) {
			logger.error("saveMessage", e);
		}
		
	}
	
	protected void saveFlowBill(String currentUser, AbstractDianXinCrawler dx) {
		try {
			Collection<DianXinFlow> list = dx.getFlowList();
			if (list == null) {
				return;
			}
			logger.info("saveData flow bill.... size:" + list.size());
			for(DianXinFlow flow : list) {
				 if (dx.getMaxFlowTs()==null||flow.getQueryMonth().getTime()>dx.getMaxFlowTs().getTime()) {
						UUID uuid = UUID.randomUUID();
						flow.setId(uuid.toString());
						dianXinFlowService.saveDianXinFlow(flow);
				}else if (flow.getQueryMonth().getTime()<dx.getMaxFlowTs().getTime()) {
					continue;
				}else {
					dianXinFlowService.update(flow);
				}
			}
		} catch (Exception e) {
			logger.error("saveFlowBill",e);
		}
		
	}
	
	protected void saveFlowDetail( AbstractDianXinCrawler dx) {
		try {
			Collection<DianXinFlowDetail> list= dx.getFlowDetailList();
			if (list == null) {
				return;
			}
			logger.info("saveData flow detail.... size:" + list.size());
			List list1 = new ArrayList();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
			String today = DateUtils.getToday("yyyy-MM");
			for(DianXinFlowDetail obj : list) {
				String beginTime = sdf.format(obj.getBeginTime());
				if(DateUtils.isSameMonth(today, beginTime))
					obj.setIscm(1);

				if (dx.getMaxFlowDetailTs()!=null&&obj.getBeginTime().getTime()<=dx.getMaxFlowDetailTs().getTime()) {
					continue ;
				}else {
					UUID uuid = UUID.randomUUID();
					obj.setId(uuid.toString());
					list1.add(obj);
				}
			}	
			dianXinFlowDetailService.insertbatch(list1);
		} catch (Exception e) {
			logger.error("saveFlowDetail", e);
		}
		
	}
	
	protected SpiderListener saveSpiderListener(final AbstractDianXinCrawler dx, final User user, final String currentUser) {
		queryMaxTs(user.getPhone(), dx);
		return new AbstractSpiderListener(Spider.buildListenerContext()) {
			public void onStartup(SimpleObject context, Object obj) {
				saveOrUpdateParse(user, currentUser, 1);
			}
			
			public void onComplete(SimpleObject contenxt, Object obj) {
				try {
					if (isSuccess(dx)) {
						saveUser(user, dx.getUser(), currentUser);
						saveMonthBill(currentUser, dx);
						saveCallLogDetail(dx);
						saveMessage(dx);
						saveFlowBill(currentUser, dx);
						saveFlowDetail(dx);
					}
				} catch (Exception e) {
					logger.error("error", e);
				}finally{
					saveOrUpdateParse(user, currentUser, 0);
				}
			}
		};
	}
	
	
	
	/**
	* <p>Title: saveSpiderListener</p>
	* <p>Description: 2014.10.27 更新爬虫监听的保存方式模型</p>
	* @author Jerry Sun
	* @param dx
	* @param user
	* @param currentUser
	* @param saveMode	1：仅保存用户信息
	* 					2：保存账单信息和详单信息（包括通话、短信和流量）
	* 					3：保存所有信息
	* @return
	*/
	protected SpiderListener saveSpiderListener(final AbstractDianXinCrawler dx, final User user, final String currentUser, final int saveMode) {
		return new AbstractSpiderListener(Spider.buildListenerContext()) {
			public void onStartup(SimpleObject context, Object obj) {
				saveOrUpdateParse(user, currentUser, 1);
			}
			
			public void onComplete(SimpleObject contenxt, Object obj) {
				try {
					if (isSuccess(dx)) {
						switch (saveMode) {
						case 1:	//保存用户信息
							saveUser(user, dx.getUser(), currentUser);	//保存user
							break;
						case 2:	//保存账单和详单信息
							queryMaxTs(user.getPhone(), dx);
							saveMonthBill(currentUser, dx);	//保存帐单
							saveCallLogDetail(dx);	//保存通话记录
							saveMessage(dx);	//保存短信记录
							saveFlowBill(currentUser, dx);	//保存流量账单
							saveFlowDetail(dx);	//保存流量详单
							break;
						case 3: //保存所有信息
							queryMaxTs(user.getPhone(), dx);
							saveUser(user, dx.getUser(), currentUser);	//保存user
							saveMonthBill(currentUser, dx);	//保存帐单
							saveCallLogDetail(dx);	//保存通话记录
							saveMessage(dx);	//保存短信记录
							saveFlowBill(currentUser, dx);	//保存流量账单
							saveFlowDetail(dx);	//保存流量详单
							break;
						}
					}
				} catch (Exception e) {
					logger.error("error", e);
				}finally{
					saveOrUpdateParse(user, currentUser, 0);
				}
			}
		};
	}
}
