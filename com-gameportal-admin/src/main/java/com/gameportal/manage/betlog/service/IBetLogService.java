package com.gameportal.manage.betlog.service;

import java.util.List;
import java.util.Map;

import com.gameportal.manage.betlog.model.BetClearing;
import com.gameportal.manage.betlog.model.BetLog;
import com.gameportal.manage.betlog.model.BetLogTotal;
import com.gameportal.manage.betlog.model.BetLogVo;



public abstract interface IBetLogService {

	BetLog save(BetLog entity);
	
	boolean modify(BetLog entity);

	boolean deleteById(Long id);

	List<BetLog> queryForList(Map<String,Object> map, Integer startNo,
			Integer pageSize);
	
	 Long queryForCount(Map map);
	 
	 BetLog findEntityById(Long id);
	 
	 List<BetLogVo> queryForListVo(Map map, Integer startNo,Integer pageSize);
	 Long getMaxBetNo(Map map);
	 
	 /**
		 * 查询用户投注统计信息
		 * @param params
		 * @param thisPage
		 * @param pageSize
		 * @return
		 */
		public List<BetLogTotal> selectBetTotal(Map<String, Object> params,int thisPage,int pageSize);
		
		/**
		 * 查询注单统计数据做报表
		 * @param params
		 * @return
		 */
		public List<BetLogTotal> selectBetTotalForReport(Map<String, Object> params);
		/**
		 * 查询需要洗码的会员
		 * @param params
		 * @return
		 */
		public List<BetLogTotal> selectXimaBetTotal(Map<String, Object> params,int thisPage,int pageSize);
		
		/**
		 * 统计要洗码的会员
		 * @param params
		 * @return
		 */
		public Long selectXimaBetTotalCount(Map<String, Object> params);
		
		/**
		 * 统计用户投注总数条数
		 * @param params
		 * @return
		 */
		public Long selectBetTotalCount(Map<String, Object> params);
		
		/**
		 * 获取会员盈亏
		 * @param params
		 * @return
		 */
		public String getProxyLoss(Map<String, Object> params);
		
		/**
		 * 查询结算代理
		 * @param params
		 * @return
		 */
		public List<BetClearing> getBetClearing(Map<String, Object> params);
		
		/**
		 * 查询代理洗码
		 * @param params
		 * @return
		 */
		public List<BetClearing> selectProxyXima(Map<String, Object> params);
		
		/**
		 * 代理报表查询
		 * @param params
		 * @param thisPage
		 * @param pageSize
		 * @return
		 */
		public List<BetLogTotal> selectProxyDownUserXima(Map<String, Object> params,int thisPage,int pageSize);
		public Long selectProxyDownUserXimaCount(Map<String, Object> params);
		/**
		 * 统计代理下线所有优惠+洗码
		 * @param params
		 * @return
		 */
		public String getProxyPreferential(Map<String, Object> params);
}
