package com.lkb.warning;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.lkb.bean.Warning;
import com.lkb.service.IWarningService;

/*
 * 将报警写入数据库，并且发送报警邮件
 * */
public class WarningUtil {
	private IWarningService warningService;
	private String currentUser;
	SendMailOnTime sendmail = new SendMailOnTime();
	public void setContext(IWarningService warningService,String currentUser) {
		this.warningService = warningService;
		this.currentUser = currentUser;
	}
	public void warning(String warnType){
		warning(warningService, currentUser, warnType);
	}
	public void warning(IWarningService warningService,String currentUser,String warnType){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		java.util.Date date=new java.util.Date();  
		String dates=sdf.format(date);  		
		
		Boolean wflag = false;
		
		Map warningMap = new HashMap();
		warningMap.put("ptype", warnType);
		List<Warning> wlist = warningService.getWarningByType(warningMap);
		if(wlist!=null&&wlist.size()>0){
			Warning warning = wlist.get(0);
			if(warning.getFlag()==0){
				warning.setFlag(1);
				warningService.update(warning);
				wflag = true;
			}	
		}else{
			Warning warning = new Warning();
			warning.setcTime(date);
			warning.setCurrentId(currentUser);
			warning.setFlag(1);
			warning.setPtype(warnType);
			UUID wuuid = UUID.randomUUID();
			warning.setId(wuuid.toString());
			warningService.saveWarning(warning);
			wflag = true;
		}
		if(wflag){
			sendmail.sendAllMail( warnType+" 出问题了！登陆ID："+currentUser+"    时间："+dates);
		}
	}
}
