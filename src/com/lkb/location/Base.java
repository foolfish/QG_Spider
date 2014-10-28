package com.lkb.location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.lkb.util.InfoUtil;

public abstract class Base {
	private static Logger logger = Logger.getLogger(Base.class);

	public void goWhere(String ptype, String currentUser, Model model) {

		if (ptype.contains("移动")) {
			mobile(currentUser, model);
		} else if (ptype.contains("联通")) {
			unicom(currentUser, model);
		} else {
			telecom(currentUser, model);
		}
	}

	public CloseableHttpClient closeableHttpClient(String pathKey, String passwordKey) {

		KeyStore store;
		CloseableHttpClient httpClient = null;
		FileInputStream instream = null;
		try {
			store = KeyStore.getInstance(KeyStore.getDefaultType());
			String path = InfoUtil.getInstance().getInfo("road", pathKey);
			instream = new FileInputStream(new File(path));
			try {
				String password = InfoUtil.getInstance().getInfo("road", passwordKey);
				store.load(instream, password.toCharArray());
			} finally {
				instream.close();
			}
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(store).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

			CookieStore cookieStore = new BasicCookieStore();
			HttpClientContext localContext = HttpClientContext.create();
			localContext.setCookieStore(cookieStore);

			LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

			httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setRedirectStrategy(redirectStrategy).setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			logger.info("第345行捕获异常："  , e);
			e.printStackTrace();
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				logger.info("第352行捕获异常："  , e);
				e.printStackTrace();
			}
		}
		return httpClient;

	}

	public abstract void mobile(String currentUser, Model model);

	public abstract void unicom(String currentUser, Model model);

	public abstract void telecom(String currentUser, Model model);

}
