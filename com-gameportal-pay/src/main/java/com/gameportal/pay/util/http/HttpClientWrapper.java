package com.gameportal.pay.util.http;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpException;

/**
 * 
 * ��װ��HttpClient ����������������Ľӿ�
 *  ���û� �������κ�HttpClient ��api �� 
 *  ֱ�ӵ��øýӿھͿ���ʵ����Ӧ�Ĳ���
 * 
 */
public interface HttpClientWrapper {
	/**
	 * ����Э��ͷ����Ϣ �û����Ը����Լ���������趨������ʹ��Ĭ�ϵ�����
	 * @param headers
	 */
	
	public void addHttpHeader( Map<String,String> headers);
		
	
	/**
	 * ���cookie��Ϣ
	 */
	public void clearCookie();

	/**
	 * ��һ��cookies�ӵ� httpclient �� 
	 * @param cookies
	 */
    
	public void addCookies(Cookie[] cookies);
   /**
    *  ���ӵ�����cookie
    * @param cookie
    */
	public void addCookie(Cookie cookie);

	/**
	 * 
	 * @param method
	 * @param url
	 * @param params
	 * @param charset
	 * @return ���ش����뼯���ַ���
	 * @throws HttpException
	 * @throws IOException
	 */
	public String doRequest(MethodType method, String url,
			Map<String, String> params, String charset) throws HttpException,
			IOException;
 /**
  * 
  * @param method
  * @param url
  * @param charset
  * @return ���ش����뼯�Ľ��
  * @throws HttpException
  * @throws IOException
  */
	public String doRequest(MethodType method, String url, String charset)
			throws HttpException, IOException;

	/**
	 * �޷���ֵ ʵ��HttpResponseCallBack�ӿ� �������д����ת����ֵ �ⲿ�������õ������õ���� ��Ҫ���ǵ��Ƕ��߳����ص����
	 * 
	 * @param method
	 * @param url
	 * @param params
	 * @param charset
	 * @param is
	 * @throws HttpException
	 * @throws IOException
	 */
	
	// HttpResponseCallBack �����õ�һ���ص��� ����Ҫ�ǿ�������httpClient ���ص��� �����ӹر�ʱ��Ҳ�ر���
	// �������ûص��ķ�ʽ�����ر�֮��Ƕ���û����룬��������
	public void doRequest(HttpResponseCallBack callback, MethodType method,
			String url, Map<String, String> params, String charset)
			throws HttpException, IOException;

	/**
	 * �޷���ֵ �ⲿ�������õ������õ���� ��Ҫ���ǵ��Ƕ��߳����ص����
	 * 
	 * @param method
	 * @param url
	 * @param charset
	 * @param ��callback
	 * @throws HttpException
	 * @throws IOException
	 */

	// HttpResponseCallBack �����õ�һ���ص��� ����Ҫ�ǿ�������httpClient ���ص��� �����ӹر�ʱ��Ҳ�ر���
	// �������ûص��ķ�ʽ�����ر�֮��Ƕ���û����룬��������
	public void doRequest(HttpResponseCallBack callback, MethodType method,
			String url, String charset) throws HttpException, IOException;

}
