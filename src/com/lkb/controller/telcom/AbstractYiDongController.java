/**
 * 
 */
package com.lkb.controller.telcom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.bean.SimpleObject;
import com.lkb.bean.User;
import com.lkb.robot.AbstractSpiderListener;
import com.lkb.robot.Spider;
import com.lkb.robot.SpiderListener;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileMessageService;
import com.lkb.service.IMobileOnlineBillService;
import com.lkb.service.IMobileOnlineListService;
import com.lkb.service.IMobileTelService;
import com.lkb.thirdUtil.yd.AbstractYiDongCrawler;
import com.lkb.util.DateUtils;

/**
 * @author think
 * @date 2014-7-31
 */
public abstract class AbstractYiDongController extends AbstractTelcomController{
	@Resource
	protected IMobileTelService mobileTelService;
	@Resource
	protected IMobileDetailService mobileDetailService;
	@Resource
	protected IMobileMessageService mobileMessageService;
	@Resource
	protected IMobileOnlineListService mobileOnlineListService;
	@Resource
	protected IMobileOnlineBillService mobileOnlineBillService;
	protected void queryMaxTs(String phoneNo, AbstractYiDongCrawler yd){
		MobileMessage mobileMessage =mobileMessageService.getMaxSentTime(phoneNo);
		if (mobileMessage!=null) {
			yd.setMaxMessageTs(mobileMessage.getSentTime());
		}else {
			yd.setMaxMessageTs(null);
		}
		MobileTel mobileTel = new MobileTel();
		mobileTel.setTeleno(phoneNo);
		MobileTel maxMobileTel =mobileTelService.getMaxTime(mobileTel);
		if (maxMobileTel!=null) {
			yd.setMaxTelTs(maxMobileTel.getcTime());
		}else {
			yd.setMaxTelTs(null);
		}
		MobileDetail mobileDetail = new MobileDetail();
		mobileDetail.setPhone(phoneNo);
		MobileDetail maxMobileDetail= mobileDetailService.getMaxTime(mobileDetail);
		if (maxMobileDetail!=null) {
			yd.setMaxDetailTs(maxMobileDetail.getcTime());
		}else {
			yd.setMaxDetailTs(null);
		}
		MobileOnlineBill maxYdFlow= mobileOnlineBillService.getMaxTime(phoneNo);
		if (maxYdFlow!=null) {
			yd.setMaxFlowTs(maxYdFlow.getMonthly());
		}else {
			yd.setMaxFlowTs(null);
		}
		MobileOnlineList maxYdFlowDetail = mobileOnlineListService.getMaxTime(phoneNo);
		if (maxYdFlowDetail!=null) {
			yd.setMaxFlowDetailTs(maxYdFlowDetail.getcTime());
		}else {
			yd.setMaxFlowDetailTs(null);
		}
	}
	protected void saveMonthBill(String currentUser, AbstractYiDongCrawler yd) {
		Collection<MobileTel> list = yd.getTelList();
		if (list == null) {
			return;
		}
		logger.info("saveData month bill.... size:" + list.size());
		for(MobileTel tel : list) {
			if (yd.getMaxTelTs()==null||tel.getcTime().getTime()>yd.getMaxTelTs().getTime()) {
				UUID uuid = UUID.randomUUID();
				tel.setId(uuid.toString());
//				tel.setBaseUserId(currentUser);	
				mobileTelService.saveMobileTel(tel);
			}else if (tel.getcTime().getTime()<yd.getMaxTelTs().getTime()) {
				continue;
			}else {
				mobileTelService.update(tel);
			}	
		}
	}
	protected void saveCallLogDetail(AbstractYiDongCrawler yd) {
		Collection<MobileDetail> list = yd.getDetailList();
		if (list == null) {
			return;
		}
		logger.info("saveData detail.... size:" + list.size());
		for(MobileDetail detail : list) {
			if(yd.getMaxDetailTs()!=null&&detail.getcTime().getTime()<=yd.getMaxDetailTs().getTime()){
				continue;
			}
			UUID uuid = UUID.randomUUID();
			detail.setId(uuid.toString());
			mobileDetailService.saveMobileDetail(detail);	
		}
	}
	protected void saveMessage( AbstractYiDongCrawler yd) {
		Collection<MobileMessage> list= yd.getMessageList();
		if (list == null) {
			return;
		}
		logger.info("saveData message.... size:" + list.size());
		List list1 = new ArrayList();
		for(MobileMessage obj : list) {
			if (yd.getMaxMessageTs()!=null&&obj.getSentTime().getTime()<=yd.getMaxMessageTs().getTime()) {
				continue ;
			}else {
				UUID uuid = UUID.randomUUID();
				obj.setId(uuid.toString());
				obj.setCreateTs(new Date());
//				mobileMessageService.save(obj);	
				list1.add(obj);
			}
		}		
		mobileMessageService.insertbatch(list1);
	}
	
	protected void saveFlowBill(String currentUser, AbstractYiDongCrawler yd) {
		Collection<MobileOnlineBill> list = yd.getFlowList();
		if (list == null) {
			return;
		}
		logger.info("saveData flow bill.... size:" + list.size());
		for(MobileOnlineBill flow : list) {
			 if (yd.getMaxFlowTs()==null||flow.getMonthly().getTime()>yd.getMaxFlowTs().getTime()) {
					UUID uuid = UUID.randomUUID();
					flow.setId(uuid.toString());
					mobileOnlineBillService.save(flow);
			}else if (flow.getMonthly().getTime()<yd.getMaxFlowTs().getTime()) {
				continue;
			}else {
				mobileOnlineBillService.update(flow);
			}
		}
	}
	
	protected void saveFlowDetail( AbstractYiDongCrawler yd) {
		Collection<MobileOnlineList> list= yd.getFlowDetailList();
		if (list == null) {
			return;
		}
		logger.info("saveData flow detail.... size:" + list.size());
		List list1 = new ArrayList();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		String today = DateUtils.getToday("yyyy-MM");
		for(MobileOnlineList obj : list) {
			String beginTime = sdf.format(obj.getcTime());
//			if(DateUtils.isSameMonth(today, beginTime))
//				obj.setIscm(1);

			if (yd.getMaxFlowDetailTs()!=null&&obj.getcTime().getTime()<=yd.getMaxFlowDetailTs().getTime()) {
				continue ;
			}else {
				UUID uuid = UUID.randomUUID();
				obj.setId(uuid.toString());
				list1.add(obj);
			}
		}	
		mobileOnlineListService.insertbatch(list1);
	}
	
	protected SpiderListener saveSpiderListener(final AbstractYiDongCrawler yd, final User user, final String currentUser) {
		return new AbstractSpiderListener(Spider.buildListenerContext()) {
			public void onStartup(SimpleObject context, Object obj) {
				saveOrUpdateParse(user, currentUser, 1);
			}
			public void onComplete(SimpleObject contenxt, Object obj) {
				if (isSuccess(yd)) {
					queryMaxTs(user.getPhone(), yd);
					saveUser(user, yd.getUser(), currentUser);
					saveCallLogDetail(yd);
					saveMonthBill(currentUser, yd);
					saveMessage(yd);
					saveFlowBill(currentUser, yd);
					saveFlowDetail(yd);
				}
				saveOrUpdateParse(user, currentUser, 0);
			}
		};
	}
}
