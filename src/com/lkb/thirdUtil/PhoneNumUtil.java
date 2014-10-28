package com.lkb.thirdUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.lkb.bean.PhoneNum;
import com.lkb.service.IPhoneNumService;

public class PhoneNumUtil {
	private static Logger logger = Logger.getLogger(PhoneNumUtil.class);
	
//	public static void main(String[] args){
//		getCityNameByTel_taobao("18701683762");
//	}
	
	public void saveLog(IPhoneNumService phoneNumService) {
		String filePathAndName = "D:/2/data.txt";
		try {
			File f = new File(filePathAndName);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(f), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
					String[] strs = line.split("-");
					if(strs!=null && strs.length>2){
						String phone= strs[0];
						String province= strs[1];
						String city= strs[2];
						PhoneNum phoneNum = phoneNumService.findById(phone);
						if(phoneNum==null){
							 phoneNum = new PhoneNum();
							 phoneNum.setCity(city);
							 phoneNum.setProvince(province);
							 phoneNum.setPhone(phone);
							 phoneNumService.savePhoneNum(phoneNum);	 
						}
						
					}
				
				}
				read.close();
			}
		} catch (Exception e) {
			logger.info(e.getStackTrace());
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
	}
	

//    public static String getCityNameByTel_taobao(String tel) {  
//        try {  
//        	CloseableHttpClient httpclient = HttpClients.createDefault();
//        	String _str = getText(httpclient,"http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="  
//                    + tel);
//
//        	System.out.println(_str);
//        
//            JSONObject jo = new JSONObject(_str.substring(_str.indexOf("{"))[1]  
//                    );  
////            String province = jo.get("province") == null ? "" : URLDecoder  
////                    .decode(jo.get("province").toString());  
////            return province.equals("") ? null :province;  
//        } catch (Exception e) {  
//            return null;  
//        }  
//    }  
  
/** 
     * 根据手机号查询所在地,只返回Province 
     * showji.com数据 
     * @param tel 
     * @return 
     */  
//    @SuppressWarnings("deprecation")  
//    public static String getCityNameByTel_shouji(String tel) {  
//        try {  
//            HttpClient client = new HttpClient();  
//            HttpMethod method = new GetMethod();  
//            method.setPath("http://api.showji.com/Locating/20080808.aspx?m="  
//                    + tel + "&output=json&callback=querycallback" + tel);  
//            client.executeMethod(method);  
//            BufferedReader in = new BufferedReader(new InputStreamReader(method  
//                    .getResponseBodyAsStream(), "UTF-8"));  
//            StringBuilder _str = new StringBuilder();  
//            String inputLine = null;  
//            while ((inputLine = in.readLine()) != null) {  
//                _str.append(inputLine).append("\n");  
//            }  
//            method.releaseConnection();  
//            String js = _str.toString();  
//            String js2 = js.substring(14 + tel.length(), js.length() - 3);  
//            JSONObject jo = JSONObject.fromObject(js2);  
//            String province = jo.get("Province") == null ? "" : URLDecoder  
//                    .decode(jo.get("Province").toString());  
////          String city = jo.get("City") == null ? "" : URLDecoder.decode(jo   
////                  .get("City").toString());   
////          return (province.equals("") || province.equals(city)) ? city   
////                  : province + " " + city;   
//            return province.equals("") ? null :province;  
//        } catch (Exception e) {  
//            return null;  
//        }  
//    }  

    private static String getText(CloseableHttpClient httpclient,String redirectLocation) {
		HttpGet httpget = new HttpGet(redirectLocation);
		httpget.addHeader("Accept-Charset", "gb2312,utf-8");  
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}

}
