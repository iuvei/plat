package com.gameportal.pay.util;

import java.util.Random;

public class RandomUtil {

	private final static String[] codes = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
			"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

	private final static String[] numbers = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	public static String getRandomCode(int amount) {
		StringBuffer code = new StringBuffer();
		for (int i = 0; i < amount; i++) {
			Random random = new Random();
			code.append(codes[random.nextInt(codes.length)]);
		}
		return code.toString();
	}

	public static String getRandomNumber(int amount) {
		StringBuffer code = new StringBuffer();
		for (int i = 0; i < amount; i++) {
			Random random = new Random();
			code.append(numbers[random.nextInt(numbers.length)]);
		}
		return code.toString();
	}
}
