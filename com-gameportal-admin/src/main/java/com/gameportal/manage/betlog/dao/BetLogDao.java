package com.gameportal.manage.betlog.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.gameportal.manage.betlog.model.BetClearing;
import com.gameportal.manage.betlog.model.BetLog;
import com.gameportal.manage.betlog.model.BetLogTotal;
import com.gameportal.manage.member.model.DailyReportDetail;
import com.gameportal.manage.system.dao.BaseIbatisDAO;

@Component
public class BetLogDao extends BaseIbatisDAO {

	public Class<BetLog> getEntityClass() {
		return BetLog.class;
	}

	public boolean saveOrUpdate(BetLog entity) {
		if (entity.getPdid() == null)
			return StringUtils.isNotBlank(ObjectUtils.toString(save(entity))) ? true
					: false;
		else
			return update(entity);
	}

	public Long selectBackAmount(Map<String, Object> params) {
		return (Long) getSqlMapClientTemplate().queryForObject(
				getSimpleName() + ".selectBackAmount", params);
	}
	
	public List<BetLog> getList(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".pageSelect", params);
	} 
	
	/**
	 * 计算用户游戏投注总额-用于洗码
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> selectSum(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectSum", params);
	}
	
	/**
	 * 查询用户投注统计信息
	 * @param params
	 * @param thisPage
	 * @param pageSize
	 * @return
	 */
	public List<BetLogTotal> selectBetTotal(Map<String, Object> params,int thisPage,int pageSize){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectBetTotal", params);
	}
	
	/***
	 * 查询需要洗码的会员
	 * @param params
	 * @return
	 */
	public List<BetLogTotal> selectXimaBetTotal(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectXimaBetTotal", params);
	}
	
	/**
	 * 统计要洗码的会员
	 * @param params
	 * @return
	 */
	public Long selectXimaBetTotalCount(Map<String, Object> params){
		return (Long) getSqlMapClientTemplate().queryForObject(
				getSimpleName() + ".selectXimaBetTotalCount", params);
	}
	
	/**
	 * 查询代理下线，洗码
	 * @param params
	 * @return
	 */
	public List<BetLogTotal> selectProxyDownUserXima(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyDownUserXima", params);
	}
	
	
	public List<BetLogTotal> selectProxyDownUserXima(Map<String, Object> params,int thisPage,int pageSize){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyDownUserXima", params,thisPage,pageSize);
	}
	
	/**
	 * 统计查询代理下线，洗码
	 * @param params
	 * @return
	 */
	public Long selectProxyDownUserXimaCount(Map<String, Object> params){
		return super.getRecordCount(getSimpleName()+".selectProxyDownUserXimaCount", params);
	}
	
	/**
	 * 统计用户投注总数条数
	 * @param params
	 * @return
	 */
	public Long selectBetTotalCount(Map<String, Object> params) {
		return super.getRecordCount(getSimpleName()+".selectBetTotalCount", params);
	}
	
	/**
	 *  查询代理下级会员信息
	 * @param params
	 * @param thisPage
	 * @param pageSize
	 * @return
	 */
	public List<BetLogTotal> selectProxyDownUser(Map<String, Object> params,int thisPage,int pageSize){
		return super.getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyDownUser", params);
	}
	
	public Long selectProxyDownUserCount(Map<String, Object> params){
		return super.getRecordCount(getSimpleName()+".selectProxyDownUserCount", params);
	}
	
	/**
	 * 获取会员盈亏
	 * @param params
	 * @return
	 */
	public String getProxyLoss(Map<String, Object> params){
		return (String)super.queryForObject(getSimpleName()+".getProxyLoss", params);
	}
	
	/**
	 * 统计代理下线所有优惠+洗码
	 * @param params
	 * @return
	 */
	public String getProxyPreferential(Map<String, Object> params){
		return (String)super.queryForObject(getSimpleName()+".getProxyPreferential", params);
	}
	
	/**
	 * 查询结算代理数据
	 * @param params
	 * @return
	 */
	public List<BetClearing> getBetClearing(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyClearing",params);
	}
	
	/**
	 * 代理洗码
	 * @param params
	 * @return
	 */
	public List<BetClearing> selectProxyXima(Map<String, Object> params){
		return getSqlMapClientTemplate().queryForList(getSimpleName()+".selectProxyXima",params);
	}
	
	
	public void insertStatDailyReport(DailyReportDetail report){
		getSqlMapClientTemplate().insert(getSimpleName() + ".insertStatDailyReport",
				report);
	}
}
