/**
 * 
 */
package com.lkb.thirdUtil.dx.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.thirdUtil.dx.HeiLongJiangDianXin;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.redis.RedisUtil;


/**
 * @author think
 * @date 2014-9-2
 */
public class HeiLongJiangDianXinTest extends DianXinForSpiderTest {
	protected void initData() {
		phoneNo = "13351266898";
		password = "014814";
		//个人信息  姓名,余额,身份证号,地址,邮箱
		personInfo = new String[]{"王蕾 ", "20.0", null, null, null};

		//详单信息  通话时间,对方号码,费用,时长
		detailInfo =  new String[]{"2014-07-23 14:49:01,18934700051,0.40,8", "2014-07-23 14:49:26,18934700051,0.40,5", "2014-07-23 14:51:44,18934700051,0.60,13", "2014-07-23 15:00:25,18934700051,0.60,11", 
				"2014-07-24 15:03:49,18810335650,0.60,21"};

		//账单信息  帐单月份 总费用 主套餐基本费bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
		telListInfo = new String[]{"201408,19.00", "201407,34.4", "201406,7.6"};
		//短信信息  发送时间,收短信号码,短信费,电话号码.
		/*mesInfo = new String[]{"2014-07-04 14:46:47,10659401,0.00","2014-07-16 14:15:19,10659401,0.00", "2014-07-23 12:13:47,10659401,0.00", "2014-09-01 14:33:27,10659401,0.00"
				, "2014-09-01 14:34:28,10659401,0.00", "2014-09-01 14:35:29,10659401,0.00"};	*/	
	}
	protected void execute() {
		String key = "HeiLongJiangDianXinTest";
		int seconds = 10 * 60 * 60;
		boolean isCache = false;
		//useProxy();
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
		
		dx = new HeiLongJiangDianXin(spider, null, phoneNo, password, "2345", null);		
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

