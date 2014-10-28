package com.lkb.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.lkb.bean.IdentifyLocation;
import com.lkb.service.IIdentifyLocationService;

public class SaveLoticaon {
	
//	public static void main(String[] args){
//		SaveLoticaon sl = new SaveLoticaon();
//		sl.saveLocation();
//	}
	public void saveLocation(IIdentifyLocationService identifyLocationService){
		 String fileContent = ""; 
		  try {  
		   File f = new File("d:/21.txt"); 
		   if(f.isFile()&&f.exists()){ 
		    InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8"); 
		    BufferedReader reader=new BufferedReader(read); 
		    String line; 
		    while ((line = reader.readLine()) != null) { 

		    	if(line!=null && line.length()>5){
		    		String[] strs = line.split(",");
		    		String id = strs[0];
		    		String location = strs[1];
		    		IdentifyLocation identifyLocation = identifyLocationService.findById(id);
		    		if(identifyLocation==null){
		    			 identifyLocation =  new IdentifyLocation();
		    			 identifyLocation.setCity(location);
		    			 identifyLocation.setId(id);
		    			 identifyLocationService.saveIdentifyLocation(identifyLocation);
		    		}
		    		
		    		
		    		
		    	}
		
		    }   
		    read.close(); 
		   } 
		  } catch (Exception e) { 
		   System.out.println("读取文件内容操作出错"); 
		   e.printStackTrace(); 
		  } 
	}
}
