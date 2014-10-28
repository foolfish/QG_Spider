/**
 * 
 */
package com.lkb.context;

import com.lkb.robot.proxy.ProxyManager;
import com.lkb.robot.proxy.ProxyManagerFactory;


/**
 * @author think
 * @date 2014-9-9
 */

public class ContextLifecycle {

	public void initializing() throws Exception {
		/*
		ProxyPool.initProxyPool();
		ProxyPool pp = ProxyPool.getProxyPool();
		pp.addProxy(new String[] {"139.217.4.210", "31288"});
		*/
		/*ProxyManagerFactory.initProxyManager();
		ProxyManager pm = ProxyManagerFactory.getProxyManager();
		pm.addProxy(new String[]{"122.96.59.103", "843"});*/
	}
	public void finalizing() throws Exception {
		
	}
}
