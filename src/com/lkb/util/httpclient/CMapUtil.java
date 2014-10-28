package com.lkb.util.httpclient;

import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.lkb.util.httpclient.constant.ConstantHC;
import com.lkb.util.httpclient.entity.CHeader;

/**
 * 此类封装一些通用的get请求和部分的post,基于CUtil类和ParseResponse基础之上
 * @author fastw
 */
public class CMapUtil {
	/**无重定向
	 * @param map  包含httpclient
	 * @param path 访问地址
	 * @param host	主机地址
	 * @param Accept	text/html, application/xhtml+xml, *\/\* 默认
	 * @return	解析后结果
	 */
	public static String getHttpGet(Map<String,Object> map,String path,String host,String Accept){
		CHeader h = new CHeader(Accept,null,null,host);
		HttpResponse response = CUtil.getHttpGet(path, h, ConstantHC.getClient(map));
		if(response!=null){
			return ParseResponse.parse(response);
		}
		return null;
	}
	/**无重定向
	 * @param map  包含httpclient
	 * @param path 访问地址
	 * @param host	主机地址
	 * @param Accept	text/html, application/xhtml+xml, *\/\* 默认
	 * @return	解析后结果
	 */
	public static String getHttpGet(Map<String,Object> map,String path,String host){
		return getHttpGet(map, path, host,CHeaderUtil.Accept_);
	}
	/**无重定向
	 * @param map  包含httpclient
	 * @param path 访问地址
	 * @param host	主机地址
	 * @param Accept	
	 * @return	空执行
	 */
	public static boolean getHttpGetNo(Map<String,Object> map,String path,String host,String Accept){
		CHeader h = new CHeader(Accept,null,null,host);
		HttpResponse response = CUtil.getHttpGet(path, h, ConstantHC.getClient(map));
		if(response!=null){
			 return  ParseResponse.closeResponse(response);
		}
		return false;
	}
	/**无重定向
	 * @param map  包含httpclient
	 * @param path 访问地址
	 * @param host	主机地址
	 * @param Accept	text/html, application/xhtml+xml, *\/\* 默认
	 * @return	空执行
	 */
	public static boolean getHttpGetNo(Map<String,Object> map,String path,String host){
		return getHttpGetNo(map, path, host,CHeaderUtil.Accept_);
	}
	/**有重定向
	 * @param map  包含httpclient
	 * @param path 访问地址
	 * @param host	主机地址
	 * @param Accept	text/html, application/xhtml+xml, *\/\* 默认
	 * @return	location 地址
	 */
	public static String getHttpGetLocation(Map<String,Object> map,String path,String host){
		return getHttpGetLocation(map, path, host,CHeaderUtil.Accept_);
	}
	/**有重定向
	 * @param map  包含httpclient
	 * @param path 访问地址
	 * @param host	主机地址
	 * @param Accept	
	 * @return	location 地址
	 */
	public static String getHttpGetLocation(Map<String,Object> map,String path,String host,String Accept){
		CHeader h = new CHeader(Accept,null,null,host);
		CUtil.setHandleRedirect(ConstantHC.getClient(map), false);
		HttpResponse response = CUtil.getHttpGet(path, h, ConstantHC.getClient(map));
		if(response!=null){
			Header header = response.getFirstHeader("Location");
			if(header!=null){
				String location = header.getValue();
				 ParseResponse.closeResponse(response);;
				return location;
			}
			 ParseResponse.closeResponse(response);
		}
		return null;
	}
}
