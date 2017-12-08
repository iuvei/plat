package com.gameportal.web.game.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gameportal.web.game.dao.GameAccountDao;
import com.gameportal.web.game.dao.GamePlatformDao;
import com.gameportal.web.game.dao.PlatformBetStatsDao;
import com.gameportal.web.game.model.AGElectronic;
import com.gameportal.web.game.model.AGINElectronic;
import com.gameportal.web.game.model.GameAccount;
import com.gameportal.web.game.model.GamePlatform;
import com.gameportal.web.game.model.MGElectronic;
import com.gameportal.web.game.model.PlatformBetStats;
import com.gameportal.web.game.model.SAElectronic;
import com.gameportal.web.game.service.IGamePlatformService;
import com.gameportal.web.redis.service.IRedisService;
import com.gameportal.web.util.DateUtil;
import com.gameportal.web.util.HttpUtil;
import com.gameportal.web.util.WebConst;

import net.sf.json.JSONObject;

@Service("gamePlatformServiceImpl")
public class GamePlatformServiceImpl implements IGamePlatformService {
	@Resource(name = "gamePlatformDao")
	private GamePlatformDao gamePlatformDao = null;
	@Resource(name = "gameAccountDao")
	private GameAccountDao gameAccountDao = null;
	@Resource(name = "platformBetStatsDao")
	PlatformBetStatsDao platformBetStatsDao;
	@Resource(name = "redisServiceImpl")
	private IRedisService iRedisService;
	private static Logger logger = Logger.getLogger(GamePlatformServiceImpl.class);

	public GamePlatformServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public GamePlatform queryGamePlatform(String gpname) {
		// TODO Auto-generated method stub
		return queryGamePlatform(gpname, 1);
	}

	@Override
	public GamePlatform queryGamePlatform(String gpname, Integer status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gpname", gpname);
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			map.put("status", status);
		}
		GamePlatform gamePlatform = (GamePlatform) gamePlatformDao
				.queryForObject(gamePlatformDao.getSelectQuery(), map);
		return gamePlatform;
	}

	@Override
	public boolean isExistAccount(Long uiid, String type) {
		// TODO Auto-generated method stub
		return isExistAccount(uiid, type, 1);
	}

	@Override
	public boolean isExistAccount(Long uiid, String type, Integer status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uiid", uiid);
		if (StringUtils.isNotBlank(ObjectUtils.toString(type))) {
			map.put("uname", type);
		}
		if (StringUtils.isNotBlank(ObjectUtils.toString(status))) {
			map.put("status", status);
		}
		GameAccount gameAccount = (GameAccount) gameAccountDao.queryForObject(
				gameAccountDao.getSelectQuery(), map);
		return StringUtils.isNotBlank(ObjectUtils.toString(gameAccount))
				&& StringUtils.isNotBlank(ObjectUtils.toString(gameAccount
						.getGaid())) ? true : false;
	}

	@Override
	public GameAccount saveGameAccount(GameAccount gameAccount)
			throws Exception {
		// TODO Auto-generated method stub
		gameAccount = (GameAccount) gameAccountDao.save(gameAccount);
		return StringUtils.isNotBlank(ObjectUtils.toString(gameAccount
				.getGaid())) ? gameAccount : null;
	}

	@Override
	public List<GamePlatform> queryGame(Map<String, Object> params) {
		return gamePlatformDao.queryAllGame(params);
	}

	@Override
	public void saveMGElectronic(MGElectronic entity) {
		gamePlatformDao.saveMGElectronic(entity);
	}

	@Override
	public Long queryElectronicCount(Map<String, Object> params) {
		return gamePlatformDao.queryElectronicCount(params);
	}

	@Override
	public List<MGElectronic> queryElectronic(Map<String, Object> params,int startNo, int pagaSize) {
		return gamePlatformDao.queryElectronic(params,(startNo - 1) * pagaSize, pagaSize);
	}

	@Override
	public List<MGElectronic> queryElectronic(Map<String, Object> params) {
		return gamePlatformDao.queryElectronic(params);
	}
	
	@Override
	public List<AGElectronic> queryAGElectronic(Map<String, Object> params) {
		return gamePlatformDao.queryAgElectronic(params);
	}
	
	@Override
	public Long queryAgElectronicCount(Map<String, Object> params) {
		return gamePlatformDao.queryAgElectronicCount(params);
	}

	@Override
	public Long queryAginElecCount(Map<String, Object> params) {
		return gamePlatformDao.queryAginElecCount(params);
	}

	@Override
	public List<AGINElectronic> queryAginElec(Map<String, Object> params) {
		return gamePlatformDao.queryAginElec(params);
	}

	@Override
	public Long querySAElecCount(Map<String, Object> params) {
		return gamePlatformDao.querySAElecCount(params);
	}

	@Override
	public List<SAElectronic> querySAElec(Map<String, Object> params) {
		return gamePlatformDao.querySAElec(params);
	}

	@Override
	public void saveStatsPlatformBet() {
		Date yesterday = DateUtil.addDay(new Date(), -1);
		List<PlatformBetStats> list = platformBetStatsDao.selectBetFromBetlogByDate(yesterday);
		PlatformBetStats platformBetStats = null;
		for(PlatformBetStats tmpBetStats : list){
			platformBetStats = new PlatformBetStats();
			platformBetStats.setPlatform(tmpBetStats.getPlatform());
			platformBetStats.setCreateDate(yesterday);
			platformBetStats.setBetTotal(tmpBetStats.getBetTotal()
					.setScale(2, BigDecimal.ROUND_DOWN));
			platformBetStats.setBetNum(tmpBetStats.getBetNum());
			platformBetStats.setValidBetAmount(tmpBetStats.getValidBetAmount()
					.setScale(2, BigDecimal.ROUND_DOWN));
			platformBetStats.setPayoutAmount(tmpBetStats.getPayoutAmount()
					.setScale(2, BigDecimal.ROUND_DOWN));
			platformBetStats.setFinalAmount(tmpBetStats.getFinalAmount()
					.setScale(2, BigDecimal.ROUND_DOWN));
			platformBetStatsDao.save(platformBetStats);
		}
	}

	@Override
	public List<MGElectronic> queryMPT(Map<String, Object> params) {
		return gamePlatformDao.queryMobilePT(params);
	}

	@Override
	public List<PlatformBetStats> selectBetFromBetlogByUser(Map<String, Object> params) {
		return platformBetStatsDao.selectBetFromBetlogByUser(params);
	}

	@Override
	public String refreshMgToken() {
		Map<String, Object> headerMap = new HashMap<String, Object>();
		GamePlatform plat =queryGamePlatform(WebConst.MG);
		if(plat !=null){
			headerMap.put("Authorization", "Basic R2FtaW5nTWFzdGVyMUNOWV9hdXRoOjlGSE9SUWRHVFp3cURYRkBeaVpeS1JNZ1U=");
			headerMap.put("X-DAS-TZ", "UTC+8");
			headerMap.put("X-DAS-CURRENCY", "CNY");
			headerMap.put("X-DAS-TX-ID", "TEXT-TX-ID");
			headerMap.put("X-DAS-LANG", "zh-CN");
			
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("grant_type", plat.getDeskey());
			body.put("username", plat.getApiaccount());
			body.put("password", plat.getCiphercode());
			String result =HttpUtil.doPost(plat.getDomainname()+"/oauth/token", headerMap, body, "UTF-8");
			logger.info("获取MG TOKEN返回："+result);
			JSONObject json = JSONObject.fromObject(result);
			if(json.containsKey("access_token")){
				iRedisService.addToRedis(WebConst.MG_TOKEN, json.getString("access_token"));
				return json.getString("access_token");
			}
			return "";
		}
		return "";
	}
}
