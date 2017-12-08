package com.gameportal.web.game.util;

import java.util.HashMap;
import java.util.Map;

/**
 * AGIN官方API CODE信息
 * @author Administrator
 *
 */
public class AGINOfficialCode {

	
	/**
	 * 转账成功
	 */
	public static String AGIN_ERRORCODE_0="0"; 
	
	/**
	 * 失败, 订单未处理状态
	 */
	public static String AGIN_ERRORCODE_1="1"; 
	
	/**
	 * 因无效的转账金额引致的失败
	 */
	public static String AGIN_ERRORCODE_2="2"; 
	
	/**
	 * key值错误
	 */
	public static String AGIN_ERRORCODE_KEY_ERROR="key_error"; 
	
	/**
	 * 重复转账
	 */
	public static String AGIN_ERRORCODE_DUPLICATE_TRANSFER ="duplicate_transfer";
	
	/**
	 * 游戏账号不存在
	 */
	public static String AGIN_ERRORCODE_ACCOUNT_NOT_EXIST ="account_not_exist"; 
	
	/**
	 * 网络问题导致资料遗失
	 */
	public static String AGIN_ERRORCODE_NETWORK_ERROR ="network_error"; 	
	
	/**
	 * 余额不足不能转账
	 */
	public static String AGIN_ERRORCODE_NOT_ENOUGH_CREDIT ="not_enough_credit"; 
	
	/**
	 * 余额不足不能转账
	 */
	public static String AGIN_ERRORCODE_ERROR="error"; 
	

	static Map<String, Object> code = new HashMap<String, Object>();
	static{
		code.put(AGIN_ERRORCODE_0, "转账成功");
		code.put(AGIN_ERRORCODE_1, "交易失败,存在未处理订单");
		code.put(AGIN_ERRORCODE_2, "因无效的转账金额引致的失败");
		code.put(AGIN_ERRORCODE_KEY_ERROR, "key值错误。");
		code.put(AGIN_ERRORCODE_DUPLICATE_TRANSFER, "重复转账");
		code.put(AGIN_ERRORCODE_ACCOUNT_NOT_EXIST, "游戏账号不存在");
		code.put(AGIN_ERRORCODE_NETWORK_ERROR, "网络问题导致资料遗失");
		code.put(AGIN_ERRORCODE_NOT_ENOUGH_CREDIT, "余额不足,不能转账");
		code.put(AGIN_ERRORCODE_ERROR, "转账错误, 参考 msg 的错误描述信息");
	}
	
	public static String getMsg(String key){
		return code.get(key).toString();
	}

}
