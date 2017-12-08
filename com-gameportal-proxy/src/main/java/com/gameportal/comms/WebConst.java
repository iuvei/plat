package com.gameportal.comms;

import java.util.HashMap;
import java.util.Map;

/**
 * web 常量
 * @author brooke
 *
 */
public class WebConst {

	/**
	 * 验证码CODE
	 */
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";
	
	/**
	 * 密码加密code
	 */
	public static final String BLOWFISH_CODE = "GAMEPORTAL";
	
	/**
	 * 文件上传父目录
	 */
	public static final String ROOT_PATH = "uploadFiles/";
	
	/**
	 * 文件上传目录
	 */
	public static final String FILE_FIRST_PATH = "file/";
	
	/**
	 * 图片上传目录
	 */
	public static final String IMAGES_FIRST_PATH = "uploadImgs/";
	
	/**
	 * 文件上传缓冲目录
	 */
	public static final String TEMP_FIRST_PATH = "temp/";
	
	/**
	 * APP默认ICO图标
	 */
	public static final String DEFAULT_APP_ICO = "default_app_icon.png";
	
	/**
	 * 获取web路径Key  System.getProperty(WEB_ROOT_KEY);
	 */
	public static final String WEB_ROOT_KEY = "webapp.root";
	
	/**
	 * 失败状态
	 */
	public static final String STATUS_FAILED = "failed";
	/**
	 * 成功状态
	 */
	public static final String STATUS_SUCCESS = "success";
	public static final String SYSNAME = "admin/config/SYSNAME.txt";	//系统名称路径
	public static final String PAGE	   = "admin/config/PAGE.txt";		//分页条数配置路径
	public static final String EMAIL   = "admin/config/EMAIL.txt";		//邮箱服务器配置路径
	public static final String SMS1   = "admin/config/SMS1.txt";		//短信账户配置路径1
	public static final String SMS2   = "admin/config/SMS2.txt";		//短信账户配置路径2
	
	/**
	 * 数据库表
	 */
	public static Map<String, String> dbtable = new HashMap<String, String>();
	static{
		//key游戏类型value对应数据库
		dbtable.put("ssc", "open_award_cqssc");
		dbtable.put("jxssc", "open_award_jxssc");
		dbtable.put("xjssc", "open_award_xjssc");
	}
	
	/**
	 * 根据游戏ID获取对应数据库
	 * @param key
	 * @return
	 */
	public static String getDB(String key){
		return dbtable.get(key).toString();
	}
}
