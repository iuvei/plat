package com.gameportal.web.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gameportal.web.user.model.BetLogTotal;

@Component
@SuppressWarnings("all")
public class BetLogDao extends BaseIbatisDAO {

	@Override
	public Class getEntityClass() {
		return BetLogTotal.class;
	}
	
	/**
	 * 查询可洗码数据
	 * @param params
	 * @return
	 */
	public List<BetLogTotal> selectXimaBetTotal(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectXimaBetTotal",params);
	}
	
	public long selectXimaBetTotalCount(Map<String, Object> params){
		return (Long) getSqlMapClientTemplate().queryForObject(
				getSimpleName() + ".selectXimaBetTotalCount", params);
	}
	
	public List<Map<String,String>> selectSum(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectSum", params);
	}
	
	public long sumBetTotalAmount(Map<String, Object> params){
		return (Long) getSqlMapClientTemplate().queryForObject(getSimpleName() + ".sumBetTotalAmount", params);
	}

	public long selectUserClearnFlag(Map<String, Object> params){
		return (Long) getSqlMapClientTemplate().queryForObject(
				getSimpleName() + ".selectUserClearnFlag", params);
	}
	
	/**
	 * 查找指定日期的玩家
	 * @param params
	 * @return
	 */
	public List<String> selectGameMemberByDate(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectGameMemberByDate", params);
	}
	
	/**
	 * 查找每周的玩家
	 * @param params
	 * @return
	 */
	public List<String> selectGameMemberByWeek(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectGameMemberByWeek", params);
	}
}
