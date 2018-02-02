package com.na.manager.facade;

import java.math.BigDecimal;

import com.na.manager.bean.RoundCorrectDataRequest;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;
import com.na.manager.enums.UserType;

/**
 * @author andy
 * @date 2017年7月3日 下午3:07:17
 * 
 */
public interface IFinancialFacade {
	/**
	 * 增量更新余额
	 * @param userId
	 * @param parentId
	 * @param userType
	 * @param accountRecordType
	 * @param changeType
	 * @param balance
	 * @param remark
	 */
	void updateBalance(Long userId, Long parentId, UserType userType, AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType, BigDecimal balance, String remark);
	
	/**
	 * 全量覆盖余额
	 * @param userId
	 * @param parentId
	 * @param userType
	 * @param accountRecordType
	 * @param changeType
	 * @param balance
	 * @param remark
	 */
	void modifyBalance(Long userId, Long parentId, UserType userType, AccountRecordType accountRecordType,ChangeBalanceTypeEnum changeType, BigDecimal balance, String remark);
	
	void invalidBetOrders(RoundCorrectDataRequest param);
}
