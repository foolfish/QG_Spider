package com.lkb.constant;

/**所有网站的通用编号
 * redis键 基于httpclient
 * @author fastw
 * @date   2014-7-14 上午11:26:22
 */
public class ConstantNum {
	/**所有电商编码,基于cookie等的调用**/
	public static String ds_taobao = "TAOBAO";
	public static String ds_zhifubao = "ALIPAY";
	public static String ds_jd = "DIANSHANGJINGDONG";
	public static String other_xuexin = "XUEXIN";
	public static String other_zhengxin = "ZHANGXIN";
	
	public static String shebao_shanghai = "SHANGHAISHEBAO";
	public static String shebao_shenzhen = "SHENZHENGSHEBAO";
	
	/**所有通信编码**/
	//广东
	public static String comm_gd_yidong = "GUANGDONG_YIDONG";
	public static String comm_gd_lt = "GUANGDONG_LIANTONG";
	public static String comm_gd_dx = "GUANGDONG_DIANXIN";
	//北京
	public static String comm_bj_yidong = "BEIJING_YIDONG";
	public static String comm_bj_lt = "BEIJING_LIANTONG";
	public static String comm_bj_dx = "BEIJING_DIANXIN";
	//天津
	public static String comm_tj_yidong = "TIANJIAN_YIDONG";
	public static String comm_tj_lt = "TIANJIAN_LIANTONG";
	public static String comm_tj_dx = "TIANJIAN_DIANXIN";
	//上海
	public static String comm_sh_yidong = "SHANGHAI_YIDONG";
	public static String comm_sh_lt = "ZHONGGUOLIANTONG";
	public static String comm_sh_dx = "SHANGHAI_DIANXIN";
	//江苏
	public static String comm_js_yidong = "JIANGSU_YIDONG";
	public static String comm_js_lt = "JIANGSU_LIANTONG";
	public static String comm_js_dx = "JIANGSU_DIANXIN";
	//浙江
	public static String comm_zj_yidong = "ZHEJIANG_YIDONG";
	public static String comm_zj_lt = "ZHEJIANG_LIANTONG";
	public static String comm_zj_dx = "ZHEJIANG_DIANXIN";
	//福建
	public static String comm_fj_yidong = "FUJIAN_YIDONG";
	public static String comm_fj_lt = "FUJIAN_LIANTONG";
	public static String comm_fj_dx = "FUJIAN_DIANXIN";
	//四川
	public static String comm_sc_yidong = "SICHUAN_YIDONG";
	public static String comm_sc_dianxin = "SICHUAN_DIANXIN";
	// 重庆
	public static String comm_cq_yidong= "CHONGQING_YIDONG";
	public static String comm_cq_dx = "CHONGQING_DIANXIN";
	
	//山西
	public static String comm_shx_yidong="SHANXI_YIDONG";
	
	//河南
	public static String comm_hen_yidong = "HENAN_YIDONG";
	public static String comm_hen_dianxin = "HENAN_DIANXIN";
	
	//吉林
	public static String comm_jl_yidong = "JILIN_YIDONG";
	public static String comm_jl_dianxin = "JILIN_DIANXIN";
	
	//山东
	public static String comm_sd_yidong = "SHANDONG_YIDONG";
	public static String comm_sd_dianxin= "SHANDONGDIANXIN";
	
	//新疆
	public static String comm_xj_yidong = "XINJIANG_YIDONG";
	public static String comm_xj_dianxin = "XINJIANG_DIANXIN";
	
	//湖北
	public static String comm_hub_dianxin = "HUBEI_DIANXIN";
	public static String comm_hub_yidong = "HUBEI_YIDONG";
	//湖南
	public static String comm_hun_yidong = "HUNAN_YIDONG";
	
	//黑龙江
	public static String comm_hlj_yidong = "HEILONGJIANG_YIDONG";
	
	//广西
	public static String comm_gx_dianxi = "GUANGXI_DIANXIN";
	public static String comm_gx_yidong = "GUANGXI_YIDONG";
	//内蒙古
	public static String comm_nmg_yidong = "NEIMENGGU_YIDONG";
	public static String comm_nmg_dianxin = "NEIMENGGU_DIANXIN";
	
	
	//西藏
	public static String comm_xz_yidong = "ZIZANG_YIDONG";
	
	//河北
	public static String comm_heb_yidong = "HEBEI_YIDONG";
	
	//江西
	public static String comm_jx_yidong = "JIANGXI_YIDONG";
	
	//甘肃
	public static String comm_gs_dianxin = "GANSU_DIANXIN";
	public static String comm_gs_yidong = "GANSU_YIDONG";
	//安徽
	public static String comm_ah_yidong = "ANHUI_YIDONG";
	//宁夏
	public static String comm_nx_yidong = "NINGXIA_YIDONG";
	//云南
	public static String comm_yn_yidong = "YUNNAN_YIDONG";
	//海南
	public static String comm_hain_yidong = "HAINAN_YIDONG";
	//贵州
	public static String comm_gz_yidong = "GUIZHOU_YIDONG";
	//青海
	public static String comm_qh_yidong = "QINGHAI_YIDONG";
	
	/**
	 * 得到redismap的key字符串
	 * @param loginName
	 * @param constantnum
	 * @return
	 */
	public static String getClientMapKey(String loginName,String constantnum){
		return constantnum+loginName;
	}
	//------------------------通过key-------------------
	/**验证码的url地址*/
	public static String client_yanzmUrl = "yanzhengmaurl";
	/**淘宝验证码的url地址*/
	public static String client_taobaoUrl = "taobaourl";
	/**验证码的值地址*/
	public static String client_yanzmValue = "yanzhengmavalue";
	//taobaoip
	public static String client_taobaoip = "taobaoip";
	/**httpclient key*/
	public static String client = "k_client";
	/**cookie key*/
	public static String client_cookie = "k_cookie";
	/**通用url*/
	public static String client_url = "k_url";
	/**方法执行步骤确实定成功执行到哪一步,特殊情况适用键值对应整型,步骤自定义,
	 * 一般此参数作为输入账号后,初始化失败,然后在输入密码处,根据步骤判断在账户初始化时哪步错了
	 * 
	 * */
	public static String client_buzhou = "k_buzhou";
	/**当步骤的值等于100时确定redis是登陆成功的,所以无需重复登陆*/
	public static int client_buzhou_value = 100;
}
