package com.na.user.socketserver.util.des;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Random;

/**
 * 3DES(DESede) + 字符串 反转 随机 加密类
 */
public class EncryptKit {
	private static final Logger log = LoggerFactory.getLogger(EncryptKit.class);
	private static final byte[] Key = new byte[] { 0x09, 0x35, 0x11, 0x18, 0x78, 0x3f, 0x7c, 0x5d, 0x71, 0x44, 0x29, 0x5b, 0x31, 0x40, 0x18, 0x7d, 0x21, 0x64, 0x29, 0x53, 0x5b, 0x40, 0x55, 0x4c };
	private static final String[] rans = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "g", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private static final int qIndex = 8;
	private static final int hIndex = 6;
	private static final String Algorithm = "DESede"; // DES,DESede,Blowfish

	private static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return Base64Kit.encode(c1.doFinal(src)).getBytes();
		} catch (java.security.NoSuchAlgorithmException e1) {
			log.error(e1.getMessage(),e1);
		} catch (javax.crypto.NoSuchPaddingException e2) {
			log.error(e2.getMessage(),e2);
		} catch (Exception e3) {
			log.error(e3.getMessage(),e3);
		}
		return null;
	}

	private static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(Base64Kit.decode(new String(src)));
		} catch (Exception e3) {
			log.error(e3.getMessage(),e3);
		}
		return null;
	}

	private static String enReverse(String code) {
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		int rIndex = r.nextInt(9) + 1;
		sb.append(code.substring(rIndex));
		sb.reverse();
		sb.append(code.substring(0, rIndex));
		sb.append(rIndex);
		sb.reverse();
		int ran = 0;
		for (int i = 0; i < hIndex; i++) {
			ran = r.nextInt(rans.length - 1);
			sb.append(rans[ran]);
		}
		StringBuilder sq = new StringBuilder();
		for (int i = 0; i < qIndex; i++) {
			ran = r.nextInt(rans.length - 1);
			sq.append(rans[ran]);
		}
		return sq.append(sb).toString();
	}

	private static String deReverse(String code) {
		code = code.substring(qIndex, code.length() - hIndex);
		StringBuilder sb = new StringBuilder(code);
		code = sb.reverse().toString();
		int rIndex = Integer.valueOf(code.substring(code.length() - 1));
		int len = code.length();
		code = code.substring(0, len - 1);
		len = len - 1;
		rIndex = len - rIndex;
		sb = new StringBuilder();
		sb.append(code.substring(0, rIndex));
		String q = sb.reverse().toString();
		sb = new StringBuilder();
		sb.append(code.substring(rIndex));
		sb.append(q);
		return sb.toString();
	}

	public static String encode(String src) {
		byte[] encoded = encryptMode(Key, src.getBytes());
		return enReverse(new String(encoded));
	}

	public static String decode(String enSrc) {
		byte[] srcBytes = decryptMode(Key, deReverse(enSrc).getBytes());
		return new String(srcBytes);
	}

	public static String getKey(List<String> cookies) {
		if (cookies == null || cookies.size() <= 0) {
			return null;
		}
		for (String cookie : cookies) {
			String[] cookievalues = cookie.split(";");
			if (cookievalues != null && cookievalues.length > 0) {
				for (int j = 0; j < cookievalues.length; j++) {
					cookie = cookievalues[j];
					if (cookie.indexOf("=") > 0) {
						String keyPair = cookie.substring(0, cookie.indexOf("=")).trim();
						if (keyPair.equals("US")) {
							String val = cookie.substring(cookie.indexOf("=") + 1).trim();
							if (val.startsWith("\"")) {
								val = val.substring(1, val.length() - 1);
							}
							try {
								return EncryptKit.decode(val);
							} catch (Exception e) {
								return null;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
//		for (int i = 0; i < 10; i++) {
//			String szSrc = "test测试一下字符串加密" + i + "个，只" + i + "sasdfasd";
//			System.out.println("需要加密的字符串:" + szSrc);
//			String enSrc = encode(szSrc);
//			System.out.println("加密后字符串:" + enSrc);
//			System.out.println("解密后字符串:" + decode(enSrc));
//		}
		String u = encode("1438180414519@play2@12@114.254.122.121");
//		String mw = "iF7VdQ1B7bjgXncQRFkoI7NBVj24fy64yZuMw6s2sB9NCzebBIxmLKBQ9sj5eA==IHGErR";
//		mw= "DGzumWun8QNtyEVHUteB3JWhpNDObOVv7tx6w7VGuLuPcXx8dPDw9GIcgUGRedg==fOXzVb";
		//u = "XrNNT2tZ1x+Rto3oIY5d44aVdHaNCtZLnrOXIGMr7/nwjzVZhC/V2Cp5sT8gAKA==fkMNw4";
		//u = "HTDePofK25YF3IR23IQtYJ5SIl/lfeRGDkhSOwo1aB9NCzebBIx+mLKBQ9sj5eA==mqXVhG";
		System.err.println(u);
		System.err.println(u.length());
		System.err.println(decode(u));
	}

}
