/**
 * 
 */
package com.lkb.thirdUtil.dx.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.debug.DebugUtil;
import com.lkb.thirdUtil.dx.NeiMengGuDianXin;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.redis.RedisUtil;


/**
 * @author think
 * @date 2014-9-2
 */
public class NeiMengGuDianXinTest extends DianXinForSpiderTest {
	protected void initData() {
		phoneNo = "13314863621";
		password = "145306";
		//个人信息  姓名,余额,身份证号,地址,邮箱
		personInfo = new String[]{"冀爱枝 ", "21.60", null, null, null};
		
		//","converseDate":"2014-08-12","converseDuration":"0'0'20","callingNbr":"","base":"00033:18546","
		//详单信息  通话时间,对方号码,费用,时长
		detailInfo =  new String[]{"2014-08-12 15:44:49,05922591472,0.40,20"};
		
		//账单信息  帐单月份 总费用 主套餐基本费bdthf; 本地通话费ldxsf; 来电显示费 mythf; 漫游通话费
		telListInfo = new String[]{"201408,19.00", "201407,34.4", "201406,7.6"};
		//短信信息  发送时间,收短信号码,短信费,电话号码.
		mesInfo = new String[]{"2014-09-05 20:22:32, 13864262971 ,0.00"};
	}
	protected void execute() {
		String key = "NeiMengGuDianXinTest";
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
		
		dx = new NeiMengGuDianXin(spider, null, phoneNo, password, "2345", null);		
		if (map == null && !isCache) {
			NeiMengGuDianXin dx1 = (NeiMengGuDianXin) dx;
			boolean testSms = false;
			dx.setTest(true);
			if (testSms) {
				DebugUtil.addToCookieStore("nm.189.cn", ".ybtj.189.cn=9FDFBB8215C9D4498124E6C1EF5D19A4;BIGipServerDZQD-WangTing-GeRenZiZhu=84218028.25371.0000;JSESSIONID=Tn5dJjTD155nLJ2SzWG8GhJQTxGSg7KJKp7BcWM67pgKLZz29ngH!31942275!1577129756;cityCode=nm;isLogin=logined;userId=201|159206657;");
				//DebugUtil.addToCookieStore("nm.189.cn", "svid=53cf75caa8db8e55b2171f6c4a037fc6; dqmhIpCityInfos=%E5%8C%97%E4%BA%AC%E5%B8%82+%E7%94%B5%E4%BF%A1; SHOPID_COOKIEID=10008; userId=201|159206657; Hm_lvt_2e629dae6af3fccff6fc5bcc4e9e067e=1407383972; pgv_pvid=3510660951; s_pers=%20s_fid%3D300F99DC0CB56354-2DA21587DDF69803%7C1474705536242%3B; _gscu_1758414200=07806228id0ems13; WT_FPC=id=2b47cd42519e19c31681407806515711:lv=1407806515711:ss=1407806515711; _gscu_1708861450=08414401qgrh1a16; citrix_ns_id_.189.cn_%2F_wlf=TlNDX3h1LTIyMi42OC4xODUuMjI5?NsNVGZ1ouCBNq/CMOEeCK+p6z4YA&; flag=2; pgv_pvi=9517952000; Hm_lvt_9c25be731676bc425f242983796b341c=1409307916; cityCode=nm; ffstat_uv=9280660341501856971|3023; BIGipServerDZQD-WangTing-MenHu=67440812.17443.0000; s_sess=%20s_cc%3Dtrue%3B%20s_sq%3Deshipeship-189-all%252Ceship-189-nm%253D%252526pid%25253D%2525252Fnm%2525252F%252526pidt%25253D1%252526oid%25253Dhttp%2525253A%2525252F%2525252Fnm.189.cn%2525252Fselfservice%2525252Fbill%2525252Fxd%252526ot%25253DA%3B; loginStatus=logined; isLogin=logined; .ybtj.189.cn=9FDFBB8215C9D4498124E6C1EF5D19A4; s_cc=true; _last_url=/selfservice/bill/xd; JSESSIONID=zN2wJv1T2tGw0nxzYTcBy7s2pv12PdTFhJnM9d15j4TTwQ91mLhl!-53561157!-661392439; BIGipServerDZQD-WangTing-GeRenZiZhu=84218028.26395.0000; COOKIE_LOGINACCT=13314863621; COOKIE_LOGINACCTTYPE=4; COOKIE_AREACODE=0472; COOKIE_CODE=0");
				dx1.beforeSendSmsPassword();
				dx1.sendSmsPasswordForRequireCallLogService();
				
				//dx.requestAllService();
				spider.start();
				dx.printData();
			} else {
				dx.checkVerifyCode(phoneNo);
				spider.start();
				dx.printData();
				dx.getData().clear();
				dx.setAuthCode(CUtil.inputYanzhengma());
				dx.goLoginReq();
				spider.start();
				dx.printData();
				dx.setAuthCode(CUtil.inputYanzhengma("短信码："));
				//dx1.sendSmsPasswordForRequireCallLogService();
				//spider.start();
				dx.requestAllService();
				spider.start();
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

