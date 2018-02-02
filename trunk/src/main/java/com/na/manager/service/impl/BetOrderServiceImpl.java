package com.na.manager.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.na.manager.bean.BaccaratLiveBean;
import com.na.manager.bean.BetOrderReportRequest;
import com.na.manager.bean.LiveBetBean;
import com.na.manager.bean.LiveBetOrder;
import com.na.manager.bean.LiveBetResponse;
import com.na.manager.bean.Page;
import com.na.manager.bean.RouletteLiveBetBean;
import com.na.manager.bean.RoundCorrectDataRequest;
import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.cache.AppCache;
import com.na.manager.dao.IAccountRecordMapper;
import com.na.manager.dao.IBetOrderMapper;
import com.na.manager.dao.ILiveUserMapper;
import com.na.manager.dao.IRoundMapper;
import com.na.manager.dao.IUserMapper;
import com.na.manager.entity.AccountRecord;
import com.na.manager.entity.BetOrder;
import com.na.manager.entity.ChildAccountUser;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.Round;
import com.na.manager.entity.User;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.BetOrderStatus;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserType;
import com.na.manager.service.IBetOrderService;
import com.na.manager.util.SnowflakeIdWorker;

/**
 * @author andy
 * @date 2017年6月23日 下午4:34:11
 * 
 */
@Service
@Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
public class BetOrderServiceImpl implements IBetOrderService{
	private Logger logger = LoggerFactory.getLogger(BetOrderServiceImpl.class);
	@Autowired
	private IBetOrderMapper betOrderMapper;
	@Autowired
	private IRoundMapper roundMapper;
	@Autowired
	private IUserMapper userMapper;
	@Autowired
	private ILiveUserMapper liveUserMapper;
	@Autowired
	private IAccountRecordMapper accountRecordMapper;
	
	@Resource(name="accountRecordSnowflakeIdWorker")
    private SnowflakeIdWorker accountRecordSnowflakeIdWorker;
	
	@Override
	@Transactional(readOnly=true)
	public Page<BetOrderVO> queryBetOrderByPage(BetOrderReportRequest reportRequest) {
		Page<BetOrderVO> page = new Page<>(reportRequest);
		User currentUser = AppCache.getCurrentUser();
		if(currentUser.getUserTypeEnum() ==UserType.UNKNOWN || currentUser.getUserTypeEnum() == UserType.DEALER){
			return page; 
		}

		// 子账号归属父级节点
		if(currentUser.getUserTypeEnum() == UserType.SUB_ACCOUNT){
			ChildAccountUser childAccountUser = (ChildAccountUser)currentUser;
			currentUser =childAccountUser.getParentUser();
		}

		LiveUser liveUser = null;
		if(StringUtils.isNoneBlank(reportRequest.getAgentName())){
			liveUser =liveUserMapper.findLiveUserByUserName(reportRequest.getAgentName().trim());
			if(liveUser == null || !liveUser.getParentPath().contains("/"+currentUser.getId()+"/")){
				return page;
			}
		}else {
			liveUser = (LiveUser)currentUser;
		}

		reportRequest.setPath(liveUser.getParentPath());
		if(liveUser.getTypeEnum()== LiveUserType.MEMBER){
			reportRequest.setUserId(liveUser.getUserId());
		}else {
			reportRequest.setParentId(liveUser.getUserId());
		}
		
		if(StringUtils.isNotEmpty(reportRequest.getBetNo())){
			reportRequest.setBetNo(reportRequest.getBetNo().trim());
		}
		
		page.setTotal(betOrderMapper.getBetOrderCount(reportRequest));
		if(page.getTotal()==0){
			return page;
		}

		List<BetOrderVO> list = betOrderMapper.queryBetOrderByPage(reportRequest);
		BigDecimal betMoney=BigDecimal.ZERO;
		BigDecimal winLostMoney =BigDecimal.ZERO;
		
		for(BetOrderVO item : list) {
			convert(item);
			betMoney =betMoney.add(item.getAmount());
			winLostMoney=winLostMoney.add(item.getWinLostAmount());
		}
		page.setData(list);
		
		Map<String, BigDecimal> totalMap = betOrderMapper.queryBetOrderByPageTotal(reportRequest);
		Map<String, Object> staMap = new HashMap<>();
		
		staMap.put("betMoney", betMoney);
		staMap.put("winLostMoney", winLostMoney);
		staMap.put("totalBetMoney", totalMap.get("totalBetMoney"));
		staMap.put("totalWinLostMoney", totalMap.get("totalWinLostMoney"));
		page.setOtherInfo(staMap);

		return page;
	}
	
	@Override
	public Page<BetOrderVO> queryRemoteBetOrderByPage(BetOrderReportRequest reportRequest) {
		Page<BetOrderVO> page = new Page<>(reportRequest);
//		page.setTotal(betOrderMapper.getBetOrderCount(reportRequest));
//		if(page.getTotal()==0){
//			return page;
//		}
		List<BetOrderVO> list = betOrderMapper.queryBetOrderByPage(reportRequest);
		list.forEach(item->{
			convert(item);
		});
		page.setData(list);
		logger.info("推送大厅[{}]局下注[{}]条记录。",reportRequest.getRoundId(),list.size());
		return page;
	}

	private void convert(BetOrderVO item) {
		if(item.getTableId() ==122 && (item.getItemId() <642 || item.getItemId() >653)){ //部分轮盘显示详情
            item.setItemName(item.getAddition());
        }
		if(item.getGameId()==1){ //百家乐
            item.setBetNum(item.getBootsNum()+"-"+item.getRoundNum());
        }else{
            item.setBetNum(item.getRoundNum());
        }
	}

	@Override
	public Set<Long> invalidBetOrders(RoundCorrectDataRequest param) {
		Round round = roundMapper.getRoundById(param.getRid());
		List<BetOrder> betOrders = betOrderMapper.listBetOrders(param.getRid(),param.getGameTableId());
		Set<Long> ids = new HashSet<>();
		User user = null;
		for (BetOrder betOrder : betOrders) {
			AccountRecord accountRecord = new AccountRecord();
			user = userMapper.findUserById(betOrder.getUserId());
			// 修改订单取消
			//betOrder.setStatus(0);
			// 返回 用户消费金额
			accountRecord.setId(accountRecordSnowflakeIdWorker.nextId());
			accountRecord.setBusinessKey(betOrder.getId().toString());
			accountRecord.setRemark("{\"tableId\":"+round.getGameTableId()+",\"roundId\":"+round.getId()+"}");
			accountRecord.setSn("ZR"+accountRecord.getId());
			accountRecord.setPreBalance(user.getBalance());
			accountRecord.setTime(new Date());
			accountRecord.setAmount(betOrder.getAmount());
			accountRecord.setUserId(betOrder.getUserId());
			accountRecord.setType(AccountRecordType.REBACK.get());
			accountRecord.setExecUser(AppCache.getCurrentUser().getLoginName());
			user.setBalance(user.getBalance().add(betOrder.getAmount()));
			ids.add(accountRecord.getUserId());
			accountRecordMapper.add(accountRecord);
//			userMapper.update(user);
			userMapper.updateBalance(user.getId(), betOrder.getAmount());
			// 更新 用户的 取消返款金额
			betOrderMapper.updateBetOrderStatus(betOrder.getId(), BetOrderStatus.CANCEL.get());
			logger.info("处理异常订单："+betOrder.getId());
		}
		
		round.setStatus(7);
		roundMapper.updateRoundById(round);
		return ids;
	}

	@Override
	@Transactional(readOnly = true)
	public LiveBetResponse queryLiveBet() {
		String path = getCurrentUserPath();

		List<BaccaratLiveBean> baccaratLiveBeans = betOrderMapper.queryBaccartLiveInfo(path);
		List<RouletteLiveBetBean> rouletteLiveBetBeans = betOrderMapper.queryRouletteLiveInfo(path);
		int liveUserNum = betOrderMapper.countLive(path);

		LiveBetResponse res = new LiveBetResponse();
		res.setLiveUserNum(liveUserNum);
		res.addLiveBetBeans(baccaratLiveBeans);
		res.addLiveBetBeans(rouletteLiveBetBeans);
		for(LiveBetBean bean:res.getLiveBetBeans()){
			res.setBetUserNum(res.getBetUserNum()+bean.getUserNum());
			bean.setGameName(AppCache.getGame(bean.getGameId()).getName());
			bean.setTableName(AppCache.getGameTable(bean.getGameTableId()).getName());
		}
		return res;
	}

	/**
	 * 返回当前用户的路径。
	 * @return
	 */
	private String getCurrentUserPath() {
		LiveUser currentUser = null;
		User user = AppCache.getCurrentUser();
		if(user instanceof LiveUser){
			currentUser = (LiveUser)AppCache.getCurrentUser();
		}

		return currentUser==null ? "/" : currentUser.getParentPath();
	}

	@Override
	@Transactional(readOnly = true)
	public List<LiveBetOrder> queryTopBet(Long roundId) {
		return betOrderMapper.queryTopBet(roundId,getCurrentUserPath());
	}

	@Override
	public void updateModifiedUserBalance(Long userId) {
		betOrderMapper.updateModifiedUserBalance(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public BetOrderVO findBetOrderDetail(Long betId){
		BetOrderVO betOrderVO = betOrderMapper.findBetOrderDetail(betId,getCurrentUserPath());
		if(betOrderVO!=null){
			convert(betOrderVO);
		}
		return betOrderVO;
	}

	@Override
	public long findUnsettlementOrder(Long userId) {
		return betOrderMapper.findUnsettlementOrder(userId);
	}
}
