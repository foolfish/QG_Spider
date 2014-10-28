package com.lkb.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lkb.bean.User;
import com.lkb.bean.Warning;
import com.lkb.bean.YqMa;
import com.lkb.constant.Constant;
import com.lkb.service.IPhoneNumService;
import com.lkb.service.IUserService;
import com.lkb.service.IWarningService;
import com.lkb.service.IYqMaService;
import com.lkb.thirdUtil.LoginUtil;
import com.lkb.util.*;
/**
 * @author jzr
 *
 */
@Controller
public class WarningController {
	@Resource
	private IWarningService warningService;
	private static Logger logger = Logger.getLogger(WarningController.class);
	
	@RequestMapping(value = "/warning")
	public String warining(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		logger.info("warning!");
		///LKB/WebContent/WEB-INF/pages/waring/waringPage.jsp
		return "/warning/warning";
	}
	@RequestMapping(value = "/warning/getWarnings")
	public @ResponseBody Map<String, Object> getWarnings(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		String flag = req.getParameter("flag");
		Map<String, String> param = new HashMap<String, String>();
		param.put("flag", flag);
		List<Warning> warningList = warningService.getWarningByFlag(param);
		
		Map<String, Object> data = new HashMap<String, Object>();
		if (warningList!=null&&warningList.size()>0) {
			/*for (Iterator<Warning> iterator = warningList.iterator(); iterator.hasNext();) {
				Warning warning = (Warning) iterator.next();
				String s = DateUtils.formatDate(warning.getcTime(), "yyyyMM");
				System.out.println(s);
			}*/
			data.put("warningList", warningList);
		}
		return data;
	}
	@RequestMapping(value = "/warning/updateWarning")
	public String updateWarning(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		String id = req.getParameter("id");
		if (id!=null) {
			Warning warning = warningService.findById(id);
			if (warning!=null) {
				warning.setFlag(0);
				warningService.update(warning);
			}
		}
		return "/warning/warning";
	}
	
	

}
