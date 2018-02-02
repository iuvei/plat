package com.na.manager.service;

import java.util.List;
import java.util.Map;


/**
 * 
 * 
 * @author alan
 * @date 2017年12月19日 下午4:50:08
 */
public interface IStatisticsUserService {
	
	void add(String beginTime, String endTime);
	
	/**
	 * 查询当前在线人数
	 * @return
	 */
	Long selectOnlineNumberNow();
	
	/**
	 * 按天查询注册人数
	 * @return
	 */
	List<?> selectRegisterNumberByDay();
	
	/**
	 * 按天查询投注人数
	 * @return
	 */
	List<?> selectBetNumberByDay();
	
	/**
	 * 按天查询投注量
	 * @return
	 */
	List<?> selectBetTotalByDay();
	
	/**
	 * 查询投注排行
	 * @return
	 */
	Map<Object, Object> selectBetTotalRankByDay();

}
