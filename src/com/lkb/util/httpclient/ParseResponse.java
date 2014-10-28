package com.lkb.util.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;


/**
 * httpResponse 内容解析
 * @author fastw
 *
 */
public class ParseResponse {
	public static Logger log = Logger.getLogger(ParseResponse.class);
	/**
	 * @param response HttpResponse响应
	 * @param charset 响应编码 默认GBK
	 * @return
	 */
	public static String parse(HttpResponse response,String charset){
		return parse(response, charset,false);
	}
	/**
	 * 理论上万能解析
	 * @param response HttpResponse响应
	 */
	public static String parse(HttpResponse response) {
		return parse(response, getContentCharset(response),getContentEncoding(response));
	}
	public static String getLocation(HttpResponse response){
		return response.getFirstHeader("Location").getValue();
	}
	/**
	 * @param response HttpResponse响应
	 * @param charset 响应编码 默认GBK
	 * @param isGzip 是否gzip 默认false
	 * @return
	 * @throws Exception 
	 */
	public static String parse(HttpResponse response,String charset,boolean isGzip){
		HttpEntity entity = response.getEntity();
		BufferedReader br = null;
		StringBuilder sb = null;
		String s = null;
		try {
			if(isGzip){
				try{
					br = new BufferedReader(new InputStreamReader(new GZIPInputStream(
							entity.getContent()), charset));
				}catch(IOException e){
					System.out.println("gzip"+e.getLocalizedMessage());
					log.info(e.getMessage());
					br = new BufferedReader(new InputStreamReader( entity.getContent(), charset));
				}
			}else{
				br = new BufferedReader(new InputStreamReader( entity.getContent(), charset));
			}
			
			sb = new StringBuilder();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			s = sb.toString();
			if(s.contains("Error: The requested URL could not be checked")){
				return null;
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			s = sb.toString();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
	
	/** *//**
	    *获取Response内容字符集
	    *
	    * @param response
	    * @return
	    */
	    public static String getContentCharset(HttpResponse response) {
	    	String strs = "GBK";
			Header header = response.getEntity().getContentType(); 
			if (header != null) {
				String s = header.getValue(); 
				if (matcher(s, "(charset)\\s?=\\s?(utf-?8)")) {
					strs = "utf-8";
				} else if (matcher(s, "(charset)\\s?=\\s?(gbk)")) {
					strs  = "gbk";
				} else if (matcher(s, "(charset)\\s?=\\s?(gb2312)")) {
					strs  = "gb2312";
				}
			}
			return strs;
	    }
	    public static boolean getContentEncoding(HttpResponse response){
	    	Header header = response.getEntity().getContentEncoding();
	    	boolean b = false;
			if (header != null) {
				String s = header.getValue(); 
				 if(s.toLowerCase().contains("gzip")){
					 b =true;
				}
			}
			return b;
	    }
	    /**
	     *正则匹配
	     * @param s
	     * @param pattern
	     * @return
	     */
	     public static boolean matcher(String s, String pattern) { 
	    	 Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE); 
	    	 Matcher matcher = p.matcher(s);
	    	 if (matcher.find()) {
	    		 return true;
		     } else {
		    	 return false;
		     }
	     }
	/**
	 *空关闭
	 **/
	public static boolean closeResponse(HttpResponse response){
		boolean b = false;
		try {
			response.getEntity().getContent().close();
			b = true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
	/**
	 * @param response urlconnection响应
	 * @param charset 响应编码 默认UTF-8
	 * @return
	 */
	public static String parse(URLConnection conn,String charset){
		return parse(conn, charset,false);
	}
	/**默认utf-8
	 * @param response urlconnection响应
	 */
	public static String parse(URLConnection conn) {
		return parse(conn, "UTF-8",false);
	}
	/**
	 * @param response urlconnection响应
	 * @param charset 响应编码 默认utf-8
	 * @param isGzip 是否gzip 默认false
	 * @return
	 * @throws Exception 
	 */
	public static String parse(URLConnection conn,String charset,boolean isGzip){
		BufferedReader br = null;
		StringBuilder sb = null;
		try {
			if(isGzip){
				br = new BufferedReader(new InputStreamReader(new GZIPInputStream(
						conn.getInputStream()), charset));
			}else{
				br = new BufferedReader(new InputStreamReader( conn.getInputStream(), charset));
			}
			
			sb = new StringBuilder();
			String temp = null;
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
