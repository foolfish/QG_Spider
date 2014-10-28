package com.lkb.util.thread.loginParse;

import java.io.Serializable;
import java.util.Map;


import com.lkb.bean.client.Login;
import com.lkb.constant.ConstantNum;
import com.lkb.constant.DxConstant;
import com.lkb.service.ICBCCheckBillItemService;
import com.lkb.service.ICBCCheckBillService;
import com.lkb.service.ICMBTransferBillService;
import com.lkb.service.IDianXinDetailService;
import com.lkb.service.IDianXinTelService;
import com.lkb.service.IMobileDetailService;
import com.lkb.service.IMobileTelService;
import com.lkb.service.IOrderItemService;
import com.lkb.service.IOrderService;
import com.lkb.service.IParseService;
import com.lkb.service.IPayInfoService;
import com.lkb.service.ISheBaoService;
import com.lkb.service.IUnicomDetailService;
import com.lkb.service.IUnicomTelService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.service.IYuEBaoService;
import com.lkb.service.IZhengxinDetailService;
import com.lkb.service.IZhengxinSummaryService;
import com.lkb.thirdUtil.CommonMail;
import com.lkb.thirdUtil.JD;
import com.lkb.thirdUtil.XueXin;
import com.lkb.thirdUtil.Zhengxin;
import com.lkb.thirdUtil.dx.CQDianXin;
import com.lkb.thirdUtil.dx.HuBDianxin;
import com.lkb.thirdUtil.dx.SHDianXin;
import com.lkb.thirdUtil.lt.SHLiantong;
import com.lkb.thirdUtil.shebao.ShShebao;
import com.lkb.thirdUtil.yd.CQYidong;
import com.lkb.thirdUtil.yd.HENYidong;
import com.lkb.thirdUtil.yd.HLJYidong;
import com.lkb.thirdUtil.yd.HUBYidong;
import com.lkb.thirdUtil.yd.NMGYidong;
import com.lkb.thirdUtil.yd.SCYiDong;
import com.lkb.thirdUtil.yd.SDYidong;
//import com.lkb.thirdUtil.yd.CQYidong;


/**
 * 授权登陆成功后,启动线程开启采集任务
 * @author fastw
 *
 */
public class Control implements Runnable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private IParseService parseService;
	private IUserService userService;
	private IOrderService orderService;
	private IWarningService warningService;
	private IOrderItemService orderItemService;
	private String currentUser;
	private Integer type = 0;//1.京东.2淘宝.3....4....
	private IPayInfoService payInfoService;
	private IYuEBaoService yuebaoService;
	private String smscode;
	private Login login;
	private String content;
	public Integer getType() {
		return type;
	}
	/***
	 * 设置type值 1.京东 2.淘宝 3.......4....  <br/>
	 * 100联通 -101全部<br/>
	 * 200电信 -201 北京 202 重庆 203 上海 204 福建 205 浙江 206江苏207天津电信208甘肃电信209四川电信210山东电信<br/>
	 * 300移动 -301江西 302 上海 303 北京304 福建  306 浙江 307江苏308广东309天津310四川移动311山东移动312重庆移动313内蒙古移动<br/>
	 * 400社保-401上海 402深圳<br/>
	 * 500征信-501学信502征信<br/>
	 * 600邮箱-601邮箱<br/>
	 * <br/>
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/***
	 * 京东后台解析
	 * @param username
	 * @param password
	 */
	public Control(String username, String password, IParseService parseService ,IUserService userService,
			IOrderService orderService, IWarningService warningService,
			IOrderItemService orderItemService, String currentUser) {
		super();
		this.username = username;
		this.password = password;
		this.parseService = parseService;
		this.userService = userService;
		this.orderService = orderService;
		this.warningService = warningService;
		this.orderItemService = orderItemService;
		this.currentUser = currentUser;
	}

	/**
	 * 淘宝后台解析
	 * @param username
	 * @param password
	 */
	public Control(String currentUser,String username,IParseService parseService,IUserService userService,
			IOrderService orderService,IOrderItemService orderItemService,IPayInfoService payInfoService,IYuEBaoService yuebaoService) {
		super();
		this.username = username;
		this.parseService = parseService;
		this.userService = userService;
		this.orderService = orderService;
		this.orderItemService = orderItemService;
		this.currentUser = currentUser;
		this.payInfoService =payInfoService ;
		this.yuebaoService = yuebaoService;
	}
	private String phone;
	private IDianXinTelService dianXinTelService;
	private IDianXinDetailService dianXinDetailService; 
	/**
	 * 北京电信 type值201
	 * @param phone
	 */
	public Control(String phone, IUserService userService,
			 IWarningService warningService,String currentUser,IDianXinTelService dianXinTelService,IDianXinDetailService dianXinDetailService) {
		super();
		this.phone = phone;
		this.userService = userService;
		this.currentUser = currentUser;
		this.warningService =warningService ;
		this.dianXinTelService = dianXinTelService;
		this.dianXinDetailService = dianXinDetailService;
	}
	/**
	 * 联通  type值 101
	 */
	private IUnicomTelService unicomTelService;
	private IUnicomDetailService unicomDetailService;
	public Control(Login login, IUserService userService,IUnicomTelService unicomTelService,IUnicomDetailService unicomDetailService,
			 IWarningService warningService,String currentUser) {
		super();
		this.login = login;
		this.userService = userService;
		this.currentUser = currentUser;
		this.warningService =warningService ;
		this.unicomDetailService = unicomDetailService;
		this.unicomTelService = unicomTelService;
	}
	 /***
	  * 江西移动 type=301
	  */
	 private IMobileDetailService mobileDetailService;
	 private IMobileTelService mobileTelService;
	 public Control(String phone, IUserService userService,
			 IWarningService warningService,String currentUser,IMobileDetailService mobileDetailService,IMobileTelService mobileTelService) {
		super();
		this.phone = phone;
		this.userService = userService;
		this.currentUser = currentUser;
		this.warningService =warningService ;
		this.mobileDetailService = mobileDetailService;
		this.mobileTelService = mobileTelService;
	}
	 
	 /***
		 * 湖北电信
		 * @param username
		 * @param password
		 */
		public Control(Login login, IWarningService warningService,String currentUser,IUserService userService,IDianXinTelService dianXinTelService,IDianXinDetailService dianXinDetailService) {
			super();
			this.login = login;
			this.currentUser = currentUser;
			this.userService = userService;
			this.warningService =warningService ;
			this.dianXinTelService = dianXinTelService;
			this.dianXinDetailService = dianXinDetailService;
		}

	 public Control(String phone,String password, IUserService userService,
			 IWarningService warningService,String currentUser,IMobileDetailService mobileDetailService,IMobileTelService mobileTelService) {
		super();
		this.phone = phone;
		this.password = password;
		this.userService = userService;
		this.currentUser = currentUser;
		this.warningService =warningService ;
		this.mobileDetailService = mobileDetailService;
		this.mobileTelService = mobileTelService;
	}
	
	 
	 /**
	  * 北京移动 type = 303
	  * @param telno
	  * @param password
	  */
	 /**
	  * 新疆移动 type = 398
	  * @param telno
	  * @param password
	  */
	 public Control(String phone, IUserService userService,
			 IWarningService warningService,String currentUser,IMobileDetailService mobileDetailService,IMobileTelService mobileTelService,String password) {
		 super();
		 this.phone = phone;
		 this.password = password;
		
		 this.userService = userService;
		 this.currentUser = currentUser;
		 this.warningService =warningService ;
		 this.mobileDetailService = mobileDetailService;
		 this.mobileTelService = mobileTelService;
	 }
	 
	 

	 
	 
	private ISheBaoService shebaoService;
		/**
		 * 上海社保 type值401
		 * */
	public Control(String username, String password,
				IUserService userService, ISheBaoService shebaoService,IWarningService warningService,IParseService parseService,String currentUser) {
			super();
			this.username = username;
			this.password = password;
			this.userService = userService;
			this.shebaoService = shebaoService;
			this.warningService = warningService;
			this.currentUser = currentUser;
			this.parseService = parseService;
		}
	
		
	/**
	 * 学信网 type值501
	 * */
	public Control(String username,IUserService userService,
		String currentUser) {
		super();
		this.username = username;
		this.userService = userService;
		this.currentUser = currentUser;
	}
	
	
	
	private String chaxunma;
	private IZhengxinSummaryService zxsummaryService;
	private IZhengxinDetailService zxDetailService;
	
	/**
	 * 征信网 type值502
	 * 
	 * */
	public Control(String userName,String content,
			IUserService userService, IZhengxinSummaryService zxsummaryService, IZhengxinDetailService zxDetailService, IWarningService warningService, String currentUser,IParseService parseService) {
		super();
		this.userService = userService;
		this.currentUser = currentUser;
		this.warningService =warningService ;
		this.username = userName;
		this.content = content;
		this.zxsummaryService = zxsummaryService;
		this.zxDetailService = zxDetailService;
		this.parseService = parseService;
	}
	
	private ICMBTransferBillService cmbService;
	private ICBCCheckBillService icbcCheckBillService;
	private ICBCCheckBillItemService icbcCheckBillItemService;
	private String commonMailBrand;
	
	/**
	 * 邮箱type值601
	 * 
	 * **/
	public Control(String username, String password,String commonMailBrand,
			IUserService userService,ICMBTransferBillService cmbService, ICBCCheckBillService icbcCheckBillService,ICBCCheckBillItemService icbcCheckBillItemService,
			IWarningService warningService,String currentUser) {
		super();
		this.username = username;
		this.password = password;
		this.userService = userService;
		this.currentUser = currentUser;
		this.warningService =warningService ;
		this.cmbService = cmbService;
		this.icbcCheckBillService = icbcCheckBillService;
		this.icbcCheckBillItemService = icbcCheckBillItemService;
		this.commonMailBrand = commonMailBrand;
	}

	/***
	 * 浙江电信
	 * @param username
	 * @param password
	 */
	public Control(String phone, IUserService userService,
			 IWarningService warningService,String currentUser,IDianXinTelService dianXinTelService,IDianXinDetailService dianXinDetailService,String smscode) {
		super();
		this.phone = phone;
		this.userService = userService;
		this.currentUser = currentUser;
		this.warningService =warningService ;
		this.dianXinTelService = dianXinTelService;
		this.dianXinDetailService = dianXinDetailService;
		this.smscode=smscode;
	}
	
	/***
	 * 河南移动
	 * @param username
	 * @param password
	 */
	public Control(Login login,
			IWarningService warningService,String currentUser,IUserService userService,
			 IMobileDetailService mobileDetailService,IMobileTelService mobileTelService) {
		super();
		this.login = login;
		this.userService = userService;
		this.currentUser = currentUser;
		this.warningService =warningService ;
		this.mobileDetailService = mobileDetailService;
		this.mobileTelService = mobileTelService;
	}
	

	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		String key = null;
		Map<String,Object> redismap = null;
		switch (type) {
		case 1:
//			JD jd = new JD(new Login(username,password),warningService,currentUser);
//			jd.parseBegin(userService, orderService,  orderItemService, parseService);
			break;
		case 2:
//			TaoBaoUtils  taoBaoUtils = new TaoBaoUtils();
//			key = ConstantNum.getClientMapKey(currentUser+username, ConstantNum.ds_taobao);
//			redismap = RedisClient.getRedisMapThread(key);
//			taoBaoUtils.login(redismap, currentUser,username,parseService,userService,orderService,orderItemService,payInfoService,yuebaoService);
			break;
		case 101:
//			SHLiantong lt = new SHLiantong(login, warningService, currentUser, unicomTelService, unicomDetailService, userService);
//			lt.parseBegin();
			break;
		case 201:
//			HtmlParse.parseBJDianxinHTML(phone, userService,  warningService,  currentUser,dianXinTelService,dianXinDetailService);
			break;
		case 203:
//			DefaultHttpClient httpclient1 = DxConstant.sh_dxcloseClientMap1.get(currentUser);
//			SHDianXin dx=new SHDianXin();
//			dx.parseList(httpclient1, phone, "null", currentUser,userService,dianXinDetailService, warningService);
			break;
		case 301:
//			HtmlParse.parseJXYidongHTML(phone, userService, warningService, currentUser, mobileDetailService, mobileTelService);
			break;
		/*case 302:
			key = ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_sh_yidong);
			redismap = RedisClient.getRedisMapThread(key);
			SHYidong yidong = new SHYidong();
			yidong.parseBegin( redismap,phone,  
					userService,mobileTelService,mobileDetailService,currentUser,warningService);
			break;*/
		case 303:
//			HtmlParse.parseBJYidongHTML(phone,  userService,
//					  warningService, currentUser, mobileDetailService, mobileTelService, password);
			break;
		case 305:
			//HtmlParse.parseJSYidongHTML(phone, dongtaiJsYidongPassword, password, authCode2, userService, warningService, currentUser, mobileDetailService, mobileTelService, password);
			break;
			
		case 307:
//			HtmlParse.parseJSYidongHTML(   phone,  currentUser  , password,  userService,  warningService,  mobileDetailService,  mobileTelService);
			break; 
			
		case 401:
//			ShShebao sh = new ShShebao(new Login(username,password),warningService, currentUser);
//			sh.setShebaoService(shebaoService).setParseService(parseService).setUserService(userService);
//			sh.parseBegin1();
			break;
			
		case 402:

//			ShenZhenShebao szsh = new ShenZhenShebao(new Login(username,password), currentUser);
//			szsh.setShebaoService(shebaoService).setParseService(parseService).setUserService(userService).setWarningService(warningService);
//			szsh.parseBegin1();
			break;
			
		case 501:
//			key = ConstantNum.getClientMapKey(currentUser+username, ConstantNum.other_xuexin);
//			redismap = RedisClient.getRedisMapThread(key);
//			XueXin xx=new XueXin();
//			xx.parse(redismap,username,currentUser,userService);
			break;
		case 502:		
			Zhengxin zx=new Zhengxin(new Login(username,password),currentUser);		
			zx.parseBegin( username, content,
					userService, zxsummaryService,zxDetailService, warningService,currentUser,parseService);
			break;	
			
		case 601:		
			CommonMail cm=new CommonMail();
			try {
				cm.parseBegin( username,  password,commonMailBrand,
						userService,cmbService, icbcCheckBillService,icbcCheckBillItemService, warningService,currentUser);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//logger.info("第142行捕获异常：",e);		
				e.printStackTrace();
			}
			break;
		/*case 204:
			HtmlParse.parseFJDianxinHTML(phone, userService,  warningService,  currentUser,dianXinTelService,dianXinDetailService);
			break;*/
		case 304:
			HtmlParse.parseFJYingDongHTML(phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
			break;
		/*case 306:
			HtmlParse.parseZJYingDongHTML(phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
			break;*/
		case 205:
//			HtmlParse.parseZJDianxinHTML(phone, userService,  warningService,  currentUser, dianXinTelService,
//					 dianXinDetailService,smscode);
			break;	
		/*case 308:
			HtmlParse.parseGDYiDongHTML(phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
			break;		*/
		/*case 309:
//			HtmlParse.parseTJYiDongHTML(phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
			break;	*/
		case 207:
//			HtmlParse.parseTJDianxinHTML(phone, userService,  warningService,  currentUser,dianXinTelService,dianXinDetailService);
			break;
		/*case 208:
			HtmlParse.parseGSDianxinHTML(phone, userService,  warningService,  currentUser,dianXinTelService,dianXinDetailService);
			break;*/
		/*case 209:
			HtmlParse.parseSCDianxinHTML(phone, userService,  warningService,  currentUser,dianXinTelService,dianXinDetailService);
			break;*/
		/*case 210:
//			HtmlParse.parseSDDianxinHTML(phone, userService,  warningService,  currentUser,dianXinTelService,dianXinDetailService);
			break;*/
		/*case 310:
			
		/*	key = ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_sc_yidong);
			redismap = RedisClient.getRedisMapThread(key);
			SCYiDong yidong2 = new SCYiDong();
			yidong2.parseBegin(redismap,phone,  userService,mobileTelService,mobileDetailService,currentUser,warningService);*/
			//HtmlParse.parseSCYiDongHTML(redismap,phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
		/*	break;*/
	/*	case 202:
			key = ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_cq_dx);
			redismap = RedisClient.getRedisMapThread(key);
			CQDianXin cqdx = new CQDianXin();
			cqdx.parseBegin(redismap,phone,  userService,dianXinTelService,dianXinDetailService,currentUser,warningService);
			//HtmlParse.parseSCYiDongHTML(redismap,phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
			break;*/

		/*case 1084:
			HENYidong henYidong = new HENYidong(login,warningService, currentUser, userService,mobileTelService, mobileDetailService);
			henYidong.saveTelData();
			break;	*/
		/*case 1200:
			HuBDianxin hubDianXin = new HuBDianxin(login,warningService, currentUser, userService,dianXinTelService, dianXinDetailService);
			hubDianXin.saveTelData();
			break;*/
		/*case 1210:
			HUBYidong hubYidong = new HUBYidong(login, warningService,currentUser,userService,mobileDetailService, mobileTelService);
			hubYidong.saveTelData();
			break;*/
		/*case 1400:
			HLJYidong hljYidong = new HLJYidong(login, warningService,currentUser,userService,mobileDetailService, mobileTelService);
			hljYidong.saveTelData();
			break;*/
	/*	case 311:
			key = ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_sd_yidong);
			redismap = RedisClient.getRedisMapThread(key);
			SDYidong sdyd = new SDYidong();
			sdyd.parseBegin(redismap,phone,  userService,mobileTelService,mobileDetailService,currentUser,warningService);
			//HtmlParse.parseSCYiDongHTML(redismap,phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
			break;*/
		/*case 312:
			key = ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_cq_yidong);
			redismap = RedisClient.getRedisMapThread(key);
			CQYidong cqyd = new CQYidong();
			cqyd.parseBegin(redismap,phone,  userService,mobileTelService,mobileDetailService,currentUser,warningService);
			//HtmlParse.parseSCYiDongHTML(redismap,phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
			break;*/
//		case 313:
//			key = ConstantNum.getClientMapKey(currentUser+phone, ConstantNum.comm_nmg_yidong);
//			redismap = RedisClient.getRedisMapThread(key);
//			NMGYidong nmgyd = new NMGYidong();
//			nmgyd.parseBegin(redismap,phone,  userService,mobileTelService,mobileDetailService,currentUser,warningService);
//			//HtmlParse.parseSCYiDongHTML(redismap,phone, userService,  warningService,  currentUser,mobileDetailService,mobileTelService);
//			break;
		case 398:
			HtmlParse.parseXJYidongHTML(phone, password, userService,
					  warningService, currentUser, mobileDetailService, mobileTelService);
			break;	
		default:
			break;
		}
		
	}
	
}
