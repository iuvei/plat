package com.na.user.socketserver.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.baccarat.socketserver.cache.BaccaratCache;
import com.na.baccarat.socketserver.common.enums.RoundStatusEnum;
import com.na.user.socketserver.dao.IRoundExtMapper;
import com.na.user.socketserver.dao.IRoundMapper;
import com.na.user.socketserver.entity.GameTablePO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.service.IRoundService;
import com.na.user.socketserver.util.DateUtil;

/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class RoundServiceImpl implements IRoundService {
	
    @Autowired
    private IRoundMapper roundMapper;
    @Autowired
    private IRoundExtMapper roundExtMapper;

	@Override
	public RoundPO initLastRound(GameTablePO table) {
		RoundPO round = roundMapper.findByGidAndTid(table.getGameId(), table.getId());
		if(round == null) {
			round = initRound(table);
		}
		return round;
	}
	
	private RoundPO initRound(GameTablePO table) {
		RoundPO round = new RoundPO();
		// 初始化 最新的 未初始化过的数据
//		round.setStatus(TableStatusEnum.INACTIVE.getStatus());
		round.setGameId(BaccaratCache.getGame().getGamePO().getId());
		// 初始化 第几薛 和薛ID
		round.setGameTableId(table.getId());
		round.setBootId(DateUtil.format(new Date(), DateUtil.yyyyMMdd) + "-" + 1);
		round.setBootNumber(1);
		Date d = new Date();
		round.setBootStartTime(d);
		round.setRoundNumber(0);
		round.setStartTime(d);
		round.setStatusEnum(RoundStatusEnum.INACTIVE);
		add(round);
		return round;
	}

	@Override
	public void add(RoundPO round) {
		roundMapper.add(round);
	}

	@Override
	public void update(RoundPO round) {
		roundMapper.update(round);
	}

	@Override
	public RoundPO getRound(Integer roundId) {
		return roundMapper.findByRid(roundId);
	}
}
