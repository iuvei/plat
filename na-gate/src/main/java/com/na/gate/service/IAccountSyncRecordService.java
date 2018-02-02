package com.na.gate.service;

import java.util.Date;
import java.util.List;

import com.na.gate.entity.AccountSyncRecord;

/**
 * 平台用户登录记录服务。
 * Created by sunny on 2017/8/9 0009.
 */
public interface IAccountSyncRecordService {
    void add(Long userId,Long id);
    
    void batchAdd(List<AccountSyncRecord> list);

    Date findLastSyncTime(Long userId);
    
    List<AccountSyncRecord> queryList(Long userId,String loginTime);
}
