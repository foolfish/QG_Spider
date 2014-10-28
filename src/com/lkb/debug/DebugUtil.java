/**
 * 
 */
package com.lkb.debug;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lkb.robot.util.CookieStoreUtil;

/**
 * @author think
 * @date 2014-8-8
 */
public class DebugUtil {
	private static Logger logger = LoggerFactory.getLogger(DebugUtil.class);
	public static void printCookieData(CookieStore cs, String domain) {
		if (cs == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		int i = 0;
		for(Cookie c : cs.getCookies()) {
			if (domain == null || (StringUtils.isBlank(c.getDomain()) || domain.indexOf(c.getDomain()) >= 0) || c.getDomain().endsWith(domain)) {
				sb.append(c.getName()).append("=").append(c.getValue()).append(";");
				sb1.append(i).append("-[domain=").append(c.getDomain()).append("][path=").append(c.getPath()).append("];\r\n");
				i++;
			}
		}
		logger.info(sb.toString());
		logger.info(sb1.toString());
	}
	public static final String toCookieString(Cookie c) {
		final StringBuilder buffer = new StringBuilder();		 
		if (c != null) {
			buffer.append("[name: ");
			buffer.append(c.getName());
			buffer.append("]");		
			buffer.append("[domain: ");
			buffer.append(c.getDomain());
			buffer.append("]");
			buffer.append("[path: ");
			buffer.append(c.getPath());
			buffer.append("]");
		}
		 return buffer.toString();
	}
	public static void findMissing(CookieStore cs1, CookieStore cs2) {
		if (cs1 == null) {
			return;
		}
		Map<String, Cookie> map = new HashMap<String, Cookie>();
		for(Cookie c : cs1.getCookies()) {
			map.put(toCookieString(c), c);
		}
		StringBuilder sb = new StringBuilder();
		if (cs2 != null) {
			for(Cookie c : cs2.getCookies()) {
				String key = toCookieString(c);
				if (!map.containsKey(key)) {
					sb.append(c).append(";");
				} else {
					map.remove(key);
				}
				
			}
		}
		sb.append(map.values().toString());
		logger.info(sb.toString());
	}
	public static void findMissing(CookieStore cs, String cookies) {
		if (cs == null) {
			return;
		}
		Set<String> set = new HashSet<String>();
		for(Cookie c : cs.getCookies()) {
			set.add(c.getName());
		}
		StringBuilder sb = new StringBuilder();
		if (cookies != null) {
			String[] carr = cookies.split(";");
			for(String c : carr) {
				int i = c.indexOf("=");
				String name = c.substring(0, i).trim();
				if (!set.contains(name)) {
					sb.append(c).append(";");
				}
			}
		}
		logger.info(sb.toString());
	}
	public static void addToCookieStore(String domain, String cookies) {
		if (cookies != null) {
			CookieStore cs =  new BasicCookieStore();
			String[] carr = cookies.split(";");
			for(String c : carr) {
				int i = c.indexOf("=");
				String name = c.substring(0, i).trim();
				String value = c.substring(i + 1, c.length());
				BasicClientCookie c1 = new BasicClientCookie(name, value);
				if (domain != null) {
					c1.setDomain(domain);
				}
				cs.addCookie(c1);
			}
			CookieStoreUtil.addCookieStoreToContext(cs, 2);
		}		 
	}
}
