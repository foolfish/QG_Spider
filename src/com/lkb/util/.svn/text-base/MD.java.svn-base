package com.lkb.util;

import java.security.MessageDigest;

public class MD {
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
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
			return null;
		}
	}
	
	/**
	 * 用户密码加密
	 * @param password
	 * @return
	 */
	public static String MD5_pwd(String password){
		return MD.MD5(ConstantUtil.PASSWORD_SRC+password);
	}
	
	public static void main(String[] args){
		//2d979608bf78796bb69c717941d78bee
		System.out.println(MD5("1").toLowerCase());
	}
}
