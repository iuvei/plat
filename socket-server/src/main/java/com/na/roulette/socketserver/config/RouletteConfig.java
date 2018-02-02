package com.na.roulette.socketserver.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 轮盘玩法配置
 * 
 * @author alan
 * @date 2017年5月2日 上午10:14:26
 */
@Component
@PropertySource("classpath:config/game/roulette.properties")
public class RouletteConfig {
	
	public static Properties config;
	
}
