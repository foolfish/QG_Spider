package com.lkb.util;
import java.io.File;   
import java.io.FileOutputStream;   
import java.io.InputStream;   
import java.io.OutputStream;   
import java.net.URL;   
import java.net.URLConnection; 
import java.util.HashSet;
import java.util.Set;
public class DownloadImage {
	  /**  
     * @param args  
     * @throws Exception   
     */  
    public static void main(String[] args) throws Exception {   
        // TODO Auto-generated method stub   
         download("https://authcode.jd.com/verify/image?a=1&acid=eaae3bda-e31b-4e1d-be5b-6d9bda8c5a90&uid=eaae3bda-e31b-4e1d-be5b-6d9bda8c5a90", "jd.gif","F:\\workspace\\lkb\\WebContent\\img");   
    }   
       
    public static void download(String urlString, String filename,String savePath) throws Exception {   
        // 构造URL   
        URL url = new URL(urlString);   
        // 打开连接   
        URLConnection con = url.openConnection();   
        //设置请求超时为5s   
        con.setConnectTimeout(5*1000);   
        // 输入流   
        InputStream is = con.getInputStream();   
       
        // 1K的数据缓冲   
        byte[] bs = new byte[1024];   
        // 读取到的数据长度   
        int len;   
        // 输出的文件流   
       File sf=new File(savePath);   
       if(!sf.exists()){   
           sf.mkdirs();   
       }   
       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);   
        // 开始读取   
        while ((len = is.read(bs)) != -1) {   
          os.write(bs, 0, len);   
        }   
        // 完毕，关闭所有链接   
        os.close();   
        is.close();   
    }    

}
