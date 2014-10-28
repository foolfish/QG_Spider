package com.lkb.controller;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lkb.bean.client.Login;
import com.lkb.thirdUtil.base.BaseInfo;
import com.lkb.util.redis.Redis;

@Controller
public class ImageAuthController {
	
	private static Logger logger = Logger.getLogger(ImageAuthController.class);
//	@RequestMapping(value = "/img_auth/{name}/{loginName}/{ssid}")
//	public void getAuthImg(HttpServletRequest request,HttpServletResponse response,@PathVariable("name") String name,@PathVariable("loginName") String loginName,@PathVariable("ssid") String userId) {
//		 Login login = new Login(loginName,null);
//		 String current = null;
//		if(userId!=null&&!"".equals(userId)){
//			login.setCurrentUser(userId);
//		}else{
//			Object currentUser  = request.getSession().getAttribute("currentUser");
//			current = currentUser==null?"":currentUser.toString();
//		}
//		BaseInfo baseInfo = null;
//		try{
//			name = name.substring(name.indexOf("thirdUtil"));
//			 Class<?> clazz = Class.forName("com.lkb."+name);
//			Constructor constructor = clazz.getConstructor(Login.class, String.class); //构造函数参数列表的class类型
//		    baseInfo = (BaseInfo) constructor.newInstance(login,current);
//		    if(baseInfo!=null){
//		    	setResponse(response,baseInfo.getAuthcode1());
//		    }
//		    baseInfo.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	@RequestMapping(value = "/img_auth/{key}")
	public void getAuthImg(HttpServletResponse response,@PathVariable("key") String key) {
			byte[] b = Redis.getByte(key);
			if(b!=null){
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				response.setContentType("image/jpeg;charset=UTF-8");
				ServletOutputStream sopt = null;
				if(b!=null){
					try {
						sopt = response.getOutputStream();
						sopt.write(b);
						sopt.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						if(sopt!=null){
							try {
								Redis.exists(key, 30);//30秒自动删除
								sopt.close();   
							} catch (IOException e) {
								e.printStackTrace();
							}  
						}
					}
					
				}
		}
				
	}
	
}
