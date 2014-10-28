package com.lkb.util.report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/*
 * 组装成json数据
 * */
public class JsonUtils {
	public String zuzhuangDate(String date){
		String[] strs = date.split(",");
		String str2pre = "'[";
		String middle = "";
		String str2after = "]'";
		for(int i=0;i<strs.length;i++){
			middle+="\""+strs[i]+"\""+",";
		}
		middle = middle.substring(0, middle.length()-1);
		date = str2pre+middle+str2after;
		return  date;
		
	}
	
	public String zuzhuangNum(String num){
		String[] strs2 = num.split(",");
		String str2pre2 = "'[";
		String middle2 = "";
		String str2after2 = "]'";
		for(int i=0;i<strs2.length;i++){
			middle2+= strs2[i]+",";
		}
		middle2 = middle2.substring(0, middle2.length()-1);
		num = str2pre2+middle2+str2after2;
		return  num;
		
	}
	
	public static void main(String[] args) {

		String a = "{\"basicAccountBalance\":{\"staffCode\":null,\"itemGroupBalance\":null,\"specBalance\":\"0.00\",\"accountID\":null,\"accountBalance\":\"22.22\"}}";
		JSONObject json;
		try {
			json = new JSONObject(a);
			System.out.println(json.getString("accountBalance"));
			JSONArray results = json.getJSONArray("value");
			for (int i = 0; i < results.length(); i++) {
				JSONObject result = results.getJSONObject(i);
				System.out.println(result.getString("accountBalance") + " "
						+ result.getString("accountBalance"));
			}
		} catch (JSONException e) {
			
			e.printStackTrace();

		}

	}
}
