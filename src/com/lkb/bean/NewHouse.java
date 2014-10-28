package com.lkb.bean;

import java.math.BigDecimal;

/*
 * 新房
 * */
public class NewHouse {
	private int id;
	private String  city;//城市：北京
	private String  hname;//小区名称：华贸国际公寓
	private String  htype;//住宅类型
	private String  hfix;//几环
	private String  hlocation;//住宅位置	
	private BigDecimal  havg;//房间均价（49020）
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getHname() {
		return hname;
	}
	public void setHname(String hname) {
		this.hname = hname;
	}
	public String getHtype() {
		return htype;
	}
	public void setHtype(String htype) {
		this.htype = htype;
	}
	public String getHfix() {
		return hfix;
	}
	public void setHfix(String hfix) {
		this.hfix = hfix;
	}
	public String getHlocation() {
		return hlocation;
	}
	public void setHlocation(String hlocation) {
		this.hlocation = hlocation;
	}
	public BigDecimal getHavg() {
		return havg;
	}
	public void setHavg(BigDecimal havg) {
		this.havg = havg;
	}
	

	
	
	
}
