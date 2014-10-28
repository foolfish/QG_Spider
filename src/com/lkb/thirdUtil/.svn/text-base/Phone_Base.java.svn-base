package com.lkb.thirdUtil;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.ui.Model;

import com.lkb.bean.PhoneNum;
import com.lkb.location.*;
import com.lkb.service.IPhoneNumService;

public class Phone_Base {
	
	private static Logger logger = Logger.getLogger(Phone_Base.class);
	private String url="http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";
	public PhoneNum getPhoneBelong(String PhoneNo,IPhoneNumService phoneNumService){
		PhoneNum pn=new PhoneNum();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String belong=getText(httpclient,url+PhoneNo);
		System.out.println(belong);
		if(!belong.contains("catName")){
			return null;
		}
		JSONObject json;
		try {
			belong=belong.split("=")[1];
			json = new JSONObject(belong);
			String province = json.getString("province");
			String catName = json.getString("catName");
			String ptype=catName.replace("中国", "");
			pn.setCity(province);
			pn.setPhone(PhoneNo.substring(0, 7));
			pn.setProvince(province);
			pn.setPtype(ptype);
			phoneNumService.savePhoneNum(pn);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.info("第52行捕获异常：",e);		
			e.printStackTrace();
		}
		finally{
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("第60行捕获异常：",e);		
				e.printStackTrace();
			}
		}
		
		return pn;
	}
	public String getText(CloseableHttpClient httpclient,
			String redirectLocation) {
		HttpGet httpget = new HttpGet(redirectLocation);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			logger.info("第174行捕获异常：",e);		
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}

	// 梁涛注释
	public Boolean goWhere(PhoneNum phoneNum,Model model){
		Boolean flag = false;
		if(phoneNum==null){
			flag = true;
		}else{
			String value = null; //phoneNum.getPutongUrl();
			if(value!=null){
				model.addAttribute(value, "none");
			}else{
				 flag = true;
			}
//			if(phoneNum !=null && province.contains("上海")){		
//				 SH_Base shbase = new SH_Base();
//				 shbase.goWhere(ptype,  currentUser, model);
//
//		    }else if(phoneNum.getProvince().contains("北京")){		
//		    	 BJ_Base bjbase = new BJ_Base();
//		    	 bjbase.goWhere(ptype,  currentUser, model);
//
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("山西")){		
//			
//
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("湖南")){		
//			
//
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("重庆")){		
//		    	 Base base = new CQ_Base(); 
//		    	 base.goWhere(ptype,  currentUser, model);
//
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("江西")){		
//		    	 JX_Base jxbase = new JX_Base();
//		    	 jxbase.goWhere(ptype,  currentUser, model);
//
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("福建")){		
//		    	 FJ_Base fjbase = new FJ_Base();
//		    	 fjbase.goWhere(ptype,  currentUser, model);
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("浙江")){		
//		    	 ZJ_Base zjbase = new ZJ_Base();
//		    	 zjbase.goWhere(ptype,  currentUser, model);
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("广东")){		
//		    	GD_Base base = new GD_Base();
//		    	base.goWhere(ptype,  currentUser, model);
//
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("江苏")){		
//		    	JS_Base base = new JS_Base();
//		    	base.goWhere(ptype,  currentUser, model);
//
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("天津")){		
//		    	TJ_Base base = new TJ_Base();
//		    	base.goWhere(ptype,  currentUser, model);
//
//		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("甘肃")){		
//		    	GS_Base base = new GS_Base();
//		    	base.goWhere(ptype,  currentUser, model);
//
//		    }else{
//			    flag = true;
//		  }
		}
		return flag;

		
	}

	public Boolean goWhere(PhoneNum phoneNum, String currentUser,Model model){
		Boolean flag = false;
		if(phoneNum==null){
			flag = true;
		}else{
			AbstractBase ab = null;
			String province = phoneNum.getProvince();
			String ptype = phoneNum.getPtype();
			
			if(phoneNum !=null && province.contains("上海")){		
				ab = new SH_Base();
				ab.goWhere(ptype,  currentUser, model);
			}else if(phoneNum !=null&&phoneNum.getProvince().contains("北京")){		
				BJ_Base bjbase = new BJ_Base();
				bjbase.goWhere(ptype,  currentUser, model);
			}else if(phoneNum !=null&&phoneNum.getProvince().contains("山西")){		
		    	SHX_Base base = new SHX_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("湖南")){		
		    	 HuN_Base base = new HuN_Base(); 
		    	 base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("重庆")){		
		    	 CQ_Base base = new CQ_Base(); 
		    	 base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("宁夏")){		
		    	 NX_Base base = new NX_Base(); 
		    	 base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("江西")){		
		    	 JX_Base jxbase = new JX_Base();
		    	 jxbase.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("广西")){		
		    	 GX_Base base = new GX_Base();
		    	 base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("西藏")){		
		    	 XZ_Base base = new XZ_Base();
		    	 base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("青海")){		
		    	 QH_Base jxbase = new QH_Base();
		    	 jxbase.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("福建")){		
		    	 FJ_Base fjbase = new FJ_Base();
		    	 fjbase.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("浙江")){		
		    	 ZJ_Base zjbase = new ZJ_Base();
		    	 zjbase.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("河北")){		
		    	 HB_Base base = new HB_Base();
		    	 base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("黑龙江")){		
		    	 HLJ_Base base = new HLJ_Base();
		    	 base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("广东")){		
		    	GD_Base base = new GD_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("江苏")){		
		    	JS_Base base = new JS_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("天津")){		
		    	TJ_Base base = new TJ_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("甘肃")){		
		    	GS_Base base = new GS_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("湖北")){		
		    	HuB_Base base = new HuB_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("吉林")){		
		    	JL_Base base = new JL_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("陕西")){		
		    	SX_Base base = new SX_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("辽宁")){		
		    	LN_Base base = new LN_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("内蒙古")){		
		    	NMG_Base base = new NMG_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("贵州")){		
		    	GZ_Base base = new GZ_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("河南")){		
		    	HN_Base base = new HN_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("新疆")){		
		    	XJ_Base base = new XJ_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("海南")){		
		    	ab = new HaiN_Base();
		    	ab.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("云南")){		
		    	YN_Base base = new YN_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("四川")){		
		    	SC_Base base = new SC_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("安徽")){		
		    	AH_Base base = new AH_Base();
		    	base.goWhere(ptype,  currentUser, model);
		    }else if(phoneNum !=null&&phoneNum.getProvince().contains("山东")){		
		    	SD_Base base = new SD_Base();
		    	base.goWhere(ptype,  currentUser, model);

		    }else{
			    flag = true;

		  }
		}
		 
		return flag;

		
	}
}
