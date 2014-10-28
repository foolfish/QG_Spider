//package com.lkb.util.taobao.util;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import com.lkb.constant.TaoBaoConstant;
//import com.lkb.util.httpclient.CUtil;
//import com.lkb.util.taobao.alipay.InfoPage;
//import com.lkb.util.taobao.yuebao.YuEBaoInfo;
//
//public class Test {
//	public static String taobao = "uc1=lltime=1403598703&cookie14=UoW3vKSDMMssPA%3D%3D&existShop=false&cookie16=URm48syIJ1yk0MX2J7mAAEhTuw%3D%3D&cookie21=UtASsssmfA%3D%3D&tag=7&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; v=0; cookie2=46e132c5e45be34f9961126ce98aa798; _tb_token_=muk1c23bjPn; _nk_=wangxuefei0310; existShop=MTQwMzYyMjU5Mw%3D%3D; sg=015; cookie1=BqAIRDePkWtJ%2F5rlsrmVkA4WQ4zn8xjwVU8R3t0BCcU%3D; unb=740874121; _l_g_=Ug%3D%3D; cookie17=VAXb13I8QeuR; t=e93f4fc531af18fd8e983fd432d8f994; uc3=nk2=FPjangOvqbRFtLppTNU%3D&id2=VAXb13I8QeuR&vt3=F8dATH1TTZFX2Fb6plE%3D&lg2=U%2BGCWk%2F75gdr5Q%3D%3D; lgc=wangxuefei0310; tracknick=wangxuefei0310; mt=ci=2_1&cyk=1_0; _cc_=UtASsssmfA%3D%3D; tg=0; cna=lZAqDB6pF3MCAXehuAvSYa4R; l=wangxuefei0310::1403626251530::11";
//	public static String alipay = "JSESSIONID=498820116F3714D5EBE6F6B71EE20AA1; spanner=ikhLiR4FppK00gFWTBiEqMGqq/a+peaP; ALIPAYJSESSIONID=RZ01txJy7OwDKdyXY0tdTXWIKt14TvauthRZ01GZ00; ctoken=xZk6OorXxCL7pRJe0f1Q5lhEsJwQUe; umt=; cna=lZAqDB6pF3MCAXehuAvSYa4R; mobileSendTime=-1; credibleMobileSendTime=-1; ctuMobileSendTime=-1; riskMobileBankSendTime=-1; riskMobileAccoutSendTime=-1; riskMobileCreditSendTime=-1; riskCredibleMobileSendTime=-1; riskOriginalAccountMobileSendTime=-1; LoginForm=alipay_login_auth; alipay=\"K1iSL16SV9fxBaFvv56DX1nUgy/e0geSSKzpPanfxeBWKmmQUpNJPrJp\"; CLUB_ALIPAY_COM=2088702043220974; iw.userid=\"K1iSL16SV9fxBaFvv56DXw==\"; ali_apache_tracktmp=\"uid=2088702043220974\"; session.cookieNameId=ALIPAYJSESSIONID; unicard1.vm=K1iSL1mlXo+WrxS0CsK6a1rXgq161UUeoHiAeasAAblp; iw.partner=\" \"; iw.nick=\" \"; havana_sid=2So9KAtove4wkm3ZXxAa0qA1; JSESSIONID=GZ00qaWk62fu9yfdSNX3a2y6jRBrM9personalGZ00"; 
//	public static void main(String[] args) {
//		Map map = new HashMap();
//		//初始化client
//		DefaultHttpClient client = CUtil.init();
//		map.put(TaoBaoConstant.k_client, client);
//		/**--------打开支付宝个人信息-----------**/
////		new InfoPage().openInfoPage(map);
//		/**交易记录*/
//		//new InfoPage().openAlipayJiaoyi(map);
//		/**一年前交易记录*/
////		new InfoPage().openAlipayJiaoyiYear(map);
////		new InfoPage().openAlipayJiaoyiYear(map,null,null);
//		/**余额宝收益*/
//		//new YuEBaoInfo().openShouyi(map);
//		/**余额宝转出**/
////		new YuEBaoInfo().openZhuanchu(map);
//		/**余额宝转入**/
////		new YuEBaoInfo().openZhuanru(map);
//		/**支付宝订单详情*/
//		new LoginPage().orderinfo(map,"http://trade.taobao.com/trade/detail/trade_item_detail.htm?spm=a1z09.2.9.18.gfJgDX&bizOrderId=684192988332141");
//	}
//
//}
