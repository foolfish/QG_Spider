package com.lkb.util.proxyip.entity;

import java.net.URLEncoder;


/**
 * @author fastw
 * @date 2014-7-8
 * @version 1.0
 */
public class TaobaoIp {
	private String ip;
	private Integer port;
	private boolean flag = true;//ip状态
	private int x;//ip所在集合的下标
	
	public TaobaoIp(){}
	
	public TaobaoIp(String ip, Integer port) {
		super();
		this.ip = ip;
		this.port = port;
	}
	public TaobaoIp(String ip, Integer port,int x) {
		super();
		this.ip = ip;
		this.port = port;
		this.x = x;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
	/**
	 * ip下标
	 * @return
	 */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "TaobaoIp [ip=" + ip + ", port=" + port + ", flag=" + flag + "]";
	}
	
	
	
	
	
 }
