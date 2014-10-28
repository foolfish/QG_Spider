/**
 * 
 */
package com.lkb.thirdUtil.telcom.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import com.lkb.bean.User;
import com.lkb.util.StringUtil;

/**
 * @author think
 * @date 2014-9-2
 */
public abstract class TelcomTest {
	protected String phoneNo = null;

	protected String password = null;
	protected String imgCode = null;
	
	protected String[] personInfo = null;
	protected String[] detailInfo = null;
	protected String[] telListInfo = null;
	protected String[] mesInfo = null;
	
	protected String monthFormat = "yyyyMM";
	protected String detailFormat = "yyyy-MM-dd HH:mm:ss";
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	protected User user;
	protected abstract void initData();
	protected abstract void execute();
	@Test
	public void test() {
		initData();
		execute();
	}
	
	//比较客户信息
	public void compUserInfo(User u){
		if (u == null || personInfo == null) {
			return;
		}
		if(!StringUtils.isBlank(personInfo[0])){
			//collector.checkThat("姓名出错", new IsEqual<String>(personInfo[0]).equalTo(u.getRealName()));
			collector.checkThat(u.getRealName(), new IsEqual<String>(personInfo[0].trim()));
		}
		if(!StringUtils.isBlank(personInfo[1])){
			collector.checkThat(formatNumber(u.getPhoneRemain()), new IsEqual<String>(formatNumber(NumberUtils.toDouble(personInfo[1].trim()))));
			//collector.checkThat("余额出错", new IsEqual<String>(transBd(new BigDecimal(personInfo[1]))).equalTo(transBd(u.getPhoneRemain())));
		}
		if(!StringUtils.isBlank(personInfo[2])){
			collector.checkThat(u.getIdcard(), new IsEqual<String>(personInfo[2].trim()));
			//collector.checkThat("身份证号出错", new IsEqual<String>(personInfo[2]).equalTo(u.getIdcard()));
		}
		if(!StringUtils.isBlank(personInfo[3])){
			collector.checkThat(u.getAddr(), new IsEqual<String>(personInfo[3].trim()));
			//collector.checkThat("地址出错", new IsEqual<String>(personInfo[3]).equalTo(u.getAddr()));		
		}
		if(!StringUtils.isBlank(personInfo[4])){
			collector.checkThat(u.getEmail(), new IsEqual<String>(personInfo[4].trim()));
			//collector.checkThat("邮箱出错", new IsEqual<String>(personInfo[4]).equalTo(u.getEmail()));
		}
	}
	//把BigDecimal型数转化成两位小数点的字符串
	public static String formatNumber(Number bd){	
		if(bd==null)
			return null;
		return StringUtil.formatNumber(bd);
	}
}
