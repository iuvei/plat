package com.na.baccarat.socketserver.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 百家乐玩法配置
 * 
 * @author alan
 * @date 2017年5月2日 上午10:14:26
 */
@Component
@PropertySource("classpath:config/game/baccarat.properties")
public class BaccaratConfig {
	
	public static Properties config;
	
	/**
	 * 显示好路的个数
	 */
	public static final int goodRoadsNum = 2;
	

	/**
	 * 座位数
	 */
	public static final int seatNum = 7;
	
	/**
	 * 座位号
	 */
	public static final int seatNums[] = {1,2,3,5,6,7,8};
	
	/**
	 * 此游戏 最多可以有多少张牌
	 */
	public static final int maxCardNum = 6;
	
	/**
	 * 最大局数。
	 */
	public static final int maxGameNum = 52 * 8 / 4;

}
