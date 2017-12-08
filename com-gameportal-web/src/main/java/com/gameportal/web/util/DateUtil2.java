package com.gameportal.web.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
public class DateUtil2 {
	// 初始化日志

		public static final String DATE_PATTERN_S = "yyyy-MM-dd HH:mm:ss";

		public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

		public static final String DATE_PATTERN_D = "yyyy-MM-dd";
		public static final String DATE_PATTERN_Dy = "MM/dd/yyyy";
		
		public static final String DATE_PATTERN_M = "yyyyMM";

		public static final String DATE_PATTERN_D_CN = "yyyy年MM月dd日";
		public static final String DATE_PATTERN_D_EN = "dd/MM/yyyy";

		public static Date format(String dateStr) {
			Date date = null;
			if (dateStr == null || "".equals(dateStr.trim())) {
				return date;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_S);
			
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				date = null;
			}
			return date;
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

		public static String format(Date date) {
			String dateStr = null;
			if (date == null) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_D);
			//sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			dateStr = sdf.format(date);
			return dateStr;
		}
		
		public static String format2(Date date) {
			String dateStr = null;
			if (date == null) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_S);
			dateStr = sdf.format(date);
			return dateStr;
		}
		
		public static String format2(Date date,String pattern) {
			String dateStr = null;
			if (date == null) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			dateStr = sdf.format(date);
			return dateStr;
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
		 * 小时加减
		 * 
		 * @param date
		 * @return
		 */
		public static Date getAddHour(Date date, int step) {
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
		
		public static void main(String[] args) {
			System.out.println(format2(getAddHour(new Date(),1)));
		}
		
		/**
		 * 分钟加减
		 * @param date
		 * @param step
		 * @return
		 */
		public static Date getAddMinute(Date date, int step){
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
		 * 秒钟加减
		 * @param date
		 * @param step
		 * @return
		 */
		public static Date getAddSecond(Date date, int step){
			if (date == null) {
				return null;
			}
			Calendar c = Calendar.getInstance();
			// 设置日期
			c.setTime(date);
			// 日期加1
			c.add(Calendar.SECOND, step);
			// 结果
			return c.getTime();
		}
		
		/**
		 * 计算两个日期相差的时间
		 * @param startTime 开始日期
		 * @param endTime 结束日期
		 * @param format 格式
		 * @param str h 返回相差的小时，m返回相差的分钟，s返回相差的秒 否则返回相差的天数
		 * @return
		 */
		public static Long dateDiff(String startTime, String endTime,   
	            String format, String str) {   
	        // 按照传入的格式生成一个simpledateformate对象   
	        SimpleDateFormat sd = new SimpleDateFormat(format);   
	        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数   
	        long nh = 1000 * 60 * 60;// 一小时的毫秒数   
	        long nm = 1000 * 60;// 一分钟的毫秒数   
	        long ns = 1000;// 一秒钟的毫秒数   
	        long diff;   
	        long day = 0;   
	        long hour = 0;   
	        long min = 0;   
	        long sec = 0;   
	        // 获得两个时间的毫秒时间差异   
	        try {   
	            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();   
	            day = diff / nd;// 计算差多少天   
	            hour = diff % nd / nh + day * 24;// 计算差多少小时   
	            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟   
	            sec = diff % nd % nh % nm / ns;// 计算差多少秒   
	            // 输出结果   
//	            System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"  
//	                    + (min - day * 24 * 60) + "分钟" + sec + "秒。");   
//	            System.out.println("hour=" + hour + ",min=" + min);   
	            if (str.equals("h")) {   
	                return hour;   
	            } else if(str.equals("m")) {   
	                return min;   
	            }else if(str.equals("s")){
	            	return sec;
	            }else if(str.equals("ms")){
	            	return (min*60)+sec;
	            }else{
	            	return day;
	            }
	  
	        } catch (ParseException e) {   
	            // TODO Auto-generated catch block   
	            e.printStackTrace();   
	        }   
	        if (str.equalsIgnoreCase("h")) {   
	            return hour;   
	        } else {   
	            return min;   
	        }   
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
	     public static Date calcYesterday() {
	    	 GregorianCalendar calendar =  new  GregorianCalendar(); 
	    	 calendar.add(GregorianCalendar.DATE,  - 1 );
	    	 
	        return calendar.getTime();
	        
	    }
	    
	     /**
	      * 本周开始的日期
	      * @param now
	      * @return 
	      */
	     public static Date calcThisWeek() {
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
	     public static Date calcLastWeekBegin()  {
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
	     public static Date calcLastWeekEnd()  {
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
	      * 获取本月第一天
	      * @return
	     * @throws ParseException 
	      */
	     public static String getFirstDay(String now) throws ParseException {
	         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	         Date theDate = df.parse(now);
	         GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
	         gcLast.setTime(theDate);
	         gcLast.set(Calendar.DAY_OF_MONTH, 1);
	         return df.format(gcLast.getTime());

	     }
	     
	     /**
	      * 获取最后一天
	      * @param now
	      * @return
	      * @throws ParseException
	      */
	     public static String getEndDay(String now) throws ParseException{
	    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	 Calendar calendar = Calendar.getInstance(); 
	    	 calendar.setTime(df.parse(now));
	    	 final int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	    	 Date lastDate = calendar.getTime();
	    	 lastDate.setDate(lastDay);
	    	 return df.format(lastDate);
	     }
	     
	     public static Date getUpMonthDay(){
	    	 Calendar c = Calendar.getInstance();
	         c.add(Calendar.MONTH, -1);
	         return c.getTime();
	     }
	     
	   
	     
	     
	     /**
	      * 功能描述：返回小
	      *
	      * @param date
	      *            日期
	      * @return 返回小时
	      */
	     public static int getHour(Date date) {
	    	 Calendar calendar = Calendar.getInstance();
	         calendar.setTime(date);
	         return calendar.get(Calendar.HOUR_OF_DAY);
	     }

	     
	     /**
	      * 功能描述：返回分
	      *
	      * @param date
	      *            日期
	      * @return 返回分钟
	      */
	     public static int getMinute(Date date) {
	    	 Calendar calendar = Calendar.getInstance();
	         calendar.setTime(date);
	         return calendar.get(Calendar.MINUTE);
	     }
	     
	     /**
	      * 返回年月日
	      * @param value
	      * @return
	      */
	     public static int getYMd(String value){
	    	 Calendar calendar = Calendar.getInstance();
	         calendar.setTime(new Date());
	         if("Y".equals(value)){
	        	 return calendar.get(Calendar.YEAR);
	         }else if("M".equals(value)){
	        	 return calendar.get(Calendar.MONTH)+1;
	         }else if("D".equals(value)){
	        	 return calendar.get(Calendar.DATE);
	         }else{
	        	 return calendar.get(Calendar.HOUR_OF_DAY);
	         }
	     }
	     
	     public static int getYMd(String value,String now) throws ParseException{
	    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	 Calendar calendar = Calendar.getInstance();
	         calendar.setTime(df.parse(now));
	         if("Y".equals(value)){
	        	 return calendar.get(Calendar.YEAR);
	         }else if("M".equals(value)){
	        	 return calendar.get(Calendar.MONTH)+1;
	         }else if("D".equals(value)){
	        	 return calendar.get(Calendar.DATE);
	         }else{
	        	 return calendar.get(Calendar.HOUR_OF_DAY);
	         }
	     }
	     
	     /**
	 	 * 日期相减
	 	 * @param date 截至日期
	 	 * @param date1 开始日期
	 	 * @return
	 	 */
	 	 public static int diffDate(Date endDate,Date startDate) {
	 		  return (int) ((getMillis(endDate) - getMillis(startDate)) / (24 * 3600 * 1000));
	 	 }
	 	 
	 	/**
		  * 返回日期毫秒
		  * @param date
		  * @return
		  */
		  public static long getMillis(java.util.Date date) {
		  Calendar c = java.util.Calendar.getInstance();
		  c.setTime(date);
		  return c.getTimeInMillis();
		  }
	     
	    
}
