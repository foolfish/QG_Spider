package com.lkb.location;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.controller.dx.LiaoNingDianxin_Controller;
import com.lkb.controller.lt.SHLianTong_Controller;
import com.lkb.controller.yd.LiaoNingYiDong_Controller;
/*辽宁*/
public class LN_Base extends AbstractBase {
	
	public void goWhere(String ptype,String currentUser,Model model) {

		if (ptype.contains("移动")) {
			/*DefaultHttpClient client = CUtil.init(); 
			Map<String,Object> map = new HashMap<String,Object>();
			map.put(ConstantHC.k_client,client);
			YdConstant.gd_ydMap.put(currentUser, map);*/
			model.addAttribute("putong_ln_yidong_url", "none");			
		} else if  (ptype.contains("联通")) {
			model.addAttribute("putong_sh_liantong_url", "none");
		}else if (ptype.contains("电信")) {
			
			model.addAttribute("putong_ln_dianxin_url", "none");
		} else {
			
		}
	}
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			LiaoNingYiDong_Controller controller = (LiaoNingYiDong_Controller) getBean(LiaoNingYiDong_Controller.class);
			//在map中需要有个url的key，为none是不需要验证码，否则返回验证码的链接
			Map map = controller.getAuth(false, fd);
			final String url = (String) map.get("url");
			r.setStatus(1);
			r.setImgUrl(url);
			r.setSuccess(true);
			r.setResult(true);
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			LiaoNingDianxin_Controller controller = (LiaoNingDianxin_Controller) getBean(LiaoNingDianxin_Controller.class);
			Map map = controller.getAuth(false, fd);
			setImgResult(r, map);
		}
		return r;
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			//r = setImgCodeResult(fd, null);
			if (r.getErrorcode() == 0) {
				LiaoNingYiDong_Controller controller = (LiaoNingYiDong_Controller) getBean(LiaoNingYiDong_Controller.class);
				Map map = controller.putong_vertifyLogin(false, fd);
				setResult(r, map);
				//未完成流程，走下一步，短信码
				r.setResult(false);
			}
		} else if (isLianTong(phoneNum)) {
		} else if (isDianXin(phoneNum)) {
			LiaoNingDianxin_Controller controller = (LiaoNingDianxin_Controller) getBean(LiaoNingDianxin_Controller.class);
			Map map = controller.putong_vertifyLogin(false, fd);
			setResult(r, map);
		}
		return r;
	}
	
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			LiaoNingYiDong_Controller controller = (LiaoNingYiDong_Controller) getBean(LiaoNingYiDong_Controller.class);
			Map map = controller.getSms(false, fd);
			setResult(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			
		}
		return r;
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		Result r = new Result();
		if (isYiDong(phoneNum)) {
			LiaoNingYiDong_Controller controller = (LiaoNingYiDong_Controller) getBean(LiaoNingYiDong_Controller.class);
			Map map = controller.dongtai_vertifyLogin(false, fd);
			setResult(r, map);
		} else if (isLianTong(phoneNum)) {
			
		} else if (isDianXin(phoneNum)) {
			
		}
		return r;
	}
}
