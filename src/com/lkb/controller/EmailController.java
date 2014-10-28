package com.lkb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lkb.bean.ApplyEmail;
import com.lkb.bean.Cells;
import com.lkb.service.IApplyEmailService;
import com.lkb.service.ICellService;
import com.lkb.util.InfoUtil;


@Controller
public class EmailController {
	
	@Resource
	private IApplyEmailService applyEmailService;
	
	@Resource
	private ICellService cellService;
	
	@RequestMapping(value = "/sendEmail")
	public void sendEmail(HttpServletRequest req, HttpServletResponse response,
			Model model) {
		
		String email = (String) req.getParameter("email");
		String type = (String) req.getParameter("type");
		String realname = (String) req.getParameter("realname");
		String job = (String) req.getParameter("job");
		String teleno = (String) req.getParameter("teleno");

		int ctype = Integer.parseInt(type);
		Date date = new Date();
		Map map = new HashMap();
		map.put("email", email);
		map.put("ctype", ctype);
		map.put("teleno", teleno);
		List<ApplyEmail> list = applyEmailService.getApplyEmailEt(map);
		if(list!=null && list.size()>0){
			ApplyEmail applyEmail = (ApplyEmail)list.get(0);
			applyEmail.setApplyTime(date);
			applyEmail.setJob(job);
			applyEmail.setRealname(realname);
			applyEmail.setTeleno(teleno);
			applyEmailService.update(applyEmail);
		}else{
			ApplyEmail applyEmail = new ApplyEmail();
			applyEmail.setApplyTime(date);
			applyEmail.setEmail(email);
			applyEmail.setCtype(ctype);
			applyEmail.setJob(job);
			applyEmail.setRealname(realname);
			applyEmail.setTeleno(teleno);
			applyEmailService.save(applyEmail);		
		}
		
		
/*    	String[] toMail = {email};
    	String title = InfoUtil.getInstance().getInfo("mail","title");
    	String content = InfoUtil.getInstance().getInfo("mail","content");
    	SendMailOnTime sendMailOnTime = new SendMailOnTime();
    	sendMailOnTime.sendMail(toMail, title,content);*/
//    	Map map1 = new HashMap();
//    	map1.put("flag", "true");
//    	String jsonpstr = "jsonpcallback({\"flag\":\"true\"})";
    	String jsonpstr = "jsonpcallback(jsonpcallback(\"123\"));";
    	response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(jsonpstr);
			out.flush();
			out.close();
		} catch (IOException e) {
			
		}finally{
			if(out!=null){
				out.flush();
				out.close();
			}		
		}
	}
	
	
	/*
	 * 展示所有申请用户
	 * */
	@RequestMapping(value = "/listEmail")
	public String listEmail(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		
		List<ApplyEmail> list = applyEmailService.getAll();
		model.addAttribute("applyEmailList", list);
		return "/index/listEmail";
		
		
	}
	
	
	/*
	 * 展示所有申请用户
	 * */
	@RequestMapping(value = "/listJob")
	public String listJob(HttpServletRequest req, HttpServletResponse resp,
			Model model) {
		
		List<Cells> list = cellService.getCells();
		model.addAttribute("cellList", list);
		return "/index/listCell";
		
		
	}
	
}
