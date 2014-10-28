package com.lkb.bean.client;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.lkb.thirdUtil.base.BaseInfoSocialInsurance;
import com.lkb.thirdUtil.shebao.ShShebao;
import com.lkb.thirdUtil.shebao.guangdong.ShenZhenShebao;


/**
 * 社保登陆属性
 * @author fastw
 * @date	2014-9-4 下午4:53:53
 */
public class SocialInsuranceProperty {
	public static Map<String,Class> map = new HashMap<String,Class>();
	static{
		//广东
		map.put("guangdong_shenzhen", ShenZhenShebao.class);
		//上海
		map.put("shanghai_shanghai", ShShebao.class);
	}
	
//	private SocialInsuranceInput sii;
//	public SocialInsuranceInput getSii() {
//		return sii;
//	}
//	public void setSii(SocialInsuranceInput sii) {
//		this.sii = sii;
//	}
	public static BaseInfoSocialInsurance getBaseInfoSocialInsurance(SocialInsuranceInput sii){
		Class<BaseInfoSocialInsurance> cls = map.get(sii.getProvince()+"_"+sii.getCity());
		BaseInfoSocialInsurance baseinfo =  null;
		if(cls!=null){
			try {
				baseinfo =  cls.newInstance();
			}catch (Exception e) {
				e.printStackTrace();
			} //构造函数参数列表的class类型
		}
		return baseinfo;
	}
}
// abstract class  SocialInsuranceTempBase{
//	 SocialInsuranceInput sii;
//	public SocialInsuranceTempBase setSii(SocialInsuranceInput sii) {
//		this.sii = sii;
//		return this;
//	}
//	abstract BaseInfoSocialInsurance getInstance();
// }
// class ShenZhenTemp extends SocialInsuranceTempBase{
//	BaseInfoSocialInsurance getInstance() {
//		return new ShenZhenShebao(sii);
//	}
// }