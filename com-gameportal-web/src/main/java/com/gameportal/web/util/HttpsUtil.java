package com.gameportal.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.log4j.Logger;

/**
 * Https请求工具类。
 * 
 * @author sum
 *
 */
public class HttpsUtil {
	private static Logger logger = Logger.getLogger(HttpsUtil.class);

	public HttpsUtil(){
		//sun.net.www.protocol.http.HttpURLConnection
	}
	public static String processRequst(String requestUrl, String key) {
		try {
			URL url = new URL(requestUrl);
			HttpsURLConnection yc = (HttpsURLConnection)url.openConnection();
			KeyStore ks = KeyStore.getInstance("PKCS12");
//			String keyPath = "E:/ptplay.p12";
//			keyPath = keyPath.replaceFirst("/", "");
			String keyPath = HttpsUtil.class.getResource("/").getPath()+"key/ptplay.p12";
			File file = new File(keyPath);
			FileInputStream fis = new FileInputStream(file);
			ks.load(fis, "iQ3xuZrS".toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, "iQ3xuZrS".toCharArray());
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			yc.setRequestProperty("X_ENTITY_KEY", key);
			yc.setSSLSocketFactory(sc.getSocketFactory());
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			StringBuffer result = new StringBuffer();
			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
			}
			in.close();
			fis.close();
			return result.toString();
		} catch (Exception e) {
			logger.error("HTTPS REQUEST ERROR,请求地址:" + requestUrl, e);
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String loginname = "QB7test";
		String password = "123456";
		String kisoskname = "VBETCNYQB7";
		String adminname = "VBETCNYQB7";

		//String requestUrl = "https://kioskpublicapi.redhorse88.com/player/create/playername/" + loginname.toUpperCase() + "/kioskname/" + kisoskname
				//+ "/adminname/" + adminname + "/password/" + password;
		String requestUrl = "https://kioskpublicapi.redhorse88.com/player/info/playername/"+loginname.toUpperCase();
		System.out.println(processRequst(requestUrl,
				"526b3aadd71d04c348390d80947fd59457239b309658e5a4ada3e6e958bcb4d66980283dbee5152e158709501dae3e23a7e9ab2e6376b2ea28b780600c24b32b"));
	}
}
