package com.na.gate.service.impl;

import com.na.gate.dao.IPlatformSyncRecordMapper;
import com.na.gate.entity.PlatformSyncRecord;
import com.na.gate.service.IPlatformSyncRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/8/22 0022.
 */
@Service
public class PlatformSyncRecordServiceImpl implements IPlatformSyncRecordService {
    @Autowired
    private IPlatformSyncRecordMapper platformSyncRecordMapper;

    @Override
    public void add(PlatformSyncRecord syncRecord) {
        platformSyncRecordMapper.add(syncRecord);
    }

    @Override
    public PlatformSyncRecord findLastSyncRecord() {
        return platformSyncRecordMapper.findLastSyncRecord();
    }
}
