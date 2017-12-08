package com.gameportal.pay.util.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 
 * ʵ�� HttpClientWrapper �ӿڵ���
 * 
 */
public class HttpClientUtils implements HttpClientWrapper {
	// �̰߳�ȫ�� httpClient
	private HttpClient client = new HttpClient(
			new MultiThreadedHttpConnectionManager());

	// �̰߳�ȫ�� MAP ��cookie ��Ϣ����Ϊȫ�� ����������� ��ÿ������֮�䱣��cookie�Ĵ���
	private Map<String, String> cookies = new HashMap<String, String>();
	
	List<Header> headers = Collections.synchronizedList(new ArrayList<Header>());
	//������Э��ͷ��Ϣ
	
	// �չ�����ǲ�����֤�� HttpClient ����
	public HttpClientUtils() {
		super();
	}

	// ��username ��password ������ ��Ҫ��֤�� ������basic ����DIGEST ��ժҪ�� ��֤����һ��
	// ����httpclient ��װ�����֮��
	public HttpClientUtils(String userName, String passWord) {
		client.getState().setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(userName, passWord));
	}

	/**
	 * ���cookie ��Ϣ�ķ���ʵ��
	 */
	public void clearCookie() {
		client.getState().clearCookies();
	}

	/**
	 * ��֤ÿ�ε�cookie��Ϣ����������
	 * 
	 * @param cookies1
	 */

	private void setCookies(Cookie[] cookies1) {
		for (Cookie cookie : cookies1) {
			this.cookies.put(cookie.getName(), cookie.getValue());
		}
	}

	public String doRequest(MethodType method, String url,
			Map<String, String> params, String charset) throws HttpException,
			IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		doRequest(new HttpResponseCallBack() {
			public void processResponse(InputStream in) throws IOException {
				int b = -1;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				out.flush();
				out.close();
				in.close();
			}
		}, method, url, params, charset);

		return out.toString(charset);
	}

	/**
	 * �ر���������е���·����
	 * 
	 * @param method
	 */
	private void closeConnection(HttpMethod method) {
		method.releaseConnection();
	}

	/***
	 * �������������� ��Ҫ��������ص���� ���õĻص�����ʽ����Դ�ر�֮ǰ ���û�������
	 * 
	 */
	public void doRequest(HttpResponseCallBack callback, MethodType method,
			String url, Map<String, String> params, String charset)
			throws HttpException, IOException {

		HttpMethod httpMethod = null;
		switch (method) {
		// ����get����
		case GET:
			httpMethod = this.doGet(url, params, charset);
			break;

		// ����post����
		case POST:
			httpMethod = this.doPost(url, params, charset);
			break;
		// ����option����
		case OPTION:
			httpMethod = this.doOption(url, params, charset);
			break;
		// ����put����
		case PUT:
			httpMethod = this.doPut(url, params, charset);
			break;
		case TRACE:
			httpMethod = this.doTrace(url, params, charset);
			break;
		case DELETE:
			httpMethod = this.doDelete(url, params, charset);
			break;
		default:

		}
		InputStream is = httpMethod.getResponseBodyAsStream();
		callback.processResponse(is);
		is.close();
		this.closeConnection(httpMethod);
	}

	public String doRequest(MethodType method, String url, String charset)
			throws HttpException, IOException {

		return this.doRequest(method, url, null, charset);
	}

	public void doRequest(HttpResponseCallBack callback, MethodType method,
			String url, String charset) throws HttpException, IOException {

		this.doRequest(callback, method, url, null, charset);
	}

	/**
	 * ��Ҫ������Щ http ͷ����Ϣ ���ڶ�http ͷ���˽���һ���Ƚ��ѵ��� �������Ԥ�����ú� http ͷ����Ϣ
	 * 
	 * @return list
	 */
	private List<Header> getHeaders() {
       
		headers.add(new Header("Accept-Language", "zh-CN"));
		headers.add(new Header("Accept-Encoding", " gzip, deflate"));
		//headers.add(new Header("If-Modified-Since",
				//"Thu, 29 Jul 2004 02:24:49 GMT"));
		//headers.add(new Header("If-None-Match", "'3014d-1d31-41085ff1'"));
		headers
				.add(new Header(
						"Accept",
						"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash,"
								+ " application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));
		headers
				.add(new Header("User-Agent",
						" Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; InfoPath.2)"));
		headers.add(new Header("Connection", " Keep-Alive"));

		return headers;
	}
	public void addHttpHeader(Map<String, String> headers1) {
		 headers = getHeaders();
  		 Set<String> names = headers1.keySet();
  		 for(String name : names){
  			for(Header header:headers){
  				if(header.getName().equals(name)){
  					header.setValue(headers1.get(name));
  				}
  			}
  			headers.add(new Header(name,headers1.get(name)));
  		 }
	}

	/**
	 * post ����ʱ �Ѵ������Ĳ��� ���з�װ��
	 * 
	 * @param params
	 * @return NameValuePair[]
	 */
	private NameValuePair[] postParams(Map<String, String> params) {
		if (params == null || params.isEmpty())
			return new NameValuePair[0];

		Set<String> paramNames = params.keySet();
		int i = 0;
		NameValuePair[] nameValuePairs = new NameValuePair[paramNames.size()];
		for (String paramName : paramNames) {
			i++;
			NameValuePair nameValuePair = new NameValuePair(paramName, params
					.get(paramName));
			nameValuePairs[i - 1] = nameValuePair;
		}

		return nameValuePairs;
	}

	/***
	 * get ����ʱ ������в��� �����ⷽ�� ���ɴ�������url
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private String createNewUrl(String url, Map<String, String> params) {

		if (params == null || params.isEmpty())
			return url;

		Set<String> names = params.keySet();
		int i = 0;
		for (String name : names) {
			i++;
			String value = params.get(name);
			if (i == 1) {
				url += "?" + name + "=" + value;
			} else {
				url += "&" + name + "=" + value;
			}
		}
		return url;
	}

	/**
	 * ��map����ĵ� cookie ��Ϣ��� http Э��ͷ�����cookie���ַ���
	 * 
	 * @param cookies
	 * @return String
	 */
	private String cookieStr(Map<String, String> cookies) {
		if (cookies == null || cookies.isEmpty())
			return "";

		String cookieStr = "";
		int i = 0;
		Set<String> cookieNames = cookies.keySet();
		for (String cookie : cookieNames) {
			i++;
			if (i == 1) {
				cookieStr = cookieStr + cookie + "=" + cookies.get(cookie);
			} else {
				cookieStr = cookieStr + ";" + cookie + "="
						+ cookies.get(cookie);
			}
		}
		return cookieStr;
	}

	/**
	 * ����get ����
	 * 
	 * @param url
	 * @param params
	 * @param cookies
	 * @param charset
	 * @return HttpMethod ��Ҫ�ǿ��ǵ�������ж���
	 * @throws HttpException
	 * @throws IOException
	 */

	private HttpMethod doGet(String url, Map<String, String> params,
			String charset) throws HttpException, IOException {
		// �����µ�url
		String newUrl = createNewUrl(url, params);

		// ����get ����
		GetMethod get = new GetMethod(newUrl);
		// ����cookie��Ϣ
		get.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		get.setRequestHeader("Cookie", this.cookieStr(cookies));
		// ����Э��ͷ�Ĳ����ı��뼯
		client.getParams().setContentCharset(charset);
		// ����Э��ͷ
		client.getHostConfiguration().getParams().setParameter(
				"http.default-headers", headers);

		// ����get���� �쳣ֱ���׳�
		executeHttpMethod(get);
		// System.out.println(get.getResponseBodyAsStream());
		return get;
	}

	/**
	 * post ���� ����post��put ����HttpClient ��֧���Զ��ض��� �������ǵ��ֶ����ض����ҳ��
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return HttpMethod
	 * @throws HttpException
	 * @throws IOException
	 */

	private HttpMethod doPost(String url, Map<String, String> params,
			String charset) throws HttpException, IOException {
		PostMethod post = new PostMethod(url);
		// ����Э��ͷ�Ĳ����ı��뼯
		client.getParams().setContentCharset(charset);
		// ����Э��ͷ
		client.getHostConfiguration().getParams().setParameter(
				"http.default-headers", headers);
		// ����post����ĵĲ���
		post.setRequestBody(this.postParams(params));
		// ����cookie��Ϣ
		post.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		post.setRequestHeader("Cookie", this.cookieStr(cookies));
		int status = executeHttpMethod(post);
		if (isRedirected(status)) {

			// ��ȡ���ع����� URL��Ϣ
			Header locationHeader = post.getResponseHeader("location");
			// ���� HttpClient ��post put ������֧�� �Զ��ض��� ��������Ҫ�Լ� �ֶ��ض���
			// ��get �������� �����Զ��ض���
			String newUrl = "";
			if (locationHeader != null) {
				// ��Э��ͷ���ȡ��Ҫ�ض����url
				newUrl = locationHeader.getValue();

			} else {
				// ���û���򷵻�Ĭ�ϵ�ҳ��
				newUrl = "/";
			}
			// ����get���� ������newurl
			GetMethod get = new GetMethod(newUrl);
			// ����get ���� �ֶ��ض���
			get.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			get.setRequestHeader("Cookie", this.cookieStr(cookies));
			executeHttpMethod(get);
			return get;
		} else {
			return post;
		}

	}

	/**
	 * �����Ƿ��ض���
	 * 
	 * @param status
	 * @return
	 */
	private boolean isRedirected(int status) {
		return status == HttpStatus.SC_MOVED_TEMPORARILY
				|| status == HttpStatus.SC_MOVED_PERMANENTLY
				|| status == HttpStatus.SC_TEMPORARY_REDIRECT
				|| status == HttpStatus.SC_USE_PROXY
				|| status == HttpStatus.SC_NOT_MODIFIED
				|| status == HttpStatus.SC_SEE_OTHER;
	}

	/**
	 * ����method���� ������֮���cookie��Ϣ������һ�� �����ڸ�������֮��ͷ�������cookie��Ϣһ��
	 * 
	 * @param httpMethod
	 * @return status
	 * @throws HttpException
	 * @throws IOException
	 */
	private int executeHttpMethod(HttpMethod httpMethod) throws HttpException,
			IOException {
		int status = this.client.executeMethod(httpMethod);
		this.setCookies(client.getState().getCookies());
		return status;
	}

	/**
	 * ����option ����
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private HttpMethod doOption(String url, Map<String, String> params,
			String charset) {
		return null;
	}

	/**
	 * ����delete ����
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */

	private HttpMethod doDelete(String url, Map<String, String> params,
			String charset) {
		return null;
	}

	/**
	 * ����put ����
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private HttpMethod doPut(String url, Map<String, String> params,
			String charset) {
		return null;
	}

	/**
	 * ����trace ����
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private HttpMethod doTrace(String url, Map<String, String> params,
			String charset) {
		return null;
	}

	public void addCookie(Cookie cookie) {
		client.getState().addCookie(cookie);
		Cookie[] cookies = client.getState().getCookies();
		this.setCookies(cookies);
	}

	public void addCookies(Cookie[] cookies1) {
		client.getState().addCookies(cookies1);
		Cookie[] cookies = client.getState().getCookies();
		this.setCookies(cookies);
	}

//	public static void main(String[] args) throws HttpException, IOException{
//		HttpClientUtils httpClientUtils = new HttpClientUtils();
//		String responseStr = httpClientUtils.doRequest(MethodType.POST, "https://ehkpay.ehking.com/gateway/controller.action", "GBK");
//		
//	}
}