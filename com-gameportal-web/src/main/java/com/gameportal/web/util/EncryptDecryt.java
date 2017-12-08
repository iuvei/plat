package com.gameportal.web.util;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DESC数据加密解密
 * 
 * @author changsheng
 *
 */
@SuppressWarnings("restriction")
public class EncryptDecryt {
	
	private final static Logger logger =Logger.getLogger(EncryptDecryt.class);

	String key;

	public EncryptDecryt() {
	}

	public EncryptDecryt(String key) {
		this.key = key;
	}

	/**
	 * DES加密算法。
	 * 
	 * @param plainText
	 * @return 返回加密后的字节数组。
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] plainText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);
		byte encryptedData[] = cipher.doFinal(plainText);
		return encryptedData;
	}
	
	

	/**
	 * DES解密算法。
	 * 
	 * @param encryptText
	 * @return 返回解密后字节数组。
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] encryptText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
		byte encryptedData[] = cipher.doFinal(encryptText);
		return encryptedData;
	}

	public String encrypt(String input){
		try {
			return base64Encode(encrypt(input.getBytes()))
					.replaceAll("\\s*", "");
		} catch (Exception e) {
			logger.error("加密算法异常："+e.getMessage());
		}
		return null;
	}

	public String decrypt(String input) throws Exception {
		byte[] result = base64Decode(input);
		return new String(decrypt(result));
	}

	public String base64Encode(byte[] s) {
		if (s == null) {
			return null;
		}
		BASE64Encoder b = new BASE64Encoder();
		return b.encode(s);
	}

	public byte[] base64Decode(String s){
		if (s == null)return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(s);
		} catch (IOException e) {
			logger.error("解密算法异常："+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取字符串的ASSIC码
	 * @param value
	 * @return
	 */
	public static String stringToAscii(String value)  
	{  
	    StringBuffer sbu = new StringBuffer();  
	    char[] chars = value.toCharArray();   
	    for (int i = 0; i < chars.length; i++) {  
	        if(i != chars.length - 1)  
	        {  
	            sbu.append((int)chars[i]).append(",");  
	        }  
	        else {  
	            sbu.append((int)chars[i]);  
	        }  
	    }  
	    return sbu.toString();  
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public static void main(String[] args) throws Exception {
		String str ="method=VerifyUsername&Key=D4542CFAA60D4940ABFDA9D753BA5CDF&Time=20161205143613&Username=xiaogao";
		
		EncryptDecryt e = new EncryptDecryt("00837104b7285090");
		System.out.println(e.encrypt(str));
		
		System.out.println(e.decrypt(e.encrypt(str)));
	}
}
