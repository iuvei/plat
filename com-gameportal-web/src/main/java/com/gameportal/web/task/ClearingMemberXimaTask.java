package com.gameportal.web.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.user.service.IMemberXimaMainService;
import com.gameportal.web.user.service.IReportQueryService;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.SpringUtils;

/**
 * 定时会员洗码
 * 
 * @author Administrator
 *
 */
public class ClearingMemberXimaTask {
	private static final Logger logger = Logger.getLogger(ClearingMemberXimaTask.class);

	private IReportQueryService reportQueryService;

	private IGamePlatformService gamePlatformService;

	private List<String> gameMembers;

	private List<GamePlatform> gamePlatforms;

	private IMemberXimaMainService memberXimaMainServiceImpl;
	
	public void run() {
		String betDate = DateUtil.FormatDate(DateUtil.addDay(new Date(), -1)); // 昨天日期
		String gamePlatStr = "";
		String gameplat = "";
		logger.info(betDate + "洗码开始。");
		reportQueryService = (IReportQueryService) SpringUtils.getBean("reportQueryServiceImpl");
		gamePlatformService = (IGamePlatformService) SpringUtils.getBean("gamePlatformServiceImpl");
		memberXimaMainServiceImpl = (IMemberXimaMainService) SpringUtils.getBean("memberXimaMainServiceImpl");

		Map<String, Object> params = new HashMap<String, Object>();
		gamePlatforms = gamePlatformService.queryGame(params);
		params.put("betDate", betDate);
		gameMembers = reportQueryService.selectGameMemberByDate(params);
		for (GamePlatform gp : gamePlatforms) { // 拼接游戏平台字符串
			if (gamePlatStr.length() > 0) {
				gamePlatStr += ",";
			}
			gamePlatStr += "'" + gp.getGpname() + "'";
			if (gameplat.length() > 0) {
				gameplat += ",";
			}
			gameplat += gp.getGpid() + "#" + gp.getGpname();
		}
		params.put("ymdstart", betDate + " 00:00:00");
		params.put("ymdend", betDate + " 23:59:59");
		params.put("jstimes", betDate);
		params.put("gameplat", gameplat);
		params.put("platformcodeparams", "(" + gamePlatStr + ")");
		try {
			String code = memberXimaMainServiceImpl.saveMemberXimaYesterDay(params, gameMembers);
			if ("2000".equals(code)) {
				logger.info(betDate + "洗码完成。");
			} else if ("2001".equals(code)) {
				logger.info(betDate + "没有可洗码平台。");
			} else {
				logger.error(betDate + "洗码失败。");
			}
		} catch (Exception e) {
			logger.error(betDate + "洗码失败。");
		}
	}
}
