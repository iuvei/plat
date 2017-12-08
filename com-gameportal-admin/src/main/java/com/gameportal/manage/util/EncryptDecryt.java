package com.gameportal.manage.util;

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
	public byte[] desEncrypt(byte[] plainText) throws Exception {
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
	public byte[] desDecrypt(byte[] encryptText) throws Exception {
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
			return base64Encode(desEncrypt(input.getBytes()))
					.replaceAll("\\s*", "");
		} catch (Exception e) {
			logger.error("加密算法异常："+e.getMessage());
		}
		return null;
	}

	public String decrypt(String input) throws Exception {
		byte[] result = base64Decode(input);
		return new String(desDecrypt(result));
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static void main(String[] args) throws Exception {
		EncryptDecryt e = new EncryptDecryt("bAowjitQ");
		System.out.println(e.encrypt("cagent=81288128/\\\\/method=tc"));
		System.out.println(e.decrypt("oFiBLI8PzdZrzuhZMHHpBTn6LvSWxhAbADB0dGgLxBc="));
	}
}
