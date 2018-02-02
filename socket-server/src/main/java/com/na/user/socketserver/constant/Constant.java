package com.na.user.socketserver.constant;


/**
 * 服务器常量集合
 */
public class Constant {
	
	/**
	 * API用户登陆秘钥
	 */
	public static final String API_LOGIN_URL_KEY ="16e98c5caea97a93";
	
	/**
	 * API用户登陆RedisKey
	 */
	public static final String USER_API_SERVICE_LOGIN_KEY ="16e98c5caea97a93";
	
	/**
	 * 客户端秘钥
	 */
	public static final String SECRET_KEY = "secretKey";
	public static final String SECRET_OLD_KEY = "oldSecretKey";
	public static final String SECRET_NEW_KEY = "newSecretKey";
	public static final String SECRET_TEST = "testNewKeyStr";

	/**
	 * 客户端 语言包
	 */
	public static final String LAN = "lan";

	/**
	 * 客户端更换秘钥时间戳
	 */
	public static final String CHANGE_KEY_TIMETSTAMP = "ChangekeyTimeStamp";

	/**
	 * 客户端 属性 -- 游戏名称。加入房间赋予
	 *
	 * 以后的命令以及其他 功能，都跟这个属性有关系的。
	 */
	public static final String APP_NAME = "an";

	/**
	 * 客户端 属性 -- 该客户端所对应对象。加入房间赋予
	 */
	public static final String USER = "user";

	public static final String PACKAGE_NAME = "com.na.baccarat.socketserver.command";
	
	/**
	 * 平台玩家登陆验证码
	 */
	public static final String PLATFORM_GAME_USER_TOKEN = "game.session.platform.authcode.";

}
