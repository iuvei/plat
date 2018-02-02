package com.na.test.batchbet.util;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/*
 * AES对称加密和解密
 */
public class AESEncryptKit {

	public static String DES = "AES"; // optional value AES/DES/DESede

	public static String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"; // optional
																	// value
	/** 
     * 每位允许的字符 
     */  
    private static final String POSSIBLE_CHARS="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
    

	// AES/DES/DESede
	public static Key getSecretKey(String key) {
		SecretKey securekey = null;
		if (key == null) {
			key = "";
		}
//		KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
//		keyGenerator.init(key.getBytes(),DES);
//		securekey = keyGenerator.generateKey();
		securekey = new SecretKeySpec(key.getBytes(),DES);
		return securekey;
	}

	public static String encrypt(String data, String key) throws BadPaddingException, Exception {
//		SecureRandom sr = new SecureRandom();
		Key securekey = getSecretKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
		byte[] bt = cipher.doFinal(data.getBytes());
		String strs = new BASE64Encoder().encode(bt);
		return strs;
	}

	public static String detrypt(String message, String key) throws BadPaddingException, Exception {
//		SecureRandom sr = new SecureRandom();
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		Key securekey = getSecretKey(key);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		byte[] res = new BASE64Decoder().decodeBuffer(message);
		res = cipher.doFinal(res);
		return new String(res);
	}

	public static byte[] initSecretKey() {
		// 返回生成指定算法密钥生成器的 KeyGenerator 对象
		KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance(DES);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return new byte[0];
		}
		// 初始化此密钥生成器，使其具有确定的密钥大小
		// AES 要求密钥长度为 128
		kg.init(128);
		// 生成一个密钥
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	private static String showByteArray(byte[] data) {
		if (null == data) {
			return null;
		}
		StringBuilder sb = new StringBuilder("");
		for (byte b : data) {
			sb.append(b).append("");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public static String genarateKey() {
		return generateRandomString(16);
//		return showByteArray(initSecretKey());
	}
	
    /** 
     * 生产一个指定长度的随机字符串 
     * @param length 字符串长度 
     * @return 
     */  
    public static String generateRandomString(int length) {  
        StringBuilder sb = new StringBuilder(length);  
        SecureRandom random = new SecureRandom();  
        for (int i = 0; i < length; i++) {  
            sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS.length())));  
        }  
        return sb.toString();  
    } 

	public static void main(String[] args) throws Exception {

		String message = "{\"type\":\"req\",\"msg\":\"notice replace key\",\"signMsg\":\"7fe8d06405c96c33a7d883f8dc4413e9\"}";
		
		String key = "0123456789ABCDEF";
		System.out.println("key：" + key);
		String entryptedMsg = encrypt(message, key);
		System.out.println("encrypted message is below :");
		System.out.println(entryptedMsg.replace("\r\n", ""));
		
		String decryptedMsg = detrypt(entryptedMsg, "0023456789ABCDEF");
		System.out.println("decrypted message is below :");
		System.out.println(decryptedMsg);
	}

}