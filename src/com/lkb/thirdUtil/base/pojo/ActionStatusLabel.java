package com.lkb.thirdUtil.base.pojo;

import java.io.Serializable;

import com.lkb.bean.client.ResOutput;



/**
 * controller 请求标示类,主要防止伪同时请求一个地址
 * @author fastw
 * @date   2014-10-10 下午1:12:40
 */
public class ActionStatusLabel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1288144097936932902L;
	private ResOutput res;

	public ActionStatusLabel(){};
	
	
	public ActionStatusLabel(ResOutput res) {
		this.res = res;
	}


	public ResOutput getRes() {
		return res;
	}


	public ActionStatusLabel setRes(ResOutput res) {
		this.res = res;
		return this;
	}

	
	
}
