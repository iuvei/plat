package com.gameportal.pay.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

@SuppressWarnings("static-access")
public class CoderUtil {
	/**
	 * 解密
	 * 
	 * @param pwd
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static byte[] decodeStr(byte[] key) {
		Base64 base64 = new Base64();
		byte[] debytes = base64.decodeBase64(key);
		return debytes;
	}

	/**
	 * 加密
	 * 
	 * @param pwd
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static byte[] encodeStr(byte[] key) {
		Base64 base64 = new Base64();
		byte[] enbytes = base64.encodeBase64(key);
		return enbytes;
	}
}
