package com.lkb.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.lkb.bean.Cells;
import com.lkb.bean.PhoneNum;
import com.lkb.service.ICellService;
import com.lkb.service.IPhoneNumService;
import com.lkb.thirdUtil.Phone_Base;

public class CleanRead{
	
	public static void main(String args[]) {
        try {
            // 打开文件
            WritableWorkbook book = Workbook.createWorkbook(new File("d:/test.xls"));
            // 生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("第一页", 0);
            // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
            // 以及单元格内容为test
            Label label = new Label(0, 0, "test");
           
            sheet.addCell(label);

            Label label2 = new Label(0, 1, "3333");//第0列第1行（从0开始）
            // 将定义好的单元格添加到工作表中
            sheet.addCell(label2);
            
            // 写入数据并关闭文件
            book.write();
            book.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

	/*
	 * 准备写入
	 * */
	public void writeExcel(ICellService ics){
		//全部的list
		List listAll = new ArrayList();
		//List phonelist = new ArrayList();//电话
		List lablelist = new ArrayList();//近期违约
		List agelist = new ArrayList();//年龄
		List sexlist = new ArrayList();//性别
		List incomelist = new ArrayList();//收入
		List babylist = new ArrayList();//有无子女
		List xqlist = new ArrayList();//兴趣
		List domainlist = new ArrayList();//域名
		
		String phone="手机号";
		listAll.add(phone);
		
		List<Map> lableList = ics.getlable();
		for(int i=0;i<lableList.size();i++){
			String lable = lableList.get(i).get("lable").toString();
			lablelist.add(lable);
			listAll.add(lable);
		}
		
		List<Map> ageList = ics.getAge();
		for(int i=0;i<ageList.size();i++){
			String age = ageList.get(i).get("age").toString();
			agelist.add(age);
			listAll.add(age);
		}
		
		List<Map> sexList = ics.getSex();
		for(int i=0;i<sexList.size();i++){
			String sex = sexList.get(i).get("sex").toString();
			sexlist.add(sex);
			listAll.add(sex);
		}
		
		List<Map> incomeList = ics.getIncome();
		for(int i=0;i<incomeList.size();i++){
			String income = incomeList.get(i).get("income").toString();
			incomelist.add(income);
			listAll.add(income);
		}
		
		List<Map> babyList = ics.getBaby();
		for(int i=0;i<babyList.size();i++){
			String baby = babyList.get(i).get("baby").toString();
			babylist.add(baby);
			listAll.add(baby);
		}
		
		List<Map> xqList = ics.getXq();
		for(int i=0;i<xqList.size();i++){
			String xq = xqList.get(i).get("xq").toString();	
			String[] sxqs = xq.split(",");
			for(int j=0;j<sxqs.length;j++){
				if(!listAll.contains(sxqs[j])){
					xqlist.add(sxqs[j]);
					listAll.add(sxqs[j]);
				}
			
			}
			
		}
		
		List<Map> domainList = ics.getDomain();
		for(int i=0;i<domainList.size();i++){
			String domain = domainList.get(i).get("domain").toString();
			String[] domains = domain.split(",");
			for(int j=0;j<domains.length;j++){
				if(!listAll.contains(domains[j])){
					domainlist.add(domains[j]);
					listAll.add(domains[j]);
				}
			}
		}
		String keywords = "keywords";
		listAll.add(keywords);

		 try {
	            // 打开文件
	            WritableWorkbook book = Workbook.createWorkbook(new File("d:/test.xls"));
	            // 生成名为“第一页”的工作表，参数0表示这是第一页
	            WritableSheet sheet = book.createSheet("第一页", 0);
	            // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
	            // 以及单元格内容为test
	   
	            for(int i=0;i<listAll.size();i++){
	            	String mm= (String)listAll.get(i);
	            	Label label = new Label(i, 0, mm);
	 	           
		            sheet.addCell(label);
		     
	            }

	            List list = ics.getCells();
	    		for(int i=0;i<list.size();i++){
	    			Cells cells = (Cells)list.get(i);
	    			String phones = cells.getPhone();
	    			Label label = new Label(0, i+1  , phones);		 	           
		            sheet.addCell(label);    
		            
		            String lables = cells.getLable();
		            for(int j=0;j<lablelist.size();j++){
		            	String lablei = (String)lablelist.get(j);
		            	String status = "0";
		            	if(lablei.equals(lables)){
		            		 status = "1";
		            	}
		            	Label label2 = new Label(1+j, i+1  , status);		 	           
			            sheet.addCell(label2);    
			           
		    		}
		            
		            String ages = cells.getAge();
		            for(int j=0;j<agelist.size();j++){
		            	String age = (String)agelist.get(j);
		            	String status = "0";
		            	if(age.equals(ages)){
		            		 status = "1";
		            	}
		            	Label label2 = new Label(1+lablelist.size()+j, i+1  , status);		 	           
			            sheet.addCell(label2);    
			           
		    		}
		            
		            
		            String sexs = cells.getSex();
		            for(int j=0;j<sexlist.size();j++){
		            	String sex = (String)sexlist.get(j);
		            	String status = "0";
		            	if(sex.equals(sexs)){
		            		 status = "1";
		            	}
		            	Label label2 = new Label(1+lablelist.size()+agelist.size()+j, i+1  , status);		 	           
			            sheet.addCell(label2);    
			           
		    		}
		            
		            String incomes = cells.getIncome();
		            for(int j=0;j<incomelist.size();j++){
		            	String income = (String)incomelist.get(j);
		            	String status = "0";
		            	if(income.equals(incomes)){
		            		 status = "1";
		            	}
		            	Label label2 = new Label(1+lablelist.size()+agelist.size()+sexlist.size()+j, i+1  , status);		 	           
			            sheet.addCell(label2);    
			           
		    		}
		            
		            String babys = cells.getBaby();
		            for(int j=0;j<babylist.size();j++){
		            	String baby = (String)babylist.get(j);
		            	String status = "0";
		            	if(baby.equals(babys)){
		            		 status = "1";
		            	}
		            	Label label2 = new Label(1+lablelist.size()+agelist.size()+sexlist.size()+incomelist.size()+j, i+1  , status);		 	           
			            sheet.addCell(label2);    
			           
		    		}
		            String xqs = cells.getXq();
		            for(int j=0;j<xqlist.size();j++){
		            	String xq = (String)xqlist.get(j);
		            	String status = "0";
		            	if(xqs.contains(xq)){
		            		 status = "1";
		            	}
		            	Label label2 = new Label(1+lablelist.size()+agelist.size()+sexlist.size()+incomelist.size()+babylist.size()+j, i+1  , status);		 	           
			            sheet.addCell(label2);    
			           
		    		}
		            
		            String domains = cells.getDomain();
		            for(int j=0;j<domainlist.size();j++){
		            	String domain = (String)domainlist.get(j);
		            	String status = "0";
		            	if(domains.contains(domain)){
		            		 status = "1";
		            	}
		            	Label label2 = new Label(1+lablelist.size()+agelist.size()+sexlist.size()+incomelist.size()+babylist.size()+xqlist.size()+j, i+1  , status);		 	           
			            sheet.addCell(label2);    
			           
		    		}
		            
		            String keyword = cells.getKeyword();
	            	Label label2 = new Label(1+lablelist.size()+agelist.size()+sexlist.size()+incomelist.size()+babylist.size()+xqlist.size()+domainlist.size(), i+1  , keyword);		 	           
		            sheet.addCell(label2);    
		            
	    		}
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	          
	            // 写入数据并关闭文件
	            book.write();
	            book.close();

	        } catch (Exception e) {
	            System.out.println(e);
	        }
		
	}
	
	
	/*
	 * 读文件
	 * */
	public  void readintodb(ICellService ics) throws Exception {
		// 通过Workbook的静态方法getWorkbook选取Excel文件
		FileUtil futil = new FileUtil();
		Workbook workbook = Workbook.getWorkbook(new File("E:/1/0611.xls"));
		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		System.out.println(rows);
		int cols = sheet.getColumns();
		System.out.println(cols);
		Cell cells[][] = new Cell[cols][rows];
		StringBuffer sb = new StringBuffer(100000000);
		for (int i = 0; i < rows; i++) {			
			cells[0][i] = sheet.getCell(0, i);
			String phone = cells[0][i].getContents();
				
			cells[1][i] = sheet.getCell(1, i);
			String lable = cells[1][i].getContents();
			
			cells[2][i] = sheet.getCell(2, i);
			String age = cells[2][i].getContents();
			
			cells[3][i] = sheet.getCell(3, i);
			String sex = cells[3][i].getContents();
			
			cells[4][i] = sheet.getCell(4, i);
			String income = cells[4][i].getContents();
			
			cells[5][i] = sheet.getCell(5, i);
			String baby = cells[5][i].getContents();
			
			cells[6][i] = sheet.getCell(6, i);
			String xq = cells[6][i].getContents();
			
			cells[7][i] = sheet.getCell(7, i);
			String domain = cells[7][i].getContents();
			
			cells[8][i] = sheet.getCell(8, i);
			String keyword = cells[8][i].getContents();
			
			Cells cells1 =  ics.findById(phone);
			if(cells1!=null){
				if(cells1.getAge()=="NULL"){
					cells1.setAge(age);
				}
				if(cells1.getSex()=="NULL"){
					cells1.setSex(sex);
				}
				if(cells1.getIncome()=="NULL"){
					cells1.setIncome(income);
				}
				if(cells1.getBaby()=="NULL"){
					cells1.setBaby(baby);
				}
				HashSet set = new HashSet();
				if(cells1.getXq()=="NULL"){
					cells1.setXq(xq);
				}else{
					String xq1 = cells1.getXq();
					String[] xq1s = xq1.split(",");
					for(int m=0;m<xq1s.length;m++){
						set.add(xq1s[m]);
					}
					if(xq!="NULL"){
						String[] xq2s = xq.split(",");
						for(int m=0;m<xq2s.length;m++){
							set.add(xq2s[m]);
						}
					}
					String content = "";
					Iterator it = set.iterator();
					while (it.hasNext()) {   
						  String str = (String) it.next();   
						  content+=str+",";   
					}
					cells1.setXq(content);
					
				}
				
				HashSet set2 = new HashSet();
				if(cells1.getDomain()=="NULL"){
					cells1.setDomain(domain);
				}else{
					String domain1 = cells1.getDomain();
					String[] domain1s = domain1.split(",");
					for(int m=0;m<domain1s.length;m++){
						set2.add(domain1s[m]);
					}
					if(domain1!="NULL"){
						String[] domain1s2s = domain.split(",");
						for(int m=0;m<domain1s2s.length;m++){
							set2.add(domain1s2s[m]);
						}
					}
					String content = "";
					Iterator it = set2.iterator();
					while (it.hasNext()) {   
						  String str = (String) it.next();   
						  content+=str+",";   
					}   
					cells1.setDomain(content);
					
				}
				
				HashSet set3 = new HashSet();
				if(cells1.getKeyword()=="NULL"){
					cells1.setKeyword(keyword);
				}else{
					String keyword1 = cells1.getKeyword();
					String[] keyword1s = keyword1.split(",");
					for(int m=0;m<keyword1s.length;m++){
						set3.add(keyword1s[m]);
					}
					if(keyword!="NULL"){
						String[] keyword2s = keyword.split(",");
						for(int m=0;m<keyword2s.length;m++){
							set3.add(keyword2s[m]);
						}
					}
					String content = "";
					Iterator it = set3.iterator();
					while (it.hasNext()) {   
						  String str = (String) it.next();   
						  content+=str+",";   
					}   
					cells1.setKeyword(content);
					
				}
				
				
	
				ics.update(cells1);
			}else{
				cells1 = new Cells();
				cells1.setPhone(phone);
				cells1.setAge(age);
				cells1.setBaby(baby);
				cells1.setDomain(domain);
				cells1.setIncome(income);
				cells1.setKeyword(keyword);
				cells1.setLable(lable);
				cells1.setSex(sex);
				cells1.setXq(xq);
				ics.saveCell(cells1);
			}
			
			
			
			
			
		}
		

		workbook.close();
	}
	
	
	
	
	

	public static void write(IPhoneNumService ps) throws BiffException {
		try {
			StringBuffer sb = new StringBuffer();
			String content2 = "";
			FileUtil futil = new FileUtil();
			String files = futil.readFile("E:/1/h.txt");
			String[] strs = files.split(",");
			for (int i = 0; i < strs.length; i++) {
				String phone = strs[i];
				if (phone != null && phone.length() > 7) {
					String prephone = phone.substring(0, 7);
					PhoneNum phoneNum = ps.findById(prephone);
					if (phoneNum != null) {
						String province = phoneNum.getProvince();
						content2 = phone + "," + province;
					} else {
						i++;
						Phone_Base phonebase = new Phone_Base();
						phoneNum = phonebase.getPhoneBelong(phone, ps);
						if (phoneNum != null) {
							content2 = phone + "," + phoneNum.getProvince();
						}
					}
					sb.append(content2 + "\n");
				}
			}

			futil.writeFile("e:/1/h2.txt", sb.toString());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();

		}

	}
	
	
	/*
	 * 读文件 "d:/1/fj.xlsx"
	 * */
	public  void readintodbs(ICellService ics,IPhoneNumService phoneNumService,String file) throws Exception {
		// 通过Workbook的静态方法getWorkbook选取Excel文件
		
		Workbook workbook = Workbook.getWorkbook(new File(file));
		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		System.out.println(rows);
		int cols = sheet.getColumns();
		System.out.println(cols);
		Cell cells[][] = new Cell[cols][rows];
		for (int i = 0; i < rows; i++) {	
			try{
			cells[0][i] = sheet.getCell(0, i);
			String phone = cells[0][i].getContents();
			String phoneId = phone.substring(0, 7);
			
//			cells[1][i] = sheet.getCell(1, i);
//			String province = cells[1][i].getContents();
			PhoneNum phoneNum = phoneNumService.findById(phoneId);
			String province ="";
			String yunyingshang = "";
			if(phoneNum==null){
				Phone_Base phonebase = new Phone_Base();
				phoneNum=phonebase.getPhoneBelong(phone,phoneNumService);
				if(phoneNum!=null){
					yunyingshang = phoneNum.getPtype();
					province = phoneNum.getProvince();
				}				
			}else{
				yunyingshang = phoneNum.getPtype();
				province = phoneNum.getProvince();
			}
			 
			
			Cells cells1 =  ics.findById(phone);
			if(cells1!=null){
//	
//				cells1.setPhone(phone);
//				cells1.setAge(province);
//				cells1.setBaby(yunyingshang);
//	
//				ics.update(cells1);
			}else{
				cells1 = new Cells();
				cells1.setPhone(phone);
				cells1.setAge(province);
				cells1.setBaby(yunyingshang);
				ics.saveCell(cells1);
			}
			
			
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		

		workbook.close();
	}

}