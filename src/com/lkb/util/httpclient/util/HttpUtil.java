package com.lkb.util.httpclient.util;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil{
	/**
	 * @param url 根据url 获取网站主页
	 * @return
	 */
	public static String getHomePage(String url)
	{
	    Pattern p = Pattern.compile("(http://|https://)?([^/|?]*)",Pattern.CASE_INSENSITIVE);
	    Matcher m = p.matcher(url);
	    String s = "";
	    for (int i = 0; i < 2; i++) {
	    	m.find();
	    	s = s+ m.group();
		}
	    return s;
	}
	public static String inputYanzhengma(){
		// 输出的文件流
		System.out.println("请输入验证码:");
		Scanner in = new Scanner(System.in);
		String name = in.nextLine();
	    return name;
	}
	public static boolean isMovedStatusCode(int code) {
		return code >= 300 && code < 400;
	}
}