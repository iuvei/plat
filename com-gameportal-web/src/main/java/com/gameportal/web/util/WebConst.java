package com.gameportal.web.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类
 * @author brooke
 * @date 20150610
 */
public class WebConst {

	/**
	 * 验证码Session key
	 */
	public static final String TOKEN_CODE = "SessionValidationCode";
	
	/**
	 * 代理验证码Session key
	 */
	public static final String AGENT_TOKEN_CODE = "AgentSessionValidationCode";
	
	/**
	 * 支付宝扫码验证码Session key
	 */
	public static final String QR_TOKEN_CODE = "QrSessionValidationCode";
	
	/**
	 * 微信扫码验证码Session key
	 */
	public static final String WX_TOKEN_CODE = "WXSessionValidationCode";
	
	/**
	 * 加密key
	 */
	public static final String DECRYPT_KEY = "GAMEWEB";
	
	/**
	 * SA游戏checkkey
	 */
	public static final String SA_CHECK_KEY = "qbgameSA777";
	
	/**
	 * AG游戏sessionID
	 */
	public static final String AG_SESSIONGUID = "_AG_SESSIONGUID";
	
	/**
	 * 在线用户 redis key
	 */
	public static final String ONLINE_USER_KEY = "ONLINE_USER_KEY";
	
	/**
	 * 第三方游戏前缀
	 */
	public static final String AG_API_PREFIX = "QB";
	
	/**
	 * 第三方PT游戏用户名前缀
	 */
	public static final String PT_API_USERNAME_PREFIX = "QB7";
		
	/**
	 * MG游戏用户名前缀
	 */
	public static final String MG_API_USERNAME_PREFIX = "QB7";
	public static final String MG_API_USERNAME_PREFIX_NEW = "QB7";//新MG前缀	
	
	/**
	 * AG-BBIN用户名前缀
	 */
	public static final String AG_BBIN_USERNAME_PREFIX = "qb7";
	
	/**
	 * 在线用户key
	 */
	public static final String ONLINE_USER_COUNT_KEY = "ONLINE_USER_COUNT_KEY";
	
	/**
	 * 第三方请求地址参数分隔符
	 */
	public static final String API_URL_PARAM_SPLIT="/\\\\/";
	
	/**
	 * 活动礼金
	 */
	public static final Integer ACTIVTY_MONEY = 38;
	
	/**
	 * email key
	 */
	public static final String E_MAIL_KEY = "_EMAIL_VERIFY";
	
	/**
	 * email Queue
	 */
	public static final String E_MAIL_QUEUE = "_MAIL_QUEUE";
	
	/**
	 * email-签证
	 */
	public static final String E_MAIL_PREFIX = "v_";
	
	public static final String AG="AG";
	
	public static final String AGIN="AGIN";
	
	public static final String BBIN = "BBIN";
	
	public static final String PT = "PT";
	
	public static final String MG = "MG";
	
	public static final String SA = "SA";
		
	public static final String startt = "00:00:00";
	
	public static final String endt = "23:59:59";
	
	public static final String EMAILCODE_DES_KEY = "qianbaobet";
	//用户每天可抽奖最大次数
	public static final Integer LOTTERY_MAX_TIMES = 10;
	
	public static final String MG_TOKEN = "mg.token";
	
	public static final Map<String, Object> gameMap = new HashMap<String, Object>();
	static{
		gameMap.put("AA", 0);//本游戏平台
		gameMap.put(MG, 2);
		gameMap.put(AGIN, 5);
		gameMap.put(PT, 8);
		gameMap.put(AG, 9);
		gameMap.put(BBIN, 10);
		gameMap.put(SA, 11);
	}
	
	/**
	 * 获取平台名称
	 * @param id
	 * @return
	 */
	public static String getPlatName(int id) {
		String name = "PT";
		switch (id) {
		case 2:
			name = "MG";
			break;
		case 5:
			name = "AGIN";
			break;
		case 8:
			name = "PT";
			break;
		case 9:
			name = "AG";
			break;
		case 10:
			name = "BBIN";
			break;
		case 11:
			name = "SA";
			break;
		default:
			break;
		}
		return name;
	}
	
	/**
	 * 游戏kv
	 * @param key
	 * @return
	 */
	public static Long getGameMap(String key){
		return Long.valueOf(gameMap.get(key).toString());
	}
	/**
	 * 检测是否为十六进制非空字符串
	 * 
	 * @param value 待检测的字符串
	 * @return 检测结果
	 */
	public static boolean isHex( String value ) {
		return value.matches("^[0-9A-Fa-f]+$"); 
	}
	
	/**
	 * 检测是否为整形数字
	 * true为是false为不是
	 * @param value 待检测的字符串
	 * @return 检测结果
	 */
	public static boolean isInteger( String value) {
		try {
			Integer.valueOf(value);
			return true;
		} catch (Exception ex) {
			return false;
		}		
	}
	
	/**
	 * 验证是否为浮点型数字
	 * @param value
	 * @return
	 */
	public static boolean isDouble(String value){
		try {
			Double.valueOf(value);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * 检测是否为非负整数
	 * 是为false 不是为true
	 * @param value 待检测的字符串
	 * @return 检测结果
	 */
	public static boolean isNonnegative( String value ) {
		try {
			if (Integer.valueOf(value) < 0 ) {
				return false;
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * 检测是否为普通英文名字符串（字符、数字、下划线）
	 * 
	 * @param value 待检测的字符串
	 * @return 检测结果
	 */
	public static boolean isNameString( String value ) {
		return (value.matches("^[_0-9A-Za-z]+$"));
	}
	
	/**
	 * 检测是否为KeyID（15位、十六进制数）
	 * 
	 * @param value 待检测的字符串
	 * @return 检测结果
	 */
	public static boolean isKeyID( String value ) {
		return (value.length() == 15) && isHex( value );
	}
	
	/**
	 * 检查手机号码是否是(11位,十六进制数)
	 * @param value 待检测的字符串 
	 * @return 检测结果
	 */
	public static boolean isPhone(String value){
		return (value.length() == 11) && isHex( value );
	}
	
	/**
	 * 检查用户输人的动态密码 (8位,十六进制数)
	 * @param value 待检测的字符串 
	 * @return 检测结果
	 */
	public static boolean isPassWord(String value){
		return (value.length() == 8) && isHex( value );
	}
	
	/**
	 * 检查用户输人的动态密码 (len位,十六进制数)
	 * @param value 待检测的字符串 
	 * @param len 长度
	 * @return 检测结果
	 */
	public static boolean isCard(String value,int len){
		return (value.length() == len) && isHex( value );
	}
}
