package com.gameportal.manage.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class WebConstants {
	/** 超时提醒 */
	public static final String TIME_OUT = "{\"error\":true,\"msg\":\"登录超时,请重新登录！\"}";
	/** 保存session中的admin用户key */
	public static final String FRAMEWORK_USER = "FRAMEWORK_USER";
	public static final String FRAMEWORK_IMGCODE = "FRAMEWORK_IMGCODE";
	/**
	 * 保存代理key 
	 */
	public static final String FRAMEWORK_PROXY_USER = "PROXY_FRAMEWORK_USER";
	/**
	 * 密码加密code
	 */
	public static final String BLOWFISH_CODE = "GAMEWEB";
	
	/**
	 * 第三方游戏前缀
	 */
	public static final String API_PREFIX = "";
	
	/**
	 * email Queue
	 */
	public static final String E_MAIL_QUEUE = "_MAIL_QUEUE";
	
	/**
	 * 第三方请求地址参数分隔符
	 */
	public static final String API_URL_PARAM_SPLIT="/\\\\/";
	
	
	public static final String AG="AG";
	
	public static final String AGIN="AGIN";
	
	public static final String BBIN = "BBIN";
	
	public static final String PT = "PT";
	
	public static final String MG = "MG";
	
	public static final String SA = "SA";
	
	/**
	 * 广告图片文件上传路径
	 */
	public static final String ROOT_PATH = "uploadFiles/";
	
	/**
	 * 文件上传临时目录
	 */
	public static final String TEMP_FIRST_PATH = "temp/";
	
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
	
	public static final String MG_TOKEN = "mg.token";
	
	/**
	 * AG-BBIN用户名前缀
	 */
	public static final String AG_BBIN_USERNAME_PREFIX = "qb7";
	
	/**
	 * SA游戏checkkey
	 */
	public static final String SA_CHECK_KEY = "qbgameSA777";
	
	/**
	 * 默认数据分页
	 */
	public static final Integer PAGE_SIZE = 20;
	
	public static final Map<String, Object> gameMap = new HashMap<String, Object>();
	
	/** 银行名称集合 */
	public static JSONObject bankNameMap = new JSONObject();
	static {
		bankNameMap.put("中国工商银行", "中国工商银行");
		bankNameMap.put("中国建设银行", "中国建设银行");
		bankNameMap.put("中国银行", "中国银行");
		bankNameMap.put("中国农业银行", "中国农业银行");
		bankNameMap.put("交通银行", "交通银行");
		bankNameMap.put("招商银行", "招商银行");
		bankNameMap.put("中国邮政储蓄银行", "中国邮政储蓄银行");
		bankNameMap.put("中信银行", "中信银行");
		bankNameMap.put("光大银行", "光大银行");
		bankNameMap.put("民生银行", "民生银行");
		bankNameMap.put("兴业银行", "兴业银行");
		bankNameMap.put("华夏银行", "华夏银行");
		bankNameMap.put("上海浦东发展银行", "上海浦东发展银行");
		bankNameMap.put("深圳发展银行", "深圳发展银行");
		bankNameMap.put("广东发展银行", "广东发展银行");
		bankNameMap.put("上海银行", "上海银行");
		bankNameMap.put("平安银行", "平安银行");
		bankNameMap.put("北京银行", "北京银行");
		bankNameMap.put("南京银行", "南京银行");
		bankNameMap.put("宁波银行", "宁波银行");
		bankNameMap.put("江苏银行", "江苏银行");
		bankNameMap.put("浙商银行", "浙商银行");
		bankNameMap.put("渤海银行", "渤海银行");
		bankNameMap.put("恒丰银行", "恒丰银行");
		bankNameMap.put("昆仑银行", "昆仑银行");
		bankNameMap.put("大连银行", "大连银行");
		bankNameMap.put("长沙银行", "长沙银行");
		bankNameMap.put("国家开发银行", "国家开发银行");
		bankNameMap.put("中国进出口银行", "中国进出口银行");
		bankNameMap.put("中国农业发展银行", "中国农业发展银行");
		bankNameMap.put("其它银行","其它银行");
		gameMap.put("AA", 0);//本游戏平台
		gameMap.put("BBIN", 10);
		gameMap.put("MG", 2);
		gameMap.put("AG", 9);
		gameMap.put("AGIN", 5);
		gameMap.put("PT", 8);
		gameMap.put("SA", 11);
	}
	
	/**
	 * 游戏kv
	 * @param key
	 * @return
	 */
	public static Long getGameMap(String key){
		return Long.valueOf(gameMap.get(key).toString());
	}
}
