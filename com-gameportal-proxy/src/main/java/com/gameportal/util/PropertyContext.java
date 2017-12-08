package com.gameportal.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 对JAVA资源文件操作工具类
 */
public class PropertyContext {

	private Properties propertie;
	private String filePath;
	private String coding = "UTF-8";
	private static PropertyContext context = null;

	public static PropertyContext PropertyContextFactory(String pathFile) {
		if (null != context) {
			return context;
		} else {
			return new PropertyContext(pathFile);
		}
	}

	private PropertyContext() {
	}

	/**
	 * 初始化 propertie 对像
	 * 
	 * @param pathFile
	 */
	private PropertyContext(String pathFile) {
		// TODO Auto-generated constructor stub
		Properties prope;
		InputStream inputStream = null;
		try {
			URLConnection urlConnection = this.getFileCon(pathFile);
			if (null == urlConnection) {
				System.out.println("ERROR File:" + pathFile + " not find!");
				return;
			} else {
				inputStream = urlConnection.getInputStream();
			}
			if (null != inputStream) {
				prope = new Properties();
			} else {
				System.out.println("ERROR File:" + pathFile + " not find!");
				return;
			}
			prope.load(inputStream);
			this.propertie = prope;
			this.filePath = pathFile;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化 propertie 对像
	 * 
	 * @param pathFile
	 */
	public PropertyContext(String pathFile, String coding) {
		// TODO Auto-generated constructor stub
		Properties prope;
		InputStream inputStream = null;
		try {
			URLConnection urlConnection = this.getFileCon(pathFile);
			if (null == urlConnection) {
				System.out.println("ERROR File:" + pathFile + " not find!");
				return;
			} else {
				inputStream = urlConnection.getInputStream();
			}
			if (null != inputStream) {
				prope = new Properties();
			} else {
				System.out.println("ERROR File:" + pathFile + " not find!");
				return;
			}
			prope.load(inputStream);
			this.propertie = prope;
			this.filePath = pathFile;
			this.coding = coding;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 得到URLConnection 对像
	 * 
	 * @param filename
	 * @return URLConnection
	 */
	private URLConnection getFileCon(String filename) {
		try {
			Resource resource = new ClassPathResource(filename);

			if (resource.exists()) {
				return resource.getURL().openConnection();
			} else {
				System.out.println("ERROR File:" + filename + " not find!");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getValue(String key) {
		try {
			String value = this.propertie.getProperty(key);
			if (StringUtils.isNotBlank(value)) {
				byte[] bytes = value.getBytes("iso8859-1");
				String str = new String(bytes, this.coding);
				return str;
			}
			return null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String get(String key) {
		try {
			return this.propertie.getProperty(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Properties getPropertie() {
		return propertie;
	}
}
