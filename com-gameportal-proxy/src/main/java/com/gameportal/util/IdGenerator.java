package com.gameportal.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: IdGenerator
 * @Description: (订单号生成规则)
 * @date 2015年4月12日 下午12:22:53
 */
public class IdGenerator {
	/**
	 * 订单号（生成规则：取系统配置文件的instance号2位+12位currentTimeMillis/10(10毫秒)+2位流水号）
	 */
	private static int i = 0;
	private static String instanceId = null;

	public static String getInstanceId() {
		return instanceId;
	}

	public static void setInstanceId(String instanceId) {
		if (instanceId == null || instanceId.length() != 2) {
			throw new IllegalArgumentException(
					"Set instanceId error: the length should be 2 '"
							+ (instanceId == null ? "" : instanceId)
							+ "' is not correct.");
		}
		IdGenerator.instanceId = instanceId;
	}

	/**
	 * @DESCRIPION :每10毫秒可生成100个序列号；优于每毫秒10个序列号
	 * @Create on: 2013-4-16 下午5:27:05
	 * @Author : "Jack"
	 * @return : String
	 */
	public synchronized static String genOrdId16(String instanceId) {
		return genOrdId16(instanceId, null);
	}

	public synchronized static String genOrdId16(String instanceId, Integer code) {
		String time = StringUtils.isNotBlank(ObjectUtils.toString(code)) ? "yyyyMMddHHmmssSSS"
				: "yyyyMMddHHmmss";
		i = i % 100;
		String index = (i < 10) ? ("0" + i) : "" + i;
		String orderNum = instanceId
				+ new SimpleDateFormat(time).format(new Date()) + index;
		i++;
		return orderNum;
	}

	public static String getTmie(Integer code) {
		String time = StringUtils.isNotBlank(ObjectUtils.toString(code)) ? "yyyyMMddHHmmssSSS"
				: "yyyyMMddHHmmss";
		return new SimpleDateFormat(time).format(new Date());
	}
	
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }    
	
	/**
	 * 返回制定长度的纯数字
	 * @param length
	 * @return
	 */
	public static String getRandomNumber(int length) { //length表示生成字符串的长度  
	    String base = "0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }

	public static void main(String[] args) {
		System.err.println(genOrdId16("OKSL"));
	}
}
