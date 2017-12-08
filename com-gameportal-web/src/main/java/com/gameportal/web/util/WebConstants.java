package com.gameportal.web.util;

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
	public static final String BLOWFISH_CODE = "GAMEPORTAL";
	
	/**
	 * 第三方游戏前缀
	 */
	public static final String API_PREFIX = "";
	
	
	/**
	 * SA游戏checkkey
	 */
	public static final String SA_CHECK_KEY = "qbgameSA777";
	
	/**
	 * email Queue
	 */
	public static final String E_MAIL_QUEUE = "_MAIL_QUEUE";
	
	/**
	 * 第三方请求地址参数分隔符
	 */
	public static final String API_URL_PARAM_SPLIT="/\\\\/";
	
	/**
	 * AG-BBIN用户名前缀
	 */
	public static final String AG_BBIN_USERNAME_PREFIX = "f10";
	
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
	 * 第三方PT游戏用户名前缀
	 */
	public static final String PT_API_USERNAME_PREFIX = "DXPT";
	
	/**
	 * 默认数据分页
	 */
	public static final Integer PAGE_SIZE = 20;

	/**
	 * 操作成功
	 */
	public static final int SUCCESS = 1;

	/**
	 * 操作失败
	 */
	public static final int FAILURE = 0;

	/**
	 * 1001表示用户未登录
	 */
	public static final int NO_LOGIN = 1001;
	
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
	
	/**
	 * 积分兑换
	 */
	public static final Map<Integer, Integer> integralMap = new HashMap<Integer,Integer>(){
		{
			put(1, 500);
			put(2, 500);
			put(3, 400);
			put(4, 300);
			put(5, 250);
			put(6, 200);
		}
	};
	
	/**
	 * 晋级礼金
	 */
	public static final Map<Integer, Integer> upGradeMap = new HashMap<Integer,Integer>(){
		{
			put(1, 88);
			put(2, 188);
			put(3, 588);
			put(4, 888);
			put(5, 1888);
		}
	};
	
	/**
	 * 生日礼金
	 */
	public static final Map<Integer, Integer> birthdayMap = new HashMap<Integer,Integer>(){
		{
			put(2, 88);
			put(3, 188);
			put(4, 588);
			put(5, 888);
			put(6, 1888);
		}
	};
	
	/**
	 * 会员等级
	 */
	public static final Map<Integer, String> levelMap = new HashMap<Integer,String>(){
		{
			put(1, "新会员");
			put(2, "星级VIP");
			put(3, "银卡VIP");
			put(4, "金卡VIP");
			put(5, "钻石VIP");
			put(6, "钻石VIP");
		}
	};
}
