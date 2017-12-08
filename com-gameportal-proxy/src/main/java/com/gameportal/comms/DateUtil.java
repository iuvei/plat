package com.gameportal.comms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期格式化类
 * @author Administrator 
 *
 */
public class DateUtil {

	// 初始化日志

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_PATTERN_S = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final String DATE_PATTERN_S2 = "yyyy-MM-dd HH:mm";

	/**
	 * yyyyMMddHHmmss
	 */
	public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/**
	 * yyyy-MM-dd
	 */
	public static final String DATE_PATTERN_D = "yyyy-MM-dd";
	
	/**
	 * yyyyMMdd
	 */
	public static final String DATE_PATTERN_D2 = "yyyyMMdd";
	public static final String DATE_PATTERN_Dy = "MM/dd/yyyy";
	
	public static final String DATE_PATTERN_M = "yyyyMM";

	public static final String DATE_PATTERN_D_CN = "yyyy年MM月dd日";
	public static final String DATE_PATTERN_D_EN = "dd/MM/yyyy";

	/**
	 * 将dateStr转换Date
	 * @param date 
	 * @param pattern 日期格式yyyy-MM-dd HH:mm:ss
	 * @return Date
	 */
	public static Date format(String dateStr,String pattern) {
		Date date = null;
		if (dateStr == null || "".equals(dateStr.trim())) {
			return date;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}
	
	/**
	 * 将Date转换String
	 * @param date 
	 * @param pattern 日期格式yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String format(Date date,String pattern) {
		String dateStr = null;
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		dateStr = sdf.format(date);
		return dateStr;
	}
	
	/**
	 * 把字符串转化为日期类型
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Date convert2Date(String dateStr, String pattern) {
		Date date = null;
		if (dateStr == null || "".equals(dateStr.trim())) {
			return date;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}

	

	/**
	 * 根据日期模式，返回需要的日期对象
	 * 
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public static String convert2Str(Date date, String pattern) {
		String dateStr = null;
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * 根据日期模式, 获取当前日期
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getCurrentDate(String pattern) {
		return convert2Str(new Date(), pattern);
	}

	/**
	 * 日期加一天的方法
	 * 
	 * @param date
	 * @return
	 */
	public static Date getAddDate(Date date, int step) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		// 设置日期
		c.setTime(date);
		// 日期加1
		c.add(Calendar.DATE, step);
		// 结果
		return c.getTime();
	}
	
	/**
	 * 日期加分钟
	 * @param date
	 * @param step
	 * @return
	 */
	public static Date getAddMinute(Date date,int step){
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		// 设置日期
		c.setTime(date);
		// 日期加1
		c.add(Calendar.MINUTE, step);
		// 结果
		return c.getTime();
	}
	
	/**
	 * 日期加小时
	 * @param date
	 * @param step
	 * @return
	 */
	public static Date getAddHour(Date date,int step){
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		// 设置日期
		c.setTime(date);
		// 日期加1
		c.add(Calendar.HOUR, step);
		// 结果
		return c.getTime();
	}

	/**
	 * 日期加多少天的方法2 
	 * @param specifiedDay 日期 格式yyyy-MM-dd
	 * @param step  天
	 * @return
	 */
    public static String getSpecifiedDayAfter(String specifiedDay,int step){  
    	String dayAfter = "";   
    	try{
	        Calendar c = Calendar.getInstance();  
	        Date date=null;  
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);  
            
            c.setTime(date);  
            int day=c.get(Calendar.DATE);  
	        c.set(Calendar.DATE,day+1);  
	        dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());  
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return dayAfter;  
 } 
	
	
	/**
	 * 当前系统下一个月份
	 * 
	 * @return
	 */
	public static Long getNextMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(GregorianCalendar.MONTH, 1);
		SimpleDateFormat theDate = new SimpleDateFormat(DATE_PATTERN_M);
		String dateString = theDate.format(cal.getTime());
		return Long.valueOf(dateString);
	}
	
	/**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }
    
    public static String getWeekOfDateEn(Date dt) {
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }
    
    /**
     * 设置区域时间
     * @param sdf SimpleDateFormat 日期格式
     */
    public static void setTimeZone(SimpleDateFormat sdf) {
    	sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }
    
    /**
     * 设置区域时间
     * @param sdf SimpleDateFormat 日期格式
     * @param area 区域
     */
    public static void setTimeZone(SimpleDateFormat sdf, String area) {
    	sdf.setTimeZone(TimeZone.getTimeZone(area));
    }
    
    /**
	 * 昨天的日期
	 * @param now 当前日期
	 */
     public static Date calcYesterday(String now) {
    	 GregorianCalendar calendar =  new  GregorianCalendar(); 
    	 calendar.add(GregorianCalendar.DATE,  - 1 );
    	 
        return calendar.getTime();
        
    }
    
     /**
      * 本周开始的日期
      * @param now
      * @return 
      */
     public static Date calcThisWeek(String now) {
    	 GregorianCalendar calendar =  new  GregorianCalendar(); 
         int  minus = calendar.get(GregorianCalendar.DAY_OF_WEEK) - 2 ;
         if (minus < 0 ) {
        	 return new Date();
        } else{
	        calendar.add(GregorianCalendar.DATE,  - minus);
	       return calendar.getTime();
        }
    }
    
     /**
      * 上周开始日期
      * @param now 当前日期
      * @return
      */
     public static Date calcLastWeekBegin(String now)  {
    	GregorianCalendar calendar =  new  GregorianCalendar(); 
    	 int  minus = calendar.get(GregorianCalendar.DAY_OF_WEEK) + 1 ;
         
	        calendar.add(GregorianCalendar.DATE, - minus);
        calendar.add(GregorianCalendar.DATE, - 4 );
        return calendar.getTime();
    }
     
     /**
      * 上周结束日期
      * @param now
      * @return
      */
     public static Date calcLastWeekEnd(String now)  {
    	 GregorianCalendar calendar =  new  GregorianCalendar(); 
         int  minus = calendar.get(GregorianCalendar.DAY_OF_WEEK) - 1 ;
         calendar.add(GregorianCalendar.DATE, - minus);
        return calendar.getTime();
    }
     
     /**
      * 上个月的结束日期
      * @param now 当前日期
      * @return
      */
     public static Date calcLastMonth(String now)  {
    	 GregorianCalendar calendar =  new  GregorianCalendar(); 
        calendar.set(calendar.get(GregorianCalendar.YEAR),calendar.get(GregorianCalendar.MONTH), 1 );
        calendar.add(GregorianCalendar.DATE,  - 1 );
        return calendar.getTime();
    }
     
     /**
      * 功能描述：返回时
      *
      * @param date
      *            日期
      * @return 返回时
      */
     public static int getHour(Date date) {
    	 Calendar calendar = Calendar.getInstance();
         calendar.setTime(date);
         return calendar.get(Calendar.HOUR_OF_DAY);
     }
     
    /**
     * 获取时时彩日期
     * @param spel
     * @return
     */
    public static String getSSCDate(Date params,int spel){
    	Date date = getAddMinute(params, spel);
    	return format(date,DateUtil.DATE_PATTERN_S2)+":00";
    }
    
    /**
     * 开始时间等于当前时间，
     * @param endtime 结束时间
     * @return 返回两个时间相差的秒数
     */
    public static long getTimeInMillis(String endtime){
    	Calendar nowDate=Calendar.getInstance(),endDate=Calendar.getInstance();
    	nowDate.setTime(new Date());//设置为当前系统时间 
		endDate.setTime(format(endtime, DATE_PATTERN_S));
		long timeNow=nowDate.getTimeInMillis();
		long timeOld=endDate.getTimeInMillis();
		long result = (timeOld-timeNow)/1000;
//		long m = result/1000;//将毫秒转换成秒
//		long f = m/60;//秒
//		System.out.println("相隔"+f+":"+m%60+ " 当前时间："+format(date, DATE_PATTERN_S));
    	return result;
    }
    
    public static void main(String[] args) {
		System.out.println(getTimeInMillis("2015-04-16 15:55:00"));
	}
     
}
