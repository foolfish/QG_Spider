/**
 * 
 */
package com.lkb.thirdUtil.dx.test.last;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.thirdUtil.dx.HeBeiDianXin;
import com.lkb.thirdUtil.dx.test.DianXinForSpiderTest;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.redis.RedisUtil;


/**
 * @author think
 * @date 2014-9-2
 */
public class HeBeiDianXinTest extends DianXinForSpiderTest {
	protected void initData() {
		phoneNo = "18033723291";
		password = "199034";
		//个人信息  姓名,余额,身份证号,地址,邮箱
		personInfo = new String[]{"石家庄世泽房地产经纪有限公司", "13.55", null, "广东省深圳市罗湖区", "sff@sdf.cn"};

		//详单信息  通话时间,对方号码,费用,时长
		detailInfo =  new String[]{"2014-07-04 14:53:32,8615390037740,0.39,9", "2014-07-04 15:03:13,8615390037740,0.39,7"};

		//账单信息  帐单月份 总费用 主套餐基本费bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
		telListInfo = new String[]{"201408,49.00", "201407,50", "201406,49"};
		//短信信息  发送时间,收短信号码,短信费,电话号码.
		mesInfo = new String[]{"2014-09-05 09:37:42,18645119158,0.1","2014-09-05 09:37:11 ,18645119158,0.1"};		
	}
	protected void execute() {
		String key = "HeBeiDianXinTest";
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
		
		dx = new HeBeiDianXin(spider, null, phoneNo, password, "2345", null);		
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
