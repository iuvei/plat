package com.na.manager.util;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

/** 
 * 功能描述 
 * 加密常用类 
 */  
public class EncryptUtil {  
	
	
    /** 
     * <li> 
     * 方法名称:encrypt</li> <li> 
     * 加密方法 
     * @param xmlStr 
     *            需要加密的消息字符串 
     * @return 加密后的字符串 
     * @throws Exception 
     */  
    public static String encrypt(String priKey,String xmlStr) throws Exception  {  
       
    	Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		int blockSize = cipher.getBlockSize();

		byte[] dataBytes = xmlStr.getBytes();
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}

		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

		SecretKeySpec keyspec = new SecretKeySpec(priKey.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(priKey.getBytes());

		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
		byte[] encrypted = cipher.doFinal(plaintext);
		return new sun.misc.BASE64Encoder().encode(encrypted);  
    }  
  
    /** 
     * <li> 
     * 方法名称:encrypt</li> <li> 
     * 功能描述: 
     *  
     * <pre> 
     * 解密方法 
     * </pre> 
     *  
     * </li> 
     *  
     * @param xmlStr 
     *            需要解密的消息字符串 
     * @return 解密后的字符串 
     * @throws Exception 
     */  
    public static String decrypt(String priKey,String xmlStr) throws Exception {  
    	byte[] encrypted1 = new BASE64Decoder().decodeBuffer(xmlStr);

		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec keyspec = new SecretKeySpec(priKey.getBytes(), "AES");
		IvParameterSpec ivspec = new IvParameterSpec(priKey.getBytes());

		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original);
		return originalString;
    }  
    
    public static String  SHA256Encrypt(String eData){
    	StringBuffer retData = new StringBuffer();
    	try {   
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(eData.getBytes("GBK"));
			for(byte b:md.digest())
			{
				retData.append(String.format("%02x",b));
			}

		} catch (Exception e) {	
			e.printStackTrace();
		}
    	return retData.toString();
    }
      
} 