package com.lkb.thirdUtil.base;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.lkb.bean.User;
import com.lkb.bean.client.Login;
import com.lkb.bean.client.ResOutput;
import com.lkb.constant.ConstantProperty;
import com.lkb.constant.ErrorMsg;
import com.lkb.util.InfoUtil;
import com.lkb.util.httpclient.response.ExecuteAbstract;
import com.lkb.util.httpclient.thread.Task;
import com.lkb.util.redis.Redis;
import com.lkb.warning.WarningUtil;


public abstract class BasicCommonMobileControl<T> extends BasicCommonAbstract<T>{
	protected static Logger log = Logger.getLogger(BasicCommonMobileControl.class);
	public Task task;
	
	
	public BasicCommonMobileControl(Login login, String currentUser) {
		super(login, currentUser);
	}

	public BasicCommonMobileControl(Login login) {
		super(login);
	}

	/**本网站的验证码url*/
	public String getAuthcode(){
		String url = context.getImgUrl();
		if(StringUtils.isNotBlank(url)){
			StringBuffer sb = new StringBuffer();
			String uuid = UUID.randomUUID().toString();
			sb.append(InfoUtil.getInstance().getInfo(ConstantProperty.road,"server.full.path"));
			sb.append("/img_auth/").append(uuid);	
			sb.append(InfoUtil.getInstance().getInfo(ConstantProperty.road,"server.img.auth.code.suffix"));
			byte[] b = getAuthcodeByte();
			if(b!=null){
				Redis.setEx(uuid,b,2);	
			}
			return sb.toString();
					
		}
		return  url;
	}
	
	/**验证码字节数组
	 * @return
	 */
	private byte[] getAuthcodeByte(){
		if(context.getImgUrl()!=null)
			return cutil.downimgCode(context.getImgUrl());	
		return null;
	}
	
	public T index(){
		output = new ResOutput();
		if(verifyName(output)){
			try{
				this.init();
			}catch(Exception e){
			}
			output.setUrl(getAuthcode());
			output.setStatus(1);
			context.update();
		}
		return (T) output;
	}
	
	public T logins(){
		output = new ResOutput();
		if(verifyNameAndPassword(output)){
			if(ExecuteAbstract.isServerBusy()){
				output.setErrorMsg("服务器繁忙,请稍后再试!");
			}else{
				try{
					this.init();//校验基本参数是否初始化
				}catch(Exception e){}
				try{
					this.login();
				}catch(Exception e){
					log4j.login(e);
				}
				if(StringUtils.isBlank(output.getErrorMsg())){
					this.removeAndInit();
					output.setErrorMsg("登陆出错，请重试！");
				}
				output.setUrl(getAuthcode());
				context.update();
			}
		}
		return (T) output;
	}
	public abstract void login() throws Exception;
	public  void sendPhoneDynamicsCode(ResOutput output) throws Exception{};
	public void checkPhoneDynamicsCode(ResOutput output) throws Exception{};
	public  void getSeccondRequest(ResOutput output) throws Exception{};
	public void checkDynamicsCode2(ResOutput output) throws Exception{};

	@Override
	public T sendPhoneDynamicsCode() {
		ResOutput output = new ResOutput();
		if(!isTest()){
			if(verifyName(output)){
				if(ExecuteAbstract.isServerBusy()){
					output.setErrorMsg("服务器繁忙,请稍后再试!");
				}else{
					try{
						this.sendPhoneDynamicsCode(output);
					}catch(Exception e){
						log4j.warn(this.loginName+"发送短信响应异常", e);
						output.setErrorMsg("发送失败!");
					}
					context.update();
				}
			}
		}
		return (T) output;
	}

	@Override
	public T checkPhoneDynamicsCode() {
		ResOutput output = new ResOutput();
		if(verifyNamePhone(output)){
			if(ExecuteAbstract.isServerBusy()){
				output.setErrorMsg("服务器繁忙,请稍后再试!");
			}else{
				try{
					this.checkPhoneDynamicsCode(output);
				}catch(Exception e){
					log4j.warn(this.loginName+"验证短信异常#", e);
					output.setErrorMsg("验证短信失败!");
				}
				context.update();
			}
		}
		return (T) output;
	}

	/**
	 *针对登陆失败后,无错误信息进行的初始化操作
	 */
	private void removeAndInit(){
		Integer num = 1;
		String key = "BasicCommonControl.removeInit";
    	Object obj = redismap.get(key);
    	if(obj!=null){
    		int i = Integer.parseInt(obj.toString());
    		if(i<3){
    			num = ++i;
    		}else{
    			num = 1;
    			new WarningUtil().warning(service.getWarningService(),currentUser, this.getClass().getSimpleName()+"_登录失败");//登陆失败
    		}
    	}
		this.reset();
		this.init();
		redismap.put(key, num);
	}
	
	 /**
     * 添加时需要设置login.getType 如果是1大部分值采集个人信息和账单信息,2通话记录.3采集所有
     * @param base
     */
    public void addTask(int type){
    	//不是测试环境添加任务
    	if(!isTest()){
    		Task.addTask(this,type);
    	}
    	
	}
 
	/**检测login对象的用户名和密码是否是正确传值
	 */
	private  boolean verifyName(ResOutput output){
		if(StringUtils.isBlank(this.loginName)){
			output.setErrorcode(ErrorMsg.loginNameEorror);
			return false;
		}
		return true;
	}	
	/**检测login对象的用户名和密码是否是正确传值
	 */
	public boolean verifyNameAndPassword(ResOutput output){
		boolean b = true;
		if(StringUtils.isBlank(login.getLoginName())){
			output.setErrorcode(ErrorMsg.loginNameEorror);
			b = false;
		}
		if(StringUtils.isBlank(login.getPassword())){
			output.setErrorcode(ErrorMsg.passwordEorror);
			b = false;
		}
		if(StringUtils.isNotBlank(context.getImgUrl())){
			if(StringUtils.isBlank(login.getAuthcode())){
				output.setErrorcode(ErrorMsg.authcodetEorror);
				output.setErrorMsg("请输入验证码!");
				output.setUrl(getAuthcode());
				b = false;
			}
		}
		return b;
	}
	/**检测login对象的用户名和服务密码是否是正确传值
	 */
	private boolean verifyNameSpass(ResOutput output){
		boolean b = true;
		if(StringUtils.isBlank(login.getLoginName())){
			output.setErrorcode(ErrorMsg.loginNameEorror);
			b = false;
		}
		if(StringUtils.isBlank(login.getSpassword())){
			output.setErrorcode(ErrorMsg.phonepassEorror);
			b = false;
		}
		return b;
	}
	/**检测login对象的用户名和密码是否是正确传值
	 */
	public boolean verifyNamePhone(ResOutput output){
		boolean b = true;
		if(StringUtils.isBlank(login.getLoginName())){
			output.setErrorcode(ErrorMsg.loginNameEorror);
			b = false;
		}
		if(StringUtils.isBlank(login.getPhoneCode())){
			output.setErrorcode(ErrorMsg.dynpasswordEorror);
			b = false;
		}
		return b;
	}
	/**
	 * 保存个人信息,以后不再执行getUserInfo方法
	 * 子类方法必须重新getInfo方法
	 * @return
	 */
	public abstract User gatherUser() throws Exception;
	/**
	 * 保存用户
	 */
	public User saveUser(){
		User user = null;
		try{
			user = this.gatherUser();
		}catch(Exception e){
			log4j.writeLogByMyInfo(e);
		}
		return saveUser(user);
	}
	public User saveUser(User user){
		if(user!=null){
			User user1 = null;
			boolean b = false;
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("parentId", this.currentUser);
			map1.put("usersource", userSource);
			map1.put("loginName", this.loginName);
			List<User> list = null;
			try{
			 list = service.getUserService().getUserByParentIdSource(map1);
			}catch(Exception e){
			} 
			if (list != null && list.size() > 0) {
				user1 = list.get(0);
				b = true;
			}else{
				user1 = user;
				UUID uuid = UUID.randomUUID();
				user1.setId(uuid.toString());
			}
			user1.setUsersource(this.userSource);
			user1.setUsersource2(this.userSource);
			user1.setLoginName(this.loginName);
			
			if(StringUtils.isNotBlank(user.getUserName()))
				user1.setUserName(user.getUserName());
			if(StringUtils.isNotBlank(user.getRealName()))
				user1.setRealName(user.getRealName());
			if(StringUtils.isNotBlank(user.getIdcard()))
				user1.setIdcard(user.getIdcard());
			if(StringUtils.isNotBlank(user.getAddr()))
				user1.setAddr(user.getAddr());
			if(StringUtils.isEmpty(user1.getEmail())){
				user1.setEmail("");	
			}else{
				user1.setEmail(user.getEmail());
			}
			if(StringUtils.isNotBlank(user.getFixphone()))
				user1.setFixphone(user.getFixphone());
			if(user.getPhoneRemain()!=null)
				user1.setPhoneRemain(user.getPhoneRemain());
			if(StringUtils.isNotBlank(user.getPackageName()))
				user1.setPackageName(user.getPackageName());
			if(user.getBirthday()!=null)
				user1.setBirthday(user.getBirthday());
			if(StringUtils.isNotBlank(user.getBuyLevel()))
				user1.setBuyLevel(user.getBuyLevel());
			if(StringUtils.isNotBlank(user.getCardNo()))
				user1.setCardNo(user.getCardNo());
			if(StringUtils.isNotBlank(user.getCardstatus()))
				user1.setCardstatus(user.getCardstatus());
			if(StringUtils.isNotBlank(user.getCardType()))
				user1.setCardTye(user.getCardType());
			if(StringUtils.isNotBlank(user.getCardType()))	
				user1.setCardType(user.getCardType());
			if(StringUtils.isNotBlank(user.getCarePerson()))
				user1.setCarePerson(user.getCarePerson());
			if(user.getCars()!=0)
				user1.setCars(user.getCars());
			if(StringUtils.isNotBlank(user.getCertificateId()))
				user1.setCertificateId(user.getCertificateId());
			if(StringUtils.isNotBlank(user.getEduConclusion()))
				user1.setEduConclusion(user.getEduConclusion());
			if(StringUtils.isNotBlank(user.getEduForm()))
				user1.setEduForm(user.getEduForm());
			if(StringUtils.isNotBlank(user.getEduRecord()))
				user1.setEduRecord(user.getEduRecord());
			if(StringUtils.isNotBlank(user.getEduSchool()))
				user1.setEduSchool(user.getEduSchool());
			if(user.getEntranceDate()!=null)
				user1.setEntranceDate(user.getEntranceDate());
			if(StringUtils.isNotBlank(user.getFixphone()))
				user1.setFixphone(user.getFixphone());
			if(user.getGraduateDate()!=null)
				user1.setGraduateDate(user.getGraduateDate());
			if(StringUtils.isNotBlank(user.getHcatagory()))
				user1.setHcatagory(user.getHcatagory());
			if(user.getHourse()!=0)
				user1.setHourse(user.getHourse());
			if(StringUtils.isNotBlank(user.getIdcard()))
				user1.setIdcard(user.getIdcard());
			if(StringUtils.isNotBlank(user.getIdentifyTime()))
				user1.setIdentifyTime(user.getIdentifyTime());
			if(StringUtils.isNotBlank(user.getIsPhone()))
				user1.setIsPhone(user.getIsPhone());
			if(StringUtils.isNotBlank(user.getIsProtected()))
				user1.setIsProtected(user.getIsProtected());
			if(StringUtils.isNotBlank(user.getIsRealName()))
				user1.setIsRealName(user.getIsRealName());
			if(StringUtils.isNotBlank(user.getJobTitle()))
				user1.setJobTitle(user.getJobTitle());
			if(StringUtils.isNotBlank(user.getLive()))
				user1.setLive(user.getLive());
			if(StringUtils.isNotBlank(user.getMajor()))
				user1.setMajor(user.getMajor());
			if(StringUtils.isNotBlank(user.getMarkId()))
				user1.setMarkId(user.getMarkId());
			if(StringUtils.isNotBlank(user.getMemberLevel()))
				user1.setMemberLevel(user.getMemberLevel());
			if(StringUtils.isNotBlank(user.getMemberType()))
				user1.setMemberType(user.getMemberType());
			if(StringUtils.isNotBlank(user.getMerry()))
				user1.setMerry(user.getMerry());
			user1.setModifyDate(new Date());
			if(StringUtils.isNotBlank(user.getPackageName()))
				user1.setPackageName(user.getPackageName());
			user1.setParentId(this.currentUser);
			if(StringUtils.isNotBlank(user.getPaySalary()))
				user1.setPaySalary(user.getPaySalary());
			if(StringUtils.isNotBlank(user.getPayStatus()))
				user1.setPayStatus(user.getPayStatus());
			if(StringUtils.isNotBlank(user.getPetName()))
				user1.setPetName(user.getPetName());
			if(StringUtils.isNotBlank(user.getPhone()))
				user1.setPhone(user.getPhone());
			if(user.getPhoneRemain()!=null)
				user1.setPhoneRemain(user.getPhoneRemain());
			if(StringUtils.isNotBlank(user.getpUnit()))
				user1.setpUnit(user.getpUnit());
			if(StringUtils.isNotBlank(user.getQq()))
				user1.setQq(user.getQq());
			if(StringUtils.isNotBlank(user.getRealName()))
				user1.setRealName(user.getRealName());
			if(StringUtils.isNotBlank(user.getRedstar()))
				user1.setRedstar(user.getRedstar());
			if(user.getRegisterDate()!=null)
				user1.setRegisterDate(user.getRegisterDate());
			if(StringUtils.isNotBlank(user.getSchoolPlace()))
				user1.setSchoolPlace(user.getSchoolPlace());
			if(StringUtils.isNotBlank(user.getSecretLevel()))
				user1.setSecretLevel(user.getSecretLevel());
			if(StringUtils.isNotBlank(user.getSex()))
				user1.setSex(user.getSex());
			if(StringUtils.isNotBlank(user.getShebaolocation()))
				user1.setShebaolocation(user.getShebaolocation());
			if(StringUtils.isNotBlank(user.getSpecialty()))
				user1.setSpecialty(user.getSpecialty());
			if(StringUtils.isNotBlank(user.getSsNo()))
				user1.setSsNo(user.getSsNo());
			if(StringUtils.isNotBlank(user.getSspComNo()))
				user1.setSspComNo(user.getSspComNo());
			if(StringUtils.isNotBlank(user.getTaobaoName()))
				user1.setTaobaoName(user.getTaobaoName());
			if(StringUtils.isNotBlank(user.getUserName()))
				user1.setUserName(user.getUserName());
			if(StringUtils.isNotBlank(user.getWorkerNature()))
				user1.setWorkerNature(user.getWorkerNature());
			if(user.getyBalance()!=null)
				user1.setyBalance(user.getyBalance());
			if(user.getYincome()!=null)
				user1.setYincome(user.getYincome());
			saveUser(user1, b);
			return user1;
		}
		return user;
	}
	private boolean saveUser(User user,boolean b){
		if(!isTest()){
			try{
				if(b){
					 service.getUserService().update(user);
				}else{
					 service.getUserService().saveUser(user);
				}
			}catch(Exception e){
				log4j.writeLogByMyInfo(e);
				return false;
			}
		}
		return true;
	}
	
	
}
