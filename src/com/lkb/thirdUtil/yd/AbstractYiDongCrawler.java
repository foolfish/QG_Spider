/**
 * 
 */
package com.lkb.thirdUtil.yd;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import com.lkb.bean.MobileDetail;
import com.lkb.bean.MobileMessage;
import com.lkb.bean.MobileOnlineBill;
import com.lkb.bean.MobileOnlineList;
import com.lkb.bean.MobileTel;
import com.lkb.service.IDianXinFlowDetailService;
import com.lkb.service.IDianXinFlowService;
import com.lkb.service.IMobileOnlineBillService;
import com.lkb.service.IMobileOnlineListService;
import com.lkb.thirdUtil.AbstractCrawler;

/**
 * @author think
 * @date 2014-8-16
 */
public class AbstractYiDongCrawler extends AbstractCrawler {
	
	protected Collection<MobileDetail> detailList = new ArrayList();
	protected Collection<MobileTel> telList = new ArrayList();
	protected Collection<MobileMessage> mobileList = new ArrayList();
	protected Collection<MobileOnlineList> flowDetailList = new ArrayList();
	protected Collection<MobileOnlineBill> flowList = new ArrayList();
	
	
	public Collection<MobileDetail> getDetailList() {
		return detailList;
	}
	public Collection<MobileTel> getTelList() {
		return telList;
	}
	public Collection<MobileMessage> getMessageList(){
		return mobileList;
	}
	public Collection<MobileOnlineList> getFlowDetailList() {
		return flowDetailList;
	}
	public Collection<MobileOnlineBill> getFlowList() {
		return flowList;
	}
	
	
}
