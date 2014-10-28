package com.lkb.thirdUtil.fc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lkb.bean.LeaseHouse;
import com.lkb.bean.NewHouse;
import com.lkb.bean.OldHouse;
import com.lkb.robot.SpiderManager;
import com.lkb.service.ILeaseHouseService;
import com.lkb.service.INewHouseService;
import com.lkb.service.IOldHouseService;
import com.lkb.thirdUtil.yd.HBYidong;
import com.lkb.util.InfoUtil;
import com.lkb.util.RegexPaserUtil;
import com.lkb.util.httpclient.CUtil;
import com.lkb.util.httpclient.ParseResponse;
import com.lkb.util.httpclient.entity.CHeader;

public class SouFang {
	public String getText(DefaultHttpClient httpclient,
			String redirectLocation) {
		CHeader cHeader = new CHeader();	
		String responseBody = "";
		try{
		//cHeader.setUser_Agent("	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
		//cHeader.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		
		HttpResponse response = CUtil.getHttpGet(redirectLocation, cHeader, httpclient);
		if(response!=null){
			responseBody = ParseResponse.parse(response,"gbk",true);
		}
		}catch(Exception e){}
		return responseBody;
	}
	
	public String getTextNew(DefaultHttpClient httpclient,
			String redirectLocation) {
		String responseBody = "";
		try{
		CHeader cHeader = new CHeader();	
		//cHeader.setUser_Agent("	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
		//cHeader.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		
		HttpResponse response = CUtil.getHttpGet(redirectLocation, cHeader, httpclient);
		if(response!=null){
			responseBody = ParseResponse.parse(response,"utf-8");
		}
		
		}catch(Exception e){}
		return responseBody;
	}
	public static void main(String[] args){
		SouFang souFang = new SouFang();
//		String url = "http://www.fang.com/house/s/b9";
//		try {
//			INewHouseService newHouseService = null;
//			souFang.parse(newHouseService,url,1,503,"南京");
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		ILeaseHouseService leaseHouseService = null;
		try {
			souFang.parse( leaseHouseService,"http://zu.hd.fang.com/house/i3",1,2,"邯郸");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//souFang.getCity();
	}
	
	
	public void getCity(INewHouseService newHouseService){
		String url = "http://fang.com/SoufunFamily.htm";
		DefaultHttpClient httpclient = CUtil.init();		
		String text = getTextNew(httpclient, url);
		Document doc =Jsoup.parse(text);
		String preid = "sffamily_B03_";
		String finishcity = InfoUtil.getInstance().getInfo("fc", "finishcity"); 
		String mm = "";
		List<Map> list2 = newHouseService.getAllCity();
		for(int m=0;m<list2.size();m++){
			String city = list2.get(m).get("city").toString();
			mm +=city;
		}
		
		List<Map> lists = new ArrayList<Map>();
		for(int i=1;i<=30;i++){
			String id = ""; 
			if(i<10){
				id = preid+"0"+i;
			}else{
				id = preid+i;
			}
			
			Element element = doc.select("tr[id="+id+"]").first();
			Elements elements = element.select("a");
			for(int j=0;j<elements.size();j++){
				Element element2 =elements.get(j); 
				String city = element2.text();
				if(!mm.contains(city)){
					System.out.println(element2.html());
					String key = element2.attr("href").trim().replace("http://", "").replace(".fang.com", "").replace("/", "");
					System.out.println(key);			
					Map map = new HashMap();
					
					String newurl = "http://newhouse."+key+".fang.com/house/s/b9";
					
					String text2 = getText(httpclient, newurl+"1");
					Document doc2 =Jsoup.parse(text2);
					String mourl = "";
					if(doc2.select("li[class=pagenum]")!=null && doc2.select("li[class=pagenum]").size()>0){
						for(int m=0;m<doc2.select("li[class=pagenum]").size();m++){
							String ss = doc2.select("li[class=pagenum]").get(m).text();
							if(ss.contains("尾页")){
								mourl = doc2.select("li[class=pagenum]").get(m).select("a").first().attr("href").replace("/house/s/b9", "").replace("/", "");
								break;
							}
							
						}
						
					}else{					
						try{
							Elements elements3 = doc2.select("div[id=sjina_C12_01]").select("a");
							for(int m=0;m<elements3.size();m++){
								String ss = elements3.get(m).text();
								if(ss.contains("尾页")){
									mourl = elements3.get(m).attr("href").replace("/house/s/b9", "").replace("/", "");
									break;
								}
								
							}
							
						}catch(Exception e){
							continue;
						}
					}
					int end = 1;
					try{
					 end = Integer.parseInt(mourl);
					}catch(Exception e){
						continue;
					}
					
					Map maps = new HashMap();
					maps.put("newurl", newurl);
					maps.put("end", end);
					maps.put("city", city);
					//SpiderManager.getInstance().startSouFang(this, newHouseService, map);
				}
				
			}
		}
		
		
		
	}

	public void parseCity(INewHouseService newHouseService, Map map) {
		String newurl = map.get("newurl").toString();
		int end = Integer.parseInt(map.get("end").toString());
		String city = map.get("city").toString();
		try {
			parse(newHouseService,newurl,1,end,city);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void getOldhouse(IOldHouseService oldHouseService){
		List list = new ArrayList();
		String url = "http://fang.com/SoufunFamily.htm";
		DefaultHttpClient httpclient = CUtil.createDefaultClient();		
		String text = getTextNew(httpclient, url);
		Document doc =Jsoup.parse(text);
		String preid = "sffamily_B03_";
		String finishcity = InfoUtil.getInstance().getInfo("fc", "finishcity"); 
		String mm = "";
		List<Map> list2 = oldHouseService.getAllCity();
		for(int m=0;m<list2.size();m++){
			String city = list2.get(m).get("city").toString();
			mm +=city;
		}
		
		for(int i=1;i<=30;i++){
			String id = ""; 
			if(i<10){
				id = preid+"0"+i;
			}else{
				id = preid+i;
			}
			
			Element element = doc.select("tr[id="+id+"]").first();
			Elements elements = element.select("a");
			for(int j=0;j<elements.size();j++){
				Element element2 =elements.get(j); 
				String city = element2.text();
				if(!mm.contains(city)){
					System.out.println(element2.html());
					String key = element2.attr("href").trim().replace("http://", "").replace(".fang.com", "").replace("/", "");
					System.out.println(key);			
					Map map = new HashMap();
					
					String newurl = "http://esf."+key+".fang.com/house/i3";
	
					String text2 = getText(httpclient, newurl+"1");
					System.out.println(text2);
					String newurl1 = null;
					if(text2.equalsIgnoreCase("")){
						newurl1 = "http://esf."+key+".fang.com/house/s31-c61/";
						text2 = getText(httpclient, newurl1);
					}
					RegexPaserUtil rq = new RegexPaserUtil("末页</a><span class=\"txt\">共","页</span>",text2,RegexPaserUtil.TEXTEGEXANDNRT);
					String endNum = rq.getText();
					if(endNum!=null){
						System.out.println("城市:"+city+",页数:"+endNum);
						int end = Integer.parseInt(endNum);
						try {
							parse(oldHouseService,newurl,1,end,city,httpclient,newurl1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
				}
				
			}
		}

		
		
	}
	
	
	/*
	 * 租房 整租
	 * */
	public void addReaLease(ILeaseHouseService leaseHouseService){
		List list = new ArrayList();
		String url = "http://fang.com/SoufunFamily.htm";
		DefaultHttpClient httpclient = CUtil.init();		
		String text = getTextNew(httpclient, url);
		Document doc =Jsoup.parse(text);
		String preid = "sffamily_B03_";
		String mm = "";
		List<Map> list2 = leaseHouseService.getAllCity();
		for(int m=0;m<list2.size();m++){
			String city = list2.get(m).get("city").toString();
			mm +=city;
		}
		
		for(int i=1;i<=30;i++){
			String id = ""; 
			if(i<10){
				id = preid+"0"+i;
			}else{
				id = preid+i;
			}
			
			Element element = doc.select("tr[id="+id+"]").first();
			Elements elements = element.select("a");
			for(int j=0;j<elements.size();j++){
				Element element2 =elements.get(j); 
				String city = element2.text();
				if(!mm.contains(city)){
					System.out.println(element2.html());
					String key = element2.attr("href").trim().replace("http://", "").replace(".fang.com", "").replace("/", "");
					System.out.println(key);			
			
					
					String newurl = "http://zu."+key+".fang.com/house/i3";
					
					String text2 = getText(httpclient, newurl+"1");
					Document doc2 =Jsoup.parse(text2);
					String mourl = "";
					Elements eles = doc2.select("div[class=fanye gray6]");
					if(eles!=null && eles.size()>0){
						Elements eles2 = eles.first().select("a");
						for(int m=0;m<eles2.size();m++){
							String ss = eles2.get(m).text();
							if(ss.contains("末页")){
								mourl = eles2.get(m).attr("href").replace("/house/i3", "").replace("/", "");
								break; 
							}
							
						}					
					}else{		
						eles = doc2.select("li[class=pages floatr]");
						if(eles!=null && eles.size()>0){						
							Elements eles2  = eles.first().select("a");
							for(int m=0;m<eles2.size();m++){
								String ss= eles2.get(m).text();
								if(ss.contains("末页")){
									mourl = eles2.get(m).attr("href").replace("/house/i3", "").replace("/", "");
									break; 
								}
							}					
						}else{
							newurl = "http://zu."+key+".fang.com/renthouse/i3";
							String text3 = getText(httpclient, newurl+"1");
							Document doc3 =Jsoup.parse(text3);
							mourl = doc3.select("a[id=PageControl1_hlk_last]").text();							
						}
					}
					
					int end = 1;
					try{
					 end = Integer.parseInt(mourl);
					}catch(Exception e){
						continue;
					}
					
					try {
						//SpiderManager.getInstance().startSouFang(this, leaseHouseService, newurl, 1, end, city);
						//parse(leaseHouseService,newurl,1,end,city);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
				
	}
	
	/*
	 * 租房 合租
	 * */
	public void addReaLease2(ILeaseHouseService leaseHouseService){
	
		String url = "http://fang.com/SoufunFamily.htm";
		DefaultHttpClient httpclient = CUtil.init();		
		String text = getTextNew(httpclient, url);
		Document doc =Jsoup.parse(text);
		String preid = "sffamily_B03_";
		String mm = "";
		List<Map> list2 = leaseHouseService.getAllCity();
		for(int m=0;m<list2.size();m++){
			String city = list2.get(m).get("city").toString();
			mm +=city;
		}
		
		for(int i=1;i<=30;i++){
			String id = ""; 
			if(i<10){
				id = preid+"0"+i;
			}else{
				id = preid+i;
			}
			
			Element element = doc.select("tr[id="+id+"]").first();
			Elements elements = element.select("a");
			for(int j=0;j<elements.size();j++){
				Element element2 =elements.get(j); 
				String city = element2.text();
				if(!mm.contains(city)){
					System.out.println(element2.html());
					String key = element2.attr("href").trim().replace("http://", "").replace(".fang.com", "").replace("/", "");
					System.out.println(key);			
				
					
					String newurl = "http://zu."+key+".fang.com/hezu/h31-i3";
					
					String text2 = getText(httpclient, newurl+"1");
					Document doc2 =Jsoup.parse(text2);
					String mourl = "";
					Elements eles = doc2.select("div[class=fanye gray6]");
					if(eles!=null && eles.size()>0){
						Elements eles2 = eles.first().select("a");
						for(int m=0;m<eles2.size();m++){
							String ss = eles2.get(m).text();
							if(ss.contains("末页")){
								mourl = eles2.get(m).attr("href").replace("/house/i3", "").replace("/", "");
								break; 
							}
							
						}					
					}else{		
						eles = doc2.select("li[class=pages floatr]");
						if(eles!=null && eles.size()>0){						
							Elements eles2  = eles.first().select("a");
							for(int m=0;m<eles2.size();m++){
								String ss= eles2.get(m).text();
								if(ss.contains("末页")){
									mourl = eles2.get(m).attr("href").replace("/house/i3", "").replace("/", "");
									break; 
								}
							}					
						}else{
							newurl = "http://zu."+key+".fang.com/renthouse/i3";
							String text3 = getText(httpclient, newurl+"1");
							Document doc3 =Jsoup.parse(text3);
							mourl = doc3.select("a[id=PageControl1_hlk_last]").text();							
						}
					}
					
					int end = 1;
					try{
					 end = Integer.parseInt(mourl);
					}catch(Exception e){
						continue;
					}
					try {
						//SpiderManager.getInstance().startSouFang(this, leaseHouseService, newurl, 1, end, city);
//						parse(leaseHouseService,newurl,1,end,city);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
				
	}
	
	
	/*
	 * 租房
	 * */
	public  void parse(ILeaseHouseService leaseHouseService,String url,int start,int end,String city) throws IOException{
		try{
		for(int i=start;i<=end;i++){
			if(i%10==0){
				Thread.sleep(2000);
			}
			System.out.println(city+"zufang开始采集第"+i+"页");
			String nurl = url+i;			
			DefaultHttpClient httpclient = CUtil.init();		
			String text = getText(httpclient, nurl);
			Document doc =Jsoup.parse(text);
			
			Elements eles = doc.select("dl[class=list hiddenMap rel]");
			if(eles!=null && eles.size()>2){
				parseA( eles, leaseHouseService, city);
			}else{
				 eles = doc.select("div[class=bkyellow]");
				 if(eles!=null && eles.size()>2){
					 parseB( eles, leaseHouseService, city);
				 }
			}
	
			
		
		}
		}catch(Exception ee){}
		
	}
	
	/*
	 * 分析第一种类型租房
	 * */
	public void parseA(Elements eles,ILeaseHouseService leaseHouseService,String city){
		for(int j=0;j<eles.size();j++){
			try{
			Element eles2 = eles.get(j);
			Element ele = eles2.select("dd[class=info rel floatr]").first();
			String textall = ele.select("p[class=gray6 mt10]").text().trim();
			String hlocation = ele.select("p[class=gray6 mt10]").first().select("span[class=iconAdress ml10]").first().attr("title").trim();
			String other = textall.replace(hlocation, "");
			String[] strs = other.split("-");
			System.out.println(other);
			String h_loc1 = "";
			String h_loc2 = "";
			if(strs!=null && strs.length>1){
				 h_loc1 = strs[0];
				 h_loc2 = strs[1];
			}
			
			String hname = "";
			if(strs!=null && strs.length>2){
				hname = strs[2];
			}
			
			String text2 = ele.select("p[class=gray6 mt5]").first().text().trim();
			String[] strs2 = text2.split("/");
			System.out.println(text2);
			String hst = strs2[0];
			String hsize = strs2[1].replace("㎡", "");
			String h_degree = strs2[2];
			String hfloor = strs2[3].replace("层", "");
			String hfloors = "0";
			try{
			if(strs2!=null && strs2.length>4){
				 hfloors = strs2[4].replace("层", "");;	
			}
			}catch(Exception e){
				System.out.println("出错了："+strs2[4]);
				
			}
			
			String hdirection = "";
			if(strs2.length>5&&strs2[5].trim().length()>0){
				 hdirection = strs2[5];	
			}
			
			String hlocation2 = ele.select("p[class=gray6 mt5]").get(1).text().trim();
			String havg = ele.select("div[class=moreInfo]").first().select("span[class=price]").text();
			
			
			BigDecimal bhavg = new BigDecimal("0");
			try{
				bhavg = new BigDecimal(havg);
			}catch(Exception e ){}
			BigDecimal bhsize = new BigDecimal("0");
			try{
				bhsize = new BigDecimal(hsize);
			}catch(Exception e ){}
			int ifloor = 0;
			try{
				ifloor = Integer.parseInt(hfloor);
			}catch(Exception e){
				
			}
			
			int ifloors = 0;
			try{
				ifloors = Integer.parseInt(hfloors);
			}catch(Exception e){
				
			}
			
			LeaseHouse leaseHouse = new LeaseHouse();
			leaseHouse.setCity(city);
			leaseHouse.setH_loc1(h_loc1);
			leaseHouse.setH_loc2(h_loc2);
			leaseHouse.setHname(hname);
			leaseHouse.setHlocation(hlocation);
			leaseHouse.setHlocation2(hlocation2);
			leaseHouse.setHavg(bhavg);
			leaseHouse.setHsize(bhsize);
			leaseHouse.setHst(hst);
			leaseHouse.setH_degree(h_degree);
			leaseHouse.setHfloor(ifloor);
			leaseHouse.setHfloors(ifloors);
			leaseHouse.setHdirection(hdirection);
			leaseHouseService.saveLeaseHouse(leaseHouse);
			}catch(	Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * 分析第二种类型租房
	 * */
	public void parseB(Elements eles,ILeaseHouseService leaseHouseService,String city){
		for(int j=0;j<eles.size();j++){
			try{
			Element eles2 = eles.get(j);
			Elements ele3 = eles2.select("div[class=house1]");
			if(ele3!=null && ele3.size()>0){
				Element ele =  ele3.first();
				Element ele1 = ele.select("p[class=black]").first();  
				String all = ele1.text();
				String hname = ele1.select("span").first().text();
				String hlocation = all.trim().replace(hname, "");
				if(hlocation.trim().equals("")){
					hlocation = hname;
				}
				BigDecimal bhsize = new BigDecimal("0");
				if(ele.select("p[class=black]")!=null && ele.select("p[class=black]").size()>1){
					Element ele2 = ele.select("p[class=black]").get(1);
					String hsize = ele2.select("span").first().text().replace("平米", "");
					try{
						bhsize = new BigDecimal(hsize);
					}catch(Exception e){
						
					}
				}
				
				String hst = ele.select("dd[class=area]").first().text().trim();
				String hvg = ele.select("dd[class=money]").first().select("strong").first().text().trim();
				BigDecimal havg = new BigDecimal("0");
				try{
					havg = new BigDecimal(hvg);
				}catch(Exception e){
					
				}
				LeaseHouse leaseHouse = new LeaseHouse();
				leaseHouse.setCity(city);
				leaseHouse.setHlocation(hlocation);
				leaseHouse.setHname(hname);

//				leaseHouse.setHlocation2(hlocation2);
			
				leaseHouse.setHsize(bhsize);
				leaseHouse.setHst(hst);
				leaseHouse.setHavg(havg);
				leaseHouseService.saveLeaseHouse(leaseHouse);
				
			}else{
				Element ele = eles2.select("div[class=house]").first().select("dl").first();
				String hname = ele.select("dt").first().select("p[class=black]").first().select("span").first().text();
				String hsize = "";
				String h_degree = "";
				String hfloor = "0";
				String hfloors = "0";
				String hdirection = "";
				if(ele.select("dt").first().select("p[class=black]")!=null && ele.select("dt").first().select("p[class=black]").size()>1){
					hsize = ele.select("dt").first().select("p[class=black]").get(1).select("span").first().text().replace("平米", "");
					String hall = ele.select("dt").first().select("p[class=black]").get(1).text().trim();
					if(hall.contains("平米")){
						String[] strs = hall.split("平米");
						if(strs!=null && strs.length>1){
							String other = strs[1];
							if(other.contains("，")){
								String[] strs2 = other.split("，");
								if(strs2!=null && strs2.length>0){
									for(int i=0;i<strs2.length;i++){
										String index = strs2[i];
										if(index.contains("装修")){
											h_degree = index;
										}else if(index.contains("层")){
											String enindex = index.replace("层", "");
											String[] strs3 = enindex.split("/");
											hfloor = strs3[0];
											hfloors = strs3[1];
										}else if(index.contains("向")){
											hdirection = index;
										}
										
										
									}
								}
								
							}
						}
					}
				}
				
				String hst =  "";
				try{
					hst = ele.select("dd[class=area]").first().select("p[class=mb10]").first().text();
				}catch(Exception e){}
				
				String havg = "0";
						
				try{
					havg = ele.select("dd[class=money]").first().select("strong").first().text().trim();
				}catch(Exception e){
					
				}
				
				BigDecimal bhavg = new BigDecimal("0");
				try{
					bhavg = new BigDecimal(havg);
				}catch(Exception e ){}
				BigDecimal bhsize = new BigDecimal("0");
				try{
					bhsize = new BigDecimal(hsize);
				}catch(Exception e ){}
				int ifloor = 0;
				try{
					ifloor = Integer.parseInt(hfloor);
				}catch(Exception e){
					
				}
				
				int ifloors = 0;
				try{
					ifloors = Integer.parseInt(hfloors);
				}catch(Exception e){
					
				}
				
			
				

				
				LeaseHouse leaseHouse = new LeaseHouse();
				leaseHouse.setCity(city);
		
				leaseHouse.setHname(hname);
//				leaseHouse.setHlocation(hlocation);
//				leaseHouse.setHlocation2(hlocation2);
				leaseHouse.setHavg(bhavg);
				leaseHouse.setHsize(bhsize);
				leaseHouse.setHst(hst);
				leaseHouse.setH_degree(h_degree);
				leaseHouse.setHfloor(ifloor);
				leaseHouse.setHfloors(ifloors);
				leaseHouse.setHdirection(hdirection);
				leaseHouseService.saveLeaseHouse(leaseHouse);
			}
				}catch(	Exception e){
					e.printStackTrace();
				}
			
			
		}
	}
	
	
	
	/*
	 * 新房
	 * */
	public  void parse(INewHouseService newHouseService,String url,int start,int end,String city) throws IOException{
		try{
		for(int i=start;i<=end;i++){
			if(i/10==0){
				Thread.sleep(2000);
			}
			System.out.println(city+"xinfang开始采集第"+i+"页");
			String nurl = url+i;			
			DefaultHttpClient httpclient = CUtil.init();		
			String text = "";
//			try{
//				text = getText(httpclient, nurl);
//			}catch(Exception e){
				try{
					text = getText(httpclient, nurl);
				}catch(Exception e2){}
//			}
			Document doc =Jsoup.parse(text);
			Elements eles = doc.select("div[class=sslalone]");
			for(int j=0;j<eles.size();j++){
				
				try{
				Element eles2 = eles.get(j);
				if(eles2.select("span[class=scpri]")!=null &&eles2.select("span[class=scpri]").size()>0){
					String hname = eles2.select("div[class=sclist_con_h2 tf]").first().select("a").text();
					String hall = eles2.select("div[class=sclist_con_h2 tf]").first().text();			
					String htype = hall.replace(hname, "").replace("[", "").replace("]", "");;
					String hlocation = eles2.select("p").first().text().replace("地址：", "").replace("查看地图", "");
					String hfix = "";
					if(eles2.select("p").first().select("span[class=sngrey]")!=null && eles2.select("p").first().select("span[class=sngrey]").size()>0){
						 hfix = eles2.select("p").first().select("span[class=sngrey]").text();
					}
					
					hlocation =hlocation.replace(hfix, "");
					hfix =hfix.replace("[", "").replace("]", "");
					String havg = eles2.select("span[class=scpri]").first().text();
					
					BigDecimal bhavg = new BigDecimal("0");
					try{
						bhavg = new BigDecimal(havg);
					}catch(Exception e ){}
					NewHouse newHouse = new NewHouse();
					newHouse.setCity(city);
					newHouse.setHname(hname);
					newHouse.setHtype(htype);
					newHouse.setHlocation(hlocation);
					newHouse.setHfix(hfix);
					newHouse.setHavg(bhavg);

					newHouseService.saveNewHouse(newHouse);
					
				}else{
					Element ele = eles2.select("ul[class=sslainfor]").first();
					String hname = ele.select("li").first().select("a[class=snblue]").text();
					String htype = "";
					String texts = "";
					try{
						htype = ele.select("li").first().select("span[class=sngrey]").text().replace("[", "").replace("]", "");
						texts = ele.select("li").get(1).text().trim();
					}catch(Exception e){}
					
					String hfix = "";
					try{
						hfix = ele.select("li").get(1).select("span[class=sngrey]").text().trim();
					}catch(Exception e){
						
					}
					String hlocation = "";
					try{
						 hlocation =texts.replace(hfix, "").replace("查看地图", "");
					}catch(Exception e){
						
					}
					
					String havg = "0";
					try{
						havg = eles2.select("ul[class=sslaright]").first().select("li[class=junjia]").first().select("span").text().trim();
					}catch(Exception e){
						
					}

					hfix = hfix.replace("[", "").replace("]", "");
					BigDecimal bhavg = new BigDecimal("0");
					try{
						bhavg = new BigDecimal(havg);
					}catch(Exception e ){}
					NewHouse newHouse = new NewHouse();
					newHouse.setCity(city);
					newHouse.setHname(hname);
					newHouse.setHtype(htype);
					newHouse.setHlocation(hlocation);
					newHouse.setHfix(hfix);
					newHouse.setHavg(bhavg);

					newHouseService.saveNewHouse(newHouse);
					
				}
				}catch(	Exception e){
					e.printStackTrace();
				}
			}
			
		
		}
		}catch(Exception ee){}
		
	}
	
	/*
	 * 北京二手房
	 * */
	public  void parse(IOldHouseService oldHouseService,String url,int start,int end,String city,DefaultHttpClient httpclient,String url2) throws IOException{
		String nurl = null;
		for(int i=start;i<=end;i++){
			System.out.println(city+"ershoufang开始采集第"+i+"页");
			if(url2==null){
				nurl = url+i;	
			}else{
				if(start==1){
					nurl = url2;
				}else{
					nurl = url+i;	
				}
			}
						
			HBYidong hbYidong = new HBYidong();			
			String text = getText(httpclient, nurl);
			Document doc =Jsoup.parse(text);
			Elements eles = doc.select("dl[class=list rel]");
			for(int j=0;j<eles.size();j++){
				try{
				Element eles2 = eles.get(j);
				Element ele = eles2.select("dd[class=info rel floatr]").first();
				String hname= ele.select("p[class=gray6 mt15]").first().select("span").first().text();
				System.out.println(hname);
				String hlocation = ele.select("span[class=iconAdress ml10]").first().attr("title");
				String hsts = ele.select("p[class=gray6 mt10]").first().text();
				String havg = ele.select("p[class=danjia alignR mt5 gray6]").first().text().replace("元/㎡", "");
				String hall = ele.select("span[class=price]").first().text();
				String hsize = ele.select("div[class=area alignR]").first().text().replace("㎡", "");
				String[] strs =  hsts.trim().split("/");
				String hst = "";
				String hfloor = "0";
				String hfloors = "0";
				String hdirection = "";
				String hyear = "0";
				
				if(strs.length>4){
					 hst = strs[0];
					 hfloor = strs[1];
					 hfloors = strs[2].replace("层", "");
					 hdirection = strs[3];
					 hyear = "0";
					
					if(strs.length>4&&strs[4].trim().length()>0){
						 hyear =  strs[4].replace("建筑年代：", "");		
					}
				}else{
					for(int m=0;m<strs.length;m++){
						String index = strs[m];
						if(index.contains("室")||index.contains("厅")){
							hst = index;
						}else if(index.contains("层")){
							hfloors = index.replace("层", "");
							hfloor =  strs[m-1];
						}else if(index.contains("朝")||index.contains("向")){
							 hdirection = index;
						}else if(index.contains("建筑年代")){
							hyear =  index.trim().replace("建筑年代：","");	
						}
					}
				}
				
				
				OldHouse oldHouse = new OldHouse();
				oldHouse.setCity(city);
				oldHouse.setHname(hname);
				oldHouse.setHlocation(hlocation);
				oldHouse.setHavg(new BigDecimal(havg));
				oldHouse.setHall(new BigDecimal(hall));
				oldHouse.setHsize(new BigDecimal(hsize));
			    oldHouse.setHst(hst);
				oldHouse.setHfloor(Integer.parseInt(hfloor));
				oldHouse.setHfloors(Integer.parseInt(hfloors));
				oldHouse.setHdirection(hdirection);
				oldHouse.setHyear(Integer.parseInt(hyear) );
				oldHouseService.saveOldHouse(oldHouse);
				}catch(	Exception e){}
			}
			
		
		}
		
		
	}

}
