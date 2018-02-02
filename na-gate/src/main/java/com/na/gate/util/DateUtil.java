package com.na.gate.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andy
 * @version 创建时间：2017年10月23日 上午11:37:20
 */
public class DateUtil {
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Long getTimestamp(String date) {
		try {
			TimeZone zone = TimeZone.getTimeZone("GMT+08:00"); // 获取中国时区
			sdf.setTimeZone(zone);
			return sdf.parse(date).getTime();
		} catch (ParseException e) {
			logger.error("时间转换异常。",e);
		}
		return null;
	}
	
	public static Long getTimestamp(Date date) {
		try {
			String dateStr =sdf.format(date);
			TimeZone zone = TimeZone.getTimeZone("GMT+08:00"); // 获取中国时区
			sdf.setTimeZone(zone);
			return sdf.parse(dateStr).getTime();
		} catch (ParseException e) {
			logger.error("时间转换异常。",e);
		}
		return null;
	}
	
	public static String date2Str(Date date){
		return sdf.format(date);
	}
}
