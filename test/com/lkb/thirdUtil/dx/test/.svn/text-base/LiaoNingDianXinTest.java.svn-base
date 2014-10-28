/**
 * 
 */
package com.lkb.thirdUtil.dx.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.thirdUtil.dx.LiaoNingDianxin;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.redis.RedisUtil;


/**
 * @author think
 * @date 2014-9-2
 */
public class LiaoNingDianXinTest extends DianXinForSpiderTest {
	protected void initData() {
		phoneNo = "18134537231";
		password = "062888";
		//个人信息  姓名,余额,身份证号,地址,邮箱
		personInfo = new String[]{"周灏", "-5.24", null, "广东省深圳市罗湖区", "sff@sdf.cn"};

		//详单信息  通话时间,对方号码,费用,时长
		detailInfo =  new String[]{"2014-07-04 14:53:32,8615390037740,0.39,9", "2014-07-04 15:03:13,8615390037740,0.39,7", "2014-07-23 12:06:17,8618810335650,0.39,12", "2014-07-23 12:09:13,8618622818293,0.39,7", 
				"2014-07-23 14:12:40,8618622818293,1.56,206"};

		//账单信息  帐单月份 总费用 主套餐基本费bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
		telListInfo = new String[]{"201408,19.00", "201407,32.76", "201406,0.00", "201405,0.00", "201404,0.00", "201403,0.00"};
		//短信信息  发送时间,收短信号码,短信费,电话号码.
		mesInfo = new String[]{"2014-07-04 14:46:47,10659401,0.00","2014-07-16 14:15:19,10659401,0.00", "2014-07-23 12:13:47,10659401,0.00", "2014-09-01 14:33:27,10659401,0.00"
				, "2014-09-01 14:34:28,10659401,0.00", "2014-09-01 14:35:29,10659401,0.00"};		
	}
	protected void execute() {
		String key = "AnHuiDianXinTest";
		int seconds = 10 * 60 * 60;
		boolean isCache = false;
		
		final String userKey = "user";
		final String telKey = "telList";
		final String detailKey = "detailList";
		final String messKey = "messList";
		Map map = null;
		
		try {
			map = RedisUtil.getMap(key);
		} catch (Exception e) {
			System.out.println("No Redis");
		}
		
		dx = new LiaoNingDianxin(spider, null, phoneNo, password, "2345", null);		
		if (map == null && !isCache) {
			dx.setTest(true);
			dx.checkVerifyCode(phoneNo);
			spider.start();
			dx.printData();
			dx.getData().clear();
			dx.setAuthCode(CUtil.inputYanzhengma());
			dx.goLoginReq();
			spider.start();
			dx.printData();
			
			try {
				map = new HashMap();
				
				map.put(userKey, dx.getUser());
				map.put(telKey, dx.getTelList());
				map.put(detailKey, dx.getDetailList());
				map.put(messKey, dx.getMessageList());
				RedisUtil.setMapToRedis(key, map, seconds);
			} catch (Exception e) {
				System.out.println("No Redis");
			}
		} else if (map != null) {
			dx.setStatus(dx.STAT_SUC);
			
			user = (User) map.get(userKey);
			Collection telList = (Collection) map.get(telKey);
			Collection detailList = (Collection) map.get(detailKey);
			Collection messList = (Collection) map.get(messKey);
			
			dx.getTelList().addAll(telList);
			dx.getDetailList().addAll(detailList);
			dx.getMessageList().addAll(messList);
		}
		
		/*dx.setAuthCode(CUtil.inputYanzhengma());
		dx.requestAllService();
		spider.start();*/
		//dx.printData();
	}
}

