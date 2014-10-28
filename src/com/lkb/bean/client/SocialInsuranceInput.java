package com.lkb.bean.client;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.lkb.constant.ErrorMsg;

/**
 * 社保登陆类
 * @author fastw
 * @date	2014-9-4 下午4:53:53
 */
public class SocialInsuranceInput extends Login implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1649764172646129363L;
	
	/**省*必填*/
	private String province;
	/**市*必填*/
	private String  city;
	/**身份证号*/
	private String idCard;
	/**姓名*/
	private String name;
	
	/**The social Insurance number,社保号*/
	private String tsin;

	public static final String param_tsin = "tsin";
	public static final String param_id_card = "idCard";
	public static final String param_name = "name";
	
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * @return 社保号
	 */
	public String getTsin() {
		return tsin;
	}
	public void setTsin(String tsin) {
		this.tsin = tsin;
	}
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	/**校验省市
	 * @return
	 */
	public int checkProvinAndCity(){
		int errorcode = ErrorMsg.normal;
		if(StringUtils.isEmpty(getProvince())){
			return ErrorMsg.provinceNullEorror;
		}else if(StringUtils.isEmpty(getCity())){
			return ErrorMsg.cityNullEorror;
		}
		return errorcode;
	}
	@Override
	public String toString() {
		return "SocialInsuranceInput [province=" + province + ", city=" + city
				+ ", idCard=" + idCard + ", name=" + name + ", tsin=" + tsin
				+ ", getPassword()=" + getPassword() + ", getAuthcode()="
				+ getAuthcode() + "]";
	}
	
	
	
	

}
