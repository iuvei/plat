package com.na.user.socketserver.service;

import java.util.List;
import java.util.Map;

import com.na.baccarat.socketserver.common.enums.BetOrderBetTypeEnum;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.UserPO;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
public interface IBetOrderService {
	
	/**
	 * 获取该局对应的用户下注记录
	 * @return
	 */
	public List<BetOrder> getBetOrderByTable(Integer tableId,RoundPO round);
	
	/**
	 * 获取该局对应的用户下注记录
	 * @return
	 */
	public List<BetOrder> getBetOrderByTable(Integer tableId, BetOrderBetTypeEnum typeEnum, RoundPO round);
	
    /**
     * 获取该局对应的用户下注记录
     * @return
     */
	public List<BetOrder> getBetOrderByVirtualTable(Integer virtualTableId,RoundPO round);
	/**
	 * 查询该局所有用户投注记录
	 * @param round
	 * @return
	 */
	public List<BetOrder> getBetOrderByRound(RoundPO round);
	
	
	/**
	 * 查询该用户最近投注记录总数
	 * @param round
	 * @return
	 */
	public Integer getBetOrderByUserCount(Long userId, String beginDate, String endDate);
	
	/**
	 * 查询该用户最近投注记录
	 * @param round
	 * @return
	 */
	public List<Map> getBetOrderByUser(Long userId, Integer pageNumber, Integer pageSize, String beginDate, String endDate);
	
	/**
	 * 查询该局庄或闲投注记录(不含交易项)
	 * @param round
	 * @return
	 */
	public List<BetOrder> getBetOrderByRoundNoTradeItem(RoundPO round);
	
	public void addBetOrder(UserPO user, BetOrder betOrder);

	public void update(BetOrder order);
	
	/**
	 * 批量更新
	 * @param orderList
	 */
	public void batchUpdate(List<BetOrder> orderList);
	
	/**
	 * 查询当局好路的订单
	 * @param tid
	 * @param roundPO
	 * @param i
	 */
	public List<BetOrder> getBetOrderByTable(int tid, RoundPO roundPO, int i);
	
	/**
	 * 查询该用户该局投注记录总数
	 * @param round
	 * @return
	 */
	public Integer getBetOrderByRoundCount(Long userId, Long roundId);

	/**
	 * 批量加入订单
	 * @param userPO
	 * @param betOrderList
	 */
	public void batchAddBetOrders(UserPO userPO, List<BetOrder> betOrderList, Integer tableId, Long roundId);
	
	/**
	 * 批量加入订单
	 * @param userPO
	 * @param betOrderList
	 */
	public void batchAddBetOrders(List<BetOrder> betOrderList);
	
	/**
	 * 查找未结算订单
	 * @param userId
	 * @return
	 */
	public Integer findUnSettleCount(Long userId);
}
