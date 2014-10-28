package com.lkb.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class FileUtil {
	public static String readFile(String filePathAndName) { 
		  String fileContent = ""; 
		  try {  
		   File f = new File(filePathAndName); 
		   if(f.isFile()&&f.exists()){ 
		    InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8"); 
		    BufferedReader reader=new BufferedReader(read); 
		    String line; 
		    while ((line = reader.readLine()) != null) { 
		    	System.out.println(line);
		     fileContent += line; 
		    }   
		    read.close(); 
		   } 
		  } catch (Exception e) { 
		   System.out.println("读取文件内容操作出错"); 
		   e.printStackTrace(); 
		  } 
		  return fileContent; 
		} 
	
	public static void writeFile(String filePathAndName, String fileContent) throws IOException { 
		 BufferedWriter writer = null; 
		try { 
		   File f = new File(filePathAndName); 
		   if (!f.exists()) { 
		    f.createNewFile(); 
		   } 
		   OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8"); 
		   writer=new BufferedWriter(write);   
		   writer.write(fileContent); 
		   writer.close(); 
		  } catch (Exception e) { 
		   System.out.println("写文件内容操作出错"); 
		   e.printStackTrace(); 
		  }finally{
			  if(writer!=null){
				  writer.close();
			  }
		  } 
		} 
	 /**
     * A方法追加文件：使用RandomAccessFile
     */
    public static void appendMethodA(String fileName, String content) {
        try {
        	File f = new File(fileName); 
 		   if (!f.exists()) { 
 		    f.createNewFile(); 
 		   } 
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     */
    public static void appendMethodB(String fileName, String content) {
        try {
        	File f = new File(fileName); 
  		   if (!f.exists()) { 
  		    f.createNewFile(); 
  		   } 
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void method1(String file, String conent) {    
        BufferedWriter out = null;    
        try {    
        	File f = new File(file); 
   		   if (!f.exists()) { 
   			   f.createNewFile(); 
   		   } 
            out = new BufferedWriter(new OutputStreamWriter(    
                    new FileOutputStream(file, true)));    
            out.write(conent);    
        } catch (Exception e) {    
            e.printStackTrace();    
        } finally {    
            try {    
                out.close();    
            } catch (IOException e) {    
                e.printStackTrace();    
            }    
        }    
    }    


	public static void main(String[] args) throws IOException{
//		appendMethodB("E:/workspace/tools/doc/1.txt","ddd\n");
//		appendMethodB("E:/workspace/tools/doc/1.txt","fff\n");
//		appendMethodB("E:/workspace/tools/doc/1.txt","eee\n");
//		appendMethodB("E:/workspace/tools/doc/1.txt","yyy\n");
//		method1("E:/workspace/tools/doc/5.txt","ddddddddddddddddddddddddddddd");
		System.out.println(readFile("D:/2/data.txt"));
	}
}
