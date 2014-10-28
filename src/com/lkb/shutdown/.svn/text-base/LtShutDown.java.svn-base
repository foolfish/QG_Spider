package com.lkb.shutdown;

import org.apache.http.impl.client.CloseableHttpClient;

import com.lkb.constant.LtConstant;


/*
 * 关闭联通的链接
 * */
public class LtShutDown {
	
	public void shutDown(String currentUser) {
		// 删除上海联通
		CloseableHttpClient sh_ltcloseClientMap = LtConstant.sh_ltcloseClientMap
				.get(currentUser);
		if (sh_ltcloseClientMap != null) {
			sh_ltcloseClientMap.getConnectionManager().shutdown();
			LtConstant.sh_ltcloseClientMap.remove(currentUser);
		}

		// 删除北京的

		// 删除山西的
		// 。。。。
	}
}
