package com.lkb.thirdUtil;

import com.lkb.util.FileUpload;
import com.lkb.util.InfoUtil;


/*
 * 图片上传到远程服务器
 * */
public class PicUpload {
	
	/*
	 * 上传
	 * */
	public void upload(String localFile){
		String islinux = InfoUtil.getInstance().getInfo("server/server",
				"islinux");	
		//System.out.println("islinux="+islinux);
		if(islinux.equals("true")){
			FileUpload fileUpload = new FileUpload();
			String remoteTargetDirectory = InfoUtil.getInstance().getInfo("server/server",
					"remoteRoad");	
					
			String[] strs = localFile.split("/");
			String time = strs[strs.length-2];
			remoteTargetDirectory = remoteTargetDirectory +"/"+time;
			System.out.println("localFile="+localFile);
			System.out.println("remoteTargetDirectory="+remoteTargetDirectory);
			fileUpload.getFile( remoteTargetDirectory,localFile);			
		}

		
	}
	
	public static void main(String[] args){
		String str = "E:/apache-tomcat-7.0.35/webapps/LKB/img/authcode/20140704/ddd.png";
		String[] strs = str.split("/");
		String time = strs[strs.length-2];
		System.out.println(time);

        
	}
	
}



