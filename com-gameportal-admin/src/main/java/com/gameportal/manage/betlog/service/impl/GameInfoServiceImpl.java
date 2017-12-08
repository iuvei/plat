package com.gameportal.manage.betlog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.gameportal.manage.betlog.dao.GameInfoDao;

import com.gameportal.manage.betlog.model.GameInfo;
import com.gameportal.manage.betlog.service.IGameInfoService;


@Service
public class GameInfoServiceImpl implements IGameInfoService {

	@Resource(name = "gameInfoDao")
	private GameInfoDao gameInfoDao = null;
	
	@Override
	public boolean deleteById(Long id) {
	
		return gameInfoDao.delete(id);
	}

	@Override
	public GameInfo findEntityById(Long id) {
		return (GameInfo) gameInfoDao.findById(id);
	}

	@Override
	public boolean modify(GameInfo entity) {
		return gameInfoDao.saveOrUpdate(entity);
	}

	@Override
	public Long queryForCount(Map map) {
		return gameInfoDao.getRecordCount(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GameInfo> queryForList(Map map, Integer startNo, Integer pageSize) {
		List<GameInfo> list = gameInfoDao.queryForPager(map,
				startNo, pageSize);
		return list;
	}

	@Override
	public GameInfo save(GameInfo entity) {
		GameInfo gameInfo= (GameInfo) gameInfoDao.save(entity);
		return StringUtils.isNotBlank(ObjectUtils.toString(gameInfo.getGid())) ? gameInfo : null;
	}

}
