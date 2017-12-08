package com.gameportal.web.game.handler;

import java.util.Map;

import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.user.model.UserInfo;

/**
 * @ClassName: IGameServiceHandler
 * @Description: (游戏平台分发器)
 * @date 2015年4月13日 下午10:24:19
 */
public abstract interface IGameServiceHandler {

	/**
	 * @Title: createAccount
	 * @Description: (游戏帐号创建)
	 * @param userInfo
	 * @param paramMap
	 * @return 设定文件
	 * @return Object 返回类型
	 */
	public Object createAccount(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap);

	/**
	 * @Title: loginGame
	 * @Description: (登录游戏平台)
	 * @param userInfo
	 * @param paramMap
	 * @return 设定文件
	 * @return Object 返回类型
	 */
	public Object loginGame(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap);

	/**
	 * @Title: logoutGame
	 * @Description: (退出游戏平台)
	 * @param userInfo
	 * @param paramMap
	 * @return 设定文件
	 * @return Object 返回类型
	 */
	public Object logoutGame(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap);

	/**
	 * @Title: deposit
	 * @Description: (存入游戏平台)
	 * @param userInfo
	 * @param paramMap
	 * @return 设定文件
	 * @return Object 返回类型
	 */
	public Object deposit(UserInfo userInfo, GamePlatform gamePlatform,
			String amount, Map paramMap);

	/**
	 * @Title: queryBalance
	 * @Description: (查询用户游戏平台餘額)
	 * @param userInfo
	 * @param paramMap
	 * @return 设定文件
	 * @return Object 返回类型
	 */
	public Object queryBalance(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap);

	/**
	 * @Title: withdrawal
	 * @Description: (用户游戏平台提款)
	 * @param userInfo
	 * @param paramMap
	 * @return 设定文件
	 * @return Object 返回类型
	 */
	public Object withdrawal(UserInfo userInfo, GamePlatform gamePlatform,
			String amount, Map paramMap);
	
	/**
	 * 确认转账。
	 * @param userInfo
	 * @param gamePlatform
	 * @param paramMap
	 * @return 返回接口结果（0表示成功）
	 */
	public Object transferCreditConfirm(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap);
	/**
	 * 查询订单状态。
	 * @param userInfo
	 * @param gamePlatform
	 * @param paramMap
	 * @return 返回第三方订单状态。
	 */
	public Object queryOrderStatus(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap);

	/**
	 * @Title: betRecord
	 * @Description: (用户游戏平台游戏记录)
	 * @param userInfo
	 * @param paramMap
	 * @return 设定文件
	 * @return JSONObject 返回类型
	 */
	public Object betRecord(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap);

	public Object edit(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap);

	public Object resetloginattempts(UserInfo userInfo,
			GamePlatform gamePlatform, Map paramMap);

	public Object getSessionGUID(UserInfo userInfo, GamePlatform gamePlatform,
			Map paramMap, boolean isCache);

}
