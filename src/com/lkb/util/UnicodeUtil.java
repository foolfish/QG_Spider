package com.lkb.util;

import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

/** \u8bf7\u5237\u65b0\u9875\u9762\u540e\u91cd\u65b0\u63d0\u4ea4 格式转汉字
 * @author fastw
 * @date	2014-7-22 上午12:57:43
 */
public class UnicodeUtil {
	
	/**针对\\u 转码 16进制转汉字,支持标点符号
	 * @param string
	 * @return
	 */
	public static String UnicodeToChinese(String string) {
		StringBuffer sb = new StringBuffer();
		if (string.contains("\\u")) {
			string = string.replace("&nbsp;", "");
			int m = string.indexOf("\\u");
			String s5 = string.substring(0, m);
			String str = string.substring(m);
			str = str.replace("\\u", ",. ");
			String[] s2 = str.split(",. ");
			String s3 = "";
			String s4 = "";
			try {
				for (int i = 1; i < s2.length; i++) {
					s3 = s2[i].substring(0, 4);
					s4 = s2[i].substring(4);
					s2[i] = s3;
					sb.append((char) Integer.parseInt(s2[i].trim(), 16) + s4);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.insert(0, s5);
			System.out.println(sb.toString());
			return sb.toString();
		} else {
			return string;
		}
	}
	/**不带标点符号
	 * @param ascii
	 * @return
	 */
	public static String ascii2native(String ascii) {  
		  StringBuilder sb = null;
		try{
		    int n = ascii.length() / 6;  
		    sb = new StringBuilder(n);  
		    for (int i = 0, j = 2; i < n; i++, j += 6) {  
		        String code = ascii.substring(j, j + 4);  
		        char ch = (char) Integer.parseInt(code, 16);  
		        sb.append(ch);  
		    }  
		}catch(Exception e){
			return UnicodeToChinese(ascii);
		}
	    return sb.toString();  
	}  
	public static void main(String[] args) {
		System.out.println(UnicodeToChinese("\\u8bf7\\u5237\\u65b0\\u9875\\u9762\\u540e\\u91cd,\\u65b0\\u63d0\\u4ea4"));
	}
}