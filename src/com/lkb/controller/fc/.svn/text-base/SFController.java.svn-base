package com.lkb.controller.fc;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lkb.service.ILeaseHouseService;
import com.lkb.service.INewHouseService;
import com.lkb.service.IOldHouseService;
import com.lkb.thirdUtil.fc.SouFang;
import com.lkb.util.InfoUtil;
/*
 * 搜房网
 * */
@Controller
public class SFController {
	@Resource
	private IOldHouseService oldHouseService;
	@Resource
	private INewHouseService newHouseService;
	@Resource
	private ILeaseHouseService leaseHouseService;
	
	private static Logger logger = Logger.getLogger(SFController.class);
	

	/*
	 * 增加新房
	 * */
	@RequestMapping(value = "/fc/addnew", method = RequestMethod.GET)
	public String addnew(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		SouFang souFang = new SouFang();


			
		souFang.getCity( newHouseService);
			
			
	
		
		return "sucess";
	}

	/*
	 * 增加二手房
	 * */
	@RequestMapping(value = "/fc/adder", method = RequestMethod.GET)
	public String adder(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		SouFang souFang = new SouFang();
		
		
			souFang.getOldhouse( oldHouseService);
			
		
	
		
		return "sucess";
	}
	

	
	
	/*
	 * 增加租房整租房
	 * */
	@RequestMapping(value = "/fc/addlease", method = RequestMethod.GET)
	public String addlease(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		SouFang souFang = new SouFang();
	
		souFang.addReaLease( leaseHouseService);
		
		return "sucess";
	}
	
	
	/*
	 * 增加租房合租房
	 * */
	@RequestMapping(value = "/fc/addhzlease", method = RequestMethod.GET)
	public String addhzlease(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		SouFang souFang = new SouFang();
		souFang.addReaLease2( leaseHouseService);
		return "sucess";
	}
	
	
	
	
}
