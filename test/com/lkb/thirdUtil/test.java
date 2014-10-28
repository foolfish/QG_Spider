package com.lkb.thirdUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import jxl.write.DateFormat;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;
import org.apache.http.client.protocol.HttpClientContext;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.web.util.HtmlUtils;

import com.lkb.bean.DianXinTel;
import com.lkb.hbase.BeanUtil;
import com.lkb.thirdUtil.base.BaseInfo;
import com.lkb.util.DateUtils;
import com.lkb.util.StringUtil;
import com.lkb.util.httpclient.ClientConnectionPool;
import com.lkb.util.httpclient.HttpRequest;

public class test extends BaseInfo {
	
	
	
	
	private test() {
		client = ClientConnectionPool.getInstance();
		context = HttpClientContext.create();
//		cutil = new HttpRequest(client,context);
	}


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		String s = "实时话费（截止日期：2014年10月23日） 套餐包月费 19.00元 话费总额 19.00元";
//		System.out.println(StringUtil.decodeUnicode("mpageName=feeService.VoiceQueryNew&service=direct%2F1%2Fpersonalinfo.CheckedSms%2F%24Form&sp=S1&Form1=bforgot&SMS_NUMBER=12312&bforgot=%C8%B7++%C8%CF&loginFlag=1&smsLeftNum=33"));
//		String l1 = "1565228009320141012201111";
//		String l2 = "1565228009320140912201111";
//		String l3 = "1565228009320141112201111";
//					 9223372036854775807
		
//		System.out.println(new StringBuilder(l1).reverse().toString());
//		System.out.println(new StringBuilder(l2).reverse().toString());
//		System.out.println(new StringBuilder(l3).reverse().toString());
		
//		byte [] rowkey = Bytes.add(MD5Hash.getMD5AsHex(Bytes.toBytes(l1)).substring(0, 8).getBytes(),Bytes.toBytes(l1));
//		System.out.println(rowkey);
		
		DianXinTel dxt = new DianXinTel();
		dxt.setBdthf(new BigDecimal(1));
		dxt.setcAllPay(new BigDecimal(22));
		dxt.setcName("测试数据");
		dxt.setcTime(Calendar.getInstance().getTime());
		Map<String, Object> map = BeanUtil.transBean2Map(dxt);
		JSONObject jso = new JSONObject(map);
		System.out.println(jso.toString());
		
	}

	
/*	private void sendMobileCode(){
		String[][] pairs = {{"user", "zs_liangke"}, {"password", pwdToMd5()}, {"tele", "15652280093"}, {"msg", encodeMsg()}};
		postUrl("http://58.83.147.92:8080/qxt/smssenderv2", null, pairs, new AbstractProcessorObserver(util, "") {
			@Override
			public void afterRequest(SimpleObject context) {
				String content = ContextUtil.getContent(context);
				System.out.println(content);
			}
		});
	}*/
	
	/**
     * post方式
     * @param param1
     * @param param2
     * @return
*/
    public void postHttp() {
    	String url = "http://58.83.147.92:8080/qxt/smssenderv2";
    	/*Map<String,String> param = new LinkedHashMap<String,String>();
    	param.put("user", "zs_liangke");
    	param.put("password", pwdToMd5());
    	param.put("tele", "15652280093");
    	param.put("msg", encodeMsg());
    	
    	param.put("type", "4");
    	param.put("msg", encodeMsg());
    	param.put("type", "4");
    	param.put("type", "4");
    	param.put("type", "4");
    	
    	String post = cutil.postURLForSendSms(url, null, param);
//    	String post = cutil.post(url, param);
    	System.out.println(post);*/
    	
    	String sr = sendPost(url, "user=zs_liangke&password=" + pwdToMd5() + "&tele=15652280093" + "&msg=" + encodeMsg());
    	System.out.println(sr);
    }
	
	private String pwdToMd5(){
		String s = "147698";
		 char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       
	        try {
	            byte[] btInput = s.getBytes();
	            // 获得MD5摘要算法的 MessageDigest 对象
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            // 使用指定的字节更新摘要
	            mdInst.update(btInput);
	            // 获得密文
	            byte[] md = mdInst.digest();
	            // 把密文转换成十六进制的字符串形式
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	
	private String encodeMsg(){
		String str = "验证码测试123345【量科邦】自测";
		String encode = "";
		try {
			encode = URLEncoder.encode(str, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encode;
	}
	
	
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	  
       
    
}
