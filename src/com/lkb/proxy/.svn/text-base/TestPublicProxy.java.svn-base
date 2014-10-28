package com.lkb.proxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lkb.framework.ThreadPoolManager;
import com.lkb.proxy.util.ProxyAuthentication;

/**
 * 抓取不同的网站建议
 */
@SuppressWarnings("deprecation")
public class TestPublicProxy {

	private static final AtomicInteger threadNum = new AtomicInteger(0);
	public static String fileName = "C:\\Users\\asus\\Desktop\\ip-list-xici.txt";
	public static String newFileName = "C:\\Users\\asus\\Desktop\\txt\\ip_new.txt";

	public List<String> readFile(String filename) {
		try {
			String lineTxt;
			List<String> ipList = new ArrayList<String>();
			File fin = new File(filename);
			if (!fin.exists())
				return null;
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					fin));// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			while ((lineTxt = bufferedReader.readLine()) != null) {
				if (lineTxt.length() > 5)
					ipList.add(lineTxt);
			}
			read.close();
			return ipList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean writeFile(String filename, List<String> ipList) {
		try {
			File f = new File(filename);
			if (f.exists()) {
				System.out.println("文件存在");
			} else {
				System.out.println("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			Iterator<String> iter = ipList.iterator();
			while (iter.hasNext()) {
				output.write(iter.next() + "\n");
			}
			output.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public String inputStream2String(InputStream in, String charset)
			throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[409600];
		int count = -1;
		while ((count = in.read(data, 0, 409600)) != -1)
			outStream.write(data, 0, count);
		data = null;
		return new String(outStream.toByteArray(), charset);
	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public String get(String url) {
		try {
			@SuppressWarnings("resource")
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpgets = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpgets);
			HttpEntity entity = response.getEntity();
			String str = convertStreamToString(entity.getContent());
			// String str = inputStream2String(entity.getContent(), charset);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String post(String url, Map<String, String> params) {
		try {
			@SuppressWarnings("resource")
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, params.get(key)));
			}
			try {
				post.setEntity(new UrlEncodedFormEntity(nvps));
			} catch (Exception e) {
				e.printStackTrace();
			}
			HttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			// String charset=EntityUtils.getContentCharSet(entity);
			String str = convertStreamToString(entity.getContent());
			// String str = inputStream2String(entity.getContent(), charset);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getIpXici() {
		try {
			List<String> ipList = new ArrayList<String>();
			String str = get("http://www.xici.net.co/nn/1"/* , "UTF-8" */);
			while (true) {
				Document doc = Jsoup.parse(str);
				Element table = doc.getElementById("ip_list");
				if(table==null)
					break;
				Elements trs = table.getElementsByTag("tr");
				if(trs==null)
					break;
				Iterator<Element> tr = trs.iterator();
				if (tr.hasNext())
					tr.next();
				while (tr.hasNext()) {
					Element tr_now = tr.next();
					// System.out.println(tr_now.child(2).text()+" "+tr_now.child(3).text());
					String time_str = tr_now.child(7).child(0).attr("title")
							.replaceAll("秒", "");
					float time = Float.parseFloat(time_str);
					// 只拿反应时间5秒一下的
					// if (time < 5)
					ipList.add(tr_now.child(2).text() + ":"
							+ tr_now.child(3).text());
					// System.out.println(time);
				}
				Element nextPage = doc.getElementsByClass("next_page").first();
				if (nextPage != null && nextPage.attr("href") != null
						&& nextPage.attr("href").length() > 0) {
					String href = "http://www.xici.net.co"
							+ nextPage.attr("href");
					System.out.println(href);
					str = get(href/* , "UTF-8" */);
				} else
					break;
			}
			writeFile(TestPublicProxy.fileName, ipList);
			return ipList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 站大爷port动态加密~妈蛋！
	public List<String> getIpZdaye() {
		try {
			List<String> ipList = new ArrayList<String>();
			String str = get("http://ip.zdaye.com/?ip=&port=&adr=&checktime=&sleep=1%C3%EB%C4%DA&cunhuo=&px=%B0%B4%CF%EC%D3%A6%CA%B1%BC%E4%C9%FD%D0%F2&nport=&nadr=&gb=2&dengji=&fgl=%B5%C8%D3%DA100&s1=1&s3=1&s4=1&s6=1&s9=1&s2=1&s7=1&s8=1&s10=1&s11=1&login=&daochu=&api=&ct=1000"/*
																																																																					 * ,
																																																																					 * "gb2312"
																																																																					 */);
			Document doc = Jsoup.parse(str);
			Element table = doc.getElementsByClass("ctable").first();
			Elements trs = table.getElementsByTag("tr");
			for (int i = 0; i < trs.size(); i++) {
				Element e = trs.get(i);
				if (e.hasAttr("class"))
					continue;
				System.out.println(e);
			}
			return ipList;
		} catch (Exception e) {
		}
		return null;
	}

	public List<String> getProxy360() {
		try {
			List<String> ipList = new ArrayList<String>();
			String str = get("http://www.proxy360.cn/Region/China"/* , "utf-8" */);
			Document doc = Jsoup.parse(str);
			Elements divs = doc.select("div[name=list_proxy_ip]");
			for (int i = 0; i < divs.size(); i++) {
				Elements spans = divs.get(i).getElementsByTag("span");
				System.out.println((spans.get(0).text() + ":" + spans.get(1)
						.text()));
			}
			return ipList;
		} catch (Exception e) {
		}
		return null;
	}

	public List<String> getDainer() {
		try {
			List<String> ipList = new ArrayList<String>();
			Map<String, String> params = new HashMap<String, String>();
			params.put("agreement", "HTTP");
			params.put("agreement", "HTTPS");
			params.put("agreement", "SOCKS4/5");
			params.put("anonymityLevel", "透明");
			params.put("anonymityLevel", "普通");
			params.put("anonymityLevel", "高匿");
			params.put("key", "");
			params.put("p", "");
			params.put("passWord", "");
			params.put("pp", "2");
			params.put("sort", "降序");
			params.put("sertBy", "date");
			params.put("userName", "");

			String str = post("http://dl.dainar.net/search/index", params);
			Document doc = Jsoup.parse(str);
			Elements divs = doc.select("div[name=list_proxy_ip]");
			for (int i = 0; i < divs.size(); i++) {
				Elements spans = divs.get(i).getElementsByTag("span");
				System.out.println((spans.get(0).text() + ":" + spans.get(1)
						.text()));
			}
			return ipList;
		} catch (Exception e) {
		}
		return null;
	}

	public void writeCanUseIp() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAverageResult(){
		List<String> list=new ArrayList<String>();
		list.addAll(readFile("C:\\Users\\asus\\Desktop\\txt\\ip-xici-time.txt"));
		list.addAll(readFile("C:\\Users\\asus\\Desktop\\txt\\ip-samair-time.txt"));
		long time_sum=0;
		int time_num=0;
		int[] a=new int[1000];
		for(int i=0;i<list.size();i++){
			String str=list.get(i);
			if(!str.contains("~0")){
				int time=Integer.parseInt(str.substring(str.indexOf("~")+1));
				a[time_num]=time;
				time_num+=1;
				time_sum+=time;
			}
		}
		for(int i=0;i<time_num;i++)
			for(int j=i+1;j<time_num;j++){
				if(a[i]>a[j]){
					int temp=a[i];
					a[i]=a[j];
					a[j]=temp;
				}
			}
		System.out.println("average:"+time_sum/time_num);
		System.out.println("max:"+a[time_num-1]);
		System.out.println("min"+a[0]);
		System.out.println("中位数:"+a[time_num/2]);
	}
	
	private static String[] getList(){
		String[] str={"","",""};
		return str;
	}
	
	static Map<String,Integer> MapList=new HashMap();
	static int[] httpList=new int[20000];
	static int[] httpsList=new int[20000];
	static long[] responseTime=new long[20000];
	
	public static void main(String[] args) {
		try {
			TestPublicProxy a = new TestPublicProxy();
//			a.getIpXici();
//			if(true)
//				return;
			//a.getAverageResult();
			// a.getProxy360();
			// a.getIpZdaye();
			// a.getIpXici();
//			ProxyAuthentication pro=new ProxyAuthentication();
//			pro.requestResponseTime("122.96.59.107", "81");
//			System.out.println(pro.getResponseTime());
//			List<String> list = a
//					.readFile("C:\\Users\\asusa\\Desktop\\txt\\ip-samair.txt");
//
//			List<String> canUseList = new ArrayList<String>();
//			Iterator<String> i = list.iterator();
//			int num = 0;
//			int sum=0;
//			while (i.hasNext()) {
//				String str = i.next();
//				sum++;
//				ProxyAuthentication pro = new ProxyAuthentication();
//				boolean isTrue = pro.requestResponseTime(
//						str.substring(0, str.indexOf(":")),
//						str.substring(str.indexOf(":") + 1));
//				canUseList.add(str + "~" + pro.getResponseTime());
//				System.out.println(sum+"   "+pro.getResponseTime());
//				if(sum>100)
//					break;
//			}
//			// a.getIp();
//			// 以下地址是代理服务器的地址
//			// a.writeCanUseIp();
//			a.writeFile(newFileName, canUseList);
			
			
			List<String> listRead=a.readFile("C:\\Users\\asus\\Desktop\\ip-list.txt");
			String str = listRead.toString();
			int j=str.indexOf("[");
			str=str.replaceAll("\\[", "");
			str=str.replaceAll("\\]", "");
			String[] ips = str.split(", ");
			for(int i=0;i<ips.length;i++){
				MapList.put(ips[i], i);
				if(i==0)
					str=ips[0];
				else
					str=str+","+ips[i];
			}

    
			
			exec(str);
	
			while(threadNum.get() > 0) {
				System.out.println(threadNum.get());
				char c=(char)System.in.read();
				if(c=='1')
					break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("Complete!!!!!!!!!\nComplete!!!!!!!!!\nComplete!!!!!!!!!\nComplete!!!!!!!!!\nComplete!!!!!!!!!\nComplete!!!!!!!!!\n");
			List<String> list=new ArrayList<String>();
			int http=0,https=0,http_https=0;
			for(int i=0;i<ips.length;i++){
				list.add(ips[i]+" "+httpList[i]+" "+httpsList[i]+" "+responseTime[i]+" "+(httpList[i]&httpsList[i]));
				if(httpList[i]==1 && httpsList[i]==0)
					http++;
				else if(httpList[i]==0 && httpsList[i]==1)
					https++;
				else if(httpList[i]==1 && httpsList[i]==1)
					http_https++;
			}
			System.out.println();
			System.out.println("http:"+http);
			System.out.println("https:"+https);
			System.out.println("http&https:"+http_https);
			a.writeFile("C:\\Users\\asus\\Desktop\\result.txt",list);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void exec(String str) {
		String[] ips = str.split("[,|]");
		for(String ip1 : ips) {
			final String[] ip2 = ip1.split(":");
			ThreadPoolManager.getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					threadNum.incrementAndGet();
					exec(ip2[0], ip2[1]);
					threadNum.decrementAndGet();
				}
			});
		}
	}
	
	private static void exec(String ip, String port) {
		ProxyAuthentication pa = new ProxyAuthentication();
		String x = ip + ":" + port;
		//System.out.println("------------" + x + "-------------");
		int k=MapList.get(ip+":"+port);
		boolean h = pa.authenticateHttps(ip, port);
		if (h) {
			httpsList[k]=1;
			responseTime[k]=pa.getResponseTime();
		}
		else
			httpsList[k]=0;
		System.out.println(x + ">>>>>Proxy Authenticate Https= " + h + " >>>>>Resp Time=" + pa.getResponseTime());
		h = pa.authenticateHttp(ip, port);
		if (h) {
			httpList[k]=1;
			responseTime[k]=pa.getResponseTime();
		}
		else
			httpList[k]=0;
		System.out.println(x + ">>>>>Proxy Authenticate Http = " + h + " >>>>>Resp Time=" + pa.getResponseTime());
	}
	
}
