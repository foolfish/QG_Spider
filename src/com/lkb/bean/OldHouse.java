package com.lkb.bean;

import java.math.BigDecimal;

/*
 * 二手房
 * */
public class OldHouse {
	private int id;
	private String  city;//城市：北京
	private String  hname;//小区名称：华贸国际公寓
	private String  hlocation;//大望路-建国路89号院
	
	private BigDecimal  havg;//房间均价（49020）
	private BigDecimal  hall;//房产总价（1000万
	private BigDecimal  hsize;//房间平米数：204
	
	private String  hst;//3室2厅
	private int hfloor;//层数 16
	private int hfloors;//总层数 27
	private String  hdirection;//方向：东南向
	private int hyear;//建筑年代：2003
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
	public BigDecimal getHall() {
		return hall;
	}
	public void setHall(BigDecimal hall) {
		this.hall = hall;
	}
	public BigDecimal getHsize() {
		return hsize;
	}
	public void setHsize(BigDecimal hsize) {
		this.hsize = hsize;
	}
	public String getHst() {
		return hst;
	}
	public void setHst(String hst) {
		this.hst = hst;
	}
	public int getHfloor() {
		return hfloor;
	}
	public void setHfloor(int hfloor) {
		this.hfloor = hfloor;
	}
	public int getHfloors() {
		return hfloors;
	}
	public void setHfloors(int hfloors) {
		this.hfloors = hfloors;
	}
	public String getHdirection() {
		return hdirection;
	}
	public void setHdirection(String hdirection) {
		this.hdirection = hdirection;
	}
	public int getHyear() {
		return hyear;
	}
	public void setHyear(int hyear) {
		this.hyear = hyear;
	}

	
	
}
