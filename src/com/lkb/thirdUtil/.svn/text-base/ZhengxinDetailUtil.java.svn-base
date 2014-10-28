package com.lkb.thirdUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.bean.ZhengxinDetail;
import com.lkb.bean.ZhengxinSummary;
import com.lkb.constant.Constant;
import com.lkb.service.IUserService;
import com.lkb.service.IZhengxinDetailService;
import com.lkb.service.IZhengxinSummaryService;

public class ZhengxinDetailUtil {
	
	public List getZhengxinDetail(IUserService userService,List loginNames,IZhengxinDetailService zhengxinDetailService,IZhengxinSummaryService zhengxinSummaryService){
		List list = new ArrayList();
		for(int i=0;i<loginNames.size();i++){
			Map map = new HashMap();
			String loginName = loginNames.get(i).toString();
			List<ZhengxinDetail> lists1 = zhengxinDetailService.getZhengxinByLoginName(loginName);
			List<ZhengxinSummary> lists2 = zhengxinSummaryService.getZhengxinSummaryByLoginName(loginName);			
			
			Map<String, String> map2 = new HashMap<String, String>(2);
			map2.put("loginName", loginName);
			map2.put("usersource", Constant.ZHENGXIN);
			List<User> list2 = userService.getUserByUserNamesource(map2);
			if (list2 != null && list2.size() > 0) {
				User user = list2.get(0);
				String realname = user.getRealName();
				String cardtype = user.getCardType();
				String cardno = user.getCardNo();
				String merry = user.getMerry();
				map.put("realname", realname);
				map.put("cardtype", cardtype);
				map.put("cardno", cardno);
				map.put("merry", merry);
			}

			map.put("loginName", loginName);
			map.put("source", Constant.ZHENGXIN);
			map.put("detailData", lists1);
			map.put("summaryData", lists2);
	
			list.add(map);
		}
		return list;
	}
}
