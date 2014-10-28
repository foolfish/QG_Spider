package com.lkb.util.httpclient.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**公用返回结果标记类
 * @author fastw
 * @date 2014-7-9 下午10:00:08
 */
public class Result implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**字符串结果*/
	private String text;
	/**返回list集合*/
	private  List list;
	/**返回map集合*/
	private Map map;
	/**返回set集合*/
	private Set set;
	/**返回错误状态码*/
	private int code;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public <T> List<T>  getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public <T> Set<T> getSet() {
		return set;
	}
	public void setSet(Set set) {
		this.set = set;
	}
	/**设置错误状态码
	 * 0.正常返回
	 * 4.错误,不可处理,影响后边操作
	 * 5.ip代理失效
	 * 6.ip被封
	 */
	public int getCode() {
		return code;
	}
	/**设置错误状态码
	 * 0.正常返回
	 * 5.错误,不可处理,终止后边程序运行
	 * 6.ip代理失效
	 * 7.ip被封
	 */
	public void setCode(int code) {
		this.code = code;
		
	}
	/**
	 * @return 返回响应字符串
	 */
	public String getTexts(){
		if(code==A_Error){
			return  "服务器繁忙,请稍后再试!";
		}else{
			return text;
		}
	}
	/**
	 * 根据返回值确定是否在登录过程中出现问题,ture为没有出现
	 * @return
	 */
	public boolean isPass(){
		if(getText()!=null||getCode()!=0){
			return false;
		}
		return true;
	}

	/**错误,不可处理,终止后边程序运行*/
	public static final int A_Error = 5; 
	
	/**ip代理失效*/
	public static final int A_IP_Proxy = 6; 
	/**ip被封*/
	public static final int A_Ip_Seal = 7; 
	/**您输入的密码和账户名不匹配，请重新输入*/
	public static final String B_MMError = "您输入的密码和账户名不匹配，请重新输入"; 
	/**验证码错误*/
	public static final String B_CodeError = "验证码错误,请重新输入";
	/**请输入验证码*/
	public static final String B_CodeInfo = "为了您的账户安全，请输入验证码";
	/**账号不存在,请确定后重新输入*/
	public static final String B_AccountNot = "您输入的账号不存在!";
	
	
	public static final String B_Fanmang = "系统忙，请稍候再试！";
	public static final String C_Geshi = "温馨提示：您输入的手机号码格式不正确，请重新输入!";
	public static final String C_Guishudi = "手机号码非本地号码,请切换至号码归属地";
	public static final String C_Suiji = "温馨提示：请输入正确的短信随机密码!";
	public static final String C_Weizhuce = "温馨提示：您尚未注册，请注册后使用。";
	public static final String C_Cuowusanci = "对不起，密码输入错误三次，账号锁定!";
	public static final String C_YizhiWeizhuce = "您一直使用随机密码方式登录但从未注册，请您注册后使用。";
	public static final String C_Tiji = "对不起，您的手机号状态为停机！";
	public static final String C_Zhuxiao= "对不起，您的手机号状态为注销！";
	public static final String C_Yidenglu= "温馨提示：您已经登录本网站，请稍后再试！";

	@Override
	public String toString() {
		return "Result [text=" + text + ", list=" + list + ", map=" + map
				+ ", set=" + set + ", code=" + code + "]";
	} 
	
	
	
	
}
