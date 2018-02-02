package com.na.manager.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.na.manager.bean.AccountRecordReportRequest;
import com.na.manager.bean.Page;
import com.na.manager.entity.AccountRecord;
import com.na.manager.entity.User;
import com.na.manager.enums.AccountRecordType;
import com.na.manager.enums.ChangeBalanceTypeEnum;

/**
 * @author andy
 * @date 2017年6月23日 上午10:15:59
 * 
 */
public interface IAccountRecordService {
	
	Page<AccountRecord> queryAccountRecordByPage(AccountRecordReportRequest reportRequest);
	
	void add(User user, AccountRecordType type,ChangeBalanceTypeEnum changeType,BigDecimal changeBalance, String remark);

	/**
	 * 查看指定用户的账户流水。
	 * @param startTime
	 * @param userId
	 * @return
	 */
	List<AccountRecord> findAccountRecordBy(Date startTime, Long userId);
	
	/**
	 * 查询单局所有流水
	 * @param roundId
	 * @return
	 */
	List<AccountRecord> findAccountRecordByRoundId(Long roundId);
	
	/**
	 * 修改流水状态为已推送
	 * @param list
	 */
	void updataFlag(List<AccountRecord> list);
}
