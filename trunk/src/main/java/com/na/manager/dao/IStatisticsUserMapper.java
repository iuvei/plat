package com.na.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author andy
 * @date 2017年6月22日 下午5:07:12
 * 
 */
@Mapper
public interface IStatisticsUserMapper {
	
	void add(@Param("beginTime") String beginTime, @Param("endTime") String endTime);
	
	/**
	 * 查询当前在线人数
	 * @param userId
	 * @return
	 */
	Long selectOnlineNumberNow(long userId);
	
	/**
	 * 按天查询注册人数
	 * @param userId
	 * @return
	 */
	List<?> selectRegisterNumberByDay(long userId);
	
	/**
	 * 按天查询投注人数
	 * @param userId
	 * @return
	 */
	List<?> selectBetNumberByDay(long userId);
	
	/**
	 * 按天查询投注量
	 * @param userId
	 * @return
	 */
	List<?> selectBetTotalByDay(long userId);
	
	/**
	 * 查询投注排行
	 * @param userId
	 * @return
	 */
	List<?> selectBetTotalRankByDay(long userId);

}
