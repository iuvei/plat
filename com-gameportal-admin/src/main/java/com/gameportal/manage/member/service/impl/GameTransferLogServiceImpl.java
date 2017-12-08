package com.gameportal.manage.member.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.gameportal.manage.member.dao.GameTransferLogDao;
import com.gameportal.manage.member.model.GameTransferLog;
import com.gameportal.manage.member.service.IGameTransferLogService;

@Service
public class GameTransferLogServiceImpl implements IGameTransferLogService {

	@Resource(name = "gameTransferLogDao")
	private GameTransferLogDao gameTransferLogDao = null;
	
	@Override
	public GameTransferLog findGameTransferLogId(Long uiid) {
		return (GameTransferLog) gameTransferLogDao.findById(uiid);
		
	}
	
	@Override
	public boolean saveGameTransferLog(GameTransferLog gameTransferLog) throws Exception {
		return gameTransferLogDao.saveOrUpdate(gameTransferLog);
		
	}

	@Override
	public boolean deleteGameTransferLog(Long uiid) {
		return	gameTransferLogDao.delete(uiid);
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<GameTransferLog> queryGameTransferLog(Map paramMap, Integer startNo,
			Integer pagaSize) {
		paramMap.put("limit", true);
		paramMap.put("thisPage", startNo);
		paramMap.put("pageSize", pagaSize);
		List<GameTransferLog> list = gameTransferLogDao.getList(paramMap);
		return list;
	}

	@Override
	public Long queryGameTransferLogCount(Map map) {
		return gameTransferLogDao.getRecordCount(map);
	}
}
