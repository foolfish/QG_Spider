/**
 * 
 */
package com.lkb.controller.telcom;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.PhoneNum;
import com.lkb.bean.Result;
import com.lkb.bean.req.FormData;
import com.lkb.constant.ErrorMsg;
import com.lkb.location.AbstractBase;
import com.lkb.robot.util.RobotUtil;
import com.lkb.service.IPhoneNumService;
import com.lkb.thirdUtil.Phone_Base;
import com.lkb.util.SessionUtil;

/**
 * @author think
 * @date 2014-8-13
 */
@Controller
public class PhoneController implements ApplicationContextAware{
	@Resource
	private IPhoneNumService phoneNumService;
	private static final String TYPE = "PhoneController";
	protected Logger logger = Logger.getLogger(getClass());
	private ApplicationContext context;
	
	
	@RequestMapping(value = "/pt/service/img/{key}")
	public void getImg(HttpServletRequest req, HttpServletResponse resp, Model model, @PathVariable String key) {
		Map<String,Object> map = null;
		resp.setContentType("image/jpeg;charset=UTF-8");
		map = RobotUtil.getCacheMap(null, key);
		if (map == null) {
			return;
		}
		byte[] bs = (byte[]) map.get("1");
		writeBytes(resp, bs);
	}
	@RequestMapping(value = "/pt_ued_phone_service")
	public @ResponseBody Result requireService(HttpServletRequest request,HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(request, resp, model);
		Result r = setPhoneResult(fd, null);
		r = setUserIdResult(fd, r);
		r = setDynamicCodeResult(fd, r);
		if (r != null) {
			return r;
		} else {
			try {
				if (RobotUtil.init(request, TYPE, fd.getPhoneNo())) {
					initContext();
					try {
						PhoneNum phoneNum = getPhoneNum(fd.getPhoneNo(), true);
						if (phoneNum != null) {
							AbstractBase ab = AbstractBase.getBase(phoneNum);
							r = ab.requireService(phoneNum, fd);
							r = setRtnResult(r);
						} else {
							r = setNoPhoneNumResult(r);
						}
					} catch (Exception e) {
						logger.error("error", e);
						r = setErrorResult(r);
					} finally {
						RobotUtil.stop();
					}
				}
			} catch (Exception e) {
				logger.error("error", e);
				r = setErrorResult(r);
			}

		}
		return r;
	}
	@RequestMapping(value = "/pt_ued_phone_sms")
	public @ResponseBody Result sendSMS(HttpServletRequest request,HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(request, resp, model);
		Result r = setPhoneResult(fd, null);
		r = setUserIdResult(fd, r);
		if (r != null) {
			return r;
		} else {
			try {
				if (RobotUtil.init(request, TYPE, fd.getPhoneNo())) {
					initContext();
					try {
						PhoneNum phoneNum = getPhoneNum(fd.getPhoneNo(), true);
						if (phoneNum != null) {
							AbstractBase ab = AbstractBase.getBase(phoneNum);
							r = ab.sendSMS(phoneNum, fd);
							r = setRtnResult(r);
						} else {
							r = setNoPhoneNumResult(r);
						}
					} catch (Exception e) {
						logger.error("error", e);
						r = setErrorResult(r);
					} finally {
						RobotUtil.stop();
					}
				}
			} catch (Exception e) {
				logger.error("error", e);
				r = setErrorResult(r);
			}

		}
		return r;
	}
	@RequestMapping(value = "/pt_ued_phone_login")
	public @ResponseBody Result goLogin(HttpServletRequest request,HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(request, resp, model);
		Result r = setPhoneResult(fd, null);
		r = setUserIdResult(fd, r);
		r = setPasswordResult(fd, r);
		if (r != null) {
			return r;
		} else {
			try {
				if (RobotUtil.init(request, TYPE, fd.getPhoneNo())) {
					initContext();
					try {
						PhoneNum phoneNum = getPhoneNum(fd.getPhoneNo(), true);
						if (phoneNum != null) {
							AbstractBase ab = AbstractBase.getBase(phoneNum);
							r = ab.goLogin(phoneNum, fd);
							r = setRtnResult(r);
						} else {
							r = setNoPhoneNumResult(r);
						}
					} catch (Exception e) {
						logger.error("error", e);
						r = setErrorResult(r);
					} finally {
						RobotUtil.stop();
					}
				}
			} catch (Exception e) {
				logger.error("error", e);
				r = setErrorResult(r);
			}

		}
		return r;
	}
	@RequestMapping(value = "/pt_ued_phone_img2")
	public @ResponseBody Result showImg2(HttpServletRequest request,HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(request, resp, model);
		String phone = fd.getPhoneNo();
		Result r = setPhoneResult(fd, null);
		r = setUserIdResult(fd, r);
		if (r != null) {
			return r;
		} else {
			try {
				if (RobotUtil.init(request, TYPE, fd.getPhoneNo())) {
					initContext();
					try {
						PhoneNum phoneNum = getPhoneNum(phone, true);
						if (phoneNum != null) {
							AbstractBase ab = AbstractBase.getBase(phoneNum);
							r = ab.showImg2(phoneNum, fd);
							r = setRtnResult(r);
							//r.setTitle(phoneNum.getProvince() + phoneNum.getPtype());
						} else {
							r = setNoPhoneNumResult(r);
						}
					} catch (Exception e) {
						logger.error("error", e);
						r = setErrorResult(r);
					} finally {
						RobotUtil.stop();
					}
				}
			} catch (Exception e) {
				logger.error("error", e);
				r = setErrorResult(r);
			}
		}
		return r;
	}
	@RequestMapping(value = "/pt_ued_phone_img")
	public @ResponseBody Result checkVerifyCode(HttpServletRequest request,HttpServletResponse resp, Model model) {
		FormData fd = FormData.build(request, resp, model);
		String phone = fd.getPhoneNo();
		Result r = setPhoneResult(fd, null);
		r = setUserIdResult(fd, r);
		if (r != null) {
			return r;
		} else {
			try {
				if (RobotUtil.init(request, TYPE, fd.getPhoneNo())) {
					initContext();
					try {
						PhoneNum phoneNum = getPhoneNum(phone, true);
						if (phoneNum != null) {
							AbstractBase ab = AbstractBase.getBase(phoneNum);
							r = ab.showValidateImg(phoneNum, fd);
							r.setTitle(phoneNum.getProvince() + phoneNum.getPtype());
							r = setRtnResult(r);
						} else {
							r = setNoPhoneNumResult(r);
						}
					} catch (Exception e) {
						logger.error("error", e);
						r = setErrorResult(r);
					} finally {
						RobotUtil.stop();
					}
				}
			} catch (Exception e) {
				logger.error("error", e);
				r = setErrorResult(r);
			}
		}
		return r;
	}
	@RequestMapping(value = "/pt_ued_fnc_list")
	public String showFncList(HttpServletRequest request,HttpServletResponse resp, Model model) {
		//final String currentUser = request.getSession().getAttribute("currentUser").toString();
		/*String phone = request.getParameter("phone");	*/
		return "/ued/uedhome";
	}
	private Result setUserIdResult(FormData fd, Result r) {
		if (r != null) {
			return r;
		}
		if (StringUtil.isBlank(fd.getUserId())) {
			r = new Result();
			r.setErrorMsg("请重新登录");
			r.setErrorcode(ErrorMsg.userIdEorror);
		} 
			
		if (r != null) {
			r.setSuccess(true);
		}
		return r;
	}
	private Result setPhoneResult(FormData fd, Result r) {
		if (r != null) {
			return r;
		}
		if (StringUtil.isBlank(fd.getPhoneNo())) {
			r = new Result();
			r.setErrorMsg("手机号码不能为空");
			r.setErrorcode(ErrorMsg.phoneEorror);
		}  else if (fd.getPhoneNo().length() != 11) {
			r = new Result();
			r.setErrorMsg("手机号码错误");
			r.setErrorcode(ErrorMsg.phoneNum1Eorror);
		}  else if (!StringUtils.isNumeric(fd.getPhoneNo())){
			r = new Result();
			r.setErrorMsg("手机号码错误");
			r.setErrorcode(ErrorMsg.phoneNum2Eorror);
		}
		if (r != null) {
			r.setSuccess(true);
		}
		return r;
	}
	private Result setDynamicCodeResult(FormData fd, Result r) {
		if (r != null || !StringUtil.isBlank(fd.getSmsCode())) {
			return r;
		}
		if (r == null) {
			r = new Result();
		}
		r.setSuccess(true);
		r.setErrorMsg("动态密码不能为空");
		r.setErrorcode(ErrorMsg.dynpasswordEorror);
		return r;
	}
	private Result setPasswordResult(FormData fd, Result r) {
		if (r != null || !StringUtil.isBlank(fd.getPassword())) {
			return r;
		}
		if (r == null) {
			r = new Result();
		}
		r.setSuccess(true);
		r.setErrorMsg("登录密码不能为空");
		r.setErrorcode(ErrorMsg.passwordEorror);
		return r;
	}
	private Result setFailResult(Result r) {
		if (r == null) {
			r = new Result();
		}
		r.setSuccess(true);
		r.setErrorMsg("请重新登录！(1)");
		r.setErrorcode(ErrorMsg.loginNameEorror);
		return r;
	}
	private Result setErrorResult(Result r) {
		if (r == null) {
			r = new Result();
		}
		r.setSuccess(true);
		r.setErrorMsg("服务器错误！");
		return r;
	}
	private Result setNoPhoneNumResult(Result r) {
		if (r == null) {
			r = new Result();
		}
		r.setSuccess(true);
		r.setErrorMsg("手机号错误！(3)");
		r.setErrorcode(ErrorMsg.phoneNum2Eorror);
		return r;
	}
	private Result setRtnResult(Result r) {
		if (r == null) {
			r = new Result();
		}
		if (!r.isSuccess()) {
			r.setErrorMsg("暂不支持该地区手机号");
		}
		return r;
	}
	private PhoneNum getPhoneNum(String phone, boolean getCache) {
		PhoneNum phoneNum = null;
		Map<String, Object> map = getCache ? RobotUtil.getCacheMap(TYPE, null) : null;
		if (map != null && map.get("PhoneNum") != null) {
			phoneNum = (PhoneNum)map.get("PhoneNum"); 
			//phoneNum.setProvince((String) map.get("PhoneNum"));
		} else {
			String phoneId = phone.substring(0, 7);
			phoneNum = phoneNumService.findById(phoneId);
			Phone_Base phonebase = new Phone_Base();
			if(phoneNum == null){
				phoneNum = phonebase.getPhoneBelong(phone, phoneNumService);
			}
			if (phoneNum != null) {
				initCacheMap(phoneNum);
			}
		}
		return phoneNum;
	}
	
	private void initContext() {
		SessionUtil.setObject(AbstractBase.CURRENT_CONTEXT, context);
	}
	private void initCacheMap(PhoneNum pn) {
		Map<String, Object> map = RobotUtil.getCacheMap(TYPE, null);
		if (map != null) {
			map.put("PhoneNum", pn);
			RobotUtil.setCacheMap(TYPE, null, map);
		}
	}
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		context = arg0;
		
	}
	private void writeBytes(HttpServletResponse resp, byte[] b){
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		ServletOutputStream out = null;
		if(b!=null){
			try {
				out = resp.getOutputStream();
				IOUtils.write(b, out);
			} catch (IOException e) {
				logger.error("error", e);
			}finally{
				IOUtils.closeQuietly(out);
			}
		}
	}
}
