package com.gameportal.manage.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StringUtils {

	/**
	 * 将数据类型转换为 保留两位小数的
	 * @param vlaue 需要转换的数据
	 * @return 返回已转换的数据
	 */
	public static String convertNumber(double vlaue){
		BigDecimal decimal = new BigDecimal(vlaue);
		decimal = decimal.setScale(1, BigDecimal.ROUND_HALF_UP);
		NumberFormat af = new DecimalFormat("0.00");
		return af.format(decimal.doubleValue());
	}
	
	/**
	 * 格式不
	 * @param vlaue
	 * @return
	 */
	public static String convertNumber2(double vlaue){
		BigDecimal decimal = new BigDecimal(vlaue);
		decimal = decimal.setScale(1, BigDecimal.ROUND_HALF_UP);
		NumberFormat af = new DecimalFormat("0");
		return af.format(decimal.doubleValue());
	}
	
	/**
     * 将驼峰字符串改成加下划线的字符串（如：createTime改成create_time）
     * @param word
     */
    public static String convertToDownLine(String word) {
        if(word == null){
            return null;
        }
        String newStr = word;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isUpperCase(c)) {//如果是大写
                newStr = newStr.replaceAll(String.valueOf(c), "_" + Character.toLowerCase(c));
            }
        }
        return newStr;
    }
	
	public static void main(String[] args) {
		System.out.println(convertNumber2(67.60));
	}
}
