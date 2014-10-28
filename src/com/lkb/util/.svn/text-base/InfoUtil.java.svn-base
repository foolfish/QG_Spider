package com.lkb.util;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * 类说明 提取指定资源文件key=>value
 */
public class InfoUtil {
	private static InfoUtil instance = null;

	private InfoUtil() {

	}

	public synchronized static InfoUtil getInstance() {
		if (instance == null) {
			instance = new InfoUtil();
		}
		return instance;
	}
	
	
	public String getInfo(String name,String key){	
		ResourceBundle rb = ResourceBundle.getBundle(name);
		return rb.getString(key);
	}
}
