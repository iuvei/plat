package com.gameportal.web.util;

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
	 * 加密用户帐号供前台展示
	 * @param account
	 * @return
	 */
	public static String encryptAccount(String account){
		if(account == null){
			return "******";
		}
		return account.substring(0, 1) + "****"
		 + account.substring(account.length()-1,account.length());
	}
	
	public static void main(String[] args) {
		System.out.println(convertNumber2(67.60));
	}
}
