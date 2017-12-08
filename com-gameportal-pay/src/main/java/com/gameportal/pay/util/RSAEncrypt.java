package com.gameportal.pay.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAEncrypt {
	
	/**
	 * �ֽ�����ת�ַ���ר�ü���
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * ���������Կ��
	 */
	public static void genKeyPair(String filePath) {
		// KeyPairGenerator���������ɹ�Կ��˽Կ�ԣ�����RSA�㷨���ɶ���
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// ��ʼ����Կ������������Կ��СΪ96-1024λ
		keyPairGen.initialize(1024,new SecureRandom());
		// ����һ����Կ�ԣ�������keyPair��
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// �õ�˽Կ
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// �õ���Կ
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		try {
			// �õ���Կ�ַ���
			String publicKeyString = Base64.encode(publicKey.getEncoded());
			// �õ�˽Կ�ַ���
			String privateKeyString = Base64.encode(privateKey.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
