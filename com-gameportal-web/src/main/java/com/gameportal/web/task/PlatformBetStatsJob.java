package com.gameportal.web.task;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.gameportal.web.game.service.IGamePlatformService;

@Component
public class PlatformBetStatsJob {
	
	private static Logger logger = Logger.getLogger(PlatformBetStatsJob.class);
	 
	@Resource(name = "gamePlatformServiceImpl")
	private IGamePlatformService gamePlatformService;
	
	public void statsPlatformBet(){
		logger.info("start statistics platform bet");
		gamePlatformService.saveStatsPlatformBet();
		logger.info("end statistics platform bet");
	}
}
