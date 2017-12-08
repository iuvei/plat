package com.gameportal.manage.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class SADES {
	public static String encrypt(String key,String source){
		try{
//			byte[] encryptKey = new byte[] {'g', '9', 'G', '1', '6', 'n', 'T', 's'};
			byte[] encryptKey =key.getBytes();
			KeySpec keySpec = new DESKeySpec(encryptKey);
			SecretKey myDesKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
			IvParameterSpec iv = new IvParameterSpec(encryptKey);
		    
		    Cipher desCipher;

		    // Create the cipher 
		    desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		    
		    // Initialize the cipher for encryption
		    desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, iv);

		    //sensitive information
		    byte[] text =source.getBytes();

//		    System.out.println("Text [Byte Format] : " + text);
//		    System.out.println("Text : " + new String(text));
		   
		    // Encrypt the text
		    byte[] textEncrypted = desCipher.doFinal(text);
			String t =  new String(new Base64().encodeBase64(textEncrypted),"UTF-8");
			
//		    System.out.println("Text Encryted [Byte Format] : " + textEncrypted);
			System.out.println("Text Encryted : " + URLDecoder.decode(t,"UTF-8"));
		    
		    // Initialize the same cipher for decryption
		    desCipher.init(Cipher.DECRYPT_MODE, myDesKey, iv);

		    // Decrypt the text
		    byte[] textDecrypted = desCipher.doFinal(textEncrypted);
		    
		   return t;
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String urlEncoder(String key,String source){
		try {
			return URLEncoder.encode(encrypt(key,source),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String str =SADES.urlEncoder("g9G16nTs", "method=VerifyUsername&Key=D4542CFAA60D4940ABFDA9D753BA5CDF&Time=20161205143613&Username=xiaogao");
		System.out.println(str);
	}
}
