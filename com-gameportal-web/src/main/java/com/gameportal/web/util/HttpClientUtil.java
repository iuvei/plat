package com.gameportal.web.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 利用HttpClient进行post请求的工具类(为了避免需要证书,忽略校验过程)。
 * 
 * @author sum
 *
 */
@SuppressWarnings("all")
public class HttpClientUtil {
	/**
	 * httpClient 提交post请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param headerMap
	 *            头部参数
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> headerMap) {
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet();
			httpGet.setURI(new URI(url));
			// 设置头部参数。
			if (headerMap != null) {
				Iterator iterator = headerMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry entry = (Entry) iterator.next();
					httpGet.setHeader(entry.getKey().toString(), entry.getValue().toString());
				}
			}
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, "UTF-8");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * httpClient 提交put请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param headerMap
	 *            头部参数
	 * @param parameterMap
	 *            包体参数
	 * @return
	 */
	public static String doPut(String url, Map<String, Object> headerMap, String param) {
		HttpClient httpClient = null;
		HttpPut httpPut = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPut = new HttpPut();
			httpPut.setURI(new URI(url));
			// 设置头部参数。
			if (headerMap != null) {
				Iterator iterator = headerMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry entry = (Entry) iterator.next();
					httpPut.setHeader(entry.getKey().toString(), entry.getValue().toString());
				}
			}
			StringEntity se = new StringEntity(param);
			se.setContentEncoding("UTF-8");
			se.setContentType("application/json");
			httpPut.setEntity(se);
			HttpResponse response = httpClient.execute(httpPut);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, "UTF-8");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * httpClient 提交post请求。
	 * 
	 * @param url
	 *            请求地址
	 * @param headerMap
	 *            头部参数
	 * @param parameterMap
	 *            包体参数
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> headerMap, String param) {
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			// 设置头部参数。
			if (headerMap != null) {
				Iterator iterator = headerMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry entry = (Entry) iterator.next();
					httpPost.setHeader(entry.getKey().toString(), entry.getValue().toString());
				}
			}
			// 设置包体参数。
			StringEntity se = new StringEntity(param);
			se.setContentEncoding("UTF-8");
			se.setContentType("application/json");
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, "UTF-8");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
