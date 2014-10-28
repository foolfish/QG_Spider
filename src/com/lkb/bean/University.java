package com.lkb.bean;

/**
 * 211和985大学
 * 默认所有都是211院校
 * @author fastw
 */
public class University {
	private Integer id;
	private String name; //院校名称
	private Boolean type; //true==1为985
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getType() {
		return type;
	}
	public void setType(Boolean type) {
		this.type = type;
	}
	
	
	
}
