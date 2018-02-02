package com.na.gate.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.gate.dao.IAccountSyncRecordMapper;
import com.na.gate.entity.AccountSyncRecord;
import com.na.gate.service.IAccountSyncRecordService;

/**
* @author Andy
* @version 创建时间：2017年10月6日 下午1:50:26
*/
@Service
public class AccountSyncRecordService implements IAccountSyncRecordService {

	@Autowired
	private IAccountSyncRecordMapper accountSyncRecordMapper;
	
	@Override
	public void add(Long userId,Long id) {
		accountSyncRecordMapper.add(userId,id);
	}

	@Override
	public void batchAdd(List<AccountSyncRecord> list) {
		accountSyncRecordMapper.batchAdd(list);
	}
	
	@Override
	public Date findLastSyncTime(Long userId) {
		return accountSyncRecordMapper.findLastSyncTime(userId);
	}

	@Override
	public List<AccountSyncRecord> queryList(Long userId, String loginTime) {
		return accountSyncRecordMapper.queryList(userId, loginTime);
	}
}
