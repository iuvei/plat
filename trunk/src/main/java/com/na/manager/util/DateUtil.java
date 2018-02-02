package com.na.manager.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author Administrator
 *
 */
public class DateUtil {

	/**
	 * 字符串化为日期，格式：yyyy-MM-dd
	 *
	 * @param strDate
	 * @return
	 */
	public static Date parseDate(Serializable strDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;

		try {
			date = format.parse(strDate.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}

	/**
	 * 字符串化为long，格式：yyyy-MM-dd HH:mm:ss
	 *
	 * @param strDate
	 * @return
	 */
	public static long parseDates(String strDate) {
		Date date = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = dateFormat.parse(strDate.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	/**
	 * 字符串化为日期，格式：yyyy-MM-dd HH:mm:ss
	 *
	 * @param strDate
	 * @return
	 */
	public static Date DateWithSecond(Serializable strDate) throws ParseException {
		return parseDate(strDate, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 字符串化为日期
	 *
	 * @param strDate
	 *            转化的日期格式
	 * @return
	 */
	public static Date parseDate(Serializable strDate, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(strDate.toString());
	}

	/**
	 * 日期化为字符串
	 *
	 * @param time
	 * @param format
	 *            转化的日期格式
	 * @return
	 */
	public static String string(Date time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);
	}

	/**
	 * @param lo
	 *            毫秒数
	 * @return String yyyy-MM-dd HH:mm:ss
	 * @Description: long类型转换成日期
	 */
	public static String longToDate(long lo) {
		Date date = new Date(lo);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(date);
	}

	/**
	 * @param lo
	 *            毫秒数
	 * @return String yyyy-MM-dd HH:mm:ss
	 * @Description: long类型转换成日期
	 */
	public static String longToDate(long lo, String format) {
		Date date = new Date(lo);
		SimpleDateFormat sd = new SimpleDateFormat(format);
		return sd.format(date);
	}

	/**
	 * 日期化为字符串，格式：yyyy-MM-dd
	 *
	 * @param time
	 * @return
	 */
	public static String string(Date time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(time);
	}

	/**
	 * 日期化为字符串，格式：yyyyMMdd
	 *
	 * @param time
	 * @return
	 */
	public static String string(long time) {
		String dateFormats = "yyyyMMdd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormats);
		return dateFormat.format(new Date(time));
	}

	/**
	 * 返回一周前的日期
	 *
	 * @param now
	 * @return
	 */
	public static Date recentWeek(Date now) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_WEEK, -7);
		return calendar.getTime();
	}

	/**
	 * 返回一月前的日期
	 *
	 * @param now
	 * @return
	 */
	public static Date recentMonth(Date now) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	/**
	 * 返回半年前的日期
	 *
	 * @param now
	 * @return
	 */
	public static Date recentHalfYear(Date now) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.MONTH, -6);
		return calendar.getTime();
	}

	/**
	 * 返回一年前的日期
	 *
	 * @param now
	 * @return
	 */
	public static Date recentYear(Date now) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.YEAR, -1);
		return calendar.getTime();
	}
	
	/**
	 * 获取当前时间前一小时
	 * @param now
	 * @return
	 */
	public static String getOneHoursAgoTime(Date now, String format) {
		String oneHoursAgoTime = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR, Calendar.HOUR - 1);
		oneHoursAgoTime = new SimpleDateFormat(format).format(cal.getTime());
		return oneHoursAgoTime;
	}
}
