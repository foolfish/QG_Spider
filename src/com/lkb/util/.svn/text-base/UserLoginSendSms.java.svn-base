package com.lkb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.lkb.bean.PhoneNum;
import com.lkb.service.IPhoneNumService;
import com.lkb.thirdUtil.Phone_Base;
import com.lkb.util.redis.RedisMap;

/**
 * 创建时间：2014-9-21 上午11:36:35
 * 项目名称：LKB
 * @author Jerry Sun
 * @version 1.0
 * @since JDK 1.6
 * 文件名称：UserLoginSendSms.java
 * 类说明：新版用户注册时，使用手机验证码进行注册。建议3分钟内仅允许发送一次（请做前台校验），另建议前台页面做手机唯一性校验（即当前手机号是否已注册）。目前
 * 			验证码在缓存中的有效时限是默认的10分钟。
 */
public class UserLoginSendSms{
	private IPhoneNumService phoneNumService;
	protected Logger logger = Logger.getLogger(getClass());
	
	private static final String uname = "zs_liangke";	//短信下发接口用户名
	private static final String pwd = MD.MD5("147698");		//短信下发接口密码
	private static String url = "http://58.83.147.92:8080/qxt/smssenderv2";		//短信下发接口url
	
	private static final String sucMsg = "短信校验码发送成功";
	

	public UserLoginSendSms(IPhoneNumService phoneNumService) {
		this.phoneNumService = phoneNumService;
	}

	/**
	* <p>Title: sendSms</p>
	* <p>Description: 发送手机验证码</p>
	* @author Jerry Sun
	* @param phone
	*/
	public String sendSms(String phone){
		//1.校验手机号是否有效
		PhoneNum phoneNum = phoneNumService.findById(phone);
		if(phoneNum == null){
			Phone_Base phonebase = new Phone_Base(); 
			phoneNum=phonebase.getPhoneBelong(phone,phoneNumService); 
		}
		//2.生成短信验证码，并转码GBK(如果有汉字)
		int rsc = createRandomSmsCode();
		//3.短信下行服务接口调用
		StringBuffer params = new StringBuffer();
    	params.append("user=" + this.uname);
    	params.append("&password=" + this.pwd);
    	params.append("&tele=" + phone);
    	params.append("&msg=" + encodeMsg("尊敬的用户，您好!您本次的验证码为: " + String.valueOf(rsc) + "，有效期为10分钟。【量化派】"));
    	
    	String result = sendPost(url, params.toString());
		//4.判断返回值 成功：将验证码存入redis 失败：返回失败信息
    	if(result.indexOf("ok") != -1){
    		//putRedis(phone, String.valueOf(rsc));
    		logger.info(sucMsg);
    		return String.valueOf(rsc);
    	}else if(result.indexOf("fail") != -1){
    		logger.error(result.split(":")[1]);
    	}else if(result.indexOf("error") != -1){
    		logger.error(result.split(":")[1]);
    	}
    	return "send_message_fail";
	}
	
	/**
	* <p>Title: sendPost</p>
	* <p>Description: jdk自带的post请求</p>
	* @author Jerry Sun
	* @param url
	* @param param
	* @return
	*/
	private String sendPost(String url, String param) {
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
        	logger.error("发送 POST 请求出现异常", e);
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
            	logger.error("关闭流出错", ex);
            }
        }
        return result;
    }   
	
	/**
	* <p>Title: createRandomSmsCode</p>
	* <p>Description: 创建一个五位随机整数</p>
	* @author Jerry Sun
	* @return
	*/
	private int createRandomSmsCode(){
		int rsc;
		Random random = new Random();
		rsc = random.nextInt(89999) + 10000;
		return rsc;
	}
	
	/**
	* <p>Title: putRedis</p>
	* <p>Description: 将短信验证码放入redis中，redis的key组成为uname + pwd + url + phone，默认过期时间10分钟（防止手机信号不稳定导致的延迟信息接收）
	* redis中的map key 为 rndCode</p>
	* @author Jerry Sun
	* @param phone
	* @param rsc
	*/
	private void putRedis(String phone, String rsc){
		Map<String, Object> redismap = RedisMap.getInstance(uname + pwd + url + phone);
		redismap.put("rndCode", rsc);
	}
	
	/**
	* <p>Title: getRscFromRedis</p>
	* <p>Description: 通过手机号码获取10分钟内发送的短信验证码</p>
	* @author Jerry Sun
	* @param phone
	* @return
	*/
	public String getRscFromRedis(String phone){
		Map<String, Object> redismap = RedisMap.getInstance(uname + pwd + url + phone);
		String rsc = (String) redismap.get("rndCode");
		return rsc;
	}
	
	private String encodeMsg(String str){
		String encode = "";
		try {
			encode = URLEncoder.encode(str, "GBK");
		} catch (UnsupportedEncodingException e) {
			logger.error("中文转换GBK编码出错", e);
		}
		return encode;
	}
	
	public static void main(String[] args) {
		Map<String, Object> redismap = RedisMap.getInstance(uname + pwd + url + "19645119158");
		String rsc = (String) redismap.get("rndCode");
		System.out.println(rsc);
	}
}
