/**
 * 
 */
package com.lkb.location;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.CommonConstant;
import com.lkb.constant.ConstantProperty;
import com.lkb.constant.ErrorMsg;
import com.lkb.util.InfoUtil;
import com.lkb.util.SessionUtil;

/**
 * @author think
 * @date 2014-8-13
 */
public abstract class AbstractBase {
	protected Logger logger = Logger.getLogger(getClass());
	protected String imgPath2 = InfoUtil.getInstance().getInfo(ConstantProperty.road, "imgPath");
	
	private static final Map<String, AbstractBase> map = new HashMap<String, AbstractBase>();
	static{
		//北京
		map.put("北京", new BJ_Base());
		//河北
		map.put("河北", new HB_Base());
		//天津
		map.put("天津", new TJ_Base());
		//上海
		map.put("上海", new SH_Base());
		//黑龙江
		map.put("黑龙江", new HLJ_Base());
		//吉林
		map.put("吉林", new JL_Base());
		//辽宁
		map.put("辽宁", new LN_Base());
		//内蒙古
		map.put("内蒙古", new NMG_Base());
		//新疆
		map.put("新疆", new XJ_Base());
		//青海
		map.put("青海", new QH_Base());
		//甘肃
		map.put("甘肃", new GS_Base());
		//宁夏
		map.put("宁夏", new NX_Base());
		//山西
		map.put("山西", new SHX_Base());
		//山东
		map.put("山东", new SD_Base());
		//江苏
		map.put("江苏", new JS_Base());
		//河南
		map.put("河南", new HN_Base());
		//陕西
		map.put("陕西", new SX_Base());
		//四川
		map.put("四川", new SC_Base());
		//重庆
		map.put("重庆", new CQ_Base());
		//贵州
		map.put("贵州", new GZ_Base());
		//西藏
		map.put("西藏", new XZ_Base());
		//云南
		map.put("云南", new YN_Base());
		//海南
		map.put("海南", new HaiN_Base());
		//广西
		map.put("广西", new GX_Base());
		//广东
		map.put("广东", new GD_Base());
		//福建
		map.put("福建", new FJ_Base());
		//浙江
		map.put("浙江", new ZJ_Base());
		//湖南
		map.put("湖南", new HuN_Base());
		//湖北
		map.put("湖北", new HuB_Base());
		//江西
		map.put("江西", new JX_Base());
		//安徽
		map.put("安徽", new AH_Base());
		//青海
		map.put("青海", new QH_Base());
	}
	public static AbstractBase getBase(PhoneNum phoneNum) {
		AbstractBase ab = null;
		if (isLianTong(phoneNum)) {
			ab = map.get("上海");
		} else {
			ab = map.get(phoneNum.getProvince());
		}
		return ab;
	}
	public static boolean isYiDong(PhoneNum phoneNum) {
		return phoneNum != null && "移动".indexOf(phoneNum.getPtype()) >=0;
	}
	public static boolean isLianTong(PhoneNum phoneNum) {
		return phoneNum != null && "联通".indexOf(phoneNum.getPtype()) >=0;
	}
	public static boolean isDianXin(PhoneNum phoneNum) {
		return phoneNum != null && "电信".indexOf(phoneNum.getPtype()) >=0;
	}
	
	public abstract void goWhere(String ptype, String currentUser, Model model);
	public Result showValidateImg(PhoneNum phoneNum, FormData fd) {
		return new Result();
	}
	public Result showImg2(PhoneNum phoneNum, FormData fd) {
		return new Result();
	}
	public Result goLogin(PhoneNum phoneNum, FormData fd) {
		return new Result();
	}
	public Result sendSMS(PhoneNum phoneNum, FormData fd) {
		return new Result();
	}
	public Result requireService(PhoneNum phoneNum, FormData fd) {
		return new Result();
	}
	public static final String CURRENT_CONTEXT = "AbstractBase.applicationContext";
	protected Object getBean(Class cls) {
		ApplicationContext ac = (ApplicationContext) SessionUtil.getObject(CURRENT_CONTEXT);
		return ac.getBean(cls);
	}
	public void setImgResult(Result r, Map map) {
		final String url = (String) map.get("url");
		r.setStatus(1);
		r.setImgUrl(url);
		r.setSuccess(true);
		r.setResult(true);
	}
	public void setResult(Result r, Map map) {
		//状态码，1为正常
		String flag = map.get("flag").toString();
		r.setStatus(Integer.parseInt(flag));
		//错误信息
		flag = (String) map.get("errMsg");
		if (flag != null) {
			r.setErrorMsg(flag);
		}
		//成功处理
		r.setSuccess(true);
		//流程结束，没有下一步
		r.setResult(true);
	}
	/**
	 * 传统代码方式
	 */
	public void setResultOld(Result r, Map<String,Object> map) {
		//非第一步使用
		//状态码，1为正常
		Integer errorcode = (Integer)map.get("errorcode");
		if(errorcode!=null){
			r.setErrorcode(errorcode);
		}
		try{
			Integer status = (Integer)map.get(CommonConstant.status);
			if(status!=null){
				r.setStatus(status);
			}else{
				r.setStatus(0);
			}
		}catch(Exception e){
			r.setStatus(0);
		}
		//错误信息
		String msg = (String) map.get(CommonConstant.errorMsg);
		r.setErrorMsg(msg);
		//成功处理
		r.setSuccess(true);
		//流程结束，还有下一步
		r.setResult(true);
		//第一步使用,或者登陆返回验证码使用
		String imgUrl = (String) map.get("url");
		if(imgUrl!=null){
			r.setImgUrl(imgUrl);	
		}else{
			r.setImgUrl("none");	
		}
	}
	protected Result setImgCodeResult(FormData fd, Result r) {
		if (!StringUtil.isBlank(fd.getAuthCode())) {
			return r;
		}
		if (r == null) {
			r = new Result();
		}
		r.setSuccess(true);
		r.setErrorMsg("请输入图片验证码！");
		r.setErrorcode(ErrorMsg.authcodetEorror);
		return r;
	}
}
