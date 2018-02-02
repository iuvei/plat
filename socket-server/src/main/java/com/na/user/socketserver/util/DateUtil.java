package com.na.user.socketserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author alan
 * @date 2017年4月28日 上午10:12:16
 */
public class DateUtil {
	private static Logger log = LoggerFactory.getLogger(DateUtil.class);

	public final static String yyyyMMdd = "yyyyMMdd";
	
	public final static String yyyy_MM_ddHHMMss = "yyyy-MM-dd HH:mm:ss";

	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 获取指定日期当天开始的时间
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateBeginTime(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				Calendar calendar = Calendar.getInstance();
			    calendar.setTime(date);
			    calendar.set(Calendar.HOUR_OF_DAY, 0);
			    calendar.set(Calendar.MINUTE, 0);
			    calendar.set(Calendar.SECOND, 0);
				
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(calendar.getTime());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 获取指定日期当天结束的时间
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateEndTime(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				Calendar calendar = Calendar.getInstance();
			    calendar.setTime(date);
			    calendar.set(Calendar.HOUR_OF_DAY, 23);
			    calendar.set(Calendar.MINUTE, 59);
			    calendar.set(Calendar.SECOND, 59);
				
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(calendar.getTime());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 获取指定时间的几小时后
	 * @param date
	 * @param time
	 */
	public static Date nextHourDate(Date date, int time) {
		if(date == null) {
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, time);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定时间的几分钟后
	 * @param date
	 * @param time
	 */
	public static Date nextMinuteDate(Date date, int time) {
		if(date == null) {
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, time);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定时间的几秒钟后
	 * @param date
	 * @param time
	 */
	public static Date nextSecondDate(Date date, int time) {
		if(date == null) {
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, time);
		return calendar.getTime();
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
     * String类型日期转Date
     *
     * @param time
     * @return
     * @throws ParseException 
     */
    public static Date toDate(String source) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(source);
    }
    
}
