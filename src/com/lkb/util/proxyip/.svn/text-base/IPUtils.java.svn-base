package com.lkb.util.proxyip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Random;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;

import com.lkb.constant.ConstantProperty;
import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.CHeaderUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.entity.CHeader;
import com.lkb.util.httpclient.entity.Result;
import com.lkb.util.proxyip.entity.TaobaoIp;

/**
 * 设置代理,获取ip,判断返回状态及处理
 * @author fastw
 * @date 2014-7-4
 * @version 1.0
 */
public class IPUtils {
	public static final Logger log = Logger.getLogger(IPUtils.class);
	public static LinkedList<TaobaoIp> list = new LinkedList<TaobaoIp>();
	private static final String quantity = "10000";
//	private static String proxyip = InfoUtil.getInstance().getInfo(ConstantProperty.road, "proxyip");
	static{
		getIpList();
	}
	/**设置代理 从淘宝获取
	 * @param client
	 */
	public static  TaobaoIp setProxyId(HttpClient client){
		TaobaoIp tb = null;
//		if(proxyip.equals("true")){
			synchronized (list) {
				tb = getIp();
			}
			if(tb!=null){
				 HttpHost proxy = new HttpHost(tb.getIp(), tb.getPort());     //无效
			        //对HttpClient对象设置代理     
		        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,     
		                proxy);   
			}else{
				CUtil.removePROXY(client);
			}
			
//		}
		return tb;
	}
	/**移除过期ip
	 * @param tb
	 */
	public static void remove(TaobaoIp tb){
		list.remove(tb.getX());
	}
	/**
	 * 移除ip,并且取消代理
	 * @param tb
	 * @param client
	 */
	public static void remove(TaobaoIp tb,HttpClient client){
		list.remove(tb.getX());
		CUtil.removePROXY(client);
	}
//	/**
//	 * 不允许存放代理的参数,但是有可能涉及到ip被封
//	 * @param result
//	 * @return
//	 */
//	public static boolean isContinue(Result result){
//		return isContinue(result, null, null);
//	}
//	/**
//	 * 有代理
//	 * @param result 公用result对象
//	 * @param tb ip对象
//	 * @param client  httpclient客户端
//	 * @return
//	 */
//	public static boolean isContinue(Result result,TaobaoIp tb,HttpClient client){
//		boolean b = false;
//		switch (result.getCode()) {
//		case Result.A_Ip_Seal:
//			System.out.println("ip被封:"+result.getText());
//			break;
//		case Result.A_IP_Proxy:
//			remove(tb,client);
//			b = true;
//			break;
//		case Result.A_Error:
//			System.out.println("错误信息:"+result.getText());
//			break;
//		default:
//			b = true;
//			break;
//		}
//		if(result.getText()!=null&&result.getText().contains("Error: The requested URL could not be checked")){
//			b = false;
//		}
//		return b;
//	}
	/**
	 * 是否代理出问题
	 * @param text
	 * @return
	 */
	public static boolean isContinueText(String text){
		if(text.contains("Error: The requested URL could not be checked")){
			return false;
		}
		return true;
	}
	/**获取一个ip
	 * @return
	 */
	public static TaobaoIp getIp(){
		TaobaoIp tip = null;
		if(list!=null){
			if(list.size()==0){
				getIpList();
				if(list.size()==0){
					return tip;
				}
			}
			tip = list.get(new Random().nextInt(list.size()));
			
			return  tip;
		}
		return tip;
	}
	public static LinkedList<TaobaoIp> getIpList() {
		String region = "北京,天津,河北,河南,黑龙江,吉林,辽宁,内蒙古,青海,新疆,甘肃,宁夏,贵州,广西,广东,福建,陕西,山西,山东,江苏,浙江,上海,重庆,安徽,江西,湖北,湖南,海南,四川,云南,湖南,湖北";
		StringBuffer sb = new StringBuffer(
				"http://www.56pu.com/api?orderId=724421933062686&quantity=");
		sb.append(quantity).append("&line=all&region=");
		try {
			sb.append(URLEncoder.encode(region,"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		sb.append("&regionEx=&beginWith=&ports=&vport=&speed=500&anonymity=3&scheme=1&duplicate=3&sarea=");
		BufferedReader br = null;
		try {
		URLConnection httpurlconnection = CUtil.openURL(sb.toString()) ;
		String sts[] = null;
			if(httpurlconnection!=null){
					br = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream(),"utf-8"));
					String temp = null;
					int i = 0;
					while ((temp = br.readLine()) != null) {
						System.out.println(temp);
						sts = temp.split(":");
						list.add(new TaobaoIp(sts[0],Integer.parseInt(sts[1]),i));
						i++;
					}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取的ip解析失败", e);
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		CHeader h = new CHeader();
//		HttpResponse response = CUtil.getHttpGet(sb.toString(), h, (DefaultHttpClient)client);
//		BufferedReader br = null;
//		String sts[] = null;
//		try{
//			if(response!=null){
//				br = new BufferedReader(new InputStreamReader( response.getEntity().getContent(),"utf-8"));
//				String temp = null;
//				int i = 0;
//				while ((temp = br.readLine()) != null) {
//					System.out.println(temp);
//					sts = temp.split(":");
//					list.add(new TaobaoIp(sts[0],Integer.parseInt(sts[1]),i));
//					i++;
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			log.error("获取的ip解析失败", e);
//			//次数加上监控
//		}finally{
//			try {
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return list;
	}
	/**
	 * 默认deaultHttpClient
	 * 检查ip是否失效 </br>
	 * Result result = new Result();
	 * if(!result.isError()){			</br>
	 * 		remove(TaobaoId)对象
	 * }
	 * @param client
	 * @return
	 */
	public static Result checkip(DefaultHttpClient client) {
		Result result = new Result();
		HttpGet	get = CHeaderUtil.getHttpGet("http://20140507.ip138.com/ic.asp", new CHeader());
		HttpResponse  response = null;
		try {
			response = client.execute(get);
		} catch(ConnectException e){
			e.printStackTrace();
			System.out.println("地址失效");
			result.setCode(Result.A_Error);
		}catch (IOException e1) {
			e1.printStackTrace();
		}
		if(response!=null){
			result.setText(ParseResponse.parse(response));
		}
		return result;
	}
	
	/**
	 * 默认closeHttpClient
	 * 检查ip是否失效 </br>
	 * Result result = new Result();
	 * if(!result.isError()){			</br>
	 * 		remove(TaobaoId)对象
	 * }
	 * @param client
	 * @return
	 */
	public static Result checkip(HttpClient client) {
		Result result = new Result();
		HttpGet	get = CHeaderUtil.getHttpGet("http://20140507.ip138.com/ic.asp", new CHeader());
		HttpResponse  response = null;
		try {
			response = client.execute(get);
		} catch(ConnectException e){
			e.printStackTrace();
			System.out.println("地址失效");
			result.setCode(Result.A_Error);
		}catch (IOException e1) {
			e1.printStackTrace();
		}
		if(response!=null){
			result.setText(ParseResponse.parse(response));
		}
		return result;
	}
	
	
	
	// public static String getipURL =
	// "http://www.56pu.com/api?orderId=712102669193007&quantity=1&line=tel&region=%E4%B8%8A%E6%B5%B7&regionEx=&beginWith=&ports=&vport=&speed=&anonymity=3&scheme=&duplicate=3&sarea=";
 	private static String getProxyIP() {
		DefaultHttpClient client = new DefaultHttpClient();
		String getipURL = "http://www.56pu.com/api?orderId=724421933062686&quantity=200&line=tel&region="+URLEncoder.encode("河北")+"&regionEx=&beginWith=&ports=&vport=&speed=&anonymity=3&scheme=&duplicate=3&sarea=";
		CHeader h = new CHeader();
		HttpResponse response = CUtil.getHttpGet(getipURL, h, client);
		String text = null;
		if(response!=null){
			text = ParseResponse.parse(response, "utf-8");
		}
		String ss[] = text.split(":");
		// System.getProperties().setProperty("proxySet", "true");
		// //如果不设置，只要代理IP和代理端口正确,此项不设置也可以
//		 System.getProperties().setProperty("http.proxyHost",
//		 "27.129.195.113");
//		 System.getProperties().setProperty("http.proxyPort", "80");
		 HttpHost proxy = new HttpHost("116.228.55.217", 80);     //https
	       //对HttpClient对象设置代理     
	       client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,     
	               proxy);    
//			System.getProperties().setProperty("http.proxyHost", ss[0]);
//			System.getProperties().setProperty("http.proxyPort", ss[1]);
		Result result = checkip(client); // 判断代理是否设置成功
		if(result.getCode()==Result.A_Error){
			System.out.println("代理失败!.....");
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		//--------------
//		getProxyIP();
		DefaultHttpClient client = CUtil.init();
//		 HttpHost proxy = new HttpHost("116.228.55.217", 80);     //https
		 HttpHost proxy = new HttpHost("183.207.232.193", 8080);     //https
	       //对HttpClient对象设置代理     
	       client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,     
	               proxy);    
	       client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,30000); 
	       client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,30000);
	       Result result = checkip(client); // 判断代理是否设置成功
			if(result.getCode()==Result.A_Error){
				System.out.println("代理失败!.....");
			}else{
				System.out.println(result.getText());
			}
//		System.out.println(getIp(ConstantRegion.r_beijing));
	}
}
