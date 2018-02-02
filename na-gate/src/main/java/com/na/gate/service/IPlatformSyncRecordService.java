package com.na.gate.service;

import com.na.gate.entity.PlatformSyncRecord;

/**
 * Created by sunny on 2017/8/22 0022.
 */
public interface IPlatformSyncRecordService {
    void add(PlatformSyncRecord syncRecord);

    PlatformSyncRecord findLastSyncRecord();
}
