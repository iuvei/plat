package com.na.manager.remote.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.na.light.LightRpcService;
import com.na.manager.bean.BetOrderReportRequest;
import com.na.manager.bean.Page;
import com.na.manager.bean.vo.BetOrderVO;
import com.na.manager.cache.AppCache;
import com.na.manager.entity.LiveUser;
import com.na.manager.entity.MerchantUser;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.LiveUserIsBet;
import com.na.manager.enums.LiveUserSource;
import com.na.manager.enums.LiveUserType;
import com.na.manager.enums.UserType;
import com.na.manager.facade.IFinancialFacade;
import com.na.manager.remote.IMerchantRemote;
import com.na.manager.service.IBetOrderService;
import com.na.manager.service.ILiveUserService;
import com.na.manager.service.IMerchantUserService;
import com.na.manager.service.IUserService;

/**
 * 供API商户远程调用
 * 
 * @author Andy
 * @version 创建时间：2017年8月16日 下午3:24:17
 */
@LightRpcService(value = "merchantRemote", group = "remote")
public class MerchantRemoteImpl implements IMerchantRemote {
	@Autowired
	private IMerchantUserService merchantUserService;

	@Autowired
	private ILiveUserService liveUserService;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IFinancialFacade financialFacade;
	
	@Autowired
	private IBetOrderService betOrderService;

	@Override
	public MerchantUser getMerchantUser(String number, String privateKey) {
		return merchantUserService.getMerchantUser(number, privateKey);
	}

	@Override
	public Long addLiveUser(String userName, String password, String nickName, Long parentId) {
		LiveUser parentUser = liveUserService.findById(parentId);

		LiveUser liveUser = new LiveUser();
		liveUser.setLoginName(userName);
		liveUser.setNickName(nickName);
		liveUser.setParentId(parentId);
		liveUser.setSource(LiveUserSource.CASH);
		liveUser.setIsBet(LiveUserIsBet.TRUE);
		liveUser.setBiggestBalance(BigDecimal.ZERO);
		liveUser.setType(LiveUserType.MEMBER);
		liveUser.setChips(parentUser.getChips());
		liveUser.setPassword(password);
		liveUser.setCreateTime(new Date());
		liveUser.setCreateBy(parentUser.getLoginName());
		// API会员设置洗码比、占成比为0
		liveUser.setWashPercentage(BigDecimal.ZERO);
		liveUser.setIntoPercentage(BigDecimal.ZERO);

		liveUserService.add(liveUser);
		return liveUser.getId();
	}

	@Override
	public LiveUser getLiveUser(String userName) {
		return liveUserService.findLiveUserByUserName(userName);
	}

	@Override
	public void updatePassword(Long userId, String newPassword) {
		userService.updatePassword(userId, UserType.LIVE, newPassword);
	}

	@Override
	public void deposit(Long userId, Long parentId, BigDecimal amount, String transactionId) {
		financialFacade.updateBalance(userId, parentId, UserType.LIVE, AccountRecordType.INTO,ChangeBalanceTypeEnum.SELF, amount,
				transactionId);
	}

	@Override
	public void withdraw(Long userId, Long parentId, BigDecimal amount, String transactionId) {
		financialFacade.updateBalance(userId, parentId, UserType.LIVE, AccountRecordType.OUT,ChangeBalanceTypeEnum.SELF, amount, transactionId);
	}

	@Override
	public Page<BetOrderVO> pageBetOrder(String startTime, String endTime, Integer pageSize, Integer currentPage,
			Long parentId) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            LiveUser user = liveUserService.findById(parentId);
            AppCache.setCurrentUser(user);

            BetOrderReportRequest orderReportRequest = new BetOrderReportRequest();
            orderReportRequest.setLastUpdateTimeStart(sf.parse(startTime));
            orderReportRequest.setLastUpdateTimeEnd(sf.parse(endTime));
            orderReportRequest.setParentId(parentId);
            orderReportRequest.setPageSize(pageSize);
            orderReportRequest.setCurrentPage(currentPage);
            return betOrderService.queryBetOrderByPage(orderReportRequest);
        }catch (Exception e){
            throw new RuntimeException("查询订单失败",e);
        }
	}
}
