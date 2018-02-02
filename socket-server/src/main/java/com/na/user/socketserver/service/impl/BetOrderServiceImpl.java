package com.na.user.socketserver.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.na.baccarat.socketserver.common.enums.AccountRecordTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderBetTypeEnum;
import com.na.baccarat.socketserver.common.enums.BetOrderStatusEnum;
import com.na.baccarat.socketserver.entity.AccountRecord;
import com.na.baccarat.socketserver.entity.BetOrder;
import com.na.baccarat.socketserver.entity.TradeItem;
import com.na.baccarat.socketserver.util.SnowflakeIdWorker;
import com.na.user.socketserver.dao.IBetOrderMapper;
import com.na.user.socketserver.entity.LiveUserPO;
import com.na.user.socketserver.entity.RoundPO;
import com.na.user.socketserver.entity.UserPO;
import com.na.user.socketserver.service.IAccountRecordService;
import com.na.user.socketserver.service.IBetOrderService;
import com.na.user.socketserver.service.ITradeItemService;
import com.na.user.socketserver.util.ActiveMQUtil;


/**
 * Created by sunny on 2017/4/26 0026.
 */
@Service
public class BetOrderServiceImpl implements IBetOrderService {
	
    @Autowired
    private IBetOrderMapper betOrderMapper;
    
    @Autowired
    private ITradeItemService tradeItemService;

	@Resource(name = "betOrderSnowflakeIdWorker")
	private SnowflakeIdWorker betOrderSnowflakeIdWorker;
	
	@Resource(name = "accountRecordSnowflakeIdWorker")
    private SnowflakeIdWorker accountRecordSnowflakeIdWorker;

	@Autowired
	private IAccountRecordService accountRecordService;
	
	@Autowired
	private ActiveMQUtil activeMQUtil;

	@Override
	public List<BetOrder> getBetOrderByTable(Integer tableId, RoundPO round) {
		List<BetOrder> list = betOrderMapper.getBetOrderByTable(tableId, round.getId());
		for(BetOrder order : list) {
			TradeItem tradeItem = tradeItemService.getTradeItemById(order.getTradeItemId());
			order.setTradeItem(tradeItem);
		}
		return list;
	}
	
	@Override
	public List<BetOrder> getBetOrderByTable(Integer tableId, BetOrderBetTypeEnum typeEnum, RoundPO round) {
		List<BetOrder> list = betOrderMapper.getBetOrderByTableAndType(tableId, typeEnum.get(), round.getId());
		for(BetOrder order : list) {
			TradeItem tradeItem = tradeItemService.getTradeItemById(order.getTradeItemId());
			order.setTradeItem(tradeItem);
		}
		return list;
	}

	@Override
	public List<BetOrder> getBetOrderByVirtualTable(Integer virtualTableId, RoundPO round) {
		List<BetOrder> list = betOrderMapper.getBetOrderByVirtualTable(virtualTableId, round.getId());
		for(BetOrder order : list) {
			TradeItem tradeItem = tradeItemService.getTradeItemById(order.getTradeItemId());
			order.setTradeItem(tradeItem);
		}
		return list;
	}
	
	@Override
	public List<BetOrder> getBetOrderByRound(RoundPO round) {
		List<BetOrder> list = betOrderMapper.getBetOrderByRound(round.getId());
		for(BetOrder order : list) {
			TradeItem tradeItem = tradeItemService.getTradeItemById(order.getTradeItemId());
			order.setTradeItem(tradeItem);
		}
		return list;
	}
	
	
	@Override
	public List<BetOrder> getBetOrderByRoundNoTradeItem(RoundPO round) {
		List<BetOrder> list = betOrderMapper.getBetOrderByRoundNoTradeItem(round.getId());
		return list;
	}

	@Override
	public void update(BetOrder order) {
		
		betOrderMapper.update(order);
	}

	@Override
	public void addBetOrder(UserPO user, BetOrder betOrder) {
		AccountRecord acountRecord = new AccountRecord();
		acountRecord.setUserId(user.getId());
		acountRecord.setPreBalance(user.getBalance());
		acountRecord.setAmount(betOrder.getAmount());
		acountRecord.setType(AccountRecordTypeEnum.BET.get());
		accountRecordService.add(acountRecord);

		betOrder.setStatus(BetOrderStatusEnum.CONFIRM.get());
		betOrder.setId(betOrderSnowflakeIdWorker.nextId());
		betOrderMapper.addBetOrder(betOrder);
	}

	@Override
	public void batchUpdate(List<BetOrder> orderList) {
		betOrderMapper.batchUpdate(orderList);
	}

	/**
	 * 查询好路的订单
	 */
	@Override
	public List<BetOrder> getBetOrderByTable(int tid, RoundPO round, int source) {
		List<BetOrder> list = betOrderMapper.getBetOrderByTableSource(tid,round.getId(),source);
		for(BetOrder order : list) {
			TradeItem tradeItem = tradeItemService.getTradeItemById(order.getTradeItemId());
			order.setTradeItem(tradeItem);
		}
		return list;
	}
	
	

	@Override
	public List<Map> getBetOrderByUser(Long userId, Integer begin, Integer end, String beginDate, String endDate) {
		
		List<Map> list = betOrderMapper.getBetOrderByUser(userId, begin, end, beginDate, endDate);
		return list;
	}

	@Override
	public Integer getBetOrderByUserCount(Long userId, String beginDate, String endDate) {
		return betOrderMapper.getBetOrderByUserCount(userId, beginDate, endDate);
	}
 
	@Override
	public Integer getBetOrderByRoundCount(Long userId, Long roundId) {
		return betOrderMapper.getBetOrderByRoundCount(userId, roundId);
	}

	/**
	 * 批量加入订单
	 */
	@Override
	public void batchAddBetOrders(UserPO user, List<BetOrder> betOrderList, Integer tableId, Long roundId) {
		
		JSONObject json = new JSONObject();
		json.put("tableId", tableId);
		json.put("roundId", roundId);
		String strinJson = json.toJSONString();
		
		List<AccountRecord> accountRecordList = new ArrayList<>();
		betOrderList.forEach(item ->{
			item.setStatus(BetOrderStatusEnum.CONFIRM.get());
			item.setId(betOrderSnowflakeIdWorker.nextId());
			
			AccountRecord accountRecord = new AccountRecord();
			long id = accountRecordSnowflakeIdWorker.nextId();
			accountRecord.setUserId(user.getId());
			accountRecord.setPreBalance(item.getUserPreBalance());
			accountRecord.setAmount(item.getAmount().negate());
			accountRecord.setType(AccountRecordTypeEnum.BET.get());
			accountRecord.setTime(item.getBetTime());
			accountRecord.setId(id);
			accountRecord.setSn("ZR" + id);
			accountRecord.setExecUser(user.getLoginName());
			accountRecord.setBusinessKey(item.getId() + "");
			accountRecord.setRemark(strinJson);
	        accountRecordList.add(accountRecord);
		});
		
		accountRecordService.batchAddAcountRecord(accountRecordList);
		betOrderMapper.batchAddBetOrder(betOrderList);
		
		Map<String,Object> headers = new HashMap<>();
		headers.put("path",((LiveUserPO)user).getParentPath());
		activeMQUtil.send(accountRecordList, headers);
	}
	
	/**
	 * 批量加入订单
	 */
	@Override
	public void batchAddBetOrders(List<BetOrder> betOrderList) {
		
		List<AccountRecord> accountRecordList = new ArrayList<>();
		betOrderList.forEach(item ->{
			item.setStatus(BetOrderStatusEnum.CONFIRM.get());
			item.setId(betOrderSnowflakeIdWorker.nextId());
			
			AccountRecord accountRecord = new AccountRecord();
			long id = accountRecordSnowflakeIdWorker.nextId();
			accountRecord.setId(id);
			accountRecord.setSn("ZR" + id);
			accountRecord.setUserId(item.getUserId());
			accountRecord.setPreBalance(item.getUserPreBalance());
			accountRecord.setAmount(item.getAmount().negate());
			accountRecord.setType(AccountRecordTypeEnum.BET.get());
			accountRecord.setTime(new Date());
			accountRecord.setExecUser(item.getLoginName());
			accountRecord.setBusinessKey(item.getId() + "");
	        accountRecordList.add(accountRecord);
		});
		
		accountRecordService.batchAddAcountRecord(accountRecordList);
		betOrderMapper.batchAddBetOrder(betOrderList);
	}

	@Override
	public Integer findUnSettleCount(Long userId) {
		return betOrderMapper.findUnSettleCount(userId);
	}

}
