package com.lkb.warning;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

import com.lkb.util.InfoUtil;

public class MailBean {

	public static Logger log=null;

    static{

       log = Logger.getLogger(MailBean.class);

    }

    

    // smtpHost发件人所用到的smtp服务器

    String smtpHost = "smtp.126.com";

    // from发件人邮箱

    String from = "zhaoyulong0626@126.com";

    // to收件人邮箱

    String to = "369768231@qq.com";

    // subject邮件标题

    String subject = "receive a mail from zhaoyulong0626@126.com";

    // theMessage邮件内容

    StringBuffer theMessage = new StringBuffer();

 

    /**

     * 固定的给369768231@qq.com,yulong.zhao@quantgroup.cn发送邮件

     * create date:2008-8-15

     * author:Administrator

     *

     * @param smtpHost

     * @param from

     * @param subject

     * @param messageText

     * @throws MessagingException

     */

    public void sendMessage(String smtpHost, String from,

           String subject, String messageText) throws MessagingException {

       SmtpAuth sa = new SmtpAuth();

       sa.getuserinfo("zhaoyulong0626", "chaoren0626");

       java.util.Properties props = new java.util.Properties();

       props.put("mail.smtp.auth", "true");

       props.put("mail.smtp.host", smtpHost);

       System.out.println("Constructing message- from=" + from + " to=" + to);

       InternetAddress fromAddress = new InternetAddress(from);

       InternetAddress[] toAddresss = new InternetAddress[2];

       toAddresss[0] = new InternetAddress("369768231@qq.com");

       toAddresss[1] = new InternetAddress("yulong.zhao@quantgroup.cn");

       int i = 0;

       while (i < toAddresss.length) {

           Session mailSession = Session.getDefaultInstance(props, sa);

           MimeMessage testMessage = new MimeMessage(mailSession);

           testMessage.setFrom(fromAddress);

           testMessage.addRecipient(javax.mail.Message.RecipientType.TO,

                  toAddresss[i]);

           testMessage.setSentDate(new java.util.Date());

           testMessage.setSubject(subject);

           testMessage.setText(messageText);

 

           Transport.send(testMessage);

           System.out.println("A mail have been sent!");

           i++;

       }

    }

 

    /*

     * 由126服务器向目的邮箱发送邮件

     * 邮件发送处理 @param stmHost,from,to,subject,messageText

     */

 

    public void sendMessage(String smtpHost, String from, String to,

           String subject, String messageText) throws MessagingException {

       SmtpAuth sa = new SmtpAuth();

       sa.getuserinfo("yulong.zhao@quantgroup.cn", "lkb201418");

       java.util.Properties props = new java.util.Properties();

       props.put("mail.smtp.auth", "true");

       props.put("mail.smtp.host", smtpHost);

       System.out.println("Constructing message- from=" + from + " to=" + to);

       InternetAddress fromAddress = new InternetAddress(from);

       InternetAddress toAddresss = new InternetAddress(to);

       

       

           Session mailSession = Session.getDefaultInstance(props, sa);

           MimeMessage testMessage = new MimeMessage(mailSession);

           testMessage.setFrom(fromAddress);

           testMessage.addRecipient(javax.mail.Message.RecipientType.TO,

                  toAddresss);

           testMessage.setSentDate(new java.util.Date());

           testMessage.setSubject(subject);

           testMessage.setText(messageText);

 

           Transport.send(testMessage);

           System.out.println("A mail have been sent to "+ to);

           

    }

 

    /**

     * 功能：群发功能,把所有的目的邮箱作为一个数组参数传入

     * create date:2008-8-15

     * author:Administrator

     *

     * @param smtpHost

     * @param from

     * @param to 目的邮箱数组

     * @param subject

     * @param messageText

     * @throws MessagingException

     */

    public void sendMessage(String smtpHost, String from, String[] to,

           String subject, String messageText) throws MessagingException {

       SmtpAuth sa = new SmtpAuth();
       String applyEmail = InfoUtil.getInstance().getInfo("mail", "applyEmail");
//       String[] emails = applyEmail.split("@");
//       String userName = emails[0];
       String password = InfoUtil.getInstance().getInfo("mail", "password");
       sa.getuserinfo(applyEmail, password);

       java.util.Properties props = new java.util.Properties();

       props.put("mail.smtp.auth", "true");

       props.put("mail.smtp.host", smtpHost);

       System.out.println("Constructing message- from=" + from + " to=" + to);

       InternetAddress fromAddress = new InternetAddress(from);

       

       InternetAddress[] toAddresss = new InternetAddress[to.length];

       for(int len=0;len<to.length;len++){
    	   if(to[len]!=null && to[len].length()>1){
    		   toAddresss[len] = new InternetAddress(to[len]);
    	   } 

       }

       

       int i = 0;

       while (i < toAddresss.length) {
    	   if(toAddresss[i]!=null){
    		   Session mailSession = Session.getDefaultInstance(props, sa);

               MimeMessage testMessage = new MimeMessage(mailSession);

               testMessage.setFrom(fromAddress);

               testMessage.addRecipient(javax.mail.Message.RecipientType.TO,

                      toAddresss[i]);

               testMessage.setSentDate(new java.util.Date());

               testMessage.setSubject(subject);

               testMessage.setText(messageText);

     

               Transport.send(testMessage);

               System.out.println("A mail have been sent to "+to[i]); 
    	   }
    	   i++;

       }

    }

    /*

     * 邮件用户名和密码认证

     */

    static class SmtpAuth extends javax.mail.Authenticator {

       private String user, password;

 

       public void getuserinfo(String getuser, String getpassword) {

           user = getuser;

           password = getpassword;

       }

 

       protected javax.mail.PasswordAuthentication getPasswordAuthentication() {

           return new javax.mail.PasswordAuthentication(user, password);

       }

    }

 

}

