package com.gameportal.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



/**
 * <p>
 * Http工具类
 * 
 * <p>
 * Http工具类，为系统提供通用Http访问操作方法：
 * 
 * <p>
 * 1、发送GET请求；
 * <p>
 * 2、发送POST请求。
 * 
 */
public class HttpUtil {

	private static Logger logger = Logger.getLogger(HttpUtil.class);

	/**
	 * <p>
	 * 发送GET请求
	 * 
	 * @param url
	 *            GET请求地址
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 */
	public static byte[] doGet(String url) {

		return HttpUtil.doGet(url, null, null, 0);
	}

	/**
	 * <p>
	 * 发送GET请求
	 * 
	 * @param url
	 *            GET请求地址
	 * @param headerMap
	 *            GET请求头参数容器
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 */
	public static byte[] doGet(String url, Map headerMap) {

		return HttpUtil.doGet(url, headerMap, null, 0);
	}

	/**
	 * <p>
	 * 发送GET请求
	 * 
	 * @param url
	 *            GET请求地址
	 * @param proxyUrl
	 *            代理服务器地址
	 * @param proxyPort
	 *            代理服务器端口号
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-03-19
	 */
	public static byte[] doGet(String url, String proxyUrl, int proxyPort) {

		return HttpUtil.doGet(url, null, proxyUrl, proxyPort);
	}

	/**
	 * <p>
	 * 发送GET请求
	 * 
	 * @param url
	 *            GET请求地址
	 * @param headerMap
	 *            GET请求头参数容器
	 * @param proxyUrl
	 *            代理服务器地址
	 * @param proxyPort
	 *            代理服务器端口号
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-03-19
	 */
	public static byte[] doGet(String url, Map headerMap, String proxyUrl,
			int proxyPort) {

		byte[] content = null;
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);

		if (headerMap != null) {

			// 头部请求信息
			if (headerMap != null) {

				Iterator iterator = headerMap.entrySet().iterator();
				while (iterator.hasNext()) {

					Entry entry = (Entry) iterator.next();
					getMethod.addRequestHeader(entry.getKey().toString(), entry
							.getValue().toString());
				}
			}
		}

		if (StringUtils.isNotBlank(proxyUrl)) {

			httpClient.getHostConfiguration().setProxy(proxyUrl, proxyPort);
		}

		// 设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
		// postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER ,
		// new DefaultHttpMethodRetryHandler());
		InputStream inputStream = null;
		try {

			if (httpClient.executeMethod(getMethod) == HttpStatus.SC_OK) {

				// 读取内容
				inputStream = getMethod.getResponseBodyAsStream();
				content = IOUtils.toByteArray(inputStream);
			} else {

				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
		} catch (IOException ex) {

			ex.printStackTrace();
		} finally {

			IOUtils.closeQuietly(inputStream);
			getMethod.releaseConnection();
		}
		return content;
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url
	 *            POST请求地址
	 * @param parameterMap
	 *            POST请求参数容器
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 */
	public static String doPost(String url, Map parameterMap) {

		return HttpUtil.doPost(url, null, parameterMap, null, null, 0);
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url
	 *            POST请求地址
	 * @param parameterMap
	 *            POST请求参数容器
	 * @param paramCharset
	 *            参数字符集名称
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-05-21
	 */
	public static String doPost(String url, Map parameterMap,
			String paramCharset) {

		return HttpUtil.doPost(url, null, parameterMap, paramCharset, null, 0);
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url
	 *            POST请求地址
	 * @param headerMap
	 *            POST请求头参数容器
	 * @param parameterMap
	 *            POST请求参数容器
	 * @param paramCharset
	 *            参数字符集名称
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-05-21
	 */
	public static String doPost(String url, Map headerMap, Map parameterMap,
			String paramCharset) {
		return HttpUtil.doPost(url, headerMap, parameterMap, paramCharset,
				null, 0);
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url
	 *            POST请求地址
	 * @param parameterMap
	 *            POST请求参数容器
	 * @param paramCharset
	 *            参数字符集名称
	 * @param proxyUrl
	 *            代理服务器地址
	 * @param proxyPort
	 *            代理服务器端口号
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 */
	public static String doPost(String url, Map parameterMap,
			String paramCharset, String proxyUrl, int proxyPort) {
		return HttpUtil.doPost(url, null, parameterMap, paramCharset, proxyUrl,
				proxyPort);
	}

	/**
	 * <p>
	 * 发送POST请求
	 * 
	 * @param url
	 *            POST请求地址
	 * @param headerMap
	 *            POST请求头参数容器
	 * @param parameterMap
	 *            POST请求参数容器
	 * @param paramCharset
	 *            参数字符集名称
	 * @param proxyUrl
	 *            代理服务器地址
	 * @param proxyPort
	 *            代理服务器端口号
	 * 
	 * @return 与当前请求对应的响应内容字节数组
	 * 
	 * @modify 窦海宁, 2012-05-21
	 */
	public static String doPost(String url, Map headerMap, Map parameterMap,
			String paramCharset, String proxyUrl, int proxyPort) {
		Date data1 = new Date();
		byte[] content = null;
		Long time = null;
		String returnBody = null;
		HttpClient httpClient = new HttpClient();
		httpClient.setTimeout(30000);
		PostMethod postMethod = new PostMethod(url);
		if (StringUtils.isNotBlank(paramCharset)) {
			postMethod.getParams().setContentCharset(paramCharset);
			postMethod.getParams().setHttpElementCharset(paramCharset);
		}
		if (headerMap != null) {
			// 头部请求信息
			if (headerMap != null) {
				Iterator iterator = headerMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry entry = (Entry) iterator.next();
					postMethod.addRequestHeader(entry.getKey().toString(),
							entry.getValue().toString());
				}
			}
		}
		if(parameterMap != null){
			Iterator iterator = parameterMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry entry = (Entry) iterator.next();
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				postMethod.addParameter(key, value);
			}
		}
		if (StringUtils.isNotBlank(proxyUrl)) {
			httpClient.getHostConfiguration().setProxy(proxyUrl, proxyPort);
		}
		// 设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 100000);
		// postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER ,
		// new DefaultHttpMethodRetryHandler());
		InputStream inputStream = null;
		try {

			if (httpClient.executeMethod(postMethod) == HttpStatus.SC_OK) {
				// 读取内容
				inputStream = postMethod.getResponseBodyAsStream();
				content = IOUtils.toByteArray(inputStream);
				returnBody = StringUtils.isNotBlank(ObjectUtils
						.toString(content)) ? new String(content, "UTF-8")
						: null;
				time =  new Date().getTime() - data1.getTime();
			} else {

				System.err.println("Method failed: "
						+ postMethod.getStatusLine());
			}
			logger.info("<|>310502<|>HttpUtil-doPost()<|>" + url + "/"
					+ headerMap + "/" + parameterMap + "/" + paramCharset
					+ "<|>" + proxyUrl + "/" + proxyPort + "<|>" + returnBody
					+ "<|>"+time+"<|>");
		} catch (IOException ex) {
			logger.error("<|>310502<|>HttpUtil-doPost()<|>" + url + "/"
					+ headerMap + "/" + parameterMap + "/" + paramCharset
					+ "<|>" + proxyUrl + "/" + proxyPort + "<|>" + ex
					+ "<|>"+time+"<|>", ex);
		} finally {
			IOUtils.closeQuietly(inputStream);
			postMethod.releaseConnection();
		}
		return returnBody;
	}


	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("wd", "nima");
		byte[] b = doGet("http://www.baidu.com", map);
		System.out.println("-------------------" + new String(b));
	}

}
