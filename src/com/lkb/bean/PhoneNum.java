package com.lkb.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 手机号码所在地查询
 * */
public class PhoneNum implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String phone;
	private String province ;
	private String city ;
	private String ptype ;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	/**
	 * 通过省名称和电信的类别返回标示字段
	 * @return
	 */
	/*public String getPutongUrl(){
		if(getProvince()!=null&&getPtype()!=null){
			return map.get(getProvince()+getPtype());
		}
		return null;
	}
	private static Map<String,String> map = null;
	static{
		map = new HashMap<String,String>();
		//北京
		map.put("北京移动", "putong_bj_yidong_url");
		map.put("北京联通", "putong_bj_liantong_url");
		map.put("北京电信", "putong_bj_dianxin_url");
		//河北
		map.put("河北移动", "putong_hb_yidong_url");
		map.put("河北联通", "putong_hb_liantong_url");
		map.put("河北电信", "putong_hb_dianxin_url");
		//天津
		map.put("天津移动", "putong_tj_yidong_url");
		map.put("天津联通", "putong_tj_liantong_url");
		map.put("天津电信", "putong_tj_dianxin_url");
		//上海
		map.put("上海移动", "putong_sh_yidong_url");
		map.put("上海联通", "putong_sh_liantong_url");
		map.put("上海电信", "putong_sh_dianxin_url");
		//黑龙江
		map.put("黑龙江移动", "putong_hlj_yidong_url");
		map.put("黑龙江联通", "putong_hlj_liantong_url");
		map.put("黑龙江电信", "putong_hlj_dianxin_url");
		//吉林
		map.put("吉林移动", "putong_jl_yidong_url");
		map.put("吉林联通", "putong_jl_liantong_url");
		map.put("吉林电信", "putong_jl_dianxin_url");
		//辽宁
		map.put("辽宁移动", "putong_ln_yidong_url");
		map.put("辽宁联通", "putong_ln_liantong_url");
		map.put("辽宁电信", "putong_ln_dianxin_url");
		//内蒙古
		map.put("内蒙古移动", "putong_nmg_yidong_url");
		map.put("内蒙古联通", "putong_nmg_liantong_url");
		map.put("内蒙古电信", "putong_nmg_dianxin_url");
		//新疆
		map.put("新疆移动", "putong_xj_yidong_url");
		map.put("新疆联通", "putong_xj_liantong_url");
		map.put("新疆电信", "putong_xj_dianxin_url");
		//青海
		map.put("青海移动", "putong_qh_yidong_url");
		map.put("青海联通", "putong_qh_liantong_url");
		map.put("青海电信", "putong_qh_dianxin_url");
		//甘肃
		map.put("甘肃移动", "putong_gs_yidong_url");
		map.put("甘肃联通", "putong_gs_liantong_url");
		map.put("甘肃电信", "putong_gs_dianxin_url");
		//宁夏
		map.put("宁夏移动", "putong_nx_yidong_url");
		map.put("宁夏联通", "putong_nx_liantong_url");
		map.put("宁夏电信", "putong_nx_dianxin_url");
		//山西
		map.put("山西移动", "putong_shx_yidong_url");
		map.put("山西联通", "putong_shx_liantong_url");
		map.put("山西电信", "putong_shx_dianxin_url");
		//山东
		map.put("山东移动", "putong_sd_yidong_url");
		map.put("山东联通", "putong_sd_liantong_url");
		map.put("山东电信", "putong_sd_dianxin_url");
		//江苏
		map.put("江苏移动", "putong_js_yidong_url");
		map.put("江苏联通", "putong_js_liantong_url");
		map.put("江苏电信", "putong_js_dianxin_url");
		//河南
		map.put("河南移动", "putong_hn_yidong_url");
		map.put("河南联通", "putong_hn_liantong_url");
		map.put("河南电信", "putong_hn_dianxin_url");
		//陕西
		map.put("陕西移动", "putong_sx_yidong_url");
		map.put("陕西联通", "putong_sx_liantong_url");
		map.put("陕西电信", "putong_sx_dianxin_url");
		//四川
		map.put("四川移动", "putong_sc_yidong_url");
		map.put("四川联通", "putong_sc_liantong_url");
		map.put("四川电信", "putong_sc_dianxin_url");
		//重庆
		map.put("重庆移动", "putong_cq_yidong_url");
		map.put("重庆联通", "putong_cq_liantong_url");
		map.put("重庆电信", "putong_cq_dianxin_url");
		//贵州
		map.put("贵州移动", "putong_gz_yidong_url");
		map.put("贵州联通", "putong_gz_liantong_url");
		map.put("贵州电信", "putong_gz_dianxin_url");
		//西藏
		map.put("西藏移动", "putong_xz_yidong_url");
		map.put("西藏联通", "putong_xz_liantong_url");
		map.put("西藏电信", "putong_xz_dianxin_url");
		//云南
		map.put("云南移动", "putong_yn_yidong_url");
		map.put("云南联通", "putong_yn_liantong_url");
		map.put("云南电信", "putong_yn_dianxin_url");
		//海南
		map.put("海南移动", "putong_han_yidong_url");
		map.put("海南联通", "putong_han_liantong_url");
		map.put("海南电信", "putong_hainan_dianxin_url");
		//广西
		map.put("广西移动", "putong_gx_yidong_url");
		map.put("广西联通", "putong_gx_liantong_url");
		map.put("广西电信", "putong_gx_dianxin_url");
		//广东
		map.put("广东移动", "putong_gd_yidong_url");
		map.put("广东联通", "putong_gd_liantong_url");
		map.put("广东电信", "putong_gd_dianxin_url");
		//福建
		map.put("福建移动", "putong_fj_yidong_url");
		map.put("福建联通", "putong_fj_liantong_url");
		map.put("福建电信", "putong_fj_dianxin_url");
		//浙江
		map.put("浙江移动", "putong_zj_yidong_url");
		map.put("浙江联通", "putong_zj_liantong_url");
		map.put("浙江电信", "putong_zj_dianxin_url");
		//湖南
		map.put("湖南移动", "putong_hun_yidong_url");
		map.put("湖南联通", "putong_hun_liantong_url");
		map.put("湖南电信", "putong_hun_dianxin_url");
		//湖北
		map.put("湖北移动", "putong_hub_yidong_url");
		map.put("湖北联通", "putong_hub_liantong_url");
		map.put("湖北电信", "putong_hub_dianxin_url");
		//江西
		map.put("江西移动", "putong_jx_yidong_url");
		map.put("江西联通", "putong_jx_liantong_url");
		map.put("江西电信", "putong_jx_dianxin_url");
		//安徽
		map.put("安徽移动", "putong_an_yidong_url");
		map.put("安徽联通", "putong_an_liantong_url");
		map.put("安徽电信", "putong_an_dianxin_url");
		//青海
		map.put("青海移动", "putong_qh_yidong_url");
		map.put("青海联通", "putong_qh_liantong_url");
		map.put("青海电信", "putong_qh_dianxin_url");
	}*/
}
