package com.na.user.socketserver.util;

/**
 * 类型转换工具类
 * 
 * @author alan
 * @date 2017年4月28日 上午11:17:09
 */
public class ConvertUtil {
	
	public static String toStr(Object obj) {
		if (obj != null&& !"".equals(obj)) {
			return obj.toString();
		}
		return null;
	}

	public static String toStr(Object obj, Object defaultVal) {
		if (obj != null && !"".equals(obj)) {
			return obj.toString();
		}
		return defaultVal.toString();
	}
	
	public static int toInt(Object obj) {
		if (obj != null && !"".equals(obj)) {
			return Integer.parseInt(obj.toString());
		}
		return 0;
	}
	
	public static int toInt(Object obj, int defaultValue) {
		if (obj != null&& !"".equals(obj)) {
			return Integer.parseInt(obj.toString());
		}
		return defaultValue;
	}
	
	public static long toLong(Object obj) {
		if (obj != null&& !"".equals(obj)) {
			return Long.parseLong(obj.toString());
		}
		return 0;
	}
	
	public static long toLong(Object obj, long defaultValue) {
		if (obj != null && !"".equals(obj)) {
			return Long.parseLong(obj.toString());
		}
		return defaultValue;
	}
	
	public static double toDouble(Object obj) {
		if (obj != null&& !"".equals(obj)) {
			return Double.parseDouble(obj.toString());
		}
		return 0;
	}
	
	public static double toDouble(Object obj, double defaultValue) {
		if (obj != null&& !"".equals(obj)) {
			return Double.parseDouble(obj.toString());
		}
		return defaultValue;
	}

}
