package com.gameportal.web.game.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.web.game.model.PlatformBetStats;
import com.gameportal.web.user.dao.BaseIbatisDAO;

@Component
public class PlatformBetStatsDao extends  BaseIbatisDAO{

	@Override
	public Class<PlatformBetStats> getEntityClass() {
		return PlatformBetStats.class;
	}
	
	/**
	 * 根据时间从betlog中查询各个游戏平台的投注统计
	 * @return
	 */
	public List<PlatformBetStats> selectBetFromBetlogByDate(Date date) {
		Map<String,Object> params = new HashMap<>();
		params.put("date", date);
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectBetFromBetlogByDate", params);
	}

	/**
	 * 批量添加游戏平台投注统计
	 * @param list
	 */
	public void batchAddPlatformBetStats(List<PlatformBetStats> list) {
		// TODO Auto-generated method stub
	}
	
	public List<PlatformBetStats> selectBetFromBetlogByUser(Map<String, Object> params) {
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectBetFromBetlogByUser", params);
	}
	
}
