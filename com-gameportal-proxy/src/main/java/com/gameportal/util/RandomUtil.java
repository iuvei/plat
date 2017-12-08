package com.gameportal.util;



import java.util.Random;

public class RandomUtil {
	
	private final static String[] codes = {"0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k" , "l", "m", "n", "o", "p", "q", "r",
		"s", "t", "u", "v", "w", "x", "y", "z"};
	
	private final static String[] numbers = {"0", "1", "2", "3", "4", "5","6", "7", "8", "9"};
	
	private StringBuffer code;
	
	public String getRandomCode(int amount){
		code = new StringBuffer();
		for(int i=0; i<amount; i++){
			Random random = new Random();
			code.append(codes[random.nextInt(codes.length)]);
		}
		return code.toString();
	}
	
	public String getRandomNumber(int amount){
		code = new StringBuffer();
		for(int i=0; i<amount; i++){
			Random random = new Random();
			code.append(numbers[random.nextInt(numbers.length)]);
		}
		return code.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = new RandomUtil().getRandomCode(5);
		System.out.println(s);
	}

}
