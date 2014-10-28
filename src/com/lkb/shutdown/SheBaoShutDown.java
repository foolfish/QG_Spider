package com.lkb.shutdown;

import org.apache.http.impl.client.CloseableHttpClient;
import com.lkb.constant.SbConstant;


/*
 * 关闭社保的链接
 * */
public class SheBaoShutDown {
	public void shutDown(String currentUser) {
		// 删除深圳社保
		CloseableHttpClient szsbcloseClientMap = SbConstant.szsbcloseClientMap
				.get(currentUser);
		if (szsbcloseClientMap != null) {
			szsbcloseClientMap.getConnectionManager().shutdown();
			SbConstant.szsbcloseClientMap.remove(currentUser);
		}
		
		//删除上海社保		
		CloseableHttpClient httpclient = SbConstant.shhttpClientMap
				.get(currentUser);
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
			SbConstant.shhttpClientMap.remove(currentUser);
		}
		// 删除北京的

		// 删除山西的
		// 。。。。
	}
}
