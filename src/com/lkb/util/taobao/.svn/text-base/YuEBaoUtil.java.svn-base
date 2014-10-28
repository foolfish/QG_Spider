package com.lkb.util.taobao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.User;
import com.lkb.bean.YuEBao;
import com.lkb.constant.Constant;
import com.lkb.service.IUserService;
import com.lkb.service.IYuEBaoService;
import com.lkb.util.DateUtils;



public class YuEBaoUtil {
     public static String read(String url)throws IOException{
    	 String p ="";
    	 String s = "";
         FileReader fr=new FileReader(url);
         //���Ի��ɹ���Ŀ¼�µ������ı��ļ�
         BufferedReader br=new BufferedReader(fr);
         while((p=br.readLine())!=null){
             s+=p;
            // System.out.println(s);
         }
         br.close();
		return s;
    	 
     }
     public static void yeb(String url) throws IOException, ParseException {
    	SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		String s = read(url);	
		System.out.println(s);
	}
     
     /**
     * @param currentUser
     * @param yuEBaoService
     * @param cuser
     * @return
     */
    public static Map<String,String>  getYuEBao(String currentUser,IYuEBaoService yuEBaoService,IUserService userService){
    	Map<String,String> map = null;
    	if(org.apache.commons.lang3.StringUtils.isNotEmpty(currentUser)){
    		Map canMap = new HashMap(2);
    		canMap.put("parentId", currentUser);
    		canMap.put("usersource", Constant.ZHIFUBAO);
    		List<User> contactlistlt = userService.getUserByParentIdSource(canMap);
    		if(contactlistlt!=null&&contactlistlt.size()!=0){
    			String alipayName = contactlistlt.get(0).getLoginName();
    			 List<Map> list = new ArrayList<Map>();
    		 		Date nowDate = new Date();
    		 		java.text.DateFormat format2 = new java.text.SimpleDateFormat(
    		 				"yyyy-MM-dd");
    		 		String date = format2.format(nowDate);
    		 		canMap = new HashMap<String,String>(3);
    				canMap.put("alipayName", alipayName);
    				DateUtils dateUtils = new DateUtils();
    				canMap.put("lmDay", dateUtils.getLMDay());
    				canMap.put("yearDay", dateUtils.getLMDay(12));
    				map = yuEBaoService.getYuEBaoPersonalStatistical(canMap);
    			
    		}
    	}
    	 return map;
     }
    
    
    public  List  getYuEBao(List alipayNames,IYuEBaoService yuEBaoService){
    	List list = new ArrayList();
    	for(int i=0;i<alipayNames.size();i++){
    		String alipayName = alipayNames.get(i).toString();
    		List<YuEBao> ylist = yuEBaoService.getYuEBaoByAlipay(alipayName);	
    		
    		Map map2 = new HashMap();
    		map2.put("loginName", alipayName);
			map2.put("source", Constant.ZHIFUBAO);
			map2.put("data", ylist);
			list.add(map2);
    	}
    	 return list;
     }
    
    
     public static void main(String[] args) throws IOException, ParseException {
    	 YuEBaoUtil.yeb("");
	}
}
