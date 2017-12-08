package com.gameportal.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 读取属性文件工具类。
 * 
 * @author sum
 *
 */
public class GetProperty {
	private static final Logger logger = Logger.getLogger(GetProperty.class);

	public static Properties getProp(String name) {
		String path =GetProperty.class.getResource("/").getPath() + name;
		Properties prop = new Properties();
		try {
			File file = new File(path);
			if (!file.exists()) {
				logger.info("读取不到配置文件" + path);
			}
			prop.load(new FileInputStream(file));
			file = null;
		} catch (Exception e) {
			logger.error("解析文件错误", e);
		}
		return prop;
	}

	// 方法一：通过java.util.ResourceBundle读取资源属性文件
	public static String getPropertyByName(String path, String name) {
		String result = "";
		try {
			// 方法一：通过java.util.ResourceBundle读取资源属性文件
			result = java.util.ResourceBundle.getBundle(path).getString(name);
		} catch (Exception e) {
			logger.error("解析文件错误", e);
		}
		return result;
	}

	// 方法二：通过类加载目录getClassLoader()加载属性文件
	public static String getPropertyByName2(String path, String name) {
		String result = "";
		// 方法二：通过类加载目录getClassLoader()加载属性文件
		InputStream in = GetProperty.class.getClassLoader().getResourceAsStream(path);
		Properties prop = new Properties();
		try {
			prop.load(in);
			result = prop.getProperty(name).trim();
		} catch (IOException e) {
			logger.error("解析文件错误", e);
		}
		return result;
	}
}
