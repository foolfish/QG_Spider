/*
 * PROPRIETARY/CONFIDENTIAL
 */
package com.lkb.robot.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkb.bean.SimpleObject;
import com.lkb.robot.Request;
import com.lkb.robot.util.ContextUtil;
import com.lkb.warning.WarningUtil;

/**
 * @author think
 * @date 2014年6月21日
 */
public abstract class AbstractProcessorObserver implements ProcessorObserver {
	private WarningUtil util;
	private String warnType;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	public AbstractProcessorObserver(WarningUtil util, String warnType) {
		this.util = util;
		this.warnType = warnType;
	}

	@Override
	public void preparedData(SimpleObject context) {
		if (ContextUtil.getError(context) != null) {
			Request request = ContextUtil.getRequest(context);
			logger.error(request.getMethod() == null ? "GET" : request.getMethod() + " page " + request.getUrl(), ContextUtil.getError(context));
			if (util != null) {
				util.warning(warnType);
			}
			logger.error("Response Text:" + ContextUtil.getContent(context));
		}
	}
	
	@Override
	public void beforeRequest(SimpleObject context) {
	}
	
	@Override
	public void afterRequest(SimpleObject context) {
	}
}
