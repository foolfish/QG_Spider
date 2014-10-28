package com.lkb.thirdUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lkb.bean.SheBao;
import com.lkb.service.ISheBaoService;
import com.lkb.util.DateUtils;

public class FinanceInformation {
	public List getIncomeInformation(List loginNames,ISheBaoService shebaoService){
		Map map=new HashMap();
		map.put("list", loginNames);
		List<Map> list=shebaoService.getSheBaoForReport1(map);
		if(list!=null && list.size()>0){
			if(list.get(0)!=null){
				String min=list.get(0).get("min").toString();
				min=min.substring(0, min.indexOf("."));
				String avg=list.get(0).get("avg").toString();
				avg=avg.substring(0, avg.indexOf("."));
				String max=list.get(0).get("max").toString();
				max=max.substring(0, max.indexOf("."));
				String start=list.get(0).get("start").toString().replace(" 00:00:00.0", "");
				String year=start.substring(0,4);
				String month=start.substring(5,6);
				if("0".equals(month)){
					month=start.substring(6,7);
				}
				else{
					month=start.substring(5,7);
				}
				start=year+"."+month;
				String end=list.get(0).get("end").toString().replace(" 00:00:00.0", "");
				year=end.substring(0,4);
				month=end.substring(5,6);
				if("0".equals(month)){
					month=end.substring(6,7);
				}
				else{
					month=end.substring(5,7);
				}
				end=year+"."+month;
				String modifyDate2 = DateUtils.getLYDay();
				map.put("payTime", modifyDate2);
				List<Map> list1=shebaoService.getSheBaoForReport2(map);
				String avglastyear="0";
				if(list1!=null && list1.size()>0&&list1.get(0)!=null&&list1.get(0).get("avgLastYear")!=null){
					 avglastyear=list1.get(0).get("avgLastYear").toString();
					 avglastyear=avglastyear.substring(0, avglastyear.indexOf("."));
				}				
				
				Map map1=new HashMap();
				map1.put("min", min);
				map1.put("avg", avg);
				map1.put("max", max);
				map1.put("last", start+"-"+end);
				map1.put("avgLasgYear",avglastyear);
				list.clear();
				list.add(map1);
			}	
		}
		return list;
	}
	
	
	public List getIncomeInformation(List loginNames,ISheBaoService shebaoService,String source){
		List shebaolist = new ArrayList();
		for(int i=0;i<loginNames.size();i++){
			String loginName = loginNames.get(i).toString();
			Map map=new HashMap();
			map.put("loginName", loginName);
			map.put("source", source);
			List<SheBao> list=shebaoService.getSheBaoByBaseUseridsource(map);
			
			Map map2 = new HashMap();
			map2.put("loginName", loginName);
			map2.put("source", source);
			map2.put("data", list);
			shebaolist.add(map2);
		}
		
		
		return shebaolist;
	}
}
