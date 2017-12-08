package com.gameportal.comms.http;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求父类
 * @author brooke
 *
 */
public class BaseHttpRequest {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseHttpRequest.class);
	
	// 连接超时值
	protected int connectTimeout = -1;
	// 读取超时值
	protected int readTimeout = -1;

	/**
	 * 
	 * @return 获取当前的链接超时值
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 *  设置连接超时值
	 *  
	 * @param connectTimeout 新的连接超时值（毫秒），-1代表不设置
	 */
	public void setConnectTimeout( int connectTimeout ) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 
	 * @return 获取当前的读取超时值
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 *  设置读取超时值
	 *  
	 * @param readTimeout 新的读取超时值（毫秒）， -1 代表不设置
	 */
	public void setReadTimeout( int readTimeout ) {
		this.readTimeout = readTimeout;
	}
	
	public BaseHttpRequest(){
		
	}
	
	/**
	 * 构造函数
	 * 
	 * @param connectTimeout 连接超时值（毫秒），-1代表不设置
	 * @param readTimeout 读取超时值（毫秒），-1代表不设置
	 */
	public BaseHttpRequest( int connectTimeout, int readTimeout ) {
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	/**
	 * 关闭对象。
	 * 
	 * @param f 待关闭的对象
	 */
	protected void close( Closeable f ) {
		if ( f != null ){
			try {
				f.close();
			} catch ( IOException ex) {
				logger.error(ex.toString());
			}
		}
	}
	
	/**
	 * 获取URL
	 * @param url
	 * @return
	 */
	protected URL toURL (String url ) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			logger.info(e.toString());
		}
		return null;
	}
}
