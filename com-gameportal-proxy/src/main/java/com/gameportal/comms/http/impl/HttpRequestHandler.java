package com.gameportal.comms.http.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gameportal.comms.exception.OpensnsException;
import com.gameportal.comms.http.BaseHttpRequest;
import com.gameportal.comms.http.IHttpRequestHandler;
import com.gameportal.comms.http.utils.ErrorCode;

public class HttpRequestHandler extends BaseHttpRequest implements IHttpRequestHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);
	
	public HttpRequestHandler(){
		super();
	}
	
	/**
	 * 构造函数
	 * 
	 * @param connectTimeout 连接超时值（毫秒），-1代表不设置
	 * @param readTimeout 读取超时值（毫秒），-1代表不设置
	 */
	public HttpRequestHandler( int connectTimeout, int readTimeout ) {
		super(connectTimeout, readTimeout);
	}
	
	@Override
	public String request(String methodUrl, String params, String method,
			String encoding) throws OpensnsException {
		logger.info("请求URL："+methodUrl);
		logger.info("请求参数："+params);
		logger.info("请求类型："+method);
		logger.info("数据编码："+encoding);
		HttpURLConnection conn = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			// 构造连接
			conn = (HttpURLConnection) toURL(methodUrl).openConnection();
			
			//设置一个指定的超时值（以毫秒为单位）
			if ( connectTimeout != -1 ) {
				conn.setConnectTimeout( connectTimeout );
			}
			//将读超时设置为指定的超时值，以毫秒为单位。
			if ( readTimeout != -1 ) {
				conn.setReadTimeout( readTimeout );
			}
			conn.setRequestMethod(method);
			conn.setRequestProperty( "Content-type", "text/html;charset="+encoding );
			conn.setDoOutput( true );
			conn.connect();
			
			if(null != params && !"".equals(params)){
				// 写入POST数据
				out = conn.getOutputStream();
				out.write( params.getBytes( encoding ) );
			}
			//读取服务器响应
			in = conn.getInputStream();
			Reader reader = new BufferedReader( new InputStreamReader( in, encoding) );
			StringBuilder buffer = new StringBuilder();
			char[] buf = new char[1000];
			int len = 0;
			while ( len >= 0 ) {
				buffer.append( buf, 0, len );
				len = reader.read( buf );
			}
			return buffer.toString();
		}catch(IOException e){
			throw new OpensnsException(ErrorCode.NETWORK_ERROR, methodUrl+"访问失败",e);
		}finally{
			close( in );
			close( out );
			if ( conn != null ){
				conn.disconnect();
			}
		}
	}

}
