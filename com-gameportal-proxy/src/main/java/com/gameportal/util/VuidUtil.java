package com.gameportal.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @FileName：VuidUtil.java
 * @Copyright: Copyright (c)　中讯爱乐, 2012-2013
 * @Description：vuid 工具类
 * @author 
 * @version 1.0
 */
public class VuidUtil {
	private static Integer tmpIndex = 10000;

	/**
	 * 生成一个vuid,根据时间长整形和4位随机数生成
	 * 
	 * @return
	 */
	public static String createVuid() {
		int randomNumber = createRandomNumer();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String d = sf.format(new Date());
		return d.toString() + randomNumber + "";
	}

	/**
	 * 生成一个5位数的递增正整数
	 * 
	 * @return
	 */
	private static int createRandomNumer() {
		synchronized (tmpIndex) {
			if (tmpIndex > 99999)
				tmpIndex = 10000;
			tmpIndex++;
			return tmpIndex;
		}
	}
}
