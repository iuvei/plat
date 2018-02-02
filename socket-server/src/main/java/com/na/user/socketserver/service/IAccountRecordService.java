package com.na.user.socketserver.service;

import java.util.List;

import com.na.baccarat.socketserver.entity.AccountRecord;

/**
 * 资金流水记录表。
 * Created by sunny on 2017/5/8 0008.
 */
public interface IAccountRecordService {
    void add(AccountRecord acountRecord);

	void batchAddAcountRecord(List<AccountRecord> acountRecordList);
}
