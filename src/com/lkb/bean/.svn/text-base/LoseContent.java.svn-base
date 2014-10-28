package com.lkb.bean;

import java.util.Date;
import java.util.UUID;

/**错误记录类
 * @author fastw
 * @date  2014-10-19 下午12:35:17
 */
public class LoseContent {
	
	private String id;
	/**
	 * 记录错误地址
	 * 根据实际需要使用
	 * 此采集位单线程
	 * */
	private String url;
	//来源
	private String userSource;
	//登陆名
	private String loginName;
	//创建时间
	private Date updateTime;
	
	/**表示该方法的哪块,具体自己定义*/
	private Integer type;
	//0正常,1.异常
	private Integer errorCode;//update 0;
	//上次更新到这次更新的标志段
	private Long code;//更新
	//地址被更新次数如果该地址五次更新都出错,把url剔除
	private int count;
	
	//备用字段,根据实际情况 使用 
	private String verify;
	//备用字段
	private String flag;
	
	
	
	public LoseContent(){
		
	}
	public LoseContent(String url, String userSource,
			String loginName, Integer type, int errorCode,Long code) {
		super();
		this.id = UUID.randomUUID().toString();
		this.url = url;
		this.userSource = userSource;
		this.loginName = loginName;
		this.type = type;
		this.errorCode = errorCode;
		this.code = code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserSource() {
		return userSource;
	}
	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**表示该方法的哪块,具体自己定义*/
	public Integer getType() {
		return type;
	}
	/**表示该方法的哪块,具体自己定义*/
	public void setType(Integer type) {
		this.type = type;
	}
	/**0正常,1.异常*/
	public Integer getErrorCode() {
		return errorCode;
	}
	/**0正常,1.异常*/
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**统计地址次数**/
	public Integer getCount() {
		return count;
	}/**统计地址次数**/
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
	

}
