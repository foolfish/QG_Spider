package com.lkb.thirdUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.ui.Model;
import com.lkb.bean.PhoneNum;
import com.lkb.bean.User;
import com.lkb.constant.Constant;
import com.lkb.service.IPhoneNumService;
import com.lkb.service.IUserService;
import com.lkb.util.DateUtils;
/*
 * 登陆工具
 * */
public class LoginUtil {

	/*
	 * 拿到用户信息
	 * */
	public void base(String uid,IUserService userService,Model model){
		User user = userService.findById(uid);
		String realName = user.getRealName();
		String idcard = user.getIdcard();
		String email = user.getEmail();
		String qq = user.getQq();
		model.addAttribute("realName", realName);
		model.addAttribute("idcard", idcard);
		model.addAttribute("email", email);
		model.addAttribute("qq", qq);
		
		String shebao = user.getShebaolocation();
		int cars = user.getCars();
		int hourse = user.getHourse();
		model.addAttribute("shebao", shebao);
		model.addAttribute("cars", cars);
		model.addAttribute("hourse", hourse);
	}
	
	/*
	 * 拿到列表首页
	 * */
	public void entrance(String uid,IUserService userService,IPhoneNumService phoneNumService,Model model){
		Map<String,String> map = new HashMap<String,String>();
		
		User user = userService.findById(uid);
		String email = user.getEmail();
		String phone = user.getPhone();
		String location = user.getShebaolocation();
		if(email!=null&&(email.contains("163.com")||email.contains("126.com")||email.contains("yeah.net")||email.contains("sohu.com")||email.contains("139.com")||email.contains("21cn.com"))){
			model.addAttribute("emailshow", "show");
		}else{
			
		}
		
		if(location!=null && (location.contains("shanghai_shebao")||location.contains("shenzhen_shebao"))){
			model.addAttribute("shebaoshow", "show");
		}
		
		String phoneId = phone.substring(0, 7);
		
		PhoneNum phoneNum = phoneNumService.findById(phoneId);
		if(phoneNum==null){
			Phone_Base phonebase = new Phone_Base();
			phoneNum=phonebase.getPhoneBelong(phone,phoneNumService);
		}
		if(phoneNum !=null){
			String province = phoneNum.getProvince();
			String ptype = phoneNum.getPtype();
			if(province.contains("上海")||(province.contains("北京"))){
				model.addAttribute("phoneshow", "show");
			}
		}
		if(phoneNum !=null && (phoneNum.getProvince().contains("重庆"))){
			model.addAttribute("phoneshow", "show");
		}
		if(phoneNum !=null && (phoneNum.getProvince().contains("广东"))){
			model.addAttribute("phoneshow", "show");
		}
		if(phoneNum !=null && (phoneNum.getProvince().contains("福建"))){
			model.addAttribute("phoneshow", "show");
		}
		if(phoneNum !=null && (phoneNum.getProvince().contains("浙江"))){
			model.addAttribute("phoneshow", "show");
		}	
		if(phoneNum !=null && (phoneNum.getProvince().contains("江苏"))){
			model.addAttribute("phoneshow", "show");
		}
		model.addAttribute("email", email);
		model.addAttribute("phone", phone);
		
		
		String modifyDate2 = DateUtils.getLMDay();
		map.put("parentId", uid);
		map.put("modifyDate", modifyDate2);
		List<User> users = userService.checkParse(map);
		
		
		Boolean jdFlag = false;
		Boolean taobaoFlag = false;
		Boolean phoneFlag = false;
		Boolean xuexinFlag = false;
		Boolean zhengxinFlag = false;
		Boolean shebaoFlag = false;
		Boolean emailFlag = false;
		
		
		List<User> taobaoList = new ArrayList<User>();
		List<User> jdList = new ArrayList<User>();
		List<User> phoneList = new ArrayList<User>();
		Set emailSet = new HashSet();
		List<String> emailList = new ArrayList<String>();
		List<User> xuexinList = new ArrayList<User>();
		List<User> zhengxinList = new ArrayList<User>();
		List<User> shebaoList = new ArrayList<User>();
		
		for(int i=0;i<users.size();i++){
			User useri = users.get(i);
			String userSource = useri.getUsersource();
			if(userSource.equals(Constant.XUEXIN)){
				xuexinList.add(useri);
				xuexinFlag = true;
			}else if(userSource.equals(Constant.ZHENGXIN)){
				zhengxinList.add(useri);
				zhengxinFlag = true;
			}else if(userSource.equals(Constant.JD)){
				jdList.add(useri);
				jdFlag = true;
			}else if(userSource.equals(Constant.TAOBAO)){
				taobaoList.add(useri);
				taobaoFlag = true;
			}else if(userSource.equals(Constant.YIDONG) || userSource.equals(Constant.LIANTONG) || userSource.equals(Constant.DIANXIN)){
				phoneList.add(useri);
				phoneFlag = true;
			}else if(userSource.contains("SHEBAO")){
				shebaoList.add(useri);
				shebaoFlag = true;
			}else if(userSource.contains("126") || userSource.contains("163") || userSource.contains("yeah") || userSource.contains("sohu") || userSource.contains("139") || userSource.contains("21cn")){
				String email2 = useri.getLoginName();
				emailSet.add(email2);
				emailFlag = true;
			}
		}
		
		if(emailFlag == true){
			model.addAttribute("emailshow2", "show");
			Iterator it = emailSet.iterator();
			while(it.hasNext()){
				String email2 = it.next().toString();
				emailList.add(email2);
			}
			model.addAttribute("emailList", emailList);
		}
		
		if(xuexinFlag==true){
			model.addAttribute("xuexinshow", "show");
			model.addAttribute("xuexinList", xuexinList);
		}
		if(zhengxinFlag==true){
			model.addAttribute("zhengxinshow", "show");
			model.addAttribute("zhengxinList", zhengxinList);
		}
		if(jdFlag==true){
			model.addAttribute("jdshow", "show");
			model.addAttribute("jdList", jdList);
		}
		if(taobaoFlag==true){
			model.addAttribute("taobaoshow", "show");
			model.addAttribute("taobaoList", taobaoList);
		}
		if(jdFlag==true || taobaoFlag ==true){
			model.addAttribute("dsshow", "show");
		}
		if(phoneFlag==true){
			model.addAttribute("phoneshow2", "show");
			model.addAttribute("phoneList", phoneList);
		}
		if(shebaoFlag == true){
			model.addAttribute("shebaoshow2", "show");
			model.addAttribute("shebaoList", shebaoList);
		}

		
		
		model.addAttribute("userList", users);
	}

}
