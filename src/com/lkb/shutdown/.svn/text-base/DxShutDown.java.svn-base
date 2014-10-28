package com.lkb.shutdown;

import com.lkb.constant.Constant;
import com.lkb.constant.DxConstant;


/*
 * 关闭电信的链接
 * */
public class DxShutDown {
	public void shutdown(String currentUser) {		
		// 删除上海电信
		if (Constant.sh_uvcMap.get(currentUser) != null) {
			Constant.sh_uvcMap.remove(currentUser);
		}		
		if(Constant.sh_dxNoMap.get(currentUser) != null) {
			Constant.sh_dxNoMap.remove(currentUser);
		}
		if(Constant.sh_dxMap.get(currentUser) != null) {
			Constant.sh_dxMap.remove(currentUser);
		}
		if(DxConstant.sh_dxcloseClientMap.get(currentUser)!=null){
			DxConstant.sh_dxcloseClientMap.remove(currentUser);
		}
		if(DxConstant.sh_dxcloseClientMap1.get(currentUser)!=null){
			DxConstant.sh_dxcloseClientMap1.remove(currentUser);
		}

		// 删除北京电信
		if (DxConstant.bj_dxcloseClientMap.get(currentUser) != null) {
			DxConstant.bj_dxcloseClientMap.remove(currentUser);
		}
		// 删除福建电信
		if (DxConstant.fj_dxcloseClientMap.get(currentUser) != null) {
			//DxConstant.fj_dxcloseClientMap.remove(currentUser);
		}
		
		//删除山西
		//...
		
		

	}
}
