package com.na.user.socketserver.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.na.baccarat.socketserver.entity.AccountRecord;
import com.na.baccarat.socketserver.util.SnowflakeIdWorker;
import com.na.user.socketserver.dao.IAccountRecordMapper;
import com.na.user.socketserver.service.IAccountRecordService;

/**
 * Created by sunny on 2017/5/8 0008.
 */
@Service
public class AccountRecordServiceImpl implements IAccountRecordService{
    @Autowired
    private IAccountRecordMapper accountRecordMapper;
    @Resource(name = "accountRecordSnowflakeIdWorker")
    private SnowflakeIdWorker accountRecordSnowflakeIdWorker;

    @Override
    public void add(AccountRecord acountRecord) {
        acountRecord.setTime(new Date());
        acountRecord.setId(accountRecordSnowflakeIdWorker.nextId());
        accountRecordMapper.add(acountRecord);
    }

	@Override
	public void batchAddAcountRecord(List<AccountRecord> acountRecordList) {
		accountRecordMapper.batchAdd(acountRecordList);
	}
}
