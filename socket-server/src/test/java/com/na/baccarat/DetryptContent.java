package com.na.baccarat;

import com.na.user.socketserver.util.aes.AESEncryptKit;

public class DetryptContent {
	
	public static void main(String[] args) {
//		String message = "{\"type\":\"req\",\"msg\":\"notice replace key\",\"signMsg\":\"7fe8d06405c96c33a7d883f8dc4413e9\"}";

		String key = "0123456789ABCDEF";
//		System.out.println("key：" + key);
//		String entryptedMsg = encrypt(message, key);
//		System.out.println("encrypted message is below :");
//		System.out.println(entryptedMsg.replace("\r\n", ""));

		String message = "6Vk37CIUk1fnWm+ewI6RUBn9QsMb5XUwicXnkqGim1/+o9EuhpXlRnyQ9RkizyaRkl3inx3+3DReGHmxuMSic0SFoH3tlEDZehbeHAgllUOQrizlYRfTJnU5uz5NwzO3meXJKzK3cg/W3pg6WzhyCA==";
		String decryptedMsg = null;
		try {
			decryptedMsg = AESEncryptKit.detrypt(message, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("decrypted message is below :");
		System.out.println(decryptedMsg);
		
	}

}
