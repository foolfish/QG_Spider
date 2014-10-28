package com.lkb.shutdown;

import org.apache.http.impl.client.CloseableHttpClient;

import com.lkb.constant.YdConstant;

/*
 * 关闭移动的链接
 * */
public class YdShutDown {

	public void shutDown(String currentUser){
		//删除上海移动
		CloseableHttpClient sh_ydcloseClientMap = YdConstant.sh_ydcloseClientMap
				.get(currentUser);
		if (sh_ydcloseClientMap != null) {
			sh_ydcloseClientMap.getConnectionManager().shutdown();
			YdConstant.sh_ydcloseClientMap.remove(currentUser);
		}
		//删除江西移动
		CloseableHttpClient jx_ydcloseClientMap = YdConstant.jx_ydcloseClientMap
				.get(currentUser);
		if (jx_ydcloseClientMap != null) {
			jx_ydcloseClientMap.getConnectionManager().shutdown();
			YdConstant.jx_ydcloseClientMap.remove(currentUser);
		}
		
		//删除北京的
//		CloseableHttpClient bj_ydcloseClientMap = YdConstant.bj_ydcloseClientMap.get(currentUser);
//		if (jx_ydcloseClientMap != null) {
//			jx_ydcloseClientMap.getConnectionManager().shutdown();
//			YdConstant.bj_ydcloseClientMap.remove(currentUser);
//		}
		
		//删除山西的
		//。。。。
		
		
		
		
	}
}
