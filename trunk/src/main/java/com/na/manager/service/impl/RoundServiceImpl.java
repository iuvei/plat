package com.na.manager.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.na.manager.bean.NaResponse;
import com.na.manager.bean.RoundCorrectDataRequest;
import com.na.manager.common.redis.RedisClient;
import com.na.manager.common.redis.RedisEventData;
import com.na.manager.dao.IRoundExtMapper;
import com.na.manager.dao.IRoundMapper;
import com.na.manager.entity.Round;
import com.na.manager.entity.RoundExt;
import com.na.manager.enums.GameEnum;
import com.na.manager.remote.IPlatformUserRemote;
import com.na.manager.service.IRoundService;
import com.na.manager.util.DateUtil;

@Service
public class RoundServiceImpl implements IRoundService {

	@Autowired
	private IRoundMapper roundMapper;
	
	@Autowired
	private IRoundExtMapper roundExtMapper;  
	
	@Autowired
	private RedisClient redisClient;
	
	@Autowired
	private IPlatformUserRemote platformUserRemote;

	@Override
	public List<String> listGameLand(String bid, String gid, String tid) {
		return roundMapper.listRoundResults(bid, Integer.parseInt(gid), Integer.parseInt(tid));

	}

	@Override
	public LinkedHashMap<String, Object> listGameResult(Integer pageNumber, Integer pageSize,
			Integer gid, Integer tid, Date startDt, Date endDt) {
		List<Round> round = null;
		Integer count = null;
		if(gid == 3){
			round = roundMapper.listRouletteGameResult(pageNumber*pageSize,pageSize,
					gid, tid,startDt,endDt);
			count = roundMapper.countRouletteGameResult(gid, tid,startDt,endDt);
		}else if(gid == 1){
			round = roundMapper.listBaccaratGameResult(pageNumber*pageSize,pageSize,
					gid, tid,startDt,endDt);
			count = roundMapper.countBaccaratGameResult(gid, tid,startDt,endDt);
		}
		LinkedHashMap<String, Object> datamap = Maps.newLinkedHashMapWithExpectedSize(2);
		datamap.put("total", count);
		datamap.put("rows", round);
		return datamap;
	}

	@Override
	public List<Map> listAbnormalTableRound(Integer tid) {
		List<Map> listMap = roundMapper.listAbnormalTableRound(tid);
		for (Map map : listMap) {
			java.sql.Timestamp timestamp= (java.sql.Timestamp)map.get("startTime");
			java.sql.Timestamp endtimestamp= (java.sql.Timestamp)map.get("endTime");
			map.put("startTime", DateUtil.longToDate(timestamp.getTime()));
			if(endtimestamp != null){
				map.put("endTime", DateUtil.longToDate(endtimestamp.getTime()));
			}
		}
		return listMap;
	}

	@Override
	public Boolean updateRoundAndRoundExt(RoundCorrectDataRequest param) throws ParseException {
		
		Round round = roundMapper.getRoundById(param.getRid());
		Date startTime = round.getStartTime();

		if(param.getGameId() == 3 && round != null ){
			Preconditions.checkArgument(!(Strings.isNullOrEmpty(param.getResult()) || param.getResult().equals("undefined")),"param.error");
			round.setResult(param.getResult());
			round.setEndTime(new Date());
			int count = roundMapper.updateRoundById(round);;
			if(count == 1) return true;
			return false;
		}
		RoundExt roundExt = roundExtMapper.getRoundExtById(param.getRid());
		if(roundExt == null){
			roundExt = new RoundExt();
			roundExt.setRoundId(param.getRid());
		}
		roundExt.setBankCard1Mode(param.getBankCard1Mode());
		roundExt.setBankCard2Mode(param.getBankCard2Mode());
		roundExt.setBankCard3Mode(param.getBankCard3Mode());
		roundExt.setBankCard1Number(param.getBankCard1Number());
		roundExt.setBankCard2Number(param.getBankCard2Number());
		roundExt.setBankCard3Number(param.getBankCard3Number());

		roundExt.setPlayerCard1Mode(param.getPlayerCard1Mode());
		roundExt.setPlayerCard2Mode(param.getPlayerCard2Mode());
		roundExt.setPlayerCard3Mode(param.getPlayerCard3Mode());
		roundExt.setPlayerCard1Number(param.getPlayerCard1Number());
		roundExt.setPlayerCard2Number(param.getPlayerCard2Number());
		roundExt.setPlayerCard3Number(param.getPlayerCard3Number());
		round.setEndTime(new Date());
		
		int count = roundMapper.updateRoundById(round);;
		int count1 = roundExtMapper.updateRoundExtById(roundExt);
		if(count == 1 && count1 ==1) return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public NaResponse<Object> settleBetOrders(RoundCorrectDataRequest param) {
		RoundExt roundExt = roundExtMapper.getRoundExtById(param.getRid());
		if(param.getGameId() == GameEnum.ROULETTE.get()){ //轮盘
			Round round =roundMapper.getRoundById(param.getRid());
			Preconditions.checkArgument(!Strings.isNullOrEmpty(round.getResult()),"abnormaltable.sava.result");
			RedisEventData redisEventData = new RedisEventData();
			redisEventData.setRedisEventType(RedisEventData.RedisEventType.AbnormalGameResult.get());
			redisEventData.setRedisEventData(round.getId());
			redisClient.publishGameServer(JSON.toJSONString(redisEventData));
			return NaResponse.createSuccess();
		}
		// 百家乐
		Preconditions.checkArgument(!(Strings.isNullOrEmpty(roundExt.getBankCard1Mode())||Strings.isNullOrEmpty(roundExt.getBankCard2Mode())),"abnormaltable.sava.result");
		Preconditions.checkArgument(!(Strings.isNullOrEmpty(roundExt.getPlayerCard1Mode())||Strings.isNullOrEmpty(roundExt.getPlayerCard2Mode())),"abnormaltable.sava.result");
		Preconditions.checkArgument(!(roundExt.getBankCard1Number() == 0 || roundExt.getBankCard2Number()== 0),"abnormaltable.sava.result");
		Preconditions.checkArgument(!(roundExt.getPlayerCard1Number() == 0 || roundExt.getPlayerCard2Number() == 0),"abnormaltable.sava.result");
		Preconditions.checkArgument(!(Strings.isNullOrEmpty(roundExt.getBankCard3Mode())&& roundExt.getBankCard3Number() != null && roundExt.getBankCard3Number() != 0),"abnormaltable.sava.result");
		Preconditions.checkArgument(!(!Strings.isNullOrEmpty(roundExt.getBankCard3Mode())&& roundExt.getBankCard3Number() == null && roundExt.getBankCard3Number() == 0),"abnormaltable.sava.result");
		Preconditions.checkArgument(!(Strings.isNullOrEmpty(roundExt.getPlayerCard3Mode()) && roundExt.getPlayerCard3Number() != null && roundExt.getPlayerCard3Number() != 0),"abnormaltable.sava.result");
		Preconditions.checkArgument(!(!Strings.isNullOrEmpty(roundExt.getPlayerCard3Mode()) && roundExt.getPlayerCard3Number() == null && roundExt.getPlayerCard3Number() == 0),"abnormaltable.sava.result");


		RedisEventData redisEventData = new RedisEventData();
		redisEventData.setRedisEventType(RedisEventData.RedisEventType.AbnormalGameResult.get());
		redisEventData.setRedisEventData(roundExt.getRoundId());
		redisClient.publishGameServer(JSON.toJSONString(redisEventData));
		
		platformUserRemote.sendRound(param.getRid());
		return NaResponse.createSuccess();
	}

}
