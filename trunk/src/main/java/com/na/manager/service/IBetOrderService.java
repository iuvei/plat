package com.na.manager.service;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.na.manager.bean.BetOrderReportRequest;
import com.na.manager.bean.LiveBetOrder;
import com.na.manager.bean.LiveBetResponse;
import com.na.manager.bean.Page;
import com.na.manager.bean.RoundCorrectDataRequest;
import com.na.manager.bean.vo.BetOrderVO;

/**
 * @author andy
 * @date 2017年6月23日 下午4:32:36
 * 
 */
public interface IBetOrderService {
	Page<BetOrderVO> queryBetOrderByPage(BetOrderReportRequest reportRequest);
	
	Page<BetOrderVO> queryRemoteBetOrderByPage(BetOrderReportRequest reportRequest);

	Set<Long> invalidBetOrders(RoundCorrectDataRequest param);

	/**
	 * 查询现场投注记录。包括在线人数、投注人数、投注排名。
	 * @return
	 */
	LiveBetResponse queryLiveBet();

	List<LiveBetOrder> queryTopBet(Long roundId);

	/**
	 * 账户资金调整后，需要调整当前未结算的最后一笔订单余额为用户当前余额。
	 * @param userId
	 */
	void updateModifiedUserBalance(@Param("userId") Long userId);

	/**
	 * 查询订单详情
	 * @param betId
	 * @return
	 */
	BetOrderVO findBetOrderDetail(Long betId);
	
	long findUnsettlementOrder(Long userId);
}
