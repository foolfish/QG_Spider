package com.lkb.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.lkb.bean.PhoneNum;
import com.lkb.service.IPhoneNumService;
import com.lkb.thirdUtil.Phone_Base;

public class JxlRead {
	public static void main(String[] args) throws Exception {
		// 通过Workbook的静态方法getWorkbook选取Excel文件
		FileUtil futil = new FileUtil();
		Workbook workbook = Workbook.getWorkbook(new File("E:/1/0611.xls"));
		// 通过Workbook的getSheet方法选择第一个工作簿（从0开始）
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		int cols = sheet.getColumns();
		Cell cells[][] = new Cell[cols][rows];

		StringBuffer sb = new StringBuffer(100000000);
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				cells[i][j] = sheet.getCell(i, j);
				String phone = cells[i][j].getContents();
				System.out.println(phone+";");
				sb.append(phone+",");
			}
			
		}
		futil.writeFile("e:/1/h.txt", sb.toString());

		workbook.close();
	}

	public static void write(IPhoneNumService ps) throws BiffException {
		try {
			StringBuffer sb = new StringBuffer();
			String content2="";			
			FileUtil futil = new FileUtil();
			String files = futil.readFile("E:/1/h.txt");
			String[] strs= files.split(",");
			for(int i=0;i<strs.length;i++){
				String phone = strs[i];
				if (phone!=null&&phone.length() > 7) {
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
					 sb.append(content2+"\n");
				}
			}

		
			futil.writeFile("e:/1/h2.txt", sb.toString());


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}