package com.gameportal.pay.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import com.gameportal.pay.util.WebConst;

import nu.xom.jaxen.function.ConcatFunction;

public class RSACoderUtil extends CoderUtil {
	public static final String KEY_ALGORTHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	public static final int KEY_SIZE = 1024;
	public static final String PUBLIC_KEY = "RSAPublicKey";
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	private static final int MAX_ENCRYPT_BLOCK = 117;
	private static final int MAX_DECRYPT_BLOCK = 128;

	public static String getParamsWithDecodeByPublicKey(String paramsString, String charset, String fcsPublicKey)
			throws Exception {
		byte[] paramByteArr = encryptByPublicKey(paramsString.getBytes(charset), fcsPublicKey);
		String str= URLEncoder.encode(new String(encodeStr(paramByteArr),WebConst.CHARSET), charset);
		return str;
	}

	public static String getParamsWithDecodeByPrivateKey(String paramsString, String charset, String privateKey)
			throws Exception {
		System.out.println(privateKey);
		byte[] shaParams = DigestCoder.encodeWithSHA(paramsString.getBytes(charset));

		String signParams = sign(shaParams, privateKey);

		return URLEncoder.encode(signParams, charset);
	}

	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decodeStr(key.getBytes(WebConst.CHARSET));

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);

		byte[] dataReturn = null;
		for (int i = 0; i < data.length; i += 117) {
			byte[] doFinal = cipher.doFinal(subarray(data, i, i + 117));
			dataReturn = addAll(dataReturn, doFinal);
		}

		return dataReturn;
	}

	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decodeStr(key.getBytes(WebConst.CHARSET));

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		//对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 解密时超过128字节就报错。为此采用分段解密的办法来解密
		byte[] dataReturn = null;
		for (int i = 0; i < data.length; i += MAX_DECRYPT_BLOCK) {
			byte[] doFinal = cipher.doFinal(RSACoderUtil.subarray(data, i, i + MAX_DECRYPT_BLOCK));
			dataReturn = RSACoderUtil.addAll(dataReturn, doFinal);
		}
		return dataReturn;
	}

	public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decodeStr(key.getBytes(WebConst.CHARSET));

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(1, publicKey);

		byte[] dataReturn = null;
		for (int i = 0; i < data.length; i += 117) {
			byte[] doFinal = cipher.doFinal(subarray(data, i, i + 117));
			dataReturn = addAll(dataReturn, doFinal);
		}

		return dataReturn;
	}

	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decodeStr(key.getBytes(WebConst.CHARSET));
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, publicKey);

		byte[] dataReturn = null;
		for (int i = 0; i < data.length; i += 128) {
			byte[] doFinal = cipher.doFinal(subarray(data, i, i + 128));
			dataReturn = addAll(dataReturn, doFinal);
		}

		return dataReturn;
	}

	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = decodeStr(privateKey.getBytes(WebConst.CHARSET));

		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(privateKey2);
		signature.update(data);
		return new String(encodeStr(signature.sign()));
	}

	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = decodeStr(publicKey.getBytes(WebConst.CHARSET));

		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initVerify(publicKey2);
		signature.update(data);

		return signature.verify(decodeStr(sign.getBytes(WebConst.CHARSET)));
	}

	public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return new byte[0];
		}

		byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map keyMap = new HashMap();
		keyMap.put("RSAPublicKey", publicKey);
		keyMap.put("RSAPrivateKey", privateKey);
		return keyMap;
	}

	public static byte[] addAll(byte[] array1, byte[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return (byte[]) (byte[]) array.clone();
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String paramsString="TLEp%2BcJyvGz530GHFS8FZSuyNxROSqe5ftVQN6aPwqkQyoElK86KfTsK9UsdiDFOt6aSoA3kXf1lWb4rKQVl0aSaPn48rL983DwiFsWyN4EWZc7txCQWCx%2BLVyY04g2EzBfH%2F42mjLN%2BSK%2BrnVg4jOuxklZjqJN41wj3WO6T6s%2BbAv0LsB1X2bzG68bmdTGKWnKcGoQ6PwlZRwPr3WgzdTLyISnMEVNd64Rw7Z1ihQN%2FUR8h4kdUayQ9%2FIWoCMv5TxSYvO6EPQoDHfA%2Bes9ygkJvjswymvCtThxZJpu%2F%2BDU8l2bcDCluKZjdqrqtgRiwHr4UzwTczc3gmu0tUhUMJaFg6pK5SpiGwbXl1%2FqaZeBNih1JLkqIfu9yn5c7AeCZhvemto1gUHgiX9b%2FJKdJqX9KRf0RiMw%2BWj9GKKoSWXBYB%2BKbGqrZ45tFWTGbtLxmdC7k9UKRMFhGmTMXT8RN4ZL5Rkv34npbikZVmz05cVXQ%2B924ZBOL%2BdmNT8hhpJmN";
		String sign="e%2FZokDP2a3jzwm1HUaTPeWO77LqsDLnwUg9%2BtnBpGreUHF3ho9jJ4N5HRAM09AysygVILC3teivaXQu7rCWDOBsyvOJNF6oAd8MXUDTsRsDzQXLwpOg00u8HV3R%2BSbsqDL3rK4XUVIDzRZ%2FJH878KGLxavdaqe9XJIiYDn%2F5%2FJo%3D";
		try {
			paramsString = new String(RSACoderUtil.decryptByPrivateKey(CoderUtil.decodeStr(URLDecoder.decode(paramsString, "UTF-8").getBytes("UTF-8")), WebConst.PRIVATE_KEY), "UTF-8");
			System.out.println(paramsString);
			boolean flag = RSACoderUtil.verify(paramsString.getBytes("UTF-8"), WebConst.PUBLIC_KEY,URLDecoder.decode(sign,"UTF-8"));
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
