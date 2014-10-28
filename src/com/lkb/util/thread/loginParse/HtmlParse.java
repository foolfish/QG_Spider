package com.lkb.util.thread.loginParse;


import java.io.IOException;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;

import com.lkb.bean.client.Login;
import com.lkb.constant.ConstantNum;
import com.lkb.constant.DxConstant;
import com.lkb.constant.YdConstant;
import com.lkb.controller.dx.BJDianXin_Controller;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.thirdUtil.dx.BJDianxin;
import com.lkb.thirdUtil.dx.FJDianxin;
import com.lkb.thirdUtil.dx.GSDianxin;
import com.lkb.thirdUtil.dx.TJDianxin;
import com.lkb.thirdUtil.yd.BJYidong;
import com.lkb.thirdUtil.yd.JXYidong;
import com.lkb.thirdUtil.yd.XJYidong;
import com.lkb.util.httpclient.constant.ConstantHC;

public class HtmlParse {
	private static Logger log = Logger.getLogger(BJDianXin_Controller.class);

//	public static void parseBJDianxinHTML(String phone,
//			IUserService userService, IWarningService warningService,
//			String currentUser, IDianXinTelService dianXinTelService,
//			IDianXinDetailService dianXinDetailService) {
//		String key = ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_bj_dx);
//		Map<String, Object> redismap = RedisClient.getRedisMap(key);
//		DefaultHttpClient client = RedisClient.getClient(redismap);
//		
//		BJDianxin service = new BJDianxin();
//		// 获取个人资料
//		boolean isMyInfo = service.getMyInfo(client, phone, currentUser,
//				warningService, userService);
//		if (isMyInfo) {
//			log.info(phone + "获取个人资料成功");
//		} else {
//			log.info(phone + "获取个人资料成功");
//		}
//		// 获取通话记录
//		boolean isPhoneDetail = service.getPhoneDetailHtml(client, phone,
//				dianXinDetailService, currentUser, warningService);
//		if (isPhoneDetail) {
//			log.info(phone + "获取通话记录成功");
//		} else {
//			log.info(phone + "获取通话记录失败");
//		}
//		// 获取话费
//		boolean isTelDetail = service.getTelDetailHtml(client, phone,
//				currentUser, warningService, dianXinTelService);
//		if (isTelDetail) {
//			log.info(phone + "获取话费资料成功");
//
//		} else {
//			log.info(phone + "获取话费资料失败");
//		}
//
//		client.close();
//		RedisClient.delRedisMap(key);
//	}
//
//	public static void parseJXYidongHTML(String phone,
//			IUserService userService, IWarningService warningService,
//			String currentUser, IMobileDetailService mobileDetailService,
//			IMobileTelService mobileTelService) {
//		CloseableHttpClient httpclient = YdConstant.jx_ydcloseClientMap
//				.get(currentUser);
//		JXYidong yidong = new JXYidong();
//		// 获取个人资料
//		boolean isMyInfo = yidong.getMyInfo(httpclient, currentUser,
//				warningService, userService,phone);
//		if (isMyInfo) {
//			log.info(phone + "获取个人资料成功");
//		} else {
//			log.info(phone + "获取个人资料成功");
//		}
//		// 获取通话记录
//		boolean isPhoneDetail = yidong.getPhoneDetailHtml(httpclient,
//				mobileDetailService, phone, currentUser, warningService);
//		if (isPhoneDetail) {
//			log.info(phone + "获取通话记录成功");
//		} else {
//			log.info(phone + "获取通话记录失败");
//		}
//		// 获取话费
//		boolean isTelDetail = yidong.getTelDetailHtml(httpclient, phone,
//				currentUser, warningService, mobileTelService);
//		if (isTelDetail) {
//			log.info(phone + "获取话费资料成功");
//		} else {
//			log.info(phone + "获取话费资料失败");
//		}
//		try {
//			httpclient.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			YdConstant.jx_ydcloseClientMap
//			.remove(currentUser);
//		}
//		
//	}

//	/*
//	 * 北京移动
//	 */
//	public static void parseBJYidongHTML(String phone,
//			IUserService userService, IWarningService warningService,
//			String currentUser, IMobileDetailService mobileDetailService,
//			IMobileTelService mobileTelService, String fwpassword) {
//		BJYidong yidong = new BJYidong(new Login(phone,fwpassword),warningService,currentUser);
//		// 获取个人资料
//		// 获取话费
//		int isTelDetail = yidong.getTelDetailHtml(mobileTelService);
//		if (isTelDetail == 1) {
//			log.info(phone + "获取话费资料成功.");
//		} else {
//			log.info(phone + "获取话费资料失败.");
//		}
//		// 获取通话记录
//		int isPhoneDetail = yidong.getPhoneDetailHtml( mobileDetailService);
//		if (isPhoneDetail == 1) {
//			log.info(phone + "获取通话记录成功.");
//		} else if (isPhoneDetail == -1) {
//			log.info(phone + "获取通话记录失败,服务密码不正确.");
//		} else {
//			log.info(phone + "获取通话记录失败.");
//		}
//
//		// 获取个人资料
//		int isMyInfo = yidong.getMyInfo( userService);
//		if (isMyInfo == 1) {
//			log.info(phone + "获取个人资料成功.");
//		} else {
//			log.info(phone + "获取个人资料失败.");
//		}
//		yidong.clearRedis();
//		yidong.close();
//	}

	/***
	 * 
	 * 福建电信
	 * */
	/*public static void parseFJDianxinHTML(String phone,
			IUserService userService, IWarningService warningService,
			String currentUser, IDianXinTelService dianXinTelService,
			IDianXinDetailService dianXinDetailService) {
		Map fjmap =DxConstant.fj_dxcloseClientMap.get(currentUser);
		DefaultHttpClient client = (DefaultHttpClient)fjmap.get(ConstantHC.k_client);	
		FJDianxin service = new FJDianxin();
		// 获取个人资料
		boolean isMyInfo = service.getMyInfo(client, phone, currentUser,warningService, userService);
		if (isMyInfo) {
			log.info(phone + "获取个人资料成功");
		} else {
			log.info(phone + "获取个人资料成功");
		}
		// 获取通话记录
		boolean isPhoneDetail = service.getPhoneDetailHtml(client, phone,dianXinDetailService, currentUser, warningService);
		if (isPhoneDetail) {
			log.info(phone + "获取通话记录成功");
		} else {
			log.info(phone + "获取通话记录失败");
		}
		// 获取话费
		boolean isTelDetail = service.getTelDetailHtml(client, phone,currentUser, warningService, dianXinTelService);
		if (isTelDetail) {
			log.info(phone + "获取话费资料成功");

		} else {
			log.info(phone + "获取话费资料失败");
		}
		 
		
		try {
			client.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DxConstant.fj_dxcloseClientMap.remove(currentUser);
		}
		
	}*/
	
	
	/***
	 * 
	 * 福建移动
	 * */
	public static void parseFJYingDongHTML(String phone,
			IUserService userService, IWarningService warningService,
			String currentUser, IMobileDetailService mobileDetailService,
			IMobileTelService mobileTelService) {
		CloseableHttpClient httpclient = YdConstant.fj_ydcloseClientMap.get(currentUser);
		/*FJYingDong service = new FJYingDong();
		// 获取个人资料asd
		boolean isMyInfo = service.getMyInfo(httpclient, phone, currentUser,warningService, userService);
		if (isMyInfo) {
			log.info(phone + "获取个人资料成功");
		} else {
			log.info(phone + "获取个人资料成功");
		}
		// 获取通话记录
		boolean isPhoneDetail = service.getPhoneDetailHtml(httpclient, phone,mobileDetailService, currentUser, warningService);
		if (isPhoneDetail) {
			log.info(phone + "获取通话记录成功");
		} else {
			log.info(phone + "获取通话记录失败");
		}
		// 获取话费
		boolean isTelDetail = service.getTelDetailHtml(httpclient, phone,currentUser, warningService, mobileTelService);
		if (isTelDetail) {
			log.info(phone + "获取话费资料成功");

		} else {
			log.info(phone + "获取话费资料失败");
		}
		
		try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			YdConstant.fj_ydcloseClientMap.remove(currentUser);
		}*/
		
		 
	}

	
	/***
	 * 
	 * 浙江移动
	 * */
	/*public static void parseZJYingDongHTML(String phone,
			IUserService userService, IWarningService warningService,
			String currentUser, IMobileDetailService mobileDetailService,
			IMobileTelService mobileTelService) {
		CloseableHttpClient httpclient = YdConstant.zj_ydcloseClientMap.get(currentUser);
		ZJYidong service = new ZJYidong();
		
		// 获取通话记录
		boolean isPhoneDetail = service.getPhoneDetailHtml(httpclient, phone,mobileDetailService, currentUser, warningService);
		if (isPhoneDetail) {
			log.info(phone + "获取通话记录成功");
		} else {
			log.info(phone + "获取通话记录失败");
		}
		// 获取话费
		boolean isTelDetail = service.getTelDetailHtml(httpclient, phone,currentUser, warningService, mobileTelService);
		if (isTelDetail) {
			log.info(phone + "获取话费资料成功");
		} else {
			log.info(phone + "获取话费资料失败");
		}
		
		// 获取个人资料
		boolean isMyInfo = service.getMyInfo(httpclient, phone, currentUser,warningService, userService);
		if (isMyInfo) {
			log.info(phone + "获取个人资料成功");
		} else {
			log.info(phone + "获取个人资料成功");
		}
		 
	}*/

//	/*
//	 * 江苏移动
//	 * */
//	public static void parseJSYidongHTML(String phone, String currentUser  ,String smsCode, IUserService userService, IWarningService warningService, IMobileDetailService mobileDetailService, IMobileTelService mobileTelService)  {
//
//		 Map jsMap =YdConstant.jsMap.get(currentUser); 
//		 DefaultHttpClient httpclient = (DefaultHttpClient)jsMap.get(ConstantHC.k_client);
//		 JSYidong yidong = new JSYidong();
//		 yidong.saveMobileDetail( httpclient,  phone, currentUser, smsCode, mobileDetailService);
//		 yidong.saveUserInfoAndCurrentPay( httpclient,  userService, phone, currentUser, mobileTelService );
//		 yidong.saveMobileTel( httpclient,  phone, currentUser, mobileTelService);
//			
//		 if (YdConstant.jsMap.get(currentUser) != null) {
//			
//			 httpclient.close();
//			 jsMap.clear();
//			 YdConstant.jsMap.remove(currentUser);
//		 }
//
//	}
	
//	/***
//	 * 
//	 * 浙江电信
//	 * */
//	public static void parseZJDianxinHTML(String phone,
//			IUserService userService, IWarningService warningService,
//			String currentUser, IDianXinTelService dianXinTelService,
//			IDianXinDetailService dianXinDetailService,String smscode) {
//		Map jzmap =DxConstant.zjMap.get(currentUser);
//		DefaultHttpClient httpclient = (DefaultHttpClient)jzmap.get(ConstantHC.k_client);		
//		ZJDianxin service = new ZJDianxin();
//		
//		
//		
//		// 获取话费
//		boolean isTelDetail = service.getTelDetailHtml(httpclient, phone,currentUser, warningService, dianXinTelService);
//		if (isTelDetail) {
//			log.info(phone + "获取话费资料成功");
//		} else {
//			log.info(phone + "获取话费资料失败");
//		}
//		
//		// 获取通话记录
//		boolean isPhoneDetail = service.getPhoneDetailHtml(httpclient, phone,dianXinDetailService, currentUser, warningService,smscode);
//		if (isPhoneDetail) {
//			log.info(phone + "获取通话记录成功");
//		} else {
//			log.info(phone + "获取通话记录失败");
//		}
//		
//		// 获取个人资料
//		boolean isMyInfo = service.getMyInfo(httpclient, phone, currentUser,warningService, userService);
//		if (isMyInfo) {
//			log.info(phone + "获取个人资料成功");
//		} else {
//			log.info(phone + "获取个人资料成功");
//		}
//		
//		
//		if (DxConstant.zjMap.get(currentUser) != null) {
//			jzmap.clear();
//			httpclient.close();
//			DxConstant.zjMap.remove(currentUser);
//		 }
//		
//		 
//	}
	
	
	
	/***
	 * 
	 * 四川电信
	 * */
	/*public static void parseSCDianxinHTML(String phone,
			IUserService userService, IWarningService warningService,
			String currentUser, IDianXinTelService dianXinTelService,
			IDianXinDetailService dianXinDetailService) {
		Map<String,Object> map =DxConstant.sc_dxMap.get(currentUser);
		DefaultHttpClient client = (DefaultHttpClient)map.get(ConstantHC.k_client);	
		SCDianXin service = new SCDianXin();
		// 获取个人资料
		boolean isMyInfo = service.getMyInfo(map, phone, currentUser,warningService, userService);
		if (isMyInfo) {
			log.info(phone + "获取个人资料成功");
		} else {
			log.info(phone + "获取个人资料成功");
		}
		// 获取通话记录
		boolean isPhoneDetail = service.getPhoneDetailHtml(map, phone,dianXinDetailService, currentUser, warningService);
		if (isPhoneDetail) {
			log.info(phone + "获取通话记录成功");
		} else {
			log.info(phone + "获取通话记录失败");
		}
		// 获取话费
		boolean isTelDetail = service.getTelDetailHtml(map, phone,currentUser, warningService, dianXinTelService);
		if (isTelDetail) {
			log.info(phone + "获取话费资料成功");

		} else {
			log.info(phone + "获取话费资料失败");
		}
		 
		
		try {
			client.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DxConstant.sc_dxMap.remove(currentUser);
		}
		
	}*/
	
//	/***
//	 * 
//	 * 山东电信
//	 * */
//	public static void parseSDDianxinHTML(String phone,
//			IUserService userService, IWarningService warningService,
//			String currentUser, IDianXinTelService dianXinTelService,
//			IDianXinDetailService dianXinDetailService) {
//		Map<String,Object> map =DxConstant.sd_dxMap.get(currentUser);
//		DefaultHttpClient client = (DefaultHttpClient)map.get(ConstantHC.k_client);	
//		SDDianXin service = new SDDianXin();
//		// 获取个人资料
//		boolean isMyInfo = service.getMyInfo(map, phone, currentUser,warningService, userService);
//		if (isMyInfo) {
//			log.info(phone + "获取个人资料成功");
//		} else {
//			log.info(phone + "获取个人资料成功");
//		}
//		// 获取通话记录
//		boolean isPhoneDetail = service.getPhoneDetailHtml(map, phone,dianXinDetailService, currentUser, warningService);
//		if (isPhoneDetail) {
//			log.info(phone + "获取通话记录成功");
//		} else {
//			log.info(phone + "获取通话记录失败");
//		}
//		// 获取话费
//		boolean isTelDetail = service.getTelDetailHtml(map, phone,currentUser, warningService, dianXinTelService);
//		if (isTelDetail) {
//			log.info(phone + "获取话费资料成功");
//
//		} else {
//			log.info(phone + "获取话费资料失败");
//		}
//		 
//		
//		try {
//			client.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			DxConstant.sc_dxMap.remove(currentUser);
//		}
//		
//	}
//	
//	
	/***
	 * 
	 * 山东电信
	 * */
	/*public static void parseSDDianxinHTML(String phone,
			IUserService userService, IWarningService warningService,
			String currentUser, IDianXinTelService dianXinTelService,
			IDianXinDetailService dianXinDetailService) {
		Map<String,Object> map =DxConstant.sd_dxMap.get(currentUser);
		DefaultHttpClient client = (DefaultHttpClient)map.get(ConstantHC.k_client);	
		SDDianXin service = new SDDianXin();
		// 获取个人资料
		boolean isMyInfo = service.getMyInfo(map, phone, currentUser,warningService, userService);
		if (isMyInfo) {
			log.info(phone + "获取个人资料成功");
		} else {
			log.info(phone + "获取个人资料成功");
		}
		// 获取通话记录
		boolean isPhoneDetail = service.getPhoneDetailHtml(map, phone,dianXinDetailService, currentUser, warningService);
		if (isPhoneDetail) {
			log.info(phone + "获取通话记录成功");
		} else {
			log.info(phone + "获取通话记录失败");
		}
		// 获取话费
		boolean isTelDetail = service.getTelDetailHtml(map, phone,currentUser, warningService, dianXinTelService);
		if (isTelDetail) {
			log.info(phone + "获取话费资料成功");

		} else {
			log.info(phone + "获取话费资料失败");
		}
		 
		
		try {
			client.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DxConstant.sc_dxMap.remove(currentUser);
		}
		
	}
	*/
	
	/*
	 * 新疆移动
	 * */
	public static void parseXJYidongHTML(String phone,String password, IUserService userService,
			 IWarningService warningService,String currentUser,IMobileDetailService mobileDetailService,IMobileTelService mobileTelService){
		String key = ConstantNum.getClientMapKey(currentUser + phone,
				ConstantNum.comm_xj_yidong);
//		Map<String, Object> redismap = RedisClient.getRedisMap(key);
//		DefaultHttpClient httpclient = RedisClient.getClient(redismap);
//		XJYidong yidong = new XJYidong();
//		//保存用户信息
//		int isUserInfo=yidong.saveUserInfo( httpclient,currentUser, warningService, userService,phone);
//		//保存账单信息
//		int isMobileTel=yidong.saveMobileTel(httpclient,currentUser,warningService,mobileTelService,phone);
//		//保存通话记录
//		int isMobileDetail =yidong.saveMobileDetail(httpclient, currentUser, warningService, mobileDetailService, phone);
//
//		RedisClient.setRedisMap(redismap, key);
	}
	
	
}
