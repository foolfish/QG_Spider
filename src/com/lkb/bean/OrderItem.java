package com.lkb.bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrderItem {
	private String id;
	private String orderTId;//订单表ID
	private String productName;//商品名称
	private String productId;//商品编号
	private BigDecimal price;//价格
	private int num;//商品数量
	private String productType;//商品类型
	private String merchant;//商家名字
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderTId() {
		return orderTId;
	}
	public void setOrderTId(String orderTId) {
		this.orderTId = orderTId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	
	
}
