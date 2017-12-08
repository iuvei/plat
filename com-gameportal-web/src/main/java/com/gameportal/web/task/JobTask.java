package com.gameportal.web.task;

import org.apache.log4j.Logger;

import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.util.SpringUtils;

/**
 * 定时任务。
 * 
 * @author sum
 *
 */
public class JobTask {
	private Logger logger = Logger.getLogger(JobTask.class);

	public void refreshMgToken() {
		IGamePlatformService gamePlatformService = (IGamePlatformService) SpringUtils.getBean("gamePlatformServiceImpl");
//		gamePlatformService.refreshMgToken();
		logger.info("MG TOKEN 更新完毕。");
	}
}
