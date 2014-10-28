package com.lkb.warning;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.lkb.util.InfoUtil;

public class SendMailOnTime {

	public static Logger log = null;

	static {

		log = Logger.getLogger(SendMailOnTime.class);

	}

	public static void main(String[] args) {

		// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// java.util.Date date=new java.util.Date();
		// String dates=sdf.format(date);
		// sendAllMail( "京东登陆出问题了！时间："+date);
		// String email = "369768231@qq.com";
		// String[] toMail = {email};
		// String title = "aaaa";
		// sendMail(toMail, title,"eeeeeee");

		String emailName = InfoUtil.getInstance().getInfo("mail", "emailName");
		String title = "爬虫出问题了";
		String[] toMail = null;
		if (emailName != null && emailName.length() > 0) {
			toMail = emailName.split("###");
		}

		sendMail(toMail, title, "just test\n");
	}

	/**
	 * 
	 * @param args
	 */

	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	/**
	 * 获取本机IP地址，并自动区分Windows还是Linux操作系统
	 * 
	 * @return String
	 */
	public static String getHostIp() {
		String sIP = "";
		InetAddress ip = null;
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			}
			// 如果是Linux操作系统
			else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces
							.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有ip
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
								&& ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}

	public static void sendMail(String str) {

		MailBean mail = new MailBean();

		try {

			mail.sendMessage("smtp.126.com", "zhaoyulong0626@126.com",

			"rent information", str);

		} catch (MessagingException e) {

			System.out.println("mail send error !");

			log.debug("mail send error !", e);

			e.printStackTrace();

		}

		System.out.println("Mail have been sended .");

	}

	/**
	 * 
	 * 给一个指定邮箱发送指定的邮件内容 create date:2008-8-15 author:Administrator
	 * 
	 * 
	 * 
	 * @param str
	 */

	public static void sendMail(String toMail, String content) {

		MailBean mail = new MailBean();

		try {
			mail.sendMessage("103.244.235.12", "yulong.zhao@quantgroup.cn",
					toMail,

					"爬虫程序出问题了", content + "    ip:" + getHostIp());

		} catch (MessagingException e) {

			System.out.println("mail send error !");

			log.debug("mail send error !", e);

			e.printStackTrace();

		}

		System.out.println("Mail have been sended .");

	}

	/**
	 * 
	 * 指定目的邮箱数组进行群发 create date:2008-8-15 author:Administrator
	 * 
	 * 
	 * 
	 * @param toMail
	 * 
	 * @param content
	 */

	public static void sendMail(String[] toMail, String title, String content) {

		MailBean mail = new MailBean();

		try {
			String ip = InfoUtil.getInstance().getInfo("mail", "ip");
			String applyEmail = InfoUtil.getInstance().getInfo("mail",
					"applyEmail");
			mail.sendMessage(ip, applyEmail, toMail,

			title, content + "    ip:" + getHostIp());

		} catch (MessagingException e) {

			System.out.println("mail send error !");

			log.debug("mail send error !", e);

			e.printStackTrace();

		}

		System.out.println("Mail have been sended .");

	}

	// 真正发异常的email
	public static void sendAllMail(String content) {
		String emailName = InfoUtil.getInstance().getInfo("mail", "emailName");
		String title = "爬虫出问题了";
		String[] toMail = null;
		if (emailName != null && emailName.length() > 0) {
			toMail = emailName.split("###");
		}
		sendMail(toMail, title, content);
	}

}
