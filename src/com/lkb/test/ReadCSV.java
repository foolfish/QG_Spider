package com.lkb.test;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.IOException;

import java.util.StringTokenizer;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import com.lkb.bean.PhoneNum;
import com.lkb.service.IPhoneNumService;
import com.lkb.thirdUtil.Phone_Base;
import com.lkb.util.FileUtil;

public class ReadCSV {
	
	public static void write(IPhoneNumService ps){
		try {           

			File csv = new File("e:/1/11.xls"); // CSV
	        

			BufferedReader br = new BufferedReader(new FileReader(csv)); 

			String line = ""; 
			FileUtil futil = new FileUtil();
			StringBuffer sb = new StringBuffer("");
			int i=0;
			while ((line = br.readLine()) != null) { 
				;
			StringTokenizer st = new StringTokenizer(line, ","); 

			while (st.hasMoreTokens()) { 

				String phone = st.nextToken();
				if(phone.length()>7){
					String prephone = phone.substring(0,7);
					PhoneNum phoneNum = ps.findById(prephone);
					String content ="";
					if(phoneNum!=null){
						String province = phoneNum.getProvince();
						 content = phone+","+province;
						
					}else{
							i++;
							Phone_Base phonebase = new Phone_Base();
							phoneNum=phonebase.getPhoneBelong(phone,ps);
							if(phoneNum!=null){
								 content = phone+","+phoneNum.getProvince();
							}
							
							if(i>20){
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								i=0;
							}
						   
					}
					 sb.append(content+"\n");
					
					System.out.print(content+"\n");
				}
		
				

			} 
			
			futil.writeFile("e:/1/11.txt",  sb.toString());

			} 

			br.close(); 
	        

			} catch (FileNotFoundException e) { 
			e.printStackTrace(); 
			} catch (IOException e) { 

				e.printStackTrace();     

			}   

			}
	

		
}
