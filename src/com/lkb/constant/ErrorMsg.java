/**
 * 
 */
package com.lkb.constant;

/**
 * @author think
 * @date 2014-8-29
 */
public class ErrorMsg {
	/**正常*/
	public static final int normal = 0;
	/**页面过期，请重新刷新页面 */
	public static final int pageOver = 10010;
	/**userId太长，超过45个字符 */
	public static final int userIdlengthError = 10011;
	
	
	/**登录名为空 */
	public static final int loginNameEorror = 20001;
	/**密码为空  */
	public static final int passwordEorror = 20002;
	/**淘宝动态密码为空   */
	public static final int dynpasswordEorror = 20003;
	/**手机号码为空   */
	public static final int phoneEorror = 20004;
	/**userId为空   */
	public static final int userIdEorror = 20005;
	/**手机服务密码为空   */
	public static final int phonepassEorror = 20006;
	/**手机号码位数不正确   */
	public static final int phoneNum1Eorror = 20007;
	/**手机号码格式不正确  */
	public static final int phoneNum2Eorror = 20008;
	/**身份证位数不正确  */
	public static final int idcard1Eorror = 20009;
	/**邮箱类型不支持  */
	public static final int emailsupportEorror = 20010;
	/**验证码为空  */
	public static final int authcodetEorror = 20011;	
	/**征信查询码为空  */
	public static final int spassEorror = 20012;
	/**真实姓名为空或超过20位长度  */
	public static final int realnameEorror = 20013;
	/**省,为空*/
	public static final int provinceNullEorror = 20014;
	/**省 不存在*/
	public static final int provinceNotEorror = 20015;
	/**市,为空*/
	public static final int cityNullEorror = 20016;	
	/**市,不存在*/
	public static final int cityNotEorror = 20017;
	/**身份证号不能为空*/
	public static final int idCardNotEorror = 20018;
	/**社保号不能为空*/
	public static final int tsinNotEorror = 20019;
	/**姓名不能为空*/
	public static final int nameNotEorror = 20020;
	/**省市信息不符合规范或不正确*/
	public static final int provinceOrCityEorror = 20021;
}
