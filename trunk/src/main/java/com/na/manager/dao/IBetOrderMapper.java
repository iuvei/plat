package com.na.manager.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.na.manager.bean.BaccaratLiveBean;
import com.na.manager.bean.BetOrderReportRequest;
import com.na.manager.bean.LiveBetOrder;
import com.na.manager.bean.RouletteLiveBetBean;
import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.entity.BetOrder;

/**
 * @author andy
 * @date 2017年6月22日 下午2:28:42
 * 
 */
@Mapper
public interface IBetOrderMapper {
	List<BetOrderVO> queryBetOrderByPage(BetOrderReportRequest reportRequest);
	
	Map<String, BigDecimal> queryBetOrderByPageTotal(BetOrderReportRequest reportRequest);
	
	long getBetOrderCount(BetOrderReportRequest reportRequest);

	@Select("select * from bet_order where round_id = #{rid} and game_table_id = #{tid}")
	List<BetOrder> listBetOrders(@Param("rid")Long rid, @Param("tid")Integer gameTableId);
	
	@Update("update bet_order set status = #{status} where id = #{id}")
	void updateBetOrderStatus(@Param("id")Long id, @Param("status")int status);

	/**
	 * 查询百家乐现场投注情况。
	 * @return
	 */
	List<BaccaratLiveBean> queryBaccartLiveInfo(@Param("path") String path);

	/**
	 * 查询轮盘现场投注情况。
	 * @return
	 */
	List<RouletteLiveBetBean> queryRouletteLiveInfo(@Param("path") String path);

	/**
	 * 统计现场投注人数。
	 * @return
	 */
	int countLive(String path);

	List<LiveBetOrder> queryTopBet(@Param("roundId") Long roundId, @Param("path") String path);

	/**
	 * 账户资金调整后，需要调整当前未结算的最后一笔订单余额为用户当前余额。
	 * @param userId
	 */
	void updateModifiedUserBalance(@Param("userId") Long userId);


	BetOrderVO findBetOrderDetail(@Param("betId") Long betId,@Param("path") String path);
	
	@Select("select count(*) from bet_order where user_id=#{userId} and status in(1,2) and bet_time>=DATE_ADD(NOW(),INTERVAL -1 HOUR)")
	long findUnsettlementOrder(@Param("userId") Long userId);

}
