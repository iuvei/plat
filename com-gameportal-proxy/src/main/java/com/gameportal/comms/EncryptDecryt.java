package com.gameportal.comms;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加密
 * @author brooke
 *
 */
@SuppressWarnings("all")
public class EncryptDecryt {

	private static final String KEY = "alTx_82$Phon";//PhoenixK
	private static final String DES = "DES";
	private static final String CIPHER_DESC = "DES/CBC/PKCS5Padding";
	
	private static final byte[] IV = { 78, (byte)214, 48, 76, 9, 61, (byte)244, (byte)246};
	
	
	/**
	 * 加密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws RuntimeException
	 */
	private static byte[] encrypt(String src, byte[] key) throws RuntimeException {
		//DES算法要求有一个可信任的随机数源
		try{
			byte[] inputByteArray = src.getBytes("UnicodeLittleUnmarked");
			IvParameterSpec iv = new IvParameterSpec(IV);
			// 从原始密匙数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(inputByteArray);
		}catch(Exception e){
            throw new RuntimeException(e);
        }
	}
	
	/**
	 * 解密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws RuntimeException
	 */
	private static byte[] decrypt(String src, byte[] key) throws RuntimeException {
		try {
			byte[] inputByteArray = new byte[src.length() / 2];
			for (int x = 0; x < src.length() / 2; x++){
				int i = Integer.parseInt(src.substring(x*2, (x*2)+2), 16);
				inputByteArray[x] = (byte)i;
			}
			IvParameterSpec iv = new IvParameterSpec(IV);
			// 从原始密匙数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
			return cipher.doFinal(inputByteArray);
		} catch (Exception e) {
			throw new RuntimeException("错误解码："+src);
		}
	}
	
	/**
	  * 数据解密
	  * @param data
	  * @param key 密钥
	  * @return
	  * @throws Exception
	  */
	public final static String decrypt(String data) throws Exception{
		byte[] str = decrypt(data,KEY.getBytes("UTF-8"));
		String result = "";
		for(int i=0;i<str.length;i++){
			if(str[i] != 0){
				result += (char)str[i];
			}
		}
		return result;
	}
	
	/**
	 * 数据加密
	 * @param data 数据源
	 * @param key 密钥
	 * @return 加密后的数据
	 */
	public final static String encrypt(String data){
        if(data!=null)
        {
        	try {
	            return byte2hex(encrypt(data,KEY.getBytes("UTF-8")));
	        }catch(Exception e) {
	            throw new RuntimeException(e);
	        }

        }
		return null;     
	}
	
	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b!=null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1){
				hs.append('0');
			}
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
	public static void main(String[] args) throws Exception {
		System.out.println(encrypt("root"));
		System.out.println(encrypt("123456"));
	}
}
