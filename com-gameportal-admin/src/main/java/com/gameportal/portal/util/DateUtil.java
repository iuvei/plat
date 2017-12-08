package com.gameportal.portal.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT_SECOND = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String TIME_FORMAT_MEDIA = "yyyyMMddHHmmss";
	public static final String TIME_FORMAT_DATE = "yyyyMMdd";

	/**
	 * 根据时间字符串返回Date对象
	 * 
	 * @param dateStr,可以接受3种格式分别是:yyyy-MM-dd,yyyy-MM-dd
	 *            HH:mm,yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date getDateByStr(String dateStr) {
		SimpleDateFormat formatter = null;
		if (dateStr.length() == 10)
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		else if (dateStr.length() == 16)
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		else if (dateStr.length() == 19)
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else if(dateStr.length() == 8)//补充日期格式
		  formatter = new SimpleDateFormat("yyyyMMdd");
		else {
			System.out.println("日期字符串格式错误!");
			return null;
		}
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回日期的字符串
	 * 
	 * @param date
	 *            Date对象
	 * @param format
	 *            例如:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getStrByDate(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * 返回日期的字符串,年-月-日
	 * 
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String getStrYMDByDate(Date date) {
		return getStrByDate(date, "yyyy-MM-dd");
	}

	/**
	 * 返回日期的字符串,
	 * 
	 * @param date
	 * @return yyyy年MM月dd日
	 */
	public static String getStrChineseYMDByDate(Date date) {
		String str = getStrByDate(date, "yyyy-MM-dd");
		str = str.replaceFirst("-", "年");
		str = str.replaceFirst("-", "月");
		str += "日";
		return str;
	}

	/**
	 * 返回日期的字符串,时:分:秒
	 * 
	 * @param date
	 * @return HH:mm:ss
	 */
	public static String getStrHMSByDate(Date date) {
		return getStrByDate(date, "HH:mm:ss");
	}

	/**
	 * 返回日期的字符串,年-月-日 时:分:秒
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getStrYMDHMSByDate(Date date) {
		return getStrByDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回日期的字符串,年月日 时:分
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String getStrYMDHMByDate(Date date) {
		return getStrByDate(date, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 对天数进行加减运算
	 * 
	 * @param date
	 *            原来的时间
	 * @param days
	 *            正数为加,负数为减
	 * @return 返回运算后的时间
	 */
	public static Date addDay(Date date, Integer days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * 对月数进行加减运算
	 * 
	 * @param date
	 *            原来的时间
	 * @param days
	 *            正数为加,负数为减
	 * @return 返回运算后的时间
	 */
	public static Date addMonth(Date date, Integer months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}

	/**
	 * 返回中文时间格式
	 * 
	 * @param object
	 *            可以为Date对象或2007-06-12格式的字符串
	 * @return
	 */
	public static String toChinese(Object object) {
		String dateStr = null;
		if (object instanceof Date)
			dateStr = getStrYMDByDate((Date) object);
		else if (object instanceof String)
			dateStr = (String) object;
		else
			return dateStr;
		String[] cnArray = { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		String year = dateStr.split("-")[0];
		String month = dateStr.split("-")[1];
		String date = dateStr.split("-")[2];
		dateStr = "";
		for (int i = 0; i < year.length(); i++)
			dateStr += cnArray[Integer.valueOf(String.valueOf(year.charAt(i)))];
		dateStr += "年";
		if ("10".equals(month))
			dateStr += "十";
		else {
			int num = Integer.valueOf(String.valueOf(month.charAt(1)));
			if ("0".equals(String.valueOf(month.charAt(0))))
				dateStr += cnArray[num];
			else
				dateStr += "十" + cnArray[num];
		}
		dateStr += "月";
		if ("10".equals(date))
			dateStr += "十";
		else {
			String temp = String.valueOf(date.charAt(0));
			if ("1".equals(temp))
				dateStr += "十";
			else if ("2".equals(temp))
				dateStr += "二十";
			else if ("3".equals(temp))
				dateStr += "三十";
			if (!"0".equals(String.valueOf(date.charAt(1))))
				dateStr += cnArray[Integer.valueOf(String.valueOf(date.charAt(1)))];
		}
		dateStr += "日";
		return dateStr;
	}

	/**
	 * 返回星期几
	 * 
	 * @param object
	 *            Date对象或者字符串,yyyy-MM-dd
	 * @return 星期五
	 */
	@SuppressWarnings("deprecation")
	public static String getWeek(Object object) {
		Date date = null;
		if (object instanceof Date)
			date = (Date) object;
		else if (object instanceof String)
			date = getDateByStr((String) object);
		else
			return "";
		String[] cnWeek = { "日", "一", "二", "三", "四", "五", "六" };
		return "星期" + cnWeek[date.getDay()];
	}

	public static Date get00_00_00Date(Date date) {
		return getDateByStr(getStrYMDByDate(date));
	}

	public static Date get23_59_59Date(Date date) {
		return getDateByStr(getStrYMDHMSByDate(date).substring(0, 10) + " 23:59:59");
	}

	public static Integer changeSecond(String hms) {
		if (hms == null || "".equals(hms)) {
			return null;
		}

		String[] t = hms.split(":");
		int hour = Integer.valueOf("0" + t[0]);
		int min = Integer.valueOf("0" + t[1]);
		int sec = Integer.valueOf("0" + t[2]);

		return hour * 3600 + min * 60 + sec;
	}

	/**
	 * 返回多少时间前
	 * 
	 * @param date
	 * @return
	 */
	public static String getPreTime(Date date) {
		long time = date.getTime();
		Long result = 0L;
		result = (new Date().getTime() - time);
		result = result / 1000L;
		if (result < 60L) {
			return result + "秒前";
		}
		if (result >= 60L && result < 3600L) {
			result = result / 60;
			return result + "分钟前";
		}
		if (result >= 3600L && result < 86400L) {
			result = result / 3600;
			return result + "小时前";
		} else {
			result = result / 3600 / 24;
			return result + "天前";
		}
	}

	/**
	 * 取传入日期后的第一个星期一(包括传入的日期)
	 * 
	 * @param startTime
	 * @return
	 */
	public static Date getFirstMondayAfter(Date startTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			cal.add(Calendar.DATE, 1);
		}
		return cal.getTime();
	}

	/**
	 * 当前年
	 * 
	 * @return
	 */
	public static int getCurrYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 当前月
	 * 
	 * @return
	 */
	public static int getCurrMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 当前日
	 * 
	 * @return
	 */
	public static int getCurrDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 年
	 * 
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 月
	 * 
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 日
	 * 
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取传入时间的当月第一日
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	public static String getFirstDayStr(Date now) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(now);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        return df.format(gcLast.getTime());

    }

	/**
	 * 取传入时间的当月最后一日
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}

	public static String getFristTime(String yearStr, String monthstr) {
		if ((null != yearStr && !"".equals(yearStr.trim()))
				&& (null != monthstr && !"".equals(monthstr.trim()))) {
			return yearStr + "-" + monthstr + "-" + "01 00:00:00";
		}
		return null;
	}

	public static String getLastTime(String yearStr, String monthstr) {
		if ((yearStr == null || "".equals(yearStr))
				|| (monthstr == null || "".equals(monthstr))) {
			return null;
		}
		int month = Integer.parseInt(monthstr);
		String LastTime = null;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			LastTime = yearStr + "-" + monthstr + "-" + "31 59:59:59";
		} else if (month == 2) {
			int year = Integer.parseInt(yearStr);
			if (year % 4 == 0) {
				LastTime = yearStr + "-" + monthstr + "-" + "29 59:59:59";
			} else {
				LastTime = yearStr + "-" + monthstr + "-" + "28 59:59:59";
			}
		} else {
			LastTime = yearStr + "-" + monthstr + "-" + "30 59:59:59";
		}
		return LastTime;
	}

	public static String FormatTimeS(java.util.Date date) {
		if (date == null) {
			return "";
		}
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("SS");
			return fmt.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static String FormatYear(java.util.Date date) {
		if (date == null) {
			return "";
		}
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
			return fmt.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static String FormatMonth(java.util.Date date) {
		if (date == null) {
			return "";
		}
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("MM");
			return fmt.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static String FormatDay(java.util.Date date) {
		if (date == null) {
			return "";
		}
		String str = FormatDate(date);

		try {
			str = str.substring(str.lastIndexOf("-") + 1, str.lastIndexOf("-") + 3);
			return str;
		} catch (Exception e) {
			return "";
		}
	}

	public static String FormatDate(java.util.Date date) {
		if (date == null)
			return "";
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			return fmt.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	public static String inHTMLConvert(String str) {
		if (str != null && str != "" && !str.equalsIgnoreCase("")) {
			str = str.replaceAll("&amp;", "&");
			str = str.replaceAll("&lt;", "<");
			str = str.replaceAll("&gt;", ">");
			str = str.replaceAll("&quot;", "\"");
			return str;
		}
		return null;
	}
	public static Date getDateByUsa(Date chinaDate){
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(chinaDate);
		calDateA.add(Calendar.HOUR, -12);
		return calDateA.getTime();
	}
	public static Date toChinaDate(Date usaDate){
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(usaDate);
		calDateA.add(Calendar.HOUR, 12);
		return calDateA.getTime();
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
	
	public static void main(String[] args) {
		//System.out.println(getStrByDate(new Date(), DateUtil.TIME_FORMAT_DATE));
		System.out.println(getStrHMSByDate(new Date()));
		//System.out.println(getStrByDate(getAddDate(new Date(),-2),"yyyy-MM-dd"));
	}
}